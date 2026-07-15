package com.param.pharmacy.master.unit.service.configuration;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.constants.Formats;
import com.param.common.constants.Resets;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.master.PrefixConfiguration;
import com.param.entity.model.master.Screen;
import com.param.global.common.ServiceException;
import com.param.global.dao.global.IGlobalDao;
import com.param.pharmacy.master.unit.dao.configuration.IPrefixConfigurationDao;
import com.param.pharmacy.master.unit.dto.configuration.FormatsDto;
import com.param.pharmacy.master.unit.dto.configuration.PrefixConfigurationDropdown;
import com.param.pharmacy.master.unit.dto.configuration.PrefixConfigurationDto;
import com.param.pharmacy.master.unit.dto.configuration.ResetDto;
import com.param.service.global.SessionService;

@Service("PrefixConfigurationService")
public class PrefixConfigurationService implements IPrefixConfigurationService {

	private static final Logger logger = Logger.getLogger(PrefixConfigurationService.class);

	@Autowired
	IPrefixConfigurationDao PrefixConfigurationDao;

	@Autowired
	IGlobalDao GlobalDao;
	
	@Autowired
	private SessionService sessionService;

	@Override
	@Transactional
	public PrefixConfigurationDropdown getDropdownList() {
		PrefixConfigurationDropdown response = new PrefixConfigurationDropdown();

		for (Screen screen : GlobalDao.getScreens()) {
			response.addScreen(
					new com.param.pharmacy.master.unit.dto.configuration.ScreenDto(screen.getId(), screen.getName() ,screen.getIsGlobal()));
		}
		List<ResetDto> resetDtoList = new ArrayList<ResetDto>();
		for (Resets resets : EnumSet.allOf(Resets.class)) {
			ResetDto resetDto = new ResetDto(resets.getId(), resets.getResets());
			resetDtoList.add(resetDto);
		}
		response.setReset(resetDtoList);

		List<FormatsDto> formatsDtos = new ArrayList<FormatsDto>();
		for (Formats formats : EnumSet.allOf(Formats.class)) {
			FormatsDto formatsDto = new FormatsDto(formats.getId(), formats.getFormats(), formats.getCode());
			/*
			 * if(formats.getId() == 9) { Store store =
			 * GlobalDao.getStore(com.param.common.constants.Constants.getStoreId()); Random
			 * random = new Random(); formatsDto = new FormatsDto(formats.getId(),
			 * "ST"+store.getStoreCode()+random.nextInt()); }
			 */
			formatsDtos.add(formatsDto);
		}
		response.setFormat(formatsDtos);
		return response;
	}

	@Override
	@Transactional
	public Object savePrefixConfiguration(PrefixConfigurationDto request) {
		try {
			CommonResponse response = new CommonResponse();
			PrefixConfiguration res  = PrefixConfigurationDao.getByScreenId(request);
			
			if(res!=null && res.getId()!=null && res.getId()>0)
			{
				response.setMessage("Prefix Configuration Already Exist");
				response.setCode("409");
			}else{
				PrefixConfiguration prefixConfiguration = new PrefixConfiguration(new Screen(request.getScreenId()),
						request.getResetId(), request.getRunningNumber(), request.getLength(), request.getGeneratedCode());
				
				
				//sessionService.setEntityDefaults(prefixConfiguration);
				prefixConfiguration.setCreateDefaults(request.getOrganizationId()!=null ? request.getOrganizationId() : sessionService.getOrganizationId(),request.getUnitId() !=null ? request.getUnitId() :sessionService.getUnitId() ,request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());

				String string = request.getGeneratedCode();

				response.setId(PrefixConfigurationDao.create(prefixConfiguration).longValue());
				response.setMessage("Prefix Configuration Inserted Successfully");
				response.setCode("200");
			}

			return response;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Object update(PrefixConfigurationDto prefixConfigurationDto) {
		PrefixConfiguration prefixConfiguration = PrefixConfigurationDao.getById(prefixConfigurationDto.getId());

		prefixConfiguration.updatePrefixConfiguration(
				(prefixConfigurationDto.getRunningNumber() != null && prefixConfigurationDto.getRunningNumber() > 0)
						? prefixConfigurationDto.getRunningNumber()
						: 0,
				(prefixConfigurationDto.getResetId() != null && prefixConfigurationDto.getResetId() > 0)
						? prefixConfigurationDto.getResetId()
						: 0,
				(prefixConfigurationDto.getLength() != null && prefixConfigurationDto.getLength() > 0)
						? prefixConfigurationDto.getLength()
						: 0,
				(prefixConfigurationDto.getGeneratedCode() != null && prefixConfigurationDto.getGeneratedCode() != "")
						? prefixConfigurationDto.getGeneratedCode()
						: null);
		prefixConfiguration.setUpdateDefaults(prefixConfigurationDto.getUserId()!=null ? prefixConfigurationDto.getUserId() : sessionService.getUserId(),prefixConfiguration.getUnitId());
		CommonResponse response = new CommonResponse();
		response.setId(PrefixConfigurationDao.updatePrefixConfiguration(prefixConfiguration).longValue());
		response.setMessage("Prefix Configuration updated successfully");
		return response;
	}

	@Override
	@Transactional
	public PrefixConfigurationDto getById(Integer id) {
		PrefixConfiguration prefixConfiguration = PrefixConfigurationDao.getById(id);
		PrefixConfigurationDto response = new PrefixConfigurationDto(prefixConfiguration.getId(),
				prefixConfiguration.getScreen().getId(), prefixConfiguration.getResetId(),
				Resets.getById(prefixConfiguration.getResetId()), prefixConfiguration.getRunningNumber(),
				prefixConfiguration.getLength(), prefixConfiguration.getGeneratedCode());
		return response;
	}

	@Override
	@Transactional
	public List<PrefixConfigurationDto> getAll(Boolean flag,Integer orgId, Integer unitId ) {
		List<PrefixConfigurationDto> list = new ArrayList<PrefixConfigurationDto>();

		for (PrefixConfiguration prefixConfiguration : PrefixConfigurationDao.getAll(flag,orgId,unitId)) {
			PrefixConfigurationDto response = new PrefixConfigurationDto(prefixConfiguration.getId(),
					prefixConfiguration.getScreen().getId(), prefixConfiguration.getScreen().getName(),
					prefixConfiguration.getResetId(), Resets.getById(prefixConfiguration.getResetId()),
					prefixConfiguration.getRunningNumber(), prefixConfiguration.getLength(),
					prefixConfiguration.getGeneratedCode());
			list.add(response);
		}
		return list;
	}

}
