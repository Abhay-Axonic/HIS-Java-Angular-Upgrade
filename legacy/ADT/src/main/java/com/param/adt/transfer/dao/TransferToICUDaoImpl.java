package com.param.adt.transfer.dao;

import java.util.List;

import org.dozer.DozerBeanMapper;
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
import com.param.adt.master.global.dto.BedMasterDto;
import com.param.adt.transfer.dto.TransferDto;
import com.param.adt.transfer.model.Transfer;
import com.param.adt.transfer.model.TransferRequest;
import com.param.global.dto.DoctorMasterDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class TransferToICUDaoImpl extends GenericDao<TransferRequest, Integer> implements ITransferToICUDao,ICommonConstants {

	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	DozerBeanMapper mapper;
	
	public TransferToICUDaoImpl() {
		super(TransferRequest.class);
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public Response getDoctorForICU(DoctorMasterDto doctorMasterDto) throws ApplicationException {
		try {
			List<TransferDto> pendingTransferRequestList = sessionFactory.getCurrentSession()

					.getNamedQuery("GET_DOCTOR_FOR_ICU")
					.setParameter("orgId", doctorMasterDto.getOrganizationId())
					.setParameter("unitId", doctorMasterDto.getUnitId())
					.setParameter("specialityId", 9)// Speciality Id 9 is for Intensivist
					.setResultTransformer(Transformers.aliasToBean(DoctorMasterDto.class)).list();
			return new Response(SUCCESS, null, null, pendingTransferRequestList, null);
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}


	@Override
	public Response saveICUTransferRequest(TransferDto transferDto) throws ApplicationException {
		try {
			
			Session session= sessionFactory.getCurrentSession();
			
			//<-----In-activating previous TransferRequest entry if any
			Query q = session.createQuery("update TransferRequest traReq set "
					+ "traReq.status = :status, "
					+ "traReq.updatedBy = :updatedBy, "
					+ "traReq.updatedDate = :updatedDate "
					+ "WHERE traReq.admissionId = :admissionId "
					+ "AND traReq.unitId = :unitId "
					+ "AND traReq.organizationId = :organizationId");
			q.setParameter("status", 'I');
			q.setParameter("updatedBy", transferDto.getUpdatedBy());
			q.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0), "dd-MM-yyyy HH:mm:ss"));
			q.setParameter("admissionId", transferDto.getAdmissionId());
			q.setParameter("unitId", transferDto.getUnitId());
			q.setParameter("organizationId", transferDto.getOrganizationId());
			q.executeUpdate();
			//----->
			//<-----Generating new transferRequest
			TransferRequest transferRequest = mapper.map(transferDto, TransferRequest.class,
					"TransferDto_to_TransferRequest");
			transferRequest.setStatus('A');
			transferRequest=save(transferRequest);
			//----->
			return new Response(SUCCESS, null, null, null, transferRequest);
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}


	@Override
	public Response saveICUTransfer(TransferDto transferDto) throws ApplicationException {
		try {
			//Session session=sessionFactory.getCurrentSession();
			//<-----In-activating previous TransferRequest entry if any
			
			List<TransferDto> listServiceMasterDto = sessionFactory.getCurrentSession()
					.createSQLQuery(
							"SELECT transfer_id as \"transferId\" FROM adt.t_transfer WHERE admission_id="
									+ transferDto.getAdmissionId() + " AND status = 'A' limit 1")
					.setResultTransformer(Transformers.aliasToBean(TransferDto.class)).list();
			
			if(listServiceMasterDto.size() > 0) 
			{
				transferDto.setOldTransferId(listServiceMasterDto.get(0).getTransferId());
			}
			
			Query q=sessionFactory.getCurrentSession().createQuery("update Transfer traReq set "
					+"traReq.status=:status, "
					+ "traReq.updatedBy=:updatedBy, "
					+ "traReq.updatedDate=:updatedDate "
					+ "WHERE traReq.admissionId=:admissionId "
					+ "AND traReq.unitId=:unitId "
					+ "AND traReq.organizationId=:organizationId ");
			q.setParameter("status", 'I');
			q.setParameter("updatedBy", transferDto.getUpdatedBy());
			q.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0), "dd-MM-yyyy HH:mm:ss"));
			q.setParameter("admissionId", transferDto.getAdmissionId());
			q.setParameter("unitId", transferDto.getUnitId());
			q.setParameter("organizationId", transferDto.getOrganizationId());
			q.executeUpdate();
			//----->
			
			
			//Transaction tr=session.beginTransaction();
			Transfer transfer = mapper.map(transferDto, Transfer.class,"TransferDto_to_Transfer");
				transfer.setFromDate(ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
				transfer.setFromTime(ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
			transfer.setStatus('A');
			transfer.setIsPrimary('Y');
			sessionFactory.getCurrentSession().save(transfer);
			return new Response(SUCCESS, null, null, null, null);
		} catch (HibernateException he) {
			System.out.println(he);
		} catch (Exception e) {
			System.out.println(e);
		}
		return new Response(ERROR, null, null, null, null);
	}


	@Override
	public Response getICUTransferRequestList(TransferDto transferDto) throws ApplicationException {
		try {
			StringBuilder query1 = new StringBuilder("SELECT transfer.transfer_id as \"transferId\", "
					+" transfer.old_transfer_id as \"oldTransferId\", "
					+" transfer.transfer_request_id as \"transferRequestId\", "
					+ "admission.admission_id as \"admissionId\", "
					+ "adl.admission_details_id as \"admissionDetailsId\", "		
					+ "admission.patient_id as \"patientId\", "
					+ "admission.t_patient_id as \"tPatientId\", "
					+" pr.uhid_number as \"UHIDNumber\", "
					+" concat(coalesce(pr.first_name),' ',coalesce(pr.middle_name),' ',coalesce(pr.last_name)) as \"patientName\",  " 
					+" pr.gender_id as \"genderId\",   "
					+" gm.gender_code as \"genderCode\",  "
					+" to_char(pr.dob,'MM/dd/yyyy') as \"dob\",   "
					+" transfer.from_ward_id as \"fromWardId\", "
					+" ward.ward_name as \"fromWardName\", "
					+" transfer.from_bed_id as \"fromBedId\", " 
					+" bm.bed_number as \"fromBedNumber\", "
					+" transfer.from_bed_category_id as \"fromBedCategoryId\", "
					+" bcm1.bed_category_desc as \"fromBedCategoryDesc\", "
					+" bm.floor_id as \"fromFloorId\", "
					+" bm.room_id as \"fromRoomId\", "
					+" transfer.to_ward_id as \"toWardId\", "
					+" ward2.ward_name as \"toWardName\", "
					+" transfer.to_bed_id as \"toBedId\", " 
					+" bm2.bed_number as \"toBedNumber\", "
					+" transfer.to_bed_category_id as \"toBedCategoryId\", "
					+" bcm2.bed_category_desc as \"toBedCategoryDesc\", "
					+" bm2.floor_id as \"toFloorId\", "
					+" bm2.room_id as \"toRoomId\", "
					+" traReq.treating_doctor_id as \"doctorId\", "
					+" concat(coalesce(doc.first_name),' ',coalesce(doc.middle_name),' ',coalesce(doc.last_name)) as \"doctorName\", "
					+" traReq.reason_id as \"reasonId\",  "
					+" reason.reason_desc as \"reasonDesc\", "
					+" traReq.note as \"note\", "
					+ "traReq.icu_type_id as \"icuTypeId\", "
					+ "icu.icu_type as \"icuType\", "
					+ "traReq.transfer_status_id as \"transferStatusId\", "
					+ "to_char(traReq.created_date,'DD-MM-YYYY HH24:MI:ss') as \"createdDate\","
					+" traReq.final_note as \"finalNote\", "
					+ "bm.billing_bed_category_id as \"fromBillingBedCategoryId\" ,"
					+ " prefxmstr.prefix_code as \"prefixDesc\",pr.image_path as \"imagePath\",  "
//			        + " concat(to_char(extract(year from age(current_timestamp, pr.dob)), '999'), 'Y') as \"ageStr\" " 
					+ " concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, pr.dob)) ,'Y ', EXTRACT(month from AGE(CURRENT_TIMESTAMP, pr.dob)) , 'M ' , EXTRACT(day from AGE(CURRENT_TIMESTAMP, pr.dob)),'D ') as \"ageStr\" "

					//+ "bm2.billing_bed_category_id as \"toBillingBedCategoryId\" "
					+" FROM adt.t_transfer transfer "
					+" INNER JOIN adt.t_admission admission on admission.admission_id=transfer.admission_id "
					+ "INNER JOIN adt.t_admission_details adl on admission.admission_id=adl.admission_id "
					+" INNER JOIN patient.t_patient_registration pr on admission.patient_id=pr.patient_id  "
					+ " LEFT join public.m_prefix_master prefxmstr on prefxmstr.prefix_id = pr.prefix_id "
					+" INNER JOIN public.m_gender_master gm on pr.gender_id=gm.gender_id   "
					+" INNER JOIN adt.m_bed_master bm on transfer.from_bed_id=bm.bed_id   "
					+" LEFT JOIN adt.m_bed_master bm2 on transfer.to_bed_id=bm2.bed_id   "
					+" INNER JOIN adt.m_floor_master floor on bm.floor_id=floor.floor_id  "
					+" INNER JOIN adt.m_bed_category_master bcm1 on transfer.from_bed_category_id=bcm1.bed_category_id  "
					+" LEFT JOIN adt.m_bed_category_master bcm2 on transfer.to_bed_category_id=bcm2.bed_category_id  "
					+" INNER JOIN adt.m_ward_master ward on transfer.from_ward_id=ward.ward_id "
					+" LEFT JOIN adt.m_ward_master ward2 on transfer.to_ward_id=ward2.ward_id "
					+" INNER JOIN adt.t_transfer_request traReq on transfer.transfer_request_id=traReq.transfer_request_id  "
					+" INNER JOIN doctor.m_doctor_master doc on traReq.treating_doctor_id=doc.doctor_id   "
					+" LEFT JOIN adt.m_reason_master reason on traReq.reason_id=reason.reason_master_id "
					+ "INNER JOIN adt.m_icu_type_master icu on traReq.icu_type_id=icu.icu_type_id "
					+" WHERE transfer.transfer_status_id IN (10,3) "
					+" AND traReq.transfer_status_id IN (10,3) "
					+" AND transfer.status='A' "
					+" AND admission.status='A' "
					+ " AND adl.status='A' "
					+ " AND adl.nursing_acceptance = 'Y' "
					+" AND transfer.organization_id=:organizationId "
					+" AND transfer.unit_id=:unitId "
					+ "AND traReq.transfer_type_id=3 ");				
			
			if(transferDto.getIsFromNursingAcceptancePage()!=null && transferDto.getIsFromNursingAcceptancePage()=='Y') {
				
				if(transferDto.getWardList()!=null && transferDto.getWardList().size()>0)
				{
					query1.append("AND transfer.from_ward_id IN (:wardList) ");
					
					List<TransferDto> getICUTransferRequestList = sessionFactory.getCurrentSession()
							.createSQLQuery(query1.toString())
							.setParameter("organizationId", transferDto.getOrganizationId())
							.setParameter("unitId", transferDto.getUnitId())
							.setParameterList("wardList", transferDto.getWardList())
							.setFirstResult(transferDto.getOffset() != null ? transferDto.getOffset() : 0)
							.setMaxResults(transferDto.getNoOfRecordsPerPage() != null ? transferDto.getNoOfRecordsPerPage() : 10)
							.setResultTransformer(Transformers.aliasToBean(TransferDto.class)).list();
					return new Response(SUCCESS, null, null, getICUTransferRequestList, null);
				}
				
				if(transferDto.getDoctorId()!=null && transferDto.getDoctorId()>0)
				{
					query1.append("AND traReq.treating_doctor_id=:doctorId ");
					
					List<TransferDto> getICUTransferRequestList = sessionFactory.getCurrentSession()
							.createSQLQuery(query1.toString())
							.setParameter("organizationId", transferDto.getOrganizationId())
							.setParameter("unitId", transferDto.getUnitId())
							.setParameter("doctorId", transferDto.getDoctorId())
							.setFirstResult(transferDto.getOffset() != null ? transferDto.getOffset() : 0)
							.setMaxResults(transferDto.getNoOfRecordsPerPage() != null ? transferDto.getNoOfRecordsPerPage() : 10)
							.setResultTransformer(Transformers.aliasToBean(TransferDto.class)).list();
					return new Response(SUCCESS, null, null, getICUTransferRequestList, null);
				}
				
			}else {
				if(transferDto.getWardList()!=null && transferDto.getWardList().size()>0)
				{
					query1.append("AND transfer.from_ward_id IN (:wardList) ");
					
					List<TransferDto> getICUTransferRequestList = sessionFactory.getCurrentSession()
							.createSQLQuery(query1.toString())
							.setParameter("organizationId", transferDto.getOrganizationId())
							.setParameter("unitId", transferDto.getUnitId())
							.setParameterList("wardList", transferDto.getWardList())
							.setResultTransformer(Transformers.aliasToBean(TransferDto.class)).list();
					return new Response(SUCCESS, null, null, getICUTransferRequestList, null);
				}
				if(transferDto.getAdmissionId()!=null){
					query1.append("AND admission.admission_id =:admissionId ");
				
					List<TransferDto> getICUTransferRequestList = sessionFactory.getCurrentSession()
					.createSQLQuery(query1.toString())
					.setParameter("organizationId", transferDto.getOrganizationId())
					.setParameter("unitId", transferDto.getUnitId())
							.setParameter("admissionId", transferDto.getAdmissionId())
					.setResultTransformer(Transformers.aliasToBean(TransferDto.class)).list();
			return new Response(SUCCESS, null, null, getICUTransferRequestList, null);
				}
				
				if(transferDto.getDoctorId()!=null && transferDto.getDoctorId()>0)
				{
					query1.append("AND traReq.treating_doctor_id=:doctorId ");
					
					List<TransferDto> getICUTransferRequestList = sessionFactory.getCurrentSession()
							.createSQLQuery(query1.toString())
							.setParameter("organizationId", transferDto.getOrganizationId())
							.setParameter("unitId", transferDto.getUnitId())
							.setParameter("doctorId", transferDto.getDoctorId())
							.setResultTransformer(Transformers.aliasToBean(TransferDto.class)).list();
					return new Response(SUCCESS, null, null, getICUTransferRequestList, null);
				}
			}
			
			
			
			
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}


	@Override
	public Response getICUBedsByIUCTypeId(TransferDto transferDto) throws ApplicationException {
		try {
			List<TransferDto> getICUTransferRequestList = sessionFactory.getCurrentSession()

					.getNamedQuery("GET_ICU_BED_LIST_BY_ICU_TYPE_ID")
					.setParameter("organizationId", transferDto.getOrganizationId())
					.setParameter("unitId", transferDto.getUnitId())
					.setParameter("icuTypeId", transferDto.getIcuTypeId())
					.setResultTransformer(Transformers.aliasToBean(BedMasterDto.class)).list();
			return new Response(SUCCESS, null, null, getICUTransferRequestList, null);
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}


	@Override
	public Response getICUTransferRequestListByAdmissionId(TransferDto transferDto) throws ApplicationException {
		try {
			List<TransferDto> getICUTransferRequestList = sessionFactory.getCurrentSession()

					.getNamedQuery("GET_ICU_TRANSFER_REQ_LIST_BY_ADMISSION_ID")
					.setParameter("organizationId", transferDto.getOrganizationId())
					.setParameter("unitId", transferDto.getUnitId())
					.setParameter("admissionId", transferDto.getAdmissionId())
					.setResultTransformer(Transformers.aliasToBean(TransferDto.class)).list();
			return new Response(SUCCESS, null, null, getICUTransferRequestList, null);
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}


	@Override
	public Response getICUTransferRequestListCount(TransferDto transferDto) throws ApplicationException {
		try {
			StringBuilder query1 = new StringBuilder("SELECT transfer.transfer_id as \"transferId\", "
					+" transfer.transfer_request_id as \"transferRequestId\", "
					+ "admission.admission_id as \"admissionId\", "
					+ "adl.admission_details_id as \"admissionDetailsId\", "		
					+ "admission.patient_id as \"patientId\", "
					+ "admission.t_patient_id as \"tPatientId\", "
					+" pr.uhid_number as \"UHIDNumber\", "
					+" concat(coalesce(pr.first_name),' ',coalesce(pr.middle_name),' ',coalesce(pr.last_name)) as \"patientName\",  " 
					+" pr.gender_id as \"genderId\",   "
					+" gm.gender_code as \"genderCode\",  "
					+" to_char(pr.dob,'MM/dd/yyyy') as \"dob\",   "
					+" transfer.from_ward_id as \"fromWardId\", "
					+" ward.ward_name as \"fromWardName\", "
					+" transfer.from_bed_id as \"fromBedId\", " 
					+" bm.bed_number as \"fromBedNumber\", "
					+" transfer.from_bed_category_id as \"fromBedCategoryId\", "
					+" bcm1.bed_category_desc as \"fromBedCategoryDesc\", "
					+" bm.floor_id as \"fromFloorId\", "
					+" bm.room_id as \"fromRoomId\", "
					+" transfer.to_ward_id as \"toWardId\", "
					+" ward2.ward_name as \"toWardName\", "
					+" transfer.to_bed_id as \"toBedId\", " 
					+" bm2.bed_number as \"toBedNumber\", "
					+" transfer.to_bed_category_id as \"toBedCategoryId\", "
					+" bcm2.bed_category_desc as \"toBedCategoryDesc\", "
					+" bm2.floor_id as \"toFloorId\", "
					+" bm2.room_id as \"toRoomId\", "
					+" traReq.treating_doctor_id as \"doctorId\", "
					+" concat(coalesce(doc.first_name),' ',coalesce(doc.middle_name),' ',coalesce(doc.last_name)) as \"doctorName\", "
					+" traReq.reason_id as \"reasonId\",  "
					+" reason.reason_desc as \"reasonDesc\", "
					+" traReq.note as \"note\", "
					+ "traReq.icu_type_id as \"icuTypeId\", "
					+ "icu.icu_type as \"icuType\", "
					+ "traReq.transfer_status_id as \"transferStatusId\", "
					+" traReq.final_note as \"finalNote\", "
					+ "bm.billing_bed_category_id as \"fromBillingBedCategoryId\" "
					//+ "bm2.billing_bed_category_id as \"toBillingBedCategoryId\" "
					+" FROM adt.t_transfer transfer "
					+" INNER JOIN adt.t_admission admission on admission.admission_id=transfer.admission_id "
					+ "INNER JOIN adt.t_admission_details adl on admission.admission_id=adl.admission_id "
					+" INNER JOIN patient.t_patient_registration pr on admission.patient_id=pr.patient_id  "
					+" INNER JOIN public.m_gender_master gm on pr.gender_id=gm.gender_id   "
					+" INNER JOIN adt.m_bed_master bm on transfer.from_bed_id=bm.bed_id   "
					+" LEFT JOIN adt.m_bed_master bm2 on transfer.to_bed_id=bm2.bed_id   "
					+" INNER JOIN adt.m_floor_master floor on bm.floor_id=floor.floor_id  "
					+" INNER JOIN adt.m_bed_category_master bcm1 on transfer.from_bed_category_id=bcm1.bed_category_id  "
					+" LEFT JOIN adt.m_bed_category_master bcm2 on transfer.to_bed_category_id=bcm2.bed_category_id  "
					+" INNER JOIN adt.m_ward_master ward on transfer.from_ward_id=ward.ward_id "
					+" LEFT JOIN adt.m_ward_master ward2 on transfer.to_ward_id=ward2.ward_id "
					+" INNER JOIN adt.t_transfer_request traReq on transfer.transfer_request_id=traReq.transfer_request_id  "
					+" INNER JOIN doctor.m_doctor_master doc on traReq.treating_doctor_id=doc.doctor_id   "
					+" INNER JOIN adt.m_reason_master reason on traReq.reason_id=reason.reason_master_id "
					+ "INNER JOIN adt.m_icu_type_master icu on traReq.icu_type_id=icu.icu_type_id "
					+" WHERE transfer.transfer_status_id IN (10,3) "
					+" AND traReq.transfer_status_id IN (10,3) "
					+" AND transfer.status='A' "
					+" AND admission.status='A' "
					+ " AND adl.status='A' "
					+ " AND adl.nursing_acceptance = 'Y' "
					+" AND transfer.organization_id=:organizationId "
					+" AND transfer.unit_id=:unitId "
					+ "AND traReq.transfer_type_id=3 ");
					
			StringBuilder query2 = new StringBuilder(" SELECT transfer.transfer_id as \"transferId\", "
					+" transfer.transfer_request_id as \"transferRequestId\", "
					+ "admission.admission_id as \"admissionId\", "	
					+ "adl.admission_details_id as \"admissionDetailsId\", "	
					+ "admission.patient_id as \"patientId\", "
					+ "admission.t_patient_id as \"tPatientId\", "
					+" pr.t_uhid as \"UHIDNumber\", "
					+" pr.patient_name as \"patientName\",  " 
					+" pr.gender_id as \"genderId\",   "
					+" gm.gender_code as \"genderCode\",  "
					+" concat(to_char(pr.age,'999'),' ', pr.age_format) as \"dob\",  " 
					+" transfer.from_ward_id as \"fromWardId\", "
					+" ward.ward_name as \"fromWardName\", "
					+" transfer.from_bed_id as \"fromBedId\", " 
					+" bm.bed_number as \"fromBedNumber\", "
					+" transfer.from_bed_category_id as \"fromBedCategoryId\", "
					+" bcm1.bed_category_desc as \"fromBedCategoryDesc\", "
					+" bm.floor_id as \"fromFloorId\", "
					+" bm.room_id as \"fromRoomId\", "
					+" transfer.to_ward_id as \"toWardId\", "
					+" ward2.ward_name as \"toWardName\", "
					+" transfer.to_bed_id as \"toBedId\", " 
					+" bm2.bed_number as \"toBedNumber\", "
					+" transfer.to_bed_category_id as \"toBedCategoryId\", "
					+" bcm2.bed_category_desc as \"toBedCategoryDesc\", "
					+" bm2.floor_id as \"toFloorId\", "
					+" bm2.room_id as \"toRoomId\", "
					+" traReq.treating_doctor_id as \"doctorId\", "
					+" concat(coalesce(doc.first_name),' ',coalesce(doc.middle_name),' ',coalesce(doc.last_name)) as \"doctorName\", "
					+" traReq.reason_id as \"reasonId\",  "
					+" reason.reason_desc as \"reasonDesc\", "
					+" traReq.note as \"note\", "
					+ "traReq.icu_type_id as \"icuTypeId\", "
					+ "icu.icu_type as \"icuType\", "
					+ "traReq.transfer_status_id as \"transferStatusId\", "
					+" traReq.final_note as \"finalNote\", "
					+ "bm.billing_bed_category_id as \"fromBillingBedCategoryId\" "
					//+ "bm2.billing_bed_category_id as \"toBillingBedCategoryId\" "
					+" FROM adt.t_transfer transfer "
					+" INNER JOIN adt.t_admission admission on admission.admission_id=transfer.admission_id  " 
					+ "INNER JOIN adt.t_admission_details adl on admission.admission_id=adl.admission_id "
					+" INNER JOIN patient.m_unknown_patient_registration pr on admission.t_patient_id=pr.unknown_patient_id  "
					+" INNER JOIN public.m_gender_master gm on pr.gender_id=gm.gender_id   "
					+" INNER JOIN adt.m_bed_master bm on transfer.from_bed_id=bm.bed_id   "
					+" LEFT JOIN adt.m_bed_master bm2 on transfer.to_bed_id=bm2.bed_id   "
					+" INNER JOIN adt.m_floor_master floor on bm.floor_id=floor.floor_id  "
					+" INNER JOIN adt.m_bed_category_master bcm1 on transfer.from_bed_category_id=bcm1.bed_category_id  "
					+" LEFT JOIN adt.m_bed_category_master bcm2 on transfer.to_bed_category_id=bcm2.bed_category_id  "
					+" INNER JOIN adt.m_ward_master ward on transfer.from_ward_id=ward.ward_id "
					+" LEFT JOIN adt.m_ward_master ward2 on transfer.to_ward_id=ward2.ward_id "
					+" INNER JOIN adt.t_transfer_request traReq on transfer.transfer_request_id=traReq.transfer_request_id  "
					+" INNER JOIN doctor.m_doctor_master doc on traReq.treating_doctor_id=doc.doctor_id   "
					+" INNER JOIN adt.m_reason_master reason on traReq.reason_id=reason.reason_master_id "
					+ "INNER JOIN adt.m_icu_type_master icu on traReq.icu_type_id=icu.icu_type_id "
					+" WHERE transfer.transfer_status_id IN (10,3) "
					+" AND traReq.transfer_status_id IN (10,3) "
					+" AND transfer.status='A' "
					+" AND admission.status='A' "
					+ " AND adl.status='A' "
					+ " AND adl.nursing_acceptance = 'Y' "
					+" AND transfer.organization_id=:organizationId "
					+" AND transfer.unit_id=:unitId "
					+ "AND traReq.transfer_type_id=3 ");	
			
			
			if(transferDto.getIsFromNursingAcceptancePage()!=null && transferDto.getIsFromNursingAcceptancePage()=='Y') {
				
				if(transferDto.getWardList()!=null && transferDto.getWardList().size()>0)
				{
					query1.append("AND transfer.from_ward_id IN (:wardList) ");
					query2.append("AND transfer.from_ward_id IN (:wardList) ");
					
					List<TransferDto> getICUTransferRequestList = sessionFactory.getCurrentSession()
							.createSQLQuery(query1.append(" union ").append(query2).toString())
							.setParameter("organizationId", transferDto.getOrganizationId())
							.setParameter("unitId", transferDto.getUnitId())
							.setParameterList("wardList", transferDto.getWardList())
							//.setFirstResult(transferDto.getOffset() != null ? transferDto.getOffset() : 0)
							//.setMaxResults(transferDto.getNoOfRecordsPerPage() != null ? transferDto.getNoOfRecordsPerPage() : 10)
							.setResultTransformer(Transformers.aliasToBean(TransferDto.class)).list();
					
					Integer count=getICUTransferRequestList!=null && getICUTransferRequestList.size()>0 ? getICUTransferRequestList.size() : 0;
					return new Response(SUCCESS, null, null, null, count);
				}
				
				if(transferDto.getDoctorId()!=null && transferDto.getDoctorId()>0)
				{
					query1.append("AND traReq.treating_doctor_id=:doctorId ");
					query2.append("AND traReq.treating_doctor_id=:doctorId ");
					
					List<TransferDto> getICUTransferRequestList = sessionFactory.getCurrentSession()
							.createSQLQuery(query1.append(" union ").append(query2).toString())
							.setParameter("organizationId", transferDto.getOrganizationId())
							.setParameter("unitId", transferDto.getUnitId())
							.setParameter("doctorId", transferDto.getDoctorId())
							//.setFirstResult(transferDto.getOffset() != null ? transferDto.getOffset() : 0)
							//.setMaxResults(transferDto.getNoOfRecordsPerPage() != null ? transferDto.getNoOfRecordsPerPage() : 10)
							.setResultTransformer(Transformers.aliasToBean(TransferDto.class)).list();
					Integer count=getICUTransferRequestList!=null && getICUTransferRequestList.size()>0 ? getICUTransferRequestList.size() : 0;
					return new Response(SUCCESS, null, null, null, count);
				}
				
			}

			
			
			
			
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}


	@Override
	public Response rejectTransferRequestForICUTransfer(TransferDto transferDto) throws ApplicationException {

		try {
			
			
			
			//<-----In-activating previous TransferRequest entry if any
			Query qu = sessionFactory.getCurrentSession().createQuery(
					"update TransferRequest traReq set " +
							" traReq.status = :status, " +
							" traReq.icuRejectionNote = :icuRejectionNote, " +
							" traReq.icuRejectReasonId = :icuRejectReasonId, " +
							" traReq.updatedBy = :updatedBy, " +
							" traReq.updatedDate = :updatedDate " +
							" WHERE traReq.admissionId = :admissionId " +
							" AND traReq.unitId = :unitId " +
							" AND traReq.organizationId = :organizationId");
			qu.setParameter("status", 'I');
			qu.setParameter("icuRejectionNote", transferDto.getIcuRejectionNote());
			qu.setParameter("icuRejectReasonId", transferDto.getIcuRejectReasonId());
			qu.setParameter("updatedBy", transferDto.getUpdatedBy());
			qu.setParameter("updatedDate", ADTCommonDateUtils.getDate(
					TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),
					"dd-MM-yyyy HH:mm:ss"));
			qu.setParameter("admissionId", transferDto.getAdmissionId());
			qu.setParameter("unitId", transferDto.getUnitId());
			qu.setParameter("organizationId", transferDto.getOrganizationId());

			qu.executeUpdate();
			//----->
			
			
			//Session session=sessionFactory.getCurrentSession();
			//<-----In-activating previous TransferRequest entry if any
			Query q=sessionFactory.getCurrentSession().createQuery("update Transfer traReq set"
					+" traReq.status=:status, "
					+ " traReq.updatedBy=:updatedBy, "
					+ " traReq.updatedDate=:updatedDate "
					+ " WHERE traReq.transferId=:transferId "
					+ " AND traReq.unitId=:unitId "
					+ " AND traReq.organizationId=:organizationId ");
			q.setParameter("status", 'I');
			q.setParameter("updatedBy", transferDto.getUpdatedBy());
			q.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0), "dd-MM-yyyy HH:mm:ss"));
			q.setParameter("transferId", transferDto.getTransferId());
			q.setParameter("unitId", transferDto.getUnitId());
			q.setParameter("organizationId", transferDto.getOrganizationId());

			q.executeUpdate();
			
			Query que=sessionFactory.getCurrentSession().createQuery("update Transfer traReq set"
					+" traReq.status=:status, "
					+ " traReq.updatedBy=:updatedBy, "
					+ " traReq.updatedDate=:updatedDate "
					+ " WHERE traReq.transferId=:transferId "
					+ " AND traReq.unitId=:unitId "
					+ " AND traReq.organizationId=:organizationId ");
			que.setParameter("status", 'A');
			que.setParameter("updatedBy", transferDto.getUpdatedBy());
			que.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0), "dd-MM-yyyy HH:mm:ss"));
			que.setParameter("transferId", transferDto.getOldTransferId());
			que.setParameter("unitId", transferDto.getUnitId());
			que.setParameter("organizationId", transferDto.getOrganizationId());
			que.executeUpdate();
			
			//----->
			
			return new Response(SUCCESS, null, null, null, null);
		} catch (HibernateException he) {
			he.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new Response(ERROR, null, null, null, null);
	}


	@Override
	public Response universalSearchForICUTransferRequestList(TransferDto transferDto) throws ApplicationException {
		try {
			StringBuilder query1 = new StringBuilder("SELECT transfer.transfer_id as \"transferId\", "
					+" transfer.old_transfer_id as \"oldTransferId\", "
					+" transfer.transfer_request_id as \"transferRequestId\", "
					+ "admission.admission_id as \"admissionId\", "
					+ "adl.admission_details_id as \"admissionDetailsId\", "		
					+ "admission.patient_id as \"patientId\", "
					+ "admission.t_patient_id as \"tPatientId\", "
					+" pr.uhid_number as \"UHIDNumber\", "
					+" concat(coalesce(pr.first_name),' ',coalesce(pr.middle_name),' ',coalesce(pr.last_name)) as \"patientName\",  " 
					+" pr.gender_id as \"genderId\",   "
					+" gm.gender_code as \"genderCode\",  "
					+" to_char(pr.dob,'MM/dd/yyyy') as \"dob\",   "
					+" transfer.from_ward_id as \"fromWardId\", "
					+" ward.ward_name as \"fromWardName\", "
					+" transfer.from_bed_id as \"fromBedId\", " 
					+" bm.bed_number as \"fromBedNumber\", "
					+" transfer.from_bed_category_id as \"fromBedCategoryId\", "
					+" bcm1.bed_category_desc as \"fromBedCategoryDesc\", "
					+" bm.floor_id as \"fromFloorId\", "
					+" bm.room_id as \"fromRoomId\", "
					+" transfer.to_ward_id as \"toWardId\", "
					+" ward2.ward_name as \"toWardName\", "
					+" transfer.to_bed_id as \"toBedId\", " 
					+" bm2.bed_number as \"toBedNumber\", "
					+" transfer.to_bed_category_id as \"toBedCategoryId\", "
					+" bcm2.bed_category_desc as \"toBedCategoryDesc\", "
					+" bm2.floor_id as \"toFloorId\", "
					+" bm2.room_id as \"toRoomId\", "
					+" traReq.treating_doctor_id as \"doctorId\", "
					+" concat(coalesce(doc.first_name),' ',coalesce(doc.middle_name),' ',coalesce(doc.last_name)) as \"doctorName\", "
					+" traReq.reason_id as \"reasonId\",  "
					+" reason.reason_desc as \"reasonDesc\", "
					+" traReq.note as \"note\", "
					+ "traReq.icu_type_id as \"icuTypeId\", "
					+ "icu.icu_type as \"icuType\", "
					+ "traReq.transfer_status_id as \"transferStatusId\", "
		            + "to_char(traReq.created_date,'DD-MM-YYYY HH24:MI:ss') as \"createdDate\","
					+" traReq.final_note as \"finalNote\", "
					+ "bm.billing_bed_category_id as \"fromBillingBedCategoryId\" ,"
					+ " concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, pr.dob)) ,'Y ', EXTRACT(month from AGE(CURRENT_TIMESTAMP, pr.dob)) , 'M ' , EXTRACT(day from AGE(CURRENT_TIMESTAMP, pr.dob)),'D ') as \"ageStr\" ,"
		            + " prefxmstr.prefix_code as \"prefixDesc\",pr.image_path as \"imagePath\"  "
					+" FROM adt.t_transfer transfer "
					+" INNER JOIN adt.t_admission admission on admission.admission_id=transfer.admission_id "
					+ "INNER JOIN adt.t_admission_details adl on admission.admission_id=adl.admission_id "
					+" INNER JOIN patient.t_patient_registration pr on admission.patient_id=pr.patient_id  "
		            + "left join public.m_prefix_master prefxmstr on prefxmstr.prefix_id = pr.prefix_id "
					+" INNER JOIN public.m_gender_master gm on pr.gender_id=gm.gender_id   "
					+" INNER JOIN adt.m_bed_master bm on transfer.from_bed_id=bm.bed_id   "
					+" LEFT JOIN adt.m_bed_master bm2 on transfer.to_bed_id=bm2.bed_id   "
					+" INNER JOIN adt.m_floor_master floor on bm.floor_id=floor.floor_id  "
					+" INNER JOIN adt.m_bed_category_master bcm1 on transfer.from_bed_category_id=bcm1.bed_category_id  "
					+" LEFT JOIN adt.m_bed_category_master bcm2 on transfer.to_bed_category_id=bcm2.bed_category_id  "
					+" INNER JOIN adt.m_ward_master ward on transfer.from_ward_id=ward.ward_id "
					+" LEFT JOIN adt.m_ward_master ward2 on transfer.to_ward_id=ward2.ward_id "
					+" INNER JOIN adt.t_transfer_request traReq on transfer.transfer_request_id=traReq.transfer_request_id  "
					+" INNER JOIN doctor.m_doctor_master doc on traReq.treating_doctor_id=doc.doctor_id   "
					+" LEFT JOIN adt.m_reason_master reason on traReq.reason_id=reason.reason_master_id "
					+ "INNER JOIN adt.m_icu_type_master icu on traReq.icu_type_id=icu.icu_type_id "
					+" WHERE transfer.transfer_status_id IN (10,3) "
					+" AND traReq.transfer_status_id IN (10,3) "
					+" AND transfer.status='A' "
					+" AND admission.status='A' "
					+ " AND adl.status='A' "
					+ " AND adl.nursing_acceptance = 'Y' "
					+" AND transfer.organization_id=:organizationId "
					+" AND transfer.unit_id=:unitId "
					+ "AND traReq.transfer_type_id=3 "
					+ "  AND (lower(pr.uhid_number) like :patientName "
		            + "  OR lower(pr.first_name) like :patientName   OR lower(pr.middle_name) like :patientName OR lower(pr.last_name) like :patientName "
		            + "  OR lower(doc.first_name) like :patientName       OR lower(doc.middle_name) like :patientName OR lower(doc.last_name) like :patientName "
		            + "  OR lower(bm.bed_number) like :patientName "
		            + "  OR lower(bm2.bed_number) like :patientName "
		            + "  OR lower(ward.ward_name) like :patientName "
		            + "  OR lower(ward2.ward_name) like :patientName "
		            + "  OR lower(bcm1.bed_category_desc) like :patientName "
		            + "  OR lower(bcm2.bed_category_desc) like :patientName "
		            + "  OR lower(reason.reason_desc) like :patientName )");	
					
			
			
			if(transferDto.getIsFromNursingAcceptancePage()!=null && transferDto.getIsFromNursingAcceptancePage()=='Y') {
				
				if(transferDto.getWardList()!=null && transferDto.getWardList().size()>0)
				{
					query1.append("AND transfer.from_ward_id IN (:wardList) ");
					
					List<TransferDto> getICUTransferRequestList = sessionFactory.getCurrentSession()
							.createSQLQuery(query1.toString())
							.setParameter("organizationId", transferDto.getOrganizationId())
							.setParameter("unitId", transferDto.getUnitId())
							.setParameterList("wardList", transferDto.getWardList())
	                        .setParameter("patientName", "%"+transferDto.getPatientName().trim().toLowerCase()+"%")
							.setFirstResult(transferDto.getOffset() != null ? transferDto.getOffset() : 0)
							.setMaxResults(transferDto.getNoOfRecordsPerPage() != null ? transferDto.getNoOfRecordsPerPage() : 10)
							.setResultTransformer(Transformers.aliasToBean(TransferDto.class)).list();
					return new Response(SUCCESS, null, null, getICUTransferRequestList, null);
				}
				
				if(transferDto.getDoctorId()!=null && transferDto.getDoctorId()>0)
				{
					query1.append("AND traReq.treating_doctor_id=:doctorId ");
					
					List<TransferDto> getICUTransferRequestList = sessionFactory.getCurrentSession()
							.createSQLQuery(query1.toString())
							.setParameter("organizationId", transferDto.getOrganizationId())
							.setParameter("unitId", transferDto.getUnitId())
	                        .setParameter("patientName", "%"+transferDto.getPatientName().trim().toLowerCase()+"%")
							.setParameter("doctorId", transferDto.getDoctorId())
							.setFirstResult(transferDto.getOffset() != null ? transferDto.getOffset() : 0)
							.setMaxResults(transferDto.getNoOfRecordsPerPage() != null ? transferDto.getNoOfRecordsPerPage() : 10)
							.setResultTransformer(Transformers.aliasToBean(TransferDto.class)).list();
					return new Response(SUCCESS, null, null, getICUTransferRequestList, null);
				}
				
			}
			
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

}
