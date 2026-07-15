package com.param.global.mrd.service;

import com.param.global.common.Response;
import com.param.global.mrd.dao.IMrdRackMasterDao;
import com.param.global.mrd.dao.MrdRackMasterDaoImpl;
import com.param.global.mrd.dto.RackFilterDto;
import com.param.global.mrd.dto.RackRequestDto;
import in.param.exception.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
import static com.param.global.common.ICommonConstants.*;

@Service
@SuppressWarnings({"rawtypes", "unchecked", "unused"})
public class MrdRackMasterServiceImpl implements IMrdRackMasterService {
	final static Logger logger = Logger.getLogger(MrdRackMasterServiceImpl.class);

    @Autowired
    private IMrdRackMasterDao iMrdRackMasterDao;
    @Transactional
    @Override
    public Response saveRackMaster(RackRequestDto rackRequestDto) throws ApplicationException {
        try {
            if (iMrdRackMasterDao.checkRackExist(rackRequestDto)) {
                return new Response(ERROR, null, ALREADY_EXIST, null, null);
            } else {
                return iMrdRackMasterDao.saveRackMaster(rackRequestDto);
            }
        } catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
    }

    @Transactional
    @Override
    public Response getRackById(Integer rackId, Integer orgId, Integer unitId) throws ApplicationException {
        try {
            System.out.println("getRackById: "+ iMrdRackMasterDao.getRackById(rackId, orgId, unitId));
             return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, iMrdRackMasterDao.getRackById(rackId, orgId, unitId));

        } catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
    }

    @Transactional
    @Override
    public Response getRackList(RackFilterDto rackFilterDto) throws ApplicationException {

        try {
            return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE,  iMrdRackMasterDao.getRackList(rackFilterDto), iMrdRackMasterDao.getRackCount(rackFilterDto));

        } catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
    }

    @Transactional
    @Override
    public Response updateRack(RackRequestDto rackRequestDto) throws ApplicationException {
        try {
            if (iMrdRackMasterDao.checkRackExistExcludeRackId(rackRequestDto)) {
                return new Response(ERROR, null, ALREADY_EXIST, null, null);
            } else {
                return iMrdRackMasterDao.updateRack(rackRequestDto);
            }
        } catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
    }

    @Transactional
    @Override
    public Response updateRackStatus(RackRequestDto rackRequestDto) throws ApplicationException {
        return iMrdRackMasterDao.updateRackStatus(rackRequestDto);
    }

    @Override
    public Response getRackCount(RackRequestDto rackRequestDto) throws ApplicationException {
        return null;
    }

    @Transactional
    @Override
    public Response getActiveRackList(Integer orgId, Integer unitId) throws ApplicationException {
            return iMrdRackMasterDao.getActiveRackList(orgId,unitId);

    }
}
