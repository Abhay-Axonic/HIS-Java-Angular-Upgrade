package com.param.global.dao;

/*import com.param.adt.master.unit.model.NursingStationMaster;*/

import com.param.adt.master.unit.model.NursingStationMaster;
import com.param.global.common.Response;
import com.param.global.dto.NursingStationMasterDto;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface INursingStationDao extends IGenericDao<NursingStationMaster, Integer>{

	
	public Response getListOfNursingStationMaster(NursingStationMasterDto nursingStationMasterDto)throws ApplicationException;
	
	public Response getListOfNursingStationMasterByClinicId(NursingStationMasterDto nursingStationMasterDto)throws ApplicationException;
	
	public Response saveNursingStationMaster(NursingStationMasterDto nursingStationMasterDto) throws ApplicationException;

	public Response getNursingStationMasterById(int nursingId, int orgId, int unitId) throws ApplicationException;

	public Response getNursingStationMasterList(NursingStationMasterDto nursingStationMasterDto) throws ApplicationException;

	public Response updateNursingStationMaster(NursingStationMasterDto nursingStationMasterDto) throws ApplicationException;

	public Response updateNursingStationMasterStatus(NursingStationMasterDto nursingStationMasterDto) throws ApplicationException;

	public Response getNursingStationMasterCount(NursingStationMasterDto nursingStationMasterDto) throws ApplicationException;

	public Response getNursingStationMasterByName(NursingStationMasterDto nursingStationMasterDto) throws ApplicationException;
	
	public Response getNursingStationMasterByNameNotById(NursingStationMasterDto nursingStationMasterDto) throws ApplicationException;
	
}
