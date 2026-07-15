/************EDITED ************************/
'use strict';

/**
 * @Author Sneha Dalave
 * @name myApp.controller:MISReportsController
 *
 */
angular
		.module('myApp')
		.controller(
				'MLCReportingWorklistController',
				[
						'$scope',
						'$cookies',
						'$rootScope',
						'GenericService',
						'PagerService',
						'BillingGenericService',
						'$state',
						'$timeout',
						'$interval',
						'$stateParams',
						'GenericServiceParamHeader',
						'GenericServiceFileUpload',
						'growl',
						'$compile',
						'reportService',
						function($scope, $cookies, $rootScope, GenericService,PagerService,
								BillingGenericService, $state, $timeout,$interval,
								$stateParams,GenericServiceParamHeader,GenericServiceFileUpload,growl,$compile,reportService) {
							
							
							$rootScope.loginpage = true;
							$scope.saveBtnFlag = true;
							$scope.updateBtnFlag = false;
							$scope.updateScope = false;
							$scope.popUpFlag = true;
				
							var cookieObject = $cookies
							.getObject('cookieObject');
							if (cookieObject == undefined) {
								$state.go('login');
								return;
							}
							$scope.unitId = cookieObject.unitId;
							$scope.organizationId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							
							var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
							if(cookieObjectAppConfig == undefined){
								$state.go('applicationConfiguration');
								return;
							}
							else
								{
									console.log('cookieObjectAppConfig',cookieObjectAppConfig);
									
								}
							
							/**Variables for pagination */
							$scope.offset = 0;
							$scope.noOfRecordsPerPage = 10;
							$scope.commonList = [];
							$scope.totalNoOfRecords;
							$scope.searchKeyword;
							$scope.commonListCount;
							
							/* init() function for form object create. */
							$scope.init = function() {
								var date = new Date();
								$scope.dateOptions = {
									formatYear : 'yyyy',
									showWeeks : false,
									minDate : date.setDate((new Date()).getDate() - 90),
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
								$scope.selectedDepartMentList=[];
								$scope.selectedModalityList = [];
								$scope.selectedtestList=[];
								
								var commonObj = {
										unitId : $scope.unitId,
										organizationId : $scope.organizationId
									};
								
								$scope.encounterDetailsList = [];
								$scope.encounterDetails = {
									  encounterDocId : "",
									  admissionId:"",
									  visitTypeId:2,
									  patientId : "",
									  documentPath : "",
									  documentTypeId : "",
									  documentTypeId : "",
									  uploadDoccumentDate:new Date(),
									  status : 'A',
									  createdDate : "",
									  createdBy : $scope.userId,
									  updatedDate : "",
									  updatedBy : $scope.userId,
									  unitId : $scope.unitId,
									  organisationId : $scope.organizationId,
									  file : {},
								     dateOptionsUpload :
    							    {
    								    formatYear : 'yyyy',
    								    showWeeks : false,
    								    maxDate : new Date(),
    							    }
									}
								
								  $scope.dateOptionsUpload =
   							    {
   								    formatYear : 'yyyy',
   								    showWeeks : false,
   								    maxDate : new Date(),
   							    }
							}

							$scope.init();
							
							
							
							
							
							$scope.getToDate=function(){
								$scope.todateOptions.minDate=$scope.datepickerFromDate.getTime();
								
								if(moment($scope.datepickerToDate).isBefore(moment($scope.datepickerFromDate)))
								{
									 $scope.datepickerToDate=new Date($scope.datepickerFromDate.getTime());
								}
							}
							
							$scope.myFunction = function(){
							  var d = new Date();
							  d.setMonth(4, 20);
							 console.log("converted date",d);
							}
							
							
							 $scope.datepickerUploadK = function(index) {

	                             for (var i = $scope.encounterDetailsList.length - 1; i >= 0; i--) {
	                                 if (i === index) {
	                                     $scope.datepickerUpload.opened[i] = true;
	                                 } else {
	                                	
	                                     $scope.datepickerUpload.opened[i] = false;
	                                 }
	                             }
	                         };

	                         $scope.datepickerUpload = {
	                             opened : [ false ],

	                         };
	                         
							
							 $scope.searchGroupList = function(keyword) {

									var data = {
										patientName : keyword,
										unitId : $scope.unitId,
										organizationId : $scope.organizationId
									}
									console.log(data);
									//$rootScope.startLoader();
									var URI = BASE_URL + ROOT_URL
											+ PATIENTAUTOFILLSEARCH;
									
									return BillingGenericService
											.serviceAction(METHOD_POST, URI,
													data,
													NOTIFICATION_MSG_STATUS_FALSE)
											.then(
													function(response) {
														console.log(response);
														// $rootScope.startLoader();
														if (response.data.status == "success")
															return response.data.listObject;
														//$rootScope.stopLoader();
													});

								}
							 
							 
							 $scope.clearSearchObject = function(){
								 $scope.patientId = null;
								 $scope.data = {
											'organizationId' : $scope.organizationId,
											'unitId' : $scope.unitId,
											'patientId' : 0,
										}
										$scope.selectedVisitType=[];
										$scope.selectedtestList=[];
										$scope.selectedDepartMentList=[];
										
										$scope.patientName = '';
										$scope.uhidNumber = '';

										$scope.datepickerFromDate = new Date();
										$scope.datepickerToDate = new Date();
							 }
							 
							 
							 $scope.selectPatientId = function(info, model,
										label) {
									
									$scope.patientName = info.patientName;
									$scope.uhidNumber = info.uhidNumber;
									$scope.mobileNumber = info.mobileNumber;
									$scope.patientId = info.patientId;
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
								
								$scope.openImage=function(index){
									$rootScope.startLoaderPopup();
									var imageUrl = $scope.patientDocumentList[index].documentPath;
									var href = $rootScope.currentHrefDoccument();
									$.fancybox.open({
										width: 600, // or whatever
										height: 320,
										href:href+imageUrl,
										type: 'iframe'
									});
	                                $rootScope.stopLoaderPopup();
								}
								
								
								$scope.getPatientDocumentList = function(obj)
							     {
							      
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
							        	angular.forEach($scope.patientDocumentList,function(value, key) {							        		
							        	    if(value.bucketFlag == 1){
							        		var filepath = value.documentPath
							        		var arr = filepath.split("/");
							        		var length=arr.length;
							        		var fileName1=arr[length-1];
							        		var arr1 = fileName1.split("_");
							        		var length1=arr1.length;
							        		var fileName=arr1[length1-1];
							        		value.fileName=fileName;
							        	    }else{
							        	    	
							        	    	value.fileName=value.fileName;
							        	    }
							        	});
							        		
							        	
							           
							            $rootScope.stopLoader();
							        }
							        else if(response.data.status == "error")
							         {
							        	$rootScope.stopLoader();
							         }
							       });
							     }
								
							 $scope.showDocUPloadPoup   = function(encounterObj)
								{
								 $scope.forView = false;
								 $scope.encounterDetails={};
									 $('#fileId').val(undefined);
									$scope.encounterDetailsList=[];
									$scope.encounterDetails.patientId = encounterObj.patientId;	
									$scope.encounterDetails.visitTypeId = encounterObj.visitTypeId;	
									$scope.encounterDetails.createdBy = $scope.userId;
									$scope.encounterDetails.updatedBy = $scope.userId;
									$scope.encounterDetails.unitId = $scope.unitId;
									$scope.encounterDetails.organisationId = $scope.organizationId;
									$scope.encounterDetails.uploadDoccumentDate=new Date();
									if($scope.encounterDetailsList.length>0)
									{
									  $('#uploadDetails').modal('show');
									}
								else{
									 $('#uploadDetails').modal('show');
									var encounterDetails = angular.copy($scope.encounterDetails);
									encounterDetails.patientId = encounterObj.patientId;
									$scope.encounterDetailsList.push(encounterDetails);
								}
									//$scope.getPatientDocumentList(encounterObj);
									
										
								}
							 $scope.forView = false;
							 $scope.uploadPatientImage = function()
							    {
								  $scope.finalDocUploadList=[];
								  var count=0;
								  $scope.forView = false;
								 angular.forEach($scope.encounterDetailsList, function(value, key) {  
									  if(value.documentTypeId==undefined || value.documentTypeId=="" || value.documentTypeId==null){
											 growl.warning("Please select document type .",
																		{
																			title : 'warning!'
																		});
															   $rootScope.stopLoader();
											return false;
										}
									  if (angular.equals(value.file, {}) || value.file==undefined) {
											growl.warning('Please select file.',
															{
																title : 'warning!'
															});
											 $rootScope.stopLoader();
											return false;
										}
										 var ext = value.file.name.split(".").pop().toLowerCase();
											if($.inArray(ext, ["doc","pdf",'docx',"txt"]) == -1) {
												growl.warning('You can only upload *.txt, *.pdf, *.docx, *.doc file',
														{
															title : 'warning!'
														});
												$rootScope.stopLoader();
												return false;
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
														   $rootScope.stopLoader();
														 }
												      }
												  else 
													{
														growl.error('Something went wrong.',
																{
																	title : 'Error!'
																});
													}
											    });
											
									});
								 }
								
							    }
								$scope.uploadEncounterDoc = function(Obj)
								{
									 var fd = new FormData();
								        fd.append('encounterId', 0);
								        fd.append('visitTypeId', Obj.visitTypeId);
								        fd.append('admissionId', 0);
								        fd.append('patientId',Obj.patientId );
								        fd.append('documentTypeId', Obj.documentTypeId);
								        fd.append('createdBy',Obj.createdBy );
								        fd.append('updatedBy', Obj.updatedBy);
								        fd.append('unitId', Obj.unitId);
								        fd.append('organisationId', Obj.organisationId);
								        fd.append('uploadDoccumentDate', moment(Obj.uploadDoccumentDate).format('DD-MM-YYYY HH:mm:ss'));
								        fd.append('file',Obj.file );
								        fd.append('expenseId', 0);
								        fd.append('grnId',0);
										fd.append('poId',0);
								        fd.append('incidentReportingMapperId',0);
								        fd.append('companyExpenseId',0);
								        fd.append('createdDate',null);
										fd.append('fileEditedName',null);
										fd.append('opbId',0);
										fd.append('vendorId',0);
								        var URI1 = BASE_URL + ROOT_URL + UPLOAD_ENCOUNTER_DOCUMENT;
								        return GenericServiceFileUpload.serviceAction("POST",URI1,fd,{ 'Content-Type': undefined}).then(function(response) 
										{	
											return response;
										});
								}
								$scope.viewUploadedDoccument = function(encounterObj)
								{
									$scope.forView = true;
									$scope.getPatientDocumentList(encounterObj);
									$('#uploadDetails').modal('show');
								}
								
								
