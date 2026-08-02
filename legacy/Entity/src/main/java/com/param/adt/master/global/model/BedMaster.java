package com.param.adt.master.global.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.adt.admission.model.AdmissionDetails;
import com.param.adt.discharge.model.Discharge;
import com.param.adt.housekeeping.model.Housekeeping;
import com.param.adt.master.unit.model.BillingBedCategoryMaster;
import com.param.adt.master.unit.model.FloorMaster;
import com.param.adt.master.unit.model.RoomMaster;
import com.param.adt.master.unit.model.WardMaster;
import com.param.adt.transfer.model.Transfer;
import com.param.global.model.BedCategoryMaster;

@NamedQueries({ 
	
	@NamedQuery(name = "GET_ACTIVE_BED_LIST", query = "SELECT bed.bedId as bedId, "
		+ "bed.bedNumber as bedNumber, " 
		+ "bed.floorId as floorId, " 
		+ "bed.roomId as roomId, "
		+ "room.roomDesc as roomDesc, "
		+ "flr.floorName as floorName, "
		+ "bed.wardId as wardId, " 
		+ "ward.wardName as wardName, "
		+ "bed.bedCategoryId as bedCategoryId, "
		+ "bed.billingBedCategoryId as billingBedCategoryId, "
		+ "bbc.billingBedDesc as billingBedDesc, " 
		+ "bbc.hierarchyId as hierarchyId, " 
		+ "bc.bedCategoryDesc as bedCategoryDesc, "
		+ "bedlog.bedStatusId as bedStatusId, "
		+ "bedStatus.bedStatusDesc as bedStatusDesc " 
		+ "FROM BedMaster bed "
		+ "INNER JOIN bed.listBedLogStatus bedlog "
		+ "INNER JOIN bedlog.bedStatusMaster bedStatus " 
		+ "INNER JOIN bed.floorMaster flr "
		+ "INNER JOIN bed.wardMaster ward " 
		+ "INNER JOIN bed.bedCategoryMaster bc "
		+ "INNER JOIN bed.billingBedCategoryMaster bbc " 
		+ "INNER JOIN bed.roomMaster room " 
		+ " WHERE bed.unitId=:unitId "
		+ "AND bed.organizationId=:organizationId " 
		+ "AND bedlog.status='A' " 
		+ "AND bed.status='A' "
		+ "AND bed.bedStatusId=3 "
		+ "AND bedlog.bedStatusId=3 "//BedStatusId 3---> Ready For Use
		+ "AND ward.isEr ='N' "
		//+ "AND ward.isIcu='N' "HIS-24001
		),  
		//+ "AND bed.bedCategoryId=:bedCategoryId"),
	
	@NamedQuery(name = "GET_ACTIVE_LABOR_BED_LIST", query = "SELECT bed.bedId as bedId, "
			+ "bed.bedNumber as bedNumber, " 
			+ "bed.floorId as floorId, " 
			+ "bed.roomId as roomId, "
			+ "room.roomDesc as roomDesc, "
			+ "flr.floorName as floorName, "
			+ "bed.wardId as wardId, " 
			+ "ward.wardName as wardName, "
			+ "bedlog.bedStatusId as bedStatusId, "
			+ "bedStatus.bedStatusDesc as bedStatusDesc " 
			+ "FROM BedMaster bed "
			+ "INNER JOIN bed.listBedLogStatus bedlog "
			+ "INNER JOIN bedlog.bedStatusMaster bedStatus " 
			+ "INNER JOIN bed.floorMaster flr "
			+ "INNER JOIN bed.wardMaster ward " 
			+ "INNER JOIN bed.roomMaster room " 
			+ " WHERE bed.unitId=:unitId "
			+ "AND bed.organizationId=:organizationId " 
			+ "AND bedlog.status='A' " 
			+ "AND bed.status='A' "
			+ "AND bed.bedStatusId=3 "
			+ "AND bedlog.bedStatusId=3 "//BedStatusId 3---> Ready For Use
			+ "AND ward.isEr ='N' "
			+ "AND ward.isIcu='N' "
			+ "AND ward.islaborWard ='Y' "),
	
	@NamedQuery(name = "GET_ACTIVE_HEMODIALYSIS_BED_LIST", query = "SELECT bed.bedId as bedId, "
			+ "bed.bedNumber as bedNumber, " 
			+ "bed.floorId as floorId, " 
			+ "bed.roomId as roomId, "
			+ "room.roomDesc as roomDesc, "
			+ "flr.floorName as floorName, "
			+ "bed.wardId as wardId, " 
			+ "ward.wardName as wardName, "
			+ "bedlog.bedStatusId as bedStatusId, "
			+ "bedStatus.bedStatusDesc as bedStatusDesc " 
			+ "FROM BedMaster bed "
			+ "INNER JOIN bed.listBedLogStatus bedlog "
			+ "INNER JOIN bedlog.bedStatusMaster bedStatus " 
			+ "INNER JOIN bed.floorMaster flr "
			+ "INNER JOIN bed.wardMaster ward " 
			+ "INNER JOIN bed.roomMaster room " 
			+ " WHERE bed.unitId=:unitId "
			+ "AND bed.organizationId=:organizationId " 
			+ "AND bedlog.status='A' " 
			+ "AND bed.status='A' "
			+ "AND bed.bedStatusId=3 "
			+ "AND bedlog.bedStatusId=3 "//BedStatusId 3---> Ready For Use
			+ "AND ward.isEr ='N' "
			+ "AND ward.isIcu='N' "
			+ "AND ward.ishemodialysisward ='Y' "),
	
	@NamedQuery(name = "GET_ICU_BED_LIST_BY_ICU_TYPE_ID", query = "SELECT bed.bedId as bedId, "
			+ "bed.bedNumber as bedNumber, " 
			+ "bed.floorId as floorId, " 
			+ "bed.roomId as roomId, "
			+ "flr.floorName as floorName, "
			+ "bed.wardId as wardId, " 
			+ "ward.wardName as wardName, "
			+ "bed.bedCategoryId as bedCategoryId, "
			+ "bed.billingBedCategoryId as billingBedCategoryId, "
			+ "bbc.billingBedDesc as billingBedDesc, " 
			+ "bc.bedCategoryDesc as bedCategoryDesc " 
			+ "FROM BedMaster bed "
			+ "INNER JOIN bed.listBedLogStatus bedlog " 
			+ "INNER JOIN bed.floorMaster flr "
			+ "INNER JOIN bed.wardMaster ward " 
			+ "INNER JOIN bed.bedCategoryMaster bc "
			+ "INNER JOIN bed.billingBedCategoryMaster bbc " 
			+ "WHERE bed.unitId=:unitId "
			+ "AND bed.organizationId=:organizationId " 
			+ "AND bedlog.status='A' " 
			+ "AND bed.status='A' "
			+ "AND bed.bedStatusId=3 "
			+ "AND bedlog.bedStatusId=3 "//BedStatusId 3---> Ready For Use
			+ "AND ward.isEr ='N' "
			+ "AND ward.isIcu='Y' "  
			+ "AND ward.icuTypeId=:icuTypeId "
			),
	
	@NamedQuery(name = "GET_ACTIVE_ER_BED_LIST", query = "SELECT bed.bedId as bedId, "
			+ "bed.bedNumber as bedNumber, " 
			+ "bed.floorId as floorId, " 
			+ "bed.roomId as roomId, "
			+ "room.erCategoryId as erCategoryId, "
			+ "flr.floorName as floorName, "
			+ "bed.wardId as wardId, " 
			+ "ward.wardName as wardName, "
			+ "bed.bedCategoryId as bedCategoryId, "
			+ "bed.billingBedCategoryId as billingBedCategoryId, " 
			+ "bc.bedCategoryDesc as bedCategoryDesc " 
			+ "FROM BedMaster bed "
			+ "INNER JOIN bed.listBedLogStatus bedlog " 
			+ "INNER JOIN bed.floorMaster flr "
			+ "INNER JOIN bed.wardMaster ward " 
			+ "INNER JOIN bed.bedCategoryMaster bc "
			+ "INNER JOIN bed.roomMaster room " 
			+ "WHERE bed.unitId=:unitId "
			+ "AND bed.organizationId=:organizationId " 
			+ "AND bedlog.status='A' " 
			+ "AND bed.status='A' "
			+ "AND bed.bedStatusId=3 "
			+ "AND bedlog.bedStatusId=3 " //BedStatusId 3---> Ready For Use
			//+ "AND bed.bedCategoryId=5 "),
			+ "AND room.erCategoryId=:erCategoryId "),
			//5 is the id for Bed category
//=====>	
	/*@NamedQuery(name = "GET_AVAILABLE_BED_LIST_BY_MUL_PARAM", query = "SELECT bed.bedId as bedId, "
			+ "bed.bedNumber as bedNumber, " 
			+ "bed.floorId as floorId, " 
			+ "bed.roomId as roomId, "
			+ "floor.floorName as floorName, "
			+ "bed.wardId as wardId, " 
			+ "ward.wardName as wardName, "
			+ "bed.bedCategoryId as bedCategoryId, "
			+ "bed.billingBedCategoryId as billingBedCategoryId " 
			+ "bc.bedCategoryDesc as bedCategoryDesc "
			+ "FROM BedMaster bed "),
*/	
	@NamedQuery(name="GET_BED_BY_WARD_ID",query="SELECT bed.bedId as bedId, "
			+ "bed.bedNumber as bedNumber "
			+ "FROM BedMaster bed "
			+ "INNER JOIN bed.listBedLogStatus bedlog "
			+ "WHERE bed.unitId=:unitId "
			+ "AND bed.organizationId=:organizationId "
			+ "AND bed.wardId=:wardId "
			+ "AND bed.status='A' "
			+ "AND bedlog.status='A' "
			+ "AND bedlog.bedStatusId=5 "
			+ "AND bed.bedStatusId=5 "
			+ "ORDER BY bed.bedNumber ASC"//BedStatusId 5---> Booked
			),
	
	@NamedQuery(name="GET_BED_LIST_BY_WARD_ID",query="SELECT bed.bedId as bedId, "
			+ "bed.bedNumber as bedNumber, " 
			+ "bed.floorId as floorId, " 
			+ "bed.roomId as roomId, "
			+ "bed.wardId as wardId, " 
			+ "bed.bedCategoryId as bedCategoryId, "
			+ "bc.bedCategoryDesc as bedCategoryDesc, "
			+ "bed.billingBedCategoryId as billingBedCategoryId, "
			+ "bc.isIcu as isIcuSelected, "
			+ "bc.hierarchyId as hierarchyId "
			+ "FROM BedMaster bed "
			+ "INNER JOIN bed.listBedLogStatus bedlog "
			+ "INNER JOIN bed.bedCategoryMaster bc " 
			+ "WHERE bed.unitId=:unitId "
			+ "AND bed.organizationId=:organizationId "
			+ "AND bed.wardId=:wardId "
			+ "AND bed.status='A' "
			+ "AND bedlog.status='A' "
			+ "AND bedlog.bedStatusId=3 "
			+ "AND bed.bedStatusId=3 "
			//+ "AND bed.bedCategoryId!=5 "//BedStatusId 3---> Ready to use beds
			),
	
	@NamedQuery(name="GET_BED_MASTER_LIST",query="SELECT bm.bedId as bedId, "
			+ "bm.bedCode as bedCode, "
			+ "bm.bedDesc as bedDesc, "
			+ "bm.bedNumber as bedNumber, "
			+ "bm.itemId as itemId , "
			+ "bm.earmarkingDesc as earmarkingDesc, "
			+ "bm.floorId as floorId, "
			+ "floor.floorName as floorName, "
			+ "bm.wardId as wardId, "
			+ "ward.wardName as wardName, "
			+ "bm.roomId as roomId, "
			+ "room.roomDesc as roomDesc, "
			+ "bm.bedCategoryId as bedCategoryId, "
			+ "bc.bedCategoryDesc as bedCategoryDesc, "
			+ "bm.billingBedCategoryId as billingBedCategoryId, "
			+ "bbc.billingBedDesc as billingBedDesc, "
			+ "bm.isVirtual as isVirtual, "
			+ "bm.isEarMarking as isEarMarking, "
			+ "bm.status as status "
			+ "FROM BedMaster bm "
			+ "LEFT JOIN bm.floorMaster floor "
			+ "INNER JOIN bm.wardMaster ward "
			+ "LEFT JOIN bm.bedCategoryMaster bc "
			+ "LEFT JOIN bm.billingBedCategoryMaster bbc "
			+ "INNER JOIN bm.roomMaster room "
			+ "WHERE bm.organizationId=:orgId "
			+ "AND bm.unitId=:unitId order By bm.bedId DESC "),
	@NamedQuery(name="GET_BED_MOVING_BED_LIST",query="SELECT bm.bedId as bedId, "
			+ "bm.bedCode as bedCode, "
			+ "bm.bedDesc as bedDesc, "	
			+ "bm.bedNumber as bedNumber, "	
			+ "bm.itemId as itemId , "
			+ "bm.status as status "
			+ "FROM BedMaster bm "			
			+ "WHERE bm.organizationId=:orgId "
			+ "AND bm.unitId=:unitId"
			+ " AND bm.isMovingBed='Y' "),
	@NamedQuery(name="ACTIVE_MOVING_BED_LIST",query="SELECT bm.bedId as bedId, "
			+ "bm.bedCode as bedCode, "
			+ "bm.bedDesc as bedDesc, "
			+ "bm.bedNumber as bedNumber, "	
			+ "bm.itemId as itemId , "
			+ "bm.status as status "
			+ "FROM BedMaster bm "
			+ "INNER JOIN bm.listBedLogStatus bedlog "
			+ " WHERE bm.organizationId=:orgId "
			+ " AND bm.unitId=:unitId "
			+ " AND bm.isMovingBed='Y'"
			+ " AND bm.status='A' "
			+ " AND bedlog.status='A' " 
			+ " AND bm.bedStatusId=3 "
			+ " AND bedlog.bedStatusId=3 "),
	
	@NamedQuery(name="GET_BED_MASTER_BY_NAME_NOT_ID",query="SELECT bm.bedNumber as bedNumber "
			+ "FROM BedMaster bm "
			+ "WHERE bm.organizationId=:orgId "
			+ "AND bm.unitId=:unitId "
			+ "AND bm.floorId=:floorId "
			+ "AND bm.wardId=:wardId "
			+ "AND bm.roomId=:roomId "
			//+ "AND bm.bedCategoryId=:bedCategoryId "
			//+ "AND bm.billingBedCategoryId=:billingBedCatId "
			+ "AND bm.isVirtual=:isVirtual "
			+ "AND bm.isEarMarking=:isEarMarking "
			+ "AND bm.bedCode=:bedCode "
			+ "AND bm.bedDesc=:bedDesc "
			+ "AND bm.bedId!=:bedId "),
	
	@NamedQuery(name="GET_BED_BY_BED_STATUS_ID",query="SELECT bm.bedNumber as bedNumber "
			+ "FROM BedMaster bm WHERE"
			+ " bm.bedStatusId != 3 "
			+ "AND bm.bedId =:bedId "),
	
	
	
	@NamedQuery(name="GET_BED_MASTER_BY_NAME_NOT_ID_FOR_MOVING_BED",query="SELECT bm.bedCode as bedCode "
			+ "FROM BedMaster bm "
			+ "WHERE bm.organizationId=:orgId "
			+ "AND bm.unitId=:unitId "		
			+ "AND bm.bedCode=:bedCode "
			+ "AND bm.bedDesc=:bedDesc "
			+ "AND bm.bedId!=:bedId "),
	
	@NamedQuery(name="GET_BED_BY_ID",query="SELECT bm.bedId as bedId, "
			+ "bm.bedCode as bedCode, "
			+ "bm.bedDesc as bedDesc, "
			+ "bm.earmarkingDesc as earmarkingDesc, "
			+ "bm.bedNumber as bedNumber, "
			+ "bm.itemId as itemId ,"
			+ "bm.floorId as floorId, "
			+ "bm.wardId as wardId, "
			+ "bm.roomId as roomId,"
			+ "rm.roomDesc as roomDesc,"
			+ "bm.bedCategoryId as bedCategoryId, "
			+ "bm.billingBedCategoryId as billingBedCategoryId, "
			+ "bm.isVirtual as isVirtual, "
			+ "bm.isEarMarking as isEarMarking, "
			+ "bm.status as status "
			+ "FROM BedMaster bm "
			+ "LEFT JOIN bm.roomMaster rm "
			+ "WHERE bm.bedId=:bedId "),
	
	
/*	.setInteger("orgId", bedMasterDto.getOrganizationId())
	.setInteger("unitId", bedMasterDto.getUnitId())
	.setInteger("floorId", bedMasterDto.getFloorId())
	.setInteger("wardId", bedMasterDto.getWardId())
	.setInteger("roomId", bedMasterDto.getRoomId())
	.setInteger("bedCategoryId", bedMasterDto.getBedCategoryId())
	.setInteger("billingBedCatId", bedMasterDto.getBillingBedCategoryId())
	.setInteger("isVirtual", bedMasterDto.getIsVirtual())
	.setInteger("isEarMarking", bedMasterDto.getIsEarMarking())
	.setString("bedCode",bedMasterDto.getBedCode())
	.setString("bedDesc",bedMasterDto.getBedDesc())*/
	
	
	
	@NamedQuery(name="LIST_OF_VACANT_BEDS_FOR_NURSING",query="SELECT bm.bedId as bedId, "
			+ "bm.floorId as floorId, "
			+ "floor.floorName as floorName, "
			//+ "nur.nursingStationId as nursingStationId, "
			//+ "nur.nursingStationDesc as nursingStationDesc, "
			+ "bm.bedCategoryId as bedCategoryId, "
			+ "bc.bedCategoryDesc as bedCategoryDesc, "
			+ "bm.bedNumber as bedNumber, "
			+ "hskp.priorityId as priorityId, "
			+ "priority.priorityDesc as priorityDesc, "
			+ "hskp.housekeepingId as housekeepingId, "
			+ "hskp.housekeepingStatusId as housekeepingStatusId, "
			+ "hskp.assignedPersonId as assignedPersonId, "
			+ "concat(coalesce(prefix.prefixCode),'.',' ',coalesce(employee.firstName),' ',coalesce(employee.middleName),' ',coalesce(employee.lastName)) as userName, "
			+ "hsm.housekeepingStatusDesc as housekeepingStatusDesc "
			+ "FROM BedMaster bm "
			+ "INNER JOIN bm.listBedLogStatus bedLog "
			+ "LEFT JOIN bm.floorMaster floor "
			+ "LEFT JOIN bm.wardMaster ward "
			+ "LEFT JOIN bm.bedCategoryMaster bc "
			+ "INNER JOIN bm.housekeepingsList hskp "
			//+ "LEFT JOIN ward.nursingStationWardMappersList nurward "
			//+ "LEFT JOIN nurward.nursingStationMaster nur "
			+ "INNER JOIN hskp.housekeepingLogsList hskplg "
			+ "INNER JOIN hskp.housekeepingStatusMaster hsm "
			+ "INNER JOIN hskp.priorityMaster priority "
			+ "INNER JOIN hskp.userMaster um "
			+ "INNER JOIN um.employeeMastersList employee "
			+ "INNER JOIN employee.prefixMaster prefix "
			+ "WHERE bm.bedStatusId = 6 "
			+ "AND bedLog.bedStatusId= 6 "
			+ "AND bm.status='A' "
			+ "AND bedLog.status='A' "
			+ "AND bm.organizationId=:organizationId "
			+ "AND bm.unitId=:unitId "
			+ "AND hskp.status='A' "
			+ "AND hskplg.status='A' "),
	
	/*@NamedQuery(name="GET_BED_CATEGORY_AVAILABILITY_COUNT",query=" SELECT "
			+ "(SELECT count(bm.bedId) FROM BedMaster bm "
			+ "INNER JOIN bm.listBedLogStatus bls where bls.bedStatusId = 3 AND bm.bedCategoryId = bcm.bedCategoryId and bm.organizationId=:orgId and bm.unitId=:unitId and bls.status='A') as vacant, "
			+ "(SELECT count(bm.bedId) FROM BedMaster bm "
			+ "INNER JOIN bm.listBedLogStatus bls where bls.bedStatusId = 2 AND bm.bedCategoryId = bcm.bedCategoryId and bm.organizationId=:orgId and bm.unitId=:unitId and bls.status='A') as underHouseKeeping, "
			+ "(SELECT count(bm.bedId) FROM BedMaster bm "
			+ "INNER JOIN bm.listBedLogStatus bls where bls.bedStatusId = 5 AND bm.bedCategoryId = bcm.bedCategoryId and bm.organizationId=:orgId and bm.unitId=:unitId and bls.status='A') as occupied, "
			+ "bcm.bedCategoryDesc as bedCategoryDesc FROM BedCategoryMaster bcm "
			+ "WHERE bcm.organizationId=:orgId and bcm.unitId=:unitId and bcm.status='A' ORDER BY bcm.bedCategoryId "
			),*/
	/*@NamedQuery(name="GET_BED_CATEGORY_AVAILABILITY_COUNT",query=" SELECT "
			+ "(SELECT count(bm.bedId) FROM BedMaster bm "
			+ "INNER JOIN bm.wardMaster bls where bm.bedStatusId = 3 AND bm.bedCategoryId = bcm.bedCategoryId and bm.organizationId=:orgId and bm.unitId=:unitId and bm.status='A' and bls.isEr = 'N' and bls.isIcu = 'N') as vacant, "
			+ "(SELECT count(bm.bedId) FROM BedMaster bm "
			+ "INNER JOIN bm.wardMaster bls where bm.bedStatusId = 2 AND bm.bedCategoryId = bcm.bedCategoryId and bm.organizationId=:orgId and bm.unitId=:unitId and bm.status='A' and bls.isEr = 'N' and bls.isIcu = 'N') as underHouseKeeping, "
			+ "(SELECT count(bm.bedId) FROM BedMaster bm "
			+ "INNER JOIN bm.wardMaster bls where bm.bedStatusId = 5 AND bm.bedCategoryId = bcm.bedCategoryId and bm.organizationId=:orgId and bm.unitId=:unitId and bm.status='A' and bls.isEr = 'N' and bls.isIcu = 'N') as occupied, "
			+ "bcm.bedCategoryDesc as bedCategoryDesc FROM BedCategoryMaster bcm "
			+ "WHERE bcm.organizationId=:orgId and bcm.unitId=:unitId and bcm.status='A' ORDER BY bcm.bedCategoryId "
			),*/
	@NamedQuery(name="GET_BED_BY_NAME_AND_DESC",query="SELECT bm.bedNumber as bedNumber "
			+ "FROM BedMaster bm "
			+ "WHERE bm.organizationId=:orgId "
			+ "AND bm.unitId=:unitId "
			+ "AND bm.floorId=:floorId "
			+ "AND bm.wardId=:wardId "
			+ "AND bm.roomId=:roomId "
			+ "AND bm.isVirtual=:isVirtual "
			+ "AND bm.isEarMarking=:isEarMarking "
			+ "AND lower(bm.bedCode)=:bedCode "
			+ "AND lower(bm.bedDesc)=:bedDesc "),
	@NamedQuery(name="GET_BED_BY_NAME_AND_DESC_FOR_MOVING_BED",query="SELECT bm.bedCode as bedCode "
			+ "FROM BedMaster bm "
			+ "WHERE bm.organizationId=:orgId "
			+ "AND bm.unitId=:unitId "		
			+ "AND lower(bm.bedCode)=:bedCode "
			+ "AND lower(bm.bedDesc)=:bedDesc "),
	
	@NamedQuery(name="GET_PAGINATED_BED_MASTER_DETAILS_LIST_SEARCH"
	,query="SELECT bm.bedId as bedId, "
			+ "bm.bedCode as bedCode, "
			+ "bm.bedDesc as bedDesc, "
			+ "bm.bedNumber as bedNumber, "
			+ "bm.itemId as itemId , "
			+ "bm.earmarkingDesc as earmarkingDesc, "
			+ "bm.floorId as floorId, "
			+ "floor.floorName as floorName, "
			+ "bm.wardId as wardId, "
			+ "ward.wardName as wardName, "
			+ "bm.roomId as roomId, "
			+ "room.roomDesc as roomDesc, "
			+ "bm.bedCategoryId as bedCategoryId, "
			+ "bc.bedCategoryDesc as bedCategoryDesc, "
			+ "bm.billingBedCategoryId as billingBedCategoryId, "
			+ "bbc.billingBedDesc as billingBedDesc, "
			+ "bm.isVirtual as isVirtual, "
			+ "bm.isEarMarking as isEarMarking, "
			+ "bm.status as status "
			+ "FROM BedMaster bm "
			+ "LEFT JOIN bm.floorMaster floor "
			+ "INNER JOIN bm.wardMaster ward "
			+ "LEFT JOIN bm.bedCategoryMaster bc "
			+ "LEFT JOIN bm.billingBedCategoryMaster bbc "
			+ "INNER JOIN bm.roomMaster room "
			+ "WHERE bm.organizationId=:orgId "
			+ "AND bm.unitId = :unitId "
			+ "AND (lower(bm.bedCode) like :bedDesc "
			+ "OR lower(bm.bedDesc) like :bedDesc "
			+ "OR lower(bm.bedNumber) like :bedDesc "
			+ "OR lower(bm.earmarkingDesc) like :bedDesc "
			+ "OR lower(ward.wardName) like :bedDesc "
			+ "OR lower(room.roomDesc) like :bedDesc "
			+ "OR lower(bc.bedCategoryDesc) like :bedDesc "
			+ "OR lower(bbc.billingBedDesc) like :bedDesc) "
			+ " order By bm.bedId DESC "),
	
	@NamedQuery(name="GET_PAGINATED_MOVING_BED_MASTER_DETAILS_LIST_SEARCH",
		query="SELECT bm.bedId as bedId, "
		+ "bm.bedCode as bedCode, "
		+ "bm.bedDesc as bedDesc, "	
		+ "bm.bedNumber as bedNumber, "	
		+ "bm.itemId as itemId , "
		+ "bm.status as status "
		+ "FROM BedMaster bm "			
		+ "WHERE bm.organizationId=:orgId "
		+ "AND bm.unitId=:unitId "
		+ "AND bm.isMovingBed='Y' "
		+ "AND (lower(bm.bedCode) like :bedDesc "
		+ "OR lower(bm.bedDesc) like :bedDesc "
		+ "OR lower(bm.bedNumber) like :bedDesc ) "
		+ "order By bm.bedId DESC "),
	
		
})

