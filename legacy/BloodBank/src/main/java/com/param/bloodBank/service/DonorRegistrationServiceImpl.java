package com.param.bloodBank.service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.param.bloodBank.dao.IBloodGroupMasterDao;
import com.param.bloodBank.dao.IDonorRegistrationDao;
import com.param.bloodBank.dao.IDonorScreeningDetailDao;
import com.param.bloodBank.dto.BloodBagCollectionDto;
import com.param.bloodBank.dto.DonorRegistrationDto;
import com.param.bloodBank.dto.DonorScreeningDetailDto;
import com.param.global.common.DocUploadUtility;
import com.param.global.common.IBillingCommonConstants;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.IGlobalUnitMasterDao;
import com.param.global.dao.IKinDetailsDao;
import com.param.global.dto.SlotMasterDto;
import com.param.global.folder.creation.BucketUtility;
import com.param.global.model.DonorFingerPrintInterface;
import com.param.global.org.dao.IGlobalOrganisationMasterDao;
import com.param.global.org.dto.OrganizationMasterDto;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({"rawtypes", "unchecked","unused"})
public class DonorRegistrationServiceImpl implements IDonorRegistrationService, ICommonConstants,IBillingCommonConstants{

	final static Logger logger = Logger.getLogger(DonorRegistrationServiceImpl.class);


	@Autowired
	private IDonorRegistrationDao iDonorRegistrationDao;
	

	@Autowired
	IBloodGroupMasterDao iBloodGroupMasterDao;
	
	@Autowired
    IGlobalOrganisationMasterDao iGlobalOrganisationMasterDao;
    
    @Autowired
    BucketUtility bucketUtility;
    
    @Autowired
    DocUploadUtility docUploadUtility;
    
    @Autowired
    IKinDetailsDao iKinDetailsDao;
    
    @Autowired
	private IGlobalUnitMasterDao iGlobalUnitMasterDao;
    
    @Autowired
	private IDonorScreeningDetailDao donorScreeningDetailDao;
    
    @Override
	@Transactional
	public Response registerDonor(DonorRegistrationDto donorRegistrationDto) {
		return saveDonorRegistration(donorRegistrationDto);
	}

