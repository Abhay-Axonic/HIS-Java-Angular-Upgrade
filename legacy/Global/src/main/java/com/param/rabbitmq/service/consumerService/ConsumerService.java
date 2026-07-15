package com.param.rabbitmq.service.consumerService;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Random;

import javax.transaction.Transactional;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.constants.Status;
import com.param.entity.model.adt.Admission;
import com.param.entity.model.adt.Priority;
import com.param.entity.model.doctor.Doctor;
import com.param.entity.model.master.Frequency;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.RouteOfAdministration;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.StrengthUnit;
import com.param.entity.model.master.User;
import com.param.entity.model.master.VisitType;
import com.param.entity.model.pharmacy.Instruction;
import com.param.entity.model.pharmacy.Prescription;
import com.param.entity.model.pharmacy.PrescriptionDetail;
import com.param.entity.model.pharmacy.PrescriptionFrequencyDetail;
import com.param.rabbitmq.dao.IConsumerDao;
import com.param.rabbitmq.dto.base.Request;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service("ConsumerService")
public class ConsumerService implements IConsumerService {

	@Autowired
	IConsumerDao consumerDao;
	
	@Autowired
	private SessionService sessionService;
	/*@Override
	public Object consume(Request request) {
		
		try {
			
			boolean publish = RabbitMQ.getInstance().consume("HIS.EXCHANGE", "EMR", "EMR");
			if (publish) {
				return "Message successfully consumed from RabbitMQ";
			} else {
				return "Message consume failed";
			}
		} catch (Exception ex) {
			return null;
		}
	}*/
	
	@Override
	@Transactional
	public void keywiseDataRead(String key,JSONObject payload) {
		
		String actionType =  (String) payload.get("actionType");
		JSONObject datajson =  (JSONObject) payload.get("data");
		JSONObject orderItemsjson =  (JSONObject) datajson.get("orderItems");
		
		switch (key) {
		case "PrescriptionEMRKey":
			
			if(actionType.equals("create")) {
				JSONArray jsonList = null;
				if((JSONArray) datajson.get("infusionItems")!=null) {
					if(((JSONArray) datajson.get("infusionItems")).isEmpty() == false) {
						JSONArray infusionItemsJsonList = (JSONArray) datajson.get("infusionItems");
						saveInfusionItems(infusionItemsJsonList);
					}
				}
				else {
					if(((JSONArray) orderItemsjson.get("ipPharmacyItems")).isEmpty() == false) {
						jsonList = (JSONArray) orderItemsjson.get("ipPharmacyItems");
						//System.out.println("\nDirect Reports:"+ jsonList);
						saveIpAndComplexPharmacyItems(datajson, orderItemsjson, jsonList);
					}
					if(((JSONArray) orderItemsjson.get("complexPharmacyItems")).isEmpty() == false) {
						jsonList = (JSONArray) orderItemsjson.get("complexPharmacyItems");
						saveIpAndComplexPharmacyItems(datajson, orderItemsjson, jsonList);
					}
					
				}
			}
			if(actionType.equals("update")) {
				
			}
			break;
			
		case "AssetCategoryMasterHISKey":
			  //System.out.println("key::::::::: "+key);
			  break;	

		default:
			break;
		}
	}

	/*@Override
	public Object consume(Request request) {
		// TODO Auto-generated method stub
		return null;
	}*/
	
