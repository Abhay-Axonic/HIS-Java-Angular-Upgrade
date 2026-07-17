package com.param.pharmacy.ip.service.handoverWorklist;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
import com.param.entity.model.pharmacy.Sale;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dto.global.CommonDropdown;
import com.param.global.dto.global.DoctorSpecialityResp;
import com.param.global.utility.Util;
import com.param.pharmacy.inPatient.dto.patientConsumableIssue.Carrier;
import com.param.pharmacy.ip.dao.handoverWorklist.IHandoverworkListDao;
import com.param.pharmacy.ip.dao.pharmacology.IPharmacologyValidationDao;
import com.param.pharmacy.ip.dto.handoverWorklist.HandoverDetailItem;
import com.param.pharmacy.ip.dto.handoverWorklist.HandoverDetails;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyValidationSearch;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyValidationSearchResult;
import com.param.pharmacy.ip.dto.pharmacology.PrescriptionFrequencyDetailResp;
import com.param.pharmacy.ip.dto.pharmacology.UpdateReceiver;
import com.param.pharmacy.ip.dto.pharmacology.prescriptionDetailResp;
import com.param.pharmacy.op.dao.salesWorkList.ISalesWorkListDao;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service("handoverWorkListService")
@Transactional
public class HandoverWorkListService implements IHandoverWorkListService {
	
	@Autowired
	private IHandoverworkListDao HandoverworkListDao;
	
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
	public List<PharmacologyValidationSearchResult> handoverWorkListSearchBP(PharmacologyValidationSearch pharmacologyValidationSearch)
	{
		List<PharmacologyValidationSearchResult>  handoverWorkListResp = new  ArrayList<PharmacologyValidationSearchResult>();
		pharmacologyValidationSearch.setToDate((new DateTime(pharmacologyValidationSearch.getToDate())).plusDays(1).toDate());
		
		List<PharmacologyValidationSearchResult> handoverWorkList = HandoverworkListDao.handoverworkListSearchBP(pharmacologyValidationSearch, screenId);
		if(!handoverWorkList.isEmpty())
		{
			List<Long> saleIds = handoverWorkList.stream().map(PharmacologyValidationSearchResult::getId)
					.collect(Collectors.toList());
			
			List<prescriptionDetailResp> saleDetailsBySale = null;
			if(!saleIds.isEmpty())
				saleDetailsBySale = SalesWorkListDao.getSaleDetailBYSaleIdBP(saleIds);
			
			List<prescriptionDetailResp> prescrDetails = SalesWorkListDao.prescriptionDetailsByIdValueBP(saleDetailsBySale.stream()
					.map(x -> x.getSaleDetailId()).collect(Collectors.toList()));
			
			for(PharmacologyValidationSearchResult result : handoverWorkList)
			{
				
				Integer age = Util.getAgeByBirthDate(result.getPatientDob().toLocalDateTime().toLocalDate());
				result.setPatientDetail(result.getPatientDetail() + " | " + age);
				
				List<prescriptionDetailResp> saleDetailsSpecific = saleDetailsBySale.stream()
						.filter(x -> x.getSaleId().equals(result.getId())).collect(Collectors.toList());
				PharmacologyValidationSearchResult handoverObject = new PharmacologyValidationSearchResult();
				if(saleDetailsSpecific != null && !saleDetailsSpecific.isEmpty()) 
				{
					for(prescriptionDetailResp resp : saleDetailsSpecific) 
					{
						List<prescriptionDetailResp> prescrDetailSpecific = prescrDetails.stream()
								.filter(x -> x.getSaleDetailId().equals(resp.getSaleDetailId())).collect(Collectors.toList());
						if(prescrDetailSpecific != null && !prescrDetailSpecific.isEmpty())
						{
							handoverObject = new PharmacologyValidationSearchResult();
							handoverObject.setId(result.getId());
							handoverObject.setVisitType(result.getVisitType());
							handoverObject.setVisitDate(result.getVisitDate());
							handoverObject.setImagePath(result.getImagePath());
							handoverObject.setGenderCode(result.getGenderCode());
							handoverObject.setGenderId(result.getGenderId());
							handoverObject.setPatientName(result.getPatientName());
							handoverObject.setAgeString(result.getAgeString());
							handoverObject.setPrescriptionDate(prescrDetailSpecific.getFirst().getPrescriptionDate());
							handoverObject.setPrescriptionId(prescrDetailSpecific.getFirst().getPrescriptionId());
							handoverObject.setUhid(result.getUhid());
							handoverObject.setPatientDetail(result.getPatientDetail());
							handoverObject.setDoctorDetail(result.getDoctorDetail());
							handoverObject.setPriority(prescrDetailSpecific.getFirst().getDesc());
							handoverObject.setStatus(result.getStatus());
							handoverObject.setStatusId(result.getStatusId());
							handoverObject.setPaymentStatus("");
							handoverObject.setWardName(result.getWardName());
							handoverObject.setBedNo(result.getBedNo());
							handoverObject.setDispensedBy(result.getDispensedBy());
							handoverObject.setMedicationType(result.getMedicationType());
							handoverObject.setPatientId(result.getPatientId());
						}
					}
					handoverWorkListResp.add(handoverObject);
				}
			}
		}
		
		return handoverWorkListResp;
	}
	
