package com.param.pharmacy.master.global.dao.equipment;

import java.math.BigInteger;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.entity.model.pharmacy.ScheduleMaster;
import com.param.global.common.ICommonConstants;
import com.param.global.common.IError;
import com.param.global.common.Response;
import com.param.pharmacy.master.global.dto.equipment.ScheduleMasterDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ScheduleMasterDaoImpl extends GenericDao<ScheduleMaster, Integer>
		implements IScheduleMasterDao, ICommonConstants, IError {

	public ScheduleMasterDaoImpl() {
		super(ScheduleMaster.class);
	}

	final static Logger logger = Logger.getLogger(ScheduleMaster.class);

	@Override
	public Response saveScheduleMaster(ScheduleMasterDto scheduleMasterDto) throws ApplicationException {
		try{
			//System.out.println(scheduleMasterDto.toString());
			ScheduleMaster scheduleMaster= new ScheduleMaster();
			 scheduleMaster.setScheduleCode(scheduleMasterDto.getScheduleCode());
			 scheduleMaster.setScheduleDescription(scheduleMasterDto.getScheduleDescription());
			 scheduleMaster.setStatus(scheduleMasterDto.getStatus());
		     scheduleMaster.setUnitId(scheduleMasterDto.getUnitId());
		     scheduleMaster.setOrgId(scheduleMasterDto.getOrgId());
		     scheduleMaster.setCreatedBy(scheduleMasterDto.getCreatedBy());
		     scheduleMaster.setCreatedDate(TimeZoneComponent.getDateByzone(scheduleMasterDto.getUnitId()));
		     scheduleMaster.setUpdatedBy(scheduleMasterDto.getUpdatedBy());
		     scheduleMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(scheduleMasterDto.getUnitId()));
		     scheduleMaster.setColorCode(scheduleMasterDto.getColorCode());
		     sessionFactory.getCurrentSession().save(scheduleMaster);
	     	return new Response<>(SUCCESS, null, null, null, null);
	}   catch(Exception e){
		throw e;
	}
		}
	
	@Override
	public Response getScheduleMasterById(Integer orgId, Integer unitId, Integer scheduleId)
			throws ApplicationException {
		try {
			
			ScheduleMasterDto SerologyTestMasterDto = (ScheduleMasterDto) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SCHEDULE_BY_SCHEDULE_ID").setParameter("orgId", orgId)
					.setParameter("scheduleId", scheduleId)
//					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(ScheduleMasterDto.class)).uniqueResult();

			return new Response(SUCCESS, null, null, null, SerologyTestMasterDto);

		} catch (Exception e) {
			logger.error("Exection", e);
			return new Response(ERROR, ERR_500, "SCHEDULE_NOT_FOUND", null, null);
		}

	}

	@Override
	public Response updateScheduleMaster(ScheduleMasterDto scheduleMasterDto) throws ApplicationException {
    try{
			//System.out.println(scheduleMasterDto.toString());
			ScheduleMaster scheduleMaster= new ScheduleMaster();
			 scheduleMaster.setScheduleId(scheduleMasterDto.getScheduleId());
			 scheduleMaster.setScheduleCode(scheduleMasterDto.getScheduleCode());
			 scheduleMaster.setScheduleDescription(scheduleMasterDto.getScheduleDescription());
		     scheduleMaster.setUnitId(scheduleMasterDto.getUnitId());
		     scheduleMaster.setOrgId(scheduleMasterDto.getOrgId());
		     scheduleMaster.setStatus(scheduleMasterDto.getStatus());
		     scheduleMaster.setCreatedBy(scheduleMasterDto.getCreatedBy());
		     scheduleMaster.setCreatedDate(TimeZoneComponent.getDateByzone(scheduleMasterDto.getUnitId()));
		     scheduleMaster.setUpdatedBy(scheduleMasterDto.getUpdatedBy());
		     scheduleMaster.setColorCode(scheduleMasterDto.getColorCode());
		     scheduleMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(scheduleMasterDto.getUnitId()));
		     sessionFactory.getCurrentSession().update(scheduleMaster);
	     	return new Response<>(SUCCESS, null, null, null, null);
	}   catch(Exception e){
		throw e;
	}
		}
	@Override
	public Response ActivateInactivateScheduleMaster(Integer orgId, Integer scheduleId, Character scheduleStatus)
			throws ApplicationException {

		try {
			ScheduleMaster scheduleMaster = findById(scheduleId);
			if (scheduleMaster.getScheduleId() != 0) {
				scheduleMaster.setStatus(scheduleStatus);
				ScheduleMaster departmentMst = update(scheduleMaster);
				if (departmentMst.getStatus() == ACTIVE) {
					return new Response(SUCCESS, null, "SCHEDULE_TEST_ACTIVATE_SUCC", null, null);
				} else if (departmentMst.getStatus() == INACTIVE) {
					return new Response(SUCCESS, null, "SCHEDULE_TEST_INACTIVATE_SUCC", null, null);
				} else {
					if (scheduleStatus == ACTIVE) {
						return new Response(SUCCESS, null, "SCHEDULE_ACTIVATE_FAIL", null, null);
					} else {
						return new Response(SUCCESS, null, "SCHEDULE_INACTIVATE_FAIL", null, null);
					}
				}
			} else {
				return new Response(SUCCESS, null, "SCHEDULE_NOT_FOUND", null, null);
			}

		} catch (Exception e) {
			logger.error("Exection", e);
			return new Response(ERROR, ERR_500, "SCHEDULE_NOT_FOUND", null, null);
		}
	}

	@Override
	public Response listScheduleMaster(Integer orgId, Integer unitId, Integer offset, Integer recordPerPage)
			throws ApplicationException {
		try {
			List<ScheduleMasterDto> listTechniqueMasterDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PAGINATED_SCHEDULE_MASTER_LIST").setParameter("orgId", orgId)
//					.setParameter("unitId", unitId).setFirstResult(offset != null ? offset : 0)
					.setMaxResults(recordPerPage != null ? recordPerPage : 10)
					.setResultTransformer(Transformers.aliasToBean(ScheduleMasterDto.class)).list();

			if (listTechniqueMasterDto.isEmpty()) {
				return new Response(ERROR, ERR_500, "SCHEDULE_RECORDS_NOT_FOUND", null, null);
			} else {
				return new Response(SUCCESS, null, null, listTechniqueMasterDto, null);
			}

		} catch (Exception e) {
			logger.error("Exection", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}

	@Override
	public Response getToTalScheduleMasterRecord(Integer orgId, Integer unitId) throws ApplicationException {
		try {
			BigInteger scheduleMasterTotalRecordCount = (BigInteger) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_TOTAL_SCHEDULE_RECORD")
//					.setParameter("unitId", unitId)
					.setParameter("orgId", orgId)
					.uniqueResult();

			if (scheduleMasterTotalRecordCount.compareTo(BigInteger.ZERO) == 1) {
				return new Response(SUCCESS, null, null, null, scheduleMasterTotalRecordCount);
			} else {
				return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, BigInteger.ZERO);
			}

		} catch (Exception e) {
			logger.error("Exection", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}
}
