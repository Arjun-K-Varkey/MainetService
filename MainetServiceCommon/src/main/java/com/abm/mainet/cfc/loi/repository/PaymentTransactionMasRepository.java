package com.abm.mainet.cfc.loi.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.abm.mainet.common.integration.payment.entity.PaymentTransactionMas;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentTransactionMasRepository extends JpaRepository<PaymentTransactionMas, Long> {

	@Modifying(clearAutomatically = true)
	@Query("update PaymentTransactionMas a set a.recvStatus=:status, a.updatedDate = CURRENT_TIMESTAMP  where  a.recvBankRefNum =:recvBankRefNum and a.orgId=:orgId ")
	void updateTransactionByBankRefId(@Param("recvBankRefNum") String recvBankRefNum, @Param("status") String status,
			@Param("orgId") Long orgId);
	
	@Modifying(clearAutomatically = true)
	@Query("update PaymentTransactionMas a set a.recvStatus=:status,a.sendAmount=:sendAmount, a.updatedDate = CURRENT_TIMESTAMP  where  a.tranCmId =:transId and a.orgId=:orgId")
	void updateTransactionByTransId(@Param("transId") Long transId, @Param("status") String status,
			@Param("orgId") Long orgId, @Param("sendAmount") BigDecimal sendAmount);
	
	@Query("SELECT a.recvStatus FROM PaymentTransactionMas a WHERE a.recvBankRefNum =:recvBankRefNum ")
    String fetchPaymentStatus(@Param("recvBankRefNum") String recvBankRefNum);
	
	
	@Query("SELECT a.recvStatus FROM PaymentTransactionMas a WHERE a.tranCmId =:transId ")
    String fetchPaymentStatusForTransId(@Param("transId") Long transId);
	
	@Query("SELECT a.sendHash FROM PaymentTransactionMas a WHERE a.tranCmId =:transId ")
    String fetchSendHashByTransId(@Param("transId") Long transId);
	
	@Query("SELECT a FROM PaymentTransactionMas a WHERE a.recvBankRefNum =:recvBankRefNum ")
	PaymentTransactionMas fetchPaymentDataByrecvBankRefNum(@Param("recvBankRefNum") String recvBankRefNum);
	
	@Modifying
	@Query("update PaymentTransactionMas a set a.recvStatus = :status, a.recvBankRefNum = :bankRef, " +
	       "a.sendAmount = :amount where a.tranCmId = :txnId and a.recvStatus <> 'SUCCESS'")
	int updateIfNotProcessed(@Param("txnId") Long txnId, @Param("status") String status,
	                         @Param("bankRef") String bankRef, @Param("amount") BigDecimal amount);
	
	@Query("SELECT a FROM PaymentTransactionMas a WHERE a.tranCmId = (SELECT MAX(b.tranCmId) "
			+ "   FROM PaymentTransactionMas b WHERE b.referenceId = :referenceId "
			+ "   AND DATE(b.referenceDate) = :referenceDate AND b.recvStatus = 'pending' and pgSource='GETEPAY')")
	PaymentTransactionMas fetchPaymentTransaction(@Param("referenceId") String referenceId,
			@Param("referenceDate") Date referenceDate);

	@Query("SELECT a FROM PaymentTransactionMas a WHERE a.tranCmId = (SELECT MAX(b.tranCmId) "
			+ "   FROM PaymentTransactionMas b WHERE b.referenceId = :referenceId "
			+ "   AND DATE(b.referenceDate) = :referenceDate AND b.flatNo = :flatNo AND b.recvStatus = 'pending' and pgSource='GETEPAY')")
	PaymentTransactionMas fetchPaymentTransaction(@Param("referenceId") String referenceId,
			@Param("referenceDate") Date referenceDate, @Param("flatNo") String flatNo);
	
	@Query("SELECT p.tranCmId FROM PaymentTransactionMas p WHERE p.tranCmId IN :tranCmIdList")
	List<Long> fetchExistingTranCmIds(@Param("tranCmIdList") List<Long> tranCmIdList);
	
	@Modifying
	@Query("UPDATE PaymentTransactionMas p SET p.recvStatus = :status WHERE p.tranCmId IN :tranCmIdList")
	int updateStatusByTranCmIdList(@Param("tranCmIdList") List<Long> tranCmIdList, @Param("status") String status);
}
