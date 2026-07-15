'use strict';


angular
		.module('myApp')
		.controller(
				'surgeryNoteMasterController',
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
						'PagerService',
						'$compile',
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService,BillingGenericService, growl,
								$state, PagerService,$compile) {

							/* init() function for form object create. */

							$scope.init = function() {

								
								var cookieObject = $cookies.getObject('cookieObject');
								if(cookieObject == undefined){
									$state.go('login');
									return;
								}
								
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;

								/** Variables for pagination */
								$scope.offset = 0;
								$scope.noOfRecordsPerPage = 10;
								$scope.totalNoOfRecords;
								$scope.searchKeyword;
								$scope.commonListCount;

								$rootScope.loginpage = true;
								$scope.country = {}
								$scope.saveBtnFlag = true;
								$scope.updateBtnFlag = false;

								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup

								var data = {};
								//for datepicker From
								$scope.dateOptionsFrom = {
									formatYear : 'yyyy',
									showWeeks : false
								};

								$scope.openDatePickerFrom = function() {
									$scope.datepickerFrom.opened = true;
								};

								$scope.datepickerFrom = {
									opened : false
								};
								//for datepicker TO
								$scope.dateOptionsTo = {
									formatYear : 'yyyy',
									showWeeks : false
								};

								$scope.openDatePickerTo = function() {
									$scope.datepickerTo.opened = true;
								};

								$scope.datepickerTo = {
									opened : false
								};
								
								$scope.searchObj={
										visitTypeId:"",
										fromDate:"",
										toDate:""
								}
								
								$scope.setFirstTabLevel=1;
							}

							$scope.init();
							
							/** function for searching the IP procedure */
							$scope.searchOtBill = function () {

								var URIservice = BASE_URL + ROOT_URL + "/api/procedure/getprocedureOrderbyVisitandDate";
								var dataservice = {
									fromDate: moment($scope.datepickerFrom).format('YYYY-MM-DD'),
									toDate: moment($scope.datepickerTo).format('YYYY-MM-DD'),
									visitTypeId:$scope.searchObj.visitTypeId
								}
								console.log("dataservice=",dataservice);
//								return false;
								$rootScope.startLoader();
								GenericService.serviceAction("POST", URIservice, dataservice).then(
									function (response) {
										if (response.data.status == "success") {
											$scope.otBillServiceList = response.data.listObject;
										}
										$rootScope.stopLoader();
									});

							}
							
	/**=============== get template for Surgery notes Starts ===============*/
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
				        			  updatedDate : ""
				        }
							  
							$scope.getSurgeryNotesTemplate=function(){
								$rootScope.startLoader();
					        	var data={
					        		  orgId : $scope.organizationId,
					        		  unitId : $scope.unitId,
					        		  doctorId : '',
					        		  specialityId :'',
					        		  templateTypeId : 15
					        		}
					        	
					        	console.log('data',JSON.stringify(data));
					    		var URI = BASE_URL + ROOT_URL + "/api"+GLOBAL+TEMPLATE_LIST_BY_SPECILATIY;
					    		BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
					    				{
					    	
					    			   if(response.data.status=='success')
					    			      {
					    				   	$scope.template = response.data.listObject[0];
					    				   	$('#addtemplateModal').modal('show');
					    				   	document.getElementById("htmlContent").innerHTML = JSON.parse($scope.template.templateObject).html;
								    		var element = angular.element(document.getElementById("htmlContent"));
											   $compile(element)($scope);
											$('#addviewTemplateModal').modal('show');
											$scope.saveTrasactionFlag = true;
											
											var element = angular.element(document.getElementById("htmlContent"));
											   $compile(element)($scope);
					    				   	$rootScope.stopLoader();
					    			      }
					    			   else if(response.data.status=='error'){
					    				   $rootScope.stopLoader();
					    			   }
					    		});
							}
							  /** save the template data*/
							  $scope.saveTemplateTransaction= function()
						        {
						        		$rootScope.startLoader();
						        		
										var templateTransaction = angular.copy($scope.templateTransaction);
										templateTransaction.templateId = $scope.template.templateId;
										templateTransaction.title= $scope.template.title;
										templateTransaction.specialityId = $scope.template.specialityId;
										templateTransaction.subSpecialityId = $scope.template.subSpecialityId;
										templateTransaction.templateObject = $scope.template.templateObject;
										templateTransaction.admissionId=$scope.orderDetailsList.admissionId;
										templateTransaction.doctorId=$scope.orderDetailsList.doctorId;
										templateTransaction.patientId=$scope.orderDetailsList.patientId;
										templateTransaction.diagnosisId='';
										var jsonObject = angular.equals({},$scope.template.templateObject.obj)?JSON.parse($scope.template.templateObject.obj):{};
										var templateObj = JSON.parse($scope.template.templateObject);
										templateObj.obj = $scope.obj;
										templateTransaction.jsonObject = JSON.stringify(jsonObject);
										templateTransaction.templateObject = JSON.stringify(templateObj);
						        	    console.log('templateTransaction',JSON.stringify(templateTransaction));
						        	    
						        	    $('#addviewTemplateModal').modal('hide');
					    				$('#addtemplateModal').modal('hide');
							    		
						        	    var URI = BASE_URL + ROOT_URL + "/global/template/transaction";
						        	    BillingGenericService.serviceAction(METHOD_POST,URI,templateTransaction,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
							    			if (response.data.status == "success")
							    			{
							    				$scope.templateTransactionList = [];
												$scope.templateList=[];
												$scope.templateTransactionList =[];
												$rootScope.stopLoader();
											}
							    			else if (response.data.status == "error"){
							    				$rootScope.stopLoader();
							    				
							    			}
											
							    		});
						        }
						        
				  /**=============== get template for Surgery notes End ===============*/
							  
				  /**=============== get template for Discharge notes Starts ===============*/
								$scope.getDischargeNotesTemplate=function(){
									$rootScope.startLoader();
						        	var data={
						        		  orgId : $scope.organizationId,
						        		  unitId : $scope.unitId,
						        		  doctorId : '',
						        		  specialityId :'',
						        		  templateTypeId : 16
						        		}
						        	
						        	console.log('data',JSON.stringify(data));
						    		var URI = BASE_URL + ROOT_URL + "/api"+GLOBAL+TEMPLATE_LIST_BY_SPECILATIY;
						    		BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
						    				{
						    	
						    			   if(response.data.status=='success')
						    			      {
						    				   	$scope.template = response.data.listObject[0];
//						    				   	$('#addtemplateModal').modal('show');
						    				   	document.getElementById("htmlContent").innerHTML = JSON.parse($scope.template.templateObject).html;
//												$('#addviewTemplateModal').modal('show');
												$scope.saveTrasactionFlag = true;
												var element = angular.element(document.getElementById("htmlContent"));
												   $compile(element)($scope);
						    				   	$rootScope.stopLoader();
						    			      }
						    			   else if(response.data.status=='error'){
						    				   $rootScope.stopLoader();
						    			   }
						    		});
								}
							 
							  	$scope.viewTemplate = function(template)
						    	{
						    		document.getElementById("htmlContent").innerHTML = JSON.parse(template.templateObject).html;
						    		$scope.obj = JSON.parse(template.templateObject).obj;
						    		$('#addviewTemplateModal').modal('show');
						    		$scope.obj = JSON.parse(template.templateObject).obj;
						    		var element = angular.element(document.getElementById("htmlContent"));
									   $compile(element)($scope);
									$scope.saveTrasactionFlag = false;
						    	}
				  /**=============== get template for Discharge notes End ===============*/
							/*$scope.setFirstTabLevel = function(newTab) {
								$scope.firstTabLevel = newTab;
								
								if (newTab == '1') {
									
									$scope.getSurgeryNotesTemplate();
								}
								if (newTab == '2') {
									
									$scope.getDischargeNotesTemplate();
								}
								
							};

							$scope.isSetFirstTabLevel = function(tabNum) {
								return $scope.firstTabLevel === tabNum;
								
							};*/
							
							$scope.getOrderDetailsById = function (reqId) {
								$rootScope.startLoader();
								$scope.orderDetailsList=[];
								if (reqId != 0 && reqId != null) {
									var URI = BASE_URL + ROOT_URL + "/api/procedure/order/req/" + reqId;
									BillingGenericService.serviceAction(METHOD_GET, URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function (response) {
										if (response.data.status == "success") {
											$scope.orderDetailsList = response.data.listObject[0];										
											console.log("$scope.orderDetailsList=",$scope.orderDetailsList);
										}
									});
								}
								$scope.getSurgeryNotesTemplate();
								$rootScope.stopLoader();
							}
							// for both i.e surgery notes and discharge notes we are use same function for view purpose
							$scope.getTemplateList = function(reqId) 
					    	{
					    		/** We are calling this api one more time ,bcoz if we click the view  icon directly then we have get details of it*/
					    		$rootScope.startLoader();
					    		if (reqId != 0 && reqId != null) {
									var URI = BASE_URL + ROOT_URL + "/api/procedure/order/req/" + reqId;
									BillingGenericService.serviceAction(METHOD_GET, URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function (response) {
										if (response.data.status == "success") {
											
											//After getting success we are calling view patient details template
											$scope.orderDetailsList = response.data.listObject[0];										
//											console.log("$scope.orderDetailsList=",$scope.orderDetailsList);
									
											// code start here for getting template view details 
								        	var data={
								        		  orgId : $scope.organizationId,
								        		  unitId : $scope.unitId,
								        		  doctorId : '',
								        		  specialityId : '',
								        		  templateTypeId : 15,
								        		  patientId : $scope.orderDetailsList.patientId,
								        		  visitTypeId: $scope.orderDetailsList.visitTypeId,
								        		  admissionId:$scope.orderDetailsList.admissionId
								        		}									        	
									        	console.log('data',JSON.stringify(data));
									    		var URI = BASE_URL + ROOT_URL + "/global/petient/template";
									    		BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
									    				{									    	
										    			   if(response.data.status=='success')
										    			      {
										    				   	$scope.templateTransactionList = response.data.listObject;
										    				   	$('#viewtemplateModal').modal('show');
										    				   	$rootScope.stopLoader();
										    			      }
										    			   else if(response.data.status=='error'){
										    				   $rootScope.stopLoader();
										    			   }
									    		});
										}
									});
								}
							}
							
						} ]);
