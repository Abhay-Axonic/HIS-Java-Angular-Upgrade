package com.param.global.model;

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

import com.param.billing.global.model.TPeriodicityServiceOrders;
import com.param.bloodBank.model.BloodComponentOrderMaster;
import com.param.entity.lis.unit.TestMaster;
import com.param.entity.org.model.GeneralLedgerMaster;
import com.param.otims.model.ProcedureConfiguration;
import com.param.otims.model.ProcedureOrderMaster;
import com.param.service.global.model.MPackageMaster;
import com.param.service.global.model.MPeriodicityServiceConfig;
import com.param.service.global.model.TContractServiceDetails;
import com.param.service.global.model.TPackageIncExcDetails;
import com.param.service.global.model.TPackageServicesDetails;

@NamedQueries({

	@NamedQuery(name="GET_RENDERED_SERVICES_BY_ENCOUNTER_ID",
			query="SELECT orderDetails.orderDetailsId as orderDetailsId, "
					+ "orderDetails.orgId as orgId, "
					+ "ord.orderNo as orderNo, "
					+ "ord.saleId as saleId, "
					+ "orderDetails.orgUnitId as orgUnitId, "
					+ "orderDetails.orderId as orderId, "
					+ "orderDetails.orderQty as quantity, "
					+ "orderDetails.markupStatus as markupStatus, "
					+ "coalesce(orderDetails.patientClassPercentage,0) as patientClassPercentage, "
					+ "orderDetails.serviceId as serviceMasterId, "
					+ "service.serviceStandardName as serviceStandardName, "
					+ "service.serviceStandardCode as serviceStandardCode, "
					+ "orderDetails.priorityId as priorityId, "
					+ "orderDetails.isOutsourced as isOutsourced,"
					+ "orderDetails.discountCategoryId as discountCategoryId,"
					+ "to_char(orderDetails.orderDate,'YYYY-MM-DD') as orderDateString, "
					+ "to_char(orderDetails.orderDate,'DD/MM/YYYY HH24:MI:SS') as orderDateTimeString, "
					+ "to_char(orderDetails.orderDate, 'HH24:MI') AS  orderDateTime, "
					+ "to_char(orderDetails.orderSchDate,'DD/MM/YYYY HH24:MI:SS') as orderSchDateString, "
					//+ "orderDetails.orderSchDate as orderSchDate, "
					+ " orderDetails.orderDate as orderDate, "
					+ " coalesce(orderDetails.serviceAmt,0) as basePrice, "
					+ " coalesce(orderDetails.serviceAmt,0) as oldBasePrice, "
					+ " coalesce(orderDetails.dummyServiceAmt,0) as dummyServiceAmt, "
					+ " coalesce(orderDetails.ordConcession,0) as concession, "
					+ " coalesce( orderDetails.ordDiscount,0) as discount, "
					//+ "((( coalesce(orderDetails.ordDiscount,0)/(coalesce(orderDetails.serviceAmt,0)-coalesce(orderDetails.ordConcession,0)))*100)) "
					+ " coalesce( orderDetails.netAmount,0) as netPay, "
					+ " coalesce( orderDetails.netAmount,0) as oldNetPay, "
					+ "orderDetails.ordDocSplId as doctorSpecialityId, "
					+ "service.specialityId as serviceSpecialityId, "
					+ "service.subSpecialityId as serviceSubSpecialityId, "
					+ "orderDetails.taxId as taxId, "
					+ "tax.name as taxName, "
					+ "coalesce(orderDetails.taxPer,0) as taxPercentage, "
					+ "orderDetails.taxAmount as taxAmt, "
					+ "orderDetails.taxAmount as taxAmtOld, "
					+ "coalesce(orderDetails.selfPayable,0) as selfPayable,"
					+ "coalesce(orderDetails.creditPayable,0) as creditPayable, "
					+ "coalesce(orderDetails.selfPayable,0) as selfPayableOldC,"
					+ "coalesce(orderDetails.creditPayable,0) as creditPayableOldC, "
					+ "orderDetails.payeeId as payeeId, "
					+ "orderDetails.packageConsumptionAmt as packageConsumptionAmt, "
					+ "orderDetails.orderSetId as orderSetId, "
					+ "orderDetails.contractId as contractId, "
					+ "orderDetails.bedCategoryId as bedCategoryId, "
					+ "coalesce(orderDetails.coPayPer,0) as coPayPer, "
					+ "orderDetails.docSharePer as docSharePer, "
					+ "orderDetails.refDocSharePer as refDocSharePer, "
					+ "orderDetails.ordTotalAmt as totalAmt, "
					+ "orderDetails.ordCancelBy as ordCancelBy, "
					+ "orderDetails.ordCancelRemark as ordCancelRemark, "
					+ "orderDetails.tariffId as tariffId, "
					+ "orderDetails.orderEmergencyFlag as orderEmergencyFlag, "
					+ "orderDetails.packageId as packageId, "
					+ "orderDetails.ordDocId as doctorId, "
					+ "orderDetails.drugId as drugId, "
					+ "orderDetails.batchId as batchId, "
					+ "orderDetails.oldOrdDtlId as oldOrdDtlId, "
					+ "orderDetails.serviceRendered as serviceRendered,"
					+ "orderDetails.serviceChargeable as serviceChargeable, "
					+ "orderDetails.serviceRenderingDeptId as serviceRenderingDeptId, "
					+ "orderDetails.ordRenderDatetime as ordRenderDatetime, "
					+ "orderDetails.serviceIsBilled as serviceIsBilled, "
					+ "orderDetails.serviceBillId as serviceBillId, "
					+ "orderDetails.ordRemarks as ordRemarks, "
					+ "orderDetails.ordCancelled as ordCancelled, "
					+ "orderDetails.ordCancelReasonId as ordCancelReasonId, "
					+ "orderDetails.ordCancelDatetime as ordCancelDatetime, "
					+ "orderDetails.isDrug as isDrug, "
					+ "orderDetails.status as status, "
					+ "orderDetails.updatedBy as updatedBy, "
					+ "orderDetails.updatedDate as updatedDate, "
					+ "orderDetails.createdDate as createdDate, "
					+ "orderDetails.createdBy as createdBy, "
					+ "orderDetails.oldOrdDtlId as oldOrdDtlId, "
					+ "orderDetails.isInvestigationOrder as isInvestigationOrder , "
					+ "orderDetails.isPackageService as isPackageService, "
					+ "orderDetails.packageEitherOrGroupId as packageEitherOrGroupId, "
					+ "orderDetails.isCapService as isCapService, "
					+ "orderDetails.isFromAddservice as isFromAddservice, "
					+ "orderDetails.isConsultationService as isConsultationService, "
					+ "orderDetails.nursingServiceOrderFlag as nursingServiceOrderFlag, "
					+ "orderDetails.isDummyService as isDummyService, "
					+ "orderDetails.isHiddenService as isHiddenService, "
					+ "orderDetails.pharmacyDiscountOverride as pharmacyDiscountOverride, "
					+ "unitService.isRateEditable as isRateEditable, "
					+ "unitService.rateEditbleMinValue as minRateEditable, "
					+ "unitService.rateEditableMaxValue as maxRateEditable, "
					+ "unitService.isTaxApplicable as isTaxApplicable, "
					+ "coalesce(unitService.isRefDoctorShare,'N') as isRefDoctorShare, "
					+ "coalesce(unitService.isDocReq,'N') as isDoctorRequired, "
					+ "unitService.isDiscount as isDiscountApplicable, "
					+ "unitService.discountValue as discountValue, "
					+ "unitService.gstTypeId as gstTypeId, "
					+ "unitService.otcGstTypeId as otcGstTypeId, "
					+ "unitService.isQuantityEditable as isQuantityEditable, "
					+ "(select periodicityServiceOrderId FROM TPeriodicityServiceOrders WHERE admissionId=ord.admissionId AND orderDetailId=orderDetails.orderDetailsId AND status='A') as periodicityServiceOrderId, "
					+ "unitService.isAutoRender as isAutoRender, "
					+ "(SELECT coalesce(mpack.packageTypeId,0) from MPackageMaster mpack where mpack.packageMasterId = orderDetails.packageId) as packageTypeId, "
					+ "speciality.specialityName as specialityName, subSpec.subSpecialityMasterName as subSpecialityName, "
					+ "concat('Dr. ',coalesce(doc.firstName),' ',coalesce(doc.middleName),' ',coalesce(doc.lastName)) as doctorName,orderDetails.isPackage as isPackage, "
					+ "orderDetails.saleDetailId as saleDetailId, "
					+ "ord.encounterId as encounterId, "
					+ "enc.appointmentId as appointmentId, "
					+ "enc.patientClassId as patientClassId, "
					+ "orderDetails.procedureOrderMstId as procedureOrderMstId, "
					+ "coalesce(orderDetails.isProcedureService,'N') as isProcedureService, "
					+ " pkgConsumption.packageConsumptionMasterId as packageConsumptionMasterId, "
					+ " to_char(pkgConsumption.packageApplicableDate,'DD-MM-YYYY') as packageApplicableDate, "
					+ " to_char(pkgConsumption.packageApplicableDate,'DD/MM/YYYY') as packageApplicableDateDisplay, "
					+ " pkgConsumption.packageApplicableDate as packageApplicableDateDisplayNew, "
					+ " orderDetails.isPostedWhileDischarge as isPostedWhileDischarge, orderDetails.isConsumed as isConsumed,"
					+ " coalesce(orderDetails.forceAddRemoved,0) as forceAddRemoved, "
					+ " orderDetails.isOrderVisibleDepartment as isOrderVisibleDepartment, "
					+ " orderDetails.discountOwnership as discountOwnership, "
					+ " orderDetails.hospitalShare as hospitalShare, "
					+ " orderDetails.bedId as bedId,"
					+ " orderDetails.wardId as wardId,"
					+ " orderDetails.roomId as roomId, "
					+ "	orderDetails.isChargedHalfDay as isChargedHalfDay, "
					+ " coalesce(ord.homeCollection,false) as homeCollection,"
					+ " orderDetails.patientCouponTemplateMapperId as patientCouponTemplateMapperId ,"
					+ " ( " +
					" case " +
					" when employee.employeeId is not null then concat( employee.firstName, ' ', employee.lastName ) " +
					" when docmst.doctorId is not null then concat( 'Dr. ', docmst.firstName, ' ', docmst.lastName ) " +
					" else 'Admin' " +
					" end " +
					" ) as userName "
					+ "FROM OrderDetailsMaster orderDetails "
					+ "INNER JOIN orderDetails.orderMaster ord "
					+ "INNER JOIN orderDetails.serviceMaster service "
					+ "LEFT JOIN service.specialityMaster speciality "
					+ "LEFT JOIN service.subSpecialityMaster subSpec "
					+ "INNER JOIN service.unitServiceMasterDetailsList unitService "
					+ "LEFT JOIN orderDetails.doctorMaster doc "
					+ " LEFT JOIN orderDetails.userMaster userMaster "
					+ " LEFT JOIN userMaster.employeeMastersList employee "
					+ " LEFT JOIN userMaster.doctorMastersList docmst "
					+ "LEFT JOIN orderDetails.tax tax "
					+ "LEFT JOIN ord.encounterMaster enc "
					+ "LEFT JOIN orderDetails.listTPackageConsumptionMaster pkgConsumption with pkgConsumption.status='A' "
					+ "WHERE orderDetails.serviceIsBilled=0 " //HIS-14515
					//+ "AND (orderDetails.serviceRendered=1 OR (orderDetails.serviceRendered=0 and orderDetails.isPackage=true))"
					+ "AND unitService.unitId=orderDetails.orgUnitId "
					+ "AND orderDetails.status='A' "
					+ "AND unitService.status='A' "
					+ "AND ((ord.encounterId=:encounterId and ord.visitTypeId=:visitTypeId) or (ord.admissionId=:encounterId and ord.visitTypeId=:visitTypeId)) "
					),

			@NamedQuery(name="GET_RENDERED_SERVICES_BY_APPOINTMENT_ID",
				query="SELECT orderDetails.orderDetailsId as orderDetailsId, "
					+ "orderDetails.orgId as orgId, "
					+ "ord.orderNo as orderNo, "
					+ "ord.saleId as saleId,  "
					+ "orderDetails.orgUnitId as orgUnitId, "
					+ "orderDetails.orderId as orderId, "
					+ "orderDetails.orderQty as quantity, "
					+ "orderDetails.markupStatus as markupStatus, "
					+ "coalesce(orderDetails.patientClassPercentage,0) as patientClassPercentage, "
					+ "orderDetails.serviceId as serviceMasterId, "
					+ "service.serviceStandardName as serviceStandardName, "
					+ "service.serviceStandardCode as serviceStandardCode, "
					+ "orderDetails.priorityId as priorityId, "
					+ "orderDetails.isOutsourced as isOutsourced,"
					+ "to_char(orderDetails.orderDate,'yyyy-mm-dd') as orderDateString, "
					+ "to_char(orderDetails.orderSchDate,'yyyy-mm-dd') as orderSchDateString, "
					//+ "orderDetails.orderSchDate as orderSchDate, "
					+ " orderDetails.orderDate as orderDate, "
					+ " coalesce(orderDetails.serviceAmt,0) as basePrice, "
					+ " coalesce(orderDetails.serviceAmt,0) as oldBasePrice, "
					+ " coalesce(orderDetails.dummyServiceAmt,0) as dummyServiceAmt, "
					+ " coalesce(orderDetails.ordConcession,0) as concession, "
					+ " coalesce( orderDetails.ordDiscount,0) as discount, "
					+ " coalesce( orderDetails.netAmount,0) as netPay, "
					+ " coalesce( orderDetails.netAmount,0) as oldNetPay, "
					+ "orderDetails.ordDocSplId as doctorSpecialityId, "
					+ "service.specialityId as serviceSpecialityId, "
					+ "service.subSpecialityId as serviceSubSpecialityId, "
					+ "orderDetails.taxId as taxId, "
					+ "tax.name as taxName, "
					+ "coalesce(orderDetails.taxPer,0) as taxPercentage, "
					+ "orderDetails.taxAmount as taxAmt, "
					+ "orderDetails.taxAmount as taxAmtOld, "
					+ "coalesce(orderDetails.selfPayable,0) as selfPayable,"
					+ "coalesce(orderDetails.creditPayable,0) as creditPayable, "
					+ "coalesce(orderDetails.selfPayable,0) as selfPayableOldC,"
					+ "coalesce(orderDetails.creditPayable,0) as creditPayableOldC, "
					+ "orderDetails.payeeId as payeeId, "
					+ "orderDetails.packageConsumptionAmt as packageConsumptionAmt, "
					+ "orderDetails.orderSetId as orderSetId, "
					+ "orderDetails.contractId as contractId, "
					+ "orderDetails.bedCategoryId as bedCategoryId, "
					+ "coalesce(orderDetails.coPayPer,0) as coPayPer, "
					+ "orderDetails.docSharePer as docSharePer, "
					+ "orderDetails.refDocSharePer as refDocSharePer, "
					+ "orderDetails.ordTotalAmt as totalAmt, "
					+ "orderDetails.ordCancelBy as ordCancelBy, "
					+ "orderDetails.ordCancelRemark as ordCancelRemark, "
					+ "orderDetails.tariffId as tariffId, "
					/*+ "orderDetails.billingClassId as billingClassId, "
					+ "orderDetails.bedId as bedId, "
					+ "orderDetails.roomId as roomId, "
					+ "orderDetails.wardId as wardId, "*/
					+ "orderDetails.orderEmergencyFlag as orderEmergencyFlag, "
					+ "orderDetails.packageId as packageId, "
					+ "orderDetails.ordDocId as doctorId, "
					+ "orderDetails.drugId as drugId, "
					+ "orderDetails.batchId as batchId, "
					+ "orderDetails.oldOrdDtlId as oldOrdDtlId, "
					+ "orderDetails.serviceRendered as serviceRendered,"
					+ "orderDetails.serviceChargeable as serviceChargeable, "
					+ "orderDetails.serviceRenderingDeptId as serviceRenderingDeptId, "
					+ "orderDetails.ordRenderDatetime as ordRenderDatetime, "
					+ "orderDetails.serviceIsBilled as serviceIsBilled, "
					+ "orderDetails.serviceBillId as serviceBillId, "
					+ "orderDetails.ordRemarks as ordRemarks, "
					+ "orderDetails.ordCancelled as ordCancelled, "
					+ "orderDetails.ordCancelReasonId as ordCancelReasonId, "
					+ "orderDetails.ordCancelDatetime as ordCancelDatetime, "
					+ "orderDetails.isDrug as isDrug, "
					+ "orderDetails.status as status, "
					+ "orderDetails.updatedBy as updatedBy, "
					+ "orderDetails.updatedDate as updatedDate, "
					+ "orderDetails.createdDate as createdDate, "
					+ "orderDetails.createdBy as createdBy, "
					+ "orderDetails.oldOrdDtlId as oldOrdDtlId, "
					+ "orderDetails.isInvestigationOrder as isInvestigationOrder , "
					+ "orderDetails.isPackageService as isPackageService, orderDetails.packageEitherOrGroupId as packageEitherOrGroupId, "
					+ "orderDetails.isCapService as isCapService, "
					+ "orderDetails.isFromAddservice as isFromAddservice, "
					+ "orderDetails.isConsultationService as isConsultationService, "
					+ "orderDetails.nursingServiceOrderFlag as nursingServiceOrderFlag, "
					+ "unitService.isRateEditable as isRateEditable, "
					+ "unitService.rateEditbleMinValue as minRateEditable, "
					+ "unitService.rateEditableMaxValue as maxRateEditable, "
					+ "unitService.isTaxApplicable as isTaxApplicable, "
					+ "coalesce(unitService.isRefDoctorShare,'N') as isRefDoctorShare, "
					+ "coalesce(unitService.isDocReq,'N') as isDoctorRequired, "
					+ "unitService.isDiscount as isDiscountApplicable, "
					+ "unitService.discountValue as discountValue, "
					+ "unitService.gstTypeId as gstTypeId, "
					+ "unitService.otcGstTypeId as otcGstTypeId, "
					+ "unitService.isQuantityEditable as isQuantityEditable, "
					+ "(select periodicityServiceOrderId FROM TPeriodicityServiceOrders WHERE admissionId=ord.admissionId AND orderDetailId=orderDetails.orderDetailsId AND status='A') as periodicityServiceOrderId, "
					+ "unitService.isAutoRender as isAutoRender, "
					+ "(SELECT coalesce(mpack.packageTypeId,0) from MPackageMaster mpack where mpack.packageMasterId = orderDetails.packageId) as packageTypeId, "
					+ "speciality.specialityName as specialityName, subSpec.subSpecialityMasterName as subSpecialityName, "
					+ "concat('Dr. ',coalesce(doc.firstName),' ',coalesce(doc.middleName),' ',coalesce(doc.lastName)) as doctorName,orderDetails.isPackage as isPackage, "
					+ "orderDetails.saleDetailId as saleDetailId, "
					+ "ord.encounterId as encounterId, "
					+ "app.appointmentId as appointmentId, "
					+ "	orderDetails.isChargedHalfDay as isChargedHalfDay, "
					+ "coalesce(ord.homeCollection,false) as homeCollection "
					+ "FROM AppointmentMaster app "
					+ "INNER JOIN app.orderDetailsMaster orderDetails "
					+ "INNER JOIN orderDetails.orderMaster ord "
					+ "INNER JOIN orderDetails.serviceMaster service "
					+ "LEFT JOIN service.specialityMaster speciality "
					+ "LEFT JOIN service.subSpecialityMaster subSpec "
					+ "INNER JOIN service.unitServiceMasterDetailsList unitService "
					+ "LEFT JOIN orderDetails.doctorMaster doc "
					+ "LEFT JOIN orderDetails.tax tax "
					+ "LEFT JOIN ord.encounterMaster enc "
					+ "WHERE orderDetails.serviceIsBilled=0 "
					+ "AND orderDetails.serviceRendered=0 "
					+ "AND unitService.unitId=orderDetails.orgUnitId "
					+ "AND orderDetails.status='A' "
					+ "AND unitService.status='A' "
					+ "AND  app.appointmentId = :appointmentId "
					),
		
	
		@NamedQuery(name="GET_EHC_SERVICES_BY_PACKAGE_ID",
		query="SELECT orderDetails.orderDetailsId as orderDetailsId, "
			+ "orderDetails.orgId as orgId, "
			+ "ord.orderNo as orderNo, "
			+ "ord.saleId as saleId, "
			+ "orderDetails.orgUnitId as orgUnitId, "
			+ "orderDetails.orderId as orderId, "
			+ "orderDetails.orderQty as quantity, "
			+ "orderDetails.markupStatus as markupStatus, "
			+ "coalesce(orderDetails.patientClassPercentage,0) as patientClassPercentage, "
			+ "orderDetails.serviceId as serviceMasterId, "
			+ "service.serviceStandardName as serviceStandardName, "
			+ "service.serviceStandardCode as serviceStandardCode, "
			+ "orderDetails.priorityId as priorityId, "
			+ "orderDetails.isOutsourced as isOutsourced,"
			+ "to_char(orderDetails.orderDate,'yyyy-mm-dd') as orderDateString, "
			+ "to_char(orderDetails.orderSchDate,'yyyy-mm-dd') as orderSchDateString, "
			//+ "orderDetails.orderSchDate as orderSchDate, "
			+ " orderDetails.orderDate as orderDate, "
			+ " coalesce(orderDetails.serviceAmt,0) as basePrice, "
			+ " coalesce(orderDetails.serviceAmt,0) as oldBasePrice, "
			+ " coalesce(orderDetails.dummyServiceAmt,0) as dummyServiceAmt, "
			+ " coalesce(orderDetails.ordConcession,0) as concession, "
			+ " coalesce( orderDetails.ordDiscount,0) as discount, "
			+ " coalesce( orderDetails.netAmount,0) as netPay, "
			+ " coalesce( orderDetails.netAmount,0) as oldNetPay, "
			+ "orderDetails.ordDocSplId as doctorSpecialityId, "
			+ "service.specialityId as serviceSpecialityId, "
			+ "service.subSpecialityId as serviceSubSpecialityId, "
			+ "orderDetails.taxId as taxId, "
			+ "tax.name as taxName, "
			+ "coalesce(orderDetails.taxPer,0) as taxPercentage, "
			+ "orderDetails.taxAmount as taxAmt, "
			+ "orderDetails.taxAmount as taxAmtOld, "
			+ "coalesce(orderDetails.selfPayable,0) as selfPayable,"
			+ "coalesce(orderDetails.creditPayable,0) as creditPayable, "
			+ "coalesce(orderDetails.selfPayable,0) as selfPayableOldC,"
			+ "coalesce(orderDetails.creditPayable,0) as creditPayableOldC, "
			+ "orderDetails.payeeId as payeeId, "
			+ "orderDetails.packageConsumptionAmt as packageConsumptionAmt, "
			+ "orderDetails.orderSetId as orderSetId, "
			+ "orderDetails.contractId as contractId, "
			+ "orderDetails.bedCategoryId as bedCategoryId, "
			+ "coalesce(orderDetails.coPayPer,0) as coPayPer, "
			+ "orderDetails.docSharePer as docSharePer, "
			+ "orderDetails.refDocSharePer as refDocSharePer, "
			+ "orderDetails.ordTotalAmt as totalAmt, "
			+ "orderDetails.ordCancelBy as ordCancelBy, "
			+ "orderDetails.ordCancelRemark as ordCancelRemark, "
			+ "orderDetails.tariffId as tariffId, "
			/*+ "orderDetails.billingClassId as billingClassId, "
			+ "orderDetails.bedId as bedId, "
			+ "orderDetails.roomId as roomId, "
			+ "orderDetails.wardId as wardId, "*/
			+ "orderDetails.orderEmergencyFlag as orderEmergencyFlag, "
			+ "orderDetails.packageId as packageId, "
			+ "orderDetails.ordDocId as doctorId, "
			+ "orderDetails.drugId as drugId, "
			+ "orderDetails.batchId as batchId, "
			+ "orderDetails.oldOrdDtlId as oldOrdDtlId, "
			+ "orderDetails.serviceRendered as serviceRendered,"
			+ "orderDetails.serviceChargeable as serviceChargeable, "
			+ "orderDetails.serviceRenderingDeptId as serviceRenderingDeptId, "
			+ "orderDetails.ordRenderDatetime as ordRenderDatetime, "
			+ "orderDetails.serviceIsBilled as serviceIsBilled, "
			+ "orderDetails.serviceBillId as serviceBillId, "
			+ "orderDetails.ordRemarks as ordRemarks, "
			+ "orderDetails.ordCancelled as ordCancelled, "
			+ "orderDetails.ordCancelReasonId as ordCancelReasonId, "
			+ "orderDetails.ordCancelDatetime as ordCancelDatetime, "
			+ "orderDetails.discountCategoryId as discountCategoryId,"
			+ "orderDetails.isDrug as isDrug, "
			+ "orderDetails.status as status, "
			+ "orderDetails.updatedBy as updatedBy, "
			+ "orderDetails.updatedDate as updatedDate, "
			+ "orderDetails.createdDate as createdDate, "
			+ "orderDetails.createdBy as createdBy, "
			+ "orderDetails.oldOrdDtlId as oldOrdDtlId, "
			+ "orderDetails.isInvestigationOrder as isInvestigationOrder , "
			+ "orderDetails.isPackageService as isPackageService, orderDetails.packageEitherOrGroupId as packageEitherOrGroupId, "
			+ "orderDetails.isCapService as isCapService, "
			+ "orderDetails.isFromAddservice as isFromAddservice, "
			+ "orderDetails.isConsultationService as isConsultationService, "
			+ "orderDetails.nursingServiceOrderFlag as nursingServiceOrderFlag, "
			+ "unitService.isRateEditable as isRateEditable, "
			+ "unitService.rateEditbleMinValue as minRateEditable, "
			+ "unitService.rateEditableMaxValue as maxRateEditable, "
			+ "unitService.isTaxApplicable as isTaxApplicable, "
			+ "unitService.isRefDoctorShare as isRefDoctorShare, "
			+ "unitService.isDocReq as isDoctorRequired, "
			+ "unitService.isDiscount as isDiscountApplicable, "
			+ "unitService.discountValue as discountValue, "
			+ "unitService.gstTypeId as gstTypeId, "
			+ "unitService.otcGstTypeId as otcGstTypeId, "
			+ "unitService.isQuantityEditable as isQuantityEditable, "
			+ "(select periodicityServiceOrderId FROM TPeriodicityServiceOrders WHERE admissionId=ord.admissionId AND orderDetailId=orderDetails.orderDetailsId AND status='A') as periodicityServiceOrderId, "
			+ "unitService.isAutoRender as isAutoRender, "
			+ "(SELECT coalesce(mpack.packageTypeId,0) from MPackageMaster mpack where mpack.packageMasterId = orderDetails.packageId) as packageTypeId, "
			+ "speciality.specialityName as specialityName, subSpec.subSpecialityMasterName as subSpecialityName, "
			+ "concat('Dr. ',coalesce(doc.firstName),' ',coalesce(doc.middleName),' ',coalesce(doc.lastName)) as doctorName,orderDetails.isPackage as isPackage, "
			+ "orderDetails.saleDetailId as saleDetailId, "
			+ "ord.encounterId as encounterId, "
			+ "enc.appointmentId as appointmentId, "
			+ "enc.patientClassId as patientClassId, "
			+ " orderDetails.procedureOrderMstId as procedureOrderMstId, "
			+ " coalesce(orderDetails.isProcedureService,'N') as isProcedureService,"
			+ " orderDetails.isPostedWhileDischarge as isPostedWhileDischarge ,orderDetails.isConsumed as isConsumed , coalesce(orderDetails.forceAddRemoved,0) as forceAddRemoved "
			+ "FROM OrderDetailsMaster orderDetails "
			+ "INNER JOIN orderDetails.orderMaster ord "
			+ "INNER JOIN orderDetails.listOrderDetailsPackageMapper pkgmppr "
			+ "INNER JOIN pkgmppr.packageMaster pkgmst "
			+ "INNER JOIN orderDetails.serviceMaster service "
			+ " LEFT JOIN service.specialityMaster speciality "
			+ " LEFT JOIN service.subSpecialityMaster subSpec "
			+ " INNER JOIN service.unitServiceMasterDetailsList unitService "
			+ " LEFT JOIN orderDetails.doctorMaster doc "
			+ " LEFT JOIN orderDetails.tax tax "
			+ " LEFT JOIN ord.encounterMaster enc "
			+ " WHERE orderDetails.serviceIsBilled=0 "
			+ " AND unitService.unitId=orderDetails.orgUnitId "
			+ " AND orderDetails.status='A' "
			+ " AND unitService.status='A'  and   1 = coalesce(pkgmst.patientTypeId,0) and ord.encounterId = :encounterId"
			+ " AND pkgmppr.status='A' and pkgmppr.packageId =:packageId   "
			),
	
	
		@NamedQuery(name = "GET_PATIENT_SERVICE_ORDER_LIST", 
		query = " SELECT orderDetails.orderDetailsId as orderDetailsId, "
			+ "orderDetails.orgId as orgId, "
			+ "ord.orderNo as orderNo, "
			+ "orderDetails.orgUnitId as orgUnitId, "
			+ "orderDetails.orderId as orderId, "
			//+ "orderDetails.orderQty as orderQty, "
			+ "orderDetails.serviceId as serviceMasterId, "
			+ "service.serviceStandardName as serviceStandardName, "
			+ "orderDetails.priorityId as priorityId, "
			+ "orderDetails.isOutsourced as isOutsourced,"
			+ "to_char(orderDetails.orderDate,'yyyy-mm-dd') as orderDateString, "
			+ "to_char(orderDetails.orderSchDate,'yyyy-mm-dd') as orderSchDateString, "
			//+ "orderDetails.orderSchDate as orderSchDate, "
			//+ "orderDetails.orderDate as orderDate, "
			+ "coalesce(orderDetails.serviceAmt,0) as basePrice, "
			+ " coalesce(orderDetails.dummyServiceAmt,0) as dummyServiceAmt, "
			+ "coalesce(orderDetails.ordConcession,0) as concession, "
			+ "coalesce(orderDetails.ordDiscount,0) as discount, "
			+ "coalesce(orderDetails.netAmount,0) as netPay, "
			+ "coalesce(orderDetails.netAmount,0) as oldNetPay, "
			+ "orderDetails.ordDocSplId as doctorSpecialityId, "
			+ "service.specialityId as serviceSpecialityId, "
			+ "service.subSpecialityId as serviceSubSpecialityId, "
			+ "orderDetails.taxId as taxId, "
			+ "tax.name as taxName, "
			+ "coalesce(orderDetails.taxPer,0) as taxPercentage, "
			+ "coalesce(orderDetails.taxAmount,0) as taxAmt, "
			+ "coalesce(orderDetails.selfPayable,0) as selfPayable,"
			+ "coalesce(orderDetails.creditPayable,0) as creditPayable, "
			+ "orderDetails.payeeId as payeeId, "
			+ "orderDetails.packageConsumptionAmt as packageConsumptionAmt, "
			+ "orderDetails.orderSetId as orderSetId, "
			+ "orderDetails.contractId as contractId, "
			+ "orderDetails.bedCategoryId as bedCategoryId, "
			+ "orderDetails.coPayPer as coPayPer, "
			+ "orderDetails.docSharePer as docSharePer, "
			+ "orderDetails.ordTotalAmt as totalAmt, "
			+ "orderDetails.ordCancelBy as ordCancelBy, "
			+ "orderDetails.ordCancelRemark as ordCancelRemark, "
			+ "orderDetails.tariffId as tariffId, "
			/*+ "orderDetails.billingClassId as billingClassId, "
			+ "orderDetails.bedId as bedId, "
			+ "orderDetails.roomId as roomId, "
			+ "orderDetails.wardId as wardId, "*/
			+ "orderDetails.orderEmergencyFlag as orderEmergencyFlag, "
			+ "orderDetails.packageId as packageId, "
			+ "orderDetails.ordDocId as doctorId, "
			+ "orderDetails.drugId as drugId, "
			+ "orderDetails.batchId as batchId, "
			+ "orderDetails.oldOrdDtlId as oldOrdDtlId, "
			+ "orderDetails.serviceRendered as serviceRendered,"
			+ "orderDetails.serviceChargeable as serviceChargeable, "
			+ "orderDetails.serviceRenderingDeptId as serviceRenderingDeptId, "
			+ "orderDetails.ordRenderDatetime as ordRenderDatetime, "
			+ "orderDetails.serviceIsBilled as serviceIsBilled, "
			+ "orderDetails.serviceBillId as serviceBillId, "
			+ "orderDetails.ordRemarks as ordRemarks, "
			+ "orderDetails.ordCancelled as ordCancelled, "
			+ "orderDetails.ordCancelReasonId as ordCancelReasonId, "
			+ "orderDetails.ordCancelDatetime as ordCancelDatetime, "
			+ "orderDetails.isDrug as isDrug, "
			+ "orderDetails.status as status, "
			+ "orderDetails.updatedBy as updatedBy, "
			+ "orderDetails.updatedDate as updatedDate, "
			+ "orderDetails.createdDate as createdDate, "
			+ "orderDetails.createdBy as createdBy, "
			+ "orderDetails.oldOrdDtlId as oldOrdDtlId, "
			+ "orderDetails.isPackageService as isPackageService,"
			+ " orderDetails.isCapService as isCapService, "
			+ "orderDetails.contractId as contractId, "
			+ "orderDetails.isConsultationService as isConsultationService, "
			+ "orderDetails.nursingServiceOrderFlag as nursingServiceOrderFlag, "
			+ "unitService.isRateEditable as isRateEditable, "
			+ "unitService.rateEditbleMinValue as minRateEditable, "
			+ "unitService.rateEditableMaxValue as maxRateEditable, "
			+ "unitService.isTaxApplicable as isTaxApplicable, "
			+ "unitService.isRefDoctorShare as isRefDoctorShare, "
			+ "unitService.isDiscount as isDiscountApplicable, "
			+ "unitService.discountValue as discountValue, "
			+ "unitService.gstTypeId as gstTypeId, "
			+ "unitService.otcGstTypeId as otcGstTypeId, "
			+ "unitService.isQuantityEditable as isQuantityEditable, "
			+ "(SELECT coalesce(mpack.packageTypeId,0) from MPackageMaster mpack where mpack.packageMasterId = orderDetails.packageId) as packageTypeId, "
			+ "speciality.specialityName as specialityName, subSpec.subSpecialityMasterName as subSpecialityName, "
			+ "concat('Dr. ',coalesce(doc.firstName),' ',coalesce(doc.middleName),' ',coalesce(doc.lastName)) as doctorName,orderDetails.isPackage as isPackage "
			+ "FROM OrderDetailsMaster orderDetails "
			+ "INNER JOIN orderDetails.orderMaster ord "
			+ "LEFT JOIN orderDetails.serviceMaster service "
			+ "LEFT JOIN service.specialityMaster speciality "
			+ "LEFT JOIN service.subSpecialityMaster subSpec "
			+ "INNER JOIN service.unitServiceMasterDetailsList unitService "
			+ "LEFT JOIN orderDetails.doctorMaster doc "
			+ "LEFT JOIN orderDetails.tax tax "
			+ "WHERE orderDetails.serviceIsBilled=0 "
			+ "AND unitService.status='A' "
			+ "AND  orderDetails.serviceRendered=0 "
			+ "AND  orderDetails.status='A' "
			+ "AND orderDetails.isPackage=false "
			+ "AND ord.admissionId=:admissionId AND unitService.unitId=orderDetails.orgUnitId"),
		
		
		
		@NamedQuery(name = "GET_PATIENT_ITEM_ORDER_LIST", 
		query = " SELECT orderDetails.orderDetailsId as orderDetailsId, "
			+ "orderDetails.orgId as orgId, "
			+ "ord.orderNo as orderNo, "
			+ "orderDetails.orgUnitId as orgUnitId, "
			+ "orderDetails.orderId as orderId, "
			+ "orderDetails.orderQty as quantity, "
			//+ "orderDetails.serviceId as serviceId, "
			+ "service.serviceStandardName as serviceStandardName, "
			+ "orderDetails.priorityId as priorityId, "
			+ "orderDetails.isOutsourced as isOutsourced,"
			+ "to_char(orderDetails.orderDate,'yyyy-mm-dd') as orderDateString, "
			+ "to_char(orderDetails.orderSchDate,'yyyy-mm-dd') as orderSchDateString, "
			//+ "orderDetails.orderSchDate as orderSchDate, "
			//+ "orderDetails.orderDate as orderDate, "
			+ " coalesce(orderDetails.serviceAmt,0) as basePrice, "
			+ " coalesce(orderDetails.ordConcession,0) as concession, "
			+ " coalesce(orderDetails.ordDiscount,0) as discount, "
			+ " coalesce(orderDetails.netAmount,0) as netPay, "
			+ "coalesce(orderDetails.netAmount,0) as oldNetPay, "
			+ "orderDetails.ordDocSplId as specialityId, "
			+ "service.specialityId as serviceSpecialityId, "
			+ "service.subSpecialityId as serviceSubSpecialityId, "
			+ "orderDetails.taxId as taxId, "
			+ "tax.name as taxName, "
			+ " coalesce(orderDetails.taxPer,0) as taxPercentage, "
			+ " coalesce(orderDetails.taxAmount,0) as taxAmt, "
			+ " coalesce(orderDetails.selfPayable,0) as selfPayable,"
			+ " coalesce(orderDetails.creditPayable,0) as creditPayable, "
			+ "orderDetails.payeeId as payeeId, "
			+ "orderDetails.packageConsumptionAmt as packageConsumptionAmt, "
			+ "orderDetails.orderSetId as orderSetId, "
			+ "orderDetails.contractId as contractId, "
			+ "orderDetails.bedCategoryId as bedCategoryId, "
			+ "orderDetails.coPayPer as coPayPer, "
			+ "orderDetails.docSharePer as docSharePer, "
			+ "orderDetails.ordTotalAmt as totalAmt, "
			+ "orderDetails.ordCancelBy as ordCancelBy, "
			+ "orderDetails.ordCancelRemark as ordCancelRemark, "
			+ "orderDetails.tariffId as tariffId, "
			/*+ "orderDetails.billingClassId as billingClassId, "
			+ "orderDetails.bedId as bedId, "
			+ "orderDetails.roomId as roomId, "
			+ "orderDetails.wardId as wardId, "*/
			+ "orderDetails.orderEmergencyFlag as orderEmergencyFlag, "
			+ "orderDetails.packageId as packageId, "
			+ "orderDetails.ordDocId as doctorId, "
			+ "orderDetails.drugId as drugId, "
			+ "orderDetails.batchId as batchId, "
			+ "orderDetails.oldOrdDtlId as oldOrdDtlId, "
			+ "orderDetails.serviceRendered as serviceRendered,"
			+ "orderDetails.serviceChargeable as serviceChargeable, "
			+ "orderDetails.serviceRenderingDeptId as serviceRenderingDeptId, "
			+ "orderDetails.ordRenderDatetime as ordRenderDatetime, "
			+ "orderDetails.serviceIsBilled as serviceIsBilled, "
			+ "orderDetails.serviceBillId as serviceBillId, "
			+ "orderDetails.ordRemarks as ordRemarks, "
			+ "orderDetails.ordCancelled as ordCancelled, "
			+ "orderDetails.ordCancelReasonId as ordCancelReasonId, "
			+ "orderDetails.ordCancelDatetime as ordCancelDatetime, "
			+ "orderDetails.isDrug as isDrug, "
			+ "orderDetails.status as status, "
			+ "orderDetails.updatedBy as updatedBy, "
			+ "orderDetails.updatedDate as updatedDate, "
			+ "orderDetails.createdDate as createdDate, "
			+ "orderDetails.createdBy as createdBy, "
			+ "orderDetails.oldOrdDtlId as oldOrdDtlId, "
			+ "orderDetails.isPackageService as isPackageService,"
			+ " orderDetails.isCapService as isCapService, "
			+ "unitService.isRateEditable as isRateEditable, "
			+ "unitService.rateEditbleMinValue as minRateEditable, "
			+ "unitService.rateEditableMaxValue as maxRateEditable, "
			+ "unitService.isTaxApplicable as isTaxApplicable, "
			+ "unitService.isRefDoctorShare as isRefDoctorShare, "
			+ "unitService.isDiscount as isDiscountApplicable, "
			+ "unitService.discountValue as discountValue, "
			+ "unitService.gstTypeId as gstTypeId, "
			+ "unitService.otcGstTypeId as otcGstTypeId, "
			+ "unitService.isQuantityEditable as isQuantityEditable, "
			+ "(SELECT coalesce(mpack.packageTypeId,0) from MPackageMaster mpack where mpack.packageMasterId = orderDetails.packageId) as packageTypeId, "
			+ "speciality.specialityName as specialityName, subSpec.subSpecialityMasterName as subSpecialityName, "
			+ "orderDetails.saleDetailId as saleDetailId, "
			+ "concat('Dr. ',coalesce(doc.firstName),' ',coalesce(doc.middleName),' ',coalesce(doc.lastName)) as doctorName,orderDetails.isPackage as isPackage "
			+ "FROM OrderDetailsMaster orderDetails "
			+ "INNER JOIN orderDetails.orderMaster ord "
			+ "LEFT JOIN orderDetails.serviceMaster service "
			+ "LEFT JOIN service.specialityMaster speciality "
			+ "LEFT JOIN service.subSpecialityMaster subSpec "
			+ "LEFT JOIN service.unitServiceMasterDetailsList unitService "
			+ "LEFT JOIN orderDetails.doctorMaster doc "
			+ "LEFT JOIN orderDetails.tax tax "
			+ "WHERE "
			+ "  orderDetails.status='A' "
			+ "AND orderDetails.saleDetailId=:saleDetailId "),
		
		
		
		
		
	/*
		@NamedQuery(name="GET_CUEERENT_ORDERED_INVESTIGATION",query="SELECT orderDetails.orderDetailsId as orderDetailsId, "
				+ "ord.orderId as orderId, "
				+ "orderDetails.serviceId as serviceMasterId, "
				+ "service.serviceStandardName as serviceStandardName, "
				+ "service.serviceStandardCode as serviceStandardCode, "
				+ "to_char(orderDetails.createdDate,'dd/MM/yyyy') as createdDate, "
				+ "speciality.specialityName as specialityName "
				+ "FROM OrderDetailsMaster orderDetails "
				+ "INNER JOIN orderDetails.orderMaster ord "
				+ "INNER JOIN orderDetails.serviceMaster service "
				+ "INNER JOIN service.specialityMaster speciality "
				+ "WHERE orderDetails.status='A' "
				+ "AND service.status='A' "
				+ "AND orderDetails.isInvestigationOrder='Y' "
//				+ "AND ord.encounterId=:encounterId "
				+ "AND orderDetails.serviceIsBilled=0 "),*/

		@NamedQuery(name = "CHANGE_GLOBAL_SERVICE_MASTER_STATUS", query = " UPDATE ServiceMaster SET status=:status "
				+ " WHERE serviceMasterId =:serviceMasterId AND organizationId =:organizationId") ,
		
		@NamedQuery(name="GET_SERVICES_FOR_ADDENDUM_BILL",
		query="SELECT distinct orderDetails.orderDetailsId as orderDetailsId, "
			+ "orderDetails.orgId as orgId, ord.orderNo as orderNo, orderDetails.orgUnitId as orgUnitId, orderDetails.orderId as orderId, "
			+ "orderDetails.orderQty as quantity, orderDetails.serviceId as serviceMasterId, service.serviceStandardName as serviceStandardName, "
			+ "service.serviceStandardCode as serviceStandardCode, orderDetails.priorityId as priorityId, orderDetails.isOutsourced as isOutsourced,"
			+ "to_char(orderDetails.orderDate,'yyyy-mm-dd') as orderDateString, to_char(orderDetails.orderSchDate,'yyyy-mm-dd') as orderSchDateString, "
			+ "orderDetails.serviceAmt as basePrice, orderDetails.ordConcession as concession, orderDetails.ordDiscount as discount, "
			+ "orderDetails.netAmount as netPay, orderDetails.netAmount as oldNetPay, orderDetails.ordDocSplId as specialityId, "
			+ "service.specialityId as serviceSpecialityId, service.subSpecialityId as serviceSubSpecialityId, orderDetails.taxId as taxId, "
			+ "tax.name as taxName, orderDetails.taxPer as taxPercentage, orderDetails.taxAmount as taxAmt, coalesce(orderDetails.selfPayable,0) as selfPayable,"
			+ "coalesce(orderDetails.creditPayable,0) as creditPayable, orderDetails.payeeId as payeeId, orderDetails.packageConsumptionAmt as packageConsumptionAmt, "
			+ "orderDetails.orderSetId as orderSetId, orderDetails.contractId as contractId, orderDetails.bedCategoryId as bedCategoryId, "
			+ "orderDetails.coPayPer as coPayPer, orderDetails.docSharePer as docSharePer, orderDetails.ordTotalAmt as totalAmt, "
			+ "orderDetails.ordCancelBy as ordCancelBy, orderDetails.ordCancelRemark as ordCancelRemark, orderDetails.tariffId as tariffId, "
			//+ "orderDetails.billingClassId as billingClassId, orderDetails.bedId as bedId, orderDetails.roomId as roomId, "
			+ "orderDetails.wardId as wardId, orderDetails.orderEmergencyFlag as orderEmergencyFlag, orderDetails.packageId as packageId, "
			+ "orderDetails.ordDocId as doctorId, orderDetails.drugId as drugId, orderDetails.batchId as batchId, orderDetails.oldOrdDtlId as oldOrdDtlId, "
			+ "orderDetails.serviceRendered as serviceRendered, orderDetails.serviceChargeable as serviceChargeable, orderDetails.serviceRenderingDeptId as serviceRenderingDeptId, "
			+ "orderDetails.ordRenderDatetime as ordRenderDatetime, orderDetails.serviceIsBilled as serviceIsBilled, orderDetails.serviceBillId as serviceBillId, "
			+ "orderDetails.ordRemarks as ordRemarks, orderDetails.ordCancelled as ordCancelled, orderDetails.ordCancelReasonId as ordCancelReasonId, "
			+ "orderDetails.ordCancelDatetime as ordCancelDatetime, orderDetails.isDrug as isDrug, orderDetails.status as status, orderDetails.updatedBy as updatedBy, "
			+ "orderDetails.updatedDate as updatedDate, orderDetails.createdDate as createdDate, orderDetails.createdBy as createdBy, orderDetails.oldOrdDtlId as oldOrdDtlId, "
			+ "orderDetails.isPackageService as isPackageService, orderDetails.isCapService as isCapService, unitService.isRateEditable as isRateEditable, "
			+ "unitService.rateEditbleMinValue as minRateEditable, unitService.rateEditableMaxValue as maxRateEditable, unitService.isTaxApplicable as isTaxApplicable, "
			+ "unitService.isRefDoctorShare as isRefDoctorShare, unitService.isDiscount as isDiscountApplicable, unitService.discountValue as discountValue, "
			+ "unitService.gstTypeId as gstTypeId, unitService.otcGstTypeId as otcGstTypeId, unitService.isQuantityEditable as isQuantityEditable, "
			+ "(SELECT coalesce(mpack.packageTypeId,0) from MPackageMaster mpack where mpack.packageMasterId = orderDetails.packageId) as packageTypeId, "
			+ "speciality.specialityName as specialityName, subSpec.subSpecialityMasterName as subSpecialityName, concat('Dr. ',coalesce(doc.firstName),' ',coalesce(doc.middleName),' ',coalesce(doc.lastName)) as doctorName,orderDetails.isPackage as isPackage "
			+ "FROM OrderDetailsMaster orderDetails "
			+ "INNER JOIN orderDetails.orderMaster ord "
			+ "INNER JOIN orderDetails.billingDetailsList billdtls "
			+ "INNER JOIN billdtls.serviceMaster service "
			+ "INNER JOIN billdtls.billingMaster billmst "
			+ "LEFT JOIN service.specialityMaster speciality "
			+ "LEFT JOIN service.subSpecialityMaster subSpec "
			+ "INNER JOIN service.unitServiceMasterDetailsList unitService "
			+ "LEFT JOIN orderDetails.doctorMaster doc "
			+ "LEFT JOIN orderDetails.tax tax "
			+ "WHERE billmst.billingMasterId=:billId "
			//+ "AND orderDetails.unitId = unitService.unitId "
			),
		
		@NamedQuery(name = "GET_PAGINATED_SERVICE_MASTER_DETAILS_LIST_SEARCH", 
		query = "SELECT service.serviceMasterId as serviceMasterId , "
			+ " service.codificationServiceName as codificationServiceName , "
			+ " speciality.specialityName as specialityName , "
			+ " subspeciality.subSpecialityMasterName as subSpecialityName, "
			+ " service.serviceStandardName as serviceStandardName , "
			+ " service.serviceStandardCode as serviceStandardCode , "
			+ " to_char(service.validFrom,'dd/mm/yyyy') as validFrom , "
			+ " to_char(service.validTill,'dd/mm/yyyy') as validTill , "
			+ " glm.generalLedgerId as generalLedgerId , "
			+ " glm.generalLedgerName as generalLedgerName , "
			+ " service.status as  status "
			+ " from ServiceMaster service "
			+ " INNER JOIN service.specialityMaster speciality "
			+ " LEFT JOIN service.subSpecialityMaster subspeciality "
			+ " LEFT JOIN service.generalLedgerMaster glm "
			+ " WHERE service.organizationId=:orgId "
			+ " AND (lower(service.codificationServiceName) like :serviceStandardName "
			+ " OR lower(speciality.specialityName) like :serviceStandardName "
			+ " OR lower(subspeciality.subSpecialityMasterName) like :serviceStandardName "
			+ " OR lower(service.serviceStandardName) like :serviceStandardName "
			+ " OR lower(service.serviceStandardCode) like :serviceStandardName "
			+ " OR to_char(service.validFrom,'DD/MM/YYYY') like :serviceStandardName "
			+ " OR to_char(service.validTill,'DD/MM/YYYY') like :serviceStandardName "
			+ " OR lower(glm.generalLedgerName) like :serviceStandardName ) " 
			+ " ORDER BY service.serviceMasterId DESC"),
		
	})


