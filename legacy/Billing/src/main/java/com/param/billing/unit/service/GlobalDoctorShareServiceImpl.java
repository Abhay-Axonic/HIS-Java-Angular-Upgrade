package com.param.billing.unit.service;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.billing.unit.dao.IGlobalDoctorShareGroupWiseDao;
import com.param.billing.unit.dao.IGlobalDoctorShareServiceWiseDao;
import com.param.billing.unit.dto.DoctorShareReqDto;
import com.param.billing.unit.dto.GlobalDocShareServiceRateDto;
import com.param.billing.unit.dto.IndividualDoctorShareReqDto;
import com.param.billing.unit.dto.TGlobalDocShareGroupWiseDto;
import com.param.billing.unit.dto.TGlobalDocShareServiceWiseDto;
import com.param.billing.unit.dto.TIndividualDocShareGroupWiseDto;
import com.param.billing.unit.dto.TIndividualDocShareServiceWiseDto;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.UnitServiceTariffMasterDto;
import com.param.global.opd.service.IEncounterMasterService;
import com.param.global.service.OrderMasterServiceImpl;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class GlobalDoctorShareServiceImpl implements IGlobalDoctorShareService, ICommonConstants {

	@Autowired
	IGlobalDoctorShareGroupWiseDao iGlobalDoctorShareGroupWiseDao;
	
	@Autowired
	IGlobalDoctorShareServiceWiseDao iGlobalDoctorShareServiceWiseDao;
	
	@Autowired
	IEncounterMasterService iEncounterMasterService;

	@Override
	public Response saveGlobalDoctorShare(DoctorShareReqDto reqDto)
			throws ApplicationException {
		try{
			
			List<TGlobalDocShareGroupWiseDto> docShareGroupWiseDtosList = new LinkedList<>();
			Set<Integer> deptIds = new HashSet<>();
			Set<Integer> billingBedCategoryIds = new HashSet<>();
			Set<Integer> visitTypeIds = new HashSet<>();
			Set<Integer> patientTypeIds = new HashSet<>();
			Set<Integer> paymentEntitlementIds = new HashSet<>();
			Set<Integer> tariffIds = new HashSet<>();
			Set<Date> effectiveDates = new HashSet<>();
			Set<Integer> serviceIds = new HashSet<>();
			List<TGlobalDocShareServiceWiseDto> docShareServiceWiseDtosList = new LinkedList<>();
			
			if(reqDto != null){
			//save Global Doctor Share Group Wise details
			if(reqDto.getListTGlobalDocShareGroupWiseDto() != null && reqDto.getListTGlobalDocShareGroupWiseDto().size() > 0){
				ListIterator<TGlobalDocShareGroupWiseDto> servicDtlsItr = reqDto.getListTGlobalDocShareGroupWiseDto().listIterator();
				TGlobalDocShareGroupWiseDto tGlobalDocShareGroupWiseDto = null;
				while(servicDtlsItr.hasNext()){
					tGlobalDocShareGroupWiseDto = servicDtlsItr.next();
					tGlobalDocShareGroupWiseDto.setOrgId(reqDto.getOrgId());
					tGlobalDocShareGroupWiseDto.setUnitId(reqDto.getUnitId());
						deptIds.add(tGlobalDocShareGroupWiseDto.getDepartmentId());
						billingBedCategoryIds.add(tGlobalDocShareGroupWiseDto.getBillingBedCategoryId());
						visitTypeIds.add(tGlobalDocShareGroupWiseDto.getVisitTypeId());
						patientTypeIds.add(tGlobalDocShareGroupWiseDto.getPatientTypeId());
						paymentEntitlementIds.add(tGlobalDocShareGroupWiseDto.getPaymentEntitlementId());
						tariffIds.add(tGlobalDocShareGroupWiseDto.getTariffId());
						effectiveDates.add(GlobalCommonDateUtils.getDate(GlobalCommonDateUtils.getStringDate(tGlobalDocShareGroupWiseDto.getEffectiveDate(),"yyyy-MM-dd"), "yyyy-MM-dd"));
					docShareGroupWiseDtosList.add(tGlobalDocShareGroupWiseDto);
				}
				iGlobalDoctorShareGroupWiseDao.checkUniqueGlobalGroup(deptIds,billingBedCategoryIds,visitTypeIds,patientTypeIds,paymentEntitlementIds,tariffIds,effectiveDates,'Y');
				Response res = iGlobalDoctorShareGroupWiseDao.saveGlobalDoctorShareGroupWiseDao(docShareGroupWiseDtosList);
			}
			
			//save Global Doctor Share Service Wise details
			if(reqDto.getListTGlobalDocShareServiceWiseDto() != null && reqDto.getListTGlobalDocShareServiceWiseDto().size() > 0){
				ListIterator<TGlobalDocShareServiceWiseDto> servicDtlsItr = reqDto.getListTGlobalDocShareServiceWiseDto().listIterator();
				TGlobalDocShareServiceWiseDto tGlobalDocShareServiceWiseDto = null;
				while(servicDtlsItr.hasNext()){
						tGlobalDocShareServiceWiseDto = servicDtlsItr.next();
						tGlobalDocShareServiceWiseDto.setOrgId(reqDto.getOrgId());
						tGlobalDocShareServiceWiseDto.setUnitId(reqDto.getUnitId());
							serviceIds.add(tGlobalDocShareServiceWiseDto.getServiceId());
							billingBedCategoryIds.add(tGlobalDocShareServiceWiseDto.getBillingBedCategoryId());
							visitTypeIds.add(tGlobalDocShareServiceWiseDto.getVisitTypeId());
							patientTypeIds.add(tGlobalDocShareServiceWiseDto.getPatientTypeId());
							paymentEntitlementIds.add(tGlobalDocShareServiceWiseDto.getPaymentEntitlementId());
							tariffIds.add(tGlobalDocShareServiceWiseDto.getTariffId());
							effectiveDates.add(GlobalCommonDateUtils.getDate(GlobalCommonDateUtils.getStringDate(tGlobalDocShareServiceWiseDto.getEffectiveDate(),"yyyy-MM-dd"), "yyyy-MM-dd"));
						docShareServiceWiseDtosList.add(tGlobalDocShareServiceWiseDto);
				}
				iGlobalDoctorShareServiceWiseDao.checkUniqueGlobalService(serviceIds,billingBedCategoryIds,visitTypeIds,patientTypeIds,paymentEntitlementIds,tariffIds,effectiveDates,'Y');
				Response res2 = iGlobalDoctorShareServiceWiseDao.saveGlobalDoctorShareServiceWiseDao(docShareServiceWiseDtosList);
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, DOCTOR_SHARE, null, null);
		 }
			
		  return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			
		}catch(ServiceException se){
			throw se;
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}
/*
	@Override
	public Response searchCompanyContract(ServiceForCompnayContarctReqDto reqDto) throws ServiceException {
		try {
			return iCompanyContractDao.searchCompanyContract(reqDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}*/


	@Override
	public Response getDoctorGradeMaster(Integer orgId, Integer orgUnitId) throws ApplicationException {
		try {
			
			return iGlobalDoctorShareGroupWiseDao.getDoctorGradeList(orgId, orgUnitId);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}



	@Override
	public Response<DoctorShareReqDto, Integer> getGlobalDoctorShareDetailsByDepartmentId(
			DoctorShareReqDto doctorShareReqDto) {
		  try {
				 Response res=iGlobalDoctorShareGroupWiseDao.getGroupWiseDoctorShareByDepartmentId(doctorShareReqDto);
				Response res1=iGlobalDoctorShareServiceWiseDao.getServiceWiseDoctorShareByDepartmentId(doctorShareReqDto);
				
				List<TGlobalDocShareGroupWiseDto> groupList=res.getListObject();
				List<TGlobalDocShareServiceWiseDto> serviceList=res1.getListObject();
				doctorShareReqDto.setListTGlobalDocShareGroupWiseDto(groupList);
				doctorShareReqDto.setListTGlobalDocShareServiceWiseDto(serviceList);
				return new Response<>(SUCCESS, SUCCESS_CODE, null, null, doctorShareReqDto);
			} catch (Exception e) {
				
			}
			return new Response(ERROR, null, null, null, null);
	}


	@Override
	public Response getGlobalDoctorShareMasterList(DoctorShareReqDto doctorShareReqDto) {
		try {
			 return iGlobalDoctorShareGroupWiseDao.getGroupWiseDepartmentList(doctorShareReqDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@Override
	public Response getGlobalDoctorShareDepartmentMasterListSearch(DoctorShareReqDto doctorShareReqDto) {
		try {
			 return iGlobalDoctorShareGroupWiseDao.getGroupWiseDepartmentListSearch(doctorShareReqDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}


	@Override
	public Response getRatesForSelectedService(GlobalDocShareServiceRateDto doctorShareReqDto) {
		try {
			for(GlobalDocShareServiceRateDto dto : doctorShareReqDto.getGlobalDocShareServiceRateDtosList())
			{
				Double rate = 0.00;
				UnitServiceTariffMasterDto unitServiceTariffMasterDto =new UnitServiceTariffMasterDto();
					unitServiceTariffMasterDto.setOrganizationId(dto.getOrganizationId());
					unitServiceTariffMasterDto.setUnitId(dto.getUnitId());
					unitServiceTariffMasterDto.setVisitTypeId(dto.getVisitTypeId());
					unitServiceTariffMasterDto.setBillingBedCategoryId(dto.getBedBillingCategoryId());
					unitServiceTariffMasterDto.setPatientTypeId(dto.getPatientTypeId()); 
					unitServiceTariffMasterDto.setPaymentEntitlementId(SELF);//Self
					unitServiceTariffMasterDto.setServiceId(dto.getServicesMstId());
					unitServiceTariffMasterDto.setOrderDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "yyyy-MM-dd"));
					unitServiceTariffMasterDto.setTariffId(dto.getTariffId());
				unitServiceTariffMasterDto = iEncounterMasterService.getBasePriceByServiceTariffMaster(unitServiceTariffMasterDto);
			
				rate = (unitServiceTariffMasterDto.getRate() != null ? unitServiceTariffMasterDto.getRate() : 0.0);
				dto.setRate(rate);
				
				if(dto.getAmount() > 0) {
				if(dto.getAmount() >= rate){ 
					dto.setAmount(rate);
					dto.setServiceShare(rate > 0 ? 100.00 :0.0 );
				}else{
					
					Double tempShare = ((dto.getAmount()/rate)*100);
					dto.setServiceShare(Double.valueOf(OrderMasterServiceImpl.df2.format(tempShare)));
					}
				}
				
				if(dto.getServiceShare()!=null && dto.getServiceShare()>0.00)
				{
					 dto.setAmount(Double.valueOf(OrderMasterServiceImpl.df2.format(rate * (dto.getServiceShare() / 100))));
					 if(rate == 0.00) {
						 dto.setServiceShare(0.00);
					 }
					//dto.setAmountOld(rate * (dto.getServiceShare()/100));
				}
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, doctorShareReqDto.getGlobalDocShareServiceRateDtosList(), null);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}


	@Override
	public Response<DoctorShareReqDto, Integer> getIndividualDoctorReferralDetailsByDepartmentId(
			IndividualDoctorShareReqDto doctorShareReqDto) {
		try {
			 return iGlobalDoctorShareGroupWiseDao.getIndividualDoctorReferralDetailsByDepartmentId(doctorShareReqDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@Override
	public Response<DoctorShareReqDto, Integer> getIndividualDoctorReferralDetailsByDepartmentIdSearch(
			IndividualDoctorShareReqDto doctorShareReqDto) {
		try {
			 return iGlobalDoctorShareGroupWiseDao.getIndividualDoctorReferralDetailsByDepartmentIdSearch(doctorShareReqDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@Override
	public Response<DoctorShareReqDto, Integer> getIndividualDoctorReferralDetailsByReferralId(
			IndividualDoctorShareReqDto individualDoctorShareReqDto) {
		try {
			 Response res=iGlobalDoctorShareGroupWiseDao.getGroupWiseReferralShareByReferralId(individualDoctorShareReqDto);
			Response res1=iGlobalDoctorShareGroupWiseDao.getServiceWiseReferralShareByReferralId(individualDoctorShareReqDto);
			
			List<TIndividualDocShareGroupWiseDto> groupList=res.getListObject();
			List<TIndividualDocShareServiceWiseDto> serviceList=res1.getListObject();
			individualDoctorShareReqDto.setListTIndividualDocShareGroupWiseDto(groupList);
			individualDoctorShareReqDto.setListTIndividualDocShareServiceWiseDto(serviceList);
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, individualDoctorShareReqDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}


	@Override
	public Response duplicateCheckForGlobalDoctorShare(
			DoctorShareReqDto reqDto) {
		try{
			
			List<TGlobalDocShareGroupWiseDto> docShareGroupWiseDtosList = new LinkedList<>();
			Set<Integer> deptIds = new HashSet<>();
			Set<Integer> billingBedCategoryIds = new HashSet<>();
			Set<Integer> visitTypeIds = new HashSet<>();
			Set<Integer> patientTypeIds = new HashSet<>();
			Set<Integer> paymentEntitlementIds = new HashSet<>();
			Set<Integer> tariffIds = new HashSet<>();
			Set<Date> effectiveDates = new HashSet<>();
			Set<Integer> serviceIds = new HashSet<>();
			List<TGlobalDocShareServiceWiseDto> docShareServiceWiseDtosList = new LinkedList<>();
			
			if(reqDto.getListTGlobalDocShareGroupWiseDto() != null && reqDto.getListTGlobalDocShareGroupWiseDto().size() > 0)
			{
				ListIterator<TGlobalDocShareGroupWiseDto> servicDtlsItr = reqDto.getListTGlobalDocShareGroupWiseDto().listIterator();
				TGlobalDocShareGroupWiseDto tGlobalDocShareGroupWiseDto = null;
				while(servicDtlsItr.hasNext()){
					tGlobalDocShareGroupWiseDto = servicDtlsItr.next();
					tGlobalDocShareGroupWiseDto.setOrgId(reqDto.getOrgId());
					tGlobalDocShareGroupWiseDto.setUnitId(reqDto.getUnitId());
						deptIds.add(tGlobalDocShareGroupWiseDto.getDepartmentId());
						billingBedCategoryIds.add(tGlobalDocShareGroupWiseDto.getBillingBedCategoryId());
						visitTypeIds.add(tGlobalDocShareGroupWiseDto.getVisitTypeId());
						patientTypeIds.add(tGlobalDocShareGroupWiseDto.getPatientTypeId());
						paymentEntitlementIds.add(tGlobalDocShareGroupWiseDto.getPaymentEntitlementId());
						tariffIds.add(tGlobalDocShareGroupWiseDto.getTariffId());
						effectiveDates.add(GlobalCommonDateUtils.getDate(GlobalCommonDateUtils.getStringDate(tGlobalDocShareGroupWiseDto.getEffectiveDate(),"yyyy-MM-dd"), "yyyy-MM-dd"));
					docShareGroupWiseDtosList.add(tGlobalDocShareGroupWiseDto);
				}
				return iGlobalDoctorShareGroupWiseDao.checkUniqueGlobalGroup(deptIds,billingBedCategoryIds,visitTypeIds,patientTypeIds,paymentEntitlementIds,tariffIds,effectiveDates,'N');
			}
			
			if(reqDto.getListTGlobalDocShareServiceWiseDto() != null && reqDto.getListTGlobalDocShareServiceWiseDto().size() > 0){
				ListIterator<TGlobalDocShareServiceWiseDto> servicDtlsItr = reqDto.getListTGlobalDocShareServiceWiseDto().listIterator();
				TGlobalDocShareServiceWiseDto tGlobalDocShareServiceWiseDto = null;
				while(servicDtlsItr.hasNext()){
						tGlobalDocShareServiceWiseDto = servicDtlsItr.next();
						tGlobalDocShareServiceWiseDto.setOrgId(reqDto.getOrgId());
						tGlobalDocShareServiceWiseDto.setUnitId(reqDto.getUnitId());
							serviceIds.add(tGlobalDocShareServiceWiseDto.getServiceId());
							billingBedCategoryIds.add(tGlobalDocShareServiceWiseDto.getBillingBedCategoryId());
							visitTypeIds.add(tGlobalDocShareServiceWiseDto.getVisitTypeId());
							patientTypeIds.add(tGlobalDocShareServiceWiseDto.getPatientTypeId());
							paymentEntitlementIds.add(tGlobalDocShareServiceWiseDto.getPaymentEntitlementId());
							tariffIds.add(tGlobalDocShareServiceWiseDto.getTariffId());
							effectiveDates.add(GlobalCommonDateUtils.getDate(GlobalCommonDateUtils.getStringDate(tGlobalDocShareServiceWiseDto.getEffectiveDate(),"yyyy-MM-dd"), "yyyy-MM-dd"));
						docShareServiceWiseDtosList.add(tGlobalDocShareServiceWiseDto);
				}
				iGlobalDoctorShareServiceWiseDao.checkUniqueGlobalService(serviceIds,billingBedCategoryIds,visitTypeIds,patientTypeIds,paymentEntitlementIds,tariffIds,effectiveDates,'N');
			}
		}catch(Exception e){
			
		}
		return new Response(ERROR,null,null,null,null);
	}


	@Override
	@Transactional
	public Response<DoctorShareReqDto, Integer> updateStatusByReferralId(
			IndividualDoctorShareReqDto doctorShareReqDto1) {
		// TODO Auto-generated method stub
		return iGlobalDoctorShareServiceWiseDao.updateStatusByReferralId(doctorShareReqDto1);
	}

	
}
