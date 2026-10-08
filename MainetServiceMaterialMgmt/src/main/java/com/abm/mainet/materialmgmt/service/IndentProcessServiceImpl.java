package com.abm.mainet.materialmgmt.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.abm.mainet.common.constant.MainetConstants;
import com.abm.mainet.common.dao.IServiceMasterDAO;
import com.abm.mainet.common.domain.ServiceMaster;
import com.abm.mainet.common.dto.ApplicantDetailDTO;
import com.abm.mainet.common.exception.FrameworkException;
import com.abm.mainet.common.master.repository.EmployeeJpaRepository;
import com.abm.mainet.common.service.CommonService;
import com.abm.mainet.common.service.IEmployeeService;
import com.abm.mainet.common.service.ServiceMasterService;
import com.abm.mainet.common.utility.ApplicationContextProvider;
import com.abm.mainet.common.utility.CommonMasterUtility;
import com.abm.mainet.common.utility.LookUp;
import com.abm.mainet.common.utility.UserSession;
import com.abm.mainet.common.utility.Utility;
import com.abm.mainet.common.workflow.dto.ApplicationMetadata;
import com.abm.mainet.common.workflow.dto.WorkflowProcessParameter;
import com.abm.mainet.common.workflow.dto.WorkflowTaskAction;
import com.abm.mainet.common.workflow.service.IWorkflowExecutionService;
import com.abm.mainet.materialmgmt.dao.GoodsReceivedReceiptDao;
import com.abm.mainet.materialmgmt.dao.IndentProcessDao;
import com.abm.mainet.materialmgmt.dao.ItemOpeningBalanceDao;
import com.abm.mainet.materialmgmt.domain.CommonTransactionEntity;
import com.abm.mainet.materialmgmt.domain.GoodsReceivedItemEntity;
import com.abm.mainet.materialmgmt.domain.GoodsReceivedReceiptEntity;
import com.abm.mainet.materialmgmt.domain.IndentIssueEntity;
import com.abm.mainet.materialmgmt.domain.IndentItemEntity;
import com.abm.mainet.materialmgmt.domain.IndentProcessEntity;
import com.abm.mainet.materialmgmt.domain.ItemOpeningBalanceEntity;
import com.abm.mainet.materialmgmt.dto.BinLocMasDto;
import com.abm.mainet.materialmgmt.dto.GoodReceivedReceiptDto;
import com.abm.mainet.materialmgmt.dto.GoodsReceivedReceiptDetDto;
import com.abm.mainet.materialmgmt.dto.IndentIssueDto;
import com.abm.mainet.materialmgmt.dto.IndentProcessDTO;
import com.abm.mainet.materialmgmt.dto.IndentProcessItemDto;
import com.abm.mainet.materialmgmt.dto.ItemOpeningBalanceDto;
import com.abm.mainet.materialmgmt.repository.GoodsReceivedReceiptRepository;
import com.abm.mainet.materialmgmt.repository.ICommonTransactionRepository;
import com.abm.mainet.materialmgmt.repository.IndentProcessRepository;
import com.abm.mainet.materialmgmt.repository.StoreMasterRepository;
import com.abm.mainet.materialmgmt.ui.model.GoodReceivedReceiptModel;

@Service
public class IndentProcessServiceImpl implements IndentProcessService {

	@Autowired
	private IndentProcessRepository indentProcessRepository;

	@Autowired
	private IndentProcessDao indentProcessDao;

	@Autowired
	private IStoreMasterService storeMasterservice;

	@Resource
	private ServiceMasterService serviceMasterService;

	@Resource
	private IServiceMasterDAO iServiceMasterDAO;

	@Autowired
	private CommonService commonService;

	@Autowired
	private ItemMasterService itemMasterservice;

	@Autowired
	private IEmployeeService employeeService;
	
	@Resource
	private EmployeeJpaRepository employeeJpaRepository;

	@Resource
	private StoreMasterRepository storeMasterRepository;
  
	@Resource
	private ICommonTransactionRepository transactionRepository;
	
	@Autowired
	private GoodsReceivedReceiptRepository goodsReceivedReceiptRepository;
	
	@Autowired
	private GoodsReceivedReceiptDao goodsReceivedReceiptDao;
	
	@SuppressWarnings("unused")
	private static Logger LOGGER = Logger.getLogger(GoodReceivedReceiptModel.class);

