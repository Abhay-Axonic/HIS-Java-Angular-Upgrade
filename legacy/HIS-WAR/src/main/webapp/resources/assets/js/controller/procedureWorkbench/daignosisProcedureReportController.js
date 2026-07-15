'use strict';

/**
 * @Author By  Vivek Satle
 * @name myApp.controller:radiologyWorkbenchController
 * @description #radiologyWorkbenchController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'daignosisProcedureReportController',
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
						function($scope, $cookies, $rootScope, GenericService,
								BillingGenericService, $state, $timeout,
								$interval, GenericServiceParamHeader,$compile,reportService,$stateParams,growl) {

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
								$scope.roleId = 2;
								$scope.unitName = cookieObject.unitNme;
								
								$rootScope.loginpage = true;
								$scope.saveBtnFlag = false;
								$scope.updateBtnFlag = true;

								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup

								
								
							}

							$scope.init();
							
							  $scope.doctorsList = [];
						       $scope.getDoctorList=function(speArr){
						    	   var data = {
		                            		  organizationId:$scope.organizationId,
		                            		  unitId:$scope.unitId,
		                            		  specialityArray:speArr
		                              }
		                              var URI = BASE_URL + ROOT_URL + GETDOCTORSLISTAGANISTSPECIALITIES;
								BillingGenericService.serviceAction(METHOD_POST, URI,data,
												NOTIFICATION_MSG_STATUS_FALSE).then(
												function(response) {
													if (response.data.status == "success")
														{
															$scope.doctorsList = [];
															$scope.doctorsList = response.data.listObject;
														}
														
												});
						       }
						       
							if (angular.equals({},
									$stateParams.procedureReportObject)) {
								$state.go('radiologyResultEntry');
								return;
							} else {
								console.log('DJ$stateParams.procedureReportObject',
										$stateParams.procedureReportObject);
							
									if($stateParams.procedureReportObject.isResultEntry=='Y'){
										 $(".selectedPageName").text("Diagnostic Procedure Result Entry");
										$state.current.title="Diagnostic Procedure Result Entry";
									}else{
										 $(".selectedPageName").text("Diagnostic Procedure Result Validation");
										$state.current.title="Diagnostic Procedure Result Validation";
									}
								
								//for patient demography details start 
								$scope.patientDetails.uhIdNumber=$stateParams.procedureReportObject.uhidNumber;
								$scope.patientDetails.patientName=$stateParams.procedureReportObject.patientName;
								$scope.patientDetails.genderCode=$stateParams.procedureReportObject.genderCode;
								$scope.patientDetails.age=$stateParams.procedureReportObject.ageString;
								$scope.patientDetails.birthDate=$stateParams.procedureReportObject.dob;
								$scope.patientDetails.visitTypeId=$stateParams.procedureReportObject.visitTypeId;
								$scope.patientDetails.dateOfAdmission=$stateParams.procedureReportObject.doa;
								$scope.patientDetails.patientDischargeDate=$stateParams.procedureReportObject.pdd;
								$scope.patientDetails.bedNumber=$stateParams.procedureReportObject.bedNumber;
								if($scope.patientDetails.bedNumber=='0'){
									$scope.patientDetails.bedNumber='';
								}
								
								var dateDoa = moment($stateParams.procedureReportObject.doa, 'DD/MM/YYYY');
								var datePdd = moment($stateParams.procedureReportObject.pdd, 'DD/MM/YYYY');
								
								$scope.patientDetails.los = $rootScope.dateDiffrence(dateDoa,datePdd);
								
								$scope.patientDetails.doctorFullName=$stateParams.procedureReportObject.doctorName;
								if($scope.patientDetails.doctorFullName != null){
								if($scope.patientDetails.visitTypeId!=1){
									$scope.patientDetails.doctorFullName=$scope.patientDetails.doctorFullName.toString().replace("Dr.","");
								}
								}
								//for patient demography details end 
								$scope.radioLogyReportObj = $stateParams.procedureReportObject;
								var rediologyObj=$stateParams.procedureReportObject;
								if($stateParams.procedureReportObject.templateTypeId==null){
									rediologyObj.templateTransactionId=null;
								}
								
								
					        	$rootScope.startLoader();
					        	if(rediologyObj.templateTransactionId==null)
					        		{
//					        		var templateTypeIds=[];
//					        		 templateTypeIds.push(20);
					        		$scope.isTransactionFlag='N'; // for showing drop down list
					        		var data={
							        		  orgId : $scope.organizationId,
							        		  unitId : $scope.unitId,
							        		  doctorId : 0,
							        		  specialityId : 0,
							        		  templateTypeId : 20,
							        		  orderDetailsId : '',
							        		  patientId : '',
							        		  visitTypeId: '',
							        		  encounterId: '',
//							        		  templateTypeIds: templateTypeIds,
							        		  isTemplateTypeReq:'Y',
							        		  serviceId: rediologyObj.serviceId
							        		}							       
//					        		var URI = BASE_URL + ROOT_URL+ "/api/dignostic/diagnosisProcedure/templates";
					        		var URI = BASE_URL + ROOT_URL +'/api/global/templates/byService';
					        		BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE)
									.then(function(response) {											 
							    	
							    			   if(response.data.status=='success')
							    			      {
							    				 	$scope.templateListRadiology = response.data.listObject;
							    				 	$scope.radiologyReport = {};
							    				   	$rootScope.stopLoader();
							    			      }
							    			   else if(response.data.status=='error'){
							    				   $rootScope.stopLoader();
							    			   }
							    		});
										   
					        		}
					        	else{
					        		$scope.isTransactionFlag='Y'; // for hiding drop down list
					        		$scope.templateName=rediologyObj.title;
					        		$scope.radiologyReport = {};	
					        		$scope.radiologyReport.templateObject  = rediologyObj.templateObject;
					        		$scope.radioLogyReportObj.templateTransactionId =  rediologyObj.templateTransactionId;
					        		$scope.radiologyReport.templateFormatId  = rediologyObj.templateFormatId;
					        		$scope.radiologyReport.templateId = '';
					        		$scope.radiologyReport.title= '';
					        		$scope.templateDoctorId = rediologyObj.templateDoctorId ;
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
//					        	  	$('#radiologyReport').modal('show');
			    				   	$rootScope.stopLoader();
					        	    }
					        	//get doctor List by speciality id
					        	var speArr = [];
								speArr.push($stateParams.procedureReportObject.specialityId);
								$scope.getDoctorList(speArr);
							}

							
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
					    	
					    	
					        
					        
					        /** Template List*/
					      
					       
					       	$scope.selectTemplate  = function(radiologyReport)
					    	{
					       		if(radiologyReport!=null && radiologyReport!=undefined){
					       			$scope.radiologyReport = radiologyReport;
					       		}
					    		
					    	}
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
					       			$scope.radiologyReport= angular.fromJson($scope.radiologyReport);
					       			$scope.radioLogyReportObj.templateFormatId = 	$scope.radiologyReport.templateFormatId;
			    				   	if($scope.radiologyReport.templateFormatId==1)
			    				   		{
			    				  
			    				   		growl.warning("Please select Word Template...!",{
											title : ERROR_MSG
										});
						        		return;
				    				   
			    				   		}
			    				    else if($scope.radiologyReport.templateFormatId==2)
			    			    	  {
			    				    	 $scope.value =   $scope.radiologyReport.templateObject;
			    				    	 
			    				    	 if($scope.radioLogyReportObj.isHtmlPrint == undefined || $scope.radioLogyReportObj.isHtmlPrint == null){
						    					$scope.radioLogyReportObj.isHtmlPrint = $scope.radiologyReport.isHtmlPrint;
						    				}
			    				    	 
			    			    	  }
			    			
					       			}
					       		else{
						    			growl.warning("Please select Template...!",{
											title : ERROR_MSG
										});
						        		return;
					       		}
					        	
							}
					       	
					
					       	
					        $scope.updateReport = function(flag)
					        {
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
									templateTransaction.isProvisionalRelease=flag;
									templateTransaction.templateDoctorId = $scope.templateDoctorId;
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
						    				$scope.isTransactionFlag='Y';
						    				$scope.templateName=$scope.radiologyReport.title;
						    				console.log("updated template transaction",response.data.object);
						    				$scope.radioLogyReportObj.templateTransactionId=response.data.object;
						    				if(flag=='Y'){
						    					templateTransaction.ageString = $scope.radioLogyReportObj.ageString;
						    					templateTransaction.billNo = $scope.radioLogyReportObj.billNo;
						    					templateTransaction.orderDtTime = $scope.radioLogyReportObj.orderDtTime;
						    					templateTransaction.orderDate = $scope.radioLogyReportObj.orderDate;
						    					templateTransaction.genderId = $scope.radioLogyReportObj.genderId;
						    					templateTransaction.referralName = $scope.radioLogyReportObj.referralName;
						    					templateTransaction.visitNo = $scope.radioLogyReportObj.visitNo;
						    					templateTransaction.orderNo = $scope.radioLogyReportObj.orderNo;
						    					templateTransaction.templateDoctorName = $scope.radioLogyReportObj.templateDoctorName;
						    					templateTransaction.serviceAmt = $scope.radioLogyReportObj.serviceAmt;
						    					templateTransaction.visitNumber =$scope.radioLogyReportObj.visitNumber;
						    					templateTransaction.templateContent = $scope.radioLogyReportObj.templateContent;	
						    					templateTransaction.encounterId = $scope.radioLogyReportObj.encounterId;
						    					templateTransaction.admissionId = $scope.radioLogyReportObj.admissionId;
						    					templateTransaction.encounterTypeId = $scope.radioLogyReportObj.encounterTypeId;
						    					templateTransaction.genderCode= $scope.radioLogyReportObj.genderCode;
						    					templateTransaction.isProvisionalRelease= $scope.radioLogyReportObj.isProvisionalRelease;
						    					templateTransaction.isReportRecall= $scope.radioLogyReportObj.isReportRecall;
						    					templateTransaction.patientCategory= $scope.radioLogyReportObj.patientCategory;
						    					templateTransaction.prefixCode= $scope.radioLogyReportObj.prefixCode;
						    					templateTransaction.registerDate= $scope.radioLogyReportObj.registerDate;
						    					templateTransaction.serviceCode= $scope.radioLogyReportObj.serviceCode;
						    					templateTransaction.serviceId= $scope.radioLogyReportObj.serviceId;
						    					templateTransaction.serviceName= $scope.radioLogyReportObj.serviceName;
						    					templateTransaction.serviceRendered= $scope.radioLogyReportObj.serviceRendered;
						    					templateTransaction.specialityId= $scope.radioLogyReportObj.specialityId;
						    					templateTransaction.specialityName= $scope.radioLogyReportObj.specialityName;
						    					templateTransaction.subSpecialityId= $scope.radioLogyReportObj.subSpecialityId;
						    					templateTransaction.templateDesc= $scope.radioLogyReportObj.templateDesc;
						    					templateTransaction.templateDoctorId= $scope.radioLogyReportObj.templateDoctorId;
						    					templateTransaction.templateFormatId= $scope.radioLogyReportObj.templateFormatId;
						    					templateTransaction.templateObject = $scope.radioLogyReportObj.templateObject;
						    					templateTransaction.templateTransactionId= $scope.radioLogyReportObj.templateTransactionId;
						    					templateTransaction.templateTypeId= $scope.radioLogyReportObj.templateTypeId;
						    					templateTransaction.title= $scope.radioLogyReportObj.title;
						    					templateTransaction.visitTypeId= $scope.radioLogyReportObj.visitTypeId;
						    					templateTransaction.wardName= $scope.radioLogyReportObj.wardName;
						    					templateTransaction.isHtmlPrint= $scope.radioLogyReportObj.isHtmlPrint;
						    					templateTransaction.unitName =  $scope.unitName;
						    					templateTransaction.uhidNumber = $scope.radioLogyReportObj.uhidNumber;
						    					$scope.printDiagnosisReport(templateTransaction);
						    					$state.go('diagnosticProcedureResultEntry');
						    				}
										}
										$rootScope.stopLoader();
						    		});
					        }
					        
					        $scope.printDiagnosisReport = function(templateTransaction){
					        	
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
		    					info.templateContent = templateTransaction.templateContent != null ? templateTransaction.templateContent: $scope.value;		
		    					info.templateDesc= templateTransaction.templateDesc;
		    					info.templateDoctorId= templateTransaction.templateDoctorId;
		    					info.templateDoctorName= templateTransaction.templateDoctorName;
		    					info.templateFormatId= templateTransaction.templateFormatId;
		    					info.templateObject = templateTransaction.templateContent != null ? templateTransaction.templateContent: $scope.value;	
		    					info.templateTransactionId= templateTransaction.templateTransactionId;
		    					info.templateTypeId= templateTransaction.templateTypeId;
		    					info.title= templateTransaction.title;
		    					info.uhidNumber= templateTransaction.uhidNumber;
		    					info.visitName= templateTransaction.visitName;
		    					info.visitNumber= templateTransaction.visitNumber;
		    					info.visitTypeId= templateTransaction.visitTypeId;
		    					info.wardName= templateTransaction.wardName;
		    					info.organizationId = $scope.organizationId;
		    					info.orderDtTime = templateTransaction.orderDtTime;
		    					info.billNo = templateTransaction.billNo;
		    					info.isHtmlPrint = (templateTransaction.isHtmlPrint != undefined  && templateTransaction.isHtmlPrint!= null) ? templateTransaction.isHtmlPrint: $scope.radioLogyReportObj.isHtmlPrint;
						        	info.unitId =  $scope.unitId;
						        	info.printWithHeader = 0; //print with header
						        	info.unitName =  $scope.unitName;
						        	if(info.isHtmlPrint==true)
						        		{
						        		
						        		
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
						        		}else
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
						        		}
						      }
					        
					        $scope.updateFinalReport=function(flag){

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
								templateTransaction.isFinalRelease=flag;
								templateTransaction.isProvisionalRelease='Y';
								templateTransaction.uhidNumber=$scope.radioLogyReportObj.uhidNumber;
								templateTransaction.orgId=$scope.organizationId;
								templateTransaction.visitTypeId=$scope.radioLogyReportObj.visitTypeId;
								templateTransaction.unitId=$scope.unitId;
								
								
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
					    				$scope.isTransactionFlag='Y';
					    				$scope.templateName=$scope.radiologyReport.title;
					    				console.log("updated template transaction",response.data.object);
					    				$scope.radioLogyReportObj.templateTransactionId=response.data.object;
					    				if(flag=='Y'){
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
							    			             "paramValue": response.data.object
							    		              }
							    		            ]
							    		 };
							    	  reportService.generate(REPORT_API_URI1,data);*/
					    					templateTransaction.ageString = $scope.radioLogyReportObj.ageString;
					    					templateTransaction.billNo = $scope.radioLogyReportObj.billNo;
					    					templateTransaction.orderDtTime = $scope.radioLogyReportObj.orderDtTime;
					    					templateTransaction.orderDate = $scope.radioLogyReportObj.orderDate;
					    					templateTransaction.genderId = $scope.radioLogyReportObj.genderId;
					    					templateTransaction.referralName = $scope.radioLogyReportObj.referralName;
					    					templateTransaction.visitNo = $scope.radioLogyReportObj.visitNo;
					    					templateTransaction.orderNo = $scope.radioLogyReportObj.orderNo;
					    					templateTransaction.templateDoctorName = $scope.radioLogyReportObj.templateDoctorName;
					    					templateTransaction.serviceAmt = $scope.radioLogyReportObj.serviceAmt;
					    					templateTransaction.visitNumber =$scope.radioLogyReportObj.visitNumber;
					    					templateTransaction.templateContent = $scope.radioLogyReportObj.templateContent;	
					    					templateTransaction.encounterId = $scope.radioLogyReportObj.encounterId;
					    					templateTransaction.admissionId = $scope.radioLogyReportObj.admissionId;
					    					templateTransaction.encounterTypeId = $scope.radioLogyReportObj.encounterTypeId;
					    					templateTransaction.genderCode= $scope.radioLogyReportObj.genderCode;
					    					templateTransaction.isProvisionalRelease= $scope.radioLogyReportObj.isProvisionalRelease;
					    					templateTransaction.isReportRecall= $scope.radioLogyReportObj.isReportRecall;
					    					templateTransaction.patientCategory= $scope.radioLogyReportObj.patientCategory;
					    					templateTransaction.prefixCode= $scope.radioLogyReportObj.prefixCode;
					    					templateTransaction.registerDate= $scope.radioLogyReportObj.registerDate;
					    					templateTransaction.serviceCode= $scope.radioLogyReportObj.serviceCode;
					    					templateTransaction.serviceId= $scope.radioLogyReportObj.serviceId;
					    					templateTransaction.serviceName= $scope.radioLogyReportObj.serviceName;
					    					templateTransaction.serviceRendered= $scope.radioLogyReportObj.serviceRendered;
					    					templateTransaction.specialityId= $scope.radioLogyReportObj.specialityId;
					    					templateTransaction.specialityName= $scope.radioLogyReportObj.specialityName;
					    					templateTransaction.subSpecialityId= $scope.radioLogyReportObj.subSpecialityId;
					    					templateTransaction.templateDesc= $scope.radioLogyReportObj.templateDesc;
					    					templateTransaction.templateDoctorId= $scope.radioLogyReportObj.templateDoctorId;
					    					templateTransaction.templateFormatId= $scope.radioLogyReportObj.templateFormatId;
					    					templateTransaction.templateObject = $scope.radioLogyReportObj.templateObject;
					    					templateTransaction.templateTransactionId= $scope.radioLogyReportObj.templateTransactionId;
					    					templateTransaction.templateTypeId= $scope.radioLogyReportObj.templateTypeId;
					    					templateTransaction.title= $scope.radioLogyReportObj.title;
					    					templateTransaction.visitTypeId= $scope.radioLogyReportObj.visitTypeId;
					    					templateTransaction.wardName= $scope.radioLogyReportObj.wardName;
					    					templateTransaction.unitName =  $scope.unitName;
					    					$scope.printDiagnosisReport(templateTransaction);
					    					$state.go('diagnosticProcedureResultValidation');
					    				}
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