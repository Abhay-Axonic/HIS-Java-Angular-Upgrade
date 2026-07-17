package com.param.procurement.workOrder.dao.workOrderCreation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import in.param.exception.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.constants.Screen;
import com.param.entity.model.procurement.NewWorkOrderApprovalDetails;
import com.param.entity.model.procurement.NewWorkOrderApprovalSettlement;
import com.param.entity.model.procurement.NewWorkOrderApprovalSettlementDetails;
import com.param.entity.model.procurement.NewWorkOrderCompletion;
import com.param.entity.model.procurement.NewWorkOrderCompletionStagedDetailsMapper;
import com.param.entity.model.procurement.NewWorkOrderCreation;
import com.param.entity.model.procurement.NewWorkOrderCreationDetails;
import com.param.entity.model.procurement.NewWorkOrderCreationDocumentDetails;
import com.param.entity.model.procurement.NewWorkOrderCreationOtherChargeDetails;
import com.param.entity.model.procurement.NewWorkOrderCreationStagedDetailsMapper;
import com.param.entity.model.procurement.NewWorkOrderItemRejectionDetails;
import com.param.entity.model.procurement.NewWorkOrderRejectionDetails;
import com.param.entity.model.procurement.NewWorkOrderRequestApproval;
import com.param.entity.model.procurement.NewWorkOrderSettlementPaymentDetails;
import com.param.entity.model.procurement.newWorkOrderCompletionDetails;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.IBillingCommonConstants;
import com.param.global.common.ICommonConstants;
import com.param.global.common.IError;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dao.IGlobalUnitMasterDao;
import com.param.global.pharmacy.dto.WOSettlementDetailsDto;
import com.param.global.pharmacy.dto.WorkOrderApprovalSettlementDto;
import com.param.global.pharmacy.dto.WorkOrderCreationApprovalDto;
import com.param.global.pharmacy.dto.WorkOrderCreationOtherChargeDetailsDto;
import com.param.global.pharmacy.dto.WorkOrderCreationRequestApprovalDetailsDto;
import com.param.global.pharmacy.dto.WorkOrderCreationRequestDetailsDto;
import com.param.global.pharmacy.dto.WorkOrderCreationRequestDocumentDto;
import com.param.global.pharmacy.dto.WorkOrderCreationRequestDto;
import com.param.utility.TimeZoneComponent;

@Repository
@SuppressWarnings({"unused","rawtypes","unchecked"})
public class NewWorkOrderCreationDaoImpl implements INewWorkOrderCreationDao, ICommonConstants, IError {

	final static Logger logger = Logger.getLogger(NewWorkOrderCreationDaoImpl.class);

	private Integer screenId = Screen.WorkorderQuotationCreation.getScreen();
	@Autowired
	private SessionFactory sessionFactory;
	

	@Autowired
	IGlobalUnitMasterDao iGlobalUnitMasterDao;
	
