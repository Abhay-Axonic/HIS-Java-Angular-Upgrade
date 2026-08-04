package com.param.adt.master.unit.dao;

import java.util.ArrayList;

import java.util.List;

import org.dozer.DozerBeanMapper;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.adt.admission.model.WardGenderMapper;
import com.param.adt.master.unit.dto.WardGenderMapperDto;
import com.param.adt.master.unit.dto.WardMasterDto;
import com.param.adt.master.unit.model.WardMaster;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.MultiSelectDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
import com.param.global.service.VerifiedIdentityService;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class WardMasterDaoImpl extends GenericDao<WardMaster, Integer> implements IWardMasterDao, ICommonConstants {

	@Autowired
	private VerifiedIdentityService verifiedIdentityService;

	public WardMasterDaoImpl() {
		super(WardMaster.class);
	}

	@Autowired
	SessionFactory sessionFactory;
	@Autowired
	private DozerBeanMapper mapper;

	@Override
	public Response saveWardMasterMaster(WardMasterDto wardMasterDto) throws ApplicationException {
		try {
			WardMaster wardMaster = mapper.map(wardMasterDto, WardMaster.class, "UnitWardMasterDto_to_UnitWardMaster");
			// BATCH-5 org-trust fix: save previously wrote no org/unit (mapper doesn't carry them from
			// the DTO) — new wards would be NULL-org, invisible to their own tenant's scoped queries.
			wardMaster.setOrganizationId(verifiedIdentityService.getVerifiedOrganizationId());
			wardMaster.setUnitId(verifiedIdentityService.getVerifiedUnitId());
			wardMaster.setCreatedDate(TimeZoneComponent.getDateByzone(wardMasterDto.getUnitId() != null ? wardMasterDto.getUnitId() :0));
			wardMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(wardMasterDto.getUnitId() != null ? wardMasterDto.getUnitId() :0));
			wardMaster.setStatus('A');
			wardMaster.setIsDayCare(wardMaster.getIsDayCare()=='\u0000'?'N':wardMaster.getIsDayCare());
			wardMaster.setIsEr(wardMaster.getIsEr()=='\u0000'?'N':wardMaster.getIsEr());
			wardMaster.setIsIcu(wardMaster.getIsIcu()=='\u0000'?'N':wardMaster.getIsIcu());
			save(wardMaster);
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, wardMaster.getWardId());
		} catch (HibernateException he) {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getWardMasterById(int wardId, int orgId, int unitId) throws ApplicationException {
		try {
			List<WardMasterDto> WardMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_WARD_MASTER_BY_ID")
					.setParameter("wardId", wardId)
					.setParameter("orgId", orgId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(WardMasterDto.class))
					.list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, WardMasterDtoList, null);
		} catch (HibernateException he) {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getWardMasterList(WardMasterDto wardMasterDto) throws ApplicationException {
		try {
			List<WardMasterDto> WardMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_WARD_MASTER_LIST").setParameter("orgId", wardMasterDto.getOrganizationId())
					.setParameter("unitId", wardMasterDto.getUnitId())
					.setFirstResult(wardMasterDto.getOffset() != null ? wardMasterDto.getOffset() : 0)
					.setMaxResults(
							wardMasterDto.getNoOfRecordsPerPage() != null ? wardMasterDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(WardMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, WardMasterDtoList, null);
		} catch (HibernateException he) {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateWardMaster(WardMasterDto wardMasterDto) throws ApplicationException {
		try {
			WardMaster wardMaster = findById(wardMasterDto.getWardId());
			// BATCH-5 org-trust fix (write-by-ID class, same as BR-146/bedCategoryMaster): reject rows
			// not owned by the verified tenant.
			if (wardMaster == null || wardMaster.getOrganizationId() == null
					|| !wardMaster.getOrganizationId().equals(verifiedIdentityService.getVerifiedOrganizationId())) {
				return new Response<>(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
			}
			wardMaster.setFloorId(wardMasterDto.getFloorId());
			wardMaster.setWardCode(wardMasterDto.getWardCode());
			wardMaster.setWardName(wardMasterDto.getWardName());
			wardMaster.setGenderId(wardMasterDto.getGenderId());
			// BATCH-5 fix (pre-existing NUL-crash class, sibling of the save's guards): the update
			// copied DTO primitive-char fields raw — omitted fields default to '\u0000', and Hibernate
			// binds the NUL into char(1) columns → 'invalid byte sequence 0x00' (live-proven). Apply the
			// same NUL-guards the save already has.
			wardMaster.setIsEr(wardMasterDto.getIsEr() == '\u0000' ? 'N' : wardMasterDto.getIsEr());
			wardMaster.setIsIcu(wardMasterDto.getIsIcu() == '\u0000' ? 'N' : wardMasterDto.getIsIcu());
			wardMaster.setIcuTypeId(wardMasterDto.getIcuTypeId());
			wardMaster.setIsDayCare(wardMasterDto.getIsDayCare() == '\u0000' ? 'N' : wardMasterDto.getIsDayCare());
			wardMaster.setIsIpWard(wardMasterDto.getIsIpWard() == null || wardMasterDto.getIsIpWard() == '\u0000' ? 'N' : wardMasterDto.getIsIpWard());
			wardMaster.setIslaborWard(wardMasterDto.getIslaborWard() == null || wardMasterDto.getIslaborWard() == '\u0000' ? 'N' : wardMasterDto.getIslaborWard());
			wardMaster.setIshemodialysisward(wardMasterDto.getIshemodialysisward() == null || wardMasterDto.getIshemodialysisward() == '\u0000' ? 'N' : wardMasterDto.getIshemodialysisward());
//			wardMaster.setStoreId(wardMasterDto.getStoreId());
			wardMaster.setUpdatedBy(wardMasterDto.getUpdatedBy());
			wardMaster.setUpdatedDate(
					GlobalCommonDateUtils.getDate(wardMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			update(wardMaster);
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, wardMaster);
		} catch (HibernateException he) {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateWardMasterStatus(WardMasterDto wardMasterDto) throws ApplicationException {
		try {
			WardMaster wardMaster = findById(wardMasterDto.getWardId());
			// BATCH-5 org-trust fix (write-by-ID class): reject rows not owned by the verified tenant.
			if (wardMaster == null || wardMaster.getOrganizationId() == null
					|| !wardMaster.getOrganizationId().equals(verifiedIdentityService.getVerifiedOrganizationId())) {
				return new Response<>(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
			}
			// BATCH-5 fix: ternary wrote ACTIVE/INACTIVE into varchar(1) — toggle never worked (same
			// pre-existing bug as bedCategoryMaster). Persist the UI's 'A'/'I' directly.
			wardMaster.setStatus(wardMasterDto.getStatus() == 'A' ? 'A' : 'I');
			wardMaster.setUpdatedBy(wardMasterDto.getUpdatedBy());
			wardMaster.setUpdatedDate(
					GlobalCommonDateUtils.getDate(wardMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			update(wardMaster);
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, wardMaster.getUnitId());
		} catch (HibernateException he) {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getWardMasterCount(WardMasterDto wardMasterDto) throws ApplicationException {
		try {
			Session session = sessionFactory.getCurrentSession();
			Criteria c = session.createCriteria(WardMaster.class);
			c.add(Restrictions.eq("organizationId", wardMasterDto.getOrganizationId()));
			c.add(Restrictions.eq("unitId", wardMasterDto.getUnitId()));
			c.setProjection(Projections.rowCount());
			Long count = (Long) c.uniqueResult();
			return new Response(SUCCESS, null, null, null, count);
		} catch (HibernateException he) {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getWardMasterByName(WardMasterDto wardMasterDto) throws ApplicationException {
		try {
			List<WardMasterDto> WardMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_WARD_MASTER_BY_NAME")
					.setParameter("orgId", wardMasterDto.getOrganizationId())
					.setParameter("unitId", wardMasterDto.getUnitId())
					.setParameter("wardName", wardMasterDto.getWardName().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(WardMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, WardMasterDtoList, null);
		} catch (HibernateException he) {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getWardMasterByNameNotById(WardMasterDto wardMasterDto) throws ApplicationException {
		try {
			List<WardMasterDto> WardMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_WARD_MASTER_BY_NAME_NOT_BY_ID")
					.setParameter("wardId", wardMasterDto.getWardId())
					.setParameter("wardName", wardMasterDto.getWardName().toLowerCase())
					.setParameter("orgId", wardMasterDto.getOrganizationId())
					.setParameter("unitId", wardMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(WardMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, WardMasterDtoList, null);
		} catch (HibernateException he) { he.printStackTrace(); } catch (Exception e) { e.printStackTrace(); }
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getActiveWardMasterList(WardMasterDto wardMasterDto) throws ApplicationException {
		try {
			List<WardMasterDto> WardMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_WARD_MASTER_LIST")
					.setParameter("orgId", wardMasterDto.getOrganizationId())
					.setParameter("unitId", wardMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(WardMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, WardMasterDtoList, null);
		} catch (HibernateException he) {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getWardListNotMappedToNursingStation(WardMasterDto wardMasterDto) throws ApplicationException {
		try {
			List<WardMasterDto> WardMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_WARD_LIST_NOT_MAPPED_TO_NURSING_STATION")
					.setParameter("orgId", wardMasterDto.getOrganizationId())
					.setParameter("unitId", wardMasterDto.getUnitId())
					.setParameter("nursingId", wardMasterDto.getNursingStationId())
					.setResultTransformer(Transformers.aliasToBean(WardMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, WardMasterDtoList, null);
		} catch (HibernateException he) {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response saveWardGenderMapper(WardGenderMapperDto wardGenderMapperDto) throws ApplicationException {
		try {
			WardGenderMapper wardGenderMapper = mapper.map(wardGenderMapperDto, WardGenderMapper.class, "WardGenderMapperDto_to_WardGenderMapper");
			wardGenderMapper.setCreatedDate(TimeZoneComponent.getDateByzone(0));
			wardGenderMapper.setUpdatedDate(TimeZoneComponent.getDateByzone(0));
			sessionFactory.getCurrentSession().save(wardGenderMapper);
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (HibernateException he) {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getGenderListByWardId(int wardId) throws ApplicationException {
		try {
			List<MultiSelectDto> WardMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_GENDER_LIST_BY_WARD_ID")
					.setParameter("wardId", wardId)
					.setResultTransformer(Transformers.aliasToBean(MultiSelectDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, WardMasterDtoList, null);
		} catch (HibernateException he) {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateWardGenderStatus(int wardId) throws ApplicationException {
		try {
			sessionFactory.getCurrentSession().createQuery("update WardGenderMapper set status='I' where wardId=:wardId ")
			.setParameter("wardId",wardId)
			.executeUpdate();
		} catch (HibernateException he) {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getActiveWardListForER(WardMasterDto wardMasterDto) throws ApplicationException {
		try {
			
			List<WardMasterDto> wardMasterDtoList1 =new ArrayList<>();

			List<WardMasterDto> wardMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_WARD_MASTER_LIST_FOR_ER")
					.setParameter("orgId", wardMasterDto.getOrganizationId())
					.setParameter("unitId", wardMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(WardMasterDto.class)).list();
			
			for(WardMasterDto dto : wardMasterDtoList) {
				List<WardMasterDto> wardMasterDtoList2 = sessionFactory.getCurrentSession()
						.getNamedQuery("GET_ACTIVE_ROOM_MASTER_LIST_FOR_ER_BY_WARD_ID")
						.setParameter("orgId", wardMasterDto.getOrganizationId())
						.setParameter("wardId", dto.getWardId())
						.setParameter("unitId", wardMasterDto.getUnitId())
						.setResultTransformer(Transformers.aliasToBean(WardMasterDto.class)).list();
				dto.setWardMasterDtosList(wardMasterDtoList2);
				wardMasterDtoList1.add(dto);
				
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, wardMasterDtoList, null);
		} catch (HibernateException he) {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getWardForDirectTransfer(int genderId, int visitTypeId, int unitId) {
		try {
			
			StringBuilder query = new StringBuilder("select " 
					 + " 	mwm.ward_id as \"wardId\", " 
					 + "    mwm.is_day_care as \"isDayCare\", " 
					 + " 	mwm.ward_name as \"wardName\" " 
					 + " from " 
					 + " 	adt.m_ward_master mwm " 
					 + " where " 
					 + " 	mwm.unit_id = :unitId " 
					 + " 	and mwm.status = 'A' " 
					 + " 	and ward_id in ( " 
					 + " 	select " 
					 + " 		ward_id " 
					 + " 	from " 
					 + " 		adt.t_ward_gender_mapper twgm " 
					 + " 	where " 
					 + " 		gender_id =:genderId " 
					 + " 		and status = 'A' " 
					 + " 		and unit_id =:unitId ) ");
					 
			
			
			/*if(visitTypeId==2)
				query.append(" and mwm.is_ip_ward='Y'");HIS-24051
			else if (visitTypeId==3)
				query.append(" and mwm.is_day_care='Y'");*/
				
				query.append(" order by " 
				 + " 	mwm.ward_name  ASC");
				
			List<WardMasterDto> WardMasterDtoList = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
					.setParameter("genderId", genderId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(WardMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, WardMasterDtoList, null);
		
		} catch (HibernateException he) {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getWardByVisitType(WardMasterDto wardMasterDto) throws ApplicationException {
try {
			
			StringBuilder str=new StringBuilder();
			
			if(wardMasterDto.getIsEr() == 'Y')
			{
				str.append(" AND wd.is_er=:getIsEr ");
			}
			if(wardMasterDto.getIsIpWard()  == 'Y')
			{
				str.append(" AND wd.is_ip_ward=:getIsIpWard ");
			}
			if(wardMasterDto.getIsDayCare()  == 'Y')
			{
				str.append(" AND wd.is_day_care =:getIsDayCare ");
			}
			
		
			String query1 = "select  " + 
					"	wd.ward_name as \"wardName\" ,  " + 
					"	wd.ward_id as \"wardId\"  " + 
					" from  " + 
					"	adt.m_ward_master wd  " + 
					" where wd.unit_id =:unitId  " + 
					" and organization_id =:orgId"
					+  str.toString() +  // Append the conditions
                " ORDER BY wd.ward_name ASC";               //ORDER BY wd.ward_name ASC
			
			//String queryN = query1.concat(str.toString());	
			
			List<WardMasterDto> list=new ArrayList<>();
				Query query = sessionFactory.getCurrentSession().createSQLQuery(query1)
						.setParameter("orgId", wardMasterDto.getOrganizationId())
						.setParameter("unitId", wardMasterDto.getUnitId());
				if(wardMasterDto.getIsEr() == 'Y')
				{
					query.setParameter("getIsEr", wardMasterDto.getIsEr());
				}
				if(wardMasterDto.getIsIpWard()  == 'Y')
				{
					query.setParameter("getIsIpWard", wardMasterDto.getIsIpWard());
				}
				if(wardMasterDto.getIsDayCare()  == 'Y')
				{
					query.setParameter("getIsDayCare", wardMasterDto.getIsDayCare());
				}
				query.setResultTransformer(Transformers.aliasToBean(WardMasterDto.class));
				list = query.list();
			
				return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, list, null);	

			
		} catch (HibernateException he) {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new Response(ERROR, null, null, null, null);
	}
	


}
