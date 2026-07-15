package com.param.pharmacy.master.global.service.equipment;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.pharmacy.master.global.dto.equipment.DepartmentStoreMapperDto;
import com.param.pharmacy.master.global.dto.equipment.WardStoreMapperDto;

import in.param.exception.ApplicationException;

import java.util.List;

@SuppressWarnings("rawtypes")
@Transactional
public interface IDepartmentStoreMapperService {

	public Response getDeptListById(DepartmentStoreMapperDto departmentStoreMapperDto ) throws ApplicationException;

	public Response saveDeptStoreMapper(DepartmentStoreMapperDto departmentStoreMapperDto) throws ApplicationException;

	public Response getDeptStoremappedList(DepartmentStoreMapperDto departmentStoreMapperDto)
			throws ApplicationException;

	public List<DepartmentStoreMapperDto> getDeptStoremappedListForStoreUpdate(DepartmentStoreMapperDto departmentStoreMapperDto)
			throws ApplicationException;

	public Response activeInactiveTrustMaster(Integer orgId, Integer unitId,
			Integer deptStoreMapperId, Character status)throws ApplicationException;

	public Response getDeptStoreMapperById(Integer orgId, Integer unitId,
			Integer deptStoreMapperId)throws ApplicationException;

	public Response updateDeptStoremapper(DepartmentStoreMapperDto departmentStoreMapperDto)
			throws ApplicationException;

	public Response listDeptStoreMapper(Integer orgId, Integer unitId,
			Integer offset, Integer recordPerPage)throws ApplicationException;

	public Response getTotalDeptStoreMapperRecord(Integer orgId, Integer unitId)
			throws ApplicationException;

	public Response saveWardStoremapper(WardStoreMapperDto wardStoreMapperDto);

	public Response getWardStoremappedList(WardStoreMapperDto wardStoreMapperDto);

	public List<WardStoreMapperDto> getWardStoremappedListForStoreUpdate(WardStoreMapperDto wardStoreMapperDto);

	public Response getWardStoreMapperById(Integer orgId, Integer unitId,
			Integer wardStoreMapperId);

	public Response updateWardStoremapper(WardStoreMapperDto wardStoreMapperDto);

	public Response activeInactiveWardStoreMapper(Integer orgId, Integer unitId, Integer wardStoreMapperId, Character status);
	
	public Response getDeptStoremappedListNew(DepartmentStoreMapperDto departmentStoreMapperDto)
			throws ApplicationException;
	public Response SaveAndUpdateDeptStroreMapper(DepartmentStoreMapperDto departmentStoreMapperDto) throws ApplicationException;

	public Response getWardStoremappedListPagination(WardStoreMapperDto wardStoreMapperDto)throws ApplicationException;

	public Response getWardStoremappedListPaginationCount(WardStoreMapperDto wardStoreMapperDto)throws ApplicationException;

}
