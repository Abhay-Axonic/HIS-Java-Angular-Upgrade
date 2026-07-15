package com.param.pharmacy.nursing.pharmacy.service.returnBillableConsumption;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.adt.Admission;
import com.param.entity.model.adt.BedMaster;
import com.param.entity.model.adt.WardMasters;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.inventory.BatchUomMapper;
import com.param.entity.model.inventory.StockTransaction;
import com.param.entity.model.inventory.StoreStock;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.UomUnit;
import com.param.entity.model.master.VisitType;
import com.param.entity.model.opd.Encounter;
import com.param.entity.model.patient.PatientRegistration;
import com.param.entity.model.pharmacy.BillableConsumption;
import com.param.entity.model.pharmacy.BillableConsumptionDetail;
import com.param.entity.model.pharmacy.ReturnBillableConsumption;
import com.param.entity.model.pharmacy.ReturnBillableConsumptionDetail;
import com.param.global.billing.controller.BillingException;
import com.param.global.common.ServiceException;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dto.BillingMasterDto;
import com.param.global.dto.OrderDetailsMasterDto;
import com.param.global.dto.global.CommonDropdown;
import com.param.global.pharmacy.dto.BillableConsumptionItem;
import com.param.global.pharmacy.dto.BillableConsumptionSave;
import com.param.global.pharmacy.service.IWalkInSalesService;
import com.param.global.service.global.IGlobalService;
import com.param.global.utility.Util;
import com.param.pharmacy.ip.dto.returnAcceptance.PharmacySaleReturnDto;
import com.param.pharmacy.nursing.pharmacy.dao.returnBillableConsumption.IReturnBillableConsumptionDao;
import com.param.pharmacy.nursing.pharmacy.dto.billableConsumption.BillableConsumptionDetailItem;
import com.param.pharmacy.nursing.pharmacy.dto.billableConsumption.BillableConsumptionDetails;
import com.param.pharmacy.nursing.pharmacy.dto.billableConsumption.BillableConsumptionRequest;
import com.param.pharmacy.nursing.pharmacy.dto.billableConsumption.BillableConsumptionResponse;
import com.param.pharmacy.nursing.pharmacy.dto.billableConsumption.ConsumptionIds;
import com.param.pharmacy.nursing.workstation.dto.patientIssueReturn.PatientIssueReturnSaveItem;
import com.param.service.global.SessionService;

@Service("returnBillableConsumptionServiceService")
@Transactional
public class ReturnBillableConsumptionService implements IReturnBillableConsumptionService {
	@Autowired
	private SessionService sessionService;

	@Autowired
	private IReturnBillableConsumptionDao returnBillableConsumptionDao;

	@Autowired
	private IGlobalDao GlobalDao;

	@Autowired
	IGlobalService GlobalService;

	@Autowired
	private IWalkInSalesService iWalkInSalesService;

	private Integer screenId = Screen.ReturnBillableConsumption.getScreen();

	private static final Logger logger = Logger.getLogger(ReturnBillableConsumptionService.class);

	@Override
	@Transactional
	public CommonDropdown getDropdownList(Integer unitId, Integer orgId, Integer userId) {
		CommonDropdown response = new CommonDropdown();

		for (VisitType visitType : GlobalDao.getVisitTypeList()) {
			response.addVisitType(new com.param.global.dto.global.VisitType(visitType.getVisitTypeId(),
					visitType.getVisitTypeName()));
		}

		for (WardMasters wardMaster : GlobalDao.getWardList(unitId)) {
			response.addWard(new com.param.global.dto.global.Ward(wardMaster.getWardId(), wardMaster.getWardName()));
		}

		for (Store store : GlobalDao.getUserStoreList(unitId,orgId,userId)) {
			response.addStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
		}

		return response;
	}

	@Override
	@Transactional
	public List<BillableConsumptionResponse> returnBillableConsumptionSearch(BillableConsumptionRequest request) {
		List<BillableConsumptionResponse> returnBillableConsumptionResponseList = new ArrayList<BillableConsumptionResponse>();
		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());

