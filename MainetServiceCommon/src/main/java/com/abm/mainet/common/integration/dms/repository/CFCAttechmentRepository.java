/**
 *
 */
package com.abm.mainet.common.integration.dms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.abm.mainet.common.integration.dms.domain.CFCAttachment;

/**
 * @author Lalit.Prusti
 *
 */
@Repository
public interface CFCAttechmentRepository extends CrudRepository<CFCAttachment, Long> {

    @Query("FROM CFCAttachment s WHERE s.orgid=:orgid and s.applicationId=:applicationId and s.smScrutinyId is null ")
    public List<CFCAttachment> findAllAttachmentsByAppId(@Param("orgid") long orgId, @Param("applicationId") long applicationId);
    
    @Transactional
	@Modifying
	@Query(" update CFCAttachment a set a.clmAprStatus=:clmAprStatus where a.orgid=:orgid and  a.applicationId =:applicationId ")
	public void updateClmAprStatus(@Param("clmAprStatus") String clmAprStatus, @Param("orgid") long orgId,
			@Param("applicationId") long applicationId);

}
