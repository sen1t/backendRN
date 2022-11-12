package br.com.rnschedule.schedule.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.rnschedule.schedule.repository.UserRepository;

@RestController
@RequestMapping("/api/user/")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	
}
