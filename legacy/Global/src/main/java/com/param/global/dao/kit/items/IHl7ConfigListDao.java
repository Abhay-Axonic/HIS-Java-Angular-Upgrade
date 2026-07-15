package com.param.global.dao.kit.items;

import com.param.adt.master.global.model.UserMaster;
import com.param.adt.master.global.model.hl7Config;
import com.param.global.dto.UserMasterDto;
import com.param.global.dto.sync.hl7ConfigListDto;
import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;
import org.apache.xmlbeans.impl.xb.xmlconfig.Extensionconfig;
import org.hibernate.HibernateException;
import org.hibernate.transform.Transformers;

import java.util.List;

public interface IHl7ConfigListDao extends IGenericDao<hl7Config, Integer> {
    List<hl7ConfigListDto> gethl7ConfigList();
}