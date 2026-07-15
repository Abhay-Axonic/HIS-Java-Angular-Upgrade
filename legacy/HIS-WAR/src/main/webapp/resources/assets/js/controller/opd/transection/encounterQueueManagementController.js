
'use strict';

/**
 * @Author By  Vivek Satle
 * @name myApp.controller:encounterQueueManagementController
 * @description #encounterQueueManagementController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'encounterQueueManagementController',
				[
						'$scope',
						'$cookies',
						'$rootScope',
						'GenericService',
						'BillingGenericService',
						'$state',
						'$timeout',
						'$interval',
						'$localStorage',
						'GenericServiceParamHeader',
						'GenericServiceFileUpload',
						'growl',
						'Pubnub',
                        'PagerService',
                        '$location',
                        '$window',
						function($scope, $cookies, $rootScope, GenericService,
								BillingGenericService, $state, $timeout,$interval,$localStorage,GenericServiceParamHeader,GenericServiceFileUpload,growl,Pubnub,PagerService,$location,$window) {

							/*alert("encounterQueueManagementController");*/

							/* init() function for form object create. */
							
							//dismiss model at load
							$('.modal-backdrop').remove();
							
							
							var specialtyNursingStationCookies = JSON.parse(sessionStorage.getItem('specialtyNursingStationCookiesNur'));
							var nursingStationCookie = JSON.parse(sessionStorage.getItem('nursingStationDesc'));
							$scope.nursingStationCookieShow = JSON.parse(sessionStorage.getItem('nursingStationDesc'));
							$scope.nursingStationId  = JSON.parse(sessionStorage.getItem('nursingStationId'));
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
                             /** Variables for pagination */
                             $scope.offset = 0;
                             $scope.noOfRecordsPerPage = 10;
                             $scope.allEncounterList  = [];
                             $scope.totalNoOfRecords;
                             $scope.searchKeyword;
                             $scope.commonListCount;
                             
							$scope.init = function() {
								var date = new Date();
								$scope.dateOptions = {
									formatYear : 'yyyy',
									showWeeks : false,
									minDate : date.setDate((new Date()).getDate() - 90),
								};
								
								
								$scope.dateOptionsReschdule = {
										formatYear : 'yyyy',
										showWeeks : false,
										minDate: new Date()
									};
								
								//Date Object For Search DOB
								$scope.openDatePickerSearch = function() {
									$scope.datepickerSearch.opened = true;
								};

								$scope.datepickerSearch = {
									opened : false
								};
								
								//Date Object For Reshedule
								$scope.openDatePickerReschedule = function() {
									$scope.datepickerReschedule.opened = true;
								};

								$scope.datepickerReschedule = {
									opened : false
								};

								//Date For From Date on Search
								
								$scope.openDatePickerFromDate = function() {
										$scope.datepickerFromDate.opened = true;
								};

								$scope.datepickerFromDate = {
										opened : false
								};
								
								//Date For To Date on Search
								
								$scope.openDatePickerToDate = function() {
										$scope.datepickerToDate.opened = true;
								};

								$scope.datepickerToDate = {
										opened : false
								};
								
								$scope.datepickerFromDate = new Date();
								$scope.datepickerToDate = new Date();

								var cookieObject = $cookies
										.getObject('cookieObject');
								if (cookieObject == undefined) {
									$state.go('login');
									return;
								}
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;
								$scope.roleId = 3;

								$rootScope.loginpage = true;
								$scope.saveBtnFlag = false;
								$scope.updateBtnFlag = true;
								$scope.disableSave = true;
								var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
								 if(cookieObjectAppConfig == undefined){
								  $state.go('login');
								  return;
								 }
								 else
								  {
								   console.log('cookieObjectAppConfig',cookieObjectAppConfig);
								   
								  }
								 if(cookieObject.nursingOpScan == true){
									 $scope.mandatoryScan = 'Y';
								 }else{
									 $scope.mandatoryScan = 'N';
								 }
								 
									var cookieObjectAppConfigSecond = JSON.parse(localStorage.getItem('cookieObjectAppConfigSecond'));
									
									if (cookieObjectAppConfigSecond == undefined) {
										$state.go('login');
										return;
									} 
									else {
										console.log('cookieObjectAppConfigSecond',
												cookieObjectAppConfigSecond);
										

									}
									
								 
			                        $scope.singlePageRefresh = cookieObjectAppConfigSecond.singlePageRefresh;

								
								$scope.roleName = cookieObjectAppConfig.roleName;
								$scope.nurseRole = cookieObjectAppConfig.nurseRole;	 
								$scope.doctorRole = cookieObjectAppConfig.doctorRole;
								$scope.dateFormat = cookieObjectAppConfig.dateFormat;
								$scope.isKiosk = cookieObjectAppConfig.isKiosk;
								//$scope.departmentNursingStationMapperDtoslist = cookieObjectAppConfig.departmentNursingStationMapperDtoslist;
								$scope.isKioskAnnouncementRequired = cookieObjectAppConfig.isKioskAnnouncementRequired;
								$scope.isAppointmentRequired = cookieObjectAppConfig.isAppointmentRequired;
							
								
								
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
								$scope.selectsetNursingStation = function()
								{
									if($scope.departmentNursing == undefined || $scope.departmentNursing.length == 0){
										growl.warning('Please select nursing station..!!');
										return false;
									}else{
									$scope.wardListForGetBedList=[];
									var data = JSON.parse($scope.departmentNursing);
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
												angular.element('#nursingStationModal').modal('hide');
												
												var specialtyNursingStationCookies = JSON.parse(sessionStorage.getItem('specialtyNursingStationCookiesNur'));
												var nursingStationCookie = JSON.parse(sessionStorage.getItem('nursingStationDesc'));
												$scope.nursingStationCookieShow = JSON.parse(sessionStorage.getItem('nursingStationDesc'));
												$scope.nursingStationId  = JSON.parse(sessionStorage.getItem('nursingStationId'));
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
//												$state.reload();
												 $scope.encounterSearchObjectNew.nursingStationIdNew =$scope.nursingStationId;
											    $scope.searchEncounter($scope.offset,$scope.noOfRecordsPerPage);
//												$state.reload("encounterQueueManagement");
											}
									 });
								}
								}
								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup
								
								$scope.slotTypeId = 1;
								
								$scope.encounterAsseismentRequest = {}
								$scope.itemSelecteds = {};

								
								var commonObj = {
									unitId : $scope.unitId,
									organizationId : $scope.organizationId
								};
								var data = {};
								var headerObj = {
										organization_id:$scope.organizationId,
										unit_id : $scope.unitId
								}
								var paramObj = {};

								//For Get Encounter List
								/*var URI = BASE_URL + ROOT_URL
										+ GETLISTOFENCOUNTERMASTER;
								GenericServiceParamHeader
										.serviceAction(METHOD_GET, URI,
												data,
												NOTIFICATION_MSG_STATUS_FALSE,paramObj,headerObj)
										.then(
												function(response) {
													console.log(response);
													// $rootScope.startLoader();
													if (response.data.status == "success")
														$scope.allEncounterList = response.data.listObject;
													console.log('allEncounterList',$scope.allEncounterList);
												});*/
								
								var URI = BASE_URL + ROOT_URL
								+ "/api/global/doctor/activeWithSpeciality";
						GenericService
								.serviceAction("POST", URI, commonObj)
								.then(
										function(response) {
											$scope.allDoctorsList = [];

											if (response.data.status == "success") {
												$scope.allDoctorsList = response.data.listObject;
												console.log("allDoctorsList",$scope.allDoctorsList);

											}
										});
						
								//For Get Nursingstation List
								var URI = BASE_URL + ROOT_URL
										+ GETLISTOFNURSINGSTATION;
								GenericServiceParamHeader
										.serviceAction(METHOD_GET, URI,
												data,
												NOTIFICATION_MSG_STATUS_FALSE,paramObj,headerObj)
										.then(
												function(response) {
													console.log(response);
													// $rootScope.startLoader();
													if (response.data.status == "success")
														$scope.nursingStationList = response.data.listObject;
												});
												
												//For Get Nursingstation List
												var URI = BASE_URL + ROOT_URL
														+ GETLISTOFAPPOINTMENTSTATUSMASTER;
												GenericServiceParamHeader
														.serviceAction(METHOD_GET, URI,
																data,
																NOTIFICATION_MSG_STATUS_FALSE,paramObj,headerObj)
														.then(
																function(response) {
																	console.log(response);
																	// $rootScope.startLoader();
																	if (response.data.status == "success")
																		$scope.encounterStatusList = response.data.listObject;
																});
								
								//For Get Active Reason List
								var URI = BASE_URL + ROOT_URL
										+ GETACTIVEREASONLIST;
								GenericService
										.serviceAction("POST", URI, commonObj)
										.then(
												function(response) {
													$scope.reasonList = [];

													if (response.data.status == "success") {
														$scope.reasonList = response.data.listObject;

													}
												});
								
								//For Get Active Speciaity List
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
												});
								     
								     $scope.encounterSearchObject = {
								    		 'doctorId':0,
								    		 'specialityId':0,
								    		 'patientId':0,
								    		 'specialityId':0,
								    		 'statusId':0,
								    		 'clinicId':0,
								    		 'nursingStationId':0,
								    		
								     }
								     
								  $scope.encounterSearchObjectNew = {
								    		 'nursingStationIdNew':$scope.nursingStationId != undefined ? $scope.nursingStationId.toString() :0,
								    		
								     }
							}

							$scope.init();
							
							//Function For Get Doctor List By Speciality
							$scope.getDoctorBySpeciality = function(id) {
								var data = {
									unitId : $scope.unitId,
									organizationId : $scope.organizationId
								};
								
								//Condition For All Doctor Or By Speciality ID
								if(id > 0)
									{
										data.specialityId = id;
										var URI = BASE_URL + ROOT_URL
										+ GETDOCTORBYSPECIALITYID;

									}
								else
								{
									var URI = BASE_URL + ROOT_URL
									+ GETACTIVEDOCTORLIST;
								}
								
								BillingGenericService
										.serviceAction(METHOD_POST, URI, data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													console.log(response);
													$scope.doctorList = [];
													// $rootScope.startLoader();
													if (response.data.status == "success")
														$scope.doctorList = response.data.listObject;
												});
									}
							$scope.getClinicByDoctorId = function(doctorId)
							{
								var data = {};
								var paramObj = {
										'doctor_id' : doctorId
								};
								var headers={
										'organization_id': $scope.organizationId,
										'unit_id':$scope.unitId,
										'Content-Type':'application/json'
								};
								
								
							   // return false;
							    var URI = BASE_URL + ROOT_URL
								+ GETCLINICMASTERLISTBYDOCTORID;
						
								GenericServiceParamHeader
								.serviceAction(METHOD_GET, URI,
										data,
										NOTIFICATION_MSG_STATUS_FALSE,paramObj,headers)
								.then(
										function(response) {
											console.log(response);
											// $rootScope.startLoader();
											$scope.clinicList = [];
											if (response.data.status == "success")
												
												$scope.clinicList =response.data.listObject;
												console.log("Clnic List",$scope.clinicList);
										});
							}
							
							
							$scope.getNursingStationByClinicId = function(clinicId)
							{
								var data = {};
								
								var paramObj = {
										'clinic_id' : clinicId
								};
								
								var headers={
										'organization_id': $scope.organizationId,
										'unit_id':$scope.unitId,
										'Content-Type':'application/json'
								};
								
								
							   // return false;
							    var URI = BASE_URL + ROOT_URL
								+ GETLISTOFNURSINGSTATIONBYCLINICID;
						
								GenericServiceParamHeader
								.serviceAction(METHOD_GET, URI,
										data,
										NOTIFICATION_MSG_STATUS_FALSE,paramObj,headers)
								.then(
										function(response) {
											console.log(response);
											// $rootScope.startLoader();
											//$scope.clinicList = [];
											if (response.data.status == "success")
												
												console.log("Clnic Nursing List",response.data.listObject[0].nursingStationId);
											$scope.encounterSearchObject.nursingStationId = response.data.listObject[0].nursingStationId.toString();
										});
							}
							
							
							$scope.nursingStationChange = function()
							{
								$scope.encounterSearchObject.doctorId = '0';
								$scope.encounterSearchObject.clinicId = '0';
								

								var URI = BASE_URL+ROOT_URL+"/api/global/getDeptListByNursingStationId/"+$scope.encounterSearchObjectNew.nursingStationIdNew +"/"+$scope.organizationId+"/"+$scope.unitId;
								  GenericService.serviceAction("GET",URI,{}).then(function(response){
									  if (response.data.status == "success") {
										  $scope.departmentNursingStationListC = {};
										 // console.log("fff",response.data.object);
										  $scope.departmentNursingStationMapperDtoslist = response.data.listObject;
										  
									  }
								 });
								
								  
							}
							
							//Search Appointment List By Filters
                            $scope.searchEncounter = function(offset,noOfRecordsPerPage)
							{
                            	$scope.startLoader();
                                $scope.commonListCount = [];
                                $scope.allEncounterList = [];
                                offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
										: 10;
								$scope.encounterSearchObject.fromDate = moment($scope.datepickerFromDate).format('YYYY-MM-DD');
								$scope.encounterSearchObject.toDate = moment($scope.datepickerToDate).format('YYYY-MM-DD');
                                $scope.encounterSearchObject.offset = offset;
                                $scope.encounterSearchObject.noOfRecordsPerPage = noOfRecordsPerPage;
                                $scope.encounterSearchObject.organizationId = $scope.organizationId;
                                $scope.encounterSearchObject.unitId = $scope.unitId;
								$scope.disableSave = true;
								
								
								console.log($scope.encounterSearchObject);
								
								var paramObj = {};
								var headers={
										'Content-Type':'application/json'
								};
								//alert($scope.roleName);
								if($scope.nurseRole == 1)
									{
									$scope.encounterSearchObject.specialityIdList =$scope.departmentNursingStationMapperDtoslist;

									console.log("ff",$scope.encounterSearchObject);
									// return false;
									
							    var URI = BASE_URL + ROOT_URL
								+ GETSEARCHEDLISTENCOUNTERMASTER;
						
								GenericServiceParamHeader
								.serviceAction(METHOD_POST, URI,
										$scope.encounterSearchObject,
										NOTIFICATION_MSG_STATUS_FALSE,paramObj,headers)
								.then(
										function(response) {
											console.log(response);
											// $rootScope.startLoader();
											$scope.allEncounterList = [];
											if (response.data.status == "success"){
												$scope.allEncounterList =response.data.listObject;
												  angular.forEach($scope.allEncounterList, function(value, key) 
															 {
													    	if(value.isPriority == null)
													    	{
													    		value.isPriority = 'N';
													    		value.checkPatientSelect = true;
													    	}
														});
												  if( $scope.encounterSearchObject.statusId != 0){
													  $scope.allEncounterListFilter = $scope.allEncounterList;
													  $scope.allEncounterList = [];
													  angular.forEach($scope.allEncounterListFilter, function(value, key) 
																 {
															  if( $scope.encounterSearchObject.statusId == '1'){
																  if(value.assignmentStartTime == null || value.assignmentStopTime == ''){
																	  $scope.allEncounterList.push(value);
																  }
															  }else if($scope.encounterSearchObject.statusId == '2'){
																  if(value.assignmentStartTime != null && value.assignmentStopTime == null){
																	  $scope.allEncounterList.push(value);
																  }
															  }else if($scope.encounterSearchObject.statusId == '3'){
																  if(value.assignmentStartTime != null && value.assignmentStopTime != null){
																	  $scope.allEncounterList.push(value);
																  }
															  }
															  
															});
												  }
												  
												  
												  
												  console.log('$scope.allEncounterList',$scope.allEncounterList);
                                                  $scope.commonListCount = response.data.object;
                                                  $scope.setPage(1,false);
                                              	$scope.stopLoader();
											}
												
												
										});
									}
						
							}
                            $scope.searchEncounter($scope.offset,$scope.noOfRecordsPerPage);
							
                            $scope.setNoOfRecords = function() {
                            	  $scope.searchEncounter($scope.offset,$scope.noOfRecordsPerPage);
							};
                            
                            $scope.getEncounterList = function(offset,noOfRecordsPerPage){
                                $scope.startLoader();
                                $scope.allEncounterList = [];
                            	$scope.encounterSearchObject.fromDate = moment($scope.datepickerFromDate).format('YYYY-MM-DD');
                                $scope.encounterSearchObject.toDate = moment($scope.datepickerToDate).format('YYYY-MM-DD');
                                $scope.encounterSearchObject.offset = offset;
                                $scope.encounterSearchObject.noOfRecordsPerPage = noOfRecordsPerPage;
                                $scope.encounterSearchObject.organizationId = $scope.organizationId;
                                $scope.encounterSearchObject.unitId = $scope.unitId;
                                $scope.disableSave = true;
                                var paramObj = {};
                                var headers={
                                        'Content-Type':'application/json'
                                };
                                if($scope.nurseRole == 1)
                                    {
                                    $scope.encounterSearchObject.specialityIdList =$scope.departmentNursingStationMapperDtoslist;
                                    console.log("ff",$scope.encounterSearchObject);
                                var URI = BASE_URL + ROOT_URL
                                + GETSEARCHEDLISTENCOUNTERMASTER;
                        
                                GenericServiceParamHeader.serviceAction(METHOD_POST, URI,$scope.encounterSearchObject,
                                        NOTIFICATION_MSG_STATUS_FALSE,paramObj,headers).then(
                                        function(response) {
                                            $scope.allEncounterList = [];
                                            if (response.data.status == "success"){
                                                $scope.allEncounterList =response.data.listObject;
                                                  angular.forEach($scope.allEncounterList, function(value, key) 
                                                             {
                                                            if(value.isPriority == null)
                                                            {
                                                                value.isPriority = 'N';
                                                                value.checkPatientSelect = true;
                                                            }
                                                        });
                                                  $scope.stopLoader();
                                            }
                                        });
                                    }
                            }
                            $scope.pager = {};
                            $scope.page;
                            $scope.setPage = function(page, flag)
                            {
                                $scope.pager.totalPages = undefined;
                                if (page < 1 || page > $scope.pager.totalPages)
                                {
                                    return;
                                }
                                
                                $scope.pager = PagerService.GetPager($scope.commonListCount, page,$scope.noOfRecordsPerPage);
                                
                                if (flag) {
                                	if($scope.searchKeyword == undefined || $scope.searchKeyword == null || $scope.searchKeyword == ""){
                                    $scope.getEncounterList($scope.pager.startIndex,$scope.pager.pageSize);
                                	}else{
                                		$scope.universalSearchEncounter($scope.searchKeyword);
                                	}
                                }
                            }
                            
							$scope.checkRadioButton=false;
							
							$scope.selectItem = function(item,index1) {

								
								if(item.isPriority=='Y' || item.appointmentStatusId!=5)
									{
										$scope.disableSave = true;
									}
								if((item.isPriority==null || item.isPriority=='N') && item.appointmentStatusId==5)
								{
									$scope.disableSave = false;
									
									angular.forEach($scope.allEncounterList,function(value,index){
										if(value.patientId == item.patientId){
											value.checkPatientSelect = false;
										 }else{
											 value.checkPatientSelect = true;
											 //value.isPriority = 'N';
										 }
										
									});
								}
								if(item.patientId != null ){
									$scope.idx=index1;
								$scope.checkRadioButton=true;
								}
								$scope.itemSelecteds = [];
								// If checkbox is checked
								if ($scope.encounterAsseismentRequest) {
									$scope.itemSelecteds = item;
								}
							}
							$scope.saveAssiesment = function()
							{
								if ($scope.itemSelecteds.isPriority =='N') {
									growl
											.warning(
													'Please check priority...!!',
													{
														title : 'Warning!!'
													});
								} else {
									
										//alert('ASSIGMENT START NULL');
									$rootScope.startLoader();
										var headerObj={
												'organization_id': $scope.organizationId,
												'unit_id':$scope.unitId,
												'Content-Type':'application/json'
										};
										
											var data = {
												'assignmentStartTime':moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
												'assignmentStopTime':moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
												'isAssignmentCompStatus':'N',
												'assignmentStartBy':$scope.userId,
												'assignmentStopBy': "",
												'assignmentStartRoleId':$scope.roleId,
												'assignmentStopRoleId':"",
												'isConsultationStatus': "N",
												'consultationStartTime':$scope.itemSelecteds.consultationStartTimeDate,
												'consultationStopTime': null,
												'consultationStartBy': $scope.itemSelecteds.consultationStartBy,
												'consultationStopBy': "",
												'consultationStartRoleId':$scope.itemSelecteds.consultationStartRoleId,
												'consultationStopRoleId': "",
												'encounterId': $scope.itemSelecteds.encounterId,
												'patientId': $scope.itemSelecteds.patientId,
												'isPriority': $scope.itemSelecteds.isPriority
											}
											data.orderDetailsId = $scope.itemSelecteds.orderDetailsId;
											if($scope.itemSelecteds.assignmentStartTime != null ||$scope.itemSelecteds.assignmentStartTime == '')
											{
												data.encounterDetailsMapperId = $scope.itemSelecteds.encounterDetailsMapperId;
											}
											console.log('ASSIGMENT DATA',data);
											//return false;
											
											var paramObjForPer={};
											var URI = BASE_URL + ROOT_URL
											+ ENCOUNTERDETAILS;
											GenericServiceParamHeader
												.serviceAction(METHOD_POST, URI,
														data,
														NOTIFICATION_MSG_STATUS_FALSE,paramObjForPer,headerObj)
												.then(
														function(response) {
															//console.log(response);
															//console.log(encounterObj.encounterId);
															
															// $rootScope.startLoader();
															if (response.data.status == "success")
																{
																
																var URI = BASE_URL + ROOT_URL+ "/api/opd/patientCalled";
																
																var statusChangeObj={
																		encounterId :$scope.itemSelecteds.encounterId,
																		isCalled: 'Y', 
																		orderDetailId :$scope.itemSelecteds.orderDetailsId
																}
																console.log(statusChangeObj,'statusChangeObj')
																//return false;
																GenericService
																.serviceAction("POST", URI, statusChangeObj)
																.then(
																		function(response) {
																			if (response.data.status == "success") {
																				//$scope.allEncounterList[idx].isCalled= 'Y';
																				growl.success(
																						'Patient marked as priority patient...!!',
																						{
																							title : 'Success!'
																						});
																				$scope.init();
																				$scope.searchEncounter();
																				$rootScope.stopLoader();
																			}
																		});
																
																	//
																}
														});
											
									
								}
							}
							
						
							$scope.clearSearchObject = function()
							{
								$scope.encounterSearchObject = {
							    		 'doctorId':0,
							    		 'specialityId':0,
							    		 'organizationId':$scope.organizationId,
							    		 'unitId':$scope.unitId,
							    		 'patientId':0,
							    		 'specialityId':0,
							    		 'statusId':0,
							    		 'nursingStationId':0,
							    		 'clinicId':0
							     }
								
								$scope.clinicList = [];
								
								$scope.patientName ='';
								$scope.uhidNumber = '';
								$scope.mobileNumber = '';
								
								$scope.datepickerFromDate = new Date();
								$scope.datepickerToDate = new Date();
							}
							
							$scope.searchGroupList = function(keyword) {

								var data = {
									patientName : keyword,
									unitId : $scope.unitId,
									organizationId : $scope.organizationId
								}
								console.log(data);

								var URI = BASE_URL + ROOT_URL
										+ PATIENTAUTOFILLSEARCH;
								
								return BillingGenericService
										.serviceAction(METHOD_POST, URI,
												data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													console.log(response);
													// $rootScope.startLoader();
													if (response.data.status == "success")
														return response.data.listObject;
												});

							}
							
							$scope.selectPatientId = function(info, model,
									label) {
								
								$scope.patientName = info.patientName;
								$scope.uhidNumber = info.uhidNumber;
								$scope.mobileNumber = info.mobileNumber;
								
								$scope.encounterSearchObject.patientId = info.patientId;
							}
						
							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.saveAppointmentSchedule();
							}

							$scope.getAppointmentDetails = function(item)
							{
								$scope.appointmentDate = item.appointmentDate;
								$scope.appointmentFromTimeString = item.fromTimeString;
								$scope.appointmentRemark = item.remark;
							}
							
							$scope.getRefralDetails = function(item)
							{
								$scope.referralTypeName = item.referralTypeName;
								$scope.referralName = item.referralName;
								$scope.referralRemark = item.referralRemark;
							}
							

                            $scope.getmappedDataCoversheetByUser = function(){
								
								var URI = BASE_URL + ROOT_URL + "/api/opd/getMappedCoversheetDataForUser";
								var data = {
										  userId:$scope.userId,
									      unitId:$scope.unitId
								}
								GenericService.serviceAction("POST",URI,data).then(function(response){
									if(response.data.status == "success"){
									$rootScope.stopLoader();					
									$scope.mappedDataCoversheetByUser = [];
									$scope.mappedDataCoversheetByUser = JSON.parse(response.data.listObject[0].tabAccessIds);
									localStorage.setItem("mappedDataCoversheetByUser", JSON.stringify($scope.mappedDataCoversheetByUser));
									
									console.log("mappedDataCoversheetByUser",$scope.mappedDataCoversheetByUser);
									}
								});
								}
							$scope.getmappedDataCoversheetByUser();
							
							
							$scope.gotoCoverSheet = function(encounterObj)
							{
								
							if(encounterObj.statusId != '1' && encounterObj.statusId != '8' && $scope.roleId == '3' ){
								if(encounterObj.consultationStartTime == null || encounterObj.consultationStartTime == '')
								{
									//alert("CONS START TIME NULL");
									var headerObj={
											'organization_id': $scope.organizationId,
											'unit_id':$scope.unitId,
											'Content-Type':'application/json'
									};
									
										var data = {
											'assignmentStartTime':encounterObj.assignmentStartTimeDate,
											'assignmentStopTime':encounterObj.assignmentStopTimeDate,
											'isAssignmentCompStatus':encounterObj.isAssignmentCompStatus,
											'assignmentStartBy':encounterObj.assignmentStartBy,
											'assignmentStopBy': encounterObj.assignmentStopBy,
											'assignmentStartRoleId':encounterObj.assignmentStartRoleId,
											'assignmentStopRoleId': encounterObj.assignmentStopRoleId,
											'isConsultationStatus': "N",
											'consultationStartTime':moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
											'consultationStopTime': encounterObj.consultationStopTime,
											'consultationStartBy': $scope.userId,
											'consultationStopBy': "",
											'consultationStartRoleId':$scope.roleId,
											'consultationStopRoleId': "",
											'encounterId': encounterObj.encounterId,
											'patientId': encounterObj.patientId
										}
										data.orderDetailsId = encounterObj.orderDetailsId;
										
										console.log('Consultant DATA',data);
										//return false;
										var paramObjForPer={};
										var URI = BASE_URL + ROOT_URL
										+ ENCOUNTERDETAILS;
										GenericServiceParamHeader
											.serviceAction(METHOD_POST, URI,
													data,
													NOTIFICATION_MSG_STATUS_FALSE,paramObjForPer,headerObj)
											.then(
													function(response) {
														console.log(response);
														console.log(encounterObj.encounterId);
														
														// $rootScope.startLoader();
														if (data.status == "success")
															console.log("in success");
															var cookieObject = 
															{
																	"patientName" :encounterObj.patientName,
																	"uhidNumber" : encounterObj.uhidNumber,
																	"genderCode" : encounterObj.genderCode,
																	"age":  encounterObj.age,
																	"dob": encounterObj.dob,
																	"encounterId":encounterObj.encounterId,
																	"genderId":encounterObj.genderId,
																	"roleId" :$scope.roleId,
																	"patientId":encounterObj.patientId,
																	"doctorId":encounterObj.doctorId,
																	"specialityId":encounterObj.specialityId,
																	"paymentEntitlementTypeId":encounterObj.paymentEntitlementTypeId,
																	"doctorName":encounterObj.doctorName,
																	"prefixId":encounterObj.prefixId,
																	"genderId":encounterObj.genderId,
																	"appointmentId":encounterObj.appointmentId,
																	"visitTypeId" :1,
																	"patientCategoryId" : encounterObj.patientCategoryId
																	
															}
															
//															cookieObject.mappedDataCoversheetByUserss = [];
//															cookieObject.mappedDataCoversheetByUserss = $scope.mappedDataCoversheetByUser, 
															 sessionStorage.setItem('cookieEncounterObject', JSON.stringify(cookieObject));	
															console.log("EO",cookieObject);
															//return false;
															$state.go("OPDCoverSheet");
													});
										
								}
								else
								{
									
									//alert("CON ADDED");
									//return false;
									var cookieObject = 
									{
											"patientName" :encounterObj.patientName,
											"uhidNumber" : encounterObj.uhidNumber,
											"genderCode" : encounterObj.genderCode,
											"age":  encounterObj.age,
											"dob": encounterObj.dob,
											"encounterId":encounterObj.encounterId,
											"genderId":encounterObj.genderId,
											"roleId" :$scope.roleId,
											"patientId":encounterObj.patientId,
											"doctorId":encounterObj.doctorId,
											"specialityId":encounterObj.specialityId,
											"paymentEntitlementTypeId":encounterObj.paymentEntitlementTypeId,
											"doctorName":encounterObj.doctorName,
											"prefixId":encounterObj.prefixId,
											"genderId":encounterObj.genderId,
											"appointmentId":encounterObj.appointmentId,
											"visitTypeId" :1,
											"patientCategoryId" : encounterObj.patientCategoryId
											
									}
									
//									cookieObject.mappedDataCoversheetByUserss = [];
//									cookieObject.mappedDataCoversheetByUserss = $scope.mappedDataCoversheetByUser, 
									
									 sessionStorage.setItem('cookieEncounterObject', JSON.stringify(cookieObject));	
									console.log("EO",cookieObject);
									//return false;
									$state.go("OPDCoverSheet");
								}
								
							}
								
								
							}
							
							$scope.getClassForColor = function(info)
							{
								   
								   if(info.isCriticalOrder==1){
									   return 'td-bg-red';
								   }else if (info.isVip=='Y') 
								   {
									    return 'td-bg-blue';
								   }else if(info.encounterTypeId==6){
									   return 'td-bg-green';
								   }
							}
							
							

							//alert();
							$scope.updateEncounterDetails = function(encounterObj){
								
							$rootScope.startLoader();
						if(encounterObj.statusId != '1' && encounterObj.statusId != '8' && $scope.roleId == '3'){
							
							if(encounterObj.assignmentStartTime == null || encounterObj.assignmentStartTime == '')
							{
								//alert('ASSIGMENT START NULL');
								var headerObj={
										'organization_id': $scope.organizationId,
										'unit_id':$scope.unitId,
										'Content-Type':'application/json'
								};
								
									var data = {
										'assignmentStartTime':moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
										'assignmentStopTime':null,
										'isAssignmentCompStatus':'N',
										'assignmentStartBy':$scope.userId,
										'assignmentStopBy': "",
										'assignmentStartRoleId':$scope.roleId,
										'assignmentStopRoleId':"",
										'isConsultationStatus': "N",
										'consultationStartTime':encounterObj.consultationStartTimeDate,
										'consultationStopTime': null,
										'consultationStartBy': encounterObj.consultationStartBy,
										'consultationStopBy': "",
										'consultationStartRoleId':encounterObj.consultationStartRoleId,
										'consultationStopRoleId': "",
										'encounterId': encounterObj.encounterId,
										'patientId': encounterObj.patientId
									}
									data.orderDetailsId = encounterObj.orderDetailsId;
									console.log('ASSIGMENT DATA',data);
									//return false;
									
									var paramObjForPer={};
									var URI = BASE_URL + ROOT_URL
									+ ENCOUNTERDETAILS;
									GenericServiceParamHeader
										.serviceAction(METHOD_POST, URI,
												data,
												NOTIFICATION_MSG_STATUS_FALSE,paramObjForPer,headerObj)
										.then(
												function(response) {
													console.log(response);
													console.log(encounterObj.encounterId);
													
													// $rootScope.startLoader();
													if (data.status == "success")
														console.log("in success");
														var cookieObject = 
														{
																"patientName" :encounterObj.patientName,
																"uhidNumber" : encounterObj.uhidNumber,
																"genderCode" : encounterObj.genderCode,
																"age":  encounterObj.age,
																"dob": encounterObj.dob,
																"encounterId":encounterObj.encounterId,
																"genderId":encounterObj.genderId,
																"roleId" :$scope.roleId,
																"patientId":encounterObj.patientId,
																"doctorId":encounterObj.doctorId,
																"specialityId":encounterObj.specialityId,
																"paymentEntitlementTypeId":encounterObj.paymentEntitlementTypeId,
																//"doctorName":encounterObj.doctorName,
																"prefixId":encounterObj.prefixId,
																"genderId":encounterObj.genderId,
																"appointmentId":encounterObj.appointmentId,
																"visitTypeId" :1,
																"contractId" : encounterObj.contractId,
																"payeeId" : encounterObj.payeeId,
																"patientCategoryId" : encounterObj.patientCategoryId,
																"isCritical" : encounterObj.isCritical
																
														}
														if(encounterObj.doctorId != null && encounterObj.doctorId > 0){
															cookieObject.doctorName = encounterObj.doctorName;
														}else{
															cookieObject.doctorName = null;
														}
														 sessionStorage.setItem('cookieEncounterObject', JSON.stringify(cookieObject));	
														console.log("EO",cookieObject);
														//return false;
														$rootScope.stopLoader();
												});
									
							}
							else
							{
								//alert('ASSIGMENT START MILA');
								//return false;
								var cookieObject = 
								{
										"patientName" :encounterObj.patientName,
										"uhidNumber" : encounterObj.uhidNumber,
										"genderCode" : encounterObj.genderCode,
										"age":  encounterObj.age,
										"dob": encounterObj.dob,
										"encounterId":encounterObj.encounterId,
										"genderId":encounterObj.genderId,
										"roleId" :$scope.roleId,
										"patientId":encounterObj.patientId,
										"doctorId":encounterObj.doctorId,
										"specialityId":encounterObj.specialityId,
										"paymentEntitlementTypeId":encounterObj.paymentEntitlementTypeId,
										//"doctorName":encounterObj.doctorName,
										"prefixId":encounterObj.prefixId,
										"genderId":encounterObj.genderId,
										"appointmentId":encounterObj.appointmentId,
										"visitTypeId" :1,
										"contractId" : encounterObj.contractId,
										"payeeId" : encounterObj.payeeId,
										"patientCategoryId" : encounterObj.patientCategoryId,
										"isCritical" : encounterObj.isCritical
										
								}
								
								if(encounterObj.doctorId != null && encounterObj.doctorId > 0){
									cookieObject.doctorName = encounterObj.doctorName;
								}else{
									cookieObject.doctorName = null;
								}
								
								 sessionStorage.setItem('cookieEncounterObject', JSON.stringify(cookieObject));	
								console.log("EO",cookieObject);
								//return false;
								$rootScope.stopLoader();
							}
						  }
						}
							
							$scope.gotoCoverSheetAssigment = function(encounterObj)
							{
								//alert();
								
								$rootScope.startLoader();
							if((encounterObj.statusId != '1' && encounterObj.statusId != '8' && $scope.roleId == '3') || (encounterObj.isTeleconsultationAppointment==1 && encounterObj.billStatusId==3))
							{
								//$scope.isKiosk
								if($scope.isKiosk == 'N' && (encounterObj.assignmentStartTime == null || encounterObj.assignmentStartTime == ''))
								{
									//alert('ASSIGMENT START NULL');
									var headerObj={
											'organization_id': $scope.organizationId,
											'unit_id':$scope.unitId,
											'Content-Type':'application/json'
									};
									
										var data = {
											'assignmentStartTime':moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
											'assignmentStopTime':null,
											'isAssignmentCompStatus':'N',
											'assignmentStartBy':$scope.userId,
											'assignmentStopBy': "",
											'assignmentStartRoleId':$scope.roleId,
											'assignmentStopRoleId':"",
											'isConsultationStatus': "N",
											'consultationStartTime':encounterObj.consultationStartTimeDate,
											'consultationStopTime': null,
											'consultationStartBy': encounterObj.consultationStartBy,
											'consultationStopBy': "",
											'consultationStartRoleId':encounterObj.consultationStartRoleId,
											'consultationStopRoleId': "",
											'encounterId': encounterObj.encounterId,
											'patientId': encounterObj.patientId
										}
										data.orderDetailsId = encounterObj.orderDetailsId;
										console.log('ASSIGMENT DATA',data);
										//return false;
										
										var paramObjForPer={};
										var URI = BASE_URL + ROOT_URL
										+ ENCOUNTERDETAILS;
										GenericServiceParamHeader
											.serviceAction(METHOD_POST, URI,
													data,
													NOTIFICATION_MSG_STATUS_FALSE,paramObjForPer,headerObj)
											.then(
													function(response) {
														console.log(response);
														console.log(encounterObj.encounterId);
														
														// $rootScope.startLoader();
														if (response.data.status == "success")
														{

															
															var URI = BASE_URL + ROOT_URL+ "/api/opd/patientCalled";
															
															var statusChangeObj={
																	encounterId :encounterObj.encounterId,
																	isCalled: 'Y', 
																	orderDetailId :encounterObj.orderDetailsId
															}
															console.log(statusChangeObj,'statusChangeObj')
															//return false;
															GenericService
															.serviceAction("POST", URI, statusChangeObj)
															.then(
																	function(response) {
																		
																		var cookieObject = 
																		{
																				"patientName" :encounterObj.patientName,
																				"uhidNumber" : encounterObj.uhidNumber,
																				"genderCode" : encounterObj.genderCode,
																				"age":  encounterObj.age,
																				"dob": encounterObj.dob,
																				"encounterId":encounterObj.encounterId,
																				"genderId":encounterObj.genderId,
																				"roleId" :$scope.roleId,
																				"patientId":encounterObj.patientId,
																				"encounterDate":encounterObj.encounterDate,
																				"encounterDateWithTime":encounterObj.encounterDateWithTime,
																				"consultationStartTime":encounterObj.encounterTime,
																				'doctorId':encounterObj.doctorId!=null ? encounterObj.doctorId:encounterObj.preferredDocId,
																				"specialityId":encounterObj.specialityId,
																				"specialityName":encounterObj.specialityName,
																				"paymentEntitlementTypeId":encounterObj.paymentEntitlementTypeId,
																				//"doctorName":encounterObj.doctorName,
																				"prefixId":encounterObj.prefixId,
																				"genderId":encounterObj.genderId,
																				"appointmentId":encounterObj.appointmentId,
																				"visitTypeId" :1,
																				"contractId" : encounterObj.contractId,
																				"payeeId" : encounterObj.payeeId,
																				"patientCategoryId" : encounterObj.patientCategoryId,
																				"isCritical" : encounterObj.isCritical,
																				"uuid":encounterObj.uuid,
																				'ehcPatient':'N',
																				"email":encounterObj.email,
																				"emailDoctor":encounterObj.emailDoctor,
																				"videoCallUrl":encounterObj.videoCallUrl,
																				
																		}
																		if(encounterObj.doctorId != null && encounterObj.doctorId > 0){
																			cookieObject.doctorName =encounterObj.doctorName;
																			}else if(encounterObj.preferredDocId != null && encounterObj.preferredDocId > 0){
																				cookieObject.doctorName = encounterObj.preferedDocName ;
																			}else{
																			cookieObject.doctorName = null;
																			}
//																		cookieObject.mappedDataCoversheetByUserss = [];
//																		cookieObject.mappedDataCoversheetByUserss = $scope.mappedDataCoversheetByUser, 
																		
																		 sessionStorage.setItem('cookieEncounterObject', JSON.stringify(cookieObject));	
																		console.log("EO",cookieObject);
																		//return false;
																		$state.go("OPDCoverSheet");
																		//$rootScope.stopLoader();
																	});
															
																//
															console.log("in success");
															
															
														}	
															
													});
										
								}else if((encounterObj.assignmentStartTime == null || encounterObj.assignmentStartTime == '') && (encounterObj.isTeleconsultationAppointment==1 && encounterObj.billStatusId==3)){
									$scope.updateEncounterDetails(encounterObj);
								}
								/*else
								{*/
									//alert('ASSIGMENT START MILA');
									//return false;
									var cookieObject = 
									{
											"patientName" :encounterObj.patientName,
											"uhidNumber" : encounterObj.uhidNumber,
											"genderCode" : encounterObj.genderCode,
											"age":  encounterObj.age,
											"dob": encounterObj.dob,
											"encounterId":encounterObj.encounterId,
											"genderId":encounterObj.genderId,
											"roleId" :$scope.roleId,
											"patientId":encounterObj.patientId,
											"encounterDate":encounterObj.encounterDate,
											"encounterDateWithTime":encounterObj.encounterDateWithTime,
											"consultationStartTime":encounterObj.encounterTime,
											'doctorId':encounterObj.doctorId!=null ? encounterObj.doctorId:encounterObj.preferredDocId,
											"specialityId":encounterObj.specialityId,
											"specialityName":encounterObj.specialityName,
											"paymentEntitlementTypeId":encounterObj.paymentEntitlementTypeId,
											//"doctorName":encounterObj.doctorName,
											"prefixId":encounterObj.prefixId,
											"genderId":encounterObj.genderId,
											"appointmentId":encounterObj.appointmentId,
											"visitTypeId" :1,
											"contractId" : encounterObj.contractId,
											"payeeId" : encounterObj.payeeId,
											"patientCategoryId" : encounterObj.patientCategoryId,
											"isCritical" : encounterObj.isCritical,
											"uuid" :encounterObj.uuid,
											'ehcPatient':'N',
											"email":encounterObj.email,
											"emailDoctor":encounterObj.emailDoctor,
											"videoCallUrl":encounterObj.videoCallUrl,
									}
									
									if(encounterObj.doctorId != null && encounterObj.doctorId > 0){
										cookieObject.doctorName =encounterObj.doctorName;
										}else if(encounterObj.preferredDocId != null && encounterObj.preferredDocId > 0){
											cookieObject.doctorName = encounterObj.preferedDocName ;
										}else{
										cookieObject.doctorName = null;
										}
//									cookieObject.mappedDataCoversheetByUserss = [];
//									cookieObject.mappedDataCoversheetByUserss = $scope.mappedDataCoversheetByUser, 
									
									 sessionStorage.setItem('cookieEncounterObject', JSON.stringify(cookieObject));	
									console.log("EO",cookieObject);
									//return false;
									$state.go("OPDCoverSheet");
									$rootScope.stopLoader();
//								}
							  }
							}
							
							
							$scope.collectionObj = {};
							$scope.validateUhid = "";
							$scope.validateWorkListDetails = function(obj){
								
								if($scope.mandatoryScan != null && $scope.mandatoryScan =="Y"){
									$scope.collectionObj = obj; 
									$scope.validateUhid = "";
									$('#validatePatient4').modal('show');
									setTimeout(function(){
									    $('#patientValidate').focus();
									}, 500);
								}else{
									$scope.gotoCoverSheetAssigment(obj);
								}
								
							}
							
							$scope.closevalidatePop = function(){
								$('#validatePatient4').modal('hide');
							}
							
							$scope.validateEquealPatient = function(event,barcode){
								 var enterCode = 13;
								 if (!barcode && event.which === enterCode) {
								        barcode = $scope.validateUhid;
								      }
									 if(!!barcode){
										 
										 if($scope.collectionObj.uhidNumber.toLowerCase() == $scope.validateUhid.toLowerCase() ){
												$('#validatePatient4').modal('hide');
												$('.modal-backdrop').remove()
		                                        $(document.body).removeClass("modal-open");
												$scope.gotoCoverSheetAssigment($scope.collectionObj);
											}else{
												growl.warning('Please enter valid patient number',
														{
															title : 'Warning!'
														});
											}
										 
									 }
									
							}
							
							
							
							/**========================DOCUMENT UPLOAD =========================================*/
							$scope.getPoupForUpdate = function(info, index)
							{
							    $scope.infoUpdate = info;
							    $scope.indexUpdate = index;

								
								if(info.oldDocId > 0)
									{
									    $scope.encounterUpdateDoctorId12 = info.originalPreferredDocId;
										$scope.encounterUpdateDoctorId = info.preferredDocId;
										$scope.encounterUpdateOldDoctorId = info.oldDocId;
										$scope.encounterUpdateEncounterId = info.encounterId;
										angular.element('#updateDoctorPopup').modal('show');
										return;
									}else
										{
										$scope.encounterUpdateDoctorId12 = info.originalPreferredDocId;
										$scope.encounterUpdateDoctorId = info.preferredDocId;   //HIS-28839 Nursing worklist - Showing loader, after selecting doctor from drop down. (Is Department wise Scheduling is true at Department master)
										$scope.encounterUpdateOldDoctorId = info.preferredDocId;
										$scope.encounterUpdateEncounterId = info.encounterId;
                                        angular.element('#updateDoctorPopup').modal('show');

										if(isOldIdAvail == false){
												$rootScope.startLoader();
											var URI = BASE_URL + ROOT_URL +"/api/opd/doctor/update/"+info.encounterId+"/"+info.preferredDocId+"/"+info.preferredDocId+"/0";
											
											GenericService
											.serviceAction("GET",URI,'')
											.then(function(response) {
														if (response.data.status == "success") {
															 growl.success("Doctor assigned.",
																		{
																			title : 'Success!'
																		});
															 $scope.searchEncounter();
															   $rootScope.stopLoader();
															   angular.element('#updateDoctorPopup').modal('show');
														}
													});
													}
										
										}
								
							}
							
							$scope.encounterUpdateEncounterId;
							$scope.encounterUpdateOldDoctorId;
							$scope.encounterUpdateDoctorId;

							var isOldIdAvail = true;
