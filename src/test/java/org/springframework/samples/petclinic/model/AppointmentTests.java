
package org.springframework.samples.petclinic.model;

import java.time.LocalDateTime;
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

public class AppointmentTests {

	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}

	public Pet createDummyPet() {
		Pet pet = new Pet();
		pet.setName("Razor");
		return pet;
	}

	public Owner createDummyOwner() {
		Owner owner = new Owner();
		owner.setFirstName("Jaimito");
		owner.setLastName("Garcia");
		owner.setAddress("Calle Santa Maria");
		owner.setCity("A Coruña");
		owner.setTelephone("912347611");
		return owner;
	}

	public Hairdresser createDummyHairdresser() {
		Hairdresser hairdresser = new Hairdresser();
		hairdresser.setFirstName("Rodolfo");
		hairdresser.setLastName("Rodríguez");
		hairdresser.setSpecialties(HairdresserSpecialty.HAMSTERS);
		hairdresser.setActive(true);
		return hairdresser;
	}

	public Payment createDummyPayment() {
		Payment payment = new Payment();
		payment.setName("Pago genérico");
		payment.setAmount(20.5);
		return payment;
	}

	@Test
	void shouldNotValidateWhenDateNull() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Appointment appointment = new Appointment();
		appointment.setDate(null);
		appointment.setIsPaid(false);
		appointment.setPet(this.createDummyPet());
		appointment.setHairdresser(this.createDummyHairdresser());
		appointment.setOwner(this.createDummyOwner());
		appointment.setPayment(this.createDummyPayment());

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Appointment>> constraintViolations = validator.validate(appointment);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Appointment> violation = constraintViolations.iterator().next();
		Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("date");
	}

	@Test
	void shouldNotValidateWhenPetNull() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Appointment appointment = new Appointment();
		appointment.setDate(LocalDateTime.now().plusMonths(1));
		appointment.setIsPaid(false);
		appointment.setPet(null);
		appointment.setHairdresser(this.createDummyHairdresser());
		appointment.setOwner(this.createDummyOwner());
		appointment.setPayment(this.createDummyPayment());

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Appointment>> constraintViolations = validator.validate(appointment);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Appointment> violation = constraintViolations.iterator().next();
		Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("pet");
	}

	@Test
	void shouldNotValidateWhenIsPaidNull() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Appointment appointment = new Appointment();
		appointment.setDate(LocalDateTime.now().plusMonths(1));
		appointment.setIsPaid(null);
		appointment.setPet(this.createDummyPet());
		appointment.setHairdresser(this.createDummyHairdresser());
		appointment.setOwner(this.createDummyOwner());
		appointment.setPayment(this.createDummyPayment());

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Appointment>> constraintViolations = validator.validate(appointment);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Appointment> violation = constraintViolations.iterator().next();
		Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("isPaid");
	}

	@ParameterizedTest

	@CsvSource({
		"1", "2", "3"
	})
	void shouldNotValidateWhenDateInPast(final int minusMonth) {

		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Appointment appointment = new Appointment();
		appointment.setDate(LocalDateTime.now().minusMonths(minusMonth));
		appointment.setIsPaid(false);
		appointment.setPet(this.createDummyPet());
		appointment.setHairdresser(this.createDummyHairdresser());
		appointment.setOwner(this.createDummyOwner());
		appointment.setPayment(this.createDummyPayment());

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Appointment>> constraintViolations = validator.validate(appointment);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Appointment> violation = constraintViolations.iterator().next();
		Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("date");
	}

	@Test
	void shouldNotValidateWhenDateInPresent() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Appointment appointment = new Appointment();
		appointment.setDate(LocalDateTime.now());
		appointment.setIsPaid(false);
		appointment.setPet(this.createDummyPet());
		appointment.setHairdresser(this.createDummyHairdresser());
		appointment.setOwner(this.createDummyOwner());
		appointment.setPayment(this.createDummyPayment());

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Appointment>> constraintViolations = validator.validate(appointment);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Appointment> violation = constraintViolations.iterator().next();
		Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("date");
	}

	@Test
	void shouldNotValidateWhenLengthBiggerThan512() {

		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Appointment appointment = new Appointment();
		appointment.setDate(LocalDateTime.now().plusMonths(1));
		appointment.setIsPaid(false);
		appointment.setPet(this.createDummyPet());
		appointment.setHairdresser(this.createDummyHairdresser());
		appointment.setOwner(this.createDummyOwner());
		appointment.setPayment(this.createDummyPayment());
		appointment.setDescription("Hola este texto debe superar los 512 caracteres, incluyendo espacios y signos de puntuación," + " con la finalidad de que el test pruebe que la descripción no puede tener más de 512 caracteres. "
			+ "Es por ello que vamos a empezar a spamear letras: En conclusión, se debe tener en cuenta que la calidad de los valores" + " producidos de forma aleatoria se basa en la impredecibilidad o alto grado de entropía de los mismos. Por tanto, "
			+ "la generación de datos aleatorios es más fuerte de forma inversamente proporcional a la posibilidad de predicción" + " de sus resultados.");

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Appointment>> constraintViolations = validator.validate(appointment);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Appointment> violation = constraintViolations.iterator().next();
		Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("description");
	}

	@Test
	void shouldValidateWhenEverythingOk() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Appointment appointment = new Appointment();
		appointment.setDate(LocalDateTime.now().plusMonths(1));
		appointment.setIsPaid(false);
		appointment.setPet(this.createDummyPet());
		appointment.setHairdresser(this.createDummyHairdresser());
		appointment.setOwner(this.createDummyOwner());
		appointment.setPayment(this.createDummyPayment());

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Appointment>> constraintViolations = validator.validate(appointment);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(0);
	}

	@ParameterizedTest

	@ValueSource(booleans = {
		true, false
	})
	void shouldValidateWhenIsPaidOk(final boolean argument) {

		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Appointment appointment = new Appointment();
		appointment.setDate(LocalDateTime.now().plusMonths(1));
		appointment.setIsPaid(argument);
		appointment.setPet(this.createDummyPet());
		appointment.setHairdresser(this.createDummyHairdresser());
		appointment.setOwner(this.createDummyOwner());
		appointment.setPayment(this.createDummyPayment());

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Appointment>> constraintViolations = validator.validate(appointment);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(0);
	}

	@Test
	void shouldValidateWhenPaymentNull() {

		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Appointment appointment = new Appointment();
		appointment.setDate(LocalDateTime.now().plusMonths(1));
		appointment.setIsPaid(false);
		appointment.setPet(this.createDummyPet());
		appointment.setHairdresser(this.createDummyHairdresser());
		appointment.setOwner(this.createDummyOwner());
		appointment.setPayment(null);

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Appointment>> constraintViolations = validator.validate(appointment);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(0);
	}
}
