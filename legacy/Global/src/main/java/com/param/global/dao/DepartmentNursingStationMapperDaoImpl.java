package com.param.global.dao;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.adt.master.unit.model.DepartmentNursingStationMapper;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.DepartmentNursingStationMapperDto;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings({"rawtypes", "unchecked"})
@Repository
public class DepartmentNursingStationMapperDaoImpl extends GenericDao<DepartmentNursingStationMapper, Integer> implements IDepartmentNursingStationMapperDao, ICommonConstants{
	final static Logger logger = Logger.getLogger(DepartmentNursingStationMapperDaoImpl.class);


	public DepartmentNursingStationMapperDaoImpl() {
		super(DepartmentNursingStationMapper.class);
	}
	
	@Autowired
	SessionFactory sessionFactory;
		
	@Override
	public Response saveDepartmentNursingStationMapper(DepartmentNursingStationMapperDto departmentNursingStationMapperDto) throws ApplicationException {
		try{
			DepartmentNursingStationMapper departmentNursingStationMapper=new DepartmentNursingStationMapper();
			departmentNursingStationMapper.setSpecialityId(departmentNursingStationMapperDto.getSpecialityId());
			departmentNursingStationMapper.setNursingStationId(departmentNursingStationMapperDto.getNursingStationId());
			departmentNursingStationMapper.setOrganisationId(departmentNursingStationMapperDto.getOrganisationId());
			departmentNursingStationMapper.setUnitId(departmentNursingStationMapperDto.getUnitId());
			departmentNursingStationMapper.setCreatedBy(departmentNursingStationMapperDto.getCreatedBy());
			departmentNursingStationMapper.setStatus(departmentNursingStationMapperDto.getStatus());
			departmentNursingStationMapper.setIsDeleted('N');
			departmentNursingStationMapper.setCreatedDate(GlobalCommonDateUtils.getDate(departmentNursingStationMapperDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			departmentNursingStationMapper.setUpdatedBy(departmentNursingStationMapperDto.getUpdatedBy());
			departmentNursingStationMapper.setUpdatedDate(GlobalCommonDateUtils.getDate(departmentNursingStationMapperDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			
			save(departmentNursingStationMapper);
			
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (HibernateException he) {
		logger.error("saveDepartmentNursingStationMapper failed", he);
		} catch (Exception e) {
		logger.error("saveDepartmentNursingStationMapper failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response updateDepartmentNursingStationMapper(List<DepartmentNursingStationMapperDto> listDepartmentNursingStationMapperDto) throws ApplicationException {
		try{
			
			
			Integer result = sessionFactory.getCurrentSession()
					.getNamedQuery("UPDATE_DEPARTMENT_NURSING_STATION_MAPPER")
					.setParameter("isDeleted", 'Y')
					.setParameter("organisationId", listDepartmentNursingStationMapperDto.get(0).getOrganisationId())
					.setParameter("unitId", listDepartmentNursingStationMapperDto.get(0).getUnitId())
					.setParameter("specialityId", listDepartmentNursingStationMapperDto.get(0).getSpecialityId())
					.executeUpdate();
			if (result > 0) {
				for (Iterator iterator = listDepartmentNursingStationMapperDto.iterator(); iterator.hasNext();) {
					DepartmentNursingStationMapperDto listDepartmentNursingStationMapper = (DepartmentNursingStationMapperDto) iterator
							.next();
					DepartmentNursingStationMapper departmentNursingStationMapper=new DepartmentNursingStationMapper();
					departmentNursingStationMapper.setNursingStationId(listDepartmentNursingStationMapper.getNursingStationId());
					departmentNursingStationMapper.setSpecialityId(listDepartmentNursingStationMapper.getSpecialityId());
					departmentNursingStationMapper.setOrganisationId(listDepartmentNursingStationMapper.getOrganisationId());
					departmentNursingStationMapper.setUnitId(listDepartmentNursingStationMapper.getUnitId());
					departmentNursingStationMapper.setIsDeleted('N');
					departmentNursingStationMapper.setCreatedBy(listDepartmentNursingStationMapper.getCreatedBy());
					departmentNursingStationMapper.setStatus(listDepartmentNursingStationMapper.getStatus());
					departmentNursingStationMapper.setUpdatedBy(listDepartmentNursingStationMapper.getUpdatedBy());
					departmentNursingStationMapper.setCreatedDate(GlobalCommonDateUtils.getDate(listDepartmentNursingStationMapper.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
					departmentNursingStationMapper.setUpdatedDate(GlobalCommonDateUtils.getDate(listDepartmentNursingStationMapper.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));

					
					save(departmentNursingStationMapper);
				}
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
				
			} 
		} catch (HibernateException he) {
		logger.error("updateDepartmentNursingStationMapper failed", he);
		} catch (Exception e) {
		logger.error("updateDepartmentNursingStationMapper failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateDepartmentNursingStationMapperStatus(Integer specialityId, Integer organisationId, Integer unitId,
			Character status) throws ApplicationException {
		try{
			if (specialityId != 0) {

				Integer result = sessionFactory.getCurrentSession()
						.getNamedQuery("UPDATE_DEPARTMENT_NURSING_STATION_MAPPER_STATUS")
						.setParameter("organisationId", organisationId)
						.setParameter("unitId", unitId)
						.setParameter("status", status)
						.setParameter("specialityId", specialityId)
						.executeUpdate();
				if (result > 0) {
					if (status.equals(ACTIVE)) {
						return new Response(SUCCESS,SUCCESS_CODE, COMMON_UPDATE, null, null);
					} else if (status.equals(INACTIVE)) {
						return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
					}
				} else {
					return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
				}

			} else {
				return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}
			
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (HibernateException he) {
		logger.error("updateDepartmentNursingStationMapperStatus failed", he);
		} catch (Exception e) {
		logger.error("updateDepartmentNursingStationMapperStatus failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getDepartmentNursingStationMapperlist(DepartmentNursingStationMapperDto departmentNursingStationMapperDto) throws ApplicationException {
		try{
			List<DepartmentNursingStationMapperDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DEPARTMENT_NURSING_STATION_MAPPER_LIST")
					.setParameter("organisationId", departmentNursingStationMapperDto.getOrganisationId())
					.setParameter("unitId", departmentNursingStationMapperDto.getUnitId())
					.setFirstResult(departmentNursingStationMapperDto.getOffset() != null ? departmentNursingStationMapperDto.getOffset() : 0)
					.setMaxResults(departmentNursingStationMapperDto.getNoOfRecordsPerPage() != null ? departmentNursingStationMapperDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(DepartmentNursingStationMapperDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
		logger.error("getDepartmentNursingStationMapperlist failed", he);
		} catch (Exception e) {
		logger.error("getDepartmentNursingStationMapperlist failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getDepartmentNursingStationMapperCount(Integer organisationId, Integer unitId) throws ApplicationException {
		try{
			BigInteger departmentNursingStationMapperrCount = (BigInteger) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DEPARTMENT_NURSING_STATION_MAPPER_COUNT")
					.setParameter("organisationId", organisationId)
					.setParameter("unitId", unitId)
					.uniqueResult();
				if (departmentNursingStationMapperrCount.compareTo(BigInteger.ZERO) == 1) {
					return new Response(SUCCESS, SUCCESS_CODE, null, null, departmentNursingStationMapperrCount);
				} else {
					return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, BigInteger.ZERO);
				}
		} catch (HibernateException he) {
		logger.error("getDepartmentNursingStationMapperCount failed", he);
		} catch (Exception e) {
		logger.error("getDepartmentNursingStationMapperCount failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@Override
	public Response getActiveDepartmentNursingStationMapper(Integer organisationId, Integer unitId) throws ApplicationException {
		try{
			
			List<DepartmentNursingStationMapperDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_DEPARTMENT_NURSING_STATION_MAPPER_LIST")
					.setParameter("organisationId", organisationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(DepartmentNursingStationMapperDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
		logger.error("getActiveDepartmentNursingStationMapper failed", he);
		} catch (Exception e) {
		logger.error("getActiveDepartmentNursingStationMapper failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	
	@Override
	public Response getDepartmentNursingStationMapperById(Integer specialityId, Integer organisationId, Integer unitId)
			throws ApplicationException {
		try{
			List<DepartmentNursingStationMapperDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DEPARTMENT_NURSING_STATION_MAPPER_BY_ID")
					.setParameter("specialityId", specialityId)
					.setParameter("organisationId", organisationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(DepartmentNursingStationMapperDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
		logger.error("getDepartmentNursingStationMapperById failed", he);
		} catch (Exception e) {
		logger.error("getDepartmentNursingStationMapperById failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@Override
	public Response checkDepartmentNursingStationMapperAvailable(DepartmentNursingStationMapperDto departmentNursingStationMapperDto)
			throws ApplicationException {
		try{
			List<DepartmentNursingStationMapperDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("CHECK_DEPARTMENT_NURSING_STATION_MAPPER_AVAILABLE")
					.setParameter("specialityId", departmentNursingStationMapperDto.getSpecialityId())
					.setParameter("organisationId", departmentNursingStationMapperDto.getOrganisationId())
					.setParameter("unitId", departmentNursingStationMapperDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(DepartmentNursingStationMapperDto.class)).list();
			
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
		logger.error("checkDepartmentNursingStationMapperAvailable failed", he);
		} catch (Exception e) {
		logger.error("checkDepartmentNursingStationMapperAvailable failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getDetailedListNursingStation(Integer specialityId, Integer organisationId, Integer unitId)
			throws ApplicationException {
		try{
			List<DepartmentNursingStationMapperDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DETAILED_LIST_NURSING_STATION")
					.setParameter("specialityId", specialityId)
					.setParameter("organisationId", organisationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(DepartmentNursingStationMapperDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
		logger.error("getDetailedListNursingStation failed", he);
		} catch (Exception e) {
		logger.error("getDetailedListNursingStation failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	
	@Override
	public Response getDeptListByNursingStationId(Integer nursingStationId, Integer organisationId, Integer unitId) throws ApplicationException {
		try{
			List<DepartmentNursingStationMapperDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DEPT_LIST_BY_NURSING_STATION_ID")
					.setParameter("orgId",organisationId)
					.setParameter("unitId", unitId)
					.setParameter("nursingStationId", nursingStationId)
					.setResultTransformer(Transformers.aliasToBean(DepartmentNursingStationMapperDto.class)).list();
			
			List<DepartmentNursingStationMapperDto> wardList=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_WARD_LIST_BY_NURSING_STATION")
					.setParameter("nursingStationId", nursingStationId)
					.setResultTransformer(Transformers.aliasToBean(DepartmentNursingStationMapperDto.class)).list();
			
			
			
			List<Integer> deptList = new LinkedList<>();
			List<Integer> wardListIds = new LinkedList<>();
			
			ListIterator<DepartmentNursingStationMapperDto>  iterator = list.listIterator();
			while(iterator.hasNext())
			{
				DepartmentNursingStationMapperDto dto = iterator.next();
				deptList.add(dto.getDepartmentId());
			}
			
			ListIterator<DepartmentNursingStationMapperDto>  wardListIdsIterator = wardList.listIterator();
			while(wardListIdsIterator.hasNext())
			{
				DepartmentNursingStationMapperDto dto = wardListIdsIterator.next();
				wardListIds.add(dto.getId());
			}
			
			return new Response(SUCCESS, SUCCESS_CODE, null, deptList, wardListIds);
		} catch (HibernateException he) {
		logger.error("getDeptListByNursingStationId failed", he);
		} catch (Exception e) {
		logger.error("getDeptListByNursingStationId failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getWardListForER(Integer organisationId, Integer unitId) throws ApplicationException {
		try{
			List<DepartmentNursingStationMapperDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_WARD_LIST_FOR_ER")
					.setParameter("orgId",organisationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(DepartmentNursingStationMapperDto.class)).list();
				List<Integer> wardList = new LinkedList<>();
				ListIterator<DepartmentNursingStationMapperDto>  iterator = list.listIterator();
				while(iterator.hasNext())
				{
					DepartmentNursingStationMapperDto dto = iterator.next();
					wardList.add(dto.getWardId());
				}
			
			return new Response(SUCCESS, SUCCESS_CODE, null, wardList, null);
		} catch (HibernateException he) {
		logger.error("getWardListForER failed", he);
		} catch (Exception e) {
		logger.error("getWardListForER failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


}

