package com.param.pharmacy.master.global.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import org.apache.log4j.Logger;

import com.param.global.common.ICommonConstants;
import com.param.global.common.IError;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.pharmacy.master.global.dto.equipment.DepartmentStoreMapperDto;
import com.param.pharmacy.master.global.dto.equipment.WardStoreMapperDto;
import com.param.pharmacy.master.global.service.equipment.IDepartmentStoreMapperService;

@RestController
@RequestMapping(value = "/api/globals")
@SuppressWarnings({ "unchecked", "rawtypes" })
public class DepartmentStoreMapperController implements ICommonConstants, IError {
	private static final Logger logger = Logger.getLogger(DepartmentStoreMapperController.class);

	@Autowired
	private IDepartmentStoreMapperService iDepartmentStoreMapperService;

	@RequestMapping(value = "/getDeptListById", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getDeptListById(@RequestBody DepartmentStoreMapperDto departmentStoreMapperDto) {

		Response<DepartmentStoreMapperDto, Integer> response = null;
		try {
			response = iDepartmentStoreMapperService.getDeptListById(departmentStoreMapperDto);
			return response;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/saveDeptStoremapper", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveDeptStoreMapper(@RequestBody DepartmentStoreMapperDto departmentStoreMapperDto) {

		try {
			return iDepartmentStoreMapperService.saveDeptStoreMapper(departmentStoreMapperDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/saveWardStoremapper", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveDeptStoreMapper(@RequestBody WardStoreMapperDto wardStoreMapperDto) {

		try {
			return iDepartmentStoreMapperService.saveWardStoremapper(wardStoreMapperDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/getDeptStoremappedList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getDeptStoremappedList(@RequestBody DepartmentStoreMapperDto departmentStoreMapperDto) {

		Response<DepartmentStoreMapperDto, Integer> response = null;
		try {
			response = iDepartmentStoreMapperService.getDeptStoremappedList(departmentStoreMapperDto);
			return response;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	
	@RequestMapping(value = "/getWardStoremappedList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getWardStoremappedList(@RequestBody WardStoreMapperDto WardStoreMapperDto) {

		Response<WardStoreMapperDto, Integer> response = null;
		try {
			response = iDepartmentStoreMapperService.getWardStoremappedList( WardStoreMapperDto);
			return response;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	
	
	
	
	
	
	@RequestMapping(value = "/activeInactiveDeptStoreMapper/{orgId}/{unitId}/{deptStoreMapperId}/{status}", method = RequestMethod.GET)
	public @ResponseBody Response activeInactiveDeptStoreMapper(@PathVariable(value = "orgId") Integer orgId,
			@PathVariable(value = "unitId") Integer unitId,
			@PathVariable(value = "deptStoreMapperId") Integer deptStoreMapperId,
			@PathVariable(value = "status") Character status) {

		Response<DepartmentStoreMapperDto, Integer> response = null;
		try {
			response = iDepartmentStoreMapperService.activeInactiveTrustMaster(orgId, unitId, deptStoreMapperId,
					status);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		return response;
	}
	
	
	@RequestMapping(value = "/activeInactiveWardStoreMapper/{orgId}/{unitId}/{wardStoreMapperId}/{status}", method = RequestMethod.GET)
	public @ResponseBody Response activeInactiveWardStoreMapper(@PathVariable(value = "orgId") Integer orgId,
			@PathVariable(value = "unitId") Integer unitId,
			@PathVariable(value = "wardStoreMapperId") Integer wardStoreMapperId,
			@PathVariable(value = "status") Character status) {

		Response<DepartmentStoreMapperDto, Integer> response = null;
		try {
			response = iDepartmentStoreMapperService.activeInactiveWardStoreMapper(orgId, unitId, wardStoreMapperId,
					status);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		return response;
	}

	@RequestMapping(value = "/getDeptStoreMapperById/{orgId}/{unitId}/{deptStoreMapperId}", method = RequestMethod.GET)
	public @ResponseBody Response getDeptStoreMapperById(@PathVariable(value = "orgId") Integer orgId,
			@PathVariable(value = "unitId") Integer unitId,
			@PathVariable(value = "deptStoreMapperId") Integer deptStoreMapperId) {

		Response<DepartmentStoreMapperDto, Integer> response = null;
		try {
			response = iDepartmentStoreMapperService.getDeptStoreMapperById(orgId, unitId, deptStoreMapperId);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		return response;
	}

	
	@RequestMapping(value = "/getWardStoreMapperById/{orgId}/{unitId}/{WardStoreMapperId}", method = RequestMethod.GET)
	public @ResponseBody Response getWardStoreMapperById(@PathVariable(value = "orgId") Integer orgId,
			@PathVariable(value = "unitId") Integer unitId,
			@PathVariable(value = "WardStoreMapperId") Integer WardStoreMapperId) {

		Response<WardStoreMapperDto, Integer> response = null;
		try {
			response = iDepartmentStoreMapperService.getWardStoreMapperById(orgId, unitId, WardStoreMapperId);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		return response;
	}
	
	
	
	
	
	
	
	
	@RequestMapping(value = "/updateDeptStoremapper", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateDeptStoremapper(@RequestBody DepartmentStoreMapperDto departmentStoreMapperDto) {

		try {
			return iDepartmentStoreMapperService.updateDeptStoremapper(departmentStoreMapperDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	@RequestMapping(value = "/updateWardStoremapper", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateWardStoremapper(@RequestBody WardStoreMapperDto wardStoreMapperDto) {

		try {
			return iDepartmentStoreMapperService.updateWardStoremapper(wardStoreMapperDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	@RequestMapping(value = "/listDeptStoreMapper/{orgId}/{unitId}/{offset}/{recordPerPage}", method = RequestMethod.GET)
	public @ResponseBody Response listDeptStoreMapper(@PathVariable(value = "orgId") Integer orgId,
			@PathVariable(value = "unitId") Integer unitId, @PathVariable(value = "offset") Integer offset,
			@PathVariable(value = "recordPerPage") Integer recordPerPage) {
		Response<DepartmentStoreMapperDto, Integer> response = null;
		try {
			response = iDepartmentStoreMapperService.listDeptStoreMapper(orgId, unitId, offset, recordPerPage);
			return response;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/getTotalDeptStoreMapperRecord/{orgId}/{unitId}", method = RequestMethod.GET)
	public @ResponseBody Response getTotalDeptStoreMapperRecord(@PathVariable(value = "orgId") Integer orgId,
			@PathVariable(value = "unitId") Integer unitId) {
		Response<DepartmentStoreMapperDto, Integer> response = null;
		try {
			response = iDepartmentStoreMapperService.getTotalDeptStoreMapperRecord(orgId, unitId);
			return response;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	/* start code for resolved HIS-3198 Department Store mapper - User should be able to assign the one store to multiple department and vice versa */
	@RequestMapping(value = "/getDeptStoremappedListNew", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getDeptStoremappedListNew(@RequestBody DepartmentStoreMapperDto departmentStoreMapperDto) {
		Response<DepartmentStoreMapperDto, Integer> response = null;
		try {
			response = iDepartmentStoreMapperService.getDeptStoremappedListNew(departmentStoreMapperDto);
			return response;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/SaveAndUpdateDeptStroreMapper", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response SaveAndUpdateDeptStroreMapper(@RequestBody DepartmentStoreMapperDto departmentStoreMapperDto) {

		try {
			return iDepartmentStoreMapperService.SaveAndUpdateDeptStroreMapper(departmentStoreMapperDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getWardStoremappedListPagination", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getWardStoremappedListPagination(@RequestBody WardStoreMapperDto WardStoreMapperDto) {

		Response<WardStoreMapperDto, Integer> response = null;
		try {
			response = iDepartmentStoreMapperService.getWardStoremappedListPagination( WardStoreMapperDto);
			return response;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getWardStoremappedListPaginationCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getWardStoremappedListPaginationCount(@RequestBody  WardStoreMapperDto WardStoreMapperDto){

		Response<WardStoreMapperDto, Integer> response = null;
		try {
			response = iDepartmentStoreMapperService.getWardStoremappedListPaginationCount( WardStoreMapperDto);
			return response;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	/* end code for resolved HIS-3198 Department Store mapper - User should be able to assign the one store to multiple department and vice versa */
	
}
