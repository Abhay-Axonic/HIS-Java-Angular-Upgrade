package com.param.bloodBank.dao;

import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.bloodBank.dto.CrossMatchRegisterDto;
import com.param.bloodBank.dto.PatientRequisitionDetailsDto;
import com.param.bloodBank.model.PatientRequisitionDetails;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes", "unchecked"})
public class PatientRequisitionDetailsDaoImpl extends GenericDao<PatientRequisitionDetails, Integer> implements IPatientRequisitionDetailsDao, ICommonConstants{

	final static Logger logger = Logger.getLogger(PatientRequisitionDetailsDaoImpl.class);


	public PatientRequisitionDetailsDaoImpl() {
		super(PatientRequisitionDetails.class);
	}

	@Override
	public Response getComponentListByPatientId(PatientRequisitionDetailsDto dto) throws ApplicationException {
		try {
			List<PatientRequisitionDetailsDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_COMPONENT_LIST_BLOOD_ORDER_ID")
					.setParameter("patientId", dto.getPatientId())
					.setParameter("organizationId", dto.getOrganizationId())
					.setParameter("unitId", dto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(PatientRequisitionDetailsDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
				
			} catch (HibernateException he) {
				logger.error("getComponentListByPatientId failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getComponentListByPatientId failed", e);
				throw new ServiceException(e.getMessage());
			}
	}

	@Override
	public Response savePatientRequisitionDetails(PatientRequisitionDetailsDto dto) throws ApplicationException {
		try {
			PatientRequisitionDetails mstr = new PatientRequisitionDetails(); 
			
			mstr.setAdmissionId(dto.getAdmissionId());
			mstr.setBloodUnit(dto.getBloodUnit());
			mstr.setComponentId(dto.getComponentId());
			mstr.setCreatedDate(GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:ss:mm"));
			mstr.setCreatedBy(dto.getCreatedBy());
			//mstr.setDiagnosis(dto.getDiagnosis());
			mstr.setDoctorId(dto.getDoctorId());
			mstr.setExportedToCrossMatch(dto.getExportedToCrossMatch());
			mstr.setExportedToReservation(dto.getExportedToReservation());
			mstr.setHospitalId(dto.getHospitalId());
			mstr.setOrganizationId(dto.getOrganizationId());
			mstr.setPatientBloodGroupId(dto.getPatientBloodGroupId());
			mstr.setPatientId(dto.getPatientId());
			mstr.setPatientRequisitionId(dto.getPatientRequisitionId());
			mstr.setRequisitionTypeId(dto.getRequisitionTypeId());
			mstr.setRemark(dto.getRemark());
			mstr.setRequiredBloodGroupId(dto.getRequiredBloodGroupId());
			mstr.setBloodComponentOrderId(dto.getBloodComponentOrderId());
			mstr.setRequisitionDate(GlobalCommonDateUtils.getDate(dto.getRequisitionDate(),"dd-M-yyyy HH:ss:mm"));
			mstr.setIssueDate(GlobalCommonDateUtils.getDate(dto.getIssueDate(),"dd-M-yyyy HH:ss:mm"));
			//mstr.setRequisitionStatus(dto.getRequisitionStatus());
		//	mstr.setReservedDate(GlobalCommonDateUtils.getDate(dto.getRese, "dd-M-yyyy HH:ss:mm"));
			
			mstr.setUnitId(dto.getUnitId());
			mstr.setUpdatedBy(dto.getUpdatedBy());
			mstr.setUpdatedDate(GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));
			mstr.setStatus(dto.getStatus());
			mstr.setBloodUnit(dto.getBloodUnit());
			mstr = save(mstr);
			
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, mstr);
			
			} catch (HibernateException he) {
				logger.error("savePatientRequisitionDetails failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("savePatientRequisitionDetails failed", e);
				throw new ServiceException(e.getMessage());
			}
	}

	@Override
	public Response getPatientRequisitionDetailsOnCrossMatch(PatientRequisitionDetailsDto dto)
			throws ApplicationException {
		try{
			Session session = sessionFactory.getCurrentSession();
			StringBuilder query=new StringBuilder(session.getNamedQuery("SEARCH_PATIENTREQUISITION_DETAILS").getQueryString());
			
			StringBuffer subQuery = new StringBuffer();
			if(dto.getFromDate()!=null && dto.getToDate()!=null && dto.getFromDate()!="" && dto.getToDate()!=""){
				subQuery.append(" AND to_char(prd.createdDate,'YYYY-MM-DD') between :fromDate and :toDate ");
			}
			query = query.append(subQuery.toString());
			Query actualQuery = session.createQuery(query.toString());
			 actualQuery.setParameter("organizationId", dto.getOrganizationId())
					.setParameter("unitId", dto.getUnitId());

			if(dto.getFromDate()!=null && dto.getToDate()!=null && dto.getFromDate()!="" && dto.getToDate()!=""){
				actualQuery.setParameter("fromDate",dto.getFromDate());
				actualQuery.setParameter("toDate",dto.getToDate());
			}


			List<PatientRequisitionDetailsDto> list =actualQuery.setResultTransformer(Transformers.aliasToBean(PatientRequisitionDetailsDto.class)).list();

			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		}catch(Exception e){
			logger.error("getPatientRequisitionDetailsOnCrossMatch failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateExportedToCrossMatchStatus(PatientRequisitionDetailsDto dto) throws ApplicationException {
		try {
			
			PatientRequisitionDetails dtls = findById(dto.getPatientRequisitionId());
			dtls.setUpdatedBy(dto.getUpdatedBy());
			dtls.setUpdatedDate(GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));
			dtls.setExportedToCrossMatch(dto.getExportedToCrossMatch());
			dtls.setBloodUnit(dto.getBloodUnit());
			dtls=update(dtls);
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, dtls);
		}catch(Exception e){
			logger.error("updateExportedToCrossMatchStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updatePatientRequisitionDetails(CrossMatchRegisterDto crossMatchRegisterDto)
			throws ApplicationException {
		try 
			{
			int unit=1;
			PatientRequisitionDetails dtls = findById(crossMatchRegisterDto.getPatientRequisitionId());
			dtls.setExportedToCrossMatch(false);
			unit +=dtls.getBloodUnit();
			dtls.setBloodUnit(unit);
			dtls=update(dtls);
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, dtls);
		}catch(Exception e){
			logger.error("updatePatientRequisitionDetails failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
}
