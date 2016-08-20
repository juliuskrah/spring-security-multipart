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

import java.util.Optional;
import java.util.stream.Stream;

import javax.inject.Inject;
import javax.inject.Provider;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.juliuskrah.multipart.entity.Account;
import com.juliuskrah.multipart.repository.AccountRepository;
import com.juliuskrah.multipart.service.AccountService;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

	@Inject
	private Provider<AccountRepository> accountRepositoryProvider;

	@Override
	public Optional<Account> findAccountById(Long id) {
		return accountRepositoryProvider.get().get(id);
	}

	@Override
	public Account saveAccount(Account account) {
		return accountRepositoryProvider.get().save(account);
	}

	@Override
	public void deleteAccount(Account account) {
		accountRepositoryProvider.get().delete(account);
	}

	@Override
	public Optional<Account> findAccountByUsername(String username) {
		return accountRepositoryProvider.get().findByUsername(username);
	}

	@Override
	public Stream<Account> findAccountByLastName(String lastName) {
		return accountRepositoryProvider.get().findByLastName(lastName);
	}

}
