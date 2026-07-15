'use strict';

/**
 * @Author By  Vivek Satle
 * @name myApp.controller:radiologyWorkbenchController
 * @description #radiologyWorkbenchController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'radiologyReportController',
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
						'$stateParams',
						'growl',
						'$filter',
						'CONSTANTS',
						function($scope, $cookies, $rootScope, GenericService,
								BillingGenericService, $state, $timeout,
								$interval, GenericServiceParamHeader,$compile,reportService,$stateParams,growl,$filter,CONSTANTS) {

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
							 
							 $scope.skipRISReportEntry = cookieObjectAppConfig.skipRISReportEntry;
							 $scope.isRisPatientDetailsOnFirstPage = cookieObjectAppConfig.isRisPatientDetailsOnFirstPage;
							 if(cookieObjectAppConfig.radiologySpecialityId!=null || cookieObjectAppConfig.radiologySpecialityId !="" || cookieObjectAppConfig.radiologySpecialityId!=undefined)
								{
									$scope.radiologySpecialityId = cookieObjectAppConfig.radiologySpecialityId;
								}else{
									growl.warning("Radiology department is not configured...!!",{
										title : 'Warning!'
									});
									return false;
								}
							 
							 var cookieObject = $cookies.getObject('cookieObject');
								if (cookieObject == undefined) {
									$state.go('login');
									return;
								}
							/* init() function for form object create. */
							$scope.init = function() {

							

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
								$scope.storeId = cookieObject.storeId;
								
								$rootScope.loginpage = true;
								$scope.saveBtnFlag = false;
								$scope.updateBtnFlag = true;

								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup
								
								
								var speArr = [];
								speArr.push(cookieObjectAppConfig.radiologySpecialityId);
								 var data = {
	                            		  organizationId:$scope.organizationId,
	                            		  unitId:$scope.unitId,
	                            		  specialityArray:speArr
	                              }
	                              //console.log(data);
	                              var URI = BASE_URL + ROOT_URL
									+ GETDOCTORSLISTAGANISTSPECIALITIES;
							BillingGenericService
									.serviceAction(METHOD_POST, URI,
											data,
											NOTIFICATION_MSG_STATUS_FALSE)
									.then(
											function(response) {
												//console.log(response);
												// $rootScope.startLoader();
												if (response.data.status == "success")
													{
														$scope.doctorsList = response.data.listObject;
													}
													
											});
							
							
								
								
							}

							$scope.init();
							
							$scope.itemList = [];
							  $scope.getInventoryItemList = function(serviceMasterIds){
								  $rootScope.startLoader();
								  var data = {
										  serviceIds : serviceMasterIds,
											organizationId : $scope.organizationId,
											unitId : $scope.unitId
										};
										var URI = BASE_URL + ROOT_URL + "/billing/resultEntry/getServiceItemMapperDetailsByServiceId";
								  GenericService.serviceAction("POST", URI, data).then(function(response) {
												$scope.itemList = [];
												 $rootScope.stopLoader();
												if (response.data.status == "success") {
													
													if(response.data.listObject != null && response.data.listObject.length>0){
														angular.forEach(response.data.listObject,function(value1,index1) {
															  
															if(value1.batchReq == true){
																 $scope.itemList.push(value1);
																 value1.batchReq = value1.batchReq;
																 value1.consumedQuantity = value1.assignQty;
																 value1.newAssignQty = value1.assignQty;
															}else{
																value1.batchReq = value1.batchReq;
																value1.selectedBatch = {};
																value1.selectedBatch.batchId = value1.batchList[0].batchId;
																value1.selectedUom = {};
																value1.selectedUom.uomTypeId = value1.batchList[0].uom[2].uomTypeId;
																value1.selectedUom.uomUnitId = value1.batchList[0].uom[2].uomUnitId;
																value1.availableQuantity = value1.batchList[0].availableStockQuantity;
																value1.consumedQuantity = value1.assignQty;
																value1.newAssignQty = value1.assignQty;
																$scope.itemList.push(value1);
															}
														});
													}
												}
											});
							  }
							
							  //get StoreList 
						      $scope.storeList = [];
						      $scope.storeId = cookieObject.storeId;
						      $scope.getStoreList = function(){
									 var URI =  BASE_URL+ROOT_URL+"/api/global/getDetailedStoreList/"+$scope.userId+"/"+$scope.organizationId+"/"+$scope.unitId;
							            $rootScope.startLoader();
							            GenericService.serviceAction("GET", URI, {}).then(
							                function (response) {
							                    $scope.storeList = response.data.listObject;
							                    $rootScope.stopLoader();
							                	console.log("$scope.store",$scope.store);
												console.log("$scope.storeList",$scope.storeList);
							                });
								}
							$scope.selectTemplate  = function(radiologyReport)
					    	{
					       		if(radiologyReport!=null && radiologyReport!=undefined){
					       			$scope.radiologyReport = radiologyReport;
					       		}
					    		
					    	}
							
							
						$scope.SelectAllItems = function () {
					            if ($scope.selectAllRows) {
					            	$scope.SelectedItemsIds = $scope.searchNewItemList.map(function (item) {
					                    item.isSelected = true;
					                   // self.model.isItemChecked = true;

					                    return item.itemId;
					                });
					            } else {
					            	$scope.SelectedItemsIds = [];
					            	$scope.searchNewItemList.forEach(function (item) {
					                    item.isSelected = false;
					                    //self.model.isItemChecked = false;

					                });
					            }
					        }
							
							var isAddedTemplate = false;
					       	$scope.addTemplate = function()					       
					    	{
					       		if(Object.keys($scope.radiologyReport).length === 0  || $scope.radiologyReport=="-"){
					       			growl.warning("Please select Word Template...!",{
										title : ERROR_MSG
									});
					        		return;
					       		}
					       		if($scope.radiologyReport!=null && $scope.radiologyReport!=undefined && $scope.radiologyReport!="-")
					       			{
					       			
					       			

	    				   			var obj = {
		    				   				patientId : $scope.patientDetails.patientId,
		    				   				encounterId :$scope.patientDetails.encounterId,
		    				   				admissionId : $scope.patientDetails.admissionId,
		    				   				orderDetailsId : $scope.patientDetails.orderDetailsId,
		    				   				templateDesc : $scope.radiologyReport.templateObject
		    				   		}
								
									
						        	$rootScope.startLoader();
					        	    var URI = BASE_URL + ROOT_URL + "/api/opd/appendCensentData";
					        	    GenericService.serviceAction(METHOD_POST,URI,obj).then(function(response) {
						    			if (response.data.status == "success")
						    			{
						    				$scope.radiologyReport.templateObject = response.data.object.templateDesc;
						    				if($scope.radioLogyReportObj.isHtmlPrint == undefined || $scope.radioLogyReportObj.isHtmlPrint == null){
						    					$scope.radioLogyReportObj.isHtmlPrint = $scope.radiologyReport.isHtmlPrint;
						    				}
						    				$scope.radiologyReport= angular.fromJson($scope.radiologyReport);
							       			$scope.radioLogyReportObj.templateFormatId = 	$scope.radiologyReport.templateFormatId;
					    				   	if($scope.radiologyReport.templateFormatId==1)
					    				   		{
					    				  
					    				   		growl.warning("Please select Word Template...!",{
													title : ERROR_MSG
												});
								        		return;
						    				   		/*$scope.radiologyReport.templateObject  =  $scope.radiologyReport.templateObject;
						    				     	$scope.radiologyReport.templateFormatId  = $scope.radiologyReport.templateObject.templateFormatId;
						    				     	$scope.obj = JSON.parse($scope.radiologyReport.templateObject).obj;
							    				   	document.getElementById("htmlContentReport").innerHTML = JSON.parse($scope.radiologyReport.templateObject).html;
										    		var element = angular.element(document.getElementById("htmlContentReport"));
													   $compile(element)($scope);*/
					    				   		}
					    				    else if($scope.radiologyReport.templateFormatId==2)
					    			    	  {
					    				    	$rootScope.startLoader();
					    				    	$timeout( function(){
					    				    		$scope.value = '';
					    				    		$scope.value =$scope.radiologyReport.templateObject;
					    				    		$rootScope.stopLoader();
					    				    		},1000);
					    			    	  }
					    				   	/*$('#addtemplateModalRadio').modal('hide');
					    				   	$('#radiologyReport').modal('show');*/
						    			    isAddedTemplate = true;
					    				   	$rootScope.stopLoader();
						    			
						    			}
						    			else if(response.data.status == "error")
					    				{
						    				$rootScope.stopLoader();
					    				}
										
						    		});
	    				   		
					       			
					       			}
					       		else{
						    			growl.warning("Please select Template...!",{
											title : ERROR_MSG
										});
						        		return;
					       		}
					        	
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
							
					        $scope.savedConsumptionItems = [];
					        $scope.getSavedConsumptionDetails = function(templateTransactionId){
					        	var data = {
					        			organizationId : $scope.organizationId,
					        			unitId : $scope.unitId,
					        			requestFromId : templateTransactionId,
					        			storeId : $scope.storeId
					        	}
					        	 var URI =  BASE_URL+ROOT_URL+"/api/inventory/nursing/non/billable/consumption/radiology/getConsumptionDetailsByTemplateTransactionId";
						            $rootScope.startLoader();
						            GenericService.serviceAction("POST", URI, data).then(
						                function (response) {
						                    $scope.savedConsumptionItems = response.data.listObject;
						                    $rootScope.stopLoader();
						                });
					        }
							
							if (angular.equals({},
									$stateParams.radiologyReportObject)) {
								 $state.go("radiologyResultEntry",{searchObj :$stateParams.searchObj}); 
								//$state.go('radiologyResultEntry');
								return;
							} else {
								console.log('DJ$stateParams.radiologyReportObject',
										$stateParams.radiologyReportObject);
							
									if($stateParams.radiologyReportObject.isResultEntry=='Y'){
										 $(".selectedPageName").text("Radiology  Result Entry");
										 $state.current.title="Radiology  Result Entry";
									}else{
										 $(".selectedPageName").text("Radiology Result Validation");
										 $state.current.title=" Radiology Result Validation";
									}
								
								//for patient demography details start 
								$scope.patientDetails.uhIdNumber=$stateParams.radiologyReportObject.uhidNumber;
								$scope.patientDetails.patientName=$stateParams.radiologyReportObject.patientName;
								$scope.patientDetails.genderCode=$stateParams.radiologyReportObject.genderCode;
								$scope.patientDetails.age=$stateParams.radiologyReportObject.ageString;
								$scope.patientDetails.birthDate=$stateParams.radiologyReportObject.dob;
								$scope.patientDetails.visitTypeId=$stateParams.radiologyReportObject.visitTypeId;
								$scope.patientDetails.dateOfAdmission=$stateParams.radiologyReportObject.doa;
								$scope.patientDetails.patientDischargeDate=$stateParams.radiologyReportObject.pdd;
								$scope.patientDetails.bedNumber=$stateParams.radiologyReportObject.bedNumber;
								$scope.patientDetails.patientId=$stateParams.radiologyReportObject.patientId;
								$scope.patientDetails.admissionId=$stateParams.radiologyReportObject.admissionId;
								$scope.patientDetails.encounterId=$stateParams.radiologyReportObject.encounterId;
								$scope.patientDetails.orderDetailsId=$stateParams.radiologyReportObject.orderDetailsId;
								$scope.templateDoctorId = $stateParams.radiologyReportObject.templateDoctorId; 
								$scope.patientDetails.referralName = $stateParams.radiologyReportObject.referralName;
								if($scope.patientDetails.bedNumber=='0'){
									$scope.patientDetails.bedNumber='';
								}
								
								var dateDoa = moment($stateParams.radiologyReportObject.doa, 'DD/MM/YYYY');
								var datePdd = moment($stateParams.radiologyReportObject.pdd, 'DD/MM/YYYY');
								
								$scope.patientDetails.los = $rootScope.dateDiffrence(dateDoa,datePdd);
								
								$scope.patientDetails.doctorFullName=$stateParams.radiologyReportObject.doctorName;
								if($scope.patientDetails.doctorFullName != null){
								if($scope.patientDetails.visitTypeId!=1){
									$scope.patientDetails.doctorFullName=$scope.patientDetails.doctorFullName.toString().replace("Dr.","");
								}
								}
								//for patient demography details end 
								$scope.radioLogyReportObj = $stateParams.radiologyReportObject;
								var rediologyObj=$stateParams.radiologyReportObject;
								if($stateParams.radiologyReportObject.templateFormatId==null){
									rediologyObj.templateTransactionId=null;
								}
								
								//billable consumption get inventory item list
								$scope.serviceIds = []; 
								if($stateParams.radiologyReportObject.serviceId!=null && $stateParams.radiologyReportObject.isResultEntry=='Y'){
									$scope.serviceIds.push($stateParams.radiologyReportObject.serviceId);
									$scope.getInventoryItemList($scope.serviceIds);
									$scope.getStoreList();
								}
								
					        	$rootScope.startLoader();
					        	if(rediologyObj.templateTransactionId==null)
					        		{
					        		$scope.isTransactionFlag='N'; // for showing drop down list
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
//							    					$('#addtemplateModalRadio').modal('show');
							    				   	$rootScope.stopLoader();
							    				   	
							    				   	if(response.data.listObject.length==1)
                                                    {
                                                        $scope.info11 = response.data.listObject[0];
                                                        $scope.selectTemplate($scope.info11);
                                                        $scope.addTemplate();
                                                    }
                                                    else if(response.data.listObject.length > 1){
                                                        response.data.listObject.forEach(function (template) {
                                                            if(template.templateId === rediologyObj.templateId)
                                                            {
                                                                $scope.info11 = template;
                                                                $scope.info11.templateObject = rediologyObj.templateObject;
                                                                $scope.selectTemplate($scope.info11);
                                                                $scope.addTemplate();
                                                            }
                                                        });
                                                    }
							    				   	
							    			      }
							    			   else if(response.data.status=='error'){
							    				   $rootScope.stopLoader();
							    			   }
							    		});
										   
					        		}
					        	else{
					        	    isAddedTemplate = true;
					        		$scope.isTransactionFlag='Y'; // for hiding drop down list
					        		$scope.templateName=rediologyObj.title;
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
					        			$rootScope.startLoader();
					        			$timeout( function(){
					        				$scope.value = '';
	                                         $scope.value = rediologyObj.templateObject;
	                                         $rootScope.stopLoader();
					        				},1000);
					        			}
					        		
					        		//get saved Consumption Details by templateTransactionId
					        		if($scope.radioLogyReportObj.isResultEntry=='Y'){
					        			$scope.getSavedConsumptionDetails($scope.radioLogyReportObj.templateTransactionId );
					        		}	
