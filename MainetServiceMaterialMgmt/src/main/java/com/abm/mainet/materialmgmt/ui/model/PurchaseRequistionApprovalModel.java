package com.abm.mainet.materialmgmt.ui.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.abm.mainet.common.ui.model.AbstractFormModel;
import com.abm.mainet.materialmgmt.dto.PurchaseOrderDto;
import com.abm.mainet.materialmgmt.dto.PurchaseRequistionDto;
import com.abm.mainet.materialmgmt.dto.StoreMasterDTO;

@Component
@Scope("session")
public class PurchaseRequistionApprovalModel extends AbstractFormModel {

	private static final long serialVersionUID = 1L;
	
	private PurchaseRequistionDto purchaseRequistionDto = new PurchaseRequistionDto();

	private PurchaseOrderDto purchaseOrderDto = new PurchaseOrderDto();

	List<StoreMasterDTO> storeNameList = new ArrayList<StoreMasterDTO>();

	List<ItemMasterDTO> itemNameList = new ArrayList<>();

	List<PurchaseRequistionDto> purchaseRequistionList = new ArrayList<>();

	Map<Long, String> vendorCodeMap = new HashMap<>();

	private String successFlag;

	private String saveMode;

	public PurchaseRequistionDto getPurchaseRequistionDto() {
		return purchaseRequistionDto;
	}

	public void setPurchaseRequistionDto(PurchaseRequistionDto purchaseRequistionDto) {
		this.purchaseRequistionDto = purchaseRequistionDto;
	}

	public PurchaseOrderDto getPurchaseOrderDto() {
		return purchaseOrderDto;
	}

	public void setPurchaseOrderDto(PurchaseOrderDto purchaseOrderDto) {
		this.purchaseOrderDto = purchaseOrderDto;
	}

	public List<StoreMasterDTO> getStoreNameList() {
		return storeNameList;
	}

	public void setStoreNameList(List<StoreMasterDTO> storeNameList) {
		this.storeNameList = storeNameList;
	}

	public List<ItemMasterDTO> getItemNameList() {
		return itemNameList;
	}

	public void setItemNameList(List<ItemMasterDTO> itemNameList) {
		this.itemNameList = itemNameList;
	}

	public List<PurchaseRequistionDto> getPurchaseRequistionList() {
		return purchaseRequistionList;
	}

	public void setPurchaseRequistionList(List<PurchaseRequistionDto> purchaseRequistionList) {
		this.purchaseRequistionList = purchaseRequistionList;
	}

	public Map<Long, String> getVendorCodeMap() {
		return vendorCodeMap;
	}

	public void setVendorCodeMap(Map<Long, String> vendorCodeMap) {
		this.vendorCodeMap = vendorCodeMap;
	}

	public String getSuccessFlag() {
		return successFlag;
	}

	public void setSuccessFlag(String successFlag) {
		this.successFlag = successFlag;
	}

	public String getSaveMode() {
		return saveMode;
	}

	public void setSaveMode(String saveMode) {
		this.saveMode = saveMode;
	}
	

}
