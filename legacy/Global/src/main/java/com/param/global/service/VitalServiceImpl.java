package com.param.global.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.transaction.Transactional;

import com.param.global.common.*;
import com.param.global.dao.IScheduleLogsDao;
import com.param.global.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.dao.IVitalDao;
import com.param.global.model.TVital;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class VitalServiceImpl implements IVitalService, ICommonConstants{

	@Autowired
	private IVitalDao iVitalDao;
	
	@Autowired
	private IVitalMasterService iVitalMasterService;

	@Autowired
	private IScheduleLogsDao isheduleDao;
	
	@Override
	@Transactional
	public Response saveVitals(TVitalDto tVitalDto) throws ApplicationException {
		try{
			Date date = TimeZoneComponent.getDateByzone(tVitalDto.getUnitId());
			String dateTime = GlobalCommonDateUtils.getStringDate(date, "dd-M-yyyy HH:mm:ss");
			tVitalDto.setCreatedDate(dateTime);
			Response vitalRes = iVitalDao.saveVitals(tVitalDto);
				
				if(vitalRes.getCode().equals(SUCCESS_CODE)&& vitalRes.getObject()!=null)
				{
					TVital tvital = (TVital) vitalRes.getObject();
					
					for (Iterator iterator2 = tVitalDto.getVitalList().iterator(); iterator2.hasNext();) {
						TVitalDetailsDto tVitalDetailsDto = (TVitalDetailsDto) iterator2.next();
						tVitalDetailsDto.settVitalId(tvital.gettVitalId());
						 iVitalDao.saveVitalDetails(tVitalDetailsDto);
					}
				}
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
			
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getVitalsByPatient(VitalSearchDto vitalSearchDto) throws ApplicationException {
		try{
			List vitalList= new ArrayList<>();
			Response vitalRes =  iVitalMasterService.getVitalsForNursing(vitalSearchDto.getOrganisationId(), vitalSearchDto.getUnitId());
			Response detailsRes  =  iVitalDao.getVitalsByPatient(vitalSearchDto);
			List<TVitalDto> vitalNames= vitalRes.getListObject();
			List<TVitalDto> detailsList= detailsRes.getListObject();
			
/*			&& ti.getVitalTime()==tVitalDto1.getVitalTime()
					&& ti.getCreatedDate()==tVitalDto1.getCreatedDate() 
					&& ti.gettVitalId()==tVitalDto1.gettVitalId()*/
		
				for (Iterator iterator2 = detailsList.iterator(); iterator2.hasNext();) 
				{
					TVitalDto tVitalDto1 = (TVitalDto) iterator2.next();
					for (Iterator iterator = vitalNames.iterator(); iterator.hasNext();) 
					{
						TVitalDetailsDto tVitalDto = (TVitalDetailsDto) iterator.next();
						if (tVitalDto1.getVitalList().stream().anyMatch(
								ti -> (ti!=null&&ti.getVitalId().intValue() == tVitalDto.getVitalId().intValue())))
						{
							
						} else 
						{
							
							TVitalDetailsDto tVitalDetailsDto  = new TVitalDetailsDto();
							tVitalDetailsDto.settVitalId(tVitalDto1.gettVitalId());
							tVitalDetailsDto.setVitalId(tVitalDto.getVitalId());
							tVitalDetailsDto.setCreatedDate(tVitalDto1.getCreatedDate());
							tVitalDetailsDto.setVitalTime(tVitalDto1.getVitalTime());
							tVitalDetailsDto.setObservation("-");
							tVitalDto1.getVitalList().add(tVitalDetailsDto);
						}
						
					}
				}
		
			vitalList.add(vitalNames);
		    vitalList.add(detailsList);
			
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, vitalList, null);
		
		}catch(Exception e)
		{
			
			throw e;
		}
	}



}
