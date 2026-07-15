'use strict';

/**
 * @Author By  
 * @name myApp.controller:procedureWorklistForPrintingController
 * @description #procedureWorklistForPrintingController of the myApp
 */
angular
		.module('myApp')
		.controller(
				'procedureWorklistForPrintingController',
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
							 $scope.isRisPatientDetailsOnFirstPage = cookieObjectAppConfig.isRisPatientDetailsOnFirstPage;
							$scope.deptId= cookieObjectAppConfig.radiologySpecialityId;
							
							/*** Check user is Loggedin */
							var cookieObject,cookieObjectAppConfig;
							[cookieObject,cookieObjectAppConfig] = $scope.checkCookies();
							$scope.orgUnitId = cookieObject.unitId;
							$scope.orgId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							$scope.unitName = cookieObject.unitNme;
							
							/*** Lab Configuration */
							var labConfig  =    $scope.checkLabConfig();
							$scope.labSpecialityId = labConfig.labSpecialityId;
//							$scope.subDeptId = labConfig.histopathologyId;
							
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
								  var cookieObjectAppConfigSecond = JSON.parse(localStorage.getItem('cookieObjectAppConfigSecond'));
								  if (cookieObjectAppConfigSecond != undefined) {
									$scope.isBillManipulationAllowedUserLevel = cookieObject.isBillManipulationAllowedUserLevel;
									$scope.isBillManipulationAllowedAppLevel = cookieObjectAppConfigSecond.isBillManipulationAllowedAppLevel;
								  }
									$scope.isBillManipulationAllowed =  ($scope.isBillManipulationAllowedUserLevel && $scope.isBillManipulationAllowedAppLevel =='Y') ? true : false;
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
									id : 2,
									label : " IP "
								}, {
									id : 1,
									label : " OP "
								}  ];
								$scope.selectedVisitType = []; 
								$scope.selectedDepartMentList=[];
								$scope.selectedModalityList = [];
								$scope.selectedtestList=[];
								
								var commonObj = {
										unitId : $scope.unitId,
										organizationId : $scope.organizationId
									};
								
								

								//end radiology

								
								var data = {organizationId : $scope.organizationId};
								var headerObj = {
									organization_id : $scope.organizationId,
									unit_id : $scope.unitId
								}
								var paramObj = {};

							
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
							
					/****Template OBJECT*****/
							
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
								
								//var URI = BASE_URL + ROOT_URL + "/global/serviceMaster/"+searchKeyword+"/unit/"+$scope.unitId+"/org/"+$scope.organizationId;
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
						var deptList =[];
						var serviceList =[];
						
						angular.forEach($scope.selectedVisitType,
								function(value,index) {
							         visitList.push(value.id);
								});		
						
						angular.forEach($scope.selectedDepartMentList,
								function(value,index) {
									deptList.push(value.id);
								});
						angular.forEach($scope.selectedtestList,
								function(value,index) {
									serviceList.push(value.id);
								});
						$rootScope.startLoader();
						$scope.searchObject.fromDate = moment($scope.datepickerFromDate).format('YYYY-MM-DD');
						$scope.searchObject.toDate = moment($scope.datepickerToDate).format('YYYY-MM-DD');						
						$scope.searchObject.visitList = visitList;	
						$scope.searchObject.deptList =  deptList;
						$scope.searchObject.serviceList = serviceList;

						if($scope.searchObject.patientId==0 || $scope.searchObject.patientId==undefined || $scope.searchObject.patientId==""){
							$scope.patientName = '';
							$scope.uhidNumber = '';
						}						
						
						var URI = BASE_URL + ROOT_URL+ "/api/dignostic/getProcedureWorkListForPrinting";
						GenericService.serviceAction("POST", URI, $scope.searchObject).then(function(response) {
											$scope.workBenchList = [];

											if (response.data.status == "success") {
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
						$scope.serviceName='';
						$scope.selectedVisitType = [];
						$scope.selectedDepartMentList=[];
						$scope.selectedtestList=[];
						$scope.patientName = undefined;
						$scope.uhidNumber = undefined;
						$scope.search = undefined;
						$scope.datepickerFromDate = new Date();
						$scope.datepickerToDate = new Date();
					}
					

					$scope.getToDate=function(){
						$scope.todateOptions.minDate=$scope.datepickerFromDate.getTime();
						
						if(moment($scope.datepickerToDate).isBefore(moment($scope.datepickerFromDate)))
						{
							 $scope.datepickerToDate=new Date($scope.datepickerFromDate.getTime());
						}
					}

							$scope.getReportDetails=function(procedure){
								procedure.isTempFinalRelease='Y';
						       	 $state.go("diagnosticProcedureReport",{
						       		radiologyReportObject :procedure
									}); 
						       		
						       		
						       	}
							
							 
							
							$scope.getSpecialityAndSubspeciality = function() {
								var specialistList = [];
								specialistList.push($scope.deptId);
								var subSpecialistList = [];
								angular.forEach($scope.departmentList,
										function(value, index) {
											subSpecialistList.push(value.id);
										});
							
								var data = {
									unitId : $scope.unitId,
									orgnisationId : $scope.organizationId,
									specialityIdList : specialistList,
									subSpecialityIdList : subSpecialistList
								}
								var URI = BASE_URL
										+ ROOT_URL
										+ "/global/searchUnitServiceByMultipleSpecialityAndSubSpeciality";
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													if (response.data.status == "success") {
														$scope.testList = [];
														angular.forEach(response.data.listObject,
																		function(value,index) {
																			var data = {};
																			data.id = value.serviceId;
																			data.label = value.serviceStandardName;
																			$scope.testList.push(data);
																		});
														console.log('serviceList',$scope.testList);
													}
												});
								// }
							}
