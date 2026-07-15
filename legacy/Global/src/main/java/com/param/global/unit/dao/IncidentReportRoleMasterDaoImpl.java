package com.param.global.unit.dao;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.model.IncidentReportMaster;
import com.param.global.model.IncidentReportRoleMapper;
import com.param.global.unit.dto.IncidentReportRoleMapperDto;

import in.param.common.dao.GenericDao;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings({ "rawtypes", "unchecked" })
@Repository
public class IncidentReportRoleMasterDaoImpl extends GenericDao<IncidentReportRoleMapper, Integer>
		implements IIncidentReportRoleMasterDao, ICommonConstants {
	final static Logger logger = Logger.getLogger(IncidentReportRoleMasterDaoImpl.class);


	public IncidentReportRoleMasterDaoImpl() {
		super(IncidentReportRoleMapper.class);
	}

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Response updateIncidentReportRoleMapper(IncidentReportRoleMapperDto dtoRoleList) {
		try {
			if (dtoRoleList.getIncidentReportRoleId() != null) {
				IncidentReportRoleMapper incidentReportMaster = findById(dtoRoleList.getIncidentReportRoleId());
				incidentReportMaster.setRoleId(dtoRoleList.getRoleId());
				incidentReportMaster.setIncidentReportId(dtoRoleList.getIncidentReportId());
				incidentReportMaster.setOrganizationId(dtoRoleList.getOrganizationId());
				incidentReportMaster.setUnitId(dtoRoleList.getUnitId());
				incidentReportMaster.setStatus(dtoRoleList.getStatus());
				incidentReportMaster.setCreatedBy(dtoRoleList.getCreatedBy());
				incidentReportMaster.setCreatedDate(
						GlobalCommonDateUtils.getDate(dtoRoleList.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
				incidentReportMaster.setUpdatedBy(dtoRoleList.getUpdatedBy());
				incidentReportMaster.setUpdatedDate(
						GlobalCommonDateUtils.getDate(dtoRoleList.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));

				sessionFactory.getCurrentSession().update(incidentReportMaster);
			} else {
				IncidentReportRoleMapper incidentReportMaster = new IncidentReportRoleMapper();
				incidentReportMaster.setRoleId(dtoRoleList.getRoleId());
				incidentReportMaster.setIncidentReportId(dtoRoleList.getIncidentReportId());
				incidentReportMaster.setOrganizationId(dtoRoleList.getOrganizationId());
				incidentReportMaster.setUnitId(dtoRoleList.getUnitId());
				incidentReportMaster.setStatus(dtoRoleList.getStatus());
				incidentReportMaster.setCreatedBy(dtoRoleList.getCreatedBy());
				incidentReportMaster.setCreatedDate(
						GlobalCommonDateUtils.getDate(dtoRoleList.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
				incidentReportMaster.setUpdatedBy(dtoRoleList.getUpdatedBy());
				incidentReportMaster.setUpdatedDate(
						GlobalCommonDateUtils.getDate(dtoRoleList.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));

				sessionFactory.getCurrentSession().save(incidentReportMaster);
			}

			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);

		} catch (HibernateException he) {
		logger.error("updateIncidentReportRoleMapper failed", he);
		} catch (Exception e) {
		logger.error("updateIncidentReportRoleMapper failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
}
