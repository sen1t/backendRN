package br.com.rnschedule.schedule.controller;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.rnschedule.schedule.entities.Assignment;
import br.com.rnschedule.schedule.entities.User;
import br.com.rnschedule.schedule.entities.dto.AssignmentDTO;
import br.com.rnschedule.schedule.entities.formDTO.AssignmentFormDTO;
import br.com.rnschedule.schedule.entities.formDTO.FilterEnum;
import br.com.rnschedule.schedule.entities.formDTO.FilterForm;
import br.com.rnschedule.schedule.repository.AssignmentRepository;
import br.com.rnschedule.schedule.service.AssignmentService;
import br.com.rnschedule.schedule.utils.DateUtils;

@RestController
@RequestMapping("/api/tasks/")
public class AssignmentController {
	@Autowired
	private AssignmentService assignmentService;
	
	@Autowired
	private AssignmentRepository assignmentRepository;
	
	@PostMapping
	@RequestMapping("new")
	public ResponseEntity newAssignment(@RequestBody AssignmentFormDTO formDTO) {
		
		Map<String, Object> responseMap = new HashMap<>();
		
		try {
		   
		   LocalDateTime dtCompromissoFormatter = DateUtils.retornaLocalDateTime(formDTO.getDtAssignment());
		   
		   if(DateUtils.dateIsDifferentFromNow(dtCompromissoFormatter)) {
			   responseMap.put("error", true);
			   responseMap.put("Date Task", dtCompromissoFormatter);
			   responseMap.put("Date now", LocalDateTime.now());
			   responseMap.put("message", "Impossible to create a task dated earlier than current" );
			   return ResponseEntity.badRequest().body(responseMap);	
		   }
		   		
		   Assignment assignment = new Assignment();
		   assignment.setDescription(formDTO.getDescription());
		   assignment.setDtAssignment( dtCompromissoFormatter);
		   assignment.setUser(assignmentService.recoverLoggedUser());
		   assignment.setRecurrent(formDTO.isRecurrent());
		   assignmentRepository.saveAndFlush(assignment);
		   
		   responseMap.put("error", false);
		   responseMap.put("Task", assignment.getDescription());
		   responseMap.put("message", "task created successfully");
		   
		   return ResponseEntity.status(HttpStatus.CREATED).body(responseMap);
		   
		}catch (Exception e) {
			responseMap.put("error", true);
			responseMap.put("message", e.getMessage());
			return ResponseEntity.internalServerError().body(responseMap);	
		}	   
	}
	
	@GetMapping
	@RequestMapping("all")
	public ResponseEntity listAllAssignment() {
		   
		   Map<String, Object> responseMap = new HashMap<>();
		   
		   try {
			   
			   User user = assignmentService.recoverLoggedUser();
			   
			   Collection<AssignmentDTO> listTask = assignmentRepository.findAllByUser(user);
			   
			   responseMap.put("error", false);
			   responseMap.put("message", "Listing tasks for user " + user.getName());
			   
			   if(listTask.isEmpty()) 
				   responseMap.put("search result", "no tasks on the agenda");
			   else
				   responseMap.put("search result", listTask);
			   
			   return ResponseEntity.ok(responseMap);
			   
		   }catch (Exception e) {
			   responseMap.put("error", true);
			   responseMap.put("cause", e.getMessage());
			   responseMap.put("message", "Unknow Error");
			   return ResponseEntity.internalServerError().body(responseMap);
		  }
	}
	