	@Override
	@Transactional
	public IndentProcessDTO saveIndentProcess(IndentProcessDTO indentProcessDTO, long levelCheck, WorkflowTaskAction workflowTaskAction) {
		try {
			IndentProcessEntity indentProcessEntity = new IndentProcessEntity();
			BeanUtils.copyProperties(indentProcessDTO, indentProcessEntity);	

			List<IndentItemEntity> indentItemEntitieList = new ArrayList<>();

			if (indentProcessDTO.getItem() != null) {
				indentProcessDTO.getItem().forEach(indentItemDto -> {
					IndentItemEntity indentItemEntity = new IndentItemEntity();
					
					LOGGER.info("Item Group Id: " + indentItemDto.getItemGpId());

					if (indentItemDto.getItemGpId() != null) {
					    indentItemEntity.setItemGpId(indentItemDto.getItemGpId());
					} else {
					    String errorMessage = "Item Group Id is mandatory but found null for Item ID: " + indentItemDto.getItemid();
					    LOGGER.error(errorMessage);
					    throw new IllegalArgumentException(errorMessage); // or a custom exception
					}
					
					BeanUtils.copyProperties(indentItemDto, indentItemEntity);

					indentItemEntity.setIndentid(indentProcessEntity);
					indentItemEntity.setStoreid(indentProcessEntity.getStoreid());

					if (levelCheck == 0L) {
						indentItemEntity.setDepartmentQuantity(indentItemDto.getQuantity());
					}

					List<IndentIssueEntity> indentIssueEntityList = new ArrayList<>();
					if (indentItemDto.getIndentIssueDtoList() != null || !indentItemDto.getIndentIssueDtoList().isEmpty()) {
						indentItemDto.getIndentIssueDtoList().forEach(indentIssueDto -> {
							if (indentIssueDto != null
									&& indentIssueDto.getInditemid() != null
									&& indentIssueDto.getInditemid().equals(indentItemDto.getInditemid())) {
								IndentIssueEntity indentIssueEntity = new IndentIssueEntity();
								BeanUtils.copyProperties(indentIssueDto, indentIssueEntity);
								indentIssueEntity.setIndentid(indentProcessEntity);
								indentIssueEntity.setIndentItemEntity(indentItemEntity);
								indentIssueEntity.setItemid(indentItemDto.getItemid());

								
								BigDecimal issuedQty = indentItemDto.getIssuedqty();
								if (issuedQty != null) {
									indentIssueEntity.setIssueqty(issuedQty.longValue());
								} else {
									String errorMessage = "Item Issue Quantity for level 2 is mandatory for indentIssue table but found null for Item ID: "
											+ indentItemDto.getItemid();
									LOGGER.error(errorMessage);
									throw new IllegalArgumentException(errorMessage); // or a custom exception

								}

								indentIssueEntity.setIsreturned(MainetConstants.FlagN);
								indentIssueEntityList.add(indentIssueEntity);
							}
						});
					}

					indentItemEntity.setIndentIssueEntities(indentIssueEntityList);
					indentItemEntitieList.add(indentItemEntity);
				});
			}

			indentProcessEntity.setItem(indentItemEntitieList);
			indentProcessRepository.saveAndFlush(indentProcessEntity);

			ServiceMaster serviceMas = iServiceMasterDAO.getServiceMasterByShortCode(
					MainetConstants.MaterialManagement.DeptIndentShortCODE, indentProcessDTO.getOrgid());

			if (levelCheck == 2L) {
			    LOGGER.info("Start Saving Data For Level 2");
			    try {
			        List<CommonTransactionEntity> entities = saveDeptIndentDataToTransactionEntity(indentProcessDTO);
			        for (CommonTransactionEntity entity : entities) {
			            transactionRepository.saveAndFlush(entity); // flush after each save
			        }
			        LOGGER.info("Completed Saving Data For Level 2");
			    } catch (Exception e) {
			        LOGGER.error("Error while saving Level 2 transactions: ", e);
			        throw new RuntimeException("Failed to save Level 2 transactions: " + e.getMessage(), e);
			    }
			}
			if (levelCheck == 0L) {
				LOGGER.info("Initiate Work Flow For 0 level");
				initializeWorkFlowForFreeService(indentProcessDTO, serviceMas);
			} else {
				LOGGER.info("Starting Call to BPM for level 1 & 2");
				updateWorkFlowService(workflowTaskAction, serviceMas);
			}

		} catch (Exception e) {
			LOGGER.error("Exception in saveIndentProcess: ", e);
			throw new RuntimeException("Error saving indent process", e);
		}		

		return indentProcessDTO;
	}

