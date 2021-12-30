package com.ecommerce.userservice.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.model.roledetail.RoleDetail;
import com.ecommerce.model.roledetail.UserRole;
import com.ecommerce.model.userdetail.UserDetail;
import com.ecommerce.mongo.dao.RoleDetailRepository;
import com.ecommerce.mongo.dao.UserDetailRepository;
import com.ecommerce.userservice.service.AuthService;
import com.ecommerce.userservice.service.UserService;

@Service
public class AuthServiceImpl implements AuthService {

	@Autowired
	private UserDetailRepository userDetailRepository;
	
	@Autowired
	private RoleDetailRepository roleDetailRepository;
	
	@Autowired
	private UserService userService;
	
	@Override
	public UserDetail saveUserDetail(UserDetail userDetail) {
		Optional<UserDetail> duplicateUserDetail = userService.getSingleUser(userDetail.getUserId());
		UserDetail createdUserDetail = null;
		if(!duplicateUserDetail.isPresent()) {
			createdUserDetail = userDetailRepository.save(userDetail);
		} else {
			createdUserDetail = duplicateUserDetail.get();
		}
		return createdUserDetail;
	}

	@Override
	public RoleDetail getRoleDetail(UserRole name) {
		return roleDetailRepository.findByName(name);
	}
	
	
}
