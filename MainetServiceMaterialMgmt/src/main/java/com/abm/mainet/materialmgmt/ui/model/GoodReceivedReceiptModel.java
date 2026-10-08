package com.abm.mainet.materialmgmt.ui.model;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.abm.mainet.common.constant.MainetConstants;
import com.abm.mainet.common.dao.IServiceMasterDAO;
import com.abm.mainet.common.domain.FinancialYear;
import com.abm.mainet.common.domain.ServiceMaster;
import com.abm.mainet.common.dto.TbAcVendormaster;
import com.abm.mainet.common.integration.dms.domain.AttachDocs;
import com.abm.mainet.common.integration.dms.dto.FileUploadDTO;
import com.abm.mainet.common.integration.dms.fileUpload.FileUploadUtility;
import com.abm.mainet.common.integration.dms.service.IFileUploadService;
import com.abm.mainet.common.integration.dto.DocumentDetailsVO;
import com.abm.mainet.common.master.service.TbAcVendormasterService;
import com.abm.mainet.common.master.service.TbFinancialyearService;
import com.abm.mainet.common.ui.model.AbstractFormModel;
import com.abm.mainet.common.utility.ApplicationSession;
import com.abm.mainet.common.utility.LookUp;
import com.abm.mainet.common.utility.SeqGenFunctionUtility;
import com.abm.mainet.common.utility.UserSession;
import com.abm.mainet.common.utility.Utility;
import com.abm.mainet.common.workflow.dto.WorkflowTaskAction;
import com.abm.mainet.common.workflow.service.IWorkflowRequestService;
import com.abm.mainet.common.workflow.ui.validator.CheckerActionValidator;
import com.abm.mainet.materialmgmt.dto.BinLocMasDto;
import com.abm.mainet.materialmgmt.dto.GoodReceivedReceiptDto;
import com.abm.mainet.materialmgmt.dto.GoodsReceivedReceiptDetDto;
import com.abm.mainet.materialmgmt.dto.MaterialDispatchNoteDTO;
import com.abm.mainet.materialmgmt.dto.MaterialDispatchNoteItemsEntryDTO;
import com.abm.mainet.materialmgmt.dto.PurchaseOrderDto;
import com.abm.mainet.materialmgmt.service.IMaterialDispatchNoteService;
import com.abm.mainet.materialmgmt.service.IndentProcessService;
import com.abm.mainet.materialmgmt.ui.validator.MaterialDispatchNoteValidator;
import com.abm.mainet.materialmgmt.utility.MainetMMConstants;

@Component
@Scope("session")
public class GoodReceivedReceiptModel extends AbstractFormModel {

	@Resource
	private TbFinancialyearService financialyearService;
	
	@Autowired
	private IndentProcessService indentProcessService;

	@Resource
	private SeqGenFunctionUtility seqGenFunctionUtility;

	@Resource
	private IMaterialDispatchNoteService materialDispatchNoteService;

	@Resource
	private IServiceMasterDAO iServiceMasterDAO;

	@Resource
	private IWorkflowRequestService iWorkflowRequestService;
	
	@Resource
	private TbAcVendormasterService tbAcVendormasterService;
	
	@Autowired
	IFileUploadService fileUpload;
	
	private List<DocumentDetailsVO> documents = new ArrayList<>();
	
	private List<DocumentDetailsVO> attachments = new ArrayList<>();

	private static final long serialVersionUID = 1L;

	private MaterialDispatchNoteDTO dispatchNoteDTO = new MaterialDispatchNoteDTO();

	private List<MaterialDispatchNoteDTO> dispatchNoteDTOList = new ArrayList<>();

	private List<Object[]> storeIdNameList;

	private List<Object[]> itemIdNameList;

	private List<Object[]> storeIndentList;

	private boolean lastChecker;

	private long levelCheck;

	private int indexCount;

	private String management;

	private String saveMode;

	private BigDecimal remainQtyToBeIssued;

	private MaterialDispatchNoteItemsEntryDTO noteItemsEntryDTO = new MaterialDispatchNoteItemsEntryDTO();

