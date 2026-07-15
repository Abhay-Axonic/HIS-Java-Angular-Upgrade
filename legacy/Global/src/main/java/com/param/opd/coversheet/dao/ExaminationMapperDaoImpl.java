package com.param.opd.coversheet.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.ExaminationMasterDto;
import com.param.opd.coversheet.dto.ExaminationMapperDto;
import com.param.opd.coversheet.model.ComplaintAppointmentMapper;
import com.param.opd.coversheet.model.ExaminationMapper;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({ "unused", "unchecked" })
public class ExaminationMapperDaoImpl extends GenericDao<ExaminationMapper, Integer> implements IExaminationMapperDao,ICommonConstants{
	final static Logger logger = Logger.getLogger(ExaminationMapperDaoImpl.class);


	public ExaminationMapperDaoImpl() {
		super(ExaminationMapper.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Response saveExaminationMapper(ExaminationMapperDto examinationMapperDto) throws ApplicationException {
		try {
			ExaminationMapper examinationMapper = new ExaminationMapper();
			examinationMapper.setExaminationMasterId(examinationMapperDto.getExaminationMasterId());
			examinationMapper.setRemark(examinationMapperDto.getRemark());
			examinationMapper.setPatientId(examinationMapperDto.getPatientId());
			examinationMapper.setEncounterId(examinationMapperDto.getEncounterId());
			examinationMapper.setDoctorId(examinationMapperDto.getDoctorId());
			examinationMapper.setAdmissionId(examinationMapperDto.getAdmissionId());
			examinationMapper.setStatus(examinationMapperDto.getStatus());
			examinationMapper.setOrganizationId(examinationMapperDto.getOrganizationId());
			examinationMapper.setUnitId(examinationMapperDto.getUnitId());
			examinationMapper.setCreatedBy(examinationMapperDto.getCreatedBy());
			examinationMapper.setUpdatedBy(examinationMapperDto.getUpdatedBy());
			examinationMapper.setCreatedDate(new Date());
			examinationMapper.setUpdatedDate(new Date());
			
			save(examinationMapper);

			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		} catch (HibernateException he) {
		logger.error("saveExaminationMapper failed", he);
		} catch (Exception e) {
		logger.error("saveExaminationMapper failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response updateExaminationMapper(ExaminationMapperDto examinationMapperDto) throws ApplicationException {
		try {
			ExaminationMapper examinationMapper = findById(examinationMapperDto.getExaminationMapperId());
			examinationMapper.setExaminationMasterId(examinationMapperDto.getExaminationMasterId());
			examinationMapper.setPatientId(examinationMapperDto.getPatientId());
			examinationMapper.setRemark(examinationMapperDto.getRemark());
			examinationMapper.setEncounterId(examinationMapperDto.getEncounterId());
			examinationMapper.setDoctorId(examinationMapperDto.getDoctorId());
			examinationMapper.setAdmissionId(examinationMapperDto.getAdmissionId());
			examinationMapper.setStatus(examinationMapperDto.getStatus());
			examinationMapper.setOrganizationId(examinationMapperDto.getOrganizationId());
			examinationMapper.setUnitId(examinationMapperDto.getUnitId());
			examinationMapper.setCreatedBy(examinationMapperDto.getCreatedBy());
			examinationMapper.setUpdatedBy(examinationMapperDto.getUpdatedBy());
			examinationMapper.setCreatedDate(new Date());
			examinationMapper.setUpdatedDate(new Date());
			
			update(examinationMapper);

			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (HibernateException he) {
		logger.error("updateExaminationMapper failed", he);
		} catch (Exception e) {
		logger.error("updateExaminationMapper failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}



	@Override
	public Response getExaminationMapperById(ExaminationMapperDto examinationMapperDto) throws ApplicationException {
		try {
			List<ExaminationMapperDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_EXAMINATION_MAPPER_BY_ID")
					.setParameter("examinationMapperId", examinationMapperDto.getExaminationMapperId())
					.setParameter("unitId", examinationMapperDto.getUnitId())
					.setParameter("organizationId", examinationMapperDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(ExaminationMapperDto.class)).list();
			return new Response(SUCCESS, null, null, list, null);

		} catch (HibernateException he) {
		logger.error("getExaminationMapperById failed", he);
		} catch (Exception e) {
		logger.error("getExaminationMapperById failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getExaminationMapperList(ExaminationMapperDto examinationMapperDto) throws ApplicationException {
		try {
			List<ExaminationMapperDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_EXAMINATION_MAPPER_LIST")
					.setParameter("patientId", examinationMapperDto.getPatientId())
					.setParameter("unitId", examinationMapperDto.getUnitId())
					.setParameter("organizationId", examinationMapperDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(ExaminationMapperDto.class)).list();
			return new Response(SUCCESS, null, null, list, null);

		} catch (HibernateException he) {
		logger.error("getExaminationMapperList failed", he);
		} catch (Exception e) {
		logger.error("getExaminationMapperList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getMostlyUsedExaminationMapperListFor(ExaminationMapperDto examinationMapperDto)
			throws ApplicationException {
		try {
			List<ExaminationMapperDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_MOSTLY_EXAMINATION_MAPPER_LIST_FOR")
					.setParameter("unitId", examinationMapperDto.getUnitId())
					.setParameter("organizationId", examinationMapperDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(ExaminationMapperDto.class)).list();
			return new Response(SUCCESS, null, null, list, null);

		} catch (HibernateException he) {
		logger.error("getMostlyUsedExaminationMapperListFor failed", he);
		} catch (Exception e) {
		logger.error("getMostlyUsedExaminationMapperListFor failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response removeExaminationMapper(ExaminationMapperDto examinationMapperDto) throws ApplicationException {
		try {
			ExaminationMapper examinationMapper =findById(examinationMapperDto.getExaminationMapperId());
			examinationMapper.setStatus(examinationMapperDto.getStatus());
			examinationMapper.setUpdatedBy(examinationMapperDto.getUpdatedBy());
			examinationMapper.setUpdatedDate(new Date());
		
		update(examinationMapper);

		return new Response(SUCCESS, SUCCESS_CODE, "Examination removed successfully.", null, null);
	} catch (HibernateException he) {
	logger.error("removeExaminationMapper failed", he);
	} catch (Exception e) {
	logger.error("removeExaminationMapper failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
}

	@Override
	public Response checkAvailableExaminationMapper(ExaminationMapperDto examinationMapperDto)
			throws ApplicationException {
		try {
			List<ExaminationMapperDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("CHECK_AVAILABLE_EXAMINATION_MAPPER")
					.setParameter("exmnMasterId", examinationMapperDto.getExaminationMasterId())
					.setParameter("patientId", examinationMapperDto.getPatientId())
					.setParameter("encounterId", examinationMapperDto.getEncounterId())
					.setParameter("unitId", examinationMapperDto.getUnitId())
					.setParameter("organizationId", examinationMapperDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(ExaminationMapperDto.class)).list();
			return new Response(SUCCESS, null, null, list, null);

		} catch (HibernateException he) {
		logger.error("checkAvailableExaminationMapper failed", he);
		} catch (Exception e) {
		logger.error("checkAvailableExaminationMapper failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

}
