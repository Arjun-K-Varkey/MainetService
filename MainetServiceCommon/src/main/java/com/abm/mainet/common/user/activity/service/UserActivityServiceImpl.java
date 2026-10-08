package com.abm.mainet.common.user.activity.service;

import java.util.List;

import com.abm.mainet.common.user.activity.domain.UserActivityEntity;
import com.abm.mainet.common.user.activity.dto.UserActivityDTO;
import com.abm.mainet.common.user.activity.repository.UserActivityRepository;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class UserActivityServiceImpl implements UserActivityService {

	private static final Logger LOGGER = Logger.getLogger(UserActivityServiceImpl.class);
	@Autowired
	private UserActivityRepository userActivityRepository;

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public void saveUserActivity(UserActivityDTO userActivityDTO) {
		LOGGER.info("Begin UserActivityServiceImpl saveUserActivity() method");
		if (userActivityDTO != null) {
			UserActivityEntity userActivityEntity = new UserActivityEntity();
			BeanUtils.copyProperties(userActivityDTO, userActivityEntity);
			userActivityRepository.save(userActivityEntity);
			LOGGER.info("End UserActivityServiceImpl saveUserActivity() method");
		}

	}
	
	@Override
	@Async("userActivityExecutor")
	public void saveUserActivityAsync(List<UserActivityDTO> activityList) {

		if (CollectionUtils.isEmpty(activityList)) {
			return;
		}

		try {

			for (UserActivityDTO dto : activityList) {

				saveUserActivity(dto);
			}

		} catch (Exception e) {

			LOGGER.error("Error while saving user activity asynchronously", e);
		}
	}
	
}
