package com.param.pharmacy.master.global.service.equipment;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.IError;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.pharmacy.master.global.dao.equipment.IDepartmentStoreMapperDao;
import com.param.pharmacy.master.global.dto.equipment.DepartmentStoreMapperDto;
import com.param.pharmacy.master.global.dto.equipment.WardStoreMapperDto;

import in.param.exception.ApplicationException;
import javassist.expr.NewArray;

import java.util.ArrayList;
import java.util.List;

@Service
@SuppressWarnings({ "unchecked", "rawtypes" })
public class DepartmentStoreMapperServiceImpl implements IDepartmentStoreMapperService, ICommonConstants, IError {

	@Autowired
	private IDepartmentStoreMapperDao iDepartmentStoreMapperDao;

	final static Logger logger = Logger.getLogger(DepartmentStoreMapperServiceImpl.class);

	@Override
	public Response getDeptListById(DepartmentStoreMapperDto departmentStoreMapperDto) throws ApplicationException {
		try {
			return iDepartmentStoreMapperDao.getDeptListById(departmentStoreMapperDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response saveDeptStoreMapper(DepartmentStoreMapperDto departmentStoreMapperDto) throws ApplicationException {
		try {
			return iDepartmentStoreMapperDao.saveDeptStoreMapper(departmentStoreMapperDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getDeptStoremappedList(DepartmentStoreMapperDto departmentStoreMapperDto)
			throws ApplicationException {
		try {
			return iDepartmentStoreMapperDao.getDeptStoremappedList(departmentStoreMapperDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public List<DepartmentStoreMapperDto> getDeptStoremappedListForStoreUpdate(DepartmentStoreMapperDto departmentStoreMapperDto)
			throws ApplicationException {
		try {
			return iDepartmentStoreMapperDao.getDeptStoremappedListForStoreUpdate(departmentStoreMapperDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response activeInactiveTrustMaster(Integer orgId, Integer unitId, Integer deptStoreMapperId,
			Character status) {
		try {
			return iDepartmentStoreMapperDao.activeInactiveTrustMaster(orgId, unitId, deptStoreMapperId, status);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getDeptStoreMapperById(Integer orgId, Integer unitId, Integer deptStoreMapperId)
			throws ApplicationException {
		try {
			return iDepartmentStoreMapperDao.getDeptStoreMapperById(orgId, unitId, deptStoreMapperId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateDeptStoremapper(DepartmentStoreMapperDto departmentStoreMapperDto)
			throws ApplicationException {
		try {
			return iDepartmentStoreMapperDao.updateDeptStoremapper(departmentStoreMapperDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response listDeptStoreMapper(Integer orgId, Integer unitId, Integer offset, Integer recordPerPage)
			throws ApplicationException {
		try {
			return iDepartmentStoreMapperDao.listDeptStoreMapper(orgId, unitId, offset, recordPerPage);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getTotalDeptStoreMapperRecord(Integer orgId, Integer unitId) throws ApplicationException {
		try {
			return iDepartmentStoreMapperDao.getTotalDeptStoreMapperRecord(orgId, unitId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response saveWardStoremapper(WardStoreMapperDto wardStoreMapperDto) {

		try {
			return iDepartmentStoreMapperDao.saveWardStoremapper(wardStoreMapperDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

	}

	@Override
	public Response getWardStoremappedList(WardStoreMapperDto wardStoreMapperDto) {

		try {
			return iDepartmentStoreMapperDao.getWardStoremappedList(wardStoreMapperDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

	}

	@Override
	public List<WardStoreMapperDto> getWardStoremappedListForStoreUpdate(WardStoreMapperDto wardStoreMapperDto) {

			return iDepartmentStoreMapperDao.getWardStoremappedListForStoreUpdate(wardStoreMapperDto);

	}

	@Override
	public Response getWardStoreMapperById(Integer orgId, Integer unitId, Integer wardStoreMapperId) {

		try {
			return iDepartmentStoreMapperDao.getWardStoreMapperById(orgId, unitId, wardStoreMapperId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

	}

	@Override
	public Response updateWardStoremapper(WardStoreMapperDto wardStoreMapperDto) {

		try {
			return iDepartmentStoreMapperDao.updateWardStoremapper(wardStoreMapperDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

	}

	@Override
	public Response activeInactiveWardStoreMapper(Integer orgId, Integer unitId, Integer wardStoreMapperId,
			Character status) {

		try {
			return iDepartmentStoreMapperDao.activeInactiveWardStoreMapper(orgId, unitId, wardStoreMapperId, status);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

	}
	
	/* start code for resolved HIS-3198 Department Store mapper - User should be able to assign the one store to multiple department and vice versa */
	@Override
	public Response getDeptStoremappedListNew(DepartmentStoreMapperDto departmentStoreMapperDto)
			throws ApplicationException {
		try {
			return iDepartmentStoreMapperDao.getDeptStoremappedListNew(departmentStoreMapperDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public Response SaveAndUpdateDeptStroreMapper(DepartmentStoreMapperDto departmentStoreMapperDto) throws ApplicationException {
		try {
			int errorCount = 0,totalEntry = 0 ;
			Response response;
			for (int storeId : departmentStoreMapperDto.getStoreIds()) {
				for (int deptId : departmentStoreMapperDto.getDeptIds()) {
					
					departmentStoreMapperDto.setStoreId(storeId);
					departmentStoreMapperDto.setId(storeId);
					departmentStoreMapperDto.setDeptId(deptId);
					
					if (departmentStoreMapperDto.getDeptStoreMapperId() == null) {
						response = iDepartmentStoreMapperDao.saveDeptStoreMapper(departmentStoreMapperDto);
					} else {
						response = iDepartmentStoreMapperDao.updateDeptStoremapper(departmentStoreMapperDto);
						departmentStoreMapperDto.setDeptStoreMapperId(null);
					}
					if (response.getStatus().equals(ERROR)) {
						errorCount++;
					}
					totalEntry ++;
				}
			}
			if(errorCount > 0 && errorCount==totalEntry ) {
				return new Response<>(ERROR, ERR_500, "Department Store Already Mapped", null, null);
			}else if (errorCount > 0 && totalEntry > 0){
				return new Response(SUCCESS, null, "Some Department Store Already Mapped", null, null);
			}			
			return new Response(SUCCESS, null, null, null, null);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	/* end code for resolved HIS-3198 Department Store mapper - User should be able to assign the one store to multiple department and vice versa */

	@Override
	public Response getWardStoremappedListPagination(WardStoreMapperDto wardStoreMapperDto)
			throws ApplicationException {
		try {
			
			return iDepartmentStoreMapperDao.getWardStoremappedListPagination(wardStoreMapperDto);

	} catch (Exception e) {
		logger.error(e.getMessage(), e);
		throw new ServiceException(e.getMessage());
	}
}

	@Override
	public Response getWardStoremappedListPaginationCount(WardStoreMapperDto wardStoreMapperDto)
			throws ApplicationException {
		try {
			
			return iDepartmentStoreMapperDao.getWardStoremappedListPaginationCount(wardStoreMapperDto);

	} catch (Exception e) {
		logger.error(e.getMessage(), e);
		throw new ServiceException(e.getMessage());
	}
}

}
