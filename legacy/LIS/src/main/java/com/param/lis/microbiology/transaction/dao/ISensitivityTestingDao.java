package com.param.lis.microbiology.transaction.dao;

import java.util.List;

import com.param.entity.lis.micro.SensitivityTestResultMaster;
import com.param.lis.global.common.Response;
import com.param.lis.global.common.SearchDto;
import com.param.lis.microbiology.transaction.dto.MicrobioParamDto;
import com.param.lis.microbiology.transaction.dto.SensitivityTestResultDetailsMasterDto;
import com.param.lis.microbiology.transaction.dto.SensitivityTestResultMasterDto;
import com.param.lis.microbiology.transaction.dto.TMicroIncubationMasterDto;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface ISensitivityTestingDao extends IGenericDao<SensitivityTestResultMaster, Integer> {

	public Response getSensitivityTestingList(MicrobioParamDto microbioParamDto) throws ApplicationException;

	public Response getSensitivityTestingListCount(MicrobioParamDto microbioParamDto) throws ApplicationException;

	public Response getSensitivityTestingDetails(MicrobioParamDto microbioParamDto) throws ApplicationException;

	public Response saveSensitivityTestingObservation(SensitivityTestResultMasterDto listSensitivityTestResultMasterDto)
			throws ApplicationException;
	
	public Response getSensitivityDetails(Integer labSampleDtlsId, Integer orgId, Integer orgUnitId) throws ApplicationException;
	
	public Response updateLabSampleDetailsMaster(SensitivityTestResultMasterDto sensitivityTestResultMasterDto) throws ApplicationException;
	
	public Response sensitivityTestingListListSearch(SearchDto dto) throws ApplicationException;

	public Response sensitivityTestingListListSearchCount(SearchDto dto)throws ApplicationException;

	public Response updateAmendLogs(SensitivityTestResultMasterDto listSensitivityTestResultMasterDto)throws ApplicationException;

	public Response saveCultureTest(TMicroIncubationMasterDto tMicroIncubationMasterDto,
			List<SensitivityTestResultDetailsMasterDto> sensitivityTestResultDetailsMasterDto)throws ApplicationException;

	public Response removeSensitivityTestResultSubDetails(Integer sesitivityResultSubDetailsId, Integer orgId,
			Integer orgUnitId);

	public Response removeSensitivityTestResultDetails(Integer sesitivityResultDetailsId, Integer sensitivityResultId,
			Integer orgId, Integer orgUnitId);
	
}
