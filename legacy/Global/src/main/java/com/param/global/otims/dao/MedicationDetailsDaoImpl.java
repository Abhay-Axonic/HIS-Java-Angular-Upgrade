package com.param.global.otims.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.otims.dto.MedicationDetailsDto;
import com.param.opd.coversheet.model.MedicationDetails;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings({"unchecked","rawtypes","unused"})
@Repository
public class MedicationDetailsDaoImpl extends GenericDao<MedicationDetails, Integer>implements IMedicationDetailsDao, ICommonConstants{
	final static Logger logger = Logger.getLogger(MedicationDetailsDaoImpl.class);


public MedicationDetailsDaoImpl() {
	super(MedicationDetails.class);
} 

@Override
public Response saveMedicationDetails(List<MedicationDetailsDto> iMedicationDetailsDto,Integer procedureOrderId) throws ApplicationException {
	try{
	/*	Integer count=sessionFactory.getCurrentSession().createSQLQuery(" update otims.t_medication_details set status='I' "
				+ " where procedure_order_id='"+procedureOrderId+"' ")
				.executeUpdate();*/
		
		for(MedicationDetailsDto medicationList:iMedicationDetailsDto)
		{
			MedicationDetails medicationDetails = new MedicationDetails();
			medicationDetails.setPatientId(medicationList.getPatientId());
			if(medicationList.getItemId()!=null){
				medicationDetails.setItemId(medicationList.getItemId());
				}else{
					medicationDetails.setKitId(medicationList.getKitId());
				}
			medicationDetails.setOrganizationId(medicationList.getOrganizationId());
			medicationDetails.setUnitId(medicationList.getUnitId());
			medicationDetails.setCreatedBy(medicationList.getCreatedBy());
			//medicationDetails.setProcedureOrderId(procedureOrderId);
			medicationDetails.setProcedureOrderId(procedureOrderId==0?medicationList.getProcedureOrderId():procedureOrderId);
			if(medicationList.getAdmissionId()!=null){
				medicationDetails.setAdmissionId(medicationList.getAdmissionId());
			}else{
				medicationDetails.setEncounterId(medicationList.getEncounterId());

			}
			medicationDetails.setQty(medicationList.getQty());
			medicationDetails.setCreatedDate(medicationList.getCreatedDate() != null && !medicationList.getCreatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(medicationList.getCreatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(medicationList.getUnitId()));
			medicationDetails.setUpdatedBy(medicationList.getUpdatedBy());
			medicationDetails.setUpdatedDate(medicationList.getUpdatedDate() != null && !medicationList.getUpdatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(medicationList.getUpdatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(medicationList.getUnitId()));
			medicationDetails.setIsExtra(medicationList.getIsExtra()!= null ? medicationList.getIsExtra() : false);
			if(medicationList.getMedicationDetailsId()!=null) {
				medicationDetails.setMedicationDetailsId(medicationList.getMedicationDetailsId());
				medicationDetails.setStatus(medicationList.getStatus());
				sessionFactory.getCurrentSession().update(medicationDetails);
			}else {
				medicationDetails.setStatus('A');
				sessionFactory.getCurrentSession().save(medicationDetails);
			}
			}
	
		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
	} catch (HibernateException he) {
	logger.error("saveMedicationDetails failed", he);
	} catch (Exception e) {
	logger.error("saveMedicationDetails failed", e);
	}
	return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

@Override
public Response getMedicationDetailsByProcedureId(MedicationDetailsDto medicationDetailsDto)
		throws ApplicationException {
	try{
		List<MedicationDetailsDto> medicationDetailsList = sessionFactory.getCurrentSession().getNamedQuery("GET_MEDICATION_ITEMS_ON_OTIMS_DASHBOARD")
				.setParameter("procedureId", medicationDetailsDto.getProcedureOrderId())
				.setParameter("orgId", medicationDetailsDto.getOrganizationId())
				.setParameter("unitId", medicationDetailsDto.getUnitId())
				.setResultTransformer(Transformers.aliasToBean(MedicationDetailsDto.class)).list();
		return new Response<>(SUCCESS, SUCCESS_CODE, null, medicationDetailsList, null);
	}catch(Exception e){
	logger.error("getMedicationDetailsByProcedureId failed", e);
	}

	return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

@Override
public Response getMedicationDetails(MedicationDetailsDto medicationDetailsDto) throws ApplicationException {
	try{
		String query = sessionFactory.getCurrentSession()
				.getNamedQuery("GET_MEDICATION_DETAILS")
				.getQueryString();

		StringBuffer subQuery = new StringBuffer();
		Map<String, Object> parameters = new HashMap<>();

		if (medicationDetailsDto.getProcedureOrderId() != null) {
			subQuery.append(subQuery.length() != 0 ? " AND " : " WHERE ");
			subQuery.append("medication_details.procedure_order_id = :procedureOrderId");
			parameters.put("procedureOrderId", medicationDetailsDto.getProcedureOrderId());
		}

		if ((medicationDetailsDto.getFromDate() != null) && (medicationDetailsDto.getToDate() != null)) {
			subQuery.append(subQuery.length() != 0 ? " AND " : " WHERE ");
			subQuery.append("medication_details.created_date BETWEEN :fromDate AND :toDate");
			parameters.put("fromDate", medicationDetailsDto.getFromDate());
			parameters.put("toDate", medicationDetailsDto.getToDate());
		}

		if (medicationDetailsDto.getCreatedBy() != null) {
			subQuery.append(subQuery.length() != 0 ? " AND " : " WHERE ");
			subQuery.append("medication_details.created_by = :createdBy");
			parameters.put("createdBy", medicationDetailsDto.getCreatedBy());
		}

		subQuery.append(subQuery.length() != 0 ? " AND " : " WHERE ");
		subQuery.append("medication_details.organization_id = :organizationId AND medication_details.unit_id = :unitId");

		parameters.put("organizationId", medicationDetailsDto.getOrganizationId());
		parameters.put("unitId", medicationDetailsDto.getUnitId());

		query = query.concat(subQuery.toString());

		Query sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query)
				.setResultTransformer(Transformers.aliasToBean(MedicationDetailsDto.class));

// Set parameters conditionally
		for (Map.Entry<String, Object> entry : parameters.entrySet()) {
			sqlQuery.setParameter(entry.getKey(), entry.getValue());
		}

		List<MedicationDetailsDto> medicationDetailsList = sqlQuery.list();



		return new Response<>(SUCCESS, SUCCESS_CODE, null, medicationDetailsList, null);
	}catch(Exception e){
	logger.error("getMedicationDetails failed", e);
	}

	return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

@Override
public Response updateMedicationDetailsIsExported(List<MedicationDetailsDto> list) throws ApplicationException {
	try{
		for (MedicationDetailsDto medicationDetailsDto : list) {
			
			MedicationDetails mstr = findById(medicationDetailsDto.getMedicationDetailsId());
			 mstr.setUpdatedBy(medicationDetailsDto.getUpdatedBy());
			 mstr.setUpdatedDate(GlobalCommonDateUtils.getDate(medicationDetailsDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			 mstr.setIsExported(true);
			 update(mstr);
		}
		 
	return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
	}catch(Exception e){
		
		throw e;
	}
}

@Override
public Response getMedicationDetailsByProcedureIdOnOTIMS(MedicationDetailsDto medicationDetailsDto)
		throws ApplicationException {
	try{
		String query = sessionFactory.getCurrentSession()
				.getNamedQuery("GET_MEDICATION_ITEMS")
				.getQueryString();

		StringBuffer subQuery = new StringBuffer();
		Map<String, Object> parameters = new HashMap<>();

// Add conditionally the procedureOrderId parameter
		if (medicationDetailsDto.getProcedureOrderId() != null) {
			subQuery.append(subQuery.length() != 0 ? " AND " : " WHERE ");
			subQuery.append("medication_details.procedure_order_id = :procedureOrderId");
			parameters.put("procedureOrderId", medicationDetailsDto.getProcedureOrderId());
		}

// Add the organizationId, unitId, and status conditions (these are always included)
		subQuery.append(subQuery.length() != 0 ? " AND " : " WHERE ");
		subQuery.append("medication_details.organization_id = :organizationId AND medication_details.status = 'A' AND medication_details.unit_id = :unitId");

		parameters.put("organizationId", medicationDetailsDto.getOrganizationId());
		parameters.put("unitId", medicationDetailsDto.getUnitId());

		query = query.concat(subQuery.toString());

// Create SQLQuery and set parameters
		Query sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query)
				.setResultTransformer(Transformers.aliasToBean(MedicationDetailsDto.class));

// Set parameters conditionally
		for (Map.Entry<String, Object> entry : parameters.entrySet()) {
			sqlQuery.setParameter(entry.getKey(), entry.getValue());
		}

// Execute the query and get the result
		List<MedicationDetailsDto> medicationDetailsList = sqlQuery.list();


		return new Response<>(SUCCESS, SUCCESS_CODE, null, medicationDetailsList, null);
	}catch(Exception e){
	logger.error("getMedicationDetailsByProcedureIdOnOTIMS failed", e);
	}

	return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

@Override
public Response getMedicationListByProcedureId(MedicationDetailsDto medDto) throws ApplicationException {
	try{
		List<MedicationDetailsDto> medicationDetailsList = sessionFactory.getCurrentSession().getNamedQuery("GET_MEDICATION_ITEMS_ON_OT_BILLING")
				.setParameter("procedureId", medDto.getProcedureOrderId())
				.setParameter("orgId", medDto.getOrganizationId())
				.setParameter("unitId", medDto.getUnitId())
				.setResultTransformer(Transformers.aliasToBean(MedicationDetailsDto.class)).list();
		return new Response<>(SUCCESS, SUCCESS_CODE, null, medicationDetailsList, null);
	}catch(Exception e){
	logger.error("getMedicationListByProcedureId failed", e);
	}

	return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

}
