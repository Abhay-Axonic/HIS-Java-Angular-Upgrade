	package com.param.global.billing.dao;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.actionmaster.controller.HibernateUtil;
import com.param.billing.global.model.DepositMaster;
import com.param.billing.global.model.PaymentDepositDetails;
import com.param.global.billing.controller.BillingException;
import com.param.global.billing.dto.DepositDetailsByDepositIdDto;
import com.param.global.billing.dto.DepositMasterDto;
import com.param.global.billing.dto.DepositSearchReqDto;
import com.param.global.billing.dto.DepositSearchResDto;
import com.param.global.billing.dto.PaymentDepositDetailsDto;
import com.param.global.billing.dto.PaymentDepositDto;
import com.param.global.billing.dto.VisitTypeEncounterReqDto;
import com.param.global.billing.dto.VisitTypeEncounterResDto;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.AdmissionNoteDto;
import com.param.global.mpesa.dto.CCAvenueLogDto;
import com.param.global.service.OrderMasterServiceImpl;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class DepositMasterDaoImpl extends GenericDao<DepositMaster, Integer> implements IDepositMasterDao, ICommonConstants{

	final static Logger logger = Logger.getLogger(DepositMasterDaoImpl.class);

	public DepositMasterDaoImpl() {
		super(DepositMaster.class);
	}

	@Override
	public Response saveDeposit(DepositMasterDto dto) throws BillingException {
		try{
			DepositMaster master = new DepositMaster();
				master.setAdmNoteId(dto.getAdmNoteId());
				master.setAvailableDeposit(new Double(OrderMasterServiceImpl.df2.format(dto.getAvailableDeposit())));
				master.setConsumedDeposit(dto.getConsumedDeposit());
				master.setCreatedBy(dto.getCreatedBy());
				master.setCreatedDate(TimeZoneComponent.getDateByzone(dto.getUnitId()));
				master.setDepositAmount(new Double(OrderMasterServiceImpl.df2.format(dto.getDepositAmount())));
				master.setDepositDate(TimeZoneComponent.getDateByzone(dto.getUnitId()));
				master.setDepositNo(dto.getDepositNo());
				master.setDepositTypeId(dto.getDepositTypeId());
				master.setIdentificationNo(dto.getIdentificationNo());
				master.setOrgId(dto.getOrgId());
				master.setPatientId(dto.getPatientId());
				master.setPayeeId(dto.getPayeeId());
				master.setReceivedFrom(dto.getReceivedFrom());
				master.setRemarks(dto.getRemarks());
				master.setStatus(ACTIVE);
				master.setUnitId(dto.getUnitId());
				master.setUpdatedBy(dto.getUpdatedBy());
				master.setUpdatedDate(TimeZoneComponent.getDateByzone(dto.getUnitId()));
				master.setAdmissionId(dto.getAdmissionId());
				master.setEncounterId(dto.getEncounterId());
				master.setOtReservationId(dto.getOtReservationId()!=null?dto.getOtReservationId():null);
				master.setVisitTypeId(dto.getVisitTypeId());
				master.setRefundAmount(0.00);
				master.setCounterId(dto.getCounterId());
				master.setIsAddendum(dto.getIsAddendum());
				master.setBillId(dto.getBillId());
				master.setAgainstPaymentRecieptId(dto.getAgainstPaymentRecieptId());
				master.setSignetureImage(dto.getSignetureImage());
				master.setSignature(dto.getSignature());
			master = save(master);
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, master);
		}catch(HibernateException he){
			throw new BillingException(he.getMessage());
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	public Long getCurrentId() throws BillingException {
		try{
			BigInteger id = (BigInteger)sessionFactory.getCurrentSession().getNamedQuery("GET_CURRNET_DEPOSIT_ID").uniqueResult();
			return id.longValue();
		}catch(HibernateException he){
			throw new BillingException(he.getMessage());
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	public Response searchDeposit(DepositSearchReqDto dto) throws BillingException {
		try{
			Integer count = 0;
			StringBuilder query = new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("DEPOSIT_DETAILS_SEARCH").getQueryString());
			
			if(dto.getFromDate() == null || dto.getToDate() == null || dto.getFromDate().isEmpty() || dto.getToDate().isEmpty())
			{
				dto.setFromDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "yyyy-MM-dd"));
				dto.setToDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "yyyy-MM-dd"));
			}
			if(dto.getFromDeposit() == null || !dto.getFromDeposit()){
				query.append(" and dept.status = 'A' ");
			}
			query.append((dto.getFromDate() != null && !dto.getFromDate().isEmpty()) ? " and to_char(dept.deposit_date,'YYYY-MM-DD') >='"+dto.getFromDate()+"'":"");
			query.append((dto.getToDate() != null && !dto.getToDate().isEmpty()) ? " and to_char(dept.deposit_date,'YYYY-MM-DD') <='"+dto.getToDate()+"'":"");
			query.append((dto.getPatientName() != null && !dto.getPatientName().isEmpty()) ? " and ( trim(lower(pat.first_name)) like '%"+dto.getPatientName().trim().toLowerCase()+"%' or trim(lower(pat.middle_name)) like '%"+dto.getPatientName().trim().toLowerCase()+"%' or trim(lower(pat.last_name)) like '%"+dto.getPatientName().trim().toLowerCase()+"%' ) " : " ");
			query.append((dto.getUhidNumber() != null && !dto.getUhidNumber().isEmpty()) ? " and trim(lower(pat.uhid_number)) like '%"+dto.getUhidNumber().trim().toLowerCase()+"%' " : " ");
			query.append(dto.getDepositTypeId() != null ? " and dept.deposit_type_id = "+dto.getDepositTypeId() : " and dept.deposit_type_id not in(4) ");
			query.append(dto.getVisitTypeId() != null ? " and dept.visit_type_id = " + dto.getVisitTypeId() : " ");
			query.append(dto.getAdmissionNoteId() != null && dto.getAdmissionNoteId()!=0 ? " and dept.adm_note_id = " + dto.getAdmissionNoteId() : " ");
			query.append(dto.getAdmissionId() != null && dto.getAdmissionId()!=0 ? "  and dept.admission_id = " + dto.getAdmissionId() : " ");
			query.append(" order by dept.deposit_date desc ");
			 
			StringBuilder queryForTotalCount = query;
			List<DepositSearchResDto> allListDepositSearchResDto = sessionFactory.getCurrentSession().createSQLQuery(queryForTotalCount.toString()).setParameter("orgId", dto.getOrgId()).setParameter("unitId", dto.getUnitId()).setResultTransformer(Transformers.aliasToBean(DepositSearchResDto.class)).list();
			count = allListDepositSearchResDto.size();
			
			query.append( (dto.getOffset()!=null && dto.getNoOfRecordsPerPage()!=null) ? " LIMIT " + dto.getNoOfRecordsPerPage() + " OFFSET " + dto.getOffset() : " " );
			
			List<DepositSearchResDto> listDepositSearchResDto = sessionFactory.getCurrentSession().createSQLQuery(query.toString()).setParameter("orgId", dto.getOrgId()).setParameter("unitId", dto.getUnitId()).setResultTransformer(Transformers.aliasToBean(DepositSearchResDto.class)).list();
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listDepositSearchResDto, count);
		}catch(Exception e){
			
			throw e;
		}
	}
	

	@Override
	public Response getEncountersByPatIdForDeposit(VisitTypeEncounterReqDto reqDto) throws BillingException {
		try{
			List<VisitTypeEncounterResDto> listVisitTypeEncounterResDto = sessionFactory.getCurrentSession().getNamedQuery("GET_ENC_VISIT_NOS_BY_PATIENT_ID_FOR_DEPOSIT").setParameter("patientId", reqDto.getPatientId()).setParameter("orgId", reqDto.getOrgId()).setParameter("unitId", reqDto.getUnitId()).setResultTransformer(Transformers.aliasToBean(VisitTypeEncounterResDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listVisitTypeEncounterResDto, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getAdmissionByPatVisitForDeposit(VisitTypeEncounterReqDto reqDto) throws BillingException {
		try{
			List<VisitTypeEncounterResDto> listVisitTypeEncounterResDto = sessionFactory.getCurrentSession().getNamedQuery("GET_ADM_VISIT_NOS_BY_PATIENT_ID_FOR_DEPOSIT").setParameter("visitTypeId", reqDto.getVisitTypeId()).setParameter("patientId", reqDto.getPatientId()).setParameter("orgId", reqDto.getOrgId()).setParameter("unitId", reqDto.getUnitId()).setResultTransformer(Transformers.aliasToBean(VisitTypeEncounterResDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listVisitTypeEncounterResDto, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getAvailableDepositByPatient(int orgId, int unitId, int patientId,int encounterId,int vistTypeId,int otReservFlag, Boolean depositType) throws BillingException {
		try{
			
			Boolean depositT = true;
			if(depositType != null && depositType.equals(depositT)) {
				
				List<DepositSearchResDto> listDepositSearchResDto = sessionFactory.getCurrentSession().getNamedQuery("GET_DEPT_WISE_DEPOSIT_BY_PATIENT")
						.setParameter("orgId", orgId)
						.setParameter("unitId", unitId)
						.setParameter("patientId", patientId)
						.setResultTransformer(Transformers.aliasToBean(DepositSearchResDto.class)).list();
				return new Response<>(SUCCESS, SUCCESS_CODE, null, listDepositSearchResDto, null);
				
			}else {
				if(otReservFlag == 1) {
					List<DepositSearchResDto> listDepositSearchResDto = sessionFactory.getCurrentSession().getNamedQuery("GET_AVAILABLE_DEPOSIT_BY_PATIENT_BY_OT_RESERVATION")
							.setParameter("orgId", orgId)
							.setParameter("unitId", unitId)
							.setParameter("patientId", patientId)
						//	.setParameter("encounterId", encounterId)
							//.setParameter("vistTypeId", vistTypeId)
							.setResultTransformer(Transformers.aliasToBean(DepositSearchResDto.class)).list();
					return new Response<>(SUCCESS, SUCCESS_CODE, null, listDepositSearchResDto, null);
				}else {
					List<DepositSearchResDto> listDepositSearchResDto = sessionFactory.getCurrentSession().getNamedQuery("GET_AVAILABLE_DEPOSIT_BY_PATIENT")
							.setParameter("orgId", orgId)
							.setParameter("unitId", unitId)
							.setParameter("patientId", patientId)
						//	.setParameter("encounterId", encounterId)
							//.setParameter("vistTypeId", vistTypeId)
							.setResultTransformer(Transformers.aliasToBean(DepositSearchResDto.class)).list();
					return new Response<>(SUCCESS, SUCCESS_CODE, null, listDepositSearchResDto, null);
				}
			}
			
			
			
			
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response updateDepositMaster(PaymentDepositDto paymentDepositDto) throws ApplicationException {
		try{
			sessionFactory.getCurrentSession().createQuery("UPDATE DepositMaster depo set "
					+ "depo.availableDeposit=:availableDeposit, "
					+ "depo.consumedDeposit=:consumedDeposit+depo.consumedDeposit, "
					+ "depo.updatedBy=:updatedBy, "
					+ "depo.updatedDate=:updatedDate "
					+ "WHERE depo.depositMasterId=:depositMasterId ")
				.setParameter("depositMasterId", paymentDepositDto.getDepositMasterId())
				.setDouble("availableDeposit", paymentDepositDto.getAvailableDeposit())
				.setDouble("consumedDeposit", paymentDepositDto.getConsumedAmount())
				.setParameter("updatedBy", paymentDepositDto.getCreatedBy())
				.setDate("updatedDate", GlobalCommonDateUtils.getDate(paymentDepositDto.getDepConsumptionDate(), "dd-M-yyyy HH:mm:ss"))
			.executeUpdate();
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getDepositDetailsByDepositId(Integer depositId) throws ApplicationException {
		try{
			List<PaymentDepositDetailsDto> listPaymentDepositDetailsDto = sessionFactory.getCurrentSession().getNamedQuery("GET_DEPOSIT_DETAILS_BY_DEPOSIT_ID")
					.setParameter("depositId", depositId)
					.setResultTransformer(Transformers.aliasToBean(PaymentDepositDetailsDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listPaymentDepositDetailsDto, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getAvailableDepositByPatient(int patientId,int unitId)
			throws BillingException {
		try{
			List<DepositSearchResDto> listDepositSearchResDto = sessionFactory.getCurrentSession().getNamedQuery("GET_AVAILABLE_DEPOSITE_BY_PATIENT_ID")
					.setParameter("patientId", patientId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(DepositSearchResDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listDepositSearchResDto, null);
			
		}catch(Exception e){
			
			throw e;
		}
	}
	
	@Override
	public Response getDepositListForRefund(int orgId, int unitId, int patientId, int depositTypeId)
			throws BillingException 
	{
		try{
			
			List<Integer> depositTypeIds = depositTypeId==10?Arrays.asList(1,2,3,5,6):Arrays.asList(depositTypeId);
			List<DepositSearchResDto> listDepositMasterDto = sessionFactory.getCurrentSession().getNamedQuery("GET_DEPOSIT_LIST_BY_DEPOSIT_TYPE")
					.setParameter("orgId", orgId)
					.setParameter("unitId", unitId)
					.setParameter("patientId", patientId)
					.setParameterList("depositTypeIds", depositTypeIds)
					.setResultTransformer(Transformers.aliasToBean(DepositSearchResDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listDepositMasterDto, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getDepositDetailsByDepositMaster(int orgId, int unitId, int depositMasterId)
			throws BillingException 
	{
	  try{
				
				List<DepositDetailsByDepositIdDto> listDepositDetails = sessionFactory.getCurrentSession().getNamedQuery("GET_DEPOSIT_DETAILS_BY_DEPOSIT")
						/*.setParameter("orgId", orgId)
						.setParameter("unitId", unitId)*/
						.setParameter("depositMasterId", depositMasterId)
						.setResultTransformer(Transformers.aliasToBean(DepositDetailsByDepositIdDto.class)).list();
				return new Response<>(SUCCESS, SUCCESS_CODE, null, listDepositDetails, null);
			}catch(Exception e){
				
				throw e;
			}
	 }

	@Override
	public Response updateDepositMasterRefundAmount(DepositMasterDto depositMasterDto) throws BillingException {
		try{
			sessionFactory.getCurrentSession().createQuery("UPDATE DepositMaster depo set "
					+ "depo.refundAmount=:refundAmount, "
					+ "depo.availableDeposit=:availableDeposit, "
					+ "depo.updatedBy=:updatedBy, "
					+ "depo.updatedDate=:updatedDate "
					+ "WHERE depo.depositMasterId=:depositMasterId ")
				.setParameter("depositMasterId", depositMasterDto.getDepositMasterId())
				.setDouble("refundAmount", depositMasterDto.getRefundAmount())
				.setDouble("availableDeposit", depositMasterDto.getAvailableDeposit())
				.setParameter("updatedBy", depositMasterDto.getUpdatedBy())
				.setTimestamp("updatedDate", GlobalCommonDateUtils.getDate(depositMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"))
			.executeUpdate();
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response updateDepositDetailsRefundAmount(PaymentDepositDetailsDto paymentDepositDetailsDto)
			throws BillingException 
	{
		try{
			sessionFactory.getCurrentSession().createQuery("UPDATE PaymentDepositDetails depoDtls set "
					+ "depoDtls.balanceAmount=:balanceAmount, "
					+ "depoDtls.refundAmount=:refundAmount, "
					+ "depoDtls.updatedBy=:updatedBy, "
					+ "depoDtls.updatedDate=:updatedDate "
					+ "WHERE depoDtls.depositDetailsId=:depositDetailsId ")
			    .setParameter("depositDetailsId", paymentDepositDetailsDto.getDepositDetailsId())
			    .setDouble("balanceAmount", paymentDepositDetailsDto.getBalanceAmount())
				.setDouble("refundAmount", paymentDepositDetailsDto.getRefundAmount())
				.setParameter("updatedBy", paymentDepositDetailsDto.getUpdatedBy())
				.setTimestamp("updatedDate", GlobalCommonDateUtils.getDate(paymentDepositDetailsDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"))
			.executeUpdate();
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response updateDepositDetails(PaymentDepositDetailsDto paymentDepositDetailsDto) throws BillingException {
		try{
			sessionFactory.getCurrentSession().createQuery("UPDATE PaymentDepositDetails depo set "
					+ "depo.balanceAmount=:balanceAmount, "
					+ "depo.consumedAmount=:consumedAmount, "
					//+ "depo.updatedBy=:updatedBy, "
					+ "depo.updatedDate=:updatedDate "
					+ "WHERE depo.depositDetailsId=:depositDetailsId ")
				.setParameter("depositDetailsId", paymentDepositDetailsDto.getDepositDetailsId())
				.setDouble("balanceAmount", Math.round(paymentDepositDetailsDto.getBalanceAmount() * 100.0)/100.0)
				.setDouble("consumedAmount", Math.round(paymentDepositDetailsDto.getConsumedAmount() * 100.0)/100.0)
				//.setParameter("updatedBy", paymentDepositDetailsDto.getCreatedBy())
				.setDate("updatedDate", TimeZoneComponent.getDateByzone(paymentDepositDetailsDto.getUnitId()))
			.executeUpdate();
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response updateReservationDeposit(AdmissionNoteDto admissionNoteDto) throws ApplicationException {
		try{
			
			if( admissionNoteDto.getDepositAmount()>0)
			{
				sessionFactory.getCurrentSession().createSQLQuery("update adt.t_admission_note "
						+ " set deposit_amt =  :depositAmount , "
						+ " deposit_pending =  :depositPending , "
						+ " deposit_collected = coalesce(deposit_collected,0) + :depositCollected, "
						+ " deposit_status = :depositStatus "
						+ " where admission_note_id =:admissionNoteId")
				    .setDouble("depositAmount", admissionNoteDto.getDepositAmount())
					.setDouble("depositPending", admissionNoteDto.getDepositPending())
					.setDouble("depositCollected", admissionNoteDto.getDepositCollected())
					.setParameter("depositStatus", admissionNoteDto.getDepositStatus())
					.setParameter("admissionNoteId", admissionNoteDto.getAdmissionNoteId())
					.executeUpdate();
			}
			else {
				sessionFactory.getCurrentSession().createSQLQuery("update adt.t_admission_note "
						+ " set deposit_pending =  :depositPending , "
						+ " deposit_collected = coalesce(deposit_collected,0) + :depositCollected, "
						+ " deposit_status = :depositStatus "
						+ " where admission_note_id =:admissionNoteId")
					.setDouble("depositPending", admissionNoteDto.getDepositPending())
					.setDouble("depositCollected", admissionNoteDto.getDepositCollected())
					.setParameter("depositStatus", admissionNoteDto.getDepositStatus())
					.setParameter("admissionNoteId", admissionNoteDto.getAdmissionNoteId())
					.executeUpdate();
			}
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response updateDepositAdmission(DepositMasterDto dto) throws BillingException {
		try{
			sessionFactory.getCurrentSession().createQuery("UPDATE DepositMaster depo set "
					+ " depo.visitTypeId =:visitTypeId, "
					+ "depo.admissionId=:admissionId, "
					+ "depo.depositTypeId=:depositTypeId, "
					+ "depo.updatedBy=:updatedBy, "
					+ "depo.updatedDate=:updatedDate "
					+ "WHERE depo.admNoteId=:admNoteId ")
				.setParameter("admissionId", dto.getAdmissionId())
				.setParameter("depositTypeId", dto.getDepositTypeId())
				.setParameter("admNoteId", dto.getAdmNoteId())
				.setParameter("updatedBy", dto.getCreatedBy())
				.setParameter("visitTypeId", dto.getVisitTypeId())
				.setDate("updatedDate", GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"))
			.executeUpdate();
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getDepositConsumtionDetails(Integer depositId) throws ApplicationException {
		 try{
				
				List<DepositConsumptionDto> list = sessionFactory.getCurrentSession().getNamedQuery("GET_DEPOSIT_CONSUMPTION_BY_DEPOSIT")
						.setParameter("depositId", depositId)
						.setResultTransformer(Transformers.aliasToBean(DepositConsumptionDto.class)).list();
				return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
			}catch(Exception e){
				
				throw e;
			}
	}
	
	@Override
	public Response getDocIdFromVisitId(Integer admissionId,Integer encounterId) throws BillingException {
		try{
			VisitTypeEncounterResDto dto = null;
			String encQuery = "SELECT doctor_id as \"doctorId\" FROM opd.t_encounter_master where encounter_id="+encounterId+" limit 1";
			
			String admQuery = "SELECT doctor_id as \"doctorId\" FROM adt.t_admission where admission_id="+admissionId+" limit 1";
			
			if(admissionId!=null)
				dto =(VisitTypeEncounterResDto) sessionFactory.getCurrentSession().createSQLQuery(admQuery)
					.setResultTransformer(Transformers.aliasToBean(VisitTypeEncounterResDto.class)).uniqueResult();
			
			if(encounterId!=null)
				dto = (VisitTypeEncounterResDto)sessionFactory.getCurrentSession().createSQLQuery(encQuery)
					.setResultTransformer(Transformers.aliasToBean(VisitTypeEncounterResDto.class)).list();
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, dto.getDoctorId());
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getMySqlData() throws ApplicationException {
		try{
			
			Session s = HibernateUtil.getMySqlSessionFactory().openSession();
	        Transaction tx = s.beginTransaction();
			List<VisitTypeEncounterResDto> listVisitTypeEncounterResDto = s.createSQLQuery("select name as \"visitNo\"  from reservations_patients").setResultTransformer(Transformers.aliasToBean(VisitTypeEncounterResDto.class)).list();
			tx.commit();
			 s.close();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listVisitTypeEncounterResDto, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response updateSignatureImage(int orgId, int unitId, int depositId, byte[] singnateureIamge,String path)
			throws BillingException {
		try{
		      Integer result = sessionFactory.getCurrentSession().createSQLQuery(" update " + 
		      		"	billing.m_deposit_master " + 
		      		" set " + 
		      		"	signature =:image, "+
		      		"	signeture_image =:path "+
		      		" where " + 
		      		"	deposit_master_id =:depositId " + 
		      		"	and org_id =:orgId " + 
		      		"	and unit_id =:unitId")
		          .setParameter("path", path)
		          .setParameter("orgId", orgId)
		          .setParameter("image", singnateureIamge)
		          .setParameter("unitId", unitId)
		          .setParameter("depositId", depositId)
		           .executeUpdate();
		      if(result != null){
		          return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		      }

		  }catch(HibernateException he){
		      logger.error("updateSignatureImage failed", he);
		      throw new BillingException(he.getMessage());
		  }catch(Exception e){
		      logger.error("updateSignatureImage failed", e);
		      throw new BillingException(e.getMessage());
		  }
		  return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public DepositMaster getDepositAgainstReceipt(Integer paymentRecieptId, Integer billingMasterId) {
		try {

			Criteria c = sessionFactory.getCurrentSession().createCriteria(DepositMaster.class);
			c.add(Restrictions.eq("againstPaymentRecieptId", paymentRecieptId));
			c.add(Restrictions.eq("billId", billingMasterId));
			
			DepositMaster depositMaster = (DepositMaster) c.uniqueResult();

			return depositMaster;
		} catch (Exception e) {
			logger.error("getDepositAgainstReceipt failed", e);
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	public Response updateDepositObject(DepositMaster depositMaster) {
		try {

			for(PaymentDepositDetails dto : depositMaster.getListPaymentDepositDetails())
			{
				dto.setAmount(0.00);
				dto.setBalanceAmount(0.00);
			}
			sessionFactory.getCurrentSession().update(depositMaster);
			
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (Exception e) {
			logger.error("updateDepositObject failed", e);
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	public Response ccAvenueCheckvalidation(String transactionNo) throws BillingException {
		try{
			CCAvenueLogDto dto = null;
			String encQuery = "SELECT cc_avenue_log_id as \"ccCvenueLogId\" FROM  billing.t_cc_avenue_log  where order_no ='"+transactionNo+"' and is_used = false  order by 1 desc limit 1";
			
				dto = (CCAvenueLogDto)sessionFactory.getCurrentSession().createSQLQuery(encQuery)
					.setResultTransformer(Transformers.aliasToBean(CCAvenueLogDto.class)).uniqueResult();
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, dto);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getGRNDetailsByGRNMaster(int orgId, int unitId, int gRNId) throws BillingException {
		try{
			
			List<DepositDetailsByDepositIdDto> listDepositDetails = sessionFactory.getCurrentSession().getNamedQuery("GET_GRN_DETAILS_BY_GRN")
					.setParameter("gRNId", gRNId)
					.setResultTransformer(Transformers.aliasToBean(DepositDetailsByDepositIdDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listDepositDetails, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Integer cancelDepositReceipt(DepositMasterDto dto) {
		try {
			if(dto.getDepositMasterId() != null){
				getSession().createSQLQuery("update billing.m_deposit_master set cancel_reason_id =:cancelReasonId , " +
								" cancel_reason_note =:cancelReasonNote, " +
								" cancelled_by =:cancelledBy, " +
								" status='I' "+
								"  where deposit_master_id =:depositMasterId " +
								" and org_id =:orgId and unit_id =:unitId ;")
						.setParameter("cancelReasonNote", dto.getCancelReasonNote() != null ? dto.getCancelReasonNote() :"")
						.setParameter("cancelReasonId", dto.getCancelReasonId() != null ? dto.getCancelReasonId() :0)
						.setParameter("cancelledBy", dto.getUpdatedBy() )
						.setParameter("depositMasterId", dto.getDepositMasterId() != null ? dto.getDepositMasterId() : 0)
						.setParameter("orgId", dto.getOrgId())
						.setParameter("unitId", dto.getUnitId())
						.executeUpdate();

				return 1;
			}
			return 0;
		} catch (Exception e) {
			logger.error("cancelDepositReceipt failed", e);
			throw new BillingException(e.getMessage());
		}
	}


	@Override
	public Response validateDepositAccess(Integer userId, Integer unitId, Integer organizationId) {
		try {
			String sql = "select tuum.deposit_receipt_cancellation from public.t_user_unit_mapper tuum " +
					"where tuum.user_id =:userId " +
					"and tuum.organisation_id =:orgId and tuum.unit_id =:unitId " +
					"and tuum.status = 'A' ";
			Boolean isUserValid = (Boolean) sessionFactory.getCurrentSession()
					.createSQLQuery(sql)
					.setParameter("userId", userId)
					.setParameter("orgId", organizationId)
					.setParameter("unitId", unitId)
					.uniqueResult();
			Map<String,Object> map = new HashMap<>();
			map.put("depositAccess",isUserValid);
			return new Response(SUCCESS, SUCCESS_CODE, null, null, map);
		}catch (Exception e){
			logger.error("validateDepositAccess failed", e);
			throw new BillingException(e.getMessage());
		}
	}

}
