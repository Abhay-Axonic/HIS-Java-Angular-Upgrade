package com.param.global.dao;

import java.util.List;

import org.apache.log4j.Logger;
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
import com.param.global.common.ServiceException;
import com.param.global.dto.BedCategoryMasterDto;
import com.param.global.model.BedCategoryMaster;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
import com.param.global.service.VerifiedIdentityService;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class BedCategoryMasterDaoImpl extends GenericDao<BedCategoryMaster, Integer>
		implements IBedCategoryMasterDao, ICommonConstants {

	@Autowired
	private VerifiedIdentityService verifiedIdentityService;

	private static final Logger logger = Logger.getLogger(BedCategoryMasterDaoImpl.class);

	public BedCategoryMasterDaoImpl() {
		super(BedCategoryMaster.class);
		// TODO Auto-generated constructor stub
	}

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	DozerBeanMapper mapper;

	@Override
	public Response getBedCategoryByName(BedCategoryMasterDto bedCategoryMasterDto) throws ApplicationException {
		try {
			List<BedCategoryMasterDto> bedCategoryMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BED_CATEGORY_LIST_BY_NAME")
					.setParameter("bedCategoryDesc", bedCategoryMasterDto.getBedCategoryDesc().toLowerCase())
					.setParameter("orgId", bedCategoryMasterDto.getOrganizationId())
					.setParameter("unitId", bedCategoryMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(BedCategoryMasterDto.class)).list();
			if (bedCategoryMasterDtosList != null) {
				return new Response(SUCCESS, null, null, bedCategoryMasterDtosList, null);
			}
		} catch (HibernateException he) {
			logger.error("getBedCategoryByName failed", he);
		} catch (Exception e) {
			logger.error("getBedCategoryByName failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response addBedCategoryTypeMaster(BedCategoryMasterDto bedCategoryMasterDto) throws ApplicationException {
		try {

			BedCategoryMaster xBedCategoryMaster = mapper.map(bedCategoryMasterDto, BedCategoryMaster.class,
					"BedCategoryMasterDto_to_BedCategoryMaster");
			xBedCategoryMaster.setHierarchyId(bedCategoryMasterDto.getHierarchyId());
			xBedCategoryMaster.setCreatedDate(TimeZoneComponent.getDateByzone(bedCategoryMasterDto.getUnitId() != null ? bedCategoryMasterDto.getUnitId():0));
			xBedCategoryMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(bedCategoryMasterDto.getUnitId() != null ? bedCategoryMasterDto.getUnitId():0));
			xBedCategoryMaster.setIsBedRetention(xBedCategoryMaster.getIsBedRetention()=='\u0000'?'N':xBedCategoryMaster.getIsBedRetention());
			xBedCategoryMaster=save(xBedCategoryMaster);

			// update bed category to unit service master 
			if(bedCategoryMasterDto.getServiceId() !=null && bedCategoryMasterDto.getServiceId()>0) {
					sessionFactory.getCurrentSession().createSQLQuery("update service.t_unit_service_master_details " 
							+ "	set bed_category_id=:bedCategoryId "
							+ "	where service_master_id=:serviceId ")
					.setParameter("bedCategoryId", xBedCategoryMaster.getBedCategoryId())
					.setParameter("serviceId", bedCategoryMasterDto.getServiceId()).executeUpdate();
			}
			return new Response(SUCCESS, null, null, null, xBedCategoryMaster);
		} catch (HibernateException he) {
			logger.error("addBedCategoryTypeMaster failed", he);
		} catch (Exception e) {
			logger.error("addBedCategoryTypeMaster failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getBedCategoryMasterList(BedCategoryMasterDto bedCategoryMasterDto) throws ApplicationException {
		try {
			List<BedCategoryMasterDto> bedCategoryMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BED_CATEGORY_LIST")
					.setFirstResult(bedCategoryMasterDto.getOffset() != null ? bedCategoryMasterDto.getOffset() : 0)
					.setMaxResults(bedCategoryMasterDto.getNoOfRecordsPerPage() != null
							? bedCategoryMasterDto.getNoOfRecordsPerPage() : 10)
					.setParameter("orgId", bedCategoryMasterDto.getOrganizationId())
					.setParameter("unitId", bedCategoryMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(BedCategoryMasterDto.class)).list();
			if (bedCategoryMasterDtosList != null) {
				return new Response(SUCCESS, null, null, bedCategoryMasterDtosList, null);
			}
		} catch (HibernateException he) {
			logger.error("getBedCategoryMasterList failed", he);
		} catch (Exception e) {
			logger.error("getBedCategoryMasterList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getBedCategoryById(BedCategoryMasterDto bedCategoryMasterDto) throws ApplicationException {
		try {
			List<BedCategoryMasterDto> bedCategoryMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BED_CATEGORY_LIST_BY_ID")
					.setParameter("bedCategoryId", bedCategoryMasterDto.getBedCategoryId())
					.setParameter("orgId", bedCategoryMasterDto.getOrganizationId())
					.setParameter("unitId", bedCategoryMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(BedCategoryMasterDto.class)).list();
			if (bedCategoryMasterDtosList != null) {
				return new Response(SUCCESS, null, null, bedCategoryMasterDtosList, null);
			}
		} catch (HibernateException he) {
			logger.error("getBedCategoryById failed", he);
		} catch (Exception e) {
			logger.error("getBedCategoryById failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateBedCategoryStatus(BedCategoryMasterDto bedCategoryMasterDto) throws ApplicationException {
		try {
			BedCategoryMaster bedCategoryMaster = findById(bedCategoryMasterDto.getBedCategoryId());
			// BATCH-5 org-trust fix: status toggle was write-by-ID with zero org check.
			if (bedCategoryMaster == null || bedCategoryMaster.getOrganizationId() == null
					|| !bedCategoryMaster.getOrganizationId().equals(verifiedIdentityService.getVerifiedOrganizationId())) {
				return new Response<>(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
			}
			// BATCH-5 fix (live-proven 2026-08-04): the ternary wrote ACTIVE/INACTIVE ("ACTIVE" = 6
			// chars) into a varchar(1) column — every toggle failed with "value too long" (pre-existing,
			// toggle never worked). The legacy UI sends 'A'/'I'; persist that value directly.
			// BATCH-5 referential-integrity guard (live-proven 2026-08-04, same pattern as the StorageUnit
			// guard): deactivating a category that ACTIVE beds still reference silently orphaned those
			// beds (3 active beds on deactivated GEN before this guard). Block deactivation with a clear
			// message, mirroring StorageUnitService's "assigned to Items" convention.
			char newStatus = (bedCategoryMasterDto.getStatus() != null
					&& bedCategoryMasterDto.getStatus().charValue() == 'A') ? 'A' : 'I';
			if (newStatus == 'I') {
				Number bedCount = (Number) sessionFactory.getCurrentSession()
						.createSQLQuery("select count(*) from adt.m_bed_master where bed_category_id = :catId and status = 'A'")
						.setParameter("catId", bedCategoryMaster.getBedCategoryId())
						.uniqueResult();
				Long activeBeds = bedCount != null ? bedCount.longValue() : 0L;
				if (activeBeds > 0) {
					return new Response<>(ERROR, null, "Cannot deactivate: " + activeBeds + " active bed(s) still reference this category", null, null);
				}
			}
			bedCategoryMaster.setStatus(newStatus);
			bedCategoryMaster.setUpdatedBy(bedCategoryMasterDto.getUpdatedBy());
			bedCategoryMaster.setUpdatedDate(
			GlobalCommonDateUtils.getDate(bedCategoryMasterDto.getUpdatedDate(), "dd-M-yyy HH:mm:ss"));
			update(bedCategoryMaster);
			return new Response<BedCategoryMasterDto, Integer>(SUCCESS, null, COMMON_SUCCESS, null, bedCategoryMaster.getUnitId());

		} catch (HibernateException he) {
			logger.error("updateBedCategoryStatus failed", he);
			return new Response<BedCategoryMasterDto, Integer>(ERROR, null, null, null, null);
		} catch (Exception e) {
			logger.error("updateBedCategoryStatus failed", e);
			return new Response<BedCategoryMasterDto, Integer>(ERROR, null, null, null, null);
		}
	}

	@Override
	public Response getBedCategoryByNameNotId(BedCategoryMasterDto bedCategoryMasterDto) throws ApplicationException {
		try {
			List<BedCategoryMasterDto> bedCategoryMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BED_CATEGORY_LIST_BY_NAME_NOT_ID")
					.setParameter("bedCategoryId", bedCategoryMasterDto.getBedCategoryId())
					.setParameter("bedCategoryDesc", bedCategoryMasterDto.getBedCategoryDesc())
					.setParameter("orgId", bedCategoryMasterDto.getOrganizationId())
					.setParameter("unitId", bedCategoryMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(BedCategoryMasterDto.class)).list();
			if (bedCategoryMasterDtosList != null) {
				return new Response(SUCCESS, null, null, bedCategoryMasterDtosList, null);
			}
		} catch (HibernateException he) {
			logger.error("getBedCategoryByNameNotId failed", he);
		} catch (Exception e) {
			logger.error("getBedCategoryByNameNotId failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateBedCategoryMaster(BedCategoryMasterDto bedCategoryMasterDto) throws ApplicationException {
		try {
			BedCategoryMaster bedCategoryMaster = findById(bedCategoryMasterDto.getBedCategoryId());
			// BATCH-5 org-trust fix (CRUD-sibling pattern, same class as BR-146): write-by-ID with
			// zero org check — a forged cross-tenant ID would overwrite a victim row. Reject rows
			// not owned by the verified tenant.
			if (bedCategoryMaster == null || bedCategoryMaster.getOrganizationId() == null
					|| !bedCategoryMaster.getOrganizationId().equals(verifiedIdentityService.getVerifiedOrganizationId())) {
				return new Response<>(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
			}
			bedCategoryMaster.setBedCategoryCode(bedCategoryMasterDto.getBedCategoryCode());
			bedCategoryMaster.setBedCategoryDesc(bedCategoryMasterDto.getBedCategoryDesc());
			bedCategoryMaster.setHierarchyId(bedCategoryMasterDto.getHierarchyId());
			bedCategoryMaster.setIsBedRetention(bedCategoryMasterDto.getIsBedRetention());
			bedCategoryMaster.setOccupancyUnitId(bedCategoryMasterDto.getOccupancyUnitId());
			bedCategoryMaster.setBillingBedCategoryId(bedCategoryMasterDto.getBillingBedCategoryId());
			bedCategoryMaster.setServiceId(bedCategoryMasterDto.getServiceId());
			// bedCategoryMaster.setOrganizationId(bedCategoryMasterDto.getOrganizationId());
			bedCategoryMaster.setIcu(bedCategoryMasterDto.getIsIcu());
			bedCategoryMaster.setUpdatedBy(bedCategoryMasterDto.getUpdatedBy());
			bedCategoryMaster.setUpdatedDate(
					GlobalCommonDateUtils.getDate(bedCategoryMasterDto.getUpdatedDate(), "dd-M-yyy HH:mm:ss"));
			update(bedCategoryMaster);
			
			// update bed category to unit service master 
			      
			         
					sessionFactory.getCurrentSession().createSQLQuery("update service.t_unit_service_master_details  " 
							+ "	set bed_category_id = null "
							+ "	where bed_category_id=:bedCategoryId ")
					.setParameter("bedCategoryId", bedCategoryMasterDto.getBedCategoryId()).executeUpdate();
					
					if(bedCategoryMasterDto.getServiceId()!=null && bedCategoryMasterDto.getServiceId()>0) {
						sessionFactory.getCurrentSession().createSQLQuery("update service.t_unit_service_master_details " 
								+ "	set bed_category_id=:bedCategoryId "
								+ "	where service_master_id=:serviceId ")
						.setParameter("bedCategoryId", bedCategoryMasterDto.getBedCategoryId())
						.setParameter("serviceId", bedCategoryMasterDto.getServiceId()).executeUpdate();
					}
			return new Response<BedCategoryMasterDto, Integer>(SUCCESS, null, COMMON_SUCCESS, null, bedCategoryMaster);

		} catch (HibernateException he) {
			logger.error("updateBedCategoryMaster failed", he);
			return new Response<BedCategoryMasterDto, Integer>(ERROR, null, null, null, null);
		} catch (Exception e) {
			logger.error("updateBedCategoryMaster failed", e);
			return new Response<BedCategoryMasterDto, Integer>(ERROR, null, null, null, null);
		}
	}

	@Override
	public Response getActiveBedCategoryList() throws ApplicationException {
		try {
			List<BedCategoryMasterDto> bedCategoryMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_BED_CATEGORY_LIST")
					.setResultTransformer(Transformers.aliasToBean(BedCategoryMasterDto.class)).list();
			if (bedCategoryMasterDtosList != null) {
				return new Response(SUCCESS, null, null, bedCategoryMasterDtosList, null);
			}
		} catch (HibernateException he) {
			logger.error("getActiveBedCategoryList failed", he);
		} catch (Exception e) {
			logger.error("getActiveBedCategoryList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	public Response getCount(BedCategoryMasterDto bedCategoryMasterDto) throws ApplicationException {
		try {
			Session session = sessionFactory.getCurrentSession();
			Criteria c = session.createCriteria(BedCategoryMaster.class);
			c.add(Restrictions.eq("organizationId", bedCategoryMasterDto.getOrganizationId()));
			c.add(Restrictions.eq("unitId",bedCategoryMasterDto.getUnitId()));
			c.setProjection(Projections.rowCount());
			Long count = (Long) c.uniqueResult();
			return new Response(SUCCESS, null, null, null, count);
		} catch (Exception e) {
		logger.error("getCount failed", e);
			return new Response(ERROR, null, null, null, null);
		}
	}

	@Override
	public Response getAvailableBedCategoryAccordingToVacateBeds(BedCategoryMasterDto bedCategoryMasterDto) {
		try {
			
			String query = "select "
					 + "	bc.bed_category_id as \"bedCategoryId\", "
					 + "	bc.hierarchy_id as \"hierarchyId\", "
					 + "	bc.occupancy_unit_id as \"occupancyUnitId\", "
					 + "	bc.bed_category_code as \"bedCategoryCode\", "
					 + "	bc.bed_category_desc as \"bedCategoryDesc\", "
					 + "	bc.is_bed_retention as \"isBedRetention\", "
					 + "	bc.status as \"status\", "
					 + "	ou.occupancy_unit_desc as \"occupancyUnitDesc\", "
					 + "	bbc.billing_bed_category_id as \"billingBedCategoryId\", "
					 + "	bbc.billing_bed_desc as \"billingBedDesc\", "
					 + "	h.hierarchy_desc as \"hierarchyDesc\", "
					 + "	bc.organization_id as \"organizationId\", "
					 + "	bc.unit_id as \"unitId\" "
					 + "from "
					 + "	adt.m_bed_category_master as bc "
					 + "inner join adt.m_hierarchy_master as h on "
					 + "	bc.hierarchy_id = h.hierarchy_id "
					 + "inner join public.m_occupancy_unit_master as ou on "
					 + "	bc.occupancy_unit_id = ou.occupancy_unit_id "
					 + "inner join adt.m_billing_bed_category_master as bbc on "
					 + "	bc.billing_bed_category_id = bbc.billing_bed_category_id "
					 + "where "
					 + "	bc.organization_id =:orgId "
					 + "	and bc.unit_id =:unitId "
					 + "	and bc.status = 'A' "
					 + "	and bc.bed_category_id in (select distinct(bed_category_id) from adt.m_bed_master where bed_status_id=3 and organization_id=bc.organization_id and unit_id=bc.unit_id) "
					 + " order by bc.bed_category_desc ASC";
				
			
			
			List<BedCategoryMasterDto> bedCategoryMasterDtosList = sessionFactory.getCurrentSession()
					.createSQLQuery(query)
					.setParameter("orgId", bedCategoryMasterDto.getOrganizationId())
					.setParameter("unitId", bedCategoryMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(BedCategoryMasterDto.class)).list();
			
			
			List<BedCategoryMasterDto> bedCategoryList = sessionFactory.getCurrentSession()
					        .createSQLQuery("select bb.bed_category_id as \"bedCategoryId\",bbvm.visit_type_id as \"visitTypeId\" "
							+ " from adt.t_billing_bed_visit_type_mapper  bbvm "
							+ "inner join adt.m_bed_category_master bb on bb.billing_bed_category_id=bbvm.billing_bed_category_id "
							+ "where bbvm.visit_type_id IN(2,3) and bbvm.status='A' ")
					.setResultTransformer(Transformers.aliasToBean(BedCategoryMasterDto.class)).list();
			       bedCategoryMasterDto.setDayCareIpBedCategoryList(bedCategoryList);
	      
			if (bedCategoryMasterDtosList != null) {
				return new Response(SUCCESS, null, null, bedCategoryMasterDtosList, bedCategoryMasterDto);
			}
			
		} catch (HibernateException he) {
		logger.error("getAvailableBedCategoryAccordingToVacateBeds failed", he);
		} catch (Exception e) {
		logger.error("getAvailableBedCategoryAccordingToVacateBeds failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateBedCategoryMasterHierarchy(BedCategoryMasterDto bedCategoryMasterDto1) {
		try {
			BedCategoryMasterDto bedCategoryMasterDto=bedCategoryMasterDto1;
			BedCategoryMaster bedCategoryMaster = findById(bedCategoryMasterDto.getBedCategoryId());
			// BATCH-5 org-trust fix: hierarchy update was write-by-ID with zero org check.
			if (bedCategoryMaster == null || bedCategoryMaster.getOrganizationId() == null
					|| !bedCategoryMaster.getOrganizationId().equals(verifiedIdentityService.getVerifiedOrganizationId())) {
				return new Response<>(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
			}
			bedCategoryMaster.setBedCategoryCode(bedCategoryMasterDto.getBedCategoryCode());
			bedCategoryMaster.setBedCategoryDesc(bedCategoryMasterDto.getBedCategoryDesc());
			bedCategoryMaster.setHierarchyId(bedCategoryMasterDto.getHierarchyId());
			bedCategoryMaster.setIsBedRetention(bedCategoryMasterDto.getIsBedRetention());
			bedCategoryMaster.setOccupancyUnitId(bedCategoryMasterDto.getOccupancyUnitId());
			bedCategoryMaster.setBillingBedCategoryId(bedCategoryMasterDto.getBillingBedCategoryId());
			bedCategoryMaster.setServiceId(bedCategoryMasterDto.getServiceId());
			// bedCategoryMaster.setOrganizationId(bedCategoryMasterDto.getOrganizationId());
			bedCategoryMaster.setIcu(bedCategoryMasterDto.getIsIcu());
			bedCategoryMaster.setUpdatedBy(bedCategoryMasterDto.getUpdatedBy());
			bedCategoryMaster.setUpdatedDate(
					GlobalCommonDateUtils.getDate(bedCategoryMasterDto.getUpdatedDate(), "dd-M-yyy HH:mm:ss"));
			sessionFactory.getCurrentSession().update(bedCategoryMaster);
			
			// update bed category to unit service master 
			      
			         
					sessionFactory.getCurrentSession().createSQLQuery("update service.t_unit_service_master_details  " 
							+ "	set bed_category_id = null "
							+ "	where bed_category_id=:bedCategoryId ")
					.setParameter("bedCategoryId", bedCategoryMasterDto.getBedCategoryId()).executeUpdate();
					
					if(bedCategoryMasterDto.getServiceId()!=null && bedCategoryMasterDto.getServiceId()>0) {
						sessionFactory.getCurrentSession().createSQLQuery("update service.t_unit_service_master_details " 
								+ "	set bed_category_id=:bedCategoryId "
								+ "	where service_master_id=:serviceId ")
						.setParameter("bedCategoryId", bedCategoryMasterDto.getBedCategoryId())
						.setParameter("serviceId", bedCategoryMasterDto.getServiceId()).executeUpdate();
					}
			return new Response<BedCategoryMasterDto, Integer>(SUCCESS, null, COMMON_SUCCESS, null, null);

		} catch (HibernateException he) {
		logger.error("updateBedCategoryMasterHierarchy failed", he);
			return new Response<BedCategoryMasterDto, Integer>(ERROR, null, null, null, null);
		} catch (Exception e) {
		logger.error("updateBedCategoryMasterHierarchy failed", e);
			return new Response<BedCategoryMasterDto, Integer>(ERROR, null, null, null, null);
		}
	}

}
