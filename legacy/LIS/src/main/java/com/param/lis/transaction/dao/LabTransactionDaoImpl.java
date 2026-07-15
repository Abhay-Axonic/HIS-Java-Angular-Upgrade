package com.param.lis.transaction.dao;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.entity.lis.transaction.LabReportDocumentDetails;
import com.param.entity.lis.transaction.LabSampleDetailsMaster;
import com.param.entity.lis.transaction.LabSampleMaster;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ServiceException;
import com.param.global.dto.LabSortingMaster;
import com.param.global.dto.LastInvestigationDto;
import com.param.global.dto.UserMasterDto;
import com.param.lis.common.dto.CommonAutoCompleteDto;
import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.ITransactionConstants;
import com.param.lis.global.common.Response;
import com.param.lis.global.common.SearchDto;
import com.param.lis.global.common.StringCommonMethods;
import com.param.lis.global.dto.BarcodeDto;
import com.param.lis.transaction.dto.CollectedSampleDto;
import com.param.lis.transaction.dto.LabDashBoardDto;
import com.param.lis.transaction.dto.LabReportDocumentDetailsDto;
import com.param.lis.transaction.dto.LabResultMasterDto;
import com.param.lis.transaction.dto.LabSampleDetailsMasterDto;
import com.param.lis.transaction.dto.LabSampleDetailsMasterDtoIP;
import com.param.lis.transaction.dto.LabSampleMasterDto;
import com.param.lis.transaction.dto.SearchCommonDto;
import com.param.opd.encounter.dto.EncounterMasterDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked","unused" })
public class LabTransactionDaoImpl extends GenericDao<LabSampleMaster, Integer>
		implements ILabTransactionDao, ICommonConstants, ITransactionConstants, IError {

	@Autowired
	private SessionFactory sessionFactory;

	public LabTransactionDaoImpl() {
		super(LabSampleMaster.class);
	}

	@Autowired
	private DozerBeanMapper mapper;

	@Autowired
	private ILabCentralReceivingDao iLabCentralReceivingDao;

//	@Autowired
//	private SessionService sessionService;

	final static Logger logger = Logger.getLogger(LabTransactionDaoImpl.class);

	@Override
	public Response getPhlebotomyWorklist(Integer orgId, Integer orgUnitId, Integer deptId, Integer offset,
			Integer recordPerPage, Character isPreOrPostBilling) throws ApplicationException {

		List<LabSampleMasterDto> listLabSampleMasterDto = null;
		try {
			// System.out.print("sessionService.getIsPreOrPostBilling()"+sessionService.getIsPreOrPostBilling());
			if (isPreOrPostBilling == 'Y') {
				listLabSampleMasterDto = sessionFactory.getCurrentSession().createSQLQuery(getLabOrderQuery(orgUnitId))
						.setParameter("orgId", orgId).setParameter("orgUnitId", orgUnitId).setParameter("visitTypeId", ICommonConstants.OP)
						.setParameterList("orderStatusId", PENDING_INPROGRESS_RENDER_CANCEL)
						.setParameter("serviceIsBilled", 1).setFirstResult(offset != null ? offset : 0)
						.setMaxResults(recordPerPage != null ? recordPerPage : 10)
						.setResultTransformer(Transformers.aliasToBean(LabSampleMasterDto.class)).list();
			} else {
				listLabSampleMasterDto = sessionFactory.getCurrentSession()
						.createSQLQuery(getLabOrderQueryPostBilling(orgUnitId)).setParameter("orgId", orgId)
						.setParameter("orgUnitId", orgUnitId).setParameter("visitTypeId", ICommonConstants.OP)
						.setParameterList("orderStatusId", PENDING_INPROGRESS_RENDER_CANCEL)
						.setFirstResult(offset != null ? offset : 0)
						.setMaxResults(recordPerPage != null ? recordPerPage : 10)
						.setResultTransformer(Transformers.aliasToBean(LabSampleMasterDto.class)).list();
			}

			return new Response(SUCCESS, SUCCESS_200, null, listLabSampleMasterDto, null);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	public String getLabOrderQuery(Integer orgUnitId) {
		Date date = TimeZoneComponent.getDateByzone(orgUnitId);
		String query = "select " + "	distinct(ord_mst.order_id) as \"orderId\", "
				+ "	ord_mst.org_id as \"orgId\", " + "	ord_mst.org_unit_id as \"orgUnitId\", "
				+ "	ord_mst.order_status_id as \"orderStatusId\", " + "	ord_mst.visit_type_id as \"visitTypeId\", "
				+ "	visit_type_mst.visit_type_code as \"visitType\", " + "	ord_mst.ord_doctor_id as \"doctorId\", "
				+ "	ord_mst.patient_id as \"patientId\", " + "	ord_mst.admission_id as \"admissionId\", "
				+ "	ord_mst.encounter_id as \"encounterId\", " + "	ord_mst.created_by as \"createdBy\", "
				+ "	ord_mst.created_date as \"createdDate\", " + "	pati_mst.uhid_number as uhid, "
				+ " concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, pati_mst.dob))) as \"age\", "
				+ " pati_mst.is_vip as \"isVip\", " + " (case "
				+ " 		when pati_mst.is_vip = 'Y' then coalesce(pati_mst.alise_name, pati_mst.first_name) || ' ' || calculate_age(pati_mst.dob) "
				+ " || '/' || gen_mst.gender_code "
				+ " 		else pre_mst.prefix_code || ' ' || pati_mst.first_name || ' ' || pati_mst.last_name || ' ' || calculate_age(pati_mst.dob) "
				+ " || '/' || gen_mst.gender_code "
				+ " 	end ) as \"patientDetails\", " + "	gen_mst.gender_id as \"genderId\", "
				+ "	'Dr.' || doct_mst.first_name || ' ' || doct_mst.last_name || ' (' || speciality_master.speciality_name || ')' as \"doctorDetails\", "
				+ "	pri_mst.priority_id as \"priorityId\", " + "	pri_mst.priority_name as \"priorityName\", "
				+ "	pri_mst.color_code as \"colorCode\", " + "	enc.encounter_type_id as \"encounterTypeId\", "
				+ "	coalesce(enc.is_critical,'N') as \"isCritical\", " + " reflMst.referral_name as \"referralName\" "
				+ "from " + "	public.t_order_master ord_mst " + "inner join public.t_order_details ord_details on "
				+ "	ord_details.order_id = ord_mst.order_id " + "inner join billing.t_billing_details bill_dtls on "
				+ "	ord_details.order_details_id = bill_dtls.order_details_id "
				+ "inner join billing.m_billing_master bill_mst on "
				+ "	bill_mst.billing_master_id = bill_dtls.billing_master_id and bill.bill_cancelled = 'N' "
				+ "inner join opd.t_encounter_master enc on " + "	 bill_mst.encounter_id = enc.encounter_id "
				+ "inner join patient.t_patient_registration pati_mst on "
				+ "	ord_mst.patient_id = pati_mst.patient_id " + "inner join public.m_gender_master gen_mst on "
				+ "	gen_mst.gender_id = pati_mst.gender_id " + "inner join public.m_prefix_master pre_mst on "
				+ "	pre_mst.prefix_id = pati_mst.prefix_id "
				+ "inner join public.m_visit_type_master visit_type_mst on "
				+ "	visit_type_mst.visit_type_id = ord_mst.visit_type_id "
				+ "inner join service.t_unit_service_master_details service_dtls on "
				+ "	ord_details.service_id = service_dtls.service_master_id " + "	and service_dtls.status = 'A' "
				+ "inner join public.t_lab_configuration lab_config on " + "	lab_config.organisation_id = :orgId "
				+ "	and lab_config.unit_id = :orgUnitId " + "inner join lab.m_test_master test_mst on "
				+ "	ord_details.service_id = test_mst.service_id " + "	and test_mst.test_status = 'A' "
				+ "	and test_mst.is_histo_cyto in ('N','S') " + "left join doctor.m_doctor_master doct_mst on "
				+ "	ord_mst.ord_doctor_id = doct_mst.doctor_id " + "left join lab.m_priority_mASter pri_mst on "
				+ "	ord_mst.priority_id = pri_mst.priority_id "
				/*
				 * + "left join doctor.t_doctor_speciality_mapper doct_speciality_mppr on " +
				 * "	 doct_speciality_mppr.doctor_id = doct_mst.doctor_id " +
				 * "	 and doct_speciality_mppr.status = 'A' "
				 */
				+ "left join doctor.m_speciality_master speciality_master on "
				+ "		speciality_master.speciality_id = ord_mst.ord_doc_spl_id  "
				+ " left join public.t_referal_details refrDtls on "
				+ "		refrDtls.encounter_id = enc.encounter_id " + " left join adt.m_referral_master reflMst on "
				+ " 	reflMst.referral_id = refrDtls.referral_id " + "where " + "	ord_mst.org_id =:orgId "
				+ "	and ord_mst.org_unit_id =:orgUnitId " + "	and bill_mst.bill_status_id <> 1 "
				+ "	and ord_mst.order_status = 'A' " + "	and ord_mst.visit_type_id =:visitTypeId "
				+ "	and ord_mst.order_status_id in (:orderStatusId) " + "	and ord_details.service_rendered in (0,1) "
				+ "	and ord_details.service_is_billed =:serviceIsBilled " + "	and ord_details.ord_cancelled = 'N' "
				+ "	and ord_details.is_outsourced = 'N' "
				// +" and ord_details.is_from_addservice = 'N' "
				+ "	and ord_details.status = 'A' " + "  and ord_details.is_order_visible_dept='A' "
				+ "	and ord_details.service_rendering_deptid in (lab_config.biochemistry_id,lab_config.hematology_id,lab_config.microbiology_id,lab_config.serology_id,lab_config.clinical_pathology_id) "
				+ "	and to_char(ord_details.order_date,'YYYY-MM-DD') >= '"
				+ GlobalCommonDateUtils.getStringDate(date, "yyyy-MM-dd") + "'"
				+ "	and to_char(ord_details.order_date,'YYYY-MM-DD') <='"
				+ GlobalCommonDateUtils.getStringDate(date, "yyyy-MM-dd") + "'" + " union " + "select "
				+ "	distinct(ord_mst.order_id) as \"orderId\", " + "	ord_mst.org_id as \"orgId\", "
				+ "	ord_mst.org_unit_id as \"orgUnitId\", " + "	ord_mst.order_status_id as \"orderStatusId\", "
				+ "	ord_mst.visit_type_id as \"visitTypeId\", " + "	visit_type_mst.visit_type_code as \"visitType\", "
				+ "	ord_mst.ord_doctor_id as \"doctorId\", " + "	ord_mst.patient_id as \"patientId\", "
				+ "	ord_mst.admission_id as \"admissionId\", " + "	ord_mst.encounter_id as \"encounterId\", "
				+ "	ord_mst.created_by as \"createdBy\", " + "	ord_mst.created_date as \"createdDate\", "
				+ "	pati_mst.uhid_number as uhid, "
				+ " concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, pati_mst.dob))) as \"age\", "
				+ " pati_mst.is_vip as \"isVip\", " + " (case "
				+ " 		when pati_mst.is_vip = 'Y' then coalesce(pati_mst.alise_name, pati_mst.first_name) || ' ' || calculate_age(pati_mst.dob)  || '/' || gen_mst.gender_code "
				+ " 		else pre_mst.prefix_code || ' ' || pati_mst.first_name || ' ' || pati_mst.last_name || ' ' ||  calculate_age(pati_mst.dob)  || '/' || gen_mst.gender_code "
				+ " 	end ) as \"patientDetails\", " + "	gen_mst.gender_id as \"genderId\", "
				+ "	'Dr.' || doct_mst.first_name || ' ' || doct_mst.last_name || ' (' || speciality_master.speciality_name || ')' as \"doctorDetails\", "
				+ "	pri_mst.priority_id as \"priorityId\", " + "	pri_mst.priority_name as \"priorityName\", "
				+ "	pri_mst.color_code as \"colorCode\", " + "	enc.encounter_type_id as \"encounterTypeId\", "
				+ "	coalesce(enc.is_critical,'N') as \"isCritical\", " + " reflMst.referral_name as \"referralName\" "
				+ "from " + "	public.t_order_master ord_mst " + "inner join public.t_order_details ord_details on "
				+ "	ord_details.order_id = ord_mst.order_id " + "inner join billing.t_billing_details bill_dtls on "
				+ "	ord_details.order_details_id = bill_dtls.order_details_id "
				+ "inner join billing.m_billing_master bill_mst on "
				+ "	bill_mst.billing_master_id = bill_dtls.billing_master_id and bill_mst.bill_cancelled ='N' "
				+ "inner join opd.t_encounter_master enc on " + "	 bill_mst.encounter_id = enc.encounter_id "
				+ "inner join patient.t_patient_registration pati_mst on "
				+ "	ord_mst.patient_id = pati_mst.patient_id " + "inner join public.m_gender_master gen_mst on "
				+ "	gen_mst.gender_id = pati_mst.gender_id " + "inner join public.m_prefix_master pre_mst on "
				+ "	pre_mst.prefix_id = pati_mst.prefix_id "
				+ "inner join public.m_visit_type_master visit_type_mst on "
				+ "	visit_type_mst.visit_type_id = ord_mst.visit_type_id "
				+ "inner join service.t_unit_service_master_details service_dtls on "
				+ "	ord_details.service_id = service_dtls.service_master_id " + "	and service_dtls.status = 'A' "
				+ "inner join public.t_lab_configuration lab_config on " + "	lab_config.organisation_id = :orgId "
				+ "	and lab_config.unit_id = :orgUnitId " + "left join doctor.m_doctor_master doct_mst on "
				+ "	ord_mst.ord_doctor_id = doct_mst.doctor_id " + "left join lab.m_priority_mASter pri_mst on "
				+ "	ord_mst.priority_id = pri_mst.priority_id "
				/*
				 * + "left join doctor.t_doctor_speciality_mapper doct_speciality_mppr on " +
				 * "	 doct_speciality_mppr.doctor_id = doct_mst.doctor_id " +
				 * "	 and doct_speciality_mppr.status = 'A' "
				 */
				+ " left join doctor.m_speciality_master speciality_master on "
				+ "		speciality_master.speciality_id = ord_mst.ord_doc_spl_id "
				+ " left join public.t_referal_details refrDtls on "
				+ "		refrDtls.encounter_id = enc.encounter_id " + " left join adt.m_referral_master reflMst on "
				+ " 	reflMst.referral_id = refrDtls.referral_id " + "where " + "	ord_mst.org_id =:orgId "
				+ "	and ord_mst.org_unit_id =:orgUnitId " + "	and bill_mst.bill_status_id <> 1 "
				+ "	and ord_mst.order_status = 'A' " + "	and ord_mst.visit_type_id =:visitTypeId "
				+ "	and ord_mst.order_status_id in (:orderStatusId) " + "	and ord_details.service_rendered in (0,1) "
				+ "	and ord_details.service_is_billed =:serviceIsBilled " + "	and ord_details.ord_cancelled = 'N' "
				+ "	and ord_details.is_outsourced = 'N' " + "	and ord_details.status = 'A' "
				+ "	and service_dtls.is_panel = 'Y' "
				// +" and ord_details.is_from_addservice = 'N' "
				+ "  and ord_details.is_order_visible_dept='A' "
				+ "	and ord_details.service_rendering_deptid not in (lab_config.histopathology_id,lab_config.cystopathology_id) "
				+ "	and to_char(ord_details.order_date,'YYYY-MM-DD') >= '"
				+ GlobalCommonDateUtils.getStringDate(date, "yyyy-MM-dd") + "'"
				+ "	and to_char(ord_details.order_date,'YYYY-MM-DD') <='"
				+ GlobalCommonDateUtils.getStringDate(date, "yyyy-MM-dd") + "'" + " order by "
				+ "	\"isCritical\" desc,\"priorityId\" , " + "	\"createdDate\" DESC	";
		return query;
	}

	public String getLabOrderQueryPostBilling(Integer orgUnitId) {
		Date date = TimeZoneComponent.getDateByzone(orgUnitId);
		String query = "select " + "	distinct(ord_mst.order_id) as \"orderId\", "
				+ "	ord_mst.org_id as \"orgId\", " + "	ord_mst.org_unit_id as \"orgUnitId\", "
				+ "	ord_mst.order_status_id as \"orderStatusId\", " + "	ord_mst.visit_type_id as \"visitTypeId\", "
				+ "	visit_type_mst.visit_type_code as \"visitType\", " + "	ord_mst.ord_doctor_id as \"doctorId\", "
				+ "	ord_mst.patient_id as \"patientId\", " + "	ord_mst.admission_id as \"admissionId\", "
				+ "	ord_mst.encounter_id as \"encounterId\", " + "	ord_mst.created_by as \"createdBy\", "
				+ "	ord_mst.created_date as \"createdDate\", " + "	pati_mst.uhid_number as uhid, "
				+ " concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, pati_mst.dob))) as \"age\", "
				+ " pati_mst.is_vip as \"isVip\", " + " (case "
				+ " 		when pati_mst.is_vip = 'Y' then coalesce(pati_mst.alise_name, pati_mst.first_name) || ' ' || calculate_age(pati_mst.dob) "
				+ " || '/' || gen_mst.gender_code "
				+ " 		else pre_mst.prefix_code || ' ' || pati_mst.first_name || ' ' || pati_mst.last_name || ' ' || calculate_age(pati_mst.dob)  "
				+ " || '/' || gen_mst.gender_code "
				+ " 	end ) as \"patientDetails\", " + "	gen_mst.gender_id as \"genderId\", "
				+ "	'Dr.' || doct_mst.first_name || ' ' || doct_mst.last_name || ' (' || speciality_master.speciality_name || ')' as \"doctorDetails\", "
				+ "	pri_mst.priority_id as \"priorityId\", " + "	pri_mst.priority_name as \"priorityName\", "
				+ "	pri_mst.color_code as \"colorCode\", " + "	enc.encounter_type_id as \"encounterTypeId\", "
				+ "	coalesce(enc.is_critical,'N') as \"isCritical\" " + "from " + "	public.t_order_master ord_mst "
				+ "inner join public.t_order_details ord_details on " + "	ord_details.order_id = ord_mst.order_id "
				/*
				 * + "inner join billing.t_billing_details bill_dtls on " +
				 * "	ord_details.order_details_id = bill_dtls.order_details_id " +
				 * "inner join billing.m_billing_master bill_mst on " +
				 * "	bill_mst.billing_master_id = bill_dtls.billing_master_id "
				 */
				+ "inner join opd.t_encounter_master enc on " + "	 ord_mst.encounter_id = enc.encounter_id "
				+ "inner join patient.t_patient_registration pati_mst on "
				+ "	ord_mst.patient_id = pati_mst.patient_id " + "inner join public.m_gender_master gen_mst on "
				+ "	gen_mst.gender_id = pati_mst.gender_id " + "inner join public.m_prefix_master pre_mst on "
				+ "	pre_mst.prefix_id = pati_mst.prefix_id "
				+ "inner join public.m_visit_type_master visit_type_mst on "
				+ "	visit_type_mst.visit_type_id = ord_mst.visit_type_id "
				+ "inner join service.t_unit_service_master_details service_dtls on "
				+ "	ord_details.service_id = service_dtls.service_master_id " + "	and service_dtls.status = 'A' "
				+ "inner join public.t_lab_configuration lab_config on " + "	lab_config.organisation_id = :orgId "
				+ "	and lab_config.unit_id = :orgUnitId " + "inner join lab.m_test_master test_mst on "
				+ "	ord_details.service_id = test_mst.service_id " + "	and test_mst.test_status = 'A' "
				+ "	and test_mst.is_histo_cyto in ('N','S') " + "left join doctor.m_doctor_master doct_mst on "
				+ "	ord_mst.ord_doctor_id = doct_mst.doctor_id " + "left join lab.m_priority_mASter pri_mst on "
				+ "	ord_mst.priority_id = pri_mst.priority_id "
				/*
				 * + "left join doctor.t_doctor_speciality_mapper doct_speciality_mppr on " +
				 * "	 doct_speciality_mppr.doctor_id = doct_mst.doctor_id " +
				 * "	 and doct_speciality_mppr.status = 'A' "
				 */
				+ "left join doctor.m_speciality_master speciality_master on "
				+ "	speciality_master.speciality_id = ord_mst.ord_doc_spl_id " + "where " + "	ord_mst.org_id =:orgId "
				+ "	and ord_mst.org_unit_id =:orgUnitId "
				// + " and bill_mst.bill_status_id <> 1 "
				+ "	and ord_mst.order_status = 'A' " + "	and ord_mst.visit_type_id =:visitTypeId "
				+ "	and ord_mst.order_status_id in (:orderStatusId) " + "	and ord_details.service_rendered in (0,1) "
				// + " and ord_details.service_is_billed =:serviceIsBilled "
				+ "	and ord_details.ord_cancelled = 'N' " + "	and ord_details.is_outsourced = 'N' "
				// +" and ord_details.is_from_addservice = 'N' "
				+ "	and ord_details.status = 'A' " + "  and ord_details.is_order_visible_dept='A' "
				+ "	and ord_details.service_rendering_deptid in (lab_config.biochemistry_id,lab_config.hematology_id,lab_config.microbiology_id,lab_config.serology_id,lab_config.clinical_pathology_id) "
				+ "	and to_char(ord_details.order_date,'YYYY-MM-DD') >= '"
				+ GlobalCommonDateUtils.getStringDate(date, "yyyy-MM-dd") + "'"
				+ "	and to_char(ord_details.order_date,'YYYY-MM-DD') <='"
				+ GlobalCommonDateUtils.getStringDate(date, "yyyy-MM-dd") + "'" + " union " + "select "
				+ "	distinct(ord_mst.order_id) as \"orderId\", " + "	ord_mst.org_id as \"orgId\", "
				+ "	ord_mst.org_unit_id as \"orgUnitId\", " + "	ord_mst.order_status_id as \"orderStatusId\", "
				+ "	ord_mst.visit_type_id as \"visitTypeId\", " + "	visit_type_mst.visit_type_code as \"visitType\", "
				+ "	ord_mst.ord_doctor_id as \"doctorId\", " + "	ord_mst.patient_id as \"patientId\", "
				+ "	ord_mst.admission_id as \"admissionId\", " + "	ord_mst.encounter_id as \"encounterId\", "
				+ "	ord_mst.created_by as \"createdBy\", " + "	ord_mst.created_date as \"createdDate\", "
				+ "	pati_mst.uhid_number as uhid, "
				+ " concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, pati_mst.dob))) as \"age\", "
				+ " pati_mst.is_vip as \"isVip\", " + " (case "
				+ " 		when pati_mst.is_vip = 'Y' then coalesce(pati_mst.alise_name, pati_mst.first_name) || ' ' || calculate_age(pati_mst.dob)  "
				+ " || '/' || gen_mst.gender_code "
				+ " 		else pre_mst.prefix_code || ' ' || pati_mst.first_name || ' ' || pati_mst.last_name || ' ' || calculate_age(pati_mst.dob)  "
				+ " || '/' || gen_mst.gender_code "
				+ " 	end ) as \"patientDetails\", " + "	gen_mst.gender_id as \"genderId\", "
				+ "	'Dr.' || doct_mst.first_name || ' ' || doct_mst.last_name || ' (' || speciality_master.speciality_name || ')' as \"doctorDetails\", "
				+ "	pri_mst.priority_id as \"priorityId\", " + "	pri_mst.priority_name as \"priorityName\", "
				+ "	pri_mst.color_code as \"colorCode\", " + "	enc.encounter_type_id as \"encounterTypeId\", "
				+ "	coalesce(enc.is_critical,'N') as \"isCritical\" " + "from " + "	public.t_order_master ord_mst "
				+ "inner join public.t_order_details ord_details on " + "	ord_details.order_id = ord_mst.order_id "
				/*
				 * + "inner join billing.t_billing_details bill_dtls on " +
				 * "	ord_details.order_details_id = bill_dtls.order_details_id " +
				 * "inner join billing.m_billing_master bill_mst on " +
				 * "	bill_mst.billing_master_id = bill_dtls.billing_master_id "
				 */
				+ "inner join opd.t_encounter_master enc on " + "	 ord_mst.encounter_id = enc.encounter_id "
				+ "inner join patient.t_patient_registration pati_mst on "
				+ "	ord_mst.patient_id = pati_mst.patient_id " + "inner join public.m_gender_master gen_mst on "
				+ "	gen_mst.gender_id = pati_mst.gender_id " + "inner join public.m_prefix_master pre_mst on "
				+ "	pre_mst.prefix_id = pati_mst.prefix_id "
				+ "inner join public.m_visit_type_master visit_type_mst on "
				+ "	visit_type_mst.visit_type_id = ord_mst.visit_type_id "
				+ "inner join service.t_unit_service_master_details service_dtls on "
				+ "	ord_details.service_id = service_dtls.service_master_id " + "	and service_dtls.status = 'A' "
				+ "inner join public.t_lab_configuration lab_config on " + "	lab_config.organisation_id = :orgId "
				+ "	and lab_config.unit_id = :orgUnitId " + "left join doctor.m_doctor_master doct_mst on "
				+ "	ord_mst.ord_doctor_id = doct_mst.doctor_id " + "left join lab.m_priority_mASter pri_mst on "
				+ "	ord_mst.priority_id = pri_mst.priority_id "
				/*
				 * + "left join doctor.t_doctor_speciality_mapper doct_speciality_mppr on " +
				 * "	 doct_speciality_mppr.doctor_id = doct_mst.doctor_id " +
				 * "	 and doct_speciality_mppr.status = 'A' "
				 */
				+ "left join doctor.m_speciality_master speciality_master on "
				+ "	speciality_master.speciality_id = ord_mst.ord_doc_spl_id " + "where " + "	ord_mst.org_id =:orgId "
				+ "	and ord_mst.org_unit_id =:orgUnitId "
				// + " and bill_mst.bill_status_id <> 1 "
				+ "	and ord_mst.order_status = 'A' " + "	and ord_mst.visit_type_id =:visitTypeId "
				+ "	and ord_mst.order_status_id in (:orderStatusId) " + "	and ord_details.service_rendered in (0,1) "
				// + " and ord_details.service_is_billed =:serviceIsBilled "
				+ "	and ord_details.ord_cancelled = 'N' " + "	and ord_details.is_outsourced = 'N' "
				+ "	and ord_details.status = 'A' " + "	and service_dtls.is_panel = 'Y' "
				// +" and ord_details.is_from_addservice = 'N' "
				+ "  and ord_details.is_order_visible_dept='A' "
				+ "	and ord_details.service_rendering_deptid not in (lab_config.histopathology_id,lab_config.cystopathology_id) "
				+ "	and to_char(ord_details.order_date,'YYYY-MM-DD') >= '"
				+ GlobalCommonDateUtils.getStringDate(date, "yyyy-MM-dd") + "'"
				+ "	and to_char(ord_details.order_date,'YYYY-MM-DD') <='"
				+ GlobalCommonDateUtils.getStringDate(date, "yyyy-MM-dd") + "'" + " order by "
				+ "	\"isCritical\" desc, \"priorityId\" , " + "	\"createdDate\" DESC	";
		return query;
	}

	public String getLabOrderCount(SearchDto searchDto) {
		Date date = TimeZoneComponent.getDateByzone(searchDto.getOrgUnitId());
		if (searchDto.getFromDate() == null) {
			searchDto.setFromDate(date.getTime());
		}
		if (searchDto.getToDate() == null) {
			searchDto.setToDate(date.getTime());
		}

		String query = "with lab_order_count as (select " + "	distinct(ord_mst.order_id) as \"orderId\" " + "from "
				+ "	public.t_order_master ord_mst " + "inner join public.t_order_details ord_details on "
				+ "	ord_details.order_id = ord_mst.order_id " + "inner join billing.t_billing_details bill_dtls on "
				+ "	ord_details.order_details_id = bill_dtls.order_details_id "
				+ "inner join billing.m_billing_master bill_mst on "
				+ "	bill_mst.billing_master_id = bill_dtls.billing_master_id and bill_mst.bill_cancelled = 'N' "
				+ "inner join patient.t_patient_registration pati_mst on "
				+ "	ord_mst.patient_id = pati_mst.patient_id " + "inner join opd.t_encounter_master enc on "
				+ "	 ord_mst.encounter_id = enc.encounter_id " + "inner join public.m_gender_master gen_mst on "
				+ "	gen_mst.gender_id = pati_mst.gender_id " + "inner join public.m_prefix_master pre_mst on "
				+ "	pre_mst.prefix_id = pati_mst.prefix_id "
				+ "inner join public.m_visit_type_master visit_type_mst on "
				+ "	visit_type_mst.visit_type_id = ord_mst.visit_type_id "
				+ "inner join service.t_unit_service_master_details service_dtls on "
				+ "	ord_details.service_id = service_dtls.service_master_id " + "	and service_dtls.status = 'A' "
				+ "inner join public.t_lab_configuration lab_config on " + "	lab_config.organisation_id = :orgId "
				+ "	and lab_config.unit_id = :orgUnitId " + "inner join lab.m_test_master test_mst on "
				+ "	ord_details.service_id = test_mst.service_id " + "	and test_mst.test_status = 'A' "
				+ "	and test_mst.is_histo_cyto in ('N','S') " + "left join doctor.m_doctor_master doct_mst on "
				+ "	ord_mst.ord_doctor_id = doct_mst.doctor_id " + "left join lab.m_priority_mASter pri_mst on "
				+ "	ord_mst.priority_id = pri_mst.priority_id "
				/*
				 * + "left join doctor.t_doctor_speciality_mapper doct_speciality_mppr on " +
				 * "	 doct_speciality_mppr.doctor_id = doct_mst.doctor_id " +
				 * "	 and doct_speciality_mppr.status = 'A' "
				 */
				+ "left join doctor.m_speciality_master speciality_master on "
				+ "	speciality_master.speciality_id = ord_mst.ord_doc_spl_id " + "where " + "	ord_mst.org_id =:orgId "
				+ "	and ord_mst.org_unit_id =:orgUnitId " + "	and bill_mst.bill_status_id <> 1 "
				+ "	and ord_mst.order_status = 'A' " + "	and ord_mst.visit_type_id =:visitTypeId "
				+ "	and ord_mst.order_status_id in (:orderStatusId) " + "	and ord_details.service_rendered in (0,1) "
				+ "	and ord_details.service_is_billed =:serviceIsBilled " + "	and ord_details.ord_cancelled = 'N' "
				+ "	and ord_details.is_outsourced = 'N' " + "	and ord_details.status = 'A' "
				// +" and ord_details.is_from_addservice = 'N' "
				+ "  and ord_details.is_order_visible_dept='A' "
				+ "	and ord_details.service_rendering_deptid in (lab_config.biochemistry_id,lab_config.hematology_id,lab_config.microbiology_id,lab_config.serology_id,lab_config.clinical_pathology_id) "
				+ "	and to_char(ord_details.order_date,'YYYY-MM-DD') >= '"
				+ GlobalCommonDateUtils.getStringDate(searchDto.getToDate(), "yyyy-MM-dd") + "'"
				+ "	and to_char(ord_details.order_date,'YYYY-MM-DD') <='"
				+ GlobalCommonDateUtils.getStringDate(searchDto.getToDate(), "yyyy-MM-dd") + "'" + " union " + "select "
				+ "	distinct(ord_mst.order_id) as \"orderId\" " + "from " + "	public.t_order_master ord_mst "
				+ "inner join public.t_order_details ord_details on " + "	ord_details.order_id = ord_mst.order_id "
				+ "inner join billing.t_billing_details bill_dtls on "
				+ "	ord_details.order_details_id = bill_dtls.order_details_id "
				+ "inner join billing.m_billing_master bill_mst on "
				+ "	bill_mst.billing_master_id = bill_dtls.billing_master_id and bill_mst.bill_cancelled = 'N' "
				+ "inner join patient.t_patient_registration pati_mst on "
				+ "	ord_mst.patient_id = pati_mst.patient_id " + "inner join opd.t_encounter_master enc on "
				+ "	 ord_mst.encounter_id = enc.encounter_id " + "inner join public.m_gender_master gen_mst on "
				+ "	gen_mst.gender_id = pati_mst.gender_id " + "inner join public.m_prefix_master pre_mst on "
				+ "	pre_mst.prefix_id = pati_mst.prefix_id "
				+ "inner join public.m_visit_type_master visit_type_mst on "
				+ "	visit_type_mst.visit_type_id = ord_mst.visit_type_id "
				+ "inner join service.t_unit_service_master_details service_dtls on "
				+ "	ord_details.service_id = service_dtls.service_master_id " + "	and service_dtls.status = 'A' "
				+ "inner join public.t_lab_configuration lab_config on " + "	lab_config.organisation_id = :orgId "
				+ "	and lab_config.unit_id = :orgUnitId " + "left join doctor.m_doctor_master doct_mst on "
				+ "	ord_mst.ord_doctor_id = doct_mst.doctor_id " + "left join lab.m_priority_mASter pri_mst on "
				+ "	ord_mst.priority_id = pri_mst.priority_id "
				/*
				 * + "left join doctor.t_doctor_speciality_mapper doct_speciality_mppr on " +
				 * "	 doct_speciality_mppr.doctor_id = doct_mst.doctor_id " +
				 * "	 and doct_speciality_mppr.status = 'A' "
				 */
				+ "left join doctor.m_speciality_master speciality_master on "
				+ "	speciality_master.speciality_id = ord_mst.ord_doc_spl_id " + "where " + "	ord_mst.org_id =:orgId "
				+ "	and ord_mst.org_unit_id =:orgUnitId " + "	and bill_mst.bill_status_id <> 1 "
				+ "	and ord_mst.order_status = 'A' " + "	and ord_mst.visit_type_id =:visitTypeId "
				+ "	and ord_mst.order_status_id in (:orderStatusId) " + "	and ord_details.service_rendered in (0,1) "
				+ "	and ord_details.service_is_billed =:serviceIsBilled " + "	and ord_details.ord_cancelled = 'N' "
				+ "	and ord_details.is_outsourced = 'N' " + "	and ord_details.status = 'A' "
				+ "	and service_dtls.is_panel = 'Y' " + "  and ord_details.is_order_visible_dept='A' "
				// +" and ord_details.is_from_addservice = 'N' "
				+ "	and ord_details.service_rendering_deptid not in (lab_config.histopathology_id,lab_config.cystopathology_id) "
				+ "	and to_char(ord_details.order_date,'YYYY-MM-DD') >= '"
				+ GlobalCommonDateUtils.getStringDate(searchDto.getFromDate(), "yyyy-MM-dd") + "'"
				+ "	and to_char(ord_details.order_date,'YYYY-MM-DD') <='"
				+ GlobalCommonDateUtils.getStringDate(searchDto.getToDate(), "yyyy-MM-dd") + "'"
				+ ")select count(ct.\"orderId\")  from lab_order_count ct";

		return query;
	}

	public String getLabOrderCountPostBilling(SearchDto searchDto) {
		Date date = TimeZoneComponent.getDateByzone(searchDto.getOrgUnitId());
		if (searchDto.getFromDate() == null) {
			searchDto.setFromDate(date.getTime());
		}
		if (searchDto.getToDate() == null) {
			searchDto.setToDate(date.getTime());
		}

		String query = "with lab_order_count as (select " + "	distinct(ord_mst.order_id) as \"orderId\" " + "from "
				+ "	public.t_order_master ord_mst " + "inner join public.t_order_details ord_details on "
				+ "	ord_details.order_id = ord_mst.order_id " + "inner join public.t_lab_configuration lab_config on "
				+ "	lab_config.organisation_id = :orgId " + "	and lab_config.unit_id = :orgUnitId "
				/*
				 * + "inner join billing.t_billing_details bill_dtls on " +
				 * "	ord_details.order_details_id = bill_dtls.order_details_id " +
				 * "inner join billing.m_billing_master bill_mst on " +
				 * "	bill_mst.billing_master_id = bill_dtls.billing_master_id "
				 */
				/*
				 * + "inner join patient.t_patient_registration pati_mst on " +
				 * "	ord_mst.patient_id = pati_mst.patient_id " +
				 * "inner join public.m_gender_master gen_mst on " +
				 * "	gen_mst.gender_id = pati_mst.gender_id " +
				 * "inner join public.m_prefix_master pre_mst on " +
				 * "	pre_mst.prefix_id = pati_mst.prefix_id " +
				 * "inner join public.m_visit_type_master visit_type_mst on " +
				 * "	visit_type_mst.visit_type_id = ord_mst.visit_type_id " +
				 * "inner join service.t_unit_service_master_details service_dtls on " +
				 * "	ord_details.service_id = service_dtls.service_master_id " +
				 * "	and service_dtls.status = 'A' " +
				 * "inner join lab.m_test_master test_mst on " +
				 * "	ord_details.service_id = test_mst.service_id " +
				 * "	and test_mst.test_status = 'A' " +
				 * "	and test_mst.is_histo_cyto in ('N','S') " +
				 * "left join doctor.m_doctor_master doct_mst on " +
				 * "	enc.doctor_id = doct_mst.doctor_id " +
				 * "left join lab.m_priority_mASter pri_mst on " +
				 * "	ord_mst.priority_id = pri_mst.priority_id " +
				 * "left join doctor.t_doctor_speciality_mapper doct_speciality_mppr on " +
				 * "	 doct_speciality_mppr.doctor_id = doct_mst.doctor_id " +
				 * "	 and doct_speciality_mppr.status = 'A' " +
				 * "left join doctor.m_speciality_master speciality_master on " +
				 * "	speciality_master.speciality_id = enc.dept_id "
				 */
				+ "where " + "	ord_mst.org_id =:orgId " + "	and ord_mst.org_unit_id =:orgUnitId "
				// + " and bill_mst.bill_status_id <> 1 "
				+ "	and ord_mst.order_status = 'A' " + "	and ord_mst.visit_type_id =:visitTypeId "
				+ "	and ord_mst.order_status_id in (:orderStatusId) " + "	and ord_details.service_rendered in (0,1) "
				+ "	and ord_details.service_is_billed =:serviceIsBilled " + "	and ord_details.ord_cancelled = 'N' "
				+ "	and ord_details.is_outsourced = 'N' " + "	and ord_details.status = 'A' "
				// +" and ord_details.is_from_addservice = 'N' "
				+ "  and ord_details.is_order_visible_dept='A' "
				+ "	and ord_details.service_rendering_deptid in (lab_config.biochemistry_id,lab_config.hematology_id,lab_config.microbiology_id,lab_config.serology_id,lab_config.clinical_pathology_id) "
				+ "	and to_char(ord_details.order_date,'YYYY-MM-DD') >= '"
				+ GlobalCommonDateUtils.getStringDate(searchDto.getFromDate(), "yyyy-MM-dd") + "'"
				+ "	and to_char(ord_details.order_date,'YYYY-MM-DD') <='"
				+ GlobalCommonDateUtils.getStringDate(searchDto.getToDate(), "yyyy-MM-dd") + "'" + " union " + "select "
				+ "	distinct(ord_mst.order_id) as \"orderId\" " + "from " + "	public.t_order_master ord_mst "
				+ "inner join public.t_order_details ord_details on " + "	ord_details.order_id = ord_mst.order_id "
				+ "inner join public.t_lab_configuration lab_config on " + "	lab_config.organisation_id = :orgId "
				+ "	and lab_config.unit_id = :orgUnitId "
				/*
				 * + "inner join billing.t_billing_details bill_dtls on " +
				 * "	ord_details.order_details_id = bill_dtls.order_details_id " +
				 * "inner join billing.m_billing_master bill_mst on " +
				 * "	bill_mst.billing_master_id = bill_dtls.billing_master_id "
				 */
				/*
				 * + "inner join patient.t_patient_registration pati_mst on " +
				 * "	ord_mst.patient_id = pati_mst.patient_id " +
				 * "inner join public.m_gender_master gen_mst on " +
				 * "	gen_mst.gender_id = pati_mst.gender_id " +
				 * "inner join public.m_prefix_master pre_mst on " +
				 * "	pre_mst.prefix_id = pati_mst.prefix_id " +
				 * "inner join public.m_visit_type_master visit_type_mst on " +
				 * "	visit_type_mst.visit_type_id = ord_mst.visit_type_id " +
				 * "inner join service.t_unit_service_master_details service_dtls on " +
				 * "	ord_details.service_id = service_dtls.service_master_id " +
				 * "	and service_dtls.status = 'A' " +
				 * "left join doctor.m_doctor_master doct_mst on " +
				 * "	enc.doctor_id = doct_mst.doctor_id " +
				 * "left join lab.m_priority_mASter pri_mst on " +
				 * "	ord_mst.priority_id = pri_mst.priority_id " +
				 * "left join doctor.t_doctor_speciality_mapper doct_speciality_mppr on " +
				 * "	 doct_speciality_mppr.doctor_id = doct_mst.doctor_id " +
				 * "	 and doct_speciality_mppr.status = 'A' " +
				 * "left join doctor.m_speciality_master speciality_master on " +
				 * "	speciality_master.speciality_id = enc.dept_id "
				 */
				+ "where " + "	ord_mst.org_id =:orgId " + "	and ord_mst.org_unit_id =:orgUnitId "
				// + " and bill_mst.bill_status_id <> 1 "
				+ "	and ord_mst.order_status = 'A' " + "	and ord_mst.visit_type_id =:visitTypeId "
				+ "	and ord_mst.order_status_id in (:orderStatusId) " + "	and ord_details.service_rendered in (0,1) "
				+ "	and ord_details.service_is_billed =:serviceIsBilled " + "	and ord_details.ord_cancelled = 'N' "
				+ "	and ord_details.is_outsourced = 'N' " + "	and ord_details.status = 'A' "
				+ "	and service_dtls.is_panel = 'Y' " + "  and ord_details.is_order_visible_dept='A' "
				// +" and ord_details.is_from_addservice = 'N' "
				+ "	and ord_details.service_rendering_deptid not in (lab_config.histopathology_id,lab_config.cystopathology_id) "
				+ "	and to_char(ord_details.order_date,'YYYY-MM-DD') >= '"
				+ GlobalCommonDateUtils.getStringDate(searchDto.getFromDate(), "yyyy-MM-dd") + "'"
				+ "	and to_char(ord_details.order_date,'YYYY-MM-DD') <='"
				+ GlobalCommonDateUtils.getStringDate(searchDto.getToDate(), "yyyy-MM-dd") + "'"
				+ ")select count(ct.\"orderId\")  from lab_order_count ct";

		return query;
	}

	@Override
	public Response getTotalPhlobotomyRecord(SearchDto searchDto) throws ApplicationException {
		try {
			BigInteger phlebotomyTotalRecordCountTwo = null;
			if (searchDto.getIsPreOrPostBilling() == 'Y') {
				phlebotomyTotalRecordCountTwo = (BigInteger) sessionFactory.getCurrentSession()
						.createSQLQuery(getLabOrderCount(searchDto)).setParameter("orgId", searchDto.getOrgId())
						.setParameter("orgUnitId", searchDto.getOrgUnitId()).setParameter("visitTypeId", ICommonConstants.OP)
						.setParameterList("orderStatusId", PENDING_INPROGRESS_RENDER_CANCEL)
						.setParameter("serviceIsBilled", 1).uniqueResult();
				if (phlebotomyTotalRecordCountTwo == null) {
					phlebotomyTotalRecordCountTwo = BigInteger.ZERO;
				}
			} else {
				phlebotomyTotalRecordCountTwo = (BigInteger) sessionFactory.getCurrentSession()
						.createSQLQuery(getLabOrderCountPostBilling(searchDto))
						.setParameter("orgId", searchDto.getOrgId()).setParameter("orgUnitId", searchDto.getOrgUnitId())
						.setParameter("visitTypeId", ICommonConstants.OP)
						.setParameterList("orderStatusId", PENDING_INPROGRESS_RENDER_CANCEL)
						.setParameter("serviceIsBilled", 0).uniqueResult();
				if (phlebotomyTotalRecordCountTwo == null) {
					phlebotomyTotalRecordCountTwo = BigInteger.ZERO;
				}
			}
			return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, phlebotomyTotalRecordCountTwo);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	public Response getPhlebotomyWorklistDetails(Integer orgId, Integer orgUnitId, Integer orderId, Integer deptId,
			Character isPreOrPostBilling) throws ApplicationException {

		List<LabSampleDetailsMasterDto> listLabSampleDetailsMasterDto = null;
		try {
			if (isPreOrPostBilling == 'Y') {
				listLabSampleDetailsMasterDto = sessionFactory.getCurrentSession()
						.getNamedQuery("GET_LAB_ORDER_DETAILS").setParameter("orgId", orgId)
						.setParameter("orgUnitId", orgUnitId).setParameter("orderId", orderId).setParameter("deptId", deptId)
						.setParameter("ordCancelled", 'N')
						// .setParameterList("serviceIsBilleds", Arrays.asList(1))
						.setParameter("sampleStatus", 'A').setParameter("unitStatus", 'A')
						/* .setParameter("serviceRendered", 0) */
						.setParameter("sampleStatusId", SAMPLE_RECOLLECTED).setParameter("patientStatusId", REPAT_PATIRNT)
						.setResultTransformer(Transformers.aliasToBean(LabSampleDetailsMasterDto.class)).list();
				return new Response(SUCCESS, SUCCESS_200, null, listLabSampleDetailsMasterDto, null);
			} else {
				listLabSampleDetailsMasterDto = sessionFactory.getCurrentSession()
						.getNamedQuery("GET_LAB_ORDER_DETAILS").setParameter("orgId", orgId)
						.setParameter("orgUnitId", orgUnitId).setParameter("orderId", orderId).setParameter("deptId", deptId)
						.setParameter("ordCancelled", 'N')
						// .setParameterList("serviceIsBilleds", Arrays.asList(0,1))
						.setParameter("sampleStatus", 'A').setParameter("unitStatus", 'A')
						/* .setParameter("serviceRendered", 0) */
						.setParameter("sampleStatusId", SAMPLE_RECOLLECTED).setParameter("patientStatusId", REPAT_PATIRNT)
						.setResultTransformer(Transformers.aliasToBean(LabSampleDetailsMasterDto.class)).list();
				return new Response(SUCCESS, SUCCESS_200, null, listLabSampleDetailsMasterDto, null);
			}

		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	public Response sampleCollection(LabSampleMasterDto labSampleMasterDto) throws ApplicationException {
		try {
			// Integer labSampleDetialsId = null;
			List<LabSampleDetailsMasterDto> detailsList = new ArrayList<>();
			List<Integer> orderDetailsIdsList = new LinkedList<Integer>();
			LabSampleMaster labSampleMaster = mapper.map(labSampleMasterDto, LabSampleMaster.class,
					"LabSampleMasterDtoTOLabSampleMaster");

			LabSampleMaster labSampleMst = save(labSampleMaster);
			if (labSampleMst != null) {
				for (Iterator iterator = labSampleMst.getListLabSampleDetailsMaster().iterator(); iterator.hasNext();) {

					LabSampleDetailsMaster labSampleDetailsMaster = (LabSampleDetailsMaster) iterator.next();
					labSampleDetailsMaster.setSampleStatusId(labSampleDetailsMaster.getSampleStatusId() != null
							? labSampleDetailsMaster.getSampleStatusId()
							: SAMPLE_COLLECTED);
					labSampleDetailsMaster.setSampleBarcode(labSampleMst.getSampleId());
					labSampleDetailsMaster.setLabSampleId(labSampleMst.getLabSampleId());
					labSampleDetailsMaster.setSampleRecollectFlag('N');
					labSampleDetailsMaster.setSamplePendingCount(labSampleDetailsMaster.getSampleReqCount());
					labSampleDetailsMaster.setGenderId(labSampleMasterDto.getGenderId());
					labSampleDetailsMaster.setSampleCollectionBy(labSampleMaster.getCreatedBy());
					labSampleDetailsMaster.setPerformedUnit(labSampleMst.getOrgUnitId());
					labSampleDetailsMaster.setAssignUnit(labSampleDetailsMaster.getAssignUnit());
					labSampleDetailsMaster.setPerformedUnit(labSampleMst.getOrgUnitId());
					labSampleDetailsMaster.setPerformTestId(labSampleDetailsMaster.getPerformTestId());
					labSampleDetailsMaster.setUpdatedBy(
							labSampleDetailsMaster.getUpdatedBy() != null ? labSampleDetailsMaster.getUpdatedBy()
									: labSampleDetailsMaster.getCreatedBy());
					labSampleDetailsMaster.setUpdatedDate(
							labSampleDetailsMaster.getUpdatedDate() != null ? labSampleDetailsMaster.getUpdatedDate()
									: labSampleDetailsMaster.getCreatedDate());
					labSampleDetailsMaster.setIsReportRecall(false);
					Response<LabSampleDetailsMaster, Integer> labSampleDetailsRes = iLabCentralReceivingDao
							.samplCollection(labSampleDetailsMaster);
					if (labSampleDetailsRes.getStatus().equals(SUCCESS)) {
						LabSampleDetailsMasterDto dto = new LabSampleDetailsMasterDto();

						// labSampleDetialsId = labSampleDetailsRes.getObject().getLabSampleDtlsId();
						orderDetailsIdsList.add(labSampleDetailsMaster.getOrderDetailsId());
						dto.setSrfId(labSampleDetailsMaster.getSrfId());
						dto.setLabSampleDtlsId(labSampleDetailsRes.getObject().getLabSampleDtlsId());
						dto.setOrderDetailsId(labSampleDetailsMaster.getOrderDetailsId());
						detailsList.add(dto);
					} else {
						return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
					}
				}
				return new Response(SUCCESS, SUCCESS_200, SAMPLE_COLLECT_SUCC, orderDetailsIdsList, labSampleMaster,
						detailsList);
			}
			return new Response(SUCCESS, SUCCESS_200, SAMPLE_COLLECT_SUCC, orderDetailsIdsList, labSampleMaster,
					detailsList);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	public Response getCollectedSample(Integer orgId, Integer orgUnitId, Integer deptId, Integer offset,
			Integer recordPerPage) throws ApplicationException {
		List<CollectedSampleDto> listCollectedSampleDto = null;
		Date date = TimeZoneComponent.getDateByzone(orgUnitId);
		try {
			String pattern = "YYYY-MM-DD";
			String formattedDate = new SimpleDateFormat(pattern).format(date);
			listCollectedSampleDto = sessionFactory.getCurrentSession().getNamedQuery("GET_COLLECTED_SAMPLE_LIST")
					.setParameter("orgId", orgId).setParameter("orgUnitId", orgUnitId).setParameter("deptId", deptId)
					.setParameter("visitTypeId", ICommonConstants.OP).setParameterList("sampleStatusIds", COLLECTED_SAMPLE_STATUS)
					.setParameter("fromDate", formattedDate).setParameter("toDate", formattedDate)
					.setFirstResult(offset != null ? offset : 0)
					.setMaxResults(recordPerPage != null ? recordPerPage : 10)
					.setResultTransformer(Transformers.aliasToBean(CollectedSampleDto.class)).list();
			return new Response(SUCCESS, SUCCESS_200, null, listCollectedSampleDto, null);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	public Response getTotalSendToCrRecord(SearchDto searchDto) throws ApplicationException {
		try {
			String query = sessionFactory.getCurrentSession().getNamedQuery("GET_COUNT_FROM_SEND_TO_CR_LIST")
					.getQueryString();
			StringBuffer sb = new StringBuffer(query);
			if (searchDto.getFromDt() != null) {
				sb.append(" AND to_char(order_master.created_date, 'YYYY-MM-DD') >= :fromDt");
			}

			if (searchDto.getToDt() != null) {
				sb.append(" AND to_char(order_master.created_date, 'YYYY-MM-DD') <= :toDt");
			}

			/*
			 * if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size() >
			 * 0) sb.append(" AND visit_type_master.visit_type_id IN (" +
			 * StringCommonMethods.convertToStringJoiner(searchDto.getVisitTypes()) + ")");
			 */

			Query sqlQuery = sessionFactory.getCurrentSession()
					.createSQLQuery(sb.toString());
			sqlQuery.setParameter("orgId", searchDto.getOrgId())
					.setParameterList("sampleStatusIds", COLLECTED_SAMPLE_STATUS)
					.setParameter("deptId", searchDto.getDeptId())
					.setParameter("visitTypeId", ICommonConstants.OP)
					.setParameter("orgUnitId", searchDto.getOrgUnitId()).uniqueResult();


			if (searchDto.getFromDt() != null) {
				sqlQuery.setParameter("fromDt", searchDto.getFromDt());
			}
			if (searchDto.getToDt() != null) {
				sqlQuery.setParameter("toDt", searchDto.getToDt());
			}

			BigInteger incubationObservationListCount = (BigInteger) sqlQuery.uniqueResult();
			/*
			 * //Date date = TimeZoneComponent.getDateByzone(unitId); BigInteger
			 * getTotalSendToCrRecordCount = (BigInteger) sessionFactory.getCurrentSession()
			 * .getNamedQuery("GET_COUNT_FROM_SEND_TO_CR_LIST") .setParameter("orgId",
			 * searchDto.getOrgId()) .setParameter("deptId", searchDto.getDeptId())
			 * .setParameterList("sampleStatusIds", COLLECTED_SAMPLE_STATUS)
			 * //.setParameter("fromDate", date) //.setParameter("toDate",
			 * GlobalCommonDateUtils.getNextDateInDate(date.getTime()))
			 * .setParameter("visitTypeId", OP) .setParameter("orgUnitId",
			 * searchDto.getOrgUnitId()).uniqueResult();
			 */
			if (incubationObservationListCount.compareTo(BigInteger.ZERO) == 1) {
				return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, incubationObservationListCount);
			} else {
				return new Response(ERROR, ERR_500, SUCCESS_200_MESSAGE, null, BigInteger.ZERO);
			}

		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	public Response sendToCr(List<LabSampleDetailsMasterDto> listLabSampleDetailsMasterDto)
			throws ApplicationException {
		try {
			Date date = TimeZoneComponent
					.getDateByzone(listLabSampleDetailsMasterDto != null && listLabSampleDetailsMasterDto.size() > 0
							? listLabSampleDetailsMasterDto.getFirst().getOrgUnitId()
							: null);
			Integer result = null;
			for (Iterator iterator = listLabSampleDetailsMasterDto.iterator(); iterator.hasNext();) {
				LabSampleDetailsMasterDto labSampleDetailsMasterDto = (LabSampleDetailsMasterDto) iterator.next();
				LabSampleDetailsMaster labSampleDetailsMaster = mapper.map(labSampleDetailsMasterDto,
						LabSampleDetailsMaster.class, "LabSampleDetailsMasterDtoTOLabSampleDetailsMaster");
				labSampleDetailsMaster.setSampleSendtocrDatetime(date.getTime());
				result = sessionFactory.getCurrentSession().getNamedQuery("SEND_SAMPLE_TO_CENTRAL_RECEIVING")
						.setParameter("sampleStatusId", SAMPLE_SEND_TO_CR)
						// .setParameter("assignUnit", labSampleDetailsMasterDto.getAssignUnit())
						.setParameter("performedUnit", labSampleDetailsMasterDto.getOrgUnitId())
						// .setParameter("performTestId", labSampleDetailsMasterDto.getPerformTestId())
						.setParameter("labSampleDtlsId", labSampleDetailsMaster.getLabSampleDtlsId())
						.setParameter("sampleSendToCrBy", labSampleDetailsMaster.getUpdatedBy())
						.setParameter("sampleSendtocrDatetime", date)
						.setParameter("updatedBy", labSampleDetailsMaster.getUpdatedBy())
						.setParameter("updatedDate", date).executeUpdate();

			}
			if (result > 0) {
				return new Response(SUCCESS, SUCCESS_200, SAMPLE_SENDTOCR_SUCC, null, null);
			} else {
				return new Response(ERROR, ERR_500, SAMPLE_SENDTOCR_FAIL, null, null);
			}

		} catch (HibernateException er) {
			logger.error("Exception", er);
			throw er;
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	public Response updateOrderStatus(Integer orderId, Integer orderStatusId) throws ApplicationException {
		try {
			Integer orderUpdateSucess = (Integer) sessionFactory.getCurrentSession()
					.getNamedQuery("LAB_UPDATE_ORDER_MASTER_STATUS").setParameter("orderStatusId", orderStatusId)
					.setParameter("orderId", orderId).executeUpdate();
			return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, orderUpdateSucess);

		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	public Response updateServiceRenderStatus(Integer orderDetailsId, Integer orgUnitId) throws ApplicationException {
		try {
			Date date = TimeZoneComponent.getDateByzone(orgUnitId);
			long timestamp = date.getTime();
			Integer orderDetailsUpdateSucess = (Integer) sessionFactory.getCurrentSession()
					.getNamedQuery("LAB_UPDATE_ORDER_DETAILS_SERVICE")
					.setParameter("orderDetailsId", orderDetailsId)
					.setParameter("date", timestamp)
					.setParameter("serviceRendered", 1).executeUpdate();
			return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, orderDetailsUpdateSucess);

		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	public Response v1GetPhlebotomyWorklistDetails(Integer orgId, Integer orgUnitId, Integer orderId)
			throws ApplicationException {
		List<LabSampleDetailsMasterDto> listLabSampleDetailsMasterDto = null;
		try {
			listLabSampleDetailsMasterDto = sessionFactory.getCurrentSession()
					.getNamedQuery("V1_GET_PHLEBOTOMY_WORKLIST_DETAILS").setParameter("orgId", orgId)
					.setParameter("orgUnitId", orgUnitId).setParameter("orderId", orderId)
					.setResultTransformer(Transformers.aliasToBean(LabSampleDetailsMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_200, null, listLabSampleDetailsMasterDto, null);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	public Response searchPhlebotomyWorklistPatient(SearchCommonDto searchCommonDto) throws ApplicationException {
		List<CommonAutoCompleteDto> patientList = null;
		try {
			patientList = (List<CommonAutoCompleteDto>) sessionFactory.getCurrentSession()
					.getNamedQuery("LAB_PATIENT_SEARCH").setParameter("orgId", searchCommonDto.getOrgId())
					// .setParameter("orgUnitId", searchCommonDto.getOrgUnitId())
					.setParameter("searchKeyword",
							"%" + searchCommonDto.getSearchKeyword().trim().toLowerCase().trim() + "%")
					.setResultTransformer(Transformers.aliasToBean(CommonAutoCompleteDto.class)).list();
			return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, patientList, null);

		} catch (Exception e) {
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	public Response getFilteredPhlebotomyWorklistPatient(SearchDto searchDto) throws ApplicationException {
		try {

			Response resp = getSpecialityAndSubSpecialityListByUserIdByUnitId(searchDto);
			List<Integer> deptIds = new ArrayList<>();
			List<Integer> subDeptIds = new ArrayList<>();
			List<Integer> visitTypeIds = new ArrayList<>();
			List<SearchDto> specialityList = new ArrayList<>();
			List<SearchDto> subSpecialityList = new ArrayList<>();
			if (resp.getListObject() != NULL && resp.getListObject().size() > 0) {
				specialityList = resp.getListObject();
				if (resp.getListObject1() != null) {
					subSpecialityList = resp.getListObject1();
				}
			}
			if (specialityList != null && specialityList.size() > 0) {
				for (Iterator iterator = specialityList.iterator(); iterator.hasNext();) {
					SearchDto dto = (SearchDto) iterator.next();
					deptIds.add(dto.getSpecialityId());
				}
			} else {
				deptIds.add(searchDto.getDeptId());
			}

			if (subSpecialityList != null) {
				for (Iterator iterator = subSpecialityList.iterator(); iterator.hasNext();) {
					SearchDto dto2 = (SearchDto) iterator.next();
					subDeptIds.add(dto2.getSubSpecialityMasterId());
				}
			}
			searchDto.setSubDeptIds(subDeptIds);

			if (searchDto.getIsPreOrPostBilling() == 'Y') {
				List<Integer> orderStatusIds = (searchDto.getOrderStatusId() != null
						&& searchDto.getOrderStatusId() > 0) ? Arrays.asList(searchDto.getOrderStatusId())
								: PENDING_INPROGRESS_RENDER_CANCEL;
				StringBuilder sb = new StringBuilder();
				sb.append(" select " + " 	distinct(ord_mst.order_id) as \"orderId\", "
						+ " 	ord_mst.org_id as \"orgId\", " + "    bill_mst.bill_number as \"billNumber\", "
						/*+ " (select count(*) from t_order_details caetod where caetod.order_id = ord_mst.order_id and  caetod.is_dummy_service = false ) as \"realServiceCount\", "*/
						+ "    bill_mst.billing_master_id as \"billingMasterId\", "
						+ " 	ord_mst.org_unit_id as \"orgUnitId\", "
						+ " 	ord_mst.order_status_id as \"orderStatusId\", "
						+ " 	ord_mst.visit_type_id as \"visitTypeId\", "
						+ " 	ord_mst.neuron_plus_url as \"neuronPlusUrl\", "
						+ " 	visit_type_mst.visit_type_code as \"visitType\", "
						+ " 	ord_mst.home_collection as \"isHomeCollection\", "
						+ " 	ord_mst.ord_doctor_id as \"doctorId\", " + " 	ord_mst.patient_id as \"patientId\", "
						+ " 	ord_mst.admission_id as \"admissionId\", "
						+ " 	ord_mst.encounter_id as \"encounterId\", " + " 	ord_mst.created_by as \"createdBy\", "
						+ " 	ord_mst.created_date as \"createdDate\", "
						/*+ "		(case when (select count(*) from t_order_details caetod where caetod.order_id = ord_mst.order_id and caetod.priority_id =2 ) > 0 then 2 else 1 end) as \"priorityId\","*/
						+ "    to_char(ord_mst.created_date, 'DD/MM/YYYY HH24:MI:SS') AS \"createdDateString\" , "
						+ "     pre_mst.prefix_id as \"prefixId\", "
						+ " 	pati_mst.uhid_number as uhid, " + " 	pati_mst.is_vip as \"isVip\", " + " 	(case "
						+ " 		when pati_mst.is_vip = 'Y' then coalesce(pati_mst.alise_name, pati_mst.first_name) || ' ' || calculate_age(pati_mst.dob) "
						+ " || '/' || gen_mst.gender_code "
						+ " 		else pre_mst.prefix_code || ' ' || pati_mst.first_name || ' ' || coalesce(pati_mst.middle_name) || ' ' || pati_mst.last_name || ' ' || "
						+ "  calculate_age(pati_mst.dob) || '/' || gen_mst.gender_code "
						+ " 	end ) as \"patientDetails\", " + " 	(case "
						+ " 		when pati_mst.is_vip = 'Y' then coalesce(pati_mst.alise_name, pati_mst.first_name) || ' ' || extract( year "
						+ " 	from " + " 		age( now(), pati_mst.dob ) )|| '/' || gen_mst.gender_code "
						+ " 		else pati_mst.first_name || ' ' || coalesce(pati_mst.middle_name) || ' ' || pati_mst.last_name "
						+ " 	end ) as \"firstName\", " + " 	gen_mst.gender_id as \"genderId\", "
						+ " 	gen_mst.gender_code as \"genderCode\", "
						+ " 	to_char(pati_mst.dob, 'DD/MM/YYYY') as \"birthDate\", "
						//+ " 	pdl.address as \"address\", " + " 	cm.city_name as \"cityName\", "
						+ " 	pati_mst.email as \"email\", "
						+ " 	to_char(ord_mst.order_date, 'yyyy-mm-dd') as \"orderDateString\", "
						+ " 	to_char(ord_mst.order_date, 'HH24:MI') as \"orderDateTime\", "
						+ " 	'Dr.' || doct_mst.first_name || ' ' || doct_mst.last_name || ' (' || speciality_master.speciality_name || ')' as \"doctorDetails\", "
						+ " 	doct_mst.first_name || ' ' || doct_mst.last_name || ' (' || speciality_master.speciality_name || ')' as \"doctorName\", "
						/*+ " 	concat(extract(year from AGE(CURRENT_TIMESTAMP, pati_mst.dob))) as \"age\", "*/
						+ " 	''  as \"age\", "
						+ " 	enc.encounter_type_id as \"encounterTypeId\", "
						+ " 	enc.pat_history as \"patHistory\", " + " 	pati_mst.image_path as \"imagePath\", "
						+ " 	coalesce(enc.is_critical, 'N') as \"isCritical\", "
						+ " 	(case " + " 		when ord_mst.encounter_id > 0 then (enc.encounter_number) "
						+ " 		else (ta.admission_number) " + " 	end ) as \"encounterNumber\", "
						+ " 	ord_mst.order_no as \"orderNo\" " + " from " + " 	public.t_order_master ord_mst "
						+ " inner join public.t_order_details ord_details on "
						+ " 	ord_details.order_id = ord_mst.order_id "
						+ " inner join patient.t_patient_registration pati_mst on "
						+ " 	ord_mst.patient_id = pati_mst.patient_id "
						/*+ " left join patient.t_patient_details pdl on " + " 	pati_mst.patient_id = pdl.patient_id "
						+ " left join public.m_city_master cm on " + " 	cm.city_id = pdl.city_id "*/
						+ " inner join public.m_gender_master gen_mst on " + " 	gen_mst.gender_id = pati_mst.gender_id "
						+ " inner join public.m_prefix_master pre_mst on " + " 	pre_mst.prefix_id = pati_mst.prefix_id "
						+ " inner join public.m_visit_type_master visit_type_mst on "
						+ " 	visit_type_mst.visit_type_id = ord_mst.visit_type_id "
						/*+ " inner join service.t_unit_service_master_details service_dtls on "
						+ " 	ord_details.service_id = service_dtls.service_master_id "
						+ " 	and service_dtls.status = 'A' " + " left join service.m_service_master service on "
						+ " 	service.service_master_id = ord_details.service_id "*/
						+ " inner join public.t_lab_configuration lab_config on "
						+ " 	lab_config.organisation_id = :orgId " + " 	and lab_config.unit_id = :orgUnitId "
						+ " inner join lab.m_test_master test_mst on "
						+ " 	ord_details.service_id = test_mst.service_id " + " 	and test_mst.test_status = 'A' "
					//	+ " 	and test_mst.is_histo_cyto in ('N', 'S') "
						+ " left join billing.t_billing_details bill_dtls on "
						+ " 	ord_details.order_details_id = bill_dtls.order_details_id "
						+ " left join billing.m_billing_master bill_mst on "
						+ " 	bill_mst.billing_master_id = bill_dtls.billing_master_id "
						+ " left join opd.t_encounter_master enc on " + " 	bill_mst.encounter_id = enc.encounter_id "
						+ " left join doctor.m_doctor_master doct_mst on "
						+ " 	ord_mst.ord_doctor_id = doct_mst.doctor_id " + " left join adt.t_admission ta on "
						+ " 	ta.admission_id = ord_mst.admission_id " + " 	and ta.status = 'A' "
						+ " left join doctor.m_speciality_master speciality_master on "
						+ " 	speciality_master.speciality_id = ord_mst.ord_doc_spl_id "
						+ " left join billing.t_credit_note_detail tcnd on "
						+ " 	tcnd.order_detail_id = ord_details.order_details_id " + " 	and tcnd.status = 'A' "
						+ " 	and tcnd.cancel_or_discount = 1 ");

				if (searchDto.getSubDeptIds() != null && searchDto.getSubDeptIds().size() > 0) {
					sb.append(" left join (select " + "		count(*) c , service_id "
							+ "		from lab.m_test_master test_mst "
							+ "		left join service.m_service_master service on"
							+ "		service.service_master_id = test_mst.service_id "
							+ "		where  test_mst.test_status = 'A' "
							+ "		and service.sub_speciality_id In (:subDeptIds) group by service_id "
							+ "		) m on m.service_id = ord_details.service_id and m.c  > 0 ");
				}

				sb.append(" where " + " 	ord_mst.org_id =:orgId "
						+ "    and (bill_mst.bill_cancelled = 'N' or bill_mst.bill_cancelled is null)"
						+ " 	and ord_mst.org_unit_id =:orgUnitId " + " 	and ord_mst.order_status = 'A' "
						+ " 	and ord_mst.order_status_id in (:orderStatusId) "
						+ " 	and ord_details.service_rendered in (0, 1) " + " 	and " + " 	(case "
						+ " 		when ord_mst.visit_type_id = 1 then ord_details.service_is_billed = 1 "
						+ " 		else ord_details.service_is_billed in(0, 1) " + " 	end ) " + " 	and "
						+ " 	(case "
						+ " 		when ord_mst.visit_type_id = 1 then ( bill_mst.bill_status_id is not null  and  bill_mst.bill_status_id in( 2, 3)) "
						+ " 		else (bill_mst.bill_status_id is null "
						+ " 			or bill_mst.bill_status_id in(1, 2, 3)) " + " 	end ) "
						+ " 	and ord_details.ord_cancelled = 'N' " + " 	and ord_details.is_outsourced = 'N' "
						+ " 	and (ord_details.status = 'A' and tcnd.credit_note_detail_id is null) "
						+ " 	and ord_details.is_order_visible_dept = 'A' "
						+ " 	and lab_config.lab_speciality_id IN (:deptIds)");
						//+ deptIds.toString().replaceAll("\\[", "").replaceAll("\\]", "") + ")");
				if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0) {
					sb.append(" AND pati_mst.patient_id  =:patientId ");
				}
				if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size() > 0) {
					visitTypeIds = searchDto.getVisitTypes().stream()
							.map(obj -> obj.getId())
							.collect(Collectors.toList());
					sb.append(" AND visit_type_mst.visit_type_id  IN (:visitTypeIds) ");
				}
				if (searchDto.getFromDt() != null) {
					sb.append("	and to_char(ord_details.order_date,'YYYY-MM-DD') >=:fromDt ");
				}
				if (searchDto.getToDt() != null) {
					sb.append("	and to_char(ord_details.order_date,'YYYY-MM-DD')<=:toDt ");
				}

				if (searchDto.getUhidNumber() != "") {
					sb.append(" AND lower(pati_mst.uhid_number) LIKE :uhidNumber");
				}
/*
				sb.append(" union " + " select " + " 	distinct(ord_mst.order_id) as \"orderId\", "
						+ " 	ord_mst.org_id as \"orgId\", " + "    bill_mst.bill_number as \"billNumber\", "
						+ " (select count(*) from t_order_details caetod where caetod.order_id = ord_mst.order_id and  caetod.is_dummy_service = false ) as \"realServiceCount\", "
						+ "    bill_mst.billing_master_id as \"billingMasterId\", "
						+ " 	ord_mst.org_unit_id as \"orgUnitId\", "
						+ " 	ord_mst.order_status_id as \"orderStatusId\", "
						+ " 	ord_mst.visit_type_id as \"visitTypeId\", "
						+ " 	ord_mst.neuron_plus_url as \"neuronPlusUrl\", "
						+ " 	visit_type_mst.visit_type_code as \"visitType\", "
						+ " 	ord_mst.home_collection as \"isHomeCollection\", "
						+ " 	ord_mst.ord_doctor_id as \"doctorId\", " + " 	ord_mst.patient_id as \"patientId\", "
						+ " 	ord_mst.admission_id as \"admissionId\", "
						+ " 	ord_mst.encounter_id as \"encounterId\", " + " 	ord_mst.created_by as \"createdBy\", "
						+ " 	ord_mst.created_date as \"createdDate\", "
						+ "		(case when (select count(*) from t_order_details caetod where caetod.order_id = ord_mst.order_id and caetod.priority_id =2 ) > 0 then 2 else 1 end) as \"priorityId\","
						+ "    to_char(ord_mst.created_date, 'DD/MM/YYYY HH24:MI:SS') AS \"createdDateString\" , "
						+ "     pre_mst.prefix_id as \"prefixId\", "
						+ " 	pati_mst.uhid_number as uhid, " + " 	pati_mst.is_vip as \"isVip\", " + " 	(case "
						+ " 		when pati_mst.is_vip = 'Y' then coalesce(pati_mst.alise_name, pati_mst.first_name) || ' ' || extract( year "
						+ " 	from " + " 		age( now(), pati_mst.dob ) )|| '/' || gen_mst.gender_code "
						+ " 		else pre_mst.prefix_code || ' ' || pati_mst.first_name || ' ' || coalesce(pati_mst.middle_name) || ' ' || pati_mst.last_name || ' ' || extract( year "
						+ " 	from " + " 		age( now(), pati_mst.dob ) )|| '/' || gen_mst.gender_code "
						+ " 	end ) as \"patientDetails\", " + " 	(case "
						+ " 		when pati_mst.is_vip = 'Y' then coalesce(pati_mst.alise_name, pati_mst.first_name) || ' ' || extract( year "
						+ " 	from " + " 		age( now(), pati_mst.dob ) )|| '/' || gen_mst.gender_code "
						+ " 		else pati_mst.first_name || ' ' || coalesce(pati_mst.middle_name) || ' ' || pati_mst.last_name "
						+ " 	end ) as \"firstName\", " + " 	gen_mst.gender_id as \"genderId\", "
						+ " 	gen_mst.gender_code as \"genderCode\", "
						+ " 	to_char(pati_mst.dob, 'DD/MM/YYYY') as \"birthDate\", "
						//+ " 	pdl.address as \"address\", " + " 	cm.city_name as \"cityName\", "
						+ " 	pati_mst.email as \"email\", "
						+ " 	to_char(ord_mst.order_date, 'yyyy-mm-dd') as \"orderDateString\", "
						+ " 	to_char(ord_mst.order_date, 'HH24:MI') as \"orderDateTime\", "

						+ " 	'Dr.' || doct_mst.first_name || ' ' || doct_mst.last_name || ' (' || speciality_master.speciality_name || ')' as \"doctorDetails\", "
						+ " 	doct_mst.first_name || ' ' || doct_mst.last_name || ' (' || speciality_master.speciality_name || ')' as \"doctorName\", "

						+ " 	concat(extract(year from AGE(CURRENT_TIMESTAMP, pati_mst.dob))) as \"age\", "
						+ " 	enc.encounter_type_id as \"encounterTypeId\", "
						+ " 	enc.pat_history as \"patHistory\", " + " 	pati_mst.image_path as \"imagePath\", "
						+ " 	coalesce(enc.is_critical, 'N') as \"isCritical\", "
						+ " 	(case " + " 		when ord_mst.encounter_id > 0 then (enc.encounter_number) "
						+ " 		else (ta.admission_number) " + " 	end ) as \"encounterNumber\", "
						+ " 	ord_mst.order_no as \"orderNo\" " + " from " + " 	public.t_order_master ord_mst "
						+ " inner join public.t_order_details ord_details on "
						+ " 	ord_details.order_id = ord_mst.order_id "
						+ " inner join patient.t_patient_registration pati_mst on "
						+ " 	ord_mst.patient_id = pati_mst.patient_id "
						+ " left join patient.t_patient_details pdl on " + " 	pati_mst.patient_id = pdl.patient_id "
						+ " left join public.m_city_master cm on " + " 	cm.city_id = pdl.city_id "
						+ " inner join public.m_gender_master gen_mst on " + " 	gen_mst.gender_id = pati_mst.gender_id "
						+ " inner join public.m_prefix_master pre_mst on " + " 	pre_mst.prefix_id = pati_mst.prefix_id "
						+ " inner join public.m_visit_type_master visit_type_mst on "
						+ " 	visit_type_mst.visit_type_id = ord_mst.visit_type_id "
						+ " inner join service.t_unit_service_master_details service_dtls on "
						+ " 	ord_details.service_id = service_dtls.service_master_id "
						+ " 	and service_dtls.status = 'A' " 
							+ " left join service.m_service_master service on "
						+ " 	service.service_master_id = ord_details.service_id "
						+ " inner join public.t_lab_configuration lab_config on "
						+ " 	lab_config.organisation_id = :orgId " + " 	and lab_config.unit_id = :orgUnitId "
						+ " left join billing.t_billing_details bill_dtls on "
						+ " 	ord_details.order_details_id = bill_dtls.order_details_id "
						+ " left join billing.m_billing_master bill_mst on "
						+ " 	bill_mst.billing_master_id = bill_dtls.billing_master_id "
						+ " left join opd.t_encounter_master enc on " + " 	bill_mst.encounter_id = enc.encounter_id "
						+ " left join doctor.m_doctor_master doct_mst on "
						+ " 	ord_mst.ord_doctor_id = doct_mst.doctor_id " + " left join adt.t_admission ta on "
						+ " 	ta.admission_id = ord_mst.admission_id " + " 	and ta.status = 'A' "
						+ " left join doctor.m_speciality_master speciality_master on "
						+ " 	speciality_master.speciality_id = ord_mst.ord_doc_spl_id "
						+ " left join billing.t_credit_note_detail tcnd on "
						+ " 	tcnd.order_detail_id = ord_details.order_details_id " + " 	and tcnd.status = 'A' "
						+ " 	and tcnd.cancel_or_discount = 1	 ");

				if (searchDto.getSubDeptIds() != null && searchDto.getSubDeptIds().size() > 0) {
					sb.append("  left join (select " + 
							"		count(*) c , service_id  " + 
							"	from " + 
							"		lab.t_panel_master test_mst " + 
							"	left join service.m_service_master service on " + 
							"		service.service_master_id = test_mst.service_id " + 
							"		where  test_mst.status = 'A'  " + 
							 "		and service.sub_speciality_id In ("+ searchDto.getSubDeptIds().toString().replaceAll("\\[", "").replaceAll("\\]", "")+ ") group by service_id ) n on n.service_id = ord_details.service_id and n.c  > 0 ");
				}
				sb.append(" where " + " 	ord_mst.org_id =:orgId "
						+ "    and (bill_mst.bill_cancelled = 'N' or bill_mst.bill_cancelled is null)"
						+ " 	and ord_mst.org_unit_id =:orgUnitId " + " 	and ord_mst.order_status = 'A' "
						+ " 	and ord_mst.order_status_id in (:orderStatusId) "
						+ " 	and ord_details.service_rendered in (0, 1) " + " 	and " + " 	(case "
						+ " 		when ord_mst.visit_type_id = 1 then ord_details.service_is_billed = 1 "
						+ " 		else ord_details.service_is_billed in(0, 1) " + " 	end ) " + " 	and "
						+ " 	(case "
						+ " 		when ord_mst.visit_type_id = 1 then ( bill_mst.bill_status_id is not null  and  bill_mst.bill_status_id in( 2, 3)) "
						+ " 		else (bill_mst.bill_status_id is null "
						+ " 			or bill_mst.bill_status_id in(1, 2, 3)) " + " 	end ) "
						+ " 	and ord_details.ord_cancelled = 'N' " + " 	and ord_details.is_outsourced = 'N' "
						+ " 	and (ord_details.status = 'A' and tcnd.credit_note_detail_id is null) "
						+ " 	and ord_details.is_order_visible_dept = 'A' " + " 	and service_dtls.is_panel = 'Y' "
						+ " 	and lab_config.lab_speciality_id IN ("
						+ deptIds.toString().replaceAll("\\[", "").replaceAll("\\]", "") + ")");
				if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0) {
					sb.append(" AND pati_mst.patient_id = " + searchDto.getPatientId());
				}
				if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size() > 0) {
					sb.append(" AND visit_type_mst.visit_type_id IN ("
							+ StringCommonMethods.convertToStringJoiner(searchDto.getVisitTypes()) + ")");
				}
				if (searchDto.getFromDt() != null) {
					sb.append("	and to_char(ord_details.order_date,'YYYY-MM-DD') >= '" + searchDto.getFromDt() + "'");
				}
				if (searchDto.getToDt() != null) {
					sb.append("	and to_char(ord_details.order_date,'YYYY-MM-DD') <= '" + searchDto.getToDt() + "'");
				}*/
			
				if (searchDto.getLabSortingId() != null) {
					if (searchDto.getLabSortingId() == 1) {
						sb.append(" ORDER BY  \"isVip\" desc ,  \"patientId\" , \"createdDate\"  DESC ");
					} else if (searchDto.getLabSortingId() == 2) {
						sb.append(" ORDER BY  \"isVip\" desc ,  \"orderId\" DESC ");
					} else if (searchDto.getLabSortingId() == 4) {
						sb.append(" ORDER BY \"isVip\" desc , \"createdDate\" DESC ");
					}
				} else {
					sb.append(" order by \"isCritical\" , \"isVip\" desc, \"createdDate\" DESC	");
				}

				int count = 0;

				Query createdQuery = sessionFactory.getCurrentSession().createSQLQuery(sb.toString());

				 createdQuery.setParameter("orgId", searchDto.getOrgId())
						.setParameter("orgUnitId", searchDto.getOrgUnitId())
						.setParameterList("orderStatusId", orderStatusIds)
						.setFirstResult(searchDto.getOffset() != null ? searchDto.getOffset() : 0)
						.setMaxResults(searchDto.getRecordPerPage() != null ? searchDto.getRecordPerPage() : 50);

				if (searchDto.getSubDeptIds() != null && !searchDto.getSubDeptIds().isEmpty()) {
					createdQuery.setParameterList("subDeptIds", searchDto.getSubDeptIds());
				}

				createdQuery.setParameterList("deptIds", deptIds);

				if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0) {
					createdQuery.setParameter("patientId", searchDto.getPatientId());
				}

				if (searchDto.getVisitTypes() != null && !searchDto.getVisitTypes().isEmpty()) {
					createdQuery.setParameterList("visitTypeIds", visitTypeIds);
				}

				if (searchDto.getFromDt() != null) {
					createdQuery.setParameter("fromDt", searchDto.getFromDt());
				}

				if (searchDto.getToDt() != null) {
					createdQuery.setParameter("toDt", searchDto.getToDt());
				}

				if (searchDto.getUhidNumber() != null && !searchDto.getUhidNumber().isEmpty()) {
					createdQuery.setParameter("uhidNumber", "%" + searchDto.getUhidNumber().toLowerCase() + "%");
				}

				List<LabSampleMasterDto> listLabSampleMasterDto=createdQuery.setResultTransformer(Transformers.aliasToBean(LabSampleMasterDto.class)).list();

				if (listLabSampleMasterDto.isEmpty()) {
					return new Response(SUCCESS, SUCCESS_200_MESSAGE, "No Records Found.", listLabSampleMasterDto,
							count);
				} else {
					if (searchDto.getIsCountRequired() != null && searchDto.getIsCountRequired()) {
						List<LabSampleMasterDto> listLabSampleMasterListForCount = this
								.getPhlebotomyPreBillingCountList(searchDto);
						if (listLabSampleMasterListForCount != null && listLabSampleMasterListForCount.size() > 0) {
							count = listLabSampleMasterListForCount.size();
						}
					}
					return new Response(SUCCESS, SUCCESS_200_MESSAGE, null, listLabSampleMasterDto, count);
				}
			} else {

				List<Integer> orderStatusIds = (searchDto.getOrderStatusId() != null
						&& searchDto.getOrderStatusId() > 0) ? Arrays.asList(searchDto.getOrderStatusId())
								: PENDING_INPROGRESS_RENDER_CANCEL;
				StringBuilder sb = new StringBuilder();
				sb.append("select " + "	distinct(ord_mst.order_id) as \"orderId\", "
						+ "	ord_mst.org_id as \"orgId\", " + "    bill_mst.bill_number as \"billNumber\", "
						/*+ " (select count(*) from t_order_details caetod where caetod.order_id = ord_mst.order_id and  caetod.is_dummy_service = false ) as \"realServiceCount\", "*/
						+ "    bill_mst.billing_master_id as \"billingMasterId\", "
						+ "	ord_mst.org_unit_id as \"orgUnitId\", " + "	ord_mst.order_status_id as \"orderStatusId\", "
						+ "	ord_mst.visit_type_id as \"visitTypeId\", "
						+ "	visit_type_mst.visit_type_code as \"visitType\", "
						+ " ord_mst.home_collection as \"isHomeCollection\", "
						+ "	ord_mst.ord_doctor_id as \"doctorId\", " + "	ord_mst.patient_id as \"patientId\", "
						+ "	ord_mst.admission_id as \"admissionId\", " + "	ord_mst.encounter_id as \"encounterId\", "
						+ "	ord_mst.created_by as \"createdBy\", " + "	ord_mst.created_date as \"createdDate\", "
						+ " to_char(ord_mst.created_date, 'DD/MM/YYYY HH24:MI:SS') AS \"createdDateString\" , "
						+ "     pre_mst.prefix_id as \"prefixId\", "
					/*	+ "		(case when (select count(*) from t_order_details caetod where caetod.order_id = ord_mst.order_id and caetod.priority_id =2 ) > 0 then 2 else 1 end) as \"priorityId\","*/
						+ "	pati_mst.uhid_number as uhid, " + " pati_mst.is_vip as \"isVip\", " + " (case "
						+ " 		when pati_mst.is_vip = 'Y' then coalesce(pati_mst.alise_name, pati_mst.first_name) || ' ' || calculate_age(pati_mst.dob)  || '/' || gen_mst.gender_code "
						+ " 		else pre_mst.prefix_code || ' ' || pati_mst.first_name || ' ' || coalesce(pati_mst.middle_name) || ' ' || pati_mst.last_name || ' ' || calculate_age(pati_mst.dob) || '/' || gen_mst.gender_code "
						+ " 	end ) as \"patientDetails\", " + "	gen_mst.gender_id as \"genderId\", "
						+ "	'Dr.' || doct_mst.first_name || ' ' || doct_mst.last_name || ' (' || speciality_master.speciality_name || ')' as \"doctorDetails\", "
						+ " concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, pati_mst.dob))) as \"age\", "
						+ "	enc.encounter_type_id as \"encounterTypeId\", "
						+ "	coalesce(enc.is_critical,'N') as \"isCritical\", "
						+ " reflMst.referral_name as \"referralName\", " + " (case when ord_mst.encounter_id > 0 then "
						+ " ( tpsd.payment_entitlement_id ) "
						+ " else ( tpsd2.payment_entitlement_id ) end ) as \"paymentEntitlementId\", "
						+ " (case when ord_mst.encounter_id > 0 then " + " ( tpsd.payee_id ) "
						+ " else ( tpsd2.payee_id ) end ) as \"payeeId\", " + " ord_mst.order_no as \"orderNo\" "
						+ "from " + "	public.t_order_master ord_mst "
						+ " inner join public.t_order_details ord_details on "
						+ "	ord_details.order_id = ord_mst.order_id "
						+ " inner join patient.t_patient_registration pati_mst on "
						+ "	ord_mst.patient_id = pati_mst.patient_id " + "inner join public.m_gender_master gen_mst on "
						+ "	gen_mst.gender_id = pati_mst.gender_id " + "inner join public.m_prefix_master pre_mst on "
						+ "	pre_mst.prefix_id = pati_mst.prefix_id "
						+ "inner join public.m_visit_type_master visit_type_mst on "
						+ "	visit_type_mst.visit_type_id = ord_mst.visit_type_id "
						/*+ "inner join service.t_unit_service_master_details service_dtls on "
						+ "	ord_details.service_id = service_dtls.service_master_id "
						+ "	and service_dtls.status = 'A' " */
						+ "inner join public.t_lab_configuration lab_config on "
						+ "	lab_config.organisation_id = :orgId " + "	and lab_config.unit_id = :orgUnitId "
						+ "inner join lab.m_test_master test_mst on "
						+ "	ord_details.service_id = test_mst.service_id " + "	and test_mst.test_status = 'A' "
						+ "	and test_mst.is_histo_cyto in ('N','S') "
						+ " left join billing.t_billing_details bill_dtls on "
						+ " 	ord_details.order_details_id = bill_dtls.order_details_id "
						+ " left join billing.m_billing_master bill_mst on "
						+ " 	bill_mst.billing_master_id = bill_dtls.billing_master_id "
						+ " left join opd.t_encounter_master enc on " + "	 ord_mst.encounter_id = enc.encounter_id "
						+ "left join doctor.m_doctor_master doct_mst on "
						+ "	ord_mst.ord_doctor_id = doct_mst.doctor_id "
						+ " left join adt.t_admission ta  on ta.admission_id = ord_mst.admission_id and ta.status='A' "
						+ " left join opd.t_patient_sponsor_details tpsd "
						+ "		on enc.encounter_id = tpsd.encounter_id and tpsd.priority_id = 1 and tpsd.status='A' "
						+ " left join opd.t_patient_sponsor_details tpsd2 "
						+ "		on tpsd2.admission_id = ta.admission_id and tpsd2.priority_id = 1 and tpsd2.status='A' "
						+ "left join doctor.m_speciality_master speciality_master on "
						+ "	speciality_master.speciality_id = ord_mst.ord_doc_spl_id "
						+ " left join public.t_referal_details refrDtls on "
						+ "		refrDtls.encounter_id = enc.encounter_id "
						+ " left join adt.m_referral_master reflMst on "
						+ " 	reflMst.referral_id = refrDtls.referral_id ");
				
				if (searchDto.getSubDeptIds() != null && searchDto.getSubDeptIds().size() > 0) {
					sb.append(" left join (select count(*) c , service_id "
							+ "		from lab.m_test_master test_mst "
							+ "		left join service.m_service_master service on"
							+ "		service.service_master_id = test_mst.service_id "
							+ "		where  test_mst.test_status = 'A' "
							+ " and service.sub_speciality_id IN (:subDeptIds) "
							+ " group by service_id) m on m.service_id = ord_details.service_id and m.c > 0 ");
				}

				sb.append(" where " + "	ord_mst.org_id =:orgId "
						+ "    and (bill_mst.bill_cancelled = 'N' or bill_mst.bill_cancelled is null)"
						+ "	and ord_mst.org_unit_id =:orgUnitId " + "	and ord_mst.order_status = 'A' "
						+ "	and ord_mst.order_status_id in (:orderStatusId) "
						+ "	and ord_details.service_rendered in (0,1) " + "	and ord_details.service_is_billed in (0,1) "
						+ "	and ord_details.ord_cancelled = 'N' " + "	and ord_details.is_outsourced = 'N' "
						+ "	and ord_details.status = 'A' "
						+ "	and ord_details.service_rendering_deptid in (lab_config.biochemistry_id,lab_config.hematology_id,lab_config.microbiology_id,lab_config.serology_id,lab_config.clinical_pathology_id) ");

				if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0) {
					sb.append(" AND pati_mst.patient_id = :patientId ");
				}
				if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size() > 0) {
					visitTypeIds = searchDto.getVisitTypes().stream()
							.map(obj -> obj.getId())
							.collect(Collectors.toList());
					sb.append("AND visit_type_mst.visit_type_id IN (:visitTypeIds) ");
				}
				if (searchDto.getFromDt() != null) {
					sb.append(" AND to_char(ord_mst.created_date, 'YYYY-MM-DD') >= :fromDt ");
				}
				if (searchDto.getToDt() != null) {
					sb.append(" AND to_char(ord_mst.created_date, 'YYYY-MM-DD') <= :toDt ");
				}
				
				sb.append(" union " + "select " + "	distinct(ord_mst.order_id) as \"orderId\", "
						+ "	ord_mst.org_id as \"orgId\", " + "    bill_mst.bill_number as \"billNumber\", "
						/*+ " (select count(*) from t_order_details caetod where caetod.order_id = ord_mst.order_id and  caetod.is_dummy_service = false ) as \"realServiceCount\", "*/
						+ "    bill_mst.billing_master_id as \"billingMasterId\", "
						+ "	ord_mst.org_unit_id as \"orgUnitId\", " + "	ord_mst.order_status_id as \"orderStatusId\", "
						+ "	ord_mst.visit_type_id as \"visitTypeId\", "
						+ "	visit_type_mst.visit_type_code as \"visitType\", "
						+ " ord_mst.home_collection as \"isHomeCollection\", "
						+ "	ord_mst.ord_doctor_id as \"doctorId\", " + "	ord_mst.patient_id as \"patientId\", "
						+ "	ord_mst.admission_id as \"admissionId\", " + "	ord_mst.encounter_id as \"encounterId\", "
						+ "	ord_mst.created_by as \"createdBy\", " + "	ord_mst.created_date as \"createdDate\", "
						+ " to_char(ord_mst.created_date, 'DD/MM/YYYY HH24:MI:SS') AS \"createdDateString\" , "
						+ "     pre_mst.prefix_id as \"prefixId\", "
						/*+ "		(case when (select count(*) from t_order_details caetod where caetod.order_id = ord_mst.order_id and caetod.priority_id =2 ) > 0 then 2 else 1 end) as \"priorityId\","*/
						+ "	pati_mst.uhid_number as uhid, " + " pati_mst.is_vip as \"isVip\", " + " (case "
						+ " 		when pati_mst.is_vip = 'Y' then coalesce(pati_mst.alise_name, pati_mst.first_name) || ' ' || calculate_age(pati_mst.dob) || '/' || gen_mst.gender_code "
						+ " 		else pre_mst.prefix_code || ' ' || pati_mst.first_name || ' ' || coalesce(pati_mst.middle_name) || ' ' || pati_mst.last_name || ' ' || calculate_age(pati_mst.dob)  || '/' || gen_mst.gender_code "
						+ " 	end ) as \"patientDetails\", " + "	gen_mst.gender_id as \"genderId\", "
						+ "	'Dr.' || doct_mst.first_name || ' ' || doct_mst.last_name || ' (' || speciality_master.speciality_name || ')' as \"doctorDetails\", "
						+ " concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, pati_mst.dob))) as \"age\", "
						+ "	enc.encounter_type_id as \"encounterTypeId\", "
						+ "	coalesce(enc.is_critical,'N') as \"isCritical\", "
						+ " reflMst.referral_name as \"referralName\", " + " (case when ord_mst.encounter_id > 0 then "
						+ " ( tpsd.payment_entitlement_id ) "
						+ " else ( tpsd2.payment_entitlement_id ) end ) as \"paymentEntitlementId\", "
						+ " (case when ord_mst.encounter_id > 0 then " + " ( tpsd.payee_id ) "
						+ " else ( tpsd2.payee_id ) end ) as \"payeeId\", " + " ord_mst.order_no as \"orderNo\" "
						+ "from " + "	public.t_order_master ord_mst "
						+ " inner join public.t_order_details ord_details on "
						+ "	ord_details.order_id = ord_mst.order_id "
						+ " inner join patient.t_patient_registration pati_mst on "
						+ "	ord_mst.patient_id = pati_mst.patient_id " + "inner join public.m_gender_master gen_mst on "
						+ "	gen_mst.gender_id = pati_mst.gender_id " + "inner join public.m_prefix_master pre_mst on "
						+ "	pre_mst.prefix_id = pati_mst.prefix_id "
						+ "inner join public.m_visit_type_master visit_type_mst on "
						+ "	visit_type_mst.visit_type_id = ord_mst.visit_type_id "
						+ "inner join service.t_unit_service_master_details service_dtls on "
						+ "	ord_details.service_id = service_dtls.service_master_id "
						+ "	and service_dtls.status = 'A' " 
						+ "inner join public.t_lab_configuration lab_config on "
						+ "	lab_config.organisation_id = :orgId " + "	and lab_config.unit_id = :orgUnitId "
						+ " left join billing.t_billing_details bill_dtls on "
						+ " 	ord_details.order_details_id = bill_dtls.order_details_id "
						+ " left join billing.m_billing_master bill_mst on "
						+ " 	bill_mst.billing_master_id = bill_dtls.billing_master_id "
						+ " left join opd.t_encounter_master enc on " + "	 ord_mst.encounter_id = enc.encounter_id "
						+ "left join doctor.m_doctor_master doct_mst on "
						+ "	ord_mst.ord_doctor_id = doct_mst.doctor_id "
						+ " left join adt.t_admission ta  on ta.admission_id = ord_mst.admission_id and ta.status='A' "
						+ " left join opd.t_patient_sponsor_details tpsd "
						+ "		on enc.encounter_id = tpsd.encounter_id and tpsd.priority_id = 1 and tpsd.status='A' "
						+ " left join opd.t_patient_sponsor_details tpsd2 "
						+ "		on tpsd2.admission_id = ta.admission_id and tpsd2.priority_id = 1 and tpsd2.status='A' "
						+ "left join doctor.m_speciality_master speciality_master on "
						+ "	speciality_master.speciality_id = ord_mst.ord_doc_spl_id "
						+ " left join public.t_referal_details refrDtls on "
						+ "		refrDtls.encounter_id = enc.encounter_id "
						+ " left join adt.m_referral_master reflMst on "
						+ " 	reflMst.referral_id = refrDtls.referral_id ");
				if (searchDto.getSubDeptIds() != null && searchDto.getSubDeptIds().size() > 0) {
					sb.append("  left join (select " + 
							"		count(*) c , service_id  " + 
							"	from " + 
							"		lab.t_panel_master test_mst " + 
							"	left join service.m_service_master service on " + 
							"		service.service_master_id = test_mst.service_id " + 
							"		where  test_mst.status = 'A'  " + 
					       " and service.sub_speciality_id IN (:subDeptIds) "
							+ " group by service_id) m on m.service_id = ord_details.service_id and m.c > 0 ");
				}

				sb.append(" where	ord_mst.org_id =:orgId "
						+ "    and (bill_mst.bill_cancelled = 'N' or bill_mst.bill_cancelled is null)"
						+ "	and ord_mst.org_unit_id =:orgUnitId " + "	and ord_mst.order_status = 'A' "
						+ "	and ord_mst.order_status_id in (:orderStatusId) "
						+ "	and ord_details.service_rendered in (0,1) " + "	and ord_details.service_is_billed in (0,1) "
						+ "	and ord_details.ord_cancelled = 'N' " + "	and ord_details.is_outsourced = 'N' "
						+ "	and ord_details.status = 'A' " + "	and service_dtls.is_panel = 'Y' "
						+ " and ord_details.is_order_visible_dept='A' "
						+ "	and ord_details.service_rendering_deptid not in (lab_config.histopathology_id,lab_config.cystopathology_id,lab_config.clinical_pathology_id) ");

				if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0) {
					sb.append("AND pati_mst.patient_id = :patientId ");
				}
				if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size() > 0) {
					visitTypeIds = searchDto.getVisitTypes().stream()
							.map(obj -> obj.getId())
							.collect(Collectors.toList());
					sb.append("AND visit_type_mst.visit_type_id IN (:visitTypeIds) ");
				}
				if (searchDto.getFromDt() != null) {
					sb.append("AND TO_CHAR(ord_mst.created_date, 'YYYY-MM-DD') >= :fromDt ");
				}
				if (searchDto.getToDt() != null) {
					sb.append("AND TO_CHAR(ord_mst.created_date, 'YYYY-MM-DD') <= :toDt ");
				}
				
				if (searchDto.getLabSortingId() != null) {
					if (searchDto.getLabSortingId() == 1) {
						sb.append(" ORDER BY   \"isVip\" desc ,   \"patientId\" , \"createdDate\"  DESC ");
					} else if (searchDto.getLabSortingId() == 2) {
						sb.append(" ORDER BY   \"isVip\" desc ,   \"orderId\" DESC ");
					} else if (searchDto.getLabSortingId() == 4) {
						sb.append(" ORDER BY  \"isVip\" desc ,  \"createdDate\" DESC ");
					}
				} else {
					sb.append(" order by \"isCritical\" , \"isVip\" desc, \"createdDate\" DESC	");
				}
				int count = 0;
				Query createdQuery = sessionFactory.getCurrentSession().createSQLQuery(sb.toString());
			 createdQuery.setParameter("orgId", searchDto.getOrgId())
						.setParameter("orgUnitId", searchDto.getOrgUnitId())
						.setParameterList("orderStatusId", orderStatusIds);


				if (searchDto.getSubDeptIds() != null && !searchDto.getSubDeptIds().isEmpty()) {
					createdQuery.setParameter("subDeptIds", searchDto.getSubDeptIds());
				}

				if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0) {
					createdQuery.setParameter("patientId", searchDto.getPatientId());
				}

				if (searchDto.getVisitTypes() != null && !searchDto.getVisitTypes().isEmpty()) {
					createdQuery.setParameterList("visitTypeIds", visitTypeIds);
				}

				if (searchDto.getFromDt() != null) {
					createdQuery.setParameter("fromDt", searchDto.getFromDt());
				}

				if (searchDto.getToDt() != null) {
					createdQuery.setParameter("toDt", searchDto.getToDt());
				}



				createdQuery.setFirstResult(searchDto.getOffset() != null ? searchDto.getOffset() : 0)
						.setMaxResults(searchDto.getRecordPerPage() != null ? searchDto.getRecordPerPage() : 50);



				List<LabSampleMasterDto> listLabSampleMasterDto =createdQuery.setResultTransformer(Transformers.aliasToBean(LabSampleMasterDto.class)).list();

				if (listLabSampleMasterDto.isEmpty()) {
					return new Response(SUCCESS, SUCCESS_200_MESSAGE, "No Records Found.", listLabSampleMasterDto,
							count);
				} else {
					if (searchDto.getIsCountRequired() != null && searchDto.getIsCountRequired()) {
						List<LabSampleMasterDto> listLabSampleMasterListForCount = this
								.getPhlebotomyPostBillingCountList(searchDto);

						if (listLabSampleMasterListForCount != null && listLabSampleMasterListForCount.size() > 0) {
							count = listLabSampleMasterListForCount.size();
						}
					}
					return new Response(SUCCESS, SUCCESS_200_MESSAGE, null, listLabSampleMasterDto, count);
				}
			}

		} catch (Exception e) {
			
			logger.error("Exection", e);
			throw e;
		}
	}

	private List<LabSampleMasterDto> getPhlebotomyPostBillingCountList(SearchDto searchDto) {
		try {
			List<Integer>  visitTypeIds = new ArrayList<Integer>();
			List<Integer> orderStatusIds = (searchDto.getOrderStatusId() != null && searchDto.getOrderStatusId() > 0)
					? Arrays.asList(searchDto.getOrderStatusId())
					: PENDING_INPROGRESS_RENDER_CANCEL;
			StringBuilder sb = new StringBuilder();
			sb.append("select " + "	distinct(ord_mst.order_id) as \"orderId\" " + "from "
					+ "	public.t_order_master ord_mst " + "inner join public.t_order_details ord_details on "
					+ "	ord_details.order_id = ord_mst.order_id "
					+ "inner join patient.t_patient_registration pati_mst on "
					+ "	ord_mst.patient_id = pati_mst.patient_id "
					+ "inner join public.m_visit_type_master visit_type_mst on "
					+ "	visit_type_mst.visit_type_id = ord_mst.visit_type_id "
					+ "inner join service.t_unit_service_master_details service_dtls on "
					+ "	ord_details.service_id = service_dtls.service_master_id " + "	and service_dtls.status = 'A' "
					+ "inner join public.t_lab_configuration lab_config on "
					+ "	lab_config.organisation_id = :orgId " + "	and lab_config.unit_id = :orgUnitId "
					+ "inner join lab.m_test_master test_mst on " + "	ord_details.service_id = test_mst.service_id "
					+ "	and test_mst.test_status = 'A' " + "	and test_mst.is_histo_cyto in ('N','S') " + "where "
					+ "	ord_mst.org_id =:orgId " + "	and ord_mst.org_unit_id =:orgUnitId "
					+ "	and ord_mst.order_status = 'A' " + "	and ord_mst.order_status_id in (:orderStatusId) "
					+ "	and ord_details.service_rendered in (0,1) " + "	and ord_details.service_is_billed in (0,1) "
					+ "	and ord_details.ord_cancelled = 'N' " + "	and ord_details.is_outsourced = 'N' "
					+ "	and ord_details.status = 'A' "
					+ "	and ord_details.service_rendering_deptid in (lab_config.biochemistry_id,lab_config.hematology_id,lab_config.microbiology_id,lab_config.serology_id,lab_config.clinical_pathology_id) ");

			if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0) {
				sb.append(" AND pati_mst.patient_id = :patientId ");
			}
			if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size() > 0) {
				visitTypeIds = searchDto.getVisitTypes().stream()
						.map(obj -> obj.getId())
						.collect(Collectors.toList());
				sb.append(" AND visit_type_mst.visit_type_id IN (:visitTypeIds) ");
			}
			if (searchDto.getFromDt() != null) {
				sb.append(" and to_char(ord_mst.created_date,'YYYY-MM-DD') >= :fromDt ");
			}
			if (searchDto.getToDt() != null) {
				sb.append(" and to_char(ord_mst.created_date,'YYYY-MM-DD') <= :toDt ");
			}

			sb.append(" union " + "select " + "	distinct(ord_mst.order_id) as \"orderId\" " + "from "
					+ "	public.t_order_master ord_mst " + "inner join public.t_order_details ord_details on "
					+ "	ord_details.order_id = ord_mst.order_id "
					+ "inner join patient.t_patient_registration pati_mst on "
					+ "	ord_mst.patient_id = pati_mst.patient_id "
					+ "inner join public.m_visit_type_master visit_type_mst on "
					+ "	visit_type_mst.visit_type_id = ord_mst.visit_type_id "
					+ "inner join service.t_unit_service_master_details service_dtls on "
					+ "	ord_details.service_id = service_dtls.service_master_id " + "	and service_dtls.status = 'A' "
					+ "inner join public.t_lab_configuration lab_config on "
					+ "	lab_config.organisation_id = :orgId " + "	and lab_config.unit_id = :orgUnitId " + "where "
					+ "	ord_mst.org_id =:orgId " + "	and ord_mst.org_unit_id =:orgUnitId "
					+ "	and ord_mst.order_status = 'A' " + "	and ord_mst.order_status_id in (:orderStatusId) "
					+ "	and ord_details.service_rendered in (0,1) " + "	and ord_details.service_is_billed in (0,1) "
					+ "	and ord_details.ord_cancelled = 'N' " + "	and ord_details.is_outsourced = 'N' "
					+ "	and ord_details.status = 'A' " + "	and service_dtls.is_panel = 'Y' "
					+ " and ord_details.is_order_visible_dept='A' "
					+ "	and ord_details.service_rendering_deptid not in (lab_config.histopathology_id,lab_config.cystopathology_id,lab_config.clinical_pathology_id) ");

			if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0) {
				sb.append(" AND pati_mst.patient_id = :patientId ");
			}
			if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size() > 0) {
				visitTypeIds = searchDto.getVisitTypes().stream()
						.map(obj -> obj.getId())
						.collect(Collectors.toList());
				sb.append(" AND visit_type_mst.visit_type_id IN (:visitTypeIds) ");
			}
			if (searchDto.getFromDt() != null) {
				sb.append(" and to_char(ord_details.order_date,'YYYY-MM-DD') >= :fromDt ");
			}
			if (searchDto.getToDt() != null) {
				sb.append(" and to_char(ord_details.order_date,'YYYY-MM-DD') <= :toDt ");
			}

			Query createdQuery = sessionFactory.getCurrentSession().createSQLQuery(sb.toString());
		createdQuery.setParameter("orgId", searchDto.getOrgId())
					.setParameter("orgUnitId", searchDto.getOrgUnitId()).setParameterList("orderStatusId", orderStatusIds);


			if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0) {
				createdQuery.setParameter("patientId", searchDto.getPatientId());
			}

			if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size() > 0) {
				createdQuery.setParameterList("visitTypeIds", visitTypeIds);
			}

			if (searchDto.getFromDt() != null) {
				createdQuery.setParameter("fromDt", searchDto.getFromDt());
			}

			if (searchDto.getToDt() != null) {
				createdQuery.setParameter("toDt", searchDto.getToDt());
			}



			List<LabSampleMasterDto> listLabSampleMasterDto =createdQuery.setResultTransformer(Transformers.aliasToBean(LabSampleMasterDto.class)).list();
			return listLabSampleMasterDto;
		} catch (Exception e) {
			logger.error("Exection", e);
			return null;
		}
	}

	private List<LabSampleMasterDto> getPhlebotomyPreBillingCountList(SearchDto searchDto) {
		try {
			List<Integer>  visitTypeIds = new ArrayList<Integer>();
			List<Integer> orderStatusIds = (searchDto.getOrderStatusId() != null && searchDto.getOrderStatusId() > 0)
					? Arrays.asList(searchDto.getOrderStatusId())
					: PENDING_INPROGRESS_RENDER_CANCEL;
			StringBuilder sb = new StringBuilder();
			sb.append("select " + "	ord_mst.order_id as \"orderId\" " + "from "
					+ "	public.t_order_master ord_mst " 
					+ "inner join public.t_order_details ord_details on "
					+ "	ord_details.order_id = ord_mst.order_id "
					+ "inner join patient.t_patient_registration pati_mst on "
					+ "	ord_mst.patient_id = pati_mst.patient_id "
					+ "inner join public.m_visit_type_master visit_type_mst on "
					+ "	visit_type_mst.visit_type_id = ord_mst.visit_type_id "
					+ "inner join service.t_unit_service_master_details service_dtls on "
					+ "	ord_details.service_id = service_dtls.service_master_id " + "	and service_dtls.status = 'A' "
					/*+ "inner join public.t_lab_configuration lab_config on "
					+ "	lab_config.organisation_id = :orgId " 
					+ "	and lab_config.unit_id = :orgUnitId "*/
					+ "inner join lab.m_test_master test_mst on " 
					+ "	ord_details.service_id = test_mst.service_id "
					+ "	and test_mst.test_status = 'A' " 
					/*+ "	and test_mst.is_histo_cyto in ('N','S') "*/
					+ "left join billing.t_billing_details bill_dtls on "
					+ "	ord_details.order_details_id = bill_dtls.order_details_id "
					+ "left join billing.m_billing_master bill_mst on "
					+ "	bill_mst.billing_master_id = bill_dtls.billing_master_id and bill_mst.bill_cancelled = 'N' and bill_mst.bill_status_id <> 1 "
					/*+ "left join lab.m_priority_master pri_mst on " 
					+ "	ord_mst.priority_id = pri_mst.priority_id "*/
					+ "where " 
					+ "	ord_mst.org_id =:orgId " + "	and ord_mst.org_unit_id =:orgUnitId "
					+ "	and ord_mst.order_status = 'A' " + "	and ord_mst.order_status_id in (:orderStatusId) "
					+ "	and ord_details.service_rendered in (0,1) "
					+ " and  (case when ord_mst.visit_type_id = 1 then  ord_details.service_is_billed =1 "
					+ "	else  ord_details.service_is_billed in(0,1)  end ) " + " and  " + "	(case "
					+ "		when ord_mst.visit_type_id = 1 then bill_mst.bill_status_id  is not null " + " else "
					+ "		(bill_mst.bill_status_id is null or bill_mst.bill_status_id in(1,2,3))  " + "	end )"
					+ "	and ord_details.ord_cancelled = 'N' " + "	and ord_details.is_outsourced = 'N' "
					+ "	and ord_details.status = 'A' " + "  and ord_details.is_order_visible_dept='A' "
					// + " and ord_details.service_rendering_deptid in
					// (lab_config.biochemistry_id,lab_config.hematology_id,lab_config.microbiology_id,lab_config.serology_id,lab_config.clinical_pathology_id)
					// " );
					/*+ " and lab_config.lab_speciality_id = :deptId "*/
					);
			/*if (searchDto.getPriorityId() != null && searchDto.getPriorityId() > 0) {
				sb.append(" AND pri_mst.priority_id = " + searchDto.getPriorityId());
			}*/
			if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0) {
				sb.append(" AND pati_mst.patient_id = :patientId");
			}
			if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size() > 0) {
				visitTypeIds = searchDto.getVisitTypes().stream()
						.map(obj -> obj.getId())
						.collect(Collectors.toList());
				sb.append(" AND visit_type_mst.visit_type_id IN (:visitTypeIds)");
			}
			if (searchDto.getFromDt() != null) {
				sb.append(" AND to_char(ord_details.order_date, 'YYYY-MM-DD') >= :fromDt");
			}
			if (searchDto.getToDt() != null) {
				sb.append(" AND to_char(ord_details.order_date, 'YYYY-MM-DD') <= :toDt");
			}

			if (searchDto.getUhidNumber() != "") {
				sb.append(" AND lower(pati_mst.uhid_number) LIKE :uhidNumber");
			}
			sb.append("  group by ord_mst.order_id ");

		/*	sb.append(" union " + "select " + "	distinct(ord_mst.order_id) as \"orderId\" " + "from "
					+ "	public.t_order_master ord_mst " + "inner join public.t_order_details ord_details on "
					+ "	ord_details.order_id = ord_mst.order_id " + "inner join billing.t_billing_details bill_dtls on "
					+ "	ord_details.order_details_id = bill_dtls.order_details_id "
					+ "inner join billing.m_billing_master bill_mst on "
					+ "	bill_mst.billing_master_id = bill_dtls.billing_master_id and bill_mst.bill_cancelled = 'N' and bill_mst.bill_status_id <> 1 "
					+ "inner join patient.t_patient_registration pati_mst on "
					+ "	ord_mst.patient_id = pati_mst.patient_id "
					+ "inner join public.m_visit_type_master visit_type_mst on "
					+ "	visit_type_mst.visit_type_id = ord_mst.visit_type_id "
					+ "inner join service.t_unit_service_master_details service_dtls on "
					+ "	ord_details.service_id = service_dtls.service_master_id " + "	and service_dtls.status = 'A' "
					+ "inner join public.t_lab_configuration lab_config on "
					+ "	lab_config.organisation_id = :orgId " + "	and lab_config.unit_id = :orgUnitId "
					+ "left join lab.m_priority_mASter pri_mst on " + "	ord_mst.priority_id = pri_mst.priority_id "
					+ "where " + "	ord_mst.org_id =:orgId " + "	and ord_mst.org_unit_id =:orgUnitId "
					+ "	and ord_mst.order_status = 'A' " + "	and ord_mst.order_status_id in (:orderStatusId) "
					+ "	and ord_details.service_rendered in (0,1) "
					+ " and  (case when ord_mst.visit_type_id = 1 then  ord_details.service_is_billed =1 "
					+ "	else  ord_details.service_is_billed in(0,1)  end ) " + " and  " + "	(case "
					+ "		when ord_mst.visit_type_id = 1 then bill_mst.bill_status_id  is not null " + " else "
					+ "		(bill_mst.bill_status_id is null or bill_mst.bill_status_id in(1,2,3))  " + "	end )"
					+ "	and ord_details.ord_cancelled = 'N' " + "	and ord_details.is_outsourced = 'N' "
					+ "	and ord_details.status = 'A' " + " and ord_details.is_order_visible_dept='A' "
					+ "	and service_dtls.is_panel = 'Y' "
					+ "	and ord_details.service_rendering_deptid not in (lab_config.histopathology_id,lab_config.cystopathology_id,lab_config.clinical_pathology_id) ");

			if (searchDto.getPriorityId() != null && searchDto.getPriorityId() > 0) {
				sb.append(" AND pri_mst.priority_id = " + searchDto.getPriorityId());
			}
			if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0) {
				sb.append(" AND pati_mst.patient_id = " + searchDto.getPatientId());
			}
			if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size() > 0) {
				sb.append(" AND visit_type_mst.visit_type_id IN ("
						+ StringCommonMethods.convertToStringJoiner(searchDto.getVisitTypes()) + ")");
			}
			if (searchDto.getFromDt() != null) {
				sb.append("	and to_char(ord_details.order_date,'YYYY-MM-DD') >= '" + searchDto.getFromDt() + "'");
			}
			if (searchDto.getToDt() != null) {
				sb.append("	and to_char(ord_details.order_date,'YYYY-MM-DD') <= '" + searchDto.getToDt() + "'");
			}*/

			Query createdQuery = sessionFactory.getCurrentSession().createSQLQuery(sb.toString());

			 createdQuery.setParameter("orgId", searchDto.getOrgId())
					.setParameter("orgUnitId", searchDto.getOrgUnitId()).setParameterList("orderStatusId", orderStatusIds);

			if (searchDto.getPriorityId() != null && searchDto.getPriorityId() > 0) {
				createdQuery.setParameter("priorityId", searchDto.getPriorityId());
			}
			if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0) {
				createdQuery.setParameter("patientId", searchDto.getPatientId());
			}
			if (searchDto.getFromDt() != null) {
				createdQuery.setParameter("fromDt", searchDto.getFromDt());
			}
			if (searchDto.getToDt() != null) {
				createdQuery.setParameter("toDt", searchDto.getToDt());
			}
			/*if (searchDto.getDeptId() != null) {
				createdQuery.setParameter("deptId", searchDto.getDeptId());
			}*/
			if (searchDto.getVisitTypes() != null && !searchDto.getVisitTypes().isEmpty()) {
				createdQuery.setParameterList("visitTypes", visitTypeIds);
			}
			if (searchDto.getUhidNumber() != null && !searchDto.getUhidNumber().isEmpty()) {
				createdQuery.setParameter("uhidNumber", searchDto.getUhidNumber().toLowerCase() + "%");
			}


					//.setParameter("deptId", searchDto.getDeptId())

			List<LabSampleMasterDto> listLabSampleMasterDto =createdQuery.setResultTransformer(Transformers.aliasToBean(LabSampleMasterDto.class)).list();
			return listLabSampleMasterDto;
		} catch (Exception e) {
			logger.error("Exection", e);
			return null;
		}
	}

	@Override
	public Response searchSendToCRPatientlist(SearchCommonDto searchCommonDto) throws ApplicationException {
		List<CommonAutoCompleteDto> patientList = null;
		try {
			patientList = (List<CommonAutoCompleteDto>) sessionFactory.getCurrentSession()
					.getNamedQuery("LAB_PATIENT_SEARCH").setParameter("orgId", searchCommonDto.getOrgId())
					// .setParameter("orgUnitId", searchCommonDto.getOrgUnitId())
					.setParameter("searchKeyword",
							"%" + searchCommonDto.getSearchKeyword().trim().toLowerCase().trim() + "%")
					.setResultTransformer(Transformers.aliasToBean(CommonAutoCompleteDto.class)).list();
			return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, patientList, null);

		} catch (Exception e) {
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	public Response getFilteredSendToCRPatientlist(SearchDto searchDto) throws ApplicationException {
		try {

			Response resp = getSpecialityAndSubSpecialityListByUserIdByUnitId(searchDto);
			List<Integer> deptIds = new ArrayList<>();
			List<Integer> subDeptIds = new ArrayList<>();
			List<Integer> subDepts = new ArrayList<>();
			List<SearchDto> specialityList = new ArrayList<>();
			List<SearchDto> subSpecialityList = new ArrayList<>();
			if (resp.getListObject() != NULL && resp.getListObject().size() > 0) {
				specialityList = resp.getListObject();
				if (resp.getListObject1() != null) {
					subSpecialityList = resp.getListObject1();
				}
			}
			if (specialityList != null && specialityList.size() > 0) {
				for (Iterator iterator = specialityList.iterator(); iterator.hasNext();) {
					SearchDto dto = (SearchDto) iterator.next();
					deptIds.add(dto.getSpecialityId());
				}
			} else {
				deptIds.add(searchDto.getDeptId());
			}

			if (subSpecialityList != null && subSpecialityList.size() > 0) {
				for (Iterator iterator = subSpecialityList.iterator(); iterator.hasNext();) {
					SearchDto dto2 = (SearchDto) iterator.next();
					subDeptIds.add(dto2.getSubSpecialityMasterId());
				}
			}
			searchDto.setSubDeptIds(subDeptIds);

			String getSendToCRQuery = sessionFactory.getCurrentSession().getNamedQuery("GET_COLLECTED_SAMPLE_LIST")
					.getQueryString().toString();

			String[] orderSplit = getSendToCRQuery.split("ORDER", 2);

			String orderPart1 = orderSplit[0];
			String orderPart2 = " ORDER " + orderSplit[1];

			StringBuilder getSendToCRSearchQueryPart1 = new StringBuilder(orderPart1);

			if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0)
				getSendToCRSearchQueryPart1.append(" AND lbsampledtls.patient_id = :patientId");

			if (searchDto.getAssignUnit() != null) {
				getSendToCRSearchQueryPart1.append(" AND lbsampledtls.assign_unit = :assignUnit");
			}

			if (searchDto.getSubDepts() != null && searchDto.getSubDepts().size() > 0) {
				subDepts = searchDto.getSubDepts().stream()
						.map(obj -> obj.getId())
						.collect(Collectors.toList());
				getSendToCRSearchQueryPart1.append(" AND lbsampledtls.sub_dept_id IN (:subDeptIds)");
			}

			if (searchDto.getLabSampleDtlsId() != null && searchDto.getLabSampleDtlsId() > 0)
				getSendToCRSearchQueryPart1.append(" AND lbsampledtls.lab_sample_dtls_id = :labSampleDtlsId");

			if (searchDto.getUhidNumber() != "") {
				getSendToCRSearchQueryPart1.append(" AND lower(pati_mst.uhid_number) LIKE concat('%', LOWER(:uhidNumber), '%')");
			}

			if (searchDto.getSampleNo() != "") {
				getSendToCRSearchQueryPart1.append(" AND lower(sample_mst.sample_no) LIKE concat('%', LOWER(:sampleNo), '%')");
			}

			if (searchDto.getSearchField() != null && searchDto.getSearchField() != "") {
				getSendToCRSearchQueryPart1.append(
						" AND (lower(pati_mst.uhid_number) LIKE concat('%', LOWER(:searchField), '%') OR " +
								" lower(visit_type_master.visit_type_code) LIKE concat('%', LOWER(:searchField), '%') OR " +
								" lower(test_master.test_desc) LIKE concat('%', LOWER(:searchField), '%') OR " +
								" lower(test_master.test_code) LIKE concat('%', LOWER(:searchField), '%') OR " +
								" lower(pati_mst.first_name) LIKE concat('%', LOWER(:searchField), '%') OR " +
								" lower(pati_mst.middle_name) LIKE concat('%', LOWER(:searchField), '%') OR " +
								" lower(pati_mst.last_name) LIKE concat('%', LOWER(:searchField), '%'))");
			}
			
			StringBuilder getSendToCRSearchQueryPart2 = new StringBuilder(orderPart2);
			getSendToCRSearchQueryPart1.append(getSendToCRSearchQueryPart2);

			Query createdQuery = sessionFactory.getCurrentSession()
					.createSQLQuery(getSendToCRSearchQueryPart1.toString());

			 createdQuery.setParameter("orgId", searchDto.getOrgId())
					.setParameter("orgUnitId", searchDto.getOrgUnitId()).setParameterList("deptId", deptIds)
					.setParameterList("sampleStatusIds", COLLECTED_SAMPLE_STATUS)
					.setParameter("fromDate", searchDto.getFromDt()).setParameter("toDate", searchDto.getToDt());

			if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0) {
				createdQuery.setParameter("patientId", searchDto.getPatientId());
			}

			if (searchDto.getAssignUnit() != null) {
				createdQuery.setParameter("assignUnit", searchDto.getAssignUnit());
			}

			if (searchDto.getSubDepts() != null && !searchDto.getSubDepts().isEmpty()) {
				createdQuery.setParameterList("subDeptIds", subDepts);
			}

			if (searchDto.getLabSampleDtlsId() != null && searchDto.getLabSampleDtlsId() > 0) {
				createdQuery.setParameter("labSampleDtlsId", searchDto.getLabSampleDtlsId());
			}

			if (searchDto.getUhidNumber() != null && !searchDto.getUhidNumber().isEmpty()) {
				createdQuery.setParameter("uhidNumber", searchDto.getUhidNumber());
			}

			if (searchDto.getSampleNo() != null && !searchDto.getSampleNo().isEmpty()) {
				createdQuery.setParameter("sampleNo", searchDto.getSampleNo());
			}
			if (searchDto.getSearchField() != null && !searchDto.getSearchField().isEmpty()) {
				createdQuery.setParameter("searchField", searchDto.getSearchField());
			}


			createdQuery.setFirstResult(searchDto.getOffset() != null ? searchDto.getOffset() : 0)
					.setMaxResults(searchDto.getRecordPerPage() != null ? searchDto.getRecordPerPage() : 50)
					.setResultTransformer(Transformers.aliasToBean(CollectedSampleDto.class));

			List<CollectedSampleDto> listCollectedSampleDto = createdQuery.list();

			if (listCollectedSampleDto.isEmpty()) {
				return new Response(SUCCESS, SUCCESS_200_MESSAGE, "No Records Found.", listCollectedSampleDto, null);
			} else {
				return new Response(SUCCESS, SUCCESS_200_MESSAGE, null, listCollectedSampleDto, null);
			}

		} catch (Exception e) {
			logger.error("Exection", e);
			throw e;
		}

	}

	@Override
	public Response searchLabGenralLabSampleNo(SearchCommonDto searchCommonDto) throws ApplicationException {
		List<LabDashBoardDto> patientList = null;
		try {
			patientList = (List<LabDashBoardDto>) sessionFactory.getCurrentSession()
					.getNamedQuery("SEARCH_SAMPLE_NO_GENRAL_LAB_LIST").setParameter("orgId", searchCommonDto.getOrgId())
					.setParameter("orgUnitId", searchCommonDto.getOrgUnitId())
					.setParameter("searchKeyword",
							"%" + searchCommonDto.getSearchKeyword().trim().toLowerCase().trim() + "%")
					.setParameter("sampleStatusId", searchCommonDto.getSampleStatusId())
					// .setParameter("detpId", searchCommonDto.getDeptId())
					// .setParameter("subDeptId", searchCommonDto.getSubDeptId())
					.setResultTransformer(Transformers.aliasToBean(LabDashBoardDto.class)).list();
			return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, patientList, null);

		} catch (Exception e) {
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	public Response getInPatientOrders(int orgId, int orgUnitId, int patientId, int admissionId, int deptId)
			throws ApplicationException {
		List<LabSampleDetailsMasterDtoIP> listLabSampleDetailsMasterDto = null;
		try {

			listLabSampleDetailsMasterDto = sessionFactory.getCurrentSession().getNamedQuery("GET_LAB_ORDER_DETAILS_IP")
					.setParameter("orgId", orgId).setParameter("orgUnitId", orgUnitId).setParameter("patientId", patientId)
					.setParameter("admissionId", admissionId).setParameter("deptId", deptId)
					.setParameterList("visitTypeIds", Arrays.asList(ICommonConstants.IP, ICommonConstants.DC, ICommonConstants.ER)).setParameter("ordCancelled", 'N')
					.setParameter("sampleStatus", 'A').setParameter("unitStatus", 'A')
					/* .setParameter("serviceRendered", 0) */
					.setParameter("sampleStatusId", SAMPLE_RECOLLECTED).setParameter("patientStatusId", REPAT_PATIRNT)
					.setResultTransformer(Transformers.aliasToBean(LabSampleDetailsMasterDtoIP.class)).list();
			return new Response(SUCCESS, SUCCESS_200, null, listLabSampleDetailsMasterDto, null);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	public Response<LabSampleDetailsMasterDtoIP, Integer> getInPatientRadiologyOrders(int orgId, int orgUnitId,
			int admissionId) throws ApplicationException {
		List<LabSampleDetailsMasterDtoIP> listLabSampleDetailsMasterDto = null;
		try {
			listLabSampleDetailsMasterDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_RADIOLOGY_ORDER_DETAILS_IP").setParameter("orgId", orgId)
					.setParameter("orgUnitId", orgUnitId).setParameter("visitTypeId", ICommonConstants.IP).setParameter("ordCancelled", 'N')
					.setParameter("serviceRendered", 0).setParameter("admissionId", admissionId)
					.setResultTransformer(Transformers.aliasToBean(LabSampleDetailsMasterDtoIP.class)).list();
			return new Response(SUCCESS, SUCCESS_200, null, listLabSampleDetailsMasterDto, null);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	public Response reprintBarcode(List<String> sampleIds, Integer labSampleDetailsId) throws ApplicationException {
		try {
			List<BarcodeDto> list = sessionFactory.getCurrentSession().createSQLQuery(" select "
					+ "	DISTINCT(sample_mst.sample_id) as \"sampleId\", " + "	patient.patient_id as \"patientId\", "
					+ "	sample_mst.lab_sample_id as \"labSampleId\", " + "	patient.uhid_number as \"uhid\", "
					+ "	sample_mst.visit_type_id as \"visitTypeId\", " + "  samp.sample_desc as \"sampleName\", "
					+ "	subspe_mst.sub_speciality_name as \"deptName\", "
					+ "	pre_mst.prefix_code || ' ' || patient.first_name || ' ' || patient.last_name || ' ' || calculate_age(patient.dob) || '/' || gen_mst.gender_code as \"patientDetails\", "
					+ "	to_char(sample_mst.sample_gen_datetime, 'DD/MM/YYYY') as \"generationDateTime\", "
					+ "	to_char(sample_mst.sample_gen_datetime, 'HH:MM:SS') as \"generationTime\" ,smp_dtls.order_details_id as \"orderDetailsId\","
					+ " smp_dtls.lab_sample_dtls_id as \"labSampleDtlsId\" " + "from "
					+ "	lab.t_lab_sample_master sample_mst " + "inner join lab.t_lab_sample_dtls smp_dtls on "
					+ "	sample_mst.lab_sample_id = smp_dtls.lab_sample_id "
					+ "inner join patient.t_patient_registration patient on "
					+ "	sample_mst.patient_id = patient.patient_id " + "inner join m_prefix_master pre_mst on "
					+ "	patient.prefix_id = pre_mst.prefix_id " + "inner join m_gender_master gen_mst on "
					+ "	patient.gender_id = gen_mst.gender_id " + " inner join lab.m_sample_master samp on "
					+ " smp_dtls.sample_type_id = samp.sample_id "
					+ "left join doctor.m_sub_speciality_master subspe_mst on "
					+ "	subspe_mst.sub_speciality_id = smp_dtls.sub_dept_id " + "where "
					+ "	sample_mst.sample_id in (:sampleIds) and smp_dtls.lab_sample_dtls_id =:labSampleDetailsId")
					.setParameterList("sampleIds", sampleIds).setParameter("labSampleDetailsId", labSampleDetailsId)
					.setResultTransformer(Transformers.aliasToBean(BarcodeDto.class)).list();
			return new Response(SUCCESS, SUCCESS_200, null, list, null);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	public Response updateOrderStatus(Integer orderId) throws ApplicationException {
		try {
			BigInteger checkOrderStatus = (BigInteger) sessionFactory.getCurrentSession()
					.getNamedQuery("CHECK_ORDER_STATUS").setParameter("orderId", orderId).uniqueResult();
			Integer orderStatusId = 0;
			if (checkOrderStatus.intValue() > 0) {
				orderStatusId = INPROCESS;
			} else {
				orderStatusId = RENDER;
			}
			Integer orderUpdateSucess = (Integer) sessionFactory.getCurrentSession()
					.getNamedQuery("LAB_UPDATE_ORDER_MASTER_STATUS").setParameter("orderStatusId", orderStatusId)
					.setParameter("orderId", orderId).executeUpdate();

			return new Response(SUCCESS, SUCCESS_200, null, null, orderUpdateSucess);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	public Response getFilteredSendToCRPatientlistCount(SearchDto searchDto) throws ApplicationException {
		try {
			List<Integer>  subDepts = new ArrayList<Integer>();
			String getSendToCRQuery = sessionFactory.getCurrentSession().getNamedQuery("GET_COLLECTED_SAMPLE_LIST")
					.getQueryString().toString();
			String[] orderSplit = getSendToCRQuery.split("ORDER", 2);
			String orderPart1 = orderSplit[0];
			String orderPart2 = " ORDER " + orderSplit[1];
			StringBuilder getSendToCRSearchQueryPart1 = new StringBuilder(orderPart1);
			if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0)
				getSendToCRSearchQueryPart1.append(" AND lbsampledtls.patient_id =:patientId ");
			if (searchDto.getAssignUnit() != null) {
				getSendToCRSearchQueryPart1.append(" AND lbsampledtls.assign_unit =:assignUnit" );
			}
			if (searchDto.getSubDepts() != null && searchDto.getSubDepts().size() > 0) {
				subDepts = searchDto.getSubDepts().stream()
						.map(obj -> obj.getId())
						.collect(Collectors.toList());
				getSendToCRSearchQueryPart1.append(" AND lbsampledtls.sub_dept_id  IN (:subDepts)");
			}

			if (searchDto.getLabSampleDtlsId() != null && searchDto.getLabSampleDtlsId() > 0)
				getSendToCRSearchQueryPart1
						.append(" AND lbsampledtls.lab_sample_dtls_id =:labSampleDtlsId " );

			if (searchDto.getUhidNumber() != "") {
				getSendToCRSearchQueryPart1.append(" AND lower(pati_mst.uhid_number) LIKE(lower(:uhidNumber)) ");
			}

			if (searchDto.getSampleNo() != "") {
				getSendToCRSearchQueryPart1.append(" AND lower( sample_mst.sample_no ) LIKE(lower(:sampleNo)) ");
			}

			StringBuilder getSendToCRSearchQueryPart2 = new StringBuilder(orderPart2);
			getSendToCRSearchQueryPart1.append(getSendToCRSearchQueryPart2);

			Query createdQuery = sessionFactory.getCurrentSession()
					.createSQLQuery(getSendToCRSearchQueryPart1.toString());


			if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0)
				createdQuery.setParameter("patientId", searchDto.getPatientId());
			if (searchDto.getAssignUnit() != null) {
				createdQuery.setParameter("assignUnit", searchDto.getAssignUnit());
			}
			if (searchDto.getSubDepts() != null && searchDto.getSubDepts().size() > 0)
				createdQuery.setParameterList("subDepts", subDepts);

			if (searchDto.getLabSampleDtlsId() != null && searchDto.getLabSampleDtlsId() > 0)
				createdQuery.setParameter("labSampleDtlsId", searchDto.getLabSampleDtlsId());

			if (searchDto.getUhidNumber() != "") {
				createdQuery.setParameter("uhidNumber", "%" + searchDto.getUhidNumber()+"%");
			}

			if (searchDto.getSampleNo() != "") {
				createdQuery.setParameter("sampleNo", "%" + searchDto.getSampleNo()+"%");
			}

			List<CollectedSampleDto> listCollectedSampleDto = createdQuery.setParameter("orgId", searchDto.getOrgId())
					.setParameter("orgUnitId", searchDto.getOrgUnitId()).setParameter("deptId", searchDto.getDeptId())
					.setParameterList("sampleStatusIds", COLLECTED_SAMPLE_STATUS)
					.setString("fromDate", searchDto.getFromDt()).setString("toDate", searchDto.getToDt())
					.setResultTransformer(Transformers.aliasToBean(CollectedSampleDto.class)).list();
			if (listCollectedSampleDto.isEmpty()) {
				return new Response(SUCCESS, SUCCESS_200_MESSAGE, "No Records Found.", null,
						listCollectedSampleDto.size());
			} else {
				return new Response(SUCCESS, SUCCESS_200_MESSAGE, null, null, listCollectedSampleDto.size());
			}

		} catch (Exception e) {
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	public Response getPerformTestId(CollectedSampleDto collectedSampleDto) throws ApplicationException {
		try {
			CollectedSampleDto dto = (CollectedSampleDto) sessionFactory.getCurrentSession()
					.createSQLQuery(" select  test_id as \"performTestId\" from lab.m_test_master  "
							+ " where test_status = 'A' and service_id =:serviceId "
							+ "  and org_unit_id =:assignUnit")
					.setParameter("serviceId",collectedSampleDto.getServiceId())
					.setParameter("assignUnit",collectedSampleDto.getAssignUnit())
					.setResultTransformer(Transformers.aliasToBean(CollectedSampleDto.class)).uniqueResult();
			return new Response(SUCCESS, SUCCESS_200, null, null, dto);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	public String getTestName(Integer orderId, Integer unitId) {
		try {
			String query = " select " + " 	array_to_string(array(select " + " 	test_mst.test_desc " + " from "
					+ " 	public.t_order_details ord_dtls " + " left join billing.t_credit_note_detail tcnd on "
					+ " 	tcnd.order_detail_id = ord_dtls.order_details_id " + "	and tcnd.status = 'A' "
					+ "	and tcnd.cancel_or_discount = 1 " + " inner join lab.m_test_master test_mst on "
					+ " 	ord_dtls.service_id = test_mst.service_id " + " where " + " 	ord_dtls.order_id =:orderId "
					+ " 	and ord_dtls.org_unit_id =:unitId " + " 	and test_mst.org_unit_id =:unitId "
					+ " 	and ord_dtls.is_order_visible_dept = 'A' " + " 	and test_mst.test_status = 'A' "
					+ " 	and (ord_dtls.ord_cancelled = 'N' and tcnd.credit_note_detail_id is null)), ', ') "
					+ " union all " + " select " + " 	array_to_string(array(select " + " 	test_mst.test_desc "
					+ " from " + " 	public.t_order_details ord_dtls "
					+ " left join billing.t_credit_note_detail tcnd on "
					+ " 	tcnd.order_detail_id = ord_dtls.order_details_id " + "	and tcnd.status = 'A' "
					+ "	and tcnd.cancel_or_discount = 1 " + " inner join lab.t_panel_master panel_master on "
					+ " 	panel_master.service_id = ord_dtls.service_id "
					+ " inner join lab.t_panel_details panel_details on "
					+ " 	panel_details.panel_id = panel_master.panel_id "
					+ " 	and ord_dtls.org_unit_id = panel_master.org_unit_id "
					+ " inner join lab.m_test_master test_mst on " + " 	test_mst.test_id = panel_details.test_id "
					+ " where " + " 	ord_dtls.order_id =:orderId " + " 	and ord_dtls.org_unit_id =:unitId "
					+ " 	and test_mst.org_unit_id =:unitId " + " 	and ord_dtls.is_order_visible_dept = 'A' "
					+ " 	and test_mst.test_status = 'A' " + " 	and (ord_dtls.ord_cancelled = 'N' "
					+ " 		and tcnd.credit_note_detail_id is null) ), ', ') ";

			List<String> testDesc = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("orderId",orderId)
					.setParameter("unitId", unitId)
					.list();
			String newString = "";
			for (String str : testDesc) {
				if (!str.isEmpty()) {
					newString = str;
				}
			}

			return newString;
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	public Response getPatHistoryByEncounterId(LabSampleMasterDto labSampleMasterDto) throws ApplicationException {
		try {
			EncounterMasterDto dto = (EncounterMasterDto) sessionFactory.getCurrentSession()
					.createSQLQuery(" select pat_history as \"patHistory\"  "
							+ " from opd.t_encounter_master  where encounter_id  =:encounterId ")
					.setParameter("encounterId", labSampleMasterDto.getEncounterId())
					.setResultTransformer(Transformers.aliasToBean(EncounterMasterDto.class)).uniqueResult();
			return new Response(SUCCESS, SUCCESS_200, null, null, dto);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	public Response updatePatHistoryByEncounterId(EncounterMasterDto encounterMasterDto) throws ApplicationException {

		try {
			Integer count = sessionFactory.getCurrentSession()
					.createSQLQuery(
							" update opd.t_encounter_master set pat_history=:patHistory "
									+" where encounter_id =:encounterId")
					.setParameter("patHistory", encounterMasterDto.getPatHistory())
					.setParameter("encounterId", encounterMasterDto.getEncounterId())
					.executeUpdate();
			return new Response(SUCCESS, SUCCESS_200, null, null, count);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	public Response getPhlebotomistList(LabSampleMasterDto labSampleMasterDto) throws ApplicationException {
		try {
			List<UserMasterDto> list = sessionFactory.getCurrentSession()
					.createSQLQuery(" select mem.user_id  as \"userId\" ,"
							+ "concat(coalesce(mem.first_name), ' ', coalesce(mem.middle_name), ' ', coalesce(mem.last_name)) as \"userName\" "
							+ "from m_employee_master mem  "
							+ "left join m_user_master mum  on mum.user_id  = mem.user_id  "
							+ "inner join t_user_unit_mapper tuum  on mum.user_id  = tuum.user_id "
							+ "inner join t_user_role_mapper turm  on turm.user_id =  mum.user_id "
							+ "inner join m_role_master mrm  on mrm.role_id  = turm.role_id  "
							+ "where mrm.role_type_id  = 15  and tuum.unit_id =:orgUnitId "
							+ " and mem.organization_id =:orgId " + "  and mem.status  = 'A' "
							+ " union "
							+ "select mdm.user_id  as \"userId\" ,"
							+ "concat(coalesce(mdm.first_name), ' ', coalesce(mdm.middle_name), ' ', coalesce(mdm.last_name)) as \"userName\" "
							+ "from doctor.m_doctor_master mdm  "
							+ "left join m_user_master mum  on mum.user_id  = mdm.user_id  "
							+ "inner join t_user_unit_mapper tuum  on mum.user_id  = tuum.user_id "
							+ "inner join t_user_role_mapper turm  on turm.user_id =  mum.user_id "
							+ "inner join m_role_master mrm  on mrm.role_id  = turm.role_id  "
							+ "where mrm.role_type_id  = 15  and mdm.status  = 'A' and tuum.unit_id =:orgUnitId "
							+ " and mdm.organization_id =:orgId ")
					.setParameter("orgId", labSampleMasterDto.getOrgId())
					.setParameter("orgUnitId", labSampleMasterDto.getOrgUnitId())
					.setResultTransformer(Transformers.aliasToBean(UserMasterDto.class))

					.list();
			return new Response(SUCCESS, SUCCESS_200, null, list, null);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}


	@Override
	public Response userAllocationHomeCollectionSearch(SearchDto searchDto) throws ApplicationException {

		try {
			List<Integer>  visitTypeIds = new ArrayList<Integer>();
			if (searchDto.getIsPreOrPostBilling() == 'Y') {
				List<Integer> orderStatusIds = new ArrayList<Integer>();
				if (searchDto.getOrderStatusId() != null && searchDto.getOrderStatusId() != 5) {
					orderStatusIds = (searchDto.getOrderStatusId() != null && searchDto.getOrderStatusId() > 0)
							? Arrays.asList(searchDto.getOrderStatusId())
							: PENDING_INPROGRESS_RENDER_CANCEL;
				} else if (searchDto.getOrderStatusId() != null && searchDto.getOrderStatusId() == 5) {
					orderStatusIds = Arrays.asList(42);
				} else if (searchDto.getOrderStatusId() == null) {
					orderStatusIds = PENDING_INPROGRESS_RENDER_CANCEL;
				}
				StringBuilder sb = new StringBuilder();
				sb.append("""
						select distinct(ord_mst.order_id) as "orderId",
							ord_mst.org_id as "orgId",
							ord_mst.org_unit_id as "orgUnitId",
							ord_mst.order_status_id as "orderStatusId",
							ord_mst.visit_type_id as "visitTypeId",
							visit_type_mst.visit_type_code as "visitType",
							ord_mst.ord_doctor_id as "doctorId",
							ord_mst.patient_id as "patientId",
							ord_mst.admission_id as "admissionId",
							ord_mst.encounter_id as "encounterId",
							ord_mst.created_by as "createdBy",
							ord_mst.created_date as "createdDate",
							 to_char(ord_mst.created_date, 'DD/MM/YYYY HH24:MI:SS') AS "createdDateString",
							pati_mst.uhid_number as uhid,
							pati_mst.is_vip as "isVip",
							pati_mst.mobile_number as "mobileNo",
							(case
									when pati_mst.is_vip = 'Y' then coalesce(pati_mst.alise_name, pati_mst.first_name) || ' ' || calculate_age(pati_mst.dob) || '/' || gen_mst.gender_code
									else pre_mst.prefix_code || ' ' || pati_mst.first_name || ' ' || coalesce(pati_mst.middle_name) || ' ' || pati_mst.last_name || ' ' || calculate_age(pati_mst.dob) || '/' || gen_mst.gender_code
								end ) as "patientDetails",
							gen_mst.gender_id as "genderId",
							'Dr.' || doct_mst.first_name || ' ' || doct_mst.last_name || ' (' || speciality_master.speciality_name || ')' as "doctorDetails",
							'' as "age",
							enc.encounter_type_id as "encounterTypeId",
							enc.pat_history as "patHistory",
							pati_mst.image_path as "imagePath",
							coalesce(enc.is_critical,'N') as "isCritical",
							reflMst.referral_name as "referralName",
							pDtls.address as "address",
							collection.phlebotomist_home_collection_id as "phlebotomistHomeCollectionId",
							collection.user_id as "userId",
							 to_char(collection.phlebotomist_collection_date,'dd/MM/yyyy') as "phlebotomistCollectionDate",
							to_char(collection.from_time,'HH24:MI:SS') as "fromTime",
							to_char(collection.to_time,'HH24:MI:SS') as "toTime",
							collection.status_id as "statusId",
							bill_mst.bill_status_id as "billStatusId",
							(case when ord_mst.encounter_id > 0 then
							( tpsd.payment_entitlement_id )
							else ( tpsd2.payment_entitlement_id ) end ) as "paymentEntitlementId"
							from
								public.t_order_master ord_mst
							inner join public.t_order_details ord_details on
								ord_details.order_id = ord_mst.order_id
							inner join opd.t_encounter_master enc on
								ord_mst.encounter_id = enc.encounter_id
							inner join patient.t_patient_registration pati_mst on
								ord_mst.patient_id = pati_mst.patient_id
							inner join public.m_gender_master gen_mst on
								gen_mst.gender_id = pati_mst.gender_id
							inner join public.m_prefix_master pre_mst on
								pre_mst.prefix_id = pati_mst.prefix_id
							inner join public.m_visit_type_master visit_type_mst on
								visit_type_mst.visit_type_id = ord_mst.visit_type_id
							inner join service.t_unit_service_master_details service_dtls on
								ord_details.service_id = service_dtls.service_master_id
								and service_dtls.status = 'A'
							inner join lab.m_test_master test_mst on
								ord_details.service_id = test_mst.service_id and test_mst.test_status = 'A'
							left join billing.t_billing_details bill_dtls on
								ord_details.order_details_id = bill_dtls.order_details_id
							left join billing.m_billing_master bill_mst on
								bill_mst.billing_master_id = bill_dtls.billing_master_id and bill_mst.bill_cancelled = 'N'
							left join doctor.m_doctor_master doct_mst on
								ord_mst.ord_doctor_id = doct_mst.doctor_id
							left join adt.t_admission ta  on ta.admission_id = ord_mst.admission_id and ta.status='A'
							left join opd.t_patient_sponsor_details tpsd
								on enc.encounter_id = tpsd.encounter_id and tpsd.priority_id = 1 and tpsd.status='A'
							left join opd.t_patient_sponsor_details tpsd2
								on tpsd2.admission_id = ta.admission_id and tpsd2.priority_id = 1 and tpsd2.status='A'
							left join doctor.m_speciality_master speciality_master on
								speciality_master.speciality_id = ord_mst.ord_doc_spl_id
							left join public.t_referal_details refrDtls on
								refrDtls.encounter_id = enc.encounter_id
							left join adt.m_referral_master reflMst on
								reflMst.referral_id = refrDtls.referral_id
							left join patient.t_patient_details pDtls on pDtls.patient_id = pati_mst.patient_id
							left join lab.t_phlebotomist_home_collection collection on collection.order_id = ord_mst.order_id
							left join billing.t_credit_note_detail tcnd on
							tcnd.order_detail_id = ord_details.order_details_id and tcnd.status = 'A'
							and tcnd.cancel_or_discount = 1
							where
							ord_mst.org_id =:orgId
							and ord_mst.org_unit_id =:orgUnitId
							and ord_mst.order_status = 'A'
								and ord_details.service_rendered in (0, 1)
								and
								(case
									when ord_mst.visit_type_id = 1 then ord_details.service_is_billed = 1
									else ord_details.service_is_billed in(0, 1)
								end )
								and
								(case
									when ord_mst.visit_type_id = 1 then ( bill_mst.bill_status_id is not null  and  bill_mst.bill_status_id in( 2, 3))
									else (bill_mst.bill_status_id is null
										or bill_mst.bill_status_id in(1, 2, 3))
								end )
							and ord_details.ord_cancelled = 'N'
							and ord_details.is_outsourced = 'N'
							and ord_details.status = 'A'
							 and ord_details.is_order_visible_dept='A'
							and ord_mst.home_collection = true
							and (ord_details.status = 'A' and tcnd.credit_note_detail_id is null)
						"""
				);

				if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0) {
					sb.append(" AND pati_mst.patient_id = :patientId ");
				}
				if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size() > 0) {
					visitTypeIds = searchDto.getVisitTypes().stream()
						.map(obj -> obj.getId())
						.collect(Collectors.toList());
					sb.append(" AND visit_type_mst.visit_type_id IN (:visitTypeIds)");
				}
				if (searchDto.getFromDt() != null) {
					sb.append("	and to_char(ord_details.order_date,'YYYY-MM-DD') >=:fromDt ");
				}
				if (searchDto.getToDt() != null) {
					sb.append("	and to_char(ord_details.order_date,'YYYY-MM-DD') <= :toDt ");
				}

				if (searchDto.getUhidNumber() != "") {
					sb.append(" AND lower(pati_mst.uhid_number) LIKE concat('%',LOWER(:uhidNumber "
							+ "),'%') ");
				}

				if (searchDto.getOrderStatusId() != null && searchDto.getOrderStatusId() == 5) {
					sb.append(" and (ord_mst.order_status_id <> 3 and collection.status_id = 42) ");
				}
				if (searchDto.getOrderStatusId() != null && searchDto.getOrderStatusId() == 1) {
					sb.append(" and ord_mst.order_status_id in (:orderStatusId) and collection.status_id is null ");
				}

				if (searchDto.getOrderStatusId() != null && searchDto.getOrderStatusId() == 3) {
					sb.append(" and (ord_mst.order_status_id = 3 and collection.status_id = 42) ");
				}

				sb.append(" order by " + "\"isCritical\" desc, " + "	\"createdDate\" DESC	");

				StringBuilder newSbForCount = sb;
				int count = 0;

				Query createdQuery = sessionFactory.getCurrentSession().createSQLQuery(sb.toString());

				if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0)
					createdQuery.setParameter("patientId", searchDto.getPatientId());

				if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size() > 0)
					createdQuery.setParameterList("visitTypeIds", visitTypeIds);

				if (searchDto.getFromDt() != null)
					createdQuery.setParameter("fromDt", searchDto.getFromDt());

				if (searchDto.getToDt() != null)
					createdQuery.setParameter("toDt", searchDto.getToDt());

				if (searchDto.getUhidNumber() != "")
					createdQuery.setParameter("uhidNumber", searchDto.getUhidNumber());

				if (searchDto.getOrderStatusId() != null && searchDto.getOrderStatusId() == 1)
					createdQuery.setParameterList("orderStatusId", orderStatusIds);


				List<LabSampleMasterDto> listLabSampleMasterDto = createdQuery.setParameter("orgId", searchDto.getOrgId())
						.setParameter("orgUnitId", searchDto.getOrgUnitId())
						.setFirstResult(searchDto.getOffset() != null ? searchDto.getOffset() : 0)
						.setMaxResults(searchDto.getRecordPerPage() != null ? searchDto.getRecordPerPage() : 50)
						.setResultTransformer(Transformers.aliasToBean(LabSampleMasterDto.class)).list();

				if (listLabSampleMasterDto.isEmpty()) {
					return new Response(SUCCESS, SUCCESS_200_MESSAGE, "No Records Found.", listLabSampleMasterDto,
							count);
				} else {
					Query createdQueryForCount = sessionFactory.getCurrentSession()
							.createSQLQuery(newSbForCount.toString());

					if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0)
						createdQueryForCount.setParameter("patientId", searchDto.getPatientId());

					if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size() > 0)
						createdQueryForCount.setParameterList("visitTypeIds", visitTypeIds);

					if (searchDto.getFromDt() != null)
						createdQueryForCount.setParameter("fromDt", searchDto.getFromDt());

					if (searchDto.getToDt() != null)
						createdQueryForCount.setParameter("toDt", searchDto.getToDt());

					if (searchDto.getUhidNumber() != "")
						createdQueryForCount.setParameter("uhidNumber", searchDto.getUhidNumber());

					if (searchDto.getOrderStatusId() != null && searchDto.getOrderStatusId() == 1)
						createdQueryForCount.setParameterList("orderStatusId", orderStatusIds);

					List<LabSampleMasterDto> listLabSampleMasterListForCount = createdQueryForCount
							.setParameter("orgId", searchDto.getOrgId()).setParameter("orgUnitId", searchDto.getOrgUnitId())
							.setResultTransformer(Transformers.aliasToBean(LabSampleMasterDto.class)).list();
					if (listLabSampleMasterListForCount != null && listLabSampleMasterListForCount.size() > 0) {
						count = listLabSampleMasterListForCount.size();
					}
					return new Response(SUCCESS, SUCCESS_200_MESSAGE, null, listLabSampleMasterDto, count);
				}
			} else {

				List<Integer> orderStatusIds = new ArrayList<Integer>();
				if (searchDto.getOrderStatusId() != null && searchDto.getOrderStatusId() != 5) {
					orderStatusIds = (searchDto.getOrderStatusId() != null && searchDto.getOrderStatusId() > 0)
							? Arrays.asList(searchDto.getOrderStatusId())
							: PENDING_INPROGRESS_RENDER_CANCEL;
				} else if (searchDto.getOrderStatusId() != null && searchDto.getOrderStatusId() == 5) {
					orderStatusIds = Arrays.asList(42);
				} else if (searchDto.getOrderStatusId() == null) {
					orderStatusIds = PENDING_INPROGRESS_RENDER_CANCEL;
				}

				StringBuilder sb = new StringBuilder();
				sb.append("select " + "	distinct(ord_mst.order_id) as \"orderId\", "
						+ "	ord_mst.org_id as \"orgId\", " + "	ord_mst.org_unit_id as \"orgUnitId\", "
						+ "	ord_mst.order_status_id as \"orderStatusId\", "
						+ "	ord_mst.visit_type_id as \"visitTypeId\", "
						+ "	visit_type_mst.visit_type_code as \"visitType\", "
						+ "	ord_mst.ord_doctor_id as \"doctorId\", " + "	ord_mst.patient_id as \"patientId\", "
						+ "	ord_mst.admission_id as \"admissionId\", " + "	ord_mst.encounter_id as \"encounterId\", "
						+ "	ord_mst.created_by as \"createdBy\", " + "	ord_mst.created_date as \"createdDate\", "
						+ "	pati_mst.uhid_number as uhid, " + " pati_mst.is_vip as \"isVip\", "
						+ " pati_mst.mobile_number as \"mobileNo\", " + " (case "
						+ " 		when pati_mst.is_vip = 'Y' then coalesce(pati_mst.alise_name, pati_mst.first_name) || ' ' || calculate_age(pati_mst.dob) || '/' || gen_mst.gender_code "
						+ "         else pre_mst.prefix_code || ' ' || pati_mst.first_name || ' ' || coalesce(pati_mst.middle_name) || ' ' || pati_mst.last_name || ' ' || calculate_age(pati_mst.dob) "
						+ " || '/' || gen_mst.gender_code "
						+ " 	end ) as \"patientDetails\", " + "	gen_mst.gender_id as \"genderId\", "
						+ "	'Dr.' || doct_mst.first_name || ' ' || doct_mst.last_name || ' (' || speciality_master.speciality_name || ')' as \"doctorDetails\", "
						+ " concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, pati_mst.dob))) as \"age\", "
						+ "	enc.encounter_type_id as \"encounterTypeId\", "
						+ "	coalesce(enc.is_critical,'N') as \"isCritical\", "
						+ " reflMst.referral_name as \"referralName\", " + " pDtls.address as \"address\", "
						+ " collection.phlebotomist_home_collection_id as \"phlebotomistHomeCollectionId\","
						+ " collection.user_id as \"userId\","
						+ "  to_char(collection.phlebotomist_collection_date,'dd/MM/yyyy') as \"phlebotomistCollectionDate\", "
						+ " to_char(collection.from_time,'HH24:MI:SS') as \"fromTime\", "
						+ " to_char(collection.to_time,'HH24:MI:SS') as \"toTime\", "
						+ " collection.status_id as \"statusId\", " + " (case when ord_mst.encounter_id > 0 then "
						+ " ( tpsd.payment_entitlement_id ) "
						+ " else ( tpsd2.payment_entitlement_id ) end ) as \"paymentEntitlementId\" " + "from "
						+ "	public.t_order_master ord_mst " + "inner join public.t_order_details ord_details on "
						+ "	ord_details.order_id = ord_mst.order_id " + "inner join opd.t_encounter_master enc on "
						+ "	 ord_mst.encounter_id = enc.encounter_id "
						+ "inner join patient.t_patient_registration pati_mst on "
						+ "	ord_mst.patient_id = pati_mst.patient_id " + "inner join public.m_gender_master gen_mst on "
						+ "	gen_mst.gender_id = pati_mst.gender_id " + "inner join public.m_prefix_master pre_mst on "
						+ "	pre_mst.prefix_id = pati_mst.prefix_id "
						+ "inner join public.m_visit_type_master visit_type_mst on "
						+ "	visit_type_mst.visit_type_id = ord_mst.visit_type_id "
						+ "inner join service.t_unit_service_master_details service_dtls on "
						+ "	ord_details.service_id = service_dtls.service_master_id "
						+ "	and service_dtls.status = 'A' " + "inner join public.t_lab_configuration lab_config on "
						+ "	lab_config.organisation_id = :orgId " + "	and lab_config.unit_id = :orgUnitId "
						+ "inner join lab.m_test_master test_mst on "
						+ "	ord_details.service_id = test_mst.service_id " + "	and test_mst.test_status = 'A' "
						+ "	and test_mst.is_histo_cyto in ('N','S') " + "left join doctor.m_doctor_master doct_mst on "
						+ "	ord_mst.ord_doctor_id = doct_mst.doctor_id "
						+ " left join adt.t_admission ta  on ta.admission_id = ord_mst.admission_id and ta.status='A' "
						+ " left join opd.t_patient_sponsor_details tpsd "
						+ "		on enc.encounter_id = tpsd.encounter_id and tpsd.priority_id = 1 and tpsd.status='A' "
						+ " left join opd.t_patient_sponsor_details tpsd2 "
						+ "		on tpsd2.admission_id = ta.admission_id and tpsd2.priority_id = 1 and tpsd2.status='A' "
						+ "left join doctor.m_speciality_master speciality_master on "
						+ "	speciality_master.speciality_id = ord_mst.ord_doc_spl_id "
						+ " left join public.t_referal_details refrDtls on "
						+ "		refrDtls.encounter_id = enc.encounter_id "
						+ " left join adt.m_referral_master reflMst on "
						+ " 	reflMst.referral_id = refrDtls.referral_id "
						+ " left join patient.t_patient_details pDtls on pDtls.patient_id = pati_mst.patient_id "
						+ " left join lab.t_phlebotomist_home_collection collection on collection.order_id = ord_mst.order_id "
						+ "where " + "	ord_mst.org_id =:orgId " + "	and ord_mst.org_unit_id =:orgUnitId "
						+ "	and ord_mst.order_status = 'A' " + " and ord_mst.home_collection = true "
						+ "	and ord_mst.visit_type_id =:visitTypeId "
						+ "	and ord_details.service_rendered in (0,1) " + "	and ord_details.ord_cancelled = 'N' "
						+ "	and ord_details.is_outsourced = 'N' " + "	and ord_details.status = 'A' ");

				if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0) {
					sb.append(" AND pati_mst.patient_id =:patientId ");
				}
				if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size() > 0) {
					sb.append(" AND visit_type_mst.visit_type_id IN (:visitTypeIds)");
				}
				if (searchDto.getFromDt() != null) {
					sb.append("	and to_char(ord_mst.created_date,'YYYY-MM-DD') >= :fromDt ");
				}
				if (searchDto.getToDt() != null) {
					sb.append("	and to_char(ord_mst.created_date,'YYYY-MM-DD') <=:toDt ");
				}
				if (searchDto.getOrderStatusId() != null && searchDto.getOrderStatusId() == 5) {
					sb.append(" and (ord_mst.order_status_id <> 3 and collection.status_id = 42) ");
				}
				if (searchDto.getOrderStatusId() != null && searchDto.getOrderStatusId() == 1) {
					sb.append(" and ord_mst.order_status_id in (:orderStatusId) and collection.status_id is null ");
				}

				if (searchDto.getOrderStatusId() != null && searchDto.getOrderStatusId() == 3) {
					sb.append(" and (ord_mst.order_status_id = 3 and collection.status_id = 42) ");
				}
				sb.append(" union " + "select " + "	distinct(ord_mst.order_id) as \"orderId\", "
						+ "	ord_mst.org_id as \"orgId\", " + "	ord_mst.org_unit_id as \"orgUnitId\", "
						+ "	ord_mst.order_status_id as \"orderStatusId\", "
						+ "	ord_mst.visit_type_id as \"visitTypeId\", "
						+ "	visit_type_mst.visit_type_code as \"visitType\", "
						+ "	ord_mst.ord_doctor_id as \"doctorId\", " + "	ord_mst.patient_id as \"patientId\", "
						+ "	ord_mst.admission_id as \"admissionId\", " + "	ord_mst.encounter_id as \"encounterId\", "
						+ "	ord_mst.created_by as \"createdBy\", " + "	ord_mst.created_date as \"createdDate\", "
						+ "	pati_mst.uhid_number as uhid, " + " pati_mst.is_vip as \"isVip\", "
						+ " pati_mst.mobile_number as \"mobileNo\", " + " (case "
						+ " 		when pati_mst.is_vip = 'Y' then coalesce(pati_mst.alise_name, pati_mst.first_name) || ' ' || calculate_age(pati_mst.dob) || '/' || gen_mst.gender_code "
						+ "         else pre_mst.prefix_code || ' ' || pati_mst.first_name || ' ' || coalesce(pati_mst.middle_name) || ' ' || pati_mst.last_name || ' ' || "
						+ " calculate_age(pati_mst.dob) || '/' || gen_mst.gender_code "
						+ " 	end ) as \"patientDetails\", " + "	gen_mst.gender_id as \"genderId\", "
						+ "	'Dr.' || doct_mst.first_name || ' ' || doct_mst.last_name || ' (' || speciality_master.speciality_name || ')' as \"doctorDetails\", "
						+ " concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, pati_mst.dob))) as \"age\", "
						+ "	enc.encounter_type_id as \"encounterTypeId\", "
						+ "	coalesce(enc.is_critical,'N') as \"isCritical\", "
						+ " reflMst.referral_name as \"referralName\", " + " pDtls.address as \"address\", "
						+ " collection.phlebotomist_home_collection_id as \"phlebotomistHomeCollectionId\","
						+ " collection.user_id as \"userId\","
						+ "  to_char(collection.phlebotomist_collection_date,'dd/MM/yyyy') as \"phlebotomistCollectionDate\", "
						+ " to_char(collection.from_time,'HH24:MI:SS') as \"fromTime\", "
						+ " to_char(collection.to_time,'HH24:MI:SS') as \"toTime\", "
						+ " collection.status_id as \"statusId\", " + " (case when ord_mst.encounter_id > 0 then "
						+ " ( tpsd.payment_entitlement_id ) "
						+ " else ( tpsd2.payment_entitlement_id ) end ) as \"paymentEntitlementId\" " + "from "
						+ "	public.t_order_master ord_mst " + "inner join public.t_order_details ord_details on "
						+ "	ord_details.order_id = ord_mst.order_id " + "inner join opd.t_encounter_master enc on "
						+ "	 ord_mst.encounter_id = enc.encounter_id "
						+ "inner join patient.t_patient_registration pati_mst on "
						+ "	ord_mst.patient_id = pati_mst.patient_id " + "inner join public.m_gender_master gen_mst on "
						+ "	gen_mst.gender_id = pati_mst.gender_id " + "inner join public.m_prefix_master pre_mst on "
						+ "	pre_mst.prefix_id = pati_mst.prefix_id "
						+ "inner join public.m_visit_type_master visit_type_mst on "
						+ "	visit_type_mst.visit_type_id = ord_mst.visit_type_id "
						+ "inner join service.t_unit_service_master_details service_dtls on "
						+ "	ord_details.service_id = service_dtls.service_master_id "
						+ "	and service_dtls.status = 'A' " + "inner join public.t_lab_configuration lab_config on "
						+ "	lab_config.organisation_id = :orgId " + "	and lab_config.unit_id = :orgUnitId "
						+ "left join doctor.m_doctor_master doct_mst on "
						+ "	ord_mst.ord_doctor_id = doct_mst.doctor_id "
						+ " left join adt.t_admission ta  on ta.admission_id = ord_mst.admission_id and ta.status='A' "
						+ " left join opd.t_patient_sponsor_details tpsd "
						+ "		on enc.encounter_id = tpsd.encounter_id and tpsd.priority_id = 1 and tpsd.status='A' "
						+ " left join opd.t_patient_sponsor_details tpsd2 "
						+ "		on tpsd2.admission_id = ta.admission_id and tpsd2.priority_id = 1 and tpsd2.status='A' "
						+ "left join doctor.m_speciality_master speciality_master on "
						+ "	speciality_master.speciality_id = ord_mst.ord_doc_spl_id "
						+ " left join public.t_referal_details refrDtls on "
						+ "		refrDtls.encounter_id = enc.encounter_id "
						+ " left join adt.m_referral_master reflMst on "
						+ " 	reflMst.referral_id = refrDtls.referral_id "
						+ " left join patient.t_patient_details pDtls on pDtls.patient_id = pati_mst.patient_id "
						+ " left join lab.t_phlebotomist_home_collection collection on collection.order_id = ord_mst.order_id "
						+ "where " + "	ord_mst.org_id =:orgId " + "	and ord_mst.org_unit_id =:orgUnitId "
						+ "	and ord_mst.order_status = 'A' " + " and ord_mst.home_collection = true "
						+ "	and ord_mst.visit_type_id =:visitTypeId "
						+ "	and ord_details.service_rendered in (0,1) " + "	and ord_details.ord_cancelled = 'N' "
						+ "	and ord_details.is_outsourced = 'N' " + "	and ord_details.status = 'A' "
						+ "	and service_dtls.is_panel = 'Y' " + " and ord_details.is_order_visible_dept='A' ");

				if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0) {
					sb.append(" AND pati_mst.patient_id =:patientId ");
				}
				if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size() > 0) {
					visitTypeIds = searchDto.getVisitTypes().stream()
							.map(obj -> obj.getId())
							.collect(Collectors.toList());
					sb.append(" AND visit_type_mst.visit_type_id IN (:visitTypeIds)");
				}
				if (searchDto.getFromDt() != null) {
					sb.append("	and to_char(ord_details.order_date,'YYYY-MM-DD') >= :fromDt ");
				}
				if (searchDto.getToDt() != null) {
					sb.append("	and to_char(ord_details.order_date,'YYYY-MM-DD') <=:toDt ");
				}
				if (searchDto.getOrderStatusId() != null && searchDto.getOrderStatusId() == 5) {
					sb.append(" and (ord_mst.order_status_id <> 3 and collection.status_id = 42) ");
				}
				if (searchDto.getOrderStatusId() != null && searchDto.getOrderStatusId() == 1) {
					sb.append(" and ord_mst.order_status_id in (:orderStatusId) and collection.status_id is null ");
				}

				if (searchDto.getOrderStatusId() != null && searchDto.getOrderStatusId() == 3) {
					sb.append(" and (ord_mst.order_status_id = 3 and collection.status_id = 42) ");
				}
				sb.append(" order by " + "\"isCritical\" desc, " + "	\"createdDate\" DESC	");
				StringBuilder newSbForCount = sb;
				int count = 0;
				Query createdQuery = sessionFactory.getCurrentSession().createSQLQuery(sb.toString());

				if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0)
					createdQuery.setParameter("patientId", searchDto.getPatientId());

				if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size() > 0)
					createdQuery.setParameterList("visitTypeIds", visitTypeIds);

				if (searchDto.getFromDt() != null)
					createdQuery.setParameter("fromDt", searchDto.getFromDt());

				if (searchDto.getToDt() != null)
					createdQuery.setParameter("toDt", searchDto.getToDt());

				if (searchDto.getOrderStatusId() != null && searchDto.getOrderStatusId() == 1)
					createdQuery.setParameterList("orderStatusId", orderStatusIds);

				List<LabSampleMasterDto> listLabSampleMasterDto = createdQuery.setParameter("orgId", searchDto.getOrgId())
						.setParameter("orgUnitId", searchDto.getOrgUnitId()).setParameter("visitTypeId", ICommonConstants.OP)
						.setFirstResult(searchDto.getOffset() != null ? searchDto.getOffset() : 0)
						.setMaxResults(searchDto.getRecordPerPage() != null ? searchDto.getRecordPerPage() : 50)
						.setResultTransformer(Transformers.aliasToBean(LabSampleMasterDto.class)).list();

				if (listLabSampleMasterDto.isEmpty()) {
					return new Response(SUCCESS, SUCCESS_200_MESSAGE, "No Records Found.", listLabSampleMasterDto,
							count);
				} else {
					Query createdQueryForCount = sessionFactory.getCurrentSession()
							.createSQLQuery(newSbForCount.toString());

					if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0)
						createdQueryForCount.setParameter("patientId", searchDto.getPatientId());

					if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size() > 0)
						createdQueryForCount.setParameterList("visitTypeIds", visitTypeIds);

					if (searchDto.getFromDt() != null)
						createdQueryForCount.setParameter("fromDt", searchDto.getFromDt());

					if (searchDto.getToDt() != null)
						createdQueryForCount.setParameter("toDt", searchDto.getToDt());

					if (searchDto.getOrderStatusId() != null && searchDto.getOrderStatusId() == 1)
						createdQueryForCount.setParameterList("orderStatusId", orderStatusIds);


					List<LabSampleMasterDto> listLabSampleMasterListForCount = createdQueryForCount
							.setParameter("orgId", searchDto.getOrgId()).setParameter("orgUnitId", searchDto.getOrgUnitId())
							.setParameter("visitTypeId", ICommonConstants.OP)
							.setResultTransformer(Transformers.aliasToBean(LabSampleMasterDto.class)).list();
					if (listLabSampleMasterListForCount != null && listLabSampleMasterListForCount.size() > 0) {
						count = listLabSampleMasterListForCount.size();
					}
					return new Response(SUCCESS, SUCCESS_200_MESSAGE, null, listLabSampleMasterDto, count);
				}

			}

		} catch (Exception e) {
			logger.error("Exection", e);
			throw e;
		}

	}

	@Override
	public Response labSampleHomeCollectionSearch(SearchDto searchDto) throws ApplicationException {
		try {
			List<Integer>  visitTypes = new ArrayList<Integer>();
			if (searchDto.getIsPreOrPostBilling() == 'Y') {
				List<Integer> orderStatusIds = new ArrayList<Integer>();
				if (searchDto.getOrderStatusId() == null) {
					orderStatusIds = PENDING_INPROGRESS_RENDER_CANCEL;
				}
				StringBuilder sb = new StringBuilder();
				sb.append("select " + "	distinct(ord_mst.order_id) as \"orderId\", "
						+ "	ord_mst.org_id as \"orgId\", " + "	ord_mst.org_unit_id as \"orgUnitId\", "
						+ "	ord_mst.order_status_id as \"orderStatusId\", "
						+ "	ord_mst.visit_type_id as \"visitTypeId\", "
						+ "	visit_type_mst.visit_type_code as \"visitType\", "
						+ "	ord_mst.ord_doctor_id as \"doctorId\", " + "	ord_mst.patient_id as \"patientId\", "
						+ "	ord_mst.admission_id as \"admissionId\", " + "	ord_mst.encounter_id as \"encounterId\", "
						+ "	ord_mst.created_by as \"createdBy\", " + "	ord_mst.created_date as \"createdDate\", "
						+ "  to_char(ord_mst.created_date, 'DD/MM/YYYY HH24:MI:SS') AS \"createdDateString\",   "
						+ "	pati_mst.uhid_number as uhid, " + " pati_mst.is_vip as \"isVip\", "
						+ " pati_mst.mobile_number as \"mobileNo\", " + " (case "
						+ " 		when pati_mst.is_vip = 'Y' then coalesce(pati_mst.alise_name, pati_mst.first_name) || ' ' || "
						+ " calculate_age(pati_mst.dob) || '/' || gen_mst.gender_code "
						+ "         else pre_mst.prefix_code || ' ' || pati_mst.first_name || ' ' || coalesce(pati_mst.middle_name) || ' ' || pati_mst.last_name || ' ' || "
						+ " calculate_age(pati_mst.dob) || '/' || gen_mst.gender_code "
						+ " 	end ) as \"patientDetails\", " + "	gen_mst.gender_id as \"genderId\", "
						+ "	'Dr.' || doct_mst.first_name || ' ' || doct_mst.last_name || ' (' || speciality_master.speciality_name || ')' as \"doctorDetails\", "
						+ " '' as \"age\", "
						+ "	enc.encounter_type_id as \"encounterTypeId\", " + "	enc.pat_history as \"patHistory\", "
						+ " pati_mst.image_path as \"imagePath\","
						+ "	coalesce(enc.is_critical,'N') as \"isCritical\", "
						+ " reflMst.referral_name as \"referralName\", " + " pDtls.address as \"address\", "
						+ " collection.phlebotomist_home_collection_id as \"phlebotomistHomeCollectionId\","
						+ " collection.user_id as \"userId\","
						+ "  to_char(collection.phlebotomist_collection_date,'dd/MM/yyyy') as \"phlebotomistCollectionDate\", "
						+ " to_char(collection.from_time,'HH24:MI:SS') as \"fromTime\", "
						+ " to_char(collection.to_time,'HH24:MI:SS') as \"toTime\", "
						+ " collection.status_id as \"statusId\"," + " bill_mst.bill_status_id as \"billStatusId\", "
						+ " (case when ord_mst.encounter_id > 0 then " + " ( tpsd.payment_entitlement_id ) "
						+ " else ( tpsd2.payment_entitlement_id ) end ) as \"paymentEntitlementId\","
						+ " emp.first_name || ' ' || emp.last_name as \"assignedByName\" "
						+ "from "
						+ "	public.t_order_master ord_mst " + "inner join public.t_order_details ord_details on "
						+ "	ord_details.order_id = ord_mst.order_id " + "inner join opd.t_encounter_master enc on "
						+ "	 ord_mst.encounter_id = enc.encounter_id "
						+ "inner join patient.t_patient_registration pati_mst on "
						+ "	ord_mst.patient_id = pati_mst.patient_id " + "inner join public.m_gender_master gen_mst on "
						+ "	gen_mst.gender_id = pati_mst.gender_id " + "inner join public.m_prefix_master pre_mst on "
						+ "	pre_mst.prefix_id = pati_mst.prefix_id "
						+ "inner join public.m_visit_type_master visit_type_mst on "
						+ "	visit_type_mst.visit_type_id = ord_mst.visit_type_id "
						+ "inner join service.t_unit_service_master_details service_dtls on "
						+ "	ord_details.service_id = service_dtls.service_master_id "
						+ "	and service_dtls.status = 'A' "
						+ "inner join lab.m_test_master test_mst on "
						+ "	ord_details.service_id = test_mst.service_id " + "	and test_mst.test_status = 'A' "
						+ " inner join lab.t_phlebotomist_home_collection collection on collection.order_id = ord_mst.order_id "
						+ "left join billing.t_billing_details bill_dtls on "
						+ "	ord_details.order_details_id = bill_dtls.order_details_id "
						+ "left join billing.m_billing_master bill_mst on "
						+ "	bill_mst.billing_master_id = bill_dtls.billing_master_id and bill_mst.bill_cancelled = 'N' "
						+ "left join doctor.m_doctor_master doct_mst on "
						+ "	ord_mst.ord_doctor_id = doct_mst.doctor_id "
						+ " left join adt.t_admission ta  on ta.admission_id = ord_mst.admission_id and ta.status='A' "
						+ " left join opd.t_patient_sponsor_details tpsd "
						+ "		on enc.encounter_id = tpsd.encounter_id and tpsd.priority_id = 1 and tpsd.status='A' "
						+ " left join opd.t_patient_sponsor_details tpsd2 "
						+ "		on tpsd2.admission_id = ta.admission_id and tpsd2.priority_id = 1 and tpsd2.status='A' "
						+ "left join doctor.m_speciality_master speciality_master on "
						+ "	speciality_master.speciality_id = ord_mst.ord_doc_spl_id "
						+ " left join public.t_referal_details refrDtls on "
						+ "		refrDtls.encounter_id = enc.encounter_id "
						+ " left join adt.m_referral_master reflMst on "
						+ " 	reflMst.referral_id = refrDtls.referral_id "
						+ " left join patient.t_patient_details pDtls on pDtls.patient_id = pati_mst.patient_id "
						+ " left join m_employee_master emp on emp.user_id = collection.created_by "
						+ "where " + "	ord_mst.org_id =:orgId " + "	and ord_mst.org_unit_id =:orgUnitId "
						+ "	and ord_mst.order_status = 'A' "
						+ "	and ord_details.service_rendered in (0,1) "
						+ " 	and " + " 	(case "
						+ " 		when ord_mst.visit_type_id = 1 then ord_details.service_is_billed = 1 "
						+ " 		else ord_details.service_is_billed in(0, 1) " + " 	end ) " + " 	and "
						+ " 	(case "
						+ " 		when ord_mst.visit_type_id = 1 then ( bill_mst.bill_status_id is not null  and  bill_mst.bill_status_id in( 2, 3)) "
						+ " 		else (bill_mst.bill_status_id is null "
						+ " 			or bill_mst.bill_status_id in(1, 2, 3)) " + " 	end ) "
						+ "	and ord_details.ord_cancelled = 'N' " + "	and ord_details.is_outsourced = 'N' "
						+ "	and ord_details.status = 'A' " + "  and ord_details.is_order_visible_dept='A' "
						+ " and ord_mst.home_collection = true " + " and collection.user_id = :userId ");



				if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0) {
					sb.append( "AND pati_mst.patient_id = :patientId ");
				}

				if (searchDto.getVisitTypes() != null && !searchDto.getVisitTypes().isEmpty()) {
					visitTypes = searchDto.getVisitTypes().stream()
							.map(obj -> obj.getId())
							.collect(Collectors.toList());
					sb.append("AND visit_type_mst.visit_type_id IN (:visitTypes) ");
				}

				if (searchDto.getFromDt() != null) {
					sb.append("AND to_char(ord_details.order_date, 'YYYY-MM-DD') >= :fromDt ");
				}

				if (searchDto.getToDt() != null) {
					sb.append("AND to_char(ord_details.order_date, 'YYYY-MM-DD') <= :toDt ");
				}

				if (searchDto.getUhidNumber() != null && !searchDto.getUhidNumber().isEmpty()) {
					sb.append("AND lower(pati_mst.uhid_number) LIKE :uhidNumber ");
				}

				if (searchDto.getOrderStatusId() != null) {
					if (searchDto.getOrderStatusId() == 1) {
						sb.append("AND ord_mst.order_status_id = :orderStatusId1 ");
					} else if (searchDto.getOrderStatusId() == 3) {
						sb.append("AND ord_mst.order_status_id = :orderStatusId3 ");
					} else {
						sb.append("AND ord_mst.order_status_id IN :orderStatusIds ");
					}
				}
				sb.append(" order by " + "\"isCritical\" desc, " + "	\"createdDate\" DESC	");

				StringBuilder newSbForCount = sb;
				int count = 0;

				Query createdQuery = sessionFactory.getCurrentSession().createSQLQuery(sb.toString());

				if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0) {
					createdQuery.setParameter("patientId", searchDto.getPatientId());
				}

				if (searchDto.getVisitTypes() != null && !searchDto.getVisitTypes().isEmpty()) {

					List<Integer> visitTypeIds = searchDto.getVisitTypes()
							.stream()
							.map(obj -> obj.getId()) // Replace `getId()` with the correct method
							.collect(Collectors.toList());

					createdQuery.setParameterList("visitTypes", visitTypeIds);
				}

				if (searchDto.getFromDt() != null) {
					createdQuery.setParameter("fromDt", searchDto.getFromDt());
				}

				if (searchDto.getToDt() != null) {
					createdQuery.setParameter("toDt", searchDto.getToDt());
				}

				if (searchDto.getUhidNumber() != null && !searchDto.getUhidNumber().isEmpty()) {
					createdQuery.setParameter("uhidNumber", "%" + searchDto.getUhidNumber().toLowerCase() + "%");
				}

				if (searchDto.getOrderStatusId() != null) {
					if (searchDto.getOrderStatusId() == 1) {
						createdQuery.setParameter("orderStatusId1", 1);
					} else if (searchDto.getOrderStatusId() == 3) {
						createdQuery.setParameter("orderStatusId3", 3);
					} else {
						createdQuery.setParameter("orderStatusIds", orderStatusIds); // assuming 'orderStatusIds' is a list of IDs
					}
				}
				
				List<LabSampleMasterDto> listLabSampleMasterDto = createdQuery.setParameter("orgId", searchDto.getOrgId())
						.setParameter("orgUnitId", searchDto.getOrgUnitId())
						.setParameter("userId", searchDto.getUserId())
						.setFirstResult(searchDto.getOffset() != null ? searchDto.getOffset() : 0)
						.setMaxResults(searchDto.getRecordPerPage() != null ? searchDto.getRecordPerPage() : 50)
						.setResultTransformer(Transformers.aliasToBean(LabSampleMasterDto.class)).list();

				if (listLabSampleMasterDto.isEmpty()) {
					return new Response(SUCCESS, SUCCESS_200_MESSAGE, "No Records Found.", listLabSampleMasterDto,
							count);
				} else {
					Query createdQueryForCount = sessionFactory.getCurrentSession()
							.createSQLQuery(newSbForCount.toString());

					if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0) {
						createdQueryForCount.setParameter("patientId", searchDto.getPatientId());
					}

					if (searchDto.getVisitTypes() != null && !searchDto.getVisitTypes().isEmpty()) {
						createdQueryForCount.setParameter("visitTypes", visitTypes);
					}

					if (searchDto.getFromDt() != null) {
						createdQueryForCount.setParameter("fromDt", searchDto.getFromDt());
					}

					if (searchDto.getToDt() != null) {
						createdQueryForCount.setParameter("toDt", searchDto.getToDt());
					}

					if (searchDto.getUhidNumber() != null && !searchDto.getUhidNumber().isEmpty()) {
						createdQueryForCount.setParameter("uhidNumber", "%" + searchDto.getUhidNumber().toLowerCase() + "%");
					}

					if (searchDto.getOrderStatusId() != null) {
						if (searchDto.getOrderStatusId() == 1) {
							createdQueryForCount.setParameter("orderStatusId1", 1);
						} else if (searchDto.getOrderStatusId() == 3) {
							createdQueryForCount.setParameter("orderStatusId3", 3);
						} else {
							createdQueryForCount.setParameter("orderStatusIds", orderStatusIds); // assuming 'orderStatusIds' is a list of IDs
						}
					}

					List<LabSampleMasterDto> listLabSampleMasterListForCount = createdQueryForCount
							.setParameter("orgId", searchDto.getOrgId()).setParameter("orgUnitId", searchDto.getOrgUnitId())
							.setParameter("userId", searchDto.getUserId())
							.setResultTransformer(Transformers.aliasToBean(LabSampleMasterDto.class)).list();
					if (listLabSampleMasterListForCount != null && listLabSampleMasterListForCount.size() > 0) {
						count = listLabSampleMasterListForCount.size();
					}
					return new Response(SUCCESS, SUCCESS_200_MESSAGE, null, listLabSampleMasterDto, count);
				}
			} else {

				List<Integer> orderStatusIds = new ArrayList<Integer>();
				if (searchDto.getOrderStatusId() == null) {
					orderStatusIds = PENDING_INPROGRESS_RENDER_CANCEL;
				}
				StringBuilder sb = new StringBuilder();
				sb.append("select " + "	distinct(ord_mst.order_id) as \"orderId\", "
						+ "	ord_mst.org_id as \"orgId\", " + "	ord_mst.org_unit_id as \"orgUnitId\", "
						+ "	ord_mst.order_status_id as \"orderStatusId\", "
						+ "	ord_mst.visit_type_id as \"visitTypeId\", "
						+ "	visit_type_mst.visit_type_code as \"visitType\", "
						+ "	ord_mst.ord_doctor_id as \"doctorId\", " + "	ord_mst.patient_id as \"patientId\", "
						+ "	ord_mst.admission_id as \"admissionId\", " + "	ord_mst.encounter_id as \"encounterId\", "
						+ "	ord_mst.created_by as \"createdBy\", " + "	ord_mst.created_date as \"createdDate\", "
						+ "	pati_mst.uhid_number as uhid, " + " pati_mst.is_vip as \"isVip\", "
						+ " pati_mst.mobile_number as \"mobileNo\", " + " (case "
						+ " 		when pati_mst.is_vip = 'Y' then coalesce(pati_mst.alise_name, pati_mst.first_name) || ' ' || "
						+ " calculate_age(pati_mst.dob) || '/' || gen_mst.gender_code "
						+ "         else pre_mst.prefix_code || ' ' || pati_mst.first_name || ' ' || coalesce(pati_mst.middle_name) || ' ' || pati_mst.last_name || ' ' || "
						+ " calculate_age(pati_mst.dob) || '/' || gen_mst.gender_code "
						+ " 	end ) as \"patientDetails\", " + "	gen_mst.gender_id as \"genderId\", "
						+ "	'Dr.' || doct_mst.first_name || ' ' || doct_mst.last_name || ' (' || speciality_master.speciality_name || ')' as \"doctorDetails\", "
						+ " concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, pati_mst.dob))) as \"age\", "
						+ "	enc.encounter_type_id as \"encounterTypeId\", "
						+ "	coalesce(enc.is_critical,'N') as \"isCritical\", "
						+ " reflMst.referral_name as \"referralName\", " + " pDtls.address as \"address\", "
						+ " collection.phlebotomist_home_collection_id as \"phlebotomistHomeCollectionId\","
						+ " collection.user_id as \"userId\","
						+ "  to_char(collection.phlebotomist_collection_date,'dd/MM/yyyy') as \"phlebotomistCollectionDate\", "
						+ " to_char(collection.from_time,'HH24:MI:SS') as \"fromTime\", "
						+ " to_char(collection.to_time,'HH24:MI:SS') as \"toTime\", "
						+ " collection.status_id as \"statusId\", " + " (case when ord_mst.encounter_id > 0 then "
						+ " ( tpsd.payment_entitlement_id ) "
						+ " else ( tpsd2.payment_entitlement_id ) end ) as \"paymentEntitlementId\" ,"
						+ " emp.first_name || ' ' || emp.last_name as \"assignedByName\" "
						+ "from "
						+ "	public.t_order_master ord_mst " + "inner join public.t_order_details ord_details on "
						+ "	ord_details.order_id = ord_mst.order_id " + "inner join opd.t_encounter_master enc on "
						+ "	 ord_mst.encounter_id = enc.encounter_id "
						+ "inner join patient.t_patient_registration pati_mst on "
						+ "	ord_mst.patient_id = pati_mst.patient_id " + "inner join public.m_gender_master gen_mst on "
						+ "	gen_mst.gender_id = pati_mst.gender_id " + "inner join public.m_prefix_master pre_mst on "
						+ "	pre_mst.prefix_id = pati_mst.prefix_id "
						+ "inner join public.m_visit_type_master visit_type_mst on "
						+ "	visit_type_mst.visit_type_id = ord_mst.visit_type_id "
						+ "inner join service.t_unit_service_master_details service_dtls on "
						+ "	ord_details.service_id = service_dtls.service_master_id "
						+ "	and service_dtls.status = 'A' " + "inner join public.t_lab_configuration lab_config on "
						+ "	lab_config.organisation_id = :orgId " + "	and lab_config.unit_id = :orgUnitId "
						+ "inner join lab.m_test_master test_mst on "
						+ "	ord_details.service_id = test_mst.service_id " + "	and test_mst.test_status = 'A' "
						+ "	and test_mst.is_histo_cyto in ('N','S') "
						+ " inner join lab.t_phlebotomist_home_collection collection on collection.order_id = ord_mst.order_id "
						+ "left join doctor.m_doctor_master doct_mst on "
						+ "	ord_mst.ord_doctor_id = doct_mst.doctor_id "
						+ " left join adt.t_admission ta  on ta.admission_id = ord_mst.admission_id and ta.status='A' "
						+ " left join opd.t_patient_sponsor_details tpsd "
						+ "		on enc.encounter_id = tpsd.encounter_id and tpsd.priority_id = 1 and tpsd.status='A' "
						+ " left join opd.t_patient_sponsor_details tpsd2 "
						+ "		on tpsd2.admission_id = ta.admission_id and tpsd2.priority_id = 1 and tpsd2.status='A' "
						+ "left join doctor.m_speciality_master speciality_master on "
						+ "	speciality_master.speciality_id = ord_mst.ord_doc_spl_id "
						+ " left join public.t_referal_details refrDtls on "
						+ "		refrDtls.encounter_id = enc.encounter_id "
						+ " left join adt.m_referral_master reflMst on "
						+ " 	reflMst.referral_id = refrDtls.referral_id "
						+ " left join patient.t_patient_details pDtls on pDtls.patient_id = pati_mst.patient_id "
						+ " left join m_employee_master emp on emp.user_id = collection.created_by "
						+ " where " + "	ord_mst.org_id =:orgId " + "	and ord_mst.org_unit_id =:orgUnitId "
						+ "	and ord_mst.order_status = 'A' " + " and ord_mst.home_collection = true "
						+ " and collection.user_id = :userId " + "	and ord_mst.visit_type_id =:visitTypeId "
						+ "	and ord_details.service_rendered in (0,1) " + "	and ord_details.ord_cancelled = 'N' "
						+ "	and ord_details.is_outsourced = 'N' " + "	and ord_details.status = 'A' ");


				if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0) {
					sb.append(" AND pati_mst.patient_id =:patientId" );
				}
				if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size() > 0) {
					visitTypes = searchDto.getVisitTypes().stream()
							.map(obj -> obj.getId())
							.collect(Collectors.toList());
					sb.append(" AND visit_type_mst.visit_type_id IN (:visitTypes )");
				}
				if (searchDto.getFromDt() != null) {
					sb.append("	and to_char(ord_mst.created_date,'YYYY-MM-DD') >=:fromDate ");
				}
				if (searchDto.getToDt() != null) {
					sb.append("	and to_char(ord_mst.created_date,'YYYY-MM-DD') <=:toDate ");
				}

				if (searchDto.getOrderStatusId() != null && searchDto.getOrderStatusId() == 1) {
					sb.append(" and ord_mst.order_status_id in (:orderStatusIds) ");
				}

				if (searchDto.getOrderStatusId() != null && searchDto.getOrderStatusId() == 3) {
					sb.append(" and (ord_mst.order_status_id = 3) ");
				}

				if (searchDto.getOrderStatusId() == null) {
					sb.append(" and ord_mst.order_status_id in (:orderStatusIdsNew) ");
				}

				sb.append(" union " + "select " + "	distinct(ord_mst.order_id) as \"orderId\", "
						+ "	ord_mst.org_id as \"orgId\", " + "	ord_mst.org_unit_id as \"orgUnitId\", "
						+ "	ord_mst.order_status_id as \"orderStatusId\", "
						+ "	ord_mst.visit_type_id as \"visitTypeId\", "
						+ "	visit_type_mst.visit_type_code as \"visitType\", "
						+ "	ord_mst.ord_doctor_id as \"doctorId\", " + "	ord_mst.patient_id as \"patientId\", "
						+ "	ord_mst.admission_id as \"admissionId\", " + "	ord_mst.encounter_id as \"encounterId\", "
						+ "	ord_mst.created_by as \"createdBy\", " + "	ord_mst.created_date as \"createdDate\", "
						+ "	pati_mst.uhid_number as uhid, " + " pati_mst.is_vip as \"isVip\", "
						+ " pati_mst.mobile_number as \"mobileNo\", " + " (case "
						+ " 		when pati_mst.is_vip = 'Y' then coalesce(pati_mst.alise_name, pati_mst.first_name) || ' ' || calculate_age(pati_mst.dob) "
						+ " || '/' || gen_mst.gender_code "
						+ "         else pre_mst.prefix_code || ' ' || pati_mst.first_name || ' ' || coalesce(pati_mst.middle_name) || ' ' || pati_mst.last_name || ' ' || "
						+ " calculate_age(pati_mst.dob) || '/' || gen_mst.gender_code "
						+ " 	end ) as \"patientDetails\", " + "	gen_mst.gender_id as \"genderId\", "
						+ "	'Dr.' || doct_mst.first_name || ' ' || doct_mst.last_name || ' (' || speciality_master.speciality_name || ')' as \"doctorDetails\", "
						+ " concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, pati_mst.dob))) as \"age\", "
						+ "	enc.encounter_type_id as \"encounterTypeId\", "
						+ "	coalesce(enc.is_critical,'N') as \"isCritical\", "
						+ " reflMst.referral_name as \"referralName\", " + " pDtls.address as \"address\", "
						+ " collection.phlebotomist_home_collection_id as \"phlebotomistHomeCollectionId\","
						+ " collection.user_id as \"userId\","
						+ "  to_char(collection.phlebotomist_collection_date,'dd/MM/yyyy') as \"phlebotomistCollectionDate\", "
						+ " to_char(collection.from_time,'HH24:MI:SS') as \"fromTime\", "
						+ " to_char(collection.to_time,'HH24:MI:SS') as \"toTime\", "
						+ " collection.status_id as \"statusId\", " + " (case when ord_mst.encounter_id > 0 then "
						+ " ( tpsd.payment_entitlement_id ) "
						+ " else ( tpsd2.payment_entitlement_id ) end ) as \"paymentEntitlementId\", "
						+ " emp.first_name || ' ' || emp.last_name as \"assignedByName\" "
						+ "from "
						+ "	public.t_order_master ord_mst " + "inner join public.t_order_details ord_details on "
						+ "	ord_details.order_id = ord_mst.order_id " + "inner join opd.t_encounter_master enc on "
						+ "	 ord_mst.encounter_id = enc.encounter_id "
						+ "inner join patient.t_patient_registration pati_mst on "
						+ "	ord_mst.patient_id = pati_mst.patient_id " + "inner join public.m_gender_master gen_mst on "
						+ "	gen_mst.gender_id = pati_mst.gender_id " + "inner join public.m_prefix_master pre_mst on "
						+ "	pre_mst.prefix_id = pati_mst.prefix_id "
						+ "inner join public.m_visit_type_master visit_type_mst on "
						+ "	visit_type_mst.visit_type_id = ord_mst.visit_type_id "
						+ "inner join service.t_unit_service_master_details service_dtls on "
						+ "	ord_details.service_id = service_dtls.service_master_id "
						+ "	and service_dtls.status = 'A' " + "inner join public.t_lab_configuration lab_config on "
						+ "	lab_config.organisation_id = :orgId " + "	and lab_config.unit_id = :orgUnitId "
						+ " inner join lab.t_phlebotomist_home_collection collection on collection.order_id = ord_mst.order_id "
						+ "left join doctor.m_doctor_master doct_mst on "
						+ "	ord_mst.ord_doctor_id = doct_mst.doctor_id "
						+ " left join adt.t_admission ta  on ta.admission_id = ord_mst.admission_id and ta.status='A' "
						+ " left join opd.t_patient_sponsor_details tpsd "
						+ "		on enc.encounter_id = tpsd.encounter_id and tpsd.priority_id = 1 and tpsd.status='A' "
						+ " left join opd.t_patient_sponsor_details tpsd2 "
						+ "		on tpsd2.admission_id = ta.admission_id and tpsd2.priority_id = 1 and tpsd2.status='A' "
						+ "left join doctor.m_speciality_master speciality_master on "
						+ "	speciality_master.speciality_id = ord_mst.ord_doc_spl_id "
						+ " left join public.t_referal_details refrDtls on "
						+ "		refrDtls.encounter_id = enc.encounter_id "
						+ " left join adt.m_referral_master reflMst on "
						+ " 	reflMst.referral_id = refrDtls.referral_id "
						+ " left join patient.t_patient_details pDtls on pDtls.patient_id = pati_mst.patient_id "
						+ " left join m_employee_master emp on emp.user_id = collection.created_by "
						+ " where " + "	ord_mst.org_id =:orgId " + "	and ord_mst.org_unit_id =:orgUnitId "
						+ "	and ord_mst.order_status = 'A' " + " and ord_mst.home_collection = true"
						+ " and collection.user_id = :userId " + "	and ord_mst.visit_type_id =:visitTypeId "
						+ "	and ord_details.service_rendered in (0,1) " + "	and ord_details.ord_cancelled = 'N' "
						+ "	and ord_details.is_outsourced = 'N' " + "	and ord_details.status = 'A' "
						+ "	and service_dtls.is_panel = 'Y' " + " and ord_details.is_order_visible_dept='A' "
						+ "	and ord_details.service_rendering_deptid not in (lab_config.histopathology_id,lab_config.cystopathology_id,lab_config.clinical_pathology_id) ");

				if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0) {
					sb.append(" AND pati_mst.patient_id =:patientId ");
				}
				if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size() > 0) {
					visitTypes = searchDto.getVisitTypes().stream()
							.map(obj -> obj.getId())
							.collect(Collectors.toList());
					sb.append(" AND visit_type_mst.visit_type_id IN (:visitTypes )");
				}
				if (searchDto.getFromDt() != null) {
					sb.append("	and to_char(ord_details.order_date,'YYYY-MM-DD') >=:fromDate ");
				}
				if (searchDto.getToDt() != null) {
					sb.append("	and to_char(ord_details.order_date,'YYYY-MM-DD') <=:toDate ");
				}

				if (searchDto.getOrderStatusId() != null && searchDto.getOrderStatusId() == 1) {
					sb.append(" and ord_mst.order_status_id in (:orderStatusIds) ");
				}

				if (searchDto.getOrderStatusId() != null && searchDto.getOrderStatusId() == 3) {
					sb.append(" and (ord_mst.order_status_id = 3 ) ");
				}
				if (searchDto.getOrderStatusId() == null) {
					sb.append(" and ord_mst.order_status_id in (:orderStatusIdsNew) ");
				}
				sb.append(" order by " + "\"isCritical\" desc, " + "	\"createdDate\" DESC	");
				StringBuilder newSbForCount = sb;
				int count = 0;
				Query createdQuery = sessionFactory.getCurrentSession().createSQLQuery(sb.toString());

				if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0) {
					createdQuery.setParameter("patientId",searchDto.getPatientId());
				}

				if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size() > 0) {
					createdQuery.setParameter("visitTypes",visitTypes);
				}

				if (searchDto.getFromDt() != null) {
					createdQuery.setParameter("fromDate",searchDto.getFromDt());
				}
				if (searchDto.getToDt() != null) {
					createdQuery.setParameter("toDate",searchDto.getToDt());
				}

				if (searchDto.getOrderStatusId() != null && searchDto.getOrderStatusId() == 1) {
					createdQuery.setParameter("orderStatusIds",searchDto.getOrderStatusId());
				}
				if (searchDto.getOrderStatusId() == null) {
					createdQuery.setParameter("orderStatusIdsNew",orderStatusIds.toString().replaceAll("\\[", "").replaceAll("\\]", ""));

				}

				List<LabSampleMasterDto> listLabSampleMasterDto = createdQuery.setParameter("orgId", searchDto.getOrgId())
						.setParameter("orgUnitId", searchDto.getOrgUnitId()).setParameter("visitTypeId", ICommonConstants.OP, StandardBasicTypes.INTEGER)
						.setParameter("userId", searchDto.getUserId())
						.setFirstResult(searchDto.getOffset() != null ? searchDto.getOffset() : 0)
						.setMaxResults(searchDto.getRecordPerPage() != null ? searchDto.getRecordPerPage() : 50)
						.setResultTransformer(Transformers.aliasToBean(LabSampleMasterDto.class)).list();

				if (listLabSampleMasterDto.isEmpty()) {
					return new Response(SUCCESS, SUCCESS_200_MESSAGE, "No Records Found.", listLabSampleMasterDto,
							count);
				} else {
					Query createdQueryForCount = sessionFactory.getCurrentSession()
							.createSQLQuery(newSbForCount.toString());

					if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0) {
						createdQueryForCount.setParameter("patientId",searchDto.getPatientId());
					}

					if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size() > 0) {
						createdQueryForCount.setParameter("visitTypes",visitTypes);
					}

					if (searchDto.getFromDt() != null) {
						createdQueryForCount.setParameter("fromDate",searchDto.getFromDt());
					}
					if (searchDto.getToDt() != null) {
						createdQueryForCount.setParameter("toDate",searchDto.getToDt());
					}

					if (searchDto.getOrderStatusId() != null && searchDto.getOrderStatusId() == 1) {
						createdQueryForCount.setParameter("orderStatusIds",searchDto.getOrderStatusId());
					}
					if (searchDto.getOrderStatusId() == null) {
						createdQueryForCount.setParameter("orderStatusIdsNew",orderStatusIds.toString().replaceAll("\\[", "").replaceAll("\\]", ""));

					}
					List<LabSampleMasterDto> listLabSampleMasterListForCount = createdQueryForCount
							.setParameter("orgId", searchDto.getOrgId()).setParameter("orgUnitId", searchDto.getOrgUnitId())
							.setParameter("visitTypeId", ICommonConstants.OP).setParameter("userId", searchDto.getUserId())
							.setResultTransformer(Transformers.aliasToBean(LabSampleMasterDto.class)).list();
					if (listLabSampleMasterListForCount != null && listLabSampleMasterListForCount.size() > 0) {
						count = listLabSampleMasterListForCount.size();
					}
					return new Response(SUCCESS, SUCCESS_200_MESSAGE, null, listLabSampleMasterDto, count);
				}

			}

		} catch (Exception e) {
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	public Response getHomeColletionTestList(Integer orgId, Integer orgUnitId, Integer orderId, Integer deptId,
			Character isPreOrPostBilling) throws ApplicationException {
		List<LabSampleDetailsMasterDto> listLabSampleDetailsMasterDto = null;
		try {
			if (isPreOrPostBilling == 'Y') {
				listLabSampleDetailsMasterDto = sessionFactory.getCurrentSession()
						.getNamedQuery("GET_LAB_ORDER_DETAILS_FOR_HOME_COLLECTION").setParameter("orgId", orgId)
						.setParameter("orgUnitId", orgUnitId).setParameter("orderId", orderId)
						.setParameter("ordCancelled", 'N').setParameter("sampleStatus", 'A')
						.setParameter("unitStatus", 'A').setParameter("sampleStatusId", SAMPLE_RECOLLECTED)
						.setParameter("patientStatusId", REPAT_PATIRNT)
						.setResultTransformer(Transformers.aliasToBean(LabSampleDetailsMasterDto.class)).list();
				return new Response(SUCCESS, SUCCESS_200, null, listLabSampleDetailsMasterDto, null);
			} else {
				listLabSampleDetailsMasterDto = sessionFactory.getCurrentSession()
						.getNamedQuery("GET_LAB_ORDER_DETAILS_FOR_HOME_COLLECTION").setParameter("orgId", orgId)
						.setParameter("orgUnitId", orgUnitId).setParameter("orderId", orderId)
						.setParameter("ordCancelled", 'N').setParameter("sampleStatus", 'A')
						.setParameter("unitStatus", 'A').setParameter("sampleStatusId", SAMPLE_RECOLLECTED)
						.setParameter("patientStatusId", REPAT_PATIRNT)
						.setResultTransformer(Transformers.aliasToBean(LabSampleDetailsMasterDto.class)).list();
				return new Response(SUCCESS, SUCCESS_200, null, listLabSampleDetailsMasterDto, null);
			}

		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	public Response updateLabSampleDetails(SearchDto searchDto) throws ApplicationException {

		try {
			Integer count = sessionFactory.getCurrentSession()
					.createSQLQuery(" update lab.t_lab_sample_dtls set is_outsourced = '" + searchDto.getIsOutSource()
							+ "'   where order_details_id =:orderDetailsId " )
					.setParameter("orderDetailsId",searchDto.getOrderDetailsId())
					.executeUpdate();
			return new Response(SUCCESS, SUCCESS_200, null, null, count);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	public Response updateAssignUnitAndTestId(LabSampleDetailsMasterDto dto) throws ApplicationException {
		try {
			String date = GlobalCommonDateUtils.getAvalDate(TimeZoneComponent.getDateByzone(dto.getAssignUnit()),
					"yyyy-M-dd HH:mm:ss");

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-M-dd HH:mm:ss");
			Date parsedDate = dateFormat.parse(date);
			Timestamp packageApplicableTimestamp = new Timestamp(parsedDate.getTime());
			Integer count = sessionFactory.getCurrentSession()
					.createSQLQuery("UPDATE lab.t_lab_sample_dtls SET " +
							"assign_unit = :assignUnit, " +
							"test_id = :testId, " +
							"updated_date = :updatedDate, " +
							"updated_by = :updatedBy " +
							"WHERE lab_sample_dtls_id = :labSampleDtlsId")
					.setParameter("assignUnit", dto.getAssignUnit())
					.setParameter("testId", dto.getTestId())
					.setParameter("updatedDate", packageApplicableTimestamp)
					.setParameter("updatedBy", dto.getUpdatedBy())
					.setParameter("labSampleDtlsId", dto.getLabSampleDtlsId())
					.executeUpdate();
			return new Response(SUCCESS, SUCCESS_200, null, null, count);
		} catch (Exception e) {
			logger.error("Exception", e);

		}
		return null;
	}

	@Override
	public Response getPatientLastHistoryOfTest(LastInvestigationDto dto) throws ApplicationException {
		try {
			String query =
					"select tlrd.parameter_id as \"parameterId\", " +
							"tlrd.param_name as \"paramName\", " +
							"tlrd.param_unit as \"paramUnit\", " +
							"to_char(tlsd.updated_date, 'YYYY-MM-DD') as \"releaseDate\", " +
							"tlrd.final_result as \"finalResult\" " +
							"from lab.t_lab_res_dtls tlrd " +
							"inner join lab.t_lab_test_result tltr on tlrd.lab_result_id = tltr.lab_test_res_id " +
							"inner join LAB.t_lab_sample_dtls tlsd on tltr.lab_sample_dtls_id = tlsd.lab_sample_dtls_id " +
							"inner join t_lab_configuration tlc on tlc.unit_id = tlsd.assign_unit " +
							"where tlsd.sample_status_id = 8 and tltr.sample_status_id = 8 " +
							"and tlsd.patient_id = :patientId and tlsd.assign_unit = :orgUnitId and tltr.org_id = :orgId " +
							"and tlsd.sub_dept_id in(tlc.biochemistry_id, tlc.clinical_pathology_id, tlc.hematology_id) ";

			if (dto.getParameterId() != null) {
				query += "AND tlrd.parameter_id = :parameterId ";
			}

			query +=
					"union all " +
							"select tsrtm.parameter_id as \"parameterId\", " +
							"mpm.parameter_name as \"paramName\", " +
							"mum.unit_name as \"paramUnit\", " +
							"to_char(tlsd.updated_date, 'YYYY-MM-DD') as \"releaseDate\", " +
							"tsrtm.finding_result as \"finalResult\" " +
							"from lab.t_serology_result_test_mppr tsrtm " +
							"inner join lab.t_serology_test_result tstr on tsrtm.serology_test_result_id = tstr.serology_test_result_id " +
							"inner join LAB.t_lab_sample_dtls tlsd on tstr.lab_sample_dtls_id = tlsd.lab_sample_dtls_id " +
							"inner join lab.m_parameter_master mpm on tsrtm.parameter_id = mpm.parameter_id " +
							"inner join lab.m_unit_master mum on mpm.unit_id = mum.unit_id " +
							"inner join t_lab_configuration tlc on tlc.unit_id = tlsd.assign_unit " +
							"where tlsd.sample_status_id = 8 and tlsd.patient_id = :patientId " +
							"and tlsd.assign_unit = :orgUnitId and tlsd.org_id = :orgId " +
							"and tlsd.sub_dept_id in(tlc.serology_id) ";

			if (dto.getParameterId() != null) {
				query += "and tsrtm.parameter_id = :parameterId ";
			}

			query += "order by \"releaseDate\" asc";

			Query createdQuery = sessionFactory.getCurrentSession().createSQLQuery(query);

			// Setting parameters safely
			createdQuery.setParameter("patientId", dto.getPatientId());
			createdQuery.setParameter("orgUnitId", dto.getUnitId());
			createdQuery.setParameter("orgId", dto.getOrganizationId());

			if (dto.getParameterId() != null) {
				createdQuery.setParameter("parameterId", dto.getParameterId());
			}

			List<LastInvestigationDto> listLabSampleMasterDto = createdQuery
					.setResultTransformer(Transformers.aliasToBean(LastInvestigationDto.class))
					.list();

			if (listLabSampleMasterDto.size() > 0) {
				return new Response(SUCCESS, SUCCESS_200_MESSAGE, null, listLabSampleMasterDto, null);
			} else {
				return new Response(SUCCESS, SUCCESS_200_MESSAGE, "No Records Found.", null, null);
			}

		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}

	}

	@Override
	public Response getPhlebotomyWorklistType(SearchDto searchDto) throws ApplicationException {
		try {



			Response resp = getSpecialityAndSubSpecialityListByUserIdByUnitId(searchDto);
			List<Integer> deptIds = new ArrayList<>();
			List<Integer> subDeptIds = new ArrayList<>();
			List<SearchDto> specialityList = new ArrayList<>();
			List<SearchDto> subSpecialityList = new ArrayList<>();
			if (resp.getListObject() != NULL && resp.getListObject().size() > 0) {
				specialityList = resp.getListObject();
				if (resp.getListObject1() != null) {
					subSpecialityList = resp.getListObject1();
				}
			}
			if (specialityList != null && specialityList.size() > 0) {
				for (Iterator iterator = specialityList.iterator(); iterator.hasNext();) {
					SearchDto dto = (SearchDto) iterator.next();
					deptIds.add(dto.getSpecialityId());
				}
			} else {
				deptIds.add(searchDto.getDeptId());
			}

			if (subSpecialityList != null) {
				for (Iterator iterator = subSpecialityList.iterator(); iterator.hasNext();) {
					SearchDto dto2 = (SearchDto) iterator.next();
					subDeptIds.add(dto2.getSubSpecialityMasterId());
				}
			}
			searchDto.setSubDeptIds(subDeptIds);

			if (searchDto.getIsPreOrPostBilling() == 'Y') {
				List<Integer> orderStatusIds = (searchDto.getOrderStatusId() != null
						&& searchDto.getOrderStatusId() > 0) ? Arrays.asList(searchDto.getOrderStatusId())
						: PENDING_INPROGRESS_RENDER_CANCEL;
				StringBuilder sb = new StringBuilder();
				sb.append("select " + "	distinct(ord_mst.order_id) as \"orderId\", "
						+ "    bill_mst.bill_number as \"billNumber\", "
						/* + " (select count(*) from t_order_details caetod where caetod.order_id = ord_mst.order_id and  caetod.is_dummy_service = false ) as \"realServiceCount\", "*/
						+ "	ord_mst.org_id as \"orgId\", "
						+ "    bill_mst.billing_master_id as \"billingMasterId\", "
						+ "	ord_mst.org_unit_id as \"orgUnitId\", " + "	ord_mst.order_status_id as \"orderStatusId\", "
						+ "	ord_mst.visit_type_id as \"visitTypeId\", "
						+ "	visit_type_mst.visit_type_code as \"visitType\", "
						+ "	ord_mst.ord_doctor_id as \"doctorId\", " + "	ord_mst.patient_id as \"patientId\", "
						+ "	ord_mst.admission_id as \"admissionId\", " + "	ord_mst.encounter_id as \"encounterId\", "
						+ "	ord_mst.created_by as \"createdBy\", " + "	ord_mst.created_date as \"createdDate\", "
						+ " ord_mst.home_collection as \"isHomeCollection\", "
						+ "	pati_mst.uhid_number as uhid, " + " pati_mst.is_vip as \"isVip\", " + " (case "
						+ " 		when pati_mst.is_vip = 'Y' then coalesce(pati_mst.alise_name, pati_mst.first_name) || ' ' || "
						+ " calculate_age(pati_mst.dob)  || '/' || gen_mst.gender_code "
						+ " 		else pre_mst.prefix_code || ' ' || pati_mst.first_name || ' ' || coalesce(pati_mst.middle_name) || ' ' || pati_mst.last_name || ' ' || "
						+ "calculate_age(pati_mst.dob) || '/' || gen_mst.gender_code "
						+ " 	end ) as \"patientDetails\", " + "	gen_mst.gender_id as \"genderId\", "
						+ "	'Dr.' || doct_mst.first_name || ' ' || doct_mst.last_name || ' (' || speciality_master.speciality_name || ')' as \"doctorDetails\", "
						+ "		(case when (select count(*) from t_order_details caetod where caetod.order_id = ord_mst.order_id and caetod.priority_id =2 ) > 0 then 2 else 1 end) as \"priorityId\","
//						+ "	pri_mst.priority_id as \"priorityId\", " + "	pri_mst.priority_name as \"priorityName\", "
						+ "	pri_mst.color_code as \"colorCode\", "
						//+ " concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, pati_mst.dob))) as \"age\", "
						+ " '' as \"age\", "
						+ "	enc.encounter_type_id as \"encounterTypeId\", " + "	enc.pat_history as \"patHistory\", "
						+ " pati_mst.image_path as \"imagePath\","
						+ "	coalesce(enc.is_critical,'N') as \"isCritical\", "
						+ " 	to_char(ord_mst.order_date, 'yyyy-mm-dd') as \"orderDateString\", "
						+ " 	to_char(ord_mst.order_date, 'HH24:MI') as \"orderDateTime\", "
						+ "  to_char(ord_mst.created_date, 'DD/MM/YYYY HH24:MI:SS') AS \"createdDateString\",   "
						+ " ord_mst.order_no as \"orderNo\" "
						+ "from "
						+ "	public.t_order_master ord_mst "
						+ "inner join public.t_order_details ord_details on "
						+ "	ord_details.order_id = ord_mst.order_id "
						+ "inner join patient.t_patient_registration pati_mst on "
						+ "	ord_mst.patient_id = pati_mst.patient_id " + "inner join public.m_gender_master gen_mst on "
						+ "	gen_mst.gender_id = pati_mst.gender_id " + "inner join public.m_prefix_master pre_mst on "
						+ "	pre_mst.prefix_id = pati_mst.prefix_id "
						+ "inner join public.m_visit_type_master visit_type_mst on "
						+ "	visit_type_mst.visit_type_id = ord_mst.visit_type_id "
						/*+ "inner join service.t_unit_service_master_details service_dtls on "
						+ "	ord_details.service_id = service_dtls.service_master_id "
						+ "	and service_dtls.status = 'A' " */
						/*+ "inner join public.t_lab_configuration lab_config on "
						+ "	lab_config.organisation_id = :orgId " + "	and lab_config.unit_id = :orgUnitId "*/
						+ "inner join lab.m_test_master test_mst on "
						+ "	ord_details.service_id = test_mst.service_id " + "	and test_mst.test_status = 'A' "
						//+ "	and test_mst.is_histo_cyto in ('N','S') "
						+ "left join billing.t_billing_details bill_dtls on "
						+ "	ord_details.order_details_id = bill_dtls.order_details_id "
						+ "left join billing.m_billing_master bill_mst on "
						+ "	bill_mst.billing_master_id = bill_dtls.billing_master_id "
						+ "left join opd.t_encounter_master enc on " + "	 bill_mst.encounter_id = enc.encounter_id "
						+ "left join doctor.m_doctor_master doct_mst on "
						+ "	ord_mst.ord_doctor_id = doct_mst.doctor_id "
						+ "left join lab.m_priority_mASter pri_mst on "
						+ "	ord_mst.priority_id = pri_mst.priority_id "
						+ " left join adt.t_admission ta  on ta.admission_id = ord_mst.admission_id and ta.status='A' "
						+ "left join doctor.m_speciality_master speciality_master on "
						+ "	speciality_master.speciality_id = ord_mst.ord_doc_spl_id "
						+ " left join billing.t_credit_note_detail tcnd on "
						+ " tcnd.order_detail_id = ord_details.order_details_id " + " and tcnd.status = 'A' "
						+ " and tcnd.cancel_or_discount = 1 " );

				if (searchDto.getSubDeptIds() != null && searchDto.getSubDeptIds().size() > 0) {
					sb.append(" left join (select " + "		count(*) c , service_id "
							+ "		from lab.m_test_master test_mst "
							+ "		left join service.m_service_master service on"
							+ "		service.service_master_id = test_mst.service_id "
							+ "		where  test_mst.test_status = 'A' "
							+ "		and service.sub_speciality_id In (:subDeptIds) group by service_id "
							+ "		) m on m.service_id = ord_details.service_id and m.c  > 0 ");
				}

				sb.append(" where " + " 	ord_mst.org_id =:orgId "
								+ "    and (bill_mst.bill_cancelled = 'N' or bill_mst.bill_cancelled is null)"
								+ " 	and ord_mst.org_unit_id =:orgUnitId " + " 	and ord_mst.order_status = 'A' "
								+ " 	and ord_mst.order_status_id in (:orderStatusId) "
								+ " 	and ord_details.service_rendered in (0, 1) " + " 	and " + " 	(case "
								+ " 		when ord_mst.visit_type_id = 1 then ord_details.service_is_billed = 1 "
								+ " 		else ord_details.service_is_billed in(0, 1) " + " 	end ) " + " 	and "
								+ " 	(case "
								+ " 		when ord_mst.visit_type_id = 1 then ( bill_mst.bill_status_id is not null  and  bill_mst.bill_status_id in( 2, 3)) "
								+ " 		else (bill_mst.bill_status_id is null "
								+ " 			or bill_mst.bill_status_id in(1, 2, 3)) " + " 	end ) "
								+ " 	and ord_details.ord_cancelled = 'N' " + " 	and ord_details.is_outsourced = 'N' "
								+ " 	and (ord_details.status = 'A' and tcnd.credit_note_detail_id is null) "
								+ " 	and ord_details.is_order_visible_dept = 'A' "
						/*+ " 	and lab_config.lab_speciality_id IN ("
						+ deptIds.toString().replaceAll("\\[", "").replaceAll("\\]", "") + ")"*/
				);

				if (searchDto.getFromDt() != null) {
					sb.append("	and to_char(ord_details.order_date,'YYYY-MM-DD') >=:fromDt ");
				}
				if (searchDto.getToDt() != null) {
					sb.append("	and to_char(ord_details.order_date,'YYYY-MM-DD') <=:toDt ");
				}
				if (searchDto.getSearchField() != "") {
					sb.append(" AND (lower(pati_mst.uhid_number) LIKE concat('%',LOWER(:searchField "
							+ "),'%') OR " + " lower(pati_mst.first_name) LIKE concat('%',LOWER(:searchField "
							+ "),'%') OR "
							+ " lower(pati_mst.middle_name) LIKE concat('%',LOWER(:searchField "
							+ "),'%') OR " + " lower(pati_mst.last_name) LIKE concat('%',LOWER(:searchField "
							+ "),'%') ) ");
				}

				/*sb.append(" union " + "select " + "	distinct(ord_mst.order_id) as \"orderId\", "
						+ "    bill_mst.bill_number as \"billNumber\", "
                        + " (select count(*) from t_order_details caetod where caetod.order_id = ord_mst.order_id and  caetod.is_dummy_service = false ) as \"realServiceCount\", "
						+ "	ord_mst.org_id as \"orgId\", "
						+ "    bill_mst.billing_master_id as \"billingMasterId\", "
						+ "	ord_mst.org_unit_id as \"orgUnitId\", " + "	ord_mst.order_status_id as \"orderStatusId\", "
						+ "	ord_mst.visit_type_id as \"visitTypeId\", "
						+ "	visit_type_mst.visit_type_code as \"visitType\", "
						+ "	ord_mst.ord_doctor_id as \"doctorId\", " + "	ord_mst.patient_id as \"patientId\", "
						+ "	ord_mst.admission_id as \"admissionId\", " + "	ord_mst.encounter_id as \"encounterId\", "
						+ "	ord_mst.created_by as \"createdBy\", " + "	ord_mst.created_date as \"createdDate\", "
						+ " ord_mst.home_collection as \"isHomeCollection\", "
						+ "	pati_mst.uhid_number as uhid, " + " pati_mst.is_vip as \"isVip\", " + " (case "
						+ " 		when pati_mst.is_vip = 'Y' then coalesce(pati_mst.alise_name, pati_mst.first_name) || ' ' || extract( year  "
						+ " 	from " + " 		age( now(), pati_mst.dob ) )|| '/' || gen_mst.gender_code "
						+ " 		else pre_mst.prefix_code || ' ' || pati_mst.first_name || ' ' || coalesce(pati_mst.middle_name) || ' ' || pati_mst.last_name || ' ' || extract( year "
						+ " 	from " + " 		age( now(), pati_mst.dob ) )|| '/' || gen_mst.gender_code "
						+ " 	end ) as \"patientDetails\", " + "	gen_mst.gender_id as \"genderId\", "
						+ "	'Dr.' || doct_mst.first_name || ' ' || doct_mst.last_name || ' (' || speciality_master.speciality_name || ')' as \"doctorDetails\", "
						+ "		(case when (select count(*) from t_order_details caetod where caetod.order_id = ord_mst.order_id and caetod.priority_id =2 ) > 0 then 2 else 1 end) as \"priorityId\","
//						+ "	pri_mst.priority_id as \"priorityId\", " + "	pri_mst.priority_name as \"priorityName\", "
						+ "	pri_mst.color_code as \"colorCode\", "
						+ " concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, pati_mst.dob))) as \"age\", "
						+ "	enc.encounter_type_id as \"encounterTypeId\", " + "	enc.pat_history as \"patHistory\", "
						+ " pati_mst.image_path as \"imagePath\","
						+ "	coalesce(enc.is_critical,'N') as \"isCritical\", "
						+ " 	to_char(ord_mst.order_date, 'yyyy-mm-dd') as \"orderDateString\", "
						+ " 	to_char(ord_mst.order_date, 'HH24:MI') as \"orderDateTime\", "
						+ "  to_char(ord_mst.created_date, 'DD/MM/YYYY HH24:MI:SS') AS \"createdDateString\",   "
						+ " ord_mst.order_no as \"orderNo\" "
						+ "from "
						+ "	public.t_order_master ord_mst "
						+ "inner join public.t_order_details ord_details on "
						+ "	ord_details.order_id = ord_mst.order_id "
						+ "inner join billing.t_billing_details bill_dtls on "
						+ "	ord_details.order_details_id = bill_dtls.order_details_id "
						+ "inner join billing.m_billing_master bill_mst on "
						+ "	bill_mst.billing_master_id = bill_dtls.billing_master_id "
						+ "inner join patient.t_patient_registration pati_mst on "
						+ "	ord_mst.patient_id = pati_mst.patient_id " + "inner join public.m_gender_master gen_mst on "
						+ "	gen_mst.gender_id = pati_mst.gender_id " + "inner join public.m_prefix_master pre_mst on "
						+ "	pre_mst.prefix_id = pati_mst.prefix_id "
						+ "inner join public.m_visit_type_master visit_type_mst on "
						+ "	visit_type_mst.visit_type_id = ord_mst.visit_type_id "
						+ "inner join service.t_unit_service_master_details service_dtls on "
						+ "	ord_details.service_id = service_dtls.service_master_id "
						+ "	and service_dtls.status = 'A' " + "inner join public.t_lab_configuration lab_config on "
						+ "	lab_config.organisation_id = :orgId " + "	and lab_config.unit_id = :orgUnitId "
						+ "left join opd.t_encounter_master enc on " + "	 bill_mst.encounter_id = enc.encounter_id "
						+ "left join doctor.m_doctor_master doct_mst on "
						+ "	ord_mst.ord_doctor_id = doct_mst.doctor_id "
						+ "left join lab.m_priority_mASter pri_mst on "
						+ "	ord_mst.priority_id = pri_mst.priority_id "
						+ " left join adt.t_admission ta  on ta.admission_id = ord_mst.admission_id and ta.status='A' "
						+ "left join doctor.m_speciality_master speciality_master on "
						+ "	speciality_master.speciality_id = ord_mst.ord_doc_spl_id "
						+ " left join billing.t_credit_note_detail tcnd on "
						+ " tcnd.order_detail_id = ord_details.order_details_id " + " and tcnd.status = 'A' "
						+ " and tcnd.cancel_or_discount = 1 ");

				if (searchDto.getSubDeptIds() != null && searchDto.getSubDeptIds().size() > 0) {
					sb.append("  left join (select " +
							"		count(*) c , service_id  " +
							"	from " +
							"		lab.t_panel_master test_mst " +
							"	left join service.m_service_master service on " +
							"		service.service_master_id = test_mst.service_id " +
							"		where  test_mst.status = 'A'  " +
							 "		and service.sub_speciality_id In ("+ searchDto.getSubDeptIds().toString().replaceAll("\\[", "").replaceAll("\\]", "")+ ") group by service_id ) n on n.service_id = ord_details.service_id and n.c  > 0 ");
				}

				sb.append(" where " + " 	ord_mst.org_id =:orgId "
						+ "    and (bill_mst.bill_cancelled = 'N' or bill_mst.bill_cancelled is null)"
						+ " 	and ord_mst.org_unit_id =:orgUnitId " + " 	and ord_mst.order_status = 'A' "
						+ " 	and ord_mst.order_status_id in (:orderStatusId) "
						+ " 	and ord_details.service_rendered in (0, 1) " + " 	and " + " 	(case "
						+ " 		when ord_mst.visit_type_id = 1 then ord_details.service_is_billed = 1 "
						+ " 		else ord_details.service_is_billed in(0, 1) " + " 	end ) " + " 	and "
						+ " 	(case "
						+ " 		when ord_mst.visit_type_id = 1 then ( bill_mst.bill_status_id is not null  and  bill_mst.bill_status_id in( 2, 3)) "
						+ " 		else (bill_mst.bill_status_id is null "
						+ " 			or bill_mst.bill_status_id in(1, 2, 3)) " + " 	end ) "
						+ " 	and ord_details.ord_cancelled = 'N' " + " 	and ord_details.is_outsourced = 'N' "
						+ " 	and (ord_details.status = 'A' and tcnd.credit_note_detail_id is null) "
						+ " 	and ord_details.is_order_visible_dept = 'A' " + " 	and service_dtls.is_panel = 'Y' "
						+ " 	and lab_config.lab_speciality_id IN ("
						+ deptIds.toString().replaceAll("\\[", "").replaceAll("\\]", "") + ")");

				if (searchDto.getSearchField() != "") {
					sb.append(" AND (lower(pati_mst.uhid_number) LIKE concat('%',LOWER('" + searchDto.getSearchField()
							+ "'),'%') OR " + " lower(pati_mst.first_name) LIKE concat('%',LOWER('"
							+ searchDto.getSearchField() + "'),'%') OR "
							+ " lower(pati_mst.middle_name) LIKE concat('%',LOWER('" + searchDto.getSearchField()
							+ "'),'%') OR " + " lower(pati_mst.last_name) LIKE concat('%',LOWER('"
							+ searchDto.getSearchField() + "'),'%')) ");
				}
				if (searchDto.getFromDt() != null) {
					sb.append("	and to_char(ord_details.order_date,'YYYY-MM-DD') >= '" + searchDto.getFromDt() + "'");
				}
				if (searchDto.getToDt() != null) {
					sb.append("	and to_char(ord_details.order_date,'YYYY-MM-DD') <= '" + searchDto.getToDt() + "'");
				}*/
				/*
				 * sb.append(" order by " + "\"isCritical\" desc, " + "	\"priorityId\" , " +
				 * "	\"createdDate\" DESC	");
				 */
				if (searchDto.getLabSortingId() != null) {
					if (searchDto.getLabSortingId() == 1) {
						sb.append(" ORDER BY   \"isVip\" desc,  \"patientId\" , \"createdDate\"  DESC ");
					} else if (searchDto.getLabSortingId() == 2) {
						sb.append(" ORDER BY   \"isVip\" desc,  \"orderId\" DESC ");
					} else if (searchDto.getLabSortingId() == 4) {
						sb.append(" ORDER BY  \"isVip\" desc, \"createdDate\" DESC ");
					}
				} else {
					sb.append(" order by \"isCritical\" , \"isVip\" desc, \"createdDate\" DESC	");
				}

				int count = 0;

				Query createdQuery = sessionFactory.getCurrentSession().createSQLQuery(sb.toString());

				if (searchDto.getSubDeptIds() != null && searchDto.getSubDeptIds().size() > 0)
					createdQuery.setParameterList("subDeptIds", searchDto.getSubDeptIds());

				if (searchDto.getFromDt() != null)
					createdQuery.setParameter("fromDt", searchDto.getFromDt());

				if (searchDto.getToDt() != null)
					createdQuery.setParameter("toDt", searchDto.getToDt());

				if (searchDto.getSearchField() != "")
					createdQuery.setParameter("searchField", searchDto.getSearchField());

				List<LabSampleMasterDto> listLabSampleMasterDto = createdQuery.setParameter("orgId", searchDto.getOrgId())
						.setParameter("orgUnitId", searchDto.getOrgUnitId())
						.setParameterList("orderStatusId", orderStatusIds)
						.setFirstResult(searchDto.getOffset() != null ? searchDto.getOffset() : 0)
						.setMaxResults(searchDto.getRecordPerPage() != null ? searchDto.getRecordPerPage() : 50)
						.setResultTransformer(Transformers.aliasToBean(LabSampleMasterDto.class)).list();

				if (listLabSampleMasterDto.isEmpty()) {
					return new Response(SUCCESS, SUCCESS_200_MESSAGE, "No Records Found.", listLabSampleMasterDto,
							count);
				} else {
					if (searchDto.getIsCountRequired() != null && searchDto.getIsCountRequired()) {
						List<LabSampleMasterDto> listLabSampleMasterListForCount = this
								.getPhlebotomyPreBillingCountList(searchDto);
						if (listLabSampleMasterListForCount != null && listLabSampleMasterListForCount.size() > 0) {
							count = listLabSampleMasterListForCount.size();
						}
					}
					return new Response(SUCCESS, SUCCESS_200_MESSAGE, null, listLabSampleMasterDto, count);
				}
			} else {

				List<Integer> orderStatusIds = (searchDto.getOrderStatusId() != null
						&& searchDto.getOrderStatusId() > 0) ? Arrays.asList(searchDto.getOrderStatusId())
						: PENDING_INPROGRESS_RENDER_CANCEL;
				StringBuilder sb = new StringBuilder();
				sb.append("select " + "	distinct(ord_mst.order_id) as \"orderId\", "
						+ "    bill_mst.bill_number as \"billNumber\", "
						/*   + " (select count(*) from t_order_details caetod where caetod.order_id = ord_mst.order_id and  caetod.is_dummy_service = false ) as \"realServiceCount\", "*/
						+ "	ord_mst.org_id as \"orgId\", "
						+ "    bill_mst.billing_master_id as \"billingMasterId\", "
						+ "	ord_mst.org_unit_id as \"orgUnitId\", " + "	ord_mst.order_status_id as \"orderStatusId\", "
						+ "	ord_mst.visit_type_id as \"visitTypeId\", "
						+ "	visit_type_mst.visit_type_code as \"visitType\", "
						+ "	ord_mst.ord_doctor_id as \"doctorId\", " + "	ord_mst.patient_id as \"patientId\", "
						+ "	ord_mst.admission_id as \"admissionId\", " + "	ord_mst.encounter_id as \"encounterId\", "
						+ "	ord_mst.created_by as \"createdBy\", " + "	ord_mst.created_date as \"createdDate\", "
						+ " ord_mst.home_collection as \"isHomeCollection\", "
						+ "	pati_mst.uhid_number as uhid, " + " pati_mst.is_vip as \"isVip\", " + " (case "
						+ " 		when pati_mst.is_vip = 'Y' then coalesce(pati_mst.alise_name, pati_mst.first_name) || ' ' || "
						+ " calculate_age(pati_mst.dob) || '/' || gen_mst.gender_code "
						+ " 		else pre_mst.prefix_code || ' ' || pati_mst.first_name || ' ' || coalesce(pati_mst.middle_name) || ' ' || pati_mst.last_name || ' ' || "
						+ " calculate_age(pati_mst.dob) || '/' || gen_mst.gender_code "
						+ " 	end ) as \"patientDetails\", " + "	gen_mst.gender_id as \"genderId\", "
						+ "	'Dr.' || doct_mst.first_name || ' ' || doct_mst.last_name || ' (' || speciality_master.speciality_name || ')' as \"doctorDetails\", "
						+ "		(case when (select count(*) from t_order_details caetod where caetod.order_id = ord_mst.order_id and caetod.priority_id =2 ) > 0 then 2 else 1 end) as \"priorityId\","
//						+ "	pri_mst.priority_id as \"priorityId\", " + "	pri_mst.priority_name as \"priorityName\", "
						+ "	pri_mst.color_code as \"colorCode\", "
						+ " concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, pati_mst.dob))) as \"age\", "
						+ "	enc.encounter_type_id as \"encounterTypeId\", "
						+ "	coalesce(enc.is_critical,'N') as \"isCritical\", "
						+ " reflMst.referral_name as \"referralName\", " + " (case when ord_mst.encounter_id > 0 then "
						+ " ( tpsd.payment_entitlement_id ) "
						+ " else ( tpsd2.payment_entitlement_id ) end ) as \"paymentEntitlementId\", "
						+ " 	to_char(ord_mst.order_date, 'yyyy-mm-dd') as \"orderDateString\", "
						+ " 	to_char(ord_mst.order_date, 'HH24:MI') as \"orderDateTime\", "
						+ "  to_char(ord_mst.created_date, 'DD/MM/YYYY HH24:MI:SS') AS \"createdDateString\",   "
						+ " ord_mst.order_no as \"orderNo\" "
						+ "from "
						+ "	public.t_order_master ord_mst "
						+ "inner join public.t_order_details ord_details on "
						+ "	ord_details.order_id = ord_mst.order_id "
						+ "inner join patient.t_patient_registration pati_mst on "
						+ "	ord_mst.patient_id = pati_mst.patient_id " + "inner join public.m_gender_master gen_mst on "
						+ "	gen_mst.gender_id = pati_mst.gender_id " + "inner join public.m_prefix_master pre_mst on "
						+ "	pre_mst.prefix_id = pati_mst.prefix_id "
						+ "inner join public.m_visit_type_master visit_type_mst on "
						+ "	visit_type_mst.visit_type_id = ord_mst.visit_type_id "
						/*+ "inner join service.t_unit_service_master_details service_dtls on "
						+ "	ord_details.service_id = service_dtls.service_master_id "
						+ "	and service_dtls.status = 'A' " */
						+ "inner join public.t_lab_configuration lab_config on "
						+ "	lab_config.organisation_id = :orgId " + "	and lab_config.unit_id = :orgUnitId "
						+ "inner join lab.m_test_master test_mst on "
						+ "	ord_details.service_id = test_mst.service_id " + "	and test_mst.test_status = 'A' "
						+ "	and test_mst.is_histo_cyto in ('N','S') "
						+ "inner join billing.t_billing_details bill_dtls on "
						+ "	ord_details.order_details_id = bill_dtls.order_details_id "
						+ "inner join billing.m_billing_master bill_mst on "
						+ "	bill_mst.billing_master_id = bill_dtls.billing_master_id "
//							+ " and bill_mst.bill_cancelled = 'N'  "
						+ " left join opd.t_encounter_master enc on " + "	 ord_mst.encounter_id = enc.encounter_id "
						+ "left join doctor.m_doctor_master doct_mst on "
						+ "	ord_mst.ord_doctor_id = doct_mst.doctor_id "
						+ "left join lab.m_priority_mASter pri_mst on "
						+ "	ord_mst.priority_id = pri_mst.priority_id "
						+ " left join adt.t_admission ta  on ta.admission_id = ord_mst.admission_id and ta.status='A' "
						+ " left join opd.t_patient_sponsor_details tpsd "
						+ "		on enc.encounter_id = tpsd.encounter_id and tpsd.priority_id = 1 and tpsd.status='A' "
						+ " left join opd.t_patient_sponsor_details tpsd2 "
						+ "		on tpsd2.admission_id = ta.admission_id and tpsd2.priority_id = 1 and tpsd2.status='A' "
						+ "left join doctor.m_speciality_master speciality_master on "
						+ "	speciality_master.speciality_id = ord_mst.ord_doc_spl_id "
						+ " left join public.t_referal_details refrDtls on "
						+ "		refrDtls.encounter_id = enc.encounter_id "
						+ " left join adt.m_referral_master reflMst on "
						+ " 	reflMst.referral_id = refrDtls.referral_id " );

				if (searchDto.getSubDeptIds() != null && searchDto.getSubDeptIds().size() > 0) {
					sb.append(" left join (select count(*) c , service_id "
							+ "		from lab.m_test_master test_mst "
							+ "		left join service.m_service_master service on"
							+ "		service.service_master_id = test_mst.service_id "
							+ "		where  test_mst.test_status = 'A' "
							+ "		and service.sub_speciality_id In (:subDeptIds) group by service_id "
							+ "		) m on m.service_id = ord_details.service_id and m.c  > 0 ");
				}

				sb.append(" where " + "	ord_mst.org_id =:orgId "
						+ "    and (bill_mst.bill_cancelled = 'N' or bill_mst.bill_cancelled is null)"
						+ "	and ord_mst.org_unit_id =:orgUnitId " + "	and ord_mst.order_status = 'A' "
						+ "	and ord_mst.order_status_id in (:orderStatusId) "
						+ "	and ord_details.service_rendered in (0,1) " + "	and ord_details.service_is_billed in (0,1) "
						+ "	and ord_details.ord_cancelled = 'N' " + "	and ord_details.is_outsourced = 'N' "
						+ "	and ord_details.status = 'A' "
						+ "	and ord_details.service_rendering_deptid in (lab_config.biochemistry_id,lab_config.hematology_id,lab_config.microbiology_id,lab_config.serology_id,lab_config.clinical_pathology_id) ");

				if (searchDto.getSearchField() != "") {
					sb.append(" AND (lower(pati_mst.uhid_number) LIKE concat('%',LOWER(:searchField "
							+ "),'%') OR " + " lower(pati_mst.first_name) LIKE concat('%',LOWER(:searchField"
							+ "),'%') OR "
							+ " lower(pati_mst.middle_name) LIKE concat('%',LOWER(:searchField "
							+ "),'%') OR " + " lower(pati_mst.last_name) LIKE concat('%',LOWER(:searchField "
							+ "),'%') ) ");
				}
				if (searchDto.getFromDt() != null) {
					sb.append("	and to_char(ord_mst.created_date,'YYYY-MM-DD') >=:fromDt ");
				}
				if (searchDto.getToDt() != null) {
					sb.append("	and to_char(ord_mst.created_date,'YYYY-MM-DD') <=:toDt ");
				}

				sb.append(" union " + "select " + "	distinct(ord_mst.order_id) as \"orderId\", "
						+ "    bill_mst.bill_number as \"billNumber\", "
						/*   + " (select count(*) from t_order_details caetod where caetod.order_id = ord_mst.order_id and  caetod.is_dummy_service = false ) as \"realServiceCount\", "*/
						+ "	ord_mst.org_id as \"orgId\", "
						+ "    bill_mst.billing_master_id as \"billingMasterId\", "
						+ "	ord_mst.org_unit_id as \"orgUnitId\", " + "	ord_mst.order_status_id as \"orderStatusId\", "
						+ "	ord_mst.visit_type_id as \"visitTypeId\", "
						+ "	visit_type_mst.visit_type_code as \"visitType\", "
						+ "	ord_mst.ord_doctor_id as \"doctorId\", " + "	ord_mst.patient_id as \"patientId\", "
						+ "	ord_mst.admission_id as \"admissionId\", " + "	ord_mst.encounter_id as \"encounterId\", "
						+ "	ord_mst.created_by as \"createdBy\", " + "	ord_mst.created_date as \"createdDate\", "
						+ " ord_mst.home_collection as \"isHomeCollection\", "
						+ "	pati_mst.uhid_number as uhid, " + " pati_mst.is_vip as \"isVip\", " + " (case "
						+ " 		when pati_mst.is_vip = 'Y' then coalesce(pati_mst.alise_name, pati_mst.first_name) || ' ' || "
						+ " calculate_age(pati_mst.dob) || '/' || gen_mst.gender_code "
						+ " 		else pre_mst.prefix_code || ' ' || pati_mst.first_name || ' ' || coalesce(pati_mst.middle_name) || ' ' || pati_mst.last_name || ' ' || "
						+ " calculate_age(pati_mst.dob) || '/' || gen_mst.gender_code "
						+ " 	end ) as \"patientDetails\", " + "	gen_mst.gender_id as \"genderId\", "
						+ "	'Dr.' || doct_mst.first_name || ' ' || doct_mst.last_name || ' (' || speciality_master.speciality_name || ')' as \"doctorDetails\", "
						+ "		(case when (select count(*) from t_order_details caetod where caetod.order_id = ord_mst.order_id and caetod.priority_id =2 ) > 0 then 2 else 1 end) as \"priorityId\","
//						+ "	pri_mst.priority_id as \"priorityId\", " + "	pri_mst.priority_name as \"priorityName\", "
						+ "	pri_mst.color_code as \"colorCode\", "
						+ " concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, pati_mst.dob))) as \"age\", "
						+ "	enc.encounter_type_id as \"encounterTypeId\", "
						+ "	coalesce(enc.is_critical,'N') as \"isCritical\", "
						+ " reflMst.referral_name as \"referralName\", " + " (case when ord_mst.encounter_id > 0 then "
						+ " ( tpsd.payment_entitlement_id ) "
						+ " else ( tpsd2.payment_entitlement_id ) end ) as \"paymentEntitlementId\", "
						+ " 	to_char(ord_mst.order_date, 'yyyy-mm-dd') as \"orderDateString\", "
						+ " 	to_char(ord_mst.order_date, 'HH24:MI') as \"orderDateTime\", "
						+ "  to_char(ord_mst.created_date, 'DD/MM/YYYY HH24:MI:SS') AS \"createdDateString\",   "
						+ " ord_mst.order_no as \"orderNo\" "
						+ "from "
						+ "	public.t_order_master ord_mst "
						+ "inner join public.t_order_details ord_details on "
						+ "	ord_details.order_id = ord_mst.order_id "
						+ "inner join patient.t_patient_registration pati_mst on "
						+ "	ord_mst.patient_id = pati_mst.patient_id " + "inner join public.m_gender_master gen_mst on "
						+ "	gen_mst.gender_id = pati_mst.gender_id " + "inner join public.m_prefix_master pre_mst on "
						+ "	pre_mst.prefix_id = pati_mst.prefix_id "
						+ "inner join public.m_visit_type_master visit_type_mst on "
						+ "	visit_type_mst.visit_type_id = ord_mst.visit_type_id "
						+ "inner join service.t_unit_service_master_details service_dtls on "
						+ "	ord_details.service_id = service_dtls.service_master_id "
						+ "	and service_dtls.status = 'A' " + "inner join public.t_lab_configuration lab_config on "
						+ "	lab_config.organisation_id = :orgId " + "	and lab_config.unit_id = :orgUnitId "
						+ "inner join billing.t_billing_details bill_dtls on "
						+ "	ord_details.order_details_id = bill_dtls.order_details_id "
						+ "inner join billing.m_billing_master bill_mst on "
						+ "	bill_mst.billing_master_id = bill_dtls.billing_master_id "
//							+ " and bill_mst.bill_cancelled = 'N' "
						+ "left join opd.t_encounter_master enc on " + "	 ord_mst.encounter_id = enc.encounter_id "
						+ "left join doctor.m_doctor_master doct_mst on "
						+ "	ord_mst.ord_doctor_id = doct_mst.doctor_id "
						+ "left join lab.m_priority_mASter pri_mst on "
						+ "	ord_mst.priority_id = pri_mst.priority_id "
						+ " left join adt.t_admission ta  on ta.admission_id = ord_mst.admission_id and ta.status='A' "
						+ " left join opd.t_patient_sponsor_details tpsd "
						+ "		on enc.encounter_id = tpsd.encounter_id and tpsd.priority_id = 1 and tpsd.status='A' "
						+ " left join opd.t_patient_sponsor_details tpsd2 "
						+ "		on tpsd2.admission_id = ta.admission_id and tpsd2.priority_id = 1 and tpsd2.status='A' "
						+ "left join doctor.m_speciality_master speciality_master on "
						+ "	speciality_master.speciality_id = ord_mst.ord_doc_spl_id "
						+ " left join public.t_referal_details refrDtls on "
						+ "		refrDtls.encounter_id = enc.encounter_id "
						+ " left join adt.m_referral_master reflMst on "
						+ " 	reflMst.referral_id = refrDtls.referral_id ");

				if (searchDto.getSubDeptIds() != null && searchDto.getSubDeptIds().size() > 0) {
					sb.append("  left join (select " +
							"		count(*) c , service_id  " +
							"	from " +
							"		lab.t_panel_master test_mst " +
							"	left join service.m_service_master service on " +
							"		service.service_master_id = test_mst.service_id " +
							"		where  test_mst.status = 'A'  " +
							"		and service.sub_speciality_id In (:subDeptIds) group by service_id ) n on n.service_id = ord_details.service_id and n.c  > 0 ");
				}

				sb.append(" where	ord_mst.org_id =:orgId "
						+ "    and (bill_mst.bill_cancelled = 'N' or bill_mst.bill_cancelled is null)"
						+ "	and ord_mst.org_unit_id =:orgUnitId " + "	and ord_mst.order_status = 'A' "
						+ "	and ord_mst.order_status_id in (:orderStatusId) "
						+ "	and ord_details.service_rendered in (0,1) " + "	and ord_details.service_is_billed in (0,1) "
						+ "	and ord_details.ord_cancelled = 'N' " + "	and ord_details.is_outsourced = 'N' "
						+ "	and ord_details.status = 'A' " + "	and service_dtls.is_panel = 'Y' "
						+ " and ord_details.is_order_visible_dept='A' "
						+ "	and ord_details.service_rendering_deptid not in (lab_config.histopathology_id,lab_config.cystopathology_id,lab_config.clinical_pathology_id) ");

				if (searchDto.getSearchField() != "") {
					sb.append(" AND (lower(pati_mst.uhid_number) LIKE concat('%',LOWER(:searchField "
							+ "'),'%') OR " + "lower(pati_mst.first_name) LIKE concat('%',LOWER(:searchField "
							+  "),'%') OR "
							+ " lower(pati_mst.middle_name) LIKE concat('%',LOWER(:searchField "
							+ "'),'%') OR " + " lower(pati_mst.last_name) LIKE concat('%',LOWER(:searchField"
							+ "),'%') ) ");
				}
				if (searchDto.getFromDt() != null) {
					sb.append("	and to_char(ord_details.order_date,'YYYY-MM-DD') >= :fromDt ");
				}
				if (searchDto.getToDt() != null) {
					sb.append("	and to_char(ord_details.order_date,'YYYY-MM-DD') <=:toDt ");
				}
				/*
				 * sb.append(" order by " + "\"isCritical\" desc, " + "	\"priorityId\" , " +
				 * "	\"createdDate\" DESC	");
				 */
				if (searchDto.getLabSortingId() != null) {
					if (searchDto.getLabSortingId() == 1) {
						sb.append(" ORDER BY  \"isVip\" desc,   \"patientId\" , \"createdDate\"  DESC ");
					} else if (searchDto.getLabSortingId() == 2) {
						sb.append(" ORDER BY  \"isVip\" desc,   \"orderId\" DESC ");
					} else if (searchDto.getLabSortingId() == 4) {
						sb.append(" ORDER BY \"isVip\" desc,  \"createdDate\" DESC ");
					}
				} else {
					sb.append(" order by \"isCritical\" , \"isVip\" desc, \"createdDate\" DESC	");
				}
				int count = 0;
				Query createdQuery = sessionFactory.getCurrentSession().createSQLQuery(sb.toString());

				if (searchDto.getSubDeptIds() != null && searchDto.getSubDeptIds().size() > 0)
					createdQuery.setParameterList("subDeptIds", searchDto.getSubDeptIds());

				if (searchDto.getFromDt() != null)
					createdQuery.setParameter("fromDt", searchDto.getFromDt());

				if (searchDto.getToDt() != null)
					createdQuery.setParameter("toDt", searchDto.getToDt());

				if (searchDto.getSearchField() != "")
					createdQuery.setParameter("searchField", searchDto.getSearchField());

				List<LabSampleMasterDto> listLabSampleMasterDto = createdQuery.setParameter("orgId", searchDto.getOrgId())
						.setParameter("orgUnitId", searchDto.getOrgUnitId())
						.setParameterList("orderStatusId", orderStatusIds)
						.setFirstResult(searchDto.getOffset() != null ? searchDto.getOffset() : 0)
						.setMaxResults(searchDto.getRecordPerPage() != null ? searchDto.getRecordPerPage() : 50)
						.setResultTransformer(Transformers.aliasToBean(LabSampleMasterDto.class)).list();

				if (listLabSampleMasterDto.isEmpty()) {
					return new Response(SUCCESS, SUCCESS_200_MESSAGE, "No Records Found.", listLabSampleMasterDto,
							count);
				} else {
					if (searchDto.getIsCountRequired() != null && searchDto.getIsCountRequired()) {
						List<LabSampleMasterDto> listLabSampleMasterListForCount = this
								.getPhlebotomyPostBillingCountList(searchDto);

						if (listLabSampleMasterListForCount != null && listLabSampleMasterListForCount.size() > 0) {
							count = listLabSampleMasterListForCount.size();
						}
					}
					return new Response(SUCCESS, SUCCESS_200_MESSAGE, null, listLabSampleMasterDto, count);
				}
			}

		} catch (Exception e) {
			logger.error("Exection", e);
			throw e;
		}
	}


	@Override
	public Response getLabSortingList() throws ApplicationException {
		try {
			List<LabSortingMaster> list = sessionFactory.getCurrentSession().createSQLQuery(
					"select mlsm.id as  \"id\" , mlsm.dispay_name  as \"displayName\",mlsm.value  as \"value\" from lab.m_lab_sorting_master mlsm "
					+ " ORDER BY mlsm.dispay_name ASC ")
					.setResultTransformer(Transformers.aliasToBean(LabSortingMaster.class)).list();
			return new Response(SUCCESS, SUCCESS_200, null, list, null);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	public Response getPhlebotomyWorklistOnNewOPBill(List<SearchDto> searchDto) throws ApplicationException {

		List<LabSampleDetailsMasterDto> listLabSampleDetailsMasterDto = null;
		try {
			List<Integer> orderIds = new ArrayList<>();

			for (SearchDto dto : searchDto) {
				Integer orderId = dto.getOrderId();
				orderIds.add(orderId);
			}

			listLabSampleDetailsMasterDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_LAB_ORDER_DETAILS_FOR_OPD_BILLING")
					.setParameter("orgId", searchDto.getFirst().getOrgId())
					.setParameter("orgUnitId", searchDto.getFirst().getOrgUnitId()).setParameterList("orderIds", orderIds)
					.setParameter("deptId", searchDto.getFirst().getDeptId()).setParameter("ordCancelled", 'N')
					.setParameter("sampleStatus", 'A').setParameter("unitStatus", 'A')
					.setParameter("sampleStatusId", SAMPLE_RECOLLECTED).setParameter("patientStatusId", REPAT_PATIRNT)
					.setResultTransformer(Transformers.aliasToBean(LabSampleDetailsMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_200, null, listLabSampleDetailsMasterDto, null);

		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	public Response checkForOrderCompletion(SearchDto searchDto) throws ApplicationException {
		List<SearchDto> searchDtoList = null;
		try {
			searchDtoList = sessionFactory.getCurrentSession().getNamedQuery("CHECK_FOR_ORDER_COMPLETION")
					.setParameter("orgId", searchDto.getOrgId()).setParameter("orderId", searchDto.getOrderId())
					.setResultTransformer(Transformers.aliasToBean(SearchDto.class)).list();

			if (searchDtoList != null && searchDtoList.size() > 0) {

				return new Response(SUCCESS, SUCCESS_200, null, null, null);

			} else {

				List<SearchDto> LabSampleDetailsMasterList = sessionFactory.getCurrentSession()
						.getNamedQuery("GET_LAB_SAMPLEDETAILS_ID_BY_ORDER_ID").setParameter("orgId", searchDto.getOrgId())
						.setParameter("orderId", searchDto.getOrderId())
						.setResultTransformer(Transformers.aliasToBean(SearchDto.class)).list();

				return new Response(SUCCESS, SUCCESS_200, null, LabSampleDetailsMasterList, null);
			}

		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	public Response uploadLabReportDocument(LabReportDocumentDetailsDto labReportDocumentDetailsDto)
			throws ApplicationException {
		try {

			LabReportDocumentDetails mst = new LabReportDocumentDetails();
			mst.setLabSampleDtlsId(labReportDocumentDetailsDto.getLabSampleDtlsId());
			mst.setTestId(labReportDocumentDetailsDto.getTestId());
			mst.setOrganizationId(labReportDocumentDetailsDto.getOrganizationId());
			mst.setUnitId(labReportDocumentDetailsDto.getUnitId());
			mst.setCreatedBy(labReportDocumentDetailsDto.getCreatedBy());
			mst.setUpdatedBy(labReportDocumentDetailsDto.getUpdatedBy());
			mst.setCreatedDate(GlobalCommonDateUtils.getDate(new Date(), "dd-M-yyyy hh:mm:ss"));
			mst.setUpdatedDate(GlobalCommonDateUtils.getDate(new Date(), "dd-M-yyyy hh:mm:ss"));
			mst.setStatus('A');
			mst.setFilePath(labReportDocumentDetailsDto.getFilePath());
			mst.setFileName(labReportDocumentDetailsDto.getFileName());
			mst.setImage(labReportDocumentDetailsDto.getImage());
			sessionFactory.getCurrentSession().save(mst);

			return new Response(SUCCESS, null, null, null, null);

		} catch (HibernateException he) {
			logger.error("Exception in uploadLabReportDocument", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("Exception in uploadLabReportDocument", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getLabReportDocumentDetailsByTest(SearchDto searchDto) throws ApplicationException {
		List<LabReportDocumentDetailsDto> labReportDocumentDetailsList = null;
		try {
			labReportDocumentDetailsList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_LAB_REPORT_DOCUMENT_DETAILS_BY_TEST_ID")
					.setParameter("orgId", searchDto.getOrgId()).setParameter("unitId", searchDto.getOrgUnitId())
					.setParameterList("labSampleDtlsIds", searchDto.getLabSampleDtlsIds())
					.setResultTransformer(Transformers.aliasToBean(LabReportDocumentDetailsDto.class)).list();

			return new Response(SUCCESS, SUCCESS_200, null, labReportDocumentDetailsList, null);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	public Response validateBarcode(LabSampleDetailsMasterDto searchDto) throws ApplicationException {
		try {
			List<LabSampleDetailsMasterDto> list = sessionFactory.getCurrentSession()
					.createSQLQuery(
							" select lab_sample_id as \"labSampleId\" from lab.t_lab_sample_master where sample_id =:sampleBarcode "
									+ " AND org_id =:orgId "
									+ " AND org_unit_id =:orgUnitId ")
					.setParameter("orgId", searchDto.getOrgId())
					.setParameter("orgUnitId", searchDto.getOrgUnitId())
					.setParameter("sampleBarcode", searchDto.getSampleBarcode())
					.setResultTransformer(Transformers.aliasToBean(LabSampleDetailsMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_200, null, list, null);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	public Response getCompanyOrInsuranceNameById(LabSampleMasterDto dto) throws ApplicationException {
		try {
			LabSampleMasterDto labSampleMasterDto = (LabSampleMasterDto) sessionFactory.getCurrentSession()
					.createSQLQuery(
							" select tcm.company_desc as \"companyDesc\"  from public.t_company_master tcm where tcm.company_id =:payeeId "
									+" AND tcm.company_type_id =:paymentEntitlementId ")
					.setParameter("payeeId", dto.getPayeeId())
					.setParameter("paymentEntitlementId", dto.getPaymentEntitlementId())
					.setResultTransformer(Transformers.aliasToBean(LabSampleMasterDto.class)).uniqueResult();
			return new Response(SUCCESS, SUCCESS_200, null, null, labSampleMasterDto);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	public Response getDatesByLabDetialsId(LabSampleDetailsMasterDto dto) throws ApplicationException {
		try {
			LabSampleDetailsMasterDto labSampleMasterDto = (LabSampleDetailsMasterDto) sessionFactory
					.getCurrentSession()
					.createSQLQuery("select (case when tltr.lab_sample_dtls_id is null and tstr.lab_sample_dtls_id is null then 0 else (case when tltr.lab_sample_dtls_id is null then 1 else 0 end ) end )  as \"screenId\","
							+ " to_char(tlsd.sample_collection_datetime, 'dd/mm/yyyy HH24:MI') as \"collectedSampleGenDatetime\" , "
							+ " to_char(tlsd.sample_accept_datetime, 'dd/mm/yyyy HH24:MI') as \"sampleAcceptDate\", "
							+ " (case when tltr.result_enter_datetime is null then to_char(tstr.result_enter_date, 'dd/mm/yyyy HH24:MI') else to_char(tltr.result_enter_datetime, 'dd/mm/yyyy HH24:MI') end)  as \"entryDate\","
							+ " (case when tltr.result_validated_datetime is null then to_char(tstr.result_validated_date, 'dd/mm/yyyy HH24:MI') else to_char(tltr.result_validated_datetime, 'dd/mm/yyyy HH24:MI') end) as \"validateDate\","
							+ " (case when tltr.result_authorised_datetime is null then to_char(tstr.report_release_date, 'dd/mm/yyyy HH24:MI') else to_char(tltr.result_authorised_datetime, 'dd/mm/yyyy HH24:MI') end)  as \"releaseDate\" "
							+ " from lab.t_lab_sample_dtls tlsd "
							+ " left join lab.t_lab_test_result tltr on tltr.lab_sample_dtls_id = tlsd.lab_sample_dtls_id  and tltr.status = 'A' and tltr.sample_status_id not in(4, 7, 15) "
							+ " left join lab.t_serology_test_result tstr  on tstr.lab_sample_dtls_id  = tlsd.lab_sample_dtls_id "
							+ " where   tlsd.lab_sample_dtls_id =:labSampleDtlsId ")
					.setParameter("labSampleDtlsId", dto.getLabSampleDtlsId())
					.setResultTransformer(Transformers.aliasToBean(LabSampleDetailsMasterDto.class)).uniqueResult();
			return new Response(SUCCESS, SUCCESS_200, null, null, labSampleMasterDto);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	public Response updateDatesByLabDetialsId(LabSampleDetailsMasterDto dto) throws ApplicationException {
		try {
			Integer count = null;
			if (dto.getScreenId() == 1) {
				String query = "UPDATE lab.t_serology_test_result SET "
						+ "result_enter_date = :entryDate, "
						+ "result_validated_date = :validateDate, "
						+ "report_release_date = :releaseDate "
						+ "WHERE lab_sample_dtls_id = :labSampleDtlsId";
				Query sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query);
//				sqlQuery.setParameter("entryDate", dto.getEntryDate() != null ? dto.getEntryDate() : null, StandardBasicTypes.TIMESTAMP);
//				sqlQuery.setParameter("validateDate", dto.getValidateDate() != null ? dto.getValidateDate() : null, StandardBasicTypes.TIMESTAMP);
//				sqlQuery.setParameter("releaseDate", dto.getReleaseDate() != null ? dto.getReleaseDate() : null, StandardBasicTypes.TIMESTAMP);
//				sqlQuery.setParameter("labSampleDtlsId", dto.getLabSampleDtlsId(), StandardBasicTypes.LONG);
				sqlQuery.setParameter("entryDate", dto.getEntryDate() != null ?  Timestamp.valueOf(dto.getEntryDate()) : null, StandardBasicTypes.TIMESTAMP);
				sqlQuery.setParameter("validateDate", dto.getValidateDate() != null ?  Timestamp.valueOf(dto.getValidateDate()) : null, StandardBasicTypes.TIMESTAMP);
				sqlQuery.setParameter("releaseDate", dto.getReleaseDate() != null ? Timestamp.valueOf(dto.getReleaseDate()) : null, StandardBasicTypes.TIMESTAMP);
				sqlQuery.setParameter("labSampleDtlsId", dto.getLabSampleDtlsId() != null ? dto.getLabSampleDtlsId().longValue() : null);
				count = sqlQuery.executeUpdate();

			} else {
				Query query = sessionFactory.getCurrentSession().createSQLQuery(
						"UPDATE lab.t_lab_test_result " +
								"SET result_enter_datetime = :entryDate, " +
								"    result_validated_datetime = :validateDate, " +
								"    result_authorised_datetime = :releaseDate " +
								"WHERE lab_sample_dtls_id = :labSampleDtlsId"
				);
//				query.setParameter("entryDate", dto.getEntryDate(), StandardBasicTypes.TIMESTAMP);
//				query.setParameter("validateDate", dto.getValidateDate(), StandardBasicTypes.TIMESTAMP);
//				query.setParameter("releaseDate", dto.getReleaseDate(), StandardBasicTypes.TIMESTAMP);
				query.setParameter("entryDate", dto.getEntryDate() != null ? Timestamp.valueOf(dto.getEntryDate()) : null, StandardBasicTypes.TIMESTAMP);
				query.setParameter("validateDate", dto.getValidateDate() != null ? Timestamp.valueOf(dto.getValidateDate()) : null, StandardBasicTypes.TIMESTAMP);
				query.setParameter("releaseDate", dto.getReleaseDate() != null ? Timestamp.valueOf(dto.getReleaseDate()) : null, StandardBasicTypes.TIMESTAMP);
				query.setParameter("labSampleDtlsId", dto.getLabSampleDtlsId(), StandardBasicTypes.INTEGER);
				count = query.executeUpdate();
			}


			String query = "update lab.t_lab_sample_dtls " +
					"set sample_collection_datetime = :collectedSampleDatetime, " +
					"sample_accept_datetime = :sampleAcceptDatetime " +
					"where lab_sample_dtls_id = :labSampleDtlsId";

			count = sessionFactory.getCurrentSession().createSQLQuery(query)
//					.setParameter("collectedSampleDatetime", dto.getCollectedSampleGenDatetime(), StandardBasicTypes.TIMESTAMP)
//					.setParameter("sampleAcceptDatetime",dto.getSampleAcceptDate(), StandardBasicTypes.TIMESTAMP)
//					.setParameter("labSampleDtlsId", dto.getLabSampleDtlsId(), StandardBasicTypes.LONG)
					.setParameter("collectedSampleDatetime", dto.getCollectedSampleGenDatetime() != null ? Timestamp.valueOf(dto.getCollectedSampleGenDatetime()) : null, StandardBasicTypes.TIMESTAMP)
					.setParameter("sampleAcceptDatetime", dto.getSampleAcceptDate() != null ? Timestamp.valueOf(dto.getSampleAcceptDate()) : null, StandardBasicTypes.TIMESTAMP)
					.setParameter("labSampleDtlsId", dto.getLabSampleDtlsId() != null ? dto.getLabSampleDtlsId().longValue() : null)
					.executeUpdate();
			return new Response(SUCCESS, SUCCESS_200, null, null, null);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	public Response PhlebotomyWorklistDetails(SearchDto searchDto) throws ApplicationException {
		try {
			LabSampleMasterDto labSampleMasterDto = (LabSampleMasterDto) sessionFactory.getCurrentSession()
					.createSQLQuery("select " + "	distinct(ord_mst.order_id) as \"orderId\", "
							+ "	reflMst.referral_name as \"referralName\" , "
							+ "	tpsd.payment_entitlement_id as \"paymentEntitlementId\", "
							+ "	tpsd.payee_id as \"payeeId\" " + "from " + "	public.t_order_master ord_mst "
							+ "inner join public.t_order_details ord_details on "
							+ "	ord_details.order_id = ord_mst.order_id " + "left join opd.t_encounter_master enc on "
							+ "	ord_mst.encounter_id = enc.encounter_id " + "left join adt.t_admission ta on "
							+ "	ta.admission_id = ord_mst.admission_id " + "	and ta.status = 'A' "
							+ "left join public.t_referal_details refrDtls on "
							+ "	refrDtls.encounter_id = enc.encounter_id "
							+ "left join adt.m_referral_master reflMst on "
							+ "	reflMst.referral_id = refrDtls.referral_id "
							+ "left join opd.t_patient_sponsor_details tpsd on " + "	(case "
							+ "		when enc.encounter_id is not null then enc.encounter_id = tpsd.encounter_id "
							+ "		else ta.admission_id = tpsd.admission_id " + "	end) "
							+ "	and tpsd.priority_id = 1 " + "	and tpsd.status = 'A' " + "where "
							+ "	 ord_mst.org_unit_id =:orgUnitId 	and ord_mst .order_id =:orderId ")
					.setParameter("orgUnitId",searchDto.getOrgUnitId())
					.setParameter("orderId",searchDto.getOrderId())
					.setResultTransformer(Transformers.aliasToBean(LabSampleMasterDto.class)).uniqueResult();
			return new Response(SUCCESS, SUCCESS_200, null, null, labSampleMasterDto);
		} catch (Exception e) {
			logger.error("Exception", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}

	@Override
	public Response getSpecialityAndSubSpecialityListByUserIdByUnitId(SearchDto searchDto) throws ApplicationException {
		try {
			List<SearchDto> unitSpecialtyMapperDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SPECIALITY_LIST_BY_USER_ID_UNIT_SEARCH")
					.setParameter("userId", searchDto.getUserId()).setParameter("unitId", searchDto.getOrgUnitId())
					.setResultTransformer(Transformers.aliasToBean(SearchDto.class)).list();

			List<SearchDto> unitSubSpecialtyMapperDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SUB_SPECIALITY_LIST_BY_USER_ID_UNIT_SEARCH")
					.setParameter("userId", searchDto.getUserId()).setParameter("unitId", searchDto.getOrgUnitId())
					.setResultTransformer(Transformers.aliasToBean(SearchDto.class)).list();

			return new Response(SUCCESS, null, null, unitSpecialtyMapperDtosList, null, unitSubSpecialtyMapperDtosList);

		} catch (HibernateException he) {
			logger.error("Exception in getSpecialityAndSubSpecialityListByUserIdByUnitId", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("Exception in getSpecialityAndSubSpecialityListByUserIdByUnitId", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public String getTestNamePhlebotomy(Integer orderId, Integer unitId, SearchDto searchDto) {
		try {
			if (searchDto.getUserId() != null) {
				Response resp = getSpecialityAndSubSpecialityListByUserIdByUnitId(searchDto);
				List<Integer> deptIds = new ArrayList<>();
				List<Integer> subDeptIds = new ArrayList<>();
				List<SearchDto> specialityList = new ArrayList<>();
				List<SearchDto> subSpecialityList = new ArrayList<>();
				if (resp.getListObject().size() > 0 && resp.getListObject() != NULL) {
					specialityList = resp.getListObject();
					if (resp.getListObject1() != null) {
						subSpecialityList = resp.getListObject1();
					}
				}
				if (specialityList != null && specialityList.size() > 0) {
					for (Iterator iterator = specialityList.iterator(); iterator.hasNext();) {
						SearchDto dto = (SearchDto) iterator.next();
						deptIds.add(dto.getSpecialityId());
					}
				} else {
					deptIds.add(searchDto.getDeptId());
				}

				if (subSpecialityList != null && subSpecialityList.size() > 0) {
					for (Iterator iterator = subSpecialityList.iterator(); iterator.hasNext();) {
						SearchDto dto2 = (SearchDto) iterator.next();
						subDeptIds.add(dto2.getSubSpecialityMasterId());
					}
				}
				searchDto.setSubDeptIds(subDeptIds);
			}

			if (searchDto.getSubDeptIds() != null && searchDto.getSubDeptIds().size() > 0) {
				String query = " select " + " 	array_to_string(array(select " + " 	test_mst.test_desc " + " from "
						+ " 	public.t_order_details ord_dtls " + " left join billing.t_credit_note_detail tcnd on "
						+ " 	tcnd.order_detail_id = ord_dtls.order_details_id " + "	and tcnd.status = 'A' "
						+ "	and tcnd.cancel_or_discount = 1 " + " inner join lab.m_test_master test_mst on "
						+ " 	ord_dtls.service_id = test_mst.service_id " + " where " + " 	ord_dtls.order_id =:orderId "
						+ " 	and ord_dtls.org_unit_id =:unitId " + " 	and test_mst.org_unit_id =:unitId "
						+ " 	and ord_dtls.is_order_visible_dept = 'A' " + " 	and test_mst.test_status = 'A' "
						+ " and (select " + "			count(*) " + "		from lab.m_test_master test_mst  "
						+ "  left join service.m_service_master service on "
						+ " 	service.service_master_id = ord_dtls.service_id " + "		where"
						+ "  test_mst.service_id = ord_dtls.service_id " + "		   and test_mst.test_status = 'A' "
						+ "			and service.sub_speciality_id In (:SubDeptIds) ) > 0 "
						+ " 	and (ord_dtls.ord_cancelled = 'N' and tcnd.credit_note_detail_id is null)), ', ') "
						+ " union all " + " select " + " 	array_to_string(array(select " + " 	test_mst.test_desc "
						+ " from " + " 	public.t_order_details ord_dtls "
						+ " left join billing.t_credit_note_detail tcnd on "
						+ " 	tcnd.order_detail_id = ord_dtls.order_details_id " + "	and tcnd.status = 'A' "
						+ "	and tcnd.cancel_or_discount = 1 " + " inner join lab.t_panel_master panel_master on "
						+ " 	panel_master.service_id = ord_dtls.service_id "
						+ " inner join lab.t_panel_details panel_details on "
						+ " 	panel_details.panel_id = panel_master.panel_id "
						+ " 	and ord_dtls.org_unit_id = panel_master.org_unit_id "
						+ " inner join lab.m_test_master test_mst on " + " 	test_mst.test_id = panel_details.test_id "
						+ " where " + " 	ord_dtls.order_id =:orderId " + " 	and ord_dtls.org_unit_id =:unitId "
						+ " 	and test_mst.org_unit_id =:unitId "
						+ " 	and ord_dtls.is_order_visible_dept = 'A' " + " 	and test_mst.test_status = 'A' "
						+ " and (select " + "			count(*) " + "		from lab.m_test_master test_mst  "
						+ "  left join service.m_service_master service on "
						+ " 	service.service_master_id = ord_dtls.service_id " + "		where"
						+ "  test_mst.service_id = ord_dtls.service_id " + "		   and test_mst.test_status = 'A' "
						+ "			and service.sub_speciality_id In (:SubDeptIds) ) > 0 "
						+ " 	and (ord_dtls.ord_cancelled = 'N' "
						+ " 		and tcnd.credit_note_detail_id is null) ), ', ') ";

				List<String> testDesc = sessionFactory.getCurrentSession().createSQLQuery(query)
						.setParameter("orderId", orderId)
						.setParameter("unitId", unitId)
						.setParameterList("SubDeptIds", searchDto.getSubDeptIds())
						.list();
				String newString = "";
				for (String str : testDesc) {
					if (!str.isEmpty()) {
						newString = str;
					}
				}

				return newString;
			} else {
				Response resp = getSpecialityAndSubSpecialityListByUserIdByUnitId(searchDto);
				List<Integer> deptIds = new ArrayList<>();
				List<Integer> subDeptIds = new ArrayList<>();
				List<SearchDto> specialityList = new ArrayList<>();
				List<SearchDto> subSpecialityList = new ArrayList<>();
				if (resp.getListObject().size() > 0 && resp.getListObject() != NULL) {
					specialityList = resp.getListObject();
					if (resp.getListObject1() != null) {
						subSpecialityList = resp.getListObject1();
					}
				}
				if (specialityList != null && specialityList.size() > 0) {
					for (Iterator iterator = specialityList.iterator(); iterator.hasNext();) {
						SearchDto dto = (SearchDto) iterator.next();
						deptIds.add(dto.getSpecialityId());
					}
				} else {
					deptIds.add(searchDto.getDeptId());
				}

				if (subSpecialityList != null) {
					for (Iterator iterator = subSpecialityList.iterator(); iterator.hasNext();) {
						SearchDto dto2 = (SearchDto) iterator.next();
						subDeptIds.add(dto2.getSubSpecialityMasterId());
					}
				}
				searchDto.setSubDeptIds(subDeptIds);

				String query = " select " + " 	array_to_string(array(select " + " 	test_mst.test_desc " + " from "
						+ " 	public.t_order_details ord_dtls " + " left join billing.t_credit_note_detail tcnd on "
						+ " 	tcnd.order_detail_id = ord_dtls.order_details_id " + "	and tcnd.status = 'A' "
						+ "	and tcnd.cancel_or_discount = 1 " + " inner join lab.m_test_master test_mst on "
						+ " 	ord_dtls.service_id = test_mst.service_id " + " where " + " 	ord_dtls.order_id =:orderId "
						+ " 	and ord_dtls.org_unit_id =:unitId " + " 	and test_mst.org_unit_id =:unitId "
						+ " 	and ord_dtls.is_order_visible_dept = 'A' " + " 	and test_mst.test_status = 'A' "
						+ " 	and (ord_dtls.ord_cancelled = 'N' and tcnd.credit_note_detail_id is null)), ', ') "
						+ " union all " + " select " + " 	array_to_string(array(select " + " 	test_mst.test_desc "
						+ " from " + " 	public.t_order_details ord_dtls "
						+ " left join billing.t_credit_note_detail tcnd on "
						+ " 	tcnd.order_detail_id = ord_dtls.order_details_id " + "	and tcnd.status = 'A' "
						+ "	and tcnd.cancel_or_discount = 1 " + " inner join lab.t_panel_master panel_master on "
						+ " 	panel_master.service_id = ord_dtls.service_id "
						+ " inner join lab.t_panel_details panel_details on "
						+ " 	panel_details.panel_id = panel_master.panel_id "
						+ " 	and ord_dtls.org_unit_id = panel_master.org_unit_id "
						+ " inner join lab.m_test_master test_mst on " + " 	test_mst.test_id = panel_details.test_id "
						+ " where " + " 	ord_dtls.order_id =:orderId" + " 	and ord_dtls.org_unit_id =:unitId "
						+ " 	and test_mst.org_unit_id =:unitId "
						+ " 	and ord_dtls.is_order_visible_dept = 'A' " + " 	and test_mst.test_status = 'A' "
						+ " 		and tcnd.credit_note_detail_id is null), ', ') ";

				List<String> testDesc = sessionFactory.getCurrentSession().createSQLQuery(query)
						.setParameter("orderId", orderId)
						.setParameter("unitId", unitId)
						.list();
				String newString = "";
				for (String str : testDesc) {
					if (!str.isEmpty()) {
						newString = str;
					}
				}

				return newString;
			}

		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	public Response amendReportReason(LabResultMasterDto dto) {

		try {
			Integer count = null;
			if(dto.getTestType() == 1){
				count = sessionFactory.getCurrentSession()
						.createSQLQuery(" update lab.t_lab_test_result set amend_by =:createdBy , amend_reason =:amendReason "
								+ " where lab_test_res_id = (select tltr.lab_test_res_id from lab.t_lab_test_result tltr  where lab_sample_dtls_id =:labSampleDtlsId order by lab_test_res_id desc  limit 1 )")
						.setParameter("labSampleDtlsId", dto.getLabSampleDtlsId())
						.setParameter("createdBy", dto.getCreatedBy())
						.setParameter("amendReason", dto.getAmendReason())

						.executeUpdate();
			}
			if(dto.getTestType() == 2){
				count = sessionFactory.getCurrentSession()
						.createSQLQuery(" update lab.t_serology_test_result set amend_by =:createdBy , amend_reason =:amendReason "
								+ " where serology_test_result_id = (select tltr.serology_test_result_id from lab.t_serology_test_result tltr  where lab_sample_dtls_id =:labSampleDtlsId order by serology_test_result_id desc  limit 1 )")
						.setParameter("labSampleDtlsId", dto.getLabSampleDtlsId())
						.setParameter("createdBy", dto.getCreatedBy())
						.setParameter("amendReason", dto.getAmendReason())

						.executeUpdate();
			}
			if(dto.getTestType() == 3){
				count = sessionFactory.getCurrentSession()
						.createSQLQuery(" update lab.t_sesitivity_test_result set amend_by =:createdBy , amend_reason =:amendReason "
								+ " where sensitivity_result_id = (select tltr.sensitivity_result_id from lab.t_sesitivity_test_result tltr  where lab_sample_dtls_id =:labSampleDtlsId order by sensitivity_result_id desc  limit 1 )")
						.setParameter("labSampleDtlsId", dto.getLabSampleDtlsId())
						.setParameter("createdBy", dto.getCreatedBy())
						.setParameter("amendReason", dto.getAmendReason())

						.executeUpdate();
			}
			return new Response(SUCCESS, SUCCESS_200, null, null, count);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	public Response<LabSampleMasterDto, Integer> getSampleDataForInterface(String interfaceName , String sampleID, String sampleInfoDate, int apiNumber, int orgUnitId) {
		List<LabSampleMasterDto> listLabSampleMasterDto1 = null;
		String query = "";

		try {
			Query sqlQuery;

			if (apiNumber == 1) {
				query = "select distinct gmd.sample_no as \"sampleNo\", "
						+ "gmd.first_name as \"firstName\", gmd.machineparname as \"testDesc\", "
						+ "gmd.last_name as \"lastName\", gmd.sex as \"genderCode\", TO_CHAR(gmd.dob, 'yyyyMMdd') as \"birthDate\", "
						+ "gmd.specimen_type as \"specimenType\", gmd.machineparacode as \"testCode\", "
						+ "gmd.para_unit as \"testUnit\", CAST(gmd.ageyear AS varchar) as \"age\", "
						+ "gmd.blood_group as \"bloodGroup\", gmd.bed_no as \"bedNo\", gmd.dr_name as \"drName\", "
						+ "gmd.department as \"department\", gmd.admission_id as \"admissionId\", "
						+ "TO_CHAR(gmd.date, 'yyyyMMdd') as \"sampleInfoDate\" "
						+ "from lab.get_machine_data gmd where "
						+ "gmd.machine_desc = :interfaceName and gmd.sample_no = :sampleID";

				sqlQuery = sessionFactory.getCurrentSession()
						.createSQLQuery(query)
						.setParameter("interfaceName", interfaceName)
						.setParameter("sampleID", sampleID);
			} else if (apiNumber == 2) {
				query = "select distinct gmd.sample_no as \"sampleNo\", "
						+ "gmd.first_name as \"firstName\", gmd.test_desc as \"testDesc\", "
						+ "gmd.last_name as \"lastName\", "
						+ "TO_CHAR(gmd.date, 'dd/MM/yyyy') as \"sampleInfoDate\", prefix as \"prefix\", middle_name as \"middleName\", "
						+ "machine_desc as \"interfaceName\" "
						+ "from lab.get_machine_data gmd where gmd.date >= :sampleInfoDate and gmd.machine_desc = :interfaceName and gmd.unit_id = :orgUnitId";

				sqlQuery = sessionFactory.getCurrentSession()
						.createSQLQuery(query)
						.setParameter("sampleInfoDate", sampleInfoDate)
						.setParameter("interfaceName", interfaceName)
						.setParameter("orgUnitId", orgUnitId);
			} else if (apiNumber == 3) {
				query = "select distinct tlmi.sample_no as \"sampleNo\", tlmi.parameter_name as \"testDesc\", tlmi.Parameter_value as \"interfaceResult\" "
						+ "from lab.t_lab_machine_interface tlmi "
						+ "where tlmi.sample_no = :sampleID and tlmi.machine_name = :interfaceName";

				sqlQuery = sessionFactory.getCurrentSession()
						.createSQLQuery(query)
						.setParameter("sampleID", sampleID)
						.setParameter("interfaceName", interfaceName);
			} else {
				query = "select distinct tlmi.interface_id as \"interfaceId\", tlmi.sample_no as \"sampleNo\", tlmi.parameter_name as \"testDesc\", tlmi.Parameter_value as \"interfaceResult\" "
						+ "from lab.t_lab_machine_interface tlmi "
						+ "where machine_name = :interfaceName "
						+ "order by interface_id desc limit 1";

				sqlQuery = sessionFactory.getCurrentSession()
						.createSQLQuery(query)
						.setParameter("interfaceName", interfaceName);
			}

			listLabSampleMasterDto1 = sqlQuery.setResultTransformer(Transformers.aliasToBean(LabSampleMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, listLabSampleMasterDto1, null);
		} catch (Exception e) {
			System.out.println(e);
			logger.error("Exception", e);
			return new Response(ERROR, ERR_500, e.toString(), null, null);
		}
		}

	@Override
	public Response getOrderDetailsData(List<Integer> orderIds) throws ApplicationException {
		try {
			List<LabSampleMasterDto> labSampleMasterDtoList =  sessionFactory.getCurrentSession()
					.createSQLQuery(
							" select "
									+ "count(*) as \"realServiceCount\" ,"
									+ "caetod.order_id  as \"orderId\" from t_order_details caetod where caetod.order_id in(:orderIds) and caetod.is_dummy_service = false group by caetod.order_id  ")
					.setParameterList("orderIds", orderIds)
					.setResultTransformer(Transformers.aliasToBean(LabSampleMasterDto.class)).list();
			List<LabSampleMasterDto> labSampleMasterDtoListForPriority =  sessionFactory.getCurrentSession()
					.createSQLQuery(
							" select "
									+ "count(*) as \"priorityCount\" ,"
									+ "caetod.order_id  as \"orderId\" from t_order_details caetod where caetod.order_id in(:orderIds) and  caetod.priority_id = 2 group by caetod.order_id  ")
					.setParameterList("orderIds", orderIds)
					.setResultTransformer(Transformers.aliasToBean(LabSampleMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_200, null, labSampleMasterDtoList, null,labSampleMasterDtoListForPriority);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

//	@Override
//	public Response getPOCLabServices(int orgId, int orgUnitId, int patientId, int admissionId, int deptId)
//			throws ApplicationException {
//		List<LabSampleDetailsMasterDtoIP> listLabSampleDetailsMasterDto = null;
//		try {
//
//			listLabSampleDetailsMasterDto = sessionFactory.getCurrentSession().getNamedQuery("GET_POC_LAB_ORDER_DETAILS")
//					.setParameter("orgId", orgId).setParameter("orgUnitId", orgUnitId).setParameter("patientId", patientId)
//					.setParameter("admissionId", admissionId).setParameter("deptId", deptId)
////					.setParameterList("visitTypeIds", Arrays.asList(ICommonConstants.IP, ICommonConstants.DC, ICommonConstants.ER))
//					.setParameterList("visitTypeIds", Arrays.asList(ICommonConstants.ER))
//					.setParameter("ordCancelled", 'N')
//					.setParameter("sampleStatus", 'A')
//					.setParameter("unitStatus", 'A')
//					/* .setParameter("serviceRendered", 0) */
//					.setParameter("sampleStatusId", SAMPLE_RECOLLECTED)
//					.setParameter("patientStatusId", REPAT_PATIRNT)
//					.setResultTransformer(Transformers.aliasToBean(LabSampleDetailsMasterDtoIP.class)).list();
//			return new Response(SUCCESS, SUCCESS_200, null, listLabSampleDetailsMasterDto, null);
//		} catch (Exception e) {
//			logger.error("Exception", e);
//			throw e;
//		}
//	}

}
