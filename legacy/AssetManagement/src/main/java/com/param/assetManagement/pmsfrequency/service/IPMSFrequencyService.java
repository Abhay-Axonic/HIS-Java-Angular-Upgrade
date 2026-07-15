package com.param.assetManagement.pmsfrequency.service;

import java.util.List;

import org.springframework.util.MultiValueMap;

import com.param.assetManagement.pmsfrequency.dto.PMSFrequencyDto;
import com.param.entity.model.inventory.PMSFrequency;

public interface IPMSFrequencyService {

	Object save(PMSFrequencyDto request);
	Object update(PMSFrequencyDto request);
	List<PMSFrequencyDto> list(Integer unitId);

}
