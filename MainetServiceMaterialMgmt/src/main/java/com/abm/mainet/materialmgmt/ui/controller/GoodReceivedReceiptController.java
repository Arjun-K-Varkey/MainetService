package com.abm.mainet.materialmgmt.ui.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.abm.mainet.common.constant.MainetConstants;
import com.abm.mainet.common.constant.PrefixConstants;
import com.abm.mainet.common.constant.MainetConstants.AccountConstants;
import com.abm.mainet.common.domain.Organisation;
import com.abm.mainet.common.dto.JsonViewObject;
import com.abm.mainet.common.integration.dms.domain.AttachDocs;
import com.abm.mainet.common.integration.dms.service.IAttachDocsService;
import com.abm.mainet.common.integration.dms.service.IFileUploadService;
import com.abm.mainet.common.master.service.TbAcVendormasterService;
import com.abm.mainet.common.ui.controller.AbstractFormController;
import com.abm.mainet.common.utility.CommonMasterUtility;
import com.abm.mainet.common.utility.UserSession;
import com.abm.mainet.common.workflow.service.IWorkflowTaskService;
import com.abm.mainet.materialmgmt.dao.GoodsReceivedReceiptDao;
import com.abm.mainet.materialmgmt.dto.GoodReceivedReceiptDto;
import com.abm.mainet.materialmgmt.dto.GoodsReceivedReceiptDetDto;
import com.abm.mainet.materialmgmt.dto.IndentIssueDto;
import com.abm.mainet.materialmgmt.dto.ItemOpeningBalanceDto;
import com.abm.mainet.materialmgmt.dto.MaterialDispatchNoteDTO;
import com.abm.mainet.materialmgmt.dto.MaterialDispatchNoteItemsEntryDTO;
import com.abm.mainet.materialmgmt.dto.PurchaseOrderDto;
import com.abm.mainet.materialmgmt.service.IBinMasService;
import com.abm.mainet.materialmgmt.service.IMaterialDispatchNoteService;
import com.abm.mainet.materialmgmt.service.IPurchaseOrderService;
import com.abm.mainet.materialmgmt.service.IStoreIndentService;
import com.abm.mainet.materialmgmt.service.IStoreMasterService;
import com.abm.mainet.materialmgmt.service.IndentProcessService;
import com.abm.mainet.materialmgmt.service.ItemMasterService;
import com.abm.mainet.materialmgmt.ui.model.GoodReceivedReceiptModel;
import com.abm.mainet.materialmgmt.ui.model.MaterialDispatchNoteModel;

@Controller
@RequestMapping(value = { "/GoodReceiveReceiptForm.html" })
public class GoodReceivedReceiptController extends AbstractFormController<GoodReceivedReceiptModel> {

	@Resource
	private IStoreMasterService iStoreMasterService;

	@Resource
	private IStoreIndentService storeIndentService;
	
	@Resource
	private IMaterialDispatchNoteService materialDispatchNoteService;
	
	@Resource
	private IndentProcessService indentProcessService;
	
	@Resource
	private IWorkflowTaskService iWorkflowTaskService;
	
	@Resource
	private IBinMasService binMasService; 
	
	@Autowired
	private IPurchaseOrderService purchaseOrderService;
	
	@Autowired
	private IStoreMasterService storeMasterservice;
	
	@Autowired
	private TbAcVendormasterService tbAcVendormasterService;

	@Resource
	private ItemMasterService itemMasterService;
	
	@Autowired
	private IFileUploadService fileUpload;
	
	@Autowired
	private IAttachDocsService attachDocsService;
	
