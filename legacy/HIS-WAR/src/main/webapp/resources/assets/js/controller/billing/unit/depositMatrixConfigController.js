/**
 * @author Ganesh Chaudhari
 */

angular
		.module('myApp')
		.controller(
				"depositMatrixConfigController",
				[
						'$scope',
						'$rootScope',
						'$state',
						'$cookies',
						'GenericService',
						'PagerService',
						'promiseFactory',
						'$sessionStorage',
						'growl',
						function($scope, $rootScope, $state, $cookies,
								GenericService, PagerService, promiseFactory,
								$sessionStorage, growl) {

							$rootScope.loginpage = true;
							$scope.LISDynamicLabel = "";
							$scope.saveBtnFlag = true;
							$scope.updateBtnFlag = false;
							$scope.updateScope = false;

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
							$scope.orgUnitId = cookieObject.unitId;
							$scope.orgId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;

							/* code for setting Label */
							$(".selectedPageName").text(
									" Parameter Master");
							
							
							$scope.activePatientType=function(){
								var data = {
										organizationId : $scope.organizationId,
										unitId : $scope.unitId
									}
								var URI = BASE_URL + ROOT_URL
									+ "/global/patientcategory/"+$scope.organizationId;
								GenericService
										.serviceAction("GET", URI, data)
										.then(function(response) {
													$scope.patientTypeList = [];
													if (response.data.status == "success") {
														angular.forEach(response.data.listObject,
																		function(value,index) {
																			var data = {};
																			data.id = value.id;
																			data.label = value.name;
																			$scope.patientTypeList.push(data);
																		});
														console.log('patientTypeList',$scope.patientTypeList);
													}
												});
							}
							$scope.activePatientType();
							
							$scope.getBedCategoryList=function()
							{
								var URI =BASE_URL + ROOT_URL+GETACTIVEBILLINGBEDCATEGORYLIST+"/orgId/"+$scope.organizationId+"/unitId/"+$scope.unitId;
						         GenericService
								.serviceAction("GET", URI, {})
								.then(
										function(response) {
											$scope.bedCategoryList = [];

											if (response.data.status == "success") {
												$scope.bedCategoryList = response.data.listObject;

											}
										});
							}
							$scope.getBedCategoryList();
							
							
							$scope.activePaymentEntitlement=function(){
								var data = {
										organizationId : $scope.organizationId,
										unitId : $scope.unitId
									}
								var URI = BASE_URL + ROOT_URL
								+ GETACTIVEPAYMENTENTITLEMENT;
						GenericService
								.serviceAction("GET", URI, data)
								.then(
										function(response) {
											$scope.paymentEntitlementList = [];

											if (response.data.status == "success") {
												angular.forEach(response.data.listObject,
																function(value,index) {
																	var data = {};
																	data.id = value.paymentEntitlementId;
																	data.label = value.paymentEntitlementDesc;
																	$scope.paymentEntitlementList.push(data);
																});
												console.log('paymentEntitlementList',$scope.paymentEntitlementList);
											}
										});
							}
							$scope.activePaymentEntitlement();
							
							$scope.initDepositMatrixConfig = function()
							{
							   $scope.depositMatrixConfig = {
									  "mDepositMatrixId" :"",
									  "patientTypeId" : "",
									  "paymentEntitlementId" : "",
									  "bedCategoryId" : "",
									  "patientType" : "",
									  "patientEntitleMent" : "",
									  "billingCategory" : "",
									  "againstDeposit" : "",
									  "surgical" : 0,
									  "nonSurgical" : 0,
									  "status" : "A",
									  "organisationId" : $scope.orgId,
									  "unitId" : $scope.unitId,
									  "createdDate" : "",
									  "createdBy" : $scope.userId,
									  "updatedDate" : "",
						    	}
							}
							$scope.initDepositMatrixConfig();
							
							// ====================================================CODE START FORFOR depositMatrix MASTER LIST===========================================================

							$scope.setNoOfRecords = function() {
								$scope.initDepositMatrix($scope.orgId,$scope.orgUnitId, $scope.offset, $scope.noOfRecordsPerPage);
							};
							$scope.initDepositMatrix = function(orgId,unitId, offset, noOfRecordsPerPage)
							{
								$rootScope.startLoader();
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
								var URI1 = LIST_DEPOSIT_MATRIX + S + orgId + S +unitId+S+ offset
										+ S + noOfRecordsPerPage;
								var URI2 = DEPOSIT_MATRIX_COUNT+ S + orgId+S+unitId;
								promiseFactory.setPromises(URI1, URI2, "GET", "GET").then(function(response) 
								{
									$rootScope.stopLoader();
									$scope.commonList = response[0].listObject;
									$scope.commonListCount = response[1].object;
									$scope.setPage(1, false);
									console.log($scope.commonList);
								});
							}

							$scope.getDepositMatrixList = function(orgId,unitId, offset, noOfRecordsPerPage) {
								$rootScope.startLoader();
								console.log("offset", offset);
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
								var URI = LIST_DEPOSIT_MATRIX + S + orgId + S +unitId+S+ offset
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
									$scope.getDepositMatrixList($scope.orgId,$scope.orgUnitId, $scope.pager.startIndex, $scope.pager.pageSize);
								}
							}

							$scope.initDepositMatrix($scope.orgId,$scope.orgUnitId, $scope.offset, $scope.noOfRecordsPerPage);

							// ====================================================CODE END FOR SAMPLE MASTER LIST===========================================================
							
							
							//code for Activating Inactivating Sample
							$scope.updateStatus = function(commonId,status)
							{
								try {
									$rootScope.startLoader();
								
									
									var URI = ACT_INCT_DEPOSIT_MATRIX+S+commonId+S+status;
									console.log("ACT_INACT_URI",URI);
									GenericService.serviceAction("GET", URI, $scope.depositMatrixConfig).then(function(response) 
									{
										if (response.data.status == 'success')
										{
											$rootScope.stopLoader();
											growl.success(response.data.message,
													{
														title : 'Success!'
													});
											$scope.$broadcast('show-errors-reset');
											$scope.initDepositMatrixConfig();
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
									var URI = GET_DEPOSIT_MATRIX+S+commonId;
									console.log("EDIT",URI);
									GenericService.serviceAction("GET", URI, {}).then(function(response) 
									{

										console.log("data",response.data);

										if (response.data.status == 'success')
										{
											$rootScope.stopLoader();
											$scope.depositMatrixConfig = response.data.object;
											console.log("$scope.depositMatrixConfig",$scope.depositMatrixConfig);
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
							
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.save();
							}
							
							$scope.save = function() {
								
								console.log($scope.depositMatrixConfig)
							$scope.$broadcast('show-errors-check-validity');
							 if ($scope.commonform.$valid) {
								try {
									
									if ($scope.popUpFlag) {
										$scope.showModal = !$scope.showModal;
									}
									else{
									 $rootScope.startLoader();
										console.log("depositMatrixConfig",JSON.stringify($scope.depositMatrixConfig));
										var URI = SAVE_DEPOSIT_MATRIX;
										GenericService.serviceAction("POST", URI, $scope.depositMatrixConfig).then(function(response) 
										{	
											if (response.data.status == 'success'){
												
												$rootScope.stopLoader();
												growl.success(response.data.message,{
															title : 'Success!'
														});
												$scope.$broadcast('show-errors-reset');
												$scope.initDepositMatrixConfig();
												$scope.initDepositMatrix($scope.orgId,$scope.orgUnitId, $scope.offset, $scope.noOfRecordsPerPage);
											} else if(response.data.status == 'error'){
												$rootScope.stopLoader();
												growl.error(response.data.message,
														{
															title : 'Error!'
														});
											}
										
										});
									} 
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
										$scope.depositMatrixConfig.organisationId = $scope.orgId;
										$scope.depositMatrixConfig.unitId = $scope.unitId;
										$scope.depositMatrixConfig.updatedBy = $scope.userId;
										console.log("depositMatrixConfig",JSON.stringify($scope.depositMatrixConfig));
										var URI = UPDATE_DEPOSIT_MATRIX;
										GenericService.serviceAction("PUT", URI, $scope.depositMatrixConfig).then(function(response) 
										{
											if (response.data.status == 'success')
											{
												$rootScope.stopLoader();
												growl.success(response.data.message,
														{
															title : 'Success!'
														});
												$scope.$broadcast('show-errors-reset');
												$scope.initDepositMatrixConfig();
												$scope.initDepositMatrix($scope.orgId,$scope.orgUnitId, $scope.offset, $scope.noOfRecordsPerPage);
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