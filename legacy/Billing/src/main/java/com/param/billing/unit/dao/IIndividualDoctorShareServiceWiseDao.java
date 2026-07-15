package com.param.billing.unit.dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.param.billing.global.model.TIndividualDocShareServiceWise;
import com.param.billing.unit.dto.IndividualDoctorShareReqDto;
import com.param.billing.unit.dto.TIndividualDocShareGroupWiseDto;
import com.param.billing.unit.dto.TIndividualDocShareServiceWiseDto;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;

import in.param.common.dao.IGenericDao;
@SuppressWarnings("rawtypes")
public interface IIndividualDoctorShareServiceWiseDao extends IGenericDao<TIndividualDocShareServiceWise, Integer>{
	public Response saveIndividualDocShareServiceWise(List<TIndividualDocShareServiceWiseDto> detailDto)throws ServiceException,SQLException;

	public Response getServiceWiseDoctorShareByDepartmentIdAndDoctorId(
			IndividualDoctorShareReqDto individualDoctorShareReqDto);

	void checkUniqueIndividualService(Set<Integer> serviceIds, Set<Integer> billingBedCategoryIds,
			Set<Integer> visitTypeIds, Set<Integer> patientTypeIds, Set<Integer> paymentEntitlementIds,
			Set<Integer> doctorIds, Set<Integer> tariffIds, Set<Date> effectiveDates);

	public void checkUniqueIndividualServiceForReferral(Set<Integer> serviceIds, Set<Integer> billingBedCategoryIds,
			Set<Integer> visitTypeIds, Set<Integer> patientTypeIds, Set<Integer> paymentEntitlementIds,
			Set<Integer> doctorIds, Set<Integer> tariffIds, Set<Date> effectiveDates);

	public Response saveIndividualDocShareServiceWiseForReferral(
			List<TIndividualDocShareServiceWiseDto> docShareServiceWiseDtosList) throws SQLException;

}