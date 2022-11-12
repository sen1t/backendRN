package br.com.rnschedule.schedule.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;

import br.com.rnschedule.schedule.entities.formDTO.UserFormDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "tb_user")
@Getter
@Setter
@AllArgsConstructor
public class User {
	
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private Long id;
	@Id
	@Column(unique = true, nullable = false)
	private String username;
	@Column(nullable = false, length = 100)
	private String name;
	@Column(nullable = false, length = 50)
	private String email;
	@Column(nullable = false)
	private String password;
	@Column(nullable = false)
	private String role;
	@Temporal(javax.persistence.TemporalType.DATE)
	private Date dtRegistration = new Date();
	@OneToMany(cascade = CascadeType.REMOVE,mappedBy = "user")
	private List<Assignment> listAssignment;
	private boolean isActive = true;
	
	
	public User converteUser(UserFormDTO userFormDTO) {
		this.name = userFormDTO.getName();
		this.email = userFormDTO.getEmail();
		this.role = "USER";
		return this;		
	}


	public User() {
	
	}
	
	
	/* Getter and Setters*/
	
}
