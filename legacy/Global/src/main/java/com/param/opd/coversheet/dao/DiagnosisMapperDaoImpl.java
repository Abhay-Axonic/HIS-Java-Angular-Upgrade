package com.param.opd.coversheet.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.opd.coversheet.dto.DiagnosisMapperDto;
import com.param.opd.coversheet.dto.DiagnosisMasterDto;
import com.param.opd.coversheet.model.DiagnosisMapper;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({ "unchecked", "rawtypes" })
public class DiagnosisMapperDaoImpl  extends GenericDao<DiagnosisMapper, Integer>
implements IDiagnosisMapperDao, ICommonConstants {
	final static Logger logger = Logger.getLogger(DiagnosisMapperDaoImpl.class);


@Autowired
SessionFactory sessionFactory;

public DiagnosisMapperDaoImpl() {
super(DiagnosisMapper.class);
}

@Override
public Response saveDiagnosisMapper(DiagnosisMapperDto diagnosisMapperDto) throws ApplicationException {
	try {
			DiagnosisMapper mapper=new DiagnosisMapper();
			mapper.setDiagnosisId(diagnosisMapperDto.getDiagnosisId());
			mapper.setGenderId(diagnosisMapperDto.getGenderId());
			mapper.setIsNotifiable(diagnosisMapperDto.getIsNotifiable());
			mapper.setIsCommunicable(diagnosisMapperDto.getIsCommunicable());
			mapper.setIsNonCommunicable(diagnosisMapperDto.getIsNonCommunicable());
			mapper.setIsMedicoLegal(diagnosisMapperDto.getIsMedicoLegal());
			mapper.setStatus(diagnosisMapperDto.getStatus());
			mapper.setIsDeleted('N');
			mapper.setOrganizationId(diagnosisMapperDto.getOrganizationId());
			mapper.setUnitId(diagnosisMapperDto.getUnitId());
			mapper.setCreatedBy(diagnosisMapperDto.getCreatedBy());
			mapper.setCreatedDate(TimeZoneComponent.getDateByzone(diagnosisMapperDto.getUnitId()));
			mapper.setUpdatedBy(diagnosisMapperDto.getUpdatedBy());
			mapper.setUpdatedDate(TimeZoneComponent.getDateByzone(diagnosisMapperDto.getUnitId()));
			sessionFactory.getCurrentSession().save(mapper);
	
		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
	} catch (HibernateException he) {
	logger.error("saveDiagnosisMapper failed", he);
	} catch (Exception e) {
	logger.error("saveDiagnosisMapper failed", e);
	}
	return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);

}

