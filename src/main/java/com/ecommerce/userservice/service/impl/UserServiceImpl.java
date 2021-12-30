package com.ecommerce.userservice.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.model.exceptions.UserNotFoundException;
import com.ecommerce.model.userdetail.UserDetail;
import com.ecommerce.mongo.dao.UserDetailRepository;
import com.ecommerce.userservice.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserDetailRepository userDetailRepository;
	
	
	@Override
	public Optional<UserDetail> getSingleUser(String userId) {
		Optional<UserDetail> userDetail = userDetailRepository.findByUserId(userId);
		
		return userDetail;
	}

	@Override
	public List<UserDetail> getAllUsers() {
		return userDetailRepository.findAll();
	}

	@Override
	public UserDetail removeUser(String userId) {
		Optional<UserDetail> userDetailOptional = getSingleUser(userId);
		if(!userDetailOptional.isPresent()) {
			throw new UserNotFoundException("User not found");
		}
		userDetailRepository.deleteById(userDetailOptional.get().getUserId());
		return userDetailOptional.get();
	}

	@Override
	public UserDetail updateUser(UserDetail userDetail) {
		Optional<UserDetail> existingUserDetail = getSingleUser(userDetail.getUserId());
		if(!existingUserDetail.isPresent()) {
			throw new UserNotFoundException("User not found");
		}
		userDetail.setId(existingUserDetail.get().getId());
		userDetail.setCreatedTs(existingUserDetail.get().getCreatedTs());
		UserDetail updatedUserDetail = userDetailRepository.save(userDetail);
		return updatedUserDetail;
	}
	
}