	public void saveIpAndComplexPharmacyItems(JSONObject datajson, JSONObject orderItemsjson, JSONArray jsonList) {
	
        Priority priority = new Priority();
        priority.setPriorityId(Integer.parseInt((String) orderItemsjson.get("priority_Id")));
        
        Doctor doctor = new Doctor();
        doctor.setDoctorId(Integer.parseInt((String) datajson.get("HIS_Doctor_ID")));
        
        VisitType VisitType = new VisitType();
        VisitType.setVisitTypeId(Integer.parseInt((String) datajson.get("VisitTypeID")));
        
        com.param.entity.model.master.Status status = new com.param.entity.model.master.Status();
        status.setId(Status.New.getStatus());
        
        Random rand = new Random();
		int num = rand.nextInt(1000);
		User user = new User();
		user.setUserId(doctor.getDoctorId());
		Prescription prescription = new Prescription(doctor.getDoctorId(),new Timestamp(TimeZoneComponent.getDateByzone(0).getTime()),new Admission(88), doctor, 
									(Boolean) datajson.get("isVerified"),false, new Timestamp((Long)datajson.get("orderDate")), 
									"preno"+num, status, VisitType, (String) datajson.get("_id"),priority,
									(Boolean) datajson.get("is_active"),user,user,false, 
									new Date((Long) datajson.get("updated_date")),new Date((Long) datajson.get("added_date")));
		prescription.setCreateDefaults(sessionService.getOrganizationId(), sessionService.getUnitId(), sessionService.getUserId());
		//sessionService.setEntityDefaults(prescription);
		for(int i = 0; i < jsonList.size(); i++) {
			JSONObject json = (JSONObject) jsonList.get(i);
			Item item = new Item();
		    item.setId(((Long) json.get("Drug_HIS_ID")).intValue());
		    Item itemObj = consumerDao.getById(item.getId());
		    Store store = null;
		    if(itemObj.getIsNarcotic()== true) {
		       store = consumerDao.getNarcoticStores();
		    }
		    if(itemObj.getIsNarcotic()== false) {
			   store = consumerDao.getIPStore();
		    }
			String prnInstruction = null;
			if((Boolean) orderItemsjson.get("prn")!=false) {
				prnInstruction = (String) orderItemsjson.get("instruction");
			}
			PrescriptionDetail prescriptionDetail = prescription.addPrescriptionDetails(new PrescriptionDetail(doctor.getDoctorId(),new Timestamp(TimeZoneComponent.getDateByzone(0).getTime()),(Boolean) orderItemsjson.get("isDischargeMedication"), 
					(Boolean) datajson.get("isVerified"), (Boolean) orderItemsjson.get("prn"), (JSONArray) datajson.get("infusionItems")!=null ? jsonList!=null ? true : false : false, 
					prnInstruction, item, status, store, new Instruction(Integer.parseInt((String) json.get("instruction_HIS_id"))),
					(int) Double.parseDouble((String) json.get("quantity")),(Boolean) datajson.get("is_active"),user,user,false,
					new Date((Long) datajson.get("updated_date")),new Date((Long) datajson.get("added_date"))));
			
			prescriptionDetail.addPrescriptionFrequencyDetails(new PrescriptionFrequencyDetail(Double.parseDouble((String) json.get("dosage")),
					(String) json.get("dosage_unit")!=null ? new StrengthUnit(Integer.parseInt((String) json.get("dosage_unit"))) : null, 
					new Frequency(((Long) json.get("Frequency_HIS_ID")).intValue()), ((Boolean) orderItemsjson.get("prn"))!=false ? (String)orderItemsjson.get("instruction") : null, 
					new Date((Long) json.get("startDate")), new Date((Long) json.get("endDate")), new RouteOfAdministration(((Long) json.get("Route_HIS_ID")).intValue()),
					(Boolean) datajson.get("is_active"),false,user,user,new Date((Long) datajson.get("updated_date")),new Date((Long) datajson.get("added_date"))));
			//sessionService.setEntityDefaults(prescriptionDetail);
			prescriptionDetail.setCreateDefaults(sessionService.getOrganizationId(), sessionService.getUnitId(), sessionService.getUserId());
		}
		consumerDao.create(prescription);
		////System.out.println("prescriptionId ======::: "+prescription.getId());
	}
	
