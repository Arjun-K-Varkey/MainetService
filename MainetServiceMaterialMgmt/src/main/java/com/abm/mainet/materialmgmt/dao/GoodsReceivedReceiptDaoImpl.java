package com.abm.mainet.materialmgmt.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.abm.mainet.common.dao.AbstractDAO;
import com.abm.mainet.materialmgmt.domain.GoodsReceivedReceiptEntity;
import com.abm.mainet.materialmgmt.domain.IndentProcessEntity;
import com.abm.mainet.materialmgmt.domain.ItemOpeningBalanceEntity;

@Repository
public class GoodsReceivedReceiptDaoImpl extends AbstractDAO<GoodsReceivedReceiptEntity>
		implements GoodsReceivedReceiptDao {

	
	
	/*
	 * @SuppressWarnings("unchecked")
	 * 
	 * @Override
	 * 
	 * @Transactional public List<GoodsReceivedReceiptEntity>
	 * getGoodsSummaryList(Long vendorId, Long requisitionNo, Long orgid) { final
	 * StringBuilder builder = new StringBuilder();
	 * builder.append("select g.requisitionNo, g.vendorName, g.receivedReceiptDate"
	 * + " from GoodsReceivedReceiptEntity g where g.orgId=:orgid"); if (vendorId !=
	 * null) builder.append(" and g.vendorId=:vendorId"); if (requisitionNo != null
	 * && !requisitionNo.equals(""))
	 * builder.append(" and g.requisitionNo=:requisitionNo");
	 * 
	 * if (deptId != null) builder.append(" and i.deptId=:deptId"); if (indenter !=
	 * null) builder.append(" and i.indenter=:indenter"); if (status != null &&
	 * !status.equals("0")) builder.append(" and i.status=:status");
	 * 
	 * builder.append(" order by g.vendorId desc");
	 * 
	 * final Query query = createQuery(builder.toString());
	 * query.setParameter("orgid", orgid); if (vendorId != null)
	 * query.setParameter("vendorId", vendorId); if (requisitionNo != null &&
	 * !requisitionNo.equals("")) query.setParameter("requisitionNo",
	 * requisitionNo);
	 * 
	 * if (deptId != null) query.setParameter("deptId", deptId); if (indenter !=
	 * null) query.setParameter("indenter", indenter); if (status != null &&
	 * !status.equals("0")) query.setParameter("status", status);
	 * 
	 * return query.getResultList();
	 * 
	 * }
	 */
	 
	
	@SuppressWarnings("unchecked")
	@Override
	public List<GoodsReceivedReceiptEntity> getGoodsSummaryList(Long vendorId, String requisitionNo, Long orgId) {
	    Query query = this.createQuery(buildQuery(vendorId, requisitionNo, orgId));
	    query.setParameter("orgId", orgId);
	    
	    if (vendorId != null && vendorId != 0L) {
	        query.setParameter("vendorId", vendorId);
	    }
	    
	    if (requisitionNo != null && !requisitionNo.isEmpty()) {
	        query.setParameter("requisitionNo", requisitionNo);
	    }
	    
	    return query.getResultList();
	}

	private String buildQuery(Long vendorId, String requisitionNo, Long orgId) {
	    StringBuilder query = new StringBuilder(
	        "SELECT g FROM GoodsReceivedReceiptEntity g WHERE g.orgId = :orgId "
	    );
	    
	    if (vendorId != null && vendorId != 0L) {
	        query.append("AND g.vendorId = :vendorId ");
	    }
	    
	    if (requisitionNo != null && !requisitionNo.isEmpty()) {
	        query.append("AND g.requisitionNo = :requisitionNo ");
	    }
	    
	    return query.toString();
	}

}
