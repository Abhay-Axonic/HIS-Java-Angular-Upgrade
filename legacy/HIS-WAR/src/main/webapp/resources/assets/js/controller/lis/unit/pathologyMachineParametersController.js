/**
 * @author Ankit Landge
 */

angular
		.module('myApp')
		.controller(
				"pathologyMachineParametersController",
				[	    '$scope',
						'$rootScope',
						'$state',
						'$cookies',
						'GenericService',
						'PagerService',
						'promiseFactory',
						'$sessionStorage',
						'growl',
						'GenericServiceFileUpload',
						'reportService',
						function($scope, $rootScope, $state, $cookies,
								GenericService, PagerService, promiseFactory,
								$sessionStorage, growl,
								GenericServiceFileUpload, reportService) {

							$rootScope.loginpage = true;
							$scope.LISDynamicLabel = "Machine ";
							$scope.saveBtnFlag = true;
							$scope.updateBtnFlag = false;

							/** Variables for pagination */
							$scope.offset = 0;
							$scope.noOfRecordsPerPage = 10;
							$scope.commonList = [];
							$scope.totalNoOfRecords;
							$scope.searchKeyword;
							$scope.commonListCount;

							/** * Check user is Loggedin */
							var cookieObject, cookieObjectAppConfig;
							[ cookieObject, cookieObjectAppConfig ] = $scope
									.checkCookies();
							$scope.unitId = cookieObject.unitId;
							$scope.orgId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							//console.info($scope.orgId)
							//$(".selectedPageName").text("Machine Master");
							
							
							/* START FORFOR MACHINE PARA List MASTER */

							  $scope.getMachineParaList = function() {
									 $scope.machineparaMaster = {
									
												"organisationId" : $scope.orgId,
												"unitId" : $scope.unitId,
											}
							    	
										
										var URI = BASE_URL + ROOT_URL
										        + LIS_GLOBAL
												+ GETMACHINELIST;
										GenericService
												.serviceAction("POST", URI, $scope.machineparaMaster)
												.then(
														function(response) {
															$scope.machineList = [];

															if (response.data.status == "success") {
																$scope.machineList = response.data.listObject;
																console.log($scope.machineList);

															}
														});

									}
							      $scope.getMachineParaList();
							      
							      $scope.selectedMachineIdFun=function(machineId,action){
							    	  console.info(machineId);
							    	  $scope.arrayParameter=[];
							    	  $scope.parameterList=[];
							    	  $scope.machineparaList = [];
							    	  $scope.finalPathalogyParaList=[];
							    	  $scope.editScope='N';
										 $scope.machineparaMaster = {
													"organisationId" : $scope.orgId,
													"unitId" : $scope.unitId,
													"machineId" :machineId,
												}
								    console.info("indside parameter")
											var URI = BASE_URL + ROOT_URL
											        + LIS_GLOBAL
													+ GET_PARAMETER_MACHINE_LIST;
											GenericService.serviceAction("POST", URI, $scope.machineparaMaster)
								             .then(function(response) {
																if (response.data.status == "success") {
																	$scope.machineparaList = response.data.listObject;
																	console.log("machineparaList",$scope.machineparaList);

																}
															});
											$scope.getParameterList();
											
											if(action=='edit'){
												$scope.editScope='Y';
													var URI = BASE_URL + ROOT_URL+ "/LIS/global/getPathalogyMachineListByMachineId";
													GenericService.serviceAction("POST", URI,$scope.machineparaMaster).then(function(response) {
														if (response.data.status == 'success') {
															$scope.arrayParameter=response.data.listObject;
															console.log("Param Pathlogy list:",$scope.arrayParameter);
														    } else {
															growl.error("not available",
																	{
																		title : 'Error!'
																	});
															
														   }
														$rootScope.stopLoader();
													});
											}

										}


									  $scope.getParameterList = function() {
											 $scope.parameter = {
											
														"orgId" : $scope.orgId,
														"orgUnitId" : $scope.unitId,
														
													}
												var URI = BASE_URL + ROOT_URL
												        + "/LIS/unit"
														+ GET_PARAMETER_ACT_LIST;
												GenericService
														.serviceAction("POST", URI, $scope.parameter)
														.then(
																function(response) {
																	$scope.parameterList=[];
																	if (response.data.status == "success") 
																	{
																		$scope.parameterList = response.data.listObject;
																		console.log($scope.parameterList);

																	}
																});

											}
									      $scope.getParameterList();
									      
									   
									      $scope.getPatraMachineDetails=function(dd){
									    	  console.info(dd)
									      }
									      
									      $scope.getParameterDetails=function(aa){
									    	  console.info(aa)
									      }
									      
									      $scope.arrayParameter =[];
									      $scope.finalPathalogyParaList=[];
									      $scope.parameterdetails=function(para,machinepara){
									    
									    	  var flag=false;
									    	  angular.forEach($scope.arrayParameter, function(
														value, index) 
											 {
									    		  if(value.machinParameterId==machinepara.machinParameterId || value.parameterId==para.parameterId ){
									    			flag=true;  
									    		  }
											});
									    	  
									    	if(flag==false){
									    		var temp={
									    				 "parameterId":para.parameterId,
									    				 "machineId":machinepara.machineId,
									    				 "parameterName":para.parameterName,
									    				 "machinParameterId":machinepara.machinParameterId,
									    				 "machineParameterDesc":machinepara.machineParameterDesc,
									    				 "createdBy":$scope.userId,
									    				 "updatedBy":$scope.userId,
									    				 "status":'A',
									    				 "organisationId":$scope.orgId,
									    				 "unitId":$scope.unitId
									    		};
									    	
									    	  $scope.arrayParameter.push(temp);
									    	}
									    	else{
									    		growl.error("Already added",{
									    			title : 'Error!'
												});
									    	}
									    	  console.log($scope.arrayParameter);
									      }
									      
									      $scope.savePathologyMachineParameter=function(){
									    	  try {

													if ($scope.popUpFlag == false) {
														$rootScope.startLoader();
													}
													var date = new Date();
													$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');

													if ($scope.popUpFlag) {
														$scope.showModal = !$scope.showModal;

													} else {
														$scope.finalObj={
																"machinePathalogyParameterMpperDtoList":$scope.arrayParameter,
																"inactivePathalogyParameterMpperDtoList" : $scope.editScope=='Y' ? $scope.finalPathalogyParaList :[]
														};
														
														if($scope.editScope=='Y'){
															var URI = BASE_URL + ROOT_URL+ "/LIS/global/updatePathalogyMachineParaMapping";
														}
														else{
															var URI = BASE_URL + ROOT_URL+ "/LIS/global/addPathalogyMachineParaMapping";
														}
														
														GenericService.serviceAction("POST", URI,$scope.finalObj).then(function(response) {

															if (response.data.status == 'success') {
																if($scope.editScope=='Y'){
																	growl.success("Updated Successfully",
																			{
																				title : 'Success!'
																			});
								                                      
																}else{
																	growl.success(response.data.message,
																			{
																				title : 'Success!'
																			});
																}
															
																// $scope.$broadcast('show-errors-reset');
															
															    } else if(response.data.status == 'error') {
																growl.error(response.data.message,
																		{
																			title : 'Error!'
																		});
																
															   }
															$rootScope.stopLoader();
															$scope.getPathalogyParameterList();
															$scope.finalPathalogyParaList=[];
															$scope.arrayParameter=[];
															$scope.getMachineParaList();
														});
														
													}

												} catch (e) {
													console.log("Exception-", e);
												}
									      }
									      
									      $scope.pathalogyMachineList=[];
									      $scope.getPathalogyParameterList=function(){
									    	  try {
													var data = {
															"organisationId" : $scope.orgId,
															"unitId" : $scope.unitId
													}

														var URI = BASE_URL + ROOT_URL+ "/LIS/global/getPathalogyMachineParameterList";
														
														GenericService.serviceAction("POST", URI,data).then(function(response) {

															if (response.data.status == 'success') {
																$scope.pathalogyMachineList=response.data.listObject;
																console.log("Machine list:"+$scope.pathalogyMachineList);
															    } else {
																growl.error(response.data.message,
																		{
																			title : 'Error!'
																		});
																
															   }
															$scope.arrayParameter=[];
															$rootScope.stopLoader();
														});
														

												} catch (e) {
													console.log("Exception-", e);
												}
									      }
									      $scope.getPathalogyParameterList();
									      
									      $scope.pathalogyParamList=[];
									      $scope.getPathalogyParameterListByMachineId=function(machineId){
									    	  try {
													var data = {
															"organisationId" : $scope.orgId,
															"unitId" : $scope.unitId,
															"machineId" :machineId
													}

														var URI = BASE_URL + ROOT_URL+ "/LIS/global/getPathalogyMachineListByMachineId";
														
														GenericService.serviceAction("POST", URI,data).then(function(response) {

															if (response.data.status == 'success') {
																$scope.pathalogyParamList=response.data.listObject;
																console.log("Param Pathlogy list:"+$scope.pathalogyParamList);
															    } else {
																growl.error(response.data.message,
																		{
																			title : 'Error!'
																		});
																
															   }
															$rootScope.stopLoader();
														});
														

												} catch (e) {
													console.log("Exception-", e);
												}
									      }
									      
									      $scope.removeFun=function(obj,index){
									    	  $scope.arrayParameter.splice(index,1);
									    	  
									    	  if($scope.editScope=='Y' && obj.machineParameterMpprId!=undefined){
									    		  var temp={
									    				     "machineParameterMpprId":obj.machineParameterMpprId,
										    				 "parameterId":obj.parameterId,
										    				 "machineId":obj.machineId,
										    				 "parameterName":obj.parameterName,
										    				 "machinParameterId":obj.machinParameterId,
										    				 "machineParameterDesc":obj.machineParameterDesc,
										    				 "createdBy":$scope.userId,
										    				 "updatedBy":$scope.userId,
										    				 "status":'I',
										    				 "organisationId":$scope.orgId,
										    				 "unitId":$scope.unitId
										    		};
									    		  $scope.finalPathalogyParaList.push(temp);
									    	  }
									    	  console.log("final list:",$scope.finalPathalogyParaList);
									      }
									      
									      
									      $scope.updateStatus=function(machineId,status){
									    	  try {
													var data = {
															"organisationId" : $scope.orgId,
															"unitId" : $scope.unitId,
															"machineId" :machineId,
															"status":status
													}

														var URI = BASE_URL + ROOT_URL+ "/LIS/global/updatePathalogyMachineParameterStatusByMachineId";
														//return false;
														GenericService.serviceAction("POST", URI,data).then(function(response) {

															if (response.data.status == 'success') {
																$scope.pathalogyParamList=response.data.listObject;
																console.log("Param Pathlogy list:"+$scope.pathalogyParamList);
															    } else {
																growl.error(response.data.message,
																		{
																			title : 'Error!'
																		});
																
															   }
															$rootScope.stopLoader();
														});
														

												} catch (e) {
													console.log("Exception-", e);
												} 
									      }
									      
									      $scope.clearParaList=function(){
									    	  $scope.arrayParameter=[];
									      }
				 } ]);