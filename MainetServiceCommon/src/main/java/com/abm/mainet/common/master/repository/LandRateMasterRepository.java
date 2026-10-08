package com.abm.mainet.common.master.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.abm.mainet.common.workflow.domain.TbAreaLandRateEntity;

@Repository
public interface LandRateMasterRepository extends JpaRepository<TbAreaLandRateEntity, Long> {

}
