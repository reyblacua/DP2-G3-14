
package org.springframework.samples.petclinic.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Announcement;
import org.springframework.samples.petclinic.model.Answer;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.repository.AnswerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AnswerService {

	private AnswerRepository answerRepo;


	@Autowired
	public AnswerService(final AnswerRepository answerRepository) {
		this.answerRepo = answerRepository;
	}

	@Transactional
	public Iterable<Answer> findAll() {
		Iterable<Answer> answers = this.answerRepo.findAll();
		if (StreamSupport.stream(answers.spliterator(), false).count() == 0) {
			throw new NoSuchElementException();
		}
		return answers;
	}

	public Answer findAnswerById(final int answerId) throws NoSuchElementException {
		Optional<Answer> opt = this.answerRepo.findById(answerId);
		Answer answer = opt.get();
		return answer;
	}

	@Transactional
	public Iterable<Answer> findAnswerByAnnouncement(final Announcement announcement) throws NoSuchElementException {
		Iterable<Answer> answers = this.answerRepo.findAnswersByAnnouncement(announcement);
		if (StreamSupport.stream(answers.spliterator(), false).count() == 0) {
			throw new NoSuchElementException();
		}
		return answers;
	}

	public void saveAnswer(final Answer answer) throws DataAccessException {
		this.answerRepo.save(answer);
	}

	public void delete(final Answer a) {
		this.answerRepo.delete(a);
	}

	public List<Answer> findAnswerByOwner(final Owner owner) throws NoSuchElementException {
		List<Answer> answers = (List<Answer>) this.answerRepo.findAnswersByOwner(owner);
		if (StreamSupport.stream(answers.spliterator(), false).count() == 0) {
			throw new NoSuchElementException();
		}
		return answers;
	}

}
