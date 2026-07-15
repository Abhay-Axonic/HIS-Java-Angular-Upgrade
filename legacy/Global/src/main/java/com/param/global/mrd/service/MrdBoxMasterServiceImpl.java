package com.param.global.mrd.service;

import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.mrd.dao.IMrdBoxMasterDao;
import com.param.global.mrd.dto.BoxFilterDto;
import com.param.global.mrd.dto.BoxRequestDto;
import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static com.param.global.common.ICommonConstants.*;

@Service
public class MrdBoxMasterServiceImpl implements IMrdBoxMasterService {
    private static final Logger logger = Logger.getLogger(MrdBoxMasterServiceImpl.class);
    @Autowired
    private IMrdBoxMasterDao iMrdBoxMasterDao;
    @Transactional
    @Override
    public Response saveBoxMaster(BoxRequestDto boxRequestDto) throws ApplicationException {
        try {
            if (iMrdBoxMasterDao.checkBoxExist(boxRequestDto)) {
                return new Response(ERROR, null, ALREADY_EXIST, null, null);
            } else {
                return iMrdBoxMasterDao.saveBoxMaster(boxRequestDto);
            }
        } catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
    }

    @Transactional
    @Override
    public Response getBoxById(Integer boxId, Integer orgId, Integer unitId) throws ApplicationException {
        try {
             return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, iMrdBoxMasterDao.getBoxById(boxId, orgId, unitId));

        } catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
    }

    @Transactional
    @Override
    public Response getBoxList(BoxFilterDto boxFilterDto) throws ApplicationException {

        try {
            return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE,  iMrdBoxMasterDao.getBoxList(boxFilterDto), iMrdBoxMasterDao.getBoxCount(boxFilterDto));

        } catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
    }

    @Transactional
    @Override
    public Response updateBox(BoxRequestDto boxRequestDto) throws ApplicationException {
        try {
            if ( iMrdBoxMasterDao.checkBoxExistExcludeBoxId(boxRequestDto)) {
                return new Response(ERROR, null, ALREADY_EXIST, null, null);
            } else {
                return iMrdBoxMasterDao.updateBox(boxRequestDto);
            }
        } catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
        
    }

    @Transactional
    @Override
    public Response updateBoxStatus(BoxRequestDto boxRequestDto) throws ApplicationException {
        return iMrdBoxMasterDao.updateBoxStatus(boxRequestDto);
    }

    @Override
    public Response getBoxCount(BoxRequestDto boxRequestDto) throws ApplicationException {
        return null;
    }

    @Transactional
    @Override
    public Response getActiveBoxList(Integer orgId, Integer unitId) throws ApplicationException {
            return iMrdBoxMasterDao.getActiveBoxList(orgId,unitId);

    }
}
