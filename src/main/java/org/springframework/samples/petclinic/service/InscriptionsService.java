
package org.springframework.samples.petclinic.service;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Course;
import org.springframework.samples.petclinic.model.Inscription;
import org.springframework.samples.petclinic.repository.CourseRepository;
import org.springframework.samples.petclinic.repository.InscriptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InscriptionsService {

	InscriptionRepository inscriptionRepository;

	CourseRepository courseRepository;

	@Autowired
	public InscriptionsService(final InscriptionRepository inscriptionRepository) {
		this.inscriptionRepository = inscriptionRepository;
	}

	public Iterable<Inscription> findInscriptionsByOwner(final org.springframework.samples.petclinic.model.Owner owner) throws NoSuchElementException{
		Iterable<Inscription> res = this.inscriptionRepository.findInscriptionsByOwner(owner);
		if (StreamSupport.stream(res.spliterator(), false).count() == 0	) {
			throw new NoSuchElementException();
		}
		return res;
	}

	public Optional<Inscription> findInscriptionById(final int inscriptionId) throws NoSuchElementException {
		Optional<Inscription> res = this.inscriptionRepository.findById(inscriptionId);
		res.get();
		return res;
	}

	@Transactional
	public void saveInscription(final Inscription inscription) throws Exception {
		// ------ Validación reglas de negocio ------

		//No puede apuntarse a una mascota a un curso destinado a otro tipo de mascota
		if (!inscription.getPet().getType().equals(inscription.getCourse().getPetType())) {
			throw new Exception("You can not sign up a pet in other pet type course");
		}

		//No puede apuntarse a una mascota a un curso si no está vacunada
		if (inscription.getPet().getIsVaccinated() == false) {
			throw new Exception("You can not sign up a pet if it is not vaccinated");
		}

		//No puede apuntarse a una mascota peligrosa a un curso que no admita este tipo de mascotas
		if (inscription.getPet().getDangerous() == true && inscription.getCourse().getDangerousAllowed() == false) {
			throw new Exception("You can not sign up a dangerous pet in a not-dangerous pet course");
		}

		//No puede apuntarse a una misma mascota varias veces a un curso
		Iterable<Inscription> inscriptions = null;
		try {
			inscriptions = this.findInscriptionsByCourse(inscription.getCourse());
		} catch (NoSuchElementException e) {
		}
		if (inscriptions != null) {
			Boolean isThePetInTheCourse = false;
			for(Inscription inscriptionit: inscriptions) {
				if (inscriptionit.getPet().equals(inscription.getPet())) {
					isThePetInTheCourse = true;
					break;
				}
			}
			if (isThePetInTheCourse == true) {
				throw new Exception("You can not sign up your pet in the same course twice");
			}
		}


		this.inscriptionRepository.save(inscription);
	}

	@Transactional
	public void deleteInscription(final Inscription inscription) {
		this.inscriptionRepository.delete(inscription);
	}

	public Iterable<Inscription> findInscriptionsByCourse(final Course course) throws NoSuchElementException{
		Iterable<Inscription> res = this.inscriptionRepository.findInscriptionsByCourse(course);
		if (StreamSupport.stream(res.spliterator(), false).count() == 0	) {
			throw new NoSuchElementException();
		}
		return res;
	}
}
