'use strict';
angular
		.module('myApp')
		.controller(
				"userAllocationForHomecollectionController",
				[
						'$scope',
						'$rootScope',
						'$state',
						'$filter',
						'$cookies',
						'GenericService',
						'PagerService',
						'promiseFactoryWithObject',
						'$sessionStorage',
						'growl',
						'$interval',
						'$window',
						'$timeout',
						function($scope, $rootScope, $state, $filter, $cookies,
								GenericService, PagerService, promiseFactoryWithObject,
								$sessionStorage, growl,$interval,$window,$timeout) {

							$rootScope.loginpage = true;
							$scope.LISDynamicLabel = "Sample";

							/** Variables for pagination */
							$scope.offset = 0;
							$scope.noOfRecordsPerPage = 50;
							$scope.phlebotomyWorkList = [];
							$scope.phlebotomyWorkListDetails = [];
							$scope.totalNoOfRecords;
							$scope.searchKeyword = '';
							$scope.commonListCount;
							$scope.getUnitByOrganization();
							/*** Check user is Loggedin */
							var cookieObject,cookieObjectAppConfig;
							[cookieObject,cookieObjectAppConfig] = $scope.checkCookies();
							$scope.orgUnitId = cookieObject.unitId;
							$scope.orgId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							$scope.isPreOrPostBilling   =cookieObjectAppConfig.isPreOrPostBilling;
							
							/*** Lab Configuration */
							var labConfig  =    $scope.checkLabConfig();
							$scope.deptId = labConfig.labSpecialityId;
							$scope.isPinApplicable = labConfig.isPinApplicable;
							$scope.isPrebarcodeGenerantion = labConfig.isPrebarcodeGenerantion;
							
							$scope.visitTypeId=1;
							$scope.visityTypeList = [];
							
							$scope.selectedVisitType = [];
							

							/* code for setting Label */
							$(".selectedPageName").text("Phlebotomy Work List");
							
							/**DatePicker Code  */
							
							$scope.toDate = new Date();
							$scope.fromDate = new Date();
						    $scope.userAllocationHomeCollectionList = [];
						
							$scope.open = function() {
								$scope.popup.opened = true;
							};

							$scope.popup =
							{
								opened : false
							};
							
							$scope.popup1 =
							{
								opened : false
							};
							
							$scope.open1 = function() 
							{
								$scope.popup1.opened = true;
							};

							
							$scope.collectionDatepickerOptions =
							{
								formatYear : 'yyyy',
								showWeeks : false,
								minDate : new Date(),
							};

							$scope.openCollectionDatePicker = function(index) {

                                for (var i = $scope.userAllocationHomeCollectionList.length - 1; i >= 0; i--) {
                                    if (i === index) {

                                        $scope.isOpenCollectionDatePicker.opened[i] = true;
                                    } else {
                                        $scope.isOpenCollectionDatePicker.opened[i] = false;
                                    }
                                }
                            };

                            $scope.isOpenCollectionDatePicker = {
                                opened : [ false ],

                            };
                            
                            $scope.initClockPicker = function() {

                                $('.clockpicker').clockpicker();
                            }
							
							
							    /** Service Autocomplete List * */
								$scope.getPhlebotomyPatientList = function(searchKeyword)
								{
									if(searchKeyword.length == 3)
									{
									try 
									{
										var data = {
												"orgId":$scope.orgId,
												"orgUnitId":$scope.orgUnitId,
												"visitTypeId":$scope.visitTypeId,
												"searchKeyword":searchKeyword,
										}
										
										var URI = BASE_URL 
										        + ROOT_URL
												+ LIS_TRANSACTION
												+ SEARCH_PHLEBOTOMY_WORK_LIST_PATIENT
										console.log("URI", URI);
										return GenericService.serviceAction("POST", URI,
												data).then(function(response) {
											console.log("response.data.listObject",response.data.listObject);
											$scope.patientListNew =response.data.listObject;
											return response.data.listObject;
										});
									} catch (e) {
										console.log(e.message)
									}
									} else {
								        var filteredList = $filter('filter')($scope.patientListNew, { $: searchKeyword });
								        if (filteredList.length === 0) {
								            $scope.filteredPatientList = null;
								        } else {
								            $scope.filteredPatientList = filteredList;
								        }
								        return $scope.filteredPatientList;
								    }
								};
								
							    
							    $scope.selectPatient = function($item, $model,$label)
								{
							      $scope.selectedPatient = $item.id;
							      $scope.uhid=$item.uhid;
						        }
							    
							    $scope.multiselectObj = {
							    		"id":"",
							    		"label":""
							    }
							    
								/*get Visi tType List*/
								$scope.getVisitTypeList = function()
								{
									try 
									{
										var data = {
												"orgId":$scope.orgId,
												"orgUnitId":$scope.orgUnitId,
												"deptId":$scope.deptId,
												"subDeptId":$scope.subDeptId,
												"searchKeyword":"",
										}
										
										var URI = BASE_URL 
										        + ROOT_URL
												+ LIS_HISTOPATHOLOGY
												+ HISTO_MICRO_VISIT_LIST
										console.log("URI", URI);
										 GenericService.serviceAction("POST", URI,
												data).then(function(response) {
											console.log("response.data.listObject",response.data.listObject);
											angular.forEach(response.data.listObject, function(value, key) {
												var multiselectObj = angular.copy($scope.multiselectObj);
												multiselectObj.id = value.id;
												multiselectObj.label = 	value.label;
													$scope.visityTypeList.push(multiselectObj);
												});
											
											
										});
									} catch (e) {
										console.log(e.message)
									}
								}
								
								$scope.getVisitTypeList();
								
								
								$scope.checkPatient = function()
								{
									if($scope.patientId ==undefined)
									{
										$scope.selectedPatient = 0;
									}
								}
								
								$scope.clearFields = function()
								{
									$scope.patientId = undefined;
									$scope.selectedPatient = null;
									$scope.selectedVisitType = [];
									$scope.orderStatusId = '';
									$scope.uhid = undefined;
									$scope.toDate = new Date();
									$scope.fromDate = new Date();
									$scope.getUserAllocationHomeCollectionList($scope.orgId,$scope.orgUnitId, $scope.offset,$scope.noOfRecordsPerPage);
								};
							
			                  //get user List
								$scope.userList = [];
								$scope.getUserList = function() {
									var data = {
										orgId : $scope.orgId,
										orgUnitId : $scope.orgUnitId
									};
									 
									 var URI = BASE_URL + ROOT_URL+ "/LIS/transaction/getPhlebotomistList";//"/LIS/transaction/userAllocationHomeCollection/Search";
								GenericService.serviceAction("POST", URI, data)
										.then(
												function(response) {
													if (response.data.status == "success") {
														$scope.userList = [];
														$scope.userList = response.data.listObject;
													}
												});
								}
								
								$scope.getUserList();
								$scope.userAllocationHomeCollectionList = [];
								$scope.getUserAllocationHomeCollectionList = function(orgId,orgUnitId, offset, noOfRecordsPerPage) {
									if(orgId == undefined){
										orgId = $scope.orgId;
									}
									
									if(orgUnitId == undefined){
										orgUnitId = $scope.orgUnitId;
									}
									if(offset == undefined){
										offset = $scope.offset;
									}
									if(noOfRecordsPerPage == undefined){
										noOfRecordsPerPage = $scope.noOfRecordsPerPage;
									}
									var data = {
											 "orgId" : orgId,
											  "orgUnitId" :orgUnitId,
											  "fromDt": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
											  "toDt":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
											  "patientId" :$scope.selectedPatient,
											  "visitTypes" :$scope.selectedVisitType,
											  "deptId": $scope.deptId,
											  "orderStatusId": $scope.orderStatusId,
											  "isPreOrPostBilling": $scope.isPreOrPostBilling,
											  "uhidNumber" : ($scope.uhid==undefined ||$scope.uhid== null) ?"":$scope.uhid,
											  "offset": offset,
											  "recordPerPage" : noOfRecordsPerPage
									};
									 
									 var URI = BASE_URL + ROOT_URL+ "/LIS/transaction/userAllocationHomeCollection/Search";
								GenericService.serviceAction("POST", URI, data)
										.then(
												function(response) {
													if (response.data.status == "success") {
														$scope.userAllocationHomeCollectionList = [];
														$scope.userAllocationHomeCollectionList = response.data.listObject;
														if($scope.userList!=null && $scope.userList.length>0){
															if($scope.userAllocationHomeCollectionList!=null && $scope.userAllocationHomeCollectionList.length>0){
																angular.forEach($scope.userAllocationHomeCollectionList,function(value,index){
																	value.userId = value.userId;
																	value.collectionDate = (value.phlebotomistCollectionDate!=undefined && value.phlebotomistCollectionDate!=null ) ?  moment(value.phlebotomistCollectionDate,'DD-MM-YYYY HH:mm:ss').toDate():moment().startOf('day').toDate();
																    value.fromTime = (value.fromTime!=undefined && value.fromTime!=null ) ? value.fromTime :"";
																    value.toTime = (value.toTime!=undefined && value.toTime!=null ) ? value.toTime :"";
																    value.statusId = value.statusId;
																});
															}
														}
														$scope.commonListCount = response.data.object;
														$scope.setPage(1,false);
													}
												});
								}
								
								$scope.setNoOfRecords = function() {
									$scope.getUserAllocationHomeCollectionList($scope.orgId,$scope.orgUnitId, $scope.offset,$scope.noOfRecordsPerPage);
								};
								
								
								
								$scope.getHomeCollectionList = function(orgId,orgUnitId, offset, noOfRecordsPerPage) {
									var data = {
											 "orgId" : orgId,
											  "orgUnitId" :orgUnitId,
											  "fromDt": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
											  "toDt":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
											  "patientId" :$scope.selectedPatient,
											  "visitTypes" :$scope.selectedVisitType,
											  "deptId": $scope.deptId,
											  "orderStatusId": $scope.orderStatusId,
											  "isPreOrPostBilling": $scope.isPreOrPostBilling,
											  "uhidNumber" : ($scope.uhid==undefined ||$scope.uhid== null) ?"":$scope.uhid,
											  "offset": offset,
											  "recordPerPage" : noOfRecordsPerPage
									};
									 
									 var URI = BASE_URL + ROOT_URL+ "/LIS/transaction/userAllocationHomeCollection/Search";
								GenericService.serviceAction("POST", URI, data)
										.then(
												function(response) {
													if (response.data.status == "success") {
														$scope.userAllocationHomeCollectionList = [];
														$scope.userAllocationHomeCollectionList = response.data.listObject;
														if($scope.userAllocationHomeCollectionList!=null && $scope.userAllocationHomeCollectionList.length>0){
															angular.forEach($scope.userAllocationHomeCollectionList,function(value,index){
																value.userId = value.userId;
																value.collectionDate = (value.phlebotomistCollectionDate!=undefined && value.phlebotomistCollectionDate!=null ) ?  moment(value.phlebotomistCollectionDate,'DD-MM-YYYY HH:mm:ss').toDate():moment().startOf('day').toDate();
																value.fromTime = (value.fromTime!=undefined && value.fromTime!=null ) ? value.fromTime :"";
															    value.toTime = (value.toTime!=undefined && value.toTime!=null ) ? value.toTime :"";
															    value.statusId = value.statusId;
															});
														}
													}
												});
								}
								
								
								$scope.pager = {};
								$scope.page;

								$scope.setPage = function(page, flag) {
									if (page < 1 ) {
										return;
									}
									$scope.pager = PagerService.GetPager($scope.commonListCount, page,$scope.noOfRecordsPerPage);
									if (flag) {
										$scope.getHomeCollectionList($scope.orgId,$scope.orgUnitId,	$scope.pager.startIndex,$scope.pager.pageSize);
									}
								}
							    
								$scope.getUserAllocationHomeCollectionList($scope.orgId,$scope.orgUnitId, $scope.offset,$scope.noOfRecordsPerPage);
								$scope.isExists = false;
								$scope.checkExistingTime = function(obj){
									
									var fromTm = obj.fromTime.split(":");
									var toTm = obj.toTime.split(":");
									if(fromTm.length == 2){
									obj.fromTime = obj.fromTime +":00";
									}
									if(toTm.length == 2){
									obj.toTime = obj.toTime +":00";
									}
									var data = {
										userId :obj.userId,
										phlebotomistCollectionDate : moment(obj.collectionDate).format('YYYY-MM-DD'),
										fromTime : obj.fromTime,
										toTime : obj.toTime,
										organizationId : $scope.orgId,
										unitId : $scope.orgUnitId,
										phlebotomistHomeCollectionId : (obj.phlebotomistHomeCollectionId!=undefined && obj.phlebotomistHomeCollectionId!=null) ? obj.phlebotomistHomeCollectionId : null 
									};
									var URI = BASE_URL + ROOT_URL+ "/LIS/PhlebotomistHomeCollection/validateUserAlocation";
									GenericService.serviceAction("POST", URI, data)
									.then(function(response) {
									if (response.data.status == "success") {
										$scope.isExists =  response.data.object;
									}
									});
									}
								
								$scope.saveBtn = false;
								$scope.selectRecord = function(obj,index){
									if(obj.isSelected){
										$rootScope.startLoader();
										if(obj.userId==undefined || obj.userId==null || obj.userId==""){
											growl.warning("Please Select User",
													{
														title : 'warning!'
													});
											$rootScope.stopLoader();
											obj.isSelected = false;
											return false;
										}
										
										if(obj.collectionDate==undefined || obj.collectionDate==null || obj.collectionDate==""){
											growl.warning("Please Select collection Date",
													{
														title : 'warning!'
													});
											$rootScope.stopLoader();
											obj.isSelected = false;
											return false;
										}
										
										if(obj.fromTime==undefined || obj.fromTime==null || obj.fromTime==""){
											growl.warning("Please Select From Time",
													{
														title : 'warning!'
													});
											$rootScope.stopLoader();
											obj.isSelected = false;
											return false;
										}
										
										if(obj.toTime==undefined || obj.toTime==null || obj.toTime==""){
											growl.warning("Please Select To Time",
													{
														title : 'warning!'
													});
											$rootScope.stopLoader();
											obj.isSelected = false;
											return false;
										}
										
										if(moment(new Date()).format('yyyy-mm-dd') == moment(obj.collectionDate).format('yyyy-mm-dd'))
										{
											var fromTm =  obj.fromTime.split(":");
											if(fromTm.length ==2){
												 obj.fromTime = obj.fromTime +":00";
											}

											if(moment(new Date()).format("HH:mm:ss") > obj.fromTime){
												growl.warning("Please Select Valid From Time ",
												{
													title : 'warning!'
												});
												$rootScope.stopLoader();
												obj.isSelected = false;
												return false;
												}
										}
										
										
										if(obj.toTime < obj.fromTime){
											growl.warning("Please Select Valid From and To Time ",
													{
														title : 'warning!'
													});
											$rootScope.stopLoader();
											obj.isSelected = false;
											return false;
										}
										
										

										var isValidTime = true;
										angular.forEach($scope.userAllocationHomeCollectionList ,function(value,idx){
											if(index !=idx && $scope.userAllocationHomeCollectionList[index].collectionDate!=undefined && $scope.userAllocationHomeCollectionList[index].collectionDate!=null){
												if(moment($scope.userAllocationHomeCollectionList[index].collectionDate).format('yyyy-mm-dd') == moment(value.collectionDate).format('yyyy-mm-dd')  && $scope.userAllocationHomeCollectionList[index].userId == value.userId){
													var fromTm = $scope.userAllocationHomeCollectionList[index].fromTime.split(":");
													var toTm = $scope.userAllocationHomeCollectionList[index].toTime.split(":");
													if(fromTm.length ==2){
														$scope.userAllocationHomeCollectionList[index].fromTime = $scope.userAllocationHomeCollectionList[index].fromTime +":00";
													}

													if(toTm.length ==2){
														$scope.userAllocationHomeCollectionList[index].toTime = $scope.userAllocationHomeCollectionList[index].toTime +":00";
													}

													if($scope.userAllocationHomeCollectionList[index].fromTime!=undefined && $scope.userAllocationHomeCollectionList[index].fromTime!=null &&
															($scope.userAllocationHomeCollectionList[index].fromTime >= value.fromTime && $scope.userAllocationHomeCollectionList[index].fromTime <= value.toTime)){
														isValidTime = false;
													} 
													if($scope.userAllocationHomeCollectionList[index].toTime!=undefined && $scope.userAllocationHomeCollectionList[index].toTime!=null &&
															$scope.userAllocationHomeCollectionList[index].toTime >= value.fromTime && $scope.userAllocationHomeCollectionList[index].toTime <= value.toTime){
														isValidTime = false;
													}
												}

											}
										});

										if(!isValidTime){
											growl.warning("Please Select Valid From and To Time ",
													{
												title : 'warning!'
													});
											obj.isSelected = false;
											var found = $scope.userAllocationHomeCollectionList.find(obj => {return obj.isSelected});
											if(found!=undefined){
												$scope.saveBtn = true;
												$rootScope.stopLoader();
											}else{
												$scope.saveBtn = false;
											}
											$rootScope.stopLoader();
											return false;
										}
										
										$scope.checkExistingTime($scope.userAllocationHomeCollectionList[index]);
										$timeout(function () {
											if($scope.isExists){
												obj.isSelected = false;
												growl.warning(" Slot Already Used ",
														{
														title : 'warning!'
														});
												
												var found = $scope.userAllocationHomeCollectionList.find(obj => {return obj.isSelected});
												if(found!=undefined){
													$scope.saveBtn = true;
													$rootScope.stopLoader();
												}else{
													$scope.saveBtn = false;
												}
												
												$rootScope.stopLoader();
												return false;
											}else{
												obj.isSelected = true;
												$rootScope.stopLoader();
											}
										
										}, 1000);

										var found = $scope.userAllocationHomeCollectionList.find(obj => {return obj.isSelected});
										if(found!=undefined){
											$scope.saveBtn = true;
											$rootScope.stopLoader();
										}else{
											$scope.saveBtn = false;
										}
									}else{
										var found = $scope.userAllocationHomeCollectionList.find(obj => {return obj.isSelected});
										if(found!=undefined){
											$scope.saveBtn = true;
											$rootScope.stopLoader();
										}else{
											$scope.saveBtn = false;
										}
									}
									$rootScope.stopLoader();
								}
								$scope.saveCollection = function(){
									$rootScope.startLoader();
									$scope.finalList = [];
									var user = false;
									var collectionDate = false;
									var fromTime = false;
									var toTime = false;
									
									
									if($scope.userAllocationHomeCollectionList!=null && $scope.userAllocationHomeCollectionList.length>0){
										
										angular.forEach($scope.userAllocationHomeCollectionList,function(value,index){
											if(value.isSelected){
										
												if(value.userId==undefined || value.userId==null || value.userId==""){
													 user = true; 
													return false;
												}
												
												if(value.collectionDate==undefined || value.collectionDate==null || value.collectionDate==""){
													collectionDate = true;
													return false;
												}
												
												if(value.fromTime==undefined || value.fromTime==null || value.fromTime==""){
													fromTime = true;
													return false;
												}
												
												if(value.toTime==undefined || value.toTime==null || value.toTime==""){
													toTime = true;
													return false;
												}
												
											}
										});
										
									}
									
									
									if(user){
										growl.warning("Please Select User",
												{
													title : 'warning!'
												});
										$rootScope.stopLoader();
										return false;
									}
									
									if(collectionDate){
										growl.warning("Please Select collection Date",
												{
													title : 'warning!'
												});
										$rootScope.stopLoader();
										return false;
									}
									
									if(fromTime){
										growl.warning("Please Select From Time",
												{
													title : 'warning!'
												});
										$rootScope.stopLoader();
										return false;
									}
									
									if(toTime){
										growl.warning("Please Select To Time",
												{
													title : 'warning!'
												});
										$rootScope.stopLoader();
										return false;
									}
									
									if($scope.userAllocationHomeCollectionList!=null && $scope.userAllocationHomeCollectionList.length>0){
										angular.forEach($scope.userAllocationHomeCollectionList,function(value,index){
											if(value.isSelected){
												var data = {
														phlebotomistHomeCollectionId : (value.phlebotomistHomeCollectionId!=undefined && value.phlebotomistHomeCollectionId!=null )? value.phlebotomistHomeCollectionId : null,
														userId : value.userId,
														orderId  : value.orderId,
														phlebotomistCollectionDate : moment(value.collectionDate).format('DD-MM-YYYY HH:mm:ss'),
														fromTime : value.fromTime  ,
														toTime  : value.toTime,
														createdDate  : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
														createdBy  : $scope.userId,
														status  : 'A',
														updatedBy  : $scope.userId,
														updatedDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
														unitId  : $scope.orgUnitId,
														organizationId : $scope.orgId,
														statusId : 42 // sample Allocated To User
												};
												$scope.finalList.push(data);
											}
										});
									}
									
									console.log("$scope.finalList",$scope.finalList);
									
									 var URI = BASE_URL + ROOT_URL+ "/LIS/PhlebotomistHomeCollection/save";
										GenericService.serviceAction("POST", URI, $scope.finalList)
												.then(
														function(response) {
															if (response.data.status == "success") {
																growl.success(response.data.message,
																		{
																			title : 'success!'
																		});
																$rootScope.stopLoader();
																$scope.saveBtn = false;
																$scope.clearFields();
																$scope.getUserAllocationHomeCollectionList($scope.orgId,$scope.orgUnitId, $scope.offset,$scope.noOfRecordsPerPage);
															}
														});
									
								}
						} ]);