package com.param.billing.cashDesk.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.billing.cashDesk.dto.DenominationMasterDto;
import com.param.billing.cashDesk.dto.InterimHandoverMasterDto;
import com.param.billing.global.model.DenominationDetailsMaster;
import com.param.billing.global.model.InterimHandoverMaster;
import com.param.global.billing.dto.PaymentReceiptDetailsDto;
import com.param.global.billing.dto.PaymentRecieptMasterDto;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.DenominationDetailsMasterDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({"rawtypes","unused","unchecked"})
public class InterimHandoverMasterDaoImpl extends GenericDao<InterimHandoverMaster, Integer> implements IInterimHandoverMasterDao,ICommonConstants{
	
	public InterimHandoverMasterDaoImpl() {
		super(InterimHandoverMaster.class);
	}

	@Autowired
	private SessionFactory SessionFactory;
	
	@Override
	public Response getListOfDenominations(DenominationMasterDto denominationMasterDto) {
		try {
			List<DenominationMasterDto> list =sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DENOMINATION_MASTER_LIST")
					.setResultTransformer(Transformers.aliasToBean(DenominationMasterDto.class)).list();

		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, list, null);
		
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
	}
	
	@Override
	public Response saveInterimHandover(InterimHandoverMasterDto interimHandoverMasterDto) {
		try {
			InterimHandoverMaster interimHandoverMaster1 =null;
			
			InterimHandoverMaster interimMaster=new InterimHandoverMaster();
			interimMaster.setCashHandoverToId(interimHandoverMasterDto.getCashHandoverToId());
			interimMaster.setCashHandoverById(interimHandoverMasterDto.getCashHandoverById());
			interimMaster.setCashHandoverAmount(interimHandoverMasterDto.getCashHandoverAmount());
			interimMaster.setOrganizationId(interimHandoverMasterDto.getOrganizationId());
			interimMaster.setUnitId(interimHandoverMasterDto.getUnitId());
			interimMaster.setRemarks(interimHandoverMasterDto.getRemarks());
			interimMaster.setModeOfTransaction(interimHandoverMasterDto.getModeOfTransaction());
			interimMaster.setCreatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(interimHandoverMasterDto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
			interimMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(interimHandoverMasterDto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
			interimMaster.setCreatedBy(interimHandoverMasterDto.getCashHandoverById());
			interimMaster.setUpdatedBy(interimHandoverMasterDto.getCashHandoverById());
			interimMaster.setStatus(interimHandoverMasterDto.getStatus());
			interimMaster.setStatusId(interimHandoverMasterDto.getStatusId());
			if(interimHandoverMasterDto.getId()!=null) {
				interimMaster.setIchNumber(interimHandoverMasterDto.getIchNumber());
				interimMaster.setId(interimHandoverMasterDto.getId());
				 interimHandoverMaster1 = update(interimMaster);
					return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, interimHandoverMaster1);
			}else {
				String ichString="ICH";
				char []ichNo = GlobalCommonDateUtils.generateRandomNumber(5);
				String ichNumber= ichString+String.valueOf(ichNo);
				//System.out.println("ichNumber"+ichNumber);
				interimMaster.setIchNumber(ichNumber);	
				interimHandoverMaster1 =	save(interimMaster);
			 return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, interimHandoverMaster1);
			}
	
		
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
//		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	
	@Override
	public Response saveDenominations(List<DenominationDetailsMasterDto> denominationDetailsMasterDto) {
		try{
			List<DenominationDetailsMasterDto> list=new ArrayList<DenominationDetailsMasterDto>();
		//System.out.println(denominationDetailsMasterDto.size());
		for(DenominationDetailsMasterDto denominationList:denominationDetailsMasterDto)
		{
			Date date = TimeZoneComponent.getDateByzone(0);
			if(denominationList.getQty()!=0) {
				
			DenominationDetailsMaster denominationDetailsMaster = new DenominationDetailsMaster();
			denominationDetailsMaster.setDenomination(denominationList.getDenomination());
			denominationDetailsMaster.setAmount(Double.parseDouble(denominationList.getAmount().toString()));
			denominationDetailsMaster.setCreatedDate(GlobalCommonDateUtils.getDate(date, "dd-M-yyyy HH:mm:ss"));
			denominationDetailsMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(date, "dd-M-yyyy HH:mm:ss"));
			denominationDetailsMaster.setCreatedBy(denominationList.getCreatedBy());
			denominationDetailsMaster.setUpdatedBy(denominationList.getUpdatedBy());
			denominationDetailsMaster.setStatus("A");
			denominationDetailsMaster.setQuantity(denominationList.getQty());
			denominationDetailsMaster.setInterimHandoverTransactionId(denominationList.getInterimHandoverTransactionId());
			
			if(denominationList.getId()!=null) {
				denominationDetailsMaster.setStatus("I");
				denominationDetailsMaster.setId(denominationList.getId());
				sessionFactory.getCurrentSession().update(denominationDetailsMaster);
			}else {
				denominationDetailsMaster.setStatus("A");
				sessionFactory.getCurrentSession().save(denominationDetailsMaster);
			
			}
			}
		}
		
	return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
	} catch (HibernateException he) {
		throw he;
	} catch (Exception e) {
		
		throw e;
	}
//	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		
	}

	@Override
	public Response getInterimHandoverList(InterimHandoverMasterDto interimHandoverMasterDto) {
		try {
			
			String query = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_INTERIM_HANDOVER_MASTER_LIST")
					.getQueryString();
		
			StringBuffer subQuery = new StringBuffer();
			
			if(interimHandoverMasterDto.getCashHandoverToId()!=0){
				subQuery.append(" AND interimHandoverMaster.cashHandoverToId =:cashHandoverToId ");
			}
			
			
			if(interimHandoverMasterDto.getIchNumber()!="" && interimHandoverMasterDto.getIchNumber()!=null){
				subQuery.append(" AND interimHandoverMaster.ichNumber =:ichNumber ");
			}
			
			subQuery.append(" order by interimHandoverMaster.id desc");
			query = query.concat(subQuery.toString());
		
			Query sqlQuery =sessionFactory.getCurrentSession()
				.createQuery(query)
				.setParameter("organizationId",interimHandoverMasterDto.getOrganizationId())
				.setParameter("unitId",interimHandoverMasterDto.getUnitId())
				.setParameter("fromDate",interimHandoverMasterDto.getFromDate())
				.setParameter("toDate", interimHandoverMasterDto.getToDate())
				.setParameter("cashHandoverById",interimHandoverMasterDto.getCashHandoverById());

			if(interimHandoverMasterDto.getCashHandoverToId()!=0)
				sqlQuery.setParameter("cashHandoverToId", interimHandoverMasterDto.getCashHandoverToId());

			if(interimHandoverMasterDto.getIchNumber()!="" && interimHandoverMasterDto.getIchNumber()!=null)
				sqlQuery.setParameter("ichNumber", interimHandoverMasterDto.getIchNumber());

			List<InterimHandoverMasterDto> list = sqlQuery.setResultTransformer(Transformers.aliasToBean(InterimHandoverMasterDto.class)).list();

			//System.out.println(list);
		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, list, null);
		
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
public Response validateUser1(InterimHandoverMasterDto interimHandoverMasterDto) {
try{
			String encyption =  GlobalCommonDateUtils.encrypt(interimHandoverMasterDto.getHandoverByPassword().trim());
			InterimHandoverMasterDto uservali = (InterimHandoverMasterDto) SessionFactory.getCurrentSession().getNamedQuery("VALIDATE_USER")
					.setParameter("userId",interimHandoverMasterDto.getCashHandoverById() )
					.setParameter("password",encyption)
					.setParameter("organizationId", interimHandoverMasterDto.getOrganizationId())
	                .setParameter("unitId", interimHandoverMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(InterimHandoverMasterDto.class)).uniqueResult();
			
			if(uservali!= null) {
				String encyption1 =  GlobalCommonDateUtils.encrypt(interimHandoverMasterDto.getHandoverToPassword().trim());
				InterimHandoverMasterDto uservali1 = (InterimHandoverMasterDto) SessionFactory.getCurrentSession().getNamedQuery("VALIDATE_USER")
						.setParameter("userId",interimHandoverMasterDto.getCashHandoverToId() )
						.setParameter("password",encyption1)
						.setParameter("organizationId", interimHandoverMasterDto.getOrganizationId())
		                .setParameter("unitId", interimHandoverMasterDto.getUnitId())
						.setResultTransformer(Transformers.aliasToBean(InterimHandoverMasterDto.class)).uniqueResult();
				if(uservali1!= null) {
					return new Response(SUCCESS, null, null,null, uservali);
					
				}else {
					
					return new Response(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null,null);
				}
			}
			
		}catch(HibernateException he){
		}catch(Exception e){
			
		}
		
		return new Response(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null,null);
	}

	
	@Override
	public Response getICHNumberList(InterimHandoverMasterDto interimHandoverMasterDto) {
		try {
			List<InterimHandoverMasterDto> ichNumberList=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ICHNUMBER_LIST")
					.setParameter("organizationId",  interimHandoverMasterDto.getOrganizationId())
					.setParameter("unitId",  interimHandoverMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(InterimHandoverMasterDto.class)).list();

			//System.out.println(ichNumberList);
		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, ichNumberList, null);
		
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
//		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getDenominationDetailsList(DenominationDetailsMasterDto denominationDetailsMasterDto) {
		try {
			List<DenominationDetailsMasterDto> denominationlist = sessionFactory.getCurrentSession().createSQLQuery(
					"select dm.quantity as \"qty\","
					+ " dm.id as \"id\","
					+ " dm.denomination as \"denomination\","
					+ "interimHandover.remarks as \"remarks\","
					+ "to_char(dm.created_date,'yyyy-MM-dd hh:mm:ss') as \"createdDate\" ,"
					+ "dm.amount as \"amount\","
					+ "interimHandover.cash_handover_amount as \"cashHandoverAmount\" "
					+ "from public.t_interim_handover_details interimHandover "
					+ "right join public.t_denomination_details dm on interimHandover.id = dm.interim_handover_transaction_id "
					+ "where interimHandover.mode_of_transaction='Cash' and  dm.interim_handover_transaction_id =:interimHandoverTransactionId and dm.status='A' ")
					.setParameter("interimHandoverTransactionId", denominationDetailsMasterDto.getInterimHandoverTransactionId())
					.setResultTransformer(Transformers.aliasToBean(DenominationDetailsMasterDto.class)).list();

		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, denominationlist, null);
		
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
//		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getChequeList(PaymentReceiptDetailsDto paymentReceiptDetailsDto) {
		try {
			/*Date date = TimeZoneComponent.getDateByzone(unitId);  
			 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    String currentDate= formatter.format(date);*/
			String cashOpeingDate = paymentReceiptDetailsDto.getCashOpeingDate() != null ? paymentReceiptDetailsDto.getCashOpeingDate() : paymentReceiptDetailsDto.getCurrentDate();
			
			List<PaymentRecieptMasterDto> chequeList = sessionFactory.getCurrentSession().createSQLQuery(
					"select prm.reciept_number as \"recieptNumber\", "
					 		+"prd.payment_reciept_details_id as \"paymentRecieptDetailsId\", "
					 		+ "to_char(prm.created_date,'yyyy-MM-dd hh:mm:ss') as \"createdDate\", "
							//+ "to_char(prm.created_date,'YYYY-MM-DD') as \"createdDate\", "
							 + "concat(coalesce(pre.prefix_code,''),' ',coalesce(pr.first_name), "
							 + "coalesce(pr.last_name)) as \"patientName\", "
							 + "pr.uhid_number as \"uhidNumber\", "
							 + " (coalesce(prd.amount, 0) + coalesce(prd.available_fund, 0))  as \"amount\", "
							 + "prd.check_number as \"chequeNumber\", "
							 + "to_char(prd.cheque_date,'yyyy-MM-dd hh:mm:ss') as \"chequeDate\", "
							 + "prd.bank_id as \"bankId\", "
							 + "bnkm.bank_name as \"bankName\" "
							 +"from billing.t_payment_reciept_master prm "
							 + "inner join billing.t_payment_reciept_details prd on prm.payment_reciept_id=prd.payment_receipt_master_id "
							 + "inner join billing.m_billing_master bm on prm.billing_master_id=bm.billing_master_id and bm.bill_cancelled ='N'  "
							 + "inner join patient.t_patient_registration pr on bm.patient_id=pr.patient_id "
							 + "left join m_prefix_master pre on pre.prefix_id = pr.prefix_id "
							 + "left join billing.m_bank_master bnkm on bnkm.bank_id=prd.bank_id "
							// + "where prd.payment_mode_id=2 and prd.interim_cheque_status='A' and prm.date_of_reciept BETWEEN '"+paymentReceiptDetailsDto.getCashOpeingDate()+"' AND '"+currentDate+"' "
							 + "where prd.payment_mode_id=2 and prd.interim_cheque_status IS NULL and to_char(prm.date_of_reciept,'YYYY-MM-DD HH:mm:ss') BETWEEN :cashOpeingDate AND :currentDate "
							 +" and prm.orgnization_id =:orgnizationId and prm.unit_id =:unitId "
							 +" and prm.created_by =:createdBy order by reciept_number")
							.setParameter("cashOpeingDate", cashOpeingDate)
							.setParameter("currentDate", paymentReceiptDetailsDto.getCurrentDate())
							.setParameter("orgnizationId", paymentReceiptDetailsDto.getOrgnizationId())
							.setParameter("unitId", paymentReceiptDetailsDto.getUnitId())
							.setParameter("createdBy", paymentReceiptDetailsDto.getCreatedBy())
							.setResultTransformer(Transformers.aliasToBean(PaymentReceiptDetailsDto.class)).list();
			
		
			//System.out.println(chequeList);
		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, chequeList, null);
		
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
//		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getCardTransactionList(PaymentReceiptDetailsDto paymentDepositDetailsDto) {
		try {
			/*Date date = TimeZoneComponent.getDateByzone(unitId);  
			 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    String currentDate= formatter.format(date);*/
		    //System.out.print("paymentDepositDetailsDto.getCurrentDate()     "+paymentDepositDetailsDto.getCurrentDate());
			List<PaymentRecieptMasterDto> chequeList = sessionFactory.getCurrentSession().createSQLQuery(
					"select prd.card_number as \"cardNumber\", "
					 		+"prd.payment_reciept_details_id as \"paymentRecieptDetailsId\", "
							 +"ctm.card_type_name as \"cardName\", "
							 +"prd.transaction_no as \"transactionNo\", "
							 +"concat(coalesce(pre.prefix_code,''),' ',coalesce(pr.first_name), coalesce(pr.last_name)) as \"patientName\", "
							 +"pr.uhid_number as \"uhidNumber\", (coalesce(prd.amount, 0) + coalesce(prd.available_fund, 0))  as \"amount\" "
							 +"from billing.t_payment_reciept_master prm "
							 +"inner join billing.t_payment_reciept_details prd on prm.payment_reciept_id=prd.payment_receipt_master_id "
							 +"inner join billing.m_billing_master bm on prm.billing_master_id=bm.billing_master_id and bm.bill_cancelled ='N' "
							 +"inner join patient.t_patient_registration pr on bm.patient_id=pr.patient_id "
							 + "left join m_prefix_master pre on pre.prefix_id = pr.prefix_id "
							 + " left join billing.m_card_type_master ctm on ctm.card_type_id=prd.card_type_id "
							// + "where (prd.payment_mode_id=3 or prd.payment_mode_id=4) and 666666prd.interim_cheque_status='A' and prm.date_of_reciept BETWEEN '"+paymentDepositDetailsDto.getCashOpeingDate()+"' AND '"+currentDate+"' "
							+ "where (prd.payment_mode_id=3 or prd.payment_mode_id=4) and prd.interim_card_status IS NULL and prm.date_of_reciept BETWEEN :cashOpeingDate AND :currentDate "
							+" and prm.orgnization_id =:orgnizationId and prm.unit_id =:unitId "
							 +" and prm.created_by =:createdBy order by reciept_number")
							.setParameter("cashOpeingDate", paymentDepositDetailsDto.getCashOpeingDate())
							.setParameter("currentDate", paymentDepositDetailsDto.getCurrentDate())
							.setParameter("orgnizationId", paymentDepositDetailsDto.getOrgnizationId())
							.setParameter("unitId", paymentDepositDetailsDto.getUnitId())
							.setParameter("createdBy", paymentDepositDetailsDto.getCreatedBy())
							.setResultTransformer(Transformers.aliasToBean(PaymentReceiptDetailsDto.class)).list();
		
			//System.out.println(chequeList);
		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, chequeList, null);
		
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
//		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);

	}

	@Override
	public Response saveChequeInterimTransaction(InterimHandoverMasterDto interimHandoverMasterDto) {
		try {
			String ichString="ICH";
			char []ichNo = GlobalCommonDateUtils.generateRandomNumber(5);
			String ichNumber= ichString+String.valueOf(ichNo);
			//System.out.println("ichNumber"+ichNumber);
			InterimHandoverMaster interimMaster=new InterimHandoverMaster();
			interimMaster.setCashHandoverToId(interimHandoverMasterDto.getCashHandoverToId());
			interimMaster.setCashHandoverById(interimHandoverMasterDto.getCashHandoverById());
			interimMaster.setCashHandoverAmount(interimHandoverMasterDto.getCashHandoverAmount());
			interimMaster.setOrganizationId(interimHandoverMasterDto.getOrganizationId());
			interimMaster.setUnitId(interimHandoverMasterDto.getUnitId());
			interimMaster.setRemarks(interimHandoverMasterDto.getRemarks());
			interimMaster.setIchNumber(ichNumber);
			interimMaster.setModeOfTransaction(interimHandoverMasterDto.getModeOfTransaction());
			interimMaster.setCreatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(interimHandoverMasterDto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
			interimMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(interimHandoverMasterDto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
			interimMaster.setCreatedBy(interimHandoverMasterDto.getCashHandoverById());
			interimMaster.setUpdatedBy(interimHandoverMasterDto.getCashHandoverById());
			interimMaster.setStatus(interimHandoverMasterDto.getStatus());
			interimMaster =	save(interimMaster);
			
			
			
		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, interimMaster);
		
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
//		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response saveCardInterimTransaction(InterimHandoverMasterDto interimHandoverMasterDto) {
		try {
			String ichString="ICH";
			char []ichNo = GlobalCommonDateUtils.generateRandomNumber(5);
			String ichNumber= ichString+String.valueOf(ichNo);
			//System.out.println("ichNumber"+ichNumber);
			InterimHandoverMaster interimMaster=new InterimHandoverMaster();
			interimMaster.setCashHandoverToId(interimHandoverMasterDto.getCashHandoverToId());
			interimMaster.setCashHandoverById(interimHandoverMasterDto.getCashHandoverById());
			interimMaster.setCashHandoverAmount(interimHandoverMasterDto.getCashHandoverAmount());
			interimMaster.setOrganizationId(interimHandoverMasterDto.getOrganizationId());
			interimMaster.setUnitId(interimHandoverMasterDto.getUnitId());
			interimMaster.setRemarks(interimHandoverMasterDto.getRemarks());
			interimMaster.setIchNumber(ichNumber);
			interimMaster.setModeOfTransaction(interimHandoverMasterDto.getModeOfTransaction());
			interimMaster.setCreatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(interimHandoverMasterDto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
			interimMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(interimHandoverMasterDto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
			interimMaster.setCreatedBy(interimHandoverMasterDto.getCashHandoverById());
			interimMaster.setUpdatedBy(interimHandoverMasterDto.getCashHandoverById());
			interimMaster.setStatus(interimHandoverMasterDto.getStatus());
		    interimMaster =	save(interimMaster);
			
		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, interimMaster);
		
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
//		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateInterimChequeStatus(List<PaymentReceiptDetailsDto> paymentDepositDetailsDto) {
		try{
			
			//System.out.println(paymentDepositDetailsDto.size());
				for(PaymentReceiptDetailsDto listOfIds:paymentDepositDetailsDto)
					{
						Date date = TimeZoneComponent.getDateByzone(listOfIds.getUnitId());
						if(listOfIds.getPaymentRecieptDetailsId()!=0) {
								Integer result =SessionFactory.getCurrentSession().getNamedQuery("UPDATE_INTERIM_CHEQUE_STATUS")
								.setParameter("paymentRecieptDetailsId",listOfIds.getPaymentRecieptDetailsId())
								.setParameter("orgnizationId",listOfIds.getOrgnizationId())
								.setParameter("unitId", listOfIds.getUnitId())
								// .setString("interimChequeStatus",listOfIds.getInterimChequeStatus())
								.executeUpdate();
						}
					}	
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
	} catch (HibernateException he) {
		throw he;
	} catch (Exception e) {
		
		throw e;
	}
//	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	
	}

	@Override
	public Response updateInterimCardStatus(List<PaymentReceiptDetailsDto> paymentDepositDetailsDto) {
		try{
			
			//System.out.println(paymentDepositDetailsDto.size());
			for(PaymentReceiptDetailsDto listOfIds:paymentDepositDetailsDto)
			{
				Date date = TimeZoneComponent.getDateByzone(listOfIds.getUnitId());
				if(listOfIds.getPaymentRecieptDetailsId()!=0) {
						Integer result = SessionFactory.getCurrentSession().getNamedQuery("UPDATE_INTERIM_CARD_STATUS")
						.setParameter("paymentRecieptDetailsId",listOfIds.getPaymentRecieptDetailsId())
						.setParameter("orgnizationId",listOfIds.getOrgnizationId())
		                .setParameter("unitId", listOfIds.getUnitId()).executeUpdate();
				}
			}	
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
	} catch (HibernateException he) {
		throw he;
	} catch (Exception e) {
		
		throw e;
	}
//	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	
	}

	@Override
	public Response updateInterimStatusToComplete(InterimHandoverMasterDto interimHandoverMasterDto)
			throws ApplicationException {
		try {
			InterimHandoverMaster mstr = findById(interimHandoverMasterDto.getId());
			mstr.setStatusId(interimHandoverMasterDto.getStatusId());
			mstr.setUpdatedBy(interimHandoverMasterDto.getUpdatedBy());
			mstr.setUpdatedDate(GlobalCommonDateUtils.getDate(interimHandoverMasterDto.getUpdatedDate(),"dd-M-yyyy HH:ss:mm"));
			sessionFactory.getCurrentSession().update(mstr);
			
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		}catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
}
