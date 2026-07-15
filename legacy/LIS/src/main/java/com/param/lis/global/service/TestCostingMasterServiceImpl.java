package com.param.lis.global.service;

import java.util.ListIterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.IError;
import com.param.lis.global.common.Response;
import com.param.lis.global.dao.ITestCostingMasterDao;
import com.param.lis.global.dto.TestCostingMapperDto;
import com.param.lis.global.dto.TestCostingMasterDto;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class TestCostingMasterServiceImpl implements ITestCostingMasterService ,ICommonConstants,IError{

	@Autowired ITestCostingMasterDao iTestCostingMasterDao;
	
	@Override
	public Response addTestHeadCosting(TestCostingMasterDto dto) throws ApplicationException {
		try{
			Response response= iTestCostingMasterDao.addTestHeadCosting(dto);
			if(response.getStatus()==SUCCESS & response.getObject() != null){
				Integer id =(Integer) response.getObject();
				for(TestCostingMapperDto testCostingMapperDto : dto.getCostHeadList()){
						  if(testCostingMapperDto!=null)
						  {
							  dto.setCost(testCostingMapperDto.getCost());
							  dto.setCostHeadId(testCostingMapperDto.getTestCostHeadId());
							  dto.setTestCostingMasterId(id);
							  iTestCostingMasterDao.saveTestCodeMapperDetails(dto);						    
						  }
					}
			}
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
			
		}catch(Exception e){
			throw e;
		}
	}

	@Override
	public Response getTestHeadCostingById(TestCostingMasterDto dto) throws ApplicationException {
		try{
			 return iTestCostingMasterDao.getTestHeadCostingById(dto);
		}catch(Exception e){
			throw e;
		}
	}

	@Override
	public Response getTestHeadCostingList(TestCostingMasterDto dto) throws ApplicationException {
		try{
			Response res= iTestCostingMasterDao.getTestHeadCostingList(dto);
			if(res.getListObject()!=null && res.getStatus().equals(SUCCESS)){
				
				ListIterator<TestCostingMasterDto> iterator=res.getListObject().listIterator();
				while(iterator.hasNext()){
					TestCostingMasterDto testCostingMasterDto=iterator.next();
					TestCostingMasterDto costingDto=new TestCostingMasterDto();
					costingDto.setTestCostingMasterId(testCostingMasterDto.getTestCostingMasterId());
					Response costRes=iTestCostingMasterDao.getTestHeadCostByCostingMstrId(costingDto);
					double total=0;
					if(costRes.getListObject()!=null ){
						ListIterator<TestCostingMasterDto> iterator1=costRes.getListObject().listIterator();
						
						while(iterator1.hasNext()){
							TestCostingMasterDto testCostingMasterDto1=iterator1.next();
							total=total+(testCostingMasterDto1.getCost()!=null ? testCostingMasterDto1.getCost():0);
						}
						
					}
					
					testCostingMasterDto.setCost(total);
				}
			}
			return res;
			 
		}catch(Exception e){
			throw e;
		}
	}

	@Override
	public Response getTestHeadCostByCostingMstrId(TestCostingMasterDto dto) throws ApplicationException {
		try{
			 return iTestCostingMasterDao.getTestHeadCostByCostingMstrId(dto);
		}catch(Exception e){
			throw e;
		}
	}

}
