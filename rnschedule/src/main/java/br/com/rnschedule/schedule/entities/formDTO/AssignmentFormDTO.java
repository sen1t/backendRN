package br.com.rnschedule.schedule.entities.formDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssignmentFormDTO {
	
	private String description;
	private String dtAssignment;
	private String isRecurrent;
	
	public boolean isRecurrent() {
		  return Boolean.valueOf(isRecurrent);
	}
	
	
	
	
	

}
