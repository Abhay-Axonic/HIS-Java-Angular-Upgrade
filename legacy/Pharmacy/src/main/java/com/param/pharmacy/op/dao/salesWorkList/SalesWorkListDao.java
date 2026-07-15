package com.param.pharmacy.op.dao.salesWorkList;

import java.util.List;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.pharmacy.Prescription;
import com.param.entity.model.pharmacy.PrescriptionDetail;
import com.param.entity.model.pharmacy.PrescriptionFrequencyDetail;
import com.param.entity.model.pharmacy.Sale;
import com.param.entity.model.pharmacy.SaleDetail;
import com.param.global.common.ServiceException;
import com.param.global.pharmacy.dto.SubstituteItemDto;
import com.param.global.utility.Util;
import com.param.pharmacy.ip.dto.handoverWorklist.HandoverDetailItem;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyItem;
import com.param.pharmacy.ip.dto.pharmacology.prescriptionDetailResp;
import com.param.pharmacy.op.dto.SaleDetailResp;
import com.param.pharmacy.op.dto.SalesWorkQty;
import com.param.pharmacy.op.dto.salesWorkList.FrequencyResponse;
import com.param.pharmacy.op.dto.salesWorkList.PrescriptionDetailItemResponse;
import com.param.pharmacy.op.dto.salesWorkList.PrescriptionDetailResponse;
import com.param.pharmacy.op.dto.salesWorkList.ResponseWorkListSearch;
import com.param.pharmacy.op.dto.salesWorkList.WorkListSearch;
import com.param.service.global.SessionService;

@Repository("salesWorkListDao")
public class SalesWorkListDao extends AbstractDao<Integer, PrescriptionDetail> implements ISalesWorkListDao {

	@Autowired
	private SessionService sessionService;

	@Autowired
	DozerBeanMapper mapper;

	@Autowired
	SessionFactory sessionFactory;

	private static final Logger logger = Logger.getLogger(SalesWorkListDao.class);

