package com.param.billing.unit.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.billing.unit.dao.IDoctorSlabMasterDao;
import com.param.billing.unit.dto.DoctorSlabDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({ "rawtypes","unchecked"})
public class DoctorSlabMasterServiceImpl implements IDoctorSlabMasterService, ICommonConstants {

	@Autowired 
	IDoctorSlabMasterDao iDoctorSlabMasterDao;
	 
	
	@Override
	public Response doctorSlabSave(List<DoctorSlabDto> doctorSlabDto) throws ApplicationException {
		try {
			
			for (DoctorSlabDto dto : doctorSlabDto) {
				Response res = iDoctorSlabMasterDao.doctorSlabSave(dto);
				if(res.getStatus().equals(ERROR)) {
					 return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
				}
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, COMPANY_CONTRACT_CREATED, null, null);
			
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}


	@Override
	public Response getdoctorSlabList(DoctorSlabDto doctorSlabDto) throws ApplicationException {
		try {
			
				return iDoctorSlabMasterDao.getdoctorSlabList(doctorSlabDto);
			
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}


	@Override
	public Response getdoctorSlabListByDoctorId(DoctorSlabDto doctorSlabDto) throws ApplicationException {
		try {
			
			return iDoctorSlabMasterDao.getdoctorSlabListByDoctorId(doctorSlabDto);
		
	} catch (Exception e) {
		
	}
	return new Response(ERROR, null, null, null, null);
	}


	@Override
	public Response updateSlabStatusByDoctorId(DoctorSlabDto doctorSlabDto) throws ApplicationException {
		try {
			
			return iDoctorSlabMasterDao.updateSlabStatusByDoctorId(doctorSlabDto);
		
	} catch (Exception e) {
		
	}
	return new Response(ERROR, null, null, null, null);
	}

	
}
