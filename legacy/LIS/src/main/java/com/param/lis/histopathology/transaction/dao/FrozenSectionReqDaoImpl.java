package com.param.lis.histopathology.transaction.dao;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.entity.lis.histo.TFrozenSectionReqMaster;
import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.ITransactionConstants;
import com.param.lis.global.common.Response;
import com.param.lis.histopathology.transaction.dto.HistoParamDto;
import com.param.lis.histopathology.transaction.dto.TFrozenSectionReqMasterDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({"rawtypes", "unchecked"})
public class FrozenSectionReqDaoImpl extends GenericDao<TFrozenSectionReqMaster, Integer>
implements IFrozenSectionReqDao, ICommonConstants, IError, ITransactionConstants {
  
  final static Logger logger = Logger.getLogger(FrozenSectionReqDaoImpl.class);
  
  @Autowired
  private DozerBeanMapper mapper;

  public FrozenSectionReqDaoImpl() {
    super(TFrozenSectionReqMaster.class);
  }

  @Override
  public Response saveFrozerSectionRequest(TFrozenSectionReqMasterDto tFrozenSectionReqMasterDto)
      throws ApplicationException {
    try
    {
        tFrozenSectionReqMasterDto.setCreatedDate(TimeZoneComponent.getDateByzone(tFrozenSectionReqMasterDto.getOrgUnitId()!=null ? tFrozenSectionReqMasterDto.getOrgUnitId():null).getTime());
        TFrozenSectionReqMaster tFrozenSectionReqMaster = mapper.map(tFrozenSectionReqMasterDto, TFrozenSectionReqMaster.class,
                "TFrozenSectionReqMasterDtoToTFrozenSectionReqMaster");
        TFrozenSectionReqMaster tFrozenSectionReqMst = save(tFrozenSectionReqMaster);
        if (tFrozenSectionReqMst != null)
        {
            return new Response(SUCCESS, SUCCESS_200, FROZEN_SECTION_REQ_SUCC, null, null);
        } else
        {
            return new Response(SUCCESS, SUCCESS_200, FROZEN_SECTION_REQ_FAIL, null, null);
        }
    }catch(HibernateException ex){
        logger.error("Exection", ex);throw ex;
    } catch (Exception e)
    {
        logger.error("Exection", e);throw e;
    }
  }

  @Override
  public Response getFrozenSectionReqList(HistoParamDto histoParamDto) throws ApplicationException {
    try {
      List<TFrozenSectionReqMasterDto> listTFrozenSectionReqMasterDto = sessionFactory.getCurrentSession()
          .getNamedQuery("GET_FROZEN_SECTION_REQ_LIST")
          .setParameter("orgId", histoParamDto.getOrgId())
          .setParameter("orgUnitId", histoParamDto.getOrgUnitId())
          .setFirstResult(histoParamDto.getOffset() != null ? histoParamDto.getOffset() : 0)
          .setMaxResults(
              histoParamDto.getRecordPerPage() != null ? histoParamDto.getRecordPerPage() : 50)
          .setResultTransformer(Transformers.aliasToBean(TFrozenSectionReqMasterDto.class)).list();

      if (listTFrozenSectionReqMasterDto.isEmpty()) {
        return new Response(ERROR, ERR_500, "No Speciman Records Found.", null, null);
      } else {
        return new Response(SUCCESS, SUCCESS_200, null, listTFrozenSectionReqMasterDto, null);
      }

    } catch (Exception e) {
      logger.error("Exection", e);
      throw e;
    }
  }

  @Override
  public Response getFrozenSectionReqListCount(HistoParamDto histoParamDto)
      throws ApplicationException {
    try {
      Long forzenSectionCount =
          (Long) sessionFactory.getCurrentSession().getNamedQuery("GET_FROZEN_SECTION_REQ_LIST_COUNT")
              .setParameter("orgId", histoParamDto.getOrgId())
              .setParameter("orgUnitId", histoParamDto.getOrgUnitId())
              .uniqueResult();
      if (forzenSectionCount != null) {
        return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, forzenSectionCount);
      } else {
        return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, BigInteger.ZERO);
      }

    } catch (Exception e) {
      logger.error("Exection", e);
      throw e;
    }
  }

  @Override
  public Response acceptFrozensectionReq(
      List<TFrozenSectionReqMasterDto> listTFrozenSectionReqMasterDto) throws ApplicationException {
    try
    {
      
      for (Iterator iterator = listTFrozenSectionReqMasterDto.iterator(); iterator.hasNext();) {
        
        TFrozenSectionReqMasterDto tFrozenSectionReqMasterDto =   (TFrozenSectionReqMasterDto) iterator.next();
        tFrozenSectionReqMasterDto.setUpdatedDate(TimeZoneComponent.getDateByzone(tFrozenSectionReqMasterDto.getOrgUnitId()!=null ? tFrozenSectionReqMasterDto.getOrgUnitId():null).getTime());
        TFrozenSectionReqMaster tFrozenSectionReqMaster = mapper.map(tFrozenSectionReqMasterDto, TFrozenSectionReqMaster.class,
            "TFrozenSectionReqMasterDtoToTFrozenSectionReqMaster");
          update(tFrozenSectionReqMaster);
      }
          return new Response(SUCCESS, SUCCESS_200, FROZEN_SECTION_REQ_ACCEPT_SUCC, null, null);
        
        
    } catch (Exception e)
    {
        logger.error("Exection", e);
        throw e;
    }
  }

}
