package com.login.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.login.dao.RightRepository;
import com.login.entities.RightModel;
import com.login.exception.RightExistsException;
import com.login.exception.RightNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RightService {

	@Autowired
	private RightRepository rightRepository;

	@Cacheable(cacheNames = "right",key = "#right.rightName")
	public RightModel addRight(RightModel right) throws RightExistsException, RightNotFoundException {
		if (getRightByName(right.getRightName()) == null) {
			log.info(
					"ba) service -> RightService and method : addRight and saving to db with paramter recieved RightModel : "
							+ right);
			return rightRepository.save(right);
		} else {
			log.error("bb) service -> RightService and method : addRight but right already exists in db");
			throw new RightExistsException("Right Already Exists in DB");
		}
	}

	public List<RightModel> getRights() throws RightNotFoundException {
		log.info("bc) service -> RightService and method : getRights and getting rights from db");
		List<RightModel> findAllRights = rightRepository.findAll();
		if (findAllRights.size() > 0) {
			log.info("bd) service -> RightService and method : getRights and getting rights from db"+findAllRights);
			return findAllRights;
		} else {
			log.error("be) service -> RightService and method : getRights but no right found in db");
			throw new RightNotFoundException("No right found in DB");
		}
	}

	@Cacheable(cacheNames = "right",key = "#rightName")
	public RightModel getRightByName(String rightName) throws RightNotFoundException {
		log.info(
				"bf) service -> RightService and method : getRightByName and getting right from db with paramter recieved Right name : "
						+ rightName);
		RightModel right = rightRepository.findByRightName(rightName);
		if (right != null) {
			log.info("bg) service -> RightService and method : getRights and getting rights from db"+right);
			return right;
		} else {
			return null;
//			log.error("bh) service -> RightService and method : getRights but no right found in db");
//			throw new RightNotFoundException("No right found in DB");
		}
	}
}
