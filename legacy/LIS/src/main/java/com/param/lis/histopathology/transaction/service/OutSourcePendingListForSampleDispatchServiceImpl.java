package com.param.lis.histopathology.transaction.service;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.entity.lis.micro.MicrobioResultEntryMaster;
import com.param.entity.lis.transaction.LabSampleDetailsMaster;
import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.ITransactionConstants;
import com.param.lis.global.common.Response;
import com.param.lis.histopathology.transaction.dao.IOutSourcePendingListForSampleDispatchDao;
import com.param.lis.histopathology.transaction.dto.OutSourceDetailMasterDto;
import com.param.lis.histopathology.transaction.dto.OutSourceMasterDto;
import com.param.lis.microbiology.transaction.dao.IincubationObservationDao;
import com.param.lis.microbiology.transaction.service.IMicrobiologyService;
import com.param.lis.transaction.dto.LabSampleDetailsMasterDto;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings(
{ "rawtypes", "unchecked" })
public class OutSourcePendingListForSampleDispatchServiceImpl  implements IOutSourcePendingListForSampleDispatchService, ICommonConstants,ITransactionConstants ,IError
{
	
	final static Logger logger = Logger.getLogger(OutSourcePendingListForSampleDispatchServiceImpl.class);
	
	@Autowired
	IOutSourcePendingListForSampleDispatchDao iOutSourcePendingListForSampleDispatchDao;
	
	
	@Autowired
	IincubationObservationDao iincubationObservationDao;

    @Autowired
    IMicrobiologyService iMicrobiologyService;
    
	@Override
	@Transactional
	public Response outSourcePendingListForSampleDispatch(OutSourceMasterDto outSourceMasterDto) throws ApplicationException 
	{
		try
		{
			return iOutSourcePendingListForSampleDispatchDao.outSourcePendingListForSampleDispatch(outSourceMasterDto);
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getoutSourcePendingListForSampleDispatchCount(OutSourceMasterDto outSourceMasterDto) throws ApplicationException 
	{
		try
		{
			return iOutSourcePendingListForSampleDispatchDao.getoutSourcePendingListForSampleDispatchCount(outSourceMasterDto);
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}


	@Override
	@Transactional
	public Response saveOutSourceDetails(List<OutSourceMasterDto> listOutSourceMasterDto) throws ApplicationException 
	{
		/*try
		{
			return iOutSourcePendingListForSampleDispatchDao.saveOutSourceDetails(outSourceMasterDto);
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}*/
		
		try
		{
		  if(!listOutSourceMasterDto.isEmpty())
		  {
			  
              Date dt= TimeZoneComponent.getDateByzone(listOutSourceMasterDto.get(0).getOrgUnitId());
              Response<LabSampleDetailsMaster, Integer> sampleStatusRes =null;
              
			  for (Iterator iterator =listOutSourceMasterDto.iterator(); iterator.hasNext();) 
			  {
				 
				  OutSourceMasterDto outSourceMasterDto = (OutSourceMasterDto) iterator.next();
				  Response<MicrobioResultEntryMaster, Integer> microResultRes =  iOutSourcePendingListForSampleDispatchDao.saveOutSourceDetails(outSourceMasterDto);
				 /* if(microResultRes.getCode().equals(SUCCESS_200))
					 {
					   
					  return new Response(SUCCESS, SUCCESS_200, "Out Source Details Added Successfully.", null, null);
					  
						 MicrobioResultEntryMasterDto microbioResultEntryMasterDto= new MicrobioResultEntryMasterDto();
						 microbioResultEntryMasterDto.setCreatedBy(outSourceMasterDto.getCreatedBy());
						 microbioResultEntryMasterDto.setSampleStatusId(OUT_SOURCE_SAMPLE);
						 microbioResultEntryMasterDto.setLabSampleDtlsId(outSourceMasterDto.getLabSampleDtlsId());
						 sampleStatusRes = iincubationObservationDao.updateLabSampleDetailsMaster(microbioResultEntryMasterDto);
						  if(sampleStatusRes.getCode().equals(SUCCESS_200))
							{
								//return new Response(SUCCESS, SUCCESS_200, "Out Source Details Added Successfully.", null, null);
							}
					 }*/
				 
                  if(microResultRes.getCode().equals(SUCCESS_200))
                  {
                     LabSampleDetailsMasterDto labSampleDetailsMasterDto    = new LabSampleDetailsMasterDto();
                     labSampleDetailsMasterDto.setPerformedUnit(outSourceMasterDto.getOrgUnitId());
                     labSampleDetailsMasterDto.setOrgUnitId(outSourceMasterDto.getOrgUnitId());
                     labSampleDetailsMasterDto.setAssignUnit(outSourceMasterDto.getOrgUnitId());
                     labSampleDetailsMasterDto.setCreatedBy(outSourceMasterDto.getCreatedBy());
                     labSampleDetailsMasterDto.setSampleStatusId(OUT_SOURCE_SAMPLE_DISPATCHED);
                     labSampleDetailsMasterDto.setCreatedDate(dt);
                     labSampleDetailsMasterDto.setLabSampleDtlsId(outSourceMasterDto.getLabSampleDtlsId());
                     sampleStatusRes = iMicrobiologyService
                             .updateLabSampleDetailsMaster(labSampleDetailsMasterDto);
                       if(sampleStatusRes.getCode().equals(SUCCESS_200))
                         {
                             //return new Response(SUCCESS, SUCCESS_200, "Out Source Details Added                                 Successfully.", null, null);
                         }
                  }

			    }
			
			
			  return new Response(SUCCESS, SUCCESS_200, "Out Source Details Added Successfully.", null,  null);
		  }
		  else 
		  {
			  return new Response(ERROR, ERR_500, "Failed to Added Out Source Details.", null, null);
		  }
			
		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getOutSourceDetailsByOutSourceId(OutSourceDetailMasterDto outSourceDetailMasterDto)
			throws ApplicationException {
		try
		{
			return iOutSourcePendingListForSampleDispatchDao.getOutSourceDetailsByOutSourceId(outSourceDetailMasterDto);
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}



	

}
