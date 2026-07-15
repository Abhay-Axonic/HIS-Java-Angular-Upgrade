
/**
 * @author Ganesh
 */

angular
		.module('myApp')
		.controller(
				"histoReportCreationController",
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
							$scope.LISDynamicLabel = "Specimen Receipt";

							/** Variables for pagination */
							$scope.offset = 0;
							$scope.noOfRecordsPerPage = 10;
							$scope.specimenReceiptList = [];
							$scope.totalNoOfRecords;
							$scope.searchKeyword;
							$scope.commonListCount;
							$scope.isReleased = false;

							
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
						    $scope.pathologiestId = null;
						    $scope.triggerReleaseMail = labConfig.triggerReleaseMail;
						    $scope.getUserListByRoleType($scope.orgUnitId,$scope.orgId);
							
							/**Redirect Page Code*/
						    $scope.backFlag = '';
						
							if(angular.equals({},$stateParams.subSpecimenObj))
 							{
								$state.go('microscopicExaminiationWorkList');
							} else {
								 $scope.tSubSpecimanMaster = $stateParams.subSpecimenObj;
								 $scope.backFlag =  $stateParams.backFlag;
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
                            		  "age" : "",
                            		  "templateResId":"",
                            		  "orderDetailsId":"",
                            		  "isHistoCyto":"",
                            		  "listTGrossMaster" : []
                            		}
							
							 $scope.patientDetails = {
									 "uhIdNumber":$stateParams.subSpecimenObj.uhid,
									 "patientName":$stateParams.subSpecimenObj.patientDetails,
									 "genderCode":$stateParams.subSpecimenObj.genderName,
									 "age":$stateParams.subSpecimenObj.age,
									 "birthDate":moment($stateParams.subSpecimenObj.dob).format('DD-MM-YYYY')
							   }
						   /** code for generating url * */
						    $scope.tSubSpecimanMaster = $stateParams.subSpecimenObj;
						     $scope.histoPathNo = $stateParams.subSpecimenObj.histopathlogyNumber;
						   console.log("Report Creation",$scope.tSubSpecimanMaster);
							$scope.generateRestainingRequestUrl = function()
								{
											 if($scope.tSubSpecimanMaster!=null)
											  {
															$state.go('restainingRequest',
																{
																 subSpecimenObj : $scope.tSubSpecimanMaster,
																});
											  }
										  else
										     {
											    growl.error("Something went Wrong.",{title : 'Error!'});
										     }			
											
								}
							
		
							  $scope.labTemplateList = [];
						      $scope.templateObj = 
						      {
						    	      "templateId":"",	  
						    		  "title":"",
						    		  "image":"",
						    		  "description":"",
						    		  "html" :""
						      }
							
							$scope.getLabTemplateList = function() {
								try {
									var data = "";
									var URI = BASE_URL + ROOT_URL
											+ LIS_UNIT + GET_LAB_TEMPLATES
											+ S 
											+ $scope.orgId+
									        S + $scope.orgUnitId+S+$stateParams.subSpecimenObj.labSampleDtlsId;
									console.log("URI", URI);
									GenericService.serviceAction("GET", URI, data)
											  .then(function(response) 
													{
														$scope.labTemlateList = response.data.listObject;
														angular.forEach($scope.labTemlateList, function(value, key) 
																{
															      var templateObj = angular.copy($scope.templateObj);
															      templateObj.templateId = value.templateId;
															      templateObj.title = value.title;
															      templateObj.image  = 'template1.gif';
															      templateObj.description = value.title;
															      if(value.templateFormatId==1)
															    	  {
															    	  var tempObj  = JSON.parse(value.templateObject); 
															    	  templateObj.html = tempObj.html;
															    	  }
															      else if(value.templateFormatId==2)
															    	  {
															    	   templateObj.html = value.templateObject;
															    	  }
															     
															      $scope.labTemplateList.push(templateObj);
															});
													});
								   } catch (e) {
									console.log(e.message)
								}
							}
							
							$scope.getLabTemplateList();

							
							
							$scope.getLabResult = function(templateResId) {
								try {
									var data = "";
									var URI = BASE_URL 
									        + ROOT_URL
											+ LIS_HISTOPATHOLOGY + HISTO_GET_RESULT_ENTRY
											+ S+templateResId 
											+ S+$scope.orgId+
									        S + $scope.orgUnitId;
									console.log("URI", URI);
									GenericService.serviceAction("GET", URI, data)
											  .then(function(response) 
													{
														$scope.templateResultMaster = response.data.object;
														$scope.value = response.data.object.templateResult;
														$scope.pathologiestId = response.data.object.reportReleaseBy.toString();
													});
								   } catch (e) {
									console.log(e.message)
								}
							}
							
							if($stateParams.subSpecimenObj.templateResId!=null&&$stateParams.subSpecimenObj.templateResId!='')
								{
								  $scope.getLabResult($stateParams.subSpecimenObj.templateResId);
								}
							
							$scope.checkReportNo = function() {
							    var reportNo = $scope.templateResultMaster.reportNo; 
							    if (!reportNo || reportNo.trim() === "") {
							        growl.warning("Please Enter Report No!!! ", { title: 'warning!' });
							        return false;
							    }
							     else {
							        $('#histoResultEntry').modal('show'); 
							    }
							}
							
							$scope.value="";
							
							$scope.saveHistoResultEntry = function() 
							{

								$scope.templateResultMaster.labSampleDtlsId = $scope.tSubSpecimanMaster.labSampleDtlsId;
								$scope.templateResultMaster.orderDetailsId = $scope.tSubSpecimanMaster.orderDetailsId;
								$scope.templateResultMaster.reportReleaseBy = ($scope.pathologiestId!=null && $scope.pathologiestId>0)?$scope.pathologiestId:$scope.userId;
								$scope.templateResultMaster.templateResult = $scope.value;
								$scope.templateResultMaster.patientId = $scope.tSubSpecimanMaster.patientId ;
								$scope.templateResultMaster.doctorId = $scope.tSubSpecimanMaster.doctorId ;
								$scope.templateResultMaster.testDesc = $scope.tSubSpecimanMaster.testDesc ;
								$scope.templateResultMaster.tSubSpecimanId =  $scope.tSubSpecimanId ;
								$scope.templateResultMaster.subSpecimanName = $scope.subSpecimanName;
								$scope.templateResultMaster.tGrossId = $scope.tGrossId;
								$scope.templateResultMaster.grossDesc =  $scope.showGrossD;
								$scope.templateResultMaster.tBlockId = $scope.tBlockId;
								$scope.templateResultMaster.blockDesc = $scope.showBlockD;
								$scope.templateResultMaster.tSlideId = $scope.tSlideId;
								$scope.templateResultMaster.slideDesc1 = $scope.showSlideD;
								$scope.templateResultMaster.tMicroExaId = $scope.tMicroExaId;
								$scope.templateResultMaster.impressionDesc = $scope.showImpression;
							
								if($scope.templateResultMaster.sampleStatusId == REPORT_RELEASED != undefined && $scope.templateResultMaster.sampleStatusId == REPORT_RELEASED
										|| $scope.templateResultMaster.sampleStatusId == PROVISIONAL_REPORT_RELEASED!=undefined && $scope.templateResultMaster.sampleStatusId == PROVISIONAL_REPORT_RELEASED ){
									$scope.isReleased = true;
								}
								else{
									$scope.isReleased = false;
								}
								$scope.templateResultMaster.isReleased = $scope.isReleased;
								
								$rootScope.startLoader();
								try {
									console.log("$scope.templateResultMaster",JSON.stringify($scope.templateResultMaster));
									var URI = BASE_URL 
											+ ROOT_URL
											+ LIS_HISTOPATHOLOGY
											+ HISTO_RESULT_ENTRY;
										
									console.log("SAVE_RESTAIN_REQUEST", URI);
								 GenericService
											.serviceAction(METHOD_POST, URI,
													$scope.templateResultMaster)
											.then(
													function(response) {
														if (response.data.status == 'success') 
														{
															growl.success(response.data.message,
																{
																	title : 'Success!'
																});
															$scope.$broadcast('show-errors-reset');
															$rootScope.stopLoader();
															$('#histoResultEntry').modal('hide');
															$('#histoProvisonal').modal('hide');
															$('#histoFinalResult').modal('hide');
															
															if($scope.backFlag == 1){
																$state.go('microscopicExaminiationWorkList');
															}else{
																$state.go('worklistForPrinting',{searchObj : $stateParams.searchObj});
															}
															
															
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
								
								$scope.data.blockDesc=$scope.showBlockD;
								$scope.data.tBlockId=$scope.tBlockId;
								$scope.data.grossDesc= $scope.showGrossD;
								$scope.data.tGrossId= $scope.tGrossId;
								$scope.data.tMicroExaId=$scope.tMicroExaId;
								$scope.data.impressionDesc = $scope.impression;
								$scope.data.isNad = $scope.showIsNad;
								$scope.data.slideDesc1 = $scope.slideDesc;
									
								try{
									var URI = BASE_URL 
									+ ROOT_URL
									+ LIS_HISTOPATHOLOGY
									+ HISTO_RESULT_UPDATE;
							console.log("SAVE_RESTAIN_REQUEST", URI);
						 GenericService
									.serviceAction(METHOD_PUT, URI,
											$scope.data)
									.then(
											function(response) {
												if (response.data.status == 'success') 
												{
													growl.success(response.data.message,
														{
															title : 'Successfully Updated !!!'
														});
													$scope.$broadcast('show-errors-reset');
													$rootScope.stopLoader();
													$('#histoResultEntry').modal('hide');
													$('#histoProvisonal').modal('hide');
													$('#histoFinalResult').modal('hide');
												
													
													if($scope.backFlag == 1){
														$state.go('microscopicExaminiationWorkList');
													}else{
														$state.go('worklistForPrinting',{searchObj : $stateParams.searchObj});
													}
													
													
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
							//getMicroscopicExamDataBySubSpecimenId
							$scope.getMicroscopicExamData = {};
							$scope.captureNote= '';				
							$scope.getMacroscopicExaminationTemplateDtls = function() {
								try {
									//$rootScope.startLoader(); 
									var data = "";
									data.getSpecimenDTO;
									var URI = BASE_URL + ROOT_URL
											+ LIS_HISTOPATHOLOGY + HISTO_GET_MACROSCOPIC_EXAM_DETIALS
											+ S + $scope.orgId
											+ S + $scope.orgUnitId            
											+ S + $scope.tSubSpecimanMaster.labSampleDtlsId
											+ S + $scope.tSubSpecimanMaster.isHistoCyto;
									console.log("URI", URI);
									GenericService.serviceAction("GET", URI, data)
											  .then(function(response) 
													{
												  if (response.data.status == 'success') 
													{
												        if($scope.tSubSpecimanMaster.isHistoCyto=='H'){
												        	$scope.getMicroscopicExamData = response.data.listObject;
												        	$scope.grossList = $scope.getMicroscopicExamData[0].listTGrossMaster;
//												        	console.log($scope.grossList);
												        //	$scope.captureNoteList = response.data.object[0];
															console.log("$scope.getMicroscopicExamData",JSON.stringify($scope.getMicroscopicExamData));
												        }
												        if($scope.tSubSpecimanMaster.isHistoCyto=='C'){
												        	$scope.listTSlideMst = response.data.listObject;
															console.log("$scope.listTSlideMst",JSON.stringify($scope.listTSlideMst));
												        }
												        $('#viewMacroscopicExaminationModal').modal('show');
												        $scope.getReportDetailsByTemplate ();
												        $rootScope.stopLoader();
												        
													}  
												  else if (response.data.status == 'error') 
													{
														$rootScope.stopLoader();
														$rootScope.stopLoader();
													}	
													});
								   } catch (e) {
									console.log(e.message)
								}
							}	
						
							 $scope.getMacroscopicExaminationTemplateDtls();	
							 $scope.subSpicimanList=[];
							 $scope.subSpeciman = "";
							 $scope.getSubSpecimanNo = function() 
							 {
								 angular.forEach($scope.getMicroscopicExamData, function(item) {
									 if (parseInt($scope.tSubSpecimanId) == parseInt(item.tSubSpecimanId)) {
										 $scope.grossList =item.listTGrossMaster;
										 $scope.subSpeciman = item.subSpecimanName;
									 }
									 
								 });
							 }
							 
							 $scope.grossNo="";
							 $scope.grossDesc = '';
							 $scope.oldtGrossId=0;
							 $scope.blockList = [];
							 $scope.oldtGrossId = 0;
							 $scope.getGrossNo = function() {
								 if (parseInt($scope.tGrossId) !== parseInt($scope.oldtGrossId) ) 
									 {
									 $scope.clear();
									 }
								    angular.forEach($scope.grossList, function(item) {
								        if (parseInt($scope.tGrossId) == parseInt(item.tGrossId)) {
								            $scope.blockList = item.listTBlockMaster;
								            $scope.grossNo = item.grossNo;
								            $scope.grossDesc = item.grossDesc;
								            $scope.dataGrossD = $scope.grossDesc;
								            $scope.oldtGrossId =  $scope.tGrossId;
								           
								        }
								    });
								 
								    $scope.slideList = [];
								    $scope.tSlideId = "";
								};

								$scope.slideList = [];
								  $scope.blockNo = "";
								  $scope.blockDesc = '';
								  $scope.oldtBlockId=0;
								$scope.getBlockNo = function() { 
								 	if(parseInt($scope.tBlockId)!=parseInt($scope.oldtBlockId) ){
								 		$scope.clear();
								 	}
								       angular.forEach($scope.grossList, function(grossItem) {
								        angular.forEach(grossItem.listTBlockMaster, function(blockItem) {
								        	
								        	if(parseInt($scope.tBlockId)==parseInt(blockItem.tBlockId)){
									            $scope.slideList = blockItem.listTSlideMaster;
											      $scope.blockNo = blockItem.blockNo;
									                $scope.blockDesc = blockItem.captureNote;
									                $scope.dataBlockD = $scope.blockDesc ;
									                $scope.oldtBlockId =$scope.tBlockId;
											}  
								        });
								    });
								       $scope.tSlideId = "";  
								};
								$scope.oldtSlideId = 0;
								$scope.slideNo = "";
								$scope.slideDesc ="";
								$scope.impression ="";
								$scope.getSlideNo = function(){
									if (parseInt($scope.tSlideId) != parseInt($scope.oldtSlideId) )
									{
									$scope.clear();
									}
								
								angular.forEach($scope.slideList, function (item) {
								    angular.forEach(item.listTMicroExaDetails, function (data) {
								        if (parseInt(item.tSlideId) == parseInt($scope.tSlideId)) {
								        	$scope.isNad = data.isNad;
								        	if (data.isNad == "N" || data.isNad == null) {
								            $scope.slideNo = item.slideNo;
								            $scope.slideDesc = data.captureNote;
								            $scope.impression = data.impression;
								            $scope.tMicroExaId = data.tMicroExaId;
								            $scope.oldtSlideId = $scope.tSlideId;
								            $scope.isNad = data.isNad!= undefined ? data.isNad : 'N';
								          
								            }
								        }
								    });
								});
								}

								$scope.showGrossNo= "";
								$scope.showBlockNo= "";
								$scope.showSlideNo = "";
								$scope.slideNo = "";
								$scope.slideDesc = '';	
						
					$scope.showData = function()
					{
					
						if($scope.tGrossId == "" || $scope.tGrossId == undefined || $scope.tGrossId == null)
							{
							growl.warning("Please select GrossNo !!! ",
									{
										title : 'warning!'
									});
							return false;
							}
						
						if($scope.tBlockId == "" || $scope.tBlockId == undefined || $scope.tBlockId == null)
						{
						growl.warning("Please select BlockNo !!!",
								{
									title : 'warning!'
								});
						return false;
						}
						if($scope.tSlideId == "" || $scope.tSlideId == undefined || $scope.tSlideId == null)
						{
						growl.warning("Please select SlideNo !!!",
								{
									title : 'warning!'
								});
						return false;
						}
						
						if ($scope.isNad == 'N' || $scope.isNad == null) 
						{
						$scope.showGrossNo = $scope.grossNo;
						$scope.showBlockNo = $scope.blockNo;
						$scope.showSlideNo = $scope.slideNo;
						
						$scope.showGrossD =  $scope.dataGrossD;
						$scope.showBlockD =   $scope.blockDesc ;
						$scope.showSlideD =  $scope.slideDesc;
						$scope.showImpression = $scope.impression;
						$scope.showIsNad =  $scope.isNad;
						}
						else{
							growl.warning("NAD(No Abnormality Detected) !!!",
									{
										title : 'warning!'
									});
							return false;
						}
					}
					
					$scope.getAllNumber = function()
					{
						 angular.forEach($scope.getMicroscopicExamData, function(item) {
							 if (parseInt($scope.tSubSpecimanId) == parseInt(item.tSubSpecimanId)) {
								 $scope.grossList =item.listTGrossMaster;
								 $scope.subSpeciman = item.subSpecimanName;
							 }
							 
						 });
						
						  angular.forEach($scope.grossList, function(item) {
						        if (parseInt($scope.tGrossId) == parseInt(item.tGrossId)) {
						            $scope.blockList = item.listTBlockMaster;
						            $scope.grossNo = item.grossNo;
						            $scope.grossDesc = item.grossDesc;
						            $scope.dataGrossD = $scope.grossDesc;

						           
						        }
						    }); 
						 

						        angular.forEach($scope.blockList, function(blockItem) {
						        	
						        	if(parseInt($scope.tBlockId)==parseInt(blockItem.tBlockId)){
							            $scope.slideList = blockItem.listTSlideMaster;
									      $scope.blockNo = blockItem.blockNo;
							                $scope.blockDesc = blockItem.captureNote;
							                $scope.dataBlockD = $scope.blockDesc ;
							          
									}  
						        });

						       
						       angular.forEach($scope.slideList, function (item) {
						    	   if(item.listTMicroExaDetails != undefined && item.listTMicroExaDetails.length>0){
						    		   angular.forEach(item.listTMicroExaDetails, function (data) {
									        if (parseInt(item.tSlideId) == parseInt($scope.tSlideId)) {
									        	$scope.isNad = data.isNad;
									        
									            $scope.slideNo = item.slideNo;
									            $scope.slideDesc = data.captureNote;
									            $scope.impression = data.impression;
									            $scope.tMicroExaId = data.tMicroExaId;
									            $scope.oldtSlideId = $scope.tSlideId;
									          
									            
									        }
									    }); 
						    	   }
						    	   else{
						    		   $scope.slideNo = item.slideNo;
//							            $scope.slideDesc = data.captureNote;
						    		   
						    	   }
								    
								});
						    
						    
					}
				
						
					$scope.getReportDetailsByTemplate = function() {
						var URI = BASE_URL 
				        + ROOT_URL 
				        + '/LIS/histopathology/getReportDetails/' 
				        + $scope.tSubSpecimanMaster.labSampleDtlsId 
				        + "/" + $scope.organizationId 
				        + "/" + $scope.unitId;

					    GenericService.serviceAction("GET", URI, {}).then(
					        function(response) {
					            if (response.data.status == "success") {
					                $scope.getReportDetails = response.data.listObject;
					                $scope.tSubSpecimanId =  $scope.getReportDetails[0].tSubSpecimanId;
					                $scope.subSpecimanName = $scope.getReportDetails[0].subSpecimanName;
					                $scope.tGrossId = $scope.getReportDetails[0].tGrossId;
									$scope.showGrossNo = $scope.getReportDetails[0].grossNo;
									$scope.showGrossD = $scope.getReportDetails[0].grossDesc;
									$scope.showBlockNo = $scope.getReportDetails[0].blockNo;
									$scope.tBlockId = $scope.getReportDetails[0].tBlockId;
									$scope.showBlockD = $scope.getReportDetails[0].blockDesc;
									$scope.showSlideNo = $scope.getReportDetails[0].slideNo;
									$scope.tSlideId = $scope.getReportDetails[0].tSlideId;
									$scope.showSlideD = $scope.getReportDetails[0].slideDesc1;
									$scope.showImpression =  $scope.getReportDetails[0].impressionDesc;
									$scope.getAllNumber();
									
									$scope.tGrossId = $scope.getReportDetails[0].tGrossId.toString();
									$scope.tBlockId = $scope.getReportDetails[0].tBlockId.toString();
									$scope.tSlideId = $scope.getReportDetails[0].tSlideId.toString();
									$scope.tSubSpecimanId =  $scope.getReportDetails[0].tSubSpecimanId.toString();
					            
					        }
					        });
					}

					//$scope.getReportDetailsByTemplate();
					
					
						
							/**===================Template Editor CODE START============================================================================================================================*/
								CKEDITOR.config.width = '50%';
								CKEDITOR.config.height = '500';
								CKEDITOR.config.extraPlugins = 'imageuploader';
								//CKEDITOR.config.readOnly = true;


						      
						      $scope.showTemplate = function ()
								{
									//CKEDITOR.tools.callFunction(19,this);return false;
									CKEDITOR.tools.callFunction(1050,this);return false;
								}
						      
							 CKEDITOR.addTemplates("default", {
							    imagesPath:CKEDITOR.getUrl(CKEDITOR.plugins.getPath("templates")+"templates/images/"),
							    templates: $scope.labTemplateList
							   });
								/**===================Template Editor CODE END============================================================================================================================*/
							 
							 
							 
							 
							 
						$scope.templateResultMaster = 
							{
								      "templateResId" : "",
								      "orgId" : $scope.orgId,
									  "orgUnitId" : $scope.orgUnitId,
									  "tSubSpecimanId" : "",
									  "labSampleDtlsId" : "",
									  "labTemplateId" : "",
									  "reportNo" : "",
									  "stainingId" : "",
									  "labSampleDtlsId" : "",
									  "createdBy" : $scope.userId,
									  "createdDate" : "",
									  "updatedBy" : "",
									  "updatedDate" : "",
									  "isDeleted" : "N",
									  "sampleStatusId":"",
									  "templateResult" : "",
									  "orderDetailsId" : ""
							}
							 
							$scope.data = {
								   "orgId" : $scope.orgId,
									  "orgUnitId" : $scope.orgUnitId,
									  "tSubSpecimanId" : "",
									  "labSampleDtlsId" : "",
									  "reportNo" : "",
									  "stainingId" : "",
									  "createdBy" : $scope.userId,
										"blockDesc" : "",
										"tBlockId" : "",
										"grossDesc" : "",
										"tGrossId" :"",
										"tMicroExaId" : "",
										"impressionDesc" : "",
										"isNad" : "",
										"slideDesc1" : ""
						}
						$scope.clear = function(){
							$scope.impressionDesc = "";
							$scope.slideDesc1 = "";
							$scope.showGrossD = "";
							$scope.showBlockD = "";
							$scope.slideDesc = "";
							$scope.impression = "";
							$scope.showImpression ="";
							$scope.showSlideD = "";
						}
						
						
						$scope.saveResult = function()
						{
							
							$scope.templateResultMaster.sampleStatusId = 0;
							$scope.saveHistoResultEntry();
						}
						$scope.provisionalRelease = function()
						{
							$scope.templateResultMaster.sampleStatusId = PROVISIONAL_REPORT_RELEASED;
							$scope.saveHistoResultEntry();
						}
						$scope.finalRelease = function()
						{
							$scope.templateResultMaster.sampleStatusId = REPORT_RELEASED;
							$scope.templateResultMaster.sampleStatusId = REPORT_RELEASED;
							 if($scope.triggerReleaseMail == 'Y'){
								  $scope.templateResultMaster.triggerReleaseMail = true;
							  }else{
								  $scope.templateResultMaster.triggerReleaseMail = false;
							  }
							$scope.saveHistoResultEntry();
						}
						} ]);