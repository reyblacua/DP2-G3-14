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
import org.springframework.samples.petclinic.model.Course;
import org.springframework.samples.petclinic.model.Inscription;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.CourseService;
import org.springframework.samples.petclinic.service.InscriptionsService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = InscriptionController.class,
excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
classes = WebSecurityConfigurer.class),
excludeAutoConfiguration = SecurityConfiguration.class)
class InscriptionControllerTests {

	private static final int TEST_OWNER_ID = 1;

	@MockBean
	private InscriptionsService	inscriptionService;

	@MockBean
	private PetService petService;

	@MockBean
	private CourseService courseService;

	@MockBean
	private OwnerService ownerService;

	@Autowired
	private MockMvc mockMvc;

	private Owner george;

	private Pet lillie;

	public Inscription createDummyInscription(final String name) {
		Inscription inscription = new Inscription();
		inscription.setName(name);
		return inscription;
	}

	public Course createDummyCourse(final String name) {
		Course course = new Course();
		course.setName(name);
		return course;
	}

	@BeforeEach
	void setup() {

		this.george = new Owner();
		this.george.setId(InscriptionControllerTests.TEST_OWNER_ID);
		this.george.setFirstName("George");
		this.george.setLastName("Franklin");
		this.george.setAddress("110 W. Liberty St.");
		this.george.setCity("Madison");
		this.george.setTelephone("6085551023");
		User georgeuser = new User();
		georgeuser.setUsername("george");
		this.george.setUser(georgeuser);
		this.george.setId(1);

		this.lillie = new Pet();
		this.lillie.setName("Lillie");
		this.lillie.setOwner(this.george);
		this.lillie.setId(1);

	}

	// mostrarInscriptions

	@WithMockUser(value = "spring")
	@Test
	void shouldShowInscriptions() throws Exception {
		Inscription inscription1 = this.createDummyInscription("Inscription1");
		Inscription inscription2 = this.createDummyInscription("Inscription2");

		Mockito.when(this.inscriptionService.findInscriptionsByOwner(ArgumentMatchers.any())).thenReturn(Lists.newArrayList(inscription1, inscription2));

		this.mockMvc.perform(MockMvcRequestBuilders.get("/inscriptions"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attributeExists("inscriptions"))
		.andExpect(MockMvcResultMatchers.view().name("inscriptions/inscriptionsList"));
	}

	@WithMockUser(value = "spring")
	@Test
	void shouldNotShowInscriptionsWhenYouHaveNotInscriptions() throws Exception {

		Mockito.when(this.inscriptionService.findInscriptionsByOwner(ArgumentMatchers.any())).thenThrow(NoSuchElementException.class);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/inscriptions"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attributeExists("isempty"))
		.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("inscriptions"))
		.andExpect(MockMvcResultMatchers.view().name("inscriptions/inscriptionsList"));
	}

	// mostrarInscription