	@SuppressWarnings("unused")
	private static Logger LOGGER = Logger.getLogger(GoodReceivedReceiptModel.class);
	
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView index(final Model model, final HttpServletRequest httpServletRequest) {
		sessionCleanup(httpServletRequest);
		Long orgId = UserSession.getCurrent().getOrganisation().getOrgid();
		this.getModel().setPurchaseOrderDtoList(purchaseOrderService.searchPurchaseOrderData(null, null, null, null, orgId));
		loadDefaultData(orgId);
		this.getModel().setGoodsReceivedReceiptDto(indentProcessService.findByOrgId(UserSession.getCurrent().getOrganisation().getOrgid()));
		return new ModelAndView("GoodReceivedReceiptAdd", MainetConstants.FORM_NAME, this.getModel());
		
	}
	
	@RequestMapping(params = "addGoodReceivedNote", method = RequestMethod.POST)
	public ModelAndView addGoodReceivedNote(final Model model, final HttpServletRequest httpServletRequest) {
		sessionCleanup(httpServletRequest);
		fileUpload.sessionCleanUpForFileUpload();
		GoodReceivedReceiptModel receiptModel =this.getModel();
		this.getModel().setSaveMode(MainetConstants.WorksManagement.ADD);
		Long orgId = UserSession.getCurrent().getOrganisation().getOrgid();
		Organisation organisation = UserSession.getCurrent().getOrganisation();
        organisation.setOrgid(UserSession.getCurrent().getOrganisation().getOrgid());
        this.getModel().setUsageType(CommonMasterUtility.getLevelData("ITG", 1, organisation));
		this.getModel().setPurchaseOrderDtoList(purchaseOrderService.searchPurchaseOrderData(null, null, null, null, orgId));
		loadDefaultData(orgId);
		this.getModel().setStoreObjectList(storeMasterservice.getActiveStoreObjectListForAdd(organisation.getOrgid()));
		return new ModelAndView("GoodReceivedReceiptForm", MainetConstants.FORM_NAME, this.getModel());
	}
	
	
	@RequestMapping(params = "getGoodsReceiptItemNamesById", method = RequestMethod.POST)
	public @ResponseBody Map<Long, String> getGoodsReceiptItemNamesById(@RequestParam("itemgroup") Long itemgroup,@RequestParam("storeId") Long storeId, final HttpServletRequest request) {
		Map<Long, String> storeDetailMap = new HashMap<>();
		List<Object[]> storeOnject=  indentProcessService.getGroupItemName(itemgroup,storeId,
				UserSession.getCurrent().getOrganisation().getOrgid());
		
		Map<Long, String> resultMap = new LinkedHashMap<>();
		 
        for (Object obj : storeOnject) {
            
            Object[] keyValuePair = (Object[]) obj;
            if (keyValuePair.length == 2) {
                
                Long key = ((Number) keyValuePair[0]).longValue();
                String value = (String) keyValuePair[1];
 
      
                resultMap.put(key, value);
            }
		
	}
        return resultMap;
	}
	
	
	@SuppressWarnings("deprecation")
	private List<PurchaseOrderDto> loadDefaultData(Long orgId) {
		this.getModel().setStoreIdNameList(storeMasterservice.getStoreIdAndNameList(orgId));
		Map<Long, String> storeIdToNameMap = new HashMap<>();
		this.getModel().getStoreIdNameList().forEach(StoreIdName -> {
			storeIdToNameMap.put(Long.valueOf(StoreIdName[0].toString()), StoreIdName[1].toString());
		});

		this.getModel().setVendors(tbAcVendormasterService.getAllActiveVendors(orgId, CommonMasterUtility
				.getValueFromPrefixLookUp(AccountConstants.AC.getValue(), PrefixConstants.VSS).getLookUpId()));
		Map<Long, String> vendorCodeMap = new HashMap<>();
		this.getModel().getVendors().forEach(vendor -> {
			vendorCodeMap.put(vendor.getVmVendorid(), vendor.getVmVendorname());
		});

		this.getModel().getPurchaseOrderDtoList().forEach(purchaseOrderDto -> {
			purchaseOrderDto.setStoreName(storeIdToNameMap.get(purchaseOrderDto.getStoreId()));
			purchaseOrderDto.setVendorName(vendorCodeMap.get(purchaseOrderDto.getVendorId()));
		});
		return this.getModel().getPurchaseOrderDtoList();
	}
	
