/**
 * @author Ankit Landge
 */

angular
		.module('myApp')
		.controller(
				"trustMasterController",
				[	    	'$scope',
							'$http',
							'$localStorage',
							'$sessionStorage',
							'$cookies',
							'$rootScope',
							'GenericService',
							'PagerService',
							'promiseFactory',
							'BillingGenericService',
							'GenericServiceFileUpload',
							'reportService',
							'growl',
							'$state',
							'$stateParams',
					function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService,promiseFactory,PagerService,
								BillingGenericService,GenericServiceFileUpload,reportService, growl, $state,
								$stateParams,$modalInstance) {

							$rootScope.loginpage = true;
							$scope.LISDynamicLabel = "Trust Master ";
							$scope.saveBtnFlag = true;
							$scope.updateBtnFlag = false;

							/** Variables for pagination */
							$scope.offset = 0;
							$scope.noOfRecordsPerPage = 10;
							$scope.trustcommonList = [];
							$scope.totalNoOfRecords;
							$scope.searchKeyword;
							$scope.commonListCount;
							
					

							$scope.visitTypeList = [  {
								id : 3,
								label : "Day Care"
							}, {
								id : 4,
								label : "ER"
							}, {
								id : 2,
								label : "IP"
							}, {
								id : 1,
								label : "OP"
							}];
							$scope.selectedVisitType = [];
						
							
							$scope.init = function() {
								var cookieObject = $cookies
								.getObject('cookieObject');
								if (cookieObject == undefined) {
									$state.go('login');
									return;
								}
								
							}
							$scope.init();
							/** * Check user is Loggedin */
							var cookieObject, cookieObjectAppConfig;
			                [cookieObject, cookieObjectAppConfig ] = $scope.checkCookies();
							
						
							$scope.unitId = cookieObject.unitId;
							$scope.orgId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
			              	var data = {};
			              	
			              	
							
						$scope.initCommon = function() {
								
							$scope.saveBtnFlag = true;
							$scope.updateBtnFlag = false;
							
								$scope.trustObj = {
									"trustId" : '',
									"trustName" : '',
									"trustAddress" :'',
									"countryId" :'',
									"stateId":'',
									"districtId" : '',
									"cityId" : '',
									"areaId" : '',
									"zipCode" : '',
									"contactPersonName" :'',
									"contactPersonEmail":'',
									"phoneNo" : '',
									"cellNo" : '',
									"validFrom" : '',
									"patientCategoryId" : '',
									"patientTypeId" : '',
									"disease" :'',
									"maxLimitPerPerson":0,
									"trustStatus" : "A",
									"createdBy" : $scope.userId,
									"createdDate" : "",
									"updatedBy" : $scope.userId,
									"updatedDate" : "",
									"isCompanyContract" :"N",
									"trustCompanyId" :'',
									"orgId" : $scope.orgId,
									"unitId" : $scope.unitId,
									"visitTypeId":[]
								}
								$scope.selectedVisitType = [];
				}
							$scope.initCommon();

							$scope.initTrustMaster = function() {
								$scope.trustMaster = {
										"trustId" :  $scope.trustObj.trustId,
										"trustName" : $scope.trustObj.trustName,
										"trustAddress" : $scope.trustObj.trustAddress,
										"countryId" :$scope.trustObj.countryId,
										"stateId":$scope.trustObj.stateId,
										"districtId" :$scope.trustObj.districtId,
										"cityId" : $scope.trustObj.cityId,
										"areaId" : $scope.trustObj.areaId,
										"zipCode" : $scope.trustObj.zipCode,
										"contactPersonName" :$scope.trustObj.contactPersonName,
										"contactPersonEmail":$scope.trustObj.contactPersonEmail,
										"phoneNo" : $scope.trustObj.phoneNo,
										"cellNo" : $scope.trustObj.cellNo,
										"validFrom" : $scope.trustObj.validFrom,
										"patientCategoryId" : $scope.trustObj.patientCategoryId,
										"patientTypeId" : $scope.trustObj.patientTypeId,
										"disease" :$scope.trustObj.disease,
										"maxLimitPerPerson":$scope.trustObj.maxLimitPerPerson,
										"trustStatus" : "A",
										"trustCompanyId":$scope.trustObj.trustCompanyId,
										"isCompanyContract":$scope.trustObj.isCompanyContract,
										"createdBy" : $scope.userId,
										"createdDate" : "",
										"updatedBy" : $scope.userId,
										"updatedDate" : "",
										"orgId" : $scope.orgId,
										"unitId" : $scope.unitId
								}
							}

							$scope.openDatePicker = function() { 
								$scope.datepicker.opened = true;
							};
							$scope.datepicker = {
									opened : false
								};

							//Date For To Date on Search
							var visitList=[];
							angular.forEach($scope.selectedVisitType,
									function(value,index) {
								     visitList.push(value.id);
									});
							
							
							
							  $scope.getCountryList = function() {
										var data = {};
										
										var URI = BASE_URL + ROOT_URL
										+ GETACTIVECOUNTRYLIST+$scope.orgId;								
								BillingGenericService
								.serviceAction(METHOD_GET, URI, data,
										NOTIFICATION_MSG_STATUS_FALSE)
								.then(
										function(response) {
											console.log(response);
											$scope.countryList = [];
											if (response.data.status == "success")
												$scope.countryList = response.data.listObject;
											console.log($scope.countryList)
										});
									}
							      $scope.getCountryList();
							      
							      
							      //
							    //get calling code by using country id start
									/*$scope.getCallingCode=function(data){
										var URI = BASE_URL + ROOT_URL
										+ GETSINGLECOUNTRY;
										GenericService.serviceAction("POST", URI, data).then(
												function(response) {
													if (response.data.status == "success") {
														//$scope.trustObj.countryCallingCode = response.data.listObject[0].countryCallingCode;

													} else {
														alert("Error!!");
													}
												});
									}*/
									//get calling code by using country id end
									//get nationality code by using country id start
									$scope.getNationalityByContryId=function(data){
										data.organizationId = $scope.orgId;
										var URI = BASE_URL + ROOT_URL
										+ "/adt/getNationalityByCountryId";
										GenericService.serviceAction("POST", URI, data).then(
												function(response) {
													if (response.data.status == "success") {
														console.log("nationality List ",response.data.listObject[0]);
														//$scope.trustObj.nationalityId = response.data.listObject[0].nationalityId;

													} else {
//														alert("Error!!");
													}
												});
									}
									//get nationality code by using country id end
							  	$scope.getStateByCountry = function(id) {
									var data = {
										countryId : id
									};								
								
									$scope.getNationalityByContryId(data);
									var URI = BASE_URL + ROOT_URL
											+ GETSTATELISTBYCOUNTRYID;
									BillingGenericService
									.serviceAction(METHOD_POST, URI, data,
											NOTIFICATION_MSG_STATUS_FALSE)
									.then(
											function(response) {
												//$scope.stateList = [];
												if (response.data.status == "success"){
												
														$scope.stateList = [];
														$scope.stateList = response.data.listObject;
														
												}
											});


								}
							  	//get discrict
							  	$scope.getDistrictByState = function(id,type) {
									var data = {
										stateId : id
									};
									var URI = BASE_URL + ROOT_URL
											+ GETDISTRICTLISTBYSTATEID;
									BillingGenericService
									.serviceAction(METHOD_POST, URI, data,
											NOTIFICATION_MSG_STATUS_FALSE)
									.then(
											function(response) {
												
												if (response.data.status == "success")
												{
													$scope.districtList = [];
													$scope.districtList = response.data.listObject;
													
													}
											});
									

								}
							  	$scope.getCityByDistrict = function(id) {
                                 
									var data = {
										districtId : id
									};
									var URI = BASE_URL + ROOT_URL
											+ GETCITYLISTBYDISTRICTID;
									BillingGenericService
									.serviceAction(METHOD_POST, URI, data,
											NOTIFICATION_MSG_STATUS_FALSE)
									.then(
											function(response) {
												
												if (response.data.status == "success")
													{
													 $scope.cityList = [];
													 $scope.cityList = response.data.listObject;
													
													}
											
											});

								}
							  	
								$scope.getAreaByCity = function(id) {
								
									var data = {
										cityId : id
									};
									var URI = BASE_URL + ROOT_URL
											+ GETAREALISTBYCITY;
									BillingGenericService
									.serviceAction(METHOD_POST, URI, data,
											NOTIFICATION_MSG_STATUS_FALSE)
									.then(
											function(response) {
												
								    	if (response.data.status == "success")
											     {
													$scope.areaList = [];
													 $scope.areaList = response.data.listObject;
													}
												
											});

								}
								 
						$scope.getpatientCategory = function() {
								
								var URI = BASE_URL + ROOT_URL
								+ GETACTIVEPATIENTCATEGORY;
						GenericService
								.serviceAction("GET", URI, data)
								.then(
										function(response) {
											$scope.patientCategoryList = [];

											if (response.data.status == "success") {
												$scope.patientCategoryList = response.data.listObject;
											}
										});
						}
						$scope.getpatientCategory();
						
						
						//save function
						
						
						/* START FORFOR MACHINE MASTER */
					
						$scope.setNoOfRecords = function() {
							$scope.initTrustMasterList($scope.orgId,
									$scope.offset,
									$scope.noOfRecordsPerPage);
						};
						
						
						$scope.getTrustActiveList = function() {

							var URI = BASE_URL+ ROOT_URL+ "/api/billing/getActiveTrustList";
							var data = {
                                          orgId : $scope.orgId ,
									      unitId:$scope.unitId
									    
									   }
							GenericService.serviceAction("POST", URI, data).then(function(response) {

										$rootScope.stopLoader();
										$scope.getTrustActiveList = [];
										$scope.getTrustActiveList = response.data.listObject;
		                                
		                               

									});
						}
						$scope.getTrustActiveList();
						
						
						$scope.setPostCode = function(areaId,flag){
							if(flag==0){
							angular.forEach($scope.areaList,function(value,key){
										
										if(value.areaId==areaId)
										{
											$scope.trustObj.zipCode= value.postCode;
										}
							});
							}
							
						 }
						
						
						$scope.getPatientClassList = function() {
				        var URI = BASE_URL + ROOT_URL +"/api/billing/getPatientType/"+$scope.orgId +"/"+$scope.unitId;
				    GenericService
				        .serviceAction(METHOD_GET, URI,
				                NOTIFICATION_MSG_STATUS_FALSE)
				        .then(
				                function(response) {
				                    if (response.data.status == "success"){
				                        
				                        $scope.patientTypeList = response.data.listObject;
				                        }
				                    
				                });

						}
						$scope.getPatientClassList();
						
						
						$scope.showFieldList = function(id){
							try{
								$scope.fieldMasterList=[];
								$scope.saveBtnFlag = false;
								$scope.updateScope = true;
								$scope.updateBtnFlag = true;
								var data={};
								var URI = BASE_URL + ROOT_URL + "/api/billing/getVisitType" +"/"+ id +"/"+ $scope.orgId +"/"+$scope.unitId;
								
								GenericService.serviceAction("GET",URI,data).then(function(response){
									
									if(response.data.status == "success"){
			 							$scope.fieldMasterList= response.data.listObject;
									}else if(response.data.status == 'error'){
										$rootScope.stopLoader();
										growl.error(response.data.message,
												{
													title : 'Error!'
												});
									}
								});
								
							}catch(e){
								console.log("Exception-",e);
							}
						}
						
						
						
						
						$scope.initTrustMasterList=function(orgId,offset, noOfRecordsPerPage){
							offset = offset != null ? offset : 0;
							noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage:10;
							var data1 = {
									offset : offset,
									noOfRecordsPerPage : noOfRecordsPerPage,
								    orgId : $scope.orgId ,
							        unitId:$scope.unitId
							};

							var data2 = {
                                     orgId : $scope.orgId ,
								      unitId:$scope.unitId
								    
								   }
							$rootScope.startLoader();
							offset = offset != null ? offset : 0;
							noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
									: 10;
							
							var URI1 = BASE_URL + ROOT_URL + API
					        + BILLING
							+ LIST_TRUST_MASTER;
					var URI2 = BASE_URL + ROOT_URL + API
					        + BILLING
					        + COUNT_TRUST_MASTER;
					GenericService.serviceAction("POST", URI1,data1)
					.then(function(response) {
						if (response.data.status == "success") {
							$rootScope.stopLoader();
							$scope.commonList = response.data.listObject;
						}
					});
					GenericService.serviceAction("POST", URI2,data2)
					.then(function(response) {
						if (response.data.status == "success") {
							var trustcommonList=[];
							$rootScope.stopLoader();
							$scope.trustcommonList = response.data.listObject;
							console.log("commonList",$scope.trustcommonList);
						}
					});
				}
						
						$scope.setNoOfRecords = function() {
							$scope.initTrustMasterList($scope.orgId ,$scope.offset, $scope.noOfRecordsPerPage);
						};
						
						$scope.initTrustMasterList($scope.orgId ,$scope.offset, $scope.noOfRecordsPerPage);
					
						$scope.pager = {};
						$scope.page;

						$scope.setPage = function(page, flag) {
							if (page < 1 || page > $scope.pager.totalPages)
							{
								return;
							}
							$scope.pager = PagerService.GetPager($scope.trustcommonList, page, $scope.noOfRecordsPerPage);
							if (flag)
							{
								$scope.getWrapperExMstlist($scope.orgId ,$scope.pager.startIndex, $scope.pager.pageSize);
							}
						}
						// End of Vaccination Name
						

						// END FOR MACHINE MASTER

						/* code for Activating Inactivating Status */
						$scope.updateStatus = function(commonId, status) {
							try {
								$rootScope.startLoader();
								$scope.initTrustMaster();
								var URI = BASE_URL + ROOT_URL + API
								        + BILLING
                                        + ACTIVE_INACTIVE_TRUST_MASTER
										+ S + $scope.orgId + S + commonId
										+ S + status;
								
			           	GenericService.serviceAction("GET", URI,
			           			$scope.trustObj)
						.then(
								function(response) {
									if (response.data.status == 'success') {
										$rootScope.stopLoader();
										growl.success("Updated successfully",
												{
													title : 'Success!'
												});
										$scope.$broadcast('show-errors-reset');
										$scope.initCommon();
										$scope.initTrustMasterList($scope.orgId,
												$scope.offset,
												$scope.noOfRecordsPerPage);
									} else if (response.data.status == 'error') {
										$rootScope.stopLoader();
										growl.error(response.data.message,
														{
															title : 'Error!'
														});
										$scope.initTrustMasterList($scope.orgId,
												$scope.offset,
												$scope.noOfRecordsPerPage);
									}
								});
							} catch (e) {
								console.log("Exception", e.message);
							}

						}

						$scope.showUpdateBtn = function(commonId) {
							try {
								$rootScope.startLoader();
								$scope.saveBtnFlag = false;
								$scope.updateBtnFlag = true;
								var URI = BASE_URL + ROOT_URL + API + BILLING
										+ EDIT_TRUST_MASTER + S
										+ $scope.orgId + S + $scope.unitId
										+ S + commonId;
								GenericService.serviceAction("GET", URI,$scope.common)
										.then(function(response) {
										if (response.data.status == 'success') {
											$rootScope.stopLoader();
											$scope.getStateByCountry(response.data.object.countryId);
					                    	$scope.getDistrictByState(response.data.object.stateId);
					                    	$scope.getCityByDistrict(response.data.object.districtId);
					                    	$scope.getAreaByCity(response.data.object.cityId);
					                        $scope.trustObj = response.data.object;
					                        $scope.trustObj.validFrom= new Date(response.data.object.startDate);
					                        
											$scope.selectedVisitType=response.data.listObject;
									 
										} else if (response.data.status == 'error') {
											$rootScope
													.stopLoader();
											alert("Error In Fetching Data");
										}
									});
							} catch (e) {
								console.log("Exception", e.message);
							}

						}

				
						 $scope.saveTrust = function() {
							 
							// $scope.trustObj.validFrom=$scope.datepickerFromDate;
						
							// var visitTypeId=[];
							 $scope.trustObj.visitTypeId =$scope.selectedVisitType;
							 console.log("visit1  "+  $scope.trustObj.visitTypeId);
						     $scope.$broadcast('show-errors-check-validity');
								console.log("Trust Master",JSON.stringify($scope.trustObj));
								
								if ($scope.commonform.$valid) {
									try {
										$rootScope.startLoader();
										
										var URI = BASE_URL + ROOT_URL
										+ "/api/billing/addTrust";
										GenericService.serviceAction("POST", URI,
												$scope.trustObj)
										.then(
												function(response) {
													if (response.data.status == 'success') {
														$rootScope.stopLoader();
														growl.success("trust save successfully",
																	{
																		title : 'Success!'
																	});
													$scope.$broadcast('show-errors-reset');
													$scope.initCommon();
													$scope.initTrustMasterList($scope.orgId ,$scope.offset, $scope.noOfRecordsPerPage);
												} else if (response.data.status == 'error') {
													$rootScope.stopLoader();
													growl.error(
																	response.data.message,
																	{
																		title : 'Error!'
																	});
												}

											});
									} catch (e) {
										console.log("Exception", e.message);
									}

					       }
							}
					$scope.update = function() {
							$scope.$broadcast('show-errors-check-validity');
							 $scope.trustObj.visitTypeId =$scope.selectedVisitType;
							 $scope.trustObj.orgId = $scope.orgId;
							 $scope.trustObj.unitId = $scope.unitId;
							 $scope.trustObj.createdBy= $scope.userId;
							 $scope.trustObj.updatedBy=$scope.userId;
							 console.log("visit1  "+  $scope.trustObj);
						if ($scope.commonform.$valid) {
								try {
									$rootScope.startLoader();
									
									console.log("MachineMaster",JSON.stringify($scope.trustObj));
									var URI = BASE_URL + ROOT_URL
									      + "/api/billing/updateTrustMaster";
									GenericService
											.serviceAction("PUT", URI,
													$scope.trustObj)
							.then(function(response) {
										if (response.data.status == 'success') {
											$rootScope.stopLoader();
											growl.success("Trust Update successfully",
															{
																title : 'Success!'
															});
											$scope.initTrustMaster();
											$scope.$broadcast('show-errors-reset');
											$scope.initCommon();
											$scope.initTrustMasterList($scope.orgId ,$scope.offset, $scope.noOfRecordsPerPage);
										} else if (response.data.status == 'error') {
											$rootScope.stopLoader();
											growl.error(response.data.message,
															{
																title : 'Error!'
															});
										}
									});
								} catch (e) {
									
								}

							}
								}
			} ]);