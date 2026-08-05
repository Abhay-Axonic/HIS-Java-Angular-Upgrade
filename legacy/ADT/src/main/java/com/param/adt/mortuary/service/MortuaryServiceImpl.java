package com.param.adt.mortuary.service;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.adt.common.ICommonConstants;
import com.param.adt.mortuary.dao.IMortuaryDao;
import com.param.global.common.IBillingCommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.IGlobalUnitMasterDao;
import com.param.global.dto.MortuaryDto;
import com.param.global.service.VerifiedIdentityService;
import com.param.global.unit.dto.CabinMasterDto;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class MortuaryServiceImpl implements IMortuaryService, ICommonConstants {

	@Autowired
	IMortuaryDao iMortuaryDao;
	
	@Autowired
	IGlobalUnitMasterDao iGlobalUnitMasterDao;

	@Autowired
	private VerifiedIdentityService verifiedIdentityService;

	@Override
	public Response saveDeathRegistation(MortuaryDto mortuaryDto) throws ApplicationException {
		try {
			mortuaryDto.setOrganizationId(verifiedIdentityService.getVerifiedOrganizationId());
			mortuaryDto.setUnitId(verifiedIdentityService.getVerifiedUnitId());
			Response res=iMortuaryDao.checkAlreadyExistReg(mortuaryDto);
			if (res.getStatus() != null && res.getStatus().equalsIgnoreCase(ERROR)) {
				return res;
			}
	        if(res.getListObject()!=null && res.getListObject().size()>0){
				
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				mortuaryDto.setUhidNumber(generateDUHID(mortuaryDto.getUnitId()));
				return iMortuaryDao.saveDeathRegistation(mortuaryDto);
			}
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	
	@Override
	public Response checkAlreadyExistReg(MortuaryDto mortuaryDto) throws ApplicationException{
			try {
				
				return iMortuaryDao.checkAlreadyExistReg(mortuaryDto);
		
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, null, null, null, null);
	}
	
	@Override
	public Response saveMortuaryRequest(MortuaryDto mortuaryDto) throws ApplicationException {
		try {

			return iMortuaryDao.saveMortuaryRequest(mortuaryDto);

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	public String generateDUHID(Integer unitId) {
		return iGlobalUnitMasterDao.getGeneratedNo(0, unitId, IBillingCommonConstants.DEATH_REGESTRATION_UHID);
	}

	@Override
	public Response getPendingMortuaryRequestList(MortuaryDto mortuaryDto) throws ApplicationException {
		try {
			return iMortuaryDao.getPendingMortuaryRequestList(mortuaryDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response acceptRejectMortuaryRequestFromPendingList(MortuaryDto mortuaryDto) throws ApplicationException {
		try {
			return iMortuaryDao.acceptRejectMortuaryRequestFromPendingList(mortuaryDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getReservedMortuaryRequestList(MortuaryDto mortuaryDto) throws ApplicationException {
		try {
			return iMortuaryDao.getReservedMortuaryRequestList(mortuaryDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getMorturyBedList(MortuaryDto mortuaryDto) throws ApplicationException {
		try {
			return iMortuaryDao.getMorturyBedList(mortuaryDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response saveBedAllocationMortuary(MortuaryDto mortuaryDto) throws ApplicationException {
		try {
			if(mortuaryDto.getMortuaryStatusId()==3)
				mortuaryDto.setToeBandId(generateToeBandId());
			
			return iMortuaryDao.saveBedAllocationMortuary(mortuaryDto);
			
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}


	@Override
	public Response getAdmittedMortuaryList(MortuaryDto mortuaryDto) throws ApplicationException {
		try {
			return iMortuaryDao.getAdmittedMortuaryList(mortuaryDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response releaseMorturyBed(MortuaryDto mortuaryDto) throws ApplicationException {
		try {
			return iMortuaryDao.releaseMorturyBed(mortuaryDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	public static String generateToeBandId() {
		String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		StringBuilder salt = new StringBuilder("TBID000");
		Random rnd = new Random();
		while (salt.length() < 10) {
			int index = (int) (rnd.nextFloat() * SALTCHARS.length());
			salt.append(SALTCHARS.charAt(index));
		}
		String saltStr = salt.toString();
		return saltStr;

	}

	@Override
	public Response getDataForMorturyMapOfBed(MortuaryDto mortuaryDto) throws ApplicationException {
		try {
			return iMortuaryDao.getDataForMorturyMapOfBed(mortuaryDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response cancelMorgueRequest(MortuaryDto mortuaryDto) throws ApplicationException {
		try {
			return iMortuaryDao.cancelMorgueRequest(mortuaryDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

}
