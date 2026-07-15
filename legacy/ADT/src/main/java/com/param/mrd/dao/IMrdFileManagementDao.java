package com.param.mrd.dao;

import com.param.global.common.Response;
import com.param.mrd.dto.MrdFileListResponseDto;
import com.param.mrd.dto.MrdFileManagementDto;
import com.param.mrd.dto.MrdFileStatusUpdateDto;

import java.util.List;

public interface IMrdFileManagementDao {
    Response searchNumberByPatientIdAndType(String visitType, Integer patientId);

    Response saveFileAllocation(MrdFileManagementDto mrdDto);


    Response updateFileAllocation(MrdFileManagementDto mrdDto);

    List<MrdFileListResponseDto> getMrdFiles(Integer patientId,Integer unitId,Integer organizationId,String status);

    Response updateMrdAllocationStatus(List<MrdFileStatusUpdateDto> mrdDto);

    Response validateMrdUser(Integer userId, Integer unitId, Integer organizationId);
}
