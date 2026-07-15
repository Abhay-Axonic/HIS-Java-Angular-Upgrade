package com.param.procurement.consignment.service.implantReturnAcceptance;

import java.sql.Timestamp;
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
import com.param.entity.model.master.User;
import com.param.entity.model.pharmacy.Issue;
import com.param.entity.model.pharmacy.IssueDetailBatchMapper;
import com.param.entity.model.pharmacy.PatientIssueReturn;
import com.param.entity.model.pharmacy.PatientIssueReturnDetail;
import com.param.entity.model.pharmacy.Reject;
import com.param.entity.model.pharmacy.RejectDetail;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dto.BillingMasterDto;
import com.param.global.dto.OrderDetailsMasterDto;
import com.param.global.dto.global.Ward;
import com.param.global.service.global.IGlobalService;
import com.param.global.utility.Util;
import com.param.procurement.consignment.dao.ImplantReturnAcceptance.IImplantReturnAcceptanceDao;
import com.param.procurement.consignment.dao.consignmentReturn.IConsignmentReturnDao;
import com.param.procurement.consignment.dto.implantReturnAcceptance.ImplantReturnAcceptanceDropdown;
import com.param.procurement.consignment.dto.implantReturnAcceptance.ImplantReturnAcceptanceRequest;
import com.param.procurement.consignment.dto.implantReturnAcceptance.ImplantReturnAcceptanceResponse;
import com.param.procurement.consignment.dto.implantReturnAcceptance.ImplantReturnAcceptanceResponseItems;
import com.param.procurement.consignment.dto.implantReturnAcceptance.ImplantReturnAcceptanceSave;
import com.param.procurement.consignment.dto.implantReturnAcceptance.ImplantReturnAcceptanceSaveItem;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service("ImplantReturnAcceptanceService")
@Transactional
public class ImplantReturnAcceptanceService implements IImplantReturnAcceptanceService {

	@Autowired
	private SessionService sessionService;

	@Autowired
	private IImplantReturnAcceptanceDao implantReturnAcceptanceConsignmentDao;

	@Autowired
	private IGlobalDao GlobalDao;

	@Autowired
	IGlobalService GlobalService;
	
	@Autowired
	private IConsignmentReturnDao consignmentReturnDao;

	private Integer screenId = Screen.ReturnAcceptance.getScreen();
	
	private Integer rejectScreenId = Screen.PatientReject.getScreen();
	
	private Integer POScreenId = Screen.ConsignmentPurchaseOrder.getScreen();
	
	private com.param.entity.model.master.Screen screen;

	@Override
	@Transactional
	public ImplantReturnAcceptanceDropdown getDropdownList(Integer unitId) {
		ImplantReturnAcceptanceDropdown response = new ImplantReturnAcceptanceDropdown();

		
		for (com.param.entity.model.master.VisitType visitType : GlobalDao.getVisitTypeList())
		{
			response.addVisitType(new com.param.global.dto.global.VisitType(visitType.getVisitTypeId(), visitType.getVisitTypeCode()));
		}
		for (WardMasters ward : GlobalDao.getWardList(unitId))
		{
			response.addWard(new Ward(ward.getWardId(), ward.getWardName()));
		}

		return response;
	}
	
