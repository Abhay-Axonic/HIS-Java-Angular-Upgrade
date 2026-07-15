package com.param.global.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.ICustomHistoryConfigDetailsDao;
import com.param.global.dto.CustomHistoryConfigurationDetailsDto;
import com.param.global.dto.CustomHistoryConfigurationDto;
import com.param.global.model.CustomHistoryConfigurationDetails;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class CustomHistoryConfigDetailsServiceImpl implements ICustomHistoryConfigDetailsService, ICommonConstants{
	final static Logger logger = Logger.getLogger(CustomHistoryConfigDetailsServiceImpl.class);

	@Autowired
	private ICustomHistoryConfigDetailsDao iCustomHistoryConfigDetailsDao;

	@Override
	public Response getCustomHistoryConfigurationList(
			CustomHistoryConfigurationDetailsDto customHistoryConfigurationDetailsDto) throws ApplicationException {
		try {
			Response res =  iCustomHistoryConfigDetailsDao.getCustomHistoryConfigurationList(customHistoryConfigurationDetailsDto);
			List<CustomHistoryConfigurationDetailsDto> customHistoryConfigurationMstr = new ArrayList<CustomHistoryConfigurationDetailsDto>();
			if(res!=null&& res.getListObject()!=null && res.getListObject().size()>0) {
				List<CustomHistoryConfigurationDto> customHistoryConfigurationList = res.getListObject();
				for(CustomHistoryConfigurationDto dto :customHistoryConfigurationList) {
					CustomHistoryConfigurationDetailsDto  dtlsDto = new CustomHistoryConfigurationDetailsDto();
					dtlsDto.setSpecialityId(dto.getSpecialityId());
					dtlsDto.setCategory(dto.getCategory());
					dtlsDto.setRemark("");
					dtlsDto.setPatientId(customHistoryConfigurationDetailsDto.getPatientId());
					dtlsDto.setVisitTypeId(customHistoryConfigurationDetailsDto.getVisitTypeId());
					dtlsDto.setEncounterId(customHistoryConfigurationDetailsDto.getEncounterId());
					dtlsDto.setAdmissionId(customHistoryConfigurationDetailsDto.getAdmissionId());
					dtlsDto.setStatus('A');
					dtlsDto.setOrganizationId(customHistoryConfigurationDetailsDto.getOrganizationId());
					dtlsDto.setUnitId(customHistoryConfigurationDetailsDto.getUnitId());
					dtlsDto.setCreatedBy(customHistoryConfigurationDetailsDto.getCreatedBy());
					dtlsDto.setUpdatedBy(customHistoryConfigurationDetailsDto.getUpdatedBy());
					dtlsDto.setCreatedDate(customHistoryConfigurationDetailsDto.getCreatedDate());
					dtlsDto.setUpdatedDate(customHistoryConfigurationDetailsDto.getUpdatedDate());
					dtlsDto.setCustomHistoryConfigurationMstrId(dto.getId());
					Response detsilSaveRes =  iCustomHistoryConfigDetailsDao.saveCustomHistoryConfigurationDetails(dtlsDto);
					if(detsilSaveRes!=null && detsilSaveRes.getObject()!=null) {
						CustomHistoryConfigurationDetails mstr = (CustomHistoryConfigurationDetails)detsilSaveRes.getObject();
						CustomHistoryConfigurationDetailsDto newDto = new CustomHistoryConfigurationDetailsDto();
						newDto.setSpecialityId(mstr.getSpecialityId());
						newDto.setCategory(mstr.getCategory());
						newDto.setRemark(mstr.getRemark());
						newDto.setPatientId(mstr.getPatientId());
						newDto.setVisitTypeId(mstr.getVisitTypeId());
						newDto.setEncounterId(mstr.getEncounterId());
						newDto.setAdmissionId(mstr.getAdmissionId());
						newDto.setStatus(mstr.getStatus());
						newDto.setOrganizationId(mstr.getOrganizationId());
						newDto.setUnitId(mstr.getUnitId());
						newDto.setCreatedBy(mstr.getCreatedBy());
						newDto.setUpdatedBy(mstr.getUpdatedBy());
						newDto.setCreatedDate(GlobalCommonDateUtils.getStringDate(mstr.getCreatedDate(), "dd-M-yyyy HH:ss:mm"));
						newDto.setUpdatedDate(GlobalCommonDateUtils.getStringDate(mstr.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));
						newDto.setSpecialityName(dto.getSpecialityName());
						newDto.setCustomHistoryConfigDetailsId(mstr.getCustomHistoryConfigDetailsId());
						newDto.setCustomHistoryConfigurationMstrId(mstr.getCustomHistoryConfigurationMstrId());
						customHistoryConfigurationMstr.add(newDto);
					}
				}
			}
			
			
			return new Response(SUCCESS, SUCCESS_CODE, null, customHistoryConfigurationMstr, null);
		} catch (Exception e) {
		logger.error("getCustomHistoryConfigurationList failed", e);
		}
		return new Response(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	@Transactional
	public Response updateCustomHistoryDetails(CustomHistoryConfigurationDetailsDto dto) throws ApplicationException {
		try {
			return iCustomHistoryConfigDetailsDao.updateCustomHistoryDetails(dto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	}

	@Override
	@Transactional
	public Response deleteCustomHistoryDetailsRecord(CustomHistoryConfigurationDetailsDto dto) throws ApplicationException {
		try {
			return iCustomHistoryConfigDetailsDao.deleteCustomHistoryDetailsRecord(dto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	}

	@Override
	@Transactional
	public Response getPatientCustomHistoryDetails(CustomHistoryConfigurationDetailsDto dto)
			throws ApplicationException {
		try {
			return iCustomHistoryConfigDetailsDao.getPatientCustomHistoryDetails(dto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	}
}
