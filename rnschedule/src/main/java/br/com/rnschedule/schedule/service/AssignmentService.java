package br.com.rnschedule.schedule.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import br.com.rnschedule.schedule.entities.User;
import br.com.rnschedule.schedule.entities.dto.AssignmentDTO;
import br.com.rnschedule.schedule.repository.AssignmentRepository;
import br.com.rnschedule.schedule.repository.UserRepository;

@Service
@Component
public class AssignmentService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AssignmentRepository assignmentRepository;
	

	public User recoverLoggedUser() {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		return userRepository.findUserByUsername(authentication.getName()).get();
		
	}


	public AssignmentDTO findById(Integer id) {
		return assignmentRepository.findByFilter(recoverLoggedUser(), id).get();
	}


	public Collection<AssignmentDTO> findByDescription(String filter) {
		return assignmentRepository.findByDescription(recoverLoggedUser(),filter);
	}
	
	
}
