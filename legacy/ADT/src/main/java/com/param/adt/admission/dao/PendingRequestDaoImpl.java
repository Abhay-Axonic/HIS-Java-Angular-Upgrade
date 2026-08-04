package com.param.adt.admission.dao;

import java.util.List;

import com.param.global.dto.PatientRegistrationDto;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.adt.admission.dto.AdmissionPatientMapperDto;
import com.param.adt.admission.model.AdmissionNote;
import com.param.adt.admission.model.AdmissionPatientDocuments;
import com.param.adt.admission.model.AdmissionPatientMapper;
import com.param.adt.common.ADTCommonDateUtils;
import com.param.adt.common.ICommonConstants;
import com.param.adt.common.Response;
import com.param.adt.master.global.dto.PaymentEntitlementMasterDto;
import com.param.global.dto.AdmissionNoteDto;
import com.param.global.dto.KinDetailsDto;
import com.param.global.dto.PatientCategoryMasterDto;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class PendingRequestDaoImpl extends GenericDao<AdmissionNote, Integer>
		implements IPendingRequestDao, ICommonConstants {

	public PendingRequestDaoImpl() {
		super(AdmissionNote.class);
		// TODO Auto-generated constructor stub
	}

	@Autowired
	private SessionFactory sessionFactory;


	@Override
	public Response getPendingAdmissionRequestDetails(AdmissionNoteDto admissionNoteDto) throws ApplicationException {
		try {
			List<AdmissionNoteDto> admissionNoteDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PENDING_ADMISSION_REQUEST_DETAILS")
					.setParameter("organizationId", admissionNoteDto.getOrganizationId())
					.setParameter("unitId", admissionNoteDto.getUnitId())
					.setFirstResult(admissionNoteDto.getOffset() != null ? admissionNoteDto.getOffset() : 0)
					.setMaxResults(admissionNoteDto.getNoOfRecordsPerPage() != null ? admissionNoteDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(AdmissionNoteDto.class)).list();
			return new Response(SUCCESS, null, null, admissionNoteDtosList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} finally {
			 
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getAvtivePatientCategoryList(Integer orgId) throws ApplicationException {
		try {
			List<PatientCategoryMasterDto> categoryMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_PATIENT_CATEGORY_LIST")
					.setParameter("orgId", orgId)
					.setResultTransformer(Transformers.aliasToBean(PatientCategoryMasterDto.class)).list();
			return new Response(SUCCESS, null, null, categoryMasterDtosList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} finally {
			 
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getAvtivePaymentEntitlementList(Integer orgId) throws ApplicationException {
		try {
			List<PaymentEntitlementMasterDto> entitlementMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_PAYMENT_ENTITLEMENT_LIST")
					.setParameter("orgId", orgId)
					.setResultTransformer(Transformers.aliasToBean(PaymentEntitlementMasterDto.class)).list();
			return new Response(SUCCESS, null, null, entitlementMasterDtosList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} finally {
			 
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updatePendingAdmissionRequestDetails(AdmissionNoteDto admissionNoteDto)
			throws ApplicationException {
		try {

			AdmissionNote admissionNote = findById(admissionNoteDto.getAdmissionNoteId());
			admissionNote.setUpdatedBy(admissionNoteDto.getUpdatedBy());
			admissionNote.setUpdatedDate(ADTCommonDateUtils.getDate(admissionNoteDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			update(admissionNote);
			return new Response<AdmissionPatientMapperDto, Integer>(SUCCESS, null, COMMON_SUCCESS, null, admissionNote);

		} catch (HibernateException he) {
			return new Response<AdmissionPatientMapperDto, Integer>(ERROR, null, null, null, null);
		} catch (Exception e) {
			
			return new Response<AdmissionPatientMapperDto, Integer>(ERROR, null, null, null, null);
		} 
	}

	@Override
	public Response updateAdmissionRequestMapperDetails(AdmissionNoteDto admissionPatientMapperDto)
			throws ApplicationException {
		try {
			int admissionPatientId2 = 0;
			Response res = getPreviousData(admissionPatientMapperDto);
			List<AdmissionPatientMapperDto> admissionPatientMapperDtosList = res.getListObject();
			for (AdmissionPatientMapperDto a : admissionPatientMapperDtosList)
				admissionPatientId2 = a.getAdmissionPatientId();

			Session session = sessionFactory.openSession();
			Transaction tr = session.beginTransaction();

			Query qry = session.createQuery("update AdmissionPatientMapper m set m.status='I', " +
					"m.updatedBy=:updatedBy, m.updatedDate=:updatedDate " +
					"where m.admissionPatientId=:admissionPatientId AND m.status='A'");
			qry.setParameter("updatedBy", admissionPatientMapperDto.getUpdatedBy());
			qry.setParameter("updatedDate", ADTCommonDateUtils.getDate(admissionPatientMapperDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			qry.setParameter("admissionPatientId", admissionPatientId2);
			qry.executeUpdate();

			tr.commit();
			
			Transaction tr2=session.beginTransaction();
			
			AdmissionPatientMapper admissionPatientMapper = new AdmissionPatientMapper();
			admissionPatientMapper.setAdmissionNoteId(admissionPatientMapperDto.getAdmissionNoteId());
			admissionPatientMapper.setBedCategoryId(admissionPatientMapperDto.getBedCategoryId());
			admissionPatientMapper.setPatientCategoryId(admissionPatientMapperDto.getPatientCategoryId());
			admissionPatientMapper.setPaymentEntitlementId(admissionPatientMapperDto.getPaymentEntitlementId());
			admissionPatientMapper
					.setPdd(ADTCommonDateUtils.getDate(admissionPatientMapperDto.getPdd(), "dd-M-yyyy HH:mm:ss"));
			admissionPatientMapper
					.setDoa(ADTCommonDateUtils.getDate(admissionPatientMapperDto.getDoa(), "dd-M-yyyy HH:mm:ss"));
			admissionPatientMapper.setUpdatedBy(admissionPatientMapperDto.getUpdatedBy());
			admissionPatientMapper.setUpdatedDate(
					ADTCommonDateUtils.getDate(admissionPatientMapperDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			admissionPatientMapper.setCreatedDate(
					ADTCommonDateUtils.getDate(admissionPatientMapperDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			admissionPatientMapper.setStatus('A');
			admissionPatientMapper.setActiveStatus('P'); // P -----> Pending
			session.save(admissionPatientMapper);
			tr2.commit();
			session.close();
			return new Response<AdmissionNoteDto, Integer>(SUCCESS, null, COMMON_SUCCESS, null, null);

		} catch (HibernateException he) {
			return new Response<AdmissionNoteDto, Integer>(ERROR, null, null, null, null);
		} catch (Exception e) {
			
			return new Response<AdmissionNoteDto, Integer>(ERROR, null, null, null, null);
		} finally {
			 
		}
	}

	@Override
	public Response getPreviousData(AdmissionNoteDto admissionNoteDto) throws ApplicationException {
		try {
			List<AdmissionPatientMapperDto> admissionPatientMapperDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PREVIOUS_DATA")
					.setParameter("admissionNoteId", admissionNoteDto.getAdmissionNoteId())
					.setResultTransformer(Transformers.aliasToBean(AdmissionPatientMapperDto.class)).list();
			return new Response(SUCCESS, null, null, admissionPatientMapperDtosList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} finally {
			 
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateReserveBedDetails(AdmissionNoteDto admissionNoteDto) throws ApplicationException {
		try {
/*		int admissionPatientId2 = 0;
		Response res = getPreviousData(admissionNoteDto);
		List<AdmissionPatientMapperDto> admissionNoteDtosList = res.getListObject();
		for (AdmissionPatientMapperDto a : admissionNoteDtosList)
			admissionPatientId2 = a.getAdmissionPatientId();*/
			Session session = sessionFactory.openSession();
			//<-----
			Transaction tr1 = session.beginTransaction();
			Query qry1 = null;
			if (admissionNoteDto.getDepositStatus() != 0 && admissionNoteDto.getDepositStatus() == 1) {
				Query qry2 = session.createQuery("update AdmissionNote an set an.admissionStatus='R', "
						+ "an.depositAmount=:depositAmount, "
						+ "an.depositPending=:depositPending, "
						+ "an.depositCollected=:depositCollected, "
						+ "an.depositStatus=:depositStatus, "
						+ "an.updatedBy=:updatedBy, "
						+ "an.updatedDate=:updatedDate "
						+ "where an.admissionNoteId=:admissionNoteId and an.status='A'");
				qry2.setParameter("depositAmount", admissionNoteDto.getDepositAmount());
				qry2.setParameter("depositPending", admissionNoteDto.getDepositPending());
				qry2.setParameter("depositCollected", admissionNoteDto.getDepositCollected());
				qry2.setParameter("depositStatus", admissionNoteDto.getDepositStatus());
				qry2.setParameter("updatedBy", admissionNoteDto.getUpdatedBy());
				qry2.setParameter("updatedDate", ADTCommonDateUtils.getDate(admissionNoteDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
				qry2.setParameter("admissionNoteId", admissionNoteDto.getAdmissionNoteId());
				qry2.executeUpdate();

			} else {
				Query qry3 = session.createQuery("update AdmissionNote an set an.admissionStatus='R', "
						+ "an.updatedBy=:updatedBy, "
						+ "an.updatedDate=:updatedDate "
						+ "where an.admissionNoteId=:admissionNoteId and an.status='A'");
				qry3.setParameter("updatedBy", admissionNoteDto.getUpdatedBy());
				qry3.setParameter("updatedDate", ADTCommonDateUtils.getDate(admissionNoteDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
				qry3.setParameter("admissionNoteId", admissionNoteDto.getAdmissionNoteId());
				qry3.executeUpdate();
			}


//			qry1.executeUpdate();
			tr1.commit();
			//----->

			//<-----
			Transaction tr2 = session.beginTransaction();

			Query qry = session.createQuery("update AdmissionPatientMapper m set m.status='I', "
					+ "m.updatedBy=:updatedBy, "
					+ "m.updatedDate=:updatedDate "
					+ "where m.admissionNoteId=:admissionNoteId and m.status='A'");
			qry.setParameter("updatedBy", admissionNoteDto.getUpdatedBy());
			qry.setParameter("updatedDate", ADTCommonDateUtils.getDate(admissionNoteDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			qry.setParameter("admissionNoteId", admissionNoteDto.getAdmissionNoteId());
			qry.executeUpdate();
			tr2.commit();
			//----->

			//<-----
			Transaction tr3 = session.beginTransaction();
			AdmissionPatientMapper admissionPatientMapper = new AdmissionPatientMapper();
			admissionPatientMapper.setAdmissionNoteId(admissionNoteDto.getAdmissionNoteId());
			admissionPatientMapper.setBedCategoryId(admissionNoteDto.getBedCategoryId());
			admissionPatientMapper.setPatientCategoryId(admissionNoteDto.getPatientCategoryId());
			admissionPatientMapper.setPaymentEntitlementId(admissionNoteDto.getPaymentEntitlementId());
			admissionPatientMapper
					.setPdd(ADTCommonDateUtils.getDate(admissionNoteDto.getPdd(), "dd-M-yyyy HH:mm:ss"));
			admissionPatientMapper
					.setDoa(ADTCommonDateUtils.getDate(admissionNoteDto.getDoa(), "dd-M-yyyy HH:mm:ss"));
			admissionPatientMapper.setUpdatedBy(admissionNoteDto.getUpdatedBy());
			admissionPatientMapper.setUpdatedDate(
					ADTCommonDateUtils.getDate(admissionNoteDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			admissionPatientMapper.setCreatedDate(
					ADTCommonDateUtils.getDate(admissionNoteDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			admissionPatientMapper.setStatus('A');
			admissionPatientMapper.setActiveStatus('R'); // R -------> Reserved
			session.save(admissionPatientMapper);


			tr3.commit();
			//----->
			session.close();
			return new Response<AdmissionNoteDto, Integer>(SUCCESS, null, COMMON_SUCCESS, null, null);
		}
		catch(Exception e){

			return new Response<AdmissionNoteDto, Integer>(ERROR, null, e.toString(), null, null);
		}
	}

	@Override
	public Response uploadAdmissionDocuments(AdmissionPatientDocuments admissionPatientDocuments)
			throws ApplicationException {
		try {

			/*AdmissionPatientDocuments admissionPatientDocuments = mapper.map(admissionPatientDocumentsDto,
					AdmissionPatientDocuments.class, "AdmissionPatientDocumentsDto_to_AdmissionPatientDocuments");*/
			Session session = sessionFactory.openSession();
			Transaction tr = session.beginTransaction();
			session.save(admissionPatientDocuments);
			tr.commit();
			session.close();
			return new Response<KinDetailsDto, Integer>(SUCCESS, null, COMMON_SUCCESS, null, null);
		} catch (HibernateException he) {
			return new Response<KinDetailsDto, Integer>(ERROR, null, null, null, null);
		} catch (Exception e) {
			
			return new Response<KinDetailsDto, Integer>(ERROR, null, null, null, null);
		} finally {
			 
		}
	}
	
	public Response getCount(AdmissionNoteDto admissionNoteDto) throws ApplicationException {
		try {
			List<AdmissionNoteDto> admissionNoteDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PENDING_ADMISSION_REQUEST_DETAILS")
					.setParameter("organizationId", admissionNoteDto.getOrganizationId())
					.setParameter("unitId", admissionNoteDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(AdmissionNoteDto.class)).list();
			
			Integer count=admissionNoteDtosList.size();
			
			return new Response(SUCCESS, null, null, null, count);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} finally {
			 
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getOPDAdmissionRequestDetails(Integer encounterId, Integer patientId, Integer organizationId, Integer unitId) throws ApplicationException {
		try {
			List<AdmissionNoteDto> admissionNoteDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_OPD_ADMISSION_REQUEST_DETAILS")
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.setParameter("patientId", patientId)
					.setParameter("encounterId", encounterId)
					//.setParameter("visitTypeId", visitTypeId)
					.setResultTransformer(Transformers.aliasToBean(AdmissionNoteDto.class)).list();
			return new Response(SUCCESS, null, null, admissionNoteDtosList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} 
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response cancelPendingAdmissionRequest(AdmissionNoteDto admissionNoteDto) throws ApplicationException {
		try{
			Query qr1 = sessionFactory.getCurrentSession().createQuery(
					"update AdmissionNote an set an.status='I', an.isCancel='Y', an.canceledDate=now(), an.updatedBy=:updatedBy, an.updatedDate=now() where an.admissionNoteId=:admissionNoteId");
			qr1.setParameter("updatedBy", admissionNoteDto.getUpdatedBy());
			qr1.setParameter("admissionNoteId", admissionNoteDto.getAdmissionNoteId());

			qr1.executeUpdate();

			// ----->

			// <-----Updating status to I and active Status to C(Cancelled) in
			// Admission Patient Mapping

			Query qr2 = sessionFactory.getCurrentSession().createQuery(
					"update AdmissionPatientMapper apm set apm.status='I', apm.activeStatus='C', apm.updatedBy=:updatedBy, apm.updatedDate=now() WHERE apm.admissionNoteId=:admissionNoteId ");
			qr2.setParameter("updatedBy", admissionNoteDto.getUpdatedBy());
			qr2.setParameter("admissionNoteId", admissionNoteDto.getAdmissionNoteId());
			qr2.executeUpdate();
			return new Response(SUCCESS, null, "Request Cancelled..!!", null, null);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getDayCareAdmissionRequestDetails(AdmissionNoteDto admissionNoteDto) throws ApplicationException {
		try {
			List<AdmissionNoteDto> admissionNoteDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DAY_CARE_ADMISSION_REQUEST_DETAILS")
					.setParameter("organizationId", admissionNoteDto.getOrganizationId())
					.setParameter("unitId", admissionNoteDto.getUnitId())
					.setParameter("patientId", admissionNoteDto.getPatientId())
					.setParameter("admissionId", admissionNoteDto.getAdmissionId())
					.setParameter("visitTypeId", admissionNoteDto.getVisitTypeId())
					.setResultTransformer(Transformers.aliasToBean(AdmissionNoteDto.class)).list();
			return new Response(SUCCESS, null, null, admissionNoteDtosList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			e.printStackTrace();
		} 
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getAdmissionRequestDetailsForBookView(AdmissionNoteDto admissionNoteDto)
			throws ApplicationException {
		try {
			List<AdmissionNoteDto> admissionNoteDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ADMISSION_REQUEST_DETAILS_FOR_BOOK_VIEW")
					.setParameter("patientId", admissionNoteDto.getPatientId())
					.setParameter("fromDate", admissionNoteDto.getFromDate())
					.setParameter("toDate", admissionNoteDto.getToDate())
					.setResultTransformer(Transformers.aliasToBean(AdmissionNoteDto.class)).list();
			return new Response(SUCCESS, null, null, admissionNoteDtosList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} 
		return new Response(ERROR, null, null, null, null);
	}

    @Override
    public Response getPatientRegistrationTypes() throws ApplicationException {
        try {
            String query = "select master.id as \"registrationTypeId\", " +
                    " master.reg_type as \"registrationTypeName\" " +
                    " from patient.m_patient_registration_type_master master ";

            List<PatientCategoryMasterDto> list = sessionFactory.getCurrentSession()
                    .createSQLQuery(query)
                    .setResultTransformer(Transformers.aliasToBean(PatientCategoryMasterDto.class)).list();
            return new Response(SUCCESS, null, null, list, null);
        } catch (HibernateException he) {

        } catch (Exception e) {

        }
        return new Response(ERROR, null, null, null, null);
    }

}
