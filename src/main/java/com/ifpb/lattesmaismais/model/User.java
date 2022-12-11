package com.ifpb.lattesmaismais.model;

import javax.persistence.*;

@Table(name = "USER")
@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "USERE_ID")
	private Integer id;
	
	@Column(name = "IS_VALIDATOR")
	private boolean isValidator;
	
	@Column(name = "USER_NAME")
	private String name;
	
	@Column(name = "USER_EMAIL")
	private String email;
	
	@Column(name = "USER_PASSWORDTOKEN")
	private String passwordToken;
}
