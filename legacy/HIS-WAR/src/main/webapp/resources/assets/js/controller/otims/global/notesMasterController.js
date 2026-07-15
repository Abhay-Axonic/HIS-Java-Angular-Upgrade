angular
		.module('myApp')
		.controller(
				'notesMasterController',
				[
						'$scope',
						'$rootScope',
						'$state',
						'$http',
						'GenericService',
						'$sessionStorage',
						'$cookies',
						'growl',
						'$localStorage',
						'BillingGenericService',
						'GenericServiceForFormsEngine',
						'PagerService',
						function($scope, $rootScope, $state, $http,
								GenericService,$sessionStorage,$cookies, growl, $localStorage,BillingGenericService,GenericServiceForFormsEngine,PagerService) {
							
							var cookieObject = $cookies.getObject('cookieObject');
							if(cookieObject == undefined){
								$state.go('login');
								return;
							}
							$scope.saveBtnFlag = true;
							$rootScope.loginpage = true;
							$scope.updateBtnFlag = false;//added by minesh
							$scope.unitId = cookieObject.unitId;
							$scope.organizationId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							$scope.offset = 0;
							$scope.noOfRecordsPerPage = 10;
							
							$scope.notesObj ={
									"noteMasterId":null,
									"code":"",
									"description":"",
									"noteType":"",
									"template":"",
									"createdBy":$scope.userId,
									"createdDate":moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
									"status":"A",
									"updatedBy":$scope.userId,
									"updatedDate":moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
		 							"unitId":$scope.unitId ,
									"organizationId":$scope.organizationId,
                                     "templateFormatId":2,
                                     "templateId":0,
                                     "templateTypeId": 0
							}
							
							$scope.notesMasterList = [];
							
							/**List And Pagination Start**/
							$scope.setNoOfRecords = function() {
								$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
							};
							
							$scope.initMasterList = function(organizationId,offset, noOfRecordsPerPage,unitId)
							{
								$rootScope.startLoader();
								
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;				
								var data1 = {
									      organizationId:organizationId,
											offset:offset,
											noOfRecordsPerPage:noOfRecordsPerPage,
											unitId:unitId
											};
								
								var data2 = {
										      organizationId:organizationId,
										      unitId:unitId
											};
								
								var URI1 = BASE_URL + ROOT_URL + "/global/otims/getNotesMasterListPagination"; 
								var URI2 = BASE_URL + ROOT_URL + "/global/otims/getNotesMasterListCount";
									
								
								GenericService.serviceAction("POST",URI1,data1).then(function(response){
									if(response.data.status == "success"){
									$rootScope.stopLoader();					
									$scope.notesMasterList = response.data.listObject;
									console.log("commonList",$scope.commonList);
									}
								});
								
								GenericService.serviceAction("POST",URI2,data2).then(function(response){
									if(response.data.status == "success"){					
										$rootScope.stopLoader();
										$scope.commonListCount = response.data.object;
										$scope.setPage(1,false);
										console.log("commonListCount",response.data.object);
									}
								});
								
							}		
							
							$scope.masterList = function(organizationId,offset, noOfRecordsPerPage,unitId) {
								$rootScope.startLoader();
								console.log("offset", offset);
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
								
								var data = {
										organizationId:organizationId,
										offset:offset,
										noOfRecordsPerPage:noOfRecordsPerPage,
										unitId:unitId
										};
								
								var URI = BASE_URL + ROOT_URL +  "/global/otims/getNotesMasterListPagination";
								console.log("URI", URI);
								GenericService.serviceAction("POST", URI,data).then(function(response) {
									$rootScope.stopLoader();
									$scope.notesMasterList = response.data.listObject;
									console.log("commonList",$scope.commonList);
								});
							}

							$scope.pager = {};
							$scope.page;

							$scope.setPage = function(page, flag) {
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
							
							/*$scope.getNotesMasterList = function(){
								var data = {
										unitId:$scope.unitId,
										organizationId:$scope.organizationId
										};								
								var URI = BASE_URL + ROOT_URL+ "/global/otims/getNotesMasterList";
								BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE)
								.then(function(response) {
											$scope.notesMasterList = [];
											if (response.data.status == "success"){
												$rootScope.stopLoader();
													$scope.notesMasterList = response.data.listObject;
													console.log("$scope.notesMasterList", 	$scope.notesMasterList);
											}
										});
						}
						$scope.getNotesMasterList();*/
						
							$scope.templateTypes =[];
							$scope.getTemplateTypes = function(){
								var URI = BASE_URL + ROOT_URL +"/api/global/template/types";
								BillingGenericService.serviceAction(METHOD_GET,URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
									if(response.data.status == "success"){
										$scope.templateTypes = response.data.listObject;
									}
								});
							}
							$scope.getTemplateTypes();
							
							$scope.getNewTemplateList = function(){
							       $rootScope.startLoader();
							       if($scope.templateConfigObject != null){
							    	   if($scope.templateConfigObject.apiKey != undefined && $scope.templateConfigObject.apiKey != null){
							    		   var  data={
							    				   unitId:$scope.unitId
							    				   };
							    		   
											 var templateFormurl= BASE_URL + ROOT_URL + "/api/global/getTemplateListFormEngine";
											 GenericServiceForFormsEngine.serviceAction(METHOD_POST ,templateFormurl, data,$scope.templateConfigObject.apiKey).then(
													function(response) {
														if(response.data.status=="success"){
															$rootScope.stopLoader();
															if(response.data.object!=null){
															 $scope.newTemplateList=JSON.parse(response.data.object).results;
															}
														}
														else{
															$rootScope.stopLoader();
														}
													}); 
							    	   }
							    	   
							       }else{
							    	   $rootScope.stopLoader();
							       }
									
						}
							
							$scope.getTemplateTypes = function(){
								var obj = {};
								obj.unitId = $scope.unitId;
								var URI = BASE_URL + ROOT_URL + "/api/global/templateConfig";
								BillingGenericService.serviceAction(METHOD_POST,URI,obj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
									if (response.data.status == "success"){
										 $scope.templateConfigObject = response.data.object;
										 $scope.getNewTemplateList();
									}
									else if(response.data.status == "error")
									{
										 $rootScope.stopLoader();	
									}
								});
							}
							$scope.getTemplateTypes();
							
							$scope.templateSelect = function(optionval){
						 		 $scope.searchText = optionval.templateName ;
						 		$scope.notesObj.title= optionval.templateName ;
						 		$scope.notesObj.formTemplateKeywords="-";
						 		if(optionval.keywords!=undefined){
						 		 $scope.notesObj.formTemplateKeywords= optionval.keywords.toString();
						 		}
						 		
						 		$scope.notesObj.formTemplateId = optionval.uId;
							} 
							$scope.previewTemplate = function(){
						 		var landingUrl =  $scope.templateConfigObject.templateForm + $scope.notesObj.formTemplateId +"&formType=digital";
						 		window.open( landingUrl,  '_blank'  );
							}
							
							$scope.saveNotesMaster = function(){
								$rootScope.startLoader();
								if($scope.notesObj.templateFormatId == undefined || $scope.notesObj.templateFormatId == null || isNaN($scope.notesObj.templateFormatId) || $scope.notesObj.templateFormatId=="" ){
                                    $rootScope.stopLoader();
                                    growl.warning("Please select Template format",
                                        {
                                            title : 'warning!'
                                        });
                                    return false;
                                }
                                else{
                                       if($scope.notesObj.templateFormatId === 2){
                                            $scope.notesObj.title = null;
                                            $scope.notesObj.formTemplateId = null;
                                            $scope.notesObj.formTemplateKeywords = null;
                                       }
                                       if($scope.notesObj.templateFormatId === 3){
                                            $scope.notesObj.template = null;
                                       }
                                }
								if($scope.notesObj.code == undefined || $scope.notesObj.code==null || $scope.notesObj.code==""){
                                    $rootScope.stopLoader();
                                    growl.warning("Please Enter code",
                                        {
                                            title : 'warning!'
                                        });
                                    return false;
                                }
                                if($scope.notesObj.description == undefined || $scope.notesObj.description==null || $scope.notesObj.description==""){
                                    $rootScope.stopLoader();
                                    growl.warning("Please Enter description",
                                        {
                                            title : 'warning!'
                                        });
                                    return false;
                                }
                                if($scope.notesObj.noteType == undefined || $scope.notesObj.noteType == null || isNaN($scope.notesObj.noteType) || $scope.notesObj.noteType==""){
                                    $rootScope.stopLoader();
                                    growl.warning("Please select Note Type",
                                        {
                                            title : 'warning!'
                                        });
                                    return false;
                                }
                                if($scope.notesObj.templateFormatId == 2 && ($scope.notesObj.template == undefined || $scope.notesObj.template== null || $scope.notesObj.template=="")){
                                    $rootScope.stopLoader();
                                    growl.warning("Please Add Template",
                                        {
                                            title : 'warning!'
                                        });
                                    return false;
                                }
                                if($scope.notesObj.templateFormatId == 3 && ($scope.notesObj.title == undefined || $scope.notesObj.title== null || $scope.notesObj.title=="")){
                                    $rootScope.stopLoader();
                                    growl.warning("Please select Template",
                                        {
                                            title : 'warning!'
                                        });
                                    return false;
                                }
								
								var URI = BASE_URL + ROOT_URL +"/global/otims/saveNotesMaster";
								console.log("$scope.checklistObj", 	$scope.notesObj);
								GenericService.serviceAction("POST", URI,$scope.notesObj).then(function(response){
									if (response.data.status == 'success')
									{	$rootScope.stopLoader();
										$scope.notesObj.code="";
										$scope.notesObj.description="";
										$scope.notesObj.noteType="";
										$scope.notesObj.template="";
										$scope.notesObj.templateFormatId="";
										$scope.notesObj.formTemplateId="";
										$scope.notesObj.title="";
										$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
										//$scope.checkListItemNameList =[];
										growl.success(response.data.message,
												{
													title : 'Success!'
												});
										
									} else if(response.data.status == 'error')
									{
										$rootScope.stopLoader();
										growl.error(response.data.message,
												{
													title : 'Error!'
												});
										
									}else if(response.data.status == 'Already Exists.')
									{
										$rootScope.stopLoader();
										growl.error(response.data.message,
												{
													title : 'Error!'
												});
										
									}
								});
							}
							//added by minesh for update NotesMaster.
							$scope.updateNotesMaster = function(){
								$rootScope.startLoader();
								if($scope.notesObj.templateFormatId == undefined || $scope.notesObj.templateFormatId == null || isNaN($scope.notesObj.templateFormatId) || $scope.notesObj.templateFormatId=="" ){
                                    $rootScope.stopLoader();
                                    growl.warning("Please select Template format",
                                        {
                                            title : 'warning!'
                                        });
                                    return false;
                                }
                                else{
                                       if($scope.notesObj.templateFormatId === 2){
                                            $scope.notesObj.title = null;
                                            $scope.notesObj.formTemplateId = null;
                                            $scope.notesObj.formTemplateKeywords = null;
                                       }
                                       if($scope.notesObj.templateFormatId === 3){
                                            $scope.notesObj.template = null;
                                       }
                                }
								if($scope.notesObj.code == undefined || $scope.notesObj.code==null || $scope.notesObj.code==""){
                                    $rootScope.stopLoader();
                                    growl.warning("Please Enter code",
                                        {
                                            title : 'warning!'
                                        });
                                    return false;
                                }
                                if($scope.notesObj.description == undefined || $scope.notesObj.description==null || $scope.notesObj.description==""){
                                    $rootScope.stopLoader();
                                    growl.warning("Please Enter description",
                                        {
                                            title : 'warning!'
                                        });
                                    return false;
                                }
                                if($scope.notesObj.noteType == undefined || $scope.notesObj.noteType == null || isNaN($scope.notesObj.noteType) || $scope.notesObj.noteType==""){
                                    $rootScope.stopLoader();
                                    growl.warning("Please select Note Type",
                                        {
                                            title : 'warning!'
                                        });
                                    return false;
                                }
                                if($scope.notesObj.templateFormatId == 2 && ($scope.notesObj.template == undefined || $scope.notesObj.template== null || $scope.notesObj.template=="")){
                                    $rootScope.stopLoader();
                                    growl.warning("Please Add Template",
                                        {
                                            title : 'warning!'
                                        });
                                    return false;
                                }
                                if($scope.notesObj.templateFormatId == 3 && ($scope.notesObj.title == undefined || $scope.notesObj.title== null || $scope.notesObj.title=="")){
                                    $rootScope.stopLoader();
                                    growl.warning("Please Add Template",
                                        {
                                            title : 'warning!'
                                        });
                                    return false;
                                }
									
                                var URI = BASE_URL + ROOT_URL +"/global/otims/updateNotesMaster";
                                console.log("$scope.checklistObj", 	$scope.notesObj);
                                GenericService.serviceAction("POST", URI,$scope.notesObj).then(function(response){
                                    if (response.data.status == 'success')
                                    {	$rootScope.stopLoader();
                                    $scope.notesObj.code="";
                                    $scope.notesObj.description="";
                                    $scope.notesObj.noteType="";
                                    $scope.notesObj.template="";
                                    $scope.notesObj.templateFormatId="";
                                    $scope.notesObj.formTemplateId="";
                                    $scope.notesObj.title="";
                                    $scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
                                    $scope.saveBtnFlag = true;//added by minesh
                                    $scope.updateBtnFlag = false;//added by minesh
                                    //$scope.checkListItemNameList =[];
                                    growl.success(response.data.message,
                                            {
                                        title : 'Success!'
                                            });

                                    } else if(response.data.status == 'error')
                                    {
                                        $rootScope.stopLoader();
                                        growl.error(response.data.message,
                                                {
                                            title : 'Error!'
                                                });

                                    }else if(response.data.status == 'Already Exists.')
                                    {
                                        $rootScope.stopLoader();
                                        growl.error(response.data.message,
                                                {
                                            title : 'Error!'
                                                });

                                    }
                                });
							}//end update NotesMaster.
							
							$scope.updateStatus = function(noteMasterId ,status){
								try{
									var date = new Date();
									$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
									var data={
											updatedBy:$scope.userId,
											updatedDate:$scope.dt,
											status:status,
											noteMasterId:noteMasterId 
									};
									$rootScope.startLoader();
									var URI = BASE_URL + ROOT_URL + "/global/otims/updateNotesMasterStatus";
									console.log("update status",data);
									GenericService.serviceAction("POST",URI,data).then(function(response){
										if (response.data.status == 'success')
										{
											$scope.getNotesMasterList();
											$rootScope.stopLoader();
											
											growl.success(response.data.message,
													{
														title : 'Success!'
													});
										} else if(response.data.status == 'error')
										{
											$rootScope.stopLoader();
											growl.error(response.data.message,
													{
														title : 'Error!'
													});
										}
									});
								}catch(e){
									console.log("Exception",e);
								}
							}
							
							$scope.update = function(info,label){
								
								if(info!=null){
									
									if(info.templateFormatId == 2){
									$scope.notesObj.noteMasterId = info.noteMasterId;
									
									$scope.notesObj.code = info.code;
									
									$scope.notesObj.description = info.description;
									
									$scope.notesObj.status = info.status;
									
									$scope.notesObj.noteType = info.noteType;
									
									$scope.notesObj.template = info.template;	
									
									$scope.notesObj.templateFormatId = info.templateFormatId;

									$scope.notesObj.templateId = info.templateId;

									$scope.notesObj.templateTypeId = info.templateTypeId;

									
									}else{

										$scope.notesObj.noteMasterId = info.noteMasterId;
										
										$scope.notesObj.code = info.code;
										
										$scope.notesObj.description = info.description;
										
										$scope.notesObj.status = info.status;
										
										$scope.notesObj.noteType = info.noteType;
										
										$scope.notesObj.title = info.title;
										
										$scope.notesObj.formTemplateKeywords = info.formTemplateKeywords;
										
										$scope.notesObj.formTemplateId = info.formTemplateId;
										
										$scope.notesObj.template = info.template;
										
										$scope.notesObj.templateFormatId = info.templateFormatId;

                                        $scope.notesObj.templateId = info.templateId;

                                        $scope.notesObj.templateTypeId = info.templateTypeId;
										
									}
									
							}
								$scope.saveBtnFlag = false;//added by minesh
								$scope.updateBtnFlag = true;//added by minesh
							
						}
							$scope.template="";
							$scope.showTemplate = function(info){
								
								if(info.templateFormatId == 2){
									$scope.template=info.template;
									
									$("#viewNotesModal").modal("show");
								}else{
									var landingUrl =  $scope.templateConfigObject.templateForm + info.formTemplateId +"&formType=digital";
							 		window.open( landingUrl,  '_blank'  );
								}
								
							}
							
			}]);
							