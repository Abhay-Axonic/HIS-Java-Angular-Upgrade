package com.param.global.org.common.service;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.param.global.dto.CompanyMasterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.global.InstructionMasterDto;
import com.param.global.dto.global.TranslatedInstructionMasterDto;
import com.param.global.org.common.dao.IInstructionMasterDao;
import com.param.global.unit.dto.TemplateDiagnosisMapperDto;
import com.param.opd.coversheet.dto.PrescriptionDetailsDto;

import in.param.exception.ApplicationException;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings("rawtypes")
@Service
public class InstructionMasterServiceImpl implements IInstructionMasterService, ICommonConstants {
	final static Logger logger = Logger.getLogger(InstructionMasterServiceImpl.class);


	@Autowired
	IInstructionMasterDao iInspectionMasterDao;
	
	@Autowired
	RestTemplate restTemplate;

	private Cache<Integer, List<InstructionMasterDto>> activeInspectionMasterCache;

	@PostConstruct
	public void initCache() {
		activeInspectionMasterCache = CacheBuilder.newBuilder()
				.maximumSize(100)
				.build();
	}
	
	@Override
	public Response saveInspectionMaster(InstructionMasterDto instructionMasterDto) throws ApplicationException {
		Response res = iInspectionMasterDao.getInspectionMasterByName(instructionMasterDto);
		try {
			if (res.getListObject().size() > 0 && res.getListObject() != null) {
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			} else {
				Response resList= iInspectionMasterDao.saveInspectionMaster(instructionMasterDto);
				instructionMasterDto = (InstructionMasterDto)resList.getObject();

				activeInspectionMasterCache.invalidate(instructionMasterDto.getOrganizationId());

				TranslatedInstructionMasterDto dto=new TranslatedInstructionMasterDto();
				if(instructionMasterDto.getAddedLanguageList()!=null && instructionMasterDto.getAddedLanguageList().size() > 0) {
					for (TranslatedInstructionMasterDto tranDto : instructionMasterDto.getAddedLanguageList()) {
						dto.setConvertedText(tranDto.getConvertedText()); 
						dto.setDesc(tranDto.getDesc()); 
						dto.setInstructionId(instructionMasterDto.getInstructionId()); 
						dto.setLanguageId(tranDto.getLanguageId()); 
						dto.setCreatedBy(instructionMasterDto.getCreatedBy()); 
						dto.setCreatedDate(instructionMasterDto.getCreatedDate()); 
						dto.setUpdatedBy(instructionMasterDto.getUpdatedBy()); 
						dto.setUpdatedDate(instructionMasterDto.getUpdatedDate()); 
						dto.setOrganizationId(instructionMasterDto.getOrganizationId()); 
						dto.setUnitId(instructionMasterDto.getUnitId()); 
						dto.setStatus(instructionMasterDto.getStatus()); 
						iInspectionMasterDao.saveTranslatedInstruction(dto);
						
					}
					
				}
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
			}

		} catch (Exception e) {
		logger.error("saveInspectionMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getInspectionMasterList(InstructionMasterDto instructionMasterDto) throws ApplicationException {
		try {
			return iInspectionMasterDao.getInspectionMasterList(instructionMasterDto);
		} catch (Exception e) {
		logger.error("getInspectionMasterList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateInspectionMasterStatus(InstructionMasterDto instructionMasterDto) throws ApplicationException {
		try {
			Response res = iInspectionMasterDao.updateInspectionMasterStatus(instructionMasterDto);
			if(res.getStatus().equals(SUCCESS) && res.getObject() != null) {
				Integer filterId = (Integer) res.getObject();
				activeInspectionMasterCache.invalidate(instructionMasterDto.getOrganizationId() != null ? instructionMasterDto.getOrganizationId() : filterId);
			}
			return res;
		} catch (Exception e) {
		logger.error("updateInspectionMasterStatus failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getInspectionMasterCount(InstructionMasterDto instructionMasterDto) throws ApplicationException {
		try {
			return iInspectionMasterDao.getInspectionMasterCount(instructionMasterDto);
		} catch (Exception e) {
		logger.error("getInspectionMasterCount failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateInspectionMaster(InstructionMasterDto instructionMasterDto) throws ApplicationException {
		Response res = iInspectionMasterDao.getInspectionMasterByNameNotById(instructionMasterDto);
		try {
			if (res.getListObject().size() > 0 && res.getListObject() != null) {
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			} else {
				Response resList= iInspectionMasterDao.updateInspectionMaster(instructionMasterDto);
				instructionMasterDto = (InstructionMasterDto)resList.getObject();

				activeInspectionMasterCache.invalidate(instructionMasterDto.getOrganizationId());

				
				TranslatedInstructionMasterDto dto=new TranslatedInstructionMasterDto();
				if(instructionMasterDto.getAddedLanguageList()!=null && instructionMasterDto.getAddedLanguageList().size() > 0) {
					Response respDto=iInspectionMasterDao.inactiveTranslatedInstruction(instructionMasterDto);

					for (TranslatedInstructionMasterDto tranDto : instructionMasterDto.getAddedLanguageList()) {
						dto.setConvertedText(tranDto.getConvertedText()); 
						dto.setDesc(tranDto.getDesc()); 
						dto.setInstructionId(instructionMasterDto.getInstructionId()); 
						dto.setLanguageId(tranDto.getLanguageId()); 
						dto.setCreatedBy(instructionMasterDto.getCreatedBy()); 
						dto.setCreatedDate(instructionMasterDto.getCreatedDate()); 
						dto.setUpdatedBy(instructionMasterDto.getUpdatedBy()); 
						dto.setUpdatedDate(instructionMasterDto.getUpdatedDate()); 
						dto.setOrganizationId(instructionMasterDto.getOrganizationId()); 
						dto.setUnitId(instructionMasterDto.getUnitId()); 
						dto.setStatus(instructionMasterDto.getStatus()); 
						iInspectionMasterDao.updateTranslatedInstruction(dto);
						
					}
				}
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
				
			}

		} catch (Exception e) {
		logger.error("updateInspectionMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getInspectionMasterListById(int inspectionId, int orgId) throws ApplicationException {
		try {
			return iInspectionMasterDao.getInspectionMasterListById(inspectionId, orgId);
		} catch (Exception e) {
		logger.error("getInspectionMasterListById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getActiveInspectionMasterList(InstructionMasterDto instructionMasterDto) throws ApplicationException {
		try {
			List<InstructionMasterDto> responseCache = activeInspectionMasterCache.getIfPresent(instructionMasterDto.getOrganizationId());
			if (responseCache != null && !responseCache.isEmpty()) {
				return new Response(SUCCESS, SUCCESS_CODE, "List Size Cache: " +String.valueOf(responseCache.size()), responseCache, null);
			}

			Response res = iInspectionMasterDao.getActiveInspectionMasterList(instructionMasterDto);
			if(res.getStatus().equals(SUCCESS) && res.getListObject() != null && !res.getListObject().isEmpty()){
				activeInspectionMasterCache.put(instructionMasterDto.getOrganizationId(), res.getListObject());

				return new Response(SUCCESS, SUCCESS_CODE, "List Size DB: " +String.valueOf(res.getListObject().size()), res.getListObject(), null);
			}
			return res;
		} catch (Exception e) {
		logger.error("getActiveInspectionMasterList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getDeptSpecilityById(InstructionMasterDto instructionMasterDto) throws ApplicationException {
		try {
			return iInspectionMasterDao.getDeptSpecilityById(instructionMasterDto);
		} catch (Exception e) {
		logger.error("getDeptSpecilityById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getSpecialityByDept(InstructionMasterDto instructionMasterDto) {
		try {
			return iInspectionMasterDao.getSpecialityByDept(instructionMasterDto);
		} catch (Exception e) {
		logger.error("getSpecialityByDept failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response convertText() throws ApplicationException {
		try {
			String stringResponse = restTemplate.getForObject("https://www.apertium.org/apy/translate?q=hello%20world&langpair=en|es", String.class);
			
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, stringResponse);
			} catch (Exception e) {
			logger.error("convertText failed", e);
			}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getConvertedLanguageListById(InstructionMasterDto instructionMasterDto)
			throws ApplicationException {
		try {
			return iInspectionMasterDao.getConvertedLanguageListById(instructionMasterDto);
		} catch (Exception e) {
		logger.error("getConvertedLanguageListById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getSearchedInstructionList(InstructionMasterDto instructionMasterDto) throws ApplicationException {
		try {
			return iInspectionMasterDao.getSearchedInstructionList(instructionMasterDto);
		} catch (Exception e) {
		logger.error("getSearchedInstructionList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getMostFrequentlyUsedInstructions(InstructionMasterDto instructionMasterDto)
			throws ApplicationException {
		try {
			return iInspectionMasterDao.getMostFrequentlyUsedInstructions(instructionMasterDto);
		} catch (Exception e) {
		logger.error("getMostFrequentlyUsedInstructions failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getSearchedFreeTextInstructionList(InstructionMasterDto instructionMasterDto)
			throws ApplicationException {
		try {
			return iInspectionMasterDao.getSearchedFreeTextInstructionList(instructionMasterDto);
		} catch (Exception e) {
		logger.error("getSearchedFreeTextInstructionList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}
