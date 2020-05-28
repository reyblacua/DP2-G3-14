
package org.springframework.samples.petclinic.web;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Course;
import org.springframework.samples.petclinic.service.CourseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/courses")
public class CourseController {

	@Autowired
	private CourseService courseService;


	@GetMapping()
	public String mostrarCourses(final ModelMap modelMap) {
		String vista = "courses/coursesList";
		boolean isempty=false;
		try {
			Iterable<Course> courses = this.courseService.findAll();
			modelMap.addAttribute("courses", courses);
		}catch(NoSuchElementException e) {
			isempty=true;
			modelMap.addAttribute("isempty",isempty);
		}

		return vista;
	}

	@GetMapping("/{courseId}")
	public String mostrarCourse(final ModelMap modelMap, @PathVariable("courseId") final int courseId) {
		Course course = null;

		try {
			course = this.courseService.findCourseById(courseId).get();
			String vista = "courses/courseDetails";
			modelMap.addAttribute("course", course);
			return vista;
		} catch (NoSuchElementException e) {
			modelMap.addAttribute("message", "Course not found");
			return "exception";
		}

	}

}
