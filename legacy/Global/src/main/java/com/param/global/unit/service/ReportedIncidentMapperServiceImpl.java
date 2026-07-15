package com.param.global.unit.service;

import java.util.ListIterator;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.IError;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.model.ReportedIncidentRoleMapper;
import com.param.global.model.ReportedIncidentUserMapper;
import com.param.global.nabhprocess.dto.ReportedNabhMapperDto;
import com.param.global.unit.dao.IIncidentReportingMapperDao;
import com.param.global.unit.dao.IReportedIncidentMapperDao;
import com.param.global.unit.dao.IReportedIncidentRoleMapperDao;
import com.param.global.unit.dao.IReportedIncidentUserMapperDao;
import com.param.global.unit.dto.ReportedIncidentMapperDto;
import com.param.global.unit.dto.ReportedIncidentMapperDto;
import com.param.global.unit.dto.ReportedIncidentMapperDto;
import com.param.global.unit.dto.ReportedIncidentMapperDto;
import com.param.global.unit.dto.ReportedIncidentMapperDto;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({ "rawtypes", "unused", "unchecked" })
public class ReportedIncidentMapperServiceImpl implements IReportedIncidentMapperService, ICommonConstants, IError {
	private static final Logger logger = Logger.getLogger(ReportedIncidentMapperServiceImpl.class);

	@Autowired
	IReportedIncidentMapperDao iReportedIncidentMapperDao;

	@Autowired
	IReportedIncidentRoleMapperDao iReportedIncidentRoleMapperDao;

	@Autowired
	IReportedIncidentUserMapperDao iReportedIncidentUserMapperDao;

