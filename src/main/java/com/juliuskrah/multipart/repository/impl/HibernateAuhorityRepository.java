package com.juliuskrah.multipart.repository.impl;

import org.springframework.stereotype.Repository;

import com.juliuskrah.multipart.entity.Authority;
import com.juliuskrah.multipart.repository.AuthorityRepository;

@Repository
public class HibernateAuhorityRepository extends HibernateBaseRepository<Authority, String> implements AuthorityRepository {

	public HibernateAuhorityRepository() {
		super(Authority.class);
	}

}
