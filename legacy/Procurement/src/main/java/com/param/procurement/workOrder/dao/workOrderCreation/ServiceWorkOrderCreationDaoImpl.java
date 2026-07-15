package com.param.procurement.workOrder.dao.workOrderCreation;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import in.param.exception.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import org.apache.log4j.Logger;

import com.param.global.common.ICommonConstants;
import com.param.global.common.IError;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.pharmacy.dto.PendingServicesForWorkOrderCreationDto;

@Repository
@SuppressWarnings({ "unchecked", "rawtypes" })
public class ServiceWorkOrderCreationDaoImpl implements IServiceWorkOrderCreationDao,ICommonConstants, IError
{

	final static Logger logger = Logger.getLogger(ServiceWorkOrderCreationDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public Response getPendingDataForWorkOrderCreation(PendingServicesForWorkOrderCreationDto requestDto) throws ApplicationException {
		try {
			String stringQuery=" select " 
					 + " 	concat((case when tpr.is_vip = 'Y' then coalesce(tpr.alise_name, tpr.first_name) else concat(coalesce(tpr.first_name) , ' ', coalesce(tpr.middle_name) , ' ', coalesce(tpr.last_name)) end ), concat(to_char(extract(year from age(current_timestamp, tpr.dob)), '999'), 'Y', '/', mgm.gender_code)) as \"patientDetails\", " 
					 + " 	tpr.uhid_number as \"uhidNumber\", "
					 + " 	tpr.image_path as \"imagePath\" , " 
					 + " 	mgm.gender_code as \"genderCode\", " 
					 + "	concat(mbm.bill_number, ' | ', to_char(mbm.bill_date_time, 'DD/MM/YYYY')) as \"billDetails\", " 
					 + " 	tem.encounter_number as \"visitDetails\", " 
					 + " 	mpem.payment_entitlement_desc as \"payementEntitlement\", " 
					 + " 	'OP' as \"visitType\", " 
					 + " 	coalesce(mbm.net_amount,0) as \"totalNetAmount\", " 
					 + " 	mbm.billing_master_id as \"billId\", " 
					 + " 	tem.encounter_id as \"encounterId\", " 
					 + " 	tpr.patient_id as \"patientId\", " 
					 + " 	0 as \"admissionId\", "
					 + "	to_char(mbm.bill_date_time,'YYYY-MM-DD') as \"billDate\" " 
					 + " from " 
					 + " 	billing.m_billing_master mbm " 
					 + " inner join opd.t_encounter_master tem on " 
					 + " 	mbm.encounter_id = tem.encounter_id " 
					 + " inner join patient.t_patient_registration tpr on " 
					 + " 	tpr.patient_id = mbm.patient_id " 
					 + " inner join m_gender_master mgm on " 
					 + " 	mgm.gender_id = tpr.gender_id " 
					 + " inner join billing.t_bill_payee_details tbpd on " 
					 + " 	tbpd.bill_id = mbm.billing_master_id " 
					 + " 	and tbpd.payee_id =:selfPayeeId " 
					 + " inner join billing.t_bill_payee_details tbpd1 on " 
					 + " 	tbpd1.bill_id = mbm.billing_master_id " 
					 + " 	and tbpd1.priority_id = 1 " 
					 + " inner join m_payment_entitlement_master mpem on " 
					 + " 	mpem.payment_entitlement_id = tbpd1.payment_entitlement_id " 
					 + " where " 
					 + " 	( " 
					 + " 	select " 
					 + " 		count(*) " 
					 + " 	from " 
					 + " 		billing.t_billing_details tbd "
					 + " 	inner join t_order_details tod on "
					 + "		tbd.order_details_id = tod.order_details_id " 
					 /*+ "	inner join t_order_details_package_mapper todpm on " 
					 + " 		todpm.order_details_id = tod.order_details_id "
					 + " 		and todpm.is_package_service = 'Y' "
					 + "	inner join service.m_package_master mpm on "
					 + " 		mpm.package_master_id = todpm.package_id " */
					 + " 	inner join service.t_unit_service_master_details tusmd on " 
					 + " 		tbd.service_id = tusmd.service_master_id " 
					 + " 		and tusmd.unit_id =:unitId " 
					 + " 	where " 
					 + " 		mbm.status = 'A' "
					 //+ "		and mpm.package_type_id = 1 " 
					 + " 		and tbd.billing_master_id = mbm.billing_master_id " 
					 + " 		and tusmd.is_outsource_job = 'Y')>0 " 
					 + " 	and tbpd.due_amount = 0 " 
					 + " 	and mbm.status = 'A' " 
					 + " 	and mbm.unit_id =:unitId "
					 + "	and (:patientId = 0 OR  mbm.patient_id =:patientId)"
					 + "	and (:billNumber = '' OR  mbm.bill_number =:billNumber) "
					 + "	and (:visitNo = '' OR  tem.encounter_number =:visitNo) "
					 + "	and (:visitType = 0 OR  mbm.visit_type_id =:visitType) "
					 + "	and (:paymentEntitlementId = 0 OR  tbpd1.payment_entitlement_id =:paymentEntitlementId) "
					 + " 	and to_char(mbm.bill_date_time, 'YYYY-MM-DD') between :fromDate and :toDate "
					 + " union " 
					 + " select " 
					 + " 	concat((case when tpr.is_vip = 'Y' then coalesce(tpr.alise_name, tpr.first_name) else concat(coalesce(tpr.first_name) , ' ', coalesce(tpr.middle_name) , ' ', coalesce(tpr.last_name)) end ), concat(to_char(extract(year from age(current_timestamp, tpr.dob)), '999'), 'Y', '/', mgm.gender_code)) as \"patientDetails\", " 
					 + " 	tpr.uhid_number as \"uhidNumber\", "
					 + " 	tpr.image_path as \"imagePath\" , " 
					 + " 	mgm.gender_code as \"genderCode\", " 
					 + " 	(case " 
					 + " 		when mbm.billing_master_id is not null then concat(coalesce(mbm.bill_number), ' | ', to_char(coalesce(mbm.bill_date_time), 'DD/MM/YYYY')) " 
					 + " 	else '-' end) as \"billDetails\", " 
					 + " 	ta.admission_number as \"visitDetails\", " 
					 + " 	mpem.payment_entitlement_desc as \"payementEntitlement\", " 
					 + " 	mvtm.visit_type_code as \"visitType\", " 
					 + " 	coalesce(mbm.net_amount,0) as \"totalNetAmount\", " 
					 + " 	mbm.billing_master_id as \"billId\", " 
					 + " 	0 as \"encounterId\", " 
					 + " 	tpr.patient_id as \"patientId\", " 
					 + " 	ta.admission_id as \"admissionId\", "
					 + "	to_char((case when mbm.bill_date_time is not null then mbm.bill_date_time else ta.created_date end), 'YYYY-MM-DD') as \"billDate\" "
					 + " from " 
					 + " 	adt.t_admission ta " 
					 + " inner join patient.t_patient_registration tpr on " 
					 + " 	tpr.patient_id = ta.patient_id " 
					 + " inner join m_gender_master mgm on " 
					 + " 	mgm.gender_id = tpr.gender_id " 
					 + " inner join opd.t_patient_sponsor_details tpsd on " 
					 + " 	tpsd.admission_id = ta.admission_id " 
					 + " 	and tpsd.priority_id = 1 " 
					 + " 	and tpsd.status = 'A' " 
					 + " inner join m_payment_entitlement_master mpem on " 
					 + " 	mpem.payment_entitlement_id = tpsd.payment_entitlement_id " 
					 + " inner join m_visit_type_master mvtm on " 
					 + " 	mvtm.visit_type_id = ta.visit_type_id " 
					 + " left join billing.m_billing_master mbm on " 
					 + " 	ta.admission_id = mbm.admission_id " 
					 + " 	and mbm.status = 'A' " 
					 + " where " 
					 + " 	ta.admission_id in ( "
					 + " 	select  "
					 + " 		distinct tom.admission_id  "
					 + " 	from " 
					 + " 		t_order_details tod " 
					 /*+ " 	inner join t_order_details_package_mapper todpm on " 
					 + " 		todpm.order_details_id = tod.order_details_id "
					 + " 		and todpm.is_package_service = 'Y' "
					 + "	inner join service.m_package_master mpm on "
					 + " 		mpm.package_master_id = todpm.package_id "*/
					 + " 	inner join service.t_unit_service_master_details tusmd on " 
					 + " 		tod.service_id = tusmd.service_master_id " 
					 + " 		and tusmd.unit_id =:unitId " 
					 + " 	inner join t_order_master tom on " 
					 + " 		tom.order_id = tod.order_id " 
					 + " 	where " 
					 + " 		tusmd.status = 'A' "
					 //+ "		and mpm.package_type_id = 1 " 
					 + " 		and to_char(tod.order_date, 'YYYY-MM-DD') between :fromDate and :toDate " 
					 + " 		and tusmd.is_outsource_job = 'Y') " 
					 + " 	and ta.unit_id =:unitId "
					 + "	and (:patientId = 0 OR  ta.patient_id =:patientId)"
					 + "	and (:billNumber = '' OR  mbm.bill_number =:billNumber) "
					 + "	and (:visitNo = '' OR  ta.admission_number =:visitNo) "
					 + "	and (:visitType = 0 OR  ta.visit_type_id =:visitType) "
					 + "	and (:paymentEntitlementId = 0 OR  tpsd.payment_entitlement_id =:paymentEntitlementId) "
					 + "	order by \"billDate\" desc ";
					
			Query query = sessionFactory.getCurrentSession().createSQLQuery(stringQuery.toString());
			if(requestDto.getFromDate()!=null && requestDto.getFromDate()!="" && requestDto.getToDate()!=null && requestDto.getToDate()!="" ) {
				query.setParameter("fromDate",requestDto.getFromDate()).setParameter("toDate",requestDto.getToDate());
			}
			
			List<PendingServicesForWorkOrderCreationDto> listrequestDto =query
					.setParameter("unitId", requestDto.getUnitId())
					.setParameter("selfPayeeId",requestDto.getSelfPayeeId())
					.setParameter("patientId",requestDto.getPatientId()!=null ? requestDto.getPatientId():0)
					.setParameter("paymentEntitlementId",requestDto.getPaymentEntitlemnetId()!=null ? requestDto.getPaymentEntitlemnetId():0)
					.setParameter("visitType",requestDto.getVisitTypeId()!=null ? requestDto.getVisitTypeId():0)
					.setParameter("billNumber",requestDto.getBillNumber()!=null ? requestDto.getBillNumber():"")
					.setParameter("visitNo",requestDto.getVisitNumber()!=null ? requestDto.getVisitNumber():"")
					.setResultTransformer(Transformers.aliasToBean(PendingServicesForWorkOrderCreationDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listrequestDto, null);

		} catch (HibernateException he) {
			logger.error("getPendingDataForWorkOrderCreation failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getPendingDataForWorkOrderCreation failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public List<PendingServicesForWorkOrderCreationDto> forWorkOrderStatus(PendingServicesForWorkOrderCreationDto requestDto) {
		try {
			StringBuilder stringQuery= new StringBuilder(" select " 
					 + " 	tbd.order_details_id as \"orderDetailsId\", " 
					 + " 	coalesce(tnwocd.workorder_request_details_id, 0) as \"workorderRequestDetialsId\", " 
					 + " 	coalesce(tbd.billing_master_id, 0) as \"billId\", " 
					 + " 	coalesce(tom.admission_id, 0) as \"admissionId\", " 
					 + " 	coalesce(tom.encounter_id, 0) as \"encounterId\" " 
					 + " from " 
					 + " 	t_order_details tod " 
					 /*+ " inner join t_order_details_package_mapper todpm on " 
					 + " 	todpm.order_details_id = tod.order_details_id " 
					 + " 	and todpm.is_package_service = 'Y' " 
					 + " inner join service.m_package_master mpm on "
					 + " 	mpm.package_master_id = todpm.package_id "*/
					 + " inner join t_order_master tom on " 
					 + " 	tod.order_id = tom.order_id " 
					 + " left join billing.t_billing_details tbd on " 
					 + " 	tbd.order_details_id = tod.order_details_id " 
					 + " 	and tbd.status = 'A' " 
					 + " inner join service.t_unit_service_master_details tusmd on " 
					 + " 	tusmd.service_master_id = tod.service_id " 
					 + " 	and tusmd.unit_id =:unitId " 
					 + " left join procurement.t_new_work_order_creation_details tnwocd on " 
					 + " 	tnwocd.order_details_id = tod.order_details_id " 
					 + " 	and tnwocd.status = 'A' " 
					 + " where " 
					 + " 	tusmd.is_outsource_job = 'Y' "
					 //+ "	and mpm.package_type_id = 1 " 
					 + " 	and tusmd.status = 'A' " );
	
			if(requestDto.getBillId()!=null && requestDto.getBillId()>0) 
			{
				stringQuery.append(" and tbd.billing_master_id =:billId ");
			}
			if(requestDto.getEncounterId()!=null && requestDto.getEncounterId()>0) 
			{
				stringQuery.append(" and tom.encounter_id =:encounterId ");
			}
			if(requestDto.getAdmissionId()!=null && requestDto.getAdmissionId()>0) 
			{
				stringQuery.append(" and tom.admission_id =:admissionId ");
			}
			Query createQuery=sessionFactory.getCurrentSession().createSQLQuery(stringQuery.toString())
					.setParameter("unitId", requestDto.getUnitId());

			if(requestDto.getBillId()!=null && requestDto.getBillId()>0) {
				createQuery.setParameter("billId", requestDto.getBillId());
			}

			if(requestDto.getEncounterId()!=null && requestDto.getEncounterId()>0)
			{
				createQuery.setParameter("encounterId", requestDto.getEncounterId());
			}
			if(requestDto.getAdmissionId()!=null && requestDto.getAdmissionId()>0)
			{
				createQuery.setParameter("admissionId", requestDto.getAdmissionId());
			}

			List<PendingServicesForWorkOrderCreationDto> listrequestDto =createQuery.setResultTransformer(Transformers.aliasToBean(PendingServicesForWorkOrderCreationDto.class)).list();
			return listrequestDto;

		} catch (HibernateException he) {
			logger.error("forWorkOrderStatus failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("forWorkOrderStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	
	@Override
	public Response getDetails(PendingServicesForWorkOrderCreationDto requestDto) {
		try {
			StringBuilder stringQuery= new StringBuilder(" select " 
					 + " 	tbd.billing_master_id as \"billId\", " 
					 + " 	tbd.billing_details_id as \"billDetailsId\", " 
					 + " 	tbd.order_details_id as \"orderDetailsId\", " 
					 + " 	tbd.service_id as \"serviceId\", "
					 + "	coalesce(mpm.package_master_id,0) as \"packageId\", " 
					 + " 	msm.service_standard_name as \"serviceName\", " 
					 + " 	cast(coalesce(tbd.net_amt , 0) as double precision) as \"totalNetAmount\", " 
					 + " 	tom.admission_id as \"admissionId\", " 
					 + " 	tom.encounter_id as \"encounterId\", " 
					 + " 	tom.visit_type_id as \"visitTypeId\", " 
					 + " 	tpr.patient_category_id as \"patientCategoryId\", " 
					 + " 	tpsd.payment_entitlement_id as \"paymentEntitlemnetId\", " 
					 + " 	tad.billing_bed_category_id as \"billingBedCategoryId\", " 
					 + " 	tod.order_qty as \"itemQuantity\", " 
					 + " 	coalesce(tnwocd.item_rate, 0) as \"itemRate\", " 
					 + " 	coalesce(tnwocd.item_mrp, 0) as \"itemMrp\", " 
					 + " 	coalesce(tnwocd.item_disc_percentage, 0) as \"itemDiscPercentage\", " 
					 + " 	coalesce(tnwocd.item_disc_amount, 0) as \"itemDiscAmount\", " 
					 + " 	coalesce(tnwocd.item_tax_id, 0) as \"itemTaxId\", " 
					 + " 	coalesce(tnwocd.item_tax_amount, 0) as \"itemTaxAmount\", " 
					 + " 	coalesce(tnwocd.item_net_amount, 0) as \"itemNetAmount\", " 
					 + " 	tnwocd.item_tax as \"itemTax\", " 
					 + " 	tnwocd.item_remark as \"itemRemark\", " 
					 + " 	tnwocd.workorder_request_id as \"workorderRequestId\", " 
					 + " 	tnwocd.workorder_request_details_id as \"workorderRequestDetialsId\", " 
					 + " 	tnwoc.vendor_id as \"vendorId\", "
					 + "	tnwoc.workorder_request_number as \"workorderRequestNumber\" " 
					 + " from " 
					 + " 	t_order_details tod " 
					 + " left join t_order_details_package_mapper todpm on " 
					 + " 	todpm.order_details_id = tod.order_details_id "
					 + " 	and todpm.is_package_service = 'Y' "
					 + " left join service.m_package_master mpm on "
					 + " 	mpm.package_master_id = todpm.package_id and mpm.package_type_id IN (1,5) "
					 + " inner join t_order_master tom on " 
					 + " 	tod.order_id = tom.order_id " 
					 + " left join billing.t_billing_details tbd on " 
					 + " 	tbd.order_details_id = tod.order_details_id " 
					 + " 	and tbd.status = 'A' " 
					 + " inner join service.m_service_master msm on " 
					 + " 	msm.service_master_id = tod.service_id " 
					 + " inner join service.t_unit_service_master_details tusmd on " 
					 + " 	tusmd.service_master_id = tod.service_id " 
					 + " 	and tusmd.unit_id =:unitId " 
					 + " inner join patient.t_patient_registration tpr on " 
					 + " 	tpr.patient_id = tom.patient_id " 
					 + " left join opd.t_encounter_master tem on " 
					 + " 	tem.encounter_id = tom.encounter_id " 
					 + " left join adt.t_admission ta on " 
					 + " 	ta.admission_id = tom.admission_id " 
					 + " left join adt.t_admission_details tad on " 
					 + " 	tad.admission_details_id = ( " 
					 + " 	select " 
					 + " 		max(tad2.admission_details_id) " 
					 + " 	from " 
					 + " 		adt.t_admission_details tad2 " 
					 + " 	where " 
					 + " 		tad.admission_id = tom.admission_id) " 
					 + " left join opd.t_patient_sponsor_details tpsd on " 
					 + " 	(case " 
					 + " 		when tom.encounter_id is not null then tom.encounter_id = tpsd.encounter_id " 
					 + " 		else tom.admission_id = tpsd.admission_id " 
					 + " 	end) " 
					 + " 	and tpsd.priority_id = 1 " 
					 + " 	and tpsd.status = 'A' " 
					 + " left join procurement.t_new_work_order_creation_details tnwocd on " 
					 + " 	tnwocd.order_details_id = tod.order_details_id and tnwocd.status='A' " 
					 + " left join procurement.t_new_work_order_creation tnwoc on " 
					 + " 	tnwocd.workorder_request_id = tnwoc.workorder_request_id " 
					 + " where " 
					 + " 	tusmd.is_outsource_job = 'Y' "
					 + " 	and tusmd.status = 'A' "  );
	
			if(requestDto.getBillId()!=null && requestDto.getBillId()>0) 
			{
				stringQuery.append(" and tbd.billing_master_id =:billId ");
			}
			if(requestDto.getEncounterId()!=null && requestDto.getEncounterId()>0) 
			{
				stringQuery.append(" and tom.encounter_id =:encounterId ");
			}
			if(requestDto.getAdmissionId()!=null && requestDto.getAdmissionId()>0) 
			{
				stringQuery.append(" and tom.admission_id =:admissionId ");
			}
			stringQuery.append(" order by tbd.order_details_id ");
			Query createQuery=sessionFactory.getCurrentSession().createSQLQuery(stringQuery.toString())
					.setParameter("unitId", requestDto.getUnitId());

			if(requestDto.getBillId()!=null && requestDto.getBillId()>0) {
				createQuery.setParameter("billId", requestDto.getBillId());
			}

			if(requestDto.getEncounterId()!=null && requestDto.getEncounterId()>0)
			{
				createQuery.setParameter("encounterId", requestDto.getEncounterId());
			}
			if(requestDto.getAdmissionId()!=null && requestDto.getAdmissionId()>0)
			{
				createQuery.setParameter("admissionId", requestDto.getAdmissionId());
			}

			List<PendingServicesForWorkOrderCreationDto> listrequestDto =createQuery.setResultTransformer(Transformers.aliasToBean(PendingServicesForWorkOrderCreationDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listrequestDto, null);

		} catch (HibernateException he) {
			logger.error("getDetails failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getDetails failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
}
