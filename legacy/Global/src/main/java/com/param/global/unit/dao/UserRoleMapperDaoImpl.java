package com.param.global.unit.dao;

import java.util.Arrays;
import java.util.List;

import com.param.adt.discharge.model.DischargeFieldUnitWiseDTO;
import org.hibernate.*;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.UserUnitMapperDto;
import com.param.global.model.UserRoleMapper;
import com.param.global.model.UserUnitMapper;
import com.param.global.org.dto.UnitMasterDto;
import com.param.global.procedure.VO.RoleMapperVO;
import com.param.global.procedure.VO.StaffMapperVO;
import com.param.global.unit.dto.UserRoleMapperDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
@Repository
@SuppressWarnings({"rawtypes", "unchecked"})
public class UserRoleMapperDaoImpl extends GenericDao<UserRoleMapper, Integer> implements IUserRoleMapperDao, ICommonConstants{

	final static Logger logger = Logger.getLogger(UserRoleMapperDaoImpl.class);

	public UserRoleMapperDaoImpl() {
		super(UserRoleMapper.class);

	}
	@Autowired
	SessionFactory SessionFactory;
	
	
	 
	@Override
	public Response saveUserRoleMapper(UserRoleMapperDto userRoleMapperDto) throws ApplicationException {
		try {
									
				UserRoleMapper userRoleMapper=new UserRoleMapper();
				userRoleMapper.setOrganisationId(userRoleMapperDto.getOrganisationId());
				userRoleMapper.setRoleId(userRoleMapperDto.getRoleId());
				userRoleMapper.setUserId(userRoleMapperDto.getUserId());
				userRoleMapper.setDashboardId(userRoleMapperDto.getDashboardId());
				userRoleMapper.setUnitId(userRoleMapperDto.getUnitId());	
				userRoleMapper.setUpdatedBy(userRoleMapperDto.getUpdatedBy());
				userRoleMapper.setUpdatedDate(TimeZoneComponent.getDateByzone(userRoleMapperDto.getUnitId()));
				userRoleMapper.setStatus(userRoleMapperDto.getStatus());
				userRoleMapper.setIsRecoll(userRoleMapperDto.getIsRecoll());
				userRoleMapper.setIsRisRecall(userRoleMapperDto.getIsRisRecall()!=null ? userRoleMapperDto.getIsRisRecall():false);
				userRoleMapper.setManualMpesa(userRoleMapperDto.getManualMpesa() != null ? userRoleMapperDto.getManualMpesa() : false);
				userRoleMapper.setEnableBlockPrint(userRoleMapperDto.getEnableBlockPrint()!= null ? userRoleMapperDto.getEnableBlockPrint() : false);

				if(userRoleMapperDto.getUpdateStatus()==null){
					userRoleMapper.setCreatedBy(userRoleMapperDto.getCreatedBy());
					userRoleMapper.setCreatedDate(TimeZoneComponent.getDateByzone(userRoleMapperDto.getUnitId()));
				}
				save(userRoleMapper);
					//sessionFactory.getCurrentSession().saveOrUpdate(userRoleMapper);	
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (HibernateException he) {
			logger.error("saveUserRoleMapper failed", he);
		} catch (Exception e) {
			logger.error("saveUserRoleMapper failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response updateUserRoleMapper(UserRoleMapperDto userRoleMapperDto) throws ApplicationException {
		try {
			UserRoleMapper userRoleMapper=findById(userRoleMapperDto.getUserRoleMpprId());
			
			userRoleMapper.setOrganisationId(userRoleMapperDto.getOrganisationId());
			userRoleMapper.setUnitId(userRoleMapperDto.getUnitId());
			userRoleMapper.setRoleId(userRoleMapperDto.getRoleId());
			userRoleMapper.setUpdatedBy(userRoleMapperDto.getUpdatedBy());
			userRoleMapper.setUpdatedDate(GlobalCommonDateUtils.getDate(userRoleMapperDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			update(userRoleMapper);
			
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (HibernateException he) {
			logger.error("updateUserRoleMapper failed", he);
		} catch (Exception e) {
			logger.error("updateUserRoleMapper failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateUserRoleMapperStatus(UserRoleMapperDto userRoleMapperDto) throws ApplicationException {
		try {
			UserRoleMapper userRoleMapper=findById(userRoleMapperDto.getUserRoleMpprId());
			userRoleMapper.setUpdatedBy(userRoleMapperDto.getUpdatedBy());
			userRoleMapper.setUpdatedDate(GlobalCommonDateUtils.getDate(userRoleMapperDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			userRoleMapper.setStatus(userRoleMapperDto.getStatus());
			update(userRoleMapper);
			
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (HibernateException he) {
			logger.error("updateUserRoleMapperStatus failed", he);
		} catch (Exception e) {
			logger.error("updateUserRoleMapperStatus failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getUserRoleMapperList(UserRoleMapperDto userRoleMapperDto) throws ApplicationException {
		try {

			Session session = sessionFactory.getCurrentSession();
			String baseQuery = session.getNamedQuery("GET_USER_ROLE_MAPPER_LIST").getQueryString();
			String[] queryParts = baseQuery.split("union");
			StringBuilder array1 = new StringBuilder(queryParts[0]);
			StringBuilder array2 = new StringBuilder(queryParts[1]);
			if (userRoleMapperDto.getSearchKeyword() != null && !userRoleMapperDto.getSearchKeyword().trim().isEmpty()) {
				array1.append(" AND (")
						.append(" lower(d.doc_code) LIKE :searchKeyword ")
						.append(" OR lower(concat(d.first_name, ' ', d.middle_name, ' ', d.last_name)) LIKE :searchKeyword ")
						.append(" OR lower(d.mobile_number) LIKE :searchKeyword ")
						.append(")");

				array2.append(" AND (")
						.append(" lower(b.emp_code) LIKE :searchKeyword ")
						.append(" OR lower(concat(b.first_name, ' ', b.middle_name, ' ', b.last_name)) LIKE :searchKeyword ")
						.append(" OR lower(b.mobile_number) LIKE :searchKeyword ")
						.append(")");
			}

			String finalQueryString = "select * from ( "+ array1.toString() + " UNION " + array2.toString()+" )   as combined_query ORDER BY \"empName\" " ;
			Query query = session.createSQLQuery(finalQueryString)
					.setParameter("unitId", userRoleMapperDto.getUnitId())
					.setParameter("organisationId", userRoleMapperDto.getOrganisationId());
			if (userRoleMapperDto.getSearchKeyword() != null && !userRoleMapperDto.getSearchKeyword().trim().isEmpty()) {
				query.setParameter("searchKeyword", "%" + userRoleMapperDto.getSearchKeyword().toLowerCase() + "%");
			}

			query.setFirstResult(userRoleMapperDto.getOffset() != null ? userRoleMapperDto.getOffset() : 0)
					.setMaxResults(userRoleMapperDto.getNoOfRecordsPerPage() != null ? userRoleMapperDto.getNoOfRecordsPerPage() : 10);
			List<UserRoleMapperDto> list = query
					.setResultTransformer(Transformers.aliasToBean(UserRoleMapperDto.class))
					.list();
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			throw e;
		}
	}


	@Override
	public Response getUserRoleMapperById(Integer userRoleMpprId, Integer organisationId, Integer unitId)
			throws ApplicationException {
		try {
			List<UserRoleMapperDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_USER_ROLE_MAPPER_BY_ID")
					.setParameter("userRoleMpprId", userRoleMpprId)
					.setParameter("organisationId", organisationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(UserRoleMapperDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
			logger.error("getUserRoleMapperById failed", he);
		} catch (Exception e) {
			logger.error("getUserRoleMapperById failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getActiveUserRoleMapperList(UserRoleMapperDto userRoleMapperDto) throws ApplicationException {
		try {
			List<UserRoleMapperDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_USER_ROLE_MAPPER_LIST")
					.setParameter("unitId", userRoleMapperDto.getUnitId())
					.setParameter("organisationId", userRoleMapperDto.getOrganisationId())
					.setResultTransformer(Transformers.aliasToBean(UserRoleMapperDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
			logger.error("getActiveUserRoleMapperList failed", he);
		} catch (Exception e) {
			logger.error("getActiveUserRoleMapperList failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getUserRoleMapperCount(UserRoleMapperDto userRoleMapperDto) throws ApplicationException {
		try {
			Session session = sessionFactory.getCurrentSession();
			String baseQuery = session.getNamedQuery("GET_USER_ROLE_MAPPER_LIST").getQueryString();
			String[] queryParts = baseQuery.split("union");
			StringBuilder queryPart1 = new StringBuilder(queryParts[0]);
			StringBuilder queryPart2 = new StringBuilder(queryParts[1]);
			if (userRoleMapperDto.getSearchKeyword() != null && !userRoleMapperDto.getSearchKeyword().trim().isEmpty()) {
				queryPart1.append(" AND (")
						.append(" lower(d.doc_code) LIKE :searchKeyword ")
						.append(" OR lower(concat(d.first_name, ' ', d.middle_name, ' ', d.last_name)) LIKE :searchKeyword ")
						.append(" OR lower(d.mobile_number) LIKE :searchKeyword ")
						.append(")");
				queryPart2.append(" AND (")
						.append(" lower(b.emp_code) LIKE :searchKeyword ")
						.append(" OR lower(concat(b.first_name, ' ', b.middle_name, ' ', b.last_name)) LIKE :searchKeyword ")
						.append(" OR lower(b.mobile_number) LIKE :searchKeyword ")
						.append(")");
			}
			String finalQueryString = "select * from ( "+ queryPart1.toString() + " UNION " + queryPart2.toString()+" )   as combined_query ORDER BY \"empName\" " ;

			Query query = session.createSQLQuery(finalQueryString)
					.setParameter("unitId", userRoleMapperDto.getUnitId())
					.setParameter("organisationId", userRoleMapperDto.getOrganisationId());
			if (userRoleMapperDto.getSearchKeyword() != null && !userRoleMapperDto.getSearchKeyword().trim().isEmpty()) {
				query.setParameter("searchKeyword", "%" + userRoleMapperDto.getSearchKeyword().toLowerCase() + "%");
			}
			List<UserRoleMapperDto> list = query
					.setResultTransformer(Transformers.aliasToBean(UserRoleMapperDto.class))
					.list();


			return new Response(SUCCESS, SUCCESS_CODE, null, null, list.size());
					
			} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			throw e;
		}
	}


	@Override
	public Response getUserRoleMapperByName(UserRoleMapperDto userRoleMapperDto) throws ApplicationException {
		try {
			List<UserRoleMapperDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_USER_ROLE_MAPPER_BY_NAME")
					.setParameter("userId", userRoleMapperDto.getUserId())
					.setParameter("unitId", userRoleMapperDto.getUnitId())
					.setParameter("organisationId", userRoleMapperDto.getOrganisationId())
					.setResultTransformer(Transformers.aliasToBean(UserRoleMapperDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
			logger.error("getUserRoleMapperByName failed", he);
		} catch (Exception e) {
			logger.error("getUserRoleMapperByName failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getUserIdById(Integer userId, Integer organisationId, Integer unitId) throws ApplicationException {
		try {
			List<UserRoleMapperDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_USER_ROLE_BY_ID")
					.setParameter("userId", userId)
					.setParameter("organisationId", organisationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(UserRoleMapperDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
			logger.error("getUserIdById failed", he);
		} catch (Exception e) {
			logger.error("getUserIdById failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response truncteRecode(UserRoleMapperDto userRoleMapperDto) throws ApplicationException {
		try {
			String hql = "UPDATE UserRoleMapper "
					+ "SET status = :status "
					+ "WHERE userId = :userId "
					+ "AND unitId = :unitId";
			int updatedRows = sessionFactory.getCurrentSession().createQuery(hql)
					.setParameter("status", 'T')
					.setParameter("userId", userRoleMapperDto.getUserId())
					.setParameter("unitId", userRoleMapperDto.getUnitId())
					.executeUpdate();
			return new Response(SUCCESS, SUCCESS_CODE, null, null, null);
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			throw e;
		}
		//return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getUserAuthenticationList(UserRoleMapperDto userRoleMapperDto) throws ApplicationException {
		try {
			Session session = sessionFactory.getCurrentSession();
			String query1 = "SELECT DISTINCT ON (usrMaster.user_id) "
					+ " doctorMaster.doc_code AS \"empCode\", usrMaster.user_id AS \"userId\", "
					+ " CONCAT(doctorMaster.first_name, ' ', doctorMaster.middle_name, ' ', doctorMaster.last_name) AS \"empName\", "
					+ " doctorMaster.mobile_number AS \"contactNo\", usrRoleMppr.user_role_mppr_id AS \"userRoleMpprId\", "
					+ " roleMaster.role_id AS \"roleId\", roleMaster.role_name AS \"roleName\", usrMaster.user_name AS \"userName\", "
					+ " usrMaster.password AS \"password\", usrMaster.status AS \"status\", doctorMaster.email AS \"emailId\", "
					+ " doctorMaster.first_name AS \"firstName\" "
					+ " FROM public.t_user_role_mapper usrRoleMppr "
					+ " INNER JOIN doctor.m_doctor_master doctorMaster ON doctorMaster.user_id = usrRoleMppr.user_id "
					+ " INNER JOIN public.m_role_master roleMaster ON usrRoleMppr.role_id = roleMaster.role_id "
					+ " INNER JOIN public.m_user_master usrMaster ON usrMaster.user_id = doctorMaster.user_id "
					+ " WHERE usrRoleMppr.organisation_id = :organisationId "
					+ " AND usrRoleMppr.unit_id = :unitId "
					+ " AND (usrRoleMppr.status = 'A' OR usrRoleMppr.status = 'I')";

			String query2 = "SELECT DISTINCT ON (usrMaster.user_id) "
					+ " employeeMaster.emp_code AS \"empCode\", usrMaster.user_id AS \"userId\", "
					+ " CONCAT(employeeMaster.first_name, ' ', employeeMaster.middle_name, ' ', employeeMaster.last_name) AS \"empName\", "
					+ " employeeMaster.mobile_number AS \"contactNo\", usrRoleMppr.user_role_mppr_id AS \"userRoleMpprId\", "
					+ " roleMaster.role_id AS \"roleId\", roleMaster.role_name AS \"roleName\", usrMaster.user_name AS \"userName\", "
					+ " usrMaster.password AS \"password\", usrMaster.status AS \"status\", employeeMaster.email AS \"emailId\", "
					+ " employeeMaster.first_name AS \"firstName\" "
					+ " FROM public.t_user_role_mapper usrRoleMppr "
					+ " INNER JOIN public.m_employee_master employeeMaster ON employeeMaster.user_id = usrRoleMppr.user_id "
					+ " INNER JOIN public.m_role_master roleMaster ON usrRoleMppr.role_id = roleMaster.role_id "
					+ " INNER JOIN public.m_user_master usrMaster ON usrMaster.user_id = employeeMaster.user_id "
					+ " WHERE usrRoleMppr.organisation_id = :organisationId "
					+ " AND usrRoleMppr.unit_id = :unitId "
					+ " AND (usrRoleMppr.status = 'A' OR usrRoleMppr.status = 'I')";

			if (userRoleMapperDto.getRoleName() != null && !userRoleMapperDto.getRoleName().isEmpty()) {
				query1 += " AND roleMaster.role_name LIKE :roleName";
				query2 += " AND roleMaster.role_name LIKE :roleName";
			}

			if (userRoleMapperDto.getEmpName() != null && !userRoleMapperDto.getEmpName().isEmpty()) {
				query1 += " AND CONCAT(doctorMaster.first_name, ' ', doctorMaster.middle_name, ' ', doctorMaster.last_name) LIKE :empName";
				query2 += " AND CONCAT(employeeMaster.first_name, ' ', employeeMaster.middle_name, ' ', employeeMaster.last_name) LIKE :empName";
			}
			String finalQuery = query1 + " UNION " + query2;
			Query query = session.createSQLQuery(finalQuery)
					.setParameter("unitId", userRoleMapperDto.getUnitId())
					.setParameter("organisationId", userRoleMapperDto.getOrganisationId())
					.setResultTransformer(Transformers.aliasToBean(UserRoleMapperDto.class));

			if (userRoleMapperDto.getRoleName() != null && !userRoleMapperDto.getRoleName().isEmpty()) {
				query.setParameter("roleName", "%" + userRoleMapperDto.getRoleName() + "%");
			}

			if (userRoleMapperDto.getEmpName() != null && !userRoleMapperDto.getEmpName().isEmpty()) {
				query.setParameter("empName", "%" + userRoleMapperDto.getEmpName() + "%");
			}
			List<UserRoleMapperDto> list = query.list();
			if (list != null && !list.isEmpty()) {
				for (UserRoleMapperDto dto : list) {
					dto.setPassword(GlobalCommonDateUtils.decrypt(dto.getPassword()));
				}
			}

			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
			logger.error("getUserAuthenticationList failed", he);
		} catch (Exception e) {
			logger.error("getUserAuthenticationList failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getUserListByRoleId(UserRoleMapperDto userRoleMapperDto) throws ApplicationException {
		try {
			List<UserRoleMapperDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_USER_LIST_BY_ROLE_ID")
					.setParameter("unitId", userRoleMapperDto.getUnitId())
					.setParameter("orgId", userRoleMapperDto.getOrganisationId())
					.setParameter("roleId", userRoleMapperDto.getRoleId())
					.setResultTransformer(Transformers.aliasToBean(UserRoleMapperDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
			logger.error("getUserListByRoleId failed", he);
		} catch (Exception e) {
			logger.error("getUserListByRoleId failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getUserRoleNameByUserId(UserRoleMapperDto userRoleMapperDto) throws ApplicationException {
		try {
			List<UserRoleMapperDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_USER_ROLE_NAME_BY_USER_ID")
					.setParameter("unitId", userRoleMapperDto.getUnitId())
					.setParameter("orgId", userRoleMapperDto.getOrganisationId())
					.setParameter("userId", userRoleMapperDto.getUserId())
					.setResultTransformer(Transformers.aliasToBean(UserRoleMapperDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
			logger.error("getUserRoleNameByUserId failed", he);
		} catch (Exception e) {
			logger.error("getUserRoleNameByUserId failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getUserRoleMapperDetailsByid(StaffMapperVO staffMapperVODto) throws ApplicationException {
		try {
			StaffMapperVO  staffMapperVO = (StaffMapperVO) sessionFactory.getCurrentSession().createSQLQuery(
					"select dm.first_name as \"firstName\" ,"
					+ "dm.last_name as \"lastName\" ,"
					+ "um.user_name as \"userName\","
					+ "um.password as \"password\","
					+ "dm.email as \"emailId\","
					+ "dm.mobile_number as \"mobileNo\" ,"
					+ "urm.organisation_id as \"organizationId\","
					+ "urm.unit_id as \"unitId\" "
					+" from public.t_user_role_mapper urm "
					+" inner join  public.m_user_master um on  um.user_id = urm.user_id "
					+" inner join doctor.m_doctor_master dm on dm.user_id = urm.user_id  "
					+" WHERE urm.user_id=:userId  AND urm.organisation_id=:organizationId  and urm.unit_id=:unitId  "
					+ "union  "
					+"select em.first_name as \"firstName\" ,"
					+ "em.last_name as \"lastName\" ,"
					+ "um.user_name as \"userName\","
					+ "um.password as \"password\","
					+ "em.email as \"emailId\","
					+ "em.mobile_number as \"mobileNo\" ,"
					+ "urm.organisation_id as \"organizationId\","
					+ "urm.unit_id as \"unitId\" "
					+"from public.t_user_role_mapper urm "
					+"inner join  public.m_user_master um on  um.user_id = urm.user_id "
					+ "inner join public.m_employee_master em on em.user_id = urm.user_id "
					+" WHERE urm.user_id=:userId  AND urm.organisation_id=:organizationId and urm.unit_id=:unitId  "
					)
					.setParameter("userId",staffMapperVODto.getUserId())
					.setParameter("organizationId",staffMapperVODto.getOrganizationId())
					.setParameter("unitId",staffMapperVODto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(StaffMapperVO.class)).uniqueResult();
			

		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, staffMapperVO);
		
		} catch (HibernateException he) {
			logger.error("getUserRoleMapperDetailsByid failed", he);
		} catch (Exception e) {
			logger.error("getUserRoleMapperDetailsByid failed", e);
		}
		return new Response<>(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}
	


	@Override
	public Response getUserRoleMapperDetailsByrole(StaffMapperVO staffMapperVODto) throws ApplicationException {
		try {
			List<RoleMapperVO> RoleMapperVOlist = sessionFactory.getCurrentSession().createSQLQuery(
					"select rm.role_id as \"roleId\" , "
					+"rm.role_name as \"roleName\"  "
					+"from public.t_user_role_mapper urm "
					+"inner join public.m_role_master rm on rm.role_id = urm.role_id "
					+"WHERE urm.user_id=:userId AND urm.organisation_id=:organizationId  and urm.unit_id=:unitId "
					)
					.setParameter("userId",staffMapperVODto.getUserId())
					.setParameter("organizationId",staffMapperVODto.getOrganizationId())
					.setParameter("unitId",staffMapperVODto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(RoleMapperVO.class)).list();
			

		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, RoleMapperVOlist, null);
		
		} catch (HibernateException he) {
			logger.error("getUserRoleMapperDetailsByrole failed", he);
		} catch (Exception e) {
			logger.error("getUserRoleMapperDetailsByrole failed", e);
		}
		return new Response<>(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response saveUserUnitMapper(UserUnitMapperDto userUnitMapperDto) {
		try {
			
			UserUnitMapper userunitmpr = new UserUnitMapper();
				userunitmpr.setUserId(userUnitMapperDto.getUserId());
				userunitmpr.setStatus(userUnitMapperDto.getStatus());
				userunitmpr.setOrganisationId(userUnitMapperDto.getOrganisationId());
				userunitmpr.setUnitId(userUnitMapperDto.getUnitId());
				userunitmpr.setCreatedBy(userUnitMapperDto.getCreatedBy());
				userunitmpr.setCreatedDate(TimeZoneComponent.getDateByzone(userUnitMapperDto.getUnitId()));
				userunitmpr.setUpdatedBy(userUnitMapperDto.getUpdatedBy());
				userunitmpr.setUpdatedDate(TimeZoneComponent.getDateByzone(userUnitMapperDto.getUnitId()));
				userunitmpr.setMisReportIds(userUnitMapperDto.getMisRights());
				userunitmpr.setTabAccessIds(userUnitMapperDto.getCoversheetRights());
			sessionFactory.getCurrentSession().save(userunitmpr);
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, userunitmpr);
		} catch (HibernateException he) {
			logger.error("saveUserUnitMapper failed", he);
		} catch (Exception e) {
			logger.error("saveUserUnitMapper failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

	@Override
	public Response getActiveOTIMSRoleMasterList(UserRoleMapperDto userRoleMapperDto) throws ApplicationException {
		try {
			List<UserRoleMapperDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_OTIMS_ROLE_MASTER_LIST")
					.setParameter("unitId", userRoleMapperDto.getUnitId())
					.setParameter("organisationId", userRoleMapperDto.getOrganisationId())
					.setResultTransformer(Transformers.aliasToBean(UserRoleMapperDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
			logger.error("getActiveOTIMSRoleMasterList failed", he);
		} catch (Exception e) {
			logger.error("getActiveOTIMSRoleMasterList failed", e);
		}
		return new Response<>(ERROR, null, COMMON_ERROR_MESSAGE, null, null);

	}


	@Override
	public Response getHisRoleMapperListByUserId(UserRoleMapperDto userRoleMapperDto) throws ApplicationException {
		try {
			if(userRoleMapperDto.getUserId()!=null)
			{
				List<UserRoleMapperDto> userRoleMapperDtoList = sessionFactory.getCurrentSession()
						.createSQLQuery("select user_id as \"userId\",role_id as \"roleId\", unit_id as \"unitId\" "
								+ " from t_user_role_mapper where organisation_id=:orgId and user_id=:userId and status='A' ")
						.setParameter("orgId", userRoleMapperDto.getOrganisationId())
						.setParameter("userId", userRoleMapperDto.getUserId())
						.setResultTransformer(Transformers.aliasToBean(UserRoleMapperDto.class)).list();
				return new Response<>(SUCCESS, SUCCESS_CODE, null, userRoleMapperDtoList, null);
			}
		} catch (HibernateException he) {
			logger.error("getHisRoleMapperListByUserId failed", he);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}


	@Override
	public Response getOTIMSRoleMapperListByUserId(UserRoleMapperDto userRoleMapperDto) throws ApplicationException {
		try {
			
			if(userRoleMapperDto.getUserId()!=null)
			{
				List<UserRoleMapperDto> userRoleMapperDtoList = sessionFactory.getCurrentSession()
						.createSQLQuery("select user_id as \"userId\",role_id as \"roleId\" "
								+ " from t_otims_user_role_mapper where organisation_id=:orgId and unit_id=:unitId and user_id=:userId and status='A' ")
						.setParameter("orgId", userRoleMapperDto.getOrganisationId())
						.setParameter("unitId", userRoleMapperDto.getUnitId())
						.setParameter("userId", userRoleMapperDto.getUserId())
						.setResultTransformer(Transformers.aliasToBean(UserRoleMapperDto.class)).list();
				return new Response<>(SUCCESS, SUCCESS_CODE, null, userRoleMapperDtoList, null);
			}
		} catch (HibernateException he) {
			logger.error("getOTIMSRoleMapperListByUserId failed", he);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}


	@Override
	public Response getUserUnitListByUserId(UserUnitMapperDto userUnitMapperDto) throws ApplicationException {
		try {
			
			if(userUnitMapperDto.getUserId()!=null)
			{
				List<UserUnitMapperDto> userUnitMapperDtoList = sessionFactory.getCurrentSession()
						.createSQLQuery("select user_id as \"userId\",unit_id as \"unitId\" "
								+ " from t_user_unit_mapper where organisation_id=:orgId  and user_id=:userId and status='A' ")
						.setParameter("orgId", userUnitMapperDto.getOrganisationId())
						.setParameter("userId", userUnitMapperDto.getUserId())
						.setResultTransformer(Transformers.aliasToBean(UserUnitMapperDto.class)).list();
				return new Response<>(SUCCESS, SUCCESS_CODE, null, userUnitMapperDtoList, null);
			}
		} catch (HibernateException he) {
			logger.error("getUserUnitListByUserId failed", he);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}


	@Override
	public Response getRoleNameByRoleTypeId(Integer roleTypeId, Integer organisationId, Integer unitId)
			throws ApplicationException {
		try {
			List<UserRoleMapperDto> userRoleNameList = sessionFactory.getCurrentSession()
					.createSQLQuery("select rm.role_id as \"roleId\" , "
					+" rm.role_name as \"roleName\" , "
					+" rm.status as \"status\" ,"
					+" rm.role_type_id as \"roleTypeId\" "
					+" from public.m_role_master rm "
					+" WHERE rm.role_type_id=:roleTypeId"
					+" AND rm.organisation_id=:organisationId"
					+" AND rm.unit_id=:unitId AND rm.status='A'")
					.setParameter("roleTypeId", roleTypeId)
					.setParameter("organisationId", organisationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(UserRoleMapperDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, "Get Data user role", userRoleNameList, null);
		
		} catch (HibernateException he) {
			logger.error("getRoleNameByRoleTypeId failed", he);
		} catch (Exception e) {
			logger.error("getRoleNameByRoleTypeId failed", e);
		}
		return new Response<>(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getActiveOTRoleList(UserRoleMapperDto userRoleMapperDto) throws ApplicationException {
		try {
			List<UserRoleMapperDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_OT_ROLE_LIST")
					.setParameter("unitId", userRoleMapperDto.getUnitId())
					.setParameter("organisationId", userRoleMapperDto.getOrganisationId())
					.setResultTransformer(Transformers.aliasToBean(UserRoleMapperDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
			logger.error("getActiveOTRoleList failed", he);
		} catch (Exception e) {
			logger.error("getActiveOTRoleList failed", e);
		}
		return new Response<>(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getDialysisTechnicianList(UserRoleMapperDto userRoleMapperDto) throws ApplicationException {
		try {
			String query= " select userRoleMapper.user_role_mppr_id as \"userRoleMpprId\", CONCAT  (employeeMaster.first_name, ' ', employeeMaster.middle_name, ' ',employeeMaster.last_name) as \"empName\" ,"
					+ " userRoleMapper.user_id as \"userId\", userRoleMapper.status as \"status\" ,  userRoleMapper.role_id as \"roleId\" "
					+ " from  public.t_user_role_mapper userRoleMapper "
					+ " inner join m_role_master roleMaster on userRoleMapper.role_id=roleMaster.role_id "
					+ " inner join  public.m_employee_master employeeMaster on userRoleMapper.user_id=employeeMaster.user_id "
					+ " where userRoleMapper.organisation_id =:organisationId "
					+ " and userRoleMapper.unit_id =:unitId and roleMaster.role_type_id=13  and userRoleMapper.status='A' "
					+ " ORDER BY \"empName\" ASC " ;
			
			List<UserRoleMapperDto> list=sessionFactory.getCurrentSession()
					.createSQLQuery(query)
					.setParameter("organisationId", userRoleMapperDto.getOrganisationId())
					.setParameter("unitId", userRoleMapperDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(UserRoleMapperDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
			logger.error("getDialysisTechnicianList failed", he);
		} catch (Exception e) {
			logger.error("getDialysisTechnicianList failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getUserByRoleType(UserRoleMapperDto userRoleMapperDto) throws ApplicationException {
		try {
			String query= "SELECT DISTINCT usrMaster.user_id       AS \"userId\", "
					+ "                Concat ('Dr. ', doctorMaster.first_name, ' ', "
					+ "                doctorMaster.middle_name, ' ', "
					+ "                doctorMaster.last_name) AS \"empName\" "
					+ "FROM   PUBLIC.t_user_role_mapper usrRoleMppr "
					+ "       INNER JOIN doctor.m_doctor_master doctorMaster "
					+ "               ON doctorMaster.user_id = usrRoleMppr.user_id "
					+ "       INNER JOIN PUBLIC.m_role_master roleMaster "
					+ "               ON usrRoleMppr.role_id = roleMaster.role_id "
					+ "       INNER JOIN PUBLIC.m_user_master usrMaster "
					+ "               ON usrMaster.user_id = doctorMaster.user_id "
					+ "       INNER JOIN PUBLIC.m_prefix_master prefix "
					+ "               ON doctorMaster.prefix_id = prefix.prefix_id "
					+ "WHERE  usrRoleMppr.organisation_id = :organisationId "
					+ "       AND usrRoleMppr.unit_id = :unitId "
					+ "       AND usrRoleMppr.status = 'A' "
					+ "       AND roleMaster.role_type_id = :roleTypeId "
					+ "UNION "
					+ "SELECT DISTINCT usrMaster.user_id                                          AS "
					+ "                \"userId\", "
					+ "                Concat (prefix.prefix_desc, ' ', employeeMaster.first_name, ' ', "
					+ "                employeeMaster.middle_name, ' ', employeeMaster.last_name) AS "
					+ "                \"empName\" "
					+ "FROM   PUBLIC.t_user_role_mapper usrRoleMppr "
					+ "       INNER JOIN PUBLIC.m_employee_master employeeMaster "
					+ "               ON employeeMaster.user_id = usrRoleMppr.user_id "
					+ "       INNER JOIN PUBLIC.m_role_master roleMaster "
					+ "               ON usrRoleMppr.role_id = roleMaster.role_id "
					+ "       INNER JOIN PUBLIC.m_user_master usrMaster "
					+ "               ON usrMaster.user_id = employeeMaster.user_id "
					+ "       INNER JOIN PUBLIC.m_prefix_master prefix "
					+ "               ON employeeMaster.prefix_id = prefix.prefix_id "
					+ "WHERE  usrRoleMppr.organisation_id = :organisationId "
					+ "       AND usrRoleMppr.unit_id = :unitId "
					+ "       AND usrRoleMppr.status = 'A' "
					+ "       AND roleMaster.role_type_id = :roleTypeId "
					+ "ORDER  BY \"empName\"";
			
			List<UserRoleMapperDto> list=sessionFactory.getCurrentSession()
					.createSQLQuery(query)
					.setParameter("organisationId", userRoleMapperDto.getOrganisationId())
					.setParameter("unitId", userRoleMapperDto.getUnitId())
					.setParameter("roleTypeId", userRoleMapperDto.getRoleTypeId())
					.setResultTransformer(Transformers.aliasToBean(UserRoleMapperDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
			logger.error("getUserByRoleType failed", he);
		} catch (Exception e) {
			logger.error("getUserByRoleType failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getUserRoleType(UserRoleMapperDto dto) throws ApplicationException {
		try {
			List<UserRoleMapperDto> userRoleNameList = sessionFactory.getCurrentSession()
					.createSQLQuery(" select  rolemappr.role_id as \"roleId\" from  public.t_user_role_mapper rolemappr " + 
							"  inner join public.m_role_master rolemstr on rolemappr.role_id = rolemstr.role_id " + 
							" where  rolemstr.role_type_id in(8,9,10,11,12) and rolemappr.user_id =:userId and rolemappr.status = 'A'")
					.setParameter("userId", dto.getUserId())
					.setResultTransformer(Transformers.aliasToBean(UserRoleMapperDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, "Get Data user role", userRoleNameList, null);
		
		} catch (HibernateException he) {
			logger.error("getUserRoleType failed", he);
		} catch (Exception e) {
			logger.error("getUserRoleType failed", e);
		}
		return new Response<>(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}
	
	

		@Override
		public Response saveUserWiseMISReportsList(UserRoleMapperDto userRoleMapperDto) throws ApplicationException {
			try {
				Session session = sessionFactory.getCurrentSession();
				String sqlQuery = "UPDATE public.t_user_unit_mapper "
						+ "SET mis_report_ids = :misReportIds "
						+ "WHERE user_id = :userId "
						+ "AND unit_id = :unitId "
						+ "AND status = 'A'";
				int rowsUpdated = session.createSQLQuery(sqlQuery)
						.setParameter("misReportIds", userRoleMapperDto.getMisReportIds())
						.setParameter("userId", userRoleMapperDto.getUserId())
						.setParameter("unitId", userRoleMapperDto.getUnitId())
						.executeUpdate();
				if (rowsUpdated > 0) {
					System.out.println("Update successful: " + rowsUpdated + " rows updated.");
				} else {
					System.out.println("No rows updated.");
				}


				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		
		} catch (HibernateException he) {
			logger.error("saveUserWiseMISReportsList failed", he);
		} catch (Exception e) {
			logger.error("saveUserWiseMISReportsList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}


		@Override
		public Response updateUserUnitMapper(UserRoleMapperDto userRoleMapperDto) throws ApplicationException {
			try {
				Session session = sessionFactory.getCurrentSession();

				String sqlQuery = "UPDATE public.t_user_unit_mapper SET "
						+ "is_rendered_service_cancelled = :isRenderedServiceCancelled, "
						+ "is_update_patient_reg = :isUpdatePatientReg, "
						+ "opd_bill_cancellation = :opdBillCancellation, "
						+ "ipd_bill_cancellation = :ipdBillCancellation, "
						+ "bill_settlement = :billSettlement, "
						+ "ip_bill_settlement = :ipBillSettlement, "
						+ "service_rate_editable = :serviceRateEditable, "
						+ "is_force_update_service_package = :isForceUpdateServicePackage, "
						+ "pharmacy_bill_cancellation = :pharmacyBillCancellation, "
						+ "pharmacy_receipt_cancellation = :pharmacyReceiptCancellation, "
						+ "sample_recollection = :sampleRecollection, "
						+ "nursing_op_scan = :nursingOpScan, "
						+ "nursing_ip_scan = :nursingIpScan, "
						+ "doctor_op_scan = :doctorOpScan, "
						+ "doctor_ip_scan = :doctorIpScan, "
						+ "change_payee_rights = :changePayeeRights, "
						+ "extend_pdd = :extendPdd, "
						+ "is_update_ipd_charges_remark = :isUpdateIpdChargesRemark, "
						+ "update_discharge_summary = :updateDischargeSummary, "
						+ "ip_rendered_date_change = :ipRenderedDateChange, "
						+ "is_bill_manipulation_allowed = :isBillManipulationAllowed, "
						+ "deposit_receipt_cancellation = :depositReceiptCancellation "
						+ "WHERE unit_id = :unitId "
						+ "AND user_id = :userId "
						+ "AND organisation_id = :organisationId "
						+ "AND status = 'A'";

				int rowsUpdated = session.createSQLQuery(sqlQuery)
						.setParameter("isRenderedServiceCancelled", userRoleMapperDto.getIsRenderedServiceCancelled())
						.setParameter("isUpdatePatientReg", userRoleMapperDto.getIsUpdatePatientReg())
						.setParameter("opdBillCancellation", userRoleMapperDto.getOpdBillCancellation())
						.setParameter("ipdBillCancellation", userRoleMapperDto.getIpdBillCancellation())
						.setParameter("billSettlement", userRoleMapperDto.getBillSettlement())
						.setParameter("ipBillSettlement", userRoleMapperDto.getIpBillSettlement())
						.setParameter("serviceRateEditable", userRoleMapperDto.getServiceRateEditable())
						.setParameter("isForceUpdateServicePackage", userRoleMapperDto.getIsForceUpdateServicePackage())
						.setParameter("pharmacyBillCancellation", userRoleMapperDto.getPharmacyBillCancellation())
						.setParameter("pharmacyReceiptCancellation", userRoleMapperDto.getPharmacyReceiptCancellation())
						.setParameter("sampleRecollection", userRoleMapperDto.getSampleRecollection())
						.setParameter("nursingOpScan", userRoleMapperDto.getNursingOpScan())
						.setParameter("nursingIpScan", userRoleMapperDto.getNursingIpScan())
						.setParameter("doctorOpScan", userRoleMapperDto.getDoctorOpScan())
						.setParameter("doctorIpScan", userRoleMapperDto.getDoctorIpScan())
						.setParameter("changePayeeRights", userRoleMapperDto.getChangePayeeRights())
						.setParameter("extendPdd", userRoleMapperDto.getExtendPdd())
						.setParameter("isUpdateIpdChargesRemark", userRoleMapperDto.getIsUpdateIpdChargesRemark())
						.setParameter("updateDischargeSummary", userRoleMapperDto.isUpdateDischargeSummary())
						.setParameter("ipRenderedDateChange", userRoleMapperDto.getIpRenderedDateChange() != null ?userRoleMapperDto.getIpRenderedDateChange() :false)
						.setParameter("isBillManipulationAllowed", userRoleMapperDto.getIsBillManipulationAllowed())
						.setParameter("depositReceiptCancellation", userRoleMapperDto.getDepositReceiptCancellation() != null ?userRoleMapperDto.getDepositReceiptCancellation():false )
						.setParameter("unitId", userRoleMapperDto.getUnitId())
						.setParameter("userId", userRoleMapperDto.getUserId())
						.setParameter("organisationId", userRoleMapperDto.getOrganisationId())
						.executeUpdate();

				if (rowsUpdated > 0) {
					System.out.println("Update successful: " + rowsUpdated + " rows updated.");
				} else {
					System.out.println("No rows updated.");
				}


				return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
				
		} catch (HibernateException he) {
			logger.error("updateUserUnitMapper failed", he);
		} catch (Exception e) {
			logger.error("updateUserUnitMapper failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


		@Override
		public Response saveUserWiseCovrsheetTabList(UserRoleMapperDto userRoleMapperDto) throws ApplicationException {
			try {
				Session session = sessionFactory.getCurrentSession();
				String sqlQuery = "UPDATE public.t_user_unit_mapper SET "
						+ "tab_access_ids = :tabAccessIds "
						+ "WHERE user_id = :userId "
						+ "AND unit_id = :unitId "
						+ "AND status = 'A'";
				int rowsUpdated = session.createSQLQuery(sqlQuery)
						.setParameter("tabAccessIds", userRoleMapperDto.getTabAccessIds())
						.setParameter("userId", userRoleMapperDto.getUserId())
						.setParameter("unitId", userRoleMapperDto.getUnitId())
						.executeUpdate();
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		
		} catch (HibernateException he) {
			logger.error("saveUserWiseCovrsheetTabList failed", he);
		} catch (Exception e) {
			logger.error("saveUserWiseCovrsheetTabList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}


		@Override
		public Response saveUserWiseCovrsheetTabListIPD(UserRoleMapperDto userRoleMapperDto)
				throws ApplicationException {
			try {
				Session session = sessionFactory.getCurrentSession();
				String sqlQuery = "UPDATE public.t_user_unit_mapper SET "
						+ "tab_access_ids_IPD = :tabAccessIdsIPD "
						+ "WHERE user_id = :userId "
						+ "AND unit_id = :unitId "
						+ "AND status = 'A'";
				int rowsUpdated = session.createSQLQuery(sqlQuery)
						.setParameter("tabAccessIdsIPD", userRoleMapperDto.getTabAccessIdsIPD())
						.setParameter("userId", userRoleMapperDto.getUserId())
						.setParameter("unitId", userRoleMapperDto.getUnitId())
						.executeUpdate();

				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		
		} catch (HibernateException he) {
			logger.error("saveUserWiseCovrsheetTabListIPD failed", he);
		} catch (Exception e) {
			logger.error("saveUserWiseCovrsheetTabListIPD failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}


		@Override
		public Response getUserUnitDetListByUserId(UserUnitMapperDto unitDto) throws ApplicationException {
			try {
				
				if(unitDto.getUserId()!=null)
				{
					List<UnitMasterDto> userUnitMapperDtoList = sessionFactory.getCurrentSession()
							.createSQLQuery("select mapp.unit_id as \"unitId\",mum.unit_name as \"unitName\","
									+ " mum.unit_address as \"unitAddress\" "
									+ " from t_user_unit_mapper mapp "
									+ "  inner join public.m_unit_master mum "
									+ " on mum.unit_id =mapp.unit_id "
									+ " where mapp.organisation_id=:orgId  and mapp.user_id=:userId and mapp.status='A' ")
							.setParameter("orgId", unitDto.getOrganisationId())
							.setParameter("userId", unitDto.getUserId())
							.setResultTransformer(Transformers.aliasToBean(UnitMasterDto.class)).list();
					return new Response<>(SUCCESS, SUCCESS_CODE, null, userUnitMapperDtoList, null);
				}
		} catch (HibernateException he) {
			logger.error("getUserUnitDetListByUserId failed", he);
		}
			return new Response<>(ERROR, ERROR_CODE, null, null, null);
		
		}


		@Override
		public Response getMpesaRigthById(Integer userId, Integer unitId) throws ApplicationException {
			try {
				List<UserRoleMapperDto> list=sessionFactory.getCurrentSession()
						.getNamedQuery("GET_MPESA_BY_ID")
						.setParameter("userId", userId)
						.setParameter("unitId", unitId)
						.setResultTransformer(Transformers.aliasToBean(UserRoleMapperDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
			logger.error("getMpesaRigthById failed", he);
		} catch (Exception e) {
			logger.error("getMpesaRigthById failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}


		@Override
		public Response getEnableBlockReport(Integer userId, Integer unitId) throws ApplicationException {
			try {
				List<UserRoleMapperDto> list=sessionFactory.getCurrentSession()
						.getNamedQuery("GET_ENABLE_BLOCK_REPORT_BY_ID")
						.setParameter("userId", userId)
						.setParameter("unitId", unitId)
						.setResultTransformer(Transformers.aliasToBean(UserRoleMapperDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
			logger.error("getEnableBlockReport failed", he);
		} catch (Exception e) {
			logger.error("getEnableBlockReport failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}


		@Override
		public Response getUserRoleAccess(Integer userId, Integer unitId) throws ApplicationException {
			try {
				
				List<UserRoleMapperDto> list=sessionFactory.getCurrentSession()
						.getNamedQuery("GET_USER_ROLE_ACCESS_BY_ID")
						.setParameter("userId", userId)
						.setParameter("unitId", unitId)
						.setResultTransformer(Transformers.aliasToBean(UserRoleMapperDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
			} catch (HibernateException he) {
				throw he;
			} catch (Exception e) {
				
				throw e;
			}
		}


		@Override
		public Response inActiveRolesRecode(List<Integer> roles, Integer userId, Integer unitId) throws ApplicationException {

			try {
				sessionFactory.getCurrentSession().createQuery("UPDATE UserRoleMapper "
						+ " SET status='T' "
						+ " WHERE userId=:userId "
						+ " and roleId not in (:roles) "
						)
				.setParameter("userId",userId)
				.setParameterList("roles", roles)
				.executeUpdate();
						
					List<UserRoleMapperDto> userUnitMapperDtoList = sessionFactory.getCurrentSession()
							.createSQLQuery("select "
									+ " mapp.role_id as \"roleId\" "
									+ " from t_user_role_mapper mapp "
									+ "   where  mapp.user_id=:userId and mapp.status='A' ")
							.setParameter("userId",userId)
							.setResultTransformer(Transformers.aliasToBean(UserRoleMapperDto.class)).list();
					return new Response<>(SUCCESS, SUCCESS_CODE, null, userUnitMapperDtoList, null);
					
					
		} catch (HibernateException he) {
			logger.error("inActiveRolesRecode failed", he);
		} catch (Exception e) {
			logger.error("inActiveRolesRecode failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);

		}
	}






