package com.param.lis.unit.dao;

import com.param.entity.lis.unit.HeaderMaster;
import com.param.lis.global.common.Response;
import com.param.lis.unit.dto.HeaderMasterDto;
import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IHeaderMasterDao extends IGenericDao<HeaderMaster, Integer>
{
	public Response addHeaderMaster(HeaderMasterDto headerMasterDto) throws ApplicationException;

	public Response getHeaderMasterById(Integer orgId,Integer orgUnitId, Integer headerId) throws ApplicationException;

	public Response updateHeaderMaster(HeaderMasterDto headerMasterDto) throws ApplicationException;

	public Response ActivateInactivateHeaderMaster(Integer orgId, Integer headerId, Character headerStatus)
			throws ApplicationException;

	public Response listHeaderMaster(Integer orgId, Integer unitId,Integer offset, Integer recordPerPage) throws ApplicationException;

	public Response getToTalHeaderMasterRecord(Integer orgId,Integer unitId) throws ApplicationException;

	public Response checkForUniqueHeaderMasterDesc(HeaderMasterDto departmentMasterDto) throws ApplicationException;

	public Response updatecheckForUniqueHeaderMasterDesc(HeaderMasterDto headerMasterDto) throws ApplicationException;

	public Response getHeaderParameterList(Integer orgId, Integer unitId, Integer headerId) throws ApplicationException;

}
