package com.param.global.dao;

import java.net.URI;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Iterator;
import java.util.List;

import com.google.gson.Gson;
import com.param.global.dto.MyLifeConnectionLinkDto;
import com.param.opd.coversheet.dto.PatientFamilyHistoryDto;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.TVitalDetailsDto;
import com.param.global.dto.TVitalDto;
import com.param.global.dto.VitalSearchDto;
import com.param.global.model.TVital;
import com.param.global.model.TVitalDetails;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
import org.springframework.web.client.RestTemplate;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class VitalDaoImpl extends GenericDao<TVital, Integer> implements IVitalDao, ICommonConstants {
	final static Logger logger = Logger.getLogger(VitalDaoImpl.class);


	public VitalDaoImpl() {
		super(TVital.class);
	}


	@Override
	public Response saveVitals(TVitalDto dto) throws ApplicationException {
		try{
			TVital master = new TVital();

			java.util.Date unitZoneDate = TimeZoneComponent.getDateByzone(dto.getUnitId());
			LocalTime transferTime1 = (dto.getVitalTime() != null && !dto.getVitalTime().isEmpty())
					? LocalTime.parse(dto.getVitalTime())
					: (unitZoneDate != null ? LocalTime.from(unitZoneDate.toInstant().atZone(ZoneId.systemDefault())) : LocalTime.now());
			LocalDate today = LocalDate.now();
			Instant transferTime = transferTime1.atDate(today).atZone(ZoneId.of("UTC")).toInstant();

			master.setVitalTime(transferTime);
			master.setCreatedDate(dto.getCreatedDate() != null && !dto.getCreatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
			master.setUpdatedBy(dto.getUpdatedBy());
			master.setUpdatedDate(dto.getUpdatedDate() != null && !dto.getUpdatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
			master.setOrganisationId(dto.getOrganisationId());
			master.setPatientId(dto.getPatientId());
			master.setStatus(ACTIVE);
			master.setUnitId(dto.getUnitId());
			master.setPatientId(dto.getPatientId());
			master.setAdmissionId(dto.getAdmissionId());
			master.setEncounterId(dto.getEncounterId());
			master.setVisitTypeId(dto.getVisitTypeId());
			master = save(master);
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, master);
		}catch(HibernateException he)
		{
			throw he;
		}catch(Exception e){
			
			throw e;
		}
	}
	
	@Override
	public Response saveVitalDetails(TVitalDetailsDto dto) throws ApplicationException {
		try{
			TVitalDetails master = new TVitalDetails();
			master.settVitalId(dto.gettVitalId());
			master.setObservation(dto.getObservation());
			master.setVitalId(dto.getVitalId());
			master.setTypeId(dto.getTypeId());
			int id = (int) sessionFactory.getCurrentSession().save(master);
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, id);
		}catch(HibernateException he)
		{
			throw he;
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getVitalsByPatient(VitalSearchDto dto) throws ApplicationException 
	{
		try {
			String query = 		 	
								" select vital.t_vital_id as \"tVitalId\","
						                + " to_char(vital.vital_time, 'HH12:MI:SS AM') as \"vitalTime\" ,"
						                + " to_char(vital.created_date, 'DD-MM-YYYY') as \"createdDate\" "
										+ " from public.t_vital vital where "
									+ " vital.organisation_id = :orgId "
									+ " and vital.unit_id =:unitId "
									+ " and vital.patient_id =:patientid";
			
			String query1 = 		 	
					" select details.vital_id as \"vitalId\","
							+" details.t_vital_id as \"tVitalId\","
							+ " to_char(vital.vital_time, 'HH12:MI:SS AM') as \"vitalTime\" ,"
				            + " to_char(vital.created_date, 'DD-MM-YYYY') as \"createdDate\", "
							+" details.t_vital_details_id as \"tVitalDetailsId\","
			                +" details.observation as \"observation\" "
						    +" from public.t_vital_details details "
						    + " inner join public.t_vital vital on details.t_vital_id = vital.t_vital_id where "
						    +" details.t_vital_id = :tVitalId ";

			StringBuilder q1 = new StringBuilder(query);
			if (dto.getFromDate() != null && dto.getToDate() != null && !dto.getFromDate().isEmpty() && !dto.getToDate().isEmpty()) {
				q1.append(" AND to_date(to_char(vital.created_date,'DD-MM-YYYY'),'DD-MM-YYYY') " +
						"BETWEEN to_date(:fromDate, 'DD-MM-YYYY') AND to_date(:toDate, 'DD-MM-YYYY')");
			}
			if (dto.getFromTime() != null && dto.getToTime() != null && !dto.getFromTime().isEmpty() && !dto.getToTime().isEmpty()) {
				q1.append(" AND vital.vital_time BETWEEN :fromTime AND :toTime");
			}
			q1.append(" ORDER BY vital.created_date DESC, vital.vital_time DESC");
			Query queryObj = sessionFactory.getCurrentSession().createSQLQuery(q1.toString())
					.setParameter("orgId", dto.getOrganisationId())
					.setParameter("unitId", dto.getUnitId())
					.setParameter("patientId", dto.getPatientId());
			if (dto.getFromDate() != null && dto.getToDate() != null && !dto.getFromDate().isEmpty() && !dto.getToDate().isEmpty()) {
				queryObj.setParameter("fromDate", dto.getFromDate());
				queryObj.setParameter("toDate", dto.getToDate());
			}
			if (dto.getFromTime() != null && dto.getToTime() != null && !dto.getFromTime().isEmpty() && !dto.getToTime().isEmpty()) {
				queryObj.setParameter("fromTime", dto.getFromTime());
				queryObj.setParameter("toTime", dto.getToTime());
			}
			List<TVitalDto> VitalMasterDtoList = queryObj.setResultTransformer(Transformers.aliasToBean(TVitalDto.class))
					.list();
			for (Iterator iterator = VitalMasterDtoList.iterator(); iterator.hasNext();) {
				TVitalDto tVitalDto = (TVitalDto) iterator.next();
				List<TVitalDetailsDto> tVitalDetailsDtoList = sessionFactory.getCurrentSession()
						.createSQLQuery(query1)
						.setParameter("tVitalId", tVitalDto.gettVitalId())
						.setResultTransformer(Transformers.aliasToBean(TVitalDetailsDto.class)).list();
				tVitalDto.setVitalList(tVitalDetailsDtoList);
				
			}
			return new Response(SUCCESS, null, null, VitalMasterDtoList, null);

		} catch (HibernateException he) {
		logger.error("getVitalsByPatient failed", he);
		} catch (Exception e) {
		logger.error("getVitalsByPatient failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	

}
