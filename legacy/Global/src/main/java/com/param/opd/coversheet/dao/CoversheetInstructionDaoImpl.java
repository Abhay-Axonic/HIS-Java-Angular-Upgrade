package com.param.opd.coversheet.dao;

import java.util.List;


import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.CoversheetTemplateMasterDto;
import com.param.opd.coversheet.dto.CoversheetInstructionDto;
import com.param.opd.coversheet.model.CoversheetInstruction;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class CoversheetInstructionDaoImpl extends GenericDao<CoversheetInstruction, Integer> implements ICoversheetInstructionDao,ICommonConstants{
	final static Logger logger = Logger.getLogger(CoversheetInstructionDaoImpl.class);


	public CoversheetInstructionDaoImpl() {
		super(CoversheetInstruction.class);
	}
	
	@Autowired
	DozerBeanMapper mapper;
	
	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Response saveCoversheetInstruction(CoversheetInstructionDto coversheetInstructionDto)
			throws ApplicationException {
		try {
			CoversheetInstruction coversheetInstruction = mapper.map(coversheetInstructionDto, CoversheetInstruction.class,
					"CoversheetInstructionDto_to_CoversheetInstruction");
//			if(coversheetInstructionDto.getCoversheetInstructionDtoslist()==null) {
				if(coversheetInstructionDto.getEncounterId()!=null) {
					coversheetInstruction.setEncounterId(coversheetInstructionDto.getEncounterId());
				}else {
					coversheetInstruction.setAdmissionId(coversheetInstructionDto.getAdmissionId());

				}
				coversheetInstruction.setPatientId(coversheetInstructionDto.getPatientId());
				coversheetInstruction.setOrganizationId(coversheetInstructionDto.getOrganizationId());
				coversheetInstruction.setUnitId(coversheetInstructionDto.getUnitId());
				coversheetInstruction.setUpdatedBy(coversheetInstructionDto.getUpdatedBy());
				coversheetInstruction.setUpdatedDate(GlobalCommonDateUtils.getDate(coversheetInstructionDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
				coversheetInstruction.setCreatedDate(GlobalCommonDateUtils.getDate(coversheetInstructionDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
				coversheetInstruction.setCreatedBy(coversheetInstructionDto.getCreatedBy());
				coversheetInstruction.setStatus(coversheetInstructionDto.getStatus());
//			}
			coversheetInstruction = save(coversheetInstruction);
			
			return new Response(SUCCESS, SUCCESS_CODE, "Added to Favorites !!", null, null);
		} catch (HibernateException he) {
		logger.error("saveCoversheetInstruction failed", he);
		} catch (Exception e) {
		logger.error("saveCoversheetInstruction failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response coversheetInstructionList(Integer visitId,int visitTypeId) throws ApplicationException {
		try {
			/**/
					
		    StringBuilder sb = new StringBuilder();
		    sb.append(sessionFactory.getCurrentSession().getNamedQuery("GET_COVERSHEET_INSTRUCTION_LIST_BY_ENCOUNTER_ID").getQueryString());
		    
			if(visitTypeId==1)
			{
				sb.append(" where cover.encounterId = :visitId");
				sb.append(" AND cover.status!='D'");
				sb.append(" ORDER BY cover.instructionId asc");
			}
			else if(visitTypeId==2)
			{
				sb.append(" where cover.admissionId = :visitId");
				sb.append(" AND cover.status!='D'");
				sb.append(" ORDER BY cover.instructionId asc");

			}else 
			{
				sb.append(" where cover.admissionId = :visitId");
				sb.append(" AND cover.status!='D'");
				sb.append(" ORDER BY cover.instructionId asc");

			}
			List<CoversheetInstructionDto> listMedicationDto = sessionFactory.getCurrentSession().createQuery(sb.toString())
					.setParameter("visitId", visitId)
					.setResultTransformer(Transformers.aliasToBean(CoversheetInstructionDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listMedicationDto, null);
		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
		logger.error("coversheetInstructionList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response updateCoversheetInstructionStatus(CoversheetInstructionDto coversheetInstructionDto)
			throws ApplicationException {
		try {
			CoversheetInstruction CoversheetInstruction=findById(coversheetInstructionDto.getInstructionId());
			
			CoversheetInstruction.setStatus(coversheetInstructionDto.getStatus());
			CoversheetInstruction.setNotes(coversheetInstructionDto.getNotes());
			CoversheetInstruction.setUpdatedBy(coversheetInstructionDto.getUpdatedBy());
			CoversheetInstruction.setUpdatedDate(GlobalCommonDateUtils.getDate(coversheetInstructionDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			 
			update(CoversheetInstruction);
	
		return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		
		} catch (HibernateException he) {
		logger.error("updateCoversheetInstructionStatus failed", he);
		} catch (Exception e) {
		logger.error("updateCoversheetInstructionStatus failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_UPDATE, null, null);
	}

	@Override
	public Response coversheetInstructionListForBookView(CoversheetInstructionDto coversheetInstructionDto)
			throws ApplicationException {
		try {
		    StringBuilder sb = new StringBuilder();
		    sb.append(sessionFactory.getCurrentSession().getNamedQuery("GET_COVERSHEET_INSTRUCTION_FOR_BOOK_VIEW").getQueryString());
			List<CoversheetInstructionDto> list = sessionFactory.getCurrentSession().createQuery(sb.toString())
					.setParameter("fromDate", coversheetInstructionDto.getFromDate())
					.setParameter("toDate", coversheetInstructionDto.getToDate())
					.setParameter("patientId", coversheetInstructionDto.getPatientId())
					.setResultTransformer(Transformers.aliasToBean(CoversheetInstructionDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
		logger.error("coversheetInstructionListForBookView failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response updateCoversheetInstructionList(CoversheetTemplateMasterDto coversheetTemplateMasterDto) {
		try
		{
			Session session=sessionFactory.getCurrentSession();
			session.createSQLQuery(" update opd.t_instruction  set status ='I'"
			+ " where cov_template_id="+coversheetTemplateMasterDto.getCovTemplateId())
			.executeUpdate();
//			}
			return new Response(SUCCESS, SUCCESS_CODE, null, null, null);
		}catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("updateCoversheetInstructionList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}


}
