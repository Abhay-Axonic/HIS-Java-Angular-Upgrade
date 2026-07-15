package com.param.global.dao;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import com.param.global.dto.ICPCMapperDto;
import com.param.global.model.ICPCGenderMapper;
import com.param.global.model.ICPCMapper;
import com.param.opd.coversheet.dto.ICPCSystemDTO;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({ "unchecked", "rawtypes" })
public class ICPCMapperDaoImpl extends GenericDao<ICPCMapper, Integer>implements IICPCMapperDao, ICommonConstants {
	final static Logger logger = Logger.getLogger(ICPCMapperDaoImpl.class);



	public ICPCMapperDaoImpl() {
	super(ICPCMapper.class);
	}
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	 SessionService sessionService;

	@Override
	public Response saveICPCMapper(ICPCMapperDto iCPCMapperDto) throws ApplicationException {
		try {
			ICPCMapper iCPCMapper = new ICPCMapper();
			iCPCMapper.setIcpcSystemId(iCPCMapperDto.getIcpcSystemId());
			iCPCMapper.setStatus(iCPCMapperDto.getStatus());
			iCPCMapper.setOrganizationId(iCPCMapperDto.getOrganizationId());
			iCPCMapper.setUnitId(iCPCMapperDto.getUnitId());
			iCPCMapper.setCreatedBy(iCPCMapperDto.getCreatedBy());
			iCPCMapper.setUpdatedBy(iCPCMapperDto.getUpdatedBy());
			iCPCMapper.setCreatedDate(TimeZoneComponent.getDateByzone(iCPCMapperDto.getUnitId()));
			iCPCMapper.setUpdatedDate(TimeZoneComponent.getDateByzone(iCPCMapperDto.getUnitId()));
			 sessionFactory.getCurrentSession().save(iCPCMapper);
			
			
			  for(ICPCMapperDto dto:iCPCMapperDto.getGenderList()){
				  ICPCGenderMapper iCPCGenderMapper= new ICPCGenderMapper();
				  iCPCGenderMapper.setIcpcSystemMapperId(iCPCMapper.getIcpcSystemMapperId());
				  iCPCGenderMapper.setGenderId(dto.getId());
				  iCPCGenderMapper.setUnitId(iCPCMapperDto.getUnitId());
				  iCPCGenderMapper.setOrganizationId(iCPCMapperDto.getOrganizationId());
				  iCPCGenderMapper.setCreatedBy(iCPCMapperDto.getCreatedBy());
				  iCPCGenderMapper.setStatus('A');
				  iCPCGenderMapper.setCreatedDate(TimeZoneComponent.getDateByzone(iCPCMapperDto.getUnitId()));
				  iCPCGenderMapper.setUpdatedBy(iCPCMapperDto.getCreatedBy());
				  iCPCGenderMapper.setUpdatedDate(TimeZoneComponent.getDateByzone(iCPCMapperDto.getUnitId()));
				  sessionFactory.getCurrentSession().save(iCPCGenderMapper);

				   }
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		} catch (HibernateException he) {
		logger.error("saveICPCMapper failed", he);
		} catch (Exception e) {
		logger.error("saveICPCMapper failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response updateICPCMapper(ICPCMapperDto iCPCMapperDto) throws ApplicationException {
		try {
			ICPCMapper iCPCMapper = new ICPCMapper();
			iCPCMapper.setIcpcSystemMapperId(iCPCMapperDto.getIcpcSystemMapperId());
			iCPCMapper.setIcpcSystemId(iCPCMapperDto.getIcpcSystemId());
			iCPCMapper.setStatus(iCPCMapperDto.getStatus());
			iCPCMapper.setOrganizationId(iCPCMapperDto.getOrganizationId());
			iCPCMapper.setUnitId(iCPCMapperDto.getUnitId());
			iCPCMapper.setCreatedBy(iCPCMapperDto.getCreatedBy());
			iCPCMapper.setUpdatedBy(iCPCMapperDto.getUpdatedBy());
			iCPCMapper.setCreatedDate(TimeZoneComponent.getDateByzone(iCPCMapperDto.getUnitId()));
			iCPCMapper.setUpdatedDate(TimeZoneComponent.getDateByzone(iCPCMapperDto.getUnitId()));
			 sessionFactory.getCurrentSession().update(iCPCMapper);
			
			 Integer count=sessionFactory.getCurrentSession().createSQLQuery(" update emr.t_icpc_system_gender_mapper gm set status='I' where 	gm.icpc_system_mapper_id=:icpcSystemMapperId "	)
		     			.setInteger("icpcSystemMapperId", iCPCMapperDto.getIcpcSystemMapperId()).executeUpdate();
			 
			  for(ICPCMapperDto dto:iCPCMapperDto.getGenderList()){
				  ICPCGenderMapper iCPCGenderMapper= new ICPCGenderMapper();
				  iCPCGenderMapper.setIcpcSystemMapperId(iCPCMapper.getIcpcSystemMapperId());
				  iCPCGenderMapper.setGenderId(dto.getId());
				  iCPCGenderMapper.setUnitId(iCPCMapperDto.getUnitId());
				  iCPCGenderMapper.setOrganizationId(iCPCMapperDto.getOrganizationId());
				  iCPCGenderMapper.setCreatedBy(iCPCMapperDto.getCreatedBy());
				  iCPCGenderMapper.setStatus('A');
				  iCPCGenderMapper.setCreatedDate(TimeZoneComponent.getDateByzone(iCPCMapperDto.getUnitId()));
				  iCPCGenderMapper.setUpdatedBy(iCPCMapperDto.getCreatedBy());
				  iCPCGenderMapper.setUpdatedDate(TimeZoneComponent.getDateByzone(iCPCMapperDto.getUnitId()));
				  sessionFactory.getCurrentSession().save(iCPCGenderMapper);

				   }
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (HibernateException he) {
		logger.error("updateICPCMapper failed", he);
		} catch (Exception e) {
		logger.error("updateICPCMapper failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response updateICPCMapperStatus(ICPCMapperDto iCPCMapperDto) throws ApplicationException {
		try{
			if (iCPCMapperDto.getIcpcSystemMapperId() != 0) {

				Integer result = sessionFactory.getCurrentSession()
						.getNamedQuery("UPDATE_ICPC_MAPPER_STATUS")
						.setInteger("orgId", iCPCMapperDto.getOrganizationId())
						.setCharacter("status", iCPCMapperDto.getStatus())
						.setInteger("icpcSystemMapperId", iCPCMapperDto.getIcpcSystemMapperId())
						.executeUpdate();
				if (result > 0) {
					if ( iCPCMapperDto.getStatus().equals(ACTIVE)) {
						return new Response(SUCCESS,SUCCESS_CODE, COMMON_UPDATE, null, null);
					} else if ( iCPCMapperDto.getStatus().equals(INACTIVE)) {
						return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
					} else if ( iCPCMapperDto.getStatus().equals(REMOVED)) {
						return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
					}
				} else {
					return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
				}

			} else {
				return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}
	} catch (HibernateException he) {
	logger.error("updateICPCMapperStatus failed", he);
	} catch (Exception e) {
	logger.error("updateICPCMapperStatus failed", e);
	}
	return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getICPCMapperById(ICPCMapperDto iCPCMapperDto) throws ApplicationException {
		/*try {
			List<ICPCMapperDto> listSystemMaster =sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ICPC_MAPPER_BY_ID")
					.setInteger("icpcSystemMapperId", iCPCMapperDto.getIcpcSystemMapperId())
					.setInteger("orgId", iCPCMapperDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(ICPCMapperDto.class)).list();
			
			
			List<ICPCMapperDto> list = 	sessionFactory.getCurrentSession()
				     .createSQLQuery(" select igm.icpc_system_mapper_id as\"icpcSystemMapperId\",igm.gender_id as\"id\",gm.gender_name as\"genderDesc\"" + 
				     		" from  emr.t_icpc_system_gender_mapper igm" + 
				     		" inner join public.m_gender_master gm  on  igm.gender_id=gm.gender_id " + 
				     		" WHERE igm.organization_id =:orgId" + 
				     		" AND  igm.unit_id =:unitId " + 
				     		" AND igm.status ='A' " + 
				     		" AND igm.icpc_system_mapper_id =:icpcSystemMapperId  ") 
				        .setInteger("orgId", iCPCMapperDto.getOrganizationId())
						.setInteger("unitId", iCPCMapperDto.getUnitId())
					    .setInteger("icpcSystemMapperId", iCPCMapperDto.getIcpcSystemMapperId())
						.setResultTransformer(Transformers.aliasToBean(ICPCMapperDto.class)).list();
			return new Response(SUCCESS, null, null, listSystemMaster, null);
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}*/
		
		try {
			List<ICPCMapperDto> list2=new ArrayList<>();
			List<ICPCMapperDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ICPC_MAPPER_BY_ID")
					.setInteger("orgId", iCPCMapperDto.getOrganizationId())
					.setInteger("icpcSystemMapperId", iCPCMapperDto.getIcpcSystemMapperId())
					.setResultTransformer(Transformers.aliasToBean(ICPCMapperDto.class)).list();
			
	for(ICPCMapperDto dto : list) {
		List<ICPCMapperDto> list1=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_GENDER_LIST_BY_ICPC_SYSTEM_ID")
				.setInteger("icpcSystemMapperId", dto.getIcpcSystemMapperId())
				.setInteger("orgId", iCPCMapperDto.getOrganizationId())
				.setResultTransformer(Transformers.aliasToBean(ICPCMapperDto.class)).list();
		dto.setGenderList(list1);
		list2.add(dto);
	}
		
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getICPCMapperById failed", he);
		} catch (Exception e) {
		logger.error("getICPCMapperById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getICPCMapperList(ICPCMapperDto iCPCMapperDto) throws ApplicationException {
		try {
			List<ICPCMapperDto> list =sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ICPC_MAPPER_LIST")
					.setInteger("orgId", iCPCMapperDto.getOrganizationId())
					.setFirstResult(iCPCMapperDto.getOffset() != null ? iCPCMapperDto.getOffset()  : 0)
					.setMaxResults(iCPCMapperDto.getNoOfRecordsPerPage() != null ? iCPCMapperDto.getNoOfRecordsPerPage()  : 10)
					.setResultTransformer(Transformers.aliasToBean(ICPCMapperDto.class)).list();
		
			return new Response(SUCCESS, null, null, list, null);
		} catch (Exception e) {
		logger.error("getICPCMapperList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getICPCMapperCount(ICPCMapperDto iCPCMapperDto) throws ApplicationException {
		try {
			
			Session session = sessionFactory.getCurrentSession();
			Criteria crit = session.createCriteria(ICPCMapper.class);
			crit.add(Restrictions.eq("organizationId", iCPCMapperDto.getOrganizationId()));
			crit.setProjection(Projections.rowCount());
			Long count = (Long) crit.uniqueResult();
			
			return new Response(SUCCESS, SUCCESS_CODE, null, null, count);
				
		} catch (HibernateException he) {
		logger.error("getICPCMapperCount failed", he);
		} catch (Exception e) {
		logger.error("getICPCMapperCount failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}



	@Override
	public Response getActiveICPCMapperList(ICPCMapperDto iCPCMapperDto) throws ApplicationException {
		try {
			List<ICPCMapperDto> list =sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_ICPC_MAPPER_LIST")
					.setInteger("orgId", iCPCMapperDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(ICPCMapperDto.class)).list();
		
			return new Response(SUCCESS, null, null, list, null);
		} catch (Exception e) {
		logger.error("getActiveICPCMapperList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getGenderListByICPCSystemId(ICPCMapperDto iCPCMapperDto) throws ApplicationException {
		try {
			List<ICPCMapperDto> list =sessionFactory.getCurrentSession()
					.getNamedQuery("GET_GENDER_LIST_BY_ICPC_SYSTEM_ID")
					.setInteger("icpcSystemMapperId", iCPCMapperDto.getIcpcSystemMapperId())
					.setInteger("orgId", iCPCMapperDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(ICPCMapperDto.class)).list();
		
			return new Response(SUCCESS, null, null, list, null);
		} catch (Exception e) {
		logger.error("getGenderListByICPCSystemId failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response checkICPCMapperAvailable(ICPCMapperDto iCPCMapperDto) throws ApplicationException {
		try {
			List<ICPCMapperDto> list =sessionFactory.getCurrentSession()
					.getNamedQuery("CHECK_ICPC_MAPPER_AVAILABLE")
					.setInteger("icpcSystemId", iCPCMapperDto.getIcpcSystemId())
					.setInteger("orgId", iCPCMapperDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(ICPCMapperDto.class)).list();
		
			return new Response(SUCCESS, null, null, list, null);
		} catch (Exception e) {
		logger.error("checkICPCMapperAvailable failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getSystemListForComplaintsOnCoversheet(ICPCMapperDto iCPCMapperDto) throws ApplicationException {
		/*try {
			List<ICPCMapperDto> list =sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SYSTEM_LIST_FOR_COMPLAINTS_ON_COVERSHEET")
					.setInteger("genderId", iCPCMapperDto.getGenderId())
					.setInteger("specialityId", iCPCMapperDto.getSpecialityId())
					.setInteger("orgId", iCPCMapperDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(ICPCMapperDto.class)).list();
		
			return new Response(SUCCESS, null, null, list, null);
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}*/
		
		try{
			String query=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SYSTEM_LIST_FOR_COMPLAINTS_ON_COVERSHEET").getQueryString();
			
			StringBuffer subQuery = new StringBuffer();
			if(iCPCMapperDto.getSpecialityId()!=null){
				subQuery.append(" and icodeMppr.speciality_id ="+iCPCMapperDto.getSpecialityId());
			}
		subQuery.append(" ORDER BY isys.icpc_system_desc ASC"); 
			
			query = query.concat(subQuery.toString());
		
			List<ICPCMapperDto> icpcMasterDtos=  sessionFactory.getCurrentSession()
					.createSQLQuery(query)
					//.setInteger("specialityId",iCPCCodesConfigurationMapperDto.getSpecialityId())
					.setInteger("orgId",iCPCMapperDto.getOrganizationId())
					.setInteger("genderId", iCPCMapperDto.getGenderId())
					.setResultTransformer(Transformers.aliasToBean(ICPCMapperDto.class)).list();
			
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, icpcMasterDtos, null);
		} catch (HibernateException he) {
		logger.error("getSystemListForComplaintsOnCoversheet failed", he);
		} catch (Exception e) {
		logger.error("getSystemListForComplaintsOnCoversheet failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}

//	@Override
//	public Response getActiveGenderListForICPCMapper(ICPCMapperDto iCPCMapperDto) throws ApplicationException {
//		try {
//			List<ICPCMapperDto> list =sessionFactory.getCurrentSession()
//					.getNamedQuery("GET_ACTIVE_GENDER_LIST_FOR_ICPC_MAPPER")
//					.setInteger("orgId", iCPCMapperDto.getOrganizationId())
//					.setResultTransformer(Transformers.aliasToBean(ICPCMapperDto.class)).list();
//		
//			return new Response(SUCCESS, null, null, list, null);
//		} catch (Exception e) {
//			
//		}
//		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
//	}

	@Override
	public Response getActiveGenderListForICPCMapper(HttpServletRequest request) {
		try {
			List<ICPCMapperDto> list =sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_GENDER_LIST_FOR_ICPC_MAPPER")
					.setInteger("orgId", sessionService.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(ICPCMapperDto.class)).list();
		
			return new Response(SUCCESS, null, null, list, null);
		} catch (Exception e) {
		logger.error("getActiveGenderListForICPCMapper failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getICPCByName(String icpcName) throws ApplicationException {
		try {
			List<ICPCSystemDTO> icpcMasterDtos = null;
			if(icpcName!=null ) {
				String query = "SELECT icpc.icpc_id  as \"icpcId\", "
						 + "icpc.icpc_code  as \"icpcCode\", "
						 + "icpc.icpc_desc  as \"icpcDesc\", "
						 + "icpc.icpc_system_id  as \"icpcSystemId\"  "
						 + "FROM emr.m_icpc icpc "
						 + "WHERE icpc.status='A' "
						 + "AND lower(icpc.icpc_desc) LIKE '%"+icpcName.toLowerCase().trim()+"%' ";
			
			icpcMasterDtos = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setResultTransformer(Transformers.aliasToBean(ICPCSystemDTO.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, icpcMasterDtos, null);
			}
		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
		logger.error("getICPCByName failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);	
	
	}

	@Override
	public Response getICPCByCode(String code) throws ApplicationException {
		try {
			List<ICPCSystemDTO> icpcMasterDtos = null;
			if(code!=null ) {
				String query = "SELECT icpc.icpc_id  as \"icpcId\", "
						+ "icpc.icpc_code  as \"icpcCode\", "
						+ "icpc.icpc_desc  as \"icpcDesc\", "
						+ "icpc.icpc_system_id  as \"icpcSystemId\"  "
						+ "FROM emr.m_icpc icpc "
						+ "WHERE icpc.status='A' "
						+ "AND (icpc.icpc_code)= :code  ";

				icpcMasterDtos = sessionFactory.getCurrentSession().createSQLQuery(query)
						.setParameter("code", code.trim())
						.setResultTransformer(Transformers.aliasToBean(ICPCSystemDTO.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, icpcMasterDtos, null);
			}
		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
		logger.error("getICPCByCode failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);

	}




}
