package com.abm.mainet.common.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.abm.mainet.common.domain.TbWorkOrderEntity;

/**
 * Repository : TbWorkOrder.
 */
@Repository
public interface TbWorkOrderJpaRepository extends PagingAndSortingRepository<TbWorkOrderEntity, Long> {

    @Query("select tbWorkOrderEntity.woApplicationId, tbCfcApplicationMstEntity.apmFname , tbWorkOrderEntity.woPrintFlg from TbWorkOrderEntity tbWorkOrderEntity, TbCfcApplicationMstEntity tbCfcApplicationMstEntity "
            + " where tbWorkOrderEntity.woServiceId = :artServiceId and tbWorkOrderEntity.woApplicationId = tbCfcApplicationMstEntity.apmApplicationId and tbWorkOrderEntity.orgid= :orgid  and  tbWorkOrderEntity.woApplicationId =  tbCfcApplicationMstEntity.apmApplicationId and tbCfcApplicationMstEntity.tbOrganisation.orgid = :orgid order by tbWorkOrderEntity.woApplicationId desc")
    List<Object[]> findWorkOrderPrintList(@Param("artServiceId") Long artServiceId, @Param("orgid") Long orgid);

    @Query("select tbWorkOrderEntity.woApplicationId, tbCfcApplicationMstEntity.apmFname ,tbWorkOrderEntity.PlumId ,tbWorkOrderEntity.woPrintFlg from TbWorkOrderEntity tbWorkOrderEntity,TbCfcApplicationMstEntity tbCfcApplicationMstEntity "
            + " where tbWorkOrderEntity.woServiceId = :artServiceId and tbWorkOrderEntity.woApplicationId = tbCfcApplicationMstEntity.apmApplicationId and  tbWorkOrderEntity.orgid= :orgid and tbWorkOrderEntity.PlumId is null ")
    List<Object[]> WorkOrderPrintListwithAndWithoutPlumber(@Param("artServiceId") Long artServiceId, @Param("orgid") Long orgid);

	/*
	 * @Query("select tbWorkOrderEntity.woApplicationId, tbCfcApplicationMstEntity.apmFname ,tbWorkOrderEntity.PlumId ,tbWorkOrderEntity.woPrintFlg from TbWorkOrderEntity tbWorkOrderEntity,TbCfcApplicationMstEntity tbCfcApplicationMstEntity "
	 * +
	 * " where tbWorkOrderEntity.woServiceId = :artServiceId and tbWorkOrderEntity.woApplicationId = tbCfcApplicationMstEntity.apmApplicationId and  tbWorkOrderEntity.orgid= :orgid"
	 * ) List<Object[]> WorkOrderPrintListwithAndWithPlumber(@Param("artServiceId")
	 * Long artServiceId, @Param("orgid") Long orgid);
	 */
    
    @Query("select tbWorkOrderEntity from TbWorkOrderEntity tbWorkOrderEntity "
            + " where tbWorkOrderEntity.woOrderNo = :workOrderNo and  tbWorkOrderEntity.orgid= :orgid")
    TbWorkOrderEntity ForPrintflagupdate(@Param("workOrderNo") String workOrderNo, @Param("orgid") Long orgid);

    @Modifying
    @Transactional
	@Query("UPDATE TbWorkOrderEntity c SET  c.issuedDate=CURRENT_TIMESTAMP, c.issuedBy=:issuedBy  where c.woApplicationId=:applicationId and c.orgid=:orgid")
	void updateIssuanceDataInWorkOrder(@Param("applicationId") Long applicationId, @Param("issuedBy") Long issuedBy, @Param("orgid") Long orgid);

    @Query("select tbWorkOrderEntity from TbWorkOrderEntity tbWorkOrderEntity where tbWorkOrderEntity.woApplicationId=:applicationId and tbWorkOrderEntity.orgid=:orgid")
    List<TbWorkOrderEntity> findDiscByApplicationID(@Param("applicationId") Long applicationId, @Param("orgid") Long orgid);
    
    
    
