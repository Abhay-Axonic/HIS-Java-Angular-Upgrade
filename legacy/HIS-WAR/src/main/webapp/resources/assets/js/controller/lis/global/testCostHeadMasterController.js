/**
 * @author  Ganesh Chaudhari
 * @author dell
 */

angular.module('myApp').controller(
		"testCostHeadMasterController",
		[ '$scope','$rootScope','$state','$cookies','GenericService','PagerService','promiseFactory','$sessionStorage',
			'growl',
				function($scope,$rootScope,$state, $cookies, GenericService,PagerService,promiseFactory,$sessionStorage,growl)
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
					$scope.searchKeyword="";
					
					/*** Check user is Loggedin */
					var cookieObject,cookieObjectAppConfig;
					[cookieObject,cookieObjectAppConfig] = $scope.checkCookies();
					$scope.unitId = cookieObject.unitId;
					$scope.orgId = cookieObject.organizationId;
					$scope.userId = cookieObject.userId;
					
					/* code for setting Label */
//					$(".selectedPageName").text("Sample Master");
					
					$scope.initCommon = function()
					{
						$scope.saveBtnFlag = false; 
						$scope.updateBtnFlag = true;
						$scope.common =
						{
							 "testCosHeadId":"",
							 "testCostHeadCode":"",
							 "testCostHeadDesc":"",
						     "status":"A",
							 "createdBy":$scope.userId,
							 "createdDate":"",
							 "updatedBy":$scope.userId,
							 "updatedDate":"",
						     "organizationId":$scope.orgId,
						}
					}
					$scope.initCommon();
					
					$scope.initSampleMaster = function(){
						$scope.currentDate = new Date();
						$scope.currentDate = moment($scope.currentDate)
								.format('DD-MM-YYYY HH:mm:ss');
					$scope.sampleMaster =
					{
						 "testCosHeadId":$scope.common.testCosHeadId,
						 "testCostHeadCode":$scope.common.testCostHeadCode,
						 "testCostHeadDesc":$scope.common.testCostHeadDesc,
					     "status":$scope.common.status,
						 "createdBy":$scope.userId,
						 "createdDate":$scope.currentDate,
						 "updatedBy":$scope.userId,
						 "updatedDate":$scope.currentDate,
					     "organizationId":$scope.orgId
					 }
					}
					
					// ====================================================CODE START FORFOR SAMPLE MASTER LIST===========================================================

					$scope.setNoOfRecords = function() {
						$scope.initSampleMasterList($scope.orgId, $scope.offset, $scope.noOfRecordsPerPage);
					};
					$scope.initSampleMasterList = function(orgId, offset, noOfRecordsPerPage)
					{
						$rootScope.startLoader();
						var data = "";
						offset = offset != null ? offset : 0;
						noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
						var URI1 = BASE_URL + ROOT_URL + LIS_GLOBAL + "/getTestCodeHeadList"+S+orgId + S + offset
								+ S + noOfRecordsPerPage;
						var URI2 = BASE_URL + ROOT_URL + LIS_GLOBAL + "/countOfTestCodeHeadList" + S + orgId;
						promiseFactory.setPromises(URI1, URI2, "GET", "GET").then(function(response) 
						{
							$rootScope.stopLoader();
							$scope.commonList = response[0].listObject;
							$scope.commonListCount = response[1].object;
							$scope.setPage(1, false);
							console.log($scope.commonList);
						});
					}

					$scope.getSampleMasterList = function(orgId, offset, noOfRecordsPerPage) {
						$rootScope.startLoader();
						console.log("offset", offset);
						var data = "";
						offset = offset != null ? offset : 0;
						noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
						var URI = BASE_URL + ROOT_URL + LIS_GLOBAL + "/getTestCodeHeadList" + S + orgId + S + offset
								+ S + noOfRecordsPerPage;
						console.log("URI", URI);
						GenericService.serviceAction("GET", URI, data).then(function(response) {
							$rootScope.stopLoader();
							$scope.commonList = response.data.listObject;
						});
					}

					$scope.pager = {};
					$scope.page;

					$scope.setPage = function(page, flag) {
						if (page < 1 || page > $scope.pager.totalPages)
						{
							return;
						}
						$scope.pager = PagerService.GetPager($scope.commonListCount, page, $scope.noOfRecordsPerPage);
						if (flag)
						{
							$scope.getSampleMasterList($scope.orgId, $scope.pager.startIndex, $scope.pager.pageSize);
						}
					}

					$scope.initSampleMasterList($scope.orgId, $scope.offset, $scope.noOfRecordsPerPage);

					// ====================================================CODE END FOR SAMPLE MASTER LIST===========================================================
					
					
					//code for Activating Inactivating Sample
					$scope.updateStatus = function(commonId,status)
					{
						try {
							$rootScope.startLoader();
							$scope.initSampleMaster();
							$scope.sampleMaster.testCosHeadId=commonId;
							$scope.sampleMaster.status=status;
							var URI = BASE_URL + ROOT_URL + LIS_GLOBAL+"/updateTestCodeHeadStatus";
							console.log("ACT_INACT_URI",URI);
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
						
					}
					
					
					$scope.showUpdateBtn = function(commonId)
					{
						try {
							$rootScope.startLoader();
							$scope.saveBtnFlag = true;
							$scope.updateBtnFlag = false;
							$scope.common.testCosHeadId=commonId;
							$scope.common.organizationId=$scope.orgId;
							var URI = BASE_URL + ROOT_URL + LIS_GLOBAL+"/getTestCodeHeadListById";
							console.log("EDIT",URI);
							GenericService.serviceAction("post", URI, $scope.common).then(function(response) 
							{

								console.log("data",response.data);

								if (response.data.status == 'success')
								{
									$rootScope.stopLoader();
									if(response.data.listObject[0].status == "I"){
										growl.warning("This entry is deactivated please active first then update.",
												{
													title : 'warning!'
												});
										$scope.saveBtnFlag = true;
										$scope.updateBtnFlag = false;
										return false;
									}else{
									$scope.common = response.data.listObject[0];
									console.log("$scope.common",$scope.common);
									}
								} else if(response.data.status == 'error')
								{
									$rootScope.stopLoader();
									alert("Error In Fetching Data");
								}
							});
						} catch (e) {
							console.log("Exception",e.message);
						}
						
					}
					
					
					$scope.save = function() 
					{
					
							try 
							{
								$scope.$broadcast('show-errors-check-validity');
								if($scope.popUpFlag==false)
								{
								  $rootScope.startLoader();
								}
								$scope.initSampleMaster();
								console.log("SampleMaster",JSON.stringify($scope.sampleMaster));
								var URI = BASE_URL + ROOT_URL + LIS_GLOBAL+"/addTestCodeHead";
								if ($scope.testCostHeadForm.$valid){
									if ($scope.popUpFlag) {
										$scope.showModal = !$scope.showModal;

									} else {
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
									}
								}
							} catch (e) {
								console.log("Exception",e.message);
							}
							
//						}
					}
					$scope.update = function() 
					{
							try {
								$rootScope.startLoader();
								$scope.initSampleMaster();
								console.log("SampleMaster",JSON.stringify($scope.sampleMaster));
								$scope.$broadcast('show-errors-check-validity');
								var URI = BASE_URL + ROOT_URL + LIS_GLOBAL+"/updateTestCodeHead";
								if ($scope.testCostHeadForm.$valid){
									
								GenericService.serviceAction("post", URI, $scope.sampleMaster).then(function(response) 
								{
									if (response.data.status == 'success')
									{
										$rootScope.stopLoader();
										growl.success(response.data.message,
												{
													title : 'Success!'
												});
										$scope.saveBtnFlag = true;
										$scope.updateBtnFlag = false;
										$scope.$broadcast('show-errors-reset');
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
								}
							} catch (e) 
							{
								console.log("Exception",e.message);
							}
							
						}
					/*}*/
					
				} ]);
