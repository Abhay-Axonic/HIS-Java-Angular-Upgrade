package com.param.global.mrd.dao;

import com.param.global.common.Response;
import com.param.global.model.MrdRackMaster;
import com.param.global.mrd.dto.RackFilterDto;
import com.param.global.mrd.dto.RackRequestDto;
import com.param.global.mrd.dto.RackResponseDto;
import com.param.utility.TimeZoneComponent;
import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
import static com.param.global.common.ICommonConstants.*;
@Repository
public class MrdRackMasterDaoImpl extends GenericDao<MrdRackMaster,Integer> implements IMrdRackMasterDao {
	final static Logger logger = Logger.getLogger(MrdRackMasterDaoImpl.class);


    @Autowired
    private SessionFactory sessionFactory;

    public MrdRackMasterDaoImpl() {
        super(MrdRackMaster.class);
    }

    @Override
    public Response saveRackMaster(RackRequestDto rackRequestDto) throws ApplicationException {
        try {
            MrdRackMaster rackMaster = new MrdRackMaster();
            rackMaster.setRackCode(rackRequestDto.getRackCode());
            rackMaster.setRackDescription(rackRequestDto.getRackDescription());
            rackMaster.setStatus(rackRequestDto.getStatus());
            rackMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(rackRequestDto.getUnitId()));
            rackMaster.setCreatedDate(TimeZoneComponent.getDateByzone(rackRequestDto.getUnitId()));
            rackMaster.setUpdatedBy(rackRequestDto.getUpdatedBy());
            rackMaster.setCreatedBy(rackRequestDto.getCreatedBy());
            rackMaster.setUnitId(rackRequestDto.getUnitId());
            rackMaster.setOrganizationId(rackRequestDto.getOrganizationId());
            save(rackMaster);
            return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
    } catch (Exception e) {
    logger.error("saveRackMaster failed", e);
    }
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
    }

    @Override
    public RackResponseDto getRackById(Integer rackId, Integer orgId, Integer unitId) throws ApplicationException {
        RackResponseDto rackResponseDto = null;
        try {
            System.out.println("getRackById dao");

            rackResponseDto = (RackResponseDto) sessionFactory.getCurrentSession()
                    .getNamedQuery("GET_MRD_RACK_BY_ID")
                    .setParameter("rackId", rackId)
                    .setParameter("orgId", orgId)
                    .setParameter("unitId", unitId)
                    .setResultTransformer(Transformers.aliasToBean(RackResponseDto.class))
                    .uniqueResult();
        } catch (Exception e) {
        logger.error("getRackById failed", e);
        }
        return rackResponseDto;

    }

    @Override
    public List<RackResponseDto> getRackList(RackFilterDto rackFilterDto) throws ApplicationException {
        List rackResponseDtos = sessionFactory.getCurrentSession()
                .getNamedQuery("GET_MRD_RACK_LIST")
                .setParameter("orgId",rackFilterDto.getOrganizationId())
                .setParameter("unitId", rackFilterDto.getUnitId())
                .setFirstResult(rackFilterDto.getPage() != null ? rackFilterDto.getPage() : 0)
                .setMaxResults(rackFilterDto.getSize() != null ? rackFilterDto.getSize() : 10)
                .setResultTransformer(Transformers.aliasToBean(RackResponseDto.class)).list();
        return rackResponseDtos;
    }

    @Override
    public Response updateRack(RackRequestDto rackRequestDto) throws ApplicationException {
        Optional<MrdRackMaster> rackMasterOptional = Optional.of(findById(rackRequestDto.getRackId()));
        if (rackMasterOptional.isPresent()){
            MrdRackMaster rackMaster = rackMasterOptional.get();
            rackMaster.setRackCode(rackRequestDto.getRackCode());
            rackMaster.setRackDescription(rackRequestDto.getRackDescription());
            rackMaster.setStatus(rackRequestDto.getStatus());
            rackMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(rackRequestDto.getUnitId()));
            rackMaster.setUpdatedBy(rackRequestDto.getUpdatedBy());
            rackMaster.setUnitId(rackRequestDto.getUnitId());
            rackMaster.setOrganizationId(rackRequestDto.getOrganizationId());
            update(rackMaster);
            return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);

        }
        return new Response(ERROR, ERROR_CODE, ID_NOT_EXISTS, null, null);
    }

    @Override
    public Response updateRackStatus(RackRequestDto rackRequestDto) throws ApplicationException {
        Optional<MrdRackMaster> rackMasterOptional = Optional.of(findById(rackRequestDto.getRackId()));
        if (rackMasterOptional.isPresent()){
            MrdRackMaster rackMaster = rackMasterOptional.get();
            rackMaster.setStatus(rackRequestDto.getStatus() == ACTIVE ? ACTIVE : INACTIVE);
            rackMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(rackRequestDto.getUnitId()));
            rackMaster.setUpdatedBy(rackRequestDto.getUpdatedBy());
            update(rackMaster);
            return new Response(SUCCESS, SUCCESS, COMMON_UPDATE, null, null);

        }
        return new Response(ERROR, ERROR_CODE, ID_NOT_EXISTS, null, null);

    }

    @Override
    public BigInteger getRackCount(RackFilterDto rackFilterDto) throws ApplicationException {
        return (BigInteger) sessionFactory.getCurrentSession()
                .getNamedQuery("GET_MRD_RACK_LIST_COUNT")
                .setParameter("orgId",rackFilterDto.getOrganizationId())
                .setParameter("unitId", rackFilterDto.getUnitId())
                .uniqueResult();
    }

    @Override
    public Response getActiveRackList(Integer orgId, Integer unitId) throws ApplicationException {
      List list= sessionFactory.getCurrentSession()
                .getNamedQuery("GET_ACTIVE_MRD_RACK_LIST")
                .setParameter("orgId",orgId)
              .setParameter("unitId",unitId)
                .setResultTransformer(Transformers.aliasToBean(RackResponseDto.class)).list();
        System.out.println(list.size());
        return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);

    }

    @Override
    public Boolean checkRackExist(RackRequestDto rackRequestDto) {
            return  (Boolean) sessionFactory.getCurrentSession()
                    .getNamedQuery("CHECK_RACK_EXISTS_OR_NOT")
                    .setParameter("rackDescription", rackRequestDto.getRackDescription())
                    .setParameter("rackCode", rackRequestDto.getRackCode())
                    .setParameter("organizationId", rackRequestDto.getOrganizationId())
                    .uniqueResult();
    }

    @Override
    public Boolean checkRackExistExcludeRackId(RackRequestDto rackRequestDto) {
        return  (Boolean) sessionFactory.getCurrentSession()
                .getNamedQuery("CHECK_RACK_EXISTS_OR_NOT_AND_EXCLUDES_RACK_ID")
                .setParameter("rackDescription", rackRequestDto.getRackDescription())
                .setParameter("rackCode", rackRequestDto.getRackCode())
                .setParameter("organizationId", rackRequestDto.getOrganizationId())
                .setParameter("rackId",rackRequestDto.getRackId())
                .uniqueResult();
    }
}
