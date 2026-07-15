package com.param.billing.unit.dao;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.billing.common.IError;
import com.param.billing.common.Response;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.dto.CorporateStatementDetailsChecklistDto;
import com.param.service.global.model.CorporateStatementDetailsChecklistMapper;
import com.param.service.global.model.CorporateStatmentMaster;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings({"rawtypes", "unchecked"})
@Repository
public class CorporateStatementDetailsChecklistDaoImpl extends GenericDao<CorporateStatementDetailsChecklistMapper, Integer> implements ICorporateStatementDetailsChecklistDao,ICommonConstants, IError{

		public CorporateStatementDetailsChecklistDaoImpl() {
			super(CorporateStatementDetailsChecklistMapper.class);
		}
		// TODO Auto-generated constructor stub

		
		@Autowired
		SessionFactory sessionFactory;
		
		@Override
		public Response saveCorporateStatementDetailsChecklist(
				CorporateStatementDetailsChecklistDto corporateStatementDetailsChecklistDto)
				throws ApplicationException {

			try {
				
				for(int i=0;i<corporateStatementDetailsChecklistDto.getListOfDetailsChecklistDto().size();i++)
				{
					CorporateStatementDetailsChecklistMapper corporateStatementDetailsChecklistMapper=new CorporateStatementDetailsChecklistMapper();
					corporateStatementDetailsChecklistMapper.setCorporateStatementId(corporateStatementDetailsChecklistDto.getListOfDetailsChecklistDto().get(i).getCorporateStatementId());
					corporateStatementDetailsChecklistMapper.setCorporateStatementChecklistId(corporateStatementDetailsChecklistDto.getListOfDetailsChecklistDto().get(i).getCorporateStatementChecklistId());
					corporateStatementDetailsChecklistMapper.setOrganizationId(corporateStatementDetailsChecklistDto.getOrganizationId());
					corporateStatementDetailsChecklistMapper.setUnitId(corporateStatementDetailsChecklistDto.getUnitId());
					corporateStatementDetailsChecklistMapper.setStatus(corporateStatementDetailsChecklistDto.getStatus());
					corporateStatementDetailsChecklistMapper.setCreatedBy(corporateStatementDetailsChecklistDto.getCreatedBy());
					corporateStatementDetailsChecklistMapper.setCreatedDate(GlobalCommonDateUtils.getDate(corporateStatementDetailsChecklistDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
					corporateStatementDetailsChecklistMapper.setUpdatedBy(corporateStatementDetailsChecklistDto.getUpdatedBy());
					corporateStatementDetailsChecklistMapper.setUpdatedDate(GlobalCommonDateUtils.getDate(corporateStatementDetailsChecklistDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
					save(corporateStatementDetailsChecklistMapper);
				}
				
				
			
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null,null);
			
			} catch (HibernateException he) {
			} catch (Exception e) {
				
			}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		
		}

}
