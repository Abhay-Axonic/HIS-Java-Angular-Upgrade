
/**
 * @author Ganesh
 */

angular
		.module('myApp')
		.controller(
				"restainRequestWorklistDetailsController",
				[
						'$scope',
						'$rootScope',
						'$state',
						'$cookies',
						'GenericService',
						'PagerService',
						'promiseFactoryWithObject',
						'$sessionStorage',
						'growl',
						'$stateParams',
						function($scope, $rootScope, $state, $cookies,
								GenericService, PagerService, promiseFactoryWithObject,
								$sessionStorage, growl,$stateParams) {

							$rootScope.loginpage = true;
							$scope.LISDynamicLabel = "Block Creation Controller";

							/** Variables for pagination */
							$scope.offset = 0;
							$scope.noOfRecordsPerPage = 10;
							$scope.restainRequestDetailsList = [];
							$scope.totalNoOfRecords;
							$scope.searchKeyword;
							$scope.commonListCount;

							
							
							/*** Check user is Loggedin */
							var cookieObject,cookieObjectAppConfig;
							[cookieObject,cookieObjectAppConfig] = $scope.checkCookies();
							$scope.orgUnitId = cookieObject.unitId;
							$scope.orgId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							
							/*** Lab Configuration */
							var labConfig  =    $scope.checkLabConfig();
							$scope.deptId = labConfig.labSpecialityId;
						    $scope.subDeptId = labConfig.histopathologyId;
							
							//$scope.visityTypeList = [];
							//$scope.specimanTypeList =[];
							//$scope.testList = [];
							
							//$scope.selectedVisitType = [];
							//$scope.selectedspecimanTypeList = [];
							//$scope.selectedtestList = [];
							/**DatePicker Code  */
							
							$scope.fromDate = moment().startOf('day').toDate();
						    $scope.toDate = moment().startOf('day').toDate();

							$scope.dateOptions =
							{
								formatYear : 'yyyy',
								showWeeks : false,
								maxDate : new Date(),
							};

							$scope.open = function() {
								$scope.popup.opened = true;
							};

							$scope.popup =
							{
								opened : false
							};
							
							$scope.popup1 =
							{
								opened : false
							};
							
							$scope.open1 = function() 
							{
								$scope.popup1.opened = true;
							};

							   
							    
							   /* $scope.selectedItems = [];
							    $scope.dropDownSetting = {
							    		scrollableHeight: '100px', 
							    		scrollable: true
							    };  */
							$scope.getRestainWorkListDetails = function() {
								try 
								{
									$scope.tRestainRequestMaster = $stateParams.restainReqMstObj;
									$scope.subSpecimenCyto =  $stateParams.restainReqMstObj.isHistoCyto=='C'?$scope.tRestainRequestMaster.specimanName:'';
									$rootScope.startLoader();
									var data ={}
									var URI = BASE_URL 
											+ ROOT_URL
											+ LIS_HISTOPATHOLOGY
											+ HISTO_RESTAIN_REQUEST_WORKLIST_DETAILS
											+S+$scope.tRestainRequestMaster.tRestainingReqId
											+S+$scope.orgId
											+S+$scope.orgUnitId;
									console.log("URI", URI);
									
									GenericService
											.serviceAction(METHOD_GET, URI, data)
											.then(
													function(response)
													{
														if (response.data.status == 'success') 
														{
															$rootScope.stopLoader();
															$scope.restainRequestDetailsList = response.data.listObject;
															console.log("$scope.restainRequestDetailsList",$scope.restainRequestDetailsList);
														} else if (response.data.status == 'error') 
														{
															$rootScope.stopLoader();
															growl.error("Something went Wrong",	{title : 'Error!'});
														}
													
													});
								} catch (e) {
									console.log(e.message)
								}
							}
							
							$scope.tRestainRequestMaster =  {
									  "tRestainingReqId" : "",
									  "orgId" : $scope.orgId,
									  "orgUnitId" : $scope.orgUnitId,
									  "labSampleDtlsId" : "",
									  "tSubSpecimanId" : "",
								      "createdBy" : $scope.userId,
									  "createdDate" : "",
									  "updatedBy" : "",
									  "updatedDate" : "",
									  "isDeleted" : "N",
									  "histopathlogyNumber" : "",
									  "subSpcimanNo" : "",
									  "subSpecimanBarcode" : "",
									  "specimanId" : "",
									  "specimanName" : "",
									  "uhid" : "",
									  "genderName" : "",
									  "patientDetails" : "",
									  "doctorDetails" : "",
									  "dob" : "",
									  "age" : 0,
									  "visitType" : "",
									  "specimanType" : "",
									  "specimanTypeId" : "",
									  "wardCode":"",
									  "bedNumber":"",
									  "isHistoCyto":"",
									  "listTRestainingRequestDetailsMaster" : []
									}
							
							 $scope.tRestainRequestDetailsMaster = 
							  { 
								  "tRestainingDetailId" : "",
								  "tRestainingSubDetailId":"",
								  "orgId" : $scope.orgId,
								  "orgUnitId" : $scope.orgUnitId,
								  "tRestainingReqId" : "",
								  "labSampleDtlsId" : "",
								  "tGrossId" : "",
								  "tBlockId" : "",
								  "tSlideId" : "",
								  "blockNo" : "",
								  "grossNo" : "",
							      "createdBy" : "1",
								  "createdDate" : "",
								  "updatedBy" : "",
								  "updatedDate" : "",
								  "isDeleted" : "N",
								  "subSpecimanName" : "",
								  "subSpcimanNo" : "",
								  "listSlidesNo" : "",
								  "againstSlide" : "",
								  "noOfSlides" : "",
								  "stainingName" : "",
								  "remark" : "",
								  "listTRestainingReqSubDetailsMaster" : []
								  }
						
							 if(angular.equals({},$stateParams.restainReqMstObj))
								{
								  $state.go('restainingRequestWorklist');
								}
							 else{
								 
									
								 $scope.tRestainRequestMaster = $stateParams.restainReqMstObj;
								 $scope.getRestainWorkListDetails();
							 }
							
							
							
							
							console.log("$scope.tRestainRequestMaster",$scope.tRestainRequestMaster);
							/* $scope.patientDetails = {
									 "uhIdNumber":$stateParams.restainReqMstObj.uhid,
									 "patientName":$stateParams.restainReqMstObj.patientDetails,
									 "genderCode":$stateParams.restainReqMstObj.genderName,
									 "age":$stateParams.restainReqMstObj.age,
									 "birthDate":moment($stateParams.restainReqMstObj.dob).format('DD-MM-YYYY'),
									 "doctorDetails":$stateParams.restainReqMstObj.patientDetails
							   }*/
							 $scope.docName = "";
								if($stateParams.restainReqMstObj.doctorDetails!=undefined && $stateParams.restainReqMstObj.doctorDetails!=null){
									$scope.docName = $stateParams.restainReqMstObj.doctorDetails.split(".");
									
								}else{
									$scope.docName = "-"
								}
							$scope.patientDetails = {
									"patientName" : $stateParams.restainReqMstObj.patientDetails,
									"genderCode" : $stateParams.restainReqMstObj.genderCode,
									"age" : $stateParams.restainReqMstObj.age,
									"uhIdNumber" : $stateParams.restainReqMstObj.uhid,
									"birthDate" : moment($stateParams.restainReqMstObj.dob).format('DD-MM-YYYY'),
									"doctorFullName" : $stateParams.restainReqMstObj.doctorDetails==null?"-":$scope.docName[1],
									"dateOfAdmission" : $stateParams.restainReqMstObj.doa,
									"patientDischargeDate" : $stateParams.restainReqMstObj.pdd,
									"wardName" : "",
									"bedNumber" : $stateParams.restainReqMstObj.bedNumber,
									"imagePath":$stateParams.restainReqMstObj.imagePath,
									"patientId":"",
									"visitTypeId":$stateParams.restainReqMstObj.visitTypeId,
									"admissionId":"",
									"genderId":$stateParams.restainReqMstObj.genderId,
									 "doctorDetails": $stateParams.restainReqMstObj.doctorDetails==null?"-":$stateParams.restainReqMstObj.doctorDetails,
									"los": ""		 
								}
							 if($stateParams.restainReqMstObj.visitTypeId!=1 && $stateParams.restainReqMstObj.doa!=null && $stateParams.restainReqMstObj.pdd!=null){
								var dateDoa = moment($stateParams.restainReqMstObj.doa, 'DD/MM/YYYY');
								var datePdd = moment($stateParams.restainReqMstObj.pdd, 'DD/MM/YYYY');
							
								$scope.patientDetails.los = $rootScope.dateDiffrence(dateDoa,datePdd);
							}
							 
							 $scope.getStainingList = function() {
									try {
										var data = "";
										var URI = BASE_URL + ROOT_URL
										        + LIS_COMMON
												+ GET_STAINING_LIST
												+ $scope.orgId;
										console.log("URI", URI);
										GenericService
												.serviceAction("GET", URI, data)
												.then(
														function(response) 
														{
															$scope.stainingList  = response.data.listObject;
															console.log("$scope.stainingList",JSON.stringify($scope.stainingList));
														});
									} catch (e) {
										console.log(e.message)
									}

								}
								
								$scope.getStainingList();  
							
							/** Service Autocomplete List * *//*
							$scope.getMicroPatientList = function(searchKeyword)
							{
								try 
								{
									var data = {
											"orgId":$scope.orgId,
											"orgUnitId":$scope.orgUnitId,
											"deptId":$scope.deptId,
											"subDeptId":$scope.subDeptId,
											"searchKeyword":searchKeyword,
									}
									
									var URI = BASE_URL 
									        + ROOT_URL
											+ LIS_HISTOPATHOLOGY
											+ HISTO_MICRO_PATIENT_SEARCH
									console.log("URI", URI);
									return GenericService.serviceAction("POST", URI,
											data).then(function(response) {
										console.log("response.data.listObject",response.data.listObject);
										return response.data.listObject;
									});
								} catch (e) {
									console.log(e.message)
								}
							}	
							
							$scope.getVisitTypeList = function()
							{
								try 
								{
									var data = {
											"orgId":$scope.orgId,
											"orgUnitId":$scope.orgUnitId,
											"deptId":$scope.deptId,
											"subDeptId":$scope.subDeptId,
											"searchKeyword":"",
									}
									
									var URI = BASE_URL 
									        + ROOT_URL
											+ LIS_HISTOPATHOLOGY
											+ HISTO_MICRO_VISIT_LIST
									console.log("URI", URI);
									 GenericService.serviceAction("POST", URI,
											data).then(function(response) {
										console.log("response.data.listObject",response.data.listObject);
										$scope.visityTypeList =  response.data.listObject;
									});
								} catch (e) {
									console.log(e.message)
								}
							}
							
							$scope.getVisitTypeList();
							
							$scope.getSpecimanList = function()
							{
								try 
								{
									var data = {
											"orgId":$scope.orgId,
											"orgUnitId":$scope.orgUnitId,
											"deptId":$scope.deptId,
											"subDeptId":$scope.subDeptId,
											"searchKeyword":"",
									}
									
									var URI = BASE_URL 
									        + ROOT_URL
											+ LIS_HISTOPATHOLOGY
											+ HISTO_MICRO_SPECIMAN_LIST
									console.log("URI", URI);
									 GenericService.serviceAction("POST", URI,
											data).then(function(response) 
									{
										console.log("response.data.listObject",response.data.listObject);
									  $scope.specimanTypeList =  response.data.listObject;
									});
								} catch (e) {
									console.log(e.message)
								}
							}
							$scope.getSpecimanList();
							
							$scope.getTestList = function()
							{
								try 
								{
									var data = {
											"orgId":$scope.orgId,
											"orgUnitId":$scope.orgUnitId,
											"deptId":$scope.deptId,
											"subDeptId":$scope.subDeptId,
											"searchKeyword":"",
									}
									
									var URI = BASE_URL 
									        + ROOT_URL
											+ LIS_HISTOPATHOLOGY
											+ HISTO_MICRO_TEST_LIST
									console.log("URI", URI);
									 GenericService.serviceAction("POST", URI,
											data).then(function(response) {
										console.log("response.data.listObject",response.data.listObject);
										$scope.testList =  response.data.listObject;
									});
								} catch (e) {
									console.log(e.message)
								}
							}
							$scope.getTestList();
							
							$scope.selectPatient = function($item, $model,$label)
									{
								      $scope.selectedPatient = $item.id;
							        }
							
                            $scope.searchWorkList = function()
							{
								try 
								{
									var searchObj = {
											"orgId":$scope.orgId,
											"orgUnitId":$scope.orgUnitId,
		                            		"fromDate": moment($scope.fromDate.getTime()).format('YYYY-MM-DD') ,
		                            		"toDate":  moment($scope.toDate.getTime()).format('YYYY-MM-DD'),
		                            		"patientId": $scope.selectedPatient,
		                            		"visitTypes":$scope.selectedVisitType,
		                            		"specimanTypes":$scope.selectedspecimanTypeList,
		                            		"testTypes":$scope.selectedtestList,
		                             }
									console.log("microscopicExaminationList",JSON.stringify(searchObj));
									var URI = BASE_URL 
									        + ROOT_URL
											+ LIS_HISTOPATHOLOGY
											+ HISTO_MICRO_SEARCH_MICRO_ORGANISM_LIST
									console.log("URI", URI);
									 GenericService.serviceAction("POST", URI,
											 searchObj).then(function(response) {
										console.log("microscopicExaminationList",JSON.stringify(response.data.listObject));
										$scope.microscopicExaminationList =  response.data.listObject;
									});
								} catch (e) {
									console.log(e.message)
								}
							}*/
							
							    
							

						/*	 //** code for setting all checkboxes *//*
							$scope.checkAll = function() {
								if ($scope.selectedAll) {
									$scope.selectedAll = true;
								} else {
									$scope.selectedAll = false;
								}
								angular
										.forEach(
												$scope.microscopicExaList,
												function(specimanObj) {
													specimanObj.isAccpted = $scope.selectedAll
												});
							 }
							*/
							
							
							
								
							
							$scope.getMaxSlideNumber = function(tBlockId,blockNo,isHistoCyto,histopathlogyNumber)
							{
							try{
								var data ={
										
										"tRestainingDetailId":"",
										"tRestainingSubDetailId":"",
										"tSubSpecimanId":isHistoCyto=='H'?'':$scope.tRestainRequestMaster.tSubSpecimanId,
										"isNew":"",
										"orgId":$scope.orgId,
										"orgUnitId":$scope.orgUnitId,
										"tBlockId":tBlockId,
									    "isHistoCyto":isHistoCyto,
								};
								var URI = BASE_URL 
										+ ROOT_URL
										+ LIS_HISTOPATHOLOGY
										+ GET_MAX_SLIDE_NO
								console.log("URI", URI);
								
								var slideObj = {"slideNum":"","slideNo":""};
								return GenericService
										.serviceAction(METHOD_POST, URI, data)
										.then(
												function(response)
												{
													if (response.data.status == 'success') 
													{
														$rootScope.stopLoader();
														$scope.maxSlide = response.data.object;
														++$scope.maxSlide
														
														if(isHistoCyto=='H')
															{
															slideNo  = voucher_codes.generate({
							                    			    prefix:blockNo+SLIDE_PREFIX,
							                    			    inputNumber: $scope.maxSlide,
							                    			    length:NUMBER_WIDTH})[0];
														
															}
														else if(isHistoCyto=='C')
															
														 	{
															slideNo  = voucher_codes.generate({
							                    			    prefix:histopathlogyNumber+SLIDE_PREFIX,
							                    			    inputNumber: $scope.maxSlide,
							                    			    length:NUMBER_WIDTH})[0];
															
															}
														console.log("slideNo",slideNo); 
														
														slideObj.slideNum = $scope.maxSlide;
														slideObj.slideNo = slideNo
														return slideObj;
														
													} else if (response.data.status == 'error') 
													{
														growl.error("Something went Wrong",	{title : 'Error!'});
													}
													 return slideObj;
												});
							} catch (e)
							 {
								console.log(e.message)
							 }
							}
							
							$scope.getSlideCreationDetails = function(restainReqDtlsObj)
							{
							try{
							
								$scope.histopathlogyNumber = $stateParams.restainReqMstObj.histopathlogyNumber;
								$scope.subSpecimanName = $stateParams.restainReqMstObj.specimanName;
								$scope.isNew = restainReqDtlsObj.isNew; //For Checking is new Slide or Not
							
							var data ={
										
										"tRestainingDetailId":restainReqDtlsObj.tRestainingDetailId,
										"tRestainingSubDetailId":restainReqDtlsObj.tRestainingSubDetailId,
										"tSubSpecimanId":$stateParams.restainReqMstObj.tSubSpecimanId,
										"isNew":restainReqDtlsObj.isNew,
										"orgId":$scope.orgId,
										"orgUnitId":$scope.orgUnitId,
										"tBlockId":0,
									    "isHistoCyto":$stateParams.restainReqMstObj.isHistoCyto,
								};
								$rootScope.startLoader();
								var URI = BASE_URL 
										+ ROOT_URL
										+ LIS_HISTOPATHOLOGY
										+ HISTO_RESTAIN_REQUEST_WORKLIST_SLIDES

										console.log("URI", URI);
								
								GenericService
										.serviceAction(METHOD_POST, URI, data)
										.then(
												function(response)
												{
													if (response.data.status == 'success') 
													{
														if($stateParams.restainReqMstObj.isHistoCyto=='H')
															{
															$scope.slideCreationList = response.data.listObject;
														    if($scope.slideCreationList[0].listTBlockMaster[0].listTSlideMaster[0].slideNo == null)
														    	{
														    	var promise = 
														            $scope.getMaxSlideNumber($scope.slideCreationList[0].listTBlockMaster[0].tBlockId,$scope.slideCreationList[0].listTBlockMaster[0].blockNo,'H',0);
														        promise.then(
														           function(slideObj) { 
																    	 $scope.slideCreationList[0].listTBlockMaster[0].listTSlideMaster[0].slideNo = slideObj.slideNo;
																    	 $scope.slideCreationList[0].listTBlockMaster[0].listTSlideMaster[0].slideNum = slideObj.slideNum;
																    	 $scope.slideCreationList[0].listTBlockMaster[0].listTSlideMaster[0].tSubSpecimanId = $scope.tRestainRequestMaster.tSubSpecimanId;
																    	 $('#restainSlides').modal('show');
																    		$rootScope.stopLoader();
														           },
														           function(slideObj) {
														               console.log('Faild to Load Slide', slideNo);
														           	$rootScope.stopLoader();
														           });
														    	}
														    else{
														    	 $('#restainSlides').modal('show');	
														    	$rootScope.stopLoader();
														      }
															}
														else if($stateParams.restainReqMstObj.isHistoCyto=='C')
															{
															$scope.slideList = response.data.listObject;
														    if($scope.slideList[0].slideNo == null)
														    	{
														    	var promise = 
														            $scope.getMaxSlideNumber(0,0,'C',$scope.histopathlogyNumber);
														        promise.then(
														           function(slideObj) 
														           { 
																    	 $scope.slideList[0].slideNo = slideObj.slideNo;
																    	 $scope.slideList[0].slideNum = slideObj.slideNum;
																    	 $scope.slideList[0].tSubSpecimanId = $scope.tRestainRequestMaster.tSubSpecimanId;
																    	 $('#restainSlides').modal('show');
																    		$rootScope.stopLoader();
														           },
														           function(slideObj) {
														               console.log('Faild to Load Slide', slideNo);
														           	$rootScope.stopLoader();
														           });
														    	} else{
														    		 $('#restainSlides').modal('show');
															    	$rootScope.stopLoader();
															      }
															}
													
														console.log("$scope.slideList",$scope.slideList);
														console.log("$scope.slideCreationList",$scope.slideCreationList);
													} else if (response.data.status == 'error') 
													{
														$rootScope.stopLoader();
														$('#restainSlides').modal('hide');
														growl.error("Something went Wrong",	{title : 'Error!'});
													}
												
												});
							} catch (e)
							 {
								console.log(e.message)
							 }
							}
							
							
							$scope.createRestainedSlide = function() 
							{  
								$("#restainSlides").modal('hide');
						        $scope.slideCreationList[0].listTRestainingRequestDetailsMasterDto = [];
								angular.forEach($scope.slideCreationList[0].listTBlockMaster[0].listTSlideMaster, function(slideObj, key)
										{
									    	      slideObj.listTMicroExaDetails =[];
									    	      slideObj.listTRestainingReqSubDetailsMaster =[];
									    	      slideObj.orgId = $scope.orgId;
									    	      slideObj.labSampleDtlsId = $scope.tRestainRequestMaster.labSampleDtlsId;
									    	      slideObj.orgUnitId = $scope.orgUnitId;
									    	      slideObj.createdBy = $scope.userId;
									    	      slideObj.isRestained = $scope.isNew=='Y'?'N':'Y';
										});
								$scope.$broadcast('show-errors-check-validity');
								$rootScope.startLoader();
								try {
									var URI = BASE_URL 
											+ ROOT_URL
											+ LIS_HISTOPATHOLOGY
											+ HISTO_CREATE_SLIDE;
									console.log("new SLide Creation", URI);
									console.log("$scope.slideList",JSON.stringify($scope.slideCreationList));
									GenericService
											.serviceAction(METHOD_POST, URI,
													$scope.slideCreationList)
											.then(
													function(response) {
														if (response.data.status == 'success') 
														{
															$scope.getRestainWorkListDetails();
															 growl.success(response.data.message,
																{
																	title : 'Success!'
																});
															$scope.$broadcast('show-errors-reset');

														} else if (response.data.status == 'error') 
														{
															$rootScope.stopLoader();
															growl.error(response.data.message,
															{
																title : 'Error!'
															});
														}
													});
								} catch (e) {
									console.log("Exception", e.message);
								}
						}
							
							$scope.createRestainedSlideCyto = function() 
							{
								$("#restainSlides").modal('hide');
								angular.forEach($scope.slideList, function(slideObj, key)
										{
									    	      slideObj.listTMicroExaDetails =[];
									    	      slideObj.listTRestainingReqSubDetailsMaster =[];
									    	      slideObj.labSampleDtlsId = $scope.tRestainRequestMaster.labSampleDtlsId;
									    	      slideObj.orgId = $scope.orgId;
									    	      slideObj.orgUnitId = $scope.orgUnitId;
									    	      slideObj.createdBy = $scope.userId;
									    	      slideObj.isRestained = $scope.isNew=='Y'?'N':'Y';
										});
								$scope.$broadcast('show-errors-check-validity');
								$rootScope.startLoader();
								try {
									var URI = BASE_URL 
											+ ROOT_URL
											+ LIS_HISTOPATHOLOGY
											+ CYTO_CREATE_SLIDE;
									console.log("new SLide Creation", URI);
									console.log("$scope.listGross",JSON.stringify($scope.slideList));
									GenericService
											.serviceAction(METHOD_POST, URI,
													$scope.slideList)
											.then(
													function(response) {
														if (response.data.status == 'success') 
														{
															$scope.getRestainWorkListDetails();
															 growl.success(response.data.message,
																{
																	title : 'Success!'
																});
															$scope.$broadcast('show-errors-reset');

														} else if (response.data.status == 'error') 
														{
															$rootScope.stopLoader();
															growl.error(response.data.message,
															{
																title : 'Error!'
															});
														}
													});
								} catch (e) {
									console.log("Exception", e.message);
								}
						}	
							$scope.blockCaptureNoteReq = "Capture note is required.";	

						} ]);