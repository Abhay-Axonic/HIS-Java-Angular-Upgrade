angular.module('myApp').service('commonDetailService', ['$rootScope','$cookies', function ($rootScope,$cookies) {
  var dataId = 0;
  var dataFlag=true;
  var manufacturerName;
  var manufacturerId;
  var addmissionId;
  var uhId;
  var manufactureItemRequestId;
  var visitTypeIdForPatientIssueAcceptance;
  var patientIdForPatientIssueAcceptance;
  var uhidNumForPatientIssueAcceptance;
  var wardIdForPatientIssueAcceptance;
  var wardNameForPatientIssueAcceptance;
  var floorIdForPatientIssueAcceptance;
  var doctorIdForPatientIssueAcceptance;
  var doctorFullNameForPatientIssueAcceptance;
  var patienNameForPatientIssueAcceptance;
  var patientIdForSalesReturn;
  var patientNameForSalesReturn;
  var uhidForSalesReturn; 
  var calculateDisAmount;
  var isFromApprovalEdit; 
  var setDataId = function(id) {
      dataId = id;
  };

  var getDataId = function(){
      return dataId;
  };
  
  var setIsFromApprovalEdit = function(id) {
	  isFromApprovalEdit = id;
  };

  var getIsFromApprovalEdit = function(){
      return isFromApprovalEdit;
  };
  
  var setDataFlag=function(flag)
  {
	  dataFlag=flag;
	  
  }
  var getDataFlag=function()
  {
	  return dataFlag;
	  
  }
  
  var setDataIdForBedManagment = function(idForAdmission, idForUHID){
    addmissionId =idForAdmission;
    uhId = idForUHID;
  }
  var getAdmissionIdForBedManagment = function(){
    return addmissionId;
  }
  var getUHIDForBedManagment = function(){
    return uhId;
  }
  var getManufacturerName = function() {
    return manufacturerName;
}
var getManufacturerId = function() {
  return manufacturerId;
} 
var getItemRequestId = function() {
  return manufactureItemRequestId;
}
var setManufacturer = function(name, id, itemRequestId) {
 manufacturerName = name;
 manufacturerId = id;
 manufactureItemRequestId  = itemRequestId;
}
var setDataIdForBedManagmentPatientIssueAcceptance= function(visitTypeId, patientId,patientName, uhidNum, wardId,wardName,floorId, doctorId, doctorFullName){
  visitTypeIdForPatientIssueAcceptance = visitTypeId;
  patientIdForPatientIssueAcceptance = patientId;
  uhidNumForPatientIssueAcceptance = uhidNum;
  wardIdForPatientIssueAcceptance = wardId;
  wardNameForPatientIssueAcceptance = wardName;
  floorIdForPatientIssueAcceptance = floorId;
  doctorIdForPatientIssueAcceptance = doctorId;
  doctorFullNameForPatientIssueAcceptance = doctorFullName;
  patienNameForPatientIssueAcceptance = patientName;

}
var getDataIdForBedManagmentPatientIssueAcceptanceVisitType = function(){
  return visitTypeIdForPatientIssueAcceptance;
}
var getDataIdForBedManagmentPatientIssueAcceptancePatientId = function(){
  return patientIdForPatientIssueAcceptance;
}
var getDataIdForBedManagmentPatientIssueAcceptanceUhidNum = function(){
  return uhidNumForPatientIssueAcceptance;
}
var getDataIdForBedManagmentPatientIssueAcceptanceWardId = function(){
  return wardIdForPatientIssueAcceptance;
}
var getDataIdForBedManagmentPatientIssueAcceptanceWardName = function(){
  return wardNameForPatientIssueAcceptance;
}
var getDataIdForBedManagmentPatientIssueAcceptanceFloorId = function(){
  return floorIdForPatientIssueAcceptance;
}
var getDataIdForBedManagmentPatientIssueAcceptanceDoctorId = function(){
  return doctorIdForPatientIssueAcceptance;
}
var getDataIdForBedManagmentPatientIssueAcceptanceDoctorFullName = function(){
  return doctorFullNameForPatientIssueAcceptance;
}
var getDataIdForBedManagmentPatientIssueAcceptancePatientName = function(){
  return patienNameForPatientIssueAcceptance;
}

var setDataIdForBedManagmentSalesRetun = function(patientId, patientName, uhid ) {
  patientIdForSalesReturn = patientId;
  patientNameForSalesReturn = patientName;
  uhidForSalesReturn = uhid;
 }
 var getDataIdForBedManagmentSalesRetunPatienId = function(){
  return patientIdForSalesReturn;
}
var getDataIdForBedManagmentSalesRetunPatientName = function(){
  return patientNameForSalesReturn;
}
var getDataIdForBedManagmentSalesRetunUHID = function(){
  return uhidForSalesReturn;
}

var calculateAmount = function(data)
{
	//ganesh
  data.patientClassPercentage	= (data.patientClassPercentage==undefined||	data.patientClassPercentage==null)?0:data.patientClassPercentage;
  var response = {
    discountAmount: 0
  };
  if(data.quantity == undefined){
    data.quantity = 0;
  }
  if(data.discount == undefined){
    data.discount = 0;
  }
  if(data.mrp == undefined){
    data.mrp = 0;
  }
  if(data.patientClassPercentage == undefined){
    data.patientClassPercentage = 0;
  }
  //Calculate rate by applying markup or markdown
  if(data.markUpDownPer != undefined && data.markUpDownPer != null && data.markUpDownPer != 0){
    if(data.markup == 1){
      response.rate = fixedDecimal(data.mrp + (data.mrp * (data.markUpDownPer/100)));
    } else if (data.markup == 0){
      response.rate = fixedDecimal(data.mrp - (data.mrp * (data.markUpDownPer/100)));
    } else {
      response.rate = fixedDecimal(data.mrp);
    }
  }
  else{
    response.rate = fixedDecimal(data.mrp);
  }

  //Calculate total amount with quantity
  var totalAmount = !!data.qty ? response.rate * data.qty : 0;


    //Calcualte discount amount
  var concessionAmount;
  if(data.patientClassPercentage>0)
  {
  concessionAmount=fixedDecimal(totalAmount * (data.patientClassPercentage/100));
  }
else
  {
	concessionAmount=0;
  }
    if(!!data.isDiscPercent){
    	if(concessionAmount>0)
    		{
//    		    var disPer= ((data.discount / (totalAmount))*100).toFixed(2);
    		    response.discountAmount = parseFloat(fixedDecimal((totalAmount-(concessionAmount!=undefined && concessionAmount!=null ? concessionAmount:0)
      		  ) * (data.discount/100)).toFixed(2));
    		}
    	else
    		{
    	      response.discountAmount = parseFloat(fixedDecimal(totalAmount * (data.discount/100)));
    		}
      
      response.unitDiscountAmount = fixedDecimal(response.discountAmount / data.qty);
    } else if(!data.isDiscPercent && !!data.discount) {
    	response.discountAmount = parseFloat(fixedDecimal(totalAmount * (data.discount/100)));
      response.unitDiscountAmount = fixedDecimal(response.discountAmount / data.qty);
    }

  //Calculate final amount after discount
  //response.totalAmount = fixedDecimal(totalAmount) - response.discountAmount;
  response.totalAmount = fixedDecimal(totalAmount);
//Calculate final amount after discount
 

//Calculate tax amount & Net Amount if there is Patient class
// if(data.patientClassPercentage > 0 && (response.discountAmount == 0 || response.discountAmount == undefined)){
//   response.patientClassAmount = fixedDecimal(totalAmount * (data.patientClassPercentage/100));
//   response.unitPatientClassAmount = response.patientClassAmount / data.qty;
//   if(data.isTaxInc) {
//     response.taxAmount = fixedDecimal(((response.totalAmount - response.patientClassAmount) * (data.tax/ 100)));
//     response.netAmount = fixedDecimal((response.totalAmount - response.patientClassAmount));
//     response.discountAmount = 0;
//   } else {
//     response.taxAmount = fixedDecimal((response.totalAmount - response.patientClassAmount)* (data.tax/ 100)); 
//     response.netAmount = fixedDecimal((response.totalAmount - response.patientClassAmount) + response.taxAmount);
//     response.discountAmount = 0;
//   }
// }
// else{
 //Calculate tax amount & Net Amount if there is discount
response.patientClassAmount = fixedDecimal(totalAmount * (data.patientClassPercentage/100));
response.patientClassPercentage = data.patientClassPercentage;
response.unitPatientClassAmount = response.patientClassAmount / data.qty;
 if(data.isTaxInc) {
  // response.taxAmount = fixedDecimal(((response.totalAmount - response.patientClassAmount - response.discountAmount) * (data.tax/ 100)));
  if(data.discount >= 100){  
    response.discountAmount =parseFloat(response.discountAmount) - response.patientClassAmount;
    response.amount1 = (response.totalAmount - (response.patientClassAmount + parseFloat(response.discountAmount))) / (1 + (data.tax/100));
    response.taxAmount = (response.totalAmount -(response.patientClassAmount + parseFloat(response.discountAmount))) - response.amount1;
    response.netAmount = fixedDecimal((response.totalAmount - response.patientClassAmount - parseFloat(response.discountAmount)));
  }
  else{
    response.amount1 = (response.totalAmount - (response.patientClassAmount + parseFloat(response.discountAmount))) / (1 + (data.tax/100));
    response.taxAmount = (response.totalAmount -(response.patientClassAmount + parseFloat(response.discountAmount))) - response.amount1;
    response.netAmount = fixedDecimal((response.totalAmount - response.patientClassAmount - parseFloat(response.discountAmount)));
  }
  // response.patientClassAmount = 0;
  // response.unitPatientClassAmount = 0;
} else {
  if(data.discount >= 100){  
    response.discountAmount = parseFloat(response.discountAmount) - response.patientClassAmount;
    response.taxAmount = fixedDecimal((response.totalAmount - (parseFloat(response.discountAmount) + response.patientClassAmount))* (data.tax/ 100)); 
    response.netAmount = fixedDecimal((response.totalAmount - (parseFloat(response.discountAmount) + response.patientClassAmount)) + response.taxAmount);
  }
  else{
    response.taxAmount = fixedDecimal((response.totalAmount - (response.patientClassAmount + parseFloat(response.discountAmount)))* (data.tax/ 100)); 
    response.netAmount = fixedDecimal((response.totalAmount - (response.patientClassAmount + parseFloat(response.discountAmount))) + response.taxAmount);
  }
  // response.patientClassAmount = 0;
  // response.unitPatientClassAmount = 0;
 }
// }
 if(isNaN(response.unitDiscountAmount) || response.unitDiscountAmount==undefined || response.unitDiscountAmount==null)
	 {
	 response.unitDiscountAmount=0;
	 }
 if(isNaN(response.netAmount) || response.netAmount==undefined || response.netAmount==null)
 {
 response.netAmount=0;
 }
 if(isNaN(response.taxAmount) || response.taxAmount==undefined || response.taxAmount==null)
 {
 response.taxAmount=0;
 }
 if(isNaN(response.discountAmount) || response.discountAmount==undefined || response.discountAmount==null)
 {
 response.discountAmount=0;
 }
 if(isNaN(response.unitPatientClassAmount) || response.unitPatientClassAmount==undefined || response.unitPatientClassAmount==null)
 {
 response.unitPatientClassAmount=0;
 }
 if(isNaN(response.patientClassAmount) || response.patientClassAmount==undefined || response.patientClassAmount==null)
 {
 response.patientClassAmount=0;
 }
 if(isNaN(response.patientClassPercentage) || response.patientClassPercentage==undefined || response.patientClassPercentage==null)
 {
 response.patientClassPercentage=0;
 }
  // if(data.paymentEntitlementTypeId == 1){
  //   response.selfPayable  = response.netAmount;
  // }
	response.creditPayable = 0;
	response.packageConsumptionAmt = 0;
  return response;
}

/**************For Pharmacy Discount Calculation*********/
var calculateDisAmount=function(data){

	//ganesh
  data.patientClassPercentage	= (data.patientClassPercentage==undefined||	data.patientClassPercentage==null)?0:data.patientClassPercentage;
  var response = {
    discountAmount: 0
  };
  if(data.quantity == undefined){
    data.quantity = 0;
  }
  if(data.discount == undefined){
    data.discount = 0;
  }
  if(data.mrp == undefined){
    data.mrp = 0;
  }
  if(data.patientClassPercentage == undefined){
    data.patientClassPercentage = 0;
  }
  //Calculate rate by applying markup or markdown
  if(data.markUpDownPer != undefined && data.markUpDownPer != null && data.markUpDownPer != 0){
    if(data.markup == 1){
      response.rate = fixedDecimal(data.mrp + (data.mrp * (data.markUpDownPer/100)));
    } else if (data.markup == 0){
      response.rate = fixedDecimal(data.mrp - (data.mrp * (data.markUpDownPer/100)));
    } else {
      response.rate = fixedDecimal(data.mrp);
    }
  }
  else{
    response.rate = fixedDecimal(data.mrp);
  }

  //Calculate total amount with quantity
  var totalAmount = !!data.qty ? response.rate * data.qty : 0;


  //Calculate discount amount
  if(!!data.isDiscPercent){
    response.discountAmount = fixedDecimal(totalAmount * (data.discount/100));
    response.unitDiscountAmount = fixedDecimal(response.discountAmount / data.qty);
  } else if(!data.isDiscPercent && !!data.discount) {
    response.discountAmount=  fixedDecimal(totalAmount * data.discountPerItem*0.01);
    response.unitDiscountAmount = fixedDecimal(response.discountAmount / data.qty);
    response.discountPerItem = parseFloat(data.discountPerItem).toFixed(2);
  }
  else{
    response.discountAmount=  fixedDecimal(totalAmount * data.discountPerItem*0.01);
    response.unitDiscountAmount = fixedDecimal(response.discountAmount / data.qty);
    response.discountPerItem = parseFloat(data.discountPerItem).toFixed(2);
  }

  //Calculate final amount after discount
  response.totalAmount = fixedDecimal(totalAmount);
 
 //Calculate tax amount & Net Amount if there is discount
response.patientClassAmount = fixedDecimal(totalAmount * (data.patientClassPercentage/100));
response.patientClassPercentage = data.patientClassPercentage;
response.unitPatientClassAmount = response.patientClassAmount / data.qty;
 if(data.isTaxInc) {
  if(data.discount >= 100){
    response.discountAmount = response.discountAmount - response.patientClassAmount;
    response.amount1 = (response.totalAmount - (response.patientClassAmount + response.discountAmount)) / (1 + (data.tax/100));
    response.taxAmount = (response.totalAmount - (response.patientClassAmount + response.discountAmount)) - response.amount1;
    // response.taxAmount = fixedDecimal(((response.totalAmount - response.patientClassAmount - response.discountAmount) * (data.tax/ 100)));
    response.netAmount = fixedDecimal((response.totalAmount - response.patientClassAmount - response.discountAmount));   
  }
  else{
    response.amount1 = (response.totalAmount - (response.patientClassAmount + response.discountAmount)) / (1 + (data.tax/100));
    response.taxAmount = (response.totalAmount - (response.patientClassAmount + response.discountAmount)) - response.amount1;
    // response.taxAmount = fixedDecimal(((response.totalAmount - response.patientClassAmount - response.discountAmount) * (data.tax/ 100)));
    response.netAmount = fixedDecimal((response.totalAmount - response.patientClassAmount - response.discountAmount));
  }
} else {
  if(data.discount >= 100){  
    response.discountAmount = response.discountAmount - response.patientClassAmount;
    response.taxAmount = fixedDecimal((response.totalAmount - (response.patientClassAmount + response.discountAmount))* (data.tax/ 100)); 
  response.netAmount = fixedDecimal((response.totalAmount - (response.patientClassAmount + response.discountAmount)) + response.taxAmount);
  }
  else{
    response.taxAmount = fixedDecimal((response.totalAmount - (response.patientClassAmount + response.discountAmount))* (data.tax/ 100)); 
  response.netAmount = fixedDecimal((response.totalAmount - (response.patientClassAmount + response.discountAmount)) + response.taxAmount);
  }
 }
	response.creditPayable = 0;
	response.packageConsumptionAmt = 0;
    return response;

}

/**@Author Ganesh*/
var getDefaultBatchFromConfig = function(batchArray)
{
	var cookieObject = $cookies.getObject('cookieObject');
	if (cookieObject == undefined) {return null;} else if (cookieObject.autoBatch != null && cookieObject.autoBatch == true) {
		var selectBatch = batchArray[0];
		if(selectBatch!=undefined && selectBatch.batchId==undefined)
			{
			 return batchArray[1];
			}
	    return batchArray[0];
	} else {
	    return null;
	}
}

/**@Author Ganesh*/
var getDefaultQuantity = function(item,qtyPrescribed,availableqty)
{
	if((item.quantity ==undefined || item.quantity == 0 || item.quantity == null) && (item.selectedBatch!=undefined && item.selectedBatch.batchId!=undefined)){
		var cookieObject = $cookies.getObject('cookieObject');
		if (cookieObject == undefined) {return null;} else if (cookieObject.autoPrescribedQty != null && cookieObject.autoPrescribedQty == true) {
			if(qtyPrescribed!=undefined && parseInt(qtyPrescribed)>0)
				{
				   if(parseInt(availableqty)>=parseInt(qtyPrescribed))
					{
					   return parseInt(qtyPrescribed);
					}
				   else{
					   return parseInt(availableqty);
				   }
				}
			else{
				return null;
			}
		} else {
		    return null;
		}
	}
	else{
		return item.quantity;
	}
	
}

var calculateRate = function(uom, uomTypeId, sellingPrice){
  var conversion = 1;
  var flag = false;

  angular.forEach(uom, function (value, key) {
    if (value.uomTypeId == uomTypeId || flag) {
      conversion = conversion * value.conversion;
      flag = true;
    }
  });

  return conversion * sellingPrice;
}

var calculateLeaseQuantity = function(uom, uomTypeId, quantity){
  var conversion = 1;
  var flag = false;

  angular.forEach(uom, function (value, key) {
    if (value.uomTypeId == uomTypeId || flag) {
      conversion = conversion * value.conversion;
      flag = true;
    }
  });

  return conversion * quantity;
}

var fixedDecimal = function(amount){
  //Fixed amount to 2 decimal places
  if(amount != undefined || amount != null){
    return Number(parseFloat(amount.toString()).toFixed(2));
  }
  else{ 
    return Number(parseFloat(amount));
  }
}

var addSingleDigitMonthToZero = function(date){
    var expiryDate = moment(date).format('YYYY-MM-DD');
    var expiry = expiryDate.split("-");
    var y = expiry[0];
    var m = expiry[1];
    var d = expiry[2];

    m = parseInt(m);
    if(m < 10){
        m = "0"+ m;
    }

    expiry = y + "-" + m + "-" + d;
    return expiry;
}

  return {
    setDataId: setDataId,
    getDataId: getDataId,
    setDataFlag:setDataFlag,
    getDataFlag:getDataFlag,
    setManufacturer: setManufacturer ,
    getManufacturerName: getManufacturerName,
    getManufacturerId: getManufacturerId,
    getItemRequestId: getItemRequestId,
    setDataIdForBedManagment: setDataIdForBedManagment,
    getUHIDForBedManagment: getUHIDForBedManagment,
    getAdmissionIdForBedManagment: getAdmissionIdForBedManagment,
    setDataIdForBedManagmentPatientIssueAcceptance: setDataIdForBedManagmentPatientIssueAcceptance,
    getDataIdForBedManagmentPatientIssueAcceptanceVisitType: getDataIdForBedManagmentPatientIssueAcceptanceVisitType,
    getDataIdForBedManagmentPatientIssueAcceptancePatientId: getDataIdForBedManagmentPatientIssueAcceptancePatientId,
    getDataIdForBedManagmentPatientIssueAcceptanceUhidNum: getDataIdForBedManagmentPatientIssueAcceptanceUhidNum,
    getDataIdForBedManagmentPatientIssueAcceptanceWardId: getDataIdForBedManagmentPatientIssueAcceptanceWardId,
    getDataIdForBedManagmentPatientIssueAcceptanceWardName: getDataIdForBedManagmentPatientIssueAcceptanceWardName,
    getDataIdForBedManagmentPatientIssueAcceptanceFloorId: getDataIdForBedManagmentPatientIssueAcceptanceFloorId,
    getDataIdForBedManagmentPatientIssueAcceptanceDoctorId: getDataIdForBedManagmentPatientIssueAcceptanceDoctorId,
    getDataIdForBedManagmentPatientIssueAcceptanceDoctorFullName: getDataIdForBedManagmentPatientIssueAcceptanceDoctorFullName,
    getDataIdForBedManagmentPatientIssueAcceptancePatientName: getDataIdForBedManagmentPatientIssueAcceptancePatientName,
    setDataIdForBedManagmentSalesRetun : setDataIdForBedManagmentSalesRetun,
    getDataIdForBedManagmentSalesRetunPatienId: getDataIdForBedManagmentSalesRetunPatienId,
    getDataIdForBedManagmentSalesRetunPatientName: getDataIdForBedManagmentSalesRetunPatientName,
    getDataIdForBedManagmentSalesRetunUHID: getDataIdForBedManagmentSalesRetunUHID,
    calculateAmount: calculateAmount,
    calculateDisAmount :calculateDisAmount ,
    calculateRate: calculateRate,
    calculateLeaseQuantity: calculateLeaseQuantity,
    addSingleDigitMonthToZero: addSingleDigitMonthToZero,
    fixedDecimal: fixedDecimal,
    getDefaultBatchFromConfig: getDefaultBatchFromConfig,
    getDefaultQuantity:getDefaultQuantity,
    setIsFromApprovalEdit:setIsFromApprovalEdit,
    getIsFromApprovalEdit:getIsFromApprovalEdit
  };
}]);