package com.param.global.unit.dao;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.model.ICPCTemplateMapping;
import com.param.global.unit.dto.ICPCTemplateMappingDto;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes", "unchecked"})
public class ICPCTemplateMappingDaoImpl extends GenericDao<ICPCTemplateMapping, Integer> implements IICPCTemplateMappingDao, ICommonConstants{
	final static Logger logger = Logger.getLogger(ICPCTemplateMappingDaoImpl.class);


	public ICPCTemplateMappingDaoImpl() {
		super(ICPCTemplateMapping.class);
	}

	@Override
	public Response saveICPCTemplateMapping(ICPCTemplateMappingDto iCPCTemplateMappingDto) throws ApplicationException {
		try {
			
			ICPCTemplateMapping iCPCTemplateMapping=new ICPCTemplateMapping();
			iCPCTemplateMapping.setTemplateId(iCPCTemplateMappingDto.getTemplateId());
			iCPCTemplateMapping.setmIcpcSystemId(iCPCTemplateMappingDto.getmIcpcSystemId());
			iCPCTemplateMapping.setmIcpcId(iCPCTemplateMappingDto.getmIcpcId());
			iCPCTemplateMapping.setOrgId(iCPCTemplateMappingDto.getOrgId());
			iCPCTemplateMapping.setOrgUnitId(iCPCTemplateMappingDto.getOrgUnitId());
			iCPCTemplateMapping.setCreatedBy(iCPCTemplateMappingDto.getCreatedBy());
			iCPCTemplateMapping.setUpdatedBy(iCPCTemplateMappingDto.getUpdatedBy());
			iCPCTemplateMapping.setCreatedDate(GlobalCommonDateUtils.getDate(iCPCTemplateMappingDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			iCPCTemplateMapping.setUpdatedDate(GlobalCommonDateUtils.getDate(iCPCTemplateMappingDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			iCPCTemplateMapping.setStatus(iCPCTemplateMappingDto.getStatus());
			iCPCTemplateMapping.setIsDeleted(iCPCTemplateMappingDto.getIsDeleted());
			
			save(iCPCTemplateMapping);

			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (HibernateException he) {
		logger.error("saveICPCTemplateMapping failed", he);
		} catch (Exception e) {
		logger.error("saveICPCTemplateMapping failed", e);
		}
	return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@Override
	public Response updateICPCTemplateMapping(ICPCTemplateMappingDto iCPCTemplateMappingDto)
			throws ApplicationException {
		try{			
					sessionFactory.getCurrentSession()
					.getNamedQuery("UPDATE_ICPC_TEMPLATE_MAPPING")
					.setParameter("isDeleted", 'Y')
					.setParameter("orgId", iCPCTemplateMappingDto.getOrgId())
					.setParameter("orgUnitId", iCPCTemplateMappingDto.getOrgUnitId())		
					.setParameter("templateId", iCPCTemplateMappingDto.getTemplateId())
					.executeUpdate();
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
				}catch (HibernateException he) {
				logger.error("updateICPCTemplateMapping failed", he);
				} catch (Exception e) {
				logger.error("updateICPCTemplateMapping failed", e);
				}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	
	@Override
	public Response getICPCTemplateMappingById(ICPCTemplateMappingDto iCPCTemplateMappingDto)
			throws ApplicationException {
		try {
			List<ICPCTemplateMappingDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ICPC_TEMPLATE_MAPPING_BY_ID")
					.setParameter("orgId", iCPCTemplateMappingDto.getOrgId())
					.setParameter("orgUnitId", iCPCTemplateMappingDto.getOrgUnitId())
					.setParameter("templateId", iCPCTemplateMappingDto.getTemplateId())
					.setResultTransformer(Transformers.aliasToBean(ICPCTemplateMappingDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
		logger.error("getICPCTemplateMappingById failed", he);
		} catch (Exception e) {
		logger.error("getICPCTemplateMappingById failed", e);
		}
	return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getICPCTemplateMappingList(ICPCTemplateMappingDto iCPCTemplateMappingDto)
			throws ApplicationException {
		try {
			List<ICPCTemplateMappingDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("ICPC_TEMPLATE_MAPPING_LIST")
					.setParameter("orgId",  iCPCTemplateMappingDto.getOrgId())
					.setParameter("orgUnitId",  iCPCTemplateMappingDto.getOrgUnitId())
					.setFirstResult(iCPCTemplateMappingDto.getOffset() != null ? iCPCTemplateMappingDto.getOffset()  : 0)
					.setMaxResults(iCPCTemplateMappingDto.getNoOfRecordsPerPage() != null ? iCPCTemplateMappingDto.getNoOfRecordsPerPage()  : 10)
					.setResultTransformer(Transformers.aliasToBean(ICPCTemplateMappingDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
		logger.error("getICPCTemplateMappingList failed", he);
		} catch (Exception e) {
		logger.error("getICPCTemplateMappingList failed", e);
		}
	return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getListOfICPCSystems() throws ApplicationException {
		try {
			List<ICPCTemplateMappingDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_LIST_OF_ICPC_SYSTEMS")
					.setResultTransformer(Transformers.aliasToBean(ICPCTemplateMappingDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
		logger.error("getListOfICPCSystems failed", he);
		} catch (Exception e) {
		logger.error("getListOfICPCSystems failed", e);
		}
	return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getDetailedListOfICPCSystems(ICPCTemplateMappingDto iCPCTemplateMappingDto)
			throws ApplicationException {
		try {
			List<ICPCTemplateMappingDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DETAILED_LIST_OF_ICPC_SYSTEMS")
					.setParameter("orgId", iCPCTemplateMappingDto.getOrgId())
					.setParameter("orgUnitId", iCPCTemplateMappingDto.getOrgUnitId())
					.setParameter("templateId", iCPCTemplateMappingDto.getTemplateId())
					.setResultTransformer(Transformers.aliasToBean(ICPCTemplateMappingDto.class)).list();
			
			List<ICPCTemplateMappingDto> icpcList=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ICPC_LIST_BY_TEMPLATE_ID")
					.setParameter("orgId", iCPCTemplateMappingDto.getOrgId())
					.setParameter("orgUnitId", iCPCTemplateMappingDto.getOrgUnitId())
					.setParameter("templateId", iCPCTemplateMappingDto.getTemplateId())
					.setResultTransformer(Transformers.aliasToBean(ICPCTemplateMappingDto.class)).list();
			iCPCTemplateMappingDto.setSelectedICPCList(icpcList);
			
			return new Response(SUCCESS, SUCCESS_CODE, null, list, iCPCTemplateMappingDto);
		} catch (HibernateException he) {
		logger.error("getDetailedListOfICPCSystems failed", he);
		} catch (Exception e) {
		logger.error("getDetailedListOfICPCSystems failed", e);
		}
	return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getDetailedListOfICPCSystemsDescription(ICPCTemplateMappingDto iCPCTemplateMappingDto)
			throws ApplicationException {
		try {
			List<ICPCTemplateMappingDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DETAILED_LIST_OF_ICPC_SYSTEMS_DESCRIPTION")
					.setParameter("orgId", iCPCTemplateMappingDto.getOrgId())
					.setParameter("orgUnitId", iCPCTemplateMappingDto.getOrgUnitId())
					.setParameter("templateId", iCPCTemplateMappingDto.getTemplateId())
					.setResultTransformer(Transformers.aliasToBean(ICPCTemplateMappingDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
		logger.error("getDetailedListOfICPCSystemsDescription failed", he);
		} catch (Exception e) {
		logger.error("getDetailedListOfICPCSystemsDescription failed", e);
		}
	return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getICPCTemplateMappingCount(ICPCTemplateMappingDto iCPCTemplateMappingDto)
			throws ApplicationException {
		try{
			BigInteger iCPCTemplateMappingCount = (BigInteger) sessionFactory.getCurrentSession()
				.getNamedQuery("GET_ICPC_TEMPLATE_MAPPING_COUNT")
				.setParameter("orgId", iCPCTemplateMappingDto.getOrgId())
				.setParameter("orgUnitId", iCPCTemplateMappingDto.getOrgUnitId())
				.uniqueResult();
			if (iCPCTemplateMappingCount.compareTo(BigInteger.ZERO) == 1) {
				return new Response(SUCCESS, SUCCESS_CODE, null, null, iCPCTemplateMappingCount);
			} else {
				return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, BigInteger.ZERO);
			}
	} catch (HibernateException he) {
	logger.error("getICPCTemplateMappingCount failed", he);
	} catch (Exception e) {
	logger.error("getICPCTemplateMappingCount failed", e);
	}
	return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateICPCTemplateMappingStatus(ICPCTemplateMappingDto iCPCTemplateMappingDto)
			throws ApplicationException {
		try{
			if (iCPCTemplateMappingDto.getTemplateId() != 0) {

				Integer result = sessionFactory.getCurrentSession()
						.getNamedQuery("UPDATE_ICPC_TEMPLATE_MAPPING_STATUS")
						.setParameter("orgId", iCPCTemplateMappingDto.getOrgId())
						.setParameter("orgUnitId", iCPCTemplateMappingDto.getOrgUnitId())
						.setParameter("status", iCPCTemplateMappingDto.getStatus())
						.setParameter("templateId", iCPCTemplateMappingDto.getTemplateId())
						.executeUpdate();
				if (result > 0) {
					if ( iCPCTemplateMappingDto.getStatus().equals(ACTIVE)) {
						return new Response(SUCCESS,SUCCESS_CODE, COMMON_UPDATE, null, null);
					} else if ( iCPCTemplateMappingDto.getStatus().equals(INACTIVE)) {
						return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
					} else if ( iCPCTemplateMappingDto.getStatus().equals(REMOVED)) {
						return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
					}
				} else {
					return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
				}

			} else {
				return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}
	} catch (HibernateException he) {
	logger.error("updateICPCTemplateMappingStatus failed", he);
	} catch (Exception e) {
	logger.error("updateICPCTemplateMappingStatus failed", e);
	}
	return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

	@Override
	public Response checkICPCTemplateMappingAvailable(ICPCTemplateMappingDto iCPCTemplateMappingDto)
			throws ApplicationException {
		try {
			List<ICPCTemplateMappingDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("CHECK_ICPC_TEMPLATE_MAPPING_AVAILABLE")
					.setParameter("orgId", iCPCTemplateMappingDto.getOrgId())
					.setParameter("orgUnitId", iCPCTemplateMappingDto.getOrgUnitId())
					.setParameter("templateId", iCPCTemplateMappingDto.getTemplateId())
					.setResultTransformer(Transformers.aliasToBean(ICPCTemplateMappingDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
		logger.error("checkICPCTemplateMappingAvailable failed", he);
		} catch (Exception e) {
		logger.error("checkICPCTemplateMappingAvailable failed", e);
		}
	return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getICPCSystemsCodesBySystemId(ICPCTemplateMappingDto iCPCTemplateMappingDto)
			throws ApplicationException {
		try {
			List<ICPCTemplateMappingDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ICPC_SYSTEMS_CODES_BY_SYSTEM_ID")
					.setParameterList("systemIdList", iCPCTemplateMappingDto.getSystemIdList())
					.setResultTransformer(Transformers.aliasToBean(ICPCTemplateMappingDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
		logger.error("getICPCSystemsCodesBySystemId failed", he);
		} catch (Exception e) {
		logger.error("getICPCSystemsCodesBySystemId failed", e);
		}
	return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getTemplateListForICPC(ICPCTemplateMappingDto iCPCTemplateMappingDto) throws ApplicationException {
		try {
			List<ICPCTemplateMappingDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_TEMPLATE_LIST_FOR_ICPC")
					.setParameter("orgId", iCPCTemplateMappingDto.getOrgId())
					.setParameter("orgUnitId", iCPCTemplateMappingDto.getOrgUnitId())
					.setResultTransformer(Transformers.aliasToBean(ICPCTemplateMappingDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
		logger.error("getTemplateListForICPC failed", he);
		} catch (Exception e) {
		logger.error("getTemplateListForICPC failed", e);
		}
	return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
}