	//Consignment Return Acceptance Main Search
	@Override
	@Transactional
	public List<ImplantReturnAcceptanceResponse> consignmentReturnAcceptanceSearch(ImplantReturnAcceptanceRequest consignmentReturnAcceptanceRequest) {
		consignmentReturnAcceptanceRequest.setToDate((new DateTime(consignmentReturnAcceptanceRequest.getToDate())).plusDays(1).toDate());
		
		List<ImplantReturnAcceptanceResponse> consignmentReturnAcceptanceResponseList = new ArrayList<ImplantReturnAcceptanceResponse>();
		for (PatientIssueReturn patientIssueReturn : implantReturnAcceptanceConsignmentDao.consignmentReturnAcceptanceSearch(consignmentReturnAcceptanceRequest, screenId)) {
			ImplantReturnAcceptanceResponse consignmentReturnAcceptanceResponse = new ImplantReturnAcceptanceResponse();
			
			/*List<DoctorSpecialityMapper> doctorSpeciality = GlobalDao.doctorSearchByIdValue(patientIssueReturn.getPatientIssueReturnDetail().get(0)
							.getIssueDetailBatchMapper().getIssueDetail().getIssue().getDoctor().getDoctorId());*/
			String doctorDetail = "";
			if(patientIssueReturn.getPatientIssueReturnDetail().get(0).getIssueDetailBatchMapper()
					.getIssueDetail().getIssue()!=null)
			{
				doctorDetail = patientIssueReturn.getPatientIssueReturnDetail().get(0).getIssueDetailBatchMapper()
						.getIssueDetail().getIssue().getDoctor().getFirstName()+ " "+ patientIssueReturn.getPatientIssueReturnDetail().get(0).getIssueDetailBatchMapper()
						.getIssueDetail().getIssue().getDoctor().getMiddleName()+ " "+ patientIssueReturn.getPatientIssueReturnDetail().get(0).getIssueDetailBatchMapper()
						.getIssueDetail().getIssue().getDoctor().getLastName()+ " | " +patientIssueReturn.getPatientIssueReturnDetail().get(0).getIssueDetailBatchMapper()
						.getIssueDetail().getIssue().getSpecialityMaster().getSpecialityName();
			}
			

			Integer age = Util.getAgeByBirthDate(patientIssueReturn.getPatient().getDob().toLocalDateTime().toLocalDate());
			String patientDetail = patientIssueReturn.getPatient().getPrefix().getPrefixCode() + " "+ patientIssueReturn.getPatient().getFirstName() + " "
					+ patientIssueReturn.getPatient().getMiddleName() + " "	+ patientIssueReturn.getPatient().getLastName() + " | " + age + " | "
					+ patientIssueReturn.getPatient().getGender().getGenderCode();

			consignmentReturnAcceptanceResponse = new ImplantReturnAcceptanceResponse(patientIssueReturn.getId(),
					patientIssueReturn.getReturnNo(), patientIssueReturn.getAddedDate(),patientIssueReturn.getPatient().getUhidNumber(), patientDetail, doctorDetail,
					patientIssueReturn.getVisitType().getVisitTypeCode(), patientIssueReturn.getStore().getStoreName());

			consignmentReturnAcceptanceResponse.setStatus(patientIssueReturn.getStatus().getStatus());
			consignmentReturnAcceptanceResponse.setStatusId(patientIssueReturn.getStatus().getId());
			if (patientIssueReturn.getAdmission() != null)
			{
				consignmentReturnAcceptanceResponse.setWardName((patientIssueReturn.getWardMaster() != null) ? patientIssueReturn.getWardMaster().getWardName(): null);
				consignmentReturnAcceptanceResponse.setWardId((patientIssueReturn.getWardMaster() != null) ? patientIssueReturn.getWardMaster().getWardId(): null);
				consignmentReturnAcceptanceResponse.setBedNo((patientIssueReturn.getBedMaster() != null) ? patientIssueReturn.getBedMaster().getBedNumber(): null);
			}
			consignmentReturnAcceptanceResponseList.add(consignmentReturnAcceptanceResponse);

		}
		return consignmentReturnAcceptanceResponseList;
	}

		//Implant Return Acceptance Main Search
	
