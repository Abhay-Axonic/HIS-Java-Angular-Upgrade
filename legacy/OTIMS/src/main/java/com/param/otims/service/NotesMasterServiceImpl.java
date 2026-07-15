package com.param.otims.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.TemplateMasterDto;
import com.param.global.otims.dto.NotesMasterDto;
import com.param.global.service.ITemplateMasterService;
import com.param.otims.dao.INotesMasterDao;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({"rawtypes","unused"})
public class NotesMasterServiceImpl implements INotesMasterservice ,ICommonConstants {

	private static final Logger logger = Logger.getLogger(NotesMasterServiceImpl.class);

	@Autowired
	INotesMasterDao iNotesMasterDao;
	
	@Autowired
	private ITemplateMasterService iTemplateMasterService;
	
	@Override
	public Response saveNotesMaster(NotesMasterDto notesMasterDto) throws ApplicationException {
		try {
				
			Integer templateTypeId = 0;
			
			if(notesMasterDto.getTemplateFormatId() != null && notesMasterDto.getTemplateFormatId() != 2){
				
				if(notesMasterDto.getNoteType() == 1){
					templateTypeId = 14;
				}else if(notesMasterDto.getNoteType() == 2){
					templateTypeId = 15;
				}else if(notesMasterDto.getNoteType() == 3){
					templateTypeId = 16;
				}else if(notesMasterDto.getNoteType() == 4){
					templateTypeId = 28;
				}
				// save template master for template Id for template id 
				TemplateMasterDto templateMasterDto = new TemplateMasterDto();
				templateMasterDto.setTemplateFormatId(notesMasterDto.getTemplateFormatId());
				templateMasterDto.setOrgId(notesMasterDto.getOrganizationId());
				templateMasterDto.setUnitId(notesMasterDto.getUnitId());
				templateMasterDto.setCreatedBy(notesMasterDto.getCreatedBy());
				templateMasterDto.setTemplateTypeId(templateTypeId);
				templateMasterDto.setTitle(notesMasterDto.getTitle());
				templateMasterDto.setFormTemplateKeywords(notesMasterDto.getFormTemplateKeywords());
				templateMasterDto.setFormTemplateId(notesMasterDto.getFormTemplateId());
				templateMasterDto.setIsHtmlPrint(false);
				Response res = iTemplateMasterService.saveTemplate(templateMasterDto);
				TemplateMasterDto dtu = (TemplateMasterDto) res.getObject();
				notesMasterDto.setTemplateTypeId(templateTypeId);
				notesMasterDto.setTemplateId(dtu.getTemplateId());
			}
		
			
			return iNotesMasterDao.saveNotesMaster(notesMasterDto);

	} catch (Exception e) {
		logger.error(e.getMessage(), e);
		throw new ServiceException(e.getMessage());
	}

	}
	
	//added by minesh for update NotesMaster.
	@Override
	public Response updateNotesMaster(NotesMasterDto notesMasterDto) throws ApplicationException {
		try {
			Integer templateTypeId = 0;

			if(notesMasterDto.getTemplateFormatId() != null && notesMasterDto.getTemplateFormatId() != 2){

				if(notesMasterDto.getNoteType() == 1){
					templateTypeId = 14;
				}else if(notesMasterDto.getNoteType() == 2){
					templateTypeId = 15;
				}else if(notesMasterDto.getNoteType() == 3){
					templateTypeId = 16;
				}else if(notesMasterDto.getNoteType() == 4){
					templateTypeId = 28;
				}
				// save template master for template Id for template id
				TemplateMasterDto templateMasterDto = new TemplateMasterDto();
				templateMasterDto.setTemplateId(notesMasterDto.getTemplateId());
				templateMasterDto.setTemplateTypeId(notesMasterDto.getTemplateTypeId());
				templateMasterDto.setTemplateFormatId(notesMasterDto.getTemplateFormatId());
				templateMasterDto.setOrgId(notesMasterDto.getOrganizationId());
				templateMasterDto.setUnitId(notesMasterDto.getUnitId());
				templateMasterDto.setCreatedBy(notesMasterDto.getCreatedBy());
				templateMasterDto.setUpdatedBy(notesMasterDto.getCreatedBy());
				templateMasterDto.setTemplateTypeId(templateTypeId);
				templateMasterDto.setTitle(notesMasterDto.getTitle());
				templateMasterDto.setFormTemplateKeywords(notesMasterDto.getFormTemplateKeywords());
				templateMasterDto.setFormTemplateId(notesMasterDto.getFormTemplateId());
				templateMasterDto.setStatus('A');
				templateMasterDto.setIsHtmlPrint(false);
				Response res = iTemplateMasterService.updateTemplate(templateMasterDto);
			}
			return iNotesMasterDao.updateNotesMaster(notesMasterDto);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

	}//end update NotesMaster.

	@Override
	public Response updateNotesMasterStatus(NotesMasterDto notesMasterDto) throws ApplicationException {
		try {
			
			return iNotesMasterDao.updateNotesMasterStatus(notesMasterDto);

	} catch (Exception e) {
		logger.error(e.getMessage(), e);
		throw new ServiceException(e.getMessage());
	}

	}

	@Override
	public Response getNotesMasterList(NotesMasterDto notesMasterDto) throws ApplicationException {
		try {
			
			return iNotesMasterDao.getNotesMasterList(notesMasterDto);

	} catch (Exception e) {
		logger.error(e.getMessage(), e);
		throw new ServiceException(e.getMessage());
	}

	}

	@Override
	public Response getNotesMasterListByNoteTypeId(NotesMasterDto notesMasterDto) throws ApplicationException {
		try {
			
			return iNotesMasterDao.getNotesMasterListByNoteTypeId(notesMasterDto);

	} catch (Exception e) {
		logger.error(e.getMessage(), e);
		throw new ServiceException(e.getMessage());
	}
	}

	@Override
	public Response getNotesMasterListPagination(NotesMasterDto notesMasterDto) throws ApplicationException {
		try {
			
			return iNotesMasterDao.getNotesMasterListPagination(notesMasterDto);

	} catch (Exception e) {
		logger.error(e.getMessage(), e);
		throw new ServiceException(e.getMessage());
	}

	}

	@Override
	public Response getNotesMasterListCount(NotesMasterDto notesMasterDto) throws ApplicationException {
		try {
			
			return iNotesMasterDao.getNotesMasterListCount(notesMasterDto);

	} catch (Exception e) {
		logger.error(e.getMessage(), e);
		throw new ServiceException(e.getMessage());
	}

	}

}
