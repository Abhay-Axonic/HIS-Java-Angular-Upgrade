package com.param.adt.master.unit.dao;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.adt.master.unit.dto.RmoAllocationDto;
import com.param.adt.master.unit.model.RmoAllocationMapper;
import com.param.adt.master.unit.model.RmoAllocationMaster;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings({ "rawtypes", "unchecked" })
@Repository
public class RMOAllocatioinDaoImpl  extends GenericDao<RmoAllocationMaster, Integer> implements IRMOAllocationDao, ICommonConstants {

	public RMOAllocatioinDaoImpl() {
		super(RmoAllocationMaster.class);
	}

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Response getRmoAllocationByRmoDoctorId(RmoAllocationDto rmoAllocationDto) throws ApplicationException {
		try {
			RmoAllocationDto dto=(RmoAllocationDto)sessionFactory.getCurrentSession()
					.getNamedQuery("GET_RMO_ALLOCATION_MASTER_LIST_BY_RMODOCTOR_ID")
					.setParameter("rmoDoctorId", rmoAllocationDto.getRmoDoctorId())
					.setParameter("orgId", rmoAllocationDto.getOrganizationId())
					.setParameter("unitId", rmoAllocationDto.getUnitId())
					.setParameter("visitTypeId", rmoAllocationDto.getVisitTypeId())
					.setResultTransformer(Transformers.aliasToBean(RmoAllocationDto.class)).uniqueResult();
			
			if(dto!=null) {
				return new Response<>(SUCCESS, null, null, null, dto);
			}
			else {
				return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}
			
		}catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response saveRmoAllocationMaster(RmoAllocationDto rmoAllocationDto) throws ApplicationException {
		try {
			RmoAllocationMaster rmoAllocationMaster=new RmoAllocationMaster();
			
			if(rmoAllocationDto.getWardId()!=null)
				rmoAllocationMaster.setWardId(rmoAllocationDto.getWardId());
			
			if(rmoAllocationDto.getSpecialityId()!=null)
				rmoAllocationMaster.setSpecialityId(rmoAllocationDto.getSpecialityId());
			if(rmoAllocationDto.getRmoDoctorId()!=null)
				rmoAllocationMaster.setRmoDoctorId(rmoAllocationDto.getRmoDoctorId());
			
			rmoAllocationMaster.setVisitTypeId(rmoAllocationDto.getVisitTypeId());
			rmoAllocationMaster.setStatus(rmoAllocationDto.getStatus());
			rmoAllocationMaster.setOrganizationId(rmoAllocationDto.getOrganizationId());
			rmoAllocationMaster.setUnitId(rmoAllocationDto.getUnitId());
			rmoAllocationMaster.setCreatedBy(rmoAllocationDto.getCreatedBy());
			rmoAllocationMaster.setCreatedDate(GlobalCommonDateUtils.getDate(rmoAllocationDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			rmoAllocationMaster.setUpdatedBy(rmoAllocationDto.getUpdatedBy());
			rmoAllocationMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(rmoAllocationDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			 save(rmoAllocationMaster);
			
			 if(rmoAllocationDto.getVisitTypeId()==OP){
			   for(RmoAllocationDto rmoAllocationDto1:rmoAllocationDto.getDoctorList()){
				   RmoAllocationMapper rmoAllocationMapper= new RmoAllocationMapper();
				   rmoAllocationMapper.setRmoAllocationId(rmoAllocationMaster.getRmoAllocationId());
				   rmoAllocationMapper.setDoctorId(rmoAllocationDto1.getId());
				   rmoAllocationMapper.setUnitId(rmoAllocationDto.getUnitId());
				   rmoAllocationMapper.setOrganizationId(rmoAllocationDto.getOrganizationId());
				   rmoAllocationMapper.setCreatedBy(rmoAllocationDto.getCreatedBy());
				   rmoAllocationMapper.setIsDeleted('N');
				   rmoAllocationMapper.setStatus('A');
				   rmoAllocationMapper.setCreatedDate(TimeZoneComponent.getDateByzone(rmoAllocationDto.getUnitId() != null ? rmoAllocationDto.getUnitId() : 0));
				   rmoAllocationMapper.setUpdatedBy(rmoAllocationDto.getCreatedBy());
				   rmoAllocationMapper.setUpdatedDate(TimeZoneComponent.getDateByzone(rmoAllocationDto.getUnitId() != null ? rmoAllocationDto.getUnitId() : 0));
					     sessionFactory.getCurrentSession().save(rmoAllocationMapper);
				     }
			 }else {
				  for(RmoAllocationDto wardDto:rmoAllocationDto.getSelectedWardList()){
					   RmoAllocationMapper rmoAllocationMapper= new RmoAllocationMapper();
					   rmoAllocationMapper.setRmoAllocationId(rmoAllocationMaster.getRmoAllocationId());
					   //rmoAllocationMapper.setDoctorId(rmoAllocationDto1.getId());
					   rmoAllocationMapper.setWardId(wardDto.getId());
					   rmoAllocationMapper.setUnitId(rmoAllocationDto.getUnitId());
					   rmoAllocationMapper.setOrganizationId(rmoAllocationDto.getOrganizationId());
					   rmoAllocationMapper.setCreatedBy(rmoAllocationDto.getCreatedBy());
					   rmoAllocationMapper.setIsDeleted('N');
					   rmoAllocationMapper.setStatus('A');
					   rmoAllocationMapper.setCreatedDate(TimeZoneComponent.getDateByzone(rmoAllocationDto.getUnitId() != null ? rmoAllocationDto.getUnitId() : 0));
					   rmoAllocationMapper.setUpdatedBy(rmoAllocationDto.getCreatedBy());
					   rmoAllocationMapper.setUpdatedDate(TimeZoneComponent.getDateByzone(rmoAllocationDto.getUnitId() != null ? rmoAllocationDto.getUnitId() : 0));
						     sessionFactory.getCurrentSession().save(rmoAllocationMapper);
					     }
			 }
			return new Response(SUCCESS, SUCCESS_CODE, "RMO Allocation Done Successfully", null, null);
		}catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getRmoAllocationMasterList(RmoAllocationDto rmoAllocationDto) throws ApplicationException {
		try {
			List<RmoAllocationDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_RMO_ALLOCATION_MASTER_LIST")
					.setParameter("visitTypeId", rmoAllocationDto.getVisitTypeId())
					.setParameter("orgId", rmoAllocationDto.getOrganizationId())
					.setParameter("unitId", rmoAllocationDto.getUnitId())
					.setFirstResult(rmoAllocationDto.getOffset() != null ? rmoAllocationDto.getOffset()  : 0)
					.setMaxResults(rmoAllocationDto.getNoOfRecordsPerPage() != null ? rmoAllocationDto.getNoOfRecordsPerPage()  : 10)
			.setResultTransformer(Transformers.aliasToBean(RmoAllocationDto.class)).list();
				return new Response<>(SUCCESS, null, null, list, null);
			
		}catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getRmoAllocationMasterByNameNotId(RmoAllocationDto rmoAllocationDto)
			throws ApplicationException {
		try {
			List<RmoAllocationDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_RMO_ALLOCATION_MASTER_BY_NAME_NOT_BY_ID")
					.setParameter("rmoDoctorId", rmoAllocationDto.getRmoDoctorId())
					.setParameter("rmoAllocationId", rmoAllocationDto.getRmoAllocationId())
					.setParameter("orgId", rmoAllocationDto.getOrganizationId())
					.setParameter("unitId", rmoAllocationDto.getUnitId())
					.setParameter("visitTypeId", rmoAllocationDto.getVisitTypeId())
					.setResultTransformer(Transformers.aliasToBean(RmoAllocationDto.class)).list();
			 if(list.size()>0) {
				 return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
			 }
			 else {
				 return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			 }
		
		
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateRmoAllocationMaster(RmoAllocationDto rmoAllocationDto) throws ApplicationException {
		try {
			RmoAllocationMaster rmoAllocationMaster=findById(rmoAllocationDto.getRmoAllocationId());
			rmoAllocationMaster.setVisitTypeId(rmoAllocationDto.getVisitTypeId());
			if(rmoAllocationDto.getWardId()!=null)
				rmoAllocationMaster.setWardId(rmoAllocationDto.getWardId());
			if(rmoAllocationDto.getSpecialityId()!=null)
				rmoAllocationMaster.setSpecialityId(rmoAllocationDto.getSpecialityId());
			if(rmoAllocationDto.getRmoDoctorId()!=null)
				rmoAllocationMaster.setRmoDoctorId(rmoAllocationDto.getRmoDoctorId());
			
			rmoAllocationMaster.setStatus(rmoAllocationDto.getStatus());
			rmoAllocationMaster.setOrganizationId(rmoAllocationDto.getOrganizationId());
			rmoAllocationMaster.setUnitId(rmoAllocationDto.getUnitId());
			rmoAllocationMaster.setUpdatedBy(rmoAllocationDto.getUpdatedBy());
			rmoAllocationMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(rmoAllocationDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			 update(rmoAllocationMaster);
			 if(rmoAllocationDto.getVisitTypeId().equals(OP)){
			sessionFactory.getCurrentSession().createQuery("update RmoAllocationMapper set isDeleted='Y',updatedBy=:updatedBy,updatedDate=:updatedDate where rmoAllocationId=:rmoAllocationId")
			.setParameter("rmoAllocationId", rmoAllocationDto.getRmoAllocationId())
			.setParameter("updatedBy", rmoAllocationDto.getUpdatedBy())
			.setParameter("updatedDate", GlobalCommonDateUtils.getDate(rmoAllocationDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"))
			.executeUpdate();
			if(rmoAllocationDto.getDoctorList()!=null){
			   for(RmoAllocationDto rmoAllocationDto1:rmoAllocationDto.getDoctorList()){
				   RmoAllocationMapper rmoAllocationMapper= new RmoAllocationMapper();
				   rmoAllocationMapper.setRmoAllocationId(rmoAllocationMaster.getRmoAllocationId());
				   rmoAllocationMapper.setDoctorId(rmoAllocationDto1.getId());
				   rmoAllocationMapper.setUnitId(rmoAllocationDto.getUnitId());
				   rmoAllocationMapper.setOrganizationId(rmoAllocationDto.getOrganizationId());
				   rmoAllocationMapper.setCreatedBy(rmoAllocationDto.getCreatedBy());
				   rmoAllocationMapper.setIsDeleted('N');
				   rmoAllocationMapper.setStatus('A');
				   rmoAllocationMapper.setCreatedDate(TimeZoneComponent.getDateByzone(rmoAllocationDto.getUnitId() != null ? rmoAllocationDto.getUnitId() : 0));
				   rmoAllocationMapper.setUpdatedBy(rmoAllocationDto.getCreatedBy());
				   rmoAllocationMapper.setUpdatedDate(TimeZoneComponent.getDateByzone(rmoAllocationDto.getUnitId() != null ? rmoAllocationDto.getUnitId() : 0));
					     sessionFactory.getCurrentSession().save(rmoAllocationMapper);
				     }
			  }
			 }else {

					sessionFactory.getCurrentSession().createQuery("update RmoAllocationMapper set isDeleted='Y',updatedBy=:updatedBy,updatedDate=:updatedDate where rmoAllocationId=:rmoAllocationId")
					.setParameter("rmoAllocationId", rmoAllocationDto.getRmoAllocationId())
					.setParameter("updatedBy", rmoAllocationDto.getUpdatedBy())
					.setParameter("updatedDate", GlobalCommonDateUtils.getDate(rmoAllocationDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"))
					.executeUpdate();
					if(rmoAllocationDto.getSelectedWardList()!=null){
					   for(RmoAllocationDto wardDto:rmoAllocationDto.getSelectedWardList()){
						   RmoAllocationMapper rmoAllocationMapper= new RmoAllocationMapper();
						   rmoAllocationMapper.setRmoAllocationId(rmoAllocationMaster.getRmoAllocationId());
						  // rmoAllocationMapper.setDoctorId(rmoAllocationDto1.getId());
						   rmoAllocationMapper.setWardId(wardDto.getId());
						   rmoAllocationMapper.setUnitId(rmoAllocationDto.getUnitId());
						   rmoAllocationMapper.setOrganizationId(rmoAllocationDto.getOrganizationId());
						   rmoAllocationMapper.setCreatedBy(rmoAllocationDto.getCreatedBy());
						   rmoAllocationMapper.setIsDeleted('N');
						   rmoAllocationMapper.setStatus('A');
						   rmoAllocationMapper.setCreatedDate(TimeZoneComponent.getDateByzone(rmoAllocationDto.getUnitId() != null ? rmoAllocationDto.getUnitId() : 0));
						   rmoAllocationMapper.setUpdatedBy(rmoAllocationDto.getCreatedBy());
						   rmoAllocationMapper.setUpdatedDate(TimeZoneComponent.getDateByzone(rmoAllocationDto.getUnitId() != null ? rmoAllocationDto.getUnitId() : 0));
							     sessionFactory.getCurrentSession().save(rmoAllocationMapper);
						     }
					  }
					 
			 }
			return new Response(SUCCESS, SUCCESS_CODE, "RMO Allocation Updated Successfully", null, null);
		}catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateRmoAllocationMasterStatus(RmoAllocationDto rmoAllocationDto)
			throws ApplicationException {
		try {
			RmoAllocationMaster rmoAllocationMaster=findById(rmoAllocationDto.getRmoAllocationId());
			rmoAllocationMaster.setRmoAllocationId(rmoAllocationDto.getRmoAllocationId());
			rmoAllocationMaster.setStatus(rmoAllocationDto.getStatus());
			rmoAllocationMaster.setUpdatedBy(rmoAllocationDto.getUpdatedBy());
			rmoAllocationMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(rmoAllocationDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			 update(rmoAllocationMaster);
			
			 sessionFactory.getCurrentSession().createQuery("update RmoAllocationMapper set status=:status,updatedBy=:updatedBy,updatedDate=:updatedDate where rmoAllocationId=:rmoAllocationId")
					.setParameter("status", rmoAllocationDto.getStatus())
					.setParameter("rmoAllocationId", rmoAllocationDto.getRmoAllocationId())
					.setParameter("updatedBy", rmoAllocationDto.getUpdatedBy())
					.setParameter("updatedDate",GlobalCommonDateUtils.getDate(rmoAllocationDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"))
					.executeUpdate();
			 
			return new Response(SUCCESS, SUCCESS_CODE, "RMO Allocation Status Updated Successfully", null, null);
		}catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getRmoAllocationMasterCount(RmoAllocationDto rmoAllocationDto) throws ApplicationException {
		 try {
				Session session = sessionFactory.getCurrentSession();
				Criteria crit = session.createCriteria(RmoAllocationMaster.class);
				crit.add(Restrictions.eq("organizationId", rmoAllocationDto.getOrganizationId()));
				crit.add(Restrictions.eq("unitId", rmoAllocationDto.getUnitId()));
				crit.add(Restrictions.eq("visitTypeId", rmoAllocationDto.getVisitTypeId()));
				crit.setProjection(Projections.rowCount());
				Long count = (Long) crit.uniqueResult();
				
				return new Response(SUCCESS, SUCCESS_CODE, null, null, count);
					
			} catch (HibernateException he) {
			} catch (Exception e) {
				
			}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getRmoAllocationMasterById(Integer rmoAllocationId, Integer organizationId, Integer unitId)
			throws ApplicationException {
		try {
			RmoAllocationDto dto=(RmoAllocationDto)sessionFactory.getCurrentSession()
					.getNamedQuery("GET_RMO_ALLOCATION_MASTER_BY_ID")
					.setParameter("rmoAllocationId", rmoAllocationId)
					.setParameter("orgId", organizationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(RmoAllocationDto.class)).uniqueResult();
			
			List<RmoAllocationDto> doctorList=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DOCTOR_LIST_BY_RMO_ALLOCATION_ID")
					.setParameter("rmoAllocationId", rmoAllocationId)
					.setParameter("orgId", organizationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(RmoAllocationDto.class)).list();
			
			List<RmoAllocationDto> wardList=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_WARD_LIST_BY_RMO_ALLOCATION_ID")
					.setParameter("rmoAllocationId", rmoAllocationId)
					.setParameter("orgId", organizationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(RmoAllocationDto.class)).list();
			
			dto.setDoctorList(doctorList);
			dto.setSelectedWardList(wardList);
			 return new Response(SUCCESS, SUCCESS_CODE, null, doctorList, dto);
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getActiveRmoAllocationMasterByWardId(Integer wardId, Integer organizationId, Integer unitId)
			throws ApplicationException {
		try {
			/*List<RmoAllocationDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_RMO_ALLOCATION_MASTER_LIST_BY_WARD_ID")
					.setParameter("wardId",wardId)
					.setParameter("orgId", organizationId)
					.setParameter("unitId", unitId)
			.setResultTransformer(Transformers.aliasToBean(RmoAllocationDto.class)).list();
				return new Response<>(SUCCESS, null, null, list, null);*/
			
		}catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getActiveRmoAllocationMasterByDoctorId(Integer doctorId, Integer organizationId, Integer unitId) {
		try {
			/*List<RmoAllocationDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_RMO_ALLOCATION_MASTER_LIST_BY_NURSE_ID")
					.setParameter("doctorId",doctorId)
					.setParameter("orgId", organizationId)
					.setParameter("unitId", unitId)
			.setResultTransformer(Transformers.aliasToBean(RmoAllocationDto.class)).list();
				return new Response<>(SUCCESS, null, null, list, null);*/
			
		}catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getDoctorListByRmoallocationId(RmoAllocationDto rmoAllocationDto) throws ApplicationException {
		try {
			List<RmoAllocationDto> doctorList=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DOCTOR_LIST_BY_RMO_ALLOCATION_ID")
					.setParameter("rmoAllocationId", rmoAllocationDto.getRmoAllocationId())
					.setParameter("orgId", rmoAllocationDto.getOrganizationId())
					.setParameter("unitId", rmoAllocationDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(RmoAllocationDto.class)).list();
			 return new Response(SUCCESS, SUCCESS_CODE, null, doctorList, null);
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getActiveRmoAllocationMasterByRMODoctorId(RmoAllocationDto rmoAllocationDto)
			throws ApplicationException {
		try {
			/*
			RmoAllocationMaster dto=(RmoAllocationMaster)sessionFactory.getCurrentSession().createQuery("from RmoAllocationMaster where rmoDoctorId=:rmoDoctorId "
					+ " and organizationId=:orgId and unitId=:unitId ")
					.setParameter("rmoDoctorId", rmoAllocationDto.getRmoDoctorId())
					.setParameter("orgId", rmoAllocationDto.getOrganizationId())
					.setParameter("unitId", rmoAllocationDto.getUnitId())
					.uniqueResult();*/
			List<RmoAllocationDto> dtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_RMO_ALLOCATION_MASTER_LIST_BY_RMODOCTOR_ID")
					.setParameter("rmoDoctorId", rmoAllocationDto.getRmoDoctorId())
					.setParameter("orgId", rmoAllocationDto.getOrganizationId())
					.setParameter("unitId", rmoAllocationDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(RmoAllocationDto.class)).list();
			
				if(dtoList!=null)
				{
					List<RmoAllocationDto> doctorList = null;
					List<RmoAllocationDto> wardList=null;
					
					List<Integer> doctorIds = new LinkedList<Integer>();
					Set<Integer> wardIds = new HashSet<Integer>();
					for(RmoAllocationDto dto : dtoList)
					{
						if(dto.getVisitTypeId().equals(OP))
						{
							doctorList=sessionFactory.getCurrentSession()
									.getNamedQuery("GET_DOCTOR_LIST_BY_RMO_ALLOCATION_ID")
									.setParameter("rmoAllocationId", dto.getRmoAllocationId())
									.setParameter("orgId", rmoAllocationDto.getOrganizationId())
									.setParameter("unitId", rmoAllocationDto.getUnitId())
									.setResultTransformer(Transformers.aliasToBean(RmoAllocationDto.class)).list();
							for(RmoAllocationDto list:doctorList)
							{
								doctorIds.add(list.getDoctorId());
							}
							
						}else{
							wardList=sessionFactory.getCurrentSession()
									.getNamedQuery("GET_WARD_LIST_BY_RMO_ALLOCATION_ID")
									.setParameter("rmoAllocationId", dto.getRmoAllocationId())
									.setParameter("orgId", rmoAllocationDto.getOrganizationId())
									.setParameter("unitId", rmoAllocationDto.getUnitId())
									.setResultTransformer(Transformers.aliasToBean(RmoAllocationDto.class)).list();
							for(RmoAllocationDto list: wardList)
							{
								wardIds.add(list.getWardId());
							}
						}
					
					}
					return new Response(SUCCESS,null,null,doctorIds,wardIds);
			}else{
				return new Response(ERROR,null,null,null,null);
			}
			
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getListOfDoctorMappedToRmo(RmoAllocationDto rmoAllocationDto) throws ApplicationException {
		try {
			List<RmoAllocationDto> doctorList=sessionFactory.getCurrentSession()
					.createSQLQuery("	select concat('Dr. ',coalesce(doc.first_name),' ',coalesce(doc.middle_name),' ',coalesce(doc.last_name)) as \"doctorName\","
							+ "ramast.speciality_id as \"specialityId\",ram.doctor_id as \"doctorId\" "  
							+"	from public.m_rmo_allocation_mapper ram "  
							+"	inner join doctor.m_doctor_master doc on doc.doctor_id=ram.doctor_id " 
							+"	inner join public.m_rmo_allocation_master ramast on ramast.rmo_allocation_id=ram.rmo_allocation_id " 
							+"	where ram.doctor_id in(:doctorList) and ram.is_deleted='N' " 
							+"    and ram.organization_id=:orgId and ram.unit_id=:unitId ")
					.setParameterList("doctorList", rmoAllocationDto.getDoctorIdList())
					.setParameter("orgId", rmoAllocationDto.getOrganizationId())
					.setParameter("unitId", rmoAllocationDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(RmoAllocationDto.class)).list();
			 return new Response(SUCCESS, SUCCESS_CODE, null, doctorList, null);
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	}

