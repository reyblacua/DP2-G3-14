
package org.springframework.samples.petclinic.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.samples.petclinic.model.Announcement;
import org.springframework.samples.petclinic.model.Answer;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.repository.AnswerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace=Replace.NONE)
class AnswerServiceTests {

	@Autowired
	protected AnswerService			answerService;

	@Autowired
	protected OwnerService			ownerService;

	@Autowired
	protected PetService			petService;

	@Autowired
	protected AnnouncementService	announcementService;


	@Test
	void shouldFindAllAnswers() {
		Collection<Answer> answers = (Collection<Answer>) this.answerService.findAll();
		org.assertj.core.api.Assertions.assertThat(answers.size()).isEqualTo(3);
	}

	@Test
	void shouldNotFindAnswers() {
		AnswerRepository answerRepository = Mockito.mock(AnswerRepository.class);
		this.answerService = new AnswerService(answerRepository);
		Mockito.when(answerRepository.findAll()).thenReturn(new ArrayList<Answer>());
		Assertions.assertThrows(NoSuchElementException.class, () -> {
			this.answerService.findAll();
		});
	}

	@Test
	void shouldFindAnswersByAnnouncement() {
		Announcement announcement = this.announcementService.findAnnouncementById(1).get();
		Collection<Answer> answers = (Collection<Answer>) this.answerService.findAnswerByAnnouncement(announcement);
		org.assertj.core.api.Assertions.assertThat(answers.size()).isEqualTo(2);
	}

	@Test
	void shouldNotFindAnswersGivingAnnouncementWithoutThem() {
		Announcement announcement = this.announcementService.findAnnouncementById(3).get();
		Assertions.assertThrows(NoSuchElementException.class, () -> {
			this.answerService.findAnswerByAnnouncement(announcement);
		});

	}

	@Test
	void shouldFindSingleAnswerById() {
		Answer answer = this.answerService.findAnswerById(1);
		org.assertj.core.api.Assertions.assertThat(answer.getName()).isEqualTo("Respuesta1");
		org.assertj.core.api.Assertions.assertThat(answer.getDate()).isEqualTo("2010-03-09");
		org.assertj.core.api.Assertions.assertThat(answer.getDescription()).isEqualTo("Hola");
		org.assertj.core.api.Assertions.assertThat(answer.getOwner()).isNotNull();
		org.assertj.core.api.Assertions.assertThat(answer.getAnnouncement()).isNotNull();
	}

	@Test
	void shoulsNotFindAnswerByIncorrectId() {
		Assertions.assertThrows(NoSuchElementException.class, () -> {
			this.answerService.findAnswerById(200);
		});
	}

	@Test
	@Transactional
	public void shouldInsertAnswer() {
		Collection<Answer> answers = (Collection<Answer>) this.answerService.findAll();
		int found = answers.size();

		Answer answer = new Answer();
		Announcement announcement = this.announcementService.findAnnouncementById(1).get();
		Owner owner2 = this.ownerService.findOwnerById(2);
		answer.setOwner(owner2);
		answer.setAnnouncement(announcement);
		answer.setDate(LocalDate.of(2020, 3, 1));
		answer.setDescription("Hola");
		answer.setName("Respuesta4");
		answer.setId(4);

		this.answerService.saveAnswer(answer);
		org.assertj.core.api.Assertions.assertThat(answer.getId().longValue()).isNotEqualTo(0);

		answers = (Collection<Answer>) this.answerService.findAll();
		org.assertj.core.api.Assertions.assertThat(answers.size()).isEqualTo(found + 1);
	}

	@Test
	public void shouldDeleteAnswer() {
		Answer answer = this.answerService.findAnswerById(2);
		Collection<Answer> answersIni = (Collection<Answer>) this.answerService.findAll();
		int tamInicial = answersIni.size();
		this.answerService.delete(answer);
		Collection<Answer> answers = (Collection<Answer>) this.answerService.findAll();
		org.assertj.core.api.Assertions.assertThat(answers.size()).isEqualTo(tamInicial - 1);
	}

	@Test
	public void shouldNotDeleteAnswerFromDatabaseWhenNull() {
		Assertions.assertThrows(InvalidDataAccessApiUsageException.class, () -> {
			this.answerService.delete(null);
		});
	}

	@Test
	public void shouldNotDeleteAnswerWithIncorrectId() {
		Assertions.assertThrows(NoSuchElementException.class, () -> {
			this.answerService.delete(this.answerService.findAnswerById(200));
		});
	}

	@Test
	public void shouldFindAnswersByOwner() {
		Owner owner = this.ownerService.findOwnerById(1);
		Collection<Answer> answers = this.answerService.findAnswerByOwner(owner);
		org.assertj.core.api.Assertions.assertThat(answers.size()).isEqualTo(1);
	}

	@Test
	void shouldNotFindAnswersGivingOwnerWithoutThem() {
		Owner owner = this.ownerService.findOwnerById(12);
		Assertions.assertThrows(NoSuchElementException.class, () -> {
			this.answerService.findAnswerByOwner(owner);
		});

	}

}
