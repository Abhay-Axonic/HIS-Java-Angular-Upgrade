package com.param.billing.unit.dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.param.billing.global.model.TIndividualDocShareGroupWise;
import com.param.billing.unit.dto.IndividualDoctorShareReqDto;
import com.param.billing.unit.dto.TIndividualDocShareGroupWiseDto;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;

import in.param.common.dao.IGenericDao;

@SuppressWarnings("rawtypes")
public interface IIndividualDoctorShareGroupWiseDao extends IGenericDao<TIndividualDocShareGroupWise, Integer>{
	public Response saveIndividualDoctorShareGroupWise(List<TIndividualDocShareGroupWiseDto> detailDto)throws ServiceException,SQLException;

	public Response getGroupWiseDoctorShareByDepartmentIdAndDoctorId(
			IndividualDoctorShareReqDto individualDoctorShareReqDto);

	public Response getDoctorShareDepartmentMasterList(IndividualDoctorShareReqDto doctorShareReqDto);

	public List<TIndividualDocShareGroupWiseDto> checkUniqueIndividualGroup(Set<Integer> deptIds, Set<Integer> billingBedCategoryIds, Set<Integer> visitTypeIds,
			Set<Integer> patientTypeIds, Set<Integer> paymentEntitlementIds, Set<Integer> doctorIds,
			Set<Integer> tariffIds, Set<Date> effectiveDates);

	public List<TIndividualDocShareGroupWiseDto> checkUniqueIndividualGroupForReferral(Set<Integer> deptIds, Set<Integer> billingBedCategoryIds,
			Set<Integer> visitTypeIds, Set<Integer> patientTypeIds, Set<Integer> paymentEntitlementIds,
			Set<Integer> doctorIds, Set<Integer> tariffIds, Set<Date> effectiveDates);

	public Response saveIndividualDoctorShareGroupWiseForReferral(
			List<TIndividualDocShareGroupWiseDto> docShareGroupWiseDtosList) throws SQLException;

	public Response getDoctorShareDepartmentMasterListSearch(IndividualDoctorShareReqDto doctorShareReqDto);


}
