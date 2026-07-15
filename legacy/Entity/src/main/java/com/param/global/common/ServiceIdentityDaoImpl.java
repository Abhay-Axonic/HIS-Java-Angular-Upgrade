package com.param.global.common;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class ServiceIdentityDaoImpl extends GenericDao<ServiceIdentity, Integer> implements IServiceIdentityDao, ICommonConstants {

    public ServiceIdentityDaoImpl() {
        super(ServiceIdentity.class);
    }
    @Autowired
    SessionFactory sessionFactory;


    @Override
    public String saveServiceId(String serverId) throws ApplicationException {
        try{


            StringBuilder query = new StringBuilder(" select service_identity_id as \"serviceIdentityId\"," +
                    " service_id as \"serviceId\"," +
                    " primary_host as \"primaryHost\" " +
                    " FROM public.t_service_identity " +
                    " WHERE last_heart_beat_date_time >= NOW() - INTERVAL '6 minutes' " );


            List<ServiceIdentity> list = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
                    .setResultTransformer(Transformers.aliasToBean(ServiceIdentity.class)).list();

            List<Integer> idsToDelete = new ArrayList<>();
            if(list != null && list.size()>0){
                Boolean checkPrimHost = false;
                Boolean serviceExits = false;
                Integer id = null;
                for (ServiceIdentity dto : list) {

                    idsToDelete.add(dto.getServiceIdentityId());

                    if(dto.getServiceId().equals(serverId)){
                        serviceExits = true;
                        id = dto.getServiceIdentityId();
                        if (dto.getPrimaryHost()){
                            checkPrimHost = true;
                        }
                    }
                }
                if(serviceExits){

                    ServiceIdentity master = findById(id);
                    Date date = new Date();
                    master.setLastHeartBeatDateTime(date.getTime());
                    master.setPrimaryHost(checkPrimHost ? true : !this.checkPrimary(list));
                    update(master);

                    this.deleteOldRecords(idsToDelete);



                }else{
                    this.saveMaster(serverId,!this.checkPrimary(list),idsToDelete);
                }

            }else{
                this.saveMaster(serverId,true,idsToDelete);
            }

            return "";
        }catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public Boolean checkServiceIdAvailable(String serverIds) throws ApplicationException {
        StringBuilder query = new StringBuilder(" select  primary_host as \"primaryHost\" " +
                " FROM public.t_service_identity " +
                " WHERE service_id = :serverId and primary_host = true " );

        List<ServiceIdentity> list = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
                .setParameter("serverId",serverIds)
                .setResultTransformer(Transformers.aliasToBean(ServiceIdentity.class)).list();

        if(list != null && list.size()>0){
            return true;
        }else{
            return false;
        }
    }

    private void deleteOldRecords(List<Integer> idsToDelete) {

        String hql = "DELETE FROM t_service_identity WHERE service_identity_id not IN (:ids)";
        int rowsDeleted = sessionFactory.getCurrentSession().createSQLQuery(hql)
                .setParameterList("ids", idsToDelete) .executeUpdate();

    }

    private Boolean checkPrimary(List<ServiceIdentity> list) {

        Boolean checkThisPrimHost = false;

        for (ServiceIdentity dto : list) {

            if (dto.getPrimaryHost()){
                checkThisPrimHost = true;
            }

        }
        return checkThisPrimHost;
    }

    private void saveMaster(String serverId, Boolean checkThisPrimHost, List<Integer> idsToDelete) {

        Date date = new Date();

        ServiceIdentity transaction = new ServiceIdentity();
        transaction.setLastHeartBeatDateTime(date.getTime());
        transaction.setPrimaryHost(checkThisPrimHost);
        transaction.setServiceId(serverId);
        transaction =  save(transaction);
        idsToDelete.add(transaction.getServiceIdentityId());

        this.deleteOldRecords(idsToDelete);
    }
}
