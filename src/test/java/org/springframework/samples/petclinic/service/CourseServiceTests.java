
package org.springframework.samples.petclinic.service;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Course;
import org.springframework.samples.petclinic.repository.CourseRepository;
import org.springframework.samples.petclinic.util.CourseAssert;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace=Replace.NONE)
public class CourseServiceTests {

	@Autowired
	protected CourseService courseService;


	@Test
	void shouldFindCourses() {
		Iterable<Course> courses = this.courseService.findAll();
		org.assertj.core.api.Assertions.assertThat(courses).hasSize(5);
	}

	@Test
	void shouldNotFindCourses() {
		CourseRepository courseRepository = Mockito.mock(CourseRepository.class);
		this.courseService = new CourseService(courseRepository);
		Mockito.when(courseRepository.findAll()).thenReturn(new ArrayList<Course>());
		Assertions.assertThrows(NoSuchElementException.class, () -> {
			this.courseService.findAll();
		});
	}

	@Test
	void shouldFindCourseById() {
		Optional<Course> course = this.courseService.findCourseById(1);
		org.assertj.core.api.Assertions.assertThat(course).isPresent();

		//Assert personalizado
		CourseAssert.assertThat(course.get()).hasName("Curso para gatos");
	}

	@Test
	void shouldNotFindCourseWithIncorrectId() {
		Assertions.assertThrows(NoSuchElementException.class, () -> {
			this.courseService.findCourseById(200);
		});
	}

}
