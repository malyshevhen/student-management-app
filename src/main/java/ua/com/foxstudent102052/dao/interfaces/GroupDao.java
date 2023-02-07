package ua.com.foxstudent102052.dao.interfaces;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.com.foxstudent102052.model.entity.Group;

public interface GroupDao extends JpaRepository<Group, Integer> {

    Optional<Group> findByName(String groupName);

}
