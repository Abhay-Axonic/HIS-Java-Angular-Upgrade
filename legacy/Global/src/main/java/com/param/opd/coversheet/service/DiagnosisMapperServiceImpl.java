package com.param.opd.coversheet.service;

import java.util.Date;

import java.util.ListIterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.opd.coversheet.dao.IDiagnosisMapperDao;
import com.param.opd.coversheet.dto.DiagnosisMapperDto;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({ "rawtypes"})
public class DiagnosisMapperServiceImpl implements IDiagnosisMapperService, ICommonConstants{
	final static Logger logger = Logger.getLogger(DiagnosisMapperServiceImpl.class);

	@Autowired
	IDiagnosisMapperDao iDiagnosisMapperDao;

	@Override
	public Response saveDiagnosisMapper(DiagnosisMapperDto diagnosisMapperDto) throws ApplicationException{
		try {
		Response response=iDiagnosisMapperDao.checkDiagnosisMapperAvailable(diagnosisMapperDto);
		if(response.getListObject().size()>0 && response.getListObject()!=null){
			
			return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
		}else{
		DiagnosisMapperDto dto=new DiagnosisMapperDto();
			dto.setDiagnosisId(diagnosisMapperDto.getDiagnosisId());
			dto.setIsNotifiable(diagnosisMapperDto.getIsNotifiable());
			dto.setIsCommunicable(diagnosisMapperDto.getIsCommunicable());
			dto.setIsNonCommunicable(diagnosisMapperDto.getIsNonCommunicable());
			dto.setIsMedicoLegal(diagnosisMapperDto.getIsMedicoLegal());
			dto.setStatus(diagnosisMapperDto.getStatus());
			dto.setIsDeleted('N');
			dto.setOrganizationId(diagnosisMapperDto.getOrganizationId());
			dto.setUnitId(diagnosisMapperDto.getUnitId());
			dto.setCreatedBy(diagnosisMapperDto.getCreatedBy());
			dto.setUpdatedBy(diagnosisMapperDto.getUpdatedBy());
			dto.setCreatedDate(diagnosisMapperDto.getCreatedDate());
			dto.setUpdatedDate(diagnosisMapperDto.getUpdatedDate());
			dto.setStatus(diagnosisMapperDto.getStatus());
		
			ListIterator<DiagnosisMapperDto> listGender=diagnosisMapperDto.getListOfGendersDto().listIterator();
			while(listGender.hasNext()){
				DiagnosisMapperDto dtoGender=listGender.next();
				dto.setGenderId(dtoGender.getId());
			
		}
			iDiagnosisMapperDao.saveDiagnosisMapper(dto);
		}
		return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
	} catch (Exception e) {
	logger.error("saveDiagnosisMapper failed", e);
	}
	return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response searchDiagnosisByCodeOrDescription(DiagnosisMapperDto diagnosisMapperDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iDiagnosisMapperDao.searchDiagnosisByCodeOrDescription(diagnosisMapperDto);
	}

	@Override
	public Response getListOfDiagnosisMapper(DiagnosisMapperDto diagnosisMapperDto) throws ApplicationException {
		// TODO Auto-generated method stub
		return iDiagnosisMapperDao.getListOfDiagnosisMapper(diagnosisMapperDto);
	}

	@Override
	public Response updateDiagnosisMapperStatus(DiagnosisMapperDto diagnosisMapperDto) throws ApplicationException {
		// TODO Auto-generated method stub
		return iDiagnosisMapperDao.updateDiagnosisMapperStatus(diagnosisMapperDto);
	}

	@Override
	public Response getGenderByDiagnosisId(DiagnosisMapperDto diagnosisMapperDto) throws ApplicationException {
		// TODO Auto-generated method stub
		return iDiagnosisMapperDao.getGenderByDiagnosisId(diagnosisMapperDto);
	}

	@Override
	public Response getDiagnosisByGenderId(DiagnosisMapperDto diagnosisMapperDto) throws ApplicationException {
		// TODO Auto-generated method stub
		return iDiagnosisMapperDao.getDiagnosisByGenderId(diagnosisMapperDto);
	}

	@Override
	public Response updateDiagnosisMapper(DiagnosisMapperDto diagnosisMapperDto) throws ApplicationException {
		try {
		DiagnosisMapperDto dto=new DiagnosisMapperDto();
		dto.setDiagnosisId(diagnosisMapperDto.getDiagnosisId());
		dto.setIsNotifiable(diagnosisMapperDto.getIsNotifiable());
		dto.setIsDeleted('N');
		dto.setIsCommunicable(diagnosisMapperDto.getIsCommunicable());
		dto.setIsNonCommunicable(diagnosisMapperDto.getIsNonCommunicable());
		dto.setIsMedicoLegal(diagnosisMapperDto.getIsMedicoLegal());
		dto.setStatus(diagnosisMapperDto.getStatus());
		dto.setOrganizationId(diagnosisMapperDto.getOrganizationId());
		dto.setUnitId(diagnosisMapperDto.getUnitId());
		dto.setCreatedBy(diagnosisMapperDto.getCreatedBy());
		dto.setUpdatedBy(diagnosisMapperDto.getUpdatedBy());
		dto.setCreatedDate(diagnosisMapperDto.getCreatedDate());
		dto.setUpdatedDate(diagnosisMapperDto.getUpdatedDate());
		dto.setStatus(diagnosisMapperDto.getStatus());
		if(diagnosisMapperDto.getOldDiagnosisId()!=null) {
			diagnosisMapperDto.setDiagnosisId(diagnosisMapperDto.getOldDiagnosisId());
			iDiagnosisMapperDao.updateDiagnosisMapper(diagnosisMapperDto);
		}else {
			iDiagnosisMapperDao.updateDiagnosisMapper(dto);
		}
		
		ListIterator<DiagnosisMapperDto> listGender=diagnosisMapperDto.getListOfGendersDto().listIterator();
		while(listGender.hasNext()){
			DiagnosisMapperDto dtoGender=listGender.next();
			dto.setGenderId(dtoGender.getId());
			iDiagnosisMapperDao.saveDiagnosisMapper(dto);
		}
//			iDiagnosisMapperDao.saveDiagnosisMapper(dto);
		
//	}
	//}
	return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
} catch (Exception e) {
		logger.error("updateDiagnosisMapper failed", e);
		}
return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

	@Override
	public Response getDiagnosisMapperById(DiagnosisMapperDto diagnosisMapperDto) throws ApplicationException {
		// TODO Auto-generated method stub
		return iDiagnosisMapperDao.getDiagnosisMapperById(diagnosisMapperDto);
	}

	@Override
	public Response getDiagnosisMapperCount(DiagnosisMapperDto diagnosisMapperDto) throws ApplicationException {
		// TODO Auto-generated method stub
		return iDiagnosisMapperDao.getDiagnosisMapperCount(diagnosisMapperDto);
	}

}
