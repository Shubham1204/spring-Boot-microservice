package com.login.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.login.entities.RoleModel;

@Repository
public interface RoleRepository extends JpaRepository<RoleModel, Integer>{

	
	@Modifying
	@Query(value = "INSERT INTO role_mst(role_name,role_description) VALUES ( ?1 , ?2 )",nativeQuery = true)
	Integer savequery(String roleName, String roleDescription);

	RoleModel findByRoleName(String roleName);

	@Modifying
	@Query(value = "INSERT INTO role_right_mapping(role_id,right_id) VALUES ((select role_id from role_mst where role_name= ?1 ),(select right_id from right_mst where right_name= ?2 ) )",nativeQuery = true)
	Integer mapRoleRight(String roleName, String rightName);

}
