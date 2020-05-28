
package org.springframework.samples.petclinic.web;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Hairdresser;
import org.springframework.samples.petclinic.service.HairdresserService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = HairdresserController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)

class HairdresserControllerTests {

	private static final int	TEST_HAIRDRESSER_ID	= 1;

	@MockBean
	private HairdresserService	hairdresserService;

	@Autowired
	private MockMvc				mockMvc;


	public Hairdresser createDummyHairdresser(final String firstName, final String lastName) {
		Hairdresser hairdresser = new Hairdresser();
		hairdresser.setFirstName(firstName);
		hairdresser.setLastName(lastName);
		return hairdresser;
	}

	@WithMockUser(value = "spring")
	@Test
	void shouldShowHairdressers() throws Exception {
		Hairdresser hairdresser1 = this.createDummyHairdresser("Rodolfo", "Buenaventura");
		Hairdresser hairdresser2 = this.createDummyHairdresser("Lázaro", "Tormes");

		Mockito.when(this.hairdresserService.findAll()).thenReturn(Stream.of(hairdresser1, hairdresser2).collect(Collectors.toList()));

		this.mockMvc.perform(MockMvcRequestBuilders.get("/hairdressers")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("hairdressers"))
			.andExpect(MockMvcResultMatchers.view().name("hairdressers/hairdressersList"));
	}

	@WithMockUser(value = "spring")
	@Test
	void shouldNotShowHairdressers() throws Exception {
		Mockito.when(this.hairdresserService.findAll()).thenThrow(NoSuchElementException.class);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/hairdressers")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("isempty"))
			.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("hairdressers")).andExpect(MockMvcResultMatchers.view().name("hairdressers/hairdressersList"));
	}

	@WithMockUser(value = "spring")
	@Test
	void shouldShowHairdresserDetails() throws Exception {
		Hairdresser hairdresser1 = this.createDummyHairdresser("Lázaro", "Tormes");
		Mockito.when(this.hairdresserService.findHairdresserById(HairdresserControllerTests.TEST_HAIRDRESSER_ID)).thenReturn(Optional.of(hairdresser1));

		this.mockMvc.perform(MockMvcRequestBuilders.get("/hairdressers/{hairdresserId}", HairdresserControllerTests.TEST_HAIRDRESSER_ID)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("hairdresser")).andExpect(MockMvcResultMatchers.view().name("hairdressers/hairdresserDetails"));
	}

	@WithMockUser(value = "spring")
	@Test
	void shouldNotShowHairdresserDetails() throws Exception {
		Mockito.when(this.hairdresserService.findHairdresserById(HairdresserControllerTests.TEST_HAIRDRESSER_ID)).thenThrow(NoSuchElementException.class);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/hairdressers/{hairdresserId}", HairdresserControllerTests.TEST_HAIRDRESSER_ID)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("message"))
			.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("hairdresser")).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

}
