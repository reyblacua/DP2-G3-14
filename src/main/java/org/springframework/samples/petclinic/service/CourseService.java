
package org.springframework.samples.petclinic.service;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Course;
import org.springframework.samples.petclinic.repository.CourseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CourseService {


	private CourseRepository courseRepository;


	@Autowired
	public CourseService(final CourseRepository courseRepository) {
		this.courseRepository = courseRepository;
	}

	@Transactional
	public Iterable<Course> findAll(){
		Iterable<Course> res = this.courseRepository.findAll();
		if (StreamSupport.stream(res.spliterator(), false).count() == 0) {
			throw new NoSuchElementException();
		}
		return res;
	}

	public Optional<Course> findCourseById(final int courseId) throws NoSuchElementException{
		Optional<Course> res = this.courseRepository.findById(courseId);
		res.get();
		return res;
	}

}
