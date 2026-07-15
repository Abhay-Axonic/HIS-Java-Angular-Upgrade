package com.param.billing.cashDesk.dao;

import java.util.List;

import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.billing.cashDesk.dto.CashHandoverDetailsDto;
import com.param.billing.global.model.CashHandoverMaster;
import com.param.billing.global.model.CashInOut;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.otims.dto.ProcedureOrderMasterDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
@Repository
public class CashInOutDaoImpl extends GenericDao<CashInOut,Integer> implements ICashInOutDao,ICommonConstants{

	public CashInOutDaoImpl() {
		super(CashInOut.class);
	}

	@Autowired
	private SessionFactory SessionFactory;
	

	@Autowired
	private DozerBeanMapper mapper;


	
	@Override
	public Response saveCashInOut(CashHandoverDetailsDto cashHandoverDetailsDto) throws ApplicationException {
		try {
			
			CashInOut cashHandoverMaster = new CashInOut();
			
			cashHandoverMaster.setDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(cashHandoverDetailsDto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
			cashHandoverMaster.setCashHandoverById(cashHandoverDetailsDto.getCashHandoverById());		
			cashHandoverMaster.setCashHandoverToId(cashHandoverDetailsDto.getCashHandoverToId());		
			cashHandoverMaster.setCashHandoverAmount(cashHandoverDetailsDto.getCashHandoverAmount());
			cashHandoverMaster.setRemarks(cashHandoverDetailsDto.getRemarks());
			cashHandoverMaster.setOrganizationId(cashHandoverDetailsDto.getOrganizationId());
			cashHandoverMaster.setUnitId(cashHandoverDetailsDto.getUnitId());
			cashHandoverMaster.setCreatedDate(GlobalCommonDateUtils.getDate(cashHandoverDetailsDto.getDate(), "dd-M-yyyy HH:mm:ss"));
			cashHandoverMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(cashHandoverDetailsDto.getDate(), "dd-M-yyyy HH:mm:ss"));
			cashHandoverMaster.setCreatedBy(cashHandoverDetailsDto.getCashHandoverById());
			cashHandoverMaster.setUpdatedBy(cashHandoverDetailsDto.getCashHandoverById());
			cashHandoverMaster.setStatus("A");
			cashHandoverMaster.setCashTransactionTypeId(cashHandoverDetailsDto.getCashTransactionTypeId());
			cashHandoverMaster.setHospitalAccountId(cashHandoverDetailsDto.getHospitalAccountId() != null ? cashHandoverDetailsDto.getHospitalAccountId() : null);
			
				String cobString="CIO";
				char[] otp =GlobalCommonDateUtils.generateRandomNumber(8);
				String cob=cobString+String.valueOf(otp);
				cashHandoverMaster.setCobNumber(cob);
				SessionFactory.getCurrentSession().save(cashHandoverMaster);
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, cashHandoverMaster);
			
			
		} catch (Exception e) {
			
			throw e;
		}
	
	}



	@Override
	public Response getcashInOutList(CashHandoverDetailsDto cashHandoverDetailsDto) throws ApplicationException {

		try {
			
			String query = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CASH_IN_OUT_LIST")
					.getQueryString();
		
			StringBuffer subQuery = new StringBuffer();
			
			if(cashHandoverDetailsDto.getCashHandoverToId()!=0){
				subQuery.append(" AND cashHandoverMaster.cashHandoverToId =:cashHandoverToId ");
			}
			
			if(cashHandoverDetailsDto.getCashHandoverById()!=0){
				subQuery.append(" AND cashHandoverMaster.cashHandoverById =:cashHandoverById ");
			}
			
			if(cashHandoverDetailsDto.getCobNumber()!="" && cashHandoverDetailsDto.getCobNumber()!=null){
				subQuery.append(" AND cashHandoverMaster.cobNumber =:cobNumber ");
			}
			
			subQuery.append(" order by cashHandoverMaster.id desc");
			query = query.concat(subQuery.toString());
			
			Query sqlQuery = sessionFactory.getCurrentSession()
					.createQuery(query)
					.setParameter("organizationId",  cashHandoverDetailsDto.getOrganizationId())
					.setParameter("unitId",  cashHandoverDetailsDto.getUnitId())
					.setParameter("fromDate",  cashHandoverDetailsDto.getFromDate())
					.setParameter("toDate",cashHandoverDetailsDto.getToDate());

			if(cashHandoverDetailsDto.getCashHandoverToId()!=0)
				sqlQuery.setParameter("cashHandoverToId", cashHandoverDetailsDto.getCashHandoverToId());

			if(cashHandoverDetailsDto.getCashHandoverById()!=0)
				sqlQuery.setParameter("cashHandoverById", cashHandoverDetailsDto.getCashHandoverById());

			if(cashHandoverDetailsDto.getCobNumber()!="" && cashHandoverDetailsDto.getCobNumber()!=null)
				sqlQuery.setParameter("cobNumber", cashHandoverDetailsDto.getCobNumber());

			List<CashHandoverDetailsDto> list =	sqlQuery.setResultTransformer(Transformers.aliasToBean(CashHandoverDetailsDto.class)).list();
		return new Response(SUCCESS, SUCCESS_CODE, null,list, null);
		
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
	
	}

	@Override
	public Response getUserInhandCash(CashHandoverDetailsDto cashHandoverDetailsDto) throws ApplicationException {
		try
        {
			List<CashHandoverDetailsDto> cashInhandList = sessionFactory.getCurrentSession().createSQLQuery(
				  " select "
				  +" 1 as \"sr\","
				  +" 'Deposit' as \"lebel\","
				  +"  cast(tpdd.amount as float8)  as \"debitAmount\","
				  +"  cast(0 as float8) as \"creditAmount\" "
				  +" FROM"
				  +" billing.m_deposit_master mdm"
				  +" join billing.t_payment_deposit_details tpdd on mdm.deposit_master_id = tpdd.deposit_id"
				  +" join m_payment_mode_master mpmm on mpmm.payment_mode_id = tpdd.payment_mode_id"
				  +" join m_user_master um on um.user_id = mdm.created_by"
				  +" left join m_employee_master emp on emp.user_id = um.user_id"
				  +" left join patient.t_patient_registration pr on pr.patient_id = mdm.patient_id"
				  +" where"
				  +" um.user_id = :userId "
				  +" and mdm.unit_id = :unitId "
				  +" and to_char( mdm.deposit_date, 'YYYY-MM-DD')= :currDate "
				  + " and mpmm.payment_mode_id in(1,7) "
				  +"   union all "
				  +" select"
				  +" 2 as \"sr\","
				  +" 'Deposit Refund' as \"lebel\","
				  +" cast(0 as float8) as \"debitAmount\","
				  +" cast(tprd.amount as float8) as \"creditAmount\" "
				  +" from"
				  +" billing.t_refund_cn trc"
				  +" join billing.t_payment_refund_master tprm on tprm.t_refund_cn_id = trc.t_refund_cn_id"
				  +" join billing.t_payment_refund_details tprd on tprd.payment_refund_id = tprm.payment_refund_id"
				  +" join m_payment_mode_master mpmm on mpmm.payment_mode_id = tprd.payment_mode_id"
				  +" join billing.m_deposit_master mdm on trc.deposit_id = mdm.deposit_master_id"
				  +" join billing.m_deposite_type_master mdtm on mdtm.deposite_type_id = mdm.deposit_type_id"
				  +" join m_user_master um on um.user_id = tprm.created_by"
				  +" left join m_employee_master emp on emp.user_id = um.user_id"
				  +" left join patient.t_patient_registration pr on pr.patient_id = mdm.patient_id"
				+" where"
				+"   um.user_id =:userId "
				+"  and trc.unit_id = :unitId "
				+"   and to_char( trc.refund_cn_date, 'YYYY-MM-DD')= :currDate "
				+ " and mpmm.payment_mode_id = 1 and tprd.payment_mode_id <> 7 "
				+"   union all "
				+" select "
				+"  3 as \"sr\","
				+"  'Credit Note Refund' as \"lebel\","
				+"  cast(0 as float8) as \"debitAmount\","
				+"  cast(brdet.amount as float8) as \"creditAmount\" "
				+" FROM "
				+"  billing.t_payment_refund_details brdet"
				+"  JOIN billing.t_payment_refund_master bm ON bm.payment_refund_id = brdet.payment_refund_id"
				+"  JOIN billing.m_credit_note_master bcnid ON bm.credit_note_id = bcnid.credit_note_id"
				+"  JOIN billing.m_billing_master billmst ON billmst.billing_master_id = bcnid.bill_id"
				+"  join m_user_master um on um.user_id = brdet.created_by"
				+"  left join m_employee_master emp on emp.user_id = um.user_id"
				+"  left join patient.t_patient_registration pr on pr.patient_id = billmst.patient_id"
				+" where"
				+"  um.user_id = :userId"
				+"  and billmst.unit_id = :unitId "
				+"  and to_char( brdet.created_date, 'YYYY-MM-DD')= :currDate "
				+ " and brdet.payment_mode_id in(1,7) "
				+"   union all "
				+" select"
				+"  4 as \"sr\","
				+"  'Receipt' as \"lebel\","
				+"  cast(tprd.amount  as float8) as \"debitAmount\","
				+"  cast(0 as float8) as \"creditAmount\" "
				+" from"
				+"  billing.t_payment_reciept_master tprm"
				+"  join billing.t_payment_reciept_details tprd on tprm.payment_reciept_id = tprd.payment_receipt_master_id"
				+"  and reciept_cancelled = 'N'"
				 +" join m_payment_mode_master mpmm on mpmm.payment_mode_id = tprd.payment_mode_id"
				 +" and tprd.payment_mode_id <> 10"
				 +" join billing.m_billing_master mbm on mbm.billing_master_id = tprm.billing_master_id"
				+"  join m_user_master um on um.user_id = tprm.created_by"
				+"  left join m_employee_master emp on emp.user_id = um.user_id"
				+"  left join patient.t_patient_registration pr on pr.patient_id = mbm.patient_id"
				+" where"
				+"  um.user_id = :userId"
				+"  and mbm.unit_id =:unitId  "
				+"  and to_char(tprm.date_of_reciept, 'YYYY-MM-DD')= :currDate "
				+ " and mpmm.payment_mode_id = 1 "
				+"  union all "
				+" select"
				 +" 5 as \"sr\","
				 +" 'Transfer In' as \"lebel\","
				 +" cast(handove1.cash_handover_amount as float8)  as \"debitAmount\","
				+"  cast(0 as float8) as \"creditAmount\" "
				+" FROM"
				+"  t_cash_in_out handove1"
				+"  join m_user_master um on um.user_id = handove1.cash_handover_to_id"
				+"  left join m_employee_master emp on emp.user_id = um.user_id"
				+"  join m_user_master umTo on umTo.user_id = handove1.cash_handover_by_id"
				+"  left join m_employee_master empTo on empTo.user_id = umTo.user_id"
				+" left join billing.m_hospital_accounts ac on ac.hospital_account_id = handove1.hospital_account_id"
				+" where"
				+"   um.user_id =:userId"
				+"  and handove1.unit_id = :unitId "
				+"  and to_char(handove1.created_date, 'YYYY-MM-DD')= :currDate "
				+"  and handove1.cash_transaction_type_id in (1, 2)"
				+"  union all  "
				+" select "
				+"  6 as \"sr\","
				+"  'Transfer Out' as \"lebel\","
				+"  cast(0 as float8) as \"debitAmount\","
				+"  cast(handove1.cash_handover_amount as float8)  as \"creditAmount\" "
				+" FROM "
				+"  t_cash_in_out handove1"
				+"  join m_user_master um on um.user_id = handove1.cash_handover_by_id"
				+"  left join m_employee_master emp on emp.user_id = um.user_id"
				+"  join m_user_master umTo on umTo.user_id = handove1.cash_handover_to_id"
				+"  left join m_employee_master empTo on empTo.user_id = umTo.user_id"
				+"  left join billing.m_hospital_accounts ac on ac.hospital_account_id = handove1.hospital_account_id"
				+" where "
				 +" um.user_id = :userId"
				+"  and handove1.unit_id =:unitId "
				 +"  and to_char(handove1.created_date, 'YYYY-MM-DD')= :currDate "
				 +" and handove1.cash_transaction_type_id in (2, 3)"
				+"  union all "
				+" select"
				+"  7 as \"sr\","
				+"  'Pharmacy Sale' as \"lebel\","
				+" cast(tprm.amount  as float8) as \"debitAmount\","
				+" cast(0 as float8) as \"creditAmount\" "
				+" from "
				+"  pharmacy.t_sale_payment_mode tprm"
				+"  join m_user_master um on um.user_id = tprm.added_by"
				+"  left join m_employee_master emp on emp.user_id = um.user_id"
				+" where "
				+"  um.user_id =:userId"
				+"  and tprm.unit_id =:unitId  "
				+"  and to_char(tprm.added_date, 'YYYY-MM-DD')= :currDate "
				 +" and tprm.is_active = true"
				 + " and tprm.payment_mode_id = 1 "
				+"  union all  "
				+" select "
				+"  8 as \"sr\","
				+"  'Sale Return' as \"lebel\","
				+"  cast(0 as float8) as \"debitAmount\","
				+"  cast(rd.amount as float8)  as \"creditAmount\"  "
				+" from "
				+"  billing.t_payment_refund_master tprm"
				+"  join billing.t_payment_refund_details rd on tprm.payment_refund_id = rd.payment_refund_id"
				+"  join m_user_master um on um.user_id = tprm.created_by"
				+"  left join m_employee_master emp on emp.user_id = um.user_id"
				+" where "
				+"   um.user_id = :userId "
				+"  and tprm.unit_id = :unitId "
				+"  and to_char(tprm.created_date, 'YYYY-MM-DD')= :currDate " 
				 + " and rd.payment_mode_id = 1 "
				 +" and tprm.status = 'A'"
				 + " and tprm.t_pharmacy_return_id is not null ")
					.setParameter("userId", cashHandoverDetailsDto.getUserId())
					.setParameter("unitId", cashHandoverDetailsDto.getUnitId())
					.setParameter("currDate", cashHandoverDetailsDto.getDate())
				.setResultTransformer(Transformers.aliasToBean(CashHandoverDetailsDto.class)).list();
				return new Response<>(SUCCESS, SUCCESS_CODE, null, cashInhandList, null);
        } catch (Exception e)
        {
            
        }
        return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	
	}
}
