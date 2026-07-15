/**
 * @author  Ganesh Chaudhari
 * @author dell
 */

angular.module('myApp').controller(
		"biochemicalTestMasterController",
		[ '$scope','$rootScope','$state','$cookies','GenericService','PagerService','promiseFactory','$sessionStorage',
			'growl',
				function($scope,$rootScope,$state, $cookies, GenericService,PagerService,promiseFactory,$sessionStorage,growl)
				{
			       
			        $rootScope.loginpage = true;
					$scope.LISDynamicLabel = "Test";
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
					[cookieObject,cookieObjectAppConfig] = $scope.checkCookies();
					//$scope.orgUnitId = 1;
					//$scope.orgId = 1;
					$scope.orgUnitId = cookieObject.unitId;
					$scope.orgId =  cookieObject.organizationId;
					$scope.userId = cookieObject.userId;
					
					/* code for setting Label */
					$(".selectedPageName").text("Biochemical Test Master");
					$state.current.title="Biochemical Test Master";
//					alert();
					$scope.initCommon = function()
					{
						$scope.saveBtnFlag = true; 
						$scope.updateBtnFlag = false;
						$scope.common =
						{
							 "id":"",
							 "code":"",
							 "desc":"",
						     "status":"A",
							 "createdBy":$scope.userId,
							 "createdDate":"",
							 "updatedBy":$scope.userId,
							 "updatedDate":"",
						     "orgId":$scope.orgId,
						     "orgUnitId":$scope.orgUnitId
						}
					}
					$scope.initCommon();
					
					$scope.initSampleMaster = function(){
					$scope.sampleMaster =
					{
						 "id":$scope.common.id,
						 "code":$scope.common.code,
						 "desc":$scope.common.desc,
					     "status":$scope.common.status,
						 "createdBy":$scope.common.createdBy,
						 "createdDate":$scope.common.createdDate,
						 "updatedBy":$scope.common.updatedBy,
						 "updatedDate":$scope.common.updatedDate,
					     "orgId":$scope.common.orgId
					 }
					}
					
					/**List And Pagination Start**/
					$scope.setNoOfRecords = function() {
						$scope.initMasterList($scope.orgId,$scope.offset, $scope.noOfRecordsPerPage,$scope.orgUnitId);
					};
					
					$scope.initMasterList = function(orgId, offset, noOfRecordsPerPage, orgUnitId)
					{
						$rootScope.startLoader();
						
						offset = offset != null ? offset : 0;
						noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;				
						var data1 = {
										orgId:orgId,
										offset:offset,
										noOfRecordsPerPage:noOfRecordsPerPage,
										orgUnitId:orgUnitId
									};
						
						var data2 = {
								      orgId:orgId,
								      orgUnitId:orgUnitId
									};
						
						var URI1 = BASE_URL + ROOT_URL +"/LIS/unit/getMicrobiologyTestMasterList"; 
						var URI2 = BASE_URL + ROOT_URL +"/LIS/unit/getMicrobiologyTestMasterCount";
						
						GenericService.serviceAction("POST",URI1,data1).then(function(response){
							if(response.data.status == "success"){
							$rootScope.stopLoader();					
							$scope.commonList = response.data.listObject;
							console.log("commonList",$scope.commonList);
							}
						});
						
						GenericService.serviceAction("POST",URI2,data2).then(function(response){
							if(response.data.status == "success"){					
								$rootScope.stopLoader();
								$scope.commonListCount = response.data.object;
								$scope.setPage(1,false);
								console.log("commonListCount",response.data.object);
							}
						});
						
					}		
					
					$scope.masterList = function(orgId,offset, noOfRecordsPerPage,orgUnitId) {
						$rootScope.startLoader();
						console.log("offset", offset);
						offset = offset != null ? offset : 0;
						noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
						
						var data = {
								orgId:orgId,
								offset:offset,
								noOfRecordsPerPage:noOfRecordsPerPage,
								orgUnitId:orgUnitId
								};
						
						var URI = BASE_URL + ROOT_URL +"/LIS/unit/getMicrobiologyTestMasterList";
						console.log("URI", URI);
						GenericService.serviceAction("POST", URI, data).then(function(response) {
							$rootScope.stopLoader();
							$scope.sampleMaster = response.data.listObject;
							$scope.commonList = response.data.listObject;
							console.log("commonList",$scope.commonList);
						});
					}

					$scope.pager = {};
					$scope.page;

					$scope.setPage = function(page, flag) {
						if (page < 1 || page > $scope.pager.totalPages)
						{
							return;
						}
						$scope.pager = PagerService.GetPager($scope.commonListCount, page, $scope.noOfRecordsPerPage,$scope.orgUnitId);
						if (flag)
						{
							$scope.masterList($scope.orgId ,$scope.pager.startIndex, $scope.pager.pageSize,$scope.orgUnitId);
						}
					}

					$scope.initMasterList($scope.orgId,$scope.offset, $scope.noOfRecordsPerPage,$scope.orgUnitId);
					
					/**List And Pagination End**/
					
					
					//code for Activating Inactivating Sample
					$scope.updateStatus = function(commonId,status)
					{
						try {
							var date = new Date();
							$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');	
							$rootScope.startLoader();
							$scope.initSampleMaster();
								
							var data={
									microTestId:commonId,
									status:status,
									updatedBy:$scope.common.updatedBy,
									updatedDate:$scope.dt

							};
							
							var URI = BASE_URL + ROOT_URL + LIS_UNIT+UPDATE_MICROBIOLOGY_TEST_MASTER_STATUS;
							console.log("A$scope.sampleMaster",data);
											
							GenericService.serviceAction("POST", URI, data).then(function(response) 
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
							$scope.saveBtnFlag = false;
							$scope.updateBtnFlag = true;
							var URI = BASE_URL + ROOT_URL + LIS_UNIT+GET_MICROBIOLOGY_TEST_MASTER_BY_ID+S+commonId+S+$scope.orgId+S+$scope.orgUnitId;
							console.log("EDIT",URI);
							GenericService.serviceAction("GET", URI, $scope.common).then(function(response) 
							{

								console.log("data",response.data);

								if (response.data.status == 'success')
								{
									$rootScope.stopLoader();
									$scope.common = response.data.listObject[0];
									console.log("$scope.common",$scope.common);
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
						$scope.$broadcast('show-errors-check-validity');
						if ($scope.commonform.$valid)
						{
							try 
							{
								$scope.initSampleMaster();
								var date = new Date();
								$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');	
								$rootScope.startLoader();
								var data={
										
										microTestCode:$scope.common.code,
										microTestDesc:$scope.common.desc,
										status:'A',
										createdBy:$scope.common.createdBy,
										createdDate:$scope.dt,
										updatedBy:$scope.common.updatedBy,
										updatedDate:$scope.dt,
										orgId:$scope.common.orgId,
										orgUnitId:$scope.common.orgUnitId
								};
								
								console.log("SampleMaster",data);
								var URI = BASE_URL + ROOT_URL + LIS_UNIT+ADD_MICROBIOLOGY_TEST_MASTER;
								GenericService.serviceAction("POST", URI, data).then(function(response) 
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
										$scope.initMasterList($scope.orgId,$scope.offset, $scope.noOfRecordsPerPage,$scope.orgUnitId);
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
					}
					
					$scope.update = function() 
					{
						$scope.$broadcast('show-errors-check-validity');
						if ($scope.commonform.$valid)
						{
							try {
								$scope.initSampleMaster();
								var date = new Date();
								$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');	
								$rootScope.startLoader();
								var data={
										microTestId:$scope.common.id,
										microTestCode:$scope.common.code,
										microTestDesc:$scope.common.desc,
										updatedBy:$scope.common.updatedBy,
										updatedDate:$scope.dt,
										orgId:$scope.common.orgId,
										orgUnitId:$scope.common.orgUnitId
								};
								
								console.log("SampleMaster",data);
								var URI = BASE_URL + ROOT_URL + LIS_UNIT+UPDATE_MICROBIOLOGY_TEST_MASTER;
								GenericService.serviceAction("POST", URI, data).then(function(response) 
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
										$scope.initMasterList($scope.orgId,$scope.offset, $scope.noOfRecordsPerPage,$scope.orgUnitId);
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
					}
					
				} ]);