@Override
public Response searchDiagnosisByCodeOrDescription(DiagnosisMapperDto diagnosisMapperDto) throws ApplicationException {
	try {
		List<DiagnosisMasterDto> list = sessionFactory.getCurrentSession()
				.getNamedQuery("SEARCH_DIAGNOSIS_BY_CODE_OR_DESCRIPTION")
				.setParameter("diagnosisName", "%"+ diagnosisMapperDto.getDiagnosisName().toLowerCase() + "%")
				.setResultTransformer(Transformers.aliasToBean(DiagnosisMasterDto.class)).list();
		return new Response(SUCCESS, null, null, list, null);
	} catch (HibernateException he) {
	logger.error("searchDiagnosisByCodeOrDescription failed", he);
	} catch (Exception e) {
	logger.error("searchDiagnosisByCodeOrDescription failed", e);
	}
	return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

@Override
public Response getListOfDiagnosisMapper(DiagnosisMapperDto diagnosisMapperDto) throws ApplicationException {
	try {
		List<DiagnosisMapperDto> list = sessionFactory.getCurrentSession()
				.getNamedQuery("GET_LIST_OF_DIAGNOSIS_MAPPER")
				.setParameter("organizationId", diagnosisMapperDto.getOrganizationId())
				.setFirstResult(diagnosisMapperDto.getOffset() != null ? diagnosisMapperDto.getOffset()  : 0)
				.setMaxResults(diagnosisMapperDto.getNoOfRecordsPerPage() != null ? diagnosisMapperDto.getNoOfRecordsPerPage()  : 10)
				.setResultTransformer(Transformers.aliasToBean(DiagnosisMapperDto.class)).list();
		return new Response(SUCCESS, null, null, list, null);
	} catch (HibernateException he) {
	logger.error("getListOfDiagnosisMapper failed", he);
	} catch (Exception e) {
	logger.error("getListOfDiagnosisMapper failed", e);
	}
	return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

@Override
public Response updateDiagnosisMapperStatus(DiagnosisMapperDto diagnosisMapperDto) throws ApplicationException {
	try{
		if (diagnosisMapperDto.getDiagnosisId() != 0) {

			Integer result = sessionFactory.getCurrentSession()
					.getNamedQuery("UPDATE_DIAGNOSIS_MAPPER_STATUS")
					.setParameter("orgId", diagnosisMapperDto.getOrganizationId())
					.setParameter("orgUnitId", diagnosisMapperDto.getUnitId())
					.setParameter("status", diagnosisMapperDto.getStatus())
					.setParameter("diagnosisId", diagnosisMapperDto.getDiagnosisId())
					.executeUpdate();
			if (result > 0) {
				if ( diagnosisMapperDto.getStatus().equals(ACTIVE)) {
					return new Response(SUCCESS,SUCCESS_CODE, COMMON_UPDATE, null, null);
				} else if ( diagnosisMapperDto.getStatus().equals(INACTIVE)) {
					return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
				} else if ( diagnosisMapperDto.getStatus().equals(REMOVED)) {
					return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
				}
			} else {
				return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}

		} else {
			return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
} catch (HibernateException he) {
		logger.error("updateDiagnosisMapperStatus failed", he);
		} catch (Exception e) {
		logger.error("updateDiagnosisMapperStatus failed", e);
		}
return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

@Override
public Response getGenderByDiagnosisId(DiagnosisMapperDto diagnosisMapperDto) throws ApplicationException {
	try {
		List<DiagnosisMapperDto> list = sessionFactory.getCurrentSession()
				.getNamedQuery("GET_GENDER_BY_DIAGNOSIS_ID")
				.setParameter("diagnosisId", diagnosisMapperDto.getDiagnosisId())
				.setParameter("organizationId", diagnosisMapperDto.getOrganizationId())
				.setResultTransformer(Transformers.aliasToBean(DiagnosisMapperDto.class)).list();
		return new Response(SUCCESS, null, null, list, null);
	} catch (HibernateException he) {
	logger.error("getGenderByDiagnosisId failed", he);
	} catch (Exception e) {
	logger.error("getGenderByDiagnosisId failed", e);
	}
	return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

@Override
public Response getDiagnosisByGenderId(DiagnosisMapperDto diagnosisMapperDto) throws ApplicationException {
	try {
		List<DiagnosisMapperDto> list = sessionFactory.getCurrentSession()
				.getNamedQuery("GET_DIAGNOSIS_BY_GENDER_ID")
				.setParameter("genderId", diagnosisMapperDto.getGenderId())
				.setParameter("diagnosisName", "%"+ diagnosisMapperDto.getDiagnosisName().toLowerCase() + "%")
				.setParameter("organizationId", diagnosisMapperDto.getOrganizationId())
				.setResultTransformer(Transformers.aliasToBean(DiagnosisMapperDto.class)).list();
		return new Response(SUCCESS, null, null, list, null);
	} catch (HibernateException he) {
	logger.error("getDiagnosisByGenderId failed", he);
	} catch (Exception e) {
	logger.error("getDiagnosisByGenderId failed", e);
	}
	return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

@Override
public Response updateDiagnosisMapper(DiagnosisMapperDto dto) throws ApplicationException {
	try {
		/*DiagnosisMapper mapper=findById(dto.getDiagnosisId());
//		mapper.setDiagnosisId(dto.getDiagnosisId());
		mapper.setGenderId(dto.getGenderId());
		mapper.setIsNotifiable(dto.getIsNotifiable());
		mapper.setIsCommunicable(dto.getIsCommunicable());
		mapper.setIsNonCommunicable(dto.getIsNonCommunicable());
		mapper.setIsMedicoLegal(dto.getIsMedicoLegal());
		mapper.setStatus(dto.getStatus());
		mapper.setOrganizationId(dto.getOrganizationId());
		mapper.setUnitId(dto.getUnitId());
		mapper.setUpdatedBy(dto.getUpdatedBy());
		mapper.setUpdatedDate(TimeZoneComponent.getDateByzone(unitId));
		update(mapper);*/
		
		/*Session session=sessionFactory.getCurrentSession();
		session.createSQLQuery(" update emr.t_diagnosis_mapper  set diagnosis_id = '"+dto.getDiagnosisId()
		+"', gender_id= '"+dto.getGenderId()
		+"', is_notifiable='"+dto.getIsNotifiable()
		+"', is_communicable='"+dto.getIsCommunicable()
		+"', is_non_communicable='"+dto.getIsNonCommunicable()
		+"', is_medico_legal='"+dto.getIsMedicoLegal()
		+"', updated_by='"+dto.getUpdatedBy()
		+"' , updated_date='"+(TimeZoneComponent.getDateByzone(unitId))+"' "
		+ " where diagnosis_id="+dto.getDiagnosisId())
		.executeUpdate();*/
		Session session=sessionFactory.getCurrentSession();
		session.createSQLQuery(" update emr.t_diagnosis_mapper  set  is_deleted='"+('Y')+"' "
		+ " where diagnosis_id="+dto.getDiagnosisId())
		.executeUpdate();
	return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
} catch (HibernateException he) {
		logger.error("updateDiagnosisMapper failed", he);
		} catch (Exception e) {
		logger.error("updateDiagnosisMapper failed", e);
		}
return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);

}

@Override
public Response checkDiagnosisMapperAvailable(DiagnosisMapperDto diagnosisMapperDto) throws ApplicationException {
	try {
		List<DiagnosisMapperDto> list = sessionFactory.getCurrentSession()
				.getNamedQuery("CHECK_DIAGNOSIS_MAPPER_AVAILABLE")
				.setParameter("diagnosisId", diagnosisMapperDto.getDiagnosisId())
//				.setParameter("genderId", diagnosisMapperDto.getGenderId())
				.setParameter("organizationId", diagnosisMapperDto.getOrganizationId())
				.setResultTransformer(Transformers.aliasToBean(DiagnosisMapperDto.class)).list();
		return new Response(SUCCESS, null, null, list, null);
	} catch (HibernateException he) {
	logger.error("checkDiagnosisMapperAvailable failed", he);
	} catch (Exception e) {
	logger.error("checkDiagnosisMapperAvailable failed", e);
	}
	return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

@Override
public Response getDiagnosisMapperById(DiagnosisMapperDto diagnosisMapperDto) throws ApplicationException {
	try {
		List<DiagnosisMapperDto> list = sessionFactory.getCurrentSession()
				.getNamedQuery("GET_DIAGNOSIS_MAPPER_BY_ID")
				.setParameter("diagnosisId", diagnosisMapperDto.getDiagnosisId())
				.setParameter("organizationId", diagnosisMapperDto.getOrganizationId())
				.setResultTransformer(Transformers.aliasToBean(DiagnosisMapperDto.class)).list();
		return new Response(SUCCESS, null, null, list, null);
	} catch (HibernateException he) {
	logger.error("getDiagnosisMapperById failed", he);
	} catch (Exception e) {
	logger.error("getDiagnosisMapperById failed", e);
	}
	return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

@Override
public Response getDiagnosisMapperCount(DiagnosisMapperDto diagnosisMapperDto) throws ApplicationException {
	try {
		
		Session session = sessionFactory.getCurrentSession();
		Criteria crit = session.createCriteria(DiagnosisMapper.class);
		crit.add(Restrictions.eq("organizationId", diagnosisMapperDto.getOrganizationId()));
		crit.setProjection(Projections.rowCount());
		Long count = (Long) crit.uniqueResult();
		
		return new Response(SUCCESS, SUCCESS_CODE, null, null, count);
			
	} catch (HibernateException he) {
	logger.error("getDiagnosisMapperCount failed", he);
	} catch (Exception e) {
	logger.error("getDiagnosisMapperCount failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

}
