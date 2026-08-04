package com.param.adt.admission.service;

import java.util.*;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.param.adt.master.global.dto.PaymentEntitlementMasterDto;
import com.param.global.dto.PatientCategoryMasterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.param.adt.admission.dao.IPendingRequestDao;
import com.param.adt.admission.model.AdmissionNote;
import com.param.adt.admission.model.AdmissionPatientDocuments;
import com.param.adt.common.ADTCommonDateUtils;
import com.param.adt.common.DocumentUploadUtility;
import com.param.adt.common.ICommonConstants;
import com.param.adt.common.Response;
import com.param.global.dto.AdmissionDto;
import com.param.global.service.VerifiedIdentityService;
import com.param.global.dto.AdmissionNoteDto;
import com.param.global.dto.AdmissionPatientDocumentsDto;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class PendingRequestServiceImpl implements IPendingRequestService, ICommonConstants {

	@Autowired
	private IPendingRequestDao iPendingRequestDao;

	@Autowired
	private VerifiedIdentityService verifiedIdentityService;
	
	@Autowired
	private DocumentUploadUtility documentUploadUtility;
	
	@Autowired
	IAdmissionRequestService iAdmissionRequestService;
	
	@Autowired
	IBedAllocationService iBedAllocationService;

	private Cache<Integer, List<PatientCategoryMasterDto>> patientCategoryMasterCache;
	private Cache<Integer, List<PaymentEntitlementMasterDto>> activePaymentEntitlementMasterCache;

	@PostConstruct
	public void initCache() {
		patientCategoryMasterCache = CacheBuilder.newBuilder()
				.maximumSize(200)
				.build();

		activePaymentEntitlementMasterCache = CacheBuilder.newBuilder()
				.expireAfterWrite(120, TimeUnit.MINUTES)
				.maximumSize(200)
				.build();
	}

	@Override
	public Response getPendingAdmissionRequestDetails(AdmissionNoteDto admissionNoteDto) throws ApplicationException {
		try {
			return iPendingRequestDao.getPendingAdmissionRequestDetails(admissionNoteDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getAvtivePatientCategoryList(Integer orgId) throws ApplicationException {
		try {
			List<PatientCategoryMasterDto> responseCache = patientCategoryMasterCache.getIfPresent(orgId);
			if (responseCache != null && !responseCache.isEmpty()) {
				return new Response(SUCCESS, SUCCESS_CODE, "List Size Cache: " +String.valueOf(responseCache.size()), responseCache, null);
			}

			Response res = iPendingRequestDao.getAvtivePatientCategoryList(orgId);

			if(res.getStatus().equals(SUCCESS) && res.getListObject() != null && !res.getListObject().isEmpty()){
				patientCategoryMasterCache.put(orgId, res.getListObject());

				return new Response(SUCCESS, SUCCESS_CODE, "List Size DB: " +String.valueOf(res.getListObject().size()), res.getListObject(), null);
			}
			return res;
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getAvtivePaymentEntitlementList(Integer orgId) throws ApplicationException {
		try {
			// BATCH-4 ORG-TRUST FIX: rebind to verified tenant + org-keyed cache (was a constant
			// "paymentEntitlement" key shared across all orgs — cache poisoning + cross-tenant leak).
			orgId = verifiedIdentityService.getVerifiedOrganizationId();
			List<PaymentEntitlementMasterDto> responseCache = activePaymentEntitlementMasterCache.getIfPresent(orgId);
			if (responseCache != null && !responseCache.isEmpty()) {
				return new Response(SUCCESS, SUCCESS_CODE, "List Size Cache: " +String.valueOf(responseCache.size()), responseCache, null);
			}

			Response res = iPendingRequestDao.getAvtivePaymentEntitlementList(orgId);
			if(res.getStatus().equals(SUCCESS) && res.getListObject() != null && !res.getListObject().isEmpty()){
				activePaymentEntitlementMasterCache.put(orgId, res.getListObject());

				return new Response(SUCCESS, SUCCESS_CODE, "List Size DB: " +String.valueOf(res.getListObject().size()), res.getListObject(), null);
			}
			return res;
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	@Transactional
	public Response updatePendingAdmissionRequestDetails(AdmissionNoteDto admissionNoteDto) {
		try {

			 Response res = iPendingRequestDao.updatePendingAdmissionRequestDetails(admissionNoteDto);
				AdmissionNote admissionNoteObiject = (AdmissionNote) res.getObject();
				if (res.getStatus().equals(SUCCESS)) {
					
					admissionNoteDto.setActiveStatus('P');
					Response res2 = updateAdmissionRequestMapperDetails(admissionNoteDto,
							admissionNoteObiject);
					
					if(admissionNoteDto.getBedCategoryWaitingListId()>0 && !(admissionNoteDto.getDoa().equals(admissionNoteDto.getOldDoa())) && admissionNoteDto.getDoa().substring(0,10).equals(ADTCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(admissionNoteDto.getUnitId()), "dd-MM-yyyy")) )
					{
						if(res2.getStatus().equals(SUCCESS))
						{
							AdmissionDto admissionDto = new AdmissionDto();
								admissionDto.setOrganizationId(admissionNoteDto.getOrganizationId());
								admissionDto.setUnitId(admissionNoteDto.getUnitId());
								admissionDto.setAdmissionNoteId(admissionNoteDto.getAdmissionNoteId());
								admissionDto.setBedCategoryId(admissionNoteDto.getOldBedCategoryId());
								admissionDto.setDoa(admissionNoteDto.getOldDoa());
								admissionDto.setWaitListNumber(admissionNoteDto.getWaitListNumber());
								admissionDto.setStatus('A');
								admissionDto.setCreatedBy(admissionNoteDto.getUpdatedBy());
								admissionDto.setUpdatedBy(admissionNoteDto.getUpdatedBy());
								admissionDto.setCreatedDate(admissionNoteDto.getUpdatedDate());
								admissionDto.setUpdatedDate(admissionNoteDto.getUpdatedDate());
								admissionDto.setBedCategoryWaitingListId(admissionNoteDto.getBedCategoryWaitingListId());
							iBedAllocationService.updateWatingListNumber(admissionDto);
							return new Response (SUCCESS,null,null,null,null);
						}
					}
					
					
					if(admissionNoteDto.getBedCategoryWaitingListId()==0 && !(admissionNoteDto.getDoa().equals(admissionNoteDto.getOldDoa())))
					{
						if(res2.getStatus().equals(SUCCESS))
						{
							Response res4 = iAdmissionRequestService.saveWaitingListNumber(admissionNoteDto);
							Integer bedCategoryWatingListId=(Integer)res4.getObject();
							admissionNoteDto.setBedCategoryWaitingListId(bedCategoryWatingListId);
							return new Response (SUCCESS,null,null,null,null);
						}
					}
					else
					{
						if(!(admissionNoteDto.getDoa().equals(admissionNoteDto.getOldDoa())) || !(admissionNoteDto.getBedCategoryId().equals(admissionNoteDto.getOldBedCategoryId())) && admissionNoteDto.getBedCategoryWaitingListId()>0)
						{
							if (res2.getStatus().equals(SUCCESS)) 
							{
								AdmissionDto admissionDto = new AdmissionDto();
									admissionDto.setOrganizationId(admissionNoteDto.getOrganizationId());
									admissionDto.setUnitId(admissionNoteDto.getUnitId());
									admissionDto.setAdmissionNoteId(admissionNoteDto.getAdmissionNoteId());
									admissionDto.setBedCategoryId(admissionNoteDto.getOldBedCategoryId());
									admissionDto.setDoa(admissionNoteDto.getOldDoa());
									admissionDto.setWaitListNumber(admissionNoteDto.getWaitListNumber());
									admissionDto.setStatus('A');
									admissionDto.setCreatedBy(admissionNoteDto.getUpdatedBy());
									admissionDto.setUpdatedBy(admissionNoteDto.getUpdatedBy());
									admissionDto.setCreatedDate(admissionNoteDto.getUpdatedDate());
									admissionDto.setUpdatedDate(admissionNoteDto.getUpdatedDate());
									admissionDto.setBedCategoryWaitingListId(admissionNoteDto.getBedCategoryWaitingListId());
								Response res3 = iBedAllocationService.updateWatingListNumber(admissionDto);
								
								if (res3.getStatus().equals(SUCCESS)) 
								{
									
									Response res4 = iAdmissionRequestService.saveWaitingListNumber(admissionNoteDto);
									
									if (res4.getStatus().equals(SUCCESS)) 
									{
										AdmissionDto admissionDto2 = new AdmissionDto();
											admissionDto2.setOrganizationId(admissionNoteDto.getOrganizationId());
											admissionDto2.setUnitId(admissionNoteDto.getUnitId());
											admissionDto2.setAdmissionNoteId(admissionNoteDto.getAdmissionNoteId());
											admissionDto2.setBedCategoryId(admissionNoteDto.getBedCategoryId());
											admissionDto2.setDoa(admissionNoteDto.getDoa());
											admissionDto2.setWaitListNumber(admissionNoteDto.getWaitListNumber());
											admissionDto2.setStatus('A');
											admissionDto2.setCreatedBy(admissionNoteDto.getUpdatedBy());
											admissionDto2.setUpdatedBy(admissionNoteDto.getUpdatedBy());
											admissionDto2.setCreatedDate(admissionNoteDto.getUpdatedDate());
											admissionDto2.setUpdatedDate(admissionNoteDto.getUpdatedDate());
											admissionDto2.setBedCategoryWaitingListId(admissionNoteDto.getBedCategoryWaitingListId());
										return iBedAllocationService.updateWatingListNumber(admissionDto2);
									
									}
								}
							}
						}									
					}
				}else
				{
					return new Response (ERROR,null,null,null,null);
				}
			 
			 
			 
			return new Response (SUCCESS,null,null,null,null);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	public Response updateAdmissionRequestMapperDetails(AdmissionNoteDto admissionNoteDto,
			AdmissionNote admissionNoteObiject) {
		try {
			admissionNoteDto.setActiveStatus('P');
			return iPendingRequestDao.updateAdmissionRequestMapperDetails(admissionNoteDto);

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateReserveBedDetails(AdmissionNoteDto admissionNoteDto) throws ApplicationException {

		admissionNoteDto.setActiveStatus('R');
		return iPendingRequestDao.updateReserveBedDetails(admissionNoteDto);
	}

	@Override
	public Response uploadAdmissionDocuments(AdmissionNoteDto admissionNoteDto) throws ApplicationException {
		try {
			Iterator<AdmissionPatientDocumentsDto> itr = admissionNoteDto.getDocumentArray().iterator();
			while (itr.hasNext()) 
			{
				AdmissionPatientDocuments admissionPatientDocumentsDto = new AdmissionPatientDocuments();   
				AdmissionPatientDocumentsDto obj = itr.next();
				
				
				MultipartFile document = obj.getDocumentPath();
				String filePath = null;
				if(document != null && document.getSize() > 0){
					
					admissionPatientDocumentsDto.setAdmissionNoteId(admissionNoteDto.getAdmissionNoteId());
					filePath = documentUploadUtility.uploadAdmissionDocuments(document, admissionNoteDto);
				}
				//System.out.println(filePath);
				admissionPatientDocumentsDto.setOrganizationId(admissionNoteDto.getOrganizationId());
				admissionPatientDocumentsDto.setUnitId(admissionNoteDto.getUnitId());
				admissionPatientDocumentsDto.setDocumentTypeId(obj.getDocumentTypeId());
				admissionPatientDocumentsDto.setAdmissionNoteId(admissionNoteDto.getAdmissionNoteId());
				admissionPatientDocumentsDto.setDocumentPath(filePath);
				admissionPatientDocumentsDto.setCreatedBy(admissionNoteDto.getCreatedBy());
				admissionPatientDocumentsDto.setUpdatedBy(admissionNoteDto.getUpdatedBy());
				iPendingRequestDao.uploadAdmissionDocuments(admissionPatientDocumentsDto);
			}
			return new Response(SUCCESS, null, COMMON_UPDATE, null, null);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getPendingAdmissionCount(AdmissionNoteDto admissionNoteDto) throws ApplicationException {
		try {
			return iPendingRequestDao.getCount(admissionNoteDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getOPDAdmissionRequestDetails(Integer encounterId, Integer patientId, Integer organizationId, Integer unitId) throws ApplicationException {
		try {
			return iPendingRequestDao.getOPDAdmissionRequestDetails(encounterId, patientId, organizationId, unitId);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response cancelPendingAdmissionRequest(AdmissionNoteDto admissionNoteDto) {
		try {
			return iPendingRequestDao.cancelPendingAdmissionRequest(admissionNoteDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getDayCareAdmissionRequestDetails(AdmissionNoteDto admissionNoteDto) throws ApplicationException {
		try {
			return iPendingRequestDao.getDayCareAdmissionRequestDetails(admissionNoteDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getAdmissionRequestDetailsForBookView(AdmissionNoteDto admissionNoteDto)
			throws ApplicationException {
		try {
			return iPendingRequestDao.getAdmissionRequestDetailsForBookView(admissionNoteDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getOpAdmissionListForIPDCoversheetByDate(AdmissionNoteDto dto) throws ApplicationException {
		try{
			List<AdmissionNoteDto> resDtoList = new ArrayList<>();

			for(String date : dto.getDates()){
				List<AdmissionNoteDto> opdAdmissionDetails = new ArrayList<>();
				AdmissionNoteDto resDto = new AdmissionNoteDto();

				resDto.setAddedDate(date);
				if(dto.getOpdAdmissionDetails() != null && dto.getOpdAdmissionDetails().size()>0){
					for (AdmissionNoteDto item : dto.getOpdAdmissionDetails()) {

						if (date.equals(item.getCreatedDate())) {
							item.setAddedDate(date);
							opdAdmissionDetails.add(item);
						}
					}
					resDto.setOpdAdmissionDetails(opdAdmissionDetails);
				}
				resDtoList.add(resDto);
			}
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, resDtoList, null);
		}
		catch (Exception e){

		}

		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getUniqueDateForIPDCoversheet(AdmissionNoteDto dto) throws ApplicationException {
		try{
			List<String>distinctDates = new ArrayList<>();
			AdmissionNoteDto resDto = dto;

			if(dto.getOpdAdmissionDetails() != null && dto.getOpdAdmissionDetails().size()>0){
				for (AdmissionNoteDto complaint : dto.getOpdAdmissionDetails()) {

					if (!distinctDates.contains(complaint.getCreatedDate())) {
						distinctDates.add(complaint.getCreatedDate());
					}
				}

				if(distinctDates != null && distinctDates.size()>0){
					Collections.sort(distinctDates, Comparator.reverseOrder());
					resDto.setDates(distinctDates);
					return getOpAdmissionListForIPDCoversheetByDate(resDto);
				}

			}
			else{
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS, null, null);
			}
		}
		catch (Exception e){
			System.out.println(e);
		}

		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

    @Override
    @Transactional
    public Response getPatientRegistrationTypes() throws ApplicationException {
        try {
            Response res = iPendingRequestDao.getPatientRegistrationTypes();

            if(res.getStatus().equals(SUCCESS) && res.getListObject() != null && !res.getListObject().isEmpty()){

                return new Response(SUCCESS, SUCCESS_CODE, "List Size DB: " +String.valueOf(res.getListObject().size()), res.getListObject(), null);
            }
            return res;
        } catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
        
    }

}
