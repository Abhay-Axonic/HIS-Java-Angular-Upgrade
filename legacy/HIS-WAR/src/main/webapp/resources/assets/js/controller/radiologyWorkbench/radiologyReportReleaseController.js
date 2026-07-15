'use strict';

/**
 * @Author By  Vivek Satle
 * @name myApp.controller:radiologyWorkbenchController
 * @description #radiologyWorkbenchController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'radiologyReportReleaseController',
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
						'$compile',
						'reportService',
						function($scope, $cookies, $rootScope, GenericService,
								BillingGenericService, $state, $timeout,
								$interval, GenericServiceParamHeader,$compile,reportService) {

							//alert("radiologyWorkbenchController");
							
							var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
							 if(cookieObjectAppConfig == undefined){
							  $state.go('login');
							  return;
							 }
							 else
							  {
							   console.log('cookieObjectAppConfig',cookieObjectAppConfig);
							   
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

							/* init() function for form object create. */
							$scope.init = function() {

								$scope.dateOptions = {
									formatYear : 'yyyy',
									showWeeks : false
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
								$scope.roleId = 2;

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
									id : 1,
									label : "OP"
								}, {
									id : 2,
									label : "IP"
								}, {
									id : 3,
									label : "Day Care"
								}, {
									id : 4,
									label : "ER"
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

								//return false;
								var URI = BASE_URL + ROOT_URL
										+ GETSEARCHEDLISTMODALITY;

								GenericServiceParamHeader
										.serviceAction(METHOD_POST, URI,
												$scope.encounterSearchObject,
												NOTIFICATION_MSG_STATUS_FALSE,
												"", "")
										.then(
												function(response) {
													console.log(response);
													// $rootScope.startLoader();
													$scope.allEncounterList = [];
													if (response.data.status == "success")
														{
														  $scope.allEncounterList = response.data.listObject;
														}
													$rootScope.stopLoader();
														});

							}
							$scope.searchEncounter();

							$scope.clearSearchObject = function() {
								$scope.encounterSearchObject = {
									'organizationId' : $scope.organizationId,
									'unitId' : $scope.unitId,
									'patientId' : 0,
									'serviceId' : 0
								}

								$scope.selectedVisitType = [];
								$scope.selectedModalityList = [];
								
								$scope.patientName = '';
								$scope.uhidNumber = '';

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

							$scope.acceptRequest = function(item) {
								console.log("Accept",item);
								//alert("accept");
								var acceptObj={
										
							    		 currentDate : moment(new Date()).format('YYYY-MM-DD HH:MM:SS'),// new Date().getTime(),
							    		 orderDetailsId : item.orderDetailsId ,
							    		 specialityId :  10,
							    		 updatedBy : $scope.userId
								}
								
								var URI = BASE_URL + ROOT_URL + "/api/opd/acceptRadiologyRequest";
								BillingGenericService.serviceAction(METHOD_POST,URI,acceptObj, NOTIFICATION_MSG_STATUS_TRUE).then(function(response){
									//$scope.encounterList = response.data.listObject;
									if(response.data.status == "success"){
										$scope.init();
										$scope.searchEncounter();
									}
								});
								
							}

							$scope.rejectRequest = function() {
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
										}
										$rootScope.stopLoader();
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
					       	
						} ]);