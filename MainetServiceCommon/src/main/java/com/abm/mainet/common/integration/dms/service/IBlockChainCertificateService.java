package com.abm.mainet.common.integration.dms.service;

import com.abm.mainet.common.integration.dms.dto.BlockChainCertificateRequestDTO;
import com.abm.mainet.common.integration.dms.dto.BlockChainCertificateResponseDTO;

/**
 * @author Saroj.Tambe
 *
 */
public interface IBlockChainCertificateService {
	
	BlockChainCertificateResponseDTO issueCertificate(BlockChainCertificateRequestDTO requestDTO);

	BlockChainCertificateResponseDTO reissueCertificate(BlockChainCertificateRequestDTO requestDTO);

	BlockChainCertificateResponseDTO issueMarriageMCertificate(BlockChainCertificateRequestDTO requestDTO);

	BlockChainCertificateResponseDTO issueDuplicateLicenseCertificate(BlockChainCertificateRequestDTO requestDTO);

	BlockChainCertificateResponseDTO issueDeathCertificate(BlockChainCertificateRequestDTO requestDTO);

	BlockChainCertificateResponseDTO reissueDeathCertificate(BlockChainCertificateRequestDTO requestDTO);

	BlockChainCertificateResponseDTO issueNewLicenseCertificate(BlockChainCertificateRequestDTO requestDTO);

}
