package com.param.adt.admission.dao;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.adt.admission.model.AdmissionNote;
import com.param.adt.admission.model.AdmissionPatientMapper;
import com.param.adt.common.ADTCommonDateUtils;
import com.param.adt.common.IADTConstants;
import com.param.adt.common.ICommonConstants;
import com.param.adt.common.Response;
import com.param.adt.master.global.dto.VisitMasterDto;
import com.param.adt.master.global.model.BedCategoryWaitingList;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.dto.AdmissionNoteDto;
import com.param.global.dto.DoctorSpecialityMapperDto;
import com.param.global.dto.PatientRegistrationDto;
import com.param.global.dto.SpecialityMasterDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class AdmissionRequestDaoImpl extends GenericDao<AdmissionNote, Integer>
		implements IAdmissionRequestDao, ICommonConstants,IADTConstants {

	public AdmissionRequestDaoImpl() {
		super(AdmissionNote.class);
		// TODO Auto-generated constructor stub
	}

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private DozerBeanMapper mapper;

	@Override
	public Response getPatientListLike(PatientRegistrationDto patientRegistrationDto) {
		try {

				List<PatientRegistrationDto> patientRegistrationDtosList = sessionFactory.getCurrentSession()
						.getNamedQuery("GET_PATIENT_LIST_LIKE")
						.setParameter("firstName", patientRegistrationDto.getFirstName().toLowerCase() + "%")
						.setParameter("organizationId", patientRegistrationDto.getOrganizationId())
						//.setParameter("unitId", patientRegistrationDto.getUnitId())
						.setResultTransformer(Transformers.aliasToBean(PatientRegistrationDto.class)).list();
				return new Response(SUCCESS, null, null, patientRegistrationDtosList, null);
		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} 
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getPatientVisitDetailsById(VisitMasterDto visitMasterDto) {
		try {
			List<VisitMasterDto> visitMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PATIENT_VIST_DETAILS_BY_ID")
					.setParameter("patientId", visitMasterDto.getPatientId())
					.setResultTransformer(Transformers.aliasToBean(VisitMasterDto.class)).list();
			return new Response(SUCCESS, null, null, visitMasterDtosList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} 
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getDoctorListAganistSpeciality(DoctorSpecialityMapperDto doctorSpecialityMapperDto) {
		try {
			List<SpecialityMasterDto> specialityMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DOCTOR_LIST_BY_SPECIALITY")
					.setParameter("organizationId", doctorSpecialityMapperDto.getOrganizationId())
					.setParameter("unitId", doctorSpecialityMapperDto.getUnitId())
					.setParameter("specilaityId", doctorSpecialityMapperDto.getSpecialityId())
					.setResultTransformer(Transformers.aliasToBean(DoctorSpecialityMapperDto.class)).list();
			return new Response(SUCCESS, null, null, specialityMasterDtosList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response saveAdmissionRequest(AdmissionNoteDto admissionNoteDto) {
		try {
			AdmissionNote admissionNote = mapper.map(admissionNoteDto, AdmissionNote.class,
					"AdmissionNoteDto_to_AdmissionNote");
			admissionNote.setCreatedDate(TimeZoneComponent.getDateByzone(admissionNoteDto.getUnitId()));
			admissionNote.setUpdatedDate(TimeZoneComponent.getDateByzone(admissionNoteDto.getUnitId()));
			admissionNote.setIsAdmissionReqFromCoversheet(admissionNote.getIsAdmissionReqFromCoversheet()=='\u0000'?'N':admissionNote.getIsAdmissionReqFromCoversheet());
			admissionNote.setIsFlexiableDate(admissionNote.getIsFlexiableDate()=='\u0000'?'N':admissionNote.getIsFlexiableDate());
			admissionNote.setIsCancel(admissionNote.getIsCancel()=='\u0000'?'N':admissionNote.getIsCancel());
			admissionNote.setAdmissionStatus(admissionNote.getAdmissionStatus()=='\u0000'?'P':admissionNote.getAdmissionStatus());
			admissionNote.setStatus(admissionNote.getStatus()=='\u0000'?'A':admissionNote.getStatus());
			admissionNote = save(admissionNote);
			admissionNoteDto.setAdmissionNoteId(admissionNote.getAdmissionNoteId());
			return new Response(SUCCESS, null, null, null, admissionNoteDto);
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response saveAdmissionPatientMapper(AdmissionNoteDto admissionNoteDto)
			throws ApplicationException {
		try {
			AdmissionPatientMapper admissionNote = mapper.map(admissionNoteDto, AdmissionPatientMapper.class,
						"AdmissionNoteDto_to_AdmissionPatientMaper");
			admissionNote.setCreatedDate(TimeZoneComponent.getDateByzone(admissionNoteDto.getUnitId()));
			admissionNote.setUpdatedDate(TimeZoneComponent.getDateByzone(admissionNoteDto.getUnitId()));
			admissionNote.setActiveStatus(admissionNote.getActiveStatus()=='\u0000'?'A':admissionNote.getActiveStatus());
			admissionNote.setStatus(admissionNote.getStatus()=='\u0000'?'A':admissionNote.getStatus());
			sessionFactory.getCurrentSession().save(admissionNote);
			return new Response(SUCCESS, null, NOTE_GENERATED, null, admissionNoteDto);

		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);

	}

	@Override
	public Response getMaxWaitingListNumber(AdmissionNoteDto admissionNoteDto) throws ApplicationException {
		try {
				
				String pattern = "dd-MM-yyyy";
	        	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

	        	String date = simpleDateFormat.format(ADTCommonDateUtils.getDate(admissionNoteDto.getDoa(),"dd-MM-yyyy HH:mm:ss"));
				
				String query = " select "
						+ " max(this_.wait_list_number) as \"maxWaitingListNo\" "
						+ " from "
						+ " 	adt.t_bed_category_waiting_list this_"
						+ " where "
						+ " 	this_.organization_id =:organizationId "
						+ " 	and this_.unit_id =:unitId "
						+ " 	and this_.bed_category_id =:bedCategoryId "
						+ " 	and to_char(this_.doa,'dd-mm-yyyy') =:date "
						+ " 	and this_.status ='A' ";
				
				AdmissionNoteDto dto = (AdmissionNoteDto)sessionFactory.getCurrentSession()
						.createSQLQuery(query)
						.setParameter("organizationId", admissionNoteDto.getOrganizationId())
						.setParameter("unitId", admissionNoteDto.getUnitId())
						.setParameter("bedCategoryId", admissionNoteDto.getBedCategoryId())
						.setParameter("date", date)
						.setResultTransformer(Transformers.aliasToBean(AdmissionNoteDto.class)).uniqueResult();
				Integer maxWaitingListNo = dto.getMaxWaitingListNo();
				if(maxWaitingListNo==null)
					maxWaitingListNo=0;
				return new Response(SUCCESS, null, null, null, maxWaitingListNo);
			
		} catch (HibernateException he) {

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response saveWaitingListNumber(AdmissionNoteDto admissionNoteDto) throws ApplicationException {
		try {
				BedCategoryWaitingList bedCategoryWaitingList = mapper.map(admissionNoteDto, BedCategoryWaitingList.class,"AdmissionNoteDto_to_BedCategoryWaitingList");
					bedCategoryWaitingList.setCreatedDate(TimeZoneComponent.getDateByzone(admissionNoteDto.getUnitId()));
					bedCategoryWaitingList.setUpdatedDate(TimeZoneComponent.getDateByzone(admissionNoteDto.getUnitId()));
					bedCategoryWaitingList.setStatus('A');
				Integer bedCategoryWatingListId=(Integer) sessionFactory.getCurrentSession().save(bedCategoryWaitingList);
			
			return new Response(SUCCESS, null, NOTE_GENERATED, null, bedCategoryWatingListId);

		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
		
	}

	@Override
	public Response getDoctorsListAganistSpecialitys(DoctorSpecialityMapperDto doctorSpecialityMapper)
			throws ApplicationException {
		try {
			List<SpecialityMasterDto> specialityMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DOCTORS_LIST_BY_SPECIALITIES")
					.setParameter("organizationId", doctorSpecialityMapper.getOrganizationId())
					.setParameter("unitId", doctorSpecialityMapper.getUnitId())
					.setParameterList("specilaityArray", doctorSpecialityMapper.getSpecialityArray())
					.setResultTransformer(Transformers.aliasToBean(DoctorSpecialityMapperDto.class)).list();
			return new Response(SUCCESS, null, null, specialityMasterDtosList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Long getNextValAdmissionNoteId() throws ApplicationException {
		try {
			Session session = sessionFactory.getCurrentSession();
			SQLQuery query = session.createSQLQuery("select nextval('adt.admission_note_seq')");
			Long id = ((BigInteger) query.uniqueResult()).longValue();

			SQLQuery query2 = session.createSQLQuery("select setval('adt.admission_note_seq'," + (id-1) + ")");
			query2.uniqueResult();

			return id;
		}catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return null;
	}

	@Override
	public Response isAlreadyRequestExist(AdmissionNoteDto admissionNoteDto) throws ApplicationException {
		try {
			String pattern = "dd-MM-yyyy";
        	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        	Integer visitTypeId=admissionNoteDto.getSourceVisitTypeId()!=null ? admissionNoteDto.getSourceVisitTypeId() : admissionNoteDto.getVisitTypeId();
        	String date = simpleDateFormat.format(GlobalCommonDateUtils.getDate(admissionNoteDto.getDoa(),"dd-MM-yyyy"));
			
			String query = "SELECT note.admissionNoteId as admissionNoteId "
					+ "FROM AdmissionPatientMapper mpr "
					+ "INNER JOIN mpr.admissionNote note "
					+ "WHERE note.unitId=:unitId "
					+ "AND note.patientId=:patientId "
					//+ "AND note.requestToDoctorId=:docId "
					+ "AND to_char(mpr.doa,'dd-mm-yyyy')=:date "
					+ "AND mpr.status='A' "
					+ "AND mpr.activeStatus='P' "
					+ "and note.visitTypeId= :visitTypeId ";
			
					List<AdmissionNoteDto> specialityMasterDtosList = sessionFactory.getCurrentSession()													
					.createQuery(query)
					//.setParameter("docId", admissionNoteDto.getRequestToDoctorId())
					.setParameter("unitId", admissionNoteDto.getUnitId())
					.setParameter("patientId", admissionNoteDto.getPatientId())
					.setParameter("visitTypeId",visitTypeId )
					.setParameter("date",date )
					.setResultTransformer(Transformers.aliasToBean(AdmissionNoteDto.class)).list();
			
			String admsisonList = "select admission0_.patient_id as patientId "
					+ "from adt.t_admission admission0_ "
					+ "where admission0_.patient_id=:patientId "
					+ "and admission0_.unit_id=:unitId "
					+ "and admission0_.status='A' "																																																																																																																																																																																																																																																																																																	
					+ "and admission0_.visit_type_id!= :visitTypeId ";
																																																																																																																																																										
			List<AdmissionNoteDto> admissionList = sessionFactory.getCurrentSession()
					.createSQLQuery(admsisonList)
					.setParameter("unitId", admissionNoteDto.getUnitId())
					.setParameter("patientId", admissionNoteDto.getPatientId())
					.setParameter("visitTypeId", visitTypeId)

					.setResultTransformer(Transformers.aliasToBean(AdmissionNoteDto.class)).list();
			
			if(admissionList.size()>0)
				return new Response(ERROR, null, "Patient is already admitted !!", null, null);
			if(specialityMasterDtosList.size()>0)			
				return new Response(ERROR, null, "Request already exist !!", null, null);
			
			
			return new Response(SUCCESS, null, null, null, null);
		} catch (HibernateException he) {

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getNephrologyDoctorsList(DoctorSpecialityMapperDto doctorSpecialityMapper)
			throws ApplicationException {
		try {
			List<SpecialityMasterDto> specialityMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DOCTOR_LIST_BY_SPECIALITY")
					.setParameter("organizationId", doctorSpecialityMapper.getOrganizationId())
					.setParameter("unitId", doctorSpecialityMapper.getUnitId())
					.setParameter("specilaityId", doctorSpecialityMapper.getSpecialityId())
					.setResultTransformer(Transformers.aliasToBean(DoctorSpecialityMapperDto.class)).list();
			return new Response(SUCCESS, null, null, specialityMasterDtosList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getPastAdmissionDetails(AdmissionNoteDto admissionNoteDto) throws ApplicationException {
		try {
			List<AdmissionNoteDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PAST_ADMISSION_DETAILS")
					.setParameter("admissionId", admissionNoteDto.getAdmissionId())
					.setParameter("patientId", admissionNoteDto.getPatientId())
//					.setParameter("visitTypeId", admissionNoteDto.getVisitTypeId())
					.setResultTransformer(Transformers.aliasToBean(AdmissionNoteDto.class)).list();
			return new Response(SUCCESS, null, null, list, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getPatientListLikeForERMapOfBed(PatientRegistrationDto patientRegistrationDto)
			throws ApplicationException {
		try {

			List<PatientRegistrationDto> patientRegistrationDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PATIENT_LIST_LIKE_FOR_ER_MAP_OF_BED")
					.setParameter("firstName", patientRegistrationDto.getFirstName().toLowerCase() + "%")
					.setParameter("organizationId", patientRegistrationDto.getOrganizationId())
					//.setParameter("unitId", patientRegistrationDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(PatientRegistrationDto.class)).list();
			return new Response(SUCCESS, null, null, patientRegistrationDtosList, null);
	} catch (HibernateException he) {

	} catch (Exception e) {
		
	} 
	return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
}


}