	@Override
	@Transactional
	public List<ImplantReturnAcceptanceResponse> implantReturnAcceptanceSearch(ImplantReturnAcceptanceRequest implantReturnAcceptanceRequest) {
		implantReturnAcceptanceRequest.setToDate((new DateTime(implantReturnAcceptanceRequest.getToDate())).plusDays(1).toDate());
		
		List<ImplantReturnAcceptanceResponse> implantReturnAcceptanceResponseList = new ArrayList<ImplantReturnAcceptanceResponse>();
		for (PatientIssueReturn patientIssueReturn : implantReturnAcceptanceConsignmentDao.implantReturnAcceptanceSearch(implantReturnAcceptanceRequest, screenId)) {
			ImplantReturnAcceptanceResponse implantReturnAcceptanceResponse = new ImplantReturnAcceptanceResponse();
			
			String doctorDetail = "";
			if(patientIssueReturn.getPatientIssueReturnDetail().get(0).getIssueDetailBatchMapper()
					.getIssueDetail().getIssue()!=null)
			{
				doctorDetail = patientIssueReturn.getPatientIssueReturnDetail().get(0).getIssueDetailBatchMapper()
						.getIssueDetail().getIssue().getDoctor().getFirstName()+ " | "+ patientIssueReturn.getPatientIssueReturnDetail().get(0).getIssueDetailBatchMapper()
						.getIssueDetail().getIssue().getDoctor().getMiddleName()+ " "+ patientIssueReturn.getPatientIssueReturnDetail().get(0).getIssueDetailBatchMapper()
						.getIssueDetail().getIssue().getDoctor().getLastName()+ " | " + patientIssueReturn.getPatientIssueReturnDetail().get(0).getIssueDetailBatchMapper()
						.getIssueDetail().getIssue().getSpecialityMaster().getSpecialityName();
			}
			

			Integer age = Util.getAgeByBirthDate(patientIssueReturn.getPatient().getDob().toLocalDateTime().toLocalDate());
			String patientDetail = patientIssueReturn.getPatient().getPrefix().getPrefixCode() + " "+ patientIssueReturn.getPatient().getFirstName() + " "
					+ patientIssueReturn.getPatient().getMiddleName() + " "	+ patientIssueReturn.getPatient().getLastName() + " | " + age + " | "
					+ patientIssueReturn.getPatient().getGender().getGenderCode();

			implantReturnAcceptanceResponse = new ImplantReturnAcceptanceResponse(patientIssueReturn.getId(),
					patientIssueReturn.getReturnNo(), patientIssueReturn.getAddedDate(),patientIssueReturn.getPatient().getUhidNumber(), patientDetail, doctorDetail,
					patientIssueReturn.getVisitType().getVisitTypeCode(), patientIssueReturn.getStore().getStoreName(),
					patientIssueReturn.getPatientIssueReturnDetail().get(0).getIssueDetailBatchMapper().getIssueDetail().getIssue().getImplantIndent().getImplantIndentNo()
					);

			implantReturnAcceptanceResponse.setStatus(patientIssueReturn.getStatus().getStatus());
			implantReturnAcceptanceResponse.setStatusId(patientIssueReturn.getStatus().getId());
			if (patientIssueReturn.getAdmission() != null)
			{
				implantReturnAcceptanceResponse.setWardName((patientIssueReturn.getWardMaster() != null) ? patientIssueReturn.getWardMaster().getWardName(): null);
				implantReturnAcceptanceResponse.setWardId((patientIssueReturn.getWardMaster() != null) ? patientIssueReturn.getWardMaster().getWardId(): null);
				implantReturnAcceptanceResponse.setBedNo((patientIssueReturn.getBedMaster() != null) ? patientIssueReturn.getBedMaster().getBedNumber(): null);
				implantReturnAcceptanceResponse.setVisitNo(patientIssueReturn.getAdmission().getAdmissionNumber());
			}
			implantReturnAcceptanceResponseList.add(implantReturnAcceptanceResponse);

		}
		return implantReturnAcceptanceResponseList;
	}

	
	@Override
	@Transactional
	public ImplantReturnAcceptanceResponse implantReturnAcceptanceDetails(Integer id, Integer unitId) {
		ImplantReturnAcceptanceResponse implantReturnAcceptanceResponse = new ImplantReturnAcceptanceResponse();
		PatientIssueReturn patientIssueReturn = consignmentReturnDao.patientIssueReturnById(id, unitId);
		/*List<DoctorSpecialityMapper> doctorSpeciality = GlobalDao.doctorSearchByIdValue(patientIssueReturn.getPatientIssueReturnDetail().get(0)
						.getIssueDetailBatchMapper().getIssueDetail().getIssue().getDoctor().getDoctorId());*/
		
		String doctorDetail = "";
		
		if(patientIssueReturn.getPatientIssueReturnDetail().get(0).getIssueDetailBatchMapper()
				.getIssueDetail().getIssue()!=null)
		{
			doctorDetail = patientIssueReturn.getPatientIssueReturnDetail().get(0).getIssueDetailBatchMapper()
					.getIssueDetail().getIssue().getDoctor().getFirstName()
					+ " "
					+ patientIssueReturn.getPatientIssueReturnDetail().get(0).getIssueDetailBatchMapper().getIssueDetail()
							.getIssue().getDoctor().getMiddleName()
					+ " "
					+ patientIssueReturn.getPatientIssueReturnDetail().get(0).getIssueDetailBatchMapper().getIssueDetail()
							.getIssue().getDoctor().getLastName()
					+ " | " + patientIssueReturn.getPatientIssueReturnDetail().get(0).getIssueDetailBatchMapper().getIssueDetail()
					.getIssue().getSpecialityMaster().getSpecialityName();
		}
		

		Integer age = Util.getAgeByBirthDate(patientIssueReturn.getPatient().getDob().toLocalDateTime().toLocalDate());
		String patientDetail = patientIssueReturn.getPatient().getPrefix().getPrefixCode() + " "
				+ patientIssueReturn.getPatient().getFirstName() + " " + patientIssueReturn.getPatient().getMiddleName()
				+ " " + patientIssueReturn.getPatient().getLastName() + " | " + age + " | "
				+ patientIssueReturn.getPatient().getGender().getGenderCode();
		
		implantReturnAcceptanceResponse = new ImplantReturnAcceptanceResponse(patientIssueReturn.getId(),
				patientIssueReturn.getPatientIssueReturnDetail().get(0).getIssueDetailBatchMapper().getIssueDetail().getIssue().getImplantIndent().getAdmission().getAdmissionNumber(),
				patientIssueReturn.getReturnNo(), patientIssueReturn.getAddedDate(),
				patientIssueReturn.getPatient().getUhidNumber(), patientDetail, doctorDetail,
				patientIssueReturn.getVisitType().getVisitTypeCode(), patientIssueReturn.getStore().getStoreName());
		implantReturnAcceptanceResponse.setStatus(patientIssueReturn.getStatus().getStatus());
		implantReturnAcceptanceResponse.setStatusId(patientIssueReturn.getStatus().getId());
		
		if (patientIssueReturn.getAdmission() != null) {
			implantReturnAcceptanceResponse.setWardName(
					(patientIssueReturn.getWardMaster() != null) ? patientIssueReturn.getWardMaster().getWardName()
							: null);
			implantReturnAcceptanceResponse.setWardId(
					(patientIssueReturn.getWardMaster() != null) ? patientIssueReturn.getWardMaster().getWardId()
							: null);
			implantReturnAcceptanceResponse.setBedNo(
					(patientIssueReturn.getBedMaster() != null) ? patientIssueReturn.getBedMaster().getBedNumber()
							: null);
		}

		List<ImplantReturnAcceptanceResponseItems> items = new ArrayList<ImplantReturnAcceptanceResponseItems>();
		for (PatientIssueReturnDetail patientIssueReturnDetail : consignmentReturnDao.patientIssueReturnDetailById(id, unitId)) {
			ImplantReturnAcceptanceResponseItems item = new ImplantReturnAcceptanceResponseItems(patientIssueReturnDetail.getId(),
					patientIssueReturnDetail.getIssueDetailBatchMapper().getBatch().getItem().getId(),
					patientIssueReturnDetail.getIssueDetailBatchMapper().getBatch().getItem().getName(),
					patientIssueReturnDetail.getIssueDetailBatchMapper().getBatch().getItem().getGeneric() != null ? patientIssueReturnDetail.getIssueDetailBatchMapper().getBatch().getItem().getGeneric().getName() : null,
					patientIssueReturnDetail.getIssueDetailBatchMapper().getBatch().getBatchNo(),
					patientIssueReturnDetail.getIssueDetailBatchMapper().getBatch().getExpiryDate(),
					patientIssueReturnDetail.getIssueDetailBatchMapper().getUomType() != null ? patientIssueReturnDetail.getIssueDetailBatchMapper().getUomType().getType() : null,
					patientIssueReturnDetail.getIssueDetailBatchMapper().getBatch().getUomUnit() != null ? patientIssueReturnDetail.getIssueDetailBatchMapper().getBatch().getUomUnit().getUnits() : null,
					patientIssueReturnDetail.getIssueDetailBatchMapper().getBatch().getSellingPrice(),
					patientIssueReturnDetail.getIssueDetailBatchMapper().getAcceptedQuantity(),
					patientIssueReturnDetail.getReturnReason() !=null ?patientIssueReturnDetail.getReturnReason().getReasonDesc():null,
					patientIssueReturnDetail.getIssueDetailBatchMapper().getId());
			
			if(patientIssueReturnDetail.getStatus() != null)
			{
			item.setStatusId(patientIssueReturnDetail.getStatus().getId());
			}
			items.add(item);
		}
		implantReturnAcceptanceResponse.setItems(items);
		return implantReturnAcceptanceResponse;
	}

