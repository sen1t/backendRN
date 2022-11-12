package br.com.rnschedule.schedule.controller;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityExistsException;
import javax.xml.bind.ValidationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.rnschedule.schedule.entities.User;
import br.com.rnschedule.schedule.entities.formDTO.FormLoginDTO;
import br.com.rnschedule.schedule.entities.formDTO.UserFormDTO;
import br.com.rnschedule.schedule.repository.UserRepository;
import br.com.rnschedule.schedule.service.JwtUserDetailsService;
import br.com.rnschedule.schedule.utils.JwtTokenUtil;

@RestController
@RequestMapping("/api/auth/")
public class AuthenticationController {
	protected final Log logger = LogFactory.getLog(getClass());

    final UserRepository userRepository;
    final AuthenticationManager authenticationManager;
    final JwtUserDetailsService userDetailsService;
    final JwtTokenUtil jwtTokenUtil;

    public AuthenticationController(UserRepository userRepository, AuthenticationManager authenticationManager,
                                    JwtUserDetailsService userDetailsService, JwtTokenUtil jwtTokenUtil) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody FormLoginDTO form) {
        Map<String, Object> responseMap = new HashMap<>();
        try {
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(form.getUsername()
                    , form.getPassword()));
            if (auth.isAuthenticated()) {
                logger.info("Logged In");
                UserDetails userDetails = userDetailsService.loadUserByUsername(form.getUsername());
                String token = jwtTokenUtil.generateToken(userDetails);
                responseMap.put("error", false);
                responseMap.put("message", "Logged In");
                responseMap.put("token", token);
                return ResponseEntity.ok(responseMap);
            } else {
                responseMap.put("error", true);
                responseMap.put("message", "Invalid Credentials");
                return ResponseEntity.status(401).body(responseMap);
            }
        } catch (DisabledException e) {
            e.printStackTrace();
            responseMap.put("error", true);
            responseMap.put("message", "User is disabled");
            return ResponseEntity.status(500).body(responseMap);
        } catch (BadCredentialsException e) {
            responseMap.put("error", true);
            responseMap.put("message", "Invalid Credentials");
            return ResponseEntity.status(401).body(responseMap);
        } catch (Exception e) {
            e.printStackTrace();
            responseMap.put("error", true);
            responseMap.put("message", " went wrong");
            return ResponseEntity.status(500).body(responseMap);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> saveUser(@RequestBody UserFormDTO userForm) {
    	Map<String, Object> responseMap = new HashMap<>();

    	try {
    		
    		if(userRepository.findByEmail(userForm.getEmail()).isPresent()) {
    			responseMap.put("error", true);
                responseMap.put("e-mail", userForm.getEmail());
                responseMap.put("message", "this adress e-mail already exists! ");
                throw new ValidationException("");
    		}else if(userRepository.findUserByUsername(userForm.getUsername()).isPresent()){
    				responseMap.put("error", true);
                    responseMap.put("username", userForm.getUsername());
                    responseMap.put("message", "this user already exists! ");
                    throw new EntityExistsException();
    		}
    		
    		
        	User user = new User();
            user.setUsername(userForm.getUsername());
            user.setPassword(new BCryptPasswordEncoder().encode(userForm.getPassword()));
            user.converteUser(userForm);
            userRepository.save(user);
            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
            String token = jwtTokenUtil.generateToken(userDetails);
            responseMap.put("error", false);
            responseMap.put("username", user.getUsername());
            responseMap.put("message", "Account created successfully");
            responseMap.put("token", token);
            return ResponseEntity.ok(responseMap);
       
        }catch (EntityExistsException e) {
			return ResponseEntity.badRequest().body(responseMap);
        }catch (ValidationException e) {
        	return ResponseEntity.badRequest().body(responseMap);
		}catch (Exception e) {
			responseMap.put("error", true);
			responseMap.put("message", "unknown error!!");
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(responseMap);
		}
    }
}
