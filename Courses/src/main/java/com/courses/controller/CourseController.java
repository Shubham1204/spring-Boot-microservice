package com.courses.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.courses.entities.Course;
import com.courses.service.CourseService;

@RestController
@RequestMapping("/courses")
public class CourseController {
	
	@Autowired
	private CourseService courseService;
	
	@GetMapping("/getAll")
	public ResponseEntity<?> getAllCourses(){
		List<Course> allCourses = courseService.getAllCourses();
		return ResponseEntity.ok(allCourses);
	}
	
	@GetMapping("/get/{courseName}")
	public ResponseEntity<?> getByCourseName(@PathVariable("courseName") String courseName){
		Course byCourseName = courseService.getByCourseName(courseName);
		return ResponseEntity.ok(byCourseName);
	}
	
	@PostMapping("/add")
	public ResponseEntity<?> addCourse(@RequestBody Course course){
		System.out.println(course);
		Course saveCourse = courseService.saveCourse(course);
		return ResponseEntity.status(HttpStatus.CREATED).body(saveCourse);
	}
	
	@PutMapping("/update")
	public ResponseEntity<?> updateCourse(@RequestBody Course course){
		Course updateCourse = courseService.updateCourse(course);
		return ResponseEntity.status(HttpStatus.OK).body(updateCourse);
	}
	@DeleteMapping("/remove")
	public ResponseEntity<?> removeCourse(@RequestParam String courseName){
		Course removeCourse = courseService.removeCourse(courseName);
		return ResponseEntity.status(HttpStatus.OK).body(removeCourse);
	}
}