//							$scope.searchEncounter = function(){
//							    isOldIdAvail = true;
//							    angular.element('#updateDoctorPopup').modal('hide');
//							}
							
							$scope.updateEncounterForDoctorId = function(info, index)
							{

								if($scope.updateDoctorReasonId==undefined || $scope.updateDoctorReasonId==null || $scope.updateDoctorReasonId==""){
								    $scope.allEncounterList[index].preferredDocId = parseInt($scope.encounterUpdateDoctorId12);

									 growl.warning("Please select reason",{title : "WARNING!"});
									 return false;
								}
								angular.element('#updateDoctorPopup').modal('hide');
								$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL +"/api/opd/doctor/update/"+$scope.encounterUpdateEncounterId+"/"+$scope.encounterUpdateOldDoctorId+"/"+$scope.encounterUpdateDoctorId+"/"+$scope.updateDoctorReasonId;
								
								GenericService
								.serviceAction("GET",URI,'')
								.then(function(response) {
											if (response.data.status == "success") {
												 growl.success("Doctor assigned.",
															{
																title : 'Success!'
															});
												 $scope.searchEncounter();
												   $rootScope.stopLoader();
											}
										});
										isOldIdAvail = false;
								
							}
							
							
							$scope.getDocumentType = function(){
								$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL + GETACTIVEDOCUMENTTYPELIST;
								BillingGenericService.serviceAction(METHOD_GET, URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
										$scope.documentTypeList = [];
										if (response.data.status == "success") {
											$scope.documentTypeList = response.data.listObject;
										}
									});
							}
							$scope.getDocumentType();
							
							$scope.encounterDetailsList = [];
							$scope.encounterDetails = {
								  encounterDocId : "",
								  encounterId : "",
								  patientId : "",
								  documentPath : "",
								  documentTypeId : "",
								  status : 'A',
								  createdDate : "",
								  createdBy : $scope.userId,
								  updatedDate : "",
								  updatedBy : $scope.userId,
								  unitId : $scope.unitId,
								  organisationId : $scope.organizationId,
								  file : {}
								}
							
							$scope.showDocUPloadPoup   = function(encounterObj)
							{
								$scope.encounterDetails.encounterId = encounterObj.encounterId;
								$scope.encounterDetails.patientId = encounterObj.patientId;	
								if($scope.encounterDetailsList.length>0)
									{
									  $('#uploadDetails').modal('show');
									}
								else{
									 $('#uploadDetails').modal('show');
									var encounterDetails = angular.copy($scope.encounterDetails);
									encounterDetails.patientId = encounterObj.patientId;
									encounterDetails.encounterId = encounterObj.encounterId;
									$scope.encounterDetailsList.push(encounterDetails);
								}
							}
							
							$scope.addImageDetails = function()
							{ 
								var encounterDetails = angular.copy($scope.encounterDetails);
								$scope.encounterDetailsList.push(encounterDetails);
							};

						  $scope.removeImageDetails = function(deleteIndex)
							{ 
							   $scope.encounterDetailsList.splice(deleteIndex, 1);
						    }
						  
						//Check before Sequence
						  var isSequenceExists;
							function checkSequenceExistsOrNot(arr) {
								return arr
										.some(function(arr) {
											if(arr.doctorDeskArrival == null || arr.doctorDeskArrival == "N")
												{
													return true;
												}
											else
												{
													return false;
												}
										});
							}
							
							
						  $scope.callPatient = function(info,idx){
					
							    var calIndex = info.queueNumber + 1;
								$scope.sequenceAppointmentObj = info;
								var newAppointmentListForSequence = [];
								var i = 0;
								var loopFlag = true;
								
								angular.forEach($scope.allEncounterList,function(value,index){
									if(info.doctorId == value.doctorId){
										if(value.queueNumber == info.queueNumber){
											var datamessage = {
													triggerId : NURSE_CHECK_IN,
													patientId : value.patientId,
													unitId : $scope.unitId,
													organizationId : $scope.organizationId
												}
											
												console.log(datamessage);

												var URI = BASE_URL + ROOT_URL
														+ "/getSmsUtilityByTriggerIdForInPatientCheckInForm";
												
												return BillingGenericService
														.serviceAction(METHOD_POST, URI,
																datamessage,
																NOTIFICATION_MSG_STATUS_FALSE)
														.then(
																function(response) {
																	console.log(response);
																	// $rootScope.startLoader();
																	if (response.data.status == "success")
																		return response.data.listObject;
																});
										}
										if(value.queueNumber == calIndex){
											var datamessage = {
													triggerId : NEXT_CHECK_IN_CALL,
													patientId : value.patientId,
													unitId : $scope.unitId,
													organizationId : $scope.organizationId
												}
											
												console.log(datamessage);

												var URI = BASE_URL + ROOT_URL
														+ "/getSmsUtilityByTriggerIdForInPatientCheckInForm";
												
												return BillingGenericService
														.serviceAction(METHOD_POST, URI,
																datamessage,
																NOTIFICATION_MSG_STATUS_FALSE)
														.then(
																function(response) {
																	console.log(response);
																	// $rootScope.startLoader();
																	if (response.data.status == "success")
																		return response.data.listObject;
																});
										}
										
									}
								});
								
		/*						angular.forEach($scope.allEncounterList,function(value,index){
									if(info.queueNumber  == 1 && info.doctorId == value.doctorId){
										if(value.queueNumber <= calIndex){
											var datamessage = {
													triggerId : NURSE_CHECK_IN,
													patientId : value.patientId,
													unitId : $scope.unitId,
													organizationId : $scope.organizationId
												}
											
												console.log(datamessage);

												var URI = BASE_URL + ROOT_URL
														+ "/getSmsUtilityByTriggerIdForInPatientCheckInForm";
												
												return BillingGenericService
														.serviceAction(METHOD_POST, URI,
																datamessage,
																NOTIFICATION_MSG_STATUS_FALSE)
														.then(
																function(response) {
																	console.log(response);
																	// $rootScope.startLoader();
																	if (response.data.status == "success")
																		return response.data.listObject;
																});
										}
										
									}
									else if  (calIndex == value.queueNumber){
										var datamessage = {
												triggerId : NURSE_CHECK_IN,
												patientId : value.patientId,
												unitId : $scope.unitId,
												organizationId : $scope.organizationId
											}
										
											console.log(datamessage);

											var URI = BASE_URL + ROOT_URL
													+ "/getSmsUtilityByTriggerIdForInPatientCheckInForm";
											
											return BillingGenericService
													.serviceAction(METHOD_POST, URI,
															datamessage,
															NOTIFICATION_MSG_STATUS_FALSE)
													.then(
															function(response) {
																console.log(response);
																// $rootScope.startLoader();
																if (response.data.status == "success")
																	return response.data.listObject;
															});
									}
								});*/
								angular.forEach($scope.allEncounterList,function(value,index){
									if(idx > i){
									newAppointmentListForSequence.push(value);
									i++;
									}
									if(loopFlag){
									if(i == idx){
										loopFlag = false;
										
											if(info.callCount < 3)
											{	
												if(info.callCount < 2){
													if($scope.isKioskAnnouncementRequired!=null && $scope.isKioskAnnouncementRequired!=undefined &&
															$scope.isKioskAnnouncementRequired == 'Y'){
														 $scope.speech(' Token number, '+info.tokenNumber+', please visit '+ $scope.nursingStationDesc+' , thank you .');
													}
												}
												if(info.callCount == 2){
													if($scope.isKioskAnnouncementRequired!=null && $scope.isKioskAnnouncementRequired!=undefined &&
															$scope.isKioskAnnouncementRequired == 'Y'){
														$scope.speech(' Token number, '+info.tokenNumber+', please visit '+ $scope.nursingStationDesc+' , This is Your Last call, thank you .');
													}
												}
												info.callCount = info.callCount + 1;
											    console.log('info.callCount',info.callCount);	
											var URI = BASE_URL + ROOT_URL + "/api/opd/nursing/kiosk/callCount/"+info.encounterId+"/"+info.callCount+"/"+$scope.nursingStationId;
											GenericService.serviceAction("GET",URI,'').then(function(response) {
														console.log("reee",response);
														if (response.data.status == "success") {
															
															
														}
													});
											}
									}
								}
								});
								
									var obj = {
											unitId : $scope.unitId,
											organizationId : $scope.organizationId,
											encounterId : info.encounterId,
											specialityIdList: $scope.departmentNursingStationMapperDtoslist
										}
										var URI = BASE_URL + ROOT_URL + "/api/opd/update/latestCount";
										GenericService.serviceAction("POST",URI,obj).then(function(response) {
											if (response.data.status == "success") {
												
												
											}
										});
						  }
						  
							//for call patient
						  
						  $scope.beforeSequnce = function()
							{
								var info = $scope.sequenceAppointmentObj;
								if(info.callCount < 3)
								{	
									if(info.callCount < 2){
										if($scope.isKioskAnnouncementRequired!=null && $scope.isKioskAnnouncementRequired!=undefined &&
												$scope.isKioskAnnouncementRequired == 'Y'){
											$scope.speech(' Token number, '+info.tokenNumber+', please visit '+ $scope.nursingStationDesc+' , thank you .');
										}
									}
									
									if(info.callCount == 2){
										if($scope.isKioskAnnouncementRequired!=null && $scope.isKioskAnnouncementRequired!=undefined &&
												$scope.isKioskAnnouncementRequired == 'Y'){
											$scope.speech(' Token number, '+info.tokenNumber+', please visit '+ $scope.nursingStationDesc+' , This is Your Last call, thank you .');
										}
									}
									
									info.callCount = info.callCount + 1;
								
								console.log('info.callCount',info.callCount);	
								var URI = BASE_URL + ROOT_URL + "/api/opd/nursing/kiosk/callCount/"+info.encounterId+"/"+info.callCount+"/"+$scope.nursingStationId;
								
								GenericService
								.serviceAction("GET",URI,'')
								.then(function(response) {
											console.log("reee",response);
											if (response.data.status == "success") {
											}
										});
								}
								
							}
						  
						  
						  
						  $scope.markArrival=function(item,idx)
							{
								//console.log(idx);
							  var URI = BASE_URL + ROOT_URL+ "/api/opd/patientCalled";
									
									var statusChangeObj={
											encounterId : item.encounterId,
											isCalled: 'Y', 
											orderDetailId :item.orderDetailsId
									}
									console.log(statusChangeObj,'statusChangeObj')
									//return false;
									GenericService
									.serviceAction("POST", URI, statusChangeObj)
									.then(
											function(response) {
												if (response.data.status == "success") {
													angular.forEach($scope.allEncounterList, function(value, key) 
															{
																if(value.encounterId==item.encounterId){
																	value.isCalled= 'Y';
																	value.serviceRenderId=1;
																	value.assignmentStartTime = new Date();
																}
															});
												}
											});
								
							}
						  
						  //end call patient
						  
							
							
							$scope.uploadEncounterDoc = function(Obj)
							{
								 var fd = new FormData();
							        fd.append('encounterId', Obj.encounterId);
							        fd.append('patientId',Obj.patientId );
							        fd.append('documentTypeId', Obj.documentTypeId);
							        fd.append('createdBy',Obj.createdBy );
							        fd.append('updatedBy', Obj.updatedBy);
							        fd.append('unitId', Obj.unitId);
							        fd.append('organisationId', Obj.organisationId);
							        fd.append('file',Obj.file );
							        fd.append('expenseId', 0);
							        fd.append('grnId',0);
									fd.append('poId',0);
							        fd.append('incidentReportingMapperId',0);
							        fd.append('companyExpenseId',0);
							        fd.append('createdDate',null);
									fd.append('fileEditedName',null);
									fd.append('opbId',0);
									fd.append('vendorId',0);
							        var URI1 = BASE_URL + ROOT_URL + UPLOAD_ENCOUNTER_DOCUMENT;
							        return GenericServiceFileUpload.serviceAction("POST",URI1,fd,{ 'Content-Type': undefined}).then(function(response) 
									{	
										return response;
									});
							}
							 $scope.uploadPatientImage = function()
							    {
								 $rootScope.startLoader();
								  $scope.count = 0;
								 angular.forEach($scope.encounterDetailsList, function(value, key) 
									{
										var promise = $scope.uploadEncounterDoc(value);
										promise.then(function(response) 
												{
												  if(response.data.status == "success")
													  {
													    $scope.count++;
													    if($scope.encounterDetailsList.length==$scope.count)
														 {
														   growl.success("Document Uploaded Successfully.",
																	{
																		title : 'Success!'
																	});
														   $rootScope.stopLoader();
														 }
												      }
												  else 
													{
														growl.error('Something went wrong.',
																{
																	title : 'Error!'
																});
													}
											    });
											
									});
								
							    }
							 
							 $scope.toggleFunction=function(info,idx)
								{
									var htokenNumber= info.tokenNumber != null ? info.tokenNumber : 0;
									var URI = BASE_URL + ROOT_URL + "/api/opd/updateTokenNursing/"+info.encounterId+"/"+htokenNumber+"/"+info.orderDetailsId;
											GenericService
											.serviceAction("GET",URI,'')
											.then(function(response) {
														console.log("reee",response);
														if (response.data.status == "success") {
															//document.location.reload(true);
															 $scope.searchEncounter();
														}
													});
											
								}
							 
							 
							 $scope.speech = function (theText) {
							      window.speechSynthesis.speak(new SpeechSynthesisUtterance(theText));
							  };
							 
							 
							  
							  $scope.rescheduleAppointment = function() 
								{
									
									if($scope.reschedulledBy != '' && $scope.reschedulledBy != undefined &&  $scope.rescheduleReasonId > 0  && $scope.datepickerReschedule != '' && $scope.datepickerReschedule != undefined)
									{
									//alert(moment($scope.datepickerReschedule).format('DD-MM-YYYY'));
									var cookieObjectReschedule = {
										'slotId' : $scope.selectAppointment.slotId,
										'appointmentId' : $scope.selectAppointment.appointmentId,
										'rescheduleReasonId' : $scope.rescheduleReasonId,
										'reschedulledBy' : $scope.reschedulledBy,
										'rescheduleAppointmentDate' : moment($scope.datepickerReschedule).format('DD/MM/YYYY'),
										'patientId' : $scope.selectAppointment.patientId,
										'prefixId' : $scope.selectAppointment.prefixId,
										'firstName' : $scope.selectAppointment.firstName,
										'middleName' : $scope.selectAppointment.middleName,
										'lastName' : $scope.selectAppointment.lastName,
										'age' : $scope.selectAppointment.ageYear,
										'dob' : $scope.selectAppointment.dobForDatePicker,
										'genderId' : $scope.selectAppointment.genderId,
										'uhIdNumber' : $scope.selectAppointment.uhidNumber,
										'mobileNumber' : $scope.selectAppointment.mobileNumber,
										'email' : $scope.selectAppointment.email,
										'remark' : $scope.selectAppointment.remark,
										'bloodGroup':$scope.selectAppointment.bloodGroup,
										'allowedOverBookingSlots':$scope.selectAppointment.allowedOverBookingSlots,
										'isPaidAppointment':$scope.selectAppointment.billId !=null ? 'Y' : 'N',
										'patientCategoryId' : $scope.selectAppointment.patientCategoryId,
										'deptWiseAppointment' : $scope.selectAppointment.deptWiseAppointment,
										'isTeleconsultationAppointment':$scope.selectAppointment.isTeleconsultationAppointment
									};

								
										cookieObjectReschedule.specialityId = $scope.selectAppointment.specialityId;
										cookieObjectReschedule.modalitySubSpecialityId = $scope.selectAppointment.modalitySubSpecialityId;
										cookieObjectReschedule.doctorId = $scope.selectAppointment.doctorId;
										cookieObjectReschedule.specialityName = $scope.selectAppointment.specialityName;
										cookieObjectReschedule.doctorName = $scope.selectAppointment.docNameTemp;
										cookieObjectReschedule.selectType = "doctor";
										cookieObjectReschedule.selectedSlotId = 1;
								
									angular.element('#appRescheduleModal').modal('hide');
									//$state.go('appointmentScheduling.day');
									//return false;
									$timeout(function() {
									
											$state.go("appointmentScheduling",{
												appointmentObjectReschedule :cookieObjectReschedule
											});
										
									}, 500);
								}
									else
										{
											 growl.warning("All fields are required..!!",{title : "WARNING!"});
											 return false;
										}
								}
							  
							  
							  
							//whatsapp and skype 
								$scope.whatsAppQRCode=function(countryCode,whatsappCallNo){	
									if(countryCode == null){
									var countrycode = "91"
								}else{
									var countrycode = countryCode;
								}
								var n = countryCode.length;
								
								var str = countrycode +whatsappCallNo;
								  var res = str.split("+");
								  
                                if(res != null && res.length > 1){
                                	var n = countryCode.length;
								    var qURL="https://wa.me/"+res[1];
								     $("#qrDetailsPopup").modal(); 
								     makeCodea(qURL);
                                }else{
                                	var n = countryCode.length;
								    var qURL="https://wa.me/"+res[0];
								     $("#qrDetailsPopup").modal(); 
								     makeCodea(qURL);
                                }
								}
								
								var qrcode = new QRCode(document.getElementById("qrcode"), {
									width : 200,
									height : 200
								});

								function makeCodea(elText) {		
//								 	var qrcode = new QRCode(document.getElementById("qrcode"),{width : 200,height : 200});
									if (elText=='') {
										return;
									}
									qrcode.makeCode(elText);
								}
								
								$scope.skypeCall=function(skypeId){
									if(skypeId!='' && skypeId!=null){
										var url ="skype:"+ skypeId +"?call";
										window.open(url,"_blank");
									}else{
										growl.error("Patient has invalid skype Id",
												{
													title : 'Error!'
												});
									}
								}
								
								FroalaEditor.DefineIcon('insert', {
								    NAME: 'plus',
								    SVG_KEY: 'add'
								  });
								  FroalaEditor.RegisterCommand('insert', {
								    title: 'Insert HTML',
								    focus: true,
								    undo: true,
								    refreshAfterCallback: true,
								    callback: function() {
								      this.html.insert(`<input type="checkbox"> `);
								    }
								  });
								$scope.FroalaEditor =   new FroalaEditor('div#froala-editor', {
								   // toolbarButtons: ['insert', 'print', 'getPDF', 'html'],
									key: "AVB8B-21A3A1D3E1E2B2uigecgyqC-7frspB2twG2suc1A6B5A4B3A3C2D2G3C5B4==",
								    toolbarButtons: ['fullscreen', 'bold', 'italic', 'underline', 'strikeThrough', 'subscript', 'superscript', 'fontFamily', 'fontSize', 'color', 'emoticons', 'inlineStyle', 'paragraphStyle', 'paragraphFormat', 'align', 'formatOL', 'formatUL', 'outdent','print', 'getPDF', 'html','insert'],
								    events: {
								      'contentChanged': function() {
								        let editor = this
								        editor.html.get(true);
								       // console.log('abc',editor.html.get(true))
								        $scope.value = editor.html.get(true);
								      }
								    },
								    
								  })
							  
							  
							 $scope.getPatientDocumentList = function(encounterObj)
							 {
									$rootScope.startLoader();
									var data =
											{
											"encounterId":encounterObj.encounterId,
											"patientId":encounterObj.patientId,
											"unitId":$scope.unitId,
											"organisationId":$scope.organizationId,
											"userId":$scope.userId
									}
									var URI = BASE_URL + ROOT_URL + GET_PATIENT_DOCUMENT_ENCOUNTER;
									BillingGenericService.serviceAction(METHOD_POST, URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
											$scope.patientDocumentList = [];
											if (response.data.status == "success") 
											{
												$('#encounterUploadDocumentDetailsModal').modal('show');
												$scope.patientDocumentList = response.data.listObject;
												$rootScope.stopLoader();
											}
											else if(response.data.status == "error")
												{
												$rootScope.stopLoader();
												}
										});
								}
							 $scope.openImage = function(index)
							 { 
								 $rootScope.startLoader();
								 var imageUrl = $scope.patientDocumentList[index].documentPath;
								 var href = $rootScope.currentHrefDoccument();
								 $.fancybox.open({
				            	    width: 600, // or whatever
			                        height: 320,
				            	    href:href+imageUrl,
				            	    type: 'iframe'
				            	});
								 $rootScope.stopLoader();
							 } 
							 
							    $scope.setObject = function(item) {
									$scope.selectAppointment = [];
									$scope.selectAppointment = item;
									
									$scope.datepickerReschedule = new Date();
									$scope.reschedulledBy = "C";
									$scope.rescheduleReasonId = "";
								}
								//Function For Cancel Appointment
								$scope.cancelAppointment = function() {
									
									if($scope.cancelledBy==undefined || $scope.cancelledBy==null || $scope.cancelledBy==""){
										 growl.warning("Please select cancelled by...!!! ",{title : "WARNING!"});
										 return false;
									}
									if($scope.cancelReasonId==undefined || $scope.cancelReasonId==null || $scope.cancelReasonId==""){
										 growl.warning("Please select reason...!!! ",{title : "WARNING!"});
										 return false;
									}
									console.log("$scope.selectAppointment",$scope.selectAppointment);
									 angular.element('#appCancelModal').modal('hide');
									 $rootScope.startLoader();
									//return false;
									$scope.currentDate = new Date();
									$scope.currentDate = moment($scope.currentDate).format('DD-MM-YYYY HH:mm:ss');
									var data = {
										'slotId' : $scope.selectAppointment.slotId,
										'appointmentId' : $scope.selectAppointment.appointmentId,
										'cancelReasonId' : $scope.cancelReasonId,
										'cancelledBy' : $scope.cancelledBy,
										'updatedBy' : $scope.userId,
										'unitId' : $scope.unitId,
										'organizationId' : $scope.organizationId,
										'encounterId' :$scope.selectAppointment.encounterId
									};
									console.log(data);
									//	return false;
									var URI = BASE_URL + ROOT_URL
											+ CANCELSCHEDULEAPPOINTMENT;
									BillingGenericService
											.serviceAction(METHOD_POST, URI, data,
													NOTIFICATION_MSG_STATUS_FALSE)
											.then(
													function(response) {
														console.log(response);
														//$rootScope.startLoader();
														if (response.data.status == "success") {
															 growl.success("Appointment Cancelled Successfully.",{title : "SUCCESS!"});
															 
															 angular.element('#appCancelModal').modal('hide');
															//$scope.init();
															
															 $scope.setFirstTabLevel($scope.firstTabLevel);
														}
														else
															{
															 growl.success("Error with cancel appointment..!!",{title : "SUCCESS!"});
															angular.element('#appCancelModal').modal('hide');
															}
														angular.forEach($scope.allEncounterList,function(value,index){
															if(value.encounterId==$scope.selectAppointment.encounterId){
															    value.statusId=2;
															    value.appointmentStatusDesc="Cancelled";
															    $scope.allEncounterList.splice(index, 1);
															}
														});
														$rootScope.stopLoader();
													});
								}
								
								//Search Appointment List By Filters
	                            $scope.universalSearchEncounter = function(searchString)
								{
	                            	if(searchString!="" && searchString!=undefined && searchString!=null && searchString.length>=3){
	                            		if(searchString == 'Pending' || searchString == 'pending'){
	                            			$scope.encounterSearchObject.statusId = 1;
	                            		}
									
	                            		if(searchString == 'In Progress' || searchString == 'in progress' || searchString == 'inprogress'){
	                            			$scope.encounterSearchObject.statusId = 2;
	                            		}
									
	                            		if(searchString == 'Completed' || searchString == 'Completed'){
	                            			$scope.encounterSearchObject.statusId = 3;
	                            		}

	                            		$scope.startLoader();
	                            		$scope.commonListCount = [];
	                            		$scope.allEncounterList = [];
	                            		$scope.encounterSearchObject.searchString = searchString;
	                            		$scope.encounterSearchObject.fromDate = moment($scope.datepickerFromDate).format('YYYY-MM-DD');
	                            		$scope.encounterSearchObject.toDate = moment($scope.datepickerToDate).format('YYYY-MM-DD');
	                            		if( $scope.encounterSearchObject.statusId!=undefined &&  $scope.encounterSearchObject.statusId !=null 
	                                		&&  $scope.encounterSearchObject.statusId!="" && $scope.encounterSearchObject.statusId!=0){
	                            			$scope.encounterSearchObject.searchString = "";
	                            		}
									
	                            		$scope.disableSave = true;
									
	                            		var paramObj = {};
	                            		var headers={
											'organization_id': $scope.organizationId,
											'unit_id':$scope.unitId,
											'Content-Type':'application/json'
	                            		};
	                            		if($scope.nurseRole == 1)
										{
	                            			$scope.encounterSearchObject.specialityIdList =$scope.departmentNursingStationMapperDtoslist;
	                            			var URI = BASE_URL + ROOT_URL + "/api/opd/getUniversalSearchedListEncounterMaster";
							
	                            			GenericServiceParamHeader
	                            			.serviceAction(METHOD_POST, URI,
	                            					$scope.encounterSearchObject,
	                            					NOTIFICATION_MSG_STATUS_FALSE,paramObj,headers)
	                            					.then(
	                            							function(response) {
	                            								$scope.allEncounterList = [];
	                            								if (response.data.status == "success"){
	                            									$scope.encounterSearchObject.statusId = 0;
	                            									$scope.allEncounterList =response.data.listObject;
	                            									angular.forEach($scope.allEncounterList, function(value, key) {
	                            									    value.originalPreferredDocId=value.preferredDocId;
	                            										if(value.isPriority == null)
	                            										{
	                            											value.isPriority = 'N';
	                            											value.checkPatientSelect = true;
	                            										}
														    	
	                            									});
	                            									$scope.commonListCount = $scope.allEncounterList.length;
	                            									$scope.setPage(1,false);
	                            									$scope.stopLoader();
	                            								}
											});
										}
									}
	                            	if(searchString == ""){
	                            		$scope.searchEncounter();
	                            	}
								}
	                            
                              /*  $interval(function() {
                                    if($scope.singlePageRefresh != null && $scope.singlePageRefresh > 0)
                                            $scope.searchEncounter();
                                  }, $scope.singlePageRefresh);*/
	                            
	                        	$scope.refreshNursingWorkList = $interval(function() {
									var path = $location.path();
									if(path == '/encounterQueueManagement'){
										if($scope.singlePageRefresh != null && $scope.singlePageRefresh > 0)
										  $scope.searchEncounter();
									}
									else{
										$interval.cancel($scope.refreshNursingWorkList);
									}
									//alert('d');
						          }, $scope.singlePageRefresh);
	                        	
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