	@Override
	@Transactional
	public HandoverDetails handoverWorkListDetailsBP(Long id, Integer unitId)
	{
		{
			HandoverDetails handoverDetails = HandoverworkListDao.handoverworkListDetailsBP(id,unitId);
			
			if(handoverDetails.getDoctorDetail() == null)
			{
				List<PharmacologyValidationSearchResult> handoverWorkList = HandoverworkListDao.getDoctorDetail(id,unitId);
				if(handoverWorkList.size() >0)
				{
					handoverDetails.setDoctorDetail(handoverWorkList.getFirst().getDoctorDetail());
					
				}
				
			}
			if(handoverDetails.getAdmissionId() != null){
				List<DoctorSpecialityResp> docSpeciality = new ArrayList<DoctorSpecialityResp>();
				
				Integer age = Util.getAgeByBirthDate(handoverDetails.getPatientDob().toLocalDateTime().toLocalDate());
				handoverDetails.setPatientDetail(handoverDetails.getPatientDetail() + " | " + age);
			}
			
			List<HandoverDetailItem> items = SalesWorkListDao.getSaleDetailBYSaleIdBP(handoverDetails.getId());
			List<Long> prescriptionDetailIds = items.stream().map(x -> x.getPrescriptionDetailId()).collect(Collectors.toList());
			List<PrescriptionFrequencyDetailResp> prescriptionFrequencyDetailRespAll = pharmacologyValidationDao.getPrescriptionFrequencyDetailsBP(prescriptionDetailIds);
			for(HandoverDetailItem item : items)
			{
				List<PrescriptionFrequencyDetailResp> prescriptionFrequencySpecific = prescriptionFrequencyDetailRespAll.stream()
						.filter(x -> x.getPrescriptionDetaild().equals(item.getPrescriptionDetailId())).collect(Collectors.toList());
				for(PrescriptionFrequencyDetailResp prescriptionFrequency : prescriptionFrequencySpecific)
				{	
					
					item.setDose(prescriptionFrequency.getDose());
					item.setStartDate(prescriptionFrequency.getStartDate());
					item.setStopDate(prescriptionFrequency.getStopDate());
					item.setFrequency(prescriptionFrequency.getFreqDescription());
				}
			}
			handoverDetails.setItems(items);
			
			List<Carrier> carrierList = new ArrayList<Carrier>();
			for (User user : GlobalDao.getPickerUsers(unitId)) {
				Carrier carrier = new Carrier(user.getUserId(), user.getUserName());
				carrierList.add(carrier);
			}
			handoverDetails.setCarrier(carrierList);
			return handoverDetails;
		}
		
	}
	
	
	@Override
	@Transactional
	public Object handoverWorkListSave(UpdateReceiver updateReceiver)
	{
		Sale sale = HandoverworkListDao.handoverworkListDetails(updateReceiver.getId(),updateReceiver.getUnitId());
		Timestamp now = new Timestamp((new DateTime()).getMillis());
		sale.setReceivedBy(updateReceiver.getReceiverName());
		sale.setHandover(true);
		sale.setReceivedOn(now);
		sale.setHandoverBy(new User(updateReceiver.getUserId()!=null ? updateReceiver.getUserId() :sessionService.getUserId()));
		sale.setUpdatedDate(TimeZoneComponent.getDateByzone(sale.getUnitId()));
		sale.setUpdatedByUser(updateReceiver.getUserId()!=null ? updateReceiver.getUserId() :sessionService.getUserId());
		sale.setStatus(new Status(updateReceiver.getStatusId()));
		sale.setCarrier(new User(updateReceiver.getCarrierId()));
		HandoverworkListDao.handoverworkListUpdate(sale);
		
		CommonResponse response = new CommonResponse();
		response.setId((sale.getId()));
		response.setMessage("handover worklist updated successfully");

		return response;
	}
}