package com.param.global.dao;



import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.adt.admission.model.Admission;
import com.param.adt.admission.model.AdmissionDetails;
import com.param.adt.admission.model.MedicoLeagal;
import com.param.common.dto.CustomException;
import com.param.entity.model.opd.Encounter;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dao.global.GlobalDao;
import com.param.global.dto.AdmissionDto;
import com.param.global.dto.PatientSearchDto;
import com.param.global.service.VerifiedIdentityService;
import com.param.global.dto.global.VisitDetails;
import com.param.global.model.UnknownPatientRegistration;
import com.param.global.org.common.dto.PriorityMasterDto;
import com.param.global.utility.ErrorCorrelationUtil;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import javax.transaction.Transactional;

@SuppressWarnings({ "rawtypes", "unchecked" })
@Repository
public class UnknownPatientRegistrationDaoImpl extends GenericDao<UnknownPatientRegistration, Integer>
		implements ICommonConstants, IUnknownPatientRegistrationDao {

	private static final Logger logger = Logger.getLogger(UnknownPatientRegistrationDaoImpl.class);

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	DozerBeanMapper mapper;

	@Autowired
	private VerifiedIdentityService verifiedIdentityService;

	public UnknownPatientRegistrationDaoImpl() {
		super(UnknownPatientRegistration.class);

	}

	@Override
	public Response saveUnknownPatient(AdmissionDto unAdmissionDto) throws ApplicationException {
		try {

			UnknownPatientRegistration unknownPatientRegistration = mapper.map(unAdmissionDto,
					UnknownPatientRegistration.class, "AdmissionDto_to_UnknownPatientRegistration");
			unknownPatientRegistration.setStatus('A');
			unknownPatientRegistration.setAgeFormat(unknownPatientRegistration.getAgeFormat()=='\u0000'?'Y':unknownPatientRegistration.getAgeFormat());
			unknownPatientRegistration.setCreatedDate(TimeZoneComponent.getDateByzone(unAdmissionDto.getUnitId() != null ? unAdmissionDto.getUnitId() : 0));
			unknownPatientRegistration.setUpdatedDate(TimeZoneComponent.getDateByzone(unAdmissionDto.getUnitId() != null ? unAdmissionDto.getUnitId() : 0));
			save(unknownPatientRegistration);

			return new Response(SUCCESS, null, null, null, unknownPatientRegistration);
		} catch (HibernateException he) {
			logger.error("saveUnknownPatient failed", he);
		} catch (Exception e) {
			logger.error("saveUnknownPatient failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getPriorityMasterList(Integer orgId, Integer unitId) throws ApplicationException {
		try {
			List<PriorityMasterDto> priorityMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PRIORITY_LIST")
					.setParameter("orgId", orgId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(PriorityMasterDto.class)).list();
			if (priorityMasterDtosList != null) {
				return new Response(SUCCESS, null, null, priorityMasterDtosList, null);
			}
		} catch (HibernateException he) {
			logger.error("getPriorityMasterList failed", he);
		} catch (Exception e) {
			logger.error("getPriorityMasterList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response saveERPatientAdmission(AdmissionDto unAdmissionDto) throws ApplicationException {
		try {

			Admission admission = mapper.map(unAdmissionDto, Admission.class, "AdmissionDto_to_Admission");
			admission.setCreatedDate(TimeZoneComponent.getDateByzone(unAdmissionDto.getUnitId() != null ? unAdmissionDto.getUnitId():0));
			admission.setUpdatedDate(TimeZoneComponent.getDateByzone(unAdmissionDto.getUnitId() != null ? unAdmissionDto.getUnitId():0));
			Integer id = (Integer) sessionFactory.getCurrentSession().save(admission);
			return new Response(SUCCESS, null, id.toString(), null, null);
		} catch (HibernateException he) {
			logger.error("saveERPatientAdmission failed", he);
			return new Response(ERROR, null, null, null, null);
		} catch (Exception e) {
			logger.error("saveERPatientAdmission failed", e);
			return new Response(ERROR, null, null, null, null);
		}
	}

	@Override
	public Response saveERPatientDetails(AdmissionDto unAdmissionDto) throws ApplicationException {
		try {
			AdmissionDetails admissionDetails = mapper.map(unAdmissionDto, AdmissionDetails.class,
					"AdmissionDto_to_AdmissionDetails");
					admissionDetails.setStatus('A');
					admissionDetails.setIsShortLeave('N');
					admissionDetails.setIsDischarge('N');
					admissionDetails.setIsCancel('N');
					admissionDetails.setIsClose('N');
					admissionDetails.setIsHighRisk('N');
					admissionDetails.setIsCritical('N');
					admissionDetails.setIsMedicoLegal('N');
					admissionDetails.setIsTransferOfCare('N');
					admissionDetails.setNursingAcceptance('Y');
					admissionDetails.setCreatedDate(TimeZoneComponent.getDateByzone(unAdmissionDto.getUnitId() != null ? unAdmissionDto.getUnitId():0));
			        admissionDetails.setAdmissionDateTime(TimeZoneComponent.getDateByzone(unAdmissionDto.getUnitId() != null ? unAdmissionDto.getUnitId():0));

			admissionDetails.setUpdatedDate(TimeZoneComponent.getDateByzone(unAdmissionDto.getUnitId() != null ? unAdmissionDto.getUnitId():0));
			sessionFactory.getCurrentSession().save(admissionDetails);

			return new Response(SUCCESS, null, null, null, unAdmissionDto);

		} catch (HibernateException he) {
			logger.error("saveERPatientDetails failed", he);
		} catch (Exception e) {
			logger.error("saveERPatientDetails failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response patientSearchByMultipleCriteria(PatientSearchDto patientSearchDto) throws ApplicationException {
		try {
			// BR-182 rebind: the client-supplied org/unit are overridden with the verified session identity before the PHI search
			patientSearchDto.setOrganizationId(verifiedIdentityService.getVerifiedOrganizationId());
			patientSearchDto.setUnitId(verifiedIdentityService.getVerifiedUnitId());

			Date date = TimeZoneComponent.getDateByzone(patientSearchDto.getUnitId());
			String currentDate=GlobalCommonDateUtils.getStringDate(date, "yyyy-MM-dd");
			Session session = sessionFactory.getCurrentSession();
			StringBuilder resQuery = this.searchPatient(patientSearchDto);
			SQLQuery actualQuery = session.createSQLQuery(resQuery.toString());
			if(patientSearchDto.getOffset()!=null && patientSearchDto.getNoOfRecordsPerPage()!=null)
			{
				actualQuery.setFirstResult(patientSearchDto.getOffset());
				actualQuery.setMaxResults(patientSearchDto.getNoOfRecordsPerPage());
			}


			// Set parameters for the dynamic parts of the query
			if (patientSearchDto.getUhIdNumber() != null && !patientSearchDto.getUhIdNumber().isEmpty()) {
				actualQuery.setParameter("uhidNumber", "%" + patientSearchDto.getUhIdNumber().toLowerCase() + "%");
			}

			if (patientSearchDto.getPatientName() != null && !patientSearchDto.getPatientName().isEmpty()) {
				actualQuery.setParameter("patientName", patientSearchDto.getPatientName().toLowerCase());
			}

			if (patientSearchDto.getGenderId() != null) {
				actualQuery.setParameter("genderId", patientSearchDto.getGenderId());
			}

			if (patientSearchDto.getMobileNo() != null && !patientSearchDto.getMobileNo().isEmpty()) {
				actualQuery.setParameter("mobileNo", patientSearchDto.getMobileNo() + "%");
			}

			if (patientSearchDto.getBirthDate() != null && !patientSearchDto.getBirthDate().isEmpty()) {
				actualQuery.setParameter("birthDate", patientSearchDto.getBirthDate());
			}

			if (patientSearchDto.getIdentificationTypeId() != null && patientSearchDto.getIdentificationTypeId() > 0) {
				actualQuery.setParameter("identificationTypeId", patientSearchDto.getIdentificationTypeId());
			}

			if (patientSearchDto.getIdentificationNo() != null && !patientSearchDto.getIdentificationNo().isEmpty()) {
				actualQuery.setParameter("identificationNo", patientSearchDto.getIdentificationNo().toLowerCase());
			}

			if (patientSearchDto.getPatientId() != null && patientSearchDto.getPatientId() > 0) {
				actualQuery.setParameter("patientId", patientSearchDto.getPatientId());
			}

			if (patientSearchDto.getWardId() != null) {
				actualQuery.setParameter("wardId", patientSearchDto.getWardId());
			}

			if (patientSearchDto.getDoctorId() != null) {
				actualQuery.setParameter("doctorId", patientSearchDto.getDoctorId());
			}

			List<PatientSearchDto> bedMasterDtosList = actualQuery
					.setParameter("orgId", patientSearchDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(PatientSearchDto.class)).list();

			// Extract patient IDs as a list of Integers

			if(bedMasterDtosList != null ){

				List<Integer> patientIds = bedMasterDtosList.stream()
						.map(PatientSearchDto::getPatientId) // Assuming getPatientId() returns an Integer
						.collect(Collectors.toList());
                   if(!patientIds.isEmpty() && patientIds.size()>0) {
					   String query = " select distinct ta.patient_id as \"patientId\"  from adt.t_discharge td " +
							   " inner join adt.t_admission ta  on td.admission_id  = ta.admission_id  " +
							   " where ta.patient_id in (:id) and  td.is_death  = 'Y'";

					   Query actualQuerys = session.createSQLQuery(query);
					   List<PatientSearchDto> bedMasterDtosLists = actualQuerys
							   .setParameterList("id", patientIds)
							   .setResultTransformer(Transformers.aliasToBean(PatientSearchDto.class)).list();

					   Set<Integer> deathPatientIds = bedMasterDtosLists.stream()
							   .map(PatientSearchDto::getPatientId)
							   .collect(Collectors.toSet());

					   for (PatientSearchDto dto : bedMasterDtosList) {
						   if (deathPatientIds.contains(dto.getPatientId())) {
							   dto.setIsDeath('Y');
						   } else {
							   dto.setIsDeath('N');
						   }
					   }

				   }

			}

			if(bedMasterDtosList != null && !bedMasterDtosList.isEmpty()){

				List<Integer> patientList = bedMasterDtosList.stream().map(PatientSearchDto :: getPatientId).collect(Collectors.toList());
				String q = "select " +
						" max(enc.encounter_id) as \"encounterId\" ,  " +
						" enc.patient_id as \"patientId\" " +
						" from  " +
						" opd.t_encounter_master enc " +
						" inner join opd.m_appointment_master app on  " +
						" enc.appointment_id = app.appointment_id  " +
						" where  " +
						" enc.status_id != 6  " +
						" and to_char(app.appointment_date, 'yyyy-MM-dd')= '"+currentDate+"'   and  enc.patient_id in (:patientsList) " +
						" group by enc.patient_id  ";
				SQLQuery query = session.createSQLQuery(q);
				Query sqlsub = query.setParameterList("patientsList",patientList);
				List<AdmissionDto> encounterList = sqlsub.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();

				for(PatientSearchDto dto :bedMasterDtosList){
					AdmissionDto dummyservic= encounterList.stream()
							.filter(order -> dto.getPatientId().equals(order.getPatientId()))
							.findAny()
							.orElse(null);
					if(dummyservic!=null){
						dto.setEncounterId(dummyservic.getEncounterId());
					}
				}

			}

			return new Response(SUCCESS, null, null, bedMasterDtosList, null);

		} catch (HibernateException he) {
			logger.error("patientSearchByMultipleCriteria failed", he);
		} catch (Exception e) {
			logger.error("patientSearchByMultipleCriteria failed", e);
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response patientSearchByMultipleCriteriaForWalkInSales(PatientSearchDto patientSearchDto)throws ApplicationException {
	try {

		Date date = TimeZoneComponent.getDateByzone(patientSearchDto.getUnitId());
		String currentDate=GlobalCommonDateUtils.getStringDate(date, "yyyy-MM-dd");
		Session session = sessionFactory.getCurrentSession();
		
		StringBuilder resQuery = this.searchPatient(patientSearchDto);
		SQLQuery actualQuery = session.createSQLQuery(resQuery.toString());

		// Set parameters for the dynamic parts of the query
		if (patientSearchDto.getUhIdNumber() != null && !patientSearchDto.getUhIdNumber().isEmpty()) {
			actualQuery.setParameter("uhidNumber", "%" + patientSearchDto.getUhIdNumber().toLowerCase() + "%");
		}

		if (patientSearchDto.getPatientName() != null && !patientSearchDto.getPatientName().isEmpty()) {
			actualQuery.setParameter("patientName", patientSearchDto.getPatientName().toLowerCase());
		}

		if (patientSearchDto.getGenderId() != null) {
			actualQuery.setParameter("genderId", patientSearchDto.getGenderId());
		}

		if (patientSearchDto.getMobileNo() != null && !patientSearchDto.getMobileNo().isEmpty()) {
			actualQuery.setParameter("mobileNo", patientSearchDto.getMobileNo() + "%");
		}

		if (patientSearchDto.getBirthDate() != null && !patientSearchDto.getBirthDate().isEmpty()) {
			actualQuery.setParameter("birthDate", patientSearchDto.getBirthDate());
		}

		if (patientSearchDto.getIdentificationTypeId() != null && patientSearchDto.getIdentificationTypeId() > 0) {
			actualQuery.setParameter("identificationTypeId", patientSearchDto.getIdentificationTypeId());
		}

		if (patientSearchDto.getIdentificationNo() != null && !patientSearchDto.getIdentificationNo().isEmpty()) {
			actualQuery.setParameter("identificationNo", patientSearchDto.getIdentificationNo().toLowerCase());
		}

		if (patientSearchDto.getPatientId() != null && patientSearchDto.getPatientId() > 0) {
			actualQuery.setParameter("patientId", patientSearchDto.getPatientId());
		}

		if (patientSearchDto.getWardId() != null) {
			actualQuery.setParameter("wardId", patientSearchDto.getWardId());
		}

		if (patientSearchDto.getDoctorId() != null) {
			actualQuery.setParameter("doctorId", patientSearchDto.getDoctorId());
		}

		List<PatientSearchDto> bedMasterDtosList = actualQuery
				.setParameter("orgId", patientSearchDto.getOrganizationId())
				.setResultTransformer(Transformers.aliasToBean(PatientSearchDto.class)).list();
		
		StringBuilder resQuery1 = this.searchPatientForWalkInSales(patientSearchDto);
		SQLQuery actualQuery1 = session.createSQLQuery(resQuery1.toString());

		// Set parameters for the dynamic parts of the query
		if (patientSearchDto.getUhIdNumber() != null && !patientSearchDto.getUhIdNumber().isEmpty()) {
			actualQuery1.setParameter("uhidNumber", "%" + patientSearchDto.getUhIdNumber().toLowerCase() + "%");
		}

		if (patientSearchDto.getPatientName() != null && !patientSearchDto.getPatientName().isEmpty()) {
			actualQuery1.setParameter("patientName", patientSearchDto.getPatientName().toLowerCase());
		}

		if (patientSearchDto.getGenderId() != null) {
			actualQuery1.setParameter("genderId", patientSearchDto.getGenderId());
		}

		if (patientSearchDto.getMobileNo() != null && !patientSearchDto.getMobileNo().isEmpty()) {
			actualQuery1.setParameter("mobileNo", patientSearchDto.getMobileNo() + "%");
		}

		if (patientSearchDto.getPatientId() != null && patientSearchDto.getPatientId() > 0) {
			actualQuery1.setParameter("patientId", patientSearchDto.getPatientId());
		}

		List<PatientSearchDto> bedMasterDtosList1 = actualQuery1
				.setParameter("orgId", patientSearchDto.getOrganizationId())
				.setResultTransformer(Transformers.aliasToBean(PatientSearchDto.class)).list();
		
		if(bedMasterDtosList1 != null && bedMasterDtosList1.size()>0){
			bedMasterDtosList.addAll(bedMasterDtosList1);
		}

		if(bedMasterDtosList != null && !bedMasterDtosList.isEmpty()){

			List<Integer> patientList = bedMasterDtosList.stream().map(PatientSearchDto :: getPatientId).collect(Collectors.toList());
			String q = "select " +
					" max(enc.encounter_id) as \"encounterId\" ,  " +
					" enc.patient_id as \"patientId\" " +
					" from  " +
					" opd.t_encounter_master enc " +
					" inner join opd.m_appointment_master app on  " +
					" enc.appointment_id = app.appointment_id  " +
					" where  " +
					" enc.status_id != 6  " +
					" and to_char(app.appointment_date, 'yyyy-MM-dd')= '"+currentDate+"'   and  enc.patient_id in (:patientsList) " +
					" group by enc.patient_id  ";
			SQLQuery query = session.createSQLQuery(q);
			Query sqlsub = query.setParameterList("patientsList",patientList);
			List<AdmissionDto> encounterList = sqlsub.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();

			for(PatientSearchDto dto :bedMasterDtosList){
				AdmissionDto dummyservic= encounterList.stream()
						.filter(order -> dto.getPatientId().equals(order.getPatientId()))
						.findAny()
						.orElse(null);
				if(dummyservic!=null){
					dto.setEncounterId(dummyservic.getEncounterId());
				}
			}

		}
		
		return new Response(SUCCESS, null, null, bedMasterDtosList, null);

	} catch (HibernateException he) {
		logger.error("patientSearchByMultipleCriteriaForWalkInSales failed", he);
	} catch (Exception e) {
		logger.error("patientSearchByMultipleCriteriaForWalkInSales failed", e);
	}
	return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response patientSearchByMultipleCriteriaForIPD(PatientSearchDto patientSearchDto) throws ApplicationException {
		try {
			Session session = sessionFactory.getCurrentSession();
			StringBuilder resQuery = this.searchPatientForIPD(patientSearchDto);
			SQLQuery actualQuery = session.createSQLQuery(resQuery.toString());

			// Set parameters for the dynamic parts of the query
			if (patientSearchDto.getIdentificationTypeId() != null && patientSearchDto.getIdentificationTypeId() > 0) {
				actualQuery.setParameter("identificationTypeId", patientSearchDto.getIdentificationTypeId());
			}

			if (patientSearchDto.getUhIdNumber() != null && !patientSearchDto.getUhIdNumber().isEmpty()) {
				actualQuery.setParameter("uhidNumber", "%" + patientSearchDto.getUhIdNumber().toLowerCase() + "%");
			}

			if (patientSearchDto.getPatientName() != null && !patientSearchDto.getPatientName().isEmpty()) {
				actualQuery.setParameter("patientName", patientSearchDto.getPatientName().toLowerCase());
			}

			if (patientSearchDto.getGenderId() != null) {
				actualQuery.setParameter("genderId", patientSearchDto.getGenderId());
			}

			if (patientSearchDto.getMobileNo() != null && !patientSearchDto.getMobileNo().isEmpty()) {
				actualQuery.setParameter("mobileNo", patientSearchDto.getMobileNo() + "%");
			}

			if (patientSearchDto.getBirthDate() != null && !patientSearchDto.getBirthDate().isEmpty()) {
				actualQuery.setParameter("birthDate", patientSearchDto.getBirthDate());
			}

			if (patientSearchDto.getIdentificationNo() != null && !patientSearchDto.getIdentificationNo().isEmpty()) {
				actualQuery.setParameter("identificationNo", "%" + patientSearchDto.getIdentificationNo().toLowerCase() + "%");
			}

			if (patientSearchDto.getPatientId() != null && patientSearchDto.getPatientId() > 0) {
				actualQuery.setParameter("patientId", patientSearchDto.getPatientId());
			}

			if (patientSearchDto.getWardList() != null && !patientSearchDto.getWardList().isEmpty()) {
				actualQuery.setParameterList("wardList", patientSearchDto.getWardList());
			}

			if (patientSearchDto.getBedList() != null && !patientSearchDto.getBedList().isEmpty()) {
				actualQuery.setParameterList("bedList", patientSearchDto.getBedList());
			}

			if (patientSearchDto.getVisitTypeId() != null && patientSearchDto.getVisitTypeId() > 0) {
				actualQuery.setParameter("visitTypeId", patientSearchDto.getVisitTypeId());
			}

// Return the final query

			List<PatientSearchDto> bedMasterDtosList = null;
			if(((patientSearchDto.getWardList() == null || patientSearchDto.getWardList().isEmpty()) && (patientSearchDto.getBedList()== null || patientSearchDto.getBedList().isEmpty())))
			{
				 	bedMasterDtosList = actualQuery
					.setParameter("orgId", patientSearchDto.getOrganizationId())
					.setParameter("unitId", patientSearchDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(PatientSearchDto.class)).list();
			}
			if((patientSearchDto.getBedList()!=null && (patientSearchDto.getWardList() == null || patientSearchDto.getWardList().isEmpty())) &&  patientSearchDto.getBedList().size() > 0)
			{
				 	bedMasterDtosList = actualQuery
					.setParameter("orgId", patientSearchDto.getOrganizationId())
					.setParameter("unitId", patientSearchDto.getUnitId())
				//	.setParameterList("bedList", patientSearchDto.getBedList())
					.setResultTransformer(Transformers.aliasToBean(PatientSearchDto.class)).list();
			}
			if((patientSearchDto.getWardList()!=null && (patientSearchDto.getBedList()== null || patientSearchDto.getBedList().isEmpty())) &&  patientSearchDto.getWardList().size() > 0)
			{
				 	bedMasterDtosList = actualQuery
					.setParameter("orgId", Integer.valueOf(patientSearchDto.getOrganizationId()))
					.setParameter("unitId", Integer.valueOf(patientSearchDto.getUnitId()))
					//.setParameterList("wardList", patientSearchDto.getWardList())
					.setResultTransformer(Transformers.aliasToBean(PatientSearchDto.class)).list();
			}
			
			return new Response(SUCCESS, null, null, bedMasterDtosList, null);

		} catch (HibernateException he) {
			logger.error("patientSearchByMultipleCriteriaForIPD failed", he);
		} catch (Exception e) {
			logger.error("patientSearchByMultipleCriteriaForIPD failed", e);
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response patientSearchByMultipleCriteriaForOPBilling(PatientSearchDto patientSearchDto) throws ApplicationException {
		try {
			Session session = sessionFactory.getCurrentSession();
			StringBuilder resQuery = this.searchPatientForOPBilling(patientSearchDto);
			SQLQuery actualQuery = session.createSQLQuery(resQuery.toString());
			// Set parameters for the dynamic parts of the query
			if (patientSearchDto.getIdentificationTypeId() != null && patientSearchDto.getIdentificationTypeId() > 0) {
				actualQuery.setParameter("identificationTypeId", patientSearchDto.getIdentificationTypeId());
			}

			if (patientSearchDto.getUhIdNumber() != null && !patientSearchDto.getUhIdNumber().isEmpty()) {
				actualQuery.setParameter("uhidNumber", "%" + patientSearchDto.getUhIdNumber().toLowerCase() + "%");
			}

			if (patientSearchDto.getPatientName() != null && !patientSearchDto.getPatientName().isEmpty()) {
				actualQuery.setParameter("patientName", patientSearchDto.getPatientName().toLowerCase());
			}

			if (patientSearchDto.getGenderId() != null) {
				actualQuery.setParameter("genderId", patientSearchDto.getGenderId());
			}

			if (patientSearchDto.getMobileNo() != null && !patientSearchDto.getMobileNo().isEmpty()) {
				actualQuery.setParameter("mobileNo", patientSearchDto.getMobileNo());
			}

			if (patientSearchDto.getBirthDate() != null && !patientSearchDto.getBirthDate().isEmpty()) {
				actualQuery.setParameter("birthDate", patientSearchDto.getBirthDate());
			}

			if (patientSearchDto.getIdentificationNo() != null && !patientSearchDto.getIdentificationNo().isEmpty()) {
				actualQuery.setParameter("identificationNo", "%" + patientSearchDto.getIdentificationNo().toLowerCase() + "%");
			}

			if (patientSearchDto.getPatientId() != null && patientSearchDto.getPatientId() > 0) {
				actualQuery.setParameter("patientId", patientSearchDto.getPatientId());
			}

			List<PatientSearchDto> bedMasterDtosList = actualQuery
					.setParameter("orgId", patientSearchDto.getOrganizationId())
					.setParameter("unitId", patientSearchDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(PatientSearchDto.class)).list();
			
			return new Response(SUCCESS, null, null, bedMasterDtosList, null);

		} catch (HibernateException he) {
			logger.error("patientSearchByMultipleCriteriaForOPBilling failed", he);
		} catch (Exception e) {
			logger.error("patientSearchByMultipleCriteriaForOPBilling failed", e);
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	private StringBuilder searchPatient(PatientSearchDto patientSearchDto)
	{
		Date date = TimeZoneComponent.getDateByzone(patientSearchDto.getUnitId());  
		String currentDate=GlobalCommonDateUtils.getStringDate(date, "yyyy-MM-dd");
	    //System.out.println(currentDate); 

		StringBuilder query=new StringBuilder();
		
		String strQuery="select " 
			 + " 	distinct (pr.patient_id) as \"patientId\", " 
			 + " 	pr.image_path as \"patientProfile\", " 
			 + " 	pr.uhid_number as \"uhIdNumber\", " 
			 + " 	concat(coalesce(prifix.prefix_code, ''), ' ', coalesce(pr.first_name, '') , ' ', coalesce(pr.middle_name, '') , ' ', coalesce(pr.last_name, '')) as \"toPatientName\", "
			 + " 	concat(coalesce(prifix.prefix_code, ''), ' ', coalesce(pr.first_name, '') , ' ', coalesce(pr.middle_name, '') , ' ', coalesce(pr.last_name, '')) as \"patientName\", " 
			 + " 	pr.gender_id as \"genderId\", " 
			 + " 	pr.file_sequence_number as \"fileSequenceNumber\", " 
			 + " 	gm.gender_code as \"genderCode\", " 
			 + " 	gm.gender_name as \"genderName\", " 
			 + " 	pr.mobile_number as \"mobileNo\", " 
			 + " 	pr.first_name as \"firstName\", " 
			 + " 	pr.middle_name as \"middleName\", " 
			 + " 	pr.last_name as \"lastName\", "
			 + "    pr.video_call_url as \"videoCallUrl\", " 
			 + " 	pr.email as \"email\", " 
			 + " 	pr.is_absconded as \"isAbsconded\", " 
			 + " 	id.identification_name as \"identificationName\", " 
			 + " 	coalesce(pr.identification_number, '') as \"identificationNo\", "
			 + "	(case when id.is_expiry_required='Y' then "
			 + "		to_char(pr.identification_expiry_date, 'YYYY-MM-DD') end) as \"identificationExpiryDate\", " 
			 + " 	to_char(pr.dob, 'DD/MM/YYYY') as \"birthDate\", " 
			 + " 	pr.dob as \"birthDateNew\", " 
			 + " 	concat(extract(year from age(current_timestamp, pr.dob)) , 'Y') as \"age\", " 
			 + " 	1 as \"patientType\", " 
			 + " 	pr.prefix_id as \"prefixId\", " 
			 + " 	pr.patient_category_id as \"patientCategoryId\", " 
			 + " 	pr.registration_type_id as \"registrationTypeId\", " 
			 + " 	pr.country_calling_code as \"countryCallingCode\", " 
			 + " 	pr.blood_group as \"bloodGroup\", " 
			 + " 	to_char(pr.created_date, 'DD/MM/YYYY') as \"createdDate\", "
			 + " 	wdmst.ward_name as \"wardName\", " 
			 + " 	bdmst.bed_number as \"bedNumber\", " 
			 + " 	to_char(admdtl.doa, 'DD/MM/YYYY') as \"doa\", " 
			 + " 	to_char(admdtl.pdd, 'DD/MM/YYYY') as \"pdd\", " 
			 + " 	(case when admmst.admission_id > 0 and admmst.visit_type_id !=4 then "
			 + " 		concat('Dr. ',coalesce(docmst.first_name, '') , ' ', coalesce(docmst.middle_name, '') , ' ', coalesce(docmst.last_name, '')) end) as \"doctorName\", "
			 + "    docmst.doctor_id as \"doctorId\"," 
			// + "  en.encounter_Id  as \"encounterId\" , "
			 + " 	admmst.admission_id as \"admissionId\", " 
			 + " 	admmst.unit_id as \"admissionUnitId\", " 
			 + " 	admmst.is_final_bill as \"isFinalBill\", " 
			 + " 	patDtls.country_id as \"countryId\", " 
			 + " 	patDtls.district_id as \"districtId\", " 
			 + " 	patDtls.address as \"address\", " 
			 + " (case " 
			 + "  when dishh.is_death  is not null then dishh.is_death " + 
			 " 		else 'N'" + 
			 " 	end) as \"isDeath\", "
			 + " 	admmst.visit_type_id as \"visitTypeId\", "
			 + " concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, pr.dob)) ,'Y ', EXTRACT(month from AGE(CURRENT_TIMESTAMP, pr.dob)) , 'M ' , EXTRACT(day from AGE(CURRENT_TIMESTAMP, pr.dob)),'D ') as \"ageStr\", "
			 + " 	admmst.legacy_visit_id as \"legacyVisitId\" "
			 + " from "
			 + " 	patient.t_patient_registration as pr " 
			 + " inner join patient.t_patient_details patDtls on " 
			 + " 	pr.patient_id = patDtls.patient_id and patDtls.status='A' " 
			 + " inner join public.m_gender_master as gm on " 
			 + " 	gm.gender_id = pr.gender_id " 
			 + " left join public.m_prefix_master as prifix on " 
			 + " 	prifix.prefix_id = pr.prefix_id " 
			 + " left join adt.m_identification_master as id on " 
			 + " 	id.identification_id = pr.identification_type_id " 
			 + " left join adt.t_admission as admmst on " 
			 + " 	admmst.patient_id = pr.patient_id " 
			 + " 	and admmst.status = 'A' " 
			 + " left join adt.t_admission_details as admdtl on " 
			 + " 	admmst.admission_id = admdtl.admission_id " 
			 + " 	and admdtl.status = 'A' " 
			 + " left join adt.m_ward_master as wdmst on " 
			 + " 	wdmst.ward_id = admdtl.ward_id " 
			 + " left join adt.m_bed_master as bdmst on " 
			 + " 	bdmst.bed_id = admdtl.bed_id " 
			 + " left join doctor.m_doctor_master as docmst on " 
			 + " 	docmst.doctor_id = admmst.doctor_id " 
			 + " left join adt.t_discharge as dishh on " 
			 + " 	dishh.admission_id = admmst.admission_id "
		/*	 + " left  join ( " +
			 "	select " + 
			 "		max(enc.encounter_id) encounter_id, " + 
			 "		enc.patient_id " + 
			 "	from " + 
			 "		opd.t_encounter_master enc " + 
			 "	inner join opd.m_appointment_master app on " + 
			 "		enc.appointment_id = app.appointment_id " + 
			 "	where " + 
			 "		enc.status_id != 6 		 " + 
			 "		and to_char(app.appointment_date, 'yyyy-MM-dd')= '"+currentDate+"'  " + 
			 "		group by enc.patient_id " + 
			 "	 ) en  on en.patient_id = pr.patient_id " */
			 + " where " 
			 + " 	pr.organization_id =:orgId "
			 + " 	and pr.is_primary = 'Y' " ;

		 query = new StringBuilder(strQuery);

// Append conditions based on non-null fields in patientSearchDto
		if (patientSearchDto.getUhIdNumber() != null && !patientSearchDto.getUhIdNumber().isEmpty() && patientSearchDto.getFromMrdScreen() == null) {
			query.append(" AND lower(pr.uhid_number) LIKE :uhidNumber ");
		}

		if(patientSearchDto.getPatientName() != null && !patientSearchDto.getPatientName().isEmpty()) {

			if(patientSearchDto.getFromMrdScreen() != null && patientSearchDto.getFromMrdScreen() == true) {
				if(patientSearchDto.getUhIdNumber() != null && !patientSearchDto.getUhIdNumber().isEmpty()) {
					query.append(" AND ( ((lower(coalesce(pr.first_name, '')) like concat( lower(:patientName), '%')  OR lower(coalesce( pr.last_name, '' )) like concat( lower(:patientName ), '%') )"
							+ " OR lower(concat(coalesce(pr.first_name),' ',coalesce(pr.last_name,''))) LIKE concat('%' , lower(:patientName),'%') ) "
							+ " Or (lower(pr.uhid_number) LIKE concat('%',LOWER(:uhidNumber),'%'))  "
							+ ")");
				}
			}else {
				query.append(" AND ((lower(coalesce(pr.first_name, '')) like concat( lower(:patientName), '%')  OR lower(coalesce( pr.last_name, '' )) like concat( lower(:patientName ), '%') )"
						+ " OR lower(concat(coalesce(pr.first_name),' ',coalesce(pr.last_name,''))) LIKE concat('%' , lower(:patientName),'%') )");

			}
		}


		if (patientSearchDto.getGenderId() != null) {
			query.append(" AND pr.gender_id = :genderId ");
		}

		if (patientSearchDto.getMobileNo() != null && !patientSearchDto.getMobileNo().isEmpty()) {
			query.append(" AND pr.mobile_number LIKE :mobileNo ");
		}

		if (patientSearchDto.getBirthDate() != null && !patientSearchDto.getBirthDate().isEmpty()) {
			query.append(" AND pr.dob = :birthDate ");
		}

		if (patientSearchDto.getIdentificationTypeId() != null && patientSearchDto.getIdentificationTypeId() > 0) {
			query.append(" AND pr.identification_type_id = :identificationTypeId ");
		}

		if (patientSearchDto.getIdentificationNo() != null && !patientSearchDto.getIdentificationNo().isEmpty()) {
			query.append(" AND lower(coalesce(pr.identification_number, '')) LIKE concat('%', lower(:identificationNo), '%') ");
		}

		if (patientSearchDto.getPatientId() != null && patientSearchDto.getPatientId() > 0) {
			query.append(" AND pr.patient_id = :patientId ");
		}

		if (patientSearchDto.getFromMrdScreen() == null) {
			query.append(" AND pr.status = 'A' ");
		}

		if (patientSearchDto.getWardId() != null) {
			query.append(" AND wdmst.ward_id = :wardId ");
		}

		if (patientSearchDto.getDoctorId() != null) {
			query.append(" AND docmst.doctor_id = :doctorId ");
		}

		return query;

	}

	
	
	private StringBuilder searchPatientForWalkInSales(PatientSearchDto patientSearchDto) 
	{
		Date date = TimeZoneComponent.getDateByzone(patientSearchDto.getUnitId());  
		String currentDate=GlobalCommonDateUtils.getStringDate(date, "yyyy-MM-dd");
	    //System.out.println(currentDate); 

		StringBuilder query=new StringBuilder();
		
		String strQuery= "select "
				+" distinct (pr.patient_id) as \"patientId\", "
				+" pr.temp_uhid as \"uhIdNumber\", "
				+" pr.patient_name as \"patientName\", "
				+" pr.gender_id as \"genderId\", "
				+" gm.gender_code as \"genderCode\", "
				+" gm.gender_name as \"genderName\", "
				+" pr.mobile_number as \"mobileNo\", "
				+" '' as \"age\", "
				+" 1 as \"patientType\", "
				+" pr.prefix_id as \"prefixId\", "
				+" 0 as \"patientCategoryId\", "
				+" 0 as \"registrationTypeId\", "
				+" pr.address as \"address\" "
			    +" from "
				+" pharmacy.t_temp_patient_registration as pr "
			    +" left join public.m_gender_master as gm on "
				+" gm.gender_id = pr.gender_id "
			    +" where "
				+" pr.organization_id = :orgId";

		 query = new StringBuilder(strQuery);

// Append conditions based on non-null fields in patientSearchDto
		if (patientSearchDto.getUhIdNumber() != null && !patientSearchDto.getUhIdNumber().isEmpty() && patientSearchDto.getFromMrdScreen() == null) {
			query.append(" AND lower(pr.temp_uhid) LIKE :uhidNumber ");
		}

		if (patientSearchDto.getPatientName() != null && !patientSearchDto.getPatientName().isEmpty()) {
			if (patientSearchDto.getFromMrdScreen() != null && patientSearchDto.getFromMrdScreen()) {
				if (patientSearchDto.getUhIdNumber() != null && !patientSearchDto.getUhIdNumber().isEmpty()) {
					query.append(" AND ( (lower(coalesce(pr.patient_name, '')) LIKE concat(lower(:patientName), '%')) ")
							.append(" OR (lower(pr.temp_uhid) LIKE concat('%', lower(:uhidNumber), '%')) )");
				}
			} else {
				query.append(" AND lower(coalesce(pr.patient_name, '')) LIKE concat(lower(:patientName), '%') ");
			}
		}

		if (patientSearchDto.getGenderId() != null) {
			query.append(" AND pr.gender_id = :genderId ");
		}

		if (patientSearchDto.getMobileNo() != null && !patientSearchDto.getMobileNo().isEmpty()) {
			query.append(" AND pr.mobile_number LIKE :mobileNo ");
		}

		if (patientSearchDto.getPatientId() != null && patientSearchDto.getPatientId() > 0) {
			query.append(" AND pr.patient_id = :patientId ");
		}

		return query;

	}
	
	
	
	private StringBuilder searchPatientForIPD(PatientSearchDto patientSearchDto) 
	{
		StringBuilder query=new StringBuilder();

		String strQuery=" select " 
				 + " 	distinct (pr.patient_id) as \"patientId\", " 
				 + " 	pr.image_path as \"patientProfile\", " 
				 + " 	pr.uhid_number as \"uhIdNumber\", " 
				 + " 	concat(coalesce(prifix.prefix_code, ''), ' ', coalesce(pr.first_name, '') , ' ', coalesce(pr.middle_name, '') , ' ', coalesce(pr.last_name, '')) as \"patientName\", " 
				 + " 	pr.gender_id as \"genderId\", " 
				 + " 	gm.gender_code as \"genderCode\", " 
				 + " 	gm.gender_name as \"genderName\", " 
				 + " 	pr.mobile_number as \"mobileNo\", " 
				 + " 	pr.first_name as \"firstName\", " 
				 + " 	pr.middle_name as \"middleName\", " 
				 + " 	pr.last_name as \"lastName\", " 
				 + " 	pr.email as \"email\", " 
				 + " 	pr.is_absconded as \"isAbsconded\", " 
				 + " 	id.identification_name as \"identificationName\", " 
				 + " 	coalesce(pr.identification_number, '') as \"identificationNo\", " 
				 + " 	to_char(pr.dob, 'DD/MM/YYYY') as \"birthDate\", " 
				 + " 	concat(extract(year from age(current_timestamp, pr.dob)) , 'Y ' , extract(month from age(current_timestamp, pr.dob)), 'M ') as \"age\", " 
				 + " 	1 as \"patientType\", " 
				 + " 	pr.prefix_id as \"prefixId\", "
				 + "    to_char(adtl.doa, 'DD/MM/YYYY') as \"doa\", "
				 + " 	pr.patient_category_id as \"patientCategoryId\", "
				 + " 	pr.registration_type_id as \"registrationTypeId\", "
                 + "     padtl.address as \"address\","
                 + "    cmm.city_name as \"cityName\", " 
				 + " 	adm.admission_id as \"admissionId\", " 
				 + " 	adm.visit_type_id as \"visitTypeId\", " 
				 + " 	cast(loyaltycard.card_no as text) as \"loyaltycardNo\", " 
				 + " 	coalesce(mppr.patient_loyalty_card_mppr_id, 0) as \"patientLoyaltyCardMpprId\" " 
				 + " from " 
				 + " 	patient.t_patient_registration as pr " 
				 + " left join public.m_prefix_master as prifix on  " 
				 + "  	prifix.prefix_id = pr.prefix_id 	 " 
				 + " left join public.m_loyalty_card loyaltycard on " 
				 + " 	pr.patient_id = loyaltycard.patient_id " 
				 + " 	and loyaltycard.is_deleted = 'N' " 
				 + " left join public.t_patient_loyalty_card_mppr mppr on " 
				 + " 	mppr.patient_id = loyaltycard.patient_id " 
				 + " 	and mppr.is_deleted = 'N' " 
				 + " inner join public.m_gender_master as gm on " 
				 + " 	gm.gender_id = pr.gender_id " 
				 + " left join adt.m_identification_master as id on " 
				 + " 	id.identification_id = pr.identification_type_id " 
				 + " inner join adt.t_admission as adm on " 
				 + " 	adm.patient_id = pr.patient_id " 
				 + " inner join adt.t_admission_details as adtl on " 
				 + " 	adtl.admission_id = adm.admission_id " 
                 + " left join patient.t_patient_details as padtl on " 
                 + "  padtl.patient_id = pr.patient_id " 
                 + " left join public.m_city_master as cmm on " 
                 + "  cmm.city_id = padtl.city_id " 
				 + " where " 
				 + " 	pr.status = 'A' " 
				 + " 	and pr.organization_id =:orgId " 
				 + " 	and adm.unit_id =:unitId " 
				 + " 	and pr.is_primary = 'Y' " 
				 + " 	and adm.status = 'A' " 
				 + " 	and adtl.status = 'A' " 
				 + " 	and adtl.nursing_acceptance = 'Y' " 
				 + " 	and adtl.bed_id is not null " ;

		 query = new StringBuilder(strQuery);

// Append conditions based on non-null fields in patientSearchDto
		if (patientSearchDto.getIdentificationTypeId() != null && patientSearchDto.getIdentificationTypeId() > 0) {
			query.append(" AND pr.identification_type_id = :identificationTypeId ");
		}

		if (patientSearchDto.getUhIdNumber() != null && !patientSearchDto.getUhIdNumber().isEmpty()) {
			query.append(" AND lower(pr.uhid_number) LIKE :uhidNumber ");
		}

		if (patientSearchDto.getPatientName() != null && !patientSearchDto.getPatientName().isEmpty()) {
			query.append(" AND (lower(concat(coalesce(pr.first_name, ''), coalesce(pr.last_name, ''))) LIKE concat(lower(:patientName), '%') ")
					.append(" OR lower(concat(coalesce(pr.first_name), ' ', coalesce(pr.last_name, ''))) LIKE concat('%', lower(:patientName), '%') )");
		}

		if (patientSearchDto.getGenderId() != null) {
			query.append(" AND pr.gender_id = :genderId ");
		}

		if (patientSearchDto.getMobileNo() != null && !patientSearchDto.getMobileNo().isEmpty()) {
			query.append(" AND pr.mobile_number LIKE :mobileNo ");
		}

		if (patientSearchDto.getBirthDate() != null && !patientSearchDto.getBirthDate().isEmpty()) {
			query.append(" AND pr.dob = :birthDate ");
		}

		if (patientSearchDto.getIdentificationNo() != null && !patientSearchDto.getIdentificationNo().isEmpty()) {
			query.append(" AND pr.identification_number LIKE :identificationNo ");
		}

		if (patientSearchDto.getPatientId() != null && patientSearchDto.getPatientId() > 0) {
			query.append(" AND pr.patient_id = :patientId ");
		}

		if (patientSearchDto.getWardList() != null && !patientSearchDto.getWardList().isEmpty()) {
			query.append(" AND adtl.ward_id IN (:wardList)");
		}

		if (patientSearchDto.getBedList() != null && !patientSearchDto.getBedList().isEmpty()) {
			query.append(" AND adtl.bed_id IN (:bedList)");
		}

		if (patientSearchDto.getVisitTypeId() != null && patientSearchDto.getVisitTypeId() > 0) {
			query.append(" AND adm.visit_type_id = :visitTypeId ");
		}

		return query;

	}
	
	
	private StringBuilder searchPatientForOPBilling(PatientSearchDto patientSearchDto) 
	{
		StringBuilder query=new StringBuilder();
		String strQuery=" select " 
				 + " 	distinct (pr.patient_id) as \"patientId\", " 
				 + " 	pr.image_path as \"patientProfile\", " 
				 + " 	pr.uhid_number as \"uhIdNumber\", " 
				 + " 	padtl.address as \"address\","
				 + "    cmm.city_name as \"cityName\", " 
				 + " 	concat(coalesce(prifix.prefix_code, ''), ' ', coalesce(pr.first_name, '') , ' ', coalesce(pr.middle_name, '') , ' ', coalesce(pr.last_name, '')) as \"patientName\", " 
				 + " 	pr.gender_id as \"genderId\", " 
				 + " 	gm.gender_code as \"genderCode\", " 
				 + " 	gm.gender_name as \"genderName\", " 
				 + " 	pr.mobile_number as \"mobileNo\", " 
				 + " 	pr.first_name as \"firstName\", " 
				 + " 	pr.middle_name as \"middleName\", " 
				 + " 	pr.country_calling_code as \"countryCallingCode\", " 
				 + " 	pr.last_name as \"lastName\", " 
				 + " 	pr.email as \"email\", " 
				 + " 	pr.is_absconded as \"isAbsconded\", " 
				 + " 	id.identification_name as \"identificationName\", " 
				 + " 	coalesce(pr.identification_number, '') as \"identificationNo\", " 
				 + " 	to_char(pr.dob, 'DD/MM/YYYY') as \"birthDate\", " 
				 + " 	pr.dob as \"birthDateNew\", " 
				 + " 	concat(extract(year from age(current_timestamp, pr.dob)) , 'Y ' , extract(month from age(current_timestamp, pr.dob)), 'M ') as \"age\", " 
				 + " 	1 as \"patientType\", " 
				 + " 	pr.prefix_id as \"prefixId\", " 
				 + " 	pr.patient_category_id as \"patientCategoryId\", " 
				 + " 	pr.registration_type_id as \"registrationTypeId\", " 
				 + " 	cast(loyaltycard.card_no as text) as \"loyaltycardNo\", " 
				 + " 	coalesce(mppr.patient_loyalty_card_mppr_id, 0) as \"patientLoyaltyCardMpprId\" " 
				 + " from " 
				 + " 	patient.t_patient_registration as pr " 
				 + " left join public.m_prefix_master as prifix on " 
				 + " 	prifix.prefix_id = pr.prefix_id " 
				 + " left join public.m_loyalty_card loyaltycard on " 
				 + " 	pr.patient_id = loyaltycard.patient_id " 
				 + " 	and loyaltycard.is_deleted = 'N' " 
				 + " left join public.t_patient_loyalty_card_mppr mppr on " 
				 + " 	mppr.patient_id = loyaltycard.patient_id " 
				 + " 	and mppr.is_deleted = 'N' " 
				 + " inner join public.m_gender_master as gm on " 
				 + " 	gm.gender_id = pr.gender_id " 
				 + " left join adt.m_identification_master as id on " 
				 + " 	id.identification_id = pr.identification_type_id " 
				 + " inner join opd.t_encounter_master as enc on " 
				 + " 	enc.patient_id = pr.patient_id " 
				 + " left join patient.t_patient_details as padtl on " 
				 + "  padtl.patient_id = pr.patient_id " 
				 + " left join public.m_city_master as cmm on " 
				 + "  cmm.city_id = padtl.city_id " 
				 + " where " 
				 + " 	pr.status = 'A' " 
				 + " 	and pr.organization_id =:orgId " 
				 + " 	and enc.unit_id =:unitId " 
				 + " 	and pr.is_primary = 'Y' " 
				 + " 	and enc.status_id in (5,6,8) "  ;

		 query = new StringBuilder(strQuery);

// Append conditions based on non-null fields in patientSearchDto
		if (patientSearchDto.getIdentificationTypeId() != null && patientSearchDto.getIdentificationTypeId() > 0) {
			query.append(" AND pr.identification_type_id = :identificationTypeId ");
		}

		if (patientSearchDto.getUhIdNumber() != null && !patientSearchDto.getUhIdNumber().isEmpty()) {
			query.append(" AND lower(pr.uhid_number) LIKE :uhidNumber ");
		}

		if (patientSearchDto.getPatientName() != null && !patientSearchDto.getPatientName().isEmpty()) {
			query.append(" AND (lower(concat(coalesce(pr.first_name, ''), coalesce(pr.last_name, ''))) LIKE concat(lower(:patientName), '%') ")
					.append(" OR lower(concat(coalesce(pr.first_name), ' ', coalesce(pr.last_name, ''))) LIKE concat('%', lower(:patientName), '%') )");
		}

		if (patientSearchDto.getGenderId() != null) {
			query.append(" AND pr.gender_id = :genderId ");
		}

		if (patientSearchDto.getMobileNo() != null && !patientSearchDto.getMobileNo().isEmpty()) {
			query.append(" AND pr.mobile_number = :mobileNo ");
		}

		if (patientSearchDto.getBirthDate() != null && !patientSearchDto.getBirthDate().isEmpty()) {
			query.append(" AND pr.dob = :birthDate ");
		}

		if (patientSearchDto.getIdentificationNo() != null && !patientSearchDto.getIdentificationNo().isEmpty()) {
			query.append(" AND pr.identification_number LIKE :identificationNo ");
		}

		if (patientSearchDto.getPatientId() != null && patientSearchDto.getPatientId() > 0) {
			query.append(" AND pr.patient_id = :patientId ");
		}

		return query;

	}
	
	@Override
	public Response saveMedicoLegalDetails(AdmissionDto unAdmissionDto) throws ApplicationException {
		try {
			MedicoLeagal medicoLeagal = mapper.map(unAdmissionDto, MedicoLeagal.class,
					"AdmissionDto_to_MedicoLeagal");
			medicoLeagal.setCreatedDate(TimeZoneComponent.getDateByzone(unAdmissionDto.getUnitId() != null ? unAdmissionDto.getUnitId():0));
			medicoLeagal.setUpdatedDate(TimeZoneComponent.getDateByzone(unAdmissionDto.getUnitId() != null ? unAdmissionDto.getUnitId():0));
			medicoLeagal.setStatus(medicoLeagal.getStatus()=='\u0000'?'A':medicoLeagal.getStatus());
			if(unAdmissionDto.getMedicoLegalId()!=null) {
				unAdmissionDto.setMedicoLegalId(medicoLeagal.getMedicoLegalId());
				sessionFactory.getCurrentSession().update(medicoLeagal);
			}
			else {
					sessionFactory.getCurrentSession().save(medicoLeagal);
				}
			return new Response(SUCCESS, null, null, null, null);

		} catch (HibernateException he) {
		logger.error("saveMedicoLegalDetails failed", he);
		} catch (Exception e) {
		logger.error("saveMedicoLegalDetails failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}


	@Override
	public Long getNextValAdmissionId() throws ApplicationException {
		try {
			Session session = sessionFactory.getCurrentSession();
			SQLQuery query = session.createSQLQuery("select nextval('adt.admission_seq')");
			Long id = ((BigInteger) query.uniqueResult()).longValue();

			SQLQuery query2 = session.createSQLQuery("select setval('adt.admission_seq'," + (id-1) + ")");
			query2.uniqueResult();

			return id;
		}catch (HibernateException he) {
		logger.error("getNextValAdmissionId failed", he);
		} catch (Exception e) {
		logger.error("getNextValAdmissionId failed", e);
		}
		return null;
	}

	@Override
	public Response getListOfEncounterIdByPatientId(PatientSearchDto patientSearchDto)
			throws ApplicationException {
		
			try {
				 if(patientSearchDto.getVisitTypeId()== OP) {
					String query = "SELECT ent.patient_id as \"patientId\", ent.doctor_id as \"doctorId\",  "
							+ "ent.organization_id as \"organizationId\", ent.unit_id as \"unitId\", to_char(ent.encounter_date, 'yyyy-mm-dd') as \"fromDate\","
							+ "  to_char(ent.encounter_date, 'yyyy-mm-dd') as \"toDate\","
							+ "ent.encounter_id as \"encounterId\" "
							+ " FROM opd.t_encounter_master ent "
							+ " WHERE ent.patient_id=:patientId "
							+ " AND ent.organization_id= :organizationId"
							+ " AND unit_id= :unitId "
							+ " AND to_char(ent.encounter_date, 'YYYY-MM-DD')  BETWEEN  :fromDate AND :toDate ";
					
							List<PatientSearchDto> list = sessionFactory.getCurrentSession().createSQLQuery(query)
									.setParameter("patientId", patientSearchDto.getPatientId())
									.setParameter("organizationId", patientSearchDto.getOrganizationId())
									.setParameter("unitId", patientSearchDto.getUnitId())
									.setParameter("fromDate", patientSearchDto.getFromDate())
									.setParameter("toDate", patientSearchDto.getToDate())
									.setResultTransformer(Transformers.aliasToBean(PatientSearchDto.class)).list();
							return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
				 }
				else {
					 String query = "SELECT adm.patient_id as \"patientId\", adm.doctor_id as \"doctorId\",  " 
							 +  "adm.organization_id as \"organizationId\", adm.unit_id as \"unitId\","
					 		 +  " adm.admission_id as \"admissionId\""
					 		 +  " FROM adt.t_admission adm " 
					 		 +  " WHERE adm.patient_id=:patientId "
					 		 +  " AND adm.organization_id= :organizationId" 
					 		 +  " AND adm.unit_id= :unitId"  
							 + " AND to_char(adm.created_date,'YYYY-MM-DD')  BETWEEN  :fromDate AND :toDate ";
						
				List<PatientSearchDto> list = sessionFactory.getCurrentSession().createSQLQuery(query)
						.setParameter("patientId", patientSearchDto.getPatientId())
						.setParameter("organizationId", patientSearchDto.getOrganizationId())
						.setParameter("unitId", patientSearchDto.getUnitId())
						.setParameter("fromDate", patientSearchDto.getFromDate())
						.setParameter("toDate", patientSearchDto.getToDate())
						.setResultTransformer(Transformers.aliasToBean(PatientSearchDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
				 }
			
//			SELECT DATEADD(month, -1, '2017/08/25') AS DateAdd;
			
		
		} catch (HibernateException he) {
		logger.error("getListOfEncounterIdByPatientId failed", he);
		} catch (Exception e) {
		logger.error("getListOfEncounterIdByPatientId failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);

	}

	@Override
	public Response getListOfDocumentForBookView(PatientSearchDto patientSearchDto) throws ApplicationException {
		try {
			String query = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_LIST_OF_DOCUMENTS_FOR_BOOK_VIEW").getQueryString();
			StringBuilder sb = new StringBuilder(query);
			if (patientSearchDto.getDocumentTypeId() == null) {
				sb.append(" AND to_char(doc_dtls.created_date, 'YYYY-MM-DD') BETWEEN :fromDate AND :toDate");
			} else {
				sb.append(" AND doc_type_mst.document_type_id = :documentTypeId");
			}
			sb.append(" AND doc_dtls.patient_id = :patientId");

			sb.append(" ORDER BY doc_dtls.created_date DESC");

			SQLQuery query1 = sessionFactory.getCurrentSession().createSQLQuery(sb.toString());
			query1.setParameter("patientId", patientSearchDto.getPatientId());
			query1.setParameter("orgId", patientSearchDto.getOrganizationId());
			query1.setParameter("unitId", patientSearchDto.getUnitId());

			if (patientSearchDto.getDocumentTypeId() != null) {
				query1.setParameter("documentTypeId", patientSearchDto.getDocumentTypeId());
			}
			else{
				query1.setParameter("fromDate", patientSearchDto.getFromDate());
				query1.setParameter("toDate", patientSearchDto.getToDate());
			}

			List<PatientSearchDto> listPatientDocDto = query1.setResultTransformer(Transformers.aliasToBean(PatientSearchDto.class)).list();

			return new Response<>(SUCCESS, SUCCESS_CODE, null, listPatientDocDto, null);
		} catch (Exception e) {
		logger.error("getListOfDocumentForBookView failed", e);
		}
		return new Response(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	
	@Override
	public Response searchListOfPatientsByWardId(PatientSearchDto patientSearchDto) throws ApplicationException {
			try {
				Session session = sessionFactory.getCurrentSession();
				StringBuilder resQuery = this.searchPatientsByWardId (patientSearchDto);
				SQLQuery actualQuery = session.createSQLQuery(resQuery.toString());
				// Set the parameters for the query
				if(patientSearchDto.getPatientId() != null) {
					actualQuery.setParameter("patientId", patientSearchDto.getPatientId());
				}
				if(patientSearchDto.getIdentificationNo() != null && patientSearchDto.getIdentificationNo()!="" && !patientSearchDto.getIdentificationNo().isEmpty()) {
					actualQuery.setParameter("identificationNo", patientSearchDto.getIdentificationNo());
				}
					if(patientSearchDto.getBirthDate() != null &&  patientSearchDto.getBirthDate()!="") {
						actualQuery.setParameter("birthDate", patientSearchDto.getBirthDate());
					}
				if(patientSearchDto.getMobileNo() != null && patientSearchDto.getMobileNo()!="") {
					actualQuery.setParameter("mobileNo", patientSearchDto.getMobileNo());
				}
				if(patientSearchDto.getGenderId() != null ) {
					actualQuery.setParameter("genderId", patientSearchDto.getGenderId());
				}
				if(patientSearchDto.getUhIdNumber() != null && patientSearchDto.getUhIdNumber()!="") {
					actualQuery.setParameter("uhidNumber", patientSearchDto.getUhIdNumber());
				}
				if(patientSearchDto.getPatientName() != null && patientSearchDto.getPatientName()!="") {
					actualQuery.setParameter("patientName", patientSearchDto.getPatientName());
				}
				List<PatientSearchDto> bedMasterDtosList = actualQuery
						.setParameter("orgId", patientSearchDto.getOrganizationId())
						.setParameterList("wardList", patientSearchDto.getWardList())
						.setResultTransformer(Transformers.aliasToBean(PatientSearchDto.class)).list();
				return new Response(SUCCESS, null, null, bedMasterDtosList, null);

			} catch (HibernateException he) {
			logger.error("searchListOfPatientsByWardId failed", he);
			} catch (Exception e) {
			logger.error("searchListOfPatientsByWardId failed", e);
			}
			return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
		}

		private StringBuilder searchPatientsByWardId(PatientSearchDto patientSearchDto) 
		{
			StringBuilder query=new StringBuilder();

			String strQuery="SELECT distinct (pr.patient_id) as \"patientId\", pr.image_path as \"patientProfile\", "
				+" pr.uhid_number as \"uhIdNumber\", "
				+" concat(coalesce(pr.first_name,'') ,' ', coalesce(pr.middle_name,'') ,' ', coalesce(pr.last_name,'')) as \"patientName\",  " 
				+" pr.gender_id as \"genderId\",   "
				+" gm.gender_code as \"genderCode\",  gm.gender_name as \"genderName\","
				+ "pr.mobile_number as \"mobileNo\", "
				+ "pr.first_name as \"firstName\", "
				+ "pr.middle_name as \"middleName\", "
				+ "pr.last_name as \"lastName\", "
				+ "adm.doctor_id as \"doctorId\", "
				+ "spon.payment_entitlement_id as \"paymentEntitlementId\", " 
				+ "spon.contract_id as \"contractId\", "
				+ "adm.speciality_id as \"specialityId\", "
				+ "adnm.billing_bed_category_id as \"billingBedCategoryId\", "
				+ " wdmst.ward_name as \"wardName\", " 
				+ " bm.bed_number as \"bedNumber\", " 
				+ " to_char(adnm.doa, 'DD/MM/YYYY') as \"doa\", " 
				+ " to_char(adnm.pdd, 'DD/MM/YYYY') as \"pdd\", " 
				+ "pr.email as \"email\",pr.is_absconded as \"isAbsconded\", "
				+ "id.identification_name as \"identificationName\", "
				+ "coalesce(pr.identification_number,'') as \"identificationNo\", "
				+" to_char(pr.dob,'DD/MM/YYYY') as \"birthDate\","
				+ " concat(coalesce(docmst.first_name,'') ,' ', coalesce(docmst.middle_name,'') ,' ', coalesce(docmst.last_name,'')) as \"doctorName\", "
				+ "concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, pr.dob)) , 'Y ' , EXTRACT(month from AGE(CURRENT_TIMESTAMP, pr.dob)),'M ') as \"age\", "
				+ "1 as \"patientType\","
				+ "pr.prefix_id as \"prefixId\", "
				+ "pr.patient_category_id as \"patientCategoryId\", "
				+ "pr.registration_type_id as \"registrationTypeId\", "
				+ "(select adm.admission_id  from adt.t_admission adm where adm.patient_id = pr.patient_id and status='A' order by 1 desc limit 1) as \"admissionId\", "
				+ "(select adm.visit_type_id  from adt.t_admission adm where adm.patient_id = pr.patient_id and status='A' order by 1 desc limit 1) as \"visitTypeId\", "
				+ "(select to_char(adm.created_date,'DD/MM/YYYY')  from adt.t_admission adm where adm.patient_id = pr.patient_id and status='A' order by 1 desc limit 1) as \"createdDate\", "
				+ "(select to_char(adm.created_date,'HH:mm:ss')  from adt.t_admission adm where adm.patient_id = pr.patient_id and status='A' order by 1 desc limit 1) as \"createdTime\", "
				+ "cast(loyaltycard.card_no as text) as  \"loyaltycardNo\", "
				+ "coalesce(mppr.patient_loyalty_card_mppr_id,0) as \"patientLoyaltyCardMpprId\", "
				+ "(select app.appointment_id from opd.m_appointment_master app where  app.appointment_status_id=8 and app.patient_id=pr.patient_id order by 1 desc limit 1) as \"appointmentId\" "
				+ "FROM patient.t_patient_registration as pr "
				+ "LEFT JOIN public.m_loyalty_card loyaltycard on pr.patient_id= loyaltycard.patient_id  and loyaltycard.is_deleted = 'N' "
				+ "LEFT JOIN public.t_patient_loyalty_card_mppr mppr on mppr.patient_id= loyaltycard.patient_id and mppr.is_deleted = 'N' "
				+ "INNER JOIN public.m_gender_master as gm	on gm.gender_id= pr.gender_id "
				+ "LEFT JOIN adt.m_identification_master as id on id.identification_id = pr.identification_type_id	"

				+ "LEFT JOIN adt.t_admission as adm on adm.patient_id = pr.patient_id "
				+ " LEFT JOIN adt.t_admission_details adnm on adnm.admission_id = adm.admission_id  "
				+" left JOIN adt.m_ward_master as wdmst  on wdmst.ward_id = adnm.ward_id	"
				+" left JOIN doctor.m_doctor_master as docmst  on docmst.doctor_id = adm.doctor_id"
				+ "  LEFT JOIN adt.m_bed_master bm on adnm.bed_id = bm.bed_id "
				+ " left join opd.t_patient_sponsor_details spon on spon.admission_id = adm.admission_id "
				+  " and spon.status='A' and spon.priority_id=1 "

				//+ "LEFT JOIN opd.m_appointment_master as app on app.patient_id = pr.patient_id AND app.appointment_status_id=8 "
				+ " WHERE pr.status='A' "
				+ "AND adnm.status='A'"
				+ " AND pr.organization_id=:orgId "
				+ " AND pr.is_primary='Y' "
				+ " AND bm.ward_id IN (:wardList) " ;
			StringBuilder sb = new StringBuilder(strQuery);
			StringBuilder ANDPatientSearchID = new StringBuilder(" AND pr.patient_id = :patientId");
			StringBuilder ANDIdentification = new StringBuilder(" AND lower(pr.identification_number) LIKE concat('%',LOWER(:identificationNo),'%')");
			StringBuilder ANDDob = new StringBuilder(" AND pr.dob = :birthDate");
			StringBuilder ANDMobile = new StringBuilder(" AND pr.mobile_number = :mobileNo");
			StringBuilder ANDGenderId = new StringBuilder(" AND pr.gender_id = :genderId");
			StringBuilder ANDUhid = new StringBuilder(" AND lower(pr.uhid_number) LIKE concat('%',LOWER(:uhidNumber),'%')");

			if (patientSearchDto.getUhIdNumber() != null && !patientSearchDto.getUhIdNumber().isEmpty()) {
				sb.append(ANDUhid);
			}
			if (patientSearchDto.getPatientName() != null && !patientSearchDto.getPatientName().isEmpty()) {
				sb.append(" AND ((")
						.append("lower(coalesce(pr.first_name, '')) LIKE concat(lower(:patientName), '%')")
						.append(" OR lower(coalesce(pr.last_name, '')) LIKE concat(lower(:patientName), '%')")
						.append(") OR lower(concat(coalesce(pr.first_name), ' ', coalesce(pr.last_name, ''))) LIKE concat('%', lower(:patientName), '%'))");
			}
			if (patientSearchDto.getGenderId() != null) {
				sb.append(ANDGenderId);
			}
			if (patientSearchDto.getMobileNo() != null && !patientSearchDto.getMobileNo().isEmpty()) {
				sb.append(ANDMobile);
			}
			if (patientSearchDto.getBirthDate() != null && !patientSearchDto.getBirthDate().isEmpty()) {
				sb.append(ANDDob);
			}
			if (patientSearchDto.getIdentificationNo() != null && !patientSearchDto.getIdentificationNo().isEmpty()) {
				sb.append(ANDIdentification);
			}
			if (patientSearchDto.getPatientId() != null && patientSearchDto.getPatientId() > 0) {
				sb.append(ANDPatientSearchID);
			}

			return sb;

		}

		@Override
		public Response patientSearchByForTransferICU(PatientSearchDto patientSearchDto) throws ApplicationException {
			try {
				Session session = sessionFactory.getCurrentSession();
				StringBuilder resQuery = this.searchPatientForTransferICU(patientSearchDto);
				SQLQuery actualQuery = session.createSQLQuery(resQuery.toString());

				if (patientSearchDto.getPatientId() != null && patientSearchDto.getPatientId() > 0) {
					actualQuery.setParameter("patientId", patientSearchDto.getPatientId());
				}
				if (patientSearchDto.getIdentificationNo() != null && !patientSearchDto.getIdentificationNo().isEmpty()) {
					actualQuery.setParameter("identificationNo", patientSearchDto.getIdentificationNo());
				}
				if (patientSearchDto.getBirthDate() != null && !patientSearchDto.getBirthDate().isEmpty()) {
					actualQuery.setParameter("birthDate", patientSearchDto.getBirthDate());
				}
				if (patientSearchDto.getMobileNo() != null && !patientSearchDto.getMobileNo().isEmpty()) {
					actualQuery.setParameter("mobileNo", patientSearchDto.getMobileNo());
				}
				if (patientSearchDto.getGenderId() != null) {
					actualQuery.setParameter("genderId", patientSearchDto.getGenderId());
				}
				if (patientSearchDto.getPatientName() != null && !patientSearchDto.getPatientName().isEmpty()) {
					actualQuery.setParameter("patientName", patientSearchDto.getPatientName());
				}
				if (patientSearchDto.getUhIdNumber() != null && !patientSearchDto.getUhIdNumber().isEmpty()) {
					actualQuery.setParameter("uhidNumber", patientSearchDto.getUhIdNumber());
				}

				List<PatientSearchDto> bedMasterDtosList = null;
				if((patientSearchDto.getWardList().isEmpty() && patientSearchDto.getBedList().isEmpty()))
				{
					 	bedMasterDtosList = actualQuery
						.setParameter("orgId", patientSearchDto.getOrganizationId())
						//.setParameter("visitTypeId", patientSearchDto.getVisitTypeId())
						.setParameter("unitId", patientSearchDto.getUnitId())
						.setResultTransformer(Transformers.aliasToBean(PatientSearchDto.class)).list();
				}
				if((patientSearchDto.getWardList().isEmpty() && patientSearchDto.getBedList()!=null) &&  patientSearchDto.getBedList().size() > 0)
				{
					 	bedMasterDtosList = actualQuery
						.setParameter("orgId", patientSearchDto.getOrganizationId())
						//.setParameter("visitTypeId", patientSearchDto.getVisitTypeId())
						.setParameter("unitId", patientSearchDto.getUnitId())
						.setParameterList("bedList", patientSearchDto.getBedList())
						.setResultTransformer(Transformers.aliasToBean(PatientSearchDto.class)).list();
				}
				if((patientSearchDto.getWardList()!=null && patientSearchDto.getBedList().isEmpty()) &&  patientSearchDto.getWardList().size() > 0)
				{
					 	bedMasterDtosList = actualQuery
						.setParameter("orgId", patientSearchDto.getOrganizationId())
						//.setParameter("visitTypeId", patientSearchDto.getVisitTypeId())
						.setParameter("unitId", patientSearchDto.getUnitId())
						.setParameterList("wardList", patientSearchDto.getWardList())
						.setResultTransformer(Transformers.aliasToBean(PatientSearchDto.class)).list();
				}
				//System.out.println("resQuery"+resQuery);
				//System.out.println("actualQuery"+actualQuery);
				return new Response(SUCCESS, null, null, bedMasterDtosList, null);

			} catch (HibernateException he) {
			logger.error("patientSearchByForTransferICU failed", he);
			} catch (Exception e) {
			logger.error("patientSearchByForTransferICU failed", e);
			}
			return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
		}

		private StringBuilder searchPatientForTransferICU(PatientSearchDto patientSearchDto) 
		{
			StringBuilder query=new StringBuilder();
			
			String strQuery="SELECT distinct (pr.patient_id) as \"patientId\", pr.image_path as \"patientProfile\", "
				+" pr.uhid_number as \"uhIdNumber\", "
				+" concat(coalesce(pr.first_name,'') ,' ', coalesce(pr.middle_name,'') ,' ', coalesce(pr.last_name,'')) as \"patientName\",  " 
				+" pr.gender_id as \"genderId\",   "
				+" gm.gender_code as \"genderCode\",  gm.gender_name as \"genderName\","
				+ "pr.mobile_number as \"mobileNo\", "
				+ "pr.first_name as \"firstName\", "
				+ "pr.middle_name as \"middleName\", "
				+ "pr.last_name as \"lastName\", "
				+ "pr.email as \"email\",pr.is_absconded as \"isAbsconded\", "
				+ "id.identification_name as \"identificationName\", "
				+ "coalesce(pr.identification_number,'') as \"identificationNo\", "
				+" to_char(pr.dob,'DD/MM/YYYY') as \"birthDate\","
				+ "concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, pr.dob)) , 'Y ' , EXTRACT(month from AGE(CURRENT_TIMESTAMP, pr.dob)),'M ') as \"age\", "
				+ "1 as \"patientType\","
				+ "pr.prefix_id as \"prefixId\", "
				+ "pr.patient_category_id as \"patientCategoryId\", "
				+ "pr.registration_type_id as \"registrationTypeId\", "
				+ " adm.admission_id as \"admissionId\", "
				+ " adm.visit_type_id as \"visitTypeId\", "
				+ "cast(loyaltycard.card_no as text) as  \"loyaltycardNo\", "
				+ "coalesce(mppr.patient_loyalty_card_mppr_id,0) as \"patientLoyaltyCardMpprId\" "
				+ "FROM patient.t_patient_registration as pr "
				+ "LEFT JOIN public.m_loyalty_card loyaltycard on pr.patient_id= loyaltycard.patient_id  and loyaltycard.is_deleted = 'N' "
				+ "LEFT JOIN public.t_patient_loyalty_card_mppr mppr on mppr.patient_id= loyaltycard.patient_id and mppr.is_deleted = 'N' "
				+ "INNER JOIN public.m_gender_master as gm	on gm.gender_id= pr.gender_id "
				+ "LEFT JOIN adt.m_identification_master as id on id.identification_id = pr.identification_type_id	"
				+ "INNER JOIN adt.t_admission as adm on adm.patient_id = pr.patient_id  "
				+ "INNER JOIN adt.t_admission_details as adtl on adtl.admission_id = adm.admission_id "
				+ " WHERE pr.status='A' "
				+ " AND pr.organization_id=:orgId "
				+ " AND adm.unit_id=:unitId "
				+ " AND pr.is_primary='Y' "
				+ " AND adm.status='A' "
				+ " AND adtl.status='A' "
				+ " AND adtl.nursing_acceptance = 'Y' ";
			
			  // 
			StringBuilder sb = new StringBuilder(strQuery);

// Create individual conditions with placeholders
			StringBuilder ANDPatientSearchID = new StringBuilder(" AND pr.patient_id = :patientId");
			StringBuilder ANDIdentification = new StringBuilder(" AND lower(pr.identification_number) LIKE concat('%',LOWER(:identificationNo),'%')");
			StringBuilder ANDDob = new StringBuilder(" AND pr.dob = :birthDate");
			StringBuilder ANDMobile = new StringBuilder(" AND pr.mobile_number = :mobileNo");
			StringBuilder ANDGenderId = new StringBuilder(" AND pr.gender_id = :genderId");
			StringBuilder ANDUhid = new StringBuilder(" AND lower(pr.uhid_number) LIKE concat('%',LOWER(:uhidNumber),'%')");

			if (patientSearchDto.getUhIdNumber() != null && !patientSearchDto.getUhIdNumber().isEmpty()) {
				sb.append(ANDUhid);
			}
			if (patientSearchDto.getPatientName() != null && !patientSearchDto.getPatientName().isEmpty()) {
				sb.append(" AND ((")
						.append("lower(coalesce(pr.first_name, '')) LIKE concat(lower(:patientName), '%')")
						.append(" OR lower(coalesce(pr.last_name, '')) LIKE concat(lower(:patientName), '%')")
						.append(") OR lower(concat(coalesce(pr.first_name), ' ', coalesce(pr.last_name, ''))) LIKE concat('%', lower(:patientName), '%'))");
			}
			if (patientSearchDto.getGenderId() != null) {
				sb.append(ANDGenderId);
			}
			if (patientSearchDto.getMobileNo() != null && !patientSearchDto.getMobileNo().isEmpty()) {
				sb.append(ANDMobile);
			}
			if (patientSearchDto.getBirthDate() != null && !patientSearchDto.getBirthDate().isEmpty()) {
				sb.append(ANDDob);
			}
			if (patientSearchDto.getIdentificationNo() != null && !patientSearchDto.getIdentificationNo().isEmpty()) {
				sb.append(ANDIdentification);
			}
			if (patientSearchDto.getPatientId() != null && patientSearchDto.getPatientId() > 0) {
				sb.append(ANDPatientSearchID);
			}

			return sb;

		}

		@Override
		public Response getMedicolegalDetailsForIcon(AdmissionDto admissionDto) throws ApplicationException {
			try {
				
				String query = "SELECT " + 
								" medico.medico_legal_id as \"medicoLegalId\", " + 
								" medico.admission_id as \"admissionId\", " + 
								" medico.accompanied_by as \"accompaniedBy\", " + 
								" medico.priority_id as \"priorityId\", " + 
								" medico.remark as \"remark\", " + 
								" medico.fir_no as \"firNo\", " + 
								" medico.modeof_arrival as \"modeOfArrival\", " +
								" medico.police_buckle_no as \"policeBuckleNo\", " + 
								" medico.police_station_name as \"policeStationName\", " + 
								" medico.mlc_contact_no as \"mlcContactNo\", " + 
								" medico.mlc_relation_id as \"mlcRelationId\"," + 
								" rlmst.relation_name as \"relationName\"," + 
								" rlmst.relation_id as \"relationId\"," + 
								" prmst.priority_code as \"priorityCode\"" + 
								" FROM adt.t_medico_legal medico " + 
								" LEFT JOIN public.m_relation_master rlmst on rlmst.relation_id=medico.mlc_relation_id" + 
								" LEFT JOIN adt.m_priority_master prmst on prmst.priority_id=medico.priority_id" + 
								" WHERE medico.admission_id=:admissionId" + 
								" AND medico.organization_id=:organizationId";
				
				List<AdmissionDto> list = sessionFactory.getCurrentSession().createSQLQuery(query)
						.setParameter("admissionId", admissionDto.getAdmissionId())
						.setParameter("organizationId", admissionDto.getOrganizationId())
						.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);

			} catch (HibernateException he) {
				throw new HibernateException(he.getMessage());
			} catch (Exception e) {
			logger.error("getMedicolegalDetailsForIcon failed", e);
			}
			return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
		}

		@Override
		public Response getPatientListMedicolegalDetailsMLCWorklist(AdmissionDto admissionDto)
				throws ApplicationException {
			try {
				Session session = sessionFactory.getCurrentSession();
				StringBuilder IPQuery=new StringBuilder("SELECT admission.admission_id as \"admissionId\", "
						       + "  admission.patient_id as \"patientId\", " 
						       + " mdl.medico_legal_id as \"medicoLegalId\", "
						       + " mdl.accompanied_by as \"accompaniedBy\", " 
						       +  " mdl.priority_id as \"priorityId\", "
						       +  " mdl.remark as \"remark\", " 
						       +  " mdl.fir_no as \"firNo\", " 
						       + " mdl.police_buckle_no as \"policeBuckleNo\", " 
						       +  " mdl.police_station_name as \"policeStationName\", " 
						       + " mdl.mlc_contact_no as \"mlcContactNo\", "
						       +  " mdl.mlc_relation_id as \"mlcRelationId\","
						       + "  admission.admission_number as \"admissionNumber\", " 
						       + "  admission.visit_type_id as \"visitTypeId\", "
						       + " vp.visit_type_name as \"visitTypeName\","
							   + "  admission.doctor_id as \"doctorId\", "
						       + "  admission.speciality_id as \"specialityId\", "
						       + "  sp.speciality_name as \"specialityName\", "
						       + "  pr.uhid_number as \"UHIDNumber\", " 
						       +  "	 pr.prefix_id as \"prefixId\", " 
						       +  "  prefix.prefix_code as \"prefixCode\", "
						       +  "  pr.gender_id as \"genderId\", "
						       +  "  gm.gender_code as \"genderCode\", " 
						       +  " to_char(pr.dob, 'MM/dd/yyyy') as \"dob\", " 
						       +  " concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, pr.dob))) as \"ageYear\", "
						       +  "  concat(COALESCE(doc.first_name,''),' ',COALESCE(doc.middle_name,''),' ',COALESCE(doc.last_name,'')) as \"dFirstName\", " 
						       + "  to_char(adl.doa, 'dd/MM/yyyy') as \"doa\", " 
						       + "  to_char(adl.pdd, 'dd/MM/yyyy') as \"pdd\", "
						       + "  bedCatMst.bed_category_desc as \"bedCategoryDesc\", " 
						       + "  bedCatMst.billing_bed_category_id as \"billingBedCategoryId\", "
						       + "  adl.admission_type_id as \"admissionTypeId\", "
						       + "  bedCatMst.bed_category_id as \"bedCategoryId\", " 
						       + "  bm.floor_id as \"floorId\", "
						       + "  bm.bed_number as \"bedNumber\", " 
						       + "  bm.bed_id as \"bedId\", " 
						       + "  bm.ward_id as \"wardId\", " 
						       + "  ward.ward_name as \"wardName\", " 
						       +"  pr.patient_category_id as \"patientCategoryId\", " 
						       +  "  admission.concent as \"concent\", "
						       + "  to_char(admission.created_date, 'dd/mm/yyyy') as \"createdDate\", "
						       + "  to_char(admission.created_date, 'HH:mm') as \"createdTime\", " 
						       +  "  pr.is_vip as \"isVip\", "
						       + "  (case when pr.is_vip='Y' then coalesce(pr.alise_name,pr.first_name) else  concat(coalesce(pr.first_name) , ' ', coalesce(pr.middle_name) , ' ', coalesce(pr.last_name))  end) as \"pFirstName\" "
						       + " FROM adt.t_medico_legal mdl"
						       + "  left join adt.t_admission admission on admission.admission_id=mdl.admission_id " 
						       + "  inner JOIN patient.t_patient_registration pr on admission.patient_id = pr.patient_id " 
						       +"  left JOIN public.m_gender_master gm on pr.gender_id = gm.gender_id "
						       +"  left JOIN doctor.m_doctor_master doc on admission.doctor_id = doc.doctor_id "
						       + "  left JOIN doctor.m_speciality_master sp on admission.speciality_id = sp.speciality_id " 
						       + "  left JOIN adt.t_admission_details adl on adl.admission_id = admission.admission_id " 
						       + "  left JOIN adt.m_bed_master bm on adl.bed_id = bm.bed_id "
						       + "  left JOIN adt.m_ward_master ward on bm.ward_id = ward.ward_id "
						       + "  left JOIN  public.m_prefix_master prefix ON  prefix.prefix_id = pr.prefix_id " 
						       +"  left JOIN adt.m_bed_category_master bedCatMst on adl.bed_category_id = bedCatMst.bed_category_id "
						       + " left JOIN public.m_visit_type_master vp on admission.visit_type_id = vp.visit_type_id " 
						       + " WHERE admission.status = 'A' AND adl.status = 'A'" 
						       + "  and admission.organization_id = :organizationId" 
						       + "  and admission.unit_id = :unitId");

				 IPQuery = new StringBuilder(IPQuery);

// Append conditions dynamically
				if (admissionDto.getFromDate() != null && admissionDto.getToDate() != null) {
					IPQuery.append(" AND mdl.created_date BETWEEN :fromDate AND :toDate");
				}
				if (admissionDto.getVisitList() != null && !admissionDto.getVisitList().isEmpty()) {
					IPQuery.append(" AND admission.visit_type_id IN :visitList");
				}
				if (admissionDto.getPatientId() != null && admissionDto.getPatientId() > 0) {
					IPQuery.append(" AND pr.patient_id = :patientId");
				}
				Query query2 = session.createSQLQuery(IPQuery.toString());
				if (admissionDto.getFromDate() != null && admissionDto.getToDate() != null) {
					String fromDateStr = admissionDto.getFromDate().substring(0, 10) + " 00:00:00";
					String toDateStr = admissionDto.getToDate().substring(0, 10) + " 23:59:59";

                    Timestamp fromDate = Timestamp.valueOf(fromDateStr);
                    Timestamp toDate   = Timestamp.valueOf(toDateStr);

					query2.setParameter("fromDate", fromDate);
					query2.setParameter("toDate", toDate);
				}
				if (admissionDto.getVisitList() != null && !admissionDto.getVisitList().isEmpty()) {
					query2.setParameterList("visitList", admissionDto.getVisitList());
				}
				if (admissionDto.getPatientId() != null && admissionDto.getPatientId() > 0) {
					query2.setParameter("patientId", admissionDto.getPatientId());
				}

				query2.setParameter("organizationId", admissionDto.getOrganizationId());
				query2.setParameter("unitId", admissionDto.getUnitId());
				query2.setFirstResult(admissionDto.getOffset() != null ? admissionDto.getOffset() : 0);
				query2.setMaxResults(admissionDto.getNoOfRecordsPerPage() != null ? admissionDto.getNoOfRecordsPerPage() : 10);
				List<AdmissionDto> list1 = query2
						.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class))
						.list();

				StringBuilder countQuery = new StringBuilder(IPQuery.toString());


				Integer count=0;
				Query createdQuery = sessionFactory.getCurrentSession()
				          .createSQLQuery(countQuery.toString());

                if (admissionDto.getFromDate() != null && admissionDto.getToDate() != null) {
                    String fromDateStr = admissionDto.getFromDate().substring(0, 10) + " 00:00:00";
                    String toDateStr = admissionDto.getToDate().substring(0, 10) + " 23:59:59";

                    Timestamp fromDate = Timestamp.valueOf(fromDateStr);
                    Timestamp toDate   = Timestamp.valueOf(toDateStr);

                    createdQuery.setParameter("fromDate", fromDate);
                    createdQuery.setParameter("toDate", toDate);
                }
                if (admissionDto.getVisitList() != null && !admissionDto.getVisitList().isEmpty()) {
                    createdQuery.setParameterList("visitList", admissionDto.getVisitList());
                }
                if (admissionDto.getPatientId() != null && admissionDto.getPatientId() > 0) {
                    createdQuery.setParameter("patientId", admissionDto.getPatientId());
                }
				
				List<AdmissionDto> listCount = createdQuery
						.setParameter("organizationId", admissionDto.getOrganizationId())
						.setParameter("unitId", admissionDto.getUnitId())
						.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
				
				  if(list1 != null && list1.size()>0) {
			    	  count = listCount.size();
			    	  
			      }
				
				return new Response<>(SUCCESS, SUCCESS_CODE, null, list1, count);
			

			} catch (HibernateException he) {
				throw new HibernateException(he.getMessage());
			} catch (Exception e) {
			logger.error("getPatientListMedicolegalDetailsMLCWorklist failed", e);
			}
			return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
		}

		@Override
		public Response getMedicolegalDetailsMLCWorklistCount(AdmissionDto admissionDto) throws ApplicationException {
			try {
				
				Session session = sessionFactory.getCurrentSession();
				Criteria crit = session.createCriteria(MedicoLeagal.class);
				crit.add(Restrictions.eq("organizationId", admissionDto.getOrganizationId()));
				crit.add(Restrictions.eq("unitId", admissionDto.getUnitId()));
				crit.setProjection(Projections.rowCount());
				Long count = (Long) crit.uniqueResult();
				
				return new Response(SUCCESS, SUCCESS_CODE, null, null, count);
					
			} catch (HibernateException he) {
			logger.error("getMedicolegalDetailsMLCWorklistCount failed", he);
			} catch (Exception e) {
			logger.error("getMedicolegalDetailsMLCWorklistCount failed", e);
			}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}

		@Override
		public Response searchListOfPatientsByDoctorIdForIOChart(PatientSearchDto patientSearchDto)
				throws ApplicationException {
			try {
				Session session = sessionFactory.getCurrentSession();
				StringBuilder resQuery = this.searchPatientsByDoctorId(patientSearchDto);
				SQLQuery actualQuery = session.createSQLQuery(resQuery.toString());
				// Set parameters dynamically
				if (patientSearchDto.getUhIdNumber() != null && !patientSearchDto.getUhIdNumber().isEmpty()) {
					actualQuery.setParameter("uhIdNumber", patientSearchDto.getUhIdNumber());
				}
				if (patientSearchDto.getPatientName() != null && !patientSearchDto.getPatientName().isEmpty()) {
					actualQuery.setParameter("patientName", patientSearchDto.getPatientName());
				}
				if (patientSearchDto.getGenderId() != null) {
					actualQuery.setParameter("genderId", patientSearchDto.getGenderId());
				}
				if (patientSearchDto.getMobileNo() != null && !patientSearchDto.getMobileNo().isEmpty()) {
					actualQuery.setParameter("mobileNo", patientSearchDto.getMobileNo());
				}
				if (patientSearchDto.getBirthDate() != null && !patientSearchDto.getBirthDate().isEmpty()) {
					actualQuery.setParameter("birthDate", patientSearchDto.getBirthDate());
				}
				if (patientSearchDto.getIdentificationNo() != null && !patientSearchDto.getIdentificationNo().isEmpty()) {
					actualQuery.setParameter("identificationNo", patientSearchDto.getIdentificationNo());
				}
				if (patientSearchDto.getPatientId() != null && patientSearchDto.getPatientId() > 0) {
					actualQuery.setParameter("patientId", patientSearchDto.getPatientId());
				}
				List<PatientSearchDto> bedMasterDtosList = actualQuery
						.setParameter("orgId", patientSearchDto.getOrganizationId())
						.setParameter("doctorId", patientSearchDto.getDoctorId())
						.setResultTransformer(Transformers.aliasToBean(PatientSearchDto.class)).list();
				return new Response(SUCCESS, null, null, bedMasterDtosList, null);

			} catch (HibernateException he) {
			logger.error("searchListOfPatientsByDoctorIdForIOChart failed", he);
			} catch (Exception e) {
			logger.error("searchListOfPatientsByDoctorIdForIOChart failed", e);
			}
			return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
		}

		
		private StringBuilder searchPatientsByDoctorId(PatientSearchDto patientSearchDto) 
		{
			StringBuilder query=new StringBuilder();

			String strQuery="SELECT distinct (pr.patient_id) as \"patientId\", pr.image_path as \"patientProfile\", "
				+" pr.uhid_number as \"uhIdNumber\", "
				+" concat(coalesce(pr.first_name,'') ,' ', coalesce(pr.middle_name,'') ,' ', coalesce(pr.last_name,'')) as \"patientName\",  " 
				+" pr.gender_id as \"genderId\",   "
				+" gm.gender_code as \"genderCode\",  gm.gender_name as \"genderName\","
				+ "pr.mobile_number as \"mobileNo\", "
				+ "pr.first_name as \"firstName\", "
				+ "pr.middle_name as \"middleName\", "
				+ "pr.last_name as \"lastName\", "
				+ "adm.doctor_id as \"doctorId\", "
				+ "spon.payment_entitlement_id as \"paymentEntitlementId\", " 
				+ "spon.contract_id as \"contractId\", "
				+ "adm.speciality_id as \"specialityId\", "
				+ "adnm.billing_bed_category_id as \"billingBedCategoryId\", "
				+ " wdmst.ward_name as \"wardName\", " 
				+ " bm.bed_number as \"bedNumber\", " 
				+ " to_char(adnm.doa, 'DD/MM/YYYY') as \"doa\", " 
				+ " to_char(adnm.pdd, 'DD/MM/YYYY') as \"pdd\", " 
				+ "pr.email as \"email\",pr.is_absconded as \"isAbsconded\", "
				+ "id.identification_name as \"identificationName\", "
				+ "coalesce(pr.identification_number,'') as \"identificationNo\", "
				+" to_char(pr.dob,'DD/MM/YYYY') as \"birthDate\","
				+ " concat(coalesce(docmst.first_name,'') ,' ', coalesce(docmst.middle_name,'') ,' ', coalesce(docmst.last_name,'')) as \"doctorName\", "
				+ "concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, pr.dob)) , 'Y ' , EXTRACT(month from AGE(CURRENT_TIMESTAMP, pr.dob)),'M ') as \"age\", "
				+ "1 as \"patientType\","
				+ "pr.prefix_id as \"prefixId\", "
				+ "pr.patient_category_id as \"patientCategoryId\", "
				+ "pr.registration_type_id as \"registrationTypeId\", "
				+ "(select adm.admission_id  from adt.t_admission adm where adm.patient_id = pr.patient_id and status='A' order by 1 desc limit 1) as \"admissionId\", "
				+ "(select adm.visit_type_id  from adt.t_admission adm where adm.patient_id = pr.patient_id and status='A' order by 1 desc limit 1) as \"visitTypeId\", "
				+ "(select to_char(adm.created_date,'DD/MM/YYYY')  from adt.t_admission adm where adm.patient_id = pr.patient_id and status='A' order by 1 desc limit 1) as \"createdDate\", "
				+ "(select to_char(adm.created_date,'HH:mm:ss')  from adt.t_admission adm where adm.patient_id = pr.patient_id and status='A' order by 1 desc limit 1) as \"createdTime\", "
				+ "cast(loyaltycard.card_no as text) as  \"loyaltycardNo\", "
				+ "coalesce(mppr.patient_loyalty_card_mppr_id,0) as \"patientLoyaltyCardMpprId\", "
				+ "(select app.appointment_id from opd.m_appointment_master app where  app.appointment_status_id=8 and app.patient_id=pr.patient_id order by 1 desc limit 1) as \"appointmentId\" "
				+ "FROM patient.t_patient_registration as pr "
				+ "LEFT JOIN public.m_loyalty_card loyaltycard on pr.patient_id= loyaltycard.patient_id  and loyaltycard.is_deleted = 'N' "
				+ "LEFT JOIN public.t_patient_loyalty_card_mppr mppr on mppr.patient_id= loyaltycard.patient_id and mppr.is_deleted = 'N' "
				+ "INNER JOIN public.m_gender_master as gm	on gm.gender_id= pr.gender_id "
				+ "LEFT JOIN adt.m_identification_master as id on id.identification_id = pr.identification_type_id	"

				+ "LEFT JOIN adt.t_admission as adm on adm.patient_id = pr.patient_id "
				+ " LEFT JOIN adt.t_admission_details adnm on adnm.admission_id = adm.admission_id  "
				+" left JOIN adt.m_ward_master as wdmst  on wdmst.ward_id = adnm.ward_id	"
				+" left JOIN doctor.m_doctor_master as docmst  on docmst.doctor_id = adm.doctor_id"
				+ "  LEFT JOIN adt.m_bed_master bm on adnm.bed_id = bm.bed_id "
				+ " left join opd.t_patient_sponsor_details spon on spon.admission_id = adm.admission_id "
				+  " and spon.status='A' and spon.priority_id=1 "
				+ " WHERE pr.status='A' "
				+ "AND adnm.status='A'"
				+ " AND pr.organization_id=:orgId "
				+ " AND pr.is_primary='Y' "
				+ " AND adm.doctor_id = :doctorId " ;

			 query = new StringBuilder(strQuery);

// Dynamic conditions
			if (patientSearchDto.getUhIdNumber() != null && !patientSearchDto.getUhIdNumber().isEmpty()) {
				query.append(" AND lower(pr.uhid_number) LIKE concat('%', LOWER(:uhIdNumber), '%')");
			}
			if (patientSearchDto.getPatientName() != null && !patientSearchDto.getPatientName().isEmpty()) {
				query.append(" AND ((lower(coalesce(pr.first_name, '')) LIKE concat(lower(:patientName), '%') " +
						"OR lower(coalesce(pr.last_name, '')) LIKE concat(lower(:patientName), '%')) " +
						"OR lower(concat(coalesce(pr.first_name, ''), ' ', coalesce(pr.last_name, ''))) LIKE concat('%', lower(:patientName), '%'))");
			}
			if (patientSearchDto.getGenderId() != null) {
				query.append(" AND pr.gender_id = :genderId");
			}
			if (patientSearchDto.getMobileNo() != null && !patientSearchDto.getMobileNo().isEmpty()) {
				query.append(" AND pr.mobile_number = :mobileNo");
			}
			if (patientSearchDto.getBirthDate() != null && !patientSearchDto.getBirthDate().isEmpty()) {
				query.append(" AND pr.dob = :birthDate");
			}
			if (patientSearchDto.getIdentificationNo() != null && !patientSearchDto.getIdentificationNo().isEmpty()) {
				query.append(" AND lower(pr.identification_number) LIKE concat('%', LOWER(:identificationNo), '%')");
			}
			if (patientSearchDto.getPatientId() != null && patientSearchDto.getPatientId() > 0) {
				query.append(" AND pr.patient_id = :patientId");
			}

			return query;
		}

		@Override
		public Response patientSearchByMultipleCriteriaWithDeathPatient(PatientSearchDto patientSearchDto)
				throws ApplicationException {
			try {
				Session session = sessionFactory.getCurrentSession();
				StringBuilder resQuery = this.searchPatientWithDeathPatient(patientSearchDto);
				SQLQuery actualQuery = session.createSQLQuery(resQuery.toString());

	            if(patientSearchDto.getOffset()!=null && patientSearchDto.getNoOfRecordsPerPage()!=null)
	            {
	                actualQuery.setFirstResult(patientSearchDto.getOffset());
	                actualQuery.setMaxResults(patientSearchDto.getNoOfRecordsPerPage());
	            }
				if (patientSearchDto.getUhIdNumber() != null && !patientSearchDto.getUhIdNumber().isEmpty()) {
					actualQuery.setParameter("uhIdNumber", patientSearchDto.getUhIdNumber());
				}
				if (patientSearchDto.getPatientName() != null && !patientSearchDto.getPatientName().isEmpty()) {
					actualQuery.setParameter("patientName", patientSearchDto.getPatientName());
				}
				if (patientSearchDto.getGenderId() != null) {
					actualQuery.setParameter("genderId", patientSearchDto.getGenderId());
				}
				if (patientSearchDto.getMobileNo() != null && !patientSearchDto.getMobileNo().isEmpty()) {
					actualQuery.setParameter("mobileNo", patientSearchDto.getMobileNo() + "%");
				}
				if (patientSearchDto.getBirthDate() != null && !patientSearchDto.getBirthDate().isEmpty()) {
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
					LocalDate localDate = LocalDate.parse(patientSearchDto.getBirthDate(), formatter);
					java.sql.Timestamp birthDateTime = java.sql.Timestamp.valueOf(localDate.atStartOfDay());

					actualQuery.setParameter("birthDate", birthDateTime);
				}
				if (patientSearchDto.getIdentificationTypeId() != null && patientSearchDto.getIdentificationTypeId() > 0) {
					actualQuery.setParameter("identificationTypeId", patientSearchDto.getIdentificationTypeId());
				}
				if (patientSearchDto.getIdentificationNo() != null && !patientSearchDto.getIdentificationNo().isEmpty()) {
					actualQuery.setParameter("identificationNo", patientSearchDto.getIdentificationNo());
				}
				if (patientSearchDto.getPatientId() != null && patientSearchDto.getPatientId() > 0) {
					actualQuery.setParameter("patientId", patientSearchDto.getPatientId());
				}

				List<PatientSearchDto> bedMasterDtosList = actualQuery
						.setParameter("orgId", patientSearchDto.getOrganizationId())
						.setResultTransformer(Transformers.aliasToBean(PatientSearchDto.class)).list();
				return new Response(SUCCESS, null, null, bedMasterDtosList, null);

			} catch (HibernateException he) {
			logger.error("patientSearchByMultipleCriteriaWithDeathPatient failed", he);
			} catch (Exception e) {
			logger.error("patientSearchByMultipleCriteriaWithDeathPatient failed", e);
			}
			return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
		}

		private StringBuilder searchPatientWithDeathPatient(PatientSearchDto patientSearchDto) {
			Date date = TimeZoneComponent.getDateByzone(patientSearchDto.getUnitId());  
			String currentDate=GlobalCommonDateUtils.getStringDate(date, "yyyy-MM-dd");
		    //System.out.println(currentDate); 

			StringBuilder query=new StringBuilder();
			
			String strQuery="select " 
				 + " 	distinct (pr.patient_id) as \"patientId\", " 
				 + " 	pr.image_path as \"patientProfile\", " 
				 + " 	pr.uhid_number as \"uhIdNumber\", " 
				 + " 	concat(coalesce(prifix.prefix_code, ''), ' ', coalesce(pr.first_name, '') , ' ', coalesce(pr.middle_name, '') , ' ', coalesce(pr.last_name, '')) as \"toPatientName\", "
				 + " 	concat(coalesce(prifix.prefix_code, ''), ' ', coalesce(pr.first_name, '') , ' ', coalesce(pr.middle_name, '') , ' ', coalesce(pr.last_name, '')) as \"patientName\", " 
				 + " 	pr.gender_id as \"genderId\", " 
				 + " 	pr.file_sequence_number as \"fileSequenceNumber\", " 
				 + " 	gm.gender_code as \"genderCode\", " 
				 + " 	gm.gender_name as \"genderName\", " 
				 + " 	pr.mobile_number as \"mobileNo\", " 
				 + " 	pr.first_name as \"firstName\", " 
				 + " 	pr.middle_name as \"middleName\", " 
				 + " 	pr.last_name as \"lastName\", " 
				 + " 	pr.email as \"email\", " 
				 + " 	pr.is_absconded as \"isAbsconded\", " 
				 + " 	id.identification_name as \"identificationName\", " 
				 + " 	coalesce(pr.identification_number, '') as \"identificationNo\", "
				 + "	(case when id.is_expiry_required='Y' then "
				 + "		to_char(pr.identification_expiry_date, 'YYYY-MM-DD') end) as \"identificationExpiryDate\", " 
				 + " 	to_char(pr.dob, 'DD/MM/YYYY') as \"birthDate\", " 
				 + " 	pr.dob as \"birthDateNew\", " 
				 + " 	concat(extract(year from age(current_timestamp, pr.dob)) , 'Y') as \"age\", " 
				 + " 	1 as \"patientType\", " 
				 + " 	pr.prefix_id as \"prefixId\", " 
				 + " 	pr.patient_category_id as \"patientCategoryId\", " 
				 + " 	pr.registration_type_id as \"registrationTypeId\", " 
				 + " 	wdmst.ward_name as \"wardName\", " 
				 + " 	bdmst.bed_number as \"bedNumber\", " 
				 + " 	to_char(admdtl.doa, 'DD/MM/YYYY') as \"doa\", " 
				 + " 	to_char(admdtl.pdd, 'DD/MM/YYYY') as \"pdd\", " 
				 + " 	(case when admmst.admission_id > 0 and admmst.visit_type_id !=4 then "
				 + " 		concat('Dr. ',coalesce(docmst.first_name, '') , ' ', coalesce(docmst.middle_name, '') , ' ', coalesce(docmst.last_name, '')) end) as \"doctorName\", " 
				 + " 	( " 
				 + " 	select " 
				 + " 		enc.encounter_id " 
				 + " 	from " 
				 + " 		opd.t_encounter_master enc " 
				 + " 	inner join opd.m_appointment_master app on " 
				 + " 		enc.appointment_id = app.appointment_id " 
				 + " 	where " 
				 + " 		enc.status_id != 6 " 
				 + " 		and enc.patient_id = pr.patient_id " 
				 + " 		and to_char(app.appointment_date, 'yyyy-MM-dd')= '"+currentDate+"' " 
				 + " 	order by " 
				 + " 		enc.encounter_id asc " 
				 + " 	limit 1) as \"encounterId\" , " 
				 + " 	admmst.admission_id as \"admissionId\", " 
				 + " 	admmst.unit_id as \"admissionUnitId\", " 
				 + " 	patDtls.country_id as \"countryId\", " 
				 + " 	patDtls.district_id as \"districtId\", " 
				 + " 	patDtls.address as \"address\", " 
				 + " (case " 
				 + "  when dishh.is_death  is not null then dishh.is_death " + 
				 " 		else 'N'" + 
				 " 	end) as \"isDeath\", "
				 + " 	admmst.visit_type_id as \"visitTypeId\" " 
				 + " from "
				 + " 	patient.t_patient_registration as pr " 
				 + " inner join patient.t_patient_details patDtls on " 
				 + " 	pr.patient_id = patDtls.patient_id and patDtls.status='A' " 
				 + " inner join public.m_gender_master as gm on " 
				 + " 	gm.gender_id = pr.gender_id " 
				 + " left join public.m_prefix_master as prifix on " 
				 + " 	prifix.prefix_id = pr.prefix_id " 
				 + " left join adt.m_identification_master as id on " 
				 + " 	id.identification_id = pr.identification_type_id " 
				 + " left join adt.t_admission as admmst on " 
				 + " 	admmst.patient_id = pr.patient_id " 
				 + " 	and admmst.status = 'A' " 
				 + " left join adt.t_admission_details as admdtl on " 
				 + " 	admmst.admission_id = admdtl.admission_id " 
				 + " 	and admdtl.status = 'A' " 
				 + " left join adt.m_ward_master as wdmst on " 
				 + " 	wdmst.ward_id = admdtl.ward_id " 
				 + " left join adt.m_bed_master as bdmst on " 
				 + " 	bdmst.bed_id = admdtl.bed_id " 
				 + " left join doctor.m_doctor_master as docmst on " 
				 + " 	docmst.doctor_id = admmst.doctor_id " 
				 + " left join adt.t_discharge as dishh on " 
				 + " 	dishh.admission_id = admmst.admission_id " 
				 + " where " 
				 + " 	pr.organization_id =:orgId "
				 + " 	and pr.is_primary = 'Y' " ;

			query.append(strQuery);

// Dynamic conditions
			if (patientSearchDto.getUhIdNumber() != null && !patientSearchDto.getUhIdNumber().isEmpty()) {
				query.append(" AND lower(pr.uhid_number) LIKE concat('%', LOWER(:uhIdNumber), '%') ");
			}
			if (patientSearchDto.getPatientName() != null && !patientSearchDto.getPatientName().isEmpty()) {
				query.append(" AND ((lower(coalesce(pr.first_name, '')) LIKE concat(lower(:patientName), '%') " +
						"OR lower(coalesce(pr.last_name, '')) LIKE concat(lower(:patientName), '%')) " +
						"OR lower(concat(coalesce(pr.first_name, ''), ' ', coalesce(pr.last_name, ''))) LIKE concat('%', lower(:patientName), '%'))");
			}
			if (patientSearchDto.getGenderId() != null) {
				query.append(" AND pr.gender_id = :genderId ");
			}
			if (patientSearchDto.getMobileNo() != null && !patientSearchDto.getMobileNo().isEmpty()) {
				query.append(" AND pr.mobile_number LIKE :mobileNo ");
			}
			if (patientSearchDto.getBirthDate() != null && !patientSearchDto.getBirthDate().isEmpty()) {
				query.append(" AND pr.dob = :birthDate ");
			}
			if (patientSearchDto.getIdentificationTypeId() != null && patientSearchDto.getIdentificationTypeId() > 0) {
				query.append(" AND pr.identification_type_id = :identificationTypeId ");
			}
			if (patientSearchDto.getIdentificationNo() != null && !patientSearchDto.getIdentificationNo().isEmpty()) {
				query.append(" AND lower(coalesce(pr.identification_number, '')) LIKE concat('%', LOWER(:identificationNo), '%') ");
			}
			if (patientSearchDto.getPatientId() != null && patientSearchDto.getPatientId() > 0) {
				query.append(" AND pr.patient_id = :patientId ");
			}


			return query;
		}
		
		@Override
		public Response patientSearchByMultipleCriteriaGlobal(PatientSearchDto patientSearchDto) throws ApplicationException {
			try {
				Session session = sessionFactory.getCurrentSession();
				StringBuilder resQuery = this.searchPatient1(patientSearchDto);
				SQLQuery actualQuery = session.createSQLQuery(resQuery.toString());

				// Set Parameters
				if (patientSearchDto.getUhidNumber() != null && !patientSearchDto.getUhidNumber().isEmpty()) {
					actualQuery.setParameter("uhidNumber", patientSearchDto.getUhidNumber());
				}
				if (patientSearchDto.getPatientName() != null && !patientSearchDto.getPatientName().isEmpty()) {
					actualQuery.setParameter("patientName", patientSearchDto.getPatientName());
				}
				if (patientSearchDto.getGenderId() != null) {
					actualQuery.setParameter("genderId", patientSearchDto.getGenderId());
				}
				if (patientSearchDto.getMobileNumber() != null && !patientSearchDto.getMobileNumber().isEmpty()) {
					actualQuery.setParameter("mobileNumber", patientSearchDto.getMobileNumber() + "%");
				}
				if (patientSearchDto.getBirthDate() != null && !patientSearchDto.getBirthDate().isEmpty()) {
					actualQuery.setParameter("birthDate", patientSearchDto.getBirthDate());
				}
				if (patientSearchDto.getIdentificationTypeId() != null && patientSearchDto.getIdentificationTypeId() > 0) {
					actualQuery.setParameter("identificationTypeId", patientSearchDto.getIdentificationTypeId());
				}
				if (patientSearchDto.getIdentificationNumber() != null && !patientSearchDto.getIdentificationNumber().isEmpty()) {
					actualQuery.setParameter("identificationNumber", patientSearchDto.getIdentificationNumber());
				}
				if (patientSearchDto.getPatientId() != null && patientSearchDto.getPatientId() > 0) {
					actualQuery.setParameter("patientId", patientSearchDto.getPatientId());
				}
				if (patientSearchDto.getWardList() != null && !patientSearchDto.getWardList().isEmpty() && patientSearchDto.getVisitTypeId() != OP) {
					actualQuery.setParameterList("wardList", patientSearchDto.getWardList());
				}
				if (patientSearchDto.getVisitTypeId() != OP) {
					actualQuery.setParameter("visitTypeId", patientSearchDto.getVisitTypeId());
				}


				List<PatientSearchDto> bedMasterDtosList = actualQuery
						.setParameter("orgId", patientSearchDto.getOrganizationId())
						.setResultTransformer(Transformers.aliasToBean(PatientSearchDto.class)).list();
				for (PatientSearchDto patientSearchDto2 : bedMasterDtosList) {
					patientSearchDto2.setIdentificationType(patientSearchDto2.getIdentificationName());
				List<VisitDetails> visitDetails = new ArrayList<VisitDetails>();

				if (patientSearchDto2.getVisitTypeId() !=null && patientSearchDto2.getVisitTypeId() == OP) {
					try {
						String hql = " SELECT encounter FROM Encounters AS encounter "
								+ " INNER JOIN encounter.patient AS patient WITH patient.status = 'A' "
								+ " LEFT JOIN encounter.visitType AS visitType WITH visitType.status = 'A' "
								+ " LEFT JOIN encounter.doctor AS doctor WITH doctor.status = 'A' "
								+ " WHERE encounter.status = 'A' AND encounter.unitId = :unitId "
								+ " AND patient.id = :patientId  AND  encounter.statusId in (5,6,8) ";

						Query query = getSession().createQuery(hql);
						query.setParameter("patientId", patientSearchDto2.getPatientId());
						query.setParameter("unitId", patientSearchDto.getUnitId());

						List<Encounter> encounters= query.list();
						if (encounters != null && !encounters.isEmpty()) {
							for (Encounter encounter : encounters) {
								VisitDetails visitDetail = new VisitDetails(
										encounter.getDoctor() != null ? encounter.getDoctor().getDoctorId() : null,
										encounter.getEncounterNumber(), encounter.getEncounterDate());
								visitDetail.setEncounterId(encounter.getEncounterId());
								visitDetail.setAddedDate(encounter.getUpdatedDate());
								visitDetails.add(visitDetail);
								patientSearchDto2.setVisitType(encounter.getVisitType().getVisitTypeName());
							}
						} else
							throw new CustomException("Encounters not found for the patient");
					} catch (Exception e) {
					logger.error("patientSearchByMultipleCriteriaGlobal: encounter visit lookup failed", e);
					}
				}
				if (patientSearchDto2.getVisitTypeId() !=null && (patientSearchDto2.getVisitTypeId() == IP || patientSearchDto2.getVisitTypeId() == ER
						|| patientSearchDto2.getVisitTypeId() == DC)) {
					
						String hql = " SELECT admission FROM Admissions AS admission "
								+ " INNER JOIN admission.patient AS patient WITH patient.status = 'A' "
								+ " INNER JOIN admission.visitType AS visitType WITH visitType.status = 'A' "
								+ " LEFT JOIN admission.doctor AS doctor WITH doctor.status = 'A' "
								+ " INNER JOIN admission.admissionDetail AS admissionDetail WITH admissionDetail.status = 'A' AND admissionDetail.nursingAcceptance='Y' "
								+ " LEFT JOIN admissionDetail.bedMaster AS bedMaster WITH bedMaster.status = 'A' "
								+ " LEFT JOIN admissionDetail.wardMaster AS wardMaster WITH wardMaster.status = 'A' "
								+ " WHERE admission.status = 'A' AND admissionDetail.isDischarge != 'D' AND admission.unit.unitId = :unitId "
								+ " AND ( patient.id = :patientId ) "
								+ " AND ( visitType.visitTypeId = :visitType OR :visitType = 0 ) ORDER BY admission.admissionId DESC ";
						Query query = getSession().createQuery(hql);
						query.setParameter("visitType",
								patientSearchDto2.getVisitTypeId() != null ? patientSearchDto2.getVisitTypeId() : 0);
						query.setParameter("patientId", patientSearchDto2.getPatientId());
						query.setParameter("unitId", patientSearchDto.getUnitId());
						query.setMaxResults(1);

						com.param.entity.model.adt.Admission admission= (com.param.entity.model.adt.Admission) query.uniqueResult();
					// patientDetailsS
					if (admission != null) {

						Integer drId = 0;
						if (patientSearchDto2.getVisitTypeId() == ER) {

						} else {
							if (admission.getDoctor() != null) {
								if (admission.getDoctor() == null) {

								} else {
									drId = admission.getDoctor().getDoctorId();
								}

							}
						}

						VisitDetails visitDetail = new VisitDetails(drId, admission.getAdmissionNumber());
						visitDetail.setAdmissionId(admission.getAdmissionId());
						visitDetail.setAddedDate(admission.getUpdatedDate());
						visitDetails.add(visitDetail);
						patientSearchDto2.setVisitType(admission.getVisitType().getVisitTypeName());
						
					} 
				}

				patientSearchDto2.setVisitDetails(visitDetails);
				}
				return new Response(SUCCESS, null, null, bedMasterDtosList, null);

			} catch (HibernateException he) {
			logger.error("patientSearchByMultipleCriteriaGlobal failed", he);
			} catch (Exception e) {
			logger.error("patientSearchByMultipleCriteriaGlobal failed", e);
			}
			return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
		}
		private StringBuilder searchPatient1(PatientSearchDto patientSearchDto) 
		{
			Date date = TimeZoneComponent.getDateByzone(patientSearchDto.getUnitId());  
			String currentDate=GlobalCommonDateUtils.getStringDate(date, "yyyy-MM-dd");
		    //System.out.println(currentDate); 

			StringBuilder query=new StringBuilder();
			
			String strQuery="select " 
				 + " 	distinct (pr.patient_id) as \"patientId\", " 
				 + " 	pr.image_path as \"patientProfile\", " 
				 + " 	pr.uhid_number as \"uhIdNumber\", " 
				 + " 	concat(coalesce(prifix.prefix_code, ''), ' ', coalesce(pr.first_name, '') , ' ', coalesce(pr.middle_name, '') , ' ', coalesce(pr.last_name, '')) as \"toPatientName\", "
				 + " 	concat(coalesce(prifix.prefix_code, ''), ' ', coalesce(pr.first_name, '') , ' ', coalesce(pr.middle_name, '') , ' ', coalesce(pr.last_name, '')) as \"patientName\", " 
				 + " 	pr.gender_id as \"genderId\", " 
				 + " 	pr.file_sequence_number as \"fileSequenceNumber\", " 
				 + " 	gm.gender_code as \"genderCode\", " 
				 + " 	gm.gender_name as \"genderName\", " 
				 + " 	pr.mobile_number as \"mobileNo\", " 
				 + " 	pr.first_name as \"firstName\", " 
				 + " 	pr.middle_name as \"middleName\", " 
				 + " 	pr.last_name as \"lastName\", "
				 + "    pr.video_call_url as \"videoCallUrl\", " 
				 + " 	pr.email as \"email\", " 
				 + " 	pr.is_absconded as \"isAbsconded\", " 
				 + " 	id.identification_name as \"identificationName\", " 
				 + " 	coalesce(pr.identification_number, '') as \"identificationNo\", "
				 + "	(case when id.is_expiry_required='Y' then "
				 + "		to_char(pr.identification_expiry_date, 'YYYY-MM-DD') end) as \"identificationExpiryDate\", " 
				 + " 	to_char(pr.dob, 'DD/MM/YYYY') as \"birthDate\", " 
				 + " 	pr.dob as \"birthDateNew\", " 
				 + " 	concat(extract(year from age(current_timestamp, pr.dob)) , 'Y') as \"age\", " 
				 + " 	1 as \"patientType\", " 
				 + " 	pr.prefix_id as \"prefixId\", " 
				 + " 	pr.patient_category_id as \"patientCategoryId\", " 
				 + " 	pr.registration_type_id as \"registrationTypeId\", " 
				 + " 	wdmst.ward_name as \"wardName\", "
				 + " 	wdmst.ward_id as \"wardId\", " 
				 + " 	bdmst.bed_number as \"bedNumber\", "
				 + " 	bdmst.bed_id as \"bedId\", "
				 + " 	to_char(admdtl.doa, 'DD/MM/YYYY') as \"doa\", " 
				 + " 	to_char(admdtl.pdd, 'DD/MM/YYYY') as \"pdd\", " 
				 + " 	(case when admmst.admission_id > 0 and admmst.visit_type_id !=4 then "
				 + " 		concat('Dr. ',coalesce(docmst.first_name, '') , ' ', coalesce(docmst.middle_name, '') , ' ', coalesce(docmst.last_name, '')) end) as \"doctorName\", " 
				 + " 	( " 
				 + " 	select " 
				 + " 		enc.encounter_id " 
				 + " 	from " 
				 + " 		opd.t_encounter_master enc " 
				 + " 	inner join opd.m_appointment_master app on " 
				 + " 		enc.appointment_id = app.appointment_id " 
				 + " 	where " 
				 + " 		enc.status_id != 6 " 
				 + " 		and enc.patient_id = pr.patient_id " 
				 + " 		and to_char(app.appointment_date, 'yyyy-MM-dd')= '"+currentDate+"' " 
				 + " 	order by " 
				 + " 		enc.encounter_id asc " 
				 + " 	limit 1) as \"encounterId\" , " 
				 + " 	admmst.admission_id as \"admissionId\", " 
				 + " 	admmst.unit_id as \"admissionUnitId\", " 
				 + " 	patDtls.country_id as \"countryId\", " 
				 + " 	patDtls.district_id as \"districtId\", " 
				 + " 	patDtls.address as \"address\", " 
				 + " (case " 
				 + "  when dishh.is_death  is not null then dishh.is_death " + 
				 " 		else 'N'" + 
				 " 	end) as \"isDeath\", "
				 + " 	admmst.visit_type_id as \"visitTypeId\","
				 + " admmst.is_final_bill as  \"isFinalBill\" " 
				 + " from " 
				 + " 	patient.t_patient_registration as pr " 
				 + " inner join patient.t_patient_details patDtls on " 
				 + " 	pr.patient_id = patDtls.patient_id and patDtls.status='A' " 
				 + " inner join public.m_gender_master as gm on " 
				 + " 	gm.gender_id = pr.gender_id " 
				 + " left join public.m_prefix_master as prifix on " 
				 + " 	prifix.prefix_id = pr.prefix_id " 
				 + " left join adt.m_identification_master as id on " 
				 + " 	id.identification_id = pr.identification_type_id " 
				 + " left join adt.t_admission as admmst on " 
				 + " 	admmst.patient_id = pr.patient_id " 
				 + " 	and admmst.status = 'A' " 
				 + " left join adt.t_admission_details as admdtl on " 
				 + " 	admmst.admission_id = admdtl.admission_id " 
				 + " 	and admdtl.status = 'A' "
				 + " left join opd.t_encounter_master tem on tem.patient_id = pr.patient_id " + 
				   " and tem.status = 'A' " 
				 + " left join adt.m_ward_master as wdmst on " 
				 + " 	wdmst.ward_id = admdtl.ward_id " 
				 + "  LEFT JOIN adt.t_nursing_station_ward_mapper nurWard on wdmst.ward_id = nurWard.ward_id "
				 + "  LEFT JOIN adt.m_nursing_station_master nur on nurWard.nursing_station_id = nur.nursing_station_id "
				 + " left join adt.m_bed_master as bdmst on " 
				 + " 	bdmst.bed_id = admdtl.bed_id " 
				 + " left join doctor.m_doctor_master as docmst on " 
				 + " 	docmst.doctor_id = admmst.doctor_id " 
				 + " left join adt.t_discharge as dishh on " 
				 + " 	dishh.admission_id = admmst.admission_id " 
				 + " where " 
				 + " 	pr.status = 'A' " 
				 + " 	and pr.organization_id =:orgId " 
				 + " 	and pr.is_primary = 'Y' " ;
				// + "    and admmst.visit_type_id="+patientSearchDto.getVisitTypeId();

			query.append(strQuery);

// UHID Number
			if (patientSearchDto.getUhidNumber() != null && !patientSearchDto.getUhidNumber().isEmpty()) {
				query.append(" AND lower(pr.uhid_number) LIKE concat('%', LOWER(:uhidNumber), '%') ");
			}

// Patient Name
			if (patientSearchDto.getPatientName() != null && !patientSearchDto.getPatientName().isEmpty()) {
				query.append(" AND ((lower(coalesce(pr.first_name, '')) LIKE concat(lower(:patientName), '%') " +
						"OR lower(coalesce(pr.last_name, '')) LIKE concat(lower(:patientName), '%')) " +
						"OR lower(concat(coalesce(pr.first_name, ''), ' ', coalesce(pr.last_name, ''))) LIKE concat('%', lower(:patientName), '%'))");
			}

// Gender ID
			if (patientSearchDto.getGenderId() != null) {
				query.append(" AND pr.gender_id = :genderId ");
			}

// Mobile Number
			if (patientSearchDto.getMobileNumber() != null && !patientSearchDto.getMobileNumber().isEmpty()) {
				query.append(" AND pr.mobile_number LIKE :mobileNumber ");
			}

// Birth Date
			if (patientSearchDto.getBirthDate() != null && !patientSearchDto.getBirthDate().isEmpty()) {
				query.append(" AND pr.dob = :birthDate ");
			}

// Identification Type ID
			if (patientSearchDto.getIdentificationTypeId() != null && patientSearchDto.getIdentificationTypeId() > 0) {
				query.append(" AND pr.identification_type_id = :identificationTypeId ");
			}

// Identification Number
			if (patientSearchDto.getIdentificationNumber() != null && !patientSearchDto.getIdentificationNumber().isEmpty()) {
				query.append(" AND lower(coalesce(pr.identification_number, '')) LIKE concat('%', LOWER(:identificationNumber), '%') ");
			}

// Patient ID
			if (patientSearchDto.getPatientId() != null && patientSearchDto.getPatientId() > 0) {
				query.append(" AND pr.patient_id = :patientId ");
			}

// Ward and Visit Type ID Conditions
			if (patientSearchDto.getVisitTypeId() != OP) {
				if (patientSearchDto.getWardList() != null && !patientSearchDto.getWardList().isEmpty()) {
					query.append(" AND wdmst.ward_id IN (:wardList) ");
				} else {
					query.append(" AND wdmst.ward_id IN (0) ");
				}
				query.append(" AND admmst.visit_type_id = :visitTypeId ");
			} else {
				query.append(" AND tem.encounter_id IS NOT NULL ");
			}

			return query;
		}

		@Override
		public Response getIpRenderedDateChange(PatientSearchDto patientSearchDto) {
	        String sql = "select  ip_rendered_date_change as ipRenderedDateChange "
	                + "from public.t_user_unit_mapper where user_id=:userId and unit_id=:unitId and organisation_id=:orgId and status='A' order by 1 desc";

	        SQLQuery query = getSession().createSQLQuery(sql);
	        query.setParameter("userId", patientSearchDto.getUserId());
	        query.setParameter("unitId", patientSearchDto.getUnitId());
	        query.setParameter("orgId", patientSearchDto.getOrganizationId());
	        query.setMaxResults(1);
	        List<PatientSearchDto> res=query.list();
	        return new Response(SUCCESS, null, null, res, null);
		}

	@Override
	public Response patientSearchByMultipleCriteriaForICUAndER(PatientSearchDto patientSearchDto) throws ApplicationException {
		try{
			Session session = sessionFactory.getCurrentSession();
			StringBuilder resQuery = this.searchPatientForICUAndER(patientSearchDto);
			SQLQuery actualQuery = session.createSQLQuery(resQuery.toString());

			if (patientSearchDto.getUhIdNumber() != null && !patientSearchDto.getUhIdNumber().isEmpty()) {
				actualQuery.setParameter("uhidNumber", patientSearchDto.getUhIdNumber());
			}
			if (patientSearchDto.getPatientName() != null && !patientSearchDto.getPatientName().isEmpty()) {
				actualQuery.setParameter("patientName", patientSearchDto.getPatientName());
			}
			if (patientSearchDto.getGenderId() != null) {
				actualQuery.setParameter("genderId", patientSearchDto.getGenderId());
			}
			if (patientSearchDto.getMobileNo() != null && !patientSearchDto.getMobileNo().isEmpty()) {
				actualQuery.setParameter("mobileNo", patientSearchDto.getMobileNo());
			}
			if (patientSearchDto.getBirthDate() != null && !patientSearchDto.getBirthDate().isEmpty()) {
				actualQuery.setParameter("birthDate", patientSearchDto.getBirthDate());
			}
			if (patientSearchDto.getIdentificationNo() != null && !patientSearchDto.getIdentificationNo().isEmpty()) {
				actualQuery.setParameter("identificationNo", patientSearchDto.getIdentificationNo());
			}
			if (patientSearchDto.getPatientId() != null && patientSearchDto.getPatientId() > 0) {
				actualQuery.setParameter("patientId", patientSearchDto.getPatientId());
			}
			if (patientSearchDto.getWardList() != null && !patientSearchDto.getWardList().isEmpty()) {
				actualQuery.setParameterList("wardIds", patientSearchDto.getWardList());
			}
			if (patientSearchDto.getDoctorId() != null) {
				actualQuery.setParameter("doctorId", patientSearchDto.getDoctorId());
			}

			List<PatientSearchDto> bedMasterDtosList = null;

			if(patientSearchDto.getDoctorId()!=null) {
				bedMasterDtosList = actualQuery
						.setParameter("orgId", patientSearchDto.getOrganizationId())
						.setParameter("unitId", patientSearchDto.getUnitId())
						.setResultTransformer(Transformers.aliasToBean(PatientSearchDto.class)).list();
			}else{
				bedMasterDtosList = actualQuery
						.setParameter("orgId", patientSearchDto.getOrganizationId())
						.setParameter("unitId", patientSearchDto.getUnitId())
						.setParameterList("wardIds", patientSearchDto.getWardList())
						.setResultTransformer(Transformers.aliasToBean(PatientSearchDto.class)).list();
			}

			return new Response(SUCCESS, null, null, bedMasterDtosList, null);

		}catch (Exception e){
			throw e;
		}
	}

	private StringBuilder searchPatientForICUAndER(PatientSearchDto patientSearchDto)
	{
		StringBuilder query=new StringBuilder();

		String strQuery=" select "
				+ " 	distinct (pr.patient_id) as \"patientId\", "
				+ " 	pr.image_path as \"patientProfile\", "
				+ " 	pr.uhid_number as \"uhIdNumber\", "
				+ " 	concat(coalesce(prifix.prefix_code, ''), ' ', coalesce(pr.first_name, '') , ' ', coalesce(pr.middle_name, '') , ' ', coalesce(pr.last_name, '')) as \"patientName\", "
				+ " 	pr.gender_id as \"genderId\", "
				+ " 	gm.gender_code as \"genderCode\", "
				+ " 	gm.gender_name as \"genderName\", "
				+ " 	pr.mobile_number as \"mobileNo\", "
				+ " 	pr.first_name as \"firstName\", "
				+ " 	pr.middle_name as \"middleName\", "
				+ " 	pr.last_name as \"lastName\", "
				+ " 	pr.email as \"email\", "
				+ " 	pr.is_absconded as \"isAbsconded\", "
				+ " 	id.identification_name as \"identificationName\", "
				+ " 	coalesce(pr.identification_number, '') as \"identificationNo\", "
				+ " 	to_char(pr.dob, 'DD/MM/YYYY') as \"birthDate\", "
				+ " 	concat(extract(year from age(current_timestamp, pr.dob)) , 'Y ' , extract(month from age(current_timestamp, pr.dob)), 'M ') as \"age\", "
				+ " 	1 as \"patientType\", "
				+ " 	pr.prefix_id as \"prefixId\", "
				+ "    to_char(adtl.doa, 'DD/MM/YYYY') as \"doa\", "
				+ " 	pr.patient_category_id as \"patientCategoryId\", "
				+ " 	pr.registration_type_id as \"registrationTypeId\", "
				+ "     padtl.address as \"address\","
				+ "    cmm.city_name as \"cityName\", "
				+ " 	adm.admission_id as \"admissionId\", "
				+ " 	adm.visit_type_id as \"visitTypeId\", "
				+ " 	cast(loyaltycard.card_no as text) as \"loyaltycardNo\", "
				+ " 	coalesce(mppr.patient_loyalty_card_mppr_id, 0) as \"patientLoyaltyCardMpprId\" "
				+ " from "
				+ " 	patient.t_patient_registration as pr "
				+ " left join public.m_prefix_master as prifix on  "
				+ "  	prifix.prefix_id = pr.prefix_id 	 "
				+ " left join public.m_loyalty_card loyaltycard on "
				+ " 	pr.patient_id = loyaltycard.patient_id "
				+ " 	and loyaltycard.is_deleted = 'N' "
				+ " left join public.t_patient_loyalty_card_mppr mppr on "
				+ " 	mppr.patient_id = loyaltycard.patient_id "
				+ " 	and mppr.is_deleted = 'N' "
				+ " inner join public.m_gender_master as gm on "
				+ " 	gm.gender_id = pr.gender_id "
				+ " left join adt.m_identification_master as id on "
				+ " 	id.identification_id = pr.identification_type_id "
				+ " inner join adt.t_admission as adm on "
				+ " 	adm.patient_id = pr.patient_id "
				+ " inner join adt.t_admission_details as adtl on "
				+ " 	adtl.admission_id = adm.admission_id "
				+ " left join patient.t_patient_details as padtl on "
				+ "  padtl.patient_id = pr.patient_id "
				+ " left join adt.m_ward_master as wdmst on "
				+ " 	wdmst.ward_id = adtl.ward_id "
				+ " left join public.m_city_master as cmm on "
				+ "  cmm.city_id = padtl.city_id "
				+ " where "
				+ " 	pr.status = 'A' "
				+ " 	and pr.organization_id =:orgId "
				+ " 	and adm.unit_id =:unitId "
				+ " 	and pr.is_primary = 'Y' "
				+ " 	and adm.status = 'A' "
				+ " 	and adtl.status = 'A' "
				+ " 	and adtl.nursing_acceptance = 'Y' "
				+ " 	and( wdmst.is_er= 'Y' or wdmst.is_icu= 'Y' ) "
				+ " 	and adtl.bed_id is not null " ;

		query.append(strQuery);
		if (patientSearchDto.getUhIdNumber() != null && !patientSearchDto.getUhIdNumber().isEmpty()) {
			query.append(" AND lower(pr.uhid_number) LIKE concat('%', LOWER(:uhidNumber), '%') ");
		}
		if (patientSearchDto.getPatientName() != null && !patientSearchDto.getPatientName().isEmpty()) {
			query.append(" AND (lower(concat(coalesce(pr.first_name, ''), coalesce(pr.last_name, ''))) LIKE concat(lower(:patientName), '%') " +
					"OR lower(concat(coalesce(pr.first_name, ''), ' ', coalesce(pr.last_name, ''))) LIKE concat('%', lower(:patientName), '%')) ");
		}
		if (patientSearchDto.getGenderId() != null) {
			query.append(" AND pr.gender_id = :genderId ");
		}
		if (patientSearchDto.getMobileNo() != null && !patientSearchDto.getMobileNo().isEmpty()) {
			query.append(" AND pr.mobile_number = :mobileNo ");
		}
		if (patientSearchDto.getBirthDate() != null && !patientSearchDto.getBirthDate().isEmpty()) {
			query.append(" AND pr.dob = :birthDate ");
		}
		if (patientSearchDto.getIdentificationNo() != null && !patientSearchDto.getIdentificationNo().isEmpty()) {
			query.append(" AND lower(pr.identification_number) LIKE concat('%', LOWER(:identificationNo), '%') ");
		}
		if (patientSearchDto.getPatientId() != null && patientSearchDto.getPatientId() > 0) {
			query.append(" AND pr.patient_id = :patientId ");
		}
		if (patientSearchDto.getWardList() != null && !patientSearchDto.getWardList().isEmpty()) {
			query.append(" AND adtl.ward_id IN (:wardIds) ");
		}
		if (patientSearchDto.getDoctorId() != null) {
			query.append(" AND adm.doctor_id = :doctorId ");
		}

		return query;
	}

	@Override
	public Response patientSearchByMultipleCriteriaNabh(PatientSearchDto patientSearchDto) {
		try {
			Session session = sessionFactory.getCurrentSession();
			StringBuilder resQuery = this.searchPatientNabh(patientSearchDto);
			SQLQuery actualQuery = session.createSQLQuery(resQuery.toString());
			if(patientSearchDto.getOffset()!=null && patientSearchDto.getNoOfRecordsPerPage()!=null)
			{
				actualQuery.setFirstResult(patientSearchDto.getOffset());
				actualQuery.setMaxResults(patientSearchDto.getNoOfRecordsPerPage());
			}

			// Set Parameters
			if (patientSearchDto.getUhIdNumber() != null && !patientSearchDto.getUhIdNumber().isEmpty()) {
				actualQuery.setParameter("uhidNumber", patientSearchDto.getUhIdNumber());
			}
			if (patientSearchDto.getPatientName() != null && !patientSearchDto.getPatientName().isEmpty()) {
				actualQuery.setParameter("patientName", patientSearchDto.getPatientName());
			}
			if (patientSearchDto.getGenderId() != null) {
				actualQuery.setParameter("genderId", patientSearchDto.getGenderId());
			}
			if (patientSearchDto.getMobileNo() != null && !patientSearchDto.getMobileNo().isEmpty()) {
				actualQuery.setParameter("mobileNo", patientSearchDto.getMobileNo());
			}
			if (patientSearchDto.getBirthDate() != null && !patientSearchDto.getBirthDate().isEmpty()) {
				actualQuery.setParameter("birthDate", patientSearchDto.getBirthDate());
			}
			if (patientSearchDto.getIdentificationTypeId() != null && patientSearchDto.getIdentificationTypeId() > 0) {
				actualQuery.setParameter("identificationTypeId", patientSearchDto.getIdentificationTypeId());
			}
			if (patientSearchDto.getIdentificationNo() != null && !patientSearchDto.getIdentificationNo().isEmpty()) {
				actualQuery.setParameter("identificationNo", patientSearchDto.getIdentificationNo());
			}
			if (patientSearchDto.getPatientId() != null && patientSearchDto.getPatientId() > 0) {
				actualQuery.setParameter("patientId", patientSearchDto.getPatientId());
			}
			if (patientSearchDto.getWardId() != null) {
				actualQuery.setParameter("wardId", patientSearchDto.getWardId());
			}
			if (patientSearchDto.getDoctorId() != null) {
				actualQuery.setParameter("doctorId", patientSearchDto.getDoctorId());
			}
			List<PatientSearchDto> bedMasterDtosList = actualQuery
					.setParameter("orgId", patientSearchDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(PatientSearchDto.class)).list();
			return new Response(SUCCESS, null, null, bedMasterDtosList, null);

		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			throw e;
		}
	}
	private StringBuilder searchPatientNabh(PatientSearchDto patientSearchDto) 
	{
		Date date = TimeZoneComponent.getDateByzone(patientSearchDto.getUnitId());  
		String currentDate=GlobalCommonDateUtils.getStringDate(date, "yyyy-MM-dd");
	    //System.out.println(currentDate); 

		StringBuilder query=new StringBuilder();
		
		String strQuery="select " 
			 + " 	distinct (pr.patient_id) as \"patientId\", " 
			 + " 	pr.image_path as \"patientProfile\", " 
			 + " 	pr.uhid_number as \"uhIdNumber\", " 
			 + " 	concat(coalesce(prifix.prefix_code, ''), ' ', coalesce(pr.first_name, '') , ' ', coalesce(pr.middle_name, '') , ' ', coalesce(pr.last_name, '')) as \"toPatientName\", "
			 + " 	concat(coalesce(prifix.prefix_code, ''), ' ', coalesce(pr.first_name, '') , ' ', coalesce(pr.middle_name, '') , ' ', coalesce(pr.last_name, '')) as \"patientName\", " 
			 + " 	pr.gender_id as \"genderId\", " 
			 + " 	pr.file_sequence_number as \"fileSequenceNumber\", " 
			 + " 	gm.gender_code as \"genderCode\", " 
			 + " 	gm.gender_name as \"genderName\", " 
			 + " 	pr.mobile_number as \"mobileNo\", " 
			 + " 	pr.first_name as \"firstName\", " 
			 + " 	pr.middle_name as \"middleName\", " 
			 + " 	pr.last_name as \"lastName\", "
			 + "    pr.video_call_url as \"videoCallUrl\", " 
			 + " 	pr.email as \"email\", " 
			 + " 	pr.is_absconded as \"isAbsconded\", " 
			 + " 	id.identification_name as \"identificationName\", " 
			 + " 	coalesce(pr.identification_number, '') as \"identificationNo\", "
			 + "	(case when id.is_expiry_required='Y' then "
			 + "		to_char(pr.identification_expiry_date, 'YYYY-MM-DD') end) as \"identificationExpiryDate\", " 
			 + " 	to_char(pr.dob, 'DD/MM/YYYY') as \"birthDate\", " 
			 + " 	pr.dob as \"birthDateNew\", " 
			 + " 	concat(extract(year from age(current_timestamp, pr.dob)) , 'Y') as \"age\", " 
			 + " 	1 as \"patientType\", " 
			 + " 	pr.prefix_id as \"prefixId\", " 
			 + " 	pr.patient_category_id as \"patientCategoryId\", " 
			 + " 	pr.registration_type_id as \"registrationTypeId\", " 
			 + " 	pr.country_calling_code as \"countryCallingCode\", " 
			 + " 	pr.blood_group as \"bloodGroup\", " 
			 + " 	to_char(pr.created_date, 'DD/MM/YYYY') as \"createdDate\", "
			 + " 	wdmst.ward_name as \"wardName\", " 
			 + " 	bdmst.bed_number as \"bedNumber\", " 
			 + " 	to_char(admdtl.doa, 'DD/MM/YYYY') as \"doa\", " 
			 + " 	to_char(admdtl.pdd, 'DD/MM/YYYY') as \"pdd\", " 
			 + " 	(case when admmst.admission_id > 0 and admmst.visit_type_id !=4 then "
			 + " 		concat('Dr. ',coalesce(docmst.first_name, '') , ' ', coalesce(docmst.middle_name, '') , ' ', coalesce(docmst.last_name, '')) end) as \"doctorName\", "
			 + "    docmst.doctor_id as \"doctorId\"," 
			 + "  en.encounter_Id  as \"encounterId\" , " 
			 + " 	admmst.admission_id as \"admissionId\", " 
			 + " 	admmst.unit_id as \"admissionUnitId\", " 
			 + " 	admmst.is_final_bill as \"isFinalBill\", " 
			 + " 	patDtls.country_id as \"countryId\", " 
			 + " 	patDtls.district_id as \"districtId\", " 
			 + " 	patDtls.address as \"address\", " 
			 + " (case " 
			 + "  when dishh.is_death  is not null then dishh.is_death " + 
			 " 		else 'N'" + 
			 " 	end) as \"isDeath\", "
			 + " 	admmst.visit_type_id as \"visitTypeId\", "
			 + " concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, pr.dob)) ,'Y ', EXTRACT(month from AGE(CURRENT_TIMESTAMP, pr.dob)) , 'M ' , EXTRACT(day from AGE(CURRENT_TIMESTAMP, pr.dob)),'D ') as \"ageStr\" "
			 + " from " 
			 + " 	patient.t_patient_registration as pr " 
			 + " inner join patient.t_patient_details patDtls on " 
			 + " 	pr.patient_id = patDtls.patient_id and patDtls.status='A' " 
			 + " inner join public.m_gender_master as gm on " 
			 + " 	gm.gender_id = pr.gender_id " 
			 + " left join public.m_prefix_master as prifix on " 
			 + " 	prifix.prefix_id = pr.prefix_id " 
			 + " left join adt.m_identification_master as id on " 
			 + " 	id.identification_id = pr.identification_type_id " 
			 + " left join adt.t_admission as admmst on " 
			 + " 	admmst.patient_id = pr.patient_id " 
			 + " 	and admmst.status = 'A' " 
			 + " left join adt.t_admission_details as admdtl on " 
			 + " 	admmst.admission_id = admdtl.admission_id " 
			 + " 	and admdtl.status = 'A' " 
			 + " left join adt.m_ward_master as wdmst on " 
			 + " 	wdmst.ward_id = admdtl.ward_id " 
			 + " left join adt.m_bed_master as bdmst on " 
			 + " 	bdmst.bed_id = admdtl.bed_id " 
			 + " left join doctor.m_doctor_master as docmst on " 
			 + " 	docmst.doctor_id = admmst.doctor_id " 
			 + " left join adt.t_discharge as dishh on " 
			 + " 	dishh.admission_id = admmst.admission_id "
			 + " left  join ( " + 
			 "	select " + 
			 "		max(enc.encounter_id) encounter_id, " + 
			 "		enc.patient_id " + 
			 "	from " + 
			 "		opd.t_encounter_master enc " + 
			 "	inner join opd.m_appointment_master app on " + 
			 "		enc.appointment_id = app.appointment_id " + 
			 "	where " + 
			 "		enc.status_id != 6 		 " + 
			 "		and to_char(app.appointment_date, 'yyyy-MM-dd')= '"+currentDate+"'  " + 
			 "		group by enc.patient_id " + 
			 "	 ) en  on en.patient_id = pr.patient_id " 
			 + " where " 
			 + " 	pr.organization_id =:orgId "
			 + " 	and pr.is_primary = 'Y' " ;

		query.append(strQuery);

// UHID Number
		if (patientSearchDto.getUhIdNumber() != null && !patientSearchDto.getUhIdNumber().isEmpty() && patientSearchDto.getFromMrdScreen() == null) {
			query.append(" AND lower(pr.uhid_number) LIKE concat('%', LOWER(:uhidNumber), '%') ");
		}

// Patient Name
		if (patientSearchDto.getPatientName() != null && !patientSearchDto.getPatientName().isEmpty()) {
			if (patientSearchDto.getFromMrdScreen() != null && patientSearchDto.getFromMrdScreen()) {
				if (patientSearchDto.getUhIdNumber() != null && !patientSearchDto.getUhIdNumber().isEmpty()) {
					query.append(" AND ( ((lower(coalesce(pr.first_name, '')) LIKE concat(lower(:patientName), '%') " +
							" OR lower(coalesce(pr.last_name, '')) LIKE concat(lower(:patientName), '%')) " +
							" OR lower(concat(coalesce(pr.first_name, ''), ' ', coalesce(pr.last_name, ''))) LIKE concat('%', lower(:patientName), '%')) " +
							" OR (lower(pr.uhid_number) LIKE concat('%', LOWER(:uhidNumber), '%')) )");
				}
			} else {
				query.append(" AND ((lower(coalesce(pr.first_name, '')) LIKE concat(lower(:patientName), '%') " +
						" OR lower(coalesce(pr.last_name, '')) LIKE concat(lower(:patientName), '%')) " +
						" OR lower(concat(coalesce(pr.first_name, ''), ' ', coalesce(pr.last_name, ''))) LIKE concat('%', lower(:patientName), '%')) ");
			}
		}

// Gender ID
		if (patientSearchDto.getGenderId() != null) {
			query.append(" AND pr.gender_id = :genderId ");
		}

// Mobile Number
		if (patientSearchDto.getMobileNo() != null && !patientSearchDto.getMobileNo().isEmpty()) {
			query.append(" AND pr.mobile_number LIKE concat(:mobileNo, '%') ");
		}

// Birth Date
		if (patientSearchDto.getBirthDate() != null && !patientSearchDto.getBirthDate().isEmpty()) {
			query.append(" AND pr.dob = :birthDate ");
		}

// Identification Type ID
		if (patientSearchDto.getIdentificationTypeId() != null && patientSearchDto.getIdentificationTypeId() > 0) {
			query.append(" AND pr.identification_type_id = :identificationTypeId ");
		}

// Identification Number
		if (patientSearchDto.getIdentificationNo() != null && !patientSearchDto.getIdentificationNo().isEmpty()) {
			query.append(" AND lower(coalesce(pr.identification_number, '')) LIKE concat('%', LOWER(:identificationNo), '%') ");
		}

// Patient ID
		if (patientSearchDto.getPatientId() != null && patientSearchDto.getPatientId() > 0) {
			query.append(" AND pr.patient_id = :patientId ");
		}

// Status
		if (patientSearchDto.getFromMrdScreen() == null) {
			query.append(" AND pr.status = 'A' ");
		}

// Ward ID
		if (patientSearchDto.getWardId() != null) {
			query.append(" AND wdmst.ward_id = :wardId ");
		}

// Doctor ID
		if (patientSearchDto.getDoctorId() != null) {
			query.append(" AND docmst.doctor_id = :doctorId ");
		}


		return query;
	}
	
	@Override
	public Response patientSearchForIPDNutritionalAssessment(PatientSearchDto patientSearchDto) throws ApplicationException {
		try {
			Session session = sessionFactory.getCurrentSession();
			StringBuilder resQuery = this.searchPatientForIPDNutritionalAssessment(patientSearchDto);
			SQLQuery actualQuery = session.createSQLQuery(resQuery.toString());
			List<PatientSearchDto> bedMasterDtosList = null;
			if(((patientSearchDto.getWardList() == null || patientSearchDto.getWardList().isEmpty()) && (patientSearchDto.getBedList()== null || patientSearchDto.getBedList().isEmpty())))
			{
				 	bedMasterDtosList = actualQuery
					.setParameter("orgId", patientSearchDto.getOrganizationId())
					//.setParameter("visitTypeId", patientSearchDto.getVisitTypeId())
					.setParameter("unitId", patientSearchDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(PatientSearchDto.class)).list();
			}
			if((patientSearchDto.getBedList()!=null && (patientSearchDto.getWardList() == null || patientSearchDto.getWardList().isEmpty())) &&  patientSearchDto.getBedList().size() > 0)
			{
				 	bedMasterDtosList = actualQuery
					.setParameter("orgId", patientSearchDto.getOrganizationId())
					//.setParameter("visitTypeId", patientSearchDto.getVisitTypeId())
					.setParameter("unitId", patientSearchDto.getUnitId())
					.setParameterList("bedList", patientSearchDto.getBedList())
					.setResultTransformer(Transformers.aliasToBean(PatientSearchDto.class)).list();
			}
			if((patientSearchDto.getWardList()!=null && (patientSearchDto.getBedList()== null || patientSearchDto.getBedList().isEmpty())) &&  patientSearchDto.getWardList().size() > 0)
			{
				 	bedMasterDtosList = actualQuery
					.setParameter("orgId", patientSearchDto.getOrganizationId())
					//.setParameter("visitTypeId", patientSearchDto.getVisitTypeId())
					.setParameter("unitId", patientSearchDto.getUnitId())
					.setParameterList("wardList", patientSearchDto.getWardList())
					.setResultTransformer(Transformers.aliasToBean(PatientSearchDto.class)).list();
			}
			
			return new Response(SUCCESS, null, null, bedMasterDtosList, null);

		} catch (Exception e) {
		logger.error("patientSearchForIPDNutritionalAssessment failed", e);
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	private StringBuilder searchPatientForIPDNutritionalAssessment(PatientSearchDto patientSearchDto) 
	{
		StringBuilder query=new StringBuilder();

		String strQuery=" select " 
				 + " 	distinct (pr.patient_id) as \"patientId\", " 
				 + " 	pr.image_path as \"patientProfile\", " 
				 + " 	pr.uhid_number as \"uhIdNumber\", " 
				 + " 	concat(coalesce(prifix.prefix_code, ''), ' ', coalesce(pr.first_name, '') , ' ', coalesce(pr.middle_name, '') , ' ', coalesce(pr.last_name, '')) as \"patientName\", " 
				 + " 	pr.gender_id as \"genderId\", " 
				 + " 	gm.gender_code as \"genderCode\", " 
				 + " 	gm.gender_name as \"genderName\", " 
				 + " 	pr.mobile_number as \"mobileNo\", " 
				 + " 	pr.first_name as \"firstName\", " 
				 + " 	pr.middle_name as \"middleName\", " 
				 + " 	pr.last_name as \"lastName\", " 
				 + " 	pr.email as \"email\", " 
				 + " 	pr.is_absconded as \"isAbsconded\", " 
				 + " 	id.identification_name as \"identificationName\", " 
				 + " 	coalesce(pr.identification_number, '') as \"identificationNo\", " 
				 + " 	to_char(pr.dob, 'DD/MM/YYYY') as \"birthDate\", " 
				 + " 	concat(extract(year from age(current_timestamp, pr.dob)) , 'Y ' , extract(month from age(current_timestamp, pr.dob)), 'M ') as \"age\", " 
				 + " 	1 as \"patientType\", " 
				 + " 	pr.prefix_id as \"prefixId\", "
				 + "    to_char(adtl.doa, 'DD/MM/YYYY') as \"doa\", "
				 + " 	pr.patient_category_id as \"patientCategoryId\", "
				 + " 	pr.registration_type_id as \"registrationTypeId\", "
                 + "     padtl.address as \"address\","
                 + "    cmm.city_name as \"cityName\", " 
				 + " 	adm.admission_id as \"admissionId\", " 
				 + " 	adm.visit_type_id as \"visitTypeId\", " 
				 + " 	cast(loyaltycard.card_no as text) as \"loyaltycardNo\", " 
				 + " 	coalesce(mppr.patient_loyalty_card_mppr_id, 0) as \"patientLoyaltyCardMpprId\" " 
				 + " from " 
				 + " 	patient.t_patient_registration as pr " 
				 + " left join public.m_prefix_master as prifix on  " 
				 + "  	prifix.prefix_id = pr.prefix_id 	 " 
				 + " left join public.m_loyalty_card loyaltycard on " 
				 + " 	pr.patient_id = loyaltycard.patient_id " 
				 + " 	and loyaltycard.is_deleted = 'N' " 
				 + " left join public.t_patient_loyalty_card_mppr mppr on " 
				 + " 	mppr.patient_id = loyaltycard.patient_id " 
				 + " 	and mppr.is_deleted = 'N' " 
				 + " inner join public.m_gender_master as gm on " 
				 + " 	gm.gender_id = pr.gender_id " 
				 + " left join adt.m_identification_master as id on " 
				 + " 	id.identification_id = pr.identification_type_id " 
				 + " inner join adt.t_admission as adm on " 
				 + " 	adm.patient_id = pr.patient_id " 
				 + " inner join adt.t_admission_details as adtl on " 
				 + " 	adtl.admission_id = adm.admission_id " 
                 + " left join patient.t_patient_details as padtl on " 
                 + "  padtl.patient_id = pr.patient_id " 
                 + " left join public.m_city_master as cmm on " 
                 + "  cmm.city_id = padtl.city_id " 
				 + " where " 
				 + " 	pr.status = 'A' " 
				 + " 	and pr.organization_id =:orgId " 
				 + " 	and adm.unit_id =:unitId " 
				 + " 	and pr.is_primary = 'Y' " 
				 + " 	and adm.status = 'A' " 
				 + " 	and adtl.status = 'A' " 
				 + " 	and adtl.nursing_acceptance = 'Y' " 
				 + " 	and adtl.bed_id is not null " ;
		
		query.append(strQuery);
		
		if(patientSearchDto.getIdentificationTypeId() != null && patientSearchDto.getIdentificationTypeId()>0)
			query.append(" AND pr.identification_type_id="+patientSearchDto.getIdentificationTypeId());
		if(patientSearchDto.getUhIdNumber() != null && !patientSearchDto.getUhIdNumber().isEmpty())
			query.append(" AND lower(pr.uhid_number) LIKE concat('%',LOWER('"+patientSearchDto.getUhIdNumber()+"'),'%') ");
		if(patientSearchDto.getPatientName() != null && !patientSearchDto.getPatientName().isEmpty())
			query.append(" AND( lower(concat(coalesce(pr.first_name,''),coalesce(pr.last_name,''))) LIKE concat(lower('"+patientSearchDto.getPatientName()+"'),'%') "
					+ " OR lower(concat(coalesce(pr.first_name),' ',coalesce(pr.last_name,''))) LIKE concat('%' , lower('"+patientSearchDto.getPatientName()+"'),'%') )");
		if(patientSearchDto.getGenderId() != null)
			query.append(" AND pr.gender_id="+patientSearchDto.getGenderId()+" ");
		if(patientSearchDto.getMobileNo() != null && !patientSearchDto.getMobileNo().isEmpty())
			query.append(" AND pr.mobile_number='"+patientSearchDto.getMobileNo()+"' ");
		if(patientSearchDto.getBirthDate() != null && !patientSearchDto.getBirthDate().isEmpty())
			query.append(" AND pr.dob='"+patientSearchDto.getBirthDate()+"' ");
		if(patientSearchDto.getIdentificationNo() != null && !patientSearchDto.getIdentificationNo().isEmpty())
			query.append(" AND pr.identification_number LIKE concat('%',LOWER('"+patientSearchDto.getIdentificationNo()+"'),'%')");
		if(patientSearchDto.getPatientId() != null && patientSearchDto.getPatientId()>0)
			query.append(" AND pr.patient_id="+patientSearchDto.getPatientId());
		if(patientSearchDto.getWardList()!=null && patientSearchDto.getWardList().size()>0)
			query.append(" AND adtl.ward_id IN :wardList ");
		if(patientSearchDto.getBedList()!=null && patientSearchDto.getBedList().size()>0)
			query.append(" AND adtl.bed_id IN :bedList ");
		if(patientSearchDto.getVisitTypeId()!=null && patientSearchDto.getVisitTypeId()>0)
			query.append(" AND adm.visit_type_id="+patientSearchDto.getVisitTypeId()+" ");
		    query.append(" AND adm.visit_type_id = 2 ");
		 
		return query;
	}
	

	@Override
	public Response patientSearchByICUAndER(PatientSearchDto patientSearchDto) throws ApplicationException {
		try {
			Session session = sessionFactory.getCurrentSession();
			StringBuilder resQuery = this.searchPatientICUAndER(patientSearchDto);
			SQLQuery actualQuery = session.createSQLQuery(resQuery.toString());
			List<PatientSearchDto> bedMasterDtosList = null;
			if(((patientSearchDto.getWardList() == null || patientSearchDto.getWardList().isEmpty()) && (patientSearchDto.getBedList()== null || patientSearchDto.getBedList().isEmpty())))
			{
				 	bedMasterDtosList = actualQuery
					.setParameter("orgId", patientSearchDto.getOrganizationId())
					//.setParameter("visitTypeId", patientSearchDto.getVisitTypeId())
					.setParameter("unitId", patientSearchDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(PatientSearchDto.class)).list();
			}
			if((patientSearchDto.getBedList()!=null && (patientSearchDto.getWardList() == null || patientSearchDto.getWardList().isEmpty())) &&  patientSearchDto.getBedList().size() > 0)
			{
				 	bedMasterDtosList = actualQuery
					.setParameter("orgId", patientSearchDto.getOrganizationId())
					//.setParameter("visitTypeId", patientSearchDto.getVisitTypeId())
					.setParameter("unitId", patientSearchDto.getUnitId())
					.setParameterList("bedList", patientSearchDto.getBedList())
					.setResultTransformer(Transformers.aliasToBean(PatientSearchDto.class)).list();
			}
			if((patientSearchDto.getWardList()!=null && (patientSearchDto.getBedList()== null || patientSearchDto.getBedList().isEmpty())) &&  patientSearchDto.getWardList().size() > 0)
			{
				 	bedMasterDtosList = actualQuery
					.setParameter("orgId", patientSearchDto.getOrganizationId())
					//.setParameter("visitTypeId", patientSearchDto.getVisitTypeId())
					.setParameter("unitId", patientSearchDto.getUnitId())
				//	.setParameterList("wardList", patientSearchDto.getWardList())
					.setResultTransformer(Transformers.aliasToBean(PatientSearchDto.class)).list();
			}
			
			return new Response(SUCCESS, null, null, bedMasterDtosList, null);

		} catch (Exception e) {
		logger.error("patientSearchByICUAndER failed", e);
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
		}
	}

	@Override
	public Response patientSearchByMultipleCriteriaGrowthChart(PatientSearchDto patientSearchDto) throws ApplicationException {
		try {
			Session session = sessionFactory.getCurrentSession();
			StringBuilder resQuery = this.searchPatientGrowthChart(patientSearchDto);
			SQLQuery actualQuery = session.createSQLQuery(resQuery.toString());
			if(patientSearchDto.getOffset()!=null && patientSearchDto.getNoOfRecordsPerPage()!=null)
			{
				actualQuery.setFirstResult(patientSearchDto.getOffset());
				actualQuery.setMaxResults(patientSearchDto.getNoOfRecordsPerPage());
			}


			// Set parameters for the dynamic parts of the query
			if (patientSearchDto.getUhIdNumber() != null && !patientSearchDto.getUhIdNumber().isEmpty()) {
				actualQuery.setParameter("uhidNumber", "%" + patientSearchDto.getUhIdNumber().toLowerCase() + "%");
			}

			if (patientSearchDto.getPatientName() != null && !patientSearchDto.getPatientName().isEmpty()) {
				actualQuery.setParameter("patientName", patientSearchDto.getPatientName().toLowerCase());
			}

			if (patientSearchDto.getGenderId() != null) {
				actualQuery.setParameter("genderId", patientSearchDto.getGenderId());
			}

			if (patientSearchDto.getMobileNo() != null && !patientSearchDto.getMobileNo().isEmpty()) {
				actualQuery.setParameter("mobileNo", patientSearchDto.getMobileNo() + "%");
			}

			if (patientSearchDto.getBirthDate() != null && !patientSearchDto.getBirthDate().isEmpty()) {
				actualQuery.setParameter("birthDate", patientSearchDto.getBirthDate());
			}

			if (patientSearchDto.getIdentificationTypeId() != null && patientSearchDto.getIdentificationTypeId() > 0) {
				actualQuery.setParameter("identificationTypeId", patientSearchDto.getIdentificationTypeId());
			}

			if (patientSearchDto.getIdentificationNo() != null && !patientSearchDto.getIdentificationNo().isEmpty()) {
				actualQuery.setParameter("identificationNo", patientSearchDto.getIdentificationNo().toLowerCase());
			}

			if (patientSearchDto.getPatientId() != null && patientSearchDto.getPatientId() > 0) {
				actualQuery.setParameter("patientId", patientSearchDto.getPatientId());
			}

			if (patientSearchDto.getWardId() != null) {
				actualQuery.setParameter("wardId", patientSearchDto.getWardId());
			}

			if (patientSearchDto.getDoctorId() != null) {
				actualQuery.setParameter("doctorId", patientSearchDto.getDoctorId());
			}

			List<PatientSearchDto> bedMasterDtosList = actualQuery
					.setParameter("orgId", patientSearchDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(PatientSearchDto.class)).list();

			

			if(bedMasterDtosList != null ){

				List<Integer> patientIds = bedMasterDtosList.stream()
						.map(PatientSearchDto::getPatientId) // Assuming getPatientId() returns an Integer
						.collect(Collectors.toList());

				String query =" select distinct ta.patient_id as \"patientId\"  from adt.t_discharge td " +
						" inner join adt.t_admission ta  on td.admission_id  = ta.admission_id  " +
						" where ta.patient_id in (:id) and  td.is_death  = 'Y'";

				Query actualQuerys = session.createSQLQuery(query);
				List<PatientSearchDto> bedMasterDtosLists = actualQuerys
						.setParameterList("id", patientIds)
						.setResultTransformer(Transformers.aliasToBean(PatientSearchDto.class)).list();

				Set<Integer> deathPatientIds = bedMasterDtosLists.stream()
						.map(PatientSearchDto::getPatientId)
						.collect(Collectors.toSet());

				for (PatientSearchDto dto : bedMasterDtosList) {
					if (deathPatientIds.contains(dto.getPatientId())) {
						dto.setIsDeath('Y');
					} else {
						dto.setIsDeath('N');
					}
				}
			}
			return new Response(SUCCESS, null, null, bedMasterDtosList, null);

		} catch (HibernateException he) {
		String correlationId = ErrorCorrelationUtil.newCorrelationId();
		logger.error(ErrorCorrelationUtil.tag(correlationId, "patientSearchByMultipleCriteriaGrowthChart failed"), he);
		return new Response(ERROR, null, ErrorCorrelationUtil.clientMessage(correlationId), null, null);
		} catch (Exception e) {
		String correlationId = ErrorCorrelationUtil.newCorrelationId();
		logger.error(ErrorCorrelationUtil.tag(correlationId, "patientSearchByMultipleCriteriaGrowthChart failed"), e);
		return new Response(ERROR, null, ErrorCorrelationUtil.clientMessage(correlationId), null, null);
		}
	}

	private StringBuilder searchPatientGrowthChart(PatientSearchDto patientSearchDto) {

		try{
			Date date = TimeZoneComponent.getDateByzone(patientSearchDto.getUnitId());
			String currentDate=GlobalCommonDateUtils.getStringDate(date, "yyyy-MM-dd");
			//System.out.println(currentDate);

			StringBuilder query=new StringBuilder();

			String strQuery="select "
					+ " 	distinct (pr.patient_id) as \"patientId\", "
					+ " 	pr.image_path as \"patientProfile\", "
					+ " 	pr.uhid_number as \"uhIdNumber\", "
					+ " 	concat(coalesce(prifix.prefix_code, ''), ' ', coalesce(pr.first_name, '') , ' ', coalesce(pr.middle_name, '') , ' ', coalesce(pr.last_name, '')) as \"toPatientName\", "
					+ " 	concat(coalesce(prifix.prefix_code, ''), ' ', coalesce(pr.first_name, '') , ' ', coalesce(pr.middle_name, '') , ' ', coalesce(pr.last_name, '')) as \"patientName\", "
					+ " 	pr.gender_id as \"genderId\", "
					+ " 	pr.file_sequence_number as \"fileSequenceNumber\", "
					+ " 	gm.gender_code as \"genderCode\", "
					+ " 	gm.gender_name as \"genderName\", "
					+ " 	pr.mobile_number as \"mobileNo\", "
					+ " 	pr.first_name as \"firstName\", "
					+ " 	pr.middle_name as \"middleName\", "
					+ " 	pr.last_name as \"lastName\", "
					+ "    pr.video_call_url as \"videoCallUrl\", "
					+ " 	pr.email as \"email\", "
					+ " 	pr.is_absconded as \"isAbsconded\", "
					+ " 	id.identification_name as \"identificationName\", "
					+ " 	coalesce(pr.identification_number, '') as \"identificationNo\", "
					+ "	(case when id.is_expiry_required='Y' then "
					+ "		to_char(pr.identification_expiry_date, 'YYYY-MM-DD') end) as \"identificationExpiryDate\", "
					+ " 	to_char(pr.dob, 'DD/MM/YYYY') as \"birthDate\", "
					+ " 	pr.dob as \"birthDateNew\", "
					+ " 	concat(extract(year from age(current_timestamp, pr.dob)) , 'Y') as \"age\", "
					+ " 	1 as \"patientType\", "
					+ " 	pr.prefix_id as \"prefixId\", "
					+ " 	pr.patient_category_id as \"patientCategoryId\", "
					+ " 	pr.registration_type_id as \"registrationTypeId\", "
					+ " 	pr.country_calling_code as \"countryCallingCode\", "
					+ " 	pr.blood_group as \"bloodGroup\", "
					+ " 	to_char(pr.created_date, 'DD/MM/YYYY') as \"createdDate\", "
					+ " 	wdmst.ward_name as \"wardName\", "
					+ " 	bdmst.bed_number as \"bedNumber\", "
					+ " 	to_char(admdtl.doa, 'DD/MM/YYYY') as \"doa\", "
					+ " 	to_char(admdtl.pdd, 'DD/MM/YYYY') as \"pdd\", "
					+ " 	(case when admmst.admission_id > 0 and admmst.visit_type_id !=4 then "
					+ " 		concat('Dr. ',coalesce(docmst.first_name, '') , ' ', coalesce(docmst.middle_name, '') , ' ', coalesce(docmst.last_name, '')) end) as \"doctorName\", "
					+ "    docmst.doctor_id as \"doctorId\","
					+ "  en.encounter_Id  as \"encounterId\" , "
					+ " 	admmst.admission_id as \"admissionId\", "
					+ " 	admmst.unit_id as \"admissionUnitId\", "
					+ " 	admmst.is_final_bill as \"isFinalBill\", "
					+ " 	patDtls.country_id as \"countryId\", "
					+ " 	patDtls.district_id as \"districtId\", "
					+ " 	patDtls.address as \"address\", "
					+ " (case "
					+ "  when dishh.is_death  is not null then dishh.is_death " +
					" 		else 'N'" +
					" 	end) as \"isDeath\", "
					+ " 	admmst.visit_type_id as \"visitTypeId\", "
					+ " concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, pr.dob)) ,'Y ', EXTRACT(month from AGE(CURRENT_TIMESTAMP, pr.dob)) , 'M ' , EXTRACT(day from AGE(CURRENT_TIMESTAMP, pr.dob)),'D ') as \"ageStr\" "
					+ " from "
					+ " 	patient.t_patient_registration as pr "
					+ " inner join patient.t_patient_details patDtls on "
					+ " 	pr.patient_id = patDtls.patient_id and patDtls.status='A' "
					+ " inner join public.m_gender_master as gm on "
					+ " 	gm.gender_id = pr.gender_id "
					+ " left join public.m_prefix_master as prifix on "
					+ " 	prifix.prefix_id = pr.prefix_id "
					+ " left join adt.m_identification_master as id on "
					+ " 	id.identification_id = pr.identification_type_id "
					+ " left join adt.t_admission as admmst on "
					+ " 	admmst.patient_id = pr.patient_id "
					+ " 	and admmst.status = 'A' "
					+ " left join adt.t_admission_details as admdtl on "
					+ " 	admmst.admission_id = admdtl.admission_id "
					+ " 	and admdtl.status = 'A' "
					+ " left join adt.m_ward_master as wdmst on "
					+ " 	wdmst.ward_id = admdtl.ward_id "
					+ " left join adt.m_bed_master as bdmst on "
					+ " 	bdmst.bed_id = admdtl.bed_id "
					+ " left join doctor.m_doctor_master as docmst on "
					+ " 	docmst.doctor_id = admmst.doctor_id "
					+ " left join adt.t_discharge as dishh on "
					+ " 	dishh.admission_id = admmst.admission_id "
					+ " left  join ( " +
					"	select " +
					"		max(enc.encounter_id) encounter_id, " +
					"		enc.patient_id " +
					"	from " +
					"		opd.t_encounter_master enc " +
					"	inner join opd.m_appointment_master app on " +
					"		enc.appointment_id = app.appointment_id " +
					"	where " +
					"		enc.status_id != 6 		 " +
					"		and to_char(app.appointment_date, 'yyyy-MM-dd')= '"+currentDate+"'  " +
					"		group by enc.patient_id " +
					"	 ) en  on en.patient_id = pr.patient_id "
					+ " where "
					+ " 	pr.organization_id =:orgId "
					+ " 	and pr.is_primary = 'Y' "
					+ " 	AND ((EXTRACT(YEAR FROM AGE(CURRENT_TIMESTAMP, pr.dob)) * 12) + EXTRACT(MONTH FROM AGE(CURRENT_TIMESTAMP, pr.dob))) <= 24 ";

			query = new StringBuilder(strQuery);

// Append conditions based on non-null fields in patientSearchDto
			if (patientSearchDto.getUhIdNumber() != null && !patientSearchDto.getUhIdNumber().isEmpty() && patientSearchDto.getFromMrdScreen() == null) {
				query.append(" AND lower(pr.uhid_number) LIKE :uhidNumber ");
			}

			if(patientSearchDto.getPatientName() != null && !patientSearchDto.getPatientName().isEmpty()) {

				if(patientSearchDto.getFromMrdScreen() != null && patientSearchDto.getFromMrdScreen() == true) {
					if(patientSearchDto.getUhIdNumber() != null && !patientSearchDto.getUhIdNumber().isEmpty()) {
						query.append(" AND ( ((lower(coalesce(pr.first_name, '')) like concat( lower(:patientName), '%')  OR lower(coalesce( pr.last_name, '' )) like concat( lower(:patientName ), '%') )"
								+ " OR lower(concat(coalesce(pr.first_name),' ',coalesce(pr.last_name,''))) LIKE concat('%' , lower(:patientName),'%') ) "
								+ " Or (lower(pr.uhid_number) LIKE concat('%',LOWER(:uhidNumber),'%'))  "
								+ ")");
					}
				}else {
					query.append(" AND ((lower(coalesce(pr.first_name, '')) like concat( lower(:patientName), '%')  OR lower(coalesce( pr.last_name, '' )) like concat( lower(:patientName ), '%') )"
							+ " OR lower(concat(coalesce(pr.first_name),' ',coalesce(pr.last_name,''))) LIKE concat('%' , lower(:patientName),'%') )");

				}
			}


			if (patientSearchDto.getGenderId() != null) {
				query.append(" AND pr.gender_id = :genderId ");
			}

			if (patientSearchDto.getMobileNo() != null && !patientSearchDto.getMobileNo().isEmpty()) {
				query.append(" AND pr.mobile_number LIKE :mobileNo ");
			}

			if (patientSearchDto.getBirthDate() != null && !patientSearchDto.getBirthDate().isEmpty()) {
				query.append(" AND pr.dob = :birthDate ");
			}

			if (patientSearchDto.getIdentificationTypeId() != null && patientSearchDto.getIdentificationTypeId() > 0) {
				query.append(" AND pr.identification_type_id = :identificationTypeId ");
			}

			if (patientSearchDto.getIdentificationNo() != null && !patientSearchDto.getIdentificationNo().isEmpty()) {
				query.append(" AND lower(coalesce(pr.identification_number, '')) LIKE concat('%', lower(:identificationNo), '%') ");
			}

			if (patientSearchDto.getPatientId() != null && patientSearchDto.getPatientId() > 0) {
				query.append(" AND pr.patient_id = :patientId ");
			}

			if (patientSearchDto.getFromMrdScreen() == null) {
				query.append(" AND pr.status = 'A' ");
			}

			if (patientSearchDto.getWardId() != null) {
				query.append(" AND wdmst.ward_id = :wardId ");
			}

			if (patientSearchDto.getDoctorId() != null) {
				query.append(" AND docmst.doctor_id = :doctorId ");
			}

			return query;

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private StringBuilder searchPatientICUAndER(PatientSearchDto patientSearchDto) 
	{
		StringBuilder query=new StringBuilder();

		String strQuery=" select " 
				 + " 	distinct (pr.patient_id) as \"patientId\", " 
				 + " 	pr.image_path as \"patientProfile\", " 
				 + " 	pr.uhid_number as \"uhIdNumber\", " 
				 + " 	concat(coalesce(prifix.prefix_code, ''), ' ', coalesce(pr.first_name, '') , ' ', coalesce(pr.middle_name, '') , ' ', coalesce(pr.last_name, '')) as \"patientName\", " 
				 + " 	pr.gender_id as \"genderId\", " 
				 + " 	gm.gender_code as \"genderCode\", " 
				 + " 	gm.gender_name as \"genderName\", " 
				 + " 	pr.mobile_number as \"mobileNo\", " 
				 + " 	pr.first_name as \"firstName\", " 
				 + " 	pr.middle_name as \"middleName\", " 
				 + " 	pr.last_name as \"lastName\", " 
				 + " 	pr.email as \"email\", " 
				 + " 	pr.is_absconded as \"isAbsconded\", " 
				 + " 	id.identification_name as \"identificationName\", " 
				 + " 	coalesce(pr.identification_number, '') as \"identificationNo\", " 
				 + " 	to_char(pr.dob, 'DD/MM/YYYY') as \"birthDate\", " 
				 + " 	concat(extract(year from age(current_timestamp, pr.dob)) , 'Y ' , extract(month from age(current_timestamp, pr.dob)), 'M ') as \"age\", " 
				 + " 	1 as \"patientType\", " 
				 + " 	pr.prefix_id as \"prefixId\", "
//				 + "    to_char(adtl.doa, 'DD/MM/YYYY') as \"doa\", "
				 + "    to_char(adtl.admission_date_time, 'DD/MM/YYYY HH24:MI') as \"doa\", "
				 + " 	pr.patient_category_id as \"patientCategoryId\", "
				 + " 	pr.registration_type_id as \"registrationTypeId\", "
                 + "     padtl.address as \"address\","
                 + "    cmm.city_name as \"cityName\", " 
				 + " 	adm.admission_id as \"admissionId\", " 
				 + " 	adm.visit_type_id as \"visitTypeId\", " 
				 + " 	wdmst.ward_name as \"wardName\", "
				 + " 	wdmst.ward_id as \"wardId\", " 
				 + " 	cast(loyaltycard.card_no as text) as \"loyaltycardNo\", " 
				 + " 	coalesce(mppr.patient_loyalty_card_mppr_id, 0) as \"patientLoyaltyCardMpprId\" " 
				 + " from " 
				 + " 	patient.t_patient_registration as pr " 
				 + " left join public.m_prefix_master as prifix on  " 
				 + "  	prifix.prefix_id = pr.prefix_id 	 " 
				 + " left join public.m_loyalty_card loyaltycard on " 
				 + " 	pr.patient_id = loyaltycard.patient_id " 
				 + " 	and loyaltycard.is_deleted = 'N' " 
				 + " left join public.t_patient_loyalty_card_mppr mppr on " 
				 + " 	mppr.patient_id = loyaltycard.patient_id " 
				 + " 	and mppr.is_deleted = 'N' " 
				 + " inner join public.m_gender_master as gm on " 
				 + " 	gm.gender_id = pr.gender_id " 
				 + " left join adt.m_identification_master as id on " 
				 + " 	id.identification_id = pr.identification_type_id " 
				 + " inner join adt.t_admission as adm on " 
				 + " 	adm.patient_id = pr.patient_id " 
				 + " inner join adt.t_admission_details as adtl on " 
				 + " 	adtl.admission_id = adm.admission_id " 
                 + " left join patient.t_patient_details as padtl on " 
                 + "  padtl.patient_id = pr.patient_id " 
                 + " left join public.m_city_master as cmm on " 
                 + "  cmm.city_id = padtl.city_id " 
                 + " left join adt.m_ward_master as wdmst on "
                 + "     wdmst.ward_id = adtl.ward_id "
                 + "  LEFT JOIN adt.t_nursing_station_ward_mapper nurWard on wdmst.ward_id = nurWard.ward_id "
				 + "  LEFT JOIN adt.m_nursing_station_master nur on nurWard.nursing_station_id = nur.nursing_station_id "
				 + " where " 
				 + " 	pr.status = 'A' " 
				 + " 	and pr.organization_id =:orgId " 
				 + " 	and adm.unit_id =:unitId " 
				 + " 	and pr.is_primary = 'Y' " 
				 + " 	and adm.status = 'A' " 
				 + " 	and adtl.status = 'A' " 
				 + " 	and adtl.nursing_acceptance = 'Y' " 
                 + "    and( wdmst.is_er= 'Y' or wdmst.is_icu= 'Y' ) "
				 + " 	and adtl.bed_id is not null " ;
		
		query.append(strQuery);
		
		if(patientSearchDto.getIdentificationTypeId() != null && patientSearchDto.getIdentificationTypeId()>0)
			query.append(" AND pr.identification_type_id="+patientSearchDto.getIdentificationTypeId());
		if(patientSearchDto.getUhIdNumber() != null && !patientSearchDto.getUhIdNumber().isEmpty())
			query.append(" AND lower(pr.uhid_number) LIKE concat('%',LOWER('"+patientSearchDto.getUhIdNumber()+"'),'%') ");
		if(patientSearchDto.getPatientName() != null && !patientSearchDto.getPatientName().isEmpty())
			query.append(" AND( lower(concat(coalesce(pr.first_name,''),coalesce(pr.last_name,''))) LIKE concat(lower('"+patientSearchDto.getPatientName()+"'),'%') "
					+ " OR lower(concat(coalesce(pr.first_name),' ',coalesce(pr.last_name,''))) LIKE concat('%' , lower('"+patientSearchDto.getPatientName()+"'),'%') )");
		if(patientSearchDto.getGenderId() != null)
			query.append(" AND pr.gender_id="+patientSearchDto.getGenderId()+" ");
		if(patientSearchDto.getMobileNo() != null && !patientSearchDto.getMobileNo().isEmpty())
			query.append(" AND pr.mobile_number='"+patientSearchDto.getMobileNo()+"' ");
		if(patientSearchDto.getBirthDate() != null && !patientSearchDto.getBirthDate().isEmpty())
			query.append(" AND pr.dob='"+patientSearchDto.getBirthDate()+"' ");
		if(patientSearchDto.getIdentificationNo() != null && !patientSearchDto.getIdentificationNo().isEmpty())
			query.append(" AND pr.identification_number LIKE concat('%',LOWER('"+patientSearchDto.getIdentificationNo()+"'),'%')");
		if(patientSearchDto.getPatientId() != null && patientSearchDto.getPatientId()>0)
			query.append(" AND pr.patient_id="+patientSearchDto.getPatientId());
		
		if(patientSearchDto.getBedList()!=null && patientSearchDto.getBedList().size()>0)
			query.append(" AND adtl.bed_id IN :bedList ");
		
		if(patientSearchDto.getVisitTypeId() != OP) {
			
			if(patientSearchDto.getWardList()!=null && patientSearchDto.getWardList().size()>0) {
				
				query.append(" AND wdmst.ward_id in ("+patientSearchDto.getWardList().toString().toString().replace('[', '\n').replace(']', '\n')+")");
				
			}
//			else {
//				query.append(" AND wdmst.ward_id in (0)");
//			}
		}
		
		return query;
	}


    @Override
    public Response getAppointmentsByPatientId(PatientSearchDto patientSearchDto) {
        try {
            String sql = "Select patient.patient_id as \"patientId\", " +
                    " concat(coalesce(prefix.prefix_code, ''), ' ', coalesce(patient.first_name, '') , ' ', coalesce(patient.middle_name, '') , ' ', coalesce(patient.last_name, '')) as \"patientName\", " +
                    " encounter.encounter_id as \"encounterId\"," +
                    " to_char(encounter.encounter_date, 'DD/MM/YYYY') as \"encounterDate\", " +
                    " appointment.appointment_id as \"appointmentId\", " +
                    " to_char(slot.appointment_date, 'DD/MM/YYYY') as \"appointmentDate\", " +
                    " slot.slot_id as \"slotId\", " +
                    " concat(to_char(slot.from_time,'HH24:MI')) as \"slotFromTime\", " +
                    " concat(to_char(slot.to_time,'HH24:MI')) as \"slotToTime\" " +
                    " from patient.t_patient_registration patient " +
                    " left join public.m_prefix_master prefix on " +
                    "   prefix.prefix_id = patient.prefix_id " +
                    " inner join opd.t_encounter_master encounter on " +
                    "   encounter.patient_id = patient.patient_id " +
                    " inner join opd.m_appointment_master appointment on " +
                    "   appointment.appointment_id = encounter.appointment_id " +
                    " inner join opd.m_slot_master slot on " +
                    "   slot.slot_id = appointment.slot_id " +
                    " where " +
                    " slot.status = 'A' " +
                    " and encounter.status = 'A' " +
                    " and appointment.status  = 'A' " +
                    " and cast(encounter.encounter_date as date) >= CURRENT_DATE " +
                    " and cast(appointment.appointment_date as date) >= CURRENT_DATE " +
                    " and patient.patient_id = :patientId " +
                    " and encounter.unit_id  = :unitId " +
                    " order by slot.appointment_date ASC";

            SQLQuery query = getSession().createSQLQuery(sql);
            query.setParameter("patientId", patientSearchDto.getPatientId());
            query.setParameter("unitId", patientSearchDto.getUnitId());
            List<PatientSearchDto> appointmentsList = query
                    .setResultTransformer(Transformers.aliasToBean(PatientSearchDto.class)).list();

            return new Response(SUCCESS, null, null, appointmentsList, null);
        } catch (Exception e) {
        logger.error("getAppointmentsByPatientId failed", e);
        }
        return new Response(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
    }

	@Override
	public Response updateMLCFLag(AdmissionDto admissionDto) throws ApplicationException {
		try {
			String sql = " update adt.t_admission_details set is_medico_legal = 'Y' where admission_id= :admissionId";
			getSession().createSQLQuery(sql)
					.setParameter("admissionId", admissionDto.getAdmissionId())
					.executeUpdate();

			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		}catch (HibernateException he){
		logger.error("updateMLCFLag failed", he);
		}catch (Exception e){
		logger.error("updateMLCFLag failed", e);
		}
		return new Response(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);

	}

	@Override
	@Transactional
	public Integer getMLCcountForAdmission(AdmissionDto admissionDto) throws ApplicationException {
		try {
			String sql = " select count(*) from adt.t_medico_legal where admission_id= :admissionId ";

			BigInteger count = (BigInteger) getSession().createSQLQuery(sql)
					.setParameter("admissionId", admissionDto.getAdmissionId())
					.uniqueResult();

			return count.intValue();
		}catch (Exception e){
		throw new ApplicationException(e.getMessage(), e);
		}

	}

}
