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

import java.io.Serializable;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;

import com.juliuskrah.multipart.repository.BaseRepository;

/**
 * 
 * @author Julius Krah
 *
 * @param <T>
 * @param <ID>
 */
public class HibernateBaseRepository<T, ID extends Serializable> implements BaseRepository<T, ID> {

	@PersistenceContext(unitName = "julius")
	protected EntityManager em;
	private Class<T> persistentClass;

	public HibernateBaseRepository(Class<T> persistentClass) {
		this.persistentClass = persistentClass;
	}

	@Override
	public Optional<T> get(ID id) {
		Session session = this.em.unwrap(Session.class);

		return Optional.ofNullable(session.get(persistentClass, id));
	}

	@Override
	public T save(T t) {
		return this.em.merge(t);
	}

	@Override
	public void delete(T t) {
		this.em.remove(this.em.contains(t) ? t : em.merge(t));	
	}

}
