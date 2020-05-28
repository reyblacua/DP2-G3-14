
package org.springframework.samples.petclinic.web.e2e;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
class AnswerControllerE2ETests {

	@Autowired
	private MockMvc mockMvc;


	@WithMockUser(username = "owner1", password = "0wn3r")
	@Test
	void shouldShowAnswers() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/announcements/{announcementId}/answers", 1))//
		.andExpect(MockMvcResultMatchers.status().isOk())//
		.andExpect(MockMvcResultMatchers.model().attributeExists("answers"))//
		.andExpect(MockMvcResultMatchers.view().name("answers/answersList"));
	}

	@WithMockUser(username = "owner3", password = "0wn3r")
	@Test
	void shouldNotShowAnswersWhenYouHaveNotAnswers() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/announcements/{announcementId}/answers", 3))//
		.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model()//
			.attributeExists("isempty"))
		.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("answers"))//
		.andExpect(MockMvcResultMatchers.view().name("answers/answersList"));
	}

	@WithMockUser(username = "owner2", password = "0wn3r")
	@Test
	void shouldNotShowAnotherUserAnswer() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/announcements/{announcementId}/answers", 1))//
		.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model()//
			.attribute("message", "You cannot access another user's announcement answers"))//
		.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = "owner7", password = "0wn3r")
	@Test
	void shouldCreateAnswer() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/announcements/{announcementId}/answer/new", 1))//
		.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model()//
			.attributeExists("answer"))
		.andExpect(MockMvcResultMatchers.view().name("answers/editAnswer"));
	}

	@WithMockUser(username = "owner1", password = "0wn3r")
	@Test
	void shouldNotCreateAnswerWhenAnnouncementNotFound() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/announcements/{announcementId}/answer/new", 200))//
		.andExpect(MockMvcResultMatchers.status().isOk())//
		//.andExpect(MockMvcResultMatchers.model().attributeExists("message"))//
		.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = "owner5", password = "0wn3r")
	@Test
	void shouldNotCreateAnswerWhenAnnouncementAnswersNotFound() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/announcements/{announcementId}/answer/new", 200))//
		.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = "owner5", password = "0wn3r")
	@Test
	void shouldNotCreateAnswerWhenAnnouncementPetCanNotBeAdopted() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/announcements/{announcementId}/answer/new", 3))//
		.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attribute("message", "You can't adopt this pet because it can't be adopted"))//
		.andExpect(MockMvcResultMatchers.view().name("/exception"));
	}

	@WithMockUser(username = "owner4", password = "0wn3r")
	@Test
	void shouldNotCreateAnswerWhenOwnerHasBadHistory() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/announcements/{announcementId}/answer/new", 1))//
		.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attribute("message", "You can't adopt a pet if you have a bad history"))//
		.andExpect(MockMvcResultMatchers.view().name("/exception"));
	}

	@WithMockUser(username = "owner6", password = "0wn3r")
	@Test
	void shouldNotCreateAnswerWhenYouHavePetsNotVaccinated() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/announcements/{announcementId}/answer/new", 1))//
		.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model()//
			.attribute("message", "You can't send an answer if any of your pets aren't vaccinated"))//
		.andExpect(MockMvcResultMatchers.view().name("/exception"));
	}

	@WithMockUser(username = "owner1", password = "0wn3r")
	@Test
	void shouldNotCreateAnswerWhenYouOneAnswerToTheSameAnnouncement() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/announcements/{announcementId}/answer/new", 2))//
		.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model()//
			.attribute("message", "You can't send more than one answer to the same announcement"))//
		.andExpect(MockMvcResultMatchers.view().name("/exception"));
	}

	@WithMockUser(username = "owner1", password = "0wn3r")
	@Test
	void shouldNotCreateAnswerOfYourOwnAnnouncement() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/announcements/{announcementId}/answer/new", 1))//
		.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attribute("message", "You can't answer your own announcement"))//
		.andExpect(MockMvcResultMatchers.view().name("/exception"));
	}

	@WithMockUser(username = "owner5", password = "0wn3r")
	@Test
	void shouldSaveAnswer() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.post("/announcements/{announcementId}/answer/new", 1)//
			.with(SecurityMockMvcRequestPostProcessors.csrf())//
			.param("description", "This is a description")//
			.param("date", "2018/10/01"))//
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection())//
		.andExpect(MockMvcResultMatchers.view()//
			.name("redirect:/announcements/{announcementId}"));
	}

	@WithMockUser(username = "owner5", password = "0wn3r")
	@Test
	void shouldNotSaveAnswerWithErrors() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.post("/announcements/{announcementId}/answer/new", 1)//
			.with(SecurityMockMvcRequestPostProcessors.csrf())//
			.param("description", "This is a description")//
			.param("date", "2021/10/01"))//
		.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())//
		.andExpect(MockMvcResultMatchers.model().attributeExists("answer"))//
		.andExpect(MockMvcResultMatchers.view()//
			.name("answers/editAnswer"));
	}

	@WithMockUser(username = "owner5", password = "0wn3r")
	@Test
	void shouldNotSaveAnswerWithIncorrectAnnouncement() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.post("/announcements/{announcementId}/answer/new", 200)//
			.with(SecurityMockMvcRequestPostProcessors.csrf())//
			.param("description", "This is a description")//
			.param("date", "2018/10/01"))//
		.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())//
		//.andExpect(MockMvcResultMatchers.model().attributeExists("message"))//
		.andExpect(MockMvcResultMatchers.view()//
			.name("exception"));
	}

}
