package com.param.pharmacy.ip.service.returnAcceptance.medication;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.adt.WardMasters;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.inventory.StockTransaction;
import com.param.entity.model.inventory.StoreStock;
import com.param.entity.model.master.RejectReason;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.VisitType;
import com.param.entity.model.pharmacy.Issue;
import com.param.entity.model.pharmacy.IssueDetailBatchMapper;
import com.param.entity.model.pharmacy.PatientIssueReturn;
import com.param.entity.model.pharmacy.PatientIssueReturnDetail;
import com.param.entity.model.pharmacy.RejectedRejection;
import com.param.entity.model.pharmacy.RejectedRejectionDetail;
import com.param.global.billing.controller.BillingException;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dto.BillingMasterDto;
import com.param.global.dto.OrderDetailsMasterDto;
import com.param.global.dto.global.CommonDropdown;
import com.param.global.dto.items.BatchResponse;
import com.param.global.pharmacy.service.IWalkInSalesService;
import com.param.global.service.global.IGlobalService;
import com.param.global.utility.Util;
import com.param.pharmacy.ip.dao.patientIssueRejection.IPatientIssuesRejectionMedicationDao;
import com.param.pharmacy.ip.dao.returnAcceptance.medication.IReturnAcceptanceMedicationDao;
import com.param.pharmacy.ip.dto.returnAcceptance.PharmacySaleReturnDto;
import com.param.pharmacy.ip.dto.returnAcceptance.ReturnAcceptanceItem;
import com.param.pharmacy.ip.dto.returnAcceptance.ReturnAcceptanceRequest;
import com.param.pharmacy.ip.dto.returnAcceptance.ReturnAcceptanceResponse;
import com.param.pharmacy.ip.dto.returnAcceptance.ReturnAcceptanceSave;
import com.param.pharmacy.ip.dto.returnAcceptance.ReturnAcceptanceSaveItem;
import com.param.pharmacy.nursing.workstation.dao.patientIssueReturn.IPatientIssueReturnDao;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;
@SuppressWarnings({ "rawtypes", "unused" })
@Service("returnAcceptanceMedicationService")
@Transactional
public class ReturnAcceptanceMedicationService implements ICommonConstants,IReturnAcceptanceMedicationService {

	@Autowired
	private SessionService sessionService;

	@Autowired
	private IReturnAcceptanceMedicationDao returnAcceptanceMedicationDao;

	@Autowired
	private IGlobalDao GlobalDao;

	@Autowired
	IGlobalService GlobalService;

	@Autowired
	private IPatientIssueReturnDao patientIssueReturnDao;

	@Autowired
	private IWalkInSalesService iWalkInSalesService;
	@Autowired
	private IPatientIssuesRejectionMedicationDao patientIssuesRejectionMedicationDao;

	private Integer screenId = Screen.PatientReturnAcceptance.getScreen();

	private com.param.entity.model.master.Screen screen;

	@Override
	@Transactional
	public CommonDropdown getDropdownList(Integer unitId) {
		CommonDropdown response = new CommonDropdown();

		for (VisitType visitType : GlobalDao.getVisitTypeList()) {
			response.addVisitType(new com.param.global.dto.global.VisitType(visitType.getVisitTypeId(),
					visitType.getVisitTypeName()));
		}

		for (WardMasters wardMaster : GlobalDao.getWardList(unitId)) {
			response.addWard(new com.param.global.dto.global.Ward(wardMaster.getWardId(), wardMaster.getWardName()));
		}
		for (ScreenStatus screenStatus : GlobalDao.getScreenStatus(screenId)) {
			response.addStatus(new com.param.global.dto.global.Status(screenStatus.getStatus().getId(),
			screenStatus.getStatus().getStatus()));
		}

		return response;
	}

