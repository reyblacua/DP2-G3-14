package org.springframework.samples.petclinic.util;

import java.util.Objects;

import org.assertj.core.api.AbstractAssert;
import org.springframework.samples.petclinic.model.Inscription;


public class InscriptionAssert extends AbstractAssert<InscriptionAssert, Inscription> {

	public InscriptionAssert(final Inscription actual) {
		super(actual, InscriptionAssert.class);
	}

	public static InscriptionAssert assertThat(final Inscription actual) {
		return new InscriptionAssert(actual);
	}

	public InscriptionAssert hasName(final String name) {
		this.isNotNull();

		if (!Objects.equals(this.actual.getName(), name)) {
			this.failWithMessage("Expected inscription's name to be <%s> but was <%s>", name, this.actual.getName());
		}

		return this;
	}

	public InscriptionAssert idNotNull() {
		this.isNotNull();

		if (this.actual.getId()==null) {
			this.failWithMessage("Inscription id is null");
		}
		return this;
	}


}
