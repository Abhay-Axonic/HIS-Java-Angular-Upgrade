package com.param.billing.unit.dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.param.billing.global.model.TGlobalDocShareServiceWise;
import com.param.billing.unit.dto.DoctorShareReqDto;
import com.param.billing.unit.dto.IndividualDoctorShareReqDto;
import com.param.billing.unit.dto.TGlobalDocShareServiceWiseDto;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;

import in.param.common.dao.IGenericDao;
@SuppressWarnings("rawtypes")
public interface IGlobalDoctorShareServiceWiseDao extends IGenericDao<TGlobalDocShareServiceWise, Integer>{
	public Response saveGlobalDoctorShareServiceWiseDao(List<TGlobalDocShareServiceWiseDto> docShareServiceWiseDtosList)throws ServiceException,SQLException;

	public Response getServiceDepartmentList(Integer orgId, Integer orgUnitId);

	public Response getServiceWiseDoctorShareByDepartmentId(DoctorShareReqDto doctorShareReqDto);

	public void checkUniqueGlobalService(Set<Integer> serviceIds, Set<Integer> billingBedCategoryIds,
			Set<Integer> visitTypeIds, Set<Integer> patientTypeIds, Set<Integer> paymentEntitlementIds,
			Set<Integer> tariffIds, Set<Date> effectiveDates, Character inactive);

	public Response<DoctorShareReqDto, Integer> updateStatusByReferralId(IndividualDoctorShareReqDto doctorShareReqDto);
}