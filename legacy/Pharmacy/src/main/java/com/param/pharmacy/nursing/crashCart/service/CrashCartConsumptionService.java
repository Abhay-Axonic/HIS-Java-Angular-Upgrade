package com.param.pharmacy.nursing.crashCart.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.adt.Admission;
import com.param.entity.model.adt.AdmissionDetail;
import com.param.entity.model.adt.BedMaster;
import com.param.entity.model.adt.WardMasters;
import com.param.entity.model.doctor.Doctor;
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.UomUnit;
import com.param.entity.model.master.VisitType;
import com.param.entity.model.opd.Encounter;
import com.param.entity.model.patient.PatientRegistration;
import com.param.entity.model.pharmacy.CrashCartRequestDetail;
import com.param.global.common.ICommonConstants;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.service.global.IGlobalService;
import com.param.global.service.items.CustomException;
import com.param.global.utility.Util;
import com.param.pharmacy.nursing.crashCart.dao.ICrashCartConsumptionDao;
import com.param.pharmacy.nursing.crashCart.dto.CrashCartRequest;
import com.param.pharmacy.nursing.crashCart.dto.CrashCartResponse;
import com.param.pharmacy.nursing.crashCart.dto.ItemRequest;
import com.param.pharmacy.nursing.crashCart.dto.ItemResponse;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service
public class CrashCartConsumptionService implements ICommonConstants,ICrashCartConsumptionService {

	private Integer screenId = Screen.CrashCartRequest.getScreen();

	@Autowired
	IGlobalDao GlobalDao;

	@Autowired
	IGlobalService GlobalService;

	@Autowired
	ICrashCartConsumptionDao CrashCartConsumptionDao;

	@Autowired
	private SessionService sessionService;

	@Override
	@Transactional
	public List<CrashCartResponse> getCrashCartConsumptionList(CrashCartRequest request) {

		List<CrashCartResponse> crashCartResponsesList = new ArrayList<CrashCartResponse>();
		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());

