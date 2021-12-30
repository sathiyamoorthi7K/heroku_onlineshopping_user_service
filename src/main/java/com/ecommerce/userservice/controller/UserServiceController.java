package com.ecommerce.userservice.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.model.exceptions.UserNotFoundException;
import com.ecommerce.model.userdetail.UserDetail;
import com.ecommerce.userservice.service.UserService;

@RestController
@RequestMapping("/v1/users")
public class UserServiceController {
	
	private Logger LOGGER = LoggerFactory.getLogger(UserServiceController.class);
	
	@Autowired
	UserService userService;
	
	@PutMapping(consumes=MediaType.APPLICATION_JSON_VALUE,
			 produces= {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	public EntityModel<UserDetail> updateUserDetail(@Valid @RequestBody UserDetail userDetail) {
		
		UserDetail updatedUser = userService.updateUser(userDetail);
		
		EntityModel<UserDetail> model = EntityModel.of(updatedUser);
		WebMvcLinkBuilder linkToUsers = linkTo(methodOn(this.getClass()).getAllUser());
		model.add(linkToUsers.withRel("all-users"));
		
		return model;
	}
	
	@GetMapping(path="/{userId}",
				produces= {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	public EntityModel<UserDetail> getSingleUser(@Valid @PathVariable String userId) {
		LOGGER.info("inside getSingleUser");
		Optional<UserDetail> userDetail = userService.getSingleUser(userId);
		EntityModel<UserDetail> model = null;
		if(userDetail.isPresent()) {
			model = EntityModel.of(userDetail.get());
		} else {
			throw new UserNotFoundException("User not found");
		}
		
		WebMvcLinkBuilder linkToUsers = linkTo(methodOn(this.getClass()).getAllUser());
		model.add(linkToUsers.withRel("all-users"));
		
		return model;
	}
	
	
	@DeleteMapping(path="/{userId}",
					produces= {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<Object> removeUser(@PathVariable String userId) {
		UserDetail userDetail = userService.removeUser(userId);
		if(userDetail == null) {
			return ResponseEntity.status(HttpStatus.OK).body("User "+userId+" not deleted ");
		} else {
			return ResponseEntity.status(HttpStatus.OK).body("User "+userDetail.getUserId()+" sucessfully deleted ");
		}
	}
	
	@GetMapping(path="/all",
				produces= {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	public List<UserDetail> getAllUser() {
		return userService.getAllUsers();
	}
}
