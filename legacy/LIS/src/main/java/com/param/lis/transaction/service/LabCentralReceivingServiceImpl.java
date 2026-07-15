package com.param.lis.transaction.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.entity.lis.transaction.LabSampleMaster;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.lis.global.common.CommonUtils;
import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.ITransactionConstants;
import com.param.lis.global.common.Response;
import com.param.lis.global.common.SearchDto;
import com.param.lis.global.dto.BarcodeDto;
import com.param.lis.transaction.dao.ILabCentralReceivingDao;
import com.param.lis.transaction.dto.LabSampleDetailsMasterDto;
import com.param.lis.transaction.dto.LabSampleMasterDto;
import com.param.lis.transaction.dto.SearchCommonDto;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings(
{ "rawtypes", "unchecked" })
public class LabCentralReceivingServiceImpl implements ILabCentralReceivingService, ICommonConstants, IError,ITransactionConstants
{
	@Autowired
	ILabCentralReceivingDao iLabCentralReceivingDao;

	@Autowired
	private SessionFactory sessionFactory;
	
	final static Logger logger = Logger.getLogger(LabTransactionServiceImpl.class);

	@Transactional
	@Override
	public Response getSampleListForInOutPatient(Integer orgId, Integer orgUnitId, Integer visitTypeId,Integer offset, Integer recordPerPage,Character isSendToCrApplicable)
			throws ApplicationException
	{
		try
		{
			return iLabCentralReceivingDao.getSampleListForInOutPatient(orgId, orgUnitId, visitTypeId,offset,recordPerPage,isSendToCrApplicable);
		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response acceptOrRejectSample(List<LabSampleDetailsMasterDto> listLabSampleDetailsMasterDto)
			throws ApplicationException
	{
		try
		{
			return iLabCentralReceivingDao.acceptOrRejectSample(listLabSampleDetailsMasterDto);
		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getCentrifugationWorklist(Integer orgId, Integer orgUnitId,Integer deptId,Integer offset, Integer recordPerPage) throws ApplicationException
	{
		try
		{
			return iLabCentralReceivingDao.getCentrifugationWorklist(orgId, orgUnitId,deptId,offset,recordPerPage);
		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getTotalRecordOutPatient(SearchDto searchDto) throws ApplicationException {
		try
		{
			return iLabCentralReceivingDao.getTotalRecordOutPatient(searchDto);
		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getTotalcentrifugationWorkList(
			Integer orgId, Integer orgUnitId,Integer deptId) throws ApplicationException {
		try
		{
			return iLabCentralReceivingDao.getTotalcentrifugationWorkList(orgId,orgUnitId,deptId);
		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getTotalRecordINPatient(SearchDto searchDto)
			throws ApplicationException {
		try
		{
			return iLabCentralReceivingDao.getTotalRecordINPatient(searchDto);
		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getRejectedSampleList(Integer orgId, Integer orgUnitId, Integer deptId,Integer offset, Integer recordPerPage) throws ApplicationException
	{
		try
		{
			return iLabCentralReceivingDao.getRejectedSampleList(orgId,orgUnitId,deptId,offset,recordPerPage);
		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getTotalRecordRejectedSample(SearchDto searchDto) throws ApplicationException
	{
		try
		{
			return iLabCentralReceivingDao.getTotalRecordRejectedSample(searchDto);
		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response sampleRecollect(List<LabSampleMasterDto> listLabSampleMasterDto) throws ApplicationException
	{
		try
		{
			Date date = TimeZoneComponent.getDateByzone(listLabSampleMasterDto!=null && listLabSampleMasterDto.size()>0 
					 ? listLabSampleMasterDto.getFirst().getOrgUnitId() : null);
			
			 List<BarcodeDto> barcodeDtoList = new ArrayList<>();
			for (Iterator iterator = listLabSampleMasterDto.iterator(); iterator.hasNext();) 
			{
				LabSampleMasterDto labSampleMasterDto = (LabSampleMasterDto) iterator.next();
				labSampleMasterDto.setSampleGenDatetime(date);
				BigInteger sampleNo = (BigInteger) sessionFactory.getCurrentSession().getNamedQuery("GENERATE_SAMPLE_NO").uniqueResult();
				labSampleMasterDto.setSampleNo(CommonUtils.numberToString(sampleNo,SAMPLE_PREFIX,NUMBER_WIDTH));
				labSampleMasterDto.setSampleId(CommonUtils.numberToStringWithLength(sampleNo,NUMBER_WIDTH));
				for (Iterator iterator2 = labSampleMasterDto.getListLabSampleDetailsMaster().iterator(); iterator2.hasNext();) 
				{
					LabSampleDetailsMasterDto labSampleDetailsMaster = (LabSampleDetailsMasterDto) iterator2.next();
					labSampleDetailsMaster.setSampleCollectionDatetime(date.getTime());
					labSampleDetailsMaster.setSampleGenDatetime(date);
					labSampleDetailsMaster.setCreatedBy(labSampleMasterDto.getCreatedBy());
					labSampleDetailsMaster.setUpdatedBy(labSampleMasterDto.getCreatedBy());
					labSampleDetailsMaster.setSampleCollectionDatetime(date.getTime());
					labSampleDetailsMaster.setSampleGenDatetime(date);
					labSampleDetailsMaster.setPatientVisitAge(date.getTime());
					labSampleDetailsMaster.setSampleNo(sampleNo);
					labSampleDetailsMaster.setSampleRecollectAgainstId(labSampleDetailsMaster.getSampleRecollectAgainstId());
                    labSampleDetailsMaster.setSampleBarcode(CommonUtils.numberToStringWithLength(sampleNo,NUMBER_WIDTH));
				}
				Response labSampleRes =  iLabCentralReceivingDao.sampleRecollect(labSampleMasterDto);
			     if(labSampleRes.getCode().equals(SUCCESS_200)&&labSampleRes.getObject()!=null)
			     {
			    	 LabSampleMaster labSampleMst = (LabSampleMaster) labSampleRes.getObject();
			    	 BarcodeDto barcodeDto = new BarcodeDto();
			    	 barcodeDto.setUhid(labSampleMasterDto.getUhid());
			    	 barcodeDto.setPatientDetails(labSampleMasterDto.getPatientDetails());
			    	 barcodeDto.setPatientId(labSampleMst.getPatientId());
			    	 barcodeDto.setSampleId(labSampleMst.getSampleId());
			    	 barcodeDto.setLabSampleId(labSampleMst.getLabSampleId());
			    	 barcodeDto.setVisitType(labSampleMasterDto.getListLabSampleDetailsMaster().getFirst().getVisitType());
			    	 barcodeDto.setDeptName(labSampleMasterDto.getListLabSampleDetailsMaster().getFirst().getDeptName());
			    	 barcodeDto.setGenerationDateTime(GlobalCommonDateUtils.getStringDate(labSampleMst.getSampleGenDatetime(), "dd/MM/yyyy"));
			    	 barcodeDto.setGenerationTime(GlobalCommonDateUtils.getStringDate(labSampleMst.getSampleGenDatetime(), "HH:mm:ss"));
			    	 barcodeDtoList.add(barcodeDto);
			     }
			}
			/* Map<Integer, Map<Integer, List<LabSampleDetailsMasterDto>>> map = listlabLabSampleDetailsMasterDto.stream()
		        	    .collect(Collectors.groupingBy(LabSampleDetailsMasterDto::getSubDeptId,
		        	        Collectors.groupingBy(LabSampleDetailsMasterDto::getSampleTypeId)));
		        
		        List<LabSampleMasterDto> sampleMasterDto = new ArrayList<>();
			    for (Entry<Integer, Map<Integer, List<LabSampleDetailsMasterDto>>> entry : map.entrySet()) 
		        {
				    //System.out.println(entry.getKey());
				    for (Entry<Integer, List<LabSampleDetailsMasterDto>> entry1 :  entry.getValue().entrySet()) 
			        {
					    //System.out.println(entry1.getKey());
					    LabSampleMasterDto dto =  labSampleMasterDto;
					    List<LabSampleDetailsMasterDto> labSampleDetailsMasterDto = new ArrayList<>();
					    for (Iterator iterator = entry1.getValue().iterator(); iterator.hasNext();) 
					    {
					    	LabSampleDetailsMasterDto sampleDetailsDto = (LabSampleDetailsMasterDto) iterator.next();
					    	labSampleDetailsMasterDto.add(sampleDetailsDto);
						}
					    dto.setListLabSampleDetailsMaster(labSampleDetailsMasterDto);
					    iLabTransactionDao.sampleCollection(labSampleMasterDto);
					}
				 
				}*/
//			CommonUtils.generateSampleEPLString(barcodeDtoList);
			return new Response(SUCCESS, SUCCESS_200, SAMPLE_RECOLLECT_SUCC, barcodeDtoList, null);
			 
		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response sendToDepartMent(List<LabSampleDetailsMasterDto> listlabLabSampleDetailsMasterDto)
			throws ApplicationException
	{
		try
		{
			return iLabCentralReceivingDao.sendToDepartMent(listlabLabSampleDetailsMasterDto);
		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getFilteredOutPatientList(SearchDto searchDto) throws ApplicationException {
		try
		{
			return iLabCentralReceivingDao.getFilteredOutPatientList(searchDto);
		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getFilteredCentrifugationWorkList(SearchDto searchDto) throws ApplicationException {
		try
		{
			return iLabCentralReceivingDao.getFilteredCentrifugationWorkList(searchDto);
		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getRejectedSampleListPatientSearch(SearchCommonDto searchDto) throws ApplicationException {
		try
		{
			return iLabCentralReceivingDao.getRejectedSampleListPatientSearch(searchDto);
		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getRejectedSampleListSearch(SearchDto searchDto) throws ApplicationException {
		try
		{
			return iLabCentralReceivingDao.getRejectedSampleListSearch(searchDto);
		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getTotalcentrifugationWorkListCount(SearchDto searchDto)
			throws ApplicationException {
		try
		{
			return iLabCentralReceivingDao.getTotalcentrifugationWorkListCount(searchDto);
		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}

}
