package com.param.pharmacy.nursing.workstation.dao.drugAdministration;

import com.param.global.common.Response;
import com.param.global.pharmacy.dto.AntibioticTestDoseDto;
@SuppressWarnings("rawtypes")
public interface IAntibioticTestDoseDao {

	Response saveAntibioticTestDose(AntibioticTestDoseDto dto);

	Response getAntibioticTestDose(AntibioticTestDoseDto dto);

	Response StopAdministration(AntibioticTestDoseDto dto);

	Response checkObservation(AntibioticTestDoseDto dto);

	Response getAllergyType(AntibioticTestDoseDto dto);

}
