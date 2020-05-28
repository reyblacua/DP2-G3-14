
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
public class InscriptionE2EControllerTests {

	@Autowired
	private MockMvc mockMvc;


	@WithMockUser(username = "owner9", password = "0wn3r")
	@Test
	void shouldShowInscriptions() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/inscriptions")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("inscriptions"))
		.andExpect(MockMvcResultMatchers.view().name("inscriptions/inscriptionsList"));
	}

	@WithMockUser(username = "owner7", password = "0wn3r")
	@Test
	void shouldNotShowInscriptionsWhenYouHaveNotInscriptions() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/inscriptions")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("isempty"))
		.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("inscriptions")).andExpect(MockMvcResultMatchers.view().name("inscriptions/inscriptionsList"));
	}

	// mostrarInscription

	@WithMockUser(username = "owner9", password = "0wn3r")
	@Test
	void shouldShowInscriptionDetails() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/inscriptions/{inscriptionId}", 1)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("inscription"))
		.andExpect(MockMvcResultMatchers.view().name("inscriptions/inscriptionDetails"));
	}

	@WithMockUser(username = "owner2", password = "0wn3r")
	@Test
	void shouldNotShowAnotherUserInscription() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/inscriptions/{inscriptionId}", 1)).andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attribute("message", "You cannot access another user's inscription details")).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = "owner1", password = "0wn3r")
	@Test
	void shouldNotShowInscriptionWhenNotFound() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/inscriptions/{inscriptionId}", 20)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attribute("message", "Inscription not found"))
		.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	// Get createInscription

	@WithMockUser(username = "owner4", password = "0wn3r")
	@Test
	void shouldCreateInscription() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/courses/{courseId}/inscription/new", 1)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("inscription"))
		.andExpect(MockMvcResultMatchers.view().name("inscriptions/editInscription"));
	}

	@WithMockUser(username = "owner1", password = "0wn3r")
	@Test
	void shouldNotCreateInscriptionWhenCourseNotFound() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/courses/{courseId}/inscription/new", 20)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attribute("message", "There are errors validating data"))
		.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = "owner12", password = "0wn3r")
	@Test
	void shouldNotCreateInscriptionWhenOwnerHasNoPets() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/courses/{courseId}/inscription/new", 1)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attribute("message", "You have no pets to sign up in a course"))
		.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = "owner10", password = "0wn3r")
	@Test
	void shouldNotCreateInscriptionWithOtherInscriptionsNotPaid() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/courses/{courseId}/inscription/new", 1)).andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attribute("message", "You have to pay previous courses inscriptions")).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = "owner1", password = "0wn3r")
	@Test
	void shouldNotCreateInscriptionForAFullCourse() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/courses/{courseId}/inscription/new", 5)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attribute("message", "The course is full"))
		.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	// Post createInscription

	//	@WithMockUser(username = "owner5", password = "0wn3r")
	//	@Test
	//	void shouldSaveInscription() throws Exception {
	//
	//		this.mockMvc.perform(MockMvcRequestBuilders.post("/courses/{courseId}/inscription/new", 1).with(SecurityMockMvcRequestPostProcessors.csrf()).param("date", "2020/02/12").param("isPaid", "false").param("pet.id", "6"))
	//			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/inscriptions"));
	//	}

	@WithMockUser(username = "owner1", password = "0wn3r")
	@Test
	void shouldNotSaveInscriptionWhenServiceErrors() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.post("/courses/{courseId}/inscription/new", 2).with(SecurityMockMvcRequestPostProcessors.csrf()).param("date", "2015/02/12").param("isPaid", "false").param("pet.id", "1"))
		.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("message")).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = "owner2", password = "0wn3r")
	@Test
	void shouldNotSaveInscriptionWithFormErrors() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.post("/courses/{courseId}/inscription/new", 1).with(SecurityMockMvcRequestPostProcessors.csrf())).andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attributeHasErrors("inscription")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("inscription", "date"))
		.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("inscription", "isPaid")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("inscription", "pet"))
		.andExpect(MockMvcResultMatchers.view().name("inscriptions/editInscription"));
	}

	// deleteInscription

	@WithMockUser(username = "owner9", password = "0wn3r")
	@Test
	void shouldDeleteInscription() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/inscriptions/delete/{inscriptionId}", 1)).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/inscriptions"));
	}

	@WithMockUser(username = "owner1", password = "0wn3r")
	@Test
	void shouldNotDeleteInscriptionOfOtherUser() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/inscriptions/delete/{inscriptionId}", 2)).andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attribute("message", "You cannot delete another user's inscription details")).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = "owner1", password = "0wn3r")
	@Test
	void shouldNotDeleteInscriptionWhenNotFound() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/inscriptions/delete/{inscriptionId}", 200)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attribute("message", "Inscription not found"))
		.andExpect(MockMvcResultMatchers.view().name("exception"));
	}
}
