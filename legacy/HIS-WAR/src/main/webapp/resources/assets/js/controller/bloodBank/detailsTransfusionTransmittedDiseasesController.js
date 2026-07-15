angular
		.module('myApp')
		.controller(
				'detailsTransfusionTransmittedDiseasesController',
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
								
							$scope.testDetailList = [];
							
							$scope.common = $stateParams.bloodBagObjResult;
									
							console.log("$scope.common ",$scope.common );
							 
							$scope.detailsObj = 
								{
									bloodTestId:"",
									testResult:"",
									ttdDoneById:"",
									ttdStatus:"",
									bloodTestObj:{
										bloodTestDesc:"",
										bloodTestId:""
									},
									bloodGroupObj:{
										bloodGroupId:$scope.common.bloodGroupId,
										bloodGroupDesc:$scope.common.bloodGroup
									}
							
								};
							
							if($scope.common.ttdDoneById!=undefined && $scope.common.ttdDoneById!=null){
								$scope.detailsObj.ttdDoneById = $scope.common.ttdDoneById
							}
							//get Saved Test list by ttd ID
							
							$scope.getSavedTestList = function(transfusionTransmittedDiseasesId)
							{
								$scope.testDetailList = [];
								$rootScope.startLoader();
								var data ={
										unitId:$scope.unitId,
										organizationId:$scope.organizationId,
										transfusionTransmittedDiseasesId:transfusionTransmittedDiseasesId
								};
								
								var URI = BASE_URL + ROOT_URL + GET_SAVED_TTD_TEST_LIST_BY_TTD_ID ;

								GenericService.serviceAction(METHOD_POST, URI , data)
										.then(
											function(response) {
													if (response.data.status == "success") {
														$rootScope.stopLoader();
														if(response.data.listObject!=null && response.data.listObject!=undefined){
														$scope.testDetailList = response.data.listObject;
														console.log("$scope.testDetailList",$scope.testDetailList);
														}
															
													} else{
														$rootScope.startLoader();
														growl.error(response.data.message, {
															title : 'error!'
														});
													}
														
									});
							}
							
							if($scope.common.transfusionTransmittedDiseasesId!=null)
							{
								$scope.transfusionTransmittedDiseasesId =$scope.common.transfusionTransmittedDiseasesId;
								$scope.getSavedTestList($scope.transfusionTransmittedDiseasesId);
							}
							
								//get TTD test List
							$scope.testList = [];
							$scope.getTestList = function()
							{
								var data ={
										unitId:$scope.unitId,
										organizationId:$scope.organizationId,
								};
								
								var URI = BASE_URL + ROOT_URL + GET_TTD_TEST_LIST + $scope.organizationId + "/" + $scope.unitId;

								GenericService.serviceAction(METHOD_GET, URI)
										.then(
											function(response) {
													if (response.data.status == "success") {
														$scope.testList = response.data.listObject;
														console.log("$scope.testList",$scope.testList);
													} 
									});
								
							}
							
							$scope.getTestList();
							
							
							$scope.addTestDetails = function()
 							{
								if ($scope.detailsObj.bloodTestObj == ''
										|| $scope.detailsObj.bloodTestObj == null) {
									growl.warning("Please select Blood Test", {
										title : 'Warning!'
									});
									return false;

								}
								else if ( $scope.detailsObj.testResult == ''
										||  $scope.detailsObj.testResult == null) {
									growl.warning("Please select Blood Test Result", {
										title : 'Warning!'
									});
									return false;

								} else {
									
									 var nawArr={};
									 nawArr.transfusionTransmittedDiseasesDetailsId=null;
									 nawArr.testId=$scope.detailsObj.bloodTestObj.bloodTestId;
									 nawArr.bloodTestDesc=$scope.detailsObj.bloodTestObj.bloodTestDesc;
									 if($scope.detailsObj.testResult=='true'){
									 nawArr.testResult=true;
									 }
									 else{
										 nawArr.testResult=false;
									 }
									 nawArr.bloodGroupId=$scope.detailsObj.bloodGroupObj.bloodGroupId;
									 nawArr.bloodGroupDesc=$scope.detailsObj.bloodGroupObj.bloodGroupDesc;
									 nawArr.createdBy=$scope.userId;
									 nawArr.createdDate=moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
									 nawArr.status="A"
									 nawArr.updatedBy=$scope.userId;
									 nawArr.updatedDate=moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
									 nawArr.organizationId=$scope.organizationId; 
									 nawArr.unitId=$scope.unitId;

									$scope.testDetailList.push(nawArr);
									$scope.detailsObj.testResult="";
									console.log("$scope.testDetailList",$scope.testDetailList);
								}
							}
							
							
							//get Blood group list
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
							
							$scope.removeTestDetails = function(idx)
							{
								if($scope.testDetailList[idx].transfusionTransmittedDiseasesDetailsId==null)
								{
								$scope.testDetailList.splice(idx, 1);
								}
							}
							
							//check valid status
							$scope.checkValid = function()
							{
								 if($scope.testDetailList.length< $scope.testList.length)
									{
									 	growl.warning("Please complete All Test",
												{
													title : 'Warning!'
												});
									 $scope.detailsObj.ttdStatus = 8;
									}
							}
							
							//save TTD test 
							$scope.saveDetails = function()
							{
								try{
								 $rootScope.startLoader();
								 if($scope.testDetailList.length< $scope.testList.length)
									{
									 if($scope.detailsObj.ttdStatus!=5)
										 {
									 		$scope.detailsObj.ttdStatus = 8;
										 }
									}
								 
								 if($scope.transfusionTransmittedDiseasesId!=null)
								{
									 $scope.id=$scope.transfusionTransmittedDiseasesId;
								}else{
									$scope.id = null;
								}
								  
								var data={
										transfusionTransmittedDiseasesId:$scope.id,
										bloodBagNo:$scope.common.bloodBagNo,
										bloodBagCollectionId:$scope.common.bloodBagCollectionId,
										bloodGroupId:$scope.detailsObj.bloodGroupObj.bloodGroupId,
										barcode:$scope.common.barCode,
										ttdDoneById:$scope.detailsObj.ttdDoneById,
										transfusionTransmittedDiseasesStatus:$scope.detailsObj.ttdStatus,
										transfusionTransmittedDiseasesDetails:$scope.testDetailList,
										createdBy:$scope.userId,
								 		createdDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
								 		status:"A",
								 		updatedBy:$scope.userId,
								 		updatedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
								 		organizationId:$scope.organizationId,
								 		unitId:$scope.unitId,
								 		donorId:$scope.common.donorId,
								 		donorNumber:$scope.common.donorNumber,
								 		bloodBankStockId:$scope.common.bloodBankStockId,
								 		tTDCheck:$scope.common.tTDCheck
								};
								
								var URI = BASE_URL + ROOT_URL + SAVE_TTD_DETAILS;

								GenericService.serviceAction(METHOD_POST, URI , data)
										.then(
											function(response) {
													if (response.data.status == "success") {
														growl.success(response.data.message,
																{
																	title : 'Success!'
																});
														$scope.common={};
														$scope.detailsObj={};
														$scope.testDetailList=[];
														$state.go('transfusionTransmittedDiseases');
													} else if(response.data.status == 'error')
													{
														growl.error(response.data.message,
																{
																	title : 'Error!'
																});
													}
													$rootScope.stopLoader();
									});
								}catch(e){
									console.log("Exception-",e);
								}
							}
							
							$scope.usersList = [];
							$scope.getEmployeeList = function(){
								
								try{
									$rootScope.startLoader();
									
									var data = {
											organizationId:$scope.organizationId,
											unitId:$scope.unitId
									};
									var URI = BASE_URL + ROOT_URL
									+ GETUSERSLIST;
							GenericService.serviceAction("POST", URI, data)
									.then(
											function(response) {
												$scope.usersList = [];
												if (response.data.status == "success") {
													$scope.usersList = response.data.listObject;
													console.log("$scope.usersList",$scope.usersList);
												}
											});
								}catch(e){
									console.log("Exception-",e);
								}
							}
							$scope.getEmployeeList();
			}]);
							