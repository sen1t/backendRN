package br.com.rnschedule.schedule.entities.dto;

import java.time.LocalDateTime;

import br.com.rnschedule.schedule.entities.Assignment;
import br.com.rnschedule.schedule.utils.DateUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssignmentDTO {
	 
	   private Integer assignmentId;
	   private String description;
	   private String dtAssignment;
	   private boolean isRecurrent;
	   
	   public AssignmentDTO(Integer assignmentId, String description, LocalDateTime dtAssignment, boolean isRecurrent ) {
		   this.assignmentId = assignmentId;
		   this.description  = description;
		   this.dtAssignment = DateUtils.formatterDate(dtAssignment)  ;
		   this.isRecurrent = isRecurrent;   
	   }

	public AssignmentDTO(Assignment assignment) {
		this.assignmentId = assignment.getAssignmentId();
		this.description  = assignment.getDescription();
		this.dtAssignment = DateUtils.formatterDate( assignment.getDtAssignment());
	}
	   
	   
	   
	
}
