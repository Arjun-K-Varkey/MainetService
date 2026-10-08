package com.abm.mainet.common.integration.dms.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.abm.mainet.common.integration.dms.domain.BlockChainCertificateEntity;
import com.abm.mainet.common.integration.dms.domain.DigilockerCertificateEntity;

@Repository
public interface DigilockerCertificateRepository extends JpaRepository<DigilockerCertificateEntity, Long> {
    // No code needed, JpaRepository provides basic CRUD methods:
    // save(), findById(), findAll(), deleteById(), etc.
	
	

	
}
