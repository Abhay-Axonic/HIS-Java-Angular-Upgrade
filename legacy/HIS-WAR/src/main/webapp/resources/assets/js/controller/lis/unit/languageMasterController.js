/**
 * @author  Ankit Landge
 */

angular.module('myApp').controller(
		"languageMasterController",
		[ '$scope','$rootScope','$state','$cookies','GenericService','PagerService','promiseFactory','$sessionStorage',
			'growl','GenericServiceFileUpload','reportService',
				function($scope,$rootScope,$state, $cookies, GenericService,PagerService,promiseFactory,$sessionStorage,growl,GenericServiceFileUpload,reportService)
				{
			       
			        $rootScope.loginpage = true;
					$scope.LISDynamicLabel = "Language";
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
					$scope.unitId = cookieObject.unitId;
					$scope.orgId = cookieObject.organizationId;
					$scope.userId = cookieObject.userId;
					
					/* code for setting Label */
					$(".selectedPageName").text("Language Master");
					$state.current.title="Language Master";
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
							 "updatedBy":$scope.userId,
						     "orgId":$scope.orgId,
						     "unitId":$scope.unitId
						}
					}
					$scope.initCommon();
					
					$scope.initlanguageMaster = function(){
					$scope.languageMaster =
					{
						 "languageId":$scope.common.id,
						 "code":$scope.common.code,
						 "desc":$scope.common.desc,
					     "status":$scope.common.status,
						 "createdBy":$scope.common.createdBy,
						 "updatedBy":$scope.common.updatedBy,
					     "orgId":$scope.common.orgId,
					     "unitId":$scope.common.unitId
					 }
					}
					
					// ====================================================CODE START FORFOR LANGUAGE MASTER LIST===========================================================

					$scope.setNoOfRecords = function() {
						$scope.initlanguageMasterList($scope.orgId, $scope.offset, $scope.noOfRecordsPerPage);
					};
					$scope.initlanguageMasterList = function(orgId, offset, noOfRecordsPerPage)
					{
						var data = "";
						$rootScope.startLoader();
						offset = offset != null ? offset : 0;
						noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
						var URI1 = BASE_URL + ROOT_URL + LIS_GLOBAL + "/listLanguageMaster" + S + orgId+S+$scope.unitId+S+offset
								+ S + noOfRecordsPerPage;
						var URI2 = BASE_URL + ROOT_URL + LIS_GLOBAL + "/getTotalLanguageMasterRecord" + S + orgId+S+$scope.unitId;
						promiseFactory.setPromises(URI1, URI2, "GET", "GET").then(function(response) 
						{
							$rootScope.stopLoader();
							$scope.commonList = response[0].listObject;
							$scope.commonListCount = response[1].object;
							$scope.setPage(1, false);
							console.log($scope.commonList);
						});
					}

					$scope.getlanguageMasterList = function(orgId, offset, noOfRecordsPerPage) {
						console.log("offset", offset);
						var data = "";
						$rootScope.startLoader();
						offset = offset != null ? offset : 0;
						noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
						var URI = BASE_URL + ROOT_URL + LIS_GLOBAL + "/listLanguageMaster"+ S + orgId +S+$scope.unitId+ S + offset
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
							$scope.getlanguageMasterList($scope.orgId, $scope.pager.startIndex, $scope.pager.pageSize);
						}
					}

					$scope.initlanguageMasterList($scope.orgId, $scope.offset, $scope.noOfRecordsPerPage);

					// ====================================================CODE END FOR LANGUAGE MASTER LIST===========================================================
					
				
					//code for Activating Inactivating Sample
					$scope.updateStatus = function(commonId,status)
					{
						try {
							$rootScope.startLoader();
							$scope.initlanguageMaster();
							var URI = BASE_URL + ROOT_URL + LIS_GLOBAL+"/activeInactiveLanguage"+S+$scope.orgId+S+commonId+S+status;
							console.log("ACT_INACT_URI",URI);
							GenericService.serviceAction("GET", URI, $scope.languageMaster).then(function(response) 
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
							var URI = BASE_URL + ROOT_URL + LIS_GLOBAL+"/getLanguagebyId"+S+$scope.orgId+S+$scope.unitId+S+commonId;
							console.log("EDIT",URI);
							GenericService.serviceAction("GET", URI, $scope.common).then(function(response) 
							{
								console.log("data",response.data);
								if (response.data.status == 'success')
								{
									$rootScope.stopLoader();
									$scope.common = response.data.object;
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
							try {
								$rootScope.startLoader();
								$scope.initlanguageMaster();
								console.log("languageMaster",JSON.stringify($scope.languageMaster));
								var URI = BASE_URL + ROOT_URL + LIS_GLOBAL+"/addLanguage";
								GenericService.serviceAction("POST", URI, $scope.languageMaster).then(function(response) 
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
										$scope.initlanguageMasterList($scope.orgId, $scope.offset, $scope.noOfRecordsPerPage);
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
								$rootScope.startLoader();
								$scope.initlanguageMaster();
								console.log("languageMaster",JSON.stringify($scope.languageMaster));
								var URI = BASE_URL + ROOT_URL + LIS_GLOBAL+"/updateLanguage";
								GenericService.serviceAction("PUT", URI, $scope.languageMaster).then(function(response) 
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
										$scope.initlanguageMasterList($scope.orgId, $scope.offset, $scope.noOfRecordsPerPage);
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