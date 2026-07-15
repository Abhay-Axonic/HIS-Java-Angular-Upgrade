package com.param.mrd.service;

import com.param.global.common.Response;
import com.param.mrd.dto.MrdFileManagementDto;
import com.param.mrd.dto.MrdFileStatusUpdateDto;

import java.util.List;

public interface IMrdFileManagementService {
   Response searchNumberByPatientIdAndType(String visitType, Integer patientId);

   Response saveFileAllocation(MrdFileManagementDto mrdDto);

    Response<MrdFileManagementDto, Integer> updateMrdAllocation(MrdFileManagementDto mrdDto);
    Response getMrdFiles(Integer patientId, Integer unitId, Integer organizationId, String status);

    Response updateMrdAllocationStatus(List<MrdFileStatusUpdateDto> mrdDto);

    Response validateMrdUser(Integer userId, Integer unitId, Integer organizationId);
}