    @Query(value = "SELECT \n" + 
    		"    tbworkorde0_.WO_APPLICATION_ID AS col_0_0_,\n" + 
    		"    tbcfcappli1_.APM_FNAME AS col_1_0_,\n" + 
    		"    tbworkorde0_.PLUM_ID AS col_2_0_,\n" + 
    		"    tbworkorde0_.WO_PRINT_FLG AS col_3_0_,\n" + 
    		"    CONCAT_WS(' ',\n" + 
    		"        COALESCE(plumbermas0_.PLUM_FNAME, ''),\n" + 
    		"        COALESCE(plumbermas0_.PLUM_MNAME, ''),\n" + 
    		"        COALESCE(plumbermas0_.PLUM_LNAME, '')\n" + 
    		"    ) AS PLUMBER_NAME\n" + 
    		"FROM TB_WORK_ORDER tbworkorde0_\n" + 
    		"JOIN TB_CFC_APPLICATION_MST tbcfcappli1_\n" + 
    		"    ON tbworkorde0_.WO_APPLICATION_ID = tbcfcappli1_.APM_APPLICATION_ID\n" + 
    		"LEFT JOIN TB_WT_PLUM_MAS plumbermas0_\n" + 
    		"    ON tbworkorde0_.PLUM_ID = plumbermas0_.PLUM_ID\n" + 
    		"WHERE tbworkorde0_.WO_SERVICE_ID = :artServiceId\n" + 
    		"AND tbworkorde0_.ORGID = :orgid", nativeQuery = true)
    List<Object[]> WorkOrderPrintListwithAndWithPlumber(@Param("artServiceId") Long artServiceId, @Param("orgid") Long orgid);

    
    
    
    
    @Query(value = "SELECT \n" + 
    		"    tbworkorde0_.WO_APPLICATION_ID AS col_0_0_,\n" + 
    		"    tbcfcappli1_.APM_FNAME AS col_1_0_,\n" + 
    		"    tbworkorde0_.PLUM_ID AS col_2_0_,\n" + 
    		"    tbworkorde0_.WO_PRINT_FLG AS col_3_0_,\n" + 
    		"    CONCAT_WS('',\n" + 
    		"        COALESCE(plumbermas0_.PLUM_FNAME, ''),\n" + 
    		"        COALESCE(plumbermas0_.PLUM_MNAME, ''),\n" + 
    		"        COALESCE(plumbermas0_.PLUM_LNAME, '')\n" + 
    		"    ) AS PLUMBER_NAME\n" + 
    		"FROM TB_WORK_ORDER tbworkorde0_\n" + 
    		"JOIN TB_CFC_APPLICATION_MST tbcfcappli1_\n" + 
    		"    ON tbworkorde0_.WO_APPLICATION_ID = tbcfcappli1_.APM_APPLICATION_ID\n" + 
    		"LEFT JOIN TB_WT_PLUM_MAS plumbermas0_\n" + 
    		"    ON tbworkorde0_.PLUM_ID = plumbermas0_.PLUM_ID\n" + 
    		"WHERE tbworkorde0_.WO_SERVICE_ID = :artServiceId\n" + 
    		"AND tbworkorde0_.ORGID = :orgid AND tbcfcappli1_.APM_APPLICATION_ID = :applicationId", nativeQuery = true)
    List<Object[]> WorkOrderPrintListbyserviceIdAndAppId(@Param("artServiceId") Long artServiceId, @Param("orgid") Long orgid, @Param("applicationId") Long applicationId);
    
    
    
    
    
    
    @Query(value = "SELECT \n" + 
    		"    tbworkorde0_.WO_APPLICATION_ID AS col_0_0_,\n" + 
    		"    tbcfcappli1_.APM_FNAME AS col_1_0_,\n" + 
    		"    tbworkorde0_.PLUM_ID AS col_2_0_,\n" + 
    		"    tbworkorde0_.WO_PRINT_FLG AS col_3_0_,\n" + 
    		"    CONCAT_WS('',\n" + 
    		"        COALESCE(plumbermas0_.PLUM_FNAME, ''),\n" + 
    		"        COALESCE(plumbermas0_.PLUM_MNAME, ''),\n" + 
    		"        COALESCE(plumbermas0_.PLUM_LNAME, '')\n" + 
    		"    ) AS PLUMBER_NAME\n" + 
    		"FROM TB_WORK_ORDER tbworkorde0_\n" + 
    		"JOIN TB_CFC_APPLICATION_MST tbcfcappli1_\n" + 
    		"    ON tbworkorde0_.WO_APPLICATION_ID = tbcfcappli1_.APM_APPLICATION_ID\n" + 
    		"LEFT JOIN TB_WT_PLUM_MAS plumbermas0_\n" + 
    		"    ON tbworkorde0_.PLUM_ID = plumbermas0_.PLUM_ID\n" +  
    		"WHERE tbworkorde0_.ORGID = :orgid AND tbcfcappli1_.APM_APPLICATION_ID = :applicationId", nativeQuery = true)
    List<Object[]> WorkOrderPrintListbyAppId( @Param("orgid") Long orgid, @Param("applicationId") Long applicationId);

}
