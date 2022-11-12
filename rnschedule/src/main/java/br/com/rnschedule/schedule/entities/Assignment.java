package br.com.rnschedule.schedule.entities;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "tb_assignment")
@Getter
@Setter
@AllArgsConstructor
public class Assignment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer assignmentId;
	@Column(nullable = true, length = 255)
	private String description;
	private boolean isRecurrent;
	private LocalDateTime dtAssignment;
	private LocalDateTime dtModification;
	private LocalDateTime dtcreationTask;
	@ManyToOne
	@JsonIgnore
	private User user;
	private boolean isActive;
	
    public Assignment() {
    	this.dtcreationTask = LocalDateTime.now();
		this.isActive = true;
	}
	
	
	/* GETTER AND SETTER GERADOS AUTOMATICAMENTE */
	
	
}
