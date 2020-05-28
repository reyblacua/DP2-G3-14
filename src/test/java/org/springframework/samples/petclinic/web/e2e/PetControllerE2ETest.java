
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
public class PetControllerE2ETest {

	@Autowired
	private MockMvc mockMvc;


	@WithMockUser(username = "owner1", password = "0wn3r")
	@Test
	void testInitCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/{ownerId}/pets/new", 1)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("pets/createOrUpdatePetForm"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("pet"));
	}

	@WithMockUser(username = "owner1", password = "0wn3r")
	@Test
	void testProcessCreationFormSuccess() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.post("/owners/{ownerId}/pets/new", 1)//
			.with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "Betty").param("type", "hamster")//
			.param("birthDate", "2015/02/12").param("dangerous", "false").param("isVaccinated", "true"))//
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection())//
		.andExpect(MockMvcResultMatchers.view().name("redirect:/owners/{ownerId}"));
	}

	@WithMockUser(username = "owner1", password = "0wn3r")
	@Test
	void testProcessCreationFormHasErrors() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/owners/{ownerId}/pets/new", 1)//
			.with(SecurityMockMvcRequestPostProcessors.csrf())//
			.param("name", "Betty").param("type", "")//
			.param("birthDate", "2015/02/12").param("dangerous", "false").param("isVaccinated", "true"))//
		.andExpect(MockMvcResultMatchers.status().isOk())//
		.andExpect(MockMvcResultMatchers.model().attributeHasErrors("pet"))//
		.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("pet", "type"))//
		.andExpect(MockMvcResultMatchers.view().name("pets/createOrUpdatePetForm"));
		//
		//		this.mockMvc.perform(MockMvcRequestBuilders.post("/users/new").with(SecurityMockMvcRequestPostProcessors.csrf()).param("firstName", "Joe").param("lastName", "Bloggs").param("city", "London")).andExpect(MockMvcResultMatchers.status().isOk())
		//			.andExpect(MockMvcResultMatchers.model().attributeHasErrors("owner"))
		//.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("owner", "address"))
		//.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("owner", "telephone"))
		//			.andExpect(MockMvcResultMatchers.view().name("users/createOwnerForm"));
	}

	@WithMockUser(username = "owner6", password = "0wn3r")
	@Test
	void testProcessCreationFormDangerousAnimalOwnerWithoutLicense() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.post("/owners/{ownerId}/pets/new", //
			6)//
			.with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "Betty").param("type", "hamster")//
			.param("birthDate", "2015/02/12").param("dangerous", "true").param("isVaccinated", "true"))//
		.andExpect(MockMvcResultMatchers.model()//
			.attribute("message", "You can't add a new dangerous pet if you don't have the dangerous animals license"))
		.andExpect(MockMvcResultMatchers.status().isOk())//
		.andExpect(MockMvcResultMatchers.view().name("/exception"));
	}

	@WithMockUser(username = "owner10", password = "0wn3r")
	@Test
	void testProcessCreationFormOwnerWithoutNumerousAnimalsLivesInCity() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.post("/owners/{ownerId}/pets/new", //
			10)//
			.with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "Betty").param("type", "hamster")//
			.param("birthDate", "2015/02/12").param("dangerous", "true").param("isVaccinated", "true"))//
		.andExpect(MockMvcResultMatchers.model()//
			.attribute("message", "You can't add a new pet if you have three pets without the numerous pets license"))
		.andExpect(MockMvcResultMatchers.status().isOk())//
		.andExpect(MockMvcResultMatchers.view().name("/exception"));
	}

	@WithMockUser(username = "owner11", password = "0wn3r")
	@Test
	void testProcessCreationFormOwnerWithoutNumerousAnimalsAndNotLivesInCity() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.post("/owners/{ownerId}/pets/new", //
			11)//
			.with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "Betty").param("type", "hamster")//
			.param("birthDate", "2015/02/12").param("dangerous", "false").param("isVaccinated", "true"))//
		.andExpect(MockMvcResultMatchers.model()//
			.attribute("message", "You can't add a new pet if you have five pets without the numerous pets license"))
		.andExpect(MockMvcResultMatchers.status().isOk())//
		.andExpect(MockMvcResultMatchers.view().name("/exception"));
	}

	@WithMockUser(username = "owner1", password = "0wn3r")
	@Test
	void testProcessCreationFormPetDuplication() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.post("/owners/{ownerId}/pets/new", //
			1)//
			.with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "Leo").param("type", "cat")//
			.param("birthDate", "2010/09/07").param("dangerous", "false").param("isVaccinated", "true"))//
		.andExpect(MockMvcResultMatchers.status().isOk())//
		.andExpect(MockMvcResultMatchers.view().name("pets/createOrUpdatePetForm"));
	}

	@WithMockUser(username = "owner1", password = "0wn3r")
	@Test
	void testInitUpdateForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/{ownerId}/pets/{petId}/edit", 1, 1)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("pet"))
		.andExpect(MockMvcResultMatchers.view().name("pets/createOrUpdatePetForm"));
	}

	@WithMockUser(username = "owner3", password = "0wn3r")
	@Test
	void testProcessUpdateFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/owners/{ownerId}/pets/{petId}/edit", 3, 3).with(SecurityMockMvcRequestPostProcessors.csrf())//
			.param("name", "Boris")//
			.param("type", "hamster")//
			.param("birthDate", "2015/02/12")//
			.param("dangerous", "false").param("isVaccinated", "true"))//
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection())//
		.andExpect(MockMvcResultMatchers.view().name("redirect:/owners/{ownerId}"));
	}

	@WithMockUser(username = "owner1", password = "0wn3r")
	@Test
	void testProcessUpdateFormHasErrors() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/owners/{ownerId}/pets/{petId}/edit", 1, 1).with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "Betty").param("birthDate", "2015/02/12"))
		.andExpect(MockMvcResultMatchers.model().attributeHasNoErrors("owner")).andExpect(MockMvcResultMatchers.model().attributeHasErrors("pet")).andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.view().name("pets/createOrUpdatePetForm"));
	}

	@WithMockUser(username = "owner6", password = "0wn3r")
	@Test
	void testProcessUpdateFormPetDuplication() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.post("/owners/{ownerId}/pets/{petId}/edit", //
			6, 7)//
			.with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "Max").param("type", "cat")//
			.param("birthDate", "2010/09/07").param("dangerous", "false").param("isVaccinated", "true"))//
		.andExpect(MockMvcResultMatchers.status().isOk())//
		.andExpect(MockMvcResultMatchers.view().name("pets/createOrUpdatePetForm"));
	}

	@WithMockUser(username = "owner1", password = "0wn3r")
	@Test
	void testProcessUpdateFormDangerousAnimalOwnerWithoutLicense() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.post("/owners/{ownerId}/pets/{petId}/edit", //
			1, 1)//
			.with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "Betty").param("type", "hamster")//
			.param("birthDate", "2015/02/12").param("dangerous", "true").param("isVaccinated", "true"))//
		.andExpect(MockMvcResultMatchers.model()//
			.attribute("message", "You can't add a new dangerous pet if you don't have the dangerous animals license"))
		.andExpect(MockMvcResultMatchers.status().isOk())//
		.andExpect(MockMvcResultMatchers.view().name("/exception"));
	}
}