	@Override
	public Response saveCreationRequest(WorkOrderCreationRequestDto requestDto) throws ApplicationException {
		try {
			
			   if(requestDto.getWorkorderRequestId()!=null) {
				       Session	opensession=sessionFactory.getCurrentSession();
				       opensession.createQuery(" UPDATE NewWorkOrderCreationDetails SET status='I' "
				        		+ " WHERE workorderRequestId ="+requestDto.getWorkorderRequestId())
								.executeUpdate();
				       
				       opensession.createQuery(" UPDATE NewWorkOrderCreationOtherChargeDetails SET status='I' "
				        		+ " WHERE workorderRequestId ="+requestDto.getWorkorderRequestId())
								.executeUpdate();
				       
			   }
			
			    NewWorkOrderCreation newWorkorderObj=new NewWorkOrderCreation();
			    if(requestDto.getWorkorderRequestId()!=null) {
			    	newWorkorderObj.setWorkorderRequestId(requestDto.getWorkorderRequestId());
				   }
			    if(requestDto.getWorkorderRequestId()!=null) {
			       	newWorkorderObj.setWorkorderRequestNumber(requestDto.getWorkorderRequestNumber());
			    }else {
			    	newWorkorderObj.setWorkorderRequestNumber(iGlobalUnitMasterDao.getGeneratedNo(0, requestDto.getUnitId(), screenId));
			    }
			    
			    newWorkorderObj.setStoreId(requestDto.getStoreId());
			    newWorkorderObj.setVendorId(requestDto.getVendorId());
			    newWorkorderObj.setDeliveryDate(GlobalCommonDateUtils.getDate(requestDto.getDeliveryDate(), "dd-M-yyyy HH:mm:ss") );
			    newWorkorderObj.setBillTypeId(requestDto.getBillTypeId());
			    newWorkorderObj.setContractTypeId(requestDto.getContractTypeId());
			    newWorkorderObj.setAgainstTypeId(requestDto.getAgainstTypeId());
			    newWorkorderObj.setWorkorderRequestDate(TimeZoneComponent.getDateByzone(requestDto.getUnitId()));
			    newWorkorderObj.setTotalAmount(requestDto.getTotalAmount());
			    newWorkorderObj.setTotalDiscountAmount(requestDto.getTotalDiscountAmount());
			    newWorkorderObj.setTotalTaxAmount(requestDto.getTotalTaxAmount());
			    newWorkorderObj.setOtherChargeAmount(requestDto.getOtherChargeAmount());
			    newWorkorderObj.setTotalNetAmount(requestDto.getTotalNetAmount());
			    newWorkorderObj.setWorkorderRequestStatusId(1);
			    newWorkorderObj.setWorkorderApprovalStatusId(10);
			    newWorkorderObj.setPaymentTerms(requestDto.getPaymentTerms());
			    newWorkorderObj.setGuaranteeWarranty(requestDto.getGuaranteeWarranty());
			    newWorkorderObj.setOrgId(requestDto.getOrgId());
			    newWorkorderObj.setUnitId(requestDto.getUnitId());
			    newWorkorderObj.setCreatedBy(requestDto.getCreatedBy());
			    newWorkorderObj.setCreatedDate(TimeZoneComponent.getDateByzone(requestDto.getUnitId()));
			    newWorkorderObj.setUpdatedBy(requestDto.getCreatedBy());
			    newWorkorderObj.setUpdatedDate(TimeZoneComponent.getDateByzone(requestDto.getUnitId()));
			    newWorkorderObj.setStatus("A");
			    newWorkorderObj.setEncounterId(requestDto.getEncounterId());
			    newWorkorderObj.setAdmissionId(requestDto.getAdmissionId());
			    newWorkorderObj.setBillId(requestDto.getBillId());
			    newWorkorderObj.setIsReverseTax(requestDto.getIsReverseTax());
			    sessionFactory.getCurrentSession().saveOrUpdate(newWorkorderObj);
			    
			    if(newWorkorderObj.getWorkorderRequestId() != null) {
	    			String hql = "delete FROM procurement.t_new_work_order_creation_staged_details  where workorder_request_id=:DetailId ";

	    			Query query = sessionFactory.getCurrentSession().createSQLQuery(hql);
	    			query.setParameter("DetailId", newWorkorderObj.getWorkorderRequestId().intValue());
	    			 query.executeUpdate();
	    		}
			    if(requestDto.getRequestDetailsDto().size()>0) {
			    	for(WorkOrderCreationRequestDetailsDto detailsdto :requestDto.getRequestDetailsDto())
			    	{
			    		NewWorkOrderCreationDetails details=new NewWorkOrderCreationDetails();
			    		details.setWorkorderRequestId(newWorkorderObj.getWorkorderRequestId());
			    		if(detailsdto.getItemId()!=null) 
			    			     details.setItemId(detailsdto.getItemId());
			    		
			    		details.setStoreId(requestDto.getStoreId());
			    		details.setItemName(detailsdto.getItemName());
			    		details.setItemQuantity(detailsdto.getItemQuantity());
			    		details.setItemRate(detailsdto.getItemRate());
			    		details.setItemMrp(detailsdto.getItemMrp());
			    		if(detailsdto.getItemDiscPercentage()!=null) 
			    			details.setItemDiscPercentage(detailsdto.getItemDiscPercentage());
			    		if(detailsdto.getItemDiscAmount()!=null)  
			    			details.setItemDiscAmount(detailsdto.getItemDiscAmount());
			    		if(detailsdto.getItemTaxId()!=null)  	
			    			details.setItemTaxId(detailsdto.getItemTaxId());
			    		if(detailsdto.getItemTax()!=null)  
			    			details.setItemTax(detailsdto.getItemTax());
			    		if(detailsdto.getItemTaxAmount()!=null)
			    			details.setItemTaxAmount(detailsdto.getItemTaxAmount());
			    		
			    		details.setItemNetAmount(detailsdto.getItemNetAmount());
			    		details.setOrgId(requestDto.getOrgId());
			    		details.setUnitId(requestDto.getUnitId());
			    		details.setCreatedBy(requestDto.getCreatedBy());
			    		details.setUpdatedDate(TimeZoneComponent.getDateByzone(requestDto.getUnitId()));
			    		details.setUpdatedBy(requestDto.getCreatedBy());
			    		details.setStatus("A");
			    		details.setItemRemark(detailsdto.getItemRemark());
			    		details.setCreatedDate(TimeZoneComponent.getDateByzone(requestDto.getUnitId()));
			    		details.setOrderDetailsId(detailsdto.getOrderDetailsId());
			    		
			    		sessionFactory.getCurrentSession().save(details);
			    		
			    		if(detailsdto.getWorkOrderCreationStagedListData()!=null && detailsdto.getWorkOrderCreationStagedListData().size()>0)
			    		{
						for (NewWorkOrderCreationStagedDetailsMapper stagedMapper : detailsdto.getWorkOrderCreationStagedListData()) {
		                    NewWorkOrderCreationStagedDetailsMapper stagedDetails = new NewWorkOrderCreationStagedDetailsMapper();
		                    stagedDetails.setOrganizationId(requestDto.getOrgId());
		                    stagedDetails.setUnitId(requestDto.getUnitId());
		                    stagedDetails.setUpdatedDate(TimeZoneComponent.getDateByzone(requestDto.getUnitId()));
		                    stagedDetails.setUpdatedByUser(requestDto.getUpdatedBy());
		                    stagedDetails.setAddedByUser(requestDto.getCreatedBy());
		                    stagedDetails.setAddedDate(TimeZoneComponent.getDateByzone(requestDto.getUnitId()));
		                    stagedDetails.setPercentage(stagedMapper.getPercentage());
		                    stagedDetails.setStageName(stagedMapper.getStageName());
		                    stagedDetails.setWorkorderRequestDetailsId(details.getWorkorderRequestDetialsId());
		                    stagedDetails.setWorkorderRequestId(details.getWorkorderRequestId());
		                    sessionFactory.getCurrentSession().save(stagedDetails);
		                }
			    	    }
			    	}
			    	
			    }
			    if(requestDto.getRequestTaxDetailsDto()!=null && requestDto.getRequestTaxDetailsDto().size()>0) {
			    	for(WorkOrderCreationOtherChargeDetailsDto otherChargeDto :requestDto.getRequestTaxDetailsDto()) {
			    		NewWorkOrderCreationOtherChargeDetails othercharge=new  NewWorkOrderCreationOtherChargeDetails();
			    		othercharge.setWorkorderRequestId(newWorkorderObj.getWorkorderRequestId());
			    		othercharge.setOtherchargeTypeId(otherChargeDto.getOtherchargeTypeId());
			    		othercharge.setOtherChargeAmount(otherChargeDto.getOtherChargeAmount());
			    		othercharge.setOrgId(requestDto.getOrgId());
			    		othercharge.setUnitId(requestDto.getUnitId());
			    		othercharge.setCreatedBy(requestDto.getCreatedBy());
			    		othercharge.setCreatedDate(TimeZoneComponent.getDateByzone(requestDto.getUnitId()));
			    		othercharge.setUpdatedDate(TimeZoneComponent.getDateByzone(requestDto.getUnitId()));
			    		othercharge.setUpdatedBy(requestDto.getCreatedBy());
			    		othercharge.setStatus("A");
			    		sessionFactory.getCurrentSession().save(othercharge);
			    		
			    	}
			    	
			    }
			    
			    
			    return new Response<>(SUCCESS, SUCCESS_CODE, null, null, newWorkorderObj);
			 } catch (HibernateException he) {
				logger.error("saveCreationRequest failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("saveCreationRequest failed", e);
				throw new ServiceException(e.getMessage());
			} finally {

			}
	}


	@Override
	public Response workorderRequestDocument(WorkOrderCreationRequestDocumentDto requestDocumentDto)
			throws ApplicationException {
		try {
			NewWorkOrderCreationDocumentDetails documentObject=new NewWorkOrderCreationDocumentDetails();
			documentObject.setWorkorderRequestId(requestDocumentDto.getWorkorderRequestId());
			documentObject.setDocumentTypeId(requestDocumentDto.getDocumentTypeId());
			documentObject.setDocumentPath(requestDocumentDto.getDocumentPath());
			documentObject.setRequestDocumentDate(new  Date());
			documentObject.setCreatedBy(requestDocumentDto.getCreatedBy());
			documentObject.setUpdatedBy(requestDocumentDto.getCreatedBy());
			documentObject.setCreatedDate(new  Date());
			documentObject.setUpdatedDate(new  Date());
			documentObject.setStatus("A");
			sessionFactory.getCurrentSession().save(documentObject);
		return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
	  } catch (HibernateException he) {
		logger.error("workorderRequestDocument failed", he);
		throw new ServiceException(he.getMessage());
	  } catch (Exception e) {
		logger.error("workorderRequestDocument failed", e);
		throw new ServiceException(e.getMessage());
	  } finally {

	   }
	}


	@Override
	public Response getCreationRequestList(WorkOrderCreationRequestDto requestDto) throws ApplicationException {
		try {
			String query= "select " 
					 + " 	request.workorder_request_id as \"workorderRequestId\", " 
					 + " 	request.workorder_request_number as \"workorderRequestNumber\", " 
					 + " 	request.bill_type_id as \"billTypeId\", " 
					 + " 	request.store_id as \"storeId\", " 
					 + " 	request.vendor_id as \"vendorId\", " 
					 + " 	to_char(request.delivery_date, 'dd/mm/yyyy') as \"deliveryDate\", " 
					 + " 	request.contract_type_id as \"contractTypeId\", " 
					 + " 	to_char(request.workorder_request_date , 'dd/mm/yyyy') as \"workorderRequestDate\", " 
					 + " 	request.total_amount as \"totalAmount\", " 
					 + " 	mum.user_name as \"approvedName\", " 
					 + " 	request.total_discount_amount as \"totalDiscountAmount\", " 
					 + " 	request.total_tax_amount as \"totalTaxAmount\", " 
					 + " 	request.other_charge_amount as \"otherChargeAmount\", " 
					 + " 	request.total_net_amount as \"totalNetAmount\", " 
					 + " 	request.workorder_request_status_id as \"workorderRequestStatusId\", " 
					 + " 	request.payment_terms as \"paymentTerms\", " 
					 + " 	request.guarantee_warranty as \"guaranteeWarranty\", " 
					 + " 	request.org_id as \"orgId\", " 
					 + " 	request.unit_id as \"unitId\", " 
					 + " 	request.created_by as \"createdBy\", " 
					 + " 	empmst.user_name as \"userName\", " 
					 + " 	vendortab.name as \"vendorName\", " 
					 + " 	storetab.store_name as \"storeName\", " 
					 + " 	agaisttypeMst.type as \"againstTypeName\" , " 
					 + " 	request.workorder_request_status_id as \"statusId\", " 
					 + " 	s.status as \"status\" , " 
					 + " 	sapproval.id as \"approvalStatusId\", " 
					 + " 	sapproval.status as \"approvalStatus\", " 
					 + " 	request.work_order_completion as \"workOrderCompletion\", " 
					 + " 	mct.description as \"contractType\", "
					 + " 	concat((case when tpr.is_vip = 'Y' then coalesce(tpr.alise_name, tpr.first_name) else concat(coalesce(tpr.first_name) , ' ', coalesce(tpr.middle_name) , ' ', coalesce(tpr.last_name)) end ), concat(to_char(extract(year from age(current_timestamp, tpr.dob)), '999'), 'Y', '/', mgm.gender_code)) as \"patientDetails\", " 
					 + " 	tpr.uhid_number as \"uhidNumber\", " 
					 + " 	concat(mbm.bill_number, ' | ', to_char(mbm.bill_date_time, 'DD/MM/YYYY')) as \"billDetails\", " 
					 + " 	tem.encounter_number as \"visitDetails\" " 
					 + " from " 
					 + " 	procurement.t_new_work_order_creation request " 
					 + " left join public.m_vendor vendortab on " 
					 + " 	request.vendor_id = vendortab.id " 
					 + " left join public.m_store storetab on " 
					 + " 	request.store_id = storetab.store_id " 
					 + " left join public.m_request_type agaisttypeMst on " 
					 + " 	request.against_type_id = agaisttypeMst.id " 
					 + " left join PUBLIC.m_user_master empmst on " 
					 + " 	request.created_by = empmst.user_id " 
					 + " left join doctor.m_doctor_master docmst on " 
					 + " 	request.created_by = docmst.user_id " 
					 + " left join PUBLIC.m_employee_master empmst1 on " 
					 + " 	request.approved_by = empmst1.user_id " 
					 + " left join public.m_user_master mum on " 
					 + " 	mum.user_id = request.approved_by " 
					 + " left join doctor.m_doctor_master docmst1 on " 
					 + " 	request.approved_by = docmst1.user_id " 
					 + " left join public.m_status s on " 
					 + " 	request.workorder_request_status_id = s.id " 
					 + " left join public.m_status sapproval on " 
					 + " 	request.workorder_approval_status_id = sapproval.id "
					 + " left join procurement.m_contract_type mct on " 
					 + " 	mct.id = request.contract_type_id " 
					 + " left join billing.m_billing_master mbm on " 
					 + " 	mbm.billing_master_id = request.bill_id " 
					 + " left join opd.t_encounter_master tem on " 
					 + " 	tem.encounter_id = request.encounter_id " 
					 + " left join adt.t_admission ta on " 
					 + " 	ta.admission_id = request.admission_id " 
					 + " left join patient.t_patient_registration tpr on " 
					 + " 	tpr.patient_id = " 
					 + " 	(case " 
					 + " 		when request.encounter_id is not null then tem.patient_id " 
					 + " 		else ta.patient_id " 
					 + " 	end) " 
					 + " left join m_gender_master mgm on " 
					 + " 	mgm.gender_id = tpr.gender_id " 
					 + " where (:storeId = 0 OR request.store_id = :storeId) "
					 + "    and request.org_id =:orgId "
					 + "    and request.unit_id =:unitId "
					 + "    and request.status = 'A'";
					
			StringBuilder buildquery = new StringBuilder(query);
			if(requestDto.getFromDate()!=null && requestDto.getFromDate()!="" && requestDto.getToDate()!=null && requestDto.getToDate()!="" ) {
//				buildquery.append(" and to_char(request.delivery_date, 'dd/mm/yyyy') between ")
				buildquery.append(" and to_char(request.workorder_request_date,'YYYY-MM-DD') between :fromDate and :toDate ");
			}
			if(requestDto.getBillTypeId()!=null  ) {
				buildquery.append(" and request.bill_type_id=:billTypeId ");
			}
			if(requestDto.getVendorId()!=null  ) {
				buildquery.append(" and request.vendor_id=:vendorId ");
			}

			if (requestDto.getWorkorderRequestNumber() != null && !requestDto.getWorkorderRequestNumber().isEmpty()) {
				buildquery.append(" and request.workorder_request_number like :workorderRequestNumber ");
			}

			if(requestDto.getWorkorderRequestStatusId()!=null   ) {
				buildquery.append(" and request.workorder_request_status_id=:workorderRequestStatusId ");
			}
			buildquery.append(" order by request.workorder_request_id desc ");
			Query createQuery=sessionFactory.getCurrentSession().createSQLQuery(buildquery.toString()).setParameter("storeId",requestDto.getStoreId()!=null && requestDto.getStoreId() > 0 ? requestDto.getStoreId() : 0)
					.setParameter("orgId", requestDto.getOrgId())
					.setParameter("unitId", requestDto.getUnitId());

			if(requestDto.getFromDate()!=null && requestDto.getFromDate()!="" && requestDto.getToDate()!=null && requestDto.getToDate()!="" ) {
				createQuery.setParameter("fromDate", requestDto.getFromDate());
				createQuery.setParameter("toDate", requestDto.getToDate());
			}
			if(requestDto.getBillTypeId()!=null  ) {
				createQuery.setParameter("billTypeId", requestDto.getBillTypeId());
			}

			if(requestDto.getVendorId()!=null  ) {
				createQuery.setParameter("vendorId", requestDto.getVendorId());
			}

			if (requestDto.getWorkorderRequestNumber() != null && !requestDto.getWorkorderRequestNumber().isEmpty()) {
				createQuery.setParameter("workorderRequestNumber", "%" + requestDto.getWorkorderRequestNumber() + "%");
			}

			if(requestDto.getWorkorderRequestStatusId()!=null   ) {
				createQuery.setParameter("workorderRequestStatusId", requestDto.getWorkorderRequestStatusId());
			}

			List<WorkOrderCreationRequestDto> listrequestDto = createQuery.setResultTransformer(Transformers.aliasToBean(WorkOrderCreationRequestDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listrequestDto, null);

		} catch (HibernateException he) {
			logger.error("getCreationRequestList failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getCreationRequestList failed", e);
			throw new ServiceException(e.getMessage());
		} finally {

		}
	}


	@Override
	public Response getCreationRequestDetailsbyId(WorkOrderCreationRequestDto requestDto) throws ApplicationException {
	try {
			WorkOrderCreationRequestDto requestdetails = (WorkOrderCreationRequestDto) sessionFactory
					.getCurrentSession().getNamedQuery("GET_WO_REQUEST_BY_REQUEST_ID")
					.setParameter("workorderRequestId", requestDto.getWorkorderRequestId())
					.setResultTransformer(Transformers.aliasToBean(WorkOrderCreationRequestDto.class)).uniqueResult();

			List<WorkOrderCreationRequestDetailsDto> detaillist = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_WO_REQUEST_DETAILS_BY_REQUEST_ID")
					.setParameter("workorderRequestId", requestDto.getWorkorderRequestId())
					.setResultTransformer(Transformers.aliasToBean(WorkOrderCreationRequestDetailsDto.class)).list();
			

			List<WorkOrderCreationOtherChargeDetailsDto> otherchargedetaillist = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_WO_REQUEST_OTHERCHARGE_DETAILS_BY_REQUEST_ID")
					.setParameter("workorderRequestId", requestDto.getWorkorderRequestId())
					.setResultTransformer(Transformers.aliasToBean(WorkOrderCreationOtherChargeDetailsDto.class))
					.list();

			List<WorkOrderCreationRequestDocumentDto> documentdetaillist = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_WO_REQUEST_DOCUMENT_DETAILS_BY_REQUEST_ID")
					.setParameter("workorderRequestId", requestDto.getWorkorderRequestId())
					.setResultTransformer(Transformers.aliasToBean(WorkOrderCreationRequestDocumentDto.class)).list();
			
            for (WorkOrderCreationRequestDetailsDto workOrderCreationRequestDetailsDto : detaillist) {
            	String hql = "SELECT newWorkOrderCreationStagedDetailsMapper FROM NewWorkOrderCreationStagedDetailsMapper AS newWorkOrderCreationStagedDetailsMapper where workorderRequestDetailsId=:WODetailId ";

        		Query query = sessionFactory.getCurrentSession().createQuery(hql);
        		query.setParameter("WODetailId", workOrderCreationRequestDetailsDto.getWorkorderRequestDetialsId());
            	List<NewWorkOrderCreationStagedDetailsMapper> getList= query.list();
                List<NewWorkOrderCreationStagedDetailsMapper> getList3=new ArrayList<NewWorkOrderCreationStagedDetailsMapper>();
                for (NewWorkOrderCreationStagedDetailsMapper getList1 : getList) {
                	NewWorkOrderCreationStagedDetailsMapper getList2=new NewWorkOrderCreationStagedDetailsMapper();
                	getList2.setStagedWorkorderId(getList1.getStagedWorkorderId());
                	getList2.setStageName(getList1.getStageName());
                	getList2.setPercentage(getList1.getPercentage());
                	getList2.setWorkorderRequestId(requestDto.getWorkorderRequestId());
                    getList3.add(getList2);
                }
                workOrderCreationRequestDetailsDto.setWorkOrderCreationStagedListData(getList3);
			}
			if(detaillist.size()>0) 
				requestdetails.setRequestDetailsDto(detaillist);
			if(otherchargedetaillist.size()>0)
				requestdetails.setRequestTaxDetailsDto(otherchargedetaillist);
			if(documentdetaillist.size()>0)
				requestdetails.setRequestDocumentDto(documentdetaillist);
			
			Integer bucketType = (Integer) sessionFactory.getCurrentSession()
					.createSQLQuery("select bucket_flag from t_unit_application_billing_configuration where unit_id ="+ requestdetails.getUnitId()+" and organization_id ="+requestdetails.getOrgId())
					.uniqueResult();
			
			if(bucketType == null) {
				bucketType=1;
			}
			requestdetails.setBucketFlag(bucketType);
		   return new Response<>(SUCCESS, SUCCESS_CODE, null, null, requestdetails);
	} catch (HibernateException he) {
		logger.error("getCreationRequestDetailsbyId failed", he);
		throw new ServiceException(he.getMessage());
		  } catch (Exception e) {
		logger.error("getCreationRequestDetailsbyId failed", e);
		throw new ServiceException(e.getMessage());
		  } finally {

		   }
	}


	@Override
	public Response requestChangeToPending(WorkOrderCreationRequestDto requestDto) throws ApplicationException {
		try {
			if(requestDto.getWorkOrderRequestList().size()>0){
				for (WorkOrderCreationRequestDto dto:requestDto.getWorkOrderRequestList()) {
					sessionFactory.getCurrentSession().createQuery(" UPDATE NewWorkOrderCreation SET workorderRequestStatusId=2 "
			        		+ " WHERE workorderRequestId ="+dto.getWorkorderRequestId())
							.executeUpdate();
				}
				   
				  return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
			}
			return new Response<>(ERROR, ERROR_CODE, null, null, null);
	  } catch (HibernateException he) {
		logger.error("requestChangeToPending failed", he);
		throw new ServiceException(he.getMessage());
	  } catch (Exception e) {
		logger.error("requestChangeToPending failed", e);
		throw new ServiceException(e.getMessage());
	  } finally {

	   }
	}


	@Override
	public Response requestChangeToCancel(WorkOrderCreationRequestDto requestDto) throws ApplicationException {
	try {
	
				sessionFactory.getCurrentSession().createQuery(" UPDATE NewWorkOrderCreation SET workorderRequestStatusId=9 , workorderApprovalStatusId=9 , approvedBy="+requestDto.getApprovedBy()
		        		+ " WHERE workorderRequestId ="+requestDto.getWorkorderRequestId())
						.executeUpdate();
			   
			  return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
	} catch (HibernateException he) {
		logger.error("requestChangeToCancel failed", he);
		throw new ServiceException(he.getMessage());
	  } catch (Exception e) {
		logger.error("requestChangeToCancel failed", e);
		throw new ServiceException(e.getMessage());
	  } finally {

	   }
	}


	@Override
	public Response worequestChangeToReject(WorkOrderCreationRequestDto requestDto) throws ApplicationException {
		try {
			//reject request
			NewWorkOrderRejectionDetails rejectObject=new NewWorkOrderRejectionDetails();
			rejectObject.setWorkorderRequestId(requestDto.getWorkorderRequestId());
			rejectObject.setCancelReasonId(requestDto.getCancelReasonId());
			rejectObject.setRejectNote(requestDto.getRejectNote());
			rejectObject.setCreatedBy(requestDto.getCreatedBy());
			rejectObject.setCreatedDate(new  Date());
			rejectObject.setStatus("A");
			sessionFactory.getCurrentSession().save(rejectObject);
			
			sessionFactory.getCurrentSession().createQuery(" UPDATE NewWorkOrderCreation SET workorderRequestStatusId=8 , workorderApprovalStatusId=7 ,approvedBy="+requestDto.getCreatedBy()
	        		+ " WHERE workorderRequestId ="+requestDto.getWorkorderRequestId())
					.executeUpdate();
		  return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		   } catch (HibernateException he) {
		logger.error("worequestChangeToReject failed", he);
		throw new ServiceException(he.getMessage());
		  } catch (Exception e) {
		logger.error("worequestChangeToReject failed", e);
		throw new ServiceException(e.getMessage());
		  } finally {

		   }
			}


	@Override
	public Response getCreationApprovalDetailsbyId(WorkOrderCreationApprovalDto apptovalDto)
			throws ApplicationException {
		try {
//			NewWorkOrderRequestApproval
			 
			List<NewWorkOrderRequestApproval> list=sessionFactory.getCurrentSession().createQuery("from NewWorkOrderRequestApproval approval where approval.workorderRequestId="+apptovalDto.getWorkorderRequestId()).list();
			if(list.size()>0) {
				//approval  saved 
				WorkOrderCreationApprovalDto approvaldetails = (WorkOrderCreationApprovalDto) sessionFactory
						.getCurrentSession().getNamedQuery("GET_WO_REQUEST_APPROVAl_BY_REQUEST_ID")
						.setParameter("workorderRequestId", apptovalDto.getWorkorderRequestId())
						.setResultTransformer(Transformers.aliasToBean(WorkOrderCreationApprovalDto.class)).uniqueResult();
				
				List<WorkOrderCreationRequestApprovalDetailsDto> detaillist = sessionFactory.getCurrentSession()
						.getNamedQuery("GET_WO_REQUEST_APPROVAL_DETAILS_BY_REQUEST_ID")
						.setParameter("workorderRequestId", apptovalDto.getWorkorderRequestId())
						.setResultTransformer(Transformers.aliasToBean(WorkOrderCreationRequestApprovalDetailsDto.class)).list();
				

				List<WorkOrderCreationOtherChargeDetailsDto> otherchargedetaillist = sessionFactory.getCurrentSession()
						.getNamedQuery("GET_WO_REQUEST_OTHERCHARGE_DETAILS_BY_REQUEST_ID")
						.setParameter("workorderRequestId", apptovalDto.getWorkorderRequestId())
						.setResultTransformer(Transformers.aliasToBean(WorkOrderCreationOtherChargeDetailsDto.class))
						.list();

				List<WorkOrderCreationRequestDocumentDto> documentdetaillist = sessionFactory.getCurrentSession()
						.getNamedQuery("GET_WO_REQUEST_DOCUMENT_DETAILS_BY_REQUEST_ID")
						.setParameter("workorderRequestId", apptovalDto.getWorkorderRequestId())
						.setResultTransformer(Transformers.aliasToBean(WorkOrderCreationRequestDocumentDto.class)).list();
	            for (WorkOrderCreationRequestApprovalDetailsDto workOrderCreationRequestDetailsDto : detaillist) {
	                String hql = "SELECT newWorkOrderCreationStagedDetailsMapper FROM NewWorkOrderCreationStagedDetailsMapper AS newWorkOrderCreationStagedDetailsMapper where workorderRequestDetailsId=:WODetailId order by newWorkOrderCreationStagedDetailsMapper.stagedWorkorderId asc ";

	                Query query = sessionFactory.getCurrentSession().createQuery(hql);
	                query.setParameter("WODetailId", workOrderCreationRequestDetailsDto.getWorkorderRequestDetialsId());
	                List<NewWorkOrderCreationStagedDetailsMapper> getList= query.list();
	                List<NewWorkOrderCreationStagedDetailsMapper> getList3=new ArrayList<NewWorkOrderCreationStagedDetailsMapper>();
	                for (NewWorkOrderCreationStagedDetailsMapper getList1 : getList) {
	                    NewWorkOrderCreationStagedDetailsMapper getList2=new NewWorkOrderCreationStagedDetailsMapper();
	                    getList2.setStagedWorkorderId(getList1.getStagedWorkorderId());
	                    getList2.setStageName(getList1.getStageName());
	                    getList2.setPercentage(getList1.getPercentage());
	                    getList2.setWorkorderRequestId(apptovalDto.getWorkorderRequestId());
	                    getList2.setPercentageAmountPaid(getList1.getPercentageAmountPaid());
	                    getList2.setPercentageAmountPay(getList1.getPercentageAmountPay());
	                    getList2.setWorkorderRequestDetailsId(getList1.getWorkorderRequestDetailsId());
	                    getList3.add(getList2);
	                }
	                workOrderCreationRequestDetailsDto.setWorkOrderCreationStagedListData(getList3);
	            }
	            String hql = "SELECT work_order_completion_id from procurement.t_new_work_order_completion "
	    				+ " where  workorder_request_id=:id and is_settled=true order by 1 desc ";
	    		Query query = sessionFactory.getCurrentSession().createSQLQuery(hql);
	    		query.setParameter("id", apptovalDto.getWorkorderRequestId());
	    		query.setMaxResults(1);
                Integer completionId=(Integer) query.uniqueResult();
                approvaldetails.setWorkOrderCompletionId(completionId);
				if(detaillist.size()>0) 
					approvaldetails.setRequestApprovalDetailsDto(detaillist);
				if(otherchargedetaillist.size()>0)
					approvaldetails.setRequestTaxDetailsDto(otherchargedetaillist);
				if(documentdetaillist.size()>0)
					approvaldetails.setRequestDocumentDto(documentdetaillist);
				String creditDaysSql=" select cast(credit_days as int) from public.m_vendor where id="+approvaldetails.getVendorId() ;
				Query creditDaysQuery = sessionFactory.getCurrentSession().createSQLQuery(creditDaysSql);
				Integer creditDays=(Integer) creditDaysQuery.uniqueResult();
				approvaldetails.setCreditDays(creditDays);
				
				String getPaidOtherChargesSql=" select cast(coalesce(sum(other_charge_amount),0) as int)  from procurement.t_new_work_order_completion where workorder_request_id="+apptovalDto.getWorkorderRequestId() ;
				Query getPaidOtherChargesQuery = sessionFactory.getCurrentSession().createSQLQuery(getPaidOtherChargesSql);
				Integer getPaidOtherCharges=(Integer) getPaidOtherChargesQuery.uniqueResult();
				approvaldetails.setOtherChargeAmount((approvaldetails.getOtherChargeAmount()!=null ? approvaldetails.getOtherChargeAmount() : 0) -(getPaidOtherCharges!=null ? getPaidOtherCharges: 0));
			   return new Response<>(SUCCESS, SUCCESS_CODE, "updatedata", null, approvaldetails);
				
			}else {
				//approval not saved 
				WorkOrderCreationApprovalDto approvaldetails = (WorkOrderCreationApprovalDto) sessionFactory
						.getCurrentSession().getNamedQuery("GET_WO_REQUEST_BY_REQUEST_ID")
						.setParameter("workorderRequestId", apptovalDto.getWorkorderRequestId())
						.setResultTransformer(Transformers.aliasToBean(WorkOrderCreationApprovalDto.class)).uniqueResult();

				List<WorkOrderCreationRequestApprovalDetailsDto> detaillist = sessionFactory.getCurrentSession()
						.getNamedQuery("GET_WO_REQUEST_DETAILS_BY_REQUEST_ID")
						.setParameter("workorderRequestId", apptovalDto.getWorkorderRequestId())
						.setResultTransformer(Transformers.aliasToBean(WorkOrderCreationRequestApprovalDetailsDto.class)).list();
				

				List<WorkOrderCreationOtherChargeDetailsDto> otherchargedetaillist = sessionFactory.getCurrentSession()
						.getNamedQuery("GET_WO_REQUEST_OTHERCHARGE_DETAILS_BY_REQUEST_ID")
						.setParameter("workorderRequestId", apptovalDto.getWorkorderRequestId())
						.setResultTransformer(Transformers.aliasToBean(WorkOrderCreationOtherChargeDetailsDto.class))
						.list();

				List<WorkOrderCreationRequestDocumentDto> documentdetaillist = sessionFactory.getCurrentSession()
						.getNamedQuery("GET_WO_REQUEST_DOCUMENT_DETAILS_BY_REQUEST_ID")
						.setParameter("workorderRequestId", apptovalDto.getWorkorderRequestId())
						.setResultTransformer(Transformers.aliasToBean(WorkOrderCreationRequestDocumentDto.class)).list();
				for (WorkOrderCreationRequestApprovalDetailsDto workOrderCreationRequestDetailsDto : detaillist) {
	                String hql = "SELECT newWorkOrderCreationStagedDetailsMapper FROM NewWorkOrderCreationStagedDetailsMapper AS newWorkOrderCreationStagedDetailsMapper where workorderRequestDetailsId=:WODetailId ";

	                Query query = sessionFactory.getCurrentSession().createQuery(hql);
	                query.setParameter("WODetailId", workOrderCreationRequestDetailsDto.getWorkorderRequestDetialsId());
	                List<NewWorkOrderCreationStagedDetailsMapper> getList= query.list();
	                List<NewWorkOrderCreationStagedDetailsMapper> getList3=new ArrayList<NewWorkOrderCreationStagedDetailsMapper>();
	                for (NewWorkOrderCreationStagedDetailsMapper getList1 : getList) {
	                    NewWorkOrderCreationStagedDetailsMapper getList2=new NewWorkOrderCreationStagedDetailsMapper();
	                    getList2.setStagedWorkorderId(getList1.getStagedWorkorderId());
	                    getList2.setStageName(getList1.getStageName());
	                    getList2.setPercentage(getList1.getPercentage());
	                    getList2.setWorkorderRequestId(apptovalDto.getWorkorderRequestId());
	                    getList2.setPercentageAmountPaid(getList1.getPercentageAmountPaid());
	                    getList2.setPercentageAmountPay(getList1.getPercentageAmountPay());
	                    getList3.add(getList2);
	                }
	                workOrderCreationRequestDetailsDto.setWorkOrderCreationStagedListData(getList3);
	            }
				if(detaillist.size()>0) 
					approvaldetails.setRequestApprovalDetailsDto(detaillist);
				if(otherchargedetaillist.size()>0)
					approvaldetails.setRequestTaxDetailsDto(otherchargedetaillist);
				if(documentdetaillist.size()>0)
					approvaldetails.setRequestDocumentDto(documentdetaillist);
				 String creditDaysSql=" select cast(credit_days as int) from public.m_vendor where id="+approvaldetails.getVendorId() ;
					Query query = sessionFactory.getCurrentSession().createSQLQuery(creditDaysSql);
					Integer creditDays=(Integer) query.uniqueResult();
					approvaldetails.setCreditDays(creditDays);
				
					String getPaidOtherChargesSql=" select cast(coalesce(sum(other_charge_amount),0) as int) from procurement.t_new_work_order_completion where workorder_request_id="+apptovalDto.getWorkorderRequestId() ;
					Query getPaidOtherChargesQuery = sessionFactory.getCurrentSession().createSQLQuery(getPaidOtherChargesSql);
					Integer getPaidOtherCharges=(Integer) getPaidOtherChargesQuery.uniqueResult();
					approvaldetails.setOtherChargeAmount((approvaldetails.getOtherChargeAmount()!=null ? approvaldetails.getOtherChargeAmount() : 0) -(getPaidOtherCharges!=null ? getPaidOtherCharges: 0));

			   return new Response<>(SUCCESS, SUCCESS_CODE, "savedata", null, approvaldetails);

			}

	   } catch (HibernateException he) {
		logger.error("getCreationApprovalDetailsbyId failed", he);
		throw new ServiceException(he.getMessage());
	  } catch (Exception e) {
		logger.error("getCreationApprovalDetailsbyId failed", e);
		throw new ServiceException(e.getMessage());
	  } finally {

	  }
	}


	@Override
	public Response saveCreationApprovalDetails(WorkOrderCreationApprovalDto apptovalDto) throws ApplicationException {
	try {
		    NewWorkOrderRequestApproval approval=new NewWorkOrderRequestApproval();
		    if(apptovalDto.getWorkorderRequestApprovalId()!=null)
		    	approval.setWorkorderRequestApprovalId(apptovalDto.getWorkorderRequestApprovalId());
		    approval.setWorkorderRequestId(apptovalDto.getWorkorderRequestId());
		    approval.setWorkorderRequestNumber(apptovalDto.getWorkorderRequestNumber());
		    approval.setStoreId(apptovalDto.getStoreId());
		    approval.setVendorId(apptovalDto.getVendorId());
//		    approval.setDeliveryDate(GlobalCommonDateUtils.getDate(apptovalDto.getDeliveryDate(), "dd-M-yyyy HH:mm:ss") );
		    approval.setBillTypeId(apptovalDto.getBillTypeId());
		    approval.setContractTypeId(apptovalDto.getContractTypeId());
		    approval.setAgainstTypeId(apptovalDto.getAgainstTypeId());
		    approval.setWorkorderRequestDate(TimeZoneComponent.getDateByzone(apptovalDto.getUnitId()));
		    approval.setTotalAmount(apptovalDto.getTotalAmount());
		    approval.setTotalDiscountAmount(apptovalDto.getTotalDiscountAmount());
		    approval.setTotalTaxAmount(apptovalDto.getTotalTaxAmount());
		    approval.setOtherChargeAmount(apptovalDto.getOtherChargeAmount());
		    approval.setTotalNetAmount(apptovalDto.getTotalNetAmount());
		    approval.setPaidAmount(0.0);
		    approval.setPaymentStatusId(1);
		    approval.setBalanceAmount(apptovalDto.getTotalNetAmount());
		    approval.setOrgId(apptovalDto.getOrgId());
		    approval.setUnitId(apptovalDto.getUnitId());
		    approval.setCreatedBy(apptovalDto.getCreatedBy());
		    approval.setCreatedDate(TimeZoneComponent.getDateByzone(apptovalDto.getUnitId()));
		    approval.setUpdatedBy(apptovalDto.getCreatedBy());
		    approval.setUpdatedDate(TimeZoneComponent.getDateByzone(apptovalDto.getUnitId()));
		    approval.setStatus("A");
		    approval.setApprovalRemark(apptovalDto.getApprovalRemark());
		    sessionFactory.getCurrentSession().saveOrUpdate(approval);

//            NewWorkOrderCompletion completion=new NewWorkOrderCompletion();
//		    completion.setWorkorderRequestId(apptovalDto.getWorkorderRequestId());
//		    completion.setWorkorderRequestApprovalId(approval.getWorkorderRequestApprovalId());
//		    completion.setWorkorderRequestNumber(apptovalDto.getWorkorderRequestNumber());
//		    completion.setStoreId(apptovalDto.getStoreId());
//		    completion.setVendorId(apptovalDto.getVendorId());
////		    completion.setDeliveryDate(GlobalCommonDateUtils.getDate(apptovalDto.getDeliveryDate(), "dd-M-yyyy HH:mm:ss") );
//		    completion.setBillTypeId(apptovalDto.getBillTypeId());
//		    completion.setContractTypeId(apptovalDto.getContractTypeId());
//		    completion.setAgainstTypeId(apptovalDto.getAgainstTypeId());
//		    completion.setWorkorderRequestDate(TimeZoneComponent.getDateByzone(apptovalDto.getUnitId()));
//		    completion.setTotalAmount(apptovalDto.getTotalAmount());
//		    completion.setTotalDiscountAmount(apptovalDto.getTotalDiscountAmount());
//		    completion.setTotalTaxAmount(apptovalDto.getTotalTaxAmount());
//		    completion.setOtherChargeAmount(apptovalDto.getOtherChargeAmount());
//		    completion.setTotalNetAmount(apptovalDto.getTotalNetAmount());
//		    completion.setPaidAmount(0.0);
//		    completion.setPaymentStatusId(1);
//		    completion.setBalanceAmount(apptovalDto.getTotalNetAmount());
//		    completion.setOrgId(apptovalDto.getOrgId());
//		    completion.setUnitId(apptovalDto.getUnitId());
//		    completion.setCreatedBy(apptovalDto.getCreatedBy());
//		    completion.setCreatedDate(TimeZoneComponent.getDateByzone(apptovalDto.getUnitId()));
//		    completion.setUpdatedBy(apptovalDto.getCreatedBy());
//		    completion.setUpdatedDate(TimeZoneComponent.getDateByzone(apptovalDto.getUnitId()));
//		    completion.setStatus("A");
//		    completion.setApprovalRemark(apptovalDto.getApprovalRemark());
//		    sessionFactory.getCurrentSession().saveOrUpdate(completion);
            sessionFactory.getCurrentSession().createQuery(" UPDATE NewWorkOrderCreation SET workorderRequestStatusId=8 , workorderApprovalStatusId=5 , workorderCompletionStatusId=2, isReverseTax="+apptovalDto.getIsReverseTax()+", approvedBy="+apptovalDto.getCreatedBy()
    		+ " WHERE workorderRequestId ="+apptovalDto.getWorkorderRequestId())
			.executeUpdate();
		    if(apptovalDto.getRequestApprovalDetailsDto().size()>0){


		    	if(apptovalDto.getWorkorderRequestId() != null) {
                    String hql = "delete FROM procurement.t_new_work_order_creation_staged_details  where workorder_request_id=:DetailId ";

                    Query query = sessionFactory.getCurrentSession().createSQLQuery(hql);
                    query.setParameter("DetailId", apptovalDto.getWorkorderRequestId());
                     query.executeUpdate();
                }
		    	
		    	for (WorkOrderCreationRequestApprovalDetailsDto detailsdto: apptovalDto.getRequestApprovalDetailsDto()) {
		    		NewWorkOrderApprovalDetails details = new  NewWorkOrderApprovalDetails();
		    		
		    		details.setWorkorderRequestDetialsId(detailsdto.getWorkorderRequestDetialsId());		
		    		details.setWorkorderRequestId(apptovalDto.getWorkorderRequestId());
		    		if(detailsdto.getItemId()!=null) 
		    			     details.setItemId(detailsdto.getItemId());
		    		
		    		details.setStoreId(detailsdto.getStoreId());
		    		details.setItemName(detailsdto.getItemName());
		    		details.setItemQuantity(detailsdto.getItemQuantity());
		    		details.setItemRate(detailsdto.getItemRate());
		    		details.setItemMrp(detailsdto.getItemMrp());
		    		if(detailsdto.getItemDiscPercentage()!=null) 
		    			details.setItemDiscPercentage(detailsdto.getItemDiscPercentage());
		    		if(detailsdto.getItemDiscAmount()!=null)  
		    			details.setItemDiscAmount(detailsdto.getItemDiscAmount());
		    		if(detailsdto.getItemTaxId()!=null)  	
		    			details.setItemTaxId(detailsdto.getItemTaxId());
		    		if(detailsdto.getItemTax()!=null)  
		    			details.setItemTax(detailsdto.getItemTax());
		    		if(detailsdto.getItemTaxAmount()!=null)
		    			details.setItemTaxAmount(detailsdto.getItemTaxAmount());
		    		details.setItemNetAmount(detailsdto.getItemNetAmount());
		    		details.setOrgId(apptovalDto.getOrgId());
		    		details.setUnitId(apptovalDto.getUnitId());
		    		details.setCreatedBy(apptovalDto.getCreatedBy());
		    		details.setUpdatedDate(TimeZoneComponent.getDateByzone(apptovalDto.getUnitId()));
		    		details.setUpdatedBy(apptovalDto.getCreatedBy());
		    		details.setStatus("A");
		    		details.setItemRemark(detailsdto.getItemRemark());
		    		details.setCreatedDate(TimeZoneComponent.getDateByzone(apptovalDto.getUnitId()));
		    		details.setApprovedQty(detailsdto.getApprovedQty());
		    		details.setRejectedQty(detailsdto.getRejectedQty());
		    		details.setApprovalDetailRemark(detailsdto.getApprovalDetailRemark());
		    		details.setIsRejectedQty(detailsdto.getIsRejectedQty());
		    		if(detailsdto.getIsSelected()!=null) {
		    			details.setIsSelected(detailsdto.getIsSelected());
		    		}else{
		    			details.setIsSelected(false);
		    		}
		    					if(detailsdto.getIsRejectedQty()==true) {
		    						if(detailsdto.getItemRejectionObject().getWorkorderRequestDetialsId()!=null) {
		    							NewWorkOrderItemRejectionDetails rejectItem=new NewWorkOrderItemRejectionDetails();
			    						rejectItem.setWorkorderRequestId(apptovalDto.getWorkorderRequestId());
			    						rejectItem.setWorkorderRequestDetialsId(detailsdto.getItemRejectionObject().getWorkorderRequestDetialsId());
			    						rejectItem.setCancelReasonId(detailsdto.getItemRejectionObject().getCancelReasonId());
			    						rejectItem.setRejectNote(detailsdto.getItemRejectionObject().getRejectNote());
			    						rejectItem.setCreatedBy(apptovalDto.getCreatedBy());
			    						rejectItem.setCreatedDate(new  Date());
			    						rejectItem.setStatus("A");
			    						sessionFactory.getCurrentSession().save(rejectItem);
		    						}
		    					}
		                        if(detailsdto.getWorkOrderCreationStagedListData().size()>0)
		                        {
		                        for (NewWorkOrderCreationStagedDetailsMapper stagedMapper : detailsdto.getWorkOrderCreationStagedListData()) {
		                            NewWorkOrderCreationStagedDetailsMapper stagedDetails = new NewWorkOrderCreationStagedDetailsMapper();
		                            stagedDetails.setOrganizationId(apptovalDto.getOrgId());
		                            stagedDetails.setUnitId(apptovalDto.getUnitId());
		                            stagedDetails.setUpdatedDate(TimeZoneComponent.getDateByzone(apptovalDto.getUnitId()));
		                            stagedDetails.setUpdatedByUser(apptovalDto.getUpdatedBy());
		                            stagedDetails.setAddedByUser(apptovalDto.getCreatedBy());
		                            stagedDetails.setAddedDate(TimeZoneComponent.getDateByzone(apptovalDto.getUnitId()));
		                            stagedDetails.setPercentage(stagedMapper.getPercentage());
		                            stagedDetails.setStageName(stagedMapper.getStageName());
		                            stagedDetails.setWorkorderRequestDetailsId(details.getWorkorderRequestDetialsId());
		                            stagedDetails.setWorkorderRequestId(apptovalDto.getWorkorderRequestId());
		                            sessionFactory.getCurrentSession().save(stagedDetails);
		                        }
		                        }
		    		sessionFactory.getCurrentSession().save(details);
		    		
		    	}
		    	

		    }
		    
		    
		    
		    
		    
		
		
		 return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);

	} catch (HibernateException he) {
		logger.error("saveCreationApprovalDetails failed", he);
		throw new ServiceException(he.getMessage());
		  } catch (Exception e) {
		logger.error("saveCreationApprovalDetails failed", e);
		throw new ServiceException(e.getMessage());
		  }
	}



	@Override
	public Response requestChangeToApproved(WorkOrderCreationRequestDto requestDto) throws ApplicationException {
		try {
			if(requestDto.getWorkOrderRequestList().size()>0){
				for (WorkOrderCreationRequestDto dto:requestDto.getWorkOrderRequestList()) {
					
					if(dto.getWorkorderRequestId()!=null) {
					
						List<NewWorkOrderRequestApproval> list=sessionFactory.getCurrentSession().createQuery("from NewWorkOrderRequestApproval approval where approval.workorderRequestId="+dto.getWorkorderRequestId()).list();
						if(list.size()==0) {
							WorkOrderCreationApprovalDto approvaldetails = (WorkOrderCreationApprovalDto) sessionFactory
									.getCurrentSession().getNamedQuery("GET_WO_REQUEST_BY_REQUEST_ID")
									.setParameter("workorderRequestId", dto.getWorkorderRequestId())
									.setResultTransformer(Transformers.aliasToBean(WorkOrderCreationApprovalDto.class)).uniqueResult();

							List<WorkOrderCreationRequestApprovalDetailsDto> detaillist = sessionFactory.getCurrentSession()
									.getNamedQuery("GET_WO_REQUEST_DETAILS_BY_REQUEST_ID")
									.setParameter("workorderRequestId", dto.getWorkorderRequestId())
									.setResultTransformer(Transformers.aliasToBean(WorkOrderCreationRequestApprovalDetailsDto.class)).list();
							

							List<WorkOrderCreationOtherChargeDetailsDto> otherchargedetaillist = sessionFactory.getCurrentSession()
									.getNamedQuery("GET_WO_REQUEST_OTHERCHARGE_DETAILS_BY_REQUEST_ID")
									.setParameter("workorderRequestId", dto.getWorkorderRequestId())
									.setResultTransformer(Transformers.aliasToBean(WorkOrderCreationOtherChargeDetailsDto.class))
									.list();

							
							if(detaillist.size()>0) 
								approvaldetails.setRequestApprovalDetailsDto(detaillist);
							if(otherchargedetaillist.size()>0)
								approvaldetails.setRequestTaxDetailsDto(otherchargedetaillist);
							
							//saveapproval details
							NewWorkOrderRequestApproval approval=new NewWorkOrderRequestApproval();
						    if(approvaldetails.getWorkorderRequestApprovalId()!=null)
						    	approval.setWorkorderRequestApprovalId(approvaldetails.getWorkorderRequestApprovalId());
						    approval.setWorkorderRequestId(approvaldetails.getWorkorderRequestId());
						    approval.setWorkorderRequestNumber(approvaldetails.getWorkorderRequestNumber());
						    approval.setStoreId(approvaldetails.getStoreId());
						    approval.setVendorId(approvaldetails.getVendorId());
//						    approval.setDeliveryDate(GlobalCommonDateUtils.getDate(approvaldetails.getDeliveryDate(), "dd/MM/yyyy HH:mm:ss") );
						    approval.setBillTypeId(approvaldetails.getBillTypeId());
						    approval.setContractTypeId(approvaldetails.getContractTypeId());
						    approval.setAgainstTypeId(approvaldetails.getAgainstTypeId());
						    approval.setWorkorderRequestDate(TimeZoneComponent.getDateByzone(requestDto.getUnitId()));
						    approval.setTotalAmount(approvaldetails.getTotalAmount());
						    approval.setTotalDiscountAmount(approvaldetails.getTotalDiscountAmount());
						    approval.setTotalTaxAmount(approvaldetails.getTotalTaxAmount());
						    approval.setOtherChargeAmount(approvaldetails.getOtherChargeAmount());
						    approval.setTotalNetAmount(approvaldetails.getTotalNetAmount());
						    approval.setPaidAmount(0.0);
						    approval.setPaymentStatusId(1);
						    approval.setBalanceAmount(approvaldetails.getTotalNetAmount());
						    approval.setOrgId(requestDto.getOrgId());
						    approval.setUnitId(requestDto.getUnitId());
						    approval.setCreatedBy(requestDto.getCreatedBy());
						    approval.setCreatedDate(TimeZoneComponent.getDateByzone(requestDto.getUnitId()));
						    approval.setUpdatedBy(requestDto.getCreatedBy());
						    approval.setUpdatedDate(TimeZoneComponent.getDateByzone(requestDto.getUnitId()));
						    approval.setStatus("A");
						    approval.setApprovalRemark("");
						    sessionFactory.getCurrentSession().saveOrUpdate(approval);
						    // add completion
//						    NewWorkOrderCompletion completion=new NewWorkOrderCompletion();
//						    	completion.setWorkorderRequestApprovalId(approval.getWorkorderRequestApprovalId());
//						    completion.setWorkorderRequestId(approvaldetails.getWorkorderRequestId());
//						    completion.setWorkorderRequestNumber(approvaldetails.getWorkorderRequestNumber());
//						    completion.setStoreId(approvaldetails.getStoreId());
//						    completion.setVendorId(approvaldetails.getVendorId());
////						    approval.setDeliveryDate(GlobalCommonDateUtils.getDate(approvaldetails.getDeliveryDate(), "dd/MM/yyyy HH:mm:ss") );
//						    completion.setBillTypeId(approvaldetails.getBillTypeId());
//						    completion.setContractTypeId(approvaldetails.getContractTypeId());
//						    completion.setAgainstTypeId(approvaldetails.getAgainstTypeId());
//						    completion.setWorkorderRequestDate(TimeZoneComponent.getDateByzone(requestDto.getUnitId()));
//						    completion.setTotalAmount(approvaldetails.getTotalAmount());
//						    completion.setTotalDiscountAmount(approvaldetails.getTotalDiscountAmount());
//						    completion.setTotalTaxAmount(approvaldetails.getTotalTaxAmount());
//						    completion.setOtherChargeAmount(approvaldetails.getOtherChargeAmount());
//						    completion.setTotalNetAmount(approvaldetails.getTotalNetAmount());
//						    completion.setPaidAmount(0.0);
//						    completion.setPaymentStatusId(1);
//						    completion.setBalanceAmount(approvaldetails.getTotalNetAmount());
//						    completion.setOrgId(requestDto.getOrgId());
//						    completion.setUnitId(requestDto.getUnitId());
//						    completion.setCreatedBy(requestDto.getCreatedBy());
//						    completion.setCreatedDate(TimeZoneComponent.getDateByzone(requestDto.getUnitId()));
//						    completion.setUpdatedBy(requestDto.getCreatedBy());
//						    completion.setUpdatedDate(TimeZoneComponent.getDateByzone(requestDto.getUnitId()));
//						    completion.setStatus("A");
//						    completion.setApprovalRemark("");
//						    sessionFactory.getCurrentSession().saveOrUpdate(completion);
						    
						    if(approvaldetails.getRequestApprovalDetailsDto().size()>0){
						    	Session	opensession=sessionFactory.getCurrentSession();
						    	
						    	opensession.createQuery(" UPDATE NewWorkOrderApprovalDetails SET status='I'  "
						        		+ " WHERE workorderRequestId ="+approvaldetails.getWorkorderRequestId())
										.executeUpdate();
						    	opensession.close();
						    	
						    	for (WorkOrderCreationRequestApprovalDetailsDto detailsdto: approvaldetails.getRequestApprovalDetailsDto()) {
						    		NewWorkOrderApprovalDetails details = new  NewWorkOrderApprovalDetails();
						    		
						    		details.setWorkorderRequestDetialsId(detailsdto.getWorkorderRequestDetialsId());		
						    		details.setWorkorderRequestId(dto.getWorkorderRequestId());
						    		if(detailsdto.getItemId()!=null) 
						    			     details.setItemId(detailsdto.getItemId());
						    		details.setStoreId(detailsdto.getStoreId());
						    		details.setItemName(detailsdto.getItemName());
						    		details.setItemQuantity(detailsdto.getItemQuantity());
						    		details.setItemRate(detailsdto.getItemRate());
						    		details.setItemMrp(detailsdto.getItemMrp());
						    		if(detailsdto.getItemDiscPercentage()!=null) 
						    			details.setItemDiscPercentage(detailsdto.getItemDiscPercentage());
						    		if(detailsdto.getItemDiscAmount()!=null)  
						    			details.setItemDiscAmount(detailsdto.getItemDiscAmount());
						    		if(detailsdto.getItemTaxId()!=null)  	
						    			details.setItemTaxId(detailsdto.getItemTaxId());
						    		if(detailsdto.getItemTax()!=null)  
						    			details.setItemTax(detailsdto.getItemTax());
						    		if(detailsdto.getItemTaxAmount()!=null)
						    			details.setItemTaxAmount(detailsdto.getItemTaxAmount());
						    		details.setItemNetAmount(detailsdto.getItemNetAmount());
						    		details.setOrgId(requestDto.getOrgId());
						    		details.setUnitId(requestDto.getUnitId());
						    		details.setCreatedBy(requestDto.getCreatedBy());
						    		details.setUpdatedDate(TimeZoneComponent.getDateByzone(requestDto.getUnitId()));
						    		details.setUpdatedBy(requestDto.getCreatedBy());
						    		details.setStatus("A");
						    		details.setItemRemark(detailsdto.getItemRemark());
						    		details.setCreatedDate(TimeZoneComponent.getDateByzone(requestDto.getUnitId()));
						    		details.setApprovedQty(detailsdto.getItemQuantity());
						    		details.setRejectedQty(0);
						    		details.setApprovalDetailRemark("");
						    		details.setIsRejectedQty(false);
						    		details.setIsSelected(true);
						    		sessionFactory.getCurrentSession().save(details);
						    	}
						    }
							
						}
						sessionFactory.getCurrentSession().createQuery(" UPDATE NewWorkOrderCreation SET workorderRequestStatusId=8 , workorderApprovalStatusId=5 ,workorderCompletionStatusId=2, approvedBy="+requestDto.getCreatedBy()
				        		+ " WHERE workorderRequestId ="+dto.getWorkorderRequestId())
								.executeUpdate();	
					}
					
				}
				   
				  return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
			}
			return new Response<>(ERROR, ERROR_CODE, null, null, null);
	  } catch (HibernateException he) {
		logger.error("requestChangeToApproved failed", he);
		throw new ServiceException(he.getMessage());
	  } catch (Exception e) {
		logger.error("requestChangeToApproved failed", e);
		throw new ServiceException(e.getMessage());
	  } finally {

	   }
	}


	@Override
	public Response getRequestApprovalList(WorkOrderCreationApprovalDto requestDto) throws ApplicationException {
		try {
		//approval list
			String query= "select " + 
					"	request.workorder_request_id as \"workorderRequestId\" , " + 
					"	approval.workorder_request_approval_id as \"workorderRequestApprovalId\", " + 
					"	request.workorder_request_number as \"workorderRequestNumber\" , " + 
					"	request.bill_type_id as \"billTypeId\" , " + 
					"	request.store_id as \"storeId\" , " + 
					"	request.vendor_id as \"vendorId\" , " + 
					"	to_char(request.delivery_date, 'dd/mm/yyyy') as \"deliveryDate\" , " + 
					"	request.contract_type_id as \"contractTypeId\" , " + 
					"	to_char(request.workorder_request_date , 'dd/mm/yyyy') as \"workorderRequestDate\" , " + 
					"	approval.total_amount as \"totalAmount\" , " + 
					"	approval.paid_amount as \"paidAmount\", " + 
					"	approval.balance_amount as \"balanceAmount\","+ 
					"   approval.payment_status_id as \"paymentStatusId\", " + 
					"	(case " + 
					"		when empmst.employee_id is not null then concat(empmst.first_name, ' ', empmst.last_name) " + 
					"		when docmst.doctor_id is not null then concat('Dr. ', docmst.first_name, ' ', docmst.last_name) " + 
					"		else 'Admin' " + 
					"	end) as \"userName\" , " + 
					"	(case " + 
					"		when empmst1.employee_id is not null then concat(empmst1.first_name, ' ', empmst1.last_name) " + 
					"		when docmst1.doctor_id is not null then concat('Dr. ', docmst1.first_name, ' ', docmst1.last_name) " + 
					"		else 'Admin' " + 
					"	end) as \"approvedName\" , " + 
					"	approval.total_discount_amount as \"totalDiscountAmount\" , " + 
					"	approval.total_tax_amount as \"totalTaxAmount\" , " + 
					"	approval.other_charge_amount as \"otherChargeAmount\" , " + 
					"	approval.total_net_amount as \"totalNetAmount\" , " + 
					"	request.workorder_request_status_id as \"workorderRequestStatusId\" , " + 
					"	request.payment_terms as \"paymentTerms\" , " + 
					"	request.guarantee_warranty as \"guaranteeWarranty\" , " + 
					"	request.org_id as \"orgId\" , " + 
					"	request.unit_id as \"unitId\" , " + 
					"	request.created_by as \"createdBy\" , " + 
					"	vendortab.name as \"vendorName\" , " + 
					"	storetab.store_name as \"storeName\" , " + 
					"	agaisttypeMst.type as \"againstTypeName\", " + 
					"	approval.work_order_completion_id as \"workOrderCompletionId\", " +
					" 	mct.description as \"contractType\", " +
					 " 	concat((case when tpr.is_vip = 'Y' then coalesce(tpr.alise_name, tpr.first_name) else concat(coalesce(tpr.first_name) , ' ', coalesce(tpr.middle_name) , ' ', coalesce(tpr.last_name)) end ), concat(to_char(extract(year from age(current_timestamp, tpr.dob)), '999'), 'Y', '/', mgm.gender_code)) as \"patientDetails\", "  +
					 " 	tpr.uhid_number as \"uhidNumber\", "  +
					 " 	concat(mbm.bill_number, ' | ', to_char(mbm.bill_date_time, 'DD/MM/YYYY')) as \"billDetails\", " +
					 " 	tem.encounter_number as \"visitDetails\" "  +
					
					"from " + 
					"	procurement.t_new_work_order_creation request " + 
					"inner join procurement.t_new_work_order_completion approval on " + 
					"	request.workorder_request_id = approval.workorder_request_id " + 
					"left join public.m_vendor vendortab on " + 
					"	request.vendor_id = vendortab.id " + 
					"left join public.m_store storetab on " + 
					"	request.store_id = storetab.store_id " + 
					"left join public.m_request_type agaisttypeMst on " + 
					"	request.against_type_id = agaisttypeMst.id " + 
					"left join PUBLIC.m_employee_master empmst on " + 
					"	request.created_by = empmst.user_id " + 
					"left join doctor.m_doctor_master docmst on " + 
					"	request.created_by = docmst.user_id " + 
					"left join PUBLIC.m_employee_master empmst1 on " + 
					"	request.approved_by = empmst1.user_id " + 
					"left join doctor.m_doctor_master docmst1 on " + 
					"	request.approved_by = docmst1.user_id " 
					+ " left join procurement.m_contract_type mct on " 
					 + " 	mct.id = request.contract_type_id " 
					 + " left join billing.m_billing_master mbm on " 
					 + " 	mbm.billing_master_id = request.bill_id " 
					 + " left join opd.t_encounter_master tem on " 
					 + " 	tem.encounter_id = request.encounter_id " 
					 + " left join adt.t_admission ta on " 
					 + " 	ta.admission_id = request.admission_id " 
					 + " left join patient.t_patient_registration tpr on " 
					 + " 	tpr.patient_id = " 
					 + " 	(case " 
					 + " 		when request.encounter_id is not null then tem.patient_id " 
					 + " 		else ta.patient_id " 
					 + " 	end) " 
					 + " left join m_gender_master mgm on " 
					 + " 	mgm.gender_id = tpr.gender_id "+
					" where " +
				    "	request.store_id =:storeId " + 
				    "	and request.org_id =:orgId " + 
				    "	and request.unit_id =:unitId " + 
				    "	and request.status = 'A' "
				    + " and request.workorder_approval_status_id = 5  and approval.payment_status_id!=3 and approval.is_settled=true ";
				
		StringBuilder buildquery = new StringBuilder(query);
			if(requestDto.getFromDate()!=null && requestDto.getFromDate()!="" && requestDto.getToDate()!=null && requestDto.getToDate()!="" ) {
//				buildquery.append(" and to_char(request.delivery_date, 'dd/mm/yyyy') between ")
				buildquery.append(" and to_char(request.workorder_request_date,'YYYY-MM-DD') between :fromDate and :toDate ");
			}
			if(requestDto.getBillTypeId()!=null  ) {
				buildquery.append(" and request.bill_type_id=:billTypeId ");
			}
			if(requestDto.getVendorId()!=null  ) {
				buildquery.append(" and request.vendor_id=:vendorId ");
			}

			if (requestDto.getWorkorderRequestNumber() != null && !requestDto.getWorkorderRequestNumber().isEmpty()) {
				buildquery.append(" and request.workorder_request_number like :workorderRequestNumber ");
			}

		if(requestDto.getPaymentStatusId()!=null) {
			buildquery.append(" and approval.payment_status_id=:paymentStatusId ");
		}
		buildquery.append(" order by approval.work_order_completion_id desc ");
			Query createQuery=sessionFactory.getCurrentSession().createSQLQuery(buildquery.toString())
				.setParameter("storeId", requestDto.getStoreId())
				.setParameter("orgId", requestDto.getOrgId())
				.setParameter("unitId", requestDto.getUnitId());

			if(requestDto.getFromDate()!=null && requestDto.getFromDate()!="" && requestDto.getToDate()!=null && requestDto.getToDate()!="" ) {
				createQuery.setParameter("fromDate", requestDto.getFromDate());
				createQuery.setParameter("toDate", requestDto.getToDate());
			}
			if(requestDto.getBillTypeId()!=null  ) {
				createQuery.setParameter("billTypeId", requestDto.getBillTypeId());
			}

			if(requestDto.getVendorId()!=null  ) {
				createQuery.setParameter("vendorId", requestDto.getVendorId());
			}

			if (requestDto.getWorkorderRequestNumber() != null && !requestDto.getWorkorderRequestNumber().isEmpty()) {
				createQuery.setParameter("workorderRequestNumber", "%" + requestDto.getWorkorderRequestNumber() + "%");
			}

			if(requestDto.getPaymentStatusId()!=null   ) {
				createQuery.setParameter("paymentStatusId", requestDto.getPaymentStatusId());

			}


			List<WorkOrderCreationRequestDto> listrequestDto =	createQuery.setResultTransformer(Transformers.aliasToBean(WorkOrderCreationApprovalDto.class)).list();
		return new Response<>(SUCCESS, SUCCESS_CODE, null, listrequestDto, null);

	} catch (HibernateException he) {
		logger.error("getRequestApprovalList failed", he);
		throw new ServiceException(he.getMessage());
	} catch (Exception e) {
		logger.error("getRequestApprovalList failed", e);
		throw new ServiceException(e.getMessage());
	} finally {

	}
	}


	@Override
	public Response saveWorkOrderSettle(WorkOrderApprovalSettlementDto requestDto) throws ApplicationException {
	try {
		NewWorkOrderApprovalSettlement settlementobj=new NewWorkOrderApprovalSettlement();
		settlementobj.setSettlementAmount(requestDto.getSettlementAmount());
		settlementobj.setWorkorderSettlementNumber(iGlobalUnitMasterDao.getGeneratedNo(0, requestDto.getUnitId(), IBillingCommonConstants.WORKORDER_RECIEPT_NUMBER));
		settlementobj.setVendorId(requestDto.getVendorId());
		settlementobj.setOrgId(requestDto.getOrgId());
		settlementobj.setUnitId(requestDto.getUnitId());
		settlementobj.setCreatedBy(requestDto.getCreatedBy());
		settlementobj.setUpdatedBy(requestDto.getCreatedBy());
		settlementobj.setCreatedDate(TimeZoneComponent.getDateByzone(requestDto.getUnitId()));
		settlementobj.setUpdatedDate(TimeZoneComponent.getDateByzone(requestDto.getUnitId()));
		settlementobj.setStatus("A");
		sessionFactory.getCurrentSession().save(settlementobj);
	                  	//setteled REQUESTS details
		               if(requestDto.getWorkOrderRequestList()!=null && requestDto.getWorkOrderRequestList().size()>0 ) {
						Session	opensession=sessionFactory.getCurrentSession();
						for(WorkOrderCreationApprovalDto settlementDto :requestDto.getWorkOrderRequestList()) {
							
							NewWorkOrderApprovalSettlementDetails  settlementDetailobj=new NewWorkOrderApprovalSettlementDetails();
							settlementDetailobj.setWorkorderSettlementId(settlementobj.getWorkorderSettlementId());
							settlementDetailobj.setWorkorderRequestApprovalId(settlementDto.getWorkorderRequestApprovalId());
							settlementDetailobj.setWorkorderRequestId(settlementDto.getWorkorderRequestId());
							settlementDetailobj.setPaidAmount(settlementDto.getBalanceAmount());
							settlementDetailobj.setCreatedDate(TimeZoneComponent.getDateByzone(requestDto.getUnitId()));
							settlementDetailobj.setUpdatedDate(TimeZoneComponent.getDateByzone(requestDto.getUnitId()));
							settlementDetailobj.setCreatedBy(requestDto.getCreatedBy());
							settlementDetailobj.setUpdatedBy(requestDto.getCreatedBy());
							settlementDetailobj.setStatus("A");
							sessionFactory.getCurrentSession().save(settlementDetailobj);
							opensession.createQuery(" UPDATE NewWorkOrderCompletion SET paidAmount=:paidAmount , balanceAmount=:balanceAmount, paymentStatusId=:paymentStatusId "
					        		+ " WHERE workOrderCompletionId =:workorderRequestApprovalId")
							        .setDouble("paidAmount", settlementDto.getBalanceAmount())
							        .setDouble("balanceAmount", 0.0)
							        .setParameter("paymentStatusId", 3)
							        .setParameter("workorderRequestApprovalId", settlementDto.getWorkOrderCompletionId())
									.executeUpdate();
							
						}
						    opensession.close();
					}
					//setteled Payementdetails
					if(requestDto.getSettlementDetailsDto()!=null && requestDto.getSettlementDetailsDto().size()>0 ) {
								
						for(WOSettlementDetailsDto payementDto: requestDto.getSettlementDetailsDto()) {
							
							NewWorkOrderSettlementPaymentDetails paymentmodeDetails=new NewWorkOrderSettlementPaymentDetails();
								paymentmodeDetails.setWorkorderSettlementId(settlementobj.getWorkorderSettlementId());
								paymentmodeDetails.setPaymentModeId(payementDto.getPaymentModeId());
								paymentmodeDetails.setAmount(payementDto.getAmount());
								if(payementDto.getPaymentModeId()==2) {
									//cheque
									paymentmodeDetails.setBankId(payementDto.getBankId());
									paymentmodeDetails.setChequeNumber(payementDto.getChequeNumber());
									paymentmodeDetails.setChequeDate(GlobalCommonDateUtils.getDate(payementDto.getChequeDate(), "dd-M-yyyy HH:mm:ss") );
								}
	                            if(payementDto.getPaymentModeId()==3) {
	                            	//card
	                            	paymentmodeDetails.setCardHolderName(payementDto.getCardHolderName());
	                            	paymentmodeDetails.setCardTypeId(payementDto.getCardTypeId());
	                            	paymentmodeDetails.setCardNumber(payementDto.getCardNo());
	                            	paymentmodeDetails.setExpiryDate(GlobalCommonDateUtils.getDate(payementDto.getExpiryDate(), "dd-M-yyyy HH:mm:ss"));
								}
	                            if(payementDto.getPaymentModeId()==8) {
									//e transfer
	                            	paymentmodeDetails.setBankId(payementDto.getBankId());
	                            	paymentmodeDetails.setTransactionTypeId(payementDto.getTransactionTypeId());
	                            	paymentmodeDetails.setTransactionNo(payementDto.getTransactionNo());
	                            	paymentmodeDetails.setTransTransactionDate(GlobalCommonDateUtils.getDate(payementDto.getTransTransactionDate(), "dd-M-yyyy HH:mm:ss"));
								}
	                            paymentmodeDetails.setOrgId(requestDto.getOrgId());
	                            paymentmodeDetails.setUnitId(requestDto.getUnitId());
	                            paymentmodeDetails.setCreatedBy(requestDto.getCreatedBy());
	                            paymentmodeDetails.setUpdatedBy(requestDto.getCreatedBy());
	                            paymentmodeDetails.setCreatedDate(TimeZoneComponent.getDateByzone(requestDto.getUnitId()));
	                    		paymentmodeDetails.setUpdatedDate(TimeZoneComponent.getDateByzone(requestDto.getUnitId()));
	                    		paymentmodeDetails.setStatus("A");
	                    		sessionFactory.getCurrentSession().save(paymentmodeDetails);
						}
					}
		
					return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
	} catch (HibernateException he) {
		logger.error("saveWorkOrderSettle failed", he);
		throw new ServiceException(he.getMessage());
	} catch (Exception e) {
		logger.error("saveWorkOrderSettle failed", e);
		throw new ServiceException(e.getMessage());
	} finally {

	}
	}


	@Override
	public Response getRequestSettledList(WorkOrderApprovalSettlementDto requestDto) throws ApplicationException {
		try {
			
			/*String query= "select " + 
					"	settlement.workorder_settlement_id as \"workorderSettlementId\" , " + 
					"	settlement.workorder_settlement_number as \"workorderSettlementNumber\", " + 
					"	settlement.settlement_amount as \"settlementAmount\", " + 
					"	settlement.vendor_id as \"vendorId\", " + 
					"	vendortab.name as \"vendorName\", " + 
					"	settlement.org_id as \"orgId\", " + 
					"	settlement.unit_id as \"unitId\", " + 
					"	to_char(settlement.created_date , 'dd/mm/yyyy') as \"createdDate\" " + 
					"   from " + 
					"	procurement.t_new_work_order_settlement settlement " + 
					"   left join public.m_vendor vendortab on " + 
					"	settlement.vendor_id = vendortab.id " + 
					"   where " + 
					"	settlement.status = 'A' " + 
					"	and settlement.org_id =:orgId " + 
					"	and settlement.unit_id =:unitId ";*/
			String query= "select " + 
					"	settlement.workorder_settlement_id as \"workorderSettlementId\" , " + 
					"	settlement.workorder_settlement_number as \"workorderSettlementNumber\", " + 
					"	details.workorder_request_id as \"workorderRequestId\", " + 
					"	details.paid_amount as \"settlementAmount\"," + 
					"	settlement.vendor_id as \"vendorId\", " + 
					"	vendortab.name as \"vendorName\", " + 
					"	settlement.org_id as \"orgId\", " + 
					"	settlement.unit_id as \"unitId\", " + 
					"	to_char(settlement.created_date , 'dd/mm/yyyy') as \"createdDate\" " + 
					"   from " + 
					"	procurement.t_new_work_order_settlement settlement " + 
					"	" + 
					"   inner join procurement.t_new_work_order_settlement_details details on " + 
					"   details.workorder_settlement_id =settlement.workorder_settlement_id " + 
					"   inner join procurement.t_new_work_order_creation request on " + 
					"	details.workorder_request_id = request.workorder_request_id " + 
					"   left join public.m_vendor vendortab on " + 
					"	settlement.vendor_id = vendortab.id  " + 
					"   where " + 
					"	settlement.status = 'A' " + 
					"	and settlement.org_id =:orgId " + 
					"	and settlement.unit_id =:unitId ";
				
		StringBuilder buildquery = new StringBuilder(query);
			if(requestDto.getFromDate()!=null && requestDto.getFromDate()!="" && requestDto.getToDate()!=null && requestDto.getToDate()!="" ) {
//				buildquery.append(" and to_char(request.delivery_date, 'dd/mm/yyyy') between ")
				buildquery.append(" and to_char(request.workorder_request_date,'YYYY-MM-DD') between :fromDate and :toDate ");
			}
			if(requestDto.getVendorId()!=null  ) {
				buildquery.append(" and request.vendor_id=:vendorId ");

			}
		if(requestDto.getWorkorderSettlementNumber()!=null && requestDto.getWorkorderSettlementNumber()!="" ) {
			buildquery.append(" and settlement.workorder_settlement_number like :workorderSettlementNumber ");
		}
		buildquery.append(" order by settlement.workorder_settlement_id desc");
		Query createQuery=sessionFactory.getCurrentSession().createSQLQuery(buildquery.toString())
				.setParameter("orgId", requestDto.getOrgId())
				.setParameter("unitId", requestDto.getUnitId());

			if(requestDto.getFromDate()!=null && requestDto.getFromDate()!="" && requestDto.getToDate()!=null && requestDto.getToDate()!="" ) {
				createQuery.setParameter("fromDate", requestDto.getFromDate());
				createQuery.setParameter("toDate", requestDto.getToDate());
			}

			if(requestDto.getVendorId()!=null  ) {
				createQuery.setParameter("vendorId", requestDto.getVendorId());
			}

			if (requestDto.getWorkorderRequestNumber() != null && !requestDto.getWorkorderRequestNumber().isEmpty()) {
				createQuery.setParameter("workorderSettlementNumber", "%" + requestDto.getWorkorderSettlementNumber() + "%");
			}

			List<WorkOrderApprovalSettlementDto> listrequestDto =createQuery.setResultTransformer(Transformers.aliasToBean(WorkOrderApprovalSettlementDto.class)).list();
		return new Response<>(SUCCESS, SUCCESS_CODE, null, listrequestDto, null);
		} catch (HibernateException he) {
		logger.error("getRequestSettledList failed", he);
		throw new ServiceException(he.getMessage());
		} catch (Exception e) {
		logger.error("getRequestSettledList failed", e);
		throw new ServiceException(e.getMessage());
		} finally {

		}
	}


	@Override
	public Response getRequestSettledListDetails(WorkOrderApprovalSettlementDto requestDto)
			throws ApplicationException {
try {
			String query= "select " + 
					"	details.workorder_settlement_id as \"workorderSettlementId\" , " + 
					"	details.workorder_request_id as \"workorderRequestId\", " + 
					"	storetab.store_name as \"storeName\" , " + 
					"	vendortab.name as \"vendorName\", " + 
					"	details.paid_amount as \"paidAmount\", " + 
					"	request.workorder_request_number as \"workorderRequestNumber\" " + 
					"from " + 
					"	procurement.t_new_work_order_settlement_details details " + 
					"inner join procurement.t_new_work_order_creation request on " + 
					"	details.workorder_request_id = request.workorder_request_id " + 
					"left join public.m_store storetab on " + 
					"	request.store_id = storetab.store_id " + 
					"left join public.m_vendor vendortab on " + 
					"	request.vendor_id = vendortab.id " + 
					"where " + 
					"	details.workorder_settlement_id =:workorderSettlementId ";
				
		List<WorkOrderApprovalSettlementDto> listrequestDto =sessionFactory.getCurrentSession().createSQLQuery(query)
				.setParameter("workorderSettlementId", requestDto.getWorkorderSettlementId())
				.setResultTransformer(Transformers.aliasToBean(WorkOrderApprovalSettlementDto.class)).list();
		return new Response<>(SUCCESS, SUCCESS_CODE, null, listrequestDto, null);
		} catch (HibernateException he) {
		logger.error("getRequestSettledListDetails failed", he);
		throw new ServiceException(he.getMessage());
		} catch (Exception e) {
		logger.error("getRequestSettledListDetails failed", e);
		throw new ServiceException(e.getMessage());
		} finally {

		}
	}


	@Override
	public Response saveCreationCompletionDetails(WorkOrderCreationApprovalDto apptovalDto) {
		try {
            NewWorkOrderCompletion completion=new NewWorkOrderCompletion();
		    completion.setWorkorderRequestId(apptovalDto.getWorkorderRequestId());
		    completion.setWorkorderRequestApprovalId(apptovalDto.getWorkorderRequestApprovalId());
		    completion.setWorkorderRequestNumber(apptovalDto.getWorkorderRequestNumber());
		    completion.setStoreId(apptovalDto.getStoreId());
		    completion.setVendorId(apptovalDto.getVendorId());
//		    completion.setDeliveryDate(GlobalCommonDateUtils.getDate(apptovalDto.getDeliveryDate(), "dd-M-yyyy HH:mm:ss") );
		    completion.setBillTypeId(apptovalDto.getBillTypeId());
		    completion.setContractTypeId(apptovalDto.getContractTypeId());
		    completion.setAgainstTypeId(apptovalDto.getAgainstTypeId());
		    completion.setWorkorderRequestDate(TimeZoneComponent.getDateByzone(apptovalDto.getUnitId()));
		    completion.setTotalAmount(apptovalDto.getTotalAmount());
		    completion.setTotalDiscountAmount(apptovalDto.getTotalDiscountAmount());
		    completion.setTotalTaxAmount(apptovalDto.getTotalTaxAmount());
		    completion.setOtherChargeAmount(apptovalDto.getOtherChargeAmount());
		    completion.setTotalNetAmount(apptovalDto.getTotalNetAmount());
		    completion.setPaidAmount(0.0);
		    completion.setPaymentStatusId(1);
		    completion.setBalanceAmount(apptovalDto.getTotalNetAmount());
		    completion.setOrgId(apptovalDto.getOrgId());
		    completion.setUnitId(apptovalDto.getUnitId());
		    completion.setCreatedBy(apptovalDto.getCreatedBy());
		    completion.setCreatedDate(TimeZoneComponent.getDateByzone(apptovalDto.getUnitId()));
		    completion.setUpdatedBy(apptovalDto.getCreatedBy());
		    completion.setUpdatedDate(TimeZoneComponent.getDateByzone(apptovalDto.getUnitId()));
		    completion.setStatus("A");
		    completion.setApprovalRemark(apptovalDto.getApprovalRemark());
		    completion.setInvioceDate(apptovalDto.getInvoiceDate());
		    completion.setInvioceNumber(apptovalDto.getInvioceNumber());
		    completion.setIsReverseTax(apptovalDto.getIsReverseTax());
		    completion.setDueDate(apptovalDto.getDueDate());
		    if(apptovalDto.getWorkOrderCompletionId()!=null)
		    {
		    	completion.setWorkOrderCompletionId(apptovalDto.getWorkOrderCompletionId());
		    }
		    sessionFactory.getCurrentSession().saveOrUpdate(completion);
		    if(completion.getWorkOrderCompletionId()!=null)
		    {
		    sessionFactory.getCurrentSession().createSQLQuery("delete from procurement.t_new_work_order_completion_details "
		    		+ " where work_order_completion_id="+completion.getWorkOrderCompletionId()+"").executeUpdate();
		    }
		    for (WorkOrderCreationRequestApprovalDetailsDto detailsdto: apptovalDto.getRequestApprovalDetailsDto()) {
		    	Integer count=0;
		    	for (NewWorkOrderCreationStagedDetailsMapper stagedMapper : detailsdto.getWorkOrderCreationStagedListData()) {
		    		if(stagedMapper.getPercentageAmountPay()!=null && stagedMapper.getPercentageAmountPay()>0)
		    		{
		    			count++;
		    		}
		    	}
		    	if(count>0)
		    	{
		    	newWorkOrderCompletionDetails details = new  newWorkOrderCompletionDetails();
		    	details.setWorkOrderCompletionId(completion.getWorkOrderCompletionId());
		    	details.setWorkorderRequestApprovalDetialsId(detailsdto.getWorkorderRequestApprovalDetialsId());
	    		details.setWorkorderRequestDetialsId(detailsdto.getWorkorderRequestDetialsId());		
	    		details.setWorkorderRequestId(apptovalDto.getWorkorderRequestId());
	    		if(detailsdto.getItemId()!=null) 
	    			     details.setItemId(detailsdto.getItemId());
	    		
	    		details.setStoreId(detailsdto.getStoreId());
	    		details.setItemName(detailsdto.getItemName());
	    		details.setItemQuantity(detailsdto.getItemQuantity());
	    		details.setItemRate(detailsdto.getItemRate());
	    		details.setItemMrp(detailsdto.getItemMrp());
	    		if(detailsdto.getItemDiscPercentage()!=null) 
	    			details.setItemDiscPercentage(detailsdto.getItemDiscPercentage());
	    		if(detailsdto.getItemDiscAmount()!=null)  
	    			details.setItemDiscAmount(detailsdto.getItemDiscAmount());
	    		if(detailsdto.getItemTaxId()!=null)  	
	    			details.setItemTaxId(detailsdto.getItemTaxId());
	    		if(detailsdto.getItemTax()!=null)  
	    			details.setItemTax(detailsdto.getItemTax());
	    		if(detailsdto.getItemTaxAmount()!=null)
	    			details.setItemTaxAmount(detailsdto.getItemTaxAmount());
	    		details.setItemNetAmount(detailsdto.getItemNetAmount());
	    		details.setOrgId(apptovalDto.getOrgId());
	    		details.setUnitId(apptovalDto.getUnitId());
	    		details.setCreatedBy(apptovalDto.getCreatedBy());
	    		details.setUpdatedDate(TimeZoneComponent.getDateByzone(apptovalDto.getUnitId()));
	    		details.setUpdatedBy(apptovalDto.getCreatedBy());
	    		details.setStatus("A");
	    		details.setItemRemark(detailsdto.getItemRemark());
	    		details.setCreatedDate(TimeZoneComponent.getDateByzone(apptovalDto.getUnitId()));
	    		details.setApprovedQty(detailsdto.getApprovedQty());
	    		details.setRejectedQty(detailsdto.getRejectedQty());
	    		details.setApprovalDetailRemark(detailsdto.getApprovalDetailRemark());
	    		details.setIsRejectedQty(detailsdto.getIsRejectedQty());
	    		if(detailsdto.getIsSelected()!=null) {
	    			details.setIsSelected(detailsdto.getIsSelected());
	    		}else{
	    			details.setIsSelected(false);
	    		}
	    					if(detailsdto.getIsRejectedQty()==true) {
	    						if(detailsdto.getItemRejectionObject().getWorkorderRequestDetialsId()!=null) {
	    							NewWorkOrderItemRejectionDetails rejectItem=new NewWorkOrderItemRejectionDetails();
		    						rejectItem.setWorkorderRequestId(apptovalDto.getWorkorderRequestId());
		    						rejectItem.setWorkorderRequestDetialsId(detailsdto.getItemRejectionObject().getWorkorderRequestDetialsId());
		    						rejectItem.setCancelReasonId(detailsdto.getItemRejectionObject().getCancelReasonId());
		    						rejectItem.setRejectNote(detailsdto.getItemRejectionObject().getRejectNote());
		    						rejectItem.setCreatedBy(apptovalDto.getCreatedBy());
		    						rejectItem.setCreatedDate(new  Date());
		    						rejectItem.setStatus("A");
		    						sessionFactory.getCurrentSession().save(rejectItem);
	    						}
	    					}
	                        
	    		sessionFactory.getCurrentSession().save(details);
		    }
	    	}
		    if(apptovalDto.getRequestApprovalDetailsDto().size()>0){
		    	Session	opensession=sessionFactory.getCurrentSession();
		    	Double TotalPercentageToPay=0.00;
		    	Double TotalPercentagePaid=0.00;
		    	String hql2 = "SELECT cast(COALESCE(sum(percentage_amount),0) as float) from procurement.t_new_work_order_creation_staged_details "
	    				+ " where  workorder_request_id=:id ";
	    		Query query3 = sessionFactory.getCurrentSession().createSQLQuery(hql2);
	    		query3.setParameter("id", apptovalDto.getWorkorderRequestId());
	    		query3.setMaxResults(1);
                Double totalPercentage=(Double) query3.uniqueResult();
                TotalPercentageToPay=totalPercentage;
                
                String hql4 = "SELECT cast(COALESCE(sum(percentage_amount_paid),0) as float) from procurement.t_new_work_order_creation_staged_details "
	    				+ " where  workorder_request_id=:id ";
	    		Query query4 = sessionFactory.getCurrentSession().createSQLQuery(hql4);
	    		query4.setParameter("id", apptovalDto.getWorkorderRequestId());
	    		query4.setMaxResults(1);
                Double totalPercentagePaidTillNow=(Double) query4.uniqueResult();
                
		    	for (WorkOrderCreationRequestApprovalDetailsDto detailsdto: apptovalDto.getRequestApprovalDetailsDto()) {
		                        if(detailsdto.getWorkOrderCreationStagedListData().size()>0)
		                        {
		                        for (NewWorkOrderCreationStagedDetailsMapper stagedMapper : detailsdto.getWorkOrderCreationStagedListData()) {
//		                        	TotalPercentageToPay=TotalPercentageToPay+stagedMapper.getPercentage();
		                        	TotalPercentagePaid=TotalPercentagePaid+((stagedMapper.getPercentageAmountPay()!=null ? stagedMapper.getPercentageAmountPay():0));
		                            NewWorkOrderCreationStagedDetailsMapper stagedDetails = new NewWorkOrderCreationStagedDetailsMapper();
		                            stagedDetails.setStagedWorkorderId(stagedMapper.getStagedWorkorderId());
		                            Double prePaid=0.00;
		                            Double prePay=0.00;
		                            if(apptovalDto.getWorkOrderCompletionId()!=null)
		                		    {
		                            String hql = "SELECT COALESCE(percentage_amount_pay,0) from procurement.t_new_work_order_creation_staged_details "
		            	    				+ " where  staged_workorder_id=:id order by 1 desc ";
		            	    		Query query = sessionFactory.getCurrentSession().createSQLQuery(hql);
		            	    		query.setParameter("id", stagedMapper.getStagedWorkorderId());
		                             prePay=(Double) query.uniqueResult();
		                            
		                            String hql1 = "SELECT COALESCE(percentage_amount_paid,0) from procurement.t_new_work_order_creation_staged_details "
		            	    				+ " where  staged_workorder_id=:id order by 1 desc ";
		            	    		Query query1 = sessionFactory.getCurrentSession().createSQLQuery(hql);
		            	    		query1.setParameter("id", stagedMapper.getStagedWorkorderId());
		                             prePaid=(Double) query1.uniqueResult();
		                		    }
		                            stagedDetails.setPercentageAmountPaid(((stagedMapper.getPercentageAmountPay()!=null ? stagedMapper.getPercentageAmountPay():0)));
		                            stagedDetails.setPercentageAmountPay(stagedMapper.getPercentageAmountPay());
		                            String sql="update procurement.t_new_work_order_creation_staged_details set percentage_amount_paid=COALESCE(percentage_amount_paid,0)+"+(stagedDetails.getPercentageAmountPaid()-prePay)+", "
		                           		+ " percentage_amount_pay="+stagedDetails.getPercentageAmountPay()+" where staged_workorder_id="+stagedDetails.getStagedWorkorderId()+" ";
		                            sessionFactory.getCurrentSession().createSQLQuery(sql).executeUpdate();
		                        }
		                        }
		                        if(detailsdto.getWorkOrderCompletionStagedListData().size()>0)
					    		{
								for (NewWorkOrderCompletionStagedDetailsMapper stagedMapper : detailsdto.getWorkOrderCompletionStagedListData()) {
									NewWorkOrderCompletionStagedDetailsMapper stagedDetails = new NewWorkOrderCompletionStagedDetailsMapper();
				                    stagedDetails.setOrganizationId(apptovalDto.getOrgId());
				                    stagedDetails.setUnitId(apptovalDto.getUnitId());
				                    stagedDetails.setUpdatedDate(TimeZoneComponent.getDateByzone(apptovalDto.getUnitId()));
				                    stagedDetails.setUpdatedByUser(apptovalDto.getUpdatedBy());
				                    stagedDetails.setAddedByUser(apptovalDto.getCreatedBy());
				                    stagedDetails.setAddedDate(TimeZoneComponent.getDateByzone(apptovalDto.getUnitId()));
				                    stagedDetails.setPercentage(stagedMapper.getPercentage());
				                    stagedDetails.setStageName(stagedMapper.getStageName());
				                    stagedDetails.setWorkorderRequestId(completion.getWorkorderRequestId());
				                    stagedDetails.setPercentageAmountPaid((stagedMapper.getPercentageAmountPay()!=null ? stagedMapper.getPercentageAmountPay():0));
				                    stagedDetails.setPercentageAmountPay(stagedMapper.getPercentageAmountPay());
				                    stagedDetails.setWorkOrderCompletionId(completion.getWorkOrderCompletionId());
				                    stagedDetails.setStagedWorkorderId(stagedMapper.getStagedWorkorderId());
				                    stagedDetails.setWorkorderRequestDetailsId(stagedMapper.getWorkorderRequestDetailsId());
				                    stagedDetails.setWorkOrderCompletionStagedDetailId(stagedMapper.getWorkOrderCompletionStagedDetailId());
				                    sessionFactory.getCurrentSession().saveOrUpdate(stagedDetails);
				                }
					    	    }
		    		
		    	}
		    	sessionFactory.getCurrentSession().createSQLQuery(" UPDATE procurement.t_new_work_order_creation SET workorder_completion_status_id="+apptovalDto.getCompletionStatusId()+" "
			    		+ " WHERE workorder_request_id ="+apptovalDto.getWorkorderRequestId())
						.executeUpdate();
		    	TotalPercentagePaid=TotalPercentagePaid+(totalPercentagePaidTillNow!=null ? totalPercentagePaidTillNow:0);
                if(TotalPercentageToPay.equals(TotalPercentagePaid.floatValue()) || TotalPercentageToPay.floatValue()==TotalPercentagePaid.floatValue())
		    	{
		    		sessionFactory.getCurrentSession().createSQLQuery(" UPDATE procurement.t_new_work_order_approval SET work_order_completion=true "
		    		+ " WHERE workorder_request_id ="+apptovalDto.getWorkorderRequestId())
					.executeUpdate();
		    		
		    		sessionFactory.getCurrentSession().createSQLQuery(" UPDATE procurement.t_new_work_order_approval_details SET work_order_completion=true "
				    		+ " WHERE workorder_request_id ="+apptovalDto.getWorkorderRequestId())
							.executeUpdate();
		    		
		    		sessionFactory.getCurrentSession().createSQLQuery(" UPDATE procurement.t_new_work_order_creation SET work_order_completion=true "
				    		+ " WHERE workorder_request_id ="+apptovalDto.getWorkorderRequestId())
							.executeUpdate();
		    	}
		    	else
		    	{
		    		sessionFactory.getCurrentSession().createSQLQuery(" UPDATE procurement.t_new_work_order_approval SET work_order_completion=false "
				    		+ " WHERE workorder_request_id ="+apptovalDto.getWorkorderRequestId())
							.executeUpdate();
				    		
				    		sessionFactory.getCurrentSession().createSQLQuery(" UPDATE procurement.t_new_work_order_approval_details SET work_order_completion=false "
						    		+ " WHERE workorder_request_id ="+apptovalDto.getWorkorderRequestId())
									.executeUpdate();
				    		
				    		sessionFactory.getCurrentSession().createSQLQuery(" UPDATE procurement.t_new_work_order_creation SET work_order_completion=false "
						    		+ " WHERE workorder_request_id ="+apptovalDto.getWorkorderRequestId())
									.executeUpdate();
		    	}
		    	opensession.close();
		    }
		    
		    
		    
		    
		    
		
		
		 return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);

	} catch (HibernateException he) {
		logger.error("saveCreationCompletionDetails failed", he);
		throw new ServiceException(he.getMessage());
		  } catch (Exception e) {
		logger.error("saveCreationCompletionDetails failed", e);
		throw new ServiceException(e.getMessage());
		  } finally {

		   }
	}


