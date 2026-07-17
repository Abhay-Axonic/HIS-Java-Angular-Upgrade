package com.param.pharmacy.op.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.User;
import com.param.entity.model.master.VisitType;
import com.param.entity.model.pharmacy.PrescriptionDetail;
import com.param.entity.model.pharmacy.PrescriptionFrequencyDetail;
import com.param.entity.model.pharmacy.Sale;
import com.param.entity.model.pharmacy.SaleDetail;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dto.global.CommonDropdown;
import com.param.global.utility.Util;
import com.param.pharmacy.ip.dao.pharmacology.IPharmacologyValidationDao;
import com.param.pharmacy.ip.dto.handoverWorklist.HandoverDetailItem;
import com.param.pharmacy.ip.dto.handoverWorklist.HandoverDetails;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyValidationSearch;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyValidationSearchResult;
import com.param.pharmacy.ip.dto.pharmacology.UpdateReceiver;
import com.param.pharmacy.op.dao.IHandoverworkListDaoOP;
import com.param.pharmacy.op.dao.salesWorkList.ISalesWorkListDao;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service("HandoverWorkListServiceOP")
@Transactional
public class HandoverWorkListServiceOP implements IHandoverWorkListServiceOP {
	
	@Autowired
	private IHandoverworkListDaoOP HandoverworkListDaoOP;
	
	@Autowired
	private IGlobalDao GlobalDao;
	
	@Autowired
	private ISalesWorkListDao SalesWorkListDao;

	@Autowired
	SessionService sessionService;
	
	@Autowired
	private IPharmacologyValidationDao pharmacologyValidationDao;
	
	private Integer screenId = Screen.HandoverWorklist.getScreen();
	
	@Override
	@Transactional
	public CommonDropdown getDropdownList()
	{
		CommonDropdown response = new CommonDropdown();

		for (VisitType visitType : GlobalDao.getVisitTypeList()) {
			response.addVisitType(new com.param.global.dto.global.VisitType(visitType.getVisitTypeId(),
					visitType.getVisitTypeName()));
		}
		
		for (ScreenStatus status : GlobalDao.getScreenStatus(screenId)) {
			response.addStatus(new com.param.global.dto.global.Status(status.getStatus().getId(), status.getStatus().getStatus()));
		}
		
		return response;
	}
	
