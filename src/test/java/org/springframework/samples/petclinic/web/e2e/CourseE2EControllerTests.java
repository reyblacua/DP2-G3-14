
package org.springframework.samples.petclinic.web.e2e;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
public class CourseE2EControllerTests {

	@Autowired
	private MockMvc mockMvc;


	// Listing courses

	@WithMockUser(username = "owner1", password = "0wn3r")
	@Test
	void shouldShowCourses() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/courses")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("courses")).andExpect(MockMvcResultMatchers.view().name("courses/coursesList"));
	}

	// Showing courses

	@WithMockUser(username = "owner1", password = "0wn3r")
	@Test
	void shouldShowCourseDetails() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/courses/{courseId}", 1)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("course"))
		.andExpect(MockMvcResultMatchers.view().name("courses/courseDetails"));
	}

	@WithMockUser(username = "owner1", password = "0wn3r")
	@Test
	void shouldNotShowCourseWhenNotFound() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/courses/{courseId}", 20)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attribute("message", "Course not found"))
		.andExpect(MockMvcResultMatchers.view().name("exception"));
	}
}
