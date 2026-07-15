'use strict';

/**
 * @Author By  Vivek Satle
 * @name myApp.controller:radiologyWorkbenchController
 * @description #radiologyWorkbenchController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'radiologyWorklistForPrintingController',
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
						'PaginationService',
						'growl',
						'GenericServiceFileUpload',
						'$window',
						function($scope, $cookies, $rootScope, GenericService,
								BillingGenericService, $state, $timeout,
								$interval, GenericServiceParamHeader,$compile,reportService,PaginationService,growl,GenericServiceFileUpload,$window) {

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
							$scope.deptId= cookieObjectAppConfig.radiologySpecialityId;
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
								 

								/*** Lab Configuration */
								var labConfig  =    $scope.checkLabConfig();
								$scope.photoRequired = labConfig.photoRequired;
								
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
								
								$scope.isBillManipulationAllowedUserLevel = cookieObject.isBillManipulationAllowedUserLevel;
								$scope.isBillManipulationAllowedAppLevel = cookieObjectAppConfigSecond.isBillManipulationAllowedAppLevel;
								$scope.isBillManipulationAllowed = ($scope.isBillManipulationAllowedUserLevel && $scope.isBillManipulationAllowedAppLevel =='Y') ? true : false;
								if($scope.isBillManipulationAllowed == true){
									$scope.filterDummyService = 1;
								}else{
									$scope.filterDummyService = 2;
								}
								$scope.unitName = cookieObject.unitNme;
								$rootScope.loginpage = true;
								$scope.saveBtnFlag = false;
								$scope.updateBtnFlag = true;
								$scope.isReportRecall = 'N';
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

								$scope.encounterSearchObject = {
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
							
							
							
							$scope.getRISRecallFlag = function(){
								try{
									var URI = BASE_URL + ROOT_URL +"/api/global/getUserById/"+$scope.userId+"/"+$scope.organizationId+"/"+$scope.unitId;
									GenericService.serviceAction("GET",URI).then(function(response){
										if(response.data.status == "success"){
											$scope.isRisRecall= response.data.listObject[0].isRisRecall;
											if($scope.isRisRecall == true){
												$scope.isReportRecall = 'Y';
											}else{
												$scope.isReportRecall = 'N';
											}
										}
									});
									
								}catch(e){
									console.log("Exception-",e);
								}
							}
							
							$scope.getRISRecallFlag();
							
							$scope.reCall =  function(){
							 var obj = {}; 
   							 var  i = 0 ;
   							 angular.forEach($scope.pagination.data,function(resultEntryMaster,key) {
   								 	if(resultEntryMaster.printReport==true){
   								 		obj = resultEntryMaster;
	  									i++;
	  								}
	  							});
								if(i == 0){
									growl.warning("Please Select Record",
											{
												title : 'warning!'
											});
									return false;
								}else if(i>1){
									growl.warning("Please Select Only One Record",
											{
												title : 'warning!'
											});
									return false;
								}else{
	    							 obj.isTempFinalRelease='Y';
	    							 obj.isResultEntry='N';
	    							 obj.isRecall = 'Y';
	    							 obj.isAmend = 'Y';
	 					       	 	$state.go("radiologyReport",{
	 					       	 	radiologyReportObject :obj
	 								}); 
								}
							}
							
							//Search Appointment List By Filters
							$scope.searchEncounter = function() {
								
								console.log($scope.selectedVisitType);
								console.log($scope.selectedModalityList);
								var visitList =[];
								var modalityList =[];
								var deptList=[];
								var serviceList=[];
								angular.forEach($scope.selectedVisitType,
										function(value,index) {
									         visitList.push(value.id);
										});
								/*angular.forEach($scope.selectedModalityList,
										function(value,index) {
									      modalityList.push(value.id);
										});*/
								angular.forEach($scope.selectedDepartMentList,
										function(value,index) {
											deptList.push(value.id);
										});
								angular.forEach($scope.selectedtestList,
										function(value,index) {
											serviceList.push(value.id);
										});
								$rootScope.startLoader();
								$scope.encounterSearchObject.fromDate = moment(
										$scope.datepickerFromDate).format(
										'YYYY-MM-DD');
								$scope.encounterSearchObject.toDate = moment(
										$scope.datepickerToDate).format(
										'YYYY-MM-DD');
								
								$scope.encounterSearchObject.visitList = visitList;
								$scope.encounterSearchObject.deptList = deptList;
								$scope.encounterSearchObject.serviceList = serviceList;
								$scope.encounterSearchObject.radiologySpecialityId =cookieObjectAppConfig.radiologySpecialityId;
								$scope.isRisPatientDetailsOnFirstPage = cookieObjectAppConfig.isRisPatientDetailsOnFirstPage;
								console.log($scope.encounterSearchObject);

								var paramObj = {};
								var headers = {};

								//return false;
								var URI = BASE_URL + ROOT_URL
										+ "/api/opd/RIS/getradiologyWorkListForPrinting";

								GenericServiceParamHeader
										.serviceAction(METHOD_POST, URI,
												$scope.encounterSearchObject,
												NOTIFICATION_MSG_STATUS_FALSE,
												"", "")
										.then(
												function(response) {
													console.log(response);
													// $rootScope.startLoader();
													$scope.pagination.data = [];
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

								
								$scope.selectedVisitType=[];
								$scope.selectedtestList=[];
								$scope.selectedDepartMentList=[];
								
								$scope.patientName = '';
								$scope.uhidNumber = '';

								$scope.datepickerFromDate = new Date();
								$scope.datepickerToDate = new Date();
							}

							$scope.getReportDetails=function(radiology){
								radiology.isTempFinalRelease='Y';
						       	 $state.go("radiologyReport",{
						       		radiologyReportObject :radiology
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
							$scope.getToDate=function(){
								$scope.todateOptions.minDate=$scope.datepickerFromDate.getTime();
								
								if(moment($scope.datepickerToDate).isBefore(moment($scope.datepickerFromDate)))
								{
									 $scope.datepickerToDate=new Date($scope.datepickerFromDate.getTime());
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
								$scope.serviceName = info.serviceStandardName  +" | " +info.serviceStandardCode ;
							}
							
							 $scope.multiselectObj = {
							    		"id":"",
							    		"label":""
							    }
							
							$scope.getSubSpecialityBySpeciality = function() {
								var data = {
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									specialityId : $scope.deptId
								}
								console.log(data);
								var URI = BASE_URL + ROOT_URL
										+ GETSUBSPECIALITYBYSPECIALITYID;
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
														angular.forEach(response.data.listObject, function(value, key) {
															var multiselectObj = angular.copy($scope.multiselectObj);
																multiselectObj.id = value.subSpecialityId;
																multiselectObj.label = 	value.subSpecialityName;
																$scope.departmentList.push(multiselectObj);
															});

													}

												});
							}
							$scope.getSubSpecialityBySpeciality();
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
							$scope.getSpecialityAndSubspeciality();
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
					        
					        $scope.checkAll = function()
					        {
					        	if($scope.selectedAll)
					        		{
					        		var idex = 0;
					        		angular.forEach($scope.allEncounterList,function(resultEntryMaster,key) 
											{
					        			$scope.allEncounterList[idex].printReport=true;
					        			idex ++;
											});
					        		}else{
					        			var idex = 0;
						        		angular.forEach($scope.allEncounterList,function(resultEntryMaster,key) 
												{
						        			$scope.allEncounterList[idex].printReport=false;
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

					        $scope.printRadiologyReportWhatsApp=function(info,printWithHeader){
                                 if(info.isHtmlPrint==true){
                                    info.organizationId = $scope.organizationId;
                                    info.unitId =  $scope.unitId;
                                    info.printWithHeader = printWithHeader;
                                    info.unitName =  $scope.unitName;
                                    if(info.isFinalRelease == null){
                                        info.isFinalRelease = 'Y';
                                    }
                                    if(info.isProvisionalRelease == null){
                                        info.isProvisionalRelease = 'Y';
                                    }

                                    var URI = BASE_URL + ROOT_URL + "/api/opd/RIS/printReportTest";
                                    BillingGenericService.serviceAction(METHOD_POST, URI, info,NOTIFICATION_MSG_STATUS_FALSE).then(function(response)
                                            {

                                           if(response.data.status=='success')
                                            {
					    				   console.log("gg",response.data.object);


					    				   var headerSize = "";
					    					 var letterImgAndHeaderPdf="";
					    					 var patientDetailsPdf = "";
					    					 var completeTemplateContentPdf = "";
					    					 if($scope.isRisPatientDetailsOnFirstPage == 'N'){
					    						 //print patient details on all pages
					    						 headerSize = "220px;";
					    						 letterImgAndHeaderPdf = response.data.object.letterHeadImagePdf+response.data.object.headerPdf;
					    						 completeTemplateContentPdf = response.data.object.templateContent+response.data.object.footer;

					    					 }else{
					    						 //print patient details on first page
					    						 headerSize = "105px;";
					    						 letterImgAndHeaderPdf = response.data.object.letterHeadImagePdf;
					    						 if(info.printWithHeader == 0){
					    							 patientDetailsPdf = response.data.object.headerPdf;
					    						 }else{
					    							 patientDetailsPdf = response.data.object.headerWithoutImg;
					    						 }
					    						 completeTemplateContentPdf = patientDetailsPdf + response.data.object.templateContent+response.data.object.footer;
					    					 }

					    				   var printWindowForPdf = window.open('', '', 'height=500,width=700');

					    				   printWindowForPdf.document.write('<!DOCTYPE html> <html lang="en"><head></head>');
					    				   printWindowForPdf.document.write('<meta charset="UTF-8">');
					    				   printWindowForPdf.document.write('<meta name="viewport" content="width=device-width, initial-scale=1.0">');

                                           printWindowForPdf.document.write(' <style> '
                                                  +'.page-header, .page-header-space {height: '+headerSize+'} '
                                                  +' .page-footer, .page-footer-space { height: 105px;} '
                                                  + ' .page-footer { position: fixed; bottom: 0; width: 100%;} '
                                                  + ' .page-header { position: fixed; top: 0mm; width: 100%;} '
                                                  + ' .page {page-break-after: auto;} '
                                                  + '  @page { margin: 20mm}'
                                                  + 'p { margin:0px 0px 0px 0px; }'
                                                  + ' </style>' );

                                           printWindowForPdf.document.write('<body>');

                                           if(info.printWithHeader == 0){
                                                 printWindowForPdf.document.write('<div class="page-header" style="text-align: center">');
                                                 printWindowForPdf.document.write(letterImgAndHeaderPdf);
                                                 printWindowForPdf.document.write('</div>');

                                           }else if($scope.isRisPatientDetailsOnFirstPage == 'N'){
                                               printWindowForPdf.document.write('<div class="page-header" style="text-align: center">');
                                               printWindowForPdf.document.write(response.data.object.headerWithoutImg);
                                               printWindowForPdf.document.write('</div>');
                                           }

                                           printWindowForPdf.document.write('<table>');
                                           printWindowForPdf.document.write(' <tbody><tr><td><div class="page" style="line-height: 1.2;">');
                                           printWindowForPdf.document.write(completeTemplateContentPdf);
                                           printWindowForPdf.document.write('</div> </td></tr></tbody>');
                                           printWindowForPdf.document.write(' <tfoot> <tr><td><div class="page-footer-space"></div></td></tr></tfoot></table>');

                                           if(info.printWithHeader == 0){
                                               printWindowForPdf.document.write('<div class="page-footer" style="text-align: center">');
                                               printWindowForPdf.document.write(response.data.object.footerImg);
                                               printWindowForPdf.document.write('</div>');
                                           }
                                           printWindowForPdf.document.write('</body>');
                                           printWindowForPdf.document.write('</html>');

                                           $scope.htmlContent = printWindowForPdf.document.documentElement.outerHTML;

                                           printWindowForPdf.document.close();
//                                           if(info.isFinalRelease == 'Y'){
//                                        	   $scope.sendReport(info ,htmlContent);
////                                        	   $scope.generatePdf(info ,response);
//                                           }
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

					        
                         $scope.printRadiologyReport=function(info,printWithHeader){
                        	 if(info.isHtmlPrint==true){
					        	info.organizationId = $scope.organizationId;
					        	info.unitId =  $scope.unitId;
					        	info.printWithHeader = printWithHeader;
                                info.unitName =  $scope.unitName;
                                if(info.isFinalRelease == null){
                                	info.isFinalRelease = 'Y';
                                }
                                if(info.isProvisionalRelease == null){
                                	info.isProvisionalRelease = 'Y';
                                }
                                
					        	var URI = BASE_URL + ROOT_URL + "/api/opd/RIS/printReportTest";
					    		BillingGenericService.serviceAction(METHOD_POST, URI, info,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
					    				{
					    	
					    			   if(response.data.status=='success')
					    			      {
					    				   console.log("gg",response.data.object);
					    				   //'<iframe src="https://s3.ap-south-1.amazonaws.com/his-docs/apollohospitals1/patient/documents/clinical/episodic-documents/op-coversheet/1_1_null_Sign.png" style="margin-left:462px;" width="49%" height="15%"></iframe>'+ 
					    				  /* var divContents = response.data.object.latterHeadImg + response.data.object.header+response.data.object.templateContent+response.data.object.footer;  
								            var printWindow = window.open('', '', 'height=500,width=700');  
								            printWindow.document.write('<html><head>');  
								            printWindow.document.write('</head><body >');  
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
                                                   +'.page-header, .page-header-space {height: '+headerSize+'} '
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
 
					        $scope.printReports= function(printFlag,printWithHeader) 
							{
					        	$scope.selectedList=[];					        	
					        	//angular.forEach($scope.allEncounterList,function(resultEntryMaster,key) 
					        	angular.forEach($scope.pagination.data,function(resultEntryMaster,key)
										{
											if(resultEntryMaster.printReport==true)
												{
												 $scope.selectedList.push(resultEntryMaster.templateTransactionId);
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
										//angular.forEach($scope.allEncounterList,function(resultEntryMaster,key) 
										angular.forEach($scope.pagination.data,function(resultEntryMaster,key) 
												{
													if(resultEntryMaster.printReport==true)
														{
															var templateTransaction =
															{
																templateTransactionId :resultEntryMaster.templateTransactionId,
																isReportRelease:'Y',
																templateObject:resultEntryMaster.templateObject
																
															}
														 var URI = BASE_URL + ROOT_URL + "/global/template/transaction";
											        	    BillingGenericService.serviceAction(METHOD_PUT,URI,templateTransaction,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
												    			if (response.data.status == "success")
												    			{												    			
																	$scope.searchEncounter();
																	$scope.printRadiologyReport(resultEntryMaster,printWithHeader);
																	/*resultEntryMaster.organizationId = $scope.organizationId;
																	resultEntryMaster.unitId =  $scope.unitId;
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
																     hiddenField1.setAttribute("value", JSON.stringify(resultEntryMaster));
																 	 form.appendChild(hiddenField1);
																     document.body.appendChild(form);
																     $("form[name='Radiology Report']").submit();*/
																}
												    		});
														
														}
												});
										
									} catch (e) {
										$rootScope.stopLoader();
										console.log("Exception",e.message);
									}
								}

					         $scope.printReportsWhatsAPP = function(printFlag,printWithHeader)
                            							{
                            					        	$scope.selectedList=[];
                            					        	//angular.forEach($scope.allEncounterList,function(resultEntryMaster,key)
                            					        	angular.forEach($scope.pagination.data,function(resultEntryMaster,key)
                            										{
                            											if(resultEntryMaster.printReport==true)
                            												{
                            												 $scope.selectedList.push(resultEntryMaster.templateTransactionId);
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
                            										//angular.forEach($scope.allEncounterList,function(resultEntryMaster,key)
                            										angular.forEach($scope.pagination.data,function(resultEntryMaster,key)
                            												{
                            													if(resultEntryMaster.printReport==true)
                            														{
                            															var templateTransaction =
                            															{
                            																templateTransactionId :resultEntryMaster.templateTransactionId,
                            																isReportRelease:'Y',
                            																templateObject:resultEntryMaster.templateObject

                            															}
                            														 var URI = BASE_URL + ROOT_URL + "/global/template/transaction";
                            											        	    BillingGenericService.serviceAction(METHOD_PUT,URI,templateTransaction,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
                            												    			if (response.data.status == "success")
                            												    			{
                            																	$scope.searchEncounter();
                            																	$scope.printRadiologyReportWhatsApp(resultEntryMaster,printWithHeader);

                            																}
                            												    		});

                            														}
                            												});

                            									} catch (e) {
                            										$rootScope.stopLoader();
                            										console.log("Exception",e.message);
                            									}
                            								}
					        
  							 $scope.patientName = "";
	    						 $scope.printReportsTemp = function() {
	    							 $scope.patientName = "";
	    							 var countFlag = false;
	    							// angular.forEach($scope.allEncounterList,function(resultEntryMaster,key)
	    							 angular.forEach($scope.pagination.data,function(resultEntryMaster,key) 
		  										{
	    								 			var  i = 0 ;
		  											if(resultEntryMaster.printReport==true && resultEntryMaster.billSettled == false)
		  												{
		  												countFlag = true;
		  												if(i == 0){
		  													$scope.patientName = resultEntryMaster.patientName; 
		  													i = 1;
		  												}else{
		  													
		  													$scope.patientName = $scope.patientName+" ,"+resultEntryMaster.patientName; 
		  												}
		  												
		  												}
		  										});
	    							 
	    							 if(countFlag == true){
	    								 $("#resourceNotAvailable").modal("show");
	    								 
	    							 }else{
	    								 $('#printReport').modal('show');
	    								 
	    							 }
	    						 }
  						  $scope.finalPrint = function(){
  							 $('#printReport').modal('show');
  						  }
					        
					        
  						//photo capturing 

							$scope.filepreview = "";
							//for star camera
							  var _video = null,
						        patData = null;

						    $scope.patOpts = {x: 0, y: 0, w: 25, h: 25};

						    // Setup a channel to receive a video property
						    // with a reference to the video element
						    // See the HTML binding in main.html
						    $scope.channel = {};

						    $scope.webcamError = false;
						    $scope.onError = function (err) {
						        $scope.$apply(
						            function() {
						                $scope.webcamError = err;
						            }
						        );
						    };

						    $scope.onSuccess = function () {
						        // The video element contains the captured camera data
						        _video = $scope.channel.video;
						        _video.width = 117;
						        _video.height = 100;
						        $scope.$apply(function() {
						        	//alert(_video.width);
						            $scope.patOpts.w = _video.width;
						            $scope.patOpts.h = _video.height;
						            //$scope.showDemos = true;
						        });
						    };

						    $scope.onStream = function (stream) {
						        // You could do something manually with the stream.
						    };

						    $scope.orderDetailsIdForUpload = "";
							$scope.makeSnapshot = function(type) {
						        if (_video) {
						        	
						            var patCanvas = document.querySelector('#snapshot'+type+$scope.orderDetailsIdForUpload);
						            if (!patCanvas) return;
						            //alert(_video.width);
						            patCanvas.width = _video.width;
						            patCanvas.height = _video.height;
						            var ctxPat = patCanvas.getContext('2d');

						            var idata = getVideoData($scope.patOpts.x, $scope.patOpts.y, $scope.patOpts.w, $scope.patOpts.h);
						            ctxPat.putImageData(idata, 0, 0);

						            sendSnapshotToServer(patCanvas.toDataURL());

						            patData = idata;
						        }
						    };
						    
						    /**
						     * Redirect the browser to the URL given.
						     * Used to download the image by passing a dataURL string
						     */
						    var getVideoData = function getVideoData(x, y, w, h) {
						        var hiddenCanvas = document.createElement('canvas');
						        hiddenCanvas.width = _video.width;
						        hiddenCanvas.height = _video.height;
						        var ctx = hiddenCanvas.getContext('2d');
						        ctx.drawImage(_video, 0, 0, _video.width, _video.height);
						        return ctx.getImageData(x, y, w, h);
						    };

						    /**
						     * This function could be used to send the image data
						     * to a backend server that expects base64 encoded images.
						     *
						     * In this example, we simply store it in the scope for display.
						     */
						    var sendSnapshotToServer = function sendSnapshotToServer(imgBase64) {
						        $scope.snapshotData = imgBase64;
						    };
							
							//for end camera
							 $scope.imageObj = {};	
							
							$scope.openPopupForUpload = function(info){
								$scope.orderDetailsIdForUpload = info.orderDetailsId;
								$scope.imageObj.pImage = info.pImage;
								 $scope.imageObj.fileName = info.fileName;
								 
								$("#imgCapturingModal").modal("show");
							}
							
							 $scope.convertBase64ToImage = function (dataurl, filename) {
								    var arr = dataurl.split(','), mime = arr[0].match(/:(.*?);/)[1],
								        bstr = atob(arr[1]), n = bstr.length, u8arr = new Uint8Array(n);
								    while(n--){
								        u8arr[n] = bstr.charCodeAt(n);
								    }
								    return new File([u8arr], filename, {type:mime});
								}
							
							$scope.uploadImage = function(){
								$rootScope.startLoaderPopup();
								if($scope.snapshotData == undefined){
									 growl.warning("Please Capture Photo",
												{
													title: 'warning!'
												});	
									 $rootScope.stopLoaderPopup();
						    		 return false;
								}
								var inputFile =$scope.convertBase64ToImage($scope.snapshotData,moment().toDate().getTime()+".jpeg");
								 
								var fd = new FormData();
								fd.append('orderDetailsId',$scope.orderDetailsIdForUpload );
						    	fd.append('labSampleDtlsId',0);
						    	fd.append('unitId',$scope.unitId);
						    	fd.append('organizationId',$scope.organizationId);
						    	fd.append('file',inputFile);
						    	var URI = BASE_URL + ROOT_URL + "/LIS/ReportHandOver/saveReportHandOverImage";
						     GenericServiceFileUpload.serviceAction("POST",URI,fd,{ 'Content-Type': undefined}).then(function(response)
						    	{
						    	 if (response.data.status == "success") {
						    		 growl.success(response.data.status,
												{
													title: 'success!'
												});	
						    		
						    		 $rootScope.stopLoaderPopup();
						    		 var patCanvas = document.querySelector('#snapshot'+1+$scope.orderDetailsIdForUpload);
						    		 patCanvas.width = 0;
						    	 }
						    	 $scope.searchEncounter();
						    	 $rootScope.stopLoaderPopup();
						    	 $("#imgCapturingModal").modal("hide");
						    });
						     $rootScope.stopLoaderPopup();
						}  
					        
					
							//for Send mail
							$scope.checkPrint= function(mailSendFlag)
							
							{
								 $scope.mailSendFlagPrint = mailSendFlag;
								 $scope.sendMailButton = false;
								 $scope.patientName = "";
    							 var countFlag = false;
								$scope.patientList = [];
								var groupBy = function(xs, key) {
									  return xs.reduce(function(rv, x) {
									    (rv[x[key]] = rv[x[key]] || []).push(x);
									    return rv;
									  }, {});
									};

								   var checkList = [];	
									angular
									.forEach(
											$scope.pagination.data,
											function(resultEntryMaster,key) 
											{
												if(resultEntryMaster.printReport==true)
													{
													  checkList.push(resultEntryMaster);
													}
											});
									if(checkList.length==0)
										{
										growl
										.warning(
												"Please select test to Print.",
												{
													title : 'Warning!'
												});
										 return false
										}
									else{
										
										
									$scope.patientList =groupBy(checkList, 'patientId');
									if($scope.patientList!=null)
										var templateTransactionIds=[];
										{angular.forEach($scope.patientList,function(value,key){
//													
											if(value.templateTransactionId!=null)
												{
												templateTransactionIds.push(value.templateTransactionId);
												}
//																	
												value.templateTransactionIds = templateTransactionIds;
//															});
												});
										  }
									
										var  i = 0 ;
										 angular.forEach($scope.patientList,function(resultEntryMaster,key) 
			  										{
											
											 var  inLoop = 0;
											 var pName = ""; 
		    								 		angular.forEach(resultEntryMaster,function(val,kye) 
														{ 
		    								 			 $scope.patientVisit = val.visitTypeId;
		    								 			 $scope.paymentEntil = val.paymentEntitlementId;
		    				  							 $scope.BlockReportCompany = val.blockReportPrint;
				  											if(val.billSettled == false)
				  												{
					  												countFlag = true;
					  												inLoop = 1;
					  												pName = val.patientDetails
				  												}
														});
		    								 		
		    								 		if(inLoop == 1){
	  													
	  													if(i == 0){
		  													$scope.patientName = pName; 
		  													i = 1;
		  												}else{
		  													
		  													$scope.patientName = $scope.patientName+", "+pName; 
		  												}
	  												}
		    								 		
			  										});
		    							 if(mailSendFlag == 0){
		    								 
		    								 	if(countFlag == true){
			    								 
		    								 	if($scope.paymentEntil == 1){
		    								 		
		    								 		if($scope.blockReportPrint!= null && $scope.blockReportPrint != "" && $scope.blockReportPrint == 'Y' && $scope.patientVisit == 1 ){
		                                                 if($scope.enableBlockReport != null && $scope.enableBlockReport == true){
		                                                     
		                                                	 $('#resourceNotAvailable').modal('show');

		                                                 }else{
		                                                 	growl .warning( "You do not have rights to print the report.",
														            {
															            title : 'Warning!'
														               });
												                 return false;
		                                                 }
		                                            }else{
		                                            	$('#printReport').modal('show');
		                                            }
		    								 		
		    								 	}else{
		    								 		
		    								 		if($scope.BlockReportCompany!= null && $scope.BlockReportCompany != "" && $scope.BlockReportCompany == 'Y' && $scope.patientVisit == 1 ){
		                                                 if($scope.enableBlockReport != null && $scope.enableBlockReport == true){
		                                                     
		                                                	 $('#resourceNotAvailable').modal('show');

		                                                 }else{
		                                                 	growl .warning( "You do not have rights to print the report.",
														            {
															            title : 'Warning!'
														               });
												                 return false;
		                                                 }
		                                            }else{
		                                            	$('#printReport').modal('show');
		                                            }
		    								 		
		    								 	}	
			    								 
			    								 
			    							 }else{
			    								 $('#printReport').modal('show');
			    								 
			    							 }
		    							 }else{
		    								 
		    								 if(countFlag == true){
			    								 if($scope.paymentEntil == 1){
			    									 
			    									 if($scope.blockReportPrint!= null && $scope.blockReportPrint != "" && $scope.blockReportPrint == 'Y' && $scope.patientVisit == 1 ){
		                                                 if($scope.enableBlockReport != null && $scope.enableBlockReport == true){
		                                                     
		                                                	 $('#blockReport').modal('show');

		                                                 }else{
		                                                 	growl .warning( "You do not have rights to send the report email.",
														            {
															            title : 'Warning!'
														               });
												                 return false;
		                                                 }
		                                            }else{
		                                            	$('#reportSendOnMail').modal('show');
		                                            }
			    									 
			    								 }else{
			    									 
			    									 if($scope.BlockReportCompany!= null && $scope.BlockReportCompany != "" && $scope.BlockReportCompany == 'Y' && $scope.patientVisit == 1 ){
		                                                 if($scope.enableBlockReport != null && $scope.enableBlockReport == true){
		                                                     
		                                                	 $('#blockReport').modal('show');

		                                                 }else{
		                                                 	growl .warning( "You do not have rights to send the report email.",
														            {
															            title : 'Warning!'
														               });
												                 return false;
		                                                 }
		                                            }else{
		                                            	$('#reportSendOnMail').modal('show');
		                                            }
			    								 }
			    								 
			    								 
			    							 }else{
			    							 	$('#reportSendOnMail').modal('show');
			    							 }
		    							 }
										 
									   }
							}
							
							// for send report
							 $scope.sendReport = function(){
								 $scope.sendMailButton = true;
								 $scope.newList = [];
								 $rootScope.startLoader();
									angular.forEach($scope.patientList,function(patient,patientId) 
											{
													 var newData = {};
													  var count = 0 ;
//													   $scope.templateTransactionIdsList = [];
										 			angular.forEach(patient,function(val,kye) 
													{
//														$scope.templateTransactionIdsList.push(val.templateTransactionId);
														if(count == 0 ){
															newData.testDesc =  val.serviceName; 
														}else{
															 newData.testDesc =  newData.testDesc +","+val.serviceName;
														}
														if(val.patientId!=null){
															newData.patientId = val.patientId;
														}
											 			newData.orgId =  val.orgId;
											 			newData.organizationId =  $scope.organizationId;
											 			newData.unitId =  $scope.unitId;
											 			newData.doctorId =  val.doctorId;
											 			newData.createdBy =  $scope.userId;
											 			newData.templateTransactionId = val.templateTransactionId;
											 			 newData.encounterId = val.encounterId;
                                                         newData.payeeId = val.payeeId;
                                                         newData.paymentEntitlementId = val.paymentEntitlementId;
                                                         newData.orderId = val.orderId;
                                                         newData.htmlContent = $scope.htmlContent;
                                                         newData.isFinalRelease = 'N';
											 			 if(val.paymentEntitlementId!=null && val.paymentEntitlementId == 2){
									            			  newData.insuranceId = val.payeeId;
									            			  newData.companyId = null;
														     }else if(val.paymentEntitlementId!=null && val.paymentEntitlementId == 3){
														    	 newData.companyId = val.payeeId;
														    	 newData.insuranceId = null;
														     }else{
														    	 newData.companyId = null;
														    	 newData.insuranceId = null;
														    }
											 			count++;
													});
//								  					newData.templateTransactionIds = $scope.templateTransactionIdsList;
					 			                    $scope.newList.push(newData);
										 			
											});		
									
									  console.log("$scope.newList",$scope.newList);
								 
								 var URI = BASE_URL + ROOT_URL + "/api/opd/RIS/sendRadiologyReportOnMail";
									GenericService.serviceAction("POST",URI, $scope.newList).then(function(response){
										if (response.data.status == 'success')
										{
											 $scope.sendMailButton = false;
											if(response.data.message!=null){
												growl.warning(response.data.message,
														{
															title : 'warning!'
														});
											}else{
												growl.success(response.data.status,
														{
															title : 'Success!'
														});
											}
											 
										} 
										 $scope.sendMailButton = false;
										$rootScope.stopLoader();
										$scope.initReportHandover($scope.orgId,$scope.orgUnitId,$scope.deptId,$scope.offset,$scope.noOfRecordsPerPage);
							
									});
							 }


//                -------------------- Upload Documents Start ------------------------

							$scope.getPatientDocumentList = function(obj){

                                  $rootScope.startLoader();
                                  var data =
                                  {
                                    "patientId":obj.patientId,
                                    "unitId":$scope.unitId,
                                    "organisationId":$scope.organizationId,
                                    "userId":undefined,
                                    "visitTypeId":undefined,
                                    "admissionId":undefined,
                                  }
                                  var URI = BASE_URL + ROOT_URL + GET_PATIENT_DOCUMENT_ENCOUNTER;
                                  BillingGenericService.serviceAction(METHOD_POST, URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
                                    $scope.patientDocumentList = [];
                                    if (response.data.status == "success")
                                    {
                                        $scope.patientDocumentList = response.data.listObject;

                                        $rootScope.stopLoader();
                                    }
                                    else if(response.data.status == "error")
                                     {
                                        $rootScope.stopLoader();
                                     }
                                  });
						    }

							$scope.encounterDetailsList = [];
							$scope.encounterDetails = {
                                  encounterDocId : "",
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
                                  file : {},
                                  remark : ""
							}

							$scope.showDocUPloadPoup   = function(encounterObj)
							{
								 $('#fileId').val(undefined);
								$scope.encounterDetailsList=[];
								$scope.encounterDetails.patientId = encounterObj.patientId;
								if($scope.encounterDetailsList.length>0)
								{
								    $scope.uploadDocBtn = false;
								    $('#uploadDetails').modal('show');
								}
							else{
                                $scope.uploadDocBtn = false;
                                 $('#uploadDetails').modal('show');
                                var encounterDetails = angular.copy($scope.encounterDetails);
                                encounterDetails.patientId = encounterObj.patientId;
                                $scope.encounterDetailsList.push(encounterDetails);
							}
								$scope.getPatientDocumentList(encounterObj);

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

                             $scope.uploadDocBtn = false;
							 $scope.uploadPatientImage = function(){

                                  $scope.uploadDocBtn = true;
                                  $scope.finalDocUploadList=[];
                                  var allowedTypes = ['pdf','png', 'jpeg', 'jpg', 'doc', 'docx'];
                                  var count=0;
                                  angular.forEach($scope.encounterDetailsList, function(value, key) {
                                      if(value.documentTypeId==undefined || value.documentTypeId=="" || value.documentTypeId==null){
                                             growl.warning("Please select document type .",
                                            {
                                                title : 'warning!'
                                            });
                                            $rootScope.stopLoader();
                                            $scope.uploadDocBtn = false;
                                            return false;
                                      }
                                      if (angular.equals(value.file, {}) || value.file==undefined) {
                                            growl.warning('Please select file.',
                                                            {
                                                                title : 'warning!'
                                                            });
                                             $rootScope.stopLoader();
                                             $scope.uploadDocBtn = false;
                                            return false;
                                      }
                                      else
                                     {
                                        var fileType = value.file.name.split('.').pop().toLowerCase();
                                        if (allowedTypes.indexOf(fileType) === -1) {
                                             growl.warning("Uploaded file format is incorrect",
                                            {
                                                title : 'warning!'
                                            });

                                            $scope.uploadDocBtn = false;
                                            return false;
                                        }

                                     }
                                    $scope.finalDocUploadList.push(value);
                                    console.log("final list",$scope.finalDoc);
                                  });

								 if($scope.encounterDetailsList.length==$scope.finalDocUploadList.length){
									   $('#uploadDetails').modal('hide');

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
                                                   $scope.finalDocUploadList=[];
                                                   $scope.encounterDetailsList = [];
                                                   $scope.uploadDocBtn = false;
                                                   $rootScope.stopLoader();
                                                 }
                                              }
                                              else
                                              {
                                                    growl.error('Something went wrong.',
                                                    {
                                                        title : 'Error!'
                                                    });
                                                    $scope.uploadDocBtn = false;
                                              }
                                        });

									});
								 }
							}

                            $scope.uploadEncounterDoc = function(Obj)
                            {
                                 var fd = new FormData();
                                    fd.append('encounterId', 0);
                                    fd.append('visitTypeId', 1);
                                    fd.append('admissionId', 0);
                                    fd.append('patientId',Obj.patientId );
                                    fd.append('documentTypeId', Obj.documentTypeId);
                                    fd.append('createdBy',Obj.createdBy );
                                    fd.append('updatedBy', Obj.updatedBy);
                                    fd.append('unitId', Obj.unitId);
                                    fd.append('organisationId', Obj.organisationId);
                                    fd.append('expenseId', 0);
                                    fd.append('file',Obj.file );
                                    fd.append('expenseId', 0);
                                    fd.append('grnId',0);
                                    fd.append('poId',0);
                                    fd.append('incidentReportingMapperId',0);
                                    fd.append('companyExpenseId',0);
                                    fd.append('remark',Obj.remark );
                                    fd.append('createdDate',null);
                                    fd.append('fileEditedName',null);
                                    fd.append('opbId',0);
                                    fd.append('vendorId',0);
                                    fd.append('vendorId',0);
                                    var URI1 = BASE_URL + ROOT_URL + UPLOAD_ENCOUNTER_DOCUMENT;
                                    return GenericServiceFileUpload.serviceAction("POST",URI1,fd,{ 'Content-Type': undefined}).then(function(response)
                                    {
                                        return response;
                                    });
                            }


                        $scope.removeDocument = function(Obj, index)
                        {
                            try {
                                $rootScope.startLoader();
                                var data = {
                                        "encounterDocId":Obj.encounterDocId
                                    };
                                var URI = BASE_URL + ROOT_URL+"/api/opd/patientDoc/status/update";

                                GenericService
                                        .serviceAction(METHOD_POST, URI, data)
                                        .then(
                                                function(response) {
                                                    $rootScope.stopLoader();
                                                    if(response.data.status == "success"){
                                                        growl.success("Document removed successfully ",
                                                                {
                                                                    title : 'Success!'
                                                                });
                                                        $scope.patientDocumentList.splice(index,1);
                                                    }
                                                });
                            } catch (e) {
                                console.log(e.message)
                            }
                        }
//                -------------------- Upload Documents End ------------------------


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

                                $scope.whatsAppObj = {};
                                $scope.newWhatsAppNumber = '';
                                $scope.openPopupForWhatsApp = function(info){
                                $scope.printReportsWhatsAPP(1,0);
                                    if(info.templateTransactionId !== undefined && info.templateTransactionId != null){
                                          $scope.whatsAppObj = info;
                                          $scope.newWhatsAppNumber = '+91'+$scope.whatsAppObj.mobileNumber;
                                          $("#manualWhatsAppConfirmation").modal("show");
                                    }
                                    else{
                                        growl.error('Template TransactionId not found.',
                                        {
                                            title : 'Error!'
                                        });
                                    }
                                }

                                $scope.sendWhatsAppManually = function(){
                                    if($scope.newWhatsAppNumber !== undefined && $scope.newWhatsAppNumber !== null && $scope.newWhatsAppNumber !== ""){

                                        if ($scope.newWhatsAppNumber.includes("+")) {
                                            $scope.whatsAppObj.newWhatsAppNo =  $scope.newWhatsAppNumber.replace("+","");
                                        }else{
                                               $scope.whatsAppObj.newWhatsAppNo =  $scope.newWhatsAppNumber;
                                        }

                                        if($scope.whatsAppObj.newWhatsAppNo.length >=10 && $scope.whatsAppObj.newWhatsAppNo.length <= 12){
                                            if($scope.whatsAppObj.newWhatsAppNo.length === 10){
                                                $scope.whatsAppObj.newWhatsAppNo = "91"+$scope.whatsAppObj.newWhatsAppNo;
                                            }
                                            else if($scope.whatsAppObj.newWhatsAppNo.length === 11){
                                                growl.warning("Enter valid whatsapp number",
                                                {
                                                    title : 'Warning!'
                                                });
                                            }

                                            $scope.newList = [];
                                            var newData = {};
                                            newData.patientId = $scope.whatsAppObj.patientId;
                                            newData.testDesc = $scope.whatsAppObj.serviceName;
                                            newData.orgId =  $scope.orgId;
                                            newData.organizationId =  $scope.organizationId;
                                            newData.unitId =  $scope.unitId;
                                            newData.doctorId =  $scope.whatsAppObj.doctorId;
                                            newData.createdBy =  $scope.userId;
                                            newData.newWhatsAppNo =  $scope.whatsAppObj.newWhatsAppNo;
                                            newData.templateTransactionId = $scope.whatsAppObj.templateTransactionId;
                                            newData.encounterId = $scope.whatsAppObj.encounterId;
                                            newData.payeeId = $scope.whatsAppObj.payeeId;
                                            newData.paymentEntitlementId = $scope.whatsAppObj.paymentEntitlementId;
                                            newData.orderId = $scope.whatsAppObj.orderId;
                                            newData.htmlContent = $scope.htmlContent;
                                             newData.isFinalRelease = 'N';
                                            $scope.newList.push(newData);

                                            var URI = BASE_URL + ROOT_URL + "/api/opd/RIS/sendRadiologyReportOnMail";

                                            GenericService.serviceAction("POST", URI, $scope.newList).then(function(response)
                                            {
                                                if (response.data.status == 'success')
                                                {
                                                    growl.success("Whatsapp sent successfully",
                                                    {
                                                        title : 'Success!'
                                                    });
                                                }
                                               else{
                                                    growl.error(response.data.message,
                                                    {
                                                        title : 'Error!'
                                                    });
                                               }
                                            });
                                        }
                                        else{
                                            growl.warning("Enter valid whatsapp number",
                                            {
                                                title : 'Warning!'
                                            });
                                        }
                                    }
                                    else{
                                        growl.warning("Enter valid whatsapp number",
                                        {
                                            title : 'Warning!'
                                        });
                                    }
                                };
					 


						} ]);