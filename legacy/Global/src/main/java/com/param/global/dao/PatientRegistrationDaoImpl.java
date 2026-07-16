package com.param.global.dao;

import java.math.BigInteger;
import java.net.URI;
import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.transform.Transformers;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;


import com.param.global.billing.dto.UrlConfigurationDto;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.ISmsUtilityConstants;
import com.param.global.common.Response;
import com.param.global.dto.CustomPatientDetailsDto;
import com.param.global.dto.NewONPRegistrationDto;
import com.param.global.dto.PatientDetailsDto;
import com.param.global.dto.PatientRegistrationDocDetailsDto;
import com.param.global.dto.PatientRegistrationDto;
import com.param.global.dto.PatientSearchBillRes;
import com.param.global.dto.PatientVisitsDto;
import com.param.global.dto.SlotMasterDto;
import com.param.global.dto.sync.PatientDocumentSyncDto;
import com.param.global.dto.sync.PatientSyncDto;
import com.param.global.dto.sync.ScheduleAppointmentReqDto;
import com.param.global.model.FingerPrintInterFace;
import com.param.global.model.PatientDetails;
import com.param.global.model.PatientRegistration;
import com.param.global.model.PatientRegistrationDocDetails;
import com.param.global.model.PatientRegistrationLog;
import com.param.global.unit.dto.AllergyPatientMapperDto;
import com.param.global.unit.dto.SmsUtilityConfigInfoDto;
import com.param.global.unit.dto.whatsAppRequestDto;
import com.param.global.unit.service.ISmsUtilityConfigService;
import com.param.opd.coversheet.model.EncounterDetails;
import com.param.opd.encounter.dto.EncounterDetailsDto;
import com.param.opd.encounter.dto.EncounterDocDetailsDto;
import com.param.opd.encounter.dto.PatientDocDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.io.IOException;
import java.sql.Statement;
import okhttp3.*;
import in.param.exception.ApplicationException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({ "unchecked", "rawtypes" })
public class PatientRegistrationDaoImpl extends GenericDao<PatientRegistration, Integer> implements IPatientRegistrationDao, ICommonConstants{
	
	final static Logger logger = Logger.getLogger(PatientRegistrationDaoImpl.class);
	
	public PatientRegistrationDaoImpl() {
		super(PatientRegistration.class);
	}

	@Autowired
	DozerBeanMapper mapper;
	
	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	HikariDataSource driverManager;
	
	@Autowired
	private  RestTemplate restTemplate;
	
	@Autowired
	private ISmsUtilityConfigService smsUtilityService;
	