@NamedNativeQueries({
		@NamedNativeQuery(name = "GET_PAGINATED_SERVICE_MASTER_LIST", 
				query = "SELECT service.service_master_id as \"serviceMasterId\", "
						+ "service.codification_service_name as \"codificationServiceName\", "
						+ "speciality.speciality_name as \"specialityName\", "
						+ "subspeciality.sub_speciality_name as \"subSpecialityName\", "
				+ " service.service_standard_name as\"serviceStandardName\","
				+ " service.service_standard_code as\"serviceStandardCode\","
				+ " to_char(service.valid_from,'dd/mm/yyyy') as\"validFrom\","
				+ " to_char(service.valid_till,'dd/mm/yyyy') as\"validTill\","
				+ " glm.general_ledger_id as\"generalLedgerId\","
				+ " glm.general_ledger_name as\"generalLedgerName\","
				+ " service.status as \"status\" "
				+ " from service.m_service_master service"
				+ " INNER JOIN doctor.m_speciality_master as speciality ON speciality.speciality_id = service.speciality_id"
				+ " LEFT JOIN doctor.m_sub_speciality_master as subspeciality ON subspeciality.sub_speciality_id = service.sub_speciality_id"
				+ " LEFT JOIN public.m_general_ledger_master AS glm ON glm.general_ledger_id=service.general_ledger_id"
				+ " WHERE service.organization_id=:orgId " 
				+ " ORDER BY service.service_master_id DESC"),
		
		@NamedNativeQuery(name = "GET_ALL_SERVICE_MASTER_LIST", 
		query = "SELECT service.service_master_id as \"serviceMasterId\", "
		+ " service.service_standard_name as\"serviceStandardName\","
		+ " service.service_standard_code as\"serviceStandardCode\" "
		+ " from service.m_service_master service "
		+ " WHERE service.organization_id=:orgId "
		+ "	and service.service_standard_name is not null "
		+ "	and service.service_standard_code is not null " 
		+ " ORDER BY service.service_master_id DESC"),

		@NamedNativeQuery(name = "GET_SERVICE_MASTER_COUNT", query = "select count(*) from service.m_service_master where "
				+ "organization_id = :orgId"),



	
	
	@NamedNativeQuery(name="GET_AUTO_RENDERED_SERVICES_BY_PATIENT",
				query = " select details.order_details_id as \"orderDetailsId\", "
						+ "orderm.order_id as \"orderId\", "
						+ "service.service_master_id as \"serviceMasterId\", "
						+ "sm.speciality_name as \"specialityName\", "
						+ " service.service_standard_name as \"serviceStandardName\", "
						+ " service.service_standard_code as \"serviceStandardCode\","
						+ " to_char(orderm.order_date,'yyyy-mm-dd') as \"orderDateString\", "
						+ " details.order_date as \"orderDate\", details.package_id as \"packageMasterId\", "
						+ " CAST ('0' AS DOUBLE PRECISION) as \"concession\", "
						+ " CAST ('1' AS DOUBLE PRECISION) as \"quantity\" , "
						+ " serdtl.is_tax_applicable as \"isTaxApplicable\","
						+ " tax.id as \"taxId\", "
						+ " tax.tax_percentage as \"taxPercentage\", "
						+ " tax.name as \"taxName\", "
						+ " details.ord_doc_id as \"doctorId\", "
						+ " details.ord_doc_spl_id as \"specialityId\", "
						+ " (case when details.service_rendering_deptid is not null then "
						+ " details.service_rendering_deptid "
						+ "else "
						+ " service.speciality_id end) as \"serviceRenderingDeptId\", "
						+ " serdtl.is_doc_req as \"isDocReq\", " 
						+ " serdtl.is_ref_doctor_share as \"isRefReq\" "
						+ " FROM public.t_order_details details "
						+ " INNER JOIN public.t_order_master orderm on details.order_id = orderm.order_id "
						+ " INNER JOIN service.m_service_master service  on service.service_master_id = details.service_id "
						+ " INNER JOIN service.t_unit_service_master_details serdtl on service.service_master_id = serdtl.service_master_id "
						+ " INNER JOIN doctor.m_speciality_master sm on service.speciality_id = sm.speciality_id "
						+ " LEFT JOIN public.m_tax tax on tax.id=serdtl.gst_type_id "
						+ " WHERE orderm.patient_id=:patientId "
						+ " AND details.service_is_billed=0 "
						+ " AND orderm.org_unit_id=:unitId "
						+ " AND orderm.org_id=:orgId "
						+ " AND serdtl.organization_id=:orgId "
						+ " AND serdtl.unit_id=:unitId "
						+ " AND details.service_id=(select patient_reg_ser_id from t_unit_application_billing_configuration where unit_id=orderm.org_unit_id and status='A' )"),
	
		@NamedNativeQuery(name = "GET_SERVICE_MASTER_BY_ID",
		query = " SELECT service.service_master_id as \"serviceMasterId\","
				+ " service.organization_id as \"organizationId\","
				+ " service.service_standard_name as \"serviceStandardName\", "
				+ "service.speciality_id as \"specialityId\","
				+ "service.codification_service_name as \"codificationServiceName\", "	
				+ " service.service_standard_code as\"serviceStandardCode\","
				+ " to_char(service.valid_from,'yyyy/mm/dd') as\"validFrom\","
				+ " to_char(service.valid_till,'yyyy/mm/dd') as\"validTill\","
				+ " glm.general_ledger_id as\"generalLedgerId\","
				+ " glm.general_ledger_name as\"generalLedgerName\","
				+ " subspeciality.sub_speciality_name as \"subSpecialityName\", "
				+ " service.sub_speciality_id as \"subSpecialityId\"" 
				+ " FROM service.m_service_master service "
				+ " INNER JOIN doctor.m_sub_speciality_master as subspeciality ON subspeciality.sub_speciality_id = service.sub_speciality_id "
				+ " LEFT JOIN public.m_general_ledger_master AS glm ON glm.general_ledger_id=service.general_ledger_id "
				+ " WHERE service.service_master_id =:serviceMasterId AND service.organization_id=:orgId"),
		
		@NamedNativeQuery(name = "GET_SERVICE_MASTER_DETAILS_BY_SERVICE_MASTER_ID",
		query = " SELECT service.service_master_id as \"serviceMasterId\","
				+ " service.organization_id as \"organizationId\","
				+ " service.service_standard_name as \"serviceStandardName\", "
				+ "service.speciality_id as \"specialityId\","
				+ "service.codification_service_name as \"codificationServiceName\", "	
				+ " service.service_standard_code as\"serviceStandardCode\","
				+ " to_char(service.valid_from,'yyyy/mm/dd') as\"validFrom\","
				+ " to_char(service.valid_till,'yyyy/mm/dd') as\"validTill\","
				+ " service.sub_speciality_id as \"subSpecialityId\"" 
				+ " FROM service.m_service_master service "
				+ " WHERE service.service_master_id =:serviceMasterId"),

		@NamedNativeQuery(name = "GET_SERVICE_MASTER_BY_SPECIALITY_AND_SUB_SPECIALITY",
			query = "SELECT service.service_master_id as \"serviceMasterId\", "
				+ "speciality.speciality_name as \"specialityName\","
				+ "subspeciality.sub_speciality_name as \"subSpecialityName\", "
				+ "service.service_standard_name as\"serviceStandardName\", "
				+ "service.status as \"status\"  "
				+ "FROM service.m_service_master service"
				+ " INNER JOIN doctor.m_speciality_master as speciality ON speciality.speciality_id = service.speciality_id"
				+ " INNER JOIN doctor.m_sub_speciality_master as subspeciality ON subspeciality.sub_speciality_id = service.sub_speciality_id"
				+ " WHERE service.organization_id=:orgId" 
				+ "   AND service.speciality_id =:specialityId"
				+ "   AND service.sub_speciality_id =:subSpecialityId" 
				+ "   AND service.status ='A' "
				+ " ORDER BY subspeciality.sub_speciality_name ASC"),

		@NamedNativeQuery(name = "GET_PAGINATED_SERVICE_MASTER_LIST_WITH_BASEPRICE_BYVISITTYPE", query = "SELECT service.service_master_id as \"serviceMasterId\", speciality.speciality_name as \"specialityName\", subspeciality.sub_speciality_name as \"subSpecialityName\","
				+ " service.service_standard_name as \"serviceStandardName\",service.status as \"status\", basePrice.base_price as \"basePrice\"  "
				+ " FROM service.m_service_master as service "
				+ " INNER JOIN doctor.m_speciality_master as speciality ON speciality.speciality_id = service.speciality_id"
				+ " INNER JOIN doctor.m_sub_speciality_master as subspeciality ON subspeciality.sub_speciality_id = service.sub_speciality_id"
				+ " INNER JOIN service.m_service_base_price_master as basePrice on basePrice.service_master_id = service.service_master_id"
				+ " WHERE service.organization_id= :orgId "
				+ " AND basePrice.unit_id=:unitId "
				+ " AND basePrice.visit_type_id= :visitTypeId "
				+ " AND basePrice.from_date <= now() "
				+ " AND basePrice.to_date >= now()"
				+ " ORDER BY service.service_master_id DESC "),

		@NamedNativeQuery(name = "GET_SERVICE_DETAILS_WITH_BASEPRICE_BYSERVICELIST", query = "SELECT service.service_master_id as \"serviceMasterId\", "
				+ " service.speciality_id as \"specialityId\", " 
				+ " service.sub_speciality_id as \"subSpecialityId\", "
				+ " speciality.speciality_name as \"specialityName\", "
				+ " subspeciality.sub_speciality_name as \"subSpecialityName\","
				+ " service.service_standard_name as \"serviceStandardName\"," 
				+ " service.status as \"status\", "
				+ " basePrice.base_price as \"basePrice\", "
				+ "	service.service_standard_code as \"serviceStandardCode\" " 
				+ " FROM service.m_service_master as service "
				+ " INNER JOIN doctor.m_speciality_master as speciality ON speciality.speciality_id = service.speciality_id"
				+ " INNER JOIN doctor.m_sub_speciality_master as subspeciality ON subspeciality.sub_speciality_id = service.sub_speciality_id "
				+ " LEFT JOIN service.m_service_base_price_master as basePrice on basePrice.service_master_id = service.service_master_id "
				+ " WHERE basePrice.organisation_id =:orgId "
				+ " and basePrice.unit_id =:unitId " 
				+ " AND service.service_master_id IN (:listServiceId) "
				+ " ORDER BY service.service_master_id "),

		@NamedNativeQuery(name = "GET_SERVICE_DETAILS_BY_ID", query = " select service.service_standard_name as \"serviceStandardName\" ,"
				+ "service.service_standard_code as \"serviceStandardCode\","
				+ " speciality.speciality_id as \"specialityId\", "
				+ "speciality.speciality_name as \"specialityName\","
				+ " sub_speciality.sub_speciality_id as \"subSpecialityId\", "
				+ "sub_speciality.sub_speciality_name as \"subSpecialityName\" "
				+ " from service.t_unit_service_mapper mapper"
				+ " inner join service.m_service_master service on service.service_master_id  = mapper.service_id"
				+ " inner join service.t_unit_service_master_details details on details.service_master_id = service.service_master_id"
				+ " inner join doctor.m_speciality_master speciality on service.speciality_id = speciality.speciality_id"
				+ " inner join doctor.m_sub_speciality_master sub_speciality on service.sub_speciality_id = sub_speciality.sub_speciality_id"
				+ " WHERE mapper.unit_id = details.unit_id "
				+ " AND mapper.unit_id =:unitId " 
				+ " AND mapper.orgnisation_id=:orgId " 
				+ " AND service.status='A' "
				+ " AND service.service_master_id =:serviceId "),

		
		@NamedNativeQuery(name = "SEARCH_SERVICE_FOR_PACKAGES", query = "SELECT "
				+ "	serv.service_master_id AS \"serviceMasterId\", " + "	sp.speciality_id AS \"specialityId\", "
				+ "	sp.speciality_name AS \"groupDesc\", " 
				+ "	subsp.sub_speciality_id AS \"subSpecialityMasterId\", "
				+ "	subsp.sub_speciality_name AS \"subGroup\", " 
				+ "	serv.service_standard_code AS \"serviceCode\", "
				+ "	serv.service_standard_name AS \"serviceDescription\" , "
				+ "	petm.payment_entitlement_desc  AS \"paymentEntitlement\", "
				+ "	pcm.patient_category AS \"patientType\", " 
				+ "	vistmst.visit_type_code  AS \"visitTypeName\", "
				+ "	bbcm.billing_bed_desc  AS \"billingBedCategory\", " 
				+ "	Coalesce(srvtrf.rate, NULL) AS \"rate\", "
				+ "	Coalesce(usmd.is_doc_req, 'N') AS \"isDoctorRequired\", "
				+ "	Coalesce(usmd.is_ref_doctor_share, 'N') AS \"isDoctorReferralRequired\", "
				+ "	To_char(srvtrf.effective_date, 'DD-MM-YYYY') AS \"effectiveDate\" " + "FROM "
				+ "	service.m_service_master serv " 
				+ " INNER JOIN doctor.m_speciality_master sp ON "
				+ "	sp.speciality_id = serv.speciality_id " 
				+ " INNER JOIN doctor.m_sub_speciality_master subsp ON "
				+ "	subsp.sub_speciality_id = serv.sub_speciality_id "
				+ " INNER JOIN service.m_unit_service_tariff_master srvtrf ON "
				+ "	srvtrf.service_id = serv.service_master_id "
				+ " INNER JOIN service.t_unit_service_master_details usmd ON "
				+ "	usmd.service_master_id = serv.service_master_id "
				+ " INNER JOIN public.m_payment_entitlement_master petm ON "
				+ "	petm.payment_entitlement_id = srvtrf.payment_entitlement_id "
				+ " INNER JOIN public.m_patient_category_master pcm ON "
				+ "	pcm.patient_category_id = srvtrf.patient_type_id "
				+ " INNER JOIN public.m_visit_type_master vistmst ON "
				+ "	vistmst.visit_type_id = srvtrf.visit_type_id "
				+ " INNER JOIN adt.m_billing_bed_category_master bbcm ON "
				+ "	bbcm.billing_bed_category_id = srvtrf.billing_bed_category_id " + "WHERE " + "	serv.status = 'A' "
				+ "	AND usmd.organization_id = :orgId " 
				+ "	AND srvtrf.unit_id = usmd.unit_id "
				+ "	AND usmd.unit_id =:unitId"),
		 
		
		@NamedNativeQuery(name = "SEARCH_SERVICE_FOR_PACKAGES_ADD", query ="select "
				+ "	service.service_master_id AS \"serviceMasterId\", "
				+ "	speciality.speciality_id AS \"specialityId\", "
				+ "	speciality.speciality_name AS \"groupDesc\", "
				+ "	sub_speciality.sub_speciality_id AS \"subSpecialityMasterId\", "
				+ "	sub_speciality.sub_speciality_name AS \"subGroup\", "
				+ "	service.service_standard_code AS \"serviceCode\", "
				+ "	service.service_standard_name AS \"serviceDescription\" "
				+ "from "
				+ "	service.t_unit_service_mapper mapper "
				+ "inner join service.m_service_master service on "
				+ " mapper.service_id = service.service_master_id "
				+ "inner join service.t_unit_service_master_details details on "
				+ "	details.service_master_id = service.service_master_id "
				+ "inner join doctor.m_speciality_master speciality on "
				+ "	service.speciality_id = speciality.speciality_id "
				+ "inner join doctor.m_sub_speciality_master sub_speciality on "
				+ "	service.sub_speciality_id = sub_speciality.sub_speciality_id "
				+ "left join public.m_tax tax on "
				+ "	tax.id = details.gst_type_id "
				+ "where "
				+ "	mapper.unit_id = details.unit_id and mapper.unit_id =:unitId "
				+ "	and mapper.orgnisation_id =:orgId AND details.status = 'A' and details.is_package = 'I' "
				+ "	and service.status = 'A' "
				+ "	and mapper.status = 'A' "
				//+ " AND :currentDate between to_char(service.valid_from, 'YYYY-MM-DD') and to_char(service.valid_till, 'YYYY-MM-DD') " 			
				),
		
		
		@NamedNativeQuery(name = "GET_MAX_TERRIF_DATE", query ="SELECT "
				+ "	max(srvtrf.effective_date) "
				+ " FROM "
				+ "	service.m_service_master serv "
				+ " INNER JOIN doctor.m_speciality_master sp ON "
				+ "	sp.speciality_id = serv.speciality_id "
				+ " INNER JOIN doctor.m_sub_speciality_master subsp ON "
				+ "	subsp.sub_speciality_id = serv.sub_speciality_id "
				+ " INNER JOIN service.t_unit_service_master_details usmd ON "
				+ "	usmd.service_master_id = serv.service_master_id "
				+ " INNER JOIN service.m_unit_service_tariff_master srvtrf ON "
				+ "	srvtrf.service_id = serv.service_master_id "
				+ " INNER JOIN public.m_payment_entitlement_master petm ON "
				+ "	petm.payment_entitlement_id = srvtrf.payment_entitlement_id "
				+ " INNER JOIN public.m_patient_category_master pcm ON "
				+ "	pcm.patient_category_id = srvtrf.patient_type_id "
				+ " INNER JOIN public.m_visit_type_master vistmst ON "
				+ "	vistmst.visit_type_id = srvtrf.visit_type_id "
				+ " INNER JOIN adt.m_billing_bed_category_master bbcm ON "
				+ "	bbcm.billing_bed_category_id = srvtrf.billing_bed_category_id "
				+ " WHERE "
				+ "	serv.status = 'A' "
				+ "	AND usmd.organization_id = :orgId "
				+ "	AND srvtrf.unit_id = usmd.unit_id and usmd.status = 'A'"
				+ "	AND usmd.unit_id =:unitId AND srvtrf.status = 'A' "),
		
		
		
		@NamedNativeQuery(name = "GET_ITEM_BY_ITEM_IDS", query =  "select "
				+ "		item.id as \"itemId\", "
				+ "		item.code as \"itemCode\", "
				+ "		item.\"name\" as \"itemName\" "
				+ "	from "
				+ "		public.m_item item "
				+ "	where "
				+ "		item.id in (:itemIds) "
				+ "		and item.organization_id =:orgId "
				+ "		and item.unit_id =:unitId"),
		
		
		
		
		@NamedNativeQuery(name = "GET_SERVICE_MASTER_BY_SPECIALITY_AND_SUB_SPECIALITY_NOT_MAPPED",
		query = "SELECT service.service_master_id as \"serviceMasterId\", speciality.speciality_name as \"specialityName\", subspeciality.sub_speciality_name as \"subSpecialityName\", "
			+ " service.service_standard_name as\"serviceStandardName\","
		    + " service.service_standard_code as \"serviceStandardCode\",service.status as \"status\" "
			+ " from service.m_service_master service"
			+ " INNER JOIN doctor.m_speciality_master as speciality ON speciality.speciality_id = service.speciality_id"
			+ " INNER JOIN doctor.m_sub_speciality_master as subspeciality ON subspeciality.sub_speciality_id = service.sub_speciality_id"
			+ " WHERE service.organization_id=:orgId" 
			+ " AND service.speciality_id =:specialityId"
			+ " AND service.sub_speciality_id =:subSpecialityId" 
			+ " AND service.status ='A' "),
		
	/*	@NamedNativeQuery(name="GET_CUEERENT_ORDERED_INVESTIGATION",query="with investigations as( " 
				 + " 	( " 
				 + " 		select " 
				 + " 			ord_mst.order_id as \"orderId\", " 
				 + " 			service_mst.speciality_id as \"specialityId\","
				 + " 			service_mst.sub_speciality_id as \"subDeptId\","
				 + " 			service_mst.sub_speciality_id as \"subSpecialityId\","
				 + " 			ord_mst.admission_id as \"admissionId\", " 
				 + " 			ord_mst.encounter_id as \"encounterId\", " 
				 + " 			ord_mst.patient_id as \"patientId\", " 
				 + " 			ord_dtls.order_details_id as \"orderDetailsId\", " 
				 + " 			ord_dtls.service_id as \"serviceMasterId\", " 
				 + " 			ord_dtls.order_set_id as \"orderSetId\", " 
				 + " 			ord_dtls.priority_id as \"priorityId\", " 
				 + " 			ord_dtls.ord_remarks as \"ordRemarks\", " 
				 + "            to_char(ord_dtls.order_sch_date,'dd/MM/yyyy') as \"orderSchDate\", "
				 + " 			service_mst.service_standard_name as \"serviceStandardName\", " 
				 + " 			service_mst.service_standard_code as \"serviceStandardCode\", " 
				 + " 			speciality_mst.speciality_name as \"specialityName\", " 
				 + " 			sample_dtls.lab_sample_dtls_id as \"labSampleDtlsId\", " 
				 + " 			service_dtls.is_panel as \"isPanel\", " 
				 + " 			max( lab_res.lab_test_res_id ) as \"labTestResId\", " 
				 + " 			cast( " 
				 + " 				null as integer " 
				 + " 			) as \"panelId\", "
				 + "        (case when sample_dtls.micro_template_status_id is not null then test_status2.status_name else test_status.status_name end) as \"statusName\", " 
				 + " 			(case when sample_dtls.micro_template_status_id is not null then test_status2.test_status_id else test_status.test_status_id end) as \"sampleStatusId\", " 
				 + " 			to_char( " 
				 + " 				ord_dtls.created_date, " 
				 + " 				'dd/MM/yyyy' " 
				 + " 			) as \"createdDate\", " 
				 + " 			to_char( " 
				 + " 				ord_dtls.created_date, " 
				 + " 				'yyyy/mm/dd' " 
				 + " 			) as \"createdDateNew\", " 
				 + " 			ord_dtls.service_rendered as \"serviceRendered\", " 
				 + " 			ord_dtls.is_package_service as \"isPackageService\", " 
				 + " 			templatetr12_.template_transaction_id as \"templateTransactionId\","
				 + "            (case when (sample_dtls.micro_template_status_id is not null and sample_dtls.micro_template_status_id=41)"
				 + "            then microTemp.template_object else templatetr12_.template_object end) as \"templateObject\", " 
				 + " 			ord_dtls.service_is_billed as \"serviceIsBilled\", " 
				 + " 			ord_dtls.status as \"orderStatus\", " 
				 + " 			am.appointment_id as \"appointmentId\", " 
				 + " 			am.slot_id as \"slotId\","
				 + " sample_dtls.micro_template_status_id as \"microTemplateStatusId\" , " 
				 + " (case " 
				 + " when empmst.employee_id is not null then concat(empmst.first_name, ' ', empmst.last_name) "  
				 + " when docmst.doctor_id is not null then concat('Dr. ', docmst.first_name, ' ', docmst.last_name) "  
				 + " else 'Admin' end) as \"userName\","
				 + " 			ord_dtls.package_id as \"packageId\" " 
				 + " 			from 	public.t_order_master ord_mst " 
				 + " 		inner join public.t_order_details ord_dtls on " 
				 + " 			ord_mst.order_id = ord_dtls.order_id " 
				 + " 		inner join service.t_unit_service_mapper service_mppr on " 
				 + " 			service_mppr.service_id = ord_dtls.service_id " 
				 + " 			and service_mppr.unit_id = ord_dtls.org_unit_id " 
				 + " 		inner join service.t_unit_service_master_details service_dtls on " 
				 + " 			service_dtls.service_master_id = service_mppr.service_id " 
				 + " 			and service_dtls.unit_id = ord_dtls.org_unit_id " 
				 + " 		inner join service.m_service_master service_mst on " 
				 + " 			service_mst.service_master_id = ord_dtls.service_id " 
				 + " 		left join doctor.m_speciality_master speciality_mst on " 
				 + " 			speciality_mst.speciality_id = service_mst.speciality_id " 
				 + " 		left join t_unit_speciality_mapper unitSplMpr on "
				 + " 			unitSplMpr.speciality_id = service_mst.speciality_id "
				 + " 			and unitSplMpr.unit_id = service_mppr.unit_id "
				 + " 		left join lab.t_lab_sample_dtls sample_dtls on " 
				 + " 			sample_dtls.order_details_id = ord_dtls.order_details_id " 
				 + " 			and sample_dtls.sample_status_id <> 4 " 
				 + " 		left join lab.m_test_status_master test_status on " 
				 + " 			test_status.test_status_id = sample_dtls.sample_status_id "
				 + "        	left join lab.m_test_status_master test_status2 on "  
				 + " 			test_status2.test_status_id = sample_dtls.micro_template_status_id 	" 
				 + " 		left join lab.t_lab_test_result lab_res on " 
				 + " 			lab_res.lab_sample_dtls_id = sample_dtls.lab_sample_dtls_id " 
				 + " 			and lab_res.sample_status_id in( " 
				 + " 				8, " 
				 + " 				9 " 
				 + " 			) " 
				 + " 		left outer join public.t_template_transaction templatetr12_ on " 
				 + " 			ord_dtls.order_details_id = templatetr12_.order_details_id " 
				 + " 			and( " 
				 + " 				templatetr12_.status = 'A' " 
				 + " 			) " 
				 + " 		left join opd.m_appointment_master am on " 
				 + " 			am.order_details_id = ord_dtls.order_details_id " 
				 + " 		left join t_unit_application_billing_configuration uabc on " 
				 + " 			uabc.unit_id = ord_dtls.org_unit_id and uabc.status='A' " 
				 + " left join public.m_user_master um on ord_mst.created_by = um.user_id "  
				 + " left join public.m_employee_master empmst on um.user_id = empmst.user_id " 
				 + " left join doctor.m_doctor_master docmst on um.user_id = docmst.user_id "
				 + " left join lab.t_microbiology_template microTemp on microTemp.lab_sample_dtls_id=sample_dtls.lab_sample_dtls_id "
				 + " 		where " 
				 + " 			service_mst.status = 'A' " 
				 + " 			and ord_dtls.old_ord_dtl_id is null " 
				 + " 			and service_dtls.is_package = 'I' " 
				 + " 			and ord_dtls.nursing_service_order_flag = 'N' "
				 + " 			and service_dtls.is_panel = 'N' "
				 + "			and unitSplMpr.is_rendering_speciality = 'Y' "
				 + "   and ord_dtls.is_procedure_service = 'N'  "
//				 + "            and (ord_mst.encounter_id is not null "
//				 + " 			or ord_mst.admission_id is not null )"
				 + " 			and (service_mst.speciality_id in( " 
				 + " 					uabc.radiology_speciality_id, " 
				 + " 					uabc.physiotherapy_speciality_id, " 
				 + " 					uabc.hemodialysis_speciality_id " 
				 + " 			) " 
				 + " 			OR service_mst.speciality_id in( " 
				 + " 				select " 
				 + " 					lab.lab_speciality_id " 
				 + " 				from " 
				 + " 					t_lab_configuration lab " 
				 + " 				where " 
				 + " 					lab.unit_id = ord_mst.org_unit_id " 
				 + " 					and lab.status = 'A' " 
				 + " 			)) " 
				 + " 		group by " 
				 + " 			\"orderId\", " 
				 + " 			\"specialityId\", " 
				 + " 			\"orderDetailsId\", " 
				 + " 			\"serviceMasterId\", " 
				 + " 			\"serviceStandardName\", " 
				 + " 			\"serviceStandardCode\", " 
				 + " 			\"specialityName\", " 
				 + " 			\"labSampleDtlsId\", " 
				 + " 			\"isPanel\", " 
				 + " 			\"subDeptId\", " 
				 + " 			\"panelId\", " 
				 + " 			\"statusName\", " 
				 + " 			\"createdDate\", " 
				 + " 			\"sampleStatusId\", " 
				 + " 			\"templateTransactionId\","
				 + "          \"templateObject\", " 
				 + " 			\"appointmentId\", " 
				 + " 			\"slotId\", " 
				 + "            \"userName\" "
				 + " 	) " 
				 + " union( " 
				 + " 	select " 
				 + " 		ord_mst.order_id as \"orderId\", " 
				 + " 		service_mst.speciality_id as \"specialityId\","
				 + " 		service_mst.sub_speciality_id as \"subDeptId\","
				 + " 		service_mst.sub_speciality_id as \"subSpecialityId\","
				 + " 		ord_mst.admission_id as \"admissionId\", " 
				 + " 		ord_mst.encounter_id as \"encounterId\", " 
				 + " 		ord_mst.patient_id as \"patientId\", " 
				 + " 		ord_dtls.order_details_id as \"orderDetailsId\", " 
				 + " 		ord_dtls.service_id as \"serviceMasterId\", " 
				 + " 			ord_dtls.order_set_id as \"orderSetId\", " 
				 + " 			ord_dtls.priority_id as \"priorityId\", " 
				 + " 			ord_dtls.ord_remarks as \"ordRemarks\", " 
				 + "            to_char(ord_dtls.order_sch_date,'dd/MM/yyyy') as \"orderSchDate\", "
				 + " 		service_mst.service_standard_name as \"serviceStandardName\", " 
				 + " 		service_mst.service_standard_code as \"serviceStandardCode\", " 
				 + " 		speciality_mst.speciality_name as \"specialityName\", " 
				 + " 		cast( " 
				 + " 			null as integer " 
				 + " 		) as \"labSampleDtlsId\", " 
				 + " 		service_dtls.is_panel as \"isPanel\", " 
				 + " 		cast( " 
				 + " 			null as integer " 
				 + " 		) as \"labTestResId\", " 
				 + " 		panel.panel_id as \"panelId\", " 
				 + " 		null as \"statusName\", " 
				 + " 		0 as \"sampleStatusId\", " 
				 + " 		to_char( " 
				 + " 			ord_dtls.created_date, " 
				 + " 			'dd/MM/yyyy' " 
				 + " 		) as \"createdDate\", "
				 + " 			to_char( " 
				 + " 				ord_dtls.created_date, " 
				 + " 				'yyyy/mm/dd' " 
				 + " 			) as \"createdDateNew\", " 
				 + " 		ord_dtls.service_rendered as \"serviceRendered\", " 
				 + " 		ord_dtls.is_package_service as \"isPackageService\", " 
				 + " 		templatetr12_.template_transaction_id as \"templateTransactionId\","
				 + "        templatetr12_.template_object as \"templateObject\", " 
				 + " 		ord_dtls.service_is_billed as \"serviceIsBilled\", " 
				 + " 		ord_dtls.status as \"orderStatus\", " 
				 + " 		am.appointment_id as \"appointmentId\", " 
				 + " 		am.slot_id as \"slotId\", " 
				 + " 0 as \"microTemplateStatusId\" , " 
				 + " (case " 
				 + " when empmst.employee_id is not null then concat(empmst.first_name, ' ', empmst.last_name) "  
				 + " when docmst.doctor_id is not null then concat('Dr. ', docmst.first_name, ' ', docmst.last_name) "  
				 + " else 'Admin' end) as \"userName\","
				 + " 		ord_dtls.package_id as \"packageId\" " 
				 + " 	from " 
				 + " 		public.t_order_master ord_mst " 
				 + " 	inner join public.t_order_details ord_dtls on " 
				 + " 		ord_mst.order_id = ord_dtls.order_id " 
				 + " 	inner join service.t_unit_service_mapper service_mppr on " 
				 + " 		service_mppr.service_id = ord_dtls.service_id " 
				 + " 		and service_mppr.unit_id = ord_dtls.org_unit_id " 
				 + " 	inner join service.t_unit_service_master_details service_dtls on " 
				 + " 		service_dtls.service_master_id = service_mppr.service_id " 
				 + " 		and service_dtls.unit_id = ord_dtls.org_unit_id " 
				 + " 	inner join service.m_service_master service_mst on " 
				 + " 		service_mst.service_master_id = ord_dtls.service_id " 
				 + " 	left join doctor.m_speciality_master speciality_mst on " 
				 + " 		speciality_mst.speciality_id = service_mst.speciality_id "
				 + " 	left join t_unit_speciality_mapper unitSplMpr on "
				 + " 		unitSplMpr.speciality_id = service_mst.speciality_id "
				 + " 		and unitSplMpr.unit_id = service_mppr.unit_id "
				 + " 	inner join lab.t_panel_master panel on " 
				 + " 		panel.service_id = service_mst.service_master_id " 
				 + " 		and panel.org_unit_id = service_mppr.unit_id " 
				 + " 	left outer join public.t_template_transaction templatetr12_ on " 
				 + " 		ord_dtls.order_details_id = templatetr12_.order_details_id " 
				 + " 		and( " 
				 + " 			templatetr12_.status = 'A' " 
				 + " 		) " 
				 + " 	left join opd.m_appointment_master am on " 
				 + " 		am.order_details_id = ord_dtls.order_details_id " 
				 + " 	left join t_unit_application_billing_configuration uabc on " 
				 + " 			uabc.unit_id = ord_dtls.org_unit_id and uabc.status='A'	 " 
				 + " left join public.m_user_master um on ord_mst.created_by = um.user_id "  
				 + " left join public.m_employee_master empmst on um.user_id = empmst.user_id " 
				 + " left join doctor.m_doctor_master docmst on um.user_id = docmst.user_id "
				 + " 		 " 
				 + " 	where " 
				 + " 		service_mst.status = 'A' " 
				 + " 		and ord_dtls.old_ord_dtl_id is null " 
				 + " 		and service_dtls.is_package = 'I' " 
				 + " 		and service_dtls.is_panel = 'Y' "
				 + " 		and unitSplMpr.is_rendering_speciality = 'Y' "
				 + " 			and ord_dtls.nursing_service_order_flag = 'N' "
				 + " and ord_dtls.is_procedure_service = 'N' "
//			     + " 		and (ord_mst.encounter_id is not null "
//				 + " 		or ord_mst.admission_id is not null )"
				 + " 		and (service_mst.speciality_id in( " 
				 + " 					uabc.radiology_speciality_id, " 
				 + " 					uabc.physiotherapy_speciality_id, " 
				 + " 					uabc.hemodialysis_speciality_id " 
				 + " 			) " 
				 + " 		OR service_mst.speciality_id in( " 
				 + " 				select " 
				 + " 					lab.lab_speciality_id " 
				 + " 				from " 
				 + " 					t_lab_configuration lab " 
				 + " 				where " 
				 + " 					lab.unit_id = ord_mst.org_unit_id " 
				 + " 					and lab.status = 'A' " 
				 + " 			)) " 
				 + " 	group by " 
				 + " 		\"orderId\", " 
				 + " 			\"specialityId\", " 
				 + " 		\"orderDetailsId\", " 
				 + " 		\"serviceMasterId\", " 
				 + " 		\"serviceStandardName\", " 
				 + " 		\"serviceStandardCode\", " 
				 + " 		\"specialityName\", " 
				 + " 		\"labSampleDtlsId\", " 
				 + " 		\"isPanel\", " 
				 + " 		\"subDeptId\", " 
				 + " 		\"panelId\", " 
				 + " 		\"createdDate\", " 
				 + " 		\"sampleStatusId\", " 
				 + " 		\"templateTransactionId\", " 
				 + " 		\"appointmentId\", " 
				 + " 		\"slotId\", "
				 + "        \"userName\" "
				 + " 		 " 
				 + " )) select " 
				 + " 	* " 
				 + " from " 
				 + " 	investigations inv "  
				),*/
		
		@NamedNativeQuery(name="GET_INVESTIGATIONS_LIST_FOR_BOOK_VIEW",query= " with investigations as (( " 
				 + " select " 
				 + " 	ord_mst.order_id as \"orderId\", " 
				 + " 	to_char(ord_dtls.order_sch_date, 'dd/MM/yyyy') as \"orderSchDate\", "
				 
				 + "	to_char(ord_mst.order_date,'dd/MM/yyyy') as \"orderDate\","
				 + "    to_char(ord_mst.order_date,'dd/MM/yyyy HH12:MI AM') as \"orderDateTime\","
				 + "    ord_mst.order_no as \"orderNo\","
				 + " concat( to_char( extract( year from age( current_timestamp, patientreg.dob )), '999' ), ' Y' ) as \"ageString\", "
				 + " (case when ord_mst.visit_type_id != 1 then bedMstr.bed_number else '-' end) as \"bedNumber\", "
				 + " 	coalesce((case when app.is_pre_or_post_billing = 'Y' then tbpd.due_amount = 0 when app.is_pre_or_post_billing = 'N' then true "
				 + "    end ), false ) as \"billSettled\","
				 + " (case when  ord_mst.encounter_id > 0 then(tcm.company_desc) else(tcm1.company_desc) end ) as \"companyDesc\","
				 + "  cm.country_code as \"countryCode\","
				 + "  subspe.sub_speciality_name as \"deptName\","
				 + "  to_char(dtl.doa,'dd/mm/yyyy') as \"doa\", "
				 + "  to_char(dtl.pdd,'dd/mm/yyyy') as \"pdd\", "
				 + " (	'Dr. ' || coalesce(doctormstr.first_name)|| ' ' || coalesce(doctormstr.middle_name)|| ' ' || coalesce(doctormstr.last_name) ) as \"doctorName\", "
				 + " (case when patientreg.is_vip = 'Y' then coalesce( patientreg.alise_name , patientreg.first_name ) else concat( coalesce( patientreg.first_name ), ' ', coalesce( patientreg.middle_name ), ' ', coalesce( patientreg.last_name )) "
				 + " end ) as \"patientName\","
				 + " (case when ord_mst.encounter_id > 0 then(tpsd.payment_entitlement_id) else(tpsd2.payment_entitlement_id) end ) as \"paymentEntitlementId\","
				 + "( case when ord_mst.encounter_id > 0 then encountermstr.encounter_number else admission.admission_number end ) as \"visitNo\", "
				 + " pcm.patient_category as \"patientCategory\", "
				 + " patientreg.uhid_number as \"uhidNumber\", " 
				 +"	patientreg.gender_id as \"genderId\", " 
				 +"	patientreg.mobile_number as \"mobileNumber\", " 
				 +"	patientreg.email as \"patientMail\", "  
				 +"	gendermaster.gender_code as \"genderCode\", "  
				 +"	prefixmaster.prefix_code as \"prefixCode\", " 
				 +"	patientreg.identification_number as \"identificationNumber\","
				 + " coalesce(encountermstr.is_critical, 'N' ) as \"isCritical\", "
				 + "patientreg.is_vip as \"isVip\", " 
				 + " (case when microTemp.is_provisional_release is null  then  templatetxn.is_provisional_release  else  microTemp.is_provisional_release end) as \"isProvisionalRelease\","
				// +"	templatetxn.is_provisional_release as \"isProvisionalRelease\", " 
				 + " (case when microTemp.is_final_release is null  then  templatetxn.is_final_release  else  microTemp.is_final_release end) as \"isFinalRelease\","
				// + " templatetxn.is_final_release as \"isFinalRelease\", "
				 + " ( case when ord_mst.encounter_id > 0 then( to_char( encountermstr.encounter_date, 'dd/MM/yyyy HH12:MI AM' )) else( to_char( dtl.doa, 'dd/MM/yyyy HH12:MI AM' )) "
				 +  "end ) as \"registerDate\","
				 + " ( case when reportdoctor.first_name is null then( ( concat( coalesce( mem.first_name ), ' ', coalesce( mem.middle_name ), ' ', coalesce( mem.last_name )) ) " 
				 +"			) else( concat( coalesce( reportdoctor.first_name ), ' ', coalesce( reportdoctor.middle_name ), ' ', coalesce( reportdoctor.last_name )) ) "
				 +"		end ) as \"reportEntryByName\", "
				 + "( case when reportvalidatedoctor.first_name is null then( ( concat( coalesce( memvalidate.first_name ), ' ', coalesce( memvalidate.middle_name ), ' ', coalesce( memvalidate.last_name )) ) )  "
				 + "	else( concat( coalesce( reportvalidatedoctor.first_name ), ' ', coalesce( reportvalidatedoctor.middle_name ), ' ', coalesce( reportvalidatedoctor.last_name )) ) "
				 + " end ) as \"reportvalidateByName\", "
				 + "	( case when ord_mst.visit_type_id = 1 then encountermstr.encounter_number else admission.admission_number end ) as \"visitNumber\","
				 + "billingmstr.bill_number as \"billNo\"," 
				 + "	service_mst.service_standard_code as \"serviceCode\"," 
				 +"	service_mst.service_standard_name as \"serviceName\","  
				 +"	service_mst.service_master_id as \"serviceId\"," 
				 +"	service_mst.speciality_id as \"specialityId\","  
				 +"	service_mst.sub_speciality_id as \"subSpecialityId\"," 
				 +"	ord_dtls.status as \"status\"," 
				 +"	templatetxn.template_object as \"templateContent\","
				 + " sample_mst.sample_no as \"labSampleNo\", "
				 + " (case when microTemp.template_object is null  then  templatetxn.template_object  else  microTemp.template_object end) as \"templateObject\"," 
				// +"	templatetxn.template_object as \"templateObject\"," 
				 +"	templatemstr.template_format_id as \"templateFormatId\"," 
				 +"	templatemstr.template_type_id as \"templateTypeId\", "
				 + " (case when ttm.is_html_print is null  then  templatemstr.is_html_print  else  ttm.is_html_print end) as \"isHtmlPrint\","
				// + " templatemstr.is_html_print as \"isHtmlPrint\", " 
				 +"	vm.visit_type_name as \"visitName\"," 
				 +"	ord_mst.visit_type_id as \"visitTypeId\","
				 + " detailsusm.is_procedure as \"isProcedure\", "
				 + "to_char( microTemp.report_entry_date, 'dd/MM/yyyy HH12:MI AM' ) as \"reportEntryDate\", "  
				 +"	to_char( microTemp.report_validation_date, 'dd/MM/yyyy HH12:MI AM' ) as \"reportValidationDate\", " 
				 +"	to_char( microTemp.report_release_date, 'dd/MM/yyyy HH12:MI AM' ) as \"reportReleaseDate\", " 
				 + " 	service_mst.sub_speciality_id as \"subDeptId\", " 
				 + " 	ord_mst.admission_id as \"admissionId\", " 
				 + " 	ord_mst.encounter_id as \"encounterId\", " 
				 + " 	ord_mst.patient_id as \"patientId\", " 
				 + " 	ord_mst.org_id as \"organizationId\", " 
				 + " 	ord_dtls.order_details_id as \"orderDetailsId\", " 
				 + " 	ord_dtls.service_id as \"serviceMasterId\", " 
				 + " 	ord_dtls.ord_remarks as \"ordRemarks\", " 
				 + " 	service_mst.service_standard_name as \"serviceStandardName\", " 
				 + " 	service_mst.service_standard_code as \"serviceStandardCode\", " 
				 + " 	speciality_mst.speciality_name as \"specialityName\", " 
				 + " 	sample_dtls.lab_sample_dtls_id as \"labSampleDtlsId\", " 
				 + " 	service_dtls.is_panel as \"isPanel\", " 
				 + " 	max(lab_res.lab_test_res_id) as \"labTestResId\", " 
				 + " 	cast(null as integer) as \"panelId\", " 
				 + " 	test_status.status_name as \"statusName\", " 
				 + " 	test_status.test_status_id as \"sampleStatusId\", " 
				 + " 	to_char(ord_dtls.created_date, 'dd/MM/yyyy') as \"createdDate\", " 
				 + " 	to_char(ord_dtls.created_date, 'YYYY-MM-DD') as \"createdDateBookView\", " 
				 + " 	ord_dtls.service_rendered as \"serviceRendered\", " 
				 + " 	templatetr12_.template_transaction_id as \"templateTransactionId\", " 
				 + " 	ord_dtls.service_is_billed as \"serviceIsBilled\", " 
				 + " 	ord_dtls.status as \"orderStatus\", " 
				 + " 	am.appointment_id as \"appointmentId\", " 
				 + " 	am.slot_id as \"slotId\" " 
				 + " from " 
				 + " 	public.t_order_master ord_mst " 
				 + " inner join public.t_order_details ord_dtls on " 
				 + " 	ord_mst.order_id = ord_dtls.order_id " 
				 + " inner join service.t_unit_service_mapper service_mppr on " 
				 + " 	service_mppr.service_id = ord_dtls.service_id " 
				 + " 	and service_mppr.unit_id = ord_dtls.org_unit_id " 
				 + " inner join service.t_unit_service_master_details service_dtls on " 
				 + " 	service_dtls.service_master_id = service_mppr.service_id " 
				 + " 	and service_dtls.unit_id = ord_dtls.org_unit_id " 
				 + " inner join service.m_service_master service_mst on " 
				 + " 	service_mst.service_master_id = ord_dtls.service_id " 
				 + " left join t_unit_speciality_mapper unitSplMpr on " 
				 + " 	unitSplMpr.speciality_id = service_mst.speciality_id " 
				 + " 	and unitSplMpr.unit_id = service_dtls.unit_id " 
				 + " left join doctor.m_speciality_master speciality_mst on " 
				 + " 	speciality_mst.speciality_id = service_mst.speciality_id " 
				 + " left join lab.t_lab_sample_dtls sample_dtls on " 
				 + " 	sample_dtls.order_details_id = ord_dtls.order_details_id " 
				 + " 	and sample_dtls.sample_status_id <> 4 " 
				 + " left join lab.m_test_status_master test_status on " 
				 + " 	test_status.test_status_id = sample_dtls.sample_status_id " 
				 + " left join lab.t_lab_test_result lab_res on " 
				 + " 	lab_res.lab_sample_dtls_id = sample_dtls.lab_sample_dtls_id " 
				 + " 	and lab_res.sample_status_id in (8, " 
				 + " 	9) " 
				 + " left outer join public.t_template_transaction templatetr12_ on " 
				 + " 	ord_dtls.order_details_id = templatetr12_.order_details_id " 
				 + " 	and (templatetr12_.status = 'A') " 
				 + " left join opd.m_appointment_master am on " 
				 + " 	am.order_details_id = ord_dtls.order_details_id "
				 + "inner join t_unit_application_billing_configuration app on "
				 + "	app.unit_id = ord_mst.org_unit_id "
				 + " left join billing.t_billing_details tbd on "
				 + "	ord_dtls.order_details_id = tbd.order_details_id "
				 + "left join billing.m_billing_master mbm on " 
				 +"	mbm.billing_master_id = tbd.billing_master_id "
				 + " left outer join doctor.m_doctor_master doctormstr on "
				 + " ord_mst.ord_doctor_id = doctormstr.doctor_id "
				 + " left outer join doctor.m_speciality_master specialitymstr on "
				 +"	ord_mst.ord_doc_spl_id = specialitymstr.speciality_id "  
				 + " left join doctor.m_sub_speciality_master subspe on "
				 +"	subspe.sub_speciality_id = ord_dtls.service_rendering_deptid "
				 + "left outer join public.t_template_transaction templatetxn on "
				 + "	ord_dtls.order_details_id = templatetxn.order_details_id "
				 +  "	and( templatetxn.status = 'A') "
				 + " left join billing.t_bill_payee_details tbpd on "
				 + "	tbpd.bill_id = mbm.billing_master_id "
				 +"	and tbpd.payee_id = app.self_payee_id "
				 + "inner join patient.t_patient_registration patientreg on "
				 + " ord_mst.patient_id = patientreg.patient_id "
				 + " inner join public.m_visit_type_master vm on "
				 + " vm.visit_type_id = ord_mst.visit_type_id "
				 +"	and vm.status = 'A'"
				 + "inner join public.m_prefix_master prefixmaster on "
				 + " patientreg.prefix_id = prefixmaster.prefix_id "
				 + "inner join public.m_gender_master gendermaster on "
				 + "	patientreg.gender_id = gendermaster.gender_id "
				 + "	inner join patient.t_patient_details patdtl on "
				 +  "	patdtl.patient_id = patientreg.patient_id "
				 +  "	left join public.m_patient_category_master pcm on "
				 + "	pcm.patient_category_id = patientreg.patient_category_id "
				 + "	and pcm.status = 'A'"
				 + " left join public.m_country_master cm on "
				 + "	cm.country_id = patdtl.country_id "
				 + " left outer join opd.t_encounter_master encountermstr on "
				 + " ord_mst.encounter_id = encountermstr.encounter_id "
				 + " left outer join adt.t_admission admission on "
				 +  "	ord_mst.admission_id = admission.admission_id "
				 + " left join adt.t_admission_details dtl on "
				 + "	dtl.admission_id = admission.admission_id "
				 + "	and admission.status = 'A' " 
				 +  "	and dtl.status = 'A'"
				 + "left join opd.t_patient_sponsor_details tpsd on "
				 + "	encountermstr.encounter_id = tpsd.encounter_id "
				 + "	and tpsd.priority_id = 1 "
				 + "	and tpsd.status = 'A'"
				 + "left join opd.t_patient_sponsor_details tpsd2 on "
				 + "	tpsd2.admission_id = admission.admission_id "
				 + "	and tpsd2.priority_id = 1 "
				 + "	and tpsd2.status = 'A'"
				 + " left join adt.m_bed_master bedMstr on "
				 + " dtl.bed_id = bedMstr.bed_id "
				 + " left join t_referal_details dtls1 on "
				 +"	dtls1.encounter_id = ord_mst.encounter_id "
				 +"	and  ord_mst.admission_id is null "
				 + " left join t_referal_details dtls2 on "
				 +  "	dtls2.admission_id = ord_mst.admission_id "
				 + "	and ord_mst.encounter_id is null "
				 + " left join public.t_company_master tcm on "
				 + "	tcm.company_id = tpsd.payee_id "
				 + " left join public.t_company_master tcm1 on "
				 + "	tcm1.company_id = tpsd2.payee_id "
				 + " left join adt.m_referral_master refM on "
				 + " dtls1.referral_id = refM.referral_id "
				 + " left outer join doctor.m_doctor_master reportdoctor on "
				 + "	reportdoctor.user_id = templatetxn.report_entry_by "
				 +  "	left join billing.t_billing_details billingdtls on "
				 + "	ord_dtls.order_details_id = billingdtls.order_details_id "
				 + "	and billingdtls.status = 'A'"
				 + " left join billing.m_billing_master billingmstr on " 
				 + "	billingdtls.billing_master_id = billingmstr.billing_master_id "
				 + "	and billingmstr.bill_cancelled = 'N' "
				 + " left outer join m_employee_master mem on "
				 + "	mem.user_id = templatetxn.report_entry_by " 
				 + " left outer join doctor.m_doctor_master reportvalidatedoctor on "
				 + "	reportvalidatedoctor.user_id = templatetxn.report_validate_by "
				 + "left outer join m_employee_master memvalidate on " 
				 + "	memvalidate.user_id = templatetxn.report_validate_by "
				 + "	left outer join public.t_template_master templatemstr on "
				 + "	templatetxn.template_id = templatemstr.template_id "
//				 + "	and( templatemstr.template_type_id = 13 )
				 +" left join	lab.t_lab_sample_dtls lbsampledtls on " 
				 +" ord_dtls.order_details_id  = lbsampledtls.order_details_id  "  
				 +" left join lab.t_microbiology_template microTemp on " 
				 +"	lbsampledtls.lab_sample_dtls_id = microTemp.lab_sample_dtls_id " 
				 +" left join public.t_template_master ttm on " 
				 +"	ttm.template_id = microTemp.template_id  " 
				 +" left join lab.t_lab_sample_master sample_mst on " 
				 +"	lbsampledtls.lab_sample_id = sample_mst.lab_sample_id "
				 + " left join service.t_unit_service_master_details detailsusm on  "  
				 + "  detailsusm.service_master_id = service_mst.service_master_id "
				 + " where " 
				 + " 	service_mst.status = 'A' " 
				 + " 	and ord_dtls.old_ord_dtl_id is null "
				 + " 	and ord_dtls.org_unit_id  = detailsusm.unit_id  " 
				 + " 	and service_dtls.is_package = 'I' " 
				 + " 	and ord_dtls.nursing_service_order_flag = 'N' " 
				 + " 	and service_dtls.is_panel = 'N' " 
				 + " and unitSplMpr.is_rendering_speciality in (select is_rendering_speciality from t_unit_speciality_mapper where is_rendering_speciality = 'Y' ) "
	 			 + " 	and ord_mst.patient_id =:patientId " 
				 + " 	and ord_mst.org_id =:organizationId " 
				 + " 	and to_char(ord_mst.created_date , 'YYYY-MM-DD') between :fromDate and :toDate " 
				 + " group by " 
				 + " 	\"orderId\", " 
				 + " 	\"orderDetailsId\", " 
				 + " 	\"serviceMasterId\", " 
				 + " 	\"serviceStandardName\", " 
				 + " 	\"serviceStandardCode\", " 
				 + " 	\"specialityName\", " 
				 + " 	\"labSampleDtlsId\", " 
				 + " 	\"isPanel\", " 
				 + " 	\"subDeptId\", " 
				 + " 	\"panelId\", " 
				 + " 	\"statusName\", " 
				 + " 	\"createdDate\", " 
				 + " 	\"sampleStatusId\", " 
				 + " 	\"templateTransactionId\", " 
				 + " 	\"appointmentId\", " 
				 + " 	\"slotId\", " 
				 + " 	\"patientId\", " 
				 + " 	\"organizationId\", " 
				 + " 	\"createdDateBookView\", " 
				 + " \"ageString\","
				 + " \"bedNumber\","
				 + " \"billSettled\","
				 + " \"companyDesc\","
				 + " \"countryCode\","
				 + " \"deptName\","
				 + " \"doa\","
				 + " \"pdd\","
				 + " \"doctorName\","
				 + " \"patientName\","
				 + " \"paymentEntitlementId\","
				 + " \"patientCategory\","
				 + " \"uhidNumber\","
				 + " \"genderId\","
				 + " \"mobileNumber\","
				 + " \"patientMail\","
				 + " \"genderCode\","
				 + " \"prefixCode\","
				 + " \"identificationNumber\","
				 + " \"isCritical\","
				 + " \"isVip\","
				 + " \"isProvisionalRelease\","
				 + " \"isFinalRelease\","
				 + " \"registerDate\","
				 + " \"reportEntryByName\","
				 + " \"reportvalidateByName\","
				 + " \"visitNumber\","
				 + " \"billNo\","
				 + " \"serviceId\","
				 + " \"templateContent\","
				 + " \"templateFormatId\","
				 + " \"templateTypeId\","
				 + " \"isHtmlPrint\","
				 + " \"visitName\","
				 + " \"templateObject\","
				 + " \"labSampleNo\", "
				 + " \"visitNo\", " 
				 + " \"reportEntryDate\", " 
				 + " \"reportValidationDate\", " 
				 + " \"reportReleaseDate\", " 
				 + " \"isProcedure\") " 
				 + " union ( " 
				 + " select " 
				 + " 	ord_mst.order_id as \"orderId\", " 
				 + " 	to_char(ord_dtls.order_sch_date, 'dd/MM/yyyy') as \"orderSchDate\", " 
				 
				 + "	to_char(ord_mst.order_date,'dd/MM/yyyy') as \"orderDate\","
				 + "    to_char(ord_mst.order_date,'dd/MM/yyyy HH12:MI AM') as \"orderDateTime\","
				 + "    ord_mst.order_no as \"orderNo\","
				 + "    concat( to_char( extract( year from age( current_timestamp, patientreg.dob )), '999' ), ' Y' ) as \"ageString\", "
				 + "    (case when ord_mst.visit_type_id != 1 then bedMstr.bed_number else '-' end) as \"bedNumber\", "
				 + " 	coalesce((case when app.is_pre_or_post_billing = 'Y' then tbpd.due_amount = 0 when app.is_pre_or_post_billing = 'N' then true "
				 + "    end ), false ) as \"billSettled\","
				 + "   (case when  ord_mst.encounter_id > 0 then(tcm.company_desc) else(tcm1.company_desc) end ) as \"companyDesc\","
				 + "    cm.country_code as \"countryCode\","
				 + "  subspe.sub_speciality_name as \"deptName\","
				 + "  to_char(dtl.doa,'dd/mm/yyyy') as \"doa\", "
				 + "  to_char(dtl.pdd,'dd/mm/yyyy') as \"pdd\", "
				 + "	('Dr. ' || coalesce(doctormstr.first_name)|| ' ' || coalesce(doctormstr.middle_name)|| ' ' || coalesce(doctormstr.last_name) ) as \"doctorName\", "
				 + "( case when patientreg.is_vip = 'Y' then coalesce( patientreg.alise_name , patientreg.first_name ) else concat( coalesce( patientreg.first_name ), ' ', coalesce( patientreg.middle_name ), ' ', coalesce( patientreg.last_name )) "
				 + " end ) as \"patientName\","
				 + " (case when ord_mst.encounter_id > 0 then(tpsd.payment_entitlement_id) else(tpsd2.payment_entitlement_id) end ) as \"paymentEntitlementId\", "
				 + "( case when ord_mst.encounter_id > 0 then encountermstr.encounter_number else admission.admission_number end ) as \"visitNo\", "
				 + " pcm.patient_category as \"patientCategory\", "
				 + " patientreg.uhid_number as \"uhidNumber\", " 
				 +"	patientreg.gender_id as \"genderId\", " 
				 +"	patientreg.mobile_number as \"mobileNumber\", " 
				 +"	patientreg.email as \"patientMail\", "  
				 +"	gendermaster.gender_code as \"genderCode\", "  
				 +"	prefixmaster.prefix_code as \"prefixCode\", " 
				 +"	patientreg.identification_number as \"identificationNumber\", " 
				 + " coalesce(encountermstr.is_critical, 'N' ) as \"isCritical\", "
				 + "patientreg.is_vip as \"isVip\", "  
				 + " (case when microTemp.is_provisional_release is null  then  templatetxn.is_provisional_release  else  microTemp.is_provisional_release end) as \"isProvisionalRelease\","
			  // +"	templatetxn.is_provisional_release as \"isProvisionalRelease\", " 
				 + " (case when microTemp.is_final_release is null  then  templatetxn.is_final_release  else  microTemp.is_final_release end) as \"isFinalRelease\","
		    	// + " templatetxn.is_final_release as \"isFinalRelease\", "
				 + " ( case when ord_mst.encounter_id > 0 then( to_char( encountermstr.encounter_date, 'dd/MM/yyyy HH12:MI AM' )) else( to_char( dtl.doa, 'dd/MM/yyyy HH12:MI AM' )) "
				 +  "end ) as \"registerDate\","
				 + " ( case when reportdoctor.first_name is null then( ( concat( coalesce( mem.first_name ), ' ', coalesce( mem.middle_name ), ' ', coalesce( mem.last_name )) ) " 
				 +"			) else( concat( coalesce( reportdoctor.first_name ), ' ', coalesce( reportdoctor.middle_name ), ' ', coalesce( reportdoctor.last_name )) ) "
				 +"		end ) as \"reportEntryByName\", "
				 + "( case when reportvalidatedoctor.first_name is null then( ( concat( coalesce( memvalidate.first_name ), ' ', coalesce( memvalidate.middle_name ), ' ', coalesce( memvalidate.last_name )) ) )  "
				 + "	else( concat( coalesce( reportvalidatedoctor.first_name ), ' ', coalesce( reportvalidatedoctor.middle_name ), ' ', coalesce( reportvalidatedoctor.last_name )) ) "
				 + " end ) as \"reportvalidateByName\", "
				 + "	( case when ord_mst.visit_type_id = 1 then encountermstr.encounter_number else admission.admission_number end ) as \"visitNumber\","
				 + "billingmstr.bill_number as \"billNo\"," 
				 + "	service_mst.service_standard_code as \"serviceCode\"," 
				 +"	service_mst.service_standard_name as \"serviceName\","  
				 +"	service_mst.service_master_id as \"serviceId\"," 
				 +"	service_mst.speciality_id as \"specialityId\","  
				 +"	service_mst.sub_speciality_id as \"subSpecialityId\"," 
				 +"	ord_dtls.status as \"status\"," 
				 +"	templatetxn.template_object as \"templateContent\"," 
				 + " sample_mst.sample_no as \"labSampleNo\", "
				 + " (case when microTemp.template_object is null  then  templatetxn.template_object  else  microTemp.template_object end) as \"templateObject\"," 
				// +"	templatetxn.template_object as \"templateObject\"," 
				 +"	templatemstr.template_format_id as \"templateFormatId\"," 
				 +"	templatemstr.template_type_id as \"templateTypeId\", "
				 + " (case when ttm.is_html_print is null  then  templatemstr.is_html_print  else  ttm.is_html_print end) as \"isHtmlPrint\","
				// + " templatemstr.is_html_print as \"isHtmlPrint\", "
				 +"	vm.visit_type_name as \"visitName\"," 
				 +"	ord_mst.visit_type_id as \"visitTypeId\","
				 + " detailsusm.is_procedure as \"isProcedure\"," 
				 + "to_char( microTemp.report_entry_date, 'dd/MM/yyyy HH12:MI AM' ) as \"reportEntryDate\", "  
				 +"	to_char( microTemp.report_validation_date, 'dd/MM/yyyy HH12:MI AM' ) as \"reportValidationDate\", " 
				 +"	to_char( microTemp.report_release_date, 'dd/MM/yyyy HH12:MI AM' ) as \"reportReleaseDate\", " 
				 + " 	service_mst.sub_speciality_id as \"subDeptId\", " 
				 + " 	ord_mst.admission_id as \"admissionId\", " 
				 + " 	ord_mst.encounter_id as \"encounterId\", " 
				 + " 	ord_mst.patient_id as \"patientId\", " 
				 + " 	ord_mst.org_id as \"organizationId\", " 
				 + " 	ord_dtls.order_details_id as \"orderDetailsId\", " 
				 + " 	ord_dtls.service_id as \"serviceMasterId\", " 
				 + " 	ord_dtls.ord_remarks as \"ordRemarks\", " 
				 + " 	service_mst.service_standard_name as \"serviceStandardName\", " 
				 + " 	service_mst.service_standard_code as \"serviceStandardCode\", " 
				 + " 	speciality_mst.speciality_name as \"specialityName\", " 
				 + " 	cast(null as integer) as \"labSampleDtlsId\", " 
				 + " 	service_dtls.is_panel as \"isPanel\", " 
				 + " 	cast(null as integer) as \"labTestResId\", " 
				 + " 	panel.panel_id as \"panelId\", " 
				 + " 	null as \"statusName\" , " 
				 + " 	0 as \"sampleStatusId\", " 
				 + " 	to_char(ord_dtls.created_date, 'dd/MM/yyyy') as \"createdDate\", " 
				 + " 	to_char(ord_dtls.created_date, 'YYYY-MM-DD') as \"createdDateBookView\", " 
				 + " 	ord_dtls.service_rendered as \"serviceRendered\" , " 
				 + " 	templatetr12_.template_transaction_id as \"templateTransactionId\", " 
				 + " 	ord_dtls.service_is_billed as \"serviceIsBilled\", " 
				 + " 	ord_dtls.status as \"orderStatus\", " 
				 + " 	am.appointment_id as \"appointmentId\", " 
				 + " 	am.slot_id as \"slotId\" " 
				 + " from " 
				 + " 	public.t_order_master ord_mst " 
				 + " inner join public.t_order_details ord_dtls on " 
				 + " 	ord_mst.order_id = ord_dtls.order_id " 
				 + " inner join service.t_unit_service_mapper service_mppr on " 
				 + " 	service_mppr.service_id = ord_dtls.service_id " 
				 + " 	and service_mppr.unit_id = ord_dtls.org_unit_id " 
				 + " inner join service.t_unit_service_master_details service_dtls on " 
				 + " 	service_dtls.service_master_id = service_mppr.service_id " 
				 + " 	and service_dtls.unit_id = ord_dtls.org_unit_id " 
				 + " inner join service.m_service_master service_mst on " 
				 + " 	service_mst.service_master_id = ord_dtls.service_id " 
				 + " left join doctor.m_speciality_master speciality_mst on " 
				 + " 	speciality_mst.speciality_id = service_mst.speciality_id " 
				 + " left join t_unit_speciality_mapper unitSplMpr on " 
				 + " 	unitSplMpr.speciality_id = service_mst.speciality_id " 
				 + " 	and unitSplMpr.unit_id = service_dtls.unit_id " 
				 + " inner join lab.t_panel_master panel on " 
				 + " 	panel.service_id = service_mst.service_master_id " 
				 + " 	and panel.org_unit_id = service_mppr.unit_id " 
				 + " left outer join public.t_template_transaction templatetr12_ on " 
				 + " 	ord_dtls.order_details_id = templatetr12_.order_details_id " 
				 + " 	and (templatetr12_.status = 'A') " 
				 + " left join opd.m_appointment_master am on " 
				 + " 	am.order_details_id = ord_dtls.order_details_id " 
				 + "inner join t_unit_application_billing_configuration app on "
				 + "	app.unit_id = ord_mst.org_unit_id "
				 + " left join billing.t_billing_details tbd on "
				 + "	ord_dtls.order_details_id = tbd.order_details_id "
				 + "left join billing.m_billing_master mbm on " 
				 +"	mbm.billing_master_id = tbd.billing_master_id "
				 + " left outer join doctor.m_doctor_master doctormstr on "
				 + " ord_mst.ord_doctor_id = doctormstr.doctor_id "
				 + " left outer join doctor.m_speciality_master specialitymstr on "
				 +"	ord_mst.ord_doc_spl_id = specialitymstr.speciality_id "  
				 + " left join doctor.m_sub_speciality_master subspe on "
				 +"	subspe.sub_speciality_id = ord_dtls.service_rendering_deptid "
				 + "left outer join public.t_template_transaction templatetxn on "
				 + "	ord_dtls.order_details_id = templatetxn.order_details_id "
				 +  "	and( templatetxn.status = 'A') "
				 + " left join billing.t_bill_payee_details tbpd on "
				 + "	tbpd.bill_id = mbm.billing_master_id "
				 +"	and tbpd.payee_id = app.self_payee_id "
				 + "inner join patient.t_patient_registration patientreg on "
				 + " ord_mst.patient_id = patientreg.patient_id "
				 + " inner join public.m_visit_type_master vm on "
				 + " vm.visit_type_id = ord_mst.visit_type_id "
				 +"	and vm.status = 'A'"
				 + "inner join public.m_prefix_master prefixmaster on "
				 + " patientreg.prefix_id = prefixmaster.prefix_id "
				 + "inner join public.m_gender_master gendermaster on "
				 + "	patientreg.gender_id = gendermaster.gender_id "
				 + "	inner join patient.t_patient_details patdtl on "
				 +  "	patdtl.patient_id = patientreg.patient_id "
				 +  "	left join public.m_patient_category_master pcm on "
				 + "	pcm.patient_category_id = patientreg.patient_category_id "
				 + "	and pcm.status = 'A'"
				 + " left join public.m_country_master cm on "
				 + "	cm.country_id = patdtl.country_id "
				 + " left outer join opd.t_encounter_master encountermstr on "
				 + " ord_mst.encounter_id = encountermstr.encounter_id "
				 + " left outer join adt.t_admission admission on "
				 +  "	ord_mst.admission_id = admission.admission_id "
				 + " left join adt.t_admission_details dtl on "
				 + "	dtl.admission_id = admission.admission_id "
				 + "	and admission.status = 'A' " 
				 +  "	and dtl.status = 'A'"
				 + "left join opd.t_patient_sponsor_details tpsd on "
				 + "	encountermstr.encounter_id = tpsd.encounter_id "
				 + "	and tpsd.priority_id = 1 "
				 + "	and tpsd.status = 'A'"
				 + "left join opd.t_patient_sponsor_details tpsd2 on "
				 + "	tpsd2.admission_id = admission.admission_id "
				 + "	and tpsd2.priority_id = 1 "
				 + "	and tpsd2.status = 'A'"
				 + " left join adt.m_bed_master bedMstr on "
				 + " dtl.bed_id = bedMstr.bed_id "
				 + " left join t_referal_details dtls1 on "
				 +"	dtls1.encounter_id = ord_mst.encounter_id "
				 +"	and  ord_mst.admission_id is null "
				 + " left join t_referal_details dtls2 on "
				 +  "	dtls2.admission_id = ord_mst.admission_id "
				 + "	and ord_mst.encounter_id is null "
				 + " left join public.t_company_master tcm on "
				 + "	tcm.company_id = tpsd.payee_id "
				 + " left join public.t_company_master tcm1 on "
				 + "	tcm1.company_id = tpsd2.payee_id "
				 + " left join adt.m_referral_master refM on "
				 + " dtls1.referral_id = refM.referral_id "
				 + " left outer join doctor.m_doctor_master reportdoctor on "
				 + "	reportdoctor.user_id = templatetxn.report_entry_by "
				 +  "	left join billing.t_billing_details billingdtls on "
				 + "	ord_dtls.order_details_id = billingdtls.order_details_id "
				 + "	and billingdtls.status = 'A'"
				 + " left join billing.m_billing_master billingmstr on " 
				 + "	billingdtls.billing_master_id = billingmstr.billing_master_id "
				 + "	and billingmstr.bill_cancelled = 'N' "
				 + " left outer join m_employee_master mem on "
				 + "	mem.user_id = templatetxn.report_entry_by " 
				 + " left outer join doctor.m_doctor_master reportvalidatedoctor on "
				 + "	reportvalidatedoctor.user_id = templatetxn.report_validate_by "
				 + "left outer join m_employee_master memvalidate on " 
				 + "	memvalidate.user_id = templatetxn.report_validate_by "
				 + "	left outer join public.t_template_master templatemstr on "
				 + "	templatetxn.template_id = templatemstr.template_id "
//				 + "	and( templatemstr.template_type_id = 13 )"
				+" left join	lab.t_lab_sample_dtls lbsampledtls on " 
				+" ord_dtls.order_details_id  = lbsampledtls.order_details_id  "  
				+" left join lab.t_microbiology_template microTemp on " 
				+"	lbsampledtls.lab_sample_dtls_id = microTemp.lab_sample_dtls_id " 
				+" left join public.t_template_master ttm on " 
				+"	ttm.template_id = microTemp.template_id  " 
				+" left join lab.t_lab_sample_master sample_mst on "
				+ " lbsampledtls.lab_sample_id = sample_mst.lab_sample_id " 
				+ " left join service.t_unit_service_master_details detailsusm on  "  
				 + "  detailsusm.service_master_id = service_mst.service_master_id "
				 + " where " 
				 + " 	service_mst.status = 'A' " 
				 + " 	and ord_dtls.old_ord_dtl_id is null " 
				 + " 	and ord_dtls.org_unit_id  = detailsusm.unit_id  " 
				 + " 	and service_dtls.is_package = 'I' " 
				 + " 	and ord_dtls.nursing_service_order_flag = 'N' " 
				 + " 	and service_dtls.is_panel = 'N' " 
				 + " and unitSplMpr.is_rendering_speciality in (select is_rendering_speciality from t_unit_speciality_mapper where is_rendering_speciality = 'Y' ) "
	 			 + " 	and ord_mst.patient_id =:patientId " 
				 + " 	and ord_mst.org_id =:organizationId " 
				 + " 	and to_char(ord_mst.created_date , 'YYYY-MM-DD') between :fromDate and :toDate " 
				 + " group by " 
				 + " \"orderId\", " 
				 + " \"orderDetailsId\", " 
				 + " \"serviceMasterId\", " 
				 + " \"serviceStandardName\", " 
				 + " \"serviceStandardCode\", " 
				 + " \"specialityName\", " 
				 + " \"labSampleDtlsId\", " 
				 + " \"isPanel\", " 
				 + " \"subDeptId\", " 
				 + " \"panelId\", " 
				 + " \"createdDate\", " 
				 + " \"sampleStatusId\", " 
				 + " \"templateTransactionId\", " 
				 + " \"appointmentId\", " 
				 + " \"slotId\", " 
				 + " \"patientId\", " 
				 + " \"organizationId\", " 
				 + " \"createdDateBookView\","
				 + " \"ageString\","
				 + " \"bedNumber\","
				 + " \"billSettled\","
				 + " \"companyDesc\","
				 + " \"countryCode\","
				 + " \"deptName\","
				 + " \"doa\","
				 + " \"pdd\","
				 + " \"doctorName\","
				 + " \"patientName\","
				 + " \"paymentEntitlementId\","
				 + " \"patientCategory\","
				 + " \"uhidNumber\","
				 + " \"genderId\","
				 + " \"mobileNumber\","
				 + " \"patientMail\","
				 + " \"genderCode\","
				 + " \"prefixCode\","
				 + " \"identificationNumber\","
				 + " \"isCritical\","
				 + " \"isVip\","
				 + " \"isProvisionalRelease\","
				 + " \"isFinalRelease\","
				 + " \"registerDate\","
				 + " \"reportEntryByName\","
				 + " \"reportvalidateByName\","
				 + " \"visitNumber\","
				 + " \"billNo\","
				 + " \"serviceId\","
				 + " \"templateContent\","
				 + " \"templateFormatId\","
				 + " \"templateTypeId\","
				 + " \"isHtmlPrint\","
				 + " \"visitName\","
				 + " \"templateObject\","
				 + " \"labSampleNo\", " 
				 + " \"visitNo\", " 
				 + " \"reportEntryDate\", " 
				 + " \"reportValidationDate\", " 
				 + " \"reportReleaseDate\", " 
				 + " \"isProcedure\") " 
				 + " )select " 
				 + " 	* " 
				 + " from " 
				 + " 	investigations inv " 
				),
		
		@NamedNativeQuery(name="GET_CURRENT_INVESTIGATIONS_BY_PATIENT_ID",query="with investigations as  ((select "
				+ "	ord_mst.order_id as \"orderId\", "
				+ "	service_mst.speciality_id as \"specialityId\", "
				+ "	service_mst.sub_speciality_id as \"subDeptId\", "
				+ "	service_mst.sub_speciality_id as \"subSpecialityId\", "
				+ "	ord_mst.admission_id as \"admissionId\", "
				+ "	ord_mst.encounter_id as \"encounterId\", "
				+ "   ord_mst.patient_id as \"patientId\", "
				+ "	ord_dtls.order_details_id as \"orderDetailsId\", "
				+ "	ord_dtls.service_id as \"serviceMasterId\", "
				+ "	service_mst.service_standard_name as \"serviceStandardName\", "
				+ "	service_mst.service_standard_code as \"serviceStandardCode\", "
				+ "	speciality_mst.speciality_name as \"specialityName\", "
				+ "	sample_dtls.lab_sample_dtls_id as \"labSampleDtlsId\", "
				+ "	service_dtls.is_panel as \"isPanel\", "
				+ "	max(lab_res.lab_test_res_id) as \"labTestResId\", "
				+ "	cast(null as integer) as \"panelId\", "
				+ "	test_status.status_name as \"statusName\", "
				+ "	test_status.test_status_id as \"sampleStatusId\", "
				+ "	to_char(ord_dtls.created_date,'dd/MM/yyyy') as \"createdDate\", "
				+ "	ord_dtls.service_rendered as \"serviceRendered\", "
				+ " 	templatetr12_.template_transaction_id as \"templateTransactionId\" "
				+ " from "
				+ "	public.t_order_master ord_mst "
				+ "inner join public.t_order_details ord_dtls on "
				+ "	ord_mst.order_id = ord_dtls.order_id "
				+ "inner join service.t_unit_service_mapper service_mppr on "
				+ "	service_mppr.service_id = ord_dtls.service_id "
				+ "	and service_mppr.unit_id = ord_dtls.org_unit_id "
				+ "inner join service.t_unit_service_master_details service_dtls on "
				+ "	service_dtls.service_master_id = service_mppr.service_id "
				+ "	and service_dtls.unit_id = ord_dtls.org_unit_id "
				+ "inner join service.m_service_master service_mst on "
				+ "	service_mst.service_master_id = ord_dtls.service_id "
				+ "left join doctor.m_speciality_master speciality_mst on "
				+ "	speciality_mst.speciality_id = service_mst.speciality_id "
				+ "left join lab.t_lab_sample_dtls sample_dtls on "
				+ "	sample_dtls.order_details_id = ord_dtls.order_details_id "
				+ "	and sample_dtls.sample_status_id <> 4 "
				+ "left join lab.m_test_status_master test_status on "
				+ "	test_status.test_status_id = sample_dtls.sample_status_id "
				+ "left join lab.t_lab_test_result lab_res on "
				+ "	lab_res.lab_sample_dtls_id = sample_dtls.lab_sample_dtls_id "
				+ "	and lab_res.sample_status_id in (8,9) "
				+ " left outer join public.t_template_transaction templatetr12_ on "
				+ " 	ord_dtls.order_details_id = templatetr12_.order_details_id "
				+ " 	and (templatetr12_.status = 'A') "
				+ "where "
				+ "	 service_mst.status = 'A' "
				+ "	and ord_dtls.status = 'A' "
				+ "	and service_dtls.is_package = 'I' "
				+ "	and ord_dtls.is_investigation_order = 'Y' "
				+ "	and service_dtls.is_panel = 'N' "
				+ "group by \"orderId\",\"orderDetailsId\",\"serviceMasterId\",\"serviceStandardName\",\"serviceStandardCode\",\"specialityName\", "
				+ "	\"labSampleDtlsId\",\"isPanel\",\"specialityId\",\"subDeptId\",\"panelId\",\"statusName\",\"createdDate\",\"sampleStatusId\",\"templateTransactionId\") "
				+ " union "
				+ "(select "
				+ "	ord_mst.order_id as \"orderId\", "
				+ "	service_mst.speciality_id as \"specialityId\", "
				+ "	service_mst.sub_speciality_id as \"subDeptId\", "
				+ "	service_mst.sub_speciality_id as \"subSpecialityId\", "
				+ "	ord_mst.admission_id as \"admissionId\", "
				+ "	ord_mst.encounter_id as \"encounterId\", "
				+ "   ord_mst.patient_id as \"patientId\", "
				+ "	ord_dtls.order_details_id as \"orderDetailsId\", "
				+ "	ord_dtls.service_id as \"serviceMasterId\", "
				+ "	service_mst.service_standard_name as \"serviceStandardName\", "
				+ "	service_mst.service_standard_code as \"serviceStandardCode\", "
				+ "	speciality_mst.speciality_name as \"specialityName\", "
				+ "	cast(null as integer) as \"labSampleDtlsId\", "
				+ "	service_dtls.is_panel as \"isPanel\", "
				+ "	cast(null as integer) as \"labTestResId\", "
				+ "	panel.panel_id as \"panelId\", "
				+ "	null as \"statusName\" , "
				+ "	0 as \"sampleStatusId\", "
				+ "	to_char(ord_dtls.created_date,'dd/MM/yyyy') as \"createdDate\", "
				+ "	ord_dtls.service_rendered as \"serviceRendered\"  , "
				+ " 	templatetr12_.template_transaction_id as \"templateTransactionId\" "
				+ "from "
				+ "	public.t_order_master ord_mst "
				+ "inner join public.t_order_details ord_dtls on "
				+ "	ord_mst.order_id = ord_dtls.order_id "
				+ "inner join service.t_unit_service_mapper service_mppr on "
				+ "	service_mppr.service_id = ord_dtls.service_id "
				+ "	and service_mppr.unit_id = ord_dtls.org_unit_id "
				+ "inner join service.t_unit_service_master_details service_dtls on "
				+ "	service_dtls.service_master_id = service_mppr.service_id "
				+ "	and service_dtls.unit_id = ord_dtls.org_unit_id "
				+ "inner join service.m_service_master service_mst on "
				+ "	service_mst.service_master_id = ord_dtls.service_id "
				+ "left join doctor.m_speciality_master speciality_mst on "
				+ "	speciality_mst.speciality_id = service_mst.speciality_id "
				+ "inner join lab.t_panel_master panel "
				+ "on panel.service_id = service_mst.service_master_id "
				+ "and panel.org_unit_id = service_mppr.unit_id "
				+ " left outer join public.t_template_transaction templatetr12_ on "
				+ " 	ord_dtls.order_details_id = templatetr12_.order_details_id "
				+ " 	and (templatetr12_.status = 'A') "
				+ "where "
				+ "	 service_mst.status = 'A' "
				+ "	and ord_dtls.status = 'A' "
				+ "	and service_dtls.is_package = 'I' "
				+ "	and ord_dtls.is_investigation_order = 'Y' "
				+ "	and service_dtls.is_panel = 'Y' "
				+ "group by \"orderId\",\"orderDetailsId\",\"serviceMasterId\",\"serviceStandardName\",\"serviceStandardCode\",\"specialityName\", "
				+ "	\"labSampleDtlsId\",\"isPanel\",\"specialityId\",\"subDeptId\",\"panelId\",\"createdDate\",\"sampleStatusId\",\"templateTransactionId\") "
				+ ")select * from investigations inv"
				),
		@NamedNativeQuery(name="GET_CUEERENT_ORDERED_INVESTIGATION_DETAILS",query="select "
				+ "	ord_mst.order_id as \"orderId\", "
				+ "	service_mst.sub_speciality_id as \"subDeptId\", "
				+ "	ord_mst.admission_id as \"admissionId\", "
				+ "	ord_mst.encounter_id as \"encounterId\", "
				+ "	ord_dtls.order_details_id as \"orderDetailsId\", "
				+ "	service_mst.service_master_id as \"serviceMasterId\", "
				+ "	service_mst.service_standard_name as \"serviceStandardName\", "
				+ "	service_mst.service_standard_code as \"serviceStandardCode\", "
				+ "	speciality_mst.speciality_name as \"specialityName\", "
				+ "	sample_dtls.lab_sample_dtls_id as \"labSampleDtlsId\", "
				+ "	service_dtls.is_panel as \"isPanel\", "
				+ "	max(lab_res.lab_test_res_id) as \"labTestResId\", "
				+ "	panel.panel_id as \"panelId\", "
				+ "	test_status.status_name as \"statusName\", "
				+ "	test_status.test_status_id as \"sampleStatusId\", "
				+ "	to_char(ord_dtls.created_date,'dd/MM/yyyy') as \"createdDate\" "
				+ "from "
				+ "	public.t_order_master ord_mst "
				+ "inner join public.t_order_details ord_dtls on "
				+ "	ord_mst.order_id = ord_dtls.order_id "
				+ "inner join lab.t_panel_master panel on "
				+ "	panel.service_id = ord_dtls.service_id "
				+ "inner join lab.t_panel_details panel_dtls on "
				+ "	panel.panel_id = panel_dtls.panel_id "
				+ "	and panel_dtls.is_deleted = 'N' "
				+ "	and panel_dtls.status = 'A' "
				+ "inner join lab.m_test_master test_mst on "
				+ "	test_mst.test_id = panel_dtls.test_id "
				+ "inner join service.m_service_master service_mst on "
				+ "	service_mst.service_master_id = test_mst.service_id "
				+ "inner join service.t_unit_service_mapper service_mppr on "
				+ "	service_mppr.service_id = ord_dtls.service_id "
				+ "	and service_mppr.unit_id = ord_dtls.org_unit_id "
				+ "inner join service.t_unit_service_master_details service_dtls on "
				+ "	service_dtls.service_master_id = service_mppr.service_id "
				+ "	and service_dtls.unit_id = ord_dtls.org_unit_id "
				+ "left join doctor.m_speciality_master speciality_mst on "
				+ "	speciality_mst.speciality_id = service_mst.speciality_id "
				+ "left join lab.t_lab_sample_dtls sample_dtls on "
				+ "	sample_dtls.test_id = test_mst.test_id "
				+ "	and sample_dtls.sample_status_id <> 4 "
				+ "	and panel.panel_id = sample_dtls.profile_id "
				+ "	and ord_dtls.order_details_id = sample_dtls.order_details_id "
				+ "left join lab.m_test_status_master test_status on "
				+ "	test_status.test_status_id = sample_dtls.sample_status_id "
				+ "left join lab.t_lab_test_result lab_res on "
				+ "	lab_res.lab_sample_dtls_id = sample_dtls.lab_sample_dtls_id "
				+ "	and lab_res.sample_status_id in (8, "
				+ "	9) "
				+ "where "
				+ "	service_mst.status = 'A' "
				+ "	and ord_dtls.status = 'A' "
			//	+ "	and ord_dtls.is_investigation_order = 'Y' "
				+ "	and service_dtls.is_panel = 'Y' "
				+ "	and ord_dtls.order_details_id = :orderDetailsId "
				+ "	and panel.panel_id = :panelId "
				+ "group by "
				+ "	\"orderId\", "
				+ "	\"orderDetailsId\", "
				+ "	\"serviceMasterId\", "
				+ "	\"serviceStandardName\", "
				+ "	\"serviceStandardCode\", "
				+ "	\"specialityName\", "
				+ "	\"labSampleDtlsId\", "
				+ "	\"isPanel\", "
				+ "	\"panelId\", "
				+ "	\"subDeptId\", "
				+ "	\"statusName\","
				+ " \"sampleStatusId\""
				),
		
		@NamedNativeQuery(name="GET_LIST_OF_SERVICE_MASTER",
		query="select ser_mst.service_master_id as \"serviceMasterId\", ser_mst.service_standard_name as \"serviceStandardName\" "
			   + " from service.m_service_master ser_mst "
			   + " inner join service.t_unit_service_mapper unt on ser_mst.service_master_id=unt.service_id "
			   + " where unt.orgnisation_id=:orgId "
			   + " and unt.unit_id=:unitId"),
		
		@NamedNativeQuery(name="GET_DEPT_WISE_SERVICE_LIST_FOR_CYCLIC_ORDER",
		query=" SELECT service.service_master_id as \"serviceMasterId\", " + 
				" service.organization_id as \"organizationId\", " + 
				" service.service_standard_name as \"serviceStandardName\",  " + 
				"service.speciality_id as \"specialityId\"," + 
				" service.service_standard_code as\"serviceStandardCode\", " + 
				" spl.speciality_name as \"specialityName\", "  + 
				" serviceBasePrice.base_price as \"serviceAmount\","+
				" tax.id as \"taxId\"," + 
				" tax.tax_percentage as \"taxPercentage\"" + 
				" FROM service.m_service_master service " + 
				" inner join service.t_unit_service_master_details unitService on unitService.service_master_id=service.service_master_id" + 
				" inner join doctor.m_speciality_master spl on spl.speciality_id=service.speciality_id" + 
				" inner join service.m_service_base_price_master serviceBasePrice "+
				" on serviceBasePrice.service_master_id=service.service_master_id and serviceBasePrice.unit_id=unitService.unit_id "+
			    " left join public.m_tax tax on tax.id=unitService.gst_type_id " + 
				" WHERE  service.speciality_id =:specialityId " + 
				" AND  unitService.is_cyclic='Y'" + 
				" AND service.organization_id=:organizationId " + 
				" AND unitService.unit_id=:unitId "
				+ " ORDER BY service.service_standard_name ASC"),
		
		@NamedNativeQuery(name="GET_SERVICE_LIST_FOR_CYCLIC_ORDER",
		query=" SELECT service.service_master_id as \"serviceMasterId\", " + 
				" service.organization_id as \"organizationId\", " + 
				" service.service_standard_name as \"serviceStandardName\",  " + 
				"service.speciality_id as \"specialityId\"," + 
				" service.service_standard_code as\"serviceStandardCode\", " + 
				" spl.speciality_name as \"specialityName\", "  + 
				" serviceBasePrice.base_price as \"serviceAmount\","+
				" tax.id as \"taxId\"," + 
				" tax.tax_percentage as \"taxPercentage\"" + 
				" FROM service.m_service_master service " + 
				" inner join service.t_unit_service_master_details unitService on unitService.service_master_id=service.service_master_id" + 
				" inner join doctor.m_speciality_master spl on spl.speciality_id=service.speciality_id" + 
				" inner join service.m_service_base_price_master serviceBasePrice "+
				" on serviceBasePrice.service_master_id=service.service_master_id and serviceBasePrice.unit_id=unitService.unit_id "+
			    " left join public.m_tax tax on tax.id=unitService.gst_type_id " + 
				" WHERE unitService.is_cyclic='Y'" + 
				" AND service.organization_id=:organizationId " + 
				" AND unitService.unit_id=:unitId "
				+ " ORDER BY service.service_standard_name ASC"),
		
		@NamedNativeQuery(name="GET_DETAILS_BY_ITEM",query="SELECT orderDetails.order_details_id as \"orderDetailsId\", "
				+ "orderDetails.org_id as \"orgId\", "
				+ "ord.order_no as \"orderNo\", "
				+ "orderDetails.org_unit_id as \"orgUnitId\", "
				+ "orderDetails.order_id as \"orderId\", "
				+ "orderDetails.order_qty as \"quantity\", "
				+ "service.service_standard_name as \"serviceStandardName\", "
				+ "orderDetails.priority_id as \"priorityId\", "
				+ "orderDetails.is_outsourced as \"isOutsourced\", "
				+ " coalesce(orderDetails.service_amt,0) as \"basePrice\", "
				+ " coalesce(orderDetails.ord_concession,0) as \"concession\", "
				+ " coalesce(orderDetails.ord_discount,0) as \"discount\", "
				+ " coalesce(orderDetails.net_amount,0) as \"netPay\", "
				+ "coalesce(orderDetails.net_amount,0) as \"oldNetPay\", "
				+ "orderDetails.ord_doc_spl_id as \"specialityId\", "
				+ "service.speciality_id as \"serviceSpecialityId\", "
				+ "service.sub_speciality_id as \"serviceSubSpecialityId\", "
				+ "orderDetails.tax_id as \"taxId\", "
				+ "tax.name as \"taxName\", "
				+ " coalesce(orderDetails.tax_per,0) as \"taxPercentage\", "
				+ " coalesce(orderDetails.tax_amount,0) as \"taxAmt\", "
				+ " coalesce(orderDetails.self_payable,0) as \"selfPayable\", "
				+ " coalesce(orderDetails.credit_payable,0) as \"creditPayable\", "
				+ "orderDetails.payee_id as \"payeeId\", "
				+ "orderDetails.package_consumption_amt as \"packageConsumptionAmt\", "
				+ "orderDetails.order_set_id as \"orderSetId\", "
				+ "orderDetails.contract_id as \"contractId\", "
				+ "orderDetails.bed_category_id as \"bedCategoryId\", "
				+ "orderDetails.co_pay_per as \"coPayPer\", "
				+ "orderDetails.doc_share_per as \"docSharePer\", "
				+ "orderDetails.ord_total_amt as \"totalAmt\", "
				+ "orderDetails.ord_cancel_by as \"ordCancelBy\", "
				+ "orderDetails.ord_cancel_remark as \"ordCancelRemark\", "
				+ "orderDetails.tariff_id as \"tariffId\", "
				/*+ "orderDetails.billing_class_id as \"billingClassId\", "
				+ "orderDetails.bed_id as \"bedId\", "
				+ "orderDetails.room_id as \"roomId\", "
				+ "orderDetails.ward_id as \"wardId\", "*/
				+ "orderDetails.order_emergency_flag as \"orderEmergencyFlag\", "
				+ "orderDetails.package_id as \"packageId\", "
				+ "orderDetails.ord_doc_id as \"doctorId\", "
				+ "orderDetails.drug_id as \"drugId\", "
				+ "orderDetails.batch_id as \"batchId\", "
				+ "orderDetails.old_ord_dtl_id as \"oldOrdDtlId\", "
				+ "orderDetails.service_rendered as \"serviceRendered\", "
				+ "orderDetails.service_chargeable as \"serviceChargeable\", "
				+ "orderDetails.service_rendering_deptid as \"serviceRenderingDeptId\", "
				+ "orderDetails.service_is_billed as \"serviceIsBilled\", "
				+ "orderDetails.service_bill_id as \"serviceBillId\", "
				+ "orderDetails.ord_remarks as \"ordRemarks\", "
				+ "orderDetails.ord_cancelled as \"ordCancelled\", "
				+ "orderDetails.ord_cancel_reason_id as \"ordCancelReasonId\", "
				+ "orderDetails.is_drug as \"isDrug\", "
				+ "orderDetails.status as \"status\", "
				+ "orderDetails.updated_by as \"updatedBy\", "
				+ "orderDetails.updated_date as \"updatedDate\", "
				+ "orderDetails.created_date as \"createdDate\", "
				+ "orderDetails.created_by as \"createdBy\", "
				+ "orderDetails.is_package_service as \"isPackageService\", "
				+ " orderDetails.is_cap_service as \"isCapService\", "
				+ "unitService.is_rate_editable as \"isRateEditable\", "
				+ "unitService.rate_editble_min_value as \"minRateEditable\", "
				+ "unitService.rate_editable_max_value as \"maxRateEditable\", "
				+ "unitService.is_tax_applicable as \"isTaxApplicable\", "
				+ "unitService.is_ref_doctor_share as \"isRefDoctorShare\", "
				+ "unitService.is_discount as \"isDiscountApplicable\", "
				+ "unitService.discount_value as \"discountValue\", "
				+ "unitService.gst_type_id as \"gstTypeId\", "
				+ "unitService.otc_gst_type_id as \"otcGstTypeId\", "
				+ "unitService.is_allow_multiple_quantity as \"isQuantityEditable\", "
				+ "(SELECT coalesce(mpack.package_type_id,0) from service.m_package_master mpack where mpack.package_master_id = orderDetails.package_id) as \"packageTypeId\", "
				+ "speciality.speciality_name as \"specialityName\" , subSpec.sub_speciality_name as \"subSpecialityName\", "
				+ "orderDetails.sale_detail_id as \"saleDetailId\", "
				+ "concat('Dr. ',coalesce(doc.first_name),' ',coalesce(doc.middle_name),' ',coalesce(doc.last_name)) as \"doctorName\",orderDetails.is_package as \"isPackage\" "
				+ "FROM public.t_order_details orderDetails "
				+ "INNER JOIN public.t_order_master ord on orderDetails.order_id=ord.order_id "
				+ "LEFT JOIN  service.m_service_master  service on service.service_master_id=orderDetails.service_id "
				+ "LEFT JOIN  doctor.m_speciality_master speciality  on speciality.speciality_id= service.speciality_id "
				+ "LEFT JOIN  doctor.m_sub_speciality_master  subSpec on  subSpec.sub_speciality_id=service.sub_speciality_id "
				+ "LEFT JOIN  service.t_unit_service_master_details unitService on  unitService.service_master_id=service.service_master_id "
				+ "LEFT JOIN  doctor.m_doctor_master  doc on doc.doctor_id=orderDetails.ord_doc_spl_id "
				+ "LEFT JOIN  public.m_tax   tax on tax.id= orderDetails.tax_id "
				+ "WHERE "
				+ "  orderDetails.status='A' "
				+ "AND orderDetails.sale_detail_id=:saleDetailId"),
		
		@NamedNativeQuery(name="GET_CANCEL_SERVICES_BY_ENCOUNTER_ID",
		query="SELECT ordermaster.order_no as \"orderNo\", orderdetail.order_qty as \"quantity\","
				+ "serviceMastr.service_standard_name as \"serviceStandardName\", to_char(orderdetail.order_date,'yyyy-mm-dd') as \"orderDateString\","
				+ "coalesce( orderdetail.ord_total_amt,0) as \"netPay\",rm.reason_desc as \"reasonDesc\","
//				+ "um.user_name as \"userName\","
				+"(case when ( em.employee_id is not null )then "
				+"		 (concat(coalesce(em.first_name), ' ', coalesce(em.middle_name), ' ', coalesce(em.last_name))) "
				+"	 else  "
				+"	 (concat(coalesce(doc.first_name), ' ', coalesce(doc.middle_name), ' ', coalesce(doc.last_name))) end ) as \"userName\","
				+ " to_char(orderdetail.ord_cancel_datetime,'yyyy-mm-dd') as \"ordCancelDatetimeString\", "
				+ " orderdetail.ord_cancel_datetime as \"ordCancelDatetimeStringNew\", "
				+ " orderdetail.order_date as \"orderDateStringNew\""
				+ " FROM public.t_order_details orderdetail "
				+ " INNER JOIN public.t_order_master ordermaster ON	orderdetail.order_id = ordermaster.order_id"
				+ " LEFT OUTER JOIN opd.t_encounter_master encounterm ON ordermaster.encounter_id = encounterm.encounter_id"
				+ " INNER JOIN service.m_service_master serviceMastr ON orderdetail.service_id = serviceMastr.service_master_id"
				+ " INNER JOIN service.t_unit_service_master_details unitservice ON serviceMastr.service_master_id = unitservice.service_master_id"
				+ " LEFT JOIN adt.m_reason_master rm ON orderdetail.ord_cancel_reason_id=rm.reason_master_id"
				+ " LEFT JOIN public.m_user_master um ON um.user_id=orderdetail.updated_by "
				+ " left join doctor.m_doctor_master doc on doc.user_id=orderdetail.updated_by "
				+ " left join public.m_employee_master em on em.user_id=orderdetail.updated_by "
				+ " WHERE orderdetail.service_is_billed = 0"
				+ " AND unitservice.unit_id = orderdetail.org_unit_id"
				+ " AND ( ordermaster.encounter_id =:encounterId  AND ordermaster.visit_type_id =:visitTypeId"
				+ " OR ordermaster.admission_id =:encounterId AND ordermaster.visit_type_id =:visitTypeId)"
				+ " AND orderdetail.ord_cancelled = 'Y' AND orderdetail.status = 'I' AND ordermaster.org_id =:orgId"),
		
		@NamedNativeQuery(name="CHECK_SERVICE_MAPPED_WITH_TEST",
		query= "SELECT distinct(usmd.service_master_id) as \"serviceMasterId\" ,usmd.is_panel  as \"isPanel\", test_mst.test_id as \"testId\" "
				+ "FROM   service.m_service_master sm "
				+ "       INNER JOIN service.t_unit_service_master_details usmd "
				+ "               ON sm.service_master_id = usmd.service_master_id "
				+ "                  AND usmd.status = 'A' "
				+ "       LEFT JOIN lab.m_test_master test_mst "
				+ "               ON usmd.service_master_id = test_mst.service_id AND usmd.unit_id = test_mst.org_unit_id "
				+ "                  AND usmd.status = 'A' "
				+ " WHERE  sm.service_master_id = :serviceId "
				+ "       AND usmd.unit_id = :unitId order by usmd.service_master_id desc limit 1"),
		
		@NamedNativeQuery(name="GET_CURRENT_INVESTIGATION_BY_PRO_REQ_ID",query="with investigations as( ( " 
				 + " select " 
				 + " 	ord_mst.order_id as \"orderId\", " 
				 + " 	service_mst.sub_speciality_id as \"subDeptId\", " 
				 + " 	ord_mst.admission_id as \"admissionId\", " 
				 + " 	ord_mst.encounter_id as \"encounterId\", " 
				 + " 	ord_mst.patient_id as \"patientId\", " 
				 + " 	ord_dtls.order_details_id as \"orderDetailsId\", " 
				 + " 	ord_dtls.service_id as \"serviceMasterId\", " 
				 + " 	ord_dtls.order_set_id as \"orderSetId\", " 
				 + " 	to_char(ord_dtls.order_sch_date, 'dd/MM/yyyy') as \"orderSchDate\", " 
				 + " 	service_mst.service_standard_name as \"serviceStandardName\", " 
				 + " 	service_mst.service_standard_code as \"serviceStandardCode\", " 
				 + " 	speciality_mst.speciality_name as \"specialityName\", " 
				 + " 	sample_dtls.lab_sample_dtls_id as \"labSampleDtlsId\", " 
				 + " 	service_dtls.is_panel as \"isPanel\", " 
				 + " 	max(lab_res.lab_test_res_id) as \"labTestResId\", " 
				 + " 	cast( null as integer ) as \"panelId\", " 
				 + " 	test_status.status_name as \"statusName\", " 
				 + " 	test_status.test_status_id as \"sampleStatusId\", " 
				 + " 	to_char( ord_dtls.created_date, 'dd/MM/yyyy' ) as \"createdDate\", " 
				 + " 	ord_dtls.service_rendered as \"serviceRendered\", " 
				 + " 	ord_dtls.is_package_service as \"isPackageService\", " 
				 + " 	templatetr12_.template_transaction_id as \"templateTransactionId\", " 
				 + " 	ord_dtls.service_is_billed as \"serviceIsBilled\", " 
				 + " 	ord_dtls.status as \"orderStatus\", " 
				 + " 	am.appointment_id as \"appointmentId\", " 
				 + " 	am.slot_id as \"slotId\", " 
				 + " 	ord_dtls.package_id as \"packageId\", " 
				 + " 	prm.pro_req_id as \"proReqId\", " 
				 + " 	prm1.pro_req_id as \"procedureReqId\" " 
				 + " from " 
				 + " 	public.t_order_master ord_mst " 
				 + " inner join public.t_order_details ord_dtls on " 
				 + " 	ord_mst.order_id = ord_dtls.order_id " 
				 + " inner join service.t_unit_service_mapper service_mppr on " 
				 + " 	service_mppr.service_id = ord_dtls.service_id " 
				 + " 	and service_mppr.unit_id = ord_dtls.org_unit_id " 
				 + " inner join service.t_unit_service_master_details service_dtls on " 
				 + " 	service_dtls.service_master_id = service_mppr.service_id " 
				 + " 	and service_dtls.unit_id = ord_dtls.org_unit_id " 
				 + " inner join service.m_service_master service_mst on " 
				 + " 	service_mst.service_master_id = ord_dtls.service_id " 
				 + " left join doctor.m_speciality_master speciality_mst on " 
				 + " 	speciality_mst.speciality_id = service_mst.speciality_id " 
				 + " left join t_unit_speciality_mapper speciality_mpr on " 
				 + " 	speciality_mpr.speciality_id = service_mst.speciality_id " 
				 + " 	and speciality_mpr.unit_id  = service_mppr.unit_id " 
				 + " left join lab.t_lab_sample_dtls sample_dtls on " 
				 + " 	sample_dtls.order_details_id = ord_dtls.order_details_id " 
				 + " 	and sample_dtls.sample_status_id <> 4 " 
				 + " left join lab.m_test_status_master test_status on " 
				 + " 	test_status.test_status_id = sample_dtls.sample_status_id " 
				 + " left join lab.t_lab_test_result lab_res on " 
				 + " 	lab_res.lab_sample_dtls_id = sample_dtls.lab_sample_dtls_id " 
				 + " 	and lab_res.sample_status_id in( 8, " 
				 + " 	9 ) " 
				 + " left outer join public.t_template_transaction templatetr12_ on " 
				 + " 	ord_dtls.order_details_id = templatetr12_.order_details_id " 
				 + " 	and( templatetr12_.status = 'A' ) " 
				 + " left join opd.m_appointment_master am on " 
				 + " 	am.order_details_id = ord_dtls.order_details_id " 
				 + " left join t_unit_application_billing_configuration uabc on " 
				 + " 	uabc.unit_id = ord_dtls.org_unit_id " 
				 + " 	and uabc.status = 'A' " 
				 + " left join otims.t_procedure_req_mst prm on " 
				 + " 	ord_mst.admission_id = prm.admission_id " 
				 + " left join otims.t_procedure_req_mst prm1 on " 
				 + " 	ord_mst.encounter_id = prm1.encounter_id " 
				 + " where " 
				 + " 	service_mst.status = 'A' " 
				 + " 	and ord_dtls.old_ord_dtl_id is null " 
				 + " 	and service_dtls.is_package = 'I' " 
				 + " 	and ord_dtls.nursing_service_order_flag = 'N' " 
				 + " 	and service_dtls.is_panel = 'N' " 
				// + " 	and speciality_mpr.is_rendering_speciality = 'Y' " 
				 + " and speciality_mpr.is_rendering_speciality in (select is_rendering_speciality from t_unit_speciality_mapper where is_rendering_speciality = 'Y' ) "
				 + " 	and ord_dtls.is_procedure_service = 'N' " 
				 + " 	and ord_dtls.procedure_order_mst_id is not null " 
				 + " group by " 
				 + " 	\"orderId\", " 
				 + " 	\"orderDetailsId\", " 
				 + " 	\"serviceMasterId\", " 
				 + " 	\"serviceStandardName\", " 
				 + " 	\"serviceStandardCode\", " 
				 + " 	\"specialityName\", " 
				 + " 	\"labSampleDtlsId\", " 
				 + " 	\"isPanel\", " 
				 + " 	\"subDeptId\", " 
				 + " 	\"panelId\", " 
				 + " 	\"statusName\", " 
				 + " 	\"createdDate\", " 
				 + " 	\"sampleStatusId\", " 
				 + " 	\"templateTransactionId\", " 
				 + " 	\"appointmentId\", " 
				 + " 	\"slotId\" , " 
				 + " 	\"proReqId\", " 
				 + " 	\"procedureReqId\" ) " 
				 + " union( " 
				 + " select " 
				 + " 	ord_mst.order_id as \"orderId\", " 
				 + " 	service_mst.sub_speciality_id as \"subDeptId\", " 
				 + " 	ord_mst.admission_id as \"admissionId\", " 
				 + " 	ord_mst.encounter_id as \"encounterId\", " 
				 + " 	ord_mst.patient_id as \"patientId\", " 
				 + " 	ord_dtls.order_details_id as \"orderDetailsId\", " 
				 + " 	ord_dtls.service_id as \"serviceMasterId\", " 
				 + " 	ord_dtls.order_set_id as \"orderSetId\", " 
				 + " 	to_char(ord_dtls.order_sch_date, 'dd/MM/yyyy') as \"orderSchDate\", " 
				 + " 	service_mst.service_standard_name as \"serviceStandardName\", " 
				 + " 	service_mst.service_standard_code as \"serviceStandardCode\", " 
				 + " 	speciality_mst.speciality_name as \"specialityName\", " 
				 + " 	cast( null as integer ) as \"labSampleDtlsId\", " 
				 + " 	service_dtls.is_panel as \"isPanel\", " 
				 + " 	cast( null as integer ) as \"labTestResId\", " 
				 + " 	panel.panel_id as \"panelId\", " 
				 + " 	null as \"statusName\", " 
				 + " 	0 as \"sampleStatusId\", " 
				 + " 	to_char( ord_dtls.created_date, 'dd/MM/yyyy' ) as \"createdDate\", " 
				 + " 	ord_dtls.service_rendered as \"serviceRendered\", " 
				 + " 	ord_dtls.is_package_service as \"isPackageService\", " 
				 + " 	templatetr12_.template_transaction_id as \"templateTransactionId\", " 
				 + " 	ord_dtls.service_is_billed as \"serviceIsBilled\", " 
				 + " 	ord_dtls.status as \"orderStatus\", " 
				 + " 	am.appointment_id as \"appointmentId\", " 
				 + " 	am.slot_id as \"slotId\", " 
				 + " 	ord_dtls.package_id as \"packageId\", " 
				 + " 	prm.pro_req_id as \"proReqId\", " 
				 + " 	prm1.pro_req_id as \"procedureReqId\" " 
				 + " from " 
				 + " 	public.t_order_master ord_mst " 
				 + " inner join public.t_order_details ord_dtls on " 
				 + " 	ord_mst.order_id = ord_dtls.order_id " 
				 + " inner join service.t_unit_service_mapper service_mppr on " 
				 + " 	service_mppr.service_id = ord_dtls.service_id " 
				 + " 	and service_mppr.unit_id = ord_dtls.org_unit_id " 
				 + " inner join service.t_unit_service_master_details service_dtls on " 
				 + " 	service_dtls.service_master_id = service_mppr.service_id " 
				 + " 	and service_dtls.unit_id = ord_dtls.org_unit_id " 
				 + " inner join service.m_service_master service_mst on " 
				 + " 	service_mst.service_master_id = ord_dtls.service_id " 
				 + " left join doctor.m_speciality_master speciality_mst on " 
				 + " 	speciality_mst.speciality_id = service_mst.speciality_id " 
				 + " left join t_unit_speciality_mapper speciality_mpr on " 
				 + " 	speciality_mpr.speciality_id = service_mst.speciality_id " 
				 + " 	and speciality_mpr.unit_id  = service_mppr.unit_id	 " 
				 + " inner join lab.t_panel_master panel on " 
				 + " 	panel.service_id = service_mst.service_master_id " 
				 + " 	and panel.org_unit_id = service_mppr.unit_id " 
				 + " left outer join public.t_template_transaction templatetr12_ on " 
				 + " 	ord_dtls.order_details_id = templatetr12_.order_details_id " 
				 + " 	and( templatetr12_.status = 'A' ) " 
				 + " left join opd.m_appointment_master am on " 
				 + " 	am.order_details_id = ord_dtls.order_details_id " 
				 + " left join t_unit_application_billing_configuration uabc on " 
				 + " 	uabc.unit_id = ord_dtls.org_unit_id " 
				 + " 	and uabc.status = 'A' " 
				 + " left join otims.t_procedure_req_mst prm on " 
				 + " 	ord_mst.admission_id = prm.admission_id " 
				 + " left join otims.t_procedure_req_mst prm1 on " 
				 + " 	ord_mst.encounter_id = prm1.encounter_id " 
				 + " 	where " 
				 + " 	service_mst.status = 'A' " 
				 + " 	and ord_dtls.old_ord_dtl_id is null " 
				 + " 	and service_dtls.is_package = 'I' " 
				 + " 	and service_dtls.is_panel = 'Y' " 
				// + " 	and speciality_mpr.is_rendering_speciality = 'Y' " 
				 + " and speciality_mpr.is_rendering_speciality in (select is_rendering_speciality from t_unit_speciality_mapper where is_rendering_speciality = 'Y' ) "
				 + " 	and ord_dtls.nursing_service_order_flag = 'N' " 
				 + " 	and ord_dtls.is_procedure_service = 'N' " 
				 + " 	and ord_dtls.procedure_order_mst_id is not null " 
				 + " group by " 
				 + " \"orderId\", " 
				 + " \"orderDetailsId\", " 
				 + " \"serviceMasterId\", " 
				 + " \"serviceStandardName\", " 
				 + " \"serviceStandardCode\", " 
				 + " \"specialityName\", " 
				 + " \"labSampleDtlsId\", " 
				 + " \"isPanel\", " 
				 + " \"subDeptId\", " 
				 + " \"panelId\", " 
				 + " \"createdDate\", " 
				 + " \"sampleStatusId\", " 
				 + " \"templateTransactionId\", " 
				 + " \"appointmentId\", " 
				 + " \"slotId\", " 
				 + " \"proReqId\", " 
				 + " \"procedureReqId\" )) " 
				 + " select " 
				 + " 	* " 
				 + " from " 
				 + " 	investigations inv "  
				),

		@NamedNativeQuery(name="CHECK_FOR_UNIQUE_SERVICE_CODE",
		query=" SELECT service.service_master_id as \"serviceMasterId\" " + 
				" FROM service.m_service_master service " + 
				" WHERE service.organization_id=:orgId " + 
				" AND lower(service.service_standard_code) = :serviceStandardCode "),
		
		@NamedNativeQuery(name="CHECK_FOR_UNIQUE_SERVICE_NAME",
		query=" SELECT service.service_master_id as \"serviceMasterId\" " + 
				" FROM service.m_service_master service " + 
				" WHERE service.organization_id=:orgId " + 
				" AND lower(service.service_standard_name) = :serviceStandardName "),
		
		@NamedNativeQuery(name="GET_LIST_OF_SERVICES_FOR_WEBAPP_INTEGRATION",
		query="select ser_mst.service_master_id as \"serviceMasterId\", ser_mst.service_standard_name as \"serviceStandardName\" "
			   + " from public.t_website_integration_service_mapper ser_mpper "
			   + " inner join service.m_service_master ser_mst on ser_mpper.service_id = ser_mst.service_master_id "
			   + " inner join service.t_unit_service_mapper unt on ser_mst.service_master_id=unt.service_id "
			   + " where unt.orgnisation_id=:orgId "
			   + " and unt.unit_id=:unitId "
			   + " and ser_mpper.organization_id=:orgId "
			   + " and ser_mpper.unit_id=:unitId "),
})
@Entity
@Table(name = "m_service_master", schema = "service")
@SequenceGenerator(name = "service_master_seq", sequenceName = "service.service_master_seq", allocationSize = 1)
public class ServiceMaster {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "service_master_seq")
	@Column(name = "service_master_id")
	private int serviceMasterId;

	@Column(name = "service_code_type_id")
	private Integer serviceCodeTypeId;	

	@Column(name = "service_alias_name")
	private String serviceAliasName;	

	@Column(name = "service_short_description")
	private String serviceShortDescription;

	@Column(name = "service_long_description")
	private String serviceLongDescripton;

	@Column(name = "is_available_for_package")
	private char isAvailableForPackage;

	@Column(name = "is_outsource_service")
	private char isOutsourceService;

	@Column(name = "is_ot_procedure")
	private char isOtProcedure;

	@Column(name = "is_order_set")
	private char isOrderSet;

	@Column(name = "is_rate_editable")
	private char isRateEditable;

	@Column(name = "min_rate_editable")
	private float minRateEditable;

	@Column(name = "max_rate_editable")
	private float maxRateEditable;

	@Column(name = "is_discount_applicable")
	private char isDiscountApplicable;

	@Column(name = "is_allow_multiple_quantity")
	private char isAllowMultipleQuantity;

	@Column(name = "is_doctor_required")
	private char isDoctorRequired;

	@Column(name = "is_tax_applicable")
	private char isTaxApplicable;

	@Column(name = "rendering_department_id")
	private Integer renderingDepartmentId;

	@Column(name = "is_autorendered")
	private char isAutorendered;
	
	@Column(name = "service_standard_name")
	private String serviceStandardName;

	@Column(name = "service_standard_code")
	private String serviceStandardCode;

	@Column(name = "speciality_id")
	private Integer specialityId;

	@Column(name = "sub_speciality_id")
	private Integer subSpecialityId;
	
	@Column(name = "organization_id")
	private Integer organizationId;

	@Column(name = "created_by")
	private int createdBy;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "status")
	private char status;

	@Column(name = "updated_by")
	private int updatedBy;

	@Column(name = "updated_date")
	private Date updatedDate;

	@Column(name = "codification_service_name")
	private String codificationServiceName;

	@Column(name = "valid_from")
	private Date validFrom;

	@Column(name = "valid_till")
	private Date validTill;
	
	@Column(name = "general_ledger_id")
	private Integer generalLedgerId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "general_ledger_id", insertable = false, updatable = false, nullable = false)
	private GeneralLedgerMaster generalLedgerMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "speciality_id", insertable = false, updatable = false, nullable = false)
	private SpecialityMaster specialityMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sub_speciality_id", insertable = false, updatable = false, nullable = false)
	private SubSpecialityMaster subSpecialityMaster;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "serviceMaster")
	private List<TestMaster> listTestMaster;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "serviceMaster", fetch = FetchType.LAZY)
	private List<MPackageMaster> listMPackageMaster;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "serviceMaster", fetch = FetchType.LAZY)
	private List<TPackageServicesDetails> listTPackageServicesDetails;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "serviceMaster", fetch = FetchType.LAZY)
	private List<TPackageIncExcDetails> listTPackageIncExcDetails;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "serviceMaster")
	private List<TContractServiceDetails> listTContractServiceDetails;
	
	@OneToMany(fetch = FetchType.LAZY , cascade = CascadeType.ALL , mappedBy = "serviceMaster")
	private List<UnitServiceMasterDetails> unitServiceMasterDetailsList;
	
	@OneToMany(fetch = FetchType.LAZY , cascade = CascadeType.ALL , mappedBy = "serviceMaster")
	private List<MPeriodicityServiceConfig> listMPeriodicityServiceConfig;
	
	@OneToMany(fetch = FetchType.LAZY , cascade = CascadeType.ALL , mappedBy = "serviceMaster")
	private List<TPeriodicityServiceOrders> listTPeriodicityServiceOrders;

	@OneToMany(fetch = FetchType.LAZY , cascade = CascadeType.ALL , mappedBy = "serviceMaster")
	private List<ProcedureConfiguration> listProcedureConfiguration;
	
	@OneToMany(fetch = FetchType.LAZY , cascade = CascadeType.ALL , mappedBy = "serviceMaster")
	private List<BloodComponentOrderMaster> listBloodComponentOrderMaster;
	
	@OneToMany(fetch = FetchType.LAZY , cascade = CascadeType.ALL , mappedBy = "serviceMaster")
	private List<ProcedureOrderMaster> listProcedureOrderMaster;
	
	public List<ProcedureConfiguration> getListProcedureConfiguration() {
		return listProcedureConfiguration;
	}

	public void setListProcedureConfiguration(List<ProcedureConfiguration> listProcedureConfiguration) {
		this.listProcedureConfiguration = listProcedureConfiguration;
	}

	public List<TPeriodicityServiceOrders> getListTPeriodicityServiceOrders() {
		return listTPeriodicityServiceOrders;
	}

	public void setListTPeriodicityServiceOrders(List<TPeriodicityServiceOrders> listTPeriodicityServiceOrders) {
		this.listTPeriodicityServiceOrders = listTPeriodicityServiceOrders;
	}

	public List<MPeriodicityServiceConfig> getListMPeriodicityServiceConfig() {
		return listMPeriodicityServiceConfig;
	}

	public void setListMPeriodicityServiceConfig(List<MPeriodicityServiceConfig> listMPeriodicityServiceConfig) {
		this.listMPeriodicityServiceConfig = listMPeriodicityServiceConfig;
	}

	public List<TContractServiceDetails> getListTContractServiceDetails() {
		return listTContractServiceDetails;
	}

	public void setListTContractServiceDetails(List<TContractServiceDetails> listTContractServiceDetails) {
		this.listTContractServiceDetails = listTContractServiceDetails;
	}

	public List<TPackageIncExcDetails> getListTPackageIncExcDetails() {
		return listTPackageIncExcDetails;
	}

	public void setListTPackageIncExcDetails(List<TPackageIncExcDetails> listTPackageIncExcDetails) {
		this.listTPackageIncExcDetails = listTPackageIncExcDetails;
	}

	public List<TPackageServicesDetails> getListTPackageServicesDetails() {
		return listTPackageServicesDetails;
	}

	public void setListTPackageServicesDetails(List<TPackageServicesDetails> listTPackageServicesDetails) {
		this.listTPackageServicesDetails = listTPackageServicesDetails;
	}

	public List<MPackageMaster> getListMPackageMaster() {
		return listMPackageMaster;
	}

	public void setListMPackageMaster(List<MPackageMaster> listMPackageMaster) {
		this.listMPackageMaster = listMPackageMaster;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public int getServiceMasterId() {
		return serviceMasterId;
	}

	public void setServiceMasterId(int serviceMasterId) {
		this.serviceMasterId = serviceMasterId;
	}

	public int getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getServiceCodeTypeId() {
		return serviceCodeTypeId;
	}

	public void setServiceCodeTypeId(Integer serviceCodeTypeId) {
		this.serviceCodeTypeId = serviceCodeTypeId;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public String getServiceAliasName() {
		return serviceAliasName;
	}

	public void setServiceAliasName(String serviceAliasName) {
		this.serviceAliasName = serviceAliasName;
	}

	public String getServiceStandardName() {
		return serviceStandardName;
	}

	public void setServiceStandardName(String serviceStandardName) {
		this.serviceStandardName = serviceStandardName;
	}

	public Integer getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}

	public Integer getSubSpecialityId() {
		return subSpecialityId;
	}

	public void setSubSpecialityId(Integer subSpecialityId) {
		this.subSpecialityId = subSpecialityId;
	}

	public String getServiceShortDescription() {
		return serviceShortDescription;
	}

	public void setServiceShortDescription(String serviceShortDescription) {
		this.serviceShortDescription = serviceShortDescription;
	}

	public String getServiceLongDescripton() {
		return serviceLongDescripton;
	}

	public void setServiceLongDescripton(String serviceLongDescripton) {
		this.serviceLongDescripton = serviceLongDescripton;
	}

	public char getIsAvailableForPackage() {
		return isAvailableForPackage;
	}

	public void setIsAvailableForPackage(char isAvailableForPackage) {
		this.isAvailableForPackage = (isAvailableForPackage == '\u0000') ? 'I' : isAvailableForPackage;
	}

	public char getIsOutsourceService() {
		return isOutsourceService;
	}

	public void setIsOutsourceService(char isOutsourceService) {
		this.isOutsourceService = (isOutsourceService == '\u0000') ? 'I' : isOutsourceService;
	}

	public char getIsOtProcedure() {
		return isOtProcedure;
	}

	public void setIsOtProcedure(char isOtProcedure) {
		this.isOtProcedure = (isOtProcedure == '\u0000') ? 'I' : isOtProcedure;
	}

	public char getIsOrderSet() {
		return isOrderSet;
	}

	public void setIsOrderSet(char isOrderSet) {
		this.isOrderSet = (isOrderSet == '\u0000') ? 'I' : isOrderSet;
	}

	public char getIsRateEditable() {
		return isRateEditable;
	}

	public void setIsRateEditable(char isRateEditable) {
		this.isRateEditable = (isRateEditable == '\u0000') ? 'I' : isRateEditable;
	}

	public float getMinRateEditable() {
		return minRateEditable;
	}

	public void setMinRateEditable(float minRateEditable) {
		this.minRateEditable = minRateEditable;
	}

	public float getMaxRateEditable() {
		return maxRateEditable;
	}

	public void setMaxRateEditable(float maxRateEditable) {
		this.maxRateEditable = maxRateEditable;
	}

	public char getIsDiscountApplicable() {
		return isDiscountApplicable;
	}

	public void setIsDiscountApplicable(char isDiscountApplicable) {
		this.isDiscountApplicable = (isDiscountApplicable == '\u0000') ? 'I' : isDiscountApplicable;
	}

	public char getIsAllowMultipleQuantity() {
		return isAllowMultipleQuantity;
	}

	public void setIsAllowMultipleQuantity(char isAllowMultipleQuantity) {
		this.isAllowMultipleQuantity = (isAllowMultipleQuantity == '\u0000') ? 'I' : isAllowMultipleQuantity;
	}

	public char getIsDoctorRequired() {
		return isDoctorRequired;
	}

	public void setIsDoctorRequired(char isDoctorRequired) {
		this.isDoctorRequired = (isDoctorRequired == '\u0000') ? 'I' : isDoctorRequired;
	}

	public char getIsTaxApplicable() {
		return isTaxApplicable;
	}

	public void setIsTaxApplicable(char isTaxApplicable) {
		this.isTaxApplicable = (isTaxApplicable == '\u0000') ? 'I' : isTaxApplicable;
	}

	public Integer getRenderingDepartmentId() {
		return renderingDepartmentId;
	}

	public void setRenderingDepartmentId(Integer renderingDepartmentId) {
		this.renderingDepartmentId = renderingDepartmentId;
	}

	public char getIsAutorendered() {
		return isAutorendered;
	}

	public void setIsAutorendered(char isAutorendered) {
		this.isAutorendered = (isAutorendered == '\u0000') ? 'I' : isAutorendered;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public SpecialityMaster getSpecialityMaster() {
		return specialityMaster;
	}

	public void setSpecialityMaster(SpecialityMaster specialityMaster) {
		this.specialityMaster = specialityMaster;
	}

	public char getStatus() {
		return status;
		
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public String getServiceStandardCode() {
		return serviceStandardCode;
	}

	public void setServiceStandardCode(String serviceStandardCode) {
		this.serviceStandardCode = serviceStandardCode;
	}

	public SubSpecialityMaster getSubSpecialityMaster() {
		return subSpecialityMaster;
	}

	public void setSubSpecialityMaster(SubSpecialityMaster subSpecialityMaster) {
		this.subSpecialityMaster = subSpecialityMaster;
	}

	public List<TestMaster> getListTestMaster() {
		return listTestMaster;
	}

	public void setListTestMaster(List<TestMaster> listTestMaster) {
		this.listTestMaster = listTestMaster;
	}

	public String getCodificationServiceName() {
		return codificationServiceName;
	}

	public void setCodificationServiceName(String codificationServiceName) {
		this.codificationServiceName = codificationServiceName;
	}

	public Date getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(Date validFrom) {
		this.validFrom = validFrom;
	}

	public Date getValidTill() {
		return validTill;
	}

	public void setValidTill(Date validTill) {
		this.validTill = validTill;
	}

	public Integer getGeneralLedgerId() {
		return generalLedgerId;
	}

	public void setGeneralLedgerId(Integer generalLedgerId) {
		this.generalLedgerId = generalLedgerId;
	}

	

	

	
}
