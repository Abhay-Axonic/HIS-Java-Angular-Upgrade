package com.param.lis.histopathology.transaction.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.ITransactionConstants;
import com.param.lis.global.common.Response;
import com.param.lis.global.common.SearchDto;
import com.param.lis.histopathology.transaction.dao.IHistoMacroScopicExaminationDao;
import com.param.lis.histopathology.transaction.dto.TSpecimanMasterDto;
import com.param.lis.microbiology.transaction.dao.IMicrobiologyDao;
import com.param.lis.transaction.dto.LabSampleDetailsMasterDto;
import com.param.lis.transaction.dto.SearchCommonDto;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings(
{ "rawtypes", "unchecked" })
public class HistoMacroScopicExaminationServiceImpl  implements IHistoMacroScopicExaminationService, ICommonConstants,ITransactionConstants ,IError
{
	
	final static Logger logger = Logger.getLogger(HistoMacroScopicExaminationServiceImpl.class);
	
	@Autowired
	IHistoMacroScopicExaminationDao iHistoMicroScopicExaminationDao;

	@Autowired
	private IMicrobiologyDao iMicrobiologyDao;
	 
	@Override
	@Transactional
	public Response listSpecimanReceipt(SearchDto searchDto) throws ApplicationException 
	{
		try
		{
			return iHistoMicroScopicExaminationDao.listSpecimanReceipt(searchDto);
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getCollectedSpecimensCount(SearchDto searchDto) throws ApplicationException 
	{
		try
		{
			return iHistoMicroScopicExaminationDao.getCollectedSpecimensCount(searchDto);
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response saveMicroscopicExaminationDetails(TSpecimanMasterDto tSpecimanMasterDto)
			throws ApplicationException 
	{
		try
		{
			Response grossRes = iHistoMicroScopicExaminationDao.saveMicroscopicExaminationDetails(tSpecimanMasterDto);
			if(grossRes.getCode().equals(SUCCESS_200))
			{
				LabSampleDetailsMasterDto labSampleDetailsMasterDto = new LabSampleDetailsMasterDto();
				labSampleDetailsMasterDto.setOrgId(tSpecimanMasterDto.getOrgId());
				labSampleDetailsMasterDto.setOrgUnitId(tSpecimanMasterDto.getOrgUnitId());
				labSampleDetailsMasterDto.setCreatedBy(tSpecimanMasterDto.getUpdatedBy());
			//	labSampleDetailsMasterDto.setUpdatedDate(tSpecimanMasterDto.getUpdatedDate());
				labSampleDetailsMasterDto.setPerformedUnit(tSpecimanMasterDto.getOrgUnitId());
				labSampleDetailsMasterDto.setAssignUnit(tSpecimanMasterDto.getOrgUnitId());
				labSampleDetailsMasterDto.setLabSampleDtlsId(tSpecimanMasterDto.getLabSampleDtlsId());
				labSampleDetailsMasterDto.setSampleStatusId(GROSS_CREATED);
				Response labSampleDtlsRes =  iMicrobiologyDao.updateLabSampleDetailsMaster(labSampleDetailsMasterDto);
				if(labSampleDtlsRes.getCode().equals(SUCCESS_200))
				{
					return new Response(SUCCESS, SUCCESS_200, GROSS_CREATE_SUCCESS, null, null);
				}
				else {
					return new Response(ERROR, ERR_500, GROSS_CREATE_FAIL, null, null);
				}
			}
			else
			{
				return new Response(ERROR, ERR_500, GROSS_CREATE_FAIL, null, null);
			}
			
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response saveSpecimanDetails(TSpecimanMasterDto tSpecimanMasterDto) throws ApplicationException 
	{
		try
		{
			return iHistoMicroScopicExaminationDao.saveSpecimanDetails(tSpecimanMasterDto);
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response searchPatientbyVisitType(SearchCommonDto searchCommonDto) throws ApplicationException {
		try
		{
			return iHistoMicroScopicExaminationDao.searchPatientbyVisitType(searchCommonDto);
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response searchBySpecimenType(SearchCommonDto searchCommonDto) throws ApplicationException {
		try
		{
			return iHistoMicroScopicExaminationDao.searchBySpecimenType(searchCommonDto);
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response searchByTest(SearchCommonDto searchCommonDto) throws ApplicationException {
		try
		{
			return iHistoMicroScopicExaminationDao.searchByTest(searchCommonDto);
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response searchAcceptedSpecimanByPatient(SearchCommonDto searchCommonDto) throws ApplicationException {
		try
		{
			return iHistoMicroScopicExaminationDao.searchAcceptedSpecimanByPatient(searchCommonDto);
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response accetedSpecimenSearch(SearchDto searchDto) throws ApplicationException 
	{
		try
		{
			return iHistoMicroScopicExaminationDao.accetedSpecimenSearch(searchDto);
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getMicroscopicExamDataBySubSpecimenId(Integer subSpecimanId, Integer orgId, Integer orgUnitId,Character isHistoCyto)
			throws ApplicationException {
		try
		{
			return iHistoMicroScopicExaminationDao.getMicroscopicExamDataBySubSpecimenId(subSpecimanId, orgId, orgUnitId, isHistoCyto);
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getSubSpecimanByLabSampleDtlsId(Integer labSampleDtlsId,Integer orgId, Integer orgUnitId) throws ApplicationException {
		try
		{
			return iHistoMicroScopicExaminationDao.getSubSpecimanByLabSampleDtlsId(labSampleDtlsId,orgId, orgUnitId);
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}

	
	
	@Override
	@Transactional
	public Response getTestList(Integer orgId, Integer orgUnitId) throws ApplicationException {
		try
		{
			return iHistoMicroScopicExaminationDao.getTestList(orgId, orgUnitId);
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}

	
}
