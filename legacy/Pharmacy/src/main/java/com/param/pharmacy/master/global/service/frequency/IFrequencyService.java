package com.param.pharmacy.master.global.service.frequency;

import java.util.List;

import com.param.common.dto.CommonResponse;
import com.param.global.common.Response;
import com.param.pharmacy.master.global.dto.frequency.FrequencyDetailsResponse;
import com.param.pharmacy.master.global.dto.frequency.FrequencyRequest;
import com.param.pharmacy.master.global.dto.frequency.GetFrequencyResponse;

public interface IFrequencyService {

	CommonResponse saveFrequency(FrequencyRequest request);

	List<GetFrequencyResponse> getAll(Integer orgId);

	Object updateFrequency(FrequencyRequest request);

	FrequencyDetailsResponse getById(Integer id);
	

    Response getConvertedLanguageListById(FrequencyRequest frequencyRequest);

//	List<FrequencyResponseList> getDropdownList();


}
