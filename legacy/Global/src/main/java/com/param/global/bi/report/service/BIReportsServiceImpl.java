package com.param.global.bi.report.service;

import java.io.File;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;
import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.param.global.bi.report.dao.IBIReportsDao;
import com.param.global.bi.report.dto.BIBedDetailsReportDto;
import com.param.global.bi.report.dto.BIDateListDto;
import com.param.global.bi.report.dto.BIReportsAllCollectionDto;
import com.param.global.bi.report.dto.BIReportsBillPaymentsCsvDto;
import com.param.global.bi.report.dto.BIReportsBillPaymentsDto;
import com.param.global.bi.report.dto.BIReportsCommonDto;
import com.param.global.bi.report.dto.BIReportsDailyCollectionCsvDto;
import com.param.global.bi.report.dto.BIReportsDailyCollectionDto;
import com.param.global.bi.report.dto.BIReportsDailyPatientCountCsvDto;
import com.param.global.bi.report.dto.BIReportsDailyPatientCountDto;
import com.param.global.bi.report.dto.BIReportsDto;
import com.param.global.bi.report.dto.BIReportsForCsvDto;
import com.param.global.bi.report.dto.KeyValuePairDto;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.unit.dao.IUserRoleMapperDao;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class BIReportsServiceImpl implements IBIReportsService, ICommonConstants {
	final static Logger logger = Logger.getLogger(BIReportsServiceImpl.class);

	@Autowired
	IBIReportsDao iBIReportsDao;

	@Autowired
	IUserRoleMapperDao iUserRoleMapperDao;

	@Override
	public Response getBIReportsAll(BIReportsDto bIReportsDto) throws ApplicationException {
		try {
			// if(res.getListObject()==null && res.getListObject().size() < 0){
			//
			// return new Response<>(ERROR, ALREADY_EXIST_CODE, "Access Denied", null,
			// null);
			// }else{
			return iBIReportsDao.getBIReportsAll(bIReportsDto);

			// }

		} catch (Exception e) {
		logger.error("getBIReportsAll failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getBIReportsAllForCsv(BIReportsDto bIReportsDto) throws ApplicationException {
		try {
			Response res = iBIReportsDao.getBIReportsAllForCsv(bIReportsDto);
			List<BIReportsDto> dtoList = (List<BIReportsDto>) res.getListObject();
			JSONArray docs = new JSONArray();
			String csv = "";
			for (BIReportsDto dto : dtoList) {
				JSONObject outputNew = new JSONObject();
				outputNew.put("organizationId", dto.getOrganization_id());
				outputNew.put("unitId", dto.getUnitId());
				outputNew.put("trandate", dto.getDepositdate());
				outputNew.put("paymentModeId", dto.getPayment_mode_id());
				outputNew.put("specialityId", dto.getSpeciality_id());
				outputNew.put("paymentModeDesc", dto.getPayment_mode_desc());
				outputNew.put("amount", dto.getAmount());

				docs.put(outputNew);
				File file = new File("EmpDetails.csv");
				csv = CDL.toString(docs);
				FileUtils.writeStringToFile(file, csv);

			}
			JSONObject objNew = new JSONObject();
			objNew.put("BI_ReportList", docs);
			BIReportsForCsvDto dtoObj = new BIReportsForCsvDto();
			// dtoObj.setBiReportJson(objNew);
			dtoObj.setBiReportString(csv);

			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, dtoObj);

		} catch (Exception e) {
		logger.error("getBIReportsAllForCsv failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getPendingAndPartialBillPayments(BIReportsCommonDto bIReportsCommonDto)
			throws ApplicationException {
		try {
			return iBIReportsDao.getPendingAndPartialBillPayments(bIReportsCommonDto);

		} catch (Exception e) {
		logger.error("getPendingAndPartialBillPayments failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getUnitWiseDailyPatientCountWithGender(BIReportsCommonDto bIReportsCommonDto)
			throws ApplicationException {
		try {
			return iBIReportsDao.getUnitWiseDailyPatientCountWithGender(bIReportsCommonDto);

		} catch (Exception e) {
		logger.error("getUnitWiseDailyPatientCountWithGender failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getPendingAndPartialBillPaymentsForCsv(BIReportsCommonDto bIReportsCommonDto)
			throws ApplicationException {
		try {
			Response res = iBIReportsDao.getPendingAndPartialBillPayments(bIReportsCommonDto);
			List<BIReportsBillPaymentsDto> dtoList = (List<BIReportsBillPaymentsDto>) res.getListObject();
			JSONArray docs = new JSONArray();
			String csv = "";
			for (BIReportsBillPaymentsDto dto : dtoList) {
				JSONObject outputNew = new JSONObject();
				outputNew.put("outstanding", dto.getOutstanding());
				outputNew.put("billDateTime", dto.getBillDateTime());

				docs.put(outputNew);
				File file = new File("EmpDetails.csv");
				csv = CDL.toString(docs);
				FileUtils.writeStringToFile(file, csv);

			}
			JSONObject objNew = new JSONObject();
			objNew.put("BI_ReportList", docs);
			BIReportsBillPaymentsCsvDto dtoObj = new BIReportsBillPaymentsCsvDto();
			dtoObj.setBiBillPaymentString(csv);

			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, dtoObj);

		} catch (Exception e) {
		logger.error("getPendingAndPartialBillPaymentsForCsv failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getDailyPatientCountWithGenderForCsv(BIReportsCommonDto bIReportsCommonDto)
			throws ApplicationException {
		try {
			Response res = iBIReportsDao.getUnitWiseDailyPatientCountWithGender(bIReportsCommonDto);
			List<BIReportsDailyPatientCountDto> dtoList = (List<BIReportsDailyPatientCountDto>) res.getListObject();
			JSONArray docs = new JSONArray();
			String csv = "";

			for (BIReportsDailyPatientCountDto dto : dtoList) {
				JSONObject outputNew = new JSONObject();
				outputNew.put("patientCount", dto.getPtcount());
				outputNew.put("male", dto.getMale());
				outputNew.put("female", dto.getFemale());
				outputNew.put("createdDate", dto.getCreatedDate());

				docs.put(outputNew);
				File file = new File("EmpDetails.csv");
				csv = CDL.toString(docs);
				FileUtils.writeStringToFile(file, csv);

			}
			JSONObject objNew = new JSONObject();
			objNew.put("BI_ReportList", docs);
			BIReportsDailyPatientCountCsvDto dtoObj = new BIReportsDailyPatientCountCsvDto();
			dtoObj.setBiPatientCountString(csv);

			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, dtoObj);

		} catch (Exception e) {
		logger.error("getDailyPatientCountWithGenderForCsv failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getUnitWiseDailyCollection(BIReportsCommonDto bIReportsCommonDto) throws ApplicationException {
		try {
			return iBIReportsDao.getUnitWiseDailyCollection(bIReportsCommonDto);

		} catch (Exception e) {
		logger.error("getUnitWiseDailyCollection failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getUnitWiseDailyCollectionForCsvO(BIReportsCommonDto bIReportsCommonDto)
			throws ApplicationException {
		try {
			Response res = iBIReportsDao.getUnitWiseDailyCollection(bIReportsCommonDto);
			List<BIReportsDailyCollectionDto> dtoList = (List<BIReportsDailyCollectionDto>) res.getListObject();
			JSONArray docs = new JSONArray();
			String csv = "";
			for (BIReportsDailyCollectionDto dto : dtoList) {
				JSONObject outputNew = new JSONObject();
				outputNew.put("depositdate", dto.getDepositDate());
				outputNew.put("unitName", dto.getUnitName());
				outputNew.put("cash", dto.getCash());
				outputNew.put("cheque", dto.getCheque());
				outputNew.put("cardpay", dto.getCreditcard());
				outputNew.put("debitcard", dto.getDebitcard());
				outputNew.put("mpesa", dto.getMpesa());
				outputNew.put("loyaltycard", dto.getLoyaltycard());
				outputNew.put("eTransfer", dto.geteTransfer());

				docs.put(outputNew);
				File file = new File("EmpDetails.csv");
				csv = CDL.toString(docs);
				FileUtils.writeStringToFile(file, csv);

			}
			JSONObject objNew = new JSONObject();
			objNew.put("BI_ReportList", docs);
			BIReportsDailyCollectionCsvDto dtoObj = new BIReportsDailyCollectionCsvDto();
			dtoObj.setBiDailyCollectionString(csv);

			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, dtoObj);

		} catch (Exception e) {
		logger.error("getUnitWiseDailyCollectionForCsvO failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getBIReportsAllCollectionList(BIReportsCommonDto bIReportsCommonDto) throws ApplicationException {
		try {
			return iBIReportsDao.getBIReportsAllCollectionList(bIReportsCommonDto);

		} catch (Exception e) {
		logger.error("getBIReportsAllCollectionList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getBIReportsAllCollectionNew(BIReportsCommonDto bIReportsCommonDto) throws ApplicationException {
		try {
			Response res = iBIReportsDao.getBIReportsAllCollectionNew(bIReportsCommonDto);
			
			List<BIDateListDto> dtoList = (List<BIDateListDto>) res.getListObject();
			JSONObject object = new JSONObject();
			JSONObject objectPayment = new JSONObject();
			
			for (BIDateListDto dto : dtoList) {
				
				object.put("trandate", dto.getTrandate());
				JSONArray array2 = new JSONArray();
				for (KeyValuePairDto object2 : dto.getParameterList()) {
					
					objectPayment.put(object2.getKey(), object2.getValue());
					
				}
				array2.put(objectPayment);
				object.put("payment", array2);
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, object);
			
		} catch (Exception e) {
		logger.error("getBIReportsAllCollectionNew failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getAllCollectionList(BIReportsCommonDto bIReportsCommonDto) throws ApplicationException {
		try {
			String inpuDateFormat = "yyyy-MM-dd";
	       
			if(bIReportsCommonDto.getPeriod().equals("daily"))
	        	inpuDateFormat = "yyyy-MM-dd";
	        else
	        	inpuDateFormat = "yyyy-MM";
	        	
	        Response res = iBIReportsDao.getAllCollectionListDateWise(bIReportsCommonDto,inpuDateFormat);
	      
	        Response patientSourceRes = null;
	        if(bIReportsCommonDto.isSource())
	        	patientSourceRes = iBIReportsDao.getPatientSourceWiseData(bIReportsCommonDto, inpuDateFormat);
	        
	        Response expenseRes = null;
	        List<BIReportsAllCollectionDto> expenseDataSetList = null;
	        List<BIReportsAllCollectionDto> expenseCountList = null;
	        if(bIReportsCommonDto.isExpense())
	        {
	        	expenseRes = iBIReportsDao.getExpenseData(bIReportsCommonDto, inpuDateFormat);
	        	
	        	Stream<BIReportsAllCollectionDto> expenseDataSet = expenseRes.getListObject().stream();
	        	expenseDataSetList = expenseDataSet
						.filter(str -> str.getType().equals(1))
						.collect(Collectors.toList());
	        	
	        	Stream<BIReportsAllCollectionDto> expenseDataSetCount = expenseRes.getListObject().stream();
				expenseCountList = expenseDataSetCount
						.filter(str -> str.getType().equals(2))
						.collect(Collectors.toList());
	        }
	        Response statusWiseTestCountRes = null;
	        if(bIReportsCommonDto.isStatusWiseTestCount()==true)
	        {
	        	statusWiseTestCountRes = iBIReportsDao.getStatusWiseTestCount(bIReportsCommonDto,inpuDateFormat);
	        }
	        
	        Response entitlementRes = null;
	        if(bIReportsCommonDto.isEntitlement())
	        	entitlementRes = iBIReportsDao.getEntitlementWiseData(bIReportsCommonDto, inpuDateFormat);
	        
				List<Date> dates = new ArrayList<Date>();
				List<String> datesArray = new ArrayList<String>();
				Date startDate;
				Date endDate;
				if(bIReportsCommonDto.getPeriod().equals("daily"))
				{
					startDate = GlobalCommonDateUtils.getDate(bIReportsCommonDto.getFromDate().substring(0,10), inpuDateFormat);
					endDate = GlobalCommonDateUtils.getDate(bIReportsCommonDto.getToDate().substring(0,10), inpuDateFormat);
				}else{
					startDate = GlobalCommonDateUtils.getDate(bIReportsCommonDto.getFromDate().substring(0,7), inpuDateFormat);
					endDate = GlobalCommonDateUtils.getDate(bIReportsCommonDto.getToDate().substring(0,7), inpuDateFormat);
				}
				
				long interval = 24 * 1000 * 60 * 60; // 1 hour in millis
				long endTime = endDate.getTime(); // create your endtime here, possibly using Calendar or Date
				long curTime = startDate.getTime();
				
				while (curTime <= endTime) {
					dates.add(new Date(curTime));
					curTime += interval;
				}
				if(bIReportsCommonDto.getPeriod().equals("daily")){
					for (int i = 0; i < dates.size(); i++) 
					{
						Date lDate = (Date) dates.get(i);
						String dateStr = GlobalCommonDateUtils.getStringDate(lDate, inpuDateFormat);
						datesArray.add(dateStr);
					}
				}else{
					String lastDate = "";
					for (int i = 0; i < dates.size(); i++) 
					{
						Date lDate = (Date) dates.get(i);
		    			String dateStr = GlobalCommonDateUtils.getStringDate(lDate, "yyyy-MM");
		    			if(lastDate=="" || !(lastDate.equals(dateStr)))
		    				datesArray.add(dateStr);
		    			lastDate = dateStr;
					}
				}
				

				JSONArray finalArray = new JSONArray();
				List<BIReportsAllCollectionDto> dataSet = res.getListObject();

				for (String date : datesArray) 
				{
					JSONObject dateObject = new JSONObject();
					Field changeMap = dateObject.getClass().getDeclaredField("map");
					changeMap.setAccessible(true);
					changeMap.set(dateObject, new LinkedHashMap<>());
					changeMap.setAccessible(false);

					Stream<BIReportsAllCollectionDto> dataSetStream = dataSet.stream();
					// JSONObject dateObject = new JSONObject();
					final String finalDate = date;
					Date dateFormat = GlobalCommonDateUtils.getDate(date, inpuDateFormat);
					
					dateObject.put("trandate", GlobalCommonDateUtils.getStringDate(dateFormat, inpuDateFormat));
					dateObject.put("organizationId", bIReportsCommonDto.getOrganizationId());
					
					if(bIReportsCommonDto.getUnitId()!=null)
						dateObject.put("unitId", bIReportsCommonDto.getUnitId());
					
					//<-----payments
					if(bIReportsCommonDto.isPayments())
					{
						List<BIReportsAllCollectionDto> paymentModes = dataSetStream
								.filter(str -> str.getPaymentModeId() > 0 && str.getCreatedDate().equals(finalDate))
								.collect(Collectors.toList());
						JSONArray paymentModeArray = new JSONArray();
						JSONObject paymentModeObj = null;

						for (BIReportsAllCollectionDto kd : paymentModes) 
						{
							paymentModeObj = new JSONObject();
							paymentModeObj.put(kd.getPaymentMode(), kd.getPaymentModeNet());
							paymentModeArray.put(paymentModeObj);
						}
						dateObject.put("payments", paymentModeArray);
					}
					//payments----->
					
					//<-----Count
					if(bIReportsCommonDto.isCount())
					{
						Stream<BIReportsAllCollectionDto> datasetStreamCount = dataSet.stream();
						List<BIReportsAllCollectionDto> subDeptsCount = datasetStreamCount
								.filter(str -> str.getSubSpecialityId() > 0 && str.getCreatedDate().equals(finalDate) && str.getType().equals(7))
								.collect(Collectors.toList());
						dateObject.put("count", getCount(subDeptsCount,finalDate));
					}
					//Count----->
					
					//<-----Revenue
					if(bIReportsCommonDto.isRevenue())
					{
						Stream<BIReportsAllCollectionDto> dataSetStream2 = dataSet.stream();
						List<BIReportsAllCollectionDto> subDepts = dataSetStream2
								.filter(str -> str.getSubSpecialityId() > 0 && str.getCreatedDate().equals(finalDate) && str.getType().equals(2))
								.collect(Collectors.toList());
						dateObject.put("revenues", getRevenue(subDepts,finalDate));
					}
					//Revenue----->
					
					
					if(bIReportsCommonDto.isPatientCount())
					{
						Stream<BIReportsAllCollectionDto> dataSetStream3 = dataSet.stream();
						List<BIReportsAllCollectionDto> patCount = dataSetStream3
								.filter(str -> str.getType().equals(5) && str.getCreatedDate().equals(finalDate))
								.collect(Collectors.toList());
						JSONObject patCountObj = new JSONObject();
						for (BIReportsAllCollectionDto patList : patCount) 
						{
							patCountObj.put("OP", patList.getOP());
							patCountObj.put("IP", patList.getIP());
							patCountObj.put("Male", patList.getMale());
							patCountObj.put("Female", patList.getFemale());
						}
						dateObject.put("patientCount", patCountObj);
					}
					
					if(bIReportsCommonDto.isOutstandings())
					{
						Stream<BIReportsAllCollectionDto> dataSetStream4 = dataSet.stream();
						List<BIReportsAllCollectionDto> outstandingList = dataSetStream4
								.filter(str -> str.getType().equals(4) && str.getCreatedDate().equals(finalDate))
								.collect(Collectors.toList());
						
						JSONObject outstandingObj = null;
						JSONArray outstandingArray = new JSONArray();
						for (BIReportsAllCollectionDto outstandings : outstandingList) 
						{
							outstandingObj = new JSONObject();
							outstandingObj.put(outstandings.getPayeeDesc(),outstandings.getPayeeDue());
							outstandingArray.put(outstandingObj);
						}
						dateObject.put("outstandings", outstandingArray);
					}
					
					if(bIReportsCommonDto.isDiscount())
					{
						Stream<BIReportsAllCollectionDto> discountStream = dataSet.stream();
						List<BIReportsAllCollectionDto> discountList = discountStream
								.filter(str -> str.getType().equals(6) && str.getCreatedDate().equals(finalDate))
								.collect(Collectors.toList());
						for(BIReportsAllCollectionDto dto : discountList)
						{
							dateObject.put("discount", dto.getDiscount());
							dateObject.put("concession", dto.getConcession());
						}
					}
					
					
					if(bIReportsCommonDto.isSource())
					{
						dateObject.put("sourceWise", getSourcewiseData(patientSourceRes,finalDate));
					}
					
					if(bIReportsCommonDto.isEntitlement())
					{
						dateObject.put("entitlementWise", getEntitlementWiseData(entitlementRes, finalDate));
					}
					
					if(bIReportsCommonDto.isStatusWiseTestCount()==true)
			        {
						
						JSONArray deptArray = new JSONArray();
						//<-----Lab
						Stream<BIReportsAllCollectionDto> statusWiseTestMainListStreamForLab = statusWiseTestCountRes.getListObject().stream();
						
						List<BIReportsAllCollectionDto> statusWiseTestCountDataSetLab = statusWiseTestMainListStreamForLab
								.filter(str -> str.getType().equals(1) && str.getCreatedDate().equals(finalDate))
								.collect(Collectors.toList());
						
						JSONObject labObj = new JSONObject();
						int subDeptId = 0;
						JSONArray arraySubDep = new JSONArray();
						JSONObject subDeptObj =null;
						for(BIReportsAllCollectionDto allCollectionDto : statusWiseTestCountDataSetLab)
						{
							if(subDeptId==0 || subDeptId!=allCollectionDto.getSubSpecialityId())
							{
								subDeptId = allCollectionDto.getSubSpecialityId();
								final int subDeptIdFinal = allCollectionDto.getSubSpecialityId();
								
								subDeptObj = new JSONObject();
								Field subDeptObjMap = subDeptObj.getClass().getDeclaredField("map");
								changeMap.setAccessible(true);
								changeMap.set(subDeptObj, new LinkedHashMap<>());
								changeMap.setAccessible(false);
								
								Stream<BIReportsAllCollectionDto> statusListSubDeptWiseStream = statusWiseTestCountDataSetLab.stream();
								List<BIReportsAllCollectionDto> statusListSubDeptWiseList = statusListSubDeptWiseStream
										.filter(str -> str.getSubSpecialityId().equals(subDeptIdFinal) && str.getCreatedDate().equals(finalDate))
										.collect(Collectors.toList());
								
								int statusId = 0;
								JSONObject statusObj = null;
								JSONArray statusArray = new JSONArray();
								for(BIReportsAllCollectionDto statusWise : statusListSubDeptWiseList)
								{
									if(statusId==0 || statusId!=statusWise.getStatusId())
									{
										statusId = statusWise.getStatusId();
										int finalStatusId = statusWise.getStatusId();
										statusObj = new JSONObject();
										Field statusObjMap = statusObj.getClass().getDeclaredField("map");
										changeMap.setAccessible(true);
										changeMap.set(statusObj, new LinkedHashMap<>());
										changeMap.setAccessible(false);
										
										
										BigInteger subDeptCount = BigInteger.ZERO;
										
										Stream<BIReportsAllCollectionDto> serviceListStatusWiseStream = statusListSubDeptWiseList.stream();
										List<BIReportsAllCollectionDto> serviceListStatusWiseList = serviceListStatusWiseStream
												.filter(str -> str.getStatusId().equals(finalStatusId) && str.getCreatedDate().equals(finalDate))
												.collect(Collectors.toList());
										
										int testId = 0;
										JSONObject serviceObj = null;
										JSONArray serviceArray = new JSONArray();
										BigInteger statusWiseCount = BigInteger.ZERO;
										for(BIReportsAllCollectionDto serviceWise : serviceListStatusWiseList)
										{
											if(testId==0 || testId!= serviceWise.getServiceId())
											{
												serviceObj = new JSONObject();
												Field serviceObjMap = serviceObj.getClass().getDeclaredField("map");
												changeMap.setAccessible(true);
												changeMap.set(serviceObj, new LinkedHashMap<>());
												changeMap.setAccessible(false);
												
												serviceObj.put(serviceWise.getServiceStandardName(), serviceWise.getServiceWiseCount());
												statusWiseCount = statusWiseCount.add(serviceWise.getServiceWiseCount());
												serviceArray.put(serviceObj);
											}
										}
										statusObj.put(statusWise.getStatus(), statusWiseCount);
										statusObj.put("services", serviceArray);
										statusArray.put(statusObj);
										subDeptCount = subDeptCount.add(statusWiseCount);
									}
								}
								subDeptObj.put(allCollectionDto.getSubSpecialityName(), statusArray);
								arraySubDep.put(subDeptObj);
							}
							
						}
						labObj.put("lab", arraySubDep);
						deptArray.put(labObj);
						//Lab----->
						
						
						//<-----Radiology
						Stream<BIReportsAllCollectionDto> statusWiseTestMainListStreamForRadio = statusWiseTestCountRes.getListObject().stream();
						
						List<BIReportsAllCollectionDto> statusWiseTestCountDataSetLabForRadio = statusWiseTestMainListStreamForRadio
								.filter(str -> str.getType().equals(2) && str.getCreatedDate().equals(finalDate))
								.collect(Collectors.toList());
						
						JSONObject radioObj = new JSONObject();
						int subDeptIdForRadio = 0;
						JSONArray arraySubDepForRadio = new JSONArray();
						JSONObject subDeptObjForRadio =null;
						for(BIReportsAllCollectionDto allCollectionDto : statusWiseTestCountDataSetLabForRadio)
						{
							if(subDeptIdForRadio==0 || subDeptIdForRadio!=allCollectionDto.getSubSpecialityId())
							{
								subDeptIdForRadio = allCollectionDto.getSubSpecialityId();
								final int subDeptIdFinal = allCollectionDto.getSubSpecialityId();
								
								subDeptObjForRadio = new JSONObject();
								Field subDeptObjForRadioMap = subDeptObjForRadio.getClass().getDeclaredField("map");
								changeMap.setAccessible(true);
								changeMap.set(subDeptObjForRadio, new LinkedHashMap<>());
								changeMap.setAccessible(false);
								
								Stream<BIReportsAllCollectionDto> statusListSubDeptWiseStream = statusWiseTestCountDataSetLabForRadio.stream();
								List<BIReportsAllCollectionDto> statusListSubDeptWiseList = statusListSubDeptWiseStream
										.filter(str -> str.getSubSpecialityId().equals(subDeptIdFinal) && str.getCreatedDate().equals(finalDate))
										.collect(Collectors.toList());
								
								int statusId = 0;
								JSONObject statusObj = null;
								JSONArray statusArray = new JSONArray();
								for(BIReportsAllCollectionDto statusWise : statusListSubDeptWiseList)
								{
									if(statusId==0 || statusId!=statusWise.getStatusId())
									{
										statusId = statusWise.getStatusId();
										int finalStatusId = statusWise.getStatusId();
										statusObj = new JSONObject();
										Field statusObjMap = statusObj.getClass().getDeclaredField("map");
										changeMap.setAccessible(true);
										changeMap.set(statusObj, new LinkedHashMap<>());
										changeMap.setAccessible(false);
										
										BigInteger subDeptCount = BigInteger.ZERO;
										
										Stream<BIReportsAllCollectionDto> serviceListStatusWiseStream = statusListSubDeptWiseList.stream();
										List<BIReportsAllCollectionDto> serviceListStatusWiseList = serviceListStatusWiseStream
												.filter(str -> str.getStatusId().equals(finalStatusId) && str.getCreatedDate().equals(finalDate))
												.collect(Collectors.toList());
										
										int testId = 0;
										JSONObject serviceObj = null;
										JSONArray serviceArray = new JSONArray();
										BigInteger statusWiseCount = BigInteger.ZERO;
										for(BIReportsAllCollectionDto serviceWise : serviceListStatusWiseList)
										{
											if(testId==0 || testId!= serviceWise.getServiceId())
											{
												serviceObj = new JSONObject();
												Field serviceObjMap = serviceObj.getClass().getDeclaredField("map");
												changeMap.setAccessible(true);
												changeMap.set(serviceObj, new LinkedHashMap<>());
												changeMap.setAccessible(false);
												
												serviceObj.put(serviceWise.getServiceStandardName(), serviceWise.getServiceWiseCount());
												statusWiseCount = statusWiseCount.add(serviceWise.getServiceWiseCount());
												serviceArray.put(serviceObj);
											}
										}
										statusObj.put(statusWise.getStatus(), statusWiseCount);
										statusObj.put("services", serviceArray);
										statusArray.put(statusObj);
										subDeptCount = subDeptCount.add(statusWiseCount);
									}
								}
								subDeptObjForRadio.put(allCollectionDto.getSubSpecialityName(), statusArray);
								arraySubDepForRadio.put(subDeptObjForRadio);
							}
							
						}
						radioObj.put("radiology", arraySubDepForRadio);
						deptArray.put(radioObj);
						//Radiology----->
						
						dateObject.put("statusWiseTestCount",deptArray);
						
			        }
					
					if(bIReportsCommonDto.isExpense())
					{
						Stream<BIReportsAllCollectionDto> expenseDataSetIncome = expenseDataSetList.stream();
						List<BIReportsAllCollectionDto> expenseDataSetListForExpenses = expenseDataSetIncome
								.filter(str -> str.getBillType().equals(2)  && str.getCreatedDate().equals(finalDate))
								.collect(Collectors.toList());//expense
						
						/*Stream<BIReportsAllCollectionDto> expenseDataSetExpense = expenseDataSetList.stream();
						List<BIReportsAllCollectionDto> expenseDataSetListForIncome = expenseDataSetExpense
								.filter(str -> str.getBillType().equals(1)  && str.getCreatedDate().equals(finalDate) )
								.collect(Collectors.toList()); //income*/						
						
						Stream<BIReportsAllCollectionDto> expenseDataSetExpenseCount = expenseCountList.stream();
						List<BIReportsAllCollectionDto> expenseDataSetListForExpensesCount = expenseDataSetExpenseCount
								.filter(str -> str.getBillType().equals(2)  && str.getCreatedDate().equals(finalDate))
								.collect(Collectors.toList());//expense
						
						/*Stream<BIReportsAllCollectionDto> expenseDataSetIncomeCount = expenseCountList.stream();
						List<BIReportsAllCollectionDto> expenseDataSetListForIncomeCount = expenseDataSetIncomeCount
								.filter(str -> str.getBillType().equals(1)  && str.getCreatedDate().equals(finalDate) )
								.collect(Collectors.toList()); //income*/
						
						JSONArray expenseArrayDateWise = new JSONArray();
						JSONObject expenseObj = new JSONObject();
						JSONObject expenseArray = new JSONObject();
						JSONObject expenseLedger = null;
						JSONArray expenseLedgerArray = new JSONArray();
						JSONObject expenseLedgerCount = null;
						JSONArray expenseLedgerArrayCount = new JSONArray();
						for(BIReportsAllCollectionDto expense : expenseDataSetListForExpenses)
						{
							int legderId = 0;
							if(legderId==0 || expense.getGeneralLedgerId()!=legderId)
							{
								legderId = expense.getGeneralLedgerId();
								expenseLedger = new JSONObject();
								Field expenseLedgerMap = expenseLedger.getClass().getDeclaredField("map");
								changeMap.setAccessible(true);
								changeMap.set(expenseLedger, new LinkedHashMap<>());
								changeMap.setAccessible(false);
								
								expenseLedger.put(expense.getGeneralLedgerName(),expense.getExpenseNet());
								expenseLedgerArray.put(expenseLedger);
							}
						}
						expenseArray.put("ledgers", expenseLedgerArray);
						
						for(BIReportsAllCollectionDto expense : expenseDataSetListForExpensesCount)
						{
							int legderId = 0;
							if(legderId==0 || expense.getGeneralLedgerId()!=legderId)
							{
								legderId = expense.getGeneralLedgerId();
								expenseLedgerCount = new JSONObject();
								Field expenseLedgerCountMap = expenseLedgerCount.getClass().getDeclaredField("map");
								changeMap.setAccessible(true);
								changeMap.set(expenseLedgerCount, new LinkedHashMap<>());
								changeMap.setAccessible(false);
								
								expenseLedgerCount.put(expense.getGeneralLedgerName(),expense.getExpenseCount());
								expenseLedgerArrayCount.put(expenseLedgerCount);
							}
						}
						expenseArray.put("count", expenseLedgerArrayCount);
						
						expenseArrayDateWise.put(expenseArray);
						
						dateObject.put("expenses",expenseArrayDateWise);
						
						
						
						/*JSONObject incomeObjArray = new JSONObject();
						JSONObject incomeLedger = null;
						JSONArray incomeLedgerArray = new JSONArray();
						JSONObject incomeObj = new JSONObject();
						JSONObject incomeLedgerCount = null;
						JSONArray incomeLedgerArrayCount = new JSONArray();
						
						
						for(BIReportsAllCollectionDto expense : expenseDataSetListForIncome)
						{
							
							int legderId = 0;
							if(legderId==0 || expense.getGeneralLedgerId()!=legderId)
							{
								legderId = expense.getGeneralLedgerId();
								incomeLedger = new JSONObject();
								incomeLedger.put(expense.getGeneralLedgerName(),expense.getExpenseNet());
								incomeLedgerArray.put(incomeLedger);
							}
						}
						incomeObjArray.put("ledgers", incomeLedgerArray);
						
						for(BIReportsAllCollectionDto expense : expenseDataSetListForIncomeCount)
						{
							
							int legderId = 0;
							if(legderId==0 || expense.getGeneralLedgerId()!=legderId)
							{
								legderId = expense.getGeneralLedgerId();
								incomeLedgerCount = new JSONObject();
								incomeLedgerCount.put(expense.getGeneralLedgerName(),expense.getExpenseCount());
								incomeLedgerArrayCount.put(incomeLedgerCount);
							}
						}
						incomeObjArray.put("count", incomeLedgerArrayCount);
						incomeObj.put("Income", incomeObjArray);
						expenseArrayDateWise.put(incomeObj);*/
						//dateObject.put("miscellaneous expenses", expenseArrayDateWise);
					}
					finalArray.put(dateObject);
				}
				ObjectMapper mapper = new ObjectMapper();
				JsonNode json = mapper.readTree(finalArray.toString());
				return new Response<>(SUCCESS, json);
			
		} catch (Exception e) {
		logger.error("getAllCollectionList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	private JSONArray getEntitlementWiseData(Response entitlementRes, final String finalDate) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, JSONException
	{
		List<BIReportsAllCollectionDto> entitlementWiseList = entitlementRes.getListObject();
		Stream<BIReportsAllCollectionDto> entitlementWiseStream = entitlementWiseList.stream();
		List<BIReportsAllCollectionDto> entitlementListAmountWise = entitlementWiseStream
					.filter(str -> str.getCreatedDate().equals(finalDate) && (str.getType().equals(1) || str.getType().equals(2) || str.getType().equals(3)))
					.collect(Collectors.toList());
		Stream<BIReportsAllCollectionDto> entitlementWiseForCountStream = entitlementWiseList.stream();
		List<BIReportsAllCollectionDto> entitlementWiseListForCount = entitlementWiseForCountStream
				.filter(str -> str.getCreatedDate().equals(finalDate) && (str.getType().equals(4) || str.getType().equals(5) || str.getType().equals(6)))
				.collect(Collectors.toList());
		
		 JSONObject entitlementObj = null;
		 JSONArray entitlementArray = new JSONArray();
		 List<Integer> entitlementListArray = new LinkedList<>(); 
		 entitlementListArray.add(1);
		 entitlementListArray.add(2);
		 entitlementListArray.add(3);
		 for(Integer entitlementId :  entitlementListArray)
		 {
		 	final int finalEntitlementId = entitlementId;
			final int finalEntitlementCountId = entitlementId.equals(1) ? 4 : entitlementId.equals(2) ? 5 : 6;
			entitlementObj = new JSONObject();
			Field entitlement = entitlementObj.getClass().getDeclaredField("map");
			 	entitlement.setAccessible(true);
				entitlement.set(entitlementObj, new LinkedHashMap<>());
				entitlement.setAccessible(false);
				
			entitlementObj.put("entitlement", entitlementId.equals(1) ? "Self" : entitlementId.equals(2) ? "Insurance" : "Corporate");
			Stream<BIReportsAllCollectionDto> typeWiseStream = entitlementListAmountWise.stream();
			List<BIReportsAllCollectionDto> entitlementList = typeWiseStream
					.filter(str -> str.getCreatedDate().equals(finalDate) && str.getType().equals(finalEntitlementId))
					.collect(Collectors.toList());
			
			Stream<BIReportsAllCollectionDto> countWiseStream = entitlementWiseListForCount.stream();
			List<BIReportsAllCollectionDto> entitlementListForCount = countWiseStream
					.filter(str -> str.getCreatedDate().equals(finalDate) && str.getType().equals(finalEntitlementCountId))
					.collect(Collectors.toList());
			
			int prevPayeeId = 0;
			JSONObject payeeObj = null;
			JSONArray payeeArray = new JSONArray();
			for(BIReportsAllCollectionDto payeeWise :entitlementList)
			{
				if(payeeWise.getType().equals(1) || payeeWise.getType().equals(3))
				{
					if(prevPayeeId==0 || prevPayeeId!=payeeWise.getCompanyId())
					{
						prevPayeeId = payeeWise.getCompanyId();
						final int finalPayeeId = payeeWise.getCompanyId();
						payeeObj = new JSONObject();
						
						Field payee = payeeObj.getClass().getDeclaredField("map");
						payee.setAccessible(true);
						payee.set(payeeObj, new LinkedHashMap<>());
						payee.setAccessible(false);
						payeeObj.put("payee", payeeWise.getCompanyDesc());
						
						
						Stream<BIReportsAllCollectionDto> datasetStream = entitlementList.stream();
						List<BIReportsAllCollectionDto> subDepts = datasetStream
								.filter(str -> str.getSubSpecialityId() > 0 && str.getCreatedDate().equals(finalDate) && str.getCompanyId().equals(finalPayeeId))
								.collect(Collectors.toList());	
							
						payeeObj.put("revenues", getRevenue(subDepts, finalDate));
						
						Stream<BIReportsAllCollectionDto> datasetStreamCount = entitlementListForCount.stream();
						List<BIReportsAllCollectionDto> subDeptsCount = datasetStreamCount
								.filter(str -> str.getSubSpecialityId() > 0 && str.getCreatedDate().equals(finalDate) && str.getCompanyId().equals(finalPayeeId))
								.collect(Collectors.toList());	
						payeeObj.put("count", getCount(subDeptsCount, finalDate));
						payeeArray.put(payeeObj);
					}
				}else{
					if(prevPayeeId==0 || prevPayeeId!=payeeWise.getInsuranceId())
					{
						prevPayeeId = payeeWise.getInsuranceId();
						final int finalPayeeId = payeeWise.getInsuranceId();
						payeeObj = new JSONObject();
						Field payee = payeeObj.getClass().getDeclaredField("map");
						payee.setAccessible(true);
						payee.set(payeeObj, new LinkedHashMap<>());
						payee.setAccessible(false);
						
						payeeObj.put("payee", payeeWise.getCompanyDesc());
						payeeObj.put("associate payee", payeeWise.getInsuranceDesc());
						
						Stream<BIReportsAllCollectionDto> datasetStream = entitlementList.stream();
						List<BIReportsAllCollectionDto> subDepts = datasetStream
								.filter(str -> str.getSubSpecialityId() > 0 && str.getCreatedDate().equals(finalDate) && str.getInsuranceId().equals(finalPayeeId))
								.collect(Collectors.toList());	
							
						payeeObj.put("revenues", getRevenue(subDepts, finalDate));
						
						Stream<BIReportsAllCollectionDto> datasetStreamCount = entitlementListForCount.stream();
						List<BIReportsAllCollectionDto> subDeptsCount = datasetStreamCount
								.filter(str -> str.getSubSpecialityId() > 0 && str.getCreatedDate().equals(finalDate) && str.getInsuranceId().equals(finalPayeeId))
								.collect(Collectors.toList());	
						payeeObj.put("count", getCount(subDeptsCount, finalDate));
						payeeArray.put(payeeObj);
					}
				}
			}
			if(entitlementList.size()>0)
			{
				entitlementObj.put("payeeDetails", payeeArray);
				entitlementArray.put(entitlementObj);
			}
		 }
		 return entitlementArray;
	}
	
	private JSONArray getSourcewiseData(Response patientSourceRes, final String finalDate) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, JSONException
	{
		 List<BIReportsAllCollectionDto> dataSetPatientSource = patientSourceRes.getListObject();
		 Stream<BIReportsAllCollectionDto> datasetStreampatientSource = dataSetPatientSource.stream();
		 List<BIReportsAllCollectionDto> patientSourceList = datasetStreampatientSource
					.filter(str -> str.getCreatedDate().equals(finalDate) && str.getType().equals(1))
					.collect(Collectors.toList());
		
		 String prevPatientSource = "";
		 JSONObject patientSourceObj = null;
		 JSONArray arrayPatSource = new JSONArray();
		 for (BIReportsAllCollectionDto patientSource : patientSourceList) 
		 {
			if (prevPatientSource == "" || !prevPatientSource.equals(patientSource.getPatientScource())) 
			{
				prevPatientSource = patientSource.getPatientScource();
				final int finalPatientSourceId = patientSource.getPatientScourceId();
				patientSourceObj = new JSONObject();
				Field patientSourceMap = patientSourceObj.getClass().getDeclaredField("map");
				patientSourceMap.setAccessible(true);
				patientSourceMap.set(patientSourceObj, new LinkedHashMap<>());
				patientSourceMap.setAccessible(false);
				
				patientSourceObj.put("source" , patientSource.getPatientScource()=="-" ? null : patientSource.getPatientScource());
				Stream<BIReportsAllCollectionDto> dataSetStreamPatSource = patientSourceList.stream();
				List<BIReportsAllCollectionDto> dataSetStreamPatSourceList = dataSetStreamPatSource
						.filter(str -> str.getPatientScourceId().equals(finalPatientSourceId) && str.getCreatedDate().equals(finalDate) && str.getType().equals(1))
						.collect(Collectors.toList());
				
				patientSourceObj.put("revenues", getRevenue(dataSetStreamPatSourceList, finalDate));
				
				Stream<BIReportsAllCollectionDto> datasetStreampatientSourceCount = dataSetPatientSource.stream();
				List<BIReportsAllCollectionDto> patientSourceCountList = datasetStreampatientSourceCount
					.filter(str -> str.getPatientScourceId().equals(finalPatientSourceId) && str.getCreatedDate().equals(finalDate) && str.getType().equals(2))
					.collect(Collectors.toList());
				patientSourceObj.put("count", getCount(patientSourceCountList,finalDate));
				
				arrayPatSource.put(patientSourceObj);
			}
			
		}
		 return arrayPatSource; 
	
	}
	
	
	private JSONArray getRevenue(List<BIReportsAllCollectionDto> subDepts, final String finalDate)throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, JSONException
	{
		JSONArray arrayDep = new JSONArray();
		int deptId = 0;
		for (BIReportsAllCollectionDto kd : subDepts) 
		{
			JSONObject deptObj = null;
			if (deptId == 0 || deptId != kd.getSpecialityId()) 
			{
				deptId = kd.getSpecialityId();
				
				deptObj = new JSONObject();
				Field changeMap2 = deptObj.getClass().getDeclaredField("map");
				changeMap2.setAccessible(true);
				changeMap2.set(deptObj, new LinkedHashMap<>());
				changeMap2.setAccessible(false);

				JSONArray subDeptObjArray = new JSONArray();
				JSONArray servicesObjArray = new JSONArray();
				JSONObject subDeptObj = null;
				JSONObject serviceListObj = null;
				final int finalDeptId = deptId;
				
				BigDecimal deptSum = BigDecimal.ZERO;
				Stream<BIReportsAllCollectionDto> dataSetStream3 = subDepts.stream();
				List<BIReportsAllCollectionDto> depts = dataSetStream3.filter(str -> str.getSpecialityId().equals(finalDeptId) && str.getCreatedDate().equals(finalDate))
						.collect(Collectors.toList());
				
				int subDeptId = 0;
				for (BIReportsAllCollectionDto dto2 : depts) 
				{
					
					if (subDeptId == 0 || subDeptId != dto2.getSubSpecialityId()) 
					{
						subDeptId = dto2.getSubSpecialityId();
						
						subDeptObj = new JSONObject();
						
						servicesObjArray=new JSONArray();
						
						BigDecimal subDeptSum = BigDecimal.ZERO;
						
						Field changeMap3 = subDeptObj.getClass().getDeclaredField("map");
						changeMap3.setAccessible(true);
						changeMap3.set(subDeptObj, new LinkedHashMap<>());
						changeMap3.setAccessible(false);
						
						// for services List
						final int subSplId = dto2.getSubSpecialityId();
						Stream<BIReportsAllCollectionDto> dataSetStream4 = depts.stream();
						List<BIReportsAllCollectionDto> sevicesList = dataSetStream4.filter(str -> str.getSubSpecialityId().equals(subSplId)
										&& str.getCreatedDate().equals(finalDate) && str.getServiceId()>0).collect(Collectors.toList());
						
						for (BIReportsAllCollectionDto dto3 : sevicesList) 
						{
							serviceListObj = new JSONObject();
							
							Field changeMap4 = serviceListObj.getClass().getDeclaredField("map");
							changeMap4.setAccessible(true);
							changeMap4.set(serviceListObj, new LinkedHashMap<>());
							changeMap4.setAccessible(false);
							
							
							serviceListObj.put(dto3.getServiceStandardName(),dto3.getServiceWiseNetAmount());
							servicesObjArray.put(serviceListObj);
							subDeptSum = subDeptSum.add(dto3.getServiceWiseNetAmount());
						}
						subDeptObj.put(dto2.getSubSpecialityName(), subDeptSum);
						subDeptObj.put("services", servicesObjArray);
						
						deptSum = deptSum.add(subDeptSum);
						subDeptObjArray.put(subDeptObj);
					}
				}
				deptObj.put(kd.getSpecialityName(), deptSum);
				deptObj.put("subDepts", subDeptObjArray);
				arrayDep.put(deptObj);
			}
		}
		return arrayDep;
	}
	
	
	private JSONArray getCount(List<BIReportsAllCollectionDto> subDeptsForCount, final String finalDate)throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, JSONException
	{
		JSONArray arrayDepCount = new JSONArray();
		int deptIdCount = 0;
		for (BIReportsAllCollectionDto kd : subDeptsForCount) 
		{
			JSONObject deptObj = null;
			if (deptIdCount == 0 || deptIdCount != kd.getSpecialityId()) 
			{
				deptIdCount = kd.getSpecialityId();
				
				deptObj = new JSONObject();
				Field changeMap2 = deptObj.getClass().getDeclaredField("map");
				changeMap2.setAccessible(true);
				changeMap2.set(deptObj, new LinkedHashMap<>());
				changeMap2.setAccessible(false);

				JSONArray subDeptObjArray = new JSONArray();
				JSONArray servicesObjArray = new JSONArray();
				JSONObject subDeptObj = null;
				JSONObject serviceListObj = null;
				final int finalDeptId = deptIdCount;
				
				BigInteger deptSum = BigInteger.ZERO;
				Stream<BIReportsAllCollectionDto> dataSetStream3 = subDeptsForCount.stream();
				List<BIReportsAllCollectionDto> depts = dataSetStream3.filter(str -> str.getSpecialityId().equals(finalDeptId) && str.getCreatedDate().equals(finalDate))
						.collect(Collectors.toList());
				
				int subDeptId = 0;
				for (BIReportsAllCollectionDto dto2 : depts) 
				{
					
					if (subDeptId == 0 || subDeptId != dto2.getSubSpecialityId()) 
					{
						subDeptId = dto2.getSubSpecialityId();
						
						subDeptObj = new JSONObject();
						
						servicesObjArray=new JSONArray();
						
						BigInteger subDeptSum = BigInteger.ZERO;
						
						Field changeMap3 = subDeptObj.getClass().getDeclaredField("map");
						changeMap3.setAccessible(true);
						changeMap3.set(subDeptObj, new LinkedHashMap<>());
						changeMap3.setAccessible(false);
						
						// for services List
						final int subSplId = dto2.getSubSpecialityId();
						Stream<BIReportsAllCollectionDto> dataSetStream4 = depts.stream();
						List<BIReportsAllCollectionDto> sevicesList = dataSetStream4.filter(str -> str.getSubSpecialityId().equals(subSplId)
										&& str.getCreatedDate().equals(finalDate) && str.getServiceId()>0).collect(Collectors.toList());
						
						for (BIReportsAllCollectionDto dto3 : sevicesList) 
						{
							serviceListObj = new JSONObject();
							
							Field changeMap4 = serviceListObj.getClass().getDeclaredField("map");
							changeMap4.setAccessible(true);
							changeMap4.set(serviceListObj, new LinkedHashMap<>());
							changeMap4.setAccessible(false);
							
							serviceListObj.put(dto3.getServiceStandardName(),dto3.getServiceWiseCount());
							servicesObjArray.put(serviceListObj);
							subDeptSum = subDeptSum.add(dto3.getServiceWiseCount());
						}
						subDeptObj.put(dto2.getSubSpecialityName(), subDeptSum);
						subDeptObj.put("services", servicesObjArray);
						
						deptSum = deptSum.add(subDeptSum);
						subDeptObjArray.put(subDeptObj);
					}
				}
				deptObj.put(kd.getSpecialityName(), deptSum);
				deptObj.put("subDepts", subDeptObjArray);
				arrayDepCount.put(deptObj);
			}
		}
		return arrayDepCount;
	}

	@Override
	public Response getBIReportsRoomOccupancy(BIReportsCommonDto bIReportsCommonDto) throws ApplicationException {
		try {
			return iBIReportsDao.getBIReportsRoomOccupancy(bIReportsCommonDto);

		} catch (Exception e) {
		logger.error("getBIReportsRoomOccupancy failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	
	@Override
	public Response getBIReportsForDashboard(BIReportsCommonDto bIReportsCommonDto)
			throws ApplicationException {
		try {
			Response res= iBIReportsDao.getBIReportsForDashboard(bIReportsCommonDto);
			
			String inpuDateFormat = "yyyy-MM-dd";
		       
			if(bIReportsCommonDto.getPeriod().equals("daily"))
	        	inpuDateFormat = "yyyy-MM-dd";
	        else
	        	inpuDateFormat = "yyyy-MM";
			
			List<Date> dates = new ArrayList<Date>();
			List<String> datesArray = new ArrayList<String>();
			Date startDate;
			Date endDate;
			if(bIReportsCommonDto.getPeriod().equals("daily"))
			{
				startDate = GlobalCommonDateUtils.getDate(bIReportsCommonDto.getFromDate().substring(0,10), inpuDateFormat);
				endDate = GlobalCommonDateUtils.getDate(bIReportsCommonDto.getToDate().substring(0,10), inpuDateFormat);
			}else{
				startDate = GlobalCommonDateUtils.getDate(bIReportsCommonDto.getFromDate().substring(0,7), inpuDateFormat);
				endDate = GlobalCommonDateUtils.getDate(bIReportsCommonDto.getToDate().substring(0,7), inpuDateFormat);
			}
			
			long interval = 24 * 1000 * 60 * 60; // 1 hour in millis
			long endTime = endDate.getTime(); // create your endtime here, possibly using Calendar or Date
			long curTime = startDate.getTime();
			
			while (curTime <= endTime) {
				dates.add(new Date(curTime));
				curTime += interval;
			}
			if(bIReportsCommonDto.getPeriod().equals("daily")){
				for (int i = 0; i < dates.size(); i++) 
				{
					Date lDate = (Date) dates.get(i);
					String dateStr = GlobalCommonDateUtils.getStringDate(lDate, inpuDateFormat);
					datesArray.add(dateStr);
				}
			}else{
				String lastDate = "";
				for (int i = 0; i < dates.size(); i++) 
				{
					Date lDate = (Date) dates.get(i);
	    			String dateStr = GlobalCommonDateUtils.getStringDate(lDate, "yyyy-MM");
	    			if(lastDate=="" || !(lastDate.equals(dateStr)))
	    				datesArray.add(dateStr);
	    			lastDate = dateStr;
				}
			}
			JSONArray finalArray = new JSONArray();
			List<BIBedDetailsReportDto> dataSet = res.getListObject();

			for (String date : datesArray) 
			{
				JSONObject dateObject = new JSONObject();
				Field changeMap = dateObject.getClass().getDeclaredField("map");
				changeMap.setAccessible(true);
				changeMap.set(dateObject, new LinkedHashMap<>());
				changeMap.setAccessible(false);

				Stream<BIBedDetailsReportDto> dataSetStream = dataSet.stream();
				// JSONObject dateObject = new JSONObject();
				final String finalDate = date;
				Date dateFormat = GlobalCommonDateUtils.getDate(date, inpuDateFormat);
				
				dateObject.put("trandate", GlobalCommonDateUtils.getStringDate(dateFormat, inpuDateFormat));
				dateObject.put("organizationId", bIReportsCommonDto.getOrganizationId());
				
				if(bIReportsCommonDto.getUnitId()!=null)
					dateObject.put("unitId", bIReportsCommonDto.getUnitId());
				
			
				
				// bed occupancy details
				
				if(bIReportsCommonDto.isBedOccupancy())
				{
					Stream<BIBedDetailsReportDto> dataSetStream4 = dataSet.stream();
					List<BIBedDetailsReportDto> bedOccupancyList = dataSetStream4
							.filter(str ->  str.getCreatedDate().equals(finalDate))
							.collect(Collectors.toList());
					
					JSONObject bedOccupancyObj = null;
					JSONArray bedOccupancyArray = new JSONArray();
					for (BIBedDetailsReportDto bedOccupancys : bedOccupancyList) 
					{
						bedOccupancyObj = new JSONObject();
						bedOccupancyObj.put(bedOccupancys.getBedCategoryDesc(),bedOccupancys.getBedOccupancy());
						bedOccupancyArray.put(bedOccupancyObj);
					}
					dateObject.put("bedOccupancy", bedOccupancyArray);
				}
				// total occupancy details
				if(bIReportsCommonDto.isTotalOccupancy()) {
					Stream<BIBedDetailsReportDto> dataSetStream4 = dataSet.stream();
					List<BIBedDetailsReportDto> totalOccupancyList = dataSetStream4
							.filter(str ->  str.getCreatedDate().equals(finalDate))
							.collect(Collectors.toList());
					
					JSONObject totalOccupancyObj = null;
					JSONArray totalOccupancyArray = new JSONArray();
					for (BIBedDetailsReportDto totalOccupancys : totalOccupancyList) 
					{
						totalOccupancyObj = new JSONObject();
						totalOccupancyObj.put("occupancy",totalOccupancys.getTotalOccupancy());
						totalOccupancyArray.put(totalOccupancyObj);
					}
					dateObject.put("totalOccupancy", totalOccupancyArray);
				}
				// dischargeCount details
				if(bIReportsCommonDto.isTotalOccupancy()) {
					Stream<BIBedDetailsReportDto> dataSetStream4 = dataSet.stream();
					List<BIBedDetailsReportDto> dischargeCountList = dataSetStream4
							.filter(str ->  str.getCreatedDate().equals(finalDate))
							.collect(Collectors.toList());
					
					JSONObject dischargeCountObj = null;
					JSONArray dischargeCountArray = new JSONArray();
					int i=0;
					for (BIBedDetailsReportDto totalOccupancys : dischargeCountList) 
					{
						dischargeCountObj = new JSONObject();
						dischargeCountObj.put("dischargeCount",totalOccupancys.getDischargeCount());
						dischargeCountArray.put(dischargeCountObj);
						if(i==0)
							break;
					}
					dateObject.put("totalDischargeCount", dischargeCountArray);
				}
				/// admissionCount details
				if(bIReportsCommonDto.isTotalOccupancy()) {
					Stream<BIBedDetailsReportDto> dataSetStream4 = dataSet.stream();
					List<BIBedDetailsReportDto> admissionCountList = dataSetStream4
							.filter(str ->  str.getCreatedDate().equals(finalDate))
							.collect(Collectors.toList());
					
					JSONObject admissionCountObj = null;
					JSONArray admissionCountArray = new JSONArray();
					int i=0;
					for (BIBedDetailsReportDto totalOccupancys : admissionCountList) 
					{
						admissionCountObj = new JSONObject();
						admissionCountObj.put("admissionCount",totalOccupancys.getAdmissionCount());
						admissionCountArray.put(admissionCountObj);
						if(i==0)
							break;
						
					}
					dateObject.put("totalAdmissionCountCount", admissionCountArray);
				}
				
			
				finalArray.put(dateObject);
			}
			ObjectMapper mapper = new ObjectMapper();
			JsonNode json = mapper.readTree(finalArray.toString());
			return new Response<>(SUCCESS, json);
			
			

		} catch (Exception e) {
		logger.error("getBIReportsForDashboard failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	

}
