package com.param.billing.ipd.dao;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.param.billing.global.smartcard.dto.ItemDetailsResponseDto;
import com.param.billing.global.transaction.dto.MPreauthRequestMasterDto;
import com.param.billing.global.transaction.dto.TDoctorShareRespDto;
import com.param.billing.global.transaction.model.MPreauthMaster;
import com.param.billing.global.transaction.model.MPreauthRequestMaster;
import com.param.billing.ipd.dto.PreAuthorizationRequestDto;
import com.param.global.billing.controller.BillingException;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.opd.encounter.dto.PatientDocReqDto;
import com.param.utility.TimeZoneComponent;
import com.param.global.service.VerifiedIdentityService;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class IPreAuthorizationRequestDaoImpl extends GenericDao<MPreauthRequestMaster, Integer>  implements IPreAuthorizationRequest, ICommonConstants{

	public IPreAuthorizationRequestDaoImpl() {
		super(MPreauthRequestMaster.class);
	}



	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private VerifiedIdentityService verifiedIdentityService;
	

	@Override
	public Response getPreAuthorizationRequestList(PreAuthorizationRequestDto reqDto)
			throws BillingException {
   try{
	   StringBuilder query1 = new StringBuilder ("select"
				+ "   adm.admission_id as \"admissionId\","
				+ "   adm.visit_type_id as \"visitTypeId\","
				+ "   visitmst.visit_type_code as \"visitTypeCode\","
				+ "   pem.payment_entitlement_desc as \"paymentEntitlementDesc\","
				+ "   patSp.payment_entitlement_id as \"paymentEntitlementId\","
				+ "   to_char(admdt.doa,'DD/MM/YYYY')as \"visitDate\","
				+ "   adm.admission_number as \"admissionNo\","
				+ "   concat(coalesce(pre.prefix_code,''),' ',pat.first_name,' ', pat.middle_name,' ',pat.last_name) as \"patientName\","
				+ "   pat.patient_id AS \"patientId\","
				+ "   pat.image_path AS \"imagePath\","
				+ "   gender.gender_id AS \"genderId\","
				+ "   to_char(pat.dob,'DD-MM-YYYY HH:mm:ss') as \"dob\","
				+ "   pat.uhid_number as \"uhidNumber\", "
				+ "   patSp.patient_sponsor_details_id AS \"patientSponsorDetailsId\","	
				+ "   patSp.payee_id AS \"payeeId\","
				+ "   patSp.contract_id as \"contractId\","
				+ "   patSp.insurance_number as \"insuranceNumber\","
				+ "   to_char(patSp.valid_from,'DD/MM/YYYY') as \"validFrom\","
				+ "   to_char(patSp.valid_to,'DD/MM/YYYY')as \"validTo\","
				+ "   patSp.valid_from as \"validFromNew\","
				+ "   patSp.valid_to as \"validToNew\","
				+ "   comp.company_desc as \"companyDesc\","
				+ "   comp.company_type_id  as \"companyTypeId\","
				+ "   mccm.contract_name as \"contractName\","
				+ "   gender.gender_code as \"genderCode\", "
				+ "   gender.gender_name as \"genderDec\", "
				+ "	  ti.insurance_desc as \"insuranceDesc\","
				+ "   to_char(admdt.pdd,'DD/MM/YYYY') as \"dischargeDate\","
				+ "   concat(extract(year from age(current_timestamp, pat.dob)) , 'Y ' , extract(month from age(current_timestamp, pat.dob)), 'M ') as \"age\", " 
				+ "   concat(coalesce(doc.first_name, ''), ' ', coalesce(doc.middle_name, ''), ' ', coalesce(doc.last_name, '')) as \"doctorName\", "
				+ "   mbm.bed_number as \"bedNo\" "
				+ " from"
				+ "   adt.t_admission adm "
				+ "   inner join adt.t_admission_details admdt on admdt.admission_id = adm.admission_id and admdt.status = 'A' "
				+ "   inner join patient.t_patient_registration pat on pat.patient_id = adm.patient_id " 
				+ "   left join m_prefix_master pre on	pre.prefix_id = pat.prefix_id "
				+ "   inner join opd.t_patient_sponsor_details patSp on patSp.admission_id = adm.admission_id "
				+ "  and patSp.payee_id <> (select config.self_payee_id from public.t_unit_application_billing_configuration config where config.unit_id  =adm.unit_id) and patSp.status='A' "
				+ "   left join public.t_company_master comp on comp.company_id = patSp.payee_id"
				+ "   inner join public.m_visit_type_master visitmst on visitmst.visit_type_id = adm.visit_type_id"
				+ "   inner join public.m_payment_entitlement_master pem on pem.payment_entitlement_id = patSp.payment_entitlement_id"
				+ "   left join service.m_company_contract_master mccm on mccm.contract_id = patSp.contract_id "
				+ "   left join public.m_gender_master gender on gender.gender_id= pat.gender_id "
				+ "	  left join adt.t_insurance ti on ti.insurance_id=patSp.insurance_id "
				+ "   inner join adt.m_bed_master mbm  on mbm.bed_id=admdt.bed_id "
				+ "   left join doctor.m_doctor_master doc on doc.doctor_id = adm.doctor_id "
				+ "	  where adm.unit_id=:unitId	");
				
	   StringBuilder query2 = new StringBuilder( " select"
				+ "   enmst.encounter_id as \"admissionId\","
				+ "   1 as visit_type_id ,"
				+ "   visitmst.visit_type_code as \"visitTypeCode\","
				+ "   pem.payment_entitlement_desc as \"paymentEntitlementDesc\","
				+ "   patSp.payment_entitlement_id as \"paymentEntitlementId\","
				+ "   to_char(enmst.encounter_date,'DD/MM/YYYY') as \"visitDate\","
				+ "   enmst.encounter_number as \"admissionNo\","
				+ "   concat("
				+ "     coalesce(pre.prefix_code,''),' ',pat.first_name,"
				+ "     ' ',"
				+ "     pat.middle_name,"
				+ "     ' ',"
				+ "     pat.last_name"
				+ "   ) as \"patientName\","
				+ "   pat.patient_id AS \"patientId\","
				+ "   pat.image_path AS \"imagePath\","
				+ "   gender.gender_id AS \"genderId\","
				+ "   to_char(pat.dob,'DD-MM-YYYY HH:mm:ss') as \"dob\","
				+ "   pat.uhid_number as \"uhidNumber\", "
				+ "   patSp.patient_sponsor_details_id AS \"patientSponsorDetailsId\","	
				+ "   patSp.payee_id AS \"payeeId\","
				+ "   patSp.contract_id as \"contractId\","
				+ "   patSp.insurance_number as \"insuranceNumber\","
				+ "   to_char(patSp.valid_from,'DD/MM/YYYY') as \"validFrom\","
				+ "   to_char(patSp.valid_to,'DD/MM/YYYY')as \"validTo\","
				+ "   patSp.valid_from as \"validFromNew\","
				+ "   patSp.valid_to as \"validToNew\","
				+ "   comp.company_desc as \"companyDesc\","
				+ "   comp.company_type_id  as \"companyTypeId\","
				+ "   mccm.contract_name as \"contractName\","
				+ "    gender.gender_code as \"genderCode\", "
				+ "    gender.gender_name as \"genderDec\", "
				+ "    ti.insurance_desc as \"insuranceDesc\","
				+ "   '' as \"dischargeDate\","
				+ "   concat(extract(year from age(current_timestamp, pat.dob)) , 'Y ' , extract(month from age(current_timestamp, pat.dob)), 'M ') as \"age\", " 
				+ "   concat(coalesce(doc.first_name, ''), ' ', coalesce(doc.middle_name, ''), ' ', coalesce(doc.last_name, '')) as \"doctorName\", "
				+ "   '' as \"bedNo\" "
				+ " from"
				+ "   opd.t_encounter_master enmst"
				+ "   inner join patient.t_patient_registration pat on pat.patient_id = enmst.patient_id"
				+ "   left join m_prefix_master pre on	pre.prefix_id = pat.prefix_id "
				+ "   inner join opd.t_patient_sponsor_details patSp on patSp.encounter_id = enmst.encounter_id and patSp.status='A' "
				+ "   left join public.t_company_master comp on comp.company_id = patSp.payee_id"
				+ "   inner join public.m_visit_type_master visitmst on visitmst.visit_type_id = 1 "
				+ "   inner join public.m_payment_entitlement_master pem on pem.payment_entitlement_id = patSp.payment_entitlement_id"
				+ "   left join service.m_company_contract_master mccm on mccm.contract_id = patSp.contract_id "
				+ "   left join public.m_gender_master gender on gender.gender_id= pat.gender_id "
				+ "	  left join adt.t_insurance ti on ti.insurance_id=patSp.insurance_id "
				+ "   left join doctor.m_doctor_master doc on doc.doctor_id = enmst.doctor_id "
				+ "	  where enmst.unit_id=:unitId "
				+ "   and enmst.status_id in (5,6,8) ");
	   
	   if(reqDto.getPaymentEntitlementId()!=null)
	   {
		   query1.append(" and pem.payment_entitlement_id =:paymentEntitlementId ");
		   query2.append(" and pem.payment_entitlement_id =:paymentEntitlementId ");
	   }
	   if(reqDto.getVisitTypeId()!=null)
	   {
		   query1.append(" and visitmst.visit_type_id =:visitTypeId ");
		   query2.append(" and visitmst.visit_type_id =:visitTypeId ");
	   }
	   if(reqDto.getCompanyId()!=null)
	   {
		   query1.append(" and comp.company_id =:companyId ");
		   query2.append(" and comp.company_id =:companyId");
	   }  
	   if(reqDto.getPatientId()!=null)
	   {
		   query1.append(" and pat.patient_id =:patientId ");
		   query2.append(" and pat.patient_id =:patientId ");
	   }
	   if(reqDto.getAdmissionNo()!=null && reqDto.getAdmissionNo().length()>0)
	   {
		   query1.append(" and lower(adm.admission_number) like :admissionNo " );
		   query2.append(" and lower(enmst.encounter_number) like :admissionNo ");
	   }
	   
	   StringBuilder query = query1.append(" union all ").append(query2);
	   		query.append(" ORDER BY \"visitDate\" desc ");
	   Query sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
			   .setParameter("unitId", reqDto.getUnitId());

	   if(reqDto.getPaymentEntitlementId()!=null)
		   sqlQuery.setParameter("paymentEntitlementId", reqDto.getPaymentEntitlementId());

	   if(reqDto.getVisitTypeId()!=null)
		   sqlQuery.setParameter("visitTypeId", reqDto.getVisitTypeId());

	   if(reqDto.getCompanyId()!=null)
		   sqlQuery.setParameter("companyId", reqDto.getCompanyId());

	   if(reqDto.getPatientId()!=null)
		   sqlQuery.setParameter("patientId", reqDto.getPatientId());

	   if(reqDto.getAdmissionNo()!=null && !reqDto.getAdmissionNo().isEmpty())
		   sqlQuery.setParameter("admissionNo", "%" + reqDto.getAdmissionNo().toLowerCase() + "%");

	   List<PreAuthorizationRequestDto> listPreAuthorizationRequestDto = sqlQuery
			   .setFirstResult(reqDto.getOffset() != null ? reqDto.getOffset() : 0)
			   .setMaxResults(reqDto.getRecordPerPage() != null ? reqDto.getRecordPerPage() : 10)
			   .setResultTransformer(Transformers.aliasToBean(PreAuthorizationRequestDto.class)).list();
	 
		
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS, listPreAuthorizationRequestDto, listPreAuthorizationRequestDto.size());
		}catch(Exception e){
			
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
}



	@Override
	public Response getPreAuthorizationRequestListCount(PreAuthorizationRequestDto reqDto)
			throws BillingException {
		 try{
			   
			 StringBuilder query1 = new StringBuilder ("select"
						+ "   adm.admission_id as \"admissionId\","
						+ "   adm.visit_type_id as \"visitTypeId\","
						+ "   visitmst.visit_type_code as \"visitTypeCode\","
						+ "   pem.payment_entitlement_desc as \"paymentEntitlementDesc\","
						+ "   patSp.payment_entitlement_id as \"paymentEntitlementId\","
						+ "   to_char(admdt.doa,'DD/MM/YYYY')as \"visitDate\","
						+ "   adm.admission_number as \"admissionNo\","
						+ "   concat(coalesce(pre.prefix_code,''),' ',pat.first_name,' ', pat.middle_name,' ',pat.last_name) as \"patientName\","
						+ "   pat.patient_id AS \"patientId\","
						+ "   pat.image_path AS \"imagePath\","
						+ "   gender.gender_id AS \"genderId\","
						+ "   to_char(pat.dob,'DD-MM-YYYY HH:mm:ss') as \"dob\","
						+ "   pat.uhid_number as \"uhidNumber\", "
						+ "   patSp.patient_sponsor_details_id AS \"patientSponsorDetailsId\","	
						+ "   patSp.payee_id AS \"payeeId\","
						+ "   patSp.contract_id as \"contractId\","
						+ "   patSp.insurance_number as \"insuranceNumber\","
						+ "   to_char(patSp.valid_from,'DD/MM/YYYY') as \"validFrom\","
						+ "   to_char(patSp.valid_to,'DD/MM/YYYY')as \"validTo\","
						+ "   patSp.valid_from as \"validFromNew\","
						+ "   patSp.valid_to as \"validToNew\","
						+ "   comp.company_desc as \"companyDesc\","
						+ "   comp.company_type_id  as \"companyTypeId\","
						+ "   mccm.contract_name as \"contractName\","
						+ "   gender.gender_code as \"genderCode\", "
						+ "   gender.gender_name as \"genderDec\", "
						+ "	  ti.insurance_desc as \"insuranceDesc\","
						+ "   to_char(admdt.pdd,'DD/MM/YYYY') as \"dischargeDate\","
						+ "   concat(extract(year from age(current_timestamp, pat.dob)) , 'Y ' , extract(month from age(current_timestamp, pat.dob)), 'M ') as \"age\", " 
						+ "   concat(coalesce(doc.first_name, ''), ' ', coalesce(doc.middle_name, ''), ' ', coalesce(doc.last_name, '')) as \"doctorName\", "
						+ "   mbm.bed_number as \"bedNo\" "
						+ " from"
						+ "   adt.t_admission adm "
						+ "   inner join adt.t_admission_details admdt on admdt.admission_id = adm.admission_id and admdt.status = 'A' "
						+ "   inner join patient.t_patient_registration pat on pat.patient_id = adm.patient_id " 
						+ "   left join m_prefix_master pre on	pre.prefix_id = pat.prefix_id "
						+ "   inner join opd.t_patient_sponsor_details patSp on patSp.admission_id = adm.admission_id "
						+ "  and patSp.payee_id <> (select config.self_payee_id from public.t_unit_application_billing_configuration config where config.unit_id  =adm.unit_id) and patSp.status='A' "
						+ "   left join public.t_company_master comp on comp.company_id = patSp.payee_id"
						+ "   inner join public.m_visit_type_master visitmst on visitmst.visit_type_id = adm.visit_type_id"
						+ "   inner join public.m_payment_entitlement_master pem on pem.payment_entitlement_id = patSp.payment_entitlement_id"
						+ "   left join service.m_company_contract_master mccm on mccm.contract_id = patSp.contract_id "
						+ "   left join public.m_gender_master gender on gender.gender_id= pat.gender_id "
						+ "	  left join adt.t_insurance ti on ti.insurance_id=patSp.insurance_id "
						+ "   inner join adt.m_bed_master mbm  on mbm.bed_id=admdt.bed_id "
						+ "   left join doctor.m_doctor_master doc on doc.doctor_id = adm.doctor_id "
						+ "	  where adm.unit_id=:unitId	");
						
			   StringBuilder query2 = new StringBuilder( " select"
						+ "   enmst.encounter_id as \"admissionId\","
						+ "   1 as visit_type_id ,"
						+ "   visitmst.visit_type_code as \"visitTypeCode\","
						+ "   pem.payment_entitlement_desc as \"paymentEntitlementDesc\","
						+ "   patSp.payment_entitlement_id as \"paymentEntitlementId\","
						+ "   to_char(enmst.encounter_date,'DD/MM/YYYY') as \"visitDate\","
						+ "   enmst.encounter_number as \"admissionNo\","
						+ "   concat("
						+ "     coalesce(pre.prefix_code,''),' ',pat.first_name,"
						+ "     ' ',"
						+ "     pat.middle_name,"
						+ "     ' ',"
						+ "     pat.last_name"
						+ "   ) as \"patientName\","
						+ "   pat.patient_id AS \"patientId\","
						+ "   pat.image_path AS \"imagePath\","
						+ "   gender.gender_id AS \"genderId\","
						+ "   to_char(pat.dob,'DD-MM-YYYY HH:mm:ss') as \"dob\","
						+ "   pat.uhid_number as \"uhidNumber\", "
						+ "   patSp.patient_sponsor_details_id AS \"patientSponsorDetailsId\","	
						+ "   patSp.payee_id AS \"payeeId\","
						+ "   patSp.contract_id as \"contractId\","
						+ "   patSp.insurance_number as \"insuranceNumber\","
						+ "   to_char(patSp.valid_from,'DD/MM/YYYY') as \"validFrom\","
						+ "   to_char(patSp.valid_to,'DD/MM/YYYY')as \"validTo\","
						+ "   patSp.valid_from as \"validFromNew\","
						+ "   patSp.valid_to as \"validToNew\","
						+ "   comp.company_desc as \"companyDesc\","
						+ "   comp.company_type_id  as \"companyTypeId\","
						+ "   mccm.contract_name as \"contractName\","
						+ "    gender.gender_code as \"genderCode\", "
						+ "    gender.gender_name as \"genderDec\", "
						+ "    ti.insurance_desc as \"insuranceDesc\","
						+ "   '' as \"dischargeDate\","
						+ "   concat(extract(year from age(current_timestamp, pat.dob)) , 'Y ' , extract(month from age(current_timestamp, pat.dob)), 'M ') as \"age\", " 
						+ "   concat(coalesce(doc.first_name, ''), ' ', coalesce(doc.middle_name, ''), ' ', coalesce(doc.last_name, '')) as \"doctorName\", "
						+ "   '' as \"bedNo\" "
						+ " from"
						+ "   opd.t_encounter_master enmst"
						+ "   inner join patient.t_patient_registration pat on pat.patient_id = enmst.patient_id"
						+ "   left join m_prefix_master pre on	pre.prefix_id = pat.prefix_id "
						+ "   inner join opd.t_patient_sponsor_details patSp on patSp.encounter_id = enmst.encounter_id and patSp.status='A' "
						+ "   left join public.t_company_master comp on comp.company_id = patSp.payee_id"
						+ "   inner join public.m_visit_type_master visitmst on visitmst.visit_type_id = 1 "
						+ "   inner join public.m_payment_entitlement_master pem on pem.payment_entitlement_id = patSp.payment_entitlement_id"
						+ "   left join service.m_company_contract_master mccm on mccm.contract_id = patSp.contract_id "
						+ "   left join public.m_gender_master gender on gender.gender_id= pat.gender_id "
						+ "	  left join adt.t_insurance ti on ti.insurance_id=patSp.insurance_id "
						+ "   left join doctor.m_doctor_master doc on doc.doctor_id = enmst.doctor_id "
						+ "	  where enmst.unit_id=:unitId "
						+ "   and enmst.status_id in (5,6,8) ");
			  
			   
			   if(reqDto.getPaymentEntitlementId()!=null)
			   {
				   query1.append(" and pem.payment_entitlement_id =:paymentEntitlementId ");
				   query2.append(" and pem.payment_entitlement_id =:paymentEntitlementId ");
			   }
			   if(reqDto.getVisitTypeId()!=null)
			   {
				   query1.append(" and visitmst.visit_type_id =:visitTypeId ");
				   query2.append(" and visitmst.visit_type_id =:visitTypeId ");
			   }   
			   if(reqDto.getCompanyId()!=null)
			   {
				   query1.append(" and comp.company_id =:companyId ");
				   query2.append(" and comp.company_id =:companyId ");
			   }  
			   if(reqDto.getPatientId()!=null)
			   {
				   query1.append(" and pat.patient_id =:patientId ");
				   query2.append(" and pat.patient_id =:patientId ");
			   }
			   if(reqDto.getAdmissionNo()!=null && reqDto.getAdmissionNo().length()>0)
			   {
				   query1.append(" and lower(adm.admission_number) like :admissionNo " );
				   query2.append(" and lower(enmst.encounter_number) like :admissionNo ");
			   }
		        StringBuilder query = new StringBuilder(query1).append(" union all ").append(query2);

			   Query sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
					   .setInteger("unitId", reqDto.getUnitId());


			 if(reqDto.getPaymentEntitlementId()!=null)
				 sqlQuery.setParameter("paymentEntitlementId", reqDto.getPaymentEntitlementId());

			 if(reqDto.getVisitTypeId()!=null)
				 sqlQuery.setParameter("visitTypeId", reqDto.getVisitTypeId());

			 if(reqDto.getCompanyId()!=null)
				 sqlQuery.setParameter("companyId", reqDto.getCompanyId());

			 if(reqDto.getPatientId()!=null)
				 sqlQuery.setParameter("patientId", reqDto.getPatientId());

			 if(reqDto.getAdmissionNo()!=null && !reqDto.getAdmissionNo().isEmpty())
				 sqlQuery.setParameter("admissionNo", "%" + reqDto.getAdmissionNo().toLowerCase() + "%");

			 List<PreAuthorizationRequestDto> listPreAuthorizationRequestDto = sqlQuery
					   .setResultTransformer(Transformers.aliasToBean(PreAuthorizationRequestDto.class)).list();

			   	return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS, null, listPreAuthorizationRequestDto.size());
					
				}catch(Exception e){
					
					return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}
		
	}
	

	@Override
	public Response saveNewPreAuthorizationRequest(MPreauthRequestMasterDto dto) throws BillingException {
		try{
				MPreauthRequestMaster master = new MPreauthRequestMaster();
					master.setCreatedBy(dto.getCreatedBy());
					master.setPatientSponsorId(dto.getPatientSponsorId());
					master.setPatientId(dto.getPatientId());
					master.setOrganisationId(dto.getOrganisationId());
					master.setPayeeId(dto.getPayeeId());
					master.setRequesDate(dto.getRequesDate() != null ? new Date(dto.getRequesDate()) : TimeZoneComponent.getDateByzone(dto.getUnitId()));
					master.setPatientId(dto.getPatientId());
					master.setStatus(ACTIVE);
					master.setUnitId(dto.getUnitId());
					master.setVisitType(dto.getVisitType());
					master.setAdmissionId(dto.getAdmissionId());
					master.setEncounterId(dto.getEncounterId());
					master.setRequestNo(dto.getRequestNo());
					master.setRequestTypeId(dto.getRequestTypeId());
					master.setRequestSendBy(dto.getRequestSendBy());
					master.setReceivedThrough(dto.getReceivedThrough());
					master.setRequestAmount(dto.getRequestAmount());
					master.setRequestRemark(dto.getRequestRemark());
					master.setRequestSendThrough(dto.getRequestSendThrough());
					master.setCreatedBy(dto.getCreatedBy());
					master.setCreatedDate(TimeZoneComponent.getDateByzone(dto.getUnitId()).getTime());
					master.setAuthStatusId(dto.getAuthStatusId());
					master.setIsAuthDeleted('N');
					master.setIsFinalSubmit('N');
					master.setIsSplitStatus('N');
					master.setDocTypeId(dto.getDocTypeId());
					master.setDocumentName(dto.getDocumentName());
					master.setContractId(dto.getContractId());
				master = save(master);
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, master);
		}catch(HibernateException he){
			throw new BillingException(he.getMessage());
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}



	@SuppressWarnings("null")
	@Override
	public Response updateAuthorizationRequest(MPreauthRequestMasterDto reqDto) throws BillingException {
		try{
				if(reqDto.getPreauthRequestId()!=null)
				{
					/*Integer updateCount = sessionFactory.getCurrentSession()
					.getNamedQuery("UPDATE_AUTHORIZATION_DETAILS_BY_PREAUTH_REQUEST_ID")
					.setInteger("preauthRequestId", reqDto.getPreauthRequestId())
					.setString("authorizationNo", reqDto.getAuthorizationNo())
					.setDouble("authorizationAmount", reqDto.getAuthorizationAmount())
					.setDate("authorizationDate",reqDto.getAuthorizationDate() != null ? new Date(reqDto.getAuthorizationDate()) : TimeZoneComponent.getDateByzone(unitId))
					.setInteger("receivedThrough", reqDto.getReceivedThrough())
					.setInteger("receivedBy", reqDto.getReceivedBy())
					.setInteger("updatedBy", reqDto.getUpdatedBy())
					.setDate("updatedDate",TimeZoneComponent.getDateByzone(unitId))
					.setString("authRemark",reqDto.getAuthRemark())
					.setCharacter("isFinalSubmit", reqDto.getIsFinalSubmit())
					.setInteger("authStatusId", reqDto.getAuthStatusId())
					.setInteger("organisationId", reqDto.getOrganisationId())
					.setInteger("unitId", reqDto.getUnitId())
					.executeUpdate();*/
					MPreauthRequestMaster mPreauthRequestMaster = findById(reqDto.getPreauthRequestId());
					if (mPreauthRequestMaster == null || !mPreauthRequestMaster.getOrganisationId().equals(verifiedIdentityService.getVerifiedOrganizationId()) || !mPreauthRequestMaster.getUnitId().equals(verifiedIdentityService.getVerifiedUnitId())) {
					throw new BillingException("Preauthorization request does not belong to the current organization");
					}
						mPreauthRequestMaster.setAuthorizationNo(reqDto.getAuthorizationNo());
						mPreauthRequestMaster.setAuthorizationAmount(reqDto.getAuthorizationAmount());
						mPreauthRequestMaster.setAuthorizationDate(reqDto.getAuthorizationDate() != null ? new Date(reqDto.getAuthorizationDate()) : TimeZoneComponent.getDateByzone(reqDto.getUnitId()));
						mPreauthRequestMaster.setReceivedThrough(reqDto.getReceivedThrough());
						mPreauthRequestMaster.setReceivedBy(reqDto.getReceivedBy());
						mPreauthRequestMaster.setUpdatedBy(reqDto.getUpdatedBy());
						mPreauthRequestMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(reqDto.getUnitId()).getTime());
						mPreauthRequestMaster.setAuthRemark(reqDto.getAuthRemark());
						mPreauthRequestMaster.setIsFinalSubmit(reqDto.getIsFinalSubmit());
						mPreauthRequestMaster.setAuthStatusId(reqDto.getAuthStatusId());
						mPreauthRequestMaster.setDocTypeId(reqDto.getDocTypeId());
					update(mPreauthRequestMaster);
					
					if(reqDto.getIsFinalSubmit().equals('Y'))
					{
						Criteria c=sessionFactory.getCurrentSession().createCriteria(MPreauthMaster.class); 
							if(mPreauthRequestMaster.getAdmissionId()!=null)
								c.add(Restrictions.eq("admissionId", mPreauthRequestMaster.getAdmissionId()));
							if(mPreauthRequestMaster.getEncounterId()!=null)
								c.add(Restrictions.eq("encounterId", mPreauthRequestMaster.getEncounterId()));
							c.add(Restrictions.eq("payeeId", mPreauthRequestMaster.getPayeeId()));
							c.add(Restrictions.eq("patientSponsorId", mPreauthRequestMaster.getPatientSponsorId()));
						MPreauthMaster obj = (MPreauthMaster)c.uniqueResult();
						
						if(obj!=null)
						{
								obj.setAuthorizedAmount(obj.getAuthorizedAmount()+reqDto.getAuthorizationAmount());
								obj.setBalanceAmount(obj.getBalanceAmount()+reqDto.getAuthorizationAmount());
							sessionFactory.getCurrentSession().update(obj);
						}else{
							MPreauthMaster master = new MPreauthMaster();
								master.setPatientId(mPreauthRequestMaster.getPatientId());
								master.setAdmissionId(mPreauthRequestMaster.getAdmissionId());
								master.setEncounterId(mPreauthRequestMaster.getEncounterId());
								master.setPatientSponsorId(mPreauthRequestMaster.getPatientSponsorId());
								master.setPayeeId(mPreauthRequestMaster.getPayeeId());
								master.setContractId(mPreauthRequestMaster.getContractId());
								master.setOrganisationId(reqDto.getOrganisationId());
								master.setStatus(ACTIVE);
								master.setUnitId(reqDto.getUnitId());
								master.setCreatedBy(reqDto.getUpdatedBy());
								master.setUpdatedBy(reqDto.getUpdatedBy());
								master.setAuthorizedAmount(reqDto.getAuthorizationAmount());
								master.setBalanceAmount(reqDto.getAuthorizationAmount());
							sessionFactory.getCurrentSession().save(master);
						}
						return new Response<>(SUCCESS, SUCCESS_CODE, null, null, 'Y');
					}
					
					
					return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
				}
			}catch(Exception e){
				
				throw new BillingException(e.getMessage());
			}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}



	@Override
	public Response getPreAuthorizationRequestListByPayeeId(MPreauthRequestMasterDto reqDto) throws BillingException {
		try{
			
			
			
			StringBuilder query = new StringBuilder();
			if(reqDto.getEncounterId()!=null && reqDto.getEncounterId()>0)
			{
				 query.append("select "
							+ "	distinct(mprm.preauth_request_id) as \"preauthRequestId\", "
							+ "	mprm.payee_id as \"payeeId\", "
							+ "	mprm.request_no as \"requestNo\", "
							+ "	mprm.request_type_id as \"requestTypeId\", "
							+ "	reqmst.request_type_desc as \"requestTypeName\", "
							+ "	sendthmst.send_throug_desc as \"requestSendThroughName\", "
							+ "	to_char(mprm.request_date, 'DD-MM-YYYY') as \"requestDate\", "
							+ "	mprm.request_date as \"requestDateNew\", "
							+ "	mprm.patient_id as \"patientId\", "
							+ "	mprm.request_send_through as \"requestSendThrough\", "
							+ "	mprm.request_amount as \"requestAmount\", "
							+ "	mprm.authorization_no as \"authorizationNo\", "
							+ "	to_char(mprm.authorization_date, 'DD-MM-YYYY') as \"authDate\", "
							+ "	to_char(mprm.authorization_date, 'DD/MM/YYYY HH24:MI:SS') as \"authDateString\", "
							+ "	to_char(mprm.request_date, 'DD/MM/YYYY HH24:MI:SS') as \"requestDateString\", "
							+ "	mprm.authorization_date as \"authDateNew\", "
							+ "	mprm.authorization_amount as \"authorizationAmount\", "
							+ "	mprm.received_through as \"authRequestSendThroughId\", "
							+ "	mprm.received_by as \"receivedby\", "
							+ "	receivedmst.send_throug_desc as \"authRequestSendThroughName\", "
							+ "	concat(coalesce(pre.prefix_code,''),' ',coalesce(pat.first_name) , ' ', coalesce(pat.middle_name) , ' ', coalesce(pat.last_name)) as \"patientName\", "
							+ "	concat(coalesce(pat.first_name) , ' ', coalesce(pat.middle_name) , ' ', coalesce(pat.last_name)) as \"requestSendByName\", "
							+ "	pat.uhid_number as \"uhidNumber\", "
							+ "	gender.gender_code as \"genderCode\", "
							+ "	gender.gender_name as \"genderDec\", "
							+ "	mprm.auth_status \"authorizationStatus\", "
							+ "	mprm.is_final_submit \"isFinalSubmit\", "
							+ "	mprm.request_remark \"requestRemark\", "
							+ "	mprm.auth_remark \"authRemark\", "
							+ "	tcm.company_desc \"companyDesc\", "
							+ " (case when em.employee_id is not null then "
							+ " 	(concat(coalesce(em.first_name), ' ', coalesce(em.middle_name), ' ', coalesce(em.last_name))) "
							+ " when doc.doctor_id is not null then "
							+" 		(concat('Dr. ',coalesce(doc.first_name), ' ', coalesce(doc.middle_name), ' ', coalesce(doc.last_name))) "
							+" else( userMstr.user_name ) "
							+ " end) \"receivedByName\", "
							+ "	authstatusdmst.auth_status_desc as \"authorizationStatusName\" "
							+ "from "
							+ "	billing.m_preauth_request_master mprm "
							+ "inner join billing.m_request_type_master reqmst on "
							+ "	reqmst.request_type_id = mprm.request_type_id "
							+ "inner join public.t_unit_application_billing_configuration config on "
							+ "	config.unit_id = mprm.unit_id "
							+ "inner join opd.t_encounter_master enc on "
							+ "	enc.encounter_id = mprm.encounter_id "
							+ "inner join opd.t_patient_sponsor_details psd on "
							+ "	enc.encounter_id = psd.encounter_id "
							+ "	and psd.status = 'A' "
							+ "	and psd.payee_id <> config.self_payee_id "
							+ "left join lab.m_send_throgh_master sendthmst on "
							+ "	sendthmst.send_through_id = mprm.request_send_through "
							+ "left join lab.m_send_throgh_master receivedmst on "
							+ "	receivedmst.send_through_id = cast(mprm.received_through as integer) "
							+ "left join patient.t_patient_registration pat on "
							+ "	pat.patient_id = mprm.patient_id "
							+ " left join m_prefix_master pre on	pre.prefix_id = pat.prefix_id "
							+ "left join public.m_gender_master gender on "
							+ "	gender.gender_id = pat.gender_id "
							+ "left join billing.m_authorization_status_master authstatusdmst on "
							+ "	authstatusdmst.auth_status_id = mprm.auth_status "
							/*+ "left join public.m_user_master usermst on "
							+ "	usermst.user_id = mprm.received_by "*/
							+" left join public.m_employee_master em on "
							+" 	em.user_id = mprm.received_by "
							+" left join doctor.m_doctor_master doc on "
							+" 	doc.user_id = mprm.received_by "
							+" left join  public.m_user_master userMstr on userMstr.user_id = mprm.received_by	 "
							+" left join  public.t_company_master tcm on tcm.company_id = mprm.payee_id	"
							+ " inner join opd.t_patient_sponsor_details spons on spons.patient_sponsor_details_id=mprm.patient_sponsor_id "
							+ "where "
							+ "	mprm.organisation_id =:organisationId "
							+ "	and mprm.unit_id =:unitId "
							+ "	and mprm.patient_id =:patientId and spons.status='A' "
							+ "	and enc.encounter_id =:encounterId ");

					if(reqDto.getPayeeId() != null )
						query.append(" and mprm.payee_id =:payeeId ");

					if(reqDto.getAuthStatusId() != null )
						query.append(" and mprm.auth_status =:authStatusId "); //is authstatusId 5 means close

					if(reqDto.getIsSplitStatus() != null )
						query.append(" and mprm.is_split_status =:isSplitStatus ");
				 	query.append(" ORDER BY mprm.preauth_request_id ");


			
			}
			else if(reqDto.getAdmissionId()!=null && reqDto.getAdmissionId()>0)
			{
				 query.append( " select "
						+ "	distinct(mprm.preauth_request_id) as \"preauthRequestId\", "
						+ "	mprm.payee_id as \"payeeId\", "
						+ "	mprm.request_no as \"requestNo\", "
						+ "	mprm.request_type_id as \"requestTypeId\", "
						+ "	reqmst.request_type_desc as \"requestTypeName\", "
						+ "	sendthmst.send_throug_desc as \"requestSendThroughName\", "
						+ "	to_char(mprm.request_date, 'DD-MM-YYYY') as \"requestDate\", "
						+ "	mprm.request_date as \"requestDateNew\", "
						+ "	mprm.patient_id as \"patientId\", "
						+ "	mprm.request_send_through as \"requestSendThrough\", "
						+ "	mprm.request_amount as \"requestAmount\", "
						+ "	mprm.authorization_no as \"authorizationNo\", "
						+ "	to_char(mprm.authorization_date, 'DD-MM-YYYY') as \"authDate\", "
						+ "	to_char(mprm.authorization_date, 'DD/MM/YYYY HH24:MI:SS') as \"authDateString\", "
						+ "	to_char(mprm.request_date, 'DD/MM/YYYY HH24:MI:SS') as \"requestDateString\", "
						+ "	mprm.authorization_date as \"authDateNew\", "
						+ "	mprm.authorization_amount as \"authorizationAmount\", "
						+ "	mprm.received_through as \"authRequestSendThroughId\", "
						+ "	mprm.received_by as \"receivedby\", "
						+ "	receivedmst.send_throug_desc as \"authRequestSendThroughName\", "
						+ "	concat(coalesce(pre.prefix_code,''),' ',coalesce(pat.first_name) , ' ', coalesce(pat.middle_name) , ' ', coalesce(pat.last_name)) as \"patientName\", "
						+ "	concat(coalesce(pat.first_name) , ' ', coalesce(pat.middle_name) , ' ', coalesce(pat.last_name)) as \"requestSendByName\", "
						+ "	pat.uhid_number as \"uhidNumber\", "
						+ "	gender.gender_code as \"genderCode\", "
						+ "	gender.gender_name as \"genderDec\", "
						+ "	mprm.auth_status \"authorizationStatus\", "
						+ "	mprm.is_final_submit \"isFinalSubmit\", "
						+ "	mprm.request_remark \"requestRemark\", "
						+ "	tcm.company_desc \"companyDesc\", "
						+ "	mprm.auth_remark \"authRemark\", "
						+ " (case when em.employee_id is not null then "
						+ " 	(concat(coalesce(em.first_name), ' ', coalesce(em.middle_name), ' ', coalesce(em.last_name))) "
						+ " when doc.doctor_id is not null then "
						+" 		(concat('Dr. ',coalesce(doc.first_name), ' ', coalesce(doc.middle_name), ' ', coalesce(doc.last_name))) "
						+" else( userMstr.user_name ) "
						+ " end) \"receivedByName\", "
						+ "	authstatusdmst.auth_status_desc as \"authorizationStatusName\" "
						+ "from "
						+ "	billing.m_preauth_request_master mprm "
						+ "inner join billing.m_request_type_master reqmst on "
						+ "	reqmst.request_type_id = mprm.request_type_id "
						+ "inner join public.t_unit_application_billing_configuration config on "
						+ "	config.unit_id = mprm.unit_id "
						+ "inner join adt.t_admission adm on "
						+ "	adm.admission_id = mprm.admission_id "
						+ "inner join opd.t_patient_sponsor_details psd on "
						+ "	adm.admission_id = psd.admission_id "
						+ "	and psd.status = 'A' "
						+ "	and psd.payee_id  <>  config.self_payee_id "
						+ "left join lab.m_send_throgh_master sendthmst on "
						+ "	sendthmst.send_through_id = mprm.request_send_through "
						+ "left join lab.m_send_throgh_master receivedmst on "
						+ "	receivedmst.send_through_id = cast(mprm.received_through as integer) "
						+ "left join patient.t_patient_registration pat on "
						+ "	pat.patient_id = mprm.patient_id "
						+ "   left join m_prefix_master pre on	pre.prefix_id = pat.prefix_id "
						+ "left join public.m_gender_master gender on "
						+ "	gender.gender_id = pat.gender_id "
						+ "left join billing.m_authorization_status_master authstatusdmst on "
						+ "	authstatusdmst.auth_status_id = mprm.auth_status "
						+" left join public.m_employee_master em on "
						+" 	em.user_id = mprm.received_by "
						+" left join doctor.m_doctor_master doc on "
						+" 	doc.user_id = mprm.received_by "
						+" left join  public.m_user_master userMstr on userMstr.user_id = mprm.received_by	 "
						+" left join  public.t_company_master tcm on tcm.company_id = mprm.payee_id	 "
						+ "where "
						+ "	mprm.organisation_id =:organisationId "
						+ "	and mprm.unit_id =:unitId "
						+ "	and mprm.patient_id =:patientId "
						+ "	and adm.admission_id =:admissionId ");

				if(reqDto.getPayeeId() != null )
					 query.append(" and mprm.payee_id =:payeeId ");

				if(reqDto.getAuthStatusId() != null )
					 query.append(" and mprm.auth_status =:authStatusId "); //is authstatusId 5 means close

				if(reqDto.getIsSplitStatus() != null )
					 query.append(" and mprm.is_split_status =:isSplitStatus ");

			    query.append(" ORDER BY mprm.preauth_request_id ");

			}
			
			else {
			
			
			   query.append("SELECT"
						+ "   mprm.preauth_request_id as \"preauthRequestId\","
						+ "   mprm.request_no as \"requestNo\","
						+ "   mprm.request_type_id as \"requestTypeId\","
						+ "   reqmst.request_type_desc as \"requestTypeName\","
						+ "   sendthmst.send_throug_desc as \"requestSendThroughName\","
						+ "   to_char(mprm.request_date, 'DD-MM-YYYY') as \"requestDate\","
						+ "   mprm.request_date as \"requestDateNew\","
						+ "   mprm.patient_id as \"patientId\","
						+ "   mprm.request_send_through as \"requestSendThrough\","
						+ "   mprm.request_amount as \"requestAmount\","
						+ "   mprm.authorization_no as \"authorizationNo\","
						+ "   to_char(mprm.authorization_date, 'DD-MM-YYYY') as \"authDate\","
						+ "	to_char(mprm.authorization_date, 'DD/MM/YYYY HH24:MI:SS') as \"authDateString\", "
						+ "	to_char(mprm.request_date, 'DD/MM/YYYY HH24:MI:SS') as \"requestDateString\", "
						+ "   mprm.authorization_date as \"authDateNew\","
						+ "   mprm.authorization_amount as \"authorizationAmount\","
						+ "   mprm.received_through as \"authRequestSendThroughId\","
						+ "   mprm.received_by as \"receivedby\","
						+ "   receivedmst.send_throug_desc as \"authRequestSendThroughName\","
						+ "   concat(coalesce(pre.prefix_code,''),' ',coalesce(pat.first_name) ,' ', coalesce(pat.middle_name) ,' ', coalesce(pat.last_name)) as \"patientName\", "
						+ "   concat(coalesce(pat.first_name) ,' ', coalesce(pat.middle_name) ,' ', coalesce(pat.last_name)) as \"requestSendByName\", "
						+ "    pat.uhid_number as \"uhidNumber\","
						+ "    gender.gender_code as \"genderCode\","
						+ "    gender.gender_name as \"genderDec\","
						+ "    mprm.auth_status \"authorizationStatus\"," 
						+ "    mprm.is_final_submit \"isFinalSubmit\"," 
						+ "    mprm.request_remark \"requestRemark\"," 
						+ "    mprm.auth_remark \"authRemark\"," 
						+ "    usermst.user_name \"receivedByName\"," 
						+ "    authstatusdmst.auth_status_desc as \"authorizationStatusName\" "  
						+ " FROM"
						+ "   billing.m_preauth_request_master mprm"
						+ "   inner join billing.m_request_type_master reqmst on reqmst.request_type_id = mprm.request_type_id"
						+ "   left join lab.m_send_throgh_master sendthmst on sendthmst.send_through_id = mprm.request_send_through"
						+ "   left join lab.m_send_throgh_master receivedmst on receivedmst.send_through_id = CAST(mprm.received_through AS INTEGER)"
						+"    left join patient.t_patient_registration pat on pat.patient_id = mprm.patient_id "
						+ "   left join m_prefix_master pre on	pre.prefix_id = pat.prefix_id "
						+"	  left join public.m_gender_master gender on gender.gender_id= pat.gender_id "
						+ "   left join billing.m_authorization_status_master authstatusdmst on authstatusdmst.auth_status_id  = mprm.auth_status"
						+ "   left join public.m_user_master usermst on usermst.user_id  = mprm.received_by"
						+ " WHERE"
						+ "   mprm.organisation_id =:organisationId "
						+ "  and mprm.unit_id=:unitId"
						+ "  and mprm.patient_id=:patientId");

					if(reqDto.getPayeeId() != null )
						query.append(" and mprm.payee_id =:payeeId ");

					if(reqDto.getAuthStatusId() != null )
					   query.append(" and mprm.auth_status =:authStatusId "); //is authstatusId 5 means close

					if(reqDto.getIsSplitStatus() != null )
					   query.append(" and mprm.is_split_status =:isSplitStatus ");

			   query.append(" ORDER BY mprm.preauth_request_id ");
			}
			 
			Query sqlQuery = sessionFactory.getCurrentSession()
				   .createSQLQuery(query.toString())
				   .setInteger("patientId", reqDto.getPatientId())
				   .setInteger("organisationId", reqDto.getOrganisationId())
				   .setInteger("unitId", reqDto.getUnitId());

			if(reqDto.getPayeeId() != null )
				sqlQuery.setParameter("payeeId", reqDto.getPayeeId());

			if(reqDto.getAuthStatusId() != null )
				sqlQuery.setParameter("authStatusId", reqDto.getAuthStatusId());

			if(reqDto.getIsSplitStatus() != null )
				sqlQuery.setParameter("isSplitStatus", reqDto.getIsSplitStatus());

			if(reqDto.getEncounterId()!=null && reqDto.getEncounterId()>0)
				sqlQuery.setParameter("encounterId", reqDto.getEncounterId());
			else if (reqDto.getAdmissionId() != null && reqDto.getAdmissionId() > 0)
				sqlQuery.setParameter("admissionId", reqDto.getAdmissionId());



			List<MPreauthRequestMasterDto> listMPreauthRequestMasterDto = sqlQuery
					   .setResultTransformer(Transformers.aliasToBean(MPreauthRequestMasterDto.class)).list();
			   
			   return new Response<>(SUCCESS, SUCCESS_CODE, null, listMPreauthRequestMasterDto, null);
		  
		  }catch (Exception e) {
		   
		  }
		return new Response<>(ERROR, ERROR, COMMON_ERROR_MESSAGE, null, null);
	}


	
	@Override
	public Response getPreAuthorizationRequestListByPayeeIdIPDCharges(MPreauthRequestMasterDto reqDto)
			throws BillingException {
		try{
			
			
			
			StringBuilder query = new StringBuilder();
			if(reqDto.getEncounterId()!=null && reqDto.getEncounterId()>0)
			{
				 query.append("select "
							+ "	distinct(mprm.preauth_request_id) as \"preauthRequestId\", "
							+ "	mprm.payee_id as \"payeeId\", "
							+ "	mprm.request_no as \"requestNo\", "
							+ "	mprm.request_type_id as \"requestTypeId\", "
							+ "	reqmst.request_type_desc as \"requestTypeName\", "
							+ "	sendthmst.send_throug_desc as \"requestSendThroughName\", "
							+ "	to_char(mprm.request_date, 'DD-MM-YYYY') as \"requestDate\", "
							+ "	mprm.patient_id as \"patientId\", "
							+ "	mprm.request_send_through as \"requestSendThrough\", "
							+ "	mprm.request_amount as \"requestAmount\", "
							+ "	mprm.authorization_no as \"authorizationNo\", "
							+ "	to_char(mprm.authorization_date, 'DD-MM-YYYY') as \"authDate\", "
							+ "   mprm.request_date as \"requestDateNew\","
							+ "   mprm.authorization_date as \"authDateNew\","
							+ "	mprm.authorization_amount as \"authorizationAmount\", "
							+ "	mprm.received_through as \"authRequestSendThroughId\", "
							+ "	mprm.received_by as \"receivedby\", "
							+ "	receivedmst.send_throug_desc as \"authRequestSendThroughName\", "
							+ "	concat(coalesce(pre.prefix_code,''),' ',coalesce(pat.first_name) , ' ', coalesce(pat.middle_name) , ' ', coalesce(pat.last_name)) as \"patientName\", "
							+ "	concat(coalesce(pat.first_name) , ' ', coalesce(pat.middle_name) , ' ', coalesce(pat.last_name)) as \"requestSendByName\", "
							+ "	pat.uhid_number as \"uhidNumber\", "
							+ "	gender.gender_code as \"genderCode\", "
							+ "	gender.gender_name as \"genderDec\", "
							+ "	mprm.auth_status \"authorizationStatus\", "
							+ "	mprm.is_final_submit \"isFinalSubmit\", "
							+ "	mprm.request_remark \"requestRemark\", "
							+ "	mprm.auth_remark \"authRemark\", "
							+ "	tcm.company_desc \"companyDesc\", "
							+ " (case when em.employee_id is not null then "
							+ " 	(concat(coalesce(em.first_name), ' ', coalesce(em.middle_name), ' ', coalesce(em.last_name))) "
							+ " when doc.doctor_id is not null then "
							+" 		(concat('Dr. ',coalesce(doc.first_name), ' ', coalesce(doc.middle_name), ' ', coalesce(doc.last_name))) "
							+" else( userMstr.user_name ) "
							+ " end) \"receivedByName\", "
							+ "	authstatusdmst.auth_status_desc as \"authorizationStatusName\" "
							+ "from "
							+ "	billing.m_preauth_request_master mprm "
							+ "inner join billing.m_request_type_master reqmst on "
							+ "	reqmst.request_type_id = mprm.request_type_id "
							+ "inner join public.t_unit_application_billing_configuration config on "
							+ "	config.unit_id = mprm.unit_id "
							+ "inner join opd.t_encounter_master enc on "
							+ "	enc.encounter_id = mprm.encounter_id "
							+ "inner join opd.t_patient_sponsor_details psd on "
							+ "	enc.encounter_id = psd.encounter_id "
							+ "	and psd.status = 'A' "
							+ "	and psd.payee_id = mprm.payee_id "
							+ "left join lab.m_send_throgh_master sendthmst on "
							+ "	sendthmst.send_through_id = mprm.request_send_through "
							+ "left join lab.m_send_throgh_master receivedmst on "
							+ "	receivedmst.send_through_id = cast(mprm.received_through as integer) "
							+ "left join patient.t_patient_registration pat on "
							+ "	pat.patient_id = mprm.patient_id "
							+ " left join m_prefix_master pre on	pre.prefix_id = pat.prefix_id "
							+ "left join public.m_gender_master gender on "
							+ "	gender.gender_id = pat.gender_id "
							+ "left join billing.m_authorization_status_master authstatusdmst on "
							+ "	authstatusdmst.auth_status_id = mprm.auth_status "
							/*+ "left join public.m_user_master usermst on "
							+ "	usermst.user_id = mprm.received_by "*/
							+" left join public.m_employee_master em on "
							+" 	em.user_id = mprm.received_by "
							+" left join doctor.m_doctor_master doc on "
							+" 	doc.user_id = mprm.received_by "
							+" left join  public.m_user_master userMstr on userMstr.user_id = mprm.received_by	 "
							+" left join  public.t_company_master tcm on tcm.company_id = mprm.payee_id	 "
							+ "where "
							+ "	mprm.organisation_id =:organisationId "
							+ "	and mprm.unit_id =:unitId "
							+ "	and mprm.patient_id =:patientId "
							+ "	and enc.encounter_id =:encounterId ");

					if(reqDto.getPayeeId() != null )
						query.append(" and mprm.payee_id =:payeeId ");

					if(reqDto.getAuthStatusId() != null )
						query.append(" and mprm.auth_status =:authStatusId "); //is authstatusId 5 means close

					if(reqDto.getIsSplitStatus() != null )
						query.append(" and mprm.is_split_status =:isSplitStatus ");

				 	query.append(" ORDER BY mprm.preauth_request_id ");
			
			}
			else if(reqDto.getAdmissionId()!=null && reqDto.getAdmissionId()>0)
			{
				 query.append( " select "
						+ "	distinct(mprm.preauth_request_id) as \"preauthRequestId\", "
						+ "	mprm.payee_id as \"payeeId\", "
						+ "	mprm.request_no as \"requestNo\", "
						+ "	mprm.request_type_id as \"requestTypeId\", "
						+ "	reqmst.request_type_desc as \"requestTypeName\", "
						+ "	sendthmst.send_throug_desc as \"requestSendThroughName\", "
						+ "	to_char(mprm.request_date, 'DD-MM-YYYY') as \"requestDate\", "
						+ "	mprm.patient_id as \"patientId\", "
						+ "	mprm.request_send_through as \"requestSendThrough\", "
						+ "	mprm.request_amount as \"requestAmount\", "
						+ "	mprm.authorization_no as \"authorizationNo\", "
						+ "	to_char(mprm.authorization_date, 'DD-MM-YYYY') as \"authDate\", "
						+ "   mprm.request_date as \"requestDateNew\","
						+ "   mprm.authorization_date as \"authDateNew\","
						+ "	mprm.authorization_amount as \"authorizationAmount\", "
						+ "	mprm.received_through as \"authRequestSendThroughId\", "
						+ "	mprm.received_by as \"receivedby\", "
						+ "	receivedmst.send_throug_desc as \"authRequestSendThroughName\", "
						+ "	concat(coalesce(pre.prefix_code,''),' ',coalesce(pat.first_name) , ' ', coalesce(pat.middle_name) , ' ', coalesce(pat.last_name)) as \"patientName\", "
						+ "	concat(coalesce(pat.first_name) , ' ', coalesce(pat.middle_name) , ' ', coalesce(pat.last_name)) as \"requestSendByName\", "
						+ "	pat.uhid_number as \"uhidNumber\", "
						+ "	gender.gender_code as \"genderCode\", "
						+ "	gender.gender_name as \"genderDec\", "
						+ "	mprm.auth_status \"authorizationStatus\", "
						+ "	mprm.is_final_submit \"isFinalSubmit\", "
						+ "	mprm.request_remark \"requestRemark\", "
						+ "	tcm.company_desc \"companyDesc\", "
						+ "	mprm.auth_remark \"authRemark\", "
						+ " (case when em.employee_id is not null then "
						+ " 	(concat(coalesce(em.first_name), ' ', coalesce(em.middle_name), ' ', coalesce(em.last_name))) "
						+ " when doc.doctor_id is not null then "
						+" 		(concat('Dr. ',coalesce(doc.first_name), ' ', coalesce(doc.middle_name), ' ', coalesce(doc.last_name))) "
						+" else( userMstr.user_name ) "
						+ " end) \"receivedByName\", "
						+ "	authstatusdmst.auth_status_desc as \"authorizationStatusName\" "
						+ "from "
						+ "	billing.m_preauth_request_master mprm "
						+ "inner join billing.m_request_type_master reqmst on "
						+ "	reqmst.request_type_id = mprm.request_type_id "
						+ "inner join public.t_unit_application_billing_configuration config on "
						+ "	config.unit_id = mprm.unit_id "
						+ "inner join adt.t_admission adm on "
						+ "	adm.admission_id = mprm.admission_id "
						+ "inner join opd.t_patient_sponsor_details psd on "
						+ "	adm.admission_id = psd.admission_id "
						+ "	and psd.status = 'A' "
						+ "	and psd.payee_id = mprm.payee_id "
						+ "left join lab.m_send_throgh_master sendthmst on "
						+ "	sendthmst.send_through_id = mprm.request_send_through "
						+ "left join lab.m_send_throgh_master receivedmst on "
						+ "	receivedmst.send_through_id = cast(mprm.received_through as integer) "
						+ "left join patient.t_patient_registration pat on "
						+ "	pat.patient_id = mprm.patient_id "
						+ "   left join m_prefix_master pre on	pre.prefix_id = pat.prefix_id "
						+ "left join public.m_gender_master gender on "
						+ "	gender.gender_id = pat.gender_id "
						+ "left join billing.m_authorization_status_master authstatusdmst on "
						+ "	authstatusdmst.auth_status_id = mprm.auth_status "
						+" left join public.m_employee_master em on "
						+" 	em.user_id = mprm.received_by "
						+" left join doctor.m_doctor_master doc on "
						+" 	doc.user_id = mprm.received_by "
						+" left join  public.m_user_master userMstr on userMstr.user_id = mprm.received_by	 "
						+" left join  public.t_company_master tcm on tcm.company_id = mprm.payee_id	 "
						+ "where "
						+ "	mprm.organisation_id =:organisationId "
						+ "	and mprm.unit_id =:unitId "
						+ "	and mprm.patient_id =:patientId "
						+ "	and adm.admission_id =:admissionId ");

				if(reqDto.getPayeeId() != null )
					query.append(" and mprm.payee_id =:payeeId ");

				if(reqDto.getAuthStatusId() != null )
					query.append(" and mprm.auth_status =:authStatusId "); //is authstatusId 5 means close

				if(reqDto.getIsSplitStatus() != null )
					query.append(" and mprm.is_split_status =:isSplitStatus ");

			 	query.append(" ORDER BY mprm.preauth_request_id ");
			}
			
			else {
			
			
			   query.append("SELECT"
						+ "   mprm.preauth_request_id as \"preauthRequestId\","
						+ "   mprm.request_no as \"requestNo\","
						+ "   mprm.request_type_id as \"requestTypeId\","
						+ "   reqmst.request_type_desc as \"requestTypeName\","
						+ "   sendthmst.send_throug_desc as \"requestSendThroughName\","
						+ "   to_char(mprm.request_date, 'DD-MM-YYYY') as \"requestDate\","
						+ "   mprm.request_date as \"requestDateNew\","
						+ "   mprm.patient_id as \"patientId\","
						+ "   mprm.request_send_through as \"requestSendThrough\","
						+ "   mprm.request_amount as \"requestAmount\","
						+ "   mprm.authorization_no as \"authorizationNo\","
						+ "   to_char(mprm.authorization_date, 'DD-MM-YYYY') as \"authDate\","
						+ "   mprm.authorization_date as \"authDateNew\","
						+ "   mprm.authorization_amount as \"authorizationAmount\","
						+ "   mprm.received_through as \"authRequestSendThroughId\","
						+ "   mprm.received_by as \"receivedby\","
						+ "   receivedmst.send_throug_desc as \"authRequestSendThroughName\","
						+ "   concat(coalesce(pre.prefix_code,''),' ',coalesce(pat.first_name) ,' ', coalesce(pat.middle_name) ,' ', coalesce(pat.last_name)) as \"patientName\", "
						+ "   concat(coalesce(pat.first_name) ,' ', coalesce(pat.middle_name) ,' ', coalesce(pat.last_name)) as \"requestSendByName\", "
						+ "    pat.uhid_number as \"uhidNumber\","
						+ "    gender.gender_code as \"genderCode\","
						+ "    gender.gender_name as \"genderDec\","
						+ "    mprm.auth_status \"authorizationStatus\"," 
						+ "    mprm.is_final_submit \"isFinalSubmit\"," 
						+ "    mprm.request_remark \"requestRemark\"," 
						+ "    mprm.auth_remark \"authRemark\"," 
						+ "    usermst.user_name \"receivedByName\"," 
						+ "    authstatusdmst.auth_status_desc as \"authorizationStatusName\" "  
						+ " FROM"
						+ "   billing.m_preauth_request_master mprm"
						+ "   inner join billing.m_request_type_master reqmst on reqmst.request_type_id = mprm.request_type_id"
						+ "   left join lab.m_send_throgh_master sendthmst on sendthmst.send_through_id = mprm.request_send_through"
						+ "   left join lab.m_send_throgh_master receivedmst on receivedmst.send_through_id = CAST(mprm.received_through AS INTEGER)"
						+"    left join patient.t_patient_registration pat on pat.patient_id = mprm.patient_id "
						+ "   left join m_prefix_master pre on	pre.prefix_id = pat.prefix_id "
						+"	  left join public.m_gender_master gender on gender.gender_id= pat.gender_id "
						+ "   left join billing.m_authorization_status_master authstatusdmst on authstatusdmst.auth_status_id  = mprm.auth_status"
						+ "   left join public.m_user_master usermst on usermst.user_id  = mprm.received_by"
						+ " WHERE"
						+ "   mprm.organisation_id =:organisationId "
						+ "  and mprm.unit_id=:unitId"
						+ "  and mprm.patient_id=:patientId");

					if(reqDto.getPayeeId() != null )
						query.append(" and mprm.payee_id =:payeeId ");

					if(reqDto.getAuthStatusId() != null )
						query.append(" and mprm.auth_status =:authStatusId "); //is authstatusId 5 means close

					if(reqDto.getIsSplitStatus() != null )
						query.append(" and mprm.is_split_status =:isSplitStatus ");

					query.append(" ORDER BY mprm.preauth_request_id ");
			}
			 
			Query sqlQuery = sessionFactory.getCurrentSession()
				   .createSQLQuery(query.toString())
				   .setInteger("patientId", reqDto.getPatientId())
				   .setInteger("organisationId", reqDto.getOrganisationId())
				   .setInteger("unitId", reqDto.getUnitId());

			if(reqDto.getPayeeId() != null )
				sqlQuery.setParameter("payeeId", reqDto.getPayeeId());

			if(reqDto.getAuthStatusId() != null )
				sqlQuery.setParameter("authStatusId", reqDto.getAuthStatusId());

			if(reqDto.getIsSplitStatus() != null )
				sqlQuery.setParameter("isSplitStatus", reqDto.getIsSplitStatus());

			if(reqDto.getEncounterId()!=null && reqDto.getEncounterId()>0)
				sqlQuery.setParameter("encounterId", reqDto.getEncounterId());
			else if (reqDto.getAdmissionId() != null && reqDto.getAdmissionId() > 0)
				sqlQuery.setParameter("admissionId", reqDto.getAdmissionId());

			List<MPreauthRequestMasterDto> listMPreauthRequestMasterDto = sqlQuery.setResultTransformer(Transformers.aliasToBean(MPreauthRequestMasterDto.class)).list();
			   
			   return new Response<>(SUCCESS, SUCCESS_CODE, null, listMPreauthRequestMasterDto, null);
		  
		  }catch (Exception e) {
		   
		  }
		return new Response<>(ERROR, ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response cancelNewPreAuthorizationRequestByPreAuthorizationId(MPreauthRequestMasterDto reqDto)
			throws BillingException {
		try
		{
			MPreauthRequestMaster preauthRequestMst= findById(reqDto.getPreauthRequestId());
			if (preauthRequestMst == null || !preauthRequestMst.getOrganisationId().equals(verifiedIdentityService.getVerifiedOrganizationId()) || !preauthRequestMst.getUnitId().equals(verifiedIdentityService.getVerifiedUnitId())) {
					throw new BillingException("Preauthorization request does not belong to the current organization");
			}
			if (preauthRequestMst.getPreauthRequestId() != 0)
			{
				preauthRequestMst.setAuthStatusId(reqDto.getAuthStatusId());
				MPreauthRequestMaster pAutRequestMst = update(preauthRequestMst);
				if(pAutRequestMst.getAuthStatusId()==5) 
				{
					if(reqDto.getIsFinalSubmit().equals('Y'))
					{
						Criteria c=sessionFactory.getCurrentSession().createCriteria(MPreauthMaster.class); 
							if(preauthRequestMst.getAdmissionId()!=null)
								c.add(Restrictions.eq("admissionId", preauthRequestMst.getAdmissionId()));
							if(preauthRequestMst.getEncounterId()!=null)
								c.add(Restrictions.eq("encounterId", preauthRequestMst.getEncounterId()));
							c.add(Restrictions.eq("payeeId", preauthRequestMst.getPayeeId()));
							c.add(Restrictions.eq("patientSponsorId", preauthRequestMst.getPatientSponsorId()));
						MPreauthMaster obj = (MPreauthMaster)c.uniqueResult();
						
						if(obj!=null)
						{
								obj.setAuthorizedAmount(obj.getAuthorizedAmount()-preauthRequestMst.getAuthorizationAmount());
								obj.setBalanceAmount(obj.getBalanceAmount()-preauthRequestMst.getAuthorizationAmount());
							sessionFactory.getCurrentSession().update(obj);
						}
						return new Response<>(SUCCESS, SUCCESS_CODE, null, null, 'Y');
					}
					
					
					
					return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
				}else {
					return new Response<>(ERROR, ERROR_CODE,  SERVER_ERROR, null, null);
				}
			} else
			{
				return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
			}

		} catch (Exception e)
		{
			
			throw new BillingException(e.getMessage());
		}
	}



	@Override
	public Response updateIsSplitStatusByPreAuthorizationId(MPreauthRequestMasterDto reqDto) throws BillingException {
		try{
			MPreauthRequestMaster preauthRequestMst = findById(reqDto.getPreauthRequestId());
			if (preauthRequestMst == null || !preauthRequestMst.getOrganisationId().equals(verifiedIdentityService.getVerifiedOrganizationId()) || !preauthRequestMst.getUnitId().equals(verifiedIdentityService.getVerifiedUnitId())) {
					throw new BillingException("Preauthorization request does not belong to the current organization");
			}
			if (preauthRequestMst.getPreauthRequestId()!= 0)
			{
				preauthRequestMst.setIsSplitStatus('Y');
				preauthRequestMst.setAdmissionId(reqDto.getAdmissionId());
				preauthRequestMst.setEncounterId(reqDto.getEncounterId());
				preauthRequestMst = update(preauthRequestMst);
				return new Response<>(SUCCESS, SUCCESS_CODE, null, null, preauthRequestMst);
			}else {
				
				return new Response<>(ERROR, ERROR, COMMON_ERROR_MESSAGE, null, preauthRequestMst);
			}
			
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public Response updateConsumedAmount(Integer admissionId,Integer encounterId) throws BillingException {
		try{
			
			Criteria c=sessionFactory.getCurrentSession().createCriteria(MPreauthMaster.class); 
			if(admissionId!=null)
				c.add(Restrictions.eq("admissionId", admissionId));
			if(encounterId!=null)
				c.add(Restrictions.eq("encounterId", encounterId));
			
			List<MPreauthMaster> mPreauthMastersList = (List<MPreauthMaster>)c.list();
			if(mPreauthMastersList!=null)
			{
				for(MPreauthMaster master: mPreauthMastersList)
				{
					Double consumedAmount =  (master.getAuthorizedAmount() != null ? master.getAuthorizedAmount() :0 ) - (master.getBalanceAmount() != null ? master.getBalanceAmount() :0);
					
					if(consumedAmount > 0)
					{
							master.setAuthorizedAmount(master.getAuthorizedAmount()-consumedAmount);
						sessionFactory.getCurrentSession().update(master);
						
						Criteria inner=sessionFactory.getCurrentSession().createCriteria(MPreauthRequestMaster.class); 
						if(admissionId!=null)
							inner.add(Restrictions.eq("admissionId", admissionId));
						if(encounterId!=null)
							inner.add(Restrictions.eq("encounterId", encounterId));
						
						inner.add(Restrictions.eq("patientSponsorId", master.getPatientSponsorId()));
						inner.add(Restrictions.eq("status",'A'));
						inner.add(Restrictions.eq("isFinalSubmit",'Y'));
						
						List<MPreauthRequestMaster> preauthRequestMasterList = (List<MPreauthRequestMaster>)inner.list();
						
						for(MPreauthRequestMaster preauthRequestMaster : preauthRequestMasterList)
						{
							
							if(preauthRequestMaster.getAuthorizationAmount()<=consumedAmount)
							{	
								preauthRequestMaster.setIsSplitStatus('Y');
								preauthRequestMaster.setUtilizedAmount(preauthRequestMaster.getAuthorizationAmount());
							}else{
								preauthRequestMaster.setUtilizedAmount(consumedAmount);
							}
							consumedAmount = consumedAmount - preauthRequestMaster.getAuthorizationAmount();
							sessionFactory.getCurrentSession().update(preauthRequestMaster);
						}
						
					}
					
				}
				
				
			}
			
			
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
		return new Response<>(ERROR, ERROR, COMMON_ERROR_MESSAGE, null, null);
	}



	@Override
	@Transactional
	public Response getPatientDocumentNameForPreAuthorization(MPreauthRequestMasterDto patientDocReqDto)
			throws ApplicationException {
		try {
		String query = " select "
				+ "   mprm.preauth_request_id as \"preauthRequestId\","
				+ "   mprm.document_name as \"documentName\" "
				+ " FROM"
				+ "   billing.m_preauth_request_master mprm"
				+ " WHERE"
				+ "   mprm.preauth_request_id =:preauthRequestId "
				+ "  and mprm.unit_id=:unitId"
				+ "  and mprm.patient_id=:patientId";
		
		
		List<MPreauthRequestMasterDto> listMPreauthRequestMasterDto = sessionFactory.getCurrentSession()
				   .createSQLQuery(query.toString())
				   .setParameter("preauthRequestId", patientDocReqDto.getPreauthRequestId())
					.setParameter("unitId", patientDocReqDto.getUnitId())
					.setParameter("patientId", patientDocReqDto.getPatientId())
				   .setResultTransformer(Transformers.aliasToBean(MPreauthRequestMasterDto.class)).list();
		
		
		return new Response<>(SUCCESS, SUCCESS_CODE, null, null, listMPreauthRequestMasterDto);

	} catch (HibernateException he) {
		throw new ApplicationException(he.getMessage(), he);
	} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
}

	
}
