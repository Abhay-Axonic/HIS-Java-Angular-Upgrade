package com.param.adt.admission.dao;

import com.param.adt.admission.dto.NutritionalAssessmentScoreDTO;
import com.param.adt.common.Response;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface INutritionalAssessmentScoreMasterDao {

	Response saveOrUpdateNutritionalAssessmentScoreMaster(NutritionalAssessmentScoreDTO nutritionalAssessmentScoreDTO)throws ApplicationException;

}
