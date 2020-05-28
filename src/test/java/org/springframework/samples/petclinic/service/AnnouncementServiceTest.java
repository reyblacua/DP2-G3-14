
package org.springframework.samples.petclinic.service;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.samples.petclinic.model.Announcement;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.repository.AnnouncementRepository;
import org.springframework.samples.petclinic.repository.PetRepository;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace=Replace.NONE)
public class AnnouncementServiceTest {

	@Autowired
	protected AnnouncementService	announcementService;

	@Autowired
	protected OwnerService			ownerService;

	@Autowired
	protected PetService			petService;


	@Test
	void shouldFindAllAnnouncements() {
		Iterable<Announcement> announcements = this.announcementService.findAll();
		org.assertj.core.api.Assertions.assertThat(announcements).hasSize(3);
	}

	@Test
	void shouldNotFindCourses() {
		AnnouncementRepository announcementRepository = Mockito.mock(AnnouncementRepository.class);
		PetRepository petRepository = Mockito.mock(PetRepository.class);
		this.announcementService = new AnnouncementService(announcementRepository, petRepository);
		Mockito.when(announcementRepository.findAll()).thenReturn(new ArrayList<Announcement>());
		Assertions.assertThrows(NoSuchElementException.class, () -> {
			this.announcementService.findAll();
		});
	}

	@Test
	void shouldFindAnnouncementById() {
		Optional<Announcement> announcement = this.announcementService.findAnnouncementById(1);
		org.assertj.core.api.Assertions.assertThat(announcement).isPresent();
		org.assertj.core.api.Assertions.assertThat(announcement.get().getName()).isEqualTo("Anuncio1");
	}

	@Test
	void shouldNotFindAnnouncementWithIncorrectId() {
		Assertions.assertThrows(NoSuchElementException.class, () -> {
			this.announcementService.findAnnouncementById(200);
		});
	}

	@Test
	public void shouldInsertAnnouncementIntoDatabaseAndGenerateId() throws Exception {

		Owner owner3 = this.ownerService.findOwnerById(3);
		PetType petType = this.petService.findPetById(1).getType();

		Announcement announcement = new Announcement();
		announcement.setName("TestAnnouncement");
		announcement.setOwner(owner3);
		announcement.setCanBeAdopted(true);
		announcement.setDescription("Hola, amigo");
		announcement.setPetName("Borris");
		announcement.setType(petType);

		this.announcementService.saveAnnouncement(announcement);

		org.assertj.core.api.Assertions.assertThat(announcement.getId()).isNotNull();
	}

	@ParameterizedTest
	@CsvSource({
		"1", "2", "3"
	})
	public void shouldDeleteAnnouncmeentFromDatabase(final int id) {
		Announcement announcement = this.announcementService.findAnnouncementById(id).get();
		this.announcementService.deleteAnnouncement(announcement);
		Assertions.assertThrows(NoSuchElementException.class, () -> {
			this.announcementService.findAnnouncementById(id);
		});
	}

	@Test
	public void shouldNotDeleteAnnouncementFromDatabaseWhenNull() {
		Assertions.assertThrows(InvalidDataAccessApiUsageException.class, () -> {
			this.announcementService.deleteAnnouncement(null);
		});
	}

}