//					        	  	$('#radiologyReport').modal('show');
			    				   	$rootScope.stopLoader();
					        	    }
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
					    	
					    	
					        //save Billable Consumption
					        $scope.remark = "";
					        $scope.nonBillableConsumptionId = null;
						      $scope.saveNewConsumption = function (flag,templateTransactionId) {
						             
						    	  if($scope.savedConsumptionItems!=undefined && $scope.savedConsumptionItems!=null && $scope.savedConsumptionItems.length>0){
						    		  $scope.nonBillableConsumptionId = $scope.savedConsumptionItems[0].id;
						    	  }
						                 var URI = CONSTANTS.INVENTORY.NURSING.NON_BILLABLE_CONSUMPTION.SAVE_API;
						                 var data = {
						                	"id" : 	 $scope.nonBillableConsumptionId,
						                     "storeId": cookieObject.storeId,
						                     "statusId": 1,
						                     "approvalStatusId": 1,
						                     "remark": $scope.remark,
						                     "requestFrom":"Radiology",
						                     "batch": [],
						                     "unitId":$scope.unitId,
						                     "organizationId":$scope.organizationId,
						                     "userId":$scope.userId,
						                     "storeId":$scope.storeId,
						                     "requestFromId":$scope.radioLogyReportObj.templateTransactionId
						                 };

						                 angular.forEach($scope.itemList, function (item, key) {
						                    
						                     var storeItem = {
						                             "batchId": item.selectedBatch.batchId,
						                             "uomTypeId": item.selectedUom.uomTypeId,
						                             "uomUnitId": item.selectedUom.uomUnitId,
						                             "consumedQuantity": parseInt(item.consumedQuantity),
						                             "statusId": 1
						                         }
						                     data.batch.push(storeItem);

						                 });
						                 $rootScope.startLoader();
						                 GenericService.serviceAction("POST", URI, data).then(
						                     function (response) {
						                         $rootScope.stopLoader();
						                        // $state.go('radiologyResultEntry');
						                         if(flag == 'Y') {
						                        	  $state.go("radiologyResultEntry",{searchObj :$stateParams.searchObj}); 
						                        }else if($stateParams.radiologyReportObject.isRecall == 'Y'){
						                        	 $state.go("radiologyWorklistForPrinting",{searchObj :$stateParams.searchObj}); 
						                        }
						                         $scope.itemList = [];
						                         if($scope.radioLogyReportObj.isResultEntry=='Y'){
						                        	 $scope.getSavedConsumptionDetails(templateTransactionId );
						                         }
						                     },
						                     function (err) {
						                         $rootScope.stopLoader();
						                       
						                     });
						             }
					        
					        /** Template List*/
					        $scope.updateReport = function(flag,skipFlag)
					        {
					        	if($scope.radiologyReport!=undefined && $scope.radiologyReport.templateFormatId==2){
                                    if($scope.value!=undefined && $scope.value!=null){
	                                    $scope.radiologyReport.templateObject = $scope.value;
	                                }
					        	}
					        	
					        	if(Object.keys($scope.radiologyReport).length === 0 || $scope.radiologyReport=="-"){
					       			growl.warning("Please select Word Template...!",{
										title : 'warning'
									});
					        		return;
					       		}
					        	if( $scope.radiologyReport.templateObject==undefined ||  $scope.radiologyReport.templateObject==null ||  $scope.radiologyReport.templateObject==''){
					       			growl.warning("Please add Template...!",{
										title : 'warning'
									});
					        		return;
					       		}

					       		if(isAddedTemplate==false){
                                	growl.warning("Please add Template...!",{
                                		title : 'warning'
                                	});
                                	return;
                                }
					        	
					        	//check Billable consumption is valid or not
                                if($scope.itemList!=null && $scope.itemList.length>0){
                                	
                                	var isValidBatch = $scope.itemList.find(obj =>{return (obj.selectedBatch == undefined || obj.selectedBatch.batchId == undefined )})
									 if(isValidBatch!=undefined){
										 $rootScope.stopLoader();
                                            growl.warning("Please Select Batch Of All Items",
                                                        {
                                                            title : 'warning'
                                                        });
                                                        return false;
									 } 
                                	
                                	var isValidBatch = $scope.itemList.find(obj =>{return (obj.selectedUom == undefined || obj.selectedUom.uomTypeId == undefined )})
									 if(isValidBatch!=undefined){
										 $rootScope.stopLoader();
                                            growl.warning("Please Select UOM Type Of All Items",
                                                        {
                                                            title : 'warning'
                                                        });
                                                        return false;
									 } 
                                	
                                	var isValidBatch = $scope.itemList.find(obj =>{return (obj.consumedQuantity == undefined || obj.consumedQuantity == 0 )})
									 if(isValidBatch!=undefined){
										 $rootScope.stopLoader();
                                            growl.warning("Please Enter Consumed Quantity",
                                                        {
                                                            title : 'warning'
                                                        });
                                                        return false;
									 } 
                                	
                                
                                	
								}
				        	    
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
									templateTransaction.patientName =$scope.radioLogyReportObj.patientName;
									templateTransaction.mobileNumber =$scope.radioLogyReportObj.mobileNumber;
									templateTransaction.patientMail =$scope.radioLogyReportObj.patientMail;
									templateTransaction.reportEntryBy = $scope.userId;
									if(skipFlag != 0){
										if(skipFlag == 1){
											templateTransaction.isFinalRelease='Y';
											templateTransaction.isProvisionalRelease='Y';
											$stateParams.radiologyReportObject.finalRelease = true;
											$stateParams.radiologyReportObject.templateContent = $scope.value;
                                            $stateParams.radiologyReportObject.isProvisionalRelease = 'Y';
                                            $stateParams.radiologyReportObject.isFinalRelease = 'Y';
                                            templateTransaction.reportvalidateBy = $scope.userId;
										}else{
											templateTransaction.isFinalRelease='N';
											templateTransaction.isProvisionalRelease='N';
                                            $stateParams.radiologyReportObject.templateContent = $scope.value;
                                            $stateParams.radiologyReportObject.isProvisionalRelease = 'N';
                                            $stateParams.radiologyReportObject.isFinalRelease = null;
                                            
										}
										
										
                                    }else if(skipFlag == 0){
                                    	templateTransaction.isProvisionalRelease = flag;
                                        $stateParams.radiologyReportObject.templateContent = $scope.value;
                                        $stateParams.radiologyReportObject.isProvisionalRelease = flag;
                                        $stateParams.radiologyReportObject.isFinalRelease = null;
                                    }
									templateTransaction.uhidNumber=$scope.radioLogyReportObj.uhidNumber;
									templateTransaction.orgId=$scope.organizationId;
									templateTransaction.visitTypeId=$scope.radioLogyReportObj.visitTypeId;
									templateTransaction.unitId=$scope.unitId;
									templateTransaction.referralName =  $scope.radioLogyReportObj.referralName;
									templateTransaction.ageString =   $scope.radioLogyReportObj.ageString;
									templateTransaction.orderDate =  $scope.radioLogyReportObj.orderDate;
									templateTransaction.genderCode =  $scope.radioLogyReportObj.genderCode;
									templateTransaction.isForMail = 1;
									//templateTransaction.isProvisionalRelease=flag;
									templateTransaction.templateDoctorId = $scope.templateDoctorId;
									if($scope.radiologyReport.templateFormatId==1)
									{	
									var jsonObject = angular.equals({},$scope.radiologyReport.templateObject.obj)?JSON.parse($scope.radiologyReport.templateObject.obj):{};
									var templateObj = JSON.parse($scope.radiologyReport.templateObject);
									templateObj.obj = $scope.obj;
									templateTransaction.jsonObject = JSON.stringify(jsonObject);
									templateTransaction.templateObject = JSON.stringify(templateObj);
									$stateParams.radiologyReportObject.templateContent = templateTransaction.templateObject;
									}
									else if($scope.radiologyReport.templateFormatId==2) {
										templateTransaction.templateObject = $scope.value;
										$stateParams.radiologyReportObject.templateContent = $scope.value;
									}
					        	    console.log('templateTransaction',JSON.stringify(templateTransaction));
						    		
					        	    
					        	    //check Billable consumption is valid or not
                                    if($scope.itemList!=null && $scope.itemList.length>0){
                                    	
                                    	var isValidBatch = $scope.itemList.find(obj =>{return (obj.selectedBatch == undefined || obj.selectedBatch.batchId == undefined )})
										 if(isValidBatch!=undefined){
											 $rootScope.stopLoader();
	                                            growl.warning("Please Select Batch Of All Items",
	                                                        {
	                                                            title : 'warning'
	                                                        });
	                                                        return false;
										 } 
                                    	
                                    	var isValidBatch = $scope.itemList.find(obj =>{return (obj.selectedUom == undefined || obj.selectedUom.uomTypeId == undefined )})
										 if(isValidBatch!=undefined){
											 $rootScope.stopLoader();
	                                            growl.warning("Please Select UOM Type Of All Items",
	                                                        {
	                                                            title : 'warning'
	                                                        });
	                                                        return false;
										 } 
                                    	
                                    	var isValidBatch = $scope.itemList.find(obj =>{return (obj.consumedQuantity == undefined || obj.consumedQuantity == 0 )})
										 if(isValidBatch!=undefined){
											 $rootScope.stopLoader();
	                                            growl.warning("Please Enter Consumed Quantity",
	                                                        {
	                                                            title : 'warning'
	                                                        });
	                                                        return false;
										 } 
                                    	
                                    
                                    	
									}
					        	    
					        	    
					        	    var URI = BASE_URL + ROOT_URL + "/global/template/transaction";
					        	    BillingGenericService.serviceAction(METHOD_PUT,URI,templateTransaction,NOTIFICATION_MSG_STATUS_TRUE).then(function(response) {
						    			if (response.data.status == "success")
						    			{
						    				$scope.isTransactionFlag='Y';
						    				$scope.templateName=$scope.radiologyReport.title;
						    				console.log("updated template transaction",response.data.object);
						    				$scope.radioLogyReportObj.templateTransactionId=response.data.object;
						    				if($scope.itemList!=null && $scope.itemList.length>0){
												$scope.saveNewConsumption(flag,$scope.radioLogyReportObj.templateTransactionId);
											}
						    				if(flag=='Y'){
						    					 $state.go("radiologyResultEntry",{searchObj :$stateParams.searchObj}); 
						    				}else if($stateParams.radiologyReportObject.isRecall == 'Y'){
						    					 $state.go("radiologyWorklistForPrinting",{searchObj :$stateParams.searchObj}); 
						    				}
						    				
						    				
						    				 $scope.printRadiologyReport($stateParams.radiologyReportObject)
										}/*else{
											  growl.warning(response.data.message,
	                                                  {
	                                                      title : 'warning'
	                                                  });
										}*/
										$rootScope.stopLoader();
						    		});
					        }
					        $scope.updateFinalReport=function(flag){

				        		$rootScope.startLoader();
                                if($scope.radiologyReport !=undefined && $scope.radiologyReport.templateFormatId==2){
                                    if($scope.value == undefined || $scope.value == null || $scope.value == ""){
                                        growl.warning("Please add Template...!",{
                                            title : 'warning'
                                        });
                                        $rootScope.stopLoader();
                                        return false;
                                    }
                                }
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
								templateTransaction.patientName =$scope.radioLogyReportObj.patientName;
								templateTransaction.mobileNumber =$scope.radioLogyReportObj.mobileNumber;
								templateTransaction.patientMail =$scope.radioLogyReportObj.patientMail;
								templateTransaction.isFinalRelease=flag;
								templateTransaction.uhidNumber=$scope.radioLogyReportObj.uhidNumber;
								templateTransaction.orgId=$scope.organizationId;
								templateTransaction.visitTypeId=$scope.radioLogyReportObj.visitTypeId;
								templateTransaction.unitId=$scope.unitId;
								templateTransaction.referralName =  $scope.radioLogyReportObj.referralName;
								templateTransaction.ageString =   $scope.radioLogyReportObj.ageString;
								templateTransaction.orderDate =  $scope.radioLogyReportObj.orderDate;
								templateTransaction.genderCode =  $scope.radioLogyReportObj.genderCode;
								templateTransaction.isForMail = 1;
								templateTransaction.templateDoctorId = $scope.templateDoctorId;
								templateTransaction.reportvalidateBy = $scope.userId;
								templateTransaction.isProvisionalRelease = 'Y';
								templateTransaction.isAmend = $scope.radioLogyReportObj.isAmend;
								templateTransaction.skipRISReportEntry = $scope.skipRISReportEntry;
								if(flag=='Y'){
									templateTransaction.isFinalRelease='Y';
									$stateParams.radiologyReportObject.finalRelease = true;
									$stateParams.radiologyReportObject.templateContent = $scope.value;
								}
							
								if($scope.radiologyReport.templateFormatId==1)
								{	
								var jsonObject = angular.equals({},$scope.radiologyReport.templateObject.obj)?JSON.parse($scope.radiologyReport.templateObject.obj):{};
								var templateObj = JSON.parse($scope.radiologyReport.templateObject);
								templateObj.obj = $scope.obj;
								templateTransaction.jsonObject = JSON.stringify(jsonObject);
								templateTransaction.templateObject = JSON.stringify(templateObj);
								$stateParams.radiologyReportObject.templateContent = templateTransaction.templateObject;
								}
								else if($scope.radiologyReport.templateFormatId==2) {
									templateTransaction.templateObject = $scope.value;
									$stateParams.radiologyReportObject.templateContent = $scope.value;
								}
				        	    console.log('templateTransaction',JSON.stringify(templateTransaction));
					    		
				        	    var URI = BASE_URL + ROOT_URL + "/global/template/transaction/RIS";
				        	    BillingGenericService.serviceAction(METHOD_PUT,URI,templateTransaction,NOTIFICATION_MSG_STATUS_TRUE).then(function(response) {
					    			if (response.data.status == "success")
					    			{
					    				$scope.isTransactionFlag='Y';
					    			
					    				console.log("updated template transaction",response.data.object);
					    				$scope.radioLogyReportObj.templateTransactionId=response.data.object;
					    				if(flag=='Y'){
					    					
                                            $stateParams.radiologyReportObject.isFinalRelease='Y';
                                            $stateParams.radiologyReportObject.isProvisionalRelease='N';
					    					$scope.printRadiologyReport($stateParams.radiologyReportObject)
					    					
					    					if($stateParams.radiologyReportObject.isRecall == 'Y'){
					    						 $state.go("radiologyWorklistForPrinting"); 
					    					}else{
					    						 $state.go("radiologyResultValidation",{searchObj :$stateParams.searchObj}); 
					    					}
					    				}
									}/*else{
										  growl.warning(response.data.message,
                                                  {
                                                      title : 'warning'
                                                  });
									}*/
									$rootScope.stopLoader();
					    		});
					        }
					        
					        $scope.printRadiologyReport=function(info){
					        	
					        	if(info.isHtmlPrint==true){
					        		
					        	info.organizationId = $scope.organizationId;
					        	info.unitId =  $scope.unitId;
					        	info.printWithHeader = 0; //with header
					        	info.unitName =  $scope.unitName;
					        	info.multidelete("docList");
					        	var URI = BASE_URL + ROOT_URL + "/api/opd/RIS/printReportTest";
					    		BillingGenericService.serviceAction(METHOD_POST, URI, info,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
					    				{
					    	
					    			   if(response.data.status=='success')
					    			      {
					    				   console.log("gg",response.data.object);
					    				   //'<iframe src="https://s3.ap-south-1.amazonaws.com/his-docs/apollohospitals1/patient/documents/clinical/episodic-documents/op-coversheet/1_1_null_Sign.png" style="margin-left:462px;" width="49%" height="15%"></iframe>'+ 
					    				   /*var divContents = response.data.object.latterHeadImg + response.data.object.header+response.data.object.templateContent+response.data.object.footer;  
								            var printWindow = window.open('', '', 'height=500,width=700');  
								            printWindow.document.write('<html><head>');  
								            printWindow.document.write('</head><body >');  
								            printWindow.document.write(divContents);  
								            printWindow.document.write('</body></html>');  
								            printWindow.document.close();  
								            printWindow.print();  */
					    				   
					    				   var headerSize = "";
					    					 var letterImgAndHeader="";
					    					 var letterImgAndHeaderPdf="";
					    					 var patientDetails = "";
					    					 var patientDetailsPdf = "";
					    					 var completeTemplateContent = "";
					    					 var completeTemplateContentPdf = "";
					    					 if($scope.isRisPatientDetailsOnFirstPage == 'N'){
					    						 //print patient details on all pages
					    						 headerSize = "220px;";
					    						 letterImgAndHeader = response.data.object.latterHeadImg+response.data.object.header;
					    						 letterImgAndHeaderPdf = response.data.object.letterHeadImagePdf+response.data.object.headerPdf;
					    						 completeTemplateContent = response.data.object.templateContent+response.data.object.footer;
					    						 completeTemplateContentPdf = response.data.object.templateContent+response.data.object.footer;


					    					 }else{
					    						 //print patient details on first page
					    						 headerSize = "105px;";
					    						 letterImgAndHeader = response.data.object.latterHeadImg;
					    						 letterImgAndHeaderPdf = response.data.object.letterHeadImagePdf;
					    						 if(info.printWithHeader == 0){
					    							 patientDetails = response.data.object.header;
					    							 patientDetailsPdf = response.data.object.headerPdf;
					    						 }else{
					    							 patientDetails = response.data.object.headerWithoutImg;
					    							 patientDetailsPdf = response.data.object.headerWithoutImg;
					    						 }
					    						 completeTemplateContent = patientDetails + response.data.object.templateContent+response.data.object.footer;
					    						 completeTemplateContentPdf = patientDetailsPdf + response.data.object.templateContent+response.data.object.footer;
					    					 }
					    				   

					    				   var printWindow = window.open('', '', 'height=500,width=700');
                                           var printWindowForPdf = null;
					    				   var printWindowForPdf = window.open('', '', 'height=500,width=700');

                                           printWindow.document.write('<html><head></head>');

					    				   printWindowForPdf.document.write('<!DOCTYPE html> <html lang="en"><head></head>');
					    				   printWindowForPdf.document.write('<meta charset="UTF-8">');
					    				   printWindowForPdf.document.write('<meta name="viewport" content="width=device-width, initial-scale=1.0">');

                                           printWindow.document.write(' <style> '
                                                   +'.page-header, .page-header-space {height: '+headerSize+'} '
                                                   +' .page-footer, .page-footer-space { height: 105px;} '
                                                   + ' .page-footer { position: fixed; bottom: 0; width: 100%;} '
                                                   + ' .page-header { position: fixed; top: 0mm; width: 100%;} '
                                                   + ' .page {page-break-after: auto;} '
                                                   + '  @page { margin: 20mm}'
                                                   + 'p { margin:0px 0px 0px 0px; }'
                                                   + ' </style>' );

                                           printWindowForPdf.document.write(' <style> '
                                                  +'.page-header, .page-header-space {height: '+headerSize+'} '
                                                  +' .page-footer, .page-footer-space { height: 105px;} '
                                                  + ' .page-footer { position: fixed; bottom: 0; width: 100%;} '
                                                  + ' .page-header { position: fixed; top: 0mm; width: 100%;} '
                                                  + ' .page {page-break-after: auto;} '
                                                  + '  @page { margin: 20mm}'
                                                  + 'p { margin:0px 0px 0px 0px; }'
                                                  + ' </style>' );

                                           printWindow.document.write('<body>');

                                           printWindowForPdf.document.write('<body>');

                                           if(info.printWithHeader == 0){
                                                 printWindow.document.write('<div class="page-header" style="text-align: center">');
                                                 printWindow.document.write(letterImgAndHeader);  
                                                 printWindow.document.write('</div>');

                                                 printWindowForPdf.document.write('<div class="page-header" style="text-align: center">');
                                                 printWindowForPdf.document.write(letterImgAndHeaderPdf);
                                                 printWindowForPdf.document.write('</div>');

                                           }else if($scope.isRisPatientDetailsOnFirstPage == 'N'){
                                        	 //skip this part during first page bcoz we append patient details allready
                                               printWindow.document.write('<div class="page-header" style="text-align: center">');
                                               printWindow.document.write(response.data.object.headerWithoutImg);  
                                               printWindow.document.write('</div>');

                                               printWindowForPdf.document.write('<div class="page-header" style="text-align: center">');
                                               printWindowForPdf.document.write(response.data.object.headerWithoutImg);
                                               printWindowForPdf.document.write('</div>');
                                           }

                                           printWindow.document.write('<table> <thead> <tr><td><div class="page-header-space"></div> </td></tr> </thead>');
                                           printWindow.document.write(' <tbody><tr><td><div class="page" style="line-height: 1.2;">');
                                           printWindow.document.write(completeTemplateContent);
                                           printWindow.document.write('</div> </td></tr></tbody>');
                                           printWindow.document.write(' <tfoot> <tr><td><div class="page-footer-space"></div></td></tr></tfoot></table>');

                                           printWindowForPdf.document.write('<table>');
                                           printWindowForPdf.document.write(' <tbody><tr><td><div class="page" style="line-height: 1.2;">');
                                           printWindowForPdf.document.write(completeTemplateContentPdf);
                                           printWindowForPdf.document.write('</div> </td></tr></tbody>');
                                           printWindowForPdf.document.write(' <tfoot> <tr><td><div class="page-footer-space"></div></td></tr></tfoot></table>');

                                           if(info.printWithHeader == 0){
                                               printWindow.document.write('<div class="page-footer" style="text-align: center">');
                                               printWindow.document.write(response.data.object.footerImg);
                                               printWindow.document.write('</div>');

                                               printWindowForPdf.document.write('<div class="page-footer" style="text-align: center">');
                                               printWindowForPdf.document.write(response.data.object.footerImg);
                                               printWindowForPdf.document.write('</div>');
                                           }

                                           printWindow.document.write('</body>');
                                           printWindow.document.write('</html>');

                                           printWindowForPdf.document.write('</body>');
                                           printWindowForPdf.document.write('</html>');

                                           var htmlContent = printWindowForPdf.document.documentElement.outerHTML;

                                           printWindow.document.close();  
                                           printWindowForPdf.document.close();

                                           $timeout( function(){
                                                printWindow.print();
                                           },2000);
								            
                                           if(info.isFinalRelease == 'Y'){
                                        	   $scope.sendReport(info ,htmlContent);
//                                        	   $scope.generatePdf(info ,response);
                                           }
                                           
                                           
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
				        		"paramValue": 0
				        		}
				        		]
				        		};
				        		reportService.generate(REPORT_API_URI1,data);
				        		
				        		 if(info.isFinalRelease == 'Y'){
                              	   $scope.sendReport(info ,null);  
                                 }

				        		}
//					        	if(info.isFinalRelease == 'Y'){
//	                              	   $scope.sendReport(info ,null);
//	                                 }
					        }

					        
					        				$scope.sendReport = function(info,htmlContent){
					        					
												 $scope.newList = [];
												 $rootScope.startLoader();
												
												 var newData = {};
													newData.patientId = info.patientId;
													newData.testDesc = info.serviceName;
										 			newData.orgId =  $scope.orgId;
										 			newData.organizationId =  $scope.organizationId;
										 			newData.unitId =  $scope.unitId;
										 			newData.doctorId =  info.doctorId;
										 			newData.createdBy =  $scope.userId;
										 			newData.templateTransactionId = info.templateTransactionId;
										 			newData.htmlContent = htmlContent;
										 			newData.encounterId = $stateParams.radiologyReportObject.encounterId;
                                                    newData.payeeId = $stateParams.radiologyReportObject.payeeId;
                                                    newData.paymentEntitlementId = $stateParams.radiologyReportObject.paymentEntitlementId;
                                                    newData.orderId = $stateParams.radiologyReportObject.orderId;
                                                    newData.isFinalRelease = 'Y';
				 			                    $scope.newList.push(newData);
								
							 
							 var URI = BASE_URL + ROOT_URL + "/api/opd/RIS/sendRadiologyReportOnMail";
								GenericService.serviceAction("POST",URI, $scope.newList).then(function(response){
									if (response.data.status == 'success')
									{
										 $scope.sendMailButton = false;
									/*	if(response.data.message!=null){
											growl.warning(response.data.message,
													{
														title : 'warning!'
													});
										}else{
											growl.success(response.data.status,
													{
														title : 'Success!'
													});
										}*/
										 
									} 
									$rootScope.stopLoader();
						
								});
						 }
					        

					        $scope.printReportOLD=function()
					    	{
					    		

		    					$scope.radioLogyReportObj.unitId = $scope.unitId;
		    					$scope.radioLogyReportObj.organizationId = $scope.organizationId;
		    					$scope.radioLogyReportObj.templateContent = $scope.value;
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
							     hiddenField1.setAttribute("value", JSON.stringify($scope.radioLogyReportObj));
							   /*  hiddenField1.setAttribute("type", "hidden");
							     hiddenField1.setAttribute("name", "templateContent");
							     hiddenField1.setAttribute("value",info.templateContent);
*/							     form.appendChild(hiddenField1);
							     
							     
							     
							     document.body.appendChild(form);
							     $("form[name='Radiology Report']").submit();
					    	}
					        
					        /******* Add New Item START ******************/
						      
						      $scope.assetCategoryObj = {
										id:"",
										category:""
								}
								
								$scope.productCategoryObj = {
										id:"",
										category:""
								}
								
								$scope.assetCategories = [];
								$scope.getAssetCategoryList = function () {
									var URI = BASE_URL + ROOT_URL + "/api/global/assets/categories/1/"+$scope.unitId+"/"+$scope.organizationId; //1 for consumable asset type
									GenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
										$scope.assetCategories = response.data;
										console.log("$scope.assetCategories",$scope.assetCategories);
										
									});
								   }
								
								$scope.getAssetCategoryList();
								
								
								$scope.productCategories = {};
								$scope.getProductCategoryList = function (id) {
									var URI = BASE_URL + ROOT_URL + "/api/global/products/categories/"+id+"/"+$scope.organizationId;
									GenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
										$scope.productCategories = response.data;
										console.log("$scope.productCategories",$scope.productCategories);
										
									});
								}
								
								$scope.itemObj = {
										itemCode:"",
										itemName:""
								}
								$scope.searchNewItemList = [];
								$scope.searchNewItem = function () {
									var data = {
											 	"assetTypeId": 1,
										        "assetCategoryId": $scope.assetCategoryObj.id,
										        "productCategoryId": $scope.productCategoryObj.id,
										        "itemCode": $scope.itemObj.itemCode,
										        "itemName": $scope.itemObj.itemName
											};
									$rootScope.startLoaderPopup();
									var URI = BASE_URL + ROOT_URL + "/api/global/items/getItemList/onTemplet";
									GenericService.serviceAction("POST", URI, data).then(function(response) {
										$rootScope.stopLoaderPopup();
										$scope.searchNewItemList = response.data;
										console.log("$scope.searchNewItemList",$scope.searchNewItemList);
									});
							}
								
								$scope.isItemChecked=true;
									$scope.isItemExist=false;
									$scope.itemAdded = false;
									$scope.addItems = function(){
										$rootScope.startLoaderPopup();
										if($scope.searchNewItemList!=null  && $scope.searchNewItemList!=undefined && $scope.searchNewItemList.length>0){
											 var found = $scope.searchNewItemList.find(ar=>ar.isSelected);
		                                       if(found==undefined)
		                                           {
		                                    	   growl.warning("Please Select Item ",
															{
																title : 'warning!'
															});
		                                    	   $rootScope.stopLoaderPopup();
												   return false;
		                                           }
											}else{
												 growl.warning("Please Select Item ",
															{
																title : 'warning!'
															});
		                                    	   $rootScope.stopLoaderPopup();
												   return false;
											}
										
										$scope.isItemExist=false;
										$scope.isItemCheck=false;
										$scope.itemAdded = false;
										$scope.itemIds = [];
										
									    angular.forEach($scope.searchNewItemList,function(value,index) {
											if(value.isSelected){

												if($scope.itemList.length>0){
													angular.forEach($scope.itemList,function(value1,index1) {
														if(value1.itemId == value.itemId ){
															$scope.isItemCheck = true;
														}
													});

												}
												}
											});
										if($scope.isItemCheck){
										growl.warning("Selected Item Already Exist",
												{
													title : 'warning!'
												});
										$rootScope.stopLoaderPopup();
										return false;
									}
										
										angular.forEach($scope.searchNewItemList,function(value,index) {
											if(value.isSelected){
												
												if($scope.itemList.length>0){
													$scope.isItemExist=false;
													angular.forEach($scope.itemList,function(value1,index1) {
														if(value1.itemId == value.itemId ){
															$scope.isItemExist = true;
														}
													});
													
													if (!$scope.isItemExist){
														var newArr = {};
														newArr.itemId = value.itemId;
														newArr.selectedBatch = undefined;
														newArr.batchList = [];
														newArr.selectedUom = undefined;
														newArr.itemName = value.itemName;
														newArr.itemCode = value.itemCode;
														newArr.availableQuantity = 0;
														newArr.assignQty = 0;
														newArr.newAssignQty = 0;
														newArr.consumedQuantity = 0;
														$scope.itemList.push(newArr);
														
														var arr = {};
														arr.itemId =  value.itemId;
														$scope.itemIds.push(arr);
													}
												}else{
														var newArr = {};
															newArr.itemId = value.itemId;
															newArr.selectedBatch = undefined;
															newArr.batchList = [];
															newArr.selectedUom = undefined;
															newArr.itemName = value.itemName;
															newArr.itemCode = value.itemCode;
															newArr.availableQuantity = 0;
															newArr.assignQty = 0;
															newArr.newAssignQty = 0;
															newArr.consumedQuantity = 0;
															$scope.itemList.push(newArr);
															
															var arr = {};
															arr.itemId =  value.itemId;
															$scope.itemIds.push(arr);
												}
											}
										});
										
										if($scope.itemIds.length>0){
											var data = {
												 	"itemIds": $scope.itemIds,
												 	"unitId" :	$scope.unitId ,
													"organizationId" : $scope.organizationId ,
													"storeId" : $scope.storeId
												};
											$scope.startLoader();
										var URI = BASE_URL + ROOT_URL + "/billing/resultEntry/getBatchDetailsByItemList";
										GenericService.serviceAction("POST", URI, data).then(function(response) {
											$scope.stopLoader();
											if(response.data.listObject!=null && response.data.listObject.length>0){
												angular.forEach(response.data.listObject,function(value,index) {
													angular.forEach($scope.itemList,function(value1,index1) {
													if(value1.itemId == value.itemId ){
														if(value.batchReq == true){
															value1.batchList = value.batchList;	
															value1.batchReq = value.batchReq;
														}else{
															value1.batchReq = value.batchReq;
															value1.selectedBatch = {};
															value1.selectedBatch.batchId = value.batchList[0].batchId;
															value1.selectedUom = {};
															value1.selectedUom.uomTypeId = value.batchList[0].uom[2].uomTypeId;
															value1.selectedUom.uomUnitId = value.batchList[0].uom[2].uomUnitId;
															value1.availableQuantity = value.batchList[0].availableStockQuantity;
														}
														
														
													}
													});
												});
											}
											 $rootScope.stopLoaderPopup();
											 var popup = angular.element('#addNewItem');
										      popup.modal('hide');
										});
										}
										
										$scope.searchNewItemList = [];
										$scope.assetCategoryObj="";
										$scope.productCategoryObj= "";
										 $scope.selectAllRows = false;
										$scope.stopLoader();
									}
									
									$scope.removeItems = function(indx){
										
										if($scope.selectedItemList[indx].procedureConfigInventoryItemId!=null){
											$scope.selectedItemList[indx].status="I";
											}else{
											$scope.selectedItemList.splice(indx,1);
										}
									}
									
									
									$scope.ConvertQtyOrdered = function (item, uom, uomList) {
							            var newUOM = [];
							            uom.find(function (value) {
							                if (!!value.uomTypeId) {
							                    newUOM.push(value);
							                }
							            });
							            var assignQty = item.assignQty;
							           // var consumedQuantity= item.consumedQuantity!=undefined ?item.consumedQuantity : item.assignQty;
							            var uomDetails = uomList.uomDetails.split('-')[0];
							            if (uomDetails == "P") {
							                item.newAssignQty = Math.ceil(assignQty / (newUOM[0].conversion * newUOM[1].conversion * newUOM[2].conversion));
							               // item.consumedQuantity = Math.ceil(consumedQuantity / (newUOM[0].conversion * newUOM[1].conversion * newUOM[2].conversion));
							                item.availableQuantity = Math.ceil(item.selectedBatch.availableStockQuantity / (newUOM[0].conversion * newUOM[1].conversion * newUOM[2].conversion));
							            } else if (uomDetails == "S") {
							                item.newAssignQty = Math.ceil(assignQty / (newUOM[1].conversion * newUOM[2].conversion));
							               // item.consumedQuantity = Math.ceil(consumedQuantity / ( newUOM[1].conversion * newUOM[2].conversion));
							                item.availableQuantity = Math.ceil(item.selectedBatch.availableStockQuantity / (newUOM[1].conversion * newUOM[2].conversion));
							            } else if (uomDetails == "D") {
							                item.newAssignQty = Math.ceil(assignQty / (newUOM[2].conversion));
							               // item.consumedQuantity = Math.ceil(consumedQuantity / ( newUOM[2].conversion));
							                item.availableQuantity = Math.ceil(item.selectedBatch.availableStockQuantity / (newUOM[2].conversion));
							            }
							            
							            item.consumedQuantity = item.newAssignQty;
							        }

									$scope.batchExpiryMsgShow = function(item){
										var date = moment(new Date()).format("MM");
										var year = moment(new Date()).format("YYYY");
										var dateCal = parseInt(date) + 1;
										var expDate = moment(item.selectedBatch.expiry).format("MM");
										var expYear = moment(item.selectedBatch.expiry).format("YYYY");
										if((dateCal == expDate || date == expDate) && year == expYear){
											growl.warning("Batch No. " + item.selectedBatch.batchNo + " is near expiry");
										}  
									}
								
						        $scope.batchChange = function (index, batch) {
						            var item = null;
						            $scope.itemList[index].uom = batch.uom;
						            item =  $scope.itemList[index];
						            if (!!batch.batchId) {
						                //Select Default UOM
						                item.selectedUom = $filter('filter')(item.uom, {
						                    opUom: true
						                }, true)[0];

						                //"Select UOM" option from UOM dropdown
						                if (!item.selectedUom) {
						                    item.selectedUom = item.uom[0];
						                }
						               
						                item.availableStockQuantity = item.selectedBatch.availableStockQuantity;
						                item.expiry = item.selectedBatch.expiry;
						                //Convert Ordered Quantity
						                $scope.ConvertQtyOrdered(item, item.selectedBatch.uom, item.selectedUom);

						            } else{
						                item.selectedUom = item.uom[0];
						                item.consumedQuantity = null;
						            }
						        }
						        
						       $scope.uomChange = function (index, uom ) {
						            var item = null;
						                item = $scope.itemList[index];
						            if (!!uom) {
						                //Convert Ordered Quantity
						            	$scope.ConvertQtyOrdered(item, item.selectedBatch.uom, item.selectedUom);
						            } 
						        }
						       
						       var ValidateUniqueBatchAndUom = function (item) {
				
						            var isValid = true;
						                item['isParentBatchValid'] = false;
						                item['isParentUOMValid'] = false;
						                var items = $scope.itemList.filter(function (detail) {
						                		
						                    return detail.itemId == item.itemId && item.selectedBatch && item.selectedUom
						                    && item.selectedUom.uomTypeId > 0 && item.selectedBatch.batchId > 0 && detail.selectedBatch.batchId
						                    == item.selectedBatch.batchId && detail.selectedUom.uomTypeId == item.selectedUom.uomTypeId;
						                });
						                if (items.length > 1) {
						                    isValid = false;
						                    item['isParentBatchValid'] = true;
						                    item['isParentUOMValid'] = true;
						                    growl.error('There should not be same batch and same UOM for same item');
						                }
						            return isValid;
						        }
						       
						      $scope.validateBatch = function (item) {
						            var isValid = true;
						            if (!item.selectedBatch.batchId) {
						                item['isBatchValid'] = true;
						                growl.error("Please Select Batch");
						                isValid = false;
						            } else {
						                ValidateUniqueBatchAndUom(item)
						            }
						            return isValid;

						        }

						       $scope.validateUOM = function (item) {
						            var isValid = true;
						            if (!item.selectedUom) {
						                item['isUOMValid'] = true;
						                growl.error("Please Select UOM");
						                isValid = false;
						            } else {
						                ValidateUniqueBatchAndUom(item)
						            }
						            return isValid;
						        }
						       
						      $scope.removeItem = function (index) {
						    	  $scope.itemList.splice(index, 1);
						        }
						      
						      $scope.validateQuantity = function(index){
						    	  
						    	  if( $scope.itemList[index].availableQuantity< $scope.itemList[index].consumedQuantity){
						    		  growl.warning("Not Enough Quantity",
												{
													title : 'warning!'
												});
						    		  $scope.itemList[index].consumedQuantity = 0;
						    		  return false;
						    	   }
						      }
						      
						      /******* Add New Item START ******************/
								
								/*********************Add New Item END *********************/

					        
									$scope.setFirstTabLevel= function(newTab)
									{ 
										$scope.firstTabLevel = newTab;
									};
									
									$scope.isSetFirstTabLevel = function(tabNum)
									{
										return $scope.firstTabLevel === tabNum;
									};
					       	
									$scope.selectAllRows = false;
									   $scope.closeItemPopup = function(){
										   $scope.selectAllRows = false;
										   var popup = angular.element('#addNewItem');
										      popup.modal('hide');
									   }
									   $scope.checkSelectedAll = function(){
										   var count = 0;
										   angular.forEach($scope.searchNewItemList,function(value,index) {
								        		  if(value.isSelected){
								        			  count = count +1;
								        		  }
								            });
										   if(count == $scope.searchNewItemList.length){
											   $scope.selectAllRows = true;
										   }else{
											   $scope.selectAllRows = false;
										   }
									   }
									   
									   $scope.goToResultEntry = function(){
										   $state.go("radiologyResultEntry", {
											   searchObj: $stateParams.searchObj
	                                        });
										   
									   }
									   
									   $scope.goToResultValidation = function(){
										   if($stateParams.radiologyReportObject.isRecall == 'Y'){
					    						 $state.go("radiologyWorklistForPrinting"); 
					    					}else{
					    						 $state.go("radiologyResultValidation", {
													   searchObj: $stateParams.searchObj
			                                        });
					    					}
										  
									   }
						} ]);