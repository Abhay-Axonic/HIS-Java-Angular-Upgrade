package com.param.pharmacy.master.global.dao.equipment;

import java.math.BigInteger;
import java.util.List;

import org.apache.log4j.Logger;
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

import com.param.entity.model.inventory.WardStoreMapper;
import com.param.global.common.ICommonConstants;
import com.param.global.common.IError;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.model.DepartmentStoreMapper;
import com.param.pharmacy.master.global.dto.equipment.DepartmentStoreMapperDto;
import com.param.pharmacy.master.global.dto.equipment.WardStoreMapperDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DepartmentStoreMapperDaoImpl extends GenericDao<DepartmentStoreMapper, Integer>
		implements IDepartmentStoreMapperDao, ICommonConstants, IError {

	public DepartmentStoreMapperDaoImpl() {
		super(DepartmentStoreMapper.class);
	}

	@Autowired
	private SessionFactory sessionFactory;

	final static Logger logger = Logger.getLogger(DepartmentStoreMapper.class);

	@Override
	public Response getDeptListById(DepartmentStoreMapperDto departmentStoreMapperDto) throws ApplicationException {
		try {
			List<DepartmentStoreMapperDto> deptList = sessionFactory.getCurrentSession()
					.createSQLQuery("select sm.speciality_id as \"deptId\",sm.speciality_name as \"deptName\" "
							+ " from t_unit_speciality_mapper usm "
							+ " join doctor.m_speciality_master sm on sm.speciality_id=usm.speciality_id "
							+ " where usm.organization_id=:orgId " + " and usm.unit_id=:unitId "
							+ " and usm.status='A' and sm.status='A'"
							+ " ORDER BY sm.speciality_name ASC ")
					.setParameter("orgId", departmentStoreMapperDto.getOrgId())
					.setParameter("unitId", departmentStoreMapperDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(DepartmentStoreMapperDto.class)).list();
			return new Response(SUCCESS, null, null, deptList, null);
		} catch (Exception e) {
			logger.error("Exection", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}

	@Override
	public Response saveDeptStoreMapper(DepartmentStoreMapperDto departmentStoreMapperDto) throws ApplicationException {
		try {

			DepartmentStoreMapper departmentStoreMapper = new DepartmentStoreMapper();
			Integer deptId = (Integer) sessionFactory.getCurrentSession().createSQLQuery(
					" select dept_id from pharmacy.t_dept_store_mapper where status='A' and unit_id=:unitId and org_id=:orgId and dept_id=:dept_id and store_id=:storeId ")
					.setParameter("orgId", departmentStoreMapperDto.getOrgId())
					.setParameter("unitId", departmentStoreMapperDto.getUnitId())
					.setParameter("dept_id", departmentStoreMapperDto.getDeptId())
					.setParameter("storeId", departmentStoreMapperDto.getId()).uniqueResult();
			//System.out.println(deptId);
			if (deptId == null) {
				departmentStoreMapper.setDeptId(departmentStoreMapperDto.getDeptId());
				departmentStoreMapper.setStoreId(departmentStoreMapperDto.getId());
				departmentStoreMapper.setUnitId(departmentStoreMapperDto.getUnitId());
				departmentStoreMapper.setOrgId(departmentStoreMapperDto.getOrgId());
				departmentStoreMapper.setStatus(departmentStoreMapperDto.getStatus());
				departmentStoreMapper.setCreatedBy(departmentStoreMapperDto.getCreatedBy());
				departmentStoreMapper.setCreatedDate(TimeZoneComponent.getDateByzone(departmentStoreMapperDto.getUnitId()));
				departmentStoreMapper.setUpdatedBy(departmentStoreMapperDto.getUpdatedBy());
				departmentStoreMapper.setUpdatedDate(TimeZoneComponent.getDateByzone(departmentStoreMapperDto.getUnitId()));
				sessionFactory.getCurrentSession().save(departmentStoreMapper);
				return new Response<>(SUCCESS, null, null, null, null);
			}
			return new Response<>(ERROR, ERR_500, "Department Store Already Mapped", null, null);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public Response getDeptStoremappedList(DepartmentStoreMapperDto departmentStoreMapperDto)
			throws ApplicationException {

		try {
			List<DepartmentStoreMapperDto> deptList = sessionFactory.getCurrentSession().createSQLQuery(
					"SELECT dsm.dept_store_mapper_id as \"deptStoreMapperId\", dsm.store_id as \"storeId\",s.store_name as \"storeName\", dsm.dept_id as \"deptId\",sm.speciality_name as \"deptName\", dsm.status as \"status\"   "
							+ " FROM pharmacy.t_dept_store_mapper dsm "
							+ " join public.m_store s on  dsm.store_id =s.store_id "
							+ " join doctor.m_speciality_master sm on sm.speciality_id=dsm.dept_id"
							+ " where dsm.org_id=:orgId " + " and dsm.unit_id=:unitId " + " and dsm.status='A' ")
					.setParameter("orgId", departmentStoreMapperDto.getOrgId())
					.setParameter("unitId", departmentStoreMapperDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(DepartmentStoreMapperDto.class)).list();

			return new Response(SUCCESS, null, null, deptList, null);
		} catch (Exception e) {
			logger.error("Exection", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}

	}

	@Override
	public List<DepartmentStoreMapperDto> getDeptStoremappedListForStoreUpdate(DepartmentStoreMapperDto departmentStoreMapperDto)
			throws ApplicationException {

			List<DepartmentStoreMapperDto> deptList = sessionFactory.getCurrentSession().createSQLQuery(
					"SELECT dsm.dept_store_mapper_id as \"deptStoreMapperId\", dsm.store_id as \"storeId\",s.store_name as \"storeName\", dsm.dept_id as \"deptId\",sm.speciality_name as \"deptName\", dsm.status as \"status\"   "
							+ " FROM pharmacy.t_dept_store_mapper dsm "
							+ " join public.m_store s on  dsm.store_id =s.store_id "
							+ " join doctor.m_speciality_master sm on sm.speciality_id=dsm.dept_id"
							+ " where dsm.org_id=:orgId " + " and dsm.unit_id=:unitId " + " and dsm.status='A' and s.store_id = :storeId")
					.setParameter("orgId", departmentStoreMapperDto.getOrgId())
					.setParameter("unitId", departmentStoreMapperDto.getUnitId())
					.setParameter("storeId",departmentStoreMapperDto.getStoreId())
					.setResultTransformer(Transformers.aliasToBean(DepartmentStoreMapperDto.class)).list();

			return deptList;

	}

	@Override
	public Response getDeptStoremappings(Integer unitId, Integer storeId) throws ApplicationException {
		try {
			List<DepartmentStoreMapperDto> deptList = sessionFactory.getCurrentSession().createSQLQuery(
					"SELECT dsm.dept_store_mapper_id as \"deptStoreMapperId\", dsm.store_id as \"storeId\",s.store_name as \"storeName\", dsm.dept_id as \"deptId\",sm.speciality_name as \"deptName\", dsm.status as \"status\"   "
							+ " FROM pharmacy.t_dept_store_mapper dsm "
							+ " join public.m_store s on  dsm.store_id =s.store_id "
							+ " join doctor.m_speciality_master sm on sm.speciality_id=dsm.dept_id"
							+ " and dsm.unit_id=:unitId " + " and dsm.status='A' and s.store_id = :storeId ")
					.setParameter("storeId", storeId).setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(DepartmentStoreMapperDto.class)).list();

			return new Response(SUCCESS, null, null, deptList, null);
		} catch (Exception e) {
			logger.error("Exection", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}

	}

	@Override
	public Response activeInactiveTrustMaster(Integer orgId, Integer unitId, Integer deptStoreMapperId,
			Character status) throws ApplicationException {
		try {
			DepartmentStoreMapper departmentStoreMapper = findById(deptStoreMapperId);
			if (departmentStoreMapper.getDeptStoreMapperId() != 0) {
				departmentStoreMapper.setStatus(status);
				DepartmentStoreMapper deptStoreMst = update(departmentStoreMapper);
				if (deptStoreMst.getStatus() == ACTIVE) {
					return new Response(SUCCESS, null, null, null, null);
				} else if (deptStoreMst.getStatus() == INACTIVE) {
					return new Response(SUCCESS, null, null, null, null);
				} else {
					if (status == ACTIVE) {
						return new Response(SUCCESS, null, null, null, null);
					} else {
						return new Response(SUCCESS, null, null, null, null);
					}
				}
			} else {
				return new Response(SUCCESS, null, null, null, null);
			}

		} catch (Exception e) {
			logger.error("Exection", e);
			return new Response(ERROR, ERR_500, null, null, null);
		}
	}

	@Override
	public Response getDeptStoreMapperById(Integer orgId, Integer unitId, Integer deptStoreMapperId)
			throws ApplicationException {
		try {
			DepartmentStoreMapperDto deptList = (DepartmentStoreMapperDto) sessionFactory.getCurrentSession()
					.createSQLQuery(
							"SELECT dsm.dept_store_mapper_id as \"deptStoreMapperId\", dsm.store_id as \"id\",s.store_name as \"storeName\", dsm.dept_id as \"deptId\",sm.speciality_name as \"deptName\", dsm.status as \"status\"   "
							+ " ,s.store_id as \"storeId\" " //edited for simplify store_id as storeId (not id) 
									+ " FROM pharmacy.t_dept_store_mapper dsm "
									+ " join public.m_store s on  dsm.store_id =s.store_id "
									+ " join doctor.m_speciality_master sm on sm.speciality_id=dsm.dept_id"
									+ " where dsm.org_id=:orgId " + " and dsm.unit_id=:unitId "
									+ " and dsm.dept_store_mapper_id=:deptStoreMapperId ")
					.setParameter("orgId", orgId).setParameter("unitId", unitId)
					.setParameter("deptStoreMapperId", deptStoreMapperId)
					.setResultTransformer(Transformers.aliasToBean(DepartmentStoreMapperDto.class)).uniqueResult();

			return new Response(SUCCESS, null, null, null, deptList);
		} catch (Exception e) {
			logger.error("Exection", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}

	@Override
	public Response updateDeptStoremapper(DepartmentStoreMapperDto departmentStoreMapperDto)
			throws ApplicationException {
		try {

			DepartmentStoreMapper departmentStoreMapper = new DepartmentStoreMapper();
			Integer deptId = (Integer) sessionFactory.getCurrentSession().createSQLQuery(
					" select dept_id from pharmacy.t_dept_store_mapper where status='A' and unit_id=:unitId and org_id=:orgId and dept_id=:dept_id and store_id=:storeId")
					.setParameter("orgId", departmentStoreMapperDto.getOrgId())
					.setParameter("unitId", departmentStoreMapperDto.getUnitId())
					.setParameter("dept_id", departmentStoreMapperDto.getDeptId())
					.setParameter("storeId", departmentStoreMapperDto.getId()).uniqueResult();
			if (deptId == null) {
				departmentStoreMapper.setDeptStoreMapperId(departmentStoreMapperDto.getDeptStoreMapperId());
				departmentStoreMapper.setDeptId(departmentStoreMapperDto.getDeptId());
				departmentStoreMapper.setStoreId(departmentStoreMapperDto.getId());
				departmentStoreMapper.setUnitId(departmentStoreMapperDto.getUnitId());
				departmentStoreMapper.setOrgId(departmentStoreMapperDto.getOrgId());
				departmentStoreMapper.setStatus(departmentStoreMapperDto.getStatus());
				departmentStoreMapper.setCreatedBy(departmentStoreMapperDto.getCreatedBy());
				departmentStoreMapper.setCreatedDate(TimeZoneComponent.getDateByzone(departmentStoreMapperDto.getUnitId()));
				departmentStoreMapper.setUpdatedBy(departmentStoreMapperDto.getUpdatedBy());
				departmentStoreMapper.setUpdatedDate(TimeZoneComponent.getDateByzone(departmentStoreMapperDto.getUnitId()));
				sessionFactory.getCurrentSession().update(departmentStoreMapper);
				return new Response<>(SUCCESS, null, null, null, null);
			}
			return new Response<>(ERROR, ERR_500, "Department Store Already Mapped", null, null);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public Response listDeptStoreMapper(Integer orgId, Integer unitId, Integer offset, Integer recordPerPage)
			throws ApplicationException {

		try {
			List<DepartmentStoreMapperDto> deptstoreMappedList = sessionFactory.getCurrentSession().createSQLQuery(
					"SELECT dsm.dept_store_mapper_id as \"deptStoreMapperId\", dsm.store_id as \"storeId\",s.store_name as \"storeName\", dsm.dept_id as \"deptId\",sm.speciality_name as \"deptName\", dsm.status as \"status\"   "
							+ " FROM pharmacy.t_dept_store_mapper dsm "
							+ " join public.m_store s on  dsm.store_id =s.store_id "
							+ " join doctor.m_speciality_master sm on sm.speciality_id=dsm.dept_id"
							+ " where dsm.org_id=:orgId " + " and dsm.unit_id=:unitId "
							+ " and dsm.status='A' ORDER BY dsm.dept_store_mapper_id DESC ")
					.setParameter("orgId", orgId).setParameter("unitId", unitId).setFirstResult(offset != null ? offset : 0)
					.setMaxResults(recordPerPage != null ? recordPerPage : 10)
					.setResultTransformer(Transformers.aliasToBean(DepartmentStoreMapperDto.class)).list();

			if (deptstoreMappedList.isEmpty()) {
				return new Response(ERROR, ERR_500, null, null, null);
			} else {
				return new Response(SUCCESS, null, null, deptstoreMappedList, null);
			}

		} catch (Exception e) {
			logger.error("Exection", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}

	@Override
	public Response getTotalDeptStoreMapperRecord(Integer orgId, Integer unitId) throws ApplicationException {
		try {
			BigInteger DeptStoreMapperTotalRecordCount = (BigInteger) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_TOTAL_DEPTSTORE_RECORD").setParameter("unitId", unitId).setParameter("orgId", orgId)
					.uniqueResult();

			if (DeptStoreMapperTotalRecordCount.compareTo(BigInteger.ZERO) == 1) {
				return new Response(SUCCESS, null, null, null, DeptStoreMapperTotalRecordCount);
			} else {
				return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, BigInteger.ZERO);
			}

		} catch (Exception e) {
			logger.error("Exection", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}

	@Override
	public Response saveWardStoremapper(WardStoreMapperDto wardStoreMapperDto) {

		try {

			WardStoreMapper wardStoreMapper = new WardStoreMapper();
			Integer wardId = (Integer) sessionFactory.getCurrentSession().createSQLQuery(
					" select ward_store_mapper_id FROM pharmacy.t_ward_store_mapper dsm  join public.m_store s on  dsm.store_id =s.store_id  join adt.m_ward_master sm on sm.ward_id=dsm.ward_id where dsm.org_id=:orgId  and dsm.unit_id=:unitId  and dsm.status='A' and dsm.store_id=:stoId  and dsm.ward_store_mapper_id =:ward_id")
					.setParameter("orgId", wardStoreMapperDto.getOrgId())
					.setParameter("unitId", wardStoreMapperDto.getUnitId())
					.setParameter("ward_id", wardStoreMapperDto.getwardId())
					.setParameter("stoId", wardStoreMapperDto.getId()).uniqueResult();
			//System.out.println(wardId);
			if (wardId == null) {
				wardStoreMapper.setwardId(wardStoreMapperDto.getwardId());
				wardStoreMapper.setStoreId(wardStoreMapperDto.getId());
				wardStoreMapper.setUnitId(wardStoreMapperDto.getUnitId());
				wardStoreMapper.setOrgId(wardStoreMapperDto.getOrgId());
				wardStoreMapper.setStatus(wardStoreMapperDto.getStatus());
				wardStoreMapper.setCreatedBy(wardStoreMapperDto.getCreatedBy());
				wardStoreMapper.setCreatedDate(TimeZoneComponent.getDateByzone(wardStoreMapperDto.getUnitId()));
				wardStoreMapper.setUpdatedBy(wardStoreMapperDto.getUpdatedBy());
				wardStoreMapper.setUpdatedDate(TimeZoneComponent.getDateByzone(wardStoreMapperDto.getUnitId()));
				sessionFactory.getCurrentSession().save(wardStoreMapper);
				return new Response<>(SUCCESS, null, null, null, null);
			}
			return new Response<>(ERROR, ERR_500, "Ward Store Already Mapped", null, null);
		} catch (Exception e) {
			throw e;
		}

	}

	@Override
	public Response getWardStoremappedList(WardStoreMapperDto wardStoreMapperDto) {

		try {
			List<WardStoreMapperDto> wardList = sessionFactory.getCurrentSession().createSQLQuery(
					"SELECT dsm.ward_store_mapper_id as \"wardStoreMapperId\", dsm.store_id as \"storeId\",s.store_name as \"storeName\", dsm.ward_id as \"wardId\",sm.ward_name as \"wardName\", dsm.status as \"status\"   "
							+ " FROM pharmacy.t_ward_store_mapper dsm "
							+ " join public.m_store s on  dsm.store_id =s.store_id "
							+ " join adt.m_ward_master sm on sm.ward_id=dsm.ward_id"
							+ " where dsm.org_id=:orgId " + " and dsm.unit_id=:unitId ")
					.setParameter("orgId", wardStoreMapperDto.getOrgId())
					.setParameter("unitId", wardStoreMapperDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(WardStoreMapperDto.class)).list();

			return new Response(SUCCESS, null, null, wardList, null);
		} catch (Exception e) {
			logger.error("Exection", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}

	}

	@Override
	public List<WardStoreMapperDto> getWardStoremappedListForStoreUpdate(WardStoreMapperDto wardStoreMapperDto) {
			List<WardStoreMapperDto> wardList = sessionFactory.getCurrentSession().createSQLQuery(
					"SELECT dsm.ward_store_mapper_id as \"wardStoreMapperId\", dsm.store_id as \"storeId\",s.store_name as \"storeName\", dsm.ward_id as \"wardId\",sm.ward_name as \"wardName\", dsm.status as \"status\"   "
							+ " FROM pharmacy.t_ward_store_mapper dsm "
							+ " join public.m_store s on  dsm.store_id =s.store_id "
							+ " join adt.m_ward_master sm on sm.ward_id=dsm.ward_id"
							+ " where dsm.org_id=:orgId " + " and dsm.unit_id=:unitId " + " and dsm.status='A' and s.store_id = :storeId")
					.setParameter("orgId", wardStoreMapperDto.getOrgId())
					.setParameter("unitId", wardStoreMapperDto.getUnitId())
					.setParameter("storeId",wardStoreMapperDto.getStoreId())
					.setResultTransformer(Transformers.aliasToBean(WardStoreMapperDto.class)).list();

			return wardList;
	}

	@Override
	public Response getWardStoreMapperById(Integer orgId, Integer unitId, Integer wardStoreMapperId) {

		try {
			WardStoreMapperDto wardList = (WardStoreMapperDto) sessionFactory.getCurrentSession().createSQLQuery(
					"SELECT dsm.ward_store_mapper_id as \"wardStoreMapperId\", dsm.store_id as \"id\",s.store_name as \"storeName\", dsm.ward_id as \"wardId\",sm.ward_name as \"wardName\", dsm.status as \"status\"   "
							+ " FROM pharmacy.t_ward_store_mapper dsm "
							+ " join public.m_store s on  dsm.store_id =s.store_id "
							+ " join adt.m_ward_master sm on sm.ward_id=dsm.ward_id"
							+ " where dsm.org_id=:orgId " + " and dsm.unit_id=:unitId "
							+ " and dsm.ward_store_mapper_id=:wardStoreMapperId ")
					.setParameter("orgId", orgId).setParameter("unitId", unitId)
					.setParameter("wardStoreMapperId", wardStoreMapperId)
					.setResultTransformer(Transformers.aliasToBean(WardStoreMapperDto.class)).uniqueResult();

			return new Response(SUCCESS, null, null, null, wardList);
		} catch (Exception e) {
			logger.error("Exection", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}

	}

	@Override
	public Response updateWardStoremapper(WardStoreMapperDto wardStoreMapperDto) {

		try {

			WardStoreMapper wardStoreMapper = new WardStoreMapper();
			Integer wardId = (Integer) sessionFactory.getCurrentSession().createSQLQuery(
					" select dept_id from pharmacy.t_dept_store_mapper where status='A' and unit_id=:unitId and org_id=:orgId and dept_id=:dept_id and store_id=:storeId")
					.setParameter("orgId", wardStoreMapperDto.getOrgId())
					.setParameter("unitId", wardStoreMapperDto.getUnitId())
					.setParameter("dept_id", wardStoreMapperDto.getwardId())
					.setParameter("storeId", wardStoreMapperDto.getId()).uniqueResult();
			if (wardId == null) {
				wardStoreMapper.setwardStoreMapperId(wardStoreMapperDto.getwardStoreMapperId());
				wardStoreMapper.setwardId(wardStoreMapperDto.getwardId());
				wardStoreMapper.setStoreId(wardStoreMapperDto.getId());
				wardStoreMapper.setUnitId(wardStoreMapperDto.getUnitId());
				wardStoreMapper.setOrgId(wardStoreMapperDto.getOrgId());
				wardStoreMapper.setStatus(wardStoreMapperDto.getStatus());
				wardStoreMapper.setCreatedBy(wardStoreMapperDto.getCreatedBy());
				wardStoreMapper.setCreatedDate(TimeZoneComponent.getDateByzone(wardStoreMapperDto.getUnitId()));
				wardStoreMapper.setUpdatedBy(wardStoreMapperDto.getUpdatedBy());
				wardStoreMapper.setUpdatedDate(TimeZoneComponent.getDateByzone(wardStoreMapperDto.getUnitId()));
				sessionFactory.getCurrentSession().update(wardStoreMapper);
				return new Response<>(SUCCESS, null, null, null, null);
			}
			return new Response<>(ERROR, ERR_500, "Ward Store Already Mapped", null, null);
		} catch (Exception e) {
			throw e;
		}

	}

	@Override
	public Response activeInactiveWardStoreMapper(Integer orgId, Integer unitId, Integer wardStoreMapperId,
			Character status) {

		try {
			WardStoreMapper wardStoreMapper = (WardStoreMapper) sessionFactory.getCurrentSession().byId(WardStoreMapper.class).load(wardStoreMapperId);
			if (wardStoreMapper.getwardStoreMapperId()!= 0) {
				wardStoreMapper.setStatus(status);
			//	WardStoreMapper wardStoreMst = sessionFactory.getCurrentSession().update(wardStoreMapper);
				 sessionFactory.getCurrentSession().update(wardStoreMapper);
				if (wardStoreMapper.getStatus() == ACTIVE) {
					return new Response(SUCCESS, null, null, null, null);
				} else if (wardStoreMapper.getStatus() == INACTIVE) {
					return new Response(SUCCESS, null, null, null, null);
				} else {
					if (status == ACTIVE) {
						return new Response(SUCCESS, null, null, null, null);
					} else {
						return new Response(SUCCESS, null, null, null, null);
					}
				}
			} else {
				return new Response(SUCCESS, null, null, null, null);
			}

		} catch (Exception e) {
			logger.error("Exection", e);
			return new Response(ERROR, ERR_500, null, null, null);
		}
	
	}
	
	
	
	/* start code for resolved HIS-3198 Department Store mapper - User should be able to assign the one store to multiple department and vice versa */
	@Override
	public Response getDeptStoremappedListNew(DepartmentStoreMapperDto departmentStoreMapperDto)
			throws ApplicationException {
		String storeCondation = departmentStoreMapperDto.getStoreIds().isEmpty() 
								? " " : " and dsm.store_id in (:storeIds) ";
		String deptCondation = departmentStoreMapperDto.getDeptIds().isEmpty() 
							   ? " " : " and dsm.dept_id in (:deptIds) ";
				
		try {
			Query query = sessionFactory.getCurrentSession().createSQLQuery(
					"SELECT dsm.dept_store_mapper_id as \"deptStoreMapperId\", dsm.store_id as \"storeId\",s.store_name as \"storeName\", dsm.dept_id as \"deptId\",sm.speciality_name as \"deptName\", dsm.status as \"status\"   "
							+ " FROM pharmacy.t_dept_store_mapper dsm "
							+ " join public.m_store s on  dsm.store_id =s.store_id "
							+ " join doctor.m_speciality_master sm on sm.speciality_id=dsm.dept_id"
							+ " where dsm.org_id=:orgId " + " and dsm.unit_id=:unitId " + " and dsm.status='A' "
							+ storeCondation + deptCondation);
					query.setParameter("orgId", departmentStoreMapperDto.getOrgId());
					query.setParameter("unitId", departmentStoreMapperDto.getUnitId());
					
					if(!departmentStoreMapperDto.getStoreIds().isEmpty()) {
						query.setParameterList("storeIds", departmentStoreMapperDto.getStoreIds());
						}
					if(!departmentStoreMapperDto.getDeptIds().isEmpty()) {
						query.setParameterList("deptIds", departmentStoreMapperDto.getDeptIds());
						}
					query.setResultTransformer(Transformers.aliasToBean(DepartmentStoreMapperDto.class));
					List<DepartmentStoreMapperDto> deptList=query.list();
			return new Response(SUCCESS, null, null, deptList, null);
		} catch (Exception e) {
			logger.error("Exection", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}

	}
	/* ends code for resolved HIS-3198 Department Store mapper - User should be able to assign the one store to multiple department and vice versa */

	@Override
	public Response getWardStoremappedListPagination(WardStoreMapperDto wardStoreMapperDto)
			throws ApplicationException {
		try {
			List<WardStoreMapperDto> wardList = sessionFactory.getCurrentSession().createSQLQuery(
					"SELECT dsm.ward_store_mapper_id as \"wardStoreMapperId\", dsm.store_id as \"storeId\",s.store_name as \"storeName\", dsm.ward_id as \"wardId\",sm.ward_name as \"wardName\", dsm.status as \"status\"   "
							+ " FROM pharmacy.t_ward_store_mapper dsm "
							+ " join public.m_store s on  dsm.store_id =s.store_id "
							+ " join adt.m_ward_master sm on sm.ward_id=dsm.ward_id"
							+ " where dsm.org_id=:orgId " + " and dsm.unit_id=:unitId ")
					.setParameter("orgId", wardStoreMapperDto.getOrgId())
					.setParameter("unitId", wardStoreMapperDto.getUnitId())
					.setFirstResult(wardStoreMapperDto.getOffset() != null ? wardStoreMapperDto.getOffset()  : 0)
					.setMaxResults(wardStoreMapperDto.getNoOfRecordsPerPage() != null ? wardStoreMapperDto.getNoOfRecordsPerPage()  : 10)
					.setResultTransformer(Transformers.aliasToBean(WardStoreMapperDto.class)).list();
			return new Response(SUCCESS, null, null, wardList, null);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getWardStoremappedListPaginationCount(WardStoreMapperDto wardStoreMapperDto)
			throws ApplicationException {
		try {
			
			Session session = sessionFactory.getCurrentSession();
			Criteria crit = session.createCriteria(WardStoreMapper.class);
			crit.add(Restrictions.eq("orgId", wardStoreMapperDto.getOrgId()));
			crit.add(Restrictions.eq("unitId", wardStoreMapperDto.getUnitId()));
			crit.setProjection(Projections.rowCount());
			Long count = (Long) crit.uniqueResult();
			
			return new Response(SUCCESS, SUCCESS_CODE, null, null, count);

		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

}
