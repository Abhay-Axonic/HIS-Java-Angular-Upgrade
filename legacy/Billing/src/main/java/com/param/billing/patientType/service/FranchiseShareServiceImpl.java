package com.param.billing.patientType.service;


import com.param.billing.patientType.dao.IFranchiseShareDao;
import com.param.billing.patientType.dto.FranchiseShareDto;
import com.param.billing.patientType.dto.FranchiseShareDetailsDto;
import com.param.global.common.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FranchiseShareServiceImpl implements IFranchiseShareService{

    @Autowired
    private IFranchiseShareDao  iFranchiseShareDao;

    @Override
    public Response saveFranchiseShare(FranchiseShareDto franchiseShareDto) {
        return iFranchiseShareDao.saveFranchiseShare(franchiseShareDto);
    }

    @Override
    public Response getFranchiseShareByUnit(int unitId) {
        return iFranchiseShareDao.getFranchiseShareByUnit(unitId);
    }

    @Override
    public Response updateStatusGroupWise(FranchiseShareDetailsDto dto) {
        return iFranchiseShareDao.updateStatusGroupWise(dto);
    }

    @Override
    public Response updateStatusUnitWise(FranchiseShareDto dto) {
        return iFranchiseShareDao.updateStatusUnitWise(dto);
    }


    @Override
    public Response getShareUnit(FranchiseShareDto dto) {
        return iFranchiseShareDao.getShareUnit(dto);
    }


}
