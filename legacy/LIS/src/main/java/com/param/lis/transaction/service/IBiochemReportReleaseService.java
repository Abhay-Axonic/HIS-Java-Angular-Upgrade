package com.param.lis.transaction.service;

import java.io.ByteArrayOutputStream;
import java.util.List;

import com.param.global.dto.OrderMasterDto;
import com.param.lis.global.common.Response;
import com.param.lis.global.common.SearchDto;
import com.param.lis.transaction.dto.BioChemParamDto;
import com.param.lis.transaction.dto.LabResultMasterDto;
import com.param.lis.transaction.dto.SearchCommonDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IBiochemReportReleaseService {

	public Response getReportReleaseWorkList(BioChemParamDto bioChemParamDto) throws ApplicationException;

	public Response getReportReleaseWorkListCount(BioChemParamDto bioChemParamDto) throws ApplicationException;

	public Response getReportReleaseWorkListDetails(List<BioChemParamDto> bioChemParamDtoList) throws ApplicationException;

	public Response saveReportReleaseWorkList(List<LabResultMasterDto> listLabResultMasterDto, String contextPath) throws ApplicationException, Exception;
	
	public Response releaseNormanReports(List<LabResultMasterDto> listLabResultMasterDto) throws ApplicationException;
	
	public Response searchPatient(SearchCommonDto searchCommonDto) throws ApplicationException;

    public Response getFilteredReportReleaseWorkList(SearchDto searchDto) throws ApplicationException;

	public Response ConsolidatedReleaseWorkList(SearchDto searchDto)throws ApplicationException;

	public Response ConsolidatedReleaseWorkTypeSearchList(SearchDto searchDto)throws ApplicationException;

	public Response getFilteredReportReleaseTypeSearchWorkList(SearchDto searchDto)throws ApplicationException;

	public Response getDoctorIdByLabSampleDetailsId(Integer labSampleDtlsId)throws ApplicationException;

	public Response getOrderIdByOrderNumber(List<String> orderNos, Integer integer)throws ApplicationException;

	public Response getCompanyIdByOrderId(Integer orderId)throws ApplicationException;

	public Response getLabSampleDetailsIdByOrderId(List<SearchDto> searchDto)throws ApplicationException;

	public ByteArrayOutputStream getReportByQRCode(Integer labDetailsId, int unitId, int orgId)throws ApplicationException, Exception;

	public Response updateQAPathInLabsampleDetails(ByteArrayOutputStream bos, String docPath, Integer labSampleDtlsId, String str)throws ApplicationException;

	public com.param.global.common.Response getOrganiazationFolderPath(Integer orgId)throws ApplicationException;

	public Response checkDueAmount(OrderMasterDto orderDto)throws ApplicationException;

	public Response getCheckIsParameterCritical(List<Integer> labSampleDetlsIds)throws ApplicationException;

	public Response getLabExclusion(Integer labSampleDtlsId, Integer unitId, Integer orgId)throws ApplicationException;

	public Response getbucketTypeForUpload(Integer orgId, Integer unitId)throws ApplicationException;

	public Integer getgetEmpId(Integer createdBy)throws ApplicationException;

	public Response getAwsCreds() throws ApplicationException;
}
