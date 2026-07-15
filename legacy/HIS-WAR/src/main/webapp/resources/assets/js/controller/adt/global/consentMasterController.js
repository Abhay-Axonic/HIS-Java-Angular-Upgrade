'use strict';


/**
 * @Author By  Vivek Satle
 * @name myApp.controller:consentMasterController
 * @description # consentMasterController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'consentMasterController',
				[
						'$scope',
						'$http',
						'$localStorage',
						'$sessionStorage',
						'$cookies',
						'$rootScope',
						'$timeout',
						'GenericService',
						'GenericServiceFileUpload',
						'growl',
						'$state',
						'PagerService',
						'$window',
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, $timeout, GenericService, GenericServiceFileUpload,growl,
								$state,PagerService,$window) {

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
								$scope.createdBy =cookieObject.userId;
								$scope.updatedBy =cookieObject.userId;
								$scope.triggerId = 42;
								$scope.dataList = [];
								$scope.requiredData = "";
								/*
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;*/
								
								/**Variables for pagination */
								$scope.offset = 0;
								$scope.noOfRecordsPerPage = 10;
								$scope.totalNoOfRecords;
								$scope.searchKeyword;
								$scope.commonListCount;

								$rootScope.loginpage = true;
								$scope.consent = {}
								$scope.saveBtnFlag = false;
								$scope.updateBtnFlag = true;

								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								$scope.savedoc = false;
								// end for popup
								
								$scope.consent.file="";
								$scope.consent.template="";
								$scope.consent.templateType="";
								$scope.consent.fileName = "";
								$scope.DocDetailsList =[]; 
								var data = {};
								// $scope.status = "NO";
								/*var URI = BASE_URL + ROOT_URL + GETCONSENTLIST;
								GenericService
										.serviceAction("GET", URI, data)
										.then(
												function(response) {
													$scope.consentList = [];

													if (response.data.status == "success") {
														$scope.consentList = response.data.listObject;
														console
																.log($scope.consentList);

													}
												});*/
								var URI = BASE_URL + ROOT_URL
										+ GETACTIVECONSENTTYPELIST+"/"+$scope.organizationId+"/"+$scope.unitId;
								GenericService
										.serviceAction("GET", URI, data)
										.then(
												function(response) {
													$scope.consentTypeList = [];

													if (response.data.status == "success") {
														$scope.consentTypeList = response.data.listObject;
														console
																.log($scope.consentTypeList);

													}
												});

							}

							$scope.init();
							
							//For Paginations Start
							$scope.setNoOfRecords = function() {
								$scope.initConsentMasterList($scope.organizationId,$scope.unitId, $scope.offset, $scope.noOfRecordsPerPage);
							};
							$scope.initConsentMasterList = function(orgId,unitId, offset, noOfRecordsPerPage)
							{
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
								var URI = BASE_URL + ROOT_URL + GETCONSENTLIST;
								var URI1 = BASE_URL + ROOT_URL
										+ COUNTCONSENTMASTER;
								
								var data = {
										organizationId:orgId,
										unitId : unitId,
										offset : offset,
										noOfRecordsPerPage : noOfRecordsPerPage
								}
								
								var data1 = {
										organizationId:orgId,
										unitId : unitId
								}
								
								//For Get Consent List Using Offset
								GenericService
								.serviceAction("POST", URI, data)
								.then(
										function(response) {
											$scope.consentList = [];

											if (response.data.status == "success") {
												$scope.consentList = response.data.listObject;

											}
										});
								
								//For Count Consent List
								GenericService
								.serviceAction("POST", URI1, data1)
								.then(
										function(response) {
											if (response.data.status == "success") {
												console.log(response.data);
												$scope.commonListCount = response.data.object;
												$scope.setPage(1, false);

											}
										});
							}

							$scope.getConsentMasterList = function(orgId,unitId, offset, noOfRecordsPerPage) {
								console.log("offset", offset);
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
								var URI = BASE_URL + ROOT_URL + GETCONSENTLIST;
								var data = {
										organizationId:$scope.organizationId,
										unitId:$scope.unitId,
										offset : offset,
										noOfRecordsPerPage : noOfRecordsPerPage
								}
								
								console.log("URI", URI);
								//For Get Consent List Using Offset
								GenericService
								.serviceAction("POST", URI, data)
								.then(
										function(response) {
											$scope.consentList = [];

											if (response.data.status == "success") {
												$scope.consentList = response.data.listObject;

											}
										});
							}

							$scope.pager = {};
							$scope.page;

							$scope.setPage = function(page, flag) {
								if (page < 1 || page > $scope.pager.totalPages)
								{
									return;
								}
								$scope.pager = PagerService.GetPager($scope.commonListCount, page, $scope.noOfRecordsPerPage);
								if (flag)
								{
									$scope.getConsentMasterList($scope.organizationId,$scope.unitId, $scope.pager.startIndex, $scope.pager.pageSize);
								}
							}

							$scope.initConsentMasterList($scope.organizationId,$scope.unitId, $scope.offset, $scope.noOfRecordsPerPage);
							
							//For Paginations End
							

							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.saveConsent();
							}
							
							$scope.getReqiredData = function(triggerId) {

								var URI = BASE_URL + ROOT_URL+ "/requiredData/trigger/"+$scope.triggerId;
								var data = { };
								GenericService.serviceAction("GET", URI, data).then(function(response) {
											if (response.data.status == "success") {
												$rootScope.stopLoader();
												$scope.dataList = response.data.listObject;
												console.log("Datalist",$scope.dataList);
											}
										});
							}
							
							$scope.getReqiredData();
							
							$scope.uploadConsentDoc = function(consentMasterId,flag) {
								
								var fileName = "";
								if($scope.consent.fileName !=undefined && $scope.consent.fileName!=null && $scope.consent.fileName!=""){
									fileName = $scope.consent.fileName;
								}else{
									fileName = $scope.consent.file.name;
								}
								 var fd = new FormData();
							        fd.append('consentMasterId', consentMasterId);
							        fd.append('documentName', fileName);
							        fd.append('organizationId', $scope.organizationId);
							        fd.append('unitId',$scope.unitId);
							        fd.append('file',$scope.consent.file);
							        var URI1 = BASE_URL + ROOT_URL + "/adt/uploadConsentDoc";
							        GenericServiceFileUpload.serviceAction("POST",URI1,fd,{ 'Content-Type': undefined}).then(function(response) 
							        		{	
									        	if (response.data.status == "success") {
													if(flag ==0){
									        		growl
													.success(
															'Consent save Successfully...!!',
															{
																title : 'Success!'
															});
													}else{
														growl.success('Consent Updated Successfully...!!',
																{
																	title : 'Success!'
																});
													}
                                                    $scope.initConsentMasterList($scope.organizationId,$scope.unitId, $scope.offset, $scope.noOfRecordsPerPage);
									        		 $('#fileId').val(undefined);
												} else {
														growl.warning('Consent already exits...!!',
																	{
																		title : 'Warning!'
																	});
												}
											});
								
							}
							
							
							
							//Function for save consent
							$scope.saveConsent = function() {
							
								console.log("$scope.consent.templateType",$scope.consent.templateType);
								if(	$scope.consent.templateType == ""){
								
									growl.warning('Please Select Atleast One template...!!',
											{
												title : 'Warning!'
											});
								}
								else
								{
									$scope.currentDate = new Date();
									$scope.currentDate = moment($scope.currentDate)
											.format('DD-MM-YYYY HH:mm:ss');
	
									$scope.$broadcast('show-errors-check-validity');
	
									var data = {
										consentMasterCode : $scope.consent.consentCode,
										consentMasterName : $scope.consent.consentName,
										consentTypeMasterId : $scope.consent.consentTypeId,
										status : 'A',
										createdBy : $scope.userId,
										updatedBy : $scope.userId,
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
										createdDate : $scope.currentDate,
										updatedDate : $scope.currentDate,
										templateType : $scope.consent.templateType,
										template : $scope.consent.template
									}
									if ($scope.consentform.$valid) {
										if ($scope.popUpFlag) {
											$scope.showModal = !$scope.showModal;
	
										} else {
	
											console.log(data);
	
											var URI = BASE_URL + ROOT_URL+ ADDCONSENT;
											GenericService.serviceAction("POST", URI,data).then(
															function(response) {
	
																if (response.data.status == "success") {
																	$scope.DocDetailsList =[]; 
																	if(	$scope.consent.templateType == 2 && $scope.consent.file!=undefined && $scope.consent.file!=null && $scope.consent.file!=""){
																		$scope.uploadConsentDoc(response.data.object.consentMasterId,0);
																	}else{
																		
																		growl.success(
																				'Consent save Successfully...!!',
																				{
																					title : 'Success!'
																				});
																	}
																	$scope.init();
																} else {
																		growl.warning('Consent already exits...!!',
																					{
																						title : 'Warning!'
																					});
																}
																$scope.initConsentMasterList($scope.organizationId,$scope.unitId, $scope.offset, $scope.noOfRecordsPerPage);
	
															});
										}
									}
								}

							}
							
							$scope.remove = function(){
								$scope.DocDetailsList =[]; 
							}
							
							 $scope.templateConfigObject = {};
							 $scope.getTemplateConfig = function(){
								$rootScope.startLoader();
								var obj = {};
								obj.unitId = $scope.unitId;
								var URI = BASE_URL + ROOT_URL + "/api/global/templateConfig";
								GenericService.serviceAction(METHOD_POST,URI,obj).then(function(response) {
									if (response.data.status == "success"){
										 $scope.templateConfigObject = response.data.object;
										 $rootScope.stopLoader();
									}
									else if(response.data.status == "error")
									{
										 $rootScope.stopLoader();	
									}
								});
							}
							
							 $scope.getTemplateConfig();
							
							//Function for get single consent
							$scope.showUpdateBtn = function(id) {

								$scope.saveBtnFlag = true;
								$scope.updateBtnFlag = false;
								$scope.DocDetailsList = [];
								var data = {

									consentMasterId : id
								}
								console.log(data);
								var URI = BASE_URL + ROOT_URL
										+ GETSINGLECONSENT;
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													// $scope.speciality =[];
													if (response.data.status == "success") {

														$scope.consent.consentId = response.data.listObject[0].consentMasterId;
														$scope.consent.consentCode = response.data.listObject[0].consentMasterCode;
														$scope.consent.consentName = response.data.listObject[0].consentMasterName;
														$scope.consent.consentTypeId = response.data.listObject[0].consentTypeMasterId
																.toString();
														
														$scope.consent.template=response.data.listObject[0].template;
														$scope.consent.templateType=response.data.listObject[0].templateType;
														$scope.consent.fileName = response.data.listObject[0].documentName;
														$scope.consent.filePath = response.data.listObject[0].filePath;
														if($scope.consent.fileName!= undefined && $scope.consent.fileName!=null && $scope.consent.fileName!=""){
														var DocDetails={}
														
														DocDetails.fileName = 	$scope.consent.fileName;
														$scope.DocDetailsList.push(DocDetails);
														$scope.consent.fileName = "";
														}
														$scope.savedoc = true;

													} else {
														alert("Error!!");
													}
												});

							};
							
							//Function for get single consent
							$scope.updateStatus = function(id, type) {

								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');

								type = (type == "A") ? "A" : "I";

								var data = {
									consentMasterId : id,
									status : type,
									updatedBy : $scope.userId,
									updatedDate : $scope.currentDate
								}
								console.log(data);
								var URI = BASE_URL + ROOT_URL
										+ UPDATECONSENTSTATUS;
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {

													if (response.data.status == "success") {
														growl
																.success(
																		'Status updated Successfully...!!',
																		{
																			title : 'Success!'
																		});
													} else {
														growl
																.warning(
																		'Something went wrong...!!',
																		{
																			title : 'Warning!'
																		});
													}
												});
							}
							
							//Function for update consent
							$scope.updateConsent = function() {
								console.log("$scope.consent",$scope.consent);
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate).format('DD-MM-YYYY HH:mm:ss');
								
								$scope.$broadcast('show-errors-check-validity');
								var data = {
									consentMasterCode : $scope.consent.consentCode,
									consentMasterName : $scope.consent.consentName,
									consentMasterId : $scope.consent.consentId,
									consentTypeMasterId : $scope.consent.consentTypeId,
									updatedBy : $scope.userId,
									updatedDate : $scope.currentDate,
									template : $scope.consent.template,
									templateType : $scope.consent.templateType,
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
								}
								if($scope.DocDetailsList == null || $scope.DocDetailsList.length == 0){
									data.filePath = null;
									data.documentName = null;
								}else{
									data.filePath = $scope.consent.filePath;
									data.documentName = $scope.DocDetailsList[0].documentName;
								}
								if ($scope.consentform.$valid) {
									console.log(data);
									var URI = BASE_URL + ROOT_URL
											+ UPDATECONSENT;
									GenericService
											.serviceAction("POST", URI, data)
											.then(
													function(response) {

														if (response.data.status == "success") {
															
															$scope.DocDetailsList =[]; 
															if(	$scope.consent.templateType == 2 && $scope.consent.file!=undefined && $scope.consent.file!=null && $scope.consent.file!=""){
																$scope.uploadConsentDoc($scope.consent.consentId,1);
															}else{
																
																growl
																.success(
																		'Consent Updated Successfully...!!',
																		{
																			title : 'Success!'
																		});
															}
															$scope.$broadcast('show-errors-reset');
															$scope.init();
														} else {
															growl
																	.warning(
																			'Consent already exits...!!',
																			{
																				title : 'Warning!'
																			});
														}
														$scope.initConsentMasterList($scope.organizationId,$scope.unitId, $scope.offset, $scope.noOfRecordsPerPage);

													});
								}
							}
							
							$scope.openImage = function(index)
                            { 
								
								var imageUrl = $scope.consentList[index].filePath;
								$window.open(imageUrl, '_blank');

								$rootScope.stopLoader();
								/*var href = $rootScope.currentHrefDoccument();
								$.fancybox.open({
									width: 600, // or whatever
									height: 320,
									href:href+imageUrl,
									type: 'iframe'
								});*/
								
								
								/* $timeout( function(){
			                    	  $rootScope.stopLoader();
			                    },5000);*/
                            }
							
							 $scope.previewTemplate = function(){
							 		
								} 
							
							$scope.currentIndex ;
							$scope.viewDocument = function(info,index){
								if(info.templateType==1){
									$scope.currentIndex  = index;
									$("#viewManualTemplateModal").modal("show");
								}else if(info.templateType == 2){
									$rootScope.startLoader();
									$scope.openImage(index);
								}else if(info.templateType == 3){
									$rootScope.startLoader();
									var landingUrl =   $scope.templateConfigObject.templateForm + info.formTemplateId +"&formType=digital";
							 		window.open( landingUrl,  '_blank'  );
								}
							}
							
							$scope.addRequiredData = function(){
								
								$scope.consent.template = $scope.consent.template +" "+$scope.requiredData;
							}
							
							
							
						} ]);
