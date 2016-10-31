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
package com.juliuskrah.multipart.repository.impl;

import java.util.Optional;
import java.util.stream.Stream;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.juliuskrah.multipart.entity.Account;
import com.juliuskrah.multipart.entity.Account_;
import com.juliuskrah.multipart.repository.AccountRepository;

@Repository
public class HibernateAccountRepository extends HibernateBaseRepository<Account, Long> implements AccountRepository {

	public HibernateAccountRepository() {
		super(Account.class);
	}

	@Override
	public Optional<Account> findByUsername(String username) {
		CriteriaBuilder qb = this.em.getCriteriaBuilder();
		CriteriaQuery<Account> query = qb.createQuery(Account.class);
		Root<Account> root = query.from(Account.class);
		Predicate predicate = qb.like(root.get(Account_.username), username);
		query.where(predicate);
		Query q = this.em.createQuery(query);

		return Optional.of((Account) q.getSingleResult());
	}

	@Override
	public Stream<Account> findByLastName(String lastName) {
		Session session = this.em.unwrap(Session.class);
		CriteriaBuilder qb = session.getCriteriaBuilder();
		CriteriaQuery<Account> cq = qb.createQuery(Account.class);
		Root<Account> root = cq.from(Account.class);
		Predicate predicate = qb.like(root.get(Account_.lastName), lastName);
		cq.where(predicate);

		return session.createQuery(cq).stream();
	}

}