@NamedNativeQueries({
	
	@NamedNativeQuery(name="GET_BED_AVAILIBILITY",query="select count(*) as \"countBooked\", "
			+ "(select count(*) from adt.m_bed_master bm where bm.bed_category_id = :bedCat) as \"countTotal\", "
			+ "(select count(*) from adt.t_admission_patient_mapping  apm where apm.active_status = 'R' and apm.bed_category_id =:bedCat) as \"countReserved\" "
			+ "from adt.t_admission_details "
			+ "where status='A' "
			+ "and doa < :date "
			+ "and pdd > :date "
			+ "and organization_id=:orgId "
			+ "and unit_id=:unitId "),
	
	@NamedNativeQuery(name="GET_BED_CATEGORY_AVAILABILITY_COUNT",query=" select " + 
			"(select " + 
			"		count(bedmaster1.bed_id) " + 
			"	from " + 
			"		adt.m_bed_master bedmaster1 " + 
			"		inner join adt.t_bed_log_status tbls on tbls.bed_id = bedmaster1.bed_id  " + 
			"	where " + 
			"		tbls.bed_status_id in(1,3,6,2) " + 
			"		and tbls.status = 'A' " + 
			"		and bedmaster1.ward_id = mwm.ward_id " + 
			"		and bedmaster1.unit_id =:unitId " + 
			"		and bedmaster1.status = 'A') as \"vacant\", " + 
			/*"	( select " + 
			"		count(bedmaster3.bed_id) " + 
			"	from " + 
			"		adt.m_bed_master bedmaster3 " + 
			"	inner join adt.t_bed_log_status  tbls on tbls.bed_id = bedmaster3.bed_id  " + 
			"	where " + 
			"		tbls.bed_status_id in (5,7) " + 
			"			and tbls.status = 'A' " + 
			"			and bedmaster3.unit_id =:unitId " + 
			"			and bedmaster3.status = 'A' " + 
			"			and bedmaster3.ward_id = mwm.ward_id) as \"reserved\", " + */
			" (coalesce(cntReserv,0)) as \"reserved\", 	"
			+ "( select " + 
			"		count(bedmaster2.bed_id) " + 
			"	from " + 
			"		adt.m_bed_master bedmaster2 " + 
			"	inner join adt.t_bed_log_status  tbls on tbls.bed_id = bedmaster2.bed_id  " + 
			"	where " + 
			"		tbls.bed_status_id in (4) " + 
			"			and tbls.status = 'A' " + 
			"			and bedmaster2.unit_id =:unitId " + 
			"			and bedmaster2.status = 'A' " + 
			"			and bedmaster2.ward_id = mwm.ward_id) as \"occupied\", " + 
			"	( select " + 
			"		count(bedmaster2.bed_id) " + 
			"	from " + 
			"		adt.m_bed_master bedmaster2 " + 
			"	inner join adt.t_bed_log_status  tbls on tbls.bed_id = bedmaster2.bed_id  " + 
			"	where " + 
			"		tbls.bed_status_id in (2,6) " + 
			"			and tbls.status = 'A' " + 
			"			and bedmaster2.unit_id =:unitId " + 
			"			and bedmaster2.status = 'A' " + 
			"			and bedmaster2.ward_id = mwm.ward_id) as \"housekeeping\", " + 
			"	mwm.ward_name as \"wardDesc\" " + 
			"from  adt.m_ward_master mwm "
			+ " left join (select w.ward_id,cntReserv from adt.m_ward_master w " + 
			" inner join adt.m_bed_master mbm on w.ward_id = mbm.ward_id " + 
			" inner join adt.m_bed_category_master mbbcm on mbbcm.bed_category_id = mbm.bed_category_id  " + 
			" inner join (select count(*) cntReserv,bcm.bed_category_id from adt.t_admission_patient_mapping bcm " + 
			" inner join adt.t_admission_note tan2  on tan2.admission_note_id  = bcm.admission_note_id  " + 
			" where bcm.status = 'A' and bcm.active_status ='R' and tan2.unit_id = :unitId  " + 
			" group by bcm.bed_category_id) rc on rc.bed_category_id = mbm.bed_category_id  " + 
			" where w.unit_id =:unitId " + 
			" group by w.ward_id,cntReserv order by w.ward_id  " + 
			") reser on reser.ward_id = mwm.ward_id " 
			 + " where mwm.unit_id =:unitId " 
			 + " 	and mwm.status = 'A' " 
			 + " order by " 
			 + " 	mwm.ward_name " )
	
	
})


