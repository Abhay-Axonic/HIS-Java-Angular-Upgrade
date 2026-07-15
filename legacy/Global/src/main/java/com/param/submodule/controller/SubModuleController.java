package com.param.submodule.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.QueryParam;

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
import com.param.submodule.service.ISubModuleService;

import com.param.global.common.ServiceException;
@Controller
@RequestMapping("/submodule")
public class SubModuleController implements ICommonConstants {
	final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(SubModuleController.class);


	@Autowired
	private ISubModuleService subModuleService;

	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody Response getSubModuleList(@PathVariable("moduleID") Integer moduleId ) {
		try {
			return subModuleService.getAllSubModulesByModuleId(moduleId);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	/*@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody Response getAllSubModuleList() {
		try {
			return subModuleService.getAllSubModule();
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}*/
	
	/*
	 * This method returns the List of submodules on the basis of module name.
	 */
	
	/*@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAllSubModulesByModuleName(@QueryParam("moduleName") String moduleName) throws JSONException {
		Logger.log(this.getClass().getName()+" : INFO : In - getAllSubModulesByModuleName() ");
		List<ActionMaster> actionMasterList = null;
		Map<String,Object> responseObject;
		Map<String,Object> subModuleRecord;
		List<Map<String,Object>> subModuleRecordList=new ArrayList<>();
		try {
			actionMasterList = subModuleService.getAllSubModulesByModuleName(moduleName);
			for (ActionMaster actionMaster : actionMasterList) {
				subModuleRecord=new HashMap<>();
				subModuleRecord.put("subModuleId", actionMaster.getAction_id());
				subModuleRecord.put("subModuleName", actionMaster.getAction_name());
				subModuleRecordList.add(subModuleRecord);
			}
			
			responseObject=new HashMap<>();
			responseObject.put("status", "success");
			responseObject.put("subModules", subModuleRecordList);
		} catch (Exception e) {
			Logger.log(this.getClass().getName()+" : ERROR : "+e.toString());
			responseObject = new HashMap<>();
			responseObject.put("status", "fail");
			responseObject.put("error", e.toString());
			return new ResponseEntity<>(responseObject, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		Logger.log(this.getClass().getName()+" : INFO : Out - getAllSubModulesByModuleName() ");
		return new ResponseEntity<>(responseObject, HttpStatus.OK);
	}*/
	
	/*
	 * This method returns the List of submodules on the basis of module id.
	 */
	/*@RequestMapping(value="{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAllSubModulesByModuleId(@PathVariable("id") int id) throws JSONException {
		Logger.log(this.getClass().getName()+" : INFO : In - getAllSubModulesByModuleId() ");
		List<ActionMaster> actionMasterList = null;
		Map<String,Object> responseObject;
		Map<String,Object> subModuleRecord;
		List<Map<String,Object>> subModuleRecordList=new ArrayList<>();
		try {
			actionMasterList = subModuleService.getAllSubModulesByModuleId(id);
			for (ActionMaster actionMaster : actionMasterList) {
				subModuleRecord=new HashMap<>();
				subModuleRecord.put("subModuleId", actionMaster.getAction_id());
				subModuleRecord.put("subModuleName", actionMaster.getAction_name());
				subModuleRecordList.add(subModuleRecord);
			}
			
			responseObject=new HashMap<>();
			responseObject.put("status", "success");
			responseObject.put("subModules", subModuleRecordList);
		} catch (Exception e) {
			Logger.log(this.getClass().getName()+" : ERROR : "+e.toString());
			responseObject = new HashMap<>();
			responseObject.put("status", "fail");
			responseObject.put("error", e.toString());
			return new ResponseEntity<>(responseObject, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		Logger.log(this.getClass().getName()+" : INFO : Out - getAllSubModulesByModuleId() ");
		return new ResponseEntity<>(responseObject, HttpStatus.OK);
	}
	*/
	
	/*
	 * This method returns the List of all submodules.
	 */
	/*@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAllSubModules() throws JSONException {
		Logger.log(this.getClass().getName()+" : INFO : In - getAllSubModules() ");
		List<ActionMaster> actionMasterList = null;
		Map<String,Object> responseObject;
		Map<String,Object> subModuleRecord;
		List<Map<String,Object>> subModuleRecordList=new ArrayList<>();
		try {
			actionMasterList = subModuleService.getAllSubModules();
			for (ActionMaster actionMaster : actionMasterList) {
				subModuleRecord=new HashMap<>();
				subModuleRecord.put("subModuleId", actionMaster.getAction_id());
				subModuleRecord.put("subModuleName", actionMaster.getAction_name());
				subModuleRecordList.add(subModuleRecord);
			}
			
			responseObject=new HashMap<>();
			responseObject.put("status", "success");
			responseObject.put("subModules", subModuleRecordList);
		} catch (Exception e) {
			Logger.log(this.getClass().getName()+" : ERROR : "+e.toString());
			responseObject = new HashMap<>();
			responseObject.put("status", "fail");
			responseObject.put("error", e.toString());
			return new ResponseEntity<>(responseObject, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		Logger.log(this.getClass().getName()+" : INFO : Out - getAllSubModules() ");
		return new ResponseEntity<>(responseObject, HttpStatus.OK);
	}*/
}