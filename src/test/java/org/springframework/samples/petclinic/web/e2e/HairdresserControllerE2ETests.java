

package org.springframework.samples.petclinic.web.e2e;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
	webEnvironment=SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
public class HairdresserControllerE2ETests {

	private static final int	TEST_HAIRDRESSER_ID	= 1;

	@Autowired
	private MockMvc				mockMvc;

	@WithMockUser(username = "owner1", password = "0wn3r")
	@Test
	void shouldShowHairdressers() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/hairdressers"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attributeExists("hairdressers"))
		.andExpect(MockMvcResultMatchers.view().name("hairdressers/hairdressersList"));
	}


	@WithMockUser(username = "owner1", password = "0wn3r")
	@Test
	void shouldShowHairdresserDetails() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/hairdressers/{hairdresserId}", HairdresserControllerE2ETests.TEST_HAIRDRESSER_ID))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attributeExists("hairdresser"))
		.andExpect(MockMvcResultMatchers.view().name("hairdressers/hairdresserDetails"));
	}

	@WithMockUser(username = "owner1", password = "0wn3r")
	@Test
	void shouldNotShowHairdresserDetails() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/hairdressers/{hairdresserId}", 200))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attributeExists("message"))
		.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("hairdresser"))
		.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

}
