package com.param.otims.dao;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.otims.dto.IntraOperativeImageDto;
import com.param.otims.model.IntraOperativeImage;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class IntraOperativeImageDaoImpl extends GenericDao<IntraOperativeImage, Integer> implements IIntraOperativeImageDao,ICommonConstants{

	private static final Logger logger = Logger.getLogger(IntraOperativeImageDaoImpl.class);

	public IntraOperativeImageDaoImpl() {
		super(IntraOperativeImage.class);
	}

	@Override
	public Response saveIntraImage(IntraOperativeImageDto dto) throws ApplicationException {
		try{
			IntraOperativeImage master = new IntraOperativeImage();
			master.setIntraOperativeId(dto.getIntraOperativeId());
			master.setImageUrl(dto.getImageUrl());
			master.setOrganizationId(dto.getOrganizationId());
			master.setUnitId(dto.getUnitId());
			master.setCreatedBy(dto.getCreatedBy());
			master.setCreatedDate(dto.getCreatedDate() != null && !dto.getCreatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
			master.setUpdatedDate(dto.getUpdatedDate() != null && !dto.getUpdatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
			master.setUpdatedBy(dto.getUpdatedBy());
			master.setStatus('A');
			master = save(master);
			
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, master);
		 } catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage());
        }
	}
}
