package br.com.rnschedule.schedule.repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.rnschedule.schedule.entities.Assignment;
import br.com.rnschedule.schedule.entities.User;
import br.com.rnschedule.schedule.entities.dto.AssignmentDTO;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Integer> {
	
	@Query(value =  "SELECT NEW br.com.rnschedule.schedule.entities.dto.AssignmentDTO(assign.assignmentId, assign.description, assign.dtAssignment, assign.isRecurrent) "
					+ "from br.com.rnschedule.schedule.entities.Assignment assign "
					+ "where assign.user =:user"
					+ " and assign.isActive = true")
	Collection<AssignmentDTO> findAllByUser(@Param("user") User user);
	
	@Query(value =  "SELECT NEW br.com.rnschedule.schedule.entities.dto.AssignmentDTO(assign.assignmentId, assign.description, assign.dtAssignment, assign.isRecurrent) "
			+ "from br.com.rnschedule.schedule.entities.Assignment assign "
			+ "where assign.user =:user"
			+ " and assign.description like %:filter%"
			+ " and assign.isActive = true")
	Collection<AssignmentDTO> findByDescription(@Param("user")User user ,@Param("filter") String filter);
	
	@Query(value =  "SELECT assign "
			+ "from br.com.rnschedule.schedule.entities.Assignment assign "
			+ "where assign.user =:user"
			+ " and assign.assignmentId =:id"
			+ " and assign.isActive = true")
	Optional<Assignment> findByIdAndUser(@Param("user") User user, @Param("id") Integer id);
	
	
	@Query(value =  "SELECT NEW br.com.rnschedule.schedule.entities.dto.AssignmentDTO(assign.assignmentId, assign.description, assign.dtAssignment, assign.isRecurrent) "
			+ "from br.com.rnschedule.schedule.entities.Assignment assign "
			+ "where assign.user =:user"
			+ " and assign.assignmentId =:id"
			+ " and assign.isActive = true")
	Optional<AssignmentDTO> findByFilter(@Param("user") User user,@Param("id") Integer id);
	
	
	
	@Query(value = "SELECT * FROM"
			+ "     tb_assignment a where"
			+ "     DATEDIFF(a.dt_assignment , LocalTime()) >= -5 AND "
			+ "     DATEDIFF(a.dt_assignment , LocalTime()) < 2 AND"
			+ "     a.is_active = 1 and "
			+ "     a.user_username =:user", nativeQuery = true)
	Collection<Assignment> findAllTaskAsyncMethod(@Param("user") String user);
	
}
