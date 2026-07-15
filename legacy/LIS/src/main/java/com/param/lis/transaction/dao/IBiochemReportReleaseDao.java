package com.param.lis.transaction.dao;

import java.io.ByteArrayOutputStream;
import java.util.List;

import com.param.entity.lis.transaction.LabResultMaster;
import com.param.global.dto.OrderMasterDto;
import com.param.lis.global.common.Response;
import com.param.lis.global.common.SearchDto;
import com.param.lis.transaction.dto.BioChemParamDto;
import com.param.lis.transaction.dto.LabResultMasterDto;
import com.param.lis.transaction.dto.SearchCommonDto;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IBiochemReportReleaseDao extends IGenericDao<LabResultMaster, Integer>{

	public Response getReportReleaseWorkList(BioChemParamDto bioChemParamDto) throws ApplicationException;

	public Response getReportReleaseWorkListCount(BioChemParamDto bioChemParamDto) throws ApplicationException;

	public Response getReportReleaseWorkListDetails(BioChemParamDto bioChemParamDto) throws ApplicationException;

	public Response saveReportReleaseWorkList(LabResultMasterDto labResultMasterDto) throws ApplicationException;
	
	public Response releaseNormanReports(LabResultMasterDto labResultMasterDto) throws ApplicationException;
	
	public Response searchPatient(SearchCommonDto searchCommonDto) throws ApplicationException;

    public Response getFilteredReportReleaseWorkList(SearchDto searchDto) throws ApplicationException;

	public Response printCoverSheetReports(Integer labResultId) throws ApplicationException;

	public Response ConsolidatedReleaseWorkList(SearchDto searchDto) throws ApplicationException;

	public Response consolidatedSerologyReleaseSearch(SearchDto searchDto)throws ApplicationException;

	public Response ConsolidatedReleaseWorkTypeSearchList(SearchDto searchDto)throws ApplicationException;

	public Response consolidatedSerologyReleaseTypeSearch(SearchDto searchDto)throws ApplicationException;

	public Response getFilteredReportReleaseTypeSearchWorkList(SearchDto searchDto)throws ApplicationException;

	public Response getDoctorIdByLabSampleDetailsId(Integer labSampleDtlsId)throws ApplicationException;

	public Response getOrderIdByOrderNumber(List<String> orderNos, Integer unitId)throws ApplicationException;

	public Response getCompanyIdByOrderId(Integer orderId)throws ApplicationException;

	public Response getLabSampleDetailsIdByOrderId(SearchDto searchDto2)throws ApplicationException;

	public Response updateQAPathInLabsampleDetails(ByteArrayOutputStream bos, String docPath, Integer labSampleDtlsId, String labAPIUrl)throws ApplicationException;

	public Response checkOutStandingAmount(Integer encounterId)throws ApplicationException;

	public Response getPayee(OrderMasterDto orderDto)throws ApplicationException;

	public Response getCheckIsParameterCritical(List<Integer> labSampleDetlsIds)throws ApplicationException;

	public Response getLabExclusion(Integer labSampleDtlsId, Integer unitId, Integer orgId)throws ApplicationException;

	public Response getbucketTypeForUpload(Integer orgId, Integer unitId)throws ApplicationException;

	public Response getAwsCreds() throws ApplicationException;
}