	/** Transaction Type : Purchase-P, Indent-I, Store Indent Outward-M, Store Indent
	 	Inward-S Disposal-D, Adjustment-A, Transfer-T, Expired-E, Opening Balance-O,  Returned-R  */
	private List<CommonTransactionEntity> saveDeptIndentDataToTransactionEntity(IndentProcessDTO indentProcessDTO) {
		LOGGER.info("Indent Process Item DTO: {}" + indentProcessDTO.getItem().toString());
		List<CommonTransactionEntity> transactionEntityList = new ArrayList<>();
		indentProcessDTO.getItem().forEach(itemDetail -> {
			Long itemId = itemDetail.getItemid();
			BigDecimal issuedqty = itemDetail.getIssuedqty();
			String indentno = indentProcessDTO.getIndentno();
			Long storeid = indentProcessDTO.getStoreid();

			if (itemDetail.getIndentIssueDtoList() == null || itemDetail.getIndentIssueDtoList().isEmpty()) {
				LOGGER.info("No issue details found for itemId: {}" + itemId);
				return;
			}

			itemDetail.getIndentIssueDtoList().forEach(issueDetail -> {
				CommonTransactionEntity transactionEntity = new CommonTransactionEntity();

				LOGGER.info("Indent No:-" + indentno);
				LOGGER.info("Store Id:-" + storeid);
				LOGGER.info("Item No:-" + issueDetail.getItemno());

				transactionEntity.setTransactionDate(issueDetail.getLmoddate());
				transactionEntity.setTransactionType(MainetConstants.FlagI);
				if (indentno != null) {
					transactionEntity.setReferenceNo(indentno);
				}
				if (storeid != null) {
					transactionEntity.setStoreId(storeid);
				}
				if (itemId != null) {
					transactionEntity.setItemId(itemId);
				}

				if (issueDetail.getBinlocation() != null) {
					transactionEntity.setBinLocation(issueDetail.getBinlocation());
				}

				if (issueDetail.getItemno() != null) {
					transactionEntity.setItemNo(issueDetail.getItemno());
				}

				LOGGER.info("IssueQuantity:-" + issuedqty);
				if (issuedqty != null) {
					transactionEntity.setDebitQuantity(issuedqty);
				} else {
					String errorMessage = "Item Issue Quantity for level 2 is mandatory for transaction table but found null for Item ID: "
							+ itemDetail.getItemid();
					LOGGER.error(errorMessage);
					throw new IllegalArgumentException(errorMessage);
				}

				transactionEntity.setStatus(MainetConstants.FlagY);
				transactionEntity.setDisposalStatus(MainetConstants.FlagN);
				transactionEntity.setOrgId(issueDetail.getORGID());
				transactionEntity.setUserId(issueDetail.getUSER_ID());
				transactionEntity.setLangId(issueDetail.getLANGID());
				transactionEntity.setCreatedDate(issueDetail.getLmoddate());
				transactionEntity.setLgIpMac(issueDetail.getLG_IP_MAC());

				transactionEntityList.add(transactionEntity);
			});
		});
		return transactionEntityList;
	}

	
	@Transactional
	public void initializeWorkFlowForFreeService(IndentProcessDTO indentProcessDTO, ServiceMaster serviceMas) {		
		ApplicantDetailDTO applicantDto = new ApplicantDetailDTO();
		ApplicationMetadata applicationMetaData = new ApplicationMetadata();
		applicantDto.setApplicantFirstName(employeeService.getEmpFullNameById(indentProcessDTO.getUser_id()));
		applicantDto.setServiceId(serviceMas.getSmServiceId());
		applicantDto.setDepartmentId(serviceMas.getTbDepartment().getDpDeptid());
		applicantDto.setMobileNo("NA");
		applicantDto.setUserId(indentProcessDTO.getUser_id());
		applicationMetaData.setReferenceId(indentProcessDTO.getIndentno());
		applicationMetaData.setIsCheckListApplicable(false);
		applicationMetaData.setOrgId(indentProcessDTO.getOrgid());
		try {
			commonService.initiateWorkflowfreeService(applicationMetaData, applicantDto);
		} catch (Exception e) {
			throw new FrameworkException("Exception occured while calling workflow");
		}
	}
	

	@Override
	@Transactional
	public void updateWorkFlowService(WorkflowTaskAction workflowTaskAction, ServiceMaster serviceMas) {
		WorkflowProcessParameter workflowProcessParameter = new WorkflowProcessParameter();
		workflowProcessParameter.setProcessName(serviceMasterService.getProcessName(serviceMas.getSmServiceId(), serviceMas.getOrgid()));
		workflowProcessParameter.setWorkflowTaskAction(workflowTaskAction);
		try {
			ApplicationContextProvider.getApplicationContext().getBean(IWorkflowExecutionService.class).updateWorkflow(workflowProcessParameter);
		} catch (Exception exception) {
			throw new FrameworkException("Exception  Occured when Update Workflow methods", exception);
		}
	}
	

