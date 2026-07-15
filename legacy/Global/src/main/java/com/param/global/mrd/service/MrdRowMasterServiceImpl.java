package com.param.global.mrd.service;

import com.param.global.common.Response;
import com.param.global.mrd.dao.IMrdRowMasterDao;
import com.param.global.mrd.dao.MrdRowMasterDaoImpl;
import com.param.global.mrd.dto.RowFilterDto;
import com.param.global.mrd.dto.RowRequestDto;
import in.param.exception.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
import static com.param.global.common.ICommonConstants.*;

@Service
public class MrdRowMasterServiceImpl implements IMrdRowMasterService {
	final static Logger logger = Logger.getLogger(MrdRowMasterServiceImpl.class);

    @Autowired
    private IMrdRowMasterDao iMrdRowMasterDao;
    @Transactional
    @Override
    public Response saveRowMaster(RowRequestDto rowRequestDto) throws ApplicationException {
        try {
            if (iMrdRowMasterDao.checkRowExist(rowRequestDto)) {
                return new Response(ERROR, null, ALREADY_EXIST, null, null);
            } else {
                return iMrdRowMasterDao.saveRowMaster(rowRequestDto);
            }
        } catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
    }

    @Transactional
    @Override
    public Response getRowById(Integer rowId, Integer orgId, Integer unitId) throws ApplicationException {
        try {
             return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, iMrdRowMasterDao.getRowById(rowId, orgId, unitId));

        } catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
        
    }

    @Transactional
    @Override
    public Response getRowList(RowFilterDto rowFilterDto) throws ApplicationException {

        try {
            return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE,  iMrdRowMasterDao.getRowList(rowFilterDto), iMrdRowMasterDao.getRowCount(rowFilterDto));

        } catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
        
    }

    @Transactional
    @Override
    public Response updateRow(RowRequestDto rowRequestDto) throws ApplicationException {
        try {
            if ( iMrdRowMasterDao.checkRowExistExcludeRowId(rowRequestDto)) {
                return new Response(ERROR, null, ALREADY_EXIST, null, null);
            } else {
                return iMrdRowMasterDao.updateRow(rowRequestDto);
            }
        } catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
        
    }

    @Transactional
    @Override
    public Response updateRowStatus(RowRequestDto rowRequestDto) throws ApplicationException {
        return iMrdRowMasterDao.updateRowStatus(rowRequestDto);
    }

    @Override
    public Response getRowCount(RowRequestDto rowRequestDto) throws ApplicationException {
        return null;
    }

    @Transactional
    @Override
    public Response getActiveRowList(Integer orgId, Integer unitId) throws ApplicationException {
            return iMrdRowMasterDao.getActiveRowList(orgId,unitId);

    }
}
