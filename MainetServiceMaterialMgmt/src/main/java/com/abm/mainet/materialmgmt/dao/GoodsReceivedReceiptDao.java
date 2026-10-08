package com.abm.mainet.materialmgmt.dao;

import java.util.List;

import com.abm.mainet.materialmgmt.domain.GoodsReceivedReceiptEntity;

public interface GoodsReceivedReceiptDao {


	List<GoodsReceivedReceiptEntity> getGoodsSummaryList(Long vendorId, String requisitionNo, Long orgid);

	

}
