/*
* Copyright 2016, Julius Krah
* by the @authors tag. See the LICENCE in the distribution for a
* full listing of individual contributors.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
* http://www.apache.org/licenses/LICENSE-2.0
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package com.juliuskrah.multipart.service.impl;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.juliuskrah.multipart.entity.Account;
import com.juliuskrah.multipart.repository.AccountRepository;
import com.juliuskrah.multipart.security.exception.UserNotActivatedException;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Inject
	private AccountRepository accountRepository;

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.debug("Starting authentication for {}...", username);
		String lowercaseUsername = username.toLowerCase(Locale.ENGLISH);
		Optional<Account> userFromDb = accountRepository.findByUsername(lowercaseUsername);
		// @formatter:off
		return userFromDb.map(user -> {
			if(!user.isActivated())
				throw new UserNotActivatedException(String.format(
						"User %s is not activated", lowercaseUsername));
			List<GrantedAuthority> grantedAuthorities = user.getAuthorities()
					.stream().map(authority -> new SimpleGrantedAuthority(authority.getName()))
					.collect(Collectors.toList());
			return new User(lowercaseUsername, 
					user.getPassword(), 
					grantedAuthorities);
		}).orElseThrow(() -> new UsernameNotFoundException(String.format(
				"User %s was not found in the database", lowercaseUsername)));
		// @formatter:on
	}

}
