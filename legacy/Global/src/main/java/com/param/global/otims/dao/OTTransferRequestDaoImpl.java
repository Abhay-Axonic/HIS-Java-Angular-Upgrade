package com.param.global.otims.dao;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

import java.time.ZoneId;

import java.util.HashMap;

import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.otims.dto.OTTransferRequestDto;
import com.param.global.otims.dto.ProcedureOrderMasterDto;
import com.param.global.otims.dto.ProcedureSchedulingDto;
import com.param.opd.coversheet.dto.MedicationHistoryDto;
import com.param.otims.model.OTTransferRequest;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings({"rawtypes","unchecked","unused"})
@Repository
public class OTTransferRequestDaoImpl extends GenericDao<OTTransferRequest, Integer>implements IOTTransferRequestDao, ICommonConstants{
	final static Logger logger = Logger.getLogger(OTTransferRequestDaoImpl.class);


public OTTransferRequestDaoImpl() {
	super(OTTransferRequest.class);
}


@Override
public Response saveOTTransferRequest(OTTransferRequestDto oTTransferRequestDto) throws ApplicationException {
	try {

		LocalTime transferTime1 = LocalTime.parse(oTTransferRequestDto.getTransferTime());
		LocalDate today = LocalDate.now();
		Instant transferTime = transferTime1.atDate(today).atZone(ZoneId.of("UTC")).toInstant();

			OTTransferRequest mstr = new OTTransferRequest();
			mstr.setAdmissionId(oTTransferRequestDto.getAdmissionId());
			mstr.setOtTransferStatus(oTTransferRequestDto.getOtTransferStatus());
			mstr.setScheduleId(oTTransferRequestDto.getScheduleId());
			mstr.setTheatreId(oTTransferRequestDto.getTheatreId());
			mstr.setPatientId(oTTransferRequestDto.getPatientId());
			mstr.setTransferTime(transferTime);
			mstr.setCreatedBy(oTTransferRequestDto.getCreatedBy());
			mstr.setUpdatedBy(oTTransferRequestDto.getUpdatedBy());
			mstr.setUnitId(oTTransferRequestDto.getUnitId());
			mstr.setOrganizationId(oTTransferRequestDto.getOrganizationId());
			mstr.setCreatedDate(GlobalCommonDateUtils.getDate(oTTransferRequestDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			mstr.setUpdatedDate(GlobalCommonDateUtils.getDate(oTTransferRequestDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			mstr.setStatus(oTTransferRequestDto.getStatus());
			mstr = save(mstr);
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, mstr);
		
		} catch (HibernateException he) {
		logger.error("saveOTTransferRequest failed", he);
		} catch (Exception e) {
		logger.error("saveOTTransferRequest failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}


@Override
public Response getOTTransferRequest(OTTransferRequestDto oTTransferRequestDto) throws ApplicationException {
	try
    {
		/*List<OTTransferRequestDto> otTransferChangesList = sessionFactory.getCurrentSession().createSQLQuery(
			"  select " 
			+" distinct pr.uhid_number as \"uhidNumber\", "
			+" otreq.transfer_request_id as \"transferRequestId\","
			+" concat(coalesce(prefix_mst.prefix_code),' ',coalesce(pr.first_name),' ',coalesce(pr.last_name)) as \"patientName\", " 
			+" pr.patient_id as \"patientId\", " 
			+" otreq.admission_id as \"admissionId\", " 
			+" ot.theatre_name as \"theatreName\", " 
			+" ot.operation_theatre_id as \"theatreId\", " 
			+" to_char(schedule.prefer_time,'hh24:mi:ss') as \"transferTime\", " 
			+" concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, pr.dob)) , 'Y ' , EXTRACT(month from AGE(CURRENT_TIMESTAMP, pr.dob)),'M ',EXTRACT(day from AGE(CURRENT_TIMESTAMP, pr.dob)),'D ') as \"age\", " 
			+" to_char(schedule.schedule_date, 'YYYY-MM-DD')  as \"preferedDateTime\" , " 
			+" to_char(schedule.prefer_time,'hh24:mi:ss') as \"preferTime\", "  
			+" schedule.procedure_name as \"procedureName\"," 
			+" schedule.ot_transfer_status_id as \"otTransferStatus\" ,"
			+" concat( ward.ward_name ,' - ', bm.bed_number) as \"wardName\", " 
			+"	pr.gender_id as \"genderId\", "
			+"	pr.image_path as \"imagePath\", "
			+"  gender.gender_code as \"genderCode\", " 
			+" prosts.status as \"otTransferStatusName\" " 
			+" from otims.t_transfer_request  otreq "
			+" left join otims.t_procedure_schedule schedule on otreq.schedule_id = schedule.procedure_schedule_id " 
			+" inner join otims.m_operation_theatre ot on otreq.theatre_id = ot.operation_theatre_id " 
			+" inner join patient.t_patient_registration pr on otreq.patient_id = pr.patient_id " 
			+"  inner join public.m_gender_master gender on pr.gender_id = gender.gender_id " 
			+" inner join public.m_prefix_master prefix_mst on pr.prefix_id = prefix_mst.prefix_id " 
			+" inner join adt.t_admission admission on otreq.admission_id = admission.admission_id " 
			+" inner join adt.t_admission_details adtDtls on admission.admission_id = adtDtls.admission_id and adtDtls.status='A' "
			+" inner JOIN adt.m_bed_master bm on adtDtls.bed_id = bm.bed_id " 
			+" inner JOIN adt.m_ward_master ward on bm.ward_id = ward.ward_id " 
			+" inner join otims.m_procedure_order_status  prosts on prosts.id = otreq.ot_transfer_status " 
			+" where  otreq.organization_id = " +oTTransferRequestDto.getOrganizationId() 
			+" and otreq.unit_id =  " + oTTransferRequestDto.getUnitId()
			+" and (schedule.status = 'A' or schedule.status is null) "
			+" and otreq.ot_transfer_status = 6 "
			+ "and schedule.proceduer_status_id in (2,3,4) " )
			
			.setResultTransformer(Transformers.aliasToBean(OTTransferRequestDto.class)).list();*/
		
		Integer count=0;
		String query="  select " 
				+" distinct pr.uhid_number as \"uhidNumber\", "
				+" otreq.transfer_request_id as \"transferRequestId\","
				+" concat(coalesce(prefix_mst.prefix_code),' ',coalesce(pr.first_name),' ',coalesce(pr.last_name)) as \"patientName\", " 
				+" pr.patient_id as \"patientId\", " 
				+" otreq.admission_id as \"admissionId\", " 
				+" ot.theatre_name as \"theatreName\", " 
				+" ot.operation_theatre_id as \"theatreId\", " 
				+" to_char(schedule.prefer_time,'hh24:mi:ss') as \"transferTime\", " 
				+" concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, pr.dob)) , 'Y ' , EXTRACT(month from AGE(CURRENT_TIMESTAMP, pr.dob)),'M ',EXTRACT(day from AGE(CURRENT_TIMESTAMP, pr.dob)),'D ') as \"age\", " 
				+" to_char(schedule.schedule_date, 'YYYY-MM-DD')  as \"preferedDateTime\" , " 
				+" to_char(schedule.prefer_time,'hh24:mi:ss') as \"preferTime\", "  
				+" schedule.procedure_name as \"procedureName\"," 
				+" schedule.ot_transfer_status_id as \"otTransferStatus\" ,"
				+" concat( ward.ward_name ,' - ', bm.bed_number) as \"wardName\", " 
				+"	pr.gender_id as \"genderId\", "
				+"	pr.image_path as \"imagePath\", "
				+"  gender.gender_code as \"genderCode\", " 
				+" prosts.status as \"otTransferStatusName\" " 
				+" from otims.t_transfer_request  otreq "
				+" left join otims.t_procedure_schedule schedule on otreq.schedule_id = schedule.procedure_schedule_id " 
				+" inner join otims.m_operation_theatre ot on otreq.theatre_id = ot.operation_theatre_id " 
				+" inner join patient.t_patient_registration pr on otreq.patient_id = pr.patient_id " 
				+"  inner join public.m_gender_master gender on pr.gender_id = gender.gender_id " 
				+" inner join public.m_prefix_master prefix_mst on pr.prefix_id = prefix_mst.prefix_id " 
				+" inner join adt.t_admission admission on otreq.admission_id = admission.admission_id " 
				+" inner join adt.t_admission_details adtDtls on admission.admission_id = adtDtls.admission_id and adtDtls.status='A' "
				+" inner JOIN adt.m_bed_master bm on adtDtls.bed_id = bm.bed_id " 
				+" inner JOIN adt.m_ward_master ward on bm.ward_id = ward.ward_id " 
				+" inner join otims.m_procedure_order_status  prosts on prosts.id = otreq.ot_transfer_status " 
				+" where  otreq.organization_id =:organizationId "
				+" and otreq.unit_id =:unitId  "
				+" and (schedule.status = 'A' or schedule.status is null) "
				+" and otreq.ot_transfer_status = 6 "
				+ "and schedule.proceduer_status_id in (2,3,4) ";
		List<OTTransferRequestDto> otTransferChangesList=sessionFactory.getCurrentSession().createSQLQuery(query)
				.setParameter("organizationId",oTTransferRequestDto.getOrganizationId() )
				.setParameter("unitId",oTTransferRequestDto.getUnitId())
				.setFirstResult(oTTransferRequestDto.getOffset() != null ? oTTransferRequestDto.getOffset()  : 0)
				.setMaxResults(oTTransferRequestDto.getNoOfRecordsPerPage() != null ? oTTransferRequestDto.getNoOfRecordsPerPage()  : 10)
				.setResultTransformer(Transformers.aliasToBean(OTTransferRequestDto.class)).list();
		
		List<OTTransferRequestDto> listCount=sessionFactory.getCurrentSession().createSQLQuery(query)
				.setParameter("organizationId",oTTransferRequestDto.getOrganizationId())
				.setParameter("unitId",oTTransferRequestDto.getUnitId())
				.setResultTransformer(Transformers.aliasToBean(OTTransferRequestDto.class)).list();
		
		 if(listCount != null && listCount.size()>0) {
			 count= listCount.size();
            }
		
			return new Response<>(SUCCESS, SUCCESS_CODE, null, otTransferChangesList, count);
	
    } catch (Exception e)
    {
    logger.error("getOTTransferRequest failed", e);
    }
    return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
}


@Override
public Response updateScheduleProcedureStatus(OTTransferRequestDto oTTransferRequestDto) throws ApplicationException {
	try{	
		Integer count = sessionFactory.getCurrentSession().createSQLQuery(" update otims.t_procedure_schedule set ot_transfer_status_id = 5 "
				+ " where procedure_schedule_id =:procedureScheduleId ")
				.setParameter("procedureScheduleId",oTTransferRequestDto.getScheduleId())
				.executeUpdate();
		
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
	
	}catch (HibernateException he) {
	logger.error("updateScheduleProcedureStatus failed", he);
	} catch (Exception e) {
	logger.error("updateScheduleProcedureStatus failed", e);
	}
	return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}


@Override
public Response updateOTTransferRequest(OTTransferRequestDto oTTransferRequestDto) throws ApplicationException {
	try{	
		
		OTTransferRequest mstr = findById(oTTransferRequestDto.getTransferRequestId());
		if(oTTransferRequestDto.getOtTransferStatus()==7) {
			mstr.setReasonId(oTTransferRequestDto.getReasonId());
			mstr.setReasonNote(oTTransferRequestDto.getReasonNote());
		}
		mstr.setOtTransferStatus(oTTransferRequestDto.getOtTransferStatus());
		update(mstr);
		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
	
	}catch (HibernateException he) {
	logger.error("updateOTTransferRequest failed", he);
	} catch (Exception e) {
	logger.error("updateOTTransferRequest failed", e);
	}
	return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}


@Override
public Response getOTTransferByAdmissionId(OTTransferRequestDto oTTransferRequestDto) throws ApplicationException {
	try
    {
		OTTransferRequestDto dto = (OTTransferRequestDto)sessionFactory.getCurrentSession().createSQLQuery(
			"  select "
			+" otreq.ot_transfer_status as \"otTransferStatus\" ,"
			+" otreq.transfer_request_id as \"transferRequestId\" " 
			+" from otims.t_transfer_request  otreq "
			+" where admission_id = :admissionId "
			+" and otreq.status = 'A' "
			+" and otreq.ot_transfer_status in(6,8) " )
				.setParameter("admissionId",oTTransferRequestDto.getAdmissionId())
			.setResultTransformer(Transformers.aliasToBean(OTTransferRequestDto.class)).uniqueResult();
		
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, dto);
	
    } catch (Exception e)
    {
    logger.error("getOTTransferByAdmissionId failed", e);
    }
    return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
}


@Override
public Response initOTTransferRequest(OTTransferRequestDto oTTransferRequestDto) throws ApplicationException {
	try
	{
		StringBuilder query = new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("INIT_OT_REQUIEST_LIST").getQueryString());
		Map<String, Object> parameters = new HashMap<>();
		Integer count = 0;

// Add conditionally the admissionId parameter
		if (oTTransferRequestDto.getAdmissionId() != null && oTTransferRequestDto.getAdmissionId() > 0) {
			query.append(" and admission.admission_id = :admissionId");
			parameters.put("admissionId", oTTransferRequestDto.getAdmissionId());
		}

// Add conditionally the wardList parameter
		if (oTTransferRequestDto.getWardList() != null && oTTransferRequestDto.getWardList().size() > 0) {
			String wardIds = oTTransferRequestDto.getWardList().toString().replace('[', ' ').replace(']', ' ').trim();
			query.append(" and bm.ward_id IN (" + wardIds + ")");
		}

// Add the order by condition
		query.append(" order by admission.admission_id");

// Create SQL query for the list of results
		Query sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
				.setParameter("unitId", oTTransferRequestDto.getUnitId())
				.setParameter("organizationId", oTTransferRequestDto.getOrganizationId())
				.setFirstResult(oTTransferRequestDto.getOffset() != null ? oTTransferRequestDto.getOffset() : 0)
				.setMaxResults(oTTransferRequestDto.getNoOfRecordsPerPage() != null ? oTTransferRequestDto.getNoOfRecordsPerPage() : 10)
				.setResultTransformer(Transformers.aliasToBean(ProcedureSchedulingDto.class));

// Set parameters dynamically for the main query
		for (Map.Entry<String, Object> entry : parameters.entrySet()) {
			sqlQuery.setParameter(entry.getKey(), entry.getValue());
		}

		List<ProcedureSchedulingDto> list = sqlQuery.list();

// Create SQL query for count (without the offset and limit)
		String countQuery = new StringBuilder(query).toString();
		Query countSqlQuery = sessionFactory.getCurrentSession().createSQLQuery(countQuery)
				.setParameter("unitId", oTTransferRequestDto.getUnitId())
				.setParameter("organizationId", oTTransferRequestDto.getOrganizationId())
				.setResultTransformer(Transformers.aliasToBean(ProcedureSchedulingDto.class));

		for (Map.Entry<String, Object> entry : parameters.entrySet()) {
			countSqlQuery.setParameter(entry.getKey(), entry.getValue());
		}

		List<ProcedureSchedulingDto> listCount = countSqlQuery.list();

// Update the count if results are present
		if (listCount != null && listCount.size() > 0) {
			count = listCount.size();
		}


		return new Response(SUCCESS, SUCCESS_CODE, null, list, count);

	} catch (Exception e)
	{
	logger.error("initOTTransferRequest failed", e);
	}
	return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
}


}
