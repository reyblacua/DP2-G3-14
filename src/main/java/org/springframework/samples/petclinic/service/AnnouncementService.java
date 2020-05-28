
package org.springframework.samples.petclinic.service;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Announcement;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.repository.AnnouncementRepository;
import org.springframework.samples.petclinic.repository.PetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AnnouncementService {

	@Autowired
	private AnnouncementRepository	announcementRepo;
	private PetRepository			petRepo;


	@Autowired
	public AnnouncementService(final AnnouncementRepository announcementRepository, final PetRepository petRepository) {
		this.announcementRepo = announcementRepository;
		this.petRepo = petRepository;
	}

	@Transactional
	public Iterable<Announcement> findAll() {
		Iterable<Announcement> res = this.announcementRepo.findAll();
		if (StreamSupport.stream(res.spliterator(), false).count() == 0) {
			throw new NoSuchElementException();
		}
		return res;
	}

	public Optional<Announcement> findAnnouncementById(final int announcementId) throws NoSuchElementException {
		Optional<Announcement> res = this.announcementRepo.findById(announcementId);
		res.get();
		return res;
	}

	public void saveAnnouncement(final Announcement announcement) throws DataAccessException {
		this.announcementRepo.save(announcement);
	}

	@Transactional(readOnly = true)
	public Collection<PetType> findPetTypes() throws DataAccessException {
		return this.petRepo.findPetTypes();
	}

	@Transactional
	public void deleteAnnouncement(final Announcement announcement) {
		this.announcementRepo.delete(announcement);
	}

}
