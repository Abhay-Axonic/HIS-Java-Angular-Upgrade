/**
 * @author  Ganesh Chaudhari
 * @author dell
 */

angular.module('myApp').controller(
		"testCostingMasterController",
		[ '$scope','$rootScope','$state','$cookies','GenericService','PagerService','promiseFactory','$sessionStorage',
			'growl','BillingGenericService',
				function($scope,$rootScope,$state, $cookies, GenericService,PagerService,promiseFactory,$sessionStorage,growl,BillingGenericService)
				{
			       
			        $rootScope.loginpage = true;
					$scope.LISDynamicLabel = "Sample";
					$scope.saveBtnFlag = true;
					$scope.updateBtnFlag = false;
					
					
					/**Variables for pagination */
					$scope.offset = 0;
					$scope.noOfRecordsPerPage = 10;
					$scope.commonList = [];
					$scope.totalNoOfRecords;
					$scope.searchKeyword;
					$scope.commonListCount;
					
					/*** Check user is Loggedin */
					var cookieObject,cookieObjectAppConfig;
					[cookieObject,cookieObjectAppConfig,labConfig] = $scope.checkCookies();
					$scope.unitId = cookieObject.unitId;
					$scope.orgId = cookieObject.organizationId;
					$scope.userId = cookieObject.userId;
					$scope.labId=labConfig.labSpecialityId;
					
					/* code for setting Label */
//					$(".selectedPageName").text("Sample Master");
					
					$scope.initCommon = function()
					{
						$scope.currentDate = new Date();
						$scope.currentDate = moment($scope.currentDate)
								.format('DD-MM-YYYY HH:mm:ss');
						$scope.saveBtnFlag = false; 
						$scope.updateBtnFlag = true;
						$scope.search="";
						$scope.common =
						{
							 "specialityId":"",
							 "subSpecialityId":"",
							 "serviceId":"",
						     "status":"A",
							 "createdBy":$scope.userId,
							 "createdDate":$scope.currentDate,
							 "updatedBy":$scope.userId,
							 "updatedDate":$scope.currentDate,
						     "organizationId":$scope.orgId,
						}
						$scope.dataList = [ {
							testCostHeadId : '',
							cost : ''
							
						} ];
						// getting specility list start
						var commonObj = {
								unitId : $scope.unitId,
								organizationId : $scope.orgId,
							}

							var URI = BASE_URL + ROOT_URL
									+ GETACTIVESPECIALITY;
						
							BillingGenericService
									.serviceAction(METHOD_POST, URI,
											commonObj,
											NOTIFICATION_MSG_STATUS_FALSE)
									.then(
											function(response) {
												console.log(response);
												// $rootScope.startLoader();
												if (response.data.status == "success")
													$scope.specialityList = response.data.listObject;
												console.log("specialityList",$scope.specialityList);
											});
							//======================================= getting specility list end===============
							//for getting head cost start 
								var data = "";
								var URI = BASE_URL + ROOT_URL + LIS_GLOBAL + "/getActiveTestCodeHeadList" + S + $scope.orgId ;
								console.log("URI", URI);
								GenericService.serviceAction("GET", URI, data).then(function(response) {
									$rootScope.stopLoader();
									$scope.costHeadList = response.data.listObject;
									console.log("costHeadList",$scope.costHeadList);
								});
							
							//for getting head cost end 
					}
					$scope.initCommon();
					
					$scope.initSampleMaster = function(){
						$scope.currentDate = new Date();
						$scope.currentDate = moment($scope.currentDate)
								.format('DD-MM-YYYY HH:mm:ss');
					$scope.sampleMaster =
					{
						 "specialityId":$scope.common.specialityId,
						 "subSpecialityId":$scope.common.subSpecialityId,
						 "serviceId":$scope.common.serviceId,
					     "status":$scope.common.status,
						 "createdBy":$scope.userId,
						 "createdDate":$scope.currentDate,
						 "updatedBy":$scope.userId,
						 "updatedDate":$scope.currentDate,
					     "organizationId":$scope.orgId,
					     "unitId":$scope.unitId
					 }
					}
					//getting subspecility by specility start
					$scope.getSubSpecialityBySpeciality = function(specialityId) {
						var data = {
							organizationId : $scope.orgId,
							unitId : $scope.unitId,
							specialityId : specialityId
						}
						console.log(data);
						var URI = BASE_URL + ROOT_URL
								+ GETSUBSPECIALITYBYSPECIALITYID;
						BillingGenericService
								.serviceAction(METHOD_POST, URI, data,
										NOTIFICATION_MSG_STATUS_FALSE)
								.then(
										function(response) {
											console.log(response);
											// $rootScope.startLoader();
											$scope.subSpecialityList = [];

											if (response.data.status == "success") {
												$scope.subSpecialityList = response.data.listObject;
												console.log("subSpecialityList",$scope.subSpecialityList);

											}

										});
					}
					//getting subspecility by specility End
					
					//searching service aganinst sub speacility start
						$scope.searchServices = function(searchKeyword){
				        	
				    		var URI = BASE_URL + ROOT_URL + "/global/UnitServiceMapper/search/"+searchKeyword+"/speciality/"+$scope.common.specialityId+"/subSpeciality/"+$scope.common.subSpecialityId+"/unit/"+$scope.unitId+"/org/"+ $scope.orgId;
				    		return BillingGenericService.serviceAction(METHOD_GET,URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
				    			if(response.data.status == "success"){
				    				$scope.servicesList = response.data.listObject;
				    				console.log("servicesList",$scope.servicesList);
				    			}
				    			return $scope.servicesList;
				    		});
			        	
						}
					//searching service aganinst sub speacility end
					$scope.addServiceToTempServiceList = function($item, $model, $label){
			    		console.log($item);
			    		$scope.common.serviceId = $item.serviceMasterId;
			    		$scope.orderServiceName = $item.serviceStandardName;
			    	}
					
					//for adding & removing the datalist start
					  $scope.add = function() {
							var data = {
									testCostHeadId : '',
									cost : '',
								
							};
						
							$scope.dataList.push(data);
						};
						$scope.remove = function(index) {
							var newDataList = [];
							var le = $scope.dataList.length;
							var i = 1;
							if(le != i){
							angular.forEach($scope.dataList, function(v) {
								if (index != i) {
									newDataList.push(v);
								}
								i++;
							});
							
							$scope.dataList = newDataList;
							}
							$scope.getTotal();
						};
						//for adding & removing the datalist end
						
						//calculating total for costHead start
						$scope.getTotal=function(){
							$scope.finalTotal=0;
							angular.forEach($scope.dataList, function(v) {
								$scope.finalTotal=parseInt($scope.finalTotal)+parseInt(v.cost) ;
							});
						}
						//calculating total for costHead end
						
						//for clearing data list on cancel click start
							$scope.cancelHeadCost=function(){
								$scope.initCommon();
								$scope.finalTotal=0;
							}
						//for clearing data list end
					$scope.save = function() 
					{
						console.log("data list=",$scope.dataList);
//						$scope.$broadcast('show-errors-check-validity');
//						if ($scope.commonform.$valid)
//						{
							try 
							{
								$rootScope.startLoader();
								$scope.initSampleMaster();
								$scope.sampleMaster.costHeadList=$scope.dataList;
								console.log("SampleMaster",JSON.stringify($scope.sampleMaster));
//								return false;
								var URI = BASE_URL + ROOT_URL + LIS_GLOBAL+"/addTestHeadCosting";
								GenericService.serviceAction("POST", URI, $scope.sampleMaster).then(function(response) 
								{	
									if (response.data.status == 'success')
									{
										$rootScope.stopLoader();
										growl.success(response.data.message,
												{
													title : 'Success!'
												});
										$scope.$broadcast('show-errors-reset');
										$scope.initCommon();
										$scope.finalTotal=0;
//										$scope.initSampleMasterList($scope.orgId, $scope.offset, $scope.noOfRecordsPerPage);
									} else if(response.data.status == 'error')
									{
										$rootScope.stopLoader();
										growl.error(response.data.message,
												{
													title : 'Error!'
												});
									}
								
								});
							} catch (e) {
								console.log("Exception",e.message);
							}
							
//						}
					}
					/*$scope.update = function() 
					{
//						$scope.$broadcast('show-errors-check-validity');
//						if ($scope.commonform.$valid)
						{
							try {
								$rootScope.startLoader();
								$scope.initSampleMaster();
								console.log("SampleMaster",JSON.stringify($scope.sampleMaster));
								var URI = BASE_URL + ROOT_URL + LIS_GLOBAL+"/updateTestCodeHead";
								GenericService.serviceAction("post", URI, $scope.sampleMaster).then(function(response) 
								{
									if (response.data.status == 'success')
									{
										$rootScope.stopLoader();
										growl.success(response.data.message,
												{
													title : 'Success!'
												});
//										$scope.$broadcast('show-errors-reset');
										$scope.initCommon();
										$scope.initSampleMasterList($scope.orgId, $scope.offset, $scope.noOfRecordsPerPage);
									} else if(response.data.status == 'error')
									{
										$rootScope.stopLoader();
										growl.error(response.data.message,
												{
													title : 'Error!'
												});
									}
								});
							} catch (e) 
							{
								console.log("Exception",e.message);
							}
							
						}
					}*/
					
				} ]);
