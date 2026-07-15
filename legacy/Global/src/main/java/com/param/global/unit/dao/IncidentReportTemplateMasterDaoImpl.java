package com.param.global.unit.dao;

import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.model.IncidentReportTemplateMapper;
import com.param.global.unit.dto.IncidentReportMasterDto;
import com.param.global.unit.dto.IncidentReportRoleMapperDto;
import com.param.global.unit.dto.IncidentReportTemplateMapperDto;

import in.param.common.dao.GenericDao;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings({ "rawtypes", "unchecked" })
@Repository
public class IncidentReportTemplateMasterDaoImpl extends GenericDao<IncidentReportTemplateMapper, Integer>
		implements IIncidentReportTemplateMasterDao, ICommonConstants {
	final static Logger logger = Logger.getLogger(IncidentReportTemplateMasterDaoImpl.class);


	public IncidentReportTemplateMasterDaoImpl() {
		super(IncidentReportTemplateMapper.class);
	}

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Response updateIncidentReportTemplateMapper(IncidentReportMasterDto dtoTemplateList) {
		try {
			Integer result = sessionFactory.getCurrentSession().getNamedQuery("UPDATE_INCIDENT_REPORT_TEMPLATE")
					.setParameter("status", 'I').setParameter("organizationId", dtoTemplateList.getOrganizationId())
					.setParameter("unitId", dtoTemplateList.getUnitId())
					.setParameter("incidentReportId", dtoTemplateList.getIncidentReportId()).executeUpdate();
		//	if (result > 0) {
				for (IncidentReportRoleMapperDto roleDto : dtoTemplateList.getIncidentReportRoleMapperList()) {
					for (Iterator iterator = dtoTemplateList.getIncidentReportTemplateMapperList().iterator(); iterator
							.hasNext();) {
						IncidentReportTemplateMapperDto listUserStoreMapper = (IncidentReportTemplateMapperDto) iterator
								.next();
						IncidentReportTemplateMapper incidentReportMaster = new IncidentReportTemplateMapper();
						incidentReportMaster.setIncidentReportId(dtoTemplateList.getIncidentReportId());
						incidentReportMaster.setRoleId(roleDto.getRoleId());
						incidentReportMaster.setTemplateId(listUserStoreMapper.getTemplateId());
						incidentReportMaster.setOrganizationId(dtoTemplateList.getOrganizationId());
						incidentReportMaster.setUnitId(dtoTemplateList.getUnitId());
						incidentReportMaster.setStatus(dtoTemplateList.getStatus());
						incidentReportMaster.setCreatedBy(dtoTemplateList.getCreatedBy());
						incidentReportMaster.setCreatedDate(
								GlobalCommonDateUtils.getDate(dtoTemplateList.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
						incidentReportMaster.setUpdatedBy(dtoTemplateList.getUpdatedBy());
						incidentReportMaster.setUpdatedDate(
								GlobalCommonDateUtils.getDate(dtoTemplateList.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));

						save(incidentReportMaster);
					}
				}

				return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
			//}

		} catch (HibernateException he) {
		logger.error("updateIncidentReportTemplateMapper failed", he);
		} catch (Exception e) {
		logger.error("updateIncidentReportTemplateMapper failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
}
