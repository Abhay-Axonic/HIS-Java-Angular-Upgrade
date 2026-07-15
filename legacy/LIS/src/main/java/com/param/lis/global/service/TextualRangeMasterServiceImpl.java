package com.param.lis.global.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.lis.global.common.Response;
import com.param.lis.global.dao.ITextualRangeMasterDao;
import com.param.lis.global.dto.TextualRangeMasterDto;

import in.param.exception.ApplicationException;
@Service
@SuppressWarnings({"rawtypes"})
public class TextualRangeMasterServiceImpl implements ITextualRangeMasterService, ICommonConstants{

	@Autowired
	ITextualRangeMasterDao iTextualRangeMasterDao;
	@Override
	public Response saveTextualRangeMaster(TextualRangeMasterDto textualRangeMasterDto) throws ApplicationException{
		try{
			Response response=iTextualRangeMasterDao.getTextualRangeMasterByName(textualRangeMasterDto);
			
			Response response1=iTextualRangeMasterDao.getTextualRangeMasterByCode(textualRangeMasterDto);
			
					if (response1.getListObject()!=null &&response1.getListObject().size()>0 )
					{
						return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIT_TEXTUAL_RANGE_CODE, null, null);
					}else if (response.getListObject()!=null &&response.getListObject().size()>0 )
					{
						return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIT_TEXTUAL_RANGE_DESC, null, null);
					}else{
						return iTextualRangeMasterDao.saveTextualRangeMaster(textualRangeMasterDto);
					}
		}catch (Exception e) {
				throw e;
			}
	}

	
	
	@Override
	public Response updateTextualRangeMaster(TextualRangeMasterDto textualRangeMasterDto) throws ApplicationException{
		try{
			Response response=iTextualRangeMasterDao.getTextualRangeMasterByNameNotId(textualRangeMasterDto);
			
			Response response1=iTextualRangeMasterDao.getTextualRangeMasterByCodeNotId(textualRangeMasterDto);
			
				if (response1.getListObject().size()>0 && response1.getListObject()!=null)
				{
					return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIT_TEXTUAL_RANGE_CODE, null, null);
				}else if (response.getListObject().size()>0 && response.getListObject()!=null)
					{
						return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIT_TEXTUAL_RANGE_DESC, null, null);
					}else{
						return iTextualRangeMasterDao.updateTextualRangeMaster(textualRangeMasterDto);
					}
		}catch (Exception e) {
				throw e;
			}
	}

	
	
	@Override
	public Response updateTextualRangeMasterStatus(TextualRangeMasterDto textualRangeMasterDto) throws ApplicationException{
		try{
			return iTextualRangeMasterDao.updateTextualRangeMasterStatus(textualRangeMasterDto);
					
		}catch (Exception e) {
				throw e;
			}
	}

	
	@Override
	public Response getTextualRangeMasterById(Integer textualRangeId, Integer orgId, Integer orgUnitId) throws ApplicationException{
		try{
			return iTextualRangeMasterDao.getTextualRangeMasterById(textualRangeId, orgId, orgUnitId);
					
		}catch (Exception e) {
				throw e;
			}
	}

	
	@Override
	public Response getTextualRangeMasterList(TextualRangeMasterDto textualRangeMasterDto) throws ApplicationException{
		try{
			return iTextualRangeMasterDao.getTextualRangeMasterList(textualRangeMasterDto);
					
		}catch (Exception e) {
				throw e;
			}
	}

	
	@Override
	public Response getActiveTextualRangeMasterList(TextualRangeMasterDto textualRangeMasterDto) throws ApplicationException{
		try{
			return iTextualRangeMasterDao.getActiveTextualRangeMasterList(textualRangeMasterDto);
					
		}catch (Exception e) {
				throw e;
			}
	}

	
	@Override
	public Response getTextualRangeMasterCount(TextualRangeMasterDto textualRangeMasterDto) throws ApplicationException{
		try{
			return iTextualRangeMasterDao.getTextualRangeMasterCount(textualRangeMasterDto);
					
		}catch (Exception e) {
				throw e;
			}
	}

	
	@Override
	public Response getTextualRangeMasterByName(TextualRangeMasterDto textualRangeMasterDto) throws ApplicationException{
		try{
			return iTextualRangeMasterDao.getTextualRangeMasterByName(textualRangeMasterDto);
					
		}catch (Exception e) {
				throw e;
			}
	}
	

	@Override
	public Response getTextualRangeMasterByNameNotId(TextualRangeMasterDto textualRangeMasterDto) throws ApplicationException{
		try{
			return iTextualRangeMasterDao.getTextualRangeMasterByNameNotId(textualRangeMasterDto);
					
		}catch (Exception e) {
				throw e;
			}
	}

}
