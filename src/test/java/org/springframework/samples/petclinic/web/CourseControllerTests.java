package org.springframework.samples.petclinic.web;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Course;
import org.springframework.samples.petclinic.service.CourseService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = CourseController.class,
excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
classes = WebSecurityConfigurer.class),
excludeAutoConfiguration = SecurityConfiguration.class)
class CourseControllerTests {

	private static final int TEST_COURSE_ID = 1;

	@MockBean
	private CourseService	courseService;

	@Autowired
	private MockMvc mockMvc;

	public Course createDummyCourse(final String name) {
		Course course = new Course();
		course.setName(name);
		return course;
	}

	//	@BeforeEach
	//	void setup() {
	//
	//	}


	@WithMockUser(value = "spring")
	@Test
	void shouldShowCourses() throws Exception {
		Course course1 = this.createDummyCourse("Course1");
		Course course2 = this.createDummyCourse("Course2");

		Mockito.when(this.courseService.findAll()).thenReturn(Stream.of(course1,course2).collect(Collectors.toList()));

		this.mockMvc.perform(MockMvcRequestBuilders.get("/courses"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attributeExists("courses"))
		.andExpect(MockMvcResultMatchers.view().name("courses/coursesList"));
	}

	@WithMockUser(value = "spring")
	@Test
	void shouldNotShowCourses() throws Exception {
		Mockito.when(this.courseService.findAll()).thenThrow(NoSuchElementException.class);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/courses"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attributeExists("isempty"))
		.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("courses"))
		.andExpect(MockMvcResultMatchers.view().name("courses/coursesList"));
	}

	@WithMockUser(value = "spring")
	@Test
	void shouldShowCourseDetails() throws Exception {
		Course course1 = this.createDummyCourse("Course1");
		Mockito.when(this.courseService.findCourseById(CourseControllerTests.TEST_COURSE_ID)).thenReturn(Optional.of(course1));

		this.mockMvc.perform(MockMvcRequestBuilders.get("/courses/{courseId}",CourseControllerTests.TEST_COURSE_ID))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attributeExists("course"))
		.andExpect(MockMvcResultMatchers.view().name("courses/courseDetails"));
	}

	@WithMockUser(value = "spring")
	@Test
	void shouldNotShowCourseDetails() throws Exception {
		Mockito.when(this.courseService.findCourseById(CourseControllerTests.TEST_COURSE_ID)).thenThrow(NoSuchElementException.class);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/courses/{courseId}",CourseControllerTests.TEST_COURSE_ID))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attributeExists("message"))
		.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("course"))
		.andExpect(MockMvcResultMatchers.view().name("exception"));
	}
}
