
package org.springframework.samples.petclinic.service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Appointment;
import org.springframework.samples.petclinic.model.Hairdresser;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace=Replace.NONE)
public class AppointmentServiceTests {

	@Autowired
	protected AppointmentService	appointmentService;
	@Autowired
	protected OwnerService			ownerService;
	@Autowired
	protected HairdresserService	hairdresserService;
	@Autowired
	protected PetService			petService;


	@ParameterizedTest
	@CsvSource({
		"owner1", "owner2", "owner4"
	})
	void shouldFindAppointmentsGivingOwner(final String ownername) {
		Owner owner = this.ownerService.findOwnerByUserName(ownername);
		Collection<Appointment> appointments = this.appointmentService.findAppointmentsByOwner(owner);
		org.assertj.core.api.Assertions.assertThat(appointments).hasSize(1);
	}

	@ParameterizedTest
	@CsvSource({
		"owner8", "owner9", "owner10"
	})
	void shouldNotFindAppointmentsGivingOwnerWithoutThem(final String ownername) {
		Owner owner = this.ownerService.findOwnerByUserName(ownername);
		Assertions.assertThrows(NoSuchElementException.class, () -> {
			this.appointmentService.findAppointmentsByOwner(owner);
		});
	}

	@ParameterizedTest
	@CsvSource({
		"1", "2", "3"
	})
	void shouldFindAppointmentsGivingHairdresser(final int hairdresserId) {
		Hairdresser hairdresser = this.hairdresserService.findHairdresserById(hairdresserId).get();
		Collection<Appointment> appointments = this.appointmentService.findAppointmentsByHairdresser(hairdresser);
		org.assertj.core.api.Assertions.assertThat(appointments).hasSize(1);
	}

	@ParameterizedTest
	@CsvSource({
		"1", "2", "3"
	})
	void shouldFindAppointmentsGivingPet(final int petId) {
		Pet pet = this.petService.findPetById(petId);
		Collection<Appointment> appointments = this.appointmentService.findAppointmentsByPet(pet);
		org.assertj.core.api.Assertions.assertThat(appointments).hasSize(1);
	}

	@ParameterizedTest
	@CsvSource({
		"1,Cita1,Cita para Leo", "2,Cita2,Cita para Basil", "3,Cita3,Cita para Rosy"
	})
	void shouldFindAppointment(final int id, final String name, final String description) {
		Optional<Appointment> appointment = this.appointmentService.findAppointmentById(id);
		org.assertj.core.api.Assertions.assertThat(appointment).isPresent();
		org.assertj.core.api.Assertions.assertThat(appointment.get().getName()).isEqualTo(name);
		org.assertj.core.api.Assertions.assertThat(appointment.get().getDescription()).isEqualTo(description);

	}

	@ParameterizedTest

	@CsvSource({
		"200", "-1", "0"
	})
	void shouldNotFindAppointment(final int id) {
		Assertions.assertThrows(NoSuchElementException.class, () -> {
			this.appointmentService.findAppointmentById(id);
		});

	}

	@ParameterizedTest

	@CsvSource({
		"3,owner3", "4,owner3"
	})
	@Transactional
	void shouldDeleteAppointment(final int id, final String ownername) throws Exception {

		Owner owner = this.ownerService.findOwnerByUserName(ownername);

		Collection<Appointment> appointments = this.appointmentService.findAppointmentsByOwner(owner);
		Optional<Appointment> appointment = this.appointmentService.findAppointmentById(id);
		int count = appointments.size();

		org.assertj.core.api.Assertions.assertThat(appointment.isPresent());
		this.appointmentService.deleteAppointment(appointment.get());
		org.assertj.core.api.Assertions.assertThat(!appointment.isPresent());

		appointments = this.appointmentService.findAppointmentsByOwner(owner);
		org.assertj.core.api.Assertions.assertThat(appointments.size()).isEqualTo(count - 1);

	}

	@Test
	@Transactional
	void shouldNotDeleteAppointment() {

		Assertions.assertThrows(NullPointerException.class, () -> {
			this.appointmentService.deleteAppointment(null);
		});

	}

	@Test
	@Transactional
	void shouldNotDeleteAppointmentHappeningToday() throws Exception {
		Owner owner1 = this.ownerService.findOwnerById(1);
		Pet pet1 = owner1.getPet("Leo");
		Hairdresser hairdresser1 = this.hairdresserService.findHairdresserById(1).get();

		Appointment appointment = new Appointment();
		appointment.setOwner(owner1);
		appointment.setPet(pet1);
		appointment.setHairdresser(hairdresser1);
		appointment.setName("Cita para mi mascota");
		appointment.setDescription("Cita para cortarle el pelo y las uñas a mi mascota");
		appointment.setDate(LocalDateTime.now().plusMinutes(30));
		appointment.setIsPaid(false);

		Assertions.assertThrows(Exception.class, () -> {
			this.appointmentService.deleteAppointment(appointment);
		}, "You cannot delete an appointment whose date is today");
	}

	@Test
	@Transactional
	void shouldNotDeletePassedAppointment() throws Exception {
		Owner owner1 = this.ownerService.findOwnerById(1);
		Pet pet1 = owner1.getPet("Leo");
		Hairdresser hairdresser1 = this.hairdresserService.findHairdresserById(1).get();

		Appointment appointment = new Appointment();
		appointment.setOwner(owner1);
		appointment.setPet(pet1);
		appointment.setHairdresser(hairdresser1);
		appointment.setName("Cita para mi mascota");
		appointment.setDescription("Cita para cortarle el pelo y las uñas a mi mascota");
		appointment.setDate(LocalDateTime.now().minusMinutes(30));
		appointment.setIsPaid(false);

		Assertions.assertThrows(Exception.class, () -> {
			this.appointmentService.deleteAppointment(appointment);
		}, "You cannot delete a passed appointment");
	}

	@Test
	@Transactional
	void shouldInsertAppointmentIntoDatabaseAndGenerateId() {
		Owner owner1 = this.ownerService.findOwnerById(1);
		Integer defaultAppointments = this.appointmentService.findAppointmentsByOwner(owner1).size();
		Pet pet1 = owner1.getPet("Leo");
		Hairdresser hairdresser1 = this.hairdresserService.findHairdresserById(1).get();

		Appointment appointment = new Appointment();
		appointment.setOwner(owner1);
		appointment.setPet(pet1);
		appointment.setHairdresser(hairdresser1);
		appointment.setName("Cita para mi mascota");
		appointment.setDescription("Cita para cortarle el pelo y las uñas a mi mascota");
		appointment.setDate(LocalDateTime.now().plusMinutes(30));
		appointment.setIsPaid(false);

		this.appointmentService.saveAppointment(appointment);

		org.assertj.core.api.Assertions.assertThat(this.appointmentService.findAppointmentsByOwner(owner1).size()).isEqualTo(defaultAppointments + 1);
	}

}