	@Override
	public Response getCreationApprovalDetailsbyIdEdit(WorkOrderCreationApprovalDto apptovalDto) {
		try {
//			NewWorkOrderRequestApproval
			 
			List<NewWorkOrderRequestApproval> list=sessionFactory.getCurrentSession().createQuery("from NewWorkOrderRequestApproval approval where approval.workorderRequestId="+apptovalDto.getWorkorderRequestId()).list();
			if(list.size()>0) {
				//approval  saved 
				WorkOrderCreationApprovalDto approvaldetails = (WorkOrderCreationApprovalDto) sessionFactory
						.getCurrentSession().getNamedQuery("GET_WO_REQUEST_APPROVAl_BY_REQUEST_ID")
						.setParameter("workorderRequestId", apptovalDto.getWorkorderRequestId())
						.setResultTransformer(Transformers.aliasToBean(WorkOrderCreationApprovalDto.class)).uniqueResult();
				
				List<WorkOrderCreationRequestApprovalDetailsDto> detaillist = sessionFactory.getCurrentSession()
						.getNamedQuery("GET_WO_REQUEST_APPROVAL_DETAILS_BY_REQUEST_ID_EDIT")
						.setParameter("workorderRequestId", apptovalDto.getWorkorderRequestId())
//						.setParameter("workOrderCompletionId", apptovalDto.getWorkOrderCompletionId())
						
						.setResultTransformer(Transformers.aliasToBean(WorkOrderCreationRequestApprovalDetailsDto.class)).list();
				

				List<WorkOrderCreationOtherChargeDetailsDto> otherchargedetaillist = sessionFactory.getCurrentSession()
						.getNamedQuery("GET_WO_REQUEST_OTHERCHARGE_DETAILS_BY_REQUEST_ID")
						.setParameter("workorderRequestId", apptovalDto.getWorkorderRequestId())
						.setResultTransformer(Transformers.aliasToBean(WorkOrderCreationOtherChargeDetailsDto.class))
						.list();

				List<WorkOrderCreationRequestDocumentDto> documentdetaillist = sessionFactory.getCurrentSession()
						.getNamedQuery("GET_WO_REQUEST_DOCUMENT_DETAILS_BY_REQUEST_ID")
						.setParameter("workorderRequestId", apptovalDto.getWorkorderRequestId())
						.setResultTransformer(Transformers.aliasToBean(WorkOrderCreationRequestDocumentDto.class)).list();
	            for (WorkOrderCreationRequestApprovalDetailsDto workOrderCreationRequestDetailsDto : detaillist) {
	                String hql = "SELECT newWorkOrderCompletionStagedDetailsMapper FROM NewWorkOrderCompletionStagedDetailsMapper AS newWorkOrderCompletionStagedDetailsMapper"
	                		+ " where workOrderCompletionId=:WODetailId and workorderRequestDetailsId=:workorderRequestDetailsId ";

	                Query query = sessionFactory.getCurrentSession().createQuery(hql);
	                query.setParameter("WODetailId", workOrderCreationRequestDetailsDto.getWorkOrderCompletionId());
	                query.setParameter("workorderRequestDetailsId", workOrderCreationRequestDetailsDto.getWorkorderRequestDetialsId());
	                List<NewWorkOrderCompletionStagedDetailsMapper> getList= query.list();
	                List<NewWorkOrderCompletionStagedDetailsMapper> getList3=new ArrayList<NewWorkOrderCompletionStagedDetailsMapper>();
	                for (NewWorkOrderCompletionStagedDetailsMapper getList1 : getList) {
	                	String hql1 = "SELECT cast(SUM(percentage_amount_paid) as float) from procurement.t_new_work_order_completion_staged_details "
	    	    				+ " where  staged_workorder_id=:id ";
	    	    		Query query1 = sessionFactory.getCurrentSession().createSQLQuery(hql1);
	    	    		query1.setParameter("id", getList1.getStagedWorkorderId());
	                    Double totalPaid=(Double) query1.uniqueResult();
	                	NewWorkOrderCompletionStagedDetailsMapper getList2=new NewWorkOrderCompletionStagedDetailsMapper();
	                    getList2.setWorkOrderCompletionStagedDetailId(getList1.getWorkOrderCompletionStagedDetailId());
	                    getList2.setStageName(getList1.getStageName());
	                    getList2.setPercentage(getList1.getPercentage());
	                    getList2.setWorkorderRequestId(apptovalDto.getWorkorderRequestId());
	                    getList2.setPercentageAmountPaid(totalPaid-getList1.getPercentageAmountPaid());
	                    getList2.setPercentageAmountPay(getList1.getPercentageAmountPay());
	                    getList2.setStagedWorkorderId(getList1.getStagedWorkorderId());
	                    getList2.setWorkorderRequestDetailsId(getList1.getWorkorderRequestDetailsId());
	                    getList3.add(getList2);
	                }
	                workOrderCreationRequestDetailsDto.setWorkOrderCompletionStagedListData(getList3);
	            }
	            String hql = "SELECT work_order_completion_id from procurement.t_new_work_order_completion "
	    				+ " where  workorder_request_id=:id and is_settled=false order by 1 desc ";
	    		Query query = sessionFactory.getCurrentSession().createSQLQuery(hql);
	    		query.setParameter("id", apptovalDto.getWorkorderRequestId());
                Integer completionId=(Integer) query.uniqueResult();
                approvaldetails.setWorkOrderCompletionId(completionId);
				if(detaillist.size()>0) 
					approvaldetails.setRequestApprovalDetailsDto(detaillist);
				if(otherchargedetaillist.size()>0)
					approvaldetails.setRequestTaxDetailsDto(otherchargedetaillist);
				if(documentdetaillist.size()>0)
					approvaldetails.setRequestDocumentDto(documentdetaillist);
                String creditDaysSql=" select cast(credit_days as int) from public.m_vendor where id="+approvaldetails.getVendorId() ;
                Query creditDaysQuery = sessionFactory.getCurrentSession().createSQLQuery(creditDaysSql);
                Integer creditDays=(Integer) creditDaysQuery.uniqueResult();
                approvaldetails.setCreditDays(creditDays);
				
                String getPaidOtherChargesSql=" select cast(sum(other_charge_amount) as int) from procurement.t_new_work_order_completion where workorder_request_id="+apptovalDto.getWorkorderRequestId()+" and work_order_completion_id!="+apptovalDto.getWorkOrderCompletionId()+"" ;
                Query getPaidOtherChargesQuery = sessionFactory.getCurrentSession().createSQLQuery(getPaidOtherChargesSql);
                Integer getPaidOtherCharges=(Integer) getPaidOtherChargesQuery.uniqueResult();
                approvaldetails.setOtherChargeAmount(approvaldetails.getOtherChargeAmount()-(getPaidOtherCharges!=null ? getPaidOtherCharges:0));
                
                String getPaidOtherChargesSqlEdit=" select cast(sum(other_charge_amount) as float) from procurement.t_new_work_order_completion where workorder_request_id="+apptovalDto.getWorkorderRequestId()+" and work_order_completion_id="+apptovalDto.getWorkOrderCompletionId()+"" ;
                Query getPaidOtherChargesQueryEdit = sessionFactory.getCurrentSession().createSQLQuery(getPaidOtherChargesSqlEdit);
                Double getPaidOtherChargesEdit=(Double) getPaidOtherChargesQueryEdit.uniqueResult();
                approvaldetails.setOtherChargeAmountForEdit((getPaidOtherChargesEdit!=null ? getPaidOtherChargesEdit:0.00));
			   return new Response<>(SUCCESS, SUCCESS_CODE, "updatedata", null, approvaldetails);

			}

	   } catch (HibernateException he) {
		logger.error("getCreationApprovalDetailsbyIdEdit failed", he);
		throw new ServiceException(he.getMessage());
	  } catch (Exception e) {
		logger.error("getCreationApprovalDetailsbyIdEdit failed", e);
		throw new ServiceException(e.getMessage());
	  } finally {

	  }
	   return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}


