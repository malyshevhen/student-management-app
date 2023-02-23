package ua.com.foxstudent102052.dao.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ua.com.foxstudent102052.model.entity.Group;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    Optional<Group> findByName(String groupName);

    @Query("select distinct g from Group g join g.students s where size(s) <= :count")
    List<Group> findByStudentsCount(@Param("count") Long numberOfStudents);

}
