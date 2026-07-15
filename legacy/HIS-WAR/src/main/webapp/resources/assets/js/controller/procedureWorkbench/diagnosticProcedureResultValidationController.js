'use strict';

/**
 * @Author By  Vivek Satle
 * @name myApp.controller:radiologyWorkbenchController
 * @description #radiologyWorkbenchController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'diagnosticProcedureResultValidationController',
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
						'$window',
						function($scope, $cookies, $rootScope, GenericService,
								BillingGenericService, $state, $timeout,
								$interval, GenericServiceParamHeader,$compile,reportService,$window) {

							
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
								$scope.unitName = cookieObject.unitNme;
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

								//start procedure 
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
						
						var URI = BASE_URL + ROOT_URL+ "/api/dignostic/resultValidation";
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
						$scope.selectedModalityList = [];
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
						procedure.isResultEntry='N';
				       	 $state.go("diagnosticProcedureReport",{
				       		procedureReportObject :procedure
				       	 }); 
				       		
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
							
							
						
							
					        $scope.printReport=function(info,printWithHeader)
					    	{
					    		
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
					    			             "paramValue": obj.templateTransactionId
					    		              }
					    		            ]
					    		 };
					    	  reportService.generate(REPORT_API_URI1,data);*/

					        	
					        	info.organizationId = $scope.organizationId;
					        	info.unitId =  $scope.unitId;
					        	info.printWithHeader = printWithHeader;
					        	info.unitName =  $scope.unitName;
					        	if(info.isHtmlPrint==true){
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