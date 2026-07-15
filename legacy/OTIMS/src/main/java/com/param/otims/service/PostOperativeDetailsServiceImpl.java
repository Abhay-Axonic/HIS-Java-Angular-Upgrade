package com.param.otims.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.IBillingCommonConstants;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.otims.dao.IOTCompletionNoteDao;
import com.param.global.otims.dto.OTCompletionNoteDto;
import com.param.global.otims.dto.PostOperativeDetailsDto;
import com.param.otims.dao.IPostOperativeDetailsDao;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({ "rawtypes","unchecked"})
public class PostOperativeDetailsServiceImpl implements IPostOperativeDetailsService, ICommonConstants ,IBillingCommonConstants{

	@Autowired
	IPostOperativeDetailsDao iPostOperativeDetailsDao;
	
	@Autowired
	IOTCompletionNoteDao iOTCompletionNoteDao;
	
	@Override
	public Response savePostOperativeDetails(PostOperativeDetailsDto postOperativeDetailsDto)
			throws ApplicationException {
		try {
			Response res =  iPostOperativeDetailsDao.savePostOperativeDetails(postOperativeDetailsDto);
			if(res.getObject() != null ) {
				for (OTCompletionNoteDto dto : postOperativeDetailsDto.getoTCompletionNoteDtoList()) {
					dto.setProReqId(postOperativeDetailsDto.getProReqId());
					iOTCompletionNoteDao.saveOtCompletionNote(dto);
				}
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response getPostOperativeDetails(PostOperativeDetailsDto postOperativeDetailsDto)
			throws ApplicationException {
		try {
			Response res =  iPostOperativeDetailsDao.getPostOperativeDetails(postOperativeDetailsDto);
			PostOperativeDetailsDto dto = new PostOperativeDetailsDto();
			if(res.getObject() != null ) {
				dto =(PostOperativeDetailsDto) res.getObject();
				Response notes =  iOTCompletionNoteDao.getOtNoteList(postOperativeDetailsDto.getProReqId());
				if(notes.getListObject() != null && notes.getListObject().size()>0) {
					dto.setoTCompletionNoteDtoList(notes.getListObject());
				}
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, dto);
		} catch (Exception e) {
			
			throw e;
		}
	}


}
