package com.param.billing.global.transaction.dao;

import java.math.BigDecimal;
import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.billing.common.ICommonConstants;
import com.param.billing.common.IError;
import com.param.billing.common.Response;
import com.param.billing.global.transaction.dto.BillListDetailsCNDto;
import com.param.billing.global.transaction.model.BillDetailsPackageMapper;
import com.param.billing.global.transaction.model.BillDetailsPayeeMapper;
import com.param.billing.global.transaction.model.BillingDetails;
import com.param.global.billing.dto.BillingDetailsDto;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ServiceException;
import com.param.global.dto.OrderDetailsByEncounterIdDto;
import com.param.global.dto.OrderDetailsPackageMapperDto;
import com.param.global.dto.OrderDetailsPayeeMapperDto;
import com.param.utility.TimeZoneComponent;
import com.zaxxer.hikari.HikariDataSource;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "unchecked", "rawtypes" })
public class BillingDetailsDaoImpl extends GenericDao<BillingDetails, Integer> implements IBillingDetailsDao, ICommonConstants, IError{

	public BillingDetailsDaoImpl() {
		super(BillingDetails.class);
	}

	@Autowired
	private DozerBeanMapper mapper;
	
	@Autowired
	HikariDataSource driverManager;
	
	@Override
	public Response saveBillingDetails(List<BillingDetailsDto> billingDetailsDtoList) throws ApplicationException {
		try {
			
			List<BillDetailsPayeeMapper> billDetailsPayeeMappersList = new LinkedList<>();
			List<BillDetailsPackageMapper> billDetailsPackageMappersList = new LinkedList<>();
			for(BillingDetailsDto billingDetailsDto : billingDetailsDtoList)
			{
				BillingDetails billingDetails = mapper.map(billingDetailsDto, BillingDetails.class,"BillingDetailsDto_to_BillingDetails");
					billingDetails.setCreatedDate(TimeZoneComponent.getDateByzone(billingDetails.getUnitId()).getTime());
					billingDetails.setUpdatedDate(TimeZoneComponent.getDateByzone(billingDetails.getUnitId()).getTime());
					billingDetails.setCoPayPercentage(billingDetailsDto.getCoSharePer());
					billingDetails.setCoPayPer(billingDetailsDto.getCoSharePer());
				billingDetails = save(billingDetails);
			
				for(OrderDetailsPayeeMapperDto dto : billingDetailsDto.getOrderDetailsPayeeMapperDtosList())
				{
					BillDetailsPayeeMapper detailsPayeeMapper = new BillDetailsPayeeMapper();
						detailsPayeeMapper.setOrderDetailsId(billingDetails.getOrderDetailsId());
						detailsPayeeMapper.setBillDetailsId(billingDetails.getBillingDetailsId());
						detailsPayeeMapper.setContractId(dto.getContractId());
						detailsPayeeMapper.setPayeeId(dto.getPayeeId());
						detailsPayeeMapper.setAssociateCompanyId(dto.getAssociateCompanyId());
						detailsPayeeMapper.setPayeeAmount(dto.getPayeeAmount());
						detailsPayeeMapper.setDueAmount(dto.getPayeeAmount());
						detailsPayeeMapper.setServiceId(dto.getServiceId());
						detailsPayeeMapper.setOrganizationId(billingDetails.getOrganizationId());
						detailsPayeeMapper.setUnitId(billingDetails.getUnitId());
						detailsPayeeMapper.setStatus('A');
					//sessionFactory.getCurrentSession().save(detailsPayeeMapper);
					billDetailsPayeeMappersList.add(detailsPayeeMapper);
				}
				
				//For package
				if(billingDetailsDto.getOrderDetailsPackageMapperDtosList()!=null && billingDetailsDto.getOrderDetailsPackageMapperDtosList().size()>0)
				{
					for(OrderDetailsPackageMapperDto dto : billingDetailsDto.getOrderDetailsPackageMapperDtosList())
					{
						BillDetailsPackageMapper detailsPackageMapper = new BillDetailsPackageMapper();
							detailsPackageMapper.setBillDetailsId(billingDetails.getBillingDetailsId());
							detailsPackageMapper.setOrderDetailsId(billingDetails.getOrderDetailsId());
							detailsPackageMapper.setPayeeId(dto.getPayeeId());
							detailsPackageMapper.setPackageId(dto.getPackageId());
							detailsPackageMapper.setIsPackageService(dto.getIsPackageService());
							detailsPackageMapper.setIsCapService(dto.getIsCapService());
							detailsPackageMapper.setPackageAmount(dto.getPackageAmount());
							detailsPackageMapper.setDueAmount(BigDecimal.ZERO);
							detailsPackageMapper.setOrganizationId(billingDetails.getOrganizationId());
							detailsPackageMapper.setUnitId(billingDetails.getUnitId());
							detailsPackageMapper.setStatus('A');
						//sessionFactory.getCurrentSession().save(detailsPackageMapper);
						billDetailsPackageMappersList.add(detailsPackageMapper);
					}
				}
				else
				{
					BillDetailsPackageMapper detailsPackageMapper = new BillDetailsPackageMapper();
						detailsPackageMapper.setBillDetailsId(billingDetails.getBillingDetailsId());
						detailsPackageMapper.setOrderDetailsId(billingDetails.getOrderDetailsId());
						detailsPackageMapper.setPayeeId(billingDetailsDto.getPayeeId());
						detailsPackageMapper.setIsPackageService('N');
						detailsPackageMapper.setIsCapService('N');
						detailsPackageMapper.setPackageAmount(BigDecimal.ZERO);
						detailsPackageMapper.setDueAmount(BigDecimal.ZERO);
						detailsPackageMapper.setOrganizationId(billingDetails.getOrganizationId());
						detailsPackageMapper.setUnitId(billingDetails.getUnitId());
						detailsPackageMapper.setStatus('A');
					//sessionFactory.getCurrentSession().save(detailsPackageMapper);
					billDetailsPackageMappersList.add(detailsPackageMapper);
				}
			}
			
			batchInsertMappers(billDetailsPayeeMappersList,billDetailsPackageMappersList);
			
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	public Response getBillDetailsByBillId(int orgId, int unitId, int billId) throws ApplicationException {
		try {
			List<BillListDetailsCNDto> listBillListDetailsCNDto = sessionFactory.getCurrentSession().getNamedQuery("GET_BILL_DETAILS_BY_BILL_ID_CN")
					.setParameter("billId", billId)
					.setResultTransformer(Transformers.aliasToBean(BillListDetailsCNDto.class)).list();
		
		return new Response<>(SUCCESS, SUCCESS_CODE, null, listBillListDetailsCNDto, null);
	}catch (Exception e) {
		
		throw e;
	}
	//return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}
	
	
	@Override
	public Response updateBillDetailsIsDoctorSharePostedByBillId(List<Integer> billDtlsIds) throws ServiceException {
		try{
			sessionFactory.getCurrentSession().createSQLQuery("update billing.t_billing_details "
					+ "set is_doctor_share_posted='Y' "
					+ "where billing_details_id in (:billDtlsIds)")
			.setParameterList("billDtlsIds", billDtlsIds)
			.executeUpdate();
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		}catch(Exception e){
			
			throw e;
			//throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getBillingDetailsByBillId(BillingDetailsDto billingDetailsDto) throws ApplicationException {
		try {
			
			String query = " select "
					+ " 	billdt.billing_details_id as \"billingDetailsId\", "
					+ " 	billdt.order_details_id \"orderDetailsId\", "
					+ " 	billdt.service_id as \"serviceId\" , "
					+ " 	cast(mpr.payee_amount as double precision) as \"creditPayable\", "
					+ "		coalesce((select sum(coalesce(tcnd.cr_net_amount,0)+coalesce(tcnd.cr_tax_amount,0)) from billing.t_credit_note_detail tcnd "
					+ "		inner join billing.m_credit_note_master mcnm on mcnm.credit_note_id = tcnd.credit_note_id "
					+ "		where tcnd.order_detail_id = billdt.order_details_id "
					+ "		and mcnm.bill_payee_id = mpr.payee_id "
					+ "		and tcnd.status = 'A'), 0) as \"crNetAmount\" "
					+ " from "
					+ " 	billing.t_bill_details_payee_mapper mpr "
					+ " inner join billing.t_billing_details billdt on "
					+ " 	mpr.bill_details_id = billdt.billing_details_id "
					+ " where "
					+ " 	billdt.billing_master_id = :billingMasterId "
					+ " 	and mpr.payee_id =:payeeId ";
			if(billingDetailsDto.getAssociateCompanyId()!=null && billingDetailsDto.getAssociateCompanyId()>0)
				query = query .concat(" and mpr.associate_company_id =:associateCompanyId ");
			
			Query sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("billingMasterId", billingDetailsDto.getBillingMasterId())
					.setParameter("payeeId", billingDetailsDto.getPayeeId());

			if(billingDetailsDto.getAssociateCompanyId()!=null && billingDetailsDto.getAssociateCompanyId()>0)
				sqlQuery.setParameter("associateCompanyId", billingDetailsDto.getAssociateCompanyId());

			List<BillingDetailsDto> listBillListDetailsDto = sqlQuery
					.setResultTransformer(Transformers.aliasToBean(BillingDetailsDto.class)).list();
		
		return new Response<>(SUCCESS, SUCCESS_CODE, null, listBillListDetailsDto, null);
	}catch (Exception e) {
		
		throw e;
	}
	//return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	public Response getBillingDetailsByBillIdForInvoiceCreation(Integer billId,Integer payeeId) throws ApplicationException {
		try {
			List<BillingDetailsDto> listBillListDetailsDto = sessionFactory.getCurrentSession().getNamedQuery("GET_ORDER_DETAILS_BY_BILL_ID_FOR_COMAPNY_INVOICE")
					.setParameter("billingMasterId", billId)
					.setParameter("payeeId", payeeId)
					.setResultTransformer(Transformers.aliasToBean(BillingDetailsDto.class)).list();
		
		return new Response<>(SUCCESS, SUCCESS_CODE, null, listBillListDetailsDto, null);
	}catch (Exception e) {
		
		throw e;
	}
	//return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}
	
	@Override
	public Response getTransactionLogs(BillingDetailsDto billingDetailsDto)throws ApplicationException {
		try{
			Date fromDate = GlobalCommonDateUtils.getDate(billingDetailsDto.getFromDateTransaction(), "dd-MM-yyyy");
			Date toDate = GlobalCommonDateUtils.getDate(billingDetailsDto.getToDateTransaction(), "dd-MM-yyyy");
					
			List<BillingDetailsDto> listTransactionLogs = sessionFactory.getCurrentSession().getNamedQuery("GET_TRANSACTION_LOG_BY_RANGE").setParameter("fromDate", fromDate)
					.setParameter("toDate", toDate).setParameter("userId", billingDetailsDto.getCreatedUserId())
					//.setFirstResult(billingDetailsDto.getOffset() != null ? billingDetailsDto.getOffset() : 0)
					//.setMaxResults(billingDetailsDto.getNoOfRecords() != null ? billingDetailsDto.getNoOfRecords() : 10)
					.setResultTransformer(Transformers.aliasToBean(BillingDetailsDto.class)).list();
					
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listTransactionLogs, null);
		}catch(HibernateException he){
			throw he;
		}catch(Exception e){
			
			throw e;
		}
		//return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}
	
	public Response getBillServicesForAddendumBill(int billId) throws ApplicationException {
		try {
			List<OrderDetailsByEncounterIdDto> list = sessionFactory.getCurrentSession().getNamedQuery("GET_SERVICES_FOR_ADDENDUM_BILL").setParameter("billId", billId).setResultTransformer(Transformers.aliasToBean(OrderDetailsByEncounterIdDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);	
		}catch (Exception e) {
			
			throw e;
		}
	}
	@Override
	public List<BillingDetailsDto> getBillingDetailsByCreditNoteId(int creditNoteId) throws ApplicationException {
		try {
			List<BillingDetailsDto> billingDetailsDtoList = sessionFactory.getCurrentSession().getNamedQuery("GET_BILLING_DETAILS_BY_CREDIT_NOTE_ID")
					.setParameter("creditNoteId", creditNoteId)
					.setResultTransformer(Transformers.aliasToBean(BillingDetailsDto.class)).list();
			return billingDetailsDtoList;
		}catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response canceBillDetailsAndUpdateCrDetails(Integer billDetalilsId, Integer creditNoteId)
			throws ApplicationException {
		try {
			sessionFactory.getCurrentSession()
					.createQuery("UPDATE BillingDetails set status='I', isCancelled='Y',crNoteId =:creditNoteId "
							+ " WHERE billingDetailsId =:billDetalilsId ")
					.setParameter("creditNoteId", creditNoteId)
					.setParameter("billDetalilsId", billDetalilsId)
					.executeUpdate();
		
		return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
	}catch (Exception e) {
		
		throw e;
	}
	//return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}
	
	@Override
	public Response getBillDetailsObjByBillId(Integer billId)
			throws ApplicationException {
		try {
		
			Criteria c=sessionFactory.getCurrentSession().createCriteria(BillingDetails.class); 
			c.add(Restrictions.eq("billingMasterId", billId));
			c.add(Restrictions.eq("status", 'A'));
			List<BillingDetails> billDetailsList = c.list();
			
		return new Response<>(SUCCESS, SUCCESS_CODE, null, billDetailsList, null);
	}catch (Exception e) {
		
		throw e;
	}
	//return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}
	
	@Override
	public Response updateBillDetailsObj(BillingDetails billingDetails) 
			throws ApplicationException {
		try {
			sessionFactory.getCurrentSession().update(billingDetails);
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}
	
	
	private int batchInsertMappers(List<BillDetailsPayeeMapper> payeeList,List<BillDetailsPackageMapper> packageMprList) 
			throws Exception {

		Connection connection =null;
		PreparedStatement statement = null;
		PreparedStatement statementPayee = null;
		try {
			connection = driverManager.getConnection();
			connection.setAutoCommit(false);
			
			 statement = connection.prepareStatement("INSERT INTO billing.t_bill_details_package_mapper "
	        		+ "(bill_details_id, order_details_id, package_id, payee_id, is_package_service, is_cap_service, package_amount, due_amount, unit_id, organization_id, status) "
	        		+ "VALUES(?,?,?,?,?,?,?,?,?,?,'A');"); 
			 int i = 0;
			 int result[];
			 for(BillDetailsPackageMapper dto:packageMprList)
			 {
				 statement.setInt(1, dto.getBillDetailsId());
				 statement.setInt(2, dto.getOrderDetailsId());
				 if(dto.getPackageId()!=null && dto.getPackageId()>0)
					 statement.setInt(3, dto.getPackageId());
				 else
					 statement.setNull(3, Types.NULL);
				 
				 if(dto.getPayeeId()!=null && dto.getPayeeId()>0)
					 statement.setInt(4, dto.getPayeeId());
				 else
					 statement.setNull(4,Types.NULL);
				 statement.setObject(5, dto.getIsPackageService());
				 statement.setObject(6, dto.getIsCapService());
				 statement.setDouble(7, dto.getPackageAmount()!=null ? dto.getPackageAmount().doubleValue():0.00);
				 statement.setDouble(8, dto.getDueAmount() !=null ? dto.getDueAmount().doubleValue():0.00);
				 statement.setInt(9, dto.getUnitId());
				 statement.setInt(10,dto.getOrganizationId());
				 statement.addBatch();
				 i++;
				 if (i % 500 == 0 || i == packageMprList.size()) 
				 {
					try{
						result = statement.executeBatch(); // Execute every 20 items.
		                connection.commit();	
					}catch(BatchUpdateException e){
						throw e.getNextException();
					}
					   
				 }
			 }
			 
			 
			  statementPayee = connection.prepareStatement("INSERT INTO billing.t_bill_details_payee_mapper "
		        		+ "(bill_details_id, order_details_id, service_id, contract_id, payee_id, payee_amount, due_amount, unit_id, organization_id, status, associate_company_id) "
		        		+ "VALUES(?,?,?,?,?,?,?,?,?,'A',?);"); 
				 int j = 0;
				 int resultPackage[];
				 for(BillDetailsPayeeMapper dto:payeeList)
				 {
					 statementPayee.setInt(1, dto.getBillDetailsId());
					 statementPayee.setInt(2, dto.getOrderDetailsId());
					 statementPayee.setInt(3, dto.getServiceId());
					 if(dto.getContractId()!=null)
						 statementPayee.setInt(4, dto.getContractId());
					 else
						 statementPayee.setNull(4, Types.NULL);
					 
					 statementPayee.setInt(5, dto.getPayeeId());
					 statementPayee.setDouble(6, dto.getPayeeAmount()!=null ? dto.getPayeeAmount().doubleValue() : Types.NULL);
					 statementPayee.setDouble(7, dto.getPayeeAmount()!=null ? dto.getPayeeAmount().doubleValue() : Types.NULL);
					 statementPayee.setInt(8, dto.getUnitId());
					 statementPayee.setInt(9, dto.getOrganizationId());
					 if(dto.getAssociateCompanyId()!=null)
						 statementPayee.setInt(10, dto.getAssociateCompanyId());
					 else
						 statementPayee.setNull(10,Types.NULL);
					 
					 statementPayee.addBatch();
					 j++;
					 if (j % 500 == 0 || j == payeeList.size()) 
					 {
						resultPackage = statementPayee.executeBatch(); // Execute every 20 items.
		                connection.commit();  
					 }
				 }
			return 0;
		}catch (Exception e) {
			
			return 1;
		}finally{
			try{
				if (statement != null) {
					statement.close();
				}
				if (statementPayee != null) {
					statementPayee.close();
				}
				connection.setAutoCommit(true);
				connection.close();
			}catch(Exception e){
				
				throw e;
			}
		}
	}
	
	
}
