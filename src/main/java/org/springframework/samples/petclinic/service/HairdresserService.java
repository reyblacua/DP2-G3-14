package org.springframework.samples.petclinic.service;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Hairdresser;
import org.springframework.samples.petclinic.repository.HairdresserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HairdresserService {

	
	private HairdresserRepository hairdresserRepo;
	
	@Autowired
	public HairdresserService(final HairdresserRepository hairdresserRepository) {
		this.hairdresserRepo = hairdresserRepository;
	}
	
	@Transactional(readOnly = true)
	public Iterable<Hairdresser> findAll() throws NoSuchElementException{
		Iterable<Hairdresser> result =  this.hairdresserRepo.findAll();
		if(StreamSupport.stream(result.spliterator(), false).count()==0) {
			throw new NoSuchElementException();
		}
		return result;
	}

	@Transactional(readOnly = true)
	public Optional<Hairdresser> findHairdresserById(final int hairdresserId) throws NoSuchElementException{
		Optional<Hairdresser> result = this.hairdresserRepo.findById(hairdresserId);
		result.get();
		return result;
	}
	
}