	@Override
	public Response getCreationRequestListCompletion(WorkOrderCreationRequestDto requestDto) {
		try {
			String query= ""
					+ "select "
					+ "	request.workorder_request_id as \"workorderRequestId\", "
					+ "	request.workorder_request_number as \"workorderRequestNumber\", "
					+ "	request.bill_type_id as \"billTypeId\", "
					+ "	request.store_id as \"storeId\", "
					+ "	request.vendor_id as \"vendorId\", "
					+ "	to_char(request.delivery_date, 'dd/mm/yyyy') as \"deliveryDate\", "
					+ "	request.contract_type_id as \"contractTypeId\", "
					+ "	to_char(request.workorder_request_date , 'dd/mm/yyyy') as \"workorderRequestDate\", "
					+ "	request.total_amount as \"totalAmount\", "
					+ "   "
					+ " 	mum.user_name as \"approvedName\", "
					+ "	request.total_discount_amount as \"totalDiscountAmount\", "
					+ "	request.total_tax_amount as \"totalTaxAmount\", "
					+ "	request.other_charge_amount as \"otherChargeAmount\", "
					+ "	request.total_net_amount as \"totalNetAmount\", "
					+ "	request.workorder_request_status_id as \"workorderRequestStatusId\", "
					+ "	request.payment_terms as \"paymentTerms\", "
					+ "	request.guarantee_warranty as \"guaranteeWarranty\", "
					+ "	request.org_id as \"orgId\", "
					+ "	request.unit_id as \"unitId\", "
					+ "	request.created_by as \"createdBy\", "
					+ "	empmst.user_name as \"userName\", "
					+ "	vendortab.name as \"vendorName\", "
					+ "	storetab.store_name as \"storeName\", "
					+ "   agaisttypeMst.type as \"againstTypeName\" , request.workorder_request_status_id as \"statusId\", s.status as \"status\" , sapproval.id as  \"approvalStatusId\", sapproval.status as \"approvalStatus\" , "
					+ " request.work_order_completion as \"workOrderCompletion\", "
					+ " 	mct.description as \"contractType\", "
					 + " 	concat((case when tpr.is_vip = 'Y' then coalesce(tpr.alise_name, tpr.first_name) else concat(coalesce(tpr.first_name) , ' ', coalesce(tpr.middle_name) , ' ', coalesce(tpr.last_name)) end ), concat(to_char(extract(year from age(current_timestamp, tpr.dob)), '999'), 'Y', '/', mgm.gender_code)) as \"patientDetails\", " 
					 + " 	tpr.uhid_number as \"uhidNumber\", " 
					 + " 	concat(mbm.bill_number, ' | ', to_char(mbm.bill_date_time, 'DD/MM/YYYY')) as \"billDetails\", " 
					 + " 	tem.encounter_number as \"visitDetails\" " 
					+ " from "
					+ "	procurement.t_new_work_order_creation request "
					+ "left join public.m_vendor vendortab on "
					+ "	request.vendor_id = vendortab.id "
					+ "left join public.m_store storetab on "
					+ "	request.store_id = storetab.store_id "
					+ "left join  public.m_request_type agaisttypeMst on "
					+ " request.against_type_id = agaisttypeMst.id "
					+ " left join PUBLIC.m_user_master empmst on "
					+ " 	request.created_by = empmst.user_id "
					+ " left join doctor.m_doctor_master docmst on "
					+ " 	request.created_by = docmst.user_id "
					+ " left join PUBLIC.m_employee_master empmst1 on "
					+ " 	request.approved_by = empmst1.user_id "
					+ " left join	public.m_user_master mum  on "
					+ " mum.user_id =request.approved_by "
					+ " left join doctor.m_doctor_master docmst1 on "
					+ " 	request.approved_by = docmst1.user_id   left join public.m_status  s on request.workorder_request_status_id = s.id left join public.m_status  sapproval on request.workorder_completion_status_id = sapproval.id "
					+ " left join procurement.m_contract_type mct on " 
					 + " 	mct.id = request.contract_type_id " 
					 + " left join billing.m_billing_master mbm on " 
					 + " 	mbm.billing_master_id = request.bill_id " 
					 + " left join opd.t_encounter_master tem on " 
					 + " 	tem.encounter_id = request.encounter_id " 
					 + " left join adt.t_admission ta on " 
					 + " 	ta.admission_id = request.admission_id " 
					 + " left join patient.t_patient_registration tpr on " 
					 + " 	tpr.patient_id = " 
					 + " 	(case " 
					 + " 		when request.encounter_id is not null then tem.patient_id " 
					 + " 		else ta.patient_id " 
					 + " 	end) " 
					 + " left join m_gender_master mgm on " 
					 + " 	mgm.gender_id = tpr.gender_id "
					
					+ "where "
					+ "	(:storeId = 0 OR request.store_id = :storeId) "
					+ "	and request.org_id =:orgId "
					+ "	and request.unit_id =:unitId "
					+ "	and request.status = 'A'"
					+ " and request.workorder_completion_status_id is not null ";
//					+ " and request.workorder_request_status_id=8 "
//					+ "	and sapproval.id=5 ";
					
			StringBuilder buildquery = new StringBuilder(query);
			if(requestDto.getFromDate()!=null && requestDto.getFromDate()!="" && requestDto.getToDate()!=null && requestDto.getToDate()!="" ) {
//				buildquery.append(" and to_char(request.delivery_date, 'dd/mm/yyyy') between ")
				buildquery.append(" and to_char(request.workorder_request_date,'YYYY-MM-DD') between :fromDate and :toDate ");
			}
			if(requestDto.getBillTypeId()!=null  ) {
				buildquery.append(" and request.bill_type_id=:billTypeId ");
			}
			if(requestDto.getVendorId()!=null  ) {
				buildquery.append(" and request.vendor_id=:vendorId ");
			}
			if(requestDto.getWorkorderRequestNumber()!=null && requestDto.getWorkorderRequestNumber()!=""  ) {
				//buildquery.append(" and request.workorder_request_number like '%"+requestDto.getWorkorderRequestNumber()+"%' ");
				buildquery.append(" and request.workorder_request_number like :workorderRequestNumber ");
			}
			if(requestDto.getWorkorderRequestStatusId()!=null   ) {
				buildquery.append(" and request.workorder_completion_status_id=:workOrderRequestStatusId ");
			}
			buildquery.append(" order by request.workorder_request_id desc ");
			Query createQuery=sessionFactory.getCurrentSession().createSQLQuery(buildquery.toString()).setParameter("storeId",requestDto.getStoreId()!=null && requestDto.getStoreId() > 0 ? requestDto.getStoreId() : 0)
					.setParameter("orgId", requestDto.getOrgId())
					.setParameter("unitId", requestDto.getUnitId());



			if(requestDto.getFromDate()!=null && requestDto.getFromDate()!="" && requestDto.getToDate()!=null && requestDto.getToDate()!="" ) {
				createQuery.setParameter("fromDate", requestDto.getFromDate());
				createQuery.setParameter("toDate", requestDto.getToDate());
			}
			if(requestDto.getBillTypeId()!=null  ) {
				createQuery.setParameter("billTypeId", requestDto.getBillTypeId());
			}

			if(requestDto.getVendorId()!=null  ) {
				createQuery.setParameter("vendorId", requestDto.getVendorId());
			}

			if (requestDto.getWorkorderRequestNumber() != null && !requestDto.getWorkorderRequestNumber().isEmpty()) {
				createQuery.setParameter("workorderRequestNumber", "%" + requestDto.getWorkorderRequestNumber() + "%");
			}

			if(requestDto.getWorkorderRequestStatusId()!=null   ) {
				createQuery.setParameter("workOrderRequestStatusId", requestDto.getWorkorderRequestStatusId());

			}




			List<WorkOrderCreationRequestDto> listrequestDto =createQuery.setResultTransformer(Transformers.aliasToBean(WorkOrderCreationRequestDto.class)).list();
			for (WorkOrderCreationRequestDto workOrderCreationRequestDto : listrequestDto) {
				String hql = "SELECT work_order_completion_id from procurement.t_new_work_order_completion "
	    				+ " where  workorder_request_id=:id and is_settled=false order by 1 desc ";
	    		Query query1 = sessionFactory.getCurrentSession().createSQLQuery(hql);
	    		query1.setParameter("id", workOrderCreationRequestDto.getWorkorderRequestId());
	    		query1.setMaxResults(1);
	    		Integer completionId=(Integer) query1.uniqueResult();
	    		
//	    		String hql1 = "SELECT cast(count(*) as int) from procurement.t_new_work_order_completion "
//	    				+ " where  workorder_request_id=:id and is_settled=true order by 1 desc ";
//	    		Query query2 = sessionFactory.getCurrentSession().createSQLQuery(hql1);
//	    		query2.setParameter("id", workOrderCreationRequestDto.getWorkorderRequestId());
//	    		query2.setMaxResults(1);
//                Integer completionCount=(Integer) query2.uniqueResult();
                
//                String hql2 = "SELECT cast(COALESCE(sum(percentage_amount),0) as int) from procurement.t_new_work_order_creation_staged_details "
//	    				+ " where  workorder_request_id=:id ";
//	    		Query query3 = sessionFactory.getCurrentSession().createSQLQuery(hql2);
//	    		query3.setParameter("id", workOrderCreationRequestDto.getWorkorderRequestId());
//	    		query3.setMaxResults(1);
//                Integer totalPercentage=(Integer) query3.uniqueResult();
//                
//                String hql4 = "SELECT cast(COALESCE(sum(percentage_amount_paid),0) as int) from procurement.t_new_work_order_creation_staged_details "
//	    				+ " where  workorder_request_id=:id ";
//	    		Query query4 = sessionFactory.getCurrentSession().createSQLQuery(hql4);
//	    		query4.setParameter("id", workOrderCreationRequestDto.getWorkorderRequestId());
//	    		query4.setMaxResults(1);
//                Integer totalPercentagePaid=(Integer) query4.uniqueResult();
                
//                String hql5 = "SELECT cast(count(*) as int) from procurement.t_new_work_order_completion "
//	    				+ " where  workorder_request_id=:id and is_settled=false order by 1 desc";
//	    		Query query5 = sessionFactory.getCurrentSession().createSQLQuery(hql5);
//	    		query5.setParameter("id", workOrderCreationRequestDto.getWorkorderRequestId());
//	    		query5.setMaxResults(1);
//                Integer isSettled=(Integer) query5.uniqueResult();
//                if(isSettled>0)
//                {
//                	workOrderCreationRequestDto.setIsSettled(true);
//                }
//                else
//                {
//                	workOrderCreationRequestDto.setIsSettled(false);
//                }
                workOrderCreationRequestDto.setWorkOrderCompletionId(completionId);
//                workOrderCreationRequestDto.setCompletionCount(completionCount);
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listrequestDto, null);

		} catch (HibernateException he) {
			logger.error("getCreationRequestListCompletion failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getCreationRequestListCompletion failed", e);
			throw new ServiceException(e.getMessage());
		} finally {

		}
	}


