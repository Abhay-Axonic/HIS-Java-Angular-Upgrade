package com.param.adt.master.global.dao;

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

import com.param.adt.common.ADTCommonDateUtils;
import com.param.adt.common.ICommonConstants;
import com.param.adt.common.Response;
import com.param.adt.master.global.dto.IdentificationMasterDto;
import com.param.global.model.IdentificationMaster;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings({ "rawtypes", "unchecked" })
@Repository
public class IdentificationMasterDaoImpl extends GenericDao<IdentificationMaster, Integer>
		implements IIdentificationMasterDao, ICommonConstants {

	@Autowired
	private DozerBeanMapper mapper;

	@Autowired
	private SessionFactory sessionFactory;

	public IdentificationMasterDaoImpl() {
		super(IdentificationMaster.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Response getIdentificationByName(IdentificationMasterDto identificationMasterDto)
			throws ApplicationException {
		try {
			List<IdentificationMasterDto> identificationMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_IDENTIFICATION_LIST_BY_NAME")
					.setParameter("identificationName", identificationMasterDto.getIdentificationName().toLowerCase())
					.setParameter("orgId", identificationMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(IdentificationMasterDto.class)).list();
			return new Response(SUCCESS, null, null, identificationMasterDtoList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} finally {
			 ;
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response addIdentificationMaster(IdentificationMasterDto identificationMasterDto)
			throws ApplicationException {
		try{
			IdentificationMaster identificationMaster = mapper.map(identificationMasterDto, IdentificationMaster.class,"IdentificationMasterDto_to_IdentificationMaster");
			identificationMaster.setCreatedDate(TimeZoneComponent.getDateByzone(0));
			identificationMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(0));
			identificationMaster.setStatus('A');
			identificationMaster.setIsExpiryRequired('N');
			identificationMaster.setIsAlphanumeric('N');
			identificationMaster.setIsNumerical('N');
			identificationMaster = save(identificationMaster);
				return new Response(SUCCESS, null, null, null,identificationMaster);
			}catch(HibernateException he){
			}catch(Exception e){
				
			}
			return new Response(ERROR, null, null, null,null);
	}

	@Override
	public Response getIdentificationMasterList(IdentificationMasterDto identificationMasterDto) throws ApplicationException {
		try {
			List<IdentificationMasterDto> identificationMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_IDENTIFICATION_LIST")
					.setFirstResult(identificationMasterDto.getOffset() != null ? identificationMasterDto.getOffset() : 0)
					.setMaxResults(identificationMasterDto.getNoOfRecordsPerPage() != null ? identificationMasterDto.getNoOfRecordsPerPage() : 10)
					.setParameter("orgId", identificationMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(IdentificationMasterDto.class)).list();
			return new Response(SUCCESS, null, null, identificationMasterDtoList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} finally {
			 ;
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getIdentificationByNameNotId(IdentificationMasterDto identificationMasterDto)
			throws ApplicationException {
		try {
			List<IdentificationMasterDto> identificationMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_IDENTIFICATION_LIST_BY_NAME_NOT_ID")
					.setParameter("identificationId", identificationMasterDto.getIdentificationId())
					.setParameter("identificationName", identificationMasterDto.getIdentificationName().toLowerCase())
					.setParameter("orgId", identificationMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(IdentificationMasterDto.class)).list();
			return new Response(SUCCESS, null, null, identificationMasterDtoList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} finally {
			 ;
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateIdentificationMaster(IdentificationMasterDto identificationMasterDto)
			throws ApplicationException {
		try {

			IdentificationMaster identificationMaster = findById(identificationMasterDto.getIdentificationId());
			identificationMaster.setIdentificationName(identificationMasterDto.getIdentificationName());
			identificationMaster.setIdentificationCode(identificationMasterDto.getIdentificationCode());
			identificationMaster.setDefinedLength(identificationMasterDto.getDefinedLength());
			identificationMaster.setIsAlphanumeric(identificationMasterDto.getIsAlphanumeric());
			identificationMaster.setIsNumerical(identificationMasterDto.getIsNumerical());
			identificationMaster.setUpdatedBy(identificationMasterDto.getUpdatedBy());
			identificationMaster.setIsExpiryRequired(identificationMasterDto.getIsExpiryRequired());
			identificationMaster.setUpdatedDate(
					ADTCommonDateUtils.getDate(identificationMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			update(identificationMaster);
			return new Response<IdentificationMasterDto, Integer>(SUCCESS, null, COMMON_SUCCESS, null, identificationMaster);

		} catch (HibernateException he) {
			return new Response<IdentificationMasterDto, Integer>(ERROR, null, null, null, null);
		} catch (Exception e) {
			
			return new Response<IdentificationMasterDto, Integer>(ERROR, null, null, null, null);
		} finally {
			 ;
		}
	}

	@Override
	public Response getIdentificationById(IdentificationMasterDto identificationMasterDto) throws ApplicationException {
		try {
			List<IdentificationMasterDto> identificationMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_IDENTIFICATION_LIST_BY_ID")
					.setParameter("identificationId", identificationMasterDto.getIdentificationId())
					.setResultTransformer(Transformers.aliasToBean(IdentificationMasterDto.class)).list();
			return new Response(SUCCESS, null, null, identificationMasterDtoList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} finally {
			 ;
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateIdentificationStatus(IdentificationMasterDto identificationMasterDto)
			throws ApplicationException {
		try {

			IdentificationMaster identificationMaster = findById(identificationMasterDto.getIdentificationId());
			identificationMaster.setUpdatedBy(identificationMasterDto.getUpdatedBy());
			identificationMaster.setStatus(identificationMasterDto.getStatus() == ACTIVE ? ACTIVE : INACTIVE);
			identificationMaster.setUpdatedDate(
					ADTCommonDateUtils.getDate(identificationMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			update(identificationMaster);
			return new Response<IdentificationMasterDto, Integer>(SUCCESS, null, COMMON_SUCCESS, null, identificationMaster.getOrganizationId());

		} catch (HibernateException he) {
			return new Response<IdentificationMasterDto, Integer>(ERROR, null, null, null, null);
		} catch (Exception e) {
			
			return new Response<IdentificationMasterDto, Integer>(ERROR, null, null, null, null);
		} finally {
			 ;
		}
	}

	@Override
	public Response getActiveIdentificationList(Integer orgId) throws ApplicationException {
		try {
			List<IdentificationMasterDto> identificationMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_IDENTIFICATION_LIST")
					.setParameter("orgId", orgId)
					.setResultTransformer(Transformers.aliasToBean(IdentificationMasterDto.class)).list();
			return new Response(SUCCESS, null, null, identificationMasterDtoList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} finally {
			 ;
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@Override
	public Response getCount(IdentificationMasterDto identificationMasterDto)
	{
		try{
			Session session=sessionFactory.getCurrentSession();
			Criteria c=session.createCriteria(IdentificationMaster.class); 
			c.add(Restrictions.eq("organizationId", identificationMasterDto.getOrganizationId()));
			c.setProjection(Projections.rowCount());  
			Long count = (Long)c.uniqueResult();
			return new Response(SUCCESS, null, null, null, count);
		}catch(Exception e)
		{
			
			return new Response(ERROR, null, null, null, null);
		}
	}


}
