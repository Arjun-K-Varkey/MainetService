package com.abm.mainet.common.integration.dms.repository;

import java.util.Date;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.abm.mainet.common.integration.dms.domain.BpmsIntegrationEntity;


/**
 * 
 * @author Pritam.Chaphekar
 *
 */

@Repository
public interface BpmsIntegrationRepository extends JpaRepository<BpmsIntegrationEntity, Long> {
    // No code needed, JpaRepository provides basic CRUD methods:
    // save(), findById(), findAll(), deleteById(), etc.
	
	List<BpmsIntegrationEntity> findByDraftMode(String draftMode);
	
	
	@Modifying
	@Transactional
	@Query("UPDATE BpmsIntegrationEntity b " + "SET b.sucessFlag = 'Y', " + "    b.updatedDate = CURRENT_TIMESTAMP "
			+ "WHERE b.proposalNumber IN :proposalNumbers")
	int updateSuccessFlag(@Param("proposalNumbers") List<String> proposalNumbers);

	@Query("select b.proposalNumber " + "from BpmsIntegrationEntity b " + "where b.sucessFlag = 'Y' "
			+ "  and b.workFlowInitiated = 'N' " + "  and b.propertyNo is null")
	List<Long> findPendingWorkflowProposalNumbers();
	
	
	@Modifying
	@Transactional
	@Query("UPDATE BpmsIntegrationEntity b " +
	       "SET b.requestJson = :requestJson, " +
	       "    b.updatedDate = CURRENT_TIMESTAMP " +
	       "WHERE b.proposalNumber = :proposalNumber")
	int updateRequestJsonByProposalNumber(
	        @Param("requestJson") String requestJson,
	        @Param("proposalNumber") String proposalNumber
	);
	
	
	 @Modifying
	    @Transactional
	    @Query("UPDATE BpmsIntegrationEntity b " +
	           "SET b.propertyNo = :propertyNo, " +
	           "    b.updatedDate = CURRENT_TIMESTAMP, " +
	           "    b.draftMode = 'Y' " +
	           "WHERE b.proposalNumber = :proposalNumber")
	    int updatePropertyNoAndDraftMode(
	            @Param("propertyNo") String propertyNo,
	            @Param("proposalNumber") String proposalNumber
	    );
	 
	 
	 @Query("select b.requestJson from BpmsIntegrationEntity b where b.proposalNumber = :proposalNumber")
	    String findRequestJsonByProposalNumber(@Param("proposalNumber") String proposalNumber);
	
	@Query(value = "SELECT DR.ADDITIONAL_REF_NO PROPERTY_NUMBER, DR.RM_RCPTNO, DR.RM_DATE, BID.proposal_number FROM tb_dup_receipt DR INNER JOIN tb_bpms_integration_details BID ON BID.Property_No=DR.ADDITIONAL_REF_NO INNER JOIN lateral(SELECT MAX(DUP_RCID) DUP_RCID FROM tb_dup_receipt DRI WHERE DRI.DP_DEPTCODE='AS' AND DRI.ADDITIONAL_REF_NO=DR.ADDITIONAL_REF_NO AND RM_DATE=date(date_sub(NOW(),INTERVAL 1 DAY)) GROUP BY DRI.ADDITIONAL_REF_NO) DRI ON DRI.DUP_RCID=DR.DUP_RCID WHERE DR.DP_DEPTCODE='AS' AND RM_DATE=date(date_sub(NOW(),INTERVAL 1 DAY))", nativeQuery = true)
	List<Object> everydayDuplicateReceipts();
	

	@Modifying
	@Transactional
	@Query("UPDATE BpmsIntegrationEntity b " +
	       "SET b.receiptUrl = :receiptUrl, " +
	       "    b.updatedDate = CURRENT_TIMESTAMP " +
	       "WHERE b.proposalNumber = :proposalNumber")
	int updateReceiptUrl(
	        @Param("receiptUrl") String receiptUrl,
	        @Param("proposalNumber") String proposalNumber
	);
}



	

