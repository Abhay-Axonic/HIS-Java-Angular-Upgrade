package com.param.opd.coversheet.dao;

import java.util.List;

import javax.management.Query;

import org.hibernate.HibernateException;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.opd.common.CommonDateUtils;
import com.param.opd.coversheet.dto.DiagnosisMasterDto;
import com.param.opd.coversheet.model.DiagnosisMaster;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class DiagnosisMasterDaoImpl extends GenericDao<DiagnosisMaster, Integer> implements IDiagnosisMasterDao,ICommonConstants{
	final static Logger logger = Logger.getLogger(DiagnosisMasterDaoImpl.class);


	public DiagnosisMasterDaoImpl() {
		super(DiagnosisMaster.class);
		// TODO Auto-generated constructor stub
	}

	/*@Override
	public Response getListOfDiagnosisMaster(DiagnosisMasterDto diagnosisMasterDto) throws ApplicationException {
		try {
			
			List<DiagnosisMasterDto> DiagnosisMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("DIAGNOSIS_AUTO_FILL_SEARCH")
					.setParameter("diagnosisName", "%" + diagnosisMasterDto.getDiagnosisName().toLowerCase() + "%")
					.setInteger("organizationId", diagnosisMasterDto.getOrganizationId())
					.setInteger("unitId", diagnosisMasterDto.getUnitId())
					.setInteger("doctorId", diagnosisMasterDto.getDoctorId())
					.setResultTransformer(Transformers.aliasToBean(DiagnosisMasterDto.class)).list();
			return new Response(SUCCESS, null, null, DiagnosisMasterDtosList, null);
		} catch (HibernateException he) {
			h

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}*/
	@Override
	public Response getListOfDiagnosisMaster(DiagnosisMasterDto diagnosisMasterDto) throws ApplicationException {
		try {
			
			List<DiagnosisMasterDto> DiagnosisMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("DIAGNOSIS_AUTO_FILL_SEARCH")
//					.setParameter("diagnosisName", "%" + diagnosisMasterDto.getDiagnosisName().toLowerCase() + "%")
					.setParameter("diagnosisName",  diagnosisMasterDto.getDiagnosisName().toLowerCase() + "%")

					//.setInteger("organizationId", diagnosisMasterDto.getOrganizationId())
					//.setInteger("unitId", diagnosisMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(DiagnosisMasterDto.class)).list();
			return new Response(SUCCESS, null, null, DiagnosisMasterDtosList, null);
		} catch (HibernateException he) {
		logger.error("getListOfDiagnosisMaster failed", he);
		} catch (Exception e) {
		logger.error("getListOfDiagnosisMaster failed", e);
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}
	
	
	@Override
	public Response saveDiagnosisMaster(DiagnosisMasterDto diagnosisMasterDto) throws ApplicationException {
	try {
			DiagnosisMaster diagnosisMaster=new DiagnosisMaster();
			diagnosisMaster.setDiagnosisDesc(diagnosisMasterDto.getDiagnosisName());
			diagnosisMaster.setStatus('A');
			diagnosisMaster.setCode(diagnosisMasterDto.getCode());
			diagnosisMaster.setDiagnosisName(diagnosisMasterDto.getDiagnosisName());
			diagnosisMaster.setIsNotifiable('N');
			diagnosisMaster.setShortDesc(diagnosisMasterDto.getDiagnosisName());
			diagnosisMaster.setMarathiDiagnosisDesc(null);
			diagnosisMaster.setCreated_by(diagnosisMasterDto.getCreated_by());
			diagnosisMaster.setCreated_date(CommonDateUtils.getDate(diagnosisMasterDto.getCreated_date(), "dd-MM-yyyy"));
			diagnosisMaster.setUpdated_by(diagnosisMasterDto.getUpdated_by());
			diagnosisMaster.setUpdated_date(CommonDateUtils.getDate(diagnosisMasterDto.getUpdated_date(), "dd-MM-yyyy"));
			diagnosisMaster.setUnitId(diagnosisMasterDto.getUnitId());
			diagnosisMaster.setOrganizationId(diagnosisMasterDto.getOrganizationId());
			diagnosisMaster.setIsComorbidity('N');
		
			save(diagnosisMaster);
			diagnosisMasterDto.setDiagnosisId(diagnosisMaster.getDiagnosisId());
		
		
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, diagnosisMasterDto);
		
			} catch (HibernateException he) {
			logger.error("saveDiagnosisMaster failed", he);
			} catch (Exception e) {
			logger.error("saveDiagnosisMaster failed", e);
			}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}
