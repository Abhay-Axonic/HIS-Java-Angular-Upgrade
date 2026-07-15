package com.param.procurement.consignment.service.implantIndent;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.adt.Admission;
import com.param.entity.model.adt.Priority;
import com.param.entity.model.doctor.Doctor;
import com.param.entity.model.inventory.IndentType;
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.UnitOfMeasurement;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.UomUnit;
import com.param.entity.model.master.User;
import com.param.entity.model.master.VisitType;
import com.param.entity.model.opd.Encounter;
import com.param.entity.model.patient.PatientRegistration;
import com.param.entity.model.pharmacy.Issue;
import com.param.entity.model.pharmacy.IssueDetail;
import com.param.entity.model.procurement.ImplantIndent;
import com.param.entity.model.procurement.ImplantIndentDetail;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dto.items.UomResponse;
import com.param.global.service.global.IGlobalService;
import com.param.global.utility.Util;
import com.param.procurement.consignment.dao.implantIndent.IImplantIndentDao;
import com.param.procurement.consignment.dto.implantIndent.ImplantIndentDropdown;
import com.param.procurement.consignment.dto.implantIndent.ImplantIndentItemResponse;
import com.param.procurement.consignment.dto.implantIndent.ImplantIndentRequest;
import com.param.procurement.consignment.dto.implantIndent.ImplantIndentRequestItems;
import com.param.procurement.consignment.dto.implantIndent.ImplantIndentResponse;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service
public class ImplantIndentService implements IImplantIndentService {

	private Integer screenId = Screen.ImplantIndent.getScreen();

	private Integer issueScreenId = Screen.PendingIssues.getScreen();

	@Autowired
	IImplantIndentDao ImplantIndentDao;

	@Autowired
	IGlobalService GlobalService;

	@Autowired
	IGlobalDao GlobalDao;

	@Autowired
	private SessionService sessionService;

	@Override
	@Transactional
	public ImplantIndentDropdown getDropdowns(Integer unitId, Integer orgId, Integer userId) {
		ImplantIndentDropdown response = new ImplantIndentDropdown();

		for (Store store : GlobalDao.getUserStoreList(unitId,orgId,userId)) {
			response.addStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
		}

		for (Store store : GlobalDao.getStoreList(unitId)) {
			response.addToStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
		}

		for (ScreenStatus screenStatus : GlobalDao.getScreenStatus(screenId)) {
			response.addStatus(new com.param.global.dto.global.Status(screenStatus.getStatus().getId(),
					screenStatus.getStatus().getStatus()));
		}

		for (IndentType indentType : GlobalDao.getIndentTypeList(orgId,unitId)) {
			response.addIndentType(
					new com.param.global.dto.global.IndentType(indentType.getId(), indentType.getType()));
		}

		for (Priority priority : GlobalDao.getPriorityList()) {
			response.addPriority(
					new com.param.global.dto.global.Priority(priority.getPriorityId(), priority.getPriorityDesc()));
		}

		for (VisitType visitType : GlobalDao.getVisitTypeList()) {
			response.addVisitType(new com.param.global.dto.global.VisitType(visitType.getVisitTypeId(),
					visitType.getVisitTypeCode()));
		}
		return response;
	}

