package com.param.global.unit.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.IError;
import com.param.global.common.Response;
import com.param.global.unit.dao.IIncidentReportMasterDao;
import com.param.global.unit.dao.IIncidentReportTemplateMasterDao;
import com.param.global.unit.dto.IncidentReportMasterDto;
import com.param.global.unit.dto.IncidentReportRoleMapperDto;
import com.param.global.unit.dto.IncidentReportTemplateMapperDto;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
public class IncidentReportMasterServiceImpl implements IIncidentReportMasterService, ICommonConstants, IError {
	final static Logger logger = Logger.getLogger(IncidentReportMasterServiceImpl.class);


	@Autowired
	IIncidentReportMasterDao iIncidentReportMasterDao;
	
	@Autowired
	IIncidentReportTemplateMasterDao iIncidentReportTemplateMasterDao;


	@Override
	public Response saveIncidentReportMaster(IncidentReportMasterDto incidentReportMasterDto)
			throws ApplicationException {
		try {
			Response checkRes1 = iIncidentReportMasterDao
					.checkIncidentReportMasterAvailableByName(incidentReportMasterDto);
			IncidentReportMasterDto newdto = new IncidentReportMasterDto();
			IncidentReportRoleMapperDto newdto2 = new IncidentReportRoleMapperDto();

			IncidentReportMasterDto dto = new IncidentReportMasterDto();
			dto.setIncidentReportCode(incidentReportMasterDto.getIncidentReportCode());
			dto.setIncidentReportDesc(incidentReportMasterDto.getIncidentReportDesc());
			dto.setOrganizationId(incidentReportMasterDto.getOrganizationId());
			dto.setUnitId(incidentReportMasterDto.getUnitId());
			dto.setStatus(incidentReportMasterDto.getStatus());
			dto.setCreatedBy(incidentReportMasterDto.getCreatedBy());
			dto.setUpdatedBy(incidentReportMasterDto.getUpdatedBy());
			dto.setUpdatedDate(incidentReportMasterDto.getUpdatedDate());
			dto.setCreatedDate(incidentReportMasterDto.getCreatedDate());
			Response insiRes = iIncidentReportMasterDao.saveIncidentReportMaster(dto);

			newdto = (IncidentReportMasterDto) insiRes.getObject();
			ListIterator<IncidentReportRoleMapperDto> itr = incidentReportMasterDto.getIncidentReportRoleMapperList()
					.listIterator();
			while (itr.hasNext()) {
				IncidentReportRoleMapperDto dtoRoleList = itr.next();
				dtoRoleList.setRoleId(dtoRoleList.getRoleId());
				dtoRoleList.setIncidentReportId(newdto.getIncidentReportId());
				dtoRoleList.setOrganizationId(incidentReportMasterDto.getOrganizationId());
				dtoRoleList.setUnitId(incidentReportMasterDto.getUnitId());
				dtoRoleList.setStatus(incidentReportMasterDto.getStatus());
				dtoRoleList.setCreatedBy(incidentReportMasterDto.getCreatedBy());
				dtoRoleList.setUpdatedBy(incidentReportMasterDto.getUpdatedBy());
				dtoRoleList.setUpdatedDate(incidentReportMasterDto.getUpdatedDate());
				dtoRoleList.setCreatedDate(incidentReportMasterDto.getCreatedDate());

				Response insiRoleRes = iIncidentReportMasterDao.saveIncidentReportRoleMapper(dtoRoleList);
				newdto2 = (IncidentReportRoleMapperDto) insiRoleRes.getObject();

				ListIterator<IncidentReportTemplateMapperDto> itr2 = incidentReportMasterDto
						.getIncidentReportTemplateMapperList().listIterator();
				while (itr2.hasNext()) {
					IncidentReportTemplateMapperDto dtoTemplateList = itr2.next();
					dtoTemplateList.setRoleId(newdto2.getRoleId());
					dtoTemplateList.setIncidentReportId(newdto.getIncidentReportId());
					dtoTemplateList.setTemplateId(dtoTemplateList.getTemplateId());
					dtoTemplateList.setOrganizationId(incidentReportMasterDto.getOrganizationId());
					dtoTemplateList.setUnitId(incidentReportMasterDto.getUnitId());
					dtoTemplateList.setStatus(incidentReportMasterDto.getStatus());
					dtoTemplateList.setCreatedBy(incidentReportMasterDto.getCreatedBy());
					dtoTemplateList.setUpdatedBy(incidentReportMasterDto.getUpdatedBy());
					dtoTemplateList.setUpdatedDate(incidentReportMasterDto.getUpdatedDate());
					dtoTemplateList.setCreatedDate(incidentReportMasterDto.getCreatedDate());

					iIncidentReportMasterDao.saveIncidentReportTemplateMapper(dtoTemplateList);
					// }
				}
				// }
			}

			// }

			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (Exception e) {
		logger.error("saveIncidentReportMaster failed", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}

	@Override
	public Response updateIncidentReportMaster(IncidentReportMasterDto incidentReportMasterDto)
			throws ApplicationException {
		try {
			// Response res =
			// iIncidentReportMasterDao.checkIncidentReportMasterAvailableByName(incidentReportMasterDto);
			// if(res.getListObject() != null && res.getListObject().size() > 0){
			//
			// return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			//
			// }else{
//			IncidentReportMasterDto newdto = new IncidentReportMasterDto();
//			IncidentReportRoleMapperDto newdto2 = new IncidentReportRoleMapperDto();

			// IncidentReportMasterDto dto=new IncidentReportMasterDto();

			incidentReportMasterDto.setIncidentReportId(incidentReportMasterDto.getIncidentReportId());
			incidentReportMasterDto.setIncidentReportCode(incidentReportMasterDto.getIncidentReportCode());
			incidentReportMasterDto.setIncidentReportDesc(incidentReportMasterDto.getIncidentReportDesc());
			incidentReportMasterDto.setOrganizationId(incidentReportMasterDto.getOrganizationId());
			incidentReportMasterDto.setUnitId(incidentReportMasterDto.getUnitId());
			incidentReportMasterDto.setStatus(incidentReportMasterDto.getStatus());
			incidentReportMasterDto.setUpdatedBy(incidentReportMasterDto.getUpdatedBy());
			incidentReportMasterDto.setUpdatedDate(incidentReportMasterDto.getUpdatedDate());
			Response insiRes = iIncidentReportMasterDao.updateIncidentReportMaster(incidentReportMasterDto);

//			newdto = (IncidentReportMasterDto) insiRes.getObject();
			ListIterator<IncidentReportRoleMapperDto> itr = incidentReportMasterDto.getIncidentReportRoleMapperList()
					.listIterator();
			while (itr.hasNext()) {

				// Integer roleId = (Integer) itr.next();
				IncidentReportRoleMapperDto dtoRoleList = itr.next();
				dtoRoleList.setRoleId(dtoRoleList.getRoleId());
				dtoRoleList.setIncidentReportId(incidentReportMasterDto.getIncidentReportId());
				dtoRoleList.setOrganizationId(incidentReportMasterDto.getOrganizationId());
				dtoRoleList.setUnitId(incidentReportMasterDto.getUnitId());
				dtoRoleList.setStatus(incidentReportMasterDto.getStatus());
				dtoRoleList.setUpdatedBy(incidentReportMasterDto.getUpdatedBy());
				dtoRoleList.setUpdatedDate(incidentReportMasterDto.getUpdatedDate());

				Response insiRoleRes = iIncidentReportMasterDao.updateIncidentReportRoleMapper(dtoRoleList);
//				newdto2 = (IncidentReportRoleMapperDto) insiRoleRes.getObject();

				ListIterator<IncidentReportTemplateMapperDto> itr2 = incidentReportMasterDto
						.getIncidentReportTemplateMapperList().listIterator();
				/*while (itr2.hasNext()) {
					IncidentReportTemplateMapperDto dtoTemplateList = itr2.next();
					dtoTemplateList.setRoleId(newdto2.getRoleId());
					dtoTemplateList.setIncidentReportId(incidentReportMasterDto.getIncidentReportId());   
					dtoTemplateList.setTemplateId(dtoTemplateList.getTemplateId());
					dtoTemplateList.setOrganizationId(incidentReportMasterDto.getOrganizationId());
					dtoTemplateList.setUnitId(incidentReportMasterDto.getUnitId());
					dtoTemplateList.setStatus(incidentReportMasterDto.getStatus());
					dtoTemplateList.setUpdatedBy(incidentReportMasterDto.getUpdatedBy());
					dtoTemplateList.setUpdatedDate(incidentReportMasterDto.getUpdatedDate());            

					iIncidentReportMasterDao.updateIncidentReportTemplateMapper(dtoTemplateList);
				}*/
			}
			if (incidentReportMasterDto != null) {
				Response insiTempRes = iIncidentReportTemplateMasterDao
						.updateIncidentReportTemplateMapper(incidentReportMasterDto);
			} else {
				return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
			}

			// }

			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (Exception e) {
		logger.error("updateIncidentReportMaster failed", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}

	@Override
	public Response getIncidentReportMasterById(IncidentReportMasterDto incidentReportMasterDto)
			throws ApplicationException {
		try {

			return iIncidentReportMasterDao.getIncidentReportMasterById(incidentReportMasterDto);

		} catch (Exception e) {
		logger.error("getIncidentReportMasterById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getIncidentReportMasterList(IncidentReportMasterDto incidentReportMasterDto)
			throws ApplicationException {
		try {

			return iIncidentReportMasterDao.getIncidentReportMasterList(incidentReportMasterDto);

		} catch (Exception e) {
		logger.error("getIncidentReportMasterList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getTemplateByIncidentReportId(IncidentReportMasterDto incidentReportMasterDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response getRoleByIncidentReportId(IncidentReportMasterDto incidentReportMasterDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response updateIncidentReportMasterStatus(IncidentReportMasterDto incidentReportMasterDto)
			throws ApplicationException {
		try {
			Response res = iIncidentReportMasterDao.updateIncidentReportMasterStatus(incidentReportMasterDto);
			if (incidentReportMasterDto.getStatus() == 'I') {
				Response res1 = iIncidentReportMasterDao.updateIncidentReportRoleMasterStatus(incidentReportMasterDto);

				iIncidentReportMasterDao.updateIncidentReportTemplateMasterStatus(incidentReportMasterDto);
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);

		} catch (Exception e) {
		logger.error("updateIncidentReportMasterStatus failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getActiveIncidentReportMasterList(IncidentReportMasterDto incidentReportMasterDto)
			throws ApplicationException {
		try {

			return iIncidentReportMasterDao.getActiveIncidentReportMasterList(incidentReportMasterDto);

		} catch (Exception e) {
		logger.error("getActiveIncidentReportMasterList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getIncidentReportMasterCount(IncidentReportMasterDto incidentReportMasterDto)
			throws ApplicationException {
		try {

			return iIncidentReportMasterDao.getIncidentReportMasterCount(incidentReportMasterDto);

		} catch (Exception e) {
		logger.error("getIncidentReportMasterCount failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
}
