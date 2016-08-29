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
package com.juliuskrah.multipart.service;

import java.util.Optional;
import java.util.stream.Stream;

import com.juliuskrah.multipart.entity.Account;
import com.juliuskrah.multipart.entity.Authority;

public interface AccountService {

	public Optional<Account> findAccountById(Long id);

	public Account saveAccount(Account account);

	public void deleteAccount(Account account);

	public Optional<Account> findAccountByUsername(String username);

	public Stream<Account> findAccountByLastName(String lastName);

	public Optional<Authority> findAuthorityById(String id);

	public Authority saveAuthroity(Authority authority);

	public void deleteAuthority(Authority authority);
}
