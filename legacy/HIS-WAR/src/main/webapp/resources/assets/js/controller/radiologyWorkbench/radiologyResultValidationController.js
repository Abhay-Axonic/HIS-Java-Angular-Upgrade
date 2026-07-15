'use strict';

/**
 * @Author By  Vivek Satle
 * @name myApp.controller:radiologyWorkbenchController
 * @description #radiologyWorkbenchController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'radiologyResultValidationController',
				[
						'$scope',
						'$cookies',
						'$rootScope',
						'GenericService',
						'BillingGenericService',
						'$state',
						'$timeout',
						'$interval',
						'growl',
						'GenericServiceParamHeader',
						'PaginationService',
						'$compile',
						'reportService',
						'$stateParams',
						function($scope, $cookies, $rootScope, GenericService,
								BillingGenericService, $state, $timeout,
								$interval,growl, GenericServiceParamHeader,PaginationService,$compile,reportService,$stateParams) {

							//alert("radiologyWorkbenchController");
							var cookieObjectAppConfigSecond = JSON.parse(localStorage.getItem('cookieObjectAppConfigSecond'));
							var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
							 if(cookieObjectAppConfig == undefined){
							  $state.go('login');
							  return;
							 }
							 else
							  {
							   console.log('cookieObjectAppConfig',cookieObjectAppConfig);
							   
							  }
							$scope.radiologySpecialityId = cookieObjectAppConfig.radiologySpecialityId;
							$scope.opdBillingCatId = cookieObjectAppConfig.opdBillingCatId;
							$scope.defaultDiscountOwnership = cookieObjectAppConfig.defaultDiscountOwnership;
							$scope.serviceWiseDoctorListReq =  cookieObjectAppConfig.serviceWiseDoctorListReq;
							/* init() function for form object create. */
							$scope.init = function() {

								$scope.dateOptions = {
									formatYear : 'yyyy',
									showWeeks : false,
									maxDate : new Date()
								};
								$scope.todateOptions =
								{
									formatYear : 'yyyy',
									showWeeks : false,
									maxDate : new Date(),
									minDate : new Date(),
								};

								//Date Object For Search DOB
								/*$scope.openDatePickerSearch = function() {
									$scope.datepickerSearch.opened = true;
								};

								$scope.datepickerSearch = {
									opened : false
								};*/

								//Date Object For Reshedule
								

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
								$scope.unitName = cookieObject.unitNme;
								$scope.userId = cookieObject.userId;
								$scope.roleId = 2;
								
								$rootScope.loginpage = true;
								$scope.saveBtnFlag = false;
								$scope.updateBtnFlag = true;
								$scope.isBillManipulationAllowedUserLevel = cookieObject.isBillManipulationAllowedUserLevel;
								$scope.isBillManipulationAllowedAppLevel = cookieObjectAppConfigSecond.isBillManipulationAllowedAppLevel;
								$scope.isBillManipulationAllowed = ($scope.isBillManipulationAllowedUserLevel && $scope.isBillManipulationAllowedAppLevel =='Y') ? true : false;
								if($scope.isBillManipulationAllowed == true){
									$scope.filterDummyService = 1;
								}else{
									$scope.filterDummyService = 2;
								}
								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup

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
								 
								 var commonObjsxds = {
											unitId : $scope.unitId,
											organizationId : $scope.organizationId
										};
								 
								 
									var URI = BASE_URL 
									+ ROOT_URL
									+ "/adt/getDoctorListForOPDBilling";
							GenericService
									.serviceAction("POST", URI, commonObjsxds)
									.then(
											function(response) {
												$scope.allDoctorsList = [];

												if (response.data.status == "success") {
													$scope.allDoctorsList = response.data.listObject;
													console.log("allDoctorsList",$scope.allDoctorsList);

												}
											});
							
						
								//start radiology
								$scope.visitTypeList = [ {
									id : 3,
									label : " Day Care"
								}, {
									id : 4,
									label : " ER"
								}, {
									id : 1,
									label : " OP "
								}, {
									id : 2,
									label : " IP "
								} ];
								$scope.selectedVisitType = [];
								$scope.selectedModalityList = [];
								
								var commonObj = {
										unitId : $scope.unitId,
										organizationId : $scope.organizationId
									};
								
								var URI = BASE_URL + ROOT_URL
								+ GETACTIVEMODALITYLIST;
								GenericService
								.serviceAction("POST", URI, commonObj)
								.then(
										function(response) {
											if (response.data.status == "success") {
												$scope.modalityList = [];
												angular.forEach(response.data.listObject,
																function(value,index) {
																	var data = {};
																	data.id = value.modalityId;
																	data.label = value.modalityDesc;
																	$scope.modalityList.push(data);
																});
												console.log("ActiveModalityMasterList",$scope.modalityList);
											}
										});	

								//end radiology

								
								var data = {organizationId : $scope.organizationId};
								var headerObj = {
									organization_id : $scope.organizationId,
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

								//For Get Active Reason List
								var URI = BASE_URL + ROOT_URL
										+ GETACTIVEREASONLIST;
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													$scope.reasonList = [];

													if (response.data.status == "success") {
														$scope.reasonList = response.data.listObject;

													}
												});

								$scope.encounterSearchObject = {
									'patientId' : 0,
									'organizationId' : $scope.organizationId,
									'unitId' : $scope.unitId,
									'serviceId':0
								}
								
								if($stateParams.searchObj!=undefined && $stateParams.searchObj!=null){
									  if($stateParams.searchObj.datepickerFromDate!=undefined){
									    	$scope.datepickerFromDate = $stateParams.searchObj.datepickerFromDate;
									    }
									
									if($stateParams.searchObj.datepickerToDate!=undefined){
									    	$scope.datepickerToDate = $stateParams.searchObj.datepickerToDate;
									    }
									
									if($stateParams.searchObj.patientName!=undefined){
								    	$scope.patientName = $stateParams.searchObj.patientName;
								    }
									
									if($stateParams.searchObj.selectedPatient!=undefined){
								    	$scope.selectedPatient = $stateParams.searchObj.selectedPatient;
								    }
									
									if($stateParams.searchObj.uhidNumber!=undefined){
								    	$scope.uhidNumber = $stateParams.searchObj.uhidNumber;
								    }
									
									if($stateParams.searchObj.selectedVisitType!=undefined){
								    	$scope.selectedVisitType = $stateParams.searchObj.selectedVisitType;
								    }
									
									if($stateParams.searchObj.selectedModalityList!=undefined){
								    	$scope.selectedModalityList = $stateParams.searchObj.selectedModalityList;
								    }
									
									if($stateParams.searchObj.serviceName!=undefined){
								    	$scope.serviceName = $stateParams.searchObj.serviceName;
								    }
									
									if($stateParams.searchObj.patientId!=undefined){
										$scope.encounterSearchObject.patientId = $stateParams.searchObj.patientId;
								    }
								}
								
								  var specDocObj = {
							    			organizationId:$scope.organizationId,
							    			userId:$scope.userId
							        }
								    var URI = BASE_URL + ROOT_URL+ "/adt/getSpecialityListByUserIdByUnitId";
							        BillingGenericService.serviceAction(METHOD_POST, URI,specDocObj,
											NOTIFICATION_MSG_STATUS_FALSE)
									.then(function(response) {
													if (response.data.status == "success"){
														$scope.staffSpecialityList = [];
														$scope.staffSubSpecialityList = [];
					                                    $scope.staffSpecialityList = response.data.listObject;
													    $scope.staffSubSpecialityList = response.data.object;
													    console.log("speciality",$scope.staffSpecialityList);
													    console.log("specialitySub",$scope.staffSubSpecialityList);
													}
				                                 });
							}

							$scope.init();

							$scope.saveDoctorShareRequest = function(){
								
						        var data = {
						        		ordDiscount: $scope.item.discount,
						                ordConcession : $scope.item.concession,
						                ordDocId : $scope.item.ordDoctorId,
						                ordDocSplId : $scope.doctorSpecialityId,
						                encounterId : $scope.item.encounterId,
						                admissionId : $scope.item.admissionId,
						                orderDetailsId : $scope.item.orderDetailsId,
						                orgId : $scope.organizationId,
						                orgUnitId : $scope.unitId,
						                serviceId : $scope.item.serviceId,
						                departmentId : $scope.item.specialityId,
						                ordTotalAmt : $scope.item.ordTotalAmount,
						                tariffId : $scope.item.tariffId,
						                isDocReq : $scope.item.isDoctorRequired,
						                isRefDoctorShare:$scope.item.isRefDoctorShare ,
						                docSharePer:$scope.docSharePer,
						                hospitalShare:$scope.hospitalShare,
						                refDocSharePer:$scope.refDocSharePer,
						                discountOwnership: parseInt($scope.discountOwnership),

						        };

						        $rootScope.startLoader();
						        var URI = BASE_URL + ROOT_URL + "/api/global/updateDoctorShareRisByChangingDoctor";

						        BillingGenericService.serviceAction(METHOD_POST,URI,data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
						            if(response.data.status == "success"){
						            	growl.success("Updated Successfully..!");
						            	$scope.searchEncounter();
						                $("#confirmSaveSendDoctorShare").hide();
						            }else{
						                $rootScope.stopLoader();
						            }
						        });
						    }
							
							$scope.calculateShareAmount = function(){
								
								var item = $scope.selectedInfo;
								var index = $scope.selectedIndex;
								$scope.item = {};
								
								$scope.orderDetailsIdSelected = item.orderDetailsId;
								var billingBedCat =0;
								if(item.visitTypeId == 1){
									billingBedCat = $scope.opdBillingCatId;
								}else{
									billingBedCat = item.billingBedCategoryId;
								}
						        var data = {
						            "visitTypeId" : item.visitTypeId,
						            "billingBedCategoryId" :billingBedCat ,
						            "patientTypeId" : item.patientTypeId,
						            "paymentEntitlementTypeId" : item.paymentEntitlementId,
						            "referralId" : item.referralId,
						            listOrderDetailsMasterDto : []
						        };
						     
						        var newArr = {
						                ordDiscount:item.discount,
						                ordConcession : item.concession,
						                ordDocId : item.ordDoctorId,
						                ordDocSplId : $scope.doctorSpecialityId,
						                encounterId : item.encounterId,
						                admissionId : item.admissionId,
						                orderDetailsId : item.orderDetailsId,
						                orgId : $scope.organizationId,
						                orgUnitId : $scope.unitId,
						                serviceId : item.serviceId,
						                departmentId : item.specialityId,
						                ordTotalAmt : item.ordTotalAmount,
						                tariffId : item.tariffId,
						                isDocReq : item.isDoctorRequired,
						                isRefDoctorShare:item.isRefDoctorShare ,
						                discountOwnership: parseInt(item.discountOwnership),
						                orderDate : item.orderDateLong,

						        };
						        $scope.item = item;
						        var orderDetailsList = [];
						        orderDetailsList.push(newArr);
						        data.listOrderDetailsMasterDto = orderDetailsList;

						        $rootScope.startLoader();
						        var URI = BASE_URL + ROOT_URL + "/api/billing/order/docShare";

						        BillingGenericService.serviceAction(METHOD_POST,URI,data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
						            if(response.data.status == "success"){
						                $scope.docSharePer = response.data.object.listOrderDetailsMasterDto[0].docSharePer!=null ? parseFloat(response.data.object.listOrderDetailsMasterDto[0].docSharePer).toFixed(2) : 0;
						                $scope.hospitalShare = response.data.object.listOrderDetailsMasterDto[0].hospitalShare!=null ?parseFloat(response.data.object.listOrderDetailsMasterDto[0].hospitalShare).toFixed(2):0;
						                $scope.refDocSharePer = response.data.object.listOrderDetailsMasterDto[0].refDocSharePer!=null ? parseFloat(response.data.object.listOrderDetailsMasterDto[0].refDocSharePer).toFixed(2) : 0;
						                $scope.discountOwnership = (response.data.object.listOrderDetailsMasterDto[0].discountOwnership>0 && (response.data.object.listOrderDetailsMasterDto[0].concession != 0 || response.data.object.listOrderDetailsMasterDto[0].discount != 0)) > 0 ? response.data.object.listOrderDetailsMasterDto[0].discountOwnership : $scope.defaultDiscountOwnership;
						                
						                $scope.saveDoctorShareRequest();
						               /* $("#confirmSaveSendDoctorShare").show();*/
						            }else{
						                $rootScope.stopLoader();
						            }
						        });
						    }
							
							$scope.getSpecialityByDoctorMainChange = function(info,index)
							{
								if(info.doctorShareId != null && info.doctorShareId >0){
									growl.error("Doctor share is already processed ,You Can not change doctor ");
									return false;
								}
								
								if(info.creditNoteId != null && info.creditNoteId >0){
									growl.error("Credit note already generated ,You Can not change doctor ");
									return false;
								}
								
								if(info.ordDoctorId!=undefined && info.ordDoctorId!=null && info.ordDoctorId!=0)
									{
									var specDocObj = {
							    			unitId:$scope.unitId,
							    			organizationId:$scope.organizationId,
							    			doctorId:info.ordDoctorId
							    }
							    var URI = BASE_URL + ROOT_URL
								+ GETSPECIALITYLISTBYDOCTORID;
								BillingGenericService.serviceAction(METHOD_POST, URI,specDocObj,
										NOTIFICATION_MSG_STATUS_FALSE)
								.then(function(response) {
											if (response.data.status == "success")
												{
												$scope.getInitiallySpecialityList=response.data.listObject;
												var specialityLsit=[];
												angular.forEach($scope.getInitiallySpecialityList, function (service, key) 
														{
															if(service.doctorId==info.ordDoctorId)
																{
																specialityLsit.push(service);
																}
														});
												$scope.doctorSpecialityId = specialityLsit[0].specialityId;
												$("#confirmSaveSendDoctorShare").show();
												$scope.selectedInfo = info;
												$scope.selectedIndex = index;
												angular.forEach($scope.pagination.data, function(value, key){
                                                          if(key == index){
                                                           value.ordDoctorId = parseInt(info.ordDoctorId);
                                                          }
                                                      });
											//	$scope.calculateShareAmount(info,index);
												}
						
							    	});
								
								  
						    	}
							}
		
							
							
							$scope.CloseDoctorShareRequest = function(){
							   $scope.searchEncounter();
								 $("#confirmSaveSendDoctorShare").hide();
							}
							//Search Appointment List By Filters
							$scope.searchEncounter = function() {
								
								console.log($scope.selectedVisitType);
								console.log($scope.selectedModalityList);
								var visitList =[];
								var modalityList =[];
								
								angular.forEach($scope.selectedVisitType,
										function(value,index) {
									         visitList.push(value.id);
										});
								angular.forEach($scope.selectedModalityList,
										function(value,index) {
									      modalityList.push(value.id);
										});
								
								$rootScope.startLoader();
								$scope.encounterSearchObject.fromDate = moment(
										$scope.datepickerFromDate).format(
										'YYYY-MM-DD');
								$scope.encounterSearchObject.toDate = moment(
										$scope.datepickerToDate).format(
										'YYYY-MM-DD');
								
								$scope.encounterSearchObject.visitList = visitList;
								$scope.encounterSearchObject.modalityList = modalityList;
								$scope.encounterSearchObject.radiologySpecialityId =cookieObjectAppConfig.radiologySpecialityId;
								$scope.isRisPatientDetailsOnFirstPage = cookieObjectAppConfig.isRisPatientDetailsOnFirstPage;
								
								console.log($scope.encounterSearchObject);

								var paramObj = {};
								var headers = {};
								if($scope.encounterSearchObject.patientId==0 || $scope.encounterSearchObject.patientId==undefined || $scope.encounterSearchObject.patientId==""){
									$scope.patientName = '';
									$scope.uhidNumber = '';
								}
								//return false;
								var URI = BASE_URL + ROOT_URL
										+ "/api/opd/RIS/getradiologyReportReleaseWorkList";

								GenericServiceParamHeader
										.serviceAction(METHOD_POST, URI,
												$scope.encounterSearchObject,
												NOTIFICATION_MSG_STATUS_FALSE,
												"", "")
										.then(
												function(response) {
													console.log(response);
													// $rootScope.startLoader();
													$scope.pagination.data=[];
													$scope.allEncounterList = [];
													if (response.data.status == "success")
														{
														$scope.allEncounterList = [];
														$scope.allEncounterListTest = [];
		                                                $scope.allEncounterListTest = response.data.listObject;
														
														if($scope.staffSubSpecialityList != null && $scope.staffSubSpecialityList.length > 0 ){
															angular.forEach($scope.allEncounterListTest, function(value, key) {
																angular.forEach($scope.staffSubSpecialityList ,function(value1,key1){
																	if(value.subSpecialityId == value1.subSpecialityMasterId){
																		$scope.allEncounterList.push(value);
																	}
																
																});		
															 });
														}else{
															$scope.allEncounterList = response.data.listObject;
														}
														
														$scope.allEncounterListForDummyFilter = $scope.allEncounterList;
								                        if($scope.isBillManipulationAllowed==false)
								                        {
								                        	$scope.allEncounterList = [];
								                            angular.forEach(response.data.listObject,function(obj,key)
								                             {
								                                if(obj.isDummyService==false)
								                                {
								                                	$scope.allEncounterList.push(obj);
								                                }
								                             });
								                        }
								                        $scope.filterByDummyServices();
														
														
														angular.forEach($scope.allEncounterList, function(value, key) {
															if(value.ordDoctorId != null && value.ordDoctorId >0){
																value.docList = [];
																var dddd = {};
																dddd.doctorId = value.ordDoctorId;
																dddd.doctorName = value.doctorName;
																value.docList.push(dddd);
															}
														});	
														  if(!!response && !!response.data && response.data.listObject.length > 0){            
										                         PaginationService.searchData($scope.allEncounterList, $scope.pagination);
										                       }
														}
													$rootScope.stopLoader();
														});

							}
							$scope.searchEncounter();

							
							$scope.filterByDummyServices= function()
					         {
					             $rootScope.startLoader();
					             $scope.allEncounterList =[]
					             if($scope.filterDummyService==1)//all 
					             {
					            	 $scope.allEncounterList = angular.copy($scope.allEncounterListForDummyFilter);
					             }else if($scope.filterDummyService==2)//real
					             {
					                 angular.forEach($scope.allEncounterListForDummyFilter, function (value, key) 
					                 {
					                     if(value.isDummyService==false)
					                    	 $scope.allEncounterList.push(value);
					                 });
					             }else if($scope.filterDummyService==3 )//dummy
					             {
					                 angular.forEach($scope.allEncounterListForDummyFilter, function (value, key) 
					                 {
					                     if(value.isDummyService==true)
					                    	 $scope.allEncounterList.push(value);
					                 });
					             }
					             PaginationService.searchData($scope.allEncounterList, $scope.pagination);
					             $rootScope.stopLoaderPopup();
					             $rootScope.stopLoader();
					         }
							
							
							
							
							$scope.clearSearchObject = function() {
								$scope.encounterSearchObject = {
									'organizationId' : $scope.organizationId,
									'unitId' : $scope.unitId,
									'patientId' : 0,
									'serviceId' : 0
								}
								$scope.serviceName = "";
								$scope.selectedVisitType = [];
								$scope.selectedModalityList = [];
								
								$scope.patientName = '';
								$scope.uhidNumber = '';

								$scope.datepickerFromDate = new Date();
								$scope.datepickerToDate = new Date();
							}

							$scope.getReportDetails=function(radiology){
								var searchObj = {};
					       		searchObj.datepickerFromDate = $scope.datepickerFromDate;
					       		searchObj.datepickerToDate = $scope.datepickerToDate;
					       		searchObj.patientName = $scope.patientName;
					       		searchObj.selectedPatient = $scope.selectedPatient;
					       		searchObj.uhidNumber = $scope.uhidNumber;
					       		searchObj.selectedVisitType = $scope.selectedVisitType;
					       		searchObj.selectedModalityList = $scope.selectedModalityList;
					       		searchObj.serviceName = $scope.serviceName;
					       		searchObj.patientId = $scope.encounterSearchObject.patientId;
								radiology.isTempFinalRelease='Y';
								radiology.isResultEntry='N';
						       	 $state.go("radiologyReport",{
						       		radiologyReportObject :radiology,
						       		searchObj : searchObj
									}); 
						       		
						       		
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
										.serviceAction(METHOD_POST, URI, data,
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
					
							$scope.checkPatient = function()
							{
								if($scope.patientName ==undefined)
								{
									$scope.encounterSearchObject.patientId =0;
								}
							}
							$scope.checkService = function()
							{
								if($scope.serviceName ==undefined || $scope.serviceName=="")
								{
									$scope.encounterSearchObject.serviceId =0;
								}
							}
							
							$scope.search="";
							$scope.searchServices = function(searchKeyword){
								$scope.servicesList=[];
								
								var serData = {
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
										searchKeyword : searchKeyword
								}
								var URI = BASE_URL + ROOT_URL + "/global/serviceMaster";
								
								//var URI = BASE_URL + ROOT_URL + "/global/serviceMaster/"+searchKeyword+"/unit/"+$scope.unitId+"/org/"+$scope.organizationId;
								return BillingGenericService.serviceAction(METHOD_POST,URI,serData, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
									if(response.data.status == "success"){
										$scope.servicesList = response.data.listObject;
									}
									return $scope.servicesList;
								});
							}
							
							$scope.addServiceToTempServiceList = function($item, $model, $label){
								$scope.encounterSearchObject.serviceId = $item.serviceMasterId
//								$scope.search = '';
//								$scope.serviceName = info.serviceStandardName  +" | " +info.serviceStandardCode ;
							}
							$scope.getClassForColor = function(info)
							{
									   
							   if(info.isCritical=='Y'){
								   return 'td-bg-red';
							   }else if (info.isVip=='Y') 
							   {
								    return 'td-bg-blue';
							   }else if(info.encounterTypeId==6){
								   return 'td-bg-green';
							   }
							}
							
							
							$scope.getToDate=function(){
								$scope.todateOptions.minDate=$scope.datepickerFromDate.getTime();
								
								if(moment($scope.datepickerToDate).isBefore(moment($scope.datepickerFromDate)))
								{
									 $scope.datepickerToDate=new Date($scope.datepickerFromDate.getTime());
								}
							}
							/****Template Save*****/
							
							
							   $scope.checkServiceWiseDoctorsList = function(info, idx){
								    $scope.selectedInfo = info;
									$scope.selectedIndex = idx;
									$scope.item = {};
									$scope.orderDetailsIdSelected = info.orderDetailsId;
									var billingBedCat =0;
									if(info.visitTypeId == 1){
										billingBedCat = $scope.opdBillingCatId;
									}else{
										billingBedCat = info.billingBedCategoryId;
									}
							   
								   var docListObj={
										   serviceId : info.serviceId,
										   serviceDepartmentId : info.specialityId,
									   		unitId : $scope.unitId,
									   		organizationId : $scope.organizationId,
									   		billingBedCategoryId : billingBedCat,
									   		visitTypeId : info.visitTypeId,
									   		patientTypeId : info.patientTypeId,
									   		paymentEntitlementId:info.paymentEntitlementId
									   }
								   
									   var URI = BASE_URL + ROOT_URL + "/adt/getDoctorShareWiseDocList";

									   GenericService
									   .serviceAction("POST", URI, docListObj)
									   .then(
									   		function(response) {
									   		
									   			$scope.doctorsListNew = [];
									   			if (response.data.status == "success") {
									   				$scope.doctorsListNew = response.data.listObject;
									   				angular.forEach($scope.pagination.data, function(value, key){
									   				   if(key == idx){
									   					value.docList = $scope.doctorsListNew;
									   				   }
									   			   });
									   			}
									   		});
							   }
							   
					        $scope.templateTransaction = {
				        			  templateTransactionId : "",
				        			  templateId : "",
				        			  title : "",
				        			  specialityId : "",
				        			  subSpecialityId : "",
				        			  diagnosisId:"",
				        			  templateObject : "",
				        			  orderDetailsId : "",
				        			  admissionId : "",
				        			  encounterId : "",
				        			  doctorId : "",
				        			  patientId : "",
				        			  status : 'A',
				        			  signedBy : $scope.userId,
				        			  signedDate : "",
				        			  jsonObject : "",
				        			  orgId : $scope.organizationId,
				        			  unitId : $scope.unitId,
				        			  createdBy : $scope.userId,
				        			  createdDate : "",
				        			  updatedBy : $scope.userId,
				        			  updatedDate : ""
				        }
					        /*$scope.printReportOLD=function(info)
					    	{

					        	info.organizationId = $scope.organizationId;
					        	info.unitId =  $scope.unitId;

					        	var URI = BASE_URL + ROOT_URL + "/api/opd/RIS/printReport";
								$("#radiologyPrint").remove();
							     var form = null;
							     form = document.createElement("form");
							     form.setAttribute("id", "radiologyPrint");
							     form.setAttribute("name", "Radiology Report");
							     form.setAttribute("method", "post");
							     form.setAttribute("action", URI);
							     form.setAttribute("target", "_blank");
							     var hiddenField1 = document.createElement("input"); 
							     hiddenField1.setAttribute("type", "hidden");
							     hiddenField1.setAttribute("name", "reportParam");
							     hiddenField1.setAttribute("value", JSON.stringify(info));
							     hiddenField1.setAttribute("type", "hidden");
							     hiddenField1.setAttribute("name", "templateContent");
							     hiddenField1.setAttribute("value",info.templateContent);
							     form.appendChild(hiddenField1);
							     
							     
							     
							     document.body.appendChild(form);
							     $("form[name='Radiology Report']").submit();
					    	}*/
					        	$scope.printReport=function(info,printWithHeader){
					        	if(info.isHtmlPrint==true){
					        	info.organizationId = $scope.organizationId;
					        	info.unitId =  $scope.unitId;
					        	info.printWithHeader = printWithHeader;
					        	info.unitName =  $scope.unitName;
					        	
					        	var infoCopy = info;

					        	
					        	var URI = BASE_URL + ROOT_URL + "/api/opd/RIS/printReportTest";
					    		BillingGenericService.serviceAction(METHOD_POST, URI, infoCopy,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
					    				{
					    	
					    			   if(response.data.status=='success')
					    			      {
					    				   console.log("gg",response.data.object);
					    				   //'<iframe src="https://s3.ap-south-1.amazonaws.com/his-docs/apollohospitals1/patient/documents/clinical/episodic-documents/op-coversheet/1_1_null_Sign.png" style="margin-left:462px;" width="49%" height="15%"></iframe>'+ 
					    				  /* var divContents = response.data.object.latterHeadImg + response.data.object.header+response.data.object.templateContent+response.data.object.footer;  
								            var printWindow = window.open('', '', 'height=500,width=700');  
								            printWindow.document.write('<html><head>');  
								            printWindow.document.write('</head><body>');  
								            printWindow.document.write(divContents);  
								            printWindow.document.write('</body></html>');  
								            printWindow.document.close();  
								            printWindow.print();  */
					    				   
					    				   
					    				   var headerSize = "";
					    					 var letterImgAndHeader="";
					    					 var patientDetails = "";
					    					 var completeTemplateContent = "";
					    					 if($scope.isRisPatientDetailsOnFirstPage == 'N'){
					    						 //print patient details on all pages
					    						 headerSize = "220px;";
					    						 letterImgAndHeader = response.data.object.latterHeadImg+response.data.object.header;
					    						 completeTemplateContent = response.data.object.templateContent+response.data.object.footer;
					    					 }else{
					    						 //print patient details on first page
					    						 headerSize = "105px;";
					    						 letterImgAndHeader = response.data.object.latterHeadImg;
					    						 if(printWithHeader == 0){
					    							 patientDetails = response.data.object.header;
					    						 }else{
					    							 patientDetails = response.data.object.headerWithoutImg;
					    						 }
					    						 completeTemplateContent = patientDetails + response.data.object.templateContent+response.data.object.footer;
					    					 }
					    					 
					    					 
					    				   var printWindow = window.open('', '', 'height=500,width=700');  
                                           printWindow.document.write('<html><head></head>');  
                                           printWindow.document.write(' <style> '
                                                   +'.page-header, .page-header-space {height:'+headerSize+'} '
                                                   +' .page-footer, .page-footer-space { height: 105px;} '
                                                   + ' .page-footer { position: fixed; bottom: 0; width: 100%;} '
                                                   + ' .page-header { position: fixed; top: 0mm; width: 100%;} '
                                                   + ' .page {page-break-after: auto;} '
                                                   + '  @page { margin: 20mm}'
                                                   + 'p { margin:0px 0px 0px 0px; }'
                                                   + ' </style>' );
                                           printWindow.document.write('<body>');
                                           if(printWithHeader == 0){
                                                 printWindow.document.write('<div class="page-header" style="text-align: center">');
                                                 printWindow.document.write(letterImgAndHeader);  
                                                 printWindow.document.write('</div>');
                                                 
                                                 
                                                 printWindow.document.write('<div class="page-footer" style="text-align: center">');
                                                 printWindow.document.write(response.data.object.footerImg);  
                                                 printWindow.document.write('</div>');
                                           }else if($scope.isRisPatientDetailsOnFirstPage == 'N'){
                                        	 //skip this part during first page bcoz we append patient details allready
                                               printWindow.document.write('<div class="page-header" style="text-align: center">');
                                               printWindow.document.write(response.data.object.headerWithoutImg);  
                                               printWindow.document.write('</div>');
                                           }
                                           
                                           printWindow.document.write('<table> <thead> <tr><td><div class="page-header-space"></div> </td></tr> </thead>');
                                           printWindow.document.write(' <tbody><tr><td><div class="page" style="line-height: 1.2;">');
                                           printWindow.document.write(completeTemplateContent);  
                                           printWindow.document.write('</div> </td></tr></tbody>');
                                           printWindow.document.write(' <tfoot> <tr><td><div class="page-footer-space"></div></td></tr></tfoot></table>');
                                           printWindow.document.write('</body>');
                                           printWindow.document.write('</html>');
                                           printWindow.document.close();  
                                           $timeout( function(){
                                                printWindow.print();  
                                           },2000);
								            
					    			      }
					    			   else if(response.data.status=='error'){
					    				   $rootScope.stopLoader();
					    			   }
					    		});
					        }
				        	else
				        		{
				        		
				        		var data = {
				        		"appName": "HIS",
				        		"appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
				        		"reportDesignFilePath": "RadiologyReport.rpt",
				        		"reportFormat": "PDF",
				        		"exportFileName": "RadiologyReport",
				        		"reportParams": [
				        		{
				        		"paramName": "template_transaction_id",
				        		"paramType": "Integer",
				        		"paramValue": info.templateTransactionId
				        		},{
				        		"paramName": "isOriginal",
				        		"paramType": "Integer",
				        		"paramValue": 1
				        		},{
				        		"paramName": "isHeader",
				        		"paramType": "Integer",
				        		"paramValue": printWithHeader
				        		}
				        		]
				        		};
				        		reportService.generate(REPORT_API_URI1,data);


				        		}

							
					        }
					      
					        

							$scope.initPatientDaignosis = function(){
								$scope.patientDaignosisObj = {
										"diagnosisName":"",
										"diagnosisId":"",
										"type":1,
										"diagnosisType":"",
										"remark":""
								};
								
							}
							
							$scope.initPatientDaignosis();
							
							$scope.patientDetailsObj = {};
								$scope.getPatientDiagnosis = function(Obj) {
									$("#diagnosisPopRadiology").show();
									$scope.initPatientDaignosis();
									$scope.patientDetailsObj = Obj;
									$scope.getPatientServiceDaignosisData();
								}
							
								
								$scope.getPatientServiceDaignosisData = function(){
									var data = {
		   									unitId: $scope.unitId,
		   									patientId : $scope.patientDetailsObj.patientId,
		   									};
									
									
									var URI = BASE_URL + ROOT_URL
											+ "/api/opd/getPatientDaignosisServiceMapper";
									GenericService.serviceAction("POST", URI, data,
													NOTIFICATION_MSG_STATUS_FALSE,
													data).then(
													function(response) {
														console.log(response);
														if (response.data.status == "success")
															 $scope.patientDiagnosisList = [];
			                                            angular.forEach(response.data.listObject,function(value,index)
				 												{
			                                            	 		$scope.patientDiagnosisList.push(value);
				 												});
														
													});
								}
								
								/*$scope.searchGroupList = function(keyword) {
									$scope.DiagnosisByGenderList=[];
									var data = {
											organizationId : $scope.organizationId,
											diagnosisName : keyword,
											genderId:$scope.patientDetailsObj.genderId
											};
									
									$scope.varOr = "|";

									var URI = BASE_URL + ROOT_URL
											+ "/api/opd/getDiagnosisByGenderId";
									
									return GenericService.serviceAction("POST", URI,data)
											.then(
													function(response) {
														console.log(response);
														 $rootScope.startLoader();
														if (response.data.status == "success")
															 $scope.DiagnosisByGenderList = response.data.listObject;
														if($scope.DiagnosisByGenderList.length == 0){
															
															$scope.flagDigByGen=true;
															var newArr = {};
															newArr.diagnosisName="Diagnosis not applicable";
															newArr.diagnosisId='';
															$scope.DiagnosisByGenderList.push(newArr);
															$scope.varOr="";
															return response.data.listObject= $scope.DiagnosisByGenderList;
														} else{
															$scope.flagDigByGen=false;
														}
															return response.data.listObject;
													});

								}*/
								
								 $scope.searchDaignosisList = function(keyword) {
                                     var data = {
                                             organizationId : $scope.organizationId,
                                             diagnosisName : keyword,
                                             genderId:$scope.patientDetailsObj.genderId
                                          }
                                          console.log(data);
                                   
                                          var URI = BASE_URL + ROOT_URL
                                          + "/api/opd/getDiagnosisByGenderId";

									    return BillingGenericService
									          .serviceAction(METHOD_POST, URI, data,
									                NOTIFICATION_MSG_STATUS_FALSE)
									          .then(
									                function(response) {
									                   console.log(response);
									                   // $rootScope.startLoader();
									                   if (response.data.status == "success")
									                      return response.data.listObject;
									                });

									}


								$scope.selectDignosisIdForDiagnosis = function(
										info, model, label, ind) {
									$scope.patientDaignosisObj.diagnosisId = info.diagnosisId;
									$scope.patientDaignosisObj.diagnosisName = info.diagnosisName;
								}
								
                                $scope.checkDiagnosisSaved = false;
	                           $scope.savepatientDaignosis = function(obj){
	                        	
	                        	   $rootScope.startLoader();

	                        	   if($scope.checkDiagnosisSaved == true){
	                        	       $rootScope.stopLoader();
                                       return false;
                                   }
                                   $scope.checkDiagnosisSaved = true;

	                        	   if(obj!= undefined && (obj.diagnosisId=="" || obj.diagnosisId==null || obj.diagnosisId==undefined)){
                                      growl.error("Please select any diagnosis", {
                                          title: 'Error!'
                                      });
                                      $rootScope.stopLoader();
                                      $scope.checkDiagnosisSaved = false;
                                      return;
                                  }

                                   if(obj == undefined && ($scope.patientDaignosisObj.diagnosisId=="" || $scope.patientDaignosisObj.diagnosisId==null || $scope.patientDaignosisObj.diagnosisId==undefined)){
                                       growl.error("Please select any diagnosis", {
                                           title: 'Error!'
                                       });
                                       $rootScope.stopLoader();
                                       $scope.checkDiagnosisSaved = false;
                                       return;
                                   }
	                        	   
	                        	   var data = {};
	                        	   if(obj == undefined){
	                        		   
	                        		   data = {
	          									organizationId : $scope.organizationId,
	          									unitId: $scope.unitId,
	          									patientId : $scope.patientDetailsObj.patientId,
	          									encounterId:$scope.patientDetailsObj.encounterId,
	          									admissionId : $scope.patientDetailsObj.admissionId, 
	          									diagnosisType:$scope.patientDaignosisObj.diagnosisType,
	          									createdBy :  $scope.userId,
	          									updatedBy : $scope.userId,
	          									type : $scope.patientDaignosisObj.type,
	          									diagnosisId:$scope.patientDaignosisObj.diagnosisId,
	          									remark : $scope.patientDaignosisObj.remark
	          									};
	                        	   }else{
	                        		   data = {
	                        				   patientDiagnosisServiceMapperId : obj.patientDiagnosisServiceMapperId,
	          									organizationId : $scope.organizationId,
	          									unitId: $scope.unitId,
	          									patientId : $scope.patientDetailsObj.patientId,
	          									encounterId:$scope.patientDetailsObj.encounterId,
	          									admissionId : $scope.patientDetailsObj.admissionId,
	          									diagnosisType:obj.diagnosisType,
	          									createdBy :  $scope.userId,
	          									updatedBy : $scope.userId,
	          									type : obj.type,
	          									diagnosisId:obj.diagnosisId,
	          									remark : obj.remark
	          									};
	                        	   }
	   						 
	   							
	   							var list = [];
	   							list.push(data);
	   							
	   							var URI = BASE_URL + ROOT_URL
	   									+ "/api/opd/savePatientDaignosisServiceMapper";
	   							
	   							return GenericService.serviceAction("POST", URI,list)
	   									.then(
	   											function(response) {
	   												if (response.data.status == "success"){
	   													$scope.initPatientDaignosis();
	   													$scope.checkDiagnosisSaved = false;
	   													$scope.getPatientServiceDaignosisData();
	   													$rootScope.stopLoader();
	   													growl.success(response.data.message,
	   															{
	   																title : 'Success!'
	   															});
	   												}else{
	   													$rootScope.stopLoader();
	   													$scope.checkDiagnosisSaved = false;
	   													growl.error("Something went Wrong.",{title : 'Error!'});
	   												}
	   											});
	                        	   
	                        	   
	                           }
	                          
								$scope.close = function(){
									$("#diagnosisPopRadiology").hide();
								}
					        
							
					       	
						} ]);