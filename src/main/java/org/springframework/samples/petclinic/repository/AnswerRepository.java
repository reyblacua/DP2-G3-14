
package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Announcement;
import org.springframework.samples.petclinic.model.Answer;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerRepository extends CrudRepository<Answer, Integer> {

	@Override
	Collection<Answer> findAll() throws DataAccessException;

	Collection<Answer> findAnswersByAnnouncement(Announcement announcement) throws DataAccessException;

	Collection<Answer> findAnswersByOwner(Owner owner) throws DataAccessException;

}
