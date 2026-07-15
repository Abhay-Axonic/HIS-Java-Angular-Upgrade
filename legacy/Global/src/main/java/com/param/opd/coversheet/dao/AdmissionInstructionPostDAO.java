package com.param.opd.coversheet.dao;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.StatelessSession;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.apache.log4j.Logger;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.otims.dto.ProcedureOrderMasterDto;
import com.param.opd.coversheet.dto.AdmissionOpdDTO;
import com.param.opd.model.InstructionAdmissionMapper;
import com.param.opd.model.ReasonForAdmissionMaster;
/*
 * 
 * Created by Tushar
 */
@Repository
@SuppressWarnings({ "unused", "unchecked" })
public class AdmissionInstructionPostDAO extends GenericDao<InstructionAdmissionMapper, Integer> implements IAdmissionInstructionPostDAO,ICommonConstants{

	private static final Logger logger = Logger.getLogger(AdmissionInstructionPostDAO.class);

	public AdmissionInstructionPostDAO() {
		super(InstructionAdmissionMapper.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Response saveInstructionPostAdmission(AdmissionOpdDTO admissionOpdDTO)
			throws ApplicationException {
		try{
			InstructionAdmissionMapper instructionAdmissionMapper = new InstructionAdmissionMapper();
			instructionAdmissionMapper.setAdmission_id(admissionOpdDTO.getAdmission_id());
			instructionAdmissionMapper.setInstruction_post_admission(admissionOpdDTO.getInstruction_post_admission());
			InstructionAdmissionMapper instructionAdmissionMapperId = save(instructionAdmissionMapper);
			admissionOpdDTO.setInstruction_post_admission_id(instructionAdmissionMapperId.getInstruction_admission_mapper_id());
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, admissionOpdDTO);
		}catch (HibernateException he) {
			logger.error("saveInstructionPostAdmission failed", he);
		} catch (Exception e) {
			logger.error("saveInstructionPostAdmission failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response updateInstructionPostAdmission(
			AdmissionOpdDTO admissionOpdDTO) throws ApplicationException {
		try{
			InstructionAdmissionMapper instructionAdmissionMapper = findById(admissionOpdDTO.getInstruction_post_admission_id());
			if(instructionAdmissionMapper != null){
				instructionAdmissionMapper.setInstruction_post_admission(admissionOpdDTO.getInstruction_post_admission());
				InstructionAdmissionMapper instructionAdmissionMapperId = save(instructionAdmissionMapper);
				admissionOpdDTO.setInstruction_post_admission_id(instructionAdmissionMapperId.getInstruction_admission_mapper_id());
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, admissionOpdDTO);
			}
		}catch (HibernateException he) {
			logger.error("updateInstructionPostAdmission failed", he);
		} catch (Exception e) {
			logger.error("updateInstructionPostAdmission failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public InstructionAdmissionMapper getInstructionPostAdmission(Integer instruction_post_id)
			throws ApplicationException {
		try{
			StatelessSession statlessSession = sessionFactory.openStatelessSession();
			final Criteria criteria = statlessSession.createCriteria(InstructionAdmissionMapper.class)
					.add(Restrictions.eq("instruction_admission_mapper_id",instruction_post_id));
			InstructionAdmissionMapper instructionAdmissionMapper = (InstructionAdmissionMapper) criteria.uniqueResult();
			statlessSession.close();
			if(instructionAdmissionMapper != null){
				return instructionAdmissionMapper;
			}
		}catch (HibernateException he) {
			logger.error("getInstructionPostAdmission failed", he);
		} catch (Exception e) {
			logger.error("getInstructionPostAdmission failed", e);
		}
		return null;
	}

	@Override
	public Response getReasonMasters() throws ApplicationException {
		try{
			StatelessSession statlessSession = sessionFactory.openStatelessSession();
			Criteria criteria = statlessSession.createCriteria(ReasonForAdmissionMaster.class);
			List<ReasonForAdmissionMaster> reasonForAdmissionMasters = criteria.list();
			statlessSession.close();
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, reasonForAdmissionMasters, null );
		}catch (HibernateException he) {
			logger.error("getReasonMasters failed", he);
		} catch (Exception e) {
			logger.error("getReasonMasters failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}

	@Override
	public Response getPatientReturnDetails(AdmissionOpdDTO admissionOpdDTO) throws ApplicationException {
		try
        {
			List<ProcedureOrderMasterDto> list = sessionFactory.getCurrentSession().createSQLQuery(
				"select  unit_id  as \"unitId\" from pharmacy.t_patient_issue_return where admission_id  ="+admissionOpdDTO.getAdmissionId()+"  and status_id  in(2) ")
				.setResultTransformer(Transformers.aliasToBean(ProcedureOrderMasterDto.class)).list();
			
				return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);

        } catch (Exception e)
        {
            logger.error("getPatientReturnDetails failed", e);
        }
        return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}


}
