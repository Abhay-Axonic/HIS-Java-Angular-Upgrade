package com.param.adt.master.global.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.adt.master.global.dto.ICPCCodesConfigurationDto;
import com.param.adt.master.global.dto.ICPCCodesConfigurationMapperDto;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.model.ICPCCodesConfigurationMapper;
import com.param.global.otims.dto.ProcedureSchedulingDto;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings({"rawtypes", "unchecked","unused"})
@Repository
public class ICPCCodesConfigurationMapperDaoImpl extends GenericDao<ICPCCodesConfigurationMapper, Integer>implements IICPCCodesConfigurationMapperDao, ICommonConstants{

	public ICPCCodesConfigurationMapperDaoImpl() {
		super(ICPCCodesConfigurationMapper.class);
	}
	
	@Autowired
	private SessionFactory SessionFactory;
	
	@Override
	public Response saveICPCCodesConfigurationMapper(ICPCCodesConfigurationMapperDto iCPCCodesConfigurationMapperDto)
			throws ApplicationException {
		try{
			String sqlQuery = "UPDATE adt.t_icpc_code_configuration_mapper " +
					"SET status = 'I' " +
					"WHERE icpc_configuration_id = :icpcSystemId " +
					"AND organization_id = :organizationId " +
					"AND unit_id = :unitId";
			Query sql = sessionFactory.getCurrentSession().createSQLQuery(sqlQuery)
					.setParameter("icpcSystemId", iCPCCodesConfigurationMapperDto.getIcpcSystemId())
					.setParameter("organizationId", iCPCCodesConfigurationMapperDto.getOrganizationId())
					.setParameter("unitId", iCPCCodesConfigurationMapperDto.getUnitId());
			int count = sql.executeUpdate();
					
					for (Integer value : iCPCCodesConfigurationMapperDto.getSpecialityIdList()) {
						
								ICPCCodesConfigurationMapper iCPCCodesConfigurationMapper = new ICPCCodesConfigurationMapper();
								iCPCCodesConfigurationMapper.setIcpcConfigurationId(iCPCCodesConfigurationMapperDto.getIcpcConfigurationId());
								iCPCCodesConfigurationMapper.setOrganizationId(iCPCCodesConfigurationMapperDto.getOrganizationId());
								iCPCCodesConfigurationMapper.setUnitId(iCPCCodesConfigurationMapperDto.getUnitId());
								iCPCCodesConfigurationMapper.setSpecialityId(value);
								iCPCCodesConfigurationMapper.setCreatedDate(GlobalCommonDateUtils.getDate(iCPCCodesConfigurationMapperDto.getCreatedDate(), "dd-M-yyyy HH:ss:mm"));
								iCPCCodesConfigurationMapper.setUpdatedBy(iCPCCodesConfigurationMapperDto.getUpdatedBy());
								iCPCCodesConfigurationMapper.setUpdatedDate(GlobalCommonDateUtils.getDate(iCPCCodesConfigurationMapperDto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));
								iCPCCodesConfigurationMapper.setCreatedBy(iCPCCodesConfigurationMapperDto.getCreatedBy());
								iCPCCodesConfigurationMapper.setStatus('A');
								sessionFactory.getCurrentSession().save(iCPCCodesConfigurationMapper);
					}
					
					return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
			
		} catch (HibernateException he) {
			return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		} catch (Exception e) {
			
			return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
		
	}

	@Override
	public Response getICPCCodesConfigurationMapperList(ICPCCodesConfigurationMapperDto iCPCCodesConfigurationMapperDto)
			throws ApplicationException {
		try{
			
			List<ICPCCodesConfigurationDto> iCPCCodesConfigurationMapperDtoList =  SessionFactory.getCurrentSession()
					.getNamedQuery("GET_ICPC_CODES_CONFIGURATION_MAPPER_LIST")
				//	.setCharacter("status",iCPCCodesConfigurationMapperDto.getStatus())
					.setParameter("organizationId", iCPCCodesConfigurationMapperDto.getOrganizationId())
	                .setParameter("unitId", iCPCCodesConfigurationMapperDto.getUnitId())
	             //   .setParameter("createdBy", iCPCCodesConfigurationMapperDto.getCreatedBy())
					.setResultTransformer(Transformers.aliasToBean(ICPCCodesConfigurationDto.class)).list();
			
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, iCPCCodesConfigurationMapperDtoList, null );
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getICPCSystemList(ICPCCodesConfigurationMapperDto iCPCCodesConfigurationMapperDto)
			throws ApplicationException {
		try{
			String query=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ICPC_SYSTEM_LIST_BY_SPECIALITY_ID").getQueryString();
			
			StringBuffer subQuery = new StringBuffer();
			if(iCPCCodesConfigurationMapperDto.getSpecialityId()!=null){
				subQuery.append(" and mapper.specialityId =:specialityId ");
			}
			subQuery.append(" order by icpcMstr.icpcSystemDesc asc"); 
			
			query = query.concat(subQuery.toString());
		
			Query sql=  sessionFactory.getCurrentSession()
					.createQuery(query)
					//.setParameter("specialityId",iCPCCodesConfigurationMapperDto.getSpecialityId())
					.setParameter("organizationId",iCPCCodesConfigurationMapperDto.getOrganizationId())
					.setParameter("unitId",iCPCCodesConfigurationMapperDto.getUnitId());
			if(iCPCCodesConfigurationMapperDto.getSpecialityId()!=null){
				sql.setParameter("specialityId", iCPCCodesConfigurationMapperDto.getSpecialityId());
			}
			List<ICPCCodesConfigurationMapperDto> icpcMasterDtos= sql.setResultTransformer(Transformers.aliasToBean(ICPCCodesConfigurationMapperDto.class)).list();
			
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, icpcMasterDtos, null);
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}

	@Override
	public Response updateICPCCodesConfigurationStatus(ICPCCodesConfigurationMapperDto iCPCCodesConfigurationMapperDto)
			throws ApplicationException {
		try{
			
			ICPCCodesConfigurationMapper mpr = findById(iCPCCodesConfigurationMapperDto.getIcpcConfigurationId());
			mpr.setStatus(iCPCCodesConfigurationMapperDto.getStatus());
			update(mpr);
			
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
			
		} catch (HibernateException he) {
		} catch (Exception e) {
			
			
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getSpecialityListByIcpcSystemId(ICPCCodesConfigurationMapperDto iCPCCodesConfigurationMapperDto)
			throws ApplicationException {
try{
			
			List<ICPCCodesConfigurationMapperDto> list =  SessionFactory.getCurrentSession()
					.getNamedQuery("GET_SPECIALITY_LIST_BY_SYSTEM_ID")
					.setParameter("status",'A')
					.setParameter("organizationId", iCPCCodesConfigurationMapperDto.getOrganizationId())
					.setParameter("icpcConfigurationId", iCPCCodesConfigurationMapperDto.getIcpcConfigurationId())
	                .setParameter("unitId", iCPCCodesConfigurationMapperDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(ICPCCodesConfigurationMapperDto.class)).list();
		
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, list, null);
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
}
