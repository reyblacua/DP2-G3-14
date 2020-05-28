
package org.springframework.samples.petclinic.web;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * Test class for the {@link PetController}
 *
 * @author Colin But
 */
@WebMvcTest(value = PetController.class, includeFilters = @ComponentScan.Filter(value = PetTypeFormatter.class, type = FilterType.ASSIGNABLE_TYPE),
	excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class PetControllerTests {

	private static final int	TEST_OWNER_ID	= 1;

	private static final int	TEST_PET_ID		= 1;

	@MockBean
	private PetService			petService;

	@MockBean
	private OwnerService		ownerService;

	@Autowired
	private MockMvc				mockMvc;

	private Owner				george;


	Pet createDummyPet(final int id, final String name) {
		Pet pet = new Pet();
		pet.setId(id);
		pet.setName(name);
		pet.setDangerous(false);
		pet.setIsVaccinated(true);
		pet.setType(new PetType());

		return pet;
	}

	@BeforeEach
	void setup() {
		PetType cat = new PetType();
		cat.setId(3);
		cat.setName("hamster");

		this.george = new Owner();
		this.george.setId(1);
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
		this.george.setNumerousAnimal(true);

		BDDMockito.given(this.petService.findPetTypes()).willReturn(Lists.newArrayList(cat));
		BDDMockito.given(this.ownerService.findOwnerById(PetControllerTests.TEST_OWNER_ID)).willReturn(this.george);
		BDDMockito.given(this.petService.findPetById(PetControllerTests.TEST_PET_ID)).willReturn(new Pet());

	}

	@WithMockUser(value = "spring")
	@Test
	void testInitCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/{ownerId}/pets/new", PetControllerTests.TEST_OWNER_ID)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("pets/createOrUpdatePetForm"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("pet"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormSuccess() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.post("/owners/{ownerId}/pets/new", PetControllerTests.TEST_OWNER_ID)//
			.with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "Betty").param("type", "hamster")//
			.param("birthDate", "2015/02/12").param("dangerous", "false").param("isVaccinated", "true"))//
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())//
			.andExpect(MockMvcResultMatchers.view().name("redirect:/owners/{ownerId}"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormHasErrors() throws Exception {
		this.mockMvc
			.perform(
				MockMvcRequestBuilders.post("/owners/{ownerId}/pets/{petId}/edit", PetControllerTests.TEST_OWNER_ID, PetControllerTests.TEST_PET_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "Betty").param("birthDate", "2015/02/12"))
			.andExpect(MockMvcResultMatchers.model().attributeHasErrors("pet")).andExpect(MockMvcResultMatchers.model().attributeHasErrors("pet")).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("pet")).andExpect(MockMvcResultMatchers.view().name("pets/createOrUpdatePetForm"));
	}

	@WithMockUser(value = "george")
	@Test
	void testProcessCreationFormDangerousAnimalOwnerWithoutLicense() throws Exception {
		Owner owner = this.george;
		owner.setDangerousAnimal(false);

		BDDMockito.given(this.ownerService.findOwnerById(1)).willReturn(owner);

		this.mockMvc.perform(MockMvcRequestBuilders.post("/owners/{ownerId}/pets/new", //
			PetControllerTests.TEST_OWNER_ID, 1)//
			.with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "Betty").param("type", "hamster")//
			.param("birthDate", "2015/02/12").param("dangerous", "true").param("isVaccinated", "true"))//
			.andExpect(MockMvcResultMatchers.model()//
				.attribute("message", "You can't add a new dangerous pet if you don't have the dangerous animals license"))
			.andExpect(MockMvcResultMatchers.status().isOk())//
			.andExpect(MockMvcResultMatchers.view().name("/exception"));
	}

	@WithMockUser(value = "george")
	@Test
	void testProcessCreationFormOwnerWithoutNumerousAnimalsLivesInCity() throws Exception {
		Owner owner = this.george;
		owner.setNumerousAnimal(false);

		Pet pet1 = this.createDummyPet(1, "Puppy");
		Pet pet2 = this.createDummyPet(2, "Toby");
		Pet pet3 = this.createDummyPet(3, "Lulu");

		owner.addPet(pet1);
		owner.addPet(pet2);
		owner.addPet(pet3);

		BDDMockito.given(this.ownerService.findOwnerById(1)).willReturn(owner);

		this.mockMvc.perform(MockMvcRequestBuilders.post("/owners/{ownerId}/pets/new", //
			PetControllerTests.TEST_OWNER_ID, 1)//
			.with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "Betty").param("type", "hamster")//
			.param("birthDate", "2015/02/12").param("dangerous", "true").param("isVaccinated", "true"))//
			.andExpect(MockMvcResultMatchers.model()//
				.attribute("message", "You can't add a new pet if you have three pets without the numerous pets license"))
			.andExpect(MockMvcResultMatchers.status().isOk())//
			.andExpect(MockMvcResultMatchers.view().name("/exception"));
	}

	@WithMockUser(value = "george")
	@Test
	void testProcessCreationFormOwnerWithoutNumerousAnimalsAndNotLivesInCity() throws Exception {
		Owner owner = this.george;
		owner.setNumerousAnimal(false);
		owner.setLivesInCity(false);

		Pet pet1 = this.createDummyPet(1, "Puppy");
		Pet pet2 = this.createDummyPet(2, "Toby");
		Pet pet3 = this.createDummyPet(3, "Lulu");
		Pet pet4 = this.createDummyPet(4, "Kitty");
		Pet pet5 = this.createDummyPet(5, "Momo");

		owner.addPet(pet1);
		owner.addPet(pet2);
		owner.addPet(pet3);
		owner.addPet(pet4);
		owner.addPet(pet5);

		BDDMockito.given(this.ownerService.findOwnerById(1)).willReturn(owner);

		this.mockMvc.perform(MockMvcRequestBuilders.post("/owners/{ownerId}/pets/new", //
			PetControllerTests.TEST_OWNER_ID, 1)//
			.with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "Betty").param("type", "hamster")//
			.param("birthDate", "2015/02/12").param("dangerous", "true").param("isVaccinated", "true"))//
			.andExpect(MockMvcResultMatchers.model()//
				.attribute("message", "You can't add a new pet if you have five pets without the numerous pets license"))
			.andExpect(MockMvcResultMatchers.status().isOk())//
			.andExpect(MockMvcResultMatchers.view().name("/exception"));
	}

	@WithMockUser(value = "george")
	@Test
	void testProcessCreationFormPetDuplication() throws Exception {
		Owner owner = this.george;
		owner.setNumerousAnimal(false);

		Pet pet1 = this.createDummyPet(1, "Puppy");

		owner.addPet(pet1);

		BDDMockito.given(this.ownerService.findOwnerById(1)).willReturn(owner);

		this.mockMvc.perform(MockMvcRequestBuilders.post("/owners/{ownerId}/pets/new", //
			PetControllerTests.TEST_OWNER_ID, 1)//
			.with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "Puppy").param("type", "hamster")//
			.param("birthDate", "2015/02/12").param("dangerous", "true").param("isVaccinated", "true"))//
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())//
			.andExpect(MockMvcResultMatchers.view().name("redirect:/owners/{ownerId}"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testInitUpdateForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/{ownerId}/pets/{petId}/edit", PetControllerTests.TEST_OWNER_ID, PetControllerTests.TEST_PET_ID)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("pet")).andExpect(MockMvcResultMatchers.view().name("pets/createOrUpdatePetForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessUpdateFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/owners/{ownerId}/pets/{petId}/edit", PetControllerTests.TEST_OWNER_ID, PetControllerTests.TEST_PET_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "Betty")
			.param("type", "hamster").param("birthDate", "2015/02/12")).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/owners/{ownerId}"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessUpdateFormHasErrors() throws Exception {
		this.mockMvc
			.perform(
				MockMvcRequestBuilders.post("/owners/{ownerId}/pets/{petId}/edit", PetControllerTests.TEST_OWNER_ID, PetControllerTests.TEST_PET_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "Betty").param("birthDate", "2015/02/12"))
			.andExpect(MockMvcResultMatchers.model().attributeHasNoErrors("owner")).andExpect(MockMvcResultMatchers.model().attributeHasErrors("pet")).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("pets/createOrUpdatePetForm"));
	}

	@WithMockUser(value = "george")
	@Test
	void testProcessUpdateFormPetDuplication() throws Exception {
		Owner owner = this.george;
		owner.setNumerousAnimal(false);

		Pet pet1 = this.createDummyPet(1, "Puppy");

		owner.addPet(pet1);

		BDDMockito.given(this.ownerService.findOwnerById(1)).willReturn(owner);

		this.mockMvc.perform(MockMvcRequestBuilders.post("/owners/{ownerId}/pets/{petId}/edit", //
			PetControllerTests.TEST_OWNER_ID, 1)//
			.with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "Puppy").param("type", "hamster")//
			.param("birthDate", "2015/02/12").param("dangerous", "true").param("isVaccinated", "true"))//
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())//
			.andExpect(MockMvcResultMatchers.view().name("redirect:/owners/{ownerId}"));
	}

	@WithMockUser(value = "george")
	@Test
	void testProcessUpdateFormDangerousAnimalOwnerWithoutLicense() throws Exception {
		Owner owner = this.george;
		owner.setDangerousAnimal(false);

		BDDMockito.given(this.ownerService.findOwnerById(1)).willReturn(owner);

		this.mockMvc.perform(MockMvcRequestBuilders.post("/owners/{ownerId}/pets/{petId}/edit", //
			PetControllerTests.TEST_OWNER_ID, 1)//
			.with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "Betty").param("type", "hamster")//
			.param("birthDate", "2015/02/12").param("dangerous", "true").param("isVaccinated", "true"))//
			.andExpect(MockMvcResultMatchers.model()//
				.attribute("message", "You can't add a new dangerous pet if you don't have the dangerous animals license"))
			.andExpect(MockMvcResultMatchers.status().isOk())//
			.andExpect(MockMvcResultMatchers.view().name("/exception"));
	}

}
