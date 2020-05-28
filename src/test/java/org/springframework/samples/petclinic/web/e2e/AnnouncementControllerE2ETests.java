
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
class AnnouncementControllerE2ETests {

	@Autowired
	private MockMvc mockMvc;


	@WithMockUser(username = "owner1", password = "0wn3r")
	@Test
	void shouldShowAnnouncements() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/announcements")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("announcements"))
		.andExpect(MockMvcResultMatchers.view().name("announcements/announcementsList"));
	}

	@WithMockUser(username = "owner1", password = "0wn3r")
	@Test
	void shouldShowAnnouncementDetails() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/announcements/{announcementId}", 3))//
		.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model()//
			.attributeExists("positiveHistory"))
		.andExpect(MockMvcResultMatchers.model()//
			.attributeExists("announcement"))
		.andExpect(MockMvcResultMatchers.view().name("announcements/announcementDetails"));
	}

	@WithMockUser(username = "owner1", password = "0wn3r")
	@Test
	void shouldNotShowAnnouncementDetails() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/announcements/{announcementId}", 200)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("announcement"))
		.andExpect(MockMvcResultMatchers.view().name("exception"));

	}

	@WithMockUser(username = "owner6", password = "0wn3r")
	@Test
	void shouldNotShowAnnouncementWhenNotFound() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/announcements/{announcementId}", 200))//
		.andExpect(MockMvcResultMatchers.status().isOk())//
		.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("announcement"))//
		.andExpect(MockMvcResultMatchers.model().attribute("message", "Announcement not found"))//
		.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = "owner1", password = "0wn3r")
	@Test
	void shouldCreateAnnouncement() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/announcements/new", 1)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("announcement"))
		.andExpect(MockMvcResultMatchers.view().name("announcements/editAnnouncement"));
	}

	@WithMockUser(username = "owner1", password = "0wn3r")
	@Test
	void shouldSaveAnnouncement() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.post("/announcements/new", 1)//
			.with(SecurityMockMvcRequestPostProcessors.csrf())//
			.param("type.id", "1").param("canBeAdopted", "true").param("petName", "Bibi")).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/announcements"));
	}

	@WithMockUser(username = "owner1", password = "0wn3r")
	@Test
	void shouldNotSaveAnnouncementWithFormErrors() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.post("/announcements/new", 1).with(SecurityMockMvcRequestPostProcessors.csrf())).andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attributeHasErrors("announcement")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("announcement", "petName"))
		.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("announcement", "type"))//
		.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("announcement", "canBeAdopted")).andExpect(MockMvcResultMatchers.view().name("announcements/editAnnouncement"));
	}

	@WithMockUser(username = "owner1", password = "0wn3r")
	@Test
	void shouldDeleteAnnouncement() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/announcements/delete/{announcementId}", 1)).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/announcements"));
	}

	@WithMockUser(username = "owner2", password = "0wn3r")
	@Test
	void shouldNotDeleteAnnouncementOfOtherUser() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/announcements/delete/{announcementId}", 3)).andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attribute("message", "You cannot delete another user's announcement details")).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = "owner2", password = "0wn3r")
	@Test
	void shouldNotDeleteAnnouncementWhenNotFound() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/announcements/delete/{announcementId}", 200)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attribute("message", "Announcement not found"))
		.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = "owner3", password = "0wn3r")
	@Test
	void shouldUpdateAnnouncement() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/announcements/update/{announcementId}", 3)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("announcement"))
		.andExpect(MockMvcResultMatchers.view().name("announcements/editAnnouncement"));
	}

	@WithMockUser(username = "owner1", password = "0wn3r")
	@Test
	void shouldNotUpdateAnotherOwnerAnnouncement() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/announcements/update/{announcementId}", 3))//
		.andExpect(MockMvcResultMatchers.status().isOk())//
		.andExpect(MockMvcResultMatchers.model()//
			.attribute("message", "You can't update another owner's announcement"))//
		.andExpect(MockMvcResultMatchers.view().name("/exception"));
	}

	@WithMockUser(username = "owner1", password = "0wn3r")
	@Test
	void shouldNotUpdateAnnouncementWithIncorrectId() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/announcements/update/{announcementId}", 200))//
		.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model()//
			.attributeExists("message"))
		.andExpect(MockMvcResultMatchers.model().attribute("message", "Announcement not found"))//
		.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = "owner1", password = "0wn3r")
	@Test
	void shouldSaveUpdateAnnouncement() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.post("/announcements/update/{announcementId}", 1)//
			.with(SecurityMockMvcRequestPostProcessors.csrf())//
			.param("type.id", "1").param("canBeAdopted", "true").param("petName", "Bibi").param("id", "1")//
			.param("owner.id", "1")).andExpect(MockMvcResultMatchers.status().isOk())//
		.andExpect(MockMvcResultMatchers.view().name("announcements/announcementDetails"));

	}

	@WithMockUser(username = "owner1", password = "0wn3r")
	@Test
	void shouldNotSaveUpdatedAnnouncementWithFormErrors() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.post("/announcements/update/{announcementId}", 1)//
			.with(SecurityMockMvcRequestPostProcessors.csrf())).andExpect(MockMvcResultMatchers.status().isOk())//
		.andExpect(MockMvcResultMatchers.model().attributeHasErrors("announcement")).andExpect(MockMvcResultMatchers.view().name("announcements/editAnnouncement"));

	}

}
