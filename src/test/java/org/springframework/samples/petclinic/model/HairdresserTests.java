
package org.springframework.samples.petclinic.model;

import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

public class HairdresserTests {

	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}

	@Test
	void shouldNotValidateWhenSpecialtiesNull() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Hairdresser hairdresser = new Hairdresser();
		hairdresser.setFirstName("Manolo");
		hairdresser.setLastName("Cornac");
		hairdresser.setSpecialties(null);
		hairdresser.setActive(false);

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Hairdresser>> constraintViolations = validator.validate(hairdresser);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Hairdresser> violation = constraintViolations.iterator().next();
		Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("specialties");
	}

	@Test
	void shouldNotValidateWhenActiveNull() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Hairdresser hairdresser = new Hairdresser();
		hairdresser.setFirstName("Manuel");
		hairdresser.setLastName("Cardenal");
		hairdresser.setSpecialties(HairdresserSpecialty.OTTERS);
		hairdresser.setActive(null);

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Hairdresser>> constraintViolations = validator.validate(hairdresser);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Hairdresser> violation = constraintViolations.iterator().next();
		Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("active");
	}

	@Test
	void shouldValidateWhenEverythingOk() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Hairdresser hairdresser = new Hairdresser();
		hairdresser.setFirstName("Jesús");
		hairdresser.setLastName("Rodríguez");
		hairdresser.setSpecialties(HairdresserSpecialty.OTTERS);
		hairdresser.setActive(true);

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Hairdresser>> constraintViolations = validator.validate(hairdresser);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(0);
	}

	@ParameterizedTest

	@ValueSource(booleans = {
		true, false
	})
	void shouldValidateWhenActiveOk(final boolean argument) {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Hairdresser hairdresser = new Hairdresser();
		hairdresser.setFirstName("Manuela");
		hairdresser.setLastName("Pradas");
		hairdresser.setSpecialties(HairdresserSpecialty.OTTERS);
		hairdresser.setActive(argument);

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Hairdresser>> constraintViolations = validator.validate(hairdresser);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(0);
	}

}