	@Override
	public Object implantReturnAcceptanceConsignmentSave(ImplantReturnAcceptanceSave imlantReturnAcceptanceSave)
	{
		
		

		screen = GlobalDao.getScreen(screenId);
		PatientIssueReturn patientIssueReturn = consignmentReturnDao.patientIssueReturnById(imlantReturnAcceptanceSave.getId(), imlantReturnAcceptanceSave.getUnitId());
		
		BillingMasterDto dto  = new BillingMasterDto();
		dto.setOrganisationId(imlantReturnAcceptanceSave.getOrganizationId());
		dto.setUnitId(imlantReturnAcceptanceSave.getUnitId());
		dto.setEncounterId(patientIssueReturn.getAdmission().getAdmissionId());
		dto.setPatientId(patientIssueReturn.getPatient().getPatientId());
		dto.setVisitTypeId(patientIssueReturn.getVisitType().getVisitTypeId());
		dto.setCreatedBy(imlantReturnAcceptanceSave.getUserId());
		List<OrderDetailsMasterDto> orderDetailsList = new ArrayList<OrderDetailsMasterDto>();
		for (PatientIssueReturnDetail patientIssueReturnDetail : patientIssueReturn.getPatientIssueReturnDetail())
		{
			
			IssueDetailBatchMapper batchMapper = patientIssueReturnDetail.getIssueDetailBatchMapper();
			for (ImplantReturnAcceptanceSaveItem issueDetailBatchMapper : imlantReturnAcceptanceSave.getItems())
			{
				if (issueDetailBatchMapper.getAcceptQuantity() > 0)
				{
					if (issueDetailBatchMapper.getIssueDetailBatchMapper() != null && batchMapper.getId().equals(issueDetailBatchMapper.getIssueDetailBatchMapper()))
					{
						BatchMapperStoreStock(patientIssueReturn.getStore().getStoreId(), batchMapper.getBatch(),
								issueDetailBatchMapper.getAcceptQuantity(), batchMapper.getUomType(), imlantReturnAcceptanceSave, batchMapper);

						batchMapper.updateIssueDetailBatchMapper(issueDetailBatchMapper.getIssueDetailBatchMapper(),
								issueDetailBatchMapper.getAcceptQuantity(), issueDetailBatchMapper.getRejectedNote(),
								issueDetailBatchMapper.getIssueRejectedQuantity(),(issueDetailBatchMapper.getRejectReason() != null)
										? new RejectReason(issueDetailBatchMapper.getRejectReason())
										: null);
						if (issueDetailBatchMapper.getStatusId() != null)
						{
							patientIssueReturnDetail.setStatus(new Status(issueDetailBatchMapper.getStatusId()));
						}
						
						OrderDetailsMasterDto orderDtlsDto = new OrderDetailsMasterDto();
						orderDtlsDto.setSaleDetailId(batchMapper.getIssueDetail().getSaleDetail().getId().intValue());
						orderDtlsDto.setOrgId(dto.getOrganisationId());
						orderDtlsDto.setOrgUnitId(dto.getUnitId());
						orderDtlsDto.setVisitTypeId(dto.getVisitTypeId());
						orderDtlsDto.setOrderQty(issueDetailBatchMapper.getAcceptQuantity());
						orderDtlsDto.setBatchId(batchMapper.getBatch().getId().intValue());
						orderDtlsDto.setDrugId(batchMapper.getBatch().getItem().getId());
						orderDetailsList.add(orderDtlsDto);
						
							 
					}
				}
			}
		}
		patientIssueReturn.setStatus(new Status(com.param.common.constants.Status.Completed.getStatus()));
		patientIssueReturn.setUpdatedByUser(imlantReturnAcceptanceSave.getUserId());
		patientIssueReturn.setUpdatedDate(TimeZoneComponent.getDateByzone(patientIssueReturn.getUnitId()));
		implantReturnAcceptanceConsignmentDao.updateImplantReturnAcceptance(patientIssueReturn);
		saveRejectDetails(imlantReturnAcceptanceSave);
		
		
		 
		CommonResponse response = new CommonResponse();
		response.setId(patientIssueReturn.getId().longValue());
		response.setMessage("Implant Return Acceptance saved successfully");

		return response;
	}

