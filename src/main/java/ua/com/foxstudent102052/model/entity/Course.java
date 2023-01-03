package ua.com.foxstudent102052.model.entity;

import lombok.Builder;

@Builder
public record Course(int courseId, String courseName, String courseDescription) {
}
