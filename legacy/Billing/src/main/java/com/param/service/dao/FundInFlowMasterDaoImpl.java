package com.param.service.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.billing.global.model.FundInFlowDetails;
import com.param.billing.global.model.FundInFlowDiagnosisMaster;
import com.param.billing.global.model.FundInFlowMaster;
import com.param.billing.global.model.FundInFlowVirtualAcceptanceMaster;
import com.param.billing.global.model.FundSanctionRequestApprovalDetails;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.IError;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.global.VisitType;
import com.param.service.dto.FundInFlowDetailsDto;
import com.param.service.dto.FundSanctionAcceptanceRequestDto;
import com.param.service.dto.TrustMasterDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class FundInFlowMasterDaoImpl extends GenericDao<FundInFlowMaster, Integer>
		implements IFundInFlowMasterDao, ICommonConstants, IError {

	public FundInFlowMasterDaoImpl() {
		super(FundInFlowMaster.class);
	}


	@Autowired
	private SessionFactory sessionFactory;

	final static Logger logger = Logger.getLogger(FundInFlowMaster.class);

	@Override
	public Response getTrustList(Integer orgId, Integer unitId) throws ApplicationException {
		try {
			List<TrustMasterDto> trustMtrDto = 	sessionFactory.getCurrentSession()
					     .createSQLQuery(" select companymaster.company_id as \"trustId\",companymaster.company_desc as \"trustName\" "
									+ " from public.t_company_master companymaster  "
									+ " WHERE companymaster.organization_id=:orgId AND companymaster.is_trust='A' "
									+ " AND companymaster.unit_id=:unitId "
									+ " ORDER BY companymaster.company_desc ASC ")
							.setParameter("orgId", orgId)
						    .setParameter("unitId", unitId)
							.setResultTransformer(Transformers.aliasToBean(TrustMasterDto.class)).list();
			return new Response(SUCCESS, null, null, trustMtrDto, null);

		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getTrustDetailsById(TrustMasterDto trustMasterDto) throws ApplicationException {
		try {
			TrustMasterDto trustMstrDto = (TrustMasterDto) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_TRUST_LIST_BY_ID")
					.setParameter("trustId", trustMasterDto.getTrustId())
					.setParameter("orgId", trustMasterDto.getOrgId())
					.setParameter("unitId", trustMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(TrustMasterDto.class)).uniqueResult();
			List<VisitType> visitTypeList = 	sessionFactory.getCurrentSession()
				     .createSQLQuery(" select tvt.trust_id as \"trustId\",tvt.visit_type_id as \"id\" ,vtm.visit_type_code as \"visitTypeCode\""
								+ " FROM public.m_trust_visit_type tvt "
								+ " join public.m_visit_type_master vtm  on  tvt.visit_type_id = vtm.visit_type_id "
								+ " WHERE tvt.trust_id =:trustId "
								+ " AND tvt.org_id=:orgId "
								+ " AND tvt.unit_id=:unitId "
				                + " AND tvt.status='A'") 
				        .setParameter("trustId", trustMasterDto.getTrustId())
						.setParameter("orgId", trustMasterDto.getOrgId())
					    .setParameter("unitId", trustMasterDto.getUnitId())
						.setResultTransformer(Transformers.aliasToBean(VisitType.class)).list();
			return new Response(SUCCESS, null, null, visitTypeList, trustMstrDto);

		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response saveFundDeatils(FundInFlowDetailsDto fundInFlowDetails) throws ApplicationException {
		try{
			   
            FundInFlowMaster FundInFlowMaster = new FundInFlowMaster();
            if(fundInFlowDetails.getFundInFlowDetailsDto().get(0).getTrustId()!=null){
			FundInFlowMaster.setTrustId(fundInFlowDetails.getFundInFlowDetailsDto().get(0).getTrustId());
			FundInFlowMaster.setTotalCollectedAmount(fundInFlowDetails.getFundInFlowDetailsDto().get(0).getFinalAmount());
			FundInFlowMaster.setAvailableAmount(fundInFlowDetails.getFundInFlowDetailsDto().get(0).getFinalAmount());
			FundInFlowMaster.setUnitId(fundInFlowDetails.getFundInFlowDetailsDto().get(0).getUnitId());
			FundInFlowMaster.setOrgId(fundInFlowDetails.getFundInFlowDetailsDto().get(0).getOrgId());
			FundInFlowMaster.setCreatedBy(fundInFlowDetails.getCreatedBy());
			FundInFlowMaster.setStatus("A");
			FundInFlowMaster.setBillConsumeAmount(0.0);
			FundInFlowMaster.setCreatedDate(TimeZoneComponent.getDateByzone(fundInFlowDetails.getFundInFlowDetailsDto().get(0).getUnitId()));
			FundInFlowMaster.setUpdatedBy(fundInFlowDetails.getUpdatedBy());
			FundInFlowMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(fundInFlowDetails.getFundInFlowDetailsDto().get(0).getUnitId()));
			if(fundInFlowDetails.getFundInFlowDetailsDto().get(0).getFundRemark()!=null) {
				FundInFlowMaster.setFundRemark(fundInFlowDetails.getFundInFlowDetailsDto().get(0).getFundRemark());
			}
			if(fundInFlowDetails.getFundInFlowDiagnosisDetails()!=null && fundInFlowDetails.getFundInFlowDiagnosisDetails().size() > 0  && fundInFlowDetails.getFundtypeId()==3) {
				//agianst Diagnosis
				FundInFlowMaster.setFundtypeId(3);
			}
			if(fundInFlowDetails.getFundInFlowDetailsDto().get(0).getPatientId()!=null && fundInFlowDetails.getFundtypeId()==2) {
				//agianst Patient
				FundInFlowMaster.setPatientId(fundInFlowDetails.getFundInFlowDetailsDto().get(0).getPatientId());
				FundInFlowMaster.setFundtypeId(2);
			}
			if(fundInFlowDetails.getFundSanctionAcceptanceRequestDto()!=null && fundInFlowDetails.getFundSanctionAcceptanceRequestDto().size() > 0 && fundInFlowDetails.getFundtypeId()==4) {
				//agianst virtual acceptance
				FundInFlowMaster.setFundtypeId(4);
			}
			if( fundInFlowDetails.getFundtypeId()==1) {
				//agianst Trust
				FundInFlowMaster.setFundtypeId(1);
			}
			 sessionFactory.getCurrentSession().save(FundInFlowMaster);
            }
            
            if(FundInFlowMaster.getFundtypeId()==3) {
            	
            	if(fundInFlowDetails.getFundInFlowDiagnosisDetails()!=null  && fundInFlowDetails.getFundInFlowDiagnosisDetails().size()>0) {
    				//agianst Diagnosis
                	List<Integer> Diagnosislist = fundInFlowDetails.getFundInFlowDiagnosisDetails();
                	for(Integer diagnosis: Diagnosislist ){
                		FundInFlowDiagnosisMaster  fundInFlowDiagnosisMaster=new FundInFlowDiagnosisMaster();
                		fundInFlowDiagnosisMaster.setFundInflowId(FundInFlowMaster.getFundInFlowId());
                		fundInFlowDiagnosisMaster.setDiagnosisId(diagnosis);
                		fundInFlowDiagnosisMaster.setCreatedBy(fundInFlowDetails.getCreatedBy());
                		fundInFlowDiagnosisMaster.setCreatedDate(TimeZoneComponent.getDateByzone(fundInFlowDetails.getFundInFlowDetailsDto().get(0).getUnitId()));
                		sessionFactory.getCurrentSession().save(fundInFlowDiagnosisMaster);
                	}
    			}
            }
            
            
            if(FundInFlowMaster.getFundtypeId()==4) {
            	 Session opensession =sessionFactory.openSession();
            	if(fundInFlowDetails.getFundSanctionAcceptanceRequestDto()!=null  && fundInFlowDetails.getFundSanctionAcceptanceRequestDto().size()>0) {
    				//agianst Diagnosis
                	List<FundSanctionAcceptanceRequestDto> virtualacceptaceDtoList = fundInFlowDetails.getFundSanctionAcceptanceRequestDto();
                	for(FundSanctionAcceptanceRequestDto virtualacceptaceDto: virtualacceptaceDtoList ){
                		
                		FundInFlowVirtualAcceptanceMaster virtualAcceptance= new FundInFlowVirtualAcceptanceMaster();
                		virtualAcceptance.setFundInflowId(FundInFlowMaster.getFundInFlowId());
                		virtualAcceptance.setFundsanctionrequestApprovalDetailsId(virtualacceptaceDto.getFundsanctionrequestApprovalDetailsId());
                		virtualAcceptance.setFundApprovalAmount(virtualacceptaceDto.getRequestApprovalAmount());
                		virtualAcceptance.setCreatedBy(fundInFlowDetails.getCreatedBy());
                		virtualAcceptance.setVirtualAcceptancePaidAmmount(virtualacceptaceDto.getVirtualAcceptancePaidAmmount());
                		virtualAcceptance.setCreatedDate(TimeZoneComponent.getDateByzone(fundInFlowDetails.getFundInFlowDetailsDto().get(0).getUnitId()));
                		sessionFactory.getCurrentSession().save(virtualAcceptance);
                		
                		
                		  FundSanctionRequestApprovalDetails  detail= (FundSanctionRequestApprovalDetails) opensession.createQuery("FROM FundSanctionRequestApprovalDetails fundapproval WHERE  fundapproval.fundsanctionrequestApprovalDetailsId="+virtualacceptaceDto.getFundsanctionrequestApprovalDetailsId()).uniqueResult();
                		  double paidamount=detail.getVirtualPaidAmount();
                		  paidamount=virtualacceptaceDto.getVirtualAcceptancePaidAmmount()+paidamount;
                		  
                		  opensession.createQuery(" UPDATE FundSanctionRequestApprovalDetails SET isFundInflowGiven = 'Y' , virtualPaidAmount =:virtualPaidAmount "
				                                + " WHERE fundsanctionrequestApprovalDetailsId =:fundsanctionrequestApprovalDetailsId ")
								  .setParameter("fundsanctionrequestApprovalDetailsId", virtualacceptaceDto.getFundsanctionrequestApprovalDetailsId())
								  .setParameter("virtualPaidAmount", paidamount)
								                .executeUpdate();
                		
                		
                	}
    			}
            	opensession.beginTransaction().commit();
    			opensession.close();
            }
            
            if(fundInFlowDetails.getFundInFlowDetailsDto().size()>0) {
            	
							for(FundInFlowDetailsDto fundInFlowDetailsDto:fundInFlowDetails.getFundInFlowDetailsDto()){
							
						       FundInFlowDetails fundInFlowDetail = new FundInFlowDetails();
						     if( fundInFlowDetails.getFundInFlowDetailsDto().get(0).getTrustId()!=null){
						    	 fundInFlowDetail.setFundInFlowId(FundInFlowMaster.getFundInFlowId());
						    	 fundInFlowDetail.setPaymentModeId(fundInFlowDetailsDto.getPaymentModeId());
						    	 fundInFlowDetail.setBankId(fundInFlowDetailsDto.getBankId());
									 if(fundInFlowDetailsDto.getPaymentModeId()==1){
										 fundInFlowDetail.setCashAmount(fundInFlowDetailsDto.getCashAmount());
										
									 }
									 if(fundInFlowDetailsDto.getPaymentModeId()==2){
										 fundInFlowDetail.setChequeAmount(fundInFlowDetailsDto.getChequeAmount());
										 fundInFlowDetail.setChequeNumber(fundInFlowDetailsDto.getChequeNumber());
										 fundInFlowDetail.setChequeDate(fundInFlowDetailsDto.getChequeDate());
									 }
									 if(fundInFlowDetailsDto.getPaymentModeId()==3){
										 fundInFlowDetail.setCardHolderName(fundInFlowDetailsDto.getCardHolderName());
										 fundInFlowDetail.setCardTypeId(fundInFlowDetailsDto.getCardTypeId());
										 fundInFlowDetail.setCardNumber(fundInFlowDetailsDto.getCardNo());
										 fundInFlowDetail.setExpiryDate(GlobalCommonDateUtils.getDate(fundInFlowDetailsDto.getExpiryDate(), "dd-M-yyyy HH:mm:ss"));
									 fundInFlowDetail.setCardAmount(fundInFlowDetailsDto.getCardAmount());
									 }
									 if(fundInFlowDetailsDto.getPaymentModeId()==8){
										 fundInFlowDetail.setTransactionNo(fundInFlowDetailsDto.getTransactionNo());
										 fundInFlowDetail.setTransactionTypeId(fundInFlowDetailsDto.getTransactionTypeId());
										 fundInFlowDetail.setTransTransactionDate(GlobalCommonDateUtils.getDate(fundInFlowDetailsDto.getTransTransactionDate(), "dd-M-yyyy HH:mm:ss"));
										 fundInFlowDetail.setTransferAmount(fundInFlowDetailsDto.getTransferAmount());
									 }
									 fundInFlowDetail.setAccountNumber(fundInFlowDetailsDto.getAccountNumber());
									 fundInFlowDetail.setAmount(fundInFlowDetailsDto.getAmount());
									 fundInFlowDetail.setIsCancle(fundInFlowDetailsDto.getIsCancle());
									 fundInFlowDetail.setInterimCardStatus("A");
									 fundInFlowDetail.setInterimChequeStatus("A");
									 fundInFlowDetail.setUnitId(fundInFlowDetailsDto.getUnitId());
									 fundInFlowDetail.setOrgId(fundInFlowDetailsDto.getOrgId());
									 fundInFlowDetail.setCreatedBy(fundInFlowDetails.getCreatedBy());
									 fundInFlowDetail.setStatus('A');
									 fundInFlowDetail.setCreatedDate(TimeZoneComponent.getDateByzone(fundInFlowDetails.getFundInFlowDetailsDto().get(0).getUnitId()));
									 fundInFlowDetail.setUpdatedBy(fundInFlowDetails.getCreatedBy());
									 fundInFlowDetail.setUpdatedDate(TimeZoneComponent.getDateByzone(fundInFlowDetails.getFundInFlowDetailsDto().get(0).getUnitId()));
									 sessionFactory.getCurrentSession().save(fundInFlowDetail);
									}
						         } 
		      }
				return new Response<>(SUCCESS, null, null, null, null);
	}   catch(Exception e){
		throw e;
	}
}

	@Override
	public Response getCollectionList(Integer orgId, Integer unitId,FundInFlowDetailsDto fundInFlowDetailsDto) throws ApplicationException {
		try {
			if(fundInFlowDetailsDto.getTrustId()!=null) {
			List<FundInFlowDetailsDto> fundcolletionList = sessionFactory.getCurrentSession()
					.createSQLQuery(" SELECT  tm.trust_name as \"trustName\",bm.bank_name as \"bankName\", "
							+ " fd.payment_mode_id as \"paymentModeId\", fd.amount as \"amount\","
							+ " fd.bank_id as \"bankId\", fd.cash_amount as \"cashAmount\", "
							+ " fd.fund_inflow_detail_id as \"fundInFlowDetailsId\" , fd.cheque_amount as \"chequeAmount\" , fd.check_number as \"chequeNumber\", fd.cheque_date as \"chequeDate\","
							+ " fd.created_date as \"creditDate\" ,fm.totalcollectedamount as \"totalCollectedAmount\" "
							+ " FROM billing.m_fund_inflow_master fm"
							+ " join billing.m_fund_inflow_details fd on  fm.fund_inflow_id =fd.fund_inflow_id"
							+ " join billing.m_trust_master tm on fm.trust_id =tm.trust_id"
							+ " left join billing.m_bank_master bm on fd.bank_id=bm.bank_id"
							+ " where fm.org_id=:orgId "
							+ " AND fm.unit_id=:unitId "
							+ " AND fm.trust_id=:trustId "
							+ " AND (to_char(fd.created_date, 'YYYY-MM-DD') between :fromDate AND :toDate )"
							+ " order by fd.fund_inflow_detail_id desc")
						.setParameter("orgId", orgId)
					    .setParameter("unitId", unitId)
					    .setParameter("trustId", fundInFlowDetailsDto.getTrustId())
						.setParameter("fromDate", fundInFlowDetailsDto.getFromDate())
						.setParameter("toDate", fundInFlowDetailsDto.getToDate())
					    .setResultTransformer(Transformers.aliasToBean(FundInFlowDetailsDto.class)).list();
			return new Response(SUCCESS, null, null, fundcolletionList, null);
			}
			return new Response(ERROR, null, null, null, null);
		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getFundCollectionListByTrust(Integer orgId, Integer unitId,
			FundInFlowDetailsDto fundInFlowDetailsDto) throws ApplicationException {
		try {
			if(fundInFlowDetailsDto.getTrustId()!=null) {
			List<FundInFlowDetailsDto> fundcolletionList = sessionFactory.getCurrentSession()
					.createSQLQuery(" SELECT  tm.company_desc as \"trustName\",bm.bank_name as \"bankName\", "
							+ " fd.payment_mode_id as \"paymentModeId\", fd.amount as \"amount\","
							+ " fd.bank_id as \"bankId\", fd.cash_amount as \"cashAmount\", "
							+ " fd.fund_inflow_detail_id as \"fundInFlowDetailsId\" , fd.cheque_amount as \"chequeAmount\" , fd.check_number as \"chequeNumber\", fd.cheque_date as \"chequeDate\","
							+ " fd.created_date as \"creditDate\" , fm.totalcollectedamount as \"totalCollectedAmount\" "
							+ " FROM billing.m_fund_inflow_master fm"
							+ " join billing.m_fund_inflow_details fd on  fm.fund_inflow_id =fd.fund_inflow_id"
							+ " join public.t_company_master tm on fm.trust_id =tm.company_id"
							+ " left join billing.m_bank_master bm on fd.bank_id=bm.bank_id"
							+ " where fm.org_id=:orgId "
							+ " AND fm.unit_id=:unitId "
							+ " AND fm.trust_id=:trustId "
							+ " AND (to_char(fd.created_date, 'YYYY-MM-DD') between :fromDate AND :toDate )"
							+ " order by fd.fund_inflow_detail_id desc")
						.setParameter("orgId", orgId)
					    .setParameter("unitId", unitId)
					    .setParameter("trustId", fundInFlowDetailsDto.getTrustId())
						.setParameter("fromDate", fundInFlowDetailsDto.getFromDate())
						.setParameter("toDate", fundInFlowDetailsDto.getToDate())
					    .setResultTransformer(Transformers.aliasToBean(FundInFlowDetailsDto.class)).list();
			return new Response(SUCCESS, null, null, fundcolletionList, null);
			}
			return new Response(ERROR, null, null, null, null);
		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getfundInflowDetailsByTrust(FundInFlowDetailsDto fundInFlowDetailsDto) throws ApplicationException {
		try {
			List<FundInFlowDetailsDto> fundcolletionList = 	sessionFactory.getCurrentSession()
					        .getNamedQuery("GET_ACTIVE_FUNDINFLOW_BY_TRUST_ID")
					        .setParameter("trustId", fundInFlowDetailsDto.getTrustId())
							.setParameter("orgId", fundInFlowDetailsDto.getOrgId())
						    .setParameter("unitId", fundInFlowDetailsDto.getUnitId())
							.setResultTransformer(Transformers.aliasToBean(FundInFlowDetailsDto.class)).list();
			return new Response(SUCCESS, null, null, fundcolletionList, null);

		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}



	@Override
	public Response getAllfundInflowDetailsByTrust(FundInFlowDetailsDto fundInFlowDetailsDto) throws ApplicationException {
		try {
			List<FundInFlowDetailsDto> fundcolletionList = 	sessionFactory.getCurrentSession()
					        .getNamedQuery("GET_ACTIVE_ALL_FUND_INFLOWDETAILS_BY_TRUST_ID")
					        .setParameter("trustId", fundInFlowDetailsDto.getTrustId())
							.setParameter("orgId", fundInFlowDetailsDto.getOrgId())
						    .setParameter("unitId", fundInFlowDetailsDto.getUnitId())
							.setResultTransformer(Transformers.aliasToBean(FundInFlowDetailsDto.class)).list();
			return new Response(SUCCESS, null, null, fundcolletionList, null);

		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response getInflowDetailsByFundInflowId(FundInFlowDetailsDto fundInFlowDetailsDto) throws ApplicationException {
		try {
			List<FundInFlowDetailsDto> fundcolletionList = 	sessionFactory.getCurrentSession()
					        .getNamedQuery("GET_FUND_INFLOWDETAILS_BY_FUNDINFLOW_ID")
					        .setParameter("fundInFlowId", fundInFlowDetailsDto.getFundInFlowId())
							.setParameter("orgId", fundInFlowDetailsDto.getOrgId())
						    .setParameter("unitId", fundInFlowDetailsDto.getUnitId())
							.setResultTransformer(Transformers.aliasToBean(FundInFlowDetailsDto.class)).list();
			return new Response(SUCCESS, null, null, fundcolletionList, null);

		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
}
