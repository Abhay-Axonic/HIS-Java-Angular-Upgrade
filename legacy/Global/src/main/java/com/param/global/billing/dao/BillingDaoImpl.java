package com.param.global.billing.dao;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.billing.global.CostEstimateDto;
import com.param.billing.global.CostEstimationDetailsDto;
import com.param.billing.global.model.CostEstimate;
import com.param.billing.global.model.CostEstimateDetails;
import com.param.billing.global.model.CostEstimateDetailsPayeeMapper;
import com.param.billing.global.transaction.model.BillDetailsPayeeMapper;
import com.param.billing.global.transaction.model.BillPayeeDetails;
import com.param.billing.global.transaction.model.BillingDetails;
import com.param.billing.global.transaction.model.BillingMaster;
import com.param.billing.global.transaction.model.BillingStatusMapper;
import com.param.entity.model.pharmacy.TempPatientRegistration;
import com.param.global.billing.controller.BillingException;
import com.param.global.billing.dto.AdmissionListIpdChargesResDto;
import com.param.global.billing.dto.BillPayeeDetailsResDto;
import com.param.global.billing.dto.BillingDetailsDto;
import com.param.global.billing.dto.BillingStatusMapperDto;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.AdmissionsForBatchProcessDto;
import com.param.global.dto.BillPayeeDetailsDto;
import com.param.global.dto.BillingMasterDto;
import com.param.global.dto.OrdeDetailsPackageMapperLogDto;
import com.param.global.dto.OrderDetailsPackageMapperDto;
import com.param.global.dto.OrderDetailsPayeeMapperDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
@SuppressWarnings({ "rawtypes", "unchecked"})
@Repository
public class BillingDaoImpl extends GenericDao<BillingMaster, Integer> implements IBillingDao,ICommonConstants
{

	final static Logger logger = Logger.getLogger(BillingDaoImpl.class);

	public BillingDaoImpl() {
		super(BillingMaster.class);
	}

