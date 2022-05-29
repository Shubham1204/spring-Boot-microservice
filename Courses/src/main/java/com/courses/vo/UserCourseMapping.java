package com.courses.vo;

import com.courses.entities.Course;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCourseMapping {

	private Users user;
	private Course course;
}