	@ResponseBody
	@RequestMapping(params = "getGoodReceivedReceiptUomByItemId", method = RequestMethod.POST)
	public String getGoodReceivedReceiptUomByItemId(@RequestParam("itemid") Long itemid, final HttpServletRequest request) {
		return CommonMasterUtility.getNonHierarchicalLookUpObject(itemMasterService.getUomByitemCode(UserSession.getCurrent().getOrganisation().getOrgid(),
				itemid), UserSession.getCurrent().getOrganisation()).getLookUpDesc();
	}
	
	
	@ResponseBody
	@RequestMapping(params = "getUomAndBinLocName", method = RequestMethod.POST)
	public Map<String, Object> getUomAndBinLocName(@RequestParam("itemid") Long itemid, final HttpServletRequest request) {
		bindModel(request);
		
		 String uom = CommonMasterUtility.getNonHierarchicalLookUpObject(itemMasterService.getUomByitemCode(UserSession.getCurrent().getOrganisation().getOrgid(),
					itemid), UserSession.getCurrent().getOrganisation()).getLookUpDesc();
		 
		Long binLocation= indentProcessService.fetchBinLocationByItemId(itemid,
				UserSession.getCurrent().getOrganisation().getOrgid()).longValue();
		
		String binLocName = indentProcessService.fetchBinLocationName(binLocation,
		UserSession.getCurrent().getOrganisation().getOrgid()).toString();
		
		Map<String, Object> object = new LinkedHashMap<String, Object>();
		
	
		object.put(MainetConstants.MaterialManagement.BIN_LOCATION_NAME,binLocName);
		object.put(MainetConstants.MaterialManagement.UOM_NAME_BY_ITEM_ID,uom);
		
		List<Object[]> groupData = itemMasterService.getItemGroupIdAndNames(UserSession.getCurrent().getOrganisation().getOrgid(), itemid);
		Map<Long, String> groups = new LinkedHashMap<>();
		for (Object[] row : groupData) {
	        groups.put(((Number) row[0]).longValue(), row[1].toString());
	    }
		object.put("itemGroups", groups);
		
		return object;
		
	}
	
	@RequestMapping(method = RequestMethod.POST, params = { "saveGoodsReceiptData" })
	public @ResponseBody ModelAndView saveGoodsReceiptData(final Model model, final HttpServletRequest request) {
		bindModel(request);
		if(this.getModel().saveForm())
			 return jsonResult(JsonViewObject.successResult(this.getModel().getSuccessMessage()));
		else {
			ModelAndView modelAndView = null;		
			modelAndView = new ModelAndView("GoodReceivedReceiptForm", MainetConstants.FORM_NAME, this.getModel());
			modelAndView.addObject(BindingResult.MODEL_KEY_PREFIX + MainetConstants.FORM_NAME, getModel().getBindingResult());
			return modelAndView;
		}
		
	
	}
	
	
	@ResponseBody
    @RequestMapping(params = "searchGoodsReceivedReceipt", method = RequestMethod.POST)
    public ModelAndView searchGoodsReceivedReceipt(final HttpServletRequest request,
    		@RequestParam("vendorId") Long vendorId, @RequestParam("requisitionNo") String requisitionNo) {
        sessionCleanup(request);
        Long orgId = UserSession.getCurrent().getOrganisation().getOrgid();
        this.getModel().setGoodsReceivedReceiptDto(indentProcessService.serchByGoodsReceivedDataByIds(vendorId,requisitionNo,UserSession.getCurrent().getOrganisation().getOrgid()));
        this.getModel().setPurchaseOrderDtoList(purchaseOrderService.searchPurchaseOrderData(null, null, null, null, orgId));
		loadDefaultData(orgId);
        return new ModelAndView("goodsReceivedReceipt/showPage", MainetConstants.FORM_NAME, this.getModel());
	}
	
	
	
