package com.ecommerce.userservice.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ecommerce.model.roledetail.RoleDetail;
import com.ecommerce.model.roledetail.UserRole;
import com.ecommerce.model.userdetail.UserDetail;
import com.ecommerce.userservice.model.LoginRequest;
import com.ecommerce.userservice.model.LoginResponse;
import com.ecommerce.userservice.model.UserDetailsImpl;
import com.ecommerce.userservice.security.utils.JwtUtils;
import com.ecommerce.userservice.service.AuthService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthServiceController {

	private Logger LOGGER = LoggerFactory.getLogger(AuthServiceController.class);

	@Autowired
	private AuthService authService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtUtils jwtUtils;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@PostMapping(value="/signup", consumes=MediaType.APPLICATION_JSON_VALUE,
			produces= {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<Object> registerUser(@Valid @RequestBody UserDetail userDetail) {
		LOGGER.info("inside signup");

		List<RoleDetail> roles = new ArrayList<RoleDetail>();
		if(userDetail.getRoleDetails() == null || userDetail.getRoleDetails().isEmpty()) {

			roles.add(authService.getRoleDetail(UserRole.ROLE_USER));
			
		} else {
			userDetail.getRoleDetails().stream().forEach((role) -> {
				if(UserRole.ROLE_ADMIN.equals(role)) {
					roles.add(authService.getRoleDetail(UserRole.ROLE_ADMIN));
				} else if(UserRole.ROLE_OPERATOR.equals(role)) {
					roles.add(authService.getRoleDetail(UserRole.ROLE_OPERATOR));
				} else if(UserRole.ROLE_USER.equals(role)) {
					roles.add(authService.getRoleDetail(UserRole.ROLE_USER));
				}
			}
					);
		}
		userDetail.setRoleDetails(roles);
		userDetail.setPassword(passwordEncoder.encode(userDetail.getPassword()));
		UserDetail createdUser = authService.saveUserDetail(userDetail);

		URI uri = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(createdUser.getUserId())
				.toUri();

		return ResponseEntity.created(uri).build();

	}
	
	
	@PostMapping(path="/signin")
	public ResponseEntity<Object> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), 
						loginRequest.getPassword()));
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		String jwt = jwtUtils.generateJwtToken(authentication);
		
		LOGGER.info("Generated token : "+ jwt);
		
		UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authentication.getPrincipal();
		
		List<String> roles = userDetailsImpl.getAuthorities().stream().map(item ->  item.getAuthority()).collect(Collectors.toList());
		
		return ResponseEntity.ok(new LoginResponse(jwt, userDetailsImpl.getUsername(), roles));
	}
}
