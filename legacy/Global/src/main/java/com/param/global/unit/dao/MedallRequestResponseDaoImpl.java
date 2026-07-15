package com.param.global.unit.dao;

import java.util.ArrayList;

import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.entity.lis.global.LabConfigurationMaster;
import com.param.entity.lis.global.MedallRequestResponseDetails;
import com.param.entity.lis.unit.LabMachineInterface;
import com.param.global.billing.dto.POSMachineResponseMapperDto;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.unit.dto.MedallRequestResponseDto;
import com.param.opd.coversheet.dto.FrequencyMasterDTO;
import com.param.opd.coversheet.dto.MedicationListDto;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class MedallRequestResponseDaoImpl  extends GenericDao<MedallRequestResponseDetails, Integer> implements IMedallRequestResponseDao,ICommonConstants{
	final static Logger logger = Logger.getLogger(MedallRequestResponseDaoImpl.class);


	public MedallRequestResponseDaoImpl() {
		super(MedallRequestResponseDetails.class);
	}

	@Override
	public Response saveMedallRequestResponse(MedallRequestResponseDto medallRequestResponseDto) {
		try {
			List<MedallRequestResponseDto> dtoList=new ArrayList<MedallRequestResponseDto>();
			if(medallRequestResponseDto.getOrderId() != null) {
				String query="select " + 
						"	tmrrd.order_id as \"orderId\" " + 
						"from " + 
						"	lab.t_medall_request_response_details tmrrd " + 
						"where " + 
						"	order_id =:orderId";
			 dtoList = sessionFactory.getCurrentSession()
						.createSQLQuery(query)
						.setParameter("orderId", medallRequestResponseDto.getOrderId())
						.setResultTransformer(Transformers.aliasToBean(MedallRequestResponseDto.class)).list();	
			}
			if(dtoList != null && dtoList.size() > 0) {
				Session session=sessionFactory.getCurrentSession();
				session.createSQLQuery(" update lab.t_medall_request_response_details  set status = 'I'"
				+ " where order_id=:orderId ")
						.setParameter("orderId",medallRequestResponseDto.getOrderId())
				.executeUpdate();
			}
			
			MedallRequestResponseDetails medallRequestResponseDetails=new MedallRequestResponseDetails();
			medallRequestResponseDetails.setAdmissionId(medallRequestResponseDto.getAdmissionId());
			medallRequestResponseDetails.setEncounterId(medallRequestResponseDto.getEncounterId());
			medallRequestResponseDetails.setOrderId(medallRequestResponseDto.getOrderId());
			medallRequestResponseDetails.setPatientId(medallRequestResponseDto.getPatientId());
			medallRequestResponseDetails.setMedallBillId(medallRequestResponseDto.getMedallBillId());
			medallRequestResponseDetails.setRequestedPayload(medallRequestResponseDto.getRequestedPayload());
			medallRequestResponseDetails.setReturnedResponse("Sample processed.");
			medallRequestResponseDetails.setStatus('A');
			medallRequestResponseDetails.setOrganizationId(medallRequestResponseDto.getOrganizationId());
			medallRequestResponseDetails.setUnitId(medallRequestResponseDto.getUnitId());
			medallRequestResponseDetails.setAddedBy(medallRequestResponseDto.getAddedBy());
			medallRequestResponseDetails.setAddedDate(GlobalCommonDateUtils.getDate(medallRequestResponseDto.getAddedDate(), "dd-M-yyyy HH:mm:ss"));
			medallRequestResponseDetails.setUpdatedBy(medallRequestResponseDto.getUpdatedBy());
			medallRequestResponseDetails.setUpdatedDate(GlobalCommonDateUtils.getDate(medallRequestResponseDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			 
			save(medallRequestResponseDetails);
		
		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		
		} catch (HibernateException he) {
		logger.error("saveMedallRequestResponse failed", he);
		} catch (Exception e) {
		logger.error("saveMedallRequestResponse failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getMedallRequestResponse(MedallRequestResponseDto med) throws ApplicationException {
		try {
			String query = "select " 
					+ "	tod.order_id as \"orderId\", " + 
					"	tod.created_by as \"addedBy\", " + 
					"	to_char(tod.created_date, 'YYYY-MM-DD HH:mm:ss') as \"addedDate\", " + 
					"	to_char(tod.updated_date , 'YYYY-MM-DD HH:mm:ss') as \"updatedDate\", " + 
					"	tod.updated_by as \"updatedBy\", " + 
					"	tod.status as \"status\", " + 
					"	tod.order_details_id as \"orderDetailsId\", " + 
					"	tod.org_id as \"organizationId\", " + 
					"	tod.org_unit_id as \"unitId\", " + 
					"	tod.service_id as \"serviceId\", " + 
					"	tlsd.sample_barcode as \"sampleNo\", " + 
					"	msm.service_standard_name as \"serviceStandardName\", " + 
					"	tom.encounter_id as \"encounterId\", " + 
					"	tom.patient_id as \"patientId\", "
					+ " msm.medall_service_id as \"medallServiceId\" ," + 
					"	tom.admission_id as \"admissionId\"" 
					+ "from " 
					+ "	public.t_order_details tod "
					+ "inner join public.t_order_master tom on "
					+ "	tom.order_id = tod.order_id "
					+ "left join lab.t_lab_sample_dtls tlsd on "
					+ "	tlsd.order_details_id = tod.order_details_id "
					+ "inner join service.m_service_master msm on " 
					+ "	msm.service_master_id = tod.service_id "
					+ "where " + "	tod.order_id = :orderId";
			List<MedallRequestResponseDto> list = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("orderId",med.getOrderId())
					.setResultTransformer(Transformers.aliasToBean(MedallRequestResponseDto.class)).list();
			
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
		logger.error("getMedallRequestResponse failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response updateMedallRequestResponse(MedallRequestResponseDto medallRequestResponseDto) throws ApplicationException {
		try {
			Session session=sessionFactory.getCurrentSession();
			session.createSQLQuery(" update lab.t_medall_request_response_details  set returned_response = 'Sample is not collected'"
			+ " where order_id=:orderId ")
					.setParameter("orderId",medallRequestResponseDto.getOrderId())
			.executeUpdate();
		
		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		
		} catch (HibernateException he) {
		logger.error("updateMedallRequestResponse failed", he);
		} catch (Exception e) {
		logger.error("updateMedallRequestResponse failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	
}
