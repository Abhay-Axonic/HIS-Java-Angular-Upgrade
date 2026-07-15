package com.param.opd.coversheet.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.CoversheetTemplateMasterDto;
import com.param.global.model.CabinMaster;
import com.param.global.otims.dto.ProcedureOrderEquipmentDto;
import com.param.opd.common.CommonDateUtils;
import com.param.opd.coversheet.dto.ComplaintAppointmentDto;
import com.param.opd.coversheet.dto.ComplaintAppointmentMapperDto;
import com.param.opd.coversheet.model.ComplaintAppointmentMapper;
import com.param.opd.coversheet.model.DiagnosisPatientAppointmentMapper;
import com.param.opd.coversheet.service.IComplaintAppointmentMapperService;
import com.param.otims.model.ProcedureOrderEquipment;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({ "unused", "unchecked" })
public class ComplaintAppointmentMapperDaoImpl extends GenericDao<ComplaintAppointmentMapper, Integer> implements IComplaintAppointmentMapperDao,ICommonConstants{
	final static Logger logger = Logger.getLogger(ComplaintAppointmentMapperDaoImpl.class);


	public ComplaintAppointmentMapperDaoImpl() {
		super(ComplaintAppointmentMapper.class);
		// TODO Auto-generated constructor stub
	}

	@Autowired
	private IComplaintAppointmentMapperService iComplaintAppointmentMapperService;
	
