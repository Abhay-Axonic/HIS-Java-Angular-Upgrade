package com.param.adt.admission.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.adt.admission.dao.IMewsMapperDao;
import com.param.adt.admission.dao.INursingScaleScoreDao;
import com.param.adt.admission.dto.GcsScaleScoreDTO;
import com.param.adt.admission.dto.MewsMapperDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({"rawtypes"})
public class MewsMapperServiceImpl implements IMewsMapperService, ICommonConstants{

	@Autowired
	IMewsMapperDao iMewsMapperDao;
	
	@Autowired
	private INursingScaleScoreDao iNursingScaleScoreDao;

	@Override
	public Response saveMewsMapper(MewsMapperDto mewsMapperDto) throws ApplicationException {
		try {
			
			 
			
			Response res =  iMewsMapperDao.saveMewsMapper(mewsMapperDto);
			
			if(mewsMapperDto.getTabNumber() != null) {
				
				GcsScaleScoreDTO gcsScaleScoreDTO = new GcsScaleScoreDTO();
	            gcsScaleScoreDTO.setEncounterId(0);
	            gcsScaleScoreDTO.setAdmissionId(mewsMapperDto.getAdmissionId());
	            gcsScaleScoreDTO.setNursingScoreName(mewsMapperDto.getNursingScoreName());
	            gcsScaleScoreDTO.setTotalGcsScore(mewsMapperDto.getTotalScore().toString());
	            gcsScaleScoreDTO.setSaveAsDraft('N');
	            gcsScaleScoreDTO.setTabNumber(mewsMapperDto.getTabNumber());
	            gcsScaleScoreDTO.setUnitId(mewsMapperDto.getUnitId());
	            gcsScaleScoreDTO.setOrganizationId(mewsMapperDto.getOrganizationId());
	            gcsScaleScoreDTO.setStatus('A');
	            gcsScaleScoreDTO.setCreatedBy(mewsMapperDto.getCreatedBy());
	            gcsScaleScoreDTO.setUpdatedBy(mewsMapperDto.getUpdatedBy());
	            gcsScaleScoreDTO.setCreatedDate(mewsMapperDto.getCreatedDate());
	            gcsScaleScoreDTO.setUpdatedDate(mewsMapperDto.getUpdatedDate());
	            gcsScaleScoreDTO.setIsScoreWith3((mewsMapperDto.getRemark()!=null && mewsMapperDto.getRemark().equalsIgnoreCase("scoreWith3")) ? true : false);
	            iNursingScaleScoreDao.saveNursingScaleScore(gcsScaleScoreDTO);
	            
			}
			
	return res;
	
	} catch (Exception e) {
		
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

	@Override
	public Response getMewsMapperList(Integer patientId, Integer organizationId, Integer unitId)
			throws ApplicationException {
		try {
			
				return iMewsMapperDao.getMewsMapperList(patientId, organizationId, unitId);
	
		} catch (Exception e) {
			
		}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);

	}

	@Override
	public Response getCountOnMewsMapper(Integer patientId, Integer organizationId, Integer unitId)
			throws ApplicationException {
		try {
			
			return iMewsMapperDao.getCountOnMewsMapper(patientId, organizationId, unitId);

	} catch (Exception e) {
		
	}
return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);

}
	
	@Override
	public Response getListOfAllergies(Integer patientId, Integer organizationId, Integer unitId)
			throws ApplicationException {
		try {
			
			return iMewsMapperDao.getListOfAllergies(patientId, organizationId, unitId);

	} catch (Exception e) {
		
	}
return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);

}

	@Override
	public Response getListOfOrderedProcedure(Integer patientId, Integer organizationId, Integer unitId)
			throws ApplicationException {
		try {
					
					return iMewsMapperDao.getListOfOrderedProcedure(patientId, organizationId, unitId);
		
			} catch (Exception e) {
				
			}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			
		}
	
}
