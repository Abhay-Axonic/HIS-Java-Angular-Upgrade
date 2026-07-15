package com.param.screen.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.param.actionmaster.model.ActionMaster;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.privilege.Logger.Logger;
import com.param.screen.service.IScreenService;

import com.param.global.common.ServiceException;
@Controller
@RequestMapping("/screen")
public class ScreenController implements ICommonConstants {
	final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ScreenController.class);


	@Autowired
	private IScreenService screenService;

	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody Response getModuleList(@PathVariable("subModuleoduleID") Integer subModuleId ) {
		try {
			return screenService.getAllScreens(subModuleId);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	
	
	/*
	 * This method returns the List of Screens.
	 */
	/*@RequestMapping(value="{id}",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAllScreens(@PathVariable("id") int id) throws JSONException {
		Logger.log(this.getClass().getName()+" : INFO : In - getAllScreens() ");
		Map<String, Object> responseObject;
		Map<String, Object> screenRecord;
		List<Map<String, Object>> screenRecordList=new ArrayList<>();
		List<ActionMaster> actionMasterList = null;
		try {
			actionMasterList = screenService.getAllScreens(id);
			for (ActionMaster actionMaster : actionMasterList) {
				screenRecord=new HashMap<>();
				screenRecord.put("screenId", actionMaster.getAction_id());
				screenRecord.put("screenName", actionMaster.getAction_name());
				screenRecordList.add(screenRecord);				
			}
			responseObject=new HashMap<>();
			responseObject.put("status", "success");
			responseObject.put("screens", screenRecordList);
		} catch (Exception e) {
			Logger.log(this.getClass().getName()+" : ERROR : "+e.toString());
			responseObject = new HashMap<>();
			responseObject.put("status", "fail");
			responseObject.put("error", e);
			return new ResponseEntity<>(responseObject, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		Logger.log(this.getClass().getName()+" : INFO : Out - getAllScreens() ");
		return new ResponseEntity<>(responseObject, HttpStatus.OK);
	}*/
}