	@SuppressWarnings("rawtypes")
	@Override
	public Response saveComplaintAppointmentMapper(ComplaintAppointmentMapperDto complaintAppointmentMapperDto)throws ApplicationException {
		// TODO Auto-generated method stub
		try
		{
			for(int i=0;i<complaintAppointmentMapperDto.getListComplaintAppointmentDetailsDto().size();i++)
			{
				if(complaintAppointmentMapperDto.getListComplaintAppointmentDetailsDto().get(i).getComplaintAppoId() != null && complaintAppointmentMapperDto.getListComplaintAppointmentDetailsDto().get(i).getComplaintAppoId()>0)
				{
					
					ComplaintAppointmentMapper complaintAppointmentMapper = findById(complaintAppointmentMapperDto.getListComplaintAppointmentDetailsDto().get(i).getComplaintAppoId());
					complaintAppointmentMapper.setIsReviewedFlag(YES);
					iComplaintAppointmentMapperService.updateSaveNurseReviewedFlag(complaintAppointmentMapper);
				}
				
				ComplaintAppointmentMapper complaintAppointmentMapper = new ComplaintAppointmentMapper();
				//System.out.println(complaintAppointmentMapperDto.getListComplaintAppointmentDetailsDto().size());
				complaintAppointmentMapper.setCerated_date(CommonDateUtils.getDate(complaintAppointmentMapperDto.getCeratedDate(), "dd-MM-yyyy"));
				complaintAppointmentMapper.setEncounterId(complaintAppointmentMapperDto.getEncounterId());
				
				if(complaintAppointmentMapperDto.getListComplaintAppointmentDetailsDto().get(i).getIsReviewedFlag() == '\0')
				{
					complaintAppointmentMapper.setIsReviewedFlag(NO);
				}
				else
				{
					complaintAppointmentMapper.setIsReviewedFlag(complaintAppointmentMapperDto.getListComplaintAppointmentDetailsDto().get(i).getIsReviewedFlag());
				}
				
				complaintAppointmentMapper.setIsResolved('N');
				complaintAppointmentMapper.setPatientId(complaintAppointmentMapperDto.getPatientId());
				complaintAppointmentMapper.setRoleId(complaintAppointmentMapperDto.getRoleId());
				complaintAppointmentMapper.setCreated_by(complaintAppointmentMapperDto.getCreatedBy());
				complaintAppointmentMapper.setUpdated_by(complaintAppointmentMapperDto.getUpdatedBy());
				complaintAppointmentMapper.setUpdated_date(CommonDateUtils.getDate(complaintAppointmentMapperDto.getUpdatedDate(), "dd-MM-yyyy"));
				complaintAppointmentMapper.setStatus(complaintAppointmentMapperDto.getStatus());
				complaintAppointmentMapper.setOrganizationId(complaintAppointmentMapperDto.getOrganizationId());
				complaintAppointmentMapper.setUnitId(complaintAppointmentMapperDto.getUnitId());
				complaintAppointmentMapper.setDiagnosisId(complaintAppointmentMapperDto.getListComplaintAppointmentDetailsDto().get(i).getDiagnosisId());
				complaintAppointmentMapper.setIsComplaintStop('N');
				complaintAppointmentMapper.setIcpcCodeText(complaintAppointmentMapperDto.getListComplaintAppointmentDetailsDto().get(i).getIcpcCodeText());
				complaintAppointmentMapper.setIcpcSystemId(complaintAppointmentMapperDto.getListComplaintAppointmentDetailsDto().get(i).getIcpcSystemId());
				complaintAppointmentMapper.setDuration(complaintAppointmentMapperDto.getListComplaintAppointmentDetailsDto().get(i).getDuration());
				complaintAppointmentMapper.setRemark(complaintAppointmentMapperDto.getListComplaintAppointmentDetailsDto().get(i).getRemark());
				complaintAppointmentMapper.setSince(complaintAppointmentMapperDto.getListComplaintAppointmentDetailsDto().get(i).getSince());

			
				save(complaintAppointmentMapper);
			}
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		}catch (HibernateException he) {
		logger.error("saveComplaintAppointmentMapper failed", he);
		} catch (Exception e) {
		logger.error("saveComplaintAppointmentMapper failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}



	@SuppressWarnings("rawtypes")
	@Override
	public Response getListOfComplaintAppointmentMapper(ComplaintAppointmentMapperDto complaintAppointmentMapperDto)
			throws ApplicationException {
		try
		{
			List<ComplaintAppointmentDto> listComplaintAppointmentMapper;
			/*if(complaintAppointmentMapperDto.getRoleId() == 3)
			{
				StatelessSession statlessSession = sessionFactory.openStatelessSession();
				listComplaintAppointmentMapper = statlessSession.getNamedQuery("GET_COMPLAINT_APPOINTMENT_MAPPER_BY_ROLE_ID")
						.setParameter("encounterId", complaintAppointmentMapperDto.getEncounterId())
						.setParameter("unitId", complaintAppointmentMapperDto.getUnitId())
						.setParameter("organizationId", complaintAppointmentMapperDto.getOrganizationId())
						.setParameter("roleId", complaintAppointmentMapperDto.getRoleId())
						.setResultTransformer(Transformers.aliasToBean(ComplaintAppointmentDto.class)).list();
				statlessSession.close();
			}
			else
			{*/
				StatelessSession statlessSession = sessionFactory.openStatelessSession();
				 listComplaintAppointmentMapper = statlessSession.getNamedQuery("GET_COMPLAINT_APPOINTMENT_MAPPER")
						.setParameter("encounterId", complaintAppointmentMapperDto.getEncounterId())
						.setParameter("unitId", complaintAppointmentMapperDto.getUnitId())
						.setParameter("organizationId", complaintAppointmentMapperDto.getOrganizationId())
						.setResultTransformer(Transformers.aliasToBean(ComplaintAppointmentDto.class)).list();
				statlessSession.close();
//			}
			return new Response(SUCCESS, SUCCESS_CODE, null, listComplaintAppointmentMapper, null);
		}catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("getListOfComplaintAppointmentMapper failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}



	@Override
	public Response editListOfComplaintAppointmentMapper(ComplaintAppointmentMapperDto complaintAppointmentMapperDto)throws ApplicationException {
		try
		{
			List<ComplaintAppointmentDto> listComplaintAppointmentMapper;
				StatelessSession statlessSession = sessionFactory.openStatelessSession();
				listComplaintAppointmentMapper = statlessSession.getNamedQuery("GET_COMPLAINT_APPOINTMENT_MAPPER_BY_COMPLAINT_ID")
						.setParameter("complaintAppoId", complaintAppointmentMapperDto.getComplaintAppoId())
						.setParameter("unitId", complaintAppointmentMapperDto.getUnitId())
						.setParameter("organizationId", complaintAppointmentMapperDto.getOrganizationId())
						.setResultTransformer(Transformers.aliasToBean(ComplaintAppointmentDto.class)).list();
				statlessSession.close();
			return new Response(SUCCESS, SUCCESS_CODE, null, listComplaintAppointmentMapper, null);
		}catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("editListOfComplaintAppointmentMapper failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}



	@Override
	public Response updateComplaintAppointmentMapper(ComplaintAppointmentMapperDto complaintAppointmentMapperDto)
			throws ApplicationException {
		try
		{
			for(int i=0;i<complaintAppointmentMapperDto.getListComplaintAppointmentDetailsDto().size();i++)
			{
				/*
				 * Session statlessSession = sessionFactory.getCurrentSession();
				 * ComplaintAppointmentMapper complaintAppointmentMapper =
				 * (ComplaintAppointmentMapper) statlessSession.getNamedQuery(
				 * "GET_COMPLAINT_APPOINTMENT_MAPPER_BY_COMPLAINT_ID")
				 * .setParameter("complaintAppoId",
				 * complaintAppointmentMapperDto.getComplaintAppoId())
				 * .setResultTransformer(Transformers.aliasToBean(ComplaintAppointmentMapper.
				 * class)).uniqueResult();
				 */
				ComplaintAppointmentMapper complaintAppointmentMapper =findById(complaintAppointmentMapperDto.getComplaintAppoId());
				
				/*
				 * if(complaintAppointmentMapperDto.getRoleId() != null &&
				 * complaintAppointmentMapperDto.getRoleId() == 2 &&
				 * complaintAppointmentMapper.getRoleId() != 2 &&
				 * complaintAppointmentMapper.getIsReviewedFlag() == 'N' ) {
				 * iComplaintAppointmentMapperService.saveComplaintAppointmentMapper(
				 * complaintAppointmentMapperDto); }
				 */
				
				
				complaintAppointmentMapper.setCerated_date(CommonDateUtils.getDate(complaintAppointmentMapperDto.getCeratedDate(), "dd-MM-yyyy"));
				complaintAppointmentMapper.setEncounterId(complaintAppointmentMapperDto.getEncounterId());
				complaintAppointmentMapper.setRoleId(complaintAppointmentMapper.getRoleId());
				
				/*
				 * if(complaintAppointmentMapper.getRoleId() == 3 &&
				 * complaintAppointmentMapperDto.getRoleId() == 2 ) {
				 * complaintAppointmentMapper.setIsReviewedFlag(YES); } else {
				 * complaintAppointmentMapper.setIsReviewedFlag(NO); }
				 */
				
				//complaintAppointmentMapper.setIsReviewedFlag(complaintAppointmentMapper.getRoleId() == 3 ? YES:NO);
				//complaintAppointmentMapper.setIsReviewedFlag(YES);
				complaintAppointmentMapper.setIsComplaintStop('N');
				complaintAppointmentMapper.setIsResolved('N');
				complaintAppointmentMapper.setPatientId(complaintAppointmentMapperDto.getPatientId());
				complaintAppointmentMapper.setCreated_by(complaintAppointmentMapperDto.getCreatedBy());
				complaintAppointmentMapper.setUpdated_by(complaintAppointmentMapperDto.getUpdatedBy());
				complaintAppointmentMapper.setUpdated_date(CommonDateUtils.getDate(complaintAppointmentMapperDto.getUpdatedDate(), "dd-MM-yyyy"));
				complaintAppointmentMapper.setStatus(complaintAppointmentMapperDto.getStatus());
				complaintAppointmentMapper.setOrganizationId(complaintAppointmentMapperDto.getOrganizationId());
				complaintAppointmentMapper.setUnitId(complaintAppointmentMapperDto.getUnitId());
				complaintAppointmentMapper.setDiagnosisId(complaintAppointmentMapperDto.getListComplaintAppointmentDetailsDto().get(i).getDiagnosisId());
				complaintAppointmentMapper.setIcpcCodeText(complaintAppointmentMapperDto.getListComplaintAppointmentDetailsDto().get(i).getIcpcCodeText());
				complaintAppointmentMapper.setIcpcSystemId(complaintAppointmentMapperDto.getListComplaintAppointmentDetailsDto().get(i).getIcpcSystemId());
				complaintAppointmentMapper.setDuration(complaintAppointmentMapperDto.getListComplaintAppointmentDetailsDto().get(i).getDuration());
				complaintAppointmentMapper.setRemark(complaintAppointmentMapperDto.getListComplaintAppointmentDetailsDto().get(i).getRemark());
				complaintAppointmentMapper.setSince(complaintAppointmentMapperDto.getListComplaintAppointmentDetailsDto().get(i).getSince());
			
				save(complaintAppointmentMapper);
				
					
			}	
		
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		}catch (HibernateException he) {
		logger.error("updateComplaintAppointmentMapper failed", he);
		} catch (Exception e) {
		logger.error("updateComplaintAppointmentMapper failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}



	@Override
	public Response updateSaveNurseReviewedFlag(
			ComplaintAppointmentMapper complaintAppointmentMapper)
			throws ApplicationException {
		try
		{
			save(complaintAppointmentMapper);
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		}catch (HibernateException he) {
		logger.error("updateSaveNurseReviewedFlag failed", he);
		} catch (Exception e) {
		logger.error("updateSaveNurseReviewedFlag failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}



	@Override
	public Response updateComplaintAppointmentStatusToRemoveComplaint(ComplaintAppointmentMapperDto complaintAppointmentMapperDto)
			throws ApplicationException {
		try {
			ComplaintAppointmentMapper complaintAppointmentMapper=findById(complaintAppointmentMapperDto.getComplaintAppoId());
			
			complaintAppointmentMapper.setJustificationComplaint(complaintAppointmentMapperDto.getJustificationComplaint());
			complaintAppointmentMapper.setIsComplaintStop(complaintAppointmentMapperDto.getIsComplaintStop());
			complaintAppointmentMapper.setUpdated_by(complaintAppointmentMapperDto.getUpdatedBy());
			complaintAppointmentMapper.setUpdated_date(GlobalCommonDateUtils.getDate(complaintAppointmentMapperDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			 
			update(complaintAppointmentMapper);
	
	return new Response(SUCCESS, SUCCESS_CODE, "Cancel successfully", null, null);
	
	} catch (HibernateException he) {
	logger.error("updateComplaintAppointmentStatusToRemoveComplaint failed", he);
	} catch (Exception e) {
	logger.error("updateComplaintAppointmentStatusToRemoveComplaint failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}



	@Override
	public Response getComplaintAppointmentByPatientIdForBookView(
			ComplaintAppointmentMapperDto complaintAppointmentMapperDto) {
		try
		{
			List<ComplaintAppointmentDto> listComplaintAppointmentMapper;
				StatelessSession statlessSession = sessionFactory.openStatelessSession();
				if(complaintAppointmentMapperDto.getEncounterId()!=null) {
				listComplaintAppointmentMapper = statlessSession.getNamedQuery("GET_COMPLAINT_APPOINTMENT_MAPPER_BY_COMPLAINT_ID")
						.setParameter("encounterId", complaintAppointmentMapperDto.getEncounterId())
						.setParameter("unitId", complaintAppointmentMapperDto.getUnitId())
						.setParameter("organizationId", complaintAppointmentMapperDto.getOrganizationId())
						.setResultTransformer(Transformers.aliasToBean(ComplaintAppointmentDto.class)).list();
				
				}else {
					listComplaintAppointmentMapper = statlessSession.getNamedQuery("GET_COMPLAINT_ADMISSION")
							.setParameter("admissionId", complaintAppointmentMapperDto.getAdmissionId())
							.setParameter("unitId", complaintAppointmentMapperDto.getUnitId())
							.setParameter("organizationId", complaintAppointmentMapperDto.getOrganizationId())
							.setResultTransformer(Transformers.aliasToBean(ComplaintAppointmentDto.class)).list();
				}
				statlessSession.close();
			return new Response(SUCCESS, SUCCESS_CODE, null, listComplaintAppointmentMapper, null);
		}catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("getComplaintAppointmentByPatientIdForBookView failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}



	@Override
	public Response getComplaintAppointmentByPatientId(ComplaintAppointmentMapperDto complaintAppointmentMapperDto)
			throws ApplicationException {
		try
		{
			List<ComplaintAppointmentDto> listComplaintAppointmentMapper;
				StatelessSession statlessSession = sessionFactory.openStatelessSession();
				listComplaintAppointmentMapper = statlessSession.getNamedQuery("GET_COMPLAINT_APPOINTMENT_BY_PATIENT_ID")
						.setParameter("organizationId", complaintAppointmentMapperDto.getOrganizationId())
						.setParameter("patientId", complaintAppointmentMapperDto.getPatientId())
						.setResultTransformer(Transformers.aliasToBean(ComplaintAppointmentDto.class)).list();
				statlessSession.close();
			return new Response(SUCCESS, SUCCESS_CODE, null, listComplaintAppointmentMapper, null);
		}catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("getComplaintAppointmentByPatientId failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}



	@Override
	public Response getPastComplaintAppointmentByPatientId(ComplaintAppointmentMapperDto complaintAppointmentMapperDto)
			throws ApplicationException {
		try
		{
			List<ComplaintAppointmentDto> listComplaintAppointmentMapper;
				StatelessSession statlessSession = sessionFactory.openStatelessSession();
				listComplaintAppointmentMapper = statlessSession.getNamedQuery("GET_PAST_COMPLAINT_APPOINTMENT_BY_PATIENT_ID")
						.setParameter("organizationId", complaintAppointmentMapperDto.getOrganizationId())
						.setParameter("patientId", complaintAppointmentMapperDto.getPatientId())
						.setResultTransformer(Transformers.aliasToBean(ComplaintAppointmentDto.class)).list();
				statlessSession.close();
			return new Response(SUCCESS, SUCCESS_CODE, null, listComplaintAppointmentMapper, null);
		}catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("getPastComplaintAppointmentByPatientId failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}



	@Override
	public Response saveResolvedComplaintAppointment(ComplaintAppointmentMapperDto complaintAppointmentMapperDto)
			throws ApplicationException {
		try {
			
			/*ComplaintAppointmentMapper mppr=findById(complaintAppointmentMapperDto.getComplaintAppoId());
			mppr.setResolvedDate(GlobalCommonDateUtils.getDate(complaintAppointmentMapperDto.getResolvedDate(), "dd-M-yyyy HH:mm:ss"));
			mppr.setResolvedRemark(complaintAppointmentMapperDto.getResolvedRemark());
			mppr.setIsResolved('Y');
			update(mppr);*/
			Session session=sessionFactory.getCurrentSession();
			session.createSQLQuery(" update patient.t_complaint_appo_mapper  set resolved_date = '"+complaintAppointmentMapperDto.getResolvedDate()
			+"' , resolved_remark= '"+complaintAppointmentMapperDto.getResolvedRemark()+" "
			+"' , is_resolved='"+('Y')+"' "
			+ " where complaint_appo_id="+complaintAppointmentMapperDto.getComplaintAppoId())
			.executeUpdate();
			return new Response(SUCCESS, SUCCESS_CODE, "Complaint marked as Resolved.", null, null);
		} catch (Exception e) {
		logger.error("saveResolvedComplaintAppointment failed", e);
		}
	return new Response(ERROR, null, null, null, null);
	}



	@Override
	public Response getComplaintsByPatientIdForBookView(ComplaintAppointmentMapperDto complaintAppointmentMapperDto)
			throws ApplicationException {
		try
		{
			List<ComplaintAppointmentDto> list;
				StatelessSession statlessSession = sessionFactory.openStatelessSession();
				list = statlessSession.getNamedQuery("GET_COMPLAINTS_BY_PATIENT_ID_FOR_BOOK_VIEW")
						.setParameter("organizationId", complaintAppointmentMapperDto.getOrganizationId())
						.setParameter("patientId", complaintAppointmentMapperDto.getPatientId())
						.setParameter("fromDate", complaintAppointmentMapperDto.getFromDate())
						.setParameter("toDate", complaintAppointmentMapperDto.getToDate())
						.setResultTransformer(Transformers.aliasToBean(ComplaintAppointmentDto.class)).list();
				statlessSession.close();
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		}catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("getComplaintsByPatientIdForBookView failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}



	@Override
	public Response getPastComplaintAppointmentForBookView(ComplaintAppointmentMapperDto complaintAppointmentMapperDto)
			throws ApplicationException {
		try
		{
			List<ComplaintAppointmentDto> listComplaintAppointmentMapper;
				StatelessSession statlessSession = sessionFactory.openStatelessSession();
				listComplaintAppointmentMapper = statlessSession.getNamedQuery("GET_PAST_COMPLAINT_APPOINTMENT_FOR_BOOK_VIEW")
						.setParameter("organizationId", complaintAppointmentMapperDto.getOrganizationId())
						.setParameter("patientId", complaintAppointmentMapperDto.getPatientId())
						.setParameter("fromDate", complaintAppointmentMapperDto.getFromDate())
						.setParameter("toDate", complaintAppointmentMapperDto.getToDate())
						.setResultTransformer(Transformers.aliasToBean(ComplaintAppointmentDto.class)).list();
				statlessSession.close();
			return new Response(SUCCESS, SUCCESS_CODE, null, listComplaintAppointmentMapper, null);
		}catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("getPastComplaintAppointmentForBookView failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}



	@Override
	public Response getMostSavedComplaint(ComplaintAppointmentMapperDto complaintAppointmentMapperDto)
			throws ApplicationException {
		try
		{
			List<ComplaintAppointmentDto> listComplaintAppointmentMappers;
				StatelessSession statlessSession = sessionFactory.openStatelessSession();
				listComplaintAppointmentMappers = statlessSession.getNamedQuery("GET_MOST_SAVED_COMPLAINT")
						.setParameter("organizationId", complaintAppointmentMapperDto.getOrganizationId())
						.setParameter("unitId", complaintAppointmentMapperDto.getUnitId())
						.setResultTransformer(Transformers.aliasToBean(ComplaintAppointmentDto.class)).list();
				statlessSession.close();
			return new Response(SUCCESS, SUCCESS_CODE, null, listComplaintAppointmentMappers, null);
		}catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("getMostSavedComplaint failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}


	@Override
	public Response savefComplaintAppointmentMapper(List<ComplaintAppointmentMapperDto> listObject,CoversheetTemplateMasterDto coversheetTemplateMasterDto) {
try{
		
			for (ComplaintAppointmentMapperDto dto : listObject) {
				if(dto.getCovTemplateId() == null){
					ComplaintAppointmentMapper complaintAppointmentMapper = new ComplaintAppointmentMapper();
					complaintAppointmentMapper.setCerated_date(TimeZoneComponent.getDateByzone(coversheetTemplateMasterDto.getUnitId()));
//					complaintAppointmentMapper.setEncounterId(coversheetTemplateMasterDto.getEncounterId());
					complaintAppointmentMapper.setIsReviewedFlag(dto.getIsReviewedFlag());
					complaintAppointmentMapper.setIsResolved('N');
//					complaintAppointmentMapper.setPatientId(coversheetTemplateMasterDto.getPatientId());
					complaintAppointmentMapper.setRoleId(dto.getRoleId());
					complaintAppointmentMapper.setCreated_by(coversheetTemplateMasterDto.getCreatedBy());
					complaintAppointmentMapper.setUpdated_by(coversheetTemplateMasterDto.getUpdatedBy());
					complaintAppointmentMapper.setUpdated_date(TimeZoneComponent.getDateByzone(coversheetTemplateMasterDto.getUnitId()));
					complaintAppointmentMapper.setStatus(coversheetTemplateMasterDto.getStatus());
					complaintAppointmentMapper.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
					complaintAppointmentMapper.setUnitId(coversheetTemplateMasterDto.getUnitId());
					complaintAppointmentMapper.setDiagnosisId(dto.getDiagnosisId());
					complaintAppointmentMapper.setIsComplaintStop('N');
					complaintAppointmentMapper.setIcpcCodeText(dto.getIcpcCodeText());
					complaintAppointmentMapper.setIcpcSystemId(dto.getIcpcSystemId());
					complaintAppointmentMapper.setDuration(dto.getDuration());
					complaintAppointmentMapper.setRemark(dto.getRemark());
					complaintAppointmentMapper.setCovTemplateId(coversheetTemplateMasterDto.getCovTemplateId());
					complaintAppointmentMapper.setSince(dto.getSince());
					sessionFactory.getCurrentSession().save(complaintAppointmentMapper);
				}
				else {
					ComplaintAppointmentMapper complaintAppointmentMapper = findById(dto.getCovTemplateId());
					complaintAppointmentMapper.setCerated_date(TimeZoneComponent.getDateByzone(dto.getUnitId()));
					complaintAppointmentMapper.setIsReviewedFlag(dto.getIsReviewedFlag());
					complaintAppointmentMapper.setIsResolved('N');
					complaintAppointmentMapper.setRoleId(dto.getRoleId());
					complaintAppointmentMapper.setCreated_by(coversheetTemplateMasterDto.getCreatedBy());
					complaintAppointmentMapper.setUpdated_by(coversheetTemplateMasterDto.getUpdatedBy());
					complaintAppointmentMapper.setUpdated_date(TimeZoneComponent.getDateByzone(coversheetTemplateMasterDto.getUnitId()));
					complaintAppointmentMapper.setStatus(coversheetTemplateMasterDto.getStatus());
					complaintAppointmentMapper.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
					complaintAppointmentMapper.setUnitId(coversheetTemplateMasterDto.getUnitId());
					complaintAppointmentMapper.setDiagnosisId(dto.getDiagnosisId());
					complaintAppointmentMapper.setIsComplaintStop('N');
					complaintAppointmentMapper.setIcpcCodeText(dto.getIcpcCodeText());
					complaintAppointmentMapper.setIcpcSystemId(dto.getIcpcSystemId());
					complaintAppointmentMapper.setDuration(dto.getDuration());
					complaintAppointmentMapper.setRemark(dto.getRemark());
					complaintAppointmentMapper.setSince(dto.getSince());
					sessionFactory.getCurrentSession().update(complaintAppointmentMapper);
				}
				}
				
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		
		}catch(Exception e){
			
			throw e;
		}
	}



	@Override
	public Response updateCovTempComplaintAppointmentMapper(List<ComplaintAppointmentDto> list,
			CoversheetTemplateMasterDto coversheetTemplateMasterDto) {
		try{
			
			for (ComplaintAppointmentDto dto : list) {
				if(dto.getCovTemplateId() == null){
					
					ComplaintAppointmentMapper complaintAppointmentMapper = findById(dto.getCovTemplateId());
					complaintAppointmentMapper.setCerated_date(TimeZoneComponent.getDateByzone(dto.getUnitId()));
//					complaintAppointmentMapper.setEncounterId(dto.getEncounterId());
					complaintAppointmentMapper.setIsReviewedFlag(dto.getIsReviewedFlag());
					complaintAppointmentMapper.setIsResolved('N');
//					complaintAppointmentMapper.setPatientId(coversheetTemplateMasterDto.getPatientId());
					complaintAppointmentMapper.setRoleId(dto.getRoleId());
					complaintAppointmentMapper.setCreated_by(coversheetTemplateMasterDto.getCreatedBy());
					complaintAppointmentMapper.setUpdated_by(coversheetTemplateMasterDto.getUpdatedBy());
					complaintAppointmentMapper.setUpdated_date(TimeZoneComponent.getDateByzone(coversheetTemplateMasterDto.getUnitId()));
					complaintAppointmentMapper.setStatus(coversheetTemplateMasterDto.getStatus());
					complaintAppointmentMapper.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
					complaintAppointmentMapper.setUnitId(coversheetTemplateMasterDto.getUnitId());
					complaintAppointmentMapper.setDiagnosisId(dto.getDiagnosisId());
					complaintAppointmentMapper.setIsComplaintStop('N');
					complaintAppointmentMapper.setIcpcCodeText(dto.getIcpcCodeText());
					complaintAppointmentMapper.setIcpcSystemId(dto.getIcpcSystemId());
					complaintAppointmentMapper.setDuration(dto.getDuration());
					complaintAppointmentMapper.setRemark(dto.getRemark());
					complaintAppointmentMapper.setSince(dto.getSince());
					sessionFactory.getCurrentSession().update(complaintAppointmentMapper);
				}
				}
				
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		
		}catch(Exception e){
			
			throw e;
		}
	}



	@Override
	public Response searchFreeTextComplaint(ComplaintAppointmentMapperDto complaintAppointmentMapperDto)
			throws ApplicationException {
		try
		{
			List<ComplaintAppointmentDto> listComplaintAppointmentMapper;
			String query=" " + 
					" select " + 
//					"	complaintAdmission.complaint_admission_id as \"complaintAdmissionId\", " + 
					"	complaintAdmission.status as \"status\", " + 
					"	complaintAdmission.icpc_code_text as \"icpcCodeText\", " + 
					"	complaintAdmission.icpc_code_text as \"icpcDesc\", " + 
					"	complaintAdmission.icpc_system_id as \"icpcSystemId\" " + 
					" from " + 
					"	adt.t_complaint_admission complaintAdmission " + 
					" where " + 
					"	 complaintAdmission.status = 'A' " + 
					//"	and complaintAdmission.is_complaint_stop = 'N' " + 
					"	and complaintAdmission.organization_id =:organizationId " + 
					"	and complaintAdmission.is_resolved = 'N' " + 
					"	and ((lower(complaintAdmission.icpc_code_text) like '%"+complaintAppointmentMapperDto.getSearchIcpcCodeText()+"%') or ((complaintAdmission.icpc_code_text) like '%"+complaintAppointmentMapperDto.getSearchIcpcCodeText()+"%'))" + 
					" union " + 
					" select " + 
//					"	complaintAppointment.complaint_appo_id as \"complaintAppoId\", " + 
					"	complaintAppointment.status as \"status\", " + 
					"	complaintAppointment.icpc_code_text as \"icpcCodeText\", " + 
					"	complaintAppointment.icpc_code_text as \"icpcDesc\", " + 
					"	complaintAppointment.icpc_system_id as \"icpcSystemId\" " + 
					" from " + 
					"	patient.t_complaint_appo_mapper complaintAppointment " + 
					" where " + 
					"	 complaintAppointment.status = 'A' " + 
					"	and complaintAppointment.is_resolved = 'N' " + 
					"	and complaintAppointment.organization_id =:organizationId " + 
					//"	and complaintAppointment.is_complaint_stop = 'N'  " + 
//					"	and lower(complaintAppointment.icpc_code_text) like '%"+complaintAppointmentMapperDto.getSearchIcpcCodeText()+"%'"
					"	and ((lower(complaintAppointment.icpc_code_text) like '%"+complaintAppointmentMapperDto.getSearchIcpcCodeText()+"%') or ((complaintAppointment.icpc_code_text) like '%"+complaintAppointmentMapperDto.getSearchIcpcCodeText()+"%'))"  

					;
				StatelessSession statlessSession = sessionFactory.openStatelessSession();
				 listComplaintAppointmentMapper = statlessSession.createSQLQuery(query)
						.setParameter("organizationId", complaintAppointmentMapperDto.getOrganizationId())
						.setResultTransformer(Transformers.aliasToBean(ComplaintAppointmentDto.class)).list();
				statlessSession.close();
//			}
			return new Response(SUCCESS, SUCCESS_CODE, null, listComplaintAppointmentMapper, null);
		}catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("searchFreeTextComplaint failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);

	}



	

}