	private void BatchMapperStoreStock(Integer storeId, Batch batch, Integer issueQuantity, UomType uomType, ImplantReturnAcceptanceSave imlantReturnAcceptanceSave , IssueDetailBatchMapper  bm) {

		boolean hasStock = false;

		for (StoreStock storeStock : batch.getStoreStockList()) {
			if (storeStock.getStore().getStoreId().equals(storeId)) {

				hasStock = true;
				Integer avaialableStoreStock = GlobalDao.getStoreStockByBatch(storeId, batch.getId(),0);//0 itemId

				Integer leaseQuantity = Util.GetLeaseQuantity(issueQuantity, batch.getBatchUomList(), uomType);

				Integer balanceQuantity = avaialableStoreStock + leaseQuantity;

				storeStock.setAvailableQty(balanceQuantity);

				Integer currentQuantity = batch.getCurrentQuantity() + leaseQuantity;
				batch.setCurrentQuantity(currentQuantity);
				StockTransaction stockTransaction=	new StockTransaction(balanceQuantity, screen, screen.getCode(), leaseQuantity,
						batch.getStoreStockList().get(0).getStore(), bm.getId());
//				sessionService.setEntityDefaults(stockTransaction);	
				stockTransaction.setCreateDefaults(imlantReturnAcceptanceSave.getOrganizationId(),imlantReturnAcceptanceSave.getUnitId(),
						imlantReturnAcceptanceSave.getUserId());
				batch.addStockTransaction(stockTransaction);
			}
		}
		if (!hasStock) {

			new StoreStock(issueQuantity, new Store(storeId), new Batch(batch.getId()));

			Integer avaialableStoreStock = GlobalDao.getStoreStockByBatch(storeId, batch.getId(),0);//0 itemId

			Integer leaseQuantity = Util.GetLeaseQuantity(issueQuantity, batch.getBatchUomList(), uomType);

			Integer currentQuantity = batch.getCurrentQuantity() + leaseQuantity;
			batch.setCurrentQuantity(currentQuantity);
			StockTransaction stockTransaction =new StockTransaction(avaialableStoreStock, screen, screen.getCode(),
					leaseQuantity, batch.getStoreStockList().get(0).getStore(), bm.getId());
//			sessionService.setEntityDefaults(stockTransaction);	
			stockTransaction.setCreateDefaults(imlantReturnAcceptanceSave.getOrganizationId(),imlantReturnAcceptanceSave.getUnitId(),
					imlantReturnAcceptanceSave.getUserId());
			batch.addStockTransaction(stockTransaction);
		}
	}