	@Override
	@Transactional
	public List<PharmacologyValidationSearchResult> handoverWorkListSearch(PharmacologyValidationSearch pharmacologyValidationSearch)
	{
		List<PharmacologyValidationSearchResult>  handoverWorkList = new  ArrayList<PharmacologyValidationSearchResult>();
		pharmacologyValidationSearch.setToDate((new DateTime(pharmacologyValidationSearch.getToDate())).plusDays(1).toDate());
		for(Sale sale : HandoverworkListDaoOP.handoverworkListSearch(pharmacologyValidationSearch, screenId))
		{
			if(sale.getEncounter() != null) 
			{
				PharmacologyValidationSearchResult handoverObject = null;
				
				Integer age = Util.getAgeByBirthDate(sale.getEncounter().getPatient().getDob().toLocalDateTime().toLocalDate());
				String patientDetail = sale.getEncounter().getPatient().getPrefix().getPrefixCode() 
						+ " " 
						+ sale.getEncounter().getPatient().getFirstName() 
						+ " " + sale.getEncounter().getPatient().getMiddleName() 
						+ " " 
						+ sale.getEncounter().getPatient().getLastName() 
						+ " | " + age + " | " 
						+ sale.getEncounter().getPatient().getGender().getGenderCode();
				
				String doctorDetail = "";
				/*List<DoctorSpecialityMapper> doctorSpeciality = GlobalDao.doctorSearchByIdValue(sale.getDoctor().getDoctorId());
				if(doctorSpeciality != null && !doctorSpeciality.isEmpty())*/
				if(sale.getDoctor()!=null && sale.getSpecialityMaster()!=null)
				{
					doctorDetail = sale.getDoctor().getFirstName() + " " + sale.getDoctor().getMiddleName() + " " +
							sale.getDoctor().getLastName() + " | " + sale.getSpecialityMaster().getSpecialityName();
				}
				
				
				if(!sale.getSaleDetailsList().isEmpty())
				{
					for(SaleDetail saleDetail  : sale.getSaleDetailsList())
					{
						List<PrescriptionDetail> prescriptionDetail = SalesWorkListDao.prescriptionDetailsByIdValue(saleDetail.getPrescriptionDetail().getPrescription().getId());
						if(!prescriptionDetail.isEmpty())
						{
							handoverObject = new PharmacologyValidationSearchResult(
									sale.getId(),
									sale.getVisitType().getVisitTypeCode(), 
									sale.getVisitType().getCreatedDate(), 
									prescriptionDetail.getFirst().getPrescription().getPrescriptionDate(),
									sale.getPatient().getUhidNumber(), 
									patientDetail, 
									doctorDetail, 
									prescriptionDetail.getFirst().getPrescription().getPriority().getPriorityDesc(),
									sale.getStatus().getStatus(), 
									sale.getStatus().getId());
							
							handoverObject.setPaymentStatus("");
							handoverObject.setDispensedBy(sale.getHandoverBy() != null ? sale.getHandoverBy().getUserName() : "-");
						}
					}
					handoverWorkList.add(handoverObject);
				}
			}
			else if(sale.getTempPatient() != null) {
				PharmacologyValidationSearchResult handoverObject = null;
				
				String patientDetail = sale.getTempPatient().getPrefix().getPrefixCode() + " " + sale.getTempPatient().getPatientName() + " " +
						  " | " + sale.getTempPatient().getAge() + " | " + sale.getTempPatient().getGender().getGenderCode();
				
				if(!sale.getSaleDetailsList().isEmpty())
				{
					for(SaleDetail saleDetail  : sale.getSaleDetailsList())
					{
						List<PrescriptionDetail> prescriptionDetail = SalesWorkListDao.prescriptionDetailsByIdValue(saleDetail.getPrescriptionDetail().getPrescription().getId());
						if(!prescriptionDetail.isEmpty())
						{
							handoverObject = new PharmacologyValidationSearchResult(
									sale.getId(),
									sale.getVisitType().getVisitTypeCode(), 
									sale.getVisitType().getCreatedDate(), 
									prescriptionDetail.getFirst().getPrescription().getPrescriptionDate(),
									sale.getPatient().getUhidNumber(), 
									patientDetail, 
									sale.getTempDoctor().getDoctorName() + " " + sale.getTempDoctor().getDoctorAddress(), 
									prescriptionDetail.getFirst().getPrescription().getPriority().getPriorityDesc(),
									sale.getStatus().getStatus(), 
									sale.getStatus().getId());
							
							handoverObject.setPaymentStatus("");
							handoverObject.setDispensedBy(sale.getHandoverBy() != null ? sale.getHandoverBy().getUserName() : "-");
						}
					}
				}
			}
		}
		return handoverWorkList;
	}

	
	@Override
	@Transactional
	public HandoverDetails handoverWorkListDetails(Long id)
	{
		Sale sale = HandoverworkListDaoOP.handoverworkListDetails(id);
		HandoverDetails handoverDetails = null;
		
		if(sale.getEncounter() != null) {
			Integer age = Util.getAgeByBirthDate(sale.getPatient().getDob().toLocalDateTime().toLocalDate());
			String patientDetail = sale.getPatient().getPrefix().getPrefixCode() + " " + sale.getPatient().getFirstName() + " " + sale.getPatient().getMiddleName() + " " +
									sale.getPatient().getLastName() + " | " + age + " | " + sale.getPatient().getGender().getGenderCode();
			
			String doctorDetail = "";
			//List<DoctorSpecialityMapper> doctorSpeciality = GlobalDao.doctorSearchByIdValue(sale.getDoctor().getDoctorId());
			if(sale.getDoctor()!=null && sale.getSpecialityMaster()!=null)
			{
				doctorDetail = sale.getDoctor().getFirstName() + " " + sale.getDoctor().getMiddleName() + " " +
						sale.getDoctor().getLastName() + " | " + sale.getSpecialityMaster().getSpecialityName();
			}
			
			List<HandoverDetailItem> items = new ArrayList<HandoverDetailItem>();
			for(SaleDetail saleDetail : sale.getSaleDetailsList())
			{
				for(PrescriptionFrequencyDetail prescriptionFrequencyDetail : pharmacologyValidationDao.getPrescriptionFrequencyDetails(saleDetail.getPrescriptionDetail().getId()))
				{	
					HandoverDetailItem item = new HandoverDetailItem(saleDetail.getId(), 
							saleDetail.getPrescriptionDetail().getItem().getCode(),
							saleDetail.getPrescriptionDetail().getItem().getName(), 
							saleDetail.getPrescriptionDetail().getItem().getId(),
							saleDetail.getPrescriptionDetail().getItem().getManufacturer().getName(), 
							saleDetail.getPrescriptionDetail().getItem().getManufacturer().getId(),
							saleDetail.getPrescriptionDetail().getItem().getIsOtc(), 
							saleDetail.getUomUnit().getUnit().getUnitDescription(), 
							saleDetail.getUomUnit().getId(),
							saleDetail.getPrescriptionDetail().getOrderQty(), 
							saleDetail.getTotalAmount(), 
							saleDetail.getBatch().getBatchNo(), 
							saleDetail.getBatch().getExpiryDate(),
							saleDetail.getQuantity(), 
							saleDetail.getRate(), 
							saleDetail.getNetAmount(), 
							saleDetail.getDiscountAmount(),
							prescriptionFrequencyDetail.getStartDate(), prescriptionFrequencyDetail.getStopDate(), prescriptionFrequencyDetail.getFrequency().getDescription(),
							prescriptionFrequencyDetail.getDose());
					item.setStatus(saleDetail.getStatus().getStatus());
					item.setStatusId(saleDetail.getStatus().getId());
					items.add(item);
				}
			}
			
			handoverDetails = new HandoverDetails(sale.getId(), 
					sale.getPatient().getPatientId(), 
					patientDetail, 
					sale.getPatient().getDob(),
					sale.getDoctor().getDoctorId(), 
					doctorDetail, 
					sale.getPatient().getUhidNumber(), 
					/*doa*/null, 
					/*pdd*/null, 
					items);
			handoverDetails.setStatus(sale.getStatus().getStatus());
			handoverDetails.setStatusId(sale.getStatus().getId());
			handoverDetails.setReceiverName(sale.getReceivedBy());
		}
		else if(sale.getTempPatient() != null) {
			
			String patientDetail = sale.getTempPatient().getPrefix().getPrefixCode() + " " + sale.getTempPatient().getPatientName() + " " +
					  " | " + sale.getTempPatient().getAge() + " | " + sale.getTempPatient().getGender().getGenderCode();
			
			List<HandoverDetailItem> items = new ArrayList<HandoverDetailItem>();
			for(SaleDetail saleDetail : sale.getSaleDetailsList())
			{
				for(PrescriptionFrequencyDetail prescriptionFrequencyDetail : pharmacologyValidationDao.getPrescriptionFrequencyDetails(saleDetail.getPrescriptionDetail().getId()))
				{	
					HandoverDetailItem item = new HandoverDetailItem(saleDetail.getId(), 
							saleDetail.getPrescriptionDetail().getItem().getCode(),
							saleDetail.getPrescriptionDetail().getItem().getName(), 
							saleDetail.getPrescriptionDetail().getItem().getId(),
							saleDetail.getPrescriptionDetail().getItem().getManufacturer().getName(), 
							saleDetail.getPrescriptionDetail().getItem().getManufacturer().getId(),
							saleDetail.getPrescriptionDetail().getItem().getIsOtc(), 
							saleDetail.getUomUnit().getUnit().getUnitDescription(), 
							saleDetail.getUomUnit().getId(),
							saleDetail.getPrescriptionDetail().getOrderQty(), 
							saleDetail.getTotalAmount(), 
							saleDetail.getBatch().getBatchNo(), 
							saleDetail.getBatch().getExpiryDate(),
							saleDetail.getQuantity(), 
							saleDetail.getRate(), 
							saleDetail.getNetAmount(), 
							saleDetail.getDiscountAmount(),
							prescriptionFrequencyDetail.getStartDate(), prescriptionFrequencyDetail.getStopDate(), prescriptionFrequencyDetail.getFrequency().getDescription(),
							prescriptionFrequencyDetail.getDose());
					item.setStatus(saleDetail.getStatus().getStatus());
					item.setStatusId(saleDetail.getStatus().getId());
					items.add(item);
				}
			}
			
			handoverDetails = new HandoverDetails(sale.getId(), 
					sale.getTempPatient().getPatientId(), 
					patientDetail, /*sale.getTempPatient().getDob()*/null,
					sale.getDoctor().getDoctorId(), 
					sale.getTempDoctor().getDoctorName(), 
					sale.getPatient().getUhidNumber(), 
					/*doa*/null, 
					/*pdd*/null, 
					items);
			handoverDetails.setStatus(sale.getStatus().getStatus());
			handoverDetails.setStatusId(sale.getStatus().getId());
			handoverDetails.setReceiverName(sale.getReceivedBy());
		}
		
		return handoverDetails;
	}

	@Override
	@Transactional
	public Object handoverWorkListSave(UpdateReceiver updateReceiver)
	{
		Sale sale = HandoverworkListDaoOP.handoverworkListDetails(updateReceiver.getId());
		Timestamp now = new Timestamp((new DateTime()).getMillis());
		sale.setReceivedBy(updateReceiver.getReceiverName());
		sale.setHandover(true);
		sale.setReceivedOn(now);
		sale.setHandoverBy(new User(sessionService.getUserId()));
		sale.setUpdatedDate(TimeZoneComponent.getDateByzone(sale.getUnitId()));
		sale.setUpdatedByUser(updateReceiver.getUserId()!=null ? updateReceiver.getUserId() : sessionService.getUserId());
		sale.setStatus(new Status(updateReceiver.getStatusId()));
		HandoverworkListDaoOP.handoverworkListUpdate(sale);
		
		CommonResponse response = new CommonResponse();
		response.setId((sale.getId()));
		response.setMessage("handover worklist updated successfully");

		return response;
	}
}