//							$scope.getPatientListForMLCworklist = function() {
//								
//								var visitList =[];
//								$scope.data={};
//								$scope.data.fromDate = moment(
//										$scope.datepickerFromDate).format(
//										'YYYY-MM-DD');
//								$scope.data.toDate = moment(
//										$scope.datepickerToDate).format(
//										'YYYY-MM-DD');
//								
//								angular.forEach($scope.selectedVisitType,
//										function(value,index) {
//									         visitList.push(value.id);
//										});
//								
//								$scope.data.visitList = visitList;
//								$scope.data.organizationId = $scope.organizationId;
//								$scope.data.unitId = $scope.unitId;
//								$scope.data.patientId = $scope.patientId;
//								
////								
////								var data={
////											organizationId: $scope.organizationId,
////											unitId:$scope.unitId
////								};
//								var URI = BASE_URL+ ROOT_URL
//										+ "/adt/getPatientListMedicolegalDetailsMLCWorklist";
//
//								GenericService.serviceAction("POST", URI, $scope.data).then(function(response) {
//													$rootScope.stopLoader();
//													$scope.getPatientListMLCworklist = [];
//													$scope.getPatientListMLCworklist = response.data.listObject;
//													console.log("getPatientListMLCworklist",$scope.getPatientListMLCworklist);
//
//												});
//							}
//							$scope.getPatientListForMLCworklist();
							
							
							
							
							/**List And Pagination Start**/
							$scope.setNoOfRecords = function() {
								$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
							};
							
							$scope.initMasterList = function(organizationId,offset, noOfRecordsPerPage,unitId)
							{
								$rootScope.startLoader();
								
								offset = $scope.offset != null ? $scope.offset : 0;
								noOfRecordsPerPage = $scope.noOfRecordsPerPage != null ? $scope.noOfRecordsPerPage : 10;		
								
								var visitList =[];
								$scope.data={};
								$scope.data.fromDate = moment(
										$scope.datepickerFromDate).format(
										'YYYY-MM-DD');
								$scope.data.toDate = moment(
										$scope.datepickerToDate).format(
										'YYYY-MM-DD');
								
								angular.forEach($scope.selectedVisitType,
										function(value,index) {
									         visitList.push(value.id);
										});
								
								$scope.data.visitList = visitList;
								$scope.data.organizationId = $scope.organizationId;
								$scope.data.unitId = $scope.unitId;
								$scope.data.patientId = $scope.patientId;
								$scope.data.patientId = $scope.patientId;
								$scope.data.offset = $scope.offset;
								$scope.data.noOfRecordsPerPage = $scope.noOfRecordsPerPage;
								$scope.getPatientListMLCworklist = [];
								
								
//								var data2 = {
//										      organizationId:organizationId,
//										      unitId:unitId
//											};
								var URI1 = BASE_URL + ROOT_URL + "/adt/getPatientListMedicolegalDetailsMLCWorklist";
//								var URI2 = BASE_URL + ROOT_URL + "/adt/getMedicolegalDetailsMLCWorklistCount";
									
								
								GenericService.serviceAction("POST",URI1,$scope.data).then(function(response){
									if(response.data.status == "success"){
									$rootScope.stopLoader();					
									$scope.getPatientListMLCworklist = response.data.listObject;
									$scope.commonListCount = response.data.object;
									$scope.setPage(1,false);
									console.log("commonList",$scope.getPatientListMLCworklist);
									console.log("commonListCount",response.data.object);
									}
								});
								
//								GenericService.serviceAction("POST",URI2,data2).then(function(response){
//									if(response.data.status == "success"){					
//										$rootScope.stopLoader();
//										$scope.commonListCount = response.data.object;
//										$scope.setPage(1,false);
//										console.log("commonListCount",response.data.object);
//									}
//								});
								
							}		
							
							$scope.masterList = function(organizationId,offset, noOfRecordsPerPage,unitId) {
								$rootScope.startLoader();
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
								
								var visitList =[];
								$scope.data={};
								$scope.data.fromDate = moment(
										$scope.datepickerFromDate).format(
										'YYYY-MM-DD');
								$scope.data.toDate = moment(
										$scope.datepickerToDate).format(
										'YYYY-MM-DD');
								
								angular.forEach($scope.selectedVisitType,
										function(value,index) {
									         visitList.push(value.id);
										});
								
								$scope.data.visitList = visitList;
								$scope.data.organizationId = $scope.organizationId;
								$scope.data.unitId = $scope.unitId;
								$scope.data.patientId = $scope.patientId;
								$scope.data.patientId = $scope.patientId;
								$scope.data.offset = offset;
								$scope.data.noOfRecordsPerPage = noOfRecordsPerPage;
								$scope.getPatientListMLCworklist = [];
								
								var URI = BASE_URL + ROOT_URL +  "/adt/getPatientListMedicolegalDetailsMLCWorklist";
								console.log("URI", URI);
								GenericService.serviceAction("POST", URI,$scope.data).then(function(response) {
									$rootScope.stopLoader();
									$scope.getPatientListMLCworklist = response.data.listObject;
									$scope.commonListCount = response.data.object;
									$scope.setPage(1,false);
									console.log("commonList",$scope.commonList);
									
								});
							}

							$scope.pager = {};
							$scope.page;

							$scope.setPage = function(page, flag) {
								$scope.pager.totalPages=undefined;
								if (page < 1 || page > $scope.pager.totalPages)
								{
									return;
								}
								$scope.pager = PagerService.GetPager($scope.commonListCount, page, $scope.noOfRecordsPerPage,$scope.unitId);
								if (flag)
								{
									$scope.masterList($scope.organizationId ,$scope.pager.startIndex, $scope.pager.pageSize,$scope.unitId);
								}
							}

							$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
							
							/**List And Pagination End**/
							$scope.showSelectedDocument=function(path){
							    $("#viewer").attr("src", path);
							    $('#scanPreview_Modal').modal('show');
							}


					$scope.updateMedicolegalDetails = function() {
								
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');
							
								var data = {

									accompaniedBy : $scope.accompaniedName,
									firNo        :$scope.firNo,
									policeBuckleNo :$scope.policeBuckleNo,
									policeStationName:$scope.policeStationName,
									mlcContactNo:$scope.mlcContactNo,
									mlcRelationId:$scope.mlcRelationId,
									mlcRelationId:($scope.mlcRelationId != "0" &&  $scope.mlcRelationId != null)?  $scope.mlcRelationId : null,
									priorityId : $scope.priorityId,
									priorityId : ($scope.priorityId != "0" &&  $scope.priorityId != null)?  $scope.priorityId : null,
									status : 'A',
									updatedBy : $scope.userId,
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									admissionId:$scope.patientDetails.admissionId,
									updatedDate:$scope.currentDate,
									medicoLegalId: $scope.medicoLegalId

								}
								
								var URI = BASE_URL + ROOT_URL
										+"/adt/updateMedicolegalDetails";
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													$rootScope.stopLoader();
													if (response.data.status == "success") {
														growl.success(
																		'Updated Successfully...!!',
																		{
																			title : 'Success!'
																		});
														$scope.getMedicolegalDetails();
														
													} 
														
													$rootScope.stopLoader();
												});
							
						

					}

						 $scope.getrelationList = function(){
				 var data = {};
					var URI = BASE_URL + ROOT_URL
					+ GETACTIVERELATIONLIST;
			GenericService
					.serviceAction("GET", URI, data)
					.then(
							function(response) {
								$scope.relationList = [];

								if (response.data.status == "success") {
									$scope.relationList = response.data.listObject;

								}
							});
			 }

					$scope.updateMedicolegalDetailsModel = function(info) {
						 $scope.getrelationList();
						console.log("============>",info);
						
						console.log($scope.relationList);
						
						$scope.editMLCmedicoLegalId = info.medicoLegalId;
						$scope.editMLCaccompaniedName = info.accompaniedBy;
						$scope.editMLCfirNo = info.firNo;
						$scope.editMLCpoliceBuckleNo = info.policeBuckleNo;
						$scope.editMLCpoliceStationName = info.policeStationName;
						$scope.editMLCmlcContactNo = info.mlcContactNo;
						$scope.editMLCmlcRelationId = info.mlcRelationId;
						$scope.editMLCpriorityId = info.priorityId;
						$scope.editMLCremark = info.remark;
                       angular.element('#mlcEditPopup').modal('show');	
					}



					$scope.validateMLCFields = function () {

						var regex = /^(?!.*(<script|<\/script>|javascript:|onerror=|onload=|<|>)).*$/i;

						// Accompanied By
						if ($scope.editMLCaccompaniedName &&
							!regex.test($scope.editMLCaccompaniedName)) {

							growl.error("Invalid Accompanied By");
							return false;
						}

						// FIR No
						if ($scope.editMLCfirNo &&
							!regex.test($scope.editMLCfirNo)) {

							growl.error("Invalid FIR No");
							return false;
						}

						// Police Buckle No
						if ($scope.editMLCpoliceBuckleNo &&
							!regex.test($scope.editMLCpoliceBuckleNo)) {

							growl.error("Invalid Police Buckle No");
							return false;
						}

						// Police Station Name
						if ($scope.editMLCpoliceStationName &&
							!regex.test($scope.editMLCpoliceStationName)) {

							growl.error("Invalid Police Station Name");
							return false;
						}

						// Remark
						if ($scope.editMLCremark &&
							!regex.test($scope.editMLCremark)) {

							growl.error("Invalid Remark");
							return false;
						}

						// Contact No
						if ($scope.editMLCmlcContactNo &&
							!regex.test($scope.editMLCmlcContactNo)) {

							growl.error("Invalid Contact Number");
							return false;
						}

						return true;
					};

			$scope.updateMedicolegalDetails = function() {
								
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');


								 if (!$scope.validateMLCFields()) {
									return;
								}		
							
								var data = {

									accompaniedBy : $scope.editMLCaccompaniedName,
									firNo        :$scope.editMLCfirNo,
									policeBuckleNo :$scope.editMLCpoliceBuckleNo,
									policeStationName:$scope.editMLCpoliceStationName,
									mlcContactNo:$scope.editMLCmlcContactNo,
									mlcRelationId:$scope.editMLCmlcRelationId,
									// mlcRelationId:($scope.editMLCmlcRelationId != "0" &&  $scope.editMLCmlcRelationId != null)?  $scope.mlcRelationId : null,
									priorityId : $scope.editMLCpriorityId,
									priorityId : ($scope.editMLCpriorityId != "0" &&  $scope.editMLCpriorityId != null)?  $scope.priorityId : null,
									status : 'A',
									updatedBy : $scope.userId,
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									admissionId:$scope.patientDetails.admissionId,
									updatedDate:$scope.currentDate,
									medicoLegalId: $scope.editMLCmedicoLegalId,
									remark:$scope.editMLCremark

								}
								
								var URI = BASE_URL + ROOT_URL
										+"/adt/updateMedicolegalDetails";
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													$rootScope.stopLoader();
													if (response.data.status == "success") {
														growl.success(
																		'Updated Successfully...!!',
																		{
																			title : 'Success!'
																		});
																		 angular.element('#mlcEditPopup').modal('hide');	
														// $scope.getMedicolegalDetails();
														
													} 

												angular.element('#mlcEditPopup').modal('hide');	
												$scope.masterList()

														
													$rootScope.stopLoader();
												});
							
						

					}
							
							
						 }]);
						