	@Override
	public Response requestChangeToApprovedCompletion(WorkOrderCreationRequestDto requestDto) {
		try {
			if(requestDto.getWorkOrderRequestList().size()>0){
				for (WorkOrderCreationRequestDto dto:requestDto.getWorkOrderRequestList()) {
					String hql3 = "SELECT workorder_request_details_id from procurement.t_new_work_order_approval_details "
		    				+ " where  workorder_request_id=:id and is_selected=true ";
		    		Query query5 = sessionFactory.getCurrentSession().createSQLQuery(hql3);
		    		query5.setParameter("id", dto.getWorkorderRequestId());
		    		List<Integer> detailsIds=(List<Integer>) query5.list();
					if(dto.getWorkOrderCompletionId()!=null)
					{
						sessionFactory.getCurrentSession().createSQLQuery(" UPDATE procurement.t_new_work_order_completion SET is_settled=true "
				        		+ " WHERE work_order_completion_id ="+dto.getWorkOrderCompletionId())
								.executeUpdate();
					}
					String hql2 = "SELECT cast(COALESCE(sum(percentage_amount),0) as float) from procurement.t_new_work_order_creation_staged_details "
		    				+ " where  workorder_request_id=:id and workorder_request_details_id in (:detailsId) ";
		    		Query query3 = sessionFactory.getCurrentSession().createSQLQuery(hql2);
		    		query3.setParameter("id", dto.getWorkorderRequestId());
		    		query3.setParameterList("detailsId", detailsIds);
		    		query3.setMaxResults(1);
	                Double totalPercentage=(Double) query3.uniqueResult();
	                
	                String hql4 = "SELECT cast(COALESCE(sum(percentage_amount_paid),0) as float) from procurement.t_new_work_order_creation_staged_details "
		    				+ " where  workorder_request_id=:id and workorder_request_details_id in (:detailsId) ";
		    		Query query4 = sessionFactory.getCurrentSession().createSQLQuery(hql4);
		    		query4.setParameter("id", dto.getWorkorderRequestId());
		    		query4.setParameterList("detailsId", detailsIds);
		    		query4.setMaxResults(1);
	                Double totalPercentagePaid=(Double) query4.uniqueResult();
                    if(totalPercentagePaid.equals(totalPercentage) || totalPercentagePaid.floatValue()==totalPercentage.floatValue())
	                {
	                	sessionFactory.getCurrentSession().createSQLQuery(" UPDATE procurement.t_new_work_order_creation SET workorder_completion_status_id=8 "
	    			    		+ " WHERE workorder_request_id ="+dto.getWorkorderRequestId())
	    						.executeUpdate();
	                }
	                else
	                {
	                	sessionFactory.getCurrentSession().createSQLQuery(" UPDATE procurement.t_new_work_order_creation SET workorder_completion_status_id=18 "
	    			    		+ " WHERE workorder_request_id ="+dto.getWorkorderRequestId())
	    						.executeUpdate();
	                }
				}
				   
				  return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
			}
			return new Response<>(ERROR, ERROR_CODE, null, null, null);
	  } catch (HibernateException he) {
		logger.error("requestChangeToApprovedCompletion failed", he);
		throw new ServiceException(he.getMessage());
	  } catch (Exception e) {
		logger.error("requestChangeToApprovedCompletion failed", e);
		throw new ServiceException(e.getMessage());
	  } finally {

	   }
	}


