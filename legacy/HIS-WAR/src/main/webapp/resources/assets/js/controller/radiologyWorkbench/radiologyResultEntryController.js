'use strict';

/**
 * @Author By  Vivek Satle
 * @name myApp.controller:radiologyWorkbenchController
 * @description #radiologyWorkbenchController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'radiologyResultEntryController',
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
						'$compile',
						'reportService',
						'PaginationService',
						'$stateParams',
						function($scope, $cookies, $rootScope, GenericService,
								BillingGenericService, $state, $timeout,
								$interval,growl, GenericServiceParamHeader,$compile,reportService,PaginationService,$stateParams) {

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
							   $scope.isRisPatientDetailsOnFirstPage = cookieObjectAppConfig.isRisPatientDetailsOnFirstPage;
							  }
							 if(cookieObjectAppConfig.radiologySpecialityId!=null || cookieObjectAppConfig.radiologySpecialityId !="" || cookieObjectAppConfig.radiologySpecialityId!=undefined)
								{
									$scope.radiologySpecialityId = cookieObjectAppConfig.radiologySpecialityId;
								}else{
									growl.warning("Radiology department is not configured...!!",{
										title : 'Warning!'
									});
									return false;
								}
							    $scope.radiologySpecialityId = cookieObjectAppConfig.radiologySpecialityId;
								$scope.opdBillingCatId = cookieObjectAppConfig.opdBillingCatId;
								$scope.defaultDiscountOwnership = cookieObjectAppConfig.defaultDiscountOwnership;
								$scope.skipRISReportEntry = cookieObjectAppConfig.skipRISReportEntry;
								$scope.serviceWiseDoctorListReq =  cookieObjectAppConfig.serviceWiseDoctorListReq;
								
								
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
							/*	$scope.openDatePickerReschedule = function() {
									$scope.datepickerReschedule.opened = true;
								};

								$scope.datepickerReschedule = {
									opened : false
								};*/

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
								$scope.unitName = cookieObject.unitNme;
								$scope.roleId = 2;
								
								$scope.isBillManipulationAllowedUserLevel = cookieObject.isBillManipulationAllowedUserLevel;
								$scope.isBillManipulationAllowedAppLevel = cookieObjectAppConfigSecond.isBillManipulationAllowedAppLevel;
								$scope.isBillManipulationAllowed = ($scope.isBillManipulationAllowedUserLevel && $scope.isBillManipulationAllowedAppLevel =='Y') ? true : false;
								if($scope.isBillManipulationAllowed == true){
									$scope.filterDummyService = 1;
								}else{
									$scope.filterDummyService = 2;
								}
								$rootScope.loginpage = true;
								$scope.saveBtnFlag = false;
								$scope.updateBtnFlag = true;

								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup

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
								
								console.log($scope.encounterSearchObject);

								var paramObj = {};
								var headers = {};
								if($scope.encounterSearchObject.patientId==0 || $scope.encounterSearchObject.patientId==undefined || $scope.encounterSearchObject.patientId==""){
									$scope.patientName = '';
									$scope.uhidNumber = '';
								}
								//return false;
								var URI = BASE_URL + ROOT_URL
										+ "/api/opd/RIS/getRadiologyResultEntrylist";

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
		                                                $scope.allEncounterListForDummyFilter=[];
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
							
							
							
							$scope.getToDate=function(){
								$scope.todateOptions.minDate=$scope.datepickerFromDate.getTime();
								
								if(moment($scope.datepickerToDate).isBefore(moment($scope.datepickerFromDate)))
								{
									 $scope.datepickerToDate=new Date($scope.datepickerFromDate.getTime());
								}
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
							$scope.clearSearchObject = function() {
								$scope.encounterSearchObject = {
									'organizationId' : $scope.organizationId,
									'unitId' : $scope.unitId,
									'patientId' : 0,
									'serviceId' : 0
								}

								$scope.selectedVisitType = [];
								$scope.selectedModalityList = [];
								
								$scope.patientName =undefined;
								$scope.uhidNumber = undefined;
								$scope.serviceName = undefined;
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

							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.saveAppointmentSchedule();
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
								$scope.search = '';
								$scope.serviceName = info.serviceStandardName  +" | " +info.serviceStandardCode ;
								
							}
							
							
							  
							/****Template Save*****/
							
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
					    	$scope.viewTemplate = function(orderDetailsId)
					    	{
					    		
					        	$rootScope.startLoader();
					        	var data={
					        		  orgId : $scope.organizationId,
					        		  unitId : $scope.unitId,
					        		  doctorId : 0,
					        		  specialityId : 0,
					        		  templateTypeId : 10,
					        		  orderDetailsId : orderDetailsId,
					        		  patientId : '',
					        		  visitTypeId: '',
					        		  encounterId: ''
					        		}
					        	
					        	console.log('data',JSON.stringify(data));
					        	var URI = BASE_URL + ROOT_URL +GLOBAL+'/petient/quesionnaaire/details';
					    		BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
					    				{
					    	
					    			   if(response.data.status=='success')
					    			      {
					    				   $scope.saveTrasactionFlag = false;
					    				   	$scope.templateTransactionObject = response.data.listObject[0];
					    				   	document.getElementById("htmlContent").innerHTML = JSON.parse($scope.templateTransactionObject.templateObject).html;
					    				   	$scope.obj = JSON.parse($scope.templateTransactionObject.templateObject).obj;
					    				   	$('#questionnaireTemplateModal').modal('show');
					    				   	var element = angular.element(document.getElementById("htmlContent"));
											   $compile(element)($scope);
					    				   	$rootScope.stopLoader();
					    			      }
					    			   else if(response.data.status=='error'){
					    				   $rootScope.stopLoader();
					    			   }
					    		});
								   
								$scope.saveTrasactionFlag = false;
					    	}
					    	
					        $scope.saveTemplateTransaction = function()
					        {
					        		$rootScope.startLoader();
					        		
									var templateTransaction = angular.copy($scope.templateTransaction);
									templateTransaction.templateId = $scope.template.templateId;
									templateTransaction.title= $scope.template.title;
									templateTransaction.specialityId = $scope.radionLogyServiceObj.specialityId;
									templateTransaction.subSpecialityId = '';
									templateTransaction.templateObject = $scope.template.templateObject;
									templateTransaction.encounterId=$scope.radionLogyServiceObj.encounterId;
									templateTransaction.doctorId=$scope.radionLogyServiceObj.doctorId;
									templateTransaction.patientId=$scope.radionLogyServiceObj.patientId;
									templateTransaction.orderDetailsId=$scope.radionLogyServiceObj.orderDetailsId;
									var jsonObject = angular.equals({},$scope.template.templateObject.obj)?JSON.parse($scope.template.templateObject.obj):{};
									var templateObj = JSON.parse($scope.template.templateObject);
									templateObj.obj = $scope.obj;
									templateTransaction.jsonObject = JSON.stringify(jsonObject);
									templateTransaction.templateObject = JSON.stringify(templateObj);
					        	    console.log('templateTransaction',JSON.stringify(templateTransaction));
						    		
					        	    var URI = BASE_URL + ROOT_URL + "/global/template/transaction";
					        	    BillingGenericService.serviceAction(METHOD_POST,URI,templateTransaction,NOTIFICATION_MSG_STATUS_TRUE).then(function(response) {
						    			if (response.data.status == "success")
						    			{
						    				$('#questionnaireTemplateModal').modal('hide');
											$scope.templateList=[];
										}
										$rootScope.stopLoader();
						    		});
					        }
					        
					        /** Template List*/
					        
					       	$scope.getTemplateList = function(radionLogyServiceObj) 
					    	{
					       		$scope.radionLogyServiceObj = radionLogyServiceObj;
					       		console.log('radionLogyServiceObj',JSON.stringify(radionLogyServiceObj));
					       		
					    		$rootScope.startLoader();
					        	var data={
					        		  orgId : $scope.organizationId,
					        		  unitId : $scope.unitId,
					        		  doctorId : $scope.radionLogyServiceObj.doctorId,
					        		  specialityId : $scope.radionLogyServiceObj.specialityId,
					        		  templateTypeId : 10,
					        		  patientId : '',
					        		  visitTypeId: '',
					        		  encounterId: ''
					        		}
					        	
					        	console.log('data',JSON.stringify(data));
					        	var URI = BASE_URL + ROOT_URL +GLOBAL+'/petient/quesionnaaire';
					    		BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
					    				{
					    	
					    			   if(response.data.status=='success')
					    			      {
					    				   $scope.saveTrasactionFlag = true;
					    				   	$scope.templateTransactionObject = response.data.listObject[0];
					    				   	$scope.template = response.data.listObject[0];
					    				   	document.getElementById("htmlContent").innerHTML = JSON.parse($scope.template.templateObject).html;
					    				 	$scope.obj = JSON.parse($scope.template.templateObject).obj;
					    				   	$('#questionnaireTemplateModal').modal('show');
								    		var element = angular.element(document.getElementById("htmlContent"));
											   $compile(element)($scope);
					    				   	$rootScope.stopLoader();
					    			      }
					    			   else if(response.data.status=='error'){
					    				   $rootScope.stopLoader();
					    			   }
					    		});
					    	}
							

					       	//for Radiology Reports
					       	$scope.getReport = function(rediologyObj)
					    	{
					       		$scope.radioLogyReportObj = rediologyObj;
					        	$rootScope.startLoader();
					        	if(rediologyObj.templateTransactionId==null)
					        		{
					        		var data={
							        		  orgId : $scope.organizationId,
							        		  unitId : $scope.unitId,
							        		  doctorId : 0,
							        		  specialityId : 0,
							        		  templateTypeId : 13,
							        		  orderDetailsId : '',
							        		  patientId : '',
							        		  visitTypeId: '',
							        		  encounterId: '',
							        		  serviceId : rediologyObj.serviceId	  
							        		}
							        	console.log('data',JSON.stringify(data));
							        	var URI = BASE_URL + ROOT_URL +'/api/global/templates/byService';
							    		BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
							    				{
							    	
							    			   if(response.data.status=='success')
							    			      {
							    				 	$scope.templateListRadiology = response.data.listObject;
							    				 	$scope.radiologyReport = {};
							    					$('#addtemplateModalRadio').modal('show');
							    				   	$rootScope.stopLoader();
							    			      }
							    			   else if(response.data.status=='error'){
							    				   $rootScope.stopLoader();
							    			   }
							    		});
										   
					        		}
					        	else{
					        		$scope.radiologyReport = {};	
					        		$scope.radiologyReport.templateObject  = rediologyObj.templateObject;
					        		$scope.radioLogyReportObj.templateTransactionId =  rediologyObj.templateTransactionId;
					        		$scope.radiologyReport.templateFormatId  = rediologyObj.templateFormatId;
					        		$scope.radiologyReport.templateId = '';
					        		$scope.radiologyReport.title= '';
					        		
					        		if(rediologyObj.templateFormatId==1)
					        			{
					        			document.getElementById("htmlContentReport").innerHTML = JSON.parse(rediologyObj.templateObject).html;
						        		$scope.obj =JSON.parse(rediologyObj.templateObject).obj;
							    		var element = angular.element(document.getElementById("htmlContentReport"));
										   $compile(element)($scope);
					        			}
					        		else if(rediologyObj.templateFormatId==2)
					        			{
					        			 $scope.value =   rediologyObj.templateObject;
					        			}
					        	  	$('#radiologyReport').modal('show');
			    				   	$rootScope.stopLoader();
					        	    }
					        
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
					       		radiology.isTempFinalRelease='N';
					       		radiology.isResultEntry='Y';
					       	 $state.go("radiologyReport",{
					       		radiologyReportObject :radiology,
					       		searchObj : searchObj
								}); 
					       		/*if (angular.equals({},
										$stateParams.radiologyReportObject)) {
								} else {
									console.log('$stateParams.radiologyReportObject',
											$stateParams.radiologyReportObject);
								}*/
					       		
					       	}
					       	$scope.selectTemplate  = function(radiologyReport)
					    	{
					    		$scope.radiologyReport = radiologyReport;
					    	}
					       	$scope.addTemplate = function()
					    	{
					       		if($scope.radiologyReport!=null && $scope.radiologyReport!=undefined)
					       			{
					       			$scope.radioLogyReportObj.templateFormatId = 	$scope.radiologyReport.templateFormatId;
			    				   	if($scope.radiologyReport.templateFormatId==1)
			    				   		{
			    				   	 	$scope.radiologyReport.templateObject  = response.data.listObject[0].templateObject;
			    				     	$scope.radiologyReport.templateFormatId  = $scope.radiologyReport.templateObject.templateFormatId;
				    				   	document.getElementById("htmlContentReport").innerHTML = JSON.parse($scope.radiologyReport.templateObject).html;
							    		var element = angular.element(document.getElementById("htmlContentReport"));
										   $compile(element)($scope);
			    				   		}
			    				    else if($scope.radiologyReport.templateFormatId==2)
			    			    	  {
			    				    	 $scope.value =   $scope.radiologyReport.templateObject;
			    			    	  }
			    				   	$('#addtemplateModalRadio').modal('hide');
			    				   	$('#radiologyReport').modal('show');
					       			}
					       		else{
						    			growl.warning("Please select Template...!",{
											title : ERROR_MSG
										});
						        		return;
					       		}
					        	
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
					       	
					        $scope.updateReport = function()
					        {
					        		$rootScope.startLoader();
					        		
									var templateTransaction = angular.copy($scope.templateTransaction);
									templateTransaction.templateTransactionId = $scope.radioLogyReportObj.templateTransactionId;
									templateTransaction.templateId = $scope.radiologyReport.templateId;
									templateTransaction.title= $scope.radiologyReport.title;
									templateTransaction.specialityId = $scope.radioLogyReportObj.specialityId;
									templateTransaction.subSpecialityId = '';
									templateTransaction.templateObject = $scope.radiologyReport.templateObject;
									templateTransaction.encounterId=$scope.radioLogyReportObj.encounterId;
									templateTransaction.doctorId=$scope.radioLogyReportObj.doctorId;
									templateTransaction.patientId=$scope.radioLogyReportObj.patientId;
									templateTransaction.orderDetailsId=$scope.radioLogyReportObj.orderDetailsId;
									if($scope.radiologyReport.templateFormatId==1)
									{	
									var jsonObject = angular.equals({},$scope.radiologyReport.templateObject.obj)?JSON.parse($scope.radiologyReport.templateObject.obj):{};
									var templateObj = JSON.parse($scope.radiologyReport.templateObject);
									templateObj.obj = $scope.obj;
									templateTransaction.jsonObject = JSON.stringify(jsonObject);
									templateTransaction.templateObject = JSON.stringify(templateObj);
									}
									else if($scope.radiologyReport.templateFormatId==2) {
										templateTransaction.templateObject = $scope.value;
									}
					        	    console.log('templateTransaction',JSON.stringify(templateTransaction));
						    		
					        	    var URI = BASE_URL + ROOT_URL + "/global/template/transaction";
					        	    BillingGenericService.serviceAction(METHOD_PUT,URI,templateTransaction,NOTIFICATION_MSG_STATUS_TRUE).then(function(response) {
						    			if (response.data.status == "success")
						    			{
						    				$('#radiologyReport').modal('hide');
						    				$('#addtemplateModalRadio').modal('hide');
											$scope.templateList=[];
											$scope.radiologyReport = {};
											$scope.searchEncounter();
											 $rootScope.stopLoader();
										}
										else{
										    $rootScope.stopLoader();
										}
						    		});
					        }
					        $scope.printReport=function(obj)
					    	{
					    		
					    			var data =  {
					    		            "appName": "HIS",
					    		            "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
					    		            "reportDesignFilePath": "RadiologyReport.rpt",
					    		            "reportFormat": "PDF",
					    		            "exportFileName": "RadiologyReport",
					    		            "reportParams": [
					    		              {
					    			             "paramName": "template_transaction_id",
					    			             "paramType": "Integer",
					    			             "paramValue": obj.templateTransactionId
					    		              }
					    		            ]
					    		 };
					    	  reportService.generate(REPORT_API_URI1,data);
					    	}
					        
					        $scope.updateProvisionalReport=function(){
					        	
					        }
					        
					        $scope.updateTemplate=function(info){
					        	var data={
					        			templateTransactionId:info.templateTransactionId
					        			
					        	}
					        	 var URI = BASE_URL + ROOT_URL + "/global/template/transaction/update";
					        	    BillingGenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_TRUE).then(function(response) {
						    			if (response.data.status == "success")
						    			{						    				
											/*$scope.templateList=[];
											$scope.radiologyReport = {};
											$scope.searchEncounter();*/
						    				$scope.searchEncounter();
						    				$rootScope.stopLoader();
										}
										else{
										    $rootScope.stopLoader();
										}

						    		});
					        }
					        
					        
                           $scope.printRadiologyReport=function(info,printWithHeader){
					        	if(info.isHtmlPrint==true){
					        	info.organizationId = $scope.organizationId;
					        	info.unitId =  $scope.unitId;
					        	info.printWithHeader = printWithHeader;
					        	info.unitName =  $scope.unitName;
					        	var infoCopy = info;
					        	if(infoCopy.docList != undefined){
                                    delete infoCopy.docList;
                                }
					        	var URI = BASE_URL + ROOT_URL + "/api/opd/RIS/printReportTest";
					    		BillingGenericService.serviceAction(METHOD_POST, URI, infoCopy,NOTIFICATION_MSG_STATUS_FALSE).then(function(response)
					    				{
					    	
					    			   if(response.data.status=='success')
					    			      {
					    				   console.log("gg",response.data.object);
					    				   //'<iframe src="https://s3.ap-south-1.amazonaws.com/his-docs/apollohospitals1/patient/documents/clinical/episodic-documents/op-coversheet/1_1_null_Sign.png" style="margin-left:462px;" width="49%" height="15%"></iframe>'+ 
					    				   /*var divContents = response.data.object.latterHeadImg + response.data.object.header+response.data.object.templateContent+response.data.object.footer;  
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
	                                                    +'.page-header, .page-header-space {height:'+headerSize+'}'
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


					        $scope.printRadiologyReportOLD=function(info){
					        	
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
							   /*  hiddenField1.setAttribute("type", "hidden");
							     hiddenField1.setAttribute("name", "templateContent");
							     hiddenField1.setAttribute("value",info.templateContent);
*/							     form.appendChild(hiddenField1);
							     
							     
							     
							     document.body.appendChild(form);
							     $("form[name='Radiology Report']").submit();
							
					        }
                            //For Form F Start
                            
                            
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
                            
                            
                            $scope.fifthTabLevel = 1;

                            $scope.setFifthTabLevel = function(newTab) {
                                $scope.fifthTabLevel = newTab;
                            };

                            $scope.isSetFifthTabLevel = function(tabNum) {
                                return $scope.fifthTabLevel === tabNum;
                            };
                            
                            $scope.redirectFormFObject = {};
                            //For Test Templ
                            $scope.getConsentTemp = function(item){
                                $scope.redirectFormFObject = item;
                                $scope.selectedPatientId = item.patientId;
                                $scope.selectedAppointmentId = item.appointmentId;
                             var data={
                                     //appointmentId : item.appointmentId,
                                      consentTypeId : 1000,
                                      encounterId : item.encounterId,
				    				   admissionId : item.admissionId
                                    }
                             
                             
                             $scope.templateObj = { 
                                     "consentTransactionId":"",
                                        "consentMasterId":"",
                                        "admissionId":null,
                                        "encounterId":"",
                                        "templateDesc":"",
                                        "status":"A",
                                        "createdBy":$scope.userId,
                                        "createdDate":"",
                                        "updatedBy":$scope.userId,
                                        "updatedDate":"",
                                        "unitId":$scope.unitId,
                                        "organizationId":$scope.organizationId,
                                        "consentTypeId":"1000",
                                        "appointmentId" : item.appointmentId,
                                        "encounterId" : item.encounterId,
					    				"admissionId" : item.admissionId
                                    }
                            // $scope.selectedTemplates = [];
                             
                             
                                $rootScope.startLoader();
                                var URI = BASE_URL + ROOT_URL + "/adt/consentTemplate";
                                GenericService.serviceAction(METHOD_POST,URI,data).then(function(response) {
                                    if (response.data.status == "success")
                                    {
                                        $scope.templateList = [];
                                         $scope.templateList = response.data.listObject;
                                         $('#concentTemplateOPD').modal('show');
                                        $rootScope.stopLoader();
                                    }
                                    else if(response.data.status == "error")
                                        {
                                        $rootScope.stopLoader();
                                        }
                                    
                                });
                            }

                            $scope.openRetestPopup = function(item){
                                $scope.retestItem = item;
                                $scope.retestRemark = "";
                                angular.element('#forRetest').modal('show');
                            }

                            $scope.sendToRetest = function(retestRemark,item){

                                if(retestRemark=="" || retestRemark==null || retestRemark==undefined){
                                    growl.error("Please fill up reason", {
                                        title: 'Error!'
                                    });
                                    $rootScope.stopLoader();
                                    return;
                                }

                                $rootScope.startLoader();
                                item.redoReason = retestRemark;
                                console.log("item....",item);
                                var URI = BASE_URL + ROOT_URL + "/api/opd/retestRadiologyResultEntry";
                                GenericService.serviceAction(METHOD_POST,URI,item).then(function(response) {
                                    if (response.data.status == "success")
                                    {
                                        $rootScope.stopLoader();
                                        $scope.closePop();
                                        $scope.searchEncounter();
                                        growl.success(response.data.message,
                                         {
                                            title : 'Success!'
                                         });
                                    }
                                    else if(response.data.status == "error")
                                    {
                                        $rootScope.stopLoader();
                                    }

                                });
                            }

                            $scope.closePop = function(){
                              $scope.retestRemark = "";
                              $("#forRetest").modal("hide");
                            }
                            
                            $scope.viewTemplate  = function(templateObj,index)
                            {
                                
                            
                                $scope.FroalaEditor.html.set('');
                                    $scope.value = templateObj.templateDesc;
                                    $scope.selectedForm.templateDesc = templateObj.templateDesc;
                                    $scope.FroalaEditor.html.insert($scope.selectedForm.templateDesc);
                                    $scope.templateIndex = index;
                                    $('#consentEditPopup').modal('show');
                            
                                
                                
                            }
                            
                            
                            $scope.redirectFunctionToForm = function(item,idx){
//                                /$scope.redirectFormFObject
                                $scope.selectedForm = item;
                                item.patientId = $scope.selectedPatientId;
                            	item.encounterId = $scope.redirectFormFObject.encounterId;
								item.admissionId  =  $scope.redirectFormFObject.admissionId;
								item.orderDetailsId  = $scope.redirectFormFObject.orderDetailsId;
                                    $rootScope.startLoader();
                                    var URI = BASE_URL + ROOT_URL + "/api/opd/appendCensentData";
                                    GenericService.serviceAction(METHOD_POST,URI,item).then(function(response) {
                                        if (response.data.status == "success")
                                        {
                                            
                                            $rootScope.stopLoader();
                                            item.templateDesc = response.data.object.templateDesc;
                                            $scope.selectedForm.templateDesc = item.templateDesc;
                                            $scope.viewTemplate(item,idx);
                                             /*angular.element('#concentTemplateOPD').modal('hide');
                                             $(".modal-backdrop").hide();
                                             
                                             
                                             
                                            console.log($scope.redirectFormFObject);
                                            $state.go("formF", {
                                                itemObj: item,
                                            });*/
                                        }
                                        else if(response.data.status == "error")
                                        {
                                            $rootScope.stopLoader();
                                        }
                                        
                                    });
                                
                            }
                            
                            $scope.redirectFunctionToFormSelfId = function(item,idx){
                                
                                 angular.element('#concentTemplateOPD').modal('hide');
                                 $(".modal-backdrop").hide();
                                
                                item.patientId = $scope.selectedPatientId;
                            	item.encounterId = $scope.redirectFormFObject.encounterId;
								item.admissionId  =  $scope.redirectFormFObject.admissionId;
								item.orderDetailsId  = $scope.redirectFormFObject.orderDetailsId;
                                
                                $rootScope.startLoader();
                                var URI = BASE_URL + ROOT_URL + "/api/opd/appendCensentData";
                                GenericService.serviceAction(METHOD_POST,URI,item).then(function(response) {
                                    if (response.data.status == "success")
                                    {
                                        
                                        $rootScope.stopLoader();
                                        item.templateDesc = response.data.object.templateDesc;
                                        //$scope.selectedForm.templateDesc = item.templateDesc;
                                        //$scope.viewTemplate(item,idx);
                                        //console.log($scope.redirectFormFObject);
                                        item.appointmentId =$scope.selectedAppointmentId;
                                        $state.go("formFSelfId", {
                                            itemObj: item,
                                        });
                                    }
                                    else if(response.data.status == "error")
                                    {
                                        $rootScope.stopLoader();
                                    }
                                    
                                });
                            }

                            $scope.printTemplate = function()
                            {
                                console.log("hh",$scope.FroalaEditor.html.get(true));
                                 
                                    var templateSaveList = [];
                                                    var obj = angular.copy($scope.templateObj);
                                                    obj.consentTransactionId = $scope.selectedForm.consentTransactionId;
                                                    obj.consentMasterId =$scope.selectedForm.consentMasterId; 
                                                    obj.templateDesc =$scope.FroalaEditor.html.get(true)//$scope.selectedForm.templateDesc;
                                                    templateSaveList.push(obj);
                                        
                                     $rootScope.startLoader();
                                     $('#concentTemplateOPD').modal('hide');
                                     $('#consentEditPopup').modal('hide');
                                        var URI = BASE_URL + ROOT_URL + "/adt/patientConsent";
                                        GenericService.serviceAction(METHOD_POST,URI,templateSaveList).then(function(response) {
                                            if (response.data.status == "success")
                                            {
                                                $rootScope.stopLoader();
                                                    
                                                        var divContents = $scope.FroalaEditor.html.get(true);  
                                                        var printWindow = window.open('', '', 'height=500,width=700');  
                                                        printWindow.document.write('<html><head><title>FORM F</title>');  
                                                        printWindow.document.write('</head><body >');  
                                                        printWindow.document.write(divContents);  
                                                        printWindow.document.write('</body></html>');  
                                                        printWindow.document.close();  
                                                        printWindow.print();  
                                                        
                                                    
                                                     /*var data =  
                                                      {
                                                            "appName": "HIS",
                                                            "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
                                                            "reportDesignFilePath": "getConsent.rpt",
                                                            "reportFormat": "PDF",
                                                            "exportFileName": "consentRpt",
                                                            "reportParams": [
                                                              {
                                                                 "paramName": "admission_id",
                                                                 "paramType": "Integer",
                                                                 "paramValue": $scope.itemSelecteds.admissionId
                                                              },
                                                              {
                                                                     "paramName": "consent_master_id",
                                                                     "paramType": "Integer",
                                                                     "paramValue": value.consentMasterId
                                                               },
                                                               {
                                                                     "paramName": "consent_type_id",
                                                                     "paramType": "Integer",
                                                                     "paramValue": 1//admission consent
                                                               }
                                                            ]
                                                         };
                                                     reportService.generate(REPORT_API_URI1,data);*/
                                                    
                                            }
                                            else if(response.data.status == "error")
                                                {
                                                $rootScope.stopLoader();
                                                }
                                        });
                                    
                                     
                                
                                
                            }
                            
                            //For Form F End
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
		
							   $scope.checkServiceWiseDoctorsList = function(info, idx){
								    $scope.selectedInfo = info;
								    $scope.selectedInfoOld = info;
									$scope.selectedIndex = idx;
									$scope.selectedIndexOld = idx;
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
							
							$scope.CloseDoctorShareRequest = function(){
							   $scope.searchEncounter();
								 $("#confirmSaveSendDoctorShare").hide();
							}
					       	
						} ]);