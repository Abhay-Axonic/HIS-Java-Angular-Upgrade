package com.param.otims.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.otims.dto.IntraOperativeDetailsDto;
import com.param.global.otims.dto.PreOperativeDetailsDto;
import com.param.global.otims.dto.WhoChecklistDto;
import com.param.otims.model.WhoChecklist;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class WhoChecklistDaoImpl extends GenericDao<WhoChecklist, Integer> implements IWhoChecklistDao,ICommonConstants{

	private static final Logger logger = Logger.getLogger(WhoChecklistDaoImpl.class);

	public WhoChecklistDaoImpl() {
		super(WhoChecklist.class);
	}

	@Override
	public Response saveWhoChecklist(WhoChecklistDto dto) throws ApplicationException {
		try{
			WhoChecklist master = new WhoChecklist();
			master.setChecklistMasterId(dto.getChecklistMasterId());
			master.setRemark(dto.getRemark());
			master.setWhoStatusId(dto.getWhoStatusId());
			master.setPreOperativeId(dto.getPreOperativeId() != null ? dto.getPreOperativeId() : null);
			master.setOrganizationId(dto.getOrganizationId());
			master.setUnitId(dto.getUnitId());
			master.setIntraOperative(dto.getIntraOperative() != null ? dto.getIntraOperative() : null );
			master.setCreatedBy(dto.getCreatedBy());
			master.setCreatedDate(dto.getCreatedDate() != null && !dto.getCreatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
			master.setUpdatedDate(dto.getUpdatedDate() != null && !dto.getUpdatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
			master.setUpdatedBy(dto.getUpdatedBy());
			master.setStatus('A');
			if(dto.getWhoChecklistId() != null) {
				master.setWhoChecklistId(dto.getWhoChecklistId());
				master = update(master);
			}else {
				master = save(master);
			}
			
			
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, master);
		 } catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage());
        }
	}

	@Override
	public Response getWhoChecklistByPreOperativeId(PreOperativeDetailsDto preOperativeDetailsDto)
			throws ApplicationException {
		try{
			
			String query=sessionFactory.getCurrentSession().getNamedQuery("GET_WHO_CHECKLIST_BY_PRE_OPERATIVE_ID").getQueryString();
			List<WhoChecklistDto>  whoChecklistDtoList = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("preOperativeId", preOperativeDetailsDto.getPreOperativeId())
					.setResultTransformer(Transformers.aliasToBean(WhoChecklistDto.class)).list();
			
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, whoChecklistDtoList, null);
		 } catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage());
        }
	}

	@Override
	public Response getWhoChecklistByIntraOperativeId(IntraOperativeDetailsDto intraOperativeDetailsDto)
			throws ApplicationException {
		try{
			
			String query=sessionFactory.getCurrentSession().getNamedQuery("GET_WHO_CHECKLIST_BY_INTRA_OPERATIVE_ID").getQueryString();
			List<WhoChecklistDto>  whoChecklistDtoList = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("intraOperativeId", intraOperativeDetailsDto.getIntraOperativeId())
					.setResultTransformer(Transformers.aliasToBean(WhoChecklistDto.class)).list();
			
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, whoChecklistDtoList, null);
		 } catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage());
        }
	}

}
