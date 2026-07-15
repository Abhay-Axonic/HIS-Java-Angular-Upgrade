/**
 * doctorDeskController.js
 */
'use strict';

/**
 * @Author By  Vivek Satle
 * @name myApp.controller:doctorDeskController
 * @description #doctorDeskController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'doctorDeskController',
				[
						'$scope',
						'$cookies',
						'$rootScope',
						'GenericService',
						'BillingGenericService',
						'$state',
						'$timeout',
						'$interval',
						'GenericServiceParamHeader',
						'GenericServiceForWithoutLogin',
						'GenericServiceFileUpload',
						'PagerService',
						'$localStorage',
						'$compile',
						'growl',
						'Pubnub',
						'$http',
						'PaginationService',
						'reportService',
						'$location',
						'$window',
						function($scope, $cookies, $rootScope, GenericService,
								BillingGenericService, $state, $timeout,$interval,GenericServiceParamHeader,GenericServiceForWithoutLogin,GenericServiceFileUpload,PagerService,$localStorage,$compile,growl,Pubnub,$http,PaginationService,reportService,$location,$window) {

							/*alert("encounterQueueManagementController");*/
							$scope.openDatePicker = function() {
								$scope.datepicker.opened = true;
							};

							$scope.datepicker = {
								opened : false
							};
							/* init() function for form object create. */
							$scope.init = function() {

								
								
								$scope.openDatePickerReschedule = function() {
									$scope.datepickerReschedule.opened = true;
								};

								$scope.datepickerReschedule = {
									opened : false
								};

								
								
								$scope.dateOptionsReschdule = {
										formatYear : 'yyyy',
										showWeeks : false,
										minDate: new Date()
									};
								
								$scope.dateOptionsPddNew={
										formatYear : 'yyyy',
        								showWeeks : false,
        								minDate:new Date(), //max 
								}
								
								var date = new Date();
								$scope.dateOptionsToFrom = {
										formatYear : 'yyyy',
										showWeeks : false,
										minDate : date.setDate((new Date()).getDate() - 90),
									};
								
								// for datePicker2
								$scope.dateOptions = {
									formatYear : 'yyyy',
									showWeeks : false,
									minDate: new Date()
								};
								$scope.openDatePickerPdd = function() {
									$scope.datepickerPdd.opened = true;
								};

								$scope.datepickerPdd = {
									opened : false
								};
								$scope.openDatepickerPddN= function(){
									$scope.datepickerPddN.opened = true;
								}
								$scope.datepickerPddN ={
										opened : false
								}
								$scope.datepicker = new Date();
								$scope.datepicker1 = new Date();
								$scope.datepickerPdd = new Date();
								$scope.datepickerPdd1 = new Date();

								var cookieObject = $cookies
										.getObject('cookieObject');
								if (cookieObject == undefined) {
									$state.go('login');
									return;
								}
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;
								$scope.cookieDoctorId = cookieObject.doctorId;
								$scope.cookieDoctorName = cookieObject.userName;
								$scope.roleId = 2;
								$scope.cookieDoctorId = cookieObject.doctorId;
								$scope.isRMO = cookieObject.isRMO;
								
								
								$('#unitIdf').val($scope.unitId);
								$('#cookieDoctorId').val($scope.cookieDoctorId);
								//alert($scope.cookieDoctorId);
								$rootScope.loginpage = true;
								$scope.saveBtnFlag = false;
								$scope.updateBtnFlag = true;
								$scope.msg="No records found...!!";
								$scope.dischargeTypeId = 0;

								/** Variables for pagination */
								$scope.offset = 0;
								$scope.noOfRecordsPerPage = 10;
								$scope.noOfRecordsPerPageIpd = 10;
								$scope.noOfRecordsPerPageDC = 10;
								$scope.noOfRecordsPerPagerRefDoc = 10;
								$scope.noOfRecordsPerPageTransferReq = 10;
								$scope.noOfRecordsPerPageAug = 10;
								$scope.totalNoOfRecords;
								$scope.searchKeyword;
								$scope.commonListCount;
								$scope.searchObj ={

								searchKeywordIP:""
								};

								
								console.log('cookieObject',cookieObject);

								
								var cookieObjectAppConfig = $cookies
										.getObject('cookieObjectAppConfig');
								if (cookieObjectAppConfig == undefined) {
									$state.go('login');
									return;
								} else {
									console.log('cookieObjectAppConfig',
											cookieObjectAppConfig);

								}
								
								$scope.isKiosk = cookieObjectAppConfig.isKiosk;
								$scope.hemodialysisSpecialityId=cookieObjectAppConfig.hemodialysisSpecialityId;
								$scope.allowOtherDoctorToConsult=cookieObjectAppConfig.allowOtherDoctorToConsult;
                                $scope.isKioskAnnouncementRequired = cookieObjectAppConfig.isKioskAnnouncementRequired;
                                $scope.dateFormat = cookieObjectAppConfig.dateFormat;
								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup
								 if(cookieObject.doctorOpScan == true){
									 $scope.mandatoryScan = 'Y';
								 }else{
									 $scope.mandatoryScan = 'N';
								 }
								 
								 if(cookieObject.doctorIpScan == true){
									 $scope.mandatoryScanIp = 'Y';
								 }else{
									 $scope.mandatoryScanIp = 'N';
								 }
								 
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

								var dataDisc = {
										organizationId : $scope.organizationId
								};
								
								$scope.isRMO=cookieObject.isRMO;
								if($scope.isRMO==undefined){
									$scope.isRMO='N';
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
		/*-------------------------------------------- for disabling easy emr icon ------------------------------------------*/			
						$scope.isEmrRequired = cookieObjectAppConfigSecond.isEmrRequired;
						$scope.singlePageRefresh = cookieObjectAppConfigSecond.singlePageRefresh;
						$scope.isAiDoctorEnabled = cookieObjectAppConfigSecond.isAiDoctorEnabled;


						
								var rmoDoctorOP=JSON.parse(localStorage.getItem('rmoMappedOPDoctorList'));
								var rmoDoctorIP=JSON.parse(localStorage.getItem('rmoMappedIPWard'));
								 rmoDoctorOP= rmoDoctorOP != undefined ? rmoDoctorOP : [];
								 rmoDoctorIP= rmoDoctorIP != undefined ? rmoDoctorIP : [];
								 
								//$scope.rmoVisitType=$cookies.getObject('rmoVisit');
								 $scope.rmoMappedOpDoctorList=[];
								var rmoMappedWard=null;
								if($scope.isRMO=='Y' && (rmoDoctorOP==null || rmoDoctorIP==null)){
									 growl.warning(
											 'RMO roster is not defined !!',
												{
													title : 'Warning!'
												});
									return false;
								}
								else{
									 $scope.rmoMappedOpDoctorList= rmoDoctorOP!=undefined ? rmoDoctorOP :[];
									rmoMappedWard= rmoDoctorIP!=undefined ? rmoDoctorIP: null;
								}

								
								var URI = BASE_URL + ROOT_URL
								+ GETACTIVEDISCHARGETYPELIST;
						GenericService
								.serviceAction("POST", URI, dataDisc)
								.then(
										function(response) {
											$scope.dischargeTypeList = [];

											if (response.data.status == "success") {
												$scope.dischargeTypeList = response.data.listObject;

											}
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
								$scope.doctorListForSwap = [];
								$scope.doctObj = {};

								var commonObjff = {
										unitId : $scope.unitId,
										organizationId : $scope.organizationId,
										createdDate : moment(new Date()).format('YYYY-MM-DD')
										
									};
								var URI = BASE_URL + ROOT_URL
										+ "/api/global/getDoctorListForSwapDoctor";
								GenericService
										.serviceAction("POST", URI, commonObjff)
										.then(
												function(response) {
													
													if (response.data.status == "success") {
														$scope.doctorListForSwap = response.data.listObject;
														angular.forEach($scope.doctorListForSwap, function(value, key){
															if(value.doctorId == cookieObject.doctorId){
																$scope.doctorLoginSpecialityId = value.specialityId;
																$scope.doctorLoginSpecialityName = value.specialityName;
																
															}
														});
														$scope.doctObj ={
																doctorId : cookieObject.doctorId,
																doctorName : cookieObject.userName
														}
														console.log("$scope.doctorListForSwap",$scope.doctorListForSwap);
													}
												});
								
								if($scope.cookieDoctorId != null)
								{
									 var commonObjDoc = {
												unitId : $scope.unitId,
												organizationId : $scope.organizationId
											};
									 
									 if($scope.isRMO=='Y'){
										 if(rmoMappedWard.length>0)
										 {
												commonObjDoc.rmoMappedWardList=rmoMappedWard;
												commonObjDoc.isRMO='Y';
										}
									 }else{
										 commonObjDoc.doctorId =$scope.cookieDoctorId;
									 }
									 
									//Get Cabin List By RMO Mapped Doctor List
									 if($scope.isRMO=='Y' && rmoDoctorOP.length>0 ){
											var rmoMappedDoctorObj={
													doctorList: $scope.rmoMappedOpDoctorList,
													unitId: $scope.unitId
											}
											var URI = BASE_URL + ROOT_URL + "/api/opd/getCabinListByRMOMappedDoctorList";
											GenericService.serviceAction("POST", URI, rmoMappedDoctorObj).then(function(response) {
														$scope.cabinListForDoctor = [];
														if (response.data.status == "success") {
															$scope.cabinListForDoctor = response.data.listObject;
															
															
															if($rootScope.doctorCabinObject == undefined)
													    	 {
													    	 
													    	 if($scope.cabinListForDoctor.length > 0)
													    		 {
													    		 	if($scope.cabinListForDoctor.length==1)
													    		 		{
													    		 			$rootScope.doctorCabinObject = $scope.cabinListForDoctor[0];
													    		 			$rootScope.cabinId = $rootScope.doctorCabinObject.cabinId;
													    		 			$scope.saveDoctorInCabin();
													    		 		}else{
													    		 			angular.element('#cabinModal').modal('show');   
													    		 		}
													    		 }
													    	 
													    	 }
															
															
														}
													});
											
											    //end cabin list
										}else{
										//get cabin list by doctor id
										var URI = BASE_URL + ROOT_URL + "/api/opd/getCabinListByDoctorId/"+$scope.cookieDoctorId+"/"+$scope.unitId;
										GenericService
										.serviceAction("GET", URI, "")
										.then(
												function(response) {
													//$scope.listOfAdmittedPatientList = [];
													$scope.cabinListForDoctor = [];

													if (response.data.status == "success") {
														$scope.cabinListForDoctor = response.data.listObject;
														
														
														if($rootScope.doctorCabinObject == undefined)
												    	 {
												    	 
												    	 if($scope.cabinListForDoctor.length > 0)
												    		 {
												    		 	if($scope.cabinListForDoctor.length==1)
												    		 		{
												    		 			$rootScope.doctorCabinObject = $scope.cabinListForDoctor[0];
												    		 			$rootScope.cabinId = $rootScope.doctorCabinObject.cabinId;
												    		 			$scope.saveDoctorInCabin();
												    		 		}else{
												    		 			angular.element('#cabinModal').modal('show');   
												    		 		}
												    		 }
												    	 
												    	 }
													}
												});
										
										   
										  }
										 //end cabin list
			        	
						}
						
								     $scope.encounterSearchObject = {
								    		 'statusId':0,
								    		 'organizationId': $scope.organizationId,
											 'unitId':$scope.unitId,
								     }
								  // console.log("$rootScope.doctorCabinObject",$rootScope.doctorCabinObject);  
								     
							}

							$scope.init();
							
							 $scope.pagination = {
					                   currentPage: 1,
					                   totalPages: 0,
					                   pageSize: '10',
					                   search: '',
					                   searchData: PaginationService.searchData,
					                   setTotalPages: PaginationService.setTotalPages,
					                   getPagesList: PaginationService.getPagesList,
					                   setPage: PaginationService.setPage,
					                   setFirstPage: PaginationService.setFirstPage,
					                   setPreviousPage: PaginationService.setPreviousPage,
					                   setNextPage: PaginationService.setNextPage,
					                   setLastPage: PaginationService.setLastPage,
					                   sortBy: PaginationService.sortBy
					                 }
							 
							// For Paginations IPD Start
							$scope.setNoOfRecordsIPD = function(noOfRecordsPerPageIpd) {
								$scope.noOfRecordsPerPageIpd = noOfRecordsPerPageIpd;
								$scope.getIPDlist();
							};
							
							$scope.getIPDlist = function() {
								
								var rmoDoctorOP=JSON.parse(localStorage.getItem('rmoMappedOPDoctorList'));
								var rmoDoctorIP=JSON.parse(localStorage.getItem('rmoMappedIPWard'));
								 rmoDoctorOP= rmoDoctorOP != undefined ? rmoDoctorOP : [];
								 rmoDoctorIP= rmoDoctorIP != undefined ? rmoDoctorIP : [];
								 
								if($scope.cookieDoctorId != null)
								{
									 var commonObjDoc = {
												unitId : $scope.unitId,
												organizationId : $scope.organizationId
											};
									 
									 if($scope.isRMO=='Y'){
										 if(rmoDoctorIP.length>0)
										 {
												commonObjDoc.rmoMappedWardList=rmoDoctorIP;
												commonObjDoc.isRMO='Y';
										}
									 }else{
										 commonObjDoc.doctorId =$scope.cookieDoctorId;
									 }
									 
								if(rmoDoctorIP.length > 0 || commonObjDoc.doctorId!=undefined )
								{
									
									commonObjDoc.visitTypeId = 2;
									commonObjDoc.offset = $scope.offset;
									commonObjDoc.noOfRecordsPerPage = $scope.noOfRecordsPerPageIpd;
									
									var URI = BASE_URL + ROOT_URL
									+ GETIPDPATIENTBYDOCTORID;
							     console.log("URI",URI);
							     console.log("commonObj",JSON.stringify(commonObjDoc));
							GenericService
									.serviceAction("POST", URI, commonObjDoc)
									.then(
											function(response) {
												//$scope.listOfAdmittedPatientList = [];
												$scope.patientBedList = [];

												if (response.data.status == "success") {
													if(response.data.listObject.length > 0)
														$scope.patientBedList = response.data.listObject;
													angular.forEach($scope.patientBedList,function(value,index)
															{
															value.pdd =moment(value.pdd,'DD/MM/YYYY').format('DD-MMM-YYYY');
															});
													$scope.ip = [];
													angular.forEach(response.data.listObject, function(value , key) 
													{
													
														if( value.visitTypeId == 2)
														{
															$scope.ip.push(value);
														}
					                                });
													$scope.pager = {};
													$scope.page;
													$scope.commonListCount = response.data.object;
													$scope.setPageIpd(1,false);
												}
											});
							        }
								}
							}
	

                           $scope.getIPDlist = function() {

								var rmoDoctorOP=JSON.parse(localStorage.getItem('rmoMappedOPDoctorList'));
								var rmoDoctorIP=JSON.parse(localStorage.getItem('rmoMappedIPWard'));
								 rmoDoctorOP= rmoDoctorOP != undefined ? rmoDoctorOP : [];
								 rmoDoctorIP= rmoDoctorIP != undefined ? rmoDoctorIP : [];

								if($scope.cookieDoctorId != null)
								{
									 var commonObjDoc = {
												unitId : $scope.unitId,
												organizationId : $scope.organizationId
											};

									 if($scope.isRMO=='Y'){
										 if(rmoDoctorIP.length>0)
										 {
												commonObjDoc.rmoMappedWardList=rmoDoctorIP;
												commonObjDoc.isRMO='Y';
										}
									 }else{
										 commonObjDoc.doctorId =$scope.cookieDoctorId;
									 }

								if(rmoDoctorIP.length > 0 || commonObjDoc.doctorId!=undefined )
								{

									commonObjDoc.visitTypeId = 2;
									commonObjDoc.offset = $scope.offset;
									commonObjDoc.noOfRecordsPerPage = $scope.noOfRecordsPerPageIpd;

									var URI = BASE_URL + ROOT_URL
									+ GETIPDPATIENTBYDOCTORID;
							     console.log("URI",URI);
							     console.log("commonObj",JSON.stringify(commonObjDoc));
							GenericService
									.serviceAction("POST", URI, commonObjDoc)
									.then(
											function(response) {
												//$scope.listOfAdmittedPatientList = [];
												$scope.patientBedList = [];

												if (response.data.status == "success") {
													if(response.data.listObject.length > 0)
														$scope.patientBedList = response.data.listObject;
													angular.forEach($scope.patientBedList,function(value,index)
															{
															value.pdd =moment(value.pdd,'DD/MM/YYYY').format('DD-MMM-YYYY');
															});
													$scope.ip = [];
													angular.forEach(response.data.listObject, function(value , key)
													{

														if( value.visitTypeId == 2)
														{
															$scope.ip.push(value);
														}
					                                });
													$scope.pager = {};
													$scope.page;
													$scope.commonListCount = response.data.object;
													$scope.setPageIpd(1,false);
												}
											});
							        }
								}
							}
							$scope.universalSerachIP = function() {

							if($scope.searchObj.searchKeywordIP =='' || $scope.searchObj.searchKeywordIP == undefined || $scope.searchObj.searchKeywordIP == null){
							$scope.getIPDlist();
							return false;
							}

                            								var rmoDoctorOP=JSON.parse(localStorage.getItem('rmoMappedOPDoctorList'));
                            								var rmoDoctorIP=JSON.parse(localStorage.getItem('rmoMappedIPWard'));
                            								 rmoDoctorOP= rmoDoctorOP != undefined ? rmoDoctorOP : [];
                            								 rmoDoctorIP= rmoDoctorIP != undefined ? rmoDoctorIP : [];

                            								if($scope.cookieDoctorId != null)
                            								{
                            									 var commonObjDoc = {
                            												unitId : $scope.unitId,
                            												organizationId : $scope.organizationId
                            											};

                            									 if($scope.isRMO=='Y'){
                            										 if(rmoDoctorIP.length>0)
                            										 {
                            												commonObjDoc.rmoMappedWardList=rmoDoctorIP;
                            												commonObjDoc.isRMO='Y';
                            										}
                            									 }else{
                            										 commonObjDoc.doctorId =$scope.cookieDoctorId;
                            									 }

                            								if(rmoDoctorIP.length > 0 || commonObjDoc.doctorId!=undefined )
                            								{

                            									commonObjDoc.visitTypeId = 2;
                            									commonObjDoc.offset = $scope.offset;
                            									commonObjDoc.noOfRecordsPerPage = $scope.noOfRecordsPerPageIpd;
                            									commonObjDoc.searchKeyword = $scope.searchObj.searchKeywordIP;

                            									var URI = BASE_URL + ROOT_URL
                            									+ "/adt/doctorDesk/ipPatientListByDocIdBySearch";
                            							     console.log("URI",URI);
                            							     console.log("commonObj",JSON.stringify(commonObjDoc));
                            							GenericService
                            									.serviceAction("POST", URI, commonObjDoc)
                            									.then(
                            											function(response) {
                            												//$scope.listOfAdmittedPatientList = [];
                            												$scope.patientBedList = [];

                            												if (response.data.status == "success") {
                            													if(response.data.listObject.length > 0)
                            														$scope.patientBedList = response.data.listObject;
                            													angular.forEach($scope.patientBedList,function(value,index)
                            															{
                            															value.pdd =moment(value.pdd,'DD/MM/YYYY').format('DD-MMM-YYYY');
                            															});
                            													$scope.ip = [];
                            													angular.forEach(response.data.listObject, function(value , key)
                            													{

                            														if( value.visitTypeId == 2)
                            														{
                            															$scope.ip.push(value);
                            														}
                            					                                });
                            													$scope.pager = {};
                            													$scope.page;
                            													$scope.commonListCount = response.data.object;
                            													$scope.setPageIpd(1,false);
                            												}
                            											});
                            							        }
                            								}
                            							}
							$scope.getIPDlistMaster = function(orgId, offset, noOfRecordsPerPage) {
								
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage: 10;
								
								var rmoDoctorOP=JSON.parse(localStorage.getItem('rmoMappedOPDoctorList'));
								var rmoDoctorIP=JSON.parse(localStorage.getItem('rmoMappedIPWard'));
								 rmoDoctorOP= rmoDoctorOP != undefined ? rmoDoctorOP : [];
								 rmoDoctorIP= rmoDoctorIP != undefined ? rmoDoctorIP : [];
								 
								if($scope.cookieDoctorId != null)
								{
									 var commonObjDoc = {
												unitId : $scope.unitId,
												organizationId : $scope.organizationId
											};
									 
									 if($scope.isRMO=='Y'){
										 if(rmoDoctorIP.length>0)
										 {
												commonObjDoc.rmoMappedWardList=rmoDoctorIP;
												commonObjDoc.isRMO='Y';
										}
									 }else{
										 commonObjDoc.doctorId =$scope.cookieDoctorId;
									 }
									 
								if(rmoDoctorIP.length > 0 || commonObjDoc.doctorId!=undefined )
								{
									
									commonObjDoc.visitTypeId = 2;
									commonObjDoc.offset = offset;
									commonObjDoc.noOfRecordsPerPage = noOfRecordsPerPage;
									
									var URI = BASE_URL + ROOT_URL
									+ GETIPDPATIENTBYDOCTORID;
							     console.log("URI",URI);
							     console.log("commonObj",JSON.stringify(commonObjDoc));
							GenericService
									.serviceAction("POST", URI, commonObjDoc)
									.then(
											function(response) {
												//$scope.listOfAdmittedPatientList = [];
												$scope.patientBedList = [];

												if (response.data.status == "success") {
													if(response.data.listObject.length > 0)
														$scope.patientBedList = response.data.listObject;
													
													$scope.ip = [];
													angular.forEach(response.data.listObject, function(value , key) 
													{
													
														if( value.visitTypeId == 2)
														{
															$scope.ip.push(value);
														}
					                                });
												}
											});
							        }
								}
							}
							
							
							$scope.setPageIpd = function(page, flag) {
								if (page < 1 || page > $scope.pager.totalPages) {
									return;
								}
								$scope.pager = PagerService.GetPager($scope.commonListCount, page,$scope.noOfRecordsPerPageIpd);
								if (flag) {
									$scope.getIPDlistMaster($scope.organizationId,$scope.pager.startIndex,$scope.pager.pageSize);
								}
							}
							
							
							//end pagination
							
							
							
							
							// For Paginations Day Care Start
							$scope.setNoOfRecordsDC = function(noOfRecordsPerPageDC) {
								$scope.noOfRecordsPerPageDC = noOfRecordsPerPageDC;
								$scope.getDClist();
							};
							
							$scope.getDClist = function() {
								
								var rmoDoctorOP=JSON.parse(localStorage.getItem('rmoMappedOPDoctorList'));
								var rmoDoctorIP=JSON.parse(localStorage.getItem('rmoMappedIPWard'));
								 rmoDoctorOP= rmoDoctorOP != undefined ? rmoDoctorOP : [];
								 rmoDoctorIP= rmoDoctorIP != undefined ? rmoDoctorIP : [];
								 
								if($scope.cookieDoctorId != null)
								{
									 var commonObjDoc = {
												unitId : $scope.unitId,
												organizationId : $scope.organizationId
											};
									 
									 if($scope.isRMO=='Y'){
										 if(rmoDoctorIP.length>0)
										 {
												commonObjDoc.rmoMappedWardList=rmoDoctorIP;
												commonObjDoc.isRMO='Y';
										}
									 }else{
										 commonObjDoc.doctorId =$scope.cookieDoctorId;
									 }
									 
								if(rmoDoctorIP.length > 0 || commonObjDoc.doctorId!=undefined )
								{
									
									commonObjDoc.visitTypeId = 3;
									commonObjDoc.offset = $scope.offset;
									commonObjDoc.noOfRecordsPerPage = $scope.noOfRecordsPerPageDC;
									
									var URI = BASE_URL + ROOT_URL
									+ GETIPDPATIENTBYDOCTORID;
							     console.log("URI",URI);
							     console.log("commonObj",JSON.stringify(commonObjDoc));
							GenericService
									.serviceAction("POST", URI, commonObjDoc)
									.then(
											function(response) {
												//$scope.listOfAdmittedPatientList = [];
												$scope.patientBedList = [];

												if (response.data.status == "success") {
													if(response.data.listObject.length > 0)
														$scope.patientBedList = response.data.listObject;
													angular.forEach($scope.patientBedList,function(value,index)
															{
															value.pdd =moment(value.pdd,'DD/MM/YYYY').format('DD-MMM-YYYY');
															});
													$scope.dayCare=[];
													angular.forEach(response.data.listObject, function(value , key) 
													{
														if( value.visitTypeId == 3)
														{
															$scope.dayCare.push(value);
														}
														
					                                });
													$scope.pager = {};
													$scope.page;
													$scope.commonListCount = response.data.object;
													$scope.setPageDC(1,false);
												}
											});
							        }
								}
							}
	

							$scope.getDClistMaster = function(orgId, offset, noOfRecordsPerPage) {
								
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage: 10;
								
								var rmoDoctorOP=JSON.parse(localStorage.getItem('rmoMappedOPDoctorList'));
								var rmoDoctorIP=JSON.parse(localStorage.getItem('rmoMappedIPWard'));
								 rmoDoctorOP= rmoDoctorOP != undefined ? rmoDoctorOP : [];
								 rmoDoctorIP= rmoDoctorIP != undefined ? rmoDoctorIP : [];
								 
								if($scope.cookieDoctorId != null)
								{
									 var commonObjDoc = {
												unitId : $scope.unitId,
												organizationId : $scope.organizationId
											};
									 
									 if($scope.isRMO=='Y'){
										 if(rmoDoctorIP.length>0)
										 {
												commonObjDoc.rmoMappedWardList=rmoDoctorIP;
												commonObjDoc.isRMO='Y';
										}
									 }else{
										 commonObjDoc.doctorId =$scope.cookieDoctorId;
									 }
									 
								if(rmoDoctorIP.length > 0 || commonObjDoc.doctorId!=undefined )
								{
									
									commonObjDoc.visitTypeId = 3;
									commonObjDoc.offset = offset;
									commonObjDoc.noOfRecordsPerPage = noOfRecordsPerPage;
									
									var URI = BASE_URL + ROOT_URL
									+ GETIPDPATIENTBYDOCTORID;
							     console.log("URI",URI);
							     console.log("commonObj",JSON.stringify(commonObjDoc));
							GenericService
									.serviceAction("POST", URI, commonObjDoc)
									.then(
											function(response) {
												//$scope.listOfAdmittedPatientList = [];
												$scope.patientBedList = [];

												if (response.data.status == "success") {
													if(response.data.listObject.length > 0)
														$scope.patientBedList = response.data.listObject;
													
													$scope.dayCare=[];
													angular.forEach(response.data.listObject, function(value , key) 
													{
														if( value.visitTypeId == 3)
														{
															$scope.dayCare.push(value);
														}
														
					                                });
												}
											});
							        }
								}
							}
							
							
							$scope.setPageDC = function(page, flag) {
								if (page < 1 || page > $scope.pager.totalPages) {
									return;
								}
								$scope.pager = PagerService.GetPager($scope.commonListCount, page,$scope.noOfRecordsPerPageDC);
								if (flag) {
									$scope.getDClistMaster($scope.organizationId,$scope.pager.startIndex,$scope.pager.pageSize);
								}
							}
							
							
							//end pagination
							
							// For Paginations referred Start
							$scope.setNoOfRecordsRefDoc = function(noOfRecordsPerPagerRefDoc) {
								$scope.noOfRecordsPerPagerRefDoc = noOfRecordsPerPagerRefDoc;
								$scope.getRefDoclist();
							};
							
							$scope.getRefDoclist = function() {
								
								 
								if($scope.cookieDoctorId != null)
								{
									var commonObjDoc = {
											unitId : $scope.unitId,
											organizationId : $scope.organizationId
										};
									
									 if($scope.isRMO=='Y'){
												commonObjDoc.doctorList= $scope.rmoMappedOpDoctorList;
												//commonObjDoc.visitTypeId=$scope.rmoVisitType;
												commonObjDoc.isRMO='Y';
									 }else{
										 commonObjDoc.refToDoctorId=$scope.cookieDoctorId;
									 }
									 
										commonObjDoc.offset = $scope.offset;
										commonObjDoc.noOfRecordsPerPage = $scope.noOfRecordsPerPagerRefDoc;
										
						        	var URI = BASE_URL + ROOT_URL + CONSULTPATIENTLISTBYDOCTOR;
									
						        	if(($scope.isRMO=='Y' && commonObjDoc.doctorList!=undefined && commonObjDoc.doctorList.length>0) || commonObjDoc.refToDoctorId != undefined ){
									GenericServiceParamHeader.serviceAction(METHOD_POST, URI,
											commonObjDoc, NOTIFICATION_MSG_STATUS_FALSE)
												.then(function(response) {
													if(response.data.status == "success"){
														$scope.consultOrdersList = response.data.listObject;
														 angular.forEach($scope.consultOrdersList,function(value,key){
							                                    value.createdDate = moment(value.createdDate,'DD/MM/YYYY').format('DD-MMM-YYYY');
							                                    });
														console.log("consult list data",$scope.consultOrdersList);
														$scope.pager = {};
														$scope.page;
														$scope.commonListCount = response.data.object;
														$scope.setPageDC(1,false);
													}
									});
						        	} 
								}
							}
	

							$scope.getRefDoclistMaster = function(orgId, offset, noOfRecordsPerPage) {
								
								 
								if($scope.cookieDoctorId != null)
								{
									var commonObjDoc = {
											unitId : $scope.unitId,
											organizationId : $scope.organizationId
										};
									
									 if($scope.isRMO=='Y'){
												commonObjDoc.doctorList= $scope.rmoMappedOpDoctorList;
												//commonObjDoc.visitTypeId=$scope.rmoVisitType;
												commonObjDoc.isRMO='Y';
									 }else{
										 commonObjDoc.refToDoctorId=$scope.cookieDoctorId;
									 }
									 
										commonObjDoc.offset = offset;
										commonObjDoc.noOfRecordsPerPage = noOfRecordsPerPage;
										
						        	var URI = BASE_URL + ROOT_URL + CONSULTPATIENTLISTBYDOCTOR;
									
						        	if(($scope.isRMO=='Y' && commonObjDoc.doctorList!=undefined && commonObjDoc.doctorList.length>0) || commonObjDoc.refToDoctorId != undefined ){
									GenericServiceParamHeader.serviceAction(METHOD_POST, URI,
											commonObjDoc, NOTIFICATION_MSG_STATUS_FALSE)
												.then(function(response) {
													if(response.data.status == "success"){
														$scope.consultOrdersList = response.data.listObject;
														console.log("consult list data",$scope.consultOrdersList);
													}
									});
						        	} 
								}
							}
							
							
							$scope.setPageRefDoc = function(page, flag) {
								if (page < 1 || page > $scope.pager.totalPages) {
									return;
								}
								$scope.pager = PagerService.GetPager($scope.commonListCount, page,$scope.noOfRecordsPerPageRefDoc);
								if (flag) {
									$scope.getRefDoclistMaster($scope.organizationId,$scope.pager.startIndex,$scope.pager.pageSize);
								}
							}
							
							
							//end pagination
							
							// For Paginations TRANSFERRED Start
							$scope.setNoOfRecordsRefDoc = function(noOfRecordsPerPageTransferReq) {
								$scope.noOfRecordsPerPageTransferReq = noOfRecordsPerPageTransferReq;
								$scope.getacceptedTransferReqList();
							};
							
							$scope.getacceptedTransferReqList = function() {
								  console.log($scope.cookieDoctorId)
								  $rootScope.startLoader();
								  var data = {
										  organizationId :$scope.organizationId ,
									      unitId:$scope.unitId,
									      doctorId:$scope.cookieDoctorId,
									      offset:$scope.offset,
									      noOfRecordsPerPage : $scope.noOfRecordsPerPageTransferReq
									   }
									console.log(data)
									var URI = BASE_URL + ROOT_URL
									+ "/adt/acceptedTransferOfCareList";					
							BillingGenericService
							.serviceAction("POST", URI, data,
									NOTIFICATION_MSG_STATUS_FALSE)
							.then(
									function(response) {
										console.log(response);
										$scope.transferReqResponseList = [];
										$scope.transferReqList = [];
										if (response.data.status == "success")
											$scope.transferReqResponseList = response.data.listObject;
										
									    console.log($scope.transferReqList)
										angular.forEach($scope.transferReqResponseList,function(value,index)
										{
					            			   $scope.notes = value.dischargeSummaryNote;
					            			   if(value.requestFrom==$scope.cookieDoctorId)
												   $scope.transferReqList.push(value);
					            		   
										});
									    $scope.showFieldList=function(id){
									    	 noteId=id;
									    angular.forEach($scope.transferReqList,function(value,index)
												{
									       if(id==value.transferOfCareId)
							            			   $scope.notes = value.dischargeSummaryNote;
							            			   console.info( $scope.notes)
							            		});
									    }
									    $scope.pager = {};
										$scope.page;
										$scope.commonListCount = response.data.object;
										$scope.setPageTransferReq(1,false);
									});
								}

							$scope.getacceptedTransferReqListMaster = function(orgId, offset, noOfRecordsPerPage) {
								  console.log($scope.cookieDoctorId)
								  $rootScope.startLoader();
								  var data = {
										  organizationId :$scope.organizationId ,
									      unitId:$scope.unitId,
									      doctorId:$scope.cookieDoctorId,
									      offset:offset,
									      noOfRecordsPerPage :noOfRecordsPerPage
									   }
									console.log(data)
									var URI = BASE_URL + ROOT_URL
									+ "/adt/acceptedTransferOfCareList";					
							BillingGenericService
							.serviceAction("POST", URI, data,
									NOTIFICATION_MSG_STATUS_FALSE)
							.then(
									function(response) {
										console.log(response);
										$scope.transferReqResponseList = [];
										$scope.transferReqList = [];
										if (response.data.status == "success")
											$scope.transferReqResponseList = response.data.listObject;
									    console.log($scope.transferReqList)
										angular.forEach($scope.transferReqResponseList,function(value,index)
										{
					            			   $scope.notes = value.dischargeSummaryNote;
					            			   if(value.requestFrom==$scope.cookieDoctorId)
												   $scope.transferReqList.push(value);
					            		   
										});
									    $scope.showFieldList=function(id){
									    	 noteId=id;
									    angular.forEach($scope.transferReqList,function(value,index)
												{
									       if(id==value.transferOfCareId)
							            			   $scope.notes = value.dischargeSummaryNote;
							            			   console.info( $scope.notes)
							            		});
									    }
									});
								}
							
							$scope.setNoOfRecordsTransferReq = function() {
								$scope.getacceptedTransferReqList();
								};
							
							$scope.setPageTransferReq = function(page, flag) {
								if (page < 1 || page > $scope.pager.totalPages) {
									return;
								}
								$scope.pager = PagerService.GetPager($scope.commonListCount, page,$scope.noOfRecordsPerPageTransferReq);
								if (flag) {
									$scope.getacceptedTransferReqListMaster($scope.organizationId,$scope.pager.startIndex,$scope.pager.pageSize);
								}
							}
							
							
							//end pagination
							
							$scope.departmentNursing = {};
							$scope.selectsetCabin = function()
							{
								if($scope.doctorCabin == undefined || $scope.doctorCabin.length == 0){
									growl.warning('Please select cabin..!!');
									return false;
								}else{
								$rootScope.doctorCabinObject = JSON.parse($scope.doctorCabin);
								$rootScope.cabinId = $rootScope.doctorCabinObject.cabinId;
								//$scope.doctorCabinObject = JSON.parse($scope.doctorCabin);
								console.log("$scope.doctorCabinObject",$rootScope.doctorCabinObject);
								$scope.saveDoctorInCabin();
								angular.element('#cabinModal').modal('hide');
								}
							}

							
							 $scope.saveDoctorInCabin=function()
								{
									
								 if($rootScope.doctorCabinObject != undefined || $rootScope.doctorCabinObject != null)
									{
									var updObj = {
											doctorId : $scope.cookieDoctorId,
											unitId:$scope.unitId,
							    			organizationId:$scope.organizationId,
							    			cabinId : $rootScope.doctorCabinObject.cabinId,
							    			createdBy :$scope.userId,
							    			updatedBy :$scope.userId,
									}
									
										var URI = BASE_URL + ROOT_URL+ "/api/global/unit/saveDoctorInCabin";

										BillingGenericService .serviceAction(METHOD_POST, URI, updObj, NOTIFICATION_MSG_STATUS_FALSE)
												.then( function(response) {
															
															if (response.data.status == "success")
																{
																
																}
																 
														});
									}
									
								}
							
							
							
							
							
							$scope.gotoCoverSheetIP = function(encounterObj)
							{
								//alert();
							if(encounterObj.statusId != '1' && encounterObj.statusId != '8' && $scope.roleId == '2' ){
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
																	"visitTypeId" :1
																	
															}
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
											"visitTypeId" :1
											
									}
									 sessionStorage.setItem('cookieEncounterObject', JSON.stringify(cookieObject));	
									console.log("EO",cookieObject);
									//return false;
									$state.go("OPDCoverSheet");
								}
								
							}
								
								
							}
							$scope.collectionObjIp = {};
							//$scope.validateUhidIp = "";
							$scope.validateUhidIpObj = {
									validateUhidIp : ""
							};
							$scope.validateIPDetails = function(obj){
							
								if($scope.mandatoryScanIp != null && $scope.mandatoryScanIp =="Y"){
									$scope.collectionObjIp = obj; 
									$scope.validateUhid = "";
									$('#validatePatientIP').modal('show');
									setTimeout(function(){
									    $('#patientValidate').focus();
									}, 500);
								}else{
//									$scope.gotoCoverSheetIP(obj);
									$scope.generateIpdCoverSheetUrl(obj);
								}
								
							}
							
							$scope.closevalidatePopIp = function(){
								$('#validatePatientIP').modal('hide');
							}
							
							$scope.validateEquealPatientIp= function(event,barcode){
								 var enterCode = 13;
								 if (!barcode && event.which === enterCode) {
								        barcode = $scope.validateUhidIpObj.validateUhidIp;
								      }
									 if(!!barcode){
										 
										 if($scope.collectionObjIp.uhidnumber.toLowerCase() == $scope.validateUhidIpObj.validateUhidIp.toLowerCase() ){
												$('#validatePatientIP').modal('hide');
												$('.modal-backdrop').remove()
		                                        $(document.body).removeClass("modal-open");
												$scope.generateIpdCoverSheetUrl($scope.collectionObjIp);
											}else{
												growl.warning('Please enter valid patient number',
														{
															title : 'Warning!'
														});
											}
										 
									 }
									
							}
							
							
							$scope.generateIpdCoverSheetUrl = function(patientDetailsObj)
							{
								if(patientDetailsObj!=null)
								  {
									patientDetailsObj.startConsultanceStartTime = moment(new Date()).format('DD-MM-YYYY HH:mm:ss'); 
									//for RMO
									if($scope.isRMO=='Y'){
										patientDetailsObj.doctorId=$scope.cookieDoctorId;
									}

//									patientDetailsObj.mappedDataCoversheetByUserss = $scope.mappedDataCoversheetByUser, 
									
									//$rootScope.getTemplateCountByAdmissionOrEncounter(patientDetailsObj.patientId,patientDetailsObj.admissionId,null);
									console.log("patientDetails",JSON.stringify(patientDetailsObj));
												$state.go('IPDCoverSheet',
													{
													patientDetailsObj :patientDetailsObj
													});
								  }
							  else
							     {
								    growl.error("Something went Wrong.",{title : 'Error!'});
							     }
							}
							
							
							$scope.generateIpdCoverSheetReferred = function (patientDetailsObjRef)
							{
								
								var patientDetailsObj = { };
								if(patientDetailsObjRef!=null)
								  {
									patientDetailsObj.startConsultanceStartTime = moment(new Date()).format('DD-MM-YYYY HH:mm:ss'); 
									patientDetailsObj.pFirstName = patientDetailsObjRef.patientName;
									patientDetailsObj.uhidnumber = patientDetailsObjRef.uhidNumber;
									patientDetailsObj.genderCode = patientDetailsObjRef.genderCode;
									patientDetailsObj.age = patientDetailsObjRef.age;
									patientDetailsObj.ageStr = patientDetailsObjRef.age;
									patientDetailsObj.dob = patientDetailsObjRef.dob;
									patientDetailsObj.admissionId = patientDetailsObjRef.admissionId;
									patientDetailsObj.patientId =patientDetailsObjRef.patientId;
									patientDetailsObj.visitTypeId = patientDetailsObjRef.visitTypeId;
									patientDetailsObj.visitTypeCode = patientDetailsObjRef.visitTypeCode;
									patientDetailsObj.specialityId = patientDetailsObjRef.refToSpecialityId;
									patientDetailsObj.doctorId = patientDetailsObjRef.refToDoctorId;
									patientDetailsObj.genderId = patientDetailsObjRef.genderId;
									patientDetailsObj.prefixId = patientDetailsObjRef.prefixId;
									patientDetailsObj.dischargeStatusId = patientDetailsObjRef.dischargeStatusId;
									patientDetailsObj.billStatusId = patientDetailsObjRef.billStatusId;
									patientDetailsObj.isFinalBill = patientDetailsObjRef.isFinalBill;
									patientDetailsObj.createdDate = patientDetailsObjRef.createdDate;
									patientDetailsObj.medicoLegalId = patientDetailsObjRef.medicoLegalId;
									patientDetailsObj.createdDate = patientDetailsObjRef.createdDate;
									patientDetailsObj.doa = patientDetailsObjRef.doa;
									patientDetailsObj.pdd = patientDetailsObjRef.pdd;
									patientDetailsObj.bedNumber = patientDetailsObjRef.bedNumber;
									patientDetailsObj.wardName = patientDetailsObjRef.wardName;
									patientDetailsObj.dFirstName = patientDetailsObjRef.refToDoctor;
									patientDetailsObj.billingBedCategoryId = patientDetailsObjRef.billingBedCategoryid;
									patientDetailsObj.patientCategoryId = patientDetailsObjRef.patientCategoryId;
									patientDetailsObj.paymentEntitlementId = patientDetailsObjRef.paymentEntitlementId;
									patientDetailsObj.unitId = patientDetailsObjRef.unitIdp;
									patientDetailsObj.organizationId = patientDetailsObjRef.organizationIdp;
									patientDetailsObj.defaultSelfTariffId = patientDetailsObjRef.defaultSelfTariffId;
									patientDetailsObj.consultDoctorId = patientDetailsObjRef.consultDoctorId;
									var age = $rootScope.getAgeEr(patientDetailsObjRef.birthDate);
									patientDetailsObj.age =age;
									patientDetailsObj.startConsultanceStartTime = moment(new Date()).format('DD-MM-YYYY HH:mm:ss'); 
									console.log("patientDetails",JSON.stringify(patientDetailsObj));
												$state.go('IPDCoverSheet',
													{
													patientDetailsObj :patientDetailsObj
													});
								  }
							  else
							     {
								    growl.error("Something went Wrong.",{title : 'Error!'});
							     }
							}
							
							//Search Appointment List By Filters
							
							
							// For Paginations Start
							$scope.setNoOfRecords = function() {
								$scope.searchEncounter();
							};

                            $scope.setNoOfRecordsAug = function() {
								$scope.getAugmentDoctorList();
							};
							
							
							$scope.searchEncounter =  function() {
								
								var data = "";
								
								$scope.encounterSearchObject.fromDate = moment($scope.datepicker).format('YYYY-MM-DD');
								$scope.encounterSearchObject.toDate = moment($scope.datepickerPdd).format('YYYY-MM-DD');
								
								console.log("FT",$scope.datepicker);
								console.log("FT",$scope.datepickerPdd);
								//return false;
								
								if($scope.cookieDoctorId != null)
								{
									 if($scope.isRMO=='Y'){
											 $scope.encounterSearchObject.doctorList = $scope.rmoMappedOpDoctorList;
											 $scope.encounterSearchObject.isRMO ='Y';
									 }else{
										 if($scope.doctObj.doctorId != undefined){
												 if($scope.doctObj.doctorId != $scope.cookieDoctorId){
													 $scope.encounterSearchObject.doctorId = $scope.doctObj.doctorId;
												 }else{
													 $scope.encounterSearchObject.doctorId=$scope.cookieDoctorId;
												 }
											}else{
												$scope.encounterSearchObject.doctorId=$scope.cookieDoctorId;
											}
										 
									 }
									 
									 $scope.encounterSearchObject.offset = $scope.offset;
									 $scope.encounterSearchObject.noOfRecordsPerPage = parseInt($scope.noOfRecordsPerPage);
									 
									
						        	if(($scope.isRMO=='Y' && $scope.encounterSearchObject.doctorList!=undefined 
						        			&& $scope.encounterSearchObject.doctorList.length>0) || $scope.encounterSearchObject.doctorId != undefined ){
							   // return false;
						       $scope.startLoader();		
							    var URI = BASE_URL + ROOT_URL
								+ GETOPPATIENTLISTBYDOCTORID;
						
								GenericServiceParamHeader
								.serviceAction(METHOD_POST, URI,
										$scope.encounterSearchObject,
										NOTIFICATION_MSG_STATUS_FALSE,"",{})
								.then(
										function(response) {
											console.log(response);
											// $rootScope.startLoader();
											$scope.allEncounterList = [];
											if (response.data.status == "success")
												{
												angular.forEach(response.data.listObject,function(value,key){
													if(
													 (value.modalityId == null || value.modalityId == '') && 
												     ((value.serviceRenderId !=null && (value.appointmentTypeId==1 || value.appointmentTypeId== 9)) 
													|| value.againstEncId > 0 
													|| value.userDefinedVisitTypeId == 6)
													){
														$scope.allEncounterList.push(value);
													}
												});
                                                $scope.allEncounterList = $scope.allEncounterList.filter(function (item, index, self) {
                                                    return index === self.findIndex(function (t) {
                                                        return t.encounterNumber === item.encounterNumber;
                                                    });
                                                });

//													$scope.allEncounterList = response.data.listObject;
//												    $scope.pagination.data=[];
												  /*  if($scope.allEncounterList.length > 0){            
								                         PaginationService.searchData($scope.allEncounterList, $scope.pagination);
								                       }*/
//												$scope.commonListCount = response.data.object;
//												$scope.setPage(1,false);
												    $scope.pager = {};
													$scope.page;
													$scope.commonListCount = response.data.resObject;
													$scope.setPage(1,false);
													console.log("data encounter list",$scope.allEncounterList);
													$scope.stopLoader();
												}
												
										});
						        }
							}
						
							}

							$scope.searchEncounteMaster = function(orgId,offset, noOfRecordsPerPage) {
								
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
								
								$scope.encounterSearchObject.fromDate = moment($scope.datepicker).format('YYYY-MM-DD');
								$scope.encounterSearchObject.toDate = moment($scope.datepickerPdd).format('YYYY-MM-DD');
								
								console.log("FT",$scope.datepicker);
								console.log("FT",$scope.datepickerPdd);
								//return false;
								
								if($scope.cookieDoctorId != null)
								{
									 if($scope.isRMO=='Y'){
											 $scope.encounterSearchObject.doctorList = $scope.rmoMappedOpDoctorList;
											 $scope.encounterSearchObject.isRMO ='Y';
									 }else{
										 if($scope.doctObj.doctorId != undefined){
												 if($scope.doctObj.doctorId != $scope.cookieDoctorId){
													 $scope.encounterSearchObject.doctorId = $scope.doctObj.doctorId;
												 }else{
													 $scope.encounterSearchObject.doctorId=$scope.cookieDoctorId;
												 }
											}else{
												$scope.encounterSearchObject.doctorId=$scope.cookieDoctorId;
											}
										 
									 }
									 
									 $scope.encounterSearchObject.offset = offset;
									 $scope.encounterSearchObject.noOfRecordsPerPage =  parseInt(noOfRecordsPerPage);
									 
									
						        	if(($scope.isRMO=='Y' && $scope.encounterSearchObject.doctorList!=undefined 
						        			&& $scope.encounterSearchObject.doctorList.length>0) || $scope.encounterSearchObject.doctorId != undefined ){
							   // return false;
							    var URI = BASE_URL + ROOT_URL
								+ GETOPPATIENTLISTBYDOCTORID;
						
								GenericServiceParamHeader
								.serviceAction(METHOD_POST, URI,
										$scope.encounterSearchObject,
										NOTIFICATION_MSG_STATUS_FALSE,"",{})
								.then(
										function(response) {
											console.log(response);
											// $rootScope.startLoader();
											$scope.allEncounterList = [];
											if (response.data.status == "success")
												{
												angular.forEach(response.data.listObject,function(value,key){
													if(
													 (value.modalityId == null || value.modalityId == '') && 
												     ((value.serviceRenderId !=null && (value.appointmentTypeId==1 || value.appointmentTypeId== 9)) 
													|| value.againstEncId > 0 
													|| value.userDefinedVisitTypeId == 6)
													){
														$scope.allEncounterList.push(value);
													}
												});
												$scope.allEncounterList = $scope.allEncounterList.filter(function (item, index, self) {
                                                    return index === self.findIndex(function (t) {
                                                        return t.encounterNumber === item.encounterNumber;
                                                    });
                                                });
//													$scope.allEncounterList = response.data.listObject;
												 /*$scope.pagination.data=[];
												    if($scope.allEncounterList.length > 0){            
								                         PaginationService.searchData($scope.allEncounterList, $scope.pagination);
								                       }
													console.log("data encounter list",$scope.allEncounterList);*/
												/*if($scope.allEncounterList.length > 0){            
							                         PaginationService.searchData($scope.allEncounterList, $scope.pagination);
							                       }*/
												/*$scope.pager = {};
												$scope.page;
												$scope.commonListCount = response.data.resObject;
												$scope.setPage(1,false);*/
												}
												
										});
						        }
							}
						
							}

							$scope.pager = {};
							$scope.page;

							$scope.setPage = function(page, flag) {
								if (page < 1 || page > $scope.pager.totalPages) {
									return;
								}
								$scope.pager = PagerService.GetPager( $scope.commonListCount, page, $scope.noOfRecordsPerPage,$scope.unitId);
								if (flag) {
									$scope.searchEncounteMaster( $scope.organizationId, $scope.pager.startIndex,$scope.pager.pageSize);
								}
							}

							$scope.searchEncounter();

							// For Paginations End
							
							$scope.clearSearchObject = function()
							{
								$scope.encounterSearchObject = {
							    		 'organizationId':$scope.organizationId,
							    		 'unitId':$scope.unitId,
							    		 'statusId':0,
							     }
								$scope.doctObj ={
										doctorId : cookieObject.doctorId,
										doctorName : cookieObject.doctorName
								}
								$scope.datepicker = new Date();
								$scope.datepickerPdd = new Date();
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
								 $rootScope.startLoaderPopup();
								var URI = BASE_URL + ROOT_URL + "/api/opd/getReferalDetailsByEncounterId";
								var data = {
										  encounterId:item.encounterId
								}
								GenericService.serviceAction("POST",URI,data).then(function(response){
									if(response.data.status == "success"){
									$scope.referalDetailsList = [];
									$scope.referalDetailsList  = response.data.object;
									$scope.referralTypeName = $scope.referalDetailsList.referralTypeName;
									$scope.referralName = $scope.referalDetailsList.referralName;
									$scope.referralRemark = $scope.referalDetailsList.referralRemark;
									$scope.otherConsultants = $scope.referalDetailsList.otherConsultants;
									 $rootScope.stopLoaderPopup();
									console.log("referalDetailsList",$scope.referalDetailsList );
									}
								});
								
								
								
							}
							
							
							$scope.updateEncounterDetails = function(encounterObj)
							{
								$rootScope.startLoader();
								//alert();
							//$rootScope.getTemplateCountByAdmissionOrEncounter(encounterObj.patientId,null,encounterObj.encounterId);
							if((encounterObj.statusId != '1' && encounterObj.statusId != '8' && $scope.roleId == '2') || (encounterObj.isTeleconsultationAppointment==1 && encounterObj.billStatusId==3) )
							{
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
											'patientId': encounterObj.patientId,
											'allowOtherDoctorsToConsult' : $scope.allowOtherDoctorToConsult
										}
										if($scope.isRMO=='Y'){
											 data.doctorId = encounterObj.doctorId;
											 data.orderDetailsId = encounterObj.orderDetailsId;
										}else if($scope.allowOtherDoctorToConsult=='Y'){
											data.otherDoctorId = $scope.cookieDoctorId;
										}else if(encounterObj.doctorId == null || encounterObj.doctorId == '' || encounterObj.doctorId == undefined){
											  data.doctorId = $scope.cookieDoctorId;
											  data.orderDetailsId = encounterObj.orderDetailsId;
										}	
										data.orderDetailsId = encounterObj.orderDetailsId;
										//$scope.cookieDoctorName
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
																	"encounterId":encounterObj.openCoversheetWithEncounterId,
																	"genderId":encounterObj.genderId,
																	"roleId" :$scope.roleId,
																	"patientId":encounterObj.patientId,
																	//"doctorId":encounterObj.doctorId,
																	"doctorId":((encounterObj.doctorId != null || encounterObj.doctorId  > 0)?encounterObj.doctorId:$scope.cookieDoctorId),
																	"specialityId":encounterObj.specialityId,
																	"paymentEntitlementTypeId":encounterObj.paymentEntitlementTypeId,
																	//"doctorName":encounterObj.doctorName,
																	"doctorName":((encounterObj.doctorId != null || encounterObj.doctorId  > 0)?encounterObj.doctorName:$scope.cookieDoctorName),
																	"prefixId":encounterObj.prefixId,
																	"genderId":encounterObj.genderId,
																	"appointmentId":encounterObj.appointmentId,
																	"visitTypeId" :1,
																	"contractId" : encounterObj.contractId,
																	"payeeId" : encounterObj.payeeId,
																	"patientCategoryId" : encounterObj.patientCategoryId,
																	"allowedOverBookingSlots" :encounterObj.allowedOverBookingSlots,
																	"billId" :encounterObj.billId,
																	"childEncounterId":encounterObj.encounterId
																	
															}
															
															
															 sessionStorage.setItem('cookieEncounterObject', JSON.stringify(cookieObject));	
															console.log("EO",cookieObject);
															//return false;
															$scope.searchEncounter();
