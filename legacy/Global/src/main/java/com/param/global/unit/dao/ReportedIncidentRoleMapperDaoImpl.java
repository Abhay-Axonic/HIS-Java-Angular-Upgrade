package com.param.global.unit.dao;

import java.util.Iterator;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.IBillingCommonConstants;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.model.IncidentReportTemplateMapper;
import com.param.global.model.ReportedIncidentMapper;
import com.param.global.model.ReportedIncidentRoleMapper;
import com.param.global.nabhprocess.service.ReportedNabhMapperServiceImpl;
import com.param.global.unit.dto.IncidentReportRoleMapperDto;
import com.param.global.unit.dto.IncidentReportTemplateMapperDto;
import com.param.global.unit.dto.ReportedIncidentMapperDto;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings({ "rawtypes", "unchecked" })
@Repository
public class ReportedIncidentRoleMapperDaoImpl extends GenericDao<ReportedIncidentRoleMapper, Integer>
implements IReportedIncidentRoleMapperDao, ICommonConstants, IBillingCommonConstants {
	final static Logger logger = Logger.getLogger(ReportedIncidentRoleMapperDaoImpl.class);


public ReportedIncidentRoleMapperDaoImpl() {
super(ReportedIncidentRoleMapper.class);
}

@Autowired
SessionFactory sessionFactory;

@Override
public Response saveReportedIncidentRoleMapper(ReportedIncidentMapperDto reportedIncidentMapperDto)
		throws ApplicationException {
	try {
			ReportedIncidentRoleMapper reportedIncidentMapper = new ReportedIncidentRoleMapper();
			
			reportedIncidentMapper.setReportedIncMpprId(reportedIncidentMapperDto.getReportedIncMpprId());
			reportedIncidentMapper.setReportedTemplateId(reportedIncidentMapperDto.getReportedTemplateId());
			reportedIncidentMapper.setReportedTemplateObj(reportedIncidentMapperDto.getReportedTemplateObj());
			reportedIncidentMapper.setParentRepIncUserId(reportedIncidentMapperDto.getParentRepIncUserId());
			reportedIncidentMapper.setRoleId(reportedIncidentMapperDto.getRoleId());
			reportedIncidentMapper.setOrganizationId(reportedIncidentMapperDto.getOrganizationId());
			reportedIncidentMapper.setUnitId(reportedIncidentMapperDto.getUnitId());
			reportedIncidentMapper.setStatus(reportedIncidentMapperDto.getStatus());
			reportedIncidentMapper.setCreatedBy(reportedIncidentMapperDto.getCreatedBy());
			reportedIncidentMapper.setCreatedDate(GlobalCommonDateUtils .getDate(reportedIncidentMapperDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			reportedIncidentMapper.setUpdatedBy(reportedIncidentMapperDto.getUpdatedBy());
			reportedIncidentMapper.setUpdatedDate(GlobalCommonDateUtils.getDate(reportedIncidentMapperDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));

			save(reportedIncidentMapper);
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
				
}  catch (Exception e) {
		logger.error("saveReportedIncidentRoleMapper failed", e);
		}
return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

	@Override
	public Response saveReportedNabhRoleMapper(ReportedIncidentMapperDto reportedIncidentMapperDto) throws ApplicationException {
		try {
				ReportedIncidentRoleMapper reportedIncidentMapper = new ReportedIncidentRoleMapper();

				reportedIncidentMapper.setReportedNabhMpprId(reportedIncidentMapperDto.getReportedNabhMpprId());
				reportedIncidentMapper.setReportedTemplateId(reportedIncidentMapperDto.getReportedTemplateId());
				reportedIncidentMapper.setReportedTemplateObj(reportedIncidentMapperDto.getReportedTemplateObj());
				reportedIncidentMapper.setParentRepIncUserId(reportedIncidentMapperDto.getParentRepIncUserId());
				reportedIncidentMapper.setRoleId(reportedIncidentMapperDto.getRoleId());
				reportedIncidentMapper.setOrganizationId(reportedIncidentMapperDto.getOrganizationId());
				reportedIncidentMapper.setUnitId(reportedIncidentMapperDto.getUnitId());
				reportedIncidentMapper.setStatus(reportedIncidentMapperDto.getStatus());
				reportedIncidentMapper.setCreatedBy(reportedIncidentMapperDto.getCreatedBy());
				reportedIncidentMapper.setCreatedDate(GlobalCommonDateUtils
						.getDate(reportedIncidentMapperDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
				reportedIncidentMapper.setUpdatedBy(reportedIncidentMapperDto.getUpdatedBy());
				reportedIncidentMapper.setUpdatedDate(GlobalCommonDateUtils
						.getDate(reportedIncidentMapperDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));

				save(reportedIncidentMapper);
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);



		} catch (HibernateException he) {
		logger.error("saveReportedNabhRoleMapper failed", he);
		String errorMsg = ReportedIncidentRoleMapperDaoImpl.class.getName() + " :" + he.getMessage();
		return new Response<>(ERROR, ERROR_CODE, errorMsg, null, null);
		}

	}
    @Override
    public void updateReportedNabhRoleMapper(ReportedIncidentMapperDto reportedIncidentMapperDto) {
        Integer result = sessionFactory.getCurrentSession()
                .getNamedQuery("UPDATE_REPORTED_INCIDENT_ROLE_MAPPER_NABH").setParameter("status", 'I')
                .setParameter("organizationId", reportedIncidentMapperDto.getOrganizationId())
                .setParameter("unitId", reportedIncidentMapperDto.getUnitId())
                .setParameter("repIncRoleMpprId", reportedIncidentMapperDto.getReportedNabhMpprId())
                .executeUpdate();
    }


	@Override
	public Integer updateReportedIncidentRoleMapper(Integer reportedIncMpprId, Integer unitId, Integer organizationId ) throws ApplicationException {
		try {
			Integer result = sessionFactory.getCurrentSession().getNamedQuery("UPDATE_REPORTED_INCIDENT_ROLE_MAPPER")
					.setParameter("status", 'I')
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.setParameter("reportedIncMpprId", reportedIncMpprId)
					.executeUpdate();

			return result;
		}
		catch (Exception e){
		logger.error("updateReportedIncidentRoleMapper failed", e);
		}
		return 0;
	}

}
