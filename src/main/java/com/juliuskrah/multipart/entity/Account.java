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
package com.juliuskrah.multipart.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Account extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Size(max = 50)
	@Column(name = "first_name")
	private String firstName;

	@Size(max = 50)
	@Column(name = "last_name")
	private String lastName;

	@NotNull
	@Size(min = 60, max = 60)
	@Column(name = "password_hash", length = 60)
	private String password;

	@NotNull
	@Size(min = 1, max = 50)
	@Column(unique = true, nullable = false, length = 50)
	private String username;

	@NotNull
	@Column(nullable = false)
	private boolean activated;

	@ManyToMany
	// @formatter:off
	@JoinTable(
		name = "account_authority",
		joinColumns = {
				@JoinColumn(name = "account_id", referencedColumnName = "id"),
		},
		inverseJoinColumns = {
				@JoinColumn(name = "authority_name", referencedColumnName = "name")
		}
	)
	// @formatter:on
	private Set<Authority> authorities = new HashSet<>();

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username.toLowerCase(Locale.ENGLISH);
	}

	public boolean isActivated() {
		return activated;
	}

	public void setActivated(boolean activated) {
		this.activated = activated;
	}

	public Set<Authority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Set<Authority> authorities) {
		this.authorities = authorities;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Account account = (Account) o;

		if (!username.equals(account.username)) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		return username.hashCode();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		// @formatter:off
		sb
			.append("Account: {id=").append(this.getId()).append(',')
			.append(" firstName=").append(firstName).append(',')
			.append(" lastName=").append(lastName).append(',')
			.append(" username=").append(username).append(',')
			.append(" activated=").append(activated).append('}');
		// @formatter:on

		return sb.toString();
	}
}
