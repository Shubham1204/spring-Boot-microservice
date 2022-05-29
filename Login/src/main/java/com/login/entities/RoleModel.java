package com.login.entities;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "role_mst")
public class RoleModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "role_id")
	private int roleId;
	@Column(name = "role_name")
	private String roleName;
	@Column(name = "role_description")
	private String roleDescription;
	@Column(name = "role_status",columnDefinition = "varchar(1) default 'Y'")
	private char roleStatus='Y';
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "role_right_mapping",joinColumns = @JoinColumn(name="role_id",nullable = true,unique = false),inverseJoinColumns = @JoinColumn(name="right_id",unique = false,nullable = true))
	private Set<RightModel> rights;
}
