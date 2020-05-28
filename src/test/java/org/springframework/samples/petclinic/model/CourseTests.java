
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
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

public class CourseTests {

	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}

	@ParameterizedTest
	@CsvSource({
		"1,1,false,10", "2,2,true,20"
	})
	void shouldValidateWhenEverythingOk(final int minusMonth, final int plusMonth, final boolean dangerousAllowed, final int capacity) {

		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Course course = new Course();
		course.setStartDate(LocalDate.now().minusMonths(minusMonth));
		course.setStartDate(LocalDate.now().plusMonths(plusMonth));
		course.setPetType(new PetType());
		course.setDangerousAllowed(dangerousAllowed);
		course.setCapacity(capacity);
		course.setTrainer(new Trainer());

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Course>> constraintViolations = validator.validate(course);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(0);
	}

	@Test
	void shouldValidateWhenEverythingNull() {

		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Course course = new Course();
		course.setStartDate(null);
		course.setStartDate(null);
		course.setPetType(null);
		course.setDangerousAllowed(null);
		course.setCapacity(null);
		course.setTrainer(null);

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Course>> constraintViolations = validator.validate(course);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(0);
	}

}