//							$scope.getSpecialityAndSubspeciality();
							
							 $scope.multiselectObj = {
							    		"id":"",
							    		"label":""
							    }
							    
							$scope.getSubSpecialityBySpeciality = function() {
								var data = {
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									specialityIdList : $scope.deptIdList 
								}
								console.log(data);
								var URI = BASE_URL + ROOT_URL+ "/adt/getSubSpecialityBySpecialityArray";
								BillingGenericService
										.serviceAction(METHOD_POST, URI, data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													console.log(response);
													// $rootScope.startLoader();
													$scope.subSpecialityList = [];
													$scope.departmentList=[];
													if (response.data.status == "success") {
														$scope.subSpecialityList = response.data.listObject;
														var cnt=0;
														if(response.data.listObject!=null){
															angular.forEach(response.data.listObject, function(value, key) {
																cnt++;
																var multiselectObj = angular.copy($scope.multiselectObj);
																	multiselectObj.id = value.subSpecialityId;
																	multiselectObj.label = 	value.subSpecialityName;
																	$scope.departmentList.push(multiselectObj);
																});
																if(response.data.listObject.length==cnt){
																	$scope.getSpecialityAndSubspeciality();
																}
															
															}
													
														

													}

												});
							}
//							$scope.getSubSpecialityBySpeciality();
							
							// getting modality department
							$scope.getModalityDepartment=function(){
							var commonObj = {
									unitId : $scope.unitId,
									organizationId : $scope.organizationId
								};
							 var URI = BASE_URL + ROOT_URL + "/adt/getActiveSpecialityListForModality";
							BillingGenericService.serviceAction(METHOD_POST, URI,commonObj,NOTIFICATION_MSG_STATUS_FALSE)
									.then(function(response) {																
												if (response.data.status == "success")
													{
														$scope.deptIdList=[];
														var cnt=0;
														 angular.forEach(response.data.listObject,function(value,index) {
															 	    cnt++;
																	$scope.deptIdList.push(value.specialityId);
																});
														 	if(response.data.listObject!=null && response.data.listObject.length==cnt){
														 		$scope.getSubSpecialityBySpeciality();
														 	}										 			
													
													}
													
											});
							}
							$scope.getModalityDepartment();
							
					        $scope.checkAll = function()
					        {
					        	if($scope.selectedAll)
					        		{
					        		var idex = 0;
					        		angular.forEach($scope.workBenchList,function(val,key) 
											{
					        			$scope.workBenchList[idex].printReport=true;
					        			idex ++;
											});
					        		}else{
					        			var idex = 0;
						        		angular.forEach($scope.workBenchList,function(val,key) 
												{
						        			$scope.workBenchList[idex].printReport=false;
						        			idex ++;
												});
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
					        
					        $scope.diagnosisReportPrint =function(templateTransaction,printWithHeader){
					        	var info = {};
					        	info.ageString = templateTransaction.ageString;
					        	info.encounterId = templateTransaction.encounterId;
					        	info.admissionId = templateTransaction.admissionId;
					        	info.encounterTypeId = templateTransaction.encounterTypeId;
					        	info.genderCode= templateTransaction.genderCode;
					        	info.genderId= templateTransaction.genderId;
					        	info.isProvisionalRelease= templateTransaction.isProvisionalRelease;
					        	info.isReportRecall= templateTransaction.isReportRecall;
					        	info.orderDate= templateTransaction.orderDate;
					        	info.orderDetailsId= templateTransaction.orderDetailsId;
		    					info.orderNo= templateTransaction.orderNo;
		    					info.patientCategory= templateTransaction.patientCategory;
		    					info.patientId= templateTransaction.patientId;
		    					info.patientName= templateTransaction.patientName;
		    					info.prefixCode= templateTransaction.prefixCode;
		    					info.referralName= templateTransaction.referralName;
		    					info.registerDate= templateTransaction.registerDate;
		    					info.serviceAmt= templateTransaction.serviceAmt;
		    					info.serviceCode= templateTransaction.serviceCode;
		    					info.serviceId= templateTransaction.serviceId;
		    					info.serviceName= templateTransaction.serviceName;
		    					info.serviceRendered= templateTransaction.serviceRendered;
		    					info.specialityId= templateTransaction.specialityId;
		    					info.specialityName= templateTransaction.specialityName;
		    					info.subSpecialityId= templateTransaction.subSpecialityId;
		    					info.templateContent = templateTransaction.templateContent;		
		    					info.templateDesc= templateTransaction.templateDesc;
		    					info.templateDoctorId= templateTransaction.templateDoctorId;
		    					info.templateDoctorName= templateTransaction.templateDoctorName;
		    					info.templateFormatId= templateTransaction.templateFormatId;
		    					info.templateObject = templateTransaction.templateObject;
		    					info.templateTransactionId= templateTransaction.templateTransactionId;
		    					info.templateTypeId= templateTransaction.templateTypeId;
		    					info.title= templateTransaction.title;
		    					info.uhidNumber= templateTransaction.uhidNumber;
		    					info.visitName= templateTransaction.visitName;
		    					info.visitNumber= templateTransaction.visitNumber;
		    					info.visitTypeId= templateTransaction.visitTypeId;
		    					info.wardName= templateTransaction.wardName;
		    					info.billNo = templateTransaction.billNo;
		    					info.organizationId = $scope.organizationId;
		    					info.orderDtTime = templateTransaction.orderDtTime;
						        	info.unitId =  $scope.unitId;
						        	info.printWithHeader = printWithHeader; //print with header
						        	info.unitName =  $scope.unitName;
						        	if(templateTransaction.isHtmlPrint==true){
						        	var URI = BASE_URL + ROOT_URL + "/api/dignostic/printDiagnosisReport";
						    		BillingGenericService.serviceAction(METHOD_POST, URI, info,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
						    				{
						    	
						    			   if(response.data.status=='success')
						    			      {
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
						    						 if(info.printWithHeader == 0){
						    							 patientDetails = response.data.object.header;
						    						 }else{
						    							 patientDetails = response.data.object.headerWithoutImg;
						    						 }
						    						 completeTemplateContent = patientDetails + response.data.object.templateContent+response.data.object.footer;
						    					 }
						    				   
						    				   var printWindow = window.open('', '', 'height=500,width=700');  
	                                         printWindow.document.write('<html><head></head>');  
	                                         printWindow.document.write(' <style> '
	                                                 +'.page-header, .page-header-space {height: '+headerSize+'} '
	                                                 +' .page-footer, .page-footer-space { height: 105px;} '
	                                                 + ' .page-footer { position: fixed; bottom: 0; width: 100%;} '
	                                                 + ' .page-header { position: fixed; top: 0mm; width: 100%;} '
	                                                 + ' .page {page-break-after: auto;} '
	                                                 + '  @page { margin: 20mm}'
	                                                 + 'p { margin:0px 0px 0px 0px; }'
	                                                 + ' </style>' );
	                                         printWindow.document.write('<body>');
	                                         if(info.printWithHeader == 0){
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
					        
					        $scope.printReports= function(printFlag,printWithHeader) 
							{
					        	$scope.selectedList=[];					        	
					        	angular.forEach($scope.workBenchList,function(val,key) 
										{
											if(val.printReport==true)
												{
												 $scope.selectedList.push(val.templateTransactionId);
												}
										});
					        	if($scope.selectedList.length==0){
					        		growl.warning('Please select service...!!',
											{
												title : 'Warning!'
											});
					        		return false;
					        	}
								
								try 
								{
//										$rootScope.startLoader();
										angular.forEach($scope.workBenchList,function(val,key) 
												{
													if(val.printReport==true)
														{
															var templateTransaction =
															{
																templateTransactionId :val.templateTransactionId,
																isReportRelease:'Y',
																templateObject:val.templateObject
															}
														 var URI = BASE_URL + ROOT_URL + "/global/template/transaction";
											        	    BillingGenericService.serviceAction(METHOD_PUT,URI,templateTransaction,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
												    			if (response.data.status == "success")
												    			{												    			
												    				$scope.getworkbenchList();
																	/*var data =  {
													    		            "appName": "HIS",
													    		            "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
													    		            "reportDesignFilePath": "RadiologyReport.rpt",
													    		            "reportFormat": "PDF",
													    		            "exportFileName": "RadiologyReport",
													    		            "reportParams": [
													    		              {
													    			             "paramName": "template_transaction_id",
													    			             "paramType": "Integer",
													    			             "paramValue": val.templateTransactionId
													    		              }
													    		              ,
																              {
																		             "paramName": "isOriginal",
																		             "paramType": "Integer",
																		             "paramValue": printFlag
																	           }
													    		            ]
																		};
																	reportService.generate(REPORT_API_URI1,data);*/
												    				$scope.diagnosisReportPrint(val,printWithHeader);
																}
												    		});
														
														}
												});
										
									} catch (e) {
										$rootScope.stopLoader();
										console.log("Exception",e.message);
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