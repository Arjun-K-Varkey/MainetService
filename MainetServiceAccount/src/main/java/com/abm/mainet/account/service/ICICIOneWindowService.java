package com.abm.mainet.account.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;

import com.abm.mainet.common.integration.acccount.dto.ICICIOneWindowTransactionDTO;

public interface ICICIOneWindowService {

	ResponseEntity<?> processCallback(ICICIOneWindowTransactionDTO transactionDTO, HttpServletRequest request);

}
