package com.param.billing.unit.dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.param.billing.global.model.TGlobalDocShareGroupWise;
import com.param.billing.unit.dto.DoctorShareReqDto;
import com.param.billing.unit.dto.IndividualDoctorShareReqDto;
import com.param.billing.unit.dto.TGlobalDocShareGroupWiseDto;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IGlobalDoctorShareGroupWiseDao extends IGenericDao<TGlobalDocShareGroupWise, Integer>{
	public Response saveGlobalDoctorShareGroupWiseDao(List<TGlobalDocShareGroupWiseDto> detailDto)throws ServiceException,SQLException;
	
	public Response getDoctorGradeList(Integer orgId,Integer orgUnitId) throws ApplicationException;

	public Response getGroupWiseDoctorShareByDepartmentId(DoctorShareReqDto doctorShareReqDto);

	public Response getGroupWiseDepartmentList(DoctorShareReqDto doctorShareReqDto);

	public Response checkUniqueGlobalGroup(Set<Integer> deptIds, Set<Integer> billingBedCategoryIds,
			Set<Integer> visitTypeIds, Set<Integer> patientTypeIds, Set<Integer> paymentEntitlementIds,
			Set<Integer> tariffIds, Set<Date> effectiveDates,Character inactive);

	public Response<DoctorShareReqDto, Integer> getIndividualDoctorReferralDetailsByDepartmentId(
			IndividualDoctorShareReqDto doctorShareReqDto);

	public Response getGroupWiseReferralShareByReferralId(IndividualDoctorShareReqDto individualDoctorShareReqDto);

	public Response getServiceWiseReferralShareByReferralId(IndividualDoctorShareReqDto individualDoctorShareReqDto);

	public Response getGroupWiseDepartmentListSearch(DoctorShareReqDto doctorShareReqDto);

	public Response<DoctorShareReqDto, Integer> getIndividualDoctorReferralDetailsByDepartmentIdSearch(
			IndividualDoctorShareReqDto doctorShareReqDto);
}
