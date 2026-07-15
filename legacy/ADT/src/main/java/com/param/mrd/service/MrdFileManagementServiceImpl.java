package com.param.mrd.service;

import in.param.exception.ApplicationException;
import com.param.global.common.Response;
import com.param.mrd.dao.IMrdFileManagementDao;
import com.param.mrd.dao.IMrdFileStatusDao;
import com.param.mrd.dto.MrdFileListResponseDto;
import com.param.mrd.dto.MrdFileManagementDto;
import com.param.mrd.dto.MrdFileStatusUpdateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.param.global.common.ICommonConstants.*;

@Service
public class MrdFileManagementServiceImpl implements IMrdFileManagementService{

    @Autowired
    private IMrdFileManagementDao iMrdFileManagementDao;


    @Transactional
    @Override
    public Response searchNumberByPatientIdAndType(String visitType, Integer patientId) {
        try {
            return iMrdFileManagementDao.searchNumberByPatientIdAndType(visitType, patientId);
        } catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
        
    }

    @Transactional
    @Override
    public Response saveFileAllocation(MrdFileManagementDto mrdDto) {
        try{

        Response response = iMrdFileManagementDao.saveFileAllocation(mrdDto);
            List<MrdFileListResponseDto>  list = new ArrayList<>();
            if(Integer.parseInt(response.getCode())==200){
               list = iMrdFileManagementDao.getMrdFiles(mrdDto.getPatientId(), mrdDto.getUnitId(),mrdDto.getOrganizationId(), "Available");
            }
            response.setListObject(list);
            return response;
        }catch(Exception e){
            throw new ApplicationException(e.getMessage(), e);
        }
    }


    @Transactional
    @Override
    public Response<MrdFileManagementDto, Integer> updateMrdAllocation(MrdFileManagementDto mrdDto) {
        try{
            Response response = iMrdFileManagementDao.updateFileAllocation(mrdDto);
            System.out.println("in responsev "+response);

            List<MrdFileListResponseDto>  list = iMrdFileManagementDao.getMrdFiles(mrdDto.getPatientId(), mrdDto.getUnitId(),mrdDto.getOrganizationId(), mrdDto.getStatus());
//            System.out.println("list size "+list.size());
            return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, list, null);

        }catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
    }

    @Transactional
    @Override
    public Response getMrdFiles(Integer patientId, Integer unitId, Integer organizationId, String status) {
        try{
        List<MrdFileListResponseDto> list = iMrdFileManagementDao.getMrdFiles(patientId,unitId,organizationId,status);
        return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
        }catch (Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
    }

    @Transactional
    @Override
    public Response updateMrdAllocationStatus(List<MrdFileStatusUpdateDto> mrdDto) {
        return iMrdFileManagementDao.updateMrdAllocationStatus(mrdDto);
    }

    @Override
    public Response validateMrdUser(Integer userId, Integer unitId, Integer organizationId) {
        return iMrdFileManagementDao.validateMrdUser(userId,unitId,organizationId);
    }
}
