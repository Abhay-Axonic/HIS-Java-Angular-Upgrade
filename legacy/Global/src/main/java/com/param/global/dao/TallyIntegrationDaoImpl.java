package com.param.global.dao;

import java.math.BigDecimal;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.billing.global.transaction.config.model.UnitApplicationBillingConfiguration;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.TallyIntegrationDto;
import com.param.global.dto.UnitApplicationConfigurationDto;
import com.param.service.global.SessionService;

import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({"rawtypes", "unchecked"})
public class TallyIntegrationDaoImpl implements ITallyIntegrationDao, ICommonConstants{

	private static final Logger logger = Logger.getLogger(TallyIntegrationDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private SessionService sessionService;
	
	@Override
	public Response postVisitFaData(String date,List<Integer> unitIds) {
		try{
			Date faDate = GlobalCommonDateUtils.getDate(date, "dd-MM-yyyy");
			boolean flag = (boolean)sessionFactory.getCurrentSession().createSQLQuery("select post_visit_fa_data(:faDate,:unitIds)").setParameter("faDate", faDate)
					.setParameter("unitIds", unitIds.toString().replaceAll("\\[","").replaceAll("\\]","")).uniqueResult();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, flag);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response postDepositData(String date,List<Integer> unitIds) {
		try{
			Date faDate = GlobalCommonDateUtils.getDate(date, "dd-MM-yyyy");
			boolean flag = (boolean)sessionFactory.getCurrentSession().createSQLQuery("select post_deposit_data(:faDate,:unitIds)").setParameter("faDate", faDate)
					.setParameter("unitIds", unitIds.toString().replaceAll("\\[","").replaceAll("\\]","")).uniqueResult();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, flag);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response postDoctorShare(String date,List<Integer> unitIds) {
		try{
			Date faDate = GlobalCommonDateUtils.getDate(date, "dd-MM-yyyy");
			boolean flag = (boolean)sessionFactory.getCurrentSession().createSQLQuery("select post_doc_share_data(:faDate,:unitIds)").setParameter("faDate", faDate)
					.setParameter("unitIds", unitIds.toString().replaceAll("\\[","").replaceAll("\\]","")).uniqueResult();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, flag);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response postCreditNotes(String date,List<Integer> unitIds) {
		try{
			Date faDate = GlobalCommonDateUtils.getDate(date, "dd-MM-yyyy");
			boolean flag = (boolean)sessionFactory.getCurrentSession().createSQLQuery("select post_credit_notes(:faDate,:unitIds)").setParameter("faDate", faDate)
					.setParameter("unitIds", unitIds.toString().replaceAll("\\[","").replaceAll("\\]","")).uniqueResult();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, flag);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response postAdvanceRefund(String date,List<Integer> unitIds) {
		try{
			Date faDate = GlobalCommonDateUtils.getDate(date, "dd-MM-yyyy");
			boolean flag = (boolean)sessionFactory.getCurrentSession().createSQLQuery("select post_advance_refund(:faDate,:unitIds)").setParameter("faDate", faDate)
					.setParameter("unitIds", unitIds.toString().replaceAll("\\[","").replaceAll("\\]","")).uniqueResult();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, flag);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response postCompanySettlement(String date,List<Integer> unitIds) {
		try{
			Date faDate = GlobalCommonDateUtils.getDate(date, "dd-M-yyyy");
			boolean flag = (boolean)sessionFactory.getCurrentSession().createSQLQuery("select post_company_settlement(:faDate,:unitIds)").setParameter("faDate", faDate)
					.setParameter("unitIds", unitIds.toString().replaceAll("\\[","").replaceAll("\\]","")).uniqueResult();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, flag);
		}catch(Exception e){
			
			throw e;
		}
	}
	
	@Override
	public Response postSaleNotes(String date,List<Integer> unitIds) {
		try{
			Date faDate = GlobalCommonDateUtils.getDate(date, "dd-M-yyyy");
			boolean flag = (boolean)sessionFactory.getCurrentSession().createSQLQuery("select post_sale_notes(:faDate,:unitIds)").setParameter("faDate", faDate)
					.setParameter("unitIds", unitIds.toString().replaceAll("\\[","").replaceAll("\\]","")).uniqueResult();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, flag);
		}catch(Exception e){
			
			throw e;
		}
	}
	
	@Override
	public Response postGrnNotes(String date,List<Integer> unitIds) {
		try{
			Date faDate = GlobalCommonDateUtils.getDate(date, "dd-M-yyyy");
			boolean flag = (boolean)sessionFactory.getCurrentSession().createSQLQuery("select post_grn_notes(:faDate,:unitIds)").setParameter("faDate", faDate)
					.setParameter("unitIds", unitIds.toString().replaceAll("\\[","").replaceAll("\\]","")).uniqueResult();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, flag);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response postOPLegderCreditData(String date) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response postIpSelfServLedger(String date) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response postIpCompServLedger(String date) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response postIPBSelfCollectionData(String date) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response postIPBCompCollectionData(String date) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response postBillRefund(String date) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response postCOGSPurchaseAndSale(String date, List<Integer> unitIds) {
		try{
			Date faDate = GlobalCommonDateUtils.getDate(date, "dd-MM-yyyy");
			boolean flag = (boolean)sessionFactory.getCurrentSession().createSQLQuery("select post_sale_cogs(:faDate,:unitIds)").setParameter("faDate", faDate)
					.setParameter("unitIds", unitIds.toString().replaceAll("\\[","").replaceAll("\\]","")).uniqueResult();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, flag);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response inactiveTariffServiceBasedOnEffectiveDate() {
		try{
			boolean flag = (boolean)sessionFactory.getCurrentSession().createSQLQuery("select public.post_everyday()").uniqueResult();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, flag);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getIpLedger(TallyIntegrationDto tallyIntegrationDto) throws ApplicationException {
		try{
			Format formatter = new SimpleDateFormat("yyyy-MM-dd");
			String date = formatter.format(tallyIntegrationDto.getSearchDate());
			
					String sql="Select tti.ledger as \"ledger\", "
            		+ "tti.debit_amount as \"debit\", "
            		+ "tti.credit_amount as \"credit\", "
            		+ "tti.vou_narration as \"voucherNarration\","
            		+ "tti.voucher_no as \"voucherNo\", "
            		+ "tti.ref_no_type as \"reference\", "
            		+ "tti.voucher_type as \"voucherType\", "
            		+ "tti.amount as \"billingAmount\", "
            		+ "tti.instrument_number as \"instrumentNumber\","
            		+ "to_char(tti.instrument_date, 'DD/MM/YYYY') as \"instrumentDate\","
            		+ "to_char(tti.voucher_date, 'YYYYMMDD') as \"voucherDate\", "
            		+ "tti.unique_number as \"uniqueNumber\", "
            		+ "tti.bank_name as \"bankName\", "
            		+ "tti.ref_number as \"refNo\" "
            		+ "FROM public.t_tally_interface tti "
            		+ "WHERE date(tti.voucher_date)='"+ date +"' "
            		+ "AND tti.unit_id="+tallyIntegrationDto.getUnitId()+" "
            		+ "AND tti.tran_type='IP' "
            		+ "group by ledger, debit_amount, credit_amount, vou_narration, ref_no_type, amount, instrument_number, instrument_date, "
            		+ "voucher_date, unique_number, bank_name, ref_number, voucher_type, "
            		+ "voucher_no  having sum(debit_amount + credit_amount) <> 0 order by voucher_no asc";
					List<TallyIntegrationDto> list=sessionFactory.getCurrentSession().createSQLQuery(sql)
					.setResultTransformer(Transformers.aliasToBean(TallyIntegrationDto.class)).list();
				
			return new Response(SUCCESS, SUCCESS_CODE, TALLY_SUCCESS, list, null);

//		
		}
		catch (HibernateException he) {
			logger.error("getIpLedger failed", he);
		}
		catch (Exception e)
		{
			logger.error("getIpLedger failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getOpLedger(TallyIntegrationDto tallyIntegrationDto) throws ApplicationException {
		try{
			Format formatter = new SimpleDateFormat("yyyy-MM-dd");
			String date = formatter.format(tallyIntegrationDto.getSearchDate());
					String sqlOp="Select tti.ledger as \"ledger\", "
		            		+ "tti.debit_amount as \"debit\", "
		            		+ "tti.credit_amount as \"credit\", "
		            		+ "tti.vou_narration as \"voucherNarration\","
		            		+ "tti.voucher_no as \"voucherNo\", "
		            		+ "tti.ref_no_type as \"reference\", "
		            		+ "tti.voucher_type as \"voucherType\", "
		            		+ "tti.amount as \"billingAmount\", "
		            		+ "tti.instrument_number as \"instrumentNumber\","
		            		+ "to_char(tti.instrument_date, 'DD/MM/YYYY') as \"instrumentDate\","
		            		+ "to_char(tti.voucher_date, 'YYYYMMDD') as \"voucherDate\", "
		            		+ "tti.unique_number as \"uniqueNumber\", "
		            		+ "tti.bank_name as \"bankName\", "
		            		+ "tti.ref_number as \"refNo\" "
		            		+ "FROM public.t_tally_interface tti "
		            		+ "WHERE date(tti.voucher_date)='"+ date +"' "
		            		+ "AND tti.unit_id="+tallyIntegrationDto.getUnitId()+" "
		            		+ "AND tti.tran_type='OP' "
		            		+ "group by ledger, debit_amount, credit_amount, vou_narration, ref_no_type, amount, instrument_number, instrument_date, "
		            		+ "voucher_date, unique_number, bank_name, ref_number, voucher_type, "
		            		+ "voucher_no  having sum(debit_amount + credit_amount) <> 0 order by voucher_no asc";
						List<TallyIntegrationDto> list=sessionFactory.getCurrentSession().createSQLQuery(sqlOp)
						.setResultTransformer(Transformers.aliasToBean(TallyIntegrationDto.class)).list();
						return new Response(SUCCESS, SUCCESS_CODE, TALLY_SUCCESS, list, null);

//		
		}
		catch (HibernateException he) {
			logger.error("getOpLedger failed", he);
		}
		catch (Exception e)
		{
			logger.error("getOpLedger failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getInvetoryLedger(TallyIntegrationDto tallyIntegrationDto) throws ApplicationException {
		try{
			Format formatter = new SimpleDateFormat("yyyy-MM-dd");
			String date = formatter.format(tallyIntegrationDto.getSearchDate());
					String sql2="Select tti.ledger as \"ledger\", "
		            		+ "tti.debit_amount as \"debit\", "
		            		+ "tti.credit_amount as \"credit\", "
		            		+ "tti.vou_narration as \"voucherNarration\","
		            		+ "tti.voucher_no as \"voucherNo\", "
		            		+ "tti.ref_no_type as \"reference\", "
		            		+ "tti.voucher_type as \"voucherType\", "
		            		+ "tti.amount as \"billingAmount\", "
		            		+ "tti.instrument_number as \"instrumentNumber\","
		            		+ "to_char(tti.instrument_date, 'DD/MM/YYYY') as \"instrumentDate\","
		            		+ "to_char(tti.voucher_date, 'YYYYMMDD') as \"voucherDate\", "
		            		+ "tti.unique_number as \"uniqueNumber\", "
		            		+ "tti.bank_name as \"bankName\", "
		            		+ "tti.ref_number as \"refNo\" "
		            		+ "FROM public.t_tally_interface tti "
		            		+ "WHERE date(tti.voucher_date)='"+ date +"' "
		            		+ "AND tti.unit_id="+tallyIntegrationDto.getUnitId()+" "
            		+ "AND tti.tran_type='Inventory' "
            		+ "group by ledger, debit_amount, credit_amount, vou_narration, ref_no_type, amount, instrument_number, instrument_date, "
            		+ "voucher_date, unique_number, bank_name, ref_number, voucher_type, "
            		+ "voucher_no  having sum(debit_amount + credit_amount) <> 0 order by voucher_no asc";
					List<TallyIntegrationDto> list=sessionFactory.getCurrentSession().createSQLQuery(sql2)
					.setResultTransformer(Transformers.aliasToBean(TallyIntegrationDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, TALLY_SUCCESS, list, null);

//		
		}
		catch (HibernateException he) {
			logger.error("getInvetoryLedger failed", he);
		}
		catch (Exception e)
		{
			logger.error("getInvetoryLedger failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@Override
	public Response getDoctorShareLedger(TallyIntegrationDto tallyIntegrationDto) throws ApplicationException {
		try{
			Format formatter = new SimpleDateFormat("yyyy-MM-dd");
			String date = formatter.format(tallyIntegrationDto.getSearchDate());
					String sql1="Select tti.ledger as \"ledger\", "
		            		+ "tti.debit_amount as \"debit\", "
		            		+ "tti.credit_amount as \"credit\", "
		            		+ "tti.vou_narration as \"voucherNarration\","
		            		+ "tti.voucher_no as \"voucherNo\", "
		            		+ "tti.ref_no_type as \"reference\", "
		            		+ "tti.voucher_type as \"voucherType\", "
		            		+ "tti.amount as \"billingAmount\", "
		            		+ "tti.instrument_number as \"instrumentNumber\","
		            		+ "to_char(tti.instrument_date, 'DD/MM/YYYY') as \"instrumentDate\","
		            		+ "to_char(tti.voucher_date, 'YYYYMMDD') as \"voucherDate\", "
		            		+ "tti.unique_number as \"uniqueNumber\", "
		            		+ "tti.bank_name as \"bankName\", "
		            		+ "tti.ref_number as \"refNo\" "
		            		+ "FROM public.t_tally_interface tti "
		            		+ "WHERE date(tti.voucher_date)='"+ date +"' "
		            		+ "AND tti.unit_id="+tallyIntegrationDto.getUnitId()+" "
            		+ "AND tti.tran_type='Doctor Share' "
            		+ "group by ledger, debit_amount, credit_amount, vou_narration, ref_no_type, amount, instrument_number, instrument_date, "
            		+ "voucher_date, unique_number, bank_name, ref_number, voucher_type, "
            		+ "voucher_no  having sum(debit_amount + credit_amount) <> 0 order by voucher_no asc";
					List<TallyIntegrationDto> list=sessionFactory.getCurrentSession().createSQLQuery(sql1)
					.setResultTransformer(Transformers.aliasToBean(TallyIntegrationDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, TALLY_SUCCESS, list, null);

//		
		}
		catch (HibernateException he) {
			logger.error("getDoctorShareLedger failed", he);
		}
		catch (Exception e)
		{
			logger.error("getDoctorShareLedger failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	
	
	@Override
	public Response getAllLedgersNameFromHis(TallyIntegrationDto tallyIntegrationDto) throws ApplicationException {
		try{
			Format formatter = new SimpleDateFormat("yyyy-MM-dd");
			String date = formatter.format(tallyIntegrationDto.getSearchDate());
			
					String sql="Select distinct tti.ledger as \"ledger\" "
            		+ " FROM public.t_tally_interface tti "
            		+ " WHERE tti.unit_id="+tallyIntegrationDto.getUnitId()+" "
					+ " and tti.voucher_date = '" + date + "' "
            		+ " order by 1 asc";
					List<TallyIntegrationDto> list=sessionFactory.getCurrentSession().createSQLQuery(sql)
					.setResultTransformer(Transformers.aliasToBean(TallyIntegrationDto.class)).list();
				
			return new Response(SUCCESS, SUCCESS_CODE, TALLY_SUCCESS, list, null);

//		
		}
		catch (HibernateException he) {
			logger.error("getAllLedgersNameFromHis failed", he);
		}
		catch (Exception e)
		{
			logger.error("getAllLedgersNameFromHis failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response postDailyStock(List<Integer> unitIds) throws ApplicationException {
		try{
			boolean flag = (boolean)sessionFactory.getCurrentSession().createSQLQuery("select public.insertDailyStock(:unitIds)").setParameter("unitIds", unitIds.toString().replaceAll("\\[","").replaceAll("\\]","")).uniqueResult();

			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, flag);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response icuUtilization(List<Integer> unitIds) throws ApplicationException {
		try{
			boolean flag =false;
			for(Integer unitId :unitIds){
				 flag = (boolean)sessionFactory.getCurrentSession().createSQLQuery("select public.icu_utilization(:unitId)")
						.setParameter("unitId",unitId).uniqueResult();
			}

			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, flag);
		}catch(Exception e){

			throw e;
		}
	}
	

	/*@Override
	public Response postIPBSelfCollectionData(String date) {
		try{
			Date faDate = GlobalCommonDateUtils.getDate(date, "dd-M-yyyy");
			boolean flag = (boolean)sessionFactory.getCurrentSession().createSQLQuery("select postIPBSelfCollectionData(:faDate)").setParameter("faDate", faDate).uniqueResult();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, flag);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response postIPBCompCollectionData(String date) {
		try{
			Date faDate = GlobalCommonDateUtils.getDate(date, "dd-M-yyyy");
			boolean flag = (boolean)sessionFactory.getCurrentSession().createSQLQuery("select postIPBCompCollectionData(:faDate)").setParameter("faDate", faDate).uniqueResult();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, flag);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response postAdvanceRefund(String date) {
		try{
			Date faDate = GlobalCommonDateUtils.getDate(date, "dd-M-yyyy");
			boolean flag = (boolean)sessionFactory.getCurrentSession().createSQLQuery("select post_advance_refund(:faDate)").setParameter("faDate", faDate).uniqueResult();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, flag);
		}catch(Exception e){
			
			throw e;
		}
	}
	
	@Override
	public Response postBillRefund(String date) {
		try{
			Date faDate = GlobalCommonDateUtils.getDate(date, "dd-M-yyyy");
			boolean flag = (boolean)sessionFactory.getCurrentSession().createSQLQuery("select postBillRefund(:faDate)").setParameter("faDate", faDate).uniqueResult();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, flag);
		}catch(Exception e){

			throw e;
		}
	}*/

	@Override
	public Response callProcedureTallyMismatch() throws ApplicationException {
		try{
			/*String queryString = "CALL fix_tally_mismatches()";
			Integer updatedCnt = sessionFactory.getCurrentSession()
					.createSQLQuery(queryString)
					.executeUpdate();*/

			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		}catch(Exception e){

			throw e;
		}
	}

	@Override
	public Response getTallyInterfaceCommunicationKeys(TallyIntegrationDto tallyIntegrationDto) throws ApplicationException {
		try{
			String sql="Select uabc.tally_ip as \"tallyIp\", "
					+ " uabc.tally_port_no as \"tallyPortNo\", "
					+ " uabc.is_tally_local_host as \"isTallyLocalHost\" "
					+ " FROM public.t_unit_application_billing_configuration uabc "
					+ " WHERE uabc.unit_id =:unitId "
					+ " and uabc.organization_id =:organizationId ";

			TallyIntegrationDto tallyDto = (TallyIntegrationDto) sessionFactory.getCurrentSession().createSQLQuery(sql)
					.setParameter("unitId", tallyIntegrationDto.getUnitId())
					.setParameter("organizationId", tallyIntegrationDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(TallyIntegrationDto.class)).uniqueResult();

			return new Response(SUCCESS, SUCCESS_CODE, TALLY_SUCCESS, null, tallyDto);
		}
		catch (HibernateException he) {
			logger.error("getTallyInterfaceCommunicationKeys failed", he);
		}
		catch (Exception e)
		{
			logger.error("getTallyInterfaceCommunicationKeys failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
}
