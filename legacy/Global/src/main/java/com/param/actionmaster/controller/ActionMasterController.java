package com.param.actionmaster.controller;

import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.param.actionmaster.model.ActionMaster;
import com.param.actionmaster.service.ActionMasterService;
import com.param.privilege.Logger.Logger;

import com.param.global.common.ServiceException;
@Controller
@RequestMapping("/actionmaster")
public class ActionMasterController {
	final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ActionMasterController.class);


	@Autowired
	private ActionMasterService actionMasterService;

	/*
	 * This method creates a new privilege.
	 */
	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> addActionMaster(HttpEntity<String> httpEntity) throws ParseException {
		Logger.log(this.getClass().getName()+" : INFO : In - addActionMaster() ");
		String json = httpEntity.getBody();
        
		try {
			JsonReader jsonReader = Json.createReader(new StringReader(json));
			JsonObject reader = jsonReader.readObject();
			JsonArray actionMasterJsonArray = reader.getJsonArray("action_master");

			for (int i = 0; i < actionMasterJsonArray.size(); i++) {

				JsonObject actionMasterJson = actionMasterJsonArray.getJsonObject(i);

				int action_id = actionMasterJson.getInt("action_id");
				String action_name = actionMasterJson.getString("action_name");
				String action_link = actionMasterJson.getString("action_link");				
				int parent_id = actionMasterJson.getInt("parent_id");
				int created_by = actionMasterJson.getInt("created_by");
				java.util.Date created_date = null;
				char[] statusArray = actionMasterJson.getString("status").toCharArray();
				char status = statusArray[0];
				ActionMaster actionMaster = new ActionMaster(action_id, action_name, action_link,parent_id, created_by,created_date, status);
				//System.out.println(actionMaster.toString());
				actionMasterService.addActionMaster(actionMaster);
			}

		} catch (Exception e) {
		logger.error("addActionMaster failed", e);
		}
		Logger.log(this.getClass().getName()+" : INFO : Out - addActionMaster() ");
		return new ResponseEntity<>("succes", HttpStatus.CREATED);
	}

}