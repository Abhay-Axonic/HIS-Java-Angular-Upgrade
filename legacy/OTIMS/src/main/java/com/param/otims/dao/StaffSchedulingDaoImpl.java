package com.param.otims.dao;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.otims.dto.StaffSchedulingDto;
import com.param.otims.model.StaffScheduling;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class StaffSchedulingDaoImpl extends GenericDao<StaffScheduling, Integer> implements IStaffSchedulingDao,ICommonConstants{

	private static final Logger logger = Logger.getLogger(StaffSchedulingDaoImpl.class);

	public StaffSchedulingDaoImpl() {
		super(StaffScheduling.class);
	}

	@Override
	public Response saveStaffScheduling(List<StaffSchedulingDto>  list) throws ApplicationException {
	
		try {
			
			for (StaffSchedulingDto staffSchedulingDto : list) {

				LocalTime fromTime1 = LocalTime.parse( staffSchedulingDto.getFromTime());
				LocalDate today = LocalDate.now();
				Instant fromTime = fromTime1.atDate(today).atZone(ZoneId.of("UTC")).toInstant();

				LocalTime toTime1 = LocalTime.parse( staffSchedulingDto.getToTime());
				Instant toTime = toTime1.atDate(today).atZone(ZoneId.of("UTC")).toInstant();

				StaffScheduling mstr = new StaffScheduling();
				//System.out.println("fromTime     "+fromTime);
				mstr.setDayId(staffSchedulingDto.getDayId());
				mstr.setFromTime(fromTime);
				mstr.setToTime(toTime);
				Date date1=new SimpleDateFormat("yyyy-M-dd").parse(staffSchedulingDto.getStaffSchedulingDate());  
				mstr.setStaffSchedulingDate(date1);
				mstr.setSwitchOverTime(staffSchedulingDto.getSwitchOverTime());
				mstr.setUserId(staffSchedulingDto.getUserId());
				mstr.setRoleId(staffSchedulingDto.getRoleId());
				mstr.setCreatedBy(staffSchedulingDto.getCreatedBy());
				mstr.setCreatedDate(GlobalCommonDateUtils.getDate(staffSchedulingDto.getCreatedDate(), "dd-M-yyyy HH:ss:mm"));
				mstr.setOrgId(staffSchedulingDto.getOrgId());
				mstr.setStatus(staffSchedulingDto.getStatus());
				mstr.setUnitId(staffSchedulingDto.getUnitId());
				mstr.setUpdatedBy(staffSchedulingDto.getUpdatedBy());
				mstr.setUpdatedDate(GlobalCommonDateUtils.getDate(staffSchedulingDto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));
				save(mstr);
			
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

	}

	@Override
	public Response getStaffScheduling(StaffSchedulingDto staffSchedulingDto) throws ApplicationException {
		try {
			
			int count = 0;
			List<StaffSchedulingDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_STAFF_SCHEDULING")
					.setFirstResult(staffSchedulingDto.getOffset() != null ? staffSchedulingDto.getOffset() : 0)
					.setMaxResults(staffSchedulingDto.getNoOfRecordsPerPage() != null ? staffSchedulingDto.getNoOfRecordsPerPage() : 10)
					.setParameter("organizationId", staffSchedulingDto.getOrgId())
					.setParameter("unitId", staffSchedulingDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(StaffSchedulingDto.class)).list();
			if(list!=null && list.size()>0) {
				List<StaffSchedulingDto> listForCount = sessionFactory.getCurrentSession()
						.getNamedQuery("GET_STAFF_SCHEDULING")
						.setParameter("organizationId", staffSchedulingDto.getOrgId())
						.setParameter("unitId", staffSchedulingDto.getUnitId())
						.setResultTransformer(Transformers.aliasToBean(StaffSchedulingDto.class)).list();
				count = listForCount.size();
			}
			
			return new Response(SUCCESS, SUCCESS_CODE, null, list, count);

			} catch (HibernateException he) {
				logger.error(he.getMessage(), he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				throw new ServiceException(e.getMessage());
			}
	}

	@Override
	public Response deactivateStaffSchedule(StaffSchedulingDto staffSchedulingDto) throws ApplicationException {
		try {
			
			StaffScheduling mstr = findById(staffSchedulingDto.getStaffSchedulingId());
			mstr.setStatus(staffSchedulingDto.getStatus());
			update(mstr);
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response checkStaffScheduling(StaffSchedulingDto dto) throws ApplicationException {
		try {
			List<StaffSchedulingDto> obj = sessionFactory.getCurrentSession()
				.getNamedQuery("TO_CHECK_ALREADY_EXITS_STAFF_SCHEDULING")
				.setParameter("organizationId", dto.getOrgId())
				.setParameter("unitId", dto.getUnitId())
				.setParameter("userId", dto.getUserId())
				.setParameter("roleId", dto.getRoleId())
				.setParameter("staffScheDate", dto.getStaffSchedulingDate())
				.setParameter("fromTime", dto.getFromTime())
				.setParameter("toTime", dto.getToTime())
				.setResultTransformer(Transformers.aliasToBean(StaffSchedulingDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, obj,null );

		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getStaffListByRoleId(StaffSchedulingDto staffSchedulingDto) throws ApplicationException {
		try {
			List<StaffSchedulingDto> userlist = sessionFactory.getCurrentSession().createSQLQuery(
					"select distinct um.user_id as \"userId\","
					+ " otm.role_name as \"roleName\","
					+ " otm.role_id as \"roleId\","
                    + " dm.email as \"emailId\", "
					+ " concat('Dr.',' ',coalesce(dm.first_name,'') ,' ', coalesce(dm.middle_name,'') ,' ', coalesce(dm.last_name,'')) as \"userName\" "
					+ " from doctor.m_doctor_master dm  "
					+ " inner join public.m_user_master um on dm.user_id = um.user_id "
					+ "  inner join public.t_user_role_mapper orm  on orm.user_id = um.user_id " 
					+ " inner join  public.m_role_master otm on otm.role_id = orm.role_id "
					+ " where  otm.role_id  ="+ staffSchedulingDto.getRoleId() + " "
					+ " and otm.organisation_id  ="+ staffSchedulingDto.getOrgId() +" "
					+ " and otm.unit_id ="+ staffSchedulingDto.getUnitId() + ""
					+ " and orm.status = 'A' "
					+ " union all "
					+" select  distinct em.user_id as \"userId\","
					+ " otm.role_name as \"roleName\","
					+ " otm.role_id as \"roleId\","
                    + " em.email as \"emailId\", "
					+ " concat(coalesce(em.first_name,'') ,' ', coalesce(em.middle_name,'') ,' ', coalesce(em.last_name,'')) as \"userName\" "
					+ " from  public.m_employee_master em "
					+ " inner join public.m_user_master um on em.user_id = um.user_id "
					+ " inner join public.t_user_role_mapper orm  on orm.user_id = um.user_id  "
					+ " inner join  public.m_role_master otm on otm.role_id = orm.role_id  "
					+ " where  otm.role_id  ="+ staffSchedulingDto.getRoleId() + " "
					+ " and otm.organisation_id  ="+ staffSchedulingDto.getOrgId() +" "
					+ " and otm.unit_id ="+ staffSchedulingDto.getUnitId() + " "
					+ " and orm.status = 'A' "
					+ " ORDER BY \"userName\" ASC "
				
					)
					.setResultTransformer(Transformers.aliasToBean(StaffSchedulingDto.class)).list();
			

		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, userlist, null);

		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response checkStaffAvailableOrNot(StaffSchedulingDto dto) throws ApplicationException {
		try {
			List<StaffSchedulingDto> obj = sessionFactory.getCurrentSession()
				.getNamedQuery("TO_CHECK_AVAIBILITY_SCHEDULING")
				.setParameter("organizationId", dto.getOrgId())
				.setParameter("unitId", dto.getUnitId())
				.setParameter("userId", dto.getUserId())
				.setParameter("roleId", dto.getRoleId())
				.setParameter("staffScheDate", dto.getStaffSchedulingDate())
				.setParameter("fromTime", dto.getFromTime())
				.setParameter("toTime", dto.getToTime())
				.setResultTransformer(Transformers.aliasToBean(StaffSchedulingDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, obj,null );

		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getOtimsRoles(StaffSchedulingDto staffSchedulingDto) throws ApplicationException {
		try {
			List<StaffSchedulingDto> userlist = sessionFactory.getCurrentSession().createSQLQuery(
					"select otm.role_id as \"roleId\","
					+ " otm.role_name as \"roleName\" "
					+ " from public.m_otims_role_master otm "
					+ " where otm.org_id  ="+ staffSchedulingDto.getOrgId() +" "
					+ " and otm.unit_id ="+ staffSchedulingDto.getUnitId() + " "
					)
					.setResultTransformer(Transformers.aliasToBean(StaffSchedulingDto.class)).list();
			

		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, userlist, null);

		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getStaffListByRoleTypeId(StaffSchedulingDto staffSchedulingDto) throws ApplicationException {
		try {
			List<StaffSchedulingDto> userlist = sessionFactory.getCurrentSession().createSQLQuery(
					"select distinct um.user_id as \"userId\","
					+ " otm.role_name as \"roleName\","
					+ " otm.role_id as \"roleId\","
                    + " dm.email as \"emailId\", "
					+ " concat('Dr.',' ',coalesce(dm.first_name,'') ,' ', coalesce(dm.middle_name,'') ,' ', coalesce(dm.last_name,'')) as \"userName\" "
					+ " from doctor.m_doctor_master dm  "
					+ " inner join public.m_user_master um on dm.user_id = um.user_id "
					+ "  inner join public.t_user_role_mapper orm  on orm.user_id = um.user_id " 
					+ " inner join  public.m_role_master otm on otm.role_id = orm.role_id "
					+ " where  otm.role_type_id  ="+ staffSchedulingDto.getRoleId() + " "
					+ " and otm.organisation_id  ="+ staffSchedulingDto.getOrgId() +" "
					+ " and otm.unit_id ="+ staffSchedulingDto.getUnitId() + " "
					+ " and orm.status = 'A' "
					+ " union all "
					+" select  distinct em.user_id as \"userId\","
					+ " otm.role_name as \"roleName\","
					+ " otm.role_id as \"roleId\","
                    + " em.email as \"emailId\", "
					+ " concat(coalesce(em.first_name,'') ,' ', coalesce(em.middle_name,'') ,' ', coalesce(em.last_name,'')) as \"userName\" "
					+ " from  public.m_employee_master em "
					+ " inner join public.m_user_master um on em.user_id = um.user_id "
					+ " inner join public.t_user_role_mapper orm  on orm.user_id = um.user_id  "
					+ " inner join  public.m_role_master otm on otm.role_id = orm.role_id  "
					+ " where  otm.role_type_id  ="+ staffSchedulingDto.getRoleId() + " "
					+ " and otm.organisation_id  ="+ staffSchedulingDto.getOrgId() +" "
					+ " and otm.unit_id ="+ staffSchedulingDto.getUnitId() + " "
					+ " and orm.status = 'A'"
				
					)
					.setResultTransformer(Transformers.aliasToBean(StaffSchedulingDto.class)).list();
			

		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, userlist, null);

		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
}
