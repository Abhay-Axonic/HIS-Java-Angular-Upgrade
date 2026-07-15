package com.param.adt.admission.service;

import java.math.BigInteger;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.adt.admission.dao.IPassIssuedDao;
import com.param.adt.admission.model.VisitorAgainstPatient;
import com.param.adt.admission.model.VisitorPatientMapper;
import com.param.adt.common.ADTCommonDateUtils;
import com.param.adt.common.IADTConstants;
import com.param.adt.common.ICommonConstants;
import com.param.adt.common.Response;
import com.param.adt.master.global.dto.BedMasterDto;
import com.param.global.dto.AdmissionDto;
import com.param.global.dto.global.VisitorDto;
import com.param.global.unit.dto.SmsUtilityConfigInfoDto;
import com.param.global.unit.service.ISmsUtilityConfigService;
import com.param.utility.TimeZoneComponent;
import com.param.global.common.ISmsUtilityConstants;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class PassIssuedServiceImpl implements ICommonConstants,IPassIssuedService,IADTConstants
{
	
	@Autowired
	IPassIssuedDao iPassIssuedDao;
	
	@Autowired
	private ISmsUtilityConfigService smsUtilityService;
	
	@Autowired
	SessionFactory sessionFactory;
	
	
	
	@Override
	public Response getVisitorTypeList(AdmissionDto admissionDto) throws ApplicationException {
		try {
			return iPassIssuedDao.getVisitorTypeList(admissionDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response saveVisitorAgainsPatient(VisitorDto visitorDto) throws ApplicationException 
	{
		try {
			if(visitorDto.getListVisiterPassMaster()!=null && visitorDto.getListVisiterPassMaster().size()>0)
			{
				Iterator<VisitorDto> itr=visitorDto.getListVisiterPassMaster().iterator();
				while(itr.hasNext())
				{					
					VisitorDto dto=itr.next();
						visitorDto.setVisitorPassTypeId(dto.getVisitorPassTypeId());
						visitorDto.setPassNumber(dto.getPassNumber());
						visitorDto.setPatientId(visitorDto.getPatientId());
					Response res=iPassIssuedDao.saveVisitorAgainsPatient(visitorDto);
					
					VisitorAgainstPatient visitorAgainstPatient=(VisitorAgainstPatient) res.getObject();					
						visitorDto.setVisitorPatientId(visitorAgainstPatient.getVisitorPatientId());
					
					if(res.getStatus().equals(SUCCESS))
					{
						 iPassIssuedDao.saveVisitorPatientMapper(visitorDto);
					}
					try {
					SmsUtilityConfigInfoDto smsInfo=new SmsUtilityConfigInfoDto();
					smsInfo.setUnitId(visitorDto.getUnitId());
					smsInfo.setPatientId(visitorDto.getPatientId());
					smsInfo.setOrganizationId(visitorDto.getOrganizationId());
					smsInfo.setPassNumber(visitorDto.getPassNumber());
					
					smsUtilityService.getSmsUtilityByTriggerId(ISmsUtilityConstants.VISITOR_PASS,smsInfo);
					
					smsUtilityService.getEmailUtilityByTriggerId(ISmsUtilityConstants.VISITOR_PASS, smsInfo);
					}
					catch (Exception e) 
					{
							
						}
				}
	
				return new Response(SUCCESS, null, COMMON_SUCCESS, null, null);
			}
			
		}catch (Exception e) 
		{
				
			}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);

		}


		@Override
		public Response saveVisitorPatientMapper(VisitorDto visitorDto) throws ApplicationException {
			try{
			return iPassIssuedDao.saveVisitorPatientMapper(visitorDto);
			
			}catch(Exception e)
			{
				
			}
			return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
		}


		@Override
		public Response getVisitorsList(VisitorDto visitorDto) throws ApplicationException {
			try {
				return iPassIssuedDao.getVisitorsList(visitorDto);
			} catch (Exception e) {
				
			}
			return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
		}


		
		@Override
		public Response updateVisitorsDetails(VisitorDto visitorDto) throws ApplicationException {
			try{
				
				Iterator<VisitorDto> itr=visitorDto.getVisitorDtosList().iterator();
				while(itr.hasNext())
				{
					
					VisitorDto obj=itr.next();
					VisitorPatientMapper visitorPatientMapper=new VisitorPatientMapper();
					//<-----setting new expiry date if VisitorPassStatus is E(Extended)
					if(obj.getVisitorPassStatus()=='E')
					{
						visitorPatientMapper.setExpiryDate(ADTCommonDateUtils.getDate(obj.getTxtNewDate(),"dd-M-yyyy HH:mm:ss"));
					}
					else
					{
						visitorPatientMapper.setExpiryDate(ADTCommonDateUtils.getDate(obj.getExpiryDate(),"dd-M-yyyy HH:mm:ss"));
					}
					//----->
					
					visitorPatientMapper.setOrganizationId(obj.getOrganizationId());
					visitorPatientMapper.setUnitId(obj.getUnitId());
					visitorPatientMapper.setVisitorPatientId(obj.getVisitorPatientId());
					visitorPatientMapper.setVisitorPassStatus(obj.getVisitorPassStatus());
					visitorPatientMapper.setVisitorPassTypeId(obj.getVisitorPassTypeId());
					visitorPatientMapper.setCreatedBy(obj.getCreatedBy());
					visitorPatientMapper.setUpdatedBy(obj.getUpdatedBy());
					visitorPatientMapper.setCreatedDate(ADTCommonDateUtils.getDate(obj.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
					visitorPatientMapper.setUpdatedDate(ADTCommonDateUtils.getDate(obj.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
					visitorPatientMapper.setStatus('A');
					visitorPatientMapper.setVisitTypeId(obj.getVisitTypeId());
					visitorPatientMapper.setBedCategoryId(obj.getBedCategoryId());

					iPassIssuedDao.updateVisitorsDetails(visitorPatientMapper,obj.getVisitorPatientId(),obj.getVisitorPatientMapperId());
				}
				return new Response(SUCCESS, null, UPDATE_VISITOR, null,null);
			}catch(Exception e)
			{
				
			}
			return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
		}


		@Override
		public Response getBedListByWardId(BedMasterDto bedMasterDto) throws ApplicationException {
			try {
				return iPassIssuedDao.getBedListByWardId(bedMasterDto);
			} catch (Exception e) {
				
			}
			return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
		}


		@Override
		public Response getAdmittedPatientListByMultipleCriteria(AdmissionDto admissionDto) {
			try {
				return iPassIssuedDao.getAdmittedPatientListByMultipleCriteria(admissionDto);
			} catch (Exception e) {
				
			}
			return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
		}


		@Override
		public Response getPassNumberAlreadyIssueOrNot(VisitorDto visitorDto) throws ApplicationException {
			try {
				return iPassIssuedDao.getPassNumberAlreadyIssueOrNot(visitorDto);
			} catch (Exception e) {
				
			}
			return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
		}


		@Override
		public Response checkNoOfPassIssueAgainstAdmission(VisitorDto visitorDto) throws ApplicationException {
			try {
				return iPassIssuedDao.checkNoOfPassIssueAgainstAdmission(visitorDto);
			} catch (Exception e) {
				
			}
			return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
		}


		@Override
		public Response generatePassNumber(Integer orgId, Integer unitId) throws ApplicationException {
			try {
				boolean flag = true;
				BigInteger obj = null;
				while (flag) {
				
					 obj  = generateRandom(4);
					 VisitorDto visitorDto=new VisitorDto();
					Response cardNoRes =  iPassIssuedDao.getPassNumberAlreadyIssueOrNot(visitorDto);
					if(cardNoRes.getObject()!=null)
					{
						BigInteger count = (BigInteger)cardNoRes.getObject();
						if(count.compareTo(BigInteger.ZERO)==1)
						{
							flag = true;
						}
						else
						{
							flag = false;
						}
					}
				}
				return new Response<>(SUCCESS, SUCCESS_CODE, null, null, obj);
		
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
		}
		public static BigInteger generateRandom(int length) {
		     Random random = new Random();
		     char[] digits = new char[length];
		     digits[0] = (char) (random.nextInt(9) + '1');
		     for (int i = 1; i < length; i++) {
		         digits[i] = (char) (random.nextInt(10) + '0');
		     }
		         String stringNo = new String(digits);
		        
		      return new BigInteger(stringNo);
		 }


		@Override
		public Response getVisitorPassList(AdmissionDto admissionDto) throws ApplicationException {
			try {
				return iPassIssuedDao.getVisitorPassList(admissionDto);
			} catch (Exception e) {
				
			}
			return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
		}


		@Override
		public Response saveOrupdateVisitorAgainsPatient(VisitorDto visitorDto) throws ApplicationException {
			try {
				if(visitorDto.getListVisiterPassMaster()!=null && visitorDto.getListVisiterPassMaster().size()>0)
				{
					Iterator<VisitorDto> itr=visitorDto.getListVisiterPassMaster().iterator();
					while(itr.hasNext())
					{					
						VisitorDto dto=itr.next();
						
						if(dto.getVisitorPatientMapperId() == null) {
							visitorDto.setVisitorPassTypeId(dto.getVisitorPassTypeId());
							visitorDto.setPassNumber(dto.getPassNumber());
						Response res=iPassIssuedDao.saveVisitorAgainsPatient(visitorDto);
						
						VisitorAgainstPatient visitorAgainstPatient=(VisitorAgainstPatient) res.getObject();					
							visitorDto.setVisitorPatientId(visitorAgainstPatient.getVisitorPatientId());
						
						if(res.getStatus().equals(SUCCESS))
						{
							 iPassIssuedDao.saveVisitorPatientMapper(visitorDto);
						}
						}else {
						
						VisitorPatientMapper visitorPatientMapper=new VisitorPatientMapper();
						//<-----setting new expiry date if VisitorPassStatus is E(Extended)
			
						Session session=sessionFactory.getCurrentSession();
						session.createSQLQuery("update adt.t_visitor_against_patient set "
								+ "pass_number='" + dto.getPassNumber() + "' " 
								+ "where visitor_patient_id="+dto.getVisitorPatientId())
						.executeUpdate();
						
						visitorPatientMapper.setOrganizationId(dto.getOrganizationId());
						visitorPatientMapper.setUnitId(dto.getUnitId());
						visitorPatientMapper.setVisitorPatientId(dto.getVisitorPatientId());
						visitorPatientMapper.setVisitorPassStatus('I');
						visitorPatientMapper.setVisitorPassTypeId(dto.getVisitorPassTypeId());
						visitorPatientMapper.setCreatedBy(dto.getCreatedBy());
						visitorPatientMapper.setUpdatedBy(dto.getUpdatedBy());
						visitorPatientMapper.setCreatedDate(ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dto.getUnitId()),"dd-M-yyyy HH:mm:ss"));
						visitorPatientMapper.setUpdatedDate(ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
						visitorPatientMapper.setStatus('A');
						visitorPatientMapper.setVisitTypeId(dto.getVisitTypeId());
						visitorPatientMapper.setBedCategoryId(dto.getBedCategoryId());

						iPassIssuedDao.updateVisitorsDetails(visitorPatientMapper,dto.getVisitorPatientId(),dto.getVisitorPatientMapperId());
						
						}
						
					}
					return new Response(SUCCESS, null, COMMON_SUCCESS, null, null);
				}
				
			}catch (Exception e) 
			{
					
				}
			return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);

			}
}
