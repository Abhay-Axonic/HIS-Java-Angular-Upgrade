package com.param.adt.admission.dao;

import com.param.adt.admission.model.AdmissionTriageHistory;
import com.param.adt.common.ICommonConstants;
import com.param.adt.common.Response;
import com.param.global.dto.AdmissionDto;
import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class AdmissionTriageHistoryDaoImpl
        extends GenericDao<AdmissionTriageHistory, Integer>
        implements IAdmissionTriageHistoryDao, ICommonConstants {

    public AdmissionTriageHistoryDaoImpl() {
        super(AdmissionTriageHistory.class);
    }

    @Override
    public Response saveTriageHistory(AdmissionDto dto) throws ApplicationException {
        try {
            AdmissionTriageHistory model = new AdmissionTriageHistory();
            model.setAdmissionDetailsId(dto.getAdmissionDetailsId());
            model.setCurrentTriageId(dto.getTriageCategoryId());
            model.setCreatedBy(dto.getUserId());
            model.setCreatedDate(new Date());
            model.setUpdatedBy(dto.getUserId());
            model.setUpdatedDate(new Date());
            save(model);
            return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);

        } catch (HibernateException he) {
            he.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new Response<>(ERROR,null, COMMON_ERROR_MESSAGE, null, null);
    }
}