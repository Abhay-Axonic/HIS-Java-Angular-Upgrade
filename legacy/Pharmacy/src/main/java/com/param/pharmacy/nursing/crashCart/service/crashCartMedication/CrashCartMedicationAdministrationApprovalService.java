package com.param.pharmacy.nursing.crashCart.service.crashCartMedication;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.adt.AdmissionDetail;
import com.param.entity.model.master.RejectReason;
import com.param.entity.model.master.Status;
import com.param.entity.model.pharmacy.CrashCartRequestDetail;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.utility.Util;
import com.param.pharmacy.nursing.crashCart.dao.ICrashCartConsumptionDao;
import com.param.pharmacy.nursing.crashCart.dao.crashCartMedication.ICrashCartMedicationAdministrationApprovalDao;
import com.param.pharmacy.nursing.crashCart.dto.CrashCartRequest;
import com.param.pharmacy.nursing.crashCart.dto.CrashCartResponse;
import com.param.pharmacy.nursing.crashCart.dto.ItemRequest;
import com.param.pharmacy.nursing.crashCart.dto.ItemResponse;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service
public class CrashCartMedicationAdministrationApprovalService
		implements ICrashCartMedicationAdministrationApprovalService {

	private Integer screenId = Screen.CrashCartRequest.getScreen();
	@Autowired
	private SessionService sessionService;
	@Autowired
	IGlobalDao GlobalDao;

	@Autowired
	ICrashCartConsumptionDao CrashCartConsumptionDao;

	@Autowired
	ICrashCartMedicationAdministrationApprovalDao CrashCartMedicationAdministrationApprovalDao;

	@Override
	@Transactional
	public List<CrashCartResponse> getCrashCartMedicationList(CrashCartRequest request) {

		List<CrashCartResponse> crashCartResponsesList = new ArrayList<CrashCartResponse>();
		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());

		for (com.param.entity.model.pharmacy.CrashCartRequest crashCartRequest : CrashCartMedicationAdministrationApprovalDao
				.crashCartMedicationSearch(request, screenId)) {

			Integer age = Util
					.getAgeByBirthDate(crashCartRequest.getPatient().getDob().toLocalDateTime().toLocalDate());
			String patientDetail = crashCartRequest.getPatient().getPrefix().getPrefixCode() + " "
					+ crashCartRequest.getPatient().getFirstName() + " " + crashCartRequest.getPatient().getMiddleName()
					+ " " + crashCartRequest.getPatient().getLastName() + " | " + age + " | "
					+ crashCartRequest.getPatient().getGender().getGenderCode();

			String doctorDetail = "";
			
			if( crashCartRequest.getDoctor()!=null && crashCartRequest.getSpecialityMaster()!=null)
			{
				doctorDetail = crashCartRequest.getDoctor().getFirstName() + " "
						+ crashCartRequest.getDoctor().getMiddleName() + " " + crashCartRequest.getDoctor().getLastName()
						+ " | " + crashCartRequest.getSpecialityMaster().getSpecialityName();
			}
				
			CrashCartResponse crashCartResponse = new CrashCartResponse(crashCartRequest.getId(),
					crashCartRequest.getCrashCartNo(), crashCartRequest.getAddedDate(),
					crashCartRequest.getCodeBlueDateTime(), crashCartRequest.getVisitType().getVisitTypeCode(),
					crashCartRequest.getStore().getStoreName(), crashCartRequest.getPatient().getUhidNumber(),
					patientDetail, doctorDetail,
					(crashCartRequest.getWard() != null) ? crashCartRequest.getWard().getWardName() : null,
					(crashCartRequest.getBed() != null) ? crashCartRequest.getBed().getBedNumber() : null,
					crashCartRequest.getAddedBy().getUserName(), crashCartRequest.getStatus().getId(),
					crashCartRequest.getStatus().getStatus());

			if (crashCartRequest.getAdmission() != null) {
				crashCartResponse.setVisitNo(crashCartRequest.getAdmission().getAdmissionNumber());
			}
			if (crashCartRequest.getEncounter() != null) {
				crashCartResponse.setVisitNo(crashCartRequest.getEncounter().getEncounterNumber());
			}

			crashCartResponsesList.add(crashCartResponse);
		}
		return crashCartResponsesList;
	}

	@Override
	@Transactional

	public Object crashCartMedicationDetails(Integer id,Integer unitId) {

		CrashCartResponse response = new CrashCartResponse();
		com.param.entity.model.pharmacy.CrashCartRequest crashCartRequest = CrashCartConsumptionDao
				.getCrashCartRequest(id,unitId);

		Integer age = Util.getAgeByBirthDate(crashCartRequest.getPatient().getDob().toLocalDateTime().toLocalDate());

		String patientDetail = crashCartRequest.getPatient().getPrefix().getPrefixCode() + " "
				+ crashCartRequest.getPatient().getFirstName() + " " + crashCartRequest.getPatient().getMiddleName()
				+ " " + crashCartRequest.getPatient().getLastName() + " | " + age + " | "
				+ crashCartRequest.getPatient().getGender().getGenderCode();

		String doctorDetail = "";
		if(crashCartRequest.getDoctor()!=null && crashCartRequest.getSpecialityMaster()!=null)
		{
			doctorDetail = crashCartRequest.getDoctor().getFirstName() + " "
					+ crashCartRequest.getDoctor().getMiddleName() + " " + crashCartRequest.getDoctor().getLastName()
					+ " | " + crashCartRequest.getSpecialityMaster().getSpecialityName();
		}
		

		if (crashCartRequest.getAdmission() != null) {
			Date doa = null;
			Date pdd = null;
			AdmissionDetail admissionDetail = GlobalDao
					.getPatientWardBedDetails(crashCartRequest.getAdmission().getAdmissionId());
			if (admissionDetail != null) {
				doa = admissionDetail.getDoa();
				pdd = admissionDetail.getPdd();
			}
			response = new CrashCartResponse(crashCartRequest.getId(), crashCartRequest.getCrashCartNo(),
					crashCartRequest.getAddedDate(), crashCartRequest.getVisitType().getVisitTypeCode(),
					crashCartRequest.getStore().getStoreName(), crashCartRequest.getPatient().getUhidNumber(),
					patientDetail, doctorDetail, crashCartRequest.getAdmission().getAdmissionNumber(),
					(crashCartRequest.getWard() != null) ? crashCartRequest.getWard().getWardName() : null,
					(crashCartRequest.getBed() != null) ? crashCartRequest.getBed().getBedNumber() : null, pdd, doa,
					crashCartRequest.getAdmission().getPatient().getDob(), crashCartRequest.getRemark());
		}
		if (crashCartRequest.getEncounter() != null) {
			response = new CrashCartResponse(crashCartRequest.getId(), crashCartRequest.getCrashCartNo(),
					crashCartRequest.getAddedDate(), crashCartRequest.getVisitType().getVisitTypeCode(),
					crashCartRequest.getStore().getStoreName(), crashCartRequest.getPatient().getUhidNumber(),
					patientDetail, doctorDetail, crashCartRequest.getEncounter().getEncounterNumber(),
					crashCartRequest.getEncounter().getPatient().getDob(), crashCartRequest.getRemark(),
					crashCartRequest.getEncounter().getPatient().getGender().getGenderId(),
					crashCartRequest.getEncounter().getPatient().getGender().getGenderCode(),
					crashCartRequest.getEncounter().getPatient().getImagePath());
		}

		for (CrashCartRequestDetail detail : CrashCartConsumptionDao.getCrashCartRequestDetail(id)) {
			response.addItems(new ItemResponse(detail.getId(), detail.getItem().getId(), detail.getItem().getCode(),
					detail.getItem().getName(), detail.getItem().getGeneric().getId(),
					detail.getItem().getGeneric().getName(), detail.getUomType().getType(),
					detail.getUomUnit().getUnits(), detail.getRequiredQty(), detail.getItem().getIsThreeUOMApplicable(), 
					GlobalDao.getUnitOfMeasurementListByItem(detail.getItem().getId(), crashCartRequest.getOrganizationId())));
		}

		return response;
	}

	@Override
	@Transactional
	public Object updateCrashCartMedication(CrashCartRequest request) {

		com.param.entity.model.pharmacy.CrashCartRequest crashCartRequest = CrashCartConsumptionDao
				.getCrashCartRequest(request.getId(),request.getUnitId());

		for (CrashCartRequestDetail detail : CrashCartConsumptionDao.getCrashCartRequestDetail(request.getId())) {

			for (ItemRequest item : request.getItems()) {
				detail.setApprovedQty(item.getApprovedQuantity());
				detail.setRejectQty(item.getRejectQuantity() != null ? item.getRejectQuantity() : null);
				detail.setRejectReason(item.getReasonId() != null ? new RejectReason(item.getReasonId()) : null);
				detail.setRejectNote(item.getNote() != null ? item.getNote() : null);
				detail.setUpdatedDate(TimeZoneComponent.getDateByzone(crashCartRequest.getUnitId()));
				detail.setUpdatedByUser(request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());
				detail.setStatus(new Status(item.getStatusId()));
				crashCartRequest.addCrashCartRequestDetail(detail);
			}

		}
		crashCartRequest.setStatus(new Status(request.getStatusId()));
		crashCartRequest.setUpdatedDate(TimeZoneComponent.getDateByzone(crashCartRequest.getUnitId()));
		crashCartRequest.setUpdatedByUser(request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());
		CrashCartMedicationAdministrationApprovalDao.updateCrashCasrtRequest(crashCartRequest);

		CommonResponse response = new CommonResponse();
		response.setId(crashCartRequest.getId().longValue());
		response.setMessage("Crash Cart Medication Updated successfully");
		return response;
	}

}