	@WithMockUser(value = "george")
	@Test
	void shouldShowInscriptionDetails() throws Exception {
		Inscription inscription1 = this.createDummyInscription("Inscription1");
		inscription1.setOwner(this.george);

		Mockito.when(this.inscriptionService.findInscriptionById(1)).thenReturn(Optional.of(inscription1));

		this.mockMvc.perform(MockMvcRequestBuilders.get("/inscriptions/{inscriptionId}",1))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attributeExists("inscription"))
		.andExpect(MockMvcResultMatchers.view().name("inscriptions/inscriptionDetails"));
	}

	@WithMockUser(value = "spring")
	@Test
	void shouldNotShowAnotherUserInscription() throws Exception {
		Inscription inscription1 = this.createDummyInscription("Inscription1");
		inscription1.setOwner(this.george);

		Mockito.when(this.inscriptionService.findInscriptionById(1)).thenReturn(Optional.of(inscription1));

		this.mockMvc.perform(MockMvcRequestBuilders.get("/inscriptions/{inscriptionId}",1))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attribute("message", "You cannot access another user's inscription details"))
		.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(value = "spring")
	@Test
	void shouldNotShowInscriptionWhenNotFound() throws Exception {
		Mockito.when(this.inscriptionService.findInscriptionById(1)).thenThrow(NoSuchElementException.class);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/inscriptions/{inscriptionId}",1))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attribute("message", "Inscription not found"))
		.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	// Get createInscription

	@WithMockUser(value = "george")
	@Test
	void shouldCreateInscription() throws Exception {

		Course dummyCourse = this.createDummyCourse("dummycourse");
		dummyCourse.setCapacity(20);

		Mockito.when(this.courseService.findCourseById(1)).thenReturn(Optional.of(dummyCourse));
		Mockito.when(this.ownerService.findOwnerByUserName("george")).thenReturn(this.george);
		Mockito.when(this.inscriptionService.findInscriptionsByOwner(this.george)).thenReturn(new ArrayList<Inscription>());
		Mockito.when(this.inscriptionService.findInscriptionsByCourse(dummyCourse)).thenReturn(new ArrayList<Inscription>());
		Mockito.when(this.petService.findPets("george")).thenReturn(Lists.newArrayList(this.lillie));

		this.mockMvc.perform(MockMvcRequestBuilders.get("/courses/{courseId}/inscription/new", 1))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attributeExists("inscription"))
		.andExpect(MockMvcResultMatchers.view().name("inscriptions/editInscription"));
	}

	@WithMockUser(value = "george")
	@Test
	void shouldNotCreateInscriptionWhenCourseNotFound() throws Exception {

		Course dummyCourse = this.createDummyCourse("dummycourse");
		dummyCourse.setCapacity(20);

		Mockito.when(this.courseService.findCourseById(1)).thenThrow(NoSuchElementException.class);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/courses/{courseId}/inscription/new", 1))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attribute("message","There are errors validating data"))
		.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(value = "george")
	@Test
	void shouldNotCreateInscriptionWhenOwnerHasNoPets() throws Exception {

		Course dummyCourse = this.createDummyCourse("dummycourse");
		dummyCourse.setCapacity(20);

		Mockito.when(this.courseService.findCourseById(1)).thenReturn(Optional.of(dummyCourse));
		Mockito.when(this.ownerService.findOwnerByUserName("george")).thenReturn(this.george);
		Mockito.when(this.inscriptionService.findInscriptionsByOwner(this.george)).thenReturn(new ArrayList<Inscription>());
		Mockito.when(this.inscriptionService.findInscriptionsByCourse(dummyCourse)).thenReturn(new ArrayList<Inscription>());
		Mockito.when(this.petService.findPets("george")).thenReturn(new ArrayList<Pet>());

		this.mockMvc.perform(MockMvcRequestBuilders.get("/courses/{courseId}/inscription/new", 1))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attribute("message","You have no pets to sign up in a course"))
		.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(value = "george")
	@Test
	void shouldNotCreateInscriptionWithOtherInscriptionsNotPaid() throws Exception {

		Course dummyCourse = this.createDummyCourse("dummycourse");
		dummyCourse.setCapacity(20);

		Inscription dummyInscription = this.createDummyInscription("inscriptionNotPaid");
		dummyInscription.setIsPaid(false);

		Mockito.when(this.courseService.findCourseById(1)).thenReturn(Optional.of(dummyCourse));
		Mockito.when(this.ownerService.findOwnerByUserName("george")).thenReturn(this.george);
		Mockito.when(this.inscriptionService.findInscriptionsByOwner(this.george)).thenReturn(Lists.newArrayList(dummyInscription));
		Mockito.when(this.inscriptionService.findInscriptionsByCourse(dummyCourse)).thenReturn(new ArrayList<Inscription>());
		Mockito.when(this.petService.findPets("george")).thenReturn(Lists.newArrayList(this.lillie));

		this.mockMvc.perform(MockMvcRequestBuilders.get("/courses/{courseId}/inscription/new", 1))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attribute("message","You have to pay previous courses inscriptions"))
		.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(value = "george")
	@Test
	void shouldNotCreateInscriptionForAFullCourse() throws Exception {

		Course dummyCourse = this.createDummyCourse("dummycourse");
		dummyCourse.setCapacity(0);

		Mockito.when(this.courseService.findCourseById(1)).thenReturn(Optional.of(dummyCourse));
		Mockito.when(this.ownerService.findOwnerByUserName("george")).thenReturn(this.george);
		Mockito.when(this.inscriptionService.findInscriptionsByOwner(this.george)).thenReturn(new ArrayList<Inscription>());
		Mockito.when(this.inscriptionService.findInscriptionsByCourse(dummyCourse)).thenReturn(new ArrayList<Inscription>());
		Mockito.when(this.petService.findPets("george")).thenReturn(Lists.newArrayList(this.lillie));

		this.mockMvc.perform(MockMvcRequestBuilders.get("/courses/{courseId}/inscription/new", 1))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attribute("message","The course is full"))
		.andExpect(MockMvcResultMatchers.view().name("exception"));
	}


	// Post createInscription

	@WithMockUser(value = "george")
	@Test
	void shouldSaveInscription() throws Exception {
		Inscription dummyInscription = this.createDummyInscription("inscription");
		dummyInscription.setPet(this.lillie);
		dummyInscription.setDate(LocalDate.of(2015, 2, 12));
		dummyInscription.setIsPaid(false);

		Course dummyCourse = this.createDummyCourse("dummycourse");
		dummyCourse.setCapacity(10);

		Mockito.when(this.courseService.findCourseById(1)).thenReturn(Optional.of(dummyCourse));

		this.mockMvc.perform(MockMvcRequestBuilders.post("/courses/{courseId}/inscription/new", 1)
			.with(SecurityMockMvcRequestPostProcessors.csrf())
			.param("date", "2015/02/12")
			.param("isPaid", "false")
			.param("pet.id", this.lillie.getId().toString()))

		.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
		.andExpect(MockMvcResultMatchers.view().name("redirect:/inscriptions"));
	}

	@WithMockUser(value = "george")
	@Test
	void shouldNotSaveInscriptionWhenServiceErrors() throws Exception {
		Inscription dummyInscription = this.createDummyInscription("inscription");
		dummyInscription.setPet(this.lillie);
		dummyInscription.setDate(LocalDate.of(2015, 2, 12));
		dummyInscription.setIsPaid(false);

		Course dummyCourse = this.createDummyCourse("dummycourse");
		dummyCourse.setCapacity(10);

		Mockito.when(this.courseService.findCourseById(1)).thenReturn(Optional.of(dummyCourse));
		Mockito.doThrow(Exception.class).when(this.inscriptionService).saveInscription(ArgumentMatchers.any(Inscription.class));

		this.mockMvc.perform(MockMvcRequestBuilders.post("/courses/{courseId}/inscription/new", 1)
			.with(SecurityMockMvcRequestPostProcessors.csrf())
			.param("date", "2015/02/12")
			.param("isPaid", "false")
			.param("pet.id", this.lillie.getId().toString()))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attributeExists("message"))
		.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(value = "george")
	@Test
	void shouldNotSaveInscriptionWithFormErrors() throws Exception {

		Course dummyCourse = this.createDummyCourse("dummycourse");
		dummyCourse.setCapacity(10);

		Mockito.when(this.courseService.findCourseById(1)).thenReturn(Optional.of(dummyCourse));

		this.mockMvc.perform(MockMvcRequestBuilders.post("/courses/{courseId}/inscription/new", 1)
			.with(SecurityMockMvcRequestPostProcessors.csrf()))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attributeHasErrors("inscription"))
		.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("inscription", "date"))
		.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("inscription", "isPaid"))
		.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("inscription", "pet"))
		.andExpect(MockMvcResultMatchers.view().name("inscriptions/editInscription"));
	}



	// deleteInscription

	@WithMockUser(value = "george")
	@Test
	void shouldDeleteInscription() throws Exception {
		Inscription inscription1 = this.createDummyInscription("Inscription1");
		inscription1.setOwner(this.george);

		Mockito.when(this.inscriptionService.findInscriptionById(1)).thenReturn(Optional.of(inscription1));
		Mockito.doNothing().when(this.inscriptionService).deleteInscription(inscription1);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/inscriptions/delete/{inscriptionId}",1))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
		.andExpect(MockMvcResultMatchers.view().name("redirect:/inscriptions"));
	}

	@WithMockUser(value = "spring")
	@Test
	void shouldNotDeleteInscriptionOfOtherUser() throws Exception {
		Inscription inscription1 = this.createDummyInscription("Inscription1");
		inscription1.setOwner(this.george);

		Mockito.when(this.inscriptionService.findInscriptionById(1)).thenReturn(Optional.of(inscription1));
		this.mockMvc.perform(MockMvcRequestBuilders.get("/inscriptions/delete/{inscriptionId}",1))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attribute("message","You cannot delete another user's inscription details"))
		.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(value = "george")
	@Test
	void shouldNotDeleteInscriptionWhenNotFound() throws Exception {
		Inscription inscription1 = this.createDummyInscription("Inscription1");
		inscription1.setOwner(this.george);

		Mockito.when(this.inscriptionService.findInscriptionById(200)).thenThrow(NoSuchElementException.class);
		this.mockMvc.perform(MockMvcRequestBuilders.get("/inscriptions/delete/{inscriptionId}",200))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attribute("message","Inscription not found"))
		.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

}
