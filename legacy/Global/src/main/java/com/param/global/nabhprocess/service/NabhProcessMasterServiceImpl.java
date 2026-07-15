package com.param.global.nabhprocess.service;

import java.util.ListIterator;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.IError;
import com.param.global.common.Response;
import com.param.global.nabhprocess.controller.NabhProcessMasterController;
import com.param.global.nabhprocess.dao.INabhProcessMasterDao;
import com.param.global.nabhprocess.dto.NabhProcessMasterDto;
import com.param.global.nabhprocess.dto.NabhProcessRoleMapperDto;
import com.param.global.unit.dao.IIncidentReportMasterDao;
import com.param.global.unit.dto.IncidentReportMasterDto;
import com.param.global.unit.dto.IncidentReportRoleMapperDto;
import com.param.global.unit.dto.IncidentReportTemplateMapperDto;
import com.param.global.nabhprocess.dto.NabhProcessRoleMapperDto;
import com.param.global.nabhprocess.dto.NabhProcessTemplateMapperDto;
import com.param.global.unit.service.IIncidentReportMasterService;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
@Transactional
public class NabhProcessMasterServiceImpl implements INabhProcessMasterService, ICommonConstants, IError {
	final static Logger logger = Logger.getLogger(NabhProcessMasterServiceImpl.class);



	@Autowired
	INabhProcessMasterDao iNabhProcessMasterDao;
	
	@Autowired
	IIncidentReportMasterDao iIncidentReportMasterDao;


