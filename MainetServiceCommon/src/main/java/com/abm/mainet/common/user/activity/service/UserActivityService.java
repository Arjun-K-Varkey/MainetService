package com.abm.mainet.common.user.activity.service;

import java.util.List;

import com.abm.mainet.common.user.activity.dto.UserActivityDTO;

public interface UserActivityService {

	void saveUserActivity(UserActivityDTO userActivityDTO);
	
	void saveUserActivityAsync(List<UserActivityDTO> activityList);
}
