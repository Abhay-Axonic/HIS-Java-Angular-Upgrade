package com.param.global.dao;

import java.util.List;

import org.hibernate.transform.Transformers;
import in.param.exception.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.dozer.DozerFactory;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.DataObjectMasterDto;
import com.param.global.model.DataObjectMaster;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;

@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class DataObjectMasterDaoImpl extends GenericDao<DataObjectMaster, Integer> implements IDataObjectMasterDao, ICommonConstants{

	public DataObjectMasterDaoImpl() {
		super(DataObjectMaster.class);
	}

	@Autowired
	private DozerFactory factory;
	
	@Override
	public Response saveDataObject(DataObjectMasterDto dto) throws ApplicationException {
		try{
			DataObjectMaster master = factory.map(dto, DataObjectMaster.class, null);
			master.setCreatedDate(dto.getCreatedDate() != null && !dto.getCreatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
			if(dto.getDataObjectId()!=0){
				master.setDataObjectId(dto.getDataObjectId());
				update(master);
				return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
			}
			master = save(master);
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response checkAlreadyExists(int orgId, int unitId, String title) throws ApplicationException {
		try{
			List<DataObjectMasterDto> listDataObjectMasterDto = sessionFactory.getCurrentSession().getNamedQuery("CHECK_DATA_OBJECT_ALREADY_EXISTS").setParameter("orgId", orgId).setParameter("unitId", unitId)
						.setParameter("title", title.toLowerCase().trim()).setResultTransformer(Transformers.aliasToBean(DataObjectMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listDataObjectMasterDto, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getDataObjectList(int orgId, int unitId) throws ApplicationException {
		try{
			List<DataObjectMasterDto> listDataObjectMasterDto = sessionFactory.getCurrentSession().getNamedQuery("GET_DATA_OBJECT_LIST").setParameter("orgId", orgId)
						.setParameter("unitId", unitId).setResultTransformer(Transformers.aliasToBean(DataObjectMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listDataObjectMasterDto, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getDataObjectById(int id) throws ApplicationException {
		try{
			DataObjectMasterDto dto = (DataObjectMasterDto)sessionFactory.getCurrentSession().getNamedQuery("GET_DATA_OBJECT_BY_ID").setParameter("dataObjectId", id)
							.setResultTransformer(Transformers.aliasToBean(DataObjectMasterDto.class)).uniqueResult();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, dto);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getAllDataObject(int orgId, int unitId) throws ApplicationException {
		try{
			List<DataObjectMasterDto> listDataObjectMasterDto = sessionFactory.getCurrentSession().getNamedQuery("GET_ALL_DATA_OBJECT_LIST").setParameter("orgId", orgId)
						.setParameter("unitId", unitId).setResultTransformer(Transformers.aliasToBean(DataObjectMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listDataObjectMasterDto, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response updateDataObjectStatus(DataObjectMasterDto dataObjectMasterDto) throws ApplicationException {
		try{
		
			Integer count=sessionFactory.getCurrentSession().createQuery("update DataObjectMaster set status=:status where orgId=:orgId and unitId=:unitId and dataObjectId=:dataObjectId ")
					.setParameter("orgId", dataObjectMasterDto.getOrgId())
					.setParameter("unitId", dataObjectMasterDto.getUnitId())
					.setParameter("dataObjectId", dataObjectMasterDto.getDataObjectId())
					.setParameter("status", dataObjectMasterDto.getStatus())
					.executeUpdate();
			if(count>0){		
			   return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
			}else{
				 return new Response<>(ERROR, ERROR_CODE, null, null, null);
			}
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getDataObjectByNameNotId(DataObjectMasterDto reqDto) throws ApplicationException {
		try{
			DataObjectMasterDto dataObjectDto =(DataObjectMasterDto) sessionFactory.getCurrentSession().getNamedQuery("GET_DATA_OBJECT_BY_NAME_NOT_ID")
					.setParameter("orgId", reqDto.getOrgId()).setParameter("unitId", reqDto.getUnitId())
					.setParameter("dataObjectId", reqDto.getDataObjectId())
					.setParameter("title", reqDto.getTitle().toLowerCase().trim()).setResultTransformer(Transformers.aliasToBean(DataObjectMasterDto.class)).uniqueResult();
			if(dataObjectDto!=null)
			    return new Response<>(SUCCESS, SUCCESS_CODE, null, null, dataObjectDto);
			else
				return new Response<>(ERROR, ERROR_CODE, null, null, null);
		}catch(Exception e){
			
			throw e;
		}
	}

}