//															$state.go("OPDCoverSheet");
															$rootScope.stopLoader();
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
											"encounterId":encounterObj.openCoversheetWithEncounterId,
											"genderId":encounterObj.genderId,
											"roleId" :$scope.roleId,
											"patientId":encounterObj.patientId,
											//"doctorId":encounterObj.doctorId,
											"doctorId":((encounterObj.doctorId != null || encounterObj.doctorId  > 0)?encounterObj.doctorId:$scope.cookieDoctorId),
											"specialityId":encounterObj.specialityId,
											"paymentEntitlementTypeId":encounterObj.paymentEntitlementTypeId,
											//"doctorName":encounterObj.doctorName,
											"doctorName":((encounterObj.doctorId != null || encounterObj.doctorId  > 0)?encounterObj.doctorName:$scope.cookieDoctorName),
											"prefixId":encounterObj.prefixId,
											"genderId":encounterObj.genderId,
											"appointmentId":encounterObj.appointmentId,
											"visitTypeId" :1,
											"contractId" : encounterObj.contractId,
											"payeeId" : encounterObj.payeeId,
											"patientCategoryId" : encounterObj.patientCategoryId,
											"allowedOverBookingSlots" :encounterObj.allowedOverBookingSlots,
											"billId" :encounterObj.billId,
											"childEncounterId":encounterObj.encounterId
									}
									 sessionStorage.setItem('cookieEncounterObject', JSON.stringify(cookieObject));	
									console.log("EO",cookieObject);
									//return false;
									$scope.searchEncounter();