		for (ReturnBillableConsumption returnBillableConsumption : returnBillableConsumptionDao
				.returnBillableConsumptionSearch(request, screenId)) {
			Integer age = Util
					.getAgeByBirthDate(returnBillableConsumption.getPatient().getDob().toLocalDateTime().toLocalDate());
//			String patientDetail = returnBillableConsumption.getPatient().getFirstName() + " "
//					+ returnBillableConsumption.getPatient().getMiddleName() + " "
//					+ returnBillableConsumption.getPatient().getLastName() + " | " + age + " | "
//					+ returnBillableConsumption.getPatient().getGender().getGenderCode();
			String patientDetail = returnBillableConsumption.getPatient().getFirstName() + " "
					+ returnBillableConsumption.getPatient().getMiddleName() + " "
					+ returnBillableConsumption.getPatient().getLastName();

			String admiNumber = "";
			if (returnBillableConsumption.getAdmission() != null) {
				admiNumber = returnBillableConsumption.getAdmission().getAdmissionNumber();
			} else if (returnBillableConsumption.getEncounter() != null) {
				admiNumber = returnBillableConsumption.getEncounter().getEncounterNumber();
			}

			BillableConsumptionResponse returnBillableConsumptionResponse = new BillableConsumptionResponse(
					returnBillableConsumption.getId(), returnBillableConsumption.getReturnNumber(),
					returnBillableConsumption.getAddedDate(),
					returnBillableConsumption.getVisitType().getVisitTypeCode(),
					returnBillableConsumption.getStore().getStoreName(),
					returnBillableConsumption.getPatient().getUhidNumber(), patientDetail, age,
					returnBillableConsumption.getPatient().getGender().getGenderCode(),
					returnBillableConsumption.getPatient().getImagePath(),
					returnBillableConsumption.getAddedBy().getUserName(), admiNumber);

			returnBillableConsumptionResponse.setWardName(
					(returnBillableConsumption.getWard() != null) ? returnBillableConsumption.getWard().getWardName()
							: null);
			returnBillableConsumptionResponse.setBedNo(
					(returnBillableConsumption.getBed() != null) ? returnBillableConsumption.getBed().getBedNumber()
							: null);
			returnBillableConsumptionResponse.setPatientId(returnBillableConsumption.getPatient().getPatientId());
			returnBillableConsumptionResponseList.add(returnBillableConsumptionResponse);
		}
		return returnBillableConsumptionResponseList;
	}

	@Override
	@Transactional
	public BillableConsumptionDetails returnBillableConsumptionDetails(Integer id, Integer unitId) {
		BillableConsumptionDetails returnBillableConsumptionDetails = new BillableConsumptionDetails();
		ReturnBillableConsumption returnBillableConsumption = returnBillableConsumptionDao
				.returnBillableConsumptionById(id,unitId);
		Integer age = Util
				.getAgeByBirthDate(returnBillableConsumption.getPatient().getDob().toLocalDateTime().toLocalDate());
		String patientDetail = returnBillableConsumption.getPatient().getFirstName() + " "
				+ returnBillableConsumption.getPatient().getMiddleName() + " "
				+ returnBillableConsumption.getPatient().getLastName() + " | " + age + " | "
				+ returnBillableConsumption.getPatient().getGender().getGenderCode();

		if (returnBillableConsumption.getAdmission() != null) {
			returnBillableConsumptionDetails = new BillableConsumptionDetails(returnBillableConsumption.getId(),
					returnBillableConsumption.getStore().getStoreName(),
					returnBillableConsumption.getVisitType().getVisitTypeCode(),
					returnBillableConsumption.getPatient().getUhidNumber(), patientDetail,
					returnBillableConsumption.getAdmission().getAdmissionNumber());
			returnBillableConsumptionDetails.setReturnNo(returnBillableConsumption.getReturnNumber());
			returnBillableConsumptionDetails.setReturnDate(returnBillableConsumption.getAddedDate());
			returnBillableConsumptionDetails.setWardName(
					(returnBillableConsumption.getWard() != null) ? returnBillableConsumption.getWard().getWardName()
							: null);
			returnBillableConsumptionDetails.setBedNo(
					(returnBillableConsumption.getBed() != null) ? returnBillableConsumption.getBed().getBedNumber()
							: null);
		} else if (returnBillableConsumption.getEncounter() != null) {
			returnBillableConsumptionDetails = new BillableConsumptionDetails(returnBillableConsumption.getId(),
					returnBillableConsumption.getStore().getStoreName(),
					returnBillableConsumption.getVisitType().getVisitTypeCode(),
					returnBillableConsumption.getPatient().getUhidNumber(), patientDetail,
					returnBillableConsumption.getEncounter().getEncounterNumber());
			returnBillableConsumptionDetails.setReturnNo(returnBillableConsumption.getReturnNumber());
			returnBillableConsumptionDetails.setReturnDate(returnBillableConsumption.getAddedDate());
		}
		List<BillableConsumptionDetailItem> items = new ArrayList<BillableConsumptionDetailItem>();

		for (ReturnBillableConsumptionDetail returnBillableConsumptionDetail : returnBillableConsumptionDao
				.returnBillableConsumptionDetailById(id,unitId)) {
			/*
			 * BillableConsumptionDetailItem item = new BillableConsumptionDetailItem(
			 * returnBillableConsumptionDetail.getId(),
			 * returnBillableConsumptionDetail.getBillableConsumptionDetail().getBatch().
			 * getItem().getId(),
			 * returnBillableConsumptionDetail.getBillableConsumptionDetail().getBatch().
			 * getItem().getCode(),
			 * returnBillableConsumptionDetail.getBillableConsumptionDetail().getBatch().
			 * getItem().getName(),
			 * returnBillableConsumptionDetail.getBillableConsumptionDetail().getBatch().
			 * getItem().getGeneric() .getId(),
			 * returnBillableConsumptionDetail.getBillableConsumptionDetail().getBatch().
			 * getItem().getGeneric() .getName(),
			 * returnBillableConsumptionDetail.getBillableConsumptionDetail().getBatch().
			 * getId(),
			 * returnBillableConsumptionDetail.getBillableConsumptionDetail().getBatch().
			 * getBatchNo(),
			 * returnBillableConsumptionDetail.getBillableConsumptionDetail().getBatch().
			 * getExpiryDate(),
			 * returnBillableConsumptionDetail.getBillableConsumptionDetail().getUomType().
			 * getType(),
			 * returnBillableConsumptionDetail.getBillableConsumptionDetail().getUomUnit().
			 * getUnits(), returnBillableConsumptionDetail.getBillableConsumptionDetail().
			 * getConsumableQuantity(), (long)
			 * returnBillableConsumptionDetail.getReturnQuantity());
			 */

			BillableConsumptionDetailItem item = new BillableConsumptionDetailItem(
					returnBillableConsumptionDetail.getId(),
					returnBillableConsumptionDetail.getBillableConsumptionDetail().getBatch().getItem().getId(),
					returnBillableConsumptionDetail.getBillableConsumptionDetail().getBatch().getItem().getCode(),
					returnBillableConsumptionDetail.getBillableConsumptionDetail().getBatch().getItem().getName(),
					returnBillableConsumptionDetail.getBillableConsumptionDetail().getBatch().getItem().getGeneric()
							.getId(),
					returnBillableConsumptionDetail.getBillableConsumptionDetail().getBatch().getItem().getGeneric()
							.getName(),
					returnBillableConsumptionDetail.getBillableConsumptionDetail().getBatch().getId(),
					returnBillableConsumptionDetail.getBillableConsumptionDetail().getBatch().getBatchNo(),
					returnBillableConsumptionDetail.getBillableConsumptionDetail().getBatch().getExpiryDate(),
					returnBillableConsumptionDetail.getBillableConsumptionDetail().getUomType().getType(),
					returnBillableConsumptionDetail.getBillableConsumptionDetail().getUomType().getId(),
					returnBillableConsumptionDetail.getBillableConsumptionDetail().getUomUnit().getUnits(),
					returnBillableConsumptionDetail.getBillableConsumptionDetail().getConsumableQuantity(),
					(long) returnBillableConsumptionDetail.getReturnQuantity(), 
					returnBillableConsumptionDetail.getBillableConsumptionDetail().getBatch().getItem().getIsThreeUOMApplicable(), 
					GlobalDao.getUnitOfMeasurementListByItem(returnBillableConsumptionDetail.getBillableConsumptionDetail().getBatch().getItem().getId(), returnBillableConsumptionDetail.getBillableConsumptionDetail().getOrganizationId()));
			/*
			 * start code for resolved HIS-9214 Return billable consumption to have issues,
			 * consumed and returned columns
			 */
			Long totalConsumedQuantity = returnBillableConsumptionDao
					.getTotalConsumeQuantity(returnBillableConsumptionDetail.getBillableConsumptionDetail().getId(),unitId);
			Long totalReturnedQuantity = returnBillableConsumptionDao
					.getReturnQuantity(returnBillableConsumptionDetail.getBillableConsumptionDetail().getId(),unitId);
			item.setTotalConsumedQuantity(totalConsumedQuantity);
			item.setTotalReturnedQuantity(totalReturnedQuantity);
			item.setConsumptionQuantity(returnBillableConsumptionDetail.getConsumeQuantity());
			/*
			 * end code for resolved HIS-9214 Return billable consumption to have issues,
			 * consumed and returned columns
			 */
			items.add(item);
		}
		returnBillableConsumptionDetails.setItems(items);
		return returnBillableConsumptionDetails;
	}

	@Override
	@Transactional
	public CommonResponse returnBillableConsumptionSave(BillableConsumptionSave request)
			throws BillingException, Exception {

		/*
		 * Integer storeId = sessionService.getStoreId(); Store storeNew =
		 * GlobalDao.getStore(storeId); Unit unit =
		 * GlobalDao.getUnit(sessionService.getUnitId());
		 */
		
		HashMap<Integer,List<BillableConsumptionItem>> clubItems = new HashMap<Integer,List<BillableConsumptionItem>>();
		for (BillableConsumptionItem item : request.getItems()) {
			
			if(clubItems.containsKey(item.getBillableConsumptionId())) {
				
				clubItems.get(item.getBillableConsumptionId()).add(item);
				
			}else {
				
				List<BillableConsumptionItem> its = new ArrayList<>();
				its.add(item);
				clubItems.put(item.getBillableConsumptionId(), its);
			}
		}
		CommonResponse response = new CommonResponse();
		for (Map.Entry<Integer,List<BillableConsumptionItem>> mapData : clubItems.entrySet()) {
			
			request.setItems(null);
			
			request.setItems(mapData.getValue());
		
		
		String returnBillNumber = GlobalService.getBillNoByGeneratedCode(request.getStoreId(),
				request.getUnitId(), screenId);
		

		BillingMasterDto dto = new BillingMasterDto();
		dto.setOrganisationId(request.getOrganizationId());
		dto.setUnitId(request.getUnitId());
		dto.setAdmissionId(request.getAdmissionId());
		dto.setEncounterId(request.getEncounterId());
		dto.setPatientId(request.getPatientId());
		dto.setVisitTypeId(request.getVisitTypeId());
		dto.setCreatedBy(request.getUserId());

		// for (BillableConsumptionSave request : request1) {
		ReturnBillableConsumption returnBillableConsumption = new ReturnBillableConsumption(returnBillNumber,
				new BillableConsumption(request.getBillableConsumptionId()), new Store(request.getStoreId()),
				new VisitType(request.getVisitTypeId()), new PatientRegistration(request.getPatientId()),
				new Status(request.getStatusId()), new Status(request.getApprovedStatusId()));
		//sessionService.setEntityDefaults(returnBillableConsumption);

		returnBillableConsumption.setCreateDefaults(request.getOrganizationId()!=null ? request.getOrganizationId() : sessionService.getOrganizationId(),request.getUnitId() !=null ? request.getUnitId() :sessionService.getUnitId() ,request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());

		if (request.getAdmissionId() != null) {
			returnBillableConsumption.setAdmission(new Admission(request.getAdmissionId()));
			if (request.getWardId() != null) {
				returnBillableConsumption.setWard(new WardMasters(request.getWardId()));
			}
			if (request.getBedId() != null) {
				returnBillableConsumption.setBed(new BedMaster(request.getBedId()));
			}
		} else if (request.getEncounterId() != null) {
			returnBillableConsumption.setEncounter(new Encounter(request.getEncounterId()));
			// HIS-10492
			if (request.getWardId() != null) {
				returnBillableConsumption.setWard(new WardMasters(request.getWardId()));
			}
			if (request.getBedId() != null) {
				returnBillableConsumption.setBed(new BedMaster(request.getBedId()));
			}
		}

		List<OrderDetailsMasterDto> orderDetailsList = new ArrayList<OrderDetailsMasterDto>();
        List<Integer> billableConsumptionDetailsIds = request.getItems().stream().map(BillableConsumptionItem::getBillbleConsumptionDetailId).collect(Collectors.toList());
       List<PharmacySaleReturnDto> PharmacySalesReturnAllrecords = returnBillableConsumptionDao
		.getSaleDetailIdByByBillalbleComptionDetailsAll(billableConsumptionDetailsIds);
		for (BillableConsumptionItem items : request.getItems()) {
			ReturnBillableConsumptionDetail returnBillableConsumptionDetail = new ReturnBillableConsumptionDetail(
					new BillableConsumptionDetail(items.getBillbleConsumptionDetailId()), items.getReturnQuantity());
			/*
			 * start code for resolved HIS-9214 Return billable consumption to have issues,
			 * consumed and returned columns
			 */
			returnBillableConsumptionDetail.setConsumeQuantity(items.getConsumedQuantity());
			/*
			 * end code for resolved HIS-9214 Return billable consumption to have issues,
			 * consumed and returned columns
			 */
		//	sessionService.setEntityDefaults(returnBillableConsumptionDetail);
			returnBillableConsumptionDetail.setCreateDefaults(request.getOrganizationId()!=null ? request.getOrganizationId() : sessionService.getOrganizationId(),request.getUnitId() !=null ? request.getUnitId() :sessionService.getUnitId() ,request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());

			returnBillableConsumption.addReturnBillableConsumptionDetailList(returnBillableConsumptionDetail);
            Stream<PharmacySaleReturnDto> billableDetailsStream = PharmacySalesReturnAllrecords.stream();
            List<PharmacySaleReturnDto> billableDetailsList=billableDetailsStream
            .filter(str -> str.getBillbleConsumptionDetailId().equals(items.getBillbleConsumptionDetailId()))
            .collect(Collectors.toList());
            
			PharmacySaleReturnDto phSRDto =billableDetailsList.get(0);
			
			Long batchId = saveStoreStockDetails(request.getStoreId(), items.getBatchId(), items.getReturnQuantity(),
					new UomType(items.getUomTypeId()),request.getUnitId(),request.getCookieStoreId(), request, items.getBillbleConsumptionDetailId(),returnBillNumber);
			items.setBatchId(batchId);
			
			if (phSRDto != null) {
				OrderDetailsMasterDto orderDtlsDto = new OrderDetailsMasterDto();
				orderDtlsDto.setSaleDetailId(phSRDto.getSaleDetailId().intValue());
				orderDtlsDto.setSaleId(phSRDto.getSaleId() != null ? phSRDto.getSaleId().intValue() : null);
				orderDtlsDto.setOrgId(dto.getOrganisationId());
				orderDtlsDto.setOrgUnitId(dto.getUnitId());
				orderDtlsDto.setVisitTypeId(dto.getVisitTypeId());
				orderDtlsDto.setOrderQty(items.getReturnQuantity());
				orderDtlsDto.setBatchId(phSRDto.getBatchId().intValue());
				orderDtlsDto.setDrugId(phSRDto.getItemId());
				orderDtlsDto.setProductCategoryId(phSRDto.getProductCategoryId());
				orderDetailsList.add(orderDtlsDto);
			}

		}
		ReturnBillableConsumption returnBillableCon = null;
		// HIS-10492
		// if (dto.getVisitTypeId() != null && dto.getVisitTypeId() != 1) {
		if (dto.getVisitTypeId() != null) {
			returnBillableCon = returnBillableConsumptionDao.saveReturnBillableConsumption(returnBillableConsumption);
		}
		if (returnBillableCon != null) {

			response.setId(returnBillableCon.getId().longValue());
		}
		dto.setConsolidated(true);
		// sales return
		iWalkInSalesService.pharamcyPackageContractReturn(dto, orderDetailsList);
		response.setMessage("return billable consumption saved successfully");
		// }
		}
		return response;
	}

	@Override
	@Transactional
	public List<BillableConsumptionDetails> returnBillableConsumptionSearchIssue(BillableConsumptionRequest request) {
		List<BillableConsumptionDetails> billableConsumptionDetailList = new ArrayList<BillableConsumptionDetails>();
		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());

		for (BillableConsumption billableConsumption : returnBillableConsumptionDao
				.returnBillableConsumptionSearchIssue(request)) {
			BillableConsumptionDetails billableConsumptionDetail = new BillableConsumptionDetails(
					billableConsumption.getId(), billableConsumption.getConsumableNumber(),
					billableConsumption.getAddedDate());
			boolean dataFlag=true;
			Integer counter=0;
			for(BillableConsumptionDetail bd :billableConsumption.getBillableConsumptionDetail())
			{
             Integer returnQty=bd.getConsumableQuantity();
             Integer returnBillableQty = returnBillableConsumptionDao.getReturnQuantityById(bd.getId());
             if(returnQty.equals(returnBillableQty))
             {
            	 counter++;
             }
             
         
				
			}
			
			
			
			if (billableConsumption.getAdmission() != null) {
				billableConsumptionDetail.setVisitNo(billableConsumption.getAdmission().getAdmissionNumber());
				billableConsumptionDetail.setVisitDate(billableConsumption.getAdmission().getCreatedDate());
			} else if (billableConsumption.getEncounter() != null) {
				billableConsumptionDetail.setVisitNo(billableConsumption.getEncounter().getEncounterNumber());
				billableConsumptionDetail.setVisitDate(billableConsumption.getEncounter().getCreatedDate());
			}
			if(counter==billableConsumption.getBillableConsumptionDetail().size())
			{
				dataFlag=false;
			}
			else
			{
				dataFlag=true;
			}
			if(dataFlag==true)
			{
				billableConsumptionDetailList.add(billableConsumptionDetail);	
			}
			
		}
		return billableConsumptionDetailList;
	}

	@Override
	@Transactional
	public List<BillableConsumptionDetails> returnBillableConsumptionAdd(ConsumptionIds ids) {
		// List<BillableConsumptionDetails> jsonData=null;
		ArrayList<BillableConsumptionDetails> jsonData = new ArrayList<>();

		for (Integer id : ids.getIds()) {
			BillableConsumptionDetails billableConsumptionDetail = new BillableConsumptionDetails();
			billableConsumptionDetail.setId(id);
			List<BillableConsumptionDetailItem> items = new ArrayList<BillableConsumptionDetailItem>();
			for (BillableConsumptionDetail billableConsumptionDetails : returnBillableConsumptionDao
					.returnBillableConsumptionAdd(id,ids.getUnitId())) {
				Long returnQuantity = returnBillableConsumptionDao
						.getReturnQuantity(billableConsumptionDetails.getId(),ids.getUnitId());
				/*
				 * BillableConsumptionDetailItem item = new BillableConsumptionDetailItem(
				 * billableConsumptionDetails.getId(),
				 * billableConsumptionDetails.getBatch().getItem().getId(),
				 * billableConsumptionDetails.getBatch().getItem().getCode(),
				 * billableConsumptionDetails.getBatch().getItem().getName(),
				 * billableConsumptionDetails.getBatch().getItem().getGeneric().getId(),
				 * billableConsumptionDetails.getBatch().getItem().getGeneric().getName(),
				 * billableConsumptionDetails.getBatch().getId(),
				 * billableConsumptionDetails.getBatch().getBatchNo(),
				 * billableConsumptionDetails.getBatch().getExpiryDate(),
				 * billableConsumptionDetails.getUomType().getType(),
				 * billableConsumptionDetails.getUomUnit().getUnits(),
				 * billableConsumptionDetails.getConsumableQuantity(), returnQuantity);
				 */

				BillableConsumptionDetailItem item = new BillableConsumptionDetailItem(
						billableConsumptionDetails.getId(), billableConsumptionDetails.getBatch().getItem().getId(),
						billableConsumptionDetails.getBatch().getItem().getCode(),
						billableConsumptionDetails.getBatch().getItem().getName(),
						billableConsumptionDetails.getBatch().getItem().getGeneric().getId(),
						billableConsumptionDetails.getBatch().getItem().getGeneric().getName(),
						billableConsumptionDetails.getBatch().getId(),
						billableConsumptionDetails.getBatch().getBatchNo(),
						billableConsumptionDetails.getBatch().getExpiryDate(),
						billableConsumptionDetails.getUomType().getType(),
						billableConsumptionDetails.getUomType().getId(),
						billableConsumptionDetails.getUomUnit().getUnits(),
						billableConsumptionDetails.getConsumableQuantity(), returnQuantity,
						billableConsumptionDetails.getBatch().getItem().getIsThreeUOMApplicable(), 
						GlobalDao.getUnitOfMeasurementListByItem(billableConsumptionDetails.getBatch().getItem().getId(), billableConsumptionDetails.getOrganizationId()));
				/*
				 * start code for resolved HIS-9214 Return billable consumption to have issues,
				 * consumed and returned columns
				 */
				Long totalConsumedQuantity = returnBillableConsumptionDao
						.getTotalConsumeQuantity(billableConsumptionDetails.getId(),ids.getUnitId());
				item.setTotalConsumedQuantity(totalConsumedQuantity);
				/*
				 * end code for resolved HIS-9214 Return billable consumption to have issues,
				 * consumed and returned columns
				 */
				if(item.getReturnQuantity().intValue()!=item.getConsumedQuantity())
				{
					items.add(item);
				}
			}
			billableConsumptionDetail.setItems(items);
			jsonData.add(billableConsumptionDetail);
		}

		return jsonData;
	}

	private Long saveStoreStockDetails(Integer storeId, Long batchId, Integer quantity, UomType uomType, Integer unitId,Integer cookieStoreId, BillableConsumptionSave request, Integer integer, String returnBillNumber) {
		if (quantity > 0) {
			try {
				Batch batch = GlobalDao.getBatch(batchId);
				if (batch != null) {

					Integer leaseQuantity = Util.GetLeaseQuantity(quantity, batch.getBatchUomList(), uomType);
					Integer tQuantity = 0;
					Integer availableQuantity = 0;
					Integer currentQuantity = 0;

					// added if condation by Minesh for check flag isConsignment
					// use for convert consignmentBatch in to normalBatch
					if (batch.getIsConsignment() != null && batch.getIsConsignment()) {
						Batch normalBatch = getConsignmentConvertedBatch(batchId, batch, leaseQuantity , request);

						// change by Minesh/Atmiya for we consume quantity in batch , so set 'quantity'
						batch.setQuantity(batch.getQuantity() + leaseQuantity);
						setLeaseQuantity(batch, leaseQuantity,unitId,cookieStoreId , request,integer,returnBillNumber);

						// change by Minesh/Atmiya for we consume quantity in normal batch , so set
						// 'quantity'
						setLeaseQuantity(normalBatch, leaseQuantity,unitId,storeId , request,integer,returnBillNumber);
						return normalBatch.getId();
					} else {
						setLeaseQuantity(batch, leaseQuantity,unitId,storeId, request,integer,returnBillNumber);
						return batch.getId();
					}

				}

			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				throw new ServiceException(e.getMessage());
			}
		}
		return batchId;
	}

	private void setLeaseQuantity(Batch batch, Integer leaseQuantity,Integer unitId,Integer storeId, BillableConsumptionSave request, Integer integer, String returnBillNumber) {
		Integer tQuantity;
		Integer availableQuantity;
		Integer currentQuantity;
		// change from batchId to batch.getId() in parameter
		StoreStock storeStockList = GlobalDao.getStoreStockByBatchAndStore1(batch.getId(), storeId,unitId);
		StoreStock storeStockDetail = null;
		if (storeStockList == null) {
			// change from batchId to batch.getId() in parameter
			StoreStock storeStock = new StoreStock(leaseQuantity, new Store(storeId),
					new Batch(batch.getId()));
			//sessionService.setEntityDefaults(storeStock);
			storeStock.setCreateDefaults(request.getOrganizationId()!=null ? request.getOrganizationId() : sessionService.getOrganizationId(),request.getUnitId() !=null ? request.getUnitId() :sessionService.getUnitId() ,request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());

			storeStock.setIsConsignment(batch.getIsConsignment());
			storeStockList = GlobalDao.saveStoreStockLeaseQuantity(storeStock);
		}

		// code change: remove else condation by Atmiya. for insert new stock and update
		// it.

		// storeStockList = GlobalDao.getStoreStockByBatchAndStore(batch.getId(),
		// sessionService.getStoreId());

		tQuantity = leaseQuantity + storeStockList.gettQty();
		availableQuantity = storeStockList.getAvailableQty() + leaseQuantity;
		storeStockList.setAvailableQty(availableQuantity);
		storeStockList.settQty(tQuantity);
		storeStockDetail = GlobalDao.updateStoreStockLeaseQuantity(storeStockList);

		/*
		 * comment code by atmiya to useless fetch (alredy featched.). no need to fetch
		 * again
		 */
		/*
		 * Batch batchObject = GlobalDao.getBatch(sessionService.getStoreId(),
		 * storeStockDetail.getBatch().getId());
		 */
		batch.setCurrentQuantity(batch.getCurrentQuantity() + leaseQuantity);
		GlobalDao.updateBatchLeaseQuantity(batch);
		saveStockTransaction(leaseQuantity, storeStockDetail, request,integer,returnBillNumber);
	}

	private void saveStockTransaction(Integer leaseQuantity, StoreStock storeStock, BillableConsumptionSave request, Integer integer, String returnBillNumber) {
		com.param.entity.model.master.Screen screen = GlobalDao.getScreen(screenId);
		StockTransaction stockTransaction = new StockTransaction(storeStock.getAvailableQty(),
				new com.param.entity.model.master.Screen(screen.getId()), screen.getCode(), leaseQuantity,
				storeStock.getBatch().getRate(), storeStock.getStore(),
				new Batch(storeStock.getBatch().getId()),integer);
		stockTransaction.setTransitNo(returnBillNumber);
		stockTransaction.setInQty(leaseQuantity);
		BillableConsumptionSave res = GlobalDao.getBillableConsumId(integer);
		if(res != null ) {
			stockTransaction.setTransactionId(res.getBillableConsumptionId());
		}else {
			stockTransaction.setTransactionId(request.getBillableConsumptionId());
		}
		//stockTransaction.setTransactionId(request.getBillableConsumptionId());
		stockTransaction.setIsTransit(false);
		//sessionService.setEntityDefaults(stockTransaction);
		stockTransaction.setCreateDefaults(request.getOrganizationId()!=null ? request.getOrganizationId() : sessionService.getOrganizationId(),request.getUnitId() !=null ? request.getUnitId() :sessionService.getUnitId() ,request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());

		GlobalDao.saveStockTransaction(stockTransaction);
	}

	private Batch getConsignmentConvertedBatch(Long batchId, Batch batch, Integer leaseQuantity, BillableConsumptionSave request) {
		Batch newbatchResponse = new Batch();
		// Batch convertedBatch = GlobalDao.getConvertedBatch(batchId);
		/* if (convertedBatch == null) { */ // for new entry of converted batch
		// Batch newBatch = (Batch) SerializationUtils.clone(batch);
		// newBatch.setId(null);
		Batch newBatch = new Batch();
		newBatch.setOrganization(batch.getOrganization());
		newBatch.setUnit(batch.getUnit());
		newBatch.setUomUnit(batch.getUomUnit());
		newBatch.setItem(batch.getItem());
		newBatch.setBatchNo(batch.getBatchNo());
		newBatch.setLotNo(batch.getLotNo());
		newBatch.setQuantity(leaseQuantity);
		newBatch.setCurrentQuantity(leaseQuantity);
		newBatch.setExpiryDate(batch.getExpiryDate());
		newBatch.setCop(batch.getCop());
		newBatch.setRate(batch.getRate());
		newBatch.setSellingPrice(batch.getSellingPrice());
		newBatch.setMarkup(batch.getMarkup());
		newBatch.setLendingPrice(batch.getLendingPrice());
		newBatch.setActualLendingPrice(batch.getActualLendingPrice());
		newBatch.setTax(batch.getTax());
		newBatch.setTaxPercentage(batch.getTaxPercentage());
		newBatch.setTaxAmount(batch.getTaxAmount());
		newBatch.setBarcode(batch.getBarcode());
		newBatch.setIsOpb(batch.getIsOpb());
		newBatch.setIsActive(batch.getIsActive());
		newBatch.setUpdatedByUser(request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());
		// newBatch.setUpdatedDate(batch.getUpdatedDate());
		newBatch.setAddedByUser(request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());
		// newBatch.setAddedDate(batch.getAddedDate());
		newBatch.setIsDeleted(batch.getIsDeleted());
		newBatch.setBarcodePrintCount(batch.getBarcodePrintCount());
		newBatch.setIsQuarantine(batch.getIsQuarantine());
		newBatch.setVendor(batch.getVendor());
		newBatch.setServiceProvider(batch.getServiceProvider());
		newBatch.setWarrantyFrom(batch.getWarrantyFrom());
		newBatch.setWarrantyTo(batch.getWarrantyTo());
		newBatch.setPmsFrequency(batch.getPmsFrequency());
		newBatch.setYearOfManufacture(batch.getYearOfManufacture());
		newBatch.setWorkingCondition(batch.getWorkingCondition());
		newBatch.setModelNumber(batch.getModelNumber());
		newBatch.setIsPharmacy(batch.getIsPharmacy());
		newBatch.setIsIndent(batch.getIsIndent());
		newBatch.setIsReturn(batch.getIsReturn());
		newBatch.setRejectedQuantity(batch.getRejectedQuantity());
		// newBatch.stop
		newBatch.setIsConsignment(false);
		newBatch.setCreateDefaults(request.getOrganizationId(), request.getUnitId(), request.getUserId());

		for (BatchUomMapper batchUomMapperObj : batch.getBatchUomList()) {
			BatchUomMapper batchUomMapper = new BatchUomMapper(batchUomMapperObj.getConversions(),
					batchUomMapperObj.getIpUom(), batchUomMapperObj.getOpUom(), batchUomMapperObj.getStoreUom(),
					new UomType(batchUomMapperObj.getUomType().getId()),
					new UomUnit(batchUomMapperObj.getUomUnit().getId()));
			batchUomMapper.setCreateDefaults(request.getOrganizationId(), request.getUnitId(), request.getUserId());
			newBatch.addBatchUom(batchUomMapper);
		}

		newbatchResponse = GlobalDao.saveBatch(newBatch);
		batch = GlobalDao.getBatch(newbatchResponse.getId());

		/*
		 * } else { //for update converted batch if exist
		 * convertedBatch.setQuantity(leaseQuantity + convertedBatch.getQuantity());
		 * convertedBatch.setCurrentQuantity(leaseQuantity +
		 * convertedBatch.getCurrentQuantity());
		 * GlobalDao.updateBatchLeaseQuantity(convertedBatch); batch =
		 * GlobalDao.getBatch(convertedBatch.getId()); }
		 */
		return batch;
	}

}
