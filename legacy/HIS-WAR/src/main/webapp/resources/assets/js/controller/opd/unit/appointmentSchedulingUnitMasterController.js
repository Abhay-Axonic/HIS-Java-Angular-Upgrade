'use strict';

/**
 * @Author By  Vivek Satle
 * @name myApp.controller:appointmentSchedulingUnitMasterController
 * @description #appointmentSchedulingUnitMasterController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'appointmentSchedulingUnitMasterController',
				[
						'$scope',
						'$http',
						'$localStorage',
						'$sessionStorage',
						'$cookies',
						'$rootScope',
						'GenericService',
						'BillingGenericService',
						'growl',
						'$state',
						'$stateParams',
						'PagerService',
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService,
								BillingGenericService, growl, $state,
								$stateParams,PagerService) {

							/* init() function for form object create. */
							
							
							
							// Loop DatePicker End
							var cookieObject = $cookies
									.getObject('cookieObject');
							if (cookieObject == undefined) {
								$state.go('login');
								return;
							}
							
							var cookieObjectAppConfig = $cookies
							.getObject('cookieObjectAppConfig');
							if (cookieObjectAppConfig == undefined) {
								$state.go('login');
								return;
							}
							$scope.cookieObject =$cookies.getObject('cookieObject');
							$scope.radiologySpecialtyId = cookieObjectAppConfig.radiologySpecialityId;
							$scope.unitId = cookieObject.unitId;
							$scope.organizationId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							$scope.nephrologySpecialityId = cookieObjectAppConfig.hemodialysisSpecialityId;
							$rootScope.loginpage = true;
							$scope.saveBtnFlag = false;
							$scope.updateBtnFlag = true;

							// for popup
							$scope.showModal = false;
							$scope.buttonClicked = "";
							$scope.popUpFlag = true;
							// end for popup
							
							$scope.init = function() {
								
								
								/*var a = "12:30";
								var b = "01:00";
								alert(parseInt(a) + parseInt(b) );*/
								
								$scope.dataList = [ {
									fromDate : '',
									toDate : '',
									fromTime : '',
									toTime : '',
									timePerSlot : '',
									visitTypeId :1,
									noOfSlots:0,
									example8model:[],
									breakTimeList:[{breakFromTime:'',breakToTime:''}]
								} ];
								
								
								//Loop DatePicker Start
								// for datePicker
								var today = new Date(); // Get the current date
								  var maxDate = new Date(today.getTime() + (365 * 24 * 60 * 60 * 1000));
								var today = new Date();
								$scope.dateOptions = {
								 // Get the current date
								//var maxDate = new Date(today.getTime() + (365 * 24 * 60 * 60 * 1000));
									formatYear : 'yyyy',
									showWeeks : false,
									minDate : today,
									maxDate : new Date(today.getTime() + (365 * 24 * 60 * 60 * 1000)),
								};
								
								
								$scope.datepicker = {
										opened : false
									};
									$scope.openDt = function() {
										$scope.popup.opened = true;
									};

									$scope.popup =
									{
										opened : false
									};
									
								
								//for From Date
								$scope.openDatePicker = function(index) {
								//$scope.fromDate.opened = true;
                                //self.model.dateOptions.minDate = $scope.data.fromDate;

									for (var i = $scope.dataList.length - 1; i >= 0; i--) {
										if (i === index) {

											$scope.fromDate.opened[i] = true;
											$scope.dateOptions.minDate = $scope.dataList[i].fromDate;
										} else {
											$scope.fromDate.opened[i] = false;
										}
									}
								};

								$scope.fromDate = {

									opened : [ false ],

								};
								//End From Date
								
								//for To Date
								$scope.openDatePickerTo = function(index) {
                                //$scope.toDateOpened = true;
                               // self.model.dateOptions.minDate = $scope.data.fromDate;
									for (var i = $scope.dataList.length - 1; i >= 0; i--) {
										if (i === index) {
											$scope.toDate.opened[i] = true;
											$scope.dateOptions.minDate = $scope.dataList[i].fromDate;
										} else {
											$scope.toDate.opened[i] = false;
										}
									}
								};

								$scope.toDate = {

									opened : [ false ],

								};

								//End To Date
								
								
								/** Variables for pagination */
								$scope.offset = 0;
								$scope.noOfRecordsPerPage = 10;
								$scope.totalNoOfRecords;
								$scope.searchKeyword=null;
								$scope.commonListCount;
								$scope.commonListHemoCount;
								
					
								
								
								
								var commonObj = {
									unitId : $scope.unitId,
									organizationId : $scope.organizationId
								};
								var data = {};
								$scope.override = 'N'
								
							
								 //For Speciality
								 $scope.example14model = [];
								 $scope.example14modelUnit = [];
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
									    
									   
									   /* $scope.getAssetList = function() {
											var data = {};
											
											var URI = BASE_URL + ROOT_URL
											+ "/api/opd/getAsset";								
									BillingGenericService
									.serviceAction(METHOD_GET, URI, data,
											NOTIFICATION_MSG_STATUS_FALSE)
									.then(
											function(response) {
												//console.log(response);
												$scope.assetList = [];
												if (response.data.status == "success")
													$scope.assetList = response.data.listObject;
												//console.log($scope.countryList)
											});
										}
								      $scope.getAssetList();*/
								      
								      $scope.getDialysisChairList = function() {
											var data = {
													unitId : $scope.unitId,
													organizationId : $scope.organizationId
											};
											//console.log(data);
											var URI = BASE_URL + ROOT_URL
											+ "/api/adt/getDialysisChair";								
											GenericService
											.serviceAction("POST", URI, data)
									.then(
											function(response) {
												//console.log(response);
												$scope.chairList = [];
												if (response.data.status == "success")
													$scope.chairList = response.data.listObject;
												//console.log($scope.chairList)
											});
										}
								      $scope.getDialysisChairList();
								   /* $scope.example14data = [{
								        "label": "Cardiology",
								            "id": "1"
								    }, {
								        "label": "Dental",
								            "id": "2"
								    }, {
								        "label": "Radiology",
								            "id": "3"
								    }];*/
								    var URI = BASE_URL + ROOT_URL
												+ GETACTIVESPECIALITY;
										BillingGenericService
												.serviceAction(METHOD_POST, URI,
														commonObj,
														NOTIFICATION_MSG_STATUS_FALSE)
												.then(
														function(response) {
															//console.log(response);
															// $rootScope.startLoader();
															if (response.data.status == "success")
																{
																$scope.example14data = [];
																$scope.example14dataSpecialty = [];
																angular.forEach(response.data.listObject,function(value,index) {
																			
																	var data = {};
																	data.id=value.specialityId;
																	data.label=value.specialityName;
																	
																			if(value.deptWiseAppointment == 'Y' && value.isClinicalSpeciality == 'Y')
																				{
																				$scope.example14dataSpecialty.push(data);
																				}
																			else if(value.deptWiseAppointment == 'N' && value.isClinicalSpeciality == 'Y')
																				{
																				 $scope.example14data.push(data);
																				}
																			
																			
																		
																		});
																
																
																	 
																}
																
														});
										
										
										 //For Speciality under modality
										 $scope.example14ModalityType = [];
										 $scope.example14SubSpeciality=[];
										   
										 $scope.specialityList = [];
										    var URI = BASE_URL + ROOT_URL
														+ GETACTIVESPECIALITY;
												BillingGenericService
														.serviceAction(METHOD_POST, URI,
																commonObj,
																NOTIFICATION_MSG_STATUS_FALSE)
														.then(
																function(response) {
																	//console.log(response);
																	// $rootScope.startLoader();
																	if (response.data.status == "success")
																		{
																		 $scope.specialityList =response.data.listObject;
																		 //console.log($scope.specialityList)
																	/*	 $scope.example14ModalityTypeData = [];
																		angular.forEach(response.data.listObject,function(value,index) {
																			if(value.specialityId == $scope.radiologySpecialtyId){
																					var data = {};
																					data.id=value.specialityId;
																					data.label=value.specialityName;
																					$scope.example14ModalityTypeData.push(data);
																			}
																				
																				});*/
																			 
																		}
																		
																});
												
												  var URI = BASE_URL + ROOT_URL + "/adt/getActiveSpecialityListForModality";
											BillingGenericService.serviceAction(METHOD_POST, URI,commonObj,NOTIFICATION_MSG_STATUS_FALSE)
													.then(function(response) {																
																if (response.data.status == "success")
																	{
																	 $scope.example14ModalityTypeData = [];
																	 angular.forEach(response.data.listObject,function(value,index) {
																				var data = {};
																				data.id=value.specialityId;
																				data.label=value.specialityName;
																				$scope.example14ModalityTypeData.push(data);
																			
																			});
																		 
																	}
																	
															});
										
										
										
										
								    $scope.example2settings = {
								        displayProp: 'id'
								    };
								    
								    //For Doctor
								    $scope.example14modelDoctor = [];
								    $scope.example2settingsDoctor = {
								        displayProp: 'id'
								    };
								    
								    //For Modality
								    $scope.example14modelModality = [];
								    $scope.example2settingsModality = {
								        displayProp: 'id'
								    };
								    
								
								//Testing pouspose End
								
							}
							$scope.slotTypeId  = 1;
							$scope.deptWise = 'N';
							$scope.init();
							
							
							
							// For Paginations Start
							$scope.setNoOfRecords = function() {
								$scope.initDoctorSlotList(
										$scope.organizationId, $scope.offset,
										$scope.noOfRecordsPerPage);
							};
							$scope.initDoctorSlotList = function(orgId,
									offset, noOfRecordsPerPage) {
								$scope.doctorSlotList = [];
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
										: 10;
								var URI = BASE_URL + ROOT_URL
										//+ GETALLSLOTS;
								    +GETALLSLOTSNEW;
								var URI1 = BASE_URL + ROOT_URL
										+ GETALLSLOTSCOUNT;

								var data = {
									organizationId : orgId,
									unitId : $scope.unitId,
									slotTypeId : $scope.slotTypeId,
									offset : offset,
									noOfRecordsPerPage : noOfRecordsPerPage,
									searchKeyword : ($scope.searchKeyword != null) ? $scope.searchKeyword : null
								}

								var data1 = {
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									slotTypeId : $scope.slotTypeId,
									searchKeyword : ($scope.searchKeyword != null) ? $scope.searchKeyword : null
								}
								data.deptWiseAppointment=$scope.deptWise;
								data1.deptWiseAppointment=$scope.deptWise;
								$rootScope.startLoader();
								
								// For Get Nationality List Using Offset
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													$scope.doctorSlotList = [];

													if (response.data.status == "success") {
														$scope.doctorSlotList = response.data.listObject;
														angular.forEach($scope.doctorSlotList,function(value) {
                                                            var currentDate =  new Date();
                                                            if(value.lastSlotDate != undefined && value.lastSlotDate != null && value.lastSlotDate != ""){

                                                            var parts = value.lastSlotDate.split('/');
                                                            var day = parts[0];
                                                            var month = parts[1];
                                                            var year = parts[2];

                                                            var formattedDate = new Date(month + '/'+ day +'/'+ year);
                                                            if(currentDate > (formattedDate))
                                                            {
                                                                if(!(currentDate.toDateString() === formattedDate.toDateString())){
                                                                    value.lastSlotDate = "";
                                                                }
                                                            }
                                                           }
                                                        });
													}
												});

								// For Count Slot List
								GenericService
										.serviceAction("POST", URI1, data1)
										.then(
												function(response) {
													if (response.data.status == "success") 
													{
														console
																.log(response.data);
														$scope.commonListCount = response.data.object;
														$scope
																.setPage(1,
																		false);
														$rootScope.stopLoader();

													}
												});
							}


                            $scope.searchSchedulingUnitMaster = function(searchKeyword){

                                $scope.loadingMsg="Loading...";
                                if(searchKeyword.length>0){

                                    $scope.doctorSlotList=[];
                                    $rootScope.startLoader();
                                    var URI = BASE_URL + ROOT_URL
                                        +GETALLSLOTSNEW;
                                    var URI1 = BASE_URL + ROOT_URL
                                            + GETALLSLOTSCOUNT;

                                    var data = {
                                        organizationId : $scope.organizationId,
                                        unitId : $scope.unitId,
                                        slotTypeId : $scope.slotTypeId,
                                        offset : $scope.offset,
                                        noOfRecordsPerPage : $scope.noOfRecordsPerPage,
                                        searchKeyword : searchKeyword
                                    }

                                    var data1 = {
                                        organizationId : $scope.organizationId,
                                        unitId : $scope.unitId,
                                        slotTypeId : $scope.slotTypeId,
                                        searchKeyword : searchKeyword
                                    }

                                    data.deptWiseAppointment=$scope.deptWise;
                                    data1.deptWiseAppointment=$scope.deptWise;
                                    $rootScope.startLoader();

                                    // For Get Nationality List Using Offset
                                    GenericService
                                            .serviceAction("POST", URI, data)
                                            .then(
                                                    function(response) {
                                                        $scope.doctorSlotList = [];

                                                        if (response.data.status == "success") {
                                                            $scope.doctorSlotList = response.data.listObject;

                                                        }
                                                    });

                                    // For Count Slot List
                                    GenericService
                                            .serviceAction("POST", URI1, data1)
                                            .then(
                                                    function(response) {
                                                        if (response.data.status == "success")
                                                        {
                                                            console
                                                                    .log(response.data);
                                                            $scope.commonListCount = response.data.object;
                                                            $scope
                                                                    .setPage(1,
                                                                            false);
                                                            $rootScope.stopLoader();

                                                        }
                                                    });
                                }
                                if(searchKeyword == undefined || searchKeyword == null || searchKeyword ==""){
                                      $scope.initDoctorSlotList($scope.organizationId, $scope.offset,$scope.noOfRecordsPerPage);
                                }
                            }


							$scope.unitList=[];
							$scope.finalUnitList=[];
							
							$scope.getUnitList = function () {
								var data = {
									organizationId: $scope.organizationId
								};

								$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL + UNITLIST;
								GenericService.serviceAction("POST", URI, data).then(function (response) {
									if (response.data.status == "success") {
										$scope.unitList = response.data.listObject;
										angular.forEach($scope.unitList, function(value , key) {
											value.isUnitSelected='N';
										});
										console.log("Unit List::", $scope.unitList);
										$rootScope.stopLoader();
									}
									else {
										$rootScope.stopLoader();
									}

								});
							};
							$scope.getUnitList();
							
                           $scope.clickedDoctor = function (){
                        	   $scope.userUnitMapperList=[];
	                             $scope.finalUnitList=[];
	                             $scope.selectedDoctorList=[];
                        	   if($scope.example14modelDoctor.length > 0){

                        		   angular.forEach($scope.doctorList,function(value,index) {
                        		   angular.forEach($scope.example14modelDoctor,function(value1,index1) {
                        			   var sd = {};
		                            	  var splittedStringArray = value1.id.split("_");
		                            	  sd.doctorId =splittedStringArray[0]; 
		                            	  sd.specialityId =splittedStringArray[1];
		                            	  
	                        			   if(value.doctorId == sd.doctorId){
	                        				   var found = $scope.selectedDoctorList.find(ar=>ar.doctorId == sd.doctorId);
												if(found == undefined){
													$scope.selectedDoctorList.push(value);
												}
	                        			   }
		                            
										});
                        		   });
                        		angular.forEach($scope.selectedDoctorList,function(value){
                           		var paramObj = {
 		                 			   userId:value.userId,
 		                 			   organisationId:$scope.organizationId
 		                 		}
 		                 		 
 		                         var URI = BASE_URL + ROOT_URL+ "/api/global/getUserUnitListByUserId";
 		                     GenericService.serviceAction("POST", URI, paramObj)
 		                         .then(function(response){console.log(response);
 		                         if (response.data.status == "success")
// 		                        	 $SCOPE.USERUNITMAPPERLIST=[];
// 		                             $SCOPE.FINALUNITLIST=[];
 		                             $scope.userUnitMapperList  = response.data.listObject;
 		                         console.log("userUnitMapperList",response.data.listObject);
 		                         console.log("All unit List ",$scope.unitList);
 		                          angular.forEach($scope.unitList, function(value , key) {
 		                         	 var checkFlag = false;
 		                              angular.forEach($scope.userUnitMapperList, function(value1 , key1) {
 		                             	 if(value.unitId == value1.unitId)
 		                             		 {
 		                             		 	checkFlag = true;
 		                             		 	$scope.newArr = {};
 		                             		 	$scope.newArr.organisationId=$scope.organizationId;
 		                             		 	$scope.newArr.id =value.unitId+'_'+value1.userId;
 		                             		 	$scope.newArr.label=value.unitName;
 		                             		 	$scope.newArr.updatedBy= $scope.userId;
 		                             		 	$scope.newArr.createdBy= $scope.userId;
 		                             		 	$scope.newArr.isUnitSelected = 'Y';
 		                             		 	$scope.newArr.status='A';
 		                             		 	$scope.newArr.createdDate =$scope.currentDate;
 		                             		 	$scope.newArr.updatedDate = $scope.currentDate;
 												
 		                             		 }
 		                              		});
 		                              if (checkFlag == true) {
 		                            	/* var found = $scope.finalUnitList.find(ar=>ar.id == value.unitId);
											if(found == undefined){*/
												$scope.finalUnitList.push($scope.newArr);
//											}
 										} 
       	       
                                     });
                                 });
                        	 
                        	   });
                        	   }
                           }
							
							
							
							$scope.getDoctorSlotList = function(orgId,
									offset, noOfRecordsPerPage) {
								//console.log("offset", offset);
								
								$scope.doctorSlotList = [];
								
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
										: 10;
								var URI = BASE_URL + ROOT_URL
										//+ GETALLSLOTS;
								      +GETALLSLOTSNEW;
								var data = {
									organizationId : $scope.organizationId,
									unitId:$scope.unitId,
									slotTypeId : $scope.slotTypeId,
									offset : offset,
									noOfRecordsPerPage : noOfRecordsPerPage
								}
								data.deptWiseAppointment=$scope.deptWise;
								//console.log("URI", URI);
								// For Get Slot List Using Offset
								$rootScope.startLoader();
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													$scope.doctorSlotList = [];

													if (response.data.status == "success") {
														$scope.doctorSlotList = response.data.listObject;
														$rootScope.stopLoader();
													}
												});
							}

							$scope.pager = {};
							$scope.page;

							$scope.setPage = function(page, flag) 
							{

							    if($scope.pager.totalPages==0){
                                    $scope.pager.totalPages=undefined;
                                }
								if (page < 1 || page > $scope.pager.totalPages) {
									return;
								}
								$scope.pager = PagerService.GetPager(
										$scope.commonListCount, page,
										$scope.noOfRecordsPerPage);
								if (flag) {
									$scope.getDoctorSlotList(
											$scope.organizationId,
											$scope.pager.startIndex,
											$scope.pager.pageSize);
								}
							}

							$scope.initDoctorSlotList(
									$scope.organizationId, $scope.offset,
									$scope.noOfRecordsPerPage);

							// For Paginations End
					/******* start HEmodialysis******/
						
							$scope.setNoOfRecordsOFHemo = function() {
								$scope.initHemoDialysisSlotList(
										$scope.organizationId, $scope.offset,
										$scope.noOfRecordsPerPage);
							};
							$scope.initHemoDialysisSlotList = function(orgId,
									offset, noOfRecordsPerPage) {
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
										: 10;
								var URI = BASE_URL + ROOT_URL
								+ "/api/opd/getHemoSlots";
								
								var URI1 = BASE_URL + ROOT_URL
								+ "/api/opd/getSlotHemoCount";


								var data = {
									organizationId : orgId,
									unitId : $scope.unitId,
									slotTypeId : 8,
									offset : offset,
									noOfRecordsPerPage : noOfRecordsPerPage
								}

								var data1 = {
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									slotTypeId : 8,
								}

								// For Get Nationality List Using Offset
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													$scope.hemodialysisSlotList = [];

													if (response.data.status == "success") {
														$scope.hemodialysisSlotList = response.data.listObject;
                                                    //console.log("$scope.hemodialysisSlotList"+JSON.stringify($scope.hemodialysisSlotList))
													}
												});

								// For Count Slot List
								GenericService
										.serviceAction("POST", URI1, data1)
										.then(
												function(response) {
													if (response.data.status == "success") {
														console
																.log(response.data);
														$scope.commonListHemoCount = response.data.object;
														$scope
																.setPageOfHemo(1,
																		false);

													}
												});
							}

							$scope.getHemoDialysisSlotList = function(orgId,
									offset, noOfRecordsPerPage) {
								//console.log("offset", offset);
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
										: 10;
								var URI = BASE_URL + ROOT_URL
								       + "/api/opd/getHemoSlots";
								var data = {
									organizationId : $scope.organizationId,
									unitId:$scope.unitId,
									slotTypeId : $scope.slotTypeId,
									offset : offset,
									noOfRecordsPerPage : noOfRecordsPerPage
								}

								//console.log("URI", URI);
								// For Get Slot List Using Offset
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													$scope.hemodialysisSlotList = [];

													if (response.data.status == "success") {
														$scope.hemodialysisSlotList = response.data.listObject;

													}
												});
							}

							$scope.pager = {};
							$scope.page;

							$scope.setPageOfHemo = function(page, flag) {
								if (page < 1 || page > $scope.pager.totalPages) {
									return;
								}
								$scope.pager = PagerService.GetPager(
										$scope.commonListHemoCount, page,
										$scope.noOfRecordsPerPage);
								if (flag) {
									$scope.getHemoDialysisSlotList(
											$scope.organizationId,
											$scope.pager.startIndex,
											$scope.pager.pageSize);
								}
							}

							
							$scope.initHemoDialysisSlotList(
									$scope.organizationId, $scope.offset,
									$scope.noOfRecordsPerPage);
							
							/******* END HEmodialysis******/
							
							 /******* START TAB FOR ALL PAGES IN TREE LEVEL *******/
				        	/*First Level*/
					    	$scope.firstTabLevel = 1 ;
							
							$scope.setFirstTabLevel= function(newTab)
							{
								$scope.unitId = $scope.cookieObject.unitId;
								$scope.pager = {};
								$scope.page;
								
								if (newTab == 1) {
//									$("#case-sheet").css("background-color",
//											"#E2E8ED");
//									$("#complaints").css("background-color",
//											"#5ab7c8");
//									$("#History").css("background-color",
//											"#5ab7c8");
//									$("#specialty").css("background-color",
//									"#5ab7c8");
									
									$scope.slotTypeId = 1;
									$scope.deptWise = 'N';
								} else if (newTab == 2) {
//									$("#case-sheet").css("background-color",
//											"#5ab7c8");
//									$("#complaints").css("background-color",
//											"#5F9EA0");
//									$("#History").css("background-color",
//											"#5ab7c8");
//									$("#specialty").css("background-color",
//									"#5ab7c8");
									$scope.slotTypeId = 2;
								} else if(newTab == 3){
//									$("#case-sheet").css("background-color",
//											"#5ab7c8");
//									$("#complaints").css("background-color",
//											"#5ab7c8");
//									$("#History").css("background-color",
//											"#E2E8ED");
//									$("#specialty").css("background-color",
//									"#5ab7c8");
									$scope.slotTypeId = 3;
								}
								else if(newTab == 8){
//									$("#case-sheet").css("background-color",
//											"#5ab7c8");
//									$("#complaints").css("background-color",
//											"#5ab7c8");
//									$("#History").css("background-color",
//											"#E2E8ED");
//									$("#specialty").css("background-color",
//									"#5ab7c8");
									$scope.slotTypeId = 8;
								}
								else {
//									$("#specialty").css("background-color",
//									"#5F9EA0");
//									$("#case-sheet").css("background-color",
//											"#5ab7c8");
//									$("#complaints").css("background-color",
//											"#5ab7c8");
//									$("#History").css("background-color",
//											"#5ab7c8");
									$scope.slotTypeId = 4;
									$scope.deptWise = 'Y';
								}
								
								
								
								$scope.firstTabLevel = newTab;
								
								$scope.initDoctorSlotList(
										$scope.organizationId, $scope.offset,
										$scope.noOfRecordsPerPage);
								
							};
							
							$scope.isSetFirstTabLevel = function(tabNum)
							{
								return $scope.firstTabLevel === tabNum;
							};
							
							/*Second Level
							$scope.secondTabLevel = 1 ;
							
							$scope.setSecondTabLevel = function(newTab)
							{
								$scope.secondTabLevel = newTab;
							};
							
							$scope.isSetSecondTabLevel = function(tabNum)
							{
								return $scope.secondTabLevel === tabNum;
							};
							
							
							Third Level
							$scope.thirdTabLevel = 1 ;
							
							$scope.setThirdTabLevel = function(newTab)
							{
								$scope.thirdTabLevel = newTab;
							};
							
							$scope.isSetThirdTabLevel = function(tabNum)
							{
								return $scope.thirdTabLevel === tabNum;
							};
							
							
							
							FORTH Level
							$scope.forthTabLevel = 1 ;
							
							$scope.setForthTabLevel = function(newTab)
							{
								$scope.forthTabLevel = newTab;
							};
							
							$scope.isSetForthTabLevel = function(tabNum)
							{
								return $scope.forthTabLevel === tabNum;
							};*/
						/******* END TAB FOR ALL PAGES IN TREE LEVEL *******/
							
							
							
							 /* $scope.myEvents = {
									  onItemSelect: function(item) {
					                            //console.log(item);
					                         },
					                     onItemDeselect: function(item) {
					                              //console.log(item);
					                        },
					                        onSelectionChanged: function(item) {
					                        	alert("sdf");
					                              //console.log($scope.example14model);
					                         }
					             };*/
							  
							  $scope.clicked = function(type)
							  {
	                              if(type=='doctor'){
								  if($scope.example14model.length > 0){
	                              var speciality = [];
	                              angular.forEach($scope.example14model,function(value,index) {
	                            	  speciality.push(value.id);
									
									});
	                              var data = {
	                            		  organizationId:$scope.organizationId,
	                            		  unitId:$scope.unitId,
	                            		  specialityArray:speciality
	                              }
	                              //console.log(data);
	                              var URI = BASE_URL + ROOT_URL
									+ GETDOCTORSLISTAGANISTSPECIALITIES;
							BillingGenericService
									.serviceAction(METHOD_POST, URI,
											data,
											NOTIFICATION_MSG_STATUS_FALSE)
									.then(
											function(response) {
												//console.log(response);
												// $rootScope.startLoader();
												if (response.data.status == "success")
													{
													$scope.example14dataDoctor = [];
													$scope.doctorList = [];
													$scope.doctorList = response.data.listObject;
													angular.forEach(response.data.listObject,function(value,index) {
																var data = {};
																data.id=value.doctorId+'_'+value.specialityId;
																data.label=value.firstName+' '+value.lastName;
																$scope.example14dataDoctor.push(data);
															
															});
																											 
													}
													
											});
							  }
	                          else
	                            {
	                        	  
	                            	  $scope.example14dataDoctor = [];
	                            }
								  
	                         }
	                              else if(type=='speciality'){
	                            	  if($scope.example14ModalityType.length > 0){
	    	                              var modalityType = [];
	    	                              angular.forEach($scope.example14ModalityType,function(value,index) {
	    	                            	  modalityType.push(value.id);
	    									
	    									});
	    	                              var data = {
	    	                            		  organizationId:$scope.organizationId,
	    	                            		  unitId:$scope.unitId,
	    	                            		  specialityIdList:modalityType
	    	                              }
	    	                              //console.log(data);
	    	                              var URI = BASE_URL + ROOT_URL
	    									+ GETSUBSPECIALITYBYSPECIALITYARRAY;
	    							BillingGenericService
	    									.serviceAction(METHOD_POST, URI,
	    											data,
	    											NOTIFICATION_MSG_STATUS_FALSE)
	    									.then(
	    											function(response) {
	    												//console.log(response);
	    												// $rootScope.startLoader();
	    												if (response.data.status == "success")
	    													{
	    													$scope.example14dataSubSpeciality = [];
	    													angular.forEach(response.data.listObject,function(value,index) {
	    																var data = {};
	    																//alert(value.modalityDesc);
	    																data.id=value.subSpecialityId;
	    																data.label=value.subSpecialityName;
	    																$scope.example14dataSubSpeciality.push(data);
	    															
	    															});
	    																											 
	    													}
	    													
	    											});
	    							  }
	    	                          else
	    	                            {
	    	                        	  
	    	                            	  $scope.example14dataModality = [];
	    	                            }
	                              }
	                              else
	                            	  {

	                            	  if($scope.example14SubSpeciality.length > 0){
	    	                              var subSpecialityType = [];
	    	                              angular.forEach($scope.example14SubSpeciality,function(value,index) {
	    	                            	  subSpecialityType.push(value.id);
	    									
	    									});
	    	                              var data = {
	    	                            		  organizationId:$scope.organizationId,
	    	                            		  unitId:$scope.unitId,
	    	                            		 subSpecialityArray:subSpecialityType
	    	                              }
	    	                              //console.log(data);
	    	                              var URI = BASE_URL + ROOT_URL
	    									+ GETMODALITYBYSUBSPECIALITYARRAY
	    							BillingGenericService
	    									.serviceAction(METHOD_POST, URI,
	    											data,
	    											NOTIFICATION_MSG_STATUS_FALSE)
	    									.then(
	    											function(response) {
	    												//console.log(response);
	    												// $rootScope.startLoader();
	    												if (response.data.status == "success")
	    													{
	    													$scope.example14dataModality = [];
	    													angular.forEach(response.data.listObject,function(value,index) {
	    																var data = {};
	    																//alert(value.modalityDesc);
	    																data.id=value.modalityId+'_'+value.subSpecialityId+'_'+value.specialityId;
	    																data.label=value.modalityDesc;
	    																$scope.example14dataModality.push(data);
	    															
	    															});
	    																											 
	    													}
	    													
	    											});
	    							  }
	    	                          else
	    	                            {
	    	                        	  
	    	                            	  $scope.example14dataModality = [];
	    	                            }
	                            	  }
							  }
							  
							  $scope.add = function() {
									var data = {
											fromDate : '',
											toDate : '',
											fromTime : '',
											toTime : '',
											timePerSlot : '',
											visitTypeId :1,
											example8model:[],
											breakTimeList:[{breakFromTime:'',breakToTime:''}]
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
								};
								

								// function for initializa clockpicker
								$scope.initClockPicker = function() {

									$('.clockpicker').clockpicker();
								}

							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								if($scope.slotTypeId == '1')
									{
									$scope.saveSlots();
									}
								else if($scope.slotTypeId == '2')
									{
										$scope.saveModalitySlots();
									}
								else if($scope.slotTypeId == '3')
									{
										$scope.saveEchSlots();
									}
								else if($scope.slotTypeId == '8')
									{
										$scope.createNephroloySlot();
									}
								else
									{
										$scope.saveSpecialtySlots();
									}
								
							}
							
							$scope.saveAndOverride= function()
							{
								$scope.override = 'Y';
								
								if($scope.slotTypeId == '1')
								{
									$scope.saveSlots();
								}
								else if($scope.slotTypeId == '2')
								{
									$scope.saveModalitySlots();
								}else if($scope.slotTypeId == '4')
								{
									$scope.saveSpecialtySlots();
								}
								
							}
							
							
							//Save Doctor Slots
							$scope.saveSlots  = function()
							{
                                     var i=0;
                                                                         if($scope.dataList[i].fromDate > $scope.dataList[i].toDate){
                                                                                 									growl.warning("Please Enter From Date less than To Date ",{
                                                                                 										title : WARNING_MSG
                                                                                 									});
                                                                                 									return false;
                                                                                 								}

                                     								    if($scope.dataList[i].fromTime > $scope.dataList[i].toTime){
                                                                                 									growl.warning("Please Enter From Time less than To Time ",{
                                                                                 										title : WARNING_MSG
                                                                                 									});
                                                                                 									return false;
                                                                                 								}

								if($scope.example14modelDoctor.length  == 0){
									growl.warning("Please select doctor.",{
										title : WARNING_MSG
									});
									return false;
								}


//								if( $scope.dataList[0].breakTimeList[0].breakFromTime > $scope.dataList[0].breakTimeList[0].breakToTime ){
//                                	growl.warning("Please Enter Break To Time greater than Break From Time.",{
//                                		 title : WARNING_MSG
//                                			});
//                                			return false;
//                                			}


								/*else if($scope.example14modelUnit.length  == 0){
									growl.warning("Please select unit.",{
										title : WARNING_MSG
									});
									return false;
								}*/
								else
								{
								//console.log($scope.dataList);
								var i = 1;
								 var trueFalseFlag = true;
								angular.forEach($scope.dataList,function(value,index) {
									 if(( value.fromDate == "" || value.fromDate == null || value.fromDate == undefined ) 
											 || (value.toDate == '' || value.toDate == null || value.toDate == undefined )
											|| (value.fromTime == '' || value.fromTime == null || value.fromTime == undefined)
											|| (value.toTime == '' || value.toTime == null || value.toTime == undefined )
											 || (value.timePerSlot == '' || value.timePerSlot == null || value.timePerSlot == undefined)
											 || (value.example8model.length == 0)){
										 
											 growl.warning("All fields are mandatory.",{
													title : WARNING_MSG
												});
											 trueFalseFlag = false;
												return false;
									 }
									 if(i == $scope.dataList.length && trueFalseFlag == true ){
										//return false;
											$rootScope.startLoader();
											if ($scope.popUpFlag) {
												$scope.showModal = !$scope.showModal;

											} else {
														$scope.currentDate = new Date();
														$scope.currentDate = moment($scope.currentDate)
																.format('DD-MM-YYYY HH:mm:ss');
														
														/*//console.log("$scope.example14modelDoctor",$scope.example14modelDoctor);
														//console.log("example14dataDoctor",$scope.example14dataDoctor);
														return false;*/
														
														var doctorSpecialityList = [];
														 angular.forEach($scope.example14modelDoctor,function(value,index) {
							                            	  var sd = {};
							                            	  var splittedStringArray = value.id.split("_");
							                            	  sd.doctorId =splittedStringArray[0]; 
							                            	  sd.specialityId =splittedStringArray[1];
							                            	  doctorSpecialityList.push(sd);
															});
														 
														 var doctorUnitList = [];
														
														 angular.forEach($scope.selectedDoctorList,function(value,index) { 
															 angular.forEach($scope.example14modelUnit,function(value1,index1) {
																
																 	  var sd = {};
									                            	  var splittedStringArrayUnit = value1.id.split("_");
									                            	  sd.unitId = splittedStringArrayUnit[0]; 
									                            	  sd.userId = splittedStringArrayUnit[1];
									                            	  sd.doctorId = value.doctorId
									                            	  if(value.userId ==  sd.userId){
									                            	  doctorUnitList.push(sd); 
																 }
																});
														 });
														 
														 var slotList = [];
														 angular.forEach($scope.dataList,function(value,index) {
							                            	  var slotObj = {};
															  slotObj.fromDate =  moment(value.fromDate).format('YYYY-MM-DD');	
							                            	  slotObj.toDate =  moment(value.toDate).format('YYYY-MM-DD');
							                            	  slotObj.fromTime =  value.fromTime;	
							                            	  slotObj.toTime =  value.toTime;	
							                            	  slotObj.timePerSlot = value.timePerSlot;	
							                            	  slotObj.visitTypeId = value.visitTypeId;
							                            	  var dayList  = [];
							                            	  angular.forEach(value.example8model,function(value,index) {
							                            		  dayList.push(value.id);
							                            	  });
							                            	  slotObj.dayArrayList= dayList;
							                            	  
							                            	  slotObj.breakSlotsList = value.breakTimeList;
							                            	 
							                            	  slotList.push(slotObj);
															});
														 
													/*	if($scope.unitIdSavedData == undefined || $scope.unitIdSavedData == null){
															angular.forEach($scope.unitList,function(value,key){
																if($scope.unitId != value.unitId){
																	$scope.unitIdSavedData = value.unitId;
																}
															});
														}*/
														var data = {
																doctorArrayList:doctorSpecialityList,
																unitArrayList:doctorUnitList,
																slotMasterDtosList :slotList,
																organizationId : $scope.organizationId,
																unitId:$scope.unitId,
																createdDate:$scope.currentDate,
																updatedDate:$scope.currentDate,
																createdBy:$scope.userId,
																updatedBy:$scope.userId,
																status:'A',
																slotTypeId : $scope.slotTypeId,
																unitIdSavedData:$scope.unitIdSavedData
																
														}
														if($scope.override !=undefined && $scope.override !=null && $scope.override == 'Y')
															{
																data.override = 'Y';
															}
														
														
														console.log(data);
//													    return false;
														var URI = BASE_URL + ROOT_URL
//																+ ADDDOCTORSLOTS;
														        +"/api/opd/generateSlotsDoctorSheduling"
					
														BillingGenericService
																.serviceAction(METHOD_POST,
																		URI, data,
																		NOTIFICATION_MSG_STATUS_FALSE)
																.then(
																		function(response) {
																			//console.log(response);
																			//$rootScope.startLoader();
																			if (response.data.status == "success")
																				{
																					
																					growl.success(response.data.message,{
																						title : 'Success!'
																					});
																					
																					$scope.init();
																					$scope.initDoctorSlotList(
																							$scope.organizationId, $scope.offset,
																							$scope.noOfRecordsPerPage);
																					$rootScope.stopLoader();
																				}else if(response.data.code=409)
																					{
																						$rootScope.stopLoader();	
																						$scope.message = response.data.message;
//																						$scope.unitIdSavedData = response.data.listObject[0].unitId;
																						/*$scope.init();
																						$scope.initDoctorSlotList(
																								$scope.organizationId, $scope.offset,
																								$scope.noOfRecordsPerPage);*/
																						$scope.hideOverride = response.data.object!=null ? true : false;
																						angular.element('#already_exist').modal('show');
																					}
																		});
														
														
														
													}
									 }
	                            	  i++;
									});
								
								
							}
							}
							
							
							//Save Specialty Slots
							$scope.saveSpecialtySlots  = function()
							{
                                                var i=0;
                                                                         if($scope.dataList[i].fromDate > $scope.dataList[i].toDate){
                                                                                 									growl.warning("Please Enter From Date less than To Date ",{
                                                                                 										title : WARNING_MSG
                                                                                 									});
                                                                                 									return false;
                                                                                 								}

                                     								    if($scope.dataList[i].fromTime > $scope.dataList[i].toTime){
                                                                                 									growl.warning("Please Enter From Time less than To Time ",{
                                                                                 										title : WARNING_MSG
                                                                                 									});
                                                                                 									return false;
                                                                                 								}
								
								if($scope.example14modelSpecialty.length  == 0){
									growl.warning("Please select specialty.",{
										title : WARNING_MSG
									});
									return false;
								}

								else
								{
									
									var i = 1;
									 var trueFalseFlag = true;
									angular.forEach($scope.dataList,function(value,index) {
										 if(( value.fromDate == "" || value.fromDate == null || value.fromDate == undefined ) 
												 || (value.toDate == '' || value.toDate == null || value.toDate == undefined )
												|| (value.fromTime == '' || value.fromTime == null || value.fromTime == undefined)
												|| (value.toTime == '' || value.toTime == null || value.toTime == undefined )
												 || (value.timePerSlot == '' || value.timePerSlot == null || value.timePerSlot == undefined)
												 || (value.example8model.length == 0)){
											 
												 growl.warning("All fields are mandatory.",{
														title : WARNING_MSG
													});
												 trueFalseFlag = false;
													return false;
										 }
										 if(i == $scope.dataList.length && trueFalseFlag == true ){
											 
										 
											//console.log($scope.dataList);
											//return false;
											$rootScope.startLoader();
											if ($scope.popUpFlag) {
												$scope.showModal = !$scope.showModal;
			
											} else {
												$scope.currentDate = new Date();
												$scope.currentDate = moment($scope.currentDate)
														.format('DD-MM-YYYY HH:mm:ss');
												
												var doctorSpecialityList = [];
												 angular.forEach($scope.example14modelSpecialty,function(value,index) {
					                            	  var sd = {};
					                            	 // var splittedStringArray = value.id.split("_");
					                            	  sd.specialityId = value.id;
					                            	  doctorSpecialityList.push(sd);
													});
												 
												 
												 var slotList = [];
												 angular.forEach($scope.dataList,function(value,index) {
					                            	  var slotObj = {};
													  slotObj.fromDate =  moment(value.fromDate).format('YYYY-MM-DD');	
					                            	  slotObj.toDate =  moment(value.toDate).format('YYYY-MM-DD');
					                            	  slotObj.fromTime =  value.fromTime;	
					                            	  slotObj.toTime =  value.toTime;	
					                            	  slotObj.timePerSlot = value.timePerSlot;	
					                            	  slotObj.visitTypeId = value.visitTypeId;
					                            	  var dayList  = [];
					                            	  angular.forEach(value.example8model,function(value,index) {
					                            		  dayList.push(value.id);
					                            	  });
					                            	  slotObj.dayArrayList= dayList;
					                            	  
					                            	  slotObj.breakSlotsList = value.breakTimeList;
					                            	 
					                            	  slotList.push(slotObj);
													});
												 
												
												var data = {
														doctorArrayList:doctorSpecialityList,
														slotMasterDtosList :slotList,
														organizationId : $scope.organizationId,
														unitId:$scope.unitId,
														createdDate:$scope.currentDate,
														updatedDate:$scope.currentDate,
														createdBy:$scope.userId,
														updatedBy:$scope.userId,
														status:'A',
														slotTypeId : $scope.slotTypeId
												}
												if($scope.override !=undefined && $scope.override !=null && $scope.override == 'Y')
												{
													data.override = 'Y';
												}
												//console.log(data);
											    //return false;
												var URI = BASE_URL + ROOT_URL
														+ ADDDOCTORSLOTS;
			
												BillingGenericService
														.serviceAction(METHOD_POST,
																URI, data,
																NOTIFICATION_MSG_STATUS_FALSE)
														.then(
																function(response) {
																	//console.log(response);
																	//$rootScope.startLoader();
																	if (response.data.status == "success")
																	{
																		growl.success(response.data.message,{
																			title : 'Success!'
																		});
																		$scope.init();
																		$scope.initDoctorSlotList(
																				$scope.organizationId, $scope.offset,
																				$scope.noOfRecordsPerPage);
																		$rootScope.stopLoader();
																	}else if(response.data.code=409)
																	{
																		$rootScope.stopLoader();
																		$scope.message = response.data.message;
																		angular.element('#already_exist').modal('show');
																	}
																});
												
												
													}
										 		}
			                            	  i++;
											});
										
										
									}
									}//function close
							
							//Save Modality Slots
							$scope.saveModalitySlots  = function()
							{
                              var i=0;
                                                                                                       if($scope.dataList[i].fromDate > $scope.dataList[i].toDate){
                                                                                                               									growl.warning("Please Enter From Date less than To Date ",{
                                                                                                               										title : WARNING_MSG
                                                                                                               									});
                                                                                                               									return false;
                                                                                                               								}

                                                                   								    if($scope.dataList[i].fromTime > $scope.dataList[i].toTime){
                                                                                                               									growl.warning("Please Enter From Time less than To Time ",{
                                                                                                               										title : WARNING_MSG
                                                                                                               									});
                                                                                                               									return false;
                                                                                                               								}

								if($scope.example14ModalityType.length  == 0){
									growl.warning("Please select specialty.",{
										title : WARNING_MSG
									});
									return false;
								}

								else if($scope.example14SubSpeciality.length  == 0){
									growl.warning("Please select sub specialty.",{
										title : WARNING_MSG
									});
									return false;
								}
								else if($scope.example14modelModality.length  == 0){
									growl.warning("Please select modality.",{
										title : WARNING_MSG
									});
									return false;
								}
								else
								{
									
									var i = 1;
									 var trueFalseFlag = true;
									angular.forEach($scope.dataList,function(value,index) {
										 if(( value.fromDate == "" || value.fromDate == null || value.fromDate == undefined ) 
												 || (value.toDate == '' || value.toDate == null || value.toDate == undefined )
												|| (value.fromTime == '' || value.fromTime == null || value.fromTime == undefined)
												|| (value.toTime == '' || value.toTime == null || value.toTime == undefined )
												 || (value.timePerSlot == '' || value.timePerSlot == null || value.timePerSlot == undefined)
												 || (value.example8model.length == 0)){
											 
												 growl.warning("All fields are mandatory.",{
														title : WARNING_MSG
													});
												 trueFalseFlag = false;
													return false;
										 }
										 if(i == $scope.dataList.length && trueFalseFlag == true ){
								//console.log($scope.dataList);
								$rootScope.startLoader();
								if ($scope.popUpFlag) {
									$scope.showModal = !$scope.showModal;

								} else {
										$scope.currentDate = new Date();
										$scope.currentDate = moment($scope.currentDate)
												.format('DD-MM-YYYY HH:mm:ss');
										
										
										////console.log("$scope.example14modelModality",$scope.example14modelModality);
										////console.log("$scope.example14dataModality",$scope.example14dataModality);
										//return false;
										
										var modalityTypeModalityList = [];
										 angular.forEach($scope.example14modelModality,function(value,index) {
			                            	  var sd = {};
			                            	  var splittedStringArray = value.id.split("_");
			                            	  sd.modalityId =splittedStringArray[0]; 
			                            	  sd.subSpecialityId =splittedStringArray[1];
			                            	  sd.specialityId =splittedStringArray[2];
			                            	  modalityTypeModalityList.push(sd);
											});
										 
										 
										 var slotList = [];
										 angular.forEach($scope.dataList,function(value,index) {
			                            	  var slotObj = {};
											  slotObj.fromDate =  moment(value.fromDate).format('YYYY-MM-DD');	
			                            	  slotObj.toDate =  moment(value.toDate).format('YYYY-MM-DD');
			                            	  slotObj.fromTime =  value.fromTime;	
			                            	  slotObj.toTime =  value.toTime;	
			                            	  slotObj.timePerSlot = value.timePerSlot;	
			                            	  slotObj.visitTypeId = value.visitTypeId;
			                            	  var dayList  = [];
			                            	  angular.forEach(value.example8model,function(value,index) {
			                            		  dayList.push(value.id);
			                            	  });
			                            	  slotObj.dayArrayList= dayList;
			                            	  
			                            	  slotObj.breakSlotsList = value.breakTimeList;
			                            	 
			                            	  slotList.push(slotObj);
											});
										 
										
										var data = {
												doctorArrayList:modalityTypeModalityList,
												slotMasterDtosList :slotList,
												organizationId : $scope.organizationId,
												unitId:$scope.unitId,
												createdDate:$scope.currentDate,
												updatedDate:$scope.currentDate,
												createdBy:$scope.userId,
												updatedBy:$scope.userId,
												status:'A',
												slotTypeId : $scope.slotTypeId
										}
										if($scope.override !=undefined && $scope.override !=null && $scope.override == 'Y')
										{
											data.override = 'Y';
										}
										//console.log(data);
										//return false;
										var URI = BASE_URL + ROOT_URL
												+ ADDDOCTORSLOTS;
	
										BillingGenericService
												.serviceAction(METHOD_POST,
														URI, data,
														NOTIFICATION_MSG_STATUS_FALSE)
												.then(
														function(response) {
															//console.log(response);
															//$rootScope.startLoader();
															if (response.data.status == "success")
															{	
																growl.success(response.data.message,{
																	title : 'Success!'
																});
																//$scope.firstTabLevel = 2 ;
																
																//$scope.setFirstTabLevel(2);
																$scope.init();
																$scope.initDoctorSlotList(
																	$scope.organizationId, $scope.offset,
																	$scope.noOfRecordsPerPage);
																$rootScope.stopLoader();
														}else if(response.data.code=409)
															{
																$rootScope.stopLoader();
																$scope.message = response.data.message;
																angular.element('#already_exist').modal('show');
															}
														});
										}
										 }
										 i++;
									});
								
							}
							
							}