	@Override
	@Transactional
	public List<ReturnAcceptanceResponse> returnAcceptanceSearch(ReturnAcceptanceRequest returnAcceptanceRequest) {
		returnAcceptanceRequest.setToDate((new DateTime(returnAcceptanceRequest.getToDate())).plusDays(1).toDate());

		List<ReturnAcceptanceResponse> returnAcceptanceResponseList = new ArrayList<ReturnAcceptanceResponse>();
		List<PatientIssueReturn> list = returnAcceptanceMedicationDao.returnAcceptanceSearch(returnAcceptanceRequest,
				screenId);
		
		for (PatientIssueReturn patientIssueReturn : list) {
			ReturnAcceptanceResponse returnAcceptanceResponse = new ReturnAcceptanceResponse();

			String doctorDetail = "";
			if(patientIssueReturn.getPatientIssueReturnDetail().equals(null)  
					&& patientIssueReturn.getPatientIssueReturnDetail().get(0).getIssueDetailBatchMapper().getIssueDetail().equals(null) 
					&& patientIssueReturn.getPatientIssueReturnDetail().get(0).getIssueDetailBatchMapper().getIssueDetail().getIssue().equals(null) 
					&& patientIssueReturn.getPatientIssueReturnDetail().get(0).getIssueDetailBatchMapper().getIssueDetail().getIssue().getDoctor().equals(null)) {
				doctorDetail = patientIssueReturn.getPatientIssueReturnDetail().get(0).getIssueDetailBatchMapper()
						.getIssueDetail().getIssue().getDoctor().getFirstName()
						+ " "
						+ patientIssueReturn.getPatientIssueReturnDetail().get(0).getIssueDetailBatchMapper()
								.getIssueDetail().getIssue().getDoctor().getMiddleName()
						+ " " + patientIssueReturn.getPatientIssueReturnDetail().get(0).getIssueDetailBatchMapper()
								.getIssueDetail().getIssue().getDoctor().getLastName();
			}

			/*
			 * if(doctorDetail.isEmpty() &&
			 * patientIssueReturn.getPatientIssueReturnDetail().get(0).
			 * getIssueDetailBatchMapper()
			 * .getIssueDetail().getIssue().getSale().getDoctor()!=null) { doctorDetail =
			 * patientIssueReturn.getPatientIssueReturnDetail().get(0).
			 * getIssueDetailBatchMapper()
			 * .getIssueDetail().getIssue().getSale().getDoctor().getFirstName() + " " +
			 * patientIssueReturn.getPatientIssueReturnDetail().get(0).
			 * getIssueDetailBatchMapper()
			 * .getIssueDetail().getIssue().getSale().getDoctor().getMiddleName() + " " +
			 * patientIssueReturn.getPatientIssueReturnDetail().get(0).
			 * getIssueDetailBatchMapper()
			 * .getIssueDetail().getIssue().getSale().getDoctor().getLastName() ; }
			 */

			if (patientIssueReturn.getPatientIssueReturnDetail().get(0).getIssueDetailBatchMapper().getIssueDetail()
					.getIssue().getSpecialityMaster() != null) {
				doctorDetail = doctorDetail + " | "
						+ patientIssueReturn.getPatientIssueReturnDetail().get(0).getIssueDetailBatchMapper()
								.getIssueDetail().getIssue().getSpecialityMaster().getSpecialityName();
			}
            BigInteger saleId =returnAcceptanceMedicationDao.getSaleId(patientIssueReturn.getPatientIssueReturnDetail().get(0).getId());
            if(saleId!=null)
            {
            	String doctorName=returnAcceptanceMedicationDao.getDoctorName(saleId);
            	if(doctorDetail.length()==0 && doctorName!=null)
                {
                	doctorDetail=doctorName;
                }
            }
            
			Integer age = Util
					.getAgeByBirthDate(patientIssueReturn.getPatient().getDob().toLocalDateTime().toLocalDate());
			String patientDetail = patientIssueReturn.getPatient().getPrefix().getPrefixCode() + " "
					+ patientIssueReturn.getPatient().getFirstName() + " "
					+ patientIssueReturn.getPatient().getMiddleName() + " "
					+ patientIssueReturn.getPatient().getLastName() + " | " + age + " | "
					+ patientIssueReturn.getPatient().getGender().getGenderCode() + " | "
					+ patientIssueReturn.getPatient().getUhidNumber();

			Integer ageStringDet = Util
					.getAgeByBirthDate(patientIssueReturn.getPatient().getDob().toLocalDateTime().toLocalDate());

			String patientName = patientIssueReturn.getPatient().getPrefix().getPrefixCode() + " "
					+ patientIssueReturn.getPatient().getFirstName() + " "
					+ patientIssueReturn.getPatient().getMiddleName() + " "
					+ patientIssueReturn.getPatient().getLastName();

			returnAcceptanceResponse = new ReturnAcceptanceResponse(patientIssueReturn.getId(),
					patientIssueReturn.getReturnNo(), patientIssueReturn.getAddedDate(),
					patientIssueReturn.getPatient().getUhidNumber(), patientDetail, doctorDetail,
					patientIssueReturn.getVisitType().getVisitTypeCode(), patientIssueReturn.getStore().getStoreName(),
					patientIssueReturn.getPatient().getPatientId(), patientName, ageStringDet);

			returnAcceptanceResponse.setStatus(patientIssueReturn.getStatus().getStatus());
			returnAcceptanceResponse.setStatusId(patientIssueReturn.getStatus().getId());
			returnAcceptanceResponse.setGenderId(patientIssueReturn.getPatient().getGender().getGenderId());
			returnAcceptanceResponse.setImagePath(patientIssueReturn.getPatient().getImagePath());
			returnAcceptanceResponse.setGenderCode(patientIssueReturn.getPatient().getGender().getGenderCode());

			if (patientIssueReturn.getAdmission() != null) {
				returnAcceptanceResponse.setWardName(
						(patientIssueReturn.getWardMaster() != null) ? patientIssueReturn.getWardMaster().getWardName()
								: null);
				returnAcceptanceResponse.setWardId(
						(patientIssueReturn.getWardMaster() != null) ? patientIssueReturn.getWardMaster().getWardId()
								: null);
				returnAcceptanceResponse.setBedNo(
						(patientIssueReturn.getBedMaster() != null) ? patientIssueReturn.getBedMaster().getBedNumber()
								: null);
			}
//			patientIssueReturn.getPatient().getGender().getGenderId();
//			patientIssueReturn.getPatient().getImagePath();

			returnAcceptanceResponseList.add(returnAcceptanceResponse);

		}
		return returnAcceptanceResponseList;
	}

