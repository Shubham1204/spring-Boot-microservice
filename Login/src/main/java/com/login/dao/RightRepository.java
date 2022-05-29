package com.login.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.login.entities.RightModel;

@Repository
public interface RightRepository extends JpaRepository<RightModel, Integer>{

	RightModel findByRightName(String rightName);
}
