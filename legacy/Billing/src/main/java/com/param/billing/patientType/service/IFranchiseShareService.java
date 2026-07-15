package com.param.billing.patientType.service;

import com.param.billing.patientType.dto.FranchiseShareDto;
import com.param.billing.patientType.dto.FranchiseShareDetailsDto;
import com.param.global.common.Response;

public interface IFranchiseShareService {
    Response saveFranchiseShare(FranchiseShareDto franchiseShareDto);
    Response getFranchiseShareByUnit(int unitId);
    Response updateStatusGroupWise(FranchiseShareDetailsDto dto);
    Response updateStatusUnitWise(FranchiseShareDto dto);
    Response getShareUnit(FranchiseShareDto dto);
}
