package com.param.adt.master.unit.dao;

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

import com.param.adt.master.unit.dto.NurseAllocationDto;
import com.param.adt.master.unit.model.NurseAllocationMaster;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.org.common.dto.ShiftMasterDto;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings({ "rawtypes", "unchecked" })
@Repository
public class NurseAllocatioinDaoImpl  extends GenericDao<NurseAllocationMaster, Integer> implements INurseAllocationDao, ICommonConstants {

	public NurseAllocatioinDaoImpl() {
		super(NurseAllocationMaster.class);
	}

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Response getNurseAllocationByUserId(NurseAllocationDto nurseAllocationDto) throws ApplicationException {
		try {
			List<NurseAllocationDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_NURSE_ALLOCATION_MASTER_LIST_BY_USER_ID")
					.setParameter("userId", nurseAllocationDto.getUserId())
					.setParameter("orgId", nurseAllocationDto.getOrganizationId())
					.setParameter("unitId", nurseAllocationDto.getUnitId())
					//.setParameter("visitTypeId", nurseAllocationDto.getVisitTypeId())
					.setResultTransformer(Transformers.aliasToBean(NurseAllocationDto.class)).list();
			
			if(list!=null && list.size()>0) {
				return new Response<>(SUCCESS, null, null, null, null);
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
	public Response saveNurseAllocationMaster(NurseAllocationDto nurseAllocationDto) throws ApplicationException {
		try {
			NurseAllocationMaster nurseAllocationMaster=new NurseAllocationMaster();
			nurseAllocationMaster.setRoleId(nurseAllocationDto.getRoleId());
			nurseAllocationMaster.setUserId(nurseAllocationDto.getUserId());
			nurseAllocationMaster.setVisitTypeId(nurseAllocationDto.getVisitTypeId());
			if(nurseAllocationDto.getWardId()!=null)
			nurseAllocationMaster.setWardId(nurseAllocationDto.getWardId());
			if(nurseAllocationDto.getNursingStationId()!=null)
            nurseAllocationMaster.setNursingStationId(nurseAllocationDto.getNursingStationId());
			
			if(nurseAllocationDto.getSpecialityId()!=null)
				nurseAllocationMaster.setSpecialityId(nurseAllocationDto.getSpecialityId());
			if(nurseAllocationDto.getDoctorId()!=null)
				nurseAllocationMaster.setDoctorId(nurseAllocationDto.getDoctorId());
			if(nurseAllocationDto.getCabinId()!=null)
				nurseAllocationMaster.setCabinId(nurseAllocationDto.getCabinId());
			
			nurseAllocationMaster.setStatus(nurseAllocationDto.getStatus());
			nurseAllocationMaster.setOrganizationId(nurseAllocationDto.getOrganizationId());
			nurseAllocationMaster.setUnitId(nurseAllocationDto.getUnitId());
			nurseAllocationMaster.setCreatedBy(nurseAllocationDto.getCreatedBy());
			nurseAllocationMaster.setCreatedDate(GlobalCommonDateUtils.getDate(nurseAllocationDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			nurseAllocationMaster.setUpdatedBy(nurseAllocationDto.getUpdatedBy());
			nurseAllocationMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(nurseAllocationDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			 save(nurseAllocationMaster);
			 
			return new Response(SUCCESS, SUCCESS_CODE, "Nurse Allocation Done Successfully", null, null);
		}catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getNurseAllocationMasterList(NurseAllocationDto nurseAllocationDto) throws ApplicationException {
		//System.out.println(nurseAllocationDto.getVisitTypeId());
		try {
			List<NurseAllocationDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_NURSE_ALLOCATION_MASTER_LIST")
					.setParameter("visitTypeId", nurseAllocationDto.getVisitTypeId())
					.setParameter("orgId", nurseAllocationDto.getOrganizationId())
					.setParameter("unitId", nurseAllocationDto.getUnitId())
					.setFirstResult(nurseAllocationDto.getOffset() != null ? nurseAllocationDto.getOffset()  : 0)
					.setMaxResults(nurseAllocationDto.getNoOfRecordsPerPage() != null ? nurseAllocationDto.getNoOfRecordsPerPage()  : 10)
			.setResultTransformer(Transformers.aliasToBean(NurseAllocationDto.class)).list();
				return new Response<>(SUCCESS, null, null, list, null);
			
		}catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getNurseAllocationMasterByNameNotId(NurseAllocationDto nurseAllocationDto)
			throws ApplicationException {
		try {
			List<NurseAllocationDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_NURSE_ALLOCATION_MASTER_BY_NAME_NOT_BY_ID")
					.setParameter("userId", nurseAllocationDto.getUserId())
					.setParameter("nurseAllocationId", nurseAllocationDto.getNurseAllocationId())
					.setParameter("orgId", nurseAllocationDto.getOrganizationId())
					.setParameter("unitId", nurseAllocationDto.getUnitId())

					.setResultTransformer(Transformers.aliasToBean(NurseAllocationDto.class)).list();
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
	public Response updateNurseAllocationMaster(NurseAllocationDto nurseAllocationDto) throws ApplicationException {
		try {
			NurseAllocationMaster nurseAllocationMaster=findById(nurseAllocationDto.getNurseAllocationId());
			nurseAllocationMaster.setRoleId(nurseAllocationDto.getRoleId());
			nurseAllocationMaster.setUserId(nurseAllocationDto.getUserId());
			nurseAllocationMaster.setVisitTypeId(nurseAllocationDto.getVisitTypeId());
			if(nurseAllocationDto.getWardId()!=null)
			nurseAllocationMaster.setWardId(nurseAllocationDto.getWardId());
			if(nurseAllocationDto.getNursingStationId()!=null)
            nurseAllocationMaster.setNursingStationId(nurseAllocationDto.getNursingStationId());
			
			if(nurseAllocationDto.getSpecialityId()!=null)
				nurseAllocationMaster.setSpecialityId(nurseAllocationDto.getSpecialityId());
			if(nurseAllocationDto.getDoctorId()!=null)
				nurseAllocationMaster.setDoctorId(nurseAllocationDto.getDoctorId());
			if(nurseAllocationDto.getCabinId()!=null)
				nurseAllocationMaster.setCabinId(nurseAllocationDto.getCabinId());
			
			nurseAllocationMaster.setNurseAllocationId(nurseAllocationDto.getNurseAllocationId());
			nurseAllocationMaster.setStatus(nurseAllocationDto.getStatus());
			nurseAllocationMaster.setOrganizationId(nurseAllocationDto.getOrganizationId());
			nurseAllocationMaster.setUnitId(nurseAllocationDto.getUnitId());
			nurseAllocationMaster.setUpdatedBy(nurseAllocationDto.getUpdatedBy());
			nurseAllocationMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(nurseAllocationDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			 update(nurseAllocationMaster);
			 
			return new Response(SUCCESS, SUCCESS_CODE, "NurseAllocation Updated Successfully", null, null);
		}catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateNurseAllocationMasterStatus(NurseAllocationDto nurseAllocationDto)
			throws ApplicationException {
		try {
			NurseAllocationMaster nurseAllocationMaster=findById(nurseAllocationDto.getNurseAllocationId());
			nurseAllocationMaster.setNurseAllocationId(nurseAllocationDto.getNurseAllocationId());
			nurseAllocationMaster.setStatus(nurseAllocationDto.getStatus());
			nurseAllocationMaster.setUpdatedBy(nurseAllocationDto.getUpdatedBy());
			nurseAllocationMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(nurseAllocationDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			 update(nurseAllocationMaster);
			 
			return new Response(SUCCESS, SUCCESS_CODE, "Nurse Allocation Status Updated Successfully", null, null);
		}catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getNurseAllocationMasterCount(NurseAllocationDto nurseAllocationDto) throws ApplicationException {
		 try {
				Session session = sessionFactory.getCurrentSession();
				Criteria crit = session.createCriteria(NurseAllocationMaster.class);
				crit.add(Restrictions.eq("organizationId", nurseAllocationDto.getOrganizationId()));
				crit.add(Restrictions.eq("unitId", nurseAllocationDto.getUnitId()));
				crit.add(Restrictions.eq("visitTypeId", nurseAllocationDto.getVisitTypeId()));
				crit.setProjection(Projections.rowCount());
				Long count = (Long) crit.uniqueResult();
				
				return new Response(SUCCESS, SUCCESS_CODE, null, null, count);
					
			} catch (HibernateException he) {
			} catch (Exception e) {
				
			}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getNurseAllocationMasterById(Integer nurseAllocationId, Integer organizationId, Integer unitId)
			throws ApplicationException {
		try {
			NurseAllocationDto dto=(NurseAllocationDto)sessionFactory.getCurrentSession()
					.getNamedQuery("GET_NURSE_ALLOCATION_MASTER_BY_ID")
					.setParameter("nurseAllocationId", nurseAllocationId)
					.setParameter("orgId", organizationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(NurseAllocationDto.class)).uniqueResult();
			 return new Response(SUCCESS, SUCCESS_CODE, null, null, dto);
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getActiveNurseAllocationMasterByWardId(Integer wardId, Integer organizationId, Integer unitId)
			throws ApplicationException {
		try {
			List<NurseAllocationDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_NURSE_ALLOCATION_MASTER_LIST_BY_WARD_ID")
					.setParameter("wardId",wardId)
					.setParameter("orgId", organizationId)
					.setParameter("unitId", unitId)
			.setResultTransformer(Transformers.aliasToBean(NurseAllocationDto.class)).list();
				return new Response<>(SUCCESS, null, null, list, null);
			
		}catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getActiveNurseAllocationMasterByNurseId(Integer nurseId, Integer organizationId, Integer unitId) {
		try {
			List<NurseAllocationDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_NURSE_ALLOCATION_MASTER_LIST_BY_NURSE_ID")
					.setParameter("nurseId",nurseId)
					.setParameter("orgId", organizationId)
					.setParameter("unitId", unitId)
			.setResultTransformer(Transformers.aliasToBean(NurseAllocationDto.class)).list();
				return new Response<>(SUCCESS, null, null, list, null);
			
		}catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	}

