package com.param.pharmacy.ip.service.returnAcceptance.consumable;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.adt.WardMasters;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.inventory.StockTransaction;
import com.param.entity.model.inventory.StoreStock;
import com.param.entity.model.master.RejectReason;
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
import com.param.global.common.Response;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dto.BillingMasterDto;
import com.param.global.dto.OrderDetailsMasterDto;
import com.param.global.dto.global.CommonDropdown;
import com.param.global.pharmacy.service.IWalkInSalesService;
import com.param.global.service.IMPackageMasterService;
import com.param.global.service.global.IGlobalService;
import com.param.global.utility.Util;
import com.param.pharmacy.ip.dao.patientIssueRejection.IPatientIssuesRejectionMedicationDao;
import com.param.pharmacy.ip.dao.returnAcceptance.consumable.IReturnAcceptanceConsumableDao;
import com.param.pharmacy.ip.dto.returnAcceptance.PharmacySaleReturnDto;
import com.param.pharmacy.ip.dto.returnAcceptance.ReturnAcceptanceItem;
import com.param.pharmacy.ip.dto.returnAcceptance.ReturnAcceptanceRequest;
import com.param.pharmacy.ip.dto.returnAcceptance.ReturnAcceptanceResponse;
import com.param.pharmacy.ip.dto.returnAcceptance.ReturnAcceptanceSave;
import com.param.pharmacy.ip.dto.returnAcceptance.ReturnAcceptanceSaveItem;
import com.param.pharmacy.nursing.workstation.dao.patientIssueReturn.IPatientIssueReturnDao;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service("returnAcceptanceConsumableService")
@Transactional
public class ReturnAcceptanceConsumableService implements IReturnAcceptanceConsumableService {
	@Autowired
	private IReturnAcceptanceConsumableDao returnAcceptanceConsumableDao;

	@Autowired
	private IGlobalDao GlobalDao;

	@Autowired
	private IPatientIssueReturnDao patientIssueReturnDao;

	private Integer screenId = Screen.PatientReturnAcceptance.getScreen();

//	private Integer screenId = Screen.PatientReturnAcceptance.getScreen();
	@Autowired
	private SessionService sessionService;

	@Autowired
	IMPackageMasterService iMPackageMasterService;

	@Autowired
	IWalkInSalesService iWalkInSalesService;

	@Autowired
	IGlobalService GlobalService;

	@Autowired
	private IPatientIssuesRejectionMedicationDao patientIssuesRejectionMedicationDao;

	private com.param.entity.model.master.Screen screen;

	@Override
	@Transactional
	public CommonDropdown getDropdownList(Integer unitId) {
		CommonDropdown response = new CommonDropdown();

		for (VisitType visitType : GlobalDao.getVisitTypeList()) {
			response.addVisitType(new com.param.global.dto.global.VisitType(visitType.getVisitTypeId(),
					visitType.getVisitTypeName()));
		}

		for (Store store : GlobalDao.getStoreList(unitId)) {
			response.addStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
		}

		return response;
	}