	@Override
	public List<IndentProcessDTO> searchIndentByStoreName(Long storeid, String indentno, Long deptId, Long indenter,
			String status, Long orgid) {
		List<Object[]> indentDataList = indentProcessDao.getIndentSummaryList(storeid, indentno, deptId, indenter,
				status, orgid);
		List<IndentProcessDTO> indentProcessDTOList = new ArrayList<IndentProcessDTO>();
		IndentProcessDTO indentProcessDTO;
		String[] indentDate;
		for(Object[] indentData : indentDataList) {
			indentProcessDTO = new IndentProcessDTO();
			indentProcessDTO.setIndentid(Long.valueOf(indentData[0].toString()));
			indentProcessDTO.setIndentno(indentData[1].toString());			
			indentDate = indentData[2].toString().split(MainetConstants.WHITE_SPACE);
			indentProcessDTO.setIndentdate(Utility.stringToDate(
					LocalDate.parse(indentDate[0]).format(DateTimeFormatter.ofPattern(MainetConstants.DATE_FORMAT))));
			indentProcessDTO.setIndenterName(employeeService.getEmpFullNameById(Long.valueOf(indentData[3].toString())));
			indentProcessDTO.setStoreDesc(storeMasterservice.getStorenameByStoreId(Long.valueOf(indentData[4].toString())));
			indentProcessDTO.setStatus(indentData[5].toString());
			indentProcessDTOList.add(indentProcessDTO);
		}
		return indentProcessDTOList;
	}


	@Override
	@Transactional
	public IndentProcessDTO getIndentDataByIndentId(Long indentId, Long orgId) {
		IndentProcessEntity indentProcessEntity = indentProcessRepository.findByIndentidAndOrgid(indentId, orgId);		
		return getIndentEntityToIndentDto(indentProcessEntity, orgId);
	}
	

