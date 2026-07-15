package com.param.global.dao;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import com.param.global.common.BillingCommonMethods;
import com.param.global.common.ServiceException;
import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.billing.global.model.VistContractCapMapper;
import com.param.billing.global.model.VistContractCapMapperCostEstimation;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.PatientSponsorDetailsDto;
import com.param.global.model.PatientSponsorDetails;
import com.param.global.model.PatientSponsorForCostEstimation;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;


@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class PatientSponsorDetailsDaoImpl extends GenericDao<PatientSponsorDetails, Integer>implements IPatientSponsorDetailsDao,ICommonConstants{

	private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(PatientSponsorDetailsDaoImpl.class);

	public PatientSponsorDetailsDaoImpl() {
		super(PatientSponsorDetails.class);
		// TODO Auto-generated constructor stub
	}

	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	DozerBeanMapper mapper;

	@Autowired
	private BillingCommonMethods billingCommonMethods;
	

	@Override
	public Response savePatientSponsorDetails(PatientSponsorDetailsDto dto)
			throws ApplicationException {
		try {
			Date newDate = TimeZoneComponent.getDateByzone(dto.getUnitId());
			PatientSponsorDetails patientSponsorDetails = null;
			String aaplicableDate = dto.getApplicableDate()!=null ? dto.getApplicableDate() : GlobalCommonDateUtils.getStringDate(newDate, "dd-MM-yyyy HH:mm:ss");
			dto.setApplicableDate(aaplicableDate); 
			if(dto.getPatientSponsorDetailsId()==null)
			{
				patientSponsorDetails = mapper.map(dto,PatientSponsorDetails.class, "PatientSponsorDetailsDto_to_PatientSponsorDetails");
				 	if(patientSponsorDetails.getTariffId()==null && patientSponsorDetails.getContractId()!=null)
				 		patientSponsorDetails.setTariffId(getTariffByContractId(patientSponsorDetails.getContractId()));
				    patientSponsorDetails.setEncounterId(dto.getEncounterId()!=null && dto.getEncounterId()>0?dto.getEncounterId():null);
				    patientSponsorDetails.setAdmissionId(dto.getAdmissionId()!=null && dto.getAdmissionId()>0?dto.getAdmissionId():null);
					patientSponsorDetails.setCreatedDate(newDate);
					patientSponsorDetails.setUpdatedDate(newDate);
					patientSponsorDetails.setStatus('A');
				save(patientSponsorDetails);
			}
			else {
				patientSponsorDetails = findById(dto.getPatientSponsorDetailsId());
				if(patientSponsorDetails.getTariffId()==null && patientSponsorDetails.getContractId()!=null)
			 		patientSponsorDetails.setTariffId(getTariffByContractId(patientSponsorDetails.getContractId()));
				patientSponsorDetails.setPriorityId(dto.getPriorityId());
				patientSponsorDetails.setPatientId(dto.getPatientId());
				patientSponsorDetails.setEncounterId(dto.getEncounterId()!=null && dto.getEncounterId()>0?dto.getEncounterId():null);
				patientSponsorDetails.setAdmissionId(dto.getAdmissionId()!=null && dto.getAdmissionId()>0?dto.getAdmissionId():null);
				patientSponsorDetails.setUpdatedDate(TimeZoneComponent.getDateByzone(patientSponsorDetails.getUnitId()));
				patientSponsorDetails.setStatus(dto.getStatus());
				update(patientSponsorDetails);
			}
		
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, patientSponsorDetails.getPatientSponsorDetailsId());
		} catch (HibernateException he) {
			logger.error("savePatientSponsorDetails failed", he);
		} catch (Exception e) {
			logger.error("savePatientSponsorDetails failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}


	private Integer getTariffByContractId(Integer contractId) {
		try{
			
			String query ="select tariff_id from service.m_company_contract_master mccm where contract_id =:contractId limit 1";

			return (Integer)sessionFactory.getCurrentSession().createSQLQuery(query).setParameter("contractId",contractId).uniqueResult();
		}catch(Exception e){
			logger.error("getTariffByContractId failed", e);
		}
		return 0;
	}


	@Override
	public Response getSponsorDetailsByEncounterId(int encounterId) throws ApplicationException {
		try {

			List<PatientSponsorDetailsDto> patientSponsorDetailsDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SPONSOR_DETAILS_BY_ENCOUNTER_ID")
					.setParameter("encounterId", encounterId)
					.setResultTransformer(Transformers.aliasToBean(PatientSponsorDetailsDto.class)).list();
			return new Response(SUCCESS, null, null, patientSponsorDetailsDtosList, null);
		} catch (HibernateException he) {
			logger.error("getSponsorDetailsByEncounterId failed", he);
		} catch (Exception e) {
			logger.error("getSponsorDetailsByEncounterId failed", e);
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response updatePatientSponsorDetails(PatientSponsorDetailsDto patientSponsorDetailsDto)
			throws ApplicationException {
		try{
			PatientSponsorDetails patientSponsorDetails = findById(patientSponsorDetailsDto.getPatientSponsorDetailsId());
			if (patientSponsorDetails.getPatientSponsorDetailsId()!= 0)
			{
				Integer tempPriorityId=patientSponsorDetailsDto.getPriorityId()+1;
				patientSponsorDetails.setPriorityId(tempPriorityId);
				patientSponsorDetails = update(patientSponsorDetails);
				return new Response<>(SUCCESS, SUCCESS_CODE, null, null, patientSponsorDetails);
			}else {
				
				return new Response<>(ERROR, ERROR, COMMON_ERROR_MESSAGE, null, patientSponsorDetails);
			}

		}catch(Exception e){
			logger.error("updatePatientSponsorDetails failed", e);
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getCapDetailsByContractIds(List<Integer> contractIds) throws ApplicationException {
		try {
			
			//For Serivce
		    	List<PatientSponsorDetailsDto> patientSponsorDetailsDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CAP_AMOUNTS_BY_CONTRACT_ID")
					.setParameterList("contractId", contractIds)
					.setResultTransformer(Transformers.aliasToBean(PatientSponsorDetailsDto.class)).list();
		    	
		    //For Proceudre	
		    	List<PatientSponsorDetailsDto> procedureList = sessionFactory.getCurrentSession()
						.getNamedQuery("GET_CAP_AMOUNTS_BY_CONTRACT_ID_FOR_PROCEDURE")
						.setParameterList("contractId", contractIds)
						.setResultTransformer(Transformers.aliasToBean(PatientSponsorDetailsDto.class)).list();
		    	
		    	if(procedureList!=null && procedureList.size()>0)
		    	{
		    		procedureList.forEach(proc->proc.setApplicableDays(365));
		    		procedureList.forEach(proc->proc.setTillDischarge(true));
		    		patientSponsorDetailsDtosList.addAll(procedureList);
		    	}
			return new Response(SUCCESS, null, null, patientSponsorDetailsDtosList, null);
		} catch (HibernateException he) {
			logger.error("getCapDetailsByContractIds failed", he);
		} catch (Exception e) {
			logger.error("getCapDetailsByContractIds failed", e);
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response saveCapDetailsByPatientAndContractId(List<PatientSponsorDetailsDto> listObject, Integer encounterId, Integer admissionId)
			throws ApplicationException {
		try {

			ListIterator<PatientSponsorDetailsDto> iterator = listObject.listIterator();
			while (iterator.hasNext()) {
				PatientSponsorDetailsDto patientSponsorDetailsDto = iterator.next();
				VistContractCapMapper vistContractCapMapper = new VistContractCapMapper();
				vistContractCapMapper.setOrgId(patientSponsorDetailsDto.getOrganizationId());
				vistContractCapMapper.setUnitid(patientSponsorDetailsDto.getUnitId());
				vistContractCapMapper.setEncounterId(encounterId != null && encounterId > 0 ? encounterId : null);
				vistContractCapMapper.setAdmissionId(admissionId != null && admissionId > 0 ? admissionId : null);
				vistContractCapMapper.setContractId(patientSponsorDetailsDto.getContractId());
				vistContractCapMapper.setDepartmentId(patientSponsorDetailsDto.getDepartmentId());
				vistContractCapMapper.setSubDepartmentId(patientSponsorDetailsDto.getSubDepartmentId());
				vistContractCapMapper.setDepartmentCapAmount(patientSponsorDetailsDto.getDepartmentCapAmount());
				vistContractCapMapper.setBalanceCapAmount(patientSponsorDetailsDto.getDepartmentCapAmount());
				vistContractCapMapper.setProductCategoryId(patientSponsorDetailsDto.getProductCategoryId());
				if (patientSponsorDetailsDto.getQuantity() != null && patientSponsorDetailsDto.getQuantity() > 0) {
					vistContractCapMapper.setQuantity(patientSponsorDetailsDto.getQuantity());
					vistContractCapMapper.setBalanceQuantity(patientSponsorDetailsDto.getQuantity());
				}
				if (patientSponsorDetailsDto.getAmount() != null && patientSponsorDetailsDto.getAmount() > 0) {
					vistContractCapMapper.setAmount(patientSponsorDetailsDto.getAmount());
					vistContractCapMapper.setBalanceAmount(patientSponsorDetailsDto.getAmount());
				}
				vistContractCapMapper.setPatientId(patientSponsorDetailsDto.getPatientId());
				vistContractCapMapper.setServiceId(patientSponsorDetailsDto.getServiceId());
				if (patientSponsorDetailsDto.getTillDischarge() != null && !patientSponsorDetailsDto.getTillDischarge())
					vistContractCapMapper.setApplicableDays(patientSponsorDetailsDto.getApplicableDays());

				vistContractCapMapper.setTillDischarge(patientSponsorDetailsDto.getTillDischarge());
				vistContractCapMapper.setStatus('A');
				vistContractCapMapper.setPatientSponsorSetailsId(patientSponsorDetailsDto.getPatientSponsorDetailsId());
				if (patientSponsorDetailsDto.getServiceId() != null)
					vistContractCapMapper.setApplicableDate(TimeZoneComponent.getDateByzone(patientSponsorDetailsDto.getUnitId()));
				sessionFactory.getCurrentSession().save(vistContractCapMapper);
			}
			//	listObject.forEach(patientSponsorDetailsDto -> sessionFactory.getCurrentSession().save(billingCommonMethods.getVisitContractCapMapper(patientSponsorDetailsDto,encounterId,admissionId)));
			return new Response(SUCCESS, null, null, null, null);
		} catch (HibernateException he) {
			logger.error("saveCapDetailsByPatientAndContractId failed", he);
		} catch (Exception e) {
			logger.error("saveCapDetailsByPatientAndContractId failed", e);
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getSponsoredDetails(Integer admissionId, Integer paymentEntitlementId) {
		try {
			List<PatientSponsorDetailsDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SPONSOR_DETAILS_BY_ADMISSION")
					.setParameter("admissionId", admissionId)
					.setParameter("paymentEntitlementId", paymentEntitlementId)
					.setResultTransformer(Transformers.aliasToBean(PatientSponsorDetailsDto.class)).list();
			return new Response(SUCCESS, null, null, list, null);
		} catch (HibernateException he) {
			logger.error("getSponsoredDetails failed", he);
		} catch (Exception e) {
			logger.error("getSponsoredDetails failed", e);
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getSponsoredDetailsForUpdatePayee(Integer admissionId,Integer encounterId) {
		try {
			
			StringBuilder query = new StringBuilder(sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SPONSOR_DETAILS_BY_ADMISSION_FOR_UPDATE_PAYEE").getQueryString());
			
			if(admissionId!=null && admissionId>0)
			{
				query.append(" 	AND dtls.admission_id =:admissionId ");
			}
			if(encounterId!=null && encounterId>0)
			{
				query.append(" AND	dtls.encounter_id =:encounterId ");
			}
			
			Query creteQuery = sessionFactory.getCurrentSession()
					.createSQLQuery(query.toString());
			if(admissionId!=null && admissionId>0)
			{
				creteQuery.setParameter("admissionId",admissionId);
			}
			if(encounterId!=null && encounterId>0)
			{
				creteQuery.setParameter("encounterId",encounterId);
			}

			List<PatientSponsorDetailsDto> list = 	creteQuery.setResultTransformer(Transformers.aliasToBean(PatientSponsorDetailsDto.class)).list();
			return new Response(SUCCESS, null, null, list, null);
		} catch (HibernateException he) {
			logger.error("getSponsoredDetailsForUpdatePayee failed", he);
		} catch (Exception e) {
			logger.error("getSponsoredDetailsForUpdatePayee failed", e);
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response inactiveteSponsoredDetails(Integer patientId,List<Integer> sponsorIds) {
		try {
			Integer count = 0;
			if(sponsorIds != null && sponsorIds.size() > 0) {
				Integer ResultCount= sessionFactory.getCurrentSession()
					.createSQLQuery(" update "
							+ " opd.t_patient_sponsor_details "
							+ " set "
							+ "	status = 'I' "
							+ " where "
							+ "	patient_id = :patientId "
							+ "	and admission_id is null "
							+ "	and encounter_id is null "
							+ " and patient_sponsor_details_id not in (:sponsorIds)")
					.setParameter("patientId", patientId)
					.setParameterList("sponsorIds", sponsorIds)
					.executeUpdate();
			
			 count= sessionFactory.getCurrentSession()
					.createSQLQuery(" update "
							+ " service.t_vist_contract_cap_mapper "
							+ " set "
							+ "	status = 'I' "
							+ " where "
							+ "	patient_id = :patientId "
							+ "	and admission_id is null "
							+ "	and encounter_id is null "
							+ " and patient_sponsor_details_id not in (:sponsorIds) ")
					.setParameter("patientId", patientId)
					.setParameterList("sponsorIds", sponsorIds)
					.executeUpdate();
			}else {
				Integer ResultCount= sessionFactory.getCurrentSession()
						.createSQLQuery(" update "
								+ " opd.t_patient_sponsor_details "
								+ " set "
								+ "	status = 'I' "
								+ " where "
								+ "	patient_id = :patientId "
								+ "	and admission_id is null "
								+ "	and encounter_id is null ")
						.setParameter("patientId", patientId)
						.executeUpdate();
				
				 count= sessionFactory.getCurrentSession()
						.createSQLQuery(" update "
								+ " service.t_vist_contract_cap_mapper "
								+ " set "
								+ "	status = 'I' "
								+ " where "
								+ "	patient_id = :patientId "
								+ "	and admission_id is null "
								+ "	and encounter_id is null ")
						.setParameter("patientId", patientId)
						.executeUpdate();
			}
			return new Response(SUCCESS, SUCCESS_CODE, null, null, count);
		} catch (HibernateException he) {
			logger.error("inactiveteSponsoredDetails failed", he);
		} catch (Exception e) {
			logger.error("inactiveteSponsoredDetails failed", e);
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response inactiveSponsorDetailsForUpdatePayee(Integer admissionId,Integer encounterId) {
		try {

            Integer ResultCount;
            Integer count;
            if(admissionId!=null && admissionId > 0){
                ResultCount = sessionFactory.getCurrentSession()
                        .createSQLQuery(" update "
                                + " opd.t_patient_sponsor_details "
                                + " set "
                                + "	status = 'I' "
                                + " where "
                                + "	admission_id = :admissionId "
                                + "	and status='A' ")
                        .setParameter("admissionId", admissionId)
                        .executeUpdate();

                count = sessionFactory.getCurrentSession()
                        .createSQLQuery(" update "
                                + " service.t_vist_contract_cap_mapper "
                                + " set "
                                + "	status = 'I' "
                                + " where "
                                + "	admission_id = :admissionId "
                                + "	and status='A' ")
                        .setParameter("admissionId", admissionId).executeUpdate();
            }else{
                ResultCount = sessionFactory.getCurrentSession()
                        .createSQLQuery(" update "
                                + " opd.t_patient_sponsor_details "
                                + " set "
                                + "	status = 'I' "
                                + " where "
                                + "	encounter_id = :encounterId "
                                + "	and status='A' ")
                        .setParameter("encounterId", encounterId)
                        .executeUpdate();

                count = sessionFactory.getCurrentSession()
                        .createSQLQuery(" update "
                                + " service.t_vist_contract_cap_mapper "
                                + " set "
                                + "	status = 'I' "
                                + " where "
                                + "	encounter_id = :encounterId "
                                + "	and status='A' ")
                        .setParameter("encounterId", encounterId).executeUpdate();
				
				
				 sessionFactory.getCurrentSession()
						.createSQLQuery(" update t_order_details set  patient_coupon_template_mapper_id = null WHERE order_details_id in(select order_details_id from t_order_details tod  " + 
								"	 inner join t_order_master tom on tom.order_id = tod.order_id  " + 
								"	 where encounter_id = :encounterId)   ")
						.setParameter("encounterId", encounterId).executeUpdate();

            }
            return new Response(SUCCESS, SUCCESS_CODE, null, null,ResultCount);


        } catch (HibernateException he) {
			logger.error("inactiveSponsorDetailsForUpdatePayee failed", he);
		} catch (Exception e) {
			logger.error("inactiveSponsorDetailsForUpdatePayee failed", e);
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public List<PatientSponsorDetails> findByAdmissionId(Integer admissionId) {
		String hql = "SELECT detail FROM PatientSponsorDetails AS detail "
				+ "	INNER JOIN detail.admission AS admission WITH admission.admissionId = :admissionId "
				+ " WHERE detail.status='A' and detail.priorityId = 1 "
				+ "	ORDER BY detail.priorityId ";
		Query query = getSession().createQuery(hql);
		
		query.setParameter("admissionId", admissionId);

		return query.list();
	}


	@Override
	public List<PatientSponsorDetails> findByEncounterId(Integer encounterId) {
		String hql = "SELECT detail FROM PatientSponsorDetails AS detail "
				+ "	INNER JOIN detail.encounterMaster AS encounter "
				+ "	WHERE detail.status='A' and encounter.encounterId = :encounterId and detail.priorityId = 1"
				+ "	ORDER BY detail.priorityId ";
		Query query = getSession().createQuery(hql);
		
		query.setParameter("encounterId", encounterId);

		return query.list();
	}


	@Override
	public Response savePatientSponsorDetailsForCostEstimation(PatientSponsorDetailsDto patientSponsorDetailsDto) {
		try {
				PatientSponsorForCostEstimation patientSponsorDetails = mapper.map(patientSponsorDetailsDto,
						PatientSponsorForCostEstimation.class, "PatientSponsorDetailsDto_to_PatientSponsorForCostEstimation");
					patientSponsorDetails.setCreatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(patientSponsorDetailsDto.getUnitId()), "yyyy-MM-dd HH:mm:ss"));
					patientSponsorDetails.setCreatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(patientSponsorDetailsDto.getUnitId()), "yyyy-MM-dd HH:mm:ss"));
				sessionFactory.getCurrentSession().save(patientSponsorDetails);
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (HibernateException he) {
			logger.error("savePatientSponsorDetailsForCostEstimation failed", he);
		} catch (Exception e) {
			logger.error("savePatientSponsorDetailsForCostEstimation failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}


	@Override
	public Response saveCapDetailsByPatientAndContractIdForCostEstimation(List<PatientSponsorDetailsDto> listObject) {
		try {
		
		ListIterator<PatientSponsorDetailsDto> iterator = listObject.listIterator();
		while(iterator.hasNext())
		{
			PatientSponsorDetailsDto patientSponsorDetailsDto = iterator.next();
			
			VistContractCapMapperCostEstimation vistContractCapMapper = new VistContractCapMapperCostEstimation();
				vistContractCapMapper.setOrgId(patientSponsorDetailsDto.getOrganizationId());
				vistContractCapMapper.setUnitid(patientSponsorDetailsDto.getUnitId());
				vistContractCapMapper.setContractId(patientSponsorDetailsDto.getContractId());
				vistContractCapMapper.setDepartmentId(patientSponsorDetailsDto.getDepartmentId());
				vistContractCapMapper.setSubDepartmentId(patientSponsorDetailsDto.getSubDepartmentId());
				vistContractCapMapper.setDepartmentCapAmount(patientSponsorDetailsDto.getDepartmentCapAmount());
				vistContractCapMapper.setBalanceCapAmount(patientSponsorDetailsDto.getDepartmentCapAmount());
				vistContractCapMapper.setProductCategoryId(patientSponsorDetailsDto.getProductCategoryId());
				if(patientSponsorDetailsDto.getQuantity()!=null && patientSponsorDetailsDto.getQuantity()>0)
				{
					vistContractCapMapper.setQuantity(patientSponsorDetailsDto.getQuantity());
					vistContractCapMapper.setBalanceQuantity(patientSponsorDetailsDto.getQuantity());
				}
				if(patientSponsorDetailsDto.getAmount()!=null && patientSponsorDetailsDto.getAmount()>0)
				{
					vistContractCapMapper.setAmount(patientSponsorDetailsDto.getAmount());
					vistContractCapMapper.setBalanceAmount(patientSponsorDetailsDto.getAmount());
				}
				vistContractCapMapper.setPatientId(patientSponsorDetailsDto.getPatientId());
				vistContractCapMapper.setServiceId(patientSponsorDetailsDto.getServiceId());
				if(patientSponsorDetailsDto.getTillDischarge()==false)
					vistContractCapMapper.setApplicableDays(patientSponsorDetailsDto.getApplicableDays());
				
				vistContractCapMapper.setTillDischarge(patientSponsorDetailsDto.getTillDischarge());
				vistContractCapMapper.setStatus('A');
				if(patientSponsorDetailsDto.getServiceId()!=null)
					vistContractCapMapper.setApplicableDate(TimeZoneComponent.getDateByzone(patientSponsorDetailsDto.getUnitId()));
			sessionFactory.getCurrentSession().save(vistContractCapMapper);
			
		}
		return new Response(SUCCESS, null, null, null, null);
	} catch (HibernateException he) {
		logger.error("saveCapDetailsByPatientAndContractIdForCostEstimation failed", he);
	} catch (Exception e) {
		logger.error("saveCapDetailsByPatientAndContractIdForCostEstimation failed", e);
	}
	return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response inactivePatientSponsors(Integer patientId) {
		try {
			
			sessionFactory.getCurrentSession()
					.createSQLQuery(
							"update opd.t_patient_sponsor_details_cost_estimation "
							+ "set status='I' "
							+ "where status='A' "
							+ "and patient_id =:patientId ")
					.setParameter("patientId",patientId)
					.executeUpdate();
			
			sessionFactory.getCurrentSession()
			.createSQLQuery(
					"update service.t_vist_contract_cap_mapper_cost_estimation "
					+ "set status='I' "
					+ "where status='A' "
					+ "and patient_id =:patientId ")
					.setParameter("patientId",patientId)
			.executeUpdate();
			
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
	} catch (HibernateException he) {
		logger.error("inactivePatientSponsors failed", he);
	} catch (Exception e) {
		logger.error("inactivePatientSponsors failed", e);
	}
	return new Response(ERROR, null, null, null, null);
	}


	@Override
	public Response getLastPatientClass(Integer patientId,Integer visitTypeId, Integer unitId) {
		try {
			
			String query = null;
			if(visitTypeId==1)
			{
				query = "select " 
						 + " 	tem.encounter_id as \"visitId\", " 
						 + " 	patient_class_id as \"patientClassId\", " 
						 + " 	to_char(tem.created_date,'YYYY-MM-DD') as \"date\", " 
						 + " 	1 as \"type\" " 
						 + " from " 
						 + " 	opd.t_encounter_master tem " 
						 + " where " 
						 + " 	patient_id =:patientId "
						 + " 	and unit_id =:unitId "
						 + " order by 3 desc " 
						 + " limit 1 ";
			}else{
				query = " select " 
						 + " 	ta.admission_id as \"visitId\", " 
						 + " 	patient_class_id as \"patientClassId\", " 
						 + " 	to_char(ta.created_date,'YYYY-MM-DD') as \"date\", " 
						 + " 	2 as \"type\" " 
						 + " from " 
						 + " 	adt.t_admission ta " 
						 + " where " 
						 + " 	patient_id =:patientId "
						 + " 	and visit_type_id =:visitTypeId "
						 + " 	and unit_id =:unitId  "
						 + " order by 3 desc " 
						 + " limit 1 ";
			}
			
			
			
			Query createQur = sessionFactory.getCurrentSession()
					.createSQLQuery(query);

			if(visitTypeId==1)
			{
				createQur.setParameter("patientId",patientId);
				createQur.setParameter("unitId",unitId);
			}else{
				createQur.setParameter("patientId",patientId);
				createQur.setParameter("visitTypeId",visitTypeId);
				createQur.setParameter("unitId",unitId);
			}
			PatientSponsorDetailsDto dto = (PatientSponsorDetailsDto)createQur .setResultTransformer(Transformers.aliasToBean(PatientSponsorDetailsDto.class)).uniqueResult();
			
			if(dto!=null && dto.getVisitId()>0)
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, dto);
			else
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
	} catch (HibernateException he) {
		logger.error("getLastPatientClass failed", he);
	} catch (Exception e) {
		logger.error("getLastPatientClass failed", e);
	}
	return new Response(ERROR, null, null, null, null);
	}


	@Override
	public Response getLastSponsors(Integer visitId, Integer type) {
		try {
			
			StringBuffer query = new StringBuffer(" select " 
                     + "     tpsd.payment_entitlement_id as \"paymentEntitlementId\", " 
					 + " 	tpsd.patient_sponsor_details_id as \"patientSponsorDetailsId\", " 
					 + " 	payEnt.payment_entitlement_desc as \"paymentEntitlementDesc\", " 
					 + " 	(case " 
					 + " 		when comCon.status = 'A' then  " 
					 + " 			comCon.contract_id " 
					 + " 		when comCon.status = 'I' then  " 
					 + " 			( " 
					 + " 		select " 
					 + " 				mccm.contract_id " 
					 + " 		from " 
					 + " 				service.m_company_contract_master mccm " 
					 + " 		where " 
					 + " 				mccm.parent_contract_id = comCon.parent_contract_id " 
					 + " 			and status = 'A' " 
					 + " 		order by " 
					 + " 				contract_id desc " 
					 + " 		limit 1) " 
					 + " 	end) as \"contractId\", " 
					 + " 	comCon.contract_name as \"contractName\", " 
					 + " 	comMas.company_desc as \"companyDesc\", " 
					 + " 	tpsd.priority_id as \"priorityId\", " 
					 + " 	tpsd.payee_id as \"companyId\", " 
					 + " 	tpsd.claim_number as \"claimNumber\", " 
					 + " 	cast(tpsd.insured_amount as numeric(10, 2)) as \"insuredAmt\", " 
					 + " 	tpsd.tariff_id as \"tariffId\", " 
					 + " 	to_char(comCon.validity_from, 'DD-MM-YYYY HH:mm:ss') as \"validityFrom\", " 
					 + " 	to_char(comCon.validity_to, 'DD-MM-YYYY HH:mm:ss') as \"validityTo\", " 
					 + " 	tariff.tariff_desc as \"tariffDesc\", " 
					 + " 	tpsd.payee_id as \"payeeId\", " 
					 + " 	insu.insurance_desc as \"insuranceDesc\", " 
					 + " 	tpsd.insurance_number as \"insuranceNumber\", " 
					 + " 	tpsd.insurance_id as \"insuranceId\" " 
					 + " from " 
					 + " 	opd.t_patient_sponsor_details tpsd " 
					 + " inner join m_payment_entitlement_master payEnt on " 
					 + " 	payEnt.payment_entitlement_id = tpsd.payment_entitlement_id " 
					 + " left join service.m_company_contract_master comCon on " 
					 + " 	comCon.contract_id = tpsd.contract_id " 
					 + " 	and '"+GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(0), "yyyy-MM-dd")+"' between to_char(comCon.validity_from, 'YYYY-MM-DD') and to_char(comCon.validity_to, 'YYYY-MM-DD') " 
					 + " left join t_company_master comMas on " 
					 + " 	comMas.company_id = comCon.company_id " 
					 + " left join service.m_tariff_master tariff on " 
					 + " 	tariff.tariff_id = comCon.tariff_id " 
					 + " left join adt.t_insurance insu on " 
					 + " 	insu.insurance_id = comCon.associate_company_id " 
					 + " where " 
					 + " 	tpsd.status = 'A' " ) ;
			
			if(type==1)
				query.append(" and tpsd.encounter_id =:visitId");
			else
				query.append(" and tpsd.admission_id =:visitId");
			
			query.append(" order by tpsd.priority_id desc ");
			
			Query createQuery = sessionFactory.getCurrentSession()
					.createSQLQuery(query.toString());
				createQuery.setParameter("visitId",visitId);
			List<PatientSponsorDetailsDto> sponsorList =	createQuery.setResultTransformer(Transformers.aliasToBean(PatientSponsorDetailsDto.class)).list();
			
			List<PatientSponsorDetailsDto> finalSponsorList = new LinkedList<>();
			List<Integer> paymentEntitlementIds = new LinkedList<>();
			List<Integer> contractId = new LinkedList<>();
			
			List<Integer> priorityList = new LinkedList<>();
			for(PatientSponsorDetailsDto dto : sponsorList)
			{
				priorityList.add(dto.getPriorityId());
				if(paymentEntitlementIds.contains(dto.getPaymentEntitlementId()))
				{
					if(dto.getPaymentEntitlementId()!=1 && !(contractId.contains(dto.getContractId())))
					{
						finalSponsorList.add(dto);
						paymentEntitlementIds.add(dto.getPaymentEntitlementId());
						if(dto.getContractId()!=null)
							contractId.add(dto.getContractId());
					}
				}
				else {
					
					if((dto.getContractId()!=null && dto.getPaymentEntitlementId()!=SELF)|| dto.getPaymentEntitlementId()==SELF)
					{
						finalSponsorList.add(dto);
						paymentEntitlementIds.add(dto.getPaymentEntitlementId());
						
						if(dto.getContractId()!=null)
							contractId.add(dto.getContractId());
					}
					
				}
			}
			if(finalSponsorList.size()!=sponsorList.size())
			{
			    int N = sponsorList.size();
			    int idealSum = (N * (N + 1)) / 2;
			    int sum = priorityList.stream().reduce(0, Integer::sum);
			    int missingNumber = idealSum - sum;
				
				for(PatientSponsorDetailsDto detailsDto : finalSponsorList)
				{
					if(detailsDto.getPriorityId()>missingNumber)
						detailsDto.setPriorityId(detailsDto.getPriorityId()-1);
				}
			}
			
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, finalSponsorList, null);
	} catch (HibernateException he) {
         throw he;
	} catch (Exception e) {
		throw e;
	}
	//return new Response(ERROR, null, null, null, null);
	}


	@Override
	public String getDOAFromAdmissionId(Integer admissionId) {
		try{
			
			String query ="select to_char(ad.doa,'DD/MM/YYYY') from adt.t_admission_details ad where admission_id =:admissionId order by admission_details_id desc limit 1";
			String doa = (String)sessionFactory.getCurrentSession().createSQLQuery(query).setParameter("admissionId",admissionId).uniqueResult();
			
			return doa;
		}catch(Exception e){
			logger.error("getDOAFromAdmissionId failed", e);
		}
		return "";
	}


	@Override
	public String getVisitDateFromEncounterId(Integer encounterId) {
		try{
			
			String query ="select to_char(tem.encounter_date ,'DD/MM/YYYY') from opd.t_encounter_master tem where encounter_id =:encounterId  order by encounter_id desc limit 1";
			String visitDate = (String)sessionFactory.getCurrentSession().createSQLQuery(query).setParameter("encounterId",encounterId).uniqueResult();
			
			return visitDate;
		}catch(Exception e){
			logger.error("getVisitDateFromEncounterId failed", e);
		}
		return "";
	}


	@Override
	public Response updatePatientSponserDetailsClaimNumber(PatientSponsorDetailsDto patientSponsorDetailsDto) {
		// TODO Auto-generated method stub
		try{
			int i = sessionFactory.getCurrentSession()
					.createQuery("UPDATE PatientSponsorDetails SET claimNumber = :claimNumber WHERE patientSponsorDetailsId = :patientSponsorDetailsId")
					.setParameter("claimNumber", patientSponsorDetailsDto.getClaimNumber())
					.setParameter("patientSponsorDetailsId", patientSponsorDetailsDto.getPatientSponsorDetailsId())
					.executeUpdate();


			return new Response(SUCCESS, SUCCESS_CODE, null, null,null);
		
		
		}catch(Exception e){
			throw e;
		}
		//return null;
	}
	
}
