
package org.springframework.samples.petclinic.repository.springdatajpa;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Announcement;
import org.springframework.samples.petclinic.model.Answer;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.repository.AnswerRepository;

public interface SpringDataAnswerRepository extends AnswerRepository, Repository<Answer, Integer> {

	@Override
	@Query("SELECT answer FROM Answer answer WHERE answer.announcement =:announcement")
	Collection<Answer> findAnswersByAnnouncement(@Param("announcement") Announcement announcement);

	@Override
	@Query("SELECT count(answer) FROM Answer answer WHERE answer.owner=:owner")
	Collection<Answer> findAnswersByOwner(@Param("owner") Owner owner);

}
