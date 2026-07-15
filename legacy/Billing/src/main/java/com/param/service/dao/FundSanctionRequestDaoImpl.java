package com.param.service.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.billing.global.model.BillFundSanctionDetails;
import com.param.billing.global.model.FundInFlowMaster;
import com.param.billing.global.model.FundInflowApprovalDetails;
import com.param.billing.global.model.FundSanctionRequestApprovalDetails;
import com.param.billing.global.model.FundSanctionRequestDetails;
import com.param.billing.global.model.FundSanctionRequestDocumentsDetails;
import com.param.global.common.IBillingCommonConstants;
import com.param.global.common.ICommonConstants;
import com.param.global.common.IError;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dao.IGlobalUnitMasterDao;
import com.param.opd.encounter.dto.FundSanctionRequestDocumentDto;
import com.param.service.dto.BillFundSanctionDetailsDto;
import com.param.service.dto.FundInFlowDetailsDto;
import com.param.service.dto.FundSanctionAcceptanceRequestDto;
import com.param.service.dto.FundSanctionRequestDto;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" ,"unused"})
public class FundSanctionRequestDaoImpl 
		implements IFundSanctionRequestDao, ICommonConstants, IError {


	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	IGlobalUnitMasterDao iGlobalUnitMasterDao;

	final static Logger logger = Logger.getLogger(FundInFlowMaster.class);

	@Override
	public Response saveFundSanctionRequest(FundSanctionRequestDto fundSanctionRequestDto) throws ApplicationException {
		try {
			
			FundSanctionRequestDetails  sanctionRequestDetails =new FundSanctionRequestDetails();
			sanctionRequestDetails.setTrustId(fundSanctionRequestDto.getTrustId());
			sanctionRequestDetails.setFundrequestNumber(iGlobalUnitMasterDao.getGeneratedNo(0, fundSanctionRequestDto.getUnitId(), IBillingCommonConstants.SANCTION_REQUEST_NUMBER));
			sanctionRequestDetails.setFundrequestAmount(fundSanctionRequestDto.getFundrequestAmount());
			
			if(fundSanctionRequestDto.getFundrequestRemark()!=null)
			sanctionRequestDetails.setFundrequestRemark(fundSanctionRequestDto.getFundrequestRemark());
			
			sanctionRequestDetails.setFundrequestStatusId(1);
			sanctionRequestDetails.setPatientId(fundSanctionRequestDto.getPatientId());
			sanctionRequestDetails.setUnitId(fundSanctionRequestDto.getUnitId());
			sanctionRequestDetails.setOrgId(fundSanctionRequestDto.getOrgId());
			sanctionRequestDetails.setStatus("A");
			sanctionRequestDetails.setFundrequestDate(TimeZoneComponent.getDateByzone(fundSanctionRequestDto.getUnitId()));
			sanctionRequestDetails.setCreatedBy(fundSanctionRequestDto.getCreatedBy());
			sanctionRequestDetails.setCreatedDate(TimeZoneComponent.getDateByzone(fundSanctionRequestDto.getUnitId()));
			sanctionRequestDetails.setUpdatedBy(fundSanctionRequestDto.getUpdatedBy());
			sanctionRequestDetails.setUpdatedDate(TimeZoneComponent.getDateByzone(fundSanctionRequestDto.getUnitId()));
			sessionFactory.getCurrentSession().save(sanctionRequestDetails);
			
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);

		} catch (HibernateException he) {
			logger.error("saveFundSanctionRequest failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("saveFundSanctionRequest failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response fundSanctionRequestByPatientId(FundSanctionRequestDto fundSanctionRequestDto)
			throws ApplicationException {
   try {
	    List<FundSanctionRequestDto> listfundSanctionRequestDto = sessionFactory.getCurrentSession()
				.getNamedQuery("GET_SANCTION_REQUEST_BY_PATIENT_ID")
				.setInteger("patientId", fundSanctionRequestDto.getPatientId())
				.setInteger("unitId", fundSanctionRequestDto.getUnitId())
				.setInteger("orgId", fundSanctionRequestDto.getOrgId())
				.setResultTransformer(Transformers.aliasToBean(FundSanctionRequestDto.class)).list();
		return new Response<>(SUCCESS, SUCCESS_CODE, null, listfundSanctionRequestDto, null);

		} catch (HibernateException he) {
			logger.error("fundSanctionRequestByPatientId failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("fundSanctionRequestByPatientId failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response fundSanctionRequestDocument(FundSanctionRequestDocumentDto fundSanctionRequestDocumentDto)
			throws ApplicationException {
   try {
	   FundSanctionRequestDocumentsDetails documentupload=new FundSanctionRequestDocumentsDetails();
	   documentupload.setFundsanctionrequestDetailsId(fundSanctionRequestDocumentDto.getFundsanctionrequestDetailsId());
	   documentupload.setDocumentTypeId(fundSanctionRequestDocumentDto.getDocumentTypeId());
	   documentupload.setDocumentPath(fundSanctionRequestDocumentDto.getDocumentPath());
	   documentupload.setRequestDocumentDate(TimeZoneComponent.getDateByzone(fundSanctionRequestDocumentDto.getUnitId()));
	   documentupload.setStatus("A");
	   documentupload.setCreatedBy(fundSanctionRequestDocumentDto.getCreatedBy());
	   documentupload.setUpdatedBy(fundSanctionRequestDocumentDto.getCreatedBy());
	   documentupload.setCreatedDate(TimeZoneComponent.getDateByzone(fundSanctionRequestDocumentDto.getUnitId()));
	   documentupload.setUpdatedDate(TimeZoneComponent.getDateByzone(fundSanctionRequestDocumentDto.getUnitId()));
	   sessionFactory.getCurrentSession().save(documentupload);
		return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);

		} catch (HibernateException he) {
			logger.error("fundSanctionRequestDocument failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("fundSanctionRequestDocument failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getDocumentsByFundSanctionRequestId(FundSanctionRequestDocumentDto fundSanctionRequestDocumentDto)
			throws ApplicationException {
		try {
		    List<FundSanctionRequestDto> listfundSanctionRequestDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_REQUEST_DOCUMENT_BY_SANCTION_REQUEST_ID")
					.setParameter("fundsanctionrequestDetailsId", fundSanctionRequestDocumentDto.getFundsanctionrequestDetailsId())
					.setResultTransformer(Transformers.aliasToBean(FundSanctionRequestDocumentDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listfundSanctionRequestDto, null);

			} catch (HibernateException he) {
				logger.error("getDocumentsByFundSanctionRequestId failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getDocumentsByFundSanctionRequestId failed", e);
				throw new ServiceException(e.getMessage());
			}
	}


	@Override
	public Response saveRequestApprovalFromFundinflow(FundSanctionAcceptanceRequestDto fundSanctionAcceptanceRequestDto)
			throws ApplicationException {
		try {
			    double inflowcollectedamount=0;
                if(fundSanctionAcceptanceRequestDto.getFundInFlowDetailsDto().size()>0) {
				for(   FundInFlowDetailsDto dto :fundSanctionAcceptanceRequestDto.getFundInFlowDetailsDto()) {
					inflowcollectedamount= inflowcollectedamount+dto.getTrustAcceptedAmount();
					}
				}
			
			FundSanctionRequestApprovalDetails requestapprovalDetails=new FundSanctionRequestApprovalDetails();
			requestapprovalDetails.setFundsanctionrequestDetailsId(fundSanctionAcceptanceRequestDto.getFundsanctionrequestDetailsId());
			requestapprovalDetails.setTrustId(fundSanctionAcceptanceRequestDto.getTrustId());
			requestapprovalDetails.setFundrequestAmount(fundSanctionAcceptanceRequestDto.getFundrequestAmount());
			if(inflowcollectedamount!=fundSanctionAcceptanceRequestDto.getRequestApprovalAmount()) {
				requestapprovalDetails.setFundrequestApprovalAmount(inflowcollectedamount);
				requestapprovalDetails.setFundrequestApprovalAvailableAmount(inflowcollectedamount);
			}else {
				requestapprovalDetails.setFundrequestApprovalAmount(fundSanctionAcceptanceRequestDto.getRequestApprovalAmount());
				requestapprovalDetails.setFundrequestApprovalAvailableAmount(fundSanctionAcceptanceRequestDto.getRequestApprovalAmount());
			}
			requestapprovalDetails.setFundAcceptacepaymentTypeId(fundSanctionAcceptanceRequestDto.getFundAcceptacepaymentTypeId());
			requestapprovalDetails.setFundrequestApprovalDate(new  Date());
			requestapprovalDetails.setPatientId(fundSanctionAcceptanceRequestDto.getPatientId());
			requestapprovalDetails.setOrgId(fundSanctionAcceptanceRequestDto.getOrgId());
			requestapprovalDetails.setUnitId(fundSanctionAcceptanceRequestDto.getUnitId());
			requestapprovalDetails.setCreatedBy(fundSanctionAcceptanceRequestDto.getCreatedBy());
			requestapprovalDetails.setCreatedDate(TimeZoneComponent.getDateByzone(fundSanctionAcceptanceRequestDto.getUnitId()));
			requestapprovalDetails.setUpdatedBy(fundSanctionAcceptanceRequestDto.getCreatedBy());
			requestapprovalDetails.setUpdatedDate(TimeZoneComponent.getDateByzone(fundSanctionAcceptanceRequestDto.getUnitId()));
			requestapprovalDetails.setStatus("�");
			requestapprovalDetails.setApprovalBillConsumeAmount(0.0);
			sessionFactory.getCurrentSession().save(requestapprovalDetails);
		     Session	opensession=sessionFactory.openSession();
			if(fundSanctionAcceptanceRequestDto.getFundInFlowDetailsDto().size()>0) {
				
				double collectedamount=0;
				for(   FundInFlowDetailsDto dto :fundSanctionAcceptanceRequestDto.getFundInFlowDetailsDto()) {
					if(( dto.getAvailableAmount()- dto.getTrustAcceptedAmount())>0) {
						//update  available fund 
						double updateavailableamt=( dto.getAvailableAmount()- dto.getTrustAcceptedAmount());
						opensession.createQuery(" UPDATE FundInFlowMaster SET availableAmount =:availableAmount "
				        		+ " WHERE trustId =:trustId AND  fundInFlowId =:fundInFlowId ")
								.setParameter("availableAmount", updateavailableamt)
								.setParameter("trustId", fundSanctionAcceptanceRequestDto.getTrustId())
								.setParameter("fundInFlowId", dto.getFundInFlowId())
								.executeUpdate();
					}else {
						// inActive fund inflow
						double updateavailableamt=( dto.getAvailableAmount()- dto.getTrustAcceptedAmount());
						if(updateavailableamt==0) {
							opensession.createQuery(" UPDATE FundInFlowMaster SET status='I' , availableAmount =:availableAmount "
					        		+ " WHERE trustId =:trustId AND  fundInFlowId =:fundInFlowId ")
									.setParameter("availableAmount", updateavailableamt)
									.setParameter("trustId", fundSanctionAcceptanceRequestDto.getTrustId())
									.setParameter("fundInFlowId", dto.getFundInFlowId())
									.executeUpdate();
						}
					}
					if(dto.getFundInFlowId()!=null) {
							FundInflowApprovalDetails details=new FundInflowApprovalDetails();
							details.setFundsanctionrequestApprovalDetailsId(requestapprovalDetails.getFundsanctionrequestApprovalDetailsId());
							details.setTrustId(fundSanctionAcceptanceRequestDto.getTrustId());
							details.setFundInFlowId(dto.getFundInFlowId());
							details.setFundApprovalAmount(dto.getTrustAcceptedAmount());
							details.setFundAcceptacepaymentTypeId(fundSanctionAcceptanceRequestDto.getFundAcceptacepaymentTypeId());
							details.setFundrequestApprovalDate(TimeZoneComponent.getDateByzone(fundSanctionAcceptanceRequestDto.getUnitId()));
							details.setPatientId(requestapprovalDetails.getPatientId());
							details.setOrgId(requestapprovalDetails.getOrgId());
							details.setUnitId(requestapprovalDetails.getUnitId());
							details.setCreatedBy(fundSanctionAcceptanceRequestDto.getCreatedBy());
							details.setCreatedDate(TimeZoneComponent.getDateByzone(fundSanctionAcceptanceRequestDto.getUnitId()));
							details.setStatus("A");
							details.setApprovalBillConsumeAmount(0.0);
							collectedamount=collectedamount+dto.getTrustAcceptedAmount();
							sessionFactory.getCurrentSession().save(details);
					       }
					}
							if(collectedamount!=fundSanctionAcceptanceRequestDto.getRequestApprovalAmount() ) {
								//add half virtual Payment
								double ammountDiff=fundSanctionAcceptanceRequestDto.getRequestApprovalAmount()-collectedamount ;
								
								if(ammountDiff>0) {
									
									
									FundSanctionRequestApprovalDetails requestapprovalDetails1=new FundSanctionRequestApprovalDetails();
									requestapprovalDetails1.setFundsanctionrequestDetailsId(fundSanctionAcceptanceRequestDto.getFundsanctionrequestDetailsId());
									requestapprovalDetails1.setTrustId(fundSanctionAcceptanceRequestDto.getTrustId());
									requestapprovalDetails1.setFundrequestAmount(fundSanctionAcceptanceRequestDto.getFundrequestAmount());
									requestapprovalDetails1.setFundrequestApprovalAmount(ammountDiff);
									requestapprovalDetails1.setFundrequestApprovalAvailableAmount(ammountDiff);
									requestapprovalDetails1.setFundAcceptacepaymentTypeId(2);
									requestapprovalDetails1.setFundrequestApprovalDate(new  Date());
									requestapprovalDetails1.setPatientId(fundSanctionAcceptanceRequestDto.getPatientId());
									requestapprovalDetails1.setOrgId(fundSanctionAcceptanceRequestDto.getOrgId());
									requestapprovalDetails1.setUnitId(fundSanctionAcceptanceRequestDto.getUnitId());
									requestapprovalDetails1.setCreatedBy(fundSanctionAcceptanceRequestDto.getCreatedBy());
									requestapprovalDetails1.setCreatedDate(TimeZoneComponent.getDateByzone(fundSanctionAcceptanceRequestDto.getUnitId()));
									requestapprovalDetails1.setUpdatedBy(fundSanctionAcceptanceRequestDto.getCreatedBy());
									requestapprovalDetails1.setUpdatedDate(TimeZoneComponent.getDateByzone(fundSanctionAcceptanceRequestDto.getUnitId()));
									requestapprovalDetails1.setStatus("A");
									requestapprovalDetails1.setIsFundInflowGiven("N");
									requestapprovalDetails1.setApprovalBillConsumeAmount(0.0);
									requestapprovalDetails1.setVirtualPaidAmount(0.0);
									
									
									sessionFactory.getCurrentSession().save(requestapprovalDetails1);
									
									
									FundInflowApprovalDetails details1=new FundInflowApprovalDetails();
									details1.setFundsanctionrequestApprovalDetailsId(requestapprovalDetails1.getFundsanctionrequestApprovalDetailsId());
									details1.setTrustId(fundSanctionAcceptanceRequestDto.getTrustId());
									details1.setFundApprovalAmount(ammountDiff);
									details1.setFundAcceptacepaymentTypeId(2);
									details1.setFundrequestApprovalDate(TimeZoneComponent.getDateByzone(fundSanctionAcceptanceRequestDto.getUnitId()));
									details1.setPatientId(requestapprovalDetails1.getPatientId());
									details1.setOrgId(requestapprovalDetails1.getOrgId());
									details1.setUnitId(requestapprovalDetails1.getUnitId());
									details1.setCreatedBy(fundSanctionAcceptanceRequestDto.getCreatedBy());
									details1.setCreatedDate(TimeZoneComponent.getDateByzone(fundSanctionAcceptanceRequestDto.getUnitId()));
									details1.setStatus("A");
									details1.setApprovalBillConsumeAmount(0.0);
									sessionFactory.getCurrentSession().save(details1);
								}
					
					
				}
				
				
			}
			opensession.createQuery(" UPDATE FundSanctionRequestDetails  SET fundrequestStatusId=2 "
	        		+ " WHERE fundsanctionrequestDetailsId =:fundsanctionrequestDetailsId ")
					.setParameter("fundsanctionrequestDetailsId", fundSanctionAcceptanceRequestDto.getFundsanctionrequestDetailsId())
					 .executeUpdate();
			opensession.beginTransaction().commit();
			opensession.close();
			
			
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
			} catch (HibernateException he) {
				logger.error("saveRequestApprovalFromFundinflow failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("saveRequestApprovalFromFundinflow failed", e);
				throw new ServiceException(e.getMessage());
			}
	}

	@Override
	public Response saveRequestApprovalFromFundVirtual(
			FundSanctionAcceptanceRequestDto fundSanctionAcceptanceRequestDto) throws ApplicationException {
		try {
			FundSanctionRequestApprovalDetails requestapprovalDetails=new FundSanctionRequestApprovalDetails();
			requestapprovalDetails.setFundsanctionrequestDetailsId(fundSanctionAcceptanceRequestDto.getFundsanctionrequestDetailsId());
			requestapprovalDetails.setTrustId(fundSanctionAcceptanceRequestDto.getTrustId());
			requestapprovalDetails.setFundrequestAmount(fundSanctionAcceptanceRequestDto.getFundrequestAmount());
			requestapprovalDetails.setFundrequestApprovalAvailableAmount(fundSanctionAcceptanceRequestDto.getFundrequestAmount());
			requestapprovalDetails.setFundrequestApprovalAmount(fundSanctionAcceptanceRequestDto.getRequestApprovalAmount());
			requestapprovalDetails.setFundAcceptacepaymentTypeId(fundSanctionAcceptanceRequestDto.getFundAcceptacepaymentTypeId());
			requestapprovalDetails.setFundrequestApprovalDate(new  Date());
			requestapprovalDetails.setPatientId(fundSanctionAcceptanceRequestDto.getPatientId());
			requestapprovalDetails.setOrgId(fundSanctionAcceptanceRequestDto.getOrgId());
			requestapprovalDetails.setUnitId(fundSanctionAcceptanceRequestDto.getUnitId());
			requestapprovalDetails.setCreatedBy(fundSanctionAcceptanceRequestDto.getCreatedBy());
			requestapprovalDetails.setCreatedDate(TimeZoneComponent.getDateByzone(fundSanctionAcceptanceRequestDto.getUnitId()));
			requestapprovalDetails.setUpdatedBy(fundSanctionAcceptanceRequestDto.getCreatedBy());
			requestapprovalDetails.setUpdatedDate(TimeZoneComponent.getDateByzone(fundSanctionAcceptanceRequestDto.getUnitId()));
			requestapprovalDetails.setStatus(requestapprovalDetails.getStatus());
			requestapprovalDetails.setIsFundInflowGiven("N");
			requestapprovalDetails.setStatus("�");
			requestapprovalDetails.setApprovalBillConsumeAmount(0.0);
			requestapprovalDetails.setVirtualPaidAmount(0.0);
			sessionFactory.getCurrentSession().save(requestapprovalDetails);
			
		                Session	opensession=sessionFactory.openSession();
						FundInflowApprovalDetails details=new FundInflowApprovalDetails();
						details.setFundsanctionrequestApprovalDetailsId(requestapprovalDetails.getFundsanctionrequestApprovalDetailsId());
						details.setTrustId(fundSanctionAcceptanceRequestDto.getTrustId());
						details.setFundApprovalAmount(fundSanctionAcceptanceRequestDto.getRequestApprovalAmount());
						details.setFundAcceptacepaymentTypeId(fundSanctionAcceptanceRequestDto.getFundAcceptacepaymentTypeId());
						details.setFundrequestApprovalDate(TimeZoneComponent.getDateByzone(fundSanctionAcceptanceRequestDto.getUnitId()));
						details.setPatientId(requestapprovalDetails.getPatientId());
						details.setOrgId(requestapprovalDetails.getOrgId());
						details.setUnitId(requestapprovalDetails.getUnitId());
						details.setCreatedBy(fundSanctionAcceptanceRequestDto.getCreatedBy());
						details.setCreatedDate(TimeZoneComponent.getDateByzone(fundSanctionAcceptanceRequestDto.getUnitId()));
						details.setStatus("A");
						details.setApprovalBillConsumeAmount(0.0);
						sessionFactory.getCurrentSession().save(details);
			            opensession.createQuery(" UPDATE FundSanctionRequestDetails  SET fundrequestStatusId=2 "
	        		                          + " WHERE fundsanctionrequestDetailsId =:fundsanctionrequestDetailsId ")
								               .setParameter("fundsanctionrequestDetailsId", fundSanctionAcceptanceRequestDto.getFundsanctionrequestDetailsId())
					                           .executeUpdate();
			            opensession.beginTransaction().commit();
			            opensession.close();
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
			} catch (HibernateException he) {
				logger.error("saveRequestApprovalFromFundVirtual failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("saveRequestApprovalFromFundVirtual failed", e);
				throw new ServiceException(e.getMessage());
			}
	}

	@Override
	public Response saveRequestReject(FundSanctionAcceptanceRequestDto fundSanctionAcceptanceRequestDto)
			throws ApplicationException {
		try {
			sessionFactory.getCurrentSession().createQuery(" UPDATE FundSanctionRequestDetails  SET fundrequestStatusId=3 "
                    + " WHERE fundsanctionrequestDetailsId =:fundsanctionrequestDetailsId ")
					 .setParameter("fundsanctionrequestDetailsId", fundSanctionAcceptanceRequestDto.getFundsanctionrequestDetailsId())
                     .executeUpdate();
			return new Response<>(SUCCESS, COMMON_UPDATE, null, null, null);
			} catch (HibernateException he) {
				logger.error("saveRequestReject failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("saveRequestReject failed", e);
				throw new ServiceException(e.getMessage());
			}
	}

	@Override
	public Response getAllPendingVirtualAcceptance(FundSanctionAcceptanceRequestDto fundSanctionAcceptanceRequestDto)
			throws ApplicationException {
		try {
			List<FundSanctionAcceptanceRequestDto> listfundSanctionRequestDto=new  ArrayList<FundSanctionAcceptanceRequestDto>();
			String querystr=sessionFactory.getCurrentSession()
			                                  .getNamedQuery("GET_ALLPPENDING_REQUEST_ACCEPTED_VIRTUALAMOUNT_BY_TRUST_ID").getQueryString().toString();
			StringBuilder finalQueryPart1 = new StringBuilder(querystr);
			if(fundSanctionAcceptanceRequestDto.getPatientId()!=null) {
				
				finalQueryPart1.append(" and appdetails.patient_id =:patientId order by  appdetails.fundsanctionrequest_approval_details_id desc ");
                         listfundSanctionRequestDto = sessionFactory.getCurrentSession()
						.createSQLQuery(finalQueryPart1.toString())
						.setParameter("trustId", fundSanctionAcceptanceRequestDto.getTrustId())
						.setParameter("orgId", fundSanctionAcceptanceRequestDto.getOrgId())
						.setParameter("unitId", fundSanctionAcceptanceRequestDto.getUnitId())
						.setParameter("patientId",fundSanctionAcceptanceRequestDto.getPatientId())
						.setResultTransformer(Transformers.aliasToBean(FundSanctionAcceptanceRequestDto.class)).list();
			
			  }   else {
				finalQueryPart1.append(" order by  appdetails.fundsanctionrequest_approval_details_id desc ");
                listfundSanctionRequestDto = sessionFactory.getCurrentSession()
				.createSQLQuery(finalQueryPart1.toString())
				.setParameter("trustId", fundSanctionAcceptanceRequestDto.getTrustId())
				.setParameter("orgId", fundSanctionAcceptanceRequestDto.getOrgId())
				.setParameter("unitId", fundSanctionAcceptanceRequestDto.getUnitId())
				.setResultTransformer(Transformers.aliasToBean(FundSanctionAcceptanceRequestDto.class)).list();
				
			 }
			
//			
			 
				return new Response<>(SUCCESS, SUCCESS_CODE, null, listfundSanctionRequestDto, null);

				} catch (HibernateException he) {
					logger.error("getAllPendingVirtualAcceptance failed", he);
					throw new ServiceException(he.getMessage());
				} catch (Exception e) {
					logger.error("getAllPendingVirtualAcceptance failed", e);
					throw new ServiceException(e.getMessage());
				}
	}

	@Override
	public Response getInflowAcceptedRequest(FundSanctionAcceptanceRequestDto fundSanctionAcceptanceRequestDto)
			throws ApplicationException {
		try {
			 List<FundSanctionAcceptanceRequestDto> listfundSanctionRequestDto = sessionFactory.getCurrentSession()
						.getNamedQuery("GET_REQUEST_ACCEPTED_BY_INFLOW_ID")
						.setParameter("fundInFlowId", fundSanctionAcceptanceRequestDto.getFundInFlowId())
						.setParameter("orgId", fundSanctionAcceptanceRequestDto.getOrgId())
						.setParameter("unitId", fundSanctionAcceptanceRequestDto.getUnitId())
						.setResultTransformer(Transformers.aliasToBean(FundSanctionAcceptanceRequestDto.class)).list();
				return new Response<>(SUCCESS, SUCCESS_CODE, null, listfundSanctionRequestDto, null);

				} catch (HibernateException he) {
					logger.error("getInflowAcceptedRequest failed", he);
					throw new ServiceException(he.getMessage());
				} catch (Exception e) {
					logger.error("getInflowAcceptedRequest failed", e);
					throw new ServiceException(e.getMessage());
				}
	}

	@Override
	public Response getVirtualRequestPayments(FundSanctionAcceptanceRequestDto fundSanctionAcceptanceRequestDto)
			throws ApplicationException {
		try {
			 List<FundSanctionAcceptanceRequestDto> listfundSanctionRequestDto = sessionFactory.getCurrentSession()
						.getNamedQuery("GET_VIRTUAL_REQUEST_PAYMENTS_DETAILS")
						.setParameter("fundInFlowId", fundSanctionAcceptanceRequestDto.getFundInFlowId())
						.setParameter("orgId", fundSanctionAcceptanceRequestDto.getOrgId())
						.setParameter("unitId", fundSanctionAcceptanceRequestDto.getUnitId())
						.setResultTransformer(Transformers.aliasToBean(FundSanctionAcceptanceRequestDto.class)).list();
				return new Response<>(SUCCESS, SUCCESS_CODE, null, listfundSanctionRequestDto, null);

				} catch (HibernateException he) {
					logger.error("getVirtualRequestPayments failed", he);
					throw new ServiceException(he.getMessage());
				} catch (Exception e) {
					logger.error("getVirtualRequestPayments failed", e);
					throw new ServiceException(e.getMessage());
				}
	}

	@Override
	public Response getAllFundApprovalRequestByPatient(
			FundSanctionAcceptanceRequestDto fundSanctionAcceptanceRequestDto) throws ApplicationException {
		try {
			 List<FundSanctionAcceptanceRequestDto> listfundSanctionRequestDto = sessionFactory.getCurrentSession()
						.getNamedQuery("GET_ALL_FUND_REQUEST_PAYMENTS_DETAILS_PATIENT_ID")
						.setParameter("patientId", fundSanctionAcceptanceRequestDto.getPatientId())
						.setParameter("orgId", fundSanctionAcceptanceRequestDto.getOrgId())
						.setParameter("unitId", fundSanctionAcceptanceRequestDto.getUnitId())
						.setResultTransformer(Transformers.aliasToBean(FundSanctionAcceptanceRequestDto.class)).list();
				return new Response<>(SUCCESS, SUCCESS_CODE, null, listfundSanctionRequestDto, null);

				} catch (HibernateException he) {
					logger.error("getAllFundApprovalRequestByPatient failed", he);
					throw new ServiceException(he.getMessage());
				} catch (Exception e) {
					logger.error("getAllFundApprovalRequestByPatient failed", e);
					throw new ServiceException(e.getMessage());
				}
	}

	@Override
	public Response saveBillFundDetails(BillFundSanctionDetailsDto billFundSanctionDetailsDto)
			throws ApplicationException {
		try {
//			billFundconsumptionTypeId  1=fund consumption, 2=deposit, 3=cash
//     		fundAcceptacepaymentTypeId  1=fundInflow then fundInFlowId!=null ,  2=virtualAcceptance  then fundInFlowId=null
			
			sessionFactory.getCurrentSession().createQuery("UPDATE BillFundSanctionDetails SET status='I' where billingMasterId =:billingMasterId")
			.setParameter("billingMasterId",billFundSanctionDetailsDto.getBillingMasterId() )
			.executeUpdate();
			
			if(billFundSanctionDetailsDto.getBillFundSanctionDetailsDto().size()>0) {
							
							for(  BillFundSanctionDetailsDto fundObj: billFundSanctionDetailsDto.getBillFundSanctionDetailsDto()) {
								BillFundSanctionDetails billfundDetails=new  BillFundSanctionDetails();
								
								billfundDetails.setBillingMasterId(fundObj.getBillingMasterId());
								billfundDetails.setBillFundconsumptionTypeId(fundObj.getBillFundconsumptionTypeId());
								if(fundObj.getBillFundconsumptionTypeId()==1) {
									//	fund selected
									billfundDetails.setFundsanctionrequestDetailsId(fundObj.getFundsanctionrequestDetailsId());  
									billfundDetails.setFundsanctionrequestApprovalDetailsId(fundObj.getFundsanctionrequestApprovalDetailsId());
									billfundDetails.setFundAcceptacepaymentTypeId(fundObj.getFundAcceptacepaymentTypeId());
									if(fundObj.getFundAcceptacepaymentTypeId()==1) {
										//fund inflow
										billfundDetails.setFundInFlowId(fundObj.getFundInFlowId());
									}
									billfundDetails.setTrustId(fundObj.getTrustId());
									billfundDetails.setPatientId(fundObj.getPatientId());
								}
								billfundDetails.setBillFundSelectedAmount(fundObj.getBillAcceptancePaidAmount());
								billfundDetails.setOrgId(billFundSanctionDetailsDto.getOrgId());
								billfundDetails.setUnitId(billFundSanctionDetailsDto.getUnitId());
								billfundDetails.setStatus("A");
								billfundDetails.setCreatedBy(billFundSanctionDetailsDto.getCreatedBy());
								billfundDetails.setUpdatedBy(billFundSanctionDetailsDto.getCreatedBy());
								billfundDetails.setCreatedDate(TimeZoneComponent.getDateByzone(billFundSanctionDetailsDto.getUnitId()));
								billfundDetails.setUpdatedDate(TimeZoneComponent.getDateByzone(billFundSanctionDetailsDto.getUnitId()));
								
								sessionFactory.getCurrentSession().save(billfundDetails);
							}
							
						}
				return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
				} catch (HibernateException he) {
					logger.error("saveBillFundDetails failed", he);
					throw new ServiceException(he.getMessage());
				} catch (Exception e) {
					logger.error("saveBillFundDetails failed", e);
					throw new ServiceException(e.getMessage());
				}
	}

	@Override
	public Response getBillFundDetailsByBillMasterId(BillFundSanctionDetailsDto billFundSanctionDetailsDto)
			throws ApplicationException {
				try {
					List<BillFundSanctionDetailsDto> listbillFundSanctionDetailsDto = sessionFactory.getCurrentSession()
							.getNamedQuery("GET_BILLFUND_DETAILS_BY_BILLMASTER_ID")
							.setParameter("billingMasterId", billFundSanctionDetailsDto.getBillingMasterId())
							.setParameter("orgId", billFundSanctionDetailsDto.getOrgId())
							.setParameter("unitId", billFundSanctionDetailsDto.getUnitId())
							.setResultTransformer(Transformers.aliasToBean(BillFundSanctionDetailsDto.class)).list();
					return new Response<>(SUCCESS, SUCCESS_CODE, null, listbillFundSanctionDetailsDto, null);
					
				} catch (HibernateException he) {
					logger.error("getBillFundDetailsByBillMasterId failed", he);
					throw new ServiceException(he.getMessage());
				} catch (Exception e) {
					logger.error("getBillFundDetailsByBillMasterId failed", e);
					throw new ServiceException(e.getMessage());
				}
	}

	
	
}