	@Override
	@Transactional
	public IndentProcessDTO getIndentDataByIndentNo(String indentNo, Long orgId) {
		IndentProcessEntity indentProcessEntity = indentProcessRepository.findByIndentNo(indentNo, orgId);	
		return getIndentEntityToIndentDto(indentProcessEntity, orgId);
	}
	
	
	/**
	 * To map IndentProcessEntity to IndentProcessDTO
	 * @param IndentProcessEntity
	 * @return IndentProcessDTO
	 */
	public IndentProcessDTO getIndentEntityToIndentDto(IndentProcessEntity indentProcessEntity, Long orgId) {
		IndentProcessDTO indentProcessDTO = new IndentProcessDTO();
		BeanUtils.copyProperties(indentProcessEntity, indentProcessDTO);
		
		final Object[] empObject = employeeJpaRepository.getEmployeDetailsById(indentProcessDTO.getIndenter(), orgId);
		Object[] employee = (Object[]) empObject[0];
		indentProcessDTO.setDeptName(employee[1].toString());
		indentProcessDTO.setDesgName(employee[3].toString());		
		if(null !=  employee[5] && null != employee[6])
			indentProcessDTO.setReportingMgrName(employee[5].toString()+ MainetConstants.WHITE_SPACE + employee[6].toString());
				
		Object[] storeDet = (Object[]) storeMasterRepository.getStoreDetailsByStore(indentProcessDTO.getStoreid(), orgId)[0];
		indentProcessDTO.setLocationName(storeDet[1].toString());
		indentProcessDTO.setStoreDesc(storeDet[2].toString());
		
		List<IndentProcessItemDto> indentProcessItemDtoList = new ArrayList<>();
		indentProcessEntity.getItem().forEach(itemsEntiry -> {
			IndentProcessItemDto indentItemDto = new IndentProcessItemDto();
			BeanUtils.copyProperties(itemsEntiry, indentItemDto);
						
			Object[] itemObj = (Object[]) itemMasterservice.getItemDetailObjectByItemId(indentItemDto.getItemid())[0];
			indentItemDto.setItemName(itemObj[1].toString());
			indentItemDto.setUom(CommonMasterUtility.getNonHierarchicalLookUpObjectByPrefix(
					Long.valueOf(itemObj[2].toString()), orgId, "UOM").getLookUpDesc());	
			LookUp managementLookup = CommonMasterUtility.getNonHierarchicalLookUpObjectByPrefix(Long.valueOf(itemObj[3].toString()), orgId, "IMM");			
			indentItemDto.setManagementCode(managementLookup.getLookUpCode());
			indentItemDto.setManagementDesc(managementLookup.getLookUpDesc());
			
			List<IndentIssueDto> indentIssueDtoList = new ArrayList<>();
			itemsEntiry.getIndentIssueEntities().forEach(issueEntity -> {
				if (issueEntity.getIndentItemEntity().getInditemid() == indentItemDto.getInditemid()) {
					IndentIssueDto indentIssueDto = new IndentIssueDto();
					BeanUtils.copyProperties(issueEntity, indentIssueDto);
					indentIssueDtoList.add(indentIssueDto);
				}
			});
			indentItemDto.setIndentIssueDtoList(indentIssueDtoList);
			indentProcessItemDtoList.add(indentItemDto);
		});
		indentProcessDTO.setItem(indentProcessItemDtoList);
		return indentProcessDTO;
	}
	

	
	@Override
	@Transactional
	public void updateIndentStatus(Long orgId, String indentNo, String status, String wfFlag) {
		indentProcessRepository.updateIndentStatus(orgId, indentNo, status, wfFlag);
	}
	
	
	@Override
	@Transactional
	public List<IndentProcessDTO> findIndentByorgId(Long orgId) {
		List<IndentProcessEntity> entityList = indentProcessRepository.findIndentByorgId(orgId);
		List<IndentProcessDTO> indentProcessDTOList = new ArrayList<>();
		entityList.forEach(entity->{
			IndentProcessDTO indentProcessDTO = new IndentProcessDTO();
			if (entity != null) {
				BeanUtils.copyProperties(entity, indentProcessDTO);
				if(indentProcessDTO.getStoreid() != null && indentProcessDTO.getIndenter() != null) {
					indentProcessDTO.setStoreDesc(storeMasterservice.getStoreMasterByStoreId(entity.getStoreid()).getStoreName());
					indentProcessDTO.setIndenterName(employeeService.findEmployeeById(entity.getIndenter()).getEmpname());
				}
			}			
			indentProcessDTOList.add(indentProcessDTO);
		});
		return indentProcessDTOList;
	}
	
	
	/**To Get Bin Location List based on StoreId and ItemID*/
	@Override
	@Transactional(readOnly = true)
	public List<BinLocMasDto> findAllBinLocationByItemID(Long orgId, Long storeId, Long itemId) {
		//List<Object[]> entitiesList = indentProcessRepository.findByOrgIdAndItemId(orgId,storeId, itemId);
		List<Object[]> entitiesList = transactionRepository.fetchBinLocationListForIndent(orgId, storeId, itemId);		
		List<BinLocMasDto> binLocMasDtoList = new ArrayList<>();
		BinLocMasDto binLocMasDto;
		for (Object[] obj : entitiesList) {
			binLocMasDto = new BinLocMasDto();
				if (null != obj[0])
					binLocMasDto.setBinLocId(Long.valueOf(obj[0].toString()));
				if (null != obj[1]) 
					binLocMasDto.setBinLocation(obj[1].toString());
				binLocMasDtoList.add(binLocMasDto);
			}
		return binLocMasDtoList;
	}
	
	
	/**To Get Item No. list based on Bin Location, ItemID and StoreId */	
	@Override
	public List<String> getItemNumberListByBinLoc(Long binLocation, Long itemid, Long storeId, Long orgId) {
		return transactionRepository.fetchItemNoListForIndent(orgId, storeId, itemid, binLocation);
	}
	
	//Not in use : this is old method before MDN and Return services
	@Override
	public Double getSumOfAvailbleQuantities(Long binLocation, Long itemid, String itemNo, Long storeId, Long orgId) {
		Double availbleQty = indentProcessRepository.getSumOfQuantityByItemNoAndBinLocation(binLocation, itemid, itemNo, storeId,orgId);	
		return availbleQty= availbleQty == null ?0.0 : availbleQty;	
	}
	
	//Not in use : this is old method before MDN and Return services
	@Override
	public Double getSumOfNotInBatchAvailbleQuantities(Long binLocation, Long itemid, Long storeId, Long orgId) {
		Double availbleQty = indentProcessRepository.getSumOfNotInBatchQtyByBin(binLocation, itemid, storeId, orgId);
		return availbleQty= availbleQty == null ?0.0 : availbleQty;
	}
	
	/** To get available quantity based on orgId, storeId, itemid, binLocation, itemNo */
	@Override
	public Double fetchBalanceQuantityForIndent(Long orgId, Long storeId, Long itemid, Long binLocation, String itemNo) {
		LOGGER.info("Find available quantity");
		return transactionRepository.fetchBalanceQuantityForIndent(orgId, storeId, itemid, binLocation, itemNo);
		
	}
	
