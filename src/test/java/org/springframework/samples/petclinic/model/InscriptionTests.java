
package org.springframework.samples.petclinic.model;

import java.time.LocalDate;
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

public class InscriptionTests {

	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}

	public Pet createDummyPet() {
		Pet pet = new Pet();
		pet.setName("Dummy");
		return pet;
	}

	@Test
	void shouldNotValidateWhenDateNull() {

		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Inscription inscription = new Inscription();
		inscription.setDate(null);
		inscription.setIsPaid(false);
		inscription.setPet(this.createDummyPet());

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Inscription>> constraintViolations = validator.validate(inscription);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Inscription> violation = constraintViolations.iterator().next();
		Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("date");
	}

	@ParameterizedTest
	@CsvSource({
		"1", "2", "3"
	})
	void shouldNotValidateWhenDateInFuture(final int plusMonth) {

		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Inscription inscription = new Inscription();
		inscription.setDate(LocalDate.now().plusMonths(plusMonth));
		inscription.setIsPaid(false);
		inscription.setPet(this.createDummyPet());

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Inscription>> constraintViolations = validator.validate(inscription);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Inscription> violation = constraintViolations.iterator().next();
		Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("date");
	}

	@Test
	void shouldValidateWhenEverythingOk() {

		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Inscription inscription = new Inscription();
		inscription.setDate(LocalDate.now().minusMonths(1));
		inscription.setIsPaid(false);
		inscription.setPet(this.createDummyPet());

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Inscription>> constraintViolations = validator.validate(inscription);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(0);

	}

	@Test
	void shouldNotValidateWhenIsPaidNull() {

		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Inscription inscription = new Inscription();
		inscription.setDate(LocalDate.now().minusMonths(1));
		inscription.setIsPaid(null);
		inscription.setPet(this.createDummyPet());

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Inscription>> constraintViolations = validator.validate(inscription);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Inscription> violation = constraintViolations.iterator().next();
		Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("isPaid");
	}

	@ParameterizedTest
	@ValueSource(booleans = {
		true, false
	})
	void shouldValidateWhenIsPaidOk(final boolean argument) {

		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Inscription inscription = new Inscription();
		inscription.setDate(LocalDate.now().minusMonths(1));
		inscription.setIsPaid(argument);
		inscription.setPet(this.createDummyPet());

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Inscription>> constraintViolations = validator.validate(inscription);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(0);
	}

	@Test
	void shouldValidateWhenPaymentNull() {

		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Inscription inscription = new Inscription();
		inscription.setDate(LocalDate.now().minusMonths(1));
		inscription.setIsPaid(false);
		inscription.setPet(this.createDummyPet());
		inscription.setPayment(null);

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Inscription>> constraintViolations = validator.validate(inscription);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(0);
	}

	@Test
	void shouldValidateWhenPaymentNotNull() {

		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Inscription inscription = new Inscription();
		inscription.setDate(LocalDate.now().minusMonths(1));
		inscription.setIsPaid(false);
		inscription.setPet(this.createDummyPet());
		inscription.setPayment(new Payment());

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Inscription>> constraintViolations = validator.validate(inscription);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(0);
	}

	@Test
	void shouldNotValidateWhenPetNull() {

		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Inscription inscription = new Inscription();
		inscription.setDate(LocalDate.now().minusMonths(1));
		inscription.setIsPaid(false);
		inscription.setPet(null);

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Inscription>> constraintViolations = validator.validate(inscription);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Inscription> violation = constraintViolations.iterator().next();
		Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("pet");
	}

	@Test
	void shouldValidateWhenCourseNull() {

		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Inscription inscription = new Inscription();
		inscription.setDate(LocalDate.now().minusMonths(1));
		inscription.setIsPaid(false);
		inscription.setPet(this.createDummyPet());
		inscription.setCourse(null);

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Inscription>> constraintViolations = validator.validate(inscription);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(0);
	}

	@Test
	void shouldValidateWhenCourseNotNull() {

		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Inscription inscription = new Inscription();
		inscription.setDate(LocalDate.now().minusMonths(1));
		inscription.setIsPaid(false);
		inscription.setPet(this.createDummyPet());
		inscription.setCourse(new Course());

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Inscription>> constraintViolations = validator.validate(inscription);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(0);
	}
}
