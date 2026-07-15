package com.param.module.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.module.service.IModuleService;
import com.param.submodule.service.ISubModuleService;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Controller
@RequestMapping("/module")
public class ModuleController implements ICommonConstants {
	final static Logger logger = Logger.getLogger(ModuleController.class);


	@Autowired
	private IModuleService moduleService;
	
	/*@Autowired
	private ISubModuleService subModuleService;*/

	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody Response getModuleList() {
		try {
			return moduleService.getAllModules();
		} catch (Exception e) {
			logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/getZeroParentModule", method = RequestMethod.GET)
	public @ResponseBody Response getZeroParentModule() {
		try {
			return moduleService.getZeroParentModule();
		} catch (Exception e) {
			throw e;
		}
//		return new Response(ERROR, null, null, null, null);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/{orgId}", method = RequestMethod.GET)
	public @ResponseBody Response getModuleList(@PathVariable("orgId") int orgId) {
		try {
			return moduleService.getAllModulesByOrgId(orgId);
		} catch (Exception e) {
			logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/biModules",method = RequestMethod.GET)
	public @ResponseBody Response getBIModuleList() {
		try {
			return moduleService.getBIModuleList();
		} catch (Exception e) {
			logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
/*	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody Response getModuleList() {
		try {
			return moduleService.getAllModules();
		} catch (Exception e) {
			
		}
	}*/
	
	
	/*@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody Response getTreeView() {
		try {
			return moduleService.getTreeView();
		} catch (Exception e) {
			
		}
	}*/
	
	
	
	/*
	 * This method returns the List of Modules.
	 */
	/*@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAllModules() throws JSONException {
		Logger.log(this.getClass().getName()+" : INFO : In - getAllModules() ");
		Map<String, Object> responseObject;
		Map<String, Object> moduleRecord;
		List<Map<String, Object>> moduleRecordList=new ArrayList<>();
		List<ActionMaster> actionMasterList = null;
		try {
			actionMasterList = moduleService.getAllModules();
			for (ActionMaster actionMaster : actionMasterList) {
				moduleRecord=new HashMap<>();
				moduleRecord.put("moduleId", actionMaster.getAction_id());
				moduleRecord.put("moduleName", actionMaster.getAction_name());
				moduleRecordList.add(moduleRecord);				
			}
			responseObject=new HashMap<>();
			responseObject.put("status", "success");
			responseObject.put("modules", moduleRecordList);
		} catch (Exception e) {
			Logger.log(this.getClass().getName()+" : ERROR : "+e.toString());
			responseObject = new HashMap<>();
			responseObject.put("status", "fail");
			responseObject.put("error", e);
			return new ResponseEntity<>(responseObject, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		Logger.log(this.getClass().getName()+" : INFO : Out - getAllModules() ");
		return new ResponseEntity<>(responseObject, HttpStatus.OK);
	}*/
	
	
	/*@RequestMapping(value="/treeview",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getTreeView() throws JSONException {
		Logger.log(this.getClass().getName()+" : INFO : In - getTreeView() ");
		Map<String, Object> responseObject;
		Map<String, Object> moduleRecord;
		List<Map<String, Object>> moduleRecordList=new ArrayList<>();
		List<ActionMaster> actionMasterList = null;
		try {
			actionMasterList = moduleService.getAllModules();
			for (ActionMaster actionMaster : actionMasterList) {
				moduleRecord=new HashMap<>();
				moduleRecord.put("moduleId", actionMaster.getAction_id());
				moduleRecord.put("moduleName", actionMaster.getAction_name());
				moduleRecord.put("children", subModuleService.getAllSubModulesByModuleId(actionMaster.getAction_id()));
				moduleRecordList.add(moduleRecord);				
			}
			
			
			responseObject=new HashMap<>();
			responseObject.put("status", "success");
			responseObject.put("modules", moduleRecordList);
		} catch (Exception e) {
			Logger.log(this.getClass().getName()+" : ERROR : "+e.toString());
			responseObject = new HashMap<>();
			responseObject.put("status", "fail");
			responseObject.put("error", e);
			return new ResponseEntity<>(responseObject, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		Logger.log(this.getClass().getName()+" : INFO : Out - getTreeView() ");
		return new ResponseEntity<>(responseObject, HttpStatus.OK);
	}*/
	
	
	
	
}