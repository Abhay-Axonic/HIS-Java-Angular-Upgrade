
/**
 * @author Ganesh
 */

angular
		.module('myApp')
		.controller(
				"slideCreationController",
				[
						'$scope',
						'$rootScope',
						'$state',
						'$cookies',
						'$filter',
						'GenericService',
						'PagerService',
						'promiseFactoryWithObject',
						'$sessionStorage',
						'growl',
						function($scope, $rootScope, $state, $cookies, $filter,
								GenericService, PagerService, promiseFactoryWithObject,
								$sessionStorage, growl) {

							$rootScope.loginpage = true;
							$scope.LISDynamicLabel = "Slide Creation Controller";

							/** Variables for pagination */
							$scope.offset = 0;
							$scope.noOfRecordsPerPage = 50;
							$scope.slideCreationList = [];
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
							
							$scope.visityTypeList = [];
							$scope.specimanTypeList =[];
							$scope.testList = [];
							
							$scope.selectedVisitType = [];
							$scope.selectedspecimanTypeList = [];
							$scope.selectedtestList = [];
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

							 $scope.multiselectObj = {
							    		"id":"",
							    		"label":""
							    }
							    
							    $scope.selectedItems = [];
							    $scope.dropDownSetting = {
							    		scrollableHeight: '100px', 
							    		scrollable: true
							    };  
							    
							
							       $scope.listTSlideMaster = [];
							       $scope.listTGrossMaster = [];
							       $scope.tGrossMaster ={
		                            		  "tGrossId" : "",
		                            		  "orgId" : $scope.orgId,
		                            		  "orgUnitId" : $scope.orgUnitId,
		                            		  "tSubSpecimanId" : "",
		                            		  "labSampleDtlsId" : "",
		                            		  "grossBarcode" : "",
		                            		  "grossNo" : "",
		                            		  "grossDesc" : "",
		                            		  "captureNote" : "",
		                            		  "createdBy" : $scope.userId,
		                            		  "createdDate" : "",
		                            		  "updatedBy" : "",
		                            		  "updatedDate" : "",
		                            		  "isDeleted" : "N",
		                            		  "listTSlideMaster" : $scope.listTSlideMaster,
		                            		  "listTRestainingRequestDetailsMasterDto" :[]
		                            		}
							    
							     
							    
							    $scope.tSlideMaster = {
							    		  "tSlideId" : "",
							    		  "tBlockId":"",
							    		  "orgId" : $scope.orgId,
							    		  "orgUnitId" : $scope.orgUnitId,
							    		  "tSubSpecimanId":"",
							    		  "tGrossId" : "",
							    		  "labSampleDtlsId" : "",
							    		  "stainingId":"",
							    		  "slideBarcode":"",
							    		  "slideNo" : "",
							    		  "slideNum":"",
							    		  "captureNote" : "",
							    		  "createdBy" : $scope.userId,
							    		  "createdDate" : "",
							    		  "updatedBy" : "",
							    		  "updatedDate" : "",
							    		  "isDeleted" : "N",
							    		  "isRestained":"N",
							    		  "restainAgainstTslideid":"",
							    		  "tRestainingSubDetailId":"",
							    		  "listTMicroExaDetails" : []
							    		}
						
							       
							       $scope.findMaxSlideNumber = function(inputList)
	                                {
	                            	  if(Array.isArray(inputList))
	                            		  {
	                            		  if(inputList.length>0)
	                              		    {
	                              		      return Math.max.apply(
	                              		    		   Math,inputList.map(function(o)
	                              		    				   {
	                              		    			         return parseInt(o.slideNum)+1;
	                              		    			         }
	                              		    		        ));
	                              		    }
	                            		  else
	                              	  		{
	                            			  return 1;
	                              	  		}
	                            		  }
	                               }
							

							$scope.addSlide = function(grossIndex,blockIndex,slideIndex)
							{
								
								//$scope.listTGrossMaster[grossIndex].listTBlockMaster[blockIndex].listTSlideMaster =
								//	$scope.listTGrossMaster[grossIndex].listTBlockMaster[blockIndex].listTSlideMaster==null?[]:$scope.listTGrossMaster[grossIndex].listTBlockMaster[blockIndex].listTSlideMaster;
							  if($scope.listTGrossMaster[grossIndex].listTBlockMaster[blockIndex].listTSlideMaster.length==0)
								  {
								   console.log("slideNumber",$scope.slideNumber);
								    $scope.listTSlideMaster = [];
								    var tSlideMaster = angular.copy($scope.tSlideMaster);
								    tSlideMaster.tSubSpecimanId = $scope.tGrossMaster.tSubSpecimanId;
								    tSlideMaster.labSampleDtlsId = $scope.tGrossMaster.labSampleDtlsId;
								    tSlideMaster.slideNum =  $scope.findMaxSlideNumber([]);
								    var slideNo  = voucher_codes.generate({
	                    			    prefix:$scope.listTGrossMaster[grossIndex].listTBlockMaster[blockIndex].blockNo+SLIDE_PREFIX,
	                    			    inputNumber: tSlideMaster.slideNum,
	                    			    length:NUMBER_WIDTH
	                    			})[0];
								    tSlideMaster.slideNo = slideNo;
								    $scope.listTGrossMaster[grossIndex].listTBlockMaster[blockIndex].listTSlideMaster= 
								    	$scope.listTGrossMaster[grossIndex].listTBlockMaster[blockIndex].listTSlideMaster==null?[]:$scope.listTGrossMaster[grossIndex].listTBlockMaster[blockIndex].listTSlideMaster;
								    	$scope.listTGrossMaster[grossIndex].listTBlockMaster[blockIndex].listTSlideMaster.push(tSlideMaster);
								  }
							  else{
								  
								  if($scope.listTGrossMaster[grossIndex].listTBlockMaster[blockIndex].listTSlideMaster[slideIndex].stainingId==""){
	                                    $rootScope.stopLoader();
										growl.warning("Please Select Stain",
												{
													title : 'warning!'
												});
										return false;
	                                }
	                                if($scope.listTGrossMaster[grossIndex].listTBlockMaster[blockIndex].listTSlideMaster[slideIndex].captureNote==""){
	                                		$rootScope.stopLoader();
										growl.warning("Please Enter Capture Note",
												{
													title : 'warning!'
												});
										return false;
	                                }
	                                
								  
								    var tSlideMaster = angular.copy($scope.tSlideMaster);
								    tSlideMaster.tSubSpecimanId = $scope.tGrossMaster.tSubSpecimanId;
								    tSlideMaster.labSampleDtlsId = $scope.tGrossMaster.labSampleDtlsId;
								    tSlideMaster.slideNum =  $scope.findMaxSlideNumber($scope.listTGrossMaster[grossIndex].listTBlockMaster[blockIndex].listTSlideMaster);
								    var slideNo  = voucher_codes.generate({
	                    			    prefix:$scope.listTGrossMaster[grossIndex].listTBlockMaster[blockIndex].blockNo+SLIDE_PREFIX,
	                    			    inputNumber: tSlideMaster.slideNum,
	                    			    length:NUMBER_WIDTH
	                    			})[0];
								    tSlideMaster.slideNo =  slideNo;
								    $scope.listTGrossMaster[grossIndex].listTBlockMaster[blockIndex].listTSlideMaster= 
								    	$scope.listTGrossMaster[grossIndex].listTBlockMaster[blockIndex].listTSlideMaster==null?[]:$scope.listTGrossMaster[grossIndex].listTBlockMaster[blockIndex].listTSlideMaster;
								    	$scope.listTGrossMaster[grossIndex].listTBlockMaster[blockIndex].listTSlideMaster.push(tSlideMaster);
							    }
							  
							  
							}
							       
							   	$scope.removeSlide = function(grossIndex,blockIndex,slideIndex)
								{
							   		$scope.listTGrossMaster[grossIndex].listTBlockMaster[blockIndex].listTSlideMaster.splice(slideIndex,1);
								}
							   	
							    $scope.closePop=function(){
						              $("#subSpecimenName").modal("hide");
						            }		   	
							   	
						   	
							   	
							   	$scope.addSlideCyto = function(slideIndex)
								{
								  if($scope.listTSlideMaster.length==0)
									  {
									   console.log("slideNumber",$scope.slideNumber);
									    $scope.listTSlideMaster = [];
									    var tSlideMaster = angular.copy($scope.tSlideMaster);
									    tSlideMaster.labSampleDtlsId = $scope.tGrossMaster.labSampleDtlsId;
									    tSlideMaster.tSubSpecimanId = $scope.tGrossMaster.tSubSpecimanId;
									    tSlideMaster.slideNum =  $scope.findMaxSlideNumber([]);
									    var slideNo  = voucher_codes.generate({
		                    			    prefix:$scope.histopathlogyNumber+SLIDE_PREFIX,
		                    			    inputNumber: tSlideMaster.slideNum,
		                    			    length:NUMBER_WIDTH
		                    			})[0];
									    tSlideMaster.slideNo = slideNo;
									    	$scope.listTSlideMaster==null?[]:$scope.listTSlideMaster;
									    	$scope.listTSlideMaster.push(tSlideMaster);
									  }
								  else{
									//  if(slideIndex!=0){
									  if($scope.listTSlideMaster[slideIndex].stainingId==""){
		                                    $rootScope.stopLoader();
											growl.warning("Please Select Stain",
													{
														title : 'warning!'
													});
											return false;
		                                }
		                                if($scope.listTSlideMaster[slideIndex].captureNote==""){
		                                		$rootScope.stopLoader();
											growl.warning("Please Enter Capture Note",
													{
														title : 'warning!'
													});
											return false;
		                                }
									  
									//  }
									    var tSlideMaster = angular.copy($scope.tSlideMaster);
									    tSlideMaster.labSampleDtlsId = $scope.tGrossMaster.labSampleDtlsId;
									    tSlideMaster.tSubSpecimanId = $scope.tGrossMaster.tSubSpecimanId;
									    tSlideMaster.slideNum =  $scope.findMaxSlideNumber($scope.listTSlideMaster);
									    var slideNo  = voucher_codes.generate({
		                    			    prefix:$scope.histopathlogyNumber+SLIDE_PREFIX,
		                    			    inputNumber: tSlideMaster.slideNum,
		                    			    length:NUMBER_WIDTH
		                    			})[0];
									    tSlideMaster.slideNo =  slideNo;
									    $scope.listTSlideMaster= 
									    	$scope.listTSlideMaster==null?[]:$scope.listTSlideMaster;
									    	$scope.listTSlideMaster.push(tSlideMaster);
								    }
								  
								  
								}
								       
								   	$scope.removeSlideCyto = function(slideIndex)
									{
								   		$scope.listTSlideMaster.splice(slideIndex,1);
									}
								    
							    
							       
							$scope.setNoOfRecords = function() {
								$scope.initSlideCreationWorklist($scope.orgId,
										$scope.orgUnitId,$scope.deptId,$scope.subDeptId,$scope.offset,
										$scope.noOfRecordsPerPage);
							};

							$scope.initSlideCreationWorklist = function(orgId,
									orgUnitId,deptId,subDeptId, offset, noOfRecordsPerPage) {
								try {
									$rootScope.startLoader();
									var searchObj = {
											  "orgId" : $scope.orgId,
											  "orgUnitId" :$scope.orgUnitId,
											  "fromDate": $scope.fromDate.getTime() ,
			                            	  "toDate":  $scope.toDate.getTime() ,
											  "patientId" : $scope.selectedPatient,
											  "labSampleId" : $scope.labSampleId,
											  "visitTypes" :$scope.selectedVisitType,
											  "testTypes" :$scope.selectedtestList,
											  "deptId" : $scope.deptId,
											  "subDeptId" : $scope.subDeptId,
											  "specimanTypes": $scope.selectedspecimanTypeList,
											  "subDepts" :$scope.selectedDepartMentList,
											  "offset":offset,
											  "recordPerPage":noOfRecordsPerPage,
											  "fromDt": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
											  "toDt":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD")
	                                   }
									
									var isValidDate = $rootScope.isValidDate(searchObj.fromDate,searchObj.toDate);
									if(!isValidDate){
										$rootScope.stopLoader();
										growl.warning("From date can not be greater than to Date ",
												{
													title : 'warning!'
												});
										return false;
									}
									offset = offset != null ? offset : 0;
									noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
											: 50;
									var URI1 = BASE_URL 
									        + ROOT_URL
											+ LIS_HISTOPATHOLOGY
											+ HISTO_SLIDE_CREATION;

									var URI2 = BASE_URL
											+ ROOT_URL
											+ LIS_HISTOPATHOLOGY
											+ HISTO_SLIDE_CREATION_COUNT;

									promiseFactoryWithObject
											.setPromisesWithObject(URI1, URI2, METHOD_POST,
													METHOD_POST,searchObj,searchObj)
											.then(
													function(response) {
														$rootScope.stopLoader();
														$scope.slideCreationList = response[0].listObject;
														angular.forEach($scope.slideCreationList, function(item) {
														    $scope.tSpecimanId = item.tSpecimanId;
														    $scope.labSampleDtlsId = item.labSampleDtlsId;
														});

														$scope.commonListCount = response[1].object;
														$scope.setPage(1, false);
													});
									
								} catch (e) {
									console.log(e.message);
								}
							}
			
							
							 $scope.getSubSpecimanDetailList = function()
							 {
								 var URI = BASE_URL + ROOT_URL + '/LIS/histopathology'+ '/subspecimanbyspecimanId'+'/'+$scope.tSpecimanId;
									GenericService
									.serviceAction("GET", URI, {})
									.then(
											function(response) {
												$scope.getSubSpecimanDetailList = [];
										if (response.data.status == "success") {
											$scope.getSubSpecimanDetails = response.data.listObject;
									}
										
								});
							 }			
							
							$scope.popupIconClicked = function()
							{
								 $scope.getSubSpecimanDetailList();
							}				
					
							
							$scope.clearFields = function()
							{
								$scope.patientId = undefined;
								$scope.selectedPatient = null;
								$scope.selectedVisitType = [];
								$scope.orderStatusId = '';
								$scope.priorityId = '';
								$scope.uhid = '';
								$scope.selectedtestList = [];
								$scope.selectedDepartMentList = [];
								$scope.selectedspecimanTypeList = [];
								$scope.fromDate = moment().toDate();
							    $scope.toDate = moment().toDate();
								$scope.initSlideCreationWorklist($scope.orgId,
										$scope.orgUnitId,$scope.deptId,$scope.subDeptId,$scope.offset,
										$scope.noOfRecordsPerPage);

							};

							$scope.initSlideCreationWorklist($scope.orgId,
									$scope.orgUnitId,$scope.deptId,$scope.subDeptId,$scope.offset,
									$scope.noOfRecordsPerPage);
							
           
							$scope.getSlideCreationWorklist = function(orgId,
									orgUnitId,deptId,subDeptId,offset,noOfRecordsPerPage) {
								try {
									$rootScope.startLoader();
									var data = {
											"orgId":orgId,
											"orgUnitId":orgUnitId,
											"offset":offset,
											"recordPerPage":noOfRecordsPerPage,
											"deptId":$scope.deptId,
											"subDeptId":$scope.subDeptId
										};
									var URI = BASE_URL 
											+ ROOT_URL
											+ LIS_HISTOPATHOLOGY
											+ HISTO_BLOCK_CREATION;
									console.log("URI", URI);
									
									GenericService
											.serviceAction(METHOD_POST, URI, data)
											.then(
													function(response) {
														$rootScope.stopLoader();
														$scope.slideCreationList = response.data.listObject;
													});
								} catch (e) {
									console.log(e.message)
								}

							}

							$scope.pager = {};
							$scope.page;
							$scope.setPage = function(page, flag)
							{
								if (page < 1)
								{
									return;
								}
								$scope.pager = PagerService.GetPager($scope.commonListCount, page,$scope.noOfRecordsPerPage);
								if (flag) 
								{
									$scope.getSlideCreationWorklist($scope.orgId, $scope.orgUnitId,$scope.deptId, $scope.subDeptId,$scope.pager.startIndex,
											$scope.pager.pageSize);
								}
							}

							 //** code for setting all checkboxes *//*
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
							
							/** code for generating url * */
							$scope.generateCreateGrossUrl = function(microScopwrk)
								{
								$scope.tSpecimanMaster = microScopwrk;
											$state.go('macroscopicExamination',
												{
													microScopeObj : $scope.tSpecimanMaster,
												});
								}
							
							
							$scope.getMicroscopicExamWorkDetails = function(slideObj) {
								try 
								{
									$rootScope.startLoader();
									$scope.histopathlogyNumber = slideObj.histopathlogyNumber;
									$scope.subSpecimanName = slideObj.subSpecimanName;
									$scope.tGrossMaster = slideObj;
									$scope.tGrossMaster.orgId = $scope.orgId;
									$scope.tGrossMaster.orgUnitId = $scope.orgUnitId;
									$scope.tGrossMaster.labSampleDtlsId = $scope.labSampleDtlsId;
									$scope.isHistoCyto = slideObj.isHistoCyto;
									if(slideObj.isHistoCyto =='H')
										{
										var URI = BASE_URL 
										+ ROOT_URL
										+ LIS_HISTOPATHOLOGY
										+ HISTO_GET_SLIDE_DETAILS;
								console.log("URI", URI);
								
								GenericService
										.serviceAction(METHOD_POST, URI, $scope.tGrossMaster)
										.then(
												function(response)
												{
													if (response.data.status == 'success') 
													{
														$rootScope.stopLoader();
														$scope.listTGrossMaster = response.data.listObject;
														console.log("$scope.listTGrossMaster",JSON.stringify($scope.listTGrossMaster));
														$("#details").modal('show');
													} else if (response.data.status == 'error') 
													{
														$rootScope.stopLoader();
														growl.error("Something went Wrong",	{title : 'Error!'});
													}
												
												});
										}
									else if(slideObj.isHistoCyto =='C')
										{
										  $rootScope.stopLoader();
										   $scope.listTSlideMaster = [];
										   $scope.addSlideCyto(0);
										   $("#details").modal('show');
										}
									
									
								} catch (e) {
									console.log(e.message)
								}

							}
							
							/*
							 *  Get Stain List 
							 */
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
													function(response) {
														$scope.stainingList  = response.data.listObject;
														console.log("$scope.stainingList",JSON.stringify($scope.stainingList));
													});
								} catch (e) {
									console.log(e.message)
								}

							}
							
							$scope.getStainingList();
							$scope.saveSlideDetailsFlag=false;
							$scope.saveSlideDetails = function() 
							{
							    if($scope.saveSlideDetailsFlag){
							       return false
							    }
							    $scope.saveSlideDetailsFlag=true
								$scope.$broadcast('show-errors-check-validity');
								//if ($scope.slideCreationForm.$valid)
								//{
								$rootScope.startLoader();
								var stainFound ;
								var isStainExist=false;
								var captureFound ;
								var isCaptureExist=false;
								var checkList= false;
								var listofChecklist=[];
								if($scope.listTGrossMaster.length>0){
									angular.forEach($scope.listTGrossMaster,function(value,index){
										if(value.listTBlockMaster.length>0){
											angular.forEach(value.listTBlockMaster,function(val,idx){
												if (value.listTSlideMaster != null && angular.isUndefined(value.listTSlideMaster) && value.listTSlideMaster.length != 0) {
													checkList = false;
												}
												else{
													checkList = true;
													listofChecklist.push(checkList);
												}
												if(val.listTSlideMaster.length>0){
													stainFound = val.listTSlideMaster.find(arr=>arr.stainingId=="");
													if(stainFound!=undefined){
														isStainExist=true;
													}
													
													captureFound = val.listTSlideMaster.find(arr=>arr.captureNote=="" );
													if(captureFound!=undefined){
														isCaptureExist=true;
													}
												}
											});
										}
									});
								}
							
								if (listofChecklist.length == 0) {
									$rootScope.stopLoader();
								    growl.warning("Please Create at least One Slide !!!", {
								        title: 'warning!'
								    });
								    $scope.saveSlideDetailsFlag=false;
								    return false;
								}
								
								
								if(isStainExist){
									$rootScope.stopLoader();
									 growl.warning("Please Select Stain",
												{
													title : 'warning!'
												});
									 $scope.saveSlideDetailsFlag=false;
									return false;
								}
								
								if(isCaptureExist){
									$rootScope.stopLoader();
									 growl.warning("Please Enter Captue Note",
												{
													title : 'warning!'
												});
									 $scope.saveSlideDetailsFlag=false;
									return false;
								}
								$rootScope.startLoader();
									$("#details").modal('hide');
								
								try {
									var URI = BASE_URL 
											+ ROOT_URL
											+ LIS_HISTOPATHOLOGY
											+ HISTO_CREATE_SLIDE;
									console.log("CREATE GROSS", URI);
									console.log("$scope.listTGrossMaster",JSON.stringify($scope.listTGrossMaster));
									GenericService
											.serviceAction(METHOD_POST, URI,
													$scope.listTGrossMaster)
											.then(
													function(response) {
														if (response.data.status == 'success') 
														{
														    $scope.saveSlideDetailsFlag=false;
															$scope.initSlideCreationWorklist($scope.orgId,
																	$scope.orgUnitId,$scope.deptId,$scope.subDeptId,$scope.offset,
																	$scope.noOfRecordsPerPage);
															 growl.success(response.data.message,
																{
																	title : 'Success!'
																});
															$scope.$broadcast('show-errors-reset');

														} else if (response.data.status == 'error') 
														{
														    $scope.saveSlideDetailsFlag=false;
															$rootScope.stopLoader();
															growl.error(response.data.message,
															{
																title : 'Error!'
															});
														}
													});
								} catch (e) {
								    $scope.saveSlideDetailsFlag=false;
									console.log("Exception", e.message);
								}
							//}
						}
							
							
							 // code for Activating Inactivating Sample
							 $scope.saveSlideDetailsCytoFlag=false;
							$scope.saveSlideDetailsCyto = function() 
							{
							  if($scope.saveSlideDetailsCytoFlag){
                                     return false;
                                 }
                                $scope.saveSlideDetailsCytoFlag=true;
								$scope.$broadcast('show-errors-check-validity');
								//if ($scope.slideCreationForm.$valid)
							//	{
								
								$rootScope.startLoader();
								if($scope.listTSlideMaster.length>0){
									var	stainFound = $scope.listTSlideMaster.find(arr=>arr.stainingId=="");
									if(stainFound!=undefined){
										$rootScope.stopLoader();
										growl.warning("Please Select Stain",{title : 'warning!'});
										return false;
										}
									
								var	captureFound = $scope.listTSlideMaster.find(arr=>arr.captureNote=="");
								if(captureFound!=undefined){
									$rootScope.stopLoader();
									growl.warning("Please Enter Capture Note",{title : 'warning!'});
									return false;
									}
								}
									$("#details").modal('hide');
								$rootScope.startLoader();
								try {
									var URI = BASE_URL 
											+ ROOT_URL
											+ LIS_HISTOPATHOLOGY
											+ CYTO_CREATE_SLIDE;
									console.log("CREATE slide", URI);
									console.log("$scope.listTSlideMaster",JSON.stringify($scope.listTSlideMaster));
									GenericService
											.serviceAction(METHOD_POST, URI,
													$scope.listTSlideMaster)
											.then(
													function(response) {
														if (response.data.status == 'success') 
														{
														$scope.saveSlideDetailsCytoFlag=false;
															$rootScope.stopLoader();
															$scope.initSlideCreationWorklist($scope.orgId,
																	$scope.orgUnitId,$scope.deptId,$scope.subDeptId,$scope.offset,
																	$scope.noOfRecordsPerPage);
															 growl.success(response.data.message,
																{
																	title : 'Success!'
																});
															$scope.$broadcast('show-errors-reset');

														} else if (response.data.status == 'error') 
														{
														 $scope.saveSlideDetailsCytoFlag=false;
															$rootScope.stopLoader();
															growl.error(response.data.message,
															{
																title : 'Error!'
															});
														}
													});
								} catch (e) {
								$scope.saveSlideDetailsCytoFlag=false;
									console.log("Exception", e.message);

								}
							//}
						}
							$scope.blockCaptureNoteReq = "Capture note is required.";	
							$scope.stainingReq = "Please Select Stain .";
							
							

							   //Search Filter
								
								$scope.selectedVisitTypeIds = [];
								$scope.departmentList= [];
								$scope.visityTypeList = [];
								$scope.testList = [];
								$scope.specimanTypeList =[];
								
								$scope.selectedspecimanTypeList = [];
								$scope.selectedVisitType = [];
								$scope.selectedtestList = [];
								$scope.selectedDepartMentList = [];
								
								
								$scope.getMicroPatientList = function(searchKeyword)
								{
									if(searchKeyword.length == 3)
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
										return GenericService.serviceAction("POST", URI,
												data).then(function(response) {
											console.log("response.data.listObject",response.data.listObject);
											$scope.patientListNew =response.data.listObject;
											return response.data.listObject;
										});
									} catch (e) {
										console.log(e.message)
									}
								}	else {
							        var filteredList = $filter('filter')($scope.patientListNew, { $: searchKeyword });
							        if (filteredList.length === 0) {
							            $scope.filteredPatientList = null;
							        } else {
							            $scope.filteredPatientList = filteredList;
							        }
							        return $scope.filteredPatientList;
							    }
							};
								
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
										 GenericService.serviceAction("POST", URI,
												data).then(function(response) {
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
											$scope.testList =  response.data.listObject;
										});
									} catch (e) {
										console.log(e.message)
									}
								}
								$scope.getTestList();
								
								$scope.getDepartMentList = function(orgId,orgUnitId) {
									try {
										var data = "";
										var URI = BASE_URL + ROOT_URL
												+ LIS_COMMON + GET_DEPARMENT_LIST
												+ S + orgId + S + orgUnitId;
										console.log("URI", URI);
										GenericService.serviceAction("GET", URI, data)
												  .then(function(response) {
													  angular.forEach(response.data.listObject, function(value, key) {
															var multiselectObj = angular.copy($scope.multiselectObj);
															multiselectObj.id = value.id;
															multiselectObj.label = 	value.name;
																$scope.departmentList.push(multiselectObj);
															});
														});
									   } catch (e) {
										console.log(e.message)
									}
								}
								$scope.getDepartMentList($scope.orgId,$scope.orgUnitId);
								
								$scope.selectPatient = function($item, $model,$label)
								{
							      $scope.selectedPatient = $item.id;
						        }
						    $scope.patientBlank = function()
							{
						      if($scope.patientId==undefined)
						    	  {
						    	    $scope.selectedPatient =null;
						    	  }
					        }
							$scope.searchSlideList= function()
							{
								$scope.initSlideCreationWorklist($scope.orgId,
										$scope.orgUnitId,$scope.deptId,$scope.subDeptId,$scope.offset,
										$scope.noOfRecordsPerPage);
							}
							
							 $scope.sortColumn = function(col){
								 $rootScope.sortColumn(col);
							 }

						} ]);