	@Override
	public Response savePatientRegistration(PatientRegistrationDto patientRegistrationDto) throws ApplicationException {
	try{	
		Session session=sessionFactory.getCurrentSession();
			PatientRegistration patientRegistration = mapper.map(patientRegistrationDto,PatientRegistration.class,
						"PatientRegistrationDto_to_PatientRegistration");
				patientRegistration.setCreatedDate(TimeZoneComponent.getDateByzone(patientRegistrationDto.getUnitId()));
				patientRegistration.setUpdatedDate(TimeZoneComponent.getDateByzone(patientRegistrationDto.getUnitId()));
				if(patientRegistrationDto.getDob()!= null) {
					
					patientRegistration.setDob(GlobalCommonDateUtils.getDate(patientRegistrationDto.getDob(), "dd-MM-yyyy"));
					
				}else {
					if(patientRegistrationDto.getAge()!=null)
					{
						Integer year = Calendar.getInstance().get(Calendar.YEAR);
						Integer month = Calendar.getInstance().get(Calendar.MONTH);
						Integer day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
						Integer newYear = year  - (Integer.parseInt(patientRegistrationDto.getAge()));
						String Dob = day.toString()+ "-"+month.toString()+"-"+newYear.toString();
						patientRegistration.setDob(GlobalCommonDateUtils.getDate(Dob, "dd-MM-yyyy"));
					}
					
				}
				ObjectMapper objectMapper = new ObjectMapper();
				String json = objectMapper.writeValueAsString(patientRegistration);
				System.out.println(json);

				patientRegistration.setIsBlacklist(patientRegistration.getIsBlacklist() == '\u0000' ? 'N':patientRegistration.getIsBlacklist());
				patientRegistration.setIsOtcReg(patientRegistration.getIsOtcReg() == '\u0000' ? 'N':patientRegistration.getIsOtcReg());
				patientRegistration.setIsPreReg(patientRegistration.getIsPreReg() == '\u0000' ? 'N':patientRegistration.getIsPreReg());
				patientRegistration.setIsUnknownReg(patientRegistration.getIsUnknownReg() == '\u0000' ? 'N':patientRegistration.getIsUnknownReg());
				patientRegistration.setStatus('A');
			patientRegistrationDto.setPatientId((Integer) session.save(patientRegistration));
		return new Response(SUCCESS, null, null, null, patientRegistrationDto);
	}catch (HibernateException he) {
	logger.error("savePatientRegistration failed", he);
	} catch (Exception e) {
	logger.error("savePatientRegistration failed", e);
	}
	return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response savePatientDetails(PatientRegistrationDto patientRegistrationDto) throws ApplicationException {
		try{	
			Session session=sessionFactory.getCurrentSession();
			//Transaction tr=session.beginTransaction();
				PatientDetails patientDetails = mapper.map(patientRegistrationDto,PatientDetails.class,
						"PatientRegistrationDto_to_PatientDetails");

			patientDetails.setStatus('A');
			session.save(patientDetails);
			//tr.commit();
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		}catch (HibernateException he) {
		logger.error("savePatientDetails failed", he);
		} catch (Exception e) {
		logger.error("savePatientDetails failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	@Transactional
	public Response getPatientDetailsById(Integer patientId) throws ApplicationException {
		try {
			PatientDetailsDto patientDetailsDto = (PatientDetailsDto) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PATIENT_DETAILS_BY_ID")
					.setParameter("patientId", patientId)
					.setResultTransformer(Transformers.aliasToBean(PatientDetailsDto.class))
					.uniqueResult();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, patientDetailsDto);
		}catch (HibernateException he) {
		throw new ApplicationException(he.getMessage(), he);
	} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	public Response getPatientDetais(PatientRegistrationDto patientRegistrationDto) throws ApplicationException {
		try {
			List<PatientRegistrationDto> getPatientDetaisList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PATIENT_DETAILS")
					.setParameter("orgId", patientRegistrationDto.getOrganizationId())
					//.setParameter("unitId", patientRegistrationDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(PatientRegistrationDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, getPatientDetaisList, null);
		}catch (HibernateException he) {
		logger.error("getPatientDetais failed", he);
		} catch (Exception e) {
		logger.error("getPatientDetais failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response checkUniquePatient(PatientRegistrationDto patientRegistrationDto)
			throws ApplicationException {
		try {
			List<PatientDetailsDto> getPatientDetaisList;
			if(patientRegistrationDto.getRegistrationTypeId()==5)
			{
				getPatientDetaisList = sessionFactory.getCurrentSession()
						.getNamedQuery("CHECK_UNIQUE_PATIENT")
						.setParameter("orgId", patientRegistrationDto.getOrganizationId())
						//.setParameter("unitId", patientRegistrationDto.getUnitId())
						.setParameter("genderId", patientRegistrationDto.getGenderId())
						.setParameter("dob", patientRegistrationDto.getDob().substring(0, 10))
						.setParameter("mobileNumber", patientRegistrationDto.getMobileNumber())
						.setResultTransformer(Transformers.aliasToBean(PatientDetailsDto.class)).list();
				if(getPatientDetaisList.size()>0)
					return new Response<>(SUCCESS, ALREADY_EXIST_CODE, "Patient already exist...!!", getPatientDetaisList, null);
				else
					return new Response<>(SUCCESS, SUCCESS_CODE, null, getPatientDetaisList, null);
			}
			
			//<-----If the patient is converting from OTC to normal the condition for checking unique patient will be slight different 
			if(patientRegistrationDto.getRegistrationTypeId()==3)
			{
				
				StringBuilder finalQuery=new StringBuilder(sessionFactory.getCurrentSession()
						.getNamedQuery("CHECK_UNIQUE_PATIENT").getQueryString());
				finalQuery.append(" AND pt.patientId!="+patientRegistrationDto.getPreviousId());
				
				Query query=sessionFactory.getCurrentSession().createQuery(finalQuery.toString());
				getPatientDetaisList = query
						.setParameter("orgId", patientRegistrationDto.getOrganizationId())
						//.setParameter("unitId", patientRegistrationDto.getUnitId())
						.setParameter("genderId", patientRegistrationDto.getGenderId())
						.setParameter("dob", patientRegistrationDto.getDob().substring(0, 10))
						.setParameter("mobileNumber", patientRegistrationDto.getMobileNumber())
						.setResultTransformer(Transformers.aliasToBean(PatientDetailsDto.class)).list();
			}
			//----->
			//<----- For updating the patient record
			else if(patientRegistrationDto.getPatientId()!=null && patientRegistrationDto.getPatientId()>0)
			{
				Session session=sessionFactory.getCurrentSession();
					StringBuilder finalQuery=new StringBuilder(session
						.getNamedQuery("CHECK_UNIQUE_PATIENT").getQueryString());
				
					finalQuery.append(" AND pt.patientId!="+patientRegistrationDto.getPatientId());
				
				Query query=sessionFactory.getCurrentSession().createQuery(finalQuery.toString());
				getPatientDetaisList = query
						.setParameter("orgId", patientRegistrationDto.getOrganizationId())
						//.setParameter("unitId", patientRegistrationDto.getUnitId())
						.setParameter("genderId", patientRegistrationDto.getGenderId())
						.setParameter("dob", patientRegistrationDto.getDob().substring(0, 10))
						.setParameter("mobileNumber", patientRegistrationDto.getMobileNumber())
						.setResultTransformer(Transformers.aliasToBean(PatientDetailsDto.class)).list();
			}
			//----->
			else{
			
			getPatientDetaisList = sessionFactory.getCurrentSession()
					.getNamedQuery("CHECK_UNIQUE_PATIENT")
					.setParameter("orgId", patientRegistrationDto.getOrganizationId())
					//.setParameter("unitId", patientRegistrationDto.getUnitId())
					.setParameter("genderId", patientRegistrationDto.getGenderId())
					.setParameter("dob", patientRegistrationDto.getDob().substring(0, 10))
					.setParameter("mobileNumber", patientRegistrationDto.getMobileNumber().trim())
					.setResultTransformer(Transformers.aliasToBean(PatientDetailsDto.class)).list();
			}
			
			if(patientRegistrationDto.getHealthId() != null && patientRegistrationDto.getHealthNumber() != null && getPatientDetaisList.size() >0) {
				PatientDetailsDto alreadyExists = getPatientDetaisList.getFirst();
		         Session sessiona=sessionFactory.openSession();
					String querya="UPDATE PatientRegistration "
								+ " SET healthId ='"+patientRegistrationDto.getHealthId()+"', "
								+ "  healthNumber ='"+patientRegistrationDto.getHealthNumber()+"' "
								+ " WHERE patientId=:patientId";
						         sessionFactory.getCurrentSession().createQuery(querya)
								.setInteger("patientId", alreadyExists.getPatientId())
								.executeUpdate();
		         }
			return new Response<>(SUCCESS, SUCCESS_CODE, null, getPatientDetaisList, null);
		}catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			throw e;
		}
	//	return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Long getNextValPatientId() throws ApplicationException {
		try {
			Session session = sessionFactory.getCurrentSession();
				SQLQuery query = session.createSQLQuery("select nextval('patient.patient_registration_seq')");
				Long id = ((BigInteger) query.uniqueResult()).longValue();

				SQLQuery query2 = session.createSQLQuery("select setval('patient.patient_registration_seq'," + (id-1) + ")");
				query2.uniqueResult();

				return id;
		} catch (HibernateException he) {
		logger.error("getNextValPatientId failed", he);
		} catch (Exception e) {
		logger.error("getNextValPatientId failed", e);
		}
		return null;
	}

	@Override
	public Response patientSearchOTC(PatientRegistrationDto patientRegistrationDto) throws ApplicationException {
		try {
			Session session = sessionFactory.getCurrentSession();
			StringBuilder stringQuery = new StringBuilder(session.getNamedQuery("GET_PATIENT_DATA").getQueryString());
			stringQuery.append(" AND patReg.registration_type_id = :registrationTypeId");

			StringBuilder resQuery = this.generateQueryForPatientSearch(stringQuery, patientRegistrationDto);
			Query actualQuery = session.createSQLQuery(resQuery.toString());
			if (patientRegistrationDto.getOffset() != null && patientRegistrationDto.getNoOfRecordsPerPage() != null) {
				actualQuery.setFirstResult(patientRegistrationDto.getOffset());
				actualQuery.setMaxResults(patientRegistrationDto.getNoOfRecordsPerPage());
			}
			if (patientRegistrationDto.getUhidNumber() != null && !patientRegistrationDto.getUhidNumber().isEmpty()) {
				actualQuery.setParameter("uhidNumber", "%" + patientRegistrationDto.getUhidNumber().toLowerCase() + "%");
			}

			if (patientRegistrationDto.getPatientName() != null && !patientRegistrationDto.getPatientName().isEmpty()) {
				actualQuery.setParameter("patientName", "%" + patientRegistrationDto.getPatientName().toLowerCase() + "%");
			}

			if (patientRegistrationDto.getGenderId() != null) {
				actualQuery.setParameter("genderId", patientRegistrationDto.getGenderId());
			}

			if (patientRegistrationDto.getMobileNumber() != null && !patientRegistrationDto.getMobileNumber().isEmpty()) {
				actualQuery.setParameter("mobileNumber", patientRegistrationDto.getMobileNumber());
			}

			if (patientRegistrationDto.getDob() != null && !patientRegistrationDto.getDob().isEmpty()) {
				actualQuery.setParameter("dob", String.valueOf(GlobalCommonDateUtils.getDate(patientRegistrationDto.getDob(), "dd-MM-yyyy")));
			}

			if (patientRegistrationDto.getIdentificationNumber() != null && !patientRegistrationDto.getIdentificationNumber().isEmpty()) {
				actualQuery.setParameter("identificationNumber", "%" + patientRegistrationDto.getIdentificationNumber().toLowerCase() + "%");
			}

			if (patientRegistrationDto.getIdentificationTypeId() != null) {
				actualQuery.setParameter("identificationTypeId", String.valueOf(patientRegistrationDto.getIdentificationTypeId()));
			}
			actualQuery.setParameter("orgId", patientRegistrationDto.getOrganizationId())
					.setParameter("unitId", patientRegistrationDto.getUnitId())
					.setParameter("registrationTypeId", patientRegistrationDto.getRegistrationTypeId()) // Set the parameter for registrationTypeId
					.setResultTransformer(Transformers.aliasToBean(PatientRegistrationDto.class));
			List<PatientRegistrationDto> unknownPatientRegistrationDtosList = actualQuery.list();
			return new Response(SUCCESS, null, null, unknownPatientRegistrationDtosList, null);

		} catch (HibernateException he) {
		logger.error("patientSearchOTC failed", he);
		} catch (Exception e) {
		logger.error("patientSearchOTC failed", e);
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response patientSearchER(PatientRegistrationDto patientRegistrationDto) throws ApplicationException {
		try {
			Session session = sessionFactory.getCurrentSession();
			StringBuilder stringQuery = new StringBuilder(session.getNamedQuery("GET_PATIENT_DATA").getQueryString());
			stringQuery.append(" AND patReg.registration_type_id = :registrationTypeId");
			StringBuilder resQuery = this.generateQueryForPatientSearch(stringQuery, patientRegistrationDto);
			Query actualQuery = session.createSQLQuery(resQuery.toString());
			if (patientRegistrationDto.getOffset() != null && patientRegistrationDto.getNoOfRecordsPerPage() != null) {
				actualQuery.setFirstResult(patientRegistrationDto.getOffset());
				actualQuery.setMaxResults(patientRegistrationDto.getNoOfRecordsPerPage());
			}

			if (patientRegistrationDto.getUhidNumber() != null && !patientRegistrationDto.getUhidNumber().isEmpty()) {
				actualQuery.setParameter("uhidNumber", "%" + patientRegistrationDto.getUhidNumber().toLowerCase() + "%");
			}

			if (patientRegistrationDto.getPatientName() != null && !patientRegistrationDto.getPatientName().isEmpty()) {
				actualQuery.setParameter("patientName", "%" + patientRegistrationDto.getPatientName().toLowerCase() + "%");
			}

			if (patientRegistrationDto.getGenderId() != null) {
				actualQuery.setParameter("genderId", patientRegistrationDto.getGenderId());
			}

			if (patientRegistrationDto.getMobileNumber() != null && !patientRegistrationDto.getMobileNumber().isEmpty()) {
				actualQuery.setParameter("mobileNumber", patientRegistrationDto.getMobileNumber());
			}

			if (patientRegistrationDto.getDob() != null && !patientRegistrationDto.getDob().isEmpty()) {
				actualQuery.setParameter("dob", String.valueOf(GlobalCommonDateUtils.getDate(patientRegistrationDto.getDob(), "dd-MM-yyyy")));
			}

			if (patientRegistrationDto.getIdentificationNumber() != null && !patientRegistrationDto.getIdentificationNumber().isEmpty()) {
				actualQuery.setParameter("identificationNumber", "%" + patientRegistrationDto.getIdentificationNumber().toLowerCase() + "%");
			}

			if (patientRegistrationDto.getIdentificationTypeId() != null) {
				actualQuery.setParameter("identificationTypeId", String.valueOf(patientRegistrationDto.getIdentificationTypeId()));
			}
			actualQuery.setParameter("orgId", patientRegistrationDto.getOrganizationId())
					.setParameter("unitId", patientRegistrationDto.getUnitId())
					.setParameter("registrationTypeId", patientRegistrationDto.getRegistrationTypeId()) // Use parameterized query
					.setResultTransformer(Transformers.aliasToBean(PatientRegistrationDto.class));
			List<PatientRegistrationDto> unknownPatientRegistrationDtosList = actualQuery.list();
			return new Response(SUCCESS, null, null, unknownPatientRegistrationDtosList, null);

		} catch (HibernateException he) {
		logger.error("patientSearchER failed", he);
		} catch (Exception e) {
		logger.error("patientSearchER failed", e);
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response patientSearchNormal(PatientRegistrationDto patientRegistrationDto) throws ApplicationException {
		try {
			Session session = sessionFactory.getCurrentSession();
			StringBuilder stringQuery=new StringBuilder(session.getNamedQuery("GET_PATIENT_DATA").getQueryString());
			stringQuery.append(" AND patReg.registration_type_id IN (1) ");
			
			StringBuilder resQuery = this.generateQueryForPatientSearch(stringQuery,patientRegistrationDto);
			Query actualQuery = session.createSQLQuery(resQuery.toString());
            if(patientRegistrationDto.getOffset()!=null && patientRegistrationDto.getNoOfRecordsPerPage()!=null)
            {
                actualQuery.setFirstResult(patientRegistrationDto.getOffset());
                actualQuery.setMaxResults(patientRegistrationDto.getNoOfRecordsPerPage());
            }

			if (patientRegistrationDto.getUhidNumber() != null && !patientRegistrationDto.getUhidNumber().isEmpty()) {
				actualQuery.setParameter("uhidNumber", "%" + patientRegistrationDto.getUhidNumber().toLowerCase() + "%");
			}

			if (patientRegistrationDto.getPatientName() != null && !patientRegistrationDto.getPatientName().isEmpty()) {
				actualQuery.setParameter("patientName", "%" + patientRegistrationDto.getPatientName().toLowerCase() + "%");
			}

			if (patientRegistrationDto.getGenderId() != null) {
				actualQuery.setParameter("genderId", patientRegistrationDto.getGenderId());
			}

			if (patientRegistrationDto.getMobileNumber() != null && !patientRegistrationDto.getMobileNumber().isEmpty()) {
				actualQuery.setParameter("mobileNumber", patientRegistrationDto.getMobileNumber());
			}

			if (patientRegistrationDto.getDob() != null && !patientRegistrationDto.getDob().isEmpty()) {
				actualQuery.setParameter("dob", String.valueOf(GlobalCommonDateUtils.getDate(patientRegistrationDto.getDob(), "dd-MM-yyyy")));
			}

			if (patientRegistrationDto.getIdentificationNumber() != null && !patientRegistrationDto.getIdentificationNumber().isEmpty()) {
				actualQuery.setParameter("identificationNumber", "%" + patientRegistrationDto.getIdentificationNumber().toLowerCase() + "%");
			}

			if (patientRegistrationDto.getIdentificationTypeId() != null) {
				actualQuery.setParameter("identificationTypeId", String.valueOf(patientRegistrationDto.getIdentificationTypeId()));
			}
			List<PatientRegistrationDto> patientRegistrationsList = actualQuery
					.setParameter("orgId", patientRegistrationDto.getOrganizationId())
					.setParameter("unitId", patientRegistrationDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(PatientRegistrationDto.class)).list();
			return new Response(SUCCESS, null, null, patientRegistrationsList, null);

		} catch (HibernateException he) {
            throw he;
		} catch (Exception e) {
			 throw e;
		}
		//return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}
	
	private StringBuilder generateQueryForPatientSearch(StringBuilder stringQuery,
			PatientRegistrationDto patientRegistrationDto) {
		try{
			if (patientRegistrationDto.getUhidNumber() != null && !patientRegistrationDto.getUhidNumber().isEmpty()) {
				stringQuery.append(" AND lower(patReg.uhid_number) LIKE :uhidNumber ");
			}

			if (patientRegistrationDto.getPatientName() != null && !patientRegistrationDto.getPatientName().isEmpty()) {
				stringQuery.append(" AND ( lower(concat(coalesce(patReg.first_name), coalesce(patReg.last_name, ''))) LIKE :patientName "
						+ "OR lower(concat(coalesce(patReg.first_name), ' ', coalesce(patReg.last_name, ''))) LIKE :patientName )");
			}

			if (patientRegistrationDto.getGenderId() != null) {
				stringQuery.append(" AND patReg.gender_id = :genderId ");
			}

			if (patientRegistrationDto.getMobileNumber() != null && !patientRegistrationDto.getMobileNumber().isEmpty()) {
				stringQuery.append(" AND patReg.mobile_number = :mobileNumber ");
			}

			if (patientRegistrationDto.getDob() != null && !patientRegistrationDto.getDob().isEmpty()) {
				stringQuery.append(" AND patReg.dob = :dob ");
			}

			if (patientRegistrationDto.getIdentificationNumber() != null && !patientRegistrationDto.getIdentificationNumber().isEmpty()) {
				stringQuery.append(" AND lower(patReg.identification_number) LIKE :identificationNumber ");
			}

			if (patientRegistrationDto.getIdentificationTypeId() != null) {
				stringQuery.append(" AND patReg.identification_type_id = :identificationTypeId ");
			}

			if (patientRegistrationDto.getIsFromPatientList() == null) {
				stringQuery.append(" AND patReg.status = 'A' ");
			}

			stringQuery.append(" ORDER BY patReg.registration_type_id, patReg.first_name ");

			return stringQuery;





		}catch(Exception e){
		logger.error("generateQueryForPatientSearch failed", e);
		}
		return stringQuery;
	}
	
	@Override
	public Response patientSearchNormalByPatientId(Integer patientId,Integer orgId,Integer unitId) throws ApplicationException {
		try {
			Session session = sessionFactory.getCurrentSession();
			StringBuilder stringQuery=new StringBuilder(session.getNamedQuery("GET_PATIENT_DATA_FOR_UPDATE").getQueryString());
			//stringQuery.append(" AND patient.registrationTypeId!=2");
			stringQuery.append(" AND patient.patientId="+patientId);
			Query actualQuery = session.createQuery(stringQuery.toString());
			List<PatientRegistrationDto> patientRegistrationsList = actualQuery
					.setParameter("orgId", orgId)
					//.setParameter("unitId",unitId)
					.setResultTransformer(Transformers.aliasToBean(PatientRegistrationDto.class)).list();
			return new Response(SUCCESS, null, null, patientRegistrationsList, null);

		} catch (HibernateException he) {
		logger.error("patientSearchNormalByPatientId failed", he);
		} catch (Exception e) {
		logger.error("patientSearchNormalByPatientId failed", e);
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updatePatient(PatientRegistrationDto patientRegistrationDto) throws ApplicationException {
		try{
      Session session=sessionFactory.getCurrentSession();
			
			PatientRegistration patientRegistration = mapper.map(patientRegistrationDto,PatientRegistration.class,
					"PatientRegistrationDto_to_PatientRegistration");
			patientRegistration.setIsBlacklist(patientRegistration.getIsBlacklist() == '\u0000' ? 'N':patientRegistration.getIsBlacklist());
			patientRegistration.setIsOtcReg(patientRegistration.getIsOtcReg() == '\u0000' ? 'N':patientRegistration.getIsOtcReg());
			patientRegistration.setIsPreReg(patientRegistration.getIsPreReg() == '\u0000' ? 'N':patientRegistration.getIsPreReg());
			patientRegistration.setIsUnknownReg(patientRegistration.getIsUnknownReg() == '\u0000' ? 'N':patientRegistration.getIsUnknownReg());
			patientRegistration.setStatus('A');
			session.update(patientRegistration);

			if((patientRegistrationDto.getPatientDetailsId() == null) && (patientRegistrationDto.getPatientId()!= null && patientRegistrationDto.getPatientId() > 0)){
				String sql = "select patient_details_id from patient.t_patient_details where patient_id = " +patientRegistrationDto.getPatientId()
						+ " order by patient_details_id desc limit 1";
				Integer detailId = (Integer) sessionFactory.getCurrentSession().createSQLQuery(sql).uniqueResult();

				if(detailId != null && detailId != 0){
					patientRegistrationDto.setPatientDetailsId(detailId);
				}

			}


			PatientDetails patientDetails = mapper.map(patientRegistrationDto,PatientDetails.class,

						"PatientRegistrationDto_to_PatientDetails");
			patientDetails.setPatientDetailsId(patientRegistrationDto.getPatientDetailsId());
			patientDetails.setStatus('A');
			session.update(patientDetails);
			
			
		return new Response(SUCCESS, patientRegistration.getUhidNumber(), COMMON_UPDATE, null, patientRegistration.getPatientId());
		} catch (HibernateException he) {
          throw he;
		} catch (Exception e) {
			throw e;
		}
		//return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response savePatientRegistrationLog(PatientRegistrationDto patientRegistrationDto) throws ApplicationException {
		try {
			PatientRegistrationLog patientRegistrationLog = mapper.map(patientRegistrationDto,
					PatientRegistrationLog.class, "PatientRegistrationDto_to_PatientRegistrationLog");
			patientRegistrationLog.setIsBlacklist(patientRegistrationLog.getIsBlacklist() == '\u0000' ? 'N':patientRegistrationLog.getIsBlacklist());
			patientRegistrationLog.setIsOtcReg(patientRegistrationLog.getIsOtcReg() == '\u0000' ? 'N':patientRegistrationLog.getIsOtcReg());
			patientRegistrationLog.setIsPreReg(patientRegistrationLog.getIsPreReg() == '\u0000' ? 'N':patientRegistrationLog.getIsPreReg());
			patientRegistrationLog.setIsUnknownReg(patientRegistrationLog.getIsUnknownReg() == '\u0000' ? 'N':patientRegistrationLog.getIsUnknownReg());
			sessionFactory.getCurrentSession().save(patientRegistrationLog);

			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		} catch (HibernateException he) {
		logger.error("savePatientRegistrationLog failed", he);
		} catch (Exception e) {
		logger.error("savePatientRegistrationLog failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response patientDetailsByIdForBilling(int patientId) throws ApplicationException {
		try{
			List<PatientSearchBillRes> listPatientSearchBillRes = sessionFactory.getCurrentSession().getNamedQuery("GET_PATIENT_DETAILS_BY_ID_FOR_BILLING_SEARCH").setParameter("patientId", patientId).setResultTransformer(Transformers.aliasToBean(PatientSearchBillRes.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listPatientSearchBillRes, null);
		}catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Transactional
	public Response getPatientByIdForSync(Integer recordId) throws ApplicationException {
		try{
			PatientSyncDto dto = (PatientSyncDto)sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PATIENT_DETAILS_TO_SYNC_BY_ID")
					.setParameter("id",recordId)
					.setResultTransformer(Transformers.aliasToBean(PatientSyncDto.class)).uniqueResult();
			if(dto.getCode()==null){
				dto.setCode("+91");
			}
			return new Response(SUCCESS,SUCCESS_CODE,null,null,dto);
		}catch(HibernateException he){
		throw new ApplicationException(he.getMessage(), he);
	}catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
		
}

	@Override
	public Response patientSearchPre(PatientRegistrationDto patientRegistrationDto) throws ApplicationException {
		try {
			Session session = sessionFactory.getCurrentSession();
			StringBuilder stringQuery=new StringBuilder(session.getNamedQuery("GET_PATIENT_DATA").getQueryString());
			stringQuery.append(" AND patReg.registration_type_id="+patientRegistrationDto.getRegistrationTypeId());
		//	stringQuery.append(" ORDER BY patient.firstName ");
			StringBuilder resQuery = this.generateQueryForPatientSearch(stringQuery,patientRegistrationDto);
			Query actualQuery = session.createSQLQuery(resQuery.toString());
			if(patientRegistrationDto.getOffset()!=null && patientRegistrationDto.getNoOfRecordsPerPage()!=null)
            {
                actualQuery.setFirstResult(patientRegistrationDto.getOffset());
                actualQuery.setMaxResults(patientRegistrationDto.getNoOfRecordsPerPage());
            }

			if (patientRegistrationDto.getUhidNumber() != null && !patientRegistrationDto.getUhidNumber().isEmpty()) {
				actualQuery.setParameter("uhidNumber", "%" + patientRegistrationDto.getUhidNumber().toLowerCase() + "%");
			}

			if (patientRegistrationDto.getPatientName() != null && !patientRegistrationDto.getPatientName().isEmpty()) {
				actualQuery.setParameter("patientName", "%" + patientRegistrationDto.getPatientName().toLowerCase() + "%");
			}

			if (patientRegistrationDto.getGenderId() != null) {
				actualQuery.setParameter("genderId", patientRegistrationDto.getGenderId());
			}

			if (patientRegistrationDto.getMobileNumber() != null && !patientRegistrationDto.getMobileNumber().isEmpty()) {
				actualQuery.setParameter("mobileNumber", patientRegistrationDto.getMobileNumber());
			}

			if (patientRegistrationDto.getDob() != null && !patientRegistrationDto.getDob().isEmpty()) {
				actualQuery.setParameter("dob", String.valueOf(GlobalCommonDateUtils.getDate(patientRegistrationDto.getDob(), "dd-MM-yyyy")));
			}

			if (patientRegistrationDto.getIdentificationNumber() != null && !patientRegistrationDto.getIdentificationNumber().isEmpty()) {
				actualQuery.setParameter("identificationNumber", "%" + patientRegistrationDto.getIdentificationNumber().toLowerCase() + "%");
			}

			if (patientRegistrationDto.getIdentificationTypeId() != null) {
				actualQuery.setParameter("identificationTypeId", String.valueOf(patientRegistrationDto.getIdentificationTypeId()));
			}


			List<PatientRegistrationDto> unknownPatientRegistrationDtosList = actualQuery
					.setParameter("orgId", patientRegistrationDto.getOrganizationId())
					.setParameter("unitId", patientRegistrationDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(PatientRegistrationDto.class)).list();
			return new Response(SUCCESS, null, null, unknownPatientRegistrationDtosList, null);

		} catch (HibernateException he) {
		logger.error("patientSearchPre failed", he);
		} catch (Exception e) {
		logger.error("patientSearchPre failed", e);
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);

	}
	

  @Override
  public Response updatePatientImagePath(PatientRegistrationDto patientRegistrationDto)
      throws ApplicationException {
    try{
      Integer result = sessionFactory.getCurrentSession().getNamedQuery("UPDATE_PATIENT_IMAGE_PATH")
          .setParameter("orgId", patientRegistrationDto.getOrganizationId())
          .setBinary("image", patientRegistrationDto.getImage())
         // .setBytes("unitId", patientRegistrationDto.getImage())
          //.setParameter("unitId", patientRegistrationDto.getUnitId())
          .setParameter("patientId", patientRegistrationDto.getPatientId())
          .setParameter("imagePath", patientRegistrationDto.getImagePath()).executeUpdate();
      if(result != null){
          return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
      }
              
  }catch(HibernateException he){
  logger.error("updatePatientImagePath failed", he);
  }catch(Exception e){
  logger.error("updatePatientImagePath failed", e);
  }
  return new Response<>(ERROR, ERROR_CODE, null, null, null);
  }

@Override
public Response patientSearchByRegistrationTypeIdList(PatientRegistrationDto patientRegistrationDto) {
	try {
		Session session = sessionFactory.getCurrentSession();
		StringBuilder stringQuery=new StringBuilder(session.getNamedQuery("GET_PATIENT_DATA").getQueryString());
		stringQuery.append(" AND patReg.registration_type_id IN :registrationTypeIdList");
		//stringQuery.append(" ORDER BY patient.registrationTypeId , patient.firstName ");
		StringBuilder resQuery = this.generateQueryForPatientSearch(stringQuery,patientRegistrationDto);
		Query actualQuery = session.createSQLQuery(resQuery.toString());
		if(patientRegistrationDto.getOffset()!=null && patientRegistrationDto.getNoOfRecordsPerPage()!=null)
        {
            actualQuery.setFirstResult(patientRegistrationDto.getOffset());
            actualQuery.setMaxResults(patientRegistrationDto.getNoOfRecordsPerPage());
        }
		if (patientRegistrationDto.getUhidNumber() != null && !patientRegistrationDto.getUhidNumber().isEmpty()) {
			actualQuery.setParameter("uhidNumber", "%" + patientRegistrationDto.getUhidNumber().toLowerCase() + "%");
		}

		if (patientRegistrationDto.getPatientName() != null && !patientRegistrationDto.getPatientName().isEmpty()) {
			actualQuery.setParameter("patientName", "%" + patientRegistrationDto.getPatientName().toLowerCase() + "%");
		}

		if (patientRegistrationDto.getGenderId() != null) {
			actualQuery.setParameter("genderId", patientRegistrationDto.getGenderId());
		}

		if (patientRegistrationDto.getMobileNumber() != null && !patientRegistrationDto.getMobileNumber().isEmpty()) {
			actualQuery.setParameter("mobileNumber", patientRegistrationDto.getMobileNumber());
		}

		if (patientRegistrationDto.getDob() != null && !patientRegistrationDto.getDob().isEmpty()) {
			actualQuery.setParameter("dob", String.valueOf(GlobalCommonDateUtils.getDate(patientRegistrationDto.getDob(), "dd-MM-yyyy")));
		}

		if (patientRegistrationDto.getIdentificationNumber() != null && !patientRegistrationDto.getIdentificationNumber().isEmpty()) {
			actualQuery.setParameter("identificationNumber", "%" + patientRegistrationDto.getIdentificationNumber().toLowerCase() + "%");
		}

		if (patientRegistrationDto.getIdentificationTypeId() != null) {
			actualQuery.setParameter("identificationTypeId", String.valueOf(patientRegistrationDto.getIdentificationTypeId()));
		}

		List<PatientRegistrationDto> unknownPatientRegistrationDtosList = actualQuery
				.setParameter("orgId", patientRegistrationDto.getOrganizationId())
				.setParameter("unitId", patientRegistrationDto.getUnitId())
				.setParameterList("registrationTypeIdList", patientRegistrationDto.getRegistrationTypeIdList())
				.setResultTransformer(Transformers.aliasToBean(PatientRegistrationDto.class)).list();
		return new Response(SUCCESS, null, null, unknownPatientRegistrationDtosList, null);

	} catch (HibernateException he) {
	logger.error("patientSearchByRegistrationTypeIdList failed", he);
	} catch (Exception e) {
	logger.error("patientSearchByRegistrationTypeIdList failed", e);
	}
	return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
}

@Override
public Response updateIsPrimaryFlag(Integer patientId) throws ApplicationException {
	try {
		
		sessionFactory.getCurrentSession().createQuery("UPDATE PatientRegistration set isPrimary='N' WHERE patientId="+patientId).executeUpdate();
		
		return new Response(SUCCESS, null, null, null, null);

	} catch (HibernateException he) {
	logger.error("updateIsPrimaryFlag failed", he);
	} catch (Exception e) {
	logger.error("updateIsPrimaryFlag failed", e);
	}
	return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
}


	@Override
	@Transactional
	public Response updateUuidByPatient(int patientId, String uuid) throws ApplicationException {
		try{
			int result = sessionFactory.getCurrentSession().getNamedQuery("UPDATE_UUID_BY_PATIENT").setParameter("id", patientId).setParameter("uuid", uuid).executeUpdate();
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, result);
		}catch(Exception e){
			
			throw e;
		}
	}

@Override
public Response getPatientDetaisForHealthCard(int patientId) throws ApplicationException {
	// TODO Auto-generated method stub
	try {
		PatientRegistrationDto patientRegistrationDto = (PatientRegistrationDto) sessionFactory.getCurrentSession()
				.getNamedQuery("GET_PATIENT_DETAILS_FOR_HEALTHCARD")
				.setParameter("patientId", patientId)
				.setResultTransformer(Transformers.aliasToBean(PatientRegistrationDto.class))
				.uniqueResult();
		return new Response<>(SUCCESS, SUCCESS_CODE, null, null, patientRegistrationDto);
	}catch (HibernateException he) {
	logger.error("getPatientDetaisForHealthCard failed", he);
	} catch (Exception e) {
	logger.error("getPatientDetaisForHealthCard failed", e);
	}
	return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
}

@Override
public Response getPatientAllergies(int patientId, int unitId, int organizationId) throws ApplicationException {
	try
	{
		StatelessSession statlessSession = sessionFactory.openStatelessSession();
		List<AllergyPatientMapperDto> listAllergyPatientMapperDto = statlessSession.getNamedQuery("GET_LIST_ALLERGY_PATIENT_MAPPER")
				.setParameter("patientId", patientId)
				.setParameter("unitId", unitId)
				.setParameter("organizationId", organizationId)
				.setDate("created_date", TimeZoneComponent.getDateByzone(unitId))
				.setResultTransformer(Transformers.aliasToBean(AllergyPatientMapperDto.class)).list();
		statlessSession.close();
		return new Response(SUCCESS, SUCCESS_CODE, null, listAllergyPatientMapperDto, null);
	}catch(HibernateException he) { 
		throw new HibernateException(he.getMessage());
	}catch(Exception e) {
	logger.error("getPatientAllergies failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
}

	@Override
	public Response patientAutofillSearch(SlotMasterDto slotMasterDto) throws ApplicationException {
		try {

			List<PatientRegistrationDto> patientRegistrationDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("PATIENT_AUTO_FILL_SEARCH")
					.setParameter("patientName", slotMasterDto.getPatientName().toLowerCase() + "%")
					.setParameter("organizationId", slotMasterDto.getOrganizationId())
					//.setParameter("unitId", slotMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(PatientRegistrationDto.class)).list();
			return new Response(SUCCESS, null, null, patientRegistrationDtosList, null);
		} catch (HibernateException he) {
		logger.error("patientAutofillSearch failed", he);
		} catch (Exception e) {
		logger.error("patientAutofillSearch failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}
	
	@Override
	public Response patientAutofillSearchPlain(SlotMasterDto slotMasterDto) throws ApplicationException {
		try {

			List<PatientRegistrationDto> patientRegistrationDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("PATIENT_AUTO_FILL_SEARCH_PLAIN")
					.setParameter("patientName", slotMasterDto.getPatientName().toLowerCase() + "%")
					.setParameter("organizationId", slotMasterDto.getOrganizationId())
					//.setParameter("unitId", slotMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(PatientRegistrationDto.class)).list();
			return new Response(SUCCESS, null, null, patientRegistrationDtosList, null);
		} catch (HibernateException he) {
		logger.error("patientAutofillSearchPlain failed", he);
		} catch (Exception e) {
		logger.error("patientAutofillSearchPlain failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response patientTypeHeadSearchForSelfRegistration(SlotMasterDto slotMasterDto) throws ApplicationException {
		try {

			StringBuilder stringQuery = null;
			List<PatientRegistrationDto> patientRegistrationDtosList = null;
			if(slotMasterDto.getIsFromKioskToken().equals('Y'))
				stringQuery=new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_PATIENT_DATA_FOR_SELF_REGISTRATION").getQueryString());
			else
				stringQuery=new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_PATIENT_DATA_FOR_SELF_PRE_BOOKED").getQueryString());
			
			
			stringQuery.append(slotMasterDto.getPatientName()!= null ? "AND (LOWER(patient.firstName) LIKE :patientName "
					+ "OR LOWER(patient.lastName) LIKE :patientName "
					+ "OR LOWER(patient.uhidNumber) LIKE :patientName "
					+ "OR LOWER(patient.mobileNumber) LIKE :patientName ) " : " ");
			
			
			if(slotMasterDto.getIsFromKioskToken().equals('Y')){
				patientRegistrationDtosList =sessionFactory.getCurrentSession().createQuery(stringQuery.toString())
						.setParameter("patientName", slotMasterDto.getPatientName().toLowerCase() + "%")
						.setParameter("orgId", slotMasterDto.getOrganizationId())
						//.setParameter("unitId", slotMasterDto.getUnitId())
						//.setDate("currentDate", TimeZoneComponent.getDateByzone(unitId))
						//.setParameterList("registrationTypeIdList", slotMasterDto.getRegistrationTypeIdList())
						.setResultTransformer(Transformers.aliasToBean(PatientRegistrationDto.class)).list();
			}
			else{
				patientRegistrationDtosList =sessionFactory.getCurrentSession().createQuery(stringQuery.toString())
						.setParameter("patientName", slotMasterDto.getPatientName().toLowerCase() + "%")
						.setParameter("orgId", slotMasterDto.getOrganizationId())
						.setParameter("unitId", slotMasterDto.getUnitId())
						.setDate("currentDate", TimeZoneComponent.getDateByzone(slotMasterDto.getUnitId()))
						//.setParameterList("registrationTypeIdList", slotMasterDto.getRegistrationTypeIdList())
						.setResultTransformer(Transformers.aliasToBean(PatientRegistrationDto.class)).list();
			}
			return new Response(SUCCESS, null, null, patientRegistrationDtosList, null);
		} catch (HibernateException he) {
		logger.error("patientTypeHeadSearchForSelfRegistration failed", he);
		} catch (Exception e) {
		logger.error("patientTypeHeadSearchForSelfRegistration failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response savePatientThumb(FingerPrintInterFace fingerInterface) throws ApplicationException {
		try {

		 Integer result = (Integer)sessionFactory.getCurrentSession().save(fingerInterface);
		 return new Response(SUCCESS, SUCCESS_CODE, null, null, result);
		} catch (HibernateException he) {
		logger.error("savePatientThumb failed", he);
		} catch (Exception e) {
		logger.error("savePatientThumb failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getPatientByuuid(List<Integer> uuids,int orgId,int unitId) throws ApplicationException 
	{
		try
		{
			List<Integer> patientIds= sessionFactory.getCurrentSession().createSQLQuery("select "
					+ "	patient_id "
					+ "from "
					+ "	patient.t_fingerprint_interface "
					+ "where "
					+ "	uuid in (:uuids) "
					+ "	and organisation_id = :orgId "
					+ "	and unit_id =:unitId")
					.setParameterList("uuids", uuids)
					.setParameter("orgId", orgId)
					.setParameter("unitId", unitId)
					.list();
			return new Response(SUCCESS, SUCCESS_CODE, null, patientIds, null);
		}catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("getPatientByuuid failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getPatientDetailsByuuid(String uuid) throws ApplicationException
	{
		try
		{
			String sql = "  select patient_id as \"patientId\" ,organization_id as  \"organizationId\" ,unit_id as \"unitId\"  " +
					" from patient.t_patient_registration tpr where uuid = :uuid";
			PatientRegistrationDto dto = (PatientRegistrationDto) sessionFactory.getCurrentSession().createSQLQuery(sql)
					.setParameter("uuid", uuid)
					 .setResultTransformer(Transformers.aliasToBean(PatientRegistrationDto.class))
					.uniqueResult();
			return new Response(SUCCESS, SUCCESS_CODE, null, null, dto);
		}catch(HibernateException he) {
		logger.error("getPatientDetailsByuuid failed", he);
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
		}

	}
	
	@Override
	public List<PatientDetailsDto> getPatientDataForBirthdaySMS(Integer unitId) throws ApplicationException {
		try {
			
			String pattern = "dd-MM";
	    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

	    	String date = simpleDateFormat.format(TimeZoneComponent.getDateByzone(unitId));
			
			
			String query = "  select"
					+ "  	concat(coalesce(pre.prefix_code,''),'. ',coalesce(patient.first_name,'') ) as \"firstName\", "
					+ "  	to_char(dob,'dd/mm/yyyy') as \"dob\", "
					+ "  	mobile_number as \"mobile\", "
					+ "  	trim(concat(to_char(EXTRACT(year from AGE(CURRENT_TIMESTAMP, patient.dob)),'999'))) as \"age\" "
					+ "  from"
					+ "  	patient.t_patient_registration patient "
					+ "  inner join m_prefix_master pre on "
					+ "  	pre.prefix_id = patient.prefix_id "
					+ "  where"
					+ "  	to_char( dob,"
					+ "  	'dd-mm' )='"+date+"' ";
			
			List<PatientDetailsDto> detailsDtos = sessionFactory.getCurrentSession().createSQLQuery(query)
												.setResultTransformer(Transformers.aliasToBean(PatientDetailsDto.class)).list();
		
			
			return detailsDtos;
		
		} catch (HibernateException he) {
		logger.error("getPatientDataForBirthdaySMS failed", he);
		} catch (Exception e) {
		logger.error("getPatientDataForBirthdaySMS failed", e);
		}
		return null;
	}

	@Override
	public Response PatientDemography(int patientId) throws ApplicationException {
		try{
			List<PatientSearchBillRes> listPatientSearchBillRes = sessionFactory.getCurrentSession().getNamedQuery("GET_PATIENT_DETAILS_BY_ID_FOR_EMR").setParameter("patientId", patientId).setResultTransformer(Transformers.aliasToBean(PatientSearchBillRes.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listPatientSearchBillRes, null);
		}catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response updatePatientAsAbscound(PatientRegistrationDto dto) throws ApplicationException {
		try{
			String query="UPDATE PatientRegistration "
					+ " SET isAbsconded = 'N'"
					+ " WHERE patientId=:patientId ";
			         sessionFactory.getCurrentSession().createQuery(query)
					.setParameter("patientId", dto.getPatientId())
					.executeUpdate();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		}catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
	}

	public Response commonPatientSearch(PatientRegistrationDto patientRegistrationDto) {
		try {
			Session session = sessionFactory.getCurrentSession();
			StringBuilder stringQuery=new StringBuilder(session.getNamedQuery("GET_PATIENT_DATA").getQueryString());
			StringBuilder resQuery = this.generateQueryForPatientSearch(stringQuery,patientRegistrationDto);
			//resQuery.append(" ORDER BY patient.registrationTypeId , patient.firstName ");

			Query actualQuery = session.createSQLQuery(resQuery.toString());
			if(patientRegistrationDto.getOffset()!=null && patientRegistrationDto.getNoOfRecordsPerPage()!=null)
            {
                actualQuery.setFirstResult(patientRegistrationDto.getOffset());
                actualQuery.setMaxResults(patientRegistrationDto.getNoOfRecordsPerPage());
            }

			if (patientRegistrationDto.getUhidNumber() != null && !patientRegistrationDto.getUhidNumber().isEmpty()) {
				actualQuery.setParameter("uhidNumber", "%" + patientRegistrationDto.getUhidNumber().toLowerCase() + "%");
			}

			if (patientRegistrationDto.getPatientName() != null && !patientRegistrationDto.getPatientName().isEmpty()) {
				actualQuery.setParameter("patientName", "%" + patientRegistrationDto.getPatientName().toLowerCase() + "%");
			}

			if (patientRegistrationDto.getGenderId() != null) {
				actualQuery.setParameter("genderId", patientRegistrationDto.getGenderId());
			}

			if (patientRegistrationDto.getMobileNumber() != null && !patientRegistrationDto.getMobileNumber().isEmpty()) {
				actualQuery.setParameter("mobileNumber", patientRegistrationDto.getMobileNumber());
			}

			if (patientRegistrationDto.getDob() != null && !patientRegistrationDto.getDob().isEmpty()) {
				actualQuery.setParameter("dob", String.valueOf(GlobalCommonDateUtils.getDate(patientRegistrationDto.getDob(), "dd-MM-yyyy")));
			}

			if (patientRegistrationDto.getIdentificationNumber() != null && !patientRegistrationDto.getIdentificationNumber().isEmpty()) {
				actualQuery.setParameter("identificationNumber", "%" + patientRegistrationDto.getIdentificationNumber().toLowerCase() + "%");
			}

			if (patientRegistrationDto.getIdentificationTypeId() != null) {
				actualQuery.setParameter("identificationTypeId", String.valueOf(patientRegistrationDto.getIdentificationTypeId()));
			}

			List<PatientRegistrationDto> unknownPatientRegistrationDtosList = actualQuery
					.setParameter("orgId", patientRegistrationDto.getOrganizationId())
					.setParameter("unitId", patientRegistrationDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(PatientRegistrationDto.class)).list();
			return new Response(SUCCESS, null, null, unknownPatientRegistrationDtosList, null);

		} catch (HibernateException he) {
		logger.error("commonPatientSearch failed", he);
		} catch (Exception e) {
		logger.error("commonPatientSearch failed", e);
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);

	}

	@Override
	public Response checkEmailDetails(Integer orgId, String emailId) throws ApplicationException {
		try {
			
			
			String query = " select "
					 + " emp_mst.user_id as \"userId\"  from  public.m_employee_master emp_mst " 
					 + "  where lower(emp_mst.email) =:email " 
					 + " and emp_mst.organization_id  =:orgId " 
					 + " and emp_mst.status = 'A' "
					 + "union  "
					 + "select"  
					 + " doct_mst.user_id as \"userId\" from  doctor.m_doctor_master doct_mst " 
					 + " where  lower(doct_mst.email) =:email " 
					 + " and doct_mst.organization_id =:orgId "  
					 + " and doct_mst.status = 'A' ";
			
			List<Integer> ids = sessionFactory.getCurrentSession().createSQLQuery(query)
					            .setParameter("orgId", orgId)
					            .setParameter("emailId", emailId)
								.list();
			if(ids!=null && ids.size()>0) {
				return new Response(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, 1);
			}else {
				return new Response(SUCCESS, SUCCESS_CODE, null, null, 0);
			}
			

		} catch (HibernateException he) {
		logger.error("checkEmailDetails failed", he);
		} catch (Exception e) {
		logger.error("checkEmailDetails failed", e);
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}
	@Override
	public Response patientDetailsByIdForCyclic(int patientId) throws ApplicationException {
		try{
			List<PatientSearchBillRes> listPatientSearchBillRes = sessionFactory.getCurrentSession().getNamedQuery("GET_PATIENT_DETAILS_BY_ID_FOR_CYCLIC_SEARCH").setParameter("patientId", patientId).setResultTransformer(Transformers.aliasToBean(PatientSearchBillRes.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listPatientSearchBillRes, null);
		}catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response getAllPatientVisits(PatientVisitsDto patientVisitsDto) throws ApplicationException {
		try{
			StringBuilder ipQueryString=new StringBuilder();
			StringBuilder opQueryString=new StringBuilder();
			
			String opQuery="select enc.encounter_id as \"encounterId\", " + 
					"	enc.dept_id as \"specialityId\", " +
					"	enc.patient_id as \"patientId\", " + 
					"	to_char(enc.encounter_date, 'dd/mm/yyyy') as \"encounterDate\", " + 
					"	enc.encounter_number as \"encounterNumber\", " + 
					"	enc.doctor_id as \"doctorId\", " + 
					"	('Dr. ' || coalesce(doc.first_name)|| ' ' || coalesce(doc.middle_name)|| ' ' || coalesce(doc.last_name)) as \"doctorName\", " + 
					"	spc.speciality_name as \"specialityName\" " + 
					"from opd.t_encounter_master enc " + 
					"inner join doctor.m_doctor_master doc on " + 
					"	enc.doctor_id = doc.doctor_id " + 
					"inner join doctor.m_speciality_master spc on " + 
					"	enc.dept_id = spc.speciality_id " + 
					"where enc.organization_id =:orgId " + 
					"	and enc.patient_id =:patientId " + 
					"	and (enc.status_id in (6 ,9)) ";
			
			opQueryString.append(opQuery);
			
			if(patientVisitsDto.getFromDate()!=null && patientVisitsDto.getToDate()!=null) {
				opQueryString.append(" and to_char(enc.encounter_date,'YYYY-MM-DD') between :fromDate and :toDate ");
			}
			
			opQueryString.append(" order by enc.encounter_date");

			String ipQuery = "SELECT " +
					"adm.admission_id AS \"admissionId\", " +
					"adm.doctor_id AS \"doctorId\", " +
					"adm.patient_id AS \"patientId\", " +
					"adm.visit_type_id AS \"visitTypeId\", " +
					"TO_CHAR(adtl.doa, 'DD/MM/YYYY') AS \"admissionDate\", " +
					"TO_CHAR(discharge.discharge_date, 'DD/MM/YYYY') AS \"dischargeDate\", " +
					"adm.speciality_id AS \"specialityId\", " +
					"dept.speciality_name AS \"specialityName\", " +
					"('Dr. ' || COALESCE(doc.first_name, '') || ' ' || COALESCE(doc.middle_name, '') || ' ' || COALESCE(doc.last_name, '')) AS \"doctorName\" " +
					"FROM adt.t_admission adm " +
					"INNER JOIN adt.t_admission_details adtl ON adtl.admission_id = adm.admission_id " +
					"LEFT JOIN doctor.m_doctor_master doc ON doc.doctor_id = adm.doctor_id " +
					"LEFT JOIN doctor.m_speciality_master dept ON dept.speciality_id = adm.speciality_id " +
					"INNER JOIN adt.t_discharge discharge ON discharge.admission_id = adm.admission_id " +
					"WHERE adm.patient_id = :patientId " +
					"AND adm.organization_id = :orgId " +
					"AND adtl.admission_details_id = ( " +
					"    SELECT admission_details_id " +
					"    FROM adt.t_admission_details " +
					"    WHERE admission_id = adm.admission_id " +
					"    ORDER BY admission_details_id DESC " +
					"    LIMIT 1 " +
					")";
			
			ipQueryString.append(ipQuery);
			if(patientVisitsDto.getFromDate()!=null && patientVisitsDto.getToDate()!=null) {
				ipQueryString.append(" and to_char(adtl.doa,'YYYY-MM-DD') between :fromDate and :toDate ");
			}
			
			
			ipQueryString.append(" order by adm.admission_id desc");
			
			
			Query sql = sessionFactory.getCurrentSession()
					.createSQLQuery(opQueryString.toString())
					.setParameter("patientId", patientVisitsDto.getPatientId())
					.setParameter("orgId", patientVisitsDto.getOrganizationId());
			if(patientVisitsDto.getFromDate()!=null && patientVisitsDto.getToDate()!=null) {
				sql.setParameter("fromDate", patientVisitsDto.getFromDate());
				sql.setParameter("toDate", patientVisitsDto.getToDate());
			}
			List<PatientVisitsDto> encounterList =  sql.setResultTransformer(Transformers.aliasToBean(PatientVisitsDto.class)).list();
			
			Query sql1 = sessionFactory.getCurrentSession()
					.createSQLQuery(ipQueryString.toString())
					.setParameter("patientId", patientVisitsDto.getPatientId())
					.setParameter("orgId", patientVisitsDto.getOrganizationId());
			if(patientVisitsDto.getFromDate()!=null && patientVisitsDto.getToDate()!=null) {
				sql1.setParameter("fromDate", patientVisitsDto.getFromDate());
				sql1.setParameter("toDate", patientVisitsDto.getToDate());
			}
			List<PatientVisitsDto> admissionList = sql1.setResultTransformer(Transformers.aliasToBean(PatientVisitsDto.class)).list();
			
			PatientVisitsDto visitDto=new PatientVisitsDto();
			
			if(admissionList.size()>0)
			visitDto.setAdmissionList(admissionList);
			
			if(encounterList.size()>0)
			visitDto.setEncounterList(encounterList);
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, visitDto);
		}catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response getPatientCategoryIdByPatientId(Integer patientId) throws ApplicationException {
		try{
			Integer patientCatId=(Integer)sessionFactory.getCurrentSession().createSQLQuery("select patient_category_id   "
					+ " from patient.t_patient_registration " + 
					"	where patient_id="+patientId).uniqueResult();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null,  Integer.valueOf(patientCatId));
		}catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response getAllPastVisitsDataForInfo(PatientVisitsDto patientVisitsDto) throws ApplicationException {
		try{
			
//			List<PatientVisitsDto> list2=new ArrayList<>();
			//--For OP--//
			if(patientVisitsDto.getEncounterId()!=null) {
				List<PatientVisitsDto> list2=new ArrayList<>();
				
				List<PatientVisitsDto> list1=sessionFactory.getCurrentSession()
						.createSQLQuery("select enc.encounter_id as \"encounterId\", " + 
								"	enc.dept_id as \"specialityId\",  " + 
								"	enc.patient_id as \"patientId\"," + 
								"	to_char(enc.encounter_date, 'dd/mm/yyyy') as \"encounterDate\", " + 
								"	enc.encounter_number as \"encounterNumber\",  " + 
								"	enc.doctor_id as \"doctorId\", " + 
								"	('Dr. ' || coalesce(doc.first_name)|| ' ' || coalesce(doc.middle_name)|| ' ' || coalesce(doc.last_name)) as \"doctorName\", " + 
								"	spc.speciality_name as \"specialityName\"  " + 
								" from opd.t_encounter_master enc " + 
								" inner join doctor.m_doctor_master doc on  " + 
								"	enc.doctor_id = doc.doctor_id  " + 
								" inner join doctor.m_speciality_master spc on " + 
								"	enc.dept_id = spc.speciality_id  " + 
								" where enc.organization_id =:organizationId  " + 
								"	and enc.patient_id =:patientId  " + 
								"	and (enc.status_id in (6 ,9)) ")
					.setParameter("organizationId", patientVisitsDto.getOrganizationId())
					.setParameter("patientId", patientVisitsDto.getPatientId())
					.setResultTransformer(Transformers.aliasToBean(PatientVisitsDto.class)).list();
				
				//For Daily progress
					for(PatientVisitsDto dto : list1) 
					{
							List<PatientVisitsDto> list3=sessionFactory.getCurrentSession()
									.createSQLQuery("select " + 
											" encounter.encounter_id as \"encounterId\"," + 
											" encounter.dept_id as \"deptId\"," + 
											" encounter.user_defined_visit_type_id as \"userDefinedVisitTypeId\"," + 
											" to_char(encounter.encounter_date,'yyyy-mm-dd') as \"encounterDateString\", " + 
											" to_char(encounter.encounter_date,'dd/mm/yyyy') as \"encounterDateString2\"," + 
											" encounter.encounter_number as \"encounterNumber\", " + 
											" encounter.doctor_id as \"doctorId\", " + 
											" encounter.patient_class_id as \"patientClassId\"," + 
											" concat('Dr. ',coalesce(doc.first_name),' ',coalesce(doc.middle_name),' ',coalesce(doc.last_name)) as \"doctorName\", " + 
											" encounter.dept_id as \"specialityId\", " + 
											" speciality.speciality_name as \"specialityName\" " + 
											" from opd.t_encounter_master encounter" + 
											" INNER JOIN doctor.m_doctor_master doc on doc.doctor_id= encounter.doctor_id" + 
											" INNER JOIN doctor.m_speciality_master speciality on speciality.speciality_id = encounter.dept_id" + 
											" WHERE encounter.organization_id=:organizationId " + 
//											" AND encounter.unit_id=:unitId " + 
											" AND encounter.patient_id=:patientId " + 
											" AND encounter.status_id in (6, 9) " + 
//											" and to_char(encounter.created_date,'YYYY-MM-DD') >=:fromDate " + 
//											" and to_char(encounter.created_date,'YYYY-MM-DD') <=:toDate " + 
											" AND to_char(encounter.created_date,'YYYY-MM-DD') <= :createdDate " + 
											" ORDER BY encounter.encounter_date ")
								.setParameter("organizationId", patientVisitsDto.getOrganizationId())
//								.setParameter("unitId", patientVisitsDto.getUnitId())
								.setParameter("patientId", patientVisitsDto.getSpecialityId())
								.setParameter("createdDate", patientVisitsDto.getCreatedDate())
//								.setParameter("fromDate", patientVisitsDto.getFromDate())
//								.setParameter("toDate", patientVisitsDto.getToDate())
								.setResultTransformer(Transformers.aliasToBean(PatientVisitsDto.class)).list();
						
							dto.setListForDailyProgress(list3);
							list2.add(dto);	
					}
				/*
				 * //For Procedure Note for(PatientVisitsDto dto : list1) {
				 * List<PatientVisitsDto> list3=sessionFactory.getCurrentSession()
				 * .createSQLQuery("") .setParameter("organizationId",
				 * patientVisitsDto.getOrganizationId()) .setParameter("unitId",
				 * patientVisitsDto.getUnitId()) .setParameter("patientId",
				 * patientVisitsDto.getSpecialityId())
				 * .setResultTransformer(Transformers.aliasToBean(PatientVisitsDto.class)).list(
				 * );
				 * 
				 * dto.setListForAllPastvisits(list1);
				 * 
				 * }
				 */
				//For certificate	
					for(PatientVisitsDto dto : list1) 
					{
							List<PatientVisitsDto> list4=sessionFactory.getCurrentSession()
									.createSQLQuery("select to_char(template_transaction.created_date, 'DD-MM-YYYY HH24:MI:SS') as \"createdDate\" , "
											+ "template_transaction.template_object as \"templateObject\","
											+ "template_transaction.template_id as \"templateId\", "
											+ "template_transaction.template_transaction_id as \"templateTransactionId\", "
											+ "template_transaction.admission_id as \"admissionId\","
											+ "template_transaction.title as \"title\","
											+ "concat('Dr. ',coalesce(dctMst.first_name,''),' ',coalesce(dctMst.middle_name,''),' ',coalesce(dctMst.last_name,'')) as \"doctorName\" "
											+ "from public.t_template_transaction template_transaction "
											+ "inner join public.t_template_master tem_mst on template_transaction.template_id = tem_mst.template_id "
											+ "left join doctor.m_doctor_master dctMst on dctMst.doctor_id=template_transaction.doctor_id "
											+ "where "
											+ "template_transaction.org_id = :organizationId "
											+ "and template_transaction.unit_id = :unitId "
//											+ "and tem_mst.template_type_id =:templateTypeId "
											+ "and template_transaction.patient_id = :patientId")
//											+ " and to_char(template_transaction.created_date,'YYYY-MM-DD') >=:fromDate "
//											+ " and to_char(template_transaction.created_date,'YYYY-MM-DD') <=:toDate ")
								.setParameter("organizationId", patientVisitsDto.getOrganizationId())
								.setParameter("unitId", patientVisitsDto.getUnitId())
								.setParameter("patientId", patientVisitsDto.getPatientId())
//								.setParameter("fromDate", patientVisitsDto.getFromDate())
//								.setParameter("toDate", patientVisitsDto.getToDate())
								.setResultTransformer(Transformers.aliasToBean(PatientVisitsDto.class)).list();
						
							dto.setListForCertificates(list4);
							list2.add(dto);	
					}
					
					return new Response(SUCCESS, SUCCESS_CODE, null, list1, null);
			}
			
			
			//--For IP/ER/DC--//
			if(patientVisitsDto.getAdmissionId()!=null)
			{
				List<PatientVisitsDto> list2=new ArrayList<>();
				
				List<PatientVisitsDto> list1=sessionFactory.getCurrentSession()
						.createSQLQuery(" select  " + 
								"	adm.admission_id as \"admissionId\", " + 
								"	adm.doctor_id as \"doctorId\", " + 
								"	dissumm.discharge_summary_id as \"dischargeSummaryId\","+
								"	adm.patient_id as \"patientId\", " + 
								"  adm.visit_type_id as \"visitTypeId\"," + 
								"	to_char(adtl.doa, 'DD/MM/YYYY') as \"admissionDate\", " + 
								"	to_char(discharge.discharge_date, 'DD/MM/YYYY') as \"dischargeDate\", " + 
								"	adm.speciality_id as \"specialityId\", " + 
								"	dept.speciality_name as  \"specialityName\", " + 
								"	('Dr. ' || coalesce(doc.first_name)|| ' ' || coalesce(doc.middle_name)|| ' ' || coalesce(doc.last_name)) as \"doctorName\"  " + 
								" from " + 
								"	adt.t_admission adm  " + 
								" inner join adt.t_admission_details adtl on  " + 
								"	adtl.admission_id = adm.admission_id  " + 
								" left join doctor.m_doctor_master doc on  " + 
								"	doc.doctor_id=adm.doctor_id " + 
								" left join doctor.m_speciality_master dept on  " + 
								"	dept.speciality_id=adm.speciality_id	 " + 
								" left join adt.m_discharge_summary	dissumm on  " + 
								"    dissumm.admission_id=adm.admission_id " + 
								" left join adt.t_discharge	discharge on  " + 
								"    discharge.admission_id=adm.admission_id " + 
								" where " + 
								"	adm.patient_id=:patientId  and dissumm.status='A' "+
							    "   and adm.organization_id=:organizationId" + 
								"	and adtl.admission_details_id = ( " + 
								"	select admission_details_id " + 
								"	from adt.t_admission_details " + 
								"	where admission_id = adm.admission_id " + 
								"	order by admission_details_id desc limit 1) ")
					.setParameter("organizationId", patientVisitsDto.getOrganizationId())
					.setParameter("patientId", patientVisitsDto.getPatientId())
					.setResultTransformer(Transformers.aliasToBean(PatientVisitsDto.class)).list();
					
				//For Daily progress
				for(PatientVisitsDto dto : list1) 
				{
						List<PatientVisitsDto> list3=sessionFactory.getCurrentSession()
							.createSQLQuery(" select" + 
									"	adm.admission_id as \"admissionId\"," + 
									"	adm.visit_type_id as \"visitTypeId\"," + 
									"	vtm.visit_type_code as \"visitTypeName\"," + 
									"	to_char(adtl.doa, 'DD/MM/YYYY') as \"visitDate\"," + 
									"	adm.speciality_id as \"specialityId\"," + 
									"	dept.speciality_name as  \"specialityName\"," + 
									"	('Dr. ' || coalesce(doc.first_name)|| ' ' || coalesce(doc.middle_name)|| ' ' || coalesce(doc.last_name)) as \"doctorName\" " + 
									"	" + 
									" from" + 
									"	adt.t_admission adm " + 
									" inner join adt.t_admission_details adtl on " + 
									"	adtl.admission_id = adm.admission_id " + 
									" left join doctor.m_doctor_master doc on " + 
									"	doc.doctor_id=adm.doctor_id" + 
									" left join doctor.m_speciality_master dept on " + 
									"	dept.speciality_id=adm.speciality_id	" + 
									" left join public.m_visit_type_master vtm on " + 
									"	vtm.visit_type_id=adm.visit_type_id	" + 
									" where" + 
									//"	adm.admission_id !=:admissionId " + 
									//"	and "
									" adm.patient_id=:patientId " + 
//									"	and adm.visit_type_id=:visitTypeId " + 
									"	and adtl.admission_details_id = (" + 
									"	select admission_details_id" + 
									"	from adt.t_admission_details" + 
									"	where admission_id = adm.admission_id" + 
									"	order by admission_id desc limit 1)" + 
									"	and adm.status='I'" + 
									//"	and adm.patient_id=adm.patient_id" 
//									 " and to_char(adm.created_date,'YYYY-MM-DD') >=:fromDate "
//									+ " and to_char(adm.created_date,'YYYY-MM-DD') <=:toDate "+
									" order by adm.admission_id desc	"
									)
//							.setParameter("organizationId", dto.getOrganizationId())
//							.setParameter("unitId", dto.getUnitId())
//							.setParameter("admissionId", dto.getAdmissionId())
							.setParameter("patientId", patientVisitsDto.getPatientId())
//							.setParameter("fromDate", patientVisitsDto.getFromDate())
//							.setParameter("toDate", patientVisitsDto.getToDate())
							.setResultTransformer(Transformers.aliasToBean(PatientVisitsDto.class)).list();
					
						dto.setListForDailyProgress(list3);
						list2.add(dto);	
				}
				
				  //For Procedure Note
				  
				  for(PatientVisitsDto dto1 : list1) 
				  { 
					  List<PatientVisitsDto>  list5=sessionFactory.getCurrentSession() 
							  .createSQLQuery("select distinct(proreq.patient_id) as \"patientId\","
							  		+ " proreq.admission_id as \"admissionId\", "
							  		+ " to_char(proreq.prefered_date_time, 'DD/MM/YYYY') as \"preferedDateTime\"," 
							  		+ "	spl.speciality_id as \"specialityId\","
							  		+ " spl.speciality_name as \"specialityName\", "
							  		+ " sermst.service_standard_name as \"serviceStandardName\", "
//							  		+ " pronote.template as \"template\", "
							  		+ " proreq.pro_req_id as \"proReqId\" "
//							  		+ " concat('Dr. ',coalesce(dctMst.first_name,''),' ',coalesce(dctMst.middle_name,''),' ',coalesce(dctMst.last_name,'')) as \"doctorName\" "	
							  		+"	from  otims.t_procedure_req_mst proreq " + 
							  		"	inner join otims.t_procedure_configuration procon on proreq.primary_procedure_id = procon.service_id " + 
							  		"	inner join doctor.m_speciality_master spl on spl.speciality_id = procon.speciality_id" + 
							  		"	inner join otims.t_procedure_config_note  pronote on procon.procedure_config_id = pronote.procedure_config_id "
							  		+ " inner join otims.t_procedure_order_master  proordmst on proordmst.pro_req_id= proreq.pro_req_id" + 
							  		"	inner join service.m_service_master sermst on sermst.service_master_id= proordmst.service_id" + 
							  		"	where proreq.patient_id = :patientId")
//				  .setParameter("organizationId", patientVisitsDto.getOrganizationId())
//				  .setParameter("unitId", patientVisitsDto.getUnitId()) 
				  .setParameter("patientId", patientVisitsDto.getPatientId())
				  .setResultTransformer(Transformers.aliasToBean(PatientVisitsDto.class)).list(
				  );
				  
				      dto1.setListForProcedureNote(list5);
				      list2.add(dto1);
				  }
				 
			//For certificate	
				for(PatientVisitsDto dto2 : list1) 
				{
						List<PatientVisitsDto> list4=sessionFactory.getCurrentSession()
								.createSQLQuery("  select distinct(template_transaction.template_transaction_id) as \"templateTransactionId\", to_char(template_transaction.created_date, 'DD-MM-YYYY HH24:MI:SS') as \"createdDate\" ,"
						                + " template_transaction.template_object as \"templateObject\"," 
										+ " template_transaction.template_id as \"templateId\"," 
						                + " template_transaction.admission_id as \"admissionId\","
										+ " template_transaction.title as \"title\","
						                + " splMst.speciality_name as \"specialityName\"," 
										+ " concat('Dr. ',coalesce(dctMst.first_name,''),' ',coalesce(dctMst.middle_name,''),' ',coalesce(dctMst.last_name,'')) as \"doctorName\" "
						                + " from public.t_template_transaction template_transaction"
										+ " left join public.t_template_master tem_mst on template_transaction.template_id = tem_mst.template_id " 
						                + " left join doctor.m_doctor_master dctMst on dctMst.doctor_id=template_transaction.doctor_id"
										+ " left join doctor.t_doctor_speciality_mapper splMppr on splMppr.doctor_id=dctMst.doctor_id and splMppr.status='A'" 
						                + " left join doctor.m_speciality_master splMst on splMst.speciality_id=splMppr.speciality_id and splMst.status='A'"
										+ " where" 
										+ " template_transaction.org_id = :organizationId "
										+ " and template_transaction.unit_id = :unitId "
										+ " and template_transaction.patient_id = :patientId")
							.setParameter("organizationId", patientVisitsDto.getOrganizationId())
							.setParameter("unitId", patientVisitsDto.getUnitId())
							.setParameter("patientId", patientVisitsDto.getPatientId())
							.setResultTransformer(Transformers.aliasToBean(PatientVisitsDto.class)).list();
					
						dto2.setListForCertificates(list4);
						list2.add(dto2);	
				}
				
				
				return new Response(SUCCESS, SUCCESS_CODE, null, list1, null);
			}
		
			return new Response(SUCCESS, SUCCESS_CODE, null, null, null);
	}catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response deletePatientImage(int patientId,int organizationId) throws ApplicationException {
		 try{
		      Integer result = sessionFactory.getCurrentSession().getNamedQuery("UPDATE_PATIENT_IMAGE_PATH")
		          .setParameter("orgId", organizationId)
		          .setBinary("image", null)
		          .setParameter("patientId", patientId)
		          .setParameter("imagePath", "").executeUpdate();
		      if(result != null){
		          return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		      }
		              
		  }catch(HibernateException he){
		  logger.error("deletePatientImage failed", he);
		  }catch(Exception e){
		  logger.error("deletePatientImage failed", e);
		  }
		  return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response changeStatus(PatientRegistrationDto patientRegistrationDto) {
		try{
		      
			
			if(patientRegistrationDto.getStatus()=='I')
			{
				String query = "select " 
						 + "	1 " 
						 + "from " 
						 + "	opd.m_appointment_master mam " 
						 + "where " 
						 + "	appointment_status_id in (1, " 
						 + "	8, " 
						 + "	5) " 
						 + "	and to_char(appointment_date, 'YYYY-MM-DD') >= :currentDate " 
						 + "	and patient_id =:patientId " 
						 + " union " 
						 + "select " 
						 + "	2  " 
						 + "from " 
						 + "	adt.t_admission " 
						 + "where " 
						 + "	status = 'A' " 
						 + "	and patient_id =:patientId " 
						 + "union " 
						 + "select " 
						 + "	3  " 
						 + "from " 
						 + "	adt.t_admission_note note " 
						 + "inner join adt.t_admission_patient_mapping adl on " 
						 + "	adl.admission_note_id = note.admission_note_id " 
						 + "	and adl.status = 'A' " 
						 + "where " 
						 + "	admission_status = 'P' " 
						 + "	and note.status = 'A' " 
						 + "	and to_char(adl.doa, 'YYYY-MM-DD') >= :currentDate " 
						 + "	and patient_id =:patientId " 
						 + " union " 
						 + "select " 
						 + "	4  " 
						 + "from " 
						 + "	billing.m_billing_master mbm " 
						 + "where " 
						 + "	status = 'A' " 
						 + "	and bill_status_id != 3 " 
						 + "	and patient_id =:patientId " 
						 + "union " 
						 + "select " 
						 + "	5  " 
						 + "from " 
						 + "	t_order_master tom " 
						 + "inner join t_order_details om on " 
						 + "	tom.order_id = om.order_id " 
						 + "where " 
						 + "	om.status = 'A' " 
						 + "	and om.service_rendered = 0 " 
						 + "	and om.service_is_billed = 1 " 
						 + "	and tom.patient_id =:patientId "
						 + "limit 1 ";
						 
				Integer type = 0 ;
				type =	(Integer) sessionFactory.getCurrentSession().createSQLQuery(query)
						.setParameter("patientId",patientRegistrationDto.getPatientId())
						.setParameter("currentDate", GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(patientRegistrationDto.getUnitId()),"yyyy-MM-dd"))
						.uniqueResult();
				if(type!=null && type>0)
				{
					if(type==1)
					{
						return new Response<>(ERROR, ALREADY_EXIST_CODE, "Patient has an active appointment !!", null, null);
					}
					else if(type==2)
					{
						return new Response<>(ERROR, ALREADY_EXIST_CODE, "Patient has an active admission !!", null, null);
					}	
					else if(type==3)
					{
						return new Response<>(ERROR, ALREADY_EXIST_CODE, "Patient has a pending admission request !!", null, null);
					}	
					else if(type==4)
					{
						return new Response<>(ERROR, ALREADY_EXIST_CODE, "Patient has unsettled bill(s) !!", null, null);
					}	
					else if(type==5)
					{
						return new Response<>(ERROR, ALREADY_EXIST_CODE, "Patient has pending service(s) !!", null, null);
					}
				}
				
			}
			
			Integer result = sessionFactory.getCurrentSession().createQuery("UPDATE PatientRegistration "
						+ " SET status =:status, "
						+ "	updatedBy=:updatedBy,"
						+ " updatedDate=:updatedDate "
						+ " WHERE patientId=:patientId ")
		          .setCharacter("status", patientRegistrationDto.getStatus())
		          .setParameter("patientId", patientRegistrationDto.getPatientId())
		          .setParameter("updatedBy", patientRegistrationDto.getUpdatedBy())
		          .setDate("updatedDate", TimeZoneComponent.getDateByzone(patientRegistrationDto.getUnitId()))
		          .executeUpdate();
		      
		      Integer result2 = sessionFactory.getCurrentSession().createQuery("UPDATE PatientDetails "
						+ " SET status =:status, "
						+ "	updatedBy=:updatedBy,"
						+ " updatedDate=:updatedDate "
						+ " WHERE patientId=:patientId ")
		          .setCharacter("status", patientRegistrationDto.getStatus())
		          .setParameter("patientId", patientRegistrationDto.getPatientId())
		          .setParameter("updatedBy", patientRegistrationDto.getUpdatedBy())
		          .setDate("updatedDate", TimeZoneComponent.getDateByzone(patientRegistrationDto.getUnitId()))
		          .executeUpdate();
		      
		      if(result != null && result2!=null){
		          return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		      }
		              
		  }catch(HibernateException he){
		  logger.error("changeStatus failed", he);
		  }catch(Exception e){
		  logger.error("changeStatus failed", e);
		  }
		  return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Integer getPatientIdByUuidAndHospitalId(ScheduleAppointmentReqDto reqDto) throws ApplicationException {
		try{
			Integer patientId=(Integer)sessionFactory.getCurrentSession()
					.createSQLQuery("select patReg.patient_id from patient.t_patient_registration patReg "  
					+"	where patReg.uuid=:uuid and patReg.organization_id=:orgId ")
					.setParameter("uuid", reqDto.getUuid())
					.setParameter("orgId", reqDto.getHospitalId())
					.uniqueResult();
			return patientId;
		}catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
	}
	
	@Override
	public String getUUIDByPatientId(Integer patientId) throws ApplicationException {
		try{
			String uuid=(String)sessionFactory.getCurrentSession()
					.createSQLQuery("select patReg.uuid from patient.t_patient_registration patReg "  
					+"	where patReg.patient_id=:patientId ")
					.setParameter("patientId", patientId)
					.uniqueResult();
			return uuid;
		}catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response getPatientDocuments(EncounterDocDetailsDto encounterDocDetailsDto) throws ApplicationException {
		try {
			String query = sessionFactory.getCurrentSession()
					.createSQLQuery("select "
				+ "	doc_type_mst.document_type_name as \"documentName\", "
				+ "	doc_dtls.document_path as \"documentPath\", "
				+ "	doc_dtls.file_name as \"fileName\", "
				+ " doc_dtls.document_type_id as \"documentTypeId\", "
				+ " to_char(doc_dtls.created_date,'DD-MM-YYYY') as \"createdDate\", "
				+ " to_char(doc_dtls.created_date,'DD-MM-YYYY HH24:MI:SS') as \"uploadDoccumentDate\"  "
				+ "from "
				+ "	opd.t_encounter_doc_details doc_dtls "
				+ "inner join public.m_document_type_master doc_type_mst on "
				+ "	doc_dtls.document_type_id = doc_type_mst.document_type_id "
				+ "where "
				+ "	 doc_dtls.organisation_id = :orgId "
				+ "	and doc_dtls.unit_id = :unitId ")
					//.getNamedQuery("GET_ENCOUNTER_DOC_DETAILS")
				.getQueryString();
			StringBuffer sb = new StringBuffer(query);
			if(encounterDocDetailsDto.getEncounterId()!=null)
			{
				sb.append(" and (doc_dtls.encounter_id =  "+encounterDocDetailsDto.getEncounterId()+")");
			}
			else if(encounterDocDetailsDto.getAdmissionId()!=null)
			{
				sb.append(" and (doc_dtls.admission_id =  "+encounterDocDetailsDto.getAdmissionId()+")");
			}
			else 
			{
				sb.append(" and  doc_dtls.patient_id = "+encounterDocDetailsDto.getPatientId());
			}
			sb.append(" and  doc_dtls.status='A' order by doc_dtls.encounter_doc_id desc ");
			
			List<PatientDocDto> listPatientDocDto = sessionFactory.getCurrentSession()
					.createSQLQuery(sb.toString())
					.setParameter("unitId", encounterDocDetailsDto.getUnitId())
					.setParameter("orgId", encounterDocDetailsDto.getOrganisationId())
					.setResultTransformer(Transformers.aliasToBean(PatientDocDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listPatientDocDto, null);
		} catch (Exception e) {
		logger.error("getPatientDocuments failed", e);
		}
		return new Response(ERROR, null, null, null, null);

	}

	@Override
	public Response getPatientDocumentsByEncounterDocId(Integer recordId) throws ApplicationException {
		try {
	         PatientDocumentSyncDto patientDocSynDto =(PatientDocumentSyncDto) sessionFactory.getCurrentSession()
					.createSQLQuery("	select docType.document_type_name as \"documentTypeName\", docDet.document_path as \"documentPath\", "  
							+"	docDet.file_name as \"title\", docDet.document_type_id as \"documentTypeId\", "  
							+"	to_char(docDet.created_date,'DD-MM-YYYY') as \"createdDate\","  
							+"	to_char(docDet.created_date,'DD-MM-YYYY HH24:MI:SS') as \"documentDate\"," 
							+"    mom.global_org_id as \"globalOrgId\",mum.global_unit_id as \"globalUnitId\",tpr.uuid as \"uuid\" " 
							+"from " 
							+"	opd.t_encounter_doc_details docDet " 
							+"inner join public.m_document_type_master docType on docDet.document_type_id = docType.document_type_id " 
							+"inner join public.m_organization_master mom on mom.organization_id = docDet.organisation_id "  
							+"inner join public.m_unit_master mum on mum.unit_id = docDet.unit_id "  
							+"inner join patient.t_patient_registration tpr on tpr.patient_id = docDet.patient_id " 
							+"where docDet.encounter_doc_id =:encDocId and tpr.patient_id is not null " 
							+"and mom.global_org_id is not null and mum.global_unit_id is not null"  
							)
					.setParameter("encDocId", recordId)
					.setResultTransformer(Transformers.aliasToBean(PatientDocumentSyncDto.class)).uniqueResult();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, patientDocSynDto);
		} catch (Exception e) {
		logger.error("getPatientDocumentsByEncounterDocId failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getPatientKinDetails(Integer recordId) throws ApplicationException {
		try {
			PatientSyncDto kinDetailDto =(PatientSyncDto) sessionFactory.getCurrentSession()
					.createSQLQuery("select kinDet.patient_id \"patientId\",tpr.uuid as \"uuid\",kinDet.kin_name as \"kinFirstName\",	"
							+" (select case when (kinDet.address is null or kinDet.address='' ) then concat(mam.area_name,',',mcm.city_name) else kinDet.address end) as \"kinAddress\"," 
							+" kinDet.mobile_no as \"kinMobileNumber\",mrm.relation_name as \"kinRelationShip\" "  
							+" from public.m_kin_details kinDet "  
							+" inner join patient.t_patient_registration tpr on tpr.patient_id =kinDet.patient_id " 
							+" inner join public.m_relation_master mrm on mrm.relation_id =kinDet.relation_id "  
							+" left join public.m_city_master mcm on mcm.city_id =kinDet.city_id "  
							+" left join public.m_area_master mam on mam.area_id =kinDet.area_id " 
							+"  where kinDet.kin_id =:kinId order by kinDet.kin_id desc limit 1"  
							)
					.setParameter("kinId", recordId)
					.setResultTransformer(Transformers.aliasToBean(PatientSyncDto.class)).uniqueResult();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, kinDetailDto);
		} catch (Exception e) {
		logger.error("getPatientKinDetails failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response deletePatientDocument(EncounterDocDetailsDto encounterDocDetailsDto) throws ApplicationException {
		try {
	               sessionFactory.getCurrentSession()
				   .createSQLQuery("update opd.t_encounter_doc_details set status ='I' "
				   		+ "where to_char(created_date,'DD-MM-YYYY HH24:MI:SS')=:uploadDateTime "
				   		+ "and patient_id =:patId")
				   .setParameter("uploadDateTime", encounterDocDetailsDto.getUploadDoccumentDate())
				   .setParameter("patId", encounterDocDetailsDto.getPatientId())
				   .executeUpdate();
					
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		} catch (Exception e) {
		logger.error("deletePatientDocument failed", e);
		}
		return new Response(ERROR, null, null, null, null);

	}

	@Override
	public Response patientSearchPatientList(PatientRegistrationDto patientRegistrationDto)
			throws ApplicationException {
		try {
			Session session = sessionFactory.getCurrentSession();
			StringBuilder stringQuery=new StringBuilder(session.getNamedQuery("GET_PATIENT_DATA").getQueryString());
			StringBuilder resQuery = this.generateQueryForPatientSearch(stringQuery,patientRegistrationDto);

			Query actualQuery = session.createSQLQuery(resQuery.toString());
			if (patientRegistrationDto.getUhidNumber() != null && !patientRegistrationDto.getUhidNumber().isEmpty()) {
				actualQuery.setParameter("uhidNumber", "%" + patientRegistrationDto.getUhidNumber().toLowerCase() + "%");
			}

			if (patientRegistrationDto.getPatientName() != null && !patientRegistrationDto.getPatientName().isEmpty()) {
				actualQuery.setParameter("patientName", "%" + patientRegistrationDto.getPatientName().toLowerCase() + "%");
			}

			if (patientRegistrationDto.getGenderId() != null) {
				actualQuery.setParameter("genderId", patientRegistrationDto.getGenderId());
			}

			if (patientRegistrationDto.getMobileNumber() != null && !patientRegistrationDto.getMobileNumber().isEmpty()) {
				actualQuery.setParameter("mobileNumber", patientRegistrationDto.getMobileNumber());
			}

			if (patientRegistrationDto.getDob() != null && !patientRegistrationDto.getDob().isEmpty()) {
				actualQuery.setParameter("dob", String.valueOf(GlobalCommonDateUtils.getDate(patientRegistrationDto.getDob(), "dd-MM-yyyy")));
			}

			if (patientRegistrationDto.getIdentificationNumber() != null && !patientRegistrationDto.getIdentificationNumber().isEmpty()) {
				actualQuery.setParameter("identificationNumber", "%" + patientRegistrationDto.getIdentificationNumber().toLowerCase() + "%");
			}

			if (patientRegistrationDto.getIdentificationTypeId() != null) {
				actualQuery.setParameter("identificationTypeId", String.valueOf(patientRegistrationDto.getIdentificationTypeId()));
			}

			List<PatientRegistrationDto> unknownPatientRegistrationDtosList = actualQuery
					.setParameter("orgId", patientRegistrationDto.getOrganizationId())
					.setParameter("unitId", patientRegistrationDto.getUnitId())
					.setFirstResult(patientRegistrationDto.getOffset() != null ? patientRegistrationDto.getOffset()  : 0)
					.setMaxResults(patientRegistrationDto.getNoOfRecordsPerPage() != null ? patientRegistrationDto.getNoOfRecordsPerPage()  : 10)
					.setResultTransformer(Transformers.aliasToBean(PatientRegistrationDto.class)).list();
			Integer count=0;
		/*	Integer count=0;
			String sql = "  SELECT cast(count(*) as int) FROM patient.t_patient_registration patReg INNER JOIN patient.t_patient_details patDtls ON	patReg.patient_id = patDtls.patient_id " + 
	        		" WHERE patReg.organization_id ="+patientRegistrationDto.getOrganizationId()+" AND patReg.is_primary = 'Y' AND patReg.status = 'A' AND patDtls.status = 'A'";
            Query getCreditNoteAmount = sessionFactory.getCurrentSession().createSQLQuery(sql);
            count=(Integer) getCreditNoteAmount.uniqueResult();*/
			StringBuilder stringQuery1 = new StringBuilder(session.getNamedQuery("GET_PATIENT_DATA").getQueryString());
		StringBuilder resQuery1 = this.generateQueryForPatientSearch(stringQuery1,patientRegistrationDto);
			Query actualQuery1 = session.createSQLQuery(resQuery1.toString());
			if (patientRegistrationDto.getUhidNumber() != null && !patientRegistrationDto.getUhidNumber().isEmpty()) {
				actualQuery1.setParameter("uhidNumber", "%" + patientRegistrationDto.getUhidNumber().toLowerCase() + "%");
			}

			if (patientRegistrationDto.getPatientName() != null && !patientRegistrationDto.getPatientName().isEmpty()) {
				actualQuery1.setParameter("patientName", "%" + patientRegistrationDto.getPatientName().toLowerCase() + "%");
			}

			if (patientRegistrationDto.getGenderId() != null) {
				actualQuery1.setParameter("genderId", patientRegistrationDto.getGenderId());
			}

			if (patientRegistrationDto.getMobileNumber() != null && !patientRegistrationDto.getMobileNumber().isEmpty()) {
				actualQuery1.setParameter("mobileNumber", patientRegistrationDto.getMobileNumber());
			}

			if (patientRegistrationDto.getDob() != null && !patientRegistrationDto.getDob().isEmpty()) {
				actualQuery1.setParameter("dob", String.valueOf(GlobalCommonDateUtils.getDate(patientRegistrationDto.getDob(), "dd-MM-yyyy")));
			}

			if (patientRegistrationDto.getIdentificationNumber() != null && !patientRegistrationDto.getIdentificationNumber().isEmpty()) {
				actualQuery1.setParameter("identificationNumber", "%" + patientRegistrationDto.getIdentificationNumber().toLowerCase() + "%");
			}

			if (patientRegistrationDto.getIdentificationTypeId() != null) {
				actualQuery1.setParameter("identificationTypeId", String.valueOf(patientRegistrationDto.getIdentificationTypeId()));
			}

		List<PatientRegistrationDto> unknownPatientRegistrationDtosList1 = actualQuery1
					.setParameter("orgId", patientRegistrationDto.getOrganizationId())
				.setParameter("unitId", patientRegistrationDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(PatientRegistrationDto.class)).list();
		if(unknownPatientRegistrationDtosList1 != null && unknownPatientRegistrationDtosList1.size()>0) {
	    	  count = unknownPatientRegistrationDtosList1.size();
		      }
			return new Response(SUCCESS, null, null, unknownPatientRegistrationDtosList, count);

		} catch (HibernateException he) {
		logger.error("patientSearchPatientList failed", he);
		} catch (Exception e) {
		logger.error("patientSearchPatientList failed", e);
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
}

	@Override
	public Response getPatientDetailsByUhid(PatientRegistrationDto dto) throws ApplicationException {
		try {
			Session session = sessionFactory.getCurrentSession();
			StringBuilder stringQuery=new StringBuilder( " select " 
					 + " 	patReg.patient_id as \"patientId\", " 
					 + " 	patReg.uhid_number as \"uhidNumber\", " 
					 + " 	patReg.prefix_id as \"prefixId\", " 
					 + " 	preFix.prefix_code as \"prefixCode\", " 
					 + " 	coalesce(patReg.first_name, '') as \"firstName\", " 
					 + " 	coalesce(patReg.middle_name, '') as \"middleName\", " 
					 + " 	coalesce(patReg.last_name, '') as \"lastName\", " 
					 + " 	patReg.gender_id as \"genderId\", " 
					 + " 	patReg.image_path as \"imagePath\", " 
					 + " 	gen.gender_code as \"genderCode\", " 
					 + " 	to_char(patReg.dob, 'MM/DD/YYYY') as \"dob\", " 
					 + " 	to_char(patReg.dob, 'DD/MM/YYYY') as \"birthDate\", " 
					 + " 	trim(to_char(extract(year from age(CURRENT_TIMESTAMP, patReg.dob)), '999')) as \"ageString\", " 
					 + " 	patReg.mobile_number as \"mobileNumber\", " 
					 + " 	patReg.country_calling_code as \"countryCallingCode\", " 
					 + " 	patReg.is_vip as \"isVip\", " 
					 + " 	patReg.vip_remark as \"vipRemark\", " 
					 + " 	patReg.is_blacklist as \"isBlacklist\", " 
					 + " 	patReg.identification_type_id as \"identificationTypeId\", " 
					 + " 	coalesce(identi.identification_name, '') as \"identificationName\", " 
					 + " 	identi.is_expiry_required as \"isExpiryRequired\", " 
					 + " 	patReg.identification_number as \"identificationNumber\", " 
					 + " 	patReg.email as \"email\", " 
					 + " 	patReg.is_absconded as \"isAbsconded\", " 
					 + " 	patReg.alise_name as \"aliseName\", " 
					 + " 	patReg.uuid as \"uuid\", " 
					 + " 	patReg.status as \"status\", " 
					 + " 	patReg.patient_category_id as \"patientCategoryId\", " 
					 + " 	patReg.mpshah_patient_id as \"mpshahPatientId\", " 
					 + " 	patReg.whatsapp_no as \"whatsappNo\", " 
					 + " 	patReg.whatsapp_calling_code as \"whatsappCallingCode\", " 
					 + " 	patReg.skype_id as \"skypeId\", " 
					 + " 	to_char(patReg.identification_expiry_date, 'MM/DD/YYYY') as \"identificationExpiryDate\", " 
					 + " 	patReg.occupation_id as \"occupationId\", " 
					 + " 	patDtls.patient_details_id as \"patientDetailsId\", " 
					 + " 	patDtls.nationality_id as \"nationalityId\", " 
					 + " 	patDtls.race_id as \"raceId\", " 
					 + " 	patDtls.marital_status_id as \"maritalStatusId\", " 
					 + " 	patReg.registration_type_id as \"registrationTypeId\", " 
					 + " 	patDtls.address as \"address\", " 
					 + " 	patDtls.country_id as \"countryId\", " 
					 + " 	patDtls.state_id as \"stateId\", " 
					 + " 	patDtls.district_id as \"districtId\", " 
					 + " 	patDtls.city_id as \"cityId\", " 
					 + " 	patDtls.area_id as \"areaId\", " 
					 + " 	patDtls.zip_code as \"zipCode\", " 
					 + " 	patDtls.phone_code as \"phoneCode\", " 
					 + " 	patDtls.phone_number as \"phoneNumber\", " 
					 + " 	patDtls.company_name as \"companyName\", " 
					 + " 	patDtls.company_address as \"companyAddress\", " 
					 + " 	patDtls.company_country_id as \"companyCountryId\", " 
					 + " 	patDtls.company_state_id as \"companyStateId\", " 
					 + " 	patDtls.company_district_id as \"companyDistrictId\", " 
					 + " 	patDtls.company_city_id as \"companyCityId\", " 
					 + " 	patDtls.company_area_id as \"companyAreaId\", " 
					 + " 	patDtls.company_zip_code as \"companyZipCode\", " 
					 + " 	patDtls.company_mobile_number as \"companyMobileNumber\", " 
					 + " 	patDtls.permanent_address as \"permanentAddress\", " 
					 + " 	patDtls.permanent_country_id as \"permanentCountryId\", " 
					 + " 	patDtls.permanent_state_id as \"permanentStateId\", " 
					 + " 	patDtls.permanent_district_id as \"permanentDistrictId\", " 
					 + " 	patDtls.permanent_city_id as \"permanentCityId\", " 
					 + " 	patDtls.permanent_area_id as \"permanentAreaId\", " 
					 + " 	patDtls.permanent_zip_code as \"permanentZipCode\", " 
					 + " 	patDtls.permanent_phone_code as \"permanentPhoneCode\", " 
					 + " 	patDtls.permanent_phone_number as \"permanentPhoneNumber\" " 
					 + " from " 
					 + " 	patient.t_patient_registration patReg " 
					 + " inner join patient.t_patient_details patDtls on " 
					 + " 	patReg.patient_id = patDtls.patient_id " 
					 + " left outer join public.m_prefix_master preFix on " 
					 + " 	patReg.prefix_id = preFix.prefix_id " 
					 + " inner join public.m_gender_master gen on " 
					 + " 	patReg.gender_id = gen.gender_id " 
					 + " left outer join adt.m_identification_master identi on " 
					 + " 	patReg.identification_type_id = identi.identification_id " 
					 + " where " 
					 + " 	lower(patReg.uhid_number) =:uhidNumber and patReg.organization_id=:orgId " 
					 + " 	and patReg.is_primary = 'Y' ");
			Query actualQuery = session.createSQLQuery(stringQuery.toString());
			List<PatientRegistrationDto> patientRegistrationsList = actualQuery
					.setParameter("uhidNumber", dto.getUhidNumber().toLowerCase())
					.setParameter("orgId", dto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(PatientRegistrationDto.class)).list();
			return new Response(SUCCESS, null, null, patientRegistrationsList, null);

		} catch (HibernateException he) {
		logger.error("getPatientDetailsByUhid failed", he);
		} catch (Exception e) {
		logger.error("getPatientDetailsByUhid failed", e);
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response saveCustomPatientDetails(CustomPatientDetailsDto customPatientDetailsObj)
			throws ApplicationException {
	Connection connection =null;
		PreparedStatement statement =  null;
			try {
					connection = driverManager.getConnection();
					connection.setAutoCommit(false);
			        
					if(customPatientDetailsObj.getEncounterId()!=null && customPatientDetailsObj.getAppointmentId()==null) {
						 statement = connection.prepareStatement("INSERT INTO public.t_custom_patient_details_external "
				        		+ "(patient_id, encounter_id,custom_data ,request_payload ,posted_at,response_status ,response_msg,appointment_id,unit_id,organization_id,status,created_date,created_by, updated_date,updated_by, custom_data_post,custom_data_display)"
				        		+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);"); 
				        	statement.setInt(1, customPatientDetailsObj.getPatientId());
				        	statement.setInt(2,customPatientDetailsObj.getEncounterId());
				        	statement.setObject(3, null);
				        	statement.setObject(4,null);
				        	statement.setDate(5, new java.sql.Date(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(customPatientDetailsObj.getUnitId()), "dd-M-yyyy HH:mm:ss").getTime()));
				        	statement.setString(6, null);
				        	statement.setString(7, null);
				        	statement.setInt(8, 0);
				        	statement.setInt(9, customPatientDetailsObj.getUnitId());
				        	statement.setInt(10, customPatientDetailsObj.getOrganizationId());
				        	statement.setString(11, String.valueOf(customPatientDetailsObj.getStatus()));
				        	statement.setDate(12,new java.sql.Date(GlobalCommonDateUtils.getDate(customPatientDetailsObj.getCreatedDate(),"dd-M-yyyy HH:mm:ss").getTime()));
				        	statement.setInt(13, customPatientDetailsObj.getCreatedBy());
				        	statement.setDate(14,new java.sql.Date(GlobalCommonDateUtils.getDate(customPatientDetailsObj.getUpdatedDate(),"dd-M-yyyy HH:mm:ss").getTime()));
				        	statement.setInt(15, customPatientDetailsObj.getUpdatedBy());
				        	statement.setString(16, customPatientDetailsObj.getCustomDataPost());
				        	statement.setString(17, customPatientDetailsObj.getCustomDataDisplay());
							statement.execute();
					}else {
						 statement = connection.prepareStatement("INSERT INTO public.t_custom_patient_details_external "
				        		+ "(patient_id, encounter_id,custom_data ,request_payload ,posted_at,response_status ,response_msg,appointment_id,unit_id,organization_id,status,created_date,created_by, updated_date,updated_by, custom_data_post,custom_data_display)"
				        		+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);"); 
				        	statement.setInt(1, customPatientDetailsObj.getPatientId());
				        	statement.setInt(2,0);
				        	statement.setObject(3, null);
				        	statement.setObject(4,null);
				        	statement.setDate(5, new java.sql.Date(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(customPatientDetailsObj.getUnitId()), "dd-M-yyyy HH:mm:ss").getTime()));
				        	statement.setString(6, null);
				        	statement.setString(7, null);
				        	statement.setInt(8, customPatientDetailsObj.getAppointmentId());
				        	statement.setInt(9, customPatientDetailsObj.getUnitId());
				        	statement.setInt(10, customPatientDetailsObj.getOrganizationId());
				        	statement.setString(11, String.valueOf(customPatientDetailsObj.getStatus()));
				        	statement.setDate(12,new java.sql.Date(GlobalCommonDateUtils.getDate(customPatientDetailsObj.getCreatedDate(),"dd-M-yyyy HH:mm:ss").getTime()));
				        	statement.setInt(13, customPatientDetailsObj.getCreatedBy());
				        	statement.setDate(14,new java.sql.Date(GlobalCommonDateUtils.getDate(customPatientDetailsObj.getUpdatedDate(),"dd-M-yyyy HH:mm:ss").getTime()));
				        	statement.setInt(15, customPatientDetailsObj.getUpdatedBy());
				        	statement.setString(16, customPatientDetailsObj.getCustomDataPost());
				        	statement.setString(17, customPatientDetailsObj.getCustomDataDisplay());
							statement.execute();
					}
						
	
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		}catch (HibernateException he) {
		logger.error("saveCustomPatientDetails failed", he);
		} catch (BatchUpdateException e) {
		logger.error("saveCustomPatientDetails failed", e);
		}
			catch (Exception e) {
			logger.error("saveCustomPatientDetails failed", e);
			}finally{
			try {
				if (statement != null) {
					statement.close();
				}
				connection.setAutoCommit(true);
				connection.close();
			} catch (SQLException e) {
			logger.error("saveCustomPatientDetails failed", e);
			}
		}

		return new Response(ERROR, null, null, null,null);
	}

	@Override
	public Response getPatientInfoForHospitalVisit(PatientRegistrationDto patientRegistrationDto)
			throws ApplicationException {
		try {
			Session session = sessionFactory.getCurrentSession();
			StringBuilder stringQuery=new StringBuilder( " select " 
					 + " 	patReg.patient_id as \"patientId\", " 
					 + " 	patReg.uhid_number as \"uhidNumber\", " 
					 + " 	preFix.prefix_code as \"prefixCode\", " 
					 + " 	coalesce(patReg.first_name, '') as \"firstName\", " 
					 + " 	coalesce(patReg.middle_name, '') as \"middleName\", " 
					 + " 	coalesce(patReg.last_name, '') as \"lastName\", " 
					 + " 	patReg.image_path as \"imagePath\", " 
					 + " 	gen.gender_name as \"genderCode\", " 
					 + " 	to_char(patReg.dob, 'MM/DD/YYYY') as \"dob\", " 
					 + " 	to_char(patReg.dob, 'DD/MM/YYYY') as \"birthDate\", " 
					 + " 	trim(to_char(extract(year from age(CURRENT_TIMESTAMP, patReg.dob)), '999')) as \"ageString\", " 
					 + " 	patReg.mobile_number as \"mobileNumber\", " 
					 + " 	patReg.country_calling_code as \"countryCallingCode\", " 
					 + " 	patReg.identification_type_id as \"identificationTypeId\", " 
					 + " 	coalesce(identi.identification_name, '') as \"identificationName\", " 
					 + " 	patReg.identification_number as \"identificationNumber\", " 
					 + " 	patReg.email as \"email\", " 
					 + " 	patReg.status as \"status\", " 
					 + " 	to_char(patReg.identification_expiry_date, 'MM/DD/YYYY') as \"identificationExpiryDate\", " 
					 + " 	patDtls.address as \"address\", " 
					 + " 	patDtls.zip_code as \"zipCode\", " 
					 + " cnt.country_name as \"countryName\",  "
					 + " st.state_name as \"stateName\", cty.city_name as \"cityName\", ar.area_name as \"areaName\",  "
					 + " customDetails.custom_data_post as \"customDataPost\", "
					 + " customDetails.custom_data_display as \"customDataDisplay\", "
					 + " customDetails.id as  \"customDetailsId\" "
					 + " from " 
					 + " 	patient.t_patient_registration patReg " 
					 + " inner join patient.t_patient_details patDtls on " 
					 + " 	patReg.patient_id = patDtls.patient_id " 
					 + " left outer join public.m_prefix_master preFix on " 
					 + " 	patReg.prefix_id = preFix.prefix_id " 
					 + " inner join public.m_gender_master gen on " 
					 + " 	patReg.gender_id = gen.gender_id " 
					 + " left outer join adt.m_identification_master identi on patReg.identification_type_id = identi.identification_id " 
					 + " left join public.m_country_master cnt   on cnt.country_id = patdtls.country_id " 
					 + " left join public.m_state_master st on st.state_id = patdtls.state_id " 
					 + " left join public.m_city_master cty  on cty.city_id = patdtls.city_id " 
					 + " left join public.m_area_master ar  on ar.area_id = patdtls.area_id "
					 + " left join public.t_custom_patient_details_external customDetails  on customDetails.patient_id = patReg.patient_id "
					 + " and customDetails.unit_id=:unitId and customDetails.organization_id=:orgId "
					 +"  and customDetails.patient_id=:patientId  and customDetails.encounter_id=:encounterId "
					 + " where " 
					 + " 	patReg.unit_id=:unitId and patReg.organization_id=:orgId  " 
					 + " 	and patReg.patient_id=:patientId ");
			Query actualQuery = session.createSQLQuery(stringQuery.toString());
			PatientRegistrationDto patientRegistrationsList =(PatientRegistrationDto) actualQuery
					.setParameter("orgId", patientRegistrationDto.getOrganizationId())
					.setParameter("unitId", patientRegistrationDto.getUnitId())
					.setParameter("patientId", patientRegistrationDto.getPatientId())
					.setParameter("encounterId", patientRegistrationDto.getEncounterId())
					.setResultTransformer(Transformers.aliasToBean(PatientRegistrationDto.class)).uniqueResult();
			return new Response(SUCCESS, null, null, null, patientRegistrationsList);

		} catch (HibernateException he) {
		logger.error("getPatientInfoForHospitalVisit failed", he);
		} catch (Exception e) {
		logger.error("getPatientInfoForHospitalVisit failed", e);
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateCustomDetails(PatientRegistrationDto patientRegistrationDto) throws ApplicationException {
		try {

			Session session=sessionFactory.getCurrentSession();
			Integer count=session.createSQLQuery("update public.t_custom_patient_details_external set custom_data_post =:customDataPost where id=:id")
					.setParameter("id", patientRegistrationDto.getCustomDetailsId())
					.setParameter("customDataPost",patientRegistrationDto.getCustomDataPost())
					.executeUpdate();
		return new Response(SUCCESS, "200", null, null, count);
	} catch (Exception e) {
	logger.error("updateCustomDetails failed", e);
	return new Response(ERROR, "", "", null, null);
	}
	}
    @Override
    public Response savePatientRegistrationDocDetails(PatientRegistrationDocDetailsDto patientRegistrationDocDetailsDto)
            throws ApplicationException {
        try{    
            Session session=sessionFactory.getCurrentSession();
            
            PatientRegistrationDocDetails mstr = new PatientRegistrationDocDetails();
            mstr.setCreatedDate(GlobalCommonDateUtils.getDate(patientRegistrationDocDetailsDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
            mstr.setDocumentPath(patientRegistrationDocDetailsDto.getDocumentPath());
            mstr.setFileName(patientRegistrationDocDetailsDto.getFileName());
            mstr.setOrganizationId(patientRegistrationDocDetailsDto.getOrganizationId());
            mstr.setPatientId(patientRegistrationDocDetailsDto.getPatientId());
            mstr.setStatus(patientRegistrationDocDetailsDto.getStatus());
            mstr.setUnitId(patientRegistrationDocDetailsDto.getUnitId());
            mstr.setUpdatedDate(GlobalCommonDateUtils.getDate(patientRegistrationDocDetailsDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
            session.save(mstr);
            return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
        }catch (HibernateException he) {
        logger.error("savePatientRegistrationDocDetails failed", he);
        } catch (Exception e) {
        logger.error("savePatientRegistrationDocDetails failed", e);
        }
        return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
    }

    @Override
    public Response getPatientDocumentDetailList(PatientRegistrationDto patientRegistrationDto)
            throws ApplicationException {
        try {
            Session session = sessionFactory.getCurrentSession();
            StringBuilder stringQuery=new StringBuilder(" select document_path as \"documentPath\" , file_name as \"fileName\" ,"
                    + " to_char(created_date,'DD-MM-YYYY') as \"createdDate\"  from patient.t_patient_registration_doc_details  "
                    + " where patient_id = :patientId "
                    + " and organization_id = :orgId and unit_id = :unitId ");
            Query actualQuery = session.createSQLQuery(stringQuery.toString());
            List<PatientRegistrationDocDetailsDto> patientRegistrationDocDetailsList = actualQuery
                    .setParameter("orgId", patientRegistrationDto.getOrganizationId())
                    .setParameter("unitId", patientRegistrationDto.getUnitId())
                    .setParameter("patientId", patientRegistrationDto.getPatientId())
                    .setResultTransformer(Transformers.aliasToBean(PatientRegistrationDocDetailsDto.class)).list();
            return new Response(SUCCESS, null, null, patientRegistrationDocDetailsList, null);

        } catch (HibernateException he) {
        logger.error("getPatientDocumentDetailList failed", he);
        } catch (Exception e) {
        logger.error("getPatientDocumentDetailList failed", e);
        }
        return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
    }

	@Override
	public Response sendPatientSmsFromCoversheet(PatientRegistrationDto patientRegistrationDto)
			throws ApplicationException {
		 try {
			/*
			 * String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"; StringBuilder salt
			 * = new StringBuilder("MT"); Random rnd = new Random(); while (salt.length() <
			 * 20) { int index = (int) (rnd.nextFloat() * SALTCHARS.length());
			 * salt.append(SALTCHARS.charAt(index)); }
			 */
		//String saltStr = salt.toString();
		String saltStr = patientRegistrationDto.getUhidNumber();
		String url="meet.jit.si/"+saltStr;
        String url2="https://meet.jit.si/"+saltStr;

        int i = sessionFactory.getCurrentSession()
				.createSQLQuery("update patient.t_patient_registration set video_call_url =:videoCallUrl where patient_id= :patientId")
				.setParameter("videoCallUrl", url2)
				.setParameter("patientId", patientRegistrationDto.getPatientId())
				.executeUpdate();
		
		
		SmsUtilityConfigInfoDto smsInfo=new SmsUtilityConfigInfoDto();
		smsInfo.setOrganizationId(patientRegistrationDto.getOrganizationId());
		smsInfo.setUnitId(patientRegistrationDto.getUnitId());
		smsInfo.setPatientId(patientRegistrationDto.getPatientId());
		smsInfo.setConsultingDoctorId(patientRegistrationDto.getDoctorId());
		smsInfo.setVideoCallLink(url);
		smsInfo.setDateTime(patientRegistrationDto.getDateTime());
		smsUtilityService.getSmsUtilityByTriggerId(ISmsUtilityConstants.VIDEO_CALL,smsInfo);
		
		if(patientRegistrationDto.getEmail()!=null && patientRegistrationDto.getEmail().length()>0) 
		{
			SmsUtilityConfigInfoDto emailinfo = new SmsUtilityConfigInfoDto();
			emailinfo.setUnitId(patientRegistrationDto.getUnitId());
			emailinfo.setOrganizationId(patientRegistrationDto.getOrganizationId());
			emailinfo.setPatientEmail(patientRegistrationDto.getEmail());
			emailinfo.setPatientId(patientRegistrationDto.getPatientId());
			emailinfo.setDateTime(patientRegistrationDto.getDateTime());
			emailinfo.setVideoCallLink(url);
			
			smsUtilityService.getEmailUtilityByTriggerId(ISmsUtilityConstants.VIDEO_CALL, emailinfo);
		}
		
		
		// TODO Auto-generated method stub
		return new Response(SUCCESS, null, null, null, url2);
		 } catch (HibernateException he) {
		 logger.error("sendPatientSmsFromCoversheet failed", he);
		 } catch (Exception e) {
	        logger.error("sendPatientSmsFromCoversheet failed", e);
	        }
		 return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response patientTypeHeadSearchForAppointmentUnique(SlotMasterDto slotMasterDto) throws ApplicationException {

		try {

			StringBuilder stringQuery = null;
			List<PatientRegistrationDto> patientRegistrationDtosList = null;
			stringQuery=new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_PATIENT_DATA_FOR_SELF_PRE_BOOKED_APPOINTMENT_UNIQUE").getQueryString());
			
			stringQuery.append(slotMasterDto.getAppointmentUniqueId()!= null ? " AND (appointment.appointmentUniqueId = "+slotMasterDto.getAppointmentUniqueId()+") " : " ");
			stringQuery.append(" order by appointment.appointmentId desc ");
			patientRegistrationDtosList =sessionFactory.getCurrentSession().createQuery(stringQuery.toString())
						.setParameter("orgId", slotMasterDto.getOrganizationId())
						.setParameter("unitId", slotMasterDto.getUnitId())
						.setDate("currentDate", TimeZoneComponent.getDateByzone(slotMasterDto.getUnitId()))
						.setResultTransformer(Transformers.aliasToBean(PatientRegistrationDto.class)).list();
				
			return new Response(SUCCESS, null, null, patientRegistrationDtosList, null);
		} catch (HibernateException he) {
		logger.error("patientTypeHeadSearchForAppointmentUnique failed", he);
		} catch (Exception e) {
		logger.error("patientTypeHeadSearchForAppointmentUnique failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);

	}

	@Override
	public Response patientTypeHeadSearchForAppointmentUniqueForNurseCheckIn(SlotMasterDto slotMasterDto) throws ApplicationException, Exception {

		try {
			List<SlotMasterDto> slotMasterDtofinal = null;

			String sql=" select mam.patient_id as \"patientId\",mam.created_by as \"assignmentStartBy\", ord.order_details_id as \"orderDetailsId\", " +
					"    TO_CHAR(tem.encounter_date, 'DD-MM-YYYY HH24:MI:SS') as \"assignmentStartTime\", mam.appointment_unique_id as \"appointmentUniqueId\"," +
					"    mam.appointment_id as \"appointmentId\", tem.encounter_id as \"encounterId\" from opd.t_encounter_master tem " +
					"    inner join opd.m_appointment_master mam on tem.appointment_id = mam.appointment_id inner join t_order_master om on " +
					"    om.encounter_id = tem.encounter_id inner join t_order_details ord on om.order_id = ord.order_id where " +
					"    tem.status_id = 5 and mam.appointment_id = :appointmentId and tem.organization_id = :orgId and tem.unit_id = :unitId " +
					"    and  ord.is_consultation_service = 'Y'  order by mam.updated_date desc ";

			slotMasterDtofinal =sessionFactory.getCurrentSession().createSQLQuery(sql)
					.setParameter("appointmentId", slotMasterDto.getAppointmentId())
					.setParameter("orgId", slotMasterDto.getOrganizationId())
					.setParameter("unitId", slotMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();

			return new Response(SUCCESS, null, null, slotMasterDtofinal, null);
		} catch (HibernateException he) {
		logger.error("patientTypeHeadSearchForAppointmentUniqueForNurseCheckIn failed", he);
		} catch (Exception e) {
		logger.error("patientTypeHeadSearchForAppointmentUniqueForNurseCheckIn failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getPatientDetailsListForNewEMR(PatientRegistrationDto patientRegistrationDto)
			throws ApplicationException {

		try {
			String actualQuery= " select " 
					 + " 	enMst.encounter_id as \"encounterId\", " 
					 + " 	enMst.against_enc_id as \"againstEncId\", " 
					 + " 	enMst.encounter_number as \"encounterNumber\", " 
					 + " 	enMst.created_by as \"createdBy\", " 
					 + " 	enMst.encounter_date as \"encounterDate\", " 
					 + " 	(case " 
					 + " 		when am.is_paid_appointment = 'Y' then to_char(am.arrival_time, 'YYYY-MM-DD HH24:MI') " 
					 + " 		else to_char(enMst.encounter_date, 'YYYY-MM-DD HH24:MI') " 
					 + " 	end) as \"encounterDateWithTime\", " 
					 + " 	(case " 
					 + " 		when am.is_paid_appointment = 'Y' then to_char(am.arrival_time, 'DD/MM/YYYY HH24:MI:SS') " 
					 + " 		else to_char(enMst.encounter_date, 'DD/MM/YYYY HH24:MI:SS') " 
					 + " 	end) as \"encounterDateWithTimeString\", " 
					 + " 	(case " 
					 + " 		when am.is_paid_appointment = 'Y' then to_char(am.arrival_time, 'HH24:MI:SS') " 
					 + " 		else to_char(enMst.encounter_date, 'HH24:MI:SS') " 
					 + " 	end) as \"encounterTime\", " 
					 + " 	enMst.user_defined_visit_type_id as \"userDefinedVisitTypeId\" , " 
					 + " 	enMst.status as \"status\", " 
					 + " 	enMst.status_id as \"statusId\", " 
					 + " 	enMst.is_called as \"isCalled\", " 
					 + " 	enMst.call_count as \"callCount\", " 
					 + " 	enMst.cabin_id as \"cabinId\", " 
					 + " 	enMst.doctor_desk_arrival as \"doctorDeskArrival\", " 
					 + " 	patient.is_vip as \"isVip\", " 
					 + " 	patient.first_name as \"firstName\", " 
					 + " 	patient.middle_name as \"middleName\", " 
					 + " 	patient.uuid as \"uuid\", " 
					 + " 	patient.last_name as \"lastName\", " 
					 + " 	patient.patient_remark as \"patientRemark\", " 
					 + " 	(case " 
					 + " 		when patient.is_vip = 'Y' then coalesce(patient.alise_name, patient.first_name) " 
					 + " 		else concat(coalesce(patient.first_name) , ' ', coalesce(patient.middle_name) , ' ', coalesce(patient.last_name)) " 
					 + " 	end ) as \"patientName\", " 
					 + " 	to_char(sm.appointment_date, 'dd/MM/yyyy') as \"appointmentDate\", "
					 + " 	am.appointment_id as \"appointmentId\", " 
					 + " 	sm.slot_id as \"slotId\", " 
					 + " 	concat(to_char(sm.from_time, 'HH24:MI')) as \"fromTimeString\", " 
					 + " 	concat(to_char(sm.to_time, 'HH24:MI')) as \"toTimeString\", " 
					 + " 	am.remark as \"remark\", " 
					 + " 	enMst.doctor_id as \"doctorId\", " 
					 + " 	spd.payment_entitlement_id as \"paymentEntitlementTypeId\", " 
					 + " 	am.appointment_type_id as \"appointmentTypeId\", " 
					 + " 	enMst.encounter_type_id as \"encounterTypeId\", " 
					 + "    to_char(enMst.follow_up_date, 'dd-MM-yyyy HH:MI AM') as \"followUpDate\", "
					 + " 	am.appointment_status_id as \"appointmentStatusId\", " 
					 + " 	asm.appointment_status_desc as \"appointmentStatusDesc\", " 
					 + " 	concat('Dr. ', coalesce(doc.first_name), ' ', coalesce(doc.middle_name), ' ', coalesce(doc.last_name)) as \"doctorName\", " 
					 + " 	concat('Dr. ', coalesce(doc.first_name), ' ' , coalesce(doc.last_name)) as \"docNameTemp\", " 
					 + " 	enMst.dept_id as \"specialityId\", " 
					 + " 	speciaityMaster.speciality_name as \"specialityName\", " 
					 + " 	concat(extract(year from age(current_timestamp, patient.dob)) , 'Y ', extract(month from age(current_timestamp, patient.dob)) , 'M ' , extract(day from age(current_timestamp, patient.dob)), 'D ') as \"age\", " 
					 + " 	concat(extract(year from age(current_timestamp, patient.dob))) as \"ageYear\", " 
					 + " 	patient.mobile_number as \"mobileNumber\", " 
					 + " 	patient.email as \"email\", "
					 + "    patient.video_call_url as \"videoCallUrl\", " 
					 + " 	doc.email as \"emailDoctor\", " 
					 + " 	patient.patient_id as \"patientId\", " 
					 + " 	patient.uhid_number as \"uhidNumber\", " 
					 + " 	patient.prefix_id as \"prefixId\", " 
					 + " 	prefix.prefix_code as \"prefixCode\", " 
					 + " 	patient.gender_id as \"genderId\", " 
					 + " 	patient.image_path as \"imagePath\", " 
					 + " 	patient.patient_category_id as \"patientCategoryId\", " 
					 + " 	patient.mpshah_patient_id as \"mpshahPatientId\", " 
					 + " 	gender.gender_code as \"genderCode\", " 
					 + " 	gender.gender_name as \"genderDec\", " 
					 + " 	to_char(patient.dob, 'DD/MM/YYYY') as \"dob\", " 
					 + " 	to_char(patient.dob, 'MM/DD/YYYY') as \"dobForDatePicker\", " 
					 + " 	sm.modality_id as \"modalityId\", " 
					 + " 	enDet.is_assignment_comp_status as \"isAssignmentCompStatus\", " 
					 + " 	enDet.is_consultation_status as \"isConsultationStatus\", " 
					 + " 	to_char(enDet.consultation_start_time, 'HH24:MI:SS') as \"consultationStartTime\", " 
					 + " 	to_char(enDet.consultation_start_time, 'YYYY-MM-DD HH24:MI') as \"consultationStartTimeWithTime\", " 
					 + " 	to_char(enDet.consultation_stop_time, 'HH24:MI:SS') as \"consultationStopTime\", " 
					 + " 	to_char(enDet.consultation_start_time, 'dd-MM-yyyy HH:MM:SS') as \"consultationStartTimeDate\", " 
					 + " 	to_char(enDet.consultation_stop_time, 'dd-MM-yyyy HH:MM:SS') as \"consultationStopTimeDate\", " 
					 + " 	adm.admission_id as \"admissionId\", " 
					 + " 	to_char(enDet.assignment_start_time, 'HH:MM:SS') as \"assignmentStartTime\", " 
					 + " 	to_char(enDet.assignment_start_time, 'dd-MM-yyyy HH:MM:SS') as \"assignmentStartTimeDate\", " 
					 + " 	to_char(enDet.assignment_stop_time, 'HH:MM:SS') as \"assignmentStopTime\", " 
					 + " 	to_char(enDet.assignment_stop_time, 'dd-MM-yyyy HH:MM:SS') as \"assignmentStopTimeDate\", " 
					 + " 	enDet.assignment_start_by as \"assignmentStartBy\", " 
					 + " 	enDet.assignment_stop_by as \"assignmentStopBy\", " 
					 + " 	enDet.assignment_start_role_id as \"assignmentStartRoleId\", " 
					 + " 	enDet.assignment_stop_role_id as \"assignmentStopRoleId\", " 
					 + " 	enDet.consultation_start_by as \"consultationStartBy\", " 
					 + " 	enDet.consultation_start_role_id as \"consultationStartRoleId\", " 
					 + " 	enDet.encounter_details_mapper_id as \"encounterDetailsMapperId\", " 
					 + " 	spd.contract_id as \"contractId\", " 
					 + " 	spd.payee_id as \"payeeId\", " 
					 + " 	enMst.doc_queue_number as \"queueNumber\", " 
					 + " 	enMst.unit_token_number as \"tokenNumber\", " 
					 + " 	enDet.is_priority as \"isPriority\", " 
					 + " 	enMst.is_critical as \"isCritical\", " 
					 + " 	doc.allowed_over_booking_slots as \"allowedOverBookingSlots\", " 
					 + " 	enMst.speciality_queue_number as \"specialityQueueNumber\", " 
					 + " 	speciaitymapper.dept_wise_appointment as \"deptWiseAppointment\", " 
					 + " 	enMst.old_doc_id as \"oldDocId\", " 
					 + " 	(case " 
					 + " 		when (enMst.is_critical = 'Y' " 
					 + " 		or enDet.is_priority = 'Y') then 1 " 
					 + " 		when (enMst.is_critical = 'N' " 
					 + " 		or enDet.is_priority is null) then 2 " 
					 + " 	end) as \"isCriticalOrder\", " 
					 + " 	(case " 
					 + " 		when enDet.is_priority = 'Y' then 1 " 
					 + " 		when enDet.is_priority is null then 2 " 
					 + " 	end) as \"isPriorityOrder\", " 
					 + " 	patient.whatsapp_no as \"whatsAppCallingNo\", " 
					 + " 	patient.whatsapp_calling_code as \"whatsAppCallingCode\", " 
					 + " 	patient.skype_id as \"skypeId\", " 
					 + " 	am.is_teleconsultation_apppointment as \"isTeleconsultationAppointment\" " 
					 + " from " 
					 + " 	opd.t_encounter_master enMst " 
					 + " inner join opd.t_encounter_details enDet on " 
					 + " 	enDet.encounter_id = enMst.encounter_id " 
					 + " inner join patient.t_patient_registration patient on " 
					 + " 	enMst.patient_id = patient.patient_id " 
					 + " inner join public.m_gender_master gender on " 
					 + " 	patient.gender_id = gender.gender_id " 
					 + " inner join public.m_prefix_master prefix on " 
					 + " 	patient.prefix_id = prefix.prefix_id " 
					 + " left join opd.m_appointment_master am on " 
					 + " 	am.appointment_id = enMst.appointment_id " 
					 + " left join opd.m_slot_master sm on " 
					 + " 	am.slot_id = sm.slot_id " 
					 + " left join opd.t_patient_sponsor_details spd on " 
					 + " 	spd.encounter_id = enMst.encounter_id " 
					 + " 	and spd.priority_id = 1 " 
					 + " 	and spd.status = 'A' " 
					 + " left join doctor.m_speciality_master speciaityMaster on " 
					 + " 	enMst.dept_id = speciaityMaster.speciality_id " 
					 + " left join t_unit_speciality_mapper speciaityMapper on " 
					 + " 	enMst.dept_id = speciaityMapper.speciality_id " 
					 + " 	and speciaityMapper.unit_id = enMst.unit_id " 
					 + " left  join doctor.m_doctor_master doc on " 
					 + " 	enMst.doctor_id = doc.doctor_id " 
					 + " left join opd.m_appointment_status_master asm on " 
					 + " 	asm.appointment_status_id = enMst.status_id " 
					 + " left  join adt.t_admission adm on " 
					 + " 	adm.admission_id = am.admission_id " 
					 + " where " 
					 + " 	enMst.organization_id =:orgId " 
					 + " 	and enMst.unit_id =:unitId " 
					 + " 	and enMst.patient_id =:patientId " 
					 + " 	and enMst.doctor_id =:doctorId " 
					 + " 	and (enMst.status_id in (5,6,9)  " 
					 + " 	or (am.is_teleconsultation_apppointment = 1   and enMst.status_id in (5,6,9))) " 
					 + " 	and patient.registration_type_id in (1,3,4,6) " 
					 + " 	and am.appointment_type_id in (1,5,6,9) " ;
			StringBuffer subQuery = new StringBuffer();		
			if(patientRegistrationDto.getEncounterId()!=null){
				subQuery.append(" and enMst.encounter_id="+patientRegistrationDto.getEncounterId());
			}
			actualQuery = actualQuery.concat(subQuery.toString());			
			List<PatientRegistrationDto> patientRegistrationsList = sessionFactory.getCurrentSession().createSQLQuery(actualQuery)
					.setParameter("patientId", patientRegistrationDto.getPatientid())
					.setParameter("doctorId", patientRegistrationDto.getDoctorId())
					.setParameter("unitId", patientRegistrationDto.getUnitId())
					.setParameter("orgId", patientRegistrationDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
			return new Response(SUCCESS, null, null, patientRegistrationsList, null);

		} catch (HibernateException he) {
		logger.error("getPatientDetailsListForNewEMR failed", he);
		} catch (Exception e) {
		logger.error("getPatientDetailsListForNewEMR failed", e);
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);

	}

	@Override
	public Response savePatientRemarksForNewEMR(PatientRegistrationDto patientRegistrationDto)
			throws ApplicationException {
		try {

			Session session=sessionFactory.getCurrentSession();
					session.createSQLQuery("update  patient.t_patient_registration  set patient_remark ='"+patientRegistrationDto.getPatientRemark()+"'"
					+" where patient_id=:id and unit_id=:unitId and organization_id =:orgId ")
					.setParameter("id", patientRegistrationDto.getPatientId())
					.setParameter("unitId", patientRegistrationDto.getUnitId())
					.setParameter("orgId", patientRegistrationDto.getOrganizationId())					
					.executeUpdate();
		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
	} catch (Exception e) {
	logger.error("savePatientRemarksForNewEMR failed", e);
	return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	}

	@Override
    public Response checkSqlConnection(PatientRegistrationDto patientRegistrationDto) throws ApplicationException, Exception  {

    	String countryName = "";
    	String userName = "";
    	String stateName1 = "";
    	String cityName = "";
    	String mariatalStatus = "";
    	String nationality ="";
    	String DOB = "";
    	String religion = "";
    	String international = "";
    	String user_Name = "";
    	
        Session session = sessionFactory.getCurrentSession();
        SQLQuery query = session.createSQLQuery(
                "select mysql_onp from public.m_unit_master where unit_id = '"
                        + patientRegistrationDto.getUnitId() + "' AND status='A' limit 1");
		Object result = query.uniqueResult();

		String mysqlSmartJdbc = result != null ? result.toString() : null;

		SQLQuery queryForURL = session.createSQLQuery(
				"select patient_reg_local_url from public.m_unit_master where unit_id = '"
						+ patientRegistrationDto.getUnitId() + "' AND status='A' limit 1");
		Object result1 = queryForURL.uniqueResult();

		String localURL = result1 != null ? result1.toString() : null;
        
        if(mysqlSmartJdbc != null && mysqlSmartJdbc != "" && localURL!= null && !localURL.trim().isEmpty()){
        	
            
            if(patientRegistrationDto.getCreatedBy()!=null){
         
            
            	List<PatientRegistrationDto> list = sessionFactory.getCurrentSession().createSQLQuery(
            		"select 1 as \"id\", mum.user_name as \"name\"   from m_user_master mum  where user_id  = " + patientRegistrationDto.getCreatedBy()  
            				+ " and unit_id = "+patientRegistrationDto.getUnitId()+" and organization_id = "+patientRegistrationDto.getOrganizationId()   
                    		+ " union  all  "
                    		+ "select 2 as \"id\",mcm.country_name as \"name\"  from m_country_master mcm  where mcm.country_id  = "+patientRegistrationDto.getCountryId() 
                    		+ " union all "
                    		+ "select 3 as \"id\", msm.state_code as \"name\" from m_state_master msm where msm.state_id = "+patientRegistrationDto.getStateId()  
                    		+ " union all "
                    		+ "select 4 as \"id\", mcim.city_name as \"name\" from m_city_master mcim where mcim.city_id = "+patientRegistrationDto.getCityId() 
                    		+ " union all "
                    		+ "select 5 as \"id\", mssm.marital_status_name as \"name\" from m_marital_status_master mssm where mssm.marital_status_id = "+patientRegistrationDto.getMaritalStatusId() 
                    		+ " union all "
                    		+ "select 6 as \"id\", mssm.nationality_name as \"name\" from m_nationality_master mssm where mssm.nationality_id = "+patientRegistrationDto.getNationalityId()
                    		+ " union all "
                    		+ "select 7 as \"id\", mrm.race_name as \"name\"  from m_race_master mrm  where mrm.race_id = "+patientRegistrationDto.getRaceId()
                    		+ " union all "
                    		+ "select 8 as \"id\", (case when mpcm.is_foreginer_category ='Y' then 'Y' else 'N' end )as \"name\" from m_patient_category_master mpcm where mpcm.patient_category_id ="+ patientRegistrationDto.getPatientCategoryId()
                			+ " union all "
                			+ "select 9 as \"id\", (case when mdm.first_name is not null then (mdm.first_name) else (mem.first_name) end ) as \"name\" "
                			+ " from m_user_master mum"
                			+ " left join doctor.m_doctor_master mdm on mdm.user_id = mum.user_id "
                			+ " left join m_employee_master mem on mem.user_id = mum.user_id where mum.user_id = "+ patientRegistrationDto.getCreatedBy())
            			
            		.setResultTransformer(Transformers.aliasToBean(PatientRegistrationDto.class)).list();
            	if(list != null && list.size()>1)
            	{
            		for (PatientRegistrationDto patientRegistrationDto2 : list) {
    					if(patientRegistrationDto2.getId() == 1){
    						userName = patientRegistrationDto2.getName();
    					}
    					if(patientRegistrationDto2.getId() == 2){
    						countryName = patientRegistrationDto2.getName();
    					}
    					if(patientRegistrationDto2.getId() == 3){
    						stateName1 = patientRegistrationDto2.getName();
    					}
    					if(patientRegistrationDto2.getId() == 4){
    						cityName = patientRegistrationDto2.getName();
    					}
    					if(patientRegistrationDto2.getId() == 5){
    						mariatalStatus = patientRegistrationDto2.getName();
    					}
    					if(patientRegistrationDto2.getId() == 6){
    						nationality = patientRegistrationDto2.getName();
    					}
    					if(patientRegistrationDto2.getId() == 7){
    						religion = patientRegistrationDto2.getName();
    					}
    					if(patientRegistrationDto2.getId() == 8){
    						international = (patientRegistrationDto2.getName());
    					}
    					if(patientRegistrationDto2.getId() == 9){
    						user_Name = (patientRegistrationDto2.getName());
    						if(user_Name.length()>10){
    							user_Name = user_Name.substring(0, 10);
    						}
    						
    					}
    					
    				}
            		
            	}
            }
            
            
        String[] connStr = mysqlSmartJdbc.split(",");
        String firststr = connStr[0];
        String secondStr = connStr[1];
        String thirdStr = connStr[2];
        
        Date date = TimeZoneComponent.getDateByzone(patientRegistrationDto.getUnitId());  
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd" );  
        String strDate= formatter.format(date);
        
        

        String dateString = patientRegistrationDto.getDob();
        
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            Date birth = sdf.parse(dateString);
        
        SimpleDateFormat formatterdob = new SimpleDateFormat("yyyy-MM-dd");
        DOB = formatterdob.format(birth);
        
        }catch(Exception e){
        	logger.error("error while converting dob");
            
        }
        
        String skypeString = null;
        SQLQuery query3 = session.createSQLQuery(" select skype_id from patient.t_patient_registration tpr where patient_id="+patientRegistrationDto.getPatientId()+" ");
        String skypeId = query3.uniqueResult().toString();
        if(skypeId != null && skypeId != "") {
        	skypeString = skypeId;
        }
        
        int genderId    = patientRegistrationDto.getGenderId();
        String gender = "";
        if(genderId >0){
        	
        	if(genderId==1)
                gender="M";
            
            if(genderId==2)
            gender="F";
            
            if(genderId==3)
                gender="O";
            
        }
        
        char vip = patientRegistrationDto.getIsVip();
        String isVIP = String.valueOf(vip);
        
        if(isVIP.equalsIgnoreCase("Y")){
        	isVIP = "V";
        }
        else{
        	isVIP = "N";
        }
        
        String input2 = stateName1;
        String stateName = "";  

        if (input2.length() > 3) {
        	stateName = input2.substring(0, 3);
        } else {
        	stateName = input2;
        }
        
        String telephoneNo ="";
        
        if(patientRegistrationDto.getPhoneNumber()!= ""){
        	
        	telephoneNo = patientRegistrationDto.getPhoneCode();
        	telephoneNo = telephoneNo + "-" + patientRegistrationDto.getPhoneNumber();
        	
        }else{
        	
        	telephoneNo = "";
        }
        
//          String patientCode = String.valueOf(patientRegistrationDto.getUnitId());
//        Patient code changes done with suggestion of Prasad Sir by Avishkar.
        
        
        String newdata;
        String name = null;
        String lastName = null;
        String middleName=null;
        String address=null;
        
        if(patientRegistrationDto.getFirstName() != null) {
      	   name =patientRegistrationDto.getFirstName().replaceAll("'", "''");
        }
        if(patientRegistrationDto.getLastName() != null) {
      	  lastName =patientRegistrationDto.getLastName().replaceAll("'", "''");
        }
        if(patientRegistrationDto.getMiddleName() != null) {
      	  middleName =patientRegistrationDto.getMiddleName().replaceAll("'", "''");
        }
        if(patientRegistrationDto.getAddress() != null) {
      	  address =patientRegistrationDto.getAddress().replaceAll("'", "''");
        }
      
      		
        newdata = "INSERT INTO shreedb.dbo.patient_master (patient_code, register_on, [ref] , patient_name, patient_surname, patient_mname, sex,alive, "
                + "address, city , pin, jahangir_id, marital_status, birth_date, telephone, religion, email_id, mobile, vip, nationality, international, country, state,"
                + "create_on, create_by, RegistrationNo, userid)"
                + "VALUES('"+patientRegistrationDto.getUhidNumber()+"', '"+strDate+"', N'"+skypeString+"',"
                + " N'"+name+"', N'"+lastName+"',"
                + " N'"+middleName+"', N'"+ gender +"','A',"
                + " N'"+address+"', N'"+cityName+"',"
                + " N'"+patientRegistrationDto.getZipCode()+"', "+ patientRegistrationDto.getUnitId()+","
                + " N'"+mariatalStatus+"', '"+ DOB +"', '"+ telephoneNo +"', '"+ religion +"'," 
                + " N'"+patientRegistrationDto.getEmail()+"', N'"+patientRegistrationDto.getMobileNumber()+"',"
                + " N'"+isVIP+"', N'"+nationality+"', '"+ international +"',"
                + " N'"+countryName+"', '"+ stateName +"',"
                + " N'"+strDate+"', N'"+userName+"', N'"+ patientRegistrationDto.getUhidNumber() +"', N'"+ user_Name +"');";
        //to_char(pdmm.birth_date, 'YYYY/MM/DD') as \"dob\"
/*        newdata ="INSERT INTO shreedb.dbo.patient_master_dummy"
                 +"(patient_code, register_on, patient_name, patient_surname, patient_mname, sex, address, city, state, pin, ref_doc_code, remarks, telephone, birth_date, age, marital_status, social_sec_no, updt_by, updt_date, age1, sex_code, email_id, mobile, jahangir_id, sumati_id, shree_id, update_on, category, vip, prefered_comm, occupation, qualification, next_of_kin, doctors_name_address, nationality, religion, insurance_co, tpa, policy_no, information_provided_by, concession, flag, [ref], baby_of, problem, alive, sage, international, country, userid, id_created, id_issued, location, company, bed_wetting, online, create_on, create_by, RegistrationNo, pancard_no, adharcard_no, passport_no)"
                 +"VALUES(N'10', '2019-12-29 00:00:00.000', N'Tasneem', N'Taruuadi', N'Alihussain', N'F', N'Flat no. 3053rd floor,Khadimachin chowk,Kondhwa,Pune,Maharashtra', N'Pune', N'001', N'411048 ', NULL, NULL, NULL, '2022-05-11 00:00:00.000', NULL, N'Married', NULL, N'sheetal ', NULL, NULL, NULL, N'meera.opd@onphospital.org', N'9106924806', NULL, NULL, NULL, '2020-06-25 12:32:30.680', NULL, N'N', NULL, N' ', NULL, NULL, NULL, N'Indian ', N'Hindu ', NULL, NULL, NULL, NULL, NULL, NULL, N'Dr. Zohra Siamwala. :Family Doctor', NULL, NULL, N'A', NULL, N'N', N'India', N'sheetal', N'N', N'N', N'Kondhwa', N'008', NULL, NULL, '2019-12-29 16:41:16.220', N'pritee', N'OMRZZSGYE190147', NULL, NULL, NULL);";
*/
        NewONPRegistrationDto newONPRegistrationDto = new NewONPRegistrationDto();
        newONPRegistrationDto.setDbconnection(firststr);
        newONPRegistrationDto.setDbUser(secondStr);
        newONPRegistrationDto.setDbPassword(thirdStr);
        newONPRegistrationDto.setConnectionString (newdata);
        
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("connectionString", newdata);
        jsonObject.put("dbconnection", firststr);
        jsonObject.put("dbUser", secondStr);
        jsonObject.put("dbPassward", thirdStr);
        jsonArray.put(jsonObject);

        String requestJson =  jsonObject.toString();
        OkHttpClient client = new OkHttpClient().newBuilder()
                  .build();
                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType,requestJson);
                Request request = new Request.Builder()
                  .url(localURL.trim())
                  .method("POST", body)
                  .addHeader("Content-Type", "application/json")
                  .build();
               // okhttp3.Response response = client.newCall(request).execute();
                try (okhttp3.Response response = client.newCall(request).execute()){
                	logger.info(response.code());
                	logger.info(response.message());
                	logger.info(response.isSuccessful());
                }
                
        
        return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
      }else{
          return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
        }
        
    }



	@Override
	public Response checkSqlConnectionForUpdate(PatientRegistrationDto patientRegistrationDto)
			throws ApplicationException, Exception {

    	String countryName = "";
    	String userName = "";
    	String stateName1 = "";
    	String cityName = "";
    	String mariatalStatus = "";
    	String nationality ="";
    	String DOB = "";
    	String religion = "";
    	String international = "";
    	String user_Name = "";
    	
    	
        Session session = sessionFactory.getCurrentSession();
        SQLQuery query = session.createSQLQuery(
                "select mysql_onp from public.m_unit_master where unit_id = '"
                        + patientRegistrationDto.getUnitId() + "' AND status='A' limit 1");
		Object result = query.uniqueResult();

		String mysqlSmartJdbc = result != null ? result.toString() : null;

		SQLQuery queryForURL = session.createSQLQuery(
				"select patient_reg_local_url from public.m_unit_master where unit_id = '"
						+ patientRegistrationDto.getUnitId() + "' AND status='A' limit 1");
		Object result1 = queryForURL.uniqueResult();

		String localURL = result1 != null ? result1.toString() : null;


		if(mysqlSmartJdbc != null && mysqlSmartJdbc != "" && localURL!= null && !localURL.trim().isEmpty()){
        	
            
            if(patientRegistrationDto.getCreatedBy()!=null){
         
            
            	List<PatientRegistrationDto> list = sessionFactory.getCurrentSession().createSQLQuery(
            		"select 1 as \"id\", mum.user_name as \"name\"   from m_user_master mum  where user_id  = " + patientRegistrationDto.getCreatedBy()  
            				+ " and unit_id = "+patientRegistrationDto.getUnitId()+" and organization_id = "+patientRegistrationDto.getOrganizationId()   
                    		+ " union  all  "
                    		+ "select 2 as \"id\",mcm.country_name as \"name\"  from m_country_master mcm  where mcm.country_id  = "+patientRegistrationDto.getCountryId() 
                    		+ " union all "
                    		+ "select 3 as \"id\", msm.state_code as \"name\" from m_state_master msm where msm.state_id = "+patientRegistrationDto.getStateId()  
                    		+ " union all "
                    		+ "select 4 as \"id\", mcim.city_name as \"name\" from m_city_master mcim where mcim.city_id = "+patientRegistrationDto.getCityId() 
                    		+ " union all "
                    		+ "select 5 as \"id\", mssm.marital_status_name as \"name\" from m_marital_status_master mssm where mssm.marital_status_id = "+patientRegistrationDto.getMaritalStatusId() 
                    		+ " union all "
                    		+ "select 6 as \"id\", mssm.nationality_name as \"name\" from m_nationality_master mssm where mssm.nationality_id = "+patientRegistrationDto.getNationalityId()
                    		+ " union all "
                    		+ "select 7 as \"id\", mrm.race_name as \"name\"  from m_race_master mrm  where mrm.race_id = "+patientRegistrationDto.getRaceId()
                    		+ " union all "
                    		+ "select 8 as \"id\", (case when mpcm.is_foreginer_category ='Y' then 'Y' else 'N' end )as \"name\" from m_patient_category_master mpcm where mpcm.patient_category_id ="+ patientRegistrationDto.getPatientCategoryId()
            				+ " union all "
            				+ "select 9 as \"id\", (case when mdm.first_name is not null then (mdm.first_name) else (mem.first_name) end )as \"name\" "
            				+ " from m_user_master mum "
            				+ " left join doctor.m_doctor_master mdm on mdm.user_id = mum.user_id "
            				+ " left join m_employee_master mem on mem.user_id = mum.user_id where mum.user_id = "+ patientRegistrationDto.getCreatedBy())
                    		
            		.setResultTransformer(Transformers.aliasToBean(PatientRegistrationDto.class)).list();
            	if(list != null && list.size()>1)
            	{
            		for (PatientRegistrationDto patientRegistrationDto2 : list) {
    					if(patientRegistrationDto2.getId() == 1){
    						userName = patientRegistrationDto2.getName();
    					}
    					if(patientRegistrationDto2.getId() == 2){
    						countryName = patientRegistrationDto2.getName();
    					}
    					if(patientRegistrationDto2.getId() == 3){
    						stateName1 = patientRegistrationDto2.getName();
    					}
    					if(patientRegistrationDto2.getId() == 4){
    						cityName = patientRegistrationDto2.getName();
    					}
    					if(patientRegistrationDto2.getId() == 5){
    						mariatalStatus = patientRegistrationDto2.getName();
    					}
    					if(patientRegistrationDto2.getId() == 6){
    						nationality = patientRegistrationDto2.getName();
    					}
    					if(patientRegistrationDto2.getId() == 7){
    						religion = patientRegistrationDto2.getName();
    					}
    					if(patientRegistrationDto2.getId() == 8){
    						international = (patientRegistrationDto2.getName());
    					}
    					if(patientRegistrationDto2.getId() == 9){
    						user_Name = (patientRegistrationDto2.getName());
    						
    						if(user_Name.length()>10)
    						{
    							user_Name = user_Name.substring(0, 10);
    						}
    						
    					}
    					
    				}
            		
            	}
            }
            
        String[] connStr = mysqlSmartJdbc.split(",");
        String firststr = connStr[0];
        String secondStr = connStr[1];
        String thirdStr = connStr[2];
        
        Date date = TimeZoneComponent.getDateByzone(patientRegistrationDto.getUnitId());  
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd" );  
        String strDate= formatter.format(date);
        String dateString = patientRegistrationDto.getDob();
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            Date birth = sdf.parse(dateString);
        
            SimpleDateFormat formatterdob = new SimpleDateFormat("yyyy-MM-dd");
            DOB = formatterdob.format(birth);
        
        }catch(Exception e){
        	logger.error("error while converting dob");
            
        }
        
        int genderId    = patientRegistrationDto.getGenderId();
        String gender = "";
        if(genderId >0){
        	
        	if(genderId==1)
                gender="M";
            
            if(genderId==2)
            gender="F";
            
            if(genderId==3)
                gender="O";
            
        }
        
        char vip = patientRegistrationDto.getIsVip();
        String isVIP = String.valueOf(vip);
        
        if(isVIP.equalsIgnoreCase("Y")){
        	isVIP = "V";
        }
        else{
        	isVIP = "O";
        }
        
        String input2 = stateName1;
        String stateName = "";  

        if (input2.length() > 3) {
        	stateName = input2.substring(0, 3);
        } else {
        	stateName = input2;
        }
        
        String telephoneNo ="";
        
        if(patientRegistrationDto.getPhoneNumber()!= ""){
        	
        	telephoneNo = patientRegistrationDto.getPhoneCode();
        	telephoneNo = telephoneNo + "-" + patientRegistrationDto.getPhoneNumber();
        	
        }else{
        	
        	telephoneNo = "";
        }
//          String patientCode = String.valueOf(patientRegistrationDto.getUnitId());
          String newdata;
          String newdataSelect;
          String newdataUpdate;
          String newdataInsert;
          String name = null;
          String lastName = null;
          String middleName=null;
          String address=null;
          
          if(patientRegistrationDto.getFirstName() != null) {
        	   name =patientRegistrationDto.getFirstName().replaceAll("'", "''");
          }
          if(patientRegistrationDto.getLastName() != null) {
        	  lastName =patientRegistrationDto.getLastName().replaceAll("'", "''");
          }
          if(patientRegistrationDto.getMiddleName() != null) {
        	  middleName =patientRegistrationDto.getMiddleName().replaceAll("'", "''");
          }
          if(patientRegistrationDto.getAddress() != null) {
        	  address =patientRegistrationDto.getAddress().replaceAll("'", "''");
          }
       
          
      //insert statement
          newdataSelect = "select * from patient_master pmd where patient_code ='"+patientRegistrationDto.getUhidNumber()+"'";
          
       /*   String skypeString = null;
          SQLQuery query3 = session.createSQLQuery(" select skype_id from patient.t_patient_registration tpr where patient_id="+patientRegistrationDto.getPatientId()+" ");
          String skypeId = query3.uniqueResult().toString();
          if(skypeId != null && skypeId != "") {
          	skypeString = skypeId;
          }*/
          
      //insert statement
      newdataInsert = "INSERT INTO shreedb.dbo.patient_master (patient_code, register_on, [ref] , patient_name, patient_surname, patient_mname, sex, alive,"
              + "address, city , pin, jahangir_id, marital_status, birth_date, telephone, religion, email_id, mobile, vip, nationality, international, country, state,"
              + "create_on, create_by, RegistrationNo, userid)"
              + "VALUES('"+patientRegistrationDto.getUhidNumber()+"', '"+strDate+"', N'"+patientRegistrationDto.getSkypeId()+"',"
              + " N'"+name+"', N'"+lastName+"',"
              + " N'"+middleName+"', N'"+ gender +"',N'A',"
              + " N'"+address+"', N'"+cityName+"',"
              + " N'"+patientRegistrationDto.getZipCode()+"', "+ patientRegistrationDto.getUnitId()+","
              + " N'"+mariatalStatus+"', '"+ DOB +"', '"+ telephoneNo +"', '"+ religion +"'," 
              + " N'"+patientRegistrationDto.getEmail()+"', N'"+patientRegistrationDto.getMobileNumber()+"',"
              + " N'"+isVIP+"', N'"+nationality+"', '"+ international +"',"
              + " N'"+countryName+"', '"+ stateName +"',"
              + " N'"+strDate+"', N'"+userName+"', N'"+ patientRegistrationDto.getUhidNumber() +"', N'"+ user_Name +"')";
          
        //update statement
        newdataUpdate  = " UPDATE shreedb.dbo.patient_master SET patient_name ='"+name+"', "
        		+ " patient_surname ='"+lastName+"',"
        		+ " patient_mname = '"+middleName+"',"
        		+ " [ref] = '"+patientRegistrationDto.getSkypeId()+"', "
        		+ " sex = '"+gender+"',"
        		+ " address = '"+address+"',"
        		+ " city = '"+cityName+"',"
        		+ " pin = '"+patientRegistrationDto.getZipCode()+"',"
        		+ " jahangir_id = '"+patientRegistrationDto.getUnitId()+"',"
        		+ " marital_status = '"+mariatalStatus+"',"
        		+ " alive = 'A',"
        		+ " birth_date = '"+DOB+"',"
        		+ " telephone = '"+telephoneNo+"',"
        		+ " religion = '"+religion+"',"
        		+ " email_id = '"+patientRegistrationDto.getEmail()+"',"
        		+ " mobile = '"+patientRegistrationDto.getMobileNumber()+"',"
        		+ " vip = '"+isVIP+"',"
        		+ " nationality = '"+nationality+"',"
        		+ " international = '"+international+"',"
        		+ " country = '"+countryName+"',"
        		+ " state = '"+stateName+"',"
        		+ " update_on = '"+strDate+"',"
        		+ " updt_date = '"+strDate+"',"
        		+ " updt_by = '"+user_Name+"',"
        		+ " RegistrationNo = '"+patientRegistrationDto.getUhidNumber()+"'"
        	    + " where patient_code = '"+patientRegistrationDto.getUhidNumber()+"'";
        
        
       
        
        newdata  = " if exists("+newdataSelect+")" + 
        		" BEGIN " + 
        		" "+newdataUpdate+" "+ 
        		"End " + 
        		"else " + 
        		"begin " + 
        		" "+newdataInsert+" " + 
        		"end";
        
        System.out.println(newdata);
        
        NewONPRegistrationDto newONPRegistrationDto = new NewONPRegistrationDto();
        newONPRegistrationDto.setDbconnection(firststr);
        newONPRegistrationDto.setDbUser(secondStr);
        newONPRegistrationDto.setDbPassword(thirdStr);
        newONPRegistrationDto.setConnectionString (newdata);
        
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("connectionString", newdata);
        jsonObject.put("dbconnection", firststr);
        jsonObject.put("dbUser", secondStr);
        jsonObject.put("dbPassward", thirdStr);
        jsonArray.put(jsonObject);

        String requestJson =  jsonObject.toString();
        OkHttpClient client = new OkHttpClient().newBuilder()
                  .build();
                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType,requestJson);
                Request request = new Request.Builder()
                  .url(localURL.trim())
                  .method("POST", body)
                  .addHeader("Content-Type", "application/json")
                  .build();
                try (okhttp3.Response response = client.newCall(request).execute()){
                	logger.info(response.code());
                	logger.info(response.message());
                	logger.info(response.isSuccessful());
                }
                
        
        return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
      }else{
          return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
        }
        
    }

	@Override
	public Response checkSqlConnectionForUpdatePatientSource(PatientRegistrationDto patientRegistrationDto)
			throws ApplicationException, Exception {
    	
        Session session = sessionFactory.getCurrentSession();
        SQLQuery query = session.createSQLQuery(
                "select mysql_onp from public.m_unit_master where unit_id = '"
                        + patientRegistrationDto.getUnitId() + "' AND status='A' limit 1");
		Object result = query.uniqueResult();

		String mysqlSmartJdbc = result != null ? result.toString() : null;

		SQLQuery queryForURL = session.createSQLQuery(
				"select patient_reg_local_url from public.m_unit_master where unit_id = '"
						+ patientRegistrationDto.getUnitId() + "' AND status='A' limit 1");
		Object result1 = queryForURL.uniqueResult();

		String localURL = result1 != null ? result1.toString() : null;

		if(mysqlSmartJdbc != null && mysqlSmartJdbc != "" && localURL!= null && !localURL.trim().isEmpty()){
        	String patientSourceName ="";
        	if(patientRegistrationDto.getSourceId() != null) {
        	SQLQuery query1 = session.createSQLQuery(
                    " select patient_source_name  from adt.m_patient_source_master where patient_source_id ="+patientRegistrationDto.getSourceId()+" ");
             patientSourceName = query1.uniqueResult().toString();
        	}
            
        	SQLQuery query3 = session.createSQLQuery(" select skype_id from patient.t_patient_registration tpr where patient_id="+patientRegistrationDto.getPatientId()+" ");
        	
            SQLQuery query2 = session.createSQLQuery(
                    " select uhid_number from patient.t_patient_registration tpr where patient_id="+patientRegistrationDto.getPatientId()+" and  unit_id="+patientRegistrationDto.getUnitId()+" ");
            String uhidNmuber = query2.uniqueResult().toString();
            
            
        String[] connStr = mysqlSmartJdbc.split(",");
        String firststr = connStr[0];
        String secondStr = connStr[1];
        String thirdStr = connStr[2];
        String refString = null;
        String skypeString = null;
        
        if(patientSourceName != null && patientSourceName !=""  && patientRegistrationDto.getRemark() != null && patientRegistrationDto.getRemark() != "") {
        	refString = patientSourceName +" : "+patientRegistrationDto.getRemark();
        }else if((patientSourceName == null || patientSourceName =="")  && (patientRegistrationDto.getRemark() != null && patientRegistrationDto.getRemark() != "")) {
        	refString = patientRegistrationDto.getRemark();
        }else if((patientSourceName != null && patientSourceName !=""  ) && (patientRegistrationDto.getRemark() == null || patientRegistrationDto.getRemark() == "")) {
        	refString = patientSourceName;
        }
        
        String skypeId = query3.uniqueResult().toString();
        if(skypeId != null && skypeId != "") {
        	skypeString = skypeId;
        }
        
        
     /*   String sizeString = refString;
        Integer maxSize = 50;
        if(sizeString.length() > maxSize ){
            sizeString = sizeString.substring(0, maxSize);
        }
        */
        
        String newdata;
        System.out.println(patientRegistrationDto);
       /* newdata = " UPDATE shreedb.dbo.patient_master SET [ref] ='"+sizeString+"' "
        	    + " where patient_code = '"+uhidNmuber+"'";*/
        newdata = " UPDATE shreedb.dbo.patient_master SET [ref] ='"+skypeString+"' "
        	    + " where patient_code = '"+uhidNmuber+"'";
        
        NewONPRegistrationDto newONPRegistrationDto = new NewONPRegistrationDto();
        newONPRegistrationDto.setDbconnection(firststr);
        newONPRegistrationDto.setDbUser(secondStr);
        newONPRegistrationDto.setDbPassword(thirdStr);
        newONPRegistrationDto.setConnectionString (newdata);
        
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("connectionString", newdata);
        jsonObject.put("dbconnection", firststr);
        jsonObject.put("dbUser", secondStr);
        jsonObject.put("dbPassward", thirdStr);
        jsonArray.put(jsonObject);

        String requestJson =  jsonObject.toString();
        OkHttpClient client = new OkHttpClient().newBuilder()
                  .build();
                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType,requestJson);
                Request request = new Request.Builder()
                  .url(localURL.trim())
                  .method("POST", body)
                  .addHeader("Content-Type", "application/json")
                  .build();
                try (okhttp3.Response response = client.newCall(request).execute()){
                	logger.info(response.code());
                	logger.info(response.message());
                	logger.info(response.isSuccessful());
                }
                
        
        return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
      }else{
          return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
        }
        
    }

	@Override
	@Transactional
	public Response getPatientMLCDocumentDetailList(PatientRegistrationDto patientRegistrationDto)
			throws ApplicationException, Exception {
		try {
            Session session = sessionFactory.getCurrentSession();
//            StringBuilder stringQuery=new StringBuilder(" select document_path as \"documentPath\" , file_name as \"fileName\" ,"
//                    + " to_char(created_date,'DD-MM-YYYY') as \"createdDate\"  from patient.t_patient_registration_doc_details prdd  "
//                    + " inner join t_unit_application_billing_configuration tuabc on " 
//					+ " 	tuabc.unit_id = prdd.unit_id 	 " 
//                    + " where patient_id = :patientId "
//                    + " and organization_id = :orgId and unit_id = :unitId "
//                    + "order by created_date desc limit 1");
            
            StringBuilder stringQuery=new StringBuilder(" select prdd.document_path as \"documentPath\" , prdd.file_name as \"fileName\" ,"
            		+ "tuabc.bucket_flag as \"bucketFlag\", "
                    + " to_char(prdd.created_date,'DD-MM-YYYY') as \"createdDate\"  from patient.t_patient_registration_doc_details prdd  "
                    + " inner join t_unit_application_billing_configuration tuabc on " 
					+ " 	tuabc.unit_id = prdd.unit_id 	 " 
                    + " where prdd.patient_id = :patientId "
                    + " and prdd.organization_id = :orgId and prdd.unit_id = :unitId "
                    + "order by prdd.created_date desc limit 1");
            
            Query actualQuery = session.createSQLQuery(stringQuery.toString());
            List<PatientRegistrationDocDetailsDto> patientRegistrationDocDetailsList = actualQuery
                    .setParameter("orgId", patientRegistrationDto.getOrganizationId())
                    .setParameter("unitId", patientRegistrationDto.getUnitId())
                    .setParameter("patientId", patientRegistrationDto.getPatientId())
                    .setResultTransformer(Transformers.aliasToBean(PatientRegistrationDocDetailsDto.class)).list();
            return new Response(SUCCESS, null, null, patientRegistrationDocDetailsList, null);

        } catch (HibernateException he) {
		throw new ApplicationException(he.getMessage(), he);
	} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
        
    }

	@Override
	public Long getNextValPatientIdInPoc() throws ApplicationException {
		try {
			Session session = sessionFactory.getCurrentSession();
				SQLQuery query = session.createSQLQuery("select nextval('public.seq_m_patient_poc_master')");
				Long id = ((BigInteger) query.uniqueResult()).longValue();

				SQLQuery query2 = session.createSQLQuery("select setval('public.seq_m_patient_poc_master'," + (id-1) + ")");
				query2.uniqueResult();

				return id;
		} catch (HibernateException he) {
		logger.error("getNextValPatientIdInPoc failed", he);
		} catch (Exception e) {
		logger.error("getNextValPatientIdInPoc failed", e);
		}
		return null;
	}
	

	@Override
	public Response getDatabaseLink(PatientRegistrationDto patientRegistrationDto)
			throws ApplicationException, Exception {
		try {
           /* Session session = sessionFactory.getCurrentSession();
            
            StringBuilder stringQuery=new StringBuilder("select mysql_onp  as  \"mysqlOnp\" from public.m_unit_master where unit_id = '"
                    + patientRegistrationDto.getUnitId() + "' AND status='A' limit 1");
            
            Query actualQuery = session.createSQLQuery(stringQuery.toString());
            List<PatientRegistrationDto> patientRegistrationDtoList = actualQuery
                    .setResultTransformer(Transformers.aliasToBean(PatientRegistrationDto.class)).list();
            return new Response(SUCCESS, null, null, patientRegistrationDtoList, null);*/
            
            PatientRegistrationDto patientDocSynDto =(PatientRegistrationDto) sessionFactory.getCurrentSession()
					.createSQLQuery("select mysql_onp  as  \"mysqlOnp\" from public.m_unit_master where unit_id =:unitId AND status='A' limit 1")
					.setParameter("unitId", patientRegistrationDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(PatientRegistrationDto.class)).uniqueResult();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, patientDocSynDto);

        } catch (HibernateException he) {
          System.out.println(he);
          throw he;
        } catch (Exception e) {
        	 throw e;
        }
       // return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
    }

	@Override
	@Transactional
	public Response getcontryIdFromCountryName(ScheduleAppointmentReqDto reqDto)
			throws ApplicationException, Exception {
		try {
		
		Integer countryId=(Integer)sessionFactory.getCurrentSession()
				.createSQLQuery("select "
				+ " country_id  "
				+ " from "
				+ " 	m_country_master mpm "
				+ " where "
				+ "country_name like '%"+reqDto.getCountry()+"%'"
				+ " 	and organization_id ="+reqDto.getHospitalId()
				+ " 	order by 1 desc limit 1 ") 
				.uniqueResult();
		
		
		Integer stateId=(Integer)sessionFactory.getCurrentSession()
				.createSQLQuery("select "
				+ " state_id  "
				+ " from "
				+ " 	m_state_master mpm "
				+ " where "
				+ " state_name like '%"+reqDto.getState()+"%'"
				+ " 	and organization_id ="+reqDto.getHospitalId()
				+ " 	order by 1 desc limit 1 ") 
				.uniqueResult();
		
		Integer cityId=(Integer)sessionFactory.getCurrentSession()
				.createSQLQuery("select "
				+ " city_id  "
				+ " from "
				+ " 	m_city_master mpm "
				+ " where "
				+ " city_name like '%"+reqDto.getCity()+"%'"
				+ " 	and organization_id ="+reqDto.getHospitalId()
				+ " 	order by 1 desc limit 1 ") 
				.uniqueResult();

			Integer nationalityId =(Integer)sessionFactory.getCurrentSession()
					.createSQLQuery("select "
							+ " nationality_id  "
							+ " from "
							+ " 	m_nationality_master mnm "
							+ " where "
							+ " nationality_name like '%"+reqDto.getNationality()+"%'"
							+ " 	and organization_id ="+reqDto.getHospitalId()
							+ " 	order by 1 desc limit 1 ")
					.uniqueResult();
		
		reqDto.setCountryId(countryId);
		reqDto.setStateId(stateId);
		reqDto.setCityId(cityId);
		reqDto.setNationalityId(nationalityId);

		return new Response<>(SUCCESS, SUCCESS_CODE, null, null, reqDto);
	} catch (HibernateException he) {
        System.out.println(he);
        throw he;
      } catch (Exception e) {
      	 throw e;
      }
     // return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
  }

	@Override
	@Transactional
	public Response getVideoConsultationLinkForAppFromHIS(String uuid) throws ApplicationException, Exception {
		try {
			
			/*
			 * String videoLink=(String)sessionFactory.getCurrentSession()
			 * .createSQLQuery("select " + " video_call_url  " + " from " +
			 * " 	patient.t_patient_registration pm " + " where " + " uuid ="+uuid +
			 * " 	order by 1 desc limit 1 ") .uniqueResult();
			 */
			
			String patUhidNumber = (String) sessionFactory.getCurrentSession()
				    .createSQLQuery("SELECT uhid_number "
				        + "FROM patient.t_patient_registration pm "
				        + "WHERE uuid = :uuid "
				        + "ORDER BY 1 DESC LIMIT 1")
				    .setParameter("uuid", uuid)
				    .uniqueResult();
			
			String uhidnumber =patUhidNumber;
			PatientRegistrationDto dto = new PatientRegistrationDto();
			dto.setUhidnumber(uhidnumber);
			System.out.println("My video URL isssss:"+uhidnumber);
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, dto);
		} catch (HibernateException he) {
	        System.out.println(he);
	        throw he;
	      } catch (Exception e) {
	      	 throw e;
	      }
	     // return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	  }

	@Override
	@Transactional
	public Response updatePatientAsDeath(PatientRegistrationDto patientRegistrationDto)
			throws ApplicationException, Exception {
		
        Session session = sessionFactory.getCurrentSession();
        SQLQuery query = session.createSQLQuery(
                "select mysql_onp from public.m_unit_master where unit_id = '"
                        + patientRegistrationDto.getUnitId() + "' AND status='A' limit 1");
		Object result = query.uniqueResult();

		String mysqlSmartJdbc = result != null ? result.toString() : null;

		SQLQuery queryForURL = session.createSQLQuery(
				"select patient_reg_local_url from public.m_unit_master where unit_id = '"
						+ patientRegistrationDto.getUnitId() + "' AND status='A' limit 1");
		Object result1 = queryForURL.uniqueResult();

		String localURL = result1 != null ? result1.toString() : null;
        
        if(mysqlSmartJdbc != null && mysqlSmartJdbc != "" && localURL!= null && !localURL.trim().isEmpty()){
        	
        String[] connStr = mysqlSmartJdbc.split(",");
        String firststr = connStr[0];
        String secondStr = connStr[1];
        String thirdStr = connStr[2];
        
        String uhidNmuber = patientRegistrationDto.getUhidnumber();
        String newdata;
        newdata = " UPDATE shreedb.dbo.patient_master SET alive = 'D' "
        	    + " where patient_code = '"+uhidNmuber+"'";

        NewONPRegistrationDto newONPRegistrationDto = new NewONPRegistrationDto();
        newONPRegistrationDto.setDbconnection(firststr);
        newONPRegistrationDto.setDbUser(secondStr);
        newONPRegistrationDto.setDbPassword(thirdStr);
        newONPRegistrationDto.setConnectionString (newdata);
        
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("connectionString", newdata);
        jsonObject.put("dbconnection", firststr);
        jsonObject.put("dbUser", secondStr);
        jsonObject.put("dbPassward", thirdStr);
        jsonArray.put(jsonObject);

        String requestJson =  jsonObject.toString();
        OkHttpClient client = new OkHttpClient().newBuilder()
                  .build();
                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType,requestJson);
                Request request = new Request.Builder()
                  .url(localURL.trim())
                  .method("POST", body)
                  .addHeader("Content-Type", "application/json")
                  .build();
                try (okhttp3.Response response = client.newCall(request).execute()){
                	logger.info(response.code());
                	logger.info(response.message());
                	logger.info(response.isSuccessful());
                }
                
        
        return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
      }else{
          return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
        }
        
    }

	@Override
	public Response patientSearchPatientListForABDM(PatientRegistrationDto patientRegistrationDto)
			throws ApplicationException, Exception {
		try {
			Session session = sessionFactory.openSession();
			StringBuilder stringQuery1 = new StringBuilder(session.getNamedQuery("GET_PATIENT_DATA_ABDM").getQueryString());
		    StringBuilder resQuery1 = this.generateQueryForPatientSearch(stringQuery1,patientRegistrationDto);
			Query actualQuery1 = session.createSQLQuery(resQuery1.toString());
		    List<PatientRegistrationDto> unknownPatientRegistrationDtosList1 = actualQuery1
					.setInteger("orgId", patientRegistrationDto.getOrganizationId())
				    .setInteger("unitId", patientRegistrationDto.getUnitId())
				    .setString("searchKeyword",   "%"+patientRegistrationDto.getSearchKeyword() + "%")
					.setResultTransformer(Transformers.aliasToBean(PatientRegistrationDto.class)).list();
		
			return new Response(SUCCESS, null, null, unknownPatientRegistrationDtosList1, null);

		} catch (HibernateException he) {
             throw he;
		} catch (Exception e) {
			 throw e;
		}
}

	@Override
	public Response patientSearchPatientListForABDMForDeepLinking(PatientRegistrationDto patientRegistrationDto)
			throws ApplicationException, Exception {
		try {
			Session session = sessionFactory.openSession();
			StringBuilder stringQuery1 = new StringBuilder(session.getNamedQuery("GET_PATIENT_DATA_ABDM_DEEPLINKING").getQueryString());
		    StringBuilder resQuery1 = this.generateQueryForPatientSearch(stringQuery1,patientRegistrationDto);
			Query actualQuery1 = session.createSQLQuery(resQuery1.toString());
		    List<PatientRegistrationDto> unknownPatientRegistrationDtosList1 = actualQuery1
					.setInteger("orgId", patientRegistrationDto.getOrganizationId())
				    .setInteger("unitId", patientRegistrationDto.getUnitId())
				    .setString("searchKeyword",   "%"+patientRegistrationDto.getSearchKeyword() + "%")
					.setResultTransformer(Transformers.aliasToBean(PatientRegistrationDto.class)).list();
		
			return new Response(SUCCESS, null, null, unknownPatientRegistrationDtosList1, null);

		} catch (HibernateException he) {
             throw he;
		} catch (Exception e) {
			 throw e;
		}
}

	@Override
	public Response updateLatestMedicalSummaryReport(Integer patientId, String reportText) {
		try {
			String query = "UPDATE patient.t_patient_registration SET latest_medical_summary_report= :reportText WHERE patient_id= :patientId";
			sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("patientId", patientId)
					.setParameter("reportText", reportText)
					.executeUpdate();

			String getUUid = " select uuid from patient.t_patient_registration WHERE patient_id= :patientId limit 1";
			String uuid = (String) sessionFactory.getCurrentSession().createSQLQuery(getUUid)
					.setParameter("patientId", patientId)
					.uniqueResult();

			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, uuid);

		}catch (HibernateException he) {
		logger.error("updateLatestMedicalSummaryReport failed", he);
		}catch (Exception e) {
		logger.error("updateLatestMedicalSummaryReport failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}


	@Override
	public Response getInitalAssesmentData(SlotMasterDto slotMasterDto) throws ApplicationException, Exception {

		try {
			List<PatientRegistrationDto> patientRegistrationDtosList = null;

//			String sql="  select patientreg0_.patient_id as \"patientId\", patientreg0_.uhid_number as \"uhidNumber\"," +
//					" patientreg0_.prefix_id as \"prefixId\", prefixmast2_.prefix_code as \"prefixCode\", " +
//					" (coalesce(prefixmast2_.prefix_code,'')||' '||coalesce(patientreg0_.first_name,'')||' '||coalesce(patientreg0_.middle_name,'')||' '||coalesce(patientreg0_.last_name,'')||' '||trim(to_char(extract(year from age(CURRENT_TIMESTAMP, patientreg0_.dob)),'999'))||'Y / '||gendermast3_.gender_code) " +
//					" as \"patientName\", patientreg0_.gender_id as \"genderId\", gendermast3_.gender_code as \"genderCode\", " +
//					" patientreg0_.patient_category_id as \"patientCategoryId\", trim(to_char(extract(year from age(CURRENT_TIMESTAMP, patientreg0_.dob)),'999')) " +
//					" as \"ageString\", to_char(slotmaster6_.appointment_date,'DD/MM/YYYY') as \"appointmentDate\", appointmen4_.appointment_id " +
//					" as \"appointmentId\", appointmen4_.appointment_status_id as \"appointmentStatusId\", slotmaster6_.speciality_id as \"specialityId\", " +
//					" speciality7_.speciality_name as \"specialityName\", unitspecia8_.dept_wise_appointment as \"deptWiseAppointment\", " +
//					" slotmaster6_.doctor_id as \"doctorId\", ('Dr.'||' '||coalesce(doctormast9_.first_name)||' '||coalesce(doctormast9_.last_name)) as" +
//					"  \"doctorName\", patientreg0_.registration_type_id as \"typeId\", to_char(slotmaster6_.from_time,'HH:MI AM') as \"appointmentTime\", " +
//					" to_char(slotmaster6_.from_time,'HH24:MI') as \"appointmentTime2\", coalesce(appointmen4_.is_paid_appointment,'N') as \"isPaidAppointment\", " +
//					" encounterm5_.encounter_id as \"encounterId\", patientreg0_.identification_type_id as \"identificationTypeId\", patientreg0_.identification_number " +
//					" as \"identificationNumber\", (select admission10_.admission_id from adt.t_admission admission10_ where admission10_.status='A' and " +
//					" admission10_.patient_id=patientreg0_.patient_id) as \"admissionId\" from patient.t_patient_registration patientreg0_ inner join patient.t_patient_details " +
//					" patientdet1_ on patientreg0_.patient_id=patientdet1_.patient_id left outer join public.m_prefix_master prefixmast2_ " +
//					" on patientreg0_.prefix_id=prefixmast2_.prefix_id inner join public.m_gender_master gendermast3_ on patientreg0_.gender_id=gendermast3_.gender_id " +
//					" inner join opd.m_appointment_master appointmen4_ on patientreg0_.patient_id=appointmen4_.patient_id left outer join opd.t_encounter_master " +
//					" encounterm5_ on appointmen4_.appointment_id=encounterm5_.appointment_id left outer join opd.t_encounter_details ted on " +
//					" ted.patient_id=patientreg0_.patient_id inner join opd.m_slot_master slotmaster6_ on appointmen4_.slot_id=slotmaster6_.slot_id left outer join" +
//					"  doctor.m_speciality_master speciality7_ on slotmaster6_.speciality_id=speciality7_.speciality_id left outer join public.t_unit_speciality_mapper " +
//					" unitspecia8_ on speciality7_.speciality_id=unitspecia8_.speciality_id left outer join doctor.m_doctor_master doctormast9_ on " +
//					" slotmaster6_.doctor_id=doctormast9_.doctor_id where patientreg0_.organization_id=:orgId and unitspecia8_.unit_id=appointmen4_.unit_id and " +
//					" patientreg0_.status='A' and patientdet1_.status='A' and patientreg0_.is_primary='Y' and appointmen4_.status='A'    " +
//					" and appointmen4_.unit_id=:unitId and appointmen4_.appointment_type_id=1 and slotmaster6_.appointment_date=:currentDate and patientreg0_.registration_type_id " +
//					" not in (2,4) and (appointmen4_.is_teleconsultation_apppointment<>1 or coalesce(appointmen4_.is_paid_appointment,'N')<>'Y') and " +
//					" appointmen4_.appointment_unique_id=:appointmentUniqueId and (ted.assignment_start_time is null or ted.assignment_stop_time is null) order " +
//					" by appointmen4_.appointment_id desc limit 1 ";


			String sql=" select TO_CHAR(tem.encounter_date, 'DD-MM-YYYY HH24:MI:SS') AS \"assignmentStartTime\",appointmen4_.created_by AS \"assignmentStartBy\",CASE WHEN ted.encounter_id IS NOT NULL THEN TRUE ELSE FALSE END AS \"initialAssesmentStarted\", " +
					" ord.order_details_id as \"orderDetailsId\", patientreg0_.patient_id as \"patientId\", " +
					" patientreg0_.uhid_number as \"uhidNumber\", patientreg0_.prefix_id as \"prefixId\", prefixmast2_.prefix_code as \"prefixCode\", " +
					" (coalesce(prefixmast2_.prefix_code,'')||' '||coalesce(patientreg0_.first_name,'')||' '||coalesce(patientreg0_.middle_name,'')||' '||coalesce(patientreg0_.last_name,'')||' '||trim(to_char(extract(year from age(CURRENT_TIMESTAMP, patientreg0_.dob)),'999'))||'Y / '||gendermast3_.gender_code) " +
					" as \"patientName\", patientreg0_.gender_id as \"genderId\", gendermast3_.gender_code as \"genderCode\", patientreg0_.patient_category_id " +
					" as \"patientCategoryId\", trim(to_char(extract(year from age(CURRENT_TIMESTAMP, patientreg0_.dob)),'999')) as \"ageString\", " +
					" to_char(slotmaster6_.appointment_date,'DD/MM/YYYY') as \"appointmentDate\", appointmen4_.appointment_id as \"appointmentId\", " +
					" appointmen4_.appointment_status_id as \"appointmentStatusId\", slotmaster6_.speciality_id as \"specialityId\", " +
					" speciality7_.speciality_name as \"specialityName\", unitspecia8_.dept_wise_appointment as \"deptWiseAppointment\", " +
					" slotmaster6_.doctor_id as \"doctorId\", ('Dr.'||' '||coalesce(doctormast9_.first_name)||' '||coalesce(doctormast9_.last_name)) " +
					" as \"doctorName\", patientreg0_.registration_type_id as \"typeId\", to_char(slotmaster6_.from_time,'HH:MI AM') as \"appointmentTime\", " +
					" to_char(slotmaster6_.from_time,'HH24:MI') as \"appointmentTime2\", coalesce(appointmen4_.is_paid_appointment,'N') as \"isPaidAppointment\", " +
					" tem.encounter_id as \"encounterId\", patientreg0_.identification_type_id as \"identificationTypeId\", patientreg0_.identification_number " +
					" as \"identificationNumber\", (select admission10_.admission_id from adt.t_admission admission10_ where admission10_.status='A' and " +
					" admission10_.patient_id=patientreg0_.patient_id) as \"admissionId\" from patient.t_patient_registration patientreg0_ inner " +
					" join patient.t_patient_details patientdet1_ on patientreg0_.patient_id=patientdet1_.patient_id left outer join public.m_prefix_master prefixmast2_ on " +
					" patientreg0_.prefix_id=prefixmast2_.prefix_id inner join public.m_gender_master gendermast3_ on patientreg0_.gender_id=gendermast3_.gender_id inner " +
					" join opd.m_appointment_master appointmen4_ on patientreg0_.patient_id=appointmen4_.patient_id LEFT JOIN opd.t_encounter_master tem ON " +
					" appointmen4_.appointment_id=tem.appointment_id " +
					" LEFT JOIN billing.m_billing_master mbm on mbm.encounter_id=tem.encounter_id inner join billing.t_billing_details tbd  on tbd.billing_master_id=mbm.billing_master_id " +
					" LEFT JOIN opd.t_encounter_details ted ON ted.encounter_id=tem.encounter_id INNER JOIN t_order_master " +
					" om ON om.encounter_id=tem.encounter_id INNER JOIN t_order_details ord ON ord.order_id=om.order_id AND ord.is_consultation_service='Y' inner " +
					" join opd.m_slot_master slotmaster6_ on appointmen4_.slot_id=slotmaster6_.slot_id left outer join doctor.m_speciality_master speciality7_ on " +
					" slotmaster6_.speciality_id=speciality7_.speciality_id left outer join public.t_unit_speciality_mapper unitspecia8_ on " +
					" speciality7_.speciality_id=unitspecia8_.speciality_id left outer join doctor.m_doctor_master doctormast9_ on slotmaster6_.doctor_id=doctormast9_.doctor_id " +
					" where patientreg0_.organization_id= :orgId and unitspecia8_.unit_id=appointmen4_.unit_id and patientreg0_.status='A' and patientdet1_.status='A' and " +
					" patientreg0_.is_primary='Y' and appointmen4_.status='A' and appointmen4_.unit_id= :unitId and appointmen4_.appointment_type_id=1 and " +
					" slotmaster6_.appointment_date= :currentDate and patientreg0_.registration_type_id not in (2,4) and (appointmen4_.is_teleconsultation_apppointment<>1 " +
					" or coalesce(appointmen4_.is_paid_appointment,'N')<>'Y') and appointmen4_.appointment_unique_id= :appointmentUniqueId and (ted.assignment_start_time " +
					" is null or ted.assignment_stop_time is null) and  mbm.bill_status_id=3 order by appointmen4_.appointment_id desc limit 1 ";


			patientRegistrationDtosList =sessionFactory.getCurrentSession().createSQLQuery(sql)
					.setParameter("orgId", slotMasterDto.getOrganizationId())
					.setParameter("unitId", slotMasterDto.getUnitId())
					.setDate("currentDate", TimeZoneComponent.getDateByzone(slotMasterDto.getUnitId()))
					.setParameter("appointmentUniqueId", slotMasterDto.getAppointmentUniqueId())
					.setResultTransformer(Transformers.aliasToBean(PatientRegistrationDto.class)).list();


			return new Response(SUCCESS, null, null, patientRegistrationDtosList, null);
		} catch (HibernateException he) {
		logger.error("getInitalAssesmentData failed", he);
		} catch (Exception e) {
		logger.error("getInitalAssesmentData failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}
}