	@Override
	@Transactional
	public ReturnAcceptanceResponse returnAcceptanceDetails(Integer id, Integer unitId) {
		ReturnAcceptanceResponse returnAcceptanceResponse = new ReturnAcceptanceResponse();
		PatientIssueReturn patientIssueReturn = patientIssueReturnDao.patientIssueReturnById(id, unitId);
		ReturnAcceptanceResponse returnDto= patientIssueReturnDao.getDocDetailForPatientIssueReturn(id, unitId);

		/*
		 * List<DoctorSpecialityMapper> doctorSpeciality = GlobalDao
		 * .doctorSearchByIdValue(patientIssueReturn.getPatientIssueReturnDetail().get(
		 * 0) .getIssueDetailBatchMapper().getIssueDetail().getIssue().getDoctor().
		 * getDoctorId()); String drSpecialityName = ""; if (doctorSpeciality.size() >
		 * 0) { drSpecialityName =
		 * doctorSpeciality.get(0).getSpeciality().getSpecialityName(); }
		 */
		/*
		 * String doctorDetail = "";
		 * if(patientIssueReturn.getPatientIssueReturnDetail().get(0).
		 * getIssueDetailBatchMapper() .getIssueDetail().getIssue().getDoctor()!=null &&
		 * patientIssueReturn.getPatientIssueReturnDetail().get(0).
		 * getIssueDetailBatchMapper()
		 * .getIssueDetail().getIssue().getSpecialityMaster()!=null) { doctorDetail =
		 * patientIssueReturn.getPatientIssueReturnDetail().get(0).
		 * getIssueDetailBatchMapper()
		 * .getIssueDetail().getIssue().getDoctor().getFirstName() + " " +
		 * patientIssueReturn.getPatientIssueReturnDetail().get(0).
		 * getIssueDetailBatchMapper().getIssueDetail()
		 * .getIssue().getDoctor().getMiddleName() + " " +
		 * patientIssueReturn.getPatientIssueReturnDetail().get(0).
		 * getIssueDetailBatchMapper()
		 * .getIssueDetail().getIssue().getDoctor().getLastName() + " | " +
		 * patientIssueReturn.getPatientIssueReturnDetail().get(0).
		 * getIssueDetailBatchMapper()
		 * .getIssueDetail().getIssue().getSpecialityMaster().getSpecialityName(); }
		 */

		String doctorDetail = "";
			if(returnDto!=null && !returnDto.getDoctorDetail().equals(null)){
				doctorDetail = returnDto.getDoctorDetail();
				}
			
			else {
				doctorDetail = "";
				}
			
		

		/*if (patientIssueReturn.getPatientIssueReturnDetail().get(0).getIssueDetailBatchMapper().getIssueDetail().getIssue().getSpecialityMaster()!= null)
		{
			if(patientIssueReturn.getPatientIssueReturnDetail().get(0).getIssueDetailBatchMapper().getIssueDetail().getIssue().getSpecialityMaster().getSpecialityName() != null){
				doctorDetail = (doctorDetail != null ? doctorDetail :"") + " | " + patientIssueReturn.getPatientIssueReturnDetail().get(0)
						.getIssueDetailBatchMapper().getIssueDetail().getIssue().getSpecialityMaster().getSpecialityName();
			}else {
				doctorDetail = (doctorDetail != null ? doctorDetail :"") + " | " + "";
			}
			
		}*/

		Integer age = Util.getAgeByBirthDate(patientIssueReturn.getPatient().getDob().toLocalDateTime().toLocalDate());
		String patientDetail = patientIssueReturn.getPatient().getPrefix().getPrefixCode() + " "
				+ patientIssueReturn.getPatient().getFirstName() + " " + patientIssueReturn.getPatient().getMiddleName()
				+ " " + patientIssueReturn.getPatient().getLastName() + " | " + age + " | "
				+ patientIssueReturn.getPatient().getGender().getGenderCode();

		returnAcceptanceResponse = new ReturnAcceptanceResponse(patientIssueReturn.getId(),
				patientIssueReturn.getReturnNo(), patientIssueReturn.getAddedDate(),
				patientIssueReturn.getPatient().getUhidNumber(), patientDetail, doctorDetail,
				patientIssueReturn.getVisitType().getVisitTypeCode(), patientIssueReturn.getStore().getStoreName(),
				patientIssueReturn.getPatient().getPatientId());
		returnAcceptanceResponse.setStatus(patientIssueReturn.getStatus().getStatus());
		returnAcceptanceResponse.setStatusId(patientIssueReturn.getStatus().getId());
		returnAcceptanceResponse.setStoreId(patientIssueReturn.getStore().getStoreId());
		returnAcceptanceResponse.setIssueId(patientIssueReturn.getPatientIssueReturnDetail().get(0)
				.getIssueDetailBatchMapper().getIssueDetail().getIssue().getId());

		if (patientIssueReturn.getAdmission() != null) {
			returnAcceptanceResponse.setWardName(
					(patientIssueReturn.getWardMaster() != null) ? patientIssueReturn.getWardMaster().getWardName()
							: null);
			returnAcceptanceResponse.setWardId(
					(patientIssueReturn.getWardMaster() != null) ? patientIssueReturn.getWardMaster().getWardId()
							: null);
			returnAcceptanceResponse.setBedNo(
					(patientIssueReturn.getBedMaster() != null) ? patientIssueReturn.getBedMaster().getBedNumber()
							: null);
		}

		List<ReturnAcceptanceItem> items = new ArrayList<ReturnAcceptanceItem>();
		for (PatientIssueReturnDetail patientIssueReturnDetail : patientIssueReturnDao.patientIssueReturnDetailById(id,
				unitId)) {
			// patientIssueReturnDetail.getReturnQuantity();
            Double getActualRate=GlobalDao.getActualRateFromOpeningBalance(patientIssueReturnDetail.getIssueDetailBatchMapper().getBatch().getId(),
            		unitId);
            List<Batch> batchList = GlobalDao.getBatchDetailsOnReturnAcceptance(
            		patientIssueReturnDetail.getIssueDetailBatchMapper().getBatch().getItem().getId(), patientIssueReturn.getStore().getStoreId(), unitId);
			List<BatchResponse> medicineBatches = new ArrayList<>();
			for (Batch batch : batchList) {
				// Populate batch response
				BatchResponse batchResponse = Util.PopulateBatchWithStockWithFlag(batch, false);
				medicineBatches.add(batchResponse);
			}
			ReturnAcceptanceItem item = new ReturnAcceptanceItem(patientIssueReturnDetail.getId(),
					patientIssueReturnDetail.getIssueDetailBatchMapper().getBatch().getItem().getId(),
					patientIssueReturnDetail.getIssueDetailBatchMapper().getBatch().getItem().getName(),
					patientIssueReturnDetail.getIssueDetailBatchMapper().getBatch().getItem().getGeneric().getName(),
					patientIssueReturnDetail.getIssueDetailBatchMapper().getBatch().getBatchNo(),
					patientIssueReturnDetail.getIssueDetailBatchMapper().getBatch().getExpiryDate(),
					patientIssueReturnDetail.getIssueDetailBatchMapper().getUomType().getType(),
					patientIssueReturnDetail.getIssueDetailBatchMapper().getUomUnit().getUnits(),
					patientIssueReturnDetail.getIssueDetailBatchMapper().getBatch().getSellingPrice(),
					patientIssueReturnDetail.getReturnQuantity(),
					patientIssueReturnDetail.getReturnReason().getReasonDesc(),
					patientIssueReturnDetail.getIssueDetailBatchMapper().getId(),
					(patientIssueReturnDetail.getReturnQuantity()
							- patientIssueReturnDetail.getIssueDetailBatchMapper().getRejectedQuantity()),
					patientIssueReturnDetail.getIssueDetailBatchMapper().getBatch().getItem().getGeneric().getId(),
					patientIssueReturnDetail.getIssueDetailBatchMapper().getBatch().getItem().getIsThreeUOMApplicable());
			item.setBatch(medicineBatches);
			item.setMrp(getActualRate);
			item.setActualLendingPrice(patientIssueReturnDetail.getIssueDetailBatchMapper().getBatch().getActualLendingPrice());
			 item.setStatus(patientIssueReturnDetail.getStatus().getStatus());
			 item.setStatusId(patientIssueReturnDetail.getStatus().getId());
			item.setAcceptReturnQuantity(patientIssueReturnDetail.getReturnQuantity());
			item.setAcceptedQuantity(patientIssueReturnDetail.getAcceptQuantity());
			item.setBatchId(patientIssueReturnDetail.getIssueDetailBatchMapper().getBatch().getId());
			item.setRejectedQuantity(patientIssueReturnDetail.getRejectQuantity());
			item.setIsReturnAcceptance(patientIssueReturnDetail.getIssueDetailBatchMapper().getIsReturnAcceptance());
			item.setGenericId(
					patientIssueReturnDetail.getIssueDetailBatchMapper().getBatch().getItem().getGeneric().getId());
			items.add(item);
			
		}
		boolean copFlag=patientIssueReturnDao.getCOPFlagFromSale(returnAcceptanceResponse.getIssueId());
		returnAcceptanceResponse.setAtCOP(copFlag);
		returnAcceptanceResponse.setItems(items);
		return returnAcceptanceResponse;
	}

	
	@Override
	@Transactional
	public Object patientReturnAcceptanceSave(ReturnAcceptanceSave returnAcceptanceSave)
			throws BillingException, Exception {

		Map<Integer,Boolean> completeSalesReturnSaleIds = new HashMap<>();

		screen = GlobalDao.getScreen(screenId);
		PatientIssueReturn patientIssueReturn = patientIssueReturnDao
				.patientIssueReturnById(returnAcceptanceSave.getId(), returnAcceptanceSave.getUnitId());
		
		BillingMasterDto dto = new BillingMasterDto();
		dto.setOrganisationId(returnAcceptanceSave.getOrganizationId());
		dto.setUnitId(returnAcceptanceSave.getUnitId());
		dto.setAdmissionId(
				patientIssueReturn.getAdmission() != null ? patientIssueReturn.getAdmission().getAdmissionId() : null);
		dto.setEncounterId(
				patientIssueReturn.getEncounter() != null ? patientIssueReturn.getEncounter().getEncounterId() : null);
		dto.setPatientId(returnAcceptanceSave.getPatientId());
		dto.setVisitTypeId(patientIssueReturn.getVisitType().getVisitTypeId());
		dto.setCreatedBy(returnAcceptanceSave.getUserId());
		dto.setReturnDate(returnAcceptanceSave.getReturnDate() != null ? returnAcceptanceSave.getReturnDate() : null);

		List<OrderDetailsMasterDto> orderDetailsList = new ArrayList<OrderDetailsMasterDto>();
		for (PatientIssueReturnDetail patientIssueReturnDetail : patientIssueReturn.getPatientIssueReturnDetail()) {
			IssueDetailBatchMapper batchMapper = patientIssueReturnDetail.getIssueDetailBatchMapper();

			for (ReturnAcceptanceSaveItem issueDetailBatchMapper : returnAcceptanceSave.getItems()) {
				if (issueDetailBatchMapper.getAcceptQuantity() >= 0) {
					if (issueDetailBatchMapper.getIssueDetailBatchMapper() != null
							&& (batchMapper.getId().equals(issueDetailBatchMapper.getIssueDetailBatchMapper()))) {
						batchMapper.setIsReturnAcceptance(true);
					
						if(returnAcceptanceSave.getFromVirtualStore() == null){
							
							BatchMapperStoreStock(returnAcceptanceSave.getStoreId(), batchMapper.getBatch(),
									issueDetailBatchMapper.getAcceptQuantity(), batchMapper.getUomType(),
									returnAcceptanceSave, batchMapper, patientIssueReturn,patientIssueReturnDetail, issueDetailBatchMapper);
						}
						
						batchMapper.updateIssueDetailBatchMapper1(
								(batchMapper.getReturnAcceptedQuantity() != null
										? batchMapper.getReturnAcceptedQuantity()
										: 0) + issueDetailBatchMapper.getAcceptQuantity(),
								issueDetailBatchMapper.getRejectedNote(),
								(batchMapper.getReturnRejectedQuantity() != null
										? batchMapper.getReturnRejectedQuantity()
										: 0) + issueDetailBatchMapper.getRejectedQuantity(),
								(issueDetailBatchMapper.getRejectReason() != null)
										? new RejectReason(issueDetailBatchMapper.getRejectReason())
										: null);
						patientIssueReturnDetail.setAcceptQuantity(issueDetailBatchMapper.getAcceptQuantity());
						patientIssueReturnDetail.setRejectQuantity(issueDetailBatchMapper.getRejectedQuantity() != null
								? issueDetailBatchMapper.getRejectedQuantity()
								: 0);
						patientIssueReturnDetail.setStatus(new Status(issueDetailBatchMapper.getStatusId()));
						PharmacySaleReturnDto phSRDto = patientIssueReturnDao
								.getSaleDetailIdByIssueDetailMapper(issueDetailBatchMapper.getIssueDetailBatchMapper());

						if (phSRDto != null) {
							patientIssueReturnDetail.setSaleDetailsId(phSRDto.getSaleDetailId().intValue());

							OrderDetailsMasterDto orderDtlsDto = new OrderDetailsMasterDto();
							orderDtlsDto.setSaleDetailId(phSRDto.getSaleDetailId().intValue());
							orderDtlsDto.setSaleId(phSRDto.getSaleId() != null ? phSRDto.getSaleId().intValue() : null);
							orderDtlsDto.setOrgId(dto.getOrganisationId());
							orderDtlsDto.setOrgUnitId(dto.getUnitId());
							orderDtlsDto.setVisitTypeId(dto.getVisitTypeId());
							orderDtlsDto.setOrderQty(issueDetailBatchMapper.getAcceptQuantity());
							orderDtlsDto.setBatchId(phSRDto.getBatchId().intValue());
							orderDtlsDto.setDrugId(phSRDto.getItemId());
							orderDtlsDto.setProductCategoryId(phSRDto.getProductCategoryId());
							if(returnAcceptanceSave.getIsDummyService())
								orderDtlsDto.setDummyService(returnAcceptanceSave.getIsDummyService());
							
							if (issueDetailBatchMapper.getAcceptQuantity() > 0) {
								orderDetailsList.add(orderDtlsDto);
							}
							
						}

						//added by ganesh sales return
						if(dto.getVisitTypeId()!=null && dto.getVisitTypeId()!=OP &&
								issueDetailBatchMapper.getAcceptQuantity()!=null
						  &&    issueDetailBatchMapper.getAcceptQuantity()>0
						  &&		issueDetailBatchMapper.getReturnQuantity()!=null
								&&		issueDetailBatchMapper.getReturnQuantity()>0
								&& issueDetailBatchMapper.getAcceptQuantity().intValue() ==issueDetailBatchMapper.getReturnQuantity().intValue()

						   )
						{
							completeSalesReturnSaleIds.put(phSRDto!=null && phSRDto.getSaleId() != null ? phSRDto.getSaleId().intValue() : null,true);
						}
						else{
							completeSalesReturnSaleIds.put(phSRDto!=null && phSRDto.getSaleId() != null ? phSRDto.getSaleId().intValue() : null,false);
						}
					}
				}
			}
			
		}

		patientIssueReturn.setStatus(new Status(returnAcceptanceSave.getStatusId()));
		patientIssueReturn.setUpdatedByUser(returnAcceptanceSave.getUserId() != null ? returnAcceptanceSave.getUserId()
				: sessionService.getUserId());
		patientIssueReturn.setUpdatedDate(TimeZoneComponent.getDateByzone(patientIssueReturn.getUnitId()));

		returnAcceptanceMedicationDao.updatePatientIssueReturn(patientIssueReturn);

		// sales return
		if(!orderDetailsList.isEmpty())
		{
			iWalkInSalesService.pharamcyPackageContractReturn(dto, orderDetailsList);
		}

		saveRejectDetails(returnAcceptanceSave);

		CommonResponse response = new CommonResponse();
		response.setId(patientIssueReturn.getId().longValue());

		if(!CollectionUtils.isEmpty(completeSalesReturnSaleIds)){

			List<Integer> saleIds = completeSalesReturnSaleIds
					.entrySet()
					.stream()
					.filter(e-> e.getValue().equals(true))
					.map(Map.Entry::getKey).collect(Collectors.toList());

		}
		
		response.setMessage("Patient Return Acceptance saved successfully");

		return response;
	}

