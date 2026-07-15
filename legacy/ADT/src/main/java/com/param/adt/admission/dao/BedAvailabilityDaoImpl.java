


package com.param.adt.admission.dao;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.adt.admission.dto.BedAvailibailityDto;
import com.param.adt.common.ADTCommonDateUtils;
import com.param.adt.common.ICommonConstants;
import com.param.adt.common.Response;
import com.param.adt.master.global.dto.BedCategoryMasterDto;
import com.param.global.model.BedCategoryMaster;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class BedAvailabilityDaoImpl extends GenericDao<BedCategoryMaster, Integer> implements IBedAvailabilityDao,ICommonConstants
{

	public BedAvailabilityDaoImpl() {
		super(BedCategoryMaster.class);
		// TODO Auto-generated constructor stub
	}
	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	public Response getBedCategoryList(BedCategoryMasterDto bedCategoryMasterDto) throws ApplicationException {
		try {
			List<BedCategoryMasterDto> bedAllocationDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BED_CATEGORY_LIST_BY_ORG_UNIT")
					.setParameter("organizationId", bedCategoryMasterDto.getOrganizationId())
					.setParameter("unitId", bedCategoryMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(BedCategoryMasterDto.class)).list();
			return new Response(SUCCESS, null, null, bedAllocationDtosList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response bedAvailiblityList(BedAvailibailityDto bedAvailibailityDto) {
		Session session=sessionFactory.getCurrentSession();
		
		String query =   "select "
				+ "	 distinct bedcategor0_.bed_category_id as \"bedCategoryId\", "
				+ "	to_char(bedcategor0_.doa, "
				+ "	'yyyy-MM-dd') as \"doa\" , "
				+ "	( "
				+ "		select max(bedcategor1_.wait_list_number) "
				+ "	from "
				+ "		adt.t_bed_category_waiting_list bedcategor1_ "
				+ "	where "
				+ "		bedcategor1_.status = 'A' "
				+ "		and to_char(bedcategor1_.doa,'dd-mm-yyyy') = to_char(bedcategor0_.doa,'dd-mm-yyyy') "
				+ "		and bedcategor1_.bed_category_id = bedcategor0_.bed_category_id) as \"waitListNumber\"  "
				+ "from "
				+ "	adt.t_bed_category_waiting_list bedcategor0_ "
				+ "where "
				+ "	bedcategor0_.status = 'A' "
				+ "	and (bedcategor0_.doa between :startDate and :endDate ) "
				+ "	and bedcategor0_.organization_id =:organizationId "
				+ "	and bedcategor0_.unit_id =:unitId ";
		
		
		
		List<BedAvailibailityDto> bedAvailibailityDtosList = session.createSQLQuery(query)
				.setParameter("organizationId", bedAvailibailityDto.getOrganizationId())
				.setParameter("unitId", bedAvailibailityDto.getUnitId())
				.setParameter("startDate", ADTCommonDateUtils.getDate(bedAvailibailityDto.getStartDate(), "yyyy-M-dd"))
				.setParameter("endDate", ADTCommonDateUtils.getDate(bedAvailibailityDto.getEndDate(), "yyyy-M-dd"))
				.setResultTransformer(Transformers.aliasToBean(BedAvailibailityDto.class)).list();
		return new Response(SUCCESS, null, null, bedAvailibailityDtosList, null);
	}

}
