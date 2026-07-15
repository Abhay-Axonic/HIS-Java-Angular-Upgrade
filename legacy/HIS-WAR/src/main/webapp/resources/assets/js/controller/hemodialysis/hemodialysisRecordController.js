'use strict';

/**
 * @Author By Siddheshwar
 * @name myApp.controller: hemodialysisRecordController
 * @description # hemodialysisRecordController Controller of the myApp
 */
             angular
					.module('myApp')
					.controller(
							'hemodialysisRecordController',
							[
									'$scope',
									'$http',
									'$localStorage',
									'$sessionStorage',
									'$cookies',
									'$rootScope',
									'GenericService',
									'GenericServiceParamHeader',
									'$timeout',
									'growl',
									'reportService',
									'$state','PagerService','$compile','GenericServiceFileUpload','BillingGenericService','$window',
									function($scope, $http, $localStorage, $sessionStorage,
											$cookies, $rootScope, GenericService, GenericServiceParamHeader,$timeout,growl,reportService,
											$state,PagerService,$compile,GenericServiceFileUpload,BillingGenericService,$window) {
										
										
										var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
										 if(cookieObjectAppConfig == undefined){
										  $state.go('login');
										  return;
										 }
										 else
										  {
										   console.log('cookieObjectAppConfig',cookieObjectAppConfig);
										   
										  }
										
											/** Variables for pagination */
											$scope.offset = 0;
											$scope.noOfRecordsPerPage = 10;
											$scope.commonList = [];
											$scope.totalNoOfRecords;
											$scope.searchKeyword;
											$scope.commonListCount;
											
											var cookieObject = $cookies.getObject('cookieObject');
											if(cookieObject == undefined){
												$state.go('login');
												return;
											}
											
											$rootScope.loginpage = true;
											$scope.init = function() {
												var currDate=new Date();
												  $scope.backDate = new Date(currDate.getTime()-1*24*60*60*1000);
												$scope.dateOptions = {
														formatYear : 'yyyy',
														showWeeks : false,
														maxDate : new Date(),
														minDate :  $scope.backDate,
												};

												$scope.openHDStartDatepicker = function() {
													$scope.HDStartDate1.opened = true;
												};
												
												$scope.HDStartDate1 = {
														opened : false
													};
												
												
												// function for initializa clockpicker
												$scope.initClockPicker = function() {
													$('.clockpicker').clockpicker();
												}
												
												var cookieObject = $cookies
														.getObject('cookieObject');
												if (cookieObject == undefined) {
													$state.go('login');
													return;
												}
												$scope.unitId = cookieObject.unitId;
												$scope.organizationId = cookieObject.organizationId;
												$scope.userId = cookieObject.userId;

												$rootScope.loginpage = true;
												$scope.saveBtnFlag = false;
												$scope.updateBtnFlag = true;

												// for popup
												$scope.showModal = false;
												$scope.buttonClicked = "";
												$scope.popUpFlag = true;
												
												var date = new Date();
												$scope.currentDate = moment(date).format('DD-MM-YYYY HH:mm:ss');
												$scope.dialysisObj = {
														dialysisNote :"",
														organizationId:$scope.organizationId,
														unitId:$scope.unitId,
														createdBy:$scope.userId,
														updatedBy:$scope.userId,
														createdDate:$scope.currentDate,
														updatedDate:$scope.currentDate,
														status:'A',
														selectedType:""

												};
												
												
												// Date For From Date on Search
												$scope.vitalSearchEventId="";
											
											//search datepicker start
												$scope.searchDatepickerFromDate =new Date();
												$scope.searchDatepickerToDate =new Date();
												$scope.searchDatepickerToDate1 = {
														opened : false
												}; 
												$scope.searchDatepickerFromDate1 = {
														opened : false
												};

												$scope.searchOpenDatePickerToDate = function() {
													//$scope.searchDatepickerToDate=new Date();
													$scope.searchDatepickerToDate1.opened = true;
											    };

											    $scope.searchOpenDatePickerFromDate = function() {
											    	//$scope.searchDatepickerFromDate =new Date();
													$scope.searchDatepickerFromDate1.opened = true;
											    };
											    
											    $scope.searchFromdateOptions = {
														formatYear : 'yyyy',
														showWeeks : false,
														maxdate:new Date()
													};
											    
											    $scope.searchTodateOptions = {
														formatYear : 'yyyy',
														showWeeks : false,
														maxDate:new Date()
														//minDate:new Date()
													};
											//search date piker end
												
												$scope.datepickerFromDate = new Date();
												$scope.datepickerToDate = new Date();
												$scope.datepickerToDate = {
														opened : false
												}; 
												$scope.datepickerFromDate = {
														opened : false
												};

												$scope.openDatePickerToDate = function() {
													$scope.datepickerToDate.opened = true;
											    };

											    $scope.openDatePickerFromDate = function() {
													$scope.datepickerFromDate.opened = true;
											    };

												$scope.dateOptionsTrend = {
					                                		formatYear : 'yyyy',
					        								showWeeks : false,
					        								maxDate:new Date(), //max 
					        							};
												
											    $scope.dateOptionsTodateTrend = {
				                                		formatYear : 'yyyy',
				        								showWeeks : false,
				        								maxDate:new Date(), //max 
				        							};
											    
												// end for popup
												$scope.docObj={};
												$scope.docObj.file="";
												$scope.date = new Date();
											    $scope.today = moment($scope.date).format('DD-MM-YYYY');
											    $scope.markAsFinalCompletion='N';
												//start radiology
												$scope.visitTypeList = [ {
													id : 3,
													label : " Day Care"
												}, {
													id : 4,
													label : " ER"
												}, 
												{
													id : 2,
													label : " IP "
												},
												{
													id : 1,
													label : " OP "
												}  ];
												$scope.selectedVisitType=[];
												var data = {organizationId : $scope.organizationId};
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
														'serviceId':0,
														'technicianId':0,
														'dialysisStatusId':0,
														'fromDate':"",
														'toDate':""
													}
												//Get dialysis technician list	
												$scope.technicianList =[];
												$scope.getDialysisTechnicianList = function() {
															var data = {
																unitId : $scope.unitId,
																organisationId : $scope.organizationId
															};
															var URI = BASE_URL + ROOT_URL+ "/api/global/getDialysisTechnicianList";
															$scope.technicianList=[];
															GenericService.serviceAction("POST",URI,data)
																	.then(function(response) {
																				if (response.data.status == "success")
																					$scope.technicianList = response.data.listObject;
															 });

														};	
									               $scope.getDialysisTechnicianList();
											}

											$scope.init();
											
											$scope.dialysisObj.hdStartDate=new Date();
											$scope.dialysisObj.hdStartTime="";
											$scope.dialysisObj.hdEndTime = "";
											$scope.dialysisObj.dialysisEventId = "";
											$scope.dialysisObj.dialysisStatusId ="";
											$scope.dialysisObj.dialysisDuration ="";
											$scope.dialysisObj.vascularAccessType="";
											$scope.dialysisObj.ktvResult ="";
											$scope.dialysisObj.netUltrafication ="";
											$scope.searchByFilter='N';
											
											$scope.loadingMsg="";
											$scope.initMasterList = function(organizationId,offset, noOfRecordsPerPage, unitId) {
												$rootScope.startLoader();
												offset = offset != null ? offset : 0;
												noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage:10;
												var visitList =[];
												$scope.dialysisPatientList = [];
												$scope.loadingMsg="Loading...";
												$scope.encounterSearchObject.offset=offset;
												$scope.encounterSearchObject.noOfRecordsPerPage=noOfRecordsPerPage;
												angular.forEach($scope.selectedVisitType,
														function(value,index) {
													         visitList.push(value.id);
														});
											
												/*angular.forEach($scope.selectedHemoChairList,
														function(value,index) {
													      modalityList.push(value.id);
														});*/
												
												$rootScope.startLoader();
												$scope.encounterSearchObject.visitList = visitList;
												$scope.encounterSearchObject.hemodialysisSpecialityId = cookieObjectAppConfig.hemodialysisSpecialityId;

												$scope.encounterSearchObject.technicianAcceptance='A';
												if($scope.searchKeyword!=""){
													$scope.encounterSearchObject.searchKeyword=	$scope.searchKeyword;
												}else{
													$scope.encounterSearchObject.searchKeyword=	null;
												}
												
												
												//if($scope.searchByFilter=='Y'){
													//if(isEmpty($scope.searchDatepickerFromDate)==false && $scope.searchDatepickerFromDate!="" && isEmpty($scope.searchDatepickerToDate)!="" && isEmpty($scope.searchDatepickerToDate)==false){
													if($scope.searchDatepickerFromDate!="" && $scope.searchDatepickerToDate!="" ){	
													$scope.encounterSearchObject.fromDate = moment(
																$scope.searchDatepickerFromDate).format(
																'YYYY-MM-DD');
														$scope.encounterSearchObject.toDate = moment(
																$scope.searchDatepickerToDate).format(
																'YYYY-MM-DD');
													}else{
														$scope.encounterSearchObject.fromDate =null;
														$scope.encounterSearchObject.toDate =null;
													}
													
												/*}else{
													$scope.encounterSearchObject.fromDate =null;
													$scope.encounterSearchObject.toDate =null;
												}*/
												
												
											
												var URI1 = BASE_URL + ROOT_URL + "/api/hemodialysis/getPatientListForDialysisWorkload";
												GenericService.serviceAction("POST", URI1, $scope.encounterSearchObject).then(function(response) {
													$scope.dialysisPatientList=[];
															if (response.data.status == "success")
																{
																angular.forEach(response.data.listObject,function(value,index){
																	value.technicianList=$scope.technicianList;
																	value.dialysisMachineList=$scope.dialysisMachineList;
																	value.technicianAcceptanceFlag=angular.copy(value.technicianAcceptance);
																	$scope.dialysisPatientList.push(value);
																	
																});
																}
															
															if($scope.dialysisPatientList.length==0){
																$scope.loadingMsg="No records found...";
															}
															
															$scope.commonListCount = response.data.object;
															$scope.setPage(1,true);
															$rootScope.stopLoader();
														});
												
											   }
											
											
											//Search Appointment List By Filters
											$scope.searchEncounter = function(organizationId,offset, noOfRecordsPerPage, unitId) {
												console.log($scope.selectedHemoChairList)
												console.log($scope.selectedVisitType);
												 offset = offset != null ? offset : 0;
										         noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage: 10;
										         
												var visitList =[];
												
												angular.forEach($scope.selectedVisitType,
														function(value,index) {
													         visitList.push(value.id);
														});
												
												$rootScope.startLoader();
												$scope.encounterSearchObject.offset=offset;
												$scope.encounterSearchObject.noOfRecordsPerPage=noOfRecordsPerPage;
												$scope.encounterSearchObject.technicianAcceptance='A';
												$scope.encounterSearchObject.visitList = visitList;
												$scope.encounterSearchObject.hemodialysisSpecialityId = cookieObjectAppConfig.hemodialysisSpecialityId;
												
												console.log("cookieObjectAppConfig.hemodialysisSpecialityId"+cookieObjectAppConfig.hemodialysisSpecialityId);

												var paramObj = {};
												var headers = {};

												//return false;
												var URI = BASE_URL + ROOT_URL
														+ "/api/hemodialysis/getPatientListForDialysisWorkload";

												GenericServiceParamHeader
														.serviceAction(METHOD_POST, URI,
																$scope.encounterSearchObject,
																NOTIFICATION_MSG_STATUS_FALSE,
																"", "")
														.then(
																function(response) {
																	console.log(response);
																	// $rootScope.startLoader();
																	$scope.dialysisPatientList = [];
																	if (response.data.status == "success")
																		{
																		angular.forEach(response.data.listObject,function(value,index){
																			value.technicianList=$scope.technicianList;
																			value.dialysisMachineList=$scope.dialysisMachineList;
																			value.technicianAcceptanceFlag=angular.copy(value.technicianAcceptance);
																			$scope.dialysisPatientList.push(value);
																		});
																		}
																	$rootScope.stopLoader();
																		});

											}
											//$scope.searchEncounter(); 

											
											
											
											$scope.pager = {};
											$scope.page;

											$scope.setPage = function(page, flag) {
												if (page < 1 && page > $scope.pager.totalPages) {
													return;
												}
												$scope.pager = PagerService.GetPager(
														$scope.commonListCount, page,
														$scope.noOfRecordsPerPage,
														$scope.unitId);
												if (flag) {
													    $scope.searchEncounter($scope.organizationId,
														$scope.pager.startIndex,
														$scope.pager.pageSize,
														$scope.unitId);
												}
											}

											$scope.initMasterList($scope.organizationId,
											$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);

											$scope.searchGroupList = function(keyword) {

												var data = {
													patientName : keyword,
													unitId : $scope.unitId,
													organizationId : $scope.organizationId
												}
												console.log(data);

											/*	var URI = BASE_URL + ROOT_URL
														+ PATIENTAUTOFILLSEARCH;*/
												var URI = BASE_URL + ROOT_URL
												+ "/api/hemodialysis/searchPatientForDialysis";
												
												return GenericService.serviceAction(METHOD_POST, URI, data)
														.then(
																function(response) {
																	console.log(response);
																	// $rootScope.startLoader();
																	if (response.data.status == "success")
																		return response.data.listObject;
																});

											}
											
											$scope.searchKeyword="";
											$scope.serachPatient= function(searchKeyword){
												//orgId, unitId, offset, noOfRecordsPerPage
												$scope.searchKeyword=searchKeyword;
													$scope.initMasterList($scope.organizationId,
															$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
											}
											
											$scope.searchPatientByFilter= function(){
												$scope.searchByFilter='Y';
												$scope.searchKeyword="";
												/*if($scope.searchDatepickerFromDate=="" || isEmpty($scope.searchDatepickerFromDate)==true 
														|| isEmpty($scope.searchDatepickerFromDate)==true || $scope.searchDatepickerToDate==""){
													$scope.searchDatepickerFromDate="";
													$scope.searchDatepickerToDate="";
												}*/
												if($scope.encounterSearchObject.dialysisStatusId=="" || $scope.encounterSearchObject.dialysisStatusId==0){
													$scope.encounterSearchObject.dialysisStatusId=null;
												}
												
												if($scope.encounterSearchObject.patientId=="" || $scope.encounterSearchObject.patientId==0 || $scope.patientName==null || $scope.uhidNumber==null){
													$scope.encounterSearchObject.patientId=0;
												}
												$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
											}
											
											function isEmpty(obj) {
										  	    for(var key in obj) {
										  	        if(obj.hasOwnProperty(key))
										  	            return true;
										  	    }
										  	    return false;
										  	}
											
											$scope.setNoOfRecords = function() {
												$scope.initMasterList($scope.organizationId,
														$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
											};
										
										  	//Get dialysis template list	
											$scope.dialysisTemplateList =[];
											$scope.getDialysisTemplateList = function() {
														var tempReq = {
															unitId : $scope.unitId,
															orgId : $scope.organizationId,
															templateTypeId : 20
														};
														var URI1 = BASE_URL + ROOT_URL+ "/api/global/templates/bytype";
														$scope.dialysisTemplateList=[];
														GenericService.serviceAction("POST",URI1,tempReq)
																.then(function(response) {
																			if (response.data.status == "success")
																				$scope.dialysisTemplateList = response.data.listObject;
														 });

													};	
								               $scope.getDialysisTemplateList();
								               
								               $scope.setForthTabLevel = function(newTab) {
													$scope.forthTabLevel = newTab;
													if(newTab == 11){
														$("#SaveVital").show();
														$scope.isSetForthTabLevel(11);
														
													}
													if(newTab == 12){
															$scope.searchVitalByDates();
															$scope.onClickSearch();
															$scope.isSetForthTabLevel(12);
													}
													if(newTab == 13){
														$scope.isSetForthTabLevel(13);
												   }
												};
								         

												$scope.isSetForthTabLevel = function(tabNum) {
													return $scope.forthTabLevel === tabNum;
												};
												
												 $scope.firstTabLevel=1;
												  $scope.setFirstTabLevel = function(newTab) {
													  $scope.firstTabLevel = newTab;
														if(newTab==3){
															$scope.setForthTabLevel(11);
														}
												  }
												$scope.isSetFirstTabLevel = function(tabNum) {
													return $scope.firstTabLevel === tabNum;
												};
		//**********************************template functionality start***********************************************
								               $scope.templateTransaction = {
									        			  templateTransactionId : "",
									        			  templateId : "",
									        			  title : "",
									        			  specialityId : "",
									        			  subSpecialityId : "",
									        			  diagnosisId:"",
									        			  templateObject : "",
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
									        			  updatedDate : "",
									        			  diagnosisId :"",
														  mIcpcSystemId:"",
														  mIcpcId:"",
									        }  
								               
								              $scope.template="";  
											$scope.setTemplateObject=function(obj){
												if(obj==""){
													$scope.template="";
												}else{
													$scope.template="";
													$scope.template=JSON.parse(obj);
												}
											}
											

									    	$scope.addTemplate = function()
									    	{
									    		if($scope.template=="")
									    		{
									    			growl.warning("Please Select Template...!",{
														title : ERROR_MSG
													});
									        		return false;
									    		}else{
									    		$scope.template.templateObject = JSON.parse($scope.template.templateObject);
												$scope.obj = $scope.template.templateObject.obj;
												var replacedHtml = $scope.template.templateObject.html.replace(/ng-model="/g,'ng-model="obj.');
												document.getElementById("htmlContent").innerHTML = replacedHtml;
														$('#addviewTemplateModal').modal('show');
														$scope.saveTrasactionFlag = true;
														$scope.updateTrasactionFlag = false;
														var element = angular.element(document.getElementById("htmlContent"));
														   $compile(element)($scope);
									    		}
									    	 
											}
									    	
											$scope.addTemplateFun=function(){
					                        	 $scope.htmlContent=document.getElementById("htmlContent").innerHTML;
												$('#addviewTemplateModal').modal('hide'); 
					                     }
						//***********template functionality end*************************************************
											
											$scope.showPatientHeaderData = function(patientDetailsObj)
											{
												if(patientDetailsObj!=null)
												  {
													//patientDetailsObj.startConsultanceStartTime = moment(new Date()).format('DD-MM-YYYY HH:mm:ss'); 
													console.log("patientDetails",JSON.stringify(patientDetailsObj));
													$scope.showPatientHeader='Y';
													console.log('patientDetailsObj',
															patientDetailsObj);
													$scope.patientDetails = {};
													$scope.patientDetails.patientName = patientDetailsObj.patientName;
													$scope.patientDetails.uhIdNumber = patientDetailsObj.uhidNumber;
													$scope.patientDetails.genderCode = patientDetailsObj.genderCode;
													$scope.patientDetails.age = patientDetailsObj.ageString;
													var splitStrDob=(patientDetailsObj.dob).split(" ");
													var splitDobDate=(splitStrDob[0]).split("-");
													//$scope.patientDetails.birthDate = moment(new Date(patientDetailsObj.dob)).format('DD/MM/YYYY');
													$scope.patientDetails.birthDate = splitDobDate[0]+"/"+splitDobDate[1]+"/"+splitDobDate[2];
													$scope.patientDetails.doctorFullName=patientDetailsObj.admittingDoctorName;
													$scope.patientDetails.patientId = patientDetailsObj.patientId;
													$scope.patientDetails.appointmentId =patientDetailsObj.appointmentId;
													$scope.patientDetails.dialysisAppMapperId=patientDetailsObj.dialysisAppMapperId;
													$scope.patientDetails.dialysisStatusId=patientDetailsObj.dialysisStatusId;
													$scope.patientDetails.orderDetailsId=patientDetailsObj.orderDetailsId;
													$scope.patientDetails.visitTypeId = patientDetailsObj.visitTypeId;
													$scope.patientDetails.specialityId = patientDetailsObj.specialityId;
													$scope.patientDetails.doctorId = patientDetailsObj.doctorId;
													$scope.patientDetails.dateOfAdmission = patientDetailsObj.doa;
													$scope.patientDetails.patientDischargeDate = patientDetailsObj.pdd;
													if(patientDetailsObj.visitTypeId!=1){
													$scope.patientDetails.bedNumber = patientDetailsObj.bedNumber;
													}
													//$scope.patientDetails.doctorFullName = patientDetailsObj.dFirstName;
													$scope.patientDetails.disableDiaStartDateTime=false;
													if(patientDetailsObj.dialysisStartDate!=null){
														$scope.patientDetails.disableDiaStartDateTime=true;
														$scope.dialysisObj.hdStartDate=moment(patientDetailsObj.dialysisStartDate,'DD/MM/YYYY').toDate();
														$scope.dialysisObj.hdStartTime=patientDetailsObj.dialysisStartTime;
													}
													
													$scope.getVitalList();
													$scope.getPatientDialysisRecordByAppointmentId();
													$scope.getListOfEnteredInErrorVital();
												  }
											  else
											     {
												    growl.error("Something went Wrong.",{title : 'Error!'});
											     }
												
													$scope.patientId=patientDetailsObj.patientId;
													$scope.patientDetailsList = [];
													$scope.masterObj = {};
												
											}			
										
						//******************************save Dialysis Record*********************
											$scope.PopupOkBtn = function() {
												$scope.popUpFlag = false;
												$scope.saveDialysisRecord();
											}
											
											function isEmpty(obj) {
									    	    for(var key in obj) {
									    	        if(obj.hasOwnProperty(key))
									    	            return true;
									    	    }
									    	    return false;
									    	}
											
											$scope.saveDialysisRecord=function(){
												try {
													if($scope.dialysisObj.dialysisEventId==3){
														if($scope.dialysisObj.hdEndTime=="" || $scope.dialysisObj.hdEndTime==undefined){
															  growl.warning(
																		'HD end time required',
																		{
																			title : 'Warning!'
																		});
															   return false;
														}
														
														if($scope.dialysisObj.dialysisStatusId!=2 && $scope.dialysisObj.dialysisStatusId!=3 ){
															  growl.warning(
																		'Dialysis status required',
																		{
																			title : 'Warning!'
																		});
															   return false;
														}
													}
													
													if($scope.dialysisObj.dialysisEventId==3){
														if($scope.dialysisObj.vascularAccessType=="" || $scope.dialysisObj.vascularAccessType==undefined){
															growl.error("Vascular access type and location required",{
																title : ERROR_MSG
															});
															return false;
														}
														if($scope.dialysisObj.netUltrafication=="" || $scope.dialysisObj.netUltrafication==undefined){
															growl.error("Net Ultrafiltration(Fluid Removed) required",{
																title : ERROR_MSG
															});
															return false;
														}
														
														if($scope.dialysisObj.ktvResult=="" || $scope.dialysisObj.ktvResult==undefined){
															growl.error("Kt/v required",{
																title : ERROR_MSG
															});
															return false;
														}
													}
													
													if($scope.dialysisObj.dialysisEventId=="" || $scope.dialysisObj.dialysisEventId==undefined || parseInt($scope.dialysisObj.dialysisEventId)==0){
														  growl.warning(
																	'Dialysis event is required',
																	{
																		title : 'Warning!'
																	});
														   return false;
													}
													
													if($scope.dialysisObj.hdStartDate=="" || $scope.dialysisObj.hdStartDate==null || $scope.dialysisObj.hdStartDate==undefined){
														  growl.warning(
																	'HD start date is required',
																	{
																		title : 'Warning!'
																	});
														   return false;
													}
													
													if($scope.dialysisObj.hdStartTime==""){
														  growl.warning(
																	'HD start time is required',
																	{
																		title : 'Warning!'
																	});
														   return false;
													}
													
													var date = new Date();
													$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
													/*$scope.$broadcast('show-errors-check-validity');*/
												/*	if($scope.dialysisForm.$valid==false){
														return false;
													}*/
														if ($scope.popUpFlag) {
															$scope.showModal = !$scope.showModal;
														} else {
											
												$scope.finalDialysisObjForSave = {
														dialysisNote :"",
														organizationId:$scope.organizationId,
														unitId:$scope.unitId,
														createdBy:$scope.userId,
														updatedBy:$scope.userId,
														createdDate:$scope.currentDate,
														updatedDate:$scope.currentDate,
														status:'A',
														selectedType:""

												};
												
												
												
											if($scope.template!=""){
												if($scope.template==""){
												  growl.warning(
															'Template is not added',
															{
																title : 'Warning!'
															});
												   return false;
												}
												var templateTransaction = angular.copy($scope.templateTransaction);
												templateTransaction.templateId = $scope.template.templateId;
												templateTransaction.title = $scope.template.title;
												if($scope.template.specialityId!=null)
												templateTransaction.specialityId = $scope.template.specialityId;
												templateTransaction.templateTransactionId=$scope.template.templateTransactionId==undefined ? 0 :$scope.template.templateTransactionId;
												if($scope.template.subSpecialityId!=null)
												templateTransaction.subSpecialityId = $scope.template.subSpecialityId;
												var dataObj=document.getElementById("htmlContent").innerHTML;
												console.log("sdfsdfdsd",dataObj);
												templateTransaction.templateObject = $scope.template.templateObject;
												//templateTransaction.doctorId = $scope.encounterDetailsObj.doctorId;
												templateTransaction.patientId = $scope.patientId;
												templateTransaction.diagnosisId = 0;
												templateTransaction.mIcpcSystemId = 0;
												templateTransaction.mIcpcId = 0;
										
												if(isEmpty($scope.template.templateObject.obj)) {
													var jsonObject = angular.equals({},
															$scope.template.templateObject.obj) ? JSON
															.parse($scope.template.templateObject.obj)
															: {};
												}
												
												var templateObj = $scope.template.templateObject;
//												templateObj.html=dataObj;
//												templateObj.obj = $scope.obj;
												templateTransaction.jsonObject = JSON.stringify(jsonObject);
												templateTransaction.templateObject = JSON.stringify(templateObj);
												$scope.dialysisObj.templateObject=templateTransaction;
												$scope.finalDialysisObjForSave.templateObject=templateTransaction;
											}else if($scope.dialysisObj.dialysisNote!=""){
												$scope.dialysisObj.templateObject=null;
												if($scope.dialysisObj.dialysisNote==""){
													 growl.warning(
																'Note is required',
																{
																	title : 'Warning!'
																});
													   return false;
												}
												//file validations
												  if (angular.equals($scope.docObj.file, {}) || $scope.docObj.file==undefined || 	$scope.docObj.file=="") {
														growl.warning('Please select file.',
																		{
																			title : 'warning!'
																		});
														return false;
													} 
												  var selectedFile=$scope.docObj.file;
												 var ext = selectedFile.name.split(".").pop().toLowerCase();
													if($.inArray(ext, ["pdf","png","jpeg","jpg"]) == -1) {
														growl.warning('You can only upload  *.pdf, *.png, *.jpeg, *.jpg file',
																{
																	title : 'warning!'
																});
														return false;
													}
											
												
											}
												if($scope.patientDetails.disableDiaStartDateTime==false){
												$scope.finalDialysisObjForSave.dialysisStartDate=$scope.dialysisObj.hdStartDate==""?null:moment($scope.dialysisObj.hdStartDate).format('DD-MM-YYYY HH:mm:ss');
												$scope.finalDialysisObjForSave.dialysisStartTime=$scope.dialysisObj.hdStartTime==""?null:$scope.dialysisObj.hdStartTime;
												}else{
													$scope.finalDialysisObjForSave.dialysisStartDate=null;
													$scope.finalDialysisObjForSave.dialysisStartTime=null;
												}
												
												$scope.finalDialysisObjForSave.dialysisEventId=$scope.dialysisObj.dialysisEventId;
												if($scope.finalDialysisObjForSave.dialysisEventId==3){
													 $scope.finalDialysisObjForSave.vascularAccessType=$scope.dialysisObj.vascularAccessType;
													$scope.finalDialysisObjForSave.ktvResult=$scope.dialysisObj.ktvResult;
													$scope.finalDialysisObjForSave.netUltrafication=$scope.dialysisObj.netUltrafication;
												}
												
												$scope.finalDialysisObjForSave.dialysisEndTime=$scope.dialysisObj.hdEndTime==""?null:$scope.dialysisObj.hdEndTime;
												
												 $scope.dialysisObj.dialysisStatusId=$scope.dialysisObj.dialysisStatusId==""?null:$scope.dialysisObj.dialysisStatusId;
												 $scope.finalDialysisObjForSave.dialysisStatusId=$scope.dialysisObj.dialysisStatusId==""?null:$scope.dialysisObj.dialysisStatusId;
											
												$scope.dialysisObj.dialysisDuration=$scope.dialysisObj.dialysisDuration==""?null:$scope.dialysisObj.dialysisDuration;
												$scope.finalDialysisObjForSave.dialysisDuration=$scope.dialysisObj.dialysisDuration==""?null:$scope.dialysisObj.dialysisDuration;
												
												$scope.dialysisObj.patientId=$scope.patientDetails.patientId;
												$scope.finalDialysisObjForSave.patientId=$scope.patientDetails.patientId;
												
												$scope.dialysisObj.appointmentId=$scope.patientDetails.appointmentId;
												$scope.finalDialysisObjForSave.appointmentId=$scope.patientDetails.appointmentId;
												
												$scope.dialysisObj.dialysisAppMapperId=$scope.patientDetails.dialysisAppMapperId;
												$scope.finalDialysisObjForSave.dialysisAppMapperId=$scope.patientDetails.dialysisAppMapperId;
												var URI = BASE_URL + ROOT_URL+ "/api/hemodialysis/savePatientDialysisRecord";
												$rootScope.startLoader();
												GenericService.serviceAction("POST", URI,$scope.finalDialysisObjForSave).then(function(response) {
													
													 $scope.patientDialysisRecId=response.data.object.patientDialysisRecId;
													 $scope.patDialysisEventId=$scope.dialysisObj.dialysisEventId;
													 $scope.template="";
													var vitalSaveFlag=false;
													angular.forEach($scope.vitalFinalList,function(value,index){
														if(value.vitalValue!=''){
															vitalSaveFlag=true;
														}
													});
													/* vitalValue= $.grep($scope.vitalFinalList, function (mppr) {
								                         return mppr.vitalValue !='';
								                     })[0].vitalValue;*/
													 var promiseForVitalSave={};
													 var promiseForDocUpload={};
													 if(vitalSaveFlag==true){
														 promiseForVitalSave=$scope.savePatientDialysisVitals();
													 }
													 
													 if($scope.dialysisObj.selectedType==2){
															var obj={};
															obj.patientDialysisRecId=response.data.object.patientDialysisRecId;
															obj.orgId=response.data.object.organizationId;
															obj.unitId=response.data.object.unitId;
															obj.file=$scope.docObj.file;
															promiseForDocUpload=$scope.uploadDialysisDocument(obj);
													 }
													
													 $scope.dialysisObj.dialysisNote="";
													// $scope.$broadcast('show-errors-reset');
													 //when only template is fill
													 if(isEmpty(promiseForVitalSave)==false && isEmpty(promiseForDocUpload)==false){
															if (response.data.status == 'success') {
																growl.success("Dialysis Recorded Successfully",
																		{
																			title : 'Success!'
																		});
																$scope.init();
																$scope.getPatientDialysisRecordByAppointmentId();
																/*$scope.initMasterList(
																		$scope.organizationId,
																		$scope.offset,
																		$scope.noOfRecordsPerPage,
																		$scope.unitId);*/
																
															    } else if (response.data.status == 'error') {
																growl.error(response.data.message,
																		{
																			title : 'Error!'
																		});
															   }
													   		$rootScope.stopLoader();
													}else if(isEmpty(promiseForVitalSave)==true && isEmpty(promiseForDocUpload)==true){
														growl.success("Dialysis Recorded Successfully",
																{
																	title : 'Success!'
																});
														var funCallFlag=false;
														promiseForVitalSave.then(function(response) {
															
															if(funCallFlag==false){
																$scope.init();
																$scope.getPatientDialysisRecordByAppointmentId();
															}
															funCallFlag=true;
															/*$scope.initMasterList(
																	$scope.organizationId,
																	$scope.offset,
																	$scope.noOfRecordsPerPage,
																	$scope.unitId);*/
															$rootScope.stopLoader();
														});
														promiseForDocUpload.then(function(response) {
															if(funCallFlag==false){
																$scope.init();
																$scope.getPatientDialysisRecordByAppointmentId();
															}
															funCallFlag=true;
															/*$scope.initMasterList(
																	$scope.organizationId,
																	$scope.offset,
																	$scope.noOfRecordsPerPage,
																	$scope.unitId);*/
															$rootScope.stopLoader();
														});
														
													}else if(isEmpty(promiseForVitalSave)==true){
														promiseForVitalSave.then(function(response) {
															growl.success("Dialysis Recorded Successfully",
																	{
																		title : 'Success!'
																	});
															$scope.init();
															$scope.getPatientDialysisRecordByAppointmentId();
															/*$scope.initMasterList(
																	$scope.organizationId,
																	$scope.offset,
																	$scope.noOfRecordsPerPage,
																	$scope.unitId);*/
															$rootScope.stopLoader();
														  });
													}else if(isEmpty(promiseForDocUpload)==true){
														promiseForDocUpload.then(function(response) {
															growl.success("Dialysis Recorded Successfully",
																	{
																		title : 'Success!'
																	});
															$scope.init();
															$scope.getPatientDialysisRecordByAppointmentId();
															/*$scope.initMasterList(
																	$scope.organizationId,
																	$scope.offset,
																	$scope.noOfRecordsPerPage,
																	$scope.unitId);*/
															$rootScope.stopLoader();
														});
													}
													 
												});
												
												}
												
											     } catch (e) {
													console.log("Exception-", e);
												}
											}
											

											$scope.uploadDialysisDocument = function(Obj)
											    {
											    //$('#uploadModal').modal('hide');
											    var fd = new FormData();
											           fd.append('patientDialysisRecId', Obj.patientDialysisRecId);
											           fd.append('unitId', Obj.unitId);
											           fd.append('organizationId', Obj.orgId);
											           fd.append('file',Obj.file );
											           var URI1 = BASE_URL + ROOT_URL + "/api/hemodialysis/uploadDialysisDocument";
											    return GenericServiceFileUpload.serviceAction("POST",URI1,fd,{ 'Content-Type': undefined}).then(function(response)
											    {
											     return response;
											    });
											   }
											
											
											$scope.processFinalAcceptRequest=function(){
												if($scope.dialysisObj.hdEndTime=="" || $scope.dialysisObj.hdEndTime==null || $scope.dialysisObj.hdEndTime==undefined){
													growl.error("HD End Time required",{
														title : ERROR_MSG
													});
													return false;
												}
												
												if($scope.dialysisObj.dialysisStatusId=="" || $scope.dialysisObj.dialysisStatusId==undefined){
													growl.error("Status is required",{
														title : ERROR_MSG
													});
													return false;
												}
												$('#finalDialysisAcceptanceModal').modal('show'); 
											}
											//final submit button call
											$scope.saveFinalDialysisRecord=function(){
												 $scope.markAsFinalCompletion='Y';
													var date = new Date();
													$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
												 $scope.finalDialysisObjForSave = {
														dialysisNote :"",
														organizationId:$scope.organizationId,
														unitId:$scope.unitId,
														createdBy:$scope.userId,
														updatedBy:$scope.userId,
														createdDate:$scope.currentDate,
														updatedDate:$scope.currentDate,
														status:'A',
														selectedType:""

												};
												 $scope.dateObj = moment(date).format('DD-MM-YYYY');
												 var endTime=($scope.dateObj+" "+$scope.dialysisObj.hdEndTime+":00");
												 $scope.finalDialysisObjForSave.dialysisEndTime=$scope.dialysisObj.hdEndTime==""?null:endTime;
												 $scope.finalDialysisObjForSave.dialysisEventId=3;
												 if($scope.dialysisObj.dialysisStatusId==2)
												   $scope.finalDialysisObjForSave.dialysisStatusId=4;
												 else
													$scope.dialysisObj.dialysisStatusId=3;
												 $scope.finalDialysisObjForSave.orderDetailsId =$scope.patientDetails.orderDetailsId;
												 $scope.finalDialysisObjForSave.patientId=$scope.patientDetails.patientId;
												 $scope.finalDialysisObjForSave.appointmentId=$scope.patientDetails.appointmentId;
												 $scope.finalDialysisObjForSave.dialysisAppMapperId=$scope.patientDetails.dialysisAppMapperId;
													var URI = BASE_URL + ROOT_URL+ "/api/hemodialysis/updateDialysisStatus";
													$rootScope.startLoader();
													GenericService.serviceAction("POST", URI,$scope.finalDialysisObjForSave).then(function(response) {
														growl.success("Hemodialysis procedure completed successfully",
																{
																	title : 'Success!'
																});
														$scope.init();
														$scope.searchPatientByFilter();
														$scope.getPatientDialysisRecordByAppointmentId();
														$rootScope.stopLoader();
													});
											}
										
									$scope.disableSubmitBtn='N';		
									//get patient dialysis recorded details
									$scope.getPatientDialysisRecordByAppointmentId=function(){
										$scope.dialysisPatientNotesDetailList=[];
										$scope.dialysisPatientVitalDetailList=[];
										$scope.dialysisPatientRecDetail={};
										$scope.searcVitalsList=[];
										var data={};
										data.appointmentId=$scope.patientDetails.appointmentId;
										data.patientId=$scope.patientDetails.patientId;
										data.organizationId=$scope.organizationId;
										var URI1 = BASE_URL + ROOT_URL + "/api/hemodialysis/getPatientDialysisRecordDetails";
										GenericService.serviceAction("POST", URI1, data).then(function(response) {
											$scope.dialysisPatientNotesDetailList=response.data.object.dialysisPatientList;
											$scope.dialysisPatientRecDetail=response.data.object.patientDialysisDetails;
											$scope.dialysisPatientVitalDetailList=response.data.object.dialysisVitalList;
											$scope.dialysisPatientRecDetail.duration='';
											
											$scope.disableSubmitBtn='Y';
											if($scope.dialysisPatientRecDetail.dialysisEndTime!=null){
												$scope.disableSubmitBtn='N';
											}
											
											$scope.dialysisObj.dialysisEventId=$scope.dialysisPatientRecDetail.dialysisEventId;
											
											if($scope.dialysisPatientRecDetail.dialysisStatusId==3 || $scope.dialysisPatientRecDetail.dialysisStatusId==4){
												$scope.dialysisObj.dialysisStatusId=$scope.dialysisPatientRecDetail.dialysisStatusId;
												var one_day=1000*60*60*24;
												  // Convert both dates to milliseconds
												 var startDate=($scope.dialysisPatientRecDetail.dialysisStartDate).split("/");
												 var endDate=($scope.dialysisPatientRecDetail.dialysisEndDate).split("/");
												  var date1_ms = new Date(startDate[2]+"-"+startDate[1]+"-"+startDate[0]+" "+$scope.dialysisPatientRecDetail.dialysisStartTime.split(" ")[0]).getTime();
												  var date2_ms = new Date(endDate[2]+"-"+endDate[1]+"-"+endDate[0]+" "+$scope.dialysisPatientRecDetail.dialysisEndTime.split(" ")[0]).getTime();
												  // Calculate the difference in milliseconds
												  var difference_ms = date2_ms - date1_ms;
												  //take out milliseconds
												  difference_ms = difference_ms/1000;
												  var seconds = Math.floor(difference_ms % 60);
												  difference_ms = difference_ms/60; 
												  var minutes = Math.floor(difference_ms % 60);
												  difference_ms = difference_ms/60; 
												  var hours = Math.floor(difference_ms % 24);  
												  var days = Math.floor(difference_ms/24);
												  
												  var duration="";
												  if(days>0){
													  duration=duration+" "+days+" days";
												  }
												  if(hours>0){
													  duration=duration+" "+hours+" hrs";
												  }
												  if(minutes>0){
													  duration=duration+" "+minutes+" min"; 
												  }
												  $scope.dialysisPatientRecDetail.duration=duration;
											}
											
											if($scope.dialysisPatientRecDetail.dialysisEndTime!=null){
												$scope.dialysisObj.hdEndTime=($scope.dialysisPatientRecDetail.dialysisEndTime).split(" ")[0];
											}
											
											if($scope.dialysisPatientRecDetail.dialysisStatusId!=null){
												if($scope.dialysisObj.dialysisStatusId==4){
													$scope.dialysisObj.dialysisStatusId=2;
												}else{
													$scope.dialysisObj.dialysisStatusId=$scope.dialysisPatientRecDetail.dialysisStatusId;
												}
											}
											
											if($scope.dialysisPatientRecDetail.dialysisStartDate!=null){
												$scope.patientDetails.disableDiaStartDateTime=true;
												$scope.dialysisObj.hdStartDate=moment($scope.dialysisPatientRecDetail.dialysisStartDate,'DD/MM/YYYY').toDate();
												$scope.dialysisObj.hdStartTime=($scope.dialysisPatientRecDetail.dialysisStartTime).split(" ")[0];
											}else{
												$scope.dialysisObj.hdStartDate=new Date();
												$scope.dialysisObj.hdStartTime= moment(new Date()).format('HH:mm');;
											}
											
											if($scope.dialysisPatientVitalDetailList!=null){
												if($scope.dialysisPatientVitalDetailList.length>0){
													$scope.searcVitalsList = $scope.dialysisPatientVitalDetailList;
													$scope.patientViatalList=$scope.dialysisPatientVitalDetailList;
												    //$scope.onClickSearch();
												}
										   }
										});
									}		
									
									$scope.getVitalList = function() {
										$scope.startLoader();
										 var headerObj = {
		                                            unit_id : $scope.unitId,
		                                            organization_id : $scope.organizationId
		                                        };

		                                    
		                                    var paramObjForPer = {
		                                    };
		                                    var data ={};
										
										var URI = BASE_URL + ROOT_URL
												+ GETVITALMASTERLIST;
										GenericServiceParamHeader
                                        .serviceAction(METHOD_GET, URI, data,
                                                NOTIFICATION_MSG_STATUS_FALSE,
                                                paramObjForPer, headerObj)
												.then(
														function(response) {
															console.log(response);
															// $rootScope.startLoader();
															if (response.data.status == "success")
																$scope.vitalsList = response.data.listObject;
															console.log("Vital List", $scope.vitalsList);
									                    	
									                    	 var data={
								                    			 	   unitId:$scope.unitId,
								                       				   organizationId:$scope.organizationId,
								                       				   patientId:$scope.patientDetails.patientId,
								                    	 			}
									                               var URI2 = BASE_URL + ROOT_URL
									                               + "/api/opd/getListOfVitalsByPatientIdForCoversheet";
									                        GenericService.serviceAction("POST", URI2,data)
									                            .then(
									                                    function(response) {
									                                        console.log(response);
									                             
									                            
									                            if (response.data.status == "success")
									                                $scope.vitalFinalList = [];
									                                $scope.patientViatalList  = response.data.listObject;
									                                if($scope.forthTabLevel == 12){
									                                	$scope.onClickSearch();
									                                	}
									                                	// $scope.onClick();
									                            console.log("patientVitalList",response.data.listObject);
									                            console.log("vitalList",$scope.vitalsList);
									                            
									                            $scope.vitalsListFOR=[];
																if($scope.showHeadCircuVital == false){
																	angular.forEach( $scope.vitalsList, function( value,key) {
																		if(value.vitalId != 15){
																			$scope.vitalsListFOR.push(value);	
																		}
																	});
																	$scope.vitalsList=[];
																	$scope.vitalsList=$scope.vitalsListFOR;
																}
																
									                             angular.forEach($scope.vitalsList, function(value , key) {
									                            	 var checkFlag = false;
									                                 angular.forEach($scope.patientViatalList, function(value1 , key1) {
									                                     	
									                                	
									                                	 if(value.vitalId == value1.vitalId)
									                                		 {
									                                		 	checkFlag = true;
									                                		 	$scope.newArr = {};
									                                		 	$scope.newArr.vitalId = value.vitalId;
									                                		 	$scope.newArr.minValue = value.minValue;
									                                		 	$scope.newArr.maxValue = value.maxValue;
									                                		 	$scope.newArr.vitalName = value.vitalName;
									                                		 	$scope.newArr.vitalIconPath = value.vitalIconPath;
									                                		 	$scope.newArr.unit = value.unit;
									                                		 	$scope.newArr.vitalValue = value1.value;
									                                		 	$scope.newArr.isReviewedFlag = value1.isReviewedFlag;
									                                		 	$scope.newArr.vitalOrderOpd  = value.vitalOrderOpd;
																				$scope.newArr.checkQualifier = true;
									                                		 	if($scope.roleId == 2)
									                            		 		{
									                                		 		$scope.newArr.appointmentVitalMapperId = value1.appointmentVitalMapperId;
									                            		 		}
									                                		 	
									                                           
									                                		 }
									                                	
									                                 		});
																						if (checkFlag == true) {
																								$scope.vitalFinalList.push($scope.newArr);
																							} else {
																								// alert("fdvfd");
																								var newArr = {};
																								newArr.vitalId = value.vitalId;
										                                            		 	newArr.minValue = value.minValue;
										                                            		 	newArr.maxValue = value.maxValue;
										                                            		 	newArr.vitalName = value.vitalName;
										                                            		 	newArr.vitalIconPath = value.vitalIconPath;
										                                            		 	newArr.unit = value.unit;
										                                            		 	newArr.vitalValue = value.vitalValue;
//										                                            		 	newArr.vitalValue = 0;
										                                            		 	newArr.isReviewedFlag = 'N';
										                                            		 	newArr.vitalOrderOpd  = value.vitalOrderOpd;
										                                            			newArr.checkQualifier = true;
										                                            		 	if($scope.roleId == 2)
									                                            		 		{
										                                            		 		newArr.appointmentVitalMapperId ='';
									                                            		 		}
																								$scope.vitalFinalList.push(newArr);
																							}
									                            	       
									                                });
									                             
									                            
									                             console.log("Final Vital Arr",JSON.stringify($scope.vitalFinalList));
									                             var datalistVital = $scope.vitalFinalList;
																	for (var i = 0; i < datalistVital.length; i++) {
																		$scope.vitalFinalList[i].vitalValue = '';
																	}
									                           
									                                                                                      
									                                    });
									                    
									                    
															
														});
										$scope.stopLoader();
									}
								
									
									$scope.calculateBMI = function()
					                 {
					                   var height = 0.00;
					                   var weight = 0.00;
					                   var bmi = 0.00;
					                   var bsa = 0.00;
					                   
					                    angular.forEach($scope.vitalFinalList, function(value , key) 
					                    {
					                         if(value.vitalId ==2)
					                          {
					                            height = value.vitalValue;
					                          }
					                            if(value.vitalId ==1)
					                             {
					                               weight = value.vitalValue;
					                             }
					                      });
					                      angular.forEach($scope.vitalFinalList, function(value , key) 
							                    {
							                         if(value.vitalId ==8)
							                         {
							                          if(height!=null && height!=0)
							                           {
							                            var ht = height / 100;
							                                     bmi = weight/(ht * ht);
							                                     value.vitalValue = parseFloat(bmi).toFixed(2);
							                                     
								                                     if(bmi <= 18.5){
								                                    	 $scope.bmiStatus='BMI : Underweight';	 
								                                     }
								                                     if(bmi >18.5 && bmi<=24.9){
								                                    	 $scope.bmiStatus='BMI : Normal';	
								                                     }
								                                     if(bmi >=24.9 && bmi<=29.9){
								                                    	 $scope.bmiStatus='BMI : Overweight';	
								                                     }
								                                     if(bmi >=30.0){
								                                    	 $scope.bmiStatus='BMI : Obese';	
								                                     }
							                                     
							                           }
							                         }
							                         
							                         if(value.vitalId ==16)
							                         {
							                         if(height!=null && height!=0)
							                         {

							                         bsa = math.pow(height * weight/3600,1/2);
							                         console.log("Mosteller",bsa)

							                         /* var ht = height / 100;
							                         bsa =0.20247 math.pow(ht,0.725) math.pow(weight ,0.425);*/
							                         console.log("DuBois & DuBois",bsa)

							                         value.vitalValue = parseFloat(bsa).toFixed(3);
							                         }
							                         }
							                        
							                     });
					                     
					                 }
									 
									$scope.checkValueVital = function(index){
			     						
			     						angular.forEach($scope.vitalFinalList,function(value,index1){
											if(index == index1){
												if(value.vitalValue > 0){
												$scope.vitalFinalList[index1].checkQualifier=false;
												}
											}else{
												$scope.vitalFinalList[index1].checkQualifier=true;
											}
										})
			     					}
									
									
									 $scope.calculateTemeperature = function(temperature){
	                    	            	
                    	            	 angular.forEach($scope.vitalFinalList, function(value , key)   {
                    	            		 if(value.vitalId == 6 && value.unit !='F' )
					                         {
                    	            			 if(temperature == 'F'){
                    	            				 if(value.vitalValue != ""){
						                        	 $scope.Temperature = value.vitalValue;
						                        	 value.vitalValueShow = ($scope.Temperature *9/5+32).toFixed(2);
                    	            				 }else{
                    	            					 if(value.vitalValueShow != "" && value.vitalValueShow != undefined ){
                    	            					 $scope.Temperature = value.vitalValueShow;
							                        	 value.vitalValue = (($scope.Temperature-32)*5/9).toFixed(2);
                    	            					 }
                    	            				 }
						                        	 }
                    	            			 
	                    	            		 if(value.vitalId == 6 && value.unitT =='C'){
	                    	            			 if(value.vitalValueShow != "" && value.vitalValueShow != undefined){
	                    	            				 value.vitalValue =((value.vitalValueShow-32)*5/9).toFixed(2); 
	                    	            			 }
	                    	            		 }
					                         }else if (value.vitalId == 6 && value.unit !='C'){
					                        	 if(temperature == 'C'){
					                        	 if(value.vitalValue != ""){
						                        	 $scope.Temperature = value.vitalValue;
						                        	 value.vitalValueShow =($scope.Temperature-32*5/9).toFixed(2);
					                        	 }
					                        	 else{
                	            					 if(value.vitalValueShow != "" && value.vitalValueShow != undefined ){
                	            					 $scope.Temperature = value.vitalValueShow;
						                        	 value.vitalValue =( $scope.Temperature *9/5+32).toFixed(2);
                	            					 }
                	            				 }
					                        	 }
					                        	 if(value.vitalId == 6 && value.unitT == 'F'){
	                    	            			 if(value.vitalValueShow != "" && value.vitalValueShow != undefined){
	                    	            				 value.vitalValue =  (value.vitalValueShow * 9/5+32).toFixed(2);
	                    	            			 }
	                    	            		 }
					                         }
                    	            		
                    	            		
                    	            	 });
                    	            }
									

				                        $scope.getVitalQualifireList = function(vitalId ,index) {
				                        	 $rootScope.startLoaderPopup();
											$scope.vitalId =vitalId;
											$scope.vitalIndex=index;
											var data ={
													organizationId:$scope.organizationId,
													vitalId:vitalId
											};
											
											var URI = BASE_URL + ROOT_URL+ "/api/global/getVitalQualifireListByVitalId";
											GenericService.serviceAction("POST",URI,data).then(function(response){
																console.log(response);
																// $rootScope.startLoader();
																if (response.data.status == "success")
																	$scope.vitalQualifireList = response.data.listObject;
																console.log("vitalQualifireList", $scope.vitalQualifireList);
																 $rootScope.stopLoaderPopup();
															});
										}
				                        
				                        $scope.getListOfQualifiersForPopUp = function(info) {

											 $rootScope.startLoaderPopup();
											var data ={
													organizationId:$scope.organizationId,
													vitalId:info.vitalId,
													appointmentVitalMapperId:info.appointmentVitalMapperId
											};
											
											var URI = BASE_URL + ROOT_URL+ "/api/opd/getQualifiersListForPopUp";
											GenericService.serviceAction("POST",URI,data).then(function(response){
																console.log(response);
																// $rootScope.startLoader();
																if (response.data.status == "success")
																	$scope.listOfQualifiers = response.data.listObject;
																console.log("listOfQualifiers", $scope.listOfQualifiers);
																 $rootScope.stopLoaderPopup();
																
															});
										}
				                        
				                   	 //cancel Vital
										$scope.cancelVital=function(item){
											$scope.appointmentVitalMapperId=item.appointmentVitalMapperId;
											
										}
										
										//List Of Cancelled Vital
										$scope.getListOfEnteredInErrorVital = function() {
											$scope.cancelledDiagnosisList = [];
											var data = {
													organizationId:$scope.organizationId,
								      				unitId:$scope.unitId,
								      				patientId:$scope.patientDetails.patientId,
											};
											var URI = BASE_URL + ROOT_URL
													+ "/api/opd/getListOfEnteredInErrorVitals";
											GenericServiceParamHeader
													.serviceAction(METHOD_POST, URI, data,
															NOTIFICATION_MSG_STATUS_FALSE)
													.then(
															function(response) {
																if (response.data.status == "success") {
																	$scope.enteredInErrorVitalList = response.data.listObject;
																	console.log("enteredInErrorVitalList",$scope.enteredInErrorVitalList);
																}
															});
										}
										//$scope.getListOfEnteredInErrorVital();
										// end List Of Cancelled vital
										
				                        $scope.updateVitalEnteredInError = function(reason){
											$scope.reasonId=reason;
											$rootScope.startLoader();
											try{
//												$rootScope.startLoader();
												var date = new Date();
												$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
												var data={
														updatedBy:$scope.userId,
														updatedDate:$scope.dt,
														isEnteredInError:'Y',
														appointmentVitalMapperId:$scope.appointmentVitalMapperId,
														reasonId:$scope.reasonId,
														
												};
												
												if(reason != null && reason !="" && reason != undefined){
												var URI = BASE_URL + ROOT_URL + "/api/opd/updateVitalForEnteredInError";
												GenericService.serviceAction("POST",URI,data).then(function(response){
													if (response.data.status == 'success')
													{
														$rootScope.stopLoader();
														growl.success(response.data.message,
																{
																	title : 'Success!'
																});
														$scope.reason="";
														  
														  $scope.getListOfEnteredInErrorVital();
														 // $scope.getCurrentAppointmentVitalMapper();
						                                  // $scope.refresh();
						                                   $scope.searchVitalByDates();
//														$scope.$broadcast('show-errors-reset');
													} else if(response.data.status == 'error')
													{
														$rootScope.stopLoader();
														growl.error(response.data.message,
																{
																	title : 'Error!'
																});
													}
												});
											}else{
												growl.warning("Please enter reason to mark the vital as entered in error.",
													{
														title : 'warning!'
													});
												$rootScope.stopLoader();
										}
											}catch(e){
												console.log("Exception",e);
											}
										}
				                        
				                    	$scope.selectedQualiPosition=[];
										$scope.selectedQualiMethod=[];
										$scope.selectedQualiLocation=[];
										$scope.selectedQualiSite=[];
										$scope.selectedQualiQuality=[];
										$scope.selectedQualiCuff=[];
										$scope.selectedVitalQualifierList={};
										$scope.qualifierTemperoryList={};
										

										// for position
										$scope.getVitalsQualifierPosition= function(selectedQualiPosition){

											$scope.selectedQualiPosition1=[];
											$scope.vitalPositionList=[];
											$scope.selectedQualiPosition = selectedQualiPosition;
											angular.forEach($scope.selectedQualiPosition,function(value,index){
											angular.forEach($scope.vitalQualifireList,function(value1,index1){
												angular.forEach(value1.listOfVitalQualifiresMapperForQualifires,function(value2,index2){
													if(index == value2.vitalQualifireId)
														{
														var arrNew = {};
														arrNew.vitalQualifireId=value2.vitalQualifireId;
														arrNew.vitalId=value2.vitalId;
														$scope.selectedQualiPosition1.push(arrNew);
														}
												});
											});
											});
											$scope.selectedVitalQualifierList.vitalPositionList=$scope.selectedQualiPosition1;
//											$scope.qualifierTemperoryList.vitalPositionList=$scope.selectedQualiPosition1;
//											$scope.vitalFinalList[$scope.vitalIndex].selectedVitalQualifierList =$scope.selectedVitalQualifierList;
											$scope.vitalPositionList=$scope.selectedQualiPosition1;
											$scope.vitalFinalList[$scope.vitalIndex].vitalPositionList =$scope.selectedQualiPosition1;
											console.log("vitalQualifierDate",$scope.vitalFinalList)
										}
										
										
										// for Location
										$scope.getVitalsQualifierLocation= function(selectedQualiLocation){
											
											$scope.selectedQualiLocation1=[];
											$scope.vitalLocationList=[];
											$scope.selectedQualiLocation = selectedQualiLocation;
											angular.forEach($scope.selectedQualiLocation,function(value,index){
											angular.forEach($scope.vitalQualifireList,function(value1,index1){
												angular.forEach(value1.listOfVitalQualifiresMapperForQualifires,function(value2,index2){
													if(index == value2.vitalQualifireId)
														{
														var arrNew = {};
														arrNew.vitalQualifireId=value2.vitalQualifireId;
														arrNew.vitalId=value2.vitalId;
														$scope.selectedQualiLocation1.push(arrNew);
														}
												});
											});
											});
//											$scope.selectedVitalQualifierList.vitalLocationList=$scope.selectedQualiLocation1;
//											$scope.qualifierTemperoryList.vitalLocationList=$scope.selectedQualiLocation1;
//											$scope.vitalFinalList[$scope.vitalIndex].selectedVitalQualifierList =$scope.selectedVitalQualifierList;
											
											$scope.vitalLocationList=$scope.selectedQualiLocation1;
											$scope.vitalFinalList[$scope.vitalIndex].vitalLocationList =$scope.selectedQualiLocation1;
											
											console.log("vitalQualifierDate",$scope.vitalFinalList)
										}
										
										
										// for method
										$scope.getVitalsQualifierMethod= function(selectedQualiMethod){
											
											$scope.selectedQualiMethod1=[];
											$scope.vitalMethodList=[];
											$scope.selectedQualiMethod = selectedQualiMethod;
											angular.forEach($scope.selectedQualiMethod,function(value,index){
											angular.forEach($scope.vitalQualifireList,function(value1,index1){
												angular.forEach(value1.listOfVitalQualifiresMapperForQualifires,function(value2,index2){
													if(index == value2.vitalQualifireId)
														{
														var arrNew = {};
														arrNew.vitalQualifireId=value2.vitalQualifireId;
														arrNew.vitalId=value2.vitalId;
														$scope.selectedQualiMethod1.push(arrNew);
														}
												});
											});
											});
											$scope.selectedVitalQualifierList.vitalMethodList=$scope.selectedQualiMethod1;
//											$scope.qualifierTemperoryList.vitalMethodList=$scope.selectedQualiMethod1;
//											$scope.vitalFinalList[$scope.vitalIndex].selectedVitalQualifierList =$scope.selectedVitalQualifierList;
											$scope.vitalMethodList=$scope.selectedQualiMethod1;
											$scope.vitalFinalList[$scope.vitalIndex].vitalMethodList =$scope.selectedQualiMethod1;
											console.log("vitalQualifierDate",$scope.vitalFinalList)
										}
										
										
										//for site
										
										$scope.getVitalsQualifierSite= function(selectedQualiSite){
											
											$scope.selectedQualisite1=[];
											$scope.vitalSiteList=[];
											$scope.selectedQualiSite = selectedQualiSite;
											angular.forEach($scope.selectedQualiSite,function(value,index){
											angular.forEach($scope.vitalQualifireList,function(value1,index1){
												angular.forEach(value1.listOfVitalQualifiresMapperForQualifires,function(value2,index2){
													if(index == value2.vitalQualifireId)
														{
														var arrNew = {};
														arrNew.vitalQualifireId=value2.vitalQualifireId;
														arrNew.vitalId=value2.vitalId;
														$scope.selectedQualisite1.push(arrNew);
														}
												});
											});
											});
											$scope.selectedVitalQualifierList.vitalSiteList=$scope.selectedQualisite1;
//											$scope.qualifierTemperoryList.vitalSiteList=$scope.selectedQualisite1;
//											$scope.vitalFinalList[$scope.vitalIndex].selectedVitalQualifierList =$scope.selectedVitalQualifierList;
											
											$scope.vitalSiteList=$scope.selectedQualisite1;
											$scope.vitalFinalList[$scope.vitalIndex].vitalSiteList =$scope.selectedQualisite1;
											console.log("vitalQualifierDate",$scope.vitalFinalList)
										}
										
										//for Quality
										
			                           $scope.getVitalsQualifierQuality= function(selectedQualiQuality){
											
											$scope.selectedQualiQuality1=[];
											$scope.vitalQualityList=[];
											$scope.selectedQualiQuality = selectedQualiQuality;
											angular.forEach($scope.selectedQualiQuality,function(value,index){
											angular.forEach($scope.vitalQualifireList,function(value1,index1){
												angular.forEach(value1.listOfVitalQualifiresMapperForQualifires,function(value2,index2){
													if(index == value2.vitalQualifireId)
														{
														var arrNew = {};
														arrNew.vitalQualifireId=value2.vitalQualifireId;
														arrNew.vitalId=value2.vitalId;
														$scope.selectedQualiQuality1.push(arrNew);
														}
												});
											});
											});
											$scope.selectedVitalQualifierList.vitalQualityList=$scope.selectedQualiQuality1;
//											$scope.qualifierTemperoryList.vitalQualityList=$scope.selectedQualiQuality1;
//											$scope.vitalFinalList[$scope.vitalIndex].selectedVitalQualifierList =$scope.selectedVitalQualifierList;
											
											$scope.vitalQualityList=$scope.selectedQualiQuality1;
											$scope.vitalFinalList[$scope.vitalIndex].vitalQualityList =$scope.selectedQualiQuality1;
											console.log("vitalQualifierDate",$scope.vitalFinalList)
										}
			                           
			                           
			                           
			                         //for Cuff
										
			                           $scope.getVitalsQualifierCuff= function(selectedQualiCuff){
											
											$scope.selectedQualiCuff1=[];
											$scope.vitalCuffList=[];
											$scope.selectedQualiCuff = selectedQualiCuff;
											angular.forEach($scope.selectedQualiCuff,function(value,index){
											angular.forEach($scope.vitalQualifireList,function(value1,index1){
												angular.forEach(value1.listOfVitalQualifiresMapperForQualifires,function(value2,index2){
													if(index == value2.vitalQualifireId)
														{
														var arrNew = {};
														arrNew.vitalQualifireId=value2.vitalQualifireId;
														arrNew.vitalId=value2.vitalId;
														$scope.selectedQualiCuff1.push(arrNew);
														}
												});
											});
											});
											$scope.selectedVitalQualifierList.vitalCuffList=$scope.selectedQualiCuff1;
//											$scope.qualifierTemperoryList.vitalCuffList=$scope.selectedQualiCuff1;
//											$scope.vitalFinalList[$scope.vitalIndex].selectedVitalQualifierList =$scope.selectedVitalQualifierList;
											$scope.vitalSiteList=$scope.selectedQualisite1;
											$scope.vitalCuffList=$scope.selectedQualiCuff1;
											$scope.vitalFinalList[$scope.vitalIndex].vitalCuffList =$scope.selectedQualiCuff1;
											console.log("vitalQualifierDate",$scope.vitalFinalList)
										}
			                           
			                           
				                        $scope.qulifierOk= function(){
				                        	   $scope.selectedVitalQualifierList = {};
				                        	   $scope.selectedQualiPosition=[];
				   							   $scope.selectedQualiMethod=[];
				   							   $scope.selectedQualiLocation=[];
				   							   $scope.selectedQualiSite=[];
				   							   $scope.selectedQualiQuality=[];
				   							   $scope.selectedQualiCuff=[];
				                           }
				                        
				           //******************************************save patient dialysis vitals**********************************
				                        $scope.isPostFacto='N';
				                         $scope.postFactoTime='';
				                         $scope.postFactoDate='';
				                         
				                        $scope.savePatientDialysisVitals=function(){
				                        	var finalVitalList=[];
				                        	angular.forEach($scope.vitalFinalList,function(value,index){
				                        		var dataVital = {
														vitalId : value.vitalId,
														value : value.vitalValue,
														isReviewedFlag : value.isReviewedFlag,
														vitalMethodList: value.vitalMethodList,
														vitalPositionList: value.vitalPositionList,
														vitalLocationList: value.vitalLocationList,
														vitalSiteList: value.vitalSiteList,
														vitalQualityList: value.vitalQualityList,
														vitalCuffList: value.vitalCuffList,
														
													}
				                        		finalVitalList.push(dataVital);
				                        	});
				                        	
											             var currtimePost = moment().format("HH:mm");
																var vitalDataObj = {
																	patientId : $scope.patientDetails.patientId,
																	appointmentId : $scope.patientDetails.appointmentId,
																	//roleId : $scope.roleId,
																	createdDate : moment(new Date()).format(
																			'DD-MM-YYYY HH:mm:ss'),
																	updatedDate : moment(new Date()).format(
																			'DD-MM-YYYY HH:mm:ss'),
																	updatedBy : $scope.userId,
																	createdBy : $scope.userId,
																	status : 'A',
																	isEnteredInError:'N',
																	isPostFacto:$scope.isPostFacto,
									                               // postFactoDate:moment($scope.postFactoDate).format('DD-MM-YYYY HH:mm:ss'),
									                                postFactoTime:$scope.postFactoTime!="" ? $scope.postFactoTime+":00" :currtimePost,
																	listAppointmentVitalMapperDtos : finalVitalList,
																	dialysisEventId:$scope.patDialysisEventId,
																	patientDialysisRecId:$scope.patientDialysisRecId
																	
																}
																var paramObj = {};
																var headers = {
																	'organization_id' : $scope.organizationId,
																	'unit_id' : $scope.unitId,
																	'Content-Type' : 'application/json'
																};
																	var URI = BASE_URL + ROOT_URL
																			+ SAVEAPPOINTMENTVITALMAPPER + "/"+ $scope.unitId +"/" + $scope.organizationId;
								
																return GenericServiceParamHeader.serviceAction("POST", URI, vitalDataObj,NOTIFICATION_MSG_STATUS_FALSE,paramObj,headers)
																			.then( function(response) {
																							var datalistVital = $scope.vitalFinalList;
																							for (var i = 0; i < datalistVital.length; i++) {
																							$scope.vitalFinalList[i].vitalValue = '';
																							}
																						if (response.data.status == "success")
																							{
																							   $scope.init();
																								$scope.isPostFacto='N';
																		                         $scope.postFactoTime='';
																		                         $scope.postFactoDate='';
																								return response.data.listObject;
																							}
																						
																					});
				                                 }
				                        
				                        $scope.vitalFlag = false;
				                        $scope.calculateHeight = function(Height){
				        	            	var HeightCalculation = 0.00;
				        	            	console.log("ssdsdsdsddsdsad",$scope.unitt);
				        	            		 angular.forEach($scope.vitalFinalList, function(value , key)   {
							                         if(value.vitalId == 2 && value.unit == "cm")
							                         {
								                        	 if(Height == 'cm'){
								                        		 $scope.vitalFlag = false;
								                        	 }
								                        	 else if(Height == 'inch'){
								                        		 if(value.vitalValue != ""){
								                        		 $scope.vitalFlag = true;
								                        		 HeightCalculation = value.vitalValue;
								                        	     value.vitalValueShowHeight = (HeightCalculation / 2.54).toFixed(2);
								                        		 }
								                        	 }
								                        	 else if(Height == "foot"){
								                        		 if(value.vitalValue != ""){
								                        		 $scope.vitalFlag = true;
								                        		 HeightCalculation = value.vitalValue;
										                        value.vitalValueShowHeight =( HeightCalculation/30.48).toFixed(2);
								                        		 }
								                        	 }
								                        	 
								                        	 if(value.vitalId == 2 && value.unitT == 'inch'){
								                        		 $scope.vitalFlag = true;
				                    	            			 if(value.vitalValueShowHeight != "" && value.vitalValueShowHeight != undefined){
				                    	            				 value.vitalValue =  (value.vitalValueShowHeight * 2.54).toFixed(2);
				                    	            			 }
				                    	            		 }
								                        	 if(value.vitalId == 2 && value.unitT == 'foot'){
								                        		 $scope.vitalFlag = true;
				                    	            			 if(value.vitalValueShowHeight != "" && value.vitalValueShowHeight != undefined){
				                    	            				 value.vitalValue =  (value.vitalValueShowHeight * 30.48).toFixed(2);
				                    	            			 }
				                    	            		 }
								                        	 
							                         }else if(value.vitalId == 2 && value.unit == "inch"){
								                        	 if(Height == 'inch'){
								                        		 $scope.vitalFlag = false;
								                        	 }
								                        	 else if(Height == 'cm'){
								                        		 if(value.vitalValue != ""){
								                        		 $scope.vitalFlag = true;
								                        		 HeightCalculation = value.vitalValue;
								                        	     value.vitalValueShowHeight = (HeightCalculation *  2.54).toFixed(2);
								                        		 }
								                        	 }
								                        	 else if(Height == "foot"){
								                        		 if(value.vitalValue != ""){
								                        		   $scope.vitalFlag = true;
								                        		   HeightCalculation = value.vitalValue;
										                           value.vitalValueShowHeight = (HeightCalculation/12).toFixed(2);
								                        		 }
								                        	 }
								                        	 if(value.vitalId == 2 && value.unitT == 'cm'){
								                        		 $scope.vitalFlag = true;
				                    	            			 if(value.vitalValueShowHeight != "" && value.vitalValueShowHeight != undefined){
				                    	            				 value.vitalValue =  (value.vitalValueShowHeight / 2.54).toFixed(2);
				                    	            			 }
				                    	            		 }
							                        	 
								                        	 if(value.vitalId == 2 && value.unitT == 'foot'){
								                        		 $scope.vitalFlag = true;
				                    	            			 if(value.vitalValueShowHeight != "" && value.vitalValueShowHeight != undefined){
				                    	            				 value.vitalValue =  (value.vitalValueShowHeight * 12).toFixed(2);
				                    	            			 }
				                    	            		 }
							                         }else if(value.vitalId == 2 && value.unit == "foot"){
							                        	 
								                        	 if(Height == 'foot'){
								                        		  $scope.vitalFlag = false;
								                        	 }
								                        	 else if(Height == 'inch'){
								                        		 if(value.vitalValue != ""){
								                        		  $scope.vitalFlag = true;
								                        		  HeightCalculation = value.vitalValue;
								                        	      value.vitalValueShowHeight = (HeightCalculation * 12).toFixed(2);
								                        		 }
								                        	 }
								                        	 else if(Height == "cm"){
								                        		 if(value.vitalValue != ""){
								                        		   $scope.vitalFlag = true;
								                        		   HeightCalculation = value.vitalValue;
										                           value.vitalValueShowHeight =( HeightCalculation * 30.48).toFixed(2);
								                        		 }
								                        	 }
								                        	 if(value.vitalId == 2 && value.unitT == 'inch'){
								                        		 $scope.vitalFlag = true;
				                    	            			 if(value.vitalValueShowHeight != "" && value.vitalValueShowHeight != undefined){
				                    	            				 value.vitalValue =  (value.vitalValueShowHeight / 12).toFixed(2);
				                    	            			 }
				                    	            		 }
							                        	 
								                        	 if(value.vitalId == 2 && value.unitT == 'cm'){
								                        		 $scope.vitalFlag = true;
				                    	            			 if(value.vitalValueShowHeight != "" && value.vitalValueShowHeight != undefined){
				                    	            				 value.vitalValue = (value.vitalValueShowHeight / 30.48).toFixed(2);
				                    	            			 }
				                    	            		 }
							                         }
				        	            	});
				        	            }
				                        
				                        
				                        $scope.getVitalListMultiselect = function() {
		                                    $scope.vitalListMultiselect=[];
		                                    $scope.startLoader();
		                                   
		                                    var headerObj = {
		                                            unit_id : $scope.unitId,
		                                            organization_id : $scope.organizationId
		                                        };

		                                    
		                                    var paramObjForPer = {
		                                    };
		                                    var data ={};
										
										var URI = BASE_URL + ROOT_URL
												+ GETVITALMASTERLIST;
										GenericServiceParamHeader
                                        .serviceAction(METHOD_GET, URI, data,
                                                NOTIFICATION_MSG_STATUS_FALSE,
                                                paramObjForPer, headerObj)
		                                            .then(
		                                                    function(response) {
		                                                        console.log(response);
		                                                        // $rootScope.startLoader();
		                                                        if (response.data.status == "success")
		                                                            angular .forEach(response.data.listObject, function( value, index) {
		                                                                var newAr = {};
		                                                                newAr.id = value.vitalId;
		                                                                newAr.label = value.vitalName;
		                                                                $scope.vitalListMultiselect.push(newAr);
		                                                                console.log();
		                                                            });
		                                                        console.log("$scope.vitalListMultiselect",$scope.vitalListMultiselect);
		                                                    });
		                                                        
		                                                    
		                                    $scope.stopLoader();
		                                }
		                                $scope.getVitalListMultiselect();
		                                
		                            	// Search Vital By Dates
										$scope.msg="No records found...!!";
										$scope.selectedVital=[];
										// Search Vital By Dates
										$scope.myChartForSearch = false;
										$scope.searchVitalByDates = function()
										{
											$scope.searcVitalsList=[];
											$scope.myChartForSearch = true;
											$("#SaveVital").show();
											$scope.listVitalId=[];
											angular.forEach($scope.selectedVital, function(value , key) {
												$scope.listVitalId.push(value.id);
											});

											$scope.startLoader();
											var data = {
													
													organizationId: $scope.organizationId,
//													unitId:$scope.unitId,
													patientId:$scope.patientDetails.patientId,
													fromDate:moment($scope.datepickerFromDate).format('YYYY-MM-DD'),
													toDate:moment($scope.datepickerToDate).format('YYYY-MM-DD'),
													listOfSelectedVitals:$scope.listVitalId,
													flag:'N'
											};
											
											if($scope.vitalSearchEventId!=""){
												data.dialysisEventId=$scope.vitalSearchEventId;
											}
											var URI = BASE_URL + ROOT_URL
											+ GETAPPOINTMENTVITALMAPPERBYTRAND;
									
											GenericServiceParamHeader
											.serviceAction(METHOD_POST, URI,
													data,
													NOTIFICATION_MSG_STATUS_FALSE)
											.then(
													function(response) {
														console.log(response);
														// $rootScope.startLoader();
														if (response.data.status == "success")
															$scope.searcVitalsList = response.data.listObject;
															  $scope.onClickSearch();
														console.log("searcVitalsList", $scope.searcVitalsList);
													
													});
										}
										
									
										 $scope.onClickSearch = function () {
											    $scope.vitalnameGraphListForSearch=[];
						                    	 $scope.vitalnameForSearch=[];
						                    	 $scope.createdTimeForSearch=[];
						        				 $scope.vitalDataForSearch=[];
						        				 $scope.vitalTimeForSearch=[];
						        				 $scope.vitalLabelForSearch=[];
						        				 $scope.createdDate=[];
						        				 //for label
						        				 if($scope.searcVitalsList.length > 0){
				     							 angular.forEach($scope.searcVitalsList, function(value,key) {
				     								 $scope.flag=false;
				     								  if($scope.vitalnameGraphListForSearch.length > 0){
				     										angular.forEach($scope.vitalnameGraphListForSearch, function(value3,key) {
				     											if(value3.vitalName == value.vitalName){
				    	     										$scope.flag = true;
				    											}
				     										});
				     									}
				     									 if(!$scope.flag){
															var newArr={};
				     										 newArr.vitalName=value.vitalName 
				     										 newArr.unitDesc=value.unitDesc;
				     										 $scope.vitalnameGraphListForSearch.push(newArr);
				     									 }
				     									 console.log("$scope.vitalnameGraphListForSearch",$scope.vitalnameGraphListForSearch);
				     									 
				     							});
				     							 /*//for time array
				     							 angular.forEach($scope.searcVitalsList, function(value,key) {
				     								 $scope.flagTime=false;
				     								  if($scope.createdTime.length > 0){
				     										angular.forEach($scope.createdDate, function(value3,key) {
				     											if(value3.createdDate == value.createdDate){
				    	     										$scope.flagTime = true;
				    											}
				     										});
				     									}
				     									 if(!$scope.flagTime){
															var newArr={};
				     										 newArr.createdDate=value.createdDate;
				     										 $scope.createdDate.push(newArr);
				     									 }
				     									 
				     							});*/
				     							
				     							 
				     							 angular.forEach($scope.searcVitalsList, function(value,key) {
				     								 $scope.flagTime=false;
				     								  if($scope.createdTimeForSearch.length > 0){
				     										angular.forEach($scope.createdTimeForSearch, function(value3,key) {
				     											if(value3.createdTime1 == value.postFactoTime){
				    	     										$scope.flagTime = true;
				    											}
				     										});
				     									}
				     									 if(!$scope.flagTime){
															var newArr={};
				     										 newArr.createdTime=value.createdDate +"  "+value.postFactoTime;
				     										 newArr.createdTime1=value.postFactoTime;
				     										 newArr.createdDate=value.createdDate;
				     										 $scope.createdTimeForSearch.push(newArr);
				     									 }
				     									 console.log("$scope.createdTimeForSearch",$scope.createdTimeForSearch);
				     									 
				     							});
				     							for (var i = 0; i < $scope.createdTimeForSearch.length; i++) {
													$scope.vitalLabelForSearch.push($scope.createdTimeForSearch[i].createdTime);
												}
				     							console.log("graph vitalLabelForSearch",$scope.vitalLabelForSearch);
				     							
				     							//for data
				     							
				     							 angular.forEach($scope.vitalnameGraphListForSearch, function(value1,index) {
				     								$scope.vitalDataForSearch=[];
				     								 angular.forEach($scope.searcVitalsList, function(value2,index) {
				     									 
				     									if(value1.vitalName == value2.vitalName){
				     										$scope.vitalDataForSearch.push(value2.value);
				     									}
				     								 });
				     								value1.vitalDataForSearch = $scope.vitalDataForSearch;
				     							 });
				     						
				     							 
				     							$scope.colorArray = [];
				     							$scope.colorArray=[ '#803690', '#00ADF9', '#BE2439', '#46BFBD', '#FDB45C', '#006698', '#6A93B0','#992572','#F5FF00','#FF0000','#154889'] ;
				     							console.log("$scope.vitalname",$scope.vitalnameGraphList);
				     							console.log("colorArray",$scope.colorArray);
				     							$scope.dataForDatasetForSearch=[];
				     							angular.forEach($scope.vitalnameGraphListForSearch, function(value,index) {
				     								var newArr = {};
				     								newArr.label = value.vitalName +"  ("+value.unitDesc+") ";
				     								newArr.data = value.vitalDataForSearch;
				     								newArr.backgroundColor =$scope.colorArray[index];
				     								newArr.borderColor =$scope.colorArray[index];
				     								newArr.fill = false;
				     								newArr.borderWidth = 2;
				     								$scope.dataForDatasetForSearch.push(newArr);
				     							});
				     							console.log("$scope.dataForDatasetForSearch",$scope.dataForDatasetForSearch);
						                      var ctx = document.getElementById('myChartForSearch').getContext('2d');
						                      var myChart = new Chart(ctx, {
						                    	  type: 'line',
						                    	  data: {
						                    		    labels: $scope.vitalLabelForSearch,
						                    		    datasets:$scope.dataForDatasetForSearch
						                    		  },
						                    		  options: {
						                    			  title:{
						                    				  display: true,
						                    				  text: 'Vital Graph'
						                    			  },
						                    		        legend: {
						                    		        	 display: true,
						                    		            position: "top"
						                    		        },
						                    		        scales: {
						                    		        	animation:true,
						                    		            yAxes: [{
						                    		                ticks: {
						                    		                    fontColor: "rgba(0,0,0,0.5)",
						                    		                    fontStyle: "bold",
						                    		                    beginAtZero: true,
//						                    		                    maxTicksLimit: 5,
						                    		                    padding: 10
						                    		                },
						                    		                gridLines: {
						                    		                    drawTicks: false,
						                    		                    display: false
						                    		                }

						                    		            }],
						                    		            xAxes: [{
						                    		                gridLines: {
						                    		                    zeroLineColor: "transparent"
						                    		                },
						                    		                ticks: {
						                    		                    padding: 10,
						                    		                    fontColor: "rgba(0,0,0,0.5)",
						                    		                    fontStyle: "bold"
						                    		                }
						                    		            }]
						                    		        }
						                    		    }
						                    	});
						        				 }
						                    };
						                    
						                    $scope.saveTrasactionFlag=true;
											$scope.viewTemplate = function(template) {
												$scope.saveTrasactionFlag = false;
												$scope.updateTrasactionFlag = false;
												$scope.obj = {};
												$('#addviewTemplateModal').modal('show');
												//document.getElementById("htmlContent").innerHTML = template.templateObject;
												document.getElementById("htmlContent").innerHTML = JSON
												.parse(template.htmlTemplateObject).html;
												$scope.obj = JSON
														.parse(template.htmlTemplateObject).obj;
												var element = angular.element(document
														.getElementById("htmlContent"));

												$compile(element)($scope);
												
											}  
											
											
											$scope.openDocument=function(obj){
												window.open(obj.documentPath,'_blank');
											}
											
											$scope.checkHDEndDateTime = function()
											{
												var currDate = new Date();
											     $scope.currentTimeT = moment(currDate).format('HH:mm');
											     $scope.currentDateT=moment(currDate).format('YYYY-MM-DD');
											     if($scope.currentDateT==moment($scope.dialysisObj.hdStartDate).format('YYYY-MM-DD')){
													  if($scope.dialysisObj.hdEndTime <= $scope.currentTimeT && $scope.dialysisObj.hdEndTime > $scope.dialysisObj.hdStartTime)
														 return true;
													  else{
														  $scope.dialysisObj.hdEndTime="";
														  growl.warning("HD end time should be greater than HD start time and less than current time.",
																	{
																		title : 'Warning!'
																	});
														  return false;
													  }
												  }else{
													  if($scope.dialysisObj.hdEndTime <= $scope.currentTimeT)
															 return true;
														  else{
															  $scope.dialysisObj.hdEndTime="";
															  growl.warning("HD end time should be less than current time.",
																		{
																			title : 'Warning!'
																		});
															  return false;
														  }
												  }
											     
										   }
										
											
											$scope.checkHDStartDateTime = function()
											{
												var currDate = new Date();
											     $scope.currentTimeT = moment(currDate).format('HH:mm');
											     $scope.currentDateT=moment(currDate).format('YYYY-MM-DD');
											     
											  if($scope.currentDateT==moment($scope.dialysisObj.hdStartDate).format('YYYY-MM-DD')){
												  if($scope.dialysisObj.hdStartTime<=$scope.currentTimeT)
													 return true;
												  else{
													  $scope.dialysisObj.hdStartTime="";
													  growl.warning("HD start time should be equal to or less than current time.",
																{
																	title : 'Warning!'
																});
													  return false;
												  }
											  }
										}
											
								               
								               $scope.clearSearchObject = function() {
								            	   $scope.init();
													$scope.patientName = '';
													$scope.uhidNumber = '';
													$scope.datepickerFromDate = new Date();
													$scope.datepickerToDate = new Date();
													$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
												}
								               
								               $scope.selectPatientId = function(info, model,
														label) {

													$scope.patientName = info.patientName;
													$scope.uhidNumber = info.uhidNumber;
													$scope.mobileNumber = info.mobileNumber;
													$scope.encounterSearchObject.patientId = info.patientId;
												}
								               
								               
								               //sneha
								               $scope.printPatientDialysisRecord=function()
						             			{
						             				if($scope.patientDetails.appointmentId != null){
						             					var data =  {
						             				            "appName": "HIS",
						             				            "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
						             				            "reportDesignFilePath": "get_hemodialysisRecord.rpt",
						             				            "reportFormat": "PDF",
						             				            "exportFileName": "Certificate",
						             				            "reportParams": [
						             				              {
						             				            	 "paramName": "appointmentid",
													                  "paramType": "Integer",
													                  "paramValue": $scope.patientDetails.appointmentId
													                
						             				              }
						             				            ]
						             				 };
						             					reportService.generate(REPORT_API_URI1,data);
						             				}
						             				else{
						             					growl.error("can not print the report",
						             							{
						             								title : 'Error!'
						             							});
						             					}
						             				
						             		}
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

									
									} ]);
