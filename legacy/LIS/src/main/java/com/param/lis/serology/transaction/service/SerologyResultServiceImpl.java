package com.param.lis.serology.transaction.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.entity.lis.transaction.LabSampleDetailsMaster;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.dto.TemplateTransactionDto;
import com.param.global.opd.service.IEncounterMasterService;
import com.param.global.service.IOrderMasterService;
import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.ITransactionConstants;
import com.param.lis.global.common.Response;
import com.param.lis.global.common.SearchDto;
import com.param.lis.microbiology.transaction.dao.IMicrobiologyDao;
import com.param.lis.microbiology.transaction.dto.MicrobioParamDto;
import com.param.lis.serology.dto.SerologyResultTestMapperDto;
import com.param.lis.serology.dto.SerologyTestResultDto;
import com.param.lis.serology.transaction.dao.ISerologyResultDao;
import com.param.lis.transaction.dto.LabSampleDetailsMasterDto;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class SerologyResultServiceImpl implements ISerologyResultService, ICommonConstants, ITransactionConstants, IError {

	@Autowired
	ISerologyResultDao iSerologyResultDao;
	
	@Autowired
	IMicrobiologyDao iMicrobiologyDao;
	
	@Autowired
	IOrderMasterService iOrderMasterService;
	
	
		@Autowired
		IEncounterMasterService iEncounterMasterService;
	
	final static Logger logger = Logger.getLogger(SerologyResultServiceImpl.class);
	@Override
	@Transactional
	public Response getSerologyResultEntryList(MicrobioParamDto dto) throws ApplicationException {
		try {
			return iSerologyResultDao.getSerologyResultEntryList(dto);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getSerologyResultEntryListCount(MicrobioParamDto dto) throws ApplicationException {
		try {
			return iSerologyResultDao.getSerologyResultEntryListCount(dto);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response saveResultEntry(List<SerologyTestResultDto> list) throws ApplicationException {
		try {
			
			for (SerologyTestResultDto dto : list) {
					Date date = TimeZoneComponent.getDateByzone(dto.getOrgUnitId());
					String dateTime = GlobalCommonDateUtils.getStringDate(date, "dd-M-yyyy HH:mm:ss");
					dto.setCreatedBy(dto.getCreatedBy());
					dto.setCreatedDate(dateTime);
					dto.setUpdatedDate(dateTime);
					dto.setResultEnterDate(dateTime);
					dto.setResultEnterBy(dto.getCreatedBy());
//					dto.setIsOnHoldParameter(dto.getIsOnHoldParameter());
					Response res = iSerologyResultDao.saveResultEntry(dto);
					if(res.getObject()!=null && res.getCode().equals(SUCCESS_200))
					{
						Integer serologyTestResultId  = (Integer)res.getObject();
						for (Iterator iterator = dto.getSerologyResultTestMapperDtoList().iterator(); iterator.hasNext();) 
						{
							SerologyResultTestMapperDto mapperDto = (SerologyResultTestMapperDto) iterator.next();
							mapperDto.setSerologyTestResultId(serologyTestResultId);
							mapperDto.setCreatedBy(dto.getCreatedBy());
							mapperDto.setCreatedDate(dateTime);
							mapperDto.setOrgId(dto.getOrgId());
							mapperDto.setOrgUnitId(dto.getOrgUnitId());
						    iSerologyResultDao.serologyResultTestMapper(mapperDto);
						}
						
						LabSampleDetailsMasterDto smpDto = new LabSampleDetailsMasterDto();
						smpDto.setOrgId(dto.getOrgId());
						smpDto.setOrgUnitId(dto.getOrgUnitId());
						smpDto.setAssignUnit(dto.getOrgUnitId());
						smpDto.setPerformedUnit(dto.getOrgUnitId());
						smpDto.setCreatedBy(dto.getCreatedBy());
						smpDto.setInternalNotes(dto.getInternalNotes());
						smpDto.setLabSampleDtlsId(dto.getLabSampleDtlsId());
						smpDto.setIsOnHoldParameter(dto.getIsOnHoldParameter());
						smpDto.setSampleStatusId(RESULT_ENTRY_DONE);
						smpDto.setProvisional(dto.getProvisional()!= null ? dto.getProvisional() : false);
						smpDto.setCreatedDate(date);
						iMicrobiologyDao.updateLabSampleDetailsMaster(smpDto);
				
					}
				
			}
			return new Response(SUCCESS, SUCCESS_200, SAMPLE_RESULT_ENTRY_SUCC, null, null);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response searchSerologyResultEntryList(SearchDto dto) throws ApplicationException {
		try {
			return iSerologyResultDao.searchSerologyResultEntryList(dto);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getSerologyReportEntryDetails(List<Integer> labSampleDtlsId) throws ApplicationException {
		try {
			return iSerologyResultDao.getSerologyReportEntryDetails(labSampleDtlsId);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getSerologyValidationList(MicrobioParamDto microbioParamDto) throws ApplicationException {
		try {
			return iSerologyResultDao.getSerologyValidationList(microbioParamDto);
		} catch (Exception e) 
		{
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getSerologyValidationListCount(MicrobioParamDto microbioParamDto) throws ApplicationException {
		try {
			return iSerologyResultDao.getSerologyValidationListCount(microbioParamDto);
		} catch (Exception e) 
		{
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response searchSerologyValidationList(SearchDto dto) throws ApplicationException {
		try {
			return iSerologyResultDao.searchSerologyValidationList(dto);
		} catch (Exception e) 
		{
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response saveValidation(List<SerologyTestResultDto> list) throws ApplicationException {
		try {
			
			for (SerologyTestResultDto dto : list) {
				
					Date date = TimeZoneComponent.getDateByzone(dto.getOrgUnitId());
					String dateTime = GlobalCommonDateUtils.getStringDate(date, "yyyy-M-dd HH:mm:ss");
					dto.setResultValidatedBy(dto.getCreatedBy());
					dto.setResultValidatedDate(dateTime);
					dto.setCreatedBy(dto.getCreatedBy());
					dto.setCreatedDate(dateTime);
					Response res =  iSerologyResultDao.saveValidation(dto);
					if(res.getObject()!=null && res.getCode().equals(SUCCESS_200))
					{
						LabSampleDetailsMasterDto smpDto = new LabSampleDetailsMasterDto();
						smpDto.setOrgId(dto.getOrgId());
						smpDto.setOrgUnitId(dto.getOrgUnitId());
						smpDto.setAssignUnit(dto.getOrgUnitId());
						smpDto.setInternalNotes(dto.getInternalNotes());
						smpDto.setPerformedUnit(dto.getOrgUnitId());
						smpDto.setCreatedBy(dto.getCreatedBy());
						smpDto.setLabSampleDtlsId(dto.getLabSampleDtlsId());
						smpDto.setIsOnHoldParameter(dto.getIsOnHoldParameter());
						smpDto.setSampleStatusId(RESULT_VALIDATION_DONE);
						smpDto.setProvisional(dto.getProvisional()!= null ? dto.getProvisional() : false);
						smpDto.setCreatedDate(date);
						iMicrobiologyDao.updateLabSampleDetailsMaster(smpDto);
						
					}
			}
			
			return new Response(SUCCESS, SUCCESS_200, SAMPLE_RESULT_ENTRY_VALIDATION_SUCC, null, null);
		} catch (Exception e) 
		{
		throw new ApplicationException(e.getMessage(), e);
	}
	}

	@Override
	@Transactional
	public Response getSerologyReportValidationDetails(List<Integer> labSampleDtlsId) throws ApplicationException {
		try {
			return iSerologyResultDao.getSerologyReportValidationDetails(labSampleDtlsId);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getSerologyReportReleaseList(MicrobioParamDto microbioParamDto) throws ApplicationException {
		try {
			return iSerologyResultDao.getSerologyReportReleaseList(microbioParamDto);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getSerologyReportReleaseCount(MicrobioParamDto microbioParamDto) throws ApplicationException {
		try {
			return iSerologyResultDao.getSerologyReportReleaseCount(microbioParamDto);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response searchSerologyReportReleaseList(SearchDto dto) throws ApplicationException {
		try {
			return iSerologyResultDao.searchSerologyReportReleaseList(dto);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response saveReportRelease(SerologyTestResultDto dto) throws ApplicationException {
try {
			
			Date date = TimeZoneComponent.getDateByzone(dto.getOrgUnitId());
			String dateTime = GlobalCommonDateUtils.getStringDate(date, "yyyy-M-dd HH:mm:ss");
			dto.setReportReleaseBy((dto.getReportReleaseBy()!=null && dto.getReportReleaseBy()>0)?dto.getReportReleaseBy():dto.getCreatedBy());
			dto.setReportReleaseDate(dateTime);
			dto.setCreatedBy(dto.getCreatedBy());
			return  iSerologyResultDao.saveReportRelease(dto);
			
			/*if(res.getObject()!=null && res.getCode().equals(SUCCESS_200))
			{
				if(dto.getIsOnHoldParameter() == true) {
					LabSampleDetailsMasterDto smpDto = new LabSampleDetailsMasterDto();
					smpDto.setOrgId(dto.getOrgId());
					smpDto.setOrgUnitId(dto.getOrgUnitId());
					smpDto.setAssignUnit(dto.getOrgUnitId());
					smpDto.setPerformedUnit(dto.getOrgUnitId());
					smpDto.setCreatedBy(dto.getCreatedBy());
					smpDto.setLabSampleDtlsId(dto.getLabSampleDtlsId());
					smpDto.setSampleStatusId(REPORT_RELEASED);
					smpDto.setCreatedDate(date);
					iMicrobiologyDao.updateLabSampleDetailsMaster(smpDto);
				}
			}*/
//			return new Response(SUCCESS, SUCCESS_200, REPORT_RELEASE_SUCC, null, null);
		} catch (Exception e) 
		{
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response updateReportRelease(SerologyResultTestMapperDto mapperDto) throws ApplicationException {
		try {
			return iSerologyResultDao.updateReportRelease(mapperDto);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response ResultEntryOrReleaseEntry(List<SerologyTestResultDto> list) throws ApplicationException {
		try {
			Response labSampleDetailsMasterRes = null;
			List<LabSampleDetailsMasterDto> mstrList = new ArrayList<LabSampleDetailsMasterDto>();
 			for (SerologyTestResultDto dto : list) {
					Date date = TimeZoneComponent.getDateByzone(dto.getOrgUnitId());
					String dateTime = GlobalCommonDateUtils.getStringDate(date, "dd-M-yyyy HH:mm:ss");
					dto.setCreatedBy(dto.getCreatedBy());
					dto.setCreatedDate(dateTime);
					dto.setUpdatedDate(dateTime);
					dto.setResultEnterDate(dateTime);
					dto.setResultEnterBy(dto.getCreatedBy());
					Response res = iSerologyResultDao.ResultEntryOrReleaseEntry(dto);
					if(res.getObject()!=null && res.getCode().equals(SUCCESS_200))
					{
						Integer serologyTestResultId  = (Integer)res.getObject();
						for (Iterator iterator = dto.getSerologyResultTestMapperDtoList().iterator(); iterator.hasNext();) 
						{
							SerologyResultTestMapperDto mapperDto = (SerologyResultTestMapperDto) iterator.next();
							mapperDto.setSerologyTestResultId(serologyTestResultId);
							mapperDto.setCreatedBy(dto.getCreatedBy());
							mapperDto.setCreatedDate(dateTime);
							mapperDto.setOrgId(dto.getOrgId());
							mapperDto.setOrgUnitId(dto.getOrgUnitId());
						    iSerologyResultDao.serologyResultTestMapperAndRelease(mapperDto);
						}
						
						LabSampleDetailsMasterDto smpDto = new LabSampleDetailsMasterDto();
						smpDto.setOrgId(dto.getOrgId());
						smpDto.setOrgUnitId(dto.getOrgUnitId());
						smpDto.setAssignUnit(dto.getOrgUnitId());
						smpDto.setPerformedUnit(dto.getOrgUnitId());
						smpDto.setCreatedBy(dto.getCreatedBy());
						smpDto.setLabSampleDtlsId(dto.getLabSampleDtlsId());
						smpDto.setSampleStatusId(REPORT_RELEASED);
						smpDto.setInternalNotes(dto.getInternalNotes());
						smpDto.setIsOnHoldParameter(dto.getIsOnHoldParameter());
						smpDto.setCreatedDate(date);
					 labSampleDetailsMasterRes = 	iMicrobiologyDao.updateLabSampleDetailsMaster(smpDto);
					 
					 if(labSampleDetailsMasterRes!=null && labSampleDetailsMasterRes.getObject()!=null) {
						 LabSampleDetailsMaster msts = (LabSampleDetailsMaster)labSampleDetailsMasterRes.getObject();
						 LabSampleDetailsMasterDto newDto = new LabSampleDetailsMasterDto();
						 newDto.setLabSampleDtlsId( msts.getLabSampleDtlsId());
						 newDto.setPatientId(msts.getPatientId());
						 newDto.setDoctorId(msts.getDoctorId());
						 newDto.setTestDesc(dto.getTestDesc());
						 mstrList.add(newDto);
					 }
						
						iOrderMasterService.assignReviewApplicableDate(dto.getOrderDetailsId());
						
						 //for feedbackform
			             TemplateTransactionDto templateDto = new TemplateTransactionDto();
			             templateDto.setLabSampleDtlsId(dto.getLabSampleDtlsId());
			             iEncounterMasterService.checkFeedBackFormSubmitted(templateDto);
						
					}
				
			}
			return new Response(SUCCESS, SUCCESS_200, SAMPLE_RESULT_ENTRY_SUCC, mstrList, null);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response updateResultEntry(List<SerologyTestResultDto> list) throws ApplicationException {
		try {
			
			for (SerologyTestResultDto dto : list) {
					Date date = TimeZoneComponent.getDateByzone(dto.getOrgUnitId());
					String dateTime = GlobalCommonDateUtils.getStringDate(date, "dd-M-yyyy HH:mm:ss");
					dto.setCreatedBy(dto.getCreatedBy());
					dto.setCreatedDate(dateTime);
					dto.setUpdatedDate(dateTime);
					dto.setResultEnterDate(dateTime);
					dto.setResultEnterBy(dto.getCreatedBy());
//					dto.setIsOnHoldParameter(dto.getIsOnHoldParameter());
					Response res = iSerologyResultDao.updateResultEntry(dto);
					if(res.getObject()!=null && res.getCode().equals(SUCCESS_200))
					{
						SerologyTestResultDto dt  = (SerologyTestResultDto)res.getObject();
						for (Iterator iterator = dto.getSerologyResultTestMapperDtoList().iterator(); iterator.hasNext();) 
						{
							SerologyResultTestMapperDto mapperDto = (SerologyResultTestMapperDto) iterator.next();
							mapperDto.setSerologyTestResultId(dto.getSerologyTestResultId());
							mapperDto.setCreatedBy(dto.getCreatedBy());
							mapperDto.setCreatedDate(dateTime);
							mapperDto.setOrgId(dto.getOrgId());
							mapperDto.setOrgUnitId(dto.getOrgUnitId());
							mapperDto.setSerologyResultTestMpprId(mapperDto.getSerologyResultTestMpprId());
						    iSerologyResultDao.updateSerologyResultTestMapper(mapperDto);
						}
						
						LabSampleDetailsMasterDto smpDto = new LabSampleDetailsMasterDto();
						smpDto.setOrgId(dto.getOrgId());
						smpDto.setOrgUnitId(dto.getOrgUnitId());
						smpDto.setAssignUnit(dto.getOrgUnitId());
						smpDto.setPerformedUnit(dto.getOrgUnitId());
						smpDto.setCreatedBy(dto.getCreatedBy());
						smpDto.setInternalNotes(dto.getInternalNotes());
						smpDto.setLabSampleDtlsId(dto.getLabSampleDtlsId());
						smpDto.setIsOnHoldParameter(dto.getIsOnHoldParameter());
						if(dto.getSampleStatusId() != null){
							smpDto.setSampleStatusId(dto.getSampleStatusId());
						}else{
							smpDto.setSampleStatusId(RESULT_ENTRY_DONE);
						}
						
						smpDto.setCreatedDate(date);
						iMicrobiologyDao.updateLabSampleDetailsMaster(smpDto);
				
					}
				
			}
			return new Response(SUCCESS, SUCCESS_200, SAMPLE_RESULT_ENTRY_SUCC, null, null);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response recollectSampleForSerlogy(SearchDto dto) throws ApplicationException {
		try {
			return iSerologyResultDao.recollectSampleForSerlogy(dto);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response retestSampleForSerlogy(SearchDto dto) throws ApplicationException {
		try {
			return iSerologyResultDao.retestSampleForSerlogy(dto);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response updateAmend(List<SerologyTestResultDto> list) throws ApplicationException {
		try {
			return iSerologyResultDao.updateAmend(list);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}
}