@Entity
@Table(name = "m_bed_master", schema = "adt")
@SequenceGenerator(name = "bed_master_seq", sequenceName = "adt.bed_master_seq", allocationSize = 1)
public class BedMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bed_master_seq")
	@Column(name = "bed_id")
	private Integer bedId;

	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "floor_id")
	private Integer floorId;

	@Column(name = "ward_id")
	private Integer wardId;

	@Column(name = "room_id")
	private Integer roomId;

	@Column(name = "bed_category_id")
	private Integer bedCategoryId;

	@Column(name = "bed_status_id")
	private Integer bedStatusId;

	@Column(name = "billing_bed_category_id")
	private Integer billingBedCategoryId;

	@Column(name = "bed_code")
	private String bedCode;

	@Column(name = "bed_desc")
	private String bedDesc;
	
	@Column(name = "bed_number")
	private String bedNumber;

	@Column(name = "is_virtual")
	private Character isVirtual;
	
	@Column(name="is_ear_marking")
	private Character isEarMarking;
	
	@Column(name="earmarking_desc")
	private String earmarkingDesc;
	

	@Column(name = "status")
	private Character status;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "updated_date")
	private Date updatedDate;
	

	@Column(name = "bed_type_id")
	private Integer bedTypeId;

	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "is_moving_bed")
	private Character isMovingBed;
	
	@Column(name = "item_id")
	private Integer itemId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "organization_id", insertable = false, updatable = false)
	private OrganizationMaster organizationMaster;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "bedMaster", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<AdmissionDetails> admissionDetailsList;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "unit_id", insertable = false, updatable = false)
	private UnitMaster unitMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "floor_id", insertable = false, updatable = false)
	private FloorMaster floorMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ward_id", insertable = false, updatable = false)
	private WardMaster wardMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "room_id", insertable = false, updatable = false)
	private RoomMaster roomMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bed_category_id", insertable = false, updatable = false)
	private BedCategoryMaster bedCategoryMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bed_status_id", insertable = false, updatable = false)
	private BedStatusMaster bedStatusMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "billing_bed_category_id", insertable = false, updatable = false)
	private BillingBedCategoryMaster billingBedCategoryMaster;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "bedMaster", cascade = CascadeType.ALL)
	private List<BedLogStatus> listBedLogStatus;
	
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "bedMaster", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Transfer> transferslist;
	
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "bedMaster2", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Transfer> transferslist2;
	
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "bedMaster", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Discharge> dischargesList;
	
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "bedMaster", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Housekeeping> housekeepingsList;
	
	/*@OneToMany(fetch = FetchType.LAZY,mappedBy = "bedMaster", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<HousekeepingLog> housekeepingLogsList;*/

	public Integer getOrganizationId() {
		return organizationId;
	}

	public String getBedDesc() {
		return bedDesc;
	}

	public void setBedDesc(String bedDesc) {
		this.bedDesc = bedDesc;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public OrganizationMaster getOrganizationMaster() {
		return organizationMaster;
	}

	public void setOrganizationMaster(OrganizationMaster organizationMaster) {
		this.organizationMaster = organizationMaster;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public UnitMaster getUnitMaster() {
		return unitMaster;
	}

	public void setUnitMaster(UnitMaster unitMaster) {
		this.unitMaster = unitMaster;
	}

	public FloorMaster getFloorMaster() {
		return floorMaster;
	}

	public void setFloorMaster(FloorMaster floorMaster) {
		this.floorMaster = floorMaster;
	}

	public WardMaster getWardMaster() {
		return wardMaster;
	}

	public void setWardMaster(WardMaster wardMaster) {
		this.wardMaster = wardMaster;
	}

	public RoomMaster getRoomMaster() {
		return roomMaster;
	}

	public void setRoomMaster(RoomMaster roomMaster) {
		this.roomMaster = roomMaster;
	}


	public BedStatusMaster getBedStatusMaster() {
		return bedStatusMaster;
	}

	public void setBedStatusMaster(BedStatusMaster bedStatusMaster) {
		this.bedStatusMaster = bedStatusMaster;
	}

	

	public Integer getBillingBedCategoryId() {
		return billingBedCategoryId;
	}

	public void setBillingBedCategoryId(Integer billingBedCategoryId) {
		this.billingBedCategoryId = billingBedCategoryId;
	}

	public List<AdmissionDetails> getAdmissionDetailsList() {
		return admissionDetailsList;
	}

	public void setAdmissionDetailsList(List<AdmissionDetails> admissionDetailsList) {
		this.admissionDetailsList = admissionDetailsList;
	}

	public BillingBedCategoryMaster getBillingBedCategoryMaster() {
		return billingBedCategoryMaster;
	}

	public void setBillingBedCategoryMaster(BillingBedCategoryMaster billingBedCategoryMaster) {
		this.billingBedCategoryMaster = billingBedCategoryMaster;
	}

	public List<BedLogStatus> getListBedLogStatus() {
		return listBedLogStatus;
	}

	public void setListBedLogStatus(List<BedLogStatus> listBedLogStatus) {
		this.listBedLogStatus = listBedLogStatus;
	}

	public Integer getBedId() {
		return bedId;
	}

	public void setBedId(Integer bedId) {
		this.bedId = bedId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getFloorId() {
		return floorId;
	}

	public void setFloorId(Integer floorId) {
		this.floorId = floorId;
	}

	public Integer getWardId() {
		return wardId;
	}

	public void setWardId(Integer wardId) {
		this.wardId = wardId;
	}

	public Integer getRoomId() {
		return roomId;
	}

	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}

	public Integer getBedCategoryId() {
		return bedCategoryId;
	}

	public void setBedCategoryId(Integer bedCategoryId) {
		this.bedCategoryId = bedCategoryId;
	}

	public Integer getBedStatusId() {
		return bedStatusId;
	}

	public void setBedStatusId(Integer bedStatusId) {
		this.bedStatusId = bedStatusId;
	}

	public String getBedCode() {
		return bedCode;
	}

	public void setBedCode(String bedCode) {
		this.bedCode = bedCode;
	}

	public String getBedNumber() {
		return bedNumber;
	}

	public void setBedNumber(String bedNumber) {
		this.bedNumber = bedNumber;
	}

	public Character getIsVirtual() {
		return isVirtual;
	}

	public void setIsVirtual(Character isVirtual) {
		this.isVirtual = isVirtual;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	public Character getIsEarMarking() {
		return isEarMarking;
	}

	public void setIsEarMarking(Character isEarMarking) {
		this.isEarMarking = isEarMarking;
	}

	public String getEarmarkingDesc() {
		return earmarkingDesc;
	}

	public void setEarmarkingDesc(String earmarkingDesc) {
		this.earmarkingDesc = earmarkingDesc;
	}

	public Integer getBedTypeId() {
		return bedTypeId;
	}

	public void setBedTypeId(Integer bedTypeId) {
		this.bedTypeId = bedTypeId;
	}

	public Character getIsMovingBed() {
		return isMovingBed;
	}

	public void setIsMovingBed(Character isMovingBed) {
		this.isMovingBed = isMovingBed;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}
		
}
