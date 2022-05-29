package com.login.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.login.dao.RoleRepository;
import com.login.entities.RightModel;
import com.login.entities.RoleModel;
import com.login.exception.RightNotFoundException;
import com.login.exception.RoleExistsException;
import com.login.exception.RoleNotFoundException;
import com.login.exception.RoleNotSavedException;
import com.login.exception.RoleRightNotMappedException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RoleService {

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private RightService rightService;

	public RoleModel addRole(RoleModel role) throws RightNotFoundException, RoleNotFoundException,
			RoleRightNotMappedException, RoleNotSavedException, RoleExistsException {
		log.info("bi) service -> RoleService and method : addRole and saving to db with paramter recieved RoleModel : "
				+ role);
		if ((getRoleByRoleName(role.getRoleName())) == null) {
			Integer savequery = roleRepository.savequery(role.getRoleName(), role.getRoleDescription());
			log.info(
					"bj) service -> RoleService and method : addRole after saving role in db with count :" + savequery);
			if (savequery > 0) {
				boolean isMapRoleToRight = mapRoleToRight(role);
				log.info("bk) service -> RoleService and method : addRole after mapping role right in db :"
						+ isMapRoleToRight);
				if (isMapRoleToRight) {
					return role;
				} else {
					log.info("bl) service -> RoleService and method : addRole and getting RoleRightNotMappedException");
					throw new RoleRightNotMappedException("Role right not mapped");
				}
			} else {
				log.info("bm) service -> RoleService and method : addRole and getting RoleNotSavedException");
				throw new RoleNotSavedException("Role not saved in db");
			}
		} else {
			log.info("bn) service -> RoleService and method : addRole and getting RoleExistsException");
			throw new RoleExistsException("Role Already exists in db");
		}
	}

	public boolean mapRoleToRight(RoleModel role)
			throws RightNotFoundException, RoleNotFoundException, RoleRightNotMappedException {
		log.info("bo) service -> RoleService at start of method : mapRoleToRight with paramter recieved RoleModel : "
				+ role);
		int count = 0;
		if ((getRoleByRoleName(role.getRoleName())) != null) {
			for (RightModel rightModel : role.getRights()) {
				if ((rightService.getRightByName(rightModel.getRightName())) != null) {
					Integer mapRoleRight = roleRepository.mapRoleRight(role.getRoleName(), rightModel.getRightName());
					count += mapRoleRight;
					log.info(
							"bp) service -> RoleService method : mapRoleToRight after mapping right to role and count : "
									+ count + " with Rightname : " + rightModel.getRightName());
				} else {
					log.info("bq) service -> RoleService method : mapRoleToRight right does not exist in db");
					continue;
				}
			}
			if (!(count > 0)) {
				log.info(
						"br) service -> RoleService and method : mapRoleToRight and getting RoleRightNotMappedException");
				return false;
//				throw new RoleRightNotMappedException("role right not mapped");
			}
			return true;
		} else {
			log.info("bs) service -> RoleService and method : mapRoleToRight and getting RoleNotFoundException");
//			throw new RoleNotFoundException("Role not found in db");
			return false;
		}
	}

	public List<RoleModel> getRoles() throws RoleNotFoundException {
		log.info("bt) service -> RoleService and method : getRoles and getting roles from db");
		List<RoleModel> findAllRoles = roleRepository.findAll();
		if (findAllRoles.size() > 0) {
			log.info("bu) service -> RoleService and method : getRoles and getting roles from db" + findAllRoles);
			return findAllRoles;
		} else {
			log.error("bv) service -> RoleService and method : getRoles and getting RoleNotFoundException");
			throw new RoleNotFoundException("No role exist in db");
		}
	}

	public RoleModel getRoleByRoleName(String roleName) throws RoleNotFoundException {
		log.info("bw) service -> RoleService and method : getRoleByRoleName and getting roles from db with roleName : "
				+ roleName);
		RoleModel findByRoleName = roleRepository.findByRoleName(roleName);
		if (findByRoleName != null) {
			log.info("bx) service -> RoleService and method : getRoleByRoleName and getting roles from db : " + findByRoleName);
			return findByRoleName;
		} else {
			log.info("by) service -> RoleService and method : getRoleByRoleName and getting RoleNotFoundException");
//			throw new RoleNotFoundException("role does not exist in db");
			return null;
		}
	}
}
