package com.param.billing.unit.service;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.billing.unit.dao.IIndividualDoctorShareGroupWiseDao;
import com.param.billing.unit.dao.IIndividualDoctorShareServiceWiseDao;
import com.param.billing.unit.dto.IndividualDoctorShareReqDto;
import com.param.billing.unit.dto.TIndividualDocShareGroupWiseDto;
import com.param.billing.unit.dto.TIndividualDocShareServiceWiseDto;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({ "rawtypes" })
public class IndividualDoctorShareServiceImpl implements IIndividualDoctorShareService, ICommonConstants {

	@Autowired
	IIndividualDoctorShareGroupWiseDao iIndividualDoctorShareGroupWiseDao;
	
	@Autowired
	IIndividualDoctorShareServiceWiseDao iIndividualDoctorShareServiceWiseDao;
	

	@Override
	public Response saveIndividualDoctorShare(IndividualDoctorShareReqDto reqDto)
			throws ApplicationException {
		try{
			
			List<TIndividualDocShareGroupWiseDto> docShareGroupWiseDtosList = new LinkedList<>();
			Set<Integer> deptIds = new HashSet<>();
			Set<Integer> billingBedCategoryIds = new HashSet<>();
			Set<Integer> visitTypeIds = new HashSet<>();
			Set<Integer> patientTypeIds = new HashSet<>();
			Set<Integer> paymentEntitlementIds = new HashSet<>();
			Set<Integer> doctorIds= new HashSet<>();
			Set<Integer> tariffIds = new HashSet<>();
			Set<Date> effectiveDates = new HashSet<>();
			Set<Integer> serviceIds = new HashSet<>();
			List<TIndividualDocShareServiceWiseDto> docShareServiceWiseDtosList = new LinkedList<>();
			
			if(reqDto != null){
			//save Individual Doctor Share Group Wise details
			if(reqDto.getListTIndividualDocShareGroupWiseDto() != null && reqDto.getListTIndividualDocShareGroupWiseDto().size() > 0){
				ListIterator<TIndividualDocShareGroupWiseDto> servicDtlsItr = reqDto.getListTIndividualDocShareGroupWiseDto().listIterator();
				TIndividualDocShareGroupWiseDto tIndividualDocShareGroupWiseDto = null;
				while(servicDtlsItr.hasNext()){
					tIndividualDocShareGroupWiseDto = servicDtlsItr.next();
						tIndividualDocShareGroupWiseDto.setOrgId(reqDto.getOrgId());
						tIndividualDocShareGroupWiseDto.setUnitId(reqDto.getUnitId());
							deptIds.add(tIndividualDocShareGroupWiseDto.getDepartmentId());
							billingBedCategoryIds.add(tIndividualDocShareGroupWiseDto.getBillingBedCategoryId());
							visitTypeIds.add(tIndividualDocShareGroupWiseDto.getVisitTypeId());
							patientTypeIds.add(tIndividualDocShareGroupWiseDto.getPatientTypeId());
							paymentEntitlementIds.add(tIndividualDocShareGroupWiseDto.getPaymentEntitlementId());
							doctorIds.add(tIndividualDocShareGroupWiseDto.getDoctorId());
							tariffIds.add(tIndividualDocShareGroupWiseDto.getTariffId());
							effectiveDates.add(GlobalCommonDateUtils.getDate(GlobalCommonDateUtils.getStringDate(tIndividualDocShareGroupWiseDto.getEffectiveDate(),"yyyy-MM-dd"), "yyyy-MM-dd"));
					docShareGroupWiseDtosList.add(tIndividualDocShareGroupWiseDto);
				}
				iIndividualDoctorShareGroupWiseDao.checkUniqueIndividualGroup(deptIds,billingBedCategoryIds,visitTypeIds,patientTypeIds,paymentEntitlementIds,doctorIds,tariffIds,effectiveDates);
				Response res = iIndividualDoctorShareGroupWiseDao.saveIndividualDoctorShareGroupWise(docShareGroupWiseDtosList);
			}
			
			//save Individual Doctor Share Service Wise details
			if(reqDto.getListTIndividualDocShareServiceWiseDto() != null && reqDto.getListTIndividualDocShareServiceWiseDto().size() > 0){
				ListIterator<TIndividualDocShareServiceWiseDto> servicDtlsItr = reqDto.getListTIndividualDocShareServiceWiseDto().listIterator();
				TIndividualDocShareServiceWiseDto tIndividualDocShareServiceWiseDto = null;
				while(servicDtlsItr.hasNext()){
					tIndividualDocShareServiceWiseDto = servicDtlsItr.next();
						tIndividualDocShareServiceWiseDto.setOrgId(reqDto.getOrgId());
						tIndividualDocShareServiceWiseDto.setUnitId(reqDto.getUnitId());
							serviceIds.add(tIndividualDocShareServiceWiseDto.getServiceId());
							billingBedCategoryIds.add(tIndividualDocShareServiceWiseDto.getBillingBedCategoryId());
							visitTypeIds.add(tIndividualDocShareServiceWiseDto.getVisitTypeId());
							patientTypeIds.add(tIndividualDocShareServiceWiseDto.getPatientTypeId());
							paymentEntitlementIds.add(tIndividualDocShareServiceWiseDto.getPaymentEntitlementId());
							doctorIds.add(tIndividualDocShareServiceWiseDto.getDoctorId());
							tariffIds.add(tIndividualDocShareServiceWiseDto.getTariffId());
							effectiveDates.add(GlobalCommonDateUtils.getDate(GlobalCommonDateUtils.getStringDate(tIndividualDocShareServiceWiseDto.getEffectiveDate(),"yyyy-MM-dd"), "yyyy-MM-dd"));
					docShareServiceWiseDtosList.add(tIndividualDocShareServiceWiseDto);
				}
					iIndividualDoctorShareServiceWiseDao.checkUniqueIndividualService(serviceIds,billingBedCategoryIds,visitTypeIds,patientTypeIds,paymentEntitlementIds,doctorIds,tariffIds,effectiveDates);
				iIndividualDoctorShareServiceWiseDao.saveIndividualDocShareServiceWise(docShareServiceWiseDtosList);
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



	@Override
	public Response getDoctorShareDetailsByDepartmentIdAndDoctorId(IndividualDoctorShareReqDto individualDoctorShareReqDto) {
		try {
			 Response res=iIndividualDoctorShareGroupWiseDao.getGroupWiseDoctorShareByDepartmentIdAndDoctorId(individualDoctorShareReqDto);
			Response res1=iIndividualDoctorShareServiceWiseDao.getServiceWiseDoctorShareByDepartmentIdAndDoctorId(individualDoctorShareReqDto);
			
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
	public Response getDoctorShareDepartmentMasterList(IndividualDoctorShareReqDto doctorShareReqDto) {
		try{
			return iIndividualDoctorShareGroupWiseDao.getDoctorShareDepartmentMasterList(doctorShareReqDto);
			}catch(ServiceException se){
				throw se;
			}catch(Exception e){
				
				throw new ServiceException(e.getMessage());
			}
	}
	
	@Override
	public Response getDoctorShareDepartmentMasterListSearch(IndividualDoctorShareReqDto doctorShareReqDto) {
		try{
			return iIndividualDoctorShareGroupWiseDao.getDoctorShareDepartmentMasterListSearch(doctorShareReqDto);
			}catch(ServiceException se){
				throw se;
			}catch(Exception e){
				
				throw new ServiceException(e.getMessage());
			}
	}



	@Override
	public Response<IndividualDoctorShareReqDto, Integer> saveReferralShare(IndividualDoctorShareReqDto reqDto) {
		try{
			
			List<TIndividualDocShareGroupWiseDto> docShareGroupWiseDtosList = new LinkedList<>();
			Set<Integer> deptIds = new HashSet<>();
			Set<Integer> billingBedCategoryIds = new HashSet<>();
			Set<Integer> visitTypeIds = new HashSet<>();
			Set<Integer> patientTypeIds = new HashSet<>();
			Set<Integer> paymentEntitlementIds = new HashSet<>();
			Set<Integer> doctorIds= new HashSet<>();
			Set<Integer> tariffIds = new HashSet<>();
			Set<Date> effectiveDates = new HashSet<>();
			Set<Integer> serviceIds = new HashSet<>();
			List<TIndividualDocShareServiceWiseDto> docShareServiceWiseDtosList = new LinkedList<>();
			
			if(reqDto != null){
			//save Individual Doctor Share Group Wise details
			if(reqDto.getListTIndividualDocShareGroupWiseDto() != null && reqDto.getListTIndividualDocShareGroupWiseDto().size() > 0){
				ListIterator<TIndividualDocShareGroupWiseDto> servicDtlsItr = reqDto.getListTIndividualDocShareGroupWiseDto().listIterator();
				TIndividualDocShareGroupWiseDto tIndividualDocShareGroupWiseDto = null;
				while(servicDtlsItr.hasNext()){
					tIndividualDocShareGroupWiseDto = servicDtlsItr.next();
						tIndividualDocShareGroupWiseDto.setOrgId(reqDto.getOrgId());
						tIndividualDocShareGroupWiseDto.setUnitId(reqDto.getUnitId());
							deptIds.add(tIndividualDocShareGroupWiseDto.getDepartmentId());
							billingBedCategoryIds.add(tIndividualDocShareGroupWiseDto.getBillingBedCategoryId());
							visitTypeIds.add(tIndividualDocShareGroupWiseDto.getVisitTypeId());
							patientTypeIds.add(tIndividualDocShareGroupWiseDto.getPatientTypeId());
							paymentEntitlementIds.add(tIndividualDocShareGroupWiseDto.getPaymentEntitlementId());
							doctorIds.add(tIndividualDocShareGroupWiseDto.getDoctorId());
							tariffIds.add(tIndividualDocShareGroupWiseDto.getTariffId());
							effectiveDates.add(GlobalCommonDateUtils.getDate(GlobalCommonDateUtils.getStringDate(tIndividualDocShareGroupWiseDto.getEffectiveDate(),"yyyy-MM-dd"), "yyyy-MM-dd"));
					docShareGroupWiseDtosList.add(tIndividualDocShareGroupWiseDto);
				}
				iIndividualDoctorShareGroupWiseDao.checkUniqueIndividualGroupForReferral(deptIds,billingBedCategoryIds,visitTypeIds,patientTypeIds,paymentEntitlementIds,doctorIds,tariffIds,effectiveDates);
				iIndividualDoctorShareGroupWiseDao.saveIndividualDoctorShareGroupWiseForReferral(docShareGroupWiseDtosList);
			}
			
			//save Individual Doctor Share Service Wise details
			if(reqDto.getListTIndividualDocShareServiceWiseDto() != null && reqDto.getListTIndividualDocShareServiceWiseDto().size() > 0){
				ListIterator<TIndividualDocShareServiceWiseDto> servicDtlsItr = reqDto.getListTIndividualDocShareServiceWiseDto().listIterator();
				TIndividualDocShareServiceWiseDto tIndividualDocShareServiceWiseDto = null;
				while(servicDtlsItr.hasNext()){
					tIndividualDocShareServiceWiseDto = servicDtlsItr.next();
						tIndividualDocShareServiceWiseDto.setOrgId(reqDto.getOrgId());
						tIndividualDocShareServiceWiseDto.setUnitId(reqDto.getUnitId());
							serviceIds.add(tIndividualDocShareServiceWiseDto.getServiceId());
							billingBedCategoryIds.add(tIndividualDocShareServiceWiseDto.getBillingBedCategoryId());
							visitTypeIds.add(tIndividualDocShareServiceWiseDto.getVisitTypeId());
							patientTypeIds.add(tIndividualDocShareServiceWiseDto.getPatientTypeId());
							paymentEntitlementIds.add(tIndividualDocShareServiceWiseDto.getPaymentEntitlementId());
							doctorIds.add(tIndividualDocShareServiceWiseDto.getDoctorId());
							tariffIds.add(tIndividualDocShareServiceWiseDto.getTariffId());
							effectiveDates.add(GlobalCommonDateUtils.getDate(GlobalCommonDateUtils.getStringDate(tIndividualDocShareServiceWiseDto.getEffectiveDate(),"yyyy-MM-dd"), "yyyy-MM-dd"));
					docShareServiceWiseDtosList.add(tIndividualDocShareServiceWiseDto);
				}
					iIndividualDoctorShareServiceWiseDao.checkUniqueIndividualServiceForReferral(serviceIds,billingBedCategoryIds,visitTypeIds,patientTypeIds,paymentEntitlementIds,doctorIds,tariffIds,effectiveDates);
				iIndividualDoctorShareServiceWiseDao.saveIndividualDocShareServiceWiseForReferral(docShareServiceWiseDtosList);
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

	

	
}
