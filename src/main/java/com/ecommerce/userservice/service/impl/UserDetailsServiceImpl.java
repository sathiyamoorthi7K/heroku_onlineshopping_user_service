package com.ecommerce.userservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ecommerce.model.userdetail.UserDetail;
import com.ecommerce.mongo.dao.UserDetailRepository;
import com.ecommerce.userservice.model.UserDetailsImpl;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	UserDetailRepository userDetailReposity;

	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		
		UserDetail userDetail = userDetailReposity.findByUserId(userId).orElseThrow(() -> new UsernameNotFoundException("User not found with name : "+ userId));
		return UserDetailsImpl.build(userDetail);
	}

}
