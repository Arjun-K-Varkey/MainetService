/**
 *
 */
package com.abm.mainet.common.integration.acccount.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.abm.mainet.common.integration.acccount.domain.TbSrcptFeesDetEntity;

/**
 * @author alpesh.mehta
 *
 */
public interface TbServiceReceiptJpaRepository extends PagingAndSortingRepository<TbSrcptFeesDetEntity, Long> {

    @Query("select tbSrcptFeesDetEntity from TbSrcptFeesDetEntity tbSrcptFeesDetEntity "
            + " where tbSrcptFeesDetEntity.rmRcptid.rmRcptid in (select tbServiceReceiptMasEntity.rmRcptid  from  TbServiceReceiptMasEntity tbServiceReceiptMasEntity  where tbServiceReceiptMasEntity.apmApplicationId= :applicationId  and tbServiceReceiptMasEntity.rmLoiNo is not null ) and tbSrcptFeesDetEntity.orgId=:orgId")
    List<TbSrcptFeesDetEntity> finndTaxidFeeamount(@Param("applicationId") Long applicationId, @Param("orgId") Long orgId);

}
