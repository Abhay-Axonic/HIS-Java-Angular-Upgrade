package com.param.lis.transaction.dao;

import java.util.List;

import com.param.entity.lis.transaction.LabSampleDetailsMaster;
import com.param.lis.global.common.Response;
import com.param.lis.global.common.SearchDto;
import com.param.lis.transaction.dto.LabSampleDetailsMasterDto;
import com.param.lis.transaction.dto.LabSampleMasterDto;
import com.param.lis.transaction.dto.SearchCommonDto;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface ILabCentralReceivingDao extends IGenericDao<LabSampleDetailsMaster, Integer>
{
	public Response getSampleListForInOutPatient(Integer orgId, Integer orgUnitId, Integer visitTypeId, Integer offset,
			Integer recordPerPage,Character isSendToCrApplicable) throws ApplicationException;

	public Response acceptOrRejectSample(List<LabSampleDetailsMasterDto> listLabSampleDetailsMasterDto)
			throws ApplicationException;

	public Response getCentrifugationWorklist(Integer orgId, Integer orgUnitId,Integer deptId, Integer offset, Integer recordPerPage)
			throws ApplicationException;

	public Response getTotalRecordOutPatient(SearchDto searchDto) throws ApplicationException;

	public Response getTotalRecordINPatient(SearchDto searchDto) throws ApplicationException;

	public Response getTotalcentrifugationWorkList(Integer orgId, Integer unitId,Integer deptId) throws ApplicationException;

	public Response getRejectedSampleList(Integer orgId, Integer orgUnitId,Integer deptId, Integer offset, Integer recordPerPage)
			throws ApplicationException;

	public Response getTotalRecordRejectedSample(SearchDto searchDto) throws ApplicationException;

	public Response sampleRecollect(LabSampleMasterDto labSampleMasterDto)
			throws ApplicationException;

	public Response sendToDepartMent(List<LabSampleDetailsMasterDto> listlabLabSampleDetailsMasterDto)
			throws ApplicationException;

	// Sample Collection Dao
	public Response samplCollection(LabSampleDetailsMaster labSampleDetailsMaster) throws ApplicationException;
	
	public Response getFilteredOutPatientList(SearchDto searchDto) throws ApplicationException;
	public Response getFilteredCentrifugationWorkList(SearchDto searchDto) throws ApplicationException;

	public Response getRejectedSampleListPatientSearch(SearchCommonDto searchDto)throws ApplicationException;

	public Response getRejectedSampleListSearch(SearchDto searchDto)throws ApplicationException;

	public Response getTotalcentrifugationWorkListCount(SearchDto searchDto) throws ApplicationException;
}
