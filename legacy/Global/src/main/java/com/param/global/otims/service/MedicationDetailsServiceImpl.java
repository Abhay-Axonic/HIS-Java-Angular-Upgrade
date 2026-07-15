package com.param.global.otims.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.otims.dao.IMedicationDetailsDao;
import com.param.global.otims.dto.MedicationDetailsDto;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings({ "rawtypes", "unchecked"})
@Service
public class MedicationDetailsServiceImpl implements IMedicationDetailsService, ICommonConstants{
	final static Logger logger = Logger.getLogger(MedicationDetailsServiceImpl.class);


	@Autowired
	private IMedicationDetailsDao iMedicationDetailsDao;

	@Override
	public Response saveMedicationDetails(List<MedicationDetailsDto> medicationDetailsDto) throws ApplicationException {
		try{
			return iMedicationDetailsDao.saveMedicationDetails(medicationDetailsDto,0);
			
		} catch(Exception e){
			logger.error("saveMedicationDetails failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}

	@Override
	public Response getMedicationDetailsByProcedureId(MedicationDetailsDto medicationDetailsDto)
			throws ApplicationException {
		try{
			return iMedicationDetailsDao.getMedicationDetailsByProcedureId(medicationDetailsDto);
			
		} catch(Exception e){
			logger.error("getMedicationDetailsByProcedureId failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}

	@Override
	public Response getMedicationDetails(MedicationDetailsDto medicationDetailsDto) throws ApplicationException {
		try{
			return iMedicationDetailsDao.getMedicationDetails(medicationDetailsDto);
			
		} catch(Exception e){
			logger.error("getMedicationDetails failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}

	@Override
	public Response updateMedicationDetailsIsExported(List<MedicationDetailsDto> list) throws ApplicationException {
		try{
			return iMedicationDetailsDao.updateMedicationDetailsIsExported(list);
			
		} catch(Exception e){
			logger.error("updateMedicationDetailsIsExported failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}

	@Override
	public Response getMedicationDetailsByProcedureIdOnOTIMS(MedicationDetailsDto medicationDetailsDto)
			throws ApplicationException {
		try{
			return iMedicationDetailsDao.getMedicationDetailsByProcedureIdOnOTIMS(medicationDetailsDto);
			
		} catch(Exception e){
			logger.error("getMedicationDetailsByProcedureIdOnOTIMS failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
}
