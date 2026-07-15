angular
		.module('myApp')
		.controller(
				'bloodBagCollectionController',
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
						'$stateParams',
						function($scope, $rootScope, $state, $http,
								GenericService,$sessionStorage,$cookies, growl, $localStorage,$stateParams) {
							
							
							var cookieObject = $cookies.getObject('cookieObject');
							if(cookieObject == undefined){
								$state.go('login');
								return;
							}
							
							
						
							$rootScope.loginpage = true;
							$scope.saveBtnFlag = true;
							$scope.unitId = cookieObject.unitId;
							$scope.organizationId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
								
				
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
							
							// function for initializa clockpicker
							$scope.initClockPicker = function() {

								$('.clockpicker').clockpicker();
							}

							
							$scope.common = $stateParams.bloodBagCollectionResult;
							//$scope.donorDetailsObj.donorId = $scope.common.donorId;
							$scope.dId = $scope.common.donorId;
							console.log("com",$scope.common);
							$scope.donorDetailsObj = {
									"campId":"",
									"bloodBankId":"",
									"bloodGroupId":"",
									"collectionDate":"",
									"fromTime":"",
									"toTime":"",
									"collectedFrom":"",
									"bloodUnitNumber":"",
									"segmentNumber":"",
									"barCode":"",
									"bloodBagTypeId":"",
									"bagCapacityId":"",
									"batchNo":"",
									"terminationReasonId":"",
									"donorId":"",
									"autologous":$scope.common.autologous,
									"bloodBagCollectionStatusId":"",
									"createdBy":$scope.userId,
									"createdDate":moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
									"status":"A",
									"updatedBy":$scope.userId,
									"updatedDate":moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
									"organizationId": $scope.organizationId,
									"unitId": $scope.unitId,
									"examinationDetailsList":[],
							}
							
							$scope.exam = {
                                    "examinationDate": new Date(),
									"pluse":"",
									"systolic":"",
									"diastolic":"",
									"createdBy":$scope.userId,
									"createdDate":moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
									"status":"A",
									"updatedBy":$scope.userId,
									"updatedDate":moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
									"organizationId": $scope.organizationId,
									"unitId": $scope.unitId,
							}
							
							$scope.getBagCollectionDetails = function(bloodBagCollectionId)
							{
								$rootScope.startLoader();
								var data = {
										organizationId:$scope.organizationId,
										unitId:$scope.unitId,
										bloodBagCollectionId:bloodBagCollectionId
										};
								
								var URI = BASE_URL + ROOT_URL + "/api/bloodbank/getBagCollectionDetailById";
								console.log("URI", URI);
								GenericService.serviceAction("POST", URI,data).then(function(response) {
									if(response.data.status=="success"){
									$rootScope.stopLoader();
									$scope.bagCollectionObj = response.data.object;
									console.log("bagCollectionObj",$scope.bagCollectionObj);
									
									if($scope.bagCollectionObj!=null && $scope.bagCollectionObj!=undefined)
									{
										$scope.donorDetailsObj.collectedFrom = $scope.bagCollectionObj.collectedFrom;
										
										if($scope.donorDetailsObj.collectedFrom==2)
										{
											$scope.donorDetailsObj.campId = $scope.bagCollectionObj.campId;
										}else{
											$scope.donorDetailsObj.bloodBankId = $scope.bagCollectionObj.bloodBankId;
										}
										$scope.collectionDate  = new Date($scope.bagCollectionObj.collectionDate);
										$scope.donorDetailsObj.fromTime  = $scope.bagCollectionObj.fromTime;
										$scope.donorDetailsObj.toTime  = $scope.bagCollectionObj.toTime;
										$scope.donorDetailsObj.bloodUnitNumber  = $scope.bagCollectionObj.bloodUnitNumber;
										$scope.donorDetailsObj.segmentNumber  = $scope.bagCollectionObj.segmentNumber;
										$scope.donorDetailsObj.barCode  = $scope.bagCollectionObj.barCode;
										$scope.donorDetailsObj.bloodBagTypeId  = $scope.bagCollectionObj.bloodBagTypeId;
										$scope.donorDetailsObj.bagCapacityId  = $scope.bagCollectionObj.bagCapacityId;
										$scope.donorDetailsObj.batchNo  = $scope.bagCollectionObj.batchNo;
										$scope.donorDetailsObj.terminationReasonId  = $scope.bagCollectionObj.terminationReasonId;
										$scope.donorDetailsObj.bloodBagCollectionStatusId  = $scope.bagCollectionObj.bloodBagCollectionStatusId;
										
									}
									}
								});
							}
							
							$scope.getExaminationDetails = function(bloodBagCollectionId)
							{
								$rootScope.startLoader();
								var data1 = {
										organizationId:$scope.organizationId,
										unitId:$scope.unitId,
										bloodBagCollectionId:bloodBagCollectionId
										};
								var URI1 = BASE_URL + ROOT_URL + "/api/bloodbank/getExaminationListByCollectionId";
								console.log("URI1", URI1);
								GenericService.serviceAction("POST", URI1,data1).then(function(response) {
									if(response.data.status=="success"){
									$rootScope.stopLoader();
									$scope.examinationList = response.data.listObject;
									console.log("examinationList",$scope.examinationList);
									
									if(response.data.listObject.length >0 && response.data.listObject!=null && response.data.listObject!=undefined)
									{
										$scope.examinationDetailsList = response.data.listObject;
										
									}
									}
								});
								
							}

							if($scope.common.bloodBagCollectionId!=null && $scope.common.bloodBagCollectionId!=undefined )
							{
								$scope.saveDetails=true;
								$scope.bagCollectionObj = {};
								$scope.getBagCollectionDetails($scope.common.bloodBagCollectionId);
								
								//get Examination Details
								$scope.examinationList = [];
								$scope.getExaminationDetails($scope.common.bloodBagCollectionId);
							}
							else{
								$scope.saveDetails = false ;
							}
							
							
							$scope.campMasterList = [];
							var data = {
									organizationId:$scope.organizationId,
									unitId:$scope.unitId
									};
							
							var URI = BASE_URL + ROOT_URL + "/global/bloodBank/getCampMasterList";
							console.log("URI", URI);
							GenericService.serviceAction("POST", URI,data).then(function(response) {
								$rootScope.stopLoader();
								$scope.campMasterList = response.data.listObject;
								console.log("campMasterList",$scope.campMasterList);
							});
							
							$scope.bloodBankMasterList=[];
							var URI = BASE_URL + ROOT_URL +  "/bloodBank/getActiveBloodBankMaster/"+$scope.organizationId +"/" + $scope.unitId;
							GenericService.serviceAction("GET", URI,"").then(function(response) {
								$rootScope.stopLoader();
								$scope.bloodBankMasterList = response.data.listObject;
								console.log("commonList",$scope.bloodBankMasterList);
							});
							
							$scope.bloodBagCapacityMasterList=[];
							$scope.bloodBagTypeList = [];
							var URI1 = BASE_URL + ROOT_URL + "/global/bloodBank/getActiveBloodBagTypeMaster/"+$scope.organizationId +"/"+ $scope.unitId; 
							
							GenericService.serviceAction("GET",URI1,"").then(function(response){
								if(response.data.status == "success"){
								$rootScope.stopLoader();					
								$scope.bloodBagTypeList = response.data.listObject;
								console.log("bloodBagTypeList",$scope.bloodBagTypeList);
								}
							});
							
							var URI = BASE_URL + ROOT_URL + "/global/bloodBank/getActiveBloodBagCapacityMaster/"+$scope.organizationId +"/"+ $scope.unitId; 
							GenericService.serviceAction("GET",URI,"").then(function(response){
								if(response.data.status == "success"){
								$rootScope.stopLoader();					
								$scope.bloodBagCapacityMasterList = response.data.listObject;
								console.log("bloodBagCapacityMasterList",$scope.bloodBagCapacityMasterList);
								}
							});
							
							$scope.examinationDetailsList= [];
							
							$scope.addExaminationDetails = function(){
								
								if($scope.exam.pulse == '' || $scope.exam.pulse == null)
								{
								    growl.warning("Please enter pulse",
										{
											title : 'Warning!'
										});
								    return false;

								}
							
							else if($scope.exam.bpSystolic == '' || $scope.exam.bpSystolic == null)
								{
								    growl.warning("Please enter BP Systolic",
										{
											title : 'Warning!'
										});
								    return false;

								}
							else if($scope.exam.bpDiastolic == '' || $scope.exam.bpDiastolic == null)
								{
									growl.warning("Please enter BP diastolic",
											{
												title : 'Warning!'
											});
									    return false;
								}
							else if($scope.exam.examinationDate == '' || $scope.exam.examinationDate == null)
								{
									growl.warning("Please select examinationDate",
											{
												title : 'Warning!'
											});
									    return false;
								}else
									{
								
								 var nawArr={};
								 nawArr.examinationDate= moment($scope.exam.examinationDate).format('DD-MM-YYYY HH:mm:ss');
								 nawArr.pulse=$scope.exam.pulse;
								 nawArr.bpSystolic=$scope.exam.bpSystolic;
								 nawArr.bpDiastolic=$scope.exam.bpDiastolic;
								 nawArr.createdBy=$scope.exam.createdBy;
								 nawArr.createdDate=$scope.exam.createdDate;
								 nawArr.status=$scope.exam.status;
								 nawArr.updatedBy=$scope.exam.updatedBy;
								 nawArr.updatedDate=$scope.exam.updatedDate;
								 nawArr.organizationId=$scope.exam.organizationId; 
								 nawArr.unitId=$scope.exam.unitId;
								 
								$scope.examinationDetailsList.push(nawArr);
								$scope.donorDetailsObj.examinationDetailsList=$scope.examinationDetailsList;
								console.log("examinationDeatailsList",$scope.examinationDetailsList);
								$scope.exam = {};
							}
							}
							
							
							
							$scope.saveBloodBagCollection = function(){
								
								$scope.donorDetailsObj.donorId = $scope.common.donorId;
								$scope.donorDetailsObj.collectionDate =  moment($scope.collectionDate).format('DD-MM-YYYY HH:mm:ss');
								$scope.donorDetailsObj.bloodGroupId = $scope.common.bloodGroupId;
								console.log("final Object ",$scope.donorDetailsObj);
								
								if($scope.examinationDetailsList.length==0)
								{
									growl.warning("Please Select Examination Details!!",
											{
												title : 'Warning!'
											});
								}else{
								
								$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL +"/api/bloodbank/saveBloodBagCollection";
								console.log("URI", URI);
								GenericService.serviceAction("POST", URI,$scope.donorDetailsObj).then(function(response){
									if (response.data.status == 'success')
									{	$rootScope.stopLoader();
									$scope.exam = {}
										if(response.data.object!=null){
											console.log("response.data.object",response.data.object);
											$scope.bagCollectionObj = {};
											$scope.getBagCollectionDetails(response.data.object.bloodBagCollectionId);
										
											$scope.examinationList = [];
											$scope.getExaminationDetails(response.data.object.bloodBagCollectionId);
										}
										$scope.donorDetailsObj ={};
										$scope.examinationDeatailsList=[];
										$scope.collectionDate="";
										growl.success(response.data.message,
												{
													title : 'Success!'
												});
										$state.go('bloodBagCollectionWorklist');
									
									} else if(response.data.status == 'error')
									{
										$rootScope.stopLoader();
										growl.error(response.data.message,
												{
													title : 'Error!'
												});
										
									}else if(response.data.status == 'Already Exists.')
									{
										$rootScope.stopLoader();
										growl.error(response.data.message,
												{
													title : 'Error!'
												});
										
									}
								});
								}
								
							}
							
							
							//need to change get from blood bag reason mastr list
							$scope.reasonList = [];
							$scope.getReasonMasterList = function()
							{
								var data={
										organizationId:$scope.organizationId
								};
								var URI = BASE_URL + ROOT_URL+ GET_REASON_MASTER_LIST ;
								GenericService
								.serviceAction(METHOD_POST, URI ,data)
								.then(
										function(response) {

											if (response.data.status == "success") {
												$rootScope.stopLoader();
												$scope.reasonList = response.data.listObject;
												console.log("reasonList ",response.data.listObject);
											}
										});
							}
							
							
							$scope.getReasonMasterList();
							
							$scope.removeExaminationDetails = function(index,info1){
								if(info1.bloodBagCollectionId==null){
								$scope.examinationDetailsList.splice(index,1);
								}
							}
			}]);
							