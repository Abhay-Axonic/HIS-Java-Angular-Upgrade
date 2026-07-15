package com.param.adt.discharge.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.adt.discharge.dao.IDischargeFieldUnitWiseDao;
import com.param.adt.discharge.model.DischargeFieldUnitWiseDTO;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

@Service
@SuppressWarnings("rawtypes")
public class DischargeFieldUnitWiseServiceImpl implements IDischargeFieldUnitWiseService, ICommonConstants {

	@Autowired
	IDischargeFieldUnitWiseDao iDischargeFieldUnitWiseDao;

	@Override
	public Response saveDischargeFieldUnitWise(DischargeFieldUnitWiseDTO dischargeFieldUnitWiseDTO) {

		try {


			Response res=iDischargeFieldUnitWiseDao.checkDischargeFeildIdAlradyExist(dischargeFieldUnitWiseDTO);
			try {
				if(res.getListObject()!=null && res.getListObject().size()>0){

					return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
				}else{
					return iDischargeFieldUnitWiseDao.saveDischargeFieldUnitWise(dischargeFieldUnitWiseDTO);
				}

			} catch (Exception e) {
			}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);


		} catch (Exception e) {
		}

		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateDischargeFieldUnitWise(DischargeFieldUnitWiseDTO dischargeFieldUnitWiseDTO) {

//		Response res = iDischargeFieldUnitWiseDao
//				.getDischargeFieldUnitWiseByUnitwisetidNotDischargeid(dischargeFieldUnitWiseDTO);

		try {
//			if (res.getListObject() != null && res.getListObject().size() > 0) {
//				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
//			} else {
				return iDischargeFieldUnitWiseDao.updateDischargeFieldUnitWise(dischargeFieldUnitWiseDTO);
//			}

		} catch (Exception e) {
			// TODO: handle exception
		}

		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateDischargeFieldUnitWiseStatus(DischargeFieldUnitWiseDTO dischargeFieldUnitWiseDTO) {

		try {
			return iDischargeFieldUnitWiseDao.updateDischargeFieldUnitWiseStatus(dischargeFieldUnitWiseDTO);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getDischargeFieldUnitWiseById(Integer dischargeFieldUnitWiseId) {
		try {
			return iDischargeFieldUnitWiseDao.getDischargeFieldUnitWiseById(dischargeFieldUnitWiseId);

		} catch (Exception e) {

		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getDischargeFieldUnitWiseMasterList(DischargeFieldUnitWiseDTO dischargeFieldUnitWiseDTO) {
		try {
			return iDischargeFieldUnitWiseDao.getDischargeFieldUnitWiseMasterList(dischargeFieldUnitWiseDTO);

		} catch (Exception e) {

		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getDischargeFieldUnitWiseCount(Integer organizationId, Integer unitId,  String searchKeyword) {
		try {
			return iDischargeFieldUnitWiseDao.getDischargeFieldUnitWiseCount(organizationId, unitId, searchKeyword);

		} catch (Exception e) {

		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getActiveDischargeFieldUnitWise(Integer organizationId, Integer unitId) {
		try {
			return iDischargeFieldUnitWiseDao.getActiveDischargeFieldUnitWise(organizationId, unitId);

		} catch (Exception e) {

		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getDischargeFieldUnitWiseList(DischargeFieldUnitWiseDTO dischargeFieldUnitWiseDTO) {
		try {
			return iDischargeFieldUnitWiseDao.getDischargeFieldUnitWiseList(dischargeFieldUnitWiseDTO);

		} catch (Exception e) {

		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}
