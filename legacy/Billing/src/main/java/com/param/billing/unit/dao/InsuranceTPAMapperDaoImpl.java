package com.param.billing.unit.dao;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.billing.unit.dto.InsuranceTPAMapperDto;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.service.global.model.InsuranceTPAMapper;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class InsuranceTPAMapperDaoImpl extends GenericDao<InsuranceTPAMapper, Integer>
		implements IInsuranceTPAMapperDao, ICommonConstants {

	final static Logger logger = Logger.getLogger(InsuranceTPAMapperDaoImpl.class);

	public InsuranceTPAMapperDaoImpl() {
		super(InsuranceTPAMapper.class);
	}

	@Autowired
	SessionFactory sessionFactory;


	@Override
	public Response saveInsuranceTPAMapper(InsuranceTPAMapperDto insuranceTPAMapperDto) throws ApplicationException {
		try {
			if( insuranceTPAMapperDto.getInsuranceTPAMapperDtoList()!=null &&  insuranceTPAMapperDto.getInsuranceTPAMapperDtoList().size()>0){
				
					Iterator<InsuranceTPAMapperDto> itr = insuranceTPAMapperDto.getInsuranceTPAMapperDtoList().iterator();
					while (itr.hasNext()) {
						
						InsuranceTPAMapper unMapper = new InsuranceTPAMapper();
						
						InsuranceTPAMapperDto obj = itr.next();
		
						unMapper.setUnitId(insuranceTPAMapperDto.getUnitId());
						unMapper.setOrganizationId(insuranceTPAMapperDto.getOrganizationId());
						unMapper.setInsuranceId(obj.getInsuranceId());
						unMapper.setCompanyId(insuranceTPAMapperDto.getCompanyId());
						unMapper.setStatus('A');
						unMapper.setCreatedBy(insuranceTPAMapperDto.getCreatedBy());
						unMapper.setCreatedDate(GlobalCommonDateUtils.getDate(insuranceTPAMapperDto.getCreatedDate(),"dd-M-yyyy HH:mm:ss"));
						unMapper.setUpdatedBy(insuranceTPAMapperDto.getUpdatedBy());
						unMapper.setUpdatedDate(GlobalCommonDateUtils.getDate(insuranceTPAMapperDto.getUpdatedDate(),"dd-M-yyyy HH:mm:ss"));
						save(unMapper);				
					}
			}
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (HibernateException he) {
			logger.error("saveInsuranceTPAMapper failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("saveInsuranceTPAMapper failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getInsuranceTPAMapperList(InsuranceTPAMapperDto insuranceTPAMapperDto) throws ApplicationException {
		try {
			List<InsuranceTPAMapperDto> listInsuranceTPAMapperDto=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_INSURANCE_TPA_MAPPER_LIST")
					.setParameter("unitId", insuranceTPAMapperDto.getUnitId())
					.setParameter("orgId", insuranceTPAMapperDto.getOrganizationId())
					.setFirstResult(insuranceTPAMapperDto.getOffset() != null ? insuranceTPAMapperDto.getOffset() : 0)
					.setMaxResults(insuranceTPAMapperDto.getNoOfRecordsPerPage() != null ? insuranceTPAMapperDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(InsuranceTPAMapperDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listInsuranceTPAMapperDto, null);
		} catch (HibernateException he) {
			logger.error("getInsuranceTPAMapperList failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getInsuranceTPAMapperList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getInsuranceTPAMapperListCount(InsuranceTPAMapperDto insuranceTPAMapperDto)
			throws ApplicationException {
		try {
			Session session=sessionFactory.getCurrentSession();
			Criteria c=session.createCriteria(InsuranceTPAMapper.class);
			c.add(Restrictions.eq("organizationId", insuranceTPAMapperDto.getOrganizationId()));
			c.setProjection(Projections.rowCount());  
			Long count = (Long)c.uniqueResult();
			return new Response(SUCCESS, null, null, null, count);
		} catch (HibernateException he) {
			logger.error("getInsuranceTPAMapperListCount failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getInsuranceTPAMapperListCount failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateInsuranceTPAMapperStatus(InsuranceTPAMapperDto insuranceTPAMapperDto)
			throws ApplicationException {
		try {
			InsuranceTPAMapper unMapper =findById(insuranceTPAMapperDto.getInsuranceTpaMapperId());
					unMapper.setStatus(insuranceTPAMapperDto.getStatus()==ACTIVE ? ACTIVE :INACTIVE);
					unMapper.setUpdatedBy(insuranceTPAMapperDto.getUpdatedBy());
					unMapper.setUpdatedDate(GlobalCommonDateUtils.getDate(insuranceTPAMapperDto.getUpdatedDate(),"dd-M-yyyy HH:mm:ss"));
					update(unMapper);
					return new Response(SUCCESS, COMMON_UPDATE, COMMON_UPDATE, null, null);
		} catch (HibernateException he) {
			logger.error("updateInsuranceTPAMapperStatus failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("updateInsuranceTPAMapperStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getInsuranceTPAMapperListById(InsuranceTPAMapperDto insuranceTPAMapperDto)
			throws ApplicationException {
		try {
			List<InsuranceTPAMapperDto> listInsuranceTPAMapperDto=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_INSURANCE_TPA_MAPPER_LIST_BY_ID")
					.setParameter("insuranceTpaMapperId", insuranceTPAMapperDto.getInsuranceTpaMapperId())
					.setResultTransformer(Transformers.aliasToBean(InsuranceTPAMapperDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listInsuranceTPAMapperDto, null);
		} catch (HibernateException he) {
			logger.error("getInsuranceTPAMapperListById failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getInsuranceTPAMapperListById failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getInsuranceListNotMappedToMapper(InsuranceTPAMapperDto insuranceTPAMapperDto)
			throws ApplicationException {
		try {
			List<InsuranceTPAMapperDto> listInsuranceTPAMapperDto=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_INSURANCE_LIST_NOT_MAPPED_TO_MAPPER")
					.setParameter("unitId", insuranceTPAMapperDto.getUnitId())
					.setParameter("orgId", insuranceTPAMapperDto.getOrganizationId())
//					.setInteger("companyId", insuranceTPAMapperDto.getCompanyId())
					.setResultTransformer(Transformers.aliasToBean(InsuranceTPAMapperDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listInsuranceTPAMapperDto, null);
		} catch (HibernateException he) {
			logger.error("getInsuranceListNotMappedToMapper failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getInsuranceListNotMappedToMapper failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

}
