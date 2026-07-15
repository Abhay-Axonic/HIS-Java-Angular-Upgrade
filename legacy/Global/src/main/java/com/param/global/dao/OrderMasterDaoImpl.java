package com.param.global.dao;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.billing.dto.GetDoctorShareDto;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.ApplicationConfigurationDto;
import com.param.global.dto.BillingMasterDto;
import com.param.global.dto.OrderDetailsMasterDto;
import com.param.global.dto.OrderMasterDto;
import com.param.global.dto.UnitApplicationConfigurationDto;
import com.param.global.dto.UnitServiceMasterDetailsDto;
import com.param.global.dto.global.SlotCheckDto;
import com.param.global.model.OrderDetailsMaster;
import com.param.global.model.OrderMaster;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class OrderMasterDaoImpl extends GenericDao<OrderMaster, Integer> implements IOrderMasterDao, ICommonConstants{

	private static final Logger logger = Logger.getLogger(OrderMasterDaoImpl.class);

	public OrderMasterDaoImpl() {
		super(OrderMaster.class);
	}

	@Autowired
	private DozerBeanMapper mapper;
	
	@Override
	public Response saveOrderMaster(OrderMasterDto orderMasterDto) throws ApplicationException {
		try {
			
			Long currentDateTime = TimeZoneComponent.getDateByzone(orderMasterDto.getOrgUnitId()).getTime();
			
			if(orderMasterDto.getReturnDate()!= null){
				SimpleDateFormat formatter2=new SimpleDateFormat("dd-MM-yyyy");  
				try {
					currentDateTime = formatter2.parse(orderMasterDto.getReturnDate()).getTime();
				} catch (ParseException e) {
					logger.error("saveOrderMaster: return date parse failed", e);
				}
			}

			OrderMaster orderMaster = mapper.map(orderMasterDto, OrderMaster.class, "OrderMasterDto_to_OrderMaster");
				orderMaster.setCreatedDate(currentDateTime);
				orderMaster.setUpdatedDate(currentDateTime);
				orderMaster.setOrderDate(currentDateTime);
			Integer orderId= (Integer) sessionFactory.getCurrentSession().save(orderMaster);
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, orderId);
		}catch (Exception e) {
			logger.error("saveOrderMaster failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE,COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getDoctorSharePercentage(OrderDetailsMasterDto orderDetailsMasterDto,Integer visitTypeId,Integer billingBedCategoryId,Integer patientTypeId,Integer paymentEntitlementId) {
		try {

			if(orderDetailsMasterDto.getOrgUnitId()!=null && orderDetailsMasterDto.getOrdDocId()!=null && orderDetailsMasterDto.getOrdDocSplId()!=null && billingBedCategoryId!=null && visitTypeId!=null && orderDetailsMasterDto.getTariffId()!=null && (orderDetailsMasterDto.getServiceId()!=null||orderDetailsMasterDto.getDepartmentId()!=null))
			{
				String query = "select "
						+ "	doc_share as \"docShare\", "
						+ "	doc_share_amount as \"docShareAmount\", "
						+ "	to_char(effective_date, 'yyyy-mm-dd') as \"effectiveDate\", "
						+ "	1 as \"docShareType\" "
						+ "from "
						+ "	billing.t_individual_doc_share_service_wise "
						+ "where "
						+ "	doctor_id =:doctorId "
						+ "	and speciality_id=:specialityId "
						+ "	and service_id=:serviceId "
						+ "	and billing_bed_category_id=:billingBedCategoryId "
						+ "	and patient_type_id=:patientTypeId "
						+ "	and tariff_id=:tariffId "
						+ " and payment_entitlement_id =:paymentEntitlementId "
						+ "	and visit_type_id=:visitTypeId "
						+ "	and status = 'A' "
						+ "	and org_unit_id=:unitId "
						+ "	and effective_date < '"+GlobalCommonDateUtils.getStringDate(orderDetailsMasterDto.getOrderDate(), "yyyy-M-dd")+"' "
						+ "union select "
						+ "	doc_share as \"docShare\", "
						+ "	doc_share_amount as \"docShareAmount\", "
						+ "	to_char(effective_date, 'yyyy-mm-dd') as \"effectiveDate\", "
						+ "	2 as \"docShareType\" "
						+ "from "
						+ "	billing.t_individual_doc_share_group_wise "
						+ "where "
						+ "	doctor_id =:doctorId "
						+ "	and speciality_id=:specialityId "
						+ "	and department_id =:serSplId "
						+ "	and billing_bed_category_id=:billingBedCategoryId "
						+ " and payment_entitlement_id =:paymentEntitlementId "
						+ "	and patient_type_id=:patientTypeId "
						+ "	and tariff_id=:tariffId "
						+ "	and visit_type_id=:visitTypeId "
						+ "	and status = 'A' "
						+ "	and org_unit_id=:unitId "
						+ "	and effective_date < '"+GlobalCommonDateUtils.getStringDate(orderDetailsMasterDto.getOrderDate(), "yyyy-M-dd")+"' "
						+ "	 "
						+ " union select "
						+ "	doc_share as \"docShare\", "
						+ "	amount as \"docShareAmount\", "
						+ "	to_char(effective_date, 'yyyy-mm-dd') as \"effectiveDate\", "
						+ "	3 as \"docShareType\" "
						+ "from "
						+ "	billing.t_global_doc_share_service_wise "
						+ "where "
						+ "	speciality_id=:specialityId "
						+ "	and service_id=:serviceId "
						+ "	and billing_bed_category_id=:billingBedCategoryId "
						+ "	and patient_type_id=:patientTypeId "
						+ "	and tariff_id=:tariffId "
						+ " and payment_entitlement_id =:paymentEntitlementId "
						+ "	and visit_type_id=:visitTypeId "
						+ "	and status = 'A' "
						+ "	and org_unit_id=:unitId "
						+ "	and effective_date < '"+GlobalCommonDateUtils.getStringDate(orderDetailsMasterDto.getOrderDate(), "yyyy-M-dd")+"' "
						+ " union select "
						+ "	doc_share as \"docShare\", "
						+ "	doc_share_amount as \"docShareAmount\", "
						+ "	to_char(effective_date, 'yyyy-mm-dd') as \"effectiveDate\", "
						+ "	4 as \"docShareType\" "
						+ "from "
						+ "	billing.t_global_doc_share_group_wise "
						+ "where "
						+ "	speciality_id=:specialityId "
						+ "	and department_id =:serSplId "
						+ "	and billing_bed_category_id=:billingBedCategoryId "
						+ "	and patient_type_id=:patientTypeId "
						+ "	and tariff_id=:tariffId "
						+ " and payment_entitlement_id =:paymentEntitlementId "
						+ "	and visit_type_id=:visitTypeId "
						+ "	and status = 'A' "
						+ "	and org_unit_id=:unitId "
						+ "	and effective_date < '"+GlobalCommonDateUtils.getStringDate(orderDetailsMasterDto.getOrderDate(), "yyyy-M-dd")+"' "
						+ "union select "
						+ "	(case when is_doc_share_per_wise='Y' then doc_ref_per else 0 end) as \"docShare\", "
						+ "	(case when is_doc_share_per_wise='N' then doc_ref_per else 0 end) as \"docShareAmount\", "
						+ "	'-' as \"effectiveDate\", "
						+ "	5 as \"docShareType\" "
						+ "from "
						+ "	service.t_unit_service_master_details "
						+ "where "
						+ "	service_master_id = :serviceId "
						+ "	and unit_id = :unitId "
						+ " order by \"docShareType\" ";



				List<GetDoctorShareDto> listServiceMasterDto = sessionFactory.getCurrentSession()
						.createSQLQuery(query)
						.setInteger("doctorId",orderDetailsMasterDto.getOrdDocId())
						.setInteger("specialityId",orderDetailsMasterDto.getOrdDocSplId())
						.setInteger("serviceId",orderDetailsMasterDto.getServiceId())
						.setInteger("billingBedCategoryId",billingBedCategoryId)
						.setInteger("visitTypeId",visitTypeId)
						.setInteger("patientTypeId",patientTypeId)
						.setInteger("paymentEntitlementId",paymentEntitlementId)
						.setInteger("unitId", orderDetailsMasterDto.getOrgUnitId())
						.setInteger("tariffId", orderDetailsMasterDto.getTariffId())
						.setInteger("serSplId", orderDetailsMasterDto.getDepartmentId())
						.setResultTransformer(Transformers.aliasToBean(GetDoctorShareDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, listServiceMasterDto, null);
			}else{
				return new Response(SUCCESS, null, null, null, null);
			}

		}catch (HibernateException he) {
			logger.error("getDoctorSharePercentage failed", he);
		} catch (Exception e) {
			logger.error("getDoctorSharePercentage failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}



	@Override
	public Response getDoctorReferralPercentage(OrderDetailsMasterDto orderDetailsMasterDto,Integer referralId,Integer visitTypeId,Integer billingBedCategoryId,Integer patientTypeId,Integer paymentEntitlementId) {
		try {
			if(orderDetailsMasterDto.getOrgUnitId()!=null && referralId!=null && billingBedCategoryId!=null && visitTypeId!=null && orderDetailsMasterDto.getTariffId()!=null && (orderDetailsMasterDto.getServiceId()!=null||orderDetailsMasterDto.getDepartmentId()!=null))
			{
				String query = "select " 
						 + " 	doc_share as \"docShare\", " 
						 + " 	doc_share_amount as \"docShareAmount\", " 
						 + " 	to_char(effective_date, 'yyyy-mm-dd') as \"effectiveDate\", " 
						 + " 	1 as \"docShareType\" " 
						 + " from " 
						 + " 	billing.t_individual_doc_share_service_wise " 
						 + " where " 
						 + " 	referral_id =:referralId " 
						 + " 	and service_id =:serviceId " 
						 + " 	and billing_bed_category_id =:billingBedCategoryId " 
						 + " 	and patient_type_id =:patientTypeId " 
						 + " 	and tariff_id =:tariffId " 
						 + " 	and payment_entitlement_id =:paymentEntitlementId " 
						 + " 	and visit_type_id =:visitTypeId " 
						 + " 	and status = 'A' " 
						 + " 	and org_unit_id =:unitId " 
						 + " 	and effective_date < :orderDate "
						 + " union " 
						 + " select " 
						 + " 	doc_share as \"docShare\", " 
						 + " 	doc_share_amount as \"docShareAmount\", " 
						 + " 	to_char(effective_date, 'yyyy-mm-dd') as \"effectiveDate\", " 
						 + " 	2 as \"docShareType\" " 
						 + " from " 
						 + " 	billing.t_individual_doc_share_group_wise " 
						 + " where " 
						 + " 	referral_id =:referralId " 
						 + " 	and department_id =:serSplId " 
						 + " 	and billing_bed_category_id =:billingBedCategoryId " 
						 + " 	and payment_entitlement_id =:paymentEntitlementId " 
						 + " 	and patient_type_id =:patientTypeId " 
						 + " 	and tariff_id =:tariffId " 
						 + " 	and visit_type_id =:visitTypeId " 
						 + " 	and status = 'A' " 
						 + " 	and org_unit_id =:unitId " 
						 + " 	and effective_date < :orderDate "
						 + " union " 
						 + " select " 
						 + " 	(case when is_ref_share_per_wise='Y' then ref_share_per else 0 end) as \"docShare\", " 
						 + " 	(case when is_ref_share_per_wise='N' then ref_share_per else 0 end) as \"docShareAmount\", " 
						 + " 	'-' as \"effectiveDate\", " 
						 + " 	3 as \"docShareType\" " 
						 + " from " 
						 + " 	service.t_unit_service_master_details " 
						 + " where " 
						 + " 	service_master_id = :serviceId " 
						 + " 	and unit_id = :unitId " 
						 + " order by " 
						 + " 	\"docShareType\" ";
				
				Query sql= sessionFactory.getCurrentSession()
						.createSQLQuery(query)
						.setParameter("referralId",referralId)
						.setParameter("serviceId",orderDetailsMasterDto.getServiceId())
						.setParameter("billingBedCategoryId",billingBedCategoryId) 
						.setParameter("visitTypeId",visitTypeId)
						.setParameter("patientTypeId",patientTypeId)
						.setParameter("paymentEntitlementId",paymentEntitlementId)
						.setParameter("unitId", orderDetailsMasterDto.getOrgUnitId())
						.setParameter("tariffId", orderDetailsMasterDto.getTariffId())
						.setParameter("serSplId", orderDetailsMasterDto.getDepartmentId())
						.setParameter("orderDate", new Date(orderDetailsMasterDto.getOrderDate()), StandardBasicTypes.DATE);
				List<GetDoctorShareDto> listServiceMasterDto = sql.setResultTransformer(Transformers.aliasToBean(GetDoctorShareDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, listServiceMasterDto, null);
			}else{
				return new Response(SUCCESS, null, null, null, null);
			}
		}catch (HibernateException he) {
			logger.error("getDoctorReferralPercentage failed", he);
		} catch (Exception e) {
			logger.error("getDoctorReferralPercentage failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Long getNextValOrderMasterId() throws ApplicationException {
		try {
			Session session = sessionFactory.getCurrentSession();
			SQLQuery query = session.createSQLQuery("select nextval('public.m_seq_order_master')");
			Long id = ((BigInteger) query.uniqueResult()).longValue();

			SQLQuery query2 = session.createSQLQuery("select setval('public.m_seq_order_master', :value)");
			query2.setParameter("value", id - 1);
			query2.uniqueResult();

			return id;
		}catch (HibernateException he) {
			logger.error("getNextValOrderMasterId failed", he);
		} catch (Exception e) {
			logger.error("getNextValOrderMasterId failed", e);
		}
		return null;
	}

	@Override
	public Integer getOrderNumberPharmacyByAdmission(Integer admissionId) throws ApplicationException {
		try {	
			Integer orderId  = (Integer)sessionFactory.getCurrentSession().createSQLQuery("select "
					+ "	coalesce(ord.order_id,0) from public.t_order_master ord "
					+ "where "
					+ "	ord.is_pharmacy = 'Y' "
					+ "	and ord.admission_id = :admissionId ")
				.setParameter("admissionId", admissionId)
				.uniqueResult();
			return (orderId==null)?0:orderId;
		}catch(Exception e) {
			
			throw e;
		}
	}

	@Override
	public Integer getOrderDetailsIdByOrderId(int orderId, int orgId, int unitId) throws ApplicationException {
		try {	
			Integer orderDetailsId  = (Integer)sessionFactory.getCurrentSession().createSQLQuery("select "
					+ "	coalesce(ord_dtls.order_details_id, "
					+ "	0) "
					+ " from "
					+ "	public.t_order_details ord_dtls "
					+ " where "
					+ "	ord_dtls.order_id = :orderId "
					+ "	and ord_dtls.status = 'A' "
					+ "	and ord_dtls.service_id = ( "
					+ "	select "
					+ "		config.pharmacy_service_id "
					+ "	from "
					+ "		public.t_unit_application_billing_configuration config "
					+ "	where "
					+ "		config.organization_id =:orgId "
					+ "		and config.unit_id =:unitId"
					+ "     and config.status = 'A' )")
				.setParameter("orderId", orderId)
				.setParameter("orgId", orgId)
				.setParameter("unitId", unitId)
				.uniqueResult();
			return (orderDetailsId==null)?0:orderDetailsId;
		}catch(Exception e) {
			
			throw e;
		}
	}

	@Override
	public Integer updateOrderDetails(OrderDetailsMasterDto dto) throws ApplicationException {
		try {	
			Integer result  = (Integer)sessionFactory.getCurrentSession().createSQLQuery(" update "
					+ "	public.t_order_details set "
					+ "	 net_amount = coalesce(net_amount, "
					+ "		0) + :netAmt , "
					+ "	self_payable = coalesce(self_payable, "
					+ "		0) + :netAmt ,"
					+ "	service_amt = coalesce(service_amt, "
					+ "		0) + :serviceAmt "
					+ "	where "
					+ "		order_details_id = :orderDetailsId" )
				.setBigDecimal("netAmt", dto.getNetAmount())
				.setBigDecimal("serviceAmt", dto.getServiceAmt())
				.setParameter("orderDetailsId", dto.getOrderDetailsId())
				.executeUpdate();
			return result;
		}catch(Exception e) {
			
			throw e;
		}
	}

	@Override
	public UnitApplicationConfigurationDto getPharmacyServiceId(int orgId, int unitId) throws ApplicationException {
		try {	
			UnitApplicationConfigurationDto dto  = (UnitApplicationConfigurationDto)sessionFactory.getCurrentSession().createSQLQuery(
					 "		select "
					+ "			config.pharmacy_service_id as \"pharmacyServiceId\","
					+ "			config.pharmacy_return_sale_service_id as \"pharmacyReturnSaleServiceId\", "
					+ "			dtls.is_ref_doctor_share as \"isRefDoctorShare\", "
					+ "			serv.speciality_id as \"serviceSpecilaityId\", "
					+ "			config.default_discount_ownership as \"defaultDiscountOwnership\", "
					+ "			mac.tax_inclusive as \"taxInclusive\", "
					+ "			config.is_consolidated_bill as \"isConsolidatedBill\", "
					+ "			config.self_payee_id as \"selfPayeeId\", "
					+ "			config.default_self_tariff_id as \"defaultSelfTariffId\" "
					+ "		from "
					+ "			public.t_unit_application_billing_configuration config "
					+ "			inner join service.t_unit_service_master_details dtls on "
					+ "				dtls.service_master_id = config.pharmacy_service_id "
					+ "			inner join service.m_service_master serv on "
					+ "				serv.service_master_id = config.pharmacy_service_id	"
					+ "			inner join m_application_configuration mac on "
					+ "				mac.unit_id = config.unit_id "
					+ "		where "
					+ "			config.organization_id =:orgId "
					+ "			and config.unit_id =:unitId "
					+ "			and dtls.unit_id = config.unit_id "
					+ "         and config.status ='A' limit 1" )
				.setParameter("orgId", orgId)
				.setParameter("unitId", unitId)
				.setResultTransformer(Transformers.aliasToBean(UnitApplicationConfigurationDto.class)).uniqueResult();
			
			return dto;
		}catch(Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response getOtConfiguredServiced(int orgId, int unitId) throws ApplicationException {
		try {	
			UnitApplicationConfigurationDto dto  = (UnitApplicationConfigurationDto)sessionFactory.getCurrentSession().createSQLQuery(
					 "		select "
					+ "			config.ot_rent_service_id as \"otRentServiceId\", "
					+ "			config.surgeon_charges_service_id as \"surgeonChargesServiceId\" "
					+ "		from "
					+ "			public.t_unit_application_billing_configuration config "
					+ "		where "
					+ "			config.organization_id =:orgId "
					+ "			and config.unit_id =:unitId "
					+ "         and status ='A' " )
				.setParameter("orgId", orgId)
				.setParameter("unitId", unitId)
				.setResultTransformer(Transformers.aliasToBean(UnitApplicationConfigurationDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, dto);
		}catch(Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response getAutoPostingOrderedByAdmittionAndDate(Integer admissionId, Date currentDate)
			throws ApplicationException {
		try {	
			
			
			StringBuffer sb = new StringBuffer("select "
							+ "ord_dtls.order_details_id as \"orderDetailsId\", "
							+ "ord_dtls.order_id as \"orderId\", "
							+ "ser.speciality_id as \"specialityId\" "
							+ "from t_order_master ord_mst "
							+ "inner join PUBLIC.t_order_details ord_dtls on "
							+ "	ord_mst.order_id = ord_dtls.order_id "
							+ "	inner join service.m_service_master ser on "
							+ "	ser.service_master_id = ord_dtls.service_id "
							+ "where "
							+ "ord_dtls.ord_cancelled = 'N' "
							+ "and ord_dtls.status = 'A' "
							+ "and ord_dtls.is_posted_while_discharge='Y' "
							+ "and ord_mst.admission_id = :admissionId ");
			List<OrderDetailsMasterDto> list  = (List<OrderDetailsMasterDto>)sessionFactory.getCurrentSession().createSQLQuery(
					sb.toString()  )
				.setParameter("admissionId", admissionId)
				.setResultTransformer(Transformers.aliasToBean(OrderDetailsMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, list, null);
		}catch(Exception e) {
			
			throw e;
		}

	}

	@Override
	public Response getMedicationsByOrderId(int orderId) throws ApplicationException {
     try {
			List<OrderDetailsMaster> list = (List<OrderDetailsMaster>)sessionFactory.getCurrentSession()
					.createQuery("select od from  OrderDetailsMaster od where od.orderId =:orderId  ")
					.setParameter("orderId", orderId)
					.list();
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);

		} catch (HibernateException he) {
			logger.error("getMedicationsByOrderId failed", he);
		} catch (Exception e) {
			logger.error("getMedicationsByOrderId failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateOrderDetailsByOdrDtlsId(Integer orderDetailsId) throws ApplicationException {
		try {
			sessionFactory.getCurrentSession()
					.createSQLQuery(" 	update public.t_order_details "
							+"	set is_investigation_order='Y' "
							+"	where order_details_id=:orderDetailsId ")
					.setParameter("orderDetailsId", orderDetailsId)
					.executeUpdate();
			return new Response(SUCCESS, SUCCESS_CODE, null, null, null);

		} catch (HibernateException he) {
			logger.error("updateOrderDetailsByOdrDtlsId failed", he);
		} catch (Exception e) {
			logger.error("updateOrderDetailsByOdrDtlsId failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public UnitServiceMasterDetailsDto checkIfReviewApplicableForService(Integer orderDetailsId) {
		try {
			UnitServiceMasterDetailsDto dto =null;
			if(orderDetailsId != null ){
				String query = "select "
						+ " 	is_review_applicable as \"isReviewApplicable\", "
						+ " 	review_time as \"reviewTime\", "
						+ " 	om.ord_doctor_id as \"doctorId\", "
						+ " 	om.ord_doc_spl_id as \"specialityId\", "
						+ "	uspl.dept_wise_appointment as \"deptWiseAppointment\", "
						+ "	tod.org_unit_id as \"unitId\" "
						+ " from t_order_details tod  "
						+ " 	inner join service.t_unit_service_master_details tusmd on  "
						+ " 		tod.service_id = tusmd.service_master_id  "
						+ " 	inner join t_order_master om on  "
						+ " 		om.order_id = tod.order_id  "
						+ " 	inner join doctor.m_speciality_master spl on "
						+ " 		spl.speciality_id = om.ord_doc_spl_id "
						+ "	inner join t_unit_speciality_mapper uspl on "
						+ " 		uspl.speciality_id = om.ord_doc_spl_id and uspl.unit_id = tusmd.unit_id "
						+ " where "
						+ " 	tod.org_unit_id = tusmd.unit_id "
						+ " 	and tod.order_details_id =:orderDetailsId  "
						+ " 	and is_review_applicable = 'Y' "
						+ "	and om.visit_type_id=1 " //for op only
						+ " 	order by tod.order_details_id desc "
						+ " 	limit 1 ";

				dto = (UnitServiceMasterDetailsDto)sessionFactory.getCurrentSession()
						.createSQLQuery(query)
						.setParameter("orderDetailsId",orderDetailsId)
						.setResultTransformer(Transformers.aliasToBean(UnitServiceMasterDetailsDto.class))
						.uniqueResult();

				return dto;
			}

			return dto;
		} catch (HibernateException he) {
			logger.error("checkIfReviewApplicableForService failed", he);
		} catch (Exception e) {
			logger.error("checkIfReviewApplicableForService failed", e);
		}
		return null;
	}

	@Override
	public List<SlotCheckDto> getSlotsDataForReview(UnitServiceMasterDetailsDto unitServiceMasterDetailsDto) {
		try {

			StringBuilder query = new StringBuilder("SELECT "
					+ " 	to_char(msm.appointment_date, 'YYYY-MM-DD') AS \"appointmentDate\" "
					+ "FROM opd.m_slot_master msm "
					+ "WHERE to_char(appointment_date, 'YYYY-MM-DD') > :appointmentDate ");

			String query2 = " AND speciality_id = :specialityId AND unit_id = :unitId "
					+ "GROUP BY msm.appointment_date "
					+ "ORDER BY msm.appointment_date "
					+ "LIMIT 50";

			List<SlotCheckDto> slotCheckDtosList = sessionFactory.getCurrentSession()
					.createSQLQuery(query.append(query2).toString())
					.setParameter("appointmentDate", GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitServiceMasterDetailsDto.getUnitId()), "yyyy-MM-dd"))
					.setParameter("specialityId", unitServiceMasterDetailsDto.getSpecialityId())
					.setParameter("unitId", unitServiceMasterDetailsDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(SlotCheckDto.class))
					.list();


			return slotCheckDtosList;

		} catch (HibernateException he) {
			logger.error("getSlotsDataForReview failed", he);
		} catch (Exception e) {
			logger.error("getSlotsDataForReview failed", e);
		}
		return null;
	}

	@Override
	public Integer assignReviewApplicableDate(Integer orderDetailsId, String appointmentDate) {
		try {
			int i = sessionFactory.getCurrentSession()
					.createSQLQuery("UPDATE t_order_details SET review_applicable_date = :appointmentDate WHERE order_details_id = :orderDetailsId")
					.setParameter("appointmentDate", appointmentDate)
					.setParameter("orderDetailsId", orderDetailsId)
					.executeUpdate();
			return i;

		} catch (HibernateException he) {
			logger.error("assignReviewApplicableDate failed", he);
		} catch (Exception e) {
			logger.error("assignReviewApplicableDate failed", e);
		}
		return 0;
	}

	@Override
	public OrderDetailsMasterDto getOrderIdForLabService(Integer encounterId, Integer admissionId) {
		try {
			
			StringBuilder query = null;
			if(encounterId!=null && encounterId>0)
			{
				 query = new StringBuilder(" select "
						+ " om.order_id as \"orderId\" ,"
						+ " to_char( om.order_date , 'YYYY-MM-DD') as \"orderDateString\"  "
						+ " from "
						+ " 	t_order_master om "
						+ " inner join t_order_details od on "
						+ " 	om.order_id = od.order_id "
						+ " inner join service.m_service_master ser on "
						+ " 	od.service_id = ser.service_master_id "
						+ " where "
						+ " 	om.encounter_id =:encounterId "
						 + " 	and od.service_is_billed = 0 " 
						 + " 	and od.status = 'A' " 
						 + " 	and om.order_status_id = 1 "
						+ " 	and ser.speciality_id = ( "
						+ " 	select "
						+ " 		tlc.lab_speciality_id "
						+ " 	from "
						+ " 		t_lab_configuration tlc "
						+ " 	where "
						+ " 		unit_id = om.org_unit_id) order by 1 desc limit 1" );
			}else{
				query = new StringBuilder(  " select " 
						+ " om.order_id as \"orderId\" ,"
						+ " to_char( om.order_date , 'YYYY-MM-DD') as \"orderDateString\"  "
						 + " from " 
						 + " 	t_order_master om " 
						 + " inner join t_order_details od on " 
						 + " 	om.order_id = od.order_id " 
						 + " inner join service.m_service_master ser on " 
						 + " 	od.service_id = ser.service_master_id " 
						 + " where " 
						 + " 	om.admission_id =:admissionId  "
						 + " 	and od.service_is_billed = 0 " 
						 + " 	and od.status = 'A' " 
						 + " 	and om.order_status_id = 1 "
						 + " 	and ser.speciality_id = ( " 
						 + " 	select " 
						 + " 		tlc.lab_speciality_id " 
						 + " 	from " 
						 + " 		t_lab_configuration tlc " 
						 + " 	where " 
						 + " 		unit_id = om.org_unit_id) " 
						 + " order by " 
						 + " 	1 desc " 
						 + " limit 1 "  );
			}
			 
			Query createQuery = sessionFactory.getCurrentSession()
					.createSQLQuery(query.toString());
			if(encounterId!=null && encounterId>0)
			{
				createQuery.setParameter("encounterId",encounterId);
			}else{
				createQuery.setParameter("admissionId",admissionId);
			}


			OrderDetailsMasterDto dto = (OrderDetailsMasterDto)createQuery.setResultTransformer(Transformers.aliasToBean(OrderDetailsMasterDto.class)).uniqueResult();
		
			
			
			return dto;

		} catch (HibernateException he) {
			logger.error("getOrderIdForLabService failed", he);
		} catch (Exception e) {
			logger.error("getOrderIdForLabService failed", e);
		}
		return null;
	}

	@Override
	public Response updateIsHomeCollection(List<Integer> orderIds, boolean homeCollection) {
		try {
			
			 int i = sessionFactory.getCurrentSession()
					.createSQLQuery("update t_order_master set home_collection=:homeCollection where order_id in (:orderIds)")
					.setParameterList("orderIds", orderIds)
					.setBoolean("homeCollection", homeCollection)
					.executeUpdate();
			
			return new Response(SUCCESS,null,null,null,null);

		} catch (HibernateException he) {
			logger.error("updateIsHomeCollection failed", he);
		} catch (Exception e) {
			logger.error("updateIsHomeCollection failed", e);
		}
		return new Response(ERROR,null,null,null,null);
	}

	@Override
	public Response updateOrderMasterForWebApp(OrderMasterDto orderMasterDto) throws ApplicationException {
		try {

			 int i = sessionFactory.getCurrentSession()
					.createSQLQuery("update t_order_master set encounter_id=:encounterId,visit_type_id=:visitTypeId where order_id= :orderId")
					.setParameter("encounterId", orderMasterDto.getEncounterId())
					.setParameter("visitTypeId", orderMasterDto.getVisitTypeId())
					.setParameter("orderId", orderMasterDto.getOrderId())
					.executeUpdate();

			return new Response(SUCCESS,null,null,null,null);

		} catch (HibernateException he) {
			logger.error("updateOrderMasterForWebApp failed", he);
		} catch (Exception e) {
			logger.error("updateOrderMasterForWebApp failed", e);
		}
		return new Response(ERROR,null,null,null,null);
	}

	@Override
	public Response cashPaidAmount(OrderMasterDto orderMasterDto) {
		try {
			
			StringBuilder query = null;
			if(orderMasterDto.getEncounterId()!=null && orderMasterDto.getEncounterId()>0)
			{
				 query = new StringBuilder(" select coalesce(sum(tprd.amount),0) from billing.m_billing_master mbm  " + 
				 		"inner join billing.t_payment_reciept_master tprm on tprm.billing_master_id  = mbm.billing_master_id  " + 
				 		"inner join billing.t_payment_reciept_details tprd on tprd.payment_receipt_master_id = tprm.payment_reciept_id  " + 
				 		"where mbm.encounter_id  =:encounterId  and tprd.payment_mode_id = 1 " );
			}else{
				query = new StringBuilder( " select coalesce(sum(tprd.amount),0) from billing.m_billing_master mbm  " + 
				 		"inner join billing.t_payment_reciept_master tprm on tprm.billing_master_id  = mbm.billing_master_id  " + 
				 		"inner join billing.t_payment_reciept_details tprd on tprd.payment_receipt_master_id = tprm.payment_reciept_id  " + 
				 		"where mbm.admission_id  =:admissionId  and tprd.payment_mode_id = 1 ");
			}
				
		Query createQuery =	sessionFactory.getCurrentSession()
					.createSQLQuery(query.toString());
			if(orderMasterDto.getEncounterId()!=null && orderMasterDto.getEncounterId()>0) {
				createQuery.setParameter("encounterId", orderMasterDto.getEncounterId());
			}else{
				createQuery.setParameter("admissionId",orderMasterDto.getAdmissionId());
			}

			Double cashAmount = (Double)createQuery.uniqueResult();
			
			StringBuilder query1 = null;
			if(orderMasterDto.getEncounterId()!=null && orderMasterDto.getEncounterId()>0)
			{
				 query1 = new StringBuilder(" select coalesce(sum(tspm.amount),0) from pharmacy.t_sale_payment_mode tspm " + 
				 		"	inner join pharmacy.t_sale ts on ts.id = tspm.sale_id   " + 
				 		"where ts.encounter_id  =:encounterId  and tspm.payment_mode_id = 1 " );
			}else{
				query1 = new StringBuilder( " select coalesce(sum(tspm.amount),0) from pharmacy.t_sale_payment_mode tspm " + 
						"	inner join pharmacy.t_sale ts on ts.id = tspm.sale_id   " + 
				 		"where ts.admission_id  =:admissionId  and tspm.payment_mode_id = 1 ");
			}
				
			Query cretay = sessionFactory.getCurrentSession()
					.createSQLQuery(query1.toString());
			if(orderMasterDto.getEncounterId()!=null && orderMasterDto.getEncounterId()>0) {
				cretay.setParameter("encounterId", orderMasterDto.getEncounterId());
			}else{
				cretay.setParameter("admissionId",orderMasterDto.getAdmissionId());
			}

			Double cashAmountPharmacy = (Double)cretay.uniqueResult();
			
			cashAmount = cashAmount + cashAmountPharmacy;
			
			return new Response(SUCCESS,null,null,null,cashAmount);

		} catch (HibernateException he) {
			logger.error("cashPaidAmount failed", he);
		} catch (Exception e) {
			logger.error("cashPaidAmount failed", e);
		}
		return new Response(ERROR,null,null,null,null);
	}

	@Override
	public Response saveNeuronPlusorderedServices(OrderMasterDto orderMasterDto) throws ApplicationException {
		try {

			 int i = sessionFactory.getCurrentSession()
					.createSQLQuery("update t_order_master set neuron_plus_url=:neuronPlusUrl where order_id= :orderId")
					.setParameter("neuronPlusUrl", orderMasterDto.getNeuronPlusUrl())
					.setParameter("orderId", orderMasterDto.getOrderId())
					.executeUpdate();

			return new Response(SUCCESS,null,null,null,orderMasterDto);

		} catch (HibernateException he) {
			logger.error("saveNeuronPlusorderedServices failed", he);
		} catch (Exception e) {
			logger.error("saveNeuronPlusorderedServices failed", e);
		}
		return new Response(ERROR,null,null,null,null);
	}

	@Override
	public Response updateCouponConsumptionId(Integer tariffId,Integer patientCouponTemplateMapperId, Integer orderDetailsId)
			throws ApplicationException {
		try {

			sessionFactory.getCurrentSession()
					.createSQLQuery("UPDATE t_order_details SET patient_coupon_template_mapper_id = :patientCouponTemplateMapperId, tariff_id = :tariffId WHERE order_details_id = :orderDetailsId")
					.setParameter("patientCouponTemplateMapperId", patientCouponTemplateMapperId)
					.setParameter("tariffId", tariffId)
					.setParameter("orderDetailsId", orderDetailsId)
					.executeUpdate();


			return new Response(SUCCESS,null,null,null,null);

		} catch (HibernateException he) {
			logger.error("updateCouponConsumptionId failed", he);
		} catch (Exception e) {
			logger.error("updateCouponConsumptionId failed", e);
		}
		return new Response(ERROR,null,null,null,null);
	}

	@Override
	public Response getOrderedMedication(BillingMasterDto dto) throws ApplicationException {

		try {
			
			if(dto.getAdmissionId()!= null){
				String query = " select "
						+"tod.order_id as \"orderId\" ," 
						+"tod.order_details_id as \"orderDetailsId\" ," 
						+"mi.name as \"itemName\", " 
						+"tb.batch_no as\"batchNo\" ," 
						+"to_char(tb.expiry_date, 'DD/MM/YYYY') as\"expiryDate\", " 
						+"tb.expiry_date as\"expiryDateNew\", " 
						+"tb.rate as\"rate\" ," 
						+"mi.code as\"itemCode\", " 
						+"to_char(tod.order_date, 'YYYY-MM-DD') as\"orderDateString\", "
						+"tod.order_date as\"orderDateStringNew\", "
						+"tod.order_qty as\"orderQty\", "
						+"tom.order_no as\"orderNo\","
						+ " muu.unit as \"uomName\" ," 	
						+ "tod.net_amount as \"netAmount\","
						+ "ti.id as \"issueId\" ,  "
						+ " tidbm.id as \"batchMapperId\", "
						+ "tid.id as \"issueDetailId\", "
						+ " coalesce(tidbm.return_accepted_quantity, 0)  as \"returnQuantity\" " 	
						+"from t_order_details  tod "
						+"inner join t_order_master tom ON  tom.order_id = tod.order_id "
						+"inner join inventory.t_batch tb  on tb.id  = tod.batch_id "
						+"inner join m_item mi  on mi.id = tb.item_id   "
						+ "left join pharmacy.t_sale_detail tsd  on tsd.id = tod.sale_detail_id " 
						+"left join m_uom_unit muu on muu.id = tsd.uom_unit_id "
						+ "inner join pharmacy.t_issue_detail tid on tid.sale_detail_id  = tsd.id "
						+ "inner join pharmacy.t_issue_detail_batch_mapper tidbm on tidbm.issue_detail_id  = tid.id "
						+ "inner join pharmacy.t_issue ti on ti.id  = tid.issue_id "
						+"where tom.admission_id = :admissionId and tod.order_qty >0 ";
		
		List<OrderDetailsMasterDto> listMedicationsMasterDto = sessionFactory.getCurrentSession()
				.createSQLQuery(query)
//				.setParameter("billId",dto.getBillingMasterId())
				.setParameter("admissionId",dto.getAdmissionId())
				.setResultTransformer(Transformers.aliasToBean(OrderDetailsMasterDto.class)).list();
		return new Response(SUCCESS, SUCCESS_CODE, null, listMedicationsMasterDto, null);
		
			}else if(dto.getEncounterId()!= null ){
				String query = " select "
						+"tod.order_id as \"orderId\" ," 
						+"tod.order_details_id as \"orderDetailsId\" ," 
						+"mi.name as \"itemName\", " 
						+"tb.batch_no as\"batchNo\" ," 
						+"to_char(tb.expiry_date, 'DD/MM/YYYY') as\"expiryDate\", " 
						+"tb.rate as\"rate\" ," 
						+"mi.code as\"itemCode\", " 
						+"to_char(tod.order_date, 'DD/MM/YYYY') as\"orderDateString\", "
						+"tod.order_qty as\"orderQty\", "
						+"tom.order_no as\"orderNo\","
						+ " muu.unit as \"uomName\" ," 	
						+ "tod.net_amount as \"netAmount\", "
						+ "ti.id as \"issueId\" ,  "
						+ " tidbm.id as \"batchMapperId\", "
						+ "tid.id as \"issueDetailId\", "
						+ "tidbm.return_accepted_quantity as \"returnQuantity\" " 	
						+"from t_order_details  tod "
						+"inner join t_order_master tom ON  tom.order_id = tod.order_id "
						+"inner join inventory.t_batch tb  on tb.id  = tod.batch_id "
						+"inner join m_item mi  on mi.id = tb.item_id   "
						+ "left join pharmacy.t_sale_detail tsd  on tsd.id = tod.sale_detail_id " 
						+"left join m_uom_unit muu on muu.id = tsd.uom_unit_id "
						+ "inner join pharmacy.t_issue_detail tid on tid.sale_detail_id  = tsd.id "
						+ "inner join pharmacy.t_issue_detail_batch_mapper tidbm on tidbm.issue_detail_id  = tid.id "
						+ "inner join pharmacy.t_issue ti on ti.id  = tid.issue_id "
						+"where ((tod.order_id in(select tod.order_id from billing.t_billing_details tbd " 
						+"inner join t_order_details  tod  on tbd.order_details_id = tod.order_details_id  "
						+"where tbd.billing_master_id  = :billId ) and tod.service_is_billed = 1) OR (tom.encounter_id = :encounterId and  tod.service_is_billed =0)) and tod.order_qty >0 ";
		
		List<OrderDetailsMasterDto> listMedicationsMasterDto = sessionFactory.getCurrentSession()
				.createSQLQuery(query)
				.setParameter("billId",dto.getBillingMasterId())
				.setParameter("encounterId",dto.getAdmissionId())
				.setResultTransformer(Transformers.aliasToBean(OrderDetailsMasterDto.class)).list();
		return new Response(SUCCESS, SUCCESS_CODE, null, listMedicationsMasterDto, null);
			}
			
			
		

		} catch (HibernateException he) {
			logger.error("getOrderedMedication failed", he);
		} catch (Exception e) {
			logger.error("getOrderedMedication failed", e);
		}
		return new Response(ERROR, null, null, null, null);

	}

	@Override
	public Response getVirtualStoreId(BillingMasterDto dto) throws ApplicationException {
try {

			String query = "select "
					+ " 	appn.virtual_store_id as \"virtualStoreId\" "
					+ " from "
					+ "	m_application_configuration appn"
					+ " where appn.unit_id  =:unitId "
					+ " and appn.is_active = true ";

			List<ApplicationConfigurationDto> list = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("unitId",dto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(ApplicationConfigurationDto.class)).list();

			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
			logger.error("getVirtualStoreId failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getPackageConsumeAmount(Integer orderDetailsId) throws ApplicationException {

		try {
			
			String query = "select package_consumption_amt as \"packageConsumptionAmt\" from t_order_details tod  where order_details_id =:orderDetailsId ";
			
			OrderDetailsMasterDto obj = (OrderDetailsMasterDto)sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("orderDetailsId",orderDetailsId)
					.setResultTransformer(Transformers.aliasToBean(OrderDetailsMasterDto.class)).uniqueResult();
			
			return new Response(SUCCESS, SUCCESS_CODE, null, null, obj);
		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
			logger.error("getPackageConsumeAmount failed", e);
		}

		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	

}