	@Override
	public Response saveNabhProcessMaster(NabhProcessMasterDto nabhProcessMasterDto)
			throws ApplicationException {
		try {
//			Response checkRes1 = iNabhProcessMasterDao
//					.checknabhProcessMasterAvailableByName(nabhProcessMasterDto);
			Response insiRes = new Response<>(null, null, null, null, null);
			NabhProcessMasterDto newdto = new NabhProcessMasterDto();
			IncidentReportRoleMapperDto newdto2 = new IncidentReportRoleMapperDto();
			if(nabhProcessMasterDto.getNabhProcessId()==null) {
				
			NabhProcessMasterDto dto = new NabhProcessMasterDto();
			dto.setNabhProcessCode(nabhProcessMasterDto.getNabhProcessCode());
			dto.setNabhProcessDesc(nabhProcessMasterDto.getNabhProcessDesc()); 
			dto.setOrganizationId(nabhProcessMasterDto.getOrganizationId());
			dto.setIsCapaOff(nabhProcessMasterDto.getIsCapaOff());
			dto.setUnitId(nabhProcessMasterDto.getUnitId());
			dto.setStatus(nabhProcessMasterDto.getStatus());
			dto.setCreatedBy(nabhProcessMasterDto.getCreatedBy());
			dto.setUpdatedBy(nabhProcessMasterDto.getUpdatedBy());
			dto.setUpdatedDate(nabhProcessMasterDto.getUpdatedDate());
			dto.setCreatedDate(nabhProcessMasterDto.getCreatedDate());
		    insiRes = iNabhProcessMasterDao.saveNabhProcessMaster(dto);
		    insiRes.setMessage("Saved Successfully");
			} 
			
			else {
				NabhProcessMasterDto dto = new NabhProcessMasterDto();
				dto.setNabhProcessId(nabhProcessMasterDto.getNabhProcessId());
				dto.setNabhProcessCode(nabhProcessMasterDto.getNabhProcessCode());
				dto.setNabhProcessDesc(nabhProcessMasterDto.getNabhProcessDesc()); 
				dto.setOrganizationId(nabhProcessMasterDto.getOrganizationId());
				dto.setUnitId(nabhProcessMasterDto.getUnitId());
				dto.setIsCapaOff(nabhProcessMasterDto.getIsCapaOff());
				dto.setStatus(nabhProcessMasterDto.getStatus());
				dto.setCreatedBy(nabhProcessMasterDto.getCreatedBy());
				dto.setUpdatedBy(nabhProcessMasterDto.getUpdatedBy());
				dto.setUpdatedDate(nabhProcessMasterDto.getUpdatedDate());
				dto.setCreatedDate(nabhProcessMasterDto.getCreatedDate());
				 insiRes = iNabhProcessMasterDao.updateNabhProcessMaster(dto);
				 insiRes.setMessage("Updated Successfully");
			}
			newdto = (NabhProcessMasterDto) insiRes.getObject();
			ListIterator<IncidentReportRoleMapperDto> itr = nabhProcessMasterDto.getIncidentReportRoleMapperList()
					.listIterator();
			while (itr.hasNext()) {
				IncidentReportRoleMapperDto dtoRoleList = itr.next();
				dtoRoleList.setRoleId(dtoRoleList.getRoleId());
				dtoRoleList.setNabhProcessId(newdto.getNabhProcessId()); 
				dtoRoleList.setOrganizationId(nabhProcessMasterDto.getOrganizationId());
				dtoRoleList.setUnitId(nabhProcessMasterDto.getUnitId());
				dtoRoleList.setStatus(nabhProcessMasterDto.getStatus());
				dtoRoleList.setCreatedBy(nabhProcessMasterDto.getCreatedBy());
				dtoRoleList.setUpdatedBy(nabhProcessMasterDto.getUpdatedBy());
				dtoRoleList.setUpdatedDate(nabhProcessMasterDto.getUpdatedDate());
				dtoRoleList.setCreatedDate(nabhProcessMasterDto.getCreatedDate());

				Response insiRoleRes = iIncidentReportMasterDao.saveIncidentReportRoleMapper(dtoRoleList);
				newdto2 = (IncidentReportRoleMapperDto) insiRoleRes.getObject();

				ListIterator<IncidentReportTemplateMapperDto> itr2 = nabhProcessMasterDto
						.getIncidentReportTemplateMapperList().listIterator();
				while (itr2.hasNext()) {
					IncidentReportTemplateMapperDto dtoTemplateList = itr2.next();
					dtoTemplateList.setRoleId(newdto2.getRoleId());
					dtoTemplateList.setNabhProcessId(newdto.getNabhProcessId());
					dtoTemplateList.setTemplateId(dtoTemplateList.getTemplateId());
					dtoTemplateList.setOrganizationId(nabhProcessMasterDto.getOrganizationId());
					dtoTemplateList.setUnitId(nabhProcessMasterDto.getUnitId());
					dtoTemplateList.setStatus(nabhProcessMasterDto.getStatus());
					dtoTemplateList.setCreatedBy(nabhProcessMasterDto.getCreatedBy());
					dtoTemplateList.setUpdatedBy(nabhProcessMasterDto.getUpdatedBy());
					dtoTemplateList.setUpdatedDate(nabhProcessMasterDto.getUpdatedDate());
					dtoTemplateList.setCreatedDate(nabhProcessMasterDto.getCreatedDate());

					iIncidentReportMasterDao.saveIncidentReportTemplateMapper(dtoTemplateList);
				}
			}
			return insiRes;
			
			
			
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
		}
	}


	@Override
	public Response getNabhProcessMasterById(NabhProcessMasterDto nabhProcessMasterDto)
			throws ApplicationException {
		try {

			//return iNabhProcessMasterDao.getNabhProcessMasterById(nabhProcessMasterDto);

		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getNabhProcessMasterList(NabhProcessMasterDto nabhProcessMasterDto)
			throws ApplicationException {
		try {

			return iNabhProcessMasterDao.getNabhProcessMasterList(nabhProcessMasterDto);

		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	public Response updateNabhProcessMasterStatus(NabhProcessMasterDto nabhProcessMasterDto)
			throws ApplicationException {
		try {
			Response res = iNabhProcessMasterDao.updateNabhProcessMasterStatus(nabhProcessMasterDto);
			if (nabhProcessMasterDto.getStatus() == 'I') {
				Response res1 = iNabhProcessMasterDao.updateNabhProcessRoleMasterStatus(nabhProcessMasterDto);

				iNabhProcessMasterDao.updateNabhProcessMasterStatus(nabhProcessMasterDto);
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);

		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	public Response getActiveNabhProcessMasterList(NabhProcessMasterDto nabhProcessMasterDto)
			throws ApplicationException {
		try {

			return iNabhProcessMasterDao.getActiveNabhProcessMasterList(nabhProcessMasterDto);

		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}




}
