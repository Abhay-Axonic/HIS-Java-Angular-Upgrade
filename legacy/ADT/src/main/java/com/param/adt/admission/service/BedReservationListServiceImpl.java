package com.param.adt.admission.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.adt.admission.dao.IBedReservationListDao;
import com.param.adt.admission.dto.UnreservedPatientDto;
import com.param.adt.common.ICommonConstants;
import com.param.adt.common.Response;
import com.param.global.dto.AdmissionDto;
import com.param.global.service.VerifiedIdentityService;

import in.param.exception.ApplicationException;
@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class BedReservationListServiceImpl implements IBedReservationListService,ICommonConstants
{

	@Autowired
	IBedReservationListDao iBedReservationListDao;

	@Autowired
	private VerifiedIdentityService verifiedIdentityService;
	

	@Override
	public Response getBedReservationList(AdmissionDto admissionDto) throws ApplicationException {
		try {
			return iBedReservationListDao.getBedReservationList(admissionDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);

	}


	@Override
	public Response unreservePatrientBooking(UnreservedPatientDto unreservedPatientDto) throws ApplicationException 
	{
		try{
			// BR-182 rebind: the client-supplied org/unit are overridden with the verified session identity
			unreservedPatientDto.setOrganizationId(verifiedIdentityService.getVerifiedOrganizationId());
			unreservedPatientDto.setUnitId(verifiedIdentityService.getVerifiedUnitId());
			
			/*AdmissionNoteDto admissionNoteDto = new AdmissionNoteDto();
			admissionNoteDto.setOrganizationId(unreservedPatientDto.getOrganizationId());
			admissionNoteDto.setUnitId(unreservedPatientDto.getUnitId());
			admissionNoteDto.setAdmissionNoteId(unreservedPatientDto.getAdmissionNoteId());
			admissionNoteDto.setBedCategoryId(unreservedPatientDto.getBedCategoryId());
			admissionNoteDto.setDoa(unreservedPatientDto.getDoa());
			admissionNoteDto.setCreatedBy(unreservedPatientDto.getCreatedBy());
			admissionNoteDto.setUpdatedBy(unreservedPatientDto.getUpdatedBy());
		
		if(unreservedPatientDto.getIsCancelReservation()=='Y')
		{
			
			if(unreservedPatientDto.getBedCategoryWaitingListId()>0)
			{
				AdmissionDto admissionDto = new AdmissionDto();
					admissionDto.setOrganizationId(unreservedPatientDto.getOrganizationId());
					admissionDto.setUnitId(unreservedPatientDto.getUnitId());
					admissionDto.setAdmissionNoteId(unreservedPatientDto.getAdmissionNoteId());
					admissionDto.setBedCategoryId(unreservedPatientDto.getBedCategoryId());
					admissionDto.setDoa(unreservedPatientDto.getDoa());
					admissionDto.setWaitListNumber(unreservedPatientDto.getWaitListNumber());
					admissionDto.setStatus('A');
					admissionDto.setCreatedBy(unreservedPatientDto.getUpdatedBy());
					admissionDto.setUpdatedBy(unreservedPatientDto.getUpdatedBy());
					admissionDto.setCreatedDate(unreservedPatientDto.getUpdatedDate());
					admissionDto.setUpdatedDate(unreservedPatientDto.getUpdatedDate());
					admissionDto.setBedCategoryWaitingListId(unreservedPatientDto.getBedCategoryWaitingListId());
				iBedAllocationService.updateWatingListNumber(admissionDto);
			}
			
			unreservedPatientDto.setIsCancelReservation('Y');
			unreservedPatientDto.setIsRescheduleReservation('N');
			unreservedPatientDto.setStatus('I');
			iBedReservationListDao.cancelReservation(unreservedPatientDto);
		}
		else
		{
			
			if(unreservedPatientDto.getBedCategoryWaitingListId()==0 && !(unreservedPatientDto.getDoa().equals(unreservedPatientDto.getNewDoa())))
			{
					
				Response res4 = iAdmissionRequestService.saveWaitingListNumber(admissionNoteDto);
					Integer bedCategoryWatingListId=(Integer)res4.getObject();
					unreservedPatientDto.setBedCategoryWaitingListId(bedCategoryWatingListId);
					return new Response (SUCCESS,null,null,null,null);
			}
			
			if(unreservedPatientDto.getBedCategoryWaitingListId()>0)
			{
				AdmissionDto admissionDto = new AdmissionDto();
					admissionDto.setOrganizationId(unreservedPatientDto.getOrganizationId());
					admissionDto.setUnitId(unreservedPatientDto.getUnitId());
					admissionDto.setAdmissionNoteId(unreservedPatientDto.getAdmissionNoteId());
					admissionDto.setBedCategoryId(unreservedPatientDto.getBedCategoryId());
					admissionDto.setDoa(unreservedPatientDto.getDoa());
					admissionDto.setWaitListNumber(unreservedPatientDto.getWaitListNumber());
					admissionDto.setStatus('A');
					admissionDto.setCreatedBy(unreservedPatientDto.getUpdatedBy());
					admissionDto.setUpdatedBy(unreservedPatientDto.getUpdatedBy());
					admissionDto.setCreatedDate(unreservedPatientDto.getUpdatedDate());
					admissionDto.setUpdatedDate(unreservedPatientDto.getUpdatedDate());
					admissionDto.setBedCategoryWaitingListId(unreservedPatientDto.getBedCategoryWaitingListId());
				Response res3 = iBedAllocationService.updateWatingListNumber(admissionDto);
				
				
				Response res4 = iAdmissionRequestService.saveWaitingListNumber(admissionNoteDto);
					
				if (res4.getStatus().equals(SUCCESS)) 
				{
					AdmissionDto admissionDto2 = new AdmissionDto();
						admissionDto.setOrganizationId(unreservedPatientDto.getOrganizationId());
						admissionDto.setUnitId(unreservedPatientDto.getUnitId());
						admissionDto.setAdmissionNoteId(unreservedPatientDto.getAdmissionNoteId());
						admissionDto.setBedCategoryId(unreservedPatientDto.getBedCategoryId());
						admissionDto2.setDoa(unreservedPatientDto.getDoa());
						admissionDto2.setWaitListNumber(unreservedPatientDto.getWaitListNumber());
						admissionDto2.setStatus('A');
						admissionDto2.setCreatedBy(unreservedPatientDto.getUpdatedBy());
						admissionDto2.setUpdatedBy(unreservedPatientDto.getUpdatedBy());
						admissionDto2.setCreatedDate(unreservedPatientDto.getUpdatedDate());
						admissionDto2.setUpdatedDate(unreservedPatientDto.getUpdatedDate());
						admissionDto2.setBedCategoryWaitingListId(unreservedPatientDto.getBedCategoryWaitingListId());
					return iBedAllocationService.updateWatingListNumber(admissionDto2);
				
				}
			}
			unreservedPatientDto.setIsRescheduleReservation('Y');
			unreservedPatientDto.setDoa(unreservedPatientDto.getNewDoa());
			unreservedPatientDto.setActiveStatus('P');
			unreservedPatientDto.setIsFlexible(unreservedPatientDto.getIsFlexible());
			unreservedPatientDto.setPdd(unreservedPatientDto.getNewPdd());
			unreservedPatientDto.setCreatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););
			unreservedPatientDto.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););
			iBedReservationListDao.rescheduleReservation(unreservedPatientDto);
		}*/
			
			if(unreservedPatientDto.getIsCancelReservation()=='Y')
			{
				
				unreservedPatientDto.setIsCancelReservation('Y');
				unreservedPatientDto.setIsRescheduleReservation('N');
				unreservedPatientDto.setStatus('I');
				/*unreservedPatientDto.setCreatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););
				unreservedPatientDto.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););*/
				iBedReservationListDao.cancelReservation(unreservedPatientDto);
			}
			else
			{
				unreservedPatientDto.setIsRescheduleReservation('Y');
				unreservedPatientDto.setDoa(unreservedPatientDto.getNewDoa());
				unreservedPatientDto.setActiveStatus('P');
				unreservedPatientDto.setIsFlexible(unreservedPatientDto.getIsFlexible());
				unreservedPatientDto.setPdd(unreservedPatientDto.getNewPdd());
				/*unreservedPatientDto.setCreatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););
				unreservedPatientDto.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););*/
				iBedReservationListDao.rescheduleReservation(unreservedPatientDto);
			}
			
			return new Response<>(SUCCESS, null, COMMON_SUCCESS, null, null);
		}catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
		
	}


	@Override
	public Response getBedReservationCount(AdmissionDto admissionDto) throws ApplicationException {
		try {
			return iBedReservationListDao.getCount(admissionDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}


}
