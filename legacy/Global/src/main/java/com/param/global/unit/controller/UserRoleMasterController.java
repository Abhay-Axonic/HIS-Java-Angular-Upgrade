package com.param.global.unit.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.DoctorMasterDto;
import com.param.global.unit.dto.ButtonMasterDto;
import com.param.global.unit.dto.LetterAttachmentMapperDto;
import com.param.global.unit.dto.UserRoleMasterDto;
import com.param.global.unit.service.IUserRoleMasterService;



import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
	@RestController
	@RequestMapping("api/global/ ")
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public class UserRoleMasterController implements ICommonConstants{
	final static Logger logger = Logger.getLogger(UserRoleMasterController.class);

		@Autowired
		private IUserRoleMasterService iUserRoleMasterService;

		
		@RequestMapping(value = "/saveUserRoleMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
		public Response saveUserRoleMaster(@RequestBody UserRoleMasterDto userRoleMasterDto) {
			try {
				return iUserRoleMasterService.saveUserRoleMaster(userRoleMasterDto);
			} catch (Exception e) {
			logger.error("saveUserRoleMaster failed", e);
			}
			return new Response(ERROR, null, null, null, null);
		}
		
		
		
		/*@RequestMapping(value = "/saveUserRoleMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response addSample(@RequestBody String sampleMasterDto) {


		Response<UserRoleMasterDto, Integer> response = null;
		try {

		JsonCheck.mapJsonToObject(sampleMasterDto, UserRoleMasterDto.class);
		
		* response = iSampleMasterService.addSampleMaster(sampleMasterDto); return
		* response;
		
		return null;
		} catch (Exception e) {
		
		return new Response(ERROR, null, null, null, null);
		}
		}*/
		@RequestMapping(value = "/updateUserRoleMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
		public Response updateUserRoleMaster(@RequestBody UserRoleMasterDto userRoleMasterDto) {
			try {
				Response res =  iUserRoleMasterService.updateUserRoleMaster(userRoleMasterDto);
				if (res.getObject() != null) {
					return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
				}
			} catch (Exception e) {
			logger.error("updateUserRoleMaster failed", e);
			}
			return new Response(ERROR, null, null, null, null);
		}
		
		@RequestMapping(value = "/updateUserRoleMasterStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
		public Response updateUserRoleMasterStatus(@RequestBody UserRoleMasterDto userRoleMasterDto) {
			try {
				return iUserRoleMasterService.updateUserRoleMasterStatus(userRoleMasterDto);
			} catch (Exception e) {
			logger.error("updateUserRoleMasterStatus failed", e);
			}
			return new Response(ERROR, null, null, null, null);
		}
		
		@RequestMapping(value = "/getUserRoleMasterList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
		public Response getUserRoleMasterList(@RequestBody UserRoleMasterDto userRoleMasterDto) {
			try {
				return iUserRoleMasterService.getUserRoleMasterList(userRoleMasterDto);
			} catch (Exception e) {
			logger.error("getUserRoleMasterList failed", e);
			}
			return new Response(ERROR, null, null, null, null);
		}
		
		
		@RequestMapping(value = "/getUserRoleMasterById/{roleId}/{organisationId}/{unitId}", method = RequestMethod.GET)
		public Response getUserRoleMasterById(@PathVariable ("roleId") Integer roleId,
											  @PathVariable ("organisationId") Integer organisationId,
											  @PathVariable ("unitId") Integer unitId) {
			try {
				return iUserRoleMasterService.getUserRoleMasterById(roleId, organisationId, unitId);
			} catch (Exception e) {
			logger.error("getUserRoleMasterById failed", e);
			}
			return new Response(ERROR, null, null, null, null);
		}
		
		
		@RequestMapping(value = "/getUserRoleMasterCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
		public Response getUserRoleMasterCount(@RequestBody UserRoleMasterDto userRoleMasterDto) {
			try {
				return iUserRoleMasterService.getUserRoleMasterCount(userRoleMasterDto);
			} catch (Exception e) {
			logger.error("getUserRoleMasterCount failed", e);
			}
			return new Response(ERROR, null, null, null, null);
		}


		@RequestMapping(value = "/getActiveUserRoleMasterList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
		public Response getActiveUserRoleMasterList(@RequestBody UserRoleMasterDto userRoleMasterDto) {
			try {
				return iUserRoleMasterService.getActiveUserRoleMasterList(userRoleMasterDto);
			} catch (Exception e) {
			logger.error("getActiveUserRoleMasterList failed", e);
			}
			return new Response(ERROR, null, null, null, null);
		}
		
		 @RequestMapping(value="/searchRoleByName", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
			public @ResponseBody Response searchRoleByName(@RequestBody  UserRoleMasterDto userRoleMasterDto) {
				try {
					return iUserRoleMasterService.searchRoleByName(userRoleMasterDto);
				}catch (Exception e) {
				logger.error("method failed", e);
				}
				return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
			}
		 @RequestMapping(value = "/getAiButtonConfigMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
			public @ResponseBody Response getAiButtonConfigMaster(@RequestBody ButtonMasterDto buttonMasterDto) {
				try {
					return iUserRoleMasterService.getAiButtonConfigMaster(buttonMasterDto);
				} catch (Exception e) {
				logger.error("method failed", e);
				return new Response(ERROR, null, null, null, null);
				}
			}
		 @RequestMapping(value = "/saveButtonMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
			public @ResponseBody Response saveButtonMaster(@RequestBody ButtonMasterDto buttonMasterDto) {
				try {
					return iUserRoleMasterService.saveButtonMaster(buttonMasterDto);
				} catch (Exception e) {
				logger.error("method failed", e);
				return new Response(ERROR, null, null, null, null);
				}
			}
		 @RequestMapping(value = "/getButtonMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
			public @ResponseBody Response getButtonMaster(@RequestBody ButtonMasterDto buttonMasterDto) {
				try {
					return iUserRoleMasterService.getButtonMaster(buttonMasterDto);
				} catch (Exception e) {
				logger.error("method failed", e);
				return new Response(ERROR, null, null, null, null);
				}
			}
		 @RequestMapping(value = "/updateButtonMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
			public @ResponseBody Response updateButtonMaster(@RequestBody ButtonMasterDto buttonMasterDto) {
				try {
					return iUserRoleMasterService.updateButtonMaster(buttonMasterDto);
				} catch (Exception e) {
				logger.error("method failed", e);
				return new Response(ERROR, null, null, null, null);
				}
			}
		 @RequestMapping(value = "/updateButtonMasterStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
			public @ResponseBody Response updateButtonMasterStatus(@RequestBody ButtonMasterDto buttonMasterDto) {
				try {
					return iUserRoleMasterService.updateButtonMasterStatus(buttonMasterDto);
				} catch (Exception e) {
				logger.error("method failed", e);
				return new Response(ERROR, null, null, null, null);
				}
			}
		 @RequestMapping(value = "/getDoctor", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
			public @ResponseBody Response getDoctor(@RequestBody DoctorMasterDto doctorMasterDto) {
				try {
					List<DoctorMasterDto> getList=iUserRoleMasterService.getDoctorMasterByUnitId(doctorMasterDto).getListObject();
					return new Response(SUCCESS, SUCCESS_CODE, null,getList,null);
				} catch (Exception e) {
				logger.error("method failed", e);
				return new Response(ERROR, null, null, null, null);
				}
			}
		 @RequestMapping(value = "/getSpeciality", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
			public @ResponseBody Response getSpeciality(@RequestBody DoctorMasterDto doctorMasterDto) {
				try {
					List<DoctorMasterDto> getList=iUserRoleMasterService.getDoctorMasterByUnitIdForSpeciality(doctorMasterDto).getListObject();
					return new Response(SUCCESS, SUCCESS_CODE, null,getList,null);
				} catch (Exception e) {
				logger.error("method failed", e);
				return new Response(ERROR, null, null, null, null);
				}
			}
		 @RequestMapping(value = "/getAiButtonConfigMasterForDocAndSpeciality", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
			public @ResponseBody Response getAiButtonConfigMasterForDocAndSpeciality(@RequestBody ButtonMasterDto buttonMasterDto) {
				try {
					return iUserRoleMasterService.getAiButtonConfigMasterForDocAndSpeciality(buttonMasterDto);
				} catch (Exception e) {
				logger.error("method failed", e);
				return new Response(ERROR, null, null, null, null);
				}
			}
		 @RequestMapping(value = "/saveConfig", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
			public @ResponseBody Response saveConfig(@RequestBody ButtonMasterDto buttonMasterDto) {
				try {
					return iUserRoleMasterService.saveConfig(buttonMasterDto);
				} catch (Exception e) {
				logger.error("method failed", e);
				return new Response(ERROR, null, null, null, null);
				}
			}
		 @RequestMapping(value = "/getSavedConfig", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
			public @ResponseBody Response getSavedConfig(@RequestBody ButtonMasterDto buttonMasterDto) {
				try {
					return iUserRoleMasterService.getSavedConfig(buttonMasterDto);
				} catch (Exception e) {
				logger.error("method failed", e);
				return new Response(ERROR, ERROR_CODE, null, null,null);
				}
			}
		 @RequestMapping(value = "/getActiveButtonMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
			public @ResponseBody Response getActiveButtonMaster(@RequestBody ButtonMasterDto buttonMasterDto) {
				try {
					return iUserRoleMasterService.getActiveButtonMaster(buttonMasterDto);
				} catch (Exception e) {
				logger.error("method failed", e);
				return new Response(ERROR, ERROR_CODE, null, null,null);
				}
			}
		 @RequestMapping(value = "/saveLetterAttachment" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
			public @ResponseBody Response saveLetterAttachment(@RequestBody LetterAttachmentMapperDto letterattachmentMapperDto){
				try{
					return iUserRoleMasterService.saveLetterAttachment(letterattachmentMapperDto);
				}catch(Exception e){
				logger.error("method failed", e);
				return new Response(ERROR, ERROR_CODE, null, null,null);
				}
			}

		    @RequestMapping(value = "/updateButtonMasterConfigStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
		    public @ResponseBody Response updateButtonMasterConfigStatus(@RequestBody ButtonMasterDto buttonMasterDto) {
		        try {
		            return iUserRoleMasterService.updateButtonMasterConfigStatus(buttonMasterDto);
		        } catch (Exception e) {
		        logger.error("method failed", e);
		        	return new Response(ERROR, ERROR_CODE, null, null,null);
		        }
		    }

}
