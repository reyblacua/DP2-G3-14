
package org.springframework.samples.petclinic.model;

import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

public class AnnouncementTests {

	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}

	@ParameterizedTest
	@CsvSource({
		"'Moka','Hola',true,'AnuncioTest1'", "'Poki','Adios',false,'AnuncioTest2'"
	})
	void shouldValidateWhenEverythingOK(final String petName, final String description, final boolean canBeAdopted, final String name) {
		Announcement announcement = new Announcement();
		announcement.setPetName(petName);
		announcement.setDescription(description);
		announcement.setCanBeAdopted(canBeAdopted);
		announcement.setOwner(new Owner());
		announcement.setType(new PetType());
		announcement.setName(name);

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Announcement>> constraintViolations = validator.validate(announcement);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(0);
	}

	@Test
	void shouldNotValidateWhenEverythingNull() {
		Announcement announcement = new Announcement();
		announcement.setPetName(null);
		announcement.setDescription(null);
		announcement.setCanBeAdopted(null);
		announcement.setOwner(null);
		announcement.setType(null);
		announcement.setName(null);

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Announcement>> constraintViolations = validator.validate(announcement);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(3);
	}

	@Test
	void shouldNotValidateWhenPetNameEmpty() {

		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Announcement announcement = new Announcement();
		announcement.setPetName("");
		announcement.setCanBeAdopted(true);
		announcement.setDescription("Hola");
		announcement.setType(new PetType());

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Announcement>> constraintViolations = validator.validate(announcement);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Announcement> violation = constraintViolations.iterator().next();
		Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("petName");
		Assertions.assertThat(violation.getMessage()).isEqualTo("must not be blank");
	}

	@ParameterizedTest
	@ValueSource(strings = {
		"Esto es un texto, Esto es un texto, Esto es un texto, Esto es un texto, Esto es un texto, Esto es un texto, Esto es un texto, Esto es un texto, Esto es un texto, Esto es un texto, Esto es un texto, Esto",//
		"Es",//
		""
	})
	void shouldNotValidateDescriptionNotInRange(final String text) {

		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Announcement announcement = new Announcement();
		announcement.setDescription(text);
		announcement.setType(new PetType());
		announcement.setCanBeAdopted(true);
		announcement.setPetName("Fox");

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Announcement>> constraintViolations = validator.validate(announcement);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Announcement> violation = constraintViolations.iterator().next();
		Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("description");
		Assertions.assertThat(violation.getMessage()).isEqualTo("size must be between 3 and 200");
	}

	@ParameterizedTest
	@ValueSource(strings = {
		"",//
		"Esto es un texto, Esto es un texto, Esto es un texto",//
		"Es"
	})
	void shouldNotValidateNameNotInRange(final String name) {

		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Announcement announcement = new Announcement();
		announcement.setType(new PetType());
		announcement.setCanBeAdopted(true);
		announcement.setDescription("Hola");
		announcement.setPetName("Foxy");
		announcement.setName(name);

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Announcement>> constraintViolations = validator.validate(announcement);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Announcement> violation = constraintViolations.iterator().next();
		Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("name");
		Assertions.assertThat(violation.getMessage()).isEqualTo("size must be between 3 and 50");
	}

}
