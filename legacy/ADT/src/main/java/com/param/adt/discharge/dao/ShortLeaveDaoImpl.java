package com.param.adt.discharge.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.adt.common.ADTCommonDateUtils;
import com.param.adt.common.ICommonConstants;
import com.param.adt.common.Response;
import com.param.adt.discharge.model.ShortLeave;
import com.param.global.dto.DischargeDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class ShortLeaveDaoImpl extends GenericDao<ShortLeave, Integer> implements IShortLeaveDao,ICommonConstants 
{

	public ShortLeaveDaoImpl() {
		super(ShortLeave.class);
		// TODO Auto-generated constructor stub
	}

	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	public Response saveShortLeaveRequest(DischargeDto dischargeDto) {
		try {
			ShortLeave shortLeave =new ShortLeave();
				shortLeave.setOrganizationId(dischargeDto.getOrganizationId());
				shortLeave.setUnitId(dischargeDto.getUnitId());
				shortLeave.setPatientId(dischargeDto.getPatientId());
				shortLeave.setAdmissionId(dischargeDto.getAdmissionId());
				shortLeave.setFromDate(ADTCommonDateUtils.getDate(dischargeDto.getFromDate(),"dd-MM-yyyy HH:mm:ss"));
				shortLeave.setToDate(ADTCommonDateUtils.getDate(dischargeDto.getToDate(),"dd-MM-yyyy HH:mm:ss"));
				shortLeave.setNote(dischargeDto.getNote());
				shortLeave.setCreatedBy(dischargeDto.getCreatedBy());
				shortLeave.setCreatedDate(ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dischargeDto.getUnitId()),"dd-MM-yyyy HH:mm:ss"));
				shortLeave.setUpdatedBy(dischargeDto.getUpdatedBy());
				shortLeave.setUpdatedDate(ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dischargeDto.getUnitId()),"dd-MM-yyyy HH:mm:ss"));
				shortLeave.setStatus(dischargeDto.getStatus());
				shortLeave.settPatientId(dischargeDto.gettPatientId());
				shortLeave.setFromTime(dischargeDto.getFromTime());
				shortLeave.setToTime(dischargeDto.getToTime());
				shortLeave.setShortLeaveStatusId(dischargeDto.getShortLeaveStatusId());
				shortLeave.setShortLeaveReasonId(dischargeDto.getShortLeaveReasonId());
				shortLeave.setTreatingDoctorId(dischargeDto.getDoctorId());
				shortLeave.setIsHandoverMedication('N');
			shortLeave = save(shortLeave);
			return new Response(SUCCESS, null, null, null, null);
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getShortLeaveRequestList(DischargeDto dischargeDto) throws ApplicationException {
		try {
			List<DischargeDto> getShortLeaveRequestList = sessionFactory.getCurrentSession()

					.getNamedQuery("GET_SHORT_LEAVE_REQUEST_LIST")
					.setParameter("organizationId", dischargeDto.getOrganizationId())
					.setParameter("unitId", dischargeDto.getUnitId())
					.setParameterList("shortLeaveStatusIdList", dischargeDto.getShortLeaveStatusIdList())
					.setFirstResult(dischargeDto.getOffset() != null ? dischargeDto.getOffset() : 0)
					.setMaxResults(dischargeDto.getNoOfRecordsPerPage() != null ? dischargeDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(DischargeDto.class)).list();
			return new Response(SUCCESS, null, null, getShortLeaveRequestList, null);
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getShortLeaveRequestListForDoctor(DischargeDto dischargeDto) throws ApplicationException {
		try {
			StringBuilder query=new StringBuilder(sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SHORT_LEAVE_REQUEST_LIST_FOR_DOCTOR").getQueryString());
			if(dischargeDto.getAdmissionId()!=null) {
				query.append(" AND admission.admission_id=:admissionId ");
			}
			Query sql = sessionFactory.getCurrentSession()
					.createSQLQuery(query.toString())
					.setParameter("organizationId", dischargeDto.getOrganizationId())
					.setParameter("unitId", dischargeDto.getUnitId())
					.setParameter("treatingDoctorId", dischargeDto.getDoctorId());
			if(dischargeDto.getAdmissionId()!=null) {
				sql.setParameter("admissionId", dischargeDto.getAdmissionId());
			}
			List<DischargeDto> getShortLeaveRequestListForDoctor = sql.setResultTransformer(Transformers.aliasToBean(DischargeDto.class)).list();
			return new Response(SUCCESS, null, null, getShortLeaveRequestListForDoctor, null);
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response acceptShotLeaveByDoctor(DischargeDto dischargeDto) throws ApplicationException {
		try {

			Session session = sessionFactory.getCurrentSession();
			Query query = session.createQuery("update ShortLeave sl set " +
					"sl.updatedBy = :updatedBy, " +
					"sl.updatedDate = :updatedDate, " +
					"sl.shortLeaveStatusId = 2, " +
					"sl.doctorsNote = :doctorsNote, " +
					"sl.authorizedBy = :authorizedBy " +
					"WHERE sl.shortLeaveId = :shortLeaveId " +
					"AND sl.organizationId = :organizationId " +
					"AND sl.unitId = :unitId " +
					"AND sl.status = 'A'");
			query.setParameter("updatedBy", dischargeDto.getUpdatedBy());
			query.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dischargeDto.getUnitId()), "dd-MM-yyyy HH:mm:ss"));
			query.setParameter("doctorsNote", dischargeDto.getDoctorsNote());
			query.setParameter("authorizedBy", dischargeDto.getAuthorizedBy());
			query.setParameter("shortLeaveId", dischargeDto.getShortLeaveId());
			query.setParameter("organizationId", dischargeDto.getOrganizationId());
			query.setParameter("unitId", dischargeDto.getUnitId());
			query.executeUpdate();

			return new Response(SUCCESS, null, null, null, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} 
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response rejectShotLeaveByDoctor(DischargeDto dischargeDto) throws ApplicationException {
		try {
			Session session = sessionFactory.getCurrentSession();
			Query query = session.createQuery("update ShortLeave sl set " +
					"sl.updatedBy = :updatedBy, " +
					"sl.updatedDate = :updatedDate, " +
					"sl.shortLeaveStatusId = 7, " +
					"sl.rejectionReasonId = :rejectionReasonId, " +
					"sl.doctorsNote = :doctorsNote " +
					"WHERE sl.shortLeaveId = :shortLeaveId " +
					"AND sl.organizationId = :organizationId " +
					"AND sl.unitId = :unitId " +
					"AND sl.status = 'A'");
			query.setParameter("updatedBy", dischargeDto.getUpdatedBy());
			query.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dischargeDto.getUnitId()), "dd-MM-yyyy HH:mm:ss"));
			query.setParameter("rejectionReasonId", dischargeDto.getRejectionReasonId());
			query.setParameter("doctorsNote", dischargeDto.getDoctorsNote());
			query.setParameter("shortLeaveId", dischargeDto.getShortLeaveId());
			query.setParameter("organizationId", dischargeDto.getOrganizationId());
			query.setParameter("unitId", dischargeDto.getUnitId());
			query.executeUpdate();

			return new Response(SUCCESS, null, null, null, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} 
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response acceptShotLeaveByBilling(DischargeDto dischargeDto) throws ApplicationException {
		try {

			Session session = sessionFactory.getCurrentSession();
			Query query = session.createQuery("update ShortLeave sl set" +
					"sl.updatedBy=:updatedBy, " +
					" sl.updatedDate=:updatedDate, "+
					" sl.billingsNote=:billingsNote, "+
					" sl.shortLeaveStatusId=3 "+
					" WHERE sl.shortLeaveId=:shortLeaveId " +
					" AND sl.organizationId=:organizationId "+
					" AND sl.unitId=:unitId " +
					" AND sl.status='A' ");
			query.setParameter("updatedBy", dischargeDto.getUpdatedBy());
			query.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dischargeDto.getUnitId()), "dd-MM-yyyy HH:mm:ss"));
			query.setParameter("billingsNote", dischargeDto.getBillingsNote());
			query.setParameter("shortLeaveId", dischargeDto.getShortLeaveId());
			query.setParameter("organizationId", dischargeDto.getOrganizationId());
			query.setParameter("unitId", dischargeDto.getUnitId());
			query.executeUpdate();
			return new Response(SUCCESS, null, null, null, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} 
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response rejectShotLeaveByBilling(DischargeDto dischargeDto) throws ApplicationException {
		try {

			Session session = sessionFactory.getCurrentSession();
			Query query = session.createQuery("update ShortLeave sl set " +
					"sl.updatedBy = :updatedBy, " +
					"sl.updatedDate = :updatedDate, " +
					"sl.rejectionReasonId = :rejectionReasonId, " +
					"sl.billingsNote = :billingsNote, " +
					"sl.shortLeaveStatusId = 8 " +
					"WHERE sl.shortLeaveId = :shortLeaveId " +
					"AND sl.organizationId = :organizationId " +
					"AND sl.unitId = :unitId " +
					"AND sl.status = 'A'");
			query.setParameter("updatedBy", dischargeDto.getUpdatedBy());
			query.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dischargeDto.getUnitId()), "dd-MM-yyyy HH:mm:ss"));
			query.setParameter("rejectionReasonId", dischargeDto.getRejectionReasonId());
			query.setParameter("billingsNote", dischargeDto.getBillingsNote());
			query.setParameter("shortLeaveId", dischargeDto.getShortLeaveId());
			query.setParameter("organizationId", dischargeDto.getOrganizationId());
			query.setParameter("unitId", dischargeDto.getUnitId());
			query.executeUpdate();
			return new Response(SUCCESS, null, null, null, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} 
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response releasePatientForShotLeave(DischargeDto dischargeDto) throws ApplicationException {
		try {

			Session session = sessionFactory.getCurrentSession();
				Query query = session.createQuery("update ShortLeave sl set sl.updatedBy=:updatedBy, "
						+ " sl.updatedDate=:updatedDate,"
						+ " sl.shortLeaveStatusId=4 "
						+ " WHERE sl.shortLeaveId=:shortLeaveId "
						+ " AND sl.organizationId=:organizationId "
						+ " AND sl.unitId=:unitId "
						+ " AND sl.status='A' ");
			query.setParameter("updatedBy", dischargeDto.getUpdatedBy());
			query.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dischargeDto.getUnitId()), "dd-MM-yyyy HH:mm:ss"));
			query.setParameter("shortLeaveId", dischargeDto.getShortLeaveId());
			query.setParameter("organizationId", dischargeDto.getOrganizationId());
			query.setParameter("unitId", dischargeDto.getUnitId());
				query.executeUpdate();
				
				Query query2 = session.createQuery("update AdmissionDetails sl set sl.updatedBy=:updatedBy, "
						+ " sl.updatedDate=:updatedDate, "
						+ " sl.isShortLeave='Y' "
						+ " WHERE sl.admissionId=:admissionId "
						+ " AND sl.organizationId=:organizationId "
						+ " AND sl.unitId=:unitId "
						+ " AND sl.status='A' ");
			query2.setParameter("updatedBy", dischargeDto.getUpdatedBy());
			query2.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dischargeDto.getUnitId()), "dd-MM-yyyy HH:mm:ss"));
			query2.setParameter("admissionId", dischargeDto.getAdmissionId());
			query2.setParameter("organizationId", dischargeDto.getOrganizationId());
			query2.setParameter("unitId", dischargeDto.getUnitId());
				query2.executeUpdate();
			return new Response(SUCCESS, null, null, null, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} 
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getDoctorAcceptedPatientsForShortLeave(DischargeDto dischargeDto) throws ApplicationException {
		try {
			Integer count=0;
			List<DischargeDto> getShortLeaveRequestListForDoctor = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SHORT_LEAVE_ACCEPTED_PATIENT_LIST_BY_DOCTOR")
					.setParameter("organizationId", dischargeDto.getOrganizationId())
					.setParameter("unitId", dischargeDto.getUnitId())
					.setFirstResult(dischargeDto.getOffset() != null ? dischargeDto.getOffset()  : 0)
					.setMaxResults(dischargeDto.getNoOfRecordsPerPage() != null ? dischargeDto.getNoOfRecordsPerPage()  : 10)
					.setResultTransformer(Transformers.aliasToBean(DischargeDto.class)).list();
			
			List<DischargeDto> listCount = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SHORT_LEAVE_ACCEPTED_PATIENT_LIST_BY_DOCTOR")
					.setParameter("organizationId", dischargeDto.getOrganizationId())
					.setParameter("unitId", dischargeDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(DischargeDto.class)).list();
			
            if(listCount != null && listCount.size()>0) {
                count= listCount.size();
            }

			return new Response(SUCCESS, null, null, getShortLeaveRequestListForDoctor, count);
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response rejectShotLeaveByNurse(DischargeDto dischargeDto) throws ApplicationException {
		try {
			
			Session session = sessionFactory.getCurrentSession();
			Query query = session.createQuery("update ShortLeave sl set sl.updatedBy=:updatedBy, "
					+ " sl.updatedDate=:updatedDate, "
					+ " sl.rejectionReasonId=:rejectionReasonId, "
					+ " sl.nurseNote=:nurseNote, "
					+ " sl.shortLeaveStatusId=9 "
					+ " WHERE sl.shortLeaveId=:shortLeaveId "
					+ " AND sl.organizationId=:organizationId "
					+ " AND sl.unitId=:unitId "
					+ " AND sl.status='A' ");
			query.setParameter("updatedBy", dischargeDto.getUpdatedBy());
			query.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dischargeDto.getUnitId()), "dd-MM-yyyy HH:mm:ss"));
			query.setParameter("rejectionReasonId", dischargeDto.getRejectionReasonId());
			query.setParameter("nurseNote", dischargeDto.getNurseNote());
			query.setParameter("shortLeaveId", dischargeDto.getShortLeaveId());
			query.setParameter("organizationId", dischargeDto.getOrganizationId());
			query.setParameter("unitId", dischargeDto.getUnitId());
			query.executeUpdate();
			return new Response(SUCCESS, null, null, null, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} 
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getShortLeaveRequestListCount(DischargeDto dischargeDto) throws ApplicationException {
		try {
			List<DischargeDto> listForCount = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SHORT_LEAVE_REQUEST_LIST")
					.setParameter("organizationId", dischargeDto.getOrganizationId())
					.setParameter("unitId", dischargeDto.getUnitId())
					.setParameterList("shortLeaveStatusIdList", dischargeDto.getShortLeaveStatusIdList())
					.setResultTransformer(Transformers.aliasToBean(DischargeDto.class)).list();
			
			return new Response(SUCCESS, null, null, null, listForCount.size());
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	
}
