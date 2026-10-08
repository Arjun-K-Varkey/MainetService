package com.abm.mainet.common.exception.service;

public interface SaveExceptionDetails {

	void saveExcepionDetails(Throwable e, String methodName, String url, String fileName);

	void saveExcepionDetailsWithoutUserSession(Throwable e, String methodName, String url, String fileName, Long empId,
			String igIpmac, Long orgId);



}
