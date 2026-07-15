'use strict';

/**
 * @Author By Vivek Satle
 * @name myApp.controller:transferToOTController
 * @description #transferToOTController Controller of the myApp
 */

angular
		.module('myApp')
		.controller(
				'transferToOTController',
				[
						'$scope',
						'$http',
						'$localStorage',
						'$sessionStorage',
						'$cookies',
						'$rootScope',
						'GenericService',
						'growl',
						'$state',
						'$stateParams',
						'PagerService',
						'BillingGenericService',
						'$window',
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService, growl,
								$state,$stateParams,PagerService,BillingGenericService,$window) {

							/* init() function for form object create. */
							$scope.init = function() {
								
								$rootScope.patientFullName = '';
								$rootScope.uhidNum = '';
								$rootScope.bedNumber = '';
								$rootScope.doctorFullName = '';
								$rootScope.wardName ='';
								
								var cookieObject = $cookies.getObject('cookieObject');
								if(cookieObject == undefined){
									$state.go('login');
									return;
								}
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;
								
								/**Variables for pagination */
								$scope.offset = 0;
								$scope.noOfRecordsPerPage = 10;
								$scope.totalNoOfRecords;
								$scope.commonListCountInitiate;
								$scope.commonListCountTransfer;
								
								if(!angular.equals({},$stateParams.bedManagmentObj))
								{
									$scope.bedManagmentCookiesObj=$stateParams.bedManagmentObj;
								}
								if ($scope.bedManagmentCookiesObj != null) 
								{
									$rootScope.patientFullName = $scope.bedManagmentCookiesObj.patientFullName;
									$rootScope.uhidNum = $scope.bedManagmentCookiesObj.uhidNum;
									$rootScope.bedNumber = $scope.bedManagmentCookiesObj.bedNumber;
									$rootScope.doctorFullName = $scope.bedManagmentCookiesObj.doctorFullName;
									$rootScope.wardName = $scope.bedManagmentCookiesObj.wardName;
									
									var dataCookies = {
										admissionId : $scope.bedManagmentCookiesObj.admissionId,
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
									};

								} else {
									$scope.bedManagmentCookiesObj = null;
								}

								$rootScope.loginpage = true;

								$scope.patientSurgeryOrder = {}
								$scope.itemSelecteds = {};

								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup

								var data = {};
								var commonObj = {
									unitId : $scope.unitId,
									organizationId : $scope.organizationId,
									visitTypeId : 4
								};

							}

							$scope.init();

							var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
					        $scope.nurseRole = cookieObjectAppConfig.nurseRole;	
							$scope.nursingStationCookieShow = JSON.parse(sessionStorage.getItem('nursingStationDesc'));

							$scope.wardList = [];
							var nursingStationsWardsList = JSON.parse(sessionStorage.getItem('nursingStationsWardsList'));
							 if(nursingStationsWardsList == undefined){
								 growl.warning(
											'No ward mapped with selected nursing station !!',
											{
												title : 'Success!'
											});
							 }
							 else
							 {
							   console.log('nursingStationsWardsList',nursingStationsWardsList);
							   $scope.wardList = nursingStationsWardsList; 
							 }
							 
							 var specialtyNursingStationCookies = JSON.parse(sessionStorage.getItem('specialtyNursingStationCookiesNur'));
								var nursingStationCookie = JSON.parse(sessionStorage.getItem('nursingStationDesc'));
								$scope.nursingStationCookieShow = JSON.parse(sessionStorage.getItem('nursingStationDesc'));
								 if(specialtyNursingStationCookies == undefined){
									 growl.warning(
												'No nursing station and specialty mapped with current user!!',
												{
													title : 'Success!'
												});
								 }
								 else
								  {
								   console.log('specialtyNursingStationCookies',specialtyNursingStationCookies);
								   console.log('nursingStationCookie',nursingStationCookie);
								   $scope.nursingStationDesc=nursingStationCookie;
								   $scope.departmentNursingStationMapperDtoslist = specialtyNursingStationCookies.departmentNursingStationlist;
									console.log("ggg",$scope.departmentNursingStationMapperDtoslist);
								  }

							$scope.isWardChanged = function()
							{      
								//start for nursing
								var URI = BASE_URL+ROOT_URL+"/api/global/getDetailedNursingStationList/"+$scope.userId+"/"+$scope.organizationId+"/"+$scope.unitId;
								GenericService.serviceAction("GET",URI,{}).then(function(response){
									if (response.data.status == "success") {
										$scope.departmentNursingStationMapperDtoslist = [];			  
										$scope.departmentNursingStationMapperDtoslist = response.data.listObject;
										//alert($scope.roleName);
										if($scope.nurseRole == 1){
											if($scope.departmentNursingStationMapperDtoslist.length > 0)
											{
												if($scope.departmentNursingStationMapperDtoslist.length > 1){
													angular.element('#nursingStationModal').modal('show');
													//$('#nursingStationModal').modal({backdrop: 'static', keyboard: false});
												}
												else
												{
													var URI = BASE_URL+ROOT_URL+"/api/global/getDeptListByNursingStationId/"+$scope.departmentNursingStationMapperDtoslist[0].nursingStationId+"/"+$scope.organizationId+"/"+$scope.unitId;
													GenericService.serviceAction("GET",URI,{}).then(function(response){
														if (response.data.status == "success") {
															$scope.departmentNursingStationListC = {};
															// console.log("fff",response.data.object);
															$scope.departmentNursingStationListC.departmentNursingStationlist = response.data.listObject;					  
															sessionStorage.setItem('specialtyNursingStationCookies', JSON.stringify($scope.departmentNursingStationListC));
															sessionStorage.setItem('specialtyNursingStationCookiesNur', JSON.stringify($scope.departmentNursingStationListC));
															sessionStorage.setItem('nursingStationsWardsList', JSON.stringify(response.data.object));
															  sessionStorage.setItem('nursingStationId', JSON.stringify($scope.departmentNursingStationMapperDtoslist[0].nursingStationId));
															//$cookies.putObject('nursingStationDesc',$scope.departmentNursingStationMapperDtoslist.nursingStationDesc);
															sessionStorage.setItem('nursingStationDesc', JSON.stringify($scope.departmentNursingStationMapperDtoslist[0].nursingStationDesc));
															$scope.wardListForGetBedList = response.data.object;
														}
													});
												}
											}
										}
									}
								});        
							}

							$scope.departmentNursing = {};
							$scope.selectsetNursingStation = function(departmentNursing)
							{
								if(departmentNursing == undefined || departmentNursing.length == 0){
									growl.warning('Please select nursing station..!!');
									return false;
								}else{
								$scope.wardListForGetBedList=[];
								var data = JSON.parse(departmentNursing);
								//console.log("$scope.departmentNursing",$scope.departmentNursing);
								//console.log("$scope.departmentNursing ID",data.nursingStationId);
								var URI = BASE_URL+ROOT_URL+"/api/global/getDeptListByNursingStationId/"+data.nursingStationId+"/"+$scope.organizationId+"/"+$scope.unitId;
									GenericService.serviceAction("GET",URI,{}).then(function(response){
										if (response.data.status == "success") {
											$scope.departmentNursingStationListC = {};
											//console.log('nursingStationsWardsList',response.data.object);
											$scope.departmentNursingStationListC.departmentNursingStationlist = response.data.listObject;
											sessionStorage.setItem('specialtyNursingStationCookies', JSON.stringify($scope.departmentNursingStationListC));
											sessionStorage.setItem('specialtyNursingStationCookiesNur', JSON.stringify($scope.departmentNursingStationListC));
											sessionStorage.setItem('nursingStationsWardsList', JSON.stringify(response.data.object));
											sessionStorage.setItem('nursingStationDesc', JSON.stringify(data.nursingStationDesc));
											sessionStorage.setItem('nursingStationId', JSON.stringify(data.nursingStationId));
											$scope.wardListForGetBedList = response.data.object;
											$scope.nursingStationCookieShow = JSON.parse(sessionStorage.getItem('nursingStationDesc'));
											$scope.initMasterListInitiate($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
											angular.element('#nursingStationModal').modal('hide');
										}
									});
								}
							}
							
							
							/************************************* Transfer to OT START **********************************/
							
							// function for initializa clockpicker
							$scope.initClockPicker = function() {

								$('.clockpicker').clockpicker();
							}
							/**List And Pagination Start for initiate**/
							$scope.setNoOfRecordsInitiate = function() {
								$scope.initMasterListInitiate($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
							};
							
							$scope.initMasterListInitiate = function(organizationId,offset, noOfRecordsPerPage,unitId)
							{
							$scope.patientSurgeryTransferList =[];
								$scope.surgeriesOfTheDayList = []; 
								try {
									$scope.startLoader();
									offset = offset != null ? offset : 0;
									noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
									var data = {};
									
									if ($scope.bedManagmentCookiesObj != null) 
										{
										data = {
												"createdDate":moment(new Date()).format('YYYY-MM-DD'),
												"organizationId" : $scope.organizationId,
												"unitId" : $scope.unitId,
												"admissionId" : $scope.bedManagmentCookiesObj.admissionId,
												"offset":offset,
												"noOfRecordsPerPage":noOfRecordsPerPage,
												"wardList": $scope.wardListForGetBedList
											};
										}else{
											data = {
													"createdDate":moment(new Date()).format('YYYY-MM-DD'),
													"organizationId" : $scope.organizationId,
													"unitId" : $scope.unitId,
													"offset":offset,
													"noOfRecordsPerPage":noOfRecordsPerPage,
													"wardList": $scope.wardListForGetBedList
												};
										}
									
									var URI = BASE_URL + ROOT_URL+"/api/otims/OTTransfer/initOTTransferRequest";
									console.log("URI", URI);
									GenericService
											.serviceAction("POST", URI, data)
											.then(
													function(response) {
														if(response.data.listObject.length>0){
															var surgeriesList=[];
															angular.forEach(response.data.listObject,function(value,index){
																const includesSearchValue = Object.values(surgeriesList).some(value1 => value1.procedureScheduleId === value.procedureScheduleId);
																if(!includesSearchValue)
																	{
																	surgeriesList.push(value);
																	}
															});
															angular.forEach(surgeriesList,function(value,index){
																if(value.otTransferStatus != 6){
																	value.isSelected = false;
																	value.transferTime = moment(new Date()).format("HH:mm:ss");
																	if(value.procedureName == null){
																		value.preferedDateTime = null; 
																	}
																	$scope.surgeriesOfTheDayList.push(value);
																	$scope.commonListCountInitiate = response.data.object;
																	$scope.setPageInitiate(1,false);
																}
															});
														}
														$rootScope.stopLoader();
														
														console.log("$scope.surgeriesOfTheDayList",$scope.surgeriesOfTheDayList);
													});
								} catch (e) {
									console.log(e.message)
									$scope.stopLoader();
								}	
							}		
							
							$scope.getSurgeriesOfTheDay = function(organizationId,offset, noOfRecordsPerPage,unitId){
								$scope.surgeriesOfTheDayList = []; 
								try {
									$scope.startLoader();
									offset = offset != null ? offset : 0;
									noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
									var data = {};
									
									if ($scope.bedManagmentCookiesObj != null) 
										{
										data = {
												"createdDate":moment(new Date()).format('YYYY-MM-DD'),
												"organizationId" : $scope.organizationId,
												"unitId" : $scope.unitId,
												"admissionId" : $scope.bedManagmentCookiesObj.admissionId,
												"offset":offset,
												"noOfRecordsPerPage":noOfRecordsPerPage,
											};
										}else{
											data = {
													"createdDate":moment(new Date()).format('YYYY-MM-DD'),
													"organizationId" : $scope.organizationId,
													"unitId" : $scope.unitId,
													"offset":offset,
													"noOfRecordsPerPage":noOfRecordsPerPage,
												};
										}
									
									var URI = BASE_URL + ROOT_URL+"/api/otims/OTTransfer/initOTTransferRequest";
									console.log("URI", URI);
									GenericService
											.serviceAction("POST", URI, data)
											.then(
													function(response) {
                                                        var surgeriesList=[];
														if(response.data.listObject.length>0){
															angular.forEach(response.data.listObject,function(value,index){
																const includesSearchValue = Object.values(surgeriesList).some(value1 => value1.procedureScheduleId === value.procedureScheduleId);
																if(!includesSearchValue)
																	{
																	surgeriesList.push(value);
																	}
															});
															angular.forEach(surgeriesList,function(value,index){
																if(value.otTransferStatus != 6){
																	value.isSelected = false;
																	value.transferTime = moment(new Date()).format("HH:mm:ss");
																	if(value.procedureName == null){
																		value.preferedDateTime = null; 
																	}
																	$scope.surgeriesOfTheDayList.push(value);
																	$scope.commonListCountInitiate = response.data.object;
																}
															});
														}
														$rootScope.stopLoader();
														
														console.log("$scope.surgeriesOfTheDayList",$scope.surgeriesOfTheDayList);
													});
								} catch (e) {
									console.log(e.message)
									$scope.stopLoader();
								}	
							}
							

							$scope.pager = {};
							$scope.page;

							$scope.setPageInitiate = function(page, flag) {
								if (page < 1 || page > $scope.pager.totalPages)
								{
									return;
								}
								$scope.pager = PagerService.GetPager($scope.commonListCountInitiate, page, $scope.noOfRecordsPerPage,$scope.unitId);
								if (flag)
								{
									$scope.getSurgeriesOfTheDay($scope.organizationId ,$scope.pager.startIndex, $scope.pager.pageSize,$scope.unitId);
								}
							}

							$scope.initMasterListInitiate($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
							
							/**List And Pagination End**/
							
							/**List And Pagination Start for transfer**/
							$scope.setNoOfRecordsTransfer = function() {
								$scope.initMasterListTransfer($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
							};
							
							$scope.initMasterListTransfer = function(organizationId,offset, noOfRecordsPerPage,unitId)
							{
								$scope.patientSurgeryTransferList = [];
								try {
									$scope.startLoader();
									offset = offset != null ? offset : 0;
									noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
									var data = {
											"organizationId" : $scope.organizationId,
											"unitId" : $scope.unitId,
											"offset":offset,
											"noOfRecordsPerPage":noOfRecordsPerPage,
											
										};
									var URI = BASE_URL + ROOT_URL+"/api/otims/OTTransfer/getOTTransferRequest";
									console.log("URI", URI);
									GenericService
											.serviceAction("POST", URI, data)
											.then(
													function(response) {
                                                        var surgeriesList=[];
														if(response.data.listObject.length>0){
														angular.forEach(response.data.listObject,function(value,index){
                                                            const includesSearchValue = Object.values(surgeriesList).some(value1 => value1.procedureScheduleId === value.procedureScheduleId);
                                                            if(!includesSearchValue)
                                                                {
                                                                surgeriesList.push(value);
                                                                }
                                                        });
                                                        angular.forEach(surgeriesList,function(value,index){
															value.reasonId="";
															value.reasonNote ="";
															value.acceptRejectStatus=0;
															$scope.patientSurgeryTransferList.push(value);
															$scope.commonListCountTransfer = response.data.object;
															$scope.setPageTransfer(1,false);
														});	
														
														}
														$rootScope.stopLoader();
														console.log("$scope.patientSurgeryTransferList",$scope.patientSurgeryTransferList);
													});
								} catch (e) {
									console.log(e.message)
									$scope.stopLoader();
								}	
							}		
							
							$scope.patientSurgeryTransferList = []; 
							$scope.getpatientSurgeryTransferList = function(organizationId,offset, noOfRecordsPerPage,unitId){
								$scope.patientSurgeryTransferList = []; 
								try {
									
									$scope.startLoader();
									offset = offset != null ? offset : 0;
									noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
									var data = {
											"organizationId" : $scope.organizationId,
											"unitId" : $scope.unitId,
											"offset":offset,
											"noOfRecordsPerPage":noOfRecordsPerPage,
										};
									var URI = BASE_URL + ROOT_URL+"/api/otims/OTTransfer/getOTTransferRequest";
									console.log("URI", URI);
									GenericService
											.serviceAction("POST", URI, data)
											.then(
													function(response) {
														if(response.data.listObject.length>0){
	                                                        var surgeriesList=[];
														angular.forEach(response.data.listObject,function(value,index){
                                                            const includesSearchValue = Object.values(surgeriesList).some(value1 => value1.procedureScheduleId === value.procedureScheduleId);
                                                            if(!includesSearchValue)
                                                                {
                                                                surgeriesList.push(value);
                                                                }
                                                        });
                                                        angular.forEach(surgeriesList,function(value,index){
															value.reasonId="";
															value.reasonNote ="";
															value.acceptRejectStatus=0;
															$scope.patientSurgeryTransferList.push(value);
															$scope.commonListCountTransfer = response.data.object;
															

														});	
														
														}
														$rootScope.stopLoader();
														console.log("$scope.patientSurgeryTransferList",$scope.patientSurgeryTransferList);
													});
								} catch (e) {
									console.log(e.message)
									$scope.stopLoader();
								}	
							}
							

							$scope.pager = {};
							$scope.page;

							$scope.setPageTransfer = function(page, flag) {
								if (page < 1 || page > $scope.pager.totalPages)
								{
									return;
								}
								$scope.pager = PagerService.GetPager($scope.commonListCountTransfer, page, $scope.noOfRecordsPerPage,$scope.unitId);
								if (flag)
								{
									$scope.getpatientSurgeryTransferList($scope.organizationId ,$scope.pager.startIndex, $scope.pager.pageSize,$scope.unitId);
								}
							}

							//$scope.initMasterListTransfer($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
							
							/**List And Pagination End**/
							
							$scope.searchKeyword;
							$scope.transferReqSearch;
						//	$scope.surgeriesOfTheDayList =[];
							
							/*$scope.getSurgeriesOfTheDay = function(){
								$scope.surgeriesOfTheDayList = []; 
								try {
									$scope.startLoader();
									var data = {};
									
									if ($scope.bedManagmentCookiesObj != null) 
										{
										data = {
												"createdDate":moment(new Date()).format('YYYY-MM-DD'),
												"organizationId" : $scope.organizationId,
												"unitId" : $scope.unitId,
												"admissionId" : $scope.bedManagmentCookiesObj.admissionId,
											};
										}else{
											data = {
													"createdDate":moment(new Date()).format('YYYY-MM-DD'),
													"organizationId" : $scope.organizationId,
													"unitId" : $scope.unitId
												};
										}
									
									var URI = BASE_URL + ROOT_URL+"/api/otims/OTTransfer/initOTTransferRequest";
									console.log("URI", URI);
									GenericService
											.serviceAction("POST", URI, data)
											.then(
													function(response) {
														if(response.data.listObject.length>0){
															angular.forEach(response.data.listObject,function(value,index){
																if(value.otTransferStatus != 6){
																	value.isSelected = false;
																	value.transferTime = moment(new Date()).format("HH:mm:ss");
																	if(value.procedureName == null){
																		value.preferedDateTime = null; 
																	}
																	$scope.surgeriesOfTheDayList.push(value);
																}
															});
														}
														$rootScope.stopLoader();
														
														console.log("$scope.surgeriesOfTheDayList",$scope.surgeriesOfTheDayList);
													});
								} catch (e) {
									console.log(e.message)
									$scope.stopLoader();
								}	
							}
							
							$scope.getSurgeriesOfTheDay();*/
							
							/*$scope.patientSurgeryTransferList = []; 
							$scope.getpatientSurgeryTransferList = function(){
								$scope.patientSurgeryTransferList = []; 
								try {
									$scope.startLoader();
									var data = {
											"organizationId" : $scope.organizationId,
											"unitId" : $scope.unitId
										};
									var URI = BASE_URL + ROOT_URL+"/api/otims/OTTransfer/getOTTransferRequest";
									console.log("URI", URI);
									GenericService
											.serviceAction("POST", URI, data)
											.then(
													function(response) {
														if(response.data.listObject.length>0){
														angular.forEach(response.data.listObject,function(value,index){
															value.reasonId="";
															value.reasonNote ="";
															value.acceptRejectStatus=0;
															$scope.patientSurgeryTransferList.push(value);
														});	
														
														}
														$rootScope.stopLoader();
														console.log("$scope.patientSurgeryTransferList",$scope.patientSurgeryTransferList);
													});
								} catch (e) {
									console.log(e.message)
									$scope.stopLoader();
								}	
							}
							$scope.getpatientSurgeryTransferList();*/
							//get active theatre  List
							$scope.theatreList=[];
							$scope.getActivetheatreList=function(){
									$scope.theatreList=[];
									var data = {
											      orgId:$scope.organizationId,
											      unitId:$scope.unitId
											};
								
								var URI = BASE_URL + ROOT_URL + "/api/otims/getActiveOperationTheatreList";	
								
								GenericService.serviceAction("POST",URI,data).then(function(response){
									if(response.data.status == "success"){								
										$scope.theatreList = response.data.listObject;
										console.log("theatreList list",$scope.theatreList);
									}
								});
							}
							$scope.getActivetheatreList();
							
							$scope.saveOTRequest = function(){
								$scope.startLoader();
							var finalRequestObj = {};	
							var counter = 0;
							var checkValidTime = false;
							var checktheatreId = false;
							if($scope.surgeriesOfTheDayList.length>0){
								angular.forEach($scope.surgeriesOfTheDayList,function(value,index){
									if(value.isSelected){
										if(value.theatreId== undefined || value.theatreId== null || value.theatreId == ""){
											checktheatreId = true;
										}else{
										if(value.preferedDateTime == moment(new Date()).format('YYYY-MM-DD')){
											if(value.transferTime < value.preferTime){
												checkValidTime = true;
											}
										}
										var newArr = {};
										newArr.admissionId = value.admissionId;
										newArr.organizationId =  $scope.organizationId;
										newArr.unitId = $scope.unitId;
									//	newArr.surgeryTime = moment(moment(value.preferedDateTime+" "+value.preferTime).toDate()).format('DD-MM-YYYY HH:mm:ss');
										newArr.transferTime = value.transferTime;
										newArr.theatreId = value.theatreId;
										newArr.otTransferStatus =  6;
										newArr.scheduleId = value.procedureScheduleId;
										newArr.patientId = value.patientId;
										newArr.createdBy = $scope.userId;
										newArr.updatedBy = $scope.userId;
										newArr.createdDate = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
										newArr.updatedDate = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
										newArr.status = 'A';
										
										finalRequestObj = newArr;
										}
									}else{
										counter ++;
									}
								});
								
								
							if(checkValidTime){
								$scope.stopLoader();
								growl.warning("Please Select Valid Transfer Time",
										{
											title : 'warning!'
										});
								return false;
							}
							
							if(checktheatreId){
								$scope.stopLoader();
								growl.warning("Please Select Operation Theatre",
										{
											title : 'warning!'
										});
								return false;
							}
								if(counter == $scope.surgeriesOfTheDayList.length){
									$scope.stopLoader();
									growl.warning('Please select atleast one Request...!!',
											{
												title : 'Warning!'
											});
									return false;
								}
							}
								console.log("finalRequestObj",finalRequestObj);
								
								var URI = BASE_URL + ROOT_URL
										+ "/api/otims/OTTransfer/saveOTTransferRequest";
								GenericService
										.serviceAction("POST", URI,
												finalRequestObj)
										.then(
												function(response) {

													if (response.data.status == "success") {
														$scope.stopLoader();
														growl.success(
																		'OT Request sent Successfully...!!',
																		{
																			title : 'Success!'
																		});
														$scope.getActivetheatreList();
														$scope.getSurgeriesOfTheDay();
														$scope.initMasterListTransfer($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);

													} else {
														$scope.stopLoader();
														growl.error(
																		'Something went wrong...!!',
																		{
																			title : 'Error!'
																		});
													}

												});

							}
							
							$scope.reasonList = [];
							$scope.getReasonMasterList = function()
							{
								var data={
										organizationId:$scope.organizationId
								};
								var URI = BASE_URL + ROOT_URL+ GET_REASON_MASTER_LIST ;
								GenericService
								.serviceAction(METHOD_POST, URI ,data)
								.then(
										function(response) {

											if (response.data.status == "success") {
												$rootScope.stopLoader();
												$scope.reasonList = response.data.listObject;
												console.log("reasonList ",response.data.listObject);
											}
										});
							}
							$scope.getReasonMasterList();
							
							$scope.currentIndex="";
							$scope.acceptOrRejectRequest = function(info,index){
								$scope.currentIndex= index;
									$("#rejectOtTransfer").modal("show");
							}
							
							$scope.updateTrasferRequestStatus = function(){
								$rootScope.startLoader();
								var data = {};
								data.otTransferStatus = $scope.patientSurgeryTransferList[$scope.currentIndex].acceptRejectStatus;
								data.transferRequestId = $scope.patientSurgeryTransferList[$scope.currentIndex].transferRequestId;
								data.admissionId = $scope.patientSurgeryTransferList[$scope.currentIndex].admissionId;
								if($scope.patientSurgeryTransferList[$scope.currentIndex].reasonId == ""){
									$rootScope.stopLoader();
									growl.error("Please Select Reason",
											{
												title : 'error'
											});
									return false;
								}
								data.reasonId = $scope.patientSurgeryTransferList[$scope.currentIndex].reasonId;
								data.reasonNote = $scope.patientSurgeryTransferList[$scope.currentIndex].reasonNote;
								
								var URI = BASE_URL + ROOT_URL + "/api/otims/OTTransfer/updateOTTransferRequest";
								GenericService.serviceAction(METHOD_POST,URI,data).then(function(response) {
									if (response.data.status == "success")
									{
										$rootScope.stopLoader();
										growl.success("Updated Successfully !!!",
												{
													title : 'Success!'
												});
										$scope.patientSurgeryTransferList =[];
										//$scope.getpatientSurgeryTransferList();
										$scope.initMasterListTransfer($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
										$scope.initMasterListInitiate($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
									}
									$rootScope.stopLoader();
								});
							}
							
							/*$scope.checkForValidTime = function(index){
								
								if($scope.surgeriesOfTheDayList[index].transferTime < $scope.surgeriesOfTheDayList[index].transferTime){
									$scope.surgeriesOfTheDayList[index].transferTime = "";
									growl.warning("Please Select Valid Transfer Time!!!",
											{
												title : 'warning!'
											});
									return false;
								}
							}*/
							/************************************* Transfer to OT END **********************************/
							
							$scope.patientDocumnetInformation = function(info) {
								$scope.patientDocumentFlipBook ={};
								$scope.patientDocumentFlipBook =info;
								
									var data =
									{
									"encounterId":$scope.patientDocumentFlipBook.encounterId,
									"patientId":$scope.patientDocumentFlipBook.patientId,
									"unitId":$scope.unitId,
									"organisationId":$scope.organizationId,
									"userId":$scope.userId
									}
									
								var URI = BASE_URL + ROOT_URL + GET_PATIENT_DOCUMENT_ENCOUNTER;
								BillingGenericService.serviceAction(METHOD_POST, URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
										$scope.patientDocumentListTest = [];
										if (response.data.status == "success") 
										{
											$scope.patientDocumentListTest = response.data.listObject;
											//console.log($scope.patientDocumentListTest.length);
											if($scope.patientDocumentListTest != null && $scope.patientDocumentListTest.length >0){
												$('#patientDocumentOption').modal('show');
											}
											$rootScope.stopLoader();
										}
										else if(response.data.status == "error")
											{
											$rootScope.stopLoader();
											}
									});
						
					          };
				          
				          $scope.convertDateToTimestamp = function(dateString) {
				                let parts = dateString.split("-");
				                let day = parseInt(parts[0], 10);
				                let month = parseInt(parts[1], 10) - 1; 
				                let year = parseInt(parts[2], 10);
				                let date = new Date(year, month, day);
				                let timestampInMilliseconds = date.getTime();
				                let timestampInSeconds = timestampInMilliseconds / 1000;
				                return timestampInSeconds;
				            };
				            
					     		$scope.generateFlipbookData = function() {
					                  let flipbookDatalist = [];

						     			var filteredlist=[];
						     		    angular.forEach($scope.patientDocumentListTest,function(filterObj,index){
						     		    	
						     		    	if(filterObj.documentPath != null){
						     				var flipbookDataobj={};
						     				 let createdDate = filterObj.createdDate;
						     	            let timestamp = $scope.convertDateToTimestamp(createdDate);
						     				flipbookDataobj.timestamp=timestamp;
						     				flipbookDataobj.url=filterObj.documentPath;
						     				flipbookDataobj.tags=[];
						     				flipbookDataobj.tags.push("Document");
						     				flipbookDataobj.title=filterObj.documentName != null ? filterObj.documentName :"Lab Report" ; 
						     				flipbookDatalist.push(flipbookDataobj);
						     		    	}
						     				
						     			});

					                let data = {
					                    flipbookData: flipbookDatalist,
					                    logo: $scope.firstImgaa,
					                    privacy: "public"
					                };

					                let baseUrlLocation = $window.location.href;
					                let temp = baseUrlLocation.split('/');
					                let BASE_URL_CASESHEET = baseUrlLocation.replace(temp[temp.length - 1], "");

					                let generateFlipbookurl = "https://feqituyulh.execute-api.ap-south-1.amazonaws.com/dev/api/generateFlipbook";
					                
					        		try {
					        			var param={};
					        				$.ajax({
					        			        url : generateFlipbookurl,
					        					type : "POST",
					        					headers: {'X-Api-Key': "apikey_zHOw4R758ELytSeHFII99YibXj2sWHR6u9mxKdipN/g="},
					        					contentType : "application/json",
					        					dataType : "json",
					        					data : JSON.stringify(data),
					        			        //headers:getHeaders(),
					        					success : function(response){
					        						if(response.flipbookUrl!=undefined &&  response.flipbookUrl!=null  &&  response.flipbookUrl!="" ){
					        							  window.open(''+response.flipbookUrl, '_blank');
					        						}else{
					        							$.notify("Error occurred","error");
					        						}
					        			    	}
					        			});
					        	     } catch (e) {
					        	 		console.log(e );
					        		}
					            };

							
						} ]);

