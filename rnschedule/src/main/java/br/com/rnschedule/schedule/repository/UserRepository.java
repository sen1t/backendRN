package br.com.rnschedule.schedule.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.rnschedule.schedule.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

	Optional<User> findUserByUsername(String username);
	
	@Query(value = "select u.email from tb_user u where u.email =:email", nativeQuery = true)
	Optional<String> findByEmail(@Param("email") String email);
}