	private List<MaterialDispatchNoteItemsEntryDTO> noteItemsEntryDTOList = new ArrayList<>();

	private Map<Long, List<MaterialDispatchNoteItemsEntryDTO>> noteItemsEntryMap = new LinkedHashMap<>();

	private List<String> itemNumberList = new ArrayList<>();

	private Map<String, List<String>> itemNumbersMap = new LinkedHashMap<>();

	private List<BinLocMasDto> binLocList = new ArrayList<>();

	private List<Object[]> binLocObjectList;

	List<GoodReceivedReceiptDto> goodsReceiptDto = new ArrayList<>();
	GoodReceivedReceiptDto goodreceivedDto = new GoodReceivedReceiptDto();
	List<GoodReceivedReceiptDto> goodsReceivedReceiptDto;
	
	GoodsReceivedReceiptDetDto goodreceivedDetDto = new GoodsReceivedReceiptDetDto();
	List<PurchaseOrderDto> purchaseOrderDtoList = new ArrayList<>();
	private List<TbAcVendormaster> vendors = new ArrayList<>();

	List<LookUp> usageType = new ArrayList<LookUp>();
	
	private List<AttachDocs> attachDocsList = new ArrayList<>();
	private List<Object[]> storeObjectList;
	
	

	public List<TbAcVendormaster> getVendors() {
		return vendors;
	}

	public void setVendors(List<TbAcVendormaster> vendors) {
		this.vendors = vendors;
	}

	public List<PurchaseOrderDto> getPurchaseOrderDtoList() {
		return purchaseOrderDtoList;
	}

	public void setPurchaseOrderDtoList(List<PurchaseOrderDto> purchaseOrderDtoList) {
		this.purchaseOrderDtoList = purchaseOrderDtoList;
	}

	@SuppressWarnings("unused")
	private static Logger LOGGER = Logger.getLogger(GoodReceivedReceiptModel.class);

	@Override
	public boolean saveForm() {
		boolean status = false;

			Long orgId = UserSession.getCurrent().getOrganisation().getOrgid();
			Long empID = UserSession.getCurrent().getEmployee().getEmpId();
			Date sysDate = new Date();
			Long langId = Long.valueOf(UserSession.getCurrent().getLanguageId());
			String macAddr = UserSession.getCurrent().getEmployee().getEmppiservername();
			
			if (null == goodreceivedDto.getIndentid()) {
				
				goodreceivedDto.setOrgId(orgId);
				goodreceivedDto.setUser_id(empID);
				goodreceivedDto.setLgIpMac(macAddr);
				goodreceivedDto.setLmoddate(sysDate);
				goodreceivedDto.setLangId(langId);
				
			if (goodreceivedDto.getGoodsReceivedReceiptDetDto() != null) {
				goodreceivedDto.getGoodsReceivedReceiptDetDto().forEach(goodsItemDto -> {
					goodsItemDto.setOrgid(orgId);
					goodsItemDto.setUser_id(empID);
					goodsItemDto.setLgIpMac(macAddr);
					goodsItemDto.setLangId(langId);
					goodsItemDto.setLmoddate(sysDate);

				});
			}
				
				String vendorName =indentProcessService.fetchVendorNameByVendorId(goodreceivedDto.getVendorId(),orgId);
				goodreceivedDto.setVendorName(vendorName);
				LOGGER.info("Going call for save goods received receipt data for service");
				indentProcessService.saveGoodsReceivedReceipt(goodreceivedDto);
				setSuccessMessage(ApplicationSession.getInstance().getMessage("material.goods.receivedReceipt"));
				LOGGER.info("End execution of Item Opening Balance saveForm"+UserSession.getCurrent().getOrganisation().getOrgid());
				status = true;
				
			
			/*********** File Upload *************/
			List<Long> removeFileById = null;
			String fileId = goodreceivedDto.getRemoveFileById();
			if (fileId != null && !fileId.isEmpty()) {
				removeFileById = new ArrayList<>();
				String fileArray[] = fileId.split(MainetConstants.operator.COMMA);
				for (String fields : fileArray)
					removeFileById.add(Long.valueOf(fields));
			}
			if (removeFileById != null && !removeFileById.isEmpty())
				tbAcVendormasterService.updateUploadedFileDeleteRecords(removeFileById, goodreceivedDto.getUser_id());

			FileUploadDTO requestDTO = new FileUploadDTO();
			requestDTO.setOrgId(UserSession.getCurrent().getOrganisation().getOrgid());
			requestDTO.setStatus(MainetConstants.FlagA);
			requestDTO.setIdfId(MainetConstants.DEPT_SHORT_NAME.STORE + MainetConstants.WINDOWS_SLASH + goodreceivedDto.getRequisitionNo());
			requestDTO.setDepartmentName(MainetConstants.DEPT_SHORT_NAME.STORE);
			requestDTO.setUserId(UserSession.getCurrent().getEmployee().getEmpId());
			List<DocumentDetailsVO> dto = getDocuments();
			setDocuments(fileUpload.setFileUploadMethod(getDocuments()));
			setAttachments(fileUpload.setFileUploadMethod(getAttachments()));
			fileUpload.doMasterFileUpload(getAttachments(), requestDTO);
			int i = 0;
			for (final Map.Entry<Long, Set<File>> entry : FileUploadUtility.getCurrent().getFileMap().entrySet()) {
				getDocuments().get(i).setDoc_DESC_ENGL(dto.get(entry.getKey().intValue()).getDoc_DESC_ENGL());
				i++;
			}
			}
		return status;
	}
	

	

