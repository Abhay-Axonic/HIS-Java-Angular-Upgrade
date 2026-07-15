package com.param.pharmacy.master.global.dao.frequency;

import java.util.List;

import com.param.entity.model.master.Frequency;
import com.param.entity.model.master.FrequencyDetail;
import com.param.global.common.Response;
import com.param.global.dto.global.TranslatedFrequencyMasterDto;
import com.param.global.dto.global.TranslatedInstructionMasterDto;
import com.param.pharmacy.master.global.dto.frequency.FrequencyDetailsResponse;
import com.param.pharmacy.master.global.dto.frequency.FrequencyRequest;
import com.param.pharmacy.master.global.dto.frequency.FrequencyRequestItem;
import com.param.pharmacy.master.global.dto.frequency.GetFrequencyListResponse;

public interface IFrequencyDao {

	Frequency saveFrequency(Frequency frequency);

	List<Frequency> getAll(Integer orgId);

	Frequency getFrequency(Integer id);

	List<FrequencyDetail> getFrequencyDetail(Integer id);

	Frequency update(Frequency frequency);

	Frequency getById(Integer id);

	List<GetFrequencyListResponse>  getFrequencyDetailsById(Integer id);

	List<FrequencyRequestItem> getFrequencyDetailsByIdForUpdate(Integer id);
	

	Response inactiveTranslatedFrequency(FrequencyRequest request);

	Response updateTranslatedFrequency(TranslatedFrequencyMasterDto dto);

	Response saveTranslatedFrequency(TranslatedFrequencyMasterDto dto);

    Response getConvertedLanguageListById(FrequencyRequest request);

	List<TranslatedFrequencyMasterDto> getTranslatedFrequencyListForUpdate(Frequency responseList);

//	List<Frequency> getFrequencyList();

	
}
