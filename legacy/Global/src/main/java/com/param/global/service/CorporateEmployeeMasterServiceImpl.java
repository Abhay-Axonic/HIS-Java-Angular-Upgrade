package com.param.global.service;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.ICorporateEmployeeMasterDao;
import com.param.global.dto.CorporateEmpDependentDetailsDto;
import com.param.global.dto.CorporateEmployeeMasterDto;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class CorporateEmployeeMasterServiceImpl implements ICorporateEmployeeMasterService, ICommonConstants {
	final static Logger logger = Logger.getLogger(CorporateEmployeeMasterServiceImpl.class);


	@Autowired
	ICorporateEmployeeMasterDao iCorporateEmployeeMasterDao;

	@Override
	public Response saveCorporateEmployeeMaster(CorporateEmployeeMasterDto dto) throws ApplicationException {
		try {
			Response res = iCorporateEmployeeMasterDao.saveCorporateEmployeeMaster(dto);

			if (res.getStatus() == "success") {
				if(dto.getDependantsDetailsList()!=null && dto.getDependantsDetailsList().size()>0){
					Integer id = (Integer) res.getObject();
					Iterator itr = dto.getDependantsDetailsList().iterator();
					int cnt=0;
					while (itr.hasNext()) {
						CorporateEmpDependentDetailsDto dto2 = (CorporateEmpDependentDetailsDto) itr.next();
						dto2.setCorporateEmployeeId(id);
						iCorporateEmployeeMasterDao.saveCorporateEmpDependentDetails(dto2);
						cnt++;
					}
					if(dto.getDependantsDetailsList().size()==cnt){
						return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
					}else{
						return new Response(ERROR, null, null, null, null);
					}
				}
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
				
			}
		} catch (Exception e) {
		logger.error("saveCorporateEmployeeMaster failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getCorporateEmployeeMasterDetails(CorporateEmployeeMasterDto dto) throws ApplicationException {
		try {
			return iCorporateEmployeeMasterDao.getCorporateEmployeeMasterDetails(dto);
		} catch (Exception e) {
		logger.error("getCorporateEmployeeMasterDetails failed", e);
		}
		return new Response(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response getCorporateEmployeeDependantsDetails(CorporateEmployeeMasterDto dto) throws ApplicationException {
		try {
			return iCorporateEmployeeMasterDao.getCorporateEmployeeDependantsDetails(dto);
		} catch (Exception e) {
		logger.error("getCorporateEmployeeDependantsDetails failed", e);
		}
		return new Response(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response getCorporateEmployeeMasterDetailsById(CorporateEmployeeMasterDto dto) throws ApplicationException {
		try {
			Response corporateEmpRes= iCorporateEmployeeMasterDao.getCorporateEmployeeMasterDetailsById(dto);
			if(corporateEmpRes.getStatus()==SUCCESS && corporateEmpRes.getListObject().size()>0 && corporateEmpRes.getListObject()!=null){
				CorporateEmployeeMasterDto corporateEmpDto=(CorporateEmployeeMasterDto)corporateEmpRes.getListObject().get(0);
				List<CorporateEmpDependentDetailsDto>  dependentList=null;
				Response empDependentsRes=iCorporateEmployeeMasterDao.getCorporateEmployeeDependantsDetails(dto);
				if(empDependentsRes.getStatus()==SUCCESS && empDependentsRes.getListObject().size()>0 && empDependentsRes.getListObject()!=null){
					dependentList=empDependentsRes.getListObject();
					corporateEmpDto.setDependantsDetailsList(dependentList);
				}
				return new Response(SUCCESS, SUCCESS_CODE, null, null, corporateEmpDto);
			}
		} catch (Exception e) {
		logger.error("getCorporateEmployeeMasterDetailsById failed", e);
		}
		return new Response(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response updateCorporateEmployeeMaster(CorporateEmployeeMasterDto dto) throws ApplicationException {
		try {
			Response res= iCorporateEmployeeMasterDao.updateCorporateEmployeeMaster(dto);
			if(res.getStatus()==SUCCESS){
				Response depUpdateRes=iCorporateEmployeeMasterDao.updateCorporateEmpDependents(dto);
				if(depUpdateRes.getStatus()==SUCCESS && dto.getDependantsDetailsList().size()>0 && dto.getDependantsDetailsList()!=null){
					Iterator itr = dto.getDependantsDetailsList().iterator();
					int cnt=0;
					while (itr.hasNext()) {
						CorporateEmpDependentDetailsDto dto2 = (CorporateEmpDependentDetailsDto) itr.next();
						dto2.setCorporateEmployeeId(dto.getCorporateEmployeeId());
						iCorporateEmployeeMasterDao.saveCorporateEmpDependentDetails(dto2);
						cnt++;
					}
					if(dto.getDependantsDetailsList().size()==cnt){
						return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
					}else{
						return new Response(ERROR, null, null, null, null);
					}
				}
			}
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (Exception e) {
		logger.error("updateCorporateEmployeeMaster failed", e);
		}
		return new Response(ERROR, ERROR_CODE, null, null, null);
	}
	@Override
	public Response searchCorporateEmployee(CorporateEmployeeMasterDto dto) throws ApplicationException {
		try {
			return iCorporateEmployeeMasterDao.searchCorporateEmployee(dto);
		} catch (Exception e) {
		logger.error("searchCorporateEmployee failed", e);
		}
		return new Response(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response updateCorporateEmployeeMasterStatus(CorporateEmployeeMasterDto dto) throws ApplicationException {
		try {
			return iCorporateEmployeeMasterDao.updateCorporateEmployeeMasterStatus(dto);
		} catch (Exception e) {
		logger.error("updateCorporateEmployeeMasterStatus failed", e);
		}
		return new Response(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response verifyCorporateEmployee(CorporateEmployeeMasterDto dto) throws ApplicationException {
		try {
			Response res= iCorporateEmployeeMasterDao.verifyCorporateEmployee(dto);
			if(res.getListObject()!=null && res.getListObject().size()>0){
				if(dto.getTypeOfEmp()==1){
					CorporateEmployeeMasterDto resDto=(CorporateEmployeeMasterDto)res.getListObject().get(0);
					if(dto.getFirstName()!=null && resDto.getFirstName()!=null && resDto.getFirstName().toLowerCase().equals(dto.getFirstName().toLowerCase())) 
					{
						return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, res.getListObject(), 1);
					}else{
						return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, res.getListObject(), 2);
					}
							
					
				}else if(dto.getTypeOfEmp()==2){
					return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, res.getListObject(), 2);
				}else if(dto.getTypeOfEmp()==3){
					return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, 1);
				}else{
					//System.out.println("do nothing");
				}
			}else{
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, 0);
			}
		} catch (Exception e) {
		logger.error("verifyCorporateEmployee failed", e);
		}
		return new Response(ERROR, ERROR_CODE, null, null, null);
	}

}
