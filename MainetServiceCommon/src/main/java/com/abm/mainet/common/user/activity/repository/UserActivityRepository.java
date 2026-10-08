package com.abm.mainet.common.user.activity.repository;

import com.abm.mainet.common.user.activity.domain.UserActivityEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserActivityRepository extends JpaRepository<UserActivityEntity, Long>{

}
