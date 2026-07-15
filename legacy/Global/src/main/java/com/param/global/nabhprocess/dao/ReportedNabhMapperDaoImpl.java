package com.param.global.nabhprocess.dao;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.IBillingCommonConstants;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.model.ReportedNabhMapper;
import com.param.global.nabhprocess.dto.ReportedNabhMapperDto;
import in.param.common.dao.GenericDao;

@SuppressWarnings({ "rawtypes", "unchecked" })
@Repository
public class ReportedNabhMapperDaoImpl extends GenericDao<ReportedNabhMapper, Integer>
implements IReportedNabhMapperDao, ICommonConstants, IBillingCommonConstants {

	private static final Logger logger = Logger.getLogger(ReportedNabhMapperDaoImpl.class);

	public ReportedNabhMapperDaoImpl() {
		super(ReportedNabhMapper.class);
	}
	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Response saveReportedNabhMapper(ReportedNabhMapperDto dto) {
		try {
			if(dto.getReportedNabhMpprId()==null) {
			ReportedNabhMapper reportedNabhMapper = new ReportedNabhMapper();

			reportedNabhMapper.setResolutionDetailsTypeId(dto.getResolutionDetailsTypeId());
			reportedNabhMapper.setParentUserId(dto.getParentUserId());
			reportedNabhMapper.setNabhProcessId(dto.getNabhProcessId());
			reportedNabhMapper.setReportedTemplateId(dto.getReportedTemplateId());
			reportedNabhMapper.setReportedTemplateObj(dto.getReportedTemplateObj());
			reportedNabhMapper.setIsSubmit(dto.getIsSubmit());
			reportedNabhMapper.setParentUserId(dto.getParentUserId());
			reportedNabhMapper.setNabhReportingMapperId(dto.getNabhReportingMapperId());
			reportedNabhMapper.setOrganizationId(dto.getOrganizationId());
			reportedNabhMapper.setUnitId(dto.getUnitId());
			reportedNabhMapper.setStatus(dto.getStatus());
			reportedNabhMapper.setCreatedBy(dto.getCreatedBy());
			reportedNabhMapper
					.setCreatedDate(GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			reportedNabhMapper.setUpdatedBy(dto.getUpdatedBy());
			reportedNabhMapper
					.setUpdatedDate(GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));

			save(reportedNabhMapper);
			dto.setReportedNabhMpprId(reportedNabhMapper.getReportedNabhMpprId());

			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, dto);
			}else {
				ReportedNabhMapper reportedNabhMapper = findById(dto.getReportedNabhMpprId());

				reportedNabhMapper.setResolutionDetailsTypeId(dto.getResolutionDetailsTypeId());
				reportedNabhMapper.setParentUserId(dto.getParentUserId());
				reportedNabhMapper.setNabhProcessId(dto.getNabhProcessId());
				reportedNabhMapper.setReportedTemplateId(dto.getReportedTemplateId());
				reportedNabhMapper.setReportedTemplateObj(dto.getReportedTemplateObj());
				reportedNabhMapper.setParentUserId(dto.getParentUserId());
				reportedNabhMapper.setNabhReportingMapperId(dto.getNabhReportingMapperId());
				reportedNabhMapper.setIsSubmit(dto.getIsSubmit());
				reportedNabhMapper.setOrganizationId(dto.getOrganizationId());
				reportedNabhMapper.setUnitId(dto.getUnitId());
				reportedNabhMapper.setStatus(dto.getStatus());
				reportedNabhMapper.setCreatedBy(dto.getCreatedBy());
				reportedNabhMapper
						.setCreatedDate(GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
				reportedNabhMapper.setUpdatedBy(dto.getUpdatedBy());
				reportedNabhMapper
						.setUpdatedDate(GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));

				update(reportedNabhMapper);

				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, dto);
				}
		} catch (HibernateException he) {
			logger.error("saveReportedNabhMapper failed: " + he.getMessage(), he);
			String errorMsg = ReportedNabhMapperDaoImpl.class.getName() + " :" + he.getMessage();
			return new Response<>(ERROR, ERROR_CODE, errorMsg, null, null);
		}
	}

	@Override
	public Response submitReportedNabhMapper(ReportedNabhMapperDto dto) {
try {

	if (dto.getReportedNabhMpprId() != null) {
		Session session = sessionFactory.getCurrentSession();
	    session.createSQLQuery(
						"UPDATE public.t_reported_nabh_mapper " +
								"SET is_submit = :isSubmit, " +
								"resolution_details_type_id = :resolutionDetailsTypeId " +
								"WHERE reported_nabh_mppr_id = :reportedNabhMpprId")
				.setParameter("isSubmit", dto.getIsSubmit())
				.setParameter("resolutionDetailsTypeId", dto.getResolutionDetailsTypeId())
				.setParameter("reportedNabhMpprId", dto.getReportedNabhMpprId())
				.executeUpdate();
	}
	return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, dto);
		} catch (HibernateException he) {
			logger.error("submitReportedNabhMapper failed: " + he.getMessage(), he);
			String errorMsg = ReportedNabhMapperDaoImpl.class.getName() + " :" + he.getMessage();
			return new Response<>(ERROR, ERROR_CODE, errorMsg, null, null);
		}


	}

}
