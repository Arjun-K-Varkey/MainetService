
package com.abm.mainet.account.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.abm.mainet.account.domain.AccountBillEntryMasterEnitity;
import com.abm.mainet.common.integration.acccount.domain.AdvanceEntryEntity;
import com.abm.mainet.common.integration.acccount.domain.TbServiceReceiptMasEntity;

/**
 * @author satish.rathore
 *
 */
@Repository
public interface RegisterOfAdvanceRepository extends CrudRepository<AdvanceEntryEntity, Long> {

    @Query("from  AdvanceEntryEntity aee  where aee.prAdvEntryDate <= :asOnDates and aee.orgid=:orgId order by aee.prAdvEntryDate")
    List<AdvanceEntryEntity> findAdvanceLadgerReport(@Param("asOnDates") Date asOnDates,
            @Param("orgId") Long orgId);

    @Query(" from  TbServiceReceiptMasEntity sm  where sm.refId = :prAdvEntryId  and  sm.orgId=:orgId and sm.receiptTypeFlag='A'")
    List<TbServiceReceiptMasEntity> findReceiptForAdvanceLedger(@Param("prAdvEntryId") Long prAdvEntryId, @Param("orgId") Long orgId);

    @Query("from  AccountBillEntryMasterEnitity bm where bm.advanceTypeId =:prAdvEntryId and bm.orgId=:orgId and bm.checkerAuthorization = 'Y' ")
    List<AccountBillEntryMasterEnitity> findbillNoAndDateForAdvanceLadger(@Param("prAdvEntryId") Long prAdvEntryId,
            @Param("orgId") Long orgId);
    
    @Query(value = "SELECT BM.VM_VENDORNAME " +
            "FROM tb_ac_bill_mas BM " +
            "INNER JOIN tb_ac_payment_det PD ON PD.BM_ID = BM.BM_ID " +
            "INNER JOIN tb_ac_payment_mas PM ON PM.PAYMENT_ID = PD.PAYMENT_ID " +
            "INNER JOIN tb_ac_advance ADV ON ADV.pay_adv_voucherno = PM.PAYMENT_NO " +
            "AND DATE(ADV.pay_adv_voucherdt) = DATE(PM.PAYMENT_DATE) " +
            "WHERE ADV.pay_adv_voucherno = :paymentNo " +
            "AND DATE(ADV.pay_adv_voucherdt) = :paymentDate " +
            "LIMIT 1",
            nativeQuery = true)
    String findDummyVendorNameByPaymentVouNoAndPayDate(
            @Param("paymentNo") Long paymentNo,
            @Param("paymentDate") Date paymentDate);
    
    

}
