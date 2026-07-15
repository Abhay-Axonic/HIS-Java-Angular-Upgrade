package com.param.global.dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

import com.param.global.dto.*;
import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.param.billing.global.ConvertServicesFromOptoIpDto;
import com.param.billing.global.model.OrderDetailsLogAfterUpdatePayee;
import com.param.billing.global.transaction.model.RequestDiscountDetails;
import com.param.entity.model.master.FreeTextOrderedServices;
import com.param.global.billing.controller.BillingException;
import com.param.global.billing.dto.RequestDiscountDetailsDto;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.model.OrderDetailsMaster;
import com.param.global.model.OrderDetailsPayeeMapper;
import com.param.global.otims.dto.ProcedureOrderMasterDto;
import com.param.opd.encounter.dto.EncounterMasterDto;
import com.param.service.global.SessionService;
import com.param.service.global.model.OrderDetailsPackageMapper;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import javax.transaction.Transactional;

@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class OrderDetailsMasterDaoImpl extends GenericDao<OrderDetailsMaster, Integer> implements IOrderDetailsMasterDao, ICommonConstants{

	private static final Logger logger = Logger.getLogger(OrderDetailsMasterDaoImpl.class);

	public OrderDetailsMasterDaoImpl() {
		super(OrderDetailsMaster.class);
	}

	@Autowired
	private DozerBeanMapper mapper;
	
    @Autowired
    SessionFactory sessionFactory;
    
    
    @Autowired
	SessionService sessionService;

    
    @Autowired
    HikariDataSource driverManager;
	
	@Autowired
	IUnitApplicationBillingConfigurationDao iUnitApplicationBillingConfigurationDao;

	@Override
	public Response saveOrderDetailsMaster(OrderDetailsMasterDto orderDetailsMasterDto) throws ApplicationException {
		try {
			OrderDetailsMaster orderDetailsMaster = mapper.map(orderDetailsMasterDto, OrderDetailsMaster.class, "OrderDetailsMasterDto_to_OrderDetailsMaster");
				orderDetailsMaster.setDummyServiceAmt(orderDetailsMaster.getDummyServiceAmt()!=null ? orderDetailsMaster.getDummyServiceAmt() : orderDetailsMaster.getServiceAmt());
				orderDetailsMaster.setDebitNoteService(orderDetailsMasterDto.getDebitNoteService()==true?true:false);
				orderDetailsMaster.setIsFromAddservice(orderDetailsMasterDto.getIsFromAddservice()!=null ? orderDetailsMasterDto.getIsFromAddservice() : 'N');
				orderDetailsMaster.setNursingServiceOrderFlag(orderDetailsMasterDto.getNursingServiceOrderFlag()!=null ? orderDetailsMasterDto.getNursingServiceOrderFlag() : 'N');
				orderDetailsMaster.setIsOrderVisibleDepartment(orderDetailsMasterDto.getIsOrderVisibleDepartment()!=null  ? orderDetailsMasterDto.getIsOrderVisibleDepartment() : 'A');
				orderDetailsMaster.setOrdDocId(orderDetailsMasterDto.getOrdDocId()!=null && orderDetailsMasterDto.getOrdDocId()>0 ? orderDetailsMasterDto.getOrdDocId() : null);
				orderDetailsMaster.setOrdDocSplId(orderDetailsMasterDto.getOrdDocSplId()!=null && orderDetailsMasterDto.getOrdDocSplId()>0 ? orderDetailsMasterDto.getOrdDocSplId() :  orderDetailsMasterDto.getOrdDocSplId());
				orderDetailsMaster.setOrderSetId(orderDetailsMasterDto.getOrderSetId()!=null ? orderDetailsMasterDto.getOrderSetId() :null);
				orderDetailsMaster.setCreatedDate(orderDetailsMasterDto.getOrderDetailsId()!=null && orderDetailsMasterDto.getOrderDetailsId()>0 ? orderDetailsMasterDto.getCreatedDate() : TimeZoneComponent.getDateByzone(orderDetailsMasterDto.getOrgUnitId()).getTime());
				orderDetailsMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(orderDetailsMasterDto.getOrgUnitId()).getTime());
				
				if(orderDetailsMasterDto.getCoPayPer()!=null && (orderDetailsMasterDto.getCreditPayable()!=null && orderDetailsMasterDto.getCreditPayable().compareTo(BigDecimal.ZERO)==1))
					orderDetailsMaster.setCoPayPer(orderDetailsMasterDto.getCoPayPer()); 
				else {
					if(orderDetailsMasterDto.getOrderDetailsPayeeMapperDtosList()!=null && orderDetailsMasterDto.getOrderDetailsPayeeMapperDtosList().size()>1)
						orderDetailsMaster.setCoPayPer(new BigDecimal((orderDetailsMasterDto.getSelfPayable().doubleValue()/orderDetailsMasterDto.getNetAmount().doubleValue())*100));
					else
						orderDetailsMaster.setCoPayPer(new BigDecimal(100));
				}

			orderDetailsMaster.setIsCapService(orderDetailsMaster.getIsCapService()=='\u0000'?'N':orderDetailsMaster.getIsCapService());
			//orderDetailsMaster.setIsPackageService(orderDetailsMaster.getIsPackageService()=='\u0000'?'N':orderDetailsMaster.getIsPackageService());
			orderDetailsMaster.setIsProcedure(orderDetailsMaster.getIsProcedure()=='\u0000'?'N':orderDetailsMaster.getIsProcedure());
			orderDetailsMaster.setIsProcedureService(orderDetailsMaster.getIsProcedureService()=='\u0000'?'N':orderDetailsMaster.getIsProcedureService());


				orderDetailsMaster=save(orderDetailsMaster);
			Integer orderDetailsId = orderDetailsMaster.getOrderDetailsId();
			if(orderDetailsMasterDto.getOrderDetailsPayeeMapperDtosList()!=null && orderDetailsMasterDto.getOrderDetailsPayeeMapperDtosList().size()>0)
			{
				sessionFactory.getCurrentSession().createQuery(" delete from OrderDetailsPayeeMapper WHERE orderDetailsId in (:id)")
				.setParameter("id", orderDetailsId).executeUpdate();
		
				for(OrderDetailsPayeeMapperDto dto : orderDetailsMasterDto.getOrderDetailsPayeeMapperDtosList())
				{
					OrderDetailsPayeeMapper detailsPayeeMapper = new OrderDetailsPayeeMapper();
						detailsPayeeMapper.setOrderDetailsId(orderDetailsId);
						detailsPayeeMapper.setContractId(dto.getContractId());
						detailsPayeeMapper.setPayeeId(dto.getPayeeId());
						detailsPayeeMapper.setAssociateCompanyId(dto.getAssociateCompanyId());
						detailsPayeeMapper.setPayeeAmount(dto.getPayeeAmount());
						detailsPayeeMapper.setSplitBillAmount(dto.getPayeeAmount());
						detailsPayeeMapper.setServiceId(dto.getServiceId());
						detailsPayeeMapper.setOrganizationId(orderDetailsMasterDto.getOrgId());
						detailsPayeeMapper.setUnitId(orderDetailsMasterDto.getOrgUnitId());
						detailsPayeeMapper.setStatus('A');
					sessionFactory.getCurrentSession().save(detailsPayeeMapper);	
				}
			}
			
			
			//For package
			if(orderDetailsMasterDto.getOrderDetailsPackageMapperDtosList()!=null && orderDetailsMasterDto.getOrderDetailsPackageMapperDtosList().size()>0)
			{
				sessionFactory.getCurrentSession().createQuery(" delete from OrderDetailsPackageMapper WHERE orderDetailsId  in (:ids) ")
					.setParameter("ids", orderDetailsId).executeUpdate();
				
				for(OrderDetailsPackageMapperDto dto : orderDetailsMasterDto.getOrderDetailsPackageMapperDtosList())
				{
					OrderDetailsPackageMapper detailsPackageMapper = new OrderDetailsPackageMapper();
						detailsPackageMapper.setOrderDetailsId(orderDetailsId);
						detailsPackageMapper.setPayeeId(dto.getPayeeId());
						detailsPackageMapper.setPackageId(dto.getPackageId());
						detailsPackageMapper.setIsPackageService(dto.getIsPackageService());
						detailsPackageMapper.setIsCapService(dto.getIsCapService());
						detailsPackageMapper.setPackageAmount(dto.getPackageAmount());
						detailsPackageMapper.setServiceId(dto.getServiceId());
						detailsPackageMapper.setPackageQty(dto.getPackageQty());
						detailsPackageMapper.setServiceAmount(dto.getServiceAmount());
						detailsPackageMapper.setOrganizationId(orderDetailsMasterDto.getOrgId());
						detailsPackageMapper.setPacakgeConsumptionServiceDetailId(dto.getPacakgeConsumptionServiceDetailId());
						detailsPackageMapper.setPackageConsumptionCapDetailsId(dto.getPackageConsumptionCapDetailsId());
						detailsPackageMapper.setPackageConsumptionMasterId(dto.getPackageConsumptionMasterId());
						detailsPackageMapper.setPackageConsumptionEODetailId(dto.getPackageConsumptionEODetailId());
						detailsPackageMapper.setUnitId(orderDetailsMasterDto.getOrgUnitId());
						detailsPackageMapper.setStatus('A');
					sessionFactory.getCurrentSession().save(detailsPackageMapper);
				}
			}
			else
			{
				OrderDetailsPackageMapper detailsPackageMapper = new OrderDetailsPackageMapper();
					detailsPackageMapper.setOrderDetailsId(orderDetailsId);
					detailsPackageMapper.setIsPackageService('N');
					detailsPackageMapper.setIsCapService('N');
					detailsPackageMapper.setPackageAmount(BigDecimal.ZERO);
					detailsPackageMapper.setServiceId(orderDetailsMaster.getServiceId());
					detailsPackageMapper.setServiceAmount(orderDetailsMaster.getServiceAmt());
					detailsPackageMapper.setOrganizationId(orderDetailsMasterDto.getOrgId());
					detailsPackageMapper.setUnitId(orderDetailsMasterDto.getOrgUnitId());
					detailsPackageMapper.setStatus('A');
				sessionFactory.getCurrentSession().save(detailsPackageMapper);
			}
			
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, orderDetailsId);
		}catch (Exception e) {
			logger.error("saveOrderDetailsMaster failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateIsBilledStatus(Set<Integer> orderDetailsList, Integer billMasterId, List<Integer> pharmacyOrderIdList) throws ApplicationException {
		try {

			sessionFactory.getCurrentSession().getNamedQuery("UPDATE_IS_BILLED_STATUS")
				.setParameterList("listOrderDetailsId", orderDetailsList)
				.setParameter("serviceBillId",billMasterId )
				.executeUpdate();

			if(pharmacyOrderIdList!=null && pharmacyOrderIdList.size()>0)
			{
				sessionFactory.getCurrentSession().createQuery(" UPDATE OrderDetailsMaster SET serviceIsBilled=1 "
        		+ " WHERE orderId IN (:pharmacyOrderIdList) AND saleDetailId is not null")
				.setParameterList("pharmacyOrderIdList", pharmacyOrderIdList)
				.executeUpdate();
			}

			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		}catch(Exception e) {
			logger.error("updateIsBilledStatus failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateOrderDetails(OrderDetailsMasterDto orderDetailsMasterDto,Character isFinalBill,Integer selfPayeeId) throws ApplicationException {
		try {	
			
			OrderDetailsMaster orderDetailsMaster = mapper.map(orderDetailsMasterDto, OrderDetailsMaster.class, "OrderDetailsMasterDto_to_OrderDetailsMaster");
			orderDetailsMaster.setIsFromAddservice(orderDetailsMasterDto.getIsFromAddservice()!=null ? orderDetailsMasterDto.getIsFromAddservice() : 'N');
			orderDetailsMaster.setNursingServiceOrderFlag(orderDetailsMasterDto.getNursingServiceOrderFlag()!=null ? orderDetailsMasterDto.getNursingServiceOrderFlag() : 'N');
			orderDetailsMaster.setOrdDocId(orderDetailsMasterDto.getOrdDocId()!=null && orderDetailsMasterDto.getOrdDocId()>0 ? orderDetailsMasterDto.getOrdDocId() : null);
			orderDetailsMaster.setIsOrderVisibleDepartment(orderDetailsMasterDto.getIsOrderVisibleDepartment()!=null  ? orderDetailsMasterDto.getIsOrderVisibleDepartment() : 'A');
			orderDetailsMaster.setOrdDocSplId(orderDetailsMasterDto.getOrdDocSplId()!=null && orderDetailsMasterDto.getOrdDocSplId()>0 ? orderDetailsMasterDto.getOrdDocSplId() : null);
			
			if(orderDetailsMasterDto.getCoPayPer()!=null && orderDetailsMasterDto.getCreditPayable().compareTo(BigDecimal.ZERO)==1)
				orderDetailsMaster.setCoPayPer(orderDetailsMasterDto.getCoPayPer()); 
			else {
				if(orderDetailsMasterDto.getOrderDetailsPayeeMapperDtosList().size()>1)
					orderDetailsMaster.setCoPayPer(new BigDecimal((orderDetailsMasterDto.getSelfPayable().doubleValue()/orderDetailsMasterDto.getNetAmount().doubleValue())*100));
				else
					orderDetailsMaster.setCoPayPer(BigDecimal.ZERO);
			}

			orderDetailsMaster.setIsCapService(orderDetailsMaster.getIsCapService()=='\u0000'?'N':orderDetailsMaster.getIsCapService());
			//orderDetailsMaster.setIsPackageService(orderDetailsMaster.getIsPackageService()=='\u0000'?'N':orderDetailsMaster.getIsPackageService());
			orderDetailsMaster.setIsProcedure(orderDetailsMaster.getIsProcedure()=='\u0000'?'N':orderDetailsMaster.getIsProcedure());
			orderDetailsMaster.setIsProcedureService(orderDetailsMaster.getIsProcedureService()=='\u0000'?'N':orderDetailsMaster.getIsProcedureService());
			sessionFactory.getCurrentSession().update(orderDetailsMaster);
			
			sessionFactory.getCurrentSession().createQuery(" delete from OrderDetailsPayeeMapper WHERE orderDetailsId =:orderDetailsId ")
					.setParameter("orderDetailsId",orderDetailsMasterDto.getOrderDetailsId()).executeUpdate();
	
			//for package
			sessionFactory.getCurrentSession().createQuery(" delete from OrderDetailsPackageMapper WHERE orderDetailsId =:orderDetailsId ")
					.setParameter("orderDetailsId",orderDetailsMasterDto.getOrderDetailsId()).executeUpdate();
	
			if(orderDetailsMasterDto.getOrderDetailsPayeeMapperDtosList()!=null && orderDetailsMasterDto.getOrderDetailsPayeeMapperDtosList().size()>0)
			{
				for(OrderDetailsPayeeMapperDto dto : orderDetailsMasterDto.getOrderDetailsPayeeMapperDtosList())
				{
					if ((!selfPayeeId.equals(dto.getPayeeId()) 
							&& (dto.getPayeeAmount()!=null && !(dto.getPayeeAmount().compareTo(BigDecimal.ZERO)==0)))
								|| selfPayeeId.equals(dto.getPayeeId()))
					{
						OrderDetailsPayeeMapper detailsPayeeMapper = new OrderDetailsPayeeMapper();
						//detailsPayeeMapper.setId(dto.getId());
							detailsPayeeMapper.setOrderDetailsId(orderDetailsMaster.getOrderDetailsId());
							detailsPayeeMapper.setContractId(dto.getContractId());
							detailsPayeeMapper.setPayeeId(dto.getPayeeId());
							detailsPayeeMapper.setAssociateCompanyId(dto.getAssociateCompanyId());
							detailsPayeeMapper.setPayeeAmount(dto.getPayeeAmount());
							detailsPayeeMapper.setSplitBillAmount(dto.getPayeeAmount());
							detailsPayeeMapper.setServiceId(orderDetailsMasterDto.getServiceId());
							detailsPayeeMapper.setOrganizationId(orderDetailsMasterDto.getOrgId());
							detailsPayeeMapper.setUnitId(orderDetailsMasterDto.getOrgUnitId());
							if(orderDetailsMaster.getStatus()=='A')
								detailsPayeeMapper.setStatus('A');
							else
								detailsPayeeMapper.setStatus('I');
						sessionFactory.getCurrentSession().save(detailsPayeeMapper);
					}
				}
			}
			//For package
			if(orderDetailsMasterDto.getOrderDetailsPackageMapperDtosList()!=null && orderDetailsMasterDto.getOrderDetailsPackageMapperDtosList().size()>0)
			{
				for(OrderDetailsPackageMapperDto dto : orderDetailsMasterDto.getOrderDetailsPackageMapperDtosList())
				{
					OrderDetailsPackageMapper detailsPackageMapper = new OrderDetailsPackageMapper();
					detailsPackageMapper.setOrderDetailsId(orderDetailsMaster.getOrderDetailsId());
					detailsPackageMapper.setPayeeId(dto.getPayeeId());
					detailsPackageMapper.setPackageId(dto.getPackageId());
					detailsPackageMapper.setIsPackageService(dto.getIsPackageService());
					detailsPackageMapper.setIsCapService(dto.getIsCapService());
					detailsPackageMapper.setPackageAmount(dto.getPackageAmount());
					detailsPackageMapper.setServiceId(orderDetailsMasterDto.getServiceId());
					detailsPackageMapper.setServiceAmount(dto.getServiceAmount());
					detailsPackageMapper.setOrganizationId(orderDetailsMasterDto.getOrgId());
					detailsPackageMapper.setUnitId(orderDetailsMasterDto.getOrgUnitId());
					detailsPackageMapper.setServiceAmount(dto.getServiceAmount());
					detailsPackageMapper.setOrganizationId(orderDetailsMasterDto.getOrgId());
					detailsPackageMapper.setPacakgeConsumptionServiceDetailId(dto.getPacakgeConsumptionServiceDetailId());
					detailsPackageMapper.setPackageConsumptionCapDetailsId(dto.getPackageConsumptionCapDetailsId());
					detailsPackageMapper.setPackageConsumptionMasterId(dto.getPackageConsumptionMasterId());
					detailsPackageMapper.setPackageConsumptionEODetailId(dto.getPackageConsumptionEODetailId());
					detailsPackageMapper.setPackageQty(dto.getPackageQty());
					detailsPackageMapper.setStatus('A');
					sessionFactory.getCurrentSession().save(detailsPackageMapper);
				}
			}
			else
			{
				OrderDetailsPackageMapper detailsPackageMapper = new OrderDetailsPackageMapper();
					detailsPackageMapper.setOrderDetailsId(orderDetailsMaster.getOrderDetailsId());
					detailsPackageMapper.setIsPackageService('N');
					detailsPackageMapper.setIsCapService('N');
					detailsPackageMapper.setPackageAmount(BigDecimal.ZERO);
					detailsPackageMapper.setServiceId(orderDetailsMaster.getServiceId());
					detailsPackageMapper.setServiceAmount(orderDetailsMaster.getServiceAmt());
					detailsPackageMapper.setOrganizationId(orderDetailsMasterDto.getOrgId());
					detailsPackageMapper.setUnitId(orderDetailsMasterDto.getOrgUnitId());
					detailsPackageMapper.setPackageQty(orderDetailsMaster.getOrderQty());
					detailsPackageMapper.setStatus('A');
				sessionFactory.getCurrentSession().save(detailsPackageMapper);
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, orderDetailsMaster);
		}catch(Exception e) {
		logger.error("updateOrderDetails failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response updateOrderDetailsBetterPerformance(List<OrderDetailsMasterDto> orderDetailsMasterDtoList,Integer selfPayeeId) throws ApplicationException {
		Connection connection = null;
		Statement statement = null;
		PreparedStatement packageStatement = null;
		PreparedStatement payeeStatement = null;

		try {
			connection = driverManager.getConnection();
			connection.setAutoCommit(false);

			 statement = connection.createStatement();
			int i = 0;

			List<OrderDetailsPayeeMapper> orderDetailsPayeeMapperDtoFinalList = new LinkedList<>();
			List<OrderDetailsPackageMapper> orderDetailspackageMapperDtoFinalList = new LinkedList<>();
			List<Integer> orderDetailsIds = new LinkedList<>();

			for(OrderDetailsMasterDto orderDetailsMasterDto : orderDetailsMasterDtoList)
			{
				double coPay = 100.00;
				if(orderDetailsMasterDto.getOrderDetailsPayeeMapperDtosList() != null && orderDetailsMasterDto.getOrderDetailsPayeeMapperDtosList().size()>1 && orderDetailsMasterDto.getSelfPayable().doubleValue()>0)
					coPay = (orderDetailsMasterDto.getSelfPayable().doubleValue()/orderDetailsMasterDto.getNetAmount().doubleValue())*100;

				if(orderDetailsMasterDto.getPackageConsumptionAmt().equals(orderDetailsMasterDto.getOrdTotalAmt())){

					orderDetailsMasterDto.setNetAmount(BigDecimal.ZERO);

				}


				String sql = "update "
						+ " 	public.t_order_details "
						+ " set "
						+ " 	updated_by = "+orderDetailsMasterDto.getUpdatedBy()+", "
						+ " 	updated_date = '"+GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(orderDetailsMasterDto.getOrgUnitId()), "yyyy-MM-dd HH:mm:ss")+"', "
						+ " 	order_qty = "+orderDetailsMasterDto.getOrderQty()+", "
						+ " 	service_amt ="+orderDetailsMasterDto.getServiceAmt()+", "
						+ " 	ord_concession = "+orderDetailsMasterDto.getOrdConcession()+", "
						+ " 	ord_discount = "+orderDetailsMasterDto.getOrdDiscount()+", "
						+ " 	net_amount = "+orderDetailsMasterDto.getNetAmount()+", "
						+ " 	tariff_id = "+orderDetailsMasterDto.getTariffId()+", "
						//+ " 	bed_id = "+orderDetailsMasterDto.getBedId()+", "
						// + " 	room_id ="+orderDetailsMasterDto.getRoomId()+", "
						// + " 	ward_id = "+orderDetailsMasterDto.getWardId()+", "
						+ " 	package_id = "+orderDetailsMasterDto.getPackageId()+", "
						+ " 	ord_doc_id = "+orderDetailsMasterDto.getOrdDocId()+", "
						+ " 	drug_id = "+orderDetailsMasterDto.getDrugId()+", "
						+ " 	batch_id = "+orderDetailsMasterDto.getBatchId()+", "
						+ " 	service_rendered = "+orderDetailsMasterDto.getServiceRendered()+", "
						+ " 	service_rendering_deptid = "+orderDetailsMasterDto.getServiceRenderingDeptId()+", "
						//+ " 	ord_render_datetime = '"+GlobalCommonDateUtils.getStringDate(orderDetailsMasterDto.getOrdRenderDatetime(), "yyyy-MM-dd HH:mm:ss")+"', "
						+ " 	status = '"+orderDetailsMasterDto.getStatus()+"', "
						+ " 	service_is_billed = "+orderDetailsMasterDto.getServiceIsBilled()+", "
						+ " 	is_drug = '"+orderDetailsMasterDto.getIsDrug()+"', "
						+ " 	ord_doc_spl_id = "+orderDetailsMasterDto.getOrdDocSplId()+", "
						+ " 	tax_id = "+orderDetailsMasterDto.getTaxId()+", "
						+ " 	tax_per = "+orderDetailsMasterDto.getTaxPer()+", "
						+ " 	tax_amount = "+orderDetailsMasterDto.getTaxAmount()+", "
						+ " 	self_payable = "+orderDetailsMasterDto.getSelfPayable()+", "
						+ " 	credit_payable = "+orderDetailsMasterDto.getCreditPayable()+", "
						+ " 	payee_id = "+orderDetailsMasterDto.getPayeeId()+", "
						+ " 	package_consumption_amt = "+orderDetailsMasterDto.getPackageConsumptionAmt()+", "
						+ " 	contract_id = "+orderDetailsMasterDto.getContractId()+", "
						+ " 	bed_category_id = "+orderDetailsMasterDto.getBedCategoryId()+", "
						+ " 	co_pay_per = "+coPay+", "
						+ " 	doc_share_per = "+orderDetailsMasterDto.getDocSharePer()+", "
						+ " 	ord_total_amt = "+orderDetailsMasterDto.getOrdTotalAmt()+", "
						+ " 	ord_cancel_by = "+orderDetailsMasterDto.getOrdCancelBy()+", "
						+ " 	ord_cancel_remark = '"+orderDetailsMasterDto.getOrdCancelRemark()+"', "
						+ " 	is_package = "+orderDetailsMasterDto.isPackage()+", "
						+ " 	is_cap_service = '"+orderDetailsMasterDto.getIsCapService()+"', "
						+ " 	is_package_service = '"+orderDetailsMasterDto.getIsPackageService()+"', "
						//+ " 	is_investigation_order = '"+orderDetailsMasterDto.getIsInvestigationOrder()+"', "
						+ " 	is_posted_while_discharge = '"+orderDetailsMasterDto.getIsPostedWhileDischarge()+"', "
						+ " 	is_procedure = '"+(orderDetailsMasterDto.getIsProcedure()!='\0' ? orderDetailsMasterDto.getIsProcedure() : 'N')+"', "
						+ " 	is_procedure_service = '"+orderDetailsMasterDto.getIsProcedureService()+"', "
						+ " 	procedure_order_mst_id = "+orderDetailsMasterDto.getProcedureOrderMstId()+", "
						//+ " 	is_appointable_radiology = '"+orderDetailsMasterDto.getIsAppointableRadiology()+"', "
						+ " 	package_either_or_group_id = "+orderDetailsMasterDto.getPackageEitherOrGroupId()+", "
						+ " 	sale_detail_id = "+orderDetailsMasterDto.getSaleDetailId()+", "
						+ " 	markup_status = "+orderDetailsMasterDto.getMarkupStatus()+", "
						+ " 	patient_class_percentage = "+orderDetailsMasterDto.getPatientClassPercentage()+", "
						+ " 	debit_note_service = "+orderDetailsMasterDto.getDebitNoteService()+", "
						//+ " 	is_from_addservice = '"+orderDetailsMasterDto.getIsFromAddservice()+"', "
						+ " 	dummy_service_amt = "+orderDetailsMasterDto.getDummyServiceAmt()+", "
						+ " 	ref_doc_share_per = "+orderDetailsMasterDto.getRefDocSharePer()+", "
						+ " 	force_add_removed = "+orderDetailsMasterDto.getForceAddRemoved()+", "
						+ " 	is_consumed = "+orderDetailsMasterDto.getIsConsumed()+", "
						+ " 	is_order_visible_dept = '"+orderDetailsMasterDto.getIsOrderVisibleDepartment()+"', "
						//+ " 	nursing_service_order_flag = '"+orderDetailsMasterDto.getNursingServiceOrderFlag()+"', "
						+ " 	discount_category_id = "+orderDetailsMasterDto.getDiscountCategoryId()+", "
						+ " 	discount_ownership = "+orderDetailsMasterDto.getDiscountOwnership()+", "
						+ " 	hospital_share = "+orderDetailsMasterDto.getHospitalShare()+", "
						+ " 	ord_cancelled = '"+orderDetailsMasterDto.getOrdCancelled()+"', "
						+ " 	is_dummy_service = "+orderDetailsMasterDto.getIsDummyService()+", "
						+ " 	is_hidden_service = "+orderDetailsMasterDto.getIsHiddenService()+", "
						+ " 	ord_cancel_reason_id = "+orderDetailsMasterDto.getOrdCancelReasonId()+", "
						+ " 	ord_cancel_datetime = '"+GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(orderDetailsMasterDto.getOrgUnitId()), "yyyy-MM-dd HH:mm:ss")+"' "
						+ " where "
						+ " 	order_details_id = "+orderDetailsMasterDto.getOrderDetailsId()+"; ";

				statement.addBatch(sql);

				if(orderDetailsMasterDto.getOrderDetailsPayeeMapperDtosList()!=null && orderDetailsMasterDto.getOrderDetailsPayeeMapperDtosList().size()>0)
				{
					for(OrderDetailsPayeeMapperDto orderDetailsPayeeMapperDto : orderDetailsMasterDto.getOrderDetailsPayeeMapperDtosList())
					{
						if ((selfPayeeId!=null && !selfPayeeId.equals(orderDetailsPayeeMapperDto.getPayeeId())
								&& (orderDetailsPayeeMapperDto.getPayeeAmount()!=null && !(orderDetailsPayeeMapperDto.getPayeeAmount().compareTo(BigDecimal.ZERO)==0)))
								|| selfPayeeId.equals(orderDetailsPayeeMapperDto.getPayeeId()))
						{
							OrderDetailsPayeeMapper detailsPayeeMapper = new OrderDetailsPayeeMapper();
							detailsPayeeMapper.setOrderDetailsId(orderDetailsMasterDto.getOrderDetailsId());
							detailsPayeeMapper.setContractId(orderDetailsPayeeMapperDto.getContractId());
							detailsPayeeMapper.setPayeeId(orderDetailsPayeeMapperDto.getPayeeId());
							detailsPayeeMapper.setAssociateCompanyId(orderDetailsPayeeMapperDto.getAssociateCompanyId());
							detailsPayeeMapper.setPayeeAmount(orderDetailsPayeeMapperDto.getPayeeAmount());
							detailsPayeeMapper.setSplitBillAmount(orderDetailsPayeeMapperDto.getPayeeAmount());
							detailsPayeeMapper.setServiceId(orderDetailsMasterDto.getServiceId());
							detailsPayeeMapper.setOrganizationId(orderDetailsMasterDto.getOrgId());
							detailsPayeeMapper.setUnitId(orderDetailsMasterDto.getOrgUnitId());
							if(orderDetailsMasterDto.getStatus()=='A')
								detailsPayeeMapper.setStatus('A');
							else
								detailsPayeeMapper.setStatus('I');

							orderDetailsPayeeMapperDtoFinalList.add(detailsPayeeMapper);
						}
					}
				}
				//For package
				if(orderDetailsMasterDto.getOrderDetailsPackageMapperDtosList()!=null && orderDetailsMasterDto.getOrderDetailsPackageMapperDtosList().size()>0)
				{
					for(OrderDetailsPackageMapperDto orderDetailsPackageMapperDto : orderDetailsMasterDto.getOrderDetailsPackageMapperDtosList())
					{
						OrderDetailsPackageMapper detailsPackageMapper = new OrderDetailsPackageMapper();
						detailsPackageMapper.setOrderDetailsId(orderDetailsMasterDto.getOrderDetailsId());
						detailsPackageMapper.setPayeeId(orderDetailsPackageMapperDto.getPayeeId());
						detailsPackageMapper.setPackageId(orderDetailsPackageMapperDto.getPackageId());
						detailsPackageMapper.setIsPackageService(orderDetailsPackageMapperDto.getIsPackageService()!=null ? orderDetailsPackageMapperDto.getIsPackageService() : 'N');
						detailsPackageMapper.setIsCapService(orderDetailsPackageMapperDto.getIsCapService()!=null ? orderDetailsPackageMapperDto.getIsCapService():'N');
						detailsPackageMapper.setPackageAmount(orderDetailsPackageMapperDto.getPackageAmount());
						detailsPackageMapper.setServiceId(orderDetailsMasterDto.getServiceId());
						detailsPackageMapper.setServiceAmount(orderDetailsPackageMapperDto.getServiceAmount());
						detailsPackageMapper.setOrganizationId(orderDetailsMasterDto.getOrgId());
						detailsPackageMapper.setUnitId(orderDetailsMasterDto.getOrgUnitId());
						detailsPackageMapper.setServiceAmount(orderDetailsPackageMapperDto.getServiceAmount());
						detailsPackageMapper.setOrganizationId(orderDetailsMasterDto.getOrgId());
						detailsPackageMapper.setPacakgeConsumptionServiceDetailId(orderDetailsPackageMapperDto.getPacakgeConsumptionServiceDetailId());
						detailsPackageMapper.setPackageConsumptionCapDetailsId(orderDetailsPackageMapperDto.getPackageConsumptionCapDetailsId());
						detailsPackageMapper.setPackageConsumptionMasterId(orderDetailsPackageMapperDto.getPackageConsumptionMasterId());
						detailsPackageMapper.setPackageConsumptionEODetailId(orderDetailsPackageMapperDto.getPackageConsumptionEODetailId());
						detailsPackageMapper.setPackageQty(orderDetailsPackageMapperDto.getPackageQty());
						detailsPackageMapper.setStatus('A');
						orderDetailspackageMapperDtoFinalList.add(detailsPackageMapper);
					}
				}
				else
				{
					OrderDetailsPackageMapper detailsPackageMapper = new OrderDetailsPackageMapper();
					detailsPackageMapper.setOrderDetailsId(orderDetailsMasterDto.getOrderDetailsId());
					detailsPackageMapper.setIsPackageService('N');
					detailsPackageMapper.setIsCapService('N');
					detailsPackageMapper.setPackageAmount(BigDecimal.ZERO);
					detailsPackageMapper.setServiceId(orderDetailsMasterDto.getServiceId());
					detailsPackageMapper.setServiceAmount(orderDetailsMasterDto.getServiceAmt());
					detailsPackageMapper.setOrganizationId(orderDetailsMasterDto.getOrgId());
					detailsPackageMapper.setUnitId(orderDetailsMasterDto.getOrgUnitId());
					detailsPackageMapper.setPackageQty(orderDetailsMasterDto.getOrderQty());
					detailsPackageMapper.setStatus('A');
					orderDetailspackageMapperDtoFinalList.add(detailsPackageMapper);
				}
				orderDetailsIds.add(orderDetailsMasterDto.getOrderDetailsId());
				i++;
			}

			sessionFactory.getCurrentSession().createQuery(" delete from OrderDetailsPayeeMapper WHERE orderDetailsId in (:ids)")
					.setParameterList("ids", orderDetailsIds).executeUpdate();

			//for package
			sessionFactory.getCurrentSession().createQuery(" delete from OrderDetailsPackageMapper WHERE orderDetailsId  in (:ids) ")
					.setParameterList("ids", orderDetailsIds).executeUpdate();


			if (i == orderDetailsMasterDtoList.size())
			{
				//System.out.println(i);
				try{
					statement.executeBatch(); // Execute every 20 items.
					connection.commit();
				}catch(BatchUpdateException e){
				logger.error("updateOrderDetailsBetterPerformance batch execute failed (order details statement)", e);
				}

			}

			 packageStatement = connection.prepareStatement("INSERT INTO public.t_order_details_package_mapper "
					+ "(order_details_id, service_id, package_id, is_package_service, is_cap_service, payee_id, service_amount, package_amount, unit_id, organization_id, status, package_consumption_master_id, package_consumption_cap_details_id, pacakge_consumption_service_detail_id, package_qty, package_consumtion_eo_detail_id) "
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,'A',?,?,?,?,?);");
			int j = 0;
			for(OrderDetailsPackageMapper dto:orderDetailspackageMapperDtoFinalList)
			{
				packageStatement.setInt(1, dto.getOrderDetailsId());
				packageStatement.setInt(2, dto.getServiceId());

				if(dto.getPackageId()!=null && dto.getPackageId()>0)
					packageStatement.setInt(3, dto.getPackageId());
				else
					packageStatement.setNull(3, Types.NULL);

				packageStatement.setObject(4, dto.getIsPackageService());
				packageStatement.setObject(5, dto.getIsCapService());

				if(dto.getPayeeId()!=null && dto.getPayeeId()>0)
					packageStatement.setInt(6, dto.getPayeeId());
				else
					packageStatement.setNull(6, Types.NULL);

				packageStatement.setDouble(7, dto.getServiceAmount().doubleValue());
				packageStatement.setDouble(8, dto.getPackageAmount().doubleValue());
				packageStatement.setInt(9, dto.getUnitId());
				packageStatement.setInt(10,dto.getOrganizationId());

				if(dto.getPackageConsumptionMasterId()!=null && dto.getPackageConsumptionMasterId()>0)
					packageStatement.setInt(11, dto.getPackageConsumptionMasterId());
				else
					packageStatement.setNull(11, Types.NULL);

				if(dto.getPackageConsumptionCapDetailsId()!=null && dto.getPackageConsumptionCapDetailsId()>0)
					packageStatement.setInt(12, dto.getPackageConsumptionCapDetailsId());
				else
					packageStatement.setNull(12, Types.NULL);

				if(dto.getPacakgeConsumptionServiceDetailId()!=null && dto.getPacakgeConsumptionServiceDetailId()>0)
					packageStatement.setInt(13, dto.getPacakgeConsumptionServiceDetailId());
				else
					packageStatement.setNull(13, Types.NULL);

				if(dto.getPackageQty()!=null && dto.getPackageQty()>0)
					packageStatement.setInt(14, dto.getPackageQty());
				else
					packageStatement.setNull(14, Types.NULL);

				if(dto.getPackageConsumptionEODetailId()!=null && dto.getPackageConsumptionEODetailId()>0)
					packageStatement.setInt(15, dto.getPackageConsumptionEODetailId());
				else
					packageStatement.setNull(15, Types.NULL);

				packageStatement.addBatch();
				j++;
				if (j % 500 == 0 || j == orderDetailspackageMapperDtoFinalList.size())
				{
					///System.out.println(j);
					try{
						packageStatement.executeBatch(); // Execute every 20 items.
						connection.commit();
					}catch(BatchUpdateException e){
					logger.error("updateOrderDetailsBetterPerformance batch execute failed (package statement)", e);
					}

				}
			}


			 payeeStatement = connection.prepareStatement("INSERT INTO public.t_order_details_payee_mapper "
					+ "(order_details_id, service_id, contract_id, payee_id, payee_amount, unit_id, organization_id, status, split_bill_amount, associate_company_id) "
					+ "VALUES(?,?,?,?,?,?,?,'A',?,?);");
			int k = 0;
			for(OrderDetailsPayeeMapper dto : orderDetailsPayeeMapperDtoFinalList)
			{
				payeeStatement.setInt(1, dto.getOrderDetailsId());
				payeeStatement.setInt(2, dto.getServiceId());
				if(dto.getContractId()!=null)
					payeeStatement.setInt(3, dto.getContractId());
				else
					payeeStatement.setNull(3, Types.NULL);

				if(dto.getPayeeId()!=null)
					payeeStatement.setInt(4, dto.getPayeeId());
				else
					payeeStatement.setNull(4, Types.NULL);

				payeeStatement.setDouble(5, dto.getPayeeAmount()!=null ? dto.getPayeeAmount().doubleValue() : 0.00);
				payeeStatement.setInt(6, dto.getUnitId());
				payeeStatement.setInt(7, dto.getOrganizationId());
				payeeStatement.setDouble(8, dto.getPayeeAmount()!=null ? dto.getPayeeAmount().doubleValue() : 0.00);

				if(dto.getAssociateCompanyId()!=null)
					payeeStatement.setInt(9, dto.getAssociateCompanyId());
				else
					payeeStatement.setNull(9,Types.NULL);

				payeeStatement.addBatch();
				k++;
				if (k % 500 == 0 || k == orderDetailsPayeeMapperDtoFinalList.size())
				{
					//System.out.println(k);
					payeeStatement.executeBatch(); // Execute every 20 items.
					connection.commit();
				}
			}

		}catch (Exception e) {
		logger.error("updateOrderDetailsBetterPerformance failed", e);
		}finally{
			try{
				if (statement != null) {
					statement.close();
				}
				if (packageStatement != null) {
					packageStatement.close();
				}
				if (payeeStatement != null) {
					payeeStatement.close();
				}
				connection.setAutoCommit(true);
				connection.close();
			}catch(Exception e){
			logger.error("updateOrderDetailsBetterPerformance failed", e);
			}
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);

	}

	@Override
	public Response saveOrderDetailsMasterBetterPerformannce(List<OrderDetailsMasterDto> orderDetailsMasterDtoList) throws ApplicationException {
		try {
			
			List<OrderDetailsPayeeMapper> orderDetailsPayeeMapperDtoFinalList = new LinkedList<>();
			List<OrderDetailsPackageMapper> orderDetailspackageMapperDtoFinalList = new LinkedList<>();
			
			for(OrderDetailsMasterDto orderDetailsMasterDto : orderDetailsMasterDtoList)
			{
				OrderDetailsMaster orderDetailsMaster = mapper.map(orderDetailsMasterDto, OrderDetailsMaster.class, "OrderDetailsMasterDto_to_OrderDetailsMaster");
					orderDetailsMaster.setDummyServiceAmt(orderDetailsMaster.getDummyServiceAmt()!=null ? orderDetailsMaster.getDummyServiceAmt() : orderDetailsMaster.getServiceAmt());
					orderDetailsMaster.setDebitNoteService(orderDetailsMasterDto.getDebitNoteService()==true?true:false);
					orderDetailsMaster.setPriorityId(orderDetailsMasterDto.getServicePriorityId() != null ? orderDetailsMasterDto.getServicePriorityId() :1);
					orderDetailsMaster.setCreatedDate(TimeZoneComponent.getDateByzone(orderDetailsMasterDto.getOrgUnitId()).getTime());
					orderDetailsMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(orderDetailsMasterDto.getOrgUnitId()).getTime());
					orderDetailsMaster.setIsFromAddservice(orderDetailsMasterDto.getIsFromAddservice()!=null ? orderDetailsMasterDto.getIsFromAddservice() : 'N');
					orderDetailsMaster.setNursingServiceOrderFlag(orderDetailsMasterDto.getNursingServiceOrderFlag()!=null ? orderDetailsMasterDto.getNursingServiceOrderFlag() : 'N');
					orderDetailsMaster.setIsOrderVisibleDepartment(orderDetailsMasterDto.getIsOrderVisibleDepartment()!=null  ? orderDetailsMasterDto.getIsOrderVisibleDepartment() : 'A');
					orderDetailsMaster.setOrdDocId(orderDetailsMasterDto.getOrdDocId()!=null && orderDetailsMasterDto.getOrdDocId()>0 ? orderDetailsMasterDto.getOrdDocId() : null);
					orderDetailsMaster.setOrdDocSplId(orderDetailsMasterDto.getOrdDocSplId()!=null && orderDetailsMasterDto.getOrdDocSplId()>0 ? orderDetailsMasterDto.getOrdDocSplId() :  orderDetailsMasterDto.getOrdDocSplId());
					orderDetailsMaster.setOrderSetId(orderDetailsMasterDto.getOrderSetId()!=null ? orderDetailsMasterDto.getOrderSetId() :null);
					orderDetailsMaster.setForceAddRemoved(orderDetailsMasterDto.getForceAddRemoved()==null?0:orderDetailsMasterDto.getForceAddRemoved());
					if(orderDetailsMasterDto.getCoPayPer()!=null && orderDetailsMasterDto.getCreditPayable().compareTo(BigDecimal.ZERO)==1)
						orderDetailsMaster.setCoPayPer(orderDetailsMasterDto.getCoPayPer()); 
					else {
						
						if(orderDetailsMasterDto.getFromMedicationCoBilling() != null){
							orderDetailsMaster.setCoPayPer(orderDetailsMasterDto.getCoPayPer());
						}else{
							if(orderDetailsMasterDto.getOrderDetailsPayeeMapperDtosList().size()>1)
								orderDetailsMaster.setCoPayPer(new BigDecimal((orderDetailsMasterDto.getSelfPayable().doubleValue()/orderDetailsMasterDto.getNetAmount().doubleValue())*100));
							else
								orderDetailsMaster.setCoPayPer(new BigDecimal(100));
						}
					}
				orderDetailsMaster.setIsCapService(orderDetailsMaster.getIsCapService()=='\u0000'?'N':orderDetailsMaster.getIsCapService());
				//orderDetailsMaster.setIsPackageService(orderDetailsMaster.getIsPackageService()=='\u0000'?'N':orderDetailsMaster.getIsPackageService());
				orderDetailsMaster.setIsProcedure(orderDetailsMaster.getIsProcedure()=='\u0000'?'N':orderDetailsMaster.getIsProcedure());
				orderDetailsMaster.setIsProcedureService(orderDetailsMaster.getIsProcedureService()=='\u0000'?'N':orderDetailsMaster.getIsProcedureService());
					orderDetailsMaster=save(orderDetailsMaster);
				Integer orderDetailsId = orderDetailsMaster.getOrderDetailsId();
				orderDetailsMasterDto.setOrderDetailsId(orderDetailsId);
				System.out.println("save Order master Details");
				if(orderDetailsMasterDto.getOrderDetailsPayeeMapperDtosList()!=null && orderDetailsMasterDto.getOrderDetailsPayeeMapperDtosList().size()>0)
				{
					for(OrderDetailsPayeeMapperDto dto : orderDetailsMasterDto.getOrderDetailsPayeeMapperDtosList())
					{
						OrderDetailsPayeeMapper detailsPayeeMapper = new OrderDetailsPayeeMapper();
							detailsPayeeMapper.setOrderDetailsId(orderDetailsId);
							detailsPayeeMapper.setContractId(dto.getContractId());
							detailsPayeeMapper.setPayeeId(dto.getPayeeId());
							detailsPayeeMapper.setAssociateCompanyId(dto.getAssociateCompanyId());
							detailsPayeeMapper.setPayeeAmount(dto.getPayeeAmount());
							detailsPayeeMapper.setSplitBillAmount(dto.getPayeeAmount());
							detailsPayeeMapper.setServiceId(dto.getServiceId()!=null ? dto.getServiceId() : orderDetailsMaster.getServiceId());
							detailsPayeeMapper.setOrganizationId(orderDetailsMasterDto.getOrgId());
							detailsPayeeMapper.setUnitId(orderDetailsMasterDto.getOrgUnitId());
							detailsPayeeMapper.setStatus('A');
						orderDetailsPayeeMapperDtoFinalList.add(detailsPayeeMapper);
						//sessionFactory.getCurrentSession().save(detailsPayeeMapper);
						System.out.println("In Order master Details Payee maspper to set payee to list");
					}
				}
				
				
				//For package
				if(orderDetailsMasterDto.getOrderDetailsPackageMapperDtosList()!=null && orderDetailsMasterDto.getOrderDetailsPackageMapperDtosList().size()>0)
				{
					for(OrderDetailsPackageMapperDto dto : orderDetailsMasterDto.getOrderDetailsPackageMapperDtosList())
					{
						OrderDetailsPackageMapper detailsPackageMapper = new OrderDetailsPackageMapper();
							detailsPackageMapper.setOrderDetailsId(orderDetailsId);
							detailsPackageMapper.setPayeeId(dto.getPayeeId());
							detailsPackageMapper.setPackageId(dto.getPackageId());
							detailsPackageMapper.setIsPackageService(dto.getIsPackageService());
							detailsPackageMapper.setIsCapService(dto.getIsCapService());
							detailsPackageMapper.setPackageAmount(dto.getPackageAmount());
							detailsPackageMapper.setServiceId(orderDetailsMasterDto.getServiceId());
							detailsPackageMapper.setPackageQty(dto.getPackageQty());
							detailsPackageMapper.setServiceAmount(dto.getServiceAmount());
							detailsPackageMapper.setOrganizationId(orderDetailsMasterDto.getOrgId());
							detailsPackageMapper.setPacakgeConsumptionServiceDetailId(dto.getPacakgeConsumptionServiceDetailId());
							detailsPackageMapper.setPackageConsumptionCapDetailsId(dto.getPackageConsumptionCapDetailsId());
							detailsPackageMapper.setPackageConsumptionMasterId(dto.getPackageConsumptionMasterId());
							detailsPackageMapper.setPackageConsumptionEODetailId(dto.getPackageConsumptionEODetailId());
							detailsPackageMapper.setUnitId(orderDetailsMasterDto.getOrgUnitId());
							detailsPackageMapper.setStatus('A');
						orderDetailspackageMapperDtoFinalList.add(detailsPackageMapper);
						//sessionFactory.getCurrentSession().save(detailsPackageMapper);
						System.out.println("In Order master Details package mapper to set package to list");
					}
				}
				else
				{
					OrderDetailsPackageMapper detailsPackageMapper = new OrderDetailsPackageMapper();
						detailsPackageMapper.setOrderDetailsId(orderDetailsId);
						detailsPackageMapper.setIsPackageService('N');
						detailsPackageMapper.setIsCapService('N');
						detailsPackageMapper.setPackageAmount(BigDecimal.ZERO);
						detailsPackageMapper.setServiceId(orderDetailsMaster.getServiceId());
						detailsPackageMapper.setServiceAmount(orderDetailsMaster.getServiceAmt());
						detailsPackageMapper.setOrganizationId(orderDetailsMasterDto.getOrgId());
						detailsPackageMapper.setUnitId(orderDetailsMasterDto.getOrgUnitId());
						detailsPackageMapper.setStatus('A');
					orderDetailspackageMapperDtoFinalList.add(detailsPackageMapper);
					//sessionFactory.getCurrentSession().save(detailsPackageMapper);
					System.out.println(" In Order master Details package mapper to set package to list. when OrderDetailsPackageMapper List is null ");
				}
			}
			
			saveOrderDetailsMappers(orderDetailspackageMapperDtoFinalList,orderDetailsPayeeMapperDtoFinalList,null);

			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE,orderDetailsMasterDtoList, null);
		}catch (Exception e) {
		logger.error("saveOrderDetailsMasterBetterPerformannce failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	private void saveLog(String s, String message)  {
		Connection connection =null;
		PreparedStatement stmt= null;
			try {
				//System.out.println("Hemodialysis slot generation.......");
				String pattern = "dd-M-yyyy hh:mm:ss";
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
				String date = simpleDateFormat.format(new Date());

				connection = driverManager.getConnection();
				connection.setAutoCommit(false);
				stmt=connection.prepareStatement("insert into  public.t_check_log_execution (timezone, execution_date, function_name)  values(?,?,?)");
				stmt.setString(1,s);
				stmt.setString(2,date);
				stmt.setString(4, message);
				stmt.executeUpdate();
				connection.commit();


			} catch (HibernateException | SQLException he) {
			logger.error("saveLog failed", he);
			}finally{
				try{
					if (stmt != null) {
						stmt.close();
					}
					connection.setAutoCommit(true);
					connection.close();
				}catch(Exception e){
				logger.error("saveLog failed", e);
				}
			}
	}

	@Override
	public Response getOrderedPackgeServForCancel(int packageId, Integer encounterId,Integer admissionId,Integer visitTypeId) throws ApplicationException {
		try {	
			List<OrderDetailsMasterDto> listOrderDetailsMasterDto = sessionFactory.getCurrentSession().getNamedQuery("GET_ORDERED_PACKAGE_SERVICES_FOR_CANCELLATION_BY_PACKAGE_ENCOUNTER_ID")
				.setParameter("packageId", packageId).setParameter("encounterId", encounterId != null ? encounterId : admissionId)
				.setParameter("visitTypeId", visitTypeId).setResultTransformer(Transformers.aliasToBean(OrderDetailsMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listOrderDetailsMasterDto, null);
		}catch(Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response getOrderDetailsById(int orderDetailsId) throws ApplicationException {
		try {
			OrderDetailsMasterDto dto = (OrderDetailsMasterDto)sessionFactory.getCurrentSession().getNamedQuery("GET_ORDER_DETAILS_BY_ID").setParameter("orderDetailsId", orderDetailsId).setResultTransformer(Transformers.aliasToBean(OrderDetailsMasterDto.class)).uniqueResult();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, dto);
		}catch(Exception e) {
			
			throw e;
		}	
	}

	@Override
	public Response updateOrderDtlsAsCancelledById(int orderDetailsId) throws ApplicationException {
		try {
			Integer result = sessionFactory.getCurrentSession().getNamedQuery("UPDATE_PACKAGE_SERVICE_AS_CANCELLED").setParameter("orderDetailsId", orderDetailsId).executeUpdate();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, result);
		}catch(Exception e) {
			
			throw e;
		}
	}

	
	@Override
	public Response getPreviousOrderSerivice(int orgId, int orgUnitId, int orderId) throws ApplicationException {
		try {	
			List<OrderDetailsMasterDto> listOrderDetailsMasterDto = sessionFactory.getCurrentSession().getNamedQuery("GET_ORDERED_PACKAGE_SERVICES_FOR_CANCELLATION_BY_PACKAGE_ENCOUNTER_ID")
				.setParameter("orgId", orgId)
				.setParameter("orgUnitId", orgUnitId)
				.setParameter("orderId", orderId )
				.setResultTransformer(Transformers.aliasToBean(OrderDetailsMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listOrderDetailsMasterDto, null);
		}catch(Exception e) {
			
			throw e;
		}
	}


	// Charge Conversion 
	@Override
	public Response updateOrderDtlsByOrderDtlsId(OrderDetailsMasterDto orderDetailsMasterDto) throws ApplicationException {
		try{
			OrderDetailsMaster orderDetailsMst = findById(orderDetailsMasterDto.getOrderDetailsId());
			if (orderDetailsMst.getOrderDetailsId() != 0)
			{
				BigDecimal serviceAmt = orderDetailsMasterDto.getServiceAmt();
				BigDecimal orderTotalAmt = serviceAmt.multiply(new BigDecimal(orderDetailsMst.getOrderQty()));
				//BigDecimal taxAmt = orderTotalAmt.multiply((orderDetailsMst.getTaxPer().multiply(new BigDecimal(0.01))));
				BigDecimal netAmt = orderTotalAmt.add(orderDetailsMst.getTaxAmount()).subtract((orderDetailsMst.getOrdConcession().add(orderDetailsMst.getOrdDiscount())));
				
				orderDetailsMst.setOrdTotalAmt(orderTotalAmt);
				orderDetailsMst.setNetAmount(netAmt);
				orderDetailsMst.setServiceAmt(serviceAmt);
				orderDetailsMst.setUpdatedBy(orderDetailsMasterDto.getUpdatedBy());
				orderDetailsMst.setUpdatedDate(TimeZoneComponent.getDateByzone(orderDetailsMst.getOrgUnitId()).getTime());
				orderDetailsMst.setOrdDocId(orderDetailsMasterDto.getOrdDocId()!=null && orderDetailsMasterDto.getOrdDocId()>0 ? orderDetailsMasterDto.getOrdDocId() : null);
				orderDetailsMst.setOrdDocSplId(orderDetailsMasterDto.getOrdDocSplId()!=null && orderDetailsMasterDto.getOrdDocSplId()>0 ? orderDetailsMasterDto.getOrdDocSplId() : null);
				orderDetailsMst = update(orderDetailsMst);
				return new Response<>(SUCCESS, SUCCESS_CODE, null, null, orderDetailsMst);
			}else {
				
				return new Response<>(ERROR, ERROR, COMMON_ERROR_MESSAGE, null, orderDetailsMst);
			}
			
		}catch(Exception e){
			
			throw new ApplicationException(e.getMessage());
		}
	}

	@Override
	public Response getPatientServiceOrderListBySaleDetails(OrderDetailsMasterDto dto) throws BillingException {
		try {
			
			//System.out.println("======"+dto.getSaleDetailId());
		
			List<OrderDetailsByEncounterIdDto> list = (List<OrderDetailsByEncounterIdDto>)sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PATIENT_ITEM_ORDER_LIST")
					//.setParameter("orgId", admissionDto.getOrganizationId())
					//.setParameter("unitId", admissionDto.getUnitId())
					.setParameter("saleDetailId", dto.getSaleDetailId())
					.setResultTransformer(Transformers.aliasToBean(OrderDetailsByEncounterIdDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);

		} catch (HibernateException he) {
		logger.error("getPatientServiceOrderListBySaleDetails failed", he);
		} catch (Exception e) {
		logger.error("getPatientServiceOrderListBySaleDetails failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	
	@Override
	public OrderDetailsMaster getOrderDetailsByIdForPayeeUpdate(Integer orderDetailsMasterId) throws BillingException {
		try {
			OrderDetailsMaster orderDetailsMasterDto = findById(orderDetailsMasterId);
			return orderDetailsMasterDto;

		} catch (HibernateException he) {
		logger.error("getOrderDetailsByIdForPayeeUpdate failed", he);
		} catch (Exception e) {
		logger.error("getOrderDetailsByIdForPayeeUpdate failed", e);
		}
		return null;
	}

	@Override
	public Response orderDetailsLogAfterUpdatePayee(OrderDetailsMaster masterDto) throws BillingException {
		try {
			
			OrderDetailsLogAfterUpdatePayee detailsLogAfterUpdatePayee = mapper.map(masterDto, OrderDetailsLogAfterUpdatePayee.class, "OrderDetailsLogAfterUpdatePayee_to_OrderDetailsMaster");
			detailsLogAfterUpdatePayee.setOrdDocId(masterDto.getOrdDocId()!=null && masterDto.getOrdDocId()>0 ? masterDto.getOrdDocId() : null);
			detailsLogAfterUpdatePayee.setOrdDocSplId(masterDto.getOrdDocSplId()!=null && masterDto.getOrdDocSplId()>0 ? masterDto.getOrdDocSplId() : null);
			sessionFactory.getCurrentSession().save(detailsLogAfterUpdatePayee);
			
			return new Response(SUCCESS, SUCCESS_CODE, null, null, null);

		} catch (HibernateException he) {
		logger.error("orderDetailsLogAfterUpdatePayee failed", he);
		} catch (Exception e) {
		logger.error("orderDetailsLogAfterUpdatePayee failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response saveOrderDetailsMaster(OrderDetailsMaster masterDto, List<OrderDetailsPayeeMapperDto> list) throws BillingException {
		try {
			masterDto.setCreatedDate(TimeZoneComponent.getDateByzone(masterDto.getOrgUnitId()).getTime());
			masterDto.setUpdatedDate(TimeZoneComponent.getDateByzone(masterDto.getOrgUnitId()).getTime());
			
			if(masterDto.getCoPayPer()!=null && masterDto.getCreditPayable().compareTo(BigDecimal.ZERO)==1)
				masterDto.setCoPayPer(masterDto.getCoPayPer()); 
			else {
				if(list!=null && list.size()>1)
					masterDto.setCoPayPer(new BigDecimal((masterDto.getSelfPayable().doubleValue()/masterDto.getNetAmount().doubleValue())*100));
				else
					masterDto.setCoPayPer(BigDecimal.ZERO);
			}
			
			masterDto=save(masterDto);
			
			if(list!=null && list.size()>0)
			{
				sessionFactory.getCurrentSession().createQuery(" UPDATE OrderDetailsPayeeMapper SET status='I' "
		        		+ " WHERE orderDetailsId =:orderDetailsId ")
						.setParameter("orderDetailsId",masterDto.getOrderDetailsId()).executeUpdate();
				
				for(OrderDetailsPayeeMapperDto dto : list)
				{
					OrderDetailsPayeeMapper detailsPayeeMapper = new OrderDetailsPayeeMapper();
						detailsPayeeMapper.setOrderDetailsId(masterDto.getOrderDetailsId());
						detailsPayeeMapper.setContractId(dto.getContractId());
						detailsPayeeMapper.setPayeeId(dto.getPayeeId());
						detailsPayeeMapper.setAssociateCompanyId(dto.getAssociateCompanyId());
						detailsPayeeMapper.setPayeeAmount(dto.getPayeeAmount());
						detailsPayeeMapper.setSplitBillAmount(dto.getPayeeAmount());
						detailsPayeeMapper.setServiceId(dto.getServiceId());
						detailsPayeeMapper.setOrganizationId(masterDto.getOrgId());
						detailsPayeeMapper.setUnitId(masterDto.getOrgUnitId());
						if(masterDto.getStatus()=='A')
							detailsPayeeMapper.setStatus('A');
						else
							detailsPayeeMapper.setStatus('I');
					sessionFactory.getCurrentSession().save(detailsPayeeMapper);	
				}
			}
			
			
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		}catch (Exception e) {
		logger.error("saveOrderDetailsMaster failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response saveOrderDetailsPayeeDetails(OrderDetailsMasterDto orderDetailsMasterDto) {
		try {
			
				for(OrderDetailsPayeeMapperDto dto : orderDetailsMasterDto.getOrderDetailsPayeeMapperDtosList())
				{
					OrderDetailsPayeeMapper detailsPayeeMapper = new OrderDetailsPayeeMapper();
						detailsPayeeMapper.setOrderDetailsId(orderDetailsMasterDto.getOrderDetailsId());
						detailsPayeeMapper.setContractId(dto.getContractId());
						detailsPayeeMapper.setPayeeId(dto.getPayeeId());
						detailsPayeeMapper.setAssociateCompanyId(dto.getAssociateCompanyId());
						detailsPayeeMapper.setPayeeAmount(dto.getPayeeAmount());
						detailsPayeeMapper.setSplitBillAmount(dto.getPayeeAmount());
						detailsPayeeMapper.setServiceId(dto.getServiceId());
						detailsPayeeMapper.setOrganizationId(orderDetailsMasterDto.getOrgId());
						detailsPayeeMapper.setUnitId(orderDetailsMasterDto.getOrgUnitId());
						detailsPayeeMapper.setStatus('A');
					sessionFactory.getCurrentSession().save(detailsPayeeMapper);	
				}
				
			return new Response(SUCCESS, SUCCESS_CODE, null, null, null);

		} catch (HibernateException he) {
		logger.error("saveOrderDetailsPayeeDetails failed", he);
		} catch (Exception e) {
		logger.error("saveOrderDetailsPayeeDetails failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getOrderPayeeDetailsByOrderDetailsId(Integer orderDetailsId, Integer billId) {
		try{
			StringBuilder query = null;
			
			
			if(billId!=null && billId>0)
			{
				query = new StringBuilder("select " 
						 + " 	mapper.id as \"id\", "
						 + " 	mapper.order_details_id as \"orderDetailsId\", "
						 + " 	mapper.service_id as \"serviceId\", "
						 + " 	mapper.contract_id as \"contractId\", "
						 + " 	coalesce(mapper.payee_id,config.self_payee_id) as \"payeeId\", "
						 + " 	mapper.associate_company_id as \"associateCompanyId\", "
						 + "	insurace.insurance_desc as \"associateCompanyDesc\", "
						 + " 	mapper.payee_amount as \"payeeAmount\", "
						 + " 	mapper.payee_amount as \"creditPayable\", "
						 + " 	mapper.payee_amount as \"selfPayable\", "
						 + "	mapper.split_bill_amount as \"spiltBillAmt\", "
						 + " 	com.company_desc as \"payeeDesc\", "
						 + "    coalesce(contract.is_tariff_rate_applicable, 'N') as \"isTariffRateApplicable\", "
						 + "	tbdpm.is_invoice_generated as \"isInvoiceGenerated\" "
						 + " from "
						 + " 	t_order_details_payee_mapper mapper "
						 + " left join t_company_master com on "
						 + " 	com.company_id = mapper.payee_id "
						 + " inner join public.t_unit_application_billing_configuration  config "
						 + "  	on config.unit_id  = mapper.unit_id and config.status  = 'A' "
						 + " left join service.m_company_contract_master contract "
						 + " 	on contract.contract_id=mapper.contract_id "
						 + " left join adt.t_insurance insurace "
						 + " 	on insurace.insurance_id =mapper.associate_company_id "
						 + " left join billing.t_bill_payee_details tbdpm "
						 + " 	on tbdpm.payee_id = mapper.payee_id "
						 + "	and tbdpm.bill_id =:billId "
						 + " where "
						 + " 	mapper.status = 'A' "
						 + " 	and mapper.order_details_id =:orderDetailsId "
						 + " order by "
						 + " 	mapper.id ");
			}else{
				query = new StringBuilder("select " 
						 + " 	mapper.id as \"id\", "
						 + " 	mapper.order_details_id as \"orderDetailsId\", "
						 + " 	mapper.service_id as \"serviceId\", "
						 + " 	mapper.contract_id as \"contractId\", "
						 + " 	coalesce(mapper.payee_id,config.self_payee_id) as \"payeeId\", "
						 + " 	mapper.associate_company_id as \"associateCompanyId\", "
						 + "	insurace.insurance_desc as \"associateCompanyDesc\", "
						 + " 	mapper.payee_amount as \"payeeAmount\", "
						 + " 	mapper.payee_amount as \"creditPayable\", "
						 + " 	mapper.payee_amount as \"selfPayable\", "
						 + "	mapper.split_bill_amount as \"spiltBillAmt\", "
						 + " 	com.company_desc as \"payeeDesc\", "
						 + "    coalesce(contract.is_tariff_rate_applicable, 'N') as \"isTariffRateApplicable\", "
						 + "	cast('N' as character) as \"isInvoiceGenerated\" "
						 + " from "
						 + " 	t_order_details_payee_mapper mapper "
						 + " left join t_company_master com on "
						 + " 	com.company_id = mapper.payee_id "
						 + " inner join public.t_unit_application_billing_configuration  config "
						 + "  	on config.unit_id  = mapper.unit_id and config.status  = 'A' "
						 + " left join service.m_company_contract_master contract "
						 + " 	on contract.contract_id=mapper.contract_id "
						 + " left join adt.t_insurance insurace "
						 + " 	on insurace.insurance_id =mapper.associate_company_id "
						 + " where "
						 + " 	mapper.status = 'A' "
						 + " 	and mapper.order_details_id =:orderDetailsId "
						 + " order by "
						 + " 	mapper.id ");
			}
			
			
			Query createQuery = sessionFactory.getCurrentSession()
					.createSQLQuery(query.toString());
			if(billId!=null && billId>0) {
				createQuery.setParameter("billId",billId);
			}
			createQuery.setParameter("orderDetailsId",orderDetailsId);
			List<OrderDetailsPayeeMapperDto> list =createQuery.setResultTransformer(Transformers.aliasToBean(OrderDetailsPayeeMapperDto.class)).list();
			
			return new Response(SUCCESS, null, null, list,null);
		}catch(Exception e)
		{
		logger.error("getOrderPayeeDetailsByOrderDetailsId failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}


	@Override
	public Response getOrderPayeeDetailsByOrderDetailsId(List<Integer> orderDetailsIds, Integer billId) {
		try{
			StringBuilder query = null;
			List<OrderDetailsPayeeMapperDto> list = null;
			
			if(orderDetailsIds!=null && orderDetailsIds.size()>0) {
			if(billId!=null && billId>0 && orderDetailsIds!=null && orderDetailsIds.size()>0)
			{
				query = new StringBuilder("select " 
						 + " 	mapper.id as \"id\", "
						 + " 	mapper.order_details_id as \"orderDetailsId\", "
						 + " 	mapper.service_id as \"serviceId\", "
						 + " 	mapper.contract_id as \"contractId\", "
						 + " 	coalesce(mapper.payee_id,config.self_payee_id) as \"payeeId\", "
						 + " 	mapper.associate_company_id as \"associateCompanyId\", "
						 + "	insurace.insurance_desc as \"associateCompanyDesc\", "
						 + " 	mapper.payee_amount as \"payeeAmount\", "
						 + " 	mapper.payee_amount as \"creditPayable\", "
						 + " 	mapper.payee_amount as \"selfPayable\", "
						 + "	mapper.split_bill_amount as \"spiltBillAmt\", "
						 + " 	com.company_desc as \"payeeDesc\", "
						 + "    coalesce(contract.is_tariff_rate_applicable, 'N') as \"isTariffRateApplicable\", "
						 + "	tbdpm.is_invoice_generated as \"isInvoiceGenerated\" "
						 + " from "
						 + " 	t_order_details_payee_mapper mapper "
						 + " left join t_company_master com on "
						 + " 	com.company_id = mapper.payee_id "
						 + " inner join public.t_unit_application_billing_configuration  config "
						 + "  	on config.unit_id  = mapper.unit_id and config.status  = 'A' "
						 + " left join service.m_company_contract_master contract "
						 + " 	on contract.contract_id=mapper.contract_id "
						 + " left join adt.t_insurance insurace "
						 + " 	on insurace.insurance_id =mapper.associate_company_id "
						 + " left join billing.t_bill_payee_details tbdpm "
						 + " 	on tbdpm.payee_id = mapper.payee_id "
						 + "	and tbdpm.bill_id =:billId "
						 + " where "
						 + " 	mapper.status = 'A' "
						 + " 	and mapper.order_details_id in (:ids) "
						 + " order by "
						 + " 	mapper.id ");
			}else{
				query = new StringBuilder("select " 
						 + " 	mapper.id as \"id\", "
						 + " 	mapper.order_details_id as \"orderDetailsId\", "
						 + " 	mapper.service_id as \"serviceId\", "
						 + " 	mapper.contract_id as \"contractId\", "
						 + " 	coalesce(mapper.payee_id,config.self_payee_id) as \"payeeId\", "
						 + " 	mapper.associate_company_id as \"associateCompanyId\", "
						 + "	insurace.insurance_desc as \"associateCompanyDesc\", "
						 + " 	mapper.payee_amount as \"payeeAmount\", "
						 + " 	mapper.payee_amount as \"creditPayable\", "
						 + " 	mapper.payee_amount as \"selfPayable\", "
						 + "	mapper.split_bill_amount as \"spiltBillAmt\", "
						 + " 	com.company_desc as \"payeeDesc\", "
						 + "    coalesce(contract.is_tariff_rate_applicable, 'N') as \"isTariffRateApplicable\", "
						 + "	cast('N' as character) as \"isInvoiceGenerated\" "
						 + " from "
						 + " 	t_order_details_payee_mapper mapper "
						 + " left join t_company_master com on "
						 + " 	com.company_id = mapper.payee_id "
						 + " inner join public.t_unit_application_billing_configuration  config "
						 + "  	on config.unit_id  = mapper.unit_id and config.status  = 'A' "
						 + " left join service.m_company_contract_master contract "
						 + " 	on contract.contract_id=mapper.contract_id "
						 + " left join adt.t_insurance insurace "
						 + " 	on insurace.insurance_id =mapper.associate_company_id "
						 + " where "
						 + " 	mapper.status = 'A' "
						 + " 	and mapper.order_details_id  in (:ids) "
						 + " order by "
						 + " 	mapper.id ");
			}
				Query createQuery = sessionFactory.getCurrentSession()
						.createSQLQuery(query.toString());
				createQuery.setParameterList("ids", orderDetailsIds);
				if(billId!=null && billId>0) {
					createQuery.setParameter("billId",billId);
				}
			    list =createQuery.setResultTransformer(Transformers.aliasToBean(OrderDetailsPayeeMapperDto.class)).list();
			}
			
			return new Response(SUCCESS, null, null, list,null);
		}catch(Exception e)
		{
		logger.error("getOrderPayeeDetailsByOrderDetailsId failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getAvailableRefundAmountCreditNote(Integer orderDetailsId, Integer billId) {
		try{
			StringBuilder query = null;
			List<PayeeRefundMpprDto> list = null;
				if(billId!=null && billId>0 && orderDetailsId!=null && orderDetailsId>0)
				{
					query = new StringBuilder("with availablecramt as ( "
							+ "select "
							+ "	coalesce(odpm.payee_id, config.self_payee_id) as pid, "
							+ "	odpm.associate_company_id as aci, "
							+ "	sum(coalesce(odpm.payee_amount, 0)) as pamt, "
							+ "	coalesce((select distinct  coalesce(tcnd.cancel_or_discount, 0) from  billing.t_credit_note_detail tcnd where tcnd.order_detail_id = tbd.order_details_id "
							+ "	and tbd.status = 'A' and tcnd.status = 'A'),0)  as cod "
							+ "from "
							+ "	t_order_details_payee_mapper odpm "
							+ "inner join public.t_unit_application_billing_configuration config on "
							+ "	config.unit_id = odpm.unit_id "
							+ "	and config.status = 'A' "
							+ "inner join billing.t_billing_details tbd on "
							+ "	tbd.order_details_id = odpm.order_details_id "
							+ "where "
							+ "	odpm.order_details_id = :orderDetailsId "
							+ "	and odpm.status = 'A' and tbd.status ='A' "
							+ "group by "
							+ "	aci, "
							+ "	pid, "
							+ "	cod) "
							+ "select "
							+ "	cr.pid as \"payeeId\", "
							+ "	cr.aci as \"associateCompanyId\", "
							+ "	(cr.pamt -( "
							+ "	select "
							+ "		case "
							+ "			when cr.cod in (0,2) then coalesce(sum(coalesce(tcnd.cr_net_amount, 0)), 0) + coalesce(sum(coalesce(tcnd.cr_tax_amount , 0)), 0) "
							+ "			else cr.pamt "
							+ "		end "
							+ "	from "
							+ "		billing.t_credit_note_detail tcnd "
							+ "	inner join billing.m_credit_note_master mcnm on "
							+ "		mcnm.credit_note_id = tcnd.credit_note_id "
							+ "	where "
							+ "		tcnd.order_detail_id = :orderDetailsId "
							+ "		and tcnd.status = 'A' "
							+ "		and mcnm.bill_payee_id = cr.pid and mcnm.status ='A')) as \"payeeRefundAmount\" "
							+ "from "
							+ "	availablecramt cr");
				}

				list = sessionFactory.getCurrentSession()
						.createSQLQuery(query.toString())
						.setParameter("orderDetailsId", orderDetailsId)
						.setResultTransformer(Transformers.aliasToBean(PayeeRefundMpprDto.class)).list();

			return new Response(SUCCESS, null, null, list,null);
		}catch(Exception e)
		{
		logger.error("getAvailableRefundAmountCreditNote failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateOrderDetailsForAccessionNumber(Integer orderDetailsIdForAccession)
			throws ApplicationException {
	try {	
				String accNum = "ACCESS-"+orderDetailsIdForAccession;
				sessionFactory.getCurrentSession().createQuery(" UPDATE OrderDetailsMaster SET accessionNumber=:accessionNumber "
        		+ " WHERE orderDetailsId=:orderDetailsId")
				.setParameter("accessionNumber", accNum)
				.setParameter("orderDetailsId", orderDetailsIdForAccession)
				.executeUpdate();
			
			
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		}catch(Exception e) {
		logger.error("updateOrderDetailsForAccessionNumber failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@Override
	public Response getVisitDetailsForRenderingService(Integer orderDetailsId)
			throws ApplicationException {
		try {	
				String query = " select " 
						 + " 	admission.admission_id as \"admissionId\", " 
						 + " 	0 as \"encounterId\", " 
						 + " 	admission.patient_id as \"patientId\", " 
						 + " 	admission.visit_type_id as \"visitTypeId\", " 
						 + " 	admission.doctor_id as \"doctorId\", " 
						 + " 	admission.speciality_id as \"specialityId\", " 
						 + " 	spd.payment_entitlement_id as \"paymentEntitlementId\", " 
						 + " 	spd.contract_id as \"contractId\", " 
						 + " 	spd.payee_id as \"payeeId\", " 
						 + " 	bedBill.billing_bed_category_id as \"billingBedCategoryId\", " 
						 + " 	( " 
						 + " 		select " 
						 + " 			bed_category_id " 
						 + " 		from " 
						 + " 			adt.t_admission_details " 
						 + " 		where " 
						 + " 			admission_id = admission.admission_id " 
						 + " 		order by " 
						 + " 			1 desc limit 1 " 
						 + " 	) as \"bedCategoryId\", " 
						 + " 	patReg.patient_category_id as \"patientTypeId\", " 
						 + " 	admission.organization_id as \"organizationId\", " 
						 + " 	admission.unit_id as \"unitId\", " 
						 + " 	to_char( " 
						 + " 		admission.created_date, " 
						 + " 		'dd/MM/yyyy HH24:MI:ss' " 
						 + " 	) as \"admissionDateTime\", " 
						 + " 	config.self_payee_id as \"selfPayeeId\", " 
						 + " 	config.is_pre_or_post_billing as \"isPreBilling\", " 
						 + " ( " + 
						 "	select " + 
						 "		billing_master_id " + 
						 "	from " + 
						 "		billing.m_billing_master " + 
						 "	where " + 
						 "		admission_id = admission.admission_id " + 
						 "		and status = 'A' " + 
						 "	order by " + 
						 "		1 desc " + 
						 "	limit 1 ) as \"billId\", " 
						 + " 	od.package_id as \"packageId\", " 
						 + " 	package.package_type_id as \"packageTypeId\", " 
						 + " 	coalesce( " 
						 + " 		usmd.is_ref_doctor_share, " 
						 + " 		'N' " 
						 + " 	) as \"isRefDoctorShare\", " 
						 + " 	coalesce( " 
						 + " 		usmd.is_doc_req, " 
						 + " 		'N' " 
						 + " 	) as \"isDoctorReq\", " 
						 + " 	uniApp.default_self_tariff_id as \"tariffId\", " 
						 + " 	refDtls.referal_details_id as \"referralId\", " 
						 + " 	admission.patient_class_id as \"patientClassId\", "
						 + " 	serviceM.speciality_id as \"serviceSpecialityId\", "
						 + "	uniApp.default_discount_ownership as \"defaultDiscountOwnership\" "
						 + " from " 
						 + " 	t_order_details od " 
						 + " inner join t_order_master om on " 
						 + " 	om.order_id = od.order_id " 
						 + " inner join adt.t_admission admission on " 
						 + " 	admission.admission_id = om.admission_id " 
						 + " inner join opd.t_patient_sponsor_details spd on " 
						 + " 	spd.admission_id = admission.admission_id " 
						 + " 	and spd.priority_id = 1 " 
						 + " 	and spd.status = 'A' " 
						 + " inner join adt.m_bed_master bed on " 
						 + " 	bed.bed_id =( " 
						 + " 		select " 
						 + " 			bed_id " 
						 + " 		from " 
						 + " 			adt.t_admission_details " 
						 + " 		where " 
						 + " 			admission_id = admission.admission_id " 
						 + " 		order by " 
						 + " 			1 desc limit 1 " 
						 + " 	) " 
						 + " inner join adt.m_billing_bed_category_master bedBill on " 
						 + " 	bedBill.billing_bed_category_id =( " 
						 + " 		select " 
						 + " 			billing_bed_category_id " 
						 + " 		from " 
						 + " 			adt.t_admission_details " 
						 + " 		where " 
						 + " 			admission_id = admission.admission_id " 
						 + " 		order by " 
						 + " 			1 desc limit 1 " 
						 + " 	) " 
						 + " inner join patient.t_patient_registration patReg on " 
						 + " 	patReg.patient_id = admission.patient_id " 
						 + " inner join t_unit_application_billing_configuration config on " 
						 + " 	config.unit_id = admission.unit_id " 
						 + " 	and config.status = 'A' " 
						 + " left join service.m_package_master package on " 
						 + " 	package.package_master_id = od.package_id " 
						 + " left join t_referal_details refDtls on " 
						 + " 	refDtls.admission_id = om.admission_id " 
						 + " inner join service.t_unit_service_master_details usmd on " 
						 + " 	usmd.service_master_id = od.service_id " 
						 + " inner join service.m_service_master serviceM on "
						 + " 	serviceM.service_master_id = od.service_id "
						 + " inner join t_unit_application_billing_configuration uniApp on " 
						 + " 	uniApp.unit_id = od.org_unit_id " 
						 + " where " 
						 + " 	od.status = 'A' " 
						 + " 	and uniApp.status = 'A' " 
						 + " 	and usmd.unit_id = od.org_unit_id " 
						 + " 	and od.ord_cancelled != 'Y'  " 
						 + " 	and od.order_details_id =:orderDetailsId "
						 + " union select " 
						 + " 	0 as \"admissionId\", " 
						 + " 	enc.encounter_id as \"encounterId\", " 
						 + " 	enc.patient_id as \"patientId\", " 
						 + " 	1 as \"visitTypeId\", " 
						 + " 	enc.doctor_id as \"doctorId\", " 
						 + " 	enc.dept_id as \"specialityId\", " 
						 + " 	spd.payment_entitlement_id as \"paymentEntitlementId\", " 
						 + " 	spd.contract_id as \"contractId\", " 
						 + " 	spd.payee_id as \"payeeId\", " 
						 + " 	bedBill.billing_bed_category_id as \"billingBedCategoryId\", " 
						 + " 	0 as \"bedCategoryId\", " 
						 + " 	patReg.patient_category_id as \"patientTypeId\", " 
						 + " 	enc.organization_id as \"organizationId\", " 
						 + " 	enc.unit_id as \"unitId\", " 
						 + " 	to_char( " 
						 + " 		enc.encounter_date, " 
						 + " 		'dd/MM/yyyy HH24:MI:ss' " 
						 + " 	) as \"admissionDateTime\", " 
						 + " 	config.self_payee_id as \"selfPayeeId\", " 
						 + " 	config.is_pre_or_post_billing as \"isPreBilling\", " 
						 + " 	( " 
						 + " 		select " 
						 + " 			billing_master_id " 
						 + " 		from " 
						 + " 			billing.m_billing_master " 
						 + " 		where " 
						 + " 			encounter_id = enc.encounter_id and status='A' " 
						 + " 		order by " 
						 + " 			1 desc limit 1 " 
						 + " 	) as \"billId\", " 
						 + " 	od.package_id as \"packageId\", " 
						 + " 	package.package_type_id as \"packageTypeId\", " 
						 + " 	coalesce( " 
						 + " 		usmd.is_ref_doctor_share, " 
						 + " 		'N' " 
						 + " 	) as \"isRefDoctorShare\", " 
						 + " 	coalesce( " 
						 + " 		usmd.is_doc_req, " 
						 + " 		'N' " 
						 + " 	) as \"isDoctorReq\", " 
						 + " 	uniApp.default_self_tariff_id as \"tariffId\", " 
						 + " 	refDtls.referal_details_id as \"referralId\", " 
						 + " 	enc.patient_class_id as \"patientClassId\", "
						 + " 	serviceM.speciality_id as \"serviceSpecialityId\", "
						 + "	uniApp.default_discount_ownership as \"defaultDiscountOwnership\" "
						 + " from " 
						 + " 	t_order_details od " 
						 + " inner join t_order_master om on " 
						 + " 	om.order_id = od.order_id " 
						 + " inner join opd.t_encounter_master enc on " 
						 + " 	enc.encounter_id = om.encounter_id " 
						 + " inner join opd.t_patient_sponsor_details spd on " 
						 + " 	spd.encounter_id = enc.encounter_id " 
						 + " 	and spd.priority_id = 1 " 
						 + " 	and spd.status = 'A' " 
						 + " inner join t_unit_application_billing_configuration config on " 
						 + " 	config.unit_id = enc.unit_id " 
						 + " 	and config.status = 'A' " 
						 + " inner join adt.m_billing_bed_category_master bedBill on " 
						 + " 	bedBill.billing_bed_category_id = config.opd_billing_cat_id " 
						 + " inner join patient.t_patient_registration patReg on " 
						 + " 	patReg.patient_id = enc.patient_id " 
						 + " left join service.m_package_master package on " 
						 + " 	package.package_master_id = od.package_id " 
						 + " left join t_referal_details refDtls on " 
						 + " 	refDtls.encounter_id = om.encounter_id " 
						 + " inner join service.t_unit_service_master_details usmd on " 
						 + " 	usmd.service_master_id = od.service_id " 
						 + " inner join service.m_service_master serviceM on "
						 + " 	serviceM.service_master_id = od.service_id "
						 + " inner join t_unit_application_billing_configuration uniApp on " 
						 + " 	uniApp.unit_id = od.org_unit_id " 
						 + " where " 
						 + " 	od.status = 'A' " 
						 + " 	and uniApp.status = 'A' " 
						 + " 	and usmd.unit_id = od.org_unit_id " 
						 + " 	and od.ord_cancelled != 'Y' " 
						 + " 	and od.order_details_id =:orderDetailsId "
						 + " limit 1 ";
				
				AdmissionsForBatchProcessDto admissionsForBatchProcessDto = (AdmissionsForBatchProcessDto)sessionFactory.getCurrentSession()
						.createSQLQuery(query)
						.setParameter("orderDetailsId",orderDetailsId)
						.setResultTransformer(Transformers.aliasToBean(AdmissionsForBatchProcessDto.class))
						.uniqueResult();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, admissionsForBatchProcessDto);
		}catch(Exception e) {
		logger.error("getVisitDetailsForRenderingService failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@Override
	public Response updateIsBilledFlag(Integer orderDetailsId)
			throws ApplicationException {
		try {	
				sessionFactory.getCurrentSession().createQuery(" UPDATE OrderDetailsMaster SET serviceIsBilled='1' "
        		+ " WHERE orderDetailsId=:orderDetailsId")
				.setParameter("orderDetailsId", orderDetailsId)
				.executeUpdate();
			
			
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		}catch(Exception e) {
		logger.error("updateIsBilledFlag failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response incativeOrderPayeeDetails(Integer orderDetailsId) {
		try {	
			
			sessionFactory.getCurrentSession().createQuery(" UPDATE OrderDetailsPayeeMapper set status='I' WHERE orderDetailsId =:orderDetailsId ")
					.setParameter("orderDetailsId",orderDetailsId).executeUpdate();
	
			sessionFactory.getCurrentSession().createQuery(" UPDATE OrderDetailsPackageMapper set status='I' WHERE orderDetailsId =:orderDetailsId ")
					.setParameter("orderDetailsId",orderDetailsId).executeUpdate();
		return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		}catch(Exception e) {
		logger.error("incativeOrderPayeeDetails failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);

	}

	@Override
	public Response updateReviewedServiceStatus(Integer orderDetailsId) {
		try {	
			
			sessionFactory.getCurrentSession().createSQLQuery("UPDATE public.t_order_details set is_service_reviewed= true where order_details_id=:orderDetailsId ")
				.setParameter("orderDetailsId", orderDetailsId)
				.executeUpdate();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
			}catch(Exception e) {
			logger.error("updateReviewedServiceStatus failed", e);
			}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response cancelUnRenderedServices(CancelOrderDto cancelOrderDto)
			throws BillingException {
		try {
			sessionFactory.getCurrentSession()
					.createSQLQuery("UPDATE public.t_order_details SET "
							+ "status = 'I', "
							+ "ord_cancelled = 'Y', "
							+ "ord_cancel_reason_id = :ordCancelReasonId, "
							+ "ord_cancel_remark = :ordCancelRemark, "
							+ "ord_cancel_by = :updatedBy, "
							+ "ord_cancel_datetime = :cancelDate, "
							+ "updated_by = :updatedBy, "
							+ "updated_date = :updateDate "
							+ "WHERE order_details_id = :orderDetailsId")
					.setParameter("ordCancelReasonId", cancelOrderDto.getOrdCancelReasonId())
					.setParameter("ordCancelRemark", cancelOrderDto.getOrdCancelRemark())
					.setParameter("updatedBy", cancelOrderDto.getUserId())
					.setParameter("cancelDate", GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(sessionService.getUnitId() != null ? sessionService.getUnitId() : 0), "dd-M-yyyy HH:mm:ss"))
					.setParameter("updateDate", GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(sessionService.getUnitId() != null ? sessionService.getUnitId() : 0), "dd-M-yyyy HH:mm:ss"))
					.setParameter("orderDetailsId", cancelOrderDto.getOrderDetailsId())
					.executeUpdate();

			return new Response(SUCCESS, null, null, null, null);

		} catch (HibernateException he) {
		logger.error("cancelUnRenderedServices failed", he);
		} catch (Exception e) {
		logger.error("cancelUnRenderedServices failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateOrderMasterData(List<Integer> orderMasterIds,ConvertServicesFromOptoIpDto convertServicesFromOptoIpDto) {
		try{
		} catch (HibernateException he) {
		logger.error("updateOrderMasterData failed", he);
		} catch (Exception e) {
		logger.error("updateOrderMasterData failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getOrderDetailsPackageMapperByOrderDetailsId(Integer orderDetailsId) {
		try {
			String query ="select "
					+ "	mppr.id as \"id\", "
					+ "	mppr.order_details_id as \"orderDetailsId\", "
					+ "	mppr.service_id as \"serviceId\", "
					+ "	mppr.package_id as \"packageId\", "
					+ "	mppr.payee_id as \"payeeId\", "
					+ "	pack_service.service_standard_code as \"packageCode\", "
					+ "	pack_service.service_standard_name as \"packageName\", "
					+ "	service.service_standard_code as \"serviceStandardCode\", "
					+ "	service.service_standard_name as \"serviceStandardName\", "
					+ "	mppr.service_amount as \"serviceAmount\", "
					+ "	mppr.package_amount \"packageAmount\", "
					+ "	mppr.is_package_service as \"isPackageService\", "
					+ "	mppr.is_cap_service as \"isCapService\", "
					+ "	mppr.status as \"status\", "
					+ "	item.\"name\" as \"itemName\", "
					+ "	item.code as \"itemCode\", "
					+ "	mppr.pacakge_consumption_service_detail_id as \"pacakgeConsumptionServiceDetailId\", "
					+ "	mppr.package_consumption_master_id as \"packageConsumptionMasterId\", "
					+ "	mppr.package_consumption_cap_details_id as \"packageConsumptionCapDetailsId\", "
					+ "	mppr.package_qty as \"packageQty\", "
					+ "	mppr.package_consumtion_eo_detail_id as \"packageConsumptionEODetailId\", package_master.package_type_id \"packageTypeId\" "
					+ " "
					+ "from "
					+ "	public.t_order_details_package_mapper mppr "
					+ "left join service.m_package_master package_master "
					+ "	on mppr.package_id = package_master.package_master_id "
					+ "left join service.m_service_master pack_service on "
					+ "	package_master.service_id = pack_service.service_master_id "
					+ "left join service.m_service_master service on "
					+ "	mppr.service_id = service.service_master_id "
					+ "left join public.m_item item on "
					+ "	mppr.item_id = item.id "
					+ "where "
					+ "	mppr.status = 'A' "
					+ "	and mppr.order_details_id = :orderDetailsId "
					+ "order by "
					+ "	mppr.id,mppr.package_id desc";

			List<OrderDetailsPackageMapperDto> list = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("orderDetailsId", orderDetailsId)
					.setResultTransformer(Transformers.aliasToBean(OrderDetailsPackageMapperDto.class)).list();

			return new Response(SUCCESS, null, null, list, null);
		} catch (Exception e) {
		logger.error("getOrderDetailsPackageMapperByOrderDetailsId failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response saveOrderDetailsMaster(OrderDetailsMaster masterDto,List<OrderDetailsPayeeMapperDto> list,List<OrderDetailsPackageMapperDto> pckgMpprList) throws BillingException
	{
		try {
			
			Double serviceAmt = (masterDto.getOrdTotalAmt().subtract(masterDto.getOrdDiscount().add(masterDto.getOrdConcession()))).doubleValue();
			if((masterDto.getPackageConsumptionAmt().doubleValue() != 0.00 || masterDto.getPackageId()!=null) && serviceAmt.equals(masterDto.getPackageConsumptionAmt().doubleValue()))
				masterDto.setIsPackageService('Y');
			else
				masterDto.setIsPackageService('N');
			
			masterDto.setUpdatedDate(TimeZoneComponent.getDateByzone(masterDto.getOrgUnitId()).getTime());
			
			if(masterDto.getOrderDetailsId()>0)
			{
				sessionFactory.getCurrentSession().update(masterDto);
			}else{
				masterDto.setCreatedDate(TimeZoneComponent.getDateByzone(masterDto.getOrgUnitId()).getTime());
				sessionFactory.getCurrentSession().save(masterDto);
			}
			if(list!=null && list.size()>0)
			{
				sessionFactory.getCurrentSession().createQuery(" DELETE FROM OrderDetailsPayeeMapper "
		        		+ " WHERE orderDetailsId ="+masterDto.getOrderDetailsId())
						.executeUpdate();
			
				for(OrderDetailsPayeeMapperDto dto : list)
				{
					OrderDetailsPayeeMapper detailsPayeeMapper = new OrderDetailsPayeeMapper();
						detailsPayeeMapper.setOrderDetailsId(masterDto.getOrderDetailsId());
						detailsPayeeMapper.setContractId(dto.getContractId());
						detailsPayeeMapper.setPayeeId(dto.getPayeeId());
						detailsPayeeMapper.setAssociateCompanyId(dto.getAssociateCompanyId());
						detailsPayeeMapper.setPayeeAmount(dto.getPayeeAmount());
						detailsPayeeMapper.setSplitBillAmount(dto.getPayeeAmount());
						detailsPayeeMapper.setServiceId(dto.getServiceId());
						detailsPayeeMapper.setOrganizationId(masterDto.getOrgId());
						detailsPayeeMapper.setUnitId(masterDto.getOrgUnitId());
						if(masterDto.getStatus()=='A')
							detailsPayeeMapper.setStatus('A');
						else
							detailsPayeeMapper.setStatus('I');
					sessionFactory.getCurrentSession().save(detailsPayeeMapper);	
				}
			}
			
			//For package
			if(pckgMpprList!=null && pckgMpprList.size()>0)
			{
				sessionFactory.getCurrentSession().createQuery(" DELETE FROM OrderDetailsPackageMapper "
		        		+ " WHERE orderDetailsId ="+masterDto.getOrderDetailsId())
						.executeUpdate();
				for(OrderDetailsPackageMapperDto dto : pckgMpprList)
				{
					OrderDetailsPackageMapper detailsPackageMapper = new OrderDetailsPackageMapper();
						detailsPackageMapper.setOrderDetailsId(masterDto.getOrderDetailsId());
						detailsPackageMapper.setPayeeId(dto.getPayeeId());
						detailsPackageMapper.setPackageId(dto.getPackageId());
						detailsPackageMapper.setIsPackageService(dto.getIsPackageService());
						detailsPackageMapper.setIsCapService(dto.getIsCapService());
						detailsPackageMapper.setPackageAmount(dto.getPackageAmount());
						detailsPackageMapper.setServiceId(dto.getServiceId());
						detailsPackageMapper.setServiceAmount(dto.getServiceAmount());
						detailsPackageMapper.setOrganizationId(masterDto.getOrgId());
						detailsPackageMapper.setUnitId(masterDto.getOrgUnitId());
						detailsPackageMapper.setPackageQty(dto.getPackageQty());
						detailsPackageMapper.setPacakgeConsumptionServiceDetailId(dto.getPacakgeConsumptionServiceDetailId());
						detailsPackageMapper.setPackageConsumptionCapDetailsId(dto.getPackageConsumptionCapDetailsId());
						detailsPackageMapper.setPackageConsumptionMasterId(dto.getPackageConsumptionMasterId());
						detailsPackageMapper.setPackageConsumptionEODetailId(dto.getPackageConsumptionEODetailId());
	 					detailsPackageMapper.setStatus('A');
					sessionFactory.getCurrentSession().save(detailsPackageMapper);
				}
			}
			
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		}catch (Exception e) {
		logger.error("saveOrderDetailsMaster failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public EncounterMasterDto getOrderAppointmentDetails(Integer orderDetailsId)
	{
		try{
			String query = "select " 
			 + " 	enc.encounter_id as \"encounterId\", " 
			 + " 	app.appointment_id as \"appointmentId\", " 
			 + " 	app.slot_id as \"slotId\" " 
			 + " 	 " 
			 + " from " 
			 + " 	t_order_details od " 
			 + " inner join t_order_master om on " 
			 + " 	om.order_id = od.order_id " 
			 + " inner join opd.t_encounter_master enc on " 
			 + " 	enc.encounter_id = om.encounter_id " 
			 + " inner join opd.m_appointment_master app on " 
			 + " 	enc.appointment_id = app.appointment_id	 " 
			 + " where " 
			 + " 	od.order_details_id = "+orderDetailsId 
			 + " 	and od.is_consultation_service = 'Y' limit 1" ;
			
			EncounterMasterDto encounterMasterDto = (EncounterMasterDto) sessionFactory.getCurrentSession()
					.createSQLQuery(query)
			.setResultTransformer(Transformers.aliasToBean(EncounterMasterDto.class))
			.uniqueResult();
			return encounterMasterDto;
		}catch(Exception e){
		logger.error("getOrderAppointmentDetails failed", e);
		}
		return null;
	}

	@Override
	public Response updateEncounterToOrderMaster(EncounterMasterDto dto, Integer orderId) 
	{
		try {
				sessionFactory.getCurrentSession().createQuery(" UPDATE OrderMaster SET encounterId="+dto.getEncounterId()
		        		+ " WHERE orderId ="+orderId)
						.executeUpdate();
	
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		}catch (Exception e) {
		logger.error("updateEncounterToOrderMaster failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response saveOrderDetailsMasterForPayee(OrderDetailsMaster masterDto,
			List<OrderDetailsPayeeMapperDto> list,
			List<OrderDetailsPackageMapperDto> pckgMpprList) throws BillingException 
	{
		try {
			
			Session session = sessionFactory.openSession();
			Session session1 = sessionFactory.openSession();
			Transaction t = session.beginTransaction();
			Transaction t1 = session1.beginTransaction();
			
			masterDto.setCreatedDate(TimeZoneComponent.getDateByzone(masterDto.getOrgUnitId()).getTime());
			masterDto.setUpdatedDate(TimeZoneComponent.getDateByzone(masterDto.getOrgUnitId()).getTime());
			if(masterDto.getCoPayPer()!=null && masterDto.getCreditPayable().compareTo(BigDecimal.ZERO)==1)
				masterDto.setCoPayPer(masterDto.getCoPayPer()); 
			else {
				if(list.size()>1)
					masterDto.setCoPayPer(new BigDecimal((masterDto.getSelfPayable().doubleValue()/masterDto.getNetAmount().doubleValue())*100));
				else
					masterDto.setCoPayPer(BigDecimal.ZERO);
			}
			
			
			if( masterDto.getOrderDetailsId()>0)
				session.update(masterDto);
			else
				 session.save(masterDto);
			
			if(list!=null && list.size()>0)
			{
				session1.createQuery(" UPDATE OrderDetailsPayeeMapper SET status='I' "
		        		+ " WHERE orderDetailsId =:orderDetailsId ")
						.setParameter("orderDetailsId",masterDto.getOrderDetailsId()).executeUpdate();
				
				for(OrderDetailsPayeeMapperDto dto : list)
				{
					OrderDetailsPayeeMapper detailsPayeeMapper = new OrderDetailsPayeeMapper();
						detailsPayeeMapper.setOrderDetailsId(masterDto.getOrderDetailsId());
						detailsPayeeMapper.setContractId(dto.getContractId());
						detailsPayeeMapper.setPayeeId(dto.getPayeeId());
						detailsPayeeMapper.setAssociateCompanyId(dto.getAssociateCompanyId());
						detailsPayeeMapper.setPayeeAmount(dto.getPayeeAmount());
						detailsPayeeMapper.setSplitBillAmount(dto.getPayeeAmount());
						detailsPayeeMapper.setServiceId(dto.getServiceId());
						detailsPayeeMapper.setOrganizationId(masterDto.getOrgId());
						detailsPayeeMapper.setUnitId(masterDto.getOrgUnitId());
						if(masterDto.getStatus()=='A')
							detailsPayeeMapper.setStatus('A');
						else
							detailsPayeeMapper.setStatus('I');
						session.save(detailsPayeeMapper);	
				}
			}
			
			//For package
			if(pckgMpprList!=null && pckgMpprList.size()>0)
			{
				session1.createQuery(" UPDATE OrderDetailsPackageMapper SET status='I' "
		        		+ " WHERE orderDetailsId =:orderDetailsId ")
						.setParameter("orderDetailsId",masterDto.getOrderDetailsId()).executeUpdate();
				
				for(OrderDetailsPackageMapperDto dto : pckgMpprList)
				{
					OrderDetailsPackageMapper detailsPackageMapper = new OrderDetailsPackageMapper();
					detailsPackageMapper.setOrderDetailsId(masterDto.getOrderDetailsId());
					detailsPackageMapper.setPayeeId(dto.getPayeeId());
					detailsPackageMapper.setPackageId(dto.getPackageId());
					detailsPackageMapper.setIsPackageService(dto.getIsPackageService());
					detailsPackageMapper.setIsCapService(dto.getIsCapService());
					detailsPackageMapper.setPackageAmount(dto.getPackageAmount());
					detailsPackageMapper.setServiceId(dto.getServiceId());
					detailsPackageMapper.setServiceAmount(dto.getServiceAmount());
					detailsPackageMapper.setOrganizationId(masterDto.getOrgId());
					detailsPackageMapper.setUnitId(masterDto.getOrgUnitId());
					detailsPackageMapper.setPackageQty(dto.getPackageQty());
 					detailsPackageMapper.setStatus('A');
 					session.save(detailsPackageMapper);
				}
			}
			t.commit();
			t1.commit();
			session.close();
			session1.close();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		}catch (Exception e) {
		logger.error("saveOrderDetailsMasterForPayee failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Integer getDepartmentIdByServiceId(Integer serviceId,Integer orgId) throws BillingException {
	try{
			
			String query = "select " 
					 + " 	speciality_id "
					 + " from "
					 + " 	service.m_service_master "
					 + " where "
					 + " 	 service_master_id =:serviceId and organization_id =:orgId  and status = 'A' " ;
			
			Integer serId = (Integer)sessionFactory.getCurrentSession()
					.createSQLQuery(query)
					.setParameter("serviceId",serviceId)
					.setParameter("orgId",orgId)
					.uniqueResult();
			
			return serId;
		}catch(Exception e)
		{
		logger.error("getDepartmentIdByServiceId failed", e);
		}
		return 0;
	}

	@Override
	public Response updateOrderDetail(List<Integer> trmpServiceIdList,
			Set<Integer> serviceIdList,Set<Integer> orderDetailsIdList,Integer unitId) {
		try{
			
			
				if (trmpServiceIdList != null && trmpServiceIdList.size() > 0) 
				{ // make active
					sessionFactory.getCurrentSession().createQuery("Update OrderDetailsMaster orderDet "
							+ " set orderDet.isOrderVisibleDepartment='A' where orderDet.orderDetailsId IN (:odIds) and orderDet.serviceId in (:serviceIds)"
							+ " and orderDet.orderDetailsId NOT IN (select orderDetailId from CyclicAppointmentMapper where orderDetailId in (:odIds)) ")
							.setParameterList("serviceIds", trmpServiceIdList).setParameterList("odIds", orderDetailsIdList)
							.executeUpdate();
				}
			 
				
				if(serviceIdList!=null && serviceIdList.size()>0)
				{
					//make inactive
					sessionFactory.getCurrentSession().createQuery("Update OrderDetailsMaster orderDet "
							+ " set orderDet.isOrderVisibleDepartment='I' where orderDet.orderDetailsId IN (:odIds) and orderDet.serviceId in (:serviceIds) "
							+ " and orderDet.orderDetailsId NOT IN (select orderDetailId from CyclicAppointmentMapper where orderDetailId in (:odIds)) ")
					.setParameterList("serviceIds", serviceIdList)
    				.setParameterList("odIds", orderDetailsIdList)
					.executeUpdate();
				}
				
				if(trmpServiceIdList!=null && trmpServiceIdList.size()>0)
				{
					List<Integer> serviceIds=sessionFactory.getCurrentSession()
				    		 .createQuery("select unitSer.serviceMasterId from UnitServiceMasterDetails unitSer "
				    		 		+ "where unitSer.serviceMasterId in (:serviceIds) "
				    		 		+" and unitSer.unitId=:unitId and unitSer.status='A' and unitSer.isCyclicFlag != 'Y' ")
				    		 .setParameterList("serviceIds", trmpServiceIdList)
				    		 .setParameter("unitId", unitId)
				    .list();
				
					if(serviceIds!=null && serviceIds.size()>0)
					{
						//if service is not cyclic then make it as active by default
						sessionFactory.getCurrentSession().createQuery("Update OrderDetailsMaster orderDet "
								+ " set orderDet.isOrderVisibleDepartment='A' where orderDet.orderDetailsId IN (:odIds) "
								+ " and orderDet.serviceId in (:serviceIds) ")
						.setParameterList("serviceIds", serviceIds)
						.setParameterList("odIds", orderDetailsIdList)
						.executeUpdate();
					}
				}
				
		
		return new Response<>(SUCCESS, null, null, null, null);
		}catch(Exception e)
		{
		logger.error("updateOrderDetail failed", e);
		}
	return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public OrderDetailsMaster getOrderDetailsForRewiewAppointment(Integer orderDetailsMasterId) throws BillingException {
		try {
			OrderDetailsMaster orderDetailsMasterDto = findById(orderDetailsMasterId);
			return orderDetailsMasterDto;

		} catch (HibernateException he) {
		logger.error("getOrderDetailsForRewiewAppointment failed", he);
		} catch (Exception e) {
		logger.error("getOrderDetailsForRewiewAppointment failed", e);
		}
		return null;
	}

	@Override
	public Response updateOrdDocIdAndSpecialityId(OrderDetailsMasterDto orderDetailsMasterDto) {
		try {
			Session session=sessionFactory.openSession();
			Transaction trans=session.beginTransaction();
			session.createQuery("update OrderDetailsMaster ord set ord.ordDocId=:ordDocId,ord.ordDocSplId=:ordSpecId"
					+ " where ord.orderDetailsId=:orderDetailId ")
			.setParameter("ordDocId", orderDetailsMasterDto.getOrdDocId())
			.setParameter("ordSpecId", orderDetailsMasterDto.getOrdDocSplId())
			.setParameter("orderDetailId", orderDetailsMasterDto.getOrderDetailsId())
			.executeUpdate();
			trans.commit();
			session.close();
		} catch (HibernateException he) {
		logger.error("updateOrdDocIdAndSpecialityId failed", he);
		} catch (Exception e) {
		logger.error("updateOrdDocIdAndSpecialityId failed", e);
		} 
		return null;
	}

	@Override
	public Response inactiveSaleInOrderDetails(Integer saleId) {
		try {
			sessionFactory.getCurrentSession().createSQLQuery("update public.t_order_details set status  = 'I' where order_id in (select order_id  from public.t_order_master  where sale_id  = :saleId)")
			.setParameter("saleId", saleId)
			.executeUpdate();
			
			sessionFactory.getCurrentSession().createSQLQuery("update public.t_order_master set order_status  = 'I' where  sale_id  = :saleId")
			.setParameter("saleId", saleId)
			.executeUpdate();

		} catch (HibernateException he) {
		logger.error("inactiveSaleInOrderDetails failed", he);
		} catch (Exception e) {
		logger.error("inactiveSaleInOrderDetails failed", e);
		} 
		return null;
	}

	@Override
	public Response updateOrderDetailsObj(OrderDetailsMaster orderDetailsMasterDto) {
		try {
			
			sessionFactory.getCurrentSession().update(orderDetailsMasterDto);
			
		} catch (HibernateException he) {
		logger.error("updateOrderDetailsObj failed", he);
		} catch (Exception e) {
		logger.error("updateOrderDetailsObj failed", e);
		}
		return null;
	}

	@Override
	public Response updateOrderDetailsPayeeMapper(OrderDetailsPayeeMapperDto detailsPayeeMapperDto) {
		try {
				sessionFactory.getCurrentSession().createQuery("update OrderDetailsPayeeMapper ord set payeeAmount=:payeeAmt "
						+ " where id=:id ")
				.setBigDecimal("payeeAmt", detailsPayeeMapperDto.getPayeeAmount())
				.setParameter("id", detailsPayeeMapperDto.getId())
			.executeUpdate();

		} catch (HibernateException he) {
		logger.error("updateOrderDetailsPayeeMapper failed", he);
		} catch (Exception e) {
		logger.error("updateOrderDetailsPayeeMapper failed", e);
		}
		return null;
	}

	@Override
	public Response cancelProcedureSerivicesByProcedure(ProcedureOrderMasterDto order) {
		try {
			sessionFactory.getCurrentSession()
					.createSQLQuery("UPDATE public.t_order_details SET "
							+ "status = 'I', "
							+ "ord_cancelled = 'Y', "
							+ "ord_cancel_datetime = :cancelDate, "
							+ "updated_by = :updatedBy, "
							+ "updated_date = :updateDate "
							+ "WHERE procedure_order_mst_id = :procedureOrderMstId")
					.setParameter("cancelDate", GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(sessionService.getUnitId() != null ? sessionService.getUnitId() : 0), "dd-M-yyyy HH:mm:ss"))
					.setParameter("updatedBy", order.getCreatedBy())
					.setParameter("updateDate", GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(sessionService.getUnitId() != null ? sessionService.getUnitId() : 0), "dd-M-yyyy HH:mm:ss"))
					.setParameter("procedureOrderMstId", order.getProReqId())
					.executeUpdate();
			return new Response(SUCCESS, null, null, null, null);
		} catch (HibernateException he) {
		logger.error("cancelProcedureSerivicesByProcedure failed", he);
		} catch (Exception e) {
		logger.error("cancelProcedureSerivicesByProcedure failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public EncounterMasterDto checkConsultationService(Integer encounterId) {
		try {
			EncounterMasterDto encounterMasterDto = (EncounterMasterDto)sessionFactory.getCurrentSession()
							.createSQLQuery(" select "
										+"		tod.order_details_id as \"orderDetailsId\", "
										+ "		enc.appointment_id as \"appointmentId\" "
										+"	from "
										+"		t_order_details tod "
										+"	inner join t_order_master om on "
										+"		om.order_id = tod.order_id "
										+"	inner join opd.t_encounter_master enc on "
										+"		enc.encounter_id = om.encounter_id "
										+"	where "
										+"		om.encounter_id =:encId "
										+"		and tod.is_consultation_service ='Y' "
										+"		and tod.service_rendered = 0 ")
									.setParameter("encId",encounterId)
									.setResultTransformer(Transformers.aliasToBean(EncounterMasterDto.class))
								.uniqueResult();
			
			return encounterMasterDto;

		} catch (HibernateException he) {
		logger.error("checkConsultationService failed", he);
		} catch (Exception e) {
		logger.error("checkConsultationService failed", e);
		}
		return null;
	}

	@Override
	public Integer checkIsBilledServices(Set<Integer> orderDetailsIdList) {
		try{
			
			BigInteger  count= (BigInteger)sessionFactory.getCurrentSession()
					.createSQLQuery("select count(order_details_id) from t_order_details tod where order_details_id  in (:ids) and service_is_billed = 1 ")
					.setParameterList("ids", orderDetailsIdList)
					.uniqueResult();
			if(count.compareTo(BigInteger.ZERO)==0)
				return 0;
			else
				return 1;
		}catch(HibernateException e)
		{
		logger.error("checkIsBilledServices failed", e);
		}
		return 1;

	}

	@Override
	public Response getEhcOrderedServices(AdmissionsForBatchProcessDto master) throws ApplicationException {
		try {
			List<OrderDetailsByEncounterIdDto> listServiceMasterDto = null;
			StringBuilder stringQuery = new StringBuilder(sessionFactory.getCurrentSession()
					.getNamedQuery("GET_EHC_SERVICES_BY_PACKAGE_ID").getQueryString());
			stringQuery.append(" order by  orderDetails.orderDetailsId " );
			listServiceMasterDto = sessionFactory.getCurrentSession()
					.createQuery(stringQuery.toString())
					.setParameter("packageId", master.getPackageId())
					.setParameter("encounterId", master.getEncounterId())
					.setResultTransformer(Transformers.aliasToBean(OrderDetailsByEncounterIdDto.class)).list();

			return new Response<>(SUCCESS, SUCCESS_CODE, null, listServiceMasterDto, null);
		} catch (Exception e) {
		logger.error("getEhcOrderedServices failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getOrderDetailsPackageMapperByOrderDetailsId(List<Integer> orderDetailsIds) {
		try {

			List<OrderDetailsPackageMapperDto> list = null;
			if(orderDetailsIds!=null && orderDetailsIds.size()>0)
			{
				String query ="select "
						+ "	mppr.id as \"id\", "
						+ "	mppr.order_details_id as \"orderDetailsId\", "
						+ "	mppr.service_id as \"serviceId\", "
						+ "	mppr.package_id as \"packageId\", "
						+ "	mppr.payee_id as \"payeeId\", "
						+ "	pack_service.service_standard_code as \"packageCode\", "
						+ "	pack_service.service_standard_name as \"packageName\", "
						+ "	service.service_standard_code as \"serviceStandardCode\", "
						+ "	service.service_standard_name as \"serviceStandardName\", "
						+ "	mppr.service_amount as \"serviceAmount\", "
						+ "	mppr.package_amount \"packageAmount\", "
						+ "	mppr.is_package_service as \"isPackageService\", "
						+ "	mppr.is_cap_service as \"isCapService\", "
						+ "	item.\"name\" as \"itemName\", "
						+ "	item.code as \"itemCode\", "
						+ "	mppr.pacakge_consumption_service_detail_id as \"pacakgeConsumptionServiceDetailId\", "
						+ "	mppr.package_consumption_master_id as \"packageConsumptionMasterId\", "
						+ "	mppr.package_consumption_cap_details_id as \"packageConsumptionCapDetailsId\", "
						+ "	mppr.package_qty as \"packageQty\", "
						+ "	mppr.package_consumtion_eo_detail_id as \"packageConsumptionEODetailId\", package_master.package_type_id \"packageTypeId\" "
						+ " "
						+ "from "
						+ "	public.t_order_details_package_mapper mppr "
						+ "left join service.m_package_master package_master "
						+ "	on mppr.package_id = package_master.package_master_id "
						+ "left join service.m_service_master pack_service on "
						+ "	package_master.service_id = pack_service.service_master_id "
						+ "left join service.m_service_master service on "
						+ "	mppr.service_id = service.service_master_id "
						+ "left join public.m_item item on "
						+ "	mppr.item_id = item.id "
						+ "where "
						+ "	mppr.status = 'A' "
						+ "	and mppr.order_details_id in (:ids) "
						+ "order by "
						+ "	mppr.id,mppr.package_id desc";

				
				 list = sessionFactory.getCurrentSession().createSQLQuery(query)
						.setParameterList("ids", orderDetailsIds)
						.setResultTransformer(Transformers.aliasToBean(OrderDetailsPackageMapperDto.class)).list();
			}
			

			return new Response(SUCCESS, null, null, list, null);
		} catch (Exception e) {
		logger.error("getOrderDetailsPackageMapperByOrderDetailsId failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public List<OrderDetailsMasterDto> getItemDetailsFromOrderDetailsForUpdateReturnCalculation(Integer orderId) {
		
		try{
				String query = " select " 
						 + " 	tod.sale_detail_id as \"saleDetailId\", " 
						 + " 	tom.sale_id as \"saleId\", " 
						 + " 	tod.org_id as \"orgId\", " 
						 + " 	tod.org_unit_id as \"orgUnitId\", " 
						 + " 	tom.visit_type_id as \"visitTypeId\", " 
						 + " 	(tod.order_qty * -1) as \"orderQty\", " 
						 + " 	tod.batch_id as \"batchId\", " 
						 + " 	tod.drug_id as \"drugId\", " 
						 + " 	dtls.product_category_id as \"productCategoryId\", "
						 + "	to_char(tom.created_date,'YYYY-MM-DD HH24:MI:SS') as \"orderDateString\", "
						 + "	tod.order_details_id as \"orderDetailsId\", "
						 + "	tod.created_by as \"createdBy\", "
						 + "	(tod.net_amount + tod.package_consumption_amt)*-1 as \"netAmount\" " 
						 + " from " 
						 + " 	t_order_details tod " 
						 + " inner join t_order_master tom on " 
						 + " 	tod.order_id = tom.order_id " 
						 + " inner join pharmacy.t_sale_detail dtls on  " 
						 + " 	dtls.id = tod.sale_detail_id " 
						 + " where  " 
						 + " 	tom.order_id =:orderId "
						 + " 	and tod.is_drug ='Y' " 
						 + " 	and tod.status ='A' " 
						 + " 	and tod.order_qty < 0 ";
			
			List<OrderDetailsMasterDto> orderDetailsMasterDtosList = sessionFactory.getCurrentSession()
					.createSQLQuery(query)
					.setParameter("orderId",orderId)
					.setResultTransformer(Transformers.aliasToBean(OrderDetailsMasterDto.class))
					.list();
			return orderDetailsMasterDtosList;
		}catch(Exception e){
		logger.error("getItemDetailsFromOrderDetailsForUpdateReturnCalculation failed", e);
		}
		return null;
	}

	@Override
	public Response inactiveOldOrderDetails(Integer orderId) {
		try{
			String query = "delete from t_order_details where order_id =:orderId " ;
			sessionFactory.getCurrentSession().createSQLQuery(query).setParameter("orderId",orderId).executeUpdate();
		return new Response(SUCCESS, null, null, null, null);
	}catch(Exception e){
	logger.error("inactiveOldOrderDetails failed", e);
	}
	return null;
	}
	
	private int saveOrderDetailsMappers(List<OrderDetailsPackageMapper> orderDetailspackageMapperDtoFinalList,List<OrderDetailsPayeeMapper> orderDetailsPayeeMapperDtoFinalList,Connection connectionObj)
	{
		Connection connection =null;
		PreparedStatement payeeStatement =null;
		PreparedStatement packageStatement = null;
		try {
			connection = connectionObj!=null ? connectionObj : driverManager.getConnection();
			//connection.setAutoCommit(true);

			  packageStatement = connection.prepareStatement("INSERT INTO public.t_order_details_package_mapper "
		        		+ "(order_details_id, service_id, package_id, is_package_service, is_cap_service, payee_id, service_amount, package_amount, unit_id, organization_id, status, package_consumption_master_id, package_consumption_cap_details_id, pacakge_consumption_service_detail_id, package_qty, package_consumtion_eo_detail_id) "
		        		+ "VALUES(?,?,?,?,?,?,?,?,?,?,'A',?,?,?,?,?);");
				 int j = 0;
				 for(OrderDetailsPackageMapper dto:orderDetailspackageMapperDtoFinalList)
				 {

					 if(dto.getServiceId()!= null){

							 packageStatement.setInt(1, dto.getOrderDetailsId());
							 packageStatement.setInt(2, dto.getServiceId());

							 if(dto.getPackageId()!=null && dto.getPackageId()>0)
								 packageStatement.setInt(3, dto.getPackageId());
							 else
								 packageStatement.setNull(3, Types.NULL);

							 packageStatement.setObject(4, dto.getIsPackageService());
							 packageStatement.setObject(5, dto.getIsCapService());

							 if(dto.getPayeeId()!=null && dto.getPayeeId()>0)
								 packageStatement.setInt(6, dto.getPayeeId());
							 else
								 packageStatement.setNull(6, Types.NULL);

							 packageStatement.setDouble(7, dto.getServiceAmount().doubleValue());
							 packageStatement.setDouble(8, dto.getPackageAmount().doubleValue());
							 packageStatement.setInt(9, dto.getUnitId());
							 packageStatement.setInt(10,dto.getOrganizationId());

							 if(dto.getPackageConsumptionMasterId()!=null && dto.getPackageConsumptionMasterId()>0)
								 packageStatement.setInt(11, dto.getPackageConsumptionMasterId());
							 else
								 packageStatement.setNull(11, Types.NULL);

							 if(dto.getPackageConsumptionCapDetailsId()!=null && dto.getPackageConsumptionCapDetailsId()>0)
								 packageStatement.setInt(12, dto.getPackageConsumptionCapDetailsId());
							 else
								 packageStatement.setNull(12, Types.NULL);

							 if(dto.getPacakgeConsumptionServiceDetailId()!=null && dto.getPacakgeConsumptionServiceDetailId()>0)
								 packageStatement.setInt(13, dto.getPacakgeConsumptionServiceDetailId());
							 else
								 packageStatement.setNull(13, Types.NULL);

							 if(dto.getPackageQty()!=null && dto.getPackageQty()>0)
								 packageStatement.setInt(14, dto.getPackageQty());
							 else
								 packageStatement.setNull(14, Types.NULL);

							 if(dto.getPackageConsumptionEODetailId()!=null && dto.getPackageConsumptionEODetailId()>0)
								 packageStatement.setInt(15, dto.getPackageConsumptionEODetailId());
							 else
								 packageStatement.setNull(15, Types.NULL);

							 packageStatement.addBatch();
						 }
						 j++;
						 if (j % 500 == 0 || j == orderDetailspackageMapperDtoFinalList.size())
						 {
							//System.out.println(j);
							try{
								packageStatement.executeBatch(); // Execute every 20 items.
				              //  connection.commit();
							}catch(BatchUpdateException e){
								System.out.println(j+"exception while saving save Order master Details Package mapper"+ packageStatement );
								throw e.getNextException();
							}

						 }


				 }

				  payeeStatement = connection.prepareStatement("INSERT INTO public.t_order_details_payee_mapper "
			        		+ "(order_details_id, service_id, contract_id, payee_id, payee_amount, unit_id, organization_id, status, split_bill_amount, associate_company_id) "
			        		+ "VALUES(?,?,?,?,?,?,?,'A',?,?);");
					 int k = 0;
					 for(OrderDetailsPayeeMapper dto : orderDetailsPayeeMapperDtoFinalList)
					 {
						 if(dto.getServiceId()!= null){

							 payeeStatement.setInt(1, dto.getOrderDetailsId());
							 payeeStatement.setInt(2, dto.getServiceId());
							 if(dto.getContractId()!=null)
								 payeeStatement.setInt(3, dto.getContractId());
							 else
								 payeeStatement.setNull(3, Types.NULL);

							 payeeStatement.setInt(4, dto.getPayeeId());
							 payeeStatement.setDouble(5, dto.getPayeeAmount()!=null ? dto.getPayeeAmount().doubleValue() : 0.00);
							 payeeStatement.setInt(6, dto.getUnitId());
							 payeeStatement.setInt(7, dto.getOrganizationId());
							 payeeStatement.setDouble(8, dto.getPayeeAmount()!=null ? dto.getPayeeAmount().doubleValue() : 0.00);

							 if(dto.getAssociateCompanyId()!=null)
								 payeeStatement.setInt(9, dto.getAssociateCompanyId());
							 else
								 payeeStatement.setNull(9,Types.NULL);

							 payeeStatement.addBatch();

						 }
						 k++;
							 if (k % 500 == 0 || k == orderDetailsPayeeMapperDtoFinalList.size())
							 {

								 try{
									 payeeStatement.executeBatch(); // Execute every 20 items.
									 //  connection.commit();
								 }catch(BatchUpdateException e){
									 System.out.println(k+"exception while saving save Order master Details payee mapper"+ payeeStatement );
									 throw e.getNextException();
								 }

							 }


					 }

			return 0;
		}catch (Exception e) {
		logger.error("saveOrderDetailsMappers failed", e);
		return 1;
		}finally{
			try{
				if (packageStatement != null) {
					packageStatement.close();
				}
				if (payeeStatement != null) {
					payeeStatement.close();
				}
				connection.close();
			}catch(Exception e){
			logger.error("saveOrderDetailsMappers failed", e);
			}
		}
	}
	
	@Override
	public Response requestDiscountDetails(RequestDiscountDetailsDto requestDiscountDetailsDto) throws ApplicationException {
		try {
				RequestDiscountDetails requestDiscount = mapper.map(requestDiscountDetailsDto, RequestDiscountDetails.class,
						"RequestDiscountDetailsDto_to_RequestDiscountDetails");
				sessionFactory.getCurrentSession().save(requestDiscount);
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (HibernateException he) {
		logger.error("requestDiscountDetails failed", he);
		} catch (Exception e) {
		logger.error("requestDiscountDetails failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateOrderDetailsList(CoversheetTemplateMasterDto coversheetTemplateMasterDto) {
		try
		{
			Session session=sessionFactory.getCurrentSession();
			session.createSQLQuery(" update public.t_order_details  set status ='I'"
			+ " where cov_template_id=:covTemplateId ")
					.setParameter("covTemplateId",coversheetTemplateMasterDto.getCovTemplateId())
			.executeUpdate();
			return new Response(SUCCESS, SUCCESS_CODE, null, null, null);
		}catch(HibernateException he) {
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("updateOrderDetailsList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);

	}

	@Override
	public Response saveOrderDetailsMasterList(OrderDetailsMasterDto orderDetailsMasterDto)
			throws ApplicationException {
		try {
			OrderDetailsMaster orderDetailsMaster = mapper.map(orderDetailsMasterDto, OrderDetailsMaster.class, "OrderDetailsMasterDto_to_OrderDetailsMaster");
				orderDetailsMaster.setDummyServiceAmt(orderDetailsMaster.getDummyServiceAmt()!=null ? orderDetailsMaster.getDummyServiceAmt() : orderDetailsMaster.getServiceAmt());
				orderDetailsMaster.setDebitNoteService(orderDetailsMasterDto.getDebitNoteService()==true?true:false);
				orderDetailsMaster.setIsFromAddservice(orderDetailsMasterDto.getIsFromAddservice()!=null ? orderDetailsMasterDto.getIsFromAddservice() : 'N');
				orderDetailsMaster.setNursingServiceOrderFlag(orderDetailsMasterDto.getNursingServiceOrderFlag()!=null ? orderDetailsMasterDto.getNursingServiceOrderFlag() : 'N');
				orderDetailsMaster.setIsOrderVisibleDepartment(orderDetailsMasterDto.getIsOrderVisibleDepartment()!=null  ? orderDetailsMasterDto.getIsOrderVisibleDepartment() : 'A');
				orderDetailsMaster.setOrdDocId(orderDetailsMasterDto.getOrdDocId()!=null && orderDetailsMasterDto.getOrdDocId()>0 ? orderDetailsMasterDto.getOrdDocId() : null);
				orderDetailsMaster.setOrdDocSplId(orderDetailsMasterDto.getOrdDocSplId()!=null && orderDetailsMasterDto.getOrdDocSplId()>0 ? orderDetailsMasterDto.getOrdDocSplId() :  orderDetailsMasterDto.getOrdDocSplId());
				orderDetailsMaster.setOrderSetId(orderDetailsMasterDto.getOrderSetId()!=null ? orderDetailsMasterDto.getOrderSetId() :null);
				orderDetailsMaster.setCreatedDate(orderDetailsMasterDto.getOrderDetailsId()!=null && orderDetailsMasterDto.getOrderDetailsId()>0 ? orderDetailsMasterDto.getCreatedDate() : TimeZoneComponent.getDateByzone(orderDetailsMasterDto.getOrgUnitId()).getTime());
				orderDetailsMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(orderDetailsMasterDto.getOrgUnitId()).getTime());
				orderDetailsMaster.setCovTemplateId(orderDetailsMasterDto.getCovTemplateId()!= null ? orderDetailsMasterDto.getCovTemplateId() : null);
				
				if(orderDetailsMasterDto.getCoPayPer()!=null && (orderDetailsMasterDto.getCreditPayable()!=null && orderDetailsMasterDto.getCreditPayable().compareTo(BigDecimal.ZERO)==1))
					orderDetailsMaster.setCoPayPer(orderDetailsMasterDto.getCoPayPer()); 
				else {
					if(orderDetailsMasterDto.getOrderDetailsPayeeMapperDtosList()!=null && orderDetailsMasterDto.getOrderDetailsPayeeMapperDtosList().size()>1)
						orderDetailsMaster.setCoPayPer(new BigDecimal((orderDetailsMasterDto.getSelfPayable().doubleValue()/orderDetailsMasterDto.getNetAmount().doubleValue())*100));
					else
						orderDetailsMaster.setCoPayPer(new BigDecimal(100));
				}
			orderDetailsMaster.setIsCapService(orderDetailsMaster.getIsCapService()=='\u0000'?'N':orderDetailsMaster.getIsCapService());
			//orderDetailsMaster.setIsPackageService(orderDetailsMaster.getIsPackageService()=='\u0000'?'N':orderDetailsMaster.getIsPackageService());
			orderDetailsMaster.setIsProcedure(orderDetailsMaster.getIsProcedure()=='\u0000'?'N':orderDetailsMaster.getIsProcedure());
			orderDetailsMaster.setIsProcedureService(orderDetailsMaster.getIsProcedureService()=='\u0000'?'N':orderDetailsMaster.getIsProcedureService());

				orderDetailsMaster=save(orderDetailsMaster);
			Integer orderDetailsId = orderDetailsMaster.getOrderDetailsId();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, orderDetailsId);
		}catch (Exception e) {
		logger.error("saveOrderDetailsMasterList failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateFreeOrderService(OrderDetailsMasterDto orderDetailsMasterDto) throws ApplicationException {
		try {	
			
			sessionFactory.getCurrentSession().createSQLQuery("update t_free_text_ordered_services set order_details_id =:orderDetailsId , order_id=:orderId , status ='I', is_consumed = true  where free_text_ordered_services_id  = :freeServiceId ")
				.setParameter("freeServiceId", orderDetailsMasterDto.getFreeTextOrderedServicesId())
				.setParameter("orderDetailsId", orderDetailsMasterDto.getOrderDetailsId())
				.setParameter("orderId", orderDetailsMasterDto.getOrderId())
				.executeUpdate();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
			}catch(Exception e) {
			logger.error("updateFreeOrderService failed", e);
			}
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response saveFreeTextInvestigations(FreeTextOrderedServicesDto freeTextOrderedServicesDto)
			throws ApplicationException {
		try{
			FreeTextOrderedServices freeTextOrderedServices = new FreeTextOrderedServices();
				freeTextOrderedServices.setServiceText(freeTextOrderedServicesDto.getServiceText() != null ? freeTextOrderedServicesDto.getServiceText() : "");
				freeTextOrderedServices.setIsConsumed(false);
				freeTextOrderedServices.setDoctorId(freeTextOrderedServicesDto.getDoctorId());
				freeTextOrderedServices.setCovTemplateId(freeTextOrderedServicesDto.getCovTemplateId());
				freeTextOrderedServices.setAdmissionId(freeTextOrderedServicesDto.getAdmissionId() != null ? freeTextOrderedServicesDto.getAdmissionId() : null);
				freeTextOrderedServices.setCreatedBy(freeTextOrderedServicesDto.getCreatedBy());
				freeTextOrderedServices.setPatientId(freeTextOrderedServicesDto.getPatientId());
				freeTextOrderedServices.setEncounterId(freeTextOrderedServicesDto.getEncounterId() != null ? freeTextOrderedServicesDto.getEncounterId() : null);
				freeTextOrderedServices.setOrderDetailsId(freeTextOrderedServicesDto.getOrderDetailsId());
				freeTextOrderedServices.setOrderId(freeTextOrderedServicesDto.getOrderId());
				freeTextOrderedServices.setUnitId(freeTextOrderedServicesDto.getUnitId());
				freeTextOrderedServices.setOrganizationId(freeTextOrderedServicesDto.getOrganizationId());
				freeTextOrderedServices.setStatus('A');
				freeTextOrderedServices.setCreatedDate(TimeZoneComponent.getDateByzone(freeTextOrderedServicesDto.getUnitId()));
				freeTextOrderedServices.setUpdatedBy(freeTextOrderedServicesDto.getUpdatedBy());
				freeTextOrderedServices.setUpdatedDate(TimeZoneComponent.getDateByzone(freeTextOrderedServicesDto.getUnitId()));
				sessionFactory.getCurrentSession().save(freeTextOrderedServices);
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		}catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response updateFreeTextInvestigations(CoversheetTemplateMasterDto coversheetTemplateMasterDto)
			throws ApplicationException {
		try
		{
			Session session=sessionFactory.getCurrentSession();
			session.createSQLQuery(" update public.t_free_text_ordered_services  set status ='I'"
			+ " where cov_template_id=:covTemplateId ")
					.setParameter("covTemplateId",coversheetTemplateMasterDto.getCovTemplateId())
			.executeUpdate();
			return new Response(SUCCESS, SUCCESS_CODE, null, null, null);
		}catch(HibernateException he) {
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("updateFreeTextInvestigations failed", e);
		}
		return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);

	}

	@Override
	public Response getInvestigationsListForCoversheet(Integer patientId, boolean b, Integer unitId) {
		
		StringBuilder sb = new  StringBuilder();
		sb.append("select "  + 
				" ord_mst.order_id as \"orderId\", " + 
				" service_mst.speciality_id as \"specialityId\"," + 
				" service_mst.sub_speciality_id as \"subDeptId\"," + 
				" service_mst.sub_speciality_id as \"subSpecialityId\"," + 
				"ord_dtls.is_procedure_service as \"isProcedureService\", " + 
				" ord_mst.admission_id as \"admissionId\", " + 
				" ord_mst.encounter_id as \"encounterId\", " + 
				" ord_mst.patient_id as \"patientId\", " + 
				" ord_dtls.order_details_id as \"orderDetailsId\", " + 
				" ord_dtls.service_id as \"serviceMasterId\", " + 
				" ord_dtls.order_set_id as \"orderSetId\", " + 
				" ord_dtls.priority_id as \"priorityId\", " + 
				" ord_dtls.ord_remarks as \"ordRemarks\", " + 
				" to_char(ord_dtls.order_sch_date,'dd/MM/yyyy') as \"orderSchDate\", " + 
				 " to_char(ord_dtls.order_date,'YYYY-MM-DD') as \"orderDateString\", "+
				 " to_char(ord_dtls.order_date, 'HH24:MI') AS  \"orderDateTime\", "+
				" to_char(ord_dtls.order_sch_date,'dd/MM/yyyy HH24:MI') as \"orderSchDateString\", " + 
				" ord_dtls.order_sch_date as \"orderSchDateNew\", " + 
				" service_mst.service_standard_name as \"serviceStandardName\", " + 
				" service_mst.service_standard_code as \"serviceStandardCode\", " + 
				" speciality_mst.speciality_name as \"specialityName\", " + 
				" sample_dtls.lab_sample_dtls_id as \"labSampleDtlsId\", " + 
				" service_dtls.is_panel as \"isPanel\", " + 
				" lab_res.lab_test_res_id  as \"labTestResId\", " + 
				" cast( " + 
				" 	null as integer " + 
				" ) as \"panelId\", " + 
				" (case when sample_dtls.micro_template_status_id is not null then test_status2.status_name else test_status.status_name end) as \"statusName\", " + 
				" (case when sample_dtls.micro_template_status_id is not null then test_status2.test_status_id else test_status.test_status_id end) as \"sampleStatusId\", " + 
				" to_char( " + 
				" 	ord_dtls.created_date, " + 
				" 	'dd/MM/yyyy HH24:MI' " + 
				" ) as \"createdDate\", " + 
				" to_char( " + 
				" 	sample_dtls.sample_gen_datetime, " + 
				" 	'dd/MM/yyyy HH24:MI' " + 
				" ) as \"sampleGenDatetime\", " + 
				" 	ord_dtls.created_date as \"createdDateNew\", " + 
				" ord_dtls.service_rendered as \"serviceRendered\", " + 
				" ord_dtls.is_package_service as \"isPackageService\", " + 
				" templatetr12_.template_transaction_id as \"templateTransactionId\"," + 
				" ord_dtls.service_is_billed as \"serviceIsBilled\", " +
				" ord_dtls.status as \"orderStatus\", " + 
				 " sample_dtls.micro_template_status_id as \"microTemplateStatusId\" , "
				 + " (case  when empmst1.employee_id is not null then concat(empmst1.first_name, ' ', empmst1.last_name) " + 
				 " when docmst1.doctor_id is not null then concat('Dr. ', docmst1.first_name, ' ', docmst1.last_name) "
				 + "  when sample_dtls.lab_sample_dtls_id is  null then concat(' ') "
				 + " else 'Super Admin' end) as \"collectedUserName\","

				+" (case " + 
				" when empmst.employee_id is not null then concat(empmst.first_name, ' ', empmst.last_name) " + 
				" when docmst.doctor_id is not null then concat('Dr. ', docmst.first_name, ' ', docmst.last_name)  " + 
				" else 'Admin' end) as \"userName\"," + 
				" ord_dtls.package_id as \"packageId\" " + 
				" from 	public.t_order_master ord_mst " + 
				" 		inner join public.t_order_details ord_dtls on " + 
				" ord_mst.order_id = ord_dtls.order_id " + 
				" 		inner join service.t_unit_service_mapper service_mppr on " + 
				" service_mppr.service_id = ord_dtls.service_id " + 
				" and service_mppr.unit_id = ord_dtls.org_unit_id " + 
				" 		inner join service.t_unit_service_master_details service_dtls on " + 
				" service_dtls.service_master_id = service_mppr.service_id " + 
				" and service_dtls.unit_id = ord_dtls.org_unit_id " + 
				" 		inner join service.m_service_master service_mst on " + 
				" service_mst.service_master_id = ord_dtls.service_id " + 
				" 		left join doctor.m_speciality_master speciality_mst on " + 
				" speciality_mst.speciality_id = service_mst.speciality_id " + 
				" 		left join lab.t_lab_sample_dtls sample_dtls on " +
				" sample_dtls.order_details_id = ord_dtls.order_details_id " + 
				" and sample_dtls.sample_status_id <> 4 " + 
				" 		left join lab.m_test_status_master test_status on " + 
				" test_status.test_status_id = sample_dtls.sample_status_id " + 
				"        	left join lab.m_test_status_master test_status2 on " + 
				" test_status2.test_status_id = sample_dtls.micro_template_status_id 	" + 
				" 		left join lab.t_lab_test_result lab_res on " + 
				" lab_res.lab_sample_dtls_id = sample_dtls.lab_sample_dtls_id " + 
				" and lab_res.sample_status_id in( 8, 9,6,17) AND  lab_res.status = 'A' " + 
				" 		left outer join public.t_template_transaction templatetr12_ on " + 
				" ord_dtls.order_details_id = templatetr12_.order_details_id " + 
				" and( " + 
				" 	templatetr12_.status = 'A' " + 
				" ) " + 
				" left join public.m_employee_master empmst on ord_mst.created_by = empmst.user_id " +
				" left join doctor.m_doctor_master docmst on ord_mst.created_by = docmst.user_id " + 
				" left join lab.t_microbiology_template microTemp on microTemp.lab_sample_dtls_id=sample_dtls.lab_sample_dtls_id " + 
				 " left join public.m_employee_master empmst1 on sample_dtls.sample_collection_by = empmst1.user_id " + 
	    		 " left join doctor.m_doctor_master docmst1 on sample_dtls.sample_collection_by = docmst1.user_id " + 
				" 		where " + 
				" service_mst.status = 'A' " + 
				" and ord_dtls.old_ord_dtl_id is null " + 
				" and service_dtls.is_package = 'I' " + 
				" and ord_dtls.nursing_service_order_flag = 'N' " + 
				" and service_dtls.is_panel = 'N' " + 
				 " and speciality_mst.speciality_id  in( select speciality_id from t_unit_speciality_mapper where unit_id ="+unitId+" and is_rendering_speciality = 'Y' ) " 
				);
       
			
			sb.append("and  ord_mst.patient_id  =:patientId " );
        
     sb.append(" union all"+
    		 " select " + 
    		 "ord_mst.order_id as \"orderId\", " + 
    		 "service_mst.speciality_id as \"specialityId\"," + 
    		 "service_mst.sub_speciality_id as \"subDeptId\"," + 
    		 "service_mst.sub_speciality_id as \"subSpecialityId\"," + 
    		 "ord_dtls.is_procedure_service as \"isProcedureService\", " + 
    		 "ord_mst.admission_id as \"admissionId\", " + 
    		 "ord_mst.encounter_id as \"encounterId\", " + 
    		 "ord_mst.patient_id as \"patientId\", " + 
    		 "ord_dtls.order_details_id as \"orderDetailsId\", " + 
    		 "ord_dtls.service_id as \"serviceMasterId\", " + 
    		 "	ord_dtls.order_set_id as \"orderSetId\", " + 
    		 "	ord_dtls.priority_id as \"priorityId\", " + 
    		 "	ord_dtls.ord_remarks as \"ordRemarks\", " + 
    		 "  to_char(ord_dtls.order_sch_date,'dd/MM/yyyy') as \"orderSchDate\", " + 
    		 " to_char(ord_dtls.order_date,'YYYY-MM-DD') as \"orderDateString\", "+
			 " to_char(ord_dtls.order_date, 'HH24:MI') AS  \"orderDateTime\", "+
    		 " to_char(ord_dtls.order_sch_date,'dd/MM/yyyy HH24:MI') as \"orderSchDateString\", " + 
    		 "  ord_dtls.order_sch_date as \"orderSchDateNew\", " + 
    		 "service_mst.service_standard_name as \"serviceStandardName\", " + 
    		 "service_mst.service_standard_code as \"serviceStandardCode\", " + 
    		 "speciality_mst.speciality_name as \"specialityName\", " + 
    		 "cast( " + 
    		 "	null as integer " + 
    		 ") as \"labSampleDtlsId\", " + 
    		 "service_dtls.is_panel as \"isPanel\", " + 
    		 "cast( " + 
    		 "	null as integer " + 
    		 ") as \"labTestResId\", " + 
    		 "panel.panel_id as \"panelId\", " + 
    		 "null as \"statusName\", " + 
    		 "0 as \"sampleStatusId\", " + 
    		 "to_char( " + 
    		 "	ord_dtls.created_date, " + 
    		 "	'dd/MM/yyyy HH24:MI' " + 
    		 ") as \"createdDate\", " + 
    		 " '' as \"sampleGenDatetime\", " + 
    		 "		ord_dtls.created_date as \"createdDateNew\", " + 
    		 "ord_dtls.service_rendered as \"serviceRendered\", " + 
    		 "ord_dtls.is_package_service as \"isPackageService\", " + 
    		 "templatetr12_.template_transaction_id as \"templateTransactionId\"," + 
//    		 " templatetr12_.template_object as \"templateObject\", " + 
    		 "ord_dtls.service_is_billed as \"serviceIsBilled\", " + 
    		 "ord_dtls.status as \"orderStatus\", " + 
//    		 "am.appointment_id as \"appointmentId\", " + 
//    		 "am.slot_id as \"slotId\", " + 
    		 " 0 as \"microTemplateStatusId\" , "
    		 +"'' as \"collectedUserName\"," 
    		+ "(case " + 
    		 "when empmst.employee_id is not null then concat(empmst.first_name, ' ', empmst.last_name) " + 
    		 "when docmst.doctor_id is not null then concat('Dr. ', docmst.first_name, ' ', docmst.last_name) " + 
    		 "else 'Admin' end) as \"userName\"," + 
    		 "ord_dtls.package_id as \"packageId\" " + 
    		 "	from " + 
    		 "public.t_order_master ord_mst " + 
    		 "	inner join public.t_order_details ord_dtls on " + 
    		 "ord_mst.order_id = ord_dtls.order_id " + 
    		 "	inner join service.t_unit_service_mapper service_mppr on " + 
    		 "service_mppr.service_id = ord_dtls.service_id " + 
    		 "and service_mppr.unit_id = ord_dtls.org_unit_id " + 
    		 "	inner join service.t_unit_service_master_details service_dtls on " + 
    		 "service_dtls.service_master_id = service_mppr.service_id " + 
    		 "and service_dtls.unit_id = ord_dtls.org_unit_id " + 
    		 "	inner join service.m_service_master service_mst on " + 
    		 "service_mst.service_master_id = ord_dtls.service_id " + 
    		 "	left join doctor.m_speciality_master speciality_mst on " + 
    		 "speciality_mst.speciality_id = service_mst.speciality_id " + 
//    		 "	left join t_unit_speciality_mapper unitSplMpr on " + 
//    		 "unitSplMpr.speciality_id = service_mst.speciality_id " + 
//    		 "and unitSplMpr.unit_id = service_mppr.unit_id " + 
    		 "	inner join lab.t_panel_master panel on " + 
    		 "panel.service_id = service_mst.service_master_id " + 
    		 "and panel.org_unit_id = service_mppr.unit_id " + 
    		 "	left outer join public.t_template_transaction templatetr12_ on " + 
    		 "ord_dtls.order_details_id = templatetr12_.order_details_id " + 
    		 "and( " + 
    		 "	templatetr12_.status = 'A' " + 
    		 ") " + 
//    		 "	left join opd.m_appointment_master am on " + 
//    		 "am.order_details_id = ord_dtls.order_details_id " + 
//    		 "	left join t_unit_application_billing_configuration uabc on " + 
//    		 "	uabc.unit_id = ord_dtls.org_unit_id and uabc.status='A'	 " + 
				/* "left join public.m_user_master um on ord_mst.created_by = um.user_id " + */
    		 "left join public.m_employee_master empmst on ord_mst.created_by = empmst.user_id " + 
    		 "left join doctor.m_doctor_master docmst on ord_mst.created_by = docmst.user_id " + 
    		 " where " + 
    		 "service_mst.status = 'A' " + 
    		 "and ord_dtls.old_ord_dtl_id is null " + 
    		 "and service_dtls.is_package = 'I' " + 
    		 "and service_dtls.is_panel = 'Y' " + 
    		 " and speciality_mst.speciality_id  in( select speciality_id from t_unit_speciality_mapper where unit_id ="+unitId+" and is_rendering_speciality = 'Y' ) " + 
    		 "	and ord_dtls.nursing_service_order_flag = 'N' " 
//    		 " and ord_dtls.is_procedure_service = 'N' " 
    		 );
       
     		sb.append("and  ord_mst.patient_id  =:patientId  order by \"createdDateNew\" desc ");
     		
        	Query createdQuery=sessionFactory.getCurrentSession().createSQLQuery(sb.toString());
		createdQuery.setParameter("patientId",patientId);
        	List<InvestigationOrderMasterNewDto> investigationDTOsList = createdQuery
        	.setResultTransformer(Transformers.aliasToBean(InvestigationOrderMasterNewDto.class)).list();
		
		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS, investigationDTOsList, null);	
	}

	@Override
	public Response getInvestigationsListForNewEMR(Integer patientId, boolean b, Integer unitId) {
		try {
			List<InvestigationOrderMasterNewDto> listlabOrder=sessionFactory.getCurrentSession()

					.createSQLQuery("select " + 
							"	ord_mst.order_id as \"orderId\",   " + 
							"	ord_mst.encounter_id as \"encounterId\",   " + 
							"	service_mst.speciality_id as \"specialityId\",   " + 
							"	service_mst.sub_speciality_id as \"subDeptId\",   " + 
							"	service_mst.sub_speciality_id as \"subSpecialityId\",   " + 
							"	ord_dtls.order_details_id as \"orderDetailsId\",   " + 
							"	ord_dtls.service_id as \"serviceMasterId\",   " + 
							"	ord_dtls.order_set_id as \"orderSetId\",   " + 
							"	ord_dtls.priority_id as \"priorityId\",   " + 
							"	ord_dtls.ord_remarks as \"ordRemarks\",   " + 
							"	to_char(ord_dtls.order_sch_date, 'dd/MM/yyyy') as \"orderSchDate\",   " + 
							"	ord_dtls.order_sch_date as \"orderSchDateNew\",   " + 
							"	service_mst.service_standard_name as \"serviceStandardName\",   " + 
							"	ord_dtls.created_date as \"createdDateNew\",   " + 
							"	ord_dtls.service_rendered as \"serviceRendered\",   " + 
							"	ord_dtls.is_package_service as \"isPackageService\",   " + 
							"	ord_dtls.service_is_billed as \"serviceIsBilled\",   " + 
							"	ord_dtls.status as \"orderStatus\",   " + 
							"	ord_dtls.package_id as \"packageId\"   " + 
							"from " + 
							"	public.t_order_master ord_mst " + 
							"inner join public.t_order_details ord_dtls on " + 
							"	ord_mst.order_id = ord_dtls.order_id " + 
							"inner join service.t_unit_service_mapper service_mppr on " + 
							"	service_mppr.service_id = ord_dtls.service_id " + 
							"	and service_mppr.unit_id = ord_dtls.org_unit_id " + 
							"inner join service.t_unit_service_master_details service_dtls on " + 
							"	service_dtls.service_master_id = service_mppr.service_id " + 
							"	and service_dtls.unit_id = ord_dtls.org_unit_id " + 
							"inner join service.m_service_master service_mst on " + 
							"	service_mst.service_master_id = ord_dtls.service_id " + 
							"left join doctor.m_speciality_master speciality_mst on " + 
							"	speciality_mst.speciality_id = service_mst.speciality_id " + 
							"where " + 
							" speciality_mst.speciality_id in( " + 
							"	select " + 
							"		speciality_id " + 
							"	from " + 
							"		t_unit_speciality_mapper " + 
							"	where " + 
							"		unit_id = :unitId " + 
							"		and is_rendering_speciality = 'Y' ) " + 
							"	and ord_mst.patient_id = :patientId " + 
							"	and service_mst.status = 'A' " + 
							"	and ord_dtls.old_ord_dtl_id is null " + 
							"	and service_dtls.is_package = 'I' " + 
							"	and ord_dtls.nursing_service_order_flag = 'N' " + 
							"	and service_dtls.is_panel = 'N'" +
						    " and ord_dtls.status = 'A'" + 
							" union all " + 
							"select " + 
							"	ord_mst.order_id as \"orderId\",   " +
							"	ord_mst.encounter_id as \"encounterId\",   " + 
							"	service_mst.speciality_id as \"specialityId\",   " + 
							"	service_mst.sub_speciality_id as \"subDeptId\",   " + 
							"	service_mst.sub_speciality_id as \"subSpecialityId\",   " + 
							"	ord_dtls.order_details_id as \"orderDetailsId\",   " + 
							"	ord_dtls.service_id as \"serviceMasterId\",   " + 
							"	ord_dtls.order_set_id as \"orderSetId\",   " + 
							"	ord_dtls.priority_id as \"priorityId\",   " + 
							"	ord_dtls.ord_remarks as \"ordRemarks\",   " + 
							"	to_char(ord_dtls.order_sch_date, 'dd/MM/yyyy') as \"orderSchDate\",   " + 
							"	ord_dtls.order_sch_date as \"orderSchDateNew\",   " + 
							"	service_mst.service_standard_name as \"serviceStandardName\",   " + 
							"	ord_dtls.created_date as \"createdDateNew\",   " + 
							"	ord_dtls.service_rendered as \"serviceRendered\",   " + 
							"	ord_dtls.is_package_service as \"isPackageService\",   " + 
							"	ord_dtls.service_is_billed as \"serviceIsBilled\",   " + 
							"	ord_dtls.status as \"orderStatus\",   " + 
							"	ord_dtls.package_id as \"packageId\"   " + 
							"from " + 
							"	public.t_order_master ord_mst " + 
							"inner join public.t_order_details ord_dtls on " + 
							"	ord_mst.order_id = ord_dtls.order_id " + 
							"inner join service.t_unit_service_mapper service_mppr on " + 
							"	service_mppr.service_id = ord_dtls.service_id " + 
							"	and service_mppr.unit_id = ord_dtls.org_unit_id " + 
							"inner join service.t_unit_service_master_details service_dtls on " + 
							"	service_dtls.service_master_id = service_mppr.service_id " + 
							"	and service_dtls.unit_id = ord_dtls.org_unit_id " + 
							"inner join service.m_service_master service_mst on " + 
							"	service_mst.service_master_id = ord_dtls.service_id " + 
							"left join doctor.m_speciality_master speciality_mst on " + 
							"	speciality_mst.speciality_id = service_mst.speciality_id " + 
							"where " + 
							" speciality_mst.speciality_id in( " + 
							"	select " + 
							"		speciality_id " + 
							"	from " + 
							"		t_unit_speciality_mapper " + 
							"	where " + 
							"		unit_id = :unitId " + 
							"		and is_rendering_speciality = 'Y' ) " + 
							"	and ord_dtls.nursing_service_order_flag = 'N' " + 
							"	and ord_mst.patient_id = :patientId " + 
							"	and service_mst.status = 'A' " + 
							"	and ord_dtls.old_ord_dtl_id is null " + 
							"	and service_dtls.is_package = 'I' " + 
							"	and service_dtls.is_panel = 'Y' " + 
							" and ord_dtls.status = 'A'" + 
							" order by " + 
							"	\"createdDateNew\" asc " 
					)
			.setParameter("patientId", patientId)
			.setParameter("unitId", unitId)
		.setResultTransformer(Transformers.aliasToBean(InvestigationOrderMasterNewDto.class)).list();
	
	List<InvestigationOrderMasterNewDto> listlabOrderFreeText=sessionFactory.getCurrentSession()
			.createSQLQuery("select " + 
					"	tftos.patient_id as \"patientId\", "+
				    " tftos.free_text_ordered_services_id as \"freeTextOrderedServicesId\", " + 
					"	tftos.admission_id as \"admissionId\", " + 
					"	tftos.encounter_id as \"encounterId\", " + 
					"	tftos.order_details_id as \"orderDetailsId\", " + 
					"	CURRENT_TIMESTAMP as \"orderSchDateNew\", " + 
					"	tftos.service_text as \"serviceStandardName\", " + 
					"	CURRENT_TIMESTAMP as \"createdDateNew\" " + 
					" from " + 
					"	public.t_free_text_ordered_services tftos " + 
					" left join public.t_order_details ord_dtls on " + 
					"	tftos.order_details_id = ord_dtls.order_details_id " + 
					"	where " + 
					"		tftos.patient_id =:patientId " + 
					"		and tftos.status ='A' " + 
					"		and tftos.unit_id =:unitId "
					
					)
			.setParameter("patientId", patientId)
			.setParameter("unitId", unitId)
		.setResultTransformer(Transformers.aliasToBean(InvestigationOrderMasterNewDto.class)).list();
		listlabOrder.addAll(listlabOrderFreeText);
		return new Response(SUCCESS, SUCCESS_CODE, null, listlabOrder, null);
		} catch (Exception e) {
		logger.error("getInvestigationsListForNewEMR failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Integer getDoctorShareVac(Integer orderDetailsId) {
		try{
			
			BigInteger  count= (BigInteger)sessionFactory.getCurrentSession()
					.createSQLQuery(" select count(ord_dtl_id)  from  billing.t_doctor_share tds "
						 +"inner join billing.t_doc_share_voucher_dtls tdsvd on tds.doctor_share_id  = tdsvd .doctor_share_id "
						 +"where ord_dtl_id = :id and doc_id is not null limit 1 ")
					.setParameter("id", orderDetailsId)
					.uniqueResult();
			if(count.compareTo(BigInteger.ZERO)==0)
				return 0;
			else
				return 1;
		}catch(HibernateException e)
		{
		logger.error("getDoctorShareVac failed", e);
		}
		return 0;

	}

	@Override
	public Integer getReferShareVac(Integer orderDetailsId) {
		
		try{
			
			BigInteger  count= (BigInteger)sessionFactory.getCurrentSession()
					.createSQLQuery(" select count(ord_dtl_id)  from  billing.t_doctor_share tds "
						 +"inner join billing.t_doc_share_voucher_dtls tdsvd on tds.doctor_share_id  = tdsvd .doctor_share_id "
						 +"where ord_dtl_id = :id and doc_id is null limit 1 ")
					.setParameter("id", orderDetailsId)
					.uniqueResult();
			if(count.compareTo(BigInteger.ZERO)==0)
				return 0;
			else
				return 1;
		}catch(HibernateException e)
		{
		logger.error("getReferShareVac failed", e);
		}
		return 0;

	}

	@Override
	public Integer getDoctorShare(Integer orderDetailsId) throws ApplicationException {
		try{
			
			BigInteger  count= (BigInteger)sessionFactory.getCurrentSession()
					.createSQLQuery(" select count(ord_dtl_id)  from  billing.t_doctor_share tds "
						 +" where ord_dtl_id = :id and doc_id is not null limit 1 ")
					.setParameter("id", orderDetailsId)
					.uniqueResult();
			if(count.compareTo(BigInteger.ZERO)==0)
				return 0;
			else
				return 1;
		}catch(HibernateException e)
		{
		logger.error("getDoctorShare failed", e);
		}
		return 0;
	}

	@Override
	public Integer getReferShare(Integer orderDetailsId) throws ApplicationException {
		try{
			
			BigInteger  count= (BigInteger)sessionFactory.getCurrentSession()
					.createSQLQuery(" select count(ord_dtl_id)  from  billing.t_doctor_share tds "
						 +" where ord_dtl_id = :id and doc_id is null limit 1 ")
					.setParameter("id", orderDetailsId)
					.uniqueResult();
			if(count.compareTo(BigInteger.ZERO)==0)
				return 0;
			else
				return 1;
		}catch(HibernateException e)
		{
		logger.error("getReferShare failed", e);
		}
		return 0;

	}

	@Override
	public Response updateDoctorShare(Integer orderDetailsId, OrderDetailsMaster toBeUpdated)
			throws ApplicationException {

		try
		{
			Session session = sessionFactory.getCurrentSession();
			session.createSQLQuery("UPDATE billing.t_doctor_share SET "
							+ "doc_share = :docShare, "
							+ "total_doc_share = :totalDocShare, "
							+ "due_share_amt = :dueShareAmt, "
							+ "hosp_share = :hospShare "
							+ "WHERE ord_dtl_id = :orderDetailsId AND doc_id IS NOT NULL")
					.setParameter("docShare", toBeUpdated.getDocSharePer())
					.setParameter("totalDocShare", toBeUpdated.getDocSharePer())
					.setParameter("dueShareAmt", toBeUpdated.getDocSharePer())
					.setParameter("hospShare", toBeUpdated.getHospitalShare())
					.setParameter("orderDetailsId", orderDetailsId)
					.executeUpdate();

			return new Response(SUCCESS, SUCCESS_CODE, null, null, null);
		}catch(HibernateException he) {
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("updateDoctorShare failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);

	}

	@Override
	public Response updateReferShare(Integer orderDetailsId, OrderDetailsMaster toBeUpdated)
			throws ApplicationException {

		try
		{
			Session session = sessionFactory.getCurrentSession();
			session.createSQLQuery("UPDATE billing.t_doctor_share SET "
							+ "doc_share = :docShare, "
							+ "total_doc_share = :totalDocShare, "
							+ "due_share_amt = :dueShareAmt, "
							+ "hosp_share = :hospShare "
							+ "WHERE ord_dtl_id = :orderDetailsId AND doc_id IS NULL")
					.setParameter("docShare", toBeUpdated.getRefDocSharePer())
					.setParameter("totalDocShare", toBeUpdated.getRefDocSharePer())
					.setParameter("dueShareAmt", toBeUpdated.getRefDocSharePer())
					.setParameter("hospShare", toBeUpdated.getHospitalShare())
					.setParameter("orderDetailsId", orderDetailsId)
					.executeUpdate();

			return new Response(SUCCESS, SUCCESS_CODE, null, null, null);
		}catch(HibernateException he) {
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("updateReferShare failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);

	}

	@Override
	public void clearDoctor(OrderDetailsMasterDto orderDetailsMasterDto) {
		Session session = sessionFactory.getCurrentSession();
		session.createSQLQuery("UPDATE public.t_order_details "
						+ "SET ord_doc_id = NULL, ord_doc_spl_id = NULL "
						+ "WHERE order_details_id = :orderDetailsId")
				.setParameter("orderDetailsId", orderDetailsMasterDto.getOrderDetailsId())
				.executeUpdate();
	}

	@Override
	public Response updateOrderDetailsForDoctorShare(OrderDetailsMaster toBeUpdated) throws ApplicationException {
		try
		{

			Session session = sessionFactory.getCurrentSession();
			session.createSQLQuery("UPDATE public.t_order_details "
							+ "SET discount_ownership = :discountOwnership, "
							+ "doc_share_per = :docSharePer, "
							+ "ref_doc_share_per = :refDocSharePer, "
							+ "hospital_share = :hospitalShare "
							+ "WHERE order_details_id = :orderDetailsId")
					.setParameter("discountOwnership", toBeUpdated.getDiscountOwnership())
					.setParameter("docSharePer", toBeUpdated.getDocSharePer())
					.setParameter("refDocSharePer", toBeUpdated.getRefDocSharePer())
					.setParameter("hospitalShare", toBeUpdated.getHospitalShare())
					.setParameter("orderDetailsId", toBeUpdated.getOrderDetailsId())
					.executeUpdate();


			return new Response(SUCCESS, SUCCESS_CODE, null, null, null);
		}catch(HibernateException he) {
			he.printStackTrace();
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("updateOrderDetailsForDoctorShare failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response updateDummyHiddenServiceFlag(String flag, int orderDetailsId, boolean value, boolean isPackage, int packageId, int admissionId) {
		try
		{
			String query = null;
			List<Integer> orderDetailsIds= new LinkedList<>();
			if(isPackage) // if its a package then update the services which are consumed from this package
			{
				query = " select"
						 + " 	order_details_id"
						 + " from"
						 + " 	t_order_details_package_mapper todpm"
						 + " where"
						 + " 	todpm.package_consumption_master_id in ("
						 + " 	select"
						 + " 		tpcm.package_consumption_master_id"
						 + " 	from"
						 + " 		t_order_details od"
						 + " 	inner join billing.t_package_consumption_master tpcm on"
						 + " 		tpcm.order_detail_id = od.order_details_id"
						 + " 	where"
						 + " 		od.status='A' "
						 + "		and od.service_is_billed=0 "
						 + "		and tpcm.order_detail_id =:orderDetailsId ";
				orderDetailsIds = sessionFactory.getCurrentSession()
						.createSQLQuery(query)
						.setParameter("orderDetailsId",orderDetailsId)
						.list();
				
				if(orderDetailsIds.size()==0) //if package consumption does not have entries then this is a EHC package 
				{
					query = " select " 
							 + " 	tod.order_details_id " 
							 + " from " 
							 + " 	t_order_details tod " 
							 + " inner join t_order_master tom on " 
							 + " 	tod.order_id = tom.order_id " 
							 + " where " 
							 + " 	tod.service_is_billed = 0 " 
							 + " 	and tod.is_package_service = 'Y' " 
							 + " 	and tom.admission_id =:admissionId "
							 + " 	and tod.package_id =:packageId "
							 + " 	and tod.status = 'A' ";
					orderDetailsIds = sessionFactory.getCurrentSession()
							.createSQLQuery(query)
							.setParameter("admissionId",admissionId)
							.setParameter("packageId",packageId)
							.list();
				}
				
			}
			orderDetailsIds.add(orderDetailsId);

			sessionFactory.getCurrentSession()
					.createSQLQuery("UPDATE public.t_order_details "
							+ "SET " + flag + " = :value "
							+ "WHERE order_details_id IN (:orderDetailsIds)")
					.setParameter("value", value)
					.setParameterList("orderDetailsIds", orderDetailsIds)
					.executeUpdate();


			return new Response(SUCCESS, SUCCESS_CODE, null, orderDetailsIds, null);

		}catch(HibernateException he) {
			he.printStackTrace();
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("updateDummyHiddenServiceFlag failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public boolean getDummyServiceFlagForPackageService(List<Integer> packageConsumptionMasterId) {
		try
		{
			String query = " select " 
					 + " 	od.order_details_id " 
					 + " from " 
					 + " 	t_order_details od " 
					 + " inner join billing.t_package_consumption_master tpcm on " 
					 + " 	tpcm.order_detail_id = od.order_details_id " 
					 + " where " 
					 + " 	tpcm.package_consumption_master_id in (:ids) " 
					 + " 	and od.is_dummy_service = true ";
			List<Integer> list= sessionFactory.getCurrentSession()
					.createSQLQuery(query)
					.setParameterList("ids", packageConsumptionMasterId)
					.list();
			
			return list!=null && list.size()>0 ? true :false;
		}catch(HibernateException he) {
			he.printStackTrace();
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("getDummyServiceFlagForPackageService failed", e);
		}
		return false;
	}

	@Override
	public List<OrderDetailsPackageMapperDto> getOrderDetailsPackageMapperByOrderDetailsIdOptimise(
			List<Integer> orderDetailsIds) throws ApplicationException {
		try {
			String query ="select "
					+ "	mppr.id as \"id\", "
					+ "	mppr.order_details_id as \"orderDetailsId\", "
					+ "	mppr.service_id as \"serviceId\", "
					+ "	mppr.package_id as \"packageId\", "
					+ "	mppr.payee_id as \"payeeId\", "
					+ "	pack_service.service_standard_code as \"packageCode\", "
					+ "	pack_service.service_standard_name as \"packageName\", "
					+ "	service.service_standard_code as \"serviceStandardCode\", "
					+ "	service.service_standard_name as \"serviceStandardName\", "
					+ "	mppr.service_amount as \"serviceAmount\", "
					+ "	mppr.package_amount \"packageAmount\", "
					+ "	mppr.is_package_service as \"isPackageService\", "
					+ "	mppr.is_cap_service as \"isCapService\", "
					+ "	mppr.status as \"status\", "
					+ "	item.\"name\" as \"itemName\", "
					+ "	item.code as \"itemCode\", "
					+ "	mppr.pacakge_consumption_service_detail_id as \"pacakgeConsumptionServiceDetailId\", "
					+ "	mppr.package_consumption_master_id as \"packageConsumptionMasterId\", "
					+ "	mppr.package_consumption_cap_details_id as \"packageConsumptionCapDetailsId\", "
					+ "	mppr.package_qty as \"packageQty\", "
					+ "	mppr.package_consumtion_eo_detail_id as \"packageConsumptionEODetailId\", package_master.package_type_id \"packageTypeId\" "
					+ "from "
					+ "	public.t_order_details_package_mapper mppr "
					+ "left join service.m_package_master package_master "
					+ "	on mppr.package_id = package_master.package_master_id "
					+ "left join service.m_service_master pack_service on "
					+ "	package_master.service_id = pack_service.service_master_id "
					+ "left join service.m_service_master service on "
					+ "	mppr.service_id = service.service_master_id "
					+ "left join public.m_item item on "
					+ "	mppr.item_id = item.id "
					+ "where "
					+ "	mppr.status = 'A' "
					+ "	and mppr.order_details_id in (:orderDetailsId) "
					+ "order by "
					+ "	mppr.id,mppr.package_id desc";

			List<OrderDetailsPackageMapperDto> list = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameterList("orderDetailsId", orderDetailsIds)
					.setResultTransformer(Transformers.aliasToBean(OrderDetailsPackageMapperDto.class)).list();

			return list;
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	public Response updateDoctorShareWithDoctorAndSpeciality(Integer orderDetailsId, OrderDetailsMaster toBeUpdated)
			throws ApplicationException {

		try
		{
			Session session = sessionFactory.getCurrentSession();
			session.createSQLQuery("UPDATE billing.t_doctor_share "
							+ "SET doc_share = :docShare, "
							+ "    doc_id = :docId, "
							+ "    doc_spec_id = :docSpecId, "
							+ "    total_doc_share = :totalDocShare, "
							+ "    due_share_amt = :dueShareAmt, "
							+ "    hosp_share = :hospShare "
							+ "WHERE ord_dtl_id = :orderDetailsId "
							+ "  AND doc_id IS NOT NULL")
					.setParameter("docShare", toBeUpdated.getDocSharePer())
					.setParameter("docId", toBeUpdated.getOrdDocId())
					.setParameter("docSpecId", toBeUpdated.getOrdDocSplId())
					.setParameter("totalDocShare", toBeUpdated.getDocSharePer())
					.setParameter("dueShareAmt", toBeUpdated.getDocSharePer())
					.setParameter("hospShare", toBeUpdated.getHospitalShare())
					.setParameter("orderDetailsId", orderDetailsId)
					.executeUpdate();

			return new Response(SUCCESS, SUCCESS_CODE, null, null, null);
		}catch(HibernateException he) {
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("updateDoctorShareWithDoctorAndSpeciality failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);


	}

	@Override
	public Response updateOrderDetailsStatus(List<Integer> packIds, Integer billingMasterId) {
		try
		{
			Session session = sessionFactory.getCurrentSession();
			String query = "UPDATE t_order_details "
					+ "SET status = 'I', ord_cancelled = 'Y' "
					+ "WHERE order_details_id IN ("
					+ "    SELECT orpk.order_details_id "
					+ "    FROM t_order_details_package_mapper orpk "
					+ "    WHERE orpk.order_details_id IN ("
					+ "        SELECT tbd.order_details_id "
					+ "        FROM billing.t_billing_details tbd "
					+ "        WHERE tbd.billing_master_id = :billingMasterId"
					+ "    ) "
					+ "    AND orpk.package_id IN (:packIds)"
					+ ")";
			session.createSQLQuery(query)
					.setParameter("billingMasterId", billingMasterId)
					.setParameterList("packIds", packIds)  // Assuming packIds is a List
					.executeUpdate();
			return new Response(SUCCESS, SUCCESS_CODE, null, null, null);
		}catch(HibernateException he) {
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("updateOrderDetailsStatus failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);



	}


    @Override
    public List<AdmissionDto> getPatientsDetailsListByMobileNumber(AdmissionDto admissionDto) {
        List<AdmissionDto> list = new ArrayList<>();
        try
        {

            String query = "Select patient.patient_id as \"patientId\", " +
                    " concat(coalesce(prefix.prefix_code, ''), ' ', coalesce(patient.first_name, '') , ' ', coalesce(patient.middle_name, '') , ' ', coalesce(patient.last_name, '')) as \"patientName\", " +
                    " patient.uhid_number as \"UHIDNumber\" " +
//                    " encounter.encounter_id as \"encounterId\" " +
                    " from patient.t_patient_registration patient " +
                    " left join public.m_prefix_master prefix on " +
                    "   prefix.prefix_id = patient.prefix_id " +
//                    " inner join opd.t_encounter_master encounter on " +
//                    "   encounter.patient_id = patient.patient_id " +
                    " where " +
                    " patient.mobile_number = :mobileNumber " +
                    " and patient.status = 'A' " +
//                    " and patient.unit_id  = :unitId " + //Commented unit id condition for cross unit orders
//                    " and encounter.unit_id  = :unitId " +
                    " and patient.organization_id  = :orgId " +
//                    " and encounter.organization_id  = :orgId " +
                    " order by patient.patient_id DESC";

            list = sessionFactory.getCurrentSession()
                    .createSQLQuery(query)
                    .setParameter("mobileNumber", admissionDto.getMobileNumber())
//                    .setParameter("unitId", admissionDto.getUnitId())
                    .setParameter("orgId", admissionDto.getOrganizationId())
                    .setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();

            return list;
        }catch(HibernateException he) {
        logger.error("getPatientsDetailsListByMobileNumber failed", he);
        }catch(Exception e) {
        logger.error("getPatientsDetailsListByMobileNumber failed", e);
        }
        return list;
    }
}


