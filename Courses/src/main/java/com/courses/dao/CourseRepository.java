package com.courses.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.courses.entities.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {

	Course findByCourseName(String courseName);

}
