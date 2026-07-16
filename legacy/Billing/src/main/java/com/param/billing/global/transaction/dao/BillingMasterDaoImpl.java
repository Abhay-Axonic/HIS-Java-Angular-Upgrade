package com.param.billing.global.transaction.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.adt.admission.model.AdmissionDetails;
import com.param.billing.common.IError;
import com.param.billing.global.dto.BillListReqDto;
import com.param.billing.global.dto.BillListResDto;
import com.param.billing.global.smartcard.dto.Claim;
import com.param.billing.global.smartcard.dto.ItemDetailsResponseDto;
import com.param.billing.global.smartcard.dto.SmartAdmitionReqDto;
import com.param.billing.global.smartcard.dto.SmartCardBillReqDto;
import com.param.billing.global.smartcard.dto.SmartCardDiagnosis;
import com.param.billing.global.smartcard.dto.SmartCardIItemsReqDto;
import com.param.billing.global.smartcard.dto.SmartCardPreauthReqDto;
import com.param.billing.global.smartcard.dto.SmartCardQuries;
import com.param.billing.global.smartcard.dto.SmartCartDischargeSummeryDto;
import com.param.billing.global.smartcard.dto.SmartDiagnosisReqDto;
import com.param.billing.global.transaction.config.dto.MCompanyInvoiceMasterDto;
import com.param.billing.global.transaction.config.dto.TCompanyInvoiceCreationReqDto;
import com.param.billing.global.transaction.config.dto.TCompanyInvoiceDetailsDto;
import com.param.billing.global.transaction.config.dto.TCompanyInvoiceServiceDetailsDto;
import com.param.billing.global.transaction.dto.BillDetailsByBillIdDto;
import com.param.billing.global.transaction.dto.BillListCNDto;
import com.param.billing.global.transaction.dto.CancelOrderedServicesDto;
import com.param.billing.global.transaction.dto.CompanyReceiptDetailDto;
import com.param.billing.global.transaction.dto.DiscountTypeDto;
import com.param.billing.global.transaction.dto.SmartCardMasterDto;
import com.param.billing.global.transaction.dto.TDoctorReferralShareDto;
import com.param.billing.global.transaction.model.BillingDetails;
import com.param.billing.global.transaction.model.BillingMaster;
import com.param.billing.global.transaction.model.MPreauthRequestMaster;
import com.param.entity.model.master.DiscountType;
import com.param.global.billing.controller.BillingException;
import com.param.global.billing.dto.BillingDetailsDto;
import com.param.global.billing.dto.PaymentReceiptDetailsDto;
import com.param.global.billing.dto.PaymentRecieptMasterDto;
import com.param.global.billing.dto.RequestDiscountDto;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.IBillingStatusMasterConstant;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dto.AdmissionDto;
import com.param.global.dto.BillPayeeDetailsDto;
import com.param.global.dto.BillingMasterDto;
import com.param.global.dto.DischargeDto;
import com.param.global.dto.FreeTextOrderedServicesDto;
import com.param.global.dto.OrdeDetailsPackageMapperLogDto;
import com.param.global.dto.OrderDetailsByEncounterIdDto;
import com.param.global.dto.OrderDetailsMasterDto;
import com.param.global.dto.OrderDetailsPackageMapperDto;
import com.param.global.dto.ServiceMasterDto;
import com.param.global.dto.SlotMasterDto;
import com.param.global.dto.SmartcardClaimDto;
import com.param.global.dto.TPackageConsumptionCapDetailsDto;
import com.param.global.pharmacy.dto.SaleDetailPayeeMapperDto;
import com.param.global.service.OrderMasterServiceImpl;
import com.param.global.unit.dto.CabinMasterDto;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import static java.lang.Integer.parseInt;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
public class BillingMasterDaoImpl extends GenericDao<BillingMaster, Integer> implements IBillingMasterDao,
		com.param.billing.common.ICommonConstants, IError, IBillingStatusMasterConstant, SmartCardQuries {

	final static Logger logger = Logger.getLogger(BillingMasterDaoImpl.class);
	public BillingMasterDaoImpl() {
		super(BillingMaster.class);
	}

	String xmlp = "/xmlfolder/";
	public static final String xmlFilePath = "C:\\Users\\admin\\Documents\\employee.xml";

	@Autowired
	private DozerBeanMapper mapper;

	@Autowired
	private SessionService sessionService;

	@Autowired
	private IGlobalDao iGlobalDao;

	@Autowired
	private SessionFactory SessionFactory;

	@Autowired
	private IBillingDetailsDao iBillingDetailsDao;

	@Override
	public Response saveBillingMaster(BillingMasterDto billingMasterDto) throws ApplicationException {
		try {
			BillingMaster billingMaster = mapper.map(billingMasterDto, BillingMaster.class,
					"billing_BillingMasterDto_to_BillingMaster");
			billingMaster.setCreatedDate(TimeZoneComponent.getDateByzone(billingMasterDto.getUnitId()).getTime());
			billingMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(billingMasterDto.getUnitId()).getTime());
			// billingMaster.setBillDateTime(TimeZoneComponent.getDateByzone(unitId).getTime());
			if (billingMasterDto.getNetAmount().intValue() == 0)
				billingMaster.setBillStatusId(SETTLED);

			else if ((billingMasterDto.getSelfPayable().equals(0.00) && (billingMasterDto.getCreditPayable() > 0.00)))
				billingMaster.setBillStatusId(ONHOLD);

			else if ( billingMasterDto.getIsFinancialClearance() == 'Y')
				
				if( billingMasterDto.getFinancialRequestSendTo()!=null ) {
					billingMaster.setBillStatusId(PENDING);
				}
				else {
					billingMaster.setBillStatusId(ONHOLD);
				}

			else
				billingMaster.setBillStatusId(PENDING);
			billingMaster.setStatus('A');
			billingMaster.setIsDrNote(billingMaster.getIsDrNote()=='\u0000'?'N':billingMaster.getIsDrNote());
			billingMaster = save(billingMaster);
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, billingMaster.getBillingMasterId());
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	public Response getBillDetailsForPayment(BillingMasterDto billingMasterDto) throws ApplicationException {
		try {
			billingMasterDto = (BillingMasterDto) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BILL_DETAILS_FOR_PAYMENT")
					.setParameter("billMasterId", billingMasterDto.getBillingMasterId())
					.setResultTransformer(Transformers.aliasToBean(BillingMasterDto.class)).uniqueResult();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, billingMasterDto);
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	public Response updateBillSummaryAmount(BillingMasterDto billingMasterDto) throws ApplicationException {
		try {
			String query = " UPDATE billing.m_billing_master SET net_amount =:netAmount "
					+ " , concession_amount =:concessionAmount , total_bill_amount =:totalBillAmount "
					+ " , outstanding =:outstanding "
					+ ", bill_status_id =:billStatusId WHERE billing_master_id =:billingMasterId ";

			SQLQuery sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query);

			sqlQuery.setParameter("netAmount", billingMasterDto.getNetAmount())
					.setParameter("concessionAmount", billingMasterDto.getConcessionAmount())
					.setParameter("totalBillAmount", billingMasterDto.getTotalBillAmount())
					.setParameter("outstanding", billingMasterDto.getOutstanding())
					.setParameter("billStatusId", billingMasterDto.getBillStatusId())
					.setParameter("billingMasterId", billingMasterDto.getBillingMasterId())
					.executeUpdate();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	public Response getBillMasterById(Integer billMasterId) throws ApplicationException {
		try {
			BillingMasterDto billingMasterDto = (BillingMasterDto) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BILL_MASTER_BY_ID").setParameter("billMasterId", billMasterId)
					.setResultTransformer(Transformers.aliasToBean(BillingMasterDto.class)).uniqueResult();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, billingMasterDto);
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	public Response getAutoRenderedServicesByPatient(ServiceMasterDto serviceMasterDto) throws ApplicationException {
		try {
			List<ServiceMasterDto> listServiceMasterDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_AUTO_RENDERED_SERVICES_BY_PATIENT")
					.setParameter("patientId", serviceMasterDto.getPatientId())
					.setParameter("unitId", serviceMasterDto.getUnitId())
					.setParameter("orgId", serviceMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(ServiceMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listServiceMasterDto, null);
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	public Response getBillsByFilterForBillList(BillListReqDto reqDto) throws BillingException {
		try {

			StringBuilder query = new StringBuilder(" select " 
					 + " 	bill.billing_master_id as \"billingMasterId\", " 
					 + " 	to_char(bill.bill_date_time, 'DD-MM-YYYY HH24:MI:SS') as \"billDate\", " 
					 + " 	bill.bill_date_time as \"billDateNew\", " 
					 + " 	bill.bill_number as \"billNumber\", " 
					 + " 	pat.uhid_number as \"uhidNumber\", " 
					 + " 	pat.mobile_number as \"mobileNo\", " 
					 + " 	concat(pat.first_name,'',pat.last_name) as \"firstName\", " 
					 + " 	pat.country_calling_code as \"countryCallingCode\", " 
					 + " 	concat(coalesce(pre.prefix_code, ''), ' ', pat.first_name, ' ', pat.middle_name, ' ', pat.last_name) as \"patientName\", " 
					 + " 	to_char(pat.dob, 'DD/MM/YYYY') as \"birthDate\", " 
					 + " 	trim(to_char(extract(year from age(current_timestamp, pat.dob)), '999')) as \"ageString\", " 
					 + " 	pat.image_path as \"imagePath\", " 
					 + " 	pat.gender_id as \"genderId\", " 
					 + " 	bill.visit_type_id as \"visitTypeId\", " 
					 + " 	(case " 
					 + " 		when empmst.employee_id is not null then concat(empmst.first_name, ' ', empmst.last_name) " 
					 + " 		when docmst.doctor_id is not null then concat('Dr. ', docmst.first_name, ' ', docmst.last_name) " 
					 + " 		else 'System' " 
					 + " 	end) as \"userName\", " 
					 + " 	pat.is_otc_reg as \"isOtcReg\", " 
					 + " 	bill.organisation_id as \"orgId\", " 
					 + " 	bill.unit_id as \"unitId\", " 
					 + " 	bill.patient_id as \"patientId\", " 
					 + " 	pat.email as \"email\", " 
					 + " 	bill.is_dr_note as \"isDrNote\", " 
					 + " 	spl.is_nursing_assessment_required as \"isNursingAssessmentRequired\", " 
					 + " 	bill.disallowed_amount as \"disallowedAmount\", " 
					 + " 	bill.disallowed_against_invoice_dtl_id as \"disallowedAgainstInvoiceDtlsId\", " 
					 + " 	coalesce( case when bill.visit_type_id = 1 then (case when enc.doctor_id is not null then enc.doctor_id else enc.preferred_doc_id end ) when bill.visit_type_id != 1 then adm.doctor_id end ) as \"doctorId\", " 
					 + " 	coalesce( case when bill.visit_type_id = 1 then enc.encounter_number when bill.visit_type_id != 1 then adm.admission_number end ) as \"visitNo\", " 
					 + " 	coalesce( case when bill.visit_type_id = 1 then to_char(enc.encounter_date, 'DD/MM/YYYY') when bill.visit_type_id != 1 then to_char(adm.created_date, 'DD/MM/YYYY') end ) as \"visitDate\", " 
					 + " 	coalesce( case when bill.visit_type_id = 1 then enc.encounter_date when bill.visit_type_id != 1 then adm.created_date end ) as \"visitDateNew\", " 
					 + " 	bill.total_bill_amount as \"totalBillAmount\", " 
					 + " 	bill.net_amount as \"netAmount\", " 
					 + " 	coalesce((bill.net_amount - bill.outstanding), 0) as \"amountPaid\", " 
					 + " 	cast(coalesce(round(cast(bill.outstanding as numeric), 2), 0) as double precision) as \"outstanding\", " 
					 + " 	bill.bill_status_id as \"billStatusId\", " 
					 + " 	bill.is_interim_or_final_bill as \"isInterimOrFinalBill\", " 
					 + " 	bill.total_self_payable as \"totalSelfPayable\", " 
					 + " 	bill.total_credit_payable as \"totalCreditPayable\", " 
					 + " 	payee.due_amount as \"selfDueAmount\", " 
					 + " 	bill.is_print as \"isPrint\", " 
//					 + " 	billPayee.payment_entitlement_id as \"paymentEntitlementId\", "
//					 + " 	(case "
//					 + " 		when billPayee.associate_company_id > 0 then  "
//					 + " 			insurance.insurance_desc "
//					 + " 		else  "
//					 + " 			tcm.company_desc "
//					 + " 	end ) as \"associateCompanyDesc\", "
					 + " 	genmstr.gender_code as \"genderCode\", " 
					 + " 	concat(coalesce(mdm.first_name, ''), ' ', coalesce(mdm.middle_name, ''), ' ', coalesce(mdm.last_name, '')) as \"doctorName\", " 
					 + " 	speciaityMaster.speciality_name as \"specialityName\", "
                     + "    am.appointment_id as \"appointmentId\" "
					 + " from " 
					 + " 	billing.m_billing_master bill " 
					 + " inner join t_unit_application_billing_configuration tuabc on " 
					 + " 	tuabc.unit_id = bill.unit_id "
					/* + " left join (    " 
					 + "    select    " 
					 + "      min(bpd.priority_id) as bpid,    "
					 + "      bpd.bill_id    "  
					 + "    from    " 
					 + "      billing.t_bill_payee_details bpd    "  
					 + "    group by    "  
					 + "      bpd.bill_id    " 
					 + "  ) SS on bill.billing_master_id = SS.bill_id    "*/ 
//					 + "  left join billing.t_bill_payee_details billPayee on bill.billing_master_id = billPayee.bill_id    "
//					 + "  and billPayee.priority_id = 1"
					 /*+ " left join billing.t_bill_payee_details billPayee on " 
					 + " 	bill.billing_master_id = billPayee.bill_id " 
					 + " 	and billPayee.priority_id = ( " 
					 + " 	select " 
					 + " 		min(bpd.priority_id) " 
					 + " 	from " 
					 + " 		billing.t_bill_payee_details bpd " 
					 + " 	where " 
					 + " 		bill.billing_master_id = bpd.bill_id) " */
					 + " left join billing.t_bill_payee_details payee on " 
					 + " 	bill.billing_master_id = payee.bill_id " 
					 + " 	and payee.payee_id = tuabc.self_payee_id " 
					 + " inner join patient.t_patient_registration pat on " 
					 + " 	pat.patient_id = bill.patient_id " 
					 + " left join m_prefix_master pre on " 
					 + " 	pre.prefix_id = pat.prefix_id " 
					 + " inner join public.m_gender_master genmstr on " 
					 + " 	genmstr.gender_id = pat.gender_id " 
					 + " left join PUBLIC.m_employee_master empmst on " 
					 + " 	bill.created_by = empmst.user_id " 
					 + " left join doctor.m_doctor_master docmst on " 
					 + " 	bill.created_by = docmst.user_id " 
					 + " left join opd.t_encounter_master enc on " 
					 + " 	enc.encounter_id = bill.encounter_id " 
					 + " left join adt.t_admission adm on " 
					 + " 	adm.admission_id = bill.admission_id " 
					 + " left join t_unit_speciality_mapper spl on " 
					 + " 	spl.speciality_id = enc.dept_id " 
					 + " 	and spl.unit_id = enc.unit_id " 
					 + " left join opd.m_appointment_master am on " 
					 + " 	am.appointment_id = enc.appointment_id " 
					 + " left join opd.m_slot_master sm on " 
					 + " 	am.slot_id = sm.slot_id " 
					 + " left join doctor.m_doctor_master mdm on " 
					 + " 	mdm.doctor_id = " 
					 + " 	(case " 
					 + " 		when enc.doctor_id is not null then enc.doctor_id " 
					 + " 		when enc.preferred_doc_id is not null then enc.preferred_doc_id " 
					 + " 		else adm.doctor_id " 
					 + " 	end) " 
					 + " left outer join doctor.m_speciality_master speciaityMaster on " 
					 + " 	speciaityMaster.speciality_id = " 
					 + " 	(case " 
					 + " 		when enc.encounter_id is not null then enc.dept_id " 
					 + " 		else adm.speciality_id " 
					 + " 	end) " 
//					 + " left join adt.t_insurance insurance on "
//					 + " 	insurance.insurance_id = billPayee.associate_company_id "
//					 + " left join public.t_company_master tcm on "
//					 + " 	tcm.company_id = billPayee.payee_id "
					 + " where " 
					 + " 	bill.bill_cancelled = 'N' " 
					 + " 	and bill.is_dr_note = 'N' " 
					 + " 	and bill.organisation_id =:orgId " 
					 + " 	and bill.unit_id =:unitId ");



			if (reqDto.getFromDate() != null && reqDto.getToDate() != null) 
				query.append(" and to_char(bill.bill_date_time,'YYYY-MM-DD') between :fromDate and :toDate ");


			if (reqDto.getVisitTypeId() != null)
				query.append(" and bill.visit_type_id =:visitTypeId ");

			if(reqDto.getBillNo() != null && !reqDto.getBillNo().isEmpty())
				query.append(" and trim(lower(bill.bill_number))like :billNo ");

			if(reqDto.getVisitNo() != null && !reqDto.getVisitNo().isEmpty())
				query.append(" and (trim(lower(enc.encounter_number)) like :visitNo or trim(lower(adm.admission_number)) like :visitNo ");

			if (reqDto.getBillStatusId() != null)
				query.append(" and bill.bill_status_id =:billStatusId ");

			if (reqDto.getPatientId() != null)
				query.append(" and bill.patient_id =:patientId ");

			if (reqDto.getAdmissionId() != null)
				query.append(" and bill.admission_id =:admissionId ");

			if (reqDto.getDoctorId() != null) 
				query.append(" and (case when bill.visit_type_id = 1 then enc.doctor_id when bill.visit_type_id != 1 then adm.doctor_id end ) =:doctorId ");

			query.append(" order by bill.bill_date_time desc ");

			List<BillListResDto> listBills = null;
			Query sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
					.setInteger("orgId", reqDto.getOrgId() != null ? reqDto.getOrgId() : 0)
					.setInteger("unitId", reqDto.getUnitId() != null ? reqDto.getUnitId() : 0);

			if (reqDto.getFromDate() != null && reqDto.getToDate() != null)
			{
				Date fromDate = reqDto.getFromDate() != null && !reqDto.getFromDate().isEmpty()
						? GlobalCommonDateUtils.getDate(reqDto.getFromDate(), "dd-MM-yyyy")
						: GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(reqDto.getUnitId() != null ? reqDto.getUnitId() : 0),
						"dd-MM-yyyy");

				Date toDate = reqDto.getToDate() != null && !reqDto.getToDate().isEmpty()
						? GlobalCommonDateUtils.getDate(reqDto.getToDate(), "dd-MM-yyyy")
						: GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(reqDto.getUnitId() != null ? reqDto.getUnitId() : 0),
						"dd-MM-yyyy");

				sqlQuery.setParameter("fromDate", GlobalCommonDateUtils.getStringDate(fromDate, "yyyy-MM-dd"));
				sqlQuery.setParameter("toDate", GlobalCommonDateUtils.getStringDate(toDate, "yyyy-MM-dd"));
			}

			if (reqDto.getVisitTypeId() != null)
				sqlQuery.setParameter("visitTypeId", reqDto.getVisitTypeId());

			if(reqDto.getBillNo() != null && !reqDto.getBillNo().isEmpty())
				sqlQuery.setParameter("billNo", "%"+reqDto.getBillNo().toLowerCase().trim()+"%");

			if(reqDto.getVisitNo() != null && !reqDto.getVisitNo().isEmpty())
				sqlQuery.setParameter("visitNo", "%"+reqDto.getVisitNo().toLowerCase().trim()+"%");

			if (reqDto.getBillStatusId() != null)
				sqlQuery.setParameter("billStatusId", reqDto.getBillStatusId());

			if (reqDto.getPatientId() != null)
				sqlQuery.setParameter("patientId", reqDto.getPatientId());

			if (reqDto.getAdmissionId() != null)
				sqlQuery.setParameter("admissionId", parseInt(reqDto.getAdmissionId()));

			if (reqDto.getDoctorId() != null)
				sqlQuery.setParameter("doctorId", reqDto.getDoctorId());

			listBills = sqlQuery.setResultTransformer(Transformers.aliasToBean(BillListResDto.class)).list();

			if(listBills != null && !listBills.isEmpty()) {

				List<Integer> billIds = listBills.stream().map(BillListResDto::getBillingMasterId)
						.collect(Collectors.toList());

				StringBuilder billPayeeQuery1 = new StringBuilder("select " +
						" billPayee.payment_entitlement_id as \"paymentEntitlementId\", " +
						" billPayee.bill_id as \"billingMasterId\", " +
						" (case " +
						" when billPayee.associate_company_id > 0 then insurance.insurance_desc " +
						" else tcm.company_desc " +
						" end ) as \"associateCompanyDesc\" " +
						" from billing.t_bill_payee_details billPayee " +
						" left join adt.t_insurance insurance on " +
						" insurance.insurance_id = billPayee.associate_company_id " +
						" left join public.t_company_master tcm on " +
						" tcm.company_id = billPayee.payee_id " +
						" where billPayee.bill_id in (:billIds) " +
						" order by billPayee.priority_id  asc limit 1 "
				);

				StringBuilder billPayeeQuery = new StringBuilder("SELECT DISTINCT ON (billPayee.bill_id) " +
						"    billPayee.payment_entitlement_id AS \"paymentEntitlementId\", " +
						"    billPayee.bill_id AS \"billingMasterId\", " +
						"    (case " +
						"    when billPayee.associate_company_id > 0 then insurance.insurance_desc " +
						"    else tcm.company_desc " +
						"    end ) as \"associateCompanyDesc\" " +
						"    FROM " +
						"    billing.t_bill_payee_details billPayee " +
						"    LEFT JOIN adt.t_insurance insurance  " +
						"    ON insurance.insurance_id = billPayee.associate_company_id " +
						"    LEFT JOIN public.t_company_master tcm  " +
						"    ON tcm.company_id = billPayee.payee_id " +
						"    WHERE " +
						"    billPayee.bill_id IN (:billIds) "
				);

				if (reqDto.getAssociateCompanyId() != null && reqDto.getAssociateCompanyId() > 0)
					billPayeeQuery.append("	and billPayee.associate_company_id =:associateCompanyId ");

				if (reqDto.getPayeeId() != null && reqDto.getPayeeId() > 0 && (reqDto.getAssociateCompanyId() == null || reqDto.getAssociateCompanyId() == 0))
					billPayeeQuery.append("	and billPayee.payee_id =:payeeId ");

				billPayeeQuery.append(" ORDER BY " +
						"    billPayee.bill_id, " +
						"    billPayee.priority_id ASC ");

				List<BillListResDto> listPayees = null;
				Query billPayeeQuerySqlQuery = sessionFactory.getCurrentSession().createSQLQuery(billPayeeQuery.toString())
						.setParameterList("billIds", billIds);

				if (reqDto.getAssociateCompanyId() != null && reqDto.getAssociateCompanyId() > 0)
					billPayeeQuerySqlQuery.setParameter("associateCompanyId", reqDto.getAssociateCompanyId());

				if (reqDto.getPayeeId() != null && reqDto.getPayeeId() > 0 && (reqDto.getAssociateCompanyId() == null || reqDto.getAssociateCompanyId() == 0))
					billPayeeQuerySqlQuery.setParameter("payeeId", reqDto.getPayeeId());

				listPayees = billPayeeQuerySqlQuery
						.setResultTransformer(Transformers.aliasToBean(BillListResDto.class)).list();
				if(listPayees != null && !listPayees.isEmpty()) {
					for (BillListResDto bill : listBills) {
						bill.setPaymentEntitlementId(listPayees.stream()
								.filter(x -> x.getBillingMasterId().equals(bill.getBillingMasterId())).collect(Collectors.toList())
								.getFirst().getPaymentEntitlementId());
						bill.setAssociateCompanyDesc(listPayees.stream()
								.filter(x -> x.getBillingMasterId().equals(bill.getBillingMasterId())).collect(Collectors.toList())
								.getFirst().getAssociateCompanyDesc());
					}
				}
			}

			return new Response<>(SUCCESS, SUCCESS_CODE, null, listBills, null);
		} catch (Exception e) {
			
			throw e;
			//throw new BillingException(e.getMessage());
		}
	}

	@Override
	public Response getOrderedServicesByEncounterId(ServiceMasterDto serviceMasterDto) throws ApplicationException {
		try {

			List<ServiceMasterDto> listServiceMasterDto = null;

			StringBuilder stringQuery = new StringBuilder(sessionFactory.getCurrentSession()
					.getNamedQuery("GET_RENDERED_SERVICES_BY_ENCOUNTER_ID").getQueryString());

			if (serviceMasterDto.getSearchKeyword() != null && serviceMasterDto.getSearchKeyword().length() > 0)
				stringQuery
						.append(" AND (LOWER(service.serviceStandardName) like :searchKeyword OR LOWER(ord.orderNo) like :searchKeyword )");

			stringQuery.append(" AND orderDetails.debitNoteService =:debitNoteService ");

			if(!serviceMasterDto.getIsBillManipulationAllowed())
				stringQuery.append(" AND orderDetails.isDummyService=false");
			
			
			if (serviceMasterDto.getOrderDetailsIds() != null && serviceMasterDto.getOrderDetailsIds().size() > 0) {
				stringQuery.append(" AND orderDetails.orderDetailsId in (:ids) ");
				stringQuery.append(" order by  orderDetails.orderDetailsId ");

				 Query sqlQuery = sessionFactory.getCurrentSession().createQuery(stringQuery.toString())
						.setParameter("encounterId", serviceMasterDto.getEncounterId())
						.setParameter("visitTypeId", serviceMasterDto.getVisitTypeId())
						.setParameterList("ids", serviceMasterDto.getOrderDetailsIds())
						.setParameter("debitNoteService", serviceMasterDto.getDebitNoteService());

				if (serviceMasterDto.getSearchKeyword() != null && serviceMasterDto.getSearchKeyword().length() > 0)
					sqlQuery.setParameter("searchKeyword", "%" + serviceMasterDto.getSearchKeyword()+ "%");

				 listServiceMasterDto = sqlQuery
						.setResultTransformer(Transformers.aliasToBean(OrderDetailsByEncounterIdDto.class)).list();

			} else {

				stringQuery.append(" order by  orderDetails.orderDetailsId ");

				Query sqlQuery = sessionFactory.getCurrentSession().createQuery(stringQuery.toString())
						.setParameter("encounterId", serviceMasterDto.getEncounterId())
						.setParameter("visitTypeId", serviceMasterDto.getVisitTypeId())
						.setParameter("debitNoteService", serviceMasterDto.getDebitNoteService());

				if (serviceMasterDto.getSearchKeyword() != null && serviceMasterDto.getSearchKeyword().length() > 0)
					sqlQuery.setParameter("searchKeyword", "%" + serviceMasterDto.getSearchKeyword()+ "%");

				listServiceMasterDto = sqlQuery
						.setResultTransformer(Transformers.aliasToBean(OrderDetailsByEncounterIdDto.class)).list();

			}

			return new Response<>(SUCCESS, SUCCESS_CODE, null, listServiceMasterDto, null);
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	public Response getOrderedServicesByEncounterIdForPreBilling(ServiceMasterDto serviceMasterDto)
			throws ApplicationException {
		try {

			List<ServiceMasterDto> listServiceMasterDto = null;

			StringBuilder stringQuery = new StringBuilder(sessionFactory.getCurrentSession()
					.getNamedQuery("GET_RENDERED_SERVICES_BY_ENCOUNTER_ID").getQueryString());
			stringQuery.append(" AND orderDetails.debitNoteService =:debitNoteService ");
			stringQuery.append(" order by  orderDetails.orderDetailsId ");
			listServiceMasterDto = sessionFactory.getCurrentSession().createQuery(stringQuery.toString())
					.setParameter("encounterId", serviceMasterDto.getEncounterId())
					.setParameter("visitTypeId", serviceMasterDto.getVisitTypeId())
					.setParameter("debitNoteService", serviceMasterDto.getDebitNoteService())
					.setResultTransformer(Transformers.aliasToBean(OrderDetailsByEncounterIdDto.class)).list();

			return new Response<>(SUCCESS, SUCCESS_CODE, null, listServiceMasterDto, null);
		} catch (Exception e) {
			
			throw e;
		}
//		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	public Response getRenderedServicesByEncounterIdForBillSave(Integer encounterId, Integer visitTypeId,
			Boolean debitNoteService, Character isPreBilling) throws ApplicationException {
		try {

			StringBuilder stringQuery = new StringBuilder();

			if (isPreBilling == 'Y' && visitTypeId == 1) {
				stringQuery = new StringBuilder(sessionFactory.getCurrentSession()
						.getNamedQuery("GET_RENDERED_SERVICES_BY_ENCOUNTER_ID").getQueryString());
			} else {
				stringQuery = new StringBuilder(sessionFactory.getCurrentSession()
						.getNamedQuery("GET_RENDERED_SERVICES_BY_ENCOUNTER_ID").getQueryString());
			}
			stringQuery.append(" AND orderDetails.debitNoteService =:debitNoteService ");
			stringQuery.append(" ORDER BY orderDetails.orderDetailsId ");

			List<ServiceMasterDto> listServiceMasterDto = sessionFactory.getCurrentSession()
					.createQuery(stringQuery.toString()).setInteger("encounterId", encounterId)
					.setParameter("visitTypeId", visitTypeId)
					.setParameter("debitNoteService", debitNoteService)
					.setResultTransformer(Transformers.aliasToBean(OrderDetailsByEncounterIdDto.class)).list();

			return new Response<>(SUCCESS, SUCCESS_CODE, null, listServiceMasterDto, null);

		} catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	public Response getActiveDiscountCategories(Integer orgId, Integer unitId) throws ApplicationException {
		try {
			List<DiscountType> listServiceMasterDto = sessionFactory.getCurrentSession().createSQLQuery(
					"SELECT id as \"id\", category as \"type\" FROM public.m_discount_category WHERE organization_id =:orgId "
							+" AND unit_id =:unitId ORDER BY category ASC")
					.setParameter("orgId", orgId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(DiscountTypeDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listServiceMasterDto, null);
		} catch (Exception e) {
			
			throw e;
		}
	//	return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	public Response getBillDetailsByBillId(Integer billId) throws ApplicationException {
		try {
            AdmissionDto dto = new AdmissionDto();
			List<BillDetailsByBillIdDto> detailsByBillIdDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BILL_DETAILS_BY_BILL_ID").setParameter("billId", billId)
					.setResultTransformer(Transformers.aliasToBean(BillDetailsByBillIdDto.class)).list();

			return new Response<>(SUCCESS, SUCCESS_CODE, null, detailsByBillIdDtosList, null);
		} catch (Exception e) {
			
			throw e;
		}
	//	return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	public Response updateBillMstIsDoctorReferralPostedByBillId(TDoctorReferralShareDto tDocShareVoucher)
			throws ServiceException {
		try {
			BillingMaster billingMaster = findById(tDocShareVoucher.getBillId());
			if (billingMaster.getBillingMasterId() != 0) {
				billingMaster.setIsDoctorReferralPosted('Y');
				billingMaster = update(billingMaster);
				return new Response<>(SUCCESS, SUCCESS_CODE, null, null, tDocShareVoucher);
			} else {

				return new Response<>(ERROR, ERROR, COMMON_ERROR_MESSAGE, null, tDocShareVoucher);
			}

		} catch (Exception e) {
			
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateDischargeStatusIfFinalBill(int admissionId, int statusId) throws BillingException {
		try {
			Integer result = sessionFactory.getCurrentSession()
					.getNamedQuery("UPDATE_DISCHARGE_STATUS_BY_ADMISSION_ID_IF_FINAL_BILL")
					.setParameter("statusId", statusId).setParameter("admissionId", admissionId).executeUpdate();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, result);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response billListByPatientId(int orgId, int unitId, int patientId, char isDrNote)
			throws ApplicationException {
		try {

			if (isDrNote == 'Y') {
				List<BillListCNDto> listBillListCNDto = sessionFactory.getCurrentSession()
						.getNamedQuery("GET_BILL_DETAILS_FOR_BILL_LIST_PATIENT").setParameter("patientId", patientId)
						.setParameter("orgId", orgId).setParameter("unitId", unitId)
						.setParameterList("billStatusIds", Arrays.asList(2, 3))
						.setParameterList("intremediateFinal", Arrays.asList(2))
						.setParameterList("isDrCr", Arrays.asList('N'))
						.setResultTransformer(Transformers.aliasToBean(BillListCNDto.class)).list();

				List<BillListCNDto> filteredDebitNoteList = listBillListCNDto.stream()
						.filter(bill -> bill.getStatus() == 'A').collect(Collectors.toList());
				return new Response<>(SUCCESS, SUCCESS_CODE, null, filteredDebitNoteList, null);
			} else if (isDrNote == 'N') {
				List<BillListCNDto> listBillListCNDto = sessionFactory.getCurrentSession()
						.getNamedQuery("GET_BILL_DETAILS_FOR_BILL_LIST_PATIENT_CR").setParameter("patientId", patientId)
						.setParameter("orgId", orgId).setParameter("unitId", unitId)
						.setParameterList("billStatusIds", Arrays.asList(2, 3))
						.setParameterList("isDrCr", Arrays.asList('Y', 'N'))
						.setResultTransformer(Transformers.aliasToBean(BillListCNDto.class)).list();
				return new Response<>(SUCCESS, SUCCESS_CODE, null, listBillListCNDto, null);
			}
			return new Response<>(ERROR, COMMON_ERROR_CODE, null, null, null);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response getBillsForCreditNote(int orgId, int unitId, int patientId) throws ApplicationException {
		try {
				List<BillListCNDto> listBillListCNDto = sessionFactory.getCurrentSession()
						.getNamedQuery("GET_BILLS_FOR_CREDIT_NOTE")
						.setParameter("patientId", patientId)
						.setParameter("orgId", orgId)
						.setParameter("unitId", unitId)
						.setParameterList("billStatusIds", Arrays.asList(1,2,3))
						.setParameterList("isDrCr", Arrays.asList('Y', 'N'))
						.setResultTransformer(Transformers.aliasToBean(BillListCNDto.class)).list();
				return new Response<>(SUCCESS, SUCCESS_CODE, null, listBillListCNDto, null);
		} catch (Exception e) {
			
			throw e;
		}
	}

	public Response getIntermediateBillDetailsByAdmissionId(int admissionId) throws BillingException {
		try {
			List<BillingMasterDto> listBillingMasterDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_INTERIM_BILL_DETAILS_BY_ADMISSION_ID").setParameter("admissionId", admissionId)
					.setResultTransformer(Transformers.aliasToBean(BillingMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listBillingMasterDto, null);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response updateBillMasterIsInvoiceCreationByBillId(Integer billId, Integer payeeId, Integer assoiciateComId)
			throws ServiceException {
		try {

			StringBuilder query = new StringBuilder("update billing.t_bill_payee_details "
					+ "set is_invoice_generated='Y' " + "where bill_id =:billId and payee_id =:payeeId ");

			if (assoiciateComId != null && assoiciateComId > 0)
				query.append(" and associate_company_id =:associateComId ");

			Query sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
					.setParameter("billId", billId)
					.setParameter("payeeId", payeeId);

			if (assoiciateComId != null && assoiciateComId > 0)
				sqlQuery.setParameter("associateComId", assoiciateComId);

			sqlQuery.executeUpdate();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		} catch (Exception e) {
			
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateOutStandingByBillIdForCompanyInvoiceSett(int billId, double amount, Integer updatedBy)
			throws BillingException {
		try {
			Integer result = sessionFactory.getCurrentSession()
					.getNamedQuery("UPDATE_OUTSTANDING_AMOUNT_BY_BILL_ID_FOR_COMPANY_INVOICE_SETTLEMENT")
					.setParameter("billId", billId).setInteger("updatedBy", updatedBy).setParameter("amount", amount)
					.executeUpdate();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, result);
		} catch (Exception e) {
			
			throw e;
		}
	}

	// Ganesh
	@Override
	public Response updatePharmacyOutStandingByBillIdForCompanyInvoiceSett(int saleId, double amount, Integer updatedBy)
			throws BillingException {
		try {
			Integer result = sessionFactory.getCurrentSession()
					.getNamedQuery("UPDATE_PHARAMCY_OUTSTANDING_AMOUNT_BY_BILL_ID_FOR_COMPANY_INVOICE_SETTLEMENT")
					.setParameter("saleId", saleId).setInteger("updatedBy", updatedBy).setParameter("amount", amount)
					.executeUpdate();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, result);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public boolean checkOutstandingIsClearedForCompanyInvoice(int billId, double reciveAmount) throws BillingException {
		try {
			boolean result = (boolean) sessionFactory.getCurrentSession()
					.getNamedQuery("CHECK_OUTSTANDING_IS_ZERO_FOR_SETTLEMENT_OF_BILL").setParameter("billId", billId)
					.uniqueResult();
			
			return result;
		} catch (Exception e) {
			
			throw e;
		}
	}

	public Response getDebitNoteFilterList(BillListReqDto reqDto) throws BillingException {
		try {
			boolean dateFlag = false;

			Date fromDate = reqDto.getFromDate() != null && !reqDto.getFromDate().isEmpty()
					? GlobalCommonDateUtils.getDate(reqDto.getFromDate(), "dd-MM-yyyy")
					: null;

			Date toDate = reqDto.getToDate() != null && !reqDto.getToDate().isEmpty()
					? GlobalCommonDateUtils.getDate(reqDto.getToDate(), "dd-MM-yyyy")
					: null;

			StringBuilder query = new StringBuilder(sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BILL_DETAILS_FOR_BILL_LIST_BY_FILTER").getQueryString());

			query.append(" and bill.is_dr_note = 'Y'");

			dateFlag = (fromDate != null && toDate != null ? true : false);

			query.append(" and to_char(bill.bill_date_time,'YYYY-MM-DD') between :fromDate and :toDate ");

			if(reqDto.getVisitTypeId() != null)
				query.append(" and bill.visit_type_id =:visitTypeId ");

			if(reqDto.getBillNo() != null && !reqDto.getBillNo().isEmpty())
				query.append(" and trim(lower(bill.bill_number))like :billNo ");

			if(reqDto.getVisitNo() != null && !reqDto.getVisitNo().isEmpty())
				query.append(" and (trim(lower(enc.encounter_number)) like :visitNo or trim(lower(adm.admission_number)) like :visitNo )");

			if(reqDto.getBillStatusId() != null)
				query.append(" and bill.bill_status_id =:billStatusId ");

			if(reqDto.getPatientId() != null)
				query.append(" and bill.patient_id =:patientId ");

			query.append(" order by bill.bill_date_time desc ");
			List<BillListResDto> listBills = null;

			Query sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
					.setParameter("orgId", reqDto.getOrgId())
					.setParameter("unitId", reqDto.getUnitId())
					.setParameter("fromDate", GlobalCommonDateUtils.getStringDate(fromDate, "yyyy-MM-dd"))
					.setParameter("toDate", GlobalCommonDateUtils.getStringDate(toDate, "yyyy-MM-dd"));

			if(reqDto.getVisitTypeId() != null)
				sqlQuery.setParameter("visitTypeId", reqDto.getVisitTypeId());

			if(reqDto.getBillNo() != null && !reqDto.getBillNo().isEmpty())
				sqlQuery.setParameter("billNo", "%" + reqDto.getBillNo().toLowerCase().trim() + "%");

			if(reqDto.getVisitNo() != null && !reqDto.getVisitNo().isEmpty())
				sqlQuery.setParameter("visitNo", "%" + reqDto.getVisitNo().toLowerCase().trim() + "%");

			if(reqDto.getBillStatusId() != null)
				sqlQuery.setParameter("billStatusId", reqDto.getBillStatusId());

			if(reqDto.getPatientId() != null)
				sqlQuery.setParameter("patientId", reqDto.getPatientId());


			listBills = sqlQuery.setResultTransformer(Transformers.aliasToBean(BillListResDto.class)).list();

			return new Response<>(SUCCESS, SUCCESS_CODE, null, listBills, null);
		} catch (Exception e) {
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	public Response getPatientServiceOrderList(AdmissionDto admissionDto) throws BillingException {
		try {
			List<OrderDetailsByEncounterIdDto> admissionDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PATIENT_SERVICE_ORDER_LIST")
					// .setInteger("orgId", admissionDto.getOrganizationId())
					// .setInteger("unitId", admissionDto.getUnitId())
					.setParameter("admissionId", admissionDto.getAdmissionId())
					.setResultTransformer(Transformers.aliasToBean(OrderDetailsByEncounterIdDto.class)).list();
			return new Response(SUCCESS, null, null, admissionDtosList, null);

		} catch (HibernateException he) {
			throw he;

		} catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response cancelUnRenderedServices(CancelOrderedServicesDto cancelOrderedServicesDto)
			throws BillingException {
		try {
			
			

			sessionFactory.getCurrentSession()
					.createSQLQuery("UPDATE public.t_order_details set status='I', " + "ord_cancelled='Y', "
							+ "ord_cancel_reason_id =:ordCancelReasonId , "
							+ "ord_cancel_datetime =:ordCancelDatetime , " + "updated_by=:updatedBy, "
							+ "updated_date=:updatedDate " + "WHERE order_details_id=:orderDetailsId ")
					.setParameter("orderDetailsId", cancelOrderedServicesDto.getOrderDetailsId())
					/*.setDate("ordCancelDatetime",
							GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(0), "dd-M-yyyy HH:mm:ss"))*/
					.setParameter("updatedDate",
							GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(0), "dd-M-yyyy HH:mm:ss"))
					.setParameter("updatedBy", cancelOrderedServicesDto.getUpdatedBy())
					.setParameter("ordCancelReasonId", cancelOrderedServicesDto.getOrdCancelReasonId())
					.setParameter("ordCancelDatetime", GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(sessionService.getUnitId() != null ? sessionService.getUnitId() : 0),"dd-M-yyyy HH:mm:ss"))
					.executeUpdate();
			return new Response(SUCCESS, null, null, null, null);

		} catch (HibernateException he) {
			throw he;

		} catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response activateUnRenderedServices(CancelOrderedServicesDto cancelOrderedServicesDto)
			throws BillingException {
		try {

			sessionFactory.getCurrentSession()
					.createSQLQuery("UPDATE public.t_order_details set status='A', " + "ord_cancelled='N', "
							+ "ord_cancel_datetime=null, " + "updated_by=:updatedBy, " + "updated_date=:updatedDate "
							+ "WHERE order_details_id=:orderDetailsId ")
					.setParameter("orderDetailsId", cancelOrderedServicesDto.getOrderDetailsId())
					.setParameter("updatedDate",
							GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(0), "dd-M-yyyy HH:mm:ss"))
					.setParameter("updatedBy", cancelOrderedServicesDto.getUpdatedBy()).executeUpdate();
			return new Response(SUCCESS, null, null, null, null);

		} catch (HibernateException he) {
			throw he;

		} catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateBillStatusByBillId(Integer statusId, int billId) throws BillingException {
		try {
			Integer result = sessionFactory.getCurrentSession()
					.getNamedQuery("UPDATE_BILL_STATUS_ID_IN_BILL_MASTER_BY_BILL_ID").setParameter("statusId", statusId)
					.setParameter("billId", billId).executeUpdate();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, result);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response updateAddendumBillByBillId(BillingMasterDto billingMasterDto) throws BillingException {
		try {
			String query = " UPDATE billing.m_billing_master SET bill_cancelled='Y' ,	bill_cancel_reason_id =:billCancelReasonId, "
					+ " bill_cancel_datetime=:date , bill_cancel_by =:billCancelBy, "
					+ " bill_cancel_remark =:billCancelRemark, "
					+ " WHERE billing_master_id =:billingMasterId ";

			SQLQuery sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query);
			sqlQuery.setParameter("date", new Date(billingMasterDto.getBillCancelDatetime()))
					.setParameter("billCancelReasonId", billingMasterDto.getBillCancelReasonId())
					.setParameter("billCancelBy", billingMasterDto.getBillCancelBy())
					.setParameter("billCancelRemark", billingMasterDto.getBillCancelRemark())
					.setParameter("billingMasterId", billingMasterDto.getBillingMasterId())
			.executeUpdate();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	public Long getNextValGetBillId() throws BillingException {
		try {
			Session session = sessionFactory.getCurrentSession();
			SQLQuery query = session.createSQLQuery("select nextval('billing.billing_master_seq')");
			Long id = ((BigInteger) query.uniqueResult()).longValue();

			SQLQuery query2 = session.createSQLQuery("select setval('billing.billing_master_seq'," + (id - 1) + ")");
			query2.uniqueResult();

			return id;
		} catch (HibernateException he) {
		} catch (Exception e) {
			
			throw e;
		}
		return null;
	}

	@Override
	public Long generateRequestNumber() throws BillingException {
		try {
			Session session = sessionFactory.getCurrentSession();
			SQLQuery query = session.createSQLQuery("select nextval('billing.request_discount_seq')");
			Long id = ((BigInteger) query.uniqueResult()).longValue();

			SQLQuery query2 = session.createSQLQuery("select setval('billing.request_discount_seq'," + (id - 1) + ")");
			query2.uniqueResult();

			return id;
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return null;
	}

	@Override
	public Response updateBillDetails(BillingDetailsDto billingDetailsDto) throws BillingException {
		try {
			sessionFactory.getCurrentSession()
					.createSQLQuery("UPDATE billing.m_billing_master set " + "net_amount=(net_amount- :netAmt), "
							+ " discount_amount =(discount_amount- :discount), "
							+ " concession_amount =(concession_amount- :concession), "
							+ " gst=(gst- :taxAmount), "
							+ " total_bill_amount=(total_bill_amount- :netAmt ), "
							+ " updated_date=now() WHERE billing_master_id =:billingMasterId ")
					.setParameter("netAmt", billingDetailsDto.getNetAmt())
					.setParameter("discount", billingDetailsDto.getDiscount())
					.setParameter("concession", billingDetailsDto.getConcession())
					.setParameter("taxAmount", billingDetailsDto.getTaxAmount())
					.setParameter("billingMasterId", billingDetailsDto.getBillingMasterId())
					.executeUpdate();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	public Response billlistForBookView(Integer patientId, Integer unitId, Integer orgId, String fromDate,
			String toDate) throws BillingException {
		try {
			List<BillListResDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BILL_DETAILS_FOR_BOOK_VIEW").setParameter("patientId", patientId)
					.setParameter("orgId", orgId).setParameter("unitId", unitId).setParameter("fromDate", fromDate)
					.setParameter("toDate", toDate).setResultTransformer(Transformers.aliasToBean(BillListResDto.class))
					.list();

			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	public Response readSmartCard(Claim claim) throws Exception {
		try {

			SmartCardMasterDto emp1 = new SmartCardMasterDto();
			FileOutputStream output = null;
			try {

				Class.forName("com.mysql.jdbc.Driver");
				Connection conn = DriverManager.getConnection(System.getenv("SMARTLINK_DB_URL"),
						System.getenv("SMARTLINK_DB_USERNAME"), System.getenv("SMARTLINK_DB_PASSWORD"));

				PreparedStatement statement = conn.prepareStatement(
						"select ID,Global_ID,Member_Nr,Progress_Flag ,Exchange_Type,Admit_ID,Rejection_Reason,InOut_Type,Location_ID,Smart_Date,Exchange_Date,Result_Date,Smart_File from exchange_files where Member_Nr='14' AND Progress_Flag = 1 order by  ID Desc limit 1");
				ResultSet rs = statement.executeQuery();
				// File file = new File(xmlFilePath);
				// output = new FileOutputStream(file);
				// //System.out.println("Writing to file " + file.getAbsolutePath());
				SmartCardMasterDto emp = new SmartCardMasterDto();
				while (rs.next()) {
					InputStream input = rs.getBinaryStream("Smart_File");
					emp.setId(rs.getInt("ID"));
					emp.setGlobalId(rs.getString("Global_ID"));
					emp.setMemberNr(rs.getString("Member_Nr"));
					emp.setProgressFlag(rs.getInt("Progress_Flag"));
					emp.setAdmitID(rs.getInt("Exchange_Type"));
					emp.setExchangeType(rs.getInt("Admit_ID"));
					emp.setRejectionReason(rs.getString("Rejection_Reason"));
					emp.setInOutType(rs.getInt("InOut_Type"));
					emp.setLocationID(rs.getString("Location_ID"));

					/*
					 * byte[] buffer = new byte[1024]; while (input.read(buffer) > 0) {
					 * output.write(buffer); }
					 */

				}

				try {

					try {
						JAXBContext jaxbContext = JAXBContext.newInstance(Claim.class);

						Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

						jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

						File claimFile = new File("./claim.xml");

						jaxbMarshaller.marshal(claim, claimFile);

						/*
						 * StringWriter sw = new StringWriter(); jaxbMarshaller.marshal(claim, sw);
						 * String xmlString = sw.toString(); //System.out.println("rrr"+xmlString);
						 * return null;
						 */

					} catch (JAXBException e) {
						
						throw e;
					}

					String filePath = "./claim.xml";

					InputStream claimXMLData = new FileInputStream(new File(filePath));

					// //System.out.println("===readSmartCard Update"+
					// smartCardMasterDto.getMemberNr());
					String sqlUpdate = "UPDATE exchange_files SET Progress_Flag = ?,Exchange_File =?  WHERE ID = ? ";

					PreparedStatement updateEXP = conn.prepareStatement(sqlUpdate);
					updateEXP.setInt(1, 2);
					updateEXP.setBlob(2, claimXMLData);
					// updateEXP.setString(3, claim.getUhidNumber());
					updateEXP.setInt(3, emp.getId());
					updateEXP.executeUpdate();
					// System.out.println("Got result ");

				} catch (SQLException e) {
					logger.error("sql exception ! ");
					logger.error(e.getMessage());
					return new Response<>(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
				}

				conn.close();
				return new Response<>(SUCCESS, SUCCESS_CODE, null, null, emp1.getAmount());
			} catch (SQLException e) {
				logger.error("Got an exception! ");
				logger.error(e.getMessage());
			} finally {
				FileOutputStream writer = new FileOutputStream("./claim.xml");
				writer.write(("").getBytes());
				writer.close();
			}

		} catch (Exception e) {
			
			throw e;

		}
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}
	/*
	 * @Override public Response readSmartCard(Claim claim) throws
	 * ApplicationException { try {
	 * 
	 * SmartCardMasterDto emp1 = new SmartCardMasterDto(); FileOutputStream
	 * output=null; try {
	 * 
	 * Class.forName("com.mysql.jdbc.Driver"); Connection conn =
	 * DriverManager.getConnection("jdbc:mysql://localhost:3306/smartlink", "root",
	 * "root");
	 * 
	 * PreparedStatement statement = conn.prepareStatement(
	 * "select Global_ID,Member_Nr,Progress_Flag ,Exchange_Type,Admit_ID,Rejection_Reason,InOut_Type,Location_ID,Smart_Date,Exchange_Date,Result_Date,Smart_File from exchange_files where Member_Nr='15051914'"
	 * ); ResultSet rs = statement.executeQuery(); File file = new
	 * File(xmlFilePath); output = new FileOutputStream(file);
	 * //System.out.println("Writing to file " + file.getAbsolutePath());
	 * SmartCardMasterDto emp = new SmartCardMasterDto(); while (rs.next()) {
	 * InputStream input = rs.getBinaryStream("Smart_File");
	 * emp.setGlobalId(rs.getString("Global_ID"));
	 * emp.setMemberNr(rs.getString("Member_Nr"));
	 * emp.setProgressFlag(rs.getInt("Progress_Flag"));
	 * emp.setAdmitID(rs.getInt("Exchange_Type"));
	 * emp.setExchangeType(rs.getInt("Admit_ID"));
	 * emp.setRejectionReason(rs.getString("Rejection_Reason"));
	 * emp.setInOutType(rs.getInt("InOut_Type"));
	 * emp.setLocationID(rs.getString("Location_ID"));
	 * 
	 * byte[] buffer = new byte[1024]; while (input.read(buffer) > 0) {
	 * output.write(buffer); }
	 * 
	 * }
	 * 
	 * try {
	 * 
	 * SAXParserFactory factory = SAXParserFactory.newInstance(); SAXParser
	 * saxParser = factory.newSAXParser();
	 * 
	 * DefaultHandler handler = new DefaultHandler() {
	 * 
	 * boolean Amount = false;
	 * 
	 * public void startElement(String uri, String localName, String qName,
	 * Attributes attributes)throws SAXException {
	 * 
	 * if (qName.equalsIgnoreCase("Amount")) { Amount = true; } }
	 * 
	 * public void endElement(String uri, String localName, String qName) throws
	 * SAXException {
	 * 
	 * }
	 * 
	 * public void characters(char ch[], int start, int length) throws SAXException
	 * {
	 * 
	 * if (Amount) {
	 * 
	 * //System.out.println("Amount : " + new String(ch, start, length)); if
	 * (Double.parseDouble(new String(ch, start, length)) != 0.0) {
	 * emp1.setAmount(Double.parseDouble(new String(ch, start, length)));
	 * //System.out.println("Amount : " + emp1.getAmount()); } Amount = false; }
	 * 
	 * }
	 * 
	 * };
	 * 
	 * saxParser.parse("C:\\Users\\admin\\Documents\\employee.xml", handler);
	 * 
	 * } catch (Exception e) {  }
	 * 
	 * if (emp1.getAmount() != 0.0) {
	 * 
	 * 
	 * try {
	 * 
	 * try { JAXBContext jaxbContext = JAXBContext.newInstance(Claim.class);
	 * 
	 * Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
	 * 
	 * jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
	 * 
	 * File claimFile = new File("C:\\Users\\admin\\Documents\\claim.xml");
	 * 
	 * jaxbMarshaller.marshal(claim, claimFile);
	 * 
	 * } catch (JAXBException e) {  }
	 * 
	 * String filePath = "C:\\Users\\admin\\Documents\\claim.xml";
	 * 
	 * 
	 * InputStream claimXMLData = new FileInputStream(new File(filePath));
	 * 
	 * 
	 * ////System.out.println("===readSmartCard Update"+
	 * smartCardMasterDto.getMemberNr()); String sqlUpdate =
	 * "UPDATE exchange_files SET Progress_Flag = ?,Exchange_File =?  WHERE Member_Nr=? "
	 * ;
	 * 
	 * PreparedStatement updateEXP = conn.prepareStatement(sqlUpdate);
	 * updateEXP.setInt(1, 4); updateEXP.setBlob(2, claimXMLData);
	 * updateEXP.setString(3, "15051914"); updateEXP.executeUpdate();
	 * //System.out.println("Got result ");
	 * 
	 * } catch (SQLException e) { System.err.println("sql exception ! ");
	 * System.err.println(e.getMessage()); return new Response<>(ERROR, null,
	 * COMMON_ERROR_MESSAGE, null, null); } }
	 * 
	 * conn.close(); return new Response<>(SUCCESS, SUCCESS_CODE, null, null,
	 * emp1.getAmount()); } catch (SQLException e) {
	 * System.err.println("Got an exception! "); System.err.println(e.getMessage());
	 * }finally{ output.close(); }
	 * 
	 * } catch (Exception e) { 
	 * 
	 * 
	 * 
	 * } return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null); }
	 */

	@Override
	public Integer updateFinalBill(Integer admissionId) throws ApplicationException {
		try {

			int i = sessionFactory.getCurrentSession()
					.createQuery("UPDATE Admission set isFinalBill='Y' WHERE admissionId =:admissionId ")
					.setParameter("admissionId", admissionId)
					.executeUpdate();
			return i;
		} catch (HibernateException he) {

		} catch (Exception e) {
			
			throw e;
		}
		return 0;
	}

	@Override
	public BillPayeeDetailsDto getSelfPayeeAmount(Integer encounterId, Integer unitId, boolean debitNoteService) {
		try {

			BillPayeeDetailsDto detailsDto = (BillPayeeDetailsDto) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SELF_PAYEE_DETAILS").setParameter("encounterId", encounterId)
					.setParameter("unitId", unitId).setParameter("debitNoteService", debitNoteService)
					.setResultTransformer(Transformers.aliasToBean(BillPayeeDetailsDto.class)).uniqueResult();

			return detailsDto;
		} catch (Exception e) {
			
			throw e;
		}
		//return null;
	}

	@Override
	public List<BillPayeeDetailsDto> getCreditPayeeAmount(Integer encounterId, Integer unitId,
			boolean debitNoteService) {
		try {

			List<BillPayeeDetailsDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CREDIT_PAYEE_DETAILS").setParameter("encounterId", encounterId)
					.setParameter("unitId", unitId).setParameter("debitNoteService", debitNoteService)
					.setResultTransformer(Transformers.aliasToBean(BillPayeeDetailsDto.class)).list();
			return list;
		} catch (Exception e) {
			
			throw e;
		}
		//return null;
	}

	@Override
	public BillPayeeDetailsDto getSelfPayeeAmountForAdmission(Integer admissionId, Integer unitId,
			boolean debitNoteService, Set<Integer> orderDetailsIdsList) throws BillingException {
		try {

			BillPayeeDetailsDto detailsDto = (BillPayeeDetailsDto) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SELF_PAYEE_DETAILS_FOR_ADMISSION").setParameter("admissionId", admissionId)
					.setParameter("unitId", unitId).setParameter("debitNoteService", debitNoteService)
					.setParameterList("orderDetailsIdsList", orderDetailsIdsList)
					.setResultTransformer(Transformers.aliasToBean(BillPayeeDetailsDto.class)).uniqueResult();

			return detailsDto;
		} catch (Exception e) {
			
			throw e;
		}
		//return null;
	}

	@Override
	public List<BillPayeeDetailsDto> getCreditPayeeAmountForAdmission(Integer admissionId, Integer unitId,
			boolean debitNoteService, Set<Integer> orderDetailsIdsList) throws BillingException {
		try {

			List<BillPayeeDetailsDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CREDIT_PAYEE_DETAILS_FOR_ADMISSION").setParameter("admissionId", admissionId)
					.setParameter("unitId", unitId).setParameter("debitNoteService", debitNoteService)
					.setParameterList("orderDetailsIdsList", orderDetailsIdsList)
					.setResultTransformer(Transformers.aliasToBean(BillPayeeDetailsDto.class)).list();
			return list;
		} catch (Exception e) {
			
			throw e;
		}
		//return null;
	}

	@Override
	public Response submitSmartCard(SmartcardClaimDto smartcardClaimDto) throws ApplicationException, SQLException {
		try {

			SmartCardMasterDto emp1 = new SmartCardMasterDto();
			FileOutputStream output = null;
			try {

				Session session = sessionFactory.getCurrentSession();
				SQLQuery query = session.createSQLQuery(
						"select mysql_smart_jdbc from public.t_unit_application_billing_configuration where organization_id =:organizationId "+
								" AND unit_id =:unitId " +
								" AND status='A' limit 1");

				query.setParameter("organizationId", smartcardClaimDto.getOrganizationId())
						.setParameter("unitId", smartcardClaimDto.getUnitId());
				String mysqlSmartJdbc = query.uniqueResult().toString();

				String[] connStr = mysqlSmartJdbc.split(",");
				String firststr = connStr[0];
				String secondStr = connStr[1];
				String thirdStr = connStr[2];
				// System.out.println("First" + firststr + "-->Seco" + secondStr + "-->Thir" +
				// thirdStr);
				// "jdbc:mysql://192.168.137.1:3307/smartlink", "Vishal", "Vishal"
				// //System.out.println(""+"jdbc:mysql://"+firststr+"/smartlink",
				// "'"+secondStr+"'", "'"+thirdStr+"'");
				// //System.out.println("jdbc:mysql://"+firststr+"/smartlink",);
				Class.forName("com.mysql.jdbc.Driver");
				Connection conn = DriverManager.getConnection("jdbc:mysql://" + firststr.trim(), secondStr.trim(),
						thirdStr.trim());
				String preparedStr =
						"select ID,Global_ID,Member_Nr,Progress_Flag ,Exchange_Type,Admit_ID,Rejection_Reason,InOut_Type,Location_ID,Smart_Date,Exchange_Date,Result_Date,Smart_File "
								+ " from exchange_files where Member_Nr = ? "
								+ " AND Progress_Flag = 1 order by  ID Desc limit 1";

				PreparedStatement statement = conn.prepareStatement(preparedStr);
				statement.setString(1, smartcardClaimDto.getUhidNumber());
				/*
				 * System.out.println("ddd" +
				 * "select ID,Global_ID,Member_Nr,Progress_Flag ,Exchange_Type,Admit_ID,Rejection_Reason,InOut_Type,Location_ID,Smart_File from exchange_files where Member_Nr='"
				 * + smartcardClaimDto.getUhidNumber() +
				 * "' AND Progress_Flag = 1 order by  ID Desc limit 1");
				 */
				ResultSet rs = statement.executeQuery();
				/*
				 * if(rs.next() == false) { conn.close(); return new Response(ERROR, ERR_500,
				 * ERR_500_MESSAGE, null, null); }
				 */
				/*
				 * else {
				 */

				SmartCardMasterDto emp = new SmartCardMasterDto();
				while (rs.next()) {
					// InputStream input = rs.getBinaryStream("Smart_File");
					emp.setId(rs.getInt("ID"));
					emp.setGlobalId(rs.getString("Global_ID"));
					emp.setMemberNr(rs.getString("Member_Nr"));
					emp.setProgressFlag(rs.getInt("Progress_Flag"));
					emp.setAdmitID(rs.getInt("Exchange_Type"));
					emp.setExchangeType(rs.getInt("Admit_ID"));
					emp.setRejectionReason(rs.getString("Rejection_Reason"));
					emp.setInOutType(rs.getInt("InOut_Type"));
					emp.setLocationID(rs.getString("Location_ID"));
					emp.setSmartFileString(rs.getString("Smart_File"));

					/*
					 * byte[] buffer = new byte[1024]; while (input.read(buffer) > 0) {
					 * output.write(buffer); }
					 */
				}

				if (emp.getId() == null) {
					return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
				} else {
					// //System.out.println("Or"+emp.getSmartFileString());
					DateTimeFormatter dtfDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
					DateTimeFormatter dtfTime = DateTimeFormatter.ofPattern("HH:mm:ss");
					LocalDateTime currentDate = LocalDateTime.now();
					LocalTime currentTime = LocalTime.now();
					String curDate = dtfDate.format(currentDate);
					String curTime = dtfTime.format(currentTime);

					String claimString = getXMLClaimFormat(smartcardClaimDto, emp);

					String sqlUpdate = "UPDATE exchange_files SET Progress_Flag = ?,Exchange_File =? ,Exchange_Date= ? WHERE ID = ? ";

					PreparedStatement updateEXP = conn.prepareStatement(sqlUpdate);
					updateEXP.setInt(1, 2);
					updateEXP.setString(2, claimString);
					updateEXP.setString(3, curDate + " " + curTime);
					updateEXP.setInt(4, emp.getId());
					updateEXP.executeUpdate();
					// System.out.println("Got result ");

					/*
					 * int i = sessionFactory.getCurrentSession()
					 * .createQuery("UPDATE BillingMaster set isSentToSmartCard='Y' WHERE billingMasterId="
					 * +smartcardClaimDto.getBillingMasterId()) .executeUpdate();
					 */
					conn.close();
					this.updateSmartCardStatus(smartcardClaimDto.getBillingMasterId(), smartcardClaimDto.isPharamcy());
					return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
				}

				/* } */

				// conn.close();
				// return new Response<>(SUCCESS, SUCCESS_CODE, null, null, emp1.getAmount());
			} catch (SQLException e) {
				logger.error("Got an exception! ");
				logger.error(e.getMessage());
			} /*
				 * finally{ FileOutputStream writer = new FileOutputStream("./claim.xml");
				 * writer.write(("").getBytes()); writer.close(); }
				 */

		} catch (Exception e) {
			
			return new Response(ERROR, ERR_500, "Specialty Not Mapped With SmartCard Device", null, null);

		}
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	public Integer getTotalClaimSrvCount(SmartcardClaimDto smartcardClaimDto){
		try {
			  Integer totalCount = 0;
			  Long serviceCount = smartcardClaimDto.getOrderDetailsMasterDtosList().stream().filter(obj->obj.getSaleId()==null).count();
			  List<Integer> saleIds= smartcardClaimDto.getOrderDetailsMasterDtosList()
					  .stream()
					  .filter(obj->obj.getSaleId()!=null &&obj.getSaleId()>0)
					  .map(obj->obj.getSaleId()).collect(Collectors.toList());
			  if(saleIds!=null && saleIds.size()>0){
				  String query ="select "
						  + "	cast(count(tsd.id) as int) "
						  + "from "
						  + "	pharmacy.t_sale_detail tsd "
						  + "	where "
						  + "	tsd.sale_id in (:saleIds) "
						  + "	and tsd.is_active = true";
				  Integer itemsCount = (Integer)sessionFactory
						  .getCurrentSession()
						  .createSQLQuery(query)
						  .setParameterList("saleIds",saleIds)
						  .uniqueResult();
				  return serviceCount.intValue()+itemsCount;
			  }
			  return serviceCount.intValue();
		} catch (Exception e) {
			
			return 0;
		}
	}

	public Map<Integer,String> getMedicationOfPharamcyService(SmartcardClaimDto smartcardClaimDto,OrderDetailsMasterDto ordDto,String billDate,String billDtTime,Integer start){
		try {
			String claimString = "";
			String query = "select "
					+ "	mi.code as \"code\", "
					+ "	mi.\"name\" as \"itemName\", "
					+ "	tod.order_qty as \"quantity\", "
					+ "	cast(tod.net_amount as double precision) as \"netAmount\" "
					+ "from "
					+ "	public.t_order_details tod "
					+ "inner join public.t_order_master tom  on "
					+ "	tom.order_id = tod.order_id "
					+ "left join public.m_item mi on "
					+ "	tod.drug_id = mi.id "
					+ "where "
					+ "	tom.sale_id = :saleId "
					+ "	and tom.order_id = :orderId "
					+ "	and tod.sale_detail_id > 0";

			Map<Integer,String> inputMap = new HashMap<>();
			List<ItemDetailsResponseDto> itemsList = sessionFactory
					.getCurrentSession()
					.createSQLQuery(query)
					.setParameter("saleId",ordDto.getSaleId())
					.setParameter("orderId",ordDto.getOrderId())
					.setResultTransformer(Transformers.aliasToBean(ItemDetailsResponseDto.class)).list();

			Integer startNumber = start;
			Integer endNumber  = start;
			for(ItemDetailsResponseDto dto : itemsList){
				 endNumber = startNumber;
				 claimString += " <Service> " + "   <Number>" + startNumber + "</Number> " + "   <Invoice_Number>"
						+ smartcardClaimDto.getBillNumber() + "</Invoice_Number> " + "   <Global_Invoice_Nr>"
						+ smartcardClaimDto.getBillNumber() + "</Global_Invoice_Nr> " + "   <Start_Date>" + billDate
						+ "</Start_Date> " + "   <Start_Time>" + billDtTime + "</Start_Time> " + "   <Provider> "
						+ "       <Role>DP</Role> " + "       <Practice_Number>" + sessionService.getPracticeNo()
						+ "</Practice_Number> " + "   </Provider> "
						+ this.getDiagnosisByVisitId(smartcardClaimDto.getVisitId(),
						smartcardClaimDto.getVisitTypeId(), smartcardClaimDto.isPharamcy())
						+ "   <Encounter_Type>Medication</Encounter_Type> " + "   <Code_Type>MCODE</Code_Type> "
						+ "   <Code>" + dto.getCode() + "</Code> " + "   <Code_Description>" + dto.getItemName()
						+ "</Code_Description> " + "   <Quantity>" + dto.getQuantity() + "</Quantity> "
						+ "   <Total_Amount>" + dto.getNetAmount() + "</Total_Amount> " + "   <Reason></Reason> "
						+ "   </Service> ";
				  startNumber = startNumber+1;
			   }
			inputMap.put(endNumber,claimString);
			return inputMap;
		} catch (Exception e) {
			
			return null;
		}
	}

	public String getXMLClaimFormat(SmartcardClaimDto smartcardClaimDto, SmartCardMasterDto emp)
			throws JSONException, ParseException {

		String s1 = smartcardClaimDto.getBillDate();
		String[] billDtTime = s1.split("\\s");

		String billDate = GlobalCommonDateUtils.formatDateForClaim(billDtTime[0], "dd-MM-yyyy",
				sessionService.getStartDateFormat());

		DateTimeFormatter dtfDate = DateTimeFormatter.ofPattern(sessionService.getStartDateFormat());
		// System.out.println(sessionService.getStartDateFormat());
		DateTimeFormatter dtfTime = DateTimeFormatter.ofPattern("HH:mm:ss");
		LocalDateTime currentDate = LocalDateTime.now();
		LocalTime currentTime = LocalTime.now();
		String curDate = dtfDate.format(currentDate);
		String curTime = dtfTime.format(currentTime);

		String newSmartFile = emp.getSmartFileString().replace("\"", "'");
		newSmartFile = newSmartFile.replace("16F", "@");
		newSmartFile = newSmartFile.replace("42E", "$");
		newSmartFile = newSmartFile.replace("1E", "*");

		JSONObject soapDatainJsonObject = XML.toJSONObject(newSmartFile);
		// //System.out.println(soapDatainJsonObject.get("AdmissionInformation"));
		String cardSerialnumber = soapDatainJsonObject.getJSONObject("AdmissionInformation").getJSONObject("A1")
				.get("card_serialnumber").toString();
		cardSerialnumber = cardSerialnumber.replace("@", "16F");
		cardSerialnumber = cardSerialnumber.replace("*", "1E");
		newSmartFile = newSmartFile.replace("$", "42E");
		String Nr = soapDatainJsonObject.getJSONObject("AdmissionInformation").getJSONObject("Benefits")
				.getJSONObject("Benefit").get("Nr").toString();

		String notes = "";
		// if(smartcardClaimDto.getVisitTypeId()==2)
		// {
		// notes = this.getDischargeSummeryNotes(smartcardClaimDto.getVisitId());
		// }

		/*
		 * String claimString =
		 * "<Claim><Claim_Header><Invoice_Number>"+smartcardClaimDto.getBillNumber()+
		 * "</Invoice_Number>" +
		 * "<Claim_Date>"+curDate+"</Claim_Date><Claim_Time>"+curTime+
		 * "</Claim_Time><Pool_Number>"+Nr+"</Pool_Number>" +
		 * "<Total_Services>"+smartcardClaimDto.getOrderDetailsMasterDtosList().size()+
		 * "</Total_Services>" +
		 * "<Gross_Amount>"+smartcardClaimDto.getTotalNetPayableAmount()+
		 * "</Gross_Amount><Provider><Role>SP</Role>" +
		 * "<Country_Code>KEN</Country_Code><Group_Practice_Number>"+sessionService.
		 * getGroupPracticeNo()+"</Group_Practice_Number><Group_Practice_Name>"+
		 * sessionService.getGroupPracticeName()+"</Provider>" +
		 * " <Authorization><Pre_Authorization_Number>12</Pre_Authorization_Number><Pre_Authorization_Amount>0</Pre_Authorization_Amount></Authorization>"
		 * +
		 * "<Payment_Modifiers><Payment_Modifier><Type>1</Type><Amount>0</Amount><Receipt>0</Receipt></Payment_Modifier>"
		 * +
		 * "<PaymentModifier><Type>5</Type><NHIF_Member_Nr>0</NHIF_Member_Nr><NHIF_Contributor_Nr>0</NHIF_Contributor_Nr><NHIF_Employer_Code>0</NHIF_Employer_Code>"
		 * +
		 * "<NHIF_Site_Nr>0</NHIF_Site_Nr><NHIF_Patient_Relation>0</NHIF_Patient_Relation><Diagnosis_Code>0</Diagnosis_Code>"
		 * + " <Admit_Date>"+curDate+"</Admit_Date><Discharge_Date>"+curDate+
		 * "</Discharge_Date><Days_Used>0</Days_Used><Amount>0</Amount></PaymentModifier></Payment_Modifiers></Claim_Header>"
		 * + "<Member><Membership_Number>"+smartcardClaimDto.getUhidNumber()+
		 * "</Membership_Number><card_serialnumber>"+cardSerialnumber+
		 * "</card_serialnumber>" +
		 * "<Scheme_Code>UAP</Scheme_Code><Scheme_Plan>UAP</Scheme_Plan></Member>" +
		 * " <Patient><Dependant>N</Dependant><First_Name>"+smartcardClaimDto.
		 * getFirstName()+"</First_Name><Middle_Name>"+smartcardClaimDto.getMiddleName()
		 * +"</Middle_Name><Surname>"+smartcardClaimDto.getLastName()+"</Surname>" +
		 * "<Date_Of_Birth>"+smartcardClaimDto.getDob()+"</Date_Of_Birth><Gender>"+
		 * smartcardClaimDto.getGenderName()+"</Gender></Patient>" +
		 * "<Claim_Data><Discharge_Notes>"+notes+"</Discharge_Notes>";
		 */

		Integer totalService = (smartcardClaimDto.isPharamcy()) ? smartcardClaimDto.getItems().size()
				: this.getTotalClaimSrvCount(smartcardClaimDto);

		String claimString = "<Claim> " + "  <Claim_Header> " + " <Invoice_Number>" + smartcardClaimDto.getBillNumber()
				+ "</Invoice_Number> " + " <Claim_Date>" + curDate + "</Claim_Date> " + "  <Claim_Time>" + curTime
				+ "</Claim_Time> " + " <Pool_Number>" + Nr + "</Pool_Number> " + "  <Total_Services>" + totalService
				+ "</Total_Services> " + "  <Gross_Amount>" + smartcardClaimDto.getTotalNetPayableAmount()
				+ "</Gross_Amount>" + "    <Provider> " + "        <Role>SP</Role> "
				+ "        <Country_Code>KEN</Country_Code> " + "        <Group_Practice_Number>"
				+ sessionService.getGroupPracticeNo() + "</Group_Practice_Number> " + "        <Group_Practice_Name>"
				+ sessionService.getGroupPracticeName() + "</Group_Practice_Name> " + "     </Provider>"
				+ this.getPreAuthRequestDataByVisit(smartcardClaimDto.getVisitId(), smartcardClaimDto.getVisitTypeId(),
						smartcardClaimDto.isPharamcy())
				+ " <Payment_Modifiers> "
				+ this.getReceiptDetailsByBillid(smartcardClaimDto.getBillingMasterId(), smartcardClaimDto.isPharamcy())
				+ "            <PaymentModifier> " + "                <Type>5</Type> "
				+ "                <NHIF_Member_Nr>0</NHIF_Member_Nr> "
				+ "                <NHIF_Contributor_Nr>0</NHIF_Contributor_Nr> "
				+ "                <NHIF_Employer_Code>0</NHIF_Employer_Code> "
				+ "                 <NHIF_Site_Nr>0</NHIF_Site_Nr> "
				+ "                <NHIF_Patient_Relation>0</NHIF_Patient_Relation> "
				+ "                <Diagnosis_Code>0</Diagnosis_Code> " + "                <Admit_Date>" + curDate
				+ "</Admit_Date> " + "                <Discharge_Date>" + curDate + "</Discharge_Date> "
				+ "                <Days_Used>0</Days_Used> " + "                <Amount>0</Amount> "
				+ "             </PaymentModifier> " + "         </Payment_Modifiers> " + "  </Claim_Header> "
				+ "     <Member> " + "         <Membership_Number>" + smartcardClaimDto.getUhidNumber()
				+ "</Membership_Number> " + "          <card_serialnumber>" + cardSerialnumber + "</card_serialnumber> "
				+ "          <Scheme_Code>UAP</Scheme_Code> " + "         <Scheme_Plan>UAP</Scheme_Plan> "
				+ "     </Member> " + "     <Patient> " + "         <Dependant>N</Dependant> " + "         <First_Name>"
				+ smartcardClaimDto.getFirstName() + "</First_Name> " + "         <Middle_Name>"
				+ smartcardClaimDto.getMiddleName() + "</Middle_Name> " + "         <Surname>"
				+ smartcardClaimDto.getLastName() + "</Surname> " + "         <Date_Of_Birth>"
				+ GlobalCommonDateUtils.formatDateForClaim(smartcardClaimDto.getDob(), "dd-MM-yyyy",
						sessionService.getStartDateFormat())
				+ "</Date_Of_Birth> " + "         <Gender>" + smartcardClaimDto.getGenderName() + "</Gender> "
				+ "      </Patient> " + "      <Claim_Data> "
				+ this.getDischargeSummeryNotes(smartcardClaimDto.getVisitId(), smartcardClaimDto.isPharamcy());

		int ser = 1;

		if (smartcardClaimDto.isPharamcy()) {
			ListIterator<ItemDetailsResponseDto> iterator = smartcardClaimDto.getItems().listIterator();
			while (iterator.hasNext()) {

				ItemDetailsResponseDto dto = iterator.next();
				if (dto.getItemId() != null && dto.getItemId() > 0) {

					claimString += " <Service> " + "   <Number>" + ser + "</Number> " + "   <Invoice_Number>"
							+ smartcardClaimDto.getBillNumber() + "</Invoice_Number> " + "   <Global_Invoice_Nr>"
							+ smartcardClaimDto.getBillNumber() + "</Global_Invoice_Nr> " + "   <Start_Date>" + billDate
							+ "</Start_Date> " + "   <Start_Time>" + billDtTime[1] + "</Start_Time> " + "   <Provider> "
							+ "       <Role>DP</Role> " + "       <Practice_Number>" + sessionService.getPracticeNo()
							+ "</Practice_Number> " + "   </Provider> "
							+ this.getDiagnosisByVisitId(smartcardClaimDto.getVisitId(),
									smartcardClaimDto.getVisitTypeId(), smartcardClaimDto.isPharamcy())
							+ "   <Encounter_Type>Medication</Encounter_Type> " + "   <Code_Type>MCODE</Code_Type> "
							+ "   <Code>" + dto.getCode() + "</Code> " + "   <Code_Description>" + dto.getItemName()
							+ "</Code_Description> " + "   <Quantity>" + dto.getQuantity() + "</Quantity> "
							+ "   <Total_Amount>" + dto.getNetAmount() + "</Total_Amount> " + "   <Reason></Reason> "
							+ "   </Service> ";

				} else {

					claimString += "<Service>  " + "    <Number>" + ser + "</Number> " + "    <Invoice_Number>"
							+ smartcardClaimDto.getBillNumber() + "</Invoice_Number> " + "    <Global_Invoice_Nr>"
							+ smartcardClaimDto.getBillNumber() + "</Global_Invoice_Nr>	" + "    <Start_Date>"
							+ billDate + "</Start_Date> " + "    <Start_Time>" + billDtTime[1] + "</Start_Time> "
							+ "    <Provider> " + "        <Role>DP</Role> " + "        <Practice_Number>"
							+ sessionService.getPracticeNo() + "</Practice_Number> " + "    </Provider> "
							+ this.getDiagnosisByVisitId(smartcardClaimDto.getVisitId(),
									smartcardClaimDto.getVisitTypeId(), smartcardClaimDto.isPharamcy())
							+ "    <Encounter_Type>COPAY</Encounter_Type> " + "    <Code_Type>MCODE</Code_Type> "
							+ "    <Code>898989</Code> " + "    <Code_Description>COPAY</Code_Description> "
							+ "    <Quantity>" + dto.getQuantity() + "</Quantity> " + "    <Total_Amount>"
							+ dto.getNetAmount() + "</Total_Amount> " + "    <Reason></Reason> " + "</Service> ";
				}
				ser++;
			}
		} else {
			ListIterator<OrderDetailsMasterDto> iterator = smartcardClaimDto.getOrderDetailsMasterDtosList()
					.listIterator();
			while (iterator.hasNext()) {

				OrderDetailsMasterDto detailsByEncounterIdDto = iterator.next();
				if (detailsByEncounterIdDto.getSpecialityId() > 0) {
					Session session = sessionFactory.getCurrentSession();
					SQLQuery query = session.createSQLQuery(
							"select device_speciality_desc from billing.claim_interface_device_speciality_mppr "
									 +" where claim_device_id = '1' AND his_speciality_id =:specialityId "
									 +" limit 1");
							query.setParameter("specialityId", detailsByEncounterIdDto.getSpecialityId());
					String deviceSpecialityDesc = query.uniqueResult().toString();

					/*
					 * claimString
					 * +="<Service><Number>"+ser+"</Number><Invoice_Number>"+smartcardClaimDto.
					 * getBillNumber()+"</Invoice_Number><Global_Invoice_Nr>"+smartcardClaimDto.
					 * getBillNumber()+"</Global_Invoice_Nr>" +
					 * "<Start_Date>"+billDtTime[0]+"</Start_Date><Start_Time>"+billDtTime[1]+
					 * "</Start_Time>" +
					 * "<Provider><Role>DP</Role><Practice_Number>"+sessionService.getPracticeNo()+
					 * "</Practice_Number></Provider>" +
					 * "<Diagnosis><Stage>P</Stage><Code_Type>ICD10</Code_Type><Code>UNKN</Code></Diagnosis>"
					 * + "<Encounter_Type>"+deviceSpecialityDesc+
					 * "</Encounter_Type><Code_Type>MCODE</Code_Type>" +
					 * "<Code>"+detailsByEncounterIdDto.getServiceName()+"</Code><Code_Description>"
					 * +detailsByEncounterIdDto.getServiceName()+"</Code_Description>" +
					 * "<Quantity>"+detailsByEncounterIdDto.getOrderQty()+
					 * "</Quantity><Total_Amount>"+detailsByEncounterIdDto.getNetAmount()+
					 * "</Total_Amount><Reason></Reason></Service>";
					 */
					if(detailsByEncounterIdDto.getServiceId()!=null && smartcardClaimDto.getPharamcyServiceId()!=null && detailsByEncounterIdDto.getServiceId().equals(smartcardClaimDto.getPharamcyServiceId())){
						Map<Integer,String> resultMap = getMedicationOfPharamcyService(smartcardClaimDto,detailsByEncounterIdDto,billDate,billDtTime[1],ser);
						claimString += resultMap.values().stream().findFirst().get();
						ser = resultMap.keySet().stream().findFirst().get();
					}else{
					claimString += " <Service> " + "   <Number>" + ser + "</Number> " + "   <Invoice_Number>"
							+ smartcardClaimDto.getBillNumber() + "</Invoice_Number> " + "   <Global_Invoice_Nr>"
							+ smartcardClaimDto.getBillNumber() + "</Global_Invoice_Nr> " + "   <Start_Date>" + billDate
							+ "</Start_Date> " + "   <Start_Time>" + billDtTime[1] + "</Start_Time> " + "   <Provider> "
							+ "       <Role>DP</Role> " + "       <Practice_Number>" + sessionService.getPracticeNo()
							+ "</Practice_Number> " + "   </Provider> "
							+ this.getDiagnosisByVisitId(smartcardClaimDto.getVisitId(),
							smartcardClaimDto.getVisitTypeId(), smartcardClaimDto.isPharamcy())
							+ "   <Encounter_Type>" + deviceSpecialityDesc + "</Encounter_Type> "
							+ "   <Code_Type>MCODE</Code_Type> " + "   <Code>"
							+ detailsByEncounterIdDto.getServiceCode() + "</Code> " + "   <Code_Description>"
							+ detailsByEncounterIdDto.getServiceName() + "</Code_Description> " + "   <Quantity>"
							+ detailsByEncounterIdDto.getOrderQty() + "</Quantity> " + "   <Total_Amount>"
							+ detailsByEncounterIdDto.getNetAmount() + "</Total_Amount> " + "   <Reason></Reason> "
							+ "   </Service> ";
				     }
				} else {
					/*
					 * claimString
					 * +="<Service><Number>"+ser+"</Number><Invoice_Number>"+smartcardClaimDto.
					 * getBillNumber()+"</Invoice_Number><Global_Invoice_Nr>"+smartcardClaimDto.
					 * getBillNumber()+"</Global_Invoice_Nr>" +
					 * "<Start_Date>"+billDtTime[0]+"</Start_Date><Start_Time>"+billDtTime[1]+
					 * "</Start_Time>" +
					 * "<Provider><Role>DP</Role><Practice_Number>"+sessionService.getPracticeNo()+
					 * "</Practice_Number></Provider>" +
					 * "<Diagnosis><Stage>P</Stage><Code_Type>ICD10</Code_Type><Code>UNKN</Code></Diagnosis>"
					 * + "<Encounter_Type>COPAY</Encounter_Type><Code_Type>MCODE</Code_Type>" +
					 * "<Code>898989</Code><Code_Description>COPAY</Code_Description>" +
					 * "<Quantity>"+detailsByEncounterIdDto.getOrderQty()+
					 * "</Quantity><Total_Amount>"+detailsByEncounterIdDto.getNetAmount()+
					 * "</Total_Amount><Reason></Reason></Service>";
					 */
					if(detailsByEncounterIdDto.getServiceId()!=null && smartcardClaimDto.getPharamcyServiceId()!=null && detailsByEncounterIdDto.getServiceId().equals(smartcardClaimDto.getPharamcyServiceId())){
						Map<Integer,String> resultMap = getMedicationOfPharamcyService(smartcardClaimDto,detailsByEncounterIdDto,billDate,billDtTime[1],ser);
						claimString += resultMap.values().stream().findFirst().get();
						ser = resultMap.keySet().stream().findFirst().get();
					}else{
					claimString += "<Service>  " + "    <Number>" + ser + "</Number> " + "    <Invoice_Number>"
							+ smartcardClaimDto.getBillNumber() + "</Invoice_Number> " + "    <Global_Invoice_Nr>"
							+ smartcardClaimDto.getBillNumber() + "</Global_Invoice_Nr>	" + "    <Start_Date>"
							+ billDate + "</Start_Date> " + "    <Start_Time>" + billDtTime[1] + "</Start_Time> "
							+ "    <Provider> " + "        <Role>DP</Role> " + "        <Practice_Number>"
							+ sessionService.getPracticeNo() + "</Practice_Number> " + "    </Provider> "
							+ this.getDiagnosisByVisitId(smartcardClaimDto.getVisitId(),
							smartcardClaimDto.getVisitTypeId(), smartcardClaimDto.isPharamcy())
							+ "    <Encounter_Type>COPAY</Encounter_Type> " + "    <Code_Type>MCODE</Code_Type> "
							+ "    <Code>898989</Code> " + "    <Code_Description>COPAY</Code_Description> "
							+ "    <Quantity>" + detailsByEncounterIdDto.getOrderQty() + "</Quantity> "
							+ "    <Total_Amount>" + detailsByEncounterIdDto.getNetAmount() + "</Total_Amount> "
							+ "    <Reason></Reason> " + "</Service> ";
				   }
				}
				ser++;
			}
		}

		claimString += "</Claim_Data></Claim>";
		// System.out.println("Claim Data" + claimString);
		return claimString;
	}

	@Override
	public BillListResDto getBillSumsAdmissionId(Integer admissionId)

	{
		try {

			String query = "select sum(net_amount) as \"netAmount\", "
					+ "sum(total_self_payable) as \"totalSelfPayable\", "
					+ "sum(total_credit_payable) as \"totalCreditPayable\", " + "sum(outstanding) as \"outstanding\" "
					+ "from billing.m_billing_master where admission_id =:admissionId ";

			BillListResDto configInfoDto = (BillListResDto) sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("admissionId", admissionId)
					.setResultTransformer(Transformers.aliasToBean(BillListResDto.class)).uniqueResult();
			return configInfoDto;
		} catch (Exception e) {
			
			throw e;
		}
//		return null;
	}

	@Override
	public String getDischargeSummeryNotes(Integer admissionId, boolean isPharamcy) {
		try {
			String htmlData = "";

			if (isPharamcy) {
				htmlData = "<Discharge_Notes>Text Data as Discharge</Discharge_Notes>";
			} else {
				String query = "select " + "	dsfm.discharge_field_desc as \"dischargeFieldDesc\", "
						+ "	dsd.discharge_field_description as \"templateDesc\"	" + " from "
						+ "	adt.m_discharge_summary ds " + " inner join adt.m_discharge_summary_detail dsd on "
						+ "	ds.discharge_summary_id = dsd.discharge_summary_id "
						+ " inner join adt.m_discharge_summary_field_master dsfm on "
						+ "	dsfm.discharge_field_id = dsd.discharge_field_id " + " where "
						+ "	ds.admission_id = :admissionId " + "	and ds.status = 'A' and dsd.status='A' ";

				List<SmartCartDischargeSummeryDto> list = null;/*
																 * = (List<SmartCartDischargeSummeryDto>)sessionFactory.
																 * getCurrentSession() .createSQLQuery(query)
																 * .setInteger("admissionId", admissionId)
																 * .setResultTransformer(Transformers.aliasToBean(
																 * SmartCartDischargeSummeryDto.class)).list();
																 */

				if (list != null && list.size() > 0) {
					htmlData = "<Discharge_Notes>";
					for (Iterator iterator = list.iterator(); iterator.hasNext();) {
						SmartCartDischargeSummeryDto smartCartDischargeSummeryDto = (SmartCartDischargeSummeryDto) iterator
								.next();
						htmlData += smartCartDischargeSummeryDto.getTemplateDesc() + "<br></br>";
					}
					htmlData += "</Discharge_Notes>";
				} else {
					htmlData = "<Discharge_Notes>Text Data as Discharge</Discharge_Notes>";
				}
			}
			return htmlData;
		} catch (Exception e) {
			
			throw e;
		}
		//return null;
	}

	@Override
	public String getPreAuthRequestDataByVisit(Integer visitId, Integer visitTypeId, boolean isPharamcy) {
		try {

			String htmlData = "";
			if (isPharamcy) {
				// default
				htmlData = " <Authorization><Pre_Authorization_Number>12</Pre_Authorization_Number><Pre_Authorization_Amount>0</Pre_Authorization_Amount></Authorization>";
			} else {

				String query = "select " + "	authorization_no as \"requestNo\" , "
						+ "	authorization_amount \"requestAmount\" " + " from " + "	billing.m_preauth_request_master "
						+ " where " + "	request_type_id = 1 " + "	and auth_status = 1 "
						+ "	and is_final_submit = 'Y' " + "	and is_auth_deleted = 'N' " + "	and is_split_status = 'Y' ";
				StringBuffer sb = new StringBuffer(query);

				if(visitTypeId != null && visitTypeId == 1)
					sb.append(" and encounter_id =:visitId ");

				if(visitTypeId != null && (visitTypeId == 2 || visitTypeId == 3))
					sb.append(" and admission_id =:visitId ");

				Query sqlQuery =  sessionFactory.getCurrentSession()
						.createSQLQuery(sb.toString());

				if(visitTypeId != null && visitTypeId == 1)
					sqlQuery.setParameter("visitId", visitId);

				if(visitTypeId != null && (visitTypeId == 2 || visitTypeId == 3))
					sqlQuery.setParameter("visitId", visitId);

				List<MPreauthRequestMaster> list = (List<MPreauthRequestMaster>) sqlQuery.setResultTransformer(Transformers.aliasToBean(MPreauthRequestMaster.class)).list();

				if (list != null && list.size() > 0) {
					for (Iterator iterator = list.iterator(); iterator.hasNext();) {
						MPreauthRequestMaster dto = (MPreauthRequestMaster) iterator.next();
						htmlData += "        <Authorization> " + "            <Pre_Authorization_Number>"
								+ dto.getRequestNo() + "</Pre_Authorization_Number> "
								+ "            <Pre_Authorization_Amount>" + dto.getRequestAmount()
								+ "</Pre_Authorization_Amount> " + "        </Authorization> ";
					}

				} else {
					htmlData = " <Authorization><Pre_Authorization_Number>12</Pre_Authorization_Number><Pre_Authorization_Amount>0</Pre_Authorization_Amount></Authorization>";
				}
			}
			return htmlData;
		} catch (Exception e) {
			
			throw e;
		}
		//return null;
	}

	@Override
	public String getDiagnosisByVisitId(Integer visitId, Integer visitTypeId, boolean isPharamcy) {
		try {

			String htmlData = "";

			if (isPharamcy) {
				htmlData = "<Diagnosis><Stage>P</Stage><Code_Type>ICD10</Code_Type><Code>UNKN</Code></Diagnosis>";
			} else {
				String query = " select " + "	dtm.diagnosis_type_name as type, " + "	dm.code as code " + " from "
						+ "	patient.t_patient_diagnosis_appo_mapper pdam " + " inner join emr.m_diagnosis_master dm on "
						+ "	dm.diagnosis_id = pdam.diagnosis_id " + " inner join emr.m_diagnosis_type_master dtm on "
						+ "	pdam.\"type\" = dtm.diagnosis_type_id  " + " where pdam.status = 'A' ";

				StringBuffer sb = new StringBuffer(query);

				if(visitTypeId != 0 && visitTypeId == 1)
					sb.append(" and pdam.encounter_id =:visitId ");

				if(visitTypeId != 0 && (visitTypeId == 2 || visitTypeId == 3))
					sb.append(" and pdam.admission_id =:visitId ");

				Query sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(sb.toString());

				if(visitTypeId != 0 && visitTypeId == 1)
					sqlQuery.setParameter("visitId", visitId);

				if(visitTypeId != 0 && (visitTypeId == 2 || visitTypeId == 3))
					sqlQuery.setParameter("visitId", visitId);

				List<SmartCardDiagnosis> list =	sqlQuery.setResultTransformer(Transformers.aliasToBean(SmartCardDiagnosis.class)).list();

				if (list != null && list.size() > 0) {
					for (Iterator iterator = list.iterator(); iterator.hasNext();) {
						SmartCardDiagnosis dto = (SmartCardDiagnosis) iterator.next();

						htmlData += "<Diagnosis> " + "       <Stage>" + dto.getType() + "</Stage> "
								+ "       <Code_Type>ICD10</Code_Type> " + "       <Code>" + dto.getCode() + "</Code> "
								+ "   </Diagnosis> ";
					}
				} else {
					htmlData = "<Diagnosis><Stage>P</Stage><Code_Type>ICD10</Code_Type><Code>UNKN</Code></Diagnosis>";
				}
			}
			return htmlData;
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
//		return null;
	}

	@Override
	public String getReceiptDetailsByBillid(Integer billId, boolean isPharamcy) {
		try {

			String htmlData = "";
			if (isPharamcy) {
				htmlData = "<Payment_Modifier><Type>1</Type><Amount>0</Amount><Receipt>0</Receipt></Payment_Modifier>";
			} else {

				/*String query = "select " + "	prm.receipt_type as \"receiptType\", " + "	prm.amount as \"amount\", "
						+ "	prm.reciept_number as \"recieptNumber\" " + " from "
						+ "	billing.t_payment_reciept_master prm " + " where " + "	prm.billing_master_id = :billId and prm.status = 'A' ";

				List<PaymentRecieptMasterDto> list = sessionFactory.getCurrentSession().createSQLQuery(query)
						.setInteger("billId", billId)
						.setResultTransformer(Transformers.aliasToBean(PaymentRecieptMasterDto.class)).list();

				if (list != null && list.size() > 0) {
					for (Iterator iterator = list.iterator(); iterator.hasNext();) {
						PaymentRecieptMasterDto dto = (PaymentRecieptMasterDto) iterator.next();

						htmlData += "            <Payment_Modifier> " + "                <Type>" + dto.getReceiptType()
								+ "</Type> " + "                <Amount>" + dto.getAmount() + "</Amount> "
								+ "                <Receipt>" + dto.getRecieptNumber() + "</Receipt> "
								+ "            </Payment_Modifier> ";
					}
				} else {*/
					htmlData = "<Payment_Modifier><Type>1</Type><Amount>0</Amount><Receipt>0</Receipt></Payment_Modifier>";
				//}
			}
			return htmlData;
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return null;
	}

	/** @Code Ganesh */
	@Override
	public List<SmartCardBillReqDto> getBillListForSmartCard(Date date) {
		try {
			List<SmartCardBillReqDto> list = sessionFactory.getCurrentSession()
					.createSQLQuery(SmartCardQuries.GET_BILLS_FOR_SMART_CARD).setParameter("fromDate", date)
					.setParameter("toDate", date).setResultTransformer(Transformers.aliasToBean(SmartCardBillReqDto.class))
					.list();
			return list;
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public List<SmartDiagnosisReqDto> getDiagnosis(Integer visitTypeId, Integer visitId) {
		try {

			StringBuffer sb = new StringBuffer(SmartCardQuries.GET_DIAGNOSIS_SMART_CARD);

			if(visitTypeId != 0 && visitTypeId == 1)
				sb.append(" and pdam.encounter_id =:visitId ");

			if(visitTypeId != 0 && (visitTypeId == 2 || visitTypeId == 3))
				sb.append(" and pdam.admission_id =:visitId ");

			Query sqlQuery  = sessionFactory.getCurrentSession().createSQLQuery(sb.toString());

			if(visitTypeId != 0 && visitTypeId == 1)
				sqlQuery.setParameter("visitId", visitId);

			if(visitTypeId != 0 && (visitTypeId == 2 || visitTypeId == 3))
				sqlQuery.setParameter("visitId", visitId);


			List<SmartDiagnosisReqDto> list = sqlQuery.setResultTransformer(Transformers.aliasToBean(SmartDiagnosisReqDto.class)).list();

			return list;
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public List<SmartCardPreauthReqDto> getPreAuthReuest(Integer visitTypeId, Integer visitId) {
		try {
			StringBuffer sb = new StringBuffer(SmartCardQuries.GET_PREAUTHORIZATINON_DETAILS);
			if(visitTypeId != null && visitTypeId == 1)
				sb.append(" and encounter_id =:visitId ");

			if(visitTypeId != null && (visitTypeId == 2 || visitTypeId == 3))
				sb.append(" and admission_id =:visitId ");

			Query sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(sb.toString());

			if(visitTypeId != null && visitTypeId == 1)
				sqlQuery.setParameter("visitId", visitId);

			if(visitTypeId != null && (visitTypeId == 2 || visitTypeId == 3))
				sqlQuery.setParameter("visitId", visitId);

			List<SmartCardPreauthReqDto> list = sqlQuery.setResultTransformer(Transformers.aliasToBean(SmartCardPreauthReqDto.class)).list();

			return list;
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public SmartAdmitionReqDto getAdmissionDetails(Integer admissionId) {
		try {
			SmartAdmitionReqDto admissionDto = (SmartAdmitionReqDto) sessionFactory.getCurrentSession()
					.createSQLQuery(SmartCardQuries.GET_ADMISSION_DETAILS).setParameter("admissionId", admissionId)
					.setResultTransformer(Transformers.aliasToBean(SmartAdmitionReqDto.class)).uniqueResult();

			return admissionDto;
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public List<SmartCardIItemsReqDto> getItemsDetailsByBill(Integer billId, Integer payeeId) {
		try {
			List<SmartCardIItemsReqDto> list = sessionFactory.getCurrentSession()
					.createSQLQuery(SmartCardQuries.GET_ITEM_DETAILS).setParameter("billId", billId)
					.setParameter("payeeId", payeeId)
					.setResultTransformer(Transformers.aliasToBean(SmartCardIItemsReqDto.class)).list();

			return list;
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public List<PaymentRecieptMasterDto> getReceiptDetailsbyBill(Integer billId) {
		try {
			List<PaymentRecieptMasterDto> list = sessionFactory.getCurrentSession()
					.createSQLQuery(SmartCardQuries.RECEIPT_DETAILS).setParameter("billId", billId)
					.setResultTransformer(Transformers.aliasToBean(PaymentRecieptMasterDto.class)).list();
			return list;
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public String getDischargeSummery(Integer admissionId) {
		try {
			String data = "";
			String query = "select " + "	dsfm.discharge_field_desc as \"dischargeFieldDesc\", "
					+ "	dsd.discharge_field_description as \"templateDesc\"	" + " from "
					+ "	adt.m_discharge_summary ds " + " inner join adt.m_discharge_summary_detail dsd on "
					+ "	ds.discharge_summary_id = dsd.discharge_summary_id "
					+ " inner join adt.m_discharge_summary_field_master dsfm on "
					+ "	dsfm.discharge_field_id = dsd.discharge_field_id " + " where "
					+ "	ds.admission_id = :admissionId " + "	and ds.status = 'A'";

			List<SmartCartDischargeSummeryDto> list = (List<SmartCartDischargeSummeryDto>) sessionFactory
					.getCurrentSession().createSQLQuery(query).setParameter("admissionId", admissionId)
					.setResultTransformer(Transformers.aliasToBean(SmartCartDischargeSummeryDto.class)).list();

			if (list != null && list.size() > 0) {
				for (Iterator iterator = list.iterator(); iterator.hasNext();) {
					SmartCartDischargeSummeryDto smartCartDischargeSummeryDto = (SmartCartDischargeSummeryDto) iterator
							.next();
					data += smartCartDischargeSummeryDto.getTemplateDesc();
				}
			}
			return data;
		} catch (Exception e) {
			
			throw e;
		}
		//return null;
	}

	@Override
	public Integer updateSmartCardStatus(Integer billId, boolean isPharamcy) {
		try {
			Integer result = 0;
			if (isPharamcy) {
				result = sessionFactory.getCurrentSession()
						.createSQLQuery("update pharmacy.t_sale set smart_card_submited = true where id =:saleId")
						.setParameter("saleId", billId).executeUpdate();
			} else {
				result = sessionFactory.getCurrentSession().createSQLQuery(
						"update billing.m_billing_master set smart_card_submited = true where billing_master_id =:billId")
						.setParameter("billId", billId).executeUpdate();
			}

			return result;
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Integer updateSmartCardSubmitStatus(Integer billId, boolean isPharamcy) {
		try {
			Integer result = 0;
			if (isPharamcy) {
				result = sessionFactory.getCurrentSession()
						.createSQLQuery("update pharmacy.t_sale set claim_send = true where id =:saleId")
						.setParameter("saleId", billId).executeUpdate();
			} else {
				result = sessionFactory.getCurrentSession().createSQLQuery(
						"update billing.m_billing_master set claim_send = true where billing_master_id =:billId")
						.setParameter("billId", billId).executeUpdate();
			}

			return result;
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Integer cancelFinalBillForCancelDischarge(Integer billDetailsId, Integer orderDetailsId) {

		try {
			Integer result1 = 0;
			Integer result2 = 0;
			result1 = sessionFactory.getCurrentSession().createSQLQuery(
					"update billing.t_billing_details set is_cancelled = 'Y' , status= 'I' where billing_details_id = :billDetailsId")
					.setParameter("billDetailsId", billDetailsId).executeUpdate();
			result1 = sessionFactory.getCurrentSession().createSQLQuery(
					"update public.t_order_details set service_is_billed = 0 where order_details_id = :orderDetailsId")
					.setParameter("orderDetailsId", orderDetailsId).executeUpdate();
			return result1;
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Integer inactivateBillbyBillId(Integer admissionId, Integer billId) {
		try {
			Integer result1 = 0;
			if(billId != null && billId>0) {
			 result1 = sessionFactory.getCurrentSession().createSQLQuery(
						"update billing.m_billing_master set status = 'I',bill_cancelled = 'Y' where billing_master_id = :billId")
						.setParameter("billId", billId).executeUpdate();
			}
			Integer result2 = sessionFactory.getCurrentSession()
					.createSQLQuery("update adt.t_admission set is_final_bill = 'N' where admission_id = :admissionId")
					.setParameter("admissionId", admissionId).executeUpdate();
			return result1;
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Integer getFinalbillByAdmissionId(Integer admissionId) {
		try {

			Integer billId = 0;
			BillingMasterDto dto = (BillingMasterDto) sessionFactory.getCurrentSession()
					.createSQLQuery(" select\r\n" + "	bm.billing_master_id as \"billingMasterId\"\r\n" + " from\r\n"
							+ "	billing.m_billing_master bm\r\n" + " inner join adt.t_admission adm on\r\n"
							+ "	adm.admission_id = bm.admission_id\r\n" + " where\r\n"
							+ "	bm.admission_id =:admissionId\r\n" + "	"
							+ " and bm.bill_cancelled = 'N'\r\n"
							+ "	and bm.is_interim_or_final_bill = 2\r\n" + "	and adm.is_final_bill = 'Y' ")
					.setParameter("admissionId", admissionId)
					.setResultTransformer(Transformers.aliasToBean(BillingMasterDto.class)).uniqueResult();

			billId = (dto != null && dto.getBillingMasterId() > 0) ? dto.getBillingMasterId() : 0;
			return billId;
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public BillingMaster getBillById(Integer billId) throws ApplicationException {
		try {
			BillingMaster billingMaster = findById(billId);
			return billingMaster;
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response updateDisallowedAmountToSelfAmount(CompanyReceiptDetailDto receiptDetailsDto, Integer selfPayeeId) {
		try {
			double deductionAmount = (receiptDetailsDto.getDisallowedAmount() != null
					? receiptDetailsDto.getDisallowedAmount().doubleValue()
					: 0)
					+ (receiptDetailsDto.getOtherDeduction() != null
							? receiptDetailsDto.getOtherDeduction().doubleValue()
							: 0);

			deductionAmount = Double.valueOf(OrderMasterServiceImpl.df2.format(deductionAmount));

			int i = sessionFactory.getCurrentSession()
					.createSQLQuery("update billing.m_billing_master set "
							+ "total_self_payable = total_self_payable + :deductionAmount, "
							+ "total_credit_payable = total_credit_payable - :deductionAmount, "
							+ "disallowed_amount = :disallowedAmt, " + "other_deduction = :otherDeduction, "
							+ "disallowed_against_invoice_dtl_id = :againstInvoiceDtlsId "
							+ "where billing_master_id = :billId ")
					.setParameter("billId", receiptDetailsDto.getBillId())
					.setParameter("againstInvoiceDtlsId", receiptDetailsDto.getCompanyInvoiceDetailId())
					.setParameter("disallowedAmt",
							 Double.parseDouble(OrderMasterServiceImpl.df2
									.format(receiptDetailsDto.getDisallowedAmount().doubleValue())))
					.setParameter("otherDeduction",
							Double.parseDouble(OrderMasterServiceImpl.df2
									.format(receiptDetailsDto.getOtherDeduction().doubleValue())))
					.setParameter("deductionAmount", deductionAmount).executeUpdate();

			String query = " update billing.t_bill_payee_details set "
					+ " bill_amount = coalesce(bill_amount,0) + :deductionAmount , "
					+ " due_amount= coalesce(due_amount,0) + :deductionAmount " + " where bill_id = :billId "
					+ " AND payee_id =:payeeId " + " and status='A' ";

			i = +sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("billId", receiptDetailsDto.getBillId()).setParameter("deductionAmount", deductionAmount)
					.setParameter("payeeId", selfPayeeId != null ? selfPayeeId : sessionService.getSelfPayeeId())
					.executeUpdate();

			for (TCompanyInvoiceServiceDetailsDto serviceDetailsDto : receiptDetailsDto.getServiceDetilsList()) {

				deductionAmount = (serviceDetailsDto.getDisallowedAmount() != null
						? serviceDetailsDto.getDisallowedAmount().doubleValue()
						: 0)
						+ (serviceDetailsDto.getOtherDeduction() != null
								? serviceDetailsDto.getOtherDeduction().doubleValue()
								: 0);

				deductionAmount = Double.valueOf(OrderMasterServiceImpl.df2.format(deductionAmount));

				i = +sessionFactory.getCurrentSession()
						.createSQLQuery("update billing.t_billing_details set "
								+ "self_payable = self_payable + :deductionAmount, "
								+ "credit_payable = credit_payable - :deductionAmount, "
								+ "disallowed_amount = :disallowedAmt, " + "other_deduction = :otherDeduction, "
								+ "disallowed_against_invoice_service_dtl_id = :againstInvoiceServiceDtlsId "
								+ "where order_details_id = :orderDetailsId " + "AND status='A' "
								+ "AND billing_master_id=:billId ")

						.setParameter("billId", serviceDetailsDto.getBillingMasterId())
						.setParameter("orderDetailsId", serviceDetailsDto.getOrderDetailsId())
						.setParameter("againstInvoiceServiceDtlsId", serviceDetailsDto.getInvoiceDetailId())
						.setParameter("disallowedAmt",
								 Double.parseDouble(OrderMasterServiceImpl.df2
										.format(serviceDetailsDto.getDisallowedAmount().doubleValue())))
						.setParameter("otherDeduction",
								 Double.parseDouble(OrderMasterServiceImpl.df2
										.format(receiptDetailsDto.getOtherDeduction().doubleValue())))
						.setParameter("deductionAmount", deductionAmount).executeUpdate();

				i = +sessionFactory.getCurrentSession()
						.createSQLQuery("update t_order_details set " + "self_payable = self_payable + :disallowedAmt, "
								+ "credit_payable = credit_payable-:disallowedAmt, "
								+ "disallowed_amount = :disallowedAmt " + "where order_details_id = :orderDetailsId")

						.setParameter("orderDetailsId", serviceDetailsDto.getOrderDetailsId())
						.setParameter("disallowedAmt", deductionAmount).executeUpdate();

				query = "update t_order_details_payee_mapper set " + "payee_amount = payee_amount - :disallowedAmt "
						+ "where order_details_id = :orderDetailsId " + "AND payee_id =:payeeId AND status='A' ";
				if (receiptDetailsDto.getAssociateCompanyId() > 0)
					query = query.concat(" and associate_company_id =:associateCompanyId ");

				Query sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query)
						.setParameter("orderDetailsId", serviceDetailsDto.getOrderDetailsId())
						.setParameter("disallowedAmt", deductionAmount)
						.setParameter("payeeId", receiptDetailsDto.getPayeeId());

				if (receiptDetailsDto.getAssociateCompanyId() > 0)
					sqlQuery.setParameter("associateCompanyId", receiptDetailsDto.getAssociateCompanyId());

				i =+sqlQuery.executeUpdate();

				i = +sessionFactory.getCurrentSession()
						.createSQLQuery("update t_order_details_payee_mapper set "
								+ "payee_amount = payee_amount + :disallowedAmt "
								+ "where order_details_id = :orderDetailsId " + "AND contract_id is null "
								+ "AND  payee_id =:payeeId AND status='A' ")

						.setParameter("orderDetailsId", serviceDetailsDto.getOrderDetailsId())
						.setParameter("disallowedAmt", deductionAmount)
						.setParameter("payeeId", selfPayeeId != null ? selfPayeeId : sessionService.getSelfPayeeId())
						.executeUpdate();

				query = "update billing.t_bill_details_payee_mapper set "
						+ "payee_amount = payee_amount - :disallowedAmt " + "where order_details_id = :orderDetailsId "
						+ "AND payee_id =:payeeId AND status='A' ";
				if (receiptDetailsDto.getAssociateCompanyId() > 0)
					query = query.concat(" and associate_company_id =:associateCompanyId ");

				Query sqlQuery1 = sessionFactory.getCurrentSession().createSQLQuery(query)
						.setParameter("orderDetailsId", serviceDetailsDto.getOrderDetailsId())
						.setParameter("disallowedAmt", deductionAmount)
						.setParameter("payeeId", receiptDetailsDto.getPayeeId());

				if (receiptDetailsDto.getAssociateCompanyId() > 0)
					sqlQuery1.setParameter("associateCompanyId", receiptDetailsDto.getAssociateCompanyId());

				i = +sqlQuery1.executeUpdate();

				i = +sessionFactory.getCurrentSession()
						.createSQLQuery("update billing.t_bill_details_payee_mapper set "
								+ "payee_amount = payee_amount + :disallowedAmt "
								+ "where order_details_id = :orderDetailsId " + "AND contract_id is null "
								+ "AND payee_id =:payeeId AND status='A' ")

						.setParameter("orderDetailsId", serviceDetailsDto.getOrderDetailsId())
						.setParameter("disallowedAmt", deductionAmount)
						.setParameter("payeeId", selfPayeeId != null ? selfPayeeId : sessionService.getSelfPayeeId())
						.executeUpdate();
			}

			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response updatePharmacyDisallowedAmountToSelfAmount(CompanyReceiptDetailDto receiptDetailsDto,
			Integer selfPayeeId) {
		try {

			double deductionAmount = (receiptDetailsDto.getDisallowedAmount() != null
					? receiptDetailsDto.getDisallowedAmount().doubleValue()
					: 0)
					+ (receiptDetailsDto.getOtherDeduction() != null
							? receiptDetailsDto.getOtherDeduction().doubleValue()
							: 0);

			deductionAmount =  Double.parseDouble(OrderMasterServiceImpl.df2.format(deductionAmount));

			int i = sessionFactory.getCurrentSession()
					.createSQLQuery("update " + "	pharmacy.t_sale " + " set"
							+ "	total_self_payable = total_self_payable + :deductionAmount,"
							+ "	total_credit_payable = total_credit_payable - :deductionAmount,"
							+ "	disallowed_amount = :disallowedAmt," + "	other_deduction = :otherDeduction,"
							+ "	disallowed_against_invoice_dtl_id = :againstInvoiceDtlsId " + " where"
							+ "	id = :saleId")
					.setParameter("saleId", receiptDetailsDto.getSaleId())
					.setParameter("againstInvoiceDtlsId", receiptDetailsDto.getCompanyInvoiceDetailId())
					.setParameter("disallowedAmt", receiptDetailsDto.getDisallowedAmount().doubleValue())
					.setParameter("otherDeduction", receiptDetailsDto.getOtherDeduction().doubleValue())
					.setParameter("deductionAmount", deductionAmount).executeUpdate();

			i = +sessionFactory.getCurrentSession()
					.createSQLQuery("update " + "	pharmacy.t_pharmacy_bill_payee_details " + " set "
							+ "	bill_amount = bill_amount + :deductionAmount, "
							+ "	due_amount = due_amount + :deductionAmount " + " where " + "	t_sale_id = :saleId "
							+ "	and payee_id =:payeeId")
					.setParameter("saleId", receiptDetailsDto.getSaleId()).setParameter("deductionAmount", deductionAmount)
					.setParameter("payeeId", selfPayeeId != null ? selfPayeeId : sessionService.getSelfPayeeId())
					.executeUpdate();

			for (TCompanyInvoiceServiceDetailsDto serviceDetailsDto : receiptDetailsDto.getServiceDetilsList()) {

				deductionAmount = (serviceDetailsDto.getDisallowedAmount() != null
						? serviceDetailsDto.getDisallowedAmount().doubleValue()
						: 0)
						+ (serviceDetailsDto.getOtherDeduction() != null
								? serviceDetailsDto.getOtherDeduction().doubleValue()
								: 0);

				i = +sessionFactory.getCurrentSession()
						.createSQLQuery("update " + "	pharmacy.t_sale_detail " + " set "
								+ "	self_payable = self_payable + :deductionAmount, "
								+ "	credit_payable = credit_payable - :deductionAmount, "
								+ "	disallowed_amount = :disallowedAmt, " + "	other_deduction = :otherDeduction,"
								+ "	disallowed_against_invoice_item_dtl_id = :againstInvoiceItemDtlsId " + " where "
								+ "	id = :saleDetailsId " + "	and sale_id =:saleId ")
						.setParameter("saleId", serviceDetailsDto.getSaleId())
						.setParameter("saleDetailsId", serviceDetailsDto.gettSaleDetailsId())
						.setParameter("againstInvoiceItemDtlsId", serviceDetailsDto.getInvoiceDetailId())
						.setParameter("disallowedAmt", serviceDetailsDto.getDisallowedAmount().doubleValue())
						.setParameter("otherDeduction", serviceDetailsDto.getOtherDeduction().doubleValue())
						.setParameter("deductionAmount", deductionAmount).executeUpdate();

				List<SaleDetailPayeeMapperDto> payeeList = iGlobalDao.getSalePayeeMpperListbyDetailsIdReturn(
						serviceDetailsDto.gettSaleDetailsId(),
						BigDecimal.valueOf(serviceDetailsDto.getDisallowedAmount()), null);
				Double disallowedAmount = serviceDetailsDto.getDisallowedAmount()
						+ serviceDetailsDto.getOtherDeduction();
				for (SaleDetailPayeeMapperDto mppr : payeeList) {
					if (disallowedAmount != null && disallowedAmount > 0) {
						if (mppr.getContractId() != null && mppr.getContractId() > 0) {
							i = +sessionFactory.getCurrentSession()
									.createSQLQuery(" update " + "	pharmacy.t_sale_detail_payee_mapper "
											+ " set payee_id =:payeeId ,contract_id = null " + " where "
											+ "	id = :mpprId ")
									.setParameter("mpprId", mppr.getId())
									.setParameter("payeeId",
											selfPayeeId != null ? selfPayeeId : sessionService.getSelfPayeeId())
									.executeUpdate();
							disallowedAmount = disallowedAmount - mppr.getPayeeAmount().doubleValue();
						}
					}
				}
			}

			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response updatePDD(DischargeDto dischargeDto) {
		try {
			int preAdmissionDetailsId = 0;

			AdmissionDetails admissionDetails = (AdmissionDetails) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ADMISSION_DETAILS_BY_ADMISSION_ID")
					.setParameter("organizationId", dischargeDto.getOrganizationId())
					.setParameter("unitId", dischargeDto.getUnitId())
					.setParameter("admissionId", dischargeDto.getAdmissionId())
					.setResultTransformer(Transformers.aliasToBean(AdmissionDetails.class)).uniqueResult();

			preAdmissionDetailsId = admissionDetails.getAdmissionDetailsId();

			Session session = sessionFactory.getCurrentSession();

			if (dischargeDto.getIsCancel() != '\0') {
				// <-----Updating cancel details to admission details
				Query query = session.createQuery("update AdmissionDetails dis set dis.remark=:remark ,"
						+ "dis.updatedBy=:updatedBy, " + "dis.extendReasonId=:extendReasonId, "
						+ "dis.updatedDate=:updatedDate, " + "dis.isCancel='Y', " + "dis.cancelDate=:updatedDate, "
						+ "dis.cancelBy=:updatedBy " + "where dis.admissionId=:admissionId");

				query.setParameter("updatedBy", dischargeDto.getUpdatedBy());
				query.setParameter("admissionId", dischargeDto.getAdmissionId());
				query.setParameter("extendReasonId", dischargeDto.getReasonId());
				query.setParameter("remark", dischargeDto.getNoteCancel());
				query.setParameter("updatedDate",
						GlobalCommonDateUtils.getDate(dischargeDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
				query.executeUpdate();
				// ----->

				// <-----Updating cancel details to admission details
				Query queryAdmission = session.createQuery(
						"update Admission set isFinancialCreance='N' " + " where admissionId=:admissionId ");
				queryAdmission.setParameter("admissionId", dischargeDto.getAdmissionId());
				queryAdmission.executeUpdate();
				// ----->

				// <-----Updating cancel details to discharge table
				Session sessionDis = sessionFactory.getCurrentSession();
				Query queryDis = sessionDis.createQuery(
						"update Discharge dis set dis.status= 'I', dis.cancelReasonId=:cancelReasonId, dis.noteCancel=:noteCancel  " 
								+ " WHERE dis.status='A' AND dis.dischargeId =:dischargeId ");
				queryDis.setParameter("cancelReasonId", dischargeDto.getReasonId());
				queryDis.setParameter("noteCancel", dischargeDto.getNoteCancel());
				queryDis.setParameter("dischargeId", dischargeDto.getDischargeId());
				queryDis.executeUpdate(); 
				// ----->
			} else {
				// <-----Updating cancel details to admission details
				Query query = session.createQuery(
						"update AdmissionDetails dis set dis.remark=:remark ," + "dis.updatedBy=:updatedBy, "
								+ "dis.extendReasonId=:extendReasonId, " + "dis.updatedDate=:updatedDate, "
								+ "dis.futureDischargeDate=:pdd " + "where dis.admissionId=:admissionId");

				query.setParameter("updatedBy", dischargeDto.getUpdatedBy());
				query.setParameter("admissionId", dischargeDto.getAdmissionId());
				query.setParameter("extendReasonId", dischargeDto.getUpdatedBy());
				query.setParameter("remark", dischargeDto.getRemarkDischarge());
				query.setParameter("updatedDate",
						GlobalCommonDateUtils.getDate(dischargeDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
				query.setParameter("pdd", GlobalCommonDateUtils.getDate(dischargeDto.getPdd(), "dd-M-yyyy HH:mm:ss"));
				query.executeUpdate();
				// ----->
			}

			admissionDetails.setAdmissionDetailsId(null);
			admissionDetails.setPdd(GlobalCommonDateUtils.getDate(dischargeDto.getPdd(), "dd-M-yyyy HH:mm:ss"));
			admissionDetails
					.setCreatedDate(GlobalCommonDateUtils.getDate(dischargeDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			admissionDetails
					.setUpdatedDate(GlobalCommonDateUtils.getDate(dischargeDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			// admissionDetails.setCreatedBy(dischargeDto.getCreatedBy()==0?dischargeDto.getUpdatedBy():dischargeDto.getCreatedBy());
			admissionDetails.setUpdatedBy(dischargeDto.getUpdatedBy());
			if (dischargeDto.getDischargeStatusId() != null && dischargeDto.getDischargeStatusId().intValue() == 6) {
				admissionDetails.setIsDischarge('N');
			}
			admissionDetails.setStatus('A');
			admissionDetails.setNursingAcceptance('Y');
			sessionFactory.getCurrentSession().save(admissionDetails);

			// <-----
			Query query2 = session
					.createQuery("update AdmissionDetails dis set dis.status='I' where dis.admissionDetailsId =:preAdmissionDetailsId ")
							.setParameter("preAdmissionDetailsId", preAdmissionDetailsId);
			query2.executeUpdate();
			// ----->
			// update patient if discharge type id is 4 i.e absconding
			// start
			if (dischargeDto.getDischargeTypeId() != null && dischargeDto.getDischargeTypeId() == 4) {
				Query abscondQuery = session
						.createQuery("update PatientRegistration  set isAbsconded='Y' " + "where patientId=:patientId");
				abscondQuery.setParameter("patientId", dischargeDto.getPatientId());
				abscondQuery.executeUpdate();
			}
			// end
			return new Response(SUCCESS, null, null, null, null);
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response canceDischarge(DischargeDto dischargeDto) {
		try {
			Session session = sessionFactory.getCurrentSession();
			Query query = session.createQuery(
					"update Discharge dis set dis.dischargeStatusId =:dischargeStatusId "
							+ " WHERE dis.status='I' AND dis.dischargeId =:dischargeId ");

			   query.setParameter("dischargeStatusId", dischargeDto.getDischargeStatusId())
					.setParameter("dischargeId", dischargeDto.getDischargeId())
					.executeUpdate();

			sessionFactory.getCurrentSession().createQuery(
					"update Admission adm set adm.isFinalBill= 'N' WHERE adm.status='A' AND adm.admissionId =:admissionId ")
					.setParameter("admissionId", dischargeDto.getAdmissionId());
			query.executeUpdate();
			return new Response(SUCCESS, null, null, null, null);
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
	//	return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateBillsDisallowedAmount(Double disallowedAmount, Integer billingMasterId) {
		try {
			int i = sessionFactory.getCurrentSession().createSQLQuery("update billing.m_billing_master set "
					+ "disallowed_amount = disallowed_amount - :disallowedAmt " + "where billing_master_id = :billId ")
					.setParameter("billId", billingMasterId)
					.setParameter("disallowedAmt", disallowedAmount)
					.executeUpdate();

			return new Response(SUCCESS, null, null, null, null);
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, null, null, null, null);
	}

	@Override
	public BillingMaster getBillMasterObject(Integer billId) {
		try {
			BillingMaster billingMaster = findById(billId);
			return billingMaster;
		} catch (Exception e) {
			
			throw e;
		}
		//return null;
	}

	@Override
	public Response updateBillDtlsForDocShare(Integer billingDetailsId, Integer doctorId, Integer docSpecialityId)
			throws BillingException {
		try {
			Integer result = sessionFactory
					.getCurrentSession().createSQLQuery("update billing.t_billing_details set ord_doc_id =:doctorId, "
							+ " ord_doc_spl_id =:docSpecialityId where billing_details_id =:billingDetailsId ")
					.setParameter("doctorId", doctorId)
					.setParameter("docSpecialityId", docSpecialityId)
					.setParameter("billingDetailsId", billingDetailsId)
					.executeUpdate();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, result);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response getBadDebitListByFilterForBadDebitList(BillListReqDto reqDto) throws BillingException {
		try {
			boolean dateFlag = false;

			String query = "select bill.billing_master_id as \"billingMasterId\",  "
					+ "	To_char(bill.bill_date_time,  " + "	'DD-MM-YYYY HH24:MI:SS') as \"billDate\",  "
					+ "	bill.bill_number as \"billNumber\",  " + "	pat.uhid_number as \"uhidNumber\",  "
					+ "	concat(coalesce(pre.prefix_code,''),' ',pat.first_name, ' ', pat.middle_name, ' ', pat.last_name) as \"patientName\",  "
					+ "	bill.visit_type_id as \"visitTypeId\",  " + "	(case when empmst.employee_id is not null then "
					+ "		concat(empmst.first_name, ' ', empmst.last_name)  "
					+ "	when docmst.doctor_id is not null then "
					+ "		concat('Dr. ',docmst.first_name, ' ', docmst.last_name) " + "	else " + "		'Admin' "
					+ "	end) " + " as \"userName\",  " + "	pat.is_otc_reg as \"isOtcReg\",  "
					+ "	bill.organisation_id as \"orgId\",  " + "	bill.unit_id as \"unitId\",  "
					+ "	bill.patient_id as \"patientId\",  " + "	bill.is_dr_note as \"isDrNote\",  "
					+ "	billagainst.bill_number as \"againstbillNumber\",  "
					+ " spl.is_nursing_assessment_required as \"isNursingAssessmentRequired\", "
					+ "	bill.disallowed_amount as \"disallowedAmount\", "
					+ "	bill.disallowed_against_invoice_dtl_id as \"disallowedAgainstInvoiceDtlsId\", "
					+ " case when bill.is_interim_or_final_bill = 2 then ( " + "cast(coalesce((select"
					+ "		sum(due_amount)" + "	from" + "		billing.t_bill_payee_details" + "	where"
					+ "		payee_id = (" + "		select" + "			self_payee_id" + "		from"
					+ "			t_unit_application_billing_configuration" + "		where"
					+ "			unit_id = :unitId )" + "		and bill_id in (" + "		select"
					+ "			billing_master_id" + "		from" + "			billing.m_billing_master"
					+ "		where" + "			admission_id = bill.admission_id  "
					+ "			and is_interim_or_final_bill = 1)),0) as double precision))end as \"intermediateSelfDue\","

					+ " coalesce( case when bill.visit_type_id = 1 then ( select referral_id from t_referal_details where encounter_id = bill.encounter_id and referral_type_id = 1 ) end, case when bill.visit_type_id = 2 or bill.visit_type_id = 3 or bill.visit_type_id = 4 then ( select referral_id from t_referal_details where admission_id = bill.admission_id and referral_type_id = 1 ) end ) as \"referalDocId\", "

					+ "	coalesce(  " + "	case  " + "		when bill.visit_type_id = 1 then (  " + "		select  "
					+ "			enc.doctor_id  " + "		from  " + "			opd.t_encounter_master enc  "
					+ "		where  " + "			enc.encounter_id = bill.encounter_id )  " + "	end,  " + "	case  "
					+ "		when bill.visit_type_id = 2  " + "		or bill.visit_type_id = 3  "
					+ "		or bill.visit_type_id = 4 then (  " + "		select  " + "			adm.doctor_id  "
					+ "		from  " + "			adt.t_admission adm  " + "		where  "
					+ "			adm.admission_id = bill.admission_id   "
					/* + "			and adm.status='A'   " */
					+ "		)  " + "	end " + ") as \"doctorId\",  "

					+ "	coalesce(  " + "	case  " + "		when bill.visit_type_id = 1 then (  " + "		select  "
					+ "			enc.encounter_number  " + "		from  " + "			opd.t_encounter_master enc  "
					+ "		where  " + "			enc.encounter_id = bill.encounter_id )  " + "	end,  " + "	case  "
					+ "		when bill.visit_type_id = 2  " + "		or bill.visit_type_id = 3  "
					+ "		or bill.visit_type_id = 4 then (  " + "		select  " + "			adm.admission_number  "
					+ "		from  " + "			adt.t_admission adm  " + "		where  "
					+ "			adm.admission_id = bill.admission_id   "
					/* + "			and adm.status='A'   " */
					+ "		)  " + "	end " + ") as \"visitNo\",  " + "	coalesce(  " + "	case  "
					+ "		when bill.visit_type_id = 1 then (  " + "		select  "
					+ "			To_char(enc.encounter_date,  " + "			'DD/MM/YYYY')  " + "		from  "
					+ "			opd.t_encounter_master enc  " + "		where  "
					+ "			enc.encounter_id = bill.encounter_id )  " + "	end,  " + "	case  "
					+ "		when bill.visit_type_id = 2  " + "		or bill.visit_type_id = 3  "
					+ "		or bill.visit_type_id = 4 then (  " + "		select  "
					+ "			To_char(adm.created_date,  " + "			'DD/MM/YYYY')  " + "		from  "
					+ "			adt.t_admission adm  " + "		where  "
					+ "			adm.admission_id = bill.admission_id  "
					/* + "			and adm.status = 'A'   " */
					+ "			)  " + "	end" + " ) as \"visitDate\",  " + "	coalesce(  " + "	case  "
					+ "		when bill.visit_type_id = 1 then (  " + "		select  " + "			enc.encounter_id  "
					+ "		from  " + "			opd.t_encounter_master enc  " + "		where  "
					+ "			enc.encounter_id = bill.encounter_id )  " + "	end,  " + "	case  "
					+ "		when bill.visit_type_id = 2  " + "		or bill.visit_type_id = 3  "
					+ "		or bill.visit_type_id = 4 then (  " + "		select  " + "			adm.admission_id  "
					+ "		from  " + "			adt.t_admission adm  " + "		where  "
					+ "			adm.admission_id = bill.admission_id "
					/* + "			and adm.status='A' " */
					+ "		)  " + "	end" + " ) as \"visitId\",  " + "	coalesce(  " + "	case  "
					+ "		when bill.visit_type_id = 1 then (  " + "		select  "
					+ "			enc.appointment_id  " + "		from  " + "			opd.t_encounter_master enc  "
					+ "		where  " + "			enc.encounter_id = bill.encounter_id )  "
					+ "	end ) as \"appointmentId\",  " + "	bill.total_bill_amount as \"totalBillAmount\",  "
					+ "	bill.net_amount as \"netAmount\",  "
					+ "	coalesce((bill.net_amount - bill.outstanding), 0) as \"amountPaid\",  "
					+ "	bill.outstanding as \"outstanding\",  " + "	bill.bill_status_id as \"billStatusId\",  "
					+ "	bill.is_interim_or_final_bill as \"isInterimOrFinalBill\", "
					+ " bill.total_self_payable as \"totalSelfPayable\", "
					+ " bill.total_credit_payable as \"totalCreditPayable\" " + "from  "
					+ "	billing.m_billing_master bill  " + "left join billing.m_billing_master billagainst on  "
					+ "	bill.dr_note_against_billid = billagainst.billing_master_id  "
					+ "inner join patient.t_patient_registration pat on  " + "	pat.patient_id = bill.patient_id  "
					+ " left join m_prefix_master pre on " + " pre.prefix_id = pat.prefix_id "
					+ "left join PUBLIC.m_employee_master empmst on  " + "	bill.created_by = empmst.user_id  "
					+ "left join doctor.m_doctor_master docmst on " + "bill.created_by = docmst.user_id "
					+ "left join opd.t_encounter_master enc on " + "enc.encounter_id = bill.encounter_id "
					+ " left join t_unit_speciality_mapper spl on " + " 	spl.speciality_id = enc.dept_id "
					+ " 	and spl.unit_id =enc.unit_id  " + "where  " + "	bill.bill_cancelled = 'N'  "
					+ " and bill.organisation_id =:orgId " + " and bill.unit_id =:unitId and bill.bill_status_id != 3";

			// Date fromDate = reqDto.getFromDate() != null &&
			// !reqDto.getFromDate().isEmpty() ?
			// GlobalCommonDateUtils.getDate(reqDto.getFromDate(), "yyyy-MM-dd"): null;
			// Date toDate = reqDto.getToDate() != null && !reqDto.getToDate().isEmpty()?
			// GlobalCommonDateUtils.getDate(reqDto.getToDate(), "dd-M-yyyy"): null;

			// StringBuilder query = new
			// StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_BILL_DETAILS_FOR_BAD_DABIT_LIST_BY_FILTER").getQueryString());

			StringBuffer sb = new StringBuffer(query);

			dateFlag = true;

			if (dateFlag)
				sb.append(" and to_char(bill.bill_date_time,'YYYY-MM-DD') <=	:fromDate ");

			if(reqDto.getVisitTypeId() != null)
				sb.append(" and bill.visit_type_id =:visitTypeId ");

			if(reqDto.getBillNo() != null && !reqDto.getBillNo().isEmpty())
				sb.append(" and trim(lower(bill.bill_number))like :billNo ");

			if(reqDto.getVisitNo() != null && !reqDto.getVisitNo().isEmpty())
				sb.append(" and trim(lower(bill.\"visitNo\")) like :visitNo ");

			if(reqDto.getBillStatusId() != null)
				sb.append(" and bill.bill_status_id =:billStatusId ");

			if(reqDto.getPatientId() != null)
				sb.append(" and bill.patient_id =:patientId ");

			if(reqDto.getPayeeId() != null && reqDto.getPayeeId() > 0)
				sb.append(" and bill.billing_master_id IN (select bill_id from billing.t_bill_payee_details where payee_id =:payeeId and status='A')");
			
			sb.append(" order by bill.bill_date_time desc");

			// sb.append(" order by bill.\"billDate\" desc ");
			List<BillListResDto> listBills = null;

			Query sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(sb.toString())
							.setParameter("orgId", reqDto.getOrgId())
							.setParameter("unitId", reqDto.getUnitId());

			if (dateFlag)
				sqlQuery.setParameter("fromDate", reqDto.getFromDate());

			if(reqDto.getVisitTypeId() != null)
				sqlQuery.setParameter("visitTypeId", reqDto.getVisitTypeId());

			if(reqDto.getBillNo() != null && !reqDto.getBillNo().isEmpty())
				sqlQuery.setParameter("billNo", "%" + reqDto.getBillNo().toLowerCase().trim() + "%");

			if(reqDto.getVisitNo() != null && !reqDto.getVisitNo().isEmpty())
				sqlQuery.setParameter("visitNo", "%" + reqDto.getVisitNo().toLowerCase().trim() + "%");

			if(reqDto.getBillStatusId() != null)
				sqlQuery.setParameter("billStatusId", reqDto.getBillStatusId());

			if(reqDto.getPatientId() != null)
				sqlQuery.setParameter("patientId", reqDto.getPatientId());

			if(reqDto.getPayeeId() != null && reqDto.getPayeeId() > 0)
				sqlQuery.setParameter("payeeId", reqDto.getPayeeId());

			listBills = sqlQuery.setResultTransformer(Transformers.aliasToBean(BillListResDto.class)).list();

			return new Response<>(SUCCESS, SUCCESS_CODE, null, listBills, null);
		} catch (Exception e) {
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	public Response changePatientClassId(ServiceMasterDto serviceMasterDto) throws BillingException {
		try {
			String query = "";
			if (serviceMasterDto.getVisitTypeId() != null && serviceMasterDto.getVisitTypeId() == OP) {
				query = " UPDATE opd.t_encounter_master SET patient_class_id ="+serviceMasterDto.getPatientClassId()+", "
						+ " reason_id_for_change_class = "+ serviceMasterDto.getReasonIdForChangePatientClass()
						+ " WHERE encounter_id =:visitId and organization_id =:organizationId "
						+ " and unit_id =:unitId ";
			} else {
				query = " UPDATE adt.t_admission SET patient_class_id ="+serviceMasterDto.getPatientClassId()+", "
						+ " reason_id_for_change_class = "+ serviceMasterDto.getReasonIdForChangePatientClass()
						+ " WHERE admission_id =:visitId and organization_id =:organizationId "
						+ " and unit_id =:unitId ";
			}

			SQLQuery sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query);
			sqlQuery.setParameter("visitId", serviceMasterDto.getVisitId())
				.setParameter("organizationId", serviceMasterDto.getOrganizationId())
				.setParameter("unitId", serviceMasterDto.getUnitId())
				.executeUpdate();

			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	public Response billListByPatientIdForConcessionManager(int orgId, int unitId, int patientId) {
		try {

			List<BillListCNDto> listBillListCNDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BILL_DETAILS_FOR_BILL_LIST_PATIENT").setParameter("patientId", patientId)
					.setParameter("orgId", orgId).setParameter("unitId", unitId)
					.setParameterList("billStatusIds", Arrays.asList(2, 1))
					.setParameterList("intremediateFinal", Arrays.asList(1, 2))
					.setParameterList("isDrCr", Arrays.asList('N'))
					.setResultTransformer(Transformers.aliasToBean(BillListCNDto.class)).list();

			// List<BillListCNDto> filteredDebitNoteList =
			// listBillListCNDto.stream().filter(bill->bill.getStatus()=='A').collect(Collectors.toList());
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listBillListCNDto, null);

		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);

	}

	@Override
	public Response inactivateBillAndBillDetails(BillingMaster billingMasterDto) {
		try {

			List<Integer> billDetailsId = new LinkedList<Integer>();

			sessionFactory.getCurrentSession().update(billingMasterDto);

			sessionFactory.getCurrentSession().createSQLQuery("update billing.t_bill_payee_details " + "set status='I' "
					+ "where bill_id=:billingMasterId ")
					.setParameter("billingMasterId", billingMasterDto.getBillingMasterId())
					.executeUpdate();

			com.param.billing.common.Response res = iBillingDetailsDao
					.getBillDetailsObjByBillId(billingMasterDto.getBillingMasterId());

			List<BillingDetails> billingDetailsList = res.getListObject();

			for (BillingDetails billingDetails : billingDetailsList) {
				billDetailsId.add(billingDetails.getBillingDetailsId());

				sessionFactory.getCurrentSession().createSQLQuery(
						"update billing.t_billing_details set is_cancelled = 'Y' , status= 'I' where billing_details_id = :billDetailsId")
						.setParameter("billDetailsId", billingDetails.getBillingDetailsId()).executeUpdate();

				sessionFactory.getCurrentSession().createSQLQuery(
						"update public.t_order_details set service_is_billed = 0 where order_details_id = :orderDetailsId")
						.setParameter("orderDetailsId", billingDetails.getOrderDetailsId()).executeUpdate();

			}

			sessionFactory.getCurrentSession().createSQLQuery(
					"update billing.t_bill_details_payee_mapper set status= 'I' where bill_details_id IN (:billDetailsIds) ")
					.setParameterList("billDetailsIds", billDetailsId).executeUpdate();

			sessionFactory.getCurrentSession().createSQLQuery(
					"update billing.t_bill_details_package_mapper set status= 'I' where bill_details_id IN (:billDetailsIds) ")
					.setParameterList("billDetailsIds", billDetailsId).executeUpdate();

			String stringQuery = "select id from pharmacy.t_sale ts where bill_id =:billingMasterId ";

			List<Integer> saleIds = sessionFactory.getCurrentSession().createSQLQuery(stringQuery)
					.setParameter("billingMasterId", billingMasterDto.getBillingMasterId())
					.list();

			if (saleIds != null && saleIds.size() > 0) {
				String sql = "update pharmacy.t_sale set status_id = 2 ,is_consolidated_bill = false , paid_amount = 0,outstanding_amount = net_amount where id in (:saleIds) ";
				Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
				query.setParameterList("saleIds", saleIds);
				query.executeUpdate();

				String sql2 = "update pharmacy.t_pharmacy_bill_payee_details set due_amount = bill_amount "
						+ "	where t_sale_id in (:saleIds) ";
				Query query2 = sessionFactory.getCurrentSession().createSQLQuery(sql2);
				query2.setParameterList("saleIds", saleIds);
				query2.executeUpdate();
			}

		
			
			String query = "INSERT INTO t_order_details_billing_details_mapper ( order_details_id	,billing_master_id ,billing_details_id ,order_id, created_by, updated_by, updated_date,order_qty,	 service_id,	priority_id,	is_outsourced	,org_id	,org_unit_id,	 order_date	, order_sch_date	,service_amt,	ord_concession,	ord_discount,	net_amount	,tariff_id	,billing_class_id	,bed_id,	room_id	,ward_id	,order_emergency_flag,	 package_id,	 ord_doc_id,	 drug_id	,batch_id,	old_ord_dtl_id,	service_rendered,	service_chargeable,	service_rendering_deptid,	ord_render_datetime	,service_bill_id,	ord_remarks	, created_date,	ord_cancelled,	ord_cancel_reason_id,	ord_cancel_datetime,	 status,	service_is_billed	,is_drug,	 ord_doc_spl_id,	 tax_id,	 tax_per,	tax_amount	, self_payable	, credit_payable,	 payee_id,	 package_consumption_amt,	order_set_id,	contract_id	,bed_category_id	, co_pay_per	, doc_share_per,	 ord_total_amt	,ord_cancel_by	,ord_cancel_remark,	is_package,	is_cap_service,	is_package_service	,is_investigation_order	,is_posted_while_discharge	,is_procedure,	is_procedure_service,	procedure_order_mst_id,	is_appointable_radiology,	package_either_or_group_id	,sale_detail_id	,markup_status,	patient_class_percentage,	debit_note_service	, disallowed_amount	,accession_number,	is_from_addservice,	is_service_reviewed,	dummy_service_amt,	ref_doc_share_per,	is_consultation_service,	force_add_removed,	is_consumed,	is_order_visible_dept,	review_applicable_date	,nursing_service_order_flag,	discount_category_id,	discount_ownership,	hospital_share,	p_image,	p_image_name,	is_charged_half_day,	patient_coupon_template_mapper_id,	cov_template_id	)  " + 
					"select tod.order_details_id	,tbd.billing_master_id ,tbd.billing_details_id ,order_id,tod.created_by,tod.updated_by,tod.updated_date,order_qty,	tod.service_id,	priority_id,	is_outsourced	,org_id	,org_unit_id,	tod.order_date	,tod.order_sch_date	,service_amt,	ord_concession,	ord_discount,	net_amount	,tariff_id	,billing_class_id	,bed_id,	room_id	,ward_id	,order_emergency_flag,	tod.package_id,	tod.ord_doc_id,	tod.drug_id,	tod.batch_id,	old_ord_dtl_id,	service_rendered,	service_chargeable,	service_rendering_deptid,	ord_render_datetime	,service_bill_id,	ord_remarks	,tod.created_date,	ord_cancelled,	ord_cancel_reason_id,	ord_cancel_datetime,	tod.status,	service_is_billed	,is_drug,	tod.ord_doc_spl_id,	tod.tax_id,	tod.tax_per,	tod.tax_amount	,tod.self_payable	,tod.credit_payable,	tod.payee_id,	tod.package_consumption_amt,	order_set_id,	contract_id,	bed_category_id	,tod.co_pay_per	,tod.doc_share_per,	tod.ord_total_amt	,ord_cancel_by	,ord_cancel_remark,	is_package,	is_cap_service,	is_package_service	,is_investigation_order	,is_posted_while_discharge	,is_procedure,	is_procedure_service,	procedure_order_mst_id,	is_appointable_radiology,	package_either_or_group_id	,sale_detail_id	,markup_status,	patient_class_percentage,	debit_note_service	,tod.disallowed_amount	,accession_number,	is_from_addservice,	is_service_reviewed,	dummy_service_amt,	ref_doc_share_per,	is_consultation_service,	force_add_removed,	is_consumed,	is_order_visible_dept,	review_applicable_date	,nursing_service_order_flag,	discount_category_id,	discount_ownership,	hospital_share,	p_image,	p_image_name,	is_charged_half_day,	patient_coupon_template_mapper_id,	cov_template_id	 " + 
					"from t_order_details tod inner join billing.t_billing_details tbd  on tbd.order_details_id = tod.order_details_id  where tbd.billing_master_id =:billingMasterId ";

			Query query2 = sessionFactory.getCurrentSession().createSQLQuery(query);
				  query2.setParameter("billingMasterId", billingMasterDto.getBillingMasterId())
					    .executeUpdate();
			
			return new Response<>(SUCCESS, SUCCESS_CODE, "Bill Cancelled...!!", null, null);

		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	public Response cancelReciept(BillingMasterDto billingMasterDto) {
		try {

			String query = " UPDATE billing.t_payment_reciept_master SET " + "	reciept_cancelled='Y' ,	"
					+ "	status='I' , reciept_cancel_reason_id =:billCancelReasonId, "
					+ " reciept_cancel_datetime=:date, " + " reciept_cancel_by =:billCancelBy, "
					+ " reciept_cancel_remark =:billCancelRemark "
					+ " WHERE payment_reciept_id =:paymentReceiptId ";

			SQLQuery sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query);
			sqlQuery.setParameter("date", TimeZoneComponent
					.getDateByzone(billingMasterDto.getUnitId() != null ? billingMasterDto.getUnitId() : 0))
					.setParameter("billCancelReasonId", billingMasterDto.getBillCancelReasonId())
					.setParameter("billCancelBy", billingMasterDto.getBillCancelBy())
					.setParameter("billCancelRemark", billingMasterDto.getBillCancelRemark())
					.setParameter("paymentReceiptId", billingMasterDto.getPaymentRecieptId());
			sqlQuery.executeUpdate();

			return new Response<>(SUCCESS, SUCCESS_CODE, "Receipt Cancelled...!!", null, null);

		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	public Response getBillListByFilterForbillCancellationList(BillListReqDto reqDto) throws BillingException {
		try {

			String query = "select bill.billing_master_id as \"billingMasterId\",  "
					+ "	To_char(bill.bill_date_time,  " + "	'DD-MM-YYYY HH24:MI:SS') as \"billDate\",  "
					+ "	bill.bill_number as \"billNumber\",  " + "	pat.uhid_number as \"uhidNumber\",  "
					+ "	concat(coalesce(pre.prefix_code,''),' ',pat.first_name, ' ', pat.middle_name, ' ', pat.last_name) as \"patientName\",  "
					+ "	bill.visit_type_id as \"visitTypeId\",  " + "	(case when empmst.employee_id is not null then "
					+ "		concat(empmst.first_name, ' ', empmst.last_name)  "
					+ "	when docmst.doctor_id is not null then "
					+ "		concat('Dr. ',docmst.first_name, ' ', docmst.last_name) " + "	else " + "		'Admin' "
					+ "	end) " + " as \"userName\",  " + "	pat.is_otc_reg as \"isOtcReg\",  "
					+ "	bill.organisation_id as \"orgId\",  " + "	bill.unit_id as \"unitId\",  "
					+ "	bill.patient_id as \"patientId\",  " + "	bill.is_dr_note as \"isDrNote\",  "
					+ "	billagainst.bill_number as \"againstbillNumber\",  "
					+ " spl.is_nursing_assessment_required as \"isNursingAssessmentRequired\", "
					+ "	bill.disallowed_amount as \"disallowedAmount\", "
					+ "	bill.disallowed_against_invoice_dtl_id as \"disallowedAgainstInvoiceDtlsId\", "
					+ "	bill.total_bill_amount as \"totalBillAmount\",  " + "	bill.net_amount as \"netAmount\",  "
					+ "	coalesce((bill.net_amount - bill.outstanding), 0) as \"amountPaid\",  "
					+ "	bill.outstanding as \"outstanding\",  " + "	bill.bill_status_id as \"billStatusId\",  "
					+ "	bill.is_interim_or_final_bill as \"isInterimOrFinalBill\", "
					+ " bill.total_self_payable as \"totalSelfPayable\", "
					+ " bill.total_credit_payable as \"totalCreditPayable\" " + "from  "
					+ "	billing.m_billing_master bill  " + "left join billing.m_billing_master billagainst on  "
					+ "	bill.dr_note_against_billid = billagainst.billing_master_id  "
					+ "inner join patient.t_patient_registration pat on  " + "	pat.patient_id = bill.patient_id  "
					+ "	left join m_prefix_master pre on " + "	pre.prefix_id = pat.prefix_id "
					+ "left join PUBLIC.m_employee_master empmst on  " + "	bill.created_by = empmst.user_id  "
					+ "left join doctor.m_doctor_master docmst on " + "bill.created_by = docmst.user_id "
					+ "left join opd.t_encounter_master enc on " + "enc.encounter_id = bill.encounter_id  "
					+ " left join t_unit_speciality_mapper spl on " + " 	spl.speciality_id = enc.dept_id "
					+ " 	and spl.unit_id =enc.unit_id "
					+ " where 	bill.bill_cancelled = 'N'  "
					+ " and bill.organisation_id =:orgId "
					+ " and bill.unit_id =:unitId and bill.bill_status_id IN (1,2,3)";

			Date fromDate = reqDto.getFromDate() != null && !reqDto.getFromDate().isEmpty()
					? GlobalCommonDateUtils.getDate(reqDto.getFromDate(), "dd-M-yyyy")
					: null;
			Date toDate = reqDto.getToDate() != null && !reqDto.getToDate().isEmpty()
					? GlobalCommonDateUtils.getDate(reqDto.getToDate(), "dd-M-yyyy")
					: null;


			StringBuffer sb = new StringBuffer(query);

			sb.append(" and to_char(bill.bill_date_time,'YYYY-MM-DD') between :fromDate and :toDate ");

			if(reqDto.getVisitTypeId() != null)
				sb.append(" and bill.visit_type_id =:visitTypeId ");

			if(reqDto.getBillNo() != null && !reqDto.getBillNo().isEmpty())
				sb.append(" and trim(lower(bill.bill_number))like :billNo ");

			if(reqDto.getVisitNo() != null && !reqDto.getVisitNo().isEmpty())
				sb.append(" and trim(lower(bill.\"visitNo\")) like :visitNo ");

			if(reqDto.getBillStatusId() != null)
				sb.append(" and bill.bill_status_id =:billStatusId ");

			if(reqDto.getPatientId() != null)
				sb.append(" and bill.patient_id =:patientId ");
			
			if (reqDto.getSearchKeyword() != null && !reqDto.getSearchKeyword().isEmpty()) {
			    sb.append(" AND ( "
			            + " lower(bill.bill_number) like :searchKeyword "
			            + " OR lower(pat.uhid_number) like :searchKeyword "
			            + " OR lower(concat(coalesce(pre.prefix_code,''),' ',pat.first_name, ' ', pat.middle_name, ' ', pat.last_name)) like '%" + reqDto.getSearchKeyword().toLowerCase() + "%' "
			            + " OR lower(enc.encounter_number) like :searchKeyword "
			            + " ) ");
			}
			sb.append(" order by bill.bill_date_time desc");

			List<BillListResDto> listBills = null;

				Query sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(sb.toString())
						.setParameter("orgId", reqDto.getOrgId())
						.setParameter("unitId", reqDto.getUnitId())
						.setParameter("fromDate", GlobalCommonDateUtils.getStringDate(fromDate, "yyyy-MM-dd"))
						.setParameter("toDate",  GlobalCommonDateUtils.getStringDate(toDate, "yyyy-MM-dd"));

			if(reqDto.getVisitTypeId() != null)
				sqlQuery.setParameter("visitTypeId", reqDto.getVisitTypeId());

			if(reqDto.getBillNo() != null && !reqDto.getBillNo().isEmpty())
				sqlQuery.setParameter("billNo", "%" + reqDto.getBillNo().toLowerCase().trim() + "%");

			if(reqDto.getVisitNo() != null && !reqDto.getVisitNo().isEmpty())
				sqlQuery.setParameter("visitNo", "%" + reqDto.getVisitNo().toLowerCase().trim() + "%");

			if(reqDto.getBillStatusId() != null)
				sqlQuery.setParameter("billStatusId", reqDto.getBillStatusId());

			if(reqDto.getPatientId() != null)
				sqlQuery.setParameter("patientId", reqDto.getPatientId());

			if(reqDto.getSearchKeyword() != null && !reqDto.getSearchKeyword().isEmpty())
				sqlQuery.setParameter("searchKeyword", "%" + reqDto.getSearchKeyword().toLowerCase() + "%");


			listBills = sqlQuery.setFirstResult(reqDto.getOffset() != null ? reqDto.getOffset() : 0)
						.setMaxResults(reqDto.getNoOfRecordsPerPage() != null ? reqDto.getNoOfRecordsPerPage() : 10)
						.setResultTransformer(Transformers.aliasToBean(BillListResDto.class)).list();
			
			
			if(listBills != null && listBills.size()>0){
				
				List<Integer> billingMasterIds = listBills.stream().map(BillListResDto::getBillingMasterId).collect(Collectors.toList());
				
				if(billingMasterIds != null && billingMasterIds.size()>0){
					
					List<BillListResDto> adminAndEncounterDetailsList =  sessionFactory.getCurrentSession().createSQLQuery(
							" select "
							+ " trd.referral_id as \"referalDocId\", "
							+ " enc.doctor_id as \"doctorId\", "
							+ " enc.encounter_number as \"visitNo\", "
							+ " To_char(enc.encounter_date,'DD/MM/YYYY') as \"visitDate\", "
							+ " enc.encounter_id as \"visitId\", "
							+ " enc.appointment_id as \"appointmentId\", "
							+ " bill.billing_master_id as \"billingMasterId\" "
							+ " from "
							+ " billing.m_billing_master bill "
							+ " inner join opd.t_encounter_master enc on "
							+ " bill.encounter_id = enc.encounter_id "
							+ " left join t_referal_details trd on "
							+ " trd.encounter_id = enc.encounter_id "
							+ " where "
							+ " bill.encounter_id = enc.encounter_id "
							+ " and bill.unit_id  =:unitId "
							+ " and bill.visit_type_id = 1 and bill.billing_master_id in (:billingMasterIds) "
							+ " union all "
							+ " select "
							+ " trd.referral_id as \"referalDocId\",  "
							+ " ta.doctor_id as \"doctorId\",  "
							+ " ta.admission_number as \"visitNo\", "
							+ " To_char(ta.created_date,'DD/MM/YYYY') as \"visitDate\", "
							+ "	ta.admission_id as \"visitId\", "
							+ " 0 as \"appointmentId\", "
							+ " bill.billing_master_id as \"billingMasterId\" "
							+ " from "
							+ " billing.m_billing_master bill "
							+ " inner join adt.t_admission ta on "
							+ " bill.admission_id = ta.admission_id "
							+ " left join t_referal_details trd on "
							+ " trd.admission_id = ta.admission_id "
							+ " where "
							+ " bill.admission_id = ta.admission_id"
							+ " and bill.unit_id  =:unitId "
							+ " and bill.visit_type_id <> 1 and bill.billing_master_id in (:billingMasterIds) ")
							.setParameter("unitId", reqDto.getUnitId())
							.setParameterList("billingMasterIds", billingMasterIds)
//							.setParameterList("billingMasterIds", billingMasterIds.toString().replaceAll("\\[", "").replaceAll("\\]", ""))
							.setResultTransformer(Transformers.aliasToBean(BillListResDto.class)).list();
					
					
					
					if(adminAndEncounterDetailsList != null && adminAndEncounterDetailsList.size()>0){
						
						for (BillListResDto dto : listBills) {
							
							BillListResDto billObj = adminAndEncounterDetailsList.stream()
									  .filter(order -> dto.getBillingMasterId().equals(order.getBillingMasterId()))
									  .findAny()
									  .orElse(null);
							
							if(billObj!=null){
								
								dto.setReferalDocId(billObj.getReferalDocId());
								dto.setDoctorId(billObj.getDoctorId());
								dto.setVisitNo(billObj.getVisitNo());
								dto.setVisitDate(billObj.getVisitDate());
								dto.setAppointmentId(billObj.getAppointmentId());
								dto.setVisitId(billObj.getVisitId());
								
							}
							
						}
						
					}
					
					
					List<BillListResDto> adminDuaAmount =  sessionFactory.getCurrentSession().createSQLQuery(
							" 	select "
							+ " cast(coalesce( sum(due_amount),0)as double precision) as \"intermediateSelfDue\" ,"
							+ " mbm.billing_master_id as \"billingMasterId\" "
							+ " from billing.t_bill_payee_details tbpd"
							+ " inner join t_unit_application_billing_configuration tubc on"
							+ " tubc.self_payee_id = tbpd.payee_id"
							+ " inner join billing.m_billing_master mbm on"
							+ " mbm.billing_master_id = tbpd.bill_id"
							+ " and mbm.is_interim_or_final_bill =1 "
							+ " and mbm.status = 'A'"
							+ " and mbm.billing_master_id  in (:billingMasterIds) and mbm.unit_id  =:unitId "
							+ " group by"
							+ " mbm.billing_master_id ")
							.setParameterList("billingMasterIds", billingMasterIds)
							.setParameter("unitId", reqDto.getUnitId())
							.setResultTransformer(Transformers.aliasToBean(BillListResDto.class)).list();
					
					if(adminDuaAmount != null && adminDuaAmount.size()>0){
						
						for (BillListResDto dto : listBills) {
							
							BillListResDto billObj = adminDuaAmount.stream()
									  .filter(order -> dto.getBillingMasterId().equals(order.getBillingMasterId()))
									  .findAny()
									  .orElse(null);
							
							if(billObj!=null){
								
								dto.setIntermediateSelfDue(billObj.getIntermediateSelfDue());
								
								
							}
							
						}
						
						
					}
					
				}
				
			}
			
			String queryCount = "select bill.billing_master_id as \"billingMasterId\" "
					+ "from  "
					+ "	billing.m_billing_master bill  "
					+ "left join billing.m_billing_master billagainst on  "
					+ "	bill.dr_note_against_billid = billagainst.billing_master_id  "
					+ "inner join patient.t_patient_registration pat on  " + "	pat.patient_id = bill.patient_id  "
					+ "	left join m_prefix_master pre on " + "	pre.prefix_id = pat.prefix_id "
					+ "left join PUBLIC.m_employee_master empmst on  " + "	bill.created_by = empmst.user_id  "
					+ "left join doctor.m_doctor_master docmst on " + "bill.created_by = docmst.user_id "
					+ "left join opd.t_encounter_master enc on " + "enc.encounter_id = bill.encounter_id  "
					+ " left join t_unit_speciality_mapper spl on " + " 	spl.speciality_id = enc.dept_id "
					+ " 	and spl.unit_id =enc.unit_id "
					 + "where  " 
					+ "	bill.bill_cancelled = 'N'  "
					+ " and bill.organisation_id =:orgId "
					+ " and bill.unit_id =:unitId and bill.bill_status_id IN (1,2,3)";


			StringBuffer sb1 = new StringBuffer(queryCount);

			sb1.append(" and to_char(bill.bill_date_time,'YYYY-MM-DD') between :fromDate and :toDate ");

			if(reqDto.getVisitTypeId() != null)
				sb1.append(" and bill.visit_type_id =:visitTypeId ");

			if(reqDto.getBillNo() != null && !reqDto.getBillNo().isEmpty())
				sb1.append(" and trim(lower(bill.bill_number))like :billNo ");

			if(reqDto.getVisitNo() != null && !reqDto.getVisitNo().isEmpty())
				sb1.append(" and trim(lower(bill.\"visitNo\")) like :visitNo ");

			if(reqDto.getBillStatusId() != null)
				sb1.append(" and bill.bill_status_id =:billStatusId ");

			if(reqDto.getPatientId() != null)
				sb1.append(" and bill.patient_id =:patientId ");
			
			if (reqDto.getSearchKeyword() != null && !reqDto.getSearchKeyword().isEmpty()) {
			    sb1.append(" AND ( "
			            + " lower(bill.bill_number) like :searchKeyword "
			            + " OR lower(pat.uhid_number) like :searchKeyword "
			            + " OR lower(concat(coalesce(pre.prefix_code,''),' ',pat.first_name, ' ', pat.middle_name, ' ', pat.last_name)) like :searchKeyword "
			            + " OR lower(enc.encounter_number) like :searchKeyword "
			            + " ) ");
			}
			
			sb1.append(" order by bill.bill_date_time desc");

			Integer count = 0;
			Query createdQuery1 = sessionFactory.getCurrentSession().createSQLQuery(sb1.toString());

			if(reqDto.getVisitTypeId() != null)
				createdQuery1.setParameter("visitTypeId", reqDto.getVisitTypeId());

			if(reqDto.getBillNo() != null && !reqDto.getBillNo().isEmpty())
				createdQuery1.setParameter("billNo", "%" + reqDto.getBillNo().toLowerCase().trim() + "%");

			if(reqDto.getVisitNo() != null && !reqDto.getVisitNo().isEmpty())
				createdQuery1.setParameter("visitNo", "%" + reqDto.getVisitNo().toLowerCase().trim() + "%");

			if(reqDto.getBillStatusId() != null)
				createdQuery1.setParameter("billStatusId", reqDto.getBillStatusId());

			if(reqDto.getPatientId() != null)
				createdQuery1.setParameter("patientId", reqDto.getPatientId());

			if (reqDto.getSearchKeyword() != null && !reqDto.getSearchKeyword().isEmpty())
				createdQuery1.setParameter("searchKeyword", "%" + reqDto.getSearchKeyword().toLowerCase() + "%");

			List<BillListResDto> listcount = createdQuery1
					.setParameter("orgId", reqDto.getOrgId())
					.setParameter("unitId", reqDto.getUnitId())
					.setParameter("fromDate", GlobalCommonDateUtils.getStringDate(fromDate, "yyyy-MM-dd"))
					.setParameter("toDate", GlobalCommonDateUtils.getStringDate(toDate, "yyyy-MM-dd"))
					.setResultTransformer(Transformers.aliasToBean(BillListResDto.class)).list();

			if (listcount != null && listcount.size() > 0) {
				count = listcount.size();
			}

			return new Response<>(SUCCESS, SUCCESS_CODE, null, listBills, count);
		} catch (Exception e) {
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	public Response getBillsByFilterForCancelledBillList(BillListReqDto reqDto) throws BillingException {
		try {
			boolean dateFlag = false;
			Date fromDate = reqDto.getFromDate() != null && !reqDto.getFromDate().isEmpty()
					? GlobalCommonDateUtils.getDate(reqDto.getFromDate(), "dd-MM-yyyy")
					: GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(reqDto.getUnitId()), "dd-MM-yyyy");
			Date toDate = reqDto.getToDate() != null && !reqDto.getToDate().isEmpty()
					? GlobalCommonDateUtils.getDate(reqDto.getToDate(), "dd-MM-yyyy")
					: GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(reqDto.getUnitId()), "dd-MM-yyyy");
			StringBuilder query = new StringBuilder(sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BILL_DETAILS_FOR_BILL_LIST_BY_FILTER_FOR_CANCELLED_BILL").getQueryString());
			query.append(" and bill.is_dr_note = 'N'");
			
			if(reqDto.getCancelledBillShow().equals('N')) {
				query.append(" and (bill.is_bill_reopened != 'Y' or bill.is_bill_reopened is null)  ");
			}else {
				query.append(" and bill.is_bill_reopened = 'Y' ");
			}
			
			dateFlag = (fromDate != null && toDate != null ? true : false);
			query.append(" and to_char(bill.bill_date_time,'YYYY-MM-DD') between :fromDate and :toDate ");

			if(reqDto.getVisitTypeId() != null)
				query.append(" and bill.visit_type_id =:visitTypeId ");

			if(reqDto.getBillNo() != null && !reqDto.getBillNo().isEmpty())
				query.append(" and trim(lower(bill.bill_number))like :billNo ");

			if(reqDto.getVisitNo() != null && !reqDto.getVisitNo().isEmpty())
				query.append(" and (trim(lower(enc.encounter_number)) like :visitNo or trim(lower(adm.admission_number)) like :visitNo )");

			// query.append(reqDto.getBillStatusId() != null ? " and bill.bill_status_id ="
			// + reqDto.getBillStatusId() : " ");
			if(reqDto.getPatientId() != null)
				query.append(" and bill.patient_id =:patientId ");

			query.append(" order by bill.bill_date_time desc ");
			List<BillListResDto> listBills = null;
			 Query sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
					 .setParameter("orgId", reqDto.getOrgId())
					 .setParameter("fromDate", GlobalCommonDateUtils.getStringDate(fromDate, "yyyy-MM-dd"))
					 .setParameter("toDate", GlobalCommonDateUtils.getStringDate(toDate, "yyyy-MM-dd"))
					 .setParameter("unitId", reqDto.getUnitId());

			if(reqDto.getVisitTypeId() != null)
				sqlQuery.setParameter("visitTypeId", reqDto.getVisitTypeId());

			if(reqDto.getBillNo() != null && !reqDto.getBillNo().isEmpty())
				sqlQuery.setParameter("billNo", "%" + reqDto.getBillNo().toLowerCase().trim() + "%");

			if(reqDto.getVisitNo() != null && !reqDto.getVisitNo().isEmpty())
				sqlQuery.setParameter("visitNo", "%" + reqDto.getVisitNo().toLowerCase().trim() + "%");

			if(reqDto.getPatientId() != null)
				sqlQuery.setParameter("patientId", reqDto.getPatientId());


			listBills = sqlQuery
					.setFirstResult(reqDto.getOffset() != null ? reqDto.getOffset() : 0)
					.setMaxResults(reqDto.getNoOfRecordsPerPage() != null ? reqDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(BillListResDto.class)).list();

			Integer count = 0;
			Query sqlQuery1 = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
					.setParameter("orgId", reqDto.getOrgId())
					.setParameter("fromDate", GlobalCommonDateUtils.getStringDate(fromDate, "yyyy-MM-dd"))
					.setParameter("toDate", GlobalCommonDateUtils.getStringDate(toDate, "yyyy-MM-dd"))
					.setParameter("unitId", reqDto.getUnitId());

			if(reqDto.getVisitTypeId() != null)
				sqlQuery1.setParameter("visitTypeId", reqDto.getVisitTypeId());

			if(reqDto.getBillNo() != null && !reqDto.getBillNo().isEmpty())
				sqlQuery1.setParameter("billNo", "%" + reqDto.getBillNo().toLowerCase().trim() + "%");

			if(reqDto.getVisitNo() != null && !reqDto.getVisitNo().isEmpty())
				sqlQuery1.setParameter("visitNo", "%" + reqDto.getVisitNo().toLowerCase().trim() + "%");

			if(reqDto.getPatientId() != null)
				sqlQuery1.setParameter("patientId", reqDto.getPatientId());

			List<BillListResDto> listcount = sqlQuery1
					.setResultTransformer(Transformers.aliasToBean(BillListResDto.class)).list();

			if (listcount != null && listcount.size() > 0) {
				count = listcount.size();
			}

			return new Response<>(SUCCESS, SUCCESS_CODE, null, listBills, count);
		} catch (Exception e) {
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	public Response updateIsBillReopened(Integer billId) throws ApplicationException {
		try {
			sessionFactory.getCurrentSession().createSQLQuery(
					"update billing.m_billing_master set is_bill_reopened = 'Y' where billing_master_id =:billId ")
					.setParameter("billId", billId)
					.executeUpdate();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	public Response checkBillSettledForCancelDischarge(DischargeDto dischargeDto) {
		try {
			Integer billStatusId = (Integer) sessionFactory.getCurrentSession().createSQLQuery(
					"select coalesce(bm.bill_status_id,0) from billing.m_billing_master bm where bm.is_interim_or_final_bill = 2 and bm.admission_id = :admissionId and bm.bill_cancelled = 'N' order by bm.billing_master_id limit 1 ")
					.setParameter("admissionId", dischargeDto.getAdmissionId()).uniqueResult();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, billStatusId);
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	public Response printLog(int type, int userId, int billId) {
		try {

			if (type == 0) {
				sessionFactory.getCurrentSession()
						.createSQLQuery("update billing.m_billing_master set is_print = 'Y', print_given_by =:userId, "
								+" print_date = :printDate "
								+" where billing_master_id =:billId")
						.setParameter("printDate", TimeZoneComponent.getDateByzone(0), StandardBasicTypes.TIMESTAMP)
						.setParameter("userId", userId)
						.setParameter("billId", billId)
						.executeUpdate();
				return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
			}
			if (type == 1) {
				sessionFactory.getCurrentSession()
						.createSQLQuery("update billing.m_billing_master set is_duplicate_print = 'Y', "
								+" duplicate_print_given_by =:userId , duplicate_print_date =:duplicatePrintDate "
								+" where billing_master_id =:billId ")
						.setParameter("duplicatePrintDate", TimeZoneComponent.getDateByzone(0), StandardBasicTypes.TIMESTAMP)
						.setParameter("userId", userId)
						.setParameter("billId", billId)
						.executeUpdate();
				return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
			}

		} catch (Exception e) {
			
			throw e;
		}
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	public Response receiptPrintLog(int type, int userId, int recieptId) {
		try {

			if (type == 0) {
				sessionFactory.getCurrentSession()
						.createSQLQuery("update billing.t_payment_reciept_master set is_print = 'Y', "
								+" print_given_by =:userId , print_date =:printDate "
								+" where payment_reciept_id =:receiptId ")
						.setParameter("printDate", TimeZoneComponent.getDateByzone(0), StandardBasicTypes.TIMESTAMP)
						.setParameter("userId", userId)
						.setParameter("receiptId", recieptId)
						.executeUpdate();
				return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
			}
			if (type == 1) {
				sessionFactory.getCurrentSession()
						.createSQLQuery("update billing.t_payment_reciept_master set is_duplicate_print = 'Y', "
								+ " duplicate_print_given_by =:userId , duplicate_print_date =:duplicatePrintDate "
								+ " where payment_reciept_id =:receiptId ")
						.setParameter("duplicatePrintDate", TimeZoneComponent.getDateByzone(0), StandardBasicTypes.TIMESTAMP)
						.setParameter("userId", userId)
						.setParameter("receiptId", recieptId)
						.executeUpdate();
				return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
			}

		} catch (Exception e) {
			
			throw e;
		}
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	public Response updatelabSampleDetailsStatusId(Integer billingMasterId) throws ApplicationException {
		try {

			sessionFactory.getCurrentSession()
					.createSQLQuery("update lab.t_lab_sample_dtls " + " set " + " sample_status_id = 2 "
							+ " where  sample_status_id = 1 and  " + " order_details_id in ( " + " select "
							+ " order_details_id " + " from " + " billing.t_billing_details " + " where "
							+ " billing_master_id =:billingMasterId )")
					.setParameter("billingMasterId", billingMasterId)
					.executeUpdate();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);

		} catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	public Response getSmartCardBalance(SmartcardClaimDto smartcardClaimDto) throws ApplicationException, SQLException {
		try {

			SmartCardMasterDto emp1 = new SmartCardMasterDto();
			FileOutputStream output = null;
			try {

				Session session = sessionFactory.getCurrentSession();
				SQLQuery query = session.createSQLQuery(
						"select mysql_smart_jdbc from public.t_unit_application_billing_configuration where organization_id =:organizationId "
								+ " AND unit_id =:unitId "
								+ " AND status='A' limit 1");

				query.setParameter("organizationId", smartcardClaimDto.getOrganizationId())
					 .setParameter("unitId", smartcardClaimDto.getUnitId() != null ? smartcardClaimDto.getUnitId() : smartcardClaimDto.getOrganizationId());

				String mysqlSmartJdbc = query.uniqueResult().toString();

				String[] connStr = mysqlSmartJdbc.split(",");
				String firststr = connStr[0];
				String secondStr = connStr[1];
				String thirdStr = connStr[2];
				Class.forName("com.mysql.jdbc.Driver");
				Connection conn = DriverManager.getConnection("jdbc:mysql://" + firststr.trim(), secondStr.trim(),
						thirdStr.trim());

				PreparedStatement statement = conn.prepareStatement(
						"select ID,Global_ID,Member_Nr,Progress_Flag ,Exchange_Type,Admit_ID,Rejection_Reason,InOut_Type,Location_ID,Smart_Date,Exchange_Date,Result_Date,Smart_File from exchange_files "
								+ " where Member_Nr =? "
								+ " AND Progress_Flag = 1 order by  ID Desc limit 1");

				statement.setString(1, smartcardClaimDto.getUhidNumber());

				ResultSet rs = statement.executeQuery();

				SmartCardMasterDto emp = new SmartCardMasterDto();
				while (rs.next()) {
					emp.setId(rs.getInt("ID"));
					emp.setGlobalId(rs.getString("Global_ID"));
					emp.setMemberNr(rs.getString("Member_Nr"));
					emp.setProgressFlag(rs.getInt("Progress_Flag"));
					emp.setAdmitID(rs.getInt("Exchange_Type"));
					emp.setExchangeType(rs.getInt("Admit_ID"));
					emp.setRejectionReason(rs.getString("Rejection_Reason"));
					emp.setInOutType(rs.getInt("InOut_Type"));
					emp.setLocationID(rs.getString("Location_ID"));
					emp.setSmartFileString(rs.getString("Smart_File"));

				}

				if (emp.getId() == null) {
					return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
				} else {
					DateTimeFormatter dtfDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
					DateTimeFormatter dtfTime = DateTimeFormatter.ofPattern("HH:mm:ss");
					LocalDateTime currentDate = LocalDateTime.now();
					LocalTime currentTime = LocalTime.now();
					String curDate = dtfDate.format(currentDate);
					String curTime = dtfTime.format(currentTime);

					String claimString = getXMLClaimFormatForGetBalance(smartcardClaimDto, emp);

					conn.close();
					return new Response<>(SUCCESS, SUCCESS_CODE, null, null, claimString);
				}

				/* } */

				// conn.close();
				// return new Response<>(SUCCESS, SUCCESS_CODE, null, null, emp1.getAmount());
			} catch (SQLException e) {
				logger.error("Got an exception! ");
				logger.error(e.getMessage());
			} /*
				 * finally{ FileOutputStream writer = new FileOutputStream("./claim.xml");
				 * writer.write(("").getBytes()); writer.close(); }
				 */

		} catch (Exception e) {
			
			return new Response(ERROR, ERR_500, "Specialty Not Mapped With SmartCard Device", null, null);

		}
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	public String getXMLClaimFormatForGetBalance(SmartcardClaimDto smartcardClaimDto, SmartCardMasterDto emp)
			throws JSONException, ParseException {

		String s1 = smartcardClaimDto.getBillDate();
		String[] billDtTime = s1.split("\\s");

		String billDate = GlobalCommonDateUtils.formatDateForClaim(billDtTime[0], "dd-MM-yyyy",
				sessionService.getStartDateFormat());

		DateTimeFormatter dtfDate = DateTimeFormatter.ofPattern(sessionService.getStartDateFormat());
		// System.out.println(sessionService.getStartDateFormat());
		DateTimeFormatter dtfTime = DateTimeFormatter.ofPattern("HH:mm:ss");
		LocalDateTime currentDate = LocalDateTime.now();
		LocalTime currentTime = LocalTime.now();
		String curDate = dtfDate.format(currentDate);
		String curTime = dtfTime.format(currentTime);

		String newSmartFile = emp.getSmartFileString().replace("\"", "'");
		newSmartFile = newSmartFile.replace("16F", "@");
		newSmartFile = newSmartFile.replace("1E", "*");

		JSONObject soapDatainJsonObject = XML.toJSONObject(newSmartFile);
		// //System.out.println(soapDatainJsonObject.get("AdmissionInformation"));
		String cardSerialnumber = soapDatainJsonObject.getJSONObject("AdmissionInformation").getJSONObject("A1")
				.get("card_serialnumber").toString();
		cardSerialnumber = cardSerialnumber.replace("@", "16F");
		cardSerialnumber = cardSerialnumber.replace("*", "1E");
		String Amount = soapDatainJsonObject.getJSONObject("AdmissionInformation").getJSONObject("Benefits")
				.getJSONObject("Benefit").get("Amount").toString();

		// System.out.println("Claim Data" + claimString);
		return Amount;
	}

	@Override
	public void insertBillDataJson(Integer billingMasterId) throws SQLException {

		boolean flag = (boolean) sessionFactory.getCurrentSession().createSQLQuery("select insertBillDataJson(:billId)")
				.setParameter("billId", billingMasterId).uniqueResult();
		if (flag == true)
			throw new SQLException();
	}

	@Override
	public Response getBillReferenceData(BillListReqDto reqDto) throws ApplicationException, SQLException {
		try {
			StringBuilder query = new StringBuilder(" select "
					/*
					 * + "case " +
					 * "when bill.is_interim_or_final_bill = 2 then ( cast(coalesce((select sum(due_amount) from billing.t_bill_payee_details where payee_id = tuabc.self_payee_id and bill_id in ( select billing_master_id from billing.m_billing_master where status = 'A' and admission_id = bill.admission_id and is_interim_or_final_bill = 1)), 0) as double precision)) "
					 * + "end as \"intermediateSelfDue\", "
					 */
					+ " cast(coalesce(temp.intermediateSelfDue,0)as double precision)  as \"intermediateSelfDue\", "
					+ "coalesce( case when bill.visit_type_id = 1 then ( select referral_id from t_referal_details where encounter_id = bill.encounter_id and referral_type_id = 1 ) end, case when bill.visit_type_id != 1 then ( select referral_id from t_referal_details where admission_id = bill.admission_id and referral_type_id = 1 ) end ) as \"referalDocId\", "
					+ "coalesce( case when bill.visit_type_id = 1 then enc.encounter_id when bill.visit_type_id != 1 then adm.admission_id end ) as \"visitId\", "
					+ "coalesce( case when bill.visit_type_id = 1 then (select enc.appointment_id from opd.t_encounter_master enc where enc.encounter_id = bill.encounter_id and enc.status = 'A') end ) as \"appointmentId\", "
					+ "coalesce( case when bill.visit_type_id = 1 then (select mam.is_paid_appointment from opd.m_appointment_master mam where mam.appointment_id = (select enc.appointment_id from opd.t_encounter_master enc where enc.encounter_id = bill.encounter_id and enc.status = 'A')) end ) as \"isPaidAppointment\", "
					+ "		(case when  bill.visit_type_id = 1  then enc.is_financial_clearance  "
					+ "			else adm.is_financial_creance  " + "		end ) as \"isFinancialClearance\"  "
					+ "from " + "billing.m_billing_master bill "
					+ "inner join t_unit_application_billing_configuration tuabc on " + "tuabc.unit_id = bill.unit_id "
					+ "left join opd.t_encounter_master enc on " + "enc.encounter_id = bill.encounter_id "
					+ "left join adt.t_admission adm on " + "adm.admission_id = bill.admission_id " + " left join ("
					+ "			select sum(due_amount) as intermediateSelfDue ,tbpd.payee_id,mbm.is_interim_or_final_bill,mbm.admission_id,mbm.billing_master_id  "
					+ " 		from billing.t_bill_payee_details tbpd "
					+ " 		inner join t_unit_application_billing_configuration tubc on tubc.self_payee_id = tbpd.payee_id "
					+ " 		inner join billing.m_billing_master mbm on mbm.billing_master_id = tbpd.bill_id AND mbm.is_interim_or_final_bill = 1 and mbm.status='A' "
					+ " 		group by "
					+ " 		tbpd.payee_id,tbpd.bill_id ,mbm.is_interim_or_final_bill ,	mbm.billing_master_id ,"
					+ " 		mbm.admission_id" + ") temp on temp.billing_master_id = bill.billing_master_id "
					+ " and temp.admission_id = bill.admission_id  and temp.is_interim_or_final_bill = bill.is_interim_or_final_bill"
					+ " AND  bill.is_interim_or_final_bill = 2  " + "where bill.billing_master_id=:billId ");

			List<BillListResDto> listBills = null;
			listBills = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
					.setParameter("billId", reqDto.getBillingMasterId())
					.setResultTransformer(Transformers.aliasToBean(BillListResDto.class)).list();

			return new Response<>(SUCCESS, SUCCESS_CODE, null, listBills, null);
		} catch (Exception e) {
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	public BillListResDto getBillSumsEncounterId(Integer encounterId) throws ApplicationException, SQLException {
		try {

			String query = "select sum(net_amount) as \"netAmount\", "
					+ "sum(total_self_payable) as \"totalSelfPayable\", "
					+ "sum(total_credit_payable) as \"totalCreditPayable\", " + "sum(outstanding) as \"outstanding\" "
					+ "from billing.m_billing_master where encounter_id =:encounterId ";

			BillListResDto configInfoDto = (BillListResDto) sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("encounterId", encounterId)
					.setResultTransformer(Transformers.aliasToBean(BillListResDto.class)).uniqueResult();
			return configInfoDto;
		} catch (Exception e) {
			
			throw e;
		}
	//	return null;
	}

	@Override
	public boolean matchBillAmounts(BillingMasterDto billingMasterDto, Set<Integer> orderDetailsIdList) {
		try {

			/*
			 * String query = " select " + " 	sum(tod.net_amount) as \"net\", " +
			 * " 	sum(tod.self_payable) as \"self\", " +
			 * " 	sum(tod.credit_payable) as \"credit\" " + " from " +
			 * " 	t_order_details tod " + " inner join t_order_master tom on " +
			 * " 	tom.order_id = tod.order_id " + " where " +
			 * " 	tod.service_is_billed = 0 " + " 	and tod.status = 'A' " +
			 * " 	and tod.service_id is not null " +
			 * " 	and ((tom.encounter_id =:encounterId and tom.visit_type_id =:visitTypeId) "
			 * +
			 * " 	or (tom.admission_id =:encounterId and tom.visit_type_id =:visitTypeId)) "
			 * ;
			 * 
			 * List<Object[]> list = sessionFactory.getCurrentSession()
			 * .createSQLQuery(query) .setInteger("encounterId",
			 * billingMasterDto.getEncounterId()!=null ? billingMasterDto.getEncounterId() :
			 * billingMasterDto.getAdmissionId()) .setInteger("visitTypeId",
			 * billingMasterDto.getVisitTypeId()) .list();
			 * 
			 * if (list != null && !list.isEmpty()) {
			 * System.out.println(((((BigDecimal)(list.get(0)[0]))).intValue())+"--"+(new
			 * Double(billingMasterDto.getNetAmount()+billingMasterDto.getRoundOff()).
			 * intValue())+"--" + (((BigDecimal)(list.get(0)[0])).intValue()==new
			 * Double(billingMasterDto.getNetAmount()+billingMasterDto.getRoundOff()).
			 * intValue()));
			 * System.out.println(((((BigDecimal)(list.get(0)[1]))).intValue())+"--"+((
			 * billingMasterDto.getSelfPayable()).intValue())+"--" +
			 * (((BigDecimal)(list.get(0)[1])).intValue()==(billingMasterDto.getSelfPayable(
			 * ).intValue())));
			 * System.out.println(((((BigDecimal)(list.get(0)[2]))).intValue())+"--"+((
			 * billingMasterDto.getCreditPayable()).intValue())+"--" +
			 * (((BigDecimal)(list.get(0)[2])).intValue()==(billingMasterDto.
			 * getCreditPayable().intValue())));
			 * if(((BigDecimal)(list.get(0)[0])).intValue()==(new
			 * Double(billingMasterDto.getNetAmount()+billingMasterDto.getRoundOff()).
			 * intValue()) &&
			 * ((BigDecimal)(list.get(0)[1])).intValue()==(billingMasterDto.getSelfPayable()
			 * .intValue()) &&
			 * ((BigDecimal)(list.get(0)[2])).intValue()==(billingMasterDto.getCreditPayable
			 * ().intValue())) return true; }
			 */

			try {
				
				BigInteger count = (BigInteger) sessionFactory.getCurrentSession()
						.createSQLQuery(" select " 
								 + " 	count(tod.order_details_id) " 
								 + " from " 
								 + " 	t_order_details tod " 
								 + " inner join t_order_master tom on " 
								 + " 	tom.order_id = tod.order_id " 
								 + " inner join service.t_unit_service_master_details tusmd on " 
								 + " 	tusmd.service_master_id = tod.service_id " 
								 + " where " 
								 + " 	tod.service_is_billed = 0 " 
								 + " 	and tod.status = 'A' " 
								 + " 	and tod.service_id is not null " 
								 + " 	and ((tom.encounter_id =:encounterId " 
								 + " 		and tom.visit_type_id =:visitTypeId) " 
								 + " 	or (tom.admission_id =:encounterId " 
								 + " 		and tom.visit_type_id =:visitTypeId)) " 
								 + " 	and tusmd.unit_id = tod.org_unit_id " 
								 + " 	and tusmd.status = 'A' "
								//if final bill is generated without access for billManipulation
								 + (!billingMasterDto.getIsBillManipulationAllowed() ? " and tod.is_dummy_service = false " : "")) 
						.setParameter("encounterId",
								billingMasterDto.getEncounterId() != null ? billingMasterDto.getEncounterId()
										: billingMasterDto.getAdmissionId())
						.setParameter("visitTypeId", billingMasterDto.getVisitTypeId()).uniqueResult();

			/**	if ((count.doubleValue()) == (orderDetailsIdList.size()))
					return true;
				else
					return false;*/
			return  ((count.doubleValue()) == (orderDetailsIdList.size()));
			} catch (HibernateException e) {
				
			}
			return false;

		} catch (Exception e) {
			
		}
		return false;

	}

	@Override

	public Response checkDischargeOrNot(DischargeDto dischargeDto) throws ApplicationException, SQLException {
		try {
			DischargeDto dischargeDtosList = (DischargeDto) sessionFactory.getCurrentSession()
					.getNamedQuery("CHECK_PATIENT_DISCHARGE_OR_NOT")
					.setParameter("admissionId", dischargeDto.getAdmissionId())
					.setParameter("organizationId", dischargeDto.getOrganizationId())
					.setParameter("unitId", dischargeDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(DischargeDto.class)).uniqueResult();
			return new Response(SUCCESS, null, null, null, dischargeDtosList.getDischCount());

		} catch (HibernateException he) {
			throw he;

		} catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, null, null, null, null);
	}

	public Integer getdiscountTypeIdBySaleId(Integer serviceMasterDto) {
		if (serviceMasterDto != null) {
			String sql = " select discount_type_id from pharmacy.t_sale where id =:id";

			Query query = getSession().createSQLQuery(sql);
			query.setParameter("id", serviceMasterDto);
//        query.setParameterList("statusId", statusIds);
			return (Integer) query.uniqueResult();
		} else {
			return Integer.valueOf(0);
		}

	}

	@Override
	public void updateSaleTableBillstatus(BillingMasterDto billUpdateDto) {

		List<BillingMasterDto> list = sessionFactory.getCurrentSession().createSQLQuery(" select"
				+ " sal.outstanding_amount as \"outstanding\" ,sal.id as \"saleid\" from  pharmacy.t_sale sal where sal.id in ( select tom.sale_id from public.t_order_master tom "
				+ " inner join t_order_details tod on tod.order_id = tom.order_id "
				+ " inner join billing.t_billing_details mvm on mvm.order_details_id = tod.order_details_id "
				+ " inner join billing.m_billing_master mbm on mbm.billing_master_id =mvm.billing_master_id "
				+ " where tom.sale_id is not null and mbm.billing_master_id =:billingMasterId )")
				.setParameter("billingMasterId", billUpdateDto.getBillingMasterId())
				.setResultTransformer(Transformers.aliasToBean(BillingMasterDto.class)).list();

		if (list != null && list.size() > 0) {

			for (BillingMasterDto BillingMasterDto : list) {

				String sql = " update pharmacy.t_sale set status_id = 4,outstanding_amount=0 , paid_amount =:outstanding "
						   + " where id in(:saleId)";

				sessionFactory.getCurrentSession().createSQLQuery(sql)
						.setParameter("outstanding", BillingMasterDto.getOutstanding())
						.setParameter("saleId", BillingMasterDto.getSaleid())
						.executeUpdate();

			}
		}

	}

	@Override
	public void saveBillStatusId(Integer object) {
		String sql = " update pharmacy.t_sale set status_id = 4 where id in(select tom.sale_id from public.t_order_master tom "
				+ " inner join t_order_details tod on tod.order_id = tom.order_id "
				+ " inner join billing.t_billing_details mvm on mvm.order_details_id = tod.order_details_id "
				+ " inner join billing.m_billing_master mbm on mbm.billing_master_id =mvm.billing_master_id "
				+ " where tom.sale_id is not null and mbm.billing_master_id =:billingMasterId )";

		sessionFactory.getCurrentSession().createSQLQuery(sql)
				.setParameter("billingMasterId", object)
				.executeUpdate();

	}


	@Override
	public Integer getcheckScreenIdAvailable(Integer tt, Integer tt2) {try {

		Integer screenId = 0;
		BillingMasterDto dto = (BillingMasterDto) sessionFactory.getCurrentSession()
				.createSQLQuery("select pc.screen_id as \"screenId\" " + 
						" from public.m_prefix_configuration pc " + 
						" where pc.screen_id =:screenId" + 
						" and pc.unit_id =:unitId ")
				.setParameter("screenId", tt2)
				.setParameter("unitId", tt)
				.setResultTransformer(Transformers.aliasToBean(BillingMasterDto.class)).uniqueResult();

		screenId = (dto != null && dto.getScreenId() > 0) ? dto.getScreenId() : 0;
		return screenId;
	} catch (Exception e) {
		
		throw e;
	}}

	@Override
	public Response getAllInclusivePackageConsumeAmount(BillingMasterDto dto1)
			throws ApplicationException {
		try {

			OrderDetailsPackageMapperDto dto = new OrderDetailsPackageMapperDto();
			
			if(dto1.getAdmissionId() != null) {
				
				 dto = (OrderDetailsPackageMapperDto) sessionFactory.getCurrentSession()
						 .createSQLQuery("select todpm.package_id as \"id\" ," + 
									" sum(todpm.package_amount) as \"packageAmount\" " + 
									" from " + 
									" t_order_details_package_mapper todpm " + 
									" inner join t_order_details tod on " + 
									" todpm.order_details_id = tod.order_details_id " + 
									" inner join t_order_master tom on " + 
									" tom.order_id = tod.order_id " + 
									" where " + 
									" tom.admission_id =:admissionId " +
									" and tod.status ='A' " + 
									" and tod.service_id =:pharmacyServiceId " +
									" and todpm.package_id =:packageId " +
									" group by " + 
									" todpm.package_id")
						 .setParameter("admissionId", dto1.getAdmissionId())
						 .setParameter("pharmacyServiceId", dto1.getPharmacyServiceId())
						 .setParameter("packageId", dto1.getPackageId())
						 .setResultTransformer(Transformers.aliasToBean(OrderDetailsPackageMapperDto.class)).uniqueResult();
			}else {
				
				dto = (OrderDetailsPackageMapperDto) sessionFactory.getCurrentSession()
						.createSQLQuery("select todpm.package_id as \"id\" ," + 
								" sum(todpm.package_amount) as \"packageAmount\" " + 
								" from " + 
								" t_order_details_package_mapper todpm " + 
								" inner join t_order_details tod on " + 
								" todpm.order_details_id = tod.order_details_id " + 
								" inner join t_order_master tom on " + 
								" tom.order_id = tod.order_id " + 
								" where " + 
								" tom.encounter_id =:encounterId " +
								" and tod.status ='A' " + 
								" and tod.service_id =:pharmacyServiceId " +
								" and todpm.package_id =:packageId " +
								" group by " + 
								"todpm.package_id")
						.setParameter("encounterId", dto1.getEncounterId())
						.setParameter("pharmacyServiceId", dto1.getPharmacyServiceId())
						.setParameter("packageId", dto1.getPackageId())
						.setResultTransformer(Transformers.aliasToBean(OrderDetailsPackageMapperDto.class)).uniqueResult();
			}

			return new Response(SUCCESS, null, null, null, dto);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public void updatePharmacy(OrderDetailsMasterDto detailsByEncounterIdDto,
			Integer pharmacyServiceOrderDetailsIds,Integer paymentEntitlementId,Integer payeeId) {
		if(payeeId==null)
		{
			payeeId=detailsByEncounterIdDto.getPayeeId();
		}
		if(payeeId!=null && paymentEntitlementId!=null && paymentEntitlementId==1 && pharmacyServiceOrderDetailsIds!= null)
		{
			Double d = detailsByEncounterIdDto.getSelfPayable().doubleValue();
			
			if(detailsByEncounterIdDto.getSelfPayable() != null && d>0){
				
				String sql2 =	"update pharmacy.t_pharmacy_bill_payee_details set bill_amount =:selfPayable "
						+" where payee_id =:payeeId "
						+" and  t_sale_id in (select coalesce(om.sale_id,0) "
						+" from public.t_order_details tod "
						+" inner join public.t_order_master om on om.order_id = tod.order_id where tod.order_details_id=:id )";
				
				Query query2 = sessionFactory.getCurrentSession().createSQLQuery(sql2);
				query2.setParameter("id", pharmacyServiceOrderDetailsIds)
					  .setParameter("selfPayable", detailsByEncounterIdDto.getSelfPayable())
					  .setParameter("payeeId", payeeId);
				query2.executeUpdate();
			}
			
			if(detailsByEncounterIdDto.getSelfPayable() != null && (d < 0 || d == 0)){
				
				String sql2 =	"update pharmacy.t_pharmacy_bill_payee_details set bill_amount =0 "
						+" where payee_id =:payeeId "
						+" and  t_sale_id in (select coalesce(om.sale_id,0) "
						+"from public.t_order_details tod "
						+"inner join public.t_order_master om on om.order_id = tod.order_id where tod.order_details_id=:id )";
				
				Query query2 = sessionFactory.getCurrentSession().createSQLQuery(sql2);
				query2.setParameter("id", pharmacyServiceOrderDetailsIds);
				query2.setParameter("payeeId", payeeId);
				query2.executeUpdate();
			}
			
			
            String sql3 =	"update pharmacy.t_pharmacy_bill_payee_details set bill_amount =:creditPayable "
					+" where not payee_id =:payeeId "
					+" and  t_sale_id in (select coalesce(om.sale_id,0) from public.t_order_details tod "
					+" inner join public.t_order_master om on om.order_id = tod.order_id where tod.order_details_id=:id )";
			
			Query query3 = sessionFactory.getCurrentSession().createSQLQuery(sql3);
			query3.setParameter("id", pharmacyServiceOrderDetailsIds)
					.setParameter("creditPayable", detailsByEncounterIdDto.getCreditPayable())
					.setParameter("payeeId", payeeId);
			query3.executeUpdate();
		}
		if(payeeId!=null && paymentEntitlementId!=null && paymentEntitlementId!=1  && pharmacyServiceOrderDetailsIds!= null)
		{
			String sql2 =	"update pharmacy.t_pharmacy_bill_payee_details set bill_amount =:creditPayable "
					+" where payee_id =:payeeId"
					+" and  t_sale_id in (select coalesce(om.sale_id,0) "
					+" from public.t_order_details tod "
					+" inner join public.t_order_master om on om.order_id = tod.order_id where tod.order_details_id  In (:id))";
			
			Query query2 = sessionFactory.getCurrentSession().createSQLQuery(sql2);
			query2.setParameter("id", pharmacyServiceOrderDetailsIds)
					.setParameter("creditPayable", detailsByEncounterIdDto.getCreditPayable())
					.setParameter("payeeId", payeeId);
			query2.executeUpdate();
			
            String sql3 =	"update pharmacy.t_pharmacy_bill_payee_details set bill_amount =:selfPayable "
					+" where not payee_id =:payeeId "
					+" and  t_sale_id in (select coalesce(om.sale_id,0) "
					+" from public.t_order_details tod "
					+" inner join public.t_order_master om on om.order_id = tod.order_id where tod.order_details_id  In (:id))";
			
			Query query3 = sessionFactory.getCurrentSession().createSQLQuery(sql3);
			query3.setParameter("id", pharmacyServiceOrderDetailsIds)
					.setParameter("selfPayable", detailsByEncounterIdDto.getSelfPayable())
					.setParameter("payeeId", payeeId);
			query3.executeUpdate();
		}
		}

	@Override
	public Response getFreeTextOrderServices(BillingMasterDto dto) throws ApplicationException {
		try {
			
			String qry = "select service_text as \"serviceText\" ,free_text_ordered_services_id as \"freeTextOrderedServicesId\" "
					+ "from t_free_text_ordered_services where status = 'A' and is_consumed  = false and order_details_id is null  ";
			
			StringBuffer sb = new StringBuffer(qry);

			if(dto.getAdmissionId() != null && dto.getAdmissionId()>0) {
				sb.append(" and admission_id =:admissionId ");
			}
			
			if(dto.getEncounterId()!= null && dto.getEncounterId()>0) {
				sb.append(" and encounter_id =:encounterId ");
			}
			
			
			Query sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(sb.toString());

			if(dto.getAdmissionId() != null && dto.getAdmissionId()>0) 
				sqlQuery.setParameter("admissionId", dto.getAdmissionId());

			if(dto.getEncounterId()!= null && dto.getEncounterId()>0) 
				sqlQuery.setParameter("encounterId", dto.getEncounterId());
			
			List<FreeTextOrderedServicesDto> list = sqlQuery.setResultTransformer(Transformers.aliasToBean(FreeTextOrderedServicesDto.class)).list();

			
			return new Response(SUCCESS, null, null, list, null);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public boolean checkOutstandingIsClearedForCompanyInvoice1(Integer billId) throws ApplicationException {
		try {
			boolean result = (boolean) sessionFactory.getCurrentSession()
					.getNamedQuery("CHECK_OUTSTANDING_IS_ZERO_FOR_SETTLEMENT_OF_BILL1")
					.setParameter("billId", billId)
					.uniqueResult();
			
			return result;
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public void updateReceivedFrom(Object object,List<PaymentReceiptDetailsDto> paymentCollectionDto) {
		if( paymentCollectionDto!=null && paymentCollectionDto.size()!=0 && object!=null) {
        sessionFactory.getCurrentSession()
                .createSQLQuery(" update billing.t_payment_reciept_master set received_from =:receivedFrom  where billing_master_id =:billingMasterId ")
				.setParameter("receivedFrom", paymentCollectionDto.getFirst().getReceivedFrom())
				.setParameter("billingMasterId", object)
				.executeUpdate();
		}
	}

	@Override
	public String getTallyInterfaceDataInJsonFormat(CabinMasterDto dto) throws ApplicationException {
		try {
			String qry = dto.getQuery();
			CabinMasterDto dto1 =(CabinMasterDto) sessionFactory.getCurrentSession()
					.createSQLQuery(qry)
					.setResultTransformer(Transformers.aliasToBean(CabinMasterDto.class)).uniqueResult();
			String result =dto1.getResult();
			
			return result;
		} catch (Exception e) {
			
		}
		return null;
	}

	@Override
	public String updateTallyInterfaceDataById(CabinMasterDto dto) throws ApplicationException {
		// TODO Auto-generated method stub
		String qry = dto.getQuery();
		sessionFactory.getCurrentSession()
		.createSQLQuery(qry)
        .executeUpdate();
		String message ="Updated succesfully";
		return message;
	}

	@Override
	public Response getDataForCheckBillStatusByBillNumber(BillingMasterDto dto) throws ApplicationException {
		try {
			
			if( dto.getBillingMasterId() != null ){
				String qry = "select   " + 
						"	bill.billing_master_id as \"billingMasterId\",   " + 
						"	bill.bill_status_id as \"billStatusId\",   " + 
						"	bill.bill_number as \"billNumber\",   " + 
						"	billPayee.payment_entitlement_id as \"paymentEntitlementId\"   " + 
						"from   " + 
						"	billing.m_billing_master bill   " + 
						"inner join t_unit_application_billing_configuration tuabc on   " + 
						"	tuabc.unit_id = bill.unit_id   " + 
						"left join billing.t_bill_payee_details billPayee on   " + 
						"	bill.billing_master_id = billPayee.bill_id   " + 
						"	and billPayee.priority_id =(   " + 
						"		select   " + 
						"			min( bpd.priority_id )   " + 
						"		from   " + 
						"			billing.t_bill_payee_details bpd   " + 
						"		where   " + 
						"			bill.billing_master_id = bpd.bill_id   " + 
						"	)   " + 
						"left join billing.t_bill_payee_details payee on   " + 
						"	bill.billing_master_id = payee.bill_id   " + 
						"	and payee.payee_id = tuabc.self_payee_id   " + 
						"inner join patient.t_patient_registration pat on   " + 
						"	pat.patient_id = bill.patient_id   " + 
						"left join m_prefix_master pre on   " + 
						"	pre.prefix_id = pat.prefix_id   " + 
						"inner join public.m_gender_master genmstr on   " + 
						"	genmstr.gender_id = pat.gender_id   " + 
						"left join PUBLIC.m_employee_master empmst on   " + 
						"	bill.created_by = empmst.user_id   " + 
						"left join doctor.m_doctor_master docmst on   " + 
						"	bill.created_by = docmst.user_id   " + 
						"left join opd.t_encounter_master enc on   " + 
						"	enc.encounter_id = bill.encounter_id   " + 
						"left join adt.t_admission adm on   " + 
						"	adm.admission_id = bill.admission_id   " + 
						"left join t_unit_speciality_mapper spl on   " + 
						"	spl.speciality_id = enc.dept_id   " + 
						"	and spl.unit_id = enc.unit_id   " + 
						"left join opd.m_appointment_master am on   " + 
						"	am.appointment_id = enc.appointment_id   " + 
						"left join opd.m_slot_master sm on   " + 
						"	am.slot_id = sm.slot_id   " + 
						"left join doctor.m_doctor_master mdm on   " + 
						"	mdm.doctor_id =(   " + 
						"		case   " + 
						"			when enc.doctor_id is not null then enc.doctor_id   " + 
						"			when enc.preferred_doc_id is not null then enc.preferred_doc_id   " + 
						"			else adm.doctor_id   " + 
						"		end   " + 
						"	)   " + 
						"left outer join doctor.m_speciality_master speciaityMaster on   " + 
						"	speciaityMaster.speciality_id =(   " + 
						"		case   " + 
						"			when enc.encounter_id is not null then enc.dept_id   " + 
						"			else adm.speciality_id   " + 
						"		end   " + 
						"	)   " + 
						"left join adt.t_insurance insurance on   " + 
						"	insurance.insurance_id = billPayee.associate_company_id   " + 
						"left join public.t_company_master tcm on   " + 
						"	tcm.company_id = billPayee.payee_id   " + 
						"where   " + 
						"	bill.bill_cancelled = 'N'   " + 
						"	and bill.is_dr_note = 'N'   " + 
						" 	and bill.organisation_id =:orgId  "+
						" 	and bill.unit_id =:unitId  "+
						"    and bill.billing_master_id =:billingMasterId";
				
				StringBuffer sb = new StringBuffer(qry);

				
				List<BillingMasterDto> list = sessionFactory.getCurrentSession()
						.createSQLQuery(sb.toString())
						.setParameter("orgId", dto.getOrgId())
						.setParameter("unitId", dto.getUnitId())
						.setParameter("billingMasterId", dto.getBillingMasterId())
						.setResultTransformer(Transformers.aliasToBean(BillingMasterDto.class)).list();

				
				return new Response(SUCCESS, null, null, list, null);
			}else{
				return new Response(SUCCESS, null, null, null, null);
			}			
			
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response updateRenderedDate(BillingMasterDto dto) {
		
		if(dto.getIsPackage()==true)
		{
			try
			{
				Timestamp packageDate = new Timestamp(dto.getPackageApplicableDate());
			StringBuilder query = new StringBuilder("update billing.t_package_consumption_master "
					+ "set package_applicable_date =:packageDate where order_detail_id =:orderDetailsId ");
			sessionFactory.getCurrentSession().createSQLQuery(query.toString())
					.setParameter("packageDate", packageDate)
					.setParameter("orderDetailsId", dto.getOrderDetailsId())
					.executeUpdate();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
			} catch (Exception e) {
				
				throw e;
		}
		}
		else
		{
			try
			{
				Timestamp orderDate = new Timestamp(dto.getOrdRenderDatetime());
			StringBuilder query = new StringBuilder("update public.t_order_details "
					+ "set ord_render_datetime =:orderDate where order_details_id =:orderDetailsId ");
			sessionFactory.getCurrentSession().createSQLQuery(query.toString())
					.setParameter("orderDate", orderDate)
					.setParameter("orderDetailsId", dto.getOrderDetailsId())
					.executeUpdate();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
			} catch (Exception e) {
				
				throw e;
			}
		}
	}

	@Override
	public String getTallyInterfaceDataInJsonFormatByMode(CabinMasterDto dto) throws ApplicationException {
		try {
			
			String querySql = "select * from t_tally_interface where to_char(voucher_date,''yyyy-mm-dd'')=':value'";

			Query query = sessionFactory.getCurrentSession().createSQLQuery(querySql)
					.setParameter("value", dto.getValue());

			String querySql1 ="select array_to_json(array_agg(row_to_json(t)))::text as SummeryData from (:query) t;";

			Query query1 =  sessionFactory.getCurrentSession().createSQLQuery(querySql1)
					.setParameter("query", query.toString());

			String qry ="select public.Get_json_format(:query1) as result ";

			Query query2 =  sessionFactory.getCurrentSession().createSQLQuery(qry)
					.setParameter("query1", query1.toString());

			CabinMasterDto dto1 =(CabinMasterDto) query2
					.setResultTransformer(Transformers.aliasToBean(CabinMasterDto.class)).uniqueResult();
			String result =dto1.getResult();
			
			return result;
		} catch (Exception e) {
			
		}
		return null;
	}

	@Override
	public String updateTallyInterfaceDataByIdByMode(CabinMasterDto dto) throws ApplicationException {
		// TODO Auto-generated method stub
		try {
		String qry = null;
		if(dto.getParameter().equals("Id") ) {
			 qry = "update t_tally_interface set is_synched =:value where voucher_no IN (:voucherNoList)";
			 sessionFactory.getCurrentSession().createSQLQuery(qry.toString())
						.setParameter("value", dto.getValue())
						.setParameterList("voucherNoList", dto.getIds())
		             .executeUpdate();
		}else {
			 qry = "update t_tally_interface set is_synched =:value where to_char(voucher_date,'yyyy-mm-dd') =:voucherDate ";
			 sessionFactory.getCurrentSession()
				.createSQLQuery(qry)
		        .setParameter("value", dto.getValue())
		        .setParameter("voucherDate", dto.getVoucherDate())
		        .executeUpdate();
		}
		String message ="Updated succesfully";
		return message;
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Integer checkAppoinmentStatussss(SlotMasterDto dtott) {
		try {
			String qry = "select am.appointment_status_id as \"appointmentStatusId\" from opd.m_appointment_master am where am.appointment_id =:appointmentId ";
			StringBuffer sb = new StringBuffer(qry);
			
			
			Integer appointmentStatusId = 0;
			SlotMasterDto dto = (SlotMasterDto) sessionFactory.getCurrentSession()
					.createSQLQuery(sb.toString())
					.setParameter("appointmentId", dtott.getAppointmentId())
					.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).uniqueResult();

			if(dto != null && dto.getAppointmentStatusId() != null) {
				appointmentStatusId = (dto != null && dto.getAppointmentStatusId() > APPOINTMENTSTATUSID0) ? dto.getAppointmentStatusId() : APPOINTMENTSTATUSID0;
			}
			
			return appointmentStatusId;
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response patientCouponTemplate(Integer patientCouponTemplateMapperId) throws ApplicationException {

		// TODO Auto-generated method stub
		try {
		String qry = null;
		
			 qry = "update billing.t_patient_coupon_template_mapper set order_details_id ="
			 		+ " (select order_details_id  from t_order_details tod  where patient_coupon_template_mapper_id =:patientCouponTemplateMapperId limit 1) , "
			 		+ "consume = true , consumption ='Y' where patient_coupon_template_mapper_id =:patientCouponTemplateMapperId ";
			 sessionFactory.getCurrentSession()
				.createSQLQuery(qry)
		        .setParameter("patientCouponTemplateMapperId", patientCouponTemplateMapperId)
		        .executeUpdate();
		
		return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		} catch (Exception e) {
			
			throw e;
		}
	
	}

	@Override
	public Response getDataForFinanacialRequest(RequestDiscountDto requestDiscountDto) throws ApplicationException {
		try {
			
			String qry = "select bill_id as \"billId\" from billing.t_request_discount where bill_id=:billId and req_status =1 and request_type = 2";
			
			StringBuffer sb = new StringBuffer(qry);

			
			List<RequestDiscountDto> list = sessionFactory.getCurrentSession()
					.createSQLQuery(sb.toString())
					.setParameter("billId", requestDiscountDto.getBillId())
					.setResultTransformer(Transformers.aliasToBean(RequestDiscountDto.class)).list();

			
			return new Response(SUCCESS, null, null, list, null);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response getOrderSumNetAndSelfCreditAmount(List<Integer> orderDetailsIds) throws ApplicationException {

		try {
			
			String qry = "select sum(net_amount) as \"netAmount\"  ,sum(self_payable) as \"selfPayable\" ,sum(credit_payable) as \"creditPayable\"  from t_order_details tod where order_details_id in(:orderDetailsId) ";
			
			StringBuffer sb = new StringBuffer(qry);

			
			OrderDetailsMasterDto dto = (OrderDetailsMasterDto)sessionFactory.getCurrentSession()
					.createSQLQuery(sb.toString())
					.setParameterList("orderDetailsId", orderDetailsIds)
					.setResultTransformer(Transformers.aliasToBean(OrderDetailsMasterDto.class)).uniqueResult();

			
			return new Response(SUCCESS, null, null, null, dto);
		} catch (Exception e) {
			
			throw e;
		}
	
	}

	@Override
	public Response checkInvoiceCreatedOrNotByBillId(TCompanyInvoiceCreationReqDto reqDto) throws ApplicationException {

		try {
			
			//to solve concurrency issue
			List<Integer> billIds= null;
			List<Integer> saleIds= null;
			List<TCompanyInvoiceDetailsDto> billList = null;
			List<TCompanyInvoiceDetailsDto> pharmacyList = null;
			 billIds = reqDto.getListTCompanyInvoiceDetailsDto().stream().filter(obj->obj.getBillId() !=null && obj.getBillId()>0).map(obj->obj.getBillId()).collect(Collectors.toList());
			 saleIds = reqDto.getListTCompanyInvoiceDetailsDto().stream().filter(obj->obj.gettSaleId()!=null && obj.gettSaleId()>0).map(obj->obj.gettSaleId()).collect(Collectors.toList());
			 MCompanyInvoiceMasterDto mCompanyInvoiceMasterDto = reqDto.getmCompanyInvoiceMasterDto();
			 
			 if(billIds != null && billIds.size()>0) {
				 String qry = " select bill_payee_details_id as \"billPayeeDetailsId\" from billing.t_bill_payee_details " + 
					     " where bill_id in (:billIds) and payee_id =:payeeId and is_invoice_generated='Y' ";
			     StringBuffer sb = new StringBuffer(qry);
				if (mCompanyInvoiceMasterDto.getAssociateCompanyId() != null && mCompanyInvoiceMasterDto.getAssociateCompanyId() > 0) {
					sb.append(" and associate_company_id =:associateCompanyId ");
				}
				
				Query sqlQuery = sessionFactory.getCurrentSession()
						.createSQLQuery(sb.toString())
						.setParameterList("billIds", billIds)
						.setParameter("payeeId", mCompanyInvoiceMasterDto.getPayeeId());

				 if (mCompanyInvoiceMasterDto.getAssociateCompanyId() != null && mCompanyInvoiceMasterDto.getAssociateCompanyId() > 0)
					 sqlQuery.setParameter("associateCompanyId", mCompanyInvoiceMasterDto.getAssociateCompanyId());

				 billList = sqlQuery.setResultTransformer(Transformers.aliasToBean(TCompanyInvoiceDetailsDto.class)).list();
			 }
			
			 if(saleIds != null && saleIds.size()>0) {
				 String qrySale = " select payee_details_id as \"payeeDetailsId\" from pharmacy.t_pharmacy_bill_payee_details " + 
					     " where t_sale_id in (:saleIds) and payee_id =:payeeId and is_invoice_generated='Y' ";
			     StringBuffer sbSale = new StringBuffer(qrySale);
			     
			     Query sqlQuerySale = sessionFactory.getCurrentSession()
						.createSQLQuery(sbSale.toString())
						 .setParameterList("saleIds", saleIds)
						 .setParameter("payeeId", mCompanyInvoiceMasterDto.getPayeeId());

				 pharmacyList = sqlQuerySale.setResultTransformer(Transformers.aliasToBean(TCompanyInvoiceDetailsDto.class)).list();
			 }
			 List<TCompanyInvoiceDetailsDto> finalList = null;
			 if(billList != null && billList.size() > 0) {
				finalList = billList;
			 }
			 if(pharmacyList != null && pharmacyList.size() > 0) {
					finalList = pharmacyList;
			 }
			 if(billList != null && pharmacyList != null  && billList.size() > 0 && pharmacyList.size() > 0) {
				 finalList = Stream.concat(billList.stream(), pharmacyList.stream())
                     .collect(Collectors.toList());
			 }
			 
			return new Response(SUCCESS, null, null, finalList, null);
		} catch (Exception e) {
			
			throw e;
		}
	
	}

	@Override
	public Response getDataOfPatientToCheckDueAmountOfPharmacyService(BillingMasterDto billingMasterDto)
			throws ApplicationException {

		try {
			
			String qry = "select " + 
					"	sum(coalesce(pbpd.due_amount,0))  as \"dueAmount\"  " + 
					"from " + 
					"	pharmacy.t_sale tod " + 
					"	inner join pharmacy.t_pharmacy_bill_payee_details pbpd on " + 
					"	pbpd.t_sale_id = tod.id   " + 
					"	 and pbpd.payee_id = (:selfPayeeId) and pbpd.status='A' " +
					"where tod.patient_id =:patientId and tod.is_active = true and tod.is_deleted = false  ";
	
			
			StringBuffer sb = new StringBuffer(qry);

			
			BillingMasterDto dto = (BillingMasterDto)sessionFactory.getCurrentSession()
					.createSQLQuery(sb.toString())
					.setParameter("patientId", billingMasterDto.getPatientId())
					.setParameter("selfPayeeId",billingMasterDto.getSelfPayeeId())
					.setResultTransformer(Transformers.aliasToBean(BillingMasterDto.class)).uniqueResult();
			return new Response(SUCCESS, null, null, null, dto);
		} catch (Exception e) {
			throw e;
		}
	
	}
			
			public Response getBillIdByEncounterId(BillingMasterDto billingMasterDto) throws ApplicationException {
				try {
					
					String qry = " select    " + 
							"		bill.billing_master_id  as \"billingMasterId\"   " + 
							" from    " + 
							"		billing.m_billing_master bill    " + 
							" inner join billing.t_billing_details bhh on    " + 
							"		bhh.billing_master_id = bill.billing_master_id    " + 
							" left join billing.t_bill_payee_details billPayee on    " + 
							"		bill.billing_master_id = billPayee.bill_id    " + 
							"	and billPayee.payee_id =(    " + 
							"	select    " + 
							"			uabc.self_payee_id    " + 
							"	from    " + 
							"			public.t_unit_application_billing_configuration uabc    " + 
							"	where    " + 
							"			uabc.status = 'A'    " + 
							"		and uabc.unit_id = bill.unit_id)    " + 
							" inner join public.t_order_details ordDet on    " + 
							"		ordDet.order_details_id = bhh.order_details_id    " + 
							" where    " + 
							"		bill.bill_cancelled = 'N'    " + 
							"	and bill.is_dr_note = 'N'    " + 
							"	and bill.encounter_id =:encounterId    " + 
							"	and bill.bill_status_id in (1, 2)    " + 
							"	and bill.total_self_payable > 0    " + 
							"	and billPayee.due_amount > 0    " + 
							"	and bill.outstanding > 0    " + 
							"	and coalesce(ordDet.is_consultation_service, 'N' ) = 'Y'    " + 
							"  order by    " + 
							"    " + 
							"		bill.billing_master_id desc    ";
					
			List<BillingMasterDto> list = sessionFactory.getCurrentSession()
					.createSQLQuery(qry.toString())
					.setParameter("encounterId", billingMasterDto.getEncounterId())
					.setResultTransformer(Transformers.aliasToBean(BillingMasterDto.class)).list();

			
			return new Response(SUCCESS, null, null, list, null);
		} catch (Exception e) {
			
			throw e;
		}
	
	}

@Override
public Response getCapWithPackageBalanceAmount(BillingMasterDto billingMasterDto)
		throws ApplicationException {
	try {
		
		String qry = "select tpccd.product_category_id as \"productCategoryId\" ,"
				+ "tpccd.available_cap as \"availableCap\","
				+ "to_char(tpcm.package_applicable_date,'YYYY-MM-DD') as \"packageApplicableDate\","
				+ "tpccd.cap_amount as \"capAmount\" ,"
				+ "tpcm.package_id as \"packageConsumptionMasterId\" "
				+ "from billing.t_package_consumption_master tpcm " + 
				"inner join billing.t_package_consumption_cap_details tpccd  on tpccd.package_consumption_master_id = tpcm .package_consumption_master_id " + 
				"where tpcm.package_type_id = 3 and tpcm.status  = 'A' and tpccd.status ='A' and tpcm.is_force_close  =  1 and tpcm.admission_id =:admissionId and tpccd.available_cap >0 and tpccd.product_category_id  is not null  order by tpcm.package_applicable_date asc ";

		StringBuffer sb = new StringBuffer(qry);
		
		List<TPackageConsumptionCapDetailsDto> list = sessionFactory.getCurrentSession()
				.createSQLQuery(sb.toString())
				.setParameter("admissionId", billingMasterDto.getAdmissionId() != null ? billingMasterDto.getAdmissionId() : 0)
				.setResultTransformer(Transformers.aliasToBean(TPackageConsumptionCapDetailsDto.class)).list();
		return new Response(SUCCESS, null, null, list, null);
	} catch (Exception e) {
		throw e;
	}

}

@Override
public Response checkCapWithPackageAndOrderAmount(BillingMasterDto billingMasterDto,
		TPackageConsumptionCapDetailsDto dto) throws ApplicationException {

	try {
		
		String qry = "select tom.admission_id as \"admissionId\" " + 
				"from t_order_master tom  " + 
				"inner join t_order_details tod  on tom.order_id  = tod.order_id  " + 
				"inner join m_item mi on tod.drug_id = mi.id " + 
				"inner join m_generic mg  on mg.id = mi.generic_id  " + 
				"where  tom.admission_id =:admissionId  and tod.status ='A' " +
				"and tod.ord_cancelled = 'N'  and mg.product_category_id =:productCategoryId and tod.net_amount > 0 and tod.order_qty >0  " +
				"and to_char(tod.order_date,'YYYY-MM-DD') >=:packageApplicableDate and tod.drug_id  is not null ";

		StringBuffer sb = new StringBuffer(qry);
		
		List<BillingMasterDto> list = sessionFactory.getCurrentSession()
				.createSQLQuery(sb.toString())
				.setParameter("admissionId", billingMasterDto.getAdmissionId())
				.setParameter("productCategoryId", dto.getProductCategoryId())
				.setParameter("packageApplicableDate", dto.getPackageApplicableDate())
				.setResultTransformer(Transformers.aliasToBean(BillingMasterDto.class)).list();
		return new Response(SUCCESS, null, null, list, null);
	} catch (Exception e) {
		throw e;
	}


}

@Override
public Response checkCapWithPackageAndConsumeAmount(BillingMasterDto billingMasterDto,
		TPackageConsumptionCapDetailsDto dto) throws ApplicationException {
	try {
		
		String qry = "select SUM(tod.package_consumption_amt) as \"packageConsumptionAmt\" " + 
				"from t_order_master tom  " + 
				"inner join t_order_details tod  on tom.order_id  = tod.order_id  " + 
				"inner join t_order_details_package_mapper todpm on todpm.order_details_id = tod.order_details_id  " + 
				"where  tom.admission_id =:admissionId and tod.status ='A' " +
				"and tod.ord_cancelled = 'N' and todpm.package_id =:packageConsumptionMasterId " +// getPackageConsumptionMasterId used as a package id
				"and to_char(tod.order_date,'YYYY-MM-DD') >=:packageApplicableDate and tod.drug_id  is  null ";

		StringBuffer sb = new StringBuffer(qry);
		
		OrderDetailsMasterDto obj = (OrderDetailsMasterDto)sessionFactory.getCurrentSession()
				.createSQLQuery(sb.toString())
				.setParameter("admissionId", billingMasterDto.getAdmissionId())
				.setParameter("packageConsumptionMasterId", dto.getPackageConsumptionMasterId())
				.setParameter("packageApplicableDate", dto.getPackageApplicableDate())
				.setResultTransformer(Transformers.aliasToBean(OrderDetailsMasterDto.class)).uniqueResult();
		return new Response(SUCCESS, null, null, null, obj);
	} catch (Exception e) {
		throw e;
	}



}

@Override
public Response checkCapWithPackageBalanceAmountAndOrderAmount(BillingMasterDto billingMasterDto , String applicableDate)
		throws ApplicationException {

	try {
		
		String qry = "select tod.order_details_id as \"orderDetailsId\" " + 
				"from t_order_master tom  " + 
				"inner join t_order_details tod  on tom.order_id  = tod.order_id   " + 
				"inner join t_unit_application_billing_configuration tuabc on tuabc.pharmacy_service_id  = tod.service_id and tuabc.unit_id = tod.org_unit_id  " + 
				"where  tom.admission_id =:admissionId and tod.status ='A'  " +
				"and tod.ord_cancelled = 'N' and to_char(tod.order_date,'YYYY-MM-DD') >=:applicableDate and tod.net_amount > 0  and tod.order_qty > 0 and tod.force_add_removed = 0 and  tod.service_is_billed = 0 ";

		StringBuffer sb = new StringBuffer(qry);
		
		List<OrderDetailsMasterDto> pharList = sessionFactory.getCurrentSession()
				.createSQLQuery(sb.toString())
				.setParameter("admissionId", billingMasterDto.getAdmissionId() != null ? billingMasterDto.getAdmissionId() : 0)
				.setParameter("applicableDate", applicableDate)
				.setResultTransformer(Transformers.aliasToBean(OrderDetailsMasterDto.class)).list();
		
		
	/*	String qry1 = "select  tod.order_details_id " + 
				"from t_order_master tom  " + 
				"inner join t_order_details tod  on tom.order_id  = tod.order_id  " + 
				"inner join t_unit_application_billing_configuration tuabc on tuabc.pharmacy_service_id  = tod.service_id and tuabc.unit_id = tod.org_unit_id " + 
				"where  tom.admission_id = "+billingMasterDto.getAdmissionId()+"  and tod.status ='A' " + 
				"and tod.ord_cancelled = 'N' and to_char(tod.order_date,'YYYY-MM-DD') >= '"+applicableDate+"' and tod.order_qty <  0 ";

		StringBuffer sb1 = new StringBuffer(qry1);
		
		List<OrderDetailsMasterDto> pharreturnList = sessionFactory.getCurrentSession()
				.createSQLQuery(sb1.toString())
				.setResultTransformer(Transformers.aliasToBean(OrderDetailsMasterDto.class)).list();*/
		return new Response(SUCCESS, null, null,pharList, null/*,null,pharreturnList*/);
	} catch (Exception e) {
		throw e;
	}




}

@Override
public Response adjustCapWithPackageAvailableAmount(BillingMasterDto billingMasterDto) throws ApplicationException {

	try {
		
		String qry = " select coalesce(orderAmount.packageAmount,0) + coalesce(oldPac.oldPackAmount,0) as \"packageAmount\" , " + 
				" tpcm.package_consumption_master_id as \"packageConsumptionMasterId\" ," + 
				" tpccd.package_consumption_cap_details_id as \"packageConsumptionCapDetailsId\" " + 
				" FROM billing.t_package_consumption_cap_details tpccd " + 
				" INNER JOIN billing.t_package_consumption_master tpcm on tpcm.package_consumption_master_id  = tpccd.package_consumption_master_id " + 
				" left join (select sum(mpp.package_amount) as packageAmount ,mpp.package_consumption_cap_details_id as package_consumption_cap_details_id " + 
				" from t_order_details tod " + 
				" inner join t_order_details_package_mapper mpp on mpp.order_details_id  = tod.order_details_id " + 
				" inner join t_order_master tom on tom.order_id = tod.order_id " + 
				" inner join t_unit_application_billing_configuration mac on mac.unit_id = tod.org_unit_id  " + 
				" inner join service.m_package_master mpm on mpm.package_master_id  = mpp.package_id  " + 
				" where  tod.service_id = mac.pharmacy_service_id  and tod.status ='A' and mpm.package_type_id = 3  and mpp.package_consumption_master_id is not null group by mpp.package_consumption_cap_details_id ) as orderAmount " + 
				" on orderAmount.package_consumption_cap_details_id = tpccd.package_consumption_cap_details_id" + 
				" left join (select sum(packlog.package_amount) as oldPackAmount , packlog.package_consumption_cap_details_id as package_consumption_cap_details_id" + 
				" from  public.t_order_details_package_mapper_log packlog " + 
				" group by package_consumption_cap_details_id ) as oldPac " + 
				"	on  oldPac.package_consumption_cap_details_id = tpccd.package_consumption_cap_details_id " + 
				" where tpcm.admission_id =:admissionId and tpccd.product_category_id  is not null  and tpcm.status= 'A' ";

		StringBuffer sb = new StringBuffer(qry);
		
		List<OrderDetailsPackageMapperDto> list = sessionFactory.getCurrentSession()
				.createSQLQuery(sb.toString())
				.setParameter("admissionId", billingMasterDto.getAdmissionId())
				.setResultTransformer(Transformers.aliasToBean(OrderDetailsPackageMapperDto.class)).list();
		
		
		
	if(list != null && list.size()>0) {
		
		for (OrderDetailsPackageMapperDto orderDetailsPackageMapperDto : list) {
					
					String qry1 = null;
					
					 qry1 = "UPDATE billing.t_package_consumption_cap_details  " + 
					 		"	set available_cap  = cap_amount - :packageAmount" +
					 		" where package_consumption_master_id =:packageConsumptionMasterId  "+
					 		" and package_consumption_cap_details_id =:packageConsumptionCapDetailsId ";
					 sessionFactory.getCurrentSession()
						.createSQLQuery(qry1)
				        .setParameter("packageAmount", orderDetailsPackageMapperDto.getPackageAmount().doubleValue())
				        .setParameter("packageConsumptionMasterId", orderDetailsPackageMapperDto.getPackageConsumptionMasterId())
				        .setParameter("packageConsumptionCapDetailsId", orderDetailsPackageMapperDto.getPackageConsumptionCapDetailsId())
				        .executeUpdate();
				
					
				}
		
	}else {
	
			String qry1 = null;
			
			 qry1 = " UPDATE billing.t_package_consumption_cap_details cap " + 
			 		"SET available_cap = tpccd.cap_amount " + 
			 		"FROM billing.t_package_consumption_cap_details tpccd " + 
			 		"INNER JOIN billing.t_package_consumption_master tpcm ON tpccd.package_consumption_master_id = tpcm.package_consumption_master_id " + 
			 		"WHERE tpcm.package_type_id = 3 " + 
			 		"  AND tpcm.status = 'A' " + 
			 		"  AND tpccd.status = 'A' "
			 		+ " and tpccd.package_consumption_cap_details_id  = cap.package_consumption_cap_details_id " + 
			 		"  AND tpccd.product_category_id IS NOT NULL " + 
			 		"and tpcm.admission_id =:admissionId ";
			 sessionFactory.getCurrentSession()
				.createSQLQuery(qry1)
		        .setParameter("admissionId", billingMasterDto.getAdmissionId())
		        .executeUpdate();
		
	}	
		
		
		
		return new Response(SUCCESS, null, null, null, null);
	} catch (Exception e) {
		throw e;
	}




}

@Override
public Response resetCapWithPackage(BillingMasterDto billingMasterDto) throws ApplicationException {


	try {
		
		if(billingMasterDto.getAdmissionId() != null && billingMasterDto.getAdmissionId() != 0) {
			
			String qry = "  select distinct caplog.package_consumption_cap_details_id as \"packageConsumptionCapDetailsId\" ," + 
					"	tpcm.package_consumption_master_id as \"packageConsumptionMasterId\"," + 
					"	caplog.packageAmount as \"packageAmount\" " + 
					"from billing.t_package_consumption_master tpcm " + 
					"left join ( select coalesce(sum(packlog.package_amount),0) as packageAmount ," + 
					"		packlog.package_consumption_master_id ," + 
					"		packlog.package_consumption_cap_details_id " + 
					"	from " + 
					"		public.t_order_details_package_mapper_log packlog  group by packlog.package_consumption_master_id , " + 
					"		packlog.package_consumption_cap_details_id ) as caplog on " + 
					"	caplog.package_consumption_master_id = tpcm.package_consumption_master_id " + 
					"where " + 
					"	tpcm.admission_id =:admissionId " +
					"	and tpcm.package_consumption_master_id is not null " + 
					"	and caplog.package_consumption_cap_details_id is not null  " ;

			StringBuffer sb = new StringBuffer(qry);
			
			List<OrdeDetailsPackageMapperLogDto> list = sessionFactory.getCurrentSession()
					.createSQLQuery(sb.toString())
					.setParameter("admissionId", billingMasterDto.getAdmissionId())
					.setResultTransformer(Transformers.aliasToBean(OrdeDetailsPackageMapperLogDto.class)).list();
			
			
			if(list != null && list.size()>0) {
				for (OrdeDetailsPackageMapperLogDto ordeDetailsPackageMapperLogDto : list) {
					
					String qry1 = null;
					
					 qry1 = "update billing.t_package_consumption_cap_details cap  " + 
					 		"set available_cap = tpccd.cap_amount - " +(ordeDetailsPackageMapperLogDto.getPackageAmount()!= null ?ordeDetailsPackageMapperLogDto.getPackageAmount():0)+ 
					 		" from  " + 
					 		"	billing.t_package_consumption_cap_details tpccd  " + 
					 		"inner join billing.t_package_consumption_master tpcm on  " + 
					 		"	tpccd.package_consumption_master_id = tpcm.package_consumption_master_id  " + 
					 		"where  " + 
					 		"cap.package_consumption_cap_details_id = tpccd.package_consumption_cap_details_id  " + 
					 		"	and tpcm.package_type_id = 3  " + 
					 		"	and tpcm.status = 'A'  " + 
					 		"	and tpccd.status = 'A'  " + 
					 		"	and tpcm.admission_id =:admissionId " +
					 		"	and tpcm.package_consumption_master_id =:packageConsumptionMasterId  " +
					 		"	and tpccd.package_consumption_cap_details_id =:packageConsumptionCapDetailsId  "
					 		+ " and tpccd.product_category_id is not null  ";
					 sessionFactory.getCurrentSession()
						.createSQLQuery(qry1)
				        .setParameter("admissionId", billingMasterDto.getAdmissionId())
				        .setParameter("packageConsumptionMasterId", ordeDetailsPackageMapperLogDto.getPackageConsumptionMasterId())
				        .setParameter("packageConsumptionCapDetailsId", ordeDetailsPackageMapperLogDto.getPackageConsumptionCapDetailsId())
				        .executeUpdate();
				}
			}else {
				
				String qry1 = null;
				
				 qry1 = " UPDATE billing.t_package_consumption_cap_details cap " + 
				 		"SET available_cap = tpccd.cap_amount " + 
				 		"FROM billing.t_package_consumption_cap_details tpccd " + 
				 		"INNER JOIN billing.t_package_consumption_master tpcm ON tpccd.package_consumption_master_id = tpcm.package_consumption_master_id " + 
				 		"WHERE tpcm.package_type_id = 3 " + 
				 		"  AND tpcm.status = 'A' " + 
				 		"  AND tpccd.status = 'A' "
				 		+ " and tpccd.package_consumption_cap_details_id  = cap.package_consumption_cap_details_id " + 
				 		"  AND tpccd.product_category_id IS NOT NULL " + 
				 		"and tpcm.admission_id =:admissionId ";
				 sessionFactory.getCurrentSession()
					.createSQLQuery(qry1)
			        .setParameter("admissionId", billingMasterDto.getAdmissionId())
			        .executeUpdate();
				
			}
			
			
		
		}
			
		
		
		return new Response(SUCCESS, null, null, null, null);
	} catch (Exception e) {
		throw e;
	}





}

@Override
public Response storePackCapAmountLog(OrderDetailsMasterDto detailsMasterDto)
		throws ApplicationException {
	try {
		String qry = "select todpm.order_details_id \"orderDetailsId\" ,"
				+ "todpm.package_id \"packageId\","
				+ "todpm.package_consumption_master_id \"packageConsumptionMasterId\","
				+ "todpm.package_consumption_cap_details_id \"packageConsumptionCapDetailsId\","
				+ "todpm.package_amount \"packageAmount\" "
				+ "from t_order_details tod " + 
				" inner join t_order_details_package_mapper todpm on todpm.order_details_id = tod.order_details_id " + 
				" inner join service.m_package_master mpm on mpm.package_master_id = todpm.package_id " + 
				" where  mpm.package_type_id = 3 and todpm.package_consumption_master_id is not null  and todpm.order_details_id =:orderDetailsId ";

		StringBuffer sb = new StringBuffer(qry);
		
		List<OrdeDetailsPackageMapperLogDto> list = sessionFactory.getCurrentSession()
				.createSQLQuery(sb.toString())
				.setParameter("orderDetailsId", detailsMasterDto.getOrderDetailsId())
				.setResultTransformer(Transformers.aliasToBean(OrdeDetailsPackageMapperLogDto.class)).list();
		
		return new Response(SUCCESS, null, null, list, null);
	} catch (Exception e) {
		throw e;
	}

}

@Override
public Response checkPackageMinusAmount(BillingMasterDto billingMasterDto) throws ApplicationException {

	try {
		String qry = " select tpcm.package_consumption_master_id as \"packageConsumptionMasterId\" " + 
				"from billing.t_package_consumption_master tpcm " + 
				"inner join billing.t_package_consumption_cap_details tpccd on " + 
				"	tpccd.package_consumption_master_id = tpcm .package_consumption_master_id " + 
				"where tpcm.package_type_id = 3 " + 
				"	and tpcm.status = 'A' " + 
				"	and tpccd.status = 'A' " + 
				"	and tpcm.is_force_close = 1 " + 
				"	and tpcm.admission_id =:admissionId " +
				"	and tpccd.available_cap <0 " + 
				"	and tpccd.product_category_id is not null ";

		StringBuffer sb = new StringBuffer(qry);
		
		List<OrdeDetailsPackageMapperLogDto> list = sessionFactory.getCurrentSession()
				.createSQLQuery(sb.toString())
				.setParameter("admissionId", billingMasterDto.getAdmissionId())
				.setResultTransformer(Transformers.aliasToBean(OrdeDetailsPackageMapperLogDto.class)).list();
		
		return new Response(SUCCESS, null, null, list, null);
	} catch (Exception e) {
		throw e;
	}


}

@Override
public Response checkBillCancelledOrNot(BillingMasterDto billingMasterDto) throws ApplicationException {

	try {
		String qry = "select bill_cancelled as \"billCancelled\"  from billing.m_billing_master mbm where billing_master_id =:id and bill_cancelled ='Y'";

		StringBuffer sb = new StringBuffer(qry);
		
		BillingMasterDto dto = new BillingMasterDto();
		dto = (BillingMasterDto) sessionFactory.getCurrentSession()
				.createSQLQuery(sb.toString())
				.setParameter("id", billingMasterDto.getBillingMasterId())
				.setResultTransformer(Transformers.aliasToBean(BillingMasterDto.class)).uniqueResult();
		
		return new Response(SUCCESS, null, null, null, dto);
	} catch (Exception e) {
		throw e;
	}


}
		
	@Override
	public Response getBillNoByPatientForMyLife(BillListReqDto billListReqDto)
			throws ApplicationException {
		try {

			Integer patientId=(Integer)sessionFactory.getCurrentSession()
					.createSQLQuery("select patReg.patient_id from patient.t_patient_registration patReg "
							+"	where patReg.uuid=:uuid and patReg.organization_id=:orgId ")
					.setParameter("uuid", billListReqDto.getUuid())
					.setParameter("orgId", billListReqDto.getHospitalId())
					.uniqueResult();

			if(patientId != null && patientId>0) {

				StringBuilder stringQuery = new StringBuilder(sessionFactory.getCurrentSession()
						.getNamedQuery("GET_BILL_DETAILS_FOR_BILL_LIST_PATIENT_MYLIFE").getQueryString());

				if (billListReqDto.getFromDate() != null && billListReqDto.getToDate() != null) {
					stringQuery
							.append(" and to_char(bill.bill_date_time, 'YYYY-MM-DD') BETWEEN :fromDate and :toDate");
				}
//			if(billListReqDto.getToDate() != null){
//				stringQuery
//						.append(" and to_char(bill.bill_date_time, 'YYYY-MM-DD') <= :toDate ");
//			}
				stringQuery.append(" order by bill.bill_date_time desc");

				Query query = sessionFactory.getCurrentSession().createSQLQuery(stringQuery.toString());
				query.setParameter("patientId", patientId);

				DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
				DateTimeFormatter queryFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				if (billListReqDto.getFromDate() != null && billListReqDto.getToDate() != null) {
					LocalDate fromDate = LocalDate.parse(billListReqDto.getFromDate(), inputFormatter);
					LocalDate toDate = LocalDate.parse(billListReqDto.getToDate(), inputFormatter);
					query.setParameter("fromDate", fromDate.format(queryFormatter));
					query.setParameter("toDate", toDate.format(queryFormatter));
				}

				List<BillListCNDto> listBillListCNDto = query.setResultTransformer(Transformers.aliasToBean(BillListCNDto.class)).list();
				System.out.println(listBillListCNDto);
				return new Response<>(SUCCESS, SUCCESS_CODE, null, listBillListCNDto, null);
			}
			else{
				return new Response<>(ERROR, COMMON_ERROR_CODE, "Patient Id not found.!", null, null);
			}
		} catch (Exception e) {

		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, null, null, null);
	}

	@Override
	public Response updateDoctorIdAndSpecialityId(AdmissionDto dto) throws ApplicationException {


		try {
		
			String qry1 = null;
			
			 qry1 = " update adt.t_admission  set doctor_id =:doctorId, speciality_id =:specialityId where doctor_id = 0  and admission_id  =:admissionId  ";
			 sessionFactory.getCurrentSession()
				.createSQLQuery(qry1)
				.setParameter("admissionId", dto.getAdmissionId())
				.setParameter("doctorId", dto.getDoctorId())
				.setParameter("specialityId", dto.getSpecialityId())
		        .executeUpdate();

            Date doorToDoctorTime = TimeZoneComponent.getDateByzone(dto.getUnitId());

            if(dto.getDoctorId()!=null && dto.getDoctorId()>0) {
				String qry2 = " update adt.t_admission  set door_to_doctor_time= :createdDate where admission_id  =:admissionId " +
						"    and door_to_doctor_time is null ";
				sessionFactory.getCurrentSession()
						.createSQLQuery(qry2)
						.setParameter("admissionId", dto.getAdmissionId())
						.setParameter("createdDate", doorToDoctorTime)
						.executeUpdate();
			}

			return new Response(SUCCESS, null, null, null, dto);
		} catch (Exception e) {
			throw e;
		}



	}

	@Override
	public Response updateOrderdetailsPackageMapper(Integer encounterId, Integer orgId, Integer unitId, Integer visitTypeId) throws ApplicationException {
		try{
			boolean flag =false;

			int result = sessionFactory.getCurrentSession()
					.createSQLQuery("CALL insert_order_details_package_mapper(:admissionId, :unitId, :orgId, :visitTypeIds)")
					.setParameter("admissionId", encounterId)  // Pass the correct parameter value
					.setParameter("unitId", unitId)
					.setParameter("orgId", orgId)
					.setParameter("visitTypeIds", visitTypeId)
					.executeUpdate();

			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, flag);
		}catch(Exception e){

			throw e;
		}
	}

    @Override
    public Response getBillDetailsByListOfBillIds(List<Integer> billIds) throws ApplicationException {
        try {
            AdmissionDto dto = new AdmissionDto();
            List<BillDetailsByBillIdDto> detailsByBillIdDtosList = sessionFactory.getCurrentSession()
                    .getNamedQuery("GET_BILL_DETAILS_BY_LIST_OF_BILL_IDS")
                    .setParameterList("billIds", billIds)
                    .setResultTransformer(Transformers.aliasToBean(BillDetailsByBillIdDto.class)).list();

            return new Response<>(SUCCESS, SUCCESS_CODE, null, detailsByBillIdDtosList, null);
        } catch (Exception e) {

            throw e;
        }
        //	return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
    }

}
