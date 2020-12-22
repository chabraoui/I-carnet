package com.sid.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.sid.request.UserLoginRequest;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import com.sid.SpringApplicationContext;
import com.sid.services.UserServices;
import com.sid.shared.dto.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;

//filtre d'authentification
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	

	private final AuthenticationManager authenticationManager;

	public AuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	//pour qu'on puisse autentifié
	@Override
	public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
			throws AuthenticationException {
		try {
			
			//RECEPTION DU DATA(email,password)
			UserLoginRequest creds = new ObjectMapper().readValue(req.getInputStream(), UserLoginRequest.class);
			
			//passer data vers la methode authenticate
			return authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getPassword(), new ArrayList<>()));

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	//si authentifié avec succsée
	  @Override
	    protected void successfulAuthentication(HttpServletRequest req,
	                                            HttpServletResponse res,
	                                            FilterChain chain,
	                                            Authentication auth) throws IOException, ServletException {
	        //username=email dans notre cas
	        String userName = ((User) auth.getPrincipal()).getUsername(); 
	        
            UserServices userService = (UserServices)SpringApplicationContext.getBean("userImplementation");
	        
	        UserDto userDto = userService.getUser(userName);
	       
	        
	        String token = Jwts.builder()
	                .setSubject(userName)
	                .claim("id", userDto.getUserId())
	                .claim("name", userDto.getFirstName() + " " + userDto.getLastName())
	                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
	                .signWith(SignatureAlgorithm.HS512, SecurityConstants.TOKEN_SECRET )
	                .compact();
	        
	       
	        res.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token);
	        res.addHeader("user_id", userDto.getUserId());
	        
	        res.getWriter().write("{\"token\": \"" + token + "\", \"id\": \""+ userDto.getUserId() + "\" ,\"admin\": \""+userDto.getAdmin() + "\" }");

	    }  
	

}