	@Override
	public Double fetchAvailabelQtyToCompare(Long itemid,Long storeId,Long orgId) {
		return transactionRepository.fetchBinLocationByItmIdAndStrId(itemid, storeId, orgId);		
	}
	
	@Override
	public Double findAvailableQtyForIndent(Long orgId, Long storeId, Long itemid, Long binLocation) {
		return transactionRepository.fetchAvailableQuantity(orgId, storeId, itemid, binLocation);		
	}
	
	@Override
	@Transactional
	public List<Object[]> getGroupItemName(Long itemgroup, Long storeId, Long orgId) {
		return transactionRepository.fetchItemGroupNameById(itemgroup, storeId, orgId);
	}
	
	@Override
	@Transactional
	public String getItemNamebyItmId(Long itemId, Long orgId) {
		return transactionRepository.fetchItemNameByItemId(itemId, orgId);
	}

	@Override
	@Transactional(readOnly = true)
	public List<IndentProcessItemDto> findAvailableQty(IndentProcessDTO indentProcessDto) {
		
		List<IndentProcessItemDto> entitiesList = new ArrayList<>();
		indentProcessDto.getItem().forEach(dto->{
			IndentProcessItemDto item = new IndentProcessItemDto(); 
			BeanUtils.copyProperties(dto, item);
			
			
				Long binLoca= fetchAvailabelQtyToCompare(item.getItemid(),item.getStoreid(),
						indentProcessDto.getOrgid()).longValue();
				LOGGER.info("binLocation=" + binLoca);
				
				Long avlQuantity= findAvailableQtyForIndent(indentProcessDto.getOrgid(),
						item.getStoreid(), item.getItemid(), binLoca).longValue();
				LOGGER.info("avlQuantity=" + avlQuantity);
				item.setAvlQty(avlQuantity.toString());
				entitiesList.add(item);
				
		} );
		
		
		
		return entitiesList;
	}
	
	@Override
	public Double fetchBinLocationByItemId(Long itemid,Long orgId) {
		return transactionRepository.fetchBinLocaByItemId(itemid,orgId);		
	}
	
	@Override
	public String fetchBinLocationName(Long binLocation,Long orgId) {
		return transactionRepository.fetchBinLocationName(binLocation,orgId);		
	}
	
	@Override
	public String fetchVendorNameByVendorId(Long vendorId,Long orgId) {
		return goodsReceivedReceiptRepository.fetchVendorNameByVendorIdOrgId(vendorId,orgId);		
	}
	
	   @Override
	   @Transactional
	   public void saveGoodsReceivedReceipt(GoodReceivedReceiptDto goodsReceivedReceiptDto) {

		    GoodsReceivedReceiptEntity goodReceivedReceiptEntity = new GoodsReceivedReceiptEntity();   //praent entity
		    BeanUtils.copyProperties(goodsReceivedReceiptDto, goodReceivedReceiptEntity);
		    List<GoodsReceivedItemEntity> goodsReceivedItemEntityList = new ArrayList<>();            //child entity
				
			goodsReceivedReceiptDto.getGoodsReceivedReceiptDetDto().forEach(goodsReceiptDetDto ->{
				GoodsReceivedItemEntity goodsReceivedItemEntity = new GoodsReceivedItemEntity();
				BeanUtils.copyProperties(goodsReceiptDetDto, goodsReceivedItemEntity);
				goodsReceivedItemEntity.setIndentid(goodReceivedReceiptEntity);
				goodsReceivedItemEntity.setOrgId(goodReceivedReceiptEntity.getOrgId());
				goodsReceivedItemEntity.setUser_id(goodsReceiptDetDto.getUser_id());
				goodsReceivedItemEntity.setLangId(goodsReceiptDetDto.getLangId());
				goodsReceivedItemEntity.setLgIpMac(goodsReceiptDetDto.getLgIpMac());
				goodsReceivedItemEntity.setLmoddate(goodsReceiptDetDto.getLmoddate());
				goodsReceivedItemEntity.setItemId(goodsReceiptDetDto.getItemId());
				goodsReceivedItemEntity.setItemGroup(goodsReceiptDetDto.getItemGroup());
				goodsReceivedItemEntity.setUom(goodsReceiptDetDto.getUom());
				goodsReceivedItemEntity.setLocationName(goodsReceiptDetDto.getLocationName());
				goodsReceivedItemEntity.setReceQuantity(goodsReceiptDetDto.getReceQuantity());
				goodsReceivedItemEntity.setUnPrice(goodsReceiptDetDto.getUnPrice());
				goodsReceivedItemEntity.setTotalValue(goodsReceiptDetDto.getTotalValue());
				
				goodsReceivedItemEntityList.add(goodsReceivedItemEntity);
				
			});
			
			goodReceivedReceiptEntity.setGoodsItem(goodsReceivedItemEntityList);
			goodsReceivedReceiptRepository.save(goodReceivedReceiptEntity);
			
			
		}
	
