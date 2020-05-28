
package org.springframework.samples.petclinic.web;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Announcement;
import org.springframework.samples.petclinic.model.Answer;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.AnnouncementService;
import org.springframework.samples.petclinic.service.AnswerService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = AnswerController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class AnswerControllerTests {

	private static final int	TEST_OWNER_ID	= 1;

	@MockBean
	private AnswerService		answerService;

	@MockBean
	private PetService			petService;

	@MockBean
	private AnnouncementService	announcementService;

	@MockBean
	private OwnerService		ownerService;

	@Autowired
	private MockMvc				mockMvc;

	private Owner				george;


	public Answer createDummyAnswer(final String name) {
		Answer answer = new Answer();
		answer.setName(name);
		return answer;
	}

	public Announcement createDummyAnnouncement(final String name) {
		Announcement announcement = new Announcement();
		announcement.setName(name);
		announcement.setCanBeAdopted(true);
		announcement.setType(new PetType());
		return announcement;
	}

	public Pet createDummyPet(final String name) {
		Pet pet = new Pet();
		pet.setName(name);
		pet.setIsVaccinated(false);
		pet.setDangerous(false);

		return pet;
	}

	public Owner createDummyOwner() {
		Owner owner = new Owner();
		owner.setId(2);
		owner.setFirstName("David");
		owner.setLastName("Franklin");
		owner.setAddress("110 W. Liberty St.");
		owner.setCity("Madison");
		owner.setTelephone("6085551023");
		User owneruser = new User();
		owneruser.setUsername("owner");
		owner.setUser(owneruser);
		owner.setDangerousAnimal(true);
		owner.setLivesInCity(true);
		owner.setPositiveHistory(true);
		owner.setNumerousAnimal(true);

		return owner;
	}

	@BeforeEach
	void setup() {

		this.george = new Owner();
		this.george.setId(AnswerControllerTests.TEST_OWNER_ID);
		this.george.setFirstName("George");
		this.george.setLastName("Franklin");
		this.george.setAddress("110 W. Liberty St.");
		this.george.setCity("Madison");
		this.george.setTelephone("6085551023");
		User georgeuser = new User();
		georgeuser.setUsername("george");
		this.george.setUser(georgeuser);
		this.george.setDangerousAnimal(true);
		this.george.setLivesInCity(true);
		this.george.setPositiveHistory(true);

	}

	// mostrarAnswers

	@WithMockUser(value = "george")
	@Test
	void shouldShowAnswers() throws Exception {
		Answer answer1 = this.createDummyAnswer("Answer1");
		Answer answer2 = this.createDummyAnswer("Answer2");
		Announcement dummyAnnouncement = this.createDummyAnnouncement("Announcement1");
		dummyAnnouncement.setOwner(this.george);

		Mockito.when(this.announcementService.findAnnouncementById(1)).thenReturn(Optional.of(dummyAnnouncement));
		Mockito.when(this.ownerService.findOwnerByUserName("george")).thenReturn(this.george);
		Mockito.when(this.answerService.findAnswerByAnnouncement(dummyAnnouncement))//
			.thenReturn(Lists.newArrayList(answer1, answer2));

		this.mockMvc.perform(MockMvcRequestBuilders.get("/announcements/{announcementId}/answers", 1))//
			.andExpect(MockMvcResultMatchers.status().isOk())//
			.andExpect(MockMvcResultMatchers.model().attributeExists("answers"))//
			.andExpect(MockMvcResultMatchers.view().name("answers/answersList"));
	}

	@WithMockUser(value = "george")
	@Test
	void shouldNotShowAnswersWhenYouHaveNotAnswers() throws Exception {

		Announcement dummyAnnouncement = this.createDummyAnnouncement("Announcement1");
		dummyAnnouncement.setOwner(this.george);

		Mockito.when(this.announcementService.findAnnouncementById(1))//
			.thenReturn(Optional.of(dummyAnnouncement));
		Mockito.when(this.answerService.findAnswerByAnnouncement(ArgumentMatchers.any()))//
			.thenThrow(NoSuchElementException.class);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/announcements/{announcementId}/answers", 1))//
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model()//
				.attributeExists("isempty"))
			.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("answers"))//
			.andExpect(MockMvcResultMatchers.view().name("answers/answersList"));
	}

	@WithMockUser(value = "spring")
	@Test
	void shouldNotShowAnotherUserAnswer() throws Exception {
		Announcement dummyAnnouncement = this.createDummyAnnouncement("Anuncio1");
		dummyAnnouncement.setOwner(this.george);

		Mockito.when(this.announcementService.findAnnouncementById(1))//
			.thenReturn(Optional.of(dummyAnnouncement));

		this.mockMvc.perform(MockMvcRequestBuilders.get("/announcements/{announcementId}/answers", 1))//
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model()//
				.attribute("message", "You cannot access another user's announcement answers"))//
			.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	// Get createInscription

	@WithMockUser(value = "owner")
	@Test
	void shouldCreateAnswer() throws Exception {

		Announcement dummyAnnouncement = this.createDummyAnnouncement("dummyAnnouncement");
		dummyAnnouncement.setCanBeAdopted(true);
		dummyAnnouncement.setOwner(this.george);
		Owner owner = this.createDummyOwner();

		Mockito.when(this.announcementService.findAnnouncementById(1)).thenReturn(Optional.of(dummyAnnouncement));
		Mockito.when(this.ownerService.findOwnerByUserName("owner")).thenReturn(owner);
		Mockito.when(this.answerService.findAnswerByAnnouncement(dummyAnnouncement))//
			.thenReturn(new ArrayList<Answer>());

		this.mockMvc.perform(MockMvcRequestBuilders.get("/announcements/{announcementId}/answer/new", 1))//
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model()//
				.attributeExists("answer"))
			.andExpect(MockMvcResultMatchers.view().name("answers/editAnswer"));
	}

	@WithMockUser(value = "george")
	@Test
	void shouldNotCreateAnswerWhenAnnouncementNotFound() throws Exception {

		Mockito.when(this.announcementService.findAnnouncementById(1)).thenThrow(NoSuchElementException.class);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/announcements/{announcementId}/answer/new", 1))//
			.andExpect(MockMvcResultMatchers.status().isOk())//
			//.andExpect(MockMvcResultMatchers.model().attributeExists("message"))//
			.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(value = "george")
	@Test
	void shouldNotCreateAnswerWhenAnnouncementAnswersNotFound() throws Exception {

		Announcement dummyAnnouncement = this.createDummyAnnouncement("dummyAnnouncement");
		dummyAnnouncement.setOwner(this.george);

		Mockito.when(this.announcementService.findAnnouncementById(1)).thenThrow(NoSuchElementException.class);
		Mockito.when(this.ownerService.findOwnerByUserName("george")).thenReturn(this.george);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/announcements/{announcementId}/answer/new", 1))//
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(value = "owner")
	@Test
	void shouldNotCreateAnswerWhenAnnouncementPetCanNotBeAdopted() throws Exception {

		Announcement dummyAnnouncement = this.createDummyAnnouncement("dummyAnnouncement");
		dummyAnnouncement.setOwner(this.george);
		dummyAnnouncement.setCanBeAdopted(false);

		Owner owner = this.createDummyOwner();

		Mockito.when(this.announcementService.findAnnouncementById(1)).thenReturn(Optional.of(dummyAnnouncement));
		Mockito.when(this.ownerService.findOwnerByUserName("owner")).thenReturn(owner);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/announcements/{announcementId}/answer/new", 1))//
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attribute("message", "You can't adopt this pet because it can't be adopted"))//
			.andExpect(MockMvcResultMatchers.view().name("/exception"));
	}

	@WithMockUser(value = "owner")
	@Test
	void shouldNotCreateAnswerWhenOwnerHasBadHistory() throws Exception {

		Announcement dummyAnnouncement = this.createDummyAnnouncement("dummyAnnouncement");
		Owner dummyOwner = this.createDummyOwner();
		dummyAnnouncement.setOwner(this.george);
		dummyOwner.setPositiveHistory(false);

		Mockito.when(this.announcementService.findAnnouncementById(1)).thenReturn(Optional.of(dummyAnnouncement));
		Mockito.when(this.ownerService.findOwnerByUserName("owner")).thenReturn(dummyOwner);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/announcements/{announcementId}/answer/new", 1))//
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attribute("message", "You can't adopt a pet if you have a bad history"))//
			.andExpect(MockMvcResultMatchers.view().name("/exception"));
	}

	@WithMockUser(value = "owner")
	@Test
	void shouldNotCreateAnswerWhenYouHavePetsNotVaccinated() throws Exception {

		Announcement dummyAnnouncement = this.createDummyAnnouncement("dummyAnnouncement");
		Owner owner = this.createDummyOwner();
		Pet pet = this.createDummyPet("Puppy");
		owner.addPet(pet);
		dummyAnnouncement.setOwner(this.george);

		Mockito.when(this.announcementService.findAnnouncementById(1)).thenReturn(Optional.of(dummyAnnouncement));
		Mockito.when(this.ownerService.findOwnerByUserName("owner")).thenReturn(owner);
		Mockito.when(this.petService.findPets("owner")).thenReturn(Lists.newArrayList(pet));
		Mockito.when(this.answerService.findAnswerByOwner(owner)).thenReturn(Lists.newArrayList());

		this.mockMvc.perform(MockMvcRequestBuilders.get("/announcements/{announcementId}/answer/new", 1))//
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model()//
				.attribute("message", "You can't send an answer if any of your pets aren't vaccinated"))//
			.andExpect(MockMvcResultMatchers.view().name("/exception"));
	}

	@WithMockUser(value = "owner")
	@Test
	void shouldNotCreateAnswerWhenYouOneAnswerToTheSameAnnouncement() throws Exception {

		Announcement dummyAnnouncement = this.createDummyAnnouncement("dummyAnnouncement");
		Owner owner = this.createDummyOwner();
		Answer dummyAnswer = this.createDummyAnswer("Answer1");
		dummyAnswer.setAnnouncement(dummyAnnouncement);
		dummyAnswer.setOwner(owner);
		dummyAnnouncement.setOwner(this.george);

		Mockito.when(this.announcementService.findAnnouncementById(1)).thenReturn(Optional.of(dummyAnnouncement));
		Mockito.when(this.ownerService.findOwnerByUserName("owner")).thenReturn(owner);
		Mockito.when(this.petService.findPets("owner")).thenReturn(Lists.newArrayList());
		Mockito.when(this.answerService.findAnswerByOwner(owner)).thenReturn(Lists.newArrayList(dummyAnswer));

		this.mockMvc.perform(MockMvcRequestBuilders.get("/announcements/{announcementId}/answer/new", 1))//
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model()//
				.attribute("message", "You can't send more than one answer to the same announcement"))//
			.andExpect(MockMvcResultMatchers.view().name("/exception"));
	}

	@WithMockUser(value = "george")
	@Test
	void shouldNotCreateAnswerOfYourOwnAnnouncement() throws Exception {

		Announcement dummyAnnouncement = this.createDummyAnnouncement("dummyAnnouncement");
		dummyAnnouncement.setOwner(this.george);

		Mockito.when(this.announcementService.findAnnouncementById(1)).thenReturn(Optional.of(dummyAnnouncement));
		Mockito.when(this.ownerService.findOwnerByUserName("george")).thenReturn(this.george);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/announcements/{announcementId}/answer/new", 1))//
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attribute("message", "You can't answer your own announcement"))//
			.andExpect(MockMvcResultMatchers.view().name("/exception"));
	}

	// Post createInscription

	@WithMockUser(value = "george")
	@Test
	void shouldSaveAnswer() throws Exception {
		Announcement dummyAnnouncement = this.createDummyAnnouncement("dummyAnnouncement");
		dummyAnnouncement.setCanBeAdopted(true);
		Answer answer = this.createDummyAnswer("Answer1");
		answer.setDate(LocalDate.of(2018, 10, 01));
		answer.setDescription("This is a description");

		Mockito.when(this.announcementService.findAnnouncementById(1)).thenReturn(Optional.of(dummyAnnouncement));
		Mockito.when(this.ownerService.findOwnerByUserName("geoge")).thenReturn(this.george);

		this.mockMvc.perform(MockMvcRequestBuilders.post("/announcements/{announcementId}/answer/new", 1)//
			.with(SecurityMockMvcRequestPostProcessors.csrf())//
			.param("description", "This is a description")//
			.param("date", "2018/10/01"))//
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())//
			.andExpect(MockMvcResultMatchers.view()//
				.name("redirect:/announcements/{announcementId}"));

	}

	@WithMockUser(value = "george")
	@Test
	void shouldNotSaveAnswerWithErrors() throws Exception {
		Announcement dummyAnnouncement = this.createDummyAnnouncement("dummyAnnouncement");
		dummyAnnouncement.setCanBeAdopted(true);
		Answer answer = this.createDummyAnswer("Answer1");
		answer.setDate(LocalDate.of(2021, 10, 01));
		answer.setDescription("This is a description");

		Mockito.when(this.announcementService.findAnnouncementById(1)).thenReturn(Optional.of(dummyAnnouncement));
		Mockito.when(this.ownerService.findOwnerByUserName("geoge")).thenReturn(this.george);

		this.mockMvc.perform(MockMvcRequestBuilders.post("/announcements/{announcementId}/answer/new", 1)//
			.with(SecurityMockMvcRequestPostProcessors.csrf())//
			.param("description", "This is a description")//
			.param("date", "2021/10/01"))//
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())//
			.andExpect(MockMvcResultMatchers.model().attributeExists("answer"))//
			.andExpect(MockMvcResultMatchers.view()//
				.name("answers/editAnswer"));
	}

	@WithMockUser(value = "george")
	@Test
	void shouldNotSaveAnswerWithIncorrectAnnouncement() throws Exception {
		Announcement dummyAnnouncement = this.createDummyAnnouncement("dummyAnnouncement");
		dummyAnnouncement.setCanBeAdopted(true);
		Answer answer = this.createDummyAnswer("Answer1");
		answer.setDate(LocalDate.of(2021, 10, 01));
		answer.setDescription("This is a description");

		Mockito.when(this.announcementService.findAnnouncementById(1)).thenThrow(NoSuchElementException.class);
		Mockito.when(this.ownerService.findOwnerByUserName("geoge")).thenReturn(this.george);

		this.mockMvc.perform(MockMvcRequestBuilders.post("/announcements/{announcementId}/answer/new", 1)//
			.with(SecurityMockMvcRequestPostProcessors.csrf())//
			.param("description", "This is a description")//
			.param("date", "2018/10/01"))//
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())//
			//.andExpect(MockMvcResultMatchers.model().attributeExists("message"))//
			.andExpect(MockMvcResultMatchers.view()//
				.name("exception"));

	}

}
