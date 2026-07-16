package com.param.global.billing.dao;

import com.param.billing.global.model.FranchiseDepositMaster;
import com.param.global.billing.controller.BillingException;
import com.param.global.billing.dto.*;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.service.OrderMasterServiceImpl;
import com.param.utility.TimeZoneComponent;
import in.param.common.dao.GenericDao;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FrenchiseDepositDaoImpl extends GenericDao<FranchiseDepositMaster, Integer> implements IFrenchiseDepositDao, ICommonConstants {

    public FrenchiseDepositDaoImpl() {
        super(FranchiseDepositMaster.class);
    }

    @Override
    public Response addFrenchiseDeposit(FranchiseDepositMasterDto dto) throws BillingException {
        try{
            FranchiseDepositMaster master = new FranchiseDepositMaster();
            master.setAvailableDeposit(Double.valueOf(OrderMasterServiceImpl.df2.format(dto.getAvailableDeposit())));
            master.setConsumedDeposit(dto.getConsumedDeposit());
            master.setCreatedBy(dto.getCreatedBy());
            master.setCreatedDate(TimeZoneComponent.getDateByzone(dto.getUnitId()));
            master.setAmount(Double.valueOf(OrderMasterServiceImpl.df2.format(dto.getAmount())));
            master.setFrDepositNo(dto.getFrDepositNo());
            master.setOrgId(dto.getOrgId());
            master.setFranchiseUnitId(dto.getFranchiseUnitId());
            master.setStatus(ACTIVE);
            master.setUnitId(dto.getUnitId());
            master.setUpdatedBy(dto.getUpdatedBy());
            master.setUpdatedDate(TimeZoneComponent.getDateByzone(dto.getUnitId()));
            master.setStatus('A');
            master.setIdentificationNo(dto.getIdentificationNo());
            master.setReceivedFrom(dto.getReceivedFrom());
            master.setRemarks(dto.getRemarks());
            master.setCounterId(dto.getCounterId());
            master.setSignetureImage(dto.getSignetureImage());
            master.setSignature(dto.getSignature());
            master = save(master);
            return new Response<>(SUCCESS, SUCCESS_CODE, null, null, master);
        }catch(HibernateException he){
            throw new BillingException(he.getMessage());
        }catch(Exception e){
            
            throw new BillingException(e.getMessage());
        }
    }

    @Override
    public Response getFrenchiseDeposit(FrDepositSearchReqDto dto) throws BillingException {
        try{
            StringBuilder query = new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_FRANCHISE_DEPOSIT_LIST").getQueryString());

            if(dto.getFromDate() == null || dto.getToDate() == null || dto.getFromDate().isEmpty() || dto.getToDate().isEmpty())
            {
                dto.setFromDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "yyyy-MM-dd"));
                dto.setToDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "yyyy-MM-dd"));
            }
            query.append((dto.getFromDate() != null && !dto.getFromDate().isEmpty()) ? " and to_char(mfd.created_date,'YYYY-MM-DD') >=:fromDate ":"");
            query.append((dto.getToDate() != null && !dto.getToDate().isEmpty()) ? " and to_char(mfd.created_date,'YYYY-MM-DD') <=:toDate ":"");
            query.append(" order by mfd.created_date desc ");
            Query sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
                    .setParameter("orgId", dto.getOrgId())
                    .setParameter("unitId", dto.getUnitId());

            if(dto.getFromDate() != null && !dto.getFromDate().isEmpty()){
                sqlQuery.setString("fromDate", dto.getFromDate());
            }
            if(dto.getToDate() != null && !dto.getToDate().isEmpty()){
                sqlQuery.setString("toDate", dto.getToDate());
            }
            List<FrDepositResDto> listDepositSearchResDto = sqlQuery.setResultTransformer(Transformers.aliasToBean(FrDepositResDto.class)).list();
            return new Response<>(SUCCESS, SUCCESS_CODE, null, listDepositSearchResDto, null);
        }catch(Exception e){
            
            throw e;
        }
    }

    @Override
    public Response getFrenchiseDepositDetails(Integer frDepositId) throws BillingException {
        try{
            List<FranchiseDepositDetailsDto> frPaymentDetails = sessionFactory.getCurrentSession().getNamedQuery("GET_FRANCISE_DEPOSIT_DETAILS")
                    .setParameter("frDepositId", frDepositId)
                    .setResultTransformer(Transformers.aliasToBean(FranchiseDepositDetailsDto.class)).list();
            return new Response<>(SUCCESS, SUCCESS_CODE, null, frPaymentDetails, null);
        }catch(Exception e){
            
            throw e;
        }
    }
}
