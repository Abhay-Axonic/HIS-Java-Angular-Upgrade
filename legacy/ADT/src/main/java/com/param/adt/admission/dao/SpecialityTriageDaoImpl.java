package com.param.adt.admission.dao;

import com.param.adt.admission.model.SpecialityTriage;
import com.param.adt.common.ICommonConstants;
import com.param.adt.common.Response;
import com.param.global.dto.AdmissionDto;
import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class SpecialityTriageDaoImpl
        extends GenericDao<SpecialityTriage, Integer>
        implements ISpecialityTriageDao, ICommonConstants {

    public SpecialityTriageDaoImpl() {
        super(SpecialityTriage.class);
    }

    @Override
    public Response saveTriageDetails(AdmissionDto dto) throws ApplicationException {
        try {

            SpecialityTriage model = new SpecialityTriage();

            model.setAdmissionDetailsId(dto.getAdmissionDetailsId());
            model.setAdmissionId(dto.getAdmissionId());
            model.setSpecialityTriage(dto.getSpecialityTriage());
            model.setTriageDetails(dto.getTriageDetails());
            model.setCreatedBy(dto.getUserId());
            model.setCreatedDate(new Date());
            model.setUpdatedBy(dto.getUserId());
            model.setUpdatedDate(new Date());

            if(dto.getSpecialityTriageId() ==null) {
                save(model);
            }else{
                model.setSpecialityTriageId(dto.getSpecialityTriageId());
                update(model);
            }
            return new Response<>(SUCCESS, SUCCESS_CODE, "Triage saved successfully", null, null);

        } catch (HibernateException he) {
            he.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new Response<>(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
    }
}