	public MaterialDispatchNoteDTO getDispatchNoteDTO() {
		return dispatchNoteDTO;
	}

	public void setDispatchNoteDTO(MaterialDispatchNoteDTO dispatchNoteDTO) {
		this.dispatchNoteDTO = dispatchNoteDTO;
	}

	public List<MaterialDispatchNoteDTO> getDispatchNoteDTOList() {
		return dispatchNoteDTOList;
	}

	public void setDispatchNoteDTOList(List<MaterialDispatchNoteDTO> dispatchNoteDTOList) {
		this.dispatchNoteDTOList = dispatchNoteDTOList;
	}

	public List<Object[]> getStoreIdNameList() {
		return storeIdNameList;
	}

	public void setStoreIdNameList(List<Object[]> storeIdNameList) {
		this.storeIdNameList = storeIdNameList;
	}

	public List<Object[]> getItemIdNameList() {
		return itemIdNameList;
	}

	public void setItemIdNameList(List<Object[]> itemIdNameList) {
		this.itemIdNameList = itemIdNameList;
	}

	public List<Object[]> getStoreIndentList() {
		return storeIndentList;
	}

	public void setStoreIndentList(List<Object[]> storeIndentList) {
		this.storeIndentList = storeIndentList;
	}

	public boolean isLastChecker() {
		return lastChecker;
	}

	public void setLastChecker(boolean lastChecker) {
		this.lastChecker = lastChecker;
	}

	public long getLevelCheck() {
		return levelCheck;
	}

	public void setLevelCheck(long levelCheck) {
		this.levelCheck = levelCheck;
	}

	public int getIndexCount() {
		return indexCount;
	}

	public void setIndexCount(int indexCount) {
		this.indexCount = indexCount;
	}

	public String getManagement() {
		return management;
	}
	
	

	public List<Object[]> getStoreObjectList() {
		return storeObjectList;
	}

	public void setStoreObjectList(List<Object[]> storeObjectList) {
		this.storeObjectList = storeObjectList;
	}

	public void setManagement(String management) {
		this.management = management;
	}

	public String getSaveMode() {
		return saveMode;
	}

	public void setSaveMode(String saveMode) {
		this.saveMode = saveMode;
	}

	public BigDecimal getRemainQtyToBeIssued() {
		return remainQtyToBeIssued;
	}