	private void saveInfusionItems(JSONArray infusionItemsJsonList) {
		
		for(int i = 0; i < infusionItemsJsonList.size(); i++) {
			JSONObject json = (JSONObject) infusionItemsJsonList.get(i);
			JSONObject orderItemjson =  (JSONObject) json.get("orderItems");
			
			Priority priority = new Priority();
	        priority.setPriorityId(Integer.parseInt((String) orderItemjson.get("priority_Id")));
	        
	        Doctor doctor = new Doctor();
	        doctor.setDoctorId(Integer.parseInt((String) json.get("HIS_Doctor_ID")));
	        
	        VisitType VisitType = new VisitType();
	        VisitType.setVisitTypeId(Integer.parseInt((String) json.get("VisitTypeID")));
	        
	        com.param.entity.model.master.Status status = new com.param.entity.model.master.Status();
	        status.setId(Status.New.getStatus());
	        Random rand = new Random();
			int num = rand.nextInt(1000);
			User user = new User();
			user.setUserId(doctor.getDoctorId());
			
			Prescription prescription = new Prescription(doctor.getDoctorId(),new Timestamp(TimeZoneComponent.getDateByzone(0).getTime()),new Admission(88), doctor, 
										(Boolean) json.get("isVerified"),false, new Timestamp((Long) json.get("orderDate")), 
										"preno"+num, status, VisitType, (String) json.get("_id"), priority,
										(Boolean) json.get("is_active"),user,user,false, 
										new Date((Long) json.get("updated_date")),new Date((Long) json.get("added_date")));
			//sessionService.setEntityDefaults(prescription);
			prescription.setCreateDefaults(sessionService.getOrganizationId(), sessionService.getUnitId(), sessionService.getUserId());
			
			Item item = new Item();
		    item.setId(((Long) orderItemjson.get("Drug_HIS_ID")).intValue());
		    Item itemObj = consumerDao.getById(item.getId());
		    Store store = null;
		    if(itemObj.getIsNarcotic()== true) {
		       store = consumerDao.getNarcoticStores();
		    }
		    if(itemObj.getIsNarcotic()== false) {
			   store = consumerDao.getIPStore();
		    }
		    String prnInstruction = null;
			if((Boolean) orderItemjson.get("prn")!=false) {
				prnInstruction = (String) orderItemjson.get("instruction");
			}
			
			PrescriptionDetail prescriptionDetail = prescription.addPrescriptionDetails(new PrescriptionDetail(doctor.getDoctorId(),new Timestamp(TimeZoneComponent.getDateByzone(0).getTime()),(Boolean) orderItemjson.get("isDischargeMedication"), 
					(Boolean) json.get("isVerified"), (Boolean) orderItemjson.get("prn"), true, 
					prnInstruction, item, status, store, new Instruction(Integer.parseInt((String) orderItemjson.get("instruction_HIS_id"))),
					(int) Double.parseDouble((String) orderItemjson.get("quantity")),(Boolean) json.get("is_active"),user,user,false,   
					new Date((Long) json.get("updated_date")),new Date((Long) json.get("added_date"))));
			
			prescriptionDetail.addPrescriptionFrequencyDetails(new PrescriptionFrequencyDetail(Double.parseDouble((String) orderItemjson.get("dosage")),
					(String) orderItemjson.get("dosage_unit")!=null ? new StrengthUnit(Integer.parseInt((String) orderItemjson.get("dosage_unit"))) : null, 
					new Frequency(((Long) orderItemjson.get("Frequency_HIS_ID")).intValue()), ((Boolean) orderItemjson.get("prn"))!=false ? (String)orderItemjson.get("instruction") : null, 
					new Date((Long) orderItemjson.get("startDate")), new Date((Long) orderItemjson.get("endDate")), new RouteOfAdministration(((Long) orderItemjson.get("Route_HIS_ID")).intValue()),
					(Boolean) json.get("is_active"),false,user,user,new Date((Long) json.get("updated_date")),new Date((Long) json.get("added_date"))));
			prescriptionDetail.setCreateDefaults(sessionService.getOrganizationId(), sessionService.getUnitId(), sessionService.getUserId());
			//sessionService.setEntityDefaults(prescriptionDetail);
			consumerDao.create(prescription);
			////System.out.println("prescriptionId ======::: "+prescription.getId());
		}
	}

	@Override
	public Object consume(Request request) {
		return null;
	}
}