//									$state.go("OPDCoverSheet");
									$rootScope.stopLoader();
								}
								
							}
								
							}
							
							$scope.getDiffTime = function(info,type){
								
								if(type == 'Y'){
									
									  var startDate= new Date(info.encounterDateWithTime); 
								         
								         var startMiliSecond = startDate.getMilliseconds();
								         
								         startDate = startDate - startMiliSecond;

								         var endDate= new Date(info.consultationStartTimeWithTime);
								         
								         var endMiliSecond= endDate.getMilliseconds();
								         
								         endDate = endDate - endMiliSecond;
								      
								         var milisecondsDiff = endDate-startDate;
								         
								        
								         // return Math.floor(milisecondsDiff/(1000*60*60)).toLocaleString(undefined, {minimumIntegerDigits: 2}) + ":" + (Math.floor(milisecondsDiff/(1000*60))%60).toLocaleString(undefined, {minimumIntegerDigits: 2})  + ":" + (Math.floor(milisecondsDiff/1000)%60).toLocaleString(undefined, {minimumIntegerDigits: 2});
								         return Math.round((milisecondsDiff/1000)/60);
								          
								}/*else{
									var startDate= new Date(info.encounterDateWithTime);
							         
							         var startMiliSecond = startDate.getMilliseconds();
							         
							         startDate = startDate - startMiliSecond;

							         var endDate= new Date();
							         
							         var endMiliSecond= endDate.getMilliseconds();
							         
							         endDate = endDate - endMiliSecond;
							      
							         var milisecondsDiff = endDate-startDate;
							         return Math.round((milisecondsDiff/1000)/60);
							        
							         // return Math.floor(milisecondsDiff/(1000*60*60)).toLocaleString(undefined, {minimumIntegerDigits: 2}) + ":" + (Math.floor(milisecondsDiff/(1000*60))%60).toLocaleString(undefined, {minimumIntegerDigits: 2})  + ":" + (Math.floor(milisecondsDiff/1000)%60).toLocaleString(undefined, {minimumIntegerDigits: 2});
								}*/
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
							
							$scope.checkWhichCoversheetToShow = 1; 
							  $scope.getCoversheetAccessMasterData = function(){
									
									var URI = BASE_URL + ROOT_URL + "/api/global/unit/getCoversheetAccessMaster";
									var data = {
											  organizationId:$scope.organizationId,
										      unitId:$scope.unitId
									}
									GenericService.serviceAction("POST",URI,data).then(function(response){
										if(response.data.status == "success"){
										$rootScope.stopLoader();					
										$scope.checkWhichCoversheetToShow = response.data.object;
										console.log("mappedDataCoversheetByUser",$scope.mappedDataCoversheetByUser);
										}
									});
									}
								$scope.getCoversheetAccessMasterData();
							
							$scope.gotoCoverSheet = function(encounterObj)
							{
								$rootScope.startLoader();
								if((encounterObj.statusId != '1' && encounterObj.statusId != '8' && $scope.roleId == '2')||(encounterObj.isTeleconsultationAppointment==1 && encounterObj.billStatusId==3) )
								{
										var cookieObject = 
										{
												"patientName" :encounterObj.patientName,
												"uhidNumber" : encounterObj.uhidNumber,
												"encounterDate" : encounterObj.encounterDate,
												"consultationStartTime" : encounterObj.consultationStartTime,
												"genderCode" : encounterObj.genderCode,
	//											"specialityName" : encounterObj.specialityName,
												"specialityName" : encounterObj.doctorLoginSpecialityName,
												"uuid" : encounterObj.uuid,
												"age":  encounterObj.age,
												"email":  encounterObj.email,
												"dob": encounterObj.dob,
												"encounterId":encounterObj.openCoversheetWithEncounterId,
												"genderId":encounterObj.genderId,
												"roleId" :$scope.roleId,
												"patientId":encounterObj.patientId,
												//"doctorId":encounterObj.doctorId,
												//"doctorId":((encounterObj.doctorId != null || encounterObj.doctorId  > 0)?encounterObj.doctorId:$scope.cookieDoctorId),
												"specialityIdForOTIMS":encounterObj.specialityId,
												"specialityId":$scope.doctorLoginSpecialityId,
												"paymentEntitlementTypeId":encounterObj.paymentEntitlementTypeId,
												//"doctorName":encounterObj.doctorName,
												"doctorName":((encounterObj.doctorId != null || encounterObj.doctorId  > 0)?encounterObj.doctorName:$scope.cookieDoctorName),
												"prefixId":encounterObj.prefixId,
												"genderId":encounterObj.genderId,
												"appointmentId":encounterObj.appointmentId,
												"visitTypeId" :1,
												"visitTypeName" :encounterObj.visitTypeName,
												"contractId" : encounterObj.contractId,
												"payeeId" : encounterObj.payeeId,
												"patientCategoryId" : encounterObj.patientCategoryId,
												"allowedOverBookingSlots" :encounterObj.allowedOverBookingSlots,
												"billId" :encounterObj.billId,
												"childEncounterId":encounterObj.encounterId,
												"videoCallUrl":encounterObj.videoCallUrl,
												"isVideoCallInit": !!encounterObj.isVideoCallInit,
												"isTeleconsultationAppointment": !!encounterObj.isTeleconsultationAppointment,

										}
										
										if(encounterObj.isTeleconsultationAppointment==1 && encounterObj.billStatusId==3)
										{
											$scope.updateEncounterDetails(encounterObj);
										}
										
										if($scope.isRMO=='Y'){
											cookieObject.doctorId=$scope.cookieDoctorId;
										}else{
											
	//											cookieObject.doctorName=((encounterObj.doctorId != null || encounterObj.doctorId  > 0)?encounterObj.doctorName:$scope.cookieDoctorName)
											cookieObject.doctorName=($scope.cookieDoctorName)
											cookieObject.doctorId=($scope.cookieDoctorId)
										}
										 sessionStorage.setItem('cookieEncounterObject', JSON.stringify(cookieObject));	
										console.log("EO",cookieObject);
										//return false;
										if($scope.checkWhichCoversheetToShow == 2){
											$state.go("OPDcoversheetNew");
										}else{
											$state.go("OPDCoverSheet");
										}
										
										$rootScope.stopLoader();
									/*}*/
									
								}
								
								
							}

							$scope.collectionObj = {};
							$scope.validateUhid = "";
							$scope.validateWorkListDetails = function(obj){
								
								if($scope.mandatoryScan != null && $scope.mandatoryScan =="Y" ){
									$scope.collectionObj = obj; 
									$scope.validateUhid = "";
									$('#validatePatientCoverSheet').modal('show');
									setTimeout(function(){
										$('input[name=firstName]').focus();
//									    $('#patientValidate1').focus();
									}, 500);
								}else{
									$scope.gotoCoverSheet(obj);
								}
								
							}
							
							$scope.closevalidatePop = function(){
								$('#validatePatientCoverSheet').modal('hide');
							}
							
							$scope.validateEquealPatient = function(event,barcode){
								 var enterCode = 13;
								 if (!barcode && event.which === enterCode) {
								        barcode = $scope.validateUhid;
								      }
									 if(!!barcode){
										 
										 if($scope.collectionObj.uhidNumber.toLowerCase() == $scope.validateUhid.toLowerCase() ){
												$('#validatePatientCoverSheet').modal('hide');
												$('.modal-backdrop').remove()
		                                        $(document.body).removeClass("modal-open");
												$scope.gotoCoverSheet($scope.collectionObj);
											}else{
												growl.warning('Please enter valid patient number',
														{
															title : 'Warning!'
														});
											}
										 
									 }
									
							}
							
							
							
							$scope.gotoEasyEMR = function (info, visitTypeId) {
								var baseUrlLocation = window.location.href;
								var temp = baseUrlLocation.split('//');
								var BASE_URL_CALL = temp[1].split('.');
								$scope.subDomain = BASE_URL_CALL[0];
								$scope.patientIdNewEmr = info.patientId;
								$scope.doctorIdNewEmr = info.doctorId;
								$scope.encounterIdNewEmr = info.encounterId;
								$scope.visitTypeIdNewEMR = visitTypeId;
								$scope.getNewEmrUrl();
							}

							$scope.gotoPMSEMR = function(info){


                          /*  "http://10.10.0.163:8090/clinivantage/hisCheckIn?hisId=199280&uname=YQS3lo9XMiDd4FwswNCgYg==&mobile=qRQlJ4OA/M+52Ryamspf+w=="*/
                                  if($scope.pmsUrl != null){

                                                        var Pms_Dom_EMR = $scope.pmsUrl;
                                                               var Pms_EMR = "hisId="+info.appointmentId+"&uname="+$scope.encryDoctorFirstName+"&mobile="+$scope.encryDoctorMob;
                                                              var url = Pms_Dom_EMR + Pms_EMR;
                                                                  window.open(url, '_blank');
                                  }else{

                                            growl.warning('PMS URL is not set in Configuration',
                                                                                {
                                                                                    title : 'Warning!'
                                                                                });
                                                                        return false;
                                  }


							}

							$scope.getNewEmrUrl = function () {
							BASE_URL=window.location.origin;
							var URI = BASE_URL + ROOT_URL;

								if($scope.subDomain == "qa" || $scope.subDomain == "219" || $scope.subDomain == "interim" || $scope.subDomain == "ociqa" || $scope.subDomain == "ocioysterpp" || $scope.subDomain == "ocismepp" ||  $scope.subDomain == "ocismetwopp" || $scope.subDomain == "qahotfix" || $scope.subDomain == "hisstaging"){
									var New_EMR = "https://hisemrpp.his.axonichealth.co.in/?";

									var url_Emr = "organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId + 
										"&patientId=" + $scope.patientIdNewEmr + "&doctorId=" + $scope.doctorIdNewEmr + "&visitTypeId=" + $scope.visitTypeIdNewEMR +
										"&encounterId=" + $scope.encounterIdNewEmr + "&userId=" + $scope.userId + "&subDomain=" +$scope.subDomain;
								}
								else if($scope.subDomain == "smepp" || $scope.subDomain == "queenspp" || $scope.subDomain == "meditestpp" || $scope.subDomain == 'nchpp' || $scope.subDomain == 'appdemo' || $scope.subDomain == "smetwopp"){
									var New_EMR = "https://hisemrpp.his.axonichealth.co.in/?";
									var url_Emr = "organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId + 
										"&patientId=" + $scope.patientIdNewEmr + "&doctorId=" + $scope.doctorIdNewEmr + "&visitTypeId=" + $scope.visitTypeIdNewEMR +
										"&encounterId=" + $scope.encounterIdNewEmr + "&userId=" + $scope.userId + "&subDomain=" +$scope.subDomain;
								}
								else{
									var New_EMR = "https://emr.his.axonichealth.co.in/?";
									var url_Emr = "organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId + 
										"&patientId=" + $scope.patientIdNewEmr + "&doctorId=" + $scope.doctorIdNewEmr + "&visitTypeId=" + $scope.visitTypeIdNewEMR +
										"&encounterId=" + $scope.encounterIdNewEmr + "&userId=" + $scope.userId + "&subDomain=" +$scope.subDomain;
								}
								var url = New_EMR + url_Emr;
								return url;
							}



                                $scope.getEncodedUrl1 = function (rootUrl) {

                                  var  encodedUrl = btoa(rootUrl);
                                    return btoa(rootUrl);
                                };



                                    // Encrypt a URL
                                   $scope.encryptUrl = function (url) {
                                      var secretKey =$rootScope.secretKeyEmr;
                                        return CryptoJS.AES.encrypt(url, secretKey).toString();
                                    };

							$scope.gotoCoverSheetAssigment = function(encounterObj)
							{
								//alert();
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
																	"doctorName":encounterObj.doctorName,
																	"prefixId":encounterObj.prefixId,
																	"genderId":encounterObj.genderId,
																	"appointmentId":encounterObj.appointmentId,
																	"visitTypeId" :1,
																	"contractId" : encounterObj.contractId,
																	"payeeId" : encounterObj.payeeId,
																	"patientCategoryId" : encounterObj.patientCategoryId
																	
															}
															 sessionStorage.setItem('cookieEncounterObject', JSON.stringify(cookieObject));	
															console.log("EO",cookieObject);
															//return false;
															$state.go("OPDCoverSheet");
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
											"doctorName":encounterObj.doctorName,
											"prefixId":encounterObj.prefixId,
											"genderId":encounterObj.genderId,
											"appointmentId":encounterObj.appointmentId,
											"visitTypeId" :1,
											"contractId" : encounterObj.contractId,
											"payeeId" : encounterObj.payeeId,
											"patientCategoryId" : encounterObj.patientCategoryId
											
									}
									 sessionStorage.setItem('cookieEncounterObject', JSON.stringify(cookieObject));	
									console.log("EO",cookieObject);
									//return false;
									$state.go("OPDCoverSheet");
								}
								
							}
								
								
							}
							
							$scope.getClassForPDD = function(item)
							{
								var pickUpDate = moment(new Date()).format('YYYY-MM-DD');
								var array = item.pdd.split('/');
								var alignFillDate = "";
								alignFillDate = array[2] + '-' + array[1] + '-'+ array[0];
								
								   if (pickUpDate > alignFillDate && item.dischargeId == null) {
									    return 'true';
									  }
								
							}
							
							$scope.callPatient = function(info,idx)
							{	
								
								
								if($scope.isRMO=='Y' && info.deptWiseAppointment=='Y' && info.doctorId==null){
									if(info.oldDocId==null){
										growl.warning('select doctor to continue ..!!',
												{
													title : 'Warning!'
												});
										return false;
									}
								}
								
								if($rootScope.doctorCabinObject != undefined || $rootScope.doctorCabinObject != null)
									{
									   var calIndex = info.queueNumber + 1;
									   
									angular.forEach($scope.allEncounterList ,function(value,index){
										if(info.doctorId == value.doctorId){
											if(value.queueNumber == info.queueNumber){
												var datamessage = {
														triggerId : DOCTOR_CHECK_IN,
														patientId : value.patientId,
														unitId : $scope.unitId,
														organizationId : $scope.organizationId,
														consultingDoctorId : info.doctorId
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
										if(value.queueNumber == calIndex){
											var datamessage = {
													triggerId : NEXT_CHECK_IN_CALL,
													patientId : value.patientId,
													unitId : $scope.unitId,
													organizationId : $scope.organizationId,
													consultingDoctorId : info.doctorId
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
									})
									
								if(info.callCount < 3)
									{	
										if(info.callCount < 2){
											if($scope.isKioskAnnouncementRequired!=null && $scope.isKioskAnnouncementRequired!=undefined &&
													$scope.isKioskAnnouncementRequired == 'Y'){
												 $scope.speech(' Token number, '+info.tokenNumber+', please visit '+ $rootScope.doctorCabinObject.cabinDesc+' , thank you .');
											}
										}
										
										if(info.callCount == 2){
											if($scope.isKioskAnnouncementRequired!=null && $scope.isKioskAnnouncementRequired!=undefined &&
													$scope.isKioskAnnouncementRequired == 'Y'){
												$scope.speech(' Token number, '+info.tokenNumber+', please visit '+ $rootScope.doctorCabinObject.cabinDesc+' , This is Your Last call, thank you .');
											}
										}
										
										info.callCount = info.callCount + 1;
									
										if($scope.isRMO=='Y'){
											if(info.deptWiseAppointment=='Y'){
												info.doctorId=info.oldDocId;
												info.doctorName = $.grep($scope.rmoMappedDocList, function (mppr) {
							                         return mppr.doctorId == info.doctorId;
							                     })[0].doctorName;
											}
										}
										
									console.log('info.callCount',info.callCount);	
									var URI = BASE_URL + ROOT_URL + "/api/opd/nursing/kiosk/callCount/"+info.encounterId+"/"+info.callCount+"/"+0;
									
									GenericService
									.serviceAction("GET",URI,'')
									.then(function(response) {
												console.log("reee",response);
												if (response.data.status == "success") {
													
													if(info.cabinId == null){
													//start for update doctorId
													var updObj = {
															encounterId : info.encounterId,
															unitId:$scope.unitId,
											    			organizationId:$scope.organizationId,
											    			cabinId : $rootScope.doctorCabinObject.cabinId,
											    			doctorDeskArrival: "N"
													}
														
													if($scope.isRMO=='Y'){
														if(info.deptWiseAppointment=='Y'){
															updObj.doctorId=info.oldDocId;
															info.doctorId=info.oldDocId;
														}else{
															updObj.doctorId=info.doctorId;
														}
														
													}else{
														updObj.doctorId =$scope.cookieDoctorId;
													}
														//console.log(data);
														//return false;
														var URI = BASE_URL + ROOT_URL
																+ UPDATEDOCTORIDFORENCOUNTER; 

														BillingGenericService
																.serviceAction(METHOD_POST,
																		URI, updObj,
																		NOTIFICATION_MSG_STATUS_FALSE)
																.then(
																		function(response) {
																			console
																					.log(response);
																			// $rootScope.startLoader();
																			if (response.data.status == "success")
																				{
																					$scope.allEncounterList[idx].cabinId = response.data.object;
																					$scope.allEncounterList[idx].doctorDeskArrival = "N";
																				}
																			
																				 
																		});
												}
														//End for update doctorId
												}
											});
									}
									
                                    var obj = {
                                            unitId : $scope.unitId,
                                            organizationId : $scope.organizationId,
                                            encounterId : info.encounterId,
                                        }
                                        var URI = BASE_URL + ROOT_URL + "/api/opd/update/doctor/latestCount";
                                        GenericService.serviceAction("POST",URI,obj).then(function(response) {
                                            if (response.data.status == "success") {
                                                
                                                
                                            }
                                        });
									
									
									
							}
								else
									{
									   angular.element('#cabinModal').modal('show'); 
									}
								
							}
							
							$scope.checkDatePdd = function(){
								var new_date =moment(new Date()).format('YYYY-MM-DD');
								var dtPickerformattedDt = moment($scope.datepicker).format('YYYY-MM-DD');
								 if(!angular.isDate($scope.datepicker)){
									 growl.warning("Pdd is invalid",{
											title : WARNING_MSG
										});
									 $scope.datepicker = new Date();
										return false;
								 }else if(new_date > dtPickerformattedDt)
									 {
									 growl
										.warning(
												"PDD should be equal or greater then today's date",
												{
													title : 'Warning!'
												});
									 $scope.datepicker = new Date();
										return false;
									 }
							}
							
							$scope.checkDateExtend = function(){
								var new_date = new Date();
								 if(!angular.isDate($scope.ipExtendedPddObj.datepickerPddN)){
									 growl.warning("Pdd is invalid",{
											title : WARNING_MSG
										});
									 $scope.ipExtendedPddObj.datepickerPddN = new Date();
										return false;
								 }else if(new_date > $scope.ipExtendedPddObj.datepickerPddN)
									 {
									 growl
										.warning(
												"PDD should be equal or greater then today's date",
												{
													title : 'Warning!'
												});
									 $scope.ipExtendedPddObj.datepickerPddN = new Date();
										return false;
									 }
							}
							
							 $scope.markArrival=function(encounterObj,idx)
								{
									
								 if($rootScope.doctorCabinObject != undefined || $rootScope.doctorCabinObject != null)
									{
								//start for update doctorId
									var updObj = {
											encounterId : encounterObj.encounterId,
											//doctorId : $scope.cookieDoctorId,
											unitId:$scope.unitId,
							    			organizationId:$scope.organizationId,
							    			cabinId : $rootScope.doctorCabinObject.cabinId,
							    			doctorDeskArrival: "Y",
							    			orderDetailId:encounterObj.orderDetailsId
									}
									if($scope.isRMO=='Y'){
											updObj.doctorId=encounterObj.doctorId;
									}else if (encounterObj.doctorId ==null ||encounterObj.doctorId ==undefined || encounterObj.doctorId ==""){
										updObj.doctorId =$scope.cookieDoctorId;
									}else{
										updObj.doctorId =encounterObj.doctorId;
									}
																							
										//console.log(data);
										//return false;
										var URI = BASE_URL + ROOT_URL
												+ UPDATEDOCTORIDFORENCOUNTER;

										BillingGenericService
												.serviceAction(METHOD_POST,
														URI, updObj,
														NOTIFICATION_MSG_STATUS_FALSE)
												.then(
														function(response) {
															console
																	.log(response);
															// $rootScope.startLoader();
															if (response.data.status == "success")
																{
																	/*$scope.allEncounterList[idx].cabinId = response.data.object;
																	$scope.allEncounterList[idx].doctorDeskArrival = "Y";
																	$scope.allEncounterList[idx].serviceRenderId =1;*/
																var i = 0;
																angular.forEach($scope.allEncounterList,function(value,index){
																	
																	if(value.appointmentId==encounterObj.appointmentId){
																		$scope.allEncounterList[i].cabinId = response.data.object;
																		$scope.allEncounterList[i].doctorDeskArrival = "Y";
																		$scope.allEncounterList[i].serviceRenderId =1;
																		$scope.allEncounterList[i].consultationStartTimeWithTime =moment(new Date()).format('YYYY-MM-DD HH:mm:ss');
																		$scope.getDiffTime($scope.allEncounterList[i],'Y');
																	}
																	i++;
																});
																
																}
															
																 
														});
										//End for update doctorId
									}
									
								}
							
							
							
							/**========================DOCUMENT UPLOAD =========================================*/
							
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
								  admissionId:"",
								  visitTypeId:2,
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
								
								$scope.encounterDetailsList=[];
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
								$scope.getPatientDocumentList($scope.encounterDetails);
							}
							
							$scope.addImageDetails = function(indx)
							{ 
								var encounterDetails = angular.copy($scope.encounterDetails);
								
								if($scope.encounterDetailsList[indx].file.name == undefined){
									
									growl.warning('Please select file ..!!',
											{
												title : 'Warning!'
											});
									
								}else if($scope.encounterDetailsList[indx].documentTypeId == ""){
									
									growl.warning('Please select document type ..!!',
											{
												title : 'Warning!'
											});
								}else{
									
									$scope.encounterDetailsList.push(encounterDetails);
								}
								
							};

						  $scope.removeImageDetails = function(deleteIndex)
							{ 
							   $scope.encounterDetailsList.splice(deleteIndex, 1);
						    }		
							
							
							$scope.uploadEncounterDoc = function(Obj)
							{
								 var fd = new FormData();
							        fd.append('encounterId', Obj.encounterId);
							        fd.append('visitTypeId', 1);
							        fd.append('admissionId', 0);
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
							 $scope.clearPatientDocImage = function(){
								
								 $scope.encounterDetailsList = [];
								   $scope.encounterDetailsListfinal = [];
//								   $scope.docObj.file="";
							 }
							 $scope.uploadPatientImage = function()
							    {
								 $rootScope.startLoader();
								  $scope.count = 0;
								  $scope.encounterDetailsListfinal = [];
								  angular.forEach($scope.encounterDetailsList, function(value, key) 
											{
									  
									  if(value.file.name != undefined){
										
											  if(value.documentTypeId != ""){
													
												  $scope.encounterDetailsListfinal.push(value);
												  
												}
											}
										});
								  
								  if($scope.encounterDetailsListfinal.length>0){
										
									  angular.forEach($scope.encounterDetailsListfinal, function(value, key) 
												{
													var promise = $scope.uploadEncounterDoc(value);
													promise.then(function(response) 
															{
															  if(response.data.status == "success")
																  {
																    $scope.count++;
																    if($scope.encounterDetailsListfinal.length==$scope.count)
																	 {
																	   growl.success("Document Uploaded Successfully.",
																				{
																					title : 'Success!'
																				});
																	   $scope.encounterDetailsList = [];
																	   $scope.encounterDetailsListfinal = [];
																	   $rootScope.stopLoader();
																	 }
															      }
															  else 
																{
																  $rootScope.stopLoader();
																	growl.error('Something went wrong.',
																			{
																				title : 'Error!'
																			});
																}
															  $rootScope.stopLoader();
														    });
														
												});
									  
									}else{
										//$scope.encounterDetails.file={};
										angular.element("input[type='file']").val(null);
										 $rootScope.stopLoader();
										growl.warning('Please select atleast one document..!!',
												{
													title : 'Warning!'
												});
									}
								  $rootScope.stopLoader();
								
							    }

							 $scope.dueForDischarge = function(item)
								{
									$scope.note="";
									$scope.itemForDischargePatient = item;
									$scope.noOfHours = item.noOfHours;
									
									var data = {
											admissionId: item.admissionId,
									};
									
									var URI = BASE_URL+ ROOT_URL+ "/api/opd/getPatientReturnDetails";
									GenericService.serviceAction("POST", URI,data).then(
											function(response) {

												if (response.data.status == "success") 
												{
													if(response.data.listObject == null || response.data.listObject.length == 0){
														angular.element('#dueForDischarge').modal('show');
													}else{
														growl
														.warning(
																'Return Medication not accepted',
																{
																	title : 'Warning!'
																});
													}
													
													$rootScope.stopLoader();
													
												} else if(response.data.status == "error")
												{
													
													if(response.data.message!=null){
														growl.error(response.data.message,{title : 'Error!'});
													}else{
													 growl.error('Something went wrongs...!!',{title : 'Error!'});
													}
//													
												}
											});
							
									
								}
							 
							 $scope.obh = {};
							 $scope.obh.isSendToMortury = 'N';
							 $scope.setMortury = function(){
								 if($scope.dischargeTypeId  != '3'){
									 $scope.obh.isSendToMortury = 'N'; 
								 }
								
							 }
							 
							 
							   $scope.updatePatientDetailsInSQL = function(){
                              	 $rootScope.startLoader();
                              	  var data ={}; 
                              	   data.uhidnumber = $scope.itemForDischargePatient.uhidnumber;
                              	   data.unitId =$scope.unitId;
                              	   data.organizationId =$scope.organizationId;
                              	   data.updatedBy = $scope.userId;
                              	   data.createdBy = $scope.userId;
                              	
                              	   
                                   var URI = BASE_URL + ROOT_URL+"/api/global/updatePatientAsDeath";
                                   BillingGenericService
                                           .serviceAction(METHOD_POST,URI, data)
                                           .then(
                                                   function(response) {
                                                       if (response.data.status == "success"){
                                                      	 growl.success("Updated successfully",
           														{
           															title : 'Success!'
           														});
                                                           $rootScope.stopLoader();
                                                       }else{
                                                           $rootScope.stopLoader();
                                                       }
                                                   });
                                   }
							   
								$scope.updateExtendPdd = function()
								{
									
									if($scope.dischargeTypeId == 0 || $scope.dischargeTypeId == '')
									{
									growl
									.warning(
											'Please select discharge reason..!!',
											{
												title : 'Warning!'
											});
									return false;
									
									}
									else if($scope.reasonId == 0 || $scope.reasonId == '')
									{
									growl
									.warning(
											'Please select reason..!!',
											{
												title : 'Warning!'
											});
									return false;
									
									}
								//else if($scope.datepicker.opened == false)
									else if($scope.datepicker == undefined || $scope.datepicker == null )
									{
										growl
										.warning(
												'Please select future date..!!',
												{
													title : 'Warning!'
												});
										return false;
									}
								else{
									
									$rootScope.startLoader();
									
									$("#updateExtendPddId").removeClass("enabled");
									$("#updateExtendPddId").addClass("disabled");
									$("#"+$scope.itemForDischargePatient.admissionId+"").removeClass("enabled");
									$("#"+$scope.itemForDischargePatient.admissionId+"").addClass("disabled");
									$scope.currentDate = new Date();
									$scope.currentDate = moment($scope.currentDate)
											.format('DD-MM-YYYY HH:mm:ss');
									
									$scope.disDt = moment($scope.datepicker).format('DD-MM-YYYY HH:mm:ss');

									var data = {
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
										reasonId : $scope.reasonId,
										admissionId : $scope.itemForDischargePatient.admissionId,
										updatedBy : $scope.userId,
										updatedDate : $scope.currentDate,
										pdd :  $scope.disDt,
										noteCancel : $scope.note,
										dischargeTypeId : $scope.dischargeTypeId,
										doctorId :  $scope.itemForDischargePatient.doctorId,
										status : 'A',
										destinationId :$scope.itemForDischargePatient.bedId,
										dischargeDate : $scope.disDt,
										dischargeStatusId: 1,
										visitTypeId : $scope.itemForDischargePatient.visitTypeId,
										createdBy : $scope.userId,
										createdDate : $scope.currentDate,
										patientId: $scope.itemForDischargePatient.patientId,
										isSendToMortury : $scope.obh.isSendToMortury ,

										
									};
									
									console.log(data);
//									return false;
									var URI = BASE_URL + ROOT_URL
											+ MARKDISCHARGEFROMCASESHEET;
									
									GenericService
											.serviceAction("POST", URI, data)
											.then(
													function(response) {

														if (response.data.status == "success") {
															$rootScope.stopLoader();
															
															growl.success(
																			'Discharge Request Raised Successfully...!!',
																			{
																				title : 'Success!'
																			});
															angular.element('#dueForDischarge').modal('hide');
															
															if($scope.dischargeTypeId == "3"){
																$scope.updatePatientDetailsInSQL();
															}
															
															 $scope.init();
															 if($scope.itemForDischargePatient.visitTypeId =='2'){
																 $scope.getIPDlist(); 
															 }
															 if($scope.itemForDischargePatient.visitTypeId =='3'){
															 $scope.getDClist();
															 }
														} else {
															$rootScope.stopLoader();
															growl.error(
																			'Something went wrong...!!',
																			{
																				title : 'Error!'
																			});
														}
														$("#updateExtendPddId").removeClass("disabled");
														$("#updateExtendPddId").addClass("enabled");
														$("#"+$scope.itemForDischargePatient.admissionId+"").removeClass("disabled");
														$("#"+$scope.itemForDischargePatient.admissionId+"").addClass("enabled");
													});
								}
								}
								
								 $scope.dueForDischargeNew = function(item)
									{
									 
									 $scope.noten="";
										$scope.itemForDischargePatientNew = item;
										$scope.noOfHours = item.noOfHours;
										 $scope.reasonIdN =0;
//										
									}
//								 $scope.datepickerPddN=new Date();
								
								 $scope.ipExtendedPddObj={
										 datepickerPddN:new Date(),
										 noten:'',
										reasonIdN:''
								 }
							 $scope.updateExtendPddNew = function()
								{
									
									if( $scope.ipExtendedPddObj.reasonIdN == 0 ||  $scope.ipExtendedPddObj.reasonIdN == '')
									{
									growl
									.warning(
											'Please select reason..!!',
											{
												title : 'Warning!'
											});
									return false;
									
									}
								else if( $scope.ipExtendedPddObj.datepickerPddN == undefined ||  $scope.ipExtendedPddObj.datepickerPddN == null ||  $scope.ipExtendedPddObj.datepickerPddN == '')
									{
										growl
										.warning(
												'Please select future date..!!',
												{
													title : 'Warning!'
												});
										return false;
									}
								else{
									$rootScope.startLoaderPopup();
									$scope.btnDisableEnable = true;
									$scope.currentDate = new Date();
									$scope.currentDate = moment($scope.currentDate)
											.format('DD-MM-YYYY HH:mm:ss');

									var data = {
											organizationId : $scope.organizationId,
											unitId : $scope.unitId,
											reasonId : $scope.ipExtendedPddObj.reasonIdN,
											admissionId : $scope.itemForDischargePatientNew.admissionId,
											updatedBy : $scope.userId,
											updatedDate : $scope.currentDate,
											pdd : moment($scope.ipExtendedPddObj.datepickerPddN).format('DD-MM-YYYY HH:mm:ss'),
											noteCancel : $scope.ipExtendedPddObj.noten
										};
									console.log(data);
									//return false;
									var URI = BASE_URL + ROOT_URL
											+ UPDATEDISCHARGEPDD;
									GenericService
											.serviceAction("POST", URI, data)
											.then(
													function(response) {

														if (response.data.status == "success") {
															$rootScope.stopLoaderPopup();
															growl
																	.success(
																			'PDD extended Successfully...!!',
																			{
																				title : 'Success!'
																			});
//															angular.element('#dueForDischarge').modal('hide');
															 $scope.ipExtendedPddObj={};
															$scope.init();
															
														} else {
															$rootScope.stopLoaderPopup();
															growl
																	.error(
																			'Something went wrong...!!',
																			{
																				title : 'Error!'
																			});
														}
													});
								  }
								}
								 $scope.speech = function (theText) {
								      window.speechSynthesis.speak(new SpeechSynthesisUtterance(theText));
								  };
								 
								  var noteId='';
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
												$scope.patientDocumentList = response.data.listObject;
												console.log("upload doccument",$scope.patientDocumentList);
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
							 
							 
							 $scope.getEncounterData = function(pid) {
									try {
										
										if(pid!=null)
											{
											  $state.go('encounterData',
													{
												      pid : pid
													});
											}
									} catch (e) {
										console.log(e.message)
									}

								}
							 
							 
							 $scope.firstTabLevel = 1;
								
								
								$scope.setFirstTabLevel = function(newTab) {
									$scope.firstTabLevel = newTab;
									console.log("firsrt tab="+newTab);
									
									$scope.noOfRecordsPerPageIpd = 10;
									
									if (newTab == '5') {
										
										 $scope.getacceptedTransferReqList();
									}
									if (newTab == '6') {
										 $scope.getHemodialysisByDoctorList();
										
									}
									
								};

								$scope.isSetFirstTabLevel = function(tabNum) {
									return $scope.firstTabLevel === tabNum;
									
								};
								
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
								
								  $scope.getHemodialysisByDoctorList = function() {
									  console.log($scope.cookieDoctorId)
									  $rootScope.startLoader();
									  var data = {
											  organizationId :$scope.organizationId ,
										      unitId:$scope.unitId,
										      doctorId:$scope.cookieDoctorId
										    
										   }
										console.log(data)
										var URI = BASE_URL + ROOT_URL
										+ "/adt/doctorDesk/getHemodialysisByDoctorList";					
								BillingGenericService
								.serviceAction("POST", URI, data,
										NOTIFICATION_MSG_STATUS_FALSE)
								.then(
										function(response) {
											console.log(response);
											$scope.transferReqResponseList = [];
											$scope.hemodialysisList = [];
											if (response.data.status == "success")
												$scope.hemodialysisList = response.data.listObject;
										  console.log($scope.hemodialysisList)
										});
									}
							
								
								  $scope.saveNote=function(){
									
									  var data = {
										  organizationId :$scope.organizationId ,
									      unitId:$scope.unitId,
									      transferOfCareId:noteId,
									      note:$scope.notes
									   }
									if($scope.notes == null || $scope.notes=='' )
									{
										growl
										.warning("Nothing to update...!!");
										return false; 
									}
									var URI = BASE_URL + ROOT_URL
									+ "/adt/updateclinicatnotes";					
							BillingGenericService
							.serviceAction("POST", URI, data,
									NOTIFICATION_MSG_STATUS_FALSE)
							.then(
									function(response) {
										console.log(response);
										$('#fieldDetailsModal').modal('hide');
										if (response.data.status == "success"){
											growl.success(
													"Clinical Notes Update successfully",
													{
														title : 'Success!'
													});
											
							        	}
										else {
									growl.error( response.data.message,
													{
														title : 'Error!'
													});
								}
										$scope.getacceptedTransferReqList();
									});
									  
								  }
						
								  
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
											'bloodGroup' : $scope.selectAppointment.bloodGroup,
											'remark' : $scope.selectAppointment.remark,
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
											cookieObjectReschedule.doctorName = $scope.selectAppointment.doctorName;
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
								  
								  
								  
								  $scope.tempStoreId=function(id){
									  $scope.consultDoctorId=id
								  }
								 $scope.removeConsultDoctorId=function(){
									 $scope.consultDoctorId="";
								 }
								 
								/* $scope.getSpecialityLogDoctor = function (){
									
									  var data = {
											  organizationId :$scope.organizationId ,
										      unitId:$scope.unitId,
										      doctorId:$scope.cookieDoctorId
										    
										   }
										console.log(data)
										var URI = BASE_URL + ROOT_URL
										+ "/adt/doctorDesk/getLoginDoctorSpecialityId";					
									  GenericService.serviceAction("POST",URI,data)
									  .then(function(response){
											console.log(response);
											$scope.speciality = "";
											if (response.data.status == "success")
												$scope.speciality = response.data.object
										  console.log($scope.speciality)
										});
									}
								 
								 $scope.getSpecialityLogDoctor();*/
								 
									$scope.getRemovedReffered = function(){
										
										try{
											var date = new Date();
											$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
											
											var data={
											"consultDoctorId": $scope.consultDoctorId,
											"referredStatus":'Reject',
											"updatedBy":$scope.userId,
										     "updatedDate":$scope.dt,
											}
											
											$rootScope.startLoader();
											var URI = BASE_URL + ROOT_URL + "/api/opd/updateReferredStatus";
											console.log("update status",data); //return false;
											GenericService.serviceAction("POST",URI,data).then(function(response){
												if (response.data.status == 'success')
												{
													$rootScope.stopLoader();
													growl.success('Removed Successfully',
															{
																title : 'Success!'
															});
													$scope.init();
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
											console.log("Exception",e);
										}
										}
								
									//Appointment cancellation functionality
									 $scope.setObject = function(item) {
											$scope.selectAppointment = [];
											$scope.selectAppointment = item;
											
											$scope.datepickerReschedule = new Date($rootScope.getFormatedDateeShase(item.appointmentDate));
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
										//Tranferred 
										$scope.displayAcceptanceNote=function(remark)
										{
											$scope.remarkDoctor = remark;
											
										}
										$scope.rmoMappedDocList=[];
										$scope.getRmoDoctorList=function(){
											if($scope.isRMO=='Y' && $scope.rmoMappedOpDoctorList.length>0){
											var data = {
													'doctorIdList' : $scope.rmoMappedOpDoctorList,
													'unitId' : $scope.unitId,
													'organizationId' : $scope.organizationId
												};
											var URIFORRMO=BASE_URL + ROOT_URL+ "/api/unit/clinical/getListOfDoctorMappedToRmo";
											GenericService.serviceAction("POST", URIFORRMO, data).then(function(response) {
												if (response.data.status == "success") {
													$scope.rmoMappedDocList = response.data.listObject;
												}
											});
										 }
										}
										
										$scope.getRmoDoctorList();
										
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
//										 	var qrcode = new QRCode(document.getElementById("qrcode"),{width : 200,height : 200});
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

										
										$scope.universalSerachEncounter = function(searchString){
											if(searchString!="" && searchString!=undefined && searchString!=null && searchString.length>=3){
												$rootScope.startLoader();
												if(searchString == 'In Complete' || searchString == 'In Complete' || searchString == 'incomplete'){
													$scope.encounterSearchObject.statusId = 1;
												}
											
												if(searchString == 'In Progress' || searchString == 'in progress' || searchString == 'inprogress'){
													$scope.encounterSearchObject.statusId = 2;
												}
											
												if(searchString == 'Completed' || searchString == 'Completed'){
													$scope.encounterSearchObject.statusId = 3;
												}
											
												$scope.encounterSearchObject.fromDate = moment($scope.datepicker).format('YYYY-MM-DD');
												$scope.encounterSearchObject.toDate = moment($scope.datepickerPdd).format('YYYY-MM-DD');
												$scope.encounterSearchObject.searchString = searchString;
												if( $scope.encounterSearchObject.statusId!=undefined &&  $scope.encounterSearchObject.statusId !=null 
			                                		&&  $scope.encounterSearchObject.statusId!="" && $scope.encounterSearchObject.statusId!=0){
													$scope.encounterSearchObject.searchString = "";
												}
												if($scope.cookieDoctorId != null)
												{
													if($scope.isRMO=='Y'){
														 $scope.encounterSearchObject.doctorList = $scope.rmoMappedOpDoctorList;
														 $scope.encounterSearchObject.isRMO ='Y';
													}else{
													 if($scope.doctObj.doctorId != undefined){
															 if($scope.doctObj.doctorId != $scope.cookieDoctorId){
																 $scope.encounterSearchObject.doctorId = $scope.doctObj.doctorId;
															 }else{
																 $scope.encounterSearchObject.doctorId=$scope.cookieDoctorId;
															 }
														}else{
															$scope.encounterSearchObject.doctorId=$scope.cookieDoctorId;
														}
													 
													}
												
													if($scope.isRMO=='Y' || $scope.encounterSearchObject.doctorId != undefined ){
										   
									        		var URI = BASE_URL + ROOT_URL+ "/api/opd/doctorDesk/universalSearchOpPatientListByDocId";
									
									        		GenericService.serviceAction(METHOD_POST, URI,$scope.encounterSearchObject)
									        		.then(function(response) {
															$scope.allEncounterList = [];
															if (response.data.status == "success")
															{ 	 $rootScope.stopLoader();
																$scope.encounterSearchObject.statusId = 0;
																$scope.allEncounterList =response.data.listObject;
															}
														 $rootScope.stopLoader();
													});
													}
												}
											}
											
											if(searchString == ""){
												$scope.searchEncounter();
			                            	}
									}
										
										$scope.printCaseSheet=function(info){

											  var data =  {
											                 "appName": "HIS",
											                 "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
											                 "reportDesignFilePath": "NewCoverSheet.rpt",
											                 "reportFormat": "PDF",
											                 "exportFileName": "Coversheet",
											                 "reportParams": [
											                	 {
													                  "paramName": "Patientid",
													                  "paramType": "Integer",
													                  "paramValue": info.patientId
												                },
												                {
													                  "paramName": "Unitid",
													                  "paramType": "Integer",
													                  "paramValue": $scope.unitId
												                },
												                {
														             "paramName": "Encounter_id",
														             "paramType": "Integer",
														             "paramValue": info.encounterId
															      }
									                 ]
									      };
											    reportService.generate(REPORT_API_URI1,data);
											}
										
										/*$interval(function() {
											if($scope.singlePageRefresh != null && $scope.singlePageRefresh > 0)
													$scope.searchEncounter();
								          }, $scope.singlePageRefresh);*/
										
										
										$scope.refreshDoctorDesk = $interval(function() {
											var path = $location.path();
											if(path == '/doctorDesk'){
												if($scope.singlePageRefresh != null && $scope.singlePageRefresh > 0)
												  $scope.searchEncounter();
											}
											else{
												$interval.cancel($scope.refreshDoctorDesk);
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
								     	
									            
									         	$scope.firstImgaa;
					                            $scope.imageBackground = function(){
					                                            var URI = BASE_URL + ROOT_URL + "/api/unit/applicationConfiguration/screen-images/"+$scope.organizationId+"/2"
					                                                GenericServiceForWithoutLogin.serviceAction("GET", URI).then(function(response) {
					                                                if(response.data.object != null){
					                                                $scope.firstImgaa=response.data.object.imageURL;
					                                                console.log($scope.firstImgaa);
					                                                }
					                                                });
					                                            }
					                                         $scope.imageBackground();
					                                         
					                         				$scope.getMessageNotification = function() {
																	var data =
																	{
																	"unitId":$scope.unitId,
																	"doctorId":$scope.cookieDoctorId
																	}
																	
																	
																var URI = BASE_URL + ROOT_URL + "/api/getVideoataForNotification";
																BillingGenericService.serviceAction(METHOD_POST, URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
																		$scope.patientDocumentListTest = [];
																		if (response.data.status == "success") 
																		{
																			$scope.getMessageNotificationTest = response.data.listObject;
																		}
																		else if(response.data.status == "error")
																			{
																			$rootScope.stopLoader();
																			}
																	});
														
													          };  
													          $scope.getMessageNotification();
													          
													          
													          $rootScope.joinByDoctorByNotification = function(appointmentIdMyLife,userChatDetailsId) {
														      		$rootScope.twilioAppoId = appointmentIdMyLife;
														      		$('#twilioappointmentId').val(appointmentIdMyLife);
														      		
														      		$('#HeaderTwilioVideoClick').trigger('click');
														    		$('#videoTwillio').modal();
														    		
														      		var data =
																	{
																	"userChatDetailsId":userChatDetailsId
																	}
																	
																	var URI = BASE_URL + ROOT_URL + "/api/changeStatusofuserChatDetailsId";
																	BillingGenericService.serviceAction(METHOD_POST, URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
																			$scope.patientDocumentListTest = [];
																			if (response.data.status == "success") 
																			{
																			}
																			else if(response.data.status == "error")
																				{
																				$rootScope.stopLoader();
																				}
																		});
															
														             };  
														             
													      	$scope.joinByDoctor = function(info) {
													      		$rootScope.twilioAppoId = info.appointmentIdMyLife;
													      		$('#twilioappointmentId').val(info.appointmentIdMyLife);
													      		var data =
																{
																"userChatDetailsId":info.userChatDetailsId
																}
																
																var URI = BASE_URL + ROOT_URL + "/api/changeStatusofuserChatDetailsId";
																BillingGenericService.serviceAction(METHOD_POST, URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
																		$scope.patientDocumentListTest = [];
																		if (response.data.status == "success") 
																		{
																		}
																		else if(response.data.status == "error")
																			{
																			$rootScope.stopLoader();
																			}
																	});
														
													             };  
													             
													             $scope.joinByDoctorNoti = function(flag) {
													            	 
															      		$scope.userChatDetailsId = $('#userChatDetailsId').val(); 
															      		$scope.twilioAppoId = $('#twilioappointmentId').val(); 
															      		$('#contextPath').val('http://localhost:8078/HIS-WAR');

															      		var data =
																		{
																		"userChatDetailsId":$scope.userChatDetailsId
																		}
																		
																		var URI = BASE_URL + ROOT_URL + "/api/changeStatusofuserChatDetailsId";
																		BillingGenericService.serviceAction(METHOD_POST, URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
																				$scope.patientDocumentListTest = [];
																				if (response.data.status == "success") {
                                                                                     if(flag){
                                                                                          $('#vedioCallAlert').modal('hide');
                                                                                          var obj = null;
                                                                                          var appoId = parseInt($scope.twilioAppoId, 10);
                                                                                          angular.forEach($scope.allEncounterList, function (item) {
                                                                                              if (item.appointmentId === appoId) {
                                                                                                  obj = item;
                                                                                              }
                                                                                          });
                                                                                          if (obj) {
                                                                                              $scope.showTwilioNotification();
                                                                                              obj.isVideoCallInit = true;
                                                                                              $scope.validateWorkListDetails(obj);
                                                                                          } else {
                                                                                              console.log("No matching appointment found");
                                                                                          }
                                                                                      }
																				}
																				else if(response.data.status == "error")
																					{
																					$rootScope.stopLoader();
																					}
																			});
																
															             };  

                                                $scope.joinByDoctorFromRow = function(index) {
                                                        var obj = $scope.allEncounterList[index];
                                                        var data =
                                                        {
                                                        "userChatDetailsId":obj.userChatDetailsId
                                                        }

                                                        var URI = BASE_URL + ROOT_URL + "/api/changeStatusofuserChatDetailsId";
                                                        BillingGenericService.serviceAction(METHOD_POST, URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
                                                                $scope.patientDocumentListTest = [];
                                                                if (response.data.status == "success") {
                                                                             $('#vedioCallAlert').modal('hide');
                                                                             if (obj) {
                                                                                 $scope.showTwilioNotification();
                                                                                 obj.isVideoCallInit = true;
                                                                                 $scope.validateWorkListDetails(obj);
                                                                             } else {
                                                                                 console.log("No matching appointment found");
                                                                             }
                                                                }
                                                                else if(response.data.status == "error")
                                                                    {
                                                                    $rootScope.stopLoader();
                                                                    }
                                                            });

                                                }

															           /*  $scope.roomCreated=false; 
															             $scope.openHeaderTwilioVideoFromLHN= function() {
															            	  if($scope.roomCreated==false){
															            		  $scope.roomCreated=true; 
															            		  $('#HeaderTwilioVideoClick').trigger('click');
															            		  $('#videoTwillio').modal('show');
															            	  }
															            		
															             }*/
					                                        	/*$scope.openHeaderTwilioVideo = function() {
					                                        			 $('#openTwilioVideoFromLHN').trigger('click');
					                                        			 $('#HeaderTwilioVideoClick').trigger('click');
					                                        	}*/
					                                        	//$scope.openHeaderTwilioVideo();
					                                        	//$('#videoTwillio').modal();
															
					                                        	$rootScope.showTwilioNotification = function(){
					                                        		var cookieObject = $cookies.getObject('cookieObject');
					                                        		var url= BASE_URL+ ROOT_URL+'/api/getVideoataForNotification';
					                                        		var totalNotifications = 0;
					                                        		 var twiliobaseUrlLocation = window.location.href;
					                                        			var twiliotemp = twiliobaseUrlLocation.split('/');
					                                        			var twilioBASE_URL_CALL = twiliobaseUrlLocation.replace(twiliotemp[twiliotemp.length-1],"");
					                                        		var data =
					                                        		{
					                                        		"unitId":$scope.unitId,
					                                        		"doctorId": cookieObject.doctorId
					                                        		}
					                                        		var totalNotifications = 0;
					                                        		$.ajax({
					                                        			type : "post",
					                                        			url : url,
					                                        			contentType : "application/json",
					                                        	        data : JSON.stringify(data),
					                                        			success : function(response) {
					                                        	
					                                        					var status = response.status;
					                                        					var list = response.listObject;
					                                        					 $('#twilioNotificationDiv').empty();
					                                        					 angular.forEach(list, function(val) {
					                                        							totalNotifications = totalNotifications + 1;
					                                        				            var src = BASE_URL + ROOT_URL + "resources/assets/images/tempImages/m.jpg";
					                                        				            if (val.patientPhoto != null) {
					                                        				                src = val.patientPhoto;
					                                        				            } else if (val.genderId == 1) {
					                                        				                src = BASE_URL + ROOT_URL + "/resources/assets/images/tempImages/m.jpg";
					                                        				            } else {
					                                        				                src = BASE_URL + ROOT_URL + "/resources/assets/images/tempImages/patient.png";
					                                        				            }

					                                        				            var checkinbutton = "Join";
					                                        				            if (val.appointmentStatusId == 1) {
					                                        				                checkinbutton = "Checkin and Join";
					                                        				            }

					                                        				            var myvar = '<div class="row padding-top10">' +
					                                        				                '<div class="notificationBoxContaint padding-top-bottom5"' +
					                                        				                'style="padding-left: 0px !important; padding-right: 15px !important; padding-bottom: 10px;">' +
					                                        				                '<div class="row padding-top-bottom5">' +
					                                        				                '<div class="col-lg-1 col-md-1 col-sm-1">' +
					                                        				                '<hr' +
					                                        				                'style="margin-top: 7px !important; margin-bottom: 0px !important; border-top: 10px solid rgba(84, 249, 106, 1) !important; width: 10px; border-radius: 20px !important;">' +
					                                        				                '</div>' +
					                                        				                '<div class="col-lg-6 col-md-8 col-sm-8">' +
					                                        				                '<span>Teleconsultation Alert</span>' +
					                                        				                '</div>' +
					                                        				                '<div class="col-lg-5 col-md-3 col-sm-3 text-right">' +
					                                        				                '<span>' + val.appointmentDate + '</span>' +
					                                        				                '</div>' +
					                                        				                '</div>' +
					                                        				                '<div class="row">' +
					                                        				                ' <div class="col-lg-3 col-md-3 col-sm-3">'+
					                                        								 '					<div class="row text-center" style="padding-top: 4px;">'+
					                                        								 '						<img'+
					                                        								 '							src="'+src+'"'+
					                                        								 '							style="width: 70px; height: 70px; border-radius: 50px;">'+
					                                        								 '					</div>'+
					                                        								 '				</div>'+
					                                        				                '<div class="col-lg-9 col-md-9 col-sm-9 text-right">' +
					                                        				                '<div class="row text-center patientDetailsMsgs"' +
					                                        				                'style="font-size: 12px !important;">' +
					                                        				                '<span class="patientDetailsMsg"' +
					                                        				                'style="font-size: 12px !important;">' + val.patientName +
					                                        				                '</span> is ready and waiting in the room for the' +
					                                        				                '<span class="patientDetailsMsg"' +
					                                        				                'style="font-size: 12px !important;">Teleconsultation' +
					                                        				                'appointment at ' + val.timeSlot + '</span>' +
					                                        				                '</div>' +
					                                        				                '<div class="row padding-top10">' +
					                                        				                '<button type="button" class="btn btn-primary width-auto" style="border-radius: 10px; min-width: 110px;" ng-click="joinByDoctorByNotification(' + val.appointmentIdMyLife + ',' + val.userChatDetailsId + ');">' + checkinbutton + '</button>' +
					                                        				                '</div>' +
					                                        				                '</div>' +
					                                        				                '</div>' +
					                                        				                '</div>' +
					                                        				                '</div>';

					                                        				            // Append the compiled HTML
					                                        				            var compiledElement = $compile(myvar)($rootScope);
					                                        				            angular.element(document.getElementById('twilioNotificationDiv')).append(compiledElement);
					                                        				        });

					                                        				        // Apply the changes to the scope
					                                        				        $timeout(function() {
					                                        				            $rootScope.$apply();
					                                        				        });
					                                        				        angular.element(document.getElementById('totalNotifications')).text(totalNotifications);
					                                        					if(list.length==0){
					                                        						 $('#twilioNotificationDiv').append('<div style="text-align: center;">No notification.</div>');
					                                        					}
					                                        				},error : function(){
					                                        					console.log("Error");
					                                        					  stopLoader();
					                                        				}
					                                        			})
					                                        		
					                                        		
					                                        	}
					                                        	$rootScope.showTwilioNotification();

					                                        	
					                                        	
					                                       	
					                                        	(function poll(){
					                                        		setInterval(function(){
					                                        			//debugger;
					                                        			 var url= BASE_URL+ ROOT_URL+'/api/getVideoataForNotification';
					                                        				var totalNotifications = 0;
					                                        				var data =
					                                        				{
					                                        				"unitId":$scope.unitId,
					                                        				"doctorId":$scope.cookieDoctorId
					                                        				}
					                                        				$.ajax({
					                                        					type : "post",
					                                        					url : url,
					                                        					contentType : "application/json",
					                                        			        data : JSON.stringify(data),
					                                        					success : function(response) {
					                                        										  console.log(response);
					                                        										  var list=response.listObject;
					                                        										  if(list != null && list.length >0){
					                                        										  $.each(list,function(i, dto) {
					                                        											  $scope.notificationShownPatientData = dto;
					                                        												$('#twilioappointmentId').val(dto.appointmentIdMyLife);
					                                        												$('#userChatDetailsId').val( dto.userChatDetailsId);
					                                        													                totalNotifications = totalNotifications + 1;
					                                        													             // Get the popup element by ID
					                                        													                var popup = document.getElementById('vedioCallAlert');
					                                        													                var popupvideoTwillio = document.getElementById('videoTwillio');

					                                        													                // Check if the popup is hidden or shown
					                                        													                if ((popup.style.display === 'none' || popup.style.display === '') && (popupvideoTwillio.style.display === 'none' || popupvideoTwillio.style.display === '') ) {
					                                        													                	$('#vedioCallAlert').modal("show");
					                                        													                } else {
					                                        													                    console.log('The popup is shown.');
					                                        													                }
					                                        																	$('#twilioDoctorNotifyMsg').empty();
					                                        																	var html = '<span class="patientDetailsMsg">'
					                                        																			+ dto.patientName
					                                        																			+ '</span> is ready and waiting in the room for the <span class="patientDetailsMsg">Teleconsultation appointment at '
					                                        																			+ dto.timeSlot 
					                                        																			+ '</span>';
					                                        																	$(
					                                        																			'#twilioDoctorNotifyMsg')
					                                        																			.empty();
					                                        																	$(
					                                        																			'#twilioDoctorNotifyMsg')
					                                        																			.append(html);
					                                        																
					                                        										  });
					                                        										  }
					                                        				 $('#totalNotifications').text(totalNotifications);
					                                        			},
					                                        			error : function() {
					                                        				console.log("error");
					                                        				$scope.stopLoader();
					                                        			}
					                                        		});
					                                        		 },30000);
					                                        		})();
					                                        	
					                                            $scope.endCallFlagReset= function() {
					                                            	
					                                            	var appointmentIdForDeelete = $('#twilioappointmentId').val();
					                                         		  $scope.roomCreated=false; 
					                                         		var data = {};
							                                      		var URI = BASE_URL+ ROOT_URL+ "/deleteParticipants/Patient/dataroom_"+appointmentIdForDeelete;
							                                      		GenericService.serviceAction("GET", URI, data).then(function(response) {
					                                           				 
					                                           			});
							                                      		var URI = BASE_URL+ ROOT_URL+ "/deleteParticipants/Doctor/dataroom_"+appointmentIdForDeelete;
							                                      		GenericService.serviceAction("GET", URI, data).then(function(response) {
					                                           				 
					                                           			});
					                                          }
					                                                                $scope.encryDoctorFirstName = "";
					                                                                $scope.encryDoctorMob = "";
					                                                                $scope.pmsUrl = null;
					                                          	                    $scope.getDoctorDetialsEncrypted = function() {

                                                              									var commonObjDoc = {
                                                              											userId : $scope.userId,
                                                              										    unitId :$scope.unitId
                                                              										};

                                                              						        	var URI = BASE_URL + ROOT_URL + "/api/global/getDoctorDetailsEncrypted";

                                                              									GenericServiceParamHeader.serviceAction(METHOD_POST, URI,
                                                              											commonObjDoc, NOTIFICATION_MSG_STATUS_FALSE)
                                                              												.then(function(response) {
                                                              													if(response.data.status == "success"){
                                                              														  $scope.encryDoctorFirstName = response.data.object.firstName;
                                                                                                                      $scope.encryDoctorMob = response.data.object.mobileNumber;
                                                                                                                       $scope.pmsUrl = response.data.object.pmsUrl;
                                                              													}
                                                              									});
                                                              							}

                                                                                        $scope.getDoctorDetialsEncrypted();

        $scope.searchEncounterForAugment =  function() {

            var data = "";
            if(($scope.isRMO=='Y' && $scope.encounterSearchObject.doctorList!=undefined
                    && $scope.encounterSearchObject.doctorList.length>0) || $scope.encounterSearchObject.doctorId != undefined ){
               // return false;
               $scope.startLoader();
                var URI = BASE_URL + ROOT_URL
                + GETOPPATIENTLISTBYDOCTORID;

                GenericServiceParamHeader
                .serviceAction(METHOD_POST, URI,
                        $scope.encounterSearchObject,
                        NOTIFICATION_MSG_STATUS_FALSE,"",{})
                .then(
                    function(response) {
                        console.log(response);
                        // $rootScope.startLoader();
                        $scope.allAugmentEncounterAList = [];
                        if (response.data.status == "success")
                        {
                            angular.forEach(response.data.listObject,function(value,key){
                                if(
                                 (value.modalityId == null || value.modalityId == '') &&
                                 ((value.serviceRenderId !=null && (value.appointmentTypeId==1 || value.appointmentTypeId== 9))
                                || value.againstEncId > 0
                                || value.userDefinedVisitTypeId == 6)
                                ){
                                    $scope.allAugmentEncounterAList.push(value);
                                }
                            });
                            // $scope.allAugmentEncounterAList = $scope.allAugmentEncounterAList.filter(function (item, index, self) {
                            //     return index === self.findIndex(function (t) {
                            //         return t.encounterNumber === item.encounterNumber;
                            //     });
                            // });

								const appointmentIds = new Set(
								$scope.augmentAppointmentList.map(item => item.appointmentId)
							);

							console.log("appointmentIds@@@@@@@@@@@@@@@@@@2  ",appointmentIds);
							
							$scope.allAugmentEncounterAList = $scope.allAugmentEncounterAList.filter(
								item => appointmentIds.has(item.appointmentId)
							);


							$scope.allAugmentEncounterAList.forEach(item => {
								console.log(
									"Encounter ID:",
									item.appointmentId,
									"Exists:",
									appointmentIds.has(item.appointmentId)
								);
});

                            $scope.pager = {};
                            $scope.page;
                            $scope.commonListCountAug = response.data.resObject;
                            $scope.setPage(1,false);
                            console.log("data encounter list",$scope.allAugmentEncounterAList);
                            $scope.stopLoader();
                        }

                    });
            }
        }

        $scope.getAugmentDoctorList = function(){
            var data = {};

            $scope.encounterSearchObject.fromDate = moment($scope.datepicker1).format('YYYY-MM-DD');
            $scope.encounterSearchObject.toDate = moment($scope.datepickerPdd1).format('YYYY-MM-DD');

            console.log("FT",$scope.datepicker1);
            console.log("FT",$scope.datepickerPdd1);
            $scope.encounterSearchObject.offset = $scope.offset;
            $scope.encounterSearchObject.noOfRecordsPerPage = parseInt($scope.noOfRecordsPerPageAug);
            $scope.encounterSearchObject.statusId = 0; // for status--> In Progress

            data.fromDate = $scope.encounterSearchObject.fromDate;
            data.toDate = $scope.encounterSearchObject.toDate;
            data.organizationId = $scope.organizationId;
            data.unitId = $scope.unitId;

            $scope.startLoader();
            var URI = BASE_URL + ROOT_URL + GET_AUGMENT_DOCTOR_LIST;
            GenericServiceParamHeader
            .serviceAction(METHOD_POST, URI, data, NOTIFICATION_MSG_STATUS_FALSE,"",{})
            .then(
                function(response) {
                    console.log(response);
                    if (response.data.status == "success")
                    {
                        if(response.data.object !== undefined && response.data.object !== null){
                            $scope.augmentDoctorId = response.data.object.augmentDoctorId;
                            $scope.encounterSearchObject.doctorId = $scope.augmentDoctorId;

                            $scope.searchEncounterForAugment();
                        }
                        if(response.data.listObject !== undefined && response.data.listObject !== null && response.data.listObject.length > 0){
                            $scope.augmentAppointmentList = response.data.listObject;
                        }
                    }

                });
        }

        $scope.universalSearchEncounterAug = function(searchString){
            if(searchString!="" && searchString!=undefined && searchString!=null && searchString.length>=3){
                $rootScope.startLoader();

                $scope.encounterSearchObject.statusId = 0;
                $scope.encounterSearchObject.fromDate = moment($scope.datepicker1).format('YYYY-MM-DD');
                $scope.encounterSearchObject.toDate = moment($scope.datepickerPdd1).format('YYYY-MM-DD');
                $scope.encounterSearchObject.searchString = searchString;
                $scope.encounterSearchObject.doctorId = $scope.augmentDoctorId;

                if( $scope.encounterSearchObject.statusId!=undefined &&  $scope.encounterSearchObject.statusId !=null
                    &&  $scope.encounterSearchObject.statusId!="" && $scope.encounterSearchObject.statusId!=0){
                    $scope.encounterSearchObject.searchString = "";
                }


                if($scope.isRMO=='Y' || $scope.encounterSearchObject.doctorId != undefined ){

                    var URI = BASE_URL + ROOT_URL+ "/api/opd/doctorDesk/universalSearchOpPatientListByDocId";

                    GenericService.serviceAction(METHOD_POST, URI,$scope.encounterSearchObject)
                    .then(function(response) {
                        $scope.allEncounterList = [];
                        if (response.data.status == "success")
                        { 	 $rootScope.stopLoader();
                            $scope.encounterSearchObject.statusId = 0;
                            $scope.allEncounterList =response.data.listObject;
                        }
                         $rootScope.stopLoader();
                    });
                }
            }
            if(searchString == ""){
                $scope.getAugmentDoctorList();
            }
        }


        $scope.updateAugmentDoctorDetails = function(index){
            var obj = $scope.allAugmentEncounterAList[index];
            var data = {};
            var index = index;

            var aiAppointmentObj = $scope.augmentAppointmentList.find(function(item) {
                return item.appointmentId === obj.appointmentId;
            });

            data.doctorId = $scope.cookieDoctorId;
            data.encounterId = obj.encounterId;
            data.appointmentId = obj.appointmentId;
            data.aiAppointmentId = aiAppointmentObj.aiAppointmentId;
            data.organizationId = $scope.organizationId;
            data.unitId = $scope.unitId;

            $scope.startLoader();
            var URI = BASE_URL + ROOT_URL + UPDATE_AUGMENT_DOCTOR;
            GenericServiceParamHeader
            .serviceAction(METHOD_POST, URI, data, NOTIFICATION_MSG_STATUS_FALSE,"",{})
            .then(
                function(response) {
                    console.log(response);
                    if (response.data.status == "success")
                    {
                        $scope.allAugmentEncounterAList[index].specialityId = response.data.object.specialityId;
                        $scope.allAugmentEncounterAList[index].specialityName = response.data.object.specialityName;
                        $scope.allAugmentEncounterAList[index].doctorId = $scope.cookieDoctorId;
                        $scope.allAugmentEncounterAList[index].doctorName = $scope.cookieDoctorName;
                        $scope.allAugmentEncounterAList[index].docNameTemp = $scope.cookieDoctorName;
                        $scope.joinByDoctorFromRowAug(index);
                    }

                });
        }


        $scope.joinByDoctorFromRowAug = function(index) {
            var obj = $scope.allAugmentEncounterAList[index];
            var data =
            {
                "userChatDetailsId":obj.userChatDetailsId
            }

            var URI = BASE_URL + ROOT_URL + "/api/changeStatusofuserChatDetailsId";
            BillingGenericService.serviceAction(METHOD_POST, URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
                $scope.patientDocumentListTest = [];
                if (response.data.status == "success") {
                     $('#vedioCallAlert').modal('hide');
                     if (obj) {
                         $scope.showTwilioNotification();
                         obj.isVideoCallInit = true;
                         $scope.validateWorkListDetails(obj);
                     } else {
                         console.log("No matching appointment found");
                     }
                }
                else if(response.data.status == "error")
                {
                    $rootScope.stopLoader();
                }
            });

        }


		$scope.aiAppointmentCount=0;

		(function poll(){
					setInterval(function(){
							var url= BASE_URL+ ROOT_URL+'/api/opd/getAIAppointmentCount';
							var data =
							{
							"unitId":$scope.unitId,
							}
							$.ajax({
								type : "post",
								url : url,
								contentType : "application/json",
								data : JSON.stringify(data),
								success : function(response) {
									$scope.aiAppointmentCount = response.object;
									$scope.$apply();					
						},
						error : function() {
							$scope.aiAppointmentCount=0;
							console.log("error");
							$scope.stopLoader();
						}
					});
						},30000);
					})();

				

					$scope.aiappoinmentCheckOnRefresh = function() {
						var url= BASE_URL+ ROOT_URL+'/api/opd/getAIAppointmentCount';
							var data =
							{
							"unitId":$scope.unitId,
							}
							$.ajax({
								type : "post",
								url : url,
								contentType : "application/json",
								data : JSON.stringify(data),
								success : function(response) {
									$scope.aiAppointmentCount = response.object;
									$scope.$apply();					
						},
						error : function() {
							$scope.aiAppointmentCount=0;
							console.log("error");
							$scope.stopLoader();
						}
					});
				}

					$scope.aiappoinmentCheckOnRefresh();


						} ]);