	@Override
	public Response saveReportedIncidentMapper(ReportedIncidentMapperDto reportedIncidentMapperDto)
			throws ApplicationException {
		try {

			ReportedIncidentMapperDto dto = new ReportedIncidentMapperDto();
			dto.setResolutionDetailsTypeId(reportedIncidentMapperDto.getResolutionDetailsTypeId());
			dto.setReportedIncMpprId(reportedIncidentMapperDto.getReportedIncMpprId());
			dto.setReportedTemplateId(reportedIncidentMapperDto.getReportedTemplateId());
			dto.setReportedTemplateObj(reportedIncidentMapperDto.getReportedTemplateObj());
			dto.setParentUserId(reportedIncidentMapperDto.getParentUserId());
			dto.setIncidentReportingMapperId(reportedIncidentMapperDto.getIncidentReportingMapperId());
			dto.setParentUserId(reportedIncidentMapperDto.getParentUserId());
			dto.setIncidentReportId(reportedIncidentMapperDto.getIncidentReportId());
			dto.setIsSubmit(reportedIncidentMapperDto.getIsSubmit());
			dto.setOrganizationId(reportedIncidentMapperDto.getOrganizationId());
			dto.setUnitId(reportedIncidentMapperDto.getUnitId());
			dto.setStatus(reportedIncidentMapperDto.getStatus());
			dto.setCreatedBy(reportedIncidentMapperDto.getCreatedBy());
			dto.setCreatedDate(reportedIncidentMapperDto.getCreatedDate());
			dto.setUpdatedBy(reportedIncidentMapperDto.getUpdatedBy());
			dto.setUpdatedDate(reportedIncidentMapperDto.getUpdatedDate());

			Response insiRes = iReportedIncidentMapperDao.saveReportedIncidentMapper(dto);
			ReportedIncidentMapperDto reportedIncidentMapperDto1 = (ReportedIncidentMapperDto) insiRes.getObject();

			Integer resUpdateRoleMapper = iReportedIncidentRoleMapperDao.updateReportedIncidentRoleMapper(reportedIncidentMapperDto.getReportedIncMpprId(), reportedIncidentMapperDto.getUnitId(),
					reportedIncidentMapperDto.getOrganizationId());

			for (ReportedIncidentMapperDto roleDto : reportedIncidentMapperDto.getReportedIncidentRoleMapperList()) {
				ReportedIncidentMapperDto dtoRole = new ReportedIncidentMapperDto();
				dtoRole.setReportedIncMpprId(reportedIncidentMapperDto1.getReportedIncMpprId());
				dtoRole.setRepIncRoleMpprId(roleDto.getRepIncRoleMpprId());
				dtoRole.setRoleId(roleDto.getRoleId());
				dtoRole.setParentRepIncUserId(reportedIncidentMapperDto.getParentUserId());
				dtoRole.setReportedTemplateId(reportedIncidentMapperDto1.getReportedTemplateId());
				dtoRole.setReportedTemplateObj(reportedIncidentMapperDto1.getReportedTemplateObj());
				dtoRole.setOrganizationId(dto.getOrganizationId());
				dtoRole.setUnitId(dto.getUnitId());
				dtoRole.setStatus(dto.getStatus());
				dtoRole.setCreatedBy(dto.getCreatedBy());
				dtoRole.setCreatedDate(dto.getCreatedDate());
				dtoRole.setUpdatedBy(dto.getUpdatedBy());
				dtoRole.setUpdatedDate(dto.getUpdatedDate());

				Response response1 = iReportedIncidentRoleMapperDao.saveReportedIncidentRoleMapper(dtoRole);
				if (reportedIncidentMapperDto.getReportedIncidentUserMapperList() != null
						&& reportedIncidentMapperDto.getReportedIncidentUserMapperList().size() > 0) {

					Integer resUpdate = iReportedIncidentUserMapperDao.updateReportedIncidentUserMapper(reportedIncidentMapperDto.getReportedIncMpprId(), reportedIncidentMapperDto.getUnitId(),
							reportedIncidentMapperDto.getOrganizationId());

					for (ReportedIncidentMapperDto userDto : reportedIncidentMapperDto
							.getReportedIncidentUserMapperList()) {
						ReportedIncidentMapperDto dtoUser = new ReportedIncidentMapperDto();
						dtoUser.setRepIncUserMpprId(userDto.getRepIncUserMpprId());
						dtoUser.setReportedIncMpprId(reportedIncidentMapperDto1.getReportedIncMpprId());
						dtoUser.setRoleId(dtoRole.getRoleId());
						dtoUser.setUserId(userDto.getUserId());
						dtoUser.setParentRepIncUserId(reportedIncidentMapperDto.getParentUserId());
						dtoUser.setIsSubmit(reportedIncidentMapperDto.getIsSubmit());
						dtoUser.setReportedTemplateId(userDto.getReportedTemplateId());
						dtoUser.setReportedTemplateObj(userDto.getReportedTemplateObj());
						dtoUser.setOrganizationId(dto.getOrganizationId());
						dtoUser.setUnitId(dto.getUnitId());
						dtoUser.setStatus(dto.getStatus());
						dtoUser.setCreatedBy(dto.getCreatedBy());
						dtoUser.setCreatedDate(dto.getCreatedDate());
						dtoUser.setUpdatedBy(dto.getUpdatedBy());
						dtoUser.setUpdatedDate(dto.getUpdatedDate());
						dtoUser.setResolutionDetailsTypeId(reportedIncidentMapperDto.getResolutionDetailsTypeId());
						dtoUser.setConversationId(userDto.getConversationId());

						iReportedIncidentUserMapperDao.saveReportedIncidentUserMapper(dtoUser);
					}

//					return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
				}
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (Exception e) {
			logger.error("saveReportedIncidentMapper failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);

	}

	@Override
	public Response getReportedIncidentMapperList(ReportedIncidentMapperDto reportedIncidentMapperDto)
			throws ApplicationException {
		try {
			return iReportedIncidentUserMapperDao.getReportedIncidentMapperList(reportedIncidentMapperDto);
		} catch (Exception e) {
			logger.error("getReportedIncidentMapperList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);

	}

	@Override
	public Response getReportedIncidentMapperById(ReportedIncidentMapperDto reportedIncidentMapperDto)
			throws ApplicationException {
		try {
			return iReportedIncidentUserMapperDao.getReportedIncidentMapperById(reportedIncidentMapperDto);
		} catch (Exception e) {
			logger.error("getReportedIncidentMapperById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);

	}

	@Override
	public Response getTemplateByReportedIncidentId(ReportedIncidentMapperDto reportedIncidentMapperDto)
			throws ApplicationException {
		try {
			return iReportedIncidentUserMapperDao.getTemplateByReportedIncidentId(reportedIncidentMapperDto);
		} catch (Exception e) {
			logger.error("getTemplateByReportedIncidentId failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);

	}

	public Response submitReportedIncidentMapper(ReportedIncidentMapperDto reportedIncidentMapperDto)
			throws ApplicationException {
		try {

			ReportedIncidentMapperDto dto = new ReportedIncidentMapperDto();
			dto.setReportedIncMpprId(reportedIncidentMapperDto.getReportedIncMpprId());
			dto.setIsSubmit(reportedIncidentMapperDto.getIsSubmit());
			dto.setResolutionDetailsTypeId(reportedIncidentMapperDto.getResolutionDetailsTypeId());
			Response insiRes = iReportedIncidentMapperDao.submitReportedIncidentMapper(dto);
			ReportedIncidentMapperDto reportedIncidentMapperDto1 = (ReportedIncidentMapperDto) insiRes.getObject();
				if (reportedIncidentMapperDto.getReportedIncidentUserMapperList() != null
						&& reportedIncidentMapperDto.getReportedIncidentUserMapperList().size() > 0) {

					for (ReportedIncidentMapperDto userDto : reportedIncidentMapperDto
							.getReportedIncidentUserMapperList()) {
						ReportedIncidentMapperDto dtoUser = new ReportedIncidentMapperDto();
						dtoUser.setRepIncUserMpprId(userDto.getRepIncUserMpprId());
						dtoUser.setIsSubmit(userDto.getIsSubmit());
						dtoUser.setResolutionDetailsTypeId(userDto.getResolutionDetailsTypeId());
						iReportedIncidentUserMapperDao.submitReportedIncidentUserMapper(dtoUser);
					}

					return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
				}
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (Exception e) {
			logger.error("submitReportedIncidentMapper failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);

	}

	@Override
	public Response getTemplateByReportedNabhId(ReportedNabhMapperDto reportedNabhMapperDto)
			throws ApplicationException {
		try {
			return iReportedIncidentUserMapperDao.getTemplateByReportedNabhId(reportedNabhMapperDto);
		} catch (Exception e) {
			logger.error("getTemplateByReportedNabhId failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);

	}

	@Override
	public Response getReportedNabhMapperById(ReportedNabhMapperDto reportedNabhMapperDto)
			throws ApplicationException {
		try {
			return iReportedIncidentUserMapperDao.getReportedNabhMapperById(reportedNabhMapperDto);
		} catch (Exception e) {
			logger.error("getReportedNabhMapperById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);

	}


}
