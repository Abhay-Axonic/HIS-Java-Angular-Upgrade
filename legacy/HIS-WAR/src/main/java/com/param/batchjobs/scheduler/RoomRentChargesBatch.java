package com.param.batchjobs.scheduler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.dto.SlotMasterDto;
import com.param.global.org.service.IGlobalUnitMasterService;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.param.CSSD.service.IPreSterilizationProcessService;
import com.param.batchjobs.service.IBatchDao;
import com.param.batchjobs.service.IRoomRentBatchChargesService;
import com.param.billing.global.smartcard.dto.AdmissionJson;
import com.param.billing.global.smartcard.dto.DiagnosisJson;
import com.param.billing.global.smartcard.dto.InvoicesJson;
import com.param.billing.global.smartcard.dto.ItemsJson;
import com.param.billing.global.smartcard.dto.PaymentModifiersJson;
import com.param.billing.global.smartcard.dto.PreauthrizationJson;
import com.param.billing.global.smartcard.dto.SmartAdmitionReqDto;
import com.param.billing.global.smartcard.dto.SmartCardBillReqDto;
import com.param.billing.global.smartcard.dto.SmartCardIItemsReqDto;
import com.param.billing.global.smartcard.dto.SmartCardJson;
import com.param.billing.global.smartcard.dto.SmartCardPreauthReqDto;
import com.param.billing.global.smartcard.dto.SmartDiagnosisReqDto;
import com.param.billing.global.transaction.dao.IBillingMasterDao;
import com.param.global.billing.dto.PaymentRecieptMasterDto;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.dao.ITPackageConsumptionMasterDao;
import com.param.global.dao.ITallyIntegrationService;
import com.param.global.opd.service.IAppointmentSchedulingService;
import com.param.global.service.ICEODashboardService;
import com.param.global.service.IUserMasterService;
import com.param.global.unit.service.ISmsUtilityConfigService;
import com.param.opd.coversheet.service.IPatientDiagnosisService;
import com.param.opd.coversheet.service.IVaccineImmunizationMapperService;
import com.param.opd.unit.service.ISlotMasterService;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;
import com.param.global.common.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class RoomRentChargesBatch {

	private static final Logger logger = LoggerFactory.getLogger(RoomRentChargesBatch.class);

	@Autowired
	SessionService sessionService;
	
	@Autowired
	private IRoomRentBatchChargesService roomRentBatchChargesServ;
	
	@Autowired
	private ITallyIntegrationService iTallyIntegrationService;
	
	@Autowired
	IAppointmentSchedulingService iAppointmentSchedulingService;  
	
	@Autowired
	IBatchDao batchDao;

	@Autowired
	IPatientDiagnosisService iPatientDiagnosisService;
	
	@Autowired
	ISmsUtilityConfigService iSmsUtilityConfigService;
	
	@Autowired
	IBillingMasterDao iBillingMasterDao;
	
	@Autowired
	IUserMasterService iUserMasterService;
	
	@Autowired
	ICEODashboardService iCEODashboardService;
	
	@Autowired
	ISlotMasterService iSlotMasterService;
	
	@Autowired
    IPreSterilizationProcessService iPreSterilizationProcessService;
	
	@Autowired
	ITPackageConsumptionMasterDao iTPackageConsumptionMasterDao;
	
	@Autowired
	IVaccineImmunizationMapperService ivaccineImmunizationMapperService;

	@Autowired
	IGlobalUnitMasterService iGlobalUnitMasterService;
	
	static boolean lockBatch = true;
	
	static boolean lockRoomRent = true;
	
	static String claimLink = "https://data.smartapplicationsgroup.com:8087/sandbox/edi/claim";
	static String key = "IlAjdyf02tlM8ZRXN/jc2kK6oTnU6hK1dpzzPiloMNY09NR6ycuDrCgxOa40no8X";
	static String token = "d4vpH3VGhYxSlfxqUTNKBF4ke1MMDheDDQmlQEWzwIq";
	
	static final String croneString = "0 5 0 1/1 * ?";
   
	/*@Scheduled(cron = croneString , zone = ICommonConstant.serverTimezone)
	@Transactional
	public void midnightBatch(){
		try{
			
			if(lockBatch == true && batchDao.isBatchExecuted()==true)
			{ 
				lockBatch = false;
				try{
						batchDao.addBathLog();
					}catch(Exception e){
						
					}
					
					try{
						iSmsUtilityConfigService.sendBatchSMS();
					}catch(Exception e){
						
					}
					
					try{
						iAppointmentSchedulingService.batchJobToUpdateMultiVisitPackage();
					}catch(Exception e){
						
					}
					
					try{
						iTPackageConsumptionMasterDao.getExistingActiveMultivisitPackageForDeactivation();
					}catch(Exception e){
						
					}
					
				Tally posting
						try{
							iTallyIntegrationService.postVisitFaData();
						}catch(Exception e){
							
						}
						try{
							iTallyIntegrationService.postDepositData();
						}catch(Exception e){
							
						}
						try{
							iTallyIntegrationService.postDoctorShare();
						}catch(Exception e){
							
						}
						try{
							iTallyIntegrationService.postCreditNotes();
						}catch(Exception e){
							
						}
						try{
							iTallyIntegrationService.postAdvanceRefund();
						}catch(Exception e){
							
						}
						try{
							iTallyIntegrationService.postCompanySettlement();
						}catch(Exception e){
							
						}
				
						try{
							iTallyIntegrationService.postGrnNotes();
						}catch(Exception e){
							
						}
						
						try{
							iTallyIntegrationService.postSaleNotes();
						}catch(Exception e){
							
						}
						
						try{
							iUserMasterService.updateSchedulingLog();
						}catch(Exception e){
							
						}
						
						try{
							iCEODashboardService.postDashboardCounts();
						}catch(Exception e){
							
						}
						
						try {
							iPreSterilizationProcessService.updateSterilizationStock();
						} catch (Exception e) {
							
						}
				tally posting
						//vaccination Due alert
						try {
							ivaccineImmunizationMapperService.batchJobToSendMailAndMessageForVaccinationDueDate();
						} catch (Exception e) {
							
						}
				
			}
		}catch(Exception e){
			
		}
		finally {
			lockBatch = true;
		}
	}

    
    @Scheduled(cron = croneString , zone = ICommonConstant.serverTimezone)
	@Transactional
	public void postRoomRent(){
		try{
			
			if(lockRoomRent == true && batchDao.isBatchExecutedRoomRent()==true)
			{ 
				lockRoomRent = false;
				try{
					batchDao.addBathLogForRoomRent();
				}catch(Exception e){
					
				}
				
				try{
					roomRentBatchChargesServ.postRoomRentCharges();
				}catch(Exception e){
					
				}
				
				try{	
					String pattern = "dd";
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
					String date = simpleDateFormat.format(TimeZoneComponent.getDateByzone(0));
					if(date.toString().equals("01"))
					{
						roomRentBatchChargesServ.postPeriodicServices();
						
						iSlotMasterService.deleteUnusedSlots();
					}
						
					
				}catch(Exception e){
					
				}
				
			}
		}catch(Exception e){
			
		}finally {
			lockRoomRent = true;
		}
	}
*/    
	
	@Transactional
	public void claimSendToSmartSystem(){
		try{
			batchDao.clainSentToSmartCard();
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	
	
	/*@Transactional
	public void claimSendToSmartSystem(){
		try{
			
			BillListReqDto billListDto = new BillListReqDto();
			billListDto.setFromDate("09-04-2019 00:00:00");
			billListDto.setToDate("09-04-2019 00:00:00");
			billListDto.setIsOtcReg('N');
			billListDto.setOrgId(1);
			billListDto.setUnitId(10);
			List<BillListResDto> listBills = null;
			listBills =  (List<BillListResDto>) iBillingMasterService.getBillsByFilterForBillList(billListDto);
			
			ListIterator<BillListResDto> iterator = listBills.listIterator();
			while (iterator.hasNext()) {
				BillListResDto billListResDto = iterator.next();
				
				int unitId = 10;
				int organizationId = 1;
				String serviceType = null;
				if(billListResDto.getVisitId() == 1)
				{
					serviceType = "OUTPATIENT";
				}
				else
				{
					serviceType = "INPATIENT";
				}
				
				org.hibernate.Transaction tr = sessionFactory.getCurrentSession().beginTransaction();
				tr.begin();
				SponserDto spDeDto =(SponserDto) sessionFactory.getCurrentSession()
						.createSQLQuery("select payee_id,payment_entitlement_id from opd.t_patient_sponsor_details  where unit_id=' "
								+ unitId+"' and organization_id = '"+organizationId+"' and priority_id = 1 and  "
										+ " (encounter_id = '"+billListResDto.getVisitId()+"' or admission_id = '"+billListResDto.getVisitId()+"' limit 1 ")
						.setResultTransformer(Transformers.aliasToBean(SponserDto.class)).uniqueResult();
				InsuranceTPAMapperDto insuranceTPAMapperDto =null;
				if(spDeDto != null)
				{
					if(spDeDto.getPayeeId() > 1 && spDeDto.getPaymentEntitlementId() == 2)
					{
						 insuranceTPAMapperDto =(InsuranceTPAMapperDto) sessionFactory.getCurrentSession()
								.createSQLQuery("select insurance_code,insurance_desc,am.ar_code,company_code,company_desc from adt.t_insurance ins "
										+ "Left Join service.t_insurance_tpa_mapper itm on itm.insurance_id = ins.insurance_id "
										+ "Left join public.t_company_master cm on itm.company_id = cm.company_id "
										+ "Left join public.m_ar_code_master am om am.ar_code_id = cm.ar_code_id where ins.unit_id=' "
										+ unitId+"' and ins.organization_id = '"+organizationId+"' and ins.insurance_id = '"+spDeDto.getPayeeId()+"' limit 1 ")
								.setResultTransformer(Transformers.aliasToBean(InsuranceTPAMapperDto.class)).uniqueResult();
						
					}
					else if(spDeDto.getPayeeId() > 1 && spDeDto.getPaymentEntitlementId() == 3	)
					{
						 insuranceTPAMapperDto =(InsuranceTPAMapperDto) sessionFactory.getCurrentSession()
								.createSQLQuery("select am.ar_code,company_code,company_desc from public.t_company_master cm "
										+ "Left join public.m_ar_code_master am om am.ar_code_id = cm.ar_code_id where cm.unit_id=' "
										+ unitId+"' and cm.organization_id = '"+organizationId+"' and cm.company_id = '"+spDeDto.getPayeeId()+"' limit 1 ")
								.setResultTransformer(Transformers.aliasToBean(InsuranceTPAMapperDto.class)).uniqueResult();
					}
					
				}
				
				String JsonStr = "{	'claim_code':'MMCC00030','payer_code':'"+insuranceTPAMapperDto.getCompanyCode()+"',"
						+ "'payer_name':'"+insuranceTPAMapperDto.getCompanyDesc()+"','amount':'"+billListResDto.getTotalBillAmount()+"',"
						+ "'gross_amount':'"+billListResDto.getTotalBillAmount()+"','batch_number':'batch4','dispatch_date':'2018-01-05',"
						+ " 'patient_number':'42087','patient_name':'"+billListResDto.getPatientName()+"', 'location_code':'BURUBURU',"
						+ "'location_name':'Buruburu','scheme_code':'SCH2018','scheme_name':'SAMPLE SCHEME','member_number':'222/04',"
						+ "'visit_number':'"+billListResDto.getVisitNo()+"','visit_start':'2018-01-03T00:00:00Z','visit_end':'2018-01-03T00:00:00Z',"
						+ "'currency':'KES','doctor_name':'DR.Omondi','file_version':'0.2',";
				
				DiagnosisPatientAppointmentMapperDto diagnosisPatientAppointmentMapperDto = new DiagnosisPatientAppointmentMapperDto();
				diagnosisPatientAppointmentMapperDto.setUnitId(unitId);
				diagnosisPatientAppointmentMapperDto.setPatientId(billListResDto.getPatientId());
				diagnosisPatientAppointmentMapperDto.setOrganizationId(organizationId);
				
				List<DiagnosisPatientAppointmentMapperDto> diagnosisPatientAppointmentMapperList = null;
				diagnosisPatientAppointmentMapperList = (List<DiagnosisPatientAppointmentMapperDto>) iPatientDiagnosisService.getPatientDiagnosisByPatientId(diagnosisPatientAppointmentMapperDto);
				JsonStr+="'diagnosis':[";
				ListIterator<DiagnosisPatientAppointmentMapperDto> iteratorDia = diagnosisPatientAppointmentMapperList.listIterator();
				int i = 1;
				while (iteratorDia.hasNext()) {
					DiagnosisPatientAppointmentMapperDto diagnosisPatientDto = iteratorDia.next();
					JsonStr+="{'coding_standard':'"+diagnosisPatientDto.getCode()+"','code':'"+diagnosisPatientDto.getCode()+"',"
							+ "'name':'"+diagnosisPatientDto.getDiagnosisName()+"','is_primary':true,}";
						if(diagnosisPatientAppointmentMapperList.size() > i)
						{
							JsonStr+=",";
						}
					i++;
				}
				JsonStr+="],'pre_authorization':[{'code':'P150','amount':5000,'authorized_by':'Esther','message':'Authorized up to 5000 by Esther'}],"
						+ "'admission':[{'admit_date':'2018-01-03T00:00:00Z','discharge_date':'2018-01-03T00:00:00Z','discharge_summaty':'discharge summary goes here'}],"
						+ "'invoices':[{'amount':'"+billListResDto.getTotalBillAmount()+"','gross_amount':'"+billListResDto.getTotalBillAmount()+"',"
						+ "'invoice_date':'2018-01-03T00:00:00Z','invoice_number':'"+billListResDto.getVisitNo()+"','service_type':'"+serviceType+"',"
						+ "'lines':[";
				
				
				
				
				List<BillDetailsByBillIdDto> detailsByBillIdDtosList = (List<BillDetailsByBillIdDto>) iBillingMasterDao.getBillDetailsByBillId(billListResDto.getBillingMasterId());
				int ser = 1;
				Double seelTotal = 0.00;
				ListIterator<BillDetailsByBillIdDto> iteratorDetails = detailsByBillIdDtosList.listIterator();
				while(iteratorDetails.hasNext())
				{
					
					BillDetailsByBillIdDto detailsByEncounterIdDto = iteratorDetails.next();
					
					seelTotal+=detailsByEncounterIdDto.getSelfPayable();
					//Session session = sessionFactory.getCurrentSession();
					//SQLQuery query = session.createSQLQuery("select device_speciality_desc from billing.claim_interface_device_speciality_mppr where claim_device_id = '1' AND his_speciality_id ='"+detailsByEncounterIdDto.getSpecialityId()+"' limit 1");
					//String deviceSpecialityDesc = query.uniqueResult().toString();
					
					JsonStr+="{'item_code':'"+detailsByEncounterIdDto.getServiceStandardName()+"','item_name':'"+detailsByEncounterIdDto.getServiceStandardName()+"',"
							+ "'service_group':'"+detailsByEncounterIdDto.getSpecialityName()+"','charge_date':'"+detailsByEncounterIdDto.getOrderDateString()+"',"
							+ "'unit_price':'"+detailsByEncounterIdDto.getUnitPrice()+"','quantity':'"+detailsByEncounterIdDto.getQuantity()+"',"
							+ "'gross_amount':'"+detailsByEncounterIdDto.getNetAmt()+"','pre_authorization_code':'P150','payment_reference':["
							+ "{'ref_number':'123'},{'ref_number':'12345'}]}";
					
					if(detailsByBillIdDtosList.size() > ser)
					{
						JsonStr+=",";
					}
					
					ser++;
				}
				JsonStr+="],'payment_modifiers':[{'type':'COPAY','amount':'"+seelTotal+"','ref_number':;123'}]}]}";
				
				
				
				tr.commit();
			}
			
		}catch(Exception e){
			
		}
	}
	*/
	
	@Transactional
	public void claimToSendToSmartCardSystem()
	{
		try{
			
			Date  date = GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(0), "dd-M-yyyy");
			List<SmartCardBillReqDto> billList = iBillingMasterDao.getBillListForSmartCard(date);
			if(billList!=null && billList.size()>0)
			{
			   for (SmartCardBillReqDto smDto : billList) 
			   {
				   SmartCardJson json  = new SmartCardJson();
				   json.setClaim_code(smDto.getClaimCode());
				   json.setPayer_code(smDto.getPayerCode());
				   json.setPayer_name(smDto.getPayerName());
				   json.setAmount(smDto.getAmount());
				   json.setGross_amount(smDto.getGrossAmount());
				   json.setBatch_number(smDto.getBatchNumber());
				   json.setDispatch_date(smDto.getBatchDate());
				   json.setPatient_number(smDto.getPatientNumber());
				   json.setPatient_name(smDto.getPatientName());
				   json.setLocation_code(smDto.getLocationCode());
				   json.setLocation_name(smDto.getLocationName());
				   json.setScheme_code(smDto.getSchemeCode());
				   json.setScheme_name(smDto.getSchemeName());
				   json.setMember_number(smDto.getMemberNumber());
				   json.setVisit_number(smDto.getVisitNo());
				   json.setVisit_start(smDto.getVisitStartDate());
				   json.setVisit_end(smDto.getVisitEndDate());
				   json.setCurrency(smDto.getCurrency());
				   json.setDoctor_name(smDto.getDoctorName());
				   json.setFile_version(smDto.getFileVersion());
				   
				   //GET DIAGNOSIS
				   if(smDto.getVisitTypeId()!=null && smDto.getVisitId()!=null)
				   {
					   List<SmartDiagnosisReqDto> diagnosis = iBillingMasterDao.getDiagnosis(smDto.getVisitTypeId(), smDto.getVisitId());
					   if(diagnosis!=null  && diagnosis.size()>0)
					   {
						   List<DiagnosisJson> dList = new ArrayList<>();
						   for (SmartDiagnosisReqDto dDto : diagnosis) {
							   DiagnosisJson diagJson = new DiagnosisJson();
							   diagJson.setCode(dDto.getCode());
							   diagJson.setCoding_standard(dDto.getCodingStandard());
							   diagJson.setIs_primary(dDto.getIsPrimary());
							   diagJson.setName(dDto.getName());
							   dList.add(diagJson);
						   }
						   json.setDiagnosis(dList);
						}
					   else {
						   json.setDiagnosis(Arrays.asList());
					   }
				   }
				   
					 
				   //GET PREAUTHRIZATION
				   if(smDto.getVisitTypeId()!=null && smDto.getVisitId()!=null)
				   {
				   List<SmartCardPreauthReqDto> preauthReq = iBillingMasterDao.getPreAuthReuest(smDto.getVisitTypeId(), smDto.getVisitId());
				   if(preauthReq!=null  && preauthReq.size()>0)
				    {
					   List<PreauthrizationJson> pList = new ArrayList<>();
					   for (SmartCardPreauthReqDto pDto : preauthReq) {
						   PreauthrizationJson preAuthJson = new PreauthrizationJson();
						   preAuthJson.setAmount(pDto.getAmount());
						   preAuthJson.setCode(pDto.getCode());
						   preAuthJson.setMessage(pDto.getMessage());
						   preAuthJson.setAuthorized_by(pDto.getAuthorizedBy());
						   pList.add(preAuthJson);
					   }
					   json.setPre_authorization(pList);
					 }
				   else 
						   json.setPre_authorization(Arrays.asList());
				   }
				   
				   if(smDto.getVisitTypeId()!=null && smDto.getVisitId()!=null)
				   {
				   //GET ADMISSION
				   if((smDto.getVisitTypeId()==2||smDto.getVisitTypeId()==3||smDto.getVisitTypeId()==4))
				   {
					   SmartAdmitionReqDto adDto = iBillingMasterDao.getAdmissionDetails(smDto.getVisitId());
					   if(adDto!=null)
					   {
						   List<AdmissionJson> list=  new ArrayList<AdmissionJson>();
						   AdmissionJson adJson = new AdmissionJson();
						   adJson.setAdmit_date(adDto.getAdmitDate());
						   adJson.setDischarge_date(adDto.getDischargeDate());	
						   adJson.setDischarge_summary(iBillingMasterDao.getDischargeSummery(smDto.getVisitId()));
						   list.add(adJson);
						   json.setAdmission(list);
					   }
					   else {
						   json.setAdmission(Arrays.asList());
				        }
				    } 
				   }
				   //GET INVOICES
				   List<InvoicesJson> invoices = new ArrayList<>();
				   InvoicesJson inJson = new InvoicesJson();
				   inJson.setAmount(smDto.getAmount());
				   inJson.setGross_amount(smDto.getGrossAmount());
				   inJson.setInvoice_date(smDto.getBillDateTime());
				   inJson.setInvoice_number(smDto.getBatchNumber());
				   inJson.setService_type(smDto.getVisitTypeId()!=null && (smDto.getVisitTypeId()==1)?"outpatient":"inpatient");
				   
				 //GET items
					   List<SmartCardIItemsReqDto> list = iBillingMasterDao.getItemsDetailsByBill(smDto.getBillId(), smDto.getPayeeId());
					   if(list!=null && list.size()>0)
					   {
						   List<ItemsJson> lines = new ArrayList<ItemsJson>();
						   for (SmartCardIItemsReqDto items : list)
						   {
							   ItemsJson itemJson = new ItemsJson();
							   itemJson.setAmount(items.getAmount());
							   itemJson.setCharge_date(items.getChargeDate());
							   itemJson.setGross_amount(items.getGrossAmount());
							   itemJson.setItem_code(items.getItemCode());
							   itemJson.setItem_name(items.getItemName());
							   itemJson.setQuantity(items.getQuantity());
							   itemJson.setUnit_price(items.getUnitPrice());
							   itemJson.setService_group(items.getServiceGroup());
							   itemJson.setPre_authorization_code("");
							   itemJson.setPayment_reference(Arrays.asList());
							   lines.add(itemJson);
						   }
						   inJson.setLines(lines);
					   }
					   
					   
					   List<PaymentRecieptMasterDto> recepit = iBillingMasterDao.getReceiptDetailsbyBill(smDto.getBillId());
					   if(recepit!=null && recepit.size()>0)
					   {
						   List<PaymentModifiersJson> pModifires  = new ArrayList<>();
						   for (PaymentRecieptMasterDto recpt : recepit)
						   {
							   PaymentModifiersJson paymentM = new PaymentModifiersJson();
							   if(recpt.getPayeeId()==1)
							   {
								   paymentM.setAmount(recpt.getAmount());
								   paymentM.setRef_number(recpt.getRecieptNumber());
								   paymentM.setType(recpt.getIdentificationNumber());
								   pModifires.add(paymentM);
							   }
						   }
						   inJson.setPayment_modifiers(pModifires);
					   }
					   else {
						   inJson.setPayment_modifiers(Arrays.asList());
					   }
					   invoices.add(inJson);
					   json.setInvoices(invoices);
					   
					   //print
					   
					   ObjectMapper mapper = new ObjectMapper();
					   String request = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
					   CloseableHttpClient httpClient = HttpClientBuilder.create().build();
					   HttpPost  post = new HttpPost(claimLink);
				       post.addHeader("Content-Type","application/json");
				       post.addHeader("Key ", key);
				       post.addHeader("token", token);
				       post.addHeader("user id", "1875");
				       post.addHeader("globalIdentifier1", "1");
				       post.addHeader("globalIdentifier2", "14056");
				       post.addHeader("globalIdentifier3", "1");
				       post.setEntity(new StringEntity(request));
				    
				       HttpResponse result = httpClient.execute(post);
				       HttpEntity resEntity = result.getEntity(); 
				       iBillingMasterDao.updateSmartCardSubmitStatus(smDto.getBillId(), smDto.isPharamcy());
				   }
				
			   }
			
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}


	/*@Transactional
	@Scheduled(fixedDelay = 120000, initialDelay = 60000)
	public void syncSchedulingLogs() {
		try {
			iPreSterilizationProcessService.updateSterilizationStock();
		} catch (Exception e) {
			
		}
	}*/


	//For unit subscription
	//execute a task at 12:01 AM every day
	@Scheduled(cron = "0 1 0 * * *")
	//@Scheduled(cron = "0 */2 * ? * *")
	@Transactional
	public void subscriptionWorkflow() {
		try {
			System.out.println("subscriptionWorkflow starts");
			 //1.
			iGlobalUnitMasterService.inactivateSubcriptions();
             //2.
			iGlobalUnitMasterService.reminderForSubscription();

			System.out.println("subscriptionWorkflow ends");

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
}

