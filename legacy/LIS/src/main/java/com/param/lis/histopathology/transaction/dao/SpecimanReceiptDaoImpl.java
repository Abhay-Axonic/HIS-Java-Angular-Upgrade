package com.param.lis.histopathology.transaction.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.entity.lis.transaction.LabSampleDetailsMaster;
import com.param.entity.lis.transaction.LabSampleMaster;
import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.ITransactionConstants;
import com.param.lis.global.common.Response;
import com.param.lis.global.common.SearchDto;
import com.param.lis.global.common.StringCommonMethods;
import com.param.lis.histopathology.transaction.dto.specimanReceiptDto;
import com.param.lis.transaction.dao.ILabCentralReceivingDao;
import com.param.lis.transaction.dao.ILabTransactionDao;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings(
{ "rawtypes", "unchecked" })
public class SpecimanReceiptDaoImpl extends GenericDao<LabSampleMaster, Integer>
		implements ISpecimanReceiptDao, ICommonConstants, IError,ITransactionConstants
{

	final static Logger logger = Logger.getLogger(SpecimanReceiptDaoImpl.class);
	public SpecimanReceiptDaoImpl()
	{
		super(LabSampleMaster.class);
	}
	
   @Autowired 
  private ILabTransactionDao iLabTransactionDao;
   
   
   
   @Autowired 
   private ILabCentralReceivingDao iLabCentralReceivingDao;
   
	
	@Override
	public Response listSpecimanReceipt(SearchDto searchDto,Character isPreOrPostBilling) throws ApplicationException
	{
		try
		{
			List<Integer>  subDeptIds = new ArrayList<Integer>();
			List<Integer>  testTypes = new ArrayList<Integer>();
			List<Integer>  visitTypes = new ArrayList<Integer>();
			List<Integer>  specimanTypes = new ArrayList<Integer>();

			if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size()>0) {
				visitTypes = searchDto.getVisitTypes().stream()
						.map(obj -> obj.getId())
						.collect(Collectors.toList());

			}

			if (searchDto.getSubDepts() != null && searchDto.getSubDepts().size()>0) {
				subDeptIds = searchDto.getSubDepts().stream()
						.map(obj -> obj.getId())
						.collect(Collectors.toList());
			}

			if (searchDto.getTestTypes() != null && searchDto.getTestTypes().size()>0) {
				testTypes = searchDto.getTestTypes().stream()
						.map(obj -> obj.getId())
						.collect(Collectors.toList());
			}

			if (searchDto.getSpecimanTypes() != null && searchDto.getSpecimanTypes().size()>0) {
				specimanTypes = searchDto.getSpecimanTypes().stream()
						.map(obj -> obj.getId())
						.collect(Collectors.toList());
			}

			     Query sql = sessionFactory.getCurrentSession()
							.createSQLQuery(this.getSpecimanReciptQuery(searchDto, isPreOrPostBilling))
							.setParameter("orgId", searchDto.getOrgId())
							.setParameter("orgUnitId", searchDto.getOrgUnitId())
							.setParameterList("orderStatusId",  CANCEL_ORDER);
			if(isPreOrPostBilling=='Y')
			{
				if (searchDto.getFromDt() != null ){
					sql.setParameter("fromDt", searchDto.getFromDt());
				}
				if (searchDto.getToDt() != null ){
					sql.setParameter("toDt", searchDto.getToDt());
				}
				if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0){
					sql.setParameter("patientId", searchDto.getPatientId());
				}
				if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size()>0){
					sql.setParameterList("visitTypes", visitTypes);
				}
				if (searchDto.getSubDepts() != null && searchDto.getSubDepts().size()>0){
					sql.setParameterList("subDepts", subDeptIds);
				}

				if (searchDto.getTestTypes() != null && searchDto.getTestTypes().size()>0){
					sql.setParameterList("testTypes", testTypes);
				}
				if (searchDto.getLabSampleId() != null && searchDto.getLabSampleId() > 0){
					sql.setParameter("labSampleId", searchDto.getLabSampleId());
				}
				if (searchDto.getSpecimanTypes() != null && searchDto.getSpecimanTypes().size()>0){
					sql.setParameterList("specimanTypes", specimanTypes);
				}
			}
			else {
				if (searchDto.getFromDt() != null ){
					sql.setParameter("fromDt", searchDto.getFromDt());
				}
				if (searchDto.getToDt() != null ){
					sql.setParameter("toDt", searchDto.getToDt());
				}
				if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0){
					sql.setParameter("patientId", searchDto.getPatientId());
				}
				if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size()>0){
					sql.setParameterList("visitTypes", visitTypes);
				}
				if (searchDto.getSubDepts() != null && searchDto.getSubDepts().size()>0){
					sql.setParameterList("subDepts", subDeptIds);
				}
				if (searchDto.getTestTypes() != null && searchDto.getTestTypes().size()>0){
					sql.setParameterList("testTypes", testTypes);
				}
				if (searchDto.getLabSampleId() != null && searchDto.getLabSampleId() > 0){
					sql.setParameter("labSampleId", searchDto.getLabSampleId());
				}
				if (searchDto.getSpecimanTypes() != null && searchDto.getSpecimanTypes().size()>0){
					sql.setParameterList("specimanTypes", specimanTypes);
				}
			}
							sql.setFirstResult(searchDto.getOffset() != null ? searchDto.getOffset() : 0);
							sql.setMaxResults(searchDto.getRecordPerPage() != null ? searchDto.getRecordPerPage() : 50);
			List<specimanReceiptDto> specimanReceiptDtoList = sql.setResultTransformer(Transformers.aliasToBean(specimanReceiptDto.class)).list();
			
			if (specimanReceiptDtoList.isEmpty()) {
				return new Response(SUCCESS, SUCCESS_200, SAMPLE_RECORDS_NOT_FOUND, null, null);
			} else {
				return new Response(SUCCESS, SUCCESS_200, null, specimanReceiptDtoList, null);
			}
			
		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	public Response getTotalSpecimanReceiptRecord(SearchDto searchDto,Character isPreOrPostBilling) throws ApplicationException
	{
		try
		{
			  BigInteger count =this.getSpecimanReciptQueryCount(searchDto, isPreOrPostBilling);
				return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, count);

		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}


	@Override
	public Response collectionSpeciman(LabSampleMaster labSampleMaster) throws ApplicationException {
		try
		{
				LabSampleMaster labSampleMst = save(labSampleMaster);
					if (labSampleMst != null)
					{
						for (Iterator iterator = labSampleMst.getListLabSampleDetailsMaster().iterator(); iterator.hasNext();)
						{
							LabSampleDetailsMaster labSampleDetailsMaster = (LabSampleDetailsMaster) iterator
									.next();
							labSampleDetailsMaster.setSampleStatusId(SPECIMAN_ACCEPTED);
							labSampleDetailsMaster.setLabSampleId(labSampleMst.getLabSampleId());
							labSampleDetailsMaster.setIsReportRecall(false);
							Response<LabSampleDetailsMaster, Integer> labSampleDetailsRes = iLabCentralReceivingDao.samplCollection(labSampleDetailsMaster);
							 if(labSampleDetailsRes.getCode().equals(SUCCESS_200))
							{
								 Response res =  iLabTransactionDao.updateServiceRenderStatus(labSampleDetailsRes.getObject().getOrderDetailsId(),labSampleDetailsMaster.getOrgUnitId());
								 if(res.getCode().equals(SUCCESS_200))
								 {
									
									 iLabTransactionDao.updateOrderStatus(labSampleMaster.getOrderId(), 3); //ORDER_COMPLETED =2
									 return new Response(SUCCESS, SUCCESS_200, "Speciman Accepted Successfully.", null, labSampleDetailsRes.getObject());
								 }
								 else
								 {
										return new Response(ERROR, ERR_500, "Falied to Accept Speciman.", null, null);
								 }
							}
							else{
								return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
							  }
						}
						return new Response(ERROR, ERR_500, "Opps something went wrong.", null, null);
					} 
					else{
						return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
					}
			
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	public Response updateLabSampleDetailsMaster(LabSampleDetailsMaster labSampleDetailsMaster)
			throws ApplicationException
	{
		try {
			Integer labSampleDetailsStatus = (Integer) sessionFactory.getCurrentSession()
					.getNamedQuery("UPDATE_LAB_SAMPLE_DETAILS")
					.setParameter("assignUnit", labSampleDetailsMaster.getOrgUnitId())
					.setParameter("performedUnit", labSampleDetailsMaster.getOrgUnitId())
					.setParameter("updatedBy", labSampleDetailsMaster.getUpdatedBy())
					.setParameter("updatedDate", TimeZoneComponent.getDateByzone(labSampleDetailsMaster.getOrgUnitId()))
					.setParameter("sampleStatausId", labSampleDetailsMaster.getSampleStatusId())
					.setParameter("labSampleDtlsId", labSampleDetailsMaster.getLabSampleDtlsId()).executeUpdate();
			if (labSampleDetailsStatus > 0) 
			{
				return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, labSampleDetailsStatus);
			} else {
				return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null,  Integer.valueOf(0));
			}
		} catch (HibernateException es) {
			logger.error("Exection", es);throw es;
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}
	
	public String getSpecimanReciptQuery(SearchDto searchDto,Character isPreOrPostBilling)
	{
		StringBuffer query = new StringBuffer();
		if(isPreOrPostBilling=='Y')
		{
			//op
			query.append(  "SELECT "
					+ "  to_char(ord_mst.order_date,'DD-MM-YYYY') AS \"orderDate\", "
					+ "	subspe_mst.sub_speciality_name AS \"deptName\", "
					+ "	speciman_mst.speciman_name AS \"specimanType\", "
					+ "	speciman_mst.speciman_id AS \"specimanTypeId\", "
					+ "	vistmst.visit_type_code AS \"visitType\", "
					+ "	ord_mst.order_id AS \"orderId\", "
					+ "	ord_details.order_details_id AS \"orderDetailsId\", "
					+ "	ord_details.package_id AS \"packageId\", "
					+ "	test_mst.dept_id AS \"subDeptId\", "
					+ "	test_mst.test_id AS \"testId\", "
					+ "	test_mst.test_desc AS \"testDesc\", "
					+ "	test_mst.sample_id AS \"sampleId\", "
	                 + "    bill.bill_number as \"billNumber\", " 
					+ "	test_mst.container_id AS \"containerId\", "
					+ "	test_mst.is_outsourced AS \"isOutsourced\", "
					+ "	test_mst.is_histo_cyto AS \"isHistoCyto\", "
					+ "	test_mst.is_gynac AS \"isGynac\", "
					+ "	test_mst.is_fnac AS \"isFnac\", "
					+ "	gen_mst.gender_id AS \"genderId\", "
					+ "	ord_mst.order_status_id AS \"orderStatusId\", "
					+ "	ord_mst.org_id AS \"orgId\", "
					+ "	ord_mst.org_unit_id AS \"orgUnitId\", "
					+ "	ord_mst.visit_type_id AS \"visitTypeId\", "
					+ "	ord_mst.ord_doctor_id AS \"doctorId\", "
					+ "	ord_mst.patient_id AS \"patientId\", "
					+ "	ord_mst.admission_id AS \"visitAdmId\", "
					+ "    ord_mst.encounter_id AS \"encounterId\", "
					+ "	pati_mst.uhid_number AS \"uhid\", "
					+ "	pre_mst.prefix_code || ' ' || pati_mst.first_name || ' ' || coalesce(pati_mst.middle_name) || ' '|| pati_mst.last_name || ' ' || calculate_age(pati_mst.dob) "
					+ " || '/' || gen_mst.gender_code AS \"patientDetails\", "
					+ "	'Dr.' || doct_mst.first_name || ' ' || doct_mst.last_name AS \"doctorDetails\", "
					+ "  wardmast.ward_code AS \"wardCode\", "
					+ "  bedmst.bed_number AS \"bedNumber\", "
					+ "	pri_mst.priority_id AS \"priorityId\", "
					+ "	pri_mst.priority_name AS \"priorityName\", "
					+ "	pri_mst.color_code AS \"colorCode\", "
					+ "	null as \"panelCode\", "
					/*+ " (case when ord_mst.encounter_id > 0 then "
					+ " (select mpem.payment_entitlement_desc from opd.t_patient_sponsor_details tpsd "
					+ " inner join public.m_payment_entitlement_master mpem on tpsd.payment_entitlement_id = mpem.payment_entitlement_id "
					+ " and tpsd.encounter_id = ord_mst.encounter_id where tpsd.priority_id = 1 limit 1 ) "
					+ " else (select mpem.payment_entitlement_desc from opd.t_patient_sponsor_details tpsd "
					+ " inner join public.m_payment_entitlement_master mpem on tpsd.payment_entitlement_id = mpem.payment_entitlement_id "
					+ " and tpsd.admission_id = ord_mst.admission_id where tpsd.priority_id = 1 limit 1 ) end ) as \"entitlementDesc\" "*/
					+" (case when ord_mst.encounter_id > 0 then " +
					" ( tpsd.payment_entitlement_id ) " +
					" else ( tpsd2.payment_entitlement_id ) end ) as \"paymentEntitlementId\" "
					+ " FROM "
					+ "	public.t_order_master ord_mst "
					+ " INNER JOIN public.t_order_details ord_details ON "
					+ "	ord_details.order_id = ord_mst.order_id "
					+ " INNER JOIN lab.m_test_master test_mst ON "
					+ "	test_mst.service_id = ord_details.service_id "
					+ " INNER JOIN patient.t_patient_registration pati_mst ON "
					+ "	ord_mst.patient_id = pati_mst.patient_id "
					+ " INNER JOIN m_gender_master gen_mst ON "
					+ "	gen_mst.gender_id = pati_mst.gender_id "
					+ " INNER JOIN m_prefix_master pre_mst ON "
					+ "	pre_mst.prefix_id = pati_mst.prefix_id "
					+ " INNER JOIN m_visit_type_master vistmst ON "
					+ "	vistmst.visit_type_id = ord_mst.visit_type_id "
					+ " INNER JOIN lab.m_speciman_master speciman_mst ON "
					+ "	speciman_mst.speciman_id = test_mst.speciman_id "
					+ " INNER join billing.t_billing_details bill_details ON "
					+ "	ord_details.order_details_id = bill_details.order_details_id "
					+ " INNER join billing.m_billing_master bill ON "
					+ "	bill_details.billing_master_id = bill.billing_master_id and bill.bill_cancelled = 'N' "
					+ " LEFT JOIN doctor.m_doctor_master doct_mst ON "
					+ "	ord_mst.ord_doctor_id = doct_mst.doctor_id "
					+ " LEFT JOIN doctor.m_sub_speciality_master subspe_mst ON "
					+ "	subspe_mst.sub_speciality_id = test_mst.dept_id "
					+ " LEFT JOIN lab.m_priority_master pri_mst ON "
					+ "	ord_mst.priority_id = pri_mst.priority_id "
					+ " left join adt.t_admission ta on ta.admission_id = ord_mst.admission_id and ta.status='A' "
					+ " left join opd.t_patient_sponsor_details tpsd "
					+ " on ord_mst.encounter_id = tpsd.encounter_id and tpsd.priority_id = 1 and tpsd.status='A' "
					+ " left join opd.t_patient_sponsor_details tpsd2 "
					+ " on tpsd2.admission_id = ta.admission_id and tpsd2.priority_id = 1 and tpsd2.status='A' "
					+ " LEFT JOIN adt.t_admission_details admision_dtls ON admision_dtls.admission_id = ord_mst.admission_id and admision_dtls.status = 'A'  and admision_dtls.nursing_acceptance = 'Y' "
					+ " LEFT JOIN adt.m_ward_master wardmast ON admision_dtls.ward_id = wardmast.ward_id "
					+ " LEFT JOIN adt.m_bed_master bedmst ON admision_dtls.bed_id = bedmst.bed_id "
					+ " INNER JOIN public.t_lab_configuration config on ord_mst.org_unit_id = config.unit_id "
					+ " WHERE "
					+ "	ord_mst.org_id =:orgId "
					+ "	AND ord_mst.org_unit_id =:orgUnitId "
					+ "	AND ord_mst.order_status_id NOT IN(:orderStatusId) "
					+ "	AND ord_details.service_rendered =0 "
					+ "	AND ord_details.service_is_billed in (1) "
					+ "	AND ord_details.service_rendering_deptid in ( coalesce(config.histopathology_id,0),coalesce(config.cystopathology_id,0) ) "
					+ "	and ord_details.ord_cancelled = 'N' "
		    		+ "	and ord_details.status = 'A' "
					+ "	AND ord_details.is_outsourced ='N' "
					+ "	AND test_mst.is_histo_cyto IN ('C','H') "
					+ "    and test_mst.test_status = 'A' "
					//+ "    and bill.bill_status_id <> 1 "
					+ "    AND ord_mst.visit_type_id in (1) "
					+ "	AND test_mst.org_unit_id =:orgUnitId ");
					
					if (searchDto.getFromDt() != null ) 
						  query.append(" AND to_char(ord_mst.order_date,'YYYY-MM-DD') >=:fromDt  ");
					  if (searchDto.getToDt() != null ) 
				    	  query.append(" AND to_char(ord_mst.order_date,'YYYY-MM-DD') <=:toDt ");
				      
					  if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0)
				    	  query.append(" AND pati_mst.patient_id =:patientId  ");

				      if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size()>0)
				    	  query.append(" AND vistmst.visit_type_id IN (:visitTypes)");

				      if (searchDto.getSubDepts() != null && searchDto.getSubDepts().size()>0)
				    	  query.append(" AND test_mst.dept_id IN (:subDepts)");
				      
				      if (searchDto.getTestTypes() != null && searchDto.getTestTypes().size()>0)
				    	  query.append(" AND test_mst.test_id in (:testTypes)");
				      
				      if (searchDto.getLabSampleId() != null && searchDto.getLabSampleId() > 0)
				    	  query.append(" AND lbsampledtls.lab_sample_id =:labSampleId ");
					
				      if (searchDto.getSpecimanTypes() != null && searchDto.getSpecimanTypes().size()>0)
				    	  query.append(" AND test_mst.speciman_id in (:specimanTypes)");
					
				      
				     query.append( " union "
					+ " SELECT "
					+ "  to_char(ord_mst.order_date,'DD-MM-YYYY') AS \"orderDate\", "
					+ "	subspe_mst.sub_speciality_name AS \"deptName\", "
					+ "	speciman_mst.speciman_name AS \"specimanType\", "
					+ "	speciman_mst.speciman_id AS \"specimanTypeId\", "
					+ "	vistmst.visit_type_code AS \"visitType\", "
					+ "	ord_mst.order_id AS \"orderId\", "
					+ "	ord_details.order_details_id AS \"orderDetailsId\", "
					+ "	ord_details.package_id AS \"packageId\", "
					+ "	test_mst.dept_id AS \"subDeptId\", "
					+ "	test_mst.test_id AS \"testId\", "
					+ "	test_mst.test_desc AS \"testDesc\", "
					+ "	test_mst.sample_id AS \"sampleId\", "
	                 + "    bill.bill_number as \"billNumber\", " 

					+ "	test_mst.container_id AS \"containerId\", "
					+ "	test_mst.is_outsourced AS \"isOutsourced\", "
					+ "	test_mst.is_histo_cyto AS \"isHistoCyto\", "
					+ "	test_mst.is_gynac AS \"isGynac\", "
					+ "	test_mst.is_fnac AS \"isFnac\", "
					+ "	gen_mst.gender_id AS \"genderId\", "
					+ "	ord_mst.order_status_id AS \"orderStatusId\", "
					+ "	ord_mst.org_id AS \"orgId\", "
					+ "	ord_mst.org_unit_id AS \"orgUnitId\", "
					+ "	ord_mst.visit_type_id AS \"visitTypeId\", "
					+ "	ord_mst.ord_doctor_id AS \"doctorId\", "
					+ "	ord_mst.patient_id AS \"patientId\", "
					+ "	ord_mst.admission_id AS \"visitAdmId\", "
					+ " ord_mst.encounter_id AS \"encounterId\", "
					+ "	pati_mst.uhid_number AS \"uhid\", "
					+ "	pre_mst.prefix_code || ' ' || pati_mst.first_name || ' '  || coalesce(pati_mst.middle_name) || ' '|| pati_mst.last_name || ' ' || calculate_age(pati_mst.dob) "
					+ " || '/' || gen_mst.gender_code AS \"patientDetails\", "
					+ "	'Dr.' || doct_mst.first_name || ' ' || doct_mst.last_name AS \"doctorDetails\", "
					+ "  wardmast.ward_code AS \"wardCode\", "
					+ "  bedmst.bed_number AS \"bedNumber\", "
					+ "	pri_mst.priority_id AS \"priorityId\", "
					+ "	pri_mst.priority_name AS \"priorityName\", "
					+ "	pri_mst.color_code AS \"colorCode\", "
					+ "	panel_mst.panel_code as \"panelCode\", "
					/*+ " (case when ord_mst.encounter_id > 0 then "
					+ " (select mpem.payment_entitlement_desc from opd.t_patient_sponsor_details tpsd "
					+ " inner join public.m_payment_entitlement_master mpem on tpsd.payment_entitlement_id = mpem.payment_entitlement_id "
					+ " and tpsd.encounter_id = ord_mst.encounter_id where tpsd.priority_id = 1 limit 1 ) "
					+ " else (select mpem.payment_entitlement_desc from opd.t_patient_sponsor_details tpsd "
					+ " inner join public.m_payment_entitlement_master mpem on tpsd.payment_entitlement_id = mpem.payment_entitlement_id "
					+ " and tpsd.admission_id = ord_mst.admission_id where tpsd.priority_id = 1 limit 1 ) end ) as \"entitlementDesc\" "*/
					+" (case when ord_mst.encounter_id > 0 then " +
					" ( tpsd.payment_entitlement_id ) " +
					" else ( tpsd2.payment_entitlement_id ) end ) as \"paymentEntitlementId\" "
					+ " FROM "
					+ "	public.t_order_master ord_mst "
					+ " INNER JOIN public.t_order_details ord_details ON "
					+ "	ord_details.order_id = ord_mst.order_id "
					+ " INNER JOIN service.t_unit_service_master_details ser_dtls ON "
					+ "	ord_details.service_id = ser_dtls.service_master_id "
					+ " INNER JOIN patient.t_patient_registration pati_mst ON "
					+ "	ord_mst.patient_id = pati_mst.patient_id "
					+ " INNER JOIN m_gender_master gen_mst ON "
					+ "	gen_mst.gender_id = pati_mst.gender_id "
					+ " INNER JOIN m_prefix_master pre_mst ON "
					+ "	pre_mst.prefix_id = pati_mst.prefix_id "
					+ " INNER JOIN m_visit_type_master vistmst ON "
					+ "	vistmst.visit_type_id = ord_mst.visit_type_id "
					+ " INNER JOIN lab.t_panel_master panel_mst ON "
					+ "	panel_mst.service_id = ord_details.service_id "
					+ " INNER JOIN lab.t_panel_details panel_dtls ON "
					+ "	panel_mst.panel_id = panel_dtls.panel_id "
					+ "INNER JOIN lab.m_test_master test_mst ON "
					+ "	test_mst.test_id = panel_dtls.test_id "
					+ " INNER JOIN lab.m_speciman_master speciman_mst ON "
					+ "	speciman_mst.speciman_id = test_mst.speciman_id "
					+ "INNER join billing.t_billing_details bill_details ON "
					+ "	ord_details.order_details_id = bill_details.order_details_id "
					+ " INNER join billing.m_billing_master bill ON "
					+ "	bill_details.billing_master_id = bill.billing_master_id and bill.bill_cancelled = 'N' "
					+ " LEFT JOIN doctor.m_doctor_master doct_mst ON "
					+ "	ord_mst.ord_doctor_id = doct_mst.doctor_id "
					+ " LEFT JOIN doctor.m_sub_speciality_master subspe_mst ON "
					+ "	subspe_mst.sub_speciality_id = test_mst.dept_id "
					+ " LEFT JOIN lab.m_priority_master pri_mst ON "
					+ "	ord_mst.priority_id = pri_mst.priority_id "
					+ " left join adt.t_admission ta on ta.admission_id = ord_mst.admission_id and ta.status='A' "
					+ " left join opd.t_patient_sponsor_details tpsd "
					+ " on ord_mst.encounter_id = tpsd.encounter_id and tpsd.priority_id = 1 and tpsd.status='A' "
					+ " left join opd.t_patient_sponsor_details tpsd2 "
					+ " on tpsd2.admission_id = ta.admission_id and tpsd2.priority_id = 1 and tpsd2.status='A' "
					+ " LEFT JOIN adt.t_admission_details admision_dtls ON admision_dtls.admission_id = ord_mst.admission_id and admision_dtls.status = 'A'  and admision_dtls.nursing_acceptance = 'Y' "
					+ " LEFT JOIN adt.m_ward_master wardmast ON admision_dtls.ward_id = wardmast.ward_id "
					+ " LEFT JOIN adt.m_bed_master bedmst ON admision_dtls.bed_id = bedmst.bed_id "
					+ " INNER JOIN public.t_lab_configuration config on ord_mst.org_unit_id = config.unit_id "
					+ " WHERE "
					+ "	ord_mst.org_id =:orgId "
					+ "	AND ord_mst.org_unit_id =:orgUnitId "
					+ "	AND ord_mst.order_status_id NOT IN(:orderStatusId) "
					+ "	AND ord_details.service_rendered = 0 "
					+ "	AND ord_details.service_is_billed in (1) "
					+ "	AND test_mst.dept_id in ( coalesce(config.histopathology_id,0),coalesce(config.cystopathology_id,0) ) "
					+ "	AND ord_details.ord_cancelled ='N' "
					+ "	AND ord_details.is_outsourced ='N' "
					+ " AND ord_details.status ='A' "
					+ "	AND test_mst.is_histo_cyto IN ('C','H') "
					+ " AND test_mst.test_status = 'A' "
					+ " AND panel_mst.status  ='A' "
					+ " AND panel_dtls.is_deleted = 'N' "
					+ " AND ser_dtls.is_panel = 'Y' "
					+ " AND ser_dtls.unit_id = ord_details.org_unit_id " 
					//+ " AND bill.bill_status_id <> 1 "
					+ " AND ord_mst.visit_type_id in (1) "
					+ "	AND test_mst.org_unit_id =:orgUnitId ");
			
			  if (searchDto.getFromDt() != null ) 
				  query.append(" AND to_char(ord_mst.order_date,'YYYY-MM-DD') >=:fromDt ");
			  if (searchDto.getToDt() != null ) 
		    	  query.append(" AND to_char(ord_mst.order_date,'YYYY-MM-DD') <=:toDt  ");
		      
			  if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0)
		    	  query.append(" AND pati_mst.patient_id =:patientId  ");

		      if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size()>0)
		    	  query.append(" AND vistmst.visit_type_id IN (:visitTypes)");

		      if (searchDto.getSubDepts() != null && searchDto.getSubDepts().size()>0)
		    	  query.append(" AND test_mst.dept_id IN (:subDepts)");
		      
		      if (searchDto.getTestTypes() != null && searchDto.getTestTypes().size()>0)
		    	  query.append(" AND test_mst.test_id in (:testTypes)");
		      
		      if (searchDto.getLabSampleId() != null && searchDto.getLabSampleId() > 0)
		    	  query.append(" AND lbsampledtls.lab_sample_id =:labSampleId  ");
		      
		      if (searchDto.getSpecimanTypes() != null && searchDto.getSpecimanTypes().size()>0)
		    	  query.append(" AND test_mst.speciman_id in (:specimanTypes)");
			
			
			query.append( " UNION SELECT "
					+ "  to_char(ord_mst.order_date,'DD-MM-YYYY') AS \"orderDate\", "
					+ "  subspe_mst.sub_speciality_name AS \"deptName\", "
					+ "  speciman_mst.speciman_name AS \"specimanType\", "
					+ "  speciman_mst.speciman_id AS \"specimanTypeId\", "
					+ "  vistmst.visit_type_code AS \"visitType\", "
					+ "  ord_mst.order_id AS \"orderId\", "
					+ "  ord_details.order_details_id AS \"orderDetailsId\", "
					+ "  ord_details.package_id AS \"packageId\", "
					+ "  test_mst.dept_id AS \"subDeptId\", "
					+ "  test_mst.test_id AS \"testId\", "
					+ "  test_mst.test_desc AS \"testDesc\", "
					+ "  test_mst.sample_id AS \"sampleId\", "
	                 + "    bill.bill_number as \"billNumber\", " 
					+ "  test_mst.container_id AS \"containerId\", "
					+ "  test_mst.is_outsourced AS \"isOutsourced\", "
					+ "  test_mst.is_histo_cyto AS \"isHistoCyto\", "
					+ "  test_mst.is_gynac AS \"isGynac\", "
					+ "  test_mst.is_fnac AS \"isFnac\", "
					+ "  gen_mst.gender_id AS \"genderId\", "
					+ "  ord_mst.order_status_id AS \"orderStatusId\", "
					+ "  ord_mst.org_id AS \"orgId\", "
					+ "  ord_mst.org_unit_id AS \"orgUnitId\", "
					+ "  ord_mst.visit_type_id AS \"visitTypeId\", "
					+ "  ord_mst.ord_doctor_id AS \"doctorId\", "
					+ "  ord_mst.patient_id AS \"patientId\", "
					+ "  ord_mst.admission_id AS \"visitAdmId\", "
					+ "    ord_mst.encounter_id AS \"encounterId\", "
					+ "  pati_mst.uhid_number AS \"uhid\", "
					+ "  pre_mst.prefix_code || ' ' || pati_mst.first_name || ' '  || coalesce(pati_mst.middle_name) ||' '|| pati_mst.last_name || ' ' || "
					+ " calculate_age(pati_mst.dob)  || '/' || gen_mst.gender_code AS \"patientDetails\", "
					+ "  'Dr.' || doct_mst.first_name || ' ' || doct_mst.last_name AS \"doctorDetails\", "
					+ "  wardmast.ward_code AS \"wardCode\", "
					+ "  bedmst.bed_number AS \"bedNumber\", "
					+ "  pri_mst.priority_id AS \"priorityId\", "
					+ "  pri_mst.priority_name AS \"priorityName\", "
					+ "  pri_mst.color_code AS \"colorCode\", "
					+ "  NULL AS \"panelCode\", "
					/*+ " (case when ord_mst.encounter_id > 0 then "
					+ " (select mpem.payment_entitlement_desc from opd.t_patient_sponsor_details tpsd "
					+ " inner join public.m_payment_entitlement_master mpem on tpsd.payment_entitlement_id = mpem.payment_entitlement_id "
					+ " and tpsd.encounter_id = ord_mst.encounter_id where tpsd.priority_id = 1 limit 1 ) "
					+ " else (select mpem.payment_entitlement_desc from opd.t_patient_sponsor_details tpsd "
					+ " inner join public.m_payment_entitlement_master mpem on tpsd.payment_entitlement_id = mpem.payment_entitlement_id "
					+ " and tpsd.admission_id = ord_mst.admission_id where tpsd.priority_id = 1 limit 1 ) end ) as \"entitlementDesc\" "*/
					+" (case when ord_mst.encounter_id > 0 then " +
					" ( tpsd.payment_entitlement_id ) " +
					" else ( tpsd2.payment_entitlement_id ) end ) as \"paymentEntitlementId\" "
					+ " FROM "
					+ "  PUBLIC.t_order_master ord_mst "
					+ " INNER JOIN PUBLIC.t_order_details ord_details ON "
					+ "  ord_details.order_id = ord_mst.order_id "
					+ " INNER JOIN lab.m_test_master test_mst ON "
					+ "  test_mst.service_id = ord_details.service_id "
					+ " INNER JOIN patient.t_patient_registration pati_mst ON "
					+ "  ord_mst.patient_id = pati_mst.patient_id "
					+ " INNER JOIN m_gender_master gen_mst ON "
					+ "  gen_mst.gender_id = pati_mst.gender_id "
					+ " INNER JOIN m_prefix_master pre_mst ON "
					+ "  pre_mst.prefix_id = pati_mst.prefix_id "
					+ " INNER JOIN m_visit_type_master vistmst ON "
					+ "  vistmst.visit_type_id = ord_mst.visit_type_id "
					+ " INNER JOIN lab.m_speciman_master speciman_mst ON "
					+ "  speciman_mst.speciman_id = test_mst.speciman_id "
					+ " LEFT join billing.t_billing_details bill_details ON "
					+ "	ord_details.order_details_id = bill_details.order_details_id "
					+ " LEFT join billing.m_billing_master bill ON "
					+ "	bill_details.billing_master_id = bill.billing_master_id and bill.bill_cancelled = 'N' "
					+ " LEFT JOIN doctor.m_doctor_master doct_mst ON "
					+ "  ord_mst.ord_doctor_id = doct_mst.doctor_id "
					+ " LEFT JOIN doctor.m_sub_speciality_master subspe_mst ON "
					+ "  subspe_mst.sub_speciality_id = test_mst.dept_id "
					+ " LEFT JOIN lab.m_priority_master pri_mst ON "
					+ "  ord_mst.priority_id = pri_mst.priority_id "
					+ " left join adt.t_admission ta on ta.admission_id = ord_mst.admission_id and ta.status='A' "
					+ " left join opd.t_patient_sponsor_details tpsd "
					+ " on ord_mst.encounter_id = tpsd.encounter_id and tpsd.priority_id = 1 and tpsd.status='A' "
					+ " left join opd.t_patient_sponsor_details tpsd2 "
					+ " on tpsd2.admission_id = ta.admission_id and tpsd2.priority_id = 1 and tpsd2.status='A' "
					+ " LEFT JOIN adt.t_admission_details admision_dtls ON admision_dtls.admission_id = ord_mst.admission_id AND admision_dtls.status = 'A'  AND admision_dtls.nursing_acceptance = 'Y' "
					+ " LEFT JOIN adt.m_ward_master wardmast ON admision_dtls.ward_id = wardmast.ward_id "
					+ " LEFT JOIN adt.m_bed_master bedmst ON admision_dtls.bed_id = bedmst.bed_id "
					+ " INNER JOIN PUBLIC.t_lab_configuration config ON ord_mst.org_unit_id = config.unit_id "
					+ " WHERE "
					+ "  ord_mst.org_id =:orgId "
					+ "  AND ord_mst.org_unit_id =:orgUnitId "
					+ "  AND ord_mst.order_status_id NOT IN(:orderStatusId) "
					+ "  AND ord_details.service_rendered = 0 "
					+ "  AND ord_details.service_is_billed IN (0,1) "
					+ "  AND ord_details.service_rendering_deptid IN ( COALESCE(config.histopathology_id,0),COALESCE(config.cystopathology_id,0) ) "
					+ "  AND ord_details.ord_cancelled ='N' "
					+ "  AND ord_details.is_outsourced ='N' "
				   + "   AND ord_details.status ='A' "
					+ "  AND test_mst.is_histo_cyto IN ('C','H') "
					+ "  AND test_mst.test_status = 'A' "
					+ "  AND ord_mst.visit_type_id IN (2,3,4) "
					+ "	AND test_mst.org_unit_id =:orgUnitId ");
			
					  if (searchDto.getFromDt() != null ) 
						  query.append(" AND to_char(ord_mst.order_date,'YYYY-MM-DD') >=:fromDt ");
					  if (searchDto.getToDt() != null ) 
				    	  query.append(" AND to_char(ord_mst.order_date,'YYYY-MM-DD') <=:toDt  ");
				      
					  if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0)
				    	  query.append(" AND pati_mst.patient_id =:patientId  ");

				      if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size()>0)
				    	  query.append(" AND vistmst.visit_type_id IN (:visitTypes)");

				      if (searchDto.getSubDepts() != null && searchDto.getSubDepts().size()>0)
				    	  query.append(" AND test_mst.dept_id IN (:subDepts)");
				      
				      if (searchDto.getTestTypes() != null && searchDto.getTestTypes().size()>0)
				    	  query.append(" AND test_mst.test_id in (:testTypes)");
				      
				      if (searchDto.getLabSampleId() != null && searchDto.getLabSampleId() > 0)
				    	  query.append(" AND lbsampledtls.lab_sample_id =:labSampleId ");
				      
				      if (searchDto.getSpecimanTypes() != null && searchDto.getSpecimanTypes().size()>0)
				    	  query.append(" AND test_mst.speciman_id in (:specimanTypes)");
					
				      query.append(" UNION "
					+ " SELECT "
					+ "  to_char(ord_mst.order_date,'DD-MM-YYYY') AS \"orderDate\", "
					+ "  subspe_mst.sub_speciality_name AS \"deptName\", "
					+ "  speciman_mst.speciman_name AS \"specimanType\", "
					+ "  speciman_mst.speciman_id AS \"specimanTypeId\", "
					+ "  vistmst.visit_type_code AS \"visitType\", "
					+ "  ord_mst.order_id AS \"orderId\", "
					+ "  ord_details.order_details_id AS \"orderDetailsId\", "
					+ "  ord_details.package_id AS \"packageId\", "
					+ "  test_mst.dept_id AS \"subDeptId\", "
					+ "  test_mst.test_id AS \"testId\", "
					+ "  test_mst.test_desc AS \"testDesc\", "
					+ "  test_mst.sample_id AS \"sampleId\", "
	                 + "    bill.bill_number as \"billNumber\", " 
					+ "  test_mst.container_id AS \"containerId\", "
					+ "  test_mst.is_outsourced AS \"isOutsourced\", "
					+ "  test_mst.is_histo_cyto AS \"isHistoCyto\", "
					+ "  test_mst.is_gynac AS \"isGynac\", "
					+ "  test_mst.is_fnac AS \"isFnac\", "
					+ "  gen_mst.gender_id AS \"genderId\", "
					+ "  ord_mst.order_status_id AS \"orderStatusId\", "
					+ "  ord_mst.org_id AS \"orgId\", "
					+ "  ord_mst.org_unit_id AS \"orgUnitId\", "
					+ "  ord_mst.visit_type_id AS \"visitTypeId\", "
					+ "  ord_mst.ord_doctor_id AS \"doctorId\", "
					+ "  ord_mst.patient_id AS \"patientId\", "
					+ "  ord_mst.admission_id AS \"visitAdmId\", "
					+ " ord_mst.encounter_id AS \"encounterId\", "
					+ "  pati_mst.uhid_number AS \"uhid\", "
					+ "  pre_mst.prefix_code || ' ' || pati_mst.first_name || ' ' || coalesce(pati_mst.middle_name) || ' '|| pati_mst.last_name || ' ' || "
					+ " calculate_age(pati_mst.dob)  || '/' || gen_mst.gender_code AS \"patientDetails\", "
					+ "  'Dr.' || doct_mst.first_name || ' ' || doct_mst.last_name AS \"doctorDetails\", "
					+ "  wardmast.ward_code AS \"wardCode\", "
					+ "  bedmst.bed_number AS \"bedNumber\", "
					+ "  pri_mst.priority_id AS \"priorityId\", "
					+ "  pri_mst.priority_name AS \"priorityName\", "
					+ "  pri_mst.color_code AS \"colorCode\", "
					+ "  panel_mst.panel_code AS \"panelCode\", "
				/*	+ " (case when ord_mst.encounter_id > 0 then "
					+ " (select mpem.payment_entitlement_desc from opd.t_patient_sponsor_details tpsd "
					+ " inner join public.m_payment_entitlement_master mpem on tpsd.payment_entitlement_id = mpem.payment_entitlement_id "
					+ " and tpsd.encounter_id = ord_mst.encounter_id where tpsd.priority_id = 1 limit 1 ) "
					+ " else (select mpem.payment_entitlement_desc from opd.t_patient_sponsor_details tpsd "
					+ " inner join public.m_payment_entitlement_master mpem on tpsd.payment_entitlement_id = mpem.payment_entitlement_id "
					+ " and tpsd.admission_id = ord_mst.admission_id where tpsd.priority_id = 1 limit 1 ) end ) as \"entitlementDesc\" "*/
					+" (case when ord_mst.encounter_id > 0 then " +
					" ( tpsd.payment_entitlement_id ) " +
					" else ( tpsd2.payment_entitlement_id ) end ) as \"paymentEntitlementId\" "
					+ " FROM "
					+ "  PUBLIC.t_order_master ord_mst "
					+ " INNER JOIN PUBLIC.t_order_details ord_details ON "
					+ "  ord_details.order_id = ord_mst.order_id "
					+ " INNER JOIN service.t_unit_service_master_details ser_dtls ON "
					+ "  ord_details.service_id = ser_dtls.service_master_id "
					+ " INNER JOIN patient.t_patient_registration pati_mst ON "
					+ "  ord_mst.patient_id = pati_mst.patient_id "
					+ " INNER JOIN m_gender_master gen_mst ON "
					+ "  gen_mst.gender_id = pati_mst.gender_id "
					+ " INNER JOIN m_prefix_master pre_mst ON "
					+ "  pre_mst.prefix_id = pati_mst.prefix_id "
					+ " INNER JOIN m_visit_type_master vistmst ON "
					+ "  vistmst.visit_type_id = ord_mst.visit_type_id "
					+ " INNER JOIN lab.t_panel_master panel_mst ON "
					+ "  panel_mst.service_id = ord_details.service_id "
					+ " INNER JOIN lab.t_panel_details panel_dtls ON "
					+ "  panel_mst.panel_id = panel_dtls.panel_id "
					+ "INNER JOIN lab.m_test_master test_mst ON "
					+ "  test_mst.test_id = panel_dtls.test_id "
					+ " INNER JOIN lab.m_speciman_master speciman_mst ON "
					+ "  speciman_mst.speciman_id = test_mst.speciman_id "
					+ " LEFT join billing.t_billing_details bill_details ON "
					+ "	ord_details.order_details_id = bill_details.order_details_id "
					+ " LEFT join billing.m_billing_master bill ON "
					+ "	bill_details.billing_master_id = bill.billing_master_id and bill.bill_cancelled = 'N' "
					+ "LEFT JOIN doctor.m_doctor_master doct_mst ON "
					+ "  ord_mst.ord_doctor_id = doct_mst.doctor_id "
					+ " LEFT JOIN doctor.m_sub_speciality_master subspe_mst ON "
					+ "  subspe_mst.sub_speciality_id = test_mst.dept_id "
					+ " LEFT JOIN lab.m_priority_master pri_mst ON "
					+ "  ord_mst.priority_id = pri_mst.priority_id "
					+ " left join adt.t_admission ta on ta.admission_id = ord_mst.admission_id and ta.status='A' "
					+ " left join opd.t_patient_sponsor_details tpsd "
					+ " on ord_mst.encounter_id = tpsd.encounter_id and tpsd.priority_id = 1 and tpsd.status='A' "
					+ " left join opd.t_patient_sponsor_details tpsd2 "
					+ " on tpsd2.admission_id = ta.admission_id and tpsd2.priority_id = 1 and tpsd2.status='A' "
					+ " LEFT JOIN adt.t_admission_details admision_dtls ON admision_dtls.admission_id = ord_mst.admission_id AND admision_dtls.status = 'A'  AND admision_dtls.nursing_acceptance = 'Y' "
					+ " LEFT JOIN adt.m_ward_master wardmast ON admision_dtls.ward_id = wardmast.ward_id "
					+ " LEFT JOIN adt.m_bed_master bedmst ON admision_dtls.bed_id = bedmst.bed_id "
					+ " INNER JOIN PUBLIC.t_lab_configuration config ON ord_mst.org_unit_id = config.unit_id "
					+ " WHERE "
					+ "  ord_mst.org_id =:orgId "
					+ "  AND ord_mst.org_unit_id =:orgUnitId "
					+ "  AND ord_mst.order_status_id NOT IN(:orderStatusId) "
					+ "  AND ord_details.service_rendered = 0 "
					+ "  AND ord_details.service_is_billed IN (0,1) "
					+ "  AND test_mst.dept_id IN ( COALESCE(config.histopathology_id,0),COALESCE(config.cystopathology_id,0) ) "
					+ "  AND ord_details.ord_cancelled ='N' "
					+ "  AND ord_details.is_outsourced ='N' "
					+ "   AND ord_details.status ='A' "
					+ "  AND test_mst.is_histo_cyto IN ('C','H') "
					+ "    AND test_mst.test_status = 'A' "
					+ "    AND panel_mst.status  ='A' "
					+ "    AND panel_dtls.is_deleted = 'N' "
					+ "    AND ser_dtls.is_panel = 'Y' "
					+ "    AND ser_dtls.unit_id = ord_details.org_unit_id "
					+ "    AND ord_mst.visit_type_id IN (2,3,4) "
					+ "	AND test_mst.org_unit_id =:orgUnitId ");
				      
				      
				     
				      if (searchDto.getFromDt() != null ) 
						  query.append(" AND to_char(ord_mst.order_date,'YYYY-MM-DD') >=:fromDt ");
					  if (searchDto.getToDt() != null ) 
				    	  query.append(" AND to_char(ord_mst.order_date,'YYYY-MM-DD') <=:toDt ");
				      
					  if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0)
				    	  query.append(" AND pati_mst.patient_id =:patientId  ");

				      if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size()>0)
				    	  query.append(" AND vistmst.visit_type_id IN (:visitTypes)");

				      if (searchDto.getSubDepts() != null && searchDto.getSubDepts().size()>0)
				    	  query.append(" AND test_mst.dept_id IN (:subDepts)");
				      
				      if (searchDto.getTestTypes() != null && searchDto.getTestTypes().size()>0)
				    	  query.append(" AND test_mst.test_id in (:testTypes)");
				      
				      if (searchDto.getLabSampleId() != null && searchDto.getLabSampleId() > 0)
				    	  query.append(" AND lbsampledtls.lab_sample_id =:labSampleId  ");
				      
				      if (searchDto.getSpecimanTypes() != null && searchDto.getSpecimanTypes().size()>0)
				    	  query.append(" AND test_mst.speciman_id in (:specimanTypes)");
				      
		}
		else {
			query.append( "SELECT "
					+ "  to_char(ord_mst.order_date,'DD-MM-YYYY') AS \"orderDate\", "
					+ "  subspe_mst.sub_speciality_name AS \"deptName\", "
	                 + "    bill.bill_number as \"billNumber\", " 
					+ "  speciman_mst.speciman_name AS \"specimanType\", "
					+ "  speciman_mst.speciman_id AS \"specimanTypeId\", "
					+ "  vistmst.visit_type_code AS \"visitType\", "
					+ "  ord_mst.order_id AS \"orderId\", "
					+ "  ord_details.order_details_id AS \"orderDetailsId\", "
					+ "  ord_details.package_id AS \"packageId\", "
					+ "  test_mst.dept_id AS \"subDeptId\", "
					+ "  test_mst.test_id AS \"testId\", "
					+ "  test_mst.test_desc AS \"testDesc\", "
					+ "  test_mst.sample_id AS \"sampleId\", "
					+ "  test_mst.container_id AS \"containerId\", "
					+ "  test_mst.is_outsourced AS \"isOutsourced\", "
					+ "  test_mst.is_histo_cyto AS \"isHistoCyto\", "
					+ "  test_mst.is_gynac AS \"isGynac\", "
					+ "  test_mst.is_fnac AS \"isFnac\", "
					+ "  gen_mst.gender_id AS \"genderId\", "
					+ "  ord_mst.order_status_id AS \"orderStatusId\", "
					+ "  ord_mst.org_id AS \"orgId\", "
					+ "  ord_mst.org_unit_id AS \"orgUnitId\", "
					+ "  ord_mst.visit_type_id AS \"visitTypeId\", "
					+ "  ord_mst.ord_doctor_id AS \"doctorId\", "
					+ "  ord_mst.patient_id AS \"patientId\", "
					+ "  ord_mst.admission_id AS \"visitAdmId\", "
					+ "    ord_mst.encounter_id AS \"encounterId\", "
					+ "  pati_mst.uhid_number AS \"uhid\", "
					+ "  pre_mst.prefix_code || ' ' || pati_mst.first_name || ' ' || coalesce(pati_mst.middle_name) ||' '|| pati_mst.last_name || ' ' || "
					+ " calculate_age(pati_mst.dob)  || '/' || gen_mst.gender_code AS \"patientDetails\", "
					+ "  'Dr.' || doct_mst.first_name || ' ' || doct_mst.last_name AS \"doctorDetails\", "
					+ "  wardmast.ward_code AS \"wardCode\", "
					+ "  bedmst.bed_number AS \"bedNumber\", "
					+ "  pri_mst.priority_id AS \"priorityId\", "
					+ "  pri_mst.priority_name AS \"priorityName\", "
					+ "  pri_mst.color_code AS \"colorCode\", "
					+ "  NULL AS \"panelCode\", "
					/*+ " (case when ord_mst.encounter_id > 0 then "
					+ " (select mpem.payment_entitlement_desc from opd.t_patient_sponsor_details tpsd "
					+ " inner join public.m_payment_entitlement_master mpem on tpsd.payment_entitlement_id = mpem.payment_entitlement_id "
					+ " and tpsd.encounter_id = ord_mst.encounter_id where tpsd.priority_id = 1 limit 1 ) "
					+ " else (select mpem.payment_entitlement_desc from opd.t_patient_sponsor_details tpsd "
					+ " inner join public.m_payment_entitlement_master mpem on tpsd.payment_entitlement_id = mpem.payment_entitlement_id "
					+ " and tpsd.admission_id = ord_mst.admission_id where tpsd.priority_id = 1 limit 1 ) end ) as \"entitlementDesc\" "*/
					+" (case when ord_mst.encounter_id > 0 then " +
					" ( tpsd.payment_entitlement_id ) " +
					" else ( tpsd2.payment_entitlement_id ) end ) as \"paymentEntitlementId\" "
					+ " FROM "
					+ "  PUBLIC.t_order_master ord_mst "
					+ " INNER JOIN PUBLIC.t_order_details ord_details ON "
					+ "  ord_details.order_id = ord_mst.order_id "
					+ " INNER JOIN lab.m_test_master test_mst ON "
					+ "  test_mst.service_id = ord_details.service_id "
					+ " INNER JOIN patient.t_patient_registration pati_mst ON "
					+ "  ord_mst.patient_id = pati_mst.patient_id "
					+ " INNER JOIN m_gender_master gen_mst ON "
					+ "  gen_mst.gender_id = pati_mst.gender_id "
					+ " INNER JOIN m_prefix_master pre_mst ON "
					+ "  pre_mst.prefix_id = pati_mst.prefix_id "
					+ " INNER JOIN m_visit_type_master vistmst ON "
					+ "  vistmst.visit_type_id = ord_mst.visit_type_id "
					+ " INNER JOIN lab.m_speciman_master speciman_mst ON "
					+ "  speciman_mst.speciman_id = test_mst.speciman_id "
					+ " LEFT join billing.t_billing_details bill_details ON "
					+ "	ord_details.order_details_id = bill_details.order_details_id "
					+ " LEFT join billing.m_billing_master bill ON "
					+ "	bill_details.billing_master_id = bill.billing_master_id and bill.bill_cancelled = 'N' "
					+ " LEFT JOIN doctor.m_doctor_master doct_mst ON "
					+ "  ord_mst.ord_doctor_id = doct_mst.doctor_id "
					+ " LEFT JOIN doctor.m_sub_speciality_master subspe_mst ON "
					+ "  subspe_mst.sub_speciality_id = test_mst.dept_id "
					+ " LEFT JOIN lab.m_priority_master pri_mst ON "
					+ "  ord_mst.priority_id = pri_mst.priority_id "
					+ " left join adt.t_admission ta on ta.admission_id = ord_mst.admission_id and ta.status='A' "
					+ " left join opd.t_patient_sponsor_details tpsd "
					+ " on ord_mst.encounter_id = tpsd.encounter_id and tpsd.priority_id = 1 and tpsd.status='A' "
					+ " left join opd.t_patient_sponsor_details tpsd2 "
					+ " on tpsd2.admission_id = ta.admission_id and tpsd2.priority_id = 1 and tpsd2.status='A' "
					+ " LEFT JOIN adt.t_admission_details admision_dtls ON admision_dtls.admission_id = ord_mst.admission_id AND admision_dtls.status = 'A'  AND admision_dtls.nursing_acceptance = 'Y' "
					+ " LEFT JOIN adt.m_ward_master wardmast ON admision_dtls.ward_id = wardmast.ward_id "
					+ " LEFT JOIN adt.m_bed_master bedmst ON admision_dtls.bed_id = bedmst.bed_id "
					+ " INNER JOIN PUBLIC.t_lab_configuration config ON ord_mst.org_unit_id = config.unit_id "
					+ " WHERE "
					+ "  ord_mst.org_id =:orgId "
					+ "  AND ord_mst.org_unit_id =:orgUnitId "
					+ "  AND ord_mst.order_status_id NOT IN(:orderStatusId) "
					+ "  AND ord_details.service_rendered = 0 "
					+ "  AND ord_details.service_is_billed IN (0,1) "
					+ "  AND ord_details.service_rendering_deptid IN ( COALESCE(config.histopathology_id,0),COALESCE(config.cystopathology_id,0) ) "
					+ "  AND ord_details.ord_cancelled ='N' "
					+ "  AND ord_details.is_outsourced ='N' "
					+ "   AND ord_details.status ='A' "
					+ "  AND test_mst.is_histo_cyto IN ('C','H') "
					+ "  AND test_mst.test_status = 'A' "
					+ "  AND ord_mst.visit_type_id IN (1,2,3,4) "
					+ "	AND test_mst.org_unit_id =:orgUnitId ");
			
					  if (searchDto.getFromDt() != null ) 
						  query.append(" AND to_char(ord_mst.order_date,'YYYY-MM-DD') >=:fromDt ");
					  if (searchDto.getToDt() != null ) 
				    	  query.append(" AND to_char(ord_mst.order_date,'YYYY-MM-DD') <=:toDt ");
				      
					  if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0)
				    	  query.append(" AND pati_mst.patient_id =:patientId ");

				      if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size()>0)
				    	  query.append(" AND vistmst.visit_type_id IN (:visitTypes)");

				      if (searchDto.getSubDepts() != null && searchDto.getSubDepts().size()>0)
				    	  query.append(" AND test_mst.dept_id IN (:subDepts)");
				      
				      if (searchDto.getTestTypes() != null && searchDto.getTestTypes().size()>0)
				    	  query.append(" AND test_mst.test_id in (:testTypes)");
				      
				      if (searchDto.getLabSampleId() != null && searchDto.getLabSampleId() > 0)
				    	  query.append(" AND lbsampledtls.lab_sample_id =:labSampleId ");
				      
				      if (searchDto.getSpecimanTypes() != null && searchDto.getSpecimanTypes().size()>0)
				    	  query.append(" AND test_mst.speciman_id in (:specimanTypes)");
					
				      query.append(" UNION "
					+ " SELECT "
					+ "  to_char(ord_mst.order_date,'DD-MM-YYYY') AS \"orderDate\", "
					+ "  subspe_mst.sub_speciality_name AS \"deptName\", "
	                 + "    bill.bill_number as \"billNumber\", " 

					+ "  speciman_mst.speciman_name AS \"specimanType\", "
					+ "  speciman_mst.speciman_id AS \"specimanTypeId\", "
					+ "  vistmst.visit_type_code AS \"visitType\", "
					+ "  ord_mst.order_id AS \"orderId\", "
					+ "  ord_details.order_details_id AS \"orderDetailsId\", "
					+ "  ord_details.package_id AS \"packageId\", "
					+ "  test_mst.dept_id AS \"subDeptId\", "
					+ "  test_mst.test_id AS \"testId\", "
					+ "  test_mst.test_desc AS \"testDesc\", "
					+ "  test_mst.sample_id AS \"sampleId\", "
					+ "  test_mst.container_id AS \"containerId\", "
					+ "  test_mst.is_outsourced AS \"isOutsourced\", "
					+ "  test_mst.is_histo_cyto AS \"isHistoCyto\", "
					+ "  test_mst.is_gynac AS \"isGynac\", "
					+ "  test_mst.is_fnac AS \"isFnac\", "
					+ "  gen_mst.gender_id AS \"genderId\", "
					+ "  ord_mst.order_status_id AS \"orderStatusId\", "
					+ "  ord_mst.org_id AS \"orgId\", "
					+ "  ord_mst.org_unit_id AS \"orgUnitId\", "
					+ "  ord_mst.visit_type_id AS \"visitTypeId\", "
					+ "  ord_mst.ord_doctor_id AS \"doctorId\", "
					+ "  ord_mst.patient_id AS \"patientId\", "
					+ "  ord_mst.admission_id AS \"visitAdmId\", "
					+ " ord_mst.encounter_id AS \"encounterId\", "
					+ "  pati_mst.uhid_number AS \"uhid\", "
					+ "  pre_mst.prefix_code || ' ' || pati_mst.first_name || ' ' || coalesce(pati_mst.middle_name) || ' '|| pati_mst.last_name || ' ' || calculate_age(pati_mst.dob)  "
					+ " || '/' || gen_mst.gender_code AS \"patientDetails\", "
					+ "  'Dr.' || doct_mst.first_name || ' ' || doct_mst.last_name AS \"doctorDetails\", "
					+ "  wardmast.ward_code AS \"wardCode\", "
					+ "  bedmst.bed_number AS \"bedNumber\", "
					+ "  pri_mst.priority_id AS \"priorityId\", "
					+ "  pri_mst.priority_name AS \"priorityName\", "
					+ "  pri_mst.color_code AS \"colorCode\", "
					+ "  panel_mst.panel_code AS \"panelCode\", "
					/*+ " (case when ord_mst.encounter_id > 0 then "
					+ " (select mpem.payment_entitlement_desc from opd.t_patient_sponsor_details tpsd "
					+ " inner join public.m_payment_entitlement_master mpem on tpsd.payment_entitlement_id = mpem.payment_entitlement_id "
					+ " and tpsd.encounter_id = ord_mst.encounter_id where tpsd.priority_id = 1 limit 1 ) "
					+ " else (select mpem.payment_entitlement_desc from opd.t_patient_sponsor_details tpsd "
					+ " inner join public.m_payment_entitlement_master mpem on tpsd.payment_entitlement_id = mpem.payment_entitlement_id "
					+ " and tpsd.admission_id = ord_mst.admission_id where tpsd.priority_id = 1 limit 1 ) end ) as \"entitlementDesc\" "*/
					+" (case when ord_mst.encounter_id > 0 then " +
					" ( tpsd.payment_entitlement_id ) " +
					" else ( tpsd2.payment_entitlement_id ) end ) as \"paymentEntitlementId\" "
					+ " FROM "
					+ "  PUBLIC.t_order_master ord_mst "
					+ " INNER JOIN PUBLIC.t_order_details ord_details ON "
					+ "  ord_details.order_id = ord_mst.order_id "
					+ " INNER JOIN service.t_unit_service_master_details ser_dtls ON "
					+ "  ord_details.service_id = ser_dtls.service_master_id "
					+ " INNER JOIN patient.t_patient_registration pati_mst ON "
					+ "  ord_mst.patient_id = pati_mst.patient_id "
					+ " INNER JOIN m_gender_master gen_mst ON "
					+ "  gen_mst.gender_id = pati_mst.gender_id "
					+ " INNER JOIN m_prefix_master pre_mst ON "
					+ "  pre_mst.prefix_id = pati_mst.prefix_id "
					+ " INNER JOIN m_visit_type_master vistmst ON "
					+ "  vistmst.visit_type_id = ord_mst.visit_type_id "
					+ " INNER JOIN lab.t_panel_master panel_mst ON "
					+ "  panel_mst.service_id = ord_details.service_id "
					+ " INNER JOIN lab.t_panel_details panel_dtls ON "
					+ "  panel_mst.panel_id = panel_dtls.panel_id "
					+ "INNER JOIN lab.m_test_master test_mst ON "
					+ "  test_mst.test_id = panel_dtls.test_id "
					+ " INNER JOIN lab.m_speciman_master speciman_mst ON "
					+ "  speciman_mst.speciman_id = test_mst.speciman_id "
					+ " LEFT join billing.t_billing_details bill_details ON "
					+ "	ord_details.order_details_id = bill_details.order_details_id "
					+ " LEFT join billing.m_billing_master bill ON "
					+ "	bill_details.billing_master_id = bill.billing_master_id and bill.bill_cancelled = 'N' "
					+ "LEFT JOIN doctor.m_doctor_master doct_mst ON "
					+ "  ord_mst.ord_doctor_id = doct_mst.doctor_id "
					+ " LEFT JOIN doctor.m_sub_speciality_master subspe_mst ON "
					+ "  subspe_mst.sub_speciality_id = test_mst.dept_id "
					+ " LEFT JOIN lab.m_priority_master pri_mst ON "
					+ "  ord_mst.priority_id = pri_mst.priority_id "
					+ " left join adt.t_admission ta on ta.admission_id = ord_mst.admission_id and ta.status='A' "
					+ " left join opd.t_patient_sponsor_details tpsd "
					+ " on ord_mst.encounter_id = tpsd.encounter_id and tpsd.priority_id = 1 and tpsd.status='A' "
					+ " left join opd.t_patient_sponsor_details tpsd2 "
					+ " on tpsd2.admission_id = ta.admission_id and tpsd2.priority_id = 1 and tpsd2.status='A' "
					+ " LEFT JOIN adt.t_admission_details admision_dtls ON admision_dtls.admission_id = ord_mst.admission_id AND admision_dtls.status = 'A'  AND admision_dtls.nursing_acceptance = 'Y' "
					+ " LEFT JOIN adt.m_ward_master wardmast ON admision_dtls.ward_id = wardmast.ward_id "
					+ " LEFT JOIN adt.m_bed_master bedmst ON admision_dtls.bed_id = bedmst.bed_id "
					+ " INNER JOIN PUBLIC.t_lab_configuration config ON ord_mst.org_unit_id = config.unit_id "
					+ " WHERE "
					+ "  ord_mst.org_id =:orgId "
					+ "  AND ord_mst.org_unit_id =:orgUnitId "
					+ "  AND ord_mst.order_status_id NOT IN(:orderStatusId) "
					+ "  AND ord_details.service_rendered = 0 "
					+ "  AND ord_details.service_is_billed IN (0,1) "
					+ "  AND test_mst.dept_id IN ( COALESCE(config.histopathology_id,0),COALESCE(config.cystopathology_id,0) ) "
					+ "  AND ord_details.ord_cancelled ='N' "
					+ "  AND ord_details.is_outsourced ='N' "
					+ "   AND ord_details.status ='A' "
					+ "  AND test_mst.is_histo_cyto IN ('C','H') "
					+ "    AND test_mst.test_status = 'A' "
					+ "    AND panel_mst.status  ='A' "
					+ "    AND panel_dtls.is_deleted = 'N' "
					+ "    AND ser_dtls.is_panel = 'Y' "
					+ "    AND ser_dtls.unit_id = ord_details.org_unit_id "
					+ "    AND ord_mst.visit_type_id IN (1,2,3,4) "
					+ "	AND test_mst.org_unit_id =:orgUnitId ");
				     
				      if (searchDto.getFromDt() != null ) 
						  query.append(" AND to_char(ord_mst.order_date,'YYYY-MM-DD') >=:fromDt ");
					  if (searchDto.getToDt() != null ) 
				    	  query.append(" AND to_char(ord_mst.order_date,'YYYY-MM-DD') <=:toDt ");
				      
					  if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0)
				    	  query.append(" AND pati_mst.patient_id =:patientId  ");

				      if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size()>0)
				    	  query.append(" AND vistmst.visit_type_id IN (:visitTypes)");

				      if (searchDto.getSubDepts() != null && searchDto.getSubDepts().size()>0)
				    	  query.append(" AND test_mst.dept_id IN (:subDepts)");
				      
				      if (searchDto.getTestTypes() != null && searchDto.getTestTypes().size()>0)
				    	  query.append(" AND test_mst.test_id in (:testTypes)");
				      
				      if (searchDto.getLabSampleId() != null && searchDto.getLabSampleId() > 0)
				    	  query.append(" AND lbsampledtls.lab_sample_id =:labSampleId ");
				      
				      if (searchDto.getSpecimanTypes() != null && searchDto.getSpecimanTypes().size()>0)
				    	  query.append(" AND test_mst.speciman_id in (:specimanTypes)");
				      
		     }
		return query.toString();
		
	}

	
	public BigInteger getSpecimanReciptQueryCount(SearchDto searchDto,Character isPreOrPostBilling)
	{
		StringBuffer query = new StringBuffer();
		BigInteger count = BigInteger.ZERO;
		List<Integer>  subDeptIds = new ArrayList<Integer>();
		List<Integer>  testTypes = new ArrayList<Integer>();
		List<Integer>  visitTypes = new ArrayList<Integer>();
		List<Integer>  specimanTypes = new ArrayList<Integer>();

		if(isPreOrPostBilling=='Y')
		{
			
			//count for op
			StringBuffer sb  = new  StringBuffer();
			sb.append(" SELECT count(*) "
					+ " FROM "
					+ "	public.t_order_master ord_mst "
					+ " INNER JOIN public.t_order_details ord_details ON "
					+ "	ord_details.order_id = ord_mst.order_id "
					+ " INNER JOIN patient.t_patient_registration pati_mst ON "
					+ "	ord_mst.patient_id = pati_mst.patient_id "
					+ " INNER JOIN m_gender_master gen_mst ON "
					+ "	gen_mst.gender_id = pati_mst.gender_id "
					+ " INNER JOIN m_prefix_master pre_mst ON "
					+ "	pre_mst.prefix_id = pati_mst.prefix_id "
					+ "INNER JOIN m_visit_type_master vistmst ON "
					+ "	vistmst.visit_type_id = ord_mst.visit_type_id "
					+ " INNER JOIN lab.m_test_master test_mst ON "
					+ "	test_mst.service_id = ord_details.service_id "
					+ " INNER JOIN lab.m_speciman_master speciman_mst ON "
					+ "	speciman_mst.speciman_id = test_mst.speciman_id "
					+ " INNER JOIN public.t_lab_configuration config on ord_mst.org_unit_id = config.unit_id "
					+ " INNER join billing.t_billing_details bill_details ON  "
					+ "	ord_details.order_details_id = bill_details.order_details_id 	" 
					+ " INNER join billing.m_billing_master bill ON  "
					+ "	bill_details.billing_master_id = bill.billing_master_id and bill.bill_cancelled = 'N' "
					+ " WHERE "
					+ "	ord_mst.org_id =:orgId "
					+ "	AND ord_mst.org_unit_id =:orgUnitId "
					+ "	AND ord_mst.order_status_id NOT IN(:orderStatusId) "
					+ "	AND ord_details.service_rendered =0 "
					+ " AND ord_details.status ='A'  "
					+ "	AND ord_details.service_is_billed IN (1) "
					+ "	AND ord_details.service_rendering_deptid in ( coalesce(config.histopathology_id,0),coalesce(config.cystopathology_id,0) ) "
					+ "	AND ord_details.ord_cancelled ='N' "
					+ "	AND ord_details.is_outsourced ='N' "
					+ "	AND test_mst.is_histo_cyto IN ('H','C') "
					+ " AND test_mst.test_status = 'A' "
					+ " AND ord_mst.visit_type_id in (1)"
					+ " AND bill.bill_status_id = 3 "
					+ " AND test_mst.org_unit_id =:orgUnitId  " );
			
			  if (searchDto.getFromDt() != null ) 
				  sb.append(" AND to_char(ord_mst.order_date,'YYYY-MM-DD') >=:fromDt ");
			  if (searchDto.getToDt() != null ) 
				  sb.append(" AND to_char(ord_mst.order_date,'YYYY-MM-DD') <=:toDt ");
		      
			  if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0)
				  sb.append(" AND pati_mst.patient_id =:patientId  ");

		      if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size()>0) {
				  visitTypes = searchDto.getVisitTypes().stream()
						  .map(obj -> obj.getId())
						  .collect(Collectors.toList());

				  sb.append(" AND vistmst.visit_type_id IN (:visitTypes)");
			  }

		      if (searchDto.getSubDepts() != null && searchDto.getSubDepts().size()>0) {
				  subDeptIds = searchDto.getSubDepts().stream()
						  .map(obj -> obj.getId())
						  .collect(Collectors.toList());
				  sb.append(" AND test_mst.dept_id IN (:subDepts)");
			  }
		      
		      if (searchDto.getTestTypes() != null && searchDto.getTestTypes().size()>0) {
				  testTypes = searchDto.getTestTypes().stream()
						  .map(obj -> obj.getId())
						  .collect(Collectors.toList());
				  sb.append(" AND test_mst.test_id in (:testTypes)");
			  }
		      
		      if (searchDto.getSpecimanTypes() != null && searchDto.getSpecimanTypes().size()>0) {
				  specimanTypes = searchDto.getSpecimanTypes().stream()
						  .map(obj -> obj.getId())
						  .collect(Collectors.toList());
				  sb.append(" AND test_mst.speciman_id in (:specimanTypes)");
			  }
		      
		     Query sql = sessionFactory.getCurrentSession()
						.createSQLQuery(sb.toString())
						.setInteger("orgId", searchDto.getOrgId())
						.setInteger("orgUnitId", searchDto.getOrgUnitId())
						.setParameterList("orderStatusId",  CANCEL_ORDER);
			if (searchDto.getFromDt() != null ){
				sql.setParameter("fromDt", searchDto.getFromDt());
			}
			if (searchDto.getToDt() != null ){
				sql.setParameter("toDt", searchDto.getToDt());
			}
			if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0){
				sql.setParameter("patientId", searchDto.getPatientId());
			}
			if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size()>0){
				sql.setParameterList("visitTypes", visitTypes);
			}
			if (searchDto.getSubDepts() != null && searchDto.getSubDepts().size()>0){
				sql.setParameterList("subDepts", subDeptIds);
			}
			if (searchDto.getTestTypes() != null && searchDto.getTestTypes().size()>0){
				sql.setParameterList("testTypes", testTypes);
			}
			if (searchDto.getSpecimanTypes() != null && searchDto.getSpecimanTypes().size()>0){
				sql.setParameterList("specimanTypes", specimanTypes);
			}
			BigInteger histoCytoCountPreOp = (BigInteger) sql.uniqueResult();
		      
		      StringBuffer sb1  = new  StringBuffer();
		      sb1.append(
		    		  "SELECT count(*) "
		    				  + " FROM "
		    				  + "	public.t_order_master ord_mst "
		    				  + " INNER JOIN public.t_order_details ord_details ON "
		    				  + "	ord_details.order_id = ord_mst.order_id "
		    				  + " INNER JOIN service.t_unit_service_master_details ser_dtls ON "
		    				  + "	ord_details.service_id = ser_dtls.service_master_id "
		    				  + " INNER JOIN patient.t_patient_registration pati_mst ON "
		    				  + "	ord_mst.patient_id = pati_mst.patient_id "
		    				  + " INNER JOIN m_gender_master gen_mst ON "
		    				  + "	gen_mst.gender_id = pati_mst.gender_id "
		    				  + " INNER JOIN m_prefix_master pre_mst ON "
		    				  + "	pre_mst.prefix_id = pati_mst.prefix_id "
		    				  + " INNER JOIN m_visit_type_master vistmst ON "
		    				  + "	vistmst.visit_type_id = ord_mst.visit_type_id "
		    				  + " INNER JOIN lab.t_panel_master panel_mst ON "
		    				  + "	panel_mst.service_id = ord_details.service_id "
		    				  + " INNER JOIN lab.t_panel_details panel_dtls ON "
		    				  + "	panel_mst.panel_id = panel_dtls.panel_id "
		    				  + " INNER JOIN lab.m_test_master test_mst ON "
		    				  + "	test_mst.test_id = panel_dtls.test_id "
		    				  + " INNER JOIN lab.m_speciman_master speciman_mst ON "
		    				  + "	speciman_mst.speciman_id = test_mst.speciman_id "
		    				  + " INNER JOIN public.t_lab_configuration config on ord_mst.org_unit_id = config.unit_id "
		    				  + " INNER join billing.t_billing_details bill_details ON  "
		  					  + "	ord_details.order_details_id = bill_details.order_details_id 	" 
		  					  + " INNER join billing.m_billing_master bill ON  "
		  					  + "	bill_details.billing_master_id = bill.billing_master_id and bill.bill_cancelled = 'N' "
		    				  + " WHERE "
		    				  + "	ord_mst.org_id =:orgId "
		    				  + "	AND ord_mst.org_unit_id =:orgUnitId "
		    				  + "	AND ord_mst.order_status_id NOT IN(:orderStatusId) "
		    				  + "	AND ord_details.service_rendered = 0 "
		    				  + "	AND ord_details.service_is_billed IN (1) "
		    				  + "	AND ord_details.service_rendering_deptid in ( coalesce(config.histopathology_id,0),coalesce(config.cystopathology_id,0) ) "
		    				  + "	AND ord_details.ord_cancelled ='N' "
		    				  + "	AND ord_details.is_outsourced ='N' "
		    				  + "   AND ord_details.status ='A' "
		    				  + "	AND test_mst.is_histo_cyto IN ('H','C') "
		    				  + "	AND test_mst.test_status = 'A' "
		    				  + "	AND test_mst.test_status = 'A' "
		    				  + "	AND panel_mst.status  ='A' "
		    				  + "	AND panel_dtls.is_deleted = 'N' "
		    				  + "	AND ser_dtls.is_panel = 'Y' "
		    				  + "	AND ser_dtls.unit_id = ord_details.org_unit_id "
		    				  + "	AND ord_mst.visit_type_id in (1)"
		    				  + "   AND bill.bill_status_id = 3 " 
		    				  + "   AND test_mst.org_unit_id =:orgUnitId  ");
			
		          if (searchDto.getFromDt() != null ) 
					sb1.append(" AND to_char(ord_mst.order_date,'YYYY-MM-DD') >=:fromDt ");
				  if (searchDto.getToDt() != null ) 
					  sb1.append(" AND to_char(ord_mst.order_date,'YYYY-MM-DD') <=:toDt ");
			      
				  if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0)
					  sb1.append(" AND pati_mst.patient_id =:patientId ");

			      if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size()>0) {
					  visitTypes = searchDto.getVisitTypes().stream()
							  .map(obj -> obj.getId())
							  .collect(Collectors.toList());

					  sb1.append(" AND vistmst.visit_type_id IN (:visitTypes)");
				  }

			      if (searchDto.getSubDepts() != null && searchDto.getSubDepts().size()>0) {
					  subDeptIds = searchDto.getSubDepts().stream()
							  .map(obj -> obj.getId())
							  .collect(Collectors.toList());

					  sb1.append(" AND test_mst.dept_id IN (:subDepts)");
				  }
			      
			      if (searchDto.getTestTypes() != null && searchDto.getTestTypes().size()>0) {
					  testTypes = searchDto.getTestTypes().stream()
							  .map(obj -> obj.getId())
							  .collect(Collectors.toList());

					  sb1.append(" AND test_mst.test_id in (:testTypes)");
				  }
			      
			      if (searchDto.getSpecimanTypes() != null && searchDto.getSpecimanTypes().size()>0) {
					  specimanTypes = searchDto.getSpecimanTypes().stream()
							  .map(obj -> obj.getId())
							  .collect(Collectors.toList());

					  sb1.append(" AND test_mst.speciman_id in (:specimanTypes)");
				  }
		      
			Query sql1 = sessionFactory.getCurrentSession()
					.createSQLQuery(sb1.toString())
					.setInteger("orgId", searchDto.getOrgId())
					.setInteger("orgUnitId", searchDto.getOrgUnitId())
					.setParameterList("orderStatusId",  CANCEL_ORDER);
			if (searchDto.getFromDt() != null ){
				sql1.setParameter("fromDt", searchDto.getFromDt());
			}
			if (searchDto.getToDt() != null ){
				sql1.setParameter("toDt", searchDto.getToDt());
			}
			if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0){
				sql1.setParameter("patientId", searchDto.getPatientId());
			}
			if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size()>0){
				sql1.setParameterList("visitTypes", visitTypes);
			}
			if (searchDto.getSubDepts() != null && searchDto.getSubDepts().size()>0){
				sql1.setParameterList("subDepts", subDeptIds);
			}
			if (searchDto.getTestTypes() != null && searchDto.getTestTypes().size()>0){
				sql1.setParameterList("testTypes", testTypes);
			}
			if (searchDto.getSpecimanTypes() != null && searchDto.getSpecimanTypes().size()>0){
				sql1.setParameterList("specimanTypes", specimanTypes);
			}
			BigInteger histoCytoCountPanelPreOp =(BigInteger) sql1.uniqueResult();
			
			
			//count for op
			StringBuffer sb2  = new  StringBuffer();
			sb2.append(" SELECT count(*) "
					+ " FROM "
					+ "	public.t_order_master ord_mst "
					+ " INNER JOIN public.t_order_details ord_details ON "
					+ "	ord_details.order_id = ord_mst.order_id "
					+ " INNER JOIN patient.t_patient_registration pati_mst ON "
					+ "	ord_mst.patient_id = pati_mst.patient_id "
					+ " INNER JOIN m_gender_master gen_mst ON "
					+ "	gen_mst.gender_id = pati_mst.gender_id "
					+ " INNER JOIN m_prefix_master pre_mst ON "
					+ "	pre_mst.prefix_id = pati_mst.prefix_id "
					+ "INNER JOIN m_visit_type_master vistmst ON "
					+ "	vistmst.visit_type_id = ord_mst.visit_type_id "
					+ " INNER JOIN lab.m_test_master test_mst ON "
					+ "	test_mst.service_id = ord_details.service_id "
					+ " INNER JOIN lab.m_speciman_master speciman_mst ON "
					+ "	speciman_mst.speciman_id = test_mst.speciman_id "
					+ " INNER JOIN public.t_lab_configuration config on ord_mst.org_unit_id = config.unit_id "
					+ " WHERE "
					+ "	ord_mst.org_id =:orgId "
					+ "	AND ord_mst.org_unit_id =:orgUnitId "
					+ "	AND ord_mst.order_status_id NOT IN(:orderStatusId) "
					+ "	AND ord_details.service_rendered =0 "
					+ " AND ord_details.status ='A'  "
					+ "	AND ord_details.service_is_billed IN (1,0) "
					+ "	AND ord_details.service_rendering_deptid in ( coalesce(config.histopathology_id,0),coalesce(config.cystopathology_id,0) ) "
					+ "	AND ord_details.ord_cancelled ='N' "
					+ "	AND ord_details.is_outsourced ='N' "
					+ "	AND test_mst.is_histo_cyto IN ('H','C') "
					+ " AND test_mst.test_status = 'A' "
					+ " AND ord_mst.visit_type_id in (2,3,4) "
					+ " AND test_mst.org_unit_id =:orgUnitId  ");
			
			  if (searchDto.getFromDt() != null ) 
				  sb2.append(" AND to_char(ord_mst.order_date,'YYYY-MM-DD') >=:fromDt ");
			  if (searchDto.getToDt() != null ) 
				  sb2.append(" AND to_char(ord_mst.order_date,'YYYY-MM-DD') <=:toDt ");
		      
			  if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0)
				  sb2.append(" AND pati_mst.patient_id =:patientId ");

		      if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size()>0) {
				  visitTypes = searchDto.getVisitTypes().stream()
						  .map(obj -> obj.getId())
						  .collect(Collectors.toList());

				  sb2.append(" AND vistmst.visit_type_id IN (:visitTypes)");
			  }

		      if (searchDto.getSubDepts() != null && searchDto.getSubDepts().size()>0) {
				  subDeptIds = searchDto.getSubDepts().stream()
						  .map(obj -> obj.getId())
						  .collect(Collectors.toList());

				  sb2.append(" AND test_mst.dept_id IN (:subDepts)");
			  }
		      
		      if (searchDto.getTestTypes() != null && searchDto.getTestTypes().size()>0) {
				  testTypes = searchDto.getTestTypes().stream()
						  .map(obj -> obj.getId())
						  .collect(Collectors.toList());
				  sb2.append(" AND test_mst.test_id in (:testTypes)");
			  }
		      
		      if (searchDto.getSpecimanTypes() != null && searchDto.getSpecimanTypes().size()>0) {
				  specimanTypes = searchDto.getSpecimanTypes().stream()
						  .map(obj -> obj.getId())
						  .collect(Collectors.toList());
				  sb2.append(" AND test_mst.speciman_id in (:specimanTypes)");
			  }
		      
		     Query hqlQuery= sessionFactory.getCurrentSession()
						.createSQLQuery(sb2.toString())
						.setInteger("orgId", searchDto.getOrgId())
						.setInteger("orgUnitId", searchDto.getOrgUnitId())
						.setParameterList("orderStatusId",  CANCEL_ORDER);
			if (searchDto.getFromDt() != null ){
				hqlQuery.setParameter("fromDt", searchDto.getFromDt());
			}
			if (searchDto.getToDt() != null ){
				hqlQuery.setParameter("toDt", searchDto.getToDt());
			}
			if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0){
				hqlQuery.setParameter("patientId", searchDto.getPatientId());
			}
			if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size()>0){
				hqlQuery.setParameterList("visitTypes", visitTypes);
			}
			if (searchDto.getSubDepts() != null && searchDto.getSubDepts().size()>0){
				hqlQuery.setParameterList("subDepts", subDeptIds);
			}
			if (searchDto.getTestTypes() != null && searchDto.getTestTypes().size()>0){
				hqlQuery.setParameterList("testTypes", testTypes);
			}
			if (searchDto.getSpecimanTypes() != null && searchDto.getSpecimanTypes().size()>0){
				hqlQuery.setParameterList("specimanTypes", specimanTypes);
			}
			BigInteger histoCytoCountPreOther =(BigInteger)	hqlQuery.uniqueResult();
		      
		      StringBuffer sb3  = new  StringBuffer();
		      sb3.append( "select "
		    		  + "	count(*) "
		    		  + "from "
		    		  + "	PUBLIC.t_order_master ord_mst "
		    		  + "inner join PUBLIC.t_order_details ord_details on "
		    		  + "	ord_details.order_id = ord_mst.order_id "
		    		  + "inner join service.t_unit_service_master_details ser_dtls on "
		    		  + "	ord_details.service_id = ser_dtls.service_master_id "
		    		  + "inner join patient.t_patient_registration pati_mst on "
		    		  + "	ord_mst.patient_id = pati_mst.patient_id "
		    		  + "inner join m_gender_master gen_mst on "
		    		  + "	gen_mst.gender_id = pati_mst.gender_id "
		    		  + "inner join m_prefix_master pre_mst on "
		    		  + "	pre_mst.prefix_id = pati_mst.prefix_id "
		    		  + "inner join m_visit_type_master vistmst on "
		    		  + "	vistmst.visit_type_id = ord_mst.visit_type_id "
		    		  + "inner join lab.t_panel_master panel_mst on "
		    		  + "	panel_mst.service_id = ord_details.service_id "
		    		  + "inner join lab.t_panel_details panel_dtls on "
		    		  + "	panel_mst.panel_id = panel_dtls.panel_id "
		    		  + "inner join lab.m_test_master test_mst on "
		    		  + "	test_mst.test_id = panel_dtls.test_id "
		    		  + "inner join lab.m_speciman_master speciman_mst on "
		    		  + "	speciman_mst.speciman_id = test_mst.speciman_id "
		    		  + "inner join PUBLIC.t_lab_configuration config on "
		    		  + "	ord_mst.org_unit_id = config.unit_id "
		    		  + "where "
		    		  + "	ord_mst.org_id =:orgId "
		    		  + "	and ord_mst.org_unit_id =:orgUnitId "
		    		  + "	and ord_mst.order_status_id not in (:orderStatusId) "
		    		  + "	and ord_details.service_rendered = 0 "
		    		  + "	and ord_details.service_is_billed in (0,1) "
		    		  + "	and test_mst.dept_id in ( coalesce(config.histopathology_id, 0),coalesce(config.cystopathology_id, 0) ) "
		    		  + "	and ord_details.ord_cancelled = 'N' "
		    		  + "	and ord_details.is_outsourced = 'N' "
		    		  + "	and ord_details.status = 'A' "
		    		  + "	and test_mst.is_histo_cyto in ('C','H') "
		    		  + "	and test_mst.test_status = 'A' "
		    		  + "	and panel_mst.status = 'A' "
		    		  + "	and panel_dtls.is_deleted = 'N' "
		    		  + "	and ser_dtls.is_panel = 'Y' "
		    		  + "	and ser_dtls.unit_id = ord_details.org_unit_id "
		    		  + "	and ord_mst.visit_type_id in (2,3,4) "
		    		  + "   and test_mst.org_unit_id =:orgUnitId  ");
				
		          if (searchDto.getFromDt() != null ) 
		        	  sb3.append(" AND to_char(ord_mst.order_date,'YYYY-MM-DD') >=:fromDt ");
				  if (searchDto.getToDt() != null ) 
					  sb3.append(" AND to_char(ord_mst.order_date,'YYYY-MM-DD') <=:toDt ");
			      
				  if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0)
					  sb3.append(" AND pati_mst.patient_id =:patientId ");

			      if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size()>0) {
					  visitTypes = searchDto.getVisitTypes().stream()
							  .map(obj -> obj.getId())
							  .collect(Collectors.toList());

					  sb3.append(" AND vistmst.visit_type_id IN (:visitTypes)");
				  }

			      if (searchDto.getSubDepts() != null && searchDto.getSubDepts().size()>0) {
					  subDeptIds = searchDto.getSubDepts().stream()
							  .map(obj -> obj.getId())
							  .collect(Collectors.toList());

					  sb3.append(" AND test_mst.dept_id IN (:subDepts)");
				  }
			      
			      if (searchDto.getTestTypes() != null && searchDto.getTestTypes().size()>0) {
					  testTypes = searchDto.getTestTypes().stream()
							  .map(obj -> obj.getId())
							  .collect(Collectors.toList());

					  sb3.append(" AND test_mst.test_id in (:testTypes)");
				  }
			      
			      if (searchDto.getSpecimanTypes() != null && searchDto.getSpecimanTypes().size()>0) {
					  specimanTypes = searchDto.getSpecimanTypes().stream()
							  .map(obj -> obj.getId())
							  .collect(Collectors.toList());

					  sb3.append(" AND test_mst.speciman_id in (:specimanTypes)");
				  }
		      
			Query sql3 =sessionFactory.getCurrentSession()
					.createSQLQuery(sb3.toString())
					.setInteger("orgId", searchDto.getOrgId())
					.setInteger("orgUnitId", searchDto.getOrgUnitId())
					.setParameterList("orderStatusId",  CANCEL_ORDER);
			if (searchDto.getFromDt() != null ){
				sql3.setParameter("fromDt", searchDto.getFromDt());
			}
			if (searchDto.getToDt() != null ){
				sql3.setParameter("toDt", searchDto.getToDt());
			}
			if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0){
				sql3.setParameter("patientId", searchDto.getPatientId());
			}
			if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size()>0){
				sql3.setParameterList("visitTypes", visitTypes);
			}
			if (searchDto.getSubDepts() != null && searchDto.getSubDepts().size()>0){
				sql3.setParameterList("subDepts", subDeptIds);
			}
			if (searchDto.getTestTypes() != null && searchDto.getTestTypes().size()>0){
				sql3.setParameterList("testTypes", testTypes);
			}
			if (searchDto.getSpecimanTypes() != null && searchDto.getSpecimanTypes().size()>0){
				sql3.setParameterList("specimanTypes", specimanTypes);
			}
			BigInteger histoCytoCountPanelPreOhter = (BigInteger) sql3.uniqueResult();
			
			

				count = histoCytoCountPreOp.add(histoCytoCountPanelPreOp).add(histoCytoCountPreOther).add(histoCytoCountPanelPreOhter);
				
				return  count;
		}
		else {
			
			StringBuffer sb  = new  StringBuffer();
			sb.append(" SELECT count(*) "
					+ " FROM "
					+ "	public.t_order_master ord_mst "
					+ " INNER JOIN public.t_order_details ord_details ON "
					+ "	ord_details.order_id = ord_mst.order_id "
					+ " INNER JOIN patient.t_patient_registration pati_mst ON "
					+ "	ord_mst.patient_id = pati_mst.patient_id "
					+ " INNER JOIN m_gender_master gen_mst ON "
					+ "	gen_mst.gender_id = pati_mst.gender_id "
					+ " INNER JOIN m_prefix_master pre_mst ON "
					+ "	pre_mst.prefix_id = pati_mst.prefix_id "
					+ "INNER JOIN m_visit_type_master vistmst ON "
					+ "	vistmst.visit_type_id = ord_mst.visit_type_id "
					+ " INNER JOIN lab.m_test_master test_mst ON "
					+ "	test_mst.service_id = ord_details.service_id "
					+ " INNER JOIN lab.m_speciman_master speciman_mst ON "
					+ "	speciman_mst.speciman_id = test_mst.speciman_id "
					+ " INNER JOIN public.t_lab_configuration config on ord_mst.org_unit_id = config.unit_id "
					+ " WHERE "
					+ "	ord_mst.org_id =:orgId "
					+ "	AND ord_mst.org_unit_id =:orgUnitId "
					+ "	AND ord_mst.order_status_id NOT IN(:orderStatusId) "
					+ "	AND ord_details.service_rendered =0 "
					+ " AND ord_details.status ='A'  "
					+ "	AND ord_details.service_is_billed IN (0,1) "
					+ "	AND ord_details.service_rendering_deptid in ( coalesce(config.histopathology_id,0),coalesce(config.cystopathology_id,0) ) "
					+ "	AND ord_details.ord_cancelled ='N' "
					+ "	AND ord_details.is_outsourced ='N' "
					+ "	AND test_mst.is_histo_cyto IN ('H','C') "
					+ " AND test_mst.test_status = 'A' "
					+ " AND ord_mst.visit_type_id in (1,2,3,4) "
					+ "	AND test_mst.org_unit_id =:orgUnitId ");
			
			  if (searchDto.getFromDt() != null ) 
				  sb.append(" AND to_char(ord_mst.order_date,'YYYY-MM-DD') >=:fromDt ");
			  if (searchDto.getToDt() != null ) 
				  sb.append(" AND to_char(ord_mst.order_date,'YYYY-MM-DD') <=:toDt ");
		      
			  if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0)
				  sb.append(" AND pati_mst.patient_id =:patientId ");

		      if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size()>0) {
				  visitTypes = searchDto.getVisitTypes().stream()
						  .map(obj -> obj.getId())
						  .collect(Collectors.toList());

				  sb.append(" AND vistmst.visit_type_id IN (:visitTypes)");
			  }

		      if (searchDto.getSubDepts() != null && searchDto.getSubDepts().size()>0) {
				  subDeptIds = searchDto.getSubDepts().stream()
						  .map(obj -> obj.getId())
						  .collect(Collectors.toList());

				  sb.append(" AND test_mst.dept_id IN (:subDepts)");
			  }
		      
		      if (searchDto.getTestTypes() != null && searchDto.getTestTypes().size()>0) {
				  testTypes = searchDto.getTestTypes().stream()
						  .map(obj -> obj.getId())
						  .collect(Collectors.toList());

				  sb.append(" AND test_mst.test_id in (:testTypes)");
			  }
		      
		      if (searchDto.getSpecimanTypes() != null && searchDto.getSpecimanTypes().size()>0) {
				  specimanTypes = searchDto.getSpecimanTypes().stream()
						  .map(obj -> obj.getId())
						  .collect(Collectors.toList());
				  query.append(" AND test_mst.speciman_id in (:specimanTypes)");
			  }
		      
		     Query sql = sessionFactory.getCurrentSession()
						.createSQLQuery(sb.toString())
						.setInteger("orgId", searchDto.getOrgId())
						.setInteger("orgUnitId", searchDto.getOrgUnitId())
						.setParameterList("orderStatusId",  CANCEL_ORDER);
			if (searchDto.getFromDt() != null ){
				sql.setParameter("fromDt", searchDto.getFromDt());
			}
			if (searchDto.getToDt() != null ){
				sql.setParameter("toDt", searchDto.getToDt());
			}
			if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0){
				sql.setParameter("patientId", searchDto.getPatientId());
			}
			if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size()>0){
				sql.setParameterList("visitTypes", visitTypes);
			}
			if (searchDto.getSubDepts() != null && searchDto.getSubDepts().size()>0){
				sql.setParameterList("subDepts", subDeptIds);
			}
			if (searchDto.getTestTypes() != null && searchDto.getTestTypes().size()>0){
				sql.setParameterList("testTypes", testTypes);
			}
			if (searchDto.getSpecimanTypes() != null && searchDto.getSpecimanTypes().size()>0){
				sql.setParameterList("specimanTypes", specimanTypes);
			}
			BigInteger histoCytoCountPost = (BigInteger) sql.uniqueResult();
		      
		      StringBuffer sb1  = new  StringBuffer();
		      sb1.append(
		    		  "SELECT count(*) "
		    				  + " FROM "
		    				  + "	public.t_order_master ord_mst "
		    				  + " INNER JOIN public.t_order_details ord_details ON "
		    				  + "	ord_details.order_id = ord_mst.order_id "
		    				  + " INNER JOIN service.t_unit_service_master_details ser_dtls ON "
		    				  + "	ord_details.service_id = ser_dtls.service_master_id "
		    				  + " INNER JOIN patient.t_patient_registration pati_mst ON "
		    				  + "	ord_mst.patient_id = pati_mst.patient_id "
		    				  + " INNER JOIN m_gender_master gen_mst ON "
		    				  + "	gen_mst.gender_id = pati_mst.gender_id "
		    				  + " INNER JOIN m_prefix_master pre_mst ON "
		    				  + "	pre_mst.prefix_id = pati_mst.prefix_id "
		    				  + " INNER JOIN m_visit_type_master vistmst ON "
		    				  + "	vistmst.visit_type_id = ord_mst.visit_type_id "
		    				  + " INNER JOIN lab.t_panel_master panel_mst ON "
		    				  + "	panel_mst.service_id = ord_details.service_id "
		    				  + " INNER JOIN lab.t_panel_details panel_dtls ON "
		    				  + "	panel_mst.panel_id = panel_dtls.panel_id "
		    				  + " INNER JOIN lab.m_test_master test_mst ON "
		    				  + "	test_mst.test_id = panel_dtls.test_id "
		    				  + " INNER JOIN lab.m_speciman_master speciman_mst ON "
		    				  + "	speciman_mst.speciman_id = test_mst.speciman_id "
		    				  + " INNER JOIN public.t_lab_configuration config on ord_mst.org_unit_id = config.unit_id "
		    				  + " WHERE "
		    				  + "	ord_mst.org_id =:orgId "
		    				  + "	AND ord_mst.org_unit_id =:orgUnitId "
		    				  + "	AND ord_mst.order_status_id NOT IN(:orderStatusId) "
		    				  + "	AND ord_details.service_rendered = 0 "
		    				  + "	AND ord_details.service_is_billed IN (0,1) "
		    				  + "	AND ord_details.service_rendering_deptid in ( coalesce(config.histopathology_id,0),coalesce(config.cystopathology_id,0) ) "
		    				  + "	AND ord_details.ord_cancelled ='N' "
		    				  + "	AND ord_details.is_outsourced ='N' "
		    				  + "   AND ord_details.status ='A' "
		    				  + "	AND test_mst.is_histo_cyto IN ('H','C') "
		    				  + "	AND test_mst.test_status = 'A' "
		    				  + "	AND test_mst.test_status = 'A' "
		    				  + "	AND panel_mst.status  ='A' "
		    				  + "	AND panel_dtls.is_deleted = 'N' "
		    				  + "	AND ser_dtls.is_panel = 'Y' "
		    				  + "	AND ser_dtls.unit_id = ord_details.org_unit_id "
		    				  + "	AND ord_mst.visit_type_id in (1,2,3,4) "
		    				  + "	AND test_mst.org_unit_id =:orgUnitId ");
			
		          if (searchDto.getFromDt() != null ) 
					sb1.append(" AND to_char(ord_mst.order_date,'YYYY-MM-DD') >=:fromDt ");
				  if (searchDto.getToDt() != null ) 
					  sb1.append(" AND to_char(ord_mst.order_date,'YYYY-MM-DD') <=:toDt ");
			      
				  if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0)
					  sb1.append(" AND pati_mst.patient_id =:patientId ");

			      if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size()>0) {
					  visitTypes = searchDto.getVisitTypes().stream()
							  .map(obj -> obj.getId())
							  .collect(Collectors.toList());

					  sb1.append(" AND vistmst.visit_type_id IN (:visitTypes)");
				  }

			      if (searchDto.getSubDepts() != null && searchDto.getSubDepts().size()>0) {
					  subDeptIds = searchDto.getSubDepts().stream()
							  .map(obj -> obj.getId())
							  .collect(Collectors.toList());

					  sb1.append(" AND test_mst.dept_id IN (:subDepts)");
				  }
			      if (searchDto.getTestTypes() != null && searchDto.getTestTypes().size()>0) {
					  testTypes = searchDto.getTestTypes().stream()
							  .map(obj -> obj.getId())
							  .collect(Collectors.toList());

					  sb1.append(" AND test_mst.test_id in (:testTypes)");
				  }
			      if (searchDto.getSpecimanTypes() != null && searchDto.getSpecimanTypes().size()>0) {
					  specimanTypes = searchDto.getSpecimanTypes().stream()
							  .map(obj -> obj.getId())
							  .collect(Collectors.toList());

					  query.append(" AND test_mst.speciman_id in (:specimanTypes)");
				  }
			       
		      
			Query sql1 = sessionFactory.getCurrentSession()
					.createSQLQuery(sb1.toString())
					.setInteger("orgId", searchDto.getOrgId())
					.setInteger("orgUnitId", searchDto.getOrgUnitId())
					.setParameterList("orderStatusId",  CANCEL_ORDER);
			if (searchDto.getFromDt() != null ){
				sql1.setParameter("fromDt", searchDto.getFromDt());
			}
			if (searchDto.getToDt() != null ){
				sql1.setParameter("toDt", searchDto.getToDt());
			}
			if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0){
				sql1.setParameter("patientId", searchDto.getPatientId());
			}
			if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size()>0){
				sql1.setParameterList("visitTypes", visitTypes);
			}
			if (searchDto.getSubDepts() != null && searchDto.getSubDepts().size()>0){
				sql1.setParameterList("subDepts", subDeptIds);
			}
			if (searchDto.getTestTypes() != null && searchDto.getTestTypes().size()>0){
				sql1.setParameterList("testTypes", testTypes);
			}
			if (searchDto.getSpecimanTypes() != null && searchDto.getSpecimanTypes().size()>0){
				sql1.setParameterList("specimanTypes", specimanTypes);
			}
			BigInteger histoCytoPanelCountPost = (BigInteger) sql1.uniqueResult();
			
			
			if (histoCytoCountPost.add(histoCytoPanelCountPost).compareTo(BigInteger.ZERO) == 1)
			{
				count = histoCytoCountPost;
			}
	
	  }
		return count;
	}
}
