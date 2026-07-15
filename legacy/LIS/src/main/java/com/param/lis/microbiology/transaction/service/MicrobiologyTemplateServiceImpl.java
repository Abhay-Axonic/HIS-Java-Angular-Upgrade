package com.param.lis.microbiology.transaction.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.Response;
import com.param.lis.global.common.SearchDto;
import com.param.lis.microbiology.transaction.dao.IMicrobiologyDao;
import com.param.lis.microbiology.transaction.dao.IMicrobiologyTemplateDao;
import com.param.lis.transaction.dto.MicrobiologyTemplateDto;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class MicrobiologyTemplateServiceImpl implements IMicrobiologyTemplateService, ICommonConstants, IError {
	
	@Autowired
	IMicrobiologyTemplateDao iMicrobiologyTemplateDao;
	
	@Autowired
	IMicrobiologyDao iMicrobiologyDao;
	

	final static Logger logger = Logger.getLogger(MicrobiologyServiceImpl.class);

	@Override
	public Response saveTemplate(MicrobiologyTemplateDto microbiologyTemplateDto) throws ApplicationException {
		try {
			Response res = iMicrobiologyTemplateDao.saveTemplate(microbiologyTemplateDto);
			if(res.getStatus().equals(SUCCESS)) {
				
				SearchDto dto = new SearchDto();
				
				dto.setLabSampleDtlsId(microbiologyTemplateDto.getLabSampleDtlsId());
				
				if(microbiologyTemplateDto.getReportTypeId() == 1) {
					dto.setOrderStatusId(39);
				}
				if(microbiologyTemplateDto.getReportTypeId() == 2 && (microbiologyTemplateDto.getIsFinalRelease()== null || microbiologyTemplateDto.getIsFinalRelease().equals('N'))) {
					dto.setOrderStatusId(40);
				}
				if(microbiologyTemplateDto.getReportTypeId() == 2 && microbiologyTemplateDto.getIsFinalRelease()!= null && microbiologyTemplateDto.getIsFinalRelease().equals('Y')) {
					dto.setOrderStatusId(41);
				}

				 iMicrobiologyDao.updateLabDetailsStatus(dto);
			}
			return new Response<>(SUCCESS, null, "Save Successfully", null, res.getObject()!=null ? res.getObject():null);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	public Response getTemplate(MicrobiologyTemplateDto microbiologyTemplateDto) throws ApplicationException {
		try {
			
			return iMicrobiologyTemplateDao.getTemplate(microbiologyTemplateDto);
			
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}
}
