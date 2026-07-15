package com.param.global.nabhprocess.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.IError;
import com.param.global.common.Response;
import com.param.global.nabhprocess.dao.IReportedNabhMapperDao;
import com.param.global.nabhprocess.dto.ReportedNabhMapperDto;
import com.param.global.unit.dao.IReportedIncidentMapperDao;
import com.param.global.unit.dao.IReportedIncidentRoleMapperDao;
import com.param.global.unit.dao.IReportedIncidentUserMapperDao;
import com.param.global.unit.dao.ReportedIncidentUserMapperDaoImpl;
import com.param.global.unit.dto.ReportedIncidentMapperDto;
import com.param.global.unit.service.IReportedIncidentMapperService;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({ "rawtypes", "unused", "unchecked" })
@Transactional
public class ReportedNabhMapperServiceImpl implements IReportedNabhMapperService, ICommonConstants, IError{
	final static Logger logger = Logger.getLogger(ReportedNabhMapperServiceImpl.class);

	@Autowired
	IReportedNabhMapperDao iReportedNabhMapperDao;

	@Autowired
	IReportedIncidentRoleMapperDao iReportedIncidentRoleMapperDao;

	@Autowired
	IReportedIncidentUserMapperDao iReportedIncidentUserMapperDao;

	@Override
	public Response saveReportedNabhMapper(ReportedNabhMapperDto reportedNabhMapperDto)
			throws ApplicationException {
		try {

			ReportedNabhMapperDto dto = new ReportedNabhMapperDto();
			dto.setResolutionDetailsTypeId(reportedNabhMapperDto.getResolutionDetailsTypeId());
			dto.setReportedNabhMpprId(reportedNabhMapperDto.getReportedNabhMpprId());
			dto.setReportedTemplateId(reportedNabhMapperDto.getReportedTemplateId());
			dto.setReportedTemplateObj(reportedNabhMapperDto.getReportedTemplateObj());
			dto.setParentUserId(reportedNabhMapperDto.getParentUserId());
			dto.setNabhReportingMapperId(reportedNabhMapperDto.getNabhReportingMapperId());
			dto.setParentUserId(reportedNabhMapperDto.getParentUserId());
			dto.setNabhProcessId(reportedNabhMapperDto.getNabhProcessId());
			dto.setIsSubmit(reportedNabhMapperDto.getIsSubmit());
			dto.setOrganizationId(reportedNabhMapperDto.getOrganizationId());
			dto.setUnitId(reportedNabhMapperDto.getUnitId());
			dto.setStatus(reportedNabhMapperDto.getStatus());
			dto.setCreatedBy(reportedNabhMapperDto.getCreatedBy());
			dto.setCreatedDate(reportedNabhMapperDto.getCreatedDate());
			dto.setUpdatedBy(reportedNabhMapperDto.getUpdatedBy());
			dto.setUpdatedDate(reportedNabhMapperDto.getUpdatedDate());

			Response insiRes = iReportedNabhMapperDao.saveReportedNabhMapper(dto);
			ReportedNabhMapperDto reportedNabhMapperDto1 = (ReportedNabhMapperDto) insiRes.getObject();
            ReportedIncidentMapperDto dtoRole1 = new ReportedIncidentMapperDto();
            dtoRole1.setReportedNabhMpprId(reportedNabhMapperDto1.getReportedNabhMpprId());
            dtoRole1.setOrganizationId(dto.getOrganizationId());
            dtoRole1.setUnitId(dto.getUnitId());
            iReportedIncidentRoleMapperDao.updateReportedNabhRoleMapper(dtoRole1); 
            ReportedIncidentMapperDto dtoUser1 = new ReportedIncidentMapperDto();
            dtoUser1.setReportedNabhMpprId(reportedNabhMapperDto1.getReportedNabhMpprId());
            dtoUser1.setOrganizationId(dto.getOrganizationId());
            dtoUser1.setUnitId(dto.getUnitId());
            iReportedIncidentUserMapperDao.updateReportedNabhUserMapper(dtoUser1);
			for (ReportedIncidentMapperDto roleDto : reportedNabhMapperDto.getReportedIncidentRoleMapperList()) {
				ReportedIncidentMapperDto dtoRole = new ReportedIncidentMapperDto();
				dtoRole.setReportedNabhMpprId(reportedNabhMapperDto1.getReportedNabhMpprId());
				dtoRole.setRepIncRoleMpprId(roleDto.getRepIncRoleMpprId());
				dtoRole.setRoleId(roleDto.getRoleId());
				dtoRole.setParentRepIncUserId(reportedNabhMapperDto.getParentUserId());
				dtoRole.setReportedTemplateId(reportedNabhMapperDto1.getReportedTemplateId());
				dtoRole.setReportedTemplateObj(reportedNabhMapperDto1.getReportedTemplateObj());
				dtoRole.setOrganizationId(dto.getOrganizationId());
				dtoRole.setUnitId(dto.getUnitId());
				dtoRole.setStatus(dto.getStatus());
				dtoRole.setCreatedBy(dto.getCreatedBy());
				dtoRole.setCreatedDate(dto.getCreatedDate());
				dtoRole.setUpdatedBy(dto.getUpdatedBy());
				dtoRole.setUpdatedDate(dto.getUpdatedDate());

				Response response1 = iReportedIncidentRoleMapperDao.saveReportedNabhRoleMapper(dtoRole);  
				if (reportedNabhMapperDto.getReportedIncidentUserMapperList() != null
						&& reportedNabhMapperDto.getReportedIncidentUserMapperList().size() > 0) {

					for (ReportedIncidentMapperDto userDto : reportedNabhMapperDto
							.getReportedIncidentUserMapperList()) {
						ReportedIncidentMapperDto dtoUser = new ReportedIncidentMapperDto();
						dtoUser.setRepIncUserMpprId(userDto.getRepIncUserMpprId());
						dtoUser.setReportedNabhMpprId(reportedNabhMapperDto1.getReportedNabhMpprId());
						dtoUser.setRoleId(dtoRole.getRoleId());
						dtoUser.setUserId(userDto.getUserId());
						dtoUser.setParentRepIncUserId(reportedNabhMapperDto.getParentUserId());
						dtoUser.setIsSubmit(reportedNabhMapperDto.getIsSubmit());
						dtoUser.setReportedTemplateId(userDto.getReportedTemplateId());
						dtoUser.setReportedTemplateObj(userDto.getReportedTemplateObj());
						dtoUser.setOrganizationId(dto.getOrganizationId());
						dtoUser.setUnitId(dto.getUnitId());
						dtoUser.setStatus(dto.getStatus());
						dtoUser.setCreatedBy(dto.getCreatedBy());
						dtoUser.setCreatedDate(dto.getCreatedDate());
						dtoUser.setUpdatedBy(dto.getUpdatedBy());
						dtoUser.setUpdatedDate(dto.getUpdatedDate());
						dtoUser.setResolutionDetailsTypeId(reportedNabhMapperDto.getResolutionDetailsTypeId());
						dtoUser.setConversationId(userDto.getConversationId());

						iReportedIncidentUserMapperDao.saveReportedNabhUserMapper(dtoUser);
					}

				}
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}

	}

	@Override
	public Response getReportedNabhMapperList(ReportedNabhMapperDto reportedNabhMapperDto)
			throws ApplicationException {
		try {
			return iReportedIncidentUserMapperDao.getReportedNabhMapperList(reportedNabhMapperDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}

	}

	public Response submitReportedNabhMapper(ReportedNabhMapperDto reportedNabhMapperDto)
			throws ApplicationException {
		try {

			ReportedNabhMapperDto dto = new ReportedNabhMapperDto();
			dto.setReportedNabhMpprId(reportedNabhMapperDto.getReportedNabhMpprId());
			dto.setIsSubmit(reportedNabhMapperDto.getIsSubmit());
			dto.setResolutionDetailsTypeId(reportedNabhMapperDto.getResolutionDetailsTypeId());
			Response insiRes = iReportedNabhMapperDao.submitReportedNabhMapper(dto);
			ReportedNabhMapperDto reportedNabhMapperDto1 = (ReportedNabhMapperDto) insiRes.getObject();
				if (reportedNabhMapperDto.getReportedIncidentUserMapperList() != null
						&& reportedNabhMapperDto.getReportedIncidentUserMapperList().size() > 0) {

					for (ReportedIncidentMapperDto userDto : reportedNabhMapperDto
							.getReportedIncidentUserMapperList()) {
						ReportedIncidentMapperDto dtoUser = new ReportedIncidentMapperDto();
						dtoUser.setRepIncUserMpprId(userDto.getRepIncUserMpprId());
						dtoUser.setIsSubmit(userDto.getIsSubmit());
						dtoUser.setResolutionDetailsTypeId(userDto.getResolutionDetailsTypeId());
						iReportedIncidentUserMapperDao.submitReportedNabhUserMapper(dtoUser);
					}

					return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
				}
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}

	}
}