	private void BatchMapperStoreStock(Integer storeId, Batch batch, Integer issueQuantity, UomType uomType,
			ReturnAcceptanceSave returnAcceptanceSave, IssueDetailBatchMapper bm, PatientIssueReturn patientIssueReturn,
			PatientIssueReturnDetail patientIssueReturnDetail, ReturnAcceptanceSaveItem issueDetailBatchMapper) {

		boolean hasStock = false;

		for (StoreStock storeStock : batch.getStoreStockList()) {
			if (storeStock.getStore().getStoreId().equals(storeId)) {

				hasStock = true;
				Integer avaialableStoreStock = GlobalDao.getStoreStockByBatch(storeId, batch.getId());

				Integer leaseQuantity = Util.GetLeaseQuantity(issueQuantity, batch.getBatchUomList(), uomType);

				Integer balanceQuantity = (avaialableStoreStock != null ? avaialableStoreStock : 0) + leaseQuantity;

				storeStock.setAvailableQty(balanceQuantity < 0 ? 0 : balanceQuantity);
				storeStock.settQty(
						(storeStock.gettQty() - leaseQuantity) >= 0 ? (storeStock.gettQty() - leaseQuantity) : 0);

				Integer currentQuantity = batch.getCurrentQuantity() + leaseQuantity;
				batch.setCurrentQuantity(currentQuantity);
				Integer rejectQty = issueDetailBatchMapper.getRejectedQuantity() != null
						? issueDetailBatchMapper.getRejectedQuantity()
						: 0;
				rejectQty = Util.GetLeaseQuantity(rejectQty, batch.getBatchUomList(), uomType);
				StockTransaction stockTransaction = new StockTransaction(storeStock.getAvailableQty(), screen, screen.getCode(),
						rejectQty, storeStock.getStore(), patientIssueReturnDetail.getId());
				stockTransaction.setIsTransit(false);
				stockTransaction.setItem(batch.getItem());
				stockTransaction.setTransactionId(patientIssueReturn.getId());
				stockTransaction.setInQty(leaseQuantity);
				stockTransaction.setTransitNo(patientIssueReturn.getReturnNo());
				stockTransaction.setCreateDefaults(returnAcceptanceSave.getOrganizationId(),
						returnAcceptanceSave.getUnitId(), returnAcceptanceSave.getUserId());

				// batch.addStockTransaction(new StockTransaction(balanceQuantity, screen,
				// screen.getCode(), leaseQuantity,
				// batch.getStoreStockList().get(0).getStore()));
				batch.addStockTransaction(stockTransaction);
				break;
			}
		}
		if (!hasStock) {

			StoreStock StoreStock = new StoreStock(issueQuantity, new Store(storeId), new Batch(batch.getId()));

			// sessionService.setEntityDefaults(StoreStock);
			StoreStock.setCreateDefaults(
					returnAcceptanceSave.getOrganizationId() != null ? returnAcceptanceSave.getOrganizationId()
							: sessionService.getOrganizationId(),
					returnAcceptanceSave.getUnitId() != null ? returnAcceptanceSave.getUnitId()
							: sessionService.getUnitId(),
					returnAcceptanceSave.getUserId() != null ? returnAcceptanceSave.getUserId()
							: sessionService.getUserId());

			Integer avaialableStoreStock = GlobalDao.getStoreStockByBatch(storeId, batch.getId());

			Integer leaseQuantity = Util.GetLeaseQuantity(issueQuantity, batch.getBatchUomList(), uomType);

			Integer currentQuantity = batch.getCurrentQuantity() + leaseQuantity;
			batch.setCurrentQuantity(currentQuantity);
			Store store;
			if (batch.getStoreStockList().isEmpty())
			 {
				 store=StoreStock.getStore();
			 }else
			 {
				 store= batch.getStoreStockList().get(0).getStore();
			 }
			StockTransaction stockTransaction = new StockTransaction(avaialableStoreStock, screen, screen.getCode(),
					leaseQuantity,store, bm.getId());
			stockTransaction.setCreateDefaults(returnAcceptanceSave.getOrganizationId(),
					returnAcceptanceSave.getUnitId(), returnAcceptanceSave.getUserId());
			batch.addStockTransaction(stockTransaction);
		}
	}