	@GetMapping
	@RequestMapping("find/{findBy}/{filter}")
	public ResponseEntity findByFiltro(@PathVariable String findBy,@PathVariable String filter) {
        
		Map<String, Object> responseMap = new HashMap<>();
		try {
			
			if (findBy.equalsIgnoreCase("id")) {
				responseMap.put("error", false);
				responseMap.put("Search Result",  assignmentService.findById(Integer.valueOf(filter)));
				responseMap.put("message", "Search successfully");
			} else if (findBy.equalsIgnoreCase("description")) {
				responseMap.put("error", false);
				responseMap.put("Search Result",  assignmentService.findByDescription(filter));
				responseMap.put("message", "Search successfully");
			}else {
				responseMap.put("error", true);
				responseMap.put("Parameters", "findBy/" + findBy + "/filter" + filter);
				responseMap.put("message",
						"The accepted values ​​for the first argument are the filter type: id,description. and for the second argument the filter.");
			}
			
			return ResponseEntity.badRequest().body(responseMap);
			
		} catch (NoSuchElementException e) {
			responseMap.put("error", true);
			responseMap.put("TaskId", filter);
			responseMap.put("message", "the given task does not exist");
			return ResponseEntity.internalServerError().body(responseMap);
		} catch(NumberFormatException e) {
			responseMap.put("error", true);
			responseMap.put("message", "check the parameter provided!");
			return ResponseEntity.internalServerError().body(responseMap);
		} catch (Exception e) {
			
			responseMap.put("error", true);
			responseMap.put("cause", e.getMessage());
			responseMap.put("message", "Unknow Error");
			return ResponseEntity.internalServerError().body(responseMap);
		} 
	}
		
	@DeleteMapping
	@RequestMapping("delete/{id}")
	public ResponseEntity deleteTaskById(@PathVariable Integer id) {
		 Map<String, Object> responseMap = new HashMap<>();
		 
		 try {
			 Optional<Assignment> Assignment = assignmentRepository.findByIdAndUser(assignmentService.recoverLoggedUser(), id);
			 
			 Assignment assignment =  Assignment.get();
			 assignment.setActive(false);
			 assignment.setDtModification(LocalDateTime.now());
			 assignmentRepository.saveAndFlush(assignment);
			 responseMap.put("error", false);
			 responseMap.put("Task", new AssignmentDTO(assignment));
			 responseMap.put("message", "Task successfully deleted");
			 
			 
			return ResponseEntity.ok(responseMap);
			 
		 }catch (NoSuchElementException e) {
			 responseMap.put("error", true);
			 responseMap.put("TaskId", id);
			 responseMap.put("message", "the given task does not exist");
			 return ResponseEntity.internalServerError().body(responseMap);
		 }catch (Exception e) {
			 responseMap.put("error", true);
			 responseMap.put("cause", e.getMessage());
			 responseMap.put("message", "Unknow Error");
			 return ResponseEntity.internalServerError().body(responseMap);
		 } 	
	}	
	
	@PatchMapping
	@RequestMapping("update/{id}")
	public ResponseEntity updateTaskById(@PathVariable Integer id, @RequestBody AssignmentFormDTO formDTO) {
			 Map<String, Object> responseMap = new HashMap<>();
		 
			 try {
				 Optional<Assignment> Assignment = assignmentRepository.findByIdAndUser(assignmentService.recoverLoggedUser(), id);
				 LocalDateTime dtCompromissoFormatter = DateUtils.retornaLocalDateTime(formDTO.getDtAssignment());
				 
				 if(DateUtils.dateIsDifferentFromNow(dtCompromissoFormatter)) {
					   responseMap.put("error", true);
					   responseMap.put("Date Task", dtCompromissoFormatter);
					   responseMap.put("Date now", LocalDateTime.now());
					   responseMap.put("message", "unable to schedule a task to the past" );
					   return ResponseEntity.badRequest().body(responseMap);	
				  }
				 
				  Assignment assignment =  Assignment.get();
				  assignment.setDescription(formDTO.getDescription());
				  assignment.setDtAssignment(dtCompromissoFormatter);
				  assignment.setRecurrent(formDTO.isRecurrent());
				  assignment.setDtModification(LocalDateTime.now());
				  
				  assignmentRepository.saveAndFlush(assignment);
				  
				  responseMap.put("error", false);
				  responseMap.put("Task", new AssignmentDTO(assignment));
				  responseMap.put("message", "Task successfully update");
				    
				 return ResponseEntity.ok(responseMap);
				 
			 }catch (NoSuchElementException e) {
				 responseMap.put("error", true);
				 responseMap.put("TaskId", id);
				 responseMap.put("message", "the given task does not exist");
				 return ResponseEntity.internalServerError().body(responseMap);
			 }catch (Exception e) {
				 responseMap.put("error", true);
				 responseMap.put("cause", e.getMessage());
				 responseMap.put("message", "Unknow Error");
				 return ResponseEntity.internalServerError().body(responseMap);
			 } 	
		}
	
		
	   
}




















