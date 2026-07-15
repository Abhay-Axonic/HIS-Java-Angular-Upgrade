
/**
 * @author Ganesh
 */

angular
		.module('myApp')
		.controller(
				"blockCreationController",
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
							$scope.LISDynamicLabel = "Block Creation Controller";

							/** Variables for pagination */
							$scope.offset = 0;
							$scope.noOfRecordsPerPage = 50;
							$scope.blockCreationList = [];
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
						    $scope.popupIconFlag=false;
							
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
							 	$scope.labSampleDtlsId = null;
							    $scope.dropDownSetting = {
							    		scrollableHeight: '100px', 
							    		scrollable: true
							    };  
							    
							
							       $scope.listTBlockMaster = [];
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
		                            		  "grossNum" : "",
		                            		  "createdBy" : $scope.userId,
		                            		  "createdDate" : "",
		                            		  "updatedBy" : $scope.userId,
		                            		  "updatedDate" : "",
		                            		  "isDeleted" : "N",
		                            		  "listTBlockMaster" : $scope.listTBlockMaster,
		                            		  "listTRestainingRequestDetailsMasterDto" :[]
		                            		}
							    
							    
							    
							    $scope.tBlockMaster = {
							    		  "tBlockId" : "",
							    		  "orgId" : $scope.orgId,
							    		  "orgUnitId" : $scope.orgUnitId,
							    		  "tSubSpecimanId" : "",
							    		  "tGrossId" : "",
							    		  "labSampleDtlsId" : "",
							    		  "blockBarcode" : "",
							    		  "blockNo" : "",
							    		  "blockNum" : "",
							    		  "captureNote" : "",
							    		  "createdBy" : $scope.userId,
							    		  "createdDate" : "",
							    		  "updatedBy" : $scope.userId,
							    		  "updatedDate" : "",
							    		  "isDeleted" : "N",
							    		  "listTSlideMaster" : []
							    		}
						
							
							       
							       $scope.findMaxBlockNumber = function(inputList)
	                                {
	                            	  if(Array.isArray(inputList))
	                            		  {
	                            		  if(inputList.length>0)
	                              		    {
	                              		      return Math.max.apply(
	                              		    		   Math,inputList.map(function(o)
	                              		    				   {
	                              		    			         return parseInt(o.blockNum)+1;
	                              		    			         }
	                              		    		        ));
	                              		    }
	                            		  else
	                              	  		{
	                            			  return 1;
	                              	  		}
	                            		  }
	                               }

							$scope.addBlocks = function(grossIndex,index)
							{
								if( (index != undefined ) && ($scope.listTGrossMaster[grossIndex].listTBlockMaster[index].captureNote == undefined || 
										$scope.listTGrossMaster[grossIndex].listTBlockMaster[index].captureNote == null || $scope.listTGrossMaster[grossIndex].listTBlockMaster[index].captureNote == "" )){
									$rootScope.stopLoader();
										growl.warning("Please Enter Capture Note ",
												{
													title : 'warning!'
												});
										return false;
									}
							    $scope.listTBlockMaster = [];
							    var tBlockMaster = angular.copy($scope.tBlockMaster);
							    $scope.listTGrossMaster[grossIndex].listTBlockMaster= $scope.listTGrossMaster[grossIndex].listTBlockMaster==null?[]:$scope.listTGrossMaster[grossIndex].listTBlockMaster;
							    tBlockMaster.blockNum = $scope.findMaxBlockNumber($scope.listTGrossMaster[grossIndex].listTBlockMaster);
							    var blockNo  = voucher_codes.generate({
                    			    prefix: $scope.listTGrossMaster[grossIndex].grossNo+BLOCK_PREFIX,
                    			    inputNumber: tBlockMaster.blockNum,
                    			    length:NUMBER_WIDTH
                    			})[0];
							    tBlockMaster.blockNo =  blockNo;
							  
							    
							    $scope.listTGrossMaster[grossIndex].listTBlockMaster.push(tBlockMaster);
							    console.log("$scope.listTGrossMaster",$scope.listTGrossMaster);
							}
							       
							   	$scope.removeBlock = function(grossIndex,index)
								{
								    $scope.listTGrossMaster[grossIndex].listTBlockMaster.splice(index,1);
								}
							    
							       
							$scope.setNoOfRecords = function() {
								$scope.initBlockCreationWorklist($scope.orgId,
										$scope.orgUnitId,$scope.deptId,$scope.subDeptId,$scope.offset,
										$scope.noOfRecordsPerPage);
							};

							$scope.initBlockCreationWorklist = function(orgId,
									orgUnitId,deptId,subDeptId, offset, noOfRecordsPerPage) {
								try {
									$rootScope.startLoader();
									offset = offset != null ? offset : 0;
									noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
											: 50;
									
								
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
											  "toDt":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
											  "popupIconFlag"	: $scope.popupIconFlag
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
									
									var URI1 = BASE_URL 
									        + ROOT_URL
											+ LIS_HISTOPATHOLOGY
											+ HISTO_BLOCK_CREATION;

									var URI2 = BASE_URL
											+ ROOT_URL
											+ LIS_HISTOPATHOLOGY
											+ HISTO_BLOCK_CREATION_COUNT;

									promiseFactoryWithObject
											.setPromisesWithObject(URI1, URI2, METHOD_POST,
													METHOD_POST,searchObj,searchObj)
											.then(
													function(response) {
														$rootScope.stopLoader();
														$scope.blockCreationList = response[0].listObject;
														angular.forEach($scope.blockCreationList, function(item) {
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
							
							
							/*$scope.initBlockCreationWorklistPoup = function(orgId,
									orgUnitId,deptId,subDeptId, offset, noOfRecordsPerPage) {
								try {
									$rootScope.startLoader();
									offset = offset != null ? offset : 0;
									noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
											: 50;
									
								
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
											  "toDt":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
											  "popupIconFlag"	: $scope.popupIconFlag
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
									
									var URI1 = BASE_URL 
									        + ROOT_URL
											+ LIS_HISTOPATHOLOGY
											+ HISTO_BLOCK_CREATION;

									var URI2 = BASE_URL
											+ ROOT_URL
											+ LIS_HISTOPATHOLOGY
											+ HISTO_BLOCK_CREATION_COUNT;

									promiseFactoryWithObject
											.setPromisesWithObject(URI1, URI2, METHOD_POST,
													METHOD_POST,searchObj,searchObj)
											.then(
													function(response) {
														$rootScope.stopLoader();
														$scope.blockCreationListPopUp = [];
														$scope.blockCreationListPopUp = response[0].listObject;
														$scope.commonListCount = response[1].object;
														$scope.setPage(1, false);
													});
									
								} catch (e) {
									console.log(e.message);
								}
							}*/
							
							$scope.getSubSpecimanDetailList = function() {
							    var URI = BASE_URL + ROOT_URL + '/LIS/histopathology'+ '/subspeciman'+'/'+$scope.tSpecimanId;
							    GenericService.serviceAction("GET", URI, {}).then(
							        function(response) {
							            if (response.data.status == "success") {
							                $scope.subSpecimanDetails = response.data.listObject;
							            }
							        });
							}

							$scope.popupIconClicked = function() {
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
							    $scope.initBlockCreationWorklist($scope.orgId,
										$scope.orgUnitId,$scope.deptId,$scope.subDeptId,$scope.offset,
										$scope.noOfRecordsPerPage);

							};

							$scope.initBlockCreationWorklist($scope.orgId,
									$scope.orgUnitId,$scope.deptId,$scope.subDeptId,$scope.offset,
									$scope.noOfRecordsPerPage);
							
           

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
								  $scope.initBlockCreationWorklist($scope.orgId, $scope.orgUnitId,$scope.deptId, $scope.subDeptId,$scope.pager.startIndex,$scope.pager.pageSize);
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
							
							
							$scope.getBlockCreationDetails = function(grossObj) {
								try 
								{
									$rootScope.startLoader();
									$scope.histopathlogyNumber = grossObj.histopathlogyNumber;
								//	$scope.subSpecimanName = grossObj.subSpecimanName;
									$scope.tGrossMaster = grossObj;
									$scope.tGrossMaster.orgId = $scope.orgId;
									$scope.tGrossMaster.orgUnitId = $scope.orgUnitId;
									$scope.tGrossMaster.labSampleDtlsId = $scope.labSampleDtlsId;
									var URI = BASE_URL 
											+ ROOT_URL
											+ LIS_HISTOPATHOLOGY
											+ HISTO_GET_GROSS_DETAILS;
									console.log("URI", URI);
									
									GenericService
											.serviceAction(METHOD_POST, URI, $scope.tGrossMaster)
											.then(
													function(response)
													{
														if (response.data.status == 'success') 
														{
															$rootScope.stopLoader();
															 $scope.subSpecimanName = []; 
															$scope.listTGrossMaster = response.data.listObject;
															angular.forEach(response.data.listObject, function(item) {
															    $scope.subSpecimanName.push(item.subSpecimanName);
															});
															$("#details").modal('show');

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
							
							 // code for Activating Inactivating Sample
							$scope.saveBlockDetails = function() 
							{
								var listofChecklist=[];
								if($scope.listTGrossMaster!=null && $scope.listTGrossMaster.length>0){
									var checkCaptureNote = false;
									var checkList = false;
									angular.forEach($scope.listTGrossMaster,function(value,index){
										if (value.listTBlockMaster == null || angular.isUndefined(value.listTBlockMaster) || value.listTBlockMaster.length == 0) {
											checkList = false;
											
										}
										else{
											checkList = true;
											listofChecklist.push(checkList);
										}
										if(value.listTBlockMaster!=null && value.listTBlockMaster.length>0)
										{
											var found = value.listTBlockMaster.find(obj => {return obj.captureNote ==undefined || obj.captureNote == null || obj.captureNote == ""})
											if(found !=undefined){
												checkCaptureNote = true;
											}
										}	
										
									});
									if (listofChecklist.length==0) {
									    growl.warning("Please Create at least One  Block !!!", {
									        title: 'warning!'
									    });
									    return false;
									}
									if(checkCaptureNote){
										$rootScope.stopLoader();
										growl.warning("Please Enter Capture Note ",
												{
													title : 'warning!'
												});
										return false;
									}
								}
								$scope.$broadcast('show-errors-check-validity');
								if (this.blockCreationForm.$valid)
								{
									$("#details").modal('hide');
								$rootScope.startLoader();
								try {
									console.log("$scope.listTGrossMaster",JSON.stringify($scope.listTGrossMaster));
									var URI = BASE_URL 
											+ ROOT_URL
											+ LIS_HISTOPATHOLOGY
											+ HISTO_CREATE_BLICKS;
									console.log("CREATE GROSS", URI);
									GenericService
											.serviceAction(METHOD_POST, URI,
													$scope.listTGrossMaster)
											.then(
													function(response) {
														if (response.data.status == 'success') 
														{
															$scope.initBlockCreationWorklist($scope.orgId,
																	$scope.orgUnitId,$scope.deptId,$scope.subDeptId,$scope.offset,
																	$scope.noOfRecordsPerPage);
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
						}
							$scope.blockCaptureNoteReq = "Capture note is required.";	
							
							
							
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
									console.log("URI", URI);
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
							
						    $scope.closePop=function(){
					              $("#subSpecimenName").modal("hide");
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
						$scope.searchBlockList= function()
						{
							$scope.initBlockCreationWorklist($scope.orgId,
									$scope.orgUnitId,$scope.deptId,$scope.subDeptId,$scope.offset,
									$scope.noOfRecordsPerPage);
						}

						 $scope.sortColumn = function(col){
							 $rootScope.sortColumn(col);
						 }
						} ]);