package com.param.pharmacy.narcotics.deliveryWorklist.service;

import java.math.BigInteger;
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
import com.param.entity.model.adt.BedMaster;
import com.param.entity.model.adt.WardMasters;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.User;
import com.param.entity.model.master.VisitType;
import com.param.entity.model.pharmacy.Issue;
import com.param.entity.model.pharmacy.PrescriptionDetail;
import com.param.entity.model.pharmacy.Sale;
import com.param.entity.model.pharmacy.SaleDetail;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dto.global.CommonDropdown;
import com.param.global.utility.Util;
import com.param.pharmacy.inPatient.dto.patientConsumableIssue.Carrier;
import com.param.pharmacy.ip.dao.handoverWorklist.IHandoverworkListDao;
import com.param.pharmacy.ip.dao.pharmacology.IPharmacologyValidationDao;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyItem;
import com.param.pharmacy.narcotics.deliveryWorklist.dao.INarcoticsDeliveryWorklistDao;
import com.param.pharmacy.narcotics.issueWorklist.dto.NarcoticsDetails;
import com.param.pharmacy.narcotics.issueWorklist.dto.NarcoticsRequest;
import com.param.pharmacy.narcotics.issueWorklist.dto.NarcoticsResponse;
import com.param.pharmacy.narcotics.issueWorklist.dto.UpdateCarrier;
import com.param.pharmacy.op.dao.salesWorkList.ISalesWorkListDao;
import com.param.procurement.quotation.dao.physicalInspection.IConsignmentStockConversionDao;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service("narcoticsDeliveryWorklistService")
@Transactional
public class NarcoticsDeliveryWorklistService implements INarcoticsDeliveryWorklistService
{
	@Autowired
	private SessionService sessionService;

	@Autowired
	private INarcoticsDeliveryWorklistDao narcoticsDeliveryWorklistDao;
	
	@Autowired
	private IPharmacologyValidationDao pharmacologyValidationDao;
	
	@Autowired
	private IHandoverworkListDao handoverworkListDao;
	
	@Autowired
	private ISalesWorkListDao SalesWorkListDao;
	
	@Autowired
	private IGlobalDao GlobalDao;
	
	@Autowired
	IConsignmentStockConversionDao consignmentStockConversionDao;
	
	private Integer screenId = Screen.DeliveryWorklist.getScreen();

	@Override
	@Transactional
	public CommonDropdown getDropdownList(Integer unitId)
	{
		CommonDropdown response = new CommonDropdown();

		for (VisitType visitType : GlobalDao.getVisitTypeList()) {
			response.addVisitType(new com.param.global.dto.global.VisitType(visitType.getVisitTypeId(),
					visitType.getVisitTypeName()));
		}
		
		for (WardMasters wardMaster : GlobalDao.getWardList(unitId)) {
			response.addWard(new com.param.global.dto.global.Ward(wardMaster.getWardId(),
					wardMaster.getWardName()));
		}
		
		for (ScreenStatus status : GlobalDao.getScreenStatus(screenId)) {
			response.addStatus(new com.param.global.dto.global.Status(status.getStatus().getId(), status.getStatus().getStatus()));
		}
		
		return response;
	}
	
