'use strict';

/**
 * @Author By  
 * @name myApp.controller:procedureWorkbenchController
 * @description #radiologyWorkbenchController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'procedureWorkbenchController',
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
						'growl',
						'$window',
						function($scope, $cookies, $rootScope, GenericService,
								BillingGenericService, $state, $timeout,
								$interval, GenericServiceParamHeader,$compile,reportService,growl,$window) {

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
								if (cookieObjectAppConfigSecond != undefined) {
								$scope.mandatoryScan = cookieObjectAppConfigSecond.mandatoryScan;
								}
								$scope.isPreOrPostBilling = cookieObjectAppConfig.isPreOrPostBilling;
								
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
								

								$scope.dateOptionsSlot = {
										formatYear : 'yyyy',
										showWeeks : false,
										minDate : new Date()
									};
								
									$scope.openDatePicker = function() {
										$scope.datepicker.opened = true;
									};

									$scope.datepicker = {
										opened : false
									};
									
									$scope.datepicker = new Date();

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
								$scope.isBillManipulationAllowedUserLevel = cookieObject.isBillManipulationAllowedUserLevel;
								if (cookieObjectAppConfigSecond != undefined) {
								$scope.isBillManipulationAllowedAppLevel = cookieObjectAppConfigSecond.isBillManipulationAllowedAppLevel;
								}
								$scope.isBillManipulationAllowed =  ($scope.isBillManipulationAllowedUserLevel && $scope.isBillManipulationAllowedAppLevel =='Y') ? true : false;
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

								$scope.searchKeyword ="";
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
								},  ];
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

								$scope.searchObject = {
									'patientId' : 0,
									'organizationId' : $scope.organizationId,
									'unitId' : $scope.unitId,
									'serviceId':0
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
							
							//search patient
							$scope.searchGroupList = function(keyword) {
								
								var data = {
									patientName : keyword,
									unitId : $scope.unitId,
									organizationId : $scope.organizationId
								}

								var URI = BASE_URL + ROOT_URL+ PATIENTAUTOFILLSEARCH;

								return BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE)
										.then(function(response) {
													if (response.data.status == "success")
														return response.data.listObject;
												});

							}

							// select searched patient
							$scope.selectPatientId = function(info, model,label) {

								$scope.patientName = info.patientName;
								$scope.uhidNumber = info.uhidNumber;
								$scope.mobileNumber = info.mobileNumber;
								$scope.searchObject.patientId = info.patientId;
							}
							
							// search services
							$scope.search="";
							$scope.searchServices = function(searchKeyword){
								$scope.servicesList=[];
								
								
								var serData = {
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
										searchKeyword : searchKeyword
								}
								var URI = BASE_URL + ROOT_URL + "/global/serviceMaster";
								
								return BillingGenericService.serviceAction(METHOD_POST,URI,serData, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
									if(response.data.status == "success"){
										$scope.servicesList = response.data.listObject;
									}
									return $scope.servicesList;
								});
							}
							
							// select searched searvice
							$scope.addServiceToTempServiceList = function($item, $model, $label){
								$scope.searchObject.serviceId = $item.serviceMasterId;
								
							}

					// for getting  worklist start 
							
					$scope.getworkbenchList=function(){						
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
						$scope.searchObject.fromDate = moment($scope.datepickerFromDate).format('YYYY-MM-DD');
						$scope.searchObject.toDate = moment($scope.datepickerToDate).format('YYYY-MM-DD');						
						$scope.searchObject.visitList = visitList;		
						$scope.searchObject.modalityList = modalityList;		

						if($scope.searchObject.patientId==0 || $scope.searchObject.patientId==undefined || $scope.searchObject.patientId==""){
							$scope.patientName = '';
							$scope.uhidNumber = '';
						}						
						
						var URI = BASE_URL + ROOT_URL+ "/api/dignostic/workbench";
						GenericService.serviceAction("POST", URI, $scope.searchObject).then(function(response) {
											$scope.workBenchList = [];

											if (response.data.status == "success") {
												$scope.workBenchList = [];
												$scope.workBenchListTest =[];
												$scope.workBenchListTest = response.data.listObject;
												
												if($scope.staffSubSpecialityList != null && $scope.staffSubSpecialityList.length > 0 ){
													angular.forEach($scope.workBenchListTest, function(value, key) {
														angular.forEach($scope.staffSubSpecialityList ,function(value1,key1){
															if(value.subSpecialityId == value1.subSpecialityMasterId){
																$scope.workBenchList.push(value);
															}
														
														});		
													 });
												}else{
													$scope.workBenchList = response.data.listObject;
												}
												$rootScope.stopLoader();
												
												$scope.workBenchListForFiltter =$scope.workBenchList ;
												$scope.filterByDummyServices();
												
											}else{
												$rootScope.stopLoader();
											}
										});
					}
					$scope.getworkbenchList();
					
					$scope.filterByDummyServices= function()
                    {
						$scope.workBenchList  =[]
                        if($scope.filterDummyService==1)//all 
                        {
                        	$scope.workBenchList = angular.copy($scope.workBenchListForFiltter);
                        	
                        }else if($scope.filterDummyService==2)//real
                        {
                            angular.forEach($scope.workBenchListForFiltter, function (value, key) 
                            {
                                if(value.isDummyService==false)
                                	$scope.workBenchList.push(value);
                            });
                             
                            
                        }else if($scope.filterDummyService==3 )//dummy
                        {
                        	  angular.forEach($scope.workBenchListForFiltter, function (value, key) 
                                      {
                                          if(value.isDummyService==true)
                                        	  $scope.workBenchList.push(value);
                                      });
                                       
                        }
                    }
					
					
					
					// clear search criteria
					$scope.clearSearchObject = function() {
						$scope.searchObject = {
							'organizationId' : $scope.organizationId,
							'unitId' : $scope.unitId,
							'patientId' : 0,
							'serviceId' : 0
						}

						$scope.selectedVisitType = [];
						$scope.selectedModalityList = [];
						$scope.patientName = undefined;
						$scope.uhidNumber = undefined;
						$scope.search = undefined;
						$scope.datepickerFromDate = new Date();
						$scope.datepickerToDate = new Date();
					}
					
//					=========================================Questionannary STARTt=================================================
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
					 /** Template List*/
			        
			       	$scope.getTemplateList = function(procedureServiceObj) 
			    	{
			       		$scope.procedureServiceObj = procedureServiceObj;
			       		console.log('procedureServiceObj',JSON.stringify(procedureServiceObj));
			       		
			    		$rootScope.startLoader();
			        	var data={
			        		  orgId : $scope.organizationId,
			        		  unitId : $scope.unitId,
			        		  doctorId : $scope.procedureServiceObj.doctorId,
			        		  specialityId : $scope.procedureServiceObj.specialityId,
			        		  subSpecialityId : $scope.procedureServiceObj.subSpecialityId,
			        		  templateTypeId : 10,
			        		  patientId : '',
			        		  visitTypeId: '',
			        		  encounterId: ''
			        		}
			        	
			        	console.log('data',JSON.stringify(data));
			        	var URI = BASE_URL + ROOT_URL +GLOBAL+'/petient/quesionnaaire/radiology';
			    		BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
			    				{
			    	
			    			   if(response.data.status=='success')
			    			      {
			    				   $scope.saveTrasactionFlag = true;
			    				   	$scope.templateTransactionObject = response.data.listObject[0];
			    				   	$scope.template = response.data.listObject[0];
			    				   	if($scope.template != undefined){
			    				   	document.getElementById("htmlContent").innerHTML = JSON.parse($scope.template.templateObject).html;
			    				 	$scope.obj = JSON.parse($scope.template.templateObject).obj;
			    				   	$('#questionnaireTemplateModal').modal('show');
						    		var element = angular.element(document.getElementById("htmlContent"));
									   $compile(element)($scope);
			    				   	}else{
			    				   		growl.warning("Template is not mapped...!",{
											title : "warning"
										});
			    				   		$rootScope.stopLoader();
						        		return;
						        		
			    				   	}
			    				   	$rootScope.stopLoader();
			    			      }
			    			   else if(response.data.status=='error'){
			    				   $rootScope.stopLoader();
			    			   }
			    		});
			    	}
					
			       	//SAVE diagnostic procedure questionary
			       	
			        $scope.saveTemplateTransaction = function()
			        {
			        		$rootScope.startLoader();
			        		
							var templateTransaction = angular.copy($scope.templateTransaction);
							templateTransaction.templateId = $scope.template.templateId;
							templateTransaction.title= $scope.template.title;
							templateTransaction.specialityId = $scope.procedureServiceObj.specialityId;
							templateTransaction.subSpecialityId = '';
							templateTransaction.templateObject = $scope.template.templateObject;
							templateTransaction.encounterId=$scope.procedureServiceObj.encounterId;
							templateTransaction.doctorId=$scope.procedureServiceObj.doctorId;
							templateTransaction.patientId=$scope.procedureServiceObj.patientId;
							templateTransaction.orderDetailsId=$scope.procedureServiceObj.orderDetailsId;
							templateTransaction.isQuestionary='Y';
							var jsonObject = angular.equals({},$scope.template.templateObject.obj)?JSON.parse($scope.template.templateObject.obj):{};
							var templateObj = JSON.parse($scope.template.templateObject);
							templateObj.obj = $scope.obj;
							templateTransaction.jsonObject = JSON.stringify(jsonObject);
							templateTransaction.templateObject = JSON.stringify(templateObj);
							templateTransaction.templateTransactionId = $scope.procedureServiceObj.templateTransactionId;
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
			    				  	if($scope.templateTransactionObject==undefined || $scope.templateTransactionObject ==null ||
					    				   	 $scope.templateTransactionObject =="" ){
				    				  		growl.warning("Please add questionnaire...!",{
												title : "warning"
											});
							        		return;
					    				   	 }
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
			    	
//			        =========================================Questionannary eND=================================================     
			        
			        // accept request
			        $scope.acceptRequest = function(item) {
			        	
			        	
			        	if($scope.visitTypeId == 1){
							var data = {
								    "orgId":$scope.organizationId,
								    "unitId":$scope.unitId,
								    "billingMasterId":$scope.billingMasterId
								}
							var URI = BASE_URL + ROOT_URL + "/api/billing/getDataForCheckBillStatusByBillNumber";
							GenericService.serviceAction("POST",URI,data).then(function(response){
								if (response.data.status == 'success')
								{
									$scope.checkBillList=response.data.listObject;
									console.log($scope.checkBillList);
									$scope.billStatusId = $scope.checkBillList[0].billStatusId;
									$scope.paymentEntitlementId = $scope.checkBillList[0].paymentEntitlementId;
									if($scope.paymentEntitlementId != 2  && $scope.paymentEntitlementId != 3){
									if($scope.billStatusId == 3 && $scope.paymentEntitlementId == 1 && $scope.checkFlagBillSettle == false){
						        		$rootScope.startLoader();
										item.isCritical='N';
										item.unitId=$scope.unitId;
										item.organizationId=$scope.organizationId;
										item.userId=$scope.userId;
										item.isPACSRequired=cookieObjectAppConfig.isPACSRequired;
										item.updatedBy = $scope.userId;
										item.currentDate =moment(new Date()).format('YYYY-MM-DD HH:MM:SS');
										
										var URI = BASE_URL + ROOT_URL + "/api/dignostic/acceptRequest";
										BillingGenericService.serviceAction(METHOD_POST,URI,item, NOTIFICATION_MSG_STATUS_TRUE).then(function(response){
											if(response.data.status == "success"){
												 $rootScope.stopLoader();
												$scope.init();
												$scope.getworkbenchList();
											}else{
												 $rootScope.stopLoader();
												$scope.init();
												$scope.getworkbenchList();
											}
										});
										
						        	}else{
										if($scope.checkFlags == 1){
											$scope.checkFlagBillSettle = true;
											$('#checkBillSettlePopUp').modal('show');
										}else{
							        		$rootScope.startLoader();
											item.isCritical='N';
											item.unitId=$scope.unitId;
											item.organizationId=$scope.organizationId;
											item.userId=$scope.userId;
											item.isPACSRequired=cookieObjectAppConfig.isPACSRequired;
											item.updatedBy = $scope.userId;
											item.currentDate =moment(new Date()).format('YYYY-MM-DD HH:MM:SS');
											
											var URI = BASE_URL + ROOT_URL + "/api/dignostic/acceptRequest";
											BillingGenericService.serviceAction(METHOD_POST,URI,item, NOTIFICATION_MSG_STATUS_TRUE).then(function(response){
												if(response.data.status == "success"){
													 $rootScope.stopLoader();
													$scope.init();
													$scope.getworkbenchList();
												}else{
													 $rootScope.stopLoader();
													$scope.init();
													$scope.getworkbenchList();
												}
											});
											
							        	}
									}
									}else{
						        		$rootScope.startLoader();
										item.isCritical='N';
										item.unitId=$scope.unitId;
										item.organizationId=$scope.organizationId;
										item.userId=$scope.userId;
										item.isPACSRequired=cookieObjectAppConfig.isPACSRequired;
										item.updatedBy = $scope.userId;
										item.currentDate =moment(new Date()).format('YYYY-MM-DD HH:MM:SS');
										
										var URI = BASE_URL + ROOT_URL + "/api/dignostic/acceptRequest";
										BillingGenericService.serviceAction(METHOD_POST,URI,item, NOTIFICATION_MSG_STATUS_TRUE).then(function(response){
											if(response.data.status == "success"){
												 $rootScope.stopLoader();
												$scope.init();
												$scope.getworkbenchList();
											}else{
												 $rootScope.stopLoader();
												$scope.init();
												$scope.getworkbenchList();
											}
										});
										
						        	}
								}
							});
						}else{
			        		$rootScope.startLoader();
							item.isCritical='N';
							item.unitId=$scope.unitId;
							item.organizationId=$scope.organizationId;
							item.userId=$scope.userId;
							item.isPACSRequired=cookieObjectAppConfig.isPACSRequired;
							item.updatedBy = $scope.userId;
							item.currentDate =moment(new Date()).format('YYYY-MM-DD HH:MM:SS');
							
							var URI = BASE_URL + ROOT_URL + "/api/dignostic/acceptRequest";
							BillingGenericService.serviceAction(METHOD_POST,URI,item, NOTIFICATION_MSG_STATUS_TRUE).then(function(response){
								if(response.data.status == "success"){
									 $rootScope.stopLoader();
									$scope.init();
									$scope.getworkbenchList();
								}else{
									 $rootScope.stopLoader();
									$scope.init();
									$scope.getworkbenchList();
								}
							});
							
			        	}
			        
					}
			        

			        
			    	$scope.cancelBeforBillSettle = function()
					{
						$scope.checkFlagBillSettle = false;
						$('#checkBillSettlePopUp').modal('hide');
					}
					
					$scope.continueBeforBillSettle = function()
					{
						$scope.checkFlags = 2;
						$scope.checkFlagBillSettle = false;
						$('#checkBillSettlePopUp').modal('hide');
						$scope.acceptRequest($scope.collectionObjSSSSSSS);
					}
					
					$scope.collectionObj = {};
					$scope.validateUhid = "";
					$scope.checkFlagBillSettle = false;
					$scope.checkFlags = 1;
					$scope.collectionObjSSSSSSS = {};
					$scope.validateWorkListDetails = function(obj){
					    $scope.billingMasterId = obj.billingMasterId;
					    $scope.visitTypeId = obj.visitTypeId;
						$scope.checkFlags = 1;
						$scope.collectionObjSSSSSSS = obj;
						if($scope.mandatoryScan != null && $scope.mandatoryScan =="Y"){
							$scope.collectionObj = obj; 
							$scope.validateUhid = "";
							$('#validatePatient2').modal('show');
						}else{
							$scope.acceptRequest(obj);
						}
						
					}
					
					$scope.closevalidatePop = function(){
						$('#validatePatient2').modal('hide');
					}
					
					$scope.validateEquealPatient = function(event,barcode){
						 var enterCode = 13;
						 if (!barcode && event.which === enterCode) {
						        barcode = $scope.validateUhid;
						      }
							 if(!!barcode){
								 
								 if($scope.collectionObj.uhidNumber.toLowerCase() == $scope.validateUhid.toLowerCase() ){
										$('#validatePatient2').modal('hide');
										$scope.acceptRequest($scope.collectionObj);
									}else{
										growl.warning('Please enter valid patient number',
												{
													title : 'Warning!'
												});
									}
								 
							 }
							
					}
			        
			        
			     // accept request
			        $scope.rejectRequest = function(obj) {
						$scope.rejectReqObj=obj;
						console.log($scope.rejectReqObj);
						
					}
					$scope.saveAcceptReject=function(){
						console.log("reason Id",$scope.rejectReasonId);								
						if($scope.rejectReasonId==undefined || $scope.rejectReasonId=="")
							{
								growl.warning('Please select reason...!!',
										{
											title : 'Warning!'
										});
								return false;
							}
						else
							{
							if(Object.keys($scope.rejectReqObj).length !=0 ){
								$('#accepatReject').modal('hide');
								var data1={
											"encounterId": $scope.rejectReqObj.encounterId,
											"admissionId":$scope.rejectReqObj.admissionId,
											"userId":$scope.userId,
											"reasonId":$scope.rejectReasonId,
											"isCritical":'Y',
											"orderDetailsId":$scope.rejectReqObj.orderDetailsId,
											"unitId":$scope.unitId,
											"organizationId":$scope.organizationId,
											"visitTypeId":$scope.rejectReqObj.visitTypeId,
											"isPreOrPostBilling":$scope.isPreOrPostBilling,
											"specialityId":$scope.rejectReqObj.specialityId
								        }
							       
								
										 $rootScope.startLoader();
										
							    		var URI = BASE_URL + ROOT_URL+ "/api/dignostic/rejectRequest";
							    		BillingGenericService.serviceAction(METHOD_POST,URI,data1, NOTIFICATION_MSG_STATUS_TRUE)
										.then(
												function(response) {
														
													$scope.init();
														$scope.getworkbenchList();
														$scope.rejectReqObj={};
														$scope.rejectReasonId="";
														$rootScope.stopLoader();
													
												});
								
								}
							}
						}
					// getting color class start
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
			        
			        
			        $scope.radSpecialityId = 0;
					$scope.radSubSpecialityId = 0;
					
					$scope.selectedSpecialityId="";
					$scope.getModalityBySubSpeciality = function(item) {
						
						$scope.slotCheckTime = moment(new Date()).format("HH:mm");
						$scope.fromTimeString = "";
						$scope.modalityMasterList = [];
						$scope.admissionId = item.admissionId;
						$scope.radSpecialityId = item.specialityId;
						$scope.radSubSpecialityId = item.subSpecialityId;
						$scope.orderDetailsId = item.orderDetailsId;
						$scope.patientId = item.patientId;
						var data = {
							organizationId : $scope.organizationId,
							unitId : $scope.unitId,
							specialityId : item.specialityId,
							subSpecialityId :item.subSpecialityId
						}
						console.log(data);
						//$rootScope.startLoader();
						//return false
						if(item.specialityId==cookieObjectAppConfig.hemodialysisSpecialityId){
							$scope.selectedSpecialityId=item.specialityId;
							$scope.modalityMasterList= null;
							var dataObj = {
									unitId : $scope.unitId,
									organizationId : $scope.organizationId
							};
							console.log(data);
							var URI = BASE_URL + ROOT_URL
							+ "/api/adt/getDialysisChair";								
							GenericService
							.serviceAction("POST", URI, dataObj)
					.then(
							function(response) {
								console.log(response);
								$scope.bedList = [];
								if (response.data.status == "success")
									$scope.bedList = response.data.listObject;
								console.log($scope.chairList)
							});
						}
						
							$scope.selectedSpecialityId=item.specialityId;
							$scope.bedList = null;
							var URI = BASE_URL + ROOT_URL
								+ GETMODALITYBYSUBSPECIALITYID;
						BillingGenericService
								.serviceAction(METHOD_POST, URI, data,
										NOTIFICATION_MSG_STATUS_FALSE)
								.then(
										function(response) {
											console.log(response);
											// $rootScope.startLoader();
											$scope.modalityMasterList = [];
											if (response.data.status == "success") {
												$scope.modalityMasterList = response.data.listObject;
											 console.log($scope.modalityMasterList)
											}

										});
					}
					
					
					
					
					//For Date Difference For Modality Start
			       	$scope.daysDiffrence = function(date) {
			       	    var today = new Date();
			       	    var dd = today.getDate();
			       	    var mm = today.getMonth() + 1; //January is 0!
			       	    var yyyy = today.getFullYear();
			       	    if (dd < 10) {
			       	      dd = '0' + dd
			       	    }
			       	    if (mm < 10) {
			       	      mm = '0' + mm
			       	    }
			       	    today = yyyy + '/' + mm + '/' + dd;
			       	    $scope.today = today;
			       	    var date2 = new Date(today);
			       	    var date1 = new Date(date);
			       	    var timeDiff = Math.abs(date2.getTime() - date1.getTime());
			       	    $scope.dayDifference = Math.ceil(timeDiff / (1000 * 3600 * 24));
			       	    return $scope.dayDifference;
			       	  }
			       	//For Date Difference for Modality END
					//Function For Get Date Wise Slots
					  $scope.itemId="";
					  $scope.itemObj={};
					  $scope.globleObj={};
					  $scope.loadingMsg="";
					$scope.getSlotsOfDate = function(item) {
						$scope.slotDetailsList = [];
						$scope.loadingMsg="Loading....";
						if(item!=undefined){
							$scope.itemObj=JSON.parse(item);
							$scope.globleObj=JSON.parse(item);
						}else{
							$scope.itemObj=$scope.globleObj;
						}
						console.log(item)
						
						
						$scope.mobilityId="";
						$scope.itemId= $scope.itemObj.itemId;
						$scope.mobilityId=$scope.itemObj.modalityId
						console.log($scope.itemObj.itemId);
						$scope.slotDetailsList = [];
						$scope.fromTimeString = "";
						 
						if($scope.itemObj.bedId > 0 || $scope.mobilityId > 0)
						{
						var dt = moment($scope.datepicker).format('YYYY-MM-DD');
						$scope.diffDaysMod = $scope.daysDiffrence(moment($scope.datepicker).format('YYYY/MM/DD'));
							var data = {
								subSpecialityId : $scope.radSubSpecialityId,
								specialityId : $scope.radSpecialityId,
								appointmentDate : dt,
								organizationId : $scope.organizationId,
								unitId : $scope.unitId,
								
							};
							
							if($scope.itemObj.bedId!=null && $scope.itemObj.bedId!=undefined){
								console.log($scope.itemId)
								data.slotTypeId =8;
								data.bedId=$scope.itemObj.bedId;
							}
							else{
								
								data.slotTypeId =2;
								data.modalityId=$scope.mobilityId;	
							}
						console.log(data);
						//return false;
						var URI = BASE_URL + ROOT_URL + GETDAILYSLOTS;
						BillingGenericService
								.serviceAction(METHOD_POST, URI, data,
										NOTIFICATION_MSG_STATUS_FALSE)
								.then(
										function(response) {
											console.log(response);
											
											if (response.data.status == "success") {
												$scope.slotDetailsList = response.data.listObject;
												if($scope.slotDetailsList.length==0){
													$scope.loadingMsg="Slots are not created for selected date...!";
												}
												console.log($scope.slotDetailsList);
				}

										});
					}
						else
							{
							growl.error("Please Select Modality",{
								title : ERROR_MSG
							});
							return false;
								
							}

					}
					$scope.clearDetails=function(){
						$scope.fromTimeString = "";
						$scope.fromTimeStringSlot="";
						$scope.slotId="";
						 $scope.modalityId="";
						 $scope.slotDetailsList=[];
						 $scope.loadingMsg="";
					}
					
					$scope.slotBooking = function(item)
					{
						$scope.fromTimeStringSlot = item.fromTimeString;
						$scope.fromTimeStringSlotShow = item.fromTimeString2;
						 $scope.slotId = item.slotId;
					}
					
                   $scope.saveAppointment = function(){
						
						if($scope.modalityId=="" || $scope.modalityId==undefined){
							growl.error("Please Select Modality",{
								title : ERROR_MSG
							});
							return false;
						}
						if($scope.slotId=="" || $scope.slotId==undefined){
							growl.error("Please Select Slots",{
								title : ERROR_MSG
							});
							return false;
						}
						
						$rootScope.startLoader();
						$scope.currentDate = new Date();
						$scope.currentDate = moment(
								$scope.currentDate).format(
								'DD-MM-YYYY HH:mm:ss');
						var data = {
							slotId : $scope.slotId,
							remark : "",
							organizationId : $scope.organizationId,
							unitId : $scope.unitId,
							createdDate : $scope.currentDate,
							updatedDate : $scope.currentDate,
							createdBy : $scope.userId,
							updatedBy : $scope.userId,
							status : 'A',
							slotStatusId : 4,
							priorityId : 1,
							allowedOverBookingSlots : 0,
							admissionId : $scope.admissionId,
							orderDetailsId:$scope.orderDetailsId,
							appointmentStatusId:1,
							patientId :$scope.patientId
						}
						if($scope.selectedSpecialityId==cookieObjectAppConfig.hemodialysisSpecialityId){
							data.appointmentTypeId=6;
						}else{
							data.appointmentTypeId=2;
						}
							
								var URI = BASE_URL + ROOT_URL
									+ "/api/opd/scheduleRadiologyAppointmentForInPatient";
						
						console.log(data);
						//return false; 

						BillingGenericService
								.serviceAction(METHOD_POST, URI,
										data,
										NOTIFICATION_MSG_STATUS_TRUE)
								.then(
										function(response) {
											console.log(response);
											if (response.data.status == "success") {
												angular.element('#info').modal('hide');
											     $rootScope.stopLoader();
												$scope.clearDetails();
												$scope.getworkbenchList();
												$rootScope.stopLoader();
											}
											else if(response.data.status == "error") 
											{
													angular
													.element(
															'#info')
													.modal(
															'hide');
													$rootScope.stopLoader();
											}
										});
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