package com.param.lis.reports.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.entity.lis.transaction.LabSampleDetailsMaster;
import com.param.global.org.service.IGlobalUnitMasterService;
import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.ITransactionConstants;
import com.param.lis.global.common.Response;
import com.param.lis.reports.utility.MultiParamTestReportUtility;
import com.param.lis.reports.utility.SingleParamTestReportUtility;
import com.param.lis.transaction.dao.IBiochemReportReleaseDao;
import com.param.lis.transaction.dto.BioChemParamDto;
import com.param.lis.transaction.dto.LabResultMasterDto;
import com.param.lis.transaction.dto.LabSampleDetailsMasterDto;
import com.param.lis.transaction.dto.SingParamTestResDto;
import com.param.lis.transaction.service.IBioChemistryService;
import com.param.lis.transaction.service.IBiochemReportReleaseService;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class BiochemReportServiceImpl implements IBiochemReportService, ICommonConstants, ITransactionConstants, IError {

	final static Logger logger = Logger.getLogger(BiochemReportServiceImpl.class);

	@Autowired
	IBiochemReportReleaseService iBiochemReportReleaseService;
	
	@Autowired
    IBiochemReportReleaseDao iBiochemReportReleaseDao;
	
	@Autowired
	private IBioChemistryService iBioChemistryService;
	
	@Autowired
	IGlobalUnitMasterService iGlobalUnitMasterService;

	@Override
	@Transactional
	public Response printBiochemistryReports(List<LabResultMasterDto> listLabResultMasterDto,HttpServletRequest servletRequest, HttpServletResponse servletResponse, String contextPath)
			throws ApplicationException {
		try {

			
			   Date date = TimeZoneComponent.getDateByzone(listLabResultMasterDto!=null && listLabResultMasterDto.size()>0?listLabResultMasterDto.get(0).getOrgUnitId():null);
			   String unitLogo = iGlobalUnitMasterService.getUnitImage(listLabResultMasterDto.get(0).getOrgUnitId());
			if (!listLabResultMasterDto.isEmpty()) {
				
				
				for (Iterator iterator = listLabResultMasterDto.iterator(); iterator.hasNext();) 
				{
					
					LabResultMasterDto labResultMasterDto = (LabResultMasterDto) iterator.next();
					
					BioChemParamDto bioChemParamDto = new BioChemParamDto();
					bioChemParamDto.setLabResultId(labResultMasterDto.getLabTestResId());
					bioChemParamDto.setDeptId(labResultMasterDto.getSubDeptId());
					bioChemParamDto.setOrgId(labResultMasterDto.getOrgId());	
					bioChemParamDto.setOrgUnitId(labResultMasterDto.getOrgUnitId());
					bioChemParamDto.setTestId(labResultMasterDto.getTestId());
					bioChemParamDto.setTestType(labResultMasterDto.getTestType());
					bioChemParamDto.setAssingnedFrom(labResultMasterDto.getAssingnedFrom());
					
					LabSampleDetailsMasterDto labSampleDetailsMasterDto = new LabSampleDetailsMasterDto();
					labSampleDetailsMasterDto.setLabSampleDtlsId(labResultMasterDto.getLabSampleDtlsId());
					labSampleDetailsMasterDto.setUpdatedDate(date);
					labSampleDetailsMasterDto.setCreatedBy(labResultMasterDto.getCreatedBy());
					labSampleDetailsMasterDto.setUpdatedBy(labResultMasterDto.getUpdatedBy());
					labSampleDetailsMasterDto.setAssignUnit(labSampleDetailsMasterDto.getOrgUnitId());
					labSampleDetailsMasterDto.setPerformedUnit(labSampleDetailsMasterDto.getOrgUnitId());
					
					 List<BioChemParamDto> bioChemParamDtoList = new ArrayList<>();
                     bioChemParamDtoList.add(bioChemParamDto);
					
					if (labResultMasterDto.getTestType()==MULTY_PARAM_TEST) 
					{
					  
						Response<SingParamTestResDto, Integer> multiParamTestRes = iBiochemReportReleaseService.getReportReleaseWorkListDetails(bioChemParamDtoList);
						SingParamTestResDto singParamTestResDto = multiParamTestRes.getListObject().get(0);
						singParamTestResDto.setUnitLogo(unitLogo);
					    this.generateMultyParamReport(labResultMasterDto, singParamTestResDto,servletRequest,servletResponse);
						
					} else if (labResultMasterDto.getTestType()==SINGLE_PARAM_TEST) {
						
						
						Response<SingParamTestResDto, Integer> singleParamTestRes = iBiochemReportReleaseService
								.getReportReleaseWorkListDetails(bioChemParamDtoList);
						SingParamTestResDto singParamTestResDto = singleParamTestRes.getListObject().get(0);
						singParamTestResDto.setUnitLogo(unitLogo);
						 this.generateSingleParamReport(labResultMasterDto, singParamTestResDto,servletRequest,servletResponse);
					}
					Response reportRS = iBioChemistryService.biochemFinalReportRelease(labSampleDetailsMasterDto);
					
					if(reportRS.getCode() == SUCCESS_200){
						 iBioChemistryService.addReportHistroyMaster((LabSampleDetailsMaster)reportRS.getObject(), labResultMasterDto.getLabTestResId());
					}else{
						return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
					}
					
					return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, null);
				}
			} else {
				logger.info("Error In Printing Report");
				return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
			}
		  return null;
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response generateSingleParamReport(LabResultMasterDto labResultMasterDto,
			SingParamTestResDto singParamTestResDto,HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws ApplicationException {
		try {
			if (labResultMasterDto != null && singParamTestResDto != null) {
				SingleParamTestReportUtility.generateSingleParamReport(labResultMasterDto, singParamTestResDto,servletRequest,servletResponse);
				return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, null);
			}
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response generateMultyParamReport(LabResultMasterDto labResultMasterDto,
	    SingParamTestResDto singParamTestResDto,HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws ApplicationException {
		try {
			if (labResultMasterDto != null && singParamTestResDto != null) {
				MultiParamTestReportUtility.generateMultiParamReport(labResultMasterDto, singParamTestResDto,servletRequest,servletResponse);
				return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, null);
			}
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response printCoverSheetReports(Integer labResultId, HttpServletRequest servletRequest,
			HttpServletResponse servletResponse, String contextPath) throws ApplicationException {
		try {
			
			  
			   Response res=iBiochemReportReleaseDao.printCoverSheetReports(labResultId!=null?labResultId:0) ;			
			   List<LabResultMasterDto> list=res.getListObject();
			if (list.size()>0) {
				 Date date = TimeZoneComponent.getDateByzone(list.get(0).getOrgUnitId());
				for (Iterator iterator = list.iterator(); iterator.hasNext();) 
				{
					
					LabResultMasterDto labResultMasterDto = (LabResultMasterDto) iterator.next();
					
					BioChemParamDto bioChemParamDto = new BioChemParamDto();
					bioChemParamDto.setLabResultId(labResultMasterDto.getLabTestResId());
					bioChemParamDto.setDeptId(labResultMasterDto.getSubDeptId());
					bioChemParamDto.setOrgId(labResultMasterDto.getOrgId());	
					bioChemParamDto.setOrgUnitId(labResultMasterDto.getOrgUnitId());
					bioChemParamDto.setTestId(labResultMasterDto.getTestId());
					bioChemParamDto.setTestType(labResultMasterDto.getTestType());
					
					LabSampleDetailsMasterDto labSampleDetailsMasterDto = new LabSampleDetailsMasterDto();
					labSampleDetailsMasterDto.setLabSampleDtlsId(labResultMasterDto.getLabSampleDtlsId());
					labSampleDetailsMasterDto.setUpdatedDate(date);
					labSampleDetailsMasterDto.setCreatedBy(labResultMasterDto.getCreatedBy());
					labSampleDetailsMasterDto.setUpdatedBy(labResultMasterDto.getUpdatedBy());
					
					 List<BioChemParamDto> bioChemParamDtoList = new ArrayList<>();
                  bioChemParamDtoList.add(bioChemParamDto);
					
					if (labResultMasterDto.getTestType()==MULTY_PARAM_TEST) 
					{
					  
						Response<SingParamTestResDto, Integer> multiParamTestRes = iBiochemReportReleaseService.getReportReleaseWorkListDetails(bioChemParamDtoList);
						SingParamTestResDto singParamTestResDto = multiParamTestRes.getListObject().get(0);
					    this.generateMultyParamReport(labResultMasterDto, singParamTestResDto,servletRequest,servletResponse);
						
					} else if (labResultMasterDto.getTestType()==SINGLE_PARAM_TEST) {
						
						
						Response<SingParamTestResDto, Integer> singleParamTestRes = iBiochemReportReleaseService
								.getReportReleaseWorkListDetails(bioChemParamDtoList);
						SingParamTestResDto singParamTestResDto = singleParamTestRes.getListObject().get(0);
						 this.generateSingleParamReport(labResultMasterDto, singParamTestResDto,servletRequest,servletResponse);
					}
					Response reportRS = iBioChemistryService.biochemFinalReportRelease(labSampleDetailsMasterDto);
					
					if(reportRS.getCode() == SUCCESS_200){
						 iBioChemistryService.addReportHistroyMaster((LabSampleDetailsMaster)reportRS.getObject(), labResultMasterDto.getLabTestResId());
					}else{
						return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
					}
					
					return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, null);
				}
			} else {
				logger.info("Error In Printing Report");
				return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
			}
		  return null;
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

}
