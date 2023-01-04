package ua.com.foxstudent102052.model.entity;

import lombok.Builder;

@Builder
public record Group(int groupId, String groupName) {
}
