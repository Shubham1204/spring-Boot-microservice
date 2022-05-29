package com.login.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "right_mst")
public class RightModel implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "right_id")
	private int rightId;
	@Column(name = "right_name",unique = true)
	private String rightName;
	@Column(name = "right_description")
	private String rightDescription;
	@Column(name = "right_path")
	private String rightPath;
	@Column(name = "right_status",columnDefinition = "varchar(1) default 'Y'")
	private char rightStatus='Y';
}