	@Override
	public Response getWOCompletedTransaction(WorkOrderCreationApprovalDto requestDto) {
		try {
			//approval list
				String query= "select " + 
						"	request.workorder_request_id as \"workorderRequestId\" , " + 
						"	approval.workorder_request_approval_id as \"workorderRequestApprovalId\", " + 
						"	request.workorder_request_number as \"workorderRequestNumber\" , " + 
						"	request.store_id as \"storeId\" , " + 
						"	request.vendor_id as \"vendorId\" , " + 
						"	to_char(request.workorder_request_date , 'dd/mm/yyyy') as \"workorderRequestDate\" , " + 
						"	approval.paid_amount as \"paidAmount\", " + 
						"	approval.balance_amount as \"balanceAmount\","+ 
						"   approval.payment_status_id as \"paymentStatusId\", " + 
						"	approval.total_net_amount as \"totalNetAmount\" , " + 
						"	request.workorder_request_status_id as \"workorderRequestStatusId\" , " + 
						"	request.org_id as \"orgId\" , " + 
						"	request.unit_id as \"unitId\" , " + 
						"	request.created_by as \"createdBy\" , " + 
						"	vendortab.name as \"vendorName\" , " + 
						"	storetab.store_name as \"storeName\" , " + 
						"	approval.work_order_completion_id as \"workOrderCompletionId\", " + 
						"	approval.invioce_number as \"invioceNumber\", " + 
						"	approval.invioce_date as \"invoiceDate\", " + 
						"	approval.created_date as \"completionDate\", " +
						"	approval.due_date as \"dueDate\" " +
						"from " + 
						"	procurement.t_new_work_order_creation request " + 
						"inner join procurement.t_new_work_order_completion approval on " + 
						"	request.workorder_request_id = approval.workorder_request_id " + 
						"left join public.m_vendor vendortab on " + 
						"	request.vendor_id = vendortab.id " + 
						"left join public.m_store storetab on " + 
						"	request.store_id = storetab.store_id " + 
						"left join public.m_request_type agaisttypeMst on " + 
						"	request.against_type_id = agaisttypeMst.id " + 
						"left join PUBLIC.m_employee_master empmst on " + 
						"	request.created_by = empmst.user_id " + 
						"left join doctor.m_doctor_master docmst on " + 
						"	request.created_by = docmst.user_id " + 
						"left join PUBLIC.m_employee_master empmst1 on " + 
						"	request.approved_by = empmst1.user_id " + 
						"left join doctor.m_doctor_master docmst1 on " + 
						"	request.approved_by = docmst1.user_id " + 
						" where " + 
					    "	request.store_id =:storeId " + 
					    "	and request.org_id =:orgId " + 
					    "	and request.unit_id =:unitId " + 
					    "	and request.status = 'A' "+
					    "  and approval.workorder_request_id=:workOrderRequestId";
					    
					
			StringBuilder buildquery = new StringBuilder(query);
			buildquery.append(" order by approval.work_order_completion_id ASC ");
			List<WorkOrderCreationRequestDto> listrequestDto =sessionFactory.getCurrentSession().createSQLQuery(buildquery.toString())
					.setParameter("storeId", requestDto.getStoreId())
					.setParameter("orgId", requestDto.getOrgId())
					.setParameter("unitId", requestDto.getUnitId())
					.setParameter("workOrderRequestId", requestDto.getWorkorderRequestId())
					.setResultTransformer(Transformers.aliasToBean(WorkOrderCreationApprovalDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listrequestDto, null);

		} catch (HibernateException he) {
			logger.error("getWOCompletedTransaction failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getWOCompletedTransaction failed", e);
			throw new ServiceException(e.getMessage());
		} finally {

		}
	}


	@Override
	public Response getCreationApprovalDetailsbyIdSettlement(WorkOrderCreationApprovalDto apptovalDto) {
		try {
//			NewWorkOrderRequestApproval
			 
			List<NewWorkOrderRequestApproval> list=sessionFactory.getCurrentSession().createQuery("from NewWorkOrderRequestApproval approval where approval.workorderRequestId="+apptovalDto.getWorkorderRequestId()).list();
			if(list.size()>0) {
				//approval  saved 
				WorkOrderCreationApprovalDto approvaldetails = (WorkOrderCreationApprovalDto) sessionFactory
						.getCurrentSession().getNamedQuery("GET_WO_REQUEST_APPROVAl_BY_REQUEST_ID")
						.setParameter("workorderRequestId", apptovalDto.getWorkorderRequestId())
						.setResultTransformer(Transformers.aliasToBean(WorkOrderCreationApprovalDto.class)).uniqueResult();
				
				List<WorkOrderCreationRequestApprovalDetailsDto> detaillist = sessionFactory.getCurrentSession()
						.getNamedQuery("GET_WO_REQUEST_APPROVAL_DETAILS_BY_REQUEST_ID_SETTLEMENT")
						.setParameter("workorderRequestId", apptovalDto.getWorkorderRequestId())
						.setParameter("workOrderCompletionId", apptovalDto.getWorkOrderCompletionId())
						.setResultTransformer(Transformers.aliasToBean(WorkOrderCreationRequestApprovalDetailsDto.class)).list();
				

				List<WorkOrderCreationOtherChargeDetailsDto> otherchargedetaillist = sessionFactory.getCurrentSession()
						.getNamedQuery("GET_WO_REQUEST_OTHERCHARGE_DETAILS_BY_REQUEST_ID")
						.setParameter("workorderRequestId", apptovalDto.getWorkorderRequestId())
						.setResultTransformer(Transformers.aliasToBean(WorkOrderCreationOtherChargeDetailsDto.class))
						.list();

				List<WorkOrderCreationRequestDocumentDto> documentdetaillist = sessionFactory.getCurrentSession()
						.getNamedQuery("GET_WO_REQUEST_DOCUMENT_DETAILS_BY_REQUEST_ID")
						.setParameter("workorderRequestId", apptovalDto.getWorkorderRequestId())
						.setResultTransformer(Transformers.aliasToBean(WorkOrderCreationRequestDocumentDto.class)).list();
	            for (WorkOrderCreationRequestApprovalDetailsDto workOrderCreationRequestDetailsDto : detaillist) {
	                String hql = "SELECT newWorkOrderCreationStagedDetailsMapper FROM NewWorkOrderCreationStagedDetailsMapper AS newWorkOrderCreationStagedDetailsMapper where workorderRequestDetailsId=:WODetailId order by newWorkOrderCreationStagedDetailsMapper.stagedWorkorderId asc ";

	                Query query = sessionFactory.getCurrentSession().createQuery(hql);
	                query.setParameter("WODetailId", workOrderCreationRequestDetailsDto.getWorkorderRequestDetialsId());
	                List<NewWorkOrderCreationStagedDetailsMapper> getList= query.list();
	                List<NewWorkOrderCreationStagedDetailsMapper> getList3=new ArrayList<NewWorkOrderCreationStagedDetailsMapper>();
	                for (NewWorkOrderCreationStagedDetailsMapper getList1 : getList) {
	                    NewWorkOrderCreationStagedDetailsMapper getList2=new NewWorkOrderCreationStagedDetailsMapper();
	                    getList2.setStagedWorkorderId(getList1.getStagedWorkorderId());
	                    getList2.setStageName(getList1.getStageName());
	                    getList2.setPercentage(getList1.getPercentage());
	                    getList2.setWorkorderRequestId(apptovalDto.getWorkorderRequestId());
	                    getList2.setPercentageAmountPaid(getList1.getPercentageAmountPaid());
	                    getList2.setPercentageAmountPay(getList1.getPercentageAmountPay());
	                    getList2.setWorkorderRequestDetailsId(getList1.getWorkorderRequestDetailsId());
	                    getList3.add(getList2);
	                }
	                workOrderCreationRequestDetailsDto.setWorkOrderCreationStagedListData(getList3);
	            }
	            String hql = "SELECT work_order_completion_id from procurement.t_new_work_order_completion "
	    				+ " where  workorder_request_id=:id and is_settled=true order by 1 desc ";
	    		Query query = sessionFactory.getCurrentSession().createSQLQuery(hql);
	    		query.setParameter("id", apptovalDto.getWorkorderRequestId());
	    		query.setMaxResults(1);
                Integer completionId=(Integer) query.uniqueResult();
                approvaldetails.setWorkOrderCompletionId(completionId);
				if(detaillist.size()>0) 
					approvaldetails.setRequestApprovalDetailsDto(detaillist);
				if(otherchargedetaillist.size()>0)
					approvaldetails.setRequestTaxDetailsDto(otherchargedetaillist);
				if(documentdetaillist.size()>0)
					approvaldetails.setRequestDocumentDto(documentdetaillist);
				
                String getPaidOtherChargesSqlEdit=" select cast(sum(other_charge_amount) as float) from procurement.t_new_work_order_completion where workorder_request_id="+apptovalDto.getWorkorderRequestId()+" and work_order_completion_id="+apptovalDto.getWorkOrderCompletionId()+"" ;
                Query getPaidOtherChargesQueryEdit = sessionFactory.getCurrentSession().createSQLQuery(getPaidOtherChargesSqlEdit);
                Double getPaidOtherChargesEdit=(Double) getPaidOtherChargesQueryEdit.uniqueResult();
                approvaldetails.setOtherChargeAmount((getPaidOtherChargesEdit!=null ? getPaidOtherChargesEdit:0.00));
			   return new Response<>(SUCCESS, SUCCESS_CODE, "updatedata", null, approvaldetails);
				
			}else {
				//approval not saved 
				WorkOrderCreationApprovalDto approvaldetails = (WorkOrderCreationApprovalDto) sessionFactory
						.getCurrentSession().getNamedQuery("GET_WO_REQUEST_BY_REQUEST_ID")
						.setParameter("workorderRequestId", apptovalDto.getWorkorderRequestId())
						.setResultTransformer(Transformers.aliasToBean(WorkOrderCreationApprovalDto.class)).uniqueResult();

				List<WorkOrderCreationRequestApprovalDetailsDto> detaillist = sessionFactory.getCurrentSession()
						.getNamedQuery("GET_WO_REQUEST_DETAILS_BY_REQUEST_ID")
						.setParameter("workorderRequestId", apptovalDto.getWorkorderRequestId())
						.setResultTransformer(Transformers.aliasToBean(WorkOrderCreationRequestApprovalDetailsDto.class)).list();
				

				List<WorkOrderCreationOtherChargeDetailsDto> otherchargedetaillist = sessionFactory.getCurrentSession()
						.getNamedQuery("GET_WO_REQUEST_OTHERCHARGE_DETAILS_BY_REQUEST_ID")
						.setParameter("workorderRequestId", apptovalDto.getWorkorderRequestId())
						.setResultTransformer(Transformers.aliasToBean(WorkOrderCreationOtherChargeDetailsDto.class))
						.list();

				List<WorkOrderCreationRequestDocumentDto> documentdetaillist = sessionFactory.getCurrentSession()
						.getNamedQuery("GET_WO_REQUEST_DOCUMENT_DETAILS_BY_REQUEST_ID")
						.setParameter("workorderRequestId", apptovalDto.getWorkorderRequestId())
						.setResultTransformer(Transformers.aliasToBean(WorkOrderCreationRequestDocumentDto.class)).list();
				for (WorkOrderCreationRequestApprovalDetailsDto workOrderCreationRequestDetailsDto : detaillist) {
	                String hql = "SELECT newWorkOrderCreationStagedDetailsMapper FROM NewWorkOrderCreationStagedDetailsMapper AS newWorkOrderCreationStagedDetailsMapper where workorderRequestDetailsId=:WODetailId ";

	                Query query = sessionFactory.getCurrentSession().createQuery(hql);
	                query.setParameter("WODetailId", workOrderCreationRequestDetailsDto.getWorkorderRequestDetialsId());
	                List<NewWorkOrderCreationStagedDetailsMapper> getList= query.list();
	                List<NewWorkOrderCreationStagedDetailsMapper> getList3=new ArrayList<NewWorkOrderCreationStagedDetailsMapper>();
	                for (NewWorkOrderCreationStagedDetailsMapper getList1 : getList) {
	                    NewWorkOrderCreationStagedDetailsMapper getList2=new NewWorkOrderCreationStagedDetailsMapper();
	                    getList2.setStagedWorkorderId(getList1.getStagedWorkorderId());
	                    getList2.setStageName(getList1.getStageName());
	                    getList2.setPercentage(getList1.getPercentage());
	                    getList2.setWorkorderRequestId(apptovalDto.getWorkorderRequestId());
	                    getList2.setPercentageAmountPaid(getList1.getPercentageAmountPaid());
	                    getList2.setPercentageAmountPay(getList1.getPercentageAmountPay());
	                    getList3.add(getList2);
	                }
	                workOrderCreationRequestDetailsDto.setWorkOrderCreationStagedListData(getList3);
	            }
				if(detaillist.size()>0) 
					approvaldetails.setRequestApprovalDetailsDto(detaillist);
				if(otherchargedetaillist.size()>0)
					approvaldetails.setRequestTaxDetailsDto(otherchargedetaillist);
				if(documentdetaillist.size()>0)
					approvaldetails.setRequestDocumentDto(documentdetaillist);
				
				String getPaidOtherChargesSqlEdit=" select cast(sum(other_charge_amount) as float) from procurement.t_new_work_order_completion where workorder_request_id="+apptovalDto.getWorkorderRequestId()+" and work_order_completion_id="+apptovalDto.getWorkOrderCompletionId()+"" ;
                Query getPaidOtherChargesQueryEdit = sessionFactory.getCurrentSession().createSQLQuery(getPaidOtherChargesSqlEdit);
                Double getPaidOtherChargesEdit=(Double) getPaidOtherChargesQueryEdit.uniqueResult();
                approvaldetails.setOtherChargeAmount((getPaidOtherChargesEdit!=null ? getPaidOtherChargesEdit:0.00));
			   return new Response<>(SUCCESS, SUCCESS_CODE, "savedata", null, approvaldetails);

			}

	   } catch (HibernateException he) {
		logger.error("getCreationApprovalDetailsbyIdSettlement failed", he);
		throw new ServiceException(he.getMessage());
	  } catch (Exception e) {
		logger.error("getCreationApprovalDetailsbyIdSettlement failed", e);
		throw new ServiceException(e.getMessage());
	  } finally {

	  }
	}

	@Override
	public Response updateorder(WorkOrderCreationApprovalDto apptovalDto) {
		try {

			Session	opensession=sessionFactory.getCurrentSession();

			opensession.createQuery(" UPDATE NewWorkOrderApprovalDetails SET status='I'  "
							+ " WHERE workorderRequestId ="+apptovalDto.getWorkorderRequestId())
					.executeUpdate();

			opensession.createQuery(" UPDATE NewWorkOrderItemRejectionDetails SET status='I'  "
							+ " WHERE workorderRequestId ="+apptovalDto.getWorkorderRequestId())
					.executeUpdate();
			opensession.close();
			return new Response<>(SUCCESS, SUCCESS_CODE, "savedata", null, null);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
