package com.param.billing.cashDesk.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.billing.cashDesk.dto.BillingIdDetailsDto;
import com.param.billing.cashDesk.dto.ShiftClosingMasterDto;
import com.param.billing.common.ICommonConstants;
import com.param.billing.global.model.ShiftClosingDetailsMaster;
import com.param.billing.global.model.ShiftClosingMaster;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.Response;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;

@Repository
@SuppressWarnings({"unused","rawtypes","unchecked"})
public class ShiftClosingMasterDaoImpl extends GenericDao<ShiftClosingMaster,Integer> implements IShiftClosingMasterDao,ICommonConstants{

	
	public ShiftClosingMasterDaoImpl() {
		super(ShiftClosingMaster.class);
	}
	
	@Autowired
	private SessionFactory SessionFactory;
	@Autowired
	private DozerBeanMapper mapper;
	
	@Override
	public Response getCashReceipts(ShiftClosingMasterDto closingMasterDto) {
		try {

			List<ShiftClosingMasterDto> cashReceiptsList =sessionFactory.getCurrentSession().createSQLQuery(
							"select prm.reciept_number as \"recieptNumber\","
							+ "to_char(prm.created_date,'yyyy-MM-dd HH:mm:ss') as \"createdDate\","
							+" prm.payment_reciept_id as \"paymentReceiptId\","
							+ "concat(coalesce(pre.prefix_code,''),' ',coalesce(pr.first_name),' ',coalesce(pr.last_name)) as \"pName\","
							+ "pr.uhid_number as \"uhidNumber\","
							+ "prd.payment_reciept_details_id as \"paymentReceiptDetailsId\","
							+ " (coalesce(prd.amount, 0) + coalesce(prd.available_fund, 0))  as \"amount\" "
							+ " from billing.t_payment_reciept_master prm "
							+ "inner join billing.t_payment_reciept_details prd on prm.payment_reciept_id=prd.payment_receipt_master_id "
							+ "inner join billing.m_billing_master bm on prm.billing_master_id=bm.billing_master_id and bm.bill_cancelled ='N' "
							+ "inner join patient.t_patient_registration pr on bm.patient_id=pr.patient_id "
							+ "left join m_prefix_master pre on pre.prefix_id = pr.prefix_id "
							+ "where prd.payment_mode_id=1 and prm.date_of_reciept BETWEEN :openingDate AND :currentDate "
							+"' and prm.orgnization_id =:organizationId and prm.unit_id =:unitId "
							+" and prm.created_by =:createdBy "
							+ " and prd.exported_to_shift_close= false "
							+" order by reciept_number")
							//+ "where prd.payment_mode_id=2 and prm.date_of_reciept>'2018-11-12 16:12:19' order by reciept_number"
							.setParameter("openingDate", closingMasterDto.getOpeningDate())
							.setParameter("currentDate", closingMasterDto.getCurrentDate())
							.setParameter("organizationId", closingMasterDto.getOrganizationId())
							.setParameter("unitId", closingMasterDto.getUnitId())
							.setParameter("createdBy", closingMasterDto.getCreatedBy())
							.setResultTransformer(Transformers.aliasToBean(ShiftClosingMasterDto.class)).list();

		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, cashReceiptsList, null);
		
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getBankReceipts(ShiftClosingMasterDto closingMasterDto) {
		try {
		
			List<ShiftClosingMasterDto> bankReceiptsList =sessionFactory.getCurrentSession().createSQLQuery(
							"select prm.reciept_number as \"recieptNumber\","
							+ "to_char(prm.created_date,'yyyy-MM-dd HH:mm:ss') as \"createdDate\","
							+ "concat(coalesce(pre.prefix_code,''),' ',coalesce(pr.first_name),' ',coalesce(pr.last_name)) as \"pName\","
							+ "pr.uhid_number as \"uhidNumber\","
							+" prm.payment_reciept_id as \"paymentReceiptId\","
							+ "prd.payment_reciept_details_id as \"paymentReceiptDetailsId\","
							+ " (coalesce(prd.amount, 0) + coalesce(prd.available_fund, 0)) as \"amount\" " 
							+ " from billing.t_payment_reciept_master prm "
							+ "inner join billing.t_payment_reciept_details prd on prm.payment_reciept_id=prd.payment_receipt_master_id "
							+ "inner join billing.m_billing_master bm on prm.billing_master_id=bm.billing_master_id and bm.bill_cancelled ='N' "
							+ "inner join patient.t_patient_registration pr on bm.patient_id=pr.patient_id "
							+ "left join m_prefix_master pre on pre.prefix_id = pr.prefix_id "
							+ "where prd.payment_mode_id=2 and prm.date_of_reciept BETWEEN :openingDate AND :currentDate "
							+"' and prm.orgnization_id =:organizationId and prm.unit_id =:unitId "
							+" and prm.created_by =:createdBy "
							+ " and prd.exported_to_shift_close= false "
							+" order by reciept_number")
							.setParameter("openingDate", closingMasterDto.getOpeningDate())
							.setParameter("currentDate", closingMasterDto.getCurrentDate())
							.setParameter("organizationId", closingMasterDto.getOrganizationId())
							.setParameter("unitId", closingMasterDto.getUnitId())
							.setParameter("createdBy", closingMasterDto.getCreatedBy())
							.setResultTransformer(Transformers.aliasToBean(ShiftClosingMasterDto.class)).list();

		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, bankReceiptsList, null);
		
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getChequeInHand(ShiftClosingMasterDto closingMasterDto) {
		try {
		
			List<ShiftClosingMasterDto> chequeReceiptsList =sessionFactory.getCurrentSession().createSQLQuery(
							"select prd.check_number as \"chequeNumber\","
							+ "to_char(prm.created_date,'yyyy-MM-dd HH:mm:ss') as \"createdDate\","
							+ "bnkm.bank_name as \"bankName\","
							+" prm.payment_reciept_id as \"paymentReceiptId\","
							+ "concat(coalesce(pre.prefix_code,''),' ',coalesce(pr.first_name),' ',coalesce(pr.last_name)) as \"pName\","
							+ "prd.payment_reciept_details_id as \"paymentReceiptDetailsId\","
							+ "pr.uhid_number as \"uhidNumber\","
							+ " (coalesce(prd.amount, 0) + coalesce(prd.available_fund, 0))  as \"amount\" "
							+ " from billing.t_payment_reciept_master prm "
							+ "inner join billing.t_payment_reciept_details prd on prm.payment_reciept_id=prd.payment_receipt_master_id "
							+ "inner join billing.m_billing_master bm on prm.billing_master_id=bm.billing_master_id and bm.bill_cancelled ='N'"
							+ "inner join patient.t_patient_registration pr on bm.patient_id=pr.patient_id "
							+ "left join m_prefix_master pre on pre.prefix_id = pr.prefix_id "
							+ "left join billing.m_bank_master bnkm on bnkm.bank_id=prd.bank_id "
							+ "where prd.payment_mode_id=2 and prd.interim_cheque_status Is NULL and prm.date_of_reciept BETWEEN :openingDate AND :currentDate "
							+"' and prm.orgnization_id =:organizationId and prm.unit_id =:unitId "
							+" and prm.created_by =:createdBy "
							+ " and  prd.exported_to_shift_close= false "
							+" order by reciept_number")
							.setParameter("openingDate", closingMasterDto.getOpeningDate())
							.setParameter("currentDate", closingMasterDto.getCurrentDate())
							.setParameter("organizationId", closingMasterDto.getOrganizationId())
							.setParameter("unitId", closingMasterDto.getUnitId())
							.setParameter("createdBy", closingMasterDto.getCreatedBy())
							.setResultTransformer(Transformers.aliasToBean(ShiftClosingMasterDto.class)).list();

		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, chequeReceiptsList, null);
		
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getCardReceipts(ShiftClosingMasterDto closingMasterDto) {
		try {
			    
			List<ShiftClosingMasterDto> cardReceiptsList =sessionFactory.getCurrentSession().createSQLQuery(
							"select prm.reciept_number as \"recieptNumber\","
							+ "to_char(prm.created_date,'yyyy-MM-dd HH:mm:ss') as \"createdDate\","
							+ "concat(coalesce(pre.prefix_code,''),' ',coalesce(pr.first_name),' ',coalesce(pr.last_name)) as \"pName\","
							+ "pr.uhid_number as \"uhidNumber\","
							+ "prd.payment_reciept_details_id as \"paymentReceiptDetailsId\","
							+" prm.payment_reciept_id as \"paymentReceiptId\","
							+ " (coalesce(prd.amount, 0) + coalesce(prd.available_fund, 0))  as \"amount\" "
							+ " from billing.t_payment_reciept_master prm "
							+ "inner join billing.t_payment_reciept_details prd on prm.payment_reciept_id=prd.payment_receipt_master_id "
							+ "inner join billing.m_billing_master bm on prm.billing_master_id=bm.billing_master_id and bm.bill_cancelled ='N' "
							+ "inner join patient.t_patient_registration pr on bm.patient_id=pr.patient_id "
							+ "left join m_prefix_master pre on pre.prefix_id = pr.prefix_id "
							+ "where (prd.payment_mode_id=3 or prd.payment_mode_id=4) and prm.date_of_reciept BETWEEN :openingDate AND :currentDate "
							+"' and prm.orgnization_id =:organizationId and prm.unit_id =:unitId "
							+" and prm.created_by =:createdBy "
							+ " and prd.exported_to_shift_close= false "
							+" order by reciept_number")
							.setParameter("openingDate", closingMasterDto.getOpeningDate())
							.setParameter("currentDate", closingMasterDto.getCurrentDate())
							.setParameter("organizationId", closingMasterDto.getOrganizationId())
							.setParameter("unitId", closingMasterDto.getUnitId())
							.setParameter("createdBy", closingMasterDto.getCreatedBy())
							.setResultTransformer(Transformers.aliasToBean(ShiftClosingMasterDto.class)).list();

		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, cardReceiptsList, null);
		
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getCardInHand(ShiftClosingMasterDto closingMasterDto) {
		try {
			    
			List<ShiftClosingMasterDto> cardInHandList =sessionFactory.getCurrentSession().createSQLQuery(
							"select prd.card_number as \"cardNumber\","
							+ "ctm.card_type_name as \"cardTypeName\","
							+ "prd.transaction_no as \"transactionNo\","
							+ "concat(coalesce(pre.prefix_code,''),' ',coalesce(pr.first_name),' ',coalesce(pr.last_name)) as \"pName\","
							+ "pr.uhid_number as \"uhidNumber\","
							+" prm.payment_reciept_id as \"paymentReceiptId\","
							+ "prd.payment_reciept_details_id as \"paymentReceiptDetailsId\","
							+ " (coalesce(prd.amount, 0) + coalesce(prd.available_fund, 0))  as \"amount\" "
							+ " from billing.t_payment_reciept_master prm "
							+ "inner join billing.t_payment_reciept_details prd on prm.payment_reciept_id=prd.payment_receipt_master_id "
							+ "inner join billing.m_billing_master bm on prm.billing_master_id=bm.billing_master_id and bm.bill_cancelled ='N' "
							+ "inner join patient.t_patient_registration pr on bm.patient_id=pr.patient_id "
							+ "left join m_prefix_master pre on pre.prefix_id = pr.prefix_id "
							+ "left join billing.m_card_type_master ctm on ctm.card_type_id=prd.card_type_id "
							+ "where (prd.payment_mode_id=3 or prd.payment_mode_id=4) and prd.interim_card_status IS NULL and prm.date_of_reciept BETWEEN :openingDate AND :currentDate "
							+"' and prm.orgnization_id =:organizationId and prm.unit_id =:unitId "
							+" and prm.created_by =:createdBy "
							+ " and prd.exported_to_shift_close= false "
							+" order by reciept_number")
							.setParameter("openingDate", closingMasterDto.getOpeningDate())
							.setParameter("currentDate", closingMasterDto.getCurrentDate())
							.setParameter("organizationId", closingMasterDto.getOrganizationId())
							.setParameter("unitId", closingMasterDto.getUnitId())
							.setParameter("createdBy", closingMasterDto.getCreatedBy())
							.setResultTransformer(Transformers.aliasToBean(ShiftClosingMasterDto.class)).list();

		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, cardInHandList, null);
		
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getIntrimCash(ShiftClosingMasterDto closingMasterDto) {
		try {
			List<ShiftClosingMasterDto> intrimCashList =sessionFactory.getCurrentSession().createSQLQuery(
							"select ich_number as \"ichNumber\","
							+ "cash_handover_amount as \"cashHandoverAmount\","
							+ "id as \"id\","
							+ "to_char(created_date,'yyyy-MM-dd HH:mm:ss') as \"createdDate\","
							+ "created_by as \"createdBy\" "
							+ "from public.t_interim_handover_details "
							+ "where mode_of_transaction='Cash' and created_date BETWEEN :openingDate AND :currentDate "
							+"' and organization_id =:organizationId and unit_id =:unitId "
							+" and created_by =:createdBy "
							+ " and exported_to_shift_close= false ")
							.setParameter("openingDate", closingMasterDto.getOpeningDate())
							.setParameter("currentDate", closingMasterDto.getCurrentDate())
							.setParameter("organizationId", closingMasterDto.getOrganizationId())
							.setParameter("unitId", closingMasterDto.getUnitId())
							.setParameter("createdBy", closingMasterDto.getCreatedBy())
							.setResultTransformer(Transformers.aliasToBean(ShiftClosingMasterDto.class)).list();

		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, intrimCashList, null);
		
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getIntrimCheque(ShiftClosingMasterDto closingMasterDto) {
		try {
			List<ShiftClosingMasterDto> intrimChequeList =sessionFactory.getCurrentSession().createSQLQuery(
							"select ich_number as \"ichNumber\","
							+ "cash_handover_amount as \"cashHandoverAmount\","
							+ "id as \"id\","
							+ "to_char(created_date,'yyyy-MM-dd HH:mm:ss') as \"createdDate\","
							+ "created_by as \"createdBy\" "
							+ "from public.t_interim_handover_details "
							+ "where mode_of_transaction='cheque' and created_date BETWEEN :openingDate AND :currentDate "
							+"' and organization_id =:organizationId and unit_id =:unitId "
							+" and created_by =:createdBy "
							+ " and exported_to_shift_close= false ")
						.setParameter("openingDate", closingMasterDto.getOpeningDate())
						.setParameter("currentDate", closingMasterDto.getCurrentDate())
						.setParameter("organizationId", closingMasterDto.getOrganizationId())
						.setParameter("unitId", closingMasterDto.getUnitId())
						.setParameter("createdBy", closingMasterDto.getCreatedBy())
						.setResultTransformer(Transformers.aliasToBean(ShiftClosingMasterDto.class)).list();

		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, intrimChequeList, null);
		
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getIntrimCard(ShiftClosingMasterDto closingMasterDto) {
		try {
			List<ShiftClosingMasterDto> intrimCardList =sessionFactory.getCurrentSession().createSQLQuery(
							"select ich_number as \"ichNumber\","
							+ "cash_handover_amount as \"cashHandoverAmount\","
							+ "id as \"id\","
							+ "to_char(created_date,'yyyy-MM-dd HH:mm:ss') as \"createdDate\","
							+ "created_by as \"createdBy\" "
							+ "from public.t_interim_handover_details "
							+ "where mode_of_transaction='card' and created_date BETWEEN :openingDate AND :currentDate "
							+"' and organization_id =:organizationId and unit_id =:unitId "
							+" and created_by =:createdBy "
							+ " and exported_to_shift_close= false ")
						.setParameter("openingDate", closingMasterDto.getOpeningDate())
						.setParameter("currentDate", closingMasterDto.getCurrentDate())
						.setParameter("organizationId", closingMasterDto.getOrganizationId())
						.setParameter("unitId", closingMasterDto.getUnitId())
						.setParameter("createdBy", closingMasterDto.getCreatedBy())
						.setResultTransformer(Transformers.aliasToBean(ShiftClosingMasterDto.class)).list();

		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, intrimCardList, null);
		
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getCashHandoverDetails(ShiftClosingMasterDto closingMasterDto) {
		try {
			
			List<ShiftClosingMasterDto> cashHandoverList =sessionFactory.getCurrentSession().createSQLQuery(
							"select unit_id as \"unitId\","
							+ "cash_handover_amount as \"cashHandoverAmountlog\","
							+ "id as \"id\","
							+ "to_char(date,'yyyy-MM-dd HH24:MI:SS') as \"createdDate\","
							+ "organization_id as \"organizationId\" "
							+ "from public.t_cash_handover_details "
							+ "where cash_handover_to_id =:cashHandOverToId and organization_id =:organizationId "
							+" and unit_id =:unitId ORDER BY id desc LIMIT 1" )
					.setParameter("cashHandOverToId", closingMasterDto.getCreatedBy())
					.setParameter("organizationId", closingMasterDto.getOrganizationId())
					.setParameter("unitId", closingMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(ShiftClosingMasterDto.class)).list();

		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, cashHandoverList, null);
		
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response saveShiftCloseBalance(ShiftClosingMasterDto closingMasterDto) {
		try {
			
			
			ShiftClosingMaster shiftClosingMaster = new ShiftClosingMaster();
			shiftClosingMaster.setCreateDate(GlobalCommonDateUtils.getDate(closingMasterDto.getCreatedDate(),"dd-M-yyyy HH:mm:ss"));
			shiftClosingMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(closingMasterDto.getCreatedDate(),"dd-M-yyyy HH:mm:ss"));
			shiftClosingMaster.setIchAmount(closingMasterDto.getIchAmount());
			shiftClosingMaster.setModeOfPayment(closingMasterDto.getModeOfPayment());
			shiftClosingMaster.setToBeHandover(closingMasterDto.getToBeHandover());
			shiftClosingMaster.setAmount(closingMasterDto.getAmount());
			shiftClosingMaster.setActualAmount(closingMasterDto.getActualAmount());
			shiftClosingMaster.setCreatedBy(closingMasterDto.getCreatedBy());
			shiftClosingMaster.setOrganisationId(closingMasterDto.getOrganizationId());
			shiftClosingMaster.setUnitId(closingMasterDto.getUnitId());
			shiftClosingMaster.setDifference(closingMasterDto.getDifference());
			shiftClosingMaster.setRefundAmount(closingMasterDto.getRefundAmount());
			
			shiftClosingMaster = save(shiftClosingMaster);
			
			
				if( closingMasterDto.getListOfIds().size()>0 &&  closingMasterDto.getListOfIds() != null && shiftClosingMaster != null ) {
					
					for(BillingIdDetailsDto dto : closingMasterDto.getListOfIds()) 
					{
						
						if(dto.getPaymentReceiptDetailsId() != null ) 
						{
							Integer result =SessionFactory.getCurrentSession().getNamedQuery("UPDATE_PAYMENT_RECIEIPT_BY_ID")
									.setParameter("paymentRecieptDetailsId",dto.getPaymentReceiptDetailsId())
									.setParameter("orgnizationId",closingMasterDto.getOrganizationId())
					                .setParameter("unitId", closingMasterDto.getUnitId())
									.executeUpdate();
							
						}
						else if(dto.getInterimHandoverId() != null ) 
							{
							
								Integer result =SessionFactory.getCurrentSession().getNamedQuery("UPDATE_INTERIMHANDOVER_BY_ID")
										.setParameter("interimHandoverId",dto.getInterimHandoverId())
										.setParameter("orgnizationId",closingMasterDto.getOrganizationId())
										.setParameter("unitId", closingMasterDto.getUnitId())
										.executeUpdate();
							
							
							}
							else if(dto.getPaymentRefundId() != null )
								{
							
									Integer result =SessionFactory.getCurrentSession().getNamedQuery("UPDATE_PAYMENT_REFUND_BY_ID")
											.setParameter("paymentRefundId",dto.getPaymentRefundId())
											.setParameter("orgnizationId",closingMasterDto.getOrganizationId())
											.setParameter("unitId", closingMasterDto.getUnitId())
											.executeUpdate();
							
								} 
						
					}
						
				}
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
			
		} catch (Exception e) {
			
			throw e;
		}
			//return new Response<>(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getShiftClosingBalanceSheet(ShiftClosingMasterDto closingMasterDto) {
		try {
			List<ShiftClosingMasterDto> closingBalSheetList =sessionFactory.getCurrentSession().createSQLQuery(
							"select id as \"id\", "
							+ " mode_of_payment as \"modeOfPayment\", "
							+ "ich_amount as \"ichAmount\", "
							+ "amount as \"amountCash\", "
							+ "to_be_handover as \"toBeHandover\", "
							+ "actual_amount as \"actualAmountCash\", "
							+ "difference as \"difference\", "
							+ "to_char(created_date,'yyyy-MM-dd HH:mm:ss') as \"createdDate\" "
							+ "from billing.m_shift_close_balance "
							+ "where created_by =:createdBy and  organisation_id =:organizationId "
							+" and unit_id =:unitId order by 1 desc limit 3")
					.setParameter("createdBy", closingMasterDto.getCreatedBy())
					.setParameter("organizationId", closingMasterDto.getOrganizationId())
					.setParameter("unitId", closingMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(ShiftClosingMasterDto.class)).list();

		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, closingBalSheetList, null);
		
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response saveShiftClosedetails(ShiftClosingMasterDto closingMasterDto) {
try {
		/*	ShiftClosingMasterDto dto = new ShiftClosingMasterDto();
			dto = (ShiftClosingMasterDto)sessionFactory.getCurrentSession()
			.getNamedQuery("CHECK_FOR_UNIQUE_SHIFTCLOSING_SUMMRY")
			.setInteger("organizationId", closingMasterDto.getOrganizationId())
			.setInteger("unitId", closingMasterDto.getUnitId())
			.setInteger("handoverBy",closingMasterDto.getHandoverBy())
			//.setInteger("handoverTo",closingMasterDto.getHandoverTo())
			.setString("createdDate",GlobalCommonDateUtils.getStringDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(unitId), "dd-MM-yyyy"),"dd-MM-yyyy"))
			.setResultTransformer(Transformers.aliasToBean(ShiftClosingMasterDto.class)).uniqueResult();
	
			if(dto != null && dto.getId() != null ) {
				
				return new Response<>(ALREADY_EXIST, ALREADY_EXIST_CODE,"ALREADY SHIFT CLOSED !!", null, null);
				
			}else {*/
			ShiftClosingDetailsMaster shiftClosingDetailsMaster = new ShiftClosingDetailsMaster();
			
			Date date = TimeZoneComponent.getDateByzone(closingMasterDto.getUnitId());  
		    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-M-dd");  
		    String strDate= formatter.format(date);
			
			String cobString="SC";
			char[] otp =GlobalCommonDateUtils.generateRandomNumber(5);
			String cob=cobString+String.valueOf(otp);
			//System.out.println("cob"+cob);
			shiftClosingDetailsMaster.setClosingNumber(cob);
			shiftClosingDetailsMaster.setAmount(closingMasterDto.getAmount());
			shiftClosingDetailsMaster.setCashierName(closingMasterDto.getCashierName());
			shiftClosingDetailsMaster.setCreatedBy(closingMasterDto.getHandoverBy());
			shiftClosingDetailsMaster.setHandoverBy(closingMasterDto.getHandoverBy());
			shiftClosingDetailsMaster.setUpdatedBy(closingMasterDto.getHandoverBy());
			shiftClosingDetailsMaster.setCreatedDate(GlobalCommonDateUtils.getDate(closingMasterDto.getCreatedDate(),"dd-M-yyyy HH:mm:ss"));
			shiftClosingDetailsMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(closingMasterDto.getCreatedDate(),"dd-M-yyyy HH:mm:ss"));
			shiftClosingDetailsMaster.setDifference(closingMasterDto.getDifference());
			shiftClosingDetailsMaster.setOrganizationId(closingMasterDto.getOrganizationId());
			shiftClosingDetailsMaster.setUnitId(closingMasterDto.getUnitId());
			// BR-154: classify the shift-close by its computed discrepancy instead of hardcoding "A".
			// A non-zero difference (short/excess cash) is recorded as MISMATCH for supervisor review;
			// an exact match is MATCHED. Submission is never blocked - the discrepancy is tracked, not rejected.
			Double closingDifference = closingMasterDto.getDifference();
			shiftClosingDetailsMaster.setStatus(closingDifference != null && closingDifference.doubleValue() != 0.0
					? "MISMATCH" : "MATCHED");
			shiftClosingDetailsMaster.setHandoverTo(closingMasterDto.getHandoverTo());
			sessionFactory.getCurrentSession().save(shiftClosingDetailsMaster);
			
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
			
		} catch (Exception e) {
			
			throw e;
		}
			//return new Response<>(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getRefundList(ShiftClosingMasterDto closingMasterDto) {
		try {
			List<ShiftClosingMasterDto> refundList =sessionFactory.getCurrentSession().createSQLQuery(
							"select prm.payment_refund_voucher_no as \"voucherNo\", "
							+ "to_char(prm.date_of_refund,'yyyy-MM-dd HH:mm:ss') as \"createdDate\", "
							+ "concat(coalesce(pre.prefix_code,''),' ',coalesce(pr.first_name),' ',coalesce(pr.last_name)) as \"pName\", "
							+ "pr.uhid_number as \"uhidNumber\", "
							+"prm.payment_refund_id as \"paymentRefundId\","
							+ "prm.amount_refunded as \"amount\" "
							+ " from billing.t_payment_refund_master prm "
							+ "inner join patient.t_patient_registration pr on prm.patient_id=pr.patient_id "
							+ "left join m_prefix_master pre on pre.prefix_id = pr.prefix_id "
							+ "where prm.date_of_refund BETWEEN :openingDate AND :currentDate "
							+"' and prm.orgnisation_id =:organizationId and prm.unit_id =:unitId "
							+" and prm.created_by =:createdBy "
							+ " and prm.exported_to_shift_close= false ")
						//	+ "where prm.date_of_refund>'2018-11-12 16:12:19' order by payment_refund_id"
					.setParameter("openingDate", closingMasterDto.getOpeningDate())
					.setParameter("currentDate", closingMasterDto.getCurrentDate())
					.setParameter("organizationId", closingMasterDto.getOrganizationId())
					.setParameter("unitId", closingMasterDto.getUnitId())
					.setParameter("createdBy", closingMasterDto.getCreatedBy())
					.setResultTransformer(Transformers.aliasToBean(ShiftClosingMasterDto.class)).list();
			

		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, refundList, null);
		
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getshiftClosingRecords(ShiftClosingMasterDto closingMasterDto) {
		try {
			String query = sessionFactory.getCurrentSession()
					.getNamedQuery("SEARCH_SHIFT_CLOSING_LIST_BY_MULTIPLE_CRITERIA")
					.getQueryString();
		
			StringBuffer subQuery = new StringBuffer();
			
			if(closingMasterDto.getHandoverTo() != null && closingMasterDto.getHandoverTo()!=0){
				subQuery.append(" AND scm.handoverTo =:handoverTo ");
			}
			
			if(closingMasterDto.getClosingNumber()!="" && closingMasterDto.getClosingNumber()!=null){
				subQuery.append(" AND scm.closingNumber =:closingNumber ");
			}
			
			subQuery.append(" order by scm.id desc");
			query = query.concat(subQuery.toString());
			
			Query sqlQuery = sessionFactory.getCurrentSession()
					.createQuery(query)
					.setParameter("organizationId", closingMasterDto.getOrganizationId())
					.setParameter("unitId",  closingMasterDto.getUnitId())
					.setParameter("fromDate",  closingMasterDto.getFromDate())
					.setParameter("toDate",closingMasterDto.getToDate())
					.setParameter("handoverBy",  closingMasterDto.getHandoverBy());

			if(closingMasterDto.getHandoverTo() != null && closingMasterDto.getHandoverTo()!=0)
				sqlQuery.setParameter("handoverTo",  closingMasterDto.getHandoverTo());

			if(closingMasterDto.getClosingNumber()!="" && closingMasterDto.getClosingNumber()!=null)
				sqlQuery.setParameter("closingNumber",  closingMasterDto.getClosingNumber());

			List<ShiftClosingMasterDto> list = sqlQuery.setResultTransformer(Transformers.aliasToBean(ShiftClosingMasterDto.class)).list();

			//System.out.println(list);
	
		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, list, null);
		
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response validateInterimByShiftClosing(ShiftClosingMasterDto closingMasterDto) {
		try {
			
			
			ShiftClosingMasterDto dto = (ShiftClosingMasterDto)sessionFactory.getCurrentSession().createSQLQuery(
					 "select id as id from public.t_cash_handover_details  where  cash_handover_to_id =:handoverToId 	"
					+" and organization_id =:organizationId and unit_id =:unitId "
					+ " and  shift_close = false  and status_id=2 limit 1")
					.setParameter("handoverToId", closingMasterDto.getCreatedBy())
					.setParameter("organizationId", closingMasterDto.getOrganizationId())
					.setParameter("unitId", closingMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(ShiftClosingMasterDto.class)).uniqueResult();
			

		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, dto);
		
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	
	}

	@Override
	public Response validateShiftClosingBalance(ShiftClosingMasterDto closingMasterDto) {
		try {
			
			ShiftClosingMasterDto dto = (ShiftClosingMasterDto)sessionFactory.getCurrentSession().createSQLQuery(
					 "select id as \"id\" , to_char(created_date, 'yyyy-MM-dd HH:mm:ss') as \"createdDate\"  from public.t_cash_handover_details  where  cash_handover_to_id =:handoverToId "
					+" and organization_id =:organizationId and unit_id =:unitId "
					+ " and  shift_close = false and  status_id=2 order by 1 desc limit 1")
					.setParameter("handoverToId", closingMasterDto.getCreatedBy())
					.setParameter("organizationId", closingMasterDto.getOrganizationId())
					.setParameter("unitId", closingMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(ShiftClosingMasterDto.class)).uniqueResult();
			
			if(dto != null) {
				
				List<ShiftClosingMasterDto> closingBalSheetList =sessionFactory.getCurrentSession().createSQLQuery(
						"select distinct scb.mode_of_payment as \"modeOfPayment\", "
						+ "scb.ich_amount as \"ichAmount\", "
						+ "scb.amount as \"amountCash\", "
						+ "scb.to_be_handover as \"toBeHandover\", "
						+ "scb.actual_amount as \"actualAmountCash\","
						+ "chd.shift_close as \"shiftClose\",  "
						+ "scb.difference as \"difference\", "
						+ "to_char(scb.created_date,'yyyy-MM-dd HH:mm:ss') as \"createdDate\" "
						+ "from billing.m_shift_close_balance scb "
						+ "inner join public.t_cash_handover_details chd on chd.cash_handover_to_id  = scb.created_by "
						//+ "where  to_char(scb.created_date ,'yyyy-MM-dd HH:mm:ss') > '"+dto.getCreatedDate()+"'  and  chd.shift_close = false "
						+ "where  ((to_char(scb.created_date ,'hh24:mi') > to_char(chd.created_date,'hh24:mi') ) or (to_char(scb.created_date ,'yyyy-MM-dd') > to_char(chd.created_date ,'yyyy-MM-dd')) ) and  chd.shift_close = false "
						+ " and scb.created_by =:createdBy and scb.organisation_id =:organizationId "
						+" and scb.unit_id =:unitId ")
						.setParameter("createdBy", closingMasterDto.getCreatedBy())
						.setParameter("organizationId", closingMasterDto.getOrganizationId())
						.setParameter("unitId", closingMasterDto.getUnitId())
						.setResultTransformer(Transformers.aliasToBean(ShiftClosingMasterDto.class)).list();

				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, closingBalSheetList, null);
				
			}else {
				
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
			}
			
		
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, null, COMMON_ERROR_MESSAGE, null, null);

	}

}


