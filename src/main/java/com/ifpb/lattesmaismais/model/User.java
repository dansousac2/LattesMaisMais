package com.ifpb.lattesmaismais.model;

import javax.persistence.*;

@Entity
@Table(name = "USER")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "USERE_ID")
	private Integer id;
}