	@Override
	@Transactional
	public List<NarcoticsResponse> narcoticsDeliveryWorklistSearch(NarcoticsRequest narcoticsRequest)
	{
		List<NarcoticsResponse> narcoticsResponseList = new ArrayList<NarcoticsResponse>();
		narcoticsRequest.setToDate((new DateTime(narcoticsRequest.getToDate())).plusDays(1).toDate());
		for(Sale sale : narcoticsDeliveryWorklistDao.narcoticsDeliveryWorklistSearch(narcoticsRequest, screenId))
		{
				//Issue issue = narcoticsDeliveryWorklistDao.getIssueBySaleId(sale.getId());
				Issue issue = sale.getIssueList().get(0);

				//List<DoctorSpecialityMapper> doctorSpeciality = GlobalDao.doctorSearchByIdValue(sale.getDoctor().getDoctorId());
				String doctorDetail = "";
				if(issue.getPrescription().getDoctor()!=null && issue.getPrescription().getSpecialityMaster()!=null)
				{
					doctorDetail = issue.getPrescription().getDoctor().getFirstName() + " " + issue.getPrescription().getDoctor().getMiddleName() + " " +
							issue.getPrescription().getDoctor().getLastName() + " | " + issue.getPrescription().getSpecialityMaster().getSpecialityName();
				}
				if(sale.getAdmission() != null)
				{
					String wardName = "";
					String bedNumber = "";
					AdmissionDetail admissionDetail = GlobalDao.getPatientWardBedDetails(sale.getAdmission().getAdmissionId());
					if(admissionDetail != null)
					{
						WardMasters wardMaster = admissionDetail.getWardMaster();
						if(wardMaster != null)
						{
							wardName = wardMaster.getWardName();
						}
						BedMaster bedMaster = admissionDetail.getBedMaster();
						if(bedMaster != null)
						{
							bedNumber = bedMaster.getBedNumber();
						}
					}
					Integer age = Util.getAgeByBirthDate(sale.getPatient().getDob().toLocalDateTime().toLocalDate());
					String patientDetail = sale.getPatient().getPrefix().getPrefixCode() + " " + sale.getPatient().getFirstName() + " " + sale.getPatient().getMiddleName() + " " +
							sale.getPatient().getLastName() + " | " + age + " | " + sale.getPatient().getGender().getGenderCode();
					NarcoticsResponse narcoticsResponse = new NarcoticsResponse(sale.getId(),
							sale.getVisitType().getVisitTypeCode(), sale.getAdmission().getCreatedDate(), sale.getPatient().getUhidNumber(), patientDetail, doctorDetail,
							sale.getStatus().getStatus(), sale.getStatus().getId(), issue.getId(), issue.getAddedDate(), issue.getIssueNo());
					narcoticsResponse.setWardName(!wardName.equals("") ? wardName : null);
					narcoticsResponse.setBedNo(!bedNumber.equals("") ? bedNumber : null);
					narcoticsResponse.setMedicationType(sale.getPrescription().getMedicationType());
					if(sale.getCarrier() != null)
					{
					narcoticsResponse.setCarrierName(sale.getCarrier().getUserName());
					}	
					if(!sale.getSaleDetailsList().isEmpty())
					{
						for(SaleDetail saleDetail  : sale.getSaleDetailsList())
						{
							List<PrescriptionDetail> prescriptionDetail = SalesWorkListDao.prescriptionDetailsByIdValue(saleDetail.getPrescriptionDetail().getPrescription().getId());
							if(!prescriptionDetail.isEmpty())
							{
								narcoticsResponse.setPrescriptionDate(prescriptionDetail.get(0).getPrescription().getPrescriptionDate());
								narcoticsResponse.setPriority(prescriptionDetail.get(0).getPrescription().getPriority().getPriorityDesc());
							}
						}
					}
					narcoticsResponseList.add(narcoticsResponse);
				}
				else if(sale.getEncounter() != null)
				{
					Integer age = Util.getAgeByBirthDate(sale.getPatient().getDob().toLocalDateTime().toLocalDate());
					String patientDetail = sale.getPatient().getPrefix().getPrefixCode() + " " + sale.getPatient().getFirstName() + " " + sale.getPatient().getMiddleName() + " " +
							sale.getPatient().getLastName() + " | " + age + " | " + sale.getPatient().getGender().getGenderCode();
					NarcoticsResponse narcoticsResponse = new NarcoticsResponse(sale.getId(),
							sale.getVisitType().getVisitTypeCode(), sale.getEncounter().getCreatedDate(), sale.getPatient().getUhidNumber(), patientDetail, doctorDetail, 
							sale.getStatus().getStatus(), sale.getStatus().getId(), issue.getId(), issue.getAddedDate(), issue.getIssueNo());
					if(sale.getCarrier() != null)
					{
					narcoticsResponse.setCarrierName(sale.getCarrier().getUserName());
					}
					narcoticsResponse.setMedicationType(sale.getPrescription().getMedicationType());

					if(!sale.getSaleDetailsList().isEmpty())
					{
						for(SaleDetail saleDetail  : sale.getSaleDetailsList())
						{
							if(saleDetail.getPrescriptionDetail()!=null) {
							List<PrescriptionDetail> prescriptionDetail = SalesWorkListDao.prescriptionDetailsByIdValue(saleDetail.getPrescriptionDetail().getPrescription().getId());
							if(prescriptionDetail!=null && !prescriptionDetail.isEmpty())
							{
								narcoticsResponse.setPrescriptionId(prescriptionDetail.get(0).getPrescription().getId().intValue());
								narcoticsResponse.setPrescriptionDate(prescriptionDetail.get(0).getPrescription().getPrescriptionDate());
								narcoticsResponse.setPriority(prescriptionDetail.get(0).getPrescription().getPriority().getPriorityDesc());
							}
							}
						}
					}
					narcoticsResponseList.add(narcoticsResponse);
				}
		 }
		 return narcoticsResponseList;
	}
	
