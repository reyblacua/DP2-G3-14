
package org.springframework.samples.petclinic.web;

import java.time.LocalDateTime;
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
import org.springframework.samples.petclinic.model.Appointment;
import org.springframework.samples.petclinic.model.Hairdresser;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.AppointmentService;
import org.springframework.samples.petclinic.service.HairdresserService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = AppointmentController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class AppointmentControllerTests {

	private static final int	TEST_OWNER_ID	= 1;

	@MockBean
	private AppointmentService	appointmentService;

	@MockBean
	private OwnerService		ownerService;

	@MockBean
	private PetService			petService;

	@MockBean
	private HairdresserService	hairdresserService;

	@Autowired
	private MockMvc				mockMvc;

	private Owner				carlitos;

	private Pet					neko;


	public Appointment createDummyAppointment(final String name) {
		Appointment appointment = new Appointment();
		appointment.setName(name);
		return appointment;
	}

	public Hairdresser createDummyHairdresser(final String firstName, final String lastName) {
		Hairdresser hairdresser = new Hairdresser();
		hairdresser.setFirstName(firstName);
		hairdresser.setLastName(lastName);
		return hairdresser;
	}

	@BeforeEach
	void setup() {

		this.carlitos = new Owner();
		this.carlitos.setId(AppointmentControllerTests.TEST_OWNER_ID);
		this.carlitos.setFirstName("Carlitos");
		this.carlitos.setLastName("Fernández");
		this.carlitos.setAddress("Avenida de la Palmera, Nº56");
		this.carlitos.setCity("Sevilla");
		this.carlitos.setTelephone("955767651");
		User carlitosuser = new User();
		carlitosuser.setUsername("carlitos");
		this.carlitos.setUser(carlitosuser);

		this.neko = new Pet();
		this.neko.setName("Neko");
		this.neko.setOwner(this.carlitos);
		this.neko.setId(1);
	}

	@WithMockUser(value = "spring")
	@Test
	void shouldShowAppointments() throws Exception {
		Appointment appointment1 = this.createDummyAppointment("Cita1");
		Appointment appointment2 = this.createDummyAppointment("Cita2");

		Mockito.when(this.appointmentService.findAppointmentsByOwner(ArgumentMatchers.any())).thenReturn(Lists.newArrayList(appointment1, appointment2));

		this.mockMvc.perform(MockMvcRequestBuilders.get("/appointments")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("appointments"))
		.andExpect(MockMvcResultMatchers.view().name("appointments/appointmentsList"));
	}

	@WithMockUser(value = "spring")
	@Test
	void shouldNotShowAppointmentsWhenYouHaveNotAppointments() throws Exception {

		Mockito.when(this.appointmentService.findAppointmentsByOwner(ArgumentMatchers.any())).thenThrow(NoSuchElementException.class);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/appointments")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("isempty"))
		.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("appointments")).andExpect(MockMvcResultMatchers.view().name("appointments/appointmentsList"));
	}

	@WithMockUser(value = "carlitos")
	@Test
	void shouldShowAppointmentDetails() throws Exception {
		Appointment appointment1 = this.createDummyAppointment("Cita1");
		appointment1.setOwner(this.carlitos);

		Mockito.when(this.appointmentService.findAppointmentById(1)).thenReturn(Optional.of(appointment1));

		this.mockMvc.perform(MockMvcRequestBuilders.get("/appointments/{appointmentId}", 1)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("appointment"))
		.andExpect(MockMvcResultMatchers.view().name("appointments/appointmentDetails"));
	}

	@WithMockUser(value = "spring")
	@Test
	void shouldNotShowAppointmentWhenNotFound() throws Exception {

		Mockito.when(this.appointmentService.findAppointmentById(1)).thenThrow(NoSuchElementException.class);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/appointments/{appointmentId}", 1)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attribute("message", "Appointment not found"))
		.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("appointment")).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(value = "spring")
	@Test
	void shouldNotShowAnotherUserAppointment() throws Exception {
		Appointment appointment1 = this.createDummyAppointment("Cita1");
		appointment1.setOwner(this.carlitos);

		Mockito.when(this.appointmentService.findAppointmentById(1)).thenReturn(Optional.of(appointment1));

		this.mockMvc.perform(MockMvcRequestBuilders.get("/appointments/{appointmentId}", 1)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attribute("message", "You cannot access another user's appointment"))
		.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(value = "carlitos")
	@Test
	void shouldDeleteAppointment() throws Exception {
		Appointment appointment1 = this.createDummyAppointment("Cita1");
		appointment1.setOwner(this.carlitos);

		Mockito.when(this.appointmentService.findAppointmentById(1)).thenReturn(Optional.of(appointment1));
		Mockito.doNothing().when(this.appointmentService).deleteAppointment(appointment1);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/appointments/delete/{appointmentId}", 1)).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/appointments"));
	}

	@WithMockUser(value = "carlitos")
	@Test
	void shouldNotDeleteAppointmentWhenNotFound() throws Exception {
		Appointment appointment1 = this.createDummyAppointment("Cita1");
		appointment1.setOwner(this.carlitos);

		Mockito.when(this.appointmentService.findAppointmentById(200)).thenThrow(NoSuchElementException.class);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/appointments/delete/{appointmentId}", 200)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attribute("message", "Error: null"))
		.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("appointment")).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(value = "spring")
	@Test
	void shouldNotDeleteAppointmentOfOtherUser() throws Exception {
		Appointment appointment1 = this.createDummyAppointment("Cita1");
		appointment1.setOwner(this.carlitos);

		Mockito.when(this.appointmentService.findAppointmentById(1)).thenReturn(Optional.of(appointment1));

		this.mockMvc.perform(MockMvcRequestBuilders.get("/appointments/delete/{appointmentId}", 1)).andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attribute("message", "You cannot delete another user's appointment")).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(value = "carlitos")
	@Test
	void shouldCreateAppointment() throws Exception {

		Hairdresser dummyHairdresser = this.createDummyHairdresser("Stefano", "Menéndez");
		dummyHairdresser.setActive(true);

		Mockito.when(this.hairdresserService.findHairdresserById(1)).thenReturn(Optional.of(dummyHairdresser));
		Mockito.when(this.ownerService.findOwnerByUserName("carlitos")).thenReturn(this.carlitos);
		Mockito.when(this.appointmentService.findAppointmentsByOwner(this.carlitos)).thenReturn(new ArrayList<Appointment>());
		Mockito.when(this.appointmentService.findAppointmentsByHairdresser(dummyHairdresser)).thenReturn(new ArrayList<Appointment>());
		Mockito.when(this.petService.findPets("carlitos")).thenReturn(Lists.newArrayList(this.neko));

		this.mockMvc.perform(MockMvcRequestBuilders.get("/hairdressers/{hairdresserId}/appointments/new", 1)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("appointment"))
		.andExpect(MockMvcResultMatchers.view().name("appointments/editAppointment"));
	}

	@WithMockUser(value = "carlitos")
	@Test
	void shouldNotCreatAppointmentWhenOwnerHasNoPets() throws Exception {

		Hairdresser dummyHairdresser = this.createDummyHairdresser("Yumi", "Suzuki");
		dummyHairdresser.setActive(true);

		Mockito.when(this.hairdresserService.findHairdresserById(1)).thenReturn(Optional.of(dummyHairdresser));
		Mockito.when(this.ownerService.findOwnerByUserName("carlitos")).thenReturn(this.carlitos);
		Mockito.when(this.appointmentService.findAppointmentsByOwner(this.carlitos)).thenReturn(new ArrayList<Appointment>());
		Mockito.when(this.appointmentService.findAppointmentsByHairdresser(dummyHairdresser)).thenReturn(new ArrayList<Appointment>());
		Mockito.when(this.petService.findPets("carlitos")).thenReturn(new ArrayList<Pet>());

		this.mockMvc.perform(MockMvcRequestBuilders.get("/hairdressers/{hairdresserId}/appointments/new", 1)).andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attribute("message", "You have no pets to make an appointment")).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(value = "carlitos")
	@Test
	void shouldNotCreateAppointmentWithOtherAppointmentsNotPaid() throws Exception {

		Hairdresser dummyHairdresser = this.createDummyHairdresser("Manuel", "Santos");
		dummyHairdresser.setActive(true);
		;

		Appointment dummyAppointment = this.createDummyAppointment("appointmentNotPaid");
		dummyAppointment.setIsPaid(false);

		Mockito.when(this.hairdresserService.findHairdresserById(1)).thenReturn(Optional.of(dummyHairdresser));
		Mockito.when(this.ownerService.findOwnerByUserName("carlitos")).thenReturn(this.carlitos);
		Mockito.when(this.appointmentService.findAppointmentsByOwner(this.carlitos)).thenReturn(Lists.newArrayList(dummyAppointment));
		Mockito.when(this.appointmentService.findAppointmentsByHairdresser(dummyHairdresser)).thenReturn(new ArrayList<Appointment>());
		Mockito.when(this.petService.findPets("carlitos")).thenReturn(Lists.newArrayList(this.neko));

		this.mockMvc.perform(MockMvcRequestBuilders.get("/hairdressers/{hairdresserId}/appointments/new", 1)).andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attribute("message", "You have to pay previous appointments")).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(value = "carlitos")
	@Test
	void shouldSaveAppointment() throws Exception {
		Hairdresser dummyHairdresser = this.createDummyHairdresser("Jesús", "Maroto");
		dummyHairdresser.setActive(true);

		Appointment dummyAppointment = this.createDummyAppointment("appointment");
		dummyAppointment.setPet(this.neko);
		dummyAppointment.setDate(LocalDateTime.of(2020, 7, 7, 15, 30));
		dummyAppointment.setIsPaid(false);
		dummyAppointment.setHairdresser(dummyHairdresser);

		Mockito.when(this.hairdresserService.findHairdresserById(1)).thenReturn(Optional.of(dummyHairdresser));

		this.mockMvc
		.perform(
			MockMvcRequestBuilders.post("/hairdressers/{hairdresserId}/appointments/new", 1).with(SecurityMockMvcRequestPostProcessors.csrf()).param("date", "2020/07/07 15:30").param("isPaid", "false").param("pet.id", this.neko.getId().toString()))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/appointments"));
	}

	@WithMockUser(value = "carlitos")
	@Test
	void shouldNotSaveAppointmentWithFormErrors() throws Exception {

		Hairdresser dummyHairdresser = this.createDummyHairdresser("Fernando", "Delgado");
		dummyHairdresser.setActive(true);

		Mockito.when(this.hairdresserService.findHairdresserById(1)).thenReturn(Optional.of(dummyHairdresser));

		this.mockMvc.perform(MockMvcRequestBuilders.post("/hairdressers/{hairdresserId}/appointments/new", 1).with(SecurityMockMvcRequestPostProcessors.csrf())).andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attributeHasErrors("appointment")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("appointment", "date"))
		.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("appointment", "pet")).andExpect(MockMvcResultMatchers.view().name("appointments/editAppointment"));
	}


	@WithMockUser(value = "carlitos")
	@Test
	void shouldNotSaveAppointmentWhenDateAlreadyTaken() throws Exception {
		Hairdresser dummyHairdresser = this.createDummyHairdresser("Juanjo", "Smith");
		dummyHairdresser.setActive(true);

		Appointment dummyAppointment = this.createDummyAppointment("appointment");
		dummyAppointment.setPet(this.neko);
		dummyAppointment.setDate(LocalDateTime.of(2020, 07, 20, 20, 50));
		dummyAppointment.setIsPaid(true);
		dummyAppointment.setHairdresser(dummyHairdresser);
		dummyAppointment.setOwner(this.carlitos);

		Mockito.when(this.hairdresserService.findHairdresserById(1)).thenReturn(Optional.of(dummyHairdresser));
		Mockito.when(this.appointmentService.findAppointmentsByHairdresser(ArgumentMatchers.any())).thenReturn(Lists.newArrayList(dummyAppointment));

		this.mockMvc
		.perform(
			MockMvcRequestBuilders.post("/hairdressers/{hairdresserId}/appointments/new", 1)
			.with(SecurityMockMvcRequestPostProcessors.csrf())
			.param("date", "2020/07/20 20:50")
			.param("isPaid", "false")
			.param("pet.id", this.neko.getId().toString()))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attribute("message", "Hairdresser already has an appointment at that time"))
		.andExpect(MockMvcResultMatchers.view().name("exception"));
	}



	@WithMockUser(value = "carlitos")
	@Test
	void shouldNotSaveAppointmentWhenPetAlreadyHasAnotherAppointmentThatDay() throws Exception {
		Hairdresser dummyHairdresser = this.createDummyHairdresser("Juanjo", "Smith");
		dummyHairdresser.setActive(true);

		Appointment dummyAppointment = this.createDummyAppointment("appointment");
		dummyAppointment.setPet(this.neko);
		dummyAppointment.setDate(LocalDateTime.of(2020, 07, 20, 20, 50));
		dummyAppointment.setIsPaid(true);
		dummyAppointment.setOwner(this.carlitos);

		Mockito.when(this.hairdresserService.findHairdresserById(1)).thenReturn(Optional.of(dummyHairdresser));
		Mockito.when(this.appointmentService.findAppointmentsByHairdresser(ArgumentMatchers.any())).thenReturn(Lists.newArrayList(dummyAppointment));
		Mockito.when(this.appointmentService.findAppointmentsByPet(ArgumentMatchers.any())).thenReturn(Lists.newArrayList(dummyAppointment));

		this.mockMvc
		.perform(MockMvcRequestBuilders.post("/hairdressers/{hairdresserId}/appointments/new", 1)
			.with(SecurityMockMvcRequestPostProcessors.csrf())
			.param("date", "2020/07/20 20:00")
			.param("isPaid", "false")
			.param("pet.id", this.neko.getId().toString()))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attribute("message", "You cannot make more than one appointment per day for the same pet"))
		.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

}
