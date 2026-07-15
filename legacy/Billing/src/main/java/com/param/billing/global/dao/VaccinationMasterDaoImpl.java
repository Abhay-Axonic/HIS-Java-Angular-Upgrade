package com.param.billing.global.dao;

import java.util.ArrayList;

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

import com.param.billing.global.dto.VaccinationMasterDto;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.model.VaccinationMaster;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings({"rawtypes", "unchecked"})
@Repository
public class VaccinationMasterDaoImpl extends GenericDao<VaccinationMaster, Integer> implements VaccinationMasterDao,ICommonConstants
{

	public VaccinationMasterDaoImpl() {
		super(VaccinationMaster.class);
	}

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Response saveVaccinationMaster(VaccinationMasterDto vaccinationMasterDto) throws ApplicationException {
		try {
			 
			/*VaccinationMaster vaccinationMaster=new VaccinationMaster();
			vaccinationMaster.setVaccinationCode(vaccinationMasterDto.getVaccinationCode());
			//vaccinationMaster.setVaccinationDesc(vaccinationMasterDto.getVaccinationDesc());
			vaccinationMaster.setAdministrationId(vaccinationMasterDto.getAdministrationId());
			vaccinationMaster.setSiteId(vaccinationMasterDto.getSiteId());
			vaccinationMaster.setDoseId(vaccinationMasterDto.getDoseId());
			vaccinationMaster.setUomId(vaccinationMasterDto.getUomId());
			vaccinationMaster.setAge(vaccinationMasterDto.getAge());
			vaccinationMaster.setDayid(vaccinationMasterDto.getDayid());
			vaccinationMaster.setOrganizationId(vaccinationMasterDto.getOrganizationId());
			vaccinationMaster.setUnitId(vaccinationMasterDto.getUnitId());
			vaccinationMaster.setStatus(vaccinationMasterDto.getStatus());
			vaccinationMaster.setDay(vaccinationMasterDto.getDay());
			vaccinationMaster.setCreatedBy(vaccinationMasterDto.getCreatedBy());
			vaccinationMaster.setCreatedDate(GlobalCommonDateUtils.getDate(vaccinationMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			vaccinationMaster.setUpdatedBy(vaccinationMasterDto.getUpdatedBy());
			vaccinationMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(vaccinationMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			 
			save(vaccinationMaster);*/
					VaccinationMaster vaccinationMaster = new VaccinationMaster();
					vaccinationMaster.setGenderId(vaccinationMasterDto.getGenderId());
					vaccinationMaster.setVaccinationCode(vaccinationMasterDto.getVaccinationCode());
					vaccinationMaster.setAdministrationId(vaccinationMasterDto.getAdministrationId());
					vaccinationMaster.setSiteId(vaccinationMasterDto.getSiteId());
					vaccinationMaster.setDoseId(vaccinationMasterDto.getDoseId());
					vaccinationMaster.setUomId(vaccinationMasterDto.getUomId());
					vaccinationMaster.setAge(vaccinationMasterDto.getAge());
					vaccinationMaster.setDayid(vaccinationMasterDto.getDayid());
					vaccinationMaster.setOrganizationId(vaccinationMasterDto.getOrganizationId());
					vaccinationMaster.setUnitId(vaccinationMasterDto.getUnitId());
					vaccinationMaster.setStatus(vaccinationMasterDto.getStatus());
					vaccinationMaster.setDay(vaccinationMasterDto.getDay());
					vaccinationMaster.setGenericId(vaccinationMasterDto.getGenericId());
					vaccinationMaster.setMinAge(vaccinationMasterDto.getMinAge());
					vaccinationMaster.setMinDay(vaccinationMasterDto.getMinDay());
					vaccinationMaster.setMaxAge(vaccinationMasterDto.getMaxAge());
					vaccinationMaster.setMaxDay(vaccinationMasterDto.getMaxDay());
					vaccinationMaster.setSeries(vaccinationMasterDto.getSeries());
					vaccinationMaster.setAbbreviation(vaccinationMasterDto.getAbbreviation());
					vaccinationMaster.setRemark(vaccinationMasterDto.getRemark());
					vaccinationMaster.setInterval(vaccinationMasterDto.getInterval());
					vaccinationMaster.setChooseInterval(vaccinationMasterDto.getChooseInterval());
					vaccinationMaster.setCreatedBy(vaccinationMasterDto.getCreatedBy());
					vaccinationMaster.setCreatedDate(GlobalCommonDateUtils.getDate(vaccinationMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
					vaccinationMaster.setUpdatedBy(vaccinationMasterDto.getUpdatedBy());
					vaccinationMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(vaccinationMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
					vaccinationMaster=save(vaccinationMaster);
				
					return new Response(SUCCESS, SUCCESS_CODE, "Vaccine Added Successfully", null, vaccinationMaster);
		
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateVaccinationMaster(VaccinationMasterDto vaccinationMasterDto) throws ApplicationException {
		try {
			/*VaccinationMaster vaccinationMaster=findById(vaccinationMasterDto.getVaccinationId());
			vaccinationMaster.setVaccinationCode(vaccinationMasterDto.getVaccinationCode());
			//vaccinationMaster.setVaccinationDesc(vaccinationMasterDto.getVaccinationDesc());
			vaccinationMaster.setAdministrationId(vaccinationMasterDto.getAdministrationId());
			vaccinationMaster.setSiteId(vaccinationMasterDto.getSiteId());
			vaccinationMaster.setDoseId(vaccinationMasterDto.getDoseId());
			vaccinationMaster.setUomId(vaccinationMasterDto.getUomId());
			vaccinationMaster.setAge(vaccinationMasterDto.getAge());
			vaccinationMaster.setDayid(vaccinationMasterDto.getDayid());
			vaccinationMaster.setOrganizationId(vaccinationMasterDto.getOrganizationId());
			vaccinationMaster.setUnitId(vaccinationMasterDto.getUnitId());
			//vaccinationMaster.setStatus(vaccinationMasterDto.getStatus());
			vaccinationMaster.setDay(vaccinationMasterDto.getDay());
			vaccinationMaster.setUpdatedBy(vaccinationMasterDto.getUpdatedBy());
			vaccinationMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(vaccinationMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
		update(vaccinationMaster);*/
			
			//List<Integer> genderIdList = vaccinationMasterDto.getSelectedGenderList();
		//	if(genderIdList.size()>0 && genderIdList!=null)
		//	{
			//	for (Integer genderIdDto : genderIdList) {
				
					VaccinationMaster vaccinationMaster = findById(vaccinationMasterDto.getVaccinationId());
					vaccinationMaster.setGenderId(vaccinationMasterDto.getGenderId());
					vaccinationMaster.setVaccinationCode(vaccinationMasterDto.getVaccinationCode());
					vaccinationMaster.setAdministrationId(vaccinationMasterDto.getAdministrationId());
					vaccinationMaster.setSiteId(vaccinationMasterDto.getSiteId());
					vaccinationMaster.setDoseId(vaccinationMasterDto.getDoseId());
					vaccinationMaster.setUomId(vaccinationMasterDto.getUomId());
					vaccinationMaster.setAge(vaccinationMasterDto.getAge());
					vaccinationMaster.setDayid(vaccinationMasterDto.getDayid());
					vaccinationMaster.setOrganizationId(vaccinationMasterDto.getOrganizationId());
					vaccinationMaster.setUnitId(vaccinationMasterDto.getUnitId());
					vaccinationMaster.setDay(vaccinationMasterDto.getDay());
					vaccinationMaster.setGenericId(vaccinationMasterDto.getGenericId());
					vaccinationMaster.setMinAge(vaccinationMasterDto.getMinAge());
					vaccinationMaster.setMinDay(vaccinationMasterDto.getMinDay());
					vaccinationMaster.setMaxAge(vaccinationMasterDto.getMaxAge());
					vaccinationMaster.setMaxDay(vaccinationMasterDto.getMaxDay());
					vaccinationMaster.setAbbreviation(vaccinationMasterDto.getAbbreviation());
					vaccinationMaster.setSeries(vaccinationMasterDto.getSeries());
					vaccinationMaster.setRemark(vaccinationMasterDto.getRemark());
					vaccinationMaster.setInterval(vaccinationMasterDto.getInterval());
					vaccinationMaster.setChooseInterval(vaccinationMasterDto.getChooseInterval());
					vaccinationMaster.setUpdatedBy(vaccinationMasterDto.getUpdatedBy());
					vaccinationMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(vaccinationMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
					vaccinationMaster = update(vaccinationMaster);
			//	}
				return new Response(SUCCESS, SUCCESS_CODE, "Vaccine Updated Successfully", null, vaccinationMaster);
			//}
	
	} catch (HibernateException he) {
		throw he;
	} catch (Exception e) {
		
		throw e;
	}
	//return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateVaccinationMasterStatus(VaccinationMasterDto vaccinationMasterDto)
			throws ApplicationException {
		try {
			VaccinationMaster vaccinationMaster=findById(vaccinationMasterDto.getVaccinationId());
			
			vaccinationMaster.setStatus(vaccinationMasterDto.getStatus());
			vaccinationMaster.setUpdatedBy(vaccinationMasterDto.getUpdatedBy());
			vaccinationMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(vaccinationMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			 
			update(vaccinationMaster);
	
	return new Response(SUCCESS, SUCCESS_CODE, "Vaccination Status Updated Successfully", null, null);
	
	} catch (HibernateException he) {
		throw he;
	} catch (Exception e) {
		
		throw e;
	}
	//return new Response<>(ERROR, ERROR_CODE, COMMON_UPDATE, null, null);
	}

	@Override
	public Response getVaccinationMasterById(Integer vaccinationId, Integer organizationId, Integer unitId)
			throws ApplicationException {
		try {
			List<VaccinationMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_Vaccination_Master_BY_ID")
					.setParameter("vaccinationId", vaccinationId)
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(VaccinationMasterDto.class)).list();
	
		
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getVaccinationMasterList(VaccinationMasterDto vaccinationMasterDto) throws ApplicationException {
		try {
			List<VaccinationMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_Vaccination_Master_LIST")
					.setParameter("organizationId", vaccinationMasterDto.getOrganizationId())
					.setParameter("unitId", vaccinationMasterDto.getUnitId())
					.setFirstResult(vaccinationMasterDto.getOffset() != null ? vaccinationMasterDto.getOffset()  : 0)
					.setMaxResults(vaccinationMasterDto.getNoOfRecordsPerPage() != null ? vaccinationMasterDto.getNoOfRecordsPerPage()  : 50)
					.setResultTransformer(Transformers.aliasToBean(VaccinationMasterDto.class)).list();

		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getVaccinationMasterCount(VaccinationMasterDto vaccinationMasterDto) throws ApplicationException {
try {
			
			Session session = sessionFactory.getCurrentSession();
			Criteria crit = session.createCriteria(VaccinationMaster.class);
			crit.add(Restrictions.eq("organizationId", vaccinationMasterDto.getOrganizationId()));
			crit.add(Restrictions.eq("unitId", vaccinationMasterDto.getUnitId()));
			crit.setProjection(Projections.rowCount());
			Long count = (Long) crit.uniqueResult();
			
			return new Response(SUCCESS, SUCCESS_CODE, null, null, count);
				
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getActiveVaccinationMaster(Integer organizationId, Integer unitId) throws ApplicationException {
		try {
			List<VaccinationMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_Vaccination_Master")
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(VaccinationMasterDto.class)).list();	
	
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
	//	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@Override
	public Response getVaccinationMasterByName(VaccinationMasterDto vaccinationMasterDto) throws ApplicationException {
try {
			
	
	List<Integer> genderIdList = vaccinationMasterDto.getSelectedGenderList();
				if(genderIdList.size()>0 && genderIdList!=null)
				{
					for (Integer genderIdDto : genderIdList) {
						List<VaccinationMasterDto> list=sessionFactory.getCurrentSession()
								.getNamedQuery("GET_Vaccination_Master_BY_NAME")
								.setParameter("vaccinationCode", vaccinationMasterDto.getVaccinationCode())
								.setParameter("minAge", vaccinationMasterDto.getMinAge())
								.setParameter("minDay", vaccinationMasterDto.getMinDay())
								.setParameter("genderId", genderIdDto)
								.setParameter("series", vaccinationMasterDto.getSeries())
								.setParameter("organizationId", vaccinationMasterDto.getOrganizationId())
								.setParameter("unitId", vaccinationMasterDto.getUnitId())

									.setResultTransformer(Transformers.aliasToBean(VaccinationMasterDto.class)).list();
						return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
			}
					
				}
		
		
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getVaccinationMasterByNameNotId(VaccinationMasterDto vaccinationMasterDto)
			throws ApplicationException {
		try {
			/*List<VaccinationMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_Vaccination_Master_BY_NAME_NOT_ID")
					.setInteger("vaccinationCode", vaccinationMasterDto.getVaccinationCode())
					.setInteger("minAge", vaccinationMasterDto.getMinAge())
					.setString("minDay", vaccinationMasterDto.getMinDay())
					.setInteger("genderId", vaccinationMasterDto.getGenderId())
					.setInteger("series", vaccinationMasterDto.getSeries())
					.setInteger("vaccinationId", vaccinationMasterDto.getVaccinationId())
					.setInteger("organizationId", vaccinationMasterDto.getOrganizationId())
					.setInteger("unitId", vaccinationMasterDto.getUnitId())

					.setResultTransformer(Transformers.aliasToBean(VaccinationMasterDto.class)).list();*/
			List<Integer> genderIdList = vaccinationMasterDto.getSelectedGenderList();
			if(genderIdList.size()>0 && genderIdList!=null)
			{
				for (Integer genderIdDto : genderIdList) {
					List<VaccinationMasterDto> list=sessionFactory.getCurrentSession()
							.getNamedQuery("GET_Vaccination_Master_BY_NAME_NOT_ID")
							.setParameter("vaccinationCode", vaccinationMasterDto.getVaccinationCode())
							.setParameter("minAge", vaccinationMasterDto.getMinAge())
							.setParameter("minDay", vaccinationMasterDto.getMinDay())
							.setParameter("genderId", genderIdDto)
							.setParameter("series", vaccinationMasterDto.getSeries())
							.setParameter("vaccinationId", vaccinationMasterDto.getVaccinationId())
							.setParameter("organizationId", vaccinationMasterDto.getOrganizationId())
							.setParameter("unitId", vaccinationMasterDto.getUnitId())

							.setResultTransformer(Transformers.aliasToBean(VaccinationMasterDto.class)).list();
					return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
				}
			}
			 
		
		
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getRouteOfAdministrationList(Integer organizationId, Integer unitId) throws ApplicationException {
		try {
			List<VaccinationMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ROUTE_OF_ADMINISTRATION_LIST")
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(VaccinationMasterDto.class)).list();
			 
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
	//	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getActiveVaccinationMasterByGenderId(Integer organizationId, Integer genderId)
			throws ApplicationException {
		try {
			List<VaccinationMasterDto> list2=new ArrayList<>();
			List<VaccinationMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_Vaccination_Master_BY_GENDER_ID")
					.setParameter("organizationId", organizationId)
//					.setInteger("unitId", unitId)
					.setParameter("genderId", genderId)
					.setResultTransformer(Transformers.aliasToBean(VaccinationMasterDto.class)).list();	
			for(VaccinationMasterDto dto : list) {
				
		         String query = " select itm.id as \"itemId\", itm.name as \"name\", " + 
		         		" gen.name as \"genericName\" " + 
		         		" from public.m_item itm " + 
		         		" inner join public.m_generic gen on gen.id=itm.generic_id " + 
		         		" where itm.generic_id=:genericId "
		         		+ " ORDER BY itm.name ASC ";
			
		         List<VaccinationMasterDto> list3  = sessionFactory.getCurrentSession()
					    .createSQLQuery(query)
						.setParameter("genericId", dto.getGenericId())
						.setResultTransformer(Transformers.aliasToBean(VaccinationMasterDto.class)).list();
				dto.setListOfGenericIdList(list3);
				list2.add(dto);
			}
			
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
}

