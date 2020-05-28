
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
public class OwnerControllerE2ETest {

	@Autowired
	private MockMvc mockMvc;


	@WithMockUser(value = "spring")
	@Test
	void testInitCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/users/new")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("owner")).andExpect(MockMvcResultMatchers.view().name("users/createOwnerForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/users/new").param("firstName", "Joe")//
			.param("lastName", "Bloggs").with(SecurityMockMvcRequestPostProcessors.csrf())//
			.param("address", "123 Caramel Street").param("city", "London").param("telephone", "01316761638")//
			.param("dangerousAnimal", "true").param("numerousAnimal", "true")//
			.param("livesInCity", "true").param("positiveHistory", "true"))//
		.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormHasErrors() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/users/new").with(SecurityMockMvcRequestPostProcessors.csrf()).param("firstName", "Joe").param("lastName", "Bloggs").param("city", "London")).andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attributeHasErrors("owner")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("owner", "address")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("owner", "telephone"))
		.andExpect(MockMvcResultMatchers.view().name("users/createOwnerForm"));
	}

	@WithMockUser(username = "owner1", password = "0wn3r")
	@Test
	void testInitFindForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/find")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("owner")).andExpect(MockMvcResultMatchers.view().name("owners/findOwners"));
	}

	@WithMockUser(username = "owner1", password = "0wn3r")
	@Test
	void testProcessFindFormSuccess() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("owners/ownersList"));
	}

	@WithMockUser(username = "owner1", password = "0wn3r")
	@Test
	void testProcessFindFormByLastName() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners").param("lastName", "Franklin")).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/owners/" + 1));
	}

	@WithMockUser(username = "owner1", password = "0wn3r")
	@Test
	void testProcessFindFormNoOwnersFound() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners").param("lastName", "Unknown Surname")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("owner", "lastName"))
		.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrorCode("owner", "lastName", "notFound")).andExpect(MockMvcResultMatchers.view().name("owners/findOwners"));
	}

	@WithMockUser(username = "owner1", password = "0wn3r")
	@Test
	void testInitUpdateOwnerForm() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/{ownerId}/edit", 1))//
		.andExpect(MockMvcResultMatchers.status().isOk())//
		.andExpect(MockMvcResultMatchers.model().attributeExists("owner"))//
		.andExpect(MockMvcResultMatchers.view().name("owners/createOrUpdateOwnerForm"));
	}

	@WithMockUser(username = "owner2", password = "0wn3r")
	@Test
	void testNotInitUpdateOwnerFormOfOtherUser() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/{ownerId}/edit", 1))//
		.andExpect(MockMvcResultMatchers.status().isOk())//
		.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("owner"))//
		.andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andExpect(MockMvcResultMatchers.view().name("/exception"));
	}

	@WithMockUser(username = "owner1", password = "0wn3r")
	@Test
	void testProcessUpdateOwnerFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/owners/{ownerId}/edit", 1)//
			.with(SecurityMockMvcRequestPostProcessors.csrf()).param("firstName", "Joe")//
			.param("lastName", "Bloggs").param("address", "123 Caramel Street").param("city", "London").param("telephone", "01616291589").param("dangerousAnimal", "true")//
			.param("numerousAnimal", "true").param("livesInCity", "true").param("positiveHistory", "true")//
			.param("user.username", "owner1").param("user.password", "0wn3r"))//
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection())//
		.andExpect(MockMvcResultMatchers.view().name("redirect:/owners/{ownerId}"));
	}

	@WithMockUser(username = "owner1", password = "0wn3r")
	@Test
	void testProcessUpdateOwnerFormHasErrors() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/owners/{ownerId}/edit", 1).with(SecurityMockMvcRequestPostProcessors.csrf()).param("firstName", "Joe").param("lastName", "Bloggs").param("city", "London"))
		.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeHasErrors("owner")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("owner", "address"))
		.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("owner", "telephone")).andExpect(MockMvcResultMatchers.view().name("owners/createOrUpdateOwnerForm"));
	}

	@WithMockUser(username = "owner1", password = "0wn3r")
	@Test
	void testShowOwner() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/{ownerId}", 1))//
		.andExpect(MockMvcResultMatchers.status().isOk())//
		.andExpect(MockMvcResultMatchers.model().attributeExists("owner")).andExpect(MockMvcResultMatchers.view().name("owners/ownerDetails"));
	}
}
