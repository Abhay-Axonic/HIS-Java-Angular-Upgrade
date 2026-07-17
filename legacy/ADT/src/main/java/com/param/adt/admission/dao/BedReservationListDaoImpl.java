package com.param.adt.admission.dao;

import java.util.List;

import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.adt.admission.dto.UnreservedPatientDto;
import com.param.adt.admission.model.AdmissionPatientMapper;
import com.param.adt.admission.model.UnreservedPatient;
import com.param.adt.common.ADTCommonDateUtils;
import com.param.adt.common.ICommonConstants;
import com.param.adt.common.Response;
import com.param.global.dto.AdmissionDto;
import com.param.global.dto.AdmissionNoteDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings({ "rawtypes", "unchecked" })
@Repository
public class BedReservationListDaoImpl extends GenericDao<UnreservedPatient, Integer>
		implements IBedReservationListDao, ICommonConstants {

	public BedReservationListDaoImpl() {
		super(UnreservedPatient.class);
		// TODO Auto-generated constructor stub
	}

	@Autowired
	DozerBeanMapper mapper;

	@Autowired
	SessionFactory sessoinFactory;

	@Override
	public Response getBedReservationList(AdmissionDto admissionDto) {
		try {

			List<AdmissionNoteDto> admissionNoteDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BED_RESERVATION_LIST_DETAILS")
					.setFirstResult(admissionDto.getOffset() != null ? admissionDto.getOffset() : 0)
					.setMaxResults(admissionDto.getNoOfRecordsPerPage() != null ? admissionDto.getNoOfRecordsPerPage() : 10)
					.setParameter("organizationId", admissionDto.getOrganizationId())
					.setParameter("unitId", admissionDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(AdmissionNoteDto.class)).list();
			return new Response(SUCCESS, null, null, admissionNoteDtosList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} finally {
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response cancelReservation(UnreservedPatientDto unreservedPatientDto) throws ApplicationException {
		try {

			Session session = sessionFactory.getCurrentSession();
			// <-----Updating status in AdmissionNote to I from A, activating is
			// cancledBy Flag to Y
			Query qr1 = session.createQuery(
					"update AdmissionNote an set " +
							"an.status = :status, " +
							"an.isCancel = :isCancel, " +
							"an.canceledDate = now(), " +
							"an.updatedBy = :updatedBy, " +
							"an.updatedDate = :updatedDate " +
							"where an.admissionNoteId = :admissionNoteId"
			);
			qr1.setParameter("status", 'I');
			qr1.setParameter("isCancel", 'Y');
			qr1.setParameter("updatedBy", unreservedPatientDto.getUpdatedBy());
			qr1.setParameter("updatedDate", ADTCommonDateUtils.getDate(unreservedPatientDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			qr1.setParameter("admissionNoteId", unreservedPatientDto.getAdmissionNoteId());
			qr1.executeUpdate();


			// ----->

			// <-----Updating status to I and active Status to C(Cancelled) in
			// Admission Patient Mapping

			Query qr2 = session.createQuery(
					"update AdmissionPatientMapper apm set" +
							"apm.status =:status, "+
							"apm.activeStatus=:activeStatus, "+
							"apm.updatedBy=:updatedBy, " +
							"apm.updatedDate=:updatedDate, " +
							"WHERE apm.admissionNoteId=:admissionNoteId ");
			qr2.setParameter("status", 'I');
			qr2.setParameter("activeStatus", 'C');
			qr2.setParameter("updatedBy", unreservedPatientDto.getUpdatedBy());
			qr2.setParameter("updatedDate", ADTCommonDateUtils.getDate(unreservedPatientDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			qr2.setParameter("admissionNoteId", unreservedPatientDto.getAdmissionNoteId());
			qr2.executeUpdate();
			// ----->

			// <----- Adding a entry for cancellation in Unreserve Patient
			// transaction

			UnreservedPatient unreservedPatient = new UnreservedPatient();
				unreservedPatient.setAdmissionNoteId(unreservedPatientDto.getAdmissionNoteId());
				unreservedPatient.setIsCancelReservation(unreservedPatientDto.getIsCancelReservation());
				unreservedPatient.setReasonForCancelation(unreservedPatientDto.getReasonForCancelation());
				unreservedPatient.setDoa(ADTCommonDateUtils.getDate(unreservedPatientDto.getDoa(), "dd-MM-yyyy HH:mm:ss"));
				unreservedPatient.setCreatedDate(ADTCommonDateUtils.getDate(unreservedPatientDto.getCreatedDate(), "dd-MM-yyyy HH:mm:ss"));
				unreservedPatient.setUpdatedDate(ADTCommonDateUtils.getDate(unreservedPatientDto.getUpdatedDate(), "dd-MM-yyyy HH:mm:ss"));
				unreservedPatient.setUpdatedBy(unreservedPatientDto.getUpdatedBy());
				unreservedPatient.setCreatedBy(unreservedPatientDto.getCreatedBy());
				unreservedPatient.setReasonForAdmission(unreservedPatientDto.getReasonForAdmission());
				unreservedPatient.setStatus(unreservedPatientDto.getStatus());
				unreservedPatient.setIsCancelReservation(unreservedPatient.getIsCancelReservation());
				unreservedPatient.setIsRescheduleReservation(unreservedPatientDto.getIsRescheduleReservation());
				unreservedPatient.setIsFlexible(unreservedPatientDto.getIsFlexible());
			session.save(unreservedPatient);
			// ----->
			return new Response(SUCCESS, null, null, null, null);

		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);

	}

	@Override
	public Response rescheduleReservation(UnreservedPatientDto unreservedPatientDto) throws ApplicationException {
		try {
			Session session = sessionFactory.getCurrentSession();
			// <-----Updating status in AdmissionNote to I from A
			Query qr1 = session.createQuery(
					"update AdmissionNote an set " +
							"an.admissionStatus = :admissionStatus, " +
							"an.updatedBy = :updatedBy, " +
							"an.updatedDate = :updatedDate " +
							"where an.admissionNoteId = :admissionNoteId"
			);
			qr1.setParameter("admissionStatus", 'P');
			qr1.setParameter("updatedBy", unreservedPatientDto.getUpdatedBy());
			qr1.setParameter("updatedDate", ADTCommonDateUtils.getDate(unreservedPatientDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			qr1.setParameter("admissionNoteId", unreservedPatientDto.getAdmissionNoteId());
			qr1.executeUpdate();

			// ----->

			// <-----Updating status to I and active Status to RS(Reschedule) in
			// Admission Patient Mapping
			Query qr2 = session.createQuery(
					"update AdmissionPatientMapper apm set " +
							"apm.status = :status, " +
							"apm.activeStatus = :activeStatus, " +
							"apm.updatedBy = :updatedBy, " +
							"apm.updatedDate = :updatedDate " +
							"WHERE apm.admissionNoteId = :admissionNoteId"
			);
			qr2.setParameter("status", 'I');
			qr2.setParameter("activeStatus", 'I');
			qr2.setParameter("updatedBy", unreservedPatientDto.getUpdatedBy());
			qr2.setParameter("updatedDate", ADTCommonDateUtils.getDate(unreservedPatientDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			qr2.setParameter("admissionNoteId", unreservedPatientDto.getAdmissionNoteId());
			qr2.executeUpdate();
			// ----->
			// <----- Adding new entry in Admission Patient Mapper
			AdmissionPatientMapper admissionPatientMapper = mapper.map(unreservedPatientDto,
					AdmissionPatientMapper.class, "UnreservedPatientDto_to_AdmissionPatientMapper");
			
			admissionPatientMapper.setCreatedDate(TimeZoneComponent.getDateByzone(unreservedPatientDto.getUnitId() != null ? unreservedPatientDto.getUnitId() : 0));
			admissionPatientMapper.setUpdatedDate(TimeZoneComponent.getDateByzone(unreservedPatientDto.getUnitId() != null ? unreservedPatientDto.getUnitId() : 0));
			

			session.save(admissionPatientMapper);

			// ----->
			// <----- Adding a entry for cancellation in Unreserve Patient
			// transaction
			UnreservedPatient unreservedPatient = new UnreservedPatient();
			unreservedPatient.setAdmissionNoteId(unreservedPatientDto.getAdmissionNoteId());
			unreservedPatient
					.setNewDoa(ADTCommonDateUtils.getDate(unreservedPatientDto.getNewDoa(), "dd-M-yyyy HH:mm:ss"));
			unreservedPatient.setDoa(ADTCommonDateUtils.getDate(unreservedPatientDto.getDoa(), "dd-M-yyyy HH:mm:ss"));
			unreservedPatient.setIsFlexible(unreservedPatientDto.getIsFlexible());
			unreservedPatient.setBedCategoryId(unreservedPatientDto.getBedCategoryId());
			unreservedPatient.setIsRescheduleReservation(unreservedPatient.getIsRescheduleReservation());
			unreservedPatient.setPatientId(unreservedPatientDto.getPatientId());
			unreservedPatient.setCreatedDate(
					ADTCommonDateUtils.getDate(unreservedPatientDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			unreservedPatient.setUpdatedDate(
					ADTCommonDateUtils.getDate(unreservedPatientDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			unreservedPatient.setUpdatedBy(unreservedPatientDto.getUpdatedBy());
			unreservedPatient.setCreatedBy(unreservedPatientDto.getCreatedBy());
			unreservedPatient.setStatus(unreservedPatientDto.getStatus());
			unreservedPatient.setReasonForUnreserve(unreservedPatientDto.getReasonForUnreserve());
			unreservedPatient.setReasonForAdmission(unreservedPatientDto.getReasonForAdmission());
			unreservedPatient.setIsCancelReservation(unreservedPatient.getIsCancelReservation());
			unreservedPatient.setOrganizatoinId(unreservedPatientDto.getOrganizationId());
			unreservedPatient.setUnitId(unreservedPatientDto.getUnitId());
			session.save(unreservedPatient);
			// ----->

			return new Response(SUCCESS, null, null, null, null);

		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);

	}
	
	public Response getCount(AdmissionDto admissionDto) throws ApplicationException {
		try{
			List<AdmissionNoteDto> admissionNoteDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BED_RESERVATION_LIST_DETAILS")
					.setParameter("organizationId", admissionDto.getOrganizationId())
					.setParameter("unitId", admissionDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(AdmissionNoteDto.class)).list();	
			
			Integer count=admissionNoteDtosList.size();
			return new Response(SUCCESS, null, null, null, count);
		}catch(Exception e)
		{
			
			return new Response(ERROR, null, null, null, null);
		}
	}

	
}
