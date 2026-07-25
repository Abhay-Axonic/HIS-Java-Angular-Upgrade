package com.param.adt.master.unit.dao;

import java.util.Iterator;
import java.util.List;

import org.hibernate.*;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.adt.master.unit.dto.NursingStationWardMapperDto;
import com.param.adt.master.unit.model.NursingStationWardMapper;
import com.param.adt.master.unit.model.UserNursingStationMapper;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.UserNursingStationMapperDto;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
@SuppressWarnings({ "unchecked", "rawtypes" })
@Repository
public class NursingStationWardMapperDaoImpl extends GenericDao<NursingStationWardMapper, Integer>
		implements INursingStationWardMapperDao, ICommonConstants {

	@Autowired
	SessionFactory sessionFactory;

	public NursingStationWardMapperDaoImpl() {
		super(NursingStationWardMapper.class);
	}

	@Override
	public Response saveNursingStationWardMapper(NursingStationWardMapperDto nursingStationWardMapperDto)
			throws ApplicationException {
		try {
			 StringBuilder resString= new StringBuilder();
			 StringBuilder positiveRes = new StringBuilder();
			for (NursingStationWardMapperDto mapperDto : nursingStationWardMapperDto.getNursingWardMapperList()) {
				mapperDto.setNursingStationId(nursingStationWardMapperDto.getNursingStationId());
				mapperDto.setOrganizationId(nursingStationWardMapperDto.getOrganizationId());
				mapperDto.setUnitId(nursingStationWardMapperDto.getUnitId());
				Response res = checkIsNursingStationAlreadyAssignedOrNot(mapperDto);
				if((Integer)res.getObject()>0){
					resString.append(mapperDto.getWardName()+" is already assigned to selected Nursing station...!! \n");
				}else{
					NursingStationWardMapper nursingStationWardMapper = new NursingStationWardMapper();
						nursingStationWardMapper.setNursingStationId(nursingStationWardMapperDto.getNursingStationId());
						nursingStationWardMapper.setWardId(mapperDto.getWardId());
						nursingStationWardMapper.setStatus(nursingStationWardMapperDto.getStatus());
						nursingStationWardMapper.setOrganizationId(nursingStationWardMapperDto.getOrganizationId());
						nursingStationWardMapper.setUnitId(nursingStationWardMapperDto.getUnitId());
						nursingStationWardMapper.setCreatedBy(nursingStationWardMapperDto.getCreatedBy());
						nursingStationWardMapper.setUpdatedBy(nursingStationWardMapperDto.getUpdatedBy());
						nursingStationWardMapper.setIsDeleted('N');
						nursingStationWardMapper.setCreatedDate(GlobalCommonDateUtils.getDate(nursingStationWardMapperDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
						nursingStationWardMapper.setUpdatedDate(GlobalCommonDateUtils.getDate(nursingStationWardMapperDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
					save(nursingStationWardMapper);
					positiveRes.append("Nursing station for "+mapperDto.getWardName()+" is assigned successfully...!! \n");
				}
			}
			return new Response<>(SUCCESS, positiveRes.toString(), resString.toString(), null, null);
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	
	@Override
	public Response getNursingStationWardMapperList(NursingStationWardMapperDto nursingStationWardMapperDto)
			throws ApplicationException {
		try {
			List<NursingStationWardMapperDto>nursingStationWardMapperDtoList=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_NURSING_STATION_WARD_MAPPER_LIST")
					.setParameter("orgId", nursingStationWardMapperDto.getOrganizationId())
					.setParameter("unitId", nursingStationWardMapperDto.getUnitId())
					.setFirstResult(nursingStationWardMapperDto.getOffset() != null ? nursingStationWardMapperDto.getOffset() : 0)
					.setMaxResults(nursingStationWardMapperDto.getRecordPerPage() != null ? nursingStationWardMapperDto.getRecordPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(NursingStationWardMapperDto.class)).list();
			
			List<NursingStationWardMapperDto>nursingStationWardMapperDtoList2=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_NURSING_STATION_WARD_MAPPER_LIST")
					.setParameter("orgId", nursingStationWardMapperDto.getOrganizationId())
					.setParameter("unitId", nursingStationWardMapperDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(NursingStationWardMapperDto.class)).list();
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, nursingStationWardMapperDtoList,nursingStationWardMapperDtoList2.size());
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateNursingStationWardMapperStatus(NursingStationWardMapperDto nursingStationWardMapperDto)
			throws ApplicationException {
		try {
			NursingStationWardMapper nursingStationWardMapper = findById(nursingStationWardMapperDto.getNursingWardId());
			nursingStationWardMapper.setStatus(nursingStationWardMapperDto.getStatus() == ACTIVE ? ACTIVE : INACTIVE);
			nursingStationWardMapper.setUpdatedBy(nursingStationWardMapperDto.getUpdatedBy());
			nursingStationWardMapper.setUpdatedDate(
					GlobalCommonDateUtils.getDate(nursingStationWardMapperDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			update(nursingStationWardMapper);
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateNursingStationWardMapper(List<NursingStationWardMapperDto> nursingStationWardMapperDto,NursingStationWardMapperDto dto)
			throws ApplicationException {
try{
			
			
/*			Integer result = sessionFactory.getCurrentSession()
					.createQuery("UPDATE NursingStationWardMapper mppr "
							+" SET mppr.isDeleted='Y' "
							+" WHERE mppr.organizationId="+nursingStationWardMapperDto.get(0).getOrganizationId()
							+" AND mppr.unitId="+nursingStationWardMapperDto.get(0).getUnitId()
//							+" AND mppr.wardId="+nursingStationWardMapperDto.get(0).getWardId()
							+" AND mppr.nursingStationId="+nursingStationWardMapperDto.get(0).getNursingStationId())*/		
	
			Query sql = sessionFactory.getCurrentSession()
			.createQuery("UPDATE NursingStationWardMapper mppr "
					+" SET mppr.isDeleted='Y' "
					+" WHERE mppr.organizationId=:organizationId "
					+" AND mppr.unitId=:unitId "
					+" AND mppr.nursingStationId=:nursingStationId ");
					sql.setParameter("organizationId", dto.getOrganizationId());
					sql.setParameter("unitId", dto.getUnitId());
					sql.setParameter("nursingStationId", dto.getNursingStationId());
					Integer result = sql.executeUpdate();
			if (result > 0 && nursingStationWardMapperDto!=null && nursingStationWardMapperDto.size()>0) {
				for (Iterator iterator = nursingStationWardMapperDto.iterator(); iterator.hasNext();) {
					NursingStationWardMapperDto listNursingStationWardMapper = (NursingStationWardMapperDto) iterator
							.next();
					NursingStationWardMapper nursingStationWardMapper=new NursingStationWardMapper();
					nursingStationWardMapper.setNursingStationId(listNursingStationWardMapper.getNursingStationId());
					nursingStationWardMapper.setWardId(listNursingStationWardMapper.getWardId());
					nursingStationWardMapper.setOrganizationId(listNursingStationWardMapper.getOrganizationId());
					nursingStationWardMapper.setUnitId(listNursingStationWardMapper.getUnitId());
					nursingStationWardMapper.setIsDeleted('N');
					nursingStationWardMapper.setCreatedBy(listNursingStationWardMapper.getCreatedBy());
					nursingStationWardMapper.setStatus(listNursingStationWardMapper.getStatus());
					nursingStationWardMapper.setUpdatedBy(listNursingStationWardMapper.getUpdatedBy());
					nursingStationWardMapper.setCreatedDate(GlobalCommonDateUtils.getDate(listNursingStationWardMapper.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
					nursingStationWardMapper.setUpdatedDate(GlobalCommonDateUtils.getDate(listNursingStationWardMapper.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
					
					save(nursingStationWardMapper);
				}
				
				
			} 
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getNursingStationWardMapperById(Integer id, Integer orgId, Integer unitId)
			throws ApplicationException {
		try {
			List<NursingStationWardMapperDto>nursingStationWardMapperDtoList=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_NURSING_STATION_WARD_MAPPER_BY_ID")
					.setParameter("nursingStationId", id)
					.setParameter("orgId", orgId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(NursingStationWardMapperDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, nursingStationWardMapperDtoList,null);
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getNursingStationWardMapperCount(NursingStationWardMapperDto nursingStationWardMapperDto)
			throws ApplicationException {
		try {
			Session session=sessionFactory.getCurrentSession();
			Criteria c=session.createCriteria(NursingStationWardMapper.class); 
			c.add(Restrictions.eq("organizationId", nursingStationWardMapperDto.getOrganizationId()));
			c.add(Restrictions.eq("unitId", nursingStationWardMapperDto.getUnitId()));
			c.setProjection(Projections.rowCount());  
			Long count = (Long)c.uniqueResult();
			return new Response(SUCCESS, null, null, null, count);
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getWardListByUserId(Integer userId,Integer orgId, Integer unitId)
			throws ApplicationException {
		try {
			List<NursingStationWardMapperDto>nursingStationWardMapperDtoList=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_WARD_LIST_BY_USER_ID")
					.setParameter("userId", userId)
					.setParameter("orgId", orgId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(NursingStationWardMapperDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, nursingStationWardMapperDtoList,null);
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response checkIsNursingStationAlreadyAssigned(NursingStationWardMapperDto nursingStationWardMapperDto)
			throws ApplicationException {
		try {
			List<NursingStationWardMapperDto>nursingStationWardMapperDtoList=sessionFactory.getCurrentSession()
					.getNamedQuery("CHECK_IS_NURSING_STATION_ALREADY_ASSIGNED")
//					.setParameter("wardId", nursingStationWardMapperDto.getWardId())
					.setParameter("nursingStationId", nursingStationWardMapperDto.getNursingStationId())
					.setParameter("orgId", nursingStationWardMapperDto.getOrganizationId())
					.setParameter("unitId", nursingStationWardMapperDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(NursingStationWardMapperDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, nursingStationWardMapperDtoList,nursingStationWardMapperDtoList.size());
		} catch (HibernateException he) {
		} catch (Exception e) {

		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getWardListByNursingStationId(Integer nursingStationId, Integer orgId, Integer unitId)
			throws ApplicationException {
		try {
			List<NursingStationWardMapperDto>nursingStationWardMapperDtoList=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_WARD_LIST_BY_NURSING_STATION_ID")
					.setParameter("nursingStationId", nursingStationId)
					.setParameter("orgId", orgId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(NursingStationWardMapperDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, nursingStationWardMapperDtoList,null);
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response checkIsNursingStationAlreadyAssignedOrNot(NursingStationWardMapperDto nursingStationWardMapperDto)
			throws ApplicationException {
		try {
			List<NursingStationWardMapperDto>nursingStationWardMapperDtoList=sessionFactory.getCurrentSession()
					.getNamedQuery("CHECK_IS_NURSINGSTATION_ALREADY_ASSIGNED_OR_NOT")
					.setParameter("wardId", nursingStationWardMapperDto.getWardId())
					.setParameter("nursingStationId", nursingStationWardMapperDto.getNursingStationId())
					.setParameter("orgId", nursingStationWardMapperDto.getOrganizationId())
					.setParameter("unitId", nursingStationWardMapperDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(NursingStationWardMapperDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, nursingStationWardMapperDtoList,nursingStationWardMapperDtoList.size());
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getNursingStationListByWardId(Integer wardId, Integer orgId, Integer unitId)
			throws ApplicationException {
		try {
			List<NursingStationWardMapperDto>nursingStationWardMapperDtoList=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_NURSING_STATION_LIST_BY_WARD_ID")
					.setParameter("wardId", wardId)
					.setParameter("orgId", orgId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(NursingStationWardMapperDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, nursingStationWardMapperDtoList,null);
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}
