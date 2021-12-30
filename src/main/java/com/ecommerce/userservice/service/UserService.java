package com.ecommerce.userservice.service;

import java.util.List;
import java.util.Optional;

import com.ecommerce.model.userdetail.UserDetail;

public interface UserService {

	public Optional<UserDetail> getSingleUser(String userId);
	public List<UserDetail> getAllUsers();
	public UserDetail removeUser(String userId);
	public UserDetail updateUser(UserDetail userDetail);
}
