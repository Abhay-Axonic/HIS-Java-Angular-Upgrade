/**
 * @author Ankit Landge
 */

angular
		.module('myApp')
		.controller(
				"genericScheduleMasterController",
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
						'GenericServiceFileUpload',
						'reportService',
						function($scope, $rootScope, $state, $cookies,
								GenericService, PagerService, promiseFactory,
								$sessionStorage, growl,
								GenericServiceFileUpload, reportService) {

							$rootScope.loginpage = true;
							$scope.LISDynamicLabel = "Schedule Description ";
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

							/* code for setting Label */
							$(".selectedPageName").text("Schedule Master");

							$scope.initCommon = function() {
								$scope.saveBtnFlag = true;
								$scope.updateBtnFlag = false;
								$scope.genericScheOBj = {
									"genericScheduleId" : "",
									"genericId" : "",
									"scheduleId" : "",
									"serviceMasterId" : "",
									"isEmergencyMedicine" : "",
									"isInvestgation" : "",
									"status" : "A",
									"createdBy" :$scope.userId,
									"createdDate" : "",
									"updatedBy" : $scope.userId,
									"updatedDate" : "",
									"orgId" : $scope.orgId,
									"unitId" : $scope.unitId,
									"serMstrId":"",
									"serviceCodeId":[]
								}
							}
							$scope.initCommon();
							//For Speciality
							
							 $scope.example14model = [];
							    $scope.example14settings = {
							        scrollableHeight: '200px',
							        scrollable: true,
							        enableSearch: true
							    };
							    
							  //For Specialityc Scheduling
								 $scope.example14modelSpecialty = [];
								    $scope.example14settingsSpecialty = {
								        scrollableHeight: '200px',
								        scrollable: true,
								        enableSearch: true
								    };
							
								   
						
						 $scope.getlistServiceCode = function() {
							 
								    var URI = BASE_URL + ROOT_URL + PHARMACY_GLOBAL
									 +"/listserviceCode/"+$scope.orgId +"/"+$scope.unitId;	
								    
								    return GenericService
									.serviceAction(METHOD_GET, URI, "",
											NOTIFICATION_MSG_STATUS_FALSE)
									.then(
											function(response) {
												console.log(response);
											if (response.data.status == "success")
													{
													$scope.example14data = [];
													 $scope.example14dataSpecialty = [];
													angular.forEach(response.data.listObject,function(value,index) {
																
														var data = {};
														data.id=value.serviceMasterId;
														data.label=value.serviceStandardName;
															$scope.example14dataSpecialty.push(data);
																
															});
													}
											});
								    }
								    $scope.getlistServiceCode();
							$scope.initGenericScheduleMaster = function() {
								$scope.genericScheduleOBj = {
										"genericScheduleId" : $scope.genericScheOBj.genericScheduleId,
										"genericId" : $scope.genericScheOBj.genericId,
										"scheduleId" : $scope.genericScheOBj.scheduleId,
										"serviceMasterId":$scope.genericScheOBj.serviceMstrId,
										"isEmergencyMedicine" :$scope.genericScheOBj.isEmergencyMedicine,
										"isInvestgation" :$scope.genericScheOBj.isInvestgation,
										"status" : $scope.genericScheOBj.status,
										"createdBy" : $scope.genericScheOBj.createdBy,
										"createdDate" :$scope.genericScheOBj.createdDate,
										"updatedBy" : $scope.genericScheOBj.updatedBy,
										"updatedDate" : $scope.genericScheOBj.updatedDate,
										"orgId" : $scope.genericScheOBj.orgId,
										"unitId" : $scope.genericScheOBj.unitId,
										"serviceCodeId":$scope.genericScheOBj.serviceCodeId,
								}
							}
						
							$scope.getScheduleList = function() {
								var data = {};
								var URI = BASE_URL + ROOT_URL + PHARMACY_GLOBAL
								 +"/listScheduleDesc/"+$scope.orgId +"/"+$scope.unitId;							
								GenericService.serviceAction(METHOD_GET, URI, data,
								NOTIFICATION_MSG_STATUS_FALSE)
					              	.then(
								function(response) {
									console.log(response);
									$scope.scheduleList = [];
									if (response.data.status == "success")
										$scope.scheduleList = response.data.listObject;
									console.log($scope.scheduleList)
								});
							}
					      $scope.getScheduleList();
					      
					 	$scope.getGenericList = function() {
							var data = {};
							
							var URI = BASE_URL + ROOT_URL
							 +"/api/globals/generics/"+$scope.orgId;							
							GenericService.serviceAction(METHOD_GET, URI, data,
							NOTIFICATION_MSG_STATUS_FALSE)
				              	.then(
							function(response) {
								$scope.genericList = [];
								$scope.genericList = response.data;
									
							});
						}
				      $scope.getGenericList();
				      $scope.close = function(id){
				    	  $scope.saveBtnFlag = true;
							$scope.updateScope = false;
							$scope.updateBtnFlag = false;
				      }
				      $scope.showFieldList = function(id){
				    	  console.log(id)
							try{
								$scope.fieldMasterList=[];
							//	$scope.saveBtnFlag = false;
							//	$scope.updateScope = true;
							//	$scope.updateBtnFlag = true;
								var data={};
								var URI = BASE_URL + ROOT_URL + PHARMACY_GLOBAL
								+ "/getServicecodeList/"+ id +"/"+ $scope.orgId +"/"+$scope.unitId;
								
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
				      /*$scope.searchServices = function(searchKeyword) {
				    	  console.log(searchKeyword)
							$scope.servicesList = [];
							searchKeyword = searchKeyword
									+ "---"
									
							var URI = BASE_URL + ROOT_URL+ PHARMACY_GLOBAL
									+ "/serviceInvestigation/"
									+ searchKeyword + "/"+
									+ $scope.unitId +"/"+
									+ $scope.orgId;
							return GenericService
									.serviceAction(METHOD_GET, URI, "",
											NOTIFICATION_MSG_STATUS_FALSE)
									.then(
											function(response) {
												if (response.data.status == "success") {
													$scope.servicesList = response.data.listObject;
													
												}
												return $scope.servicesList;
											});
						}
				      
				      $scope.addServiceToTempServiceList = function($item, $model, $label){
				    		console.log($item);
				    		console.log($item.serviceMasterId)
				    		
				    		$scope.serviceMasterId = $item.serviceMasterId;
				    		console.log($scope.serviceMasterId)
				    		$scope.serviceStandardName = $item.serviceStandardName;
				    		$scope.genericScheOBj.serviceMstrId =  $item.serviceMasterId;
				    		
				    		console.log($scope.serviceMasterId)
				    		
				    	}*/
				       /* ------GENERIC SCHEDULE MASTER------*/
				      

						$scope.setNoOfRecords = function() {
							$scope.initGenericScheduleMasterList($scope.orgId,
									$scope.offset,
									$scope.noOfRecordsPerPage);
						};
						$scope.initGenericScheduleMasterList = function(orgId,
								offset, noOfRecordsPerPage) {
							var data = "";
							$rootScope.startLoader();
							offset = offset != null ? offset : 0;
							noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
									: 10;
							var URI1 = BASE_URL + ROOT_URL + PHARMACY_GLOBAL
									+ "/listGenericScheduleMaster" + S + $scope.orgId + S
									+ $scope.unitId + S + offset + S
									+ noOfRecordsPerPage;
							var URI2 = BASE_URL + ROOT_URL + PHARMACY_GLOBAL
									+ "/getTotalGenericScheduleMasterRecord" + S + $scope.orgId + S
									+ $scope.unitId;
							promiseFactory
									.setPromises(URI1, URI2, "GET", "GET")
									.then(
											function(response) {
												$rootScope.stopLoader();
												$scope.commonList = response[0].listObject;
												$scope.commonListCount = response[1].object;
												$scope.setPage(1, false);
												console.log($scope.commonList);
												
											});
						}

						$scope.getGenericScheduleMasterList = function(orgId,
								offset, noOfRecordsPerPage) {
							var data = "";
							$rootScope.startLoader();
							offset = offset != null ? offset : 0;
							noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
									: 10;
							var URI = BASE_URL + ROOT_URL + PHARMACY_GLOBAL
									+ "/listGenericScheduleMaster" + S + $scope.orgId + S
									+ $scope.unitId + S + offset + S
									+ noOfRecordsPerPage;
							GenericService
									.serviceAction("GET", URI, data)
									.then(function(response) {
												$rootScope.stopLoader();
												$scope.commonList = response.data.listObject;
												console.info("LIST "+ $scope.commonList)
											});
						}

						$scope.pager = {};
						$scope.page;

						$scope.setPage = function(page, flag) {
							if (page < 1 || page > $scope.pager.totalPages) {
								return;
							}
							$scope.pager = PagerService.GetPager(
									$scope.commonListCount, page,
									$scope.noOfRecordsPerPage);
							if (flag) {
								$scope.getGenericScheduleMasterList($scope.orgId,
										$scope.pager.startIndex,
										$scope.pager.pageSize);
							}
						}

						$scope.initGenericScheduleMasterList($scope.orgId,
								$scope.offset, $scope.noOfRecordsPerPage);
						// END FOR SEROLOGY MASTER

						/* code for Activating Inactivating Status */
						$scope.updateStatus = function(commonId, status) {
							try {
								$rootScope.startLoader();
								$scope.initGenericScheduleMaster();
								var URI = BASE_URL + ROOT_URL + PHARMACY_GLOBAL
										+ "/activeInactiveGenericSchedule"
										+ S + $scope.orgId + S + commonId
										+ S + status;
								console.log("ACT_INACT_URI", URI);
								console.info($scope.genericScheOBj)
								GenericService
										.serviceAction("GET", URI,
												$scope.genericScheOBj)
										.then(
												function(response) {
													if (response.data.status == 'success') {
														$rootScope.stopLoader();
														growl.success("Status Update successfully",
																		{
																			title : 'Success!'
																		});
														$scope.$broadcast('show-errors-reset');
														$scope.initCommon();
													} else if (response.data.status == 'error') {
														$rootScope
																.stopLoader();
														growl
																.error(response.data.message,
																		{
																			title : 'Error!'
																		});
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
								var URI = BASE_URL + ROOT_URL + PHARMACY_GLOBAL
										+ "/getGenericScheduleMasterById" + S
										+ $scope.orgId + S + $scope.unitId
										+ S + commonId;
								console.log("EDIT", URI);
								GenericService
										.serviceAction("GET", URI,
												$scope.genericScheOBj)
										.then(
												function(response) {
													console.log("data",
															response.data);
													if (response.data.status == 'success') {
														$rootScope.stopLoader();
														$scope.genericScheOBj = response.data.object;
												console.log(response.data.object)
												$scope.genericScheOBj.serMstrId=response.data.object.serviceMasterId
												$scope.genericScheOBj.serviceMasterId=response.data.object.serviceStandardName
												$scope.example14modelSpecialty=response.data.listObject;
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
						//SAVE GENERIC SCHEDULE MASTER?//
				  	$scope.save = function() {
				  	
						$scope.$broadcast('show-errors-check-validity');
						if ($scope.commonform.$valid) {

						    if($scope.genericScheOBj.isInvestgation !== undefined && $scope.genericScheOBj.isInvestgation !== '' && $scope.genericScheOBj.isInvestgation == 'Y'){
    						    $scope.genericScheOBj.serviceCodeId =$scope.example14modelSpecialty;
						    }
						    if($scope.genericScheOBj.isInvestgation === undefined || $scope.genericScheOBj.isInvestgation === '' || $scope.genericScheOBj.isInvestgation === 'N'){
                                $scope.example14modelSpecialty = [];
                                $scope.genericScheOBj.serviceCodeId = [];
                            }

                            console.log("test"+ JSON.stringify($scope.genericScheOBj.serviceCodeId));
                            try {
                                $rootScope.startLoader();
                                $scope.initGenericScheduleMaster();
                                console.log("Generic schedule Master",
                                                JSON.stringify($scope.genericScheduleOBj));
                                var URI = BASE_URL + ROOT_URL
                                        + PHARMACY_GLOBAL
                                        + "/saveGenericScheduleMaster";
                                GenericService
                                        .serviceAction("POST", URI,
                                                $scope.genericScheduleOBj)
                                        .then(
                                            function(response) {
                                                console.info(response.data.status)
                                                if (response.data.status == 'success') {
                                                    $rootScope.stopLoader();
                                                    growl.success("Generic Schedule Added Successfully",
                                                                    {
                                                                        title : 'Success!'
                                                                    });
                                                    $scope.$broadcast('show-errors-reset');
                                                    $scope.common='';
                                                    $scope.initCommon();
                                                    //$scope.genericScheOBj='';
                                                    $scope.example14modelSpecialty = [];
                                                    $scope.initGenericScheduleMasterList($scope.orgId,
                                                            $scope.offset,
                                                            $scope.noOfRecordsPerPage);

                                                } else if (response.data.status == 'error') {
                                                    $rootScope
                                                            .stopLoader();
                                                    growl.error(response.data.message,
                                                    {
                                                        title : 'Error!'
                                                    });
                                                }

                                            });
                            } catch (e) {
                                console.log("Exception", e.message);
                            }
						}
						else{
						    growl.warning("Please enter all mandatory fields",{
                                title : "warning"
                            });
						}
					}
					$scope.update = function() {
						$scope.$broadcast('show-errors-check-validity');
						console.log($scope.genericScheOBj.serMstrId)
						var serviceMasterId='';
						$scope.genericScheOBj.serviceMasterId=$scope.genericScheOBj.serMstrId
						$scope.genericScheOBj.createdBy=$scope.userId;
						$scope.genericScheOBj.updatedBy=$scope.userId;
						$scope.genericScheOBj.serviceCodeId =$scope.example14modelSpecialty;
						try {
								$rootScope.startLoader();
								$scope.initGenericScheduleMaster();
								console.log("HeaderMaster",JSON.stringify($scope.genericScheOBj));
								var URI = BASE_URL + ROOT_URL
										+ PHARMACY_GLOBAL
										+ "/updateGenericScheduleMaster";
								GenericService
										.serviceAction("PUT", URI,
												$scope.genericScheOBj)
										.then(
												function(response) {
													if (response.data.status == 'success') {
														$rootScope.stopLoader();
														growl.success("Generic Schedule Update Successfully"	,
																		{
																			title : 'Success!'
																		});
														$scope.$broadcast('show-errors-reset');
														$scope.initCommon();
														 $scope.example14modelSpecialty = [];
														 $scope.saveBtnFlag = true;
															$scope.updateBtnFlag = false;
														$scope.initGenericScheduleMasterList($scope.orgId,
																$scope.offset,
																$scope.noOfRecordsPerPage);
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

						} ]);