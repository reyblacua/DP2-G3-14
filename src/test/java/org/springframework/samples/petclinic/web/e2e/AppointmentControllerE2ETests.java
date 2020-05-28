
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
class AppointmentE2EControllerTests {

	private static final int	TEST_OWNER_ID	= 1;

	@Autowired
	private MockMvc				mockMvc;


	@WithMockUser(username = "owner1", password = "0wn3r")
	@Test
	void shouldShowAppointments() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/appointments")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("appointments"))
		.andExpect(MockMvcResultMatchers.view().name("appointments/appointmentsList"));
	}

	@WithMockUser(username = "owner11", password = "0wn3r")
	@Test
	void shouldNotShowAppointmentsWhenYouHaveNotAppointments() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/appointments")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("isempty"))
		.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("appointments")).andExpect(MockMvcResultMatchers.view().name("appointments/appointmentsList"));
	}

	@WithMockUser(username = "owner1", password = "0wn3r")
	@Test
	void shouldShowAppointmentDetails() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/appointments/{appointmentId}", 1)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("appointment"))
		.andExpect(MockMvcResultMatchers.view().name("appointments/appointmentDetails"));
	}

	@WithMockUser(username = "owner1", password = "0wn3r")
	@Test
	void shouldNotShowAppointmentWhenNotFound() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/appointments/{appointmentId}", 100)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attribute("message", "Appointment not found"))
		.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("appointment")).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = "owner2", password = "0wn3r")
	@Test
	void shouldNotShowAnotherUserAppointment() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/appointments/{appointmentId}", 1)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attribute("message", "You cannot access another user's appointment"))
		.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = "owner1", password = "0wn3r")
	@Test
	void shouldDeleteAppointment() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/appointments/delete/{appointmentId}", 1)).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/appointments"));
	}

	@WithMockUser(username = "owner1", password = "0wn3r")
	@Test
	void shouldNotDeleteAppointmentWhenNotFound() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/appointments/delete/{appointmentId}", 200)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attribute("message", "Error: No value present"))
		.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("appointment")).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = "owner11", password = "0wn3r")
	@Test
	void shouldNotDeleteAppointmentOfOtherUser() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/appointments/delete/{appointmentId}", 1)).andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attribute("message", "You cannot delete another user's appointment")).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = "owner1", password = "0wn3r")
	@Test
	void shouldCreateAppointment() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/hairdressers/{hairdresserId}/appointments/new", 1)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("appointment"))
		.andExpect(MockMvcResultMatchers.view().name("appointments/editAppointment"));
	}

	@WithMockUser(username = "owner12", password = "0wn3r")
	@Test
	void shouldNotCreatAppointmentWhenOwnerHasNoPets() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/hairdressers/{hairdresserId}/appointments/new", 1)).andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attribute("message", "You have no pets to make an appointment")).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = "owner3", password = "0wn3r")
	@Test
	void shouldNotCreateAppointmentWithOtherAppointmentsNotPaid() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/hairdressers/{hairdresserId}/appointments/new", 1)).andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attribute("message", "You have to pay previous appointments")).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = "owner1", password = "0wn3r")
	@Test
	void shouldSaveAppointment() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.post("/hairdressers/{hairdresserId}/appointments/new", 1).with(SecurityMockMvcRequestPostProcessors.csrf()).param("date", "2020/07/07 15:30").param("isPaid", "false").param("pet.id", "1"))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/appointments"));
	}

	@WithMockUser(username = "owner1", password = "0wn3r")
	@Test
	void shouldNotSaveAppointmentWithFormErrors() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.post("/hairdressers/{hairdresserId}/appointments/new", 1).with(SecurityMockMvcRequestPostProcessors.csrf())).andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attributeHasErrors("appointment")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("appointment", "date"))
		.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("appointment", "pet")).andExpect(MockMvcResultMatchers.view().name("appointments/editAppointment"));
	}

	@WithMockUser(username = "owner1", password = "0wn3r")
	@Test
	void shouldNotSaveAppointmentWhenDateAlreadyTaken() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.post("/hairdressers/{hairdresserId}/appointments/new", 1).with(SecurityMockMvcRequestPostProcessors.csrf()).param("date", "2020/07/20 20:50").param("isPaid", "false").param("pet.id", "1"))
		.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attribute("message", "Hairdresser already has an appointment at that time")).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = "owner2", password = "0wn3r")
	@Test
	void shouldNotSaveAppointmentWhenPetAlreadyHasAnotherAppointmentThatDay() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.post("/hairdressers/{hairdresserId}/appointments/new", 1).with(SecurityMockMvcRequestPostProcessors.csrf()).param("date", "2020/07/03 20:00").param("isPaid", "false").param("pet.id", "2"))
		.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attribute("message", "You cannot make more than one appointment per day for the same pet")).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

}
