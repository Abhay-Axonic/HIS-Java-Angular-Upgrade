
/**
 * @author Ganesh
 */

angular
		.module('myApp')
		.controller(
				"microscopicExaminiationWorkListController",
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
								GenericService,PagerService, promiseFactoryWithObject,
								$sessionStorage, growl) {

							$rootScope.loginpage = true;
							$scope.LISDynamicLabel = "microscopic Examiniation WorkList Controller";

							/** Variables for pagination */
							$scope.offset = 0;
							$scope.noOfRecordsPerPage = 50;
							$scope.microscopicExaminiationWorkList = [];
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
							    
							
							       $scope.listTBlockMaster = [];
							       $scope.listTMicroExaDetails = [];
							       $scope.tGrossMaster ={
		                            		  "tGrossId" : "",
		                            		  "orgId" : $scope.orgId,
		                            		  "orgUnitId" : $scope.orgUnitId,
		                            		  "tSubSpecimanId" : "",
		                            		  "labSampleDtlsId" : "",
		                            		  "grossBarcode" : "",
		                            		  "grossNo" : "",
		                            		  "grossNum" : "",
		                            		  "grossDesc" : "",
		                            		  "captureNote" : "",
		                            		  "createdBy" : $scope.userId,
		                            		  "createdDate" : "",
		                            		  "updatedBy" : "",
		                            		  "updatedDate" : "",
		                            		  "isDeleted" : "N",
		                            		  "listTBlockMaster" : $scope.listTBlockMaster,
		                            		  "listTRestainingRequestDetailsMasterDto" :[]
		                            		}
							    
							    
							    
							    $scope.tBlockMaster = {
							    		  "tBlockId" : "",
							    		  "orgId" : $scope.orgId,
							    		  "orgUnitId" : $scope.orgUnitId,
							    		  "tGrossId" : "",
							    		  "labSampleDtlsId" : "",
							    		  "blockBarcode" : "",
							    		  "blockNo" : "",
							    		  "blockNum" : "",
							    		  "captureNote" : "",
							    		  "createdBy" : $scope.userId,
							    		  "createdDate" : "",
							    		  "updatedBy" : "",
							    		  "updatedDate" : "",
							    		  "isDeleted" : "N",
							    		  "listTSlideMaster" : []
							    		}
						
							       $scope.tTMicroExaDetails={
							    		   "tMicroExaId": "",
							    		   "orgId" : $scope.orgId,
		                            	   "orgUnitId" : $scope.orgUnitId,
							               "tSlideId": "",
							               "slideNo" :"",
							               "labSampleDtlsId": "",
							               "stainingId": "",
							               "isComplete": "N",
							               "captureNote": "",
							               "sendForStorage": "N",
							               "createdBy" : $scope.userId,
							               "createdDate": "",
							               "updatedBy": "",
							               "updatedDate": "",
							               "isDeleted": "N",
							               "isRestained":"N",
							               "subSpecimanName": "",
							               "grossNo": "",
							               "blockNo": "",
							               "stainName": "",
							               "isExaComplete":""
							       }
							       
							       $scope.tSubSpecimanMaster ={
		                            		  "tSubSpecimanId" : "",
		                            		  "orgId" : $scope.orgId,
		                            		  "orgUnitId" : $scope.orgUnitId,
		                            		  "subSpecimanName":"",
		                            		  "tSpecimanId" : "",
		                            		  "subSpecimanExaminination" : "",
		                            		  "labSampleDtlsId" : "",
		                            		  "subSpcimanNo" : "",
		                            		  "subSpecimanBarcode" : "",
		                            		  "captureNote" : "",
		                            		  "createdBy" : $scope.userId,
		                            		  "createdDate" : "",
		                            		  "updatedBy" : "",
		                            		  "updatedDate" : "",
		                            		  "isDeleted" : "N",
		                            		  "specimanTypeId" : "",
		                            		  "specimanId" : "",
		                            		  "specimanName" : "",
		                            		  "subSpecimanNum" : "",
		                            		  "uhid" : "",
		                            		  "genderName" : "",
		                            		  "patientDetails" : "",
		                            		  "doctorDetails" : "",
		                            		  "dob" : "",
		                            		  "age" : 0,
		                            		  "templateResId":"",
		                            		  "wardCode":"",
		                            		  "bedNumber":"",
		                            		  "listTGrossMaster" : []
		                            		}
							       
							

							$scope.addBlocks = function(grossIndex,index)
							{
							    if(index==0)
						    	{
						    	    index=1;
						    	    $scope.blockNumber = index;
						    	    $scope.blockNumber++;
						    	}
							    else if(index==undefined)
							    	{
							    	   index=0;
							    	   $scope.blockNumber = index;
							    	   $scope.blockNumber++;
							    	}
							    else
							    	{
							    	  $scope.blockNumber = ++index;
							    	  $scope.blockNumber++;
							    	}
							  
							    console.log("blockNumber",$scope.blockNumber);
							    $scope.listTBlockMaster = [];
							    var tBlockMaster = angular.copy($scope.tBlockMaster);
							    tBlockMaster.blockNo =  $scope.listTMicroExaDetails[grossIndex].grossNo+"-B"+$scope.blockNumber;
							    $scope.listTMicroExaDetails[grossIndex].listTBlockMaster= $scope.listTMicroExaDetails[grossIndex].listTBlockMaster==null?[]:$scope.listTMicroExaDetails[grossIndex].listTBlockMaster;
							    $scope.listTMicroExaDetails[grossIndex].listTBlockMaster.push(tBlockMaster);
							    console.log("$scope.listTMicroExaDetails",$scope.listTMicroExaDetails);
							}
							       
							   	$scope.removeBlock = function(grossIndex,index)
								{
								    $scope.listTMicroExaDetails[grossIndex].listTBlockMaster.splice(index,1);
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
									var URI1 = BASE_URL 
									        + ROOT_URL
											+ LIS_HISTOPATHOLOGY
											+ HISTO_MICRO_EXAM_WORK_LIST_CREATION;

									var URI2 = BASE_URL
											+ ROOT_URL
											+ LIS_HISTOPATHOLOGY
											+ HISTO_MICRO_EXAM_WORK_LIST_CREATION_COUNT;

									promiseFactoryWithObject
											.setPromisesWithObject(URI1, URI2, METHOD_POST,
													METHOD_POST,searchObj,searchObj)
											.then(
													function(response) {
														$rootScope.stopLoader();
														$scope.microscopicExaminiationWorkList = response[0].listObject;
														for (var i = 0; i < $scope.microscopicExaminiationWorkList.length; i++) {
														    $scope.tSpecimanId = $scope.microscopicExaminiationWorkList[i].tSpecimanId;
														    $scope.labSampleDtlsId = $scope.microscopicExaminiationWorkList[i].labSampleDtlsId;
														}
														$scope.commonListCount = response[1].object;
														$scope.setPage(1, false);
													});
									
								} catch (e) {
									console.log(e.message);
								}
							}
							

								$scope.getSubSpecimanDetailList = function() {
								var URI = BASE_URL
										+ ROOT_URL
										+ '/LIS/histopathology'
										+ '/microscopic/examination/getSubSpecimanBySpeciman'
										+ '/' + $scope.tSpecimanId;
								GenericService
										.serviceAction("GET", URI, {})
										.then(
												function(response) {
													if (response.data.status == "success") {
														$scope.subSpecimanDetails = response.data.listObject;
													}
												});
							}
							
							$scope.popupIconClicked = function() {
							    $scope.getSubSpecimanDetailList();
							}

							 $scope.closePop=function(){
					              $("#subSpecimenName").modal("hide");
					            }
							 
							 
							$scope.initSlideCreationWorklist($scope.orgId,
									$scope.orgUnitId,$scope.deptId,$scope.subDeptId,$scope.offset,
									$scope.noOfRecordsPerPage);
							
           
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
							    $scope.initSlideCreationWorklist($scope.orgId,$scope.orgUnitId,$scope.deptId,$scope.subDeptId,$scope.offset,$scope.noOfRecordsPerPage);
							};
							
							$scope.getSlideCreationWorklist = function(orgId,
									orgUnitId,deptId,subDeptId,offset,noOfRecordsPerPage) {
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
									var URI =BASE_URL 
							        + ROOT_URL
									+ LIS_HISTOPATHOLOGY
									+ HISTO_MICRO_EXAM_WORK_LIST_CREATION;
									console.log("URI1", URI);
									
									GenericService
											.serviceAction(METHOD_POST, URI, data)
											.then(
													function(response) {
														$rootScope.stopLoader();
														$scope.microscopicExaminiationWorkList = response.data.listObject;
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

							/** code for generating url * */
											
							$scope.generateReportCreationUrl = function(tSubSpecimanMaster)
							{
							  if(tSubSpecimanMaster!=null)
								  {
									 $scope.tSubSpecimanMaster = tSubSpecimanMaster;
									 $scope.tSubSpecimanMaster.orgId = $scope.orgId;
									 $scope.tSubSpecimanMaster.orgUnitId = $scope.orgUnitId;
									 $scope.tSubSpecimanMaster.createdBy = $scope.userId;
									 $scope.tSubSpecimanMaster.listTGrossMaster = [];
												$state.go('reportCreationRelease',
													{
													  backFlag : 1,
													  subSpecimenObj : $scope.tSubSpecimanMaster,
													});
								  }
							  else
							     {
								    growl.error("Something went Wrong.",{title : 'Error!'});
							     }
						
							}
							
							
							$scope.genrateSlideDetilsUrl = function(tSubSpecimanMaster)
							{		 
								     $scope.tSubSpecimanMaster = tSubSpecimanMaster;
									 $scope.tSubSpecimanMaster.orgId = $scope.orgId;
									 $scope.tSubSpecimanMaster.orgUnitId = $scope.orgUnitId;
									 $scope.tSubSpecimanMaster.createdBy = $scope.userId;
									 $scope.tSubSpecimanMaster.listTGrossMaster = [];
									 $scope.tSubSpecimanMaster.labSampleDtlsId =  $scope.labSampleDtlsId;
												$state.go('detailsMicroscopicExaminiation',
													{
													  subSpecimenObj : $scope.tSubSpecimanMaster,
													});
							}
							
					
							
							/** code for setting all checkboxes */
							$scope.checkAll = function() {
						
								if ($scope.selectedAll) {
									$scope.selectedAll = true;
								} else {
									$scope.selectedAll = false;
								}
								angular
										.forEach(
												$scope.listTMicroExaDetails,
												function(tMicroExaDetails) {
													tMicroExaDetails.isExaComplete = $scope.selectedAll;
												});
							}
							
							
							$scope.microscopicCaptureNoteReq = "Capture note is required.";	
							
							
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
						
						
						$scope.initPatientDaignosis = function(){
							$scope.patientDaignosisObj = {
									"diagnosisName":"",
									"diagnosisId":"",
									"type":1,
									"diagnosisType":"",
									"remark":""
							};
							
						}
						
						$scope.initPatientDaignosis();
						
						$scope.patientDetailsObj = {};
							$scope.getPatientDiagnosis = function(Obj) {
								$("#diagnosisPop").show();
								$scope.initPatientDaignosis();
								$scope.patientDetailsObj = Obj;
								$scope.getPatientServiceDaignosisData();
							}
						
							
							$scope.getPatientServiceDaignosisData = function(){
								var data = {
	   									unitId: $scope.orgUnitId,
	   									patientId : $scope.patientDetailsObj.patientId,
	   									};
								
								
								var URI = BASE_URL + ROOT_URL
										+ "/api/opd/getPatientDaignosisServiceMapper";
								GenericService.serviceAction("POST", URI, data,
												NOTIFICATION_MSG_STATUS_FALSE,
												data).then(
												function(response) {
													console.log(response);
													if (response.data.status == "success")
														 $scope.patientDiagnosisList = [];
		                                            angular.forEach(response.data.listObject,function(value,index)
			 												{
		                                            	 		$scope.patientDiagnosisList.push(value);
			 												});
													
												});
							}
							
							$scope.searchGroupList = function(keyword) {
								$scope.DiagnosisByGenderList=[];
								var data = {
										organizationId : $scope.orgId,
										diagnosisName : keyword,
										genderId:$scope.patientDetailsObj.genderId
										};
								
								$scope.varOr = "|";

								var URI = BASE_URL + ROOT_URL
										+ "/api/opd/getDiagnosisByGenderId";
								
								return GenericService.serviceAction("POST", URI,data)
										.then(
												function(response) {
													console.log(response);
													// $rootScope.startLoader();
													if (response.data.status == "success")
														 $scope.DiagnosisByGenderList = response.data.listObject;
													if($scope.DiagnosisByGenderList.length == 0){
														
														$scope.flagDigByGen=true;
														var newArr = {};
														newArr.diagnosisName="Diagnosis not applicable";
														newArr.diagnosisId='';
														$scope.DiagnosisByGenderList.push(newArr);
														$scope.varOr="";
														return response.data.listObject= $scope.DiagnosisByGenderList;
													} else{
														$scope.flagDigByGen=false;
													}
														return response.data.listObject;
												});

							}
							

							$scope.selectDignosisIdForDiagnosis = function(
									info, model, label, ind) {
								$scope.patientDaignosisObj.diagnosisId = info.diagnosisId;
								$scope.patientDaignosisObj.diagnosisName = info.diagnosisName;
							}
							
                           $scope.checkDiagnosisSaved = false;
                           $scope.savepatientDaignosis = function(obj){

                                if($scope.checkDiagnosisSaved == true){
                                    $rootScope.stopLoader();
                                    return false;
                                }
                                $scope.checkDiagnosisSaved = true;

                        	   $rootScope.startLoader();

                        	   if(obj!= undefined && (obj.diagnosisId=="" || obj.diagnosisId==null || obj.diagnosisId==undefined)){
                                     growl.error("Please select any diagnosis", {
                                         title: 'Error!'
                                     });
                                     $rootScope.stopLoader();
                                     $scope.checkDiagnosisSaved = false;
                                     return;
                                 }

                                  if(obj == undefined && ($scope.patientDaignosisObj.diagnosisId=="" || $scope.patientDaignosisObj.diagnosisId==null || $scope.patientDaignosisObj.diagnosisId==undefined)){
                                      growl.error("Please select any diagnosis", {
                                          title: 'Error!'
                                      });
                                      $rootScope.stopLoader();
                                      $scope.checkDiagnosisSaved = false;
                                      return;
                                  }

                        	   var data = {};
                        	   if(obj == undefined){

                        		   data = {
          									organizationId : $scope.orgId,
          									unitId: $scope.orgUnitId,
          									patientId : $scope.patientDetailsObj.patientId,
          									encounterId:$scope.patientDetailsObj.encounterId,
          									admissionId : $scope.patientDetailsObj.admissionId,
          									diagnosisType:$scope.patientDaignosisObj.diagnosisType,
          									createdBy :  $scope.userId,
          									updatedBy : $scope.userId,
          									type : $scope.patientDaignosisObj.type,
          									diagnosisId:$scope.patientDaignosisObj.diagnosisId,
          									remark : $scope.patientDaignosisObj.remark
          									};
                        	   }else{
                        		   data = {
                        				   patientDiagnosisServiceMapperId : obj.patientDiagnosisServiceMapperId,
          									organizationId : $scope.orgId,
          									unitId: $scope.orgUnitId,
          									patientId : $scope.patientDetailsObj.patientId,
          									encounterId:$scope.patientDetailsObj.encounterId,
          									admissionId : $scope.patientDetailsObj.admissionId,
          									diagnosisType:obj.diagnosisType,
          									createdBy :  $scope.userId,
          									updatedBy : $scope.userId,
          									type : obj.type,
          									diagnosisId:obj.diagnosisId,
          									remark : obj.remark
          									};
                        	   }


   							var list = [];
   							list.push(data);

   							var URI = BASE_URL + ROOT_URL
   									+ "/api/opd/savePatientDaignosisServiceMapper";

   							return GenericService.serviceAction("POST", URI,list)
   									.then(
   											function(response) {
   												if (response.data.status == "success"){
   													$scope.initPatientDaignosis();
   												    $scope.checkDiagnosisSaved = false;
   													$scope.getPatientServiceDaignosisData();
   													$rootScope.stopLoader();
   													growl.success(response.data.message,
   															{
   																title : 'Success!'
   															});
   												}else{
   												    $scope.checkDiagnosisSaved = false;
   													$rootScope.stopLoader();
   													growl.error("Something went Wrong.",{title : 'Error!'});
   												}
   											});
                        	   
                        	   
                           }
                          
							$scope.close = function(){
								$("#diagnosisPop").hide();
							}
						
							
							

						} ]);