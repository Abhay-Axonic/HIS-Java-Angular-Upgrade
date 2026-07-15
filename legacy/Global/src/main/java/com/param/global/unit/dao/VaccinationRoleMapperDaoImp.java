package com.param.global.unit.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.model.VaccinationRoleMapper;

import com.param.global.unit.dto.VaccinationRoleMapperDto;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;


@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class VaccinationRoleMapperDaoImp extends GenericDao<VaccinationRoleMapper, Integer> implements IVaccinationRoleMapperDao, ICommonConstants {

	public VaccinationRoleMapperDaoImp() {
		super(VaccinationRoleMapper.class);
		}
		
		@Autowired
		SessionFactory sessionFactory;

		@Override
		public Response getVaccinationRoleMapperByRoleId(VaccinationRoleMapperDto vaccinationRoleMapperDto){
				
			try {
				List<VaccinationRoleMapperDto> list = sessionFactory.getCurrentSession().getNamedQuery("GET_VACCINATION_MASTER_LIST_BY_ROLEID")
						.setParameter("unitId", vaccinationRoleMapperDto.getUnitId())
						.setParameter("organizationId", vaccinationRoleMapperDto.getOrganizationId())
						.setParameter("roleId", vaccinationRoleMapperDto.getRoleId())
					    .setResultTransformer(Transformers.aliasToBean(VaccinationRoleMapperDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
			} catch (HibernateException he) {
				  throw he;
			} catch (Exception e) {
               throw e;
			}
			//return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}

		@Override
		public void inactiveVaccinationByRoleId(List<Integer> mapperList) {
			for(Integer vaccinationRoleMaperId : mapperList) {
			sessionFactory.getCurrentSession().createQuery("update VaccinationRoleMapper m set m.status='I' where m.vaccinationRoleMapperId =:vaccinationRoleMaperId ")
					.setParameter("vaccinationRoleMaperId",vaccinationRoleMaperId)
			.executeUpdate();
			}
		}
		
		@Override
		public Response getVaccinationRoleMapperByRoleId1(VaccinationRoleMapperDto vaccinationRoleMapperDto){
				
			try {
				List<VaccinationRoleMapperDto> list = sessionFactory.getCurrentSession().getNamedQuery("GET_VACCINATION_MAPPER_LIST_BY_ROLE_ID")
						.setParameter("unitId", vaccinationRoleMapperDto.getUnitId())
						.setParameter("organizationId", vaccinationRoleMapperDto.getOrganizationId())
						.setParameter("roleId", vaccinationRoleMapperDto.getRoleId())
					    .setResultTransformer(Transformers.aliasToBean(VaccinationRoleMapperDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
			} catch (HibernateException he) {
				 throw he;
			} catch (Exception e) {
               throw e;
			}
			//return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}

	
}
	

		
	



