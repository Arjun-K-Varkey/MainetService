package com.abm.mainet.common.master.dao;

import java.util.List;

public interface LandRateMasterDao {

	List<Object[]> fetchLandRateMasterData(String area, String surveyNo);

}