	@Override
	@Transactional
	public List<ReturnAcceptanceResponse> returnAcceptanceSearch(ReturnAcceptanceRequest returnAcceptanceRequest) {

		List<ReturnAcceptanceResponse> patientIssueReturnList = new ArrayList<ReturnAcceptanceResponse>();
		returnAcceptanceRequest.setToDate((new DateTime(returnAcceptanceRequest.getToDate())).plusDays(1).toDate());
		for (PatientIssueReturn patientIssueReturn : returnAcceptanceConsumableDao
				.returnAcceptanceSearch(returnAcceptanceRequest, screenId)) {
			ReturnAcceptanceResponse returnAcceptanceResponse = new ReturnAcceptanceResponse();
			Integer age = Util
					.getAgeByBirthDate(patientIssueReturn.getPatient().getDob().toLocalDateTime().toLocalDate());

			String doctorDetail = "";
			if (patientIssueReturn.getPatientIssueReturnDetail().get(0).getIssueDetailBatchMapper().getIssueDetail()
					.getIssue().getDoctor() != null) {
				doctorDetail = patientIssueReturn.getPatientIssueReturnDetail().get(0).getIssueDetailBatchMapper()
						.getIssueDetail().getIssue().getDoctor().getFirstName()
						+ " "
						+ patientIssueReturn.getPatientIssueReturnDetail().get(0).getIssueDetailBatchMapper()
								.getIssueDetail().getIssue().getDoctor().getMiddleName()
						+ " " + patientIssueReturn.getPatientIssueReturnDetail().get(0).getIssueDetailBatchMapper()
								.getIssueDetail().getIssue().getDoctor().getLastName();
			}
			/*
			 * if( patientIssueReturn.getPatientIssueReturnDetail().get(0).
			 * getIssueDetailBatchMapper()
			 * .getIssueDetail().getIssue().getSpecialityMaster()!=null) { doctorDetail =
			 * doctorDetail+ " | " +
			 * patientIssueReturn.getPatientIssueReturnDetail().get(0).
			 * getIssueDetailBatchMapper()
			 * .getIssueDetail().getIssue().getSpecialityMaster().getSpecialityName(); }
			 */

			String patientDetail = patientIssueReturn.getPatient().getPrefix().getPrefixCode() + " "
					+ patientIssueReturn.getPatient().getFirstName() + " "
					+ patientIssueReturn.getPatient().getMiddleName() + " "
					+ patientIssueReturn.getPatient().getLastName() + " | " + age + " | "
					+ patientIssueReturn.getPatient().getGender().getGenderCode();
			patientIssueReturn.getPatient().getGender().getGenderId();
			patientIssueReturn.getPatient().getImagePath();
			returnAcceptanceResponse = new ReturnAcceptanceResponse(patientIssueReturn.getId(),
					patientIssueReturn.getReturnNo(), patientIssueReturn.getAddedDate(),
					patientIssueReturn.getPatient().getUhidNumber(), patientDetail, doctorDetail,
					patientIssueReturn.getVisitType().getVisitTypeCode(),
					patientIssueReturn.getTostore().getStoreName(),
					patientIssueReturn.getPatientIssueReturnDetail().get(0).getIssueDetailBatchMapper().getIssueDetail()
							.getIssue().getPatientIndent().getIndentNo());
			returnAcceptanceResponse.setStatus(patientIssueReturn.getStatus().getStatus());
			returnAcceptanceResponse.setStatusId(patientIssueReturn.getStatus().getId());
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
				returnAcceptanceResponse.setVisitNo((patientIssueReturn.getAdmission() != null)
						? patientIssueReturn.getAdmission().getAdmissionNumber()
						: null);
			} else if (patientIssueReturn.getEncounter() != null) {
				returnAcceptanceResponse.setVisitNo(patientIssueReturn.getEncounter().getEncounterNumber());

			}
			patientIssueReturnList.add(returnAcceptanceResponse);
		}
		return patientIssueReturnList;

	}

	@Override
	@Transactional
	public ReturnAcceptanceResponse returnAcceptanceDetails(Integer id, Integer unitId) {
		ReturnAcceptanceResponse returnAcceptanceResponse = new ReturnAcceptanceResponse();
		PatientIssueReturn patientIssueReturn = patientIssueReturnDao.patientIssueReturnById(id, unitId);

		String doctorDetail = "";
		if (patientIssueReturn.getPatientIssueReturnDetail().get(0).getIssueDetailBatchMapper().getIssueDetail()
				.getIssue().getDoctor() != null
				&& patientIssueReturn.getPatientIssueReturnDetail().get(0).getIssueDetailBatchMapper().getIssueDetail()
						.getIssue().getSpecialityMaster() != null) {
			doctorDetail = patientIssueReturn.getPatientIssueReturnDetail().get(0).getIssueDetailBatchMapper()
					.getIssueDetail().getIssue().getDoctor().getFirstName()
					+ " "
					+ patientIssueReturn.getPatientIssueReturnDetail().get(0).getIssueDetailBatchMapper()
							.getIssueDetail().getIssue().getDoctor().getMiddleName()
					+ " "
					+ patientIssueReturn.getPatientIssueReturnDetail().get(0).getIssueDetailBatchMapper()
							.getIssueDetail().getIssue().getDoctor().getLastName()
					+ " | " + patientIssueReturn.getPatientIssueReturnDetail().get(0).getIssueDetailBatchMapper()
							.getIssueDetail().getIssue().getSpecialityMaster().getSpecialityName();
		}

		Integer age = Util.getAgeByBirthDate(patientIssueReturn.getPatient().getDob().toLocalDateTime().toLocalDate());
		String patientDetail = patientIssueReturn.getPatient().getPrefix().getPrefixCode() + " "
				+ patientIssueReturn.getPatient().getFirstName() + " " + patientIssueReturn.getPatient().getMiddleName()
				+ " " + patientIssueReturn.getPatient().getLastName() + " | " + age + " | "
				+ patientIssueReturn.getPatient().getGender().getGenderCode();

		returnAcceptanceResponse = new ReturnAcceptanceResponse(patientIssueReturn.getId(),
				patientIssueReturn.getReturnNo(), patientIssueReturn.getAddedDate(),
				patientIssueReturn.getPatient().getUhidNumber(), patientDetail, doctorDetail,
				patientIssueReturn.getVisitType().getVisitTypeCode(), patientIssueReturn.getTostore().getStoreName(),
				patientIssueReturn.getPatient().getPatientId());
		returnAcceptanceResponse.setStoreId(patientIssueReturn.getTostore().getStoreId());
		returnAcceptanceResponse.setIndentId(patientIssueReturn.getPatientIssueReturnDetail().get(0)
				.getIssueDetailBatchMapper().getIssueDetail().getIssue().getPatientIndent().getId());
		returnAcceptanceResponse.setIndentNo(patientIssueReturn.getPatientIssueReturnDetail().get(0)
				.getIssueDetailBatchMapper().getIssueDetail().getIssue().getPatientIndent().getIndentNo());
		returnAcceptanceResponse.setStatus(patientIssueReturn.getStatus().getStatus());
		returnAcceptanceResponse.setStatusId(patientIssueReturn.getStatus().getId());
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
		List<com.param.entity.model.master.ReturnReason> reasonList = new ArrayList<>();
		reasonList = GlobalDao.getReturnReason(unitId);
		for (PatientIssueReturnDetail patientIssueReturnDetail : patientIssueReturnDao.patientIssueReturnDetailById(id,
				unitId)) {
			Double getActualRate=GlobalDao.getActualRateFromOpeningBalance(patientIssueReturnDetail.getIssueDetailBatchMapper().getBatch().getId(),
            		unitId);
			ReturnAcceptanceItem item = new ReturnAcceptanceItem(patientIssueReturnDetail.getId(),
					patientIssueReturnDetail.getIssueDetailBatchMapper().getBatch().getItem().getId(),
					patientIssueReturnDetail.getIssueDetailBatchMapper().getBatch().getItem().getName(),
					patientIssueReturnDetail.getIssueDetailBatchMapper().getBatch().getItem().getGeneric().getName(),
					patientIssueReturnDetail.getIssueDetailBatchMapper().getBatch().getBatchNo(),
					patientIssueReturnDetail.getIssueDetailBatchMapper().getBatch().getExpiryDate(),
					patientIssueReturnDetail.getIssueDetailBatchMapper().getUomType().getType(),
					patientIssueReturnDetail.getIssueDetailBatchMapper().getBatch().getUomUnit().getUnits(),
					patientIssueReturnDetail.getIssueDetailBatchMapper().getBatch().getSellingPrice(),
					patientIssueReturnDetail.getReturnQuantity() != null ? patientIssueReturnDetail.getReturnQuantity()
							: patientIssueReturnDetail.getIssueDetailBatchMapper().getAcceptedQuantity(),
					patientIssueReturnDetail.getReturnReason().getReasonDesc(),
					patientIssueReturnDetail.getIssueDetailBatchMapper().getId(),
					patientIssueReturnDetail.getIssueDetailBatchMapper().getBatch().getItem().getGeneric().getId(),
					patientIssueReturnDetail.getIssueDetailBatchMapper().getBatch().getItem().getIsThreeUOMApplicable(),
					GlobalDao.getUnitOfMeasurementListByItem(patientIssueReturnDetail.getIssueDetailBatchMapper().getBatch().getItem().getId(), patientIssueReturn.getOrganizationId()));
			item.setMrp(getActualRate);
			item.setActualLendingPrice(patientIssueReturnDetail.getIssueDetailBatchMapper().getBatch().getActualLendingPrice());
			item.setReasonForReturn(patientIssueReturnDetail.getReturnReason().getReasonDesc());
			item.setStatus(patientIssueReturnDetail.getStatus().getStatus());
			item.setGenericId(
					patientIssueReturnDetail.getIssueDetailBatchMapper().getBatch().getItem().getGeneric().getId());
			item.setStatusId(patientIssueReturnDetail.getStatus().getId());
			item.setIndentDetailId(patientIssueReturnDetail.getIssueDetailBatchMapper().getIssueDetail()
					.getPatientIndentDetail().getId());
			item.setReturnAcceptedQuantity(
					patientIssueReturnDetail.getAcceptQuantity() != null ? patientIssueReturnDetail.getAcceptQuantity()
							: 0);
			if (returnAcceptanceResponse.getStatusId().equals(2)) {
				item.setRejectedQuantity(0);

			} else {
				item.setRejectedQuantity(patientIssueReturnDetail.getRejectQuantity() != null
						? patientIssueReturnDetail.getRejectQuantity()
						: 0);

			}
			if (reasonList != null && reasonList.size() > 0) {
				for (com.param.entity.model.master.ReturnReason returnReason : reasonList) {
					if (returnReason.getId() == patientIssueReturnDetail.getReturnReason().getReasonMasterId()) {
						item.setReasonForReturn(returnReason.getReason());
					}
				}
			}
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
		dto.setCreatedBy(returnAcceptanceSave.getUserId() != null ? returnAcceptanceSave.getUserId()
				: sessionService.getUserId());

		List<OrderDetailsMasterDto> orderDetailsList = new ArrayList<OrderDetailsMasterDto>();
		for (PatientIssueReturnDetail patientIssueReturnDetail : patientIssueReturn.getPatientIssueReturnDetail()) {
			IssueDetailBatchMapper batchMapper = patientIssueReturnDetail.getIssueDetailBatchMapper();

			for (ReturnAcceptanceSaveItem issueDetailBatchMapper : returnAcceptanceSave.getItems()) {
				if (issueDetailBatchMapper.getAcceptQuantity() >= 0) {
					if (issueDetailBatchMapper.getIssueDetailBatchMapper() != null
							&& (batchMapper.getId().equals(issueDetailBatchMapper.getIssueDetailBatchMapper()))) {
						BatchMapperStoreStock(returnAcceptanceSave.getStoreId(), batchMapper.getBatch(),
								issueDetailBatchMapper.getAcceptQuantity(), batchMapper.getUomType(),
								returnAcceptanceSave, patientIssueReturn, patientIssueReturnDetail,batchMapper,issueDetailBatchMapper);
						batchMapper.setIsReturnAcceptance(true);
						/*
						 * batchMapper.updateIssueDetailBatchMapper(issueDetailBatchMapper.
						 * getAcceptQuantity(), issueDetailBatchMapper.getRejectedNote(),
						 * issueDetailBatchMapper.getRejectedQuantity(),
						 * (issueDetailBatchMapper.getRejectReason() != null) ? new
						 * RejectReason(issueDetailBatchMapper.getRejectReason()) : null);
						 */

						batchMapper.updateIssueDetailBatchMapper(issueDetailBatchMapper.getRejectedNote(),
								(issueDetailBatchMapper.getRejectReason() != null)
										? new RejectReason(issueDetailBatchMapper.getRejectReason())
										: null);
						Integer returnAcceptanceQty = 0;
						if (batchMapper.getReturnAcceptedQuantity() != null) {
							returnAcceptanceQty = (batchMapper.getReturnAcceptedQuantity() != null
									? batchMapper.getReturnAcceptedQuantity()
									: 0)
									+ (issueDetailBatchMapper.getAcceptQuantity() != null
											? issueDetailBatchMapper.getAcceptQuantity()
											: 0);
						} else {
							returnAcceptanceQty = (issueDetailBatchMapper.getAcceptQuantity() != null
									? issueDetailBatchMapper.getAcceptQuantity()
									: 0);

						}

						Integer returnRejectedQty = 0;
						if (batchMapper.getReturnRejectedQuantity() != null) {
							returnRejectedQty = batchMapper.getReturnRejectedQuantity()
									+ (issueDetailBatchMapper.getRejectedQuantity() != null
											? issueDetailBatchMapper.getRejectedQuantity()
											: 0);
						} else {
							returnRejectedQty = (issueDetailBatchMapper.getRejectedQuantity() != null
									? issueDetailBatchMapper.getRejectedQuantity()
									: 0);

						}
						patientIssueReturnDetail.setAcceptQuantity((issueDetailBatchMapper.getAcceptQuantity() != null
								? issueDetailBatchMapper.getAcceptQuantity()
								: 0));
						patientIssueReturnDetail.setRejectQuantity((issueDetailBatchMapper.getRejectedQuantity() != null
								? issueDetailBatchMapper.getRejectedQuantity()
								: 0));
						batchMapper.setReturnAcceptedQuantity(returnAcceptanceQty);
						batchMapper.setReturnRejectedQuantity(returnRejectedQty);

						/*
						 * PharmacySaleReturnDto saleDetailids = patientIssueReturnDao
						 * .getSaleDetailIdByIssueDetailMapper(issueDetailBatchMapper.
						 * getIssueDetailBatchMapper());
						 */

						PharmacySaleReturnDto phSRDto = patientIssueReturnDao
								.getSaleDetailIdByIssueDetailMapper(issueDetailBatchMapper.getIssueDetailBatchMapper());

						// System.out.println("saledetailsID:=" + phSRDto.getSaleDetailId());
						if(phSRDto != null && phSRDto.getSaleDetailId() != null){
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
						if (issueDetailBatchMapper.getAcceptQuantity() >0) {
							orderDetailsList.add(orderDtlsDto);
						}
						}
						
					}
				}
			}
		}

		patientIssueReturn.setStatus(new Status(returnAcceptanceSave.getStatusId()));
		patientIssueReturn.setUpdatedByUser(returnAcceptanceSave.getUserId() != null ? returnAcceptanceSave.getUserId()
				: sessionService.getUserId());
		patientIssueReturn.setUpdatedDate(TimeZoneComponent.getDateByzone(patientIssueReturn.getUnitId()));

		returnAcceptanceConsumableDao.updatePatientIssueReturn(patientIssueReturn);

		// sales return
		if(!orderDetailsList.isEmpty())
		{
			iWalkInSalesService.pharamcyPackageContractReturn(dto, orderDetailsList);	
		}
		

		saveRejectDetails(returnAcceptanceSave);

		CommonResponse response = new CommonResponse();
		response.setId(patientIssueReturn.getId().longValue());

		response.setMessage("Patient Return Acceptance saved successfully");

		return response;

	}

	private void BatchMapperStoreStock(Integer storeId, Batch batch, Integer issueQuantity, UomType uomType,
			ReturnAcceptanceSave returnAcceptanceSave, PatientIssueReturn patientIssueReturn,
			PatientIssueReturnDetail patientIssueReturnDetail, IssueDetailBatchMapper bm, ReturnAcceptanceSaveItem issueDetailBatchMapper) {

		boolean hasStock = false;

		List<StoreStock> storeList = GlobalDao.getStoreStockByBatchAndStoreList(batch.getId(), storeId,
				batch.getUnitId());
		for (StoreStock storeStock : storeList) {
			if (storeStock.getStore().getStoreId().equals(storeId)) {
				if (!storeStock.getTotalQty().equals(null)) {
					Integer avaialableStoreStock = GlobalDao.getStoreStockByBatch(storeId, batch.getId());

					Integer leaseQuantity = Util.GetLeaseQuantity(issueQuantity, batch.getBatchUomList(), uomType);

					Integer balanceQuantity = avaialableStoreStock + leaseQuantity;

					// storeStock.setAvailableQty(balanceQuantity);
					Integer rejectQty = issueDetailBatchMapper.getRejectedQuantity() != null
							? issueDetailBatchMapper.getRejectedQuantity()
							: 0;
					rejectQty = Util.GetLeaseQuantity(rejectQty, batch.getBatchUomList(), uomType);
					Integer currentQuantity = batch.getCurrentQuantity() + leaseQuantity;
					// batch.setCurrentQuantity(currentQuantity);
					Integer qtyT = (storeStock.gettQty() != null ? storeStock.gettQty() : 0) - leaseQuantity;
					storeStock.settQty(qtyT < 0 ? 0 : qtyT);
					storeStock.setAvailableQty(
							(storeStock.getAvailableQty() != null ? storeStock.getAvailableQty() : 0) + leaseQuantity);
					GlobalDao.updateStoreStockLeaseQuantity(storeStock);
					StockTransaction stockTransaction = new StockTransaction(balanceQuantity, screen, screen.getCode(),
							rejectQty, batch.getStoreStockList().get(0).getStore(), patientIssueReturnDetail.getId());
					stockTransaction.setIsTransit(false);
					stockTransaction.setInQty(leaseQuantity);
					stockTransaction.setTransactionId(patientIssueReturn.getId());
					stockTransaction.setItem(batch.getItem());
					stockTransaction.setTransitNo(patientIssueReturn.getReturnNo());
					stockTransaction.setCreateDefaults(returnAcceptanceSave.getOrganizationId(),
							returnAcceptanceSave.getUnitId(), returnAcceptanceSave.getUserId());
					batch.addStockTransaction(stockTransaction);
				}

				// hasStock = true;

				// batch.addStockTransaction(new StockTransaction(balanceQuantity, screen,
				// screen.getCode(), leaseQuantity,
				// batch.getStoreStockList().get(0).getStore()));
				break;
			}
		}
//As discussed with divyesh , vishal,rahul
		/*
		 * for (StoreStock storeStock : batch.getStoreStockList()) { if
		 * (patientIssueReturn.getTostore() != null) { if
		 * (storeStock.getStore().getStoreId().equals(patientIssueReturn.getTostore().
		 * getStoreId())) {
		 * 
		 * hasStock = true; Integer avaialableStoreStock = GlobalDao
		 * .getStoreStockByBatch(patientIssueReturn.getTostore().getStoreId(),
		 * batch.getId());
		 * 
		 * Integer leaseQuantity = Util.GetLeaseQuantity(issueQuantity,
		 * batch.getBatchUomList(), uomType);
		 * 
		 * Integer balanceQuantity = avaialableStoreStock + leaseQuantity;
		 * 
		 * // storeStock.setAvailableQty(balanceQuantity);
		 * 
		 * Integer currentQuantity = batch.getCurrentQuantity() + leaseQuantity; //
		 * batch.setCurrentQuantity(currentQuantity);
		 * storeStock.setAvailableQty(balanceQuantity);
		 * GlobalDao.updateStoreStockLeaseQuantity(storeStock); StockTransaction
		 * stockTransaction = new StockTransaction(balanceQuantity, screen,
		 * screen.getCode(), leaseQuantity,
		 * batch.getStoreStockList().get(0).getStore());
		 * stockTransaction.setCreateDefaults(returnAcceptanceSave.getOrganizationId(),
		 * returnAcceptanceSave.getUnitId(), returnAcceptanceSave.getUserId());
		 * batch.addStockTransaction(stockTransaction); // batch.addStockTransaction(new
		 * StockTransaction(balanceQuantity, screen, // screen.getCode(), leaseQuantity,
		 * // batch.getStoreStockList().get(0).getStore())); } }
		 * 
		 * }
		 * 
		 * if (!hasStock) { Integer leaseQuantity = Util.GetLeaseQuantity(issueQuantity,
		 * batch.getBatchUomList(), uomType); StoreStock StoreStock = new
		 * StoreStock(leaseQuantity, new
		 * Store(patientIssueReturn.getTostore().getStoreId()), new
		 * Batch(batch.getId()));
		 * 
		 * // sessionService.setEntityDefaults(StoreStock);
		 * StoreStock.setCreateDefaults( returnAcceptanceSave.getOrganizationId() !=
		 * null ? returnAcceptanceSave.getOrganizationId() :
		 * sessionService.getOrganizationId(), returnAcceptanceSave.getUnitId() != null
		 * ? returnAcceptanceSave.getUnitId() : sessionService.getUnitId(),
		 * returnAcceptanceSave.getUserId() != null ? returnAcceptanceSave.getUserId() :
		 * sessionService.getUserId()); Integer avaialableStoreStock =
		 * GlobalDao.getStoreStockByBatch(patientIssueReturn.getTostore().getStoreId(),
		 * batch.getId()); StoreStock.setAvailableQty(leaseQuantity);
		 * StoreStock.setTotalQty(leaseQuantity);
		 * GlobalDao.updateStoreStockLeaseQuantity(StoreStock); StockTransaction
		 * stockTransaction = new StockTransaction(avaialableStoreStock, screen,
		 * screen.getCode(), leaseQuantity,
		 * batch.getStoreStockList().get(0).getStore());
		 * stockTransaction.setCreateDefaults(returnAcceptanceSave.getOrganizationId(),
		 * returnAcceptanceSave.getUnitId(), returnAcceptanceSave.getUserId());
		 * batch.addStockTransaction(stockTransaction); }
		 */
	}

	private void saveRejectDetails(
			ReturnAcceptanceSave returnAcceptanceSave) {/*
														 * Timestamp now = new Timestamp((new DateTime()).getMillis());
														 * boolean flag = false;
														 * 
														 * 
														 * Integer storeId = sessionService.getStoreId();
														 * System.out.println("StoreId()" +
														 * sessionService.getStoreId());
														 * 
														 * Store storeNew = GlobalDao.getStore(storeId);
														 * 
														 * Unit unit = GlobalDao.getUnit(sessionService.getUnitId());
														 * 
														 * System.out.println("UnitId()" + sessionService.getUnitId());
														 * 
														 * 
														 * String rejectBillNumber =
														 * GlobalService.getBillNoByGeneratedCode(sessionService.
														 * getStoreId(), sessionService.getUnitId(), screenId);
														 * 
														 * Reject reject = new Reject(rejectBillNumber, new
														 * Issue(returnAcceptanceSave.getId()), new
														 * User(sessionService.getUserId()), now, new
														 * PatientIssueReturn(returnAcceptanceSave.getId()), new
														 * Status(com.param.common.constants.Status.Pending.getStatus())
														 * ); // sessionService.setEntityDefaults(reject);
														 * reject.setCreateDefaults(
														 * returnAcceptanceSave.getOrganizationId() != null ?
														 * returnAcceptanceSave.getOrganizationId() :
														 * sessionService.getOrganizationId(),
														 * returnAcceptanceSave.getUnitId() != null ?
														 * returnAcceptanceSave.getUnitId() :
														 * sessionService.getUnitId(), returnAcceptanceSave.getUserId()
														 * != null ? returnAcceptanceSave.getUserId() :
														 * sessionService.getUserId());
														 * 
														 * for (ReturnAcceptanceSaveItem issueDetailItem :
														 * returnAcceptanceSave.getItems()) { if
														 * (issueDetailItem.getRejectedQuantity() > 0) { flag = true;
														 * RejectDetail rejectDetail = new RejectDetail( new
														 * IssueDetailBatchMapper(issueDetailItem.
														 * getIssueDetailBatchMapper()),
														 * issueDetailItem.getRejectedQuantity(),
														 * (issueDetailItem.getRejectReason() != null) ? new
														 * RejectReason(issueDetailItem.getRejectReason()) : null,
														 * issueDetailItem.getRejectedNote(), new
														 * PatientIssueReturnDetail(issueDetailItem.getId())); //
														 * sessionService.setEntityDefaults(rejectDetail);
														 * rejectDetail.setCreateDefaults(
														 * returnAcceptanceSave.getOrganizationId() != null ?
														 * returnAcceptanceSave.getOrganizationId() :
														 * sessionService.getOrganizationId(),
														 * returnAcceptanceSave.getUnitId() != null ?
														 * returnAcceptanceSave.getUnitId() :
														 * sessionService.getUnitId(), returnAcceptanceSave.getUserId()
														 * != null ? returnAcceptanceSave.getUserId() :
														 * sessionService.getUserId());
														 * 
														 * reject.addRejectDetail(rejectDetail); } } if (flag == true) {
														 * returnAcceptanceConsumableDao.saveRejectDetails(reject); }
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
    @Transactional
    public Response patientReturnAcceptanceStatus(ReturnAcceptanceSave returnAcceptanceSave) {
        Integer patientReturnAcceptance = patientIssueReturnDao.patientReturnAcceptanceStatus(returnAcceptanceSave);
        return new Response<>(null, null, null, null, patientReturnAcceptance); 
    }
}