	public void setRemainQtyToBeIssued(BigDecimal remainQtyToBeIssued) {
		this.remainQtyToBeIssued = remainQtyToBeIssued;
	}

	public MaterialDispatchNoteItemsEntryDTO getNoteItemsEntryDTO() {
		return noteItemsEntryDTO;
	}

	public void setNoteItemsEntryDTO(MaterialDispatchNoteItemsEntryDTO noteItemsEntryDTO) {
		this.noteItemsEntryDTO = noteItemsEntryDTO;
	}

	public List<MaterialDispatchNoteItemsEntryDTO> getNoteItemsEntryDTOList() {
		return noteItemsEntryDTOList;
	}

	public void setNoteItemsEntryDTOList(List<MaterialDispatchNoteItemsEntryDTO> noteItemsEntryDTOList) {
		this.noteItemsEntryDTOList = noteItemsEntryDTOList;
	}

	public Map<Long, List<MaterialDispatchNoteItemsEntryDTO>> getNoteItemsEntryMap() {
		return noteItemsEntryMap;
	}

	public void setNoteItemsEntryMap(Map<Long, List<MaterialDispatchNoteItemsEntryDTO>> noteItemsEntryMap) {
		this.noteItemsEntryMap = noteItemsEntryMap;
	}

	public List<String> getItemNumberList() {
		return itemNumberList;
	}

	public void setItemNumberList(List<String> itemNumberList) {
		this.itemNumberList = itemNumberList;
	}

	public Map<String, List<String>> getItemNumbersMap() {
		return itemNumbersMap;
	}

	public void setItemNumbersMap(Map<String, List<String>> itemNumbersMap) {
		this.itemNumbersMap = itemNumbersMap;
	}

	public List<BinLocMasDto> getBinLocList() {
		return binLocList;
	}

	public void setBinLocList(List<BinLocMasDto> binLocList) {
		this.binLocList = binLocList;
	}

	public List<Object[]> getBinLocObjectList() {
		return binLocObjectList;
	}

	public void setBinLocObjectList(List<Object[]> binLocObjectList) {
		this.binLocObjectList = binLocObjectList;
	}

	public List<GoodReceivedReceiptDto> getGoodsReceiptDto() {
		return goodsReceiptDto;
	}

	public void setGoodsReceiptDto(List<GoodReceivedReceiptDto> goodsReceiptDto) {
		this.goodsReceiptDto = goodsReceiptDto;
	}

	public List<LookUp> getUsageType() {
		return usageType;
	}

	public void setUsageType(List<LookUp> usageType) {
		this.usageType = usageType;
	}

	public GoodReceivedReceiptDto getGoodreceivedDto() {
		return goodreceivedDto;
	}

	public void setGoodreceivedDto(GoodReceivedReceiptDto goodreceivedDto) {
		this.goodreceivedDto = goodreceivedDto;
	}

	public GoodsReceivedReceiptDetDto getGoodreceivedDetDto() {
		return goodreceivedDetDto;
	}

	public void setGoodreceivedDetDto(GoodsReceivedReceiptDetDto goodreceivedDetDto) {
		this.goodreceivedDetDto = goodreceivedDetDto;
	}

	public List<DocumentDetailsVO> getDocuments() {
		return documents;
	}

	public void setDocuments(List<DocumentDetailsVO> documents) {
		this.documents = documents;
	}

	public List<DocumentDetailsVO> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<DocumentDetailsVO> attachments) {
		this.attachments = attachments;
	}

	public List<GoodReceivedReceiptDto> getGoodsReceivedReceiptDto() {
		return goodsReceivedReceiptDto;
	}

	public void setGoodsReceivedReceiptDto(List<GoodReceivedReceiptDto> goodsReceivedReceiptDto) {
		this.goodsReceivedReceiptDto = goodsReceivedReceiptDto;
	}

	public List<AttachDocs> getAttachDocsList() {
		return attachDocsList;
	}

	public void setAttachDocsList(List<AttachDocs> attachDocsList) {
		this.attachDocsList = attachDocsList;
	}
	
	

}