	@Autowired
	private DozerBeanMapper mapper;
	
//	@Autowired
//	private IPatientRegistrationDao iPatientRegistrationDao;
	
	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	public BillingMaster getBillMasterObject(Integer billId) {
		try{
			BillingMaster billingMaster = findById(billId);
			return billingMaster;
		}catch(Exception e){
			logger.error("getBillMasterObject failed", e);
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	public Response updateBillMaster(BillingMaster billingMasterObj) {
		try {
			
				billingMasterObj.setCreatedDate(TimeZoneComponent.getDateByzone(billingMasterObj.getUnitId()).getTime());
				billingMasterObj.setUpdatedDate(TimeZoneComponent.getDateByzone(billingMasterObj.getUnitId()).getTime());
			save(billingMasterObj);
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		} catch (HibernateException he) {
			logger.error("updateBillMaster failed", he);
			throw new BillingException(he.getMessage());
		} catch (Exception e) {
			logger.error("updateBillMaster failed", e);
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	public Response saveBillingDetails(BillingDetailsDto billingDetailsDto) {
		try {
			BillingDetails billingDetails = mapper.map(billingDetailsDto, BillingDetails.class,"BillingDetailsDto_to_BillingDetails");
				billingDetails.setCreatedDate(TimeZoneComponent.getDateByzone(billingDetailsDto.getUnitId()).getTime());
				billingDetails.setUpdatedDate(TimeZoneComponent.getDateByzone(billingDetailsDto.getUnitId()).getTime());
			int billOrdId = (int) sessionFactory.getCurrentSession().save(billingDetails);
			
			for(OrderDetailsPayeeMapperDto dto : billingDetailsDto.getOrderDetailsPayeeMapperDtosList())
			{
				BillDetailsPayeeMapper detailsPayeeMapper = new BillDetailsPayeeMapper();
					detailsPayeeMapper.setOrderDetailsId(billingDetailsDto.getOrderDetailsId());
					detailsPayeeMapper.setBillDetailsId(billOrdId);
					detailsPayeeMapper.setContractId(dto.getContractId());
					detailsPayeeMapper.setPayeeId(dto.getPayeeId());
					detailsPayeeMapper.setAssociateCompanyId(dto.getAssociateCompanyId());
					detailsPayeeMapper.setPayeeAmount(dto.getPayeeAmount());
					detailsPayeeMapper.setDueAmount(dto.getPayeeAmount());
					detailsPayeeMapper.setServiceId(dto.getServiceId());
					detailsPayeeMapper.setOrganizationId(billingDetailsDto.getOrganizationId());
					detailsPayeeMapper.setUnitId(billingDetailsDto.getUnitId());
					detailsPayeeMapper.setStatus('A');
				sessionFactory.getCurrentSession().save(detailsPayeeMapper);	
			}
			
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, billingDetails);
		} catch (HibernateException he) {
			logger.error("saveBillingDetails failed", he);
			throw new BillingException(he.getMessage());
		} catch (Exception e) {
			logger.error("saveBillingDetails failed", e);
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	public Response getBillPayeeDetailsByBillId(Integer billId) {
		try {
			List<BillPayeeDetailsResDto> listBillPayeeDetailsResDto = sessionFactory.getCurrentSession().getNamedQuery("GET_PAYEE_DETAILS_BY_BILL_ID").setParameter("billId", billId).setResultTransformer(Transformers.aliasToBean(BillPayeeDetailsResDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listBillPayeeDetailsResDto, null);
		} catch (Exception e) { 
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	public int updatePayeeDetailsByPayeeAndBillId(OrderDetailsPayeeMapperDto detailsPayeeMapperDto,Integer billId,Integer unitId) {
		try {


			String query = "update billing.t_bill_payee_details "
					+ " set due_amount = due_amount + :dueAmount, "
					+ " bill_amount = bill_amount + :dueAmount, "
					+ " updated_date = :updatedDate "
					+ " where bill_id = :billId and payee_id = :payeeId and status = 'A'";

			if(detailsPayeeMapperDto.getAssociateCompanyId()!=null && detailsPayeeMapperDto.getAssociateCompanyId()>0)
				query = query.concat(" and associate_company_id =:associateCompanyId  ");


			Query SQL = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setDouble("dueAmount", detailsPayeeMapperDto.getPayeeAmount().doubleValue())
					.setParameter("billId", billId)
					.setParameter("payeeId", detailsPayeeMapperDto.getPayeeId())
					.setParameter("updatedDate", TimeZoneComponent.getDateByzone(unitId));



			if(detailsPayeeMapperDto.getAssociateCompanyId()!=null && detailsPayeeMapperDto.getAssociateCompanyId()>0)
				SQL.setParameter("associateCompanyId",detailsPayeeMapperDto.getAssociateCompanyId());

			int i =SQL.executeUpdate();

			return i;
		} catch (Exception e) { 
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	public Response saveBillPayeeDetails(BillPayeeDetailsDto billPayeeDetailsDto) {
		try {
			BillPayeeDetails billPayeeDetails = mapper.map(billPayeeDetailsDto, BillPayeeDetails.class, "BillPayeeDetailsDto_to_BillPayeeDetails");
			sessionFactory.getCurrentSession().save(billPayeeDetails);
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		}catch (HibernateException he) {
			logger.error("saveBillPayeeDetails failed", he);
			throw new BillingException(he.getMessage());
		} catch (Exception e) {
			logger.error("saveBillPayeeDetails failed", e);
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	public Response saveBillStatus(BillingStatusMapperDto mapperDto) {
		try{
			Date today = TimeZoneComponent.getDateByzone(mapperDto.getUnitId());
			BillingStatusMapper mapper = new BillingStatusMapper();
				mapper.setBillingStatusMapperId(mapperDto.getBillingStatusMapperId());
				mapper.setCreatedBy(mapperDto.getCreatedBy());
				mapper.setCreatedDate(today);
				mapper.setDateTime(today);
				mapper.setOrgnisationId(mapperDto.getOrgnisationId());
				mapper.setUnitId(mapperDto.getUnitId());
				mapper.setUpdatedBy(mapperDto.getUpdatedBy());
				mapper.setUpdatedDate(null);
				mapper.setStatus(mapperDto.getStatus());
			sessionFactory.getCurrentSession().saveOrUpdate(mapper);
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	public AdmissionsForBatchProcessDto getBillByEncounterId(Integer encounterId) {
		try {
			String query = "select " 
					 + " 	bill.billing_master_id as \"billId\", " 
					 + " 	bill.bill_number as \"billNumber\", "
					 + " 	enc.payment_entitlement_type_id as \"paymentEntitlementId\", " 
					 + " 	patcat.patient_category_id as \"patientTypeId\", " 
					 + " 	uabc.opd_billing_cat_id as \"billingBedCategoryId\", "
					 + "	referal.referral_id as \"referralId\", "
					 + "    tbpd.is_invoice_generated as \"isInvoiceGenerated\" " 
					 + " from " 
					 + " 	opd.t_encounter_master enc " 
					 + " 	inner join patient.t_patient_registration pat on "
					 + " 		enc.patient_id = pat.patient_id "
					 + " 	inner join m_patient_category_master patcat on "
					 + " 		patcat.patient_category_id = pat.patient_category_id "
					 + " 	inner join t_unit_application_billing_configuration uabc on "
					 + " 		uabc.unit_id = enc.unit_id "
					 + " 	left join t_referal_details referal on "
					 + " 		referal.encounter_id = enc.encounter_id	 "
					 + " 	left join billing.m_billing_master bill on "
					 + " 		bill.encounter_id = enc.encounter_id and is_dr_note = 'N' and bill_cancelled = 'N' "
					 + "    left join billing.t_bill_payee_details tbpd on tbpd.bill_id = bill.billing_master_id and tbpd.priority_id = 1 and tbpd.status = 'A'"
					 + " where " 
					 + " 	enc.encounter_id =:encounterId " 
					 + " 	and bill.is_dr_note = 'N' " 
					 + " 	and bill.bill_cancelled = 'N' " 
					 + " order by " 
					 + " 	bill.billing_master_id desc " 
					 + " limit 1 " ;
			AdmissionsForBatchProcessDto dto = (AdmissionsForBatchProcessDto) sessionFactory.getCurrentSession()
					.createSQLQuery(query).setParameter("encounterId", encounterId)
					.setResultTransformer(Transformers.aliasToBean(AdmissionsForBatchProcessDto.class)).uniqueResult();
			return dto;
		} catch (Exception e) {
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	public AdmissionsForBatchProcessDto getBillDataByAdmissionId(Integer admissionId) {
		try {
			String query = "select "
					+ " 	bill.billing_master_id as \"billId\", "
					+ " 	bill.bill_number as \"billNumber\", "
					+ " 	adm.payment_entitlement_id as \"paymentEntitlementId\", "
					+ " 	patcat.patient_category_id as \"patientTypeId\", "
					+ " 	uabc.opd_billing_cat_id as \"billingBedCategoryId\", "
					+ "	 referal.referral_id as \"referralId\", "
				    + "    tbpd.is_invoice_generated as \"isInvoiceGenerated\" " 
					+ " from "
					+ " 	adt.t_admission adm "
					+ " 	inner join patient.t_patient_registration pat on "
					+ " 		adm.patient_id = pat.patient_id "
					+ " 	inner join m_patient_category_master patcat on "
					+ " 		patcat.patient_category_id = pat.patient_category_id "
					+ " 	inner join t_unit_application_billing_configuration uabc on "
					+ " 		uabc.unit_id = adm.unit_id "
					+ " 	left join t_referal_details referal on "
					+ " 		referal.admission_id = adm.admission_id "
					+ " 	left join billing.m_billing_master bill on "
					+ " 		bill.admission_id = adm.admission_id and is_dr_note = 'N' and bill_cancelled = 'N' "
					 + "    left join billing.t_bill_payee_details tbpd on tbpd.bill_id = bill.billing_master_id and tbpd.priority_id = 1 and tbpd.status = 'A'"
					+ " where "
					+ " 	adm.admission_id =:admissionId "
					+ " 	and bill.is_dr_note = 'N' "
					+ " 	and bill.bill_cancelled = 'N' "
					+ " order by "
					+ " 	bill.billing_master_id desc "
					+ " limit 1";
			AdmissionsForBatchProcessDto dto = (AdmissionsForBatchProcessDto) sessionFactory.getCurrentSession()
					.createSQLQuery(query).setParameter("admissionId", admissionId)
					.setResultTransformer(Transformers.aliasToBean(AdmissionsForBatchProcessDto.class)).uniqueResult();
			return dto;
		} catch (Exception e) {
			
			throw new BillingException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public Response saveEstimate(CostEstimateDto costEstimationDto) {
		try {
			if (null == costEstimationDto.getPatientId()) {
				CostEstimate costEstimate = new CostEstimate();
				costEstimate.setPatientId(costEstimationDto.getPatientId());
				costEstimate.setBillingBedCategoryId(costEstimationDto.getBillingBedCategoryId());
				costEstimate.setVisitTypeId(costEstimationDto.getVisitTypeId());
				costEstimate.setPatietCategoryId(costEstimationDto.getPatietCategoryId());
				costEstimate.setUnitId(costEstimationDto.getUnitId());
				costEstimate.setOrgId(costEstimationDto.getOrgId());
				if (costEstimationDto.getDoa() != null)
					costEstimate
							.setDoa(GlobalCommonDateUtils.getDate(costEstimationDto.getDoa(), "yyyy-MM-dd HH:mm:ss"));
				if (costEstimationDto.getPdd() != null)
					costEstimate
							.setPdd(GlobalCommonDateUtils.getDate(costEstimationDto.getPdd(), "yyyy-MM-dd HH:mm:ss"));
				costEstimate.setTotalAmount(costEstimationDto.getTotalAmount());
				costEstimate.setSelfPayable(costEstimationDto.getSelfPayable());
				costEstimate.setCreditPayable(costEstimationDto.getCreditPayable());
				costEstimate.setCreatedBy(costEstimationDto.getCreatedBy());
				costEstimate.setCreatedDate(TimeZoneComponent.getDateByzone(costEstimationDto.getUnitId()));
				costEstimate.setStatus('A');
				costEstimate.setDiscountAmount(
						costEstimationDto.getDiscountAmount() != null ? costEstimationDto.getDiscountAmount() : 0);
				costEstimate.setRemark(costEstimationDto.getRemark() != null ? costEstimationDto.getRemark() : " ");
				Integer id = (Integer) sessionFactory.getCurrentSession().save(costEstimate);

				return new Response<>(SUCCESS, SUCCESS_CODE, null, null, id);
			}else {
			CostEstimate costEstimate = new CostEstimate();
				costEstimate.setPatientId(costEstimationDto.getPatientId());
				costEstimate.setBillingBedCategoryId(costEstimationDto.getBillingBedCategoryId());
				costEstimate.setVisitTypeId(costEstimationDto.getVisitTypeId());
				costEstimate.setPatietCategoryId(costEstimationDto.getPatietCategoryId());
				costEstimate.setUnitId(costEstimationDto.getUnitId());
				costEstimate.setOrgId(costEstimationDto.getOrgId());
				if(costEstimationDto.getDoa()!=null)
					costEstimate.setDoa(GlobalCommonDateUtils.getDate(costEstimationDto.getDoa(),"yyyy-MM-dd HH:mm:ss"));
				if(costEstimationDto.getPdd()!=null)
					costEstimate.setPdd(GlobalCommonDateUtils.getDate(costEstimationDto.getPdd(),"yyyy-MM-dd HH:mm:ss"));
				costEstimate.setTotalAmount(costEstimationDto.getTotalAmount());
				costEstimate.setSelfPayable(costEstimationDto.getSelfPayable());
				costEstimate.setCreditPayable(costEstimationDto.getCreditPayable());
				costEstimate.setCreatedBy(costEstimationDto.getCreatedBy());
				costEstimate.setCreatedDate(TimeZoneComponent.getDateByzone(costEstimationDto.getUnitId()));
				costEstimate.setStatus('A');
				costEstimate.setDiscountAmount(costEstimationDto.getDiscountAmount()!= null ? costEstimationDto.getDiscountAmount() : 0);
				costEstimate.setRemark(costEstimationDto.getRemark() != null ? costEstimationDto.getRemark() : " " );
			Integer id = (Integer)sessionFactory.getCurrentSession().save(costEstimate);
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, id);
			}
		} catch (Exception e) {
			
			throw new BillingException(e.getMessage());
		}
	}
	
	
	@Override
	public Response saveEstimateDetails(CostEstimateDto costEstimationDto) {
		try {
			
			for(CostEstimationDetailsDto detailsDto : costEstimationDto.getCostEstimationDetailsDtosList())
			{
				CostEstimateDetails costEstimate = new CostEstimateDetails();
				costEstimate.setCostEstimationId(costEstimationDto.getCostEstimateId());
				costEstimate.setServiceSpecialityId(detailsDto.getServiceSpecialityId());
				costEstimate.setServiceSubSpecialityId(detailsDto.getServiceSubSpecialityId());
				costEstimate.setServiceMasterId(detailsDto.getServiceMasterId());
				costEstimate.setQuantity(detailsDto.getQuantity());
				costEstimate.setTotalAmt(detailsDto.getTotalAmt());
				costEstimate.setSelfPayable(detailsDto.getSelfPayable());
				costEstimate.setCreditPayable(detailsDto.getCreditPayable());
				costEstimate.setCreatedBy(costEstimationDto.getCreatedBy());
				costEstimate.setCreatedDate(TimeZoneComponent.getDateByzone(0));
				costEstimate.setBillingBedCategoryId(detailsDto.getBillingBedCategoryId()!= null ? detailsDto.getBillingBedCategoryId() : null);
				costEstimate.setStatus('A');
				costEstimate.setTariffId(detailsDto.getTariffId()!= null ? detailsDto.getTariffId() : null);
				Integer id = (Integer)sessionFactory.getCurrentSession().save(costEstimate);
				detailsDto.setCostEstimationDetailsId(id);
				
				for(OrderDetailsPayeeMapperDto payeeDto : detailsDto.getOrderDetailsPayeeMapperDtosList())
				{
					CostEstimateDetailsPayeeMapper costEstimateDetailsPayeeMapper = new CostEstimateDetailsPayeeMapper();
						costEstimateDetailsPayeeMapper.setCostEstimationDetailsId(id);
						costEstimateDetailsPayeeMapper.setContractId(payeeDto.getContractId());
						costEstimateDetailsPayeeMapper.setPayeeAmount(payeeDto.getPayeeAmount());
						costEstimateDetailsPayeeMapper.setPayeeId(payeeDto.getPayeeId());
						costEstimateDetailsPayeeMapper.setServiceId(payeeDto.getServiceId());
						costEstimateDetailsPayeeMapper.setStatus('A');
					sessionFactory.getCurrentSession().save(costEstimateDetailsPayeeMapper);
				}
				
				
			}
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, costEstimationDto.getCostEstimationDetailsDtosList(), null);
		} catch (Exception e) {
			
			throw new BillingException(e.getMessage());
		}
	}
	
	@Override
	public AdmissionListIpdChargesResDto getAdmissionsById(int admissionId) throws BillingException {
		try {
			AdmissionListIpdChargesResDto dto = (AdmissionListIpdChargesResDto) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ADMISSION_LIST_FOR_IPD_CHARGES_BY_PATIENT")
					.setParameter("admissionId", admissionId)
					.setResultTransformer(Transformers.aliasToBean(AdmissionListIpdChargesResDto.class)).uniqueResult();
			return dto;
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response updateLabSampleStatus(Integer labSampleDetailsId) throws BillingException {
		try{	
			Integer count = sessionFactory.getCurrentSession()
					.createSQLQuery("UPDATE lab.t_lab_sample_dtls SET sample_status_id = 20 WHERE lab_sample_dtls_id = :labSampleDetailsId")
					.setParameter("labSampleDetailsId", labSampleDetailsId)
					.executeUpdate();

			return new Response(SUCCESS, null, "Accepted Successfully", null, null);

		}catch (HibernateException he) {
			logger.error("updateLabSampleStatus failed", he);
			throw new BillingException(he.getMessage());
		} catch (Exception e) {
			logger.error("updateLabSampleStatus failed", e);
			throw new BillingException(e.getMessage());
		}
	}

	public Response updateAdmissionIsFinanceFlag(BillingMasterDto billingMasterDto) throws BillingException {

      try{
		  Integer count = sessionFactory.getCurrentSession()
				  .createSQLQuery("UPDATE adt.t_admission SET is_financial_creance = 'Y' WHERE admission_id = :admissionId")
				  .setParameter("admissionId", billingMasterDto.getAdmissionId())
				  .executeUpdate();
			return new Response(SUCCESS, null, null, null, null);

		}catch (HibernateException he) {
			logger.error("updateAdmissionIsFinanceFlag failed", he);
			throw new BillingException(he.getMessage());
		} catch (Exception e) {
			logger.error("updateAdmissionIsFinanceFlag failed", e);
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	public Response changeBillStatus(Integer billId) {
		try{
				sessionFactory.getCurrentSession().createSQLQuery(" update billing.m_billing_master set bill_status_id = 2 "
					+" where billing_master_id="+billId)
					.executeUpdate();
			return new Response(SUCCESS, null, null, null, null);

		}catch (HibernateException he) {
			logger.error("changeBillStatus failed", he);
			throw new BillingException(he.getMessage());
		} catch (Exception e) {
			logger.error("changeBillStatus failed", e);
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	public Response getPaymentEntitlement(Integer billingMasterId) {

		String sql = "select tbpd.payment_entitlement_id as \"paymentEntitlementId\" from billing.m_billing_master mbm " +
				"left join billing.t_bill_payee_details tbpd " +
				"on tbpd.bill_id = mbm.billing_master_id " +
				"where tbpd.priority_id = 1 and tbpd.status = 'A' and mbm.billing_master_id = :billingMasterId";

		BillPayeeDetailsDto paymentEntitlement = (BillPayeeDetailsDto) sessionFactory.getCurrentSession()
				.createSQLQuery(sql)
				.setParameter("billingMasterId", billingMasterId)
				.setResultTransformer(Transformers.aliasToBean(BillPayeeDetailsDto.class))
				.uniqueResult();
		return new Response(SUCCESS, null, null, null, paymentEntitlement);
		
	}

	public Response getPayeeDetailsByPayeeAndBillId(Integer billId) throws BillingException {
		try {
			String query ="select bill_payee_details_id as \"billPayeeDetailsId\","
					+ "		payee_id as \"payeeId\", "
					+ "		associate_company_id as \"associateCompanyId\" "
					+ " from billing.t_bill_payee_details "
					+ "		where bill_id =:billId and status='A' ";
			List<BillPayeeDetailsDto> list  =  sessionFactory.getCurrentSession()
					.createSQLQuery(query).setParameter("billId", billId)
					.setResultTransformer(Transformers.aliasToBean(BillPayeeDetailsDto.class)).list();
			
			return new Response(SUCCESS, null, null, list, null);
		} catch (Exception e) {
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	public Response updatePayeePriority(BillPayeeDetailsDto billPayeeDetailsDto) {
		try {

			String query = "update billing.t_bill_payee_details " 
						+ " set priority_id = :priorityId, "
						+ "	payment_entitlement_id=:paymentEntitlementId "
						+ " where bill_payee_details_id = :billPayeeDetailsId ";

			Integer i = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("billPayeeDetailsId", billPayeeDetailsDto.getBillPayeeDetailsId())
					.setParameter("priorityId", billPayeeDetailsDto.getPriorityId())
					.setParameter("paymentEntitlementId", billPayeeDetailsDto.getPaymentEntitlementId())
					.executeUpdate();
			return new Response(SUCCESS, null, null, null, i);
		} catch (Exception e) {
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public TempPatientRegistration savePatient(TempPatientRegistration tempPatientRegistration) {
		getSession().save(tempPatientRegistration);
		return tempPatientRegistration;
	}

	@Override
	@Transactional
	public Response updateAdmissionsIsFinancialClearanceN(BillingMasterDto billingMasterDto) {
		try {
			
			String query = "select admission_id as \"admissionId\" from billing.m_billing_master mbm where billing_master_id =:billId "
					+ " limit 1";
			BillingMasterDto dto = (BillingMasterDto) sessionFactory.getCurrentSession()
					.createSQLQuery(query).setParameter("billId", billingMasterDto.getBillingMasterId())
					.setResultTransformer(Transformers.aliasToBean(BillingMasterDto.class)).uniqueResult();
			if(dto != null && dto.getAdmissionId() != null){
				sessionFactory.getCurrentSession()
				.createQuery(" UPDATE Admission set isFinancialCreance='N' "
						+    " WHERE admissionId=:admissionId ")
				.setParameter("admissionId", dto.getAdmissionId())
				.executeUpdate();
			}
			
			
			return new Response(SUCCESS, null, null, null, null);
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response getPackageConsumeAmountItemwise(Long id, Integer orderDetailsId) throws BillingException {

		try {
			
			if(orderDetailsId == null) {

				String qry = "select is_package_service as \"isPackageService\", package_consumption_amt \"packageAmount\", order_qty \"packageQty\", force_add_removed as \"forceAddRemoved\" " +
						"from t_order_details tod where sale_detail_id = :id";

				List<OrderDetailsPackageMapperDto> list = sessionFactory.getCurrentSession()
						.createSQLQuery(qry)
						.setParameter("id", id)
						.setResultTransformer(Transformers.aliasToBean(OrderDetailsPackageMapperDto.class))
						.list();
				return new Response(SUCCESS, null, null, list, null);
				
				
			}else {

				String qry = "select is_package_service as \"isPackageService\", package_consumption_amt \"packageAmount\", order_qty \"packageQty\", force_add_removed as \"forceAddRemoved\" " +
						"from t_order_details tod where sale_detail_id = :id and order_details_id < :orderDetailsId";

				List<OrderDetailsPackageMapperDto> list = sessionFactory.getCurrentSession()
						.createSQLQuery(qry)
						.setParameter("id", id)
						.setParameter("orderDetailsId", orderDetailsId)
						.setResultTransformer(Transformers.aliasToBean(OrderDetailsPackageMapperDto.class))
						.list();
				return new Response(SUCCESS, null, null, list, null);
			}
			
			
		} catch (Exception e) {
			throw e;
		}

	}

	
}
