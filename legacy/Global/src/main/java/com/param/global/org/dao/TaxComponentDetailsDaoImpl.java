package com.param.global.org.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.entity.model.master.TaxComponentDetails;
import com.param.global.common.ICommonConstants;
import com.param.global.dto.TaxComponentDetailsDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;


import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings({ "rawtypes", "unchecked" })
@Repository
public class TaxComponentDetailsDaoImpl extends GenericDao<TaxComponentDetails, Integer>implements ITaxComponentDetailsDao, ICommonConstants{
	final static Logger logger = Logger.getLogger(TaxComponentDetailsDaoImpl.class);


	public TaxComponentDetailsDaoImpl() {
		super(TaxComponentDetails.class);
	}

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public void saveTaxComponentDetails(TaxComponentDetailsDto dto) throws Exception {
		try{
			TaxComponentDetails master = new TaxComponentDetails();
			
				master.setCreatedBy(dto.getCreatedBy());
				master.setGrnDetailsId(dto.getGrnDetailsId() != null ?  dto.getGrnDetailsId() :null );
				master.setGrnId(dto.getGrnId() != null ? dto.getGrnId()  : null);
				master.setGrnRetrunId(dto.getGrnRetrunId()!= null ? dto.getGrnRetrunId()  : null);
				master.setGrnRetrunDetailsId(dto.getGrnRetrunDetailsId()!= null ? dto.getGrnRetrunDetailsId() : null);
				master.setItemId(dto.getItemId());
				master.setOrganizationId(dto.getOrganizationId());
				master.setStatus('A');
				master.setTaxAmount(dto.getTaxAmount());
				master.setTaxComponentAmount(dto.getTaxComponentAmount());
				master.setTaxComponentId(dto.getTaxComponentId());
				master.setTaxComponentName(dto.getTaxComponentName());
				master.setTaxComponentPercentage(dto.getTaxComponentPercentage());
				master.setTaxId(dto.getTaxId());
				master.setTaxPercentage(dto.getTaxPercentage());
				master.setUnitId(dto.getUnitId());
				master.setUpdatedBy(dto.getCreatedBy());
				master.setCreatedDate(TimeZoneComponent.getDateByzone(dto.getUnitId()));
				master.setUpdatedDate(TimeZoneComponent.getDateByzone(dto.getUnitId()));
				master = save(master);
			
		 } catch (Exception e)
        {
        logger.error("saveTaxComponentDetails failed", e);
        }
       
		
	}

	@Override
	public List<TaxComponentDetailsDto> getTaxComponentDetailsById(Integer id,Integer fromGrnOrreturn) {
		
		if(fromGrnOrreturn == 0) {
			String sql = "select ttcd.tax_component_percentage as \"taxComponentPercentage\","
					+ "ttcd.tax_percentage as \"taxPercentage\", " + 
					"ttcd.tax_component_details_id  as \"taxComponentDetailsId\" " + 
					"from  pharmacy.t_tax_component_details ttcd where  ttcd.grn_details_id  =:id ";
			Query query = getSession().createSQLQuery(sql).setParameter("id",id);
			query.setResultTransformer(Transformers.aliasToBean(TaxComponentDetailsDto.class));

			return query.list();
		}else {
			String sql = "select ttcd.tax_component_percentage as \"taxComponentPercentage\","
					+ "ttcd.tax_percentage as \"taxPercentage\", " + 
					"ttcd.tax_component_details_id  as \"taxComponentDetailsId\" " + 
					"from  pharmacy.t_tax_component_details ttcd where  ttcd.grn_retrun_details_id  =:id ";
			Query query = getSession().createSQLQuery(sql).setParameter("id",id);
			query.setResultTransformer(Transformers.aliasToBean(TaxComponentDetailsDto.class));

			return query.list();
		}
		
		
	}

	@Override
	public void updateTaxComponentDetails(TaxComponentDetailsDto taxComponentDetailsDto) {
		try{
			
				TaxComponentDetails master = findById(taxComponentDetailsDto.getTaxComponentDetailsId());
				master.setTaxComponentAmount(taxComponentDetailsDto.getTaxComponentAmount());
				master.setTaxAmount(taxComponentDetailsDto.getTaxAmount());
				update(master);
				
		 } catch (Exception e)
        {
        logger.error("updateTaxComponentDetails failed", e);
        }
       
		
	}

}
