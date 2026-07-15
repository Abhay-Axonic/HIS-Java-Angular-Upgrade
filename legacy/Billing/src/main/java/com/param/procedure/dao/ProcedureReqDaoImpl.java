package com.param.procedure.dao;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import com.param.global.billing.controller.BillingException;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.otims.dto.ProcedureReqMasterDto;
import com.param.otims.model.ProcedureReqMaster;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;

@Repository
@SuppressWarnings({"rawtypes","unchecked","unused"})
public class ProcedureReqDaoImpl extends GenericDao<ProcedureReqMaster, Integer> implements ICommonConstants, IProcedureReqDao {

	public ProcedureReqDaoImpl() {
		super(ProcedureReqMaster.class);
	}

	@Override
	public Response saveProcedureReq(ProcedureReqMasterDto procedureReqMasterDto) throws ServiceException {
		try{
			ProcedureReqMaster master = new ProcedureReqMaster();
			master.setPrimaryProcedureId(procedureReqMasterDto.getPrimaryProcedureId());
			master.setPatientId(procedureReqMasterDto.getPatientId());
			master.setAdmissionId(procedureReqMasterDto.getAdmissionId());
			master.setEncounterId(procedureReqMasterDto.getEncounterId());
			master.setVisitTypeId(procedureReqMasterDto.getVisitTypeId());
			master.setPreferedDateTime(procedureReqMasterDto.getPreferedDateTime() != null && !procedureReqMasterDto.getPreferedDateTime().isEmpty() ? GlobalCommonDateUtils.getDate(procedureReqMasterDto.getPreferedDateTime(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(procedureReqMasterDto.getUnitId()));
			master.setRequestDate(procedureReqMasterDto.getRequestDate() != null && !procedureReqMasterDto.getRequestDate().isEmpty() ? GlobalCommonDateUtils.getDate(procedureReqMasterDto.getRequestDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(procedureReqMasterDto.getUnitId()));
			master.setRequestBy(procedureReqMasterDto.getRequestBy());
			master.setProcedureStatusId(procedureReqMasterDto.getProcedureStatusId());
			master.setOtBillCompletionDateTime(procedureReqMasterDto.getOtBillCompletionDateTime() != null && !procedureReqMasterDto.getOtBillCompletionDateTime().isEmpty() ? GlobalCommonDateUtils.getDate(procedureReqMasterDto.getOtBillCompletionDateTime(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(procedureReqMasterDto.getUnitId()));
			master.setOtBillCompletionBy(procedureReqMasterDto.getOtCompletionBy());
			master.setOtBillCompletionDateTime(procedureReqMasterDto.getOtBillCompletionDateTime() != null && !procedureReqMasterDto.getOtBillCompletionDateTime().isEmpty() ? GlobalCommonDateUtils.getDate(procedureReqMasterDto.getOtBillCompletionDateTime(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(procedureReqMasterDto.getUnitId()));
			master.setOtBillCompletionBy(procedureReqMasterDto.getOtBillCompletionBy());
			master.setOrgId(procedureReqMasterDto.getOrgId());
			master.setUnitId(procedureReqMasterDto.getUnitId());
			master.setCreatedBy(procedureReqMasterDto.getCreatedBy());
			//master.setCreatedDate(procedureReqMasterDto.getCreatedDate() != null && !procedureReqMasterDto.getCreatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(procedureReqMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(procedureReqMasterDto.getUnitId()));
			master.setCreatedDate(TimeZoneComponent.getDateByzone(procedureReqMasterDto.getUnitId()));
			master.setUpdatedBy(procedureReqMasterDto.getUpdatedBy());
			if(procedureReqMasterDto.getPreferedTime()!=null) {
				LocalTime preferedTime1 = LocalTime.parse(procedureReqMasterDto.getPreferedTime());
				LocalDate today = LocalDate.now();
				Instant preferedTime = preferedTime1.atDate(today).atZone(ZoneId.of("UTC")).toInstant();

				master.setPreferedTime(preferedTime);
			}else {
				master.setPreferedTime(Instant.now());
			}
			master.setUpdatedDate(TimeZoneComponent.getDateByzone(procedureReqMasterDto.getUnitId()));
			master.setEmergency(procedureReqMasterDto.getEmergency() != null?procedureReqMasterDto.getEmergency():false);
			if(procedureReqMasterDto.getProReqId()!=null) {
				master.setProReqId(procedureReqMasterDto.getProReqId());
				master = update(master);
			}else {
				master = save(master);
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, master);
		}catch(HibernateException he){
			throw new BillingException(he.getMessage());
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}	
	}

	@Override
	public Response updateProcedureReqStatus(int id, int statusId) throws ServiceException {
		try{
			int result = sessionFactory.getCurrentSession().getNamedQuery("UPDATE_PROCEDURE_REQ_STATUS_BY_ID")
					.setParameter("statusId", statusId)
					.setParameter("id", id).executeUpdate();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, result);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response updateProcedureOrderFlag(Integer procedureOrderId) throws ServiceException {
		try{
			int result = sessionFactory.getCurrentSession().getNamedQuery("UPDATE_PROCEDURE").setParameter("proId", procedureOrderId).executeUpdate();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, result);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response updateProcedureRequestSeconderyToPrimary(Integer proReqId, Integer serviceId) {
		try{	
			Integer count=sessionFactory.getCurrentSession().createSQLQuery(" update otims.t_procedure_req_mst set primary_procedure_id =:serviceId "
					+ " where pro_req_id =:proReqId ")
					.setParameter("proReqId", proReqId)
					.setParameter("serviceId", serviceId)
					.executeUpdate();
			
			return new Response(SUCCESS, SUCCESS_CODE, null, null, null);
			
		}catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateProcedureScheduleStatus(Integer procedureRequestId, int statusId) throws ServiceException {
		try{
			int result = sessionFactory.getCurrentSession().getNamedQuery("UPDATE_PROCEDURE_SCHEDULE_STATUS_BY_ID").setParameter("statusId", statusId).setParameter("id", procedureRequestId).executeUpdate();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, result);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response cancelProcedureReq(Integer proReqId) throws ServiceException {
		try{	
			Integer count=sessionFactory.getCurrentSession().createSQLQuery(" update otims.t_procedure_req_mst set procedure_status_id = 9 "
					+ " where pro_req_id =:proReqId ")
					.setParameter("proReqId", proReqId)
					.executeUpdate();
			
			return new Response(SUCCESS, SUCCESS_CODE, null, null, null);
			
		}catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}
