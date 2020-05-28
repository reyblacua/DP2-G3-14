
package org.springframework.samples.petclinic.model;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

public class AnswerTests {

	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}

	private Answer generateAnswer() {
		Answer answer = new Answer();
		answer.setDate(LocalDate.of(2018, 3, 17));
		answer.setDescription("Hola");
		Owner owner = new Owner();
		Announcement announcement = new Announcement();

		answer.setAnnouncement(announcement);
		answer.setOwner(owner);
		answer.setName("Respuesta4");
		return answer;
	}

	//VALIDACIONES DATES

	//No debe validar si la fecha está en futuro
	@Test
	void shouldNotValidateWhendateIsInTheFuture() {

		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Answer answer = this.generateAnswer();
		answer.setDate(LocalDate.of(2021, 3, 17));

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Answer>> constraintViolations = validator.validate(answer);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Answer> violation = constraintViolations.iterator().next();
		Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("date");
		Assertions.assertThat(violation.getMessage()).isEqualTo("must be a past date");
	}

	//Debe validar si la fecha está en pasado
	@Test
	void shouldValidateWhendateIsInThePast() {

		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Answer answer = this.generateAnswer();
		answer.setDate(LocalDate.of(2018, 3, 17));
		Validator validator = this.createValidator();
		Set<ConstraintViolation<Answer>> constraintViolations = validator.validate(answer);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(0);
	}

	//No debe validar si la fecha es null
	@Test
	void shouldNotValidateWhendateIsNull() {

		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Answer answer = this.generateAnswer();
		answer.setDate(null);

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Answer>> constraintViolations = validator.validate(answer);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Answer> violation = constraintViolations.iterator().next();
		Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("date");
		Assertions.assertThat(violation.getMessage()).isEqualTo("must not be null");
	}

	//VALIDACIONES DESCRIPTION

	//Debe validar si la description está dentro del rango, es 3, es 200
	@ParameterizedTest
	@NullSource
	@ValueSource(strings = {
		"Esto es un texto",//
		"Est",//
		"Esto es un texto, Esto es un texto, Esto es un texto, Esto es un texto, Esto es un texto, Esto es un texto, Esto es un texto, Esto es un texto, Esto es un texto, Esto es un texto, Esto es un texto, Es"
	})
	void shouldValidateDescription(final String text) {

		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Answer answer = this.generateAnswer();
		answer.setDescription(text);
		Validator validator = this.createValidator();
		Set<ConstraintViolation<Answer>> constraintViolations = validator.validate(answer);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(0);
	}

	//No debe validar si la description es mayor de 200, menor de 3, vacia
	@ParameterizedTest
	@ValueSource(strings = {
		"Esto es un texto, Esto es un texto, Esto es un texto, Esto es un texto, Esto es un texto, Esto es un texto, Esto es un texto, Esto es un texto, Esto es un texto, Esto es un texto, Esto es un texto, Esto",//
		"Es",//
		""
	})
	void shouldNotValidateDescription(final String text) {

		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Answer answer = this.generateAnswer();
		answer.setDescription(text);
		Validator validator = this.createValidator();
		Set<ConstraintViolation<Answer>> constraintViolations = validator.validate(answer);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Answer> violation = constraintViolations.iterator().next();
		Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("description");
		Assertions.assertThat(violation.getMessage()).isEqualTo("size must be between 3 and 200");
	}

	//VALIDACIONES NAME

	//No debe validar si name está vacío, más de 50, menos de 3
	@ParameterizedTest
	@ValueSource(strings = {
		"",//
		"Esto es un texto, Esto es un texto, Esto es un texto",//
		"Es"
	})

	void shouldNotValidateName(final String name) {

		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Answer answer = this.generateAnswer();
		answer.setName(name);
		Validator validator = this.createValidator();
		Set<ConstraintViolation<Answer>> constraintViolations = validator.validate(answer);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Answer> violation = constraintViolations.iterator().next();
		Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("name");
		Assertions.assertThat(violation.getMessage()).isEqualTo("size must be between 3 and 50");
	}

	//Debe validar si name es 3,50, dentro de rango, null
	@ParameterizedTest
	@NullSource
	@ValueSource(strings = {
		"Est",//
		"Esto es un texto, Esto es un texto, Esto es un tex",//
		"Esto es un texto, Esto"
	})
	void shouldValidateName(final String name) {

		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Answer answer = this.generateAnswer();
		answer.setName(name);
		Validator validator = this.createValidator();
		Set<ConstraintViolation<Answer>> constraintViolations = validator.validate(answer);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(0);
	}

}
