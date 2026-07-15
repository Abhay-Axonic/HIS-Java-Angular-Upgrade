package com.param.lis.global.dao;

import com.param.entity.lis.global.MicrobiologyStainMaster;
import com.param.entity.lis.global.StainigMaster;
import com.param.lis.global.common.Response;
import com.param.lis.global.dto.MicrobiologyStainMasterDto;
import com.param.lis.global.dto.StainigMasterDto;
import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IMicrobiologyStainigMasterDao extends IGenericDao<MicrobiologyStainMaster, Integer>{
	
	
	public Response addMicrobStainigMaster(MicrobiologyStainMasterDto microbstainigMasterDto)throws ApplicationException;
	public Response checkMicrobStainigMaster(MicrobiologyStainMasterDto microbstainigMasterDto) throws ApplicationException;
	public Response updateMicrobStainigMaster(MicrobiologyStainMasterDto microbiologyStainMasterDto) throws ApplicationException;
	public Response updateCheckMicrobStainigMasterCodeAvaiable(MicrobiologyStainMasterDto microbiologyStainMasterDto) throws ApplicationException;
	public Response ActivateInactivateMicrobStainigMaster(Integer orgId, Integer  microStainId, Character  stainigMasterStatus) throws ApplicationException;
	public Response listMicrobStainigMaster(Integer orgId, Integer offset, Integer recordPerPage) throws ApplicationException;
	public Response getToTalMicrobStainigMaster(Integer orgId) throws ApplicationException;
	public Response getMicrobStainigMasterById(Integer orgId, Integer  microStainId)
			throws ApplicationException;
	public Response checkMicrobStainigMasterDesc(MicrobiologyStainMasterDto microbstainigMasterDto)throws ApplicationException;
	public Response updateCheckMicrobStainigMasterDescAvaiable(MicrobiologyStainMasterDto microbiologyStainMasterDto)throws ApplicationException;
}
