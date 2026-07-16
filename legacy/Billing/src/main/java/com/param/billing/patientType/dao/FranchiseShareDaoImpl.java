package com.param.billing.patientType.dao;

import com.param.billing.global.model.FranchiseShareMaster;
import com.param.billing.patientType.dto.FranchiseShareDetailsDto;
import com.param.billing.patientType.dto.FranchiseShareDto;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.utility.TimeZoneComponent;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.List;

@Repository
public class FranchiseShareDaoImpl implements IFranchiseShareDao, ICommonConstants {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Response saveFranchiseShare(FranchiseShareDto franchiseShareDto) {

        LinkedList<Integer> ids = new LinkedList<>();

        for (FranchiseShareDetailsDto shareDto : franchiseShareDto.getFrShareDetailsList()) {


            StringBuilder query = new StringBuilder("select share_id as \"shareId\" from billing.m_franchise_share where " );

            if(shareDto.getOrganizationId()!=null)
                query.append(" organization_id =:organizationId ");

            if(shareDto.getFranchiseUnitId()!=null)
                query.append(" AND franchise_unit_id =:franchiseUnitId ");

            if(shareDto.getSpecialityId()!=null)
                query.append(" AND speciality_id =:specialityId ");

            if(shareDto.getSubSpecialityId()!=null)
                query.append(" AND  sub_speciality_id =:subSpecialityId ");

            if(shareDto.getMarkupStatus()!=null)
                query.append(" AND markup_status =:markupStatus ");

            if(shareDto.getServiceId()!=null)
                query.append(" AND service_id =:serviceId ");

            if(shareDto.getItemId()!=null)
                query.append(" AND item_id =:itemId ");

            if(shareDto.getAssetCategoryId()!=null)
                query.append(" AND asset_category_id =:assetCategoryId ");

            if(shareDto.getProductCategoryId()!=null)
                query.append(" AND product_category_id =:productCategoryId ");

            //For Checking Start
            Query sqlQuery =sessionFactory.getCurrentSession().createSQLQuery(query.toString());

            if(shareDto.getOrganizationId()!=null)
                sqlQuery.setParameter("organizationId", shareDto.getOrganizationId());

            if(shareDto.getFranchiseUnitId()!=null)
                sqlQuery.setParameter("franchiseUnitId", shareDto.getFranchiseUnitId());

            if(shareDto.getSpecialityId()!=null)
                sqlQuery.setParameter("specialityId", shareDto.getSpecialityId());

            if(shareDto.getSubSpecialityId()!=null)
                sqlQuery.setParameter("subSpecialityId", shareDto.getSubSpecialityId());

            if(shareDto.getMarkupStatus()!=null)
                sqlQuery.setParameter("markupStatus", shareDto.getMarkupStatus());

            if(shareDto.getServiceId()!=null)
                sqlQuery.setParameter("serviceId", shareDto.getServiceId());

            if(shareDto.getItemId()!=null)
                sqlQuery.setParameter("itemId", shareDto.getItemId());

            if(shareDto.getAssetCategoryId()!=null)
                sqlQuery.setParameter("assetCategoryId", shareDto.getAssetCategoryId());

            if(shareDto.getProductCategoryId()!=null)
                sqlQuery.setParameter("productCategoryId", shareDto.getProductCategoryId());


            List<FranchiseShareDetailsDto> shareGrpList = sqlQuery.setResultTransformer(Transformers.aliasToBean(FranchiseShareDetailsDto.class)).list();
            //For Checking End

            if(shareGrpList != null && shareGrpList.size() > 0) {
                ids.add(shareGrpList.getFirst().getShareId());
            }
            FranchiseShareMaster shareMaster = new FranchiseShareMaster();
            shareMaster.setFranchiseUnitId(shareDto.getFranchiseUnitId());
            shareMaster.setSpecialityId(shareDto.getSpecialityId());
            shareMaster.setSubSpecialityId(shareDto.getSubSpecialityId());
            shareMaster.setCreatedBy(shareDto.getCreatedBy());
            shareMaster.setMarkupStatus(shareDto.getMarkupStatus());
            shareMaster.setOrganizationId(shareDto.getOrganizationId());
            shareMaster.setPercentage(shareDto.getPercentage());
            shareMaster.setUnitId(shareDto.getUnitId());
            shareMaster.setUpdatedBy(shareDto.getUpdatedBy());
            shareMaster.setStatus("A");
            shareMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(shareDto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
            shareMaster.setCreatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(shareDto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
            shareMaster.setServiceId(shareDto.getServiceId());
            shareMaster.setProductCategoryId(shareDto.getProductCategoryId());
            shareMaster.setAssetCategoryId(shareDto.getAssetCategoryId());
            shareMaster.setItemId(shareDto.getItemId());
            sessionFactory.getCurrentSession().save(shareMaster);
        }

        if(ids.size() > 0) {
            sessionFactory.getCurrentSession().createSQLQuery("DELETE FROM billing.m_franchise_share where share_id IN (:shareIds)")
                    .setParameterList("shareIds", ids)
                    .executeUpdate();

            sessionFactory.getCurrentSession().createSQLQuery("DELETE FROM billing.m_franchise_share where status='D'")
                    .executeUpdate();
        }

        return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
    }

    @Override
    public Response getFranchiseShareByUnit(int unitId) {
        List<FranchiseShareDetailsDto> shareDtoList =sessionFactory.getCurrentSession()
                .createSQLQuery("select "
                        + " 	pts.share_id as \"shareId\", "
                        + " 	pts.franchise_unit_id as \"franchiseUnitId\", "
                        + " 	pts.speciality_id as \"specialityId\", "
                        + " 	pts.sub_speciality_id as \"subSpecialityId\", "
                        + " 	pts.percentage as \"percentage\", "
                        + " 	pts.code as \"code\", "
                        + " 	pts.description as \"description\", "
                        + " 	pts.markup_status as \"markupStatus\", "
                        + " 	pts.status as \"status\", "
                        + " 	pts.unit_id as \"unitId\", "
                        + " 	mum.unit_name as \"unitName\", "
                        + " 	serviceM.service_standard_name as \"serviceName\", "
                        + " 	serviceM.service_standard_code as \"serviceCode\", "
                        + " 	serviceM.service_master_id as \"serviceId\", "
                        + " 	item.name as \"itemName\", "
                        + " 	item.code as \"itemCode\", "
                        + " 	product.category as \"productCategoryName\", "
                        + " 	asset.category as \"assetCategoryName\", "
                        + " 	asset.id as \"assetCategoryId\", "
                        + " 	pts.organization_id as \"organizationId\", "
                        + " 	to_char(pts.created_date, 'dd/mm/yyyy') as \"createdDate\", "
                        + " 	pts.created_by as \"createdBy\", "
                        + " 	to_char(pts.updated_date, 'dd/mm/yyyy') as \"updatedDate\", "
                        + " 	pts.updated_by as \"updatedBy\", "
                        + " 	sp.speciality_name as \"specialityName\", "
                        + " 	ssm.sub_speciality_name as \"subSpecialityName\" "
                        + " from "
                        + " 	billing.m_franchise_share pts "
                        + " inner join public.m_unit_master mum on pts.franchise_unit_id = mum.unit_id "
                        + " left join doctor.m_speciality_master sp on "
                        + " 	pts.speciality_id = sp.speciality_id "
                        + " left join doctor.m_sub_speciality_master ssm on "
                        + " 	pts.sub_speciality_id = ssm.sub_speciality_id "
                        + " left join service.m_service_master serviceM on "
                        + " 	serviceM.service_master_id = pts.service_id "
                        + " left join public.m_item item on "
                        + " 	item.id = pts.item_id "
                        + " left join public.m_product_category product on "
                        + " 	product.id = pts.product_category_id "
                        + " left join public.m_asset_category asset on "
                        + " 	asset.id = pts.asset_category_id "
                        + " where pts.franchise_unit_id =:unitId ")
                .setParameter("unitId", unitId)
                .setResultTransformer(Transformers.aliasToBean(FranchiseShareDetailsDto.class)).list();


        return new Response(SUCCESS, SUCCESS_CODE, "success", shareDtoList, null);
    }

    @Override
    public Response updateStatusGroupWise(FranchiseShareDetailsDto dto) {
        FranchiseShareMaster master = (FranchiseShareMaster) sessionFactory.getCurrentSession()
                .get(FranchiseShareMaster.class,dto.getShareId());
        master.setUpdatedBy(dto.getUpdatedBy());
        master.setUpdatedDate(GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
        master.setStatus(dto.getStatus().toString());
        sessionFactory.getCurrentSession().update(master);
        return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
    }

    @Override
    public Response updateStatusUnitWise(FranchiseShareDto dto) {

        sessionFactory.getCurrentSession()
                .createSQLQuery("update billing.m_franchise_share set status =:status where franchise_unit_id =:franchiseUnitId")
                .setParameter("franchiseUnitId",dto.getFranchiseUnitId())
                .setParameter("status",dto.getStatus())
                .executeUpdate();
        return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
    }

    @Override
    public Response getShareUnit(FranchiseShareDto dto) {
        List<FranchiseShareDto> unitList =sessionFactory.getCurrentSession()
                .createSQLQuery( "select "
                        + " 	distinct(pts.franchise_unit_id) as \"franchiseUnitId\", "
                        + " 	pts.code as \"code\", "
                        + " 	mum.unit_name as \"unitName\", "
                        + " 	(case when (select count(p.status) from billing.m_franchise_share p where p.franchise_unit_id = pts.franchise_unit_id and p.status='A')>0 then cast('A' as char) else cast('I' as char) end) as \"status\" "
                        + " from "
                        + " 	billing.m_franchise_share pts "
                        + " inner join public.m_unit_master mum on pts.franchise_unit_id = mum.unit_id "
                        + "  where pts.status <> 'D' and pts.unit_id =:unitId ")
                .setParameter("unitId", dto.getUnitId())
                .setResultTransformer(Transformers.aliasToBean(FranchiseShareDto.class)).list();


        return new Response(SUCCESS, SUCCESS_CODE, "success", unitList, null);
    }


}