	public Response saveDonorRegistration(DonorRegistrationDto donorRegistrationDto) throws ApplicationException {
		try {
			if(donorRegistrationDto.getPatientId()!=null) {
			Response res2 = iDonorRegistrationDao.checKPatientAvailability(donorRegistrationDto);
			if (res2.getListObject().size() > 0) {
				return new Response(ERROR, null, " Patient Already exist", null, null);
			 }
			}
			Response res = iDonorRegistrationDao.checkUniqueDonor(donorRegistrationDto);
			if (res.getListObject().size() > 0) {
				return new Response(ERROR, null, " Donor Already exist", null, null);
			} else {
				//<---save donor
				donorRegistrationDto.setIsPrimary('Y');
			//	Long incrementedId = iDonorRegistrationDao.getNextValDonorId();
				donorRegistrationDto.setExportedToScreening(false);
				if(donorRegistrationDto.getIsAutologous() == null || donorRegistrationDto.getIsAutologous() == false)
				{
					donorRegistrationDto.setIsAutologous(false);
				}
				String DonorNumber = iGlobalUnitMasterDao.getGeneratedNo(0, donorRegistrationDto.getUnitId(), DONORNUMBER);
				//donorRegistrationDto.setUhidNumber(DonorNumber);
				donorRegistrationDto.setDonorNumber(DonorNumber);
				Response res1 = iDonorRegistrationDao.saveDonorRegistration(donorRegistrationDto);
				if (res1.getStatus().equals(SUCCESS)) {
					Response donorResponse = iDonorRegistrationDao.saveDonorDetails(donorRegistrationDto);
					if(donorResponse.getStatus().equals(SUCCESS)) {
						DonorRegistrationDto donorRegistrationDto1= (DonorRegistrationDto)res1.getObject();
						//fingerprint
						if(donorRegistrationDto.getThumbImage()!=null)
						{
							donorRegistrationDto.setDonorId(donorRegistrationDto1.getPatientId());
							this.saveDonorFingerPrint(donorRegistrationDto);
						}
							return new Response(SUCCESS, SUCCESS_CODE, "Donor "+donorRegistrationDto.getDonorNumber()+" Saved Successfully ", null, donorRegistrationDto1);
							}
				}
				else {
					return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}
			}
		} catch (Exception e) {
			logger.error("saveDonorRegistration failed", e);
			throw new ServiceException(e.getMessage());
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	private String generateUhid(Long incrementedId) throws ApplicationException {
		try {
			
			//System.out.println(incrementedId);
			String uhidPrefix="DN-";
			/*if(registrationTypeId!=2)
				uhidPrefix = "PC-18-";
			
			if(registrationTypeId==5)
				uhidPrefix = "PR-18-";
			
			if(registrationTypeId==2)
				uhidPrefix = "O-18-";*/
			
			StringBuilder uhid=new StringBuilder(uhidPrefix);
			
				int length = Long.valueOf(incrementedId).toString().length();
				uhid = length == 1 ? (uhid.append("000000").append(incrementedId))
						: length == 2 ? (uhid.append("00000").append(incrementedId))
								: length == 3 ? (uhid.append("0000").append(incrementedId))
										: length == 4 ? (uhid.append("000").append(incrementedId))
												: length == 5 ? (uhid.append("00").append(incrementedId))
														: length == 6 ? (uhid.append("0").append(incrementedId))
																: length == 7 ? (uhid.append(incrementedId)) : uhid.append("");
				return uhid.toString();
			} catch (Exception e) {
				logger.error("generateUhid failed", e);
				throw new ServiceException(e.getMessage());
			}
			
	}
	
	@Override
	@Transactional
	public Response updateDonorRegistration(DonorRegistrationDto donorRegistrationDto) {
		try {
				 return iDonorRegistrationDao.updateDonor(donorRegistrationDto);
				  
		} catch (Exception e) {
			logger.error("updateDonorRegistration failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response getDonorDetails(DonorRegistrationDto patientRegistrationDto) {
		try {
			return iDonorRegistrationDao.getDonorDetails(patientRegistrationDto);
		}catch (Exception e) {
			logger.error("getDonorDetails failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response donorSearch(DonorRegistrationDto donorRegistrationDto) {
try {
	Response res = iDonorRegistrationDao.donorSearchNormal(donorRegistrationDto);
	List<DonorRegistrationDto> finalList = new ArrayList<>();
		if((res.getListObject().size()>0) && (res.getListObject()!=null))
		{
			List<DonorRegistrationDto> list = res.getListObject();
			for (DonorRegistrationDto dto : list) {
				Response res1 =   donorScreeningDetailDao.getDonorScreeningDetailByDonorId(dto.getDonorId(),donorRegistrationDto.getOrganizationId(),donorRegistrationDto.getUnitId());
				
				
				/*if(dto.getBloodGroup() != null) {
					
					int bloodGroupId = Integer.parseInt(dto.getBloodGroup());
					dto.setBloodGroupId(bloodGroupId);
					int org = donorRegistrationDto.getOrganizationId();
					int unit =  donorRegistrationDto.getUnitId();
					Response res22 = iBloodGroupMasterDao.getBloodGroupMasterById(bloodGroupId, org, unit);
					List<BloodGroupMasterDto> list1 = res22.getListObject();
					dto.setBloodGroup( list1.get(0).getBloodGroupDesc()); 
					
					}*/
				
				if(res1.getObject()!=null)
				{

					DonorScreeningDetailDto result = new DonorScreeningDetailDto();
					result = (DonorScreeningDetailDto) res1.getObject();
					if(result.getScreeningResultStatusId()==3)
					{
						dto.setDefferalStatus("Permanent Deffered");
					}
					else if(result.getScreeningResultStatusId()==1)
					{
						dto.setDefferalStatus("Accepted");
					}
					else if(result.getScreeningResultStatusId()==2)
					{
						dto.setDefferalStatus("Rejected");
					}
					else
					{
						dto.setDefferalStatus("Pending");
					}
					dto.setDefferalDays(result.getDeferralDays());
					finalList.add(dto);
				
					
					
					/*
					DonorScreeningDetailDto result = new DonorScreeningDetailDto();
					result = (DonorScreeningDetailDto) res1.getObject();
					if(result.getScreeningResultStatus().equals('P'))
					{
						dto.setDefferalStatus("Permanent Deffered");
					}
					else if(result.getScreeningResultStatus().equals('A'))
					{
						dto.setDefferalStatus("Accepted");
					}
					else if(result.getScreeningResultStatus().equals('R'))
					{
						dto.setDefferalStatus("Rejected");
					}
					else
					{
						dto.setDefferalStatus("Pending");
					}
					dto.setDefferalDays(result.getDeferralDays());
					finalList.add(dto);
				*/}else
				{
					finalList.add(dto);
				}
			}
		
		}
		return new Response<>(SUCCESS, SUCCESS_CODE,COMMON_UPDATE, finalList, null);
		}catch (Exception e) {
			logger.error("donorSearch failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response savePatientImage(int orgId, int unitId, int donorId, MultipartFile inputFile) {
		 try{
		      if(orgId>0 &&unitId>0 &&donorId>0 &&!inputFile.isEmpty())
		      {
		        Response unitRes =  iGlobalOrganisationMasterDao.getOrganiazationFolderPath(orgId);
		        OrganizationMasterDto organizationMasterDto = (OrganizationMasterDto) unitRes.getObject();
		        String patientImagePath =  docUploadUtility.uploadDonorImage(inputFile, organizationMasterDto,unitId,donorId);
		        DonorRegistrationDto donorRegistrationDto = new DonorRegistrationDto();
		       // donorRegistrationDto.setPatientId(patientId);
		        donorRegistrationDto.setDonorId(donorId);
		        donorRegistrationDto.setOrganizationId(orgId);
		        donorRegistrationDto.setUnitId(unitId);
		        donorRegistrationDto.setImagePath(patientImagePath);
		        return iDonorRegistrationDao.updateDonorImagePath(donorRegistrationDto);
		      }
		      return null;
		  }catch(Exception e){
		  	logger.error("savePatientImage failed", e);
		  	throw new ServiceException(e.getMessage());
		  }
	}

	@Override
	public Response patientAutofillSearch(SlotMasterDto slotMasterDto) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	@Transactional
	public Response saveDonorFingerPrint(DonorRegistrationDto dto) throws ApplicationException {
		try {
			
			
			JSONObject jsonString1 = new JSONObject();
			jsonString1.put("BiometricData",dto.getThumbImage());
			JSONArray jsonArguments = new JSONArray();
			jsonArguments.put(jsonString1);
			JSONObject jsonString = new JSONObject();
			jsonString.put("Uuid", "0");
			jsonString.put("Biometrics", jsonArguments);

			if (dto.getThumbImage() != null && !dto.getThumbImage().isEmpty()) {
				CloseableHttpClient httpClient = HttpClientBuilder.create().build();
				HttpPost post = new HttpPost("http://mabistest.mantratecapp.com/4/mabis/biometric/enroll");
				post.addHeader("Content-Type", "application/json;charset=UTF-8");
				post.addHeader("Accept", "application/json");
				StringEntity input = new StringEntity(jsonString.toString(),Charset.forName("UTF-8"));
				input.setContentEncoding("UTF-8");
				input.setContentType("application/json");
				post.setEntity(input);
				HttpResponse response = httpClient.execute(post);
				if (response != null) {
					if (response.getStatusLine().getStatusCode() == Integer.valueOf(SUCCESS_CODE)) {
						String json = EntityUtils.toString(response.getEntity(),"UTF-8");
						JSONObject jsonObj = new JSONObject(json);
						Integer errorCode = Integer.parseInt(jsonObj.getString("ErrorCode"));
						if(errorCode.intValue()==0)
						{
							Integer uuid = Integer.parseInt(jsonObj.getString("Uuid"));
							DonorFingerPrintInterface fingerInterface = new DonorFingerPrintInterface();
							fingerInterface.setUuid(uuid);
							fingerInterface.setOrgnisationId(dto.getOrganizationId());
							fingerInterface.setDonorId(dto.getDonorId());
							fingerInterface.setStatus('A');
							fingerInterface.setUnitId(dto.getUnitId());
							fingerInterface.setThumbImage(dto.getThumbImage());
							return iDonorRegistrationDao.saveDonorThumb(fingerInterface);
						}
					}
				}
				else{
					throw new IOException("client connection to " + post.getURI() + " fail: no connection");
					
					
				}
			}
			return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		} catch (Exception e) {
			logger.error("saveDonorFingerPrint failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
}
