'use strict';

/**
 * @Author By
 * @name myApp.controller:microbiologyReportEntryDetailsController
 * @description #microbiologyReportEntryDetailsController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'microbiologyReportEntryDetailsController',
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
							 
							 var labConfig  =    $scope.checkLabConfig();
								$scope.deptId = labConfig.labSpecialityId;
								   $scope.subDeptId = labConfig.microbiologyId;
								   $scope.printLabReportBeforRelease = labConfig.printLabReportBeforRelease;
/*
							 if(cookieObjectAppConfig.radiologySpecialityId!=null || cookieObjectAppConfig.radiologySpecialityId !="" || cookieObjectAppConfig.radiologySpecialityId!=undefined)
								{
									$scope.radiologySpecialityId = cookieObjectAppConfig.radiologySpecialityId;
								}else{
									growl.warning("Radiology department is not configured...!!",{
										title : 'Warning!'
									});
									return false;
								}*/
							 
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
								$scope.userId = cookieObject.userId;
								$rootScope.loginpage = true;
								$scope.saveBtnFlag = false;
								$scope.updateBtnFlag = true;
								$scope.storeId = cookieObject.storeId;

								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup
							}

							$scope.init();
							$scope.showButton  = true;
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
							$scope.selectTemplate  = function(microbiologyReport)
					    	{
					       		if(microbiologyReport!=null && microbiologyReport!=undefined){
					       			$scope.microbiologyReport = microbiologyReport;
					       		}
					    		
					    	}
					       	$scope.addTemplate = function()					       
					    	{
					       		if(Object.keys($scope.microbiologyReport).length === 0  || $scope.microbiologyReport=="-"){
					       			growl.warning("Please select Word Template...!",{
										title : ERROR_MSG
									});
					        		return;
					       		}
					       		if($scope.microbiologyReport!=null && $scope.microbiologyReport!=undefined && $scope.microbiologyReport!="-")
					       			{
					       			
					       			var obj = {
		    				   				patientId : $scope.patientDetails.patientId,
		    				   				encounterId :$scope.patientDetails.encounterId,
		    				   				admissionId : $scope.patientDetails.admissionId,
		    				   				orderDetailsId : $scope.patientDetails.orderDetailsId,
		    				   				templateDesc : $scope.microbiologyReport.templateObject
		    				   		}
								
									
						        	$rootScope.startLoader();
					        	    var URI = BASE_URL + ROOT_URL + "/api/opd/appendCensentData";
					        	    GenericService.serviceAction(METHOD_POST,URI,obj).then(function(response) {
						    			if (response.data.status == "success")
						    			{
						    				$scope.microbiologyReport.templateObject = response.data.object.templateDesc;
						    				if($stateParams.microbiologyReportObject.isHtmlPrint == undefined || $stateParams.microbiologyReportObject.isHtmlPrint == null){
						    					$stateParams.microbiologyReportObject.isHtmlPrint = $scope.microbiologyReport.isHtmlPrint;
						    				}
						    				$scope.microbiologyReport= angular.fromJson($scope.microbiologyReport);
							       			$scope.microbiologyReportObj.templateFormatId = 	$scope.microbiologyReport.templateFormatId;
					    				   	if($scope.microbiologyReport.templateFormatId==1)
					    				   		{
					    				  
					    				   		growl.warning("Please select Word Template...!",{
													title : ERROR_MSG
												});
								        		return;
					    				   		}
					    				    else if($scope.microbiologyReport.templateFormatId==2)
					    			    	  {
					    				    	$scope.showButton  = false;
					    				    	 $timeout( function(){
						    				    		$scope.value = '';
						    				    		 $scope.value =   $scope.microbiologyReport.templateObject;
						    				    		},1000);
					    			    	  }
						    			}
						    			$rootScope.stopLoader();
					        	    });
					        	    }
					       		else{
					       			$rootScope.stopLoader();
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
							
							
							if (angular.equals({},
									$stateParams.microbiologyReportObject)) {
								$state.go('microbiologyReportEntry');
								return;
							} else {
								console.log('DJ$stateParams.microbiologyReportObject',
										$stateParams.microbiologyReportObject);
							
									if($stateParams.microbiologyReportObject.isResultEntry=='Y'){
										 $(".selectedPageName").text("Microbiology Report Entry");
										 $state.current.title=" Report Entry Details";
									}else{
										 $(".selectedPageName").text("Microbiology Report Validation");
										 $state.current.title=" Report Validation Details";
									}
								
								//for patient demography details start 
								$scope.patientDetails.uhIdNumber=$stateParams.microbiologyReportObject.uhid;
								$scope.patientDetails.patientName=$stateParams.microbiologyReportObject.prefixCode+" "+$stateParams.microbiologyReportObject.patientName;
								$scope.patientDetails.genderCode=$stateParams.microbiologyReportObject.genderCode;
								$scope.patientDetails.age=$stateParams.microbiologyReportObject.ageString;
								$scope.patientDetails.birthDate=$stateParams.microbiologyReportObject.dob;
								$scope.patientDetails.visitTypeId=$stateParams.microbiologyReportObject.visitTypeId;
								$scope.patientDetails.dateOfAdmission=$stateParams.microbiologyReportObject.doa;
								$scope.patientDetails.patientDischargeDate=$stateParams.microbiologyReportObject.pdd;
								$scope.patientDetails.bedNumber=$stateParams.microbiologyReportObject.bedNumber;
								$scope.patientDetails.patientId=$stateParams.microbiologyReportObject.patientId;
								$scope.patientDetails.admissionId=$stateParams.microbiologyReportObject.admissionId;
								$scope.patientDetails.encounterId=$stateParams.microbiologyReportObject.encounterId;
								$scope.patientDetails.orderDetailsId=$stateParams.microbiologyReportObject.orderDetailsId;
								if($scope.patientDetails.bedNumber=='0'){
									$scope.patientDetails.bedNumber='';
								}
								
								var dateDoa = moment($stateParams.microbiologyReportObject.doa, 'DD/MM/YYYY');
								var datePdd = moment($stateParams.microbiologyReportObject.pdd, 'DD/MM/YYYY');
								
								$scope.patientDetails.los = $rootScope.dateDiffrence(dateDoa,datePdd);
								
								$scope.patientDetails.doctorFullName=$stateParams.microbiologyReportObject.doctorDetails;
								if($scope.patientDetails.doctorFullName != null){
								if($scope.patientDetails.visitTypeId!=1){
									$scope.patientDetails.doctorFullName=$scope.patientDetails.doctorFullName.toString().replace("Dr.","");
								}
								}
								//for patient demography details end 
								$scope.microbiologyReportObj = $stateParams.microbiologyReportObject;
								var microbiologyObj=$stateParams.microbiologyReportObject;
								if($stateParams.microbiologyReportObject.templateTypeId==null){
									microbiologyObj.templateTransactionId=null;
								}
								
								//billable consumption get inventory item list
								$scope.serviceIds = []; 
								if($stateParams.microbiologyReportObject.serviceId!=null && $stateParams.microbiologyReportObject.reportType == 1){
									$scope.serviceIds.push($stateParams.microbiologyReportObject.serviceId);
									$scope.getInventoryItemList($scope.serviceIds);
									$scope.getStoreList();
								}
								
					        	$rootScope.startLoader();
					        	if(microbiologyObj.templateTransactionId==null)
					        		{
					        		$scope.isTransactionFlag='N'; // for showing drop down list
					        		var data={
							        		  orgId : $scope.organizationId,
							        		  unitId : $scope.unitId,
							        		  doctorId : 0,
							        		  specialityId : 0,
							        		  templateTypeId : 9,
							        		  orderDetailsId : '',
							        		  patientId : '',
							        		  visitTypeId: '',
							        		  encounterId: '',
							        		  serviceId : microbiologyObj.serviceId	  
							        		}
							        	console.log('data',JSON.stringify(data));
							        	var URI = BASE_URL + ROOT_URL +'/api/global/templates/byService';
							    		BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
							    				{
							    	
							    			   if(response.data.status=='success')
							    			      {
							    				 	$scope.templateListMicrobiology = response.data.listObject;
							    				 	$scope.microbiologyReport = {};
//							    					$('#addtemplateModalRadio').modal('show');
							    				   	$rootScope.stopLoader();
							    				   	
							    				   	if(response.data.listObject.length==1)
							    				   		{
							    				   			$scope.info11 = response.data.listObject[0];
							    				   			$scope.selectTemplate($scope.info11);
							    				   			$scope.addTemplate();
							    				   		}
							    				   	
							    			      }
							    			   else if(response.data.status=='error'){
							    				   $rootScope.stopLoader();
							    			   }
							    		});
										   
					        		}
					        	else{
					        		$scope.isTransactionFlag='Y'; // for hiding drop down list
					        		$scope.templateName=rediologyObj.title;
					        		$scope.microbiologyReport = {};	
					        		$scope.microbiologyReport.templateObject  = microbiologyObj.templateObject;
					        		$scope.microbiologyReportObj.templateTransactionId =  microbiologyObj.templateTransactionId;
					        		$scope.microbiologyReport.templateFormatId  =microbiologyObj.templateFormatId;
					        		$scope.microbiologyReport.templateId = '';
					        		$scope.microbiologyReport.title= '';
					        		
					        		if(microbiologyObj.templateFormatId==1)
					        			{
					        			document.getElementById("htmlContentReport").innerHTML = JSON.parse(microbiologyObj.templateObject).html;
						        		$scope.obj =JSON.parse(microbiologyObj.templateObject).obj;
							    		var element = angular.element(document.getElementById("htmlContentReport"));
										   $compile(element)($scope);
					        			}
					        		else if(microbiologyObj.templateFormatId==2)
					        			{
					        			$timeout( function(){
			    				    		$scope.value = '';
			    				    		 $scope.value =   microbiologyObj.templateObject;
			    				    		},1000);
					        			}
			    				   	$rootScope.stopLoader();
					        	    }
							}

							
							/****Template Save*****/
							
							$scope.templateTransaction = {
									  microbiologyTemplateId : null,
				        			  templateId : "",
				        			  title : "",
				        			  templateObject : "",
				        			  status : 'A',
				        			  organizationId : $scope.organizationId,
				        			  unitId : $scope.unitId,
				        			  createdBy : $scope.userId,
				        			  createdDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
				        			  updatedBy : $scope.userId,
				        			  updatedDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
				        			  labSampleDtlsId :"",
				        			  isProvisionalRelease : "",
				        			  isFinalRelease :"",
				        }
					    	
					    	
					        //save Billable Consumption
					        $scope.remark = "";
						      $scope.saveNewConsumption = function () {
						             
						                 var URI = CONSTANTS.INVENTORY.NURSING.NON_BILLABLE_CONSUMPTION.SAVE_API;
						                 var data = {
						                     "storeId": cookieObject.storeId,
						                     "statusId": 1,
						                     "approvalStatusId": 1,
						                     "remark": $scope.remark,
						                     "requestFrom":"Lab",
						                     "batch": [],
						                     "unitId":$scope.unitId,
						                     "organizationId":$scope.organizationId,
						                     "userId":$scope.userId,
						                     "storeId":$scope.storeId
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
						                         $state.go('microbiologyReportEntry');
						                         $scope.itemList = [];
						                     },
						                     function (err) {
						                         $rootScope.stopLoader();
						                       
						                     });
						             }
					        
						      $scope.getSaveData = function(labsampleDetailsId){
						    	  var data = {};
						    	  data.labSampleDtlsId = labsampleDetailsId;
									 var URI =  BASE_URL+ROOT_URL+"/LIS/Microbiology/Template/getTemplate";
							            $rootScope.startLoader();
							            GenericService.serviceAction("POST", URI, data).then(
							                function (response) {
							                	$scope.showButton  = false;
							                	$scope.checkSave = false;
							                	if(response.data.object!=null){
							                	     $scope.templateTransaction.microbiologyTemplateId = response.data.object.microbiologyTemplateId;
								                     $scope.templateTransaction.templateObject = response.data.object.templateObject;
								                     $scope.templateTransaction.templateId = response.data.object.templateId;
								                     $scope.templateTransaction.title = response.data.object.title;
								                     $scope.templateTransaction.isProvisionalRelease = response.data.object.isProvisionalRelease;
								                     $scope.templateTransaction.isFinalRelease = response.data.object.isFinalRelease;
								                     $scope.info11 = {};
								                     var newArr = {};
								                     	newArr.specialityId= $scope.deptId ;
								                     	newArr.status= "A";
								                    	newArr.subSpecialityId= $scope.subDeptId ;
								                    	newArr.templateFormatId= response.data.object.templateFormatId;
								                    	newArr.templateId= response.data.object.templateId;
								                    	newArr.templateObject= response.data.object.templateObject;
								                    	newArr.templateTypeId= response.data.object.templateTypeId;
								                    	newArr.title= response.data.object.title;
								                    	$scope.info11 = newArr;
								                     $scope.selectTemplate($scope.info11);
						    				   		 $scope.addTemplate();
							                	}
							                    $rootScope.stopLoader();
							                });
								}
						  	$timeout(function () {
						      $scope.getSaveData($stateParams.microbiologyReportObject.labSampleDtlsId);
						  	}, 3000);
						      
						  	$scope.checkSave = true;
					        /** Template List*/
					        $scope.updateReport = function(flag,isReleaseFlag)
					        {
					        	$scope.showButton = true;
					        	if(	$scope.checkSave == true){
					        		return false;
					        	}
					        	$scope.checkSave = false;
					        	
					        	if($scope.value!=undefined && $scope.value!=null && $scope.value!="" ){
					        		$scope.microbiologyReport.templateObject = $scope.value;
					        	}
					        	if(Object.keys($scope.microbiologyReport).length === 0 || $scope.microbiologyReport=="-"){
					       			growl.warning("Please select Word Template...!",{
										title : 'warning'
									});
					       			$scope.checkSave = false;
					        		return;
					       		}
					        	if( $scope.microbiologyReport.templateObject==undefined ||  $scope.microbiologyReport.templateObject==null ||  $scope.microbiologyReport.templateObject==''){
					       			growl.warning("Please add Template...!",{
										title : 'warning'
									});
					       			$scope.checkSave = false;
					        		return;
					       		}
					        		$rootScope.startLoader();
					        		
									var templateTransaction = angular.copy($scope.templateTransaction);
									templateTransaction.templateId = $scope.microbiologyReport.templateId;
									templateTransaction.title= $scope.microbiologyReport.title;
									templateTransaction.templateObject = $scope.microbiologyReport.templateObject;
									
									if(isReleaseFlag == 0 ){

										if(flag == 'N'){
											templateTransaction.isProvisionalRelease = null;
											templateTransaction.isFinalRelease = null;
										}else{
											templateTransaction.isProvisionalRelease = flag;
											templateTransaction.isFinalRelease = null;
										}

									}else{
										if(flag == 'N'){
											templateTransaction.isProvisionalRelease = 'Y';
											templateTransaction.isFinalRelease = null;
										}else{
											templateTransaction.isProvisionalRelease = null;
											templateTransaction.isFinalRelease = flag;
										}
									}
									
									
									
									
									templateTransaction.reportTypeId = $stateParams.microbiologyReportObject.reportType;
									templateTransaction.labSampleDtlsId = $stateParams.microbiologyReportObject.labSampleDtlsId;
									
									if($scope.microbiologyReport.templateFormatId==1)
									{	
									var jsonObject = angular.equals({},$scope.microbiologyReport.templateObject.obj)?JSON.parse($scope.microbiologyReport.templateObject.obj):{};
									var templateObj = JSON.parse($scope.microbiologyReport.templateObject);
									templateObj.obj = $scope.obj;
									templateTransaction.jsonObject = JSON.stringify(jsonObject);
									templateTransaction.templateObject = JSON.stringify(templateObj);
									}
									else if($scope.microbiologyReport.templateFormatId==2) {
										templateTransaction.templateObject = $scope.value;
									}
					        	    console.log('templateTransaction',JSON.stringify(templateTransaction));
						    		
					        	    
					        	    //check Billable consumption is valid or not
                                    if($scope.itemList!=null && $scope.itemList.length>0  && $scope.templateTransaction.microbiologyTemplateId == null){
                                    	
                                    	var isValidBatch = $scope.itemList.find(obj =>{return (obj.selectedBatch == undefined || obj.selectedBatch.batchId == undefined )})
										 if(isValidBatch!=undefined){
											 $rootScope.stopLoader();
	                                            growl.warning("Please Select Batch Of All Items",
	                                                        {
	                                                            title : 'warning'
	                                                        });
	                                        	$scope.checkSave = false;
	                                                        return false;
	                                                        
										 } 
                                    	
                                    	var isValidBatch = $scope.itemList.find(obj =>{return (obj.selectedUom == undefined || obj.selectedUom.uomTypeId == undefined )})
										 if(isValidBatch!=undefined){
											 $rootScope.stopLoader();
	                                            growl.warning("Please Select UOM Type Of All Items",
	                                                        {
	                                                            title : 'warning'
	                                                        });
	                                        	$scope.checkSave = false;
	                                                        return false;
										 } 
                                    	
                                    	var isValidBatch = $scope.itemList.find(obj =>{return (obj.consumedQuantity == undefined || obj.consumedQuantity == 0 )})
										 if(isValidBatch!=undefined){
											 $rootScope.stopLoader();
	                                            growl.warning("Please Enter Consumed Quantity",
	                                                        {
	                                                            title : 'warning'
	                                                        });
	                                        	$scope.checkSave = false;
	                                                        return false;
										 } 
									}
                                    $scope.showButton  = true;
                                    $scope.checkSave = true;
                                    templateTransaction.isReportRecall = ($stateParams.microbiologyReportObject.isRecall!=null && $stateParams.microbiologyReportObject.isRecall == 'Y') ? true : false;
					        	    var URI = BASE_URL + ROOT_URL + "/LIS/Microbiology/Template/saveTemplate";
					        	    BillingGenericService.serviceAction("POST",URI,templateTransaction,NOTIFICATION_MSG_STATUS_TRUE).then(function(response) {
						    			if (response.data.status == "success")
						    			{
						    				
						    				$scope.isTransactionFlag='Y';
						    				$scope.templateName=$scope.microbiologyReport.title;
						    				console.log("updated template transaction",response.data.object);
						    				$scope.microbiologyReportObj.templateTransactionId=response.data.object;
						    				if(flag=='Y'){
						    					
						    					if($scope.itemList!=null && $scope.itemList.length>0 && $scope.templateTransaction.microbiologyTemplateId == undefined && $scope.templateTransaction.microbiologyTemplateId == null){
													$scope.saveNewConsumption();
												}
						    					

						    					var newObj = {};
									        	  newObj.organizationId = $scope.organizationId;
												  newObj.unitId =  $scope.unitId;
												  newObj.patientName = $scope.microbiologyReportObj.patientName;
												  newObj.orderDate = $scope.microbiologyReportObj.orderDate;
												  newObj.prefixCode = $scope.microbiologyReportObj.prefixCode;
												  newObj.referralName = $scope.microbiologyReportObj.referralName;
												  newObj.email = $scope.microbiologyReportObj.email;
												  newObj.ageString = $scope.microbiologyReportObj.ageString;
												  newObj.genderCode = $scope.microbiologyReportObj.genderCode;
												  newObj.templateContent = $scope.value; 
												  newObj.uhidNumber = $scope.microbiologyReportObj.uhid;
												  newObj.genderId = $scope.microbiologyReportObj.genderId;
												  newObj.labSampleNo = $scope.microbiologyReportObj.labSampleNo;
												  newObj.orderNo = $scope.microbiologyReportObj.orderNo;
												  newObj.visitNo = $scope.microbiologyReportObj.visitNo;
												  newObj.printWithHeader = 0 ; //print with header
												  newObj.companyDesc = $scope.microbiologyReportObj.companyDesc;
												  newObj.comAddress = $scope.microbiologyReportObj.comAddress;
												  newObj.paymentEntitlementId = $scope.microbiologyReportObj.paymentEntitlementId;
												  newObj.isProvisionalRelease = templateTransaction.isProvisionalRelease;
												  newObj.registerDate = $scope.microbiologyReportObj.registerDate;
												  newObj.unitName = cookieObject.unitNme;
												  newObj.isFinalRelease = templateTransaction.isFinalRelease;
												  newObj.reportEntryDate = response.data.object.reportEntryDate;
												  newObj.reportValidationDate = response.data.object.reportValidationDate;
												  newObj.reportReleaseDate = response.data.object.reportReleaseDate;
												  newObj.sampleType = $scope.microbiologyReportObj.sampleType;
												  newObj.orderDtTime = $scope.microbiologyReportObj.orderDtTime;
												  newObj.isReportRecall = response.data.object.isReportRecall;
												  newObj.labSampleDetailsId = $scope.microbiologyReportObj.labSampleDtlsId;
												  newObj.orderDetailsId = $scope.microbiologyReportObj.orderDetailsId;
												  newObj.validatedBy = $scope.microbiologyReportObj.validatedBy;
												  newObj.reportEntryBy = $scope.microbiologyReportObj.reportEntryBy;
                                                  newObj.tariffId = $scope.microbiologyReportObj.tariffId;
                                                  newObj.fromLisTemplate = true;

												if((flag=='Y' && isReleaseFlag ==1 ) || ( $scope.printLabReportBeforRelease != null &&  $scope.printLabReportBeforRelease == 'Y')){
													
													if($stateParams.microbiologyReportObject.isHtmlPrint==true){
														  var URI = BASE_URL + ROOT_URL + "/api/opd/RIS/printReportTestLab";
												    		BillingGenericService.serviceAction(METHOD_POST, URI, newObj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
												    				{
												    	
												    			   if(response.data.status=='success')
												    			      {
												    				   console.log("gg",response.data.object);
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
												    						 if(true){
												    							 patientDetails = response.data.object.header;
												    						 }else{
												    							 patientDetails = response.data.object.labHeaderWithoutImg;
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
							                                           if(true){
							                                                 printWindow.document.write('<div class="page-header" style="text-align: center">');
							                                                 printWindow.document.write(letterImgAndHeader);  
							                                                 printWindow.document.write('</div>');
							                                                 
							                                                 printWindow.document.write('<div class="page-footer" style="text-align: center">');
									                                         printWindow.document.write(response.data.object.footerImg);  
									                                         printWindow.document.write('</div>');
							                                           }else if($scope.isRisPatientDetailsOnFirstPage == 'N'){
							                                        	   //skip this part during first page bcoz we append patient details allready
							                                                printWindow.document.write('<div class="page-header" style="text-align: center">');
							                                                printWindow.document.write(response.data.object.labHeaderWithoutImg);  
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
														  }else
															  {
																var data = {
																		"appName": "HIS",
																		"appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
																		"reportDesignFilePath": "Pathologytemplatereport.rpt",
																		"reportFormat": "PDF",
																		"exportFileName": "Pathologytemplatereport",
																		"reportParams": [
																		{
																		"paramName": "lab_sample_dtls_id",
																		"paramType": "Integer",
																		"paramValue": $stateParams.microbiologyReportObject.labSampleDtlsId
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
															  }
													
												}
												  

						    					
						    					$timeout(function () {
						    						
						    						
						    						if($stateParams.microbiologyReportObject.reportType == 1 && $stateParams.microbiologyReportObject.isRecall==undefined){
						    						$state.go('microbiologyReportEntry');
						    						}else if($stateParams.microbiologyReportObject.reportType == 2  && $stateParams.microbiologyReportObject.isRecall==undefined){
						    							$state.go('microbiologyValidation');
						    						}else if($stateParams.microbiologyReportObject.reportType == 2  && ($stateParams.microbiologyReportObject.isRecall!=undefined || $stateParams.microbiologyReportObject.isRecall == 'Y')){
						    							$state.go('microbiologyRelease');
						    						}
						    						
						    					}, 4000);
						    					
						    				}else{
						    					
						    					$scope.getSaveData($stateParams.microbiologyReportObject.labSampleDtlsId);
						    				}
						    				
						    				//$scope.printReport($scope.microbiologyReportObj.templateTransactionId)
										}
						    			$scope.checkSave = false;
						    			$scope.showButton  = false;
										$rootScope.stopLoader();
						    		});
					        }
					        
					        $scope.goBack = function(){
					        	
	    						if($stateParams.microbiologyReportObject.reportType == 1 && $stateParams.microbiologyReportObject.isRecall==undefined){
	    							$state.go('microbiologyReportEntry');
	    						}else if($stateParams.microbiologyReportObject.reportType == 2  && $stateParams.microbiologyReportObject.isRecall==undefined){
	    							$state.go('microbiologyValidation');
	    						}else if($stateParams.microbiologyReportObject.reportType == 2  && ($stateParams.microbiologyReportObject.isRecall!=undefined || $stateParams.microbiologyReportObject.isRecall == 'Y')){
	    							$state.go('microbiologyRelease');
	    						}
					        }
					        $scope.updateFinalReport=function(flag){

				        		$rootScope.startLoader();
				        		
								var templateTransaction = angular.copy($scope.templateTransaction);
								templateTransaction.templateTransactionId = $scope.microbiologyReportObj.templateTransactionId;
								templateTransaction.templateId = $scope.microbiologyReport.templateId;
								templateTransaction.title= $scope.microbiologyReport.title;
								templateTransaction.specialityId = $scope.microbiologyReportObj.specialityId;
								templateTransaction.subSpecialityId = '';
								templateTransaction.templateObject = $scope.microbiologyReport.templateObject;
								templateTransaction.encounterId=$scope.microbiologyReportObj.encounterId;
								templateTransaction.doctorId=$scope.microbiologyReportObj.doctorId;
								templateTransaction.patientId=$scope.microbiologyReportObj.patientId;
								templateTransaction.orderDetailsId=$scope.microbiologyReportObj.orderDetailsId;
								templateTransaction.patientName =$scope.microbiologyReportObj.patientName;
								templateTransaction.mobileNumber =$scope.microbiologyReportObj.mobileNumber;
								templateTransaction.patientMail =$scope.microbiologyReportObj.patientMail;
								templateTransaction.isFinalRelease=flag;
								templateTransaction.uhidNumber=$scope.microbiologyReportObj.uhidNumber;
								templateTransaction.orgId=$scope.organizationId;
								templateTransaction.visitTypeId=$scope.microbiologyReportObj.visitTypeId;
								templateTransaction.unitId=$scope.unitId;
								
								
								if($scope.microbiologyReport.templateFormatId==1)
								{	
								var jsonObject = angular.equals({},$scope.microbiologyReport.templateObject.obj)?JSON.parse($scope.microbiologyReport.templateObject.obj):{};
								var templateObj = JSON.parse($scope.microbiologyReport.templateObject);
								templateObj.obj = $scope.obj;
								templateTransaction.jsonObject = JSON.stringify(jsonObject);
								templateTransaction.templateObject = JSON.stringify(templateObj);
								}
								else if($scope.microbiologyReport.templateFormatId==2) {
									templateTransaction.templateObject = $scope.value;
								}
				        	    console.log('templateTransaction',JSON.stringify(templateTransaction));
					    		
				        	    var URI = BASE_URL + ROOT_URL + "/global/template/transaction";
				        	    BillingGenericService.serviceAction(METHOD_PUT,URI,templateTransaction,NOTIFICATION_MSG_STATUS_TRUE).then(function(response) {
					    			if (response.data.status == "success")
					    			{
					    				$scope.isTransactionFlag='Y';
					    				$scope.templateName=$scope.microbiologyReport.title;
					    				console.log("updated template transaction",response.data.object);
					    				$scope.microbiologyReportObj.templateTransactionId=response.data.object;
					    				if(flag=='Y'){
					    					var data =  {
							    		            "appName": "HIS",
							    		            "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
							    		            "reportDesignFilePath": "MicrobiologyReport.rpt",
							    		            "reportFormat": "PDF",
							    		            "exportFileName": "MadiologyReport",
							    		            "reportParams": [
							    		              {
							    			             "paramName": "template_transaction_id",
							    			             "paramType": "Integer",
							    			             "paramValue": response.data.object
							    		              }
							    		            ]
							    		 };
							    	  reportService.generate(REPORT_API_URI1,data);
					    					$state.go('microbiologyValidation');
					    				}
									}
									$rootScope.stopLoader();
					    		});
					        }
					        $scope.printReport=function(obj)
					    	{
					    		

		    					var newObj = {};
					        	  newObj.organizationId = $scope.organizationId;
								  newObj.unitId =  $scope.unitId;
								  newObj.patientName = $scope.microbiologyReportObj.patientName;
								  newObj.orderDate = $scope.microbiologyReportObj.orderDate;
								  newObj.prefixCode = $scope.microbiologyReportObj.prefixCode;
								  newObj.referralName = $scope.microbiologyReportObj.referralName;
								  newObj.email = $scope.microbiologyReportObj.email;
								  newObj.ageString = $scope.microbiologyReportObj.ageString;
								  newObj.genderCode = $scope.microbiologyReportObj.genderCode;
								  newObj.templateContent = $scope.value; 
								  newObj.uhidNumber = $scope.microbiologyReportObj.uhidNumber;
								  newObj.genderId = $scope.microbiologyReportObj.genderId;
								  newObj.labSampleNo = $scope.microbiologyReportObj.labSampleNo;
								  newObj.orderNo = $scope.microbiologyReportObj.orderNo;
								  newObj.visitNo = $scope.microbiologyReportObj.visitNo;
								  newObj.printWithHeader = 0 ; //print with header
								  newObj.reportEntryDate = $scope.microbiologyReportObj.reportEntryDate;
								  newObj.reportValidationDate = $scope.microbiologyReportObj.reportValidationDate;
								  newObj.reportReleaseDate = $scope.microbiologyReportObj.reportReleaseDate;
								  newObj.sampleType = $scope.microbiologyReportObj.sampleType;
								  newObj.orderDtTime = $scope.microbiologyReportObj.orderDtTime;
								  newObj.isReportRecall = $scope.microbiologyReportObj.isReportRecall;
								  newObj.labSampleDetailsId = $scope.microbiologyReportObj.labSampleDtlsId;
								  newObj.orderDetailsId = $scope.microbiologyReportObj.orderDetailsId;
                                  newObj.validatedBy = $scope.microbiologyReportObj.validatedBy;
                                  newObj.reportEntryBy = $scope.microbiologyReportObj.reportEntryBy;
                                  newObj.tariffId = $scope.microbiologyReportObj.tariffId;
                                  newObj.fromLisTemplate = true;

								  var URI = BASE_URL + ROOT_URL + "/api/opd/RIS/printReportTestLab";
						    		BillingGenericService.serviceAction(METHOD_POST, URI, newObj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
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
						    						 if(true){
						    							 patientDetails = response.data.object.header;
						    						 }else{
						    							 patientDetails = response.data.object.labHeaderWithoutImg;
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
	                                           if(true){
	                                                 printWindow.document.write('<div class="page-header" style="text-align: center">');
	                                                 printWindow.document.write(letterImgAndHeader);  
	                                                 printWindow.document.write('</div>');
	                                                 
	                                                 printWindow.document.write('<div class="page-footer" style="text-align: center">');
			                                         printWindow.document.write(response.data.object.footerImg);  
			                                         printWindow.document.write('</div>');
	                                           }else  if($scope.isRisPatientDetailsOnFirstPage == 'N'){
	                                                printWindow.document.write('<div class="page-header" style="text-align: center">');
	                                                printWindow.document.write(response.data.object.labHeaderWithoutImg);  
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
					        	
								/*var URI = BASE_URL + ROOT_URL + "/api/opd/RIS/lab/printReport";
								$("#LabPrint").remove();
							     var form = null;
							     form = document.createElement("form");
							     form.setAttribute("id", "LabPrint");
							     form.setAttribute("name", "Lab Report");
							     form.setAttribute("method", "post");
							     form.setAttribute("action", URI);
							     form.setAttribute("target", "_blank");
							     var hiddenField1 = document.createElement("input"); 
							     hiddenField1.setAttribute("type", "hidden");
							     hiddenField1.setAttribute("name", "reportParam");
							     hiddenField1.setAttribute("value", JSON.stringify(newObj));
							     form.appendChild(hiddenField1);
							     
							     document.body.appendChild(form);
							     $("form[name='Lab Report']").submit();*/
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
									
									$scope.setFirstTabLevel(1);
									
									$scope.selectAllRows = false;
									   $scope.selectAllItems = function () {
									        if ($scope.selectAllRows) {
									        	  angular.forEach($scope.searchNewItemList,function(value,index) {
									        		  value.isSelected = true;
									            });
									      }else{
									    	  angular.forEach($scope.searchNewItemList,function(value,index) {
								        		  value.isSelected = false;
								            });
									      }
									   }
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
						} ]);
