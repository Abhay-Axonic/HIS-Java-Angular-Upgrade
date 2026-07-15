package com.param.lis.global.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.param.lis.global.common.ICommonConstants;
import com.param.global.common.IError;
import com.param.global.dto.TestCodeHeadDto;
import com.param.lis.global.common.Response;
import com.param.lis.global.dao.ITestCostHeadDao;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class TestCostHeadServiceImpl implements ITestCostHeadService,ICommonConstants,IError{

	@Autowired ITestCostHeadDao iTestCostHeadDao;
	
	@Override
	public Response addTestCodeHead(TestCodeHeadDto testCodeHeadDto) throws ApplicationException {
		try{
			Response res =  iTestCostHeadDao.checkForUniqueCode(testCodeHeadDto);
			
			Response res1 =  iTestCostHeadDao.checkForUniqueDesc(testCodeHeadDto);
			if(res.getListObject()!=null && res.getListObject().size()>0) {
				return new Response(ERROR, ERR_500, TEST_COST_HEAD_CODE_EXISIS, null, null);
			}else if(res1.getListObject()!=null && res1.getListObject().size()>0) {
				return new Response(ERROR, ERR_500, TEST_COST_HEAD_DESC_EXISIS, null, null);
			}else {
				return iTestCostHeadDao.addTestCodeHead(testCodeHeadDto);	
			}
				
			}catch(Exception e){
				throw e;
			}
	}

	@Override
	public Response updateTestCodeHead(TestCodeHeadDto testCodeHeadDto) throws ApplicationException {
		try{
			Response res =  iTestCostHeadDao.checkUpdateForUniqueCode(testCodeHeadDto);
			
			Response res1 =  iTestCostHeadDao.checkUpdateForUniqueDesc(testCodeHeadDto);
			if(res.getListObject()!=null && res.getListObject().size()>0) {
				return new Response(ERROR, ERR_500, TEST_COST_HEAD_CODE_EXISIS, null, null);
			}else if(res1.getListObject()!=null && res1.getListObject().size()>0) {
				return new Response(ERROR, ERR_500, TEST_COST_HEAD_DESC_EXISIS, null, null);
			}else {
				return iTestCostHeadDao.updateTestCodeHead(testCodeHeadDto);	
			}
		}catch(Exception e){
			throw e;
		}
	}

	@Override
	public Response getTestCodeHeadList(TestCodeHeadDto testCodeHeadDto) throws ApplicationException {
		try{
			return iTestCostHeadDao.getTestCodeHeadList(testCodeHeadDto);
		}catch(Exception e){
			throw e;
		}
	}

	@Override
	public Response updateTestCodeHeadStatus(TestCodeHeadDto testCodeHeadDto) throws ApplicationException {
		try{
			return iTestCostHeadDao.updateTestCodeHeadStatus(testCodeHeadDto);
		}catch(Exception e){
			throw e;
		}
	}

	@Override
	public Response countOfTestCodeHeadList(TestCodeHeadDto testCodeHeadDto) throws ApplicationException {
		try{
			return iTestCostHeadDao.countOfTestCodeHeadList(testCodeHeadDto);
		}catch(Exception e){
			throw e;
		}
	}

	@Override
	public Response getTestCodeHeadListById(TestCodeHeadDto testCodeHeadDto) throws ApplicationException {
		try{
			return iTestCostHeadDao.getTestCodeHeadListById(testCodeHeadDto);
		}catch(Exception e){
			throw e;
		}
	}

	@Override
	public Response getActiveTestCodeHeadList(TestCodeHeadDto dto) throws ApplicationException {
		try{
			return iTestCostHeadDao.getActiveTestCodeHeadList(dto);
		}catch(Exception e){
			throw e;
		}
	}

}