	@Override
	public List<Prescription> worklistSearchByFields(WorkListSearch WorkListSearch, Integer screenId) {
		try {

			String hql = "SELECT prescription FROM Prescription AS prescription "
					+ " INNER JOIN prescription.status as status WITH status.isActive = true AND status.isDeleted = false "
					+ " INNER JOIN prescription.visitType as visitType WITH visitType.status = 'A' "
					+ " INNER JOIN prescription.priority AS priority WITH priority.status = 'A'"
					+ " INNER JOIN prescription.doctor AS doctor WITH doctor.status = 'A'"
					+ " INNER JOIN prescription.encounter as encounter WITH encounter.status = 'A'"
					+ " INNER JOIN encounter.patient as patientEncounter WITH patientEncounter.status = 'A' "
					+ " LEFT JOIN patientEncounter.prefix AS prefixEncounter WITH prefixEncounter.status = 'A' "
					+ " WHERE prescription.isActive = true AND prescription.isDeleted = false AND prescription.prescriptionDate BETWEEN  :fromDate AND :toDate "
					+ " AND prescription.unit.unitId = :unitId AND ( visitType.visitTypeId = :visitTypeId OR :visitTypeId = 0 ) AND visitType.visitTypeCode != 'IP' "
					+ " AND ( status.id = :statusId OR :statusId = 0 )"
					+ " AND ((:uhid = '' OR lower(patientEncounter.uhidNumber) LIKE :uhid))"
					+ " AND ((:doctorName = '' OR lower(COALESCE(CONCAT(doctor.firstName,' ',doctor.middleName,' ',doctor.lastName))) LIKE :doctorName OR lower(doctor.firstName) LIKE :doctorName OR lower(doctor.middleName) LIKE :doctorName "
					+ "	OR lower(doctor.lastName) LIKE :doctorName))"
					+ " AND ( :patientId = 0 OR patientEncounter.patientId = :patientId ) "
					+ "	AND ((:patientId != 0 AND :patientName != '') OR (:patientName = '' OR lower(COALESCE(CONCAT(prefixEncounter.prefixCode,' ',patientEncounter.firstName,' ',patientEncounter.middleName,' ', patientEncounter.lastName))) LIKE :patientName "
					+ " OR lower(prefixEncounter.prefixCode) LIKE :patientName OR "
					+ "	lower(patientEncounter.firstName) LIKE :patientName OR lower(patientEncounter.middleName) LIKE :patientName "
					+ "	OR lower(patientEncounter.lastName) LIKE :patientName)) "
					+ " AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
					+ " INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
					+ "	INNER JOIN screenStatus.status as status) " + " ORDER BY  :orderby ";

			Query query = getSession().createQuery(hql);
			query.setParameter("fromDate", Util.getDate(WorkListSearch.getFromDate()));
			query.setParameter("toDate", Util.getDate(WorkListSearch.getToDate()));
			query.setParameter("statusId", WorkListSearch.getStatusId() != null ? WorkListSearch.getStatusId() : 0);
			query.setParameter("visitTypeId",
					WorkListSearch.getVisitTypeId() != null ? WorkListSearch.getVisitTypeId() : 0);
			query.setParameter("uhid",
					WorkListSearch.getUhid() != null ? WorkListSearch.getUhid().toLowerCase() + '%' : "");
			query.setParameter("patientId", WorkListSearch.getPatientId() != null ? WorkListSearch.getPatientId() : 0);
			query.setParameter("patientName",
					WorkListSearch.getPatientName() != null ? WorkListSearch.getPatientName().toLowerCase() + '%' : "");
			query.setParameter("doctorName",
					WorkListSearch.getDoctorName() != null ? WorkListSearch.getDoctorName().toLowerCase() + '%' : "");
			query.setParameter("unitId", sessionService.getUnitId());
			query.setParameter("screenId", screenId);
			query.setParameter("orderby", "FIELD(priority.priorityDesc, 'High','Normal','Low'), prescription.id DESC");

			return query.list();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

	}

	@Override
	public Prescription prescriptionByIdValue(Long prescriptionId) {
		try {
			String hql = "SELECT prescription FROM Prescription AS prescription "
					+ " WHERE (:prescriptionId = prescription.id) AND prescription.isActive = true AND prescription.isDeleted = false ";

			Query query = getSession().createQuery(hql);
			query.setParameter("prescriptionId", prescriptionId);

			return (Prescription) query.uniqueResult();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Sale getSaleValueByPrescriptionId(Long prescriptionId) {
		try {
			String hql = "SELECT sale FROM Sale AS sale "
					+ " WHERE sale.prescription.id = :prescriptionId AND sale.isActive = true AND sale.isDeleted = false "
					+ " ORDER BY sale.id DESC";

			Query query = getSession().createQuery(hql);
			query.setParameter("prescriptionId", prescriptionId);

			query.setMaxResults(1);

			return (Sale) query.uniqueResult();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Long getBatchId(Integer itemId, Long saleId) {
		try {
			String hql = "SELECT batch.id FROM SaleDetail AS detail " + " LEFT JOIN detail.batch AS batch  "
					+ " INNER JOIN detail.prescriptionDetail AS prescriptionDetail  "
					+ " INNER JOIN prescriptionDetail.item AS item  "
					+ " WHERE item.id = :itemId AND detail.sale.id = :saleId " + " ORDER BY detail.id DESC";

			Query query = getSession().createQuery(hql);
			query.setParameter("itemId", itemId);
			query.setParameter("saleId", saleId);

			return (Long) query.uniqueResult();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public List<PrescriptionDetail> prescriptionDetailsByIdValue(Long prescriptionId) {

		try {
			String hql = "SELECT prescriptionDetail FROM PrescriptionDetail AS prescriptionDetail "
					+ " INNER JOIN prescriptionDetail.prescription AS prescription WITH prescription.isActive = true AND prescription.isDeleted = false "
					+ " WHERE (:prescriptionId = prescription.id) AND prescriptionDetail.isActive = true AND prescriptionDetail.isDeleted = false ";

			Query query = getSession().createQuery(hql);
			query.setParameter("prescriptionId", prescriptionId);

			return query.list();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

	}

	/*@Override
	public List<Item> getSubstituteById(Integer id, Integer itemId, Boolean isNarcotic, Integer storeId) {

		try {
			String hql = "SELECT item FROM Item AS item "
                    	+ " INNER JOIN item.storeItemList AS storeItemList WITH storeItemList.isActive = true AND storeItemList.isDeleted = false "
                    	+ " INNER JOIN item.generic AS generic WITH generic.isActive = true AND generic.isDeleted = false "
                    	+ " INNER join generic.productCategory as prodCat WITH prodCat.isActive = true AND prodCat.isDeleted = false "
                    	+ " INNER join prodCat.assetCategory as assetCategory WITH assetCategory.isActive = true AND assetCategory.isDeleted = false "
                    	+ " INNER join assetCategory.assetType as assetType WITH assetType.isActive = true AND assetType.isDeleted = false AND assetType.id=1 "
                    	+ " WHERE (:id = generic.id) AND item.isActive = true AND item.isDeleted = false AND item.id != :itemId "
                    	+ " and storeItemList.store.storeId= :storeId ";


			StringBuffer s1 = new StringBuffer(hql);
			if(isNarcotic==false) {
			s1.append("AND item.isNarcotic="+isNarcotic);
			}
			Query query = getSession().createQuery(s1.toString());
			query.setParameter("id", id);
			query.setParameter("itemId", itemId);
			query.setParameter("storeId",storeId);
			

			return query.list();
		} catch (Exception e) {
			
		}
		return null;

	}*/
	
	/**Added by chetana on 26-11-2021 for HIS-17599 QNRI - Substitute screen should sort depending on formulation type added in prescription. Old API has been commented above.**/
	@Override
	public List<SubstituteItemDto> getSubstituteById(Integer id, Integer itemId, Boolean isNarcotic, Integer storeId, Integer formulationTypeId) {

		try {
			String sql = "select " + 
					"	itemMast.id as \"itemId\", " + 
					"	itemMast.code as \"itemCode\", " + 
	                "   itemMast.storage_unit_id as \"storageUnitId\", " +
	                "    storageUnit.name as \"storageUnitName\", " +
					"	itemMast.formulation_type_id as \"formulationTypeId\", " + 
					"	itemMast.generic_id as \"genericId\", " + 
					"	generic.\"name\" as \"genericName\", " + 
					"	itemMast.is_otc as \"isOtc\", " + 
					"	itemMast.is_lasa as \"isLasa\", " +
					"	itemMast.is_narcotic as \"isNarcotic\", " +
					"	itemMast.organization_id as \"organizationId\", " + 
					"	itemMast.is_batch_required as \"isBatchRequired\", " + 
					"	itemMast.is_three_uom_applicable as \"isThreeUOMApplicable\", " + 
					"	itemMast.manufacturer_id as \"manufactureId\", " + 
					"	mm.\"name\" as \"manufacturerName\", " + 
					"	itemMast.name as \"itemName\", " + 
					"	generic.product_category_id as \"productCategoryId\", " + 
					"	itemMast.s_tax_id as \"taxId\", " + 
					"	tax.tax_percentage as \"taxPercentage\" " + 
					" from " + 
					"	public.m_item itemMast " + 
					" inner join public.m_store_item_mapper storeitem on " + 
					"	itemMast.id = storeitem.item_id " + 
					"	and (storeitem.is_active = true " + 
					"	and storeitem.is_deleted = false) " + 
					" inner join public.m_manufacturer mm on " + 
					"	itemMast.manufacturer_id = mm.id " + 
					"	and (mm.is_active = true " + 
					"	and mm.is_deleted = false) " + 
					" inner join public.m_generic generic on " + 
					"	itemMast.generic_id = generic.id " + 
					"	and (generic.is_active = true " + 
					"	and generic.is_deleted = false)	 " + 
					" inner join public.m_tax tax on " + 
					"	tax.id = itemMast.s_tax_id " + 
					"	and (tax.is_active = true " + 
					"	and tax.is_deleted = false)	 " + 
					" inner join public.m_product_category productcat on " + 
					"	generic.product_category_id = productcat.id " + 
					"	and (productcat.is_active = true " + 
					"	and productcat.is_deleted = false)	 " + 
					" inner join public.m_asset_category assetcateg on " + 
					"	productcat.asset_category_id = assetcateg.id " + 
					"	and (assetcateg.is_active = true " + 
					"	and assetcateg.is_deleted = false)	 " + 
	                "   left join public.m_storage_unit storageUnit on storageUnit.id =  itemMast.storage_unit_id " +
					" inner join public.m_asset_type assettype on " + 
					"	assetcateg.asset_type_id = assettype.id " + 
					"	and (assettype.is_active = true " + 
					"	and assettype.is_deleted = false " + 
					"	and assettype.id = 1)	 " + 
					" where " + 
					"	:id= generic.id " + 
					"	and itemMast.is_active = true " + 
					"	and itemMast.is_deleted = false " + 
					"	and itemMast.id != :itemId " + 
					"	and storeitem.store_id =:storeId ";


			StringBuffer s1 = new StringBuffer(sql);
			if(isNarcotic==false) {
			s1.append("AND itemMast.is_narcotic=:isNarcotic "
					+ " order by " + 
					"	case when itemMast.formulation_type_id =:formulationTypeId then  itemMast.formulation_type_id =:formulationTypeId end asc ,"
					+"	case when itemMast.formulation_type_id !=:formulationTypeId then  itemMast.formulation_type_id end asc"
					
					);
			}else {
				s1.append(" order by " + 
						"	case when itemMast.formulation_type_id =:formulationTypeId then  itemMast.formulation_type_id =:formulationTypeId end asc ,"
						+"	case when itemMast.formulation_type_id !=:formulationTypeId then  itemMast.formulation_type_id end asc"
						);
			}
			Query query = getSession().createSQLQuery(s1.toString())
					.addScalar("itemId").addScalar("itemCode").addScalar("storageUnitName").addScalar("formulationTypeId").addScalar("genericId")
					.addScalar("genericName").addScalar("isOtc").addScalar("isLasa").addScalar("isNarcotic").addScalar("organizationId")
					.addScalar("isBatchRequired").addScalar("isThreeUOMApplicable").addScalar("manufactureId").addScalar("manufacturerName").addScalar("itemName")
			.addScalar("productCategoryId").addScalar("taxId").addScalar("taxPercentage");
			query.setParameter("id", id);
			query.setParameter("itemId", itemId);
			query.setParameter("storeId",storeId);
			if(isNarcotic==false) {
						query.setParameter("isNarcotic", isNarcotic);
						query.setParameter("formulationTypeId", formulationTypeId);
			}else {
				query.setParameter("formulationTypeId", formulationTypeId);
			}
			query.setResultTransformer(Transformers.aliasToBean(SubstituteItemDto.class));

			return query.list();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

	}

	@Override
	public Long getDispensedQuantity(Long id) {
		try {
			String hql = "SELECT COALESCE(SUM(saleDetail.quantity), 0) FROM SaleDetail AS saleDetail \r\n"
					+ " INNER JOIN saleDetail.prescriptionDetail AS prescriptionDetail WITH prescriptionDetail.isActive = true AND prescriptionDetail.isDeleted = false "
					+ " WHERE saleDetail.isActive = true AND saleDetail.isDeleted = false AND (:id = prescriptionDetail.id) AND prescriptionDetail.isActive = true AND prescriptionDetail.isDeleted = false ";

			Query query = getSession().createQuery(hql);
			query.setParameter("id", id);
			//query.setParameter("unitId", sessionService.getUnitId());

			return (query.uniqueResult() != null ? (long) query.uniqueResult() : 0L);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public List<PrescriptionFrequencyDetail> prescriptionFreqDetailByPresDetailId(Long id) {
		try {
			String hql = "SELECT prescriptionFrequencyDetail FROM PrescriptionFrequencyDetail AS prescriptionFrequencyDetail "
					+ " INNER JOIN prescriptionFrequencyDetail.prescriptionDetail AS prescriptionDetail WITH prescriptionDetail.isActive = true AND prescriptionDetail.isDeleted = false "
					+ " WHERE (:id = prescriptionDetail.id) AND prescriptionFrequencyDetail.isActive = true AND prescriptionFrequencyDetail.isDeleted = false ";

			Query query = getSession().createQuery(hql);
			query.setParameter("id", id);

			return query.list();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

	}

	@Override
	public List<SaleDetail> getSaleDetail(Long prescriptionDetailId) {
		try {
			String hql = "SELECT saleDetail FROM SaleDetail AS saleDetail "
					+ " LEFT JOIN saleDetail.prescriptionDetail AS prescriptionDetail WITH prescriptionDetail.isActive = true AND prescriptionDetail.isDeleted = false "
					+ " WHERE (:prescriptionDetailId = prescriptionDetail.id) AND saleDetail.isActive = true AND saleDetail.isDeleted = false "
					+ " order by saleDetail.id desc ";

			Query query = getSession().createQuery(hql);
			query.setMaxResults(1);
			query.setParameter("prescriptionDetailId", prescriptionDetailId);

			return query.list();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public List<ResponseWorkListSearch> worklistSearchByFieldsBP(WorkListSearch WorkListSearch, Integer screenId, Integer unitId) {
		try 
		{

			String hql = "SELECT prescription.id as id, visitType.visitTypeCode as visitType, "
					+ " encounter.encounterId as encounterId , "
					+ " mCompanyContractMaster.contractName as contractName, "
					+ " prescription.prescriptionDate as prescriptionDateTime, "
					+ " prescription.addedByUser as addedByUser, "
					+ " priority.priorityDesc as priority, status.status as status, encounter.createdDate as visitDateTime, "
					+ " doctor.doctorId as doctorId, "
					+ " CONCAT(doctor.firstName, ' ', doctor.middleName, ' ', doctor.lastName, ' | ') as doctorDetail, "
					+ " prescription.prescriptionNo as prescriptionNo, status.id as statusId, "
					+ " patientEncounter.dob as patientDob, specialityMaster.specialityName as specialityName, "
					+ " CONCAT(CASE WHEN prefixEncounter.prefixCode != null THEN prefixEncounter.prefixCode ELSE '' END, "
					+ " ' ',CASE WHEN patientEncounter.firstName != null THEN patientEncounter.firstName ELSE '' END,"
					+ " ' ',CASE WHEN patientEncounter.middleName != null THEN patientEncounter.middleName ELSE '' END,"
					+ " ' ',CASE WHEN  patientEncounter.lastName != null THEN  patientEncounter.lastName ELSE '' END,' | ',"
					+ " CASE WHEN patientEncounter.uhidNumber != null THEN patientEncounter.uhidNumber ELSE '' END, "
					+ " ' | ',CASE WHEN gender.genderCode != null THEN gender.genderCode ELSE '' END) as patientDetail, "
					+ " CONCAT(prefixEncounter.prefixCode, ' ', patientEncounter.firstName, ' ', patientEncounter.middleName, ' ',"
					+ " patientEncounter.lastName) as patientName, "
					+ " patientEncounter.uhidNumber as uhid, "
					+ " patientEncounter.gender.genderId as genderId, "
					+ " patientEncounter.gender.genderCode as genderCode,"
					+ " concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, patientEncounter.dob)) , 'Y ' ) as ageStringDet, "
					+ " patientEncounter.imagePath as imagePath ,"
					+ " patientEncounter.mobileNumber as mobileNumber,"
					+ " patientEncounter.patientId as patientId,"
					+ " patientEncounter.countryCallingCode as countryCallingCode,"
					+ " (case when (select count(*) from PrescriptionDetail presDtl1 where presDtl1.prescription.id=prescription.id and presDtl1.statFlag=true) >0 then true else false end ) as statFlag,"
					+ " paymentEntitlementMaster.paymentEntitlementDesc as entitlementDesc "
					+ " FROM Prescription AS prescription "
					+ " INNER JOIN prescription.status as status WITH status.isActive = true AND status.isDeleted = false "
					+ " INNER JOIN prescription.visitType as visitType WITH visitType.status = 'A' "
					+ " LEFT JOIN prescription.priority AS priority WITH priority.status = 'A'"
					+ " LEFT JOIN prescription.specialityMaster AS specialityMaster WITH specialityMaster.status = 'A'"
					+ " LEFT JOIN prescription.doctor AS doctor WITH doctor.status = 'A'"
					+ " INNER JOIN prescription.encounter as encounter WITH encounter.status = 'A'"
					+ " Left join  encounter.patientSponsorDetailsList as  encpatientSponsorDetailsList with encpatientSponsorDetailsList.priorityId = 1 and encpatientSponsorDetailsList.status='A' "
					+ " Left join encpatientSponsorDetailsList.mCompanyContractMaster mCompanyContractMaster "
					+ " left join encpatientSponsorDetailsList.paymentEntitlementMaster as paymentEntitlementMaster "
				
					+ " INNER JOIN encounter.patient as patientEncounter WITH patientEncounter.status = 'A' "
					+ " INNER JOIN patientEncounter.gender gender "
					+ " LEFT JOIN patientEncounter.prefix AS prefixEncounter  "
					+ " WHERE prescription.isActive = true AND prescription.isDeleted = false AND prescription.prescriptionDate BETWEEN  :fromDate AND :toDate AND prescription.isNonFormulary = 'N'"
					+ " AND prescription.unit.unitId = :unitId AND ( visitType.visitTypeId = :visitTypeId OR :visitTypeId = 0 ) AND visitType.visitTypeCode != 'IP' "
					+ " AND ( status.id = :statusId OR :statusId = 0 ) "
					/*
					 * +
					 * " and (select count(*) from prescription.prescriptionDetailsList presDtl where presDtl.prescription.id=prescription.id ) != "
					 * +
					 * "	(select count(*) from prescription.prescriptionDetailsList presDtl1 where presDtl1.prescription.id=prescription.id and presDtl1.statFlag=true)"
					 */
					+ " AND ((:uhid = '' OR lower(patientEncounter.uhidNumber) LIKE :uhid))"
					+ " AND ((:doctorName = '' OR lower(COALESCE(CONCAT(doctor.firstName,' ',doctor.middleName,' ',doctor.lastName))) LIKE :doctorName OR lower(doctor.firstName) LIKE :doctorName OR lower(doctor.middleName) LIKE :doctorName "
					+ "	OR lower(doctor.lastName) LIKE :doctorName))"
					+ " AND ( :patientId = 0 OR patientEncounter.patientId = :patientId ) "
					+ "	AND ((:patientId != 0 AND :patientName != '') OR (:patientName = '' OR lower(COALESCE(CONCAT(prefixEncounter.prefixCode,' ',patientEncounter.firstName,' ',patientEncounter.middleName,' ', patientEncounter.lastName))) LIKE :patientName "
					+ " OR lower(prefixEncounter.prefixCode) LIKE :patientName OR "
					+ "	lower(patientEncounter.firstName) LIKE :patientName OR lower(patientEncounter.middleName) LIKE :patientName "
					+ "	OR lower(patientEncounter.lastName) LIKE :patientName)) "
					+ " AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
					+ " INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
					+ "	INNER JOIN screenStatus.status as status) " + " ORDER BY  :orderby ";

			Query query = getSession().createQuery(hql);
			query.setParameter("fromDate", Util.getDate(WorkListSearch.getFromDate()));
			query.setParameter("toDate", Util.getDate(WorkListSearch.getToDate()));
			query.setParameter("statusId", WorkListSearch.getStatusId() != null ? WorkListSearch.getStatusId() : 0);
			query.setParameter("visitTypeId",
					WorkListSearch.getVisitTypeId() != null ? WorkListSearch.getVisitTypeId() : 0);
			query.setParameter("uhid",
					WorkListSearch.getUhid() != null ? '%'+WorkListSearch.getUhid().toLowerCase() + '%' : "");
			query.setParameter("patientId", WorkListSearch.getPatientId() != null ? WorkListSearch.getPatientId() : 0);
			query.setParameter("patientName",
					WorkListSearch.getPatientName() != null ? '%'+WorkListSearch.getPatientName().toLowerCase() + '%' : "");
			query.setParameter("doctorName",
					WorkListSearch.getDoctorName() != null ? '%'+WorkListSearch.getDoctorName().toLowerCase() + '%' : "");
			query.setParameter("unitId", unitId!=null ? unitId : sessionService.getUnitId());
			query.setParameter("screenId", screenId);
			query.setParameter("orderby", "FIELD(priority.priorityDesc, 'High','Normal','Low'), prescription.id DESC");
			query.setResultTransformer(Transformers.aliasToBean(ResponseWorkListSearch.class));
			return query.list();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public List<PrescriptionDetailItemResponse> getSubstituteByIdBP(Integer id, Integer itemId) {
		
		try {
			String hql = "SELECT item.id as itemId, item.code as itemCode, item.name as itemName, generic.id as genericId, "
					+ " generic.name as genericName, "
					+ " generic.name as genericName, "
					+ " generic.productCategory.id as genProdCatId ,"
					+ " item.productCategory.id as productCatId ,"
					+ " manu.id as manufacturerId, manu.name as manufacturerName, item.isOtc as isOtc, "
					+ " saleTax.id as taxId, saleTax.name as tax, saleTax.taxPercentage as taxPercentage "
					+ " FROM Item AS item "
					+ " INNER JOIN item.generic AS generic WITH generic.isActive = true AND generic.isDeleted = false "
					+ " INNER JOIN item.manufacturer as manu "
					+ " INNER JOIN item.salesTax as saleTax "
					+ " WHERE (:id = generic.id) AND item.isActive = true AND item.isDeleted = false AND item.id != :itemId";

			Query query = getSession().createQuery(hql);
			query.setParameter("id", id);
			query.setParameter("itemId", itemId);
			query.setResultTransformer(Transformers.aliasToBean(PrescriptionDetailItemResponse.class));
			return query.list();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public List<PrescriptionDetailResponse> prescriptionByIdValueBP(Long prescriptionId) 
	{
		String hql = "SELECT prescription.id as id, "
				+ "  doctor.doctorId as doctorId, "
				+ " CONCAT(doctor.firstName, ' ', doctor.middleName, ' ', doctor.lastName) as doctorDetail, "
				+ " patientEncounter.dob as patientDob, to_char(patientEncounter.dob,'DD/MM/YYYY') as dob, specialityMaster.specialityId as specialityId, specialityMaster.specialityName as specialityName, "
				+ " CONCAT(CASE WHEN prefixEncounter.prefixCode != null THEN prefixEncounter.prefixCode ELSE '' END, "
				+ " ' ',CASE WHEN patientEncounter.firstName != null THEN patientEncounter.firstName ELSE '' END,"
				+ " ' ',CASE WHEN patientEncounter.middleName != null THEN patientEncounter.middleName ELSE '' END,"
				+ " ' ',CASE WHEN  patientEncounter.lastName != null THEN  patientEncounter.lastName ELSE '' END,"
				+ " ' | ',CASE WHEN gender.genderCode != null THEN gender.genderCode ELSE '' END) as patientDetail, "
				+ " patientEncounter.uhidNumber as uhid, patientEncounter.id as patientId, "
				+ " prescription.prescriptionDate as prescriptionDate, issue.remark as remark,"
				+ " gender.genderId as genderId,"
				+ " patientEncounter.imagePath as imagePath, "
				+ " prescription.prescriptionNo as prescriptionNo, status.id as statusId, status.status as status, "
				+ " encounter.id as encounterId, sale.id as saleId, sale.billNo as billNo, sale.addedDate as saleDate,"
				+ " patientEncounter.patientCategoryId as patientCategoryId, prescription.organization.organizationId as organizationId, "
				+ " (CASE WHEN sale.cop IS NOT NULL THEN sale.cop ELSE false END) as cop "
				+ " FROM Prescription AS prescription "
				+ " LEFT JOIN prescription.sales AS sale WITH sale.isActive = true AND sale.isDeleted = false "
				+ " LEFT JOIN sale.issueList AS issue WITH issue.isActive = true AND issue.isDeleted = false "
				+ " INNER JOIN prescription.status as status "
				+ " Left JOIN prescription.specialityMaster AS specialityMaster WITH specialityMaster.status = 'A' "
				+ " Left JOIN prescription.doctor AS doctor WITH doctor.status = 'A' "
				+ " INNER JOIN prescription.encounter as encounter WITH encounter.status = 'A' "
				+ " INNER JOIN encounter.patient as patientEncounter WITH patientEncounter.status = 'A' "
				+ " INNER JOIN patientEncounter.gender gender "
				+ " LEFT JOIN patientEncounter.prefix AS prefixEncounter WITH prefixEncounter.status = 'A' "
				+ " WHERE prescription.isActive = true AND prescription.isDeleted = false "
				+ " AND prescription.id = :prescriptionId "
				+ " ORDER BY sale.id DESC";

		Query query = getSession().createQuery(hql);
		query.setParameter("prescriptionId", prescriptionId);
		query.setResultTransformer(Transformers.aliasToBean(PrescriptionDetailResponse.class));
		return query.list();
	}

	@Override
	public List<PrescriptionDetailItemResponse> prescriptionDetailsByIdValueBP(Long prescriptionId) {
	
		String hql = "SELECT prescriptionDetail.id as id, item.id as itemId, item.code as itemCode, item.isThreeUOMApplicable as isThreeUOMApplicable, prescriptionDetail.parentSubstituteId as parentSubstituteId, "
				+ " item.name as itemName, generic.id as genericId, generic.name as genericName, prescription.id as prescriptionId,item.isNarcotic as isNarcotic, item.isLasa as isLasa, "
                + " manufacturer.id as manufacturerId, manufacturer.name as manufacturerName,prescriptionDetail.id as prescriptionDetailId, prescriptionDetail.store.storeId as storeId,"
				+ " prescriptionDetail.duration as duration , prescriptionDetail.durationUnit as durationUnit, prescriptionDetail.prescribedDose as prescribedDose, prescriptionDetail.orderQty as qtyPrescribed, item.isOtc as isOtc, salesTax.id as taxId,saleDetails.quantity as quantity, item.formulationType.id as formulationTypeId , "
				+ " prescriptionDetail.store.storeId as storeId, "
				+ " salesTax.name as tax, salesTax.taxPercentage as taxPercentage, saleDetails.quantity as quantitydispensed, saleDetails.quantity as newdispQty,"
//				+ " uomUnit.id as uomUnitId, uomType.id as uomTypeId,"
				+ " status.id as statusId, status.status as status, frequency.description as frequencyDesc , "
				+ " prescriptionFrequencyDetails.startDate as startDate ,  prescriptionFrequencyDetails.stopDate as stopDate , "
                + " (case " + 
                "        when instruction.id is null  then  prescriptionDetail.instructionDesc  " + 
                "        else instruction.instructionDesc " + 
                "    end) as instructionDesc, "
                + " strengthUnit.units as units, "
//				+ " saleDetails.netAmount as netAmount, "
//				+ " saleDetails.creditPayable as creditPayable, "
//				+ " saleDetails.selfPayable as selfPayable, "
//				+ " saleDetails.discountAmount as discountAmount, "
//				+ " saleDetails.rate as rate, "
//				+ " saleDetails.taxAmount AS taxAmount, "
//				+ " saleDetails.taxPercentage as taxPercentage, "
//				+ " saleDetails.totalAmount as totalAmount,"
                + " saleDetails.creditPayable as creditPayable, "
				+ " coalesce(saleDetails.packageConsumptionAmt,0) as packageConsumptionAmt,"
				+ " productCat.id as productCatId, genProdCat.id as genProdCatId, COALESCE(itemAssetCat.id, genAssetCat.id) as assetCategoryId, storageUnit.unitName as storageUnitName,cast(saleDetails.id as int) as saleDetailId,(case when saleDetails.isActive is not null then saleDetails.isActive else true end) as isActive "
				+ " FROM PrescriptionDetail AS prescriptionDetail "
				+ " left JOIN prescriptionDetail.item as item "
				+ " left JOIN prescriptionDetail.PrescriptionFrequencyDetails as prescriptionFrequencyDetails "
				+ " left JOIN prescriptionFrequencyDetails.frequency  as frequency   "
				+ " INNER JOIN item.generic as generic "
				+ " INNER JOIN item.manufacturer as manufacturer "
				+ " INNER JOIN item.salesTax as salesTax "
				+ "	LEFT JOIN item.productCategory as productCat "
				+ "	LEFT JOIN generic.productCategory as genProdCat "
				+ " LEFT JOIN productCat.assetCategory as itemAssetCat "
				+ " LEFT JOIN genProdCat.assetCategory as genAssetCat "
				+ " left JOIN prescriptionDetail.status as status "
				+ " LEFT JOIN prescriptionDetail.saleDetails as saleDetails "
//				+ " LEFT JOIN saleDetails.uomUnit as uomUnit "
//				+ " LEFT JOIN saleDetails.uomType as uomType "
				+ " LEFT JOIN prescriptionDetail.strengthUnitPresDTL as strengthUnit "
				+ " LEFT JOIN prescriptionDetail.instruction as instruction "
                + " LEFT JOIN item.storageUnit as storageUnit WITH storageUnit.isActive = true AND storageUnit.isDeleted = false "
				+ " left JOIN prescriptionDetail.prescription AS prescription WITH prescription.isActive = true AND prescription.isDeleted = false "
				+ " WHERE (:prescriptionId = prescription.id) AND prescriptionDetail.isActive = true AND prescriptionDetail.isDeleted = false "
				+ " ORDER BY prescriptionDetail.id ";

		
		Query query = getSession().createQuery(hql);
		query.setParameter("prescriptionId", prescriptionId);
		query.setResultTransformer(Transformers.aliasToBean(PrescriptionDetailItemResponse.class));
		
		return query.list();
	}

	@Override
	public List<SalesWorkQty> getBatchIdBP(List<Integer> itemIds, Long saleId) {
		String hql = "SELECT batch.id as qty, item.id as itemId, "
				+ " detail.sale.id as saleId, uomType.id as selectedUomTypeId, "
				+ " uomUnit.id as selectedUomUnitId "
				+ " FROM SaleDetail AS detail " 
				+ " LEFT JOIN detail.batch AS batch  "
				+ " INNER JOIN detail.prescriptionDetail AS prescriptionDetail  "
				+ " INNER JOIN prescriptionDetail.item AS item  "
				+ " LEFT JOIN detail.uomType AS uomType "
				+ " LEFT JOIN detail.uomUnit AS uomUnit "
				+ " WHERE item.id in (:itemId) AND detail.sale.id = :saleId " 
				+ " ORDER BY detail.id DESC";

		Query query = getSession().createQuery(hql);
		query.setParameterList("itemId", itemIds);
		query.setParameter("saleId", saleId);
		query.setResultTransformer(Transformers.aliasToBean(SalesWorkQty.class));
		return query.list();
	}

	@Override
	public List<SalesWorkQty> getDispensedQuantityBP(List<Long> itemIds, Integer unitId) {
		String hql = "SELECT COALESCE(SUM(saleDetail.quantity), 0) as qty, prescriptionDetail.id as detailId "
				+ " FROM SaleDetail AS saleDetail \r\n"
				+ " INNER JOIN saleDetail.prescriptionDetail AS prescriptionDetail WITH prescriptionDetail.isActive = true AND prescriptionDetail.isDeleted = false "
				+ " WHERE saleDetail.isActive = true AND saleDetail.isDeleted = false AND saleDetail.unit.unitId = :unitId AND (prescriptionDetail.id in (:id)) AND prescriptionDetail.isActive = true AND prescriptionDetail.isDeleted = false "
				+ "  GROUP BY prescriptionDetail.id";

		Query query = getSession().createQuery(hql);
		query.setParameterList("id", itemIds);
		query.setParameter("unitId", unitId!=null ? unitId : sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(SalesWorkQty.class));
		return query.list();
	}

	@Override
	public List<FrequencyResponse> prescriptionFreqDetailByPresDetailIdBP(List<Long> itemIds) {
		String hql = "SELECT CONCAT(prescriptionFrequencyDetail.dose,' ',strengthUnit.units) as dose,"
				+ " frequency.description as frequency, prescriptionFrequencyDetail.instructions as instruction,"
				+ " prescriptionDetail.id as detailId "
				+ " FROM PrescriptionFrequencyDetail AS prescriptionFrequencyDetail "
				+ " INNER JOIN prescriptionFrequencyDetail.prescriptionDetail AS prescriptionDetail WITH prescriptionDetail.isActive = true AND prescriptionDetail.isDeleted = false "
				+ " INNER JOIN prescriptionFrequencyDetail.strengthUnit as strengthUnit "
				+ " INNER JOIN prescriptionFrequencyDetail.frequency as frequency "
				+ " WHERE (prescriptionDetail.id in (:id)) AND prescriptionFrequencyDetail.isActive = true AND prescriptionFrequencyDetail.isDeleted = false ";

		Query query = getSession().createQuery(hql);
		query.setParameterList("id", itemIds);
		query.setResultTransformer(Transformers.aliasToBean(FrequencyResponse.class));
		return query.list();
	}

	@Override
	public List<SaleDetailResp> getSaleDetailBP(List<Long> detailIds) {
		try {
			String hql = "SELECT CONCAT(batch.batchNo, ' | ', batch.expiryDate) as batchDetails, prescriptionDetail.id as detailId "
					+ " FROM SaleDetail AS saleDetail "
					+ " LEFT JOIN saleDetail.prescriptionDetail AS prescriptionDetail WITH prescriptionDetail.isActive = true AND prescriptionDetail.isDeleted = false "
					+ " INNER JOIN saleDetail.batch batch "
					+ " WHERE (prescriptionDetail.id in (:prescriptionDetailId)) AND saleDetail.isActive = true AND saleDetail.isDeleted = false "
					+ " order by saleDetail.id desc ";

			Query query = getSession().createQuery(hql);
			//query.setMaxResults(1);
			query.setParameterList("prescriptionDetailId", detailIds);
			query.setResultTransformer(Transformers.aliasToBean(SaleDetailResp.class));
			return query.list();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public List<prescriptionDetailResp> prescriptionDetailsByIdValueBP(List<Long> saleDetailsIds) {
		String hql = "SELECT prescription.prescriptionDate as prescriptionDate, prescription.id as prescriptionId, "
				+ " priority.priorityDesc as desc, saleDetail.id as saleDetailId "
				+ " FROM SaleDetail AS saleDetail "
				+ " INNER JOIN saleDetail.prescriptionDetail as prescriptionDetail "
				+ " INNER JOIN prescriptionDetail.prescription AS prescription WITH prescription.isActive = true AND prescription.isDeleted = false "
				+ " INNER JOIN prescription.priority as priority "
				+ " WHERE prescriptionDetail.isActive = true AND prescriptionDetail.isDeleted = false "
				+ " AND saleDetail.id in (:saleDetailsIds)";

		Query query = getSession().createQuery(hql);
		query.setParameterList("saleDetailsIds", saleDetailsIds);
		query.setResultTransformer(Transformers.aliasToBean(prescriptionDetailResp.class));
		return query.list();
	}

	@Override
	public List<prescriptionDetailResp> getSaleDetailBYSaleIdBP(List<Long> saleIds) {
	
		String hql = "SELECT saleDetail.id as saleDetailId, sale.id as saleId "
				+ " FROM SaleDetail AS saleDetail "
				+ " INNER JOIN saleDetail.sale sale WITH sale.id in (:saleIds) ";
				/*+ " INNER JOIN saleDetail.prescriptionDetail as prescriptionDetail WITH prescriptionDetail.isActive = true AND prescriptionDetail.isDeleted = false "
				+ " INNER JOIN prescriptionDetail.prescription AS prescription WITH prescription.isActive = true AND prescription.isDeleted = false "
				+ " INNER JOIN prescription.priority as priority ";*/
		
		Query query = getSession().createQuery(hql);
		query.setParameterList("saleIds", saleIds);
		query.setResultTransformer(Transformers.aliasToBean(prescriptionDetailResp.class));
		return query.list();
	}

	@Override
	public List<HandoverDetailItem> getSaleDetailBYSaleIdBP(Long id) {
	
		String hql = "SELECT saleDetail.id as id, item.code as itemCode, item.name as itemName, item.id as itemId, "
				+ " manu.name as manufacturerName, manu.id as manufacturerId, item.isOtc as handOverFlag, "
				+ " uomUnit.units as uomUnit, prescriptionDetail.priorityId as priorityId, "
				+ " uomUnit.id as uomUnitId, prescriptionDetail.orderQty as quantityOrdered, saleDetail.totalAmount as amount, "
				+ " batch.batchNo as batchNo, batch.expiryDate as expiryDate, "
				+ " saleDetail.quantity as quantityIssued, saleDetail.rate as rate, "
				+ " saleDetail.netAmount as totalAmount, saleDetail.discountAmount as discount, "
				+ " status.status as status, status.id as statusId, "
				+ " prescriptionDetail.id as prescriptionDetailId "
				+ " FROM SaleDetail AS saleDetail "
				+ " INNER JOIN saleDetail.sale sale WITH sale.id = :saleId "
				+ " INNER JOIN saleDetail.prescriptionDetail as prescriptionDetail "
				+ " INNER JOIN prescriptionDetail.item as item "
				+ " INNER JOIN item.manufacturer as manu "
				+ " INNER JOIN saleDetail.status as status "
				+ " LEFT JOIN saleDetail.uomUnit as uomUnit "
				//+ " LEFT JOIN uomUnit.unit as unit "
				+ " LEFT JOIN saleDetail.batch as batch ";
				
		
		Query query = getSession().createQuery(hql);
		query.setParameter("saleId", id);
		query.setResultTransformer(Transformers.aliasToBean(HandoverDetailItem.class));
		return query.list();
	}

	@Override
	public List<PharmacologyItem> getSubstituteByIdAssignPickerBP(Integer id, Integer itemId) {
		String hql = "SELECT item.id as itemId, item.code as itemCode, item.name as itemName, "
				+ " generic.name as genericName, generic.id as genericId, 'Rack-001' as rackNo "
				+ " FROM Item AS item "
				+ " INNER JOIN item.generic AS generic WITH generic.isActive = true AND generic.isDeleted = false "
				+ " WHERE (:id = generic.id) AND item.isActive = true AND item.isDeleted = false AND item.id != :itemId";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setParameter("itemId", itemId);
		query.setResultTransformer(Transformers.aliasToBean(PharmacologyItem.class));
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ResponseWorkListSearch> worklistSearchByFieldsAndDepartment(WorkListSearch WorkListSearch,
			Integer screenId, List<Integer> deptId, Integer unitId) {
		try 
		{
			/*
			 * String hql =
			 * "SELECT prescription.id as id, visitType.visitTypeCode as visitType, " +
			 * " encounter.encounterId as encounterId, " +
			 * " prescription.prescriptionDate as prescriptionDateTime, " +
			 * " priority.priorityDesc as priority, status.status as status, encounter.createdDate as visitDateTime, "
			 * + " doctor.doctorId as doctorId, " +
			 * " CONCAT(doctor.firstName, ' ', doctor.middleName, ' ', doctor.lastName, ' | ') as doctorDetail, "
			 * +
			 * " prescription.prescriptionNo as prescriptionNo, status.id as statusId, specialityMaster.specialityName as specialityName, "
			 * + " patientEncounter.dob as patientDob, " +
			 * " CONCAT(prefixEncounter.prefixCode, ' ', patientEncounter.firstName, ' ', patientEncounter.middleName, "
			 * +
			 * " patientEncounter.lastName, ' | ',patient.uhidNumber, ' | ', gender.genderCode) as patientDetail, "
			 * +
			 * " CONCAT(prefixEncounter.prefixCode, ' ', patientEncounter.firstName, ' ', patientEncounter.middleName, "
			 * + " patientEncounter.lastName) as patientName, " +
			 * " patientEncounter.uhidNumber as uhid, " +
			 * " patientEncounter.gender.genderId as genderId, " +
			 * " patientEncounter.gender.genderCode as genderCode, " +
			 * " concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, patientEncounter.dob)) , 'Y ' ) as ageStringDet, "
			 * + " paymentEntitlementMaster.paymentEntitlementDesc as entitlementDesc, " +
			 * " patientEncounter.mobileNumber as mobileNumber," +
			 * " patientEncounter.imagePath as imagePath " +
			 * " FROM Prescription AS prescription " +
			 * " INNER JOIN prescription.status as status WITH status.isActive = true AND status.isDeleted = false "
			 * +
			 * " INNER JOIN prescription.visitType as visitType WITH visitType.status = 'A' "
			 * + " left JOIN prescription.priority AS priority WITH priority.status = 'A'" +
			 * " LEFT JOIN prescription.specialityMaster AS specialityMaster WITH specialityMaster.status = 'A'"
			 * + " LEFT JOIN prescription.doctor AS doctor WITH doctor.status = 'A'" +
			 * " INNER JOIN prescription.encounter as encounter WITH encounter.status = 'A'"
			 * +
			 * " Left join  encounter.patientSponsorDetailsList  encpatientSponsorDetailsList with encpatientSponsorDetailsList.priorityId = 1 and encpatientSponsorDetailsList.status='A'"
			 * +
			 * " left join encpatientSponsorDetailsList.paymentEntitlementMaster paymentEntitlementMaster "
			 * +
			 * " INNER JOIN encounter.patient as patientEncounter WITH patientEncounter.status = 'A' "
			 * + " INNER JOIN patientEncounter.gender gender " //+
			 * " INNER JOIN prescription.specialityMaster dept" +
			 * " LEFT JOIN patientEncounter.prefix AS prefixEncounter  " +
			 * " WHERE prescription.isActive = true AND prescription.isDeleted = false AND prescription.prescriptionDate BETWEEN  :fromDate AND :toDate AND prescription.isNonFormulary = 'N' "
			 * +
			 * " AND prescription.unit.unitId = :unitId AND ( visitType.visitTypeId = :visitTypeId OR :visitTypeId = 0 ) AND visitType.visitTypeCode != 'IP' "
			 * + " AND ( status.id = :statusId OR :statusId = 0 )" +
			 * " and (select count(*) from prescription.prescriptionDetailsList presDtl where presDtl.prescription.id=prescription.id ) != "
			 * +
			 * "	(select count(*) from prescription.prescriptionDetailsList presDtl1 where presDtl1.prescription.id=prescription.id and presDtl1.statFlag=true)"
			 * + " AND ((:uhid = '' OR lower(patientEncounter.uhidNumber) LIKE :uhid))" +
			 * " AND ((:doctorName = '' OR lower(COALESCE(CONCAT(doctor.firstName,' ',doctor.middleName,' ',doctor.lastName))) LIKE :doctorName OR lower(doctor.firstName) LIKE :doctorName OR lower(doctor.middleName) LIKE :doctorName "
			 * + "	OR lower(doctor.lastName) LIKE :doctorName))" +
			 * " AND ( :patientId = 0 OR patientEncounter.patientId = :patientId ) " +
			 * "	AND ((:patientId != 0 AND :patientName != '') OR (:patientName = '' OR lower(COALESCE(CONCAT(prefixEncounter.prefixCode,' ',patientEncounter.firstName,' ',patientEncounter.middleName,' ', patientEncounter.lastName))) LIKE :patientName "
			 * + " OR lower(prefixEncounter.prefixCode) LIKE :patientName OR " +
			 * "	lower(patientEncounter.firstName) LIKE :patientName OR lower(patientEncounter.middleName) LIKE :patientName "
			 * + "	OR lower(patientEncounter.lastName) LIKE :patientName)) " +
			 * " AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus " +
			 * " INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId " +
			 * "	INNER JOIN screenStatus.status as status) " +
			 * " AND specialityMaster.specialityId in (:deptId)" + " ORDER BY  :orderby ";
			 * 
			 * Query query = getSession().createQuery(hql); query.setParameter("fromDate",
			 * Util.getDate(WorkListSearch.getFromDate())); query.setParameter("toDate",
			 * Util.getDate(WorkListSearch.getToDate())); query.setParameter("statusId",
			 * WorkListSearch.getStatusId() != null ? WorkListSearch.getStatusId() : 0);
			 * query.setParameter("visitTypeId", WorkListSearch.getVisitTypeId() != null ?
			 * WorkListSearch.getVisitTypeId() : 0); query.setParameter("uhid",
			 * WorkListSearch.getUhid() != null ? '%'+WorkListSearch.getUhid().toLowerCase()
			 * + '%' : ""); query.setParameter("patientId", WorkListSearch.getPatientId() !=
			 * null ? WorkListSearch.getPatientId() : 0); query.setParameter("patientName",
			 * WorkListSearch.getPatientName() != null ?
			 * '%'+WorkListSearch.getPatientName().toLowerCase() + '%' : "");
			 * query.setParameter("doctorName", WorkListSearch.getDoctorName() != null ?
			 * '%'+WorkListSearch.getDoctorName().toLowerCase() + '%' : "");
			 * query.setParameter("unitId", unitId!=null ? unitId
			 * :sessionService.getUnitId()); query.setParameter("screenId", screenId);
			 * query.setParameterList("deptId", deptId); query.setParameter("orderby",
			 * "FIELD(priority.priorityDesc, 'High','Normal','Low'), prescription.id DESC");
			 * query.setResultTransformer(Transformers.aliasToBean(ResponseWorkListSearch.
			 * class));
			 */
			 StringBuilder sb = new  StringBuilder();
			 sb.append(" select " + 
							"	prescripti0_.id as \"id\", " + 
							"	visittype2_.visit_type_code as \"visitType\", " + 
							"	encounter6_.encounter_id as \"encounterId\", " + 
							"	prescripti0_.prescription_date as \"prescriptionDateTime\", " + 
							"	priority3_.priority_desc as \"priority\", " + 
							"	status1_.status as \"status\", " + 
							"	encounter6_.created_date as \"visitDateTime\", " + 
							"	doctor5_.doctor_id as \"doctorId\", " + 
							"	CONCAT(doctor5_.first_name, ' ', doctor5_.middle_name, ' ', doctor5_.last_name , ' | ') as \"doctorDetail\", " + 
							"	prescripti0_.prescription_no as \"prescriptionNo\", " + 
							"	status1_.id as \"statusId\", " + 
							"	patientreg9_.dob as \"patientDob\", " + 
							"	speciality4_.speciality_name as \"specialityName\", " + 
							"	CONCAT(prefix11_.prefix_code , ' ', patientreg9_.first_name, ' ', patientreg9_.middle_name, ' ',patientreg9_.last_name,' | ',patientreg9_.uhid_number,' | ',gender10_.gender_code) as \"patientDetail\", " +
							"	CONCAT(prefix11_.prefix_code , ' ', patientreg9_.first_name, ' ', patientreg9_.middle_name, ' ',patientreg9_.last_name) as \"patientName\", " + 
							"	patientreg9_.uhid_number as \"uhid\", " + 
							"	patientreg9_.gender_id as \"genderId\", " + 
							"	gender12_.gender_code as \"genderCode\", " + 
							"	(extract( year from  age(CURRENT_TIMESTAMP, patientreg9_.dob)) , 'Y ' ) as \"ageStringDet\", " + 
							"	patientreg9_.image_path as \" imagePath\", " + 
							"	patientreg9_.mobile_number as \"mobileNumber\", " + 
							"	patientreg9_.patient_id as \" patientId\", " + 
							"	paymentent8_.payment_entitlement_desc as \"entitlementDesc\" " + 
							"from " + 
							"	pharmacy.t_prescription prescripti0_ " + 
							"inner join public.m_status status1_ on " + 
							"	prescripti0_.status_id = status1_.id " + 
							"	and (status1_.is_active = true " + 
							"		and status1_.is_deleted = false) " + 
							"inner join public.m_visit_type_master visittype2_ on " + 
							"	prescripti0_.visit_type_id = visittype2_.visit_type_id " + 
							"	and (visittype2_.status = 'A') " + 
							"left outer join adt.m_priority_master priority3_ on " + 
							"	prescripti0_.priority_id = priority3_.priority_id " + 
							"	and (priority3_.status = 'A') " + 
							"left outer join doctor.m_speciality_master speciality4_ on " + 
							"	prescripti0_.dept_id = speciality4_.speciality_id " + 
							"	and (speciality4_.status = 'A') " + 
							"left outer join doctor.m_doctor_master doctor5_ on " + 
							"	prescripti0_.doctor_id = doctor5_.doctor_id " + 
							"	and (doctor5_.status = 'A') " + 
							"inner join opd.t_encounter_master encounter6_ on " + 
							"	prescripti0_.encounter_id = encounter6_.encounter_id " + 
							"	and (encounter6_.status = 'A') " + 
							"left outer join opd.t_patient_sponsor_details patientspo7_ on " + 
							"	encounter6_.encounter_id = patientspo7_.encounter_id " + 
							"	and (patientspo7_.priority_id = 1 " + 
							"		and patientspo7_.status = 'A') " + 
							"left outer join public.m_payment_entitlement_master paymentent8_ on " + 
							"	patientspo7_.payment_entitlement_id = paymentent8_.payment_entitlement_id " + 
							"inner join patient.t_patient_registration patientreg9_ on " + 
							"	encounter6_.patient_id = patientreg9_.patient_id " + 
							"	and (patientreg9_.status = 'A') " + 
							"inner join public.m_gender_master gender10_ on " + 
							"	patientreg9_.gender_id = gender10_.gender_id "
							+ "left join  " + 
							"	(select count(*) p_count , prescription_id  " + 
							"	from pharmacy.t_prescription_detail group by prescription_id )  " + 
							"	prescripti13_ on prescripti0_.id = prescripti13_.prescription_id  " + 
							" left join  " + 
							"	(select count(*) p_count , prescription_id  " + 
							"	from pharmacy.t_prescription_detail where statflag = true  " + 
							"	group by prescription_id ) " + 
							"	prescripti14_ on prescripti0_.id = prescripti14_.prescription_id 	 " + 
							" left outer join public.m_prefix_master prefix11_ on " + 
							"	patientreg9_.prefix_id = prefix11_.prefix_id " + 
							" left join public.m_gender_master gender12_ on patientreg9_.gender_id = gender12_.gender_id " + 
							"where  prescripti0_.is_active = true " + 
							"	and prescripti0_.is_deleted = false " + 
							"	and prescripti0_.is_non_formulary = 'N' " + 
							"	and prescripti0_.unit_id = :unitId " + 
							"	and (status1_.id in ( " + 
							"	select " + 
							"		status17_.id " + 
							"	from " + 
							"		public.m_screen_status_mapper screenstat15_ " + 
							"	inner join public.m_screen screen16_ on " + 
							"		screenstat15_.screen_id = screen16_.id " + 
							"		and (screen16_.id = :screenId) " + 
							"	inner join public.m_status status17_ on " + 
							"		screenstat15_.status_id = status17_.id)) "
							+ "and speciality4_.speciality_id in (:deptId)  ");
			 
			 if(WorkListSearch.getFromDate()!=null)
		        {
		        	sb.append("	and to_char(prescripti0_.prescription_date,'YYYY-MM-DD') >=:fromDate ");
		        }
		        if(WorkListSearch.getToDate()!=null)
		        {
		        	sb.append("	and to_char(prescripti0_.prescription_date,'YYYY-MM-DD') <=:toDate ");
		        }
			 
			 if(WorkListSearch.getDoctorName()!=null)
		        {
		        	sb.append(" and ( lower(coalesce((doctor5_.first_name ,' ', doctor5_.middle_name ,' ', doctor5_.last_name))) like concat('%',LOWER(:doctorName),'%') " +
		        			"	or lower(doctor5_.first_name) like concat('%',LOWER(:doctorName),'%')  " +
		        			"	or lower(doctor5_.middle_name) like concat('%',LOWER(:doctorName),'%') " +
		        			"	or lower(doctor5_.last_name) like concat('%',LOWER(:doctorName),'%') ) ");
		        }
			 if(WorkListSearch.getDoctorName()!=null)
		        {
				 sb.append(" and ( lower(coalesce((prefix11_.prefix_code ,' ', patientreg9_.first_name ,' ', patientreg9_.middle_name ,' ', patientreg9_.last_name))) like concat('%',LOWER(:patientName),'%') " +
				 		"	or lower(prefix11_.prefix_code) like  concat('%',LOWER(:patientName),'%') " +
				 		"	or lower(patientreg9_.first_name) like concat('%',LOWER(:patientName),'%')  " +
				 		"	or lower(patientreg9_.middle_name) like concat('%',LOWER(:patientName),'%')  " +
				 		"	or lower(patientreg9_.last_name) like concat('%',LOWER(:patientName),'%')) ");
		        }
			 if(WorkListSearch.getVisitTypeId()!= null) {
				 sb.append(" and visittype2_.visit_type_id =:visitTypeId ");
			 }
			 if(WorkListSearch.getVisitTypeId()!= null) {
				 sb.append(" and patientreg9_.patient_id =:patientId ");
			 }
			 if(WorkListSearch.getVisitTypeId()!= null) {
				 sb.append(" and status1_.id =:statusId ");
			 }
			 if(WorkListSearch.getVisitTypeId()!= null) {
				 sb.append(" and lower(patientreg9_.uhid_number) like  concat('%',LOWER(:uhid),'%')");
			 }
					
					 Query createdQuery=sessionFactory.getCurrentSession().createSQLQuery(sb.toString());
				       
			       Query sql = createdQuery
					.setParameter("unitId", unitId!=null ? unitId :sessionService.getUnitId())
					.setParameter("screenId", screenId)
					.setParameterList("deptId" , deptId);
			if(WorkListSearch.getFromDate()!=null)
			{
				sql.setParameter("fromDate", WorkListSearch.getFromDate());
			}
			if(WorkListSearch.getToDate()!=null)
			{
				sql.setParameter("toDate", WorkListSearch.getToDate());
			}
			if(WorkListSearch.getDoctorName()!=null)
			{
				sql.setParameter("doctorName", WorkListSearch.getDoctorName().toLowerCase());
			}
			if(WorkListSearch.getDoctorName()!=null)
			{
				sql.setParameter("patientName", WorkListSearch.getPatientName().toLowerCase());
			}
			if(WorkListSearch.getVisitTypeId()!= null) {
				sql.setParameter("visitTypeId", WorkListSearch.getVisitTypeId());
			}
			if(WorkListSearch.getVisitTypeId()!= null) {
				sql.setParameter("patientId", WorkListSearch.getPatientId());
			}
			if(WorkListSearch.getVisitTypeId()!= null) {
				sql.setParameter("statusId", WorkListSearch.getStatusId());
			}
			if(WorkListSearch.getVisitTypeId()!= null) {
				sql.setParameter("uhid", WorkListSearch.getUhid());
			}
			List<ResponseWorkListSearch> listLabSampleMasterDto = sql.setResultTransformer(Transformers.aliasToBean(ResponseWorkListSearch.class)).list();
			        
			        
			        
			return listLabSampleMasterDto;
			
			

		
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Prescription getSalesWorkList(Integer id) {
		String hql = "SELECT prescription FROM Prescription AS prescription "
				+ " WHERE (:id = prescription.id) AND prescription.isActive = true AND prescription.isDeleted = false ";
				

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id.longValue());
		
		return (Prescription) query.uniqueResult();
	}

	@Override
	public Prescription updateSalesWorkList(Prescription prescription) {
		getSession().update(prescription);
		return prescription;
	}


    @Override
    public List<PrescriptionDetailItemResponse> getissuedItemList(Long prescriptionId) {
        
        String hql = " select  priDetail.parent_substitute_id as \"parentSubstituteId\", " + 
        		"	  primstr.id \"tempPrescriptionId\" , "
        		+ " issu.id as  \"issueId\", " + 
                "     issDetial.id as \"issueDetailId\", " + 
                "     issDetial.batch_id as \"batchId\", " + 
                "     issDetial.uom_type_id as \"uomTypeId\", " + 
                "     issDetial.uom_unit_id as \"uomUnitId\", " + 
                "     priDetail.order_qty as \"qtyPrescribed\", " + 
                "    issDetial.issue_quantity as \"issuedQty\", " + 
                "    saleDtl.quantity as \"quantityDispensedTemp\", " + 
                "    priDetail.id as \"pricId\", " + 
                "    item.id as \"itemId\", " + 
                "    item.code as \"itemCode\", " + 
                "    item.name as \"itemName\", " + 
                "    item.is_three_uom_applicable as \"isThreeUOMApplicable\", " + 
                "    item.storage_unit_id as \"storageUnitId\", " +
                "    storageUnit.name as \"storageUnitName\", " +
                "    item.is_narcotic as \"isNarcotic\", " + 
                "    item.formulation_type_id as \"formulationTypeId\", " + 
                "    item.generic_id as \"genericId\", " + 
                "    genr.name as \"genericName\", " + 
                "    item.manufacturer_id as \"manufacturerId\", " + 
                "    mabufac.name as \"manufacturerName\", " + 
                "    tax.id as \"taxId\", " + 
                "    tax.name as \"tax\", " + 
                "    genr.product_category_id as \"genProdCatId\", " + 
                "    item.product_category_id as \"productCatId\", " + 
                "    proCat.asset_category_id as \"assetCategoryId\", " +  //f.description as \"frequencyDesc"\
                "   f.description as \"frequencyDesc\", " +
                "   fd.start_date as \"startDate\", " +
                "   fd.stop_date as \"stopDate\", " +
                "    status.id as \"statusId\", "
                + " issDetial.consession_amount as \"patientClassAmount\", " 
                + " issDetial.consession_percentage as \"patientClassPercentage\", " + 
                 "  saledtl.net_amount as \"netAmount\", saledtl.credit_payable as \"creditPayable\", saledtl.self_payable as \"selfPayable\","
                + "  issDetial.discount_amount as \"discountAmount\",  saledtl.tax_amount as \"taxAmount\", "
                + "  saledtl.tax_percentage as \"taxPercentage\", saledtl.total_amount as \"totalAmount\","
                + "	 coalesce(saledtl.package_consumption_amt,0) as \"packageConsumptionAmt\"," +
                "    status.status as \"status\","
                + "case " + 
                "		when priDetail.instruction_id is null then priDetail.instruction_desc " + 
                "		else instructio.instruction_desc " + 
                "	end as \"instructionDesc\" , " + 
                " saleDtl.at_cop as \"atCop\" ," 
                + " presDose.id as \"prescribedDoseUnitId\", " + 
                "	presDose.unit as \"units\", " + 
                "	priDetail.prescribe_dose as \"prescribedDose\", " + 
                "	priDetail.duration as \"duration\", " + 
                "	priDetail.duration_unit as \"durationUnit\" " +
                "     from  pharmacy.t_issue  issu "
                + " inner join pharmacy.t_prescription primstr on issu.prescription_id = primstr.id " + 
                "    inner join pharmacy.t_issue_detail  issDetial on issu.id= issDetial.issue_id " + 
                "    inner join pharmacy.t_prescription_detail  priDetail on issDetial.prescription_detail_id = priDetail.id "
                + " left join public.m_instruction instructio on priDetail.instruction_id = instructio.instruction_id" + 
                "    inner join public.m_item item on priDetail.item_id = item.id and item.is_active = true and item.is_deleted = false " + 
                "    left join public.m_generic genr on genr.id = item.generic_id " + 
                "    left join public.m_product_category proCat on genr.product_category_id =  proCat.id " + 
                "   left join public.m_storage_unit storageUnit on storageUnit.id = item.storage_unit_id " +
               "    left join pharmacy.t_prescription_frequency_detail fd on fd.prescription_detail_id = priDetail.id  " + 
                "    left join public.m_frequency f on f.id =  fd.frequency_id " + 
                
                
                "    left join public.m_manufacturer mabufac on  mabufac.id = item.manufacturer_id " + 
                "    inner join public.m_tax  tax on tax.id = item.s_tax_id  " + 
                "    inner join  pharmacy.t_sale_detail saleDtl on issDetial.sale_detail_id = saleDtl.id " + 
                "    inner join public.m_status status on issDetial.status_id = status.id "
                + "  left join public.m_strength_unit presDose on " + 
                "	priDetail.prescribe_dose_unit_id = presDose.id " + 
                "    where issu.prescription_id = :prescriptionId and issDetial.is_substitute = false "
                + " union "
                + " select " + 
                "	0 as \"parentSubstituteId\"," + 
                "	0 as  \"tempPrescriptionId\"," + 
                "	issu.id as \"issueId\"," + 
                "	issDetial.id as \"issueDetailId\"," + 
                "	issDetial.batch_id as \"batchId\"," + 
                "	issDetial.uom_type_id as \"uomTypeId\"," + 
                "	issDetial.uom_unit_id as \"uomUnitId\"," + 
                "	0 as \"qtyPrescribed\"," + 
                "	issDetial.issue_quantity as \"issuedQty\"," + 
                "	saleDtl.quantity as \"quantityDispensedTemp\"," + 
                "	0 as \"pricId\"," + 
                "	item.id as \"itemId\"," + 
                "	item.code as \"itemCode\"," + 
                "	item.name as \"itemName\"," + 
                "    item.is_three_uom_applicable as \"isThreeUOMApplicable\", " + 
                "    item.storage_unit_id as \"storageUnitId\", " +
                "    storageUnit.name as \"storageUnitName\", " +
                "    item.is_narcotic as \"isNarcotic\", " + 
                "    item.formulation_type_id as \"formulationTypeId\", " + 
                "	item.generic_id as \"genericId\"," + 
                "	genr.name as \"genericName\"," + 
                "	item.manufacturer_id as \"manufacturerId\"," + 
                "	mabufac.name as \"manufacturerName\"," + 
                "	tax.id as \"taxId\"," + 
                "	tax.name as \"tax\"," + 
                "	genr.product_category_id as \"genProdCatId\"," + 
                "	item.product_category_id as \"productCatId\"," + 
                "	proCat.asset_category_id as \"assetCategoryId\"," + 
                "   f.description as \"frequencyDesc\", " +
                "   fd.start_date as \"startDate\", " +
                "   fd.stop_date as \"stopDate\", " +
                "	status.id as \"statusId\"," + 
                " issDetial.consession_amount as \"patientClassAmount\", " 
                + " issDetial.consession_percentage as \"patientClassPercentage\", " + 
                "	saledtl.net_amount as \"netAmount\"," + 
                "	saledtl.credit_payable as \"creditPayable\"," + 
                "	saledtl.self_payable as \"selfPayable\"," + 
                "	issDetial.discount_amount as \"discountAmount\"," + 
                "	saledtl.tax_amount as \"taxAmount\"," + 
                "	saledtl.tax_percentage as \"taxPercentage\"," + 
                "	saledtl.total_amount as \"totalAmount\"," + 
                "	coalesce(" + 
                "		saledtl.package_consumption_amt," + 
                "		0" + 
                "	) as \"packageConsumptionAmt\"," + 
                "	status.status as \"status\", " + 
                "	'' as \"instructionDesc\" , " + 
                " saleDtl.at_cop as \"atCop\", "
                + " presDose.id as \"prescribedDoseUnitId\", " + 
                "	presDose.unit as \"units\", " + 
                "	priDetail.prescribe_dose as \"prescribedDose\", " + 
                "	priDetail.duration as \"duration\", " + 
                "	priDetail.duration_unit as \"durationUnit\" " +
                " from " + 
                "	pharmacy.t_issue issu " + 
                " inner join pharmacy.t_prescription primstr on " + 
                "	issu.prescription_id = primstr.id " + 
                
           " inner join pharmacy.t_prescription_detail priDetail  on " + 
           "	priDetail.prescription_id = primstr.id " + 
                
                " inner join pharmacy.t_issue_detail issDetial on " + 
                "	issu.id = issDetial.issue_id " + 
                " inner join pharmacy.t_sale_detail saleDtl on " + 
                "	issDetial.sale_detail_id = saleDtl.id " + 
                " inner join public.m_item item on " + 
                "	saleDtl.item_id = item.id " + 
                "	and item.is_active = true " + 
                "	and item.is_deleted = false " + 
                " left join public.m_generic genr on " + 
                "	genr.id = item.generic_id " + 
                " left join public.m_product_category proCat on " + 
                "	genr.product_category_id = proCat.id " + 
                "   left join public.m_storage_unit storageUnit on storageUnit.id = item.storage_unit_id " +
               "    left join pharmacy.t_prescription_frequency_detail fd on fd.prescription_detail_id = priDetail.id  " + 
                "    left join public.m_frequency f on f.id =  fd.frequency_id " + 
            
                " left join public.m_manufacturer mabufac on " + 
                "	mabufac.id = item.manufacturer_id " + 
                " inner join public.m_tax tax on " + 
                "	tax.id = item.s_tax_id " + 
                " inner join public.m_status status on" + 
                "	issDetial.status_id = status.id "
                + " left join public.m_strength_unit presDose on " + 
                "	priDetail.prescribe_dose_unit_id = presDose.id " + 
                " where " + 
                "	issu.prescription_id =:prescriptionId " + 
                "	and issDetial.is_substitute = false " + 
                "	and issDetial.prescription_detail_id is null ";

        Query query = getSession().createSQLQuery(hql);
        query.setParameter("prescriptionId", prescriptionId);
        query.setResultTransformer(Transformers.aliasToBean(PrescriptionDetailItemResponse.class));
        return query.list();
    }

	@Override
	public List<PharmacologyItem> getissuedItemListForNewIssueItems(Integer id) {
        
        String hql = " select  priDetail.parent_substitute_id as \"parentSubstituteId\", " + 
        		"	  primstr.id \"tempPrescriptionId\" , "
        		+ " issu.id as  \"issueId\", " + 
                "     issDetial.id as \"issueDetailId\", " + 
                "     saleDtl.batch_id as \"batchIdNew\", " + 
                "     saleDtl.uom_type_id as \"uomTypeId\", " + 
                "     saleDtl.uom_unit_id as \"uomUnitId\", " + 
                "     priDetail.order_qty as \"qtyPrescribed\", " + 
                "    issDetial.issue_quantity as \"issuedQty\", " + 
                "    saleDtl.quantity as \"quantityDispensedTemp\", " + 
                "    priDetail.id as \"pricId\", " + 
                "    item.id as \"itemId\", " + 
                "    item.code as \"itemCode\", " + 
                "    item.name as \"itemName\", " + 
                "    item.is_three_uom_applicable as \"isThreeUOMApplicable\", " +
                "    item.storage_unit_id as \"storageUnitId\", " +
                "    storageUnit.name as \"storageUnitName\", " +
                "    item.is_narcotic as \"isNarcotic\", " + 
                "    item.formulation_type_id as \"formulationTypeId\", " + 
                "    item.generic_id as \"genericId\", " + 
                "    genr.name as \"genericName\", " + 
                "    item.manufacturer_id as \"manufacturerId\", " + 
                "    mabufac.name as \"manufacturerName\", " + 
                "    tax.id as \"taxId\", " + 
                "    tax.name as \"tax\", " + 
                "    genr.product_category_id as \"genProdCatId\", " + 
                "    item.product_category_id as \"productCatId\", " + 
                "    proCat.asset_category_id as \"assetCategoryId\", " +  //f.description as \"frequencyDesc"\
                "   f.description as \"frequencyDesc\", " +
                "   fd.start_date as \"startDate\", " +
                "   fd.stop_date as \"stopDate\", " +
                "    status.id as \"statusId\", "
                + " issDetial.consession_amount as \"patientClassAmount\", " 
                + " saledtl.rate as rate, "
                + " issDetial.consession_percentage as \"patientClassPercentage\", " + 
                 "  saledtl.net_amount as \"netAmount\", saledtl.credit_payable as \"creditPayable\", saledtl.self_payable as \"selfPayable\","
                + "  issDetial.discount_amount as \"discountAmount\",  saledtl.tax_amount as \"taxAmount\", "
                + "  saledtl.tax_percentage as \"taxPercentage\", saledtl.total_amount as \"totalAmount\","
                + "	 coalesce(saledtl.package_consumption_amt,0) as \"packageConsumptionAmt\"," +
                "    status.status as \"status\","
                + "case " + 
                "		when priDetail.instruction_id is null then priDetail.instruction_desc " + 
                "		else instructio.instruction_desc " + 
                "	end as \"instructionDesc\" " + 
                "     from  pharmacy.t_issue  issu "
                + " inner join pharmacy.t_prescription primstr on issu.prescription_id = primstr.id " + 
                "    inner join pharmacy.t_issue_detail  issDetial on issu.id= issDetial.issue_id " + 
                "    inner join pharmacy.t_prescription_detail  priDetail on issDetial.prescription_detail_id = priDetail.id "
                + " left join public.m_instruction instructio on priDetail.instruction_id = instructio.instruction_id" + 
                "    inner join public.m_item item on priDetail.item_id = item.id and item.is_active = true and item.is_deleted = false " + 
                "    left join public.m_generic genr on genr.id = item.generic_id " + 
                "    left join public.m_product_category proCat on genr.product_category_id =  proCat.id " +
                "    left join public.m_storage_unit storageUnit on storageUnit.id =  item.storage_unit_id " +

               "    left join pharmacy.t_prescription_frequency_detail fd on fd.prescription_detail_id = priDetail.id  " + 
                "    left join public.m_frequency f on f.id =  fd.frequency_id " + 
                
                
                "    left join public.m_manufacturer mabufac on  mabufac.id = item.manufacturer_id " + 
                "    inner join public.m_tax  tax on tax.id = item.s_tax_id  " + 
                "    inner join  pharmacy.t_sale_detail saleDtl on issDetial.sale_detail_id = saleDtl.id " + 
                "    inner join public.m_status status on issDetial.status_id = status.id " + 
                "    where issu.id = :id and issDetial.is_substitute = false and issu.is_active = true and issu.is_deleted = false "
                + " union "
                + " select " + 
                "	0 as \"parentSubstituteId\"," + 
                "	0 as  \"tempPrescriptionId\"," + 
                "	issu.id as \"issueId\"," + 
                "	issDetial.id as \"issueDetailId\"," + 
                "	saleDtl.batch_id as \"batchId\"," + 
                "	saleDtl.uom_type_id as \"uomTypeId\"," + 
                "	saleDtl.uom_unit_id as \"uomUnitId\"," + 
                "	0 as \"qtyPrescribed\"," + 
                "	issDetial.issue_quantity as \"issuedQty\"," + 
                "	saleDtl.quantity as \"quantityDispensedTemp\"," + 
                "	0 as \"pricId\"," + 
                "	item.id as \"itemId\"," + 
                "	item.code as \"itemCode\"," + 
                "	item.name as \"itemName\"," +
                "    item.is_three_uom_applicable as \"isThreeUOMApplicable\", " +
                "	item.storage_unit_id as \"storageUnitId\"," +
                "    storageUnit.name as \"storageUnitName\", " +
                "    item.is_narcotic as \"isNarcotic\", " + 
                "    item.formulation_type_id as \"formulationTypeId\", " + 
                "	item.generic_id as \"genericId\"," + 
                "	genr.name as \"genericName\"," + 
                "	item.manufacturer_id as \"manufacturerId\"," + 
                "	mabufac.name as \"manufacturerName\"," + 
                "	tax.id as \"taxId\"," + 
                "	tax.name as \"tax\"," + 
                "	genr.product_category_id as \"genProdCatId\"," + 
                "	item.product_category_id as \"productCatId\"," + 
                "	proCat.asset_category_id as \"assetCategoryId\"," + 
                "   f.description as \"frequencyDesc\", " +
                "   fd.start_date as \"startDate\", " +
                "   fd.stop_date as \"stopDate\", " +
                "	status.id as \"statusId\"," + 
                " issDetial.consession_amount as \"patientClassAmount\", " 
                + " saledtl.rate as rate, "
                + " issDetial.consession_percentage as \"patientClassPercentage\", " + 
                "	saledtl.net_amount as \"netAmount\"," + 
                "	saledtl.credit_payable as \"creditPayable\"," + 
                "	saledtl.self_payable as \"selfPayable\"," + 
                "	issDetial.discount_amount as \"discountAmount\"," + 
                "	saledtl.tax_amount as \"taxAmount\"," + 
                "	saledtl.tax_percentage as \"taxPercentage\"," + 
                "	saledtl.total_amount as \"totalAmount\"," + 
                "	coalesce(" + 
                "		saledtl.package_consumption_amt," + 
                "		0" + 
                "	) as \"packageConsumptionAmt\"," + 
                "	status.status as \"status\", " + 
                "	'' as \"instructionDesc\" " + 
                " from " + 
                "	pharmacy.t_issue issu " + 
                " inner join pharmacy.t_prescription primstr on " + 
                "	issu.prescription_id = primstr.id " + 
                
           " inner join pharmacy.t_prescription_detail priDetail  on " + 
           "	priDetail.prescription_id = primstr.id " + 
                
                " inner join pharmacy.t_issue_detail issDetial on " + 
                "	issu.id = issDetial.issue_id " + 
                " inner join pharmacy.t_sale_detail saleDtl on " + 
                "	issDetial.sale_detail_id = saleDtl.id " + 
                " inner join public.m_item item on " + 
                "	saleDtl.item_id = item.id " + 
                "	and item.is_active = true " + 
                "	and item.is_deleted = false " + 
                " left join public.m_generic genr on " + 
                "	genr.id = item.generic_id " + 
                " left join public.m_product_category proCat on " + 
                "	genr.product_category_id = proCat.id " + 
                "    left join public.m_storage_unit storageUnit on storageUnit.id =  item.storage_unit_id " +
               "    left join pharmacy.t_prescription_frequency_detail fd on fd.prescription_detail_id = priDetail.id  " + 
                "    left join public.m_frequency f on f.id =  fd.frequency_id " + 
            
                " left join public.m_manufacturer mabufac on " + 
                "	mabufac.id = item.manufacturer_id " + 
                " inner join public.m_tax tax on " + 
                "	tax.id = item.s_tax_id " + 
                " inner join public.m_status status on" + 
                "	issDetial.status_id = status.id " + 
                " where " + 
                "	issu.id =:id " + 
                "	and issDetial.is_substitute = false " + 
                "	and issDetial.prescription_detail_id is null and issu.is_active = true and issu.is_deleted = false ";

        Query query = getSession().createSQLQuery(hql);
        query.setParameter("id", id);
        query.setResultTransformer(Transformers.aliasToBean(PharmacologyItem.class));
        return query.list();
    }

	@Override
	public void inActiveSaleDetailForSubItems(Integer saleDetailId) {
		 String sql=" update pharmacy.t_sale_detail set is_active=false,is_deleted=true where id=:saleDetailId ";
		 Query query = getSession().createSQLQuery(sql);
		 query.setParameter("saleDetailId", saleDetailId);
		 query.executeUpdate();
	}

}
