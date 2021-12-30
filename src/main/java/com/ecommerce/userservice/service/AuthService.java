package com.ecommerce.userservice.service;

import com.ecommerce.model.roledetail.RoleDetail;
import com.ecommerce.model.roledetail.UserRole;
import com.ecommerce.model.userdetail.UserDetail;

public interface AuthService {
	
	public UserDetail saveUserDetail(UserDetail userDetail);
	public RoleDetail getRoleDetail(UserRole name);
}
