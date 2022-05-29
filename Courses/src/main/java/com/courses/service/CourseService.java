package com.courses.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.courses.dao.CourseRepository;
import com.courses.entities.Course;

@Service
public class CourseService {

	@Autowired
	private CourseRepository courseRepository;

	public List<Course> getAllCourses() {
		List<Course> findAll = courseRepository.findAll();
		return findAll;
	}

	public Course getByCourseName(String courseName) {
		Course findByCourseName = courseRepository.findByCourseName(courseName);
		return findByCourseName;
	}
	
	public Course saveCourse(Course course) {
		Course savedCourse = courseRepository.save(course);
		return savedCourse;
	}
	
	public Course updateCourse(Course course) {
		Course findByCourseName = courseRepository.findByCourseName(course.getCourseName());
		course.setCourseId(findByCourseName.getCourseId());
		Course updatedcourse = courseRepository.save(course);
		return updatedcourse;
	}
	
	public Course removeCourse(String courseName) {
		Course findByCourseName = courseRepository.findByCourseName(courseName);
		findByCourseName.setCourseStatus('N');
		Course course = courseRepository.save(findByCourseName);
		return course;
	}
}