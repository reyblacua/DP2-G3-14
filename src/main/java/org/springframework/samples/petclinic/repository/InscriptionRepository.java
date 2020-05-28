
package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Course;
import org.springframework.samples.petclinic.model.Inscription;
import org.springframework.stereotype.Repository;

@Repository
public interface InscriptionRepository extends CrudRepository<Inscription, Integer> {

	Iterable<Inscription> findInscriptionsByOwner(org.springframework.samples.petclinic.model.Owner owner) throws DataAccessException;

	@Override
	Collection<Inscription> findAll() throws DataAccessException;

	Iterable<Inscription> findInscriptionsByCourse(Course course) throws DataAccessException;

}
