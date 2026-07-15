package com.param.global.dao;

import com.param.adt.master.global.model.UserMaster;
import com.param.adt.master.global.model.hl7Config;
import com.param.global.common.ICommonConstants;
import com.param.global.dao.kit.items.IHl7ConfigListDao;
import com.param.global.dto.UserMasterDto;
import com.param.global.dto.sync.hl7ConfigListDto;
import com.param.service.global.SessionService;
import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes", "unused", "unchecked"})
public class Hl7ConfigListDaoImpl extends GenericDao<hl7Config, Integer> implements IHl7ConfigListDao, ICommonConstants {
	final static Logger logger = Logger.getLogger(Hl7ConfigListDaoImpl.class);

    public Hl7ConfigListDaoImpl() {
        super(hl7Config.class);
    }
    @Autowired
    private org.hibernate.SessionFactory SessionFactory;

    @Autowired
    private DozerBeanMapper mapper;

    @Autowired
    private SessionService sessionService;


    @Override
    public List<hl7ConfigListDto> gethl7ConfigList()  throws ApplicationException {
        try {
            String query = "SELECT "
                    + "  hl7_config_id AS \"hl7ConfigId\", "
                    + "  his_app_url AS \"hisAppUrl\", "
                    + "  is_pms AS \"isPms\", "
                    + "  hl7_tls AS \"hl7Tls\", "
                    + "  hl7_server_port AS \"hl7ServerPort\", "
                    + "  hl7_port AS \"hl7Port\", "
                    + "  hl7_host_ip AS \"hl7HostIp\", "
                    + "  org_id AS \"orgId\", "
                    + "  unit_id AS \"unitId\" "
                    + "FROM public.hl7_config ";
            List<hl7ConfigListDto> hl7ConfigListDto = SessionFactory.getCurrentSession().createSQLQuery(query)
                    .setResultTransformer(Transformers.aliasToBean(hl7ConfigListDto.class)).list();
            if (hl7ConfigListDto != null) {
                return hl7ConfigListDto;
            } else {
                return null;
            }

        } catch (HibernateException he) {
            throw new HibernateException(he.getMessage());
        } catch (Exception e) {
        logger.error("gethl7ConfigList failed", e);
        }
        return null;

    }


}
