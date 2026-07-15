/**
 * @author Suraj
 * 	Controller to handle Upadate PayeeInformation Controller
 */

angular.module("myApp").controller("chargeConversionController", [ '$scope','$rootScope','$cookies','$state','BillingGenericService','GenericService','$stateParams','$timeout','growl','paymentCollFactory','GenericServiceParamHeader',function($scope,$rootScope,$cookies,$state,BillingGenericService,GenericService,$stateParams,$timeout,growl,paymentCollFactory,GenericServiceParamHeader)
	{
		
	var cookieObject = $cookies.getObject('cookieObject');
	if (cookieObject == undefined) {
		$state.go('login');
		return;
	}
	
	$scope.init = function() {
		
		
	$scope.unitId = cookieObject.unitId;
	$scope.organizationId = cookieObject.organizationId;
	$scope.userId = cookieObject.userId;
	$scope.visitTypeId=1;
	$rootScope.loginpage = true;
	$scope.currentDate = new Date();
	$scope.patientList  = [];
    $scope.listChargeConversionDetailsDto = [];
	$scope.patientDetailsList = [];
	$scope.patientDetails = {};
	$scope.patientSearchObj = {
		      "uhIdNumber"      : "",
		      "patientName"     : "",
		      "genderId"        : "",
		      "mobileNo"        : "",
		      "birthDate"       : "",
		      "identificationNo":"",
		      "patientType"   :"1",
		      "unitId"          :$scope.unitId,
		      "organizationId"  :$scope.organizationId
		    }
	
	$scope.refundCNSearchObj = 
	    {
			  "fromDate" 	: "",
			  "toDate" 		: "",
			  "patientName" : "",
			  "uhidNumber"  : "",
			  "visitTypeId" : "",
			  "depositTypeId" : "",
			  "orgId" 		: $scope.organizationId,
			  "unitId" 		: $scope.unitId,
			  "refundCnNo"  : "",
			  "paymentRefundVoucherNo" :""	  
		}
	
	$scope.transferToActiveDeposit = "N";
	
    }
	
	$scope.init();
	
	
	$scope.searchPatient = function(patientSearchObj)
	{
		
		$rootScope.startLoader();
		$scope.patientSearchObj.unitId = cookieObject.unitId;
		$scope.patientSearchObj.organizationId = cookieObject.organizationId;
		if(patientSearchObj.patientName!="" && patientSearchObj.patientName.length < 3)
		{
			growl.warning("Please enter atleast 3 characters..!",{title : "WARNING!"});
			$scope.patientList = [];
			$rootScope.stopLoader();
			return false;
		}	
		var URI = BASE_URL + ROOT_URL + "/adt/patientSearchByMultipleCriteria";
		var patSearchObj = angular.copy($scope.patientSearchObj);
		if($scope.patientSearchObj.birthDate != "" && typeof($scope.patientSearchObj.birthDate) != "undefined"){
			var dt = new Date($scope.patientSearchObj.birthDate);
//			patSearchObj.birthDate = dt.getDate() + "-" + (dt.getMonth() + 1) + "-" + dt.getFullYear();
			patSearchObj.birthDate = dt.getFullYear() + "-" +(dt.getMonth() + 1)+ "-" +dt.getDate();
		} 
		BillingGenericService.serviceAction(METHOD_POST,URI, patSearchObj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
			$scope.searchPatientList = [];
			if (response.data.status == "success")
				$scope.patientList = response.data.listObject;
			
			 if(response.data.listObject==null){
				growl.warning(response.data.message,{
					title : ERROR_MSG
				});
				$scope.patientSearchObj.birthDate="";
				$scope.patientList=[];
				return false;
			}
			 
			 $scope.patientSearchObj.birthDate="";
			patSearchObj = {};
		});
		$rootScope.stopLoader();
	}
	
	$scope.getPatientDetailsById = function(patientId){
		$rootScope.startLoader();
		$scope.patientDetailsList = [];
		$scope.chargeConversionListByCurrentPayer=[];
		$scope.patientDetails = {}
		var URI = BASE_URL + ROOT_URL + "/api/global/patient/billing/"+patientId;
		BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
			if (response.data.status == "success"){
				$scope.patientDetailsList = response.data.listObject;
				$scope.patientDetails = response.data.listObject[0];
				$scope.deposit.patientId = $scope.patientDetails.patientId;
			}
		});
		$rootScope.stopLoader();
	}
	
	  $scope.getVisitTypeList = function() {
		  $scope.chargeConversionListByCurrentPayer=[];
		  $scope.chargeConversionListByCurrentPayer=[];
		  var param = {organizationId: cookieObject.organizationId,unitId:$scope.unitId};
		    var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/getVisitTypeList"
		    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				if (response.data.status == "success"){
					 $scope.visityTypeList = response.data.listObject;
				}
			});
		  }
		$scope.getVisitTypeList();
	
	
	$scope.getPayeeInfoByVisitType = function(){
		if($scope.patientDetails.patientId== undefined ||$scope.patientDetails.patientId == ''){
			growl.error("Please Select Patient ",{
    			title : ERROR_MSG
    		});
			return;
		}else{
			$rootScope.startLoader();
			var param={
					"patientId": $scope.patientDetails.patientId,
					"visitTypeId":$scope.visitTypeId,
			}
			var URI = BASE_URL + ROOT_URL + "/api/billing/getPayeeInfoByVisitType";
			BillingGenericService.serviceAction("POST",URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				if (response.data.status == "success"){
					$scope.payeeInfoByVisitTypeData =  response.data.listObject[0];
					if(response.data.listObject!=null){
						 $scope.paymentEntitleMentList=[];
						 angular.forEach(response.data.listObject, function (value, key) {
						   
								  var newArr = {};
								  newArr.paymentEntitlementName=value.paymentEntitlementDesc;
								  newArr.paymentEntitlementId=value.paymentEntitlementId;
								  newArr.patientSponsorDetailsId=value.patientSponsorDetailsId;
								  newArr.companyName = value.companyName;
								  newArr.companyId =value.companyId;
								  newArr.tariffName = value.tariffDesc;
								  newArr.contractName = value.companyId == 1 ? "Self triff" :value.contractName;
								  newArr.insuranceName = "";
								  newArr.insuranceNumber = "";
								  newArr.startDate = value.validityFrom;
								  newArr.endDate = value.validityTo;
								  newArr.priority =  value.priorityId;
								  newArr.tariffId = value.tariffId;
								  newArr.encounterNo=value.encounterNo;
								  newArr.encounterId=value.encounterId;
								  newArr.contractId=value.contractId;
								  newArr.payeeId=value.payeeId;
								  $scope.paymentEntitleMentList.push(newArr);
							  
						  });
						 
						
					}
					 
					

				}
			});
			$rootScope.stopLoader();
		}
		
	}
	
	$scope.getChargeConversionListByCurrentPayer = function(payeeId) {
		  var param ={
						  payeeId:payeeId,
						  encounterId:$scope.payeeInfoByVisitTypeData.encounterId
					  };
		    var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/getChargeConversionListByCurrentPayer"
		    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				if (response.data.status == "success"){
					 $scope.chargeConversionListByCurrentPayer = response.data.listObject;
					 
					 var serviceListIds = [];
						angular.forEach($scope.chargeConversionListByCurrentPayer,function(value,index){
							serviceListIds.push(value.serviceId);
						});
				}
			});
		  }
	//Check Group
	  var isServiceIdExists;
		function checkServiceIdExistsOrNot(arr, val) {
			return arr
					.some(function(arr) {
						return (val.serviceId === arr.serviceId);
					});
		}
	
	$scope.getChargeConversionListByConvertPayer = function(contractId) {
          if(contractId == "Self triff"){
        	  contractId=1;
          }
		var serviceListIds = [];
		angular.forEach($scope.chargeConversionListByCurrentPayer,function(value,index){
			serviceListIds.push(value.serviceId);
		});
		  var param ={
					  contractId:contractId,
					  serviceListIds:serviceListIds
		             };
		    var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/getChargeConversionListByConvertPayer"
		    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				if (response.data.status == "success"){
					 $scope.chargeConversionListByConvertPayer = response.data.listObject;
					 
						angular.forEach($scope.chargeConversionListByCurrentPayer,function(value,index){
							angular.forEach($scope.chargeConversionListByConvertPayer,function(inVal,inIndex){
								if(value.serviceId == inVal.serviceId)
									value.convertAmt = inVal.serviceAmount;
							});
							if(value.convertAmt == '' || typeof(value.convertAmt) == "undefined")
								value.convertAmt = '-';
							
							//console.log("$scope.chargeConversionListByCurrentPayer",JSON.stringify($scope.chargeConversionListByCurrentPayer));
						});
				}
				
				$scope.calculateAmounts();
			});
		  }
	
	
	//$scope.getChargeConversionListByCurrentPayer();
	
	
	 $scope.calculateAmounts = function(){
			var currentAmt=0;
			var convertAmt=0;
			var diffAmt=0;
		    
			angular.forEach($scope.chargeConversionListByCurrentPayer,function(value,index){
				currentAmt = parseFloat(currentAmt) + (isNaN(value.netAmount) ? 0 : parseFloat(value.netAmount));
				convertAmt = parseFloat(convertAmt) + (isNaN(value.convertAmt) ? 0 : parseFloat(value.convertAmt));

			});
			diffAmt = (isNaN(currentAmt) ? 0 : parseFloat(currentAmt)) - (isNaN(convertAmt) ? 0 : parseFloat(convertAmt));
			$scope.currentAmtTotal = $rootScope.parseFloatNum(currentAmt);
			$scope.convertAmtTotal = $rootScope.parseFloatNum(convertAmt);
			$scope.diffAmtTotal = $rootScope.parseFloatNum(diffAmt);
		
	  }
	 
	 $scope.saveChargeConversionDtls = function(){
		 
		    $scope.listChargeConversionDetailsDto = [];
			$scope.chargeConversionMasterDto={
					"conversionMasterId":  "",
					"patinetId":  $scope.patientDetails.patientId,
					"visitTypeId":  $scope.visitTypeId,
					"encounterId":   $scope.payeeInfoByVisitTypeData.encounterId,
					"admissionId":  "",
					"fromPayeeId": $scope.payeeId,
					"fromContractId": $scope.contractId,
					"toPayeeId":   $scope.payeeId,
					"toContractId":  $scope.contractId,
					"remarks":  "",
					"orgUnitId": cookieObject.unitId,
					"orgId": cookieObject.organizationId,
					"status":  "A",
					"createdDate":  "",
					"createdBy":  "",
					"updatedBy":  "",
					"updatedDate":  ""
			}
			
			$scope.chargeConversionDtlsDto={
				"conversionDetailsId":  "",
				"conversionMasterId":  "",
				"orderDetailsId":  "",
				"batchId":  "",
				"bedId":  "",
				"billingClassId":  "",
				"drugId":  "",
				"isDrug":  "",
				"isOutsourced":  "",
				"netAmount":  "",
				"oldOrdDtlId":  "",
				"ordCancelReasonId":  "",
				"ordCancelled":  "",
				"ordConcession":  "",
				"ordDiscount":  "",
				"ordDocId":  "",
				"ordRemarks":  "",
				"orderEmergencyFlag":  "",
				"orderQty":  "",
				"packageId":  "",
				"priorityId":  "",
				"roomId":  "",
				"serviceAmt":  "",
				"serviceBillId":  "",
				"serviceChargeable":  "",
				"serviceId":  "",
				"serviceIsBilled":  "",
				"serviceRendered":  "",
				"serviceRenderingDeptid":  "",
				"tariffId":  "",
				"wardId":  "",
				"orgUnitId":  "",
				"orgId":  "",
				"createdBy":  "",
				"updatedBy":  ""
			}
		   angular.forEach($scope.chargeConversionListByCurrentPayer,function(value,index){
			   var chargeConversionDtlsDto = angular.copy($scope.chargeConversionDtlsDto);
			   
			   chargeConversionDtlsDto.orderDetailsId =value.orderDetailsId;
			   chargeConversionDtlsDto.convertAmt=value.convertAmt =='-' ? value.serviceAmount:value.convertAmt;
			   $scope.listChargeConversionDetailsDto.push(chargeConversionDtlsDto);
			   
			});
			$scope.$broadcast('show-errors-check-validity');
			  var param = {
						  orgId:cookieObject.unitId,
						  unitId:cookieObject.organizationId,
						  chargeConversionMasterDto:$scope.chargeConversionMasterDto,
						  listChargeConversionDetailsDto:$scope.listChargeConversionDetailsDto
					  };
			  console.log("param",JSON.stringify(param));
			  
			  if ($scope.chargeConversionForm.$valid) {
					if ($scope.popUpFlag) {
						$scope.showModal = !$scope.showModal;

					} else {
			  var URI = BASE_URL + ROOT_URL + "/unit/ChargeConversion/saveChargeConversion"
			    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					if (response.data.status == "success"){
						$scope.init();
					}
					$scope.$broadcast('show-errors-reset');
	            });
			}
			  }
	 }	
}]);
	