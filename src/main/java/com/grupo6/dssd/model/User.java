package com.grupo6.dssd.model;

import javax.persistence.*;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
@Table(name = "LAB_USER")
public class User {

	private Long id;
	private String name;
	private String password;
	private Role role;

	public User(){}

	public User(Long id, String name, String password, Role role) {
		this.id = id;
		this.name = name;
		this.password = password;
		this.role = role;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "NAME", nullable = false, length = 30, unique = true)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "PASSWORD", nullable = false)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@JoinColumn(name = "ROLE_ID")
	@ManyToOne(fetch = FetchType.EAGER)
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Override
	public String toString(){
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}

}
