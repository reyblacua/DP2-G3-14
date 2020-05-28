package org.springframework.samples.petclinic.repository.springdatajpa;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Appointment;
import org.springframework.samples.petclinic.model.Hairdresser;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.repository.AppointmentRepository;

public interface SpringDataAppointmentRepository extends AppointmentRepository, Repository<Appointment, Integer> {

	@Override
	@Query("SELECT appointment FROM Appointment appointment WHERE appointment.owner = :owner")
	public Collection<Appointment> findAppointmentsByOwner(@Param("owner") Owner owner);
	
	@Override
	@Query("SELECT appointment FROM Appointment appointment WHERE appointment.hairdresser = :hairdresser")
	public Collection<Appointment> findAppointmentsByHairdresser(@Param("hairdresser") Hairdresser hairdresser);
	
	@Override
	@Query("SELECT appointment FROM Appointment appointment WHERE appointment.pet = :pet")
	Collection<Appointment> findAppointmentsByPet(@Param("pet") Pet pet);
}
