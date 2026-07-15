angular.module("myApp").controller("editViewPreAuthorizationRequestController", [ '$scope','$rootScope','BillingGenericService','$cookies','$state','growl','servicesFactory','itemFactory','dataService','GenericServiceFileUpload','$timeout',
	function($scope,$rootScope,BillingGenericService,$cookies,$state,growl,servicesFactory,itemFactory,dataService,GenericServiceFileUpload,$timeout){
	try{
		$rootScope.loginpage = true;
		
		var cookieObject = $cookies.getObject('cookieObject');
		if (cookieObject == undefined) {
			$state.go('login');
			return;
		}
		
		var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
		if(cookieObjectAppConfig == undefined){
			$state.go('applicationConfiguration');
			return;
		}
		else
		{
			console.log('cookieObjectAppConfig',cookieObjectAppConfig);
			
		}
		
		$scope.defaultSelfTariffId = cookieObjectAppConfig.defaultSelfTariffId;
		$scope.isPreOrPostBilling = cookieObjectAppConfig.isPreOrPostBilling;
		$scope.opdBillingCatId = cookieObjectAppConfig.opdBillingCatId;
		$scope.pharmacyServiceId = cookieObjectAppConfig.pharmacyServiceId;
		$scope.unitId = cookieObject.unitId;
		$scope.orgId = cookieObject.organizationId;
		$scope.userId = cookieObject.userId;
		$scope.selfPayeeId = cookieObjectAppConfig.selfPayeeId;
		
		$scope.init = function(){
			$scope.visitTypeId = dataService.preAuthReqObjWithData.visitTypeId;
			if(dataService.preAuthReqObjWithData.visitTypeId == 1){
				$scope.encounterId = dataService.preAuthReqObjWithData.admissionId;
			}else{
				$scope.admissionId = dataService.preAuthReqObjWithData.admissionId;
			}
			
			$scope.preAuthReqObjWithData=dataService.preAuthReqObjWithData;
			$scope.getSendThroughList();
			$scope.getRequestTypeList();
			$scope.getAuthorizationStatusList();
			$scope.addNewRequestList=[];
			$scope.newAuthorizObj = {};
			$scope.encounterObj ={};
			$scope.patientDetails = {"uhIdNumber":$scope.preAuthReqObjWithData.uhidNumber,"patientName":$scope.preAuthReqObjWithData.patientName,
					 				"birthDate":$scope.preAuthReqObjWithData.dob,"genderCode":$scope.preAuthReqObjWithData.genderCode,
					 				"patientDischargeDate":$scope.preAuthReqObjWithData.dischargeDate,"dateOfAdmission":"",
					 				"bedNumber":$scope.preAuthReqObjWithData.bedNo,"doctorFullName":$scope.preAuthReqObjWithData.doctorName,
					 				"age":$scope.preAuthReqObjWithData.age,"visitTypeId":$scope.preAuthReqObjWithData.visitTypeId,"los":"",
					 				"patientProfile":$scope.preAuthReqObjWithData.imagePath,"genderId":$scope.preAuthReqObjWithData.genderId,"patientId":$scope.preAuthReqObjWithData.patientId
			   }
			$rootScope.allergydata($scope.preAuthReqObjWithData);
			if($scope.preAuthReqObjWithData.visitTypeId != 1){
				$scope.patientDetails.dateOfAdmission=$scope.preAuthReqObjWithData.visitDate;
				var dateDoa = moment($scope.patientDetails.dateOfAdmission, 'DD/MM/YYYY');
				var datePdd = moment($scope.patientDetails.patientDischargeDate, 'DD/MM/YYYY');
				$scope.patientDetails.los = $rootScope.dateDiffrence(dateDoa,datePdd);
		    }
			
			$scope.encounterDetails = {
					  encounterDocId : "",
					  encounterId : "",
					  patientId : "",
					  visitTypeId : "",
					  preauthRequestId : "",
					  admissionId : "",
					  documentPath : "",
					  documentTypeId : "",
					  documentName : "",
					  status : 'A',
					  createdDate : "",
					  createdBy : $scope.userId,
					  updatedDate : "",
					  updatedBy : $scope.userId,
					  unitId : $scope.unitId,
					  organisationId : $scope.orgId,
					  file : {}
					}
			
			$scope.addNewRequestList=dataService.addNewRequestListByPayeeId;
			if($scope.addNewRequestList.length > 0){
				$scope.newRequestCalculates();
			}
			
			
			$scope.initRequestList = function() 
			{
				
				$rootScope.startLoader();
			    var param = {
			    		"payeeId":(($scope.preAuthReqObjWithData.payeeId == undefined ||$scope.preAuthReqObjWithData.payeeId == null)?$scope.preAuthReqObjWithData.companyId:$scope.preAuthReqObjWithData.payeeId),
			    		"organisationId":$scope.orgId,
			    		"unitId":$scope.unitId,
			    		"patientId":$scope.preAuthReqObjWithData.patientId, 
			    		};
			    if($scope.visitTypeId!=1)
			    	param.admissionId = $scope.admissionId;  
			    else
			    	param.encounterId = $scope.encounterId;
			    
			    var URI = BASE_URL + ROOT_URL + "/api/ipd/getPreAuthorizationRequestListByPayeeId"
			   return BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					if (response.data.status == "success"){
						 $scope.addNewRequestList = response.data.listObject;
						 $rootScope.stopLoader();
						return true;
					}
					
				});
			}
			$scope.initRequestList();
			
			
		}
		
		
		
		$scope.getSendThroughList = function() {
			try {
				var data = "";
				var URI = BASE_URL + ROOT_URL
						+ LIS_COMMON +GET_SEND_THROUGH_LIST; 
				BillingGenericService.serviceAction("GET", URI, data).then(function(response){
					$scope.sendThroughtList = response.data.listObject;
					$rootScope.stopLoader();
				});
			   } catch (e) {
				console.log(e.message)
			}
		}
		
		$scope.getRequestTypeList = function() {
			try {
				var data = "";
				var URI = BASE_URL + ROOT_URL
						+ LIS_COMMON +GET_REQUEST_TYPE_LIST; 
				BillingGenericService.serviceAction("GET", URI, data).then(function(response){
					$scope.requestTypeList = response.data.listObject;
					$rootScope.stopLoader();
				});
			   } catch (e) {
				console.log(e.message)
			}
		}
		
		
		$scope.getAuthorizationStatusList = function() {
			try {
				var data = "";
				var URI = BASE_URL + ROOT_URL
						+ LIS_COMMON +GET_AUTH_STATUS_LIST; 
				BillingGenericService.serviceAction("GET", URI, data).then(function(response){
					$scope.authorizationStatusList = response.data.listObject;
					$rootScope.stopLoader();
				});
			   } catch (e) {
				console.log(e.message)
			}
		}
		
		
		  $scope.requestTypeName = function(id){
				for(var i = 0; i < $scope.requestTypeList.length ; i++){
					if($scope.requestTypeList[i].id == id)
						return $scope.requestTypeList[i].name;
				}
		   }
		  
		  $scope.requestSendThroughName= function(id){
				for(var i = 0; i < $scope.sendThroughtList.length ; i++){
					if($scope.sendThroughtList[i].id == id)
						return $scope.sendThroughtList[i].name;
				}
		   }
		  
		  $scope.authorizationStatusName= function(id){
				for(var i = 0; i < $scope.authorizationStatusList.length ; i++){
					if($scope.authorizationStatusList[i].id == id)
						return $scope.authorizationStatusList[i].name;
				}
		   }
		  
		  function requestNo() {
			  var text = "";
			  var possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

			  for (var i = 0; i < 5; i++)
			    text += possible.charAt(Math.floor(Math.random() * possible.length));

			  return text;
			}
		
		
		  
		  
		$scope.addNewRequest = function() {
			try {
				$scope.documentName="";
				if($scope.newReqObj.requestTypeId == '' || $scope.newReqObj.requestTypeId == null || $scope.newReqObj.requestTypeId == undefined){
					
					growl.error("Please select request type id.",{
						title : 'Warning!'
					});
					return false;
					
				}else if($scope.newReqObj.requestDate == '' || $scope.newReqObj.requestDate == null || $scope.newReqObj.requestDate == undefined){
					
					growl.error("Please select request date.",{
						title : 'Warning!'
					});
					return false;
				}
				else if($scope.newReqObj.requestSendThroughId == '' || $scope.newReqObj.requestSendThroughId == null || $scope.newReqObj.requestSendThroughId == undefined){
					
					growl.error("Please select request send through.",{
						title : 'Warning!'
					});
					return false;
				}
				else if($scope.newReqObj.requestAmount == '' || $scope.newReqObj.requestAmount == null || $scope.newReqObj.requestAmount == undefined || $scope.newReqObj.requestAmount == 0){
					
					growl.error("Please select request amount.",{
						title : 'Warning!'
					});
					return false;
				}else{
				console.log($scope.newReqObj);
				//$scope.docTypeId = $('#docId').val();
				$scope.docTypeId = $('#documentTypeIdDropdown').val();
				console.log($scope.docTypeId);
				var selectElement = document.querySelector('#documentTypeIdDropdown');				
				var selectedOption = selectElement.options[selectElement.selectedIndex];
				var selectedOptionName = selectedOption.text;
				$scope.documentName=selectedOptionName;				
				$scope.encounterDetails.documentTypeId=$scope.docTypeId;
				$scope.encounterDetails.documentName=$scope.documentName;
				var newReqObj = {preauthRequestId : "",requestNo:"",requestTypeId:"",requestTypeName:"", requestDate:"",
						    requestSendThroughId:"",requestSendThroughName:"",authorizationStatusName:"",documentName:"",docTypeId:"",
						   requestAmount:"",requestRemark:"",requestSendByName :"",isFinalSubmit:"N",receivedByName:""};
				
                var tempNewReqObj = angular.copy(newReqObj);
				
				tempNewReqObj.requestNo="Req0-"+requestNo();
				tempNewReqObj.requestTypeId=$scope.newReqObj.requestTypeId;
				tempNewReqObj.requestTypeName=$scope.requestTypeName($scope.newReqObj.requestTypeId);
				tempNewReqObj.requestDate=$scope.newReqObj.requestDate;
				tempNewReqObj.requestSendThroughId=$scope.newReqObj.requestSendThroughId;
				tempNewReqObj.requestSendThroughName=$scope.requestSendThroughName($scope.newReqObj.requestSendThroughId);
				tempNewReqObj.requestAmount=$scope.newReqObj.requestAmount;
				tempNewReqObj.requestRemark=$scope.newReqObj.requestRemark;
				//tempNewReqObj.receivedByName=cookieObject.userName;
				tempNewReqObj.requestSendByName=$scope.preAuthReqObjWithData.patientName;
				tempNewReqObj.docTypeId=$scope.docTypeId;
				tempNewReqObj.documentName=$scope.documentName;
				tempNewReqObj.authorizationStatusName=$scope.authorizationStatusName(6); //pending status
				tempNewReqObj.isFinalSubmit="N",
				$scope.newPreAuthorizationObj={
							preauthRequestId : "",
							patientId : $scope.preAuthReqObjWithData.patientId,
							visitType : $scope.visitTypeId,
							visitAdmId : "",
							payeeId : $scope.preAuthReqObjWithData.companyId,
							requestNo : tempNewReqObj.requestNo,
							requestTypeId : tempNewReqObj.requestTypeId,
							requesDate : new Date(tempNewReqObj.requestDate ).getTime(),
							requestSendBy : $scope.userId,
							requestSendThrough :tempNewReqObj.requestSendThroughId,
							requestAmount :tempNewReqObj.requestAmount,
							authorizationAmount : "",
							authorizationNo : "",
							authorizationDate : "",
							receivedThrough : "",
							requestRemark : tempNewReqObj.requestRemark,
							authRemark : "",
							//docTypeId:$scope.docTypeId,							
							//documentName:$scope.documentName,							
							receivedBy : $scope.userId,
							patientSponsorId :$scope.preAuthReqObjWithData.patientSponsorDetailsId,
							unitId : $scope.unitId,
							organisationId : $scope.orgId,
							authStatusId : "6", //pending status
							status : "A",
							isAuthDeleted : "N",
							isFinalSubmit: "N",
							createdDate : "",
							createdBy : $scope.userId ,
							updatedDate : "",
							updatedBy : $scope.userId ,
							//admissionId : $scope.preAuthReqObjWithData.admissionId,
							//encounterId : $scope.preAuthReqObjWithData.encounterId,
							contractId : $scope.preAuthReqObjWithData.contractId,
							payeeId : $scope.preAuthReqObjWithData.payeeId
						
            }
				if($scope.visitTypeId==1){
					$scope.newPreAuthorizationObj.encounterId=$scope.encounterId;
					$scope.newPreAuthorizationObj.admissionId=null;
				}else if($scope.visitTypeId == 2 || $scope.visitTypeId == 3 || $scope.visitTypeId == 4){
					$scope.newPreAuthorizationObj.encounterId=null;
					$scope.newPreAuthorizationObj.admissionId=$scope.admissionId;
				}else
					{
						growl.error("Someting went wrong..!!",{
							title : 'Error!'
						});
						return false;
					}
			$rootScope.startLoader();
			var URI = BASE_URL + ROOT_URL + "/api/ipd/saveNewPreAuthorizationRequest";
			BillingGenericService.serviceAction(METHOD_POST, URI, $scope.newPreAuthorizationObj).then(function(response){
				if (response.data.status == "success"){
					
					angular.element('#addNewRequestModal').modal('hide');
					$scope.saveNewRequestList = response.data.object;
					tempNewReqObj.preauthRequestId=	$scope.saveNewRequestList.preauthRequestId;
					tempNewReqObj.requestDate = moment(tempNewReqObj.requestDate).format('DD-MM-YYYY');
					 $scope.addNewRequestList.push(tempNewReqObj);
					 $scope.newRequestCalculates();
					 // upload Document
					 // $scope.docTypeId=16;
					  if($scope.encounterDetailsList.length >0 &&  !angular.equals($scope.encounterDetailsList[0].file, {})){
						  $scope.uploadPatientImage(tempNewReqObj.preauthRequestId,tempNewReqObj.docTypeId);
					  }
					  
					  $scope.newReqObj.requestTypeId = "";
					  $scope.newReqObj.requestDate = "";
					  $scope.newReqObj.requestSendThroughId = "";
					  $scope.newReqObj.requestAmount = "";
					  $scope.newReqObj.requestRemark = "";
					  $scope.encounterDetailsList= [];
					  $scope.patientDocumentList = [];
				}else{
					$scope.addNewRequestList=[];
				}
					
				$rootScope.stopLoader();
			});
			}
			   } catch (e) {
				console.log(e.message)
			}
		}
		
		$scope.addAuthorizationRequest = function(newReqObj,index) {
			try {
				$scope.encounterDetailsList=[];
				//alert(index + " "+newReqObj.preauthRequestId);
				$scope.newReqObj = angular.copy(newReqObj);
				
				/*var array = $scope.newReqObj.authDate.split('-');
				var newDate = "";
			    newDate = array[1]+'/'+array[0]+'/'+array[2];*/
			   
				$scope.newReqObj.authDate = new Date();
				$scope.newAuthorizObj=$scope.newReqObj;
				$scope.requestDate = $scope.newReqObj.requestDate;
				
				 $('#detailsPreAuthorizationRequestModal').modal('show');
				 var encounterDetails = angular.copy($scope.encounterDetails);
					$scope.encounterDetailsList.push(encounterDetails);
					$scope.docTypeId=$scope.encounterDetailsList[0].documentTypeId;
					//$scope.docTypeId=$scope.docTypeId;
				$scope.getPatientDocumentList($scope.newReqObj.preauthRequestId); //docTypeId 17
			   } catch (e) {
				console.log(e.message)
			}
		}
		
		$scope.newRequestCalculates = function(){
			var totalSummary=0;
			var totalAuthSummary=0;
			angular.forEach($scope.addNewRequestList,function(value,index){
				totalSummary = parseFloat(totalSummary) + (isNaN(value.requestAmount) ? 0 : parseFloat(value.requestAmount));
				if(value.authorizationAmount!=null){
				  totalAuthSummary = parseFloat(totalAuthSummary) + (isNaN(value.authorizationAmount) ? 0 : parseFloat(value.authorizationAmount));
				}
			});
			$scope.summary = $rootScope.parseFloatNum(totalSummary);
			$scope.authsummary = $rootScope.parseFloatNum(totalAuthSummary);
	  }
	
		$scope.addNewAuthorizationRequest = function() {
			try {				
				//$scope.getPatientDocumentName($scope.newReqObj.preauthRequestId);				
				var newAuthorizObj = {authorizationNo:"",authDate:"",authRequestTypeName:"", authDate:"",
						authRequestSendThroughId:"",authRequestSendThroughName:"",authorizationStatus:"",docTypeId:"",receivedByName:"",
						    authorizationAmount:"",authRemark:"",authorizationStatusName:""};
				
				var tempNewAuthorizObj = angular.copy(newAuthorizObj);
				
				tempNewAuthorizObj.authorizationNo=$scope.newAuthorizObj.authorizationNo;
				//tempNewAuthorizObj.authRequestTypeName=$scope.requestTypeName($scope.newAuthorizObj.authorizationNo);
				tempNewAuthorizObj.authDate=$scope.newAuthorizObj.authDate;
				tempNewAuthorizObj.authRequestSendThroughId=$scope.newAuthorizObj.authRequestSendThroughId;
				tempNewAuthorizObj.authRequestSendThroughName=$scope.requestSendThroughName($scope.newAuthorizObj.authRequestSendThroughId);
				tempNewAuthorizObj.authorizationAmount=$scope.newAuthorizObj.authorizationAmount;
				tempNewAuthorizObj.authRemark=$scope.newAuthorizObj.authRemark;
				tempNewAuthorizObj.receivedByName=cookieObject.userName;
				tempNewAuthorizObj.docTypeId=$scope.docTypeId;				
				tempNewAuthorizObj.authorizationStatus=$scope.newAuthorizObj.authorizationStatus;
				tempNewAuthorizObj.authorizationStatusName=$scope.authorizationStatusName($scope.newAuthorizObj.authorizationStatus); //pending status
				//$scope.saveNewRequestList.authStatusId=$scope.newAuthorizObj.authorizationStatus;
				//$scope.docTypeId = $('#documentTypeIdDropdown').val();
				$scope.docTypeId =36;				
				//$scope.docTypeId = $('#docId').val();
				$scope.newPreAuthorizationObj={
						preauthRequestId : $scope.newReqObj.preauthRequestId,
						authorizationAmount : tempNewAuthorizObj.authorizationAmount,
						authorizationNo :tempNewAuthorizObj.authorizationNo,
						authorizationDate : new Date(tempNewAuthorizObj.authDate ).getTime(),
						receivedThrough :tempNewAuthorizObj.authRequestSendThroughId,
						authRemark :tempNewAuthorizObj.authRemark,
						receivedBy : $scope.userId,
						unitId : $scope.unitId,
						organisationId : $scope.orgId,
						docTypeId:$scope.docTypeId,
						updatedDate : "",
						isFinalSubmit: "N",
						authStatusId : tempNewAuthorizObj.authorizationStatus,
						updatedBy : $scope.userId,
					
                }
			
		$rootScope.startLoader();
		var URI = BASE_URL + ROOT_URL + "/api/ipd/updateAuthorizationRequest";
		BillingGenericService.serviceAction(METHOD_POST, URI, $scope.newPreAuthorizationObj).then(function(response){
			if (response.data.status == "success")
			{
				angular.forEach($scope.addNewRequestList,function(value,index)
						{
					if(value.preauthRequestId == $scope.newReqObj.preauthRequestId){
						var obj = $scope.addNewRequestList[index];
						angular.forEach(tempNewAuthorizObj,function(value,key){
							obj[key] = value;
						});
						$scope.docTypeId=$scope.docTypeId;
						// upload Document
						if($scope.encounterDetailsList.length >0){
							$scope.uploadPatientImage(obj.preauthRequestId,$scope.docTypeId);
						  }
					

					}
				});
				 $scope.encounterDetailsList=[];
				 var encounterDetails = angular.copy($scope.encounterDetails);
					$scope.encounterDetailsList.push(encounterDetails);
					
				//$scope.uploadPatientImage($scope.newReqObj.preauthRequestId);
				//$scope.docTypeId=$scope.docTypeId;
				$scope.getPatientDocumentList($scope.newReqObj.preauthRequestId);
				$scope.initRequestList();
				$scope.newRequestCalculates();
				$('#documentTypeIdDropdown').val('');	
				$('#docId').val('');
			}
				
			$rootScope.stopLoader();
		 });
				
			
		} catch (e) {
				console.log(e.message)
			}
		}
		
		
		
		$scope.addFinalSubmitNewAuthorizationRequest = function() {
			try {
				
				var newAuthorizObj = {authorizationNo:"",authDate:"",authRequestTypeName:"", authDate:"",
						authRequestSendThroughId:"",authRequestSendThroughName:"",authorizationStatus:"",receivedByName:"",
						    authorizationAmount:"",authRemark:"",authorizationStatusName:"",isFinalSubmit:""};
				
				var tempNewAuthorizObj = angular.copy(newAuthorizObj);
				
				tempNewAuthorizObj.authorizationNo=$scope.newAuthorizObj.authorizationNo;
				//tempNewAuthorizObj.authRequestTypeName=$scope.requestTypeName($scope.newAuthorizObj.authorizationNo);
				tempNewAuthorizObj.authDate=$scope.newAuthorizObj.authDate;
				tempNewAuthorizObj.authRequestSendThroughId=$scope.newAuthorizObj.authRequestSendThroughId;
				tempNewAuthorizObj.authRequestSendThroughName=$scope.requestSendThroughName($scope.newAuthorizObj.authRequestSendThroughId);
				tempNewAuthorizObj.authorizationAmount=$scope.newAuthorizObj.authorizationAmount;
				tempNewAuthorizObj.authRemark=$scope.newAuthorizObj.authRemark;
				tempNewAuthorizObj.receivedByName=cookieObject.userName;
				tempNewAuthorizObj.authorizationStatus=$scope.newAuthorizObj.authorizationStatus;
				tempNewAuthorizObj.authorizationStatusName=$scope.authorizationStatusName($scope.newAuthorizObj.authorizationStatus); //pending status
				tempNewAuthorizObj.isFinalSubmit="Y";
				//$scope.saveNewRequestList.authStatusId=$scope.newAuthorizObj.authorizationStatus;
				
				
				$scope.newPreAuthorizationObj={
						preauthRequestId : $scope.newReqObj.preauthRequestId,
						authorizationAmount : tempNewAuthorizObj.authorizationAmount,
						authorizationNo :tempNewAuthorizObj.authorizationNo,
						authorizationDate : new Date(tempNewAuthorizObj.authDate ).getTime(),
						receivedThrough :tempNewAuthorizObj.authRequestSendThroughId,
						authRemark :tempNewAuthorizObj.authRemark,
						receivedBy : $scope.userId,
						unitId : $scope.unitId,
						organisationId : $scope.orgId,
						updatedDate : "",
						isFinalSubmit: "Y",
						authStatusId : tempNewAuthorizObj.authorizationStatus,
						updatedBy : $scope.userId,
					
                }
		
		$rootScope.startLoader();
		var URI = BASE_URL + ROOT_URL + "/api/ipd/updateAuthorizationRequest";
		BillingGenericService.serviceAction(METHOD_POST, URI, $scope.newPreAuthorizationObj).then(function(response){
			if (response.data.status == "success")
			{
				/*angular.forEach($scope.addNewRequestList,function(value,index){
					if(value.preauthRequestId == $scope.newReqObj.preauthRequestId){
						var obj = $scope.addNewRequestList[index];
						angular.forEach(tempNewAuthorizObj,function(value,key){
							obj[key] = value;
							
						});
						
						 // upload Document
						  $scope.docTypeId=17;
						  if($scope.encounterDetailsList.length >0){
								$scope.uploadPatientImage(obj.preauthRequestId);
							  }
						
					}
				});*/
				var promise=$scope.initRequestList();
				promise.then(function(response) {
					 $scope.newRequestCalculates();
				});
				
				$rootScope.stopLoader();
				if(response.data.object=='Y')
					{
					
					$scope.initRequestList();
					
					var obj = {
							organisationId : $scope.orgId,
							unitId :$scope.unitId,
							createdBy :$scope.userId,
							updatedBy :$scope.userId,
							tariffId :	$scope.defaultSelfTariffId,
							isPreBilling : $scope.isPreOrPostBilling,
							billingBedCategoryId : $scope.opdBillingCatId,
							patientId : $scope.preAuthReqObjWithData.patientId,
							pharmacyServiceId:$scope.pharmacyServiceId,
							selfPayeeId : $scope.selfPayeeId 
					}
					if($scope.visitTypeId!=1)
						obj.admissionId = $scope.admissionId;
					else
						obj.encounterId = $scope.encounterId;
					
					/*$timeout(function () {
						$rootScope.startLoader();
						var URI = BASE_URL + ROOT_URL + "/api/billing/preAuth/approval";
						BillingGenericService.serviceAction(METHOD_POST, URI, obj).then(function(response)
						{
							$scope.newRequestCalculates();
						$rootScope.stopLoader();
						});
						}, 3000);*/
					}else{
						$scope.newRequestCalculates();
						$rootScope.stopLoader();
					}
				
			}
		 });
				
			
		} catch (e) {
				console.log(e.message)
			}
		}
		
		
		
		$scope.cancelNewPreAuthorizationRequest = function(newReqDtls) {
			   $rootScope.startLoader();
			   console.log("newReqDtls",newReqDtls.isFinalSubmit);
			    var param = {
			    				"preauthRequestId":newReqDtls.preauthRequestId,
			    				"authStatusId":5,
			    				"isFinalSubmit": newReqDtls.isFinalSubmit
			    			};
			  
			    var URI = BASE_URL + ROOT_URL + "/api/ipd/cancelNewPreAuthorizationRequestByPreAuthorizationId"
			    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					if (response.data.status == "success"){
						 $scope.cancelNewPreAuthorizationResponse = response.data.listObject;
								/*for(var i = 0; i <$scope.addNewRequestList.length ; i++)
								{
									if($scope.addNewRequestList[i].preauthRequestId == newReqDtls.preauthRequestId)
									{	
										 $scope.addNewRequestList[i].authorizationStatus=5;
									     $scope.addNewRequestList[i].authorizationStatusName="Cancel";
									}
								}*/
								$scope.initRequestList();
								if(response.data.object=='Y')
								{
									var obj = {
											organisationId : $scope.orgId,
											unitId :$scope.unitId,
											createdBy :$scope.userId,
											updatedBy :$scope.userId,
											tariffId :	$scope.defaultSelfTariffId,
											isPreBilling : $scope.isPreOrPostBilling,
											billingBedCategoryId : $scope.opdBillingCatId
									}
									if($scope.visitTypeId!=1)
										obj.admissionId = $scope.admissionId;
									else
										obj.encounterId = $scope.encounterId;
									
									var URI = BASE_URL + ROOT_URL + "/api/billing/preAuth/approval";
										BillingGenericService.serviceAction(METHOD_POST, URI, obj).then(function(response)
										{
											$rootScope.stopLoader();
										});
								}else{
									$rootScope.stopLoader();
								}	
					}
					
				});
			  }
		
		
		
		$scope.checkAmountAllowedOrNot = function(authAmount,requestAmount)
		{
			if(parseFloat(authAmount) > parseFloat(requestAmount))
			{
				growl.warning("Amount cannot be greater then requested amount..!!",{
					title : 'Warning!'
				});
			$scope.newAuthorizObj.authorizationAmount = requestAmount;
			return;
			}
			if(parseFloat(authAmount) < 0 )
				{
					$scope.newAuthorizObj.authorizationAmount = 0;
				}
		}
		
		$scope.checkPositiveAmount = function(amount)
		{
			if(parseFloat(amount) < 0)
				$scope.newReqObj.requestAmount = 0;
				return;
					
		}
		
	
		
		 $scope.isCancelStatusCheck= function(id){
			for(var i = 0; i <$scope.addNewRequestList.length ; i++){
				if($scope.authorizationStatusList[i].preauthRequestId == id)
					 $scope.authorizationStatusList[i].authStatus=5;
			}
	   }
		
		 $scope.addNewDoc = function(){
			 $scope.encounterDetailsList=[];
			 $scope.newReqObj = {};
			  $scope.patientDocumentList = [];
			 $('#addNewRequestModal').modal('show');
			 $scope.docTypeId=16;
			 $scope.getPatientDocumentList(undefined); //documentTypeId 16
			 var encounterDetails = angular.copy($scope.encounterDetails);
				$scope.encounterDetailsList.push(encounterDetails);
			}
		 
		 
			
		 
			/**========================DOCUMENT UPLOAD =========================================*/
			
			$scope.getDocumentType = function(){
				$rootScope.startLoader();
				var URI = BASE_URL + ROOT_URL + GETACTIVEDOCUMENTTYPELIST;
				BillingGenericService.serviceAction(METHOD_GET, URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
						$scope.documentTypeList = [];
						if (response.data.status == "success") {
							$scope.documentTypeList = response.data.listObject;
						}
					});
			}
			$scope.getDocumentType();
			
			$scope.encounterDetailsList = [];
			
			
			
			$scope.addImageDetails = function()
			{ 
				var encounterDetails = angular.copy($scope.encounterDetails);
				$scope.encounterDetailsList.push(encounterDetails);
			};

		  $scope.removeImageDetails = function(deleteIndex)
			{ 
			   $scope.encounterDetailsList.splice(deleteIndex, 1);
		    }		
		  
		  
		  $scope.uploadEncounterDoc = function(Obj)
			{
				 var fd = new FormData();
			        fd.append('encounterId', Obj.encounterId);
			        fd.append('patientId',Obj.patientId );
			        fd.append('documentTypeId', Obj.documentTypeId);
			        fd.append('createdBy',Obj.createdBy );
			        fd.append('updatedBy', Obj.updatedBy);
			        fd.append('preauthRequestId', Obj.preauthRequestId);
			        fd.append('visitTypeId', Obj.visitTypeId);
			        fd.append('admissionId', $scope.admissionId == undefined ? "" :$scope.admissionId);
			        fd.append('unitId', Obj.unitId);
			        fd.append('organisationId', Obj.organisationId);
			        fd.append('documentName', Obj.documentName);
			        fd.append('file',Obj.file );
			        var URI1 = BASE_URL + ROOT_URL +"/api/opd/preAuthorizationRequestDoc";
			        return GenericServiceFileUpload.serviceAction("POST",URI1,fd,{ 'Content-Type': undefined}).then(function(response) 
					{	
						return response;
					});
			}
			 $scope.uploadPatientImage = function(preAuqId)
			    {
				 $scope.docTypeId= $scope.encounterDetailsList[0].documentTypeId;
				if($scope.encounterDetailsList.length == 0){
					  return; 
				 }else{
				 $rootScope.startLoader();
				  $scope.count = 0;
				 angular.forEach($scope.encounterDetailsList, function(value, key) 
					{
					 value.preauthRequestId=preAuqId;
					 value.patientId = $scope.preAuthReqObjWithData.patientId;
					 value.visitTypeId = $scope.preAuthReqObjWithData.visitTypeId;
						value.documentTypeId=value.docTypeId;
						//$scope.docTypeId=value.documentTypeId;

						var promise = $scope.uploadEncounterDoc(value);
						promise.then(function(response) 
								{
								  if(response.data.status == "success")
									  {
									    $scope.count++;
									    if($scope.encounterDetailsList.length==$scope.count)
										 {
										   growl.success("Document Uploaded Successfully.",
													{
														title : 'Success!'
													});
										  /*$scope.newReqObj = {requestTypeId:"",requestDate:new Date(),
												    requestSendThroughId:"",requestAmount:"",requestRemark:"", docTypeId :"",isFinalSubmit:"N" };*/
										   
										  
										   $rootScope.stopLoader();
										  // $scope.getPatientDocumentList(preAuqId);
										 }
								      }
								  else 
									{
										growl.error('Something went wrong.',
												{
													title : 'Error!'
												});
									}
							    });
							
					});
				
				
				   }
			    }
			 
			 
			 $scope.uploadPatientImage = function(preAuqId,docTypeId)
			    {
				 $scope.docTypeId= $scope.encounterDetailsList[0].documentTypeId;
				if($scope.encounterDetailsList.length == 0){
					  return; 
				 }else{
				 $rootScope.startLoader();
				  $scope.count = 0;
				 angular.forEach($scope.encounterDetailsList, function(value, key) 
					{
					 value.preauthRequestId=preAuqId;
					 value.patientId = $scope.preAuthReqObjWithData.patientId;
					 value.visitTypeId = $scope.preAuthReqObjWithData.visitTypeId;
					value.documentTypeId= value.docTypeId != undefined ? value.docTypeId: value.documentTypeId;
					value.documentName=value.documentName;
						//$scope.docTypeId=value.documentTypeId;

						var promise = $scope.uploadEncounterDoc(value);
						promise.then(function(response) 
								{
								  if(response.data.status == "success")
									  {
									    $scope.count++;
									    if($scope.encounterDetailsList.length==$scope.count)
										 {
										   growl.success("Document Uploaded Successfully.",
													{
														title : 'Success!'
													});
										  /*$scope.newReqObj = {requestTypeId:"",requestDate:new Date(),
												    requestSendThroughId:"",requestAmount:"",requestRemark:"", docTypeId :"",isFinalSubmit:"N" };*/
										   
										  
										   $rootScope.stopLoader();
										  // $scope.getPatientDocumentList(preAuqId);
										 }
								      }
								  else 
									{
										growl.error('Something went wrong.',
												{
													title : 'Error!'
												});
									}
							    });
							
					});
				
				
				   }
			    }
			 			 
			 $scope.getPatientDocumentName = function(preauthRequestId )
		        {
		         $rootScope.startLoader();
		         var data =
		           {
		           "preauthRequestId":preauthRequestId == undefined ? "" :preauthRequestId,
		           "patientId":$scope.preAuthReqObjWithData.patientId,
		           "unitId":$scope.unitId,
		           "organisationId":$scope.orgId
		          // "userId":$scope.userId
		           //== undefined ? 16 :$scope.docTypeId
		         }
		         var URI = BASE_URL + ROOT_URL + "/api/ipd/getPatientDocumentNameForPreAuthorization";
		         BillingGenericService.serviceAction(METHOD_POST, URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
		           $scope.patientDocumentName = [];
		           if (response.data.status == "success") 
		           {
		            
		            $scope.patientDocumentList = response.data.listObject;
		            $rootScope.stopLoader();
		           }
		           else if(response.data.status == "error")
		            {
		            $rootScope.stopLoader();
		            }
		          });
		        }
						  
			 $scope.getPatientDocumentList = function(preauthRequestId )
		        {
		         $rootScope.startLoader();
		         var data =
		           {
		           "preauthRequestId":preauthRequestId == undefined ? "" :preauthRequestId,
		           "patientId":$scope.preAuthReqObjWithData.patientId,
		           "unitId":$scope.unitId,
		           "organisationId":$scope.orgId,
		           "userId":$scope.userId,
		           "docTypeId": $scope.docTypeId 
		           //== undefined ? 16 :$scope.docTypeId
		         }
		         var URI = BASE_URL + ROOT_URL + "/api/opd/getInsuranceDocByPatientIDDetails";
		         BillingGenericService.serviceAction(METHOD_POST, URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
		           $scope.patientDocumentList = [];
		           if (response.data.status == "success") 
		           {
		            $('#encounterUploadDocumentDetailsModal').modal('show');
		            $scope.patientDocumentList = response.data.listObject;
		            $rootScope.stopLoader();
		           }
		           else if(response.data.status == "error")
		            {
		            $rootScope.stopLoader();
		            }
		          });
		        }
		        $scope.openImage = function(index)
		        { 
		         $rootScope.startLoader();
		         var imageUrl = $scope.patientDocumentList[index].documentPath;
		         var href = $rootScope.currentHrefDoccument();
		         var bucketType = $scope.patientDocumentList[index].bucketFlag;
		         if(bucketType == 1){
		         $.fancybox.open({
		                     width: 600, // or whatever
		                           height: 320,
		                     href:href+imageUrl,
		                     type: 'iframe'
		                 });
		         $rootScope.stopLoader();
		        }else{
		        	$.fancybox.open({
	                     width: 600, // or whatever
	                           height: 320,
	                     href:imageUrl,
	                     type: 'iframe'
	                 });
	         $rootScope.stopLoader();
		        }
	}
		
	   $scope.init();
			
			
		
	}catch(e){
		console.log(e.message);
	}
}]);	