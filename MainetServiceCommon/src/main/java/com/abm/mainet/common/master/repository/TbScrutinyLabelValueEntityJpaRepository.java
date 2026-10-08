/**
 *
 */
package com.abm.mainet.common.master.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.abm.mainet.cfc.scrutiny.domain.TbScrutinyLabelValueEntity;
import com.abm.mainet.cfc.scrutiny.domain.TbScrutinyLabelValueEntityKey;

/**
 * @author umashanker.kanaujiya
 *
 */
@Repository
public interface TbScrutinyLabelValueEntityJpaRepository
        extends PagingAndSortingRepository<TbScrutinyLabelValueEntity, TbScrutinyLabelValueEntityKey> {

    @Query("select sv from TbScrutinyLabelValueEntity sv"
            + " where  sv.labelValueKey.slLabelId=:slLabelId and sv.labelValueKey.saApplicationId=:saApplicationId"
            + " and sv.orgId.orgid =:orgId")
    TbScrutinyLabelValueEntity findScrutinyLabelValueData(@Param("slLabelId") Long slLabelId,
            @Param("saApplicationId") Long saApplicationId, @Param("orgId") Long orgId);

	@Query("SELECT sv.svValue FROM TbScrutinyLabelValueEntity sv "
			+ "WHERE sv.labelValueKey.saApplicationId = :saApplicationId " + "AND sv.labelValueKey.slLabelId IN "
			+ "(SELECT sl.compositePrimaryKey.slLabelId FROM TbScrutinyLabelsEntity sl WHERE sl.slLabel LIKE '%road%') "
			+ "AND sv.orgId.orgid = :orgId")
	Optional<String> findScrutinyLabelValues(@Param("saApplicationId") Long saApplicationId,
			@Param("orgId") Long orgId);

}
