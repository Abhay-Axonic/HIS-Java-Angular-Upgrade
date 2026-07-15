package com.param.batchjobs.service;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import in.param.exception.ApplicationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.billing.global.transaction.dao.IBillingDetailsDao;
import com.param.billing.global.transaction.dao.IBillingMasterDao;
import com.param.billing.global.transaction.dao.IDoctorReferralShareDao;
import com.param.billing.global.transaction.dao.IDoctorShareDao;
import com.param.billing.global.transaction.dto.MDoctorReferralShareDto;
import com.param.billing.global.transaction.dto.TDoctorReferralShareDto;
import com.param.billing.global.transaction.dto.TDoctorShareRespDto;
import com.param.billing.global.transaction.model.DoctorReferalShareCountMapper;
import com.param.billing.unit.dao.IMDoctorReferralShareDao;
import com.param.billing.unit.dto.MDoctorReferralShareReqDto;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.IReferalDetailsDao;
import com.param.global.dto.ReferalDetailsDto;
import com.param.utility.TimeZoneComponent;

@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class TDoctorShareImpl implements IDoctorShare, ICommonConstants{

	private static final Logger logger = LoggerFactory.getLogger(TDoctorShareImpl.class);

	@Autowired
	private IDoctorShareDao iDoctorShareDao;
	
	@Autowired
	private  IDoctorReferralShareDao  iDoctorReferralShareDao;
	
	@Autowired
	IBillingMasterDao  iBillingMasterDao;
	
	@Autowired
	IBillingDetailsDao   iBillingDetailsDao;
	
	@Autowired
	IMDoctorReferralShareDao iMDoctorReferralShareDao;
	
	@Autowired
	IReferalDetailsDao iReferalDetailsDao;
	
	/**
	 *  Get List From Billing Details and save in to Doctor Share Table
	 */
	
	@Override
	@Transactional
	public Response getDoctorShareService() {
		
		try{
			int startPoint = 0;
			int maxResults = 1000;
			boolean isSync = false;
			while(!isSync){
				Response response = iDoctorShareDao.getDataFromBillingDtls(startPoint, maxResults,0);
				if(response.getCode().equals(SUCCESS_CODE) && response.getListObject() != null && response.getListObject().size() > 0){
					ListIterator<TDoctorShareRespDto> tDoctorShareRespDtoItr = response.getListObject().listIterator();
					while(tDoctorShareRespDtoItr.hasNext()){
						TDoctorShareRespDto tDoctorShareRespDto = tDoctorShareRespDtoItr.next();
						if(tDoctorShareRespDto.getSharePer()!=null)
						{
							//calculate doctor due share
							double amount = tDoctorShareRespDto.getTotalAmount() - tDoctorShareRespDto.getDiscount();
							double docShare = (tDoctorShareRespDto.getSharePer()/100) * amount; 
							tDoctorShareRespDto.setDueShareAmt(docShare);
							tDoctorShareRespDto.setDocShare(docShare);
							tDoctorShareRespDto.setHospShare(amount - docShare);
							Response result=  iDoctorShareDao.saveDoctorShareData(tDoctorShareRespDto);
							if(result.getStatus().equals(SUCCESS)) {
								//iBillingDetailsDao.updateBillDetailsIsDoctorSharePostedByBillId(tDoctorShareRespDto);
							}
								//System.out.println("data save in t_doctor_share"+tDoctorShareRespDto.getBillNumber());
						}
						
					}
					
					startPoint = maxResults;
					maxResults = maxResults + 1000;
				}else{
					isSync = true;
				}
			}
		}catch(Exception e){
			throw new ApplicationException(e.getMessage(), e);
		}
		return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
	}

	public Response saveOrderDetails(){
		return null;
	}

	@Override
	@Transactional
	public Response getDoctorReferralShareService() {

		try{
			int startPoint = 0;
			int maxResults = 1000;
			boolean isSync = false;
			//getting list of referral doctor list 
			Response referralDetailsResponse=iMDoctorReferralShareDao.getDoctorReferralDetails();
			while(!isSync){
				//checking duration of referral doctor				
				
				Date currentDate=TimeZoneComponent.getDateByzone(0);
				if(referralDetailsResponse.getListObject()!=null && referralDetailsResponse.getListObject().size()>0){
						ListIterator<MDoctorReferralShareReqDto> mDoctorReferralShareDtolist=referralDetailsResponse.getListObject().listIterator();
					
						while(mDoctorReferralShareDtolist.hasNext()){
							
							MDoctorReferralShareReqDto dto=mDoctorReferralShareDtolist.next();
							Date effectiveDate=GlobalCommonDateUtils.getDate(dto.getEffectiveDate(), "dd-M-yyyy");
							Integer duration =dto.getDuration();
							//checking effective is applicable or not
							 if(effectiveDate.before(currentDate)) {								 
								
								MDoctorReferralShareDto dtoList=null;
									Response referalResponse=iMDoctorReferralShareDao.getReferralDetails(dto.getReferraledDocId(),dto.getVisitTypeId(),dto.getOrgId(),dto.getUnitId());
									if(referalResponse.getListObject()!=null && referalResponse.getListObject().size()>0){
											dtoList=(MDoctorReferralShareDto)referalResponse.getListObject().get(0);
											//save the no of referral a day 
											DoctorReferalShareCountMapper mapper=new DoctorReferalShareCountMapper();
											mapper.setCreatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(0), "dd-M-yyyy HH:mm:ss"));//											
											mapper.setVisitTypeId(dtoList.getVisitTypeId());
											mapper.setNoOfReferralCount(referalResponse.getListObject().size());
											mapper.setOrgId(dtoList.getOrgId());
											mapper.setOrgUnitId(dtoList.getOrgUnitId());
											mapper.setReferraledDocId(dtoList.getReferraledDocId());
											mapper.setStatus('A');
											mapper.setIsRead('A');
											iMDoctorReferralShareDao.saveReferralCount(mapper);
										}
										//getting the record from  billing.t_doctor_referal_share_count_mapper and chking the that record with current date ,
										// if record matches then  save.
										if(dto.getReferraledDocId()!=null){
											
										Response countDetailsResponse=	iMDoctorReferralShareDao.getAllReferralCountDetails(dto.getReferraledDocId(),dto.getVisitTypeId(),dto.getOrgId(),dto.getUnitId());
										if(countDetailsResponse.getListObject()!=null && countDetailsResponse.getListObject().size()>0){
											MDoctorReferralShareReqDto reqDto=(MDoctorReferralShareReqDto)countDetailsResponse.getListObject().get(0);
											Date firstDate=GlobalCommonDateUtils.getDate(reqDto.getCreatedDate(),"dd-M-yyyy");
											int diffOfDays=getDifferenceDays(firstDate,currentDate);
											List <MDoctorReferralShareReqDto> listDto = countDetailsResponse.getListObject();
											int referralCount = listDto.stream().mapToInt(MDoctorReferralShareReqDto -> MDoctorReferralShareReqDto.getNoOfReferralCount()).sum();
											if(diffOfDays==duration){
												//getting the record for no of cases 
												Response referralShareResponse=	iMDoctorReferralShareDao.getDoctorReferralDetailsByDoctorReferralId(dto.getReferraledDocId(),dto.getVisitTypeId(),dto.getOrgId(),dto.getUnitId());
												if(referralShareResponse.getListObject()!=null && referralShareResponse.getListObject().size()>0){
													MDoctorReferralShareReqDto shareAndCaseDto=null;
													Integer share=null;
													if(referralShareResponse.getListObject().size()==1){
														 shareAndCaseDto=(MDoctorReferralShareReqDto)referralShareResponse.getListObject().get(0);
														 share=shareAndCaseDto.getShare();
														 
													}else{
														List<MDoctorReferralShareReqDto> noOfCasesList=referralShareResponse.getListObject();
														
														//calculateing the share
														List<Integer> noOfcases = noOfCasesList.stream().map(x -> x.getNoOfCases()).collect(Collectors.toList());
														
														/*Integer[] noOfCasesArray = new Integer[noOfcases.size()]; 
														noOfCasesArray = noOfcases.toArray(noOfCasesArray);	*/											
														share =	 getNoOfShare(referralCount, noOfcases);
														
												      
													}
													//System.out.println("Total share applicable is ="+share);
													//End IF
													//Getting all encounter & admission id's from  
													Response responseEncounterAdmission = iMDoctorReferralShareDao.getEncounterAndAdmissionIds(dto.getReferraledDocId(),firstDate,dto.getVisitTypeId(),dto.getOrgId(),dto.getUnitId());
													if(responseEncounterAdmission.getListObject()!=null && responseEncounterAdmission.getListObject().size()>0){
														List<MDoctorReferralShareReqDto> MDoctorReferralShareReqDtoList=responseEncounterAdmission.getListObject();
														List<Integer> idsList = MDoctorReferralShareReqDtoList.stream().map(x -> x.getEncounterId()).collect(Collectors.toList());
													
														//calculating the referral share by applying the share
														Response response = iDoctorShareDao.getDataFromBillingMst(startPoint, maxResults,idsList);
														
														if(response.getCode().equals(SUCCESS_CODE) && response.getListObject() != null && response.getListObject().size() > 0){
															ListIterator<TDoctorReferralShareDto> tDoctorReferralShareDtoItr = response.getListObject().listIterator();
															while(tDoctorReferralShareDtoItr.hasNext()){
																TDoctorReferralShareDto tDoctorReferralShareDto = tDoctorReferralShareDtoItr.next();
																//calculate doctor due share
																double amount = tDoctorReferralShareDto.getTotalAmount() - tDoctorReferralShareDto.getOutstanding();
																tDoctorReferralShareDto.setReferralSharePer(tDoctorReferralShareDto.getShare()!=null ? tDoctorReferralShareDto.getShare() : share*1.0);
																tDoctorReferralShareDto.setNetAmount(amount);
																com.param.billing.common.Response result= iDoctorReferralShareDao.saveDoctorReferralShare(tDoctorReferralShareDto);
																Date tommorrowDate = TimeZoneComponent.getDateByzone(0);
																Calendar c = Calendar.getInstance(); 
																c.setTime(tommorrowDate); 
																c.add(Calendar.DATE, 1);
																tommorrowDate = c.getTime();
																if(tDoctorReferralShareDto.getBillStatusId()!=3 && tDoctorReferralShareDto.getOutstanding()>0 ){
																	ReferalDetailsDto referalDetailsDto =new ReferalDetailsDto();
																	referalDetailsDto.setShare(tDoctorReferralShareDto.getShare()!=null ? tDoctorReferralShareDto.getShare() : share*1.0);
																	referalDetailsDto.setAdmissionId(tDoctorReferralShareDto.getAdmissionId()!=null ? tDoctorReferralShareDto.getAdmissionId() : null);
																	referalDetailsDto.setEncounterId(tDoctorReferralShareDto.getEncounterId()!=null ? tDoctorReferralShareDto.getEncounterId() : null);
																	referalDetailsDto.setOrganizationId(tDoctorReferralShareDto.getOrgId());
																	referalDetailsDto.setUnitId(tDoctorReferralShareDto.getOrgUnitId());
																	referalDetailsDto.setReferralId(tDoctorReferralShareDto.getReferralDocId());
																	referalDetailsDto.setReferralTypeId(tDoctorReferralShareDto.getReferralTypeId());
																	referalDetailsDto.setVisitTypeId(dto.getVisitTypeId());
																	referalDetailsDto.setCreatedDate(GlobalCommonDateUtils.getStringDate(tommorrowDate, "dd-M-yyyy HH:mm:ss"));
																	iReferalDetailsDao.saveReferalDetails(referalDetailsDto);
																}
																if(result.getStatus().equals(SUCCESS) && tDoctorReferralShareDto.getBillStatusId()==3) {
																	iBillingMasterDao.updateBillMstIsDoctorReferralPostedByBillId(tDoctorReferralShareDto);
																}
															
															}
															//update count mapper to as T
															Response updateDetailsResponse=	iMDoctorReferralShareDao.updateReferralCountDetails(dto.getReferraledDocId(),dto.getVisitTypeId(),dto.getOrgId(),dto.getUnitId());
															startPoint = maxResults;
															maxResults = maxResults + 1000;
														}
													}
													
												}
												
												
												
											}
											
										}
							 		}
										/*if(res.getStatus().equals(SUCCESS)){
											MDoctorReferralShareReqDto dtoforUpdate=new MDoctorReferralShareReqDto();
											dtoforUpdate.setReferraledDocId(dtoList.getReferraledDocId());
											iMDoctorReferralShareDao.updateReferralByReferralDocId(dtoforUpdate);
										}*/
									
//								}
								/*else{
									// get no of count for particular duration
									Response res=iMDoctorReferralShareDao.getNoOfCount(dtoList.getReferraledDocId());
									if(res.getObject()!=null){
										MDoctorReferralShareReqDto countDto=(MDoctorReferralShareReqDto)res.getObject();
										Integer noOfReferralCount=countDto.getNoOfReferralCount();
									}
									
									MDoctorReferralShareReqDto dtoforUpdate=new MDoctorReferralShareReqDto();
									if(dtoList.getReferraledDocId()!=null){
										dtoforUpdate.setReferraledDocId(dtoList.getReferraledDocId());
										iMDoctorReferralShareDao.updateReferralCountMapper(dtoforUpdate);
									}
									
								}*/
								
								/*else{*/
									
									/*Response response = iDoctorShareDao.getDataFromBillingMst(startPoint, maxResults);
								
									if(response.getCode().equals(SUCCESS_CODE) && response.getListObject() != null && response.getListObject().size() > 0){
										ListIterator<TDoctorReferralShareDto> tDoctorReferralShareDtoItr = response.getListObject().listIterator();
										while(tDoctorReferralShareDtoItr.hasNext()){
											TDoctorReferralShareDto tDoctorReferralShareDto = tDoctorReferralShareDtoItr.next();
											//calculate doctor due share
											double amount = tDoctorReferralShareDto.getTotalAmount() - tDoctorReferralShareDto.getDiscount();
											tDoctorReferralShareDto.setNetAmount(amount);
											com.param.billing.common.Response result= iDoctorReferralShareDao.saveDoctorReferralShare(tDoctorReferralShareDto);
											if(result.getStatus().equals(SUCCESS)) {
												iBillingMasterDao.updateBillMstIsDoctorReferralPostedByBillId(tDoctorReferralShareDto);
											}
										
										}
										
										startPoint = maxResults;
										maxResults = maxResults + 1000;
									}*/
									/*else{
										isSync = true;
									}
								}*/
						}
							
							
						}
						isSync = true;
					
				}
				/*Response response = iDoctorShareDao.getDataFromBillingMst(startPoint, maxResults);
				if(response.getCode().equals(SUCCESS_CODE) && response.getListObject() != null && response.getListObject().size() > 0){
					ListIterator<TDoctorReferralShareDto> tDoctorReferralShareDtoItr = response.getListObject().listIterator();
					while(tDoctorReferralShareDtoItr.hasNext()){
						TDoctorReferralShareDto tDoctorReferralShareDto = tDoctorReferralShareDtoItr.next();
						//calculate doctor due share
						double amount = tDoctorReferralShareDto.getTotalAmount() - tDoctorReferralShareDto.getDiscount();
						tDoctorReferralShareDto.setNetAmount(amount);
						com.param.billing.common.Response result= iDoctorReferralShareDao.saveDoctorReferralShare(tDoctorReferralShareDto);
						if(result.getStatus().equals(SUCCESS)) {
							iBillingMasterDao.updateBillMstIsDoctorReferralPostedByBillId(tDoctorReferralShareDto);
						}
					
					}
					
					startPoint = maxResults;
					maxResults = maxResults + 1000;
				}else{
					isSync = true;
				}*/
			}
		}catch(Exception e){
			throw new ApplicationException(e.getMessage(), e);
		}
		return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
	}
	//calculate date diff in numnber
	 public int getDifferenceDays(Date d1, Date d2) {
	        int daysdiff = 0;
	        long diff = d2.getTime() - d1.getTime();
	        long diffDays = diff / (24 * 60 * 60 * 1000) + 1;
	        daysdiff = (int) diffDays;
	        return daysdiff;
	    }
	 //calculating the share using no cases 
	 public Integer getNoOfShare(int toFind,List<Integer> noOfCases) {
		 int i=0;
		 Integer noOfShare=0;
		 //sorting list in ascending order
		 Collections.sort(noOfCases);
		 while(i < (noOfCases.size()-1)){       	  
	        	
	        	if(toFind<=noOfCases.get(i)){
	        		noOfShare=noOfCases.get(i);
	        		break;
	        	}else 
	        	if(toFind>=noOfCases.get(noOfCases.size()-1)){
	        		noOfShare=noOfCases.get(noOfCases.size()-1);
	        		break;
	        	}
	        	else if(toFind>noOfCases.get(i) && toFind<=noOfCases.get(i+1)){
	        		noOfShare=noOfCases.get(i+1);
	        		break;
	        	}else{
	        		//System.out.println("do nothing");
	        	}       	

	        	i++;
	        }
		 return noOfShare;
	 }
}
