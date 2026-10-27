package in.ameya.securities.service;

import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class MyUserDetailsService implements UserDetailsService {

	
	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
	
		if(userId != null)
		{    		
    		String bcryptPassword = "eJM9hcFdY3xnngKAG8zGSg=="; 
			User user = new User(userId, "{bcrypt}"+ bcryptPassword, new ArrayList<>());
			return user;	
		}
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid User ID Provided.");
		
	}
}