	@Override
	@Transactional
	public Object saveImplantIndent(ImplantIndentRequest request) {

		/*Integer storeId = request.getStoreId();

		Store storeNew = GlobalDao.getStore(storeId);

		Unit unit = GlobalDao.getUnit(sessionService.getUnitId());*/

		String implantIndentNumber = GlobalService.getBillNoByGeneratedCode(request.getStoreId(), request.getUnitId(), screenId);

		ImplantIndent implantIndent = new ImplantIndent();
		if (request.getAdmissionId() != null)
		{

			implantIndent = new ImplantIndent(new Status(request.getStatusId()), implantIndentNumber,
					new IndentType(request.getIndentTypeId()), false, request.getIsConsignment() != null ? request.getIsConsignment() : false,
					request.getIsImplant(), request.getIsKit(), new PatientRegistration(request.getPatientId()),
					new Doctor(request.getDoctorId()), new Admission(request.getAdmissionId()),
					new Priority(request.getPriorityId()), new VisitType(request.getVisitType()), request.getRemark(),
					new Status(request.getStatusId()), new Store(request.getStoreId()),
					new Store(request.getFromStoreId()!=null ?request.getFromStoreId() : request.getCookieStoreId()));
			
//			sessionService.setEntityDefaults(implantIndent);
			implantIndent.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
					request.getUserId());
			
		} else if (request.getEncounterId() != null) {

			implantIndent = new ImplantIndent(new Status(request.getStatusId()), implantIndentNumber,
					new IndentType(request.getIndentTypeId()), false, request.getIsConsignment() != null ? request.getIsConsignment() : false,
					request.getIsImplant(), request.getIsKit(), new PatientRegistration(request.getPatientId()),
					new Doctor(request.getDoctorId()), new Encounter(request.getEncounterId()),
					new Priority(request.getPriorityId()), new VisitType(request.getVisitType()), request.getRemark(),
					new Status(request.getStatusId()), new Store(request.getStoreId()),
					new Store(request.getFromStoreId()!=null ? request.getFromStoreId() : request.getCookieStoreId()));
//			sessionService.setEntityDefaults(implantIndent);
			implantIndent.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
					request.getUserId());
			
		}

		for (ImplantIndentRequestItems item : request.getItems()) 
		{
			ImplantIndentDetail implantIndentDetail=new ImplantIndentDetail((item.getIndentQuantity()),
					new Item(item.getItemId()), item.getIndentQuantity(), new Status(item.getStatusId()),
					item.getUomTypeId()!=null ? new UomType(item.getUomTypeId()):null,
					item.getUomUnitId()!=null ? new UomUnit(item.getUomUnitId()):null);
//			sessionService.setEntityDefaults(implantIndentDetail);	
			implantIndent.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
					request.getUserId());
			implantIndent.addImplantIndentDetail(implantIndentDetail);

		}

		ImplantIndentDao.saveImplantIndent(implantIndent);

		CommonResponse response = new CommonResponse();
		response.setId(implantIndent.getId().longValue());
		response.setMessage("Implant Indent saved successfully");

		return response;
	}

	@Override
	@Transactional
	public List<ImplantIndentResponse> searchImplantIndent(ImplantIndentRequest request) {

		List<ImplantIndentResponse> responses = new ArrayList<ImplantIndentResponse>();
		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());

		for (ImplantIndent implantIndent : ImplantIndentDao.searchImplantIndent(request, screenId)) {
			Integer age = Util.getAgeByBirthDate(implantIndent.getPatient().getDob().toLocalDateTime().toLocalDate());
			String patientDetail = implantIndent.getPatient().getPrefix().getPrefixCode() + " "
					+ implantIndent.getPatient().getFirstName() + " " + implantIndent.getPatient().getMiddleName() + " "
					+ implantIndent.getPatient().getLastName() + " | " + age + " | "
					+ implantIndent.getPatient().getGender().getGenderCode();
			
			//List<DoctorSpecialityMapper> doctorSpeciality = GlobalDao.doctorSearchByIdValue(implantIndent.getDoctor().getDoctorId());
			String doctorDetail = "";
			if(implantIndent.getDoctor()!=null)
			{
				doctorDetail = implantIndent.getDoctor().getFirstName() + " | "
						+ implantIndent.getDoctor().getMiddleName() + " " + implantIndent.getDoctor().getLastName(); 
						//+ " | "+ implantIndent.getSpecialityMaster().getSpecialityName();
			}
			

			if (implantIndent.getAdmission() != null) {
				ImplantIndentResponse response = new ImplantIndentResponse(implantIndent.getId(),
						implantIndent.getImplantIndentNo(), implantIndent.getAddedDate(),
						implantIndent.getVisitType().getVisitTypeCode(),
						implantIndent.getAdmission().getAdmissionNumber(), implantIndent.getIndentType().getType(),
						implantIndent.getIndentType().getId(), implantIndent.getPriority().getPriorityCode(),
						implantIndent.getPriority().getPriorityId(), implantIndent.getAddedBy().getUserName(),
						implantIndent.getPatient().getUhidNumber(), patientDetail, doctorDetail,
						implantIndent.getStore().getStoreId(), implantIndent.getStore().getStoreName(),
						implantIndent.getStatus().getStatus(), implantIndent.getStatus().getId(),
						implantIndent.getApprovalStatus().getStatus(), implantIndent.getStatus().getId());

				responses.add(response);
			} else if (implantIndent.getEncounter() != null) {
				ImplantIndentResponse response = new ImplantIndentResponse(implantIndent.getId(),
						implantIndent.getImplantIndentNo(), implantIndent.getAddedDate(),
						implantIndent.getVisitType().getVisitTypeCode(),
						implantIndent.getAdmission().getAdmissionNumber(), implantIndent.getIndentType().getType(),
						implantIndent.getIndentType().getId(), implantIndent.getPriority().getPriorityCode(),
						implantIndent.getPriority().getPriorityId(), implantIndent.getAddedBy().getUserName(),
						implantIndent.getPatient().getUhidNumber(), patientDetail, doctorDetail,
						implantIndent.getStore().getStoreId(), implantIndent.getStore().getStoreName(),
						implantIndent.getStatus().getStatus(), implantIndent.getStatus().getId(),
						implantIndent.getApprovalStatus().getStatus(), implantIndent.getStatus().getId());

				responses.add(response);
			}

		}
		return responses;
	}

	@Override
	@Transactional
	public ImplantIndentResponse implantIndentDetails(Integer id,Integer storeId, Integer unitId) {

		ImplantIndent implantIndent = ImplantIndentDao.implantIndentById(id, unitId);

		ImplantIndentResponse response = new ImplantIndentResponse();

		if (implantIndent.getAdmission() != null) {
			response = new ImplantIndentResponse(implantIndent.getId(), implantIndent.getIndentType().getType(),
					implantIndent.getIndentType().getId(), implantIndent.getImplantIndentNo(),
					implantIndent.getAddedDate(), implantIndent.getVisitType().getVisitTypeName(),
					implantIndent.getVisitType().getVisitTypeId(), 
					implantIndent.getAdmission().getAdmissionNumber(), implantIndent.getPatient().getUhidNumber(),
					implantIndent.getPatient().getPatientId(),
					implantIndent.getPatient().getFirstName() + " " + implantIndent.getPatient().getMiddleName() + " "
							+ implantIndent.getPatient().getLastName(),
					implantIndent.getDoctor().getDoctorId(), implantIndent.getStore().getStoreId(),
					implantIndent.getStore().getStoreName(), implantIndent.getStatus().getStatus(),
					implantIndent.getStatus().getId(), implantIndent.getStatus().getStatus(),
					implantIndent.getStatus().getId(), implantIndent.getPriority().getPriorityId(),
					implantIndent.getPriority().getPriorityDesc(), implantIndent.getIsImplant(),
					implantIndent.getIsConsignment(), implantIndent.getIsKit(), implantIndent.getRemark());
			response.setAdmissionId(implantIndent.getAdmission().getAdmissionId());

		} else if (implantIndent.getEncounter() != null) {

			
			response = new ImplantIndentResponse(implantIndent.getId(), implantIndent.getIndentType().getType(),
					implantIndent.getIndentType().getId(), implantIndent.getImplantIndentNo(),
					implantIndent.getAddedDate(), implantIndent.getVisitType().getVisitTypeName(),
					implantIndent.getVisitType().getVisitTypeId(), implantIndent.getVisitType().getVisitTypeCode(),
					implantIndent.getAdmission().getAdmissionNumber(), implantIndent.getPatient().getUhidNumber(),
					implantIndent.getPatient().getPatientId(),
					implantIndent.getPatient().getFirstName() + " " + implantIndent.getPatient().getMiddleName() + " "
							+ implantIndent.getPatient().getLastName(),
					implantIndent.getDoctor().getDoctorId(), implantIndent.getStore().getStoreId(),
					implantIndent.getStore().getStoreName(), implantIndent.getStatus().getStatus(),
					implantIndent.getStatus().getId(), implantIndent.getStatus().getStatus(),
					implantIndent.getStatus().getId(), implantIndent.getPriority().getPriorityId(),
					implantIndent.getPriority().getPriorityDesc(), implantIndent.getIsImplant(),
					implantIndent.getIsConsignment(), implantIndent.getIsKit(), implantIndent.getRemark());
		}
			for (ImplantIndentDetail implantIndentDetail : ImplantIndentDao.implantIndentDetailById(id)) {
				
				Long stockAvailable = GlobalDao.getStoreStockByItemPharmacy(implantIndent.getStore().getStoreId() ,implantIndentDetail.getItem().getId(),unitId);
			response.addItems(new ImplantIndentItemResponse(implantIndentDetail.getItem().getId(),
					implantIndentDetail.getUomType()!=null ? implantIndentDetail.getUomType().getId():null,
					implantIndentDetail.getUomUnit()!=null ? implantIndentDetail.getUomUnit().getId():null,
					implantIndentDetail.getIndentQuantity(), implantIndentDetail.getStatus().getId(),
					implantIndentDetail.getId(), 
					implantIndentDetail.getItem()!=null ?implantIndentDetail.getItem().getCode():null,
					implantIndentDetail.getItem()!=null ?implantIndentDetail.getItem().getName():null,
					implantIndentDetail.getItem().getGeneric()!=null ?implantIndentDetail.getItem().getGeneric().getName():null,
					implantIndentDetail.getUomType()!=null ? implantIndentDetail.getUomType().getType():null,
					implantIndentDetail.getUomUnit()!=null ? implantIndentDetail.getUomUnit().getUnits():null,
					implantIndentDetail.getPendingQuantity(),
					implantIndentDetail.getItem().getProductCategory() != null ? implantIndentDetail.getItem().getProductCategory().getAssetCategory().getId() :
						implantIndentDetail.getItem().getGeneric().getProductCategory().getAssetCategory().getId(),
					PopulateUom(ImplantIndentDao.getAllByItemId(implantIndentDetail.getItem().getId()))!=null ?PopulateUom(ImplantIndentDao.getAllByItemId(implantIndentDetail.getItem().getId())):null,
							stockAvailable));
		}
		return response;
	}

	private List<UomResponse> PopulateUom(List<UnitOfMeasurement> uomMapper) {
		List<UomResponse> response = new ArrayList<UomResponse>();

		for (UnitOfMeasurement uom : uomMapper) 
		{
			response.add(new UomResponse(uom.getId()!=null ?uom.getId():null, uom.getUomType()!=null ?uom.getUomType().getType():null,
					uom.getUomType()!=null ? uom.getUomType().getId():null,
					uom.getUomUnit()!=null ? uom.getUomUnit().getId():null,
					uom.getUomUnit()!=null ?uom.getUomUnit().getUnits():null, uom.getConversions()!=null ?uom.getConversions():null,
							uom.getIpUom()!=null? uom.getIpUom():null,
					uom.getOpUom()!=null ?uom.getOpUom() :null, uom.getStoreUom()!=null? uom.getStoreUom():null));
		}
		return response;
	}

	@Override
	@Transactional
	public Object updateImplantIndent(ImplantIndentRequest request) {
		ImplantIndent implantIndent = ImplantIndentDao.implantIndentById(request.getId(), request.getUnitId());

		implantIndent.updateImplantIndent(request.getIsConsignment(), request.getIsImplant(), request.getIsKit(),
				request.getRemark());

		for (ImplantIndentDetail detail : implantIndent.getImplantIndentDetails()) {
			Integer id = null;

			for (ImplantIndentRequestItems item : request.getItems()) {
				if (item.getId() != null && item.getId().equals(detail.getId())) {
					id = item.getId();
					detail.updateIndentDetail(item.getIndentQuantity(), new Item(item.getItemId()));
					break;
				}
			}

			if (id == null) {
				detail.setIsDeleted(true);
			}

			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(implantIndent.getUnitId()));
			detail.setUpdatedByUser(request.getUserId());
		}

		for (ImplantIndentRequestItems item : request.getItems()) {
			if (item.getId() == null) {
				ImplantIndentDetail implantIndentDetail=new ImplantIndentDetail((item.getIndentQuantity()),
						new Item(item.getItemId()), item.getIndentQuantity(), new Status(item.getStatusId()),
						item.getUomTypeId()!=null ? new UomType(item.getUomTypeId()):null,
								item.getUomUnitId()!=null ? new UomUnit(item.getUomUnitId()):null);
//				sessionService.setEntityDefaults(implantIndentDetail);	
				implantIndentDetail.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
						request.getUserId());
				implantIndent.addImplantIndentDetail(implantIndentDetail);
				//new UomType(item.getUomTypeId()), new UomUnit(item.getUomUnitId())

			}
		}

		implantIndent.setUpdatedDate(TimeZoneComponent.getDateByzone(implantIndent.getUnitId()));
		implantIndent.setUpdatedByUser(request.getUserId());

		ImplantIndentDao.updateImplantIndent(implantIndent);

		CommonResponse response = new CommonResponse();
		response.setMessage("Implant Indent updated successfully");

		return response;
	}

	@Override
	@Transactional
	public Object cancelImplantIndent(ImplantIndentRequest request) {
		ImplantIndent implantIndent = ImplantIndentDao.implantIndentById(request.getId(), request.getUnitId());

		for (ImplantIndentDetail detail : implantIndent.getImplantIndentDetails())
		{
			detail.setStatus(new Status(request.getStatusId()));
			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(implantIndent.getUnitId()));
			detail.setUpdatedByUser(request.getUserId());
		}

		implantIndent.setCancelReason(new CancelReason(request.getCancelReasonId()));
		implantIndent.setCancelNote(request.getCancelNote());

		implantIndent.setStatus(new Status(request.getStatusId()));

		implantIndent.setUpdatedDate(TimeZoneComponent.getDateByzone(implantIndent.getUnitId()));
		implantIndent.setUpdatedByUser(request.getUserId());

		ImplantIndentDao.updateImplantIndent(implantIndent);

		CommonResponse response = new CommonResponse();
		response.setMessage("Implant Indent cancelled successfully");

		return response;
	}

	@Override
	@Transactional
	public Object approveImplantIndent(ImplantIndentRequest request) {

		for (Integer id : request.getImpIndId()) {

			ImplantIndent implantIndent = ImplantIndentDao.implantIndentById(id, request.getUnitId());

			/*Integer storeId = implantIndent.getStore().getStoreId();

			Store storeNew = GlobalDao.getStore(storeId);
			
			//System.out.println("storeNew:-"+storeNew);

			Unit unit = GlobalDao.getUnit(sessionService.getUnitId());
			
			//System.out.println("unitId:-"+sessionService.getUnitId());
*/			
			String issueNumber = GlobalService.getBillNoByGeneratedCode(implantIndent.getStore().getStoreId(), request.getUnitId(), issueScreenId);

			Issue issue = new Issue(issueNumber, new VisitType(implantIndent.getVisitType().getVisitTypeId()),
					new Doctor(implantIndent.getDoctor().getDoctorId()),
					new Store(implantIndent.getStore().getStoreId()),
					new ImplantIndent(implantIndent.getId()),
					new Status(com.param.common.constants.Status.Pending.getStatus()), implantIndent.getIsConsignment(),implantIndent.getIsImplant());
//			sessionService.setEntityDefaults(issue);	
			issue.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
					request.getUserId());
			if (implantIndent.getAdmission() != null) {
				issue.setAdmission(new Admission(implantIndent.getAdmission().getAdmissionId()));
			} else if (implantIndent.getEncounter() != null) {
				issue.setEncounter(new Encounter(implantIndent.getEncounter().getEncounterId()));
			}

			for (ImplantIndentDetail detail : implantIndent.getImplantIndentDetails()) {
				detail.setStatus(new Status(request.getApprovalStatusId()));

				detail.setUpdatedDate(TimeZoneComponent.getDateByzone(implantIndent.getUnitId()));
				detail.setUpdatedByUser(request.getUserId());

				IssueDetail issueDetail = new IssueDetail(new ImplantIndentDetail(detail.getId()),
						new Status(com.param.common.constants.Status.Pending.getStatus()));
//				sessionService.setEntityDefaults(issueDetail);
				issueDetail.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
						request.getUserId());
				issue.addIssueDetail(issueDetail);
			}

			implantIndent.setStatus(new Status(request.getStatusId()));
			implantIndent.setApprovalStatus(new Status(request.getApprovalStatusId()));
			implantIndent.setApprovedDate(TimeZoneComponent.getDateByzone(implantIndent.getUnitId()));
			implantIndent.setApprovedBy(new User(request.getUserId()));
		//	issue.setPatientIndent(new PatientIndent(request.getPatientId()));
			ImplantIndentDao.updateImplantIndent(implantIndent);
			Issue issueResponse = ImplantIndentDao.pendingIssuesSave(issue);
		}

		CommonResponse response = new CommonResponse();
		response.setMessage("Implant Indent approved successfully");

		return response;
	}

}
