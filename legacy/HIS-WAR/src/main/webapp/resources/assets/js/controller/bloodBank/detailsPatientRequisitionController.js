angular
		.module('myApp')
		.controller(
				'detailsPatientRequisitionController',
				[
						'$scope',
						'$rootScope',
						'$state',
						'$http',
						'GenericService',
						'$sessionStorage',
						'$cookies',
						'growl',
						'$localStorage',
						'BillingGenericService',
						'$stateParams',
						function($scope, $rootScope, $state, $http,
								GenericService,$sessionStorage,$cookies, growl, $localStorage,BillingGenericService,$stateParams) {
							
							var cookieObject = $cookies.getObject('cookieObject');
							if(cookieObject == undefined){
								$state.go('login');
								return;
							}
							
							$scope.commonObj={
									
									"fromDate":"",
									"toDate":""
								}; 
							
							$rootScope.loginpage = true;
							$scope.unitId = cookieObject.unitId;
							$scope.organizationId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							
							$scope.commonObj.toDate=new Date();
							$scope.commonObj.fromDate=new Date();
							
							$scope.openFromDatePicker = function() {
								$scope.fromDatepicker.opened = true;
							};

							$scope.fromDatepicker = {
									opened : false
							};
				
							$scope.openToDatePicker = function() {
								$scope.toDatepicker.opened = true;
							};

							$scope.toDatepicker = {
									opened : false
							};
						
							//search  
							$scope.common = $stateParams.patientRequisitionDetailObj;
							console.log("common",$scope.common);
							
							//get componentList
							
							$scope.componentObj = {
									bloodComponentOrderId:"",
									bloodComponentDesc:"",
									requisitionDate:"",
									bloodUnit:"",
									isRequired:"",
									bloodComponentId:"",
									requisitionNumber:""
							};
							
							$scope.detailsObj ={
									doctorId:"",
									requisitionTypeId:"",
									hospitalId:"",
									hospitalName:"",
									requiredBloodGroupId:"",
									diagnosis:"",
									patientBloodGroupId:"",
									remark:"",
									hospitalId:"",
									requisitionStatus:false,
									requisitionDate:"",
									issueDate:""
									
							};
							
						//	$scope.detailsObj.issueDate=new Date();
							
							$scope.detailsObj.issueDate=$scope.common.issueDate;
							
							
							$scope.openFromDatePicker = function() {
								$scope.fromDatepicker.opened = true;
							};

							$scope.fromDatepicker = {
									opened : false
							};

							
							if($scope.common.bloodComponentOrderId!=null ||$scope.common.bloodComponentOrderId!=undefined){
								$rootScope.startLoader();
							$scope.componentObj.bloodComponentOrderId = $scope.common.bloodComponentOrderId ;
							$scope.componentObj.bloodComponentDesc = $scope.common.bloodComponentDesc ;
							$scope.componentObj.requisitionDate = $scope.common.requisitionDate ;
							$scope.componentObj.bloodUnit = $scope.common.unit ;
							$scope.componentObj.isRequired = $scope.common.isRequired ;
							$scope.componentObj.bloodComponentId = $scope.common.bloodComponentId ;
							$scope.componentObj.requisitionNumber = $scope.common.requisitionNumber ;
							
							$scope.detailsObj.doctorId = $scope.common.doctorId;
							$scope.detailsObj.requisitionDate = $scope.common.requisitionDate;
							$scope.detailsObj.doctorName = $scope.common.doctorName;
							
							$scope.patientDetails.patientName = $scope.common.patientName;
							$scope.patientDetails.uhIdNumber = $scope.common.uhidNumber;
						//	$scope.patientDetails.genderCode = $scope.common.genderCode;
							$scope.patientDetails.age = $scope.common.age;
							$scope.patientDetails.birthDate = $scope.common.dob;
							$scope.patientDetails.admissionId = $scope.common.admissionId;
							
							$rootScope.stopLoader();
							}
							
							
							$scope.bloodGroupList = [];
							$scope.getBloodGroupList = function(){
								$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL+ "/global/bloodBank/getActiveBloodGroupMaster/"+$scope.organizationId +"/"+ $scope.unitId;;
									GenericService
									.serviceAction(METHOD_GET, URI )
									.then(
											function(response) {

												if (response.data.status == "success") {
													$rootScope.stopLoader();
													$scope.bloodGroupList = response.data.listObject;
													console.log("bloodGroupList ",response.data.listObject);
												}
											});
							}
							$scope.getBloodGroupList();
							$scope.bloodBagStockList = [];
							$scope.getBloodBagStockList = function(){
								
								$rootScope.startLoader();
								var data={
										unitId:$scope.unitId,
										organizationId:$scope.organizationId,
						    			bloodComponentId:$scope.common.bloodComponentId
								};
								var URI = BASE_URL + ROOT_URL+ "/api/bloodbank/stockDetails/getBloodBankStockByComponentId";
									GenericService
									.serviceAction(METHOD_POST,URI,data)
									.then(
											function(response) {

												if (response.data.status == "success") {
													$rootScope.stopLoader();
													$scope.bloodBagStockList = response.data.listObject;
													console.log("bloodBagStockList ",response.data.listObject);
												}
											});
							}
							
							$scope.getBloodBagStockList();
							 
							$scope.organizationMasterList = [];
							  $scope.getOrganizationList =function(){
							    	try{
							    		var data ="";
							    		var URI = BASE_URL + ROOT_URL +"/organization/active";
							    		console.log("URI",URI);
							    		GenericService.serviceAction("GET",URI,data)
							    		.then(
							    				function(response){
							    					$scope.organizationMasterList = response.data.listObject;
							    					
							    					console.log("$scope.organizationMasterList",$scope.organizationMasterList);
							    				})
							    	}catch(e){
							    		console.log("Exception",e);
							    	}
							    }
							    $scope.getOrganizationList();
							
				$scope.closeRequisition = function()
				{
					$scope.detailsObj.requisitionStatus=true;
					
					var data={
							isRequisitionClosed:$scope.detailsObj.requisitionStatus,
							bloodComponentOrderId:$scope.common.bloodComponentOrderId,
			    			updatedBy:$scope.userId,
			    			updatedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss')
					};
					
					var URI = BASE_URL + ROOT_URL +"/bloodBank/closePatientRequisition";
		    		GenericService.serviceAction(METHOD_POST,URI,data)
		    		.then(function(response){
		    			if (response.data.status == 'success')
						{	
		    				$rootScope.stopLoader();
							growl.success(response.data.message,
									{
										title : 'Success!'
									});
							$scope.common={};
							$scope.detailsObj={};
							$scope.componentObj = {};
							$scope.patientDetails = {};
						} else if(response.data.status == 'error')
						{
							$rootScope.stopLoader();
							growl.error(response.data.message,
									{
										title : 'Error!'
									});
						}
		    		})
				}
				
				$scope.stillOnRegistration = function(){
				
					$('#closeRequisitionPopup').hide('hide');
					$scope.detailsObj.requisitionStatus=false;
				}
				$scope.saveRequisitionDetails = function()
				{
					try{
						$rootScope.startLoader();
						if($scope.detailsObj.requisitionStatus)
						{
							$rootScope.stopLoader();
							growl.warning("Requisition is Closed",
									{
										title : 'warning!'
									});
						}else if($scope.detailsObj.requisitionTypeId=="" || $scope.detailsObj.requisitionTypeId==undefined){
							$rootScope.stopLoader();
							growl.warning("Please Select Requisition Type",
									{
										title : 'warning!'
									});
						}
						else if($scope.detailsObj.hospitalId=="" || $scope.detailsObj.hospitalId==undefined){
							$rootScope.stopLoader();
							growl.warning("Please Select Hospital",
									{
										title : 'warning!'
									});
						}
						else if($scope.detailsObj.requiredBloodGroupId=="" || $scope.detailsObj.requiredBloodGroupId==undefined){
							$rootScope.stopLoader();
							growl.warning("Please Select Required Blood Group",
									{
										title : 'warning!'
									});
						}
						/*else if($scope.detailsObj.diagnosis=="" || $scope.detailsObj.diagnosis==undefined){
							$rootScope.stopLoader();
							growl.warning("Please Enter Diagnosis",
									{
										title : 'warning!'
									});
						}*/
						else if($scope.detailsObj.patientBloodGroupId=="" || $scope.detailsObj.patientBloodGroupId==undefined){
							$rootScope.stopLoader();
							growl.warning("Please Select Patient Blood Group",
									{
										title : 'warning!'
									});
						}
						else{
			    		var data ={
			    			doctorId:$scope.detailsObj.doctorId,
			    			requisitionTypeId:$scope.detailsObj.requisitionTypeId,
			    			hospitalId:$scope.detailsObj.hospitalId,
			    			requiredBloodGroupId:$scope.detailsObj.requiredBloodGroupId,
			    			//diagnosis:$scope.detailsObj.diagnosis,
			    			patientBloodGroupId:$scope.detailsObj.patientBloodGroupId,
			    			remark:$scope.detailsObj.remark,
			    			issueDate:$scope.common.issueDate+" 00:00:00",
			    			requisitionDate:$scope.common.requisitionDate+" 00:00:00",
			    			bloodComponentOrderId:$scope.common.bloodComponentOrderId,
			    			bloodUnit:$scope.common.unit,
			    			componentId:$scope.common.bloodComponentId,
			    			patientId:$scope.common.patientId,
			    			admissionId:$scope.common.admissionId,
			    			createdBy:$scope.userId,
			    			createdDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
			    			updatedBy:$scope.userId,
			    			updatedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
			    			status:'A',
			    			unitId:$scope.unitId,
			    			organizationId:$scope.organizationId
			    		};
			    		var URI = BASE_URL + ROOT_URL +"/global/api/bloodBank/savePatientRequisitionDetails";
			    		GenericService.serviceAction(METHOD_POST,URI,data)
			    		.then(function(response){
			    			if (response.data.status == 'success')
							{	
			    				$rootScope.stopLoader();
								growl.success(response.data.message,
										{
											title : 'Success!'
										});
								$scope.common={};
								$scope.detailsObj={};
								$scope.componentObj = {};
								$scope.patientDetails = {};
							} else if(response.data.status == 'error')
							{
								$rootScope.stopLoader();
								growl.error(response.data.message,
										{
											title : 'Error!'
										});
							}
			    		})
						}
			    	}catch(e){
			    		console.log("Exception",e);
			    	}
				}
}]);			
					