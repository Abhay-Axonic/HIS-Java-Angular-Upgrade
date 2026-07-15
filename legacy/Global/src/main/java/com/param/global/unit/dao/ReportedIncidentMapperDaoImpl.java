package com.param.global.unit.dao;


import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.IBillingCommonConstants;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.model.ReportedIncidentMapper;
import com.param.global.unit.dto.ReportedIncidentMapperDto;

import in.param.common.dao.GenericDao;

@SuppressWarnings({ "rawtypes", "unchecked" })
@Repository
public class ReportedIncidentMapperDaoImpl extends GenericDao<ReportedIncidentMapper, Integer>
		implements IReportedIncidentMapperDao, ICommonConstants, IBillingCommonConstants {

	final static Logger logger = Logger.getLogger(ReportedIncidentMapperDaoImpl.class);

	public ReportedIncidentMapperDaoImpl() {
		super(ReportedIncidentMapper.class);
	}

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Response saveReportedIncidentMapper(ReportedIncidentMapperDto dto) {
		try {
			if(dto.getReportedIncMpprId()==null) {
			ReportedIncidentMapper reportedIncidentMapper = new ReportedIncidentMapper();

			reportedIncidentMapper.setResolutionDetailsTypeId(dto.getResolutionDetailsTypeId());
			reportedIncidentMapper.setParentUserId(dto.getParentUserId());
			reportedIncidentMapper.setIncidentReportId(dto.getIncidentReportId());
			reportedIncidentMapper.setReportedTemplateId(dto.getReportedTemplateId());
			reportedIncidentMapper.setReportedTemplateObj(dto.getReportedTemplateObj());
			reportedIncidentMapper.setIsSubmit(dto.getIsSubmit());
			reportedIncidentMapper.setParentUserId(dto.getParentUserId());
			reportedIncidentMapper.setIncidentReportingMapperId(dto.getIncidentReportingMapperId());
			reportedIncidentMapper.setOrganizationId(dto.getOrganizationId());
			reportedIncidentMapper.setUnitId(dto.getUnitId());
			reportedIncidentMapper.setStatus(dto.getStatus());
			reportedIncidentMapper.setCreatedBy(dto.getCreatedBy());
			reportedIncidentMapper
					.setCreatedDate(GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			reportedIncidentMapper.setUpdatedBy(dto.getUpdatedBy());
			reportedIncidentMapper
					.setUpdatedDate(GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));

			save(reportedIncidentMapper);
			dto.setReportedIncMpprId(reportedIncidentMapper.getReportedIncMpprId());

			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, dto);
			}else {
				ReportedIncidentMapper reportedIncidentMapper = findById(dto.getReportedIncMpprId());

				reportedIncidentMapper.setResolutionDetailsTypeId(dto.getResolutionDetailsTypeId());
				reportedIncidentMapper.setParentUserId(dto.getParentUserId());
				reportedIncidentMapper.setIncidentReportId(dto.getIncidentReportId());
				reportedIncidentMapper.setReportedTemplateId(dto.getReportedTemplateId());
				reportedIncidentMapper.setReportedTemplateObj(dto.getReportedTemplateObj());
				reportedIncidentMapper.setParentUserId(dto.getParentUserId());
				reportedIncidentMapper.setIncidentReportingMapperId(dto.getIncidentReportingMapperId());
				reportedIncidentMapper.setIsSubmit(dto.getIsSubmit());
				reportedIncidentMapper.setOrganizationId(dto.getOrganizationId());
				reportedIncidentMapper.setUnitId(dto.getUnitId());
				reportedIncidentMapper.setStatus(dto.getStatus());
				reportedIncidentMapper.setCreatedBy(dto.getCreatedBy());
				reportedIncidentMapper
						.setCreatedDate(GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
				reportedIncidentMapper.setUpdatedBy(dto.getUpdatedBy());
				reportedIncidentMapper
						.setUpdatedDate(GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));

				update(reportedIncidentMapper);

				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, dto);
				}
		} catch (HibernateException he) {
			logger.error("saveReportedIncidentMapper failed", he);
		} catch (Exception e) {
			logger.error("saveReportedIncidentMapper failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	
	
	@Override
	public Response submitReportedIncidentMapper(ReportedIncidentMapperDto dto) {
		try {

			Session session = sessionFactory.getCurrentSession();

			String sql = "UPDATE public.t_reported_incident_mapper " +
					"SET is_submit = :isSubmit, " +
					"resolution_details_type_id = :resolutionDetailsTypeId " +
					"WHERE reported_inc_mppr_id = :reportedIncMpprId";

			Query query = session.createSQLQuery(sql);
			query.setParameter("isSubmit", dto.getIsSubmit());
			query.setParameter("resolutionDetailsTypeId", dto.getResolutionDetailsTypeId());
			query.setParameter("reportedIncMpprId", dto.getReportedIncMpprId());

			int count = query.executeUpdate();



			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, dto);
		} catch (HibernateException he) {
			logger.error("submitReportedIncidentMapper failed", he);
		} catch (Exception e) {
			logger.error("submitReportedIncidentMapper failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
}
