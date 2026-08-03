package com.param.CSSDMaster.dao;

import java.util.List;

import org.dozer.DozerBeanMapper;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.CSSDMaster.dto.AutoclaveMasterDto;
import com.param.CSSDMaster.model.AutoclaveMaster;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.service.VerifiedIdentityService;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class AutoclaveMasterDaoImpl extends GenericDao<AutoclaveMaster, Integer>
		implements IAutoclaveMasterDao, ICommonConstants {

	final static Logger logger = Logger.getLogger(AutoclaveMasterDaoImpl.class);


	public AutoclaveMasterDaoImpl() {
		super(AutoclaveMaster.class);
		// TODO Auto-generated constructor stub
	}

	@Autowired
	private DozerBeanMapper mapper;

	@Autowired
	private VerifiedIdentityService verifiedIdentityService;

	@Override
	public Response getAllAutoclaveList(Integer organizationId,Integer unitId,Integer offset,Integer noOfRecordsPerPage) throws ApplicationException {
		try {
			StatelessSession statelessSession = sessionFactory.openStatelessSession();

			List<AutoclaveMasterDto> MasterDtoList = statelessSession.getNamedQuery("GET_ALL_AUTOCLAVE_LIST")
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.setFirstResult(offset != null ?offset : 0)
					.setMaxResults(noOfRecordsPerPage != null ? noOfRecordsPerPage : 10)
					.setResultTransformer(Transformers.aliasToBean(AutoclaveMasterDto.class)).list();
			statelessSession.close();
			return new Response(SUCCESS, null, null, MasterDtoList, null);
		} catch (HibernateException he) {
			logger.error("getAllAutoclaveList failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getAllAutoclaveList failed", e);
			throw new ServiceException(e.getMessage());
		} finally {
		}
	}
	
	@Override
	public Response getActiveAutoclaveList(Integer organizationId,Integer unitId) throws ApplicationException {
		try {
			StatelessSession statelessSession = sessionFactory.openStatelessSession();

			List<AutoclaveMasterDto> MasterDtoList = statelessSession.getNamedQuery("GET_ALL_ACTIVE_AUTOCLAVE_LIST")
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(AutoclaveMasterDto.class)).list();
			statelessSession.close();
			return new Response(SUCCESS, null, null, MasterDtoList, null);
		} catch (HibernateException he) {
			logger.error("getActiveAutoclaveList failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getActiveAutoclaveList failed", e);
			throw new ServiceException(e.getMessage());
		} finally {
		}
	}

	@Override
	public Response updateAutoclave(AutoclaveMasterDto masterDto) {
		try {
			Session session=sessionFactory.getCurrentSession();
			AutoclaveMaster autoclaveMaster=(AutoclaveMaster)session.get(AutoclaveMaster.class, masterDto.getId());

			// BR-150: cross-tenant IDOR fix -- verify the loaded row belongs to the JWT-verified identity
			if (autoclaveMaster == null || autoclaveMaster.getOrganizationId() == null
					|| !autoclaveMaster.getOrganizationId().equals(verifiedIdentityService.getVerifiedOrganizationId())
					|| autoclaveMaster.getUnitId() == null
					|| !autoclaveMaster.getUnitId().equals(verifiedIdentityService.getVerifiedUnitId())) {
				return new Response(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}

			autoclaveMaster.setSterilization_pressure(masterDto.getSterilization_pressure());
			autoclaveMaster.setSterilization_temperature(masterDto.getSterilization_temperature());
			autoclaveMaster.setSterilization_type_id(masterDto.getSterilization_type_id());
			autoclaveMaster.setMachine_name(masterDto.getMachine_name());
			autoclaveMaster.setMachine_description(masterDto.getMachine_description());
			autoclaveMaster.setBiological_indicator_day(masterDto.getBiological_indicator_day());
			autoclaveMaster.setSterilization_color_id(masterDto.getSterilization_color_id());
			autoclaveMaster.setUpdated_date(TimeZoneComponent.getDateByzone(masterDto.getUnitId()!=null ?masterDto.getUnitId() :0));
			autoclaveMaster.setUpdated_by(masterDto.getUpdated_by());
			session.update(autoclaveMaster);
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null,null);
		} catch (Exception e) {
			logger.error("updateAutoclave failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public Response updateAutoclaveStatus(AutoclaveMasterDto masterDto) {
		try {
			Session session=sessionFactory.getCurrentSession();
			AutoclaveMaster autoclaveMaster=(AutoclaveMaster)session.get(AutoclaveMaster.class, masterDto.getId());

			// BR-150: cross-tenant IDOR fix -- same ownership check as updateAutoclave
			if (autoclaveMaster == null || autoclaveMaster.getOrganizationId() == null
					|| !autoclaveMaster.getOrganizationId().equals(verifiedIdentityService.getVerifiedOrganizationId())
					|| autoclaveMaster.getUnitId() == null
					|| !autoclaveMaster.getUnitId().equals(verifiedIdentityService.getVerifiedUnitId())) {
				return new Response(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}

			autoclaveMaster.setUpdated_date(TimeZoneComponent.getDateByzone(masterDto.getUnitId()!=null ?masterDto.getUnitId() :0));
			autoclaveMaster.setUpdated_by(masterDto.getUpdated_by());
			autoclaveMaster.setStatus(masterDto.getStatus());
			
			session.update(autoclaveMaster);
			
			if(masterDto.getStatus()=='A')
			{
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null,null);
			}
			else
			{
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_DELETE, null,null);
			}
		} catch (Exception e) {
			logger.error("updateAutoclaveStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response createAutoclave(AutoclaveMasterDto autoclaveMasterDto) throws ApplicationException {
		try {
			AutoclaveMaster autoclaveMaster = new AutoclaveMaster();
			autoclaveMaster.setMachine_name(autoclaveMasterDto.getMachine_name());
			autoclaveMaster.setMachine_description(autoclaveMasterDto.getMachine_description());
			autoclaveMaster.setSterilization_type_id(autoclaveMasterDto.getSterilization_type_id());
			autoclaveMaster.setBiological_indicator_day(autoclaveMasterDto.getBiological_indicator_day());
			autoclaveMaster.setSterilization_temperature(autoclaveMasterDto.getSterilization_temperature());
			autoclaveMaster.setSterilization_pressure(autoclaveMasterDto.getSterilization_pressure());
			autoclaveMaster.setSterilization_color_id(autoclaveMasterDto.getSterilization_color_id());
			autoclaveMaster.setCreated_by(autoclaveMasterDto.getCreated_by());
			autoclaveMaster.setUpdated_by(autoclaveMasterDto.getUpdated_by());
			autoclaveMaster.setOrganizationId(autoclaveMasterDto.getOrganizationId());
			autoclaveMaster.setUnitId(autoclaveMasterDto.getUnitId());
			autoclaveMaster.setCreated_date(TimeZoneComponent.getDateByzone(autoclaveMasterDto.getUnitId()));
			autoclaveMaster.setUpdated_date(TimeZoneComponent.getDateByzone(autoclaveMasterDto.getUnitId()));
			autoclaveMaster.setStatus('A');
			autoclaveMaster = save(autoclaveMaster);
			return new Response(SUCCESS, "201", "Created Successfully.", null, null);
		} catch (HibernateException he) {
			logger.error("createAutoclave failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("createAutoclave failed", e);
			throw new ServiceException(e.getMessage());
		} finally {
		}
	}

	@Override
	public Response getAutoclavesListcount(AutoclaveMasterDto autoclaveMasterDto) {
		try {
			Session session=sessionFactory.getCurrentSession();
			Criteria c=session.createCriteria(AutoclaveMaster.class); 
			c.add(Restrictions.eq("organizationId", autoclaveMasterDto.getOrganizationId()));
			c.setProjection(Projections.rowCount());  
			Long count = (Long)c.uniqueResult();
			return new Response(SUCCESS, null, null, null, count);

		} catch (HibernateException he) {
			logger.error("getAutoclavesListcount failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getAutoclavesListcount failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getActiveSterilizationColorList(AutoclaveMasterDto autoclaveMasterDto) {
		try {
			List<AutoclaveMasterDto> MasterDtoList = sessionFactory.getCurrentSession().createSQLQuery("select sterColor.color_id as \"colorId\",sterColor.color_name as \"colorName\" "
					+ " from m_sterilization_color_indicator sterColor "
					+ " ORDER BY sterColor.color_name ASC ")
					.setResultTransformer(Transformers.aliasToBean(AutoclaveMasterDto.class)).list();
			return new Response(SUCCESS, null, null, MasterDtoList, null);
		}catch (HibernateException he) {
			logger.error("getActiveSterilizationColorList failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getActiveSterilizationColorList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
}
