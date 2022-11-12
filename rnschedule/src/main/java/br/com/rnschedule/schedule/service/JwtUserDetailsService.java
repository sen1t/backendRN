package br.com.rnschedule.schedule.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.rnschedule.schedule.repository.UserRepository;

@Service
public class JwtUserDetailsService implements UserDetailsService {
	
	final UserRepository userRepository;
	
    public JwtUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	br.com.rnschedule.schedule.entities.User user = null;
    	Optional<br.com.rnschedule.schedule.entities.User> userOptional =  userRepository.findUserByUsername(username);
    	
    	
    	if(userOptional.isPresent())
    		 user = userOptional.get();
    	else
    		throw new UsernameNotFoundException(username);
        
        List<GrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority("USER_ROLE"));
        return new User(user.getUsername() , user.getPassword(), authorityList);
    }
}
