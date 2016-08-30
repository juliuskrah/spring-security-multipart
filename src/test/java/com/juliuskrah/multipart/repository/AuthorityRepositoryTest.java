package com.juliuskrah.multipart.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Optional;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;

import com.juliuskrah.multipart.ApplicationTest;
import com.juliuskrah.multipart.entity.Authority;

public class AuthorityRepositoryTest extends ApplicationTest {

	@Inject
	private AuthorityRepository authorityRepository;

	@Before
	public void before() {
		Authority a = new Authority("ROLE_SUPERVISOR");

		authorityRepository.save(a);
	}

	@Test
	public void testSave() {
		Authority a = new Authority("ROLE_PROMOTTER");

		authorityRepository.save(a);

		assertThat(a.getName(), is("ROLE_PROMOTTER"));
	}

	@Test
	public void testGet() {
		Optional<Authority> a = authorityRepository.get("ROLE_ADMIN");

		assertThat(a.isPresent(), is(true));
		assertThat(a.get().getName(), is("ROLE_ADMIN"));
	}

	@Test
	public void testDelete() {
		Authority a = authorityRepository.get("ROLE_SUPERVISOR").get();
		authorityRepository.delete(a);

		assertThat(a.getName(), is("ROLE_SUPERVISOR"));

		assertThat(authorityRepository.get("ROLE_SUPERVISOR").
				orElse(new Authority("ROLE_NOT_FOUND")), is(new Authority("ROLE_NOT_FOUND")));
	}
}
