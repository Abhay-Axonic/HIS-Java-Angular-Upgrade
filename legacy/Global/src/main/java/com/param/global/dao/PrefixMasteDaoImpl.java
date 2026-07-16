package com.param.global.dao;

import java.util.List;

import org.dozer.DozerBeanMapper;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.DoctorMasterDto;
import com.param.global.dto.PrefixMasterDto;
import com.param.global.model.PrefixMaster;
import com.param.global.org.dto.UnitMasterDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class PrefixMasteDaoImpl extends GenericDao<PrefixMaster, Integer>
		implements IPrefixMasterDao, ICommonConstants {
	final static Logger logger = Logger.getLogger(PrefixMasteDaoImpl.class);


	public PrefixMasteDaoImpl() {
		super(PrefixMaster.class);
		// TODO Auto-generated constructor stub
	}

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	DozerBeanMapper mapper;

	@Override
	public Response getPrefixByName(PrefixMasterDto prefixMasterDto) throws ApplicationException {
		try {
			List<PrefixMasterDto> prefixMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PREFIX_LIST_BY_NAME")
					.setParameter("prefixName", prefixMasterDto.getPrefixDesc().toLowerCase())
					.setParameter("orgId", prefixMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(PrefixMasterDto.class)).list();
			return new Response(SUCCESS, null, null, prefixMasterDtoList, null);

		} catch (HibernateException he) {
		logger.error("getPrefixByName failed", he);
		} catch (Exception e) {
		logger.error("getPrefixByName failed", e);
		} finally {
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response addPrefixTypeMaster(PrefixMasterDto prefixMasterDto) throws ApplicationException {
		try {
			PrefixMaster prefixMaster = mapper.map(prefixMasterDto, PrefixMaster.class, "PrefixMasterDto_to_PrefixMaster");
			prefixMaster.setCreatedDate(TimeZoneComponent.getDateByzone(0));
			prefixMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(0));
			prefixMaster = save(prefixMaster);
			return new Response(SUCCESS, null, null, null, prefixMaster);
		} catch (HibernateException he) {
		logger.error("addPrefixTypeMaster failed", he);
		} catch (Exception e) {
		logger.error("addPrefixTypeMaster failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getPrefixByNameNotId(PrefixMasterDto prefixMasterDto) throws ApplicationException {
		try {
			List<PrefixMasterDto> prefixMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PREFIX_LIST_BY_NAME_NOT_ID")
					.setParameter("prefixId", prefixMasterDto.getPrefixId())
					.setParameter("prefixName", prefixMasterDto.getPrefixDesc().toLowerCase())
					.setParameter("orgId", prefixMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(PrefixMasterDto.class)).list();
			return new Response(SUCCESS, null, null, prefixMasterDtoList, null);

		} catch (HibernateException he) {
		logger.error("getPrefixByNameNotId failed", he);
		} catch (Exception e) {
		logger.error("getPrefixByNameNotId failed", e);
		} finally {
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updatePrefixMaster(PrefixMasterDto prefixMasterDto) throws ApplicationException {
		try {
			PrefixMaster prefixMaster = findById(prefixMasterDto.getPrefixId());
			prefixMaster.setPrefixCode(prefixMasterDto.getPrefixCode());
			prefixMaster.setPrefixDesc(prefixMasterDto.getPrefixDesc());
			prefixMaster.setUpdatedBy(prefixMasterDto.getUpdatedBy());
			prefixMaster.setId(prefixMasterDto.getId());
			prefixMaster.setIsShowPatientRegistration(prefixMasterDto.getIsShowPatientRegistration());
			prefixMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(prefixMasterDto.getUpdatedDate(),"dd-M-yyyy HH:mm:ss"));
			update(prefixMaster);
			return new Response<PrefixMasterDto, Integer>(SUCCESS, null, COMMON_SUCCESS, null, prefixMaster);

		} catch (HibernateException he) {
		logger.error("updatePrefixMaster failed", he);
		return new Response<PrefixMasterDto, Integer>(ERROR, null, null, null, null);
		} catch (Exception e) {
		logger.error("updatePrefixMaster failed", e);
		return new Response<PrefixMasterDto, Integer>(ERROR, null, null, null, null);
		} finally {
		}
	}

	@Override
	public Response getPrefixByID(PrefixMasterDto prefixMasterDto) throws ApplicationException {
		try {
			List<PrefixMasterDto> prefixMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PREFIX_LIST_BY_ID").setParameter("prefixId", prefixMasterDto.getPrefixId())
					.setResultTransformer(Transformers.aliasToBean(PrefixMasterDto.class)).list();
			return new Response(SUCCESS, null, null, prefixMasterDtoList, null);

		} catch (HibernateException he) {
		logger.error("getPrefixByID failed", he);
		} catch (Exception e) {
		logger.error("getPrefixByID failed", e);
		} finally {
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updatePrefixStatus(PrefixMasterDto prefixMasterDto) throws ApplicationException {
		try {
			PrefixMaster prefixMaster = findById(prefixMasterDto.getPrefixId());
			
			prefixMaster.setUpdatedBy(prefixMasterDto.getUpdatedBy());
			prefixMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(prefixMasterDto.getUpdatedDate(),"dd-M-yyyy HH:mm:ss"));
			prefixMaster.setStatus(prefixMasterDto.getStatus() == ACTIVE ? ACTIVE : INACTIVE);
			update(prefixMaster);
			return new Response<PrefixMasterDto, Integer>(SUCCESS, null, COMMON_SUCCESS, null, prefixMaster.getOrganizationId());

		} catch (HibernateException he) {
		logger.error("updatePrefixStatus failed", he);
		return new Response<PrefixMasterDto, Integer>(ERROR, null, null, null, null);
		} catch (Exception e) {
		logger.error("updatePrefixStatus failed", e);
		return new Response<PrefixMasterDto, Integer>(ERROR, null, null, null, null);
		} finally {
		}
	}

	@Override
	public Response getActivePrefixList(Integer orgId) throws ApplicationException {
		try {
			List<PrefixMasterDto> prefixMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_PREFIX_LIST")
					.setParameter("orgId", orgId)
					.setResultTransformer(Transformers.aliasToBean(PrefixMasterDto.class)).list();
			return new Response(SUCCESS, null, null, prefixMasterDtoList, null);

		} catch (HibernateException he) {
		logger.error("getActivePrefixList failed", he);
		} catch (Exception e) {
		logger.error("getActivePrefixList failed", e);
		} finally {
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getPrefixMasterList(PrefixMasterDto prefixMasterDto) throws ApplicationException {
		try {
			List<PrefixMasterDto> prefixMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PREFIX_LIST")
					.setFirstResult(prefixMasterDto.getOffset() != null ? prefixMasterDto.getOffset() : 0)
					.setMaxResults(prefixMasterDto.getNoOfRecordsPerPage() != null ? prefixMasterDto.getNoOfRecordsPerPage() : 10)
					.setParameter("orgId", prefixMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(PrefixMasterDto.class)).list();
			return new Response(SUCCESS, null, null, prefixMasterDtoList, null);

		} catch (HibernateException he) {
		logger.error("getPrefixMasterList failed", he);
		} catch (Exception e) {
		logger.error("getPrefixMasterList failed", e);
		} finally {
		}
		return new Response(ERROR, null, null, null, null);
	}
	

	@Override
	public Response getCount(PrefixMasterDto prefixMasterDto) throws ApplicationException {
		try{
			Session session=sessionFactory.getCurrentSession();
			Criteria c=session.createCriteria(PrefixMaster.class); 
			c.add(Restrictions.eq("organizationId", prefixMasterDto.getOrganizationId()));
			c.setProjection(Projections.rowCount());  
			Long count = (Long)c.uniqueResult();
			return new Response(SUCCESS, null, null, null, count);
		}catch(Exception e)
		{
		logger.error("getCount failed", e);
			return new Response(ERROR, null, null, null, null);
		}
	}

	@Override
	public Response getActivePrefixListForWebPage(PrefixMasterDto prefixMasterDto) throws ApplicationException {
		try {
			List<PrefixMasterDto> prefixMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_PREFIX_LIST")
					.setParameter("orgId", prefixMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(PrefixMasterDto.class)).list();
			return new Response(SUCCESS, null, null, prefixMasterDtoList, null);

		} catch (HibernateException he) {
		logger.error("getActivePrefixListForWebPage failed", he);
		} catch (Exception e) {
		logger.error("getActivePrefixListForWebPage failed", e);
		} finally {
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getPrefixMasterAndMedallPrefixMaster(PrefixMasterDto prefixMasterDto) throws ApplicationException {

		 try{
			 List<PrefixMasterDto> OTTDetailsObj = sessionFactory.getCurrentSession().createSQLQuery(
					" select prefix_id as \"prefixId\" ,medall_salutation_id_prefix_id as \"medallSalutationIdPrefixId\"  from m_prefix_master mpm where organization_id  =:organizationId  and status  = 'A'")
					 .setParameter("organizationId",prefixMasterDto.getOrganizationId())
					 .setResultTransformer(Transformers.aliasToBean(PrefixMasterDto.class)).list();
			 
			 UnitMasterDto obj = (UnitMasterDto) sessionFactory.getCurrentSession().createSQLQuery(
						" select medall_centerid as \"medallCenterId\" from m_unit_master mum  where unit_id  =:unitId ")
					    .setParameter("unitId",prefixMasterDto.getUnitId())
						.setResultTransformer(Transformers.aliasToBean(UnitMasterDto.class)).uniqueResult();
			 
			 List<DoctorMasterDto> docList = sessionFactory.getCurrentSession().createSQLQuery(
						"select doctor_id as \"doctorId\" ,medall_referalid as \"medallReferalId\"  from  doctor.m_doctor_master mdm  where medall_referalid is not null  and  organization_id =:organizationId ")
					    .setParameter("organizationId",prefixMasterDto.getOrganizationId())
						.setResultTransformer(Transformers.aliasToBean(DoctorMasterDto.class)).list();
			 
				return new Response<>(SUCCESS, SUCCESS_CODE, null, OTTDetailsObj, obj,docList);
	        } catch (Exception e)
	        {
	        logger.error("getPrefixMasterAndMedallPrefixMaster failed", e);
	        }
	        return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);

	}


}
