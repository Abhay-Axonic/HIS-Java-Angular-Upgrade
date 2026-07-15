package com.param.global.unit.service;

import java.util.ArrayList;
import java.util.List;

import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.DoctorMasterDto;
import com.param.global.model.UserRoleMaster;
import com.param.global.unit.dao.IUserRoleMasterDao;
import com.param.global.unit.dao.IVaccinationRoleMapperDao;
import com.param.global.unit.dao.VaccinationRoleMapperDaoImp;
import com.param.global.unit.dto.ButtonMasterDto;
import com.param.global.unit.dto.LetterAttachmentMapperDto;
import com.param.global.unit.dto.UserRoleMasterDto;
import com.param.global.unit.dto.VaccinationRoleMapperDto;
import com.param.otims.model.OperationTableType;
import com.param.rabbitmq.dto.base.Payload;
import com.param.rabbitmq.dto.base.Request;
import com.param.rabbitmq.service.OtimsService.IOtimsService;
import com.param.rabbitmq.util.constants.MasterKeyConstants;
import org.springframework.transaction.annotation.Transactional;

@Service
@SuppressWarnings({ "rawtypes" })
public class UserRoleMasterServiceImpl implements IUserRoleMasterService, ICommonConstants {

	private static final Logger logger = Logger.getLogger(UserRoleMasterServiceImpl.class);

	@Autowired
	IUserRoleMasterDao iUserRoleMasterDao;

	@Autowired
	private IOtimsService iOtimsService;

	@Autowired
	private IVaccinationRoleMapperDao iVaccinationRoleMapperDao;

