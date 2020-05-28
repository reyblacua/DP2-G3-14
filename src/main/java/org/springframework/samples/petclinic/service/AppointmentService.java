
package org.springframework.samples.petclinic.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Appointment;
import org.springframework.samples.petclinic.model.Hairdresser;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.repository.AppointmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AppointmentService {

	private AppointmentRepository appointmentRepo;


	@Autowired
	public AppointmentService(final AppointmentRepository appointmentRepository) {
		this.appointmentRepo = appointmentRepository;
	}

	@Transactional(readOnly = true)
	public Collection<Appointment> findAppointmentsByOwner(final Owner owner) throws NoSuchElementException {
		Collection<Appointment> result = this.appointmentRepo.findAppointmentsByOwner(owner);
		if (StreamSupport.stream(result.spliterator(), false).count() == 0) {
			throw new NoSuchElementException();
		}
		return result;
	}

	@Transactional(readOnly = true)
	public Collection<Appointment> findAppointmentsByHairdresser(final Hairdresser hairdresser) throws NoSuchElementException {
		Collection<Appointment> result = this.appointmentRepo.findAppointmentsByHairdresser(hairdresser);
		return result;
	}

	@Transactional(readOnly = true)
	public Collection<Appointment> findAppointmentsByPet(final Pet pet) throws NoSuchElementException {
		Collection<Appointment> result = this.appointmentRepo.findAppointmentsByPet(pet);
		return result;
	}

	@Transactional(readOnly = true)
	public Optional<Appointment> findAppointmentById(final int appointmentId) throws NoSuchElementException {
		Optional<Appointment> result = this.appointmentRepo.findById(appointmentId);
		result.get();
		return result;
	}

	@Transactional
	public void saveAppointment(final Appointment appointment) throws DataAccessException {
		this.appointmentRepo.save(appointment);
	}

	@Transactional
	public void deleteAppointment(final Appointment appointment) throws Exception {

		if (appointment.getDate().isBefore(LocalDateTime.now())) {
			throw new Exception("You cannot delete a passed appointment");
		}

		// No puedes borrar una cita el mismo dia que esta tiene lugar
		if (LocalDateTime.now().until(appointment.getDate(), ChronoUnit.HOURS) < 24) {
			throw new Exception("You cannot delete an appointment whose date is today");
		}

		this.appointmentRepo.delete(appointment);
	}

}
