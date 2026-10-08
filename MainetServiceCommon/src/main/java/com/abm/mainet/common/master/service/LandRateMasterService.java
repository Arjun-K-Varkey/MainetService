package com.abm.mainet.common.master.service;

import java.util.List;

import com.abm.mainet.common.master.dto.LandRateMasterBean;
import com.abm.mainet.common.master.dto.LandRateMaterDTO;

public interface LandRateMasterService {

	List<Object[]> getLandRateDetailsList(String area, String surveyNo);


	boolean saveForm(LandRateMasterBean bean);

}