		for (com.param.entity.model.pharmacy.CrashCartRequest crashCartRequest : CrashCartConsumptionDao
				.crashCartSearch(request, screenId)) {

			Integer age = Util
					.getAgeByBirthDate(crashCartRequest.getPatient().getDob().toLocalDateTime().toLocalDate());
			String patientDetail = crashCartRequest.getPatient().getPrefix().getPrefixCode() + " "
					+ crashCartRequest.getPatient().getFirstName() + " " + crashCartRequest.getPatient().getMiddleName()
					+ " " + crashCartRequest.getPatient().getLastName() + " | " + age + " | "
					+ crashCartRequest.getPatient().getGender().getGenderCode();

			String doctorDetail = "";

			Integer doctorId=CrashCartConsumptionDao.getDoctorId(crashCartRequest.getId());
			if(doctorId!=null && doctorId > 0 )
			{
				if (crashCartRequest.getDoctor() != null ) {
					doctorDetail = crashCartRequest.getDoctor().getFirstName() + " "
							+ crashCartRequest.getDoctor().getMiddleName() + " "
							+ crashCartRequest.getDoctor().getLastName();
				}	
			}
			

			if (crashCartRequest.getSpecialityMaster() != null) {
				doctorDetail = doctorDetail + " | " + crashCartRequest.getSpecialityMaster().getSpecialityName();
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
	public Object saveCrashCart(CrashCartRequest request) throws Exception {
		CommonResponse response = new CommonResponse();
		try {
			String sDate = request.getCodeBlueDateTime();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

			Date date = formatter.parse(sDate);

			/*
			 * Integer storeId = sessionService.getStoreId();
			 * 
			 * Store storeNew = GlobalDao.getStore(storeId);
			 * 
			 * Unit unit = GlobalDao.getUnit(sessionService.getUnitId());
			 */

			String crashCartNumber = GlobalService.getBillNoByGeneratedCode(request.getStoreId(),
					request.getUnitId(), screenId);

			if (crashCartNumber == null) {
				throw new CustomException(" Please configure prefix number ");
			}

			Doctor doc = null;

			/*if (request.getVisitType() != 4 && request.getDoctorId() == null)
				throw new CustomException("Doctor id can not be null");
			else */if (request.getDoctorId() != null && request.getDoctorId() > 0)
				doc = new Doctor(request.getDoctorId());

			com.param.entity.model.pharmacy.CrashCartRequest crashCartRequest = new com.param.entity.model.pharmacy.CrashCartRequest(
					crashCartNumber, date, doc, new PatientRegistration(request.getPatientId()),
					new Store(request.getStoreId()), new VisitType(request.getVisitType()), request.getRemark(),
					new Status(request.getStatusId()));

			if (request.getAdmissionId() != null) {
				crashCartRequest.setAdmission(new Admission(request.getAdmissionId()));
				if (request.getWardId() != null) {
					crashCartRequest.setWard(new WardMasters(request.getWardId()));
				}
				if (request.getBedId() != null) {
					crashCartRequest.setBed(new BedMaster(request.getBedId()));
				}
			} else if (request.getEncounterId() != null) {
				crashCartRequest.setEncounter(new Encounter(request.getEncounterId()));
				if (request.getWardId() != null) {
					crashCartRequest.setWard(new WardMasters(request.getWardId()));
				}
				if (request.getBedId() != null) {
					crashCartRequest.setBed(new BedMaster(request.getBedId()));
				}
			}

			for (ItemRequest items : request.getItems()) {

				CrashCartRequestDetail crashCartRequestDetail = new CrashCartRequestDetail(new Item(items.getItemId()),
						new UomType(items.getUomTypeId()), new UomUnit(items.getUomUnitId()),
						items.getRequiredQuantity(), new Status(items.getStatusId()));
				crashCartRequest.addCrashCartRequestDetail(crashCartRequestDetail);
			//	sessionService.setEntityDefaults(crashCartRequestDetail);
				crashCartRequestDetail.setCreateDefaults(request.getOrganizationId()!=null ? request.getOrganizationId() : sessionService.getOrganizationId(),request.getUnitId() !=null ? request.getUnitId() :sessionService.getUnitId() ,request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());

			}
			//sessionService.setEntityDefaults(crashCartRequest);
			crashCartRequest.setCreateDefaults(request.getOrganizationId()!=null ? request.getOrganizationId() : sessionService.getOrganizationId(),request.getUnitId() !=null ? request.getUnitId() :sessionService.getUnitId() ,request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());

			CrashCartConsumptionDao.saveCrashCart(crashCartRequest);

			response.setId(crashCartRequest.getId().longValue());
			response.setMessage("Crash Cart Consumption saved successfully");

		} catch (Exception e) {
			
			throw e;
		}
		return response;
	}

	@Override
	@Transactional
	public Object crashCartDetails(Integer id,Integer unitId) {

		CrashCartResponse response = new CrashCartResponse();
		com.param.entity.model.pharmacy.CrashCartRequest crashCartRequest = CrashCartConsumptionDao
				.getCrashCartRequest(id,unitId);

		Integer age = Util.getAgeByBirthDate(crashCartRequest.getPatient().getDob().toLocalDateTime().toLocalDate());

		String patientDetail = crashCartRequest.getPatient().getPrefix().getPrefixCode() + " "
				+ crashCartRequest.getPatient().getFirstName() + " " + crashCartRequest.getPatient().getMiddleName()
				+ " " + crashCartRequest.getPatient().getLastName() + " | " + age + " | "
				+ crashCartRequest.getPatient().getGender().getGenderCode();

		String doctorDetail = "";
  
		if(!crashCartRequest.getVisitType().getVisitTypeId().equals(ER))
		{
			if (crashCartRequest.getDoctor() != null) {

				doctorDetail = crashCartRequest.getDoctor().getFirstName() + " "
						+ crashCartRequest.getDoctor().getMiddleName() + " " + crashCartRequest.getDoctor().getLastName();
			}
		}
		
		if (crashCartRequest.getSpecialityMaster() != null) {
			doctorDetail = doctorDetail + " | " + crashCartRequest.getSpecialityMaster().getSpecialityName();
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
					crashCartRequest.getAdmission().getPatient().getDob(), crashCartRequest.getRemark(),
					crashCartRequest.getStatus().getId(), crashCartRequest.getStatus().getStatus(),
					crashCartRequest.getAdmission().getPatient().getGender().getGenderId(), 
					crashCartRequest.getAdmission().getPatient().getGender().getGenderCode(),
					crashCartRequest.getAdmission().getPatient().getImagePath());
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
					detail.getUomUnit().getUnits(), detail.getRequiredQty(),  
					detail.getItem().getIsThreeUOMApplicable(), GlobalDao.getUnitOfMeasurementListByItem(detail.getItem().getId(), crashCartRequest.getOrganizationId())));
		}
		response.setPatientId(crashCartRequest.getPatient().getPatientId());
		return response;
	}

	@Override
	@Transactional
	public Object cancelCrashCart(CrashCartRequest request) {

		com.param.entity.model.pharmacy.CrashCartRequest crashCartRequest = CrashCartConsumptionDao
				.getCrashCartRequest(request.getId(),request.getUnitId());

		for (CrashCartRequestDetail detail : crashCartRequest.getCrashCartRequestDetails()) {
			detail.setStatus(new Status(request.getStatusId()));

			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(crashCartRequest.getUnitId()));
			detail.setUpdatedByUser(request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());
		}

		crashCartRequest.setCancelReason(new CancelReason(request.getCancelReasonId()));
		crashCartRequest.setCancelNote(request.getCancelNote());

		crashCartRequest.setStatus(new Status(request.getStatusId()));

		crashCartRequest.setUpdatedDate(TimeZoneComponent.getDateByzone(crashCartRequest.getUnitId()));
		crashCartRequest.setUpdatedByUser(request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());

		CrashCartConsumptionDao.updateCrashCartRequest(crashCartRequest);

		CommonResponse response = new CommonResponse();
		response.setMessage("Crash Cart cancelled successfully");

		return response;
	}

}
