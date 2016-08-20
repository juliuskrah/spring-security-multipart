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
package com.juliuskrah.multipart.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Inject;
import javax.persistence.NoResultException;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.juliuskrah.multipart.ApplicationTest;
import com.juliuskrah.multipart.entity.Account;
import com.juliuskrah.multipart.entity.Authority;

public class AccountRepositoryTest extends ApplicationTest {

	@Inject
	private AccountRepository accountRepository;
	@Inject
	private PasswordEncoder encoder;

	@Before
	public void before() {
		Account a = new Account();
		a.setFirstName("Administrator");
		a.setLastName("Administrator");
		a.setUsername("admin");
		a.setAuthorities(Stream.of(new Authority("ROLE_USER")).collect(Collectors.toSet()));
		a.setPassword(encoder.encode("system"));

		accountRepository.save(a);
	}

	@Test
	public void testGet() {
		Account a = accountRepository.get(3L).get();
		assertThat("The entity is null", a, notNullValue());
		assertThat(a.getFirstName(), is("Bridget"));
		assertThat(a.getAuthorities().size(), is(1));
	}

	@Test
	public void testFindByUsername() {
		Account a = accountRepository.findByUsername("admin").get();
		assertThat("The entity is null", a, notNullValue());
		assertThat(a.getLastName(), is("Administrator"));
		assertThat(a.getAuthorities().stream().findFirst().get().getName(), is("ROLE_USER"));
	}

	@Test
	public void testFindByLastName() {
		Stream<Account> accountStream = accountRepository.findByLastName("Krah");
		assertThat(accountStream, notNullValue());
		assertThat(accountStream.findFirst().get().getUsername(), is("julius"));
	}

	@Test
	public void testSave() {
		Account a = new Account();
		a.setFirstName("User");
		a.setLastName("User");
		a.setUsername("user");
		a.setPassword(encoder.encode("user"));

		a = accountRepository.save(a);

		assertThat(a.getId(), is(1003L));
	}

	@Test(expected = NoResultException.class)
	public void testDelete() {
		Account a = accountRepository.findByUsername("julius").get();
		accountRepository.delete(a);
		accountRepository.findByUsername("julius").get();
	}
}