	@RequestMapping(params = "viewGoodsReceivedReceiptForm", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView viewGoodsReceivedReceiptForm(Model model, @RequestParam("requisitionNo") String requisitionNo,
			final HttpServletRequest httpServletRequest) {
		sessionCleanup(httpServletRequest);
		this.getModel().setSaveMode(MainetConstants.WorksManagement.VIEW);
		Long orgId = UserSession.getCurrent().getOrganisation().getOrgid();
		this.getModel().setPurchaseOrderDtoList(purchaseOrderService.searchPurchaseOrderData(null, null, null, null, orgId));
		loadDefaultData(orgId);	
		this.getModel().setGoodreceivedDto(indentProcessService.viewGoodsReceived(requisitionNo,orgId));
		
		List<GoodsReceivedReceiptDetDto> goodsReceivedReceiptDetDto2 = this.getModel().getGoodreceivedDto().getGoodsReceivedReceiptDetDto();
		this.getModel().setFileSize(goodsReceivedReceiptDetDto2.size());
		
		Organisation organisation = UserSession.getCurrent().getOrganisation();
        organisation.setOrgid(UserSession.getCurrent().getOrganisation().getOrgid());
       // this.getModel().setUsageType(CommonMasterUtility.getLevelData("ITG", 1, organisation));
		
		final List<AttachDocs> attachDocs = attachDocsService.findByCode(orgId,
				(MainetConstants.DEPT_SHORT_NAME.STORE + MainetConstants.WINDOWS_SLASH + this.getModel().getGoodreceivedDto().getRequisitionNo()));
		this.getModel().setAttachDocsList(attachDocs);
		
		this.getModel().setStoreObjectList(storeMasterservice.getActiveStoreObjectListForAdd(organisation.getOrgid()));
		
		return new ModelAndView("GoodReceivedReceiptForm", MainetConstants.FORM_NAME, this.getModel());
	}
	 
	
	@ResponseBody
	@RequestMapping(params = "getDuplicateDataOrNot", method = RequestMethod.POST)
	public Double getDuplicateDataOrNot(@RequestParam("requisitionNo") String requisitionNo, final HttpServletRequest request) {
		bindModel(request);
		LOGGER.info("Requisition No.=" + requisitionNo);
		Double orderNo= indentProcessService.findGoodsDuplicateRecord(requisitionNo,UserSession.getCurrent().getOrganisation().getOrgid());
		LOGGER.info("Goods Order no =" + orderNo);
		
		
		return orderNo;
	}
	
	@RequestMapping(params = "getgoodsReceivedReceiptItemGroup", method = RequestMethod.POST)
	public @ResponseBody Map<Long, String> getgoodsReceivedReceiptItemGroup(@RequestParam("storeId") Long storeId , final HttpServletRequest request) {
		Map<Long, String> storeDetailMap = new HashMap<>();
		
		List<Object[]> goodsReceivedObject =indentProcessService.getGroupNames(UserSession.getCurrent().getOrganisation().getOrgid(),
				storeId);
		
		Map<Long, String> itemResutMap = new LinkedHashMap<>();
		
		 List<Object[]> rawItemList = itemMasterService.getActiveItemIdNameListByOrgId(UserSession.getCurrent().getOrganisation().getOrgid(), storeId);
		
		Map<Long, String> goodsReItmGrpresultMap = new LinkedHashMap<>();
		 
        for (Object obj : rawItemList) {
            
            Object[] keyValuePair = (Object[]) obj;
            if (keyValuePair.length == 2) {
                
                Long key = ((Number) keyValuePair[0]).longValue();
                String value = (String) keyValuePair[1];
 
      
                itemResutMap.put(key, value);
            }
		
		
		
	}
        return itemResutMap;
	}	
		
}
