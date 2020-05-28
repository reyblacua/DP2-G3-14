package org.springframework.samples.petclinic.util;

import java.util.Objects;

import org.assertj.core.api.AbstractAssert;
import org.springframework.samples.petclinic.model.Course;


public class CourseAssert extends AbstractAssert<CourseAssert, Course> {

	public CourseAssert(final Course actual) {
		super(actual, CourseAssert.class);
	}

	public static CourseAssert assertThat(final Course actual) {
		return new CourseAssert(actual);
	}

	public CourseAssert hasName(final String name) {
		this.isNotNull();

		if (!Objects.equals(this.actual.getName(), name)) {
			this.failWithMessage("Expected course's name to be <%s> but was <%s>", name, this.actual.getName());
		}

		return this;
	}


}