	@Override
	public Response saveUserRoleMaster(UserRoleMasterDto userRoleMasterDto) throws ApplicationException {
		try {
			Response response = iUserRoleMasterDao.getUserRoleMasterByName(userRoleMasterDto);

			if (response.getListObject().size() > 0 && response.getListObject() != null) {

				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			} else {

				Response res = iUserRoleMasterDao.saveUserRoleMaster(userRoleMasterDto);

				if (res.getObject() != null) {

					Integer roleId = (Integer) res.getObject();
					userRoleMasterDto.setRoleId(roleId);
					Request request = new Request();
					Payload payload = new Payload();
					payload.setActionType("create");
					payload.setData(userRoleMasterDto);
					request.setKey(MasterKeyConstants.StaffRole.toString());
					request.setPayload(payload);
					// iOtimsService.publish(request);
					return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
				}
			}
		} catch (Exception e) {
			logger.error("saveUserRoleMaster failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	@Transactional
	public Response updateUserRoleMaster(UserRoleMasterDto userRoleMasterDto) throws ApplicationException {
		try {
			Response response = iUserRoleMasterDao.getUserRoleMasterByNameNotId(userRoleMasterDto);

			if (response.getListObject().size() > 0 && response.getListObject() != null) {

				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			} else {

				VaccinationRoleMapperDto vaccinationRoleMapperDto = new VaccinationRoleMapperDto();
				vaccinationRoleMapperDto.setUnitId(userRoleMasterDto.getUnitId());
				vaccinationRoleMapperDto.setOrganizationId(userRoleMasterDto.getOrganisationId());
				vaccinationRoleMapperDto.setRoleId(userRoleMasterDto.getRoleId());
				Response response1 = iVaccinationRoleMapperDao
						.getVaccinationRoleMapperByRoleId(vaccinationRoleMapperDto);

				List<Integer> vacUserMapper = new ArrayList<>();
				if (response1.getListObject() != null && response1.getListObject().size() > 0) {
					List<VaccinationRoleMapperDto> list = response1.getListObject();
					for (VaccinationRoleMapperDto vaccinationRoleMapperDto2 : list) {
						vacUserMapper.add(vaccinationRoleMapperDto2.getVaccinationRoleMapperId());

					}

				}

				iVaccinationRoleMapperDao.inactiveVaccinationByRoleId(vacUserMapper);

				Response res = iUserRoleMasterDao.updateUserRoleMaster(userRoleMasterDto);

				if (res.getObject() != null) {
					return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, res);
				}

			}

		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateUserRoleMasterStatus(UserRoleMasterDto userRoleMasterDto) throws ApplicationException {
		try {
			return iUserRoleMasterDao.updateUserRoleMasterStatus(userRoleMasterDto);
		} catch (Exception e) {
			logger.error("updateUserRoleMasterStatus failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getUserRoleMasterList(UserRoleMasterDto userRoleMasterDto) throws ApplicationException {
		try {
			return iUserRoleMasterDao.getUserRoleMasterList(userRoleMasterDto);
		} catch (Exception e) {
			logger.error("getUserRoleMasterList failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response getUserRoleMasterById(Integer roleId, Integer organisationId, Integer unitId)
			throws ApplicationException {
		try {
			return iUserRoleMasterDao.getUserRoleMasterById(roleId, organisationId, unitId);
		} catch (Exception e) {
			logger.error("getUserRoleMasterById failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getActiveUserRoleMasterList(UserRoleMasterDto userRoleMasterDto) throws ApplicationException {
		try {
			return iUserRoleMasterDao.getActiveUserRoleMasterList(userRoleMasterDto);
		} catch (Exception e) {
			logger.error("getActiveUserRoleMasterList failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getUserRoleMasterCount(UserRoleMasterDto userRoleMasterDto) throws ApplicationException {
		try {
			return iUserRoleMasterDao.getUserRoleMasterCount(userRoleMasterDto);
		} catch (Exception e) {
			logger.error("getUserRoleMasterCount failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getUserRoleMasterByName(UserRoleMasterDto userRoleMasterDto) throws ApplicationException {
		try {
			return iUserRoleMasterDao.getUserRoleMasterByName(userRoleMasterDto);
		} catch (Exception e) {
			logger.error("getUserRoleMasterByName failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getUserRoleMasterByNameNotId(UserRoleMasterDto userRoleMasterDto) throws ApplicationException {
		try {
			return iUserRoleMasterDao.getUserRoleMasterByNameNotId(userRoleMasterDto);
		} catch (Exception e) {
			logger.error("getUserRoleMasterByNameNotId failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response searchRoleByName(UserRoleMasterDto userRoleMasterDto) throws ApplicationException {
		try {
			return iUserRoleMasterDao.searchRoleByName(userRoleMasterDto);
		} catch (Exception e) {
			logger.error("searchRoleByName failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	@Transactional
	public Response getAiButtonConfigMaster(ButtonMasterDto buttonMasterDto) {
		return iUserRoleMasterDao.getAiButtonConfigMaster(buttonMasterDto);
	}

	@Override
	@Transactional
	public Response saveButtonMaster(ButtonMasterDto buttonMasterDto) {
		try {
			return iUserRoleMasterDao.saveButtonMaster(buttonMasterDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	}

	@Override
	@Transactional
	public Response getButtonMaster(ButtonMasterDto buttonMasterDto) {
		try {
			return iUserRoleMasterDao.getButtonMaster(buttonMasterDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	}

	@Override
	@Transactional
	public Response updateButtonMaster(ButtonMasterDto buttonMasterDto) {
		try {
			return iUserRoleMasterDao.updateButtonMaster(buttonMasterDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	}

	@Override
	@Transactional
	public Response updateButtonMasterStatus(ButtonMasterDto buttonMasterDto) {
		try {
			return iUserRoleMasterDao.updateButtonMasterStatus(buttonMasterDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	}

	@Override
	@Transactional
	public Response getDoctorMasterByUnitId(DoctorMasterDto doctorMasterDto) {
		return iUserRoleMasterDao.getDoctorMasterByUnitId(doctorMasterDto);
	}

	@Override
	@Transactional
	public Response getDoctorMasterByUnitIdForSpeciality(DoctorMasterDto doctorMasterDto) {
		return iUserRoleMasterDao.getDoctorMasterByUnitIdForSpeciality(doctorMasterDto);
	}

	@Override
	@Transactional
	public Response getAiButtonConfigMasterForDocAndSpeciality(ButtonMasterDto buttonMasterDto) {
		return iUserRoleMasterDao.getAiButtonConfigMasterForDocAndSpeciality(buttonMasterDto);
	}

	@Override
	@Transactional
	public Response saveConfig(ButtonMasterDto buttonMasterDto) {
		return iUserRoleMasterDao.saveConfig(buttonMasterDto);
	}

	@Override
	@Transactional
	public Response getSavedConfig(ButtonMasterDto buttonMasterDto) {
		return iUserRoleMasterDao.getSavedConfig(buttonMasterDto);
	}

	@Override
	@Transactional
	public Response getActiveButtonMaster(ButtonMasterDto buttonMasterDto) {
		try {
			return iUserRoleMasterDao.getActiveButtonMaster(buttonMasterDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	}

	@Override
	@Transactional
	public Response saveLetterAttachment(LetterAttachmentMapperDto letterattachmentMapperDto) {
		try{
			return iUserRoleMasterDao.saveLetterAttachment(letterattachmentMapperDto);
		}catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
	}


	@Override
	@Transactional
	public Response updateButtonMasterConfigStatus(ButtonMasterDto buttonMasterDto) {
        try {
            return iUserRoleMasterDao.updateButtonMasterConfigStatus(buttonMasterDto);
        } catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	}

	@Override
	@Transactional
	public Response getActiveSavedConfig(ButtonMasterDto buttonMasterDto) {
		 try {
	            return iUserRoleMasterDao.getActiveSavedConfig(buttonMasterDto);
	        } catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	}

	@Override
	@Transactional
	public Response getSingleButtonById(ButtonMasterDto buttonMasterDto) {
		try {
			List<Integer> tabIds = new ArrayList<>();
			tabIds.add(44);
			tabIds.add(45);
			buttonMasterDto.setTabIds(tabIds);
			List<ButtonMasterDto> list = new ArrayList<>();
			for (Integer tabId : buttonMasterDto.getTabIds()) {
				buttonMasterDto.setTabId(tabId);
				ButtonMasterDto result = (ButtonMasterDto) iUserRoleMasterDao.getSingleButtonById(buttonMasterDto).getObject();
				if(result!=null) {
					list.add(result);
				}
			}
			return new Response(SUCCESS, null, null, list,null);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	}


}
