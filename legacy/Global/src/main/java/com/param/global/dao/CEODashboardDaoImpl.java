package com.param.global.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.BillStatDto;
import com.param.global.model.BillStatModel;
import com.param.opd.encounter.dto.EncounterMasterDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings({"rawtypes","unchecked"})
@Repository
public class CEODashboardDaoImpl extends GenericDao<BillStatModel, Integer> implements ICEODashboardDao, ICommonConstants{
	final static Logger logger = Logger.getLogger(CEODashboardDaoImpl.class);

	
	public CEODashboardDaoImpl() {
		super(BillStatModel.class);
	}
	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public Response getDashboardBedOccupancyList(BillStatDto billStatDto) throws ApplicationException {
		try {
			List<BillStatDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BILL_STAT_LIST")
					.setParameter("organizationId", billStatDto.getOrganizationId())
					.setParameter("unitId", billStatDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(BillStatDto.class)).list();
			return new Response(SUCCESS, null, null, list, null);

		} catch (HibernateException he) {
		logger.error("getDashboardBedOccupancyList failed", he);
		} catch (Exception e) {
		logger.error("getDashboardBedOccupancyList failed", e);
		}
		return new Response(ERROR, null, null, null, null); 
	}

	@Override
	public Response getBillingListByVisitType(BillStatDto billStatDto) throws ApplicationException {
		try {
				List<BillStatDto> list2=new ArrayList<>();
				List<BillStatDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BILLING_LIST_BY_VISIT_TYPE")
					.setParameter("organizationId", billStatDto.getOrganizationId())
					.setParameter("unitId", billStatDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(BillStatDto.class)).list();
				
				for(BillStatDto dto : list) 
				{
					
						List<BillStatDto> list1=sessionFactory.getCurrentSession()
							.getNamedQuery("GET_BILLING_LIST_BY_VISIT_TYPE_ID")
							.setParameter("organizationId", billStatDto.getOrganizationId())
							.setParameter("unitId", billStatDto.getUnitId())
							.setParameter("visitTypeId", dto.getVisitTypeId())
							.setResultTransformer(Transformers.aliasToBean(BillStatDto.class)).list();
						dto.setBillingCollectionList(list1);
						list2.add(dto);
					
					
				}
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
		logger.error("getBillingListByVisitType failed", he);
		} catch (Exception e) {
		logger.error("getBillingListByVisitType failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getOperationCountList(BillStatDto billStatDto) throws ApplicationException {
		try {
			//List<BillStatDto> list2=new ArrayList<>();
			List<BillStatDto> list=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_BILLING_LIST_BY_VISIT_TYPE")
				.setParameter("organizationId", billStatDto.getOrganizationId())
				.setParameter("unitId", billStatDto.getUnitId())
				.setResultTransformer(Transformers.aliasToBean(BillStatDto.class)).list();

			for(BillStatDto dto : list) 
			{			
				List<Integer> arr = new LinkedList<>() ; 	
				List<BillStatDto> list1=sessionFactory.getCurrentSession()
						.getNamedQuery("GET_OPERATION_COUNT_LIST")
						.setParameter("organizationId", billStatDto.getOrganizationId())
						.setParameter("unitId", billStatDto.getUnitId())
						.setParameter("visitTypeId", dto.getVisitTypeId())
						.setResultTransformer(Transformers.aliasToBean(BillStatDto.class)).list();
					
					for(BillStatDto billDto : list1)
					{
//						arr.add(billDto.getEhcCount());
						arr.add(billDto.getLabCount());
						arr.add(billDto.getRadiologyCount());
					}
					dto.setIntcountArr(arr);
				//list2.add(dto);
				
			}
			
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
	} catch (HibernateException he) {
	logger.error("getOperationCountList failed", he);
	} catch (Exception e) {
	logger.error("getOperationCountList failed", e);
	}
	return new Response(ERROR, null, null, null, null);
}
	
	
	@Override
	public Response getAdmissionDischargeDeathCount(BillStatDto billStatDto) throws ApplicationException {
		try {
			//List<BillStatDto> list2=new ArrayList<>();
			List<BillStatDto> list=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_BILLING_LIST_BY_VISIT_TYPE")
				.setParameter("organizationId", billStatDto.getOrganizationId())
				.setParameter("unitId", billStatDto.getUnitId())
				.setResultTransformer(Transformers.aliasToBean(BillStatDto.class)).list();

			for(BillStatDto dto : list) 
			{			
				List<Integer> arr = new LinkedList<>() ; 	
				List<BillStatDto> list1=sessionFactory.getCurrentSession()
						.getNamedQuery("GET_ADMISSION_DISCHARGE_DEATH_COUNT")
						.setParameter("visitTypeId", dto.getVisitTypeId())
						.setParameter("organizationId", billStatDto.getOrganizationId())
						.setParameter("unitId", billStatDto.getUnitId())
						.setResultTransformer(Transformers.aliasToBean(BillStatDto.class)).list();
					
					for(BillStatDto billDto : list1)
					{
						arr.add(billDto.getAdmissionCount());
						arr.add(billDto.getDischargeCount());
						arr.add(billDto.getDeathCount());
					}
					dto.setIntcountArr(arr);
				//list2.add(dto);
				
			}
			
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
	} catch (HibernateException he) {
	logger.error("getAdmissionDischargeDeathCount failed", he);
	} catch (Exception e) {
	logger.error("getAdmissionDischargeDeathCount failed", e);
	}
	return new Response(ERROR, null, null, null, null);
	
}

	
	@Override
	public Response getOPVisitsCount(BillStatDto billStatDto) throws ApplicationException {
		try {
			List<BillStatDto> list2=new ArrayList<>();
			List<BillStatDto> list=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_OP_VISITS_COUNT")
				.setParameter("organizationId", billStatDto.getOrganizationId())
				.setParameter("unitId", billStatDto.getUnitId())
				.setResultTransformer(Transformers.aliasToBean(BillStatDto.class)).list();
			
			for(BillStatDto dto : list) 
			{
				
					List<BillStatDto> list1=sessionFactory.getCurrentSession()
						.getNamedQuery("GET_OP_VISITS_COUNT_BY_ID")
						.setParameter("userDefinedVisitTypeId", dto.getUserDefinedVisitTypeId())
						.setParameter("organizationId", billStatDto.getOrganizationId())
						.setParameter("unitId", billStatDto.getUnitId())
						.setResultTransformer(Transformers.aliasToBean(BillStatDto.class)).list();
					dto.setBillingCollectionList(list1);
					list2.add(dto);
				
				
			}
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
	} catch (HibernateException he) {
	logger.error("getOPVisitsCount failed", he);
	} catch (Exception e) {
	logger.error("getOPVisitsCount failed", e);
	}
	return new Response(ERROR, null, null, null, null);
}
	
	public Response getFinanceDetailsCount(BillStatDto billStatDto) throws ApplicationException {
		try {
			//List<BillStatDto> list2=new ArrayList<>();
			List<BillStatDto> list=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_BILLING_LIST_BY_VISIT_TYPE")
				.setParameter("organizationId", billStatDto.getOrganizationId())
				.setParameter("unitId", billStatDto.getUnitId())
				.setResultTransformer(Transformers.aliasToBean(BillStatDto.class)).list();

			for(BillStatDto dto : list) 
			{			
				List<Integer> arr = new LinkedList<>() ; 	
				List<BillStatDto> list1=sessionFactory.getCurrentSession()
						.getNamedQuery("GET_FINANCE_DETAILS_COUNT")
						.setParameter("visitTypeId", dto.getVisitTypeId())
						.setParameter("organizationId", billStatDto.getOrganizationId())
						.setParameter("unitId", billStatDto.getUnitId())
						.setResultTransformer(Transformers.aliasToBean(BillStatDto.class)).list();
					
					for(BillStatDto billDto : list1)
					{
						arr.add(billDto.getDiscountCount());
						arr.add(billDto.getCreditNoteCount());
						arr.add(billDto.getRefundCount());
						arr.add(billDto.getInsuranceCount());
						arr.add(billDto.getCorporateCompanyCount());
					}
					dto.setFinanceCountArr(arr);
				//list2.add(dto);
				
			}
			
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
	} catch (HibernateException he) {
	logger.error("getFinanceDetailsCount failed", he);
	} catch (Exception e) {
	logger.error("getFinanceDetailsCount failed", e);
	}
	return new Response(ERROR, null, null, null, null);
}

	@Override
	public Response postDashboardCounts(String date,List<Integer> unitIds) throws ApplicationException {
		try{
			Date faDate = GlobalCommonDateUtils.getDate(date, "yyyy-MM-dd");
			boolean flag = (boolean)sessionFactory.getCurrentSession().createSQLQuery("select ceo_dashboard(:faDate,:unitIds)")
					.setParameter("faDate", faDate)
					.setParameter("unitIds", unitIds.toString().replaceAll("\\[","").replaceAll("\\]","")).uniqueResult();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, flag);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response pendingBills(BillStatDto billStatDto) {
		try{
			
			Date d1 = GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(billStatDto.getUnitId()), "yyyy-MM-dd");
			
			Date d2 = new Date(d1.getTime() - (1000*60*60*180));
			String query = " SELECT "
					+ " 	billMst.total_bill_amount AS \"totalBillAmount\", "
					+ " 	billMst.outstanding AS \"outstanding\", "
					+ " 	billMst.bill_number AS \"billNumber\", "
					+ " 	billMst.discount_amount AS \"discountAmount\", "
					+ " 	billMst.billing_master_id AS \"billingMasterId\", "
					+ " 	billMst.total_self_payable AS \"selfPayable\", "
					+ " 	billMst.total_credit_payable AS \"creditPayable\", "
					+ " 	billPayee.paid_amount AS \"paidAmount\", "
					+ " 	billPayee.due_amount AS \"selfDueAmount\", "
					+ " 	(CASE "
					+ " 		WHEN empmst.employee_id IS NOT NULL THEN concat(empmst.first_name, "
					+ " 		' ', "
					+ " 		empmst.last_name) "
					+ " 		WHEN docmst.doctor_id IS NOT NULL THEN concat('Dr. ', "
					+ " 		docmst.first_name, "
					+ " 		' ', "
					+ " 		docmst.last_name) "
					+ " 		ELSE 'Admin' "
					+ " 	END) AS \"userName\", "
					+ " 	concat( prefixMstr.prefix_code,' ',COALESCE(patientReg.first_name),' ',COALESCE(patientReg.middle_name),' ',COALESCE(patientReg.last_name)) AS \"patientName\" "
					+ " FROM "
					+ " 	billing.m_billing_master billMst "
					+ " INNER JOIN patient.t_patient_registration patientReg ON "
					+ " 	patientReg.patient_id = billMst.patient_id "
					+ " INNER JOIN public.m_prefix_master prefixMstr ON "
					+ " 	patientReg.prefix_id = prefixMstr.prefix_id "
					+ " LEFT OUTER JOIN billing.t_bill_payee_details billPayee ON "
					+ " 	billPayee.bill_id = billMst.billing_master_id "
					+ " 	AND billPayee.payee_id = ( "
					+ " 	SELECT "
					+ " 		tuabc.self_payee_id "
					+ " 	FROM "
					+ " 		t_unit_application_billing_configuration tuabc "
					+ " 	WHERE "
					+ " 		tuabc.unit_id = :unitId "
					+ " 		AND status = 'A') "
					+ " LEFT JOIN PUBLIC.m_employee_master empmst ON "
					+ " 	billMst.created_by = empmst.user_id "
					+ " LEFT JOIN doctor.m_doctor_master docmst ON "
					+ " 	billMst.created_by = docmst.user_id "
					+ " WHERE "
					+ " 	billMst.organisation_id = :orgId "
					+ "	AND billMst.status='A' "
					+ " 	AND billMst.unit_id = :unitId and billPayee.due_amount > 0  "
					+ " 	AND to_char(billMst.bill_date_time,'YYYY-MM-DD') >= :startDate "
					+ " 	AND to_char(billMst.bill_date_time,'YYYY-MM-DD') <= :endDate "
					+ " ORDER BY "
					+ " 	billMst.bill_date_time DESC ";

			List<EncounterMasterDto> list1 = sessionFactory.getCurrentSession()
					.createSQLQuery(query)
					.setParameter("orgId", billStatDto.getOrganizationId())
					.setParameter("unitId", billStatDto.getUnitId())
					.setParameter("startDate", GlobalCommonDateUtils.getStringDate(d2, "yyyy-MM-dd"))
					.setParameter("endDate", GlobalCommonDateUtils.getStringDate(d1, "yyyy-MM-dd"))
					.setResultTransformer(Transformers.aliasToBean(EncounterMasterDto.class))
					.list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list1, null);
		}catch(Exception e){
			
			throw e;
		}
	}
}
