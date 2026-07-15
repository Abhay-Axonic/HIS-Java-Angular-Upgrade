package com.param.ipd.dao;

import java.sql.Timestamp;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.entity.model.ipd.ComplaintAdmission;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.ipd.dto.ComplaintAdmissionDto;
import com.param.ipd.service.IComplaintAdmissionService;
import com.param.opd.coversheet.model.ComplaintAppointmentMapper;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "unchecked", "rawtypes" })
public class ComplaintAdmissionDaoImpl extends GenericDao<ComplaintAdmission, Integer>
		implements IComplaintAdmissionDao, ICommonConstants {

	public ComplaintAdmissionDaoImpl() {
		super(ComplaintAdmission.class);
	}

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	IComplaintAdmissionService iComplaintAdmissionService;

	@Override
	public Response saveComplaintAdmission(ComplaintAdmissionDto complaintAdmissionDto) throws ApplicationException {
		try {
			for (int i = 0; i < complaintAdmissionDto.getListComplaintAdmissionDetailsDto().size(); i++) {
				if (complaintAdmissionDto.getListComplaintAdmissionDetailsDto().get(i).getComplaintAdmissionId() != null && complaintAdmissionDto.getListComplaintAdmissionDetailsDto().get(i).getComplaintAdmissionId() > 0) {

					ComplaintAdmission complaintAdmission = findById(complaintAdmissionDto.getListComplaintAdmissionDetailsDto().get(i).getComplaintAdmissionId());
					complaintAdmission.setIsReviewedFlag(YES);
					iComplaintAdmissionService.updateSaveNurseReviewedFlag(complaintAdmission);
				}

				ComplaintAdmission complaintAdmission = new ComplaintAdmission();
				//System.out.println(complaintAdmissionDto.getListComplaintAdmissionDetailsDto().size());
				complaintAdmission.setCreatedDate(GlobalCommonDateUtils.getDate(complaintAdmissionDto.getCreatedDate(), "dd-MM-yyyy HH:mm:ss"));
				complaintAdmission.setAdmissionId(complaintAdmissionDto.getAdmissionId());

				if (complaintAdmissionDto.getListComplaintAdmissionDetailsDto().get(i).getIsReviewedFlag() == '\0') {
					complaintAdmission.setIsReviewedFlag(NO);
				} else {
					complaintAdmission.setIsReviewedFlag(complaintAdmissionDto.getListComplaintAdmissionDetailsDto().get(i).getIsReviewedFlag());
				}
				complaintAdmission.setIsResolved('N');
				complaintAdmission.setPatientId(complaintAdmissionDto.getPatientId());
				complaintAdmission.setRoleId(complaintAdmissionDto.getRoleId());
				complaintAdmission.setIsComplaintStop('N');
				complaintAdmission.setCreatedBy(complaintAdmissionDto.getCreatedBy());
				complaintAdmission.setUpdatedBy(complaintAdmissionDto.getUpdatedBy());
				complaintAdmission.setUpdatedDate(GlobalCommonDateUtils.getDate(complaintAdmissionDto.getUpdatedDate(), "dd-MM-yyyy HH:mm:ss"));
				complaintAdmission.setStatus(complaintAdmissionDto.getStatus());
				complaintAdmission.setOrganizationId(complaintAdmissionDto.getOrganizationId());
				complaintAdmission.setUnitId(complaintAdmissionDto.getUnitId());
				complaintAdmission.setDiagnosisId(complaintAdmissionDto.getListComplaintAdmissionDetailsDto().get(i).getDiagnosisId());
				complaintAdmission.setIcpcSystemId(complaintAdmissionDto.getListComplaintAdmissionDetailsDto().get(i).getIcpcSystemId());
				complaintAdmission.setIcpcCodeText(complaintAdmissionDto.getListComplaintAdmissionDetailsDto().get(i).getIcpcCodeText());
				complaintAdmission.setRemark(complaintAdmissionDto.getListComplaintAdmissionDetailsDto().get(i).getRemark());
				complaintAdmission.setDuration(complaintAdmissionDto.getListComplaintAdmissionDetailsDto().get(i).getDuration());
				complaintAdmission.setSince(complaintAdmissionDto.getListComplaintAdmissionDetailsDto().get(i).getSince());
//				complaintAdmission.setComplaintsDateTime(GlobalCommonDateUtils.getDate(complaintAdmissionDto.getListComplaintAdmissionDetailsDto().get(i).getComplaintsDateTime(),"dd-MM-yyyy HH:mm:ss"));

				save(complaintAdmission);
			}
			return new Response<>(SUCCESS, null, COMMON_SUCCESS, null, null);
		} catch (HibernateException he) {

		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	
	@Override
	public Response getComplaintAdmission(ComplaintAdmissionDto complaintAdmissionDto) throws ApplicationException {
		try {

			List<ComplaintAdmissionDto> listComplaintAdmissionDto;
			/*if(complaintAdmissionDto.getRoleId() == 3)
			{
				StatelessSession statlessSession = sessionFactory.openStatelessSession();
				listComplaintAdmissionDto = statlessSession.getNamedQuery("GET_COMPLAINT_ADMISSION_BY_ROLE_ID")
						.setParameter("admissionId", complaintAdmissionDto.getAdmissionId())
						.setParameter("unitId", complaintAdmissionDto.getUnitId())
						.setParameter("organizationId", complaintAdmissionDto.getOrganizationId())
						.setParameter("roleId", complaintAdmissionDto.getRoleId())
						.setResultTransformer(Transformers.aliasToBean(ComplaintAdmissionDto.class)).list();
				statlessSession.close();
			}
			else*/
//			{
				StatelessSession statlessSession = sessionFactory.openStatelessSession();
				listComplaintAdmissionDto = statlessSession.getNamedQuery("GET_COMPLAINT_ADMISSION")
						.setParameter("admissionId", complaintAdmissionDto.getAdmissionId())
						.setParameter("unitId", complaintAdmissionDto.getUnitId()).setParameter("organizationId", complaintAdmissionDto.getOrganizationId())
						.setResultTransformer(Transformers.aliasToBean(ComplaintAdmissionDto.class)).list();
				statlessSession.close();
//			}
			return new Response(SUCCESS, SUCCESS_CODE, null, listComplaintAdmissionDto, null);
		} catch (HibernateException he) {

		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response editComplaintAppointment(ComplaintAdmissionDto complaintAdmissionDto) throws ApplicationException {
		try {
			List<ComplaintAdmissionDto> listComplaintAdmissionDto;
			StatelessSession statlessSession = sessionFactory.openStatelessSession();
			listComplaintAdmissionDto = statlessSession.getNamedQuery("GET_COMPLAINT_ADMISSION_BY_COMPLAINT_ID")
					.setParameter("complaintAadmissionId", complaintAdmissionDto.getComplaintAdmissionId())
					.setParameter("unitId", complaintAdmissionDto.getUnitId())
					.setParameter("organizationId", complaintAdmissionDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(ComplaintAdmissionDto.class)).list();
			statlessSession.close();
		return new Response(SUCCESS, SUCCESS_CODE, null, listComplaintAdmissionDto, null);
		} catch (HibernateException he) {

		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateComplaintAdmission(ComplaintAdmissionDto complaintAdmissionDto) throws ApplicationException {
		try {
			for(int i=0;i<complaintAdmissionDto.getListComplaintAdmissionDetailsDto().size();i++)
			{
				Session statlessSession = sessionFactory.getCurrentSession();
				ComplaintAdmission complaintAdmission = (ComplaintAdmission) statlessSession.getNamedQuery("GET_COMPLAINT_ADMISSION_BY_COMPLAINT_ID")
						.setParameter("complaintAdmissionId", complaintAdmissionDto.getComplaintAdmissionId())
						.setResultTransformer(Transformers.aliasToBean(ComplaintAdmission.class)).uniqueResult();
				
				if(complaintAdmission.getRoleId() != null && complaintAdmission.getRoleId() == 2 && complaintAdmission.getRoleId() != 2  && complaintAdmission.getIsReviewedFlag() == 'N')
				{
					iComplaintAdmissionService.saveComplaintAdmission(complaintAdmissionDto);
				}
				
				
				complaintAdmission.setCreatedDate(GlobalCommonDateUtils.getDate(complaintAdmissionDto.getCreatedDate(), "dd-MM-yyyy HH:mm:ss"));
				complaintAdmission.setAdmissionId(complaintAdmissionDto.getAdmissionId());
				complaintAdmission.setRoleId(complaintAdmission.getRoleId());
				complaintAdmission.setPatientId(complaintAdmissionDto.getPatientId());
				if(complaintAdmission.getRoleId() == 3 && complaintAdmissionDto.getRoleId() == 2 )
				{
					complaintAdmission.setIsReviewedFlag(YES);
				}
				else
				{
					complaintAdmission.setIsReviewedFlag(NO);
				}
				
				//complaintAppointmentMapper.setIsReviewedFlag(complaintAppointmentMapper.getRoleId() == 3 ? YES:NO);
				//complaintAppointmentMapper.setIsReviewedFlag(YES);
				complaintAdmission.setCreatedBy(complaintAdmissionDto.getCreatedBy());
				complaintAdmission.setIsComplaintStop('N');
				complaintAdmission.setIsResolved('N');;
				complaintAdmission.setUpdatedBy(complaintAdmissionDto.getUpdatedBy());
				complaintAdmission.setUpdatedDate(GlobalCommonDateUtils.getDate(complaintAdmissionDto.getUpdatedDate(), "dd-MM-yyyy HH:mm:ss"));
				complaintAdmission.setStatus(complaintAdmissionDto.getStatus());
				complaintAdmission.setOrganizationId(complaintAdmissionDto.getOrganizationId());
				complaintAdmission.setUnitId(complaintAdmissionDto.getUnitId());
				complaintAdmission.setDiagnosisId(complaintAdmissionDto.getListComplaintAdmissionDetailsDto().get(i).getDiagnosisId());
				complaintAdmission.setIcpcSystemId(complaintAdmissionDto.getListComplaintAdmissionDetailsDto().get(i).getIcpcSystemId());
				complaintAdmission.setIcpcCodeText(complaintAdmissionDto.getListComplaintAdmissionDetailsDto().get(i).getIcpcCodeText());
				complaintAdmission.setRemark(complaintAdmissionDto.getListComplaintAdmissionDetailsDto().get(i).getRemark());
				complaintAdmission.setDuration(complaintAdmissionDto.getListComplaintAdmissionDetailsDto().get(i).getDuration());
				complaintAdmission.setSince(complaintAdmissionDto.getListComplaintAdmissionDetailsDto().get(i).getSince());
//				complaintAdmission.setComplaintsDateTime(GlobalCommonDateUtils.getDate(complaintAdmissionDto.getComplaintsDateTime(),"dd-MM-yyyy HH:mm:ss"));
				
				save(complaintAdmission);
				
					
			}	
		
			return new Response(SUCCESS, COMMON_SUCCESS, COMMON_UPDATE, null, null);
		} catch (HibernateException he) {

		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateSaveNurseReviewedFlag(ComplaintAdmission complaintAdmission) throws ApplicationException {
		try {
			save(complaintAdmission);
		} catch (HibernateException he) {

		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response updateComplaintAdmissionStatusToRemoveComplaint(ComplaintAdmissionDto complaintAdmissionDto)
			throws ApplicationException {
		try {
			ComplaintAdmission complaintAdmission = findById(complaintAdmissionDto.getComplaintAdmissionId());
			
			complaintAdmission.setJustificationComplaint(complaintAdmissionDto.getJustificationComplaint());
			complaintAdmission.setIsComplaintStop(complaintAdmissionDto.getIsComplaintStop());
			complaintAdmission.setUpdatedBy(complaintAdmissionDto.getUpdatedBy());
			complaintAdmission.setUpdatedDate(GlobalCommonDateUtils.getDate(complaintAdmissionDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			 
			update(complaintAdmission);
	
	return new Response(SUCCESS, SUCCESS_CODE, "Cancel successfully", null, null);
	
	} catch (HibernateException he) {
	} catch (Exception e) {
		
	}
	return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}


	/*@Override
	public Response getComplaintAppointmentByPatientId(ComplaintAdmissionDto complaintAdmissionDto)
			throws ApplicationException {
		try {
			List<ComplaintAdmissionDto> listComplaintAdmissionDto;
			StatelessSession statlessSession = sessionFactory.openStatelessSession();
			listComplaintAdmissionDto = statlessSession.getNamedQuery("GET_COMPLAINT_APPOINTMENT_BY_PATIENT_ID")
					.setParameter("organizationId", complaintAdmissionDto.getOrganizationId())
					.setParameter("patientId", complaintAdmissionDto.getPatientId())
					.setResultTransformer(Transformers.aliasToBean(ComplaintAdmissionDto.class)).list();
			statlessSession.close();
		return new Response(SUCCESS, SUCCESS_CODE, null, listComplaintAdmissionDto, null);
		} catch (HibernateException he) {

		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
*/

	@Override
	public Response saveResolvedComplaintAdmission(ComplaintAdmissionDto complaintAdmissionDto)
			throws ApplicationException {
		try {
			
			/*ComplaintAdmission mppr=findById(complaintAdmissionDto.getComplaintAdmissionId());
			mppr.setResolvedDate(GlobalCommonDateUtils.getDate(complaintAdmissionDto.getResolvedDate(), "dd-M-yyyy"));
			mppr.setResolvedRemark(complaintAdmissionDto.getResolvedRemark());
			mppr.setIsResolved('Y');
			update(mppr);*/
          System.out.println("Actual String: "+complaintAdmissionDto.getResolvedDate());
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			LocalDateTime localDateTime = LocalDateTime.parse(complaintAdmissionDto.getResolvedDate(), formatter);
			Timestamp  resolvedTimestamp = Timestamp.valueOf(localDateTime);
			System.out.println(" Timestamp: "+resolvedTimestamp);
			
			Session session=sessionFactory.getCurrentSession();
			session.createSQLQuery("UPDATE adt.t_complaint_admission SET resolved_date = :resolvedDate, "
							+ "resolved_remark = :resolvedRemark, is_resolved = :isResolved "
							+ "WHERE complaint_admission_id = :complaintAdmissionId")
					.setParameter("resolvedDate", resolvedTimestamp)
					.setParameter("resolvedRemark", complaintAdmissionDto.getResolvedRemark())
					.setParameter("isResolved", 'Y')
					.setParameter("complaintAdmissionId", complaintAdmissionDto.getComplaintAdmissionId())
					.executeUpdate();

			return new Response(SUCCESS, SUCCESS_CODE, "Complaint marked as Resolved.", null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getAISummaryData(ComplaintAdmissionDto dto) throws ApplicationException {
		try {
			List<ComplaintAdmissionDto> listComplaintAdmissionDto;
			StatelessSession statlessSession = sessionFactory.openStatelessSession();
			String query = " select document_path as \"documentPath\" from opd.t_encounter_doc_details tedd where patient_id = :patientId and " +
					" unit_id = :unitId and organisation_id = :organizationId " +
					" and document_type_id =69 order by updated_date  desc limit 1";
			listComplaintAdmissionDto = statlessSession.createSQLQuery(query)
					.setParameter("patientId", dto.getPatientId())
					.setParameter("unitId", dto.getUnitId())
					.setParameter("organizationId", dto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(ComplaintAdmissionDto.class)).list();
			statlessSession.close();
			return new Response(SUCCESS, SUCCESS_CODE, null, listComplaintAdmissionDto, null);
		} catch (HibernateException he) {

		} catch (Exception e) {

		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}



}
