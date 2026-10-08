package com.abm.mainet.common.master.service;

import java.util.List;

import javax.jws.WebService;

import com.abm.mainet.common.domain.BankMasterEntity;
import com.abm.mainet.common.master.dto.BankMasterDTO;
import com.abm.mainet.common.master.dto.BankMasterUploadDTO;
@WebService
public interface BankMasterService {

    public BankMasterEntity isCombinationIfscCodeExists(String ifsc);

    public BankMasterEntity isCombinationBranchNameExists(String bank, String branch);

    public BankMasterDTO saveBankMasterFormData(BankMasterDTO tbBankMaster);

    public List<BankMasterDTO> findByAllGridData();

    public List<BankMasterDTO> findByAllGridSearchData(String bank, String branch, String ifsc, String micr, String city);

    public BankMasterDTO getDetailsUsingBankId(BankMasterDTO bankMasterDTO);

    public List<BankMasterEntity> getBankList();

    public List<Object[]> getChequeDDNoBankAccountNames(Long bankAccountId);

    public BankMasterEntity isDuplicateMICR(String micr);

	public void saveBankMasterExcelData(BankMasterUploadDTO bankMasterUploadDto, Long orgId, int langId);
	
	public List<BankMasterDTO> getBankListDto();
	
	BankMasterDTO getBankByMicrCode(String micr);

	public String getBankById(Long bankId);
	
	public List<String> getBankDetails();

}
