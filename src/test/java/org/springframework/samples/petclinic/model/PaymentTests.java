
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

public class PaymentTests {

	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}

	@Test
	void shouldNotValidateWhenAmountNull() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Payment payment = new Payment();
		payment.setAmount(null);

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Payment>> constraintViolations = validator.validate(payment);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Payment> violation = constraintViolations.iterator().next();
		Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("amount");
	}

	@ParameterizedTest

	@CsvSource({
		"0.0", "-1.0", "10000.0"
	})
	void shouldNotValidateWhenAmountOutOfRange(final double outOfRange) {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Payment payment = new Payment();
		payment.setAmount(outOfRange);

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Payment>> constraintViolations = validator.validate(payment);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Payment> violation = constraintViolations.iterator().next();
		Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("amount");
	}

	@Test
	void shouldValidateWhenEverythingOk() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Payment payment = new Payment();
		payment.setAmount(20.5);
		payment.setDate(LocalDate.now());

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Payment>> constraintViolations = validator.validate(payment);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(0);
	}

	@ParameterizedTest

	@CsvSource({
		"2.0", "4999.5", "9998.0"
	})
	void shouldValidateWhenAmountInRange(final double inOfRange) {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Payment payment = new Payment();
		payment.setAmount(inOfRange);

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Payment>> constraintViolations = validator.validate(payment);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(0);
	}

	@Test
	void shouldValidateWhenDateNull() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Payment payment = new Payment();
		payment.setAmount(20.5);
		payment.setDate(null);

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Payment>> constraintViolations = validator.validate(payment);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(0);
	}
}
