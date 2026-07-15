package com.param.global.unit.service;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.servlet.Registration;

import org.json.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.internal.StringMap;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.unit.dao.IReportDetailsDao;
import com.param.global.unit.dto.ReportDetailsDto;

import in.param.exception.ApplicationException;
import javassist.expr.NewArray;
import scala.collection.parallel.ParIterableLike.Foreach;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({"rawtypes"})
public class ReportDetailsServiceImpl implements IReportDetailsService, ICommonConstants{
	final static Logger logger = Logger.getLogger(ReportDetailsServiceImpl.class);

	@Autowired
	IReportDetailsDao iReportDetailsDao;
	@Autowired
	Gson gson;
	@Override
	public Response getReportDetailsList(ReportDetailsDto reportDetailsDto) throws ApplicationException {
		try {
			
			return iReportDetailsDao.getReportDetailsList(reportDetailsDto);
	
	} catch (Exception e) {
	logger.error("getReportDetailsList failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}
	@Override
	public Response getReportDetailsByReportId(ReportDetailsDto reportDetailsDto) throws ApplicationException {
		try {
			
			return iReportDetailsDao.getReportDetailsByReportId(reportDetailsDto);
	
	} catch (Exception e) {
	logger.error("getReportDetailsByReportId failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}
	@SuppressWarnings("unchecked")
	@Override
	public Response getReportGroupHeaderMaster(ReportDetailsDto reportDetailsDto) throws ApplicationException {
		try {
				
					Response res1=iReportDetailsDao.getReportGroupHeaderMaster(reportDetailsDto);
					Response res=iReportDetailsDao.getMappedMISReportIdsForUser(reportDetailsDto);
					List<ReportDetailsDto> listAll=new ArrayList<>();
					List<ReportDetailsDto> listMain= res1.getListObject();
					for (ReportDetailsDto dtoMain : listMain) {
						if(res.getListObject()!=null) {
							List<ReportDetailsDto> list1= res.getListObject();
							for(ReportDetailsDto dto1:list1) {
								ReportDetailsDto[] fooFromJson = gson.fromJson(dto1.getMisReportIds(), ReportDetailsDto[].class);
								if(fooFromJson!=null) {
									for (ReportDetailsDto extractedData : fooFromJson) {
										if(dtoMain.getReportGroupHeaderId().equals(extractedData.getReportGroupHeaderId())) {
													ReportDetailsDto newDto=new ReportDetailsDto();
													newDto.setReportGroupHeaderId(extractedData.getReportGroupHeaderId());
													newDto.setReportGroupHeaderName(dtoMain.getReportGroupHeaderName());
													newDto.setOrganizationId(reportDetailsDto.getOrganizationId());
													newDto.setSelectedReprtId(extractedData.getSelectedReprtId());
													listAll.add(newDto);
										}
									}
								}
								
							}
						}
					}
					return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, listAll, null);
					
			} catch (Exception e) {
			logger.error("getReportGroupHeaderMaster failed", e);
			}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	
	private List List(String misReportIds) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Response getReportGroupHeaderForUserRoleManagement(ReportDetailsDto reportDetailsDto)
			throws ApplicationException {
		try {
			
			return iReportDetailsDao.getReportGroupHeaderForUserRoleManagement(reportDetailsDto);
	
	} catch (Exception e) {
	logger.error("getReportGroupHeaderForUserRoleManagement failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}
	@Override
	public Response getMISReportFormattesMasterforShowReports(ReportDetailsDto reportDetailsDto)
			throws ApplicationException {
		try {
			
			return iReportDetailsDao.getMISReportFormattesMasterforShowReports(reportDetailsDto);
	
			} catch (Exception e) {
			logger.error("getMISReportFormattesMasterforShowReports failed", e);
			}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	@Override
	public Response searchMISReport(ReportDetailsDto reportDetailsDto) throws ApplicationException {
		try {
			
			return iReportDetailsDao.searchMISReport(reportDetailsDto);
	
			} catch (Exception e) {
			logger.error("searchMISReport failed", e);
			}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}



}