	private void saveRejectDetails(ImplantReturnAcceptanceSave imlantReturnAcceptanceSave) {
		Timestamp now = new Timestamp((new DateTime()).getMillis());
		boolean flag = false;
		//Unit unit = GlobalDao.getUnit(sessionService.getUnitId());
		String rejectBillNumber = GlobalService.getBillNoByGeneratedCode(0, sessionService.getUnitId(), rejectScreenId);
		Reject reject = new Reject(rejectBillNumber, new Issue(imlantReturnAcceptanceSave.getId()), new PatientIssueReturn(imlantReturnAcceptanceSave.getId()),
				new User(sessionService.getUserId()), now, new Status(com.param.common.constants.Status.Pending.getStatus()));
//		sessionService.setEntityDefaults(reject);
		reject.setCreateDefaults(imlantReturnAcceptanceSave.getOrganizationId(),imlantReturnAcceptanceSave.getUnitId(),
				imlantReturnAcceptanceSave.getUserId());
		for (ImplantReturnAcceptanceSaveItem issueDetailItem : imlantReturnAcceptanceSave.getItems())
		{
			if (issueDetailItem.getIssueRejectedQuantity() > 0) {
				flag = true;
				RejectDetail rejectDetail=	new RejectDetail(new IssueDetailBatchMapper(issueDetailItem.getIssueDetailBatchMapper()),
						issueDetailItem.getIssueRejectedQuantity(), new Status(com.param.common.constants.Status.Pending.getStatus()),
						(issueDetailItem.getReasonId() != null) ? new RejectReason(issueDetailItem.getReasonId()) : null,
								issueDetailItem.getRejectedNote(),new PatientIssueReturnDetail(issueDetailItem.getId()));
//				sessionService.setEntityDefaults(rejectDetail);	
				rejectDetail.setCreateDefaults(imlantReturnAcceptanceSave.getOrganizationId(),imlantReturnAcceptanceSave.getUnitId(),
						imlantReturnAcceptanceSave.getUserId());
				reject.addRejectDetail(rejectDetail);
			}
		}
		if (flag == true) {
			implantReturnAcceptanceConsignmentDao.saveRejectDetails(reject);
		}
	}

}