	private void saveRejectDetails(ReturnAcceptanceSave returnAcceptanceSave) {
		Timestamp now = new Timestamp((new DateTime()).getMillis());
		/*
		 * boolean flag = false;
		 * 
		 * Integer storeId = sessionService.getStoreId();
		 * //System.out.println("StoreId()" + sessionService.getStoreId());
		 * 
		 * Store storeNew = GlobalDao.getStore(storeId);
		 * 
		 * Unit unit = GlobalDao.getUnit(sessionService.getUnitId());
		 * 
		 * //System.out.println("UnitId()" + sessionService.getUnitId());
		 * 
		 * String rejectBillNumber =
		 * GlobalService.getBillNoByGeneratedCode(sessionService.getStoreId(),
		 * sessionService.getUnitId(), screenId);
		 * 
		 * Reject reject = new Reject(rejectBillNumber, new
		 * Issue(returnAcceptanceSave.getId()), new User(sessionService.getUserId()),
		 * now, new PatientIssueReturn(returnAcceptanceSave.getId()), new
		 * Status(com.param.common.constants.Status.Pending.getStatus()));
		 * //sessionService.setEntityDefaults(reject);
		 * reject.setCreateDefaults(returnAcceptanceSave.getOrganizationId()!=null ?
		 * returnAcceptanceSave.getOrganizationId() :
		 * sessionService.getOrganizationId(),returnAcceptanceSave.getUnitId() !=null ?
		 * returnAcceptanceSave.getUnitId() :sessionService.getUnitId()
		 * ,returnAcceptanceSave.getUserId()!=null ? returnAcceptanceSave.getUserId() :
		 * sessionService.getUserId());
		 * 
		 * for (ReturnAcceptanceSaveItem issueDetailItem :
		 * returnAcceptanceSave.getItems()) { if (issueDetailItem.getRejectedQuantity()
		 * > 0) { flag = true; RejectDetail rejectDetail = new RejectDetail( new
		 * IssueDetailBatchMapper(issueDetailItem.getIssueDetailBatchMapper()),
		 * issueDetailItem.getRejectedQuantity(), (issueDetailItem.getRejectReason() !=
		 * null) ? new RejectReason(issueDetailItem.getRejectReason()) : null,
		 * issueDetailItem.getRejectedNote(), new
		 * PatientIssueReturnDetail(issueDetailItem.getId()));
		 * //sessionService.setEntityDefaults(rejectDetail);
		 * rejectDetail.setCreateDefaults(returnAcceptanceSave.getOrganizationId()!=null
		 * ? returnAcceptanceSave.getOrganizationId() :
		 * sessionService.getOrganizationId(),returnAcceptanceSave.getUnitId() !=null ?
		 * returnAcceptanceSave.getUnitId() :sessionService.getUnitId()
		 * ,returnAcceptanceSave.getUserId()!=null ? returnAcceptanceSave.getUserId() :
		 * sessionService.getUserId());
		 * 
		 * reject.addRejectDetail(rejectDetail); } // } if (flag == true) {
		 * returnAcceptanceMedicationDao.saveRejectDetails(reject); }
		 */

		boolean flag = false;

		String rejectBillNumber = GlobalService.getBillNoByGeneratedCode(returnAcceptanceSave.getStoreId(),
				returnAcceptanceSave.getUnitId(), screenId);

		RejectedRejection rejectedRejection = new RejectedRejection(rejectBillNumber,
				new Store(returnAcceptanceSave.getStoreId()), new Issue(returnAcceptanceSave.getIssueId()),
				new Status(com.param.common.constants.Status.Pending.getStatus()));
		rejectedRejection.setPatientIssueReturn(new PatientIssueReturn(returnAcceptanceSave.getId()));
		if (returnAcceptanceSave.getWardId() != null) {
			rejectedRejection.setWard(new WardMasters(returnAcceptanceSave.getWardId()));

		}

		rejectedRejection.setCreateDefaults(
				returnAcceptanceSave.getOrganizationId() != null ? returnAcceptanceSave.getOrganizationId()
						: sessionService.getOrganizationId(),
				returnAcceptanceSave.getUnitId() != null ? returnAcceptanceSave.getUnitId()
						: sessionService.getUnitId(),
				returnAcceptanceSave.getUserId() != null ? returnAcceptanceSave.getUserId()
						: sessionService.getUserId());
		for (ReturnAcceptanceSaveItem issueDetailItem : returnAcceptanceSave.getItems()) {
			if (issueDetailItem.getRejectedQuantity() > 0) {
				flag = true;
				RejectedRejectionDetail rejectedRejectionDetail = new RejectedRejectionDetail(
						new PatientIssueReturnDetail(issueDetailItem.getId()),
						new IssueDetailBatchMapper(issueDetailItem.getIssueDetailBatchMapper()),
						(issueDetailItem.getAcceptQuantity() != null) ? issueDetailItem.getAcceptQuantity() : null,
						(issueDetailItem.getRejectedQuantity() != null) ? issueDetailItem.getRejectedQuantity() : null,
						(issueDetailItem.getRejectReason() != null)
								? new RejectReason(issueDetailItem.getRejectReason())
								: null,
						(issueDetailItem.getRejectedNote() != null) ? issueDetailItem.getRejectedNote() : null,
						new Status(com.param.common.constants.Status.Pending.getStatus()));
				rejectedRejection.addRejectedRejectionDetail(rejectedRejectionDetail);
				// sessionService.setEntityDefaults(rejectedRejectionDetail);
				rejectedRejectionDetail.setCreateDefaults(
						returnAcceptanceSave.getOrganizationId() != null ? returnAcceptanceSave.getOrganizationId()
								: sessionService.getOrganizationId(),
						returnAcceptanceSave.getUnitId() != null ? returnAcceptanceSave.getUnitId()
								: sessionService.getUnitId(),
						returnAcceptanceSave.getUserId() != null ? returnAcceptanceSave.getUserId()
								: sessionService.getUserId());

				// status column need to be added in detail

			}
		}
		if (flag == true) {
			patientIssuesRejectionMedicationDao.patientIssuesRejectedRejectionMedicationSave(rejectedRejection);
		}
	}

	@Override
	public Response patientReturnAcceptanceStatus(ReturnAcceptanceSave returnAcceptanceSave) {
		Integer patientReturnAcceptance = patientIssueReturnDao.patientReturnAcceptanceStatus(returnAcceptanceSave);
		return new Response<>(null, null, null, null, patientReturnAcceptance); 
	}

}
