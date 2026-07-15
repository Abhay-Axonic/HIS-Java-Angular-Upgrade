package com.param.global.mrd.dao;

import com.param.global.common.Response;
import com.param.global.model.BoxMaster;
import com.param.global.mrd.dto.BoxFilterDto;
import com.param.global.mrd.dto.BoxRequestDto;
import com.param.global.mrd.dto.BoxResponseDto;
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
public class MrdBoxMasterDaoImpl extends GenericDao<BoxMaster,Integer> implements IMrdBoxMasterDao{
	final static Logger logger = Logger.getLogger(MrdBoxMasterDaoImpl.class);


    @Autowired
    private SessionFactory sessionFactory;

    public MrdBoxMasterDaoImpl() {
        super(BoxMaster.class);
    }

    @Override
    public Response saveBoxMaster(BoxRequestDto boxRequestDto) throws ApplicationException {
        try {
            BoxMaster boxMaster = new BoxMaster();
            boxMaster.setBoxCode(boxRequestDto.getBoxCode());
            boxMaster.setBoxDescription(boxRequestDto.getBoxDescription());
            boxMaster.setStatus(boxRequestDto.getStatus());
            boxMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(boxRequestDto.getUnitId()));
            boxMaster.setCreatedDate(TimeZoneComponent.getDateByzone(boxRequestDto.getUnitId()));
            boxMaster.setUpdatedBy(boxRequestDto.getUpdatedBy());
            boxMaster.setCreatedBy(boxRequestDto.getCreatedBy());
            boxMaster.setUnitId(boxRequestDto.getUnitId());
            boxMaster.setOrganizationId(boxRequestDto.getOrganizationId());
            save(boxMaster);
            return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
    } catch (Exception e) {
    logger.error("saveBoxMaster failed", e);
    }
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
    }

    @Override
    public BoxResponseDto getBoxById(Integer boxId, Integer orgId, Integer unitId) throws ApplicationException {
           return  (BoxResponseDto) sessionFactory.getCurrentSession()
                    .getNamedQuery("GET_MRD_BOX_BY_ID")
                    .setParameter("boxId", boxId)
                    .setParameter("orgId", orgId)
                    .setParameter("unitId", unitId)
                   .setResultTransformer(Transformers.aliasToBean(BoxResponseDto.class))

                    .uniqueResult();
    }

    @Override
    public List<BoxResponseDto> getBoxList(BoxFilterDto boxFilterDto) throws ApplicationException {
        List boxResponseDtos = sessionFactory.getCurrentSession()
                .getNamedQuery("GET_MRD_BOX_LIST")
                .setParameter("orgId",boxFilterDto.getOrganizationId())
                .setParameter("unitId", boxFilterDto.getUnitId())
                .setFirstResult(boxFilterDto.getPage() != null ? boxFilterDto.getPage() : 0)
                .setMaxResults(boxFilterDto.getSize() != null ? boxFilterDto.getSize() : 10)
                .setResultTransformer(Transformers.aliasToBean(BoxResponseDto.class)).list();
        return boxResponseDtos;
    }

    @Override
    public Response updateBox(BoxRequestDto boxRequestDto) throws ApplicationException {
        Optional<BoxMaster> boxMasterOptional = Optional.of(findById(boxRequestDto.getBoxId()));
        if (boxMasterOptional.isPresent()){
            BoxMaster boxMaster = boxMasterOptional.get();
            boxMaster.setBoxCode(boxRequestDto.getBoxCode());
            boxMaster.setBoxDescription(boxRequestDto.getBoxDescription());
            boxMaster.setStatus(boxRequestDto.getStatus());
            boxMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(boxRequestDto.getUnitId()));
            boxMaster.setUpdatedBy(boxRequestDto.getUpdatedBy());
            boxMaster.setUnitId(boxRequestDto.getUnitId());
            boxMaster.setOrganizationId(boxRequestDto.getOrganizationId());
            update(boxMaster);
            return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);

        }
        return new Response(ERROR, ERROR_CODE, ID_NOT_EXISTS, null, null);
    }

    @Override
    public Response updateBoxStatus(BoxRequestDto boxRequestDto) throws ApplicationException {
        Optional<BoxMaster> boxMasterOptional = Optional.of(findById(boxRequestDto.getBoxId()));
        if (boxMasterOptional.isPresent()){
            BoxMaster boxMaster = boxMasterOptional.get();
            boxMaster.setStatus(boxRequestDto.getStatus() == ACTIVE ? ACTIVE : INACTIVE);
            boxMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(boxRequestDto.getUnitId()));
            boxMaster.setUpdatedBy(boxRequestDto.getUpdatedBy());
            update(boxMaster);
            return new Response(SUCCESS, SUCCESS,COMMON_UPDATE, null, null);

        }
        return new Response(ERROR, ERROR_CODE, ID_NOT_EXISTS, null, null);

    }

    @Override
    public BigInteger getBoxCount(BoxFilterDto boxFilterDto) throws ApplicationException {
        return (BigInteger) sessionFactory.getCurrentSession()
                .getNamedQuery("GET_MRD_BOX_LIST_COUNT")
                .setParameter("orgId",boxFilterDto.getOrganizationId())
                .setParameter("unitId", boxFilterDto.getUnitId())
                .uniqueResult();
    }

    @Override
    public Response getActiveBoxList(Integer orgId, Integer unitId) throws ApplicationException {
      List list= sessionFactory.getCurrentSession()
                .getNamedQuery("GET_ACTIVE_MRD_BOX_LIST")
                .setParameter("orgId",orgId)
              .setParameter("unitId",unitId)
                .setResultTransformer(Transformers.aliasToBean(BoxRequestDto.class)).list();
        return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);

    }

    @Override
    public Boolean checkBoxExist(BoxRequestDto boxRequestDto) {
            return  (Boolean) sessionFactory.getCurrentSession()
                    .getNamedQuery("CHECK_BOX_EXISTS_OR_NOT")
                    .setParameter("boxDescription", boxRequestDto.getBoxDescription())
                    .setParameter("boxCode", boxRequestDto.getBoxCode())
                    .setParameter("organizationId", boxRequestDto.getOrganizationId())
                    .uniqueResult();
    }

    @Override
    public Boolean checkBoxExistExcludeBoxId(BoxRequestDto boxRequestDto) {
        return  (Boolean) sessionFactory.getCurrentSession()
                .getNamedQuery("CHECK_BOX_EXISTS_OR_NOT_AND_EXCLUDES_BOX_ID")
                .setParameter("boxDescription", boxRequestDto.getBoxDescription())
                .setParameter("boxCode", boxRequestDto.getBoxCode())
                .setParameter("organizationId", boxRequestDto.getOrganizationId())
                .setParameter("boxId",boxRequestDto.getBoxId())
                .uniqueResult();
    }
}