/*	--------------------Nephology SAVE --------------------- */	
							
							$scope.createNephroloySlot=function(){
								
								if($scope.nephrologySpecialityId !=undefined || $scope.nephrologySpecialityId !=null)
								{
									if($scope.itemId==undefined || $scope.itemId==0 || $scope.itemId=="" ){
										growl.warning("Please select dialysis chair.",{
											title : WARNING_MSG
										});
										return false;
									}else{
										
										var i = 1;
										 var trueFalseFlag = true;
										angular.forEach($scope.dataList,function(value,index) {
											
											 if(( value.fromDate == "" || value.fromDate == null || value.fromDate == undefined ) 
													 || (value.toDate == '' || value.toDate == null || value.toDate == undefined )
													 || (value.noOfSlots == '' || value.noOfSlots == null || value.noOfSlots == undefined)
													 || (value.example8model.length == 0))
											 {
													 growl.warning("All fields are mandatory.",{
															title : WARNING_MSG
														});
													 trueFalseFlag = false;
														return false;
											 }
											 if(i == $scope.dataList.length && trueFalseFlag == true ){
									//console.log($scope.dataList);
									$rootScope.startLoader();
									if ($scope.popUpFlag) {
										$scope.showModal = !$scope.showModal;

									} else {
										$scope.currentDate = new Date();
										$scope.currentDate = moment($scope.currentDate)
												.format('DD-MM-YYYY HH:mm:ss');
										
										
									
										 console.info($scope.specialityId)
										 var slotList = [];
										 angular.forEach($scope.dataList,function(value,index) {
			                            	  var slotObj = {};
											  slotObj.fromDate =  moment(value.fromDate).format('YYYY-MM-DD');	
			                            	  slotObj.toDate =  moment(value.toDate).format('YYYY-MM-DD');
			                            	 // slotObj.fromTime =  value.fromTime;	
			                            	  //slotObj.toTime =  value.toTime;	
			                            	  //slotObj.timePerSlot = value.timePerSlot;	
			                            	  //slotObj.visitTypeId = value.visitTypeId;
			                            	  slotObj.noOfSlots = value.noOfSlots;	
			                            	  var dayList  = [];
			                            	  angular.forEach(value.example8model,function(value,index) {
			                            		  dayList.push(value.id);
			                            	  });
			                            	  slotObj.dayArrayList= dayList;
			                            	  
			                            	  //slotObj.breakSlotsList = value.breakTimeList;
			                            	 
			                            	  slotList.push(slotObj);
											});
										 
										//console.log("itemId"+$scope.itemId)
										var data = {
												specialityId:$scope.nephrologySpecialityId,
												bedId:$scope.itemId,
												slotMasterDtosList :slotList,
												organizationId : $scope.organizationId,
												unitId:$scope.unitId,
												createdDate:$scope.currentDate,
												updatedDate:$scope.currentDate,
												createdBy:$scope.userId,
												updatedBy:$scope.userId,
												status:'A',
												slotTypeId : $scope.slotTypeId
										}
										
										//console.log(data);
										//return false;
										var URI = BASE_URL + ROOT_URL
											+"/api/opd/generateSlotsForHemodialysis";

										BillingGenericService
												.serviceAction(METHOD_POST,
														URI, data,
														NOTIFICATION_MSG_STATUS_TRUE)
												.then(
														function(response) {
															//console.log(response);
															//$rootScope.startLoader();
															if (response.data.status == "success")
																$scope.init();
															$rootScope.stopLoader();
														});
										
										
										
									}	
									}
									i++;
									});
								}
								}else{
									growl.warning(
											'Hemodialysis is not configured... !!',
											{
												title : 'Warning!'
											});
								}
								
								
							}
							
							$scope.saveNephrologySlots  = function()
							{ 
                                                             var i=0;
                                                                         if($scope.dataList[i].fromDate > $scope.dataList[i].toDate){
                                                                                 									growl.warning("Please Enter From Date less than To Date ",{
                                                                                 										title : WARNING_MSG
                                                                                 									});
                                                                                 									return false;
                                                                                 								}
								
								if($scope.nephrologySpecialityId !=undefined || $scope.nephrologySpecialityId !=null)
										$scope.createNephroloySlot();
									else{
									 growl.warning(
												'Hemodialysis speciality is not configure so, please configure .... !!',
												{
													title : 'Warning!'
												});
									return false;
								}
											
							}
							
							/*	--------------------Nephology END --------------------- */	
						//Save ECH Slots
							$scope.saveEchSlots= function()
							{
							var i=0;
                                                                                                     if($scope.dataList[i].fromDate > $scope.dataList[i].toDate){
                                                                                                             									growl.warning("Please Enter From Date less than To Date ",{
                                                                                                             										title : WARNING_MSG
                                                                                                             									});
                                                                                                             									return false;
                                                                                                             								}

								var i = 1;
								 var trueFalseFlag = true;
								angular.forEach($scope.dataList,function(value,index) {
									 if(( value.fromDate == "" || value.fromDate == null || value.fromDate == undefined ) 
											 || (value.toDate == '' || value.toDate == null || value.toDate == undefined )
											 || (value.noOfSlots == '' || value.noOfSlots == null || value.noOfSlots == undefined)
											 || (value.example8model.length == 0)){
										 
											 growl.warning("All fields are mandatory.",{
													title : WARNING_MSG
												});
											 trueFalseFlag = false;
												return false;
									 }
									 if(i == $scope.dataList.length && trueFalseFlag == true ){
								//console.log($scope.dataList);
								$rootScope.startLoader();
								if ($scope.popUpFlag) {
									$scope.showModal = !$scope.showModal;

								}
								 else {
									$scope.currentDate = new Date();
									$scope.currentDate = moment($scope.currentDate)
											.format('DD-MM-YYYY HH:mm:ss');
									
									 
									 var slotList = [];
									 angular.forEach($scope.dataList,function(value,index) {
		                            	  var slotObj = {};
										  slotObj.fromDate =  moment(value.fromDate).format('YYYY-MM-DD');	
		                            	  slotObj.toDate =  moment(value.toDate).format('YYYY-MM-DD');
		                            	  slotObj.noOfSlots = value.noOfSlots;	
		                            	  var dayList  = [];
		                            	  angular.forEach(value.example8model,function(value,index) {
		                            		  dayList.push(value.id);
		                            	  });
		                            	  slotObj.dayArrayList= dayList;
		                            	  slotList.push(slotObj);
										});
									 
									
									var data = {
											slotMasterDtosList :slotList,
											organizationId : $scope.organizationId,
											unitId:$scope.unitId,
											createdDate:$scope.currentDate,
											updatedDate:$scope.currentDate,
											createdBy:$scope.userId,
											updatedBy:$scope.userId,
											status:'A',
											slotTypeId : $scope.slotTypeId
									}
									
									//console.log(data);
									//return false;
									var URI = BASE_URL + ROOT_URL
											+ ADDDOCTORSLOTS;

									BillingGenericService
											.serviceAction(METHOD_POST,
													URI, data,
													NOTIFICATION_MSG_STATUS_TRUE)
											.then(
													function(response) {
														//console.log(response);
														//$rootScope.startLoader();
														if (response.data.status == "success")
															
															$scope.initDoctorSlotList(
																	$scope.organizationId, $scope.offset,
																	$scope.noOfRecordsPerPage);
														    $scope.init();
														/*	if($scope.slotTypeId==3)
																{
																  $scope.setFirstTabLevel(3);
																}*/
															
														$rootScope.stopLoader();
													});
									
									
									
								}
							}
							i++;
							});
							}
							
							
							//for Days
							  $scope.example8data = [ {id: 1, label: "Monday"}, {id: 2, label: "Tuesday"},{id: 3, label: "Wednesday"},{id: 4, label: "Thursday"},{id: 5, label: "Friday"},{id: 6, label: "Saturday"}, {id: 7, label: "Sunday"}]; 
							  $scope.example8settings = { 
									checkBoxes: true, 
									dynamicTitle: true, 
									showUncheckAll: false, 
									showCheckAll: false ,
									scrollable: true,
							  };
							  
							  $scope.addBreak = function(pIndex,cIndex) {
								 // alert(pIndex);
								  var Pobj = $scope.dataList[pIndex];
								  Pobj.breakTimeList.push({breakFromTime:'',breakToTime:''});
								};

								$scope.removeBreak = function(pIndex,index) {
									
									 var Pobj = $scope.dataList[pIndex];
									 var le =  $scope.dataList[pIndex].breakTimeList.length;
									 
										var i = 1;
										
										if(le != 1){
											 var newDataList1 = [];
										angular.forEach($scope.dataList[pIndex].breakTimeList, function(v) {
											
											if (index != i) {
												newDataList1.push(v);
											}
											
											i++;
										});
										
										Pobj.breakTimeList = newDataList1;
										
										}
									
								};
							
								//Get Doctor Slot Details
								$scope.getSlotDetails = function(date)
								{
									var array = date.split('_');
									var data = {
											specialityId : $scope.specialityId,
											doctorId:$scope.doctorId,
											appointmentDate :  array[1],
											//dayId : item.dayId,
											organizationId:$scope.organizationId,
											unitId : $scope.unitId,
											slotTypeId : $scope.slotTypeId
									};
									
									if($scope.modalityId != undefined && $scope.modalityId != null){
										data.modalityId =$scope.modalityId;
									}
									if($scope.subSpecialityId !=undefined && $scope.subSpecialityId !=null){
										data.subSpecialityId =$scope.subSpecialityId;
										
									}
																	
									//console.log(data);
									//return false;
							var URI = BASE_URL + ROOT_URL
										+ GETSLOTDETAILS;
								BillingGenericService
										.serviceAction(METHOD_POST, URI,
												data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													//console.log(response);
													$scope.slotDetailsList = [];
													// $rootScope.startLoader();
													if (response.data.status == "success")
														{
														  $scope.slotDetailsList = response.data.listObject;
															//console.log( $scope.slotDetailsList); 
														}
														
												});
								}
								
								//Deactive slots by doctor, speciality and appointment date
								$scope.deactivateSlots = function(item)
								{
									$scope.currentDate = new Date();
									$scope.currentDate = moment($scope.currentDate)
											.format('DD-MM-YYYY HH:mm:ss');
									
									var data = {
											
											appointmentDate :  moment(item.appointmentDate,'DD/MM/YYYY').format('YYYY-MM-DD'),
											organizationId:$scope.organizationId,
											unitId : item.unitId != null ? item.unitId : $scope.unitId,
											slotTypeId : $scope.slotTypeId,
											updatedDate:$scope.currentDate,
											updatedBy:$scope.userId
									};
									
									if($scope.slotTypeId==1)
									{
										data.specialityId = item.specialityId;
										data.doctorId = item.doctorId;
									}
									if($scope.slotTypeId==4)
									{
										data.specialityId = item.specialityId;
										//data.doctorId = item.doctorId;
									}
									if($scope.slotTypeId==2)
									{
										data.specialityId = item.specialityId;
										data.subSpecialityId = item.subSpecialityId;
										data.modalityId = item.modalityId;
									}
									if($scope.slotTypeId==8)
									{
										data.specialityId = item.specialityId;
										//data.itemId = item.itemId;
										data.bedId = item.itemId;
									}
									//console.log(data);
								//return false;
									$rootScope.startLoader();
							var URI = BASE_URL + ROOT_URL
										+ DEACTIVESLOTS;
								BillingGenericService
										.serviceAction(METHOD_POST, URI,
												data,
												NOTIFICATION_MSG_STATUS_TRUE)
										.then(
												function(response) {
													//console.log(response);
													$scope.slotDetailsList = [];
													// $rootScope.startLoader();
													if (response.data.status == "success")
														{

														$scope.initDoctorSlotList(
                                                        	$scope.organizationId, $scope.offset,
                                                        	$scope.noOfRecordsPerPage);

															$scope.getDoctorSlotList(
																	$scope.organizationId,
																	$scope.pager.startIndex,
																	$scope.pager.pageSize);
															
															$rootScope.stopLoader();
														}
														
												});
								}
								
								$scope.deactivateSlotsFlag = function(item,typeFlag)
								{
									
									$scope.slotListMonth = [];
//									$scope.setThirdTabLevel(2);
									
									if(typeFlag == 'M')
										{
										 var copyright = new Date();
										 var year = copyright.getFullYear();
										 var month = copyright.getMonth()+1;
										
										}
									else
										{
											var arr = item.fromDate.split('-');
											 var year = arr[1];
											 var month = arr[0];
										}
									
									var data = {
										"organizationId" : $scope.organizationId,
										"unitId" : $scope.unitId,
										"slotTypeId" : $scope.slotTypeId,
										"month" : month,
										"year" : year
									}
									$scope.selectedSlotId = $scope.slotTypeId;
									if($scope.selectedSlotId == '4')
									{
										data.slotTypeId = 4;
										data.specialityId = $scope.specialityId;
										//data.bedId = $scope.bedId;
									}
								
									
									if ($scope.selectedSlotId == 1 ) 
									{
										
										data.doctorId = $scope.doctorId;
										data.specialityId = $scope.specialityId;
										//data.bedId = $scope.bedId;
										data.slotTypeId = 1;
									}
							
									else if($scope.selectedSlotId == 8)
									{
										data.slotTypeId = 8;
										data.bedId = $scope.bedId;
										data.specialityId = $scope.hemodialysisSpecialityId;
										$scope.secondTabLevel = 2;
									}
									else if($scope.selectedSlotId == 2)
									{
										data.specialityId = $scope.specialityId;
										data.subSpecialityId = $scope.subSpecialityId;
										data.modalityId = $scope.modalityId;
										data.bedId = $scope.bedId;
									}
									else
									{
										$scope.secondTabLevel = 2;
									}
									data.updatedBy = $scope.userId;
									//console.log(data);
									//return false;
									var URI = BASE_URL + ROOT_URL + "/api/opd/deactiveSlotsWithInDateRange";

									BillingGenericService
											.serviceAction(METHOD_POST, URI, data,
													NOTIFICATION_MSG_STATUS_TRUE)
											.then(
													function(response) {
														//console.log(response);
														$scope.slotDetailsList = [];
														// $rootScope.startLoader();
														if (response.data.status == "success") 
														{

                                                            if($scope.firstTabLevel == 2){
                                                                $scope.getSlotsForModality($scope.modalityId,$scope.subSpecialityId,$scope.specialityId,$scope.doctorName, $scope.specialityName);
                                                            }{
														        $scope.getSlotsDoctorShedule($scope.doctorId, $scope.specialityId,
                                                                    $scope.doctorName, $scope.specialityName,$scope.slotTypeId , $scope.unitId);
                                                            }
															$scope.initDoctorSlotList(
                                                            $scope.organizationId, $scope.offset,
                                                            $scope.noOfRecordsPerPage);
														}

													});
								}
								
								//Get ECH Slot Details
								$scope.getSlotDetailsForECH = function(item)
								{
									//alert(item.date);
									var data = {
											appointmentDate :  moment(item.appointmentDate,'DD/MM/YYYY').format('YYYY-MM-DD'),
											dayId : item.dayId,
											organizationId:$scope.organizationId,
											unitId : $scope.unitId,
											slotTypeId : $scope.slotTypeId
									};
									//console.log(data);
									//return false;
							var URI = BASE_URL + ROOT_URL
										+ GETSLOTDETAILS;
								BillingGenericService
										.serviceAction(METHOD_POST, URI,
												data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													//console.log(response);
													$scope.slotDetailsList = [];
													// $rootScope.startLoader();
													if (response.data.status == "success")
														{
														  $scope.slotDetailsList = response.data.listObject;
															//console.log( $scope.slotDetailsList); 
														}
														
												});
									
								}
								
								//Get Modality Slot Details
								$scope.getSlotDetailsForModality = function(item)
								{
									//alert($scope.slotTypeId);
									var data = {
											modalityId : item.modalityId,
											specialityId:item.specialityId,
											subSpecialityId:item.subSpecialityId,
											appointmentDate :  moment(item.appointmentDate).format('YYYY-DD-MM'),
											dayId : item.dayId,
											organizationId:$scope.organizationId,
											unitId : $scope.unitId,
											slotTypeId : $scope.slotTypeId
									};
									//console.log(data);
									//return false;
							var URI = BASE_URL + ROOT_URL
										+ GETSLOTDETAILS;
								BillingGenericService
										.serviceAction(METHOD_POST, URI,
												data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													//console.log(response);
													$scope.slotDetailsList = [];
													// $rootScope.startLoader();
													if (response.data.status == "success")
														{
														  $scope.slotDetailsList = response.data.listObject;
															//console.log( $scope.slotDetailsList); 
														}
														
												});
									
								}
								 $scope.getSlotDetailsForHemodailysis=function(item){
										//console.log(item)
										//console.log(item)
										var data = {
												//modalityId : item.modalityId,
												//specialityId:item.specialityId,
												//subSpecialityId:item.subSpecialityId,
												appointmentDate :  moment(item.appointmentDate,'DD/MM/YYYY').format('YYYY-MM-DD'),
												dayId : item.dayId,
												organizationId:$scope.organizationId,
												unitId : $scope.unitId,
												slotTypeId : $scope.slotTypeId,
												bedId : item.itemId
										};
										//console.log(data);
										
								var URI = BASE_URL + ROOT_URL
											+ GETSLOTDETAILS;
									BillingGenericService
											.serviceAction(METHOD_POST, URI,
													data,
													NOTIFICATION_MSG_STATUS_FALSE)
											.then(
													function(response) {
														//console.log(response);
														$scope.slotDetailsList = [];
														// $rootScope.startLoader();
														if (response.data.status == "success")
															{
															  $scope.slotDetailsList = response.data.listObject;
																//console.log( $scope.slotDetailsList); 
															}
															
													});
										
									
								}
								// function for updateStatus
        $scope.updateStatus = function(id, type,index) {
         
         
         $scope.currentDate = new Date();
         $scope.currentDate = moment($scope.currentDate)
           .format('DD-MM-YYYY HH:mm:ss');

         type = (type == "A") ? "A" : "I";

         var data = {
          slotId : id,
          status : type,
          updatedBy : $scope.userId,
          updatedDate : $scope.currentDate,
          organizationId:$scope.organizationId,
          unitId : $scope.unitId
         }
         //console.log(data);
         var URI = BASE_URL + ROOT_URL
           + UPDATESLOTSTATUS;
         GenericService
           .serviceAction("POST", URI, data)
           .then(
             function(response) {

              if (response.data.status == "success") {
               growl
                 .success(
                   response.data.message,
                   {
                    title : 'Success!'
                   });
              } else {
               growl
                 .error(
                   response.data.message,
                   {
                    title : 'Error!'
                   });
               if(type == 'A')
                {
                $scope.slotDetailsList[index].status = 'I';
                }
               else
                {
                $scope.slotDetailsList[index].status = 'A';
                }
               
              }
             });
        }
        /************************************OT Scheduling START******************************************************/
        
        $scope.otDataList = [ {
			fromDate : '',
			toDate : '',
			fromTime : '',
			toTime : '',
			switchOverTime : '',
			example8model:[]
		} ];
        
        
      //Loop DatePicker Start
		// for datePicker
        var today = new Date(); // Get the current date
		  var maxDate = new Date(today.getTime() + (365 * 24 * 60 * 60 * 1000));
		var today = new Date();
		$scope.dateOptions = {
		 // Get the current date
		//var maxDate = new Date(today.getTime() + (365 * 24 * 60 * 60 * 1000));
			formatYear : 'yyyy',
			showWeeks : false,
			minDate : today,
			maxDate : new Date(today.getTime() + (365 * 24 * 60 * 60 * 1000)),
		};
		
		//for From Date
		$scope.openOTFromDatePicker = function(index) {
			for (var i = $scope.otDataList.length - 1; i >= 0; i--) {
				if (i === index) {

					$scope.fromDate.opened[i] = true;
				} else {
					$scope.fromDate.opened[i] = false;
				}
			}
		};

		$scope.fromDate = {
			opened : [ false ],

		};
		//End From Date
		
		//for To Date
		$scope.openOTToDattePicker = function(index) {
			for (var i = $scope.otDataList.length - 1; i >= 0; i--) {
				if (i === index) {

					$scope.toDate.opened[i] = true;
				} else {
					$scope.toDate.opened[i] = false;
				}
			}
		};

		$scope.toDate = {
			opened : [ false ],

		};
		//End To Date
		
        
        	
        $scope.theatreObj = {
        		operationTheatreId:"",
        		theatreName:""
        };
        
        //get Operation Theatre list
        $scope.operationTheatreList = [];
        $scope.getOperationTheatreList = function(){
			var URI = BASE_URL + ROOT_URL + "/api/otims/getActiveOperationTheatreList";
			var obj = {
					orgId:$scope.organizationId, 
					unitId:$scope.unitId
					};
			BillingGenericService.serviceAction(METHOD_POST,URI, obj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				if (response.data.status == "success")
					$scope.operationTheatreList = response.data.listObject;
			});
		}
		
		$scope.getOperationTheatreList();
		
		$scope.operationTableObj = {
				operationTableId:"",
				tableName:""
		};
		
		//get Operation Table List
		$scope.operationTableList = [];
		$scope.selectedTableList = [];
        $scope.getOperationTheatreList = function(theatreId){
			var URI = BASE_URL + ROOT_URL + "/api/otims/getActiveOperationTableListByTheatreId";
			var obj = {
					orgId:$scope.organizationId, 
					unitId:$scope.unitId,
					operationTheatreId:theatreId
					};
			BillingGenericService.serviceAction(METHOD_POST,URI, obj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				if (response.data.status == "success")
				angular.forEach(response.data.listObject,
						function(value,index) {
							var data = {};
							data.id = value.operationTableId;
							data.label = value.tableName;
							$scope.operationTableList.push(data);
						});
			});
		}
        
        
        //select OT Table 
    	$scope.tableIds = [];
        	$scope.clickedOt = function() {
        		$scope.tableIds = [];
		if ($scope.selectedTableList.length > 0) {
			
			angular.forEach($scope.selectedTableList,
					function(value, index) {
				$scope.tableIds.push(value.id);
					});
		
			var isExists;
			function checkExistsOrNot(arr, val) {
				return arr
						.some(function(arr) {
							return (val.id === arr.id);
						});
				}
				}
        	}
	
        	
        	//select Days
        	$scope.daysIds = [];
        	$scope.clickedOnDaysList = function(idx) {
        		
        		$scope.daysIds = [];
        	if ($scope.otDataList[idx].example8model.length > 0) {
			
			angular.forEach($scope.otDataList[idx].example8model,
					function(value, index) {
				$scope.daysIds.push(value.id);
					});
		
			var isExists;
			function checkExistsOrNot(arr, val) {
				return arr
						.some(function(arr) {
							return (val.id === arr.id);
						});
				}
				}
        	}

        	
        	
        	$scope.dateList = [];
        	 var setDateList = function(fromDate,length,idx){
        		 var i =0, year, month, day, shiftDate,date;
            		for (i = 0; i <= length; i++) {
    					var pdate = moment(fromDate, "DD/MM/YYYY")
    							.add('days', i).toDate();
    					year = pdate.getFullYear();
    					month = pdate.getMonth();
    					month = (Number(pdate.getMonth()) + 1) <= 9 ? "0"
    							+ (Number(pdate.getMonth()) + 1)
    							: (Number(pdate.getMonth()) + 1);
    					date = pdate.getDate() <= 9 ? "0"
    							+ pdate.getDate() : pdate.getDate();
    					shiftDate = date + "/" + month + "/" + year;
    					var data = {
    							shiftDate : shiftDate,
            	     			key : idx
            	     			
            			};
    					$scope.dateList.push(data);
    				}
            	
        		
        	}
        	
        
        	 
        //Add OT time details in list
        $scope.addOTTime = function(idx) {
        	$rootScope.startLoader();
        	var fromDate = $scope.otDataList[idx].fromDate;
        	var toDate = $scope.otDataList[idx].toDate;
        	var otFlag = false;
        	if($scope.otDataList[idx].fromDate==""){
        		
        		$rootScope.stopLoader();
				growl.warning("Please select From Date",
						{
							title : 'warning!'
						});
				
        	}else if($scope.otDataList[idx].toDate==""){
        		
        		$rootScope.stopLoader();
				growl.warning("Please select To Date",
						{
							title : 'warning!'
						});
				
        	}else if($scope.otDataList[idx].fromTime==""){
        		
        		$rootScope.stopLoader();
				growl.warning("Please select From Time",
						{
							title : 'warning!'
						});
				
        	}else if($scope.otDataList[idx].toTime==""){
        		
        		$rootScope.stopLoader();
				growl.warning("Please select To Time",
						{
							title : 'warning!'
						});
				
        		
        	}else if($scope.otDataList[idx].switchOverTime==""){
        		
        		$rootScope.stopLoader();
				growl.warning("Please Enter switch Over Time",
						{
							title : 'warning!'
						});
				
        	}else if($scope.otDataList[idx].example8model.length==0){
        		
        		$rootScope.stopLoader();
				growl.warning("Please select Days",
						{
							title : 'warning!'
						});
				
        	}else{ 
        		if(fromDate <= toDate){
        		var keyindx;
        		var i =0, year, month, day, shiftDate;
        		var length = Math.floor((Date.UTC(toDate.getFullYear(),
        				toDate.getMonth(),
						toDate.getDate()) - Date.UTC(fromDate.getFullYear(), 
						fromDate.getMonth(),
						fromDate.getDate()))/ (1000 * 60 * 60 * 24));
        		
        		if(idx>0){
        		
					if(length>0){
						var fdate = moment($scope.otDataList[idx].fromDate ).format('DD/MM/YYYY');
						var tdate = moment($scope.otDataList[idx].toDate ).format('DD/MM/YYYY');
						angular.forEach($scope.dateList,function(value, index) {
							
							if(value.shiftDate == fdate){
								keyindx=value.key;
								otFlag = true;
							}

							if(value.shiftDate == tdate){
								keyindx=value.key;
								otFlag = true;
								
								
							}
						});
						}	
        		}
        		
        		if(otFlag == false){
        			
        			setDateList(fromDate, length,idx);
        			var data = {
        					fromDate : '',
        	     			toDate : '',
        	     			fromTime : '',
        	     			toTime : '',
        	     			switchOverTime : '',
        	     			example8model:[]
        			};
        			$scope.otDataList.push(data);
        		}else{
        			var dayCheckFlag = false;
        			angular.forEach($scope.otDataList[idx].example8model,function(value, index) {
						
        				angular.forEach($scope.otDataList[keyindx].example8model,function(value1, index1) {
    						
        					if(value.id == value1.id){
        						
        						dayCheckFlag = true;
        					}
    						
    					});
						
					});
        			
        			if(dayCheckFlag == false){
        				
        				setDateList(fromDate, length,idx);
            			var data = {
            					fromDate : '',
            	     			toDate : '',
            	     			fromTime : '',
            	     			toTime : '',
            	     			switchOverTime : '',
            	     			example8model:[]
            			};
            			$scope.otDataList.push(data);
            			
        				
        			}else{
        				
	        				var num = $scope.otDataList[keyindx].switchOverTime ;
	        				var hours = (num / 60);
	        				var rhours = Math.floor(hours);
	        				var minutes = (hours - rhours) * 60;
	        				var rminutes = Math.round(minutes);
	        				var sOtTime =rhours+":" + rminutes;
	        				//console.log(rhours+":" + rminutes);
	        				
	        				
	        				//var selectedToTime = ;
	        				 var selectedToTimeRes = $scope.otDataList[keyindx].toTime.split(":");
	        				
	        				 var selectedSwitchTimeRes = sOtTime.split(":");
	        				
	        				 var sumofHrs = parseInt(selectedToTimeRes[0]) + parseInt(selectedSwitchTimeRes[0]);
	        				 var sumOfMin =  parseInt(selectedToTimeRes[1]) + parseInt(selectedSwitchTimeRes[1]);
	        				 var finalSum;
	        				 if(sumOfMin>=60){
	        					 
	        					 var newsumofHrs = sumofHrs+":"+"00";
	        					 
	        					 var num = sumOfMin ;
	        						var hours = (num / 60);
	        						var rhours = Math.floor(hours);
	        						var minutes = (hours - rhours) * 60;
	        						var rminutes = Math.round(minutes);
	        						var sOtTime1 =rhours+":" + rminutes;
	        						
	        						 var selectedToTimeRes1 = newsumofHrs.split(":");
	        							
	        						 var selectedSwitchTimeRes1 = sOtTime1.split(":");
	        						
	        						 var sumofHrs1 = parseInt(selectedToTimeRes1[0]) + parseInt(selectedSwitchTimeRes1[0]);
	        						 var sumOfMin1 =  parseInt(selectedToTimeRes1[1]) + parseInt(selectedSwitchTimeRes1[1]);
	        						
	        						 finalSum = (sumofHrs1<10?"0"+sumofHrs1:sumofHrs1)+":"+(sumOfMin1<10?"0"+sumOfMin1:sumOfMin1);
	        				 }else{
	        					 
	        					 finalSum = (sumofHrs<10?"0"+sumofHrs:sumofHrs)+":"+(sumOfMin<10?"0"+sumOfMin:sumOfMin);
	        					 
	        				 }
	        				
	        					
		        			 if(finalSum<=$scope.otDataList[idx].fromTime){	
		        						
		        				 setDateList(fromDate, length,idx);
		        				 var data = {
		                    		fromDate : '',
		                    	     toDate : '',
		                    	     fromTime : '',
		                    	     toTime : '',
		                    	     switchOverTime : '',
		                    	     example8model:[]
		                    		};
		                    		$scope.otDataList.push(data);
		        			 }else{
		        					$rootScope.stopLoader();
		        					growl.warning("current from time Should be greater  than Previous TO time",
		        							{
		        								title : 'warning!'
		        							});
		        					
		        			}
        			}
        			
        		}
        	
        		$rootScope.stopLoader();
			
	    }else{
			$rootScope.stopLoader();
			growl.warning("From date shouble be less than ToDate",
					{
						title : 'warning!'
					});
				}
           }
		};
		
		
		//Remove OT time Details  from list
		$scope.removeOTTime = function(index) {
			var newDataList = [];
			var le = $scope.otDataList.length;
			var i = 1;
			if(le != i){
			angular.forEach($scope.otDataList, function(v) {
				if (index != i) {
					newDataList.push(v);
				}
				i++;
			});
			
			$scope.otDataList = newDataList;
			}
		}
		
		
		$scope.listCountForOT ;
		$scope.offsetForOT=0;
		$scope.noOfRecordForOT=10;
		//get ot Scheduling list
		$scope.otSchedulingList = [];
		$scope.getOtSchedulingList = function(offset,noOfRecordsPerPage){
			try{
				$rootScope.startLoader();
				$scope.listCountForOT = 0;
				offset = offset != null ? offset : 0;
				noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
						: 10;
				var data = {
						unitId:$scope.unitId,
						orgId:$scope.organizationId,	
						offset : offset,
						noOfRecordsPerPage : noOfRecordsPerPage
						};								
				var URI = BASE_URL + ROOT_URL+ "/global/otims/getOtScheduling";
				BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE)
				.then(function(response) {
							$scope.otSchedulingList = [];
							if (response.data.status == "success"){
								$rootScope.stopLoader();
									$scope.otSchedulingList = response.data.listObject;
									$scope.listCountForOT = response.data.object;
									$scope.setPageForOT(1,false);
							}
						});
				
			}catch(e){
				$rootScope.stopLoader();
				//console.log("Exception",e);
			}
		}
		
		$scope.getOtSchedulingList($scope.offsetForOT,$scope.noOfRecordForOT);
		
		
		//Save OT Scheduling List
		$scope.saveOtScheduling = function(){

                                                   var i=0;
                                                                         if($scope.otDataList[i].fromDate > $scope.otDataList[i].toDate){
                                                                                 									growl.warning("Please Enter From Date less than To Date ",{
                                                                                 										title : WARNING_MSG
                                                                                 									});
                                                                                 									return false;
                                                                                 								}

                                     								    if($scope.otDataList[i].fromTime > $scope.otDataList[i].toTime){
                                                                                 									growl.warning("Please Enter From Time less than To Time ",{
                                                                                 										title : WARNING_MSG
                                                                                 									});
                                                                                 									return false;
                                                                                 								}
			if($scope.theatreObj.operationTheatreId == '' || $scope.theatreObj.operationTheatreId == undefined || $scope.theatreObj.operationTheatreId == null){
				
				growl.warning("Please select theatre.",
						{
							title : 'warning!'
						});
				return false;
				
			}
			else if($scope.tableIds.length == 0){
				
				growl.warning("Please select table.",
						{
							title : 'warning!'
						});
				return false;
			}
			else{
			
				try{
					
					var i = 1;
					 var trueFalseFlag = true;
					angular.forEach($scope.otDataList,function(value,index) {
						 if(( value.fromDate == "" || value.fromDate == null || value.fromDate == undefined ) 
								 || (value.toDate == '' || value.toDate == null || value.toDate == undefined )
								|| (value.fromTime == '' || value.fromTime == null || value.fromTime == undefined)
								|| (value.toTime == '' || value.toTime == null || value.toTime == undefined )
								 || (value.switchOverTime == '' || value.switchOverTime == null || value.switchOverTime == undefined)
								 || (value.example8model.length == 0)){
							 
								 growl.warning("All fields are mandatory.",{
										title : WARNING_MSG
									});
								 trueFalseFlag = false;
									return false;
						 }
						 if(i == $scope.otDataList.length && trueFalseFlag == true ){
							 
				$rootScope.startLoader();
				
				$scope.timeDetails = [];
				angular.forEach($scope.otDataList, function(v) {
					if(v.fromTime!=""){
						var newArr ={};
						$scope.newDaysList = [];
						angular.forEach(v.example8model,
								function(value,index) {
									$scope.newDaysList .push(value.id);
								});
						
						newArr.fromDate = moment(v.fromDate).format('DD-MM-YYYY'),
						newArr.toDate = moment(v.toDate).format('DD-MM-YYYY'),
						newArr.fromTime = v.fromTime+":00",
						newArr.toTime = v.toTime+":00",
						newArr.switchOverTime = v.switchOverTime,
						newArr.days = $scope.newDaysList,
						$scope.timeDetails.push(newArr);
					}
				});
				$scope.param={
					
					theatreId:$scope.theatreObj.operationTheatreId,
					tableList:$scope.tableIds,
					timeDetails:$scope.timeDetails,
					status:"A",
					orgId:$scope.organizationId,
					unitId:$scope.unitId,
					createdBy:$scope.userId,
					createdDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
					updatedBy:$scope.userId,
					updatedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
					
				};

				var URI = BASE_URL + ROOT_URL + "/global/otims/saveOtScheduling";
				//console.log("$scope.param",$scope.param);
				GenericService.serviceAction("POST",URI,$scope.param).then(function(response){
					if (response.data.status == 'success')
					{
						$scope.otDataList = [];
						var data = {
            					fromDate : '',
            	     			toDate : '',
            	     			fromTime : '',
            	     			toTime : '',
            	     			switchOverTime : '',
            	     			example8model:[]
            			};
            			$scope.otDataList.push(data);
						$scope.theatreObj.operationTheatreId ="";
						$scope.tableIds=[];
						$scope.operationTableList = [];
						$scope.selectedTableList=[];
						$scope.timeDetails=[];
						$rootScope.stopLoader();
						$scope.getOtSchedulingList($scope.offsetForOT,$scope.noOfRecordForOT);
						growl.success(response.data.message,
								{
									title : 'Success!'
								});
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
						 i++;
					});
			}catch(e){
				//console.log("Exception",e);
			}
		}
		}
		
		
		// Update Ot Scheduling Status
		$scope.updateStatusOT = function(otSchedulingId ,status){
			try{
				var date = new Date();
				$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
				var data={
						status:status,
						otSchedulingId:otSchedulingId 
				};
				$rootScope.startLoader();
				var URI = BASE_URL + ROOT_URL + "/global/otims/deactivateOtSchedule";
				GenericService.serviceAction("POST",URI,data).then(function(response){
					if (response.data.status == 'success')
					{
						$scope.getOtSchedulingList($scope.offsetForOT,$scope.noOfRecordForOT);
						$rootScope.stopLoader();
						
						growl.success(response.data.message,
								{
									title : 'Success!'
								});
					} else if(response.data.status == 'error')
					{
						$rootScope.stopLoader();
						growl.error(response.data.message,
								{
									title : 'Error!'
								});
					}
				});
			}catch(e){
				//console.log("Exception",e);
			}
		}
		
		$scope.setNoOfRecordsOfOTScheduling = function() {
			$scope.getOtSchedulingList($scope.offsetForOT,$scope.noOfRecordForOT);
		};

		$scope.getMasterOtSchedulingList = function(offset,noOfRecords){
			try{
				$rootScope.startLoader();
				var data = {
						unitId:$scope.unitId,
						orgId:$scope.organizationId,	
						offset : offset,
						noOfRecordsPerPage : noOfRecords
						};								
				var URI = BASE_URL + ROOT_URL+ "/global/otims/getOtScheduling";
				BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE)
				.then(function(response) {
							$scope.otSchedulingList = [];
							if (response.data.status == "success"){
								$rootScope.stopLoader();
									$scope.otSchedulingList = response.data.listObject;
							}
						});
				
			}catch(e){
				$rootScope.stopLoader();
			}
		}
		
		$scope.otPager = {};
		$scope.page;

		$scope.setPageForOT = function(page, flag) {
			if (page < 1 || page > $scope.otPager.totalPages) {
				return;
			}
			$scope.otPager = PagerService.GetPager(
					$scope.listCountForOT, page,
					$scope.noOfRecordForOT);
			if (flag) {
				$scope.getMasterOtSchedulingList($scope.otPager.startIndex,	$scope.otPager.pageSize);
			}
		}

/**************************************************************OT Scheduling END*****************************************************************/
		
/***********************************************Equipment Scheduling START*************************************************************************/

		$scope.equipmentTypeObj = {
				id:"",
				category:""
		}
		
		$scope.equipmentNameObj = {
				batchId:"",
				itemId:"",
				itemName:""
		}
		
		$scope.equipmentTypeList = [];
		$scope.getEquipmentTypeList = function () {
			var URI = BASE_URL + ROOT_URL + "/api/global/assets/categories/2/"+$scope.unitId+"/"+$scope.organizationId; //2 for Fixed asset type
			BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				$scope.equipmentTypeList = response.data;
				
			});
		   }
		
		$scope.getEquipmentTypeList();				
						
						
		$scope.equipmentBasicNameList = [];
		$scope.equipmentList = [];
		
		$scope.getEquipmentNamesByEquipmentId = function (equipmentTypeId) {
			$rootScope.startLoader();
			var data = {
					unitId:$scope.unitId,
					assetCategoryId:equipmentTypeId
			};
			var URI = BASE_URL + ROOT_URL + "/api/global/items/otims/getItemAndBatchList"; 
			BillingGenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				$scope.equipmentBasicNameList = response.data;
				
				angular.forEach($scope.equipmentBasicNameList,
						function(value,index) {
							var data = {};
							data.id = value.batchId;
							data.label = value.itemName;
							$scope.equipmentList.push(data);
						});
				
				$rootScope.stopLoader();	
			});
		   }				
						
						
						
		 $scope.otEquipmentList = [ {
				fromDate : '',
				toDate : '',
				fromTime : '',
				toTime : '',
				example8model:[]
			} ];
	        
	        
	      //Loop DatePicker Start
			// for datePicker
		 var today = new Date(); // Get the current date
		  var maxDate = new Date(today.getTime() + (365 * 24 * 60 * 60 * 1000));
		var today = new Date();
		$scope.dateOptions = {
		 // Get the current date
		//var maxDate = new Date(today.getTime() + (365 * 24 * 60 * 60 * 1000));
			formatYear : 'yyyy',
			showWeeks : false,
			minDate : today,
			maxDate : new Date(today.getTime() + (365 * 24 * 60 * 60 * 1000)),
		};
			
			//for From Date
			$scope.openEquipFromDatePicker = function(index) {
				for (var i = $scope.otEquipmentList.length - 1; i >= 0; i--) {
					if (i === index) {

						$scope.fromDate.opened[i] = true;
					} else {
						$scope.fromDate.opened[i] = false;
					}
				}
			};

			$scope.fromDate = {
				opened : [ false ],

			};
			//End From Date
			
			//for To Date
			$scope.openEquipToDattePicker = function(index) {
				for (var i = $scope.otEquipmentList.length - 1; i >= 0; i--) {
					if (i === index) {

						$scope.toDate.opened[i] = true;
					} else {
						$scope.toDate.opened[i] = false;
					}
				}
			};

			$scope.toDate = {
				opened : [ false ],

			};
			//End To Date
			
			$scope.selectedEquipmentList = [];
			$scope.batchAndItems = [];
			$scope.clickedOnEquipment = function(){
				$scope.batchAndItems = [];
				if ($scope.selectedEquipmentList.length > 0) {
					
					angular.forEach($scope.equipmentBasicNameList,function(value, index) {
						
						angular.forEach($scope.selectedEquipmentList,function(value1, index1) {
							if(value.batchId==value1.id){
								var newArr = {};
								newArr.batchId = value.batchId;
								newArr.itemId = value.itemId;
								$scope.batchAndItems.push(newArr);
							}
						});
					});
				
					var isExists;
					function checkExistsOrNot(arr, val) {
						return arr
								.some(function(arr) {
									return (val.id === arr.id);
								});
						}
						}
			}			
						
			
			//select Days
        	$scope.equipDaysIds = [];
        	$scope.clickedOnEquipDaysList = function(idx) {
        		
        		$scope.equipDaysIds = [];
        	if ($scope.otEquipmentList[idx].example8model.length > 0) {
			
			angular.forEach($scope.otEquipmentList[idx].example8model,
					function(value, index) {
				$scope.equipDaysIds.push(value.id);
					});
		
			var isExists;
			function checkExistsOrNot(arr, val) {
				return arr
						.some(function(arr) {
							return (val.id === arr.id);
						});
				}
				}
        	}

        	
        	
        	$scope.equipDateList = [];
        	 var setEquipDateList = function(fromDate,length,idx){
        		 var i =0, year, month, day, shiftDate,date;
            		for (i = 0; i <= length; i++) {
    					var pdate = moment(fromDate, "DD/MM/YYYY")
    							.add('days', i).toDate();
    					year = pdate.getFullYear();
    					month = pdate.getMonth();
    					month = (Number(pdate.getMonth()) + 1) <= 9 ? "0"
    							+ (Number(pdate.getMonth()) + 1)
    							: (Number(pdate.getMonth()) + 1);
    					date = pdate.getDate() <= 9 ? "0"
    							+ pdate.getDate() : pdate.getDate();
    					shiftDate = date + "/" + month + "/" + year;
    					var data = {
    							shiftDate : shiftDate,
            	     			key : idx
            	     			
            			};
    					$scope.equipDateList.push(data);
    				}
            	
        		
        	}
        	 
						
			 //Add OT time details in list
	        $scope.addEquipTime = function(idx) {
	        	$rootScope.startLoader();
	        	var fromDate = $scope.otEquipmentList[idx].fromDate;
	        	var toDate = $scope.otEquipmentList[idx].toDate;
	        	var otFlag = false;
	        	if($scope.otEquipmentList[idx].fromDate==""){
	        		
	        		$rootScope.stopLoader();
					growl.warning("Please select From Date",
							{
								title : 'warning!'
							});
					
	        	}else if($scope.otEquipmentList[idx].toDate==""){
	        		
	        		$rootScope.stopLoader();
					growl.warning("Please select To Date",
							{
								title : 'warning!'
							});
					
	        	}else if($scope.otEquipmentList[idx].fromTime==""){
	        		
	        		$rootScope.stopLoader();
					growl.warning("Please select From Time",
							{
								title : 'warning!'
							});
					
	        	}else if($scope.otEquipmentList[idx].toTime==""){
	        		
	        		$rootScope.stopLoader();
					growl.warning("Please select To Time",
							{
								title : 'warning!'
							});
					
	        		
	        	}else if($scope.otEquipmentList[idx].example8model.length==0){
	        		
	        		$rootScope.stopLoader();
					growl.warning("Please select Days",
							{
								title : 'warning!'
							});
					
	        	}else{ 
	        		if(fromDate <= toDate){
	        		var keyindx;
	        		var i =0, year, month, day, shiftDate;
	        		var length = Math.floor((Date.UTC(toDate.getFullYear(),
	        				toDate.getMonth(),
							toDate.getDate()) - Date.UTC(fromDate.getFullYear(), 
							fromDate.getMonth(),
							fromDate.getDate()))/ (1000 * 60 * 60 * 24));
	        		
	        		if(idx>0){
	        		
						if(length>0){
							var fdate = moment($scope.otEquipmentList[idx].fromDate ).format('DD/MM/YYYY');
							var tdate = moment($scope.otEquipmentList[idx].toDate ).format('DD/MM/YYYY');
							angular.forEach($scope.equipDateList,function(value, index) {
								
								if(value.shiftDate == fdate){
									keyindx=value.key;
									otFlag = true;
								}

								if(value.shiftDate == tdate){
									keyindx=value.key;
									otFlag = true;
									
									
								}
							});
							}	
	        		}
	        		
	        		if(otFlag == false){
	        			
	        			setEquipDateList(fromDate, length,idx);
	        			var data = {
	        					fromDate : '',
	        	     			toDate : '',
	        	     			fromTime : '',
	        	     			toTime : '',
	        	     			example8model:[]
	        			};
	        			$scope.otEquipmentList.push(data);
	        			
	        			 $rootScope.stopLoader();	
	        		}else{
	        			var dayCheckFlag = false;
	        			angular.forEach($scope.otEquipmentList[idx].example8model,function(value, index) {
							
	        				angular.forEach($scope.otEquipmentList[keyindx].example8model,function(value1, index1) {
	    						
	        					if(value.id == value1.id){
	        						
	        						dayCheckFlag = true;
	        					}
	    						
	    					});
							
						});
	        			
	        			if(dayCheckFlag == false){
	        				
	        				setEquipDateList(fromDate, length,idx);
	            			var data = {
	            					fromDate : '',
	            	     			toDate : '',
	            	     			fromTime : '',
	            	     			toTime : '',
	            	     			example8model:[]
	            			};
	            			$scope.otEquipmentList.push(data);
	            			
	            			$rootScope.stopLoader();
	        			}else{
			        			 if($scope.otEquipmentList[keyindx].toTime<=$scope.otEquipmentList[idx].fromTime){	
			        					
			        				 setEquipDateList(fromDate, length,idx);
			        				 var data = {
			                    		fromDate : '',
			                    	     toDate : '',
			                    	     fromTime : '',
			                    	     toTime : '',
			                    	     example8model:[]
			                    		};
			                    		$scope.otEquipmentList.push(data);
			                    		$rootScope.stopLoader();	
			        			 }else{
			        					$rootScope.stopLoader();
			        					growl.warning("current from time Should be greater  than Previous TO time",
			        							{
			        								title : 'warning!'
			        							});
			        			}
	        			}
	        		}
	           }else{
	        	   $rootScope.stopLoader();
	        	   growl.warning("From date shouble be less than ToDate",
	   					{
	   						title : 'warning!'
	   					});
	           }
			}
	        }
			
			//Remove Equipment time Details  from list
			$scope.removeEquipTime = function(index) {
				var newDataList = [];
				var le = $scope.otEquipmentList.length;
				var i = 1;
				if(le != i){
				angular.forEach($scope.otEquipmentList, function(v) {
					if (index != i) {
						newDataList.push(v);
					}
					i++;
				});
				
				$scope.otEquipmentList = newDataList;
				}
			}
						
			//get equipment Scheduling list
			$scope.equipOffset = 0;
			$scope.noOfEquipRecordsPerPage = 10;
			$scope.totalEquipNoOfRecords;
			$scope.commonEquipmentListCount;
			
			$scope.initEquipmentList = function(offset, noOfRecordsPerPage) {
				$scope.otEquipmentdataList = [];
				var data = "";
				offset = offset != null ? offset : 0;
				noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
						: 10;
				var data = {
						unitId:$scope.unitId,
						orgId:$scope.organizationId,
						offset : offset,
						noOfRecordsPerPage : noOfRecordsPerPage
						};								
				var URI = BASE_URL + ROOT_URL+ "/global/otims/equipmentScheduling/getEquipmentScheduling";
				$rootScope.startLoader();
				BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE)
				.then(function(response) {
							$rootScope.stopLoader();
							$scope.otEquipmentdataList  = response.data.listObject;
							$scope.commonEquipmentListCount = response.data.object;
							$scope.setPageForOTEquip(1,false);
						});
			}
			$scope.initEquipmentList($scope.equipOffset, $scope.noOfEquipRecordsPerPage);
				
			$scope.otEquipmentdataList = [];
			$scope.getOtEquipmentList = function(offset,noOfRecordsPerPage){
				try{
					$rootScope.startLoader();
					
					offset = offset != null ? offset : 0;
					noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
							: 10;
					var data = {
							unitId:$scope.unitId,
							orgId:$scope.organizationId,
							offset : offset,
							noOfRecordsPerPage : noOfRecordsPerPage
							};								
					var URI = BASE_URL + ROOT_URL+ "/global/otims/equipmentScheduling/getEquipmentScheduling";
					BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE)
					.then(function(response) {
								$scope.otEquipmentdataList = [];
								if (response.data.status == "success"){
									$rootScope.stopLoader();
										$scope.otEquipmentdataList = response.data.listObject;
								}
							});
					
				}catch(e){
					$rootScope.stopLoader();
					//console.log("Exception",e);
				}
			}
			
		//	$scope.getOtEquipmentList(	$scope.offset,$scope.noOfRecordsPerPage);
			
			
			//Save OT Scheduling List
			$scope.newEquipDaysList = [];
			$scope.saveOtEquipment = function(){
                                        var i=0;
                                                                         if($scope.otEquipmentList[i].fromDate > $scope.otEquipmentList[i].toDate){
                                                                                 									growl.warning("Please Enter From Date less than To Date ",{
                                                                                 										title : WARNING_MSG
                                                                                 									});
                                                                                 									return false;
                                                                                 								}

                                     								    if($scope.otEquipmentList[i].fromTime > $scope.otEquipmentList[i].toTime){
                                                                                 									growl.warning("Please Enter From Time less than To Time ",{
                                                                                 										title : WARNING_MSG
                                                                                 									});
                                                                                 									return false;
                                                                                 								}

				if($scope.equipmentTypeObj.id == '' ||$scope.equipmentTypeObj.id == undefined || $scope.equipmentTypeObj.id == null){
					
					growl.warning("Please select type.",
							{
								title : 'warning!'
							});
					return false;
					
				}else if($scope.selectedEquipmentList.length == 0){
					growl.warning("Please select equipment.",
							{
								title : 'warning!'
							});
					return false;
				}
				else {
				
				try{
					
					var i = 1;
					 var trueFalseFlag = true;
					angular.forEach($scope.otEquipmentList,function(value,index) {
						 if(( value.fromDate == "" || value.fromDate == null || value.fromDate == undefined ) 
								 || (value.toDate == '' || value.toDate == null || value.toDate == undefined )
								|| (value.fromTime == '' || value.fromTime == null || value.fromTime == undefined)
								|| (value.toTime == '' || value.toTime == null || value.toTime == undefined )
								 || (value.example8model.length == 0)){
							 
								 growl.warning("All fields are mandatory.",{
										title : WARNING_MSG
									});
								 trueFalseFlag = false;
									return false;
						 }
						 if(i == $scope.otEquipmentList.length && trueFalseFlag == true ){
							 
					$rootScope.startLoader();
					
					$scope.timeDetails = [];
					angular.forEach($scope.otEquipmentList, function(v) {
						if(v.fromTime!=""){
							var newArr ={};
							$scope.newEquipDaysList = [];
							angular.forEach(v.example8model,
									function(value,index) {
										$scope.newEquipDaysList .push(value.id);
									});
							
							newArr.fromDate = moment(v.fromDate).format('DD-MM-YYYY'),
							newArr.toDate = moment(v.toDate).format('DD-MM-YYYY'),
							newArr.fromTime = v.fromTime+":00",
							newArr.toTime = v.toTime+":00",
							newArr.days = $scope.newEquipDaysList,
							$scope.timeDetails.push(newArr);
						}
					});
					$scope.param={
						
						assetCategoryId:$scope.equipmentTypeObj.id,
						batchAndItem:$scope.batchAndItems,
						timeDetails:$scope.timeDetails,
						status:"A",
						orgId:$scope.organizationId,
						unitId:$scope.unitId,
						createdBy:$scope.userId,
						createdDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
						updatedBy:$scope.userId,
						updatedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
						
					};
					//console.log("$scope.param",$scope.param);
					$rootScope.startLoader();
					var URI = BASE_URL + ROOT_URL + "/global/otims/equipmentScheduling/saveEquipmentScheduling";
					GenericService.serviceAction("POST",URI,$scope.param).then(function(response){
						if (response.data.status == 'success')
						{
							$scope.otEquipmentList = [];
							var data = {
	            					fromDate : '',
	            	     			toDate : '',
	            	     			fromTime : '',
	            	     			toTime : '',
	            	     			example8model:[]
	            			};
	            			$scope.otEquipmentList.push(data);
	            			$scope.equipmentTypeObj.id ="";
							$scope.batchAndItems=[];
							$scope.equipmentBasicNameList = [];
							$scope.selectedEquipmentList=[];
							$scope.equipmentList = [];
							$scope.timeDetails=[];
							$rootScope.stopLoader();
							$scope.getOtEquipmentList($scope.offset,$scope.noOfRecordsPerPage);
							growl.success(response.data.message,
									{
										title : 'Success!'
									});
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
				i++;
				});
				}catch(e){
					//console.log("Exception",e);
				}
			}
			}
			
			
			// Update Ot Scheduling Status
			$scope.updateEquipStatus = function(equipSchedulingId ,status){
				try{
					var date = new Date();
					$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
					var data={
							status:status,
							equipmentSchedulingId:equipSchedulingId 
					};
					$rootScope.startLoader();
					var URI = BASE_URL + ROOT_URL + "/global/otims/equipmentScheduling/deActivateEquipmentScheduling";
					//console.log("update status",data);
					GenericService.serviceAction("POST",URI,data).then(function(response){
						if (response.data.status == 'success')
						{
							$scope.getOtEquipmentList(	$scope.offset,$scope.noOfRecordsPerPage);
							$rootScope.stopLoader();
							
							growl.success(response.data.message,
									{
										title : 'Success!'
									});
						} else if(response.data.status == 'error')
						{
							$rootScope.stopLoader();
							growl.error(response.data.message,
									{
										title : 'Error!'
									});
						}
					});
				}catch(e){
					//console.log("Exception",e);
				}
			}
			
			$scope.setNoOfRecordsOfOTEquipScheduling = function() {
				$scope.initEquipmentList($scope.equipOffset, $scope.noOfEquipRecordsPerPage);
			};

			
			$scope.equipmentPager = {};
			$scope.page;

			$scope.setPageForOTEquip = function(page, flag) {
				if (page < 1 || page > $scope.equipmentPager.totalPages) {
					return;
				}
				$scope.equipmentPager = PagerService.GetPager(
						$scope.commonEquipmentListCount, page,
						$scope.noOfEquipRecordsPerPage);
				if (flag) {
					$scope.getOtEquipmentList($scope.equipmentPager.startIndex,	$scope.equipmentPager.pageSize);
				}
			}
				
						
						
						
						
						
						
						
/***********************************************  Equipment Scheduling END  *************************************************************************/
			
/***********************************************  STAFF Scheduling START  *************************************************************************/

			$scope.otStaffTimeList = [ {
				fromDate : '',
				toDate : '',
				fromTime : '',
				toTime : '',
				switchOverTime : '',
				example8model:[]
			} ];
			
			$scope.roleObj ={
					roleId:"",
					roleName:""
			};
			
			 //Loop DatePicker Start
			// for datePicker
			var today = new Date(); // Get the current date
			  var maxDate = new Date(today.getTime() + (365 * 24 * 60 * 60 * 1000));
			var today = new Date();
			$scope.dateOptionsfromDate1 = {
					formatYear : 'yyyy',
					showWeeks : false,
					minDate : today,
					maxDate : new Date(today.getTime() + (365 * 24 * 60 * 60 * 1000)),
				};
			
			var today = new Date(); // Get the current date
			  var maxDate = new Date(today.getTime() + (365 * 24 * 60 * 60 * 1000));
			var today = new Date();
			$scope.dateOptionsfromDate2 = {
					formatYear : 'yyyy',
					showWeeks : false,
					minDate : today,
					maxDate : new Date(today.getTime() + (365 * 24 * 60 * 60 * 1000)),
				};
			
			var today = new Date(); // Get the current date
			  var maxDate = new Date(today.getTime() + (365 * 24 * 60 * 60 * 1000));
			var today = new Date();
			$scope.dateOptionsfromDate3 = {
					formatYear : 'yyyy',
					showWeeks : false,
					minDate : today,
					maxDate : new Date(today.getTime() + (365 * 24 * 60 * 60 * 1000)),
				};
			
			var today = new Date(); // Get the current date
			  var maxDate = new Date(today.getTime() + (365 * 24 * 60 * 60 * 1000));
			var today = new Date();
			$scope.dateOptionsfromDate4 = {
					formatYear : 'yyyy',
					showWeeks : false,
					minDate : today,
					maxDate : new Date(today.getTime() + (365 * 24 * 60 * 60 * 1000)),
				};
			
			var today = new Date(); // Get the current date
			  var maxDate = new Date(today.getTime() + (365 * 24 * 60 * 60 * 1000));
			var today = new Date();
			$scope.dateOptionsfromDate5 = {
					formatYear : 'yyyy',
					showWeeks : false,
					minDate : today,
					maxDate : new Date(today.getTime() + (365 * 24 * 60 * 60 * 1000)),
				};
			
			var today = new Date(); // Get the current date
			  var maxDate = new Date(today.getTime() + (365 * 24 * 60 * 60 * 1000));
			var today = new Date();
			$scope.dateOptionsfromDate6 = {
					formatYear : 'yyyy',
					showWeeks : false,
					minDate : today,
					maxDate : new Date(today.getTime() + (365 * 24 * 60 * 60 * 1000)),
				};
			
			var today = new Date(); // Get the current date
			  var maxDate = new Date(today.getTime() + (365 * 24 * 60 * 60 * 1000));
			var today = new Date();
			$scope.dateOptionsfromDate7 = {
					formatYear : 'yyyy',
					showWeeks : false,
					minDate : today,
					maxDate : new Date(today.getTime() + (365 * 24 * 60 * 60 * 1000)),
				};
			
			var today = new Date(); // Get the current date
			  var maxDate = new Date(today.getTime() + (365 * 24 * 60 * 60 * 1000));
			var today = new Date();
			$scope.dateOptionsfromDate8 = {
					formatYear : 'yyyy',
					showWeeks : false,
					minDate : today,
					maxDate : new Date(today.getTime() + (365 * 24 * 60 * 60 * 1000)),
				};
			
			
			var today = new Date(); // Get the current date
			  var maxDate = new Date(today.getTime() + (365 * 24 * 60 * 60 * 1000));
			var today = new Date();
			$scope.dateOptions = {
			 // Get the current date
			//var maxDate = new Date(today.getTime() + (365 * 24 * 60 * 60 * 1000));
				formatYear : 'yyyy',
				showWeeks : false,
				minDate : today,
				maxDate : new Date(today.getTime() + (365 * 24 * 60 * 60 * 1000)),
			};
			
			//for From Date
			$scope.openStaffFromDatePicker = function(index) {
				for (var i = $scope.otStaffTimeList.length - 1; i >= 0; i--) {
					if (i === index) {

						$scope.fromDate.opened[i] = true;
					} else {
						$scope.fromDate.opened[i] = false;
					}
				}
			};

			$scope.fromDate = {
				opened : [ false ],

			};
			//End From Date
			
			//for To Date
			$scope.openStaffToDattePicker = function(index) {
				for (var i = $scope.otStaffTimeList.length - 1; i >= 0; i--) {
					if (i === index) {

						$scope.toDate.opened[i] = true;
					} else {
						$scope.toDate.opened[i] = false;
					}
				}
			};

			$scope.toDate = {
				opened : [ false ],

			};
			//End To Date
			
			
			
			$scope.otimsRoleList = [];
			$scope.otimsRole = function() {
				$rootScope.startLoader();
				var data = {
						organisationId:$scope.organizationId,
						unitId:$scope.unitId
						};
				
				var URI = BASE_URL + ROOT_URL + "/api/global//getActiveOTRoleList";
				GenericService.serviceAction("POST", URI, data).then(function(response) {
					$rootScope.stopLoader();
					$scope.otimsRoleList = response.data.listObject;
				});
			}				
			$scope.otimsRole();

			$scope.selectedStaffList = [];
			$scope.staffList = [];
			$scope.getStaffListByRoleId = function(roleId){
				$scope.staffList = [];
				$scope.selectedStaffList = [];
				$rootScope.startLoader();
				var data = {
						orgId:$scope.organizationId,
						unitId:$scope.unitId,
						roleId:roleId
						};
				
				var URI = BASE_URL + ROOT_URL + "/global/otims/getStaffListByRoleId";
				GenericService.serviceAction("POST", URI, data).then(function(response) {
					$rootScope.stopLoader();
					angular.forEach(response.data.listObject,function(value, index) {
						var newArr = {};
						newArr.id = value.userId;
						newArr.label = value.userName;
						$scope.staffList.push(newArr);
							});
				});
			}
			
		
			$scope.selectOtStaff = function(){
				
				$scope.userIds = [];
				if ($scope.selectedStaffList.length > 0) {
					
					angular.forEach($scope.selectedStaffList,
							function(value, index) {
						$scope.userIds.push(value.id);
							});
				
					var isExists;
					function checkExistsOrNot(arr, val) {
						return arr
								.some(function(arr) {
									return (val.id === arr.id);
								});
						}
						}
			}
			
			$scope.clickedOnStaffDaysList = function(idx){
				
				$scope.otStaffDaysIds = [];
	        	if ($scope.otStaffTimeList[idx].example8model.length > 0) {
				
				angular.forEach($scope.otStaffTimeList[idx].example8model,
						function(value, index) {
					$scope.otStaffDaysIds.push(value.id);
						});
			
				var isExists;
				function checkExistsOrNot(arr, val) {
					return arr
							.some(function(arr) {
								return (val.id === arr.id);
							});
					}
					}
			}
			
			$scope.otStaffDateList = [];
       	 var setOtStaffDateList = function(fromDate,length,idx){
       		 var i =0, year, month, day, shiftDate,date;
           		for (i = 0; i <= length; i++) {
   					var pdate = moment(fromDate, "DD/MM/YYYY")
   							.add('days', i).toDate();
   					year = pdate.getFullYear();
   					month = pdate.getMonth();
   					month = (Number(pdate.getMonth()) + 1) <= 9 ? "0"
   							+ (Number(pdate.getMonth()) + 1)
   							: (Number(pdate.getMonth()) + 1);
   					date = pdate.getDate() <= 9 ? "0"
   							+ pdate.getDate() : pdate.getDate();
   					shiftDate = date + "/" + month + "/" + year;
   					var data = {
   							shiftDate : shiftDate,
           	     			key : idx
           	     			
           			};
   					$scope.otStaffDateList.push(data);
   				}
           	
       		
       	}
			
			$scope.addStaffTimeDetails = function(idx){
				
				$rootScope.startLoader();
	        	var fromDate = $scope.otStaffTimeList[idx].fromDate;
	        	var toDate = $scope.otStaffTimeList[idx].toDate;
	        	var otFlag = false;
	        	if($scope.otStaffTimeList[idx].fromDate==""){
	        		
	        		$rootScope.stopLoader();
					growl.warning("Please select From Date",
							{
								title : 'warning!'
							});
					
	        	}else if($scope.otStaffTimeList[idx].toDate==""){
	        		
	        		$rootScope.stopLoader();
					growl.warning("Please select To Date",
							{
								title : 'warning!'
							});
					
	        	}else if($scope.otStaffTimeList[idx].fromTime==""){
	        		
	        		$rootScope.stopLoader();
					growl.warning("Please select From Time",
							{
								title : 'warning!'
							});
					
	        	}else if($scope.otStaffTimeList[idx].toTime==""){
	        		
	        		$rootScope.stopLoader();
					growl.warning("Please select To Time",
							{
								title : 'warning!'
							});
					
	        		
	        	}else if($scope.otStaffTimeList[idx].example8model.length==0){
	        		
	        		$rootScope.stopLoader();
					growl.warning("Please select Days",
							{
								title : 'warning!'
							});
					
	        	}else{ 
	        		if(fromDate <= toDate){
	        		var keyindx;
	        		var i =0, year, month, day, shiftDate;
	        		var length = Math.floor((Date.UTC(toDate.getFullYear(),
	        				toDate.getMonth(),
							toDate.getDate()) - Date.UTC(fromDate.getFullYear(), 
							fromDate.getMonth(),
							fromDate.getDate()))/ (1000 * 60 * 60 * 24));
	        		
	        		if(idx>0){
	        		
						if(length>0){
							var fdate = moment($scope.otStaffTimeList[idx].fromDate ).format('DD/MM/YYYY');
							var tdate = moment($scope.otStaffTimeList[idx].toDate ).format('DD/MM/YYYY');
							angular.forEach($scope.otStaffDateList,function(value, index) {
								
								if(value.shiftDate == fdate){
									keyindx=value.key;
									otFlag = true;
								}

								if(value.shiftDate == tdate){
									keyindx=value.key;
									otFlag = true;
									
									
								}
							});
							}	
	        		}
	        		
	        		if(otFlag == false){
	        			
	        			setOtStaffDateList(fromDate, length,idx);
	        			var data = {
	        					fromDate : '',
	        	     			toDate : '',
	        	     			fromTime : '',
	        	     			toTime : '',
	        	     			switchOverTime : '',
	        	     			example8model:[]
	        			};
	        			$scope.otStaffTimeList.push(data);
	        		}else{
	        			var dayCheckFlag = false;
	        			angular.forEach($scope.otStaffTimeList[idx].example8model,function(value, index) {
							
	        				angular.forEach($scope.otStaffTimeList[keyindx].example8model,function(value1, index1) {
	    						
	        					if(value.id == value1.id){
	        						
	        						dayCheckFlag = true;
	        					}
	    						
	    					});
							
						});
	        			
	        			if(dayCheckFlag == false){
	        				
	        				setOtStaffDateList(fromDate, length,idx);
	            			var data = {
	            					fromDate : '',
	            	     			toDate : '',
	            	     			fromTime : '',
	            	     			toTime : '',
	            	     			switchOverTime : '',
	            	     			example8model:[]
	            			};
	            			$scope.otStaffTimeList.push(data);
	        				
	        			}else{
	        				
	        					if($scope.otStaffTimeList[keyindx].switchOverTime!="" && $scope.otStaffTimeList[keyindx].switchOverTime!=undefined){
	        					
	        						var num = $scope.otStaffTimeList[keyindx].switchOverTime ;
			        				var hours = (num / 60);
			        				var rhours = Math.floor(hours);
			        				var minutes = (hours - rhours) * 60;
			        				var rminutes = Math.round(minutes);
			        				var sOtTime =rhours+":" + rminutes;
			        				//console.log(rhours+":" + rminutes);
			        				
			        				
			        				//var selectedToTime = ;
			        				 var selectedToTimeRes = $scope.otStaffTimeList[keyindx].toTime.split(":");
			        				
			        				 var selectedSwitchTimeRes = sOtTime.split(":");
			        				
			        				 var sumofHrs = parseInt(selectedToTimeRes[0]) + parseInt(selectedSwitchTimeRes[0]);
			        				 var sumOfMin =  parseInt(selectedToTimeRes[1]) + parseInt(selectedSwitchTimeRes[1]);
			        				 var finalSum;
			        				 if(sumOfMin>=60){
			        					 
			        					 var newsumofHrs = sumofHrs+":"+"00";
			        					 
			        					 var num = sumOfMin ;
			        						var hours = (num / 60);
			        						var rhours = Math.floor(hours);
			        						var minutes = (hours - rhours) * 60;
			        						var rminutes = Math.round(minutes);
			        						var sOtTime1 =rhours+":" + rminutes;
			        						
			        						 var selectedToTimeRes1 = newsumofHrs.split(":");
			        							
			        						 var selectedSwitchTimeRes1 = sOtTime1.split(":");
			        						
			        						 var sumofHrs1 = parseInt(selectedToTimeRes1[0]) + parseInt(selectedSwitchTimeRes1[0]);
			        						 var sumOfMin1 =  parseInt(selectedToTimeRes1[1]) + parseInt(selectedSwitchTimeRes1[1]);
			        						
			        						 finalSum = (sumofHrs1<10?"0"+sumofHrs1:sumofHrs1)+":"+(sumOfMin1<10?"0"+sumOfMin1:sumOfMin1);
			        				 }else{
			        					 
			        					 finalSum = (sumofHrs<10?"0"+sumofHrs:sumofHrs)+":"+(sumOfMin<10?"0"+sumOfMin:sumOfMin);
			        					 
			        				 }
			        				 
			        				 if(finalSum<=$scope.otStaffTimeList[idx].fromTime){	
			        						
				        				 setOtStaffDateList(fromDate, length,idx);
				        				 var data = {
				                    		fromDate : '',
				                    	     toDate : '',
				                    	     fromTime : '',
				                    	     toTime : '',
				                    	     switchOverTime : '',
				                    	     example8model:[]
				                    		};
				                    		$scope.otStaffTimeList.push(data);
				                    			
				        			 }else{
				        					$rootScope.stopLoader();
				        					growl.warning("current from time Should be greater  than Previous TO time",
				        							{
				        								title : 'warning!'
				        							});
				        					
				        			}
	        					}else{
	        						
	        						if($scope.otStaffTimeList[keyindx].toTime<=$scope.otStaffTimeList[idx].fromTime){
			        						
				        				 setOtStaffDateList(fromDate, length,idx);
				        				 var data = {
				                    		fromDate : '',
				                    	     toDate : '',
				                    	     fromTime : '',
				                    	     toTime : '',
				                    	     switchOverTime : '',
				                    	     example8model:[]
				                    		};
				                    		//console.log("$scope.dt",data);
				                    		$scope.otStaffTimeList.push(data);
				                    			
				        			 }else{
				        					$rootScope.stopLoader();
				        					growl.warning("current from time Should be greater  than Previous TO time",
				        							{
				        								title : 'warning!'
				        						});
				        			}
	        					}
		        					
			        			
	        			}
	        			
	        		}
	        	
	        		$rootScope.stopLoader();
				
		    }else{
				$rootScope.stopLoader();
				growl.warning("From date shouble be less than ToDate",
						{
							title : 'warning!'
						});
					}
	           }
			}
			
			$scope.removeStaffTimeDetails = function(index){
				var newDataList = [];
				var le = $scope.otStaffTimeList.length;
				var i = 1;
				if(le != i){
				angular.forEach($scope.otStaffTimeList, function(v) {
					if (index != i) {
						newDataList.push(v);
					}
					i++;
				});
				
				$scope.otStaffTimeList = newDataList;
				}
			}
			
			
			//get ot staff Scheduling list
			$scope.OTUserRecordsPerPage = 10;
			$scope.OTUserOffset = 0;
			$scope.OTUserListCount ;
			$scope.otStaffSchedulingList = [];
			$scope.getOtStaffSchedulingList = function(offset,noOfRecordsPerPage){
				try{
					$rootScope.startLoader();
					$scope.OTUserListCount = 0;
					$scope.otStaffSchedulingList = [];
					//offset = offset != null ? offset : 0;
				//	noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
				//			: 10;
					var data = {
							unitId:$scope.unitId,
							orgId:$scope.organizationId,
							offset : offset,
							noOfRecordsPerPage : noOfRecordsPerPage
							};								
					var URI = BASE_URL + ROOT_URL+ "/global/otims/getStaffScheduling";
					BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE)
					.then(function(response) {
								if (response.data.status == "success"){
									$rootScope.stopLoader();
										$scope.otStaffSchedulingList = response.data.listObject;
										$scope.OTUserListCount  = response.data.object;
										$scope.setPageForOTStaff(1,false);
								}
							});
					
				}catch(e){
					$rootScope.stopLoader();
					//console.log("Exception",e);
				}
			}
			
			$scope.getOtStaffSchedulingList($scope.OTUserOffset,$scope.OTUserRecordsPerPage);
			
			
			$scope.saveOtStaffScheduling = function(){

                                                  var i=0;
                                                                         if($scope.otStaffTimeList[i].fromDate > $scope.otStaffTimeList[i].toDate){
                                                                                 									growl.warning("Please Enter From Date less than To Date ",{
                                                                                 										title : WARNING_MSG
                                                                                 									});
                                                                                 									return false;
                                                                                 								}

                                     								    if($scope.otStaffTimeList[i].fromTime > $scope.otStaffTimeList[i].toTime){
                                                                                 									growl.warning("Please Enter From Time less than To Time ",{
                                                                                 										title : WARNING_MSG
                                                                                 									});
                                                                                 									return false;
                                                                                 								}
				if($scope.roleObj.roleId == '' ||$scope.roleObj.roleId == undefined || $scope.roleObj.roleId == null){
									
									growl.warning("Please select OT role.",
											{
												title : 'warning!'
											});
									return false;
									
								}else if($scope.selectedStaffList.length == 0){
									growl.warning("Please select staff.",
											{
												title : 'warning!'
											});
									return false;
								}
								else {
				try{

					var i = 1;
					 var trueFalseFlag = true;
					angular.forEach($scope.otStaffTimeList,function(value,index) {
						 if(( value.fromDate == "" || value.fromDate == null || value.fromDate == undefined ) 
								 || (value.toDate == '' || value.toDate == null || value.toDate == undefined )
								|| (value.fromTime == '' || value.fromTime == null || value.fromTime == undefined)
								|| (value.toTime == '' || value.toTime == null || value.toTime == undefined )
								|| (value.switchOverTime == '' || value.switchOverTime == null || value.switchOverTime == undefined )
								 || (value.example8model.length == 0)){
							 
								 growl.warning("All fields are mandatory.",{
										title : WARNING_MSG
									});
								 trueFalseFlag = false;
									return false;
						 }
						 if(i == $scope.otStaffTimeList.length && trueFalseFlag == true ){
					$rootScope.startLoader();
					
					$scope.staffTimeDetails = [];
					angular.forEach($scope.otStaffTimeList, function(v) {
						if(v.fromTime!=""){
							var newArr ={};
							$scope.newDaysList = [];
							angular.forEach(v.example8model,
									function(value,index) {
										$scope.newDaysList .push(value.id);
									});
							
							newArr.fromDate = moment(v.fromDate).format('DD-MM-YYYY'),
							newArr.toDate = moment(v.toDate).format('DD-MM-YYYY'),
							newArr.fromTime = v.fromTime+":00",
							newArr.toTime = v.toTime+":00",
							newArr.switchOverTime = v.switchOverTime,
							newArr.days = $scope.newDaysList,
							$scope.staffTimeDetails.push(newArr);
						}
					});
					$scope.param={
						
						roleId:$scope.roleObj.roleId,
						userList:$scope.userIds,
						timeDetails:$scope.staffTimeDetails,
						status:"A",
						orgId:$scope.organizationId,
						unitId:$scope.unitId,
						createdBy:$scope.userId,
						createdDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
						updatedBy:$scope.userId,
						updatedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
						
					};

					var URI = BASE_URL + ROOT_URL + "/global/otims/saveStaffScheduling";
					//console.log("$scope.param",$scope.param);
					GenericService.serviceAction("POST",URI,$scope.param).then(function(response){
						if (response.data.status == 'success')
						{
							$scope.otStaffTimeList = [];
							var data = {
	            					fromDate : '',
	            	     			toDate : '',
	            	     			fromTime : '',
	            	     			toTime : '',
	            	     			switchOverTime : '',
	            	     			example8model:[]
	            			};
	            			$scope.otStaffTimeList.push(data);
	            			$scope.roleObj.roleId ="";
							$scope.userIds=[];
							$scope.staffList = [];
							$scope.selectedStaffList=[];
							$scope.staffTimeDetails=[];
							$rootScope.stopLoader();
							$scope.getOtStaffSchedulingList($scope.OTUserOffset,$scope.OTUserRecordsPerPage);
							growl.success(response.data.message,
									{
										title : 'Success!'
									});
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
					i++;
				});
				}catch(e){
					//console.log("Exception",e);
				}
			}
			}
			
			
			$scope.getSlotsDoctorShedule = function(docId, specialityId,
					docName, specialityName,slotTypeId , unitId) 
			{
				 $scope.datepicker = new Date();
				$scope.doctorName = docName;
				$scope.specialityName = specialityName;
				$scope.doctorId = docId;
				$scope.unitId = unitId
				$scope.specDoctorId = docId +'_'+ specialityId + '_'+docName+'_'+specialityName;
				$('#specDoctorId').val($scope.specDoctorId);
				$scope.specialityId = specialityId;
				$scope.thirdTabLevel = '1' ;
				$scope.slotTypeId = slotTypeId;
				//$scope.getSlotsOfDate();
				var data = {
					organizationId : $scope.organizationId,
					unitId : unitId,
					doctorId : docId,
					specialityId : specialityId,
					appointmentDate:moment(new Date()).format('YYYY-MM-DD HH:mm:ss')
				}
				if(slotTypeId == '1' && (docId == undefined || docId==null))
				{
					data.slotTypeId = 4;	
				}
				else
				{
					data.slotTypeId = slotTypeId;
				}
				
				
				
				
				//console.log(data);
				$rootScope.startLoader();
				var URI = BASE_URL + ROOT_URL + GETYEARLYSLOTS;
				BillingGenericService
						.serviceAction(METHOD_POST, URI, data,
								NOTIFICATION_MSG_STATUS_FALSE)
						.then(
								function(response) {
									//console.log(response);
									// $rootScope.startLoader();
									

									if (response.data.status == "success") {
										$scope.yearSlotList = [];
										$scope.yearSlotList = response.data.listObject;
										$scope.slotListMonth=[];
										$scope.setThirdTabLevel(1);
										$rootScope.stopLoader();
									}

								});

			}
			
			
			$scope.getSlots = function(docId, specialityId,
					docName, specialityName,slotTypeId) 
			{
				 $scope.datepicker = new Date();
				$scope.doctorName = docName;
				$scope.specialityName = specialityName;
				$scope.doctorId = docId;
				$scope.specDoctorId = docId +'_'+ specialityId + '_'+docName+'_'+specialityName;
				$('#specDoctorId').val($scope.specDoctorId);
				$scope.specialityId = specialityId;
				$scope.thirdTabLevel = '1' ;
				//$scope.getSlotsOfDate();
				var data = {
					organizationId : $scope.organizationId,
					unitId : $scope.unitId,
					doctorId : docId,
					specialityId : specialityId,
					appointmentDate:moment(new Date()).format('YYYY-MM-DD HH:mm:ss')
				}
				if(slotTypeId == '1' && (docId == undefined || docId==null))
				{
					data.slotTypeId = 4;	
				}
				else
				{
					data.slotTypeId = slotTypeId;
				}
				
				
				
				
				//console.log(data);
				$rootScope.startLoader();
				var URI = BASE_URL + ROOT_URL + GETYEARLYSLOTS;
				BillingGenericService
						.serviceAction(METHOD_POST, URI, data,
								NOTIFICATION_MSG_STATUS_FALSE)
						.then(
								function(response) {
									//console.log(response);
									// $rootScope.startLoader();
									

									if (response.data.status == "success") {
										$scope.yearSlotList = [];
										$scope.yearSlotList = response.data.listObject;
										//console.log($scope.yearSlotList);
										$scope.setThirdTabLevel(1);
										/*$state.go('appointmentScheduling.year');*/
										$rootScope.stopLoader();
									}

								});

				/*if ($scope.selectedSlotPeriod == "D") {
					$scope.getSlotsOfDate();
				}
*/
			}
			
			
			$scope.getSlotsForModality = function(modId,subSpecId,specId,modName, subSpecialityName) 
			{
			    $scope.datepicker = new Date();
				$scope.doctorName = modName;
				$scope.specialityName = subSpecialityName;
				$scope.modalityId = modId;
				$scope.subSpecialityId = subSpecId;
				$scope.specialityId = specId;
				$scope.specDoctorId = modId+'_'+subSpecId+'_'+specId+'_'+modName+'_'+subSpecialityName;
				$('#specDoctorId').val($scope.specDoctorId);
				//$scope.getSlotsOfDate();
				if(modId != undefined && modId != null && modId !=''){
				var data = {
					organizationId : $scope.organizationId,
					unitId : $scope.unitId,
					slotTypeId : $scope.slotTypeId,
					modalityId : modId,
					subSpecialityId :$scope.subSpecialityId,
					specialityId :$scope.specialityId,
					appointmentDate:moment(new Date()).format('YYYY-MM-DD HH:mm:ss')
				}
				console.log(data);
				$rootScope.startLoader();
				var URI = BASE_URL + ROOT_URL + GETYEARLYSLOTS;
				BillingGenericService
						.serviceAction(METHOD_POST, URI, data,
								NOTIFICATION_MSG_STATUS_FALSE)
						.then(
								function(response) {
									console.log(response);
									// $rootScope.startLoader();
									

									if (response.data.status == "success") {
										$scope.yearSlotList = [];
										$scope.yearSlotList = response.data.listObject;
										console
												.log($scope.yearSlotList);
										$rootScope.stopLoader();
									}

								});
				}

			}

			$scope.monthDateForRefresh = '';
            $scope.typeFlagForRefresh = 'N';

			$scope.getMonthWiseSlot = function(monthDate,typeFlag) 
			{
			    $scope.monthDateForRefresh = monthDate;
                $scope.typeFlagForRefresh = typeFlag;
				$scope.slotListMonth = [];
				$scope.setThirdTabLevel(2);
				
				if(typeFlag == 'M')
					{
					 var copyright = new Date();
					 var year = copyright.getFullYear();
					 var month = copyright.getMonth()+1;
					
					}
				else
					{
						var arr = monthDate.split('-');
						 var year = arr[1];
						 var month = arr[0];
					}
				
				var data = {
					"organizationId" : $scope.organizationId,
					"unitId" : $scope.unitId,
					"slotTypeId" : $scope.slotTypeId,
					"month" : month,
					"year" : year,

				}
				$scope.selectedSlotId = $scope.slotTypeId;
				if($scope.selectedSlotId == '1')
				{
					data.slotTypeId = 1;
					data.specialityId = $scope.specialityId;
					data.bedId = $scope.bedId;
				}
			 
				if($scope.selectedSlotId == '4')
				{
					data.slotTypeId = 4;
					data.specialityId = $scope.specialityId;
				}
				
				if ($scope.selectedSlotId == 1 ) {
					
					data.doctorId = $scope.doctorId;
					data.specialityId = $scope.specialityId;
					data.bedId = $scope.bedId;
					
				}
		
				else if($scope.selectedSlotId == 8){
					data.slotTypeId = 8;
					data.bedId = $scope.bedId;
					data.specialityId = $scope.hemodialysisSpecialityId;
					$scope.secondTabLevel = 2;
				}
				else if($scope.selectedSlotId == 2)
				{
							data.specialityId = $scope.specialityId;
							data.subSpecialityId = $scope.subSpecialityId;
							data.modalityId = $scope.modalityId;
							data.bedId = $scope.bedId;
				}
				else
					{
					$scope.secondTabLevel = 2;
					}
				
				var URI = BASE_URL + ROOT_URL + GETMONTHLYSLOTS;

				BillingGenericService
						.serviceAction(METHOD_POST, URI, data,
								NOTIFICATION_MSG_STATUS_FALSE)
						.then(
								function(response) {
									//console.log(response);
									$scope.slotDetailsList = [];
									// $rootScope.startLoader();
									if (response.data.status == "success") {
										$scope.slotListMonth = response.data.listObject;
										$scope.weekArray = [];
										for(var i=1;i <= response.data.listObject.length;i++)
											{
												var weekArr ={};
												weekArr.id = i;
												weekArr.name = "w"+i;
												$scope.weekArray.push(weekArr);
											}

										 var now = new Date(year,month,1);
										 $scope.lastDate  = new Date(now.getFullYear(), now.getMonth(), 0);
										 $scope.firstDate = new Date(year,month-1,1);
									}

								});

			}
			
			$scope.weekIdForRefresh = '';
			$scope.getWeekWiseSlots = function(weekId)
			{
			    $scope.weekIdForRefresh = weekId;
				$scope.selectedSlotId = $scope.slotTypeId;
				$scope.slotDetailsList =  [];
				$scope.setThirdTabLevel(3);
				$scope.currentTime = moment().format('HH:mm');
					
				$scope.wkId = weekId;
				
				//console.log("ff",$scope.slotListMonth[weekId-1]);
				//alert($scope.slotListMonth[weekId-1].length);
				//alert($scope.slotListMonth[weekId-1][0]);
				//alert($scope.slotListMonth[weekId-1][$scope.slotListMonth[weekId-1].length-1]);
				var arrFrom =$scope.slotListMonth[weekId-1][0].split('_');
				$scope.fromDateForWeek = arrFrom[1];
				
				var arrTo =$scope.slotListMonth[weekId-1][$scope.slotListMonth[weekId-1].length-1].split('_');
				$scope.toDateForWeek = arrTo[1];
				
				var data = {
						organizationId:$scope.organizationId,
						unitId:$scope.unitId,
						
						slotTypeId:$scope.selectedSlotId,
						fromDate:$scope.fromDateForWeek,
						toDate:$scope.toDateForWeek
				}
				
				if($scope.selectedSlotId == '1')
				{
					data.slotTypeId = $scope.selectedSlotId;
					data.specialityId = $scope.specialityId;
				}
			else
				{
						data.slotTypeId = $scope.selectedSlotId;
					
				}
				
				
				if($scope.selectedSlotId == 1 )
					{
						data.doctorId=$scope.doctorId;
						data.specialityId=$scope.specialityId;
					
					}
				else if($scope.selectedSlotId == 2 )
					{
					
						data.specialityId = $scope.specialityId;
						data.subSpecialityId = $scope.subSpecialityId;
						data.modalityId = $scope.modalityId;
					
					}
				else if($scope.selectedSlotId == 8 ){
					data.slotTypeId = 8;
					data.bedId = $scope.bedId;
					data.specialityId = $scope.hemodialysisSpecialityId;
					$scope.secondTabLevel = 3;
				}
				else
				{
				$scope.secondTabLevel = 3;
				}
			
				if($scope.selectedSlotId == 4 )
				{
					data.doctorId=$scope.doctorId;
					data.specialityId=$scope.specialityId;
					organizationId:$scope.organizationId;
					unitId:$scope.unitId;
					
					slotTypeId:$scope.selectedSlotId;
					fromDate:$scope.fromDateForWeek;
					toDate:$scope.toDateForWeek;
				}
				
				
				//console.log(data);
				//return false;
				
				var URI = BASE_URL+ ROOT_URL+ GETWEEKLYSLOTS;
				BillingGenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_FALSE)
				.then(function(response) {
							//console.log(response);
							$scope.slotDetailsList = [];
							// $rootScope.startLoader();
							if (response.data.status == "success") {
								$scope.slotsListWeek = response.data.listObject;
								//console.log($scope.slotsListWeek);
							}

						});
			
			}
			
			
			$scope.deactiveWeeklySlots = function(weekId)
			{
				$scope.selectedSlotId = $scope.slotTypeId;
				$scope.slotDetailsList =  [];
				//$scope.setThirdTabLevel(3);
				$scope.currentTime = moment().format('HH:mm');
					
				$scope.wkId = weekId;
				
				//console.log("ff",$scope.slotListMonth[weekId-1]);
				//alert($scope.slotListMonth[weekId-1].length);
				//alert($scope.slotListMonth[weekId-1][0]);
				//alert($scope.slotListMonth[weekId-1][$scope.slotListMonth[weekId-1].length-1]);
				var arrFrom =$scope.slotListMonth[weekId-1][0].split('_');
				$scope.fromDateForWeek = arrFrom[1];
				
				var arrTo =$scope.slotListMonth[weekId-1][$scope.slotListMonth[weekId-1].length-1].split('_');
				$scope.toDateForWeek = arrTo[1];
				
				var data = {
						organizationId:$scope.organizationId,
						unitId:$scope.unitId,
						
						slotTypeId:$scope.selectedSlotId,
						fromDate:$scope.fromDateForWeek,
						toDate:$scope.toDateForWeek
				}
				
				if($scope.selectedSlotId == '1')
				{
					data.slotTypeId = $scope.selectedSlotId;
					data.specialityId = $scope.specialityId;
					data.doctorId = $scope.doctorId;
				}
				
				if($scope.selectedSlotId == 4 )
					{
						//data.doctorId=$scope.doctorId;
						data.specialityId=$scope.specialityId;
					
					}
				else if($scope.selectedSlotId == 2 )
					{
					
						data.specialityId = $scope.specialityId;
						data.subSpecialityId = $scope.subSpecialityId;
						data.modalityId = $scope.modalityId;
					
					}
				else if($scope.selectedSlotId == 8 ){
					data.slotTypeId = 8;
					data.bedId = $scope.bedId;
					data.specialityId = $scope.hemodialysisSpecialityId;
					$scope.secondTabLevel = 3;
				}
				else
				{
				$scope.secondTabLevel = 3;
				}
			
				
				/*console.log(data);
				return false;*/
				data.updatedBy = $scope.userId;
				var URI = BASE_URL + ROOT_URL + "/api/opd/deactiveSlotsWithInDateRange";

				BillingGenericService
						.serviceAction(METHOD_POST, URI, data,
								NOTIFICATION_MSG_STATUS_TRUE)
						.then(
								function(response) {
									//console.log(response);
									$scope.slotDetailsList = [];
									// $rootScope.startLoader();
									if (response.data.status == "success") 
									{
										
									}

								});
			
			}
			$scope.deactiveDaySlots = function(obj)
			{
				
				console.log(obj);
				var array = obj.split('_');
				var data = {
						organizationId:$scope.organizationId,
						unitId:$scope.unitId,
						
						slotTypeId:$scope.selectedSlotId,
						fromDate:array[1],
						toDate:array[1]
				}
				
				if($scope.selectedSlotId == '1')
				{
					data.slotTypeId = $scope.selectedSlotId;
					data.specialityId = $scope.specialityId;
					data.doctorId=$scope.doctorId;
				}
				
				
				if($scope.selectedSlotId == 4 )
					{
						//data.doctorId=$scope.doctorId;
						data.specialityId=$scope.specialityId;
					
					}
				else if($scope.selectedSlotId == 2 )
					{
					
						data.specialityId = $scope.specialityId;
						data.subSpecialityId = $scope.subSpecialityId;
						data.modalityId = $scope.modalityId;
					
					}
				else if($scope.selectedSlotId == 8 ){
					data.slotTypeId = 8;
					data.bedId = $scope.bedId;
					data.specialityId = $scope.hemodialysisSpecialityId;
					$scope.secondTabLevel = 3;
				}
				else
				{
					$scope.secondTabLevel = 3;
				}
			
				
				/*console.log(data);
				return false;*/
				data.updatedBy = $scope.userId;
				var URI = BASE_URL + ROOT_URL + "/api/opd/deactiveSlotsWithInDateRange";

				BillingGenericService
						.serviceAction(METHOD_POST, URI, data,
								NOTIFICATION_MSG_STATUS_TRUE)
						.then(
								function(response) {
									//console.log(response);
									$scope.slotDetailsList = [];
									// $rootScope.startLoader();
									if (response.data.status == "success") 
									{

									    $scope.getMonthWiseSlot ($scope.monthDateForRefresh, $scope.typeFlagForRefresh)
										$scope.initDoctorSlotList(
                                        $scope.organizationId, $scope.offset,
                                        $scope.noOfRecordsPerPage);
									}

								});
			
			}
			
			$scope.checkDateIsBefore = function(slobObj)
			{
			     var endTime = moment(slobObj.originalFromTime, 'hh:mm');
			     var startTempTime = moment().format("HH:mm");
			     var startTime = moment(startTempTime, 'hh:mm');
			     
				 if(moment(slobObj.appointmentDate2).isBefore(moment().format('YYYY-MM-DD'))		 
					&&
						 (slobObj.isBlockedUnblocked == '2' && slobObj.slotPatientDetailsListsSize >= '0'))
				 {
					 return 'bg-grey cursor-pointer day-slot-div';
				 }
				 
				  if(endTime.isBefore(startTime) && moment(slobObj.appointmentDate2).isSame(moment().format('YYYY-MM-DD'))
						  &&(slobObj.isBlockedUnblocked == '2' && slobObj.slotPatientDetailsListsSize >= '0'))
					 {
					   return 'bg-grey cursor-pointer day-slot-div';
					 }
				 else{
				
					
					if(slobObj.isBlockedUnblocked == '2' && slobObj.slotPatientDetailsListsSize == '0')
					{
						return 'bg-green cursor-pointer day-slot-div';
					}else if(slobObj.isBlockedUnblocked == '2' && slobObj.slotPatientDetailsListsSize > '0' && slobObj.isVip=='Y')
					{
						//return 'bg-orange cursor-pointer day-slot-div';
						return 'bg-red1 cursor-pointer day-slot-div';
					}else if(slobObj.isBlockedUnblocked == '2' && slobObj.allowedOverBookingSlots == '0' && slobObj.slotPatientDetailsListsSize > '0')
					{
						//return 'bg-orange cursor-pointer day-slot-div';
						return 'bg-red1 cursor-pointer day-slot-div';
					}else if(slobObj.isBlockedUnblocked == '2' && slobObj.slotPatientDetailsListsSize > '0' && slobObj.slotPatientDetailsListsSize < slobObj.allowedOverBookingSlots)
					{
						//return 'bg-red1 cursor-pointer day-slot-div';
						return 'bg-orange cursor-pointer day-slot-div';
					}else if(slobObj.isBlockedUnblocked == '2' && slobObj.allowedOverBookingSlots > '0' && slobObj.slotPatientDetailsListsSize == slobObj.allowedOverBookingSlots)
					{
						//return 'bg-orange cursor-pointer day-slot-div';
						return 'bg-red1 cursor-pointer day-slot-div';
					}else if(slobObj.isBlockedUnblocked == '1' && slobObj.slotPatientDetailsListsSize == '1')
					{
						return 'bg-grey cursor-pointer day-slot-div';
					}else if(slobObj.isBlockedUnblocked == '1' && slobObj.slotPatientDetailsListsSize == '0')
					{
						return 'bg-grey cursor-pointer day-slot-div';
					}
				}
				
			}
			
			$scope.getBookSlotDetails = function(item) 
			{
				$scope.openScheduling = false;
				
				$scope.patientName = item.patientName;
				$scope.mobileNumber = item.mobileNumber;
				$scope.email = item.email;
				$scope.address = item.address;
				
				$scope.bookedPatientList = item.slotPatientDetailsList;
				//console.log($scope.bookedPatientList);
			}
			
			
			$scope.changeClass = function(colorFlag,weekId,co,appoitDate)
			{
				//console.log(colorFlag,weekId,co);
				 var pickUpDate = moment(new Date()).format('YYYY-MM-DD');
				if(colorFlag == 'Y' && pickUpDate <= appoitDate )
					{
						$('#color'+co+weekId).addClass("bg-green");
					}else
					{
						$('#color'+co+weekId).addClass("bg-grey");
					}
				
				
				 
			}
			
			
			// Update Ot staff Scheduling Status
			$scope.updateOTStaffStatus = function(staffSchedulingId ,status){
				try{
					var date = new Date();
					$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
					var data={
							status:status,
							staffSchedulingId:staffSchedulingId 
					};
					$rootScope.startLoader();
					var URI = BASE_URL + ROOT_URL + "/global/otims/deactivateStaffSchedule";
					GenericService.serviceAction("POST",URI,data).then(function(response){
						if (response.data.status == 'success')
						{
							$scope.getOtStaffSchedulingList($scope.OTUserOffset,$scope.OTUserRecordsPerPage);
							$rootScope.stopLoader();
							
							growl.success(response.data.message,
									{
										title : 'Success!'
									});
						} else if(response.data.status == 'error')
						{
							$rootScope.stopLoader();
							growl.error(response.data.message,
									{
										title : 'Error!'
									});
						}
					});
				}catch(e){
				}
			}
			
			$scope.setNoOfRecordsOfOTStaffScheduling = function() {
				$scope.getOtStaffSchedulingList($scope.OTUserOffset,$scope.OTUserRecordsPerPage);
			};
			
			$scope.getMasterOtStaffSchedulingList = function(offset,noOfRecordsPerPage){
				try{
					$rootScope.startLoader();
					$scope.otStaffSchedulingList = [];
					var data = {
							unitId:$scope.unitId,
							orgId:$scope.organizationId,
							offset : offset,
							noOfRecordsPerPage : noOfRecordsPerPage
							};								
					var URI = BASE_URL + ROOT_URL+ "/global/otims/getStaffScheduling";
					BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE)
					.then(function(response) {
								if (response.data.status == "success"){
									$rootScope.stopLoader();
										$scope.otStaffSchedulingList = response.data.listObject;
								}
							});
					
				}catch(e){
					$rootScope.stopLoader();
				}
			}
			
			$scope.OTUserPager = {};
			//$scope.otUserPage;

			$scope.setPageForOTStaff = function(page, flag) {
				if (page < 1 || page > $scope.OTUserPager.totalPages) {
					return;
				}
				$scope.OTUserPager = PagerService.GetPager(
						$scope.OTUserListCount, page,
						$scope.OTUserRecordsPerPage);
				if (flag) {
					$scope.getMasterOtStaffSchedulingList($scope.OTUserPager.startIndex,$scope.OTUserPager.pageSize);
				}
			}
			
/***********************************************  STAFF Scheduling END  *************************************************************************/
						
						
} ]);