	    @Override
		@Transactional(readOnly=true)
		public List<GoodReceivedReceiptDto> findByOrgId(Long orgId) {
			List<GoodsReceivedReceiptEntity> list = goodsReceivedReceiptRepository.findByOrgId(orgId);
			List<GoodReceivedReceiptDto> dtoList =new ArrayList<>();
			
			GoodReceivedReceiptDto goodReceivedReceiptDto;
			for (GoodsReceivedReceiptEntity goodsReceivedReceiptEntity : list) {
				goodReceivedReceiptDto =new GoodReceivedReceiptDto();
				goodReceivedReceiptDto.setVendorName(goodsReceivedReceiptEntity.getVendorName());
				goodReceivedReceiptDto.setRequisitionNo(goodsReceivedReceiptEntity.getRequisitionNo());
				goodReceivedReceiptDto.setReceivedReceiptDate(goodsReceivedReceiptEntity.getReceivedReceiptDate());
				goodReceivedReceiptDto.setIndentid(goodsReceivedReceiptEntity.getIndentid());
			
				Date receivedDate = goodsReceivedReceiptEntity.getReceivedReceiptDate();
			    if (receivedDate != null) {
			        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
			        String formattedDate = formatter.format(receivedDate);
			        goodReceivedReceiptDto.setReceivedDate(formattedDate);
			    } else {
			        goodReceivedReceiptDto.setReceivedDate(null); // Optional: handle null case
			    }
			 
				
				dtoList.add(goodReceivedReceiptDto);
				}
			return dtoList;
		}
	   
	   
	   @Override
		public List<GoodReceivedReceiptDto> serchByGoodsReceivedDataByIds(Long vendorId, String requisitionNo, Long orgId) {
			
			List<GoodsReceivedReceiptEntity> entities = goodsReceivedReceiptDao.getGoodsSummaryList(vendorId, requisitionNo, orgId);
			
			List<GoodReceivedReceiptDto> dtoList =new ArrayList<>();
			BeanUtils.copyProperties(entities, dtoList);
			GoodReceivedReceiptDto goodsReceivedReceiptDto;
			for (GoodsReceivedReceiptEntity goodsReceivedEntity : entities) {
				goodsReceivedReceiptDto =new GoodReceivedReceiptDto();
				goodsReceivedReceiptDto.setVendorName(goodsReceivedEntity.getVendorName());
				goodsReceivedReceiptDto.setRequisitionNo(goodsReceivedEntity.getRequisitionNo());
				Date receivedDate= goodsReceivedEntity.getReceivedReceiptDate();
				if (receivedDate != null && StringUtils.isNotEmpty(receivedDate.toString())) {
					SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
					String formattedDate = formatter.format(receivedDate);
					LOGGER.info("Formatted Order Date=" + formattedDate);
					goodsReceivedReceiptDto.setReceivedDate(formattedDate);
				}
				dtoList.add(goodsReceivedReceiptDto);
				}
			return dtoList;
		}
	
	@Override
	@Transactional(readOnly = true)
	public GoodReceivedReceiptDto viewGoodsReceived(String requisitionNo, Long orgId) {

		GoodsReceivedReceiptEntity goodsReceivedReceiptEntity = goodsReceivedReceiptRepository
				.findByRequisitionNoAndOrgId(requisitionNo, orgId);
		GoodReceivedReceiptDto goodReceivedReceiptDto = new GoodReceivedReceiptDto();
		/*
		 * BeanUtils.copyProperties(goodsReceivedReceiptEntity, goodReceivedReceiptDto);
		 */

		Date receivedDate = goodsReceivedReceiptEntity.getReceivedReceiptDate();
		LOGGER.info("Order Date=" + receivedDate);
		if (receivedDate != null && StringUtils.isNotEmpty(receivedDate.toString())) {
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
			String formattedDate = formatter.format(receivedDate);
			LOGGER.info("Formatted Order Date=" + formattedDate);
			goodReceivedReceiptDto.setReceivedDate(formattedDate);
		}

		if (goodsReceivedReceiptEntity.getRequisitionNo() != null) {
			LOGGER.info("Requisition No. =" + goodsReceivedReceiptEntity.getRequisitionNo());
			goodReceivedReceiptDto.setRequisitionNo(goodsReceivedReceiptEntity.getRequisitionNo());
		}
		/*
		 * if (goodsReceivedReceiptEntity.getVendorId() != null) {
		 * goodReceivedReceiptDto.setVendorId(goodsReceivedReceiptEntity.getVendorId());
		 * }
		 */
		if (goodsReceivedReceiptEntity.getVendorName() != null) {
			LOGGER.info("Vendor Name =" + goodsReceivedReceiptEntity.getVendorName());
			goodReceivedReceiptDto.setVendorName(goodsReceivedReceiptEntity.getVendorName());
		}
		if (goodsReceivedReceiptEntity.getChallanDate() != null) {
			LOGGER.info("Challan Date =" + goodsReceivedReceiptEntity.getChallanDate());
			goodReceivedReceiptDto.setChallanDate(goodsReceivedReceiptEntity.getChallanDate());
		}
		if (goodsReceivedReceiptEntity.getChallanNo() != null) {
			LOGGER.info("Challan No. =" + goodsReceivedReceiptEntity.getChallanNo());
			goodReceivedReceiptDto.setChallanNo(goodsReceivedReceiptEntity.getChallanNo());
		}
		
		if (goodsReceivedReceiptEntity.getStoreId() != null) {
			String storeName = transactionRepository
					.findStoreNmeByStoreIdAndOrgId(goodsReceivedReceiptEntity.getStoreId(), orgId);
			LOGGER.info("Store Name. =" + storeName);
			goodReceivedReceiptDto.setStoreName(storeName);
		}
		List<GoodsReceivedReceiptDetDto> itemDtoList = new ArrayList<>();

		goodsReceivedReceiptEntity.getGoodsReceivedItemEntity().forEach(goodsReceiptItemEntity -> {
			GoodsReceivedReceiptDetDto goodsReceivedReceiptDetDto = new GoodsReceivedReceiptDetDto();
			goodsReceivedReceiptDetDto.setItemId(goodsReceiptItemEntity.getItemId());
			if (goodsReceiptItemEntity.getItemId() != null) {
				LOGGER.info("ItemId=" + goodsReceiptItemEntity.getItemId());
				String itemName = transactionRepository.fetchItemNameByItemId(goodsReceiptItemEntity.getItemId(),
						orgId);
				LOGGER.info("Item Name=" + itemName);
				goodsReceivedReceiptDetDto.setItemName(itemName);
			} else {
				LOGGER.info("ItemId can not be null");
			}
			if (goodsReceiptItemEntity.getItemGroup() != null) {
				LOGGER.info("ItemGrpId=" + goodsReceiptItemEntity.getItemGroup());
				String itemGrpName = transactionRepository
						.findItemGrpNamesBygrpIdAndOrgId(goodsReceiptItemEntity.getItemGroup(), orgId);
				LOGGER.info("Item Group Name=" + itemGrpName);
				goodsReceivedReceiptDetDto.setItemGroupName(itemGrpName);
			} else {
				LOGGER.info("Item Group Id can not be null");
			}
			goodsReceivedReceiptDetDto.setItemGroup(goodsReceiptItemEntity.getItemGroup());
			goodsReceivedReceiptDetDto.setUom(goodsReceiptItemEntity.getUom());
			goodsReceivedReceiptDetDto.setLocationName(goodsReceiptItemEntity.getLocationName());
			goodsReceivedReceiptDetDto.setReceQuantity(goodsReceiptItemEntity.getReceQuantity());
			goodsReceivedReceiptDetDto.setUnPrice(goodsReceiptItemEntity.getUnPrice());
			goodsReceivedReceiptDetDto.setTotalValue(goodsReceiptItemEntity.getTotalValue());

			itemDtoList.add(goodsReceivedReceiptDetDto);

		});

		goodReceivedReceiptDto.setGoodsReceivedReceiptDetDto(itemDtoList);

		return goodReceivedReceiptDto;

	}
	   
	   	   
	   @Override
		public Double findGoodsDuplicateRecord(String requisitionNo, Long orgId) {
			return transactionRepository.findGoodsDuplicateRecordByOrderNoAndOrgId(requisitionNo,orgId);		
		}
	
	   @Override
		@Transactional
		public List<Object[]> getGroupNames(Long orgId, Long storeId) {
			return transactionRepository.fetchItemGrpIdbyOrgIdAndStoreId(orgId, storeId);
		}

}
