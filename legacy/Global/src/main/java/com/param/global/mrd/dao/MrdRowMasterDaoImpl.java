package com.param.global.mrd.dao;

import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.model.RowMaster;
import com.param.global.mrd.dto.RowFilterDto;
import com.param.global.mrd.dto.RowRequestDto;
import com.param.global.mrd.dto.RowResponseDto;
import com.param.utility.TimeZoneComponent;
import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import static com.param.global.common.ICommonConstants.*;

@Repository
public class MrdRowMasterDaoImpl extends GenericDao<RowMaster,Integer> implements IMrdRowMasterDao{

    private static final Logger logger = Logger.getLogger(MrdRowMasterDaoImpl.class);

    @Autowired
    private SessionFactory sessionFactory;

    public MrdRowMasterDaoImpl() {
        super(RowMaster.class);
    }

    @Override
    public Response saveRowMaster(RowRequestDto rowRequestDto) throws ApplicationException {
        try {
            RowMaster rowMaster = new RowMaster();
            rowMaster.setRowCode(rowRequestDto.getRowCode());
            rowMaster.setRowDescription(rowRequestDto.getRowDescription());
            rowMaster.setStatus(rowRequestDto.getStatus());
            rowMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(rowRequestDto.getUnitId()));
            rowMaster.setCreatedDate(TimeZoneComponent.getDateByzone(rowRequestDto.getUnitId()));
            rowMaster.setUpdatedBy(rowRequestDto.getUpdatedBy());
            rowMaster.setCreatedBy(rowRequestDto.getCreatedBy());
            rowMaster.setUnitId(rowRequestDto.getUnitId());
            rowMaster.setOrganizationId(rowRequestDto.getOrganizationId());
            save(rowMaster);
            return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
    } catch (Exception e) {
            logger.error("saveRowMaster failed: " + e.getMessage(), e);
    }
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
    }

    @Override
    public RowResponseDto getRowById(Integer rowId, Integer orgId, Integer unitId) throws ApplicationException {
           return  (RowResponseDto) sessionFactory.getCurrentSession()
                    .getNamedQuery("GET_MRD_ROW_BY_ID")
                    .setParameter("rowId", rowId)
                    .setParameter("orgId", orgId)
                    .setParameter("unitId", unitId)
                   .setResultTransformer(Transformers.aliasToBean(RowResponseDto.class))
                    .uniqueResult();
    }

    @Override
    public List<RowResponseDto> getRowList(RowFilterDto rowFilterDto) throws ApplicationException {
        List rowResponseDtos = sessionFactory.getCurrentSession()
                .getNamedQuery("GET_MRD_ROW_LIST")
                .setParameter("orgId",rowFilterDto.getOrganizationId())
                .setParameter("unitId", rowFilterDto.getUnitId())
                .setFirstResult(rowFilterDto.getPage() != null ? rowFilterDto.getPage() : 0)
                .setMaxResults(rowFilterDto.getSize() != null ? rowFilterDto.getSize() : 10)
                .setResultTransformer(Transformers.aliasToBean(RowResponseDto.class)).list();
        return rowResponseDtos;
    }

    @Override
    public Response updateRow(RowRequestDto rowRequestDto) throws ApplicationException {
        Optional<RowMaster> rowMasterOptional = Optional.of(findById(rowRequestDto.getRowId()));
        if (rowMasterOptional.isPresent()){
            RowMaster rowMaster = rowMasterOptional.get();
            rowMaster.setRowCode(rowRequestDto.getRowCode());
            rowMaster.setRowDescription(rowRequestDto.getRowDescription());
            rowMaster.setStatus(rowRequestDto.getStatus());
            rowMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(rowRequestDto.getUnitId()));
            rowMaster.setUpdatedBy(rowRequestDto.getUpdatedBy());
            rowMaster.setUnitId(rowRequestDto.getUnitId());
            rowMaster.setOrganizationId(rowRequestDto.getOrganizationId());
            update(rowMaster);
            return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);

        }
        return new Response(ERROR, null, ID_NOT_EXISTS, null, null);
    }

    @Override
    public Response updateRowStatus(RowRequestDto rowRequestDto) throws ApplicationException {
        Optional<RowMaster> rowMasterOptional = Optional.of(findById(rowRequestDto.getRowId()));
        if (rowMasterOptional.isPresent()){
            RowMaster rowMaster = rowMasterOptional.get();
            rowMaster.setStatus(rowRequestDto.getStatus() == ACTIVE ? ACTIVE : INACTIVE);
            rowMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(rowRequestDto.getUnitId()));
            rowMaster.setUpdatedBy(rowRequestDto.getUpdatedBy());
            update(rowMaster);
            return new Response(SUCCESS, SUCCESS, COMMON_UPDATE, null, null);
        }
        return new Response(ERROR, ERROR_CODE, ID_NOT_EXISTS, null, null);
    }

    @Override
    public BigInteger getRowCount(RowFilterDto rowFilterDto) throws ApplicationException {
        return (BigInteger) sessionFactory.getCurrentSession()
                .getNamedQuery("GET_MRD_ROW_LIST_COUNT")
                .setParameter("orgId",rowFilterDto.getOrganizationId())
                .setParameter("unitId", rowFilterDto.getUnitId())
                .uniqueResult();
    }

    @Override
    public Response getActiveRowList(Integer orgId, Integer unitId) throws ApplicationException {
      List list= sessionFactory.getCurrentSession()
                .getNamedQuery("GET_ACTIVE_MRD_ROW_LIST")
                .setParameter("orgId",orgId)
              .setParameter("unitId",unitId)
                .setResultTransformer(Transformers.aliasToBean(RowRequestDto.class)).list();
        return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);

    }

    @Override
    public Boolean checkRowExist(RowRequestDto rowRequestDto) {
            return  (Boolean) sessionFactory.getCurrentSession()
                    .getNamedQuery("CHECK_ROW_EXISTS_OR_NOT")
                    .setParameter("rowDescription", rowRequestDto.getRowDescription())
                    .setParameter("rowCode", rowRequestDto.getRowCode())
                    .setParameter("organizationId", rowRequestDto.getOrganizationId())
                    .uniqueResult();
    }

    @Override
    public Boolean checkRowExistExcludeRowId(RowRequestDto rowRequestDto) {
        return  (Boolean) sessionFactory.getCurrentSession()
                .getNamedQuery("CHECK_ROW_EXISTS_OR_NOT_AND_EXCLUDES_ROW_ID")
                .setParameter("rowDescription", rowRequestDto.getRowDescription())
                .setParameter("rowCode", rowRequestDto.getRowCode())
                .setParameter("organizationId", rowRequestDto.getOrganizationId())
                .setParameter("rowId",rowRequestDto.getRowId())
                .uniqueResult();
    }
}
