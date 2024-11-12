package admin_user.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import admin_user.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

	@Autowired
	CustomUserDetailsService customUserDetailsService;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		// TODO Auto-generated method stub
		
		 System.out.println("111111 0");
		var authourities = authentication.getAuthorities();
		var roles = authourities.stream().map(r -> r.getAuthority()).findFirst();
		
		 // Get the principal object (user) to access the status
	    Object principal = authentication.getPrincipal();
	    System.out.println("111111 ");
	    // Assuming the principal is of type UserDetails (you can adjust this based on your implementation)
	    if (principal instanceof UserDetails) {
	    	// Cast principal to UserDetails and get the username
            String username = ((UserDetails) principal).getUsername();
            User user = (User) customUserDetailsService.loadUserByUsername(username);
	        
	        System.out.println("222222");
	        // Assuming the status is part of your UserDetails (modify this depending on your UserDetails implementation)
	        boolean status = user.isStatus(); // Implement this method
	        
	        System.out.println("User status: " + status);
	       
	        // Check if the status is true before proceeding with further logic
	        if (!status) {
	            response.sendRedirect("/account-disabled"); // Redirect to a page showing the account is disabled
	            return;
	        }
	    }

		
		if (roles.orElse("").equals("ADMIN")) {
			response.sendRedirect("/admin-page");
		} else if (roles.orElse("").equals("USER")) {
			response.sendRedirect("/user-page");
		} else {
			response.sendRedirect("/error");
		}

	}

}
