package com.param.opd.coversheet.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.param.exception.ApplicationException;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.unit.dto.AllergyPatientMapperDto;
import com.param.global.unit.dto.ClinicalHeaderTabDto;
import com.param.opd.coversheet.dao.IAllergyPatientMapperDao;
import com.param.opd.coversheet.model.AllergyPatientMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
public class AllergyPatientMapperServiceImpl implements
		IAllergyPatientMapperService, ICommonConstants {
	final static Logger logger = Logger.getLogger(AllergyPatientMapperServiceImpl.class);


	@Autowired
	private IAllergyPatientMapperDao iAllergyPatientMapperDao;
	
	@Override
	@Transactional
	public Response saveAllergyPatientMapper(
			AllergyPatientMapperDto allergyPatientMapperDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iAllergyPatientMapperDao.saveAllergyPatientMapper(allergyPatientMapperDto);
	}

	@Override
	@Transactional
	public Response getListOfAllergyPatientMapper(
			AllergyPatientMapperDto allergyPatientMapperDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iAllergyPatientMapperDao.getListOfAllergyPatientMapper(allergyPatientMapperDto);
	}

	@Override
	@Transactional
	public Response cancelAllergyPatientMapper(
			AllergyPatientMapperDto allergyPatientMapperDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iAllergyPatientMapperDao.cancelAllergyPatientMapper(allergyPatientMapperDto);
	}

	@Override
	@Transactional
	public Response getOldAllergyPatientMapper(
			AllergyPatientMapperDto allergyPatientMapperDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iAllergyPatientMapperDao.getOldAllergyPatientMapper(allergyPatientMapperDto);
	}

	@Override
	@Transactional
	public Response getCurrentAllergyPatientMapper(
			AllergyPatientMapperDto allergyPatientMapperDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iAllergyPatientMapperDao.getCurrentAllergyPatientMapper(allergyPatientMapperDto);
	}

	@Override
	@Transactional
	public Response updateAllergyPatientMapper(
			AllergyPatientMapperDto allergyPatientMapperDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iAllergyPatientMapperDao.updateAllergyPatientMapper(allergyPatientMapperDto);
	}

	@Override
	@Transactional
	public Response updateSaveNurseReviewedFlag(
			AllergyPatientMapper allergyPatientMapper)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iAllergyPatientMapperDao.updateSaveNurseReviewedFlag(allergyPatientMapper);
	}

	@Override
	@Transactional
	public Response updateStatusEnterInError(
			AllergyPatientMapperDto allergyPatientMapperDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iAllergyPatientMapperDao.updateStatusEnterInError(allergyPatientMapperDto);
	}

	@Override
	@Transactional
	public Response getGenericAutoFillSearch(AllergyPatientMapperDto allergyPatientMapperDto)
			throws ApplicationException {
			try {
					return iAllergyPatientMapperDao.getGenericAutoFillSearch(allergyPatientMapperDto);
					}catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	public Response getCurrentAllergyPatientMapperForCoversheet(AllergyPatientMapperDto allergyPatientMapperDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iAllergyPatientMapperDao.getCurrentAllergyPatientMapperForCoversheet(allergyPatientMapperDto);
	}

	@Override
	public Response getPatientAllergyListForIcon(AllergyPatientMapperDto allergyPatientMapperDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iAllergyPatientMapperDao.getPatientAllergyListForIcon(allergyPatientMapperDto);
	}

	@Override
	public Response getAllergyPatientMapperForBookView(AllergyPatientMapperDto allergyPatientMapperDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iAllergyPatientMapperDao.getAllergyPatientMapperForBookView(allergyPatientMapperDto);
	}

	@Override
	public Response saveResolvedAllergyDate(AllergyPatientMapperDto allergyPatientMapperDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iAllergyPatientMapperDao.saveResolvedAllergyDate(allergyPatientMapperDto);
	}

	@Override
	public Response getResolvedAllergyPatientMapperForCoversheet(AllergyPatientMapperDto allergyPatientMapperDto)
			throws ApplicationException {
		return iAllergyPatientMapperDao.getResolvedAllergyPatientMapperForCoversheet(allergyPatientMapperDto);
	}

	@Override
	public Response getPatientAllergies(AllergyPatientMapperDto allergyPatientMapperDto) throws ApplicationException {
		return iAllergyPatientMapperDao.getPatientAllergies(allergyPatientMapperDto);
	
	}

	@Override
	public Response getActiveClinicalHeaderTabList(ClinicalHeaderTabDto clinicalHeaderTabDto)
			throws ApplicationException {
		return iAllergyPatientMapperDao.getActiveClinicalHeaderTabList(clinicalHeaderTabDto);
	}

	@Override
	public Response getMappedCoversheetDataForUser(ClinicalHeaderTabDto clinicalHeaderTabDto)
			throws ApplicationException {
		return iAllergyPatientMapperDao.getMappedCoversheetDataForUser(clinicalHeaderTabDto);
		}

	@Override
	public Response getFrequentlyUsedAllergies(AllergyPatientMapperDto allergyPatientMapperDto)
			throws ApplicationException {
		return iAllergyPatientMapperDao.getFrequentlyUsedAllergies(allergyPatientMapperDto);
	}

	@Override
	public Response searchAllergenList(AllergyPatientMapperDto allergyPatientMapperDto) throws ApplicationException {
		// TODO Auto-generated method stub
		return iAllergyPatientMapperDao.searchAllergenList(allergyPatientMapperDto);
	}

	@Override
	public Response getAllergyForIPDCoversheetByDate(AllergyPatientMapperDto dto) throws ApplicationException {
		try{
			List<AllergyPatientMapperDto> resDtoList = new ArrayList<>();

			for (String date : dto.getDates()){

				List<AllergyPatientMapperDto> oldAllergyList = new ArrayList<>();
				List<AllergyPatientMapperDto> currentAllergyList = new ArrayList<>();
				AllergyPatientMapperDto resDto = new AllergyPatientMapperDto();
				resDto.setAddedDate(date);
				if(dto.getAllergyOldList() != null && dto.getAllergyOldList().size()>0){
					for (AllergyPatientMapperDto allergy : dto.getAllergyOldList()) {

						if (date.equals(allergy.getCreatedDate())) {
							allergy.setAddedDate(date);
							oldAllergyList.add(allergy);
						}
					}
					resDto.setAllergyOldList(oldAllergyList);
				}

				if(dto.getAllergyCurrentList() != null && dto.getAllergyCurrentList().size()>0){
					for (AllergyPatientMapperDto complaint : dto.getAllergyCurrentList()) {

						if (date.equals(complaint.getCreatedDate())) {
							complaint.setAddedDate(date);
							currentAllergyList.add(complaint);
						}
					}
					resDto.setAllergyCurrentList(currentAllergyList);
				}
				resDtoList.add(resDto);
			}

			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, resDtoList, null);
		}
		catch (Exception e){
		logger.error("getAllergyForIPDCoversheetByDate failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getUniqueDateForIPDCoversheet(AllergyPatientMapperDto dto) throws ApplicationException {
		try{
			List<String> distinctDates = new ArrayList<>();
			AllergyPatientMapperDto resDto = dto;

			if(dto.getAllergyOldList() != null && dto.getAllergyOldList().size()>0){
				for (AllergyPatientMapperDto allergy : dto.getAllergyOldList()) {

					if (!distinctDates.contains(allergy.getCreatedDate())) {
						distinctDates.add(allergy.getCreatedDate());
					}
				}
			}

			if(dto.getAllergyCurrentList() != null && dto.getAllergyCurrentList().size()>0){
				for (AllergyPatientMapperDto allergy : dto.getAllergyCurrentList()) {

					if (!distinctDates.contains(allergy.getCreatedDate())) {
						distinctDates.add(allergy.getCreatedDate());
					}
				}
			}

			if(distinctDates != null && distinctDates.size()>0){
				Collections.sort(distinctDates, Comparator.reverseOrder());
				resDto.setDates(distinctDates);
				return getAllergyForIPDCoversheetByDate(resDto);
			}
			else{
				return new Response<>(ERROR, ERROR_CODE, null, null, null);
			}
		}
		catch (Exception e){
		logger.error("getUniqueDateForIPDCoversheet failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
}