	@Override
	@Transactional
	public NarcoticsDetails narcoticsDeliveryWorklistDetails(Long id,Integer unitId, Integer storeId,Integer orgId) throws Exception
	{
	
		NarcoticsDetails narcoticsDetails = new NarcoticsDetails();
		List<PharmacologyItem> items = new ArrayList<PharmacologyItem>();
		Sale sale = handoverworkListDao.handoverworkListDetails(id,unitId);
		//List<DoctorSpecialityMapper> doctorSpeciality = GlobalDao.doctorSearchByIdValue(sale.getDoctor().getDoctorId());
		String doctorDetail = "";
		if(sale.getPrescription().getDoctor()!=null && sale.getPrescription().getSpecialityMaster()!=null)
		{
			doctorDetail = sale.getPrescription().getDoctor().getFirstName() + " " + sale.getPrescription().getDoctor().getMiddleName() + " " +
					sale.getPrescription().getDoctor().getLastName() + " | " + sale.getPrescription().getSpecialityMaster().getSpecialityName();
		}
		
		Integer carrierId = null;
		if(sale.getCarrier() != null)
		{
			carrierId = sale.getCarrier().getUserId();
		}
		if(sale.getAdmission() != null)
		{
			String wardName = "";
			String bedNumber = "";
			Date doa = null;
			Date pdd = null;
			AdmissionDetail admissionDetail = GlobalDao.getPatientWardBedDetails(sale.getAdmission().getAdmissionId());
			if(admissionDetail != null)
			{
				doa = admissionDetail.getDoa();
				pdd = admissionDetail.getPdd();
				WardMasters wardMaster = admissionDetail.getWardMaster();
				if(wardMaster != null)
				{
					wardName = wardMaster.getWardName();
				}
				BedMaster bedMaster = admissionDetail.getBedMaster();
				if(bedMaster != null)
				{
					bedNumber = bedMaster.getBedNumber();
				}
			}
			Integer age = Util.getAgeByBirthDate(sale.getPatient().getDob().toLocalDateTime().toLocalDate());
			String patientDetail = sale.getPatient().getPrefix().getPrefixCode() + " " + sale.getPatient().getFirstName() + " " + sale.getPatient().getMiddleName() + " " +
					sale.getPatient().getLastName() + " | " + age + " | " + sale.getPatient().getGender().getGenderCode();
		
			narcoticsDetails = new NarcoticsDetails(sale.getId(), sale.getPatient().getUhidNumber(),
					patientDetail, sale.getPatient().getDob(), doctorDetail);
			narcoticsDetails.setDoa(doa != null ? doa : null);
			narcoticsDetails.setPdd(pdd != null ? pdd : null);
			narcoticsDetails.setWardName(!wardName.equals("") ? wardName : null);
			narcoticsDetails.setBedNo(!bedNumber.equals("") ? bedNumber : null);
			narcoticsDetails.setPatientId(sale.getPatient().getPatientId());
			narcoticsDetails.setStatusId(sale.getStatus().getId());
			narcoticsDetails.setStatus(sale.getStatus().getStatus());
			narcoticsDetails.setGenderId(sale.getPatient().getGender().getGenderId());
			narcoticsDetails.setImagePath(sale.getPatient().getImagePath());
		}
		else if(sale.getEncounter() != null)
		{
			Integer age = Util.getAgeByBirthDate(sale.getPatient().getDob().toLocalDateTime().toLocalDate());
			String patientDetail = sale.getPatient().getPrefix().getPrefixCode() + " " + sale.getPatient().getFirstName() + " " + sale.getPatient().getMiddleName() + " " +
					sale.getPatient().getLastName() + " | " + age + " | " + sale.getPatient().getGender().getGenderCode();
		
			narcoticsDetails = new NarcoticsDetails(sale.getId(), sale.getPatient().getUhidNumber(),
					patientDetail, sale.getPatient().getDob(), doctorDetail);
			narcoticsDetails.setPatientId(sale.getPatient().getPatientId());
		}
		for(SaleDetail saleDetail : handoverworkListDao.handoverworkListDetailList(id,unitId))
		{
			for(PrescriptionDetail prescriptionFrequencyDetail : pharmacologyValidationDao.getPrescriptionFrequencyDetailsForWorklist(saleDetail.getPrescriptionDetail().getPrescription().getId()))
			{
				BigInteger availableQty=null;
//				Long storeStock = GlobalDao.getStoreStockByItemPharmacy(storeId,saleDetail.getPrescriptionDetail().getItem().getId(),unitId);
				
				availableQty = (consignmentStockConversionDao.getAvailableWithoutLotStock(saleDetail.getBatch().getBatchNo(),false, storeId, saleDetail.getItem().getId(),unitId));					
				Long storeStock=availableQty.longValue();
				
				PharmacologyItem pharmacologyItem = new PharmacologyItem(saleDetail.getId(), saleDetail.getPrescriptionDetail().getItem().getId(), saleDetail.getPrescriptionDetail().getItem().getCode(), 
						saleDetail.getPrescriptionDetail().getItem().getName(), saleDetail.getPrescriptionDetail().getItem().getGeneric().getId(),
						saleDetail.getPrescriptionDetail().getItem().getGeneric().getName(),
						saleDetail.getPrescriptionDetail().getOrderQty(), 
						saleDetail.getPrescriptionDetail().getItem().getManufacturer().getId(), saleDetail.getPrescriptionDetail().getItem().getManufacturer().getName(),
						storeStock, "Rack-001",  saleDetail.getBatch().getId(), saleDetail.getBatch().getBatchNo(), saleDetail.getBatch().getExpiryDate(),
						saleDetail.getUomType().getType(), saleDetail.getUomUnit().getUnits(), saleDetail.getPrescriptionDetail().getItem().getSalesTax().getId(),
						saleDetail.getPrescriptionDetail().getItem().getSalesTax().getName(), saleDetail.getPrescriptionDetail().getItem().getSalesTax().getTaxPercentage());
//				pharmacologyItem.setStatusId(saleDetail.getPrescriptionDetail().getStatus().getId());
//				pharmacologyItem.setStatus(saleDetail.getPrescriptionDetail().getStatus().getStatus());
//				Long issueQuantity = handoverworkListDao.getIssuedQuantity(saleDetail.getPrescriptionDetail().getId());
				pharmacologyItem.setIssuedQuantity(saleDetail.getQuantity().longValue());
				pharmacologyItem.setPriorityId(saleDetail.getPrescriptionDetail().getPriorityId());
				items.add(pharmacologyItem);
			}
		}
		narcoticsDetails.setItems(items);
		List<Carrier> carrierList = new ArrayList<Carrier>();
		for(User user : GlobalDao.getUser(orgId,unitId))
		{
			if(carrierId == user.getUserId())
			{
				Carrier carrier = new Carrier(user.getUserId(), user.getUserName(), true);
				carrierList.add(carrier);
			}
			else
			{
				Carrier carrier = new Carrier(user.getUserId(), user.getUserName(), false);
				carrierList.add(carrier);
			}
			
		}
		narcoticsDetails.setCarriers(carrierList);
		return narcoticsDetails;
	}
	
	
	@Override
	@Transactional
	public CommonResponse narcoticsDeliveryWorklistSave(UpdateCarrier updateCarrier)
	{
		Sale sale = handoverworkListDao.handoverworkListDetails(updateCarrier.getId(),updateCarrier.getUnitId());
		sale.setCarrier(new User(updateCarrier.getCarrierId()));
		sale.setUpdatedByUser(updateCarrier.getUserId()!=null ? updateCarrier.getUserId() : sessionService.getUserId());
		sale.setUpdatedDate(TimeZoneComponent.getDateByzone(sale.getUnitId()));
		handoverworkListDao.handoverworkListUpdate(sale);
		
		CommonResponse response = new CommonResponse();
		response.setId(((long)sale.getId()));
		response.setMessage("delivery worklist updated successfully");

		return response;
	}
}