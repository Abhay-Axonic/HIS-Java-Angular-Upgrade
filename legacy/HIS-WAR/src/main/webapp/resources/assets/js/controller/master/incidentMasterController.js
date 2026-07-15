/**
 * 
 * Sneha Dalave
 */
(function () {
	"use strict";
	function incidentMasterController($scope,$rootScope,$state, $cookies, GenericService,PagerService,promiseFactory,$sessionStorage,growl,BillingGenericService,GenericServiceParamHeader){
			$rootScope.loginpage = true;
			$scope.LISDynamicLabel = "Antibiotic Addition";
			$scope.saveBtnFlag = true;
			$scope.updateBtnFlag = false;
			$scope.updateScope = false;
			$scope.popUpFlag = true;
		
			$scope.storeList = [];
		
		/**Variables for pagination */
			$scope.offset = 0;
			$scope.noOfRecordsPerPage = 10;
			$scope.commonList = [];
			$scope.onLoadCommonList = [];
			$scope.usersList=[];
			$scope.totalNoOfRecords;
			$scope.searchKeyword;
			$scope.commonListCount;
			$scope.onLoadCommonListCount = 0;
		
		var cookieObject = $cookies.getObject('cookieObject');
			if(cookieObject == undefined){
				$state.go('login');
				return;
			}
			
			
			$scope.unitId = cookieObject.unitId;
			$scope.organisationId = cookieObject.organizationId;
			$scope.organizationId = cookieObject.organizationId;
			$scope.userId = cookieObject.userId;
			$scope.createdBy =cookieObject.userId;
			$scope.updatedBy =cookieObject.userId;
		
		$scope.selectedRoleList=[];	
		$scope.selectedTemplateList=[];
		$scope.selectedTemplateList1=[];
		$scope.userTemplateList = [];
		
		
		$scope.initCommon = function()
					{
						$scope.saveBtnFlag = true; 
						$scope.updateBtnFlag = false;
						$scope.common =
						{
							 "incidentReportId":"",
							 "incidentReportCode":"",
							 "incidentReportCode":"",
						     "status":"A",
							 "createdBy":$scope.userId,
							 "createdDate":"",
							 "updatedBy":$scope.userId,
							 "updatedDate":"",
						     "organizationId":$scope.organisationId,

						}
					}
					$scope.initCommon();
		
					$scope.getActiveList = function(){
						
					var URI = BASE_URL + ROOT_URL + "/api/global/getActiveUserRoleMasterList";
					var data = {
							organisationId:$scope.organisationId,
						      unitId:$scope.unitId
					}
					GenericService.serviceAction("POST",URI,data).then(function(response){
						if(response.data.status == "success"){
						$rootScope.stopLoader();					
						$scope.getActiveRoleList = [];
						angular.forEach(response.data.listObject,
										function(value,index) {
											var data = {};
											data.id = value.roleId;
											data.label = value.roleName;
											$scope.getActiveRoleList.push(data);
										});
						console.log("getActiveRoleList",$scope.getActiveRoleList);
						}
					});
					}
					$scope.getActiveList();

					
					$scope.getTemplate=function(){
						$rootScope.startLoader();
						var data = {
							orgId : $scope.organizationId,
							unitId : $scope.unitId,
							templateTypeId : 22,
						}
						
						var URI = BASE_URL + ROOT_URL
								+ "/api/global/coversheet/templates";
						BillingGenericService.serviceAction(METHOD_POST, URI, data,
										NOTIFICATION_MSG_STATUS_FALSE)
								.then(
										function(response) {
											if (response.data.status == 'success') {
												$scope.templateList = response.data.listObject;
												console.log("templatelist",$scope.templateList);
												$rootScope.stopLoader();
											} else if (response.data.status == 'error') {
												$rootScope.stopLoader();
												growl.warning("Template is already filled..!",
																{
																	title : 'Warning!'
																});
											}

										});
					}
					$scope.getTemplate();
				
		
			/**Crud Operations Start**/
		
			/**List And Pagination Start**/
			$scope.setNoOfRecords = function() {
            				$scope.initMasterList($scope.organisationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
            			};

            			$scope.initMasterList = function(organisationId,offset, noOfRecordsPerPage,unitId)
            			{
            				$rootScope.startLoader();

            				offset = offset != null ? offset : 0;
            				noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
            						: 10;
            				var data1 = {
            		  			      organizationId:$scope.organisationId,
            		  			      unitId:$scope.unitId,
            		  			      offset:offset,
            		  			      noOfRecordsPerPage:noOfRecordsPerPage
            		  		};

            				var URI1 = BASE_URL + ROOT_URL +"/api/unit/getIncidentReportMasterList";
            				var URI2 = BASE_URL + ROOT_URL +"/api/unit/getIncidentReportMasterCount";


            				GenericService.serviceAction("POST",URI1,data1).then(function(response){
            					if(response.data.status == "success"){
            					$rootScope.stopLoader();
            					$scope.commonList = response.data.listObject;
            					console.log("commonList",$scope.commonList);
            					}
            				});

            				GenericService.serviceAction("POST",URI2,data1).then(function(response){
            					if(response.data.status == "success"){
            						$rootScope.stopLoader();
            						$scope.commonListCount = response.data.object;
            						$scope.setPage(1,false);
            						console.log("commonListCount",response.data.object);
            					}
            				});

            			}

            			$scope.masterList = function(organisationId,offset, noOfRecordsPerPage,unitId) {
            	            offset = offset != null ? offset : 0;
            				noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
            						: 10;
            				var data1 = {
            		  			      organizationId:$scope.organisationId,
            		  			      unitId:$scope.unitId,
            		  			      offset:offset,
            		  			      noOfRecordsPerPage:noOfRecordsPerPage
            		  		};

            				var URI1 = BASE_URL + ROOT_URL +"/api/unit/getIncidentReportMasterList";
            				var URI2 = BASE_URL + ROOT_URL +"/api/unit/getIncidentReportMasterCount";


            				GenericService.serviceAction("POST",URI1,data1).then(function(response){
            					if(response.data.status == "success"){
            					$rootScope.stopLoader();
            					$scope.commonList = response.data.listObject;
            					console.log("commonList",$scope.commonList);
            					}
            				});
            				GenericService.serviceAction("POST",URI2,data1).then(function(response){
            					if(response.data.status == "success"){
            						$rootScope.stopLoader();
            						$scope.commonListCount = response.data.object;
            						console.log("commonListCount",response.data.object);
            					}
            				});

            			}


            			$scope.pager = {};
            			$scope.page;

            			$scope.setPage = function(page, flag) {
            //				/$rootScope.startLoader();
            				if (page < 1 || page > $scope.pager.totalPages)
            				{
            					$rootScope.stopLoader();
            					return;
            				}
            				$scope.pager = PagerService.GetPager($scope.commonListCount, page, $scope.noOfRecordsPerPage);
            				if (flag)
            				{
            					$scope.masterList($scope.organisationId ,$scope.pager.startIndex, $scope.pager.pageSize,$scope.unitId);
            				}
            			}

            			$scope.initMasterList($scope.organisationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);

			/**List And Pagination End**/
			
			
			//code for status Update
					$scope.updateStatus = function(incidentReportId,status)
					{
						try {
							$rootScope.startLoader();
							var date = new Date();
							$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
							
							$scope.common.updatedBy=$scope.updatedBy,
							$scope.common.updatedDate=$scope.dt,
							$scope.common.status=status,
							$scope.common.incidentReportId=incidentReportId
							
							var URI = BASE_URL + ROOT_URL +"/api/unit/updateIncidentReportMasterStatus";
								console.log("ACT_INACT_URI",URI);
								GenericService.serviceAction("POST", URI, $scope.common).then(function(response) 
							{
								if (response.data.status == 'success')
								{
									$rootScope.stopLoader();
									growl.success(response.data.message,
											{
												title : 'Success!'
											});
									$scope.$broadcast('show-errors-reset');
									$scope.initCommon();
									$scope.selectedTemplateList1=[];
									$scope.selectedRoleList=[];
									$scope.storeList = [];
									$scope.tempSelectStore = [];
									$scope.initMasterList($scope.organisationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
									$rootScope.stopLoader();
								} else if(response.data.status == 'error')
								{
									$rootScope.stopLoader();
									growl.error(response.data.message,
											{
												title : 'Error!'
											});
								}
							});
						} catch (e) {
							console.log("Exception",e.message);
						}
						
					}
			//end update Status
					$scope.getInformation = function(info,type){
						if(type==1){
							$scope.listRoleByUserId = [];
							
							angular.forEach(info.incidentReportRoleMapperList,function(value,index){
									var neeArr = {};
									neeArr.roleId = value.roleId;
									neeArr.roleName = value.roleName;
									$scope.listRoleByUserId.push(neeArr);
								
			  				});
						}else{
							
							$scope.incidentReportTemplateMapperList1=[];
							$scope.incidentReportTemplateMapperList1=angular.copy(info.incidentReportTemplateMapperList);
						}
					}
			
			//code for show update 
			$scope.showUpdateBtn = function(id)
					{
						try {
							$rootScope.startLoader();
							$scope.saveBtnFlag = false;
							$scope.updateBtnFlag = true;
							$scope.incidentReportId = id;
							var data={
									 "incidentReportId" : id,
									 "organizationId":$scope.organisationId,
								     "unitId":$scope.unitId,
							}
							var URI = BASE_URL + ROOT_URL +"/api/unit/getIncidentReportMasterById";
								console.log("EDIT",URI);
								GenericService.serviceAction("POST", URI,data).then(function(response)  
							{
								console.log("data",response.data);
								
								if (response.data.status == 'success')
								{
									
									$rootScope.stopLoader();
									$scope.common = response.data.listObject[0];
//									$scope.common.code = response.data.listObject[0].code;
//									$scope.common.description = response.data.listObject[0].description;
									$scope.selectedTemplateList1 = [];
									$scope.selectedTemplateList = [];
									$scope.tempSelectedTemplateList = [];
									$scope.selectedRoleList = [];
									$scope.oldSelectedRoleList =[];
									$scope.selectedRoleListFinal =[];
									angular.forEach(response.data.listObject[0].incidentReportRoleMapperList, function( value, index) {
										var newAr = {};
										newAr.id = value.roleId;
										newAr.incidentReportRoleId = value.incidentReportRoleId;
										newAr.incidentReportId = value.incidentReportId;
										$scope.selectedRoleList.push(newAr);
										var newAr1 = {};
										newAr1.roleId = value.roleId;
										newAr1.incidentReportRoleId = value.incidentReportRoleId;
										newAr1.incidentReportId = value.incidentReportId;
										$scope.selectedRoleListFinal.push(newAr1);
										$scope.oldSelectedRoleList.push(newAr);
									});
									
									$scope.tempSelectedTemplateList = [];
									angular.forEach($scope.templateList,function(value,index){
										//$scope.templateList[index] = false;
										var i = $scope.tempSelectedTemplateList.indexOf(value);
										$scope.tempSelectedTemplateList.splice(i,1);
									})
									angular.forEach(response.data.listObject[0].incidentReportTemplateMapperList,function(value,index){
										var temp = {
													templateId:value.templateId,
													title:value.title,
													incidentReportTemplateId:value.incidentReportTemplateId,
													roleId:value.roleId,
													incidentReportId:value.incidentReportId
												};
//										$scope.incidentReportTemplateId=value.incidentReportTemplateId;
//										$scope.tempRoleId=value.roleId;
										$scope.tempSelectedTemplateList.push(temp);
										$scope.selectedTemplateList[value.templateId] = true;
										$scope.selectedTemplateList1.push(temp);
										console.log("$scope.selectedStoreList1111111",$scope.selectedStoreList1);
									});
									
								} else if(response.data.status == 'error')
								{
									$rootScope.stopLoader();
									alert("Error In Fetching Data");
								}
							});
						} catch (e) {
							console.log("Exception",e.message);
						}
						
					}
			//end show update
			
			$scope.clicked = function(type) {
				$scope.selectedRoleListFinal=[];
				var date = new Date();
				$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
				if (type == 'selectedRoleList') {
					if ($scope.selectedRoleList.length > 0) {
						angular.forEach($scope.selectedRoleList,function(value,key){
							var newArr = {};
							newArr.roleId=value.id;
							newArr.organizationId=$scope.organisationId,
							newArr.incidentReportRoleId=value.incidentReportRoleId,
							newArr.incidentReportId=value.incidentReportId,
							newArr.unitId=$scope.unitId,
							newArr.status='A',
							newArr.createdBy=$scope.createdBy,
							newArr.createdDate=$scope.dt,
							newArr.updatedBy=$scope.updatedBy,
							newArr.updatedDate=$scope.dt
							$scope.selectedRoleListFinal.push(newArr);
							console.log("$scope.selectedRoleList", $scope.selectedRoleList);
						});
					}
				
				}
			}
			
			//code for save user store mapper
			
			// function for popup ok button call
			$scope.PopupOkBtn = function() {
				$scope.popUpFlag = false;
				$scope.save();
			}
	
			$scope.save = function() 
					{
						$scope.$broadcast('show-errors-check-validity');
//						if ($scope.commonform.$valid)
//						{
							try {
								$rootScope.startLoader();
								var date = new Date();
								$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
								/*if($scope.popUpFlag==false)
								{
								  $rootScope.startLoader();
								}*/
								
								if($scope.common.incidentReportCode ==""){
									growl.error("Please Enter Code",
											{
												title : 'Warning!'
											});
									$rootScope.stopLoader();
									return false;
								}
								if($scope.common.incidentReportDesc ==""){
									growl.error("Please Enter Description",
											{
												title : 'Warning!'
											});
									$rootScope.stopLoader();
									return false;
								}
								if($scope.selectedRoleListFinal.length == undefined || $scope.selectedRoleListFinal==null || $scope.selectedRoleListFinal== ""){
									growl.error("Please Select Role",
											{
												title : 'Warning!'
											});
									$rootScope.stopLoader();
									return false;
								}
								
								if($scope.selectedTemplateList1.length == 0){
									growl.error("Please Select Template",
											{
												title : 'Warning!'
											});
									$rootScope.stopLoader();
									return false;
								}
								 var isUpdate = $scope.common.incidentReportId != null && $scope.common.incidentReportId !== '';

							        var counter = 0;
							        if (!isUpdate) {
							            angular.forEach($scope.commonList, function(value, index) {
							                if (value.incidentReportCode === $scope.common.incidentReportCode) {
							                    growl.error("Code already exists", {
							                        title: 'Warning!'
							                    });
							                    $rootScope.stopLoader();
							                    counter++;
							                }
							            });

							            if (counter > 0) {
							                return false;
							            }
							        }
							        
							        var count = 0;
							        if (!isUpdate) {
							            angular.forEach($scope.commonList, function(value, index) {
							                if (value.incidentReportDesc === $scope.common.incidentReportDesc) {
							                    growl.error("Description already exists", {
							                        title: 'Warning!'
							                    });
							                    $rootScope.stopLoader();
							                    count++;
							                }
							            });

							            if (count > 0) {
							                return false;
							            }
							        }

								var data =
								{
										
									 "incidentReportCode":$scope.common.incidentReportCode,
								     "incidentReportDesc":$scope.common.incidentReportDesc,
								     "organizationId":$scope.organisationId,
								     "unitId":$scope.unitId,
								     "status":'A',
									 "createdBy":$scope.createdBy,
									 "createdDate":$scope.dt,
									 "updatedBy":$scope.updatedBy,
									 "updatedDate":$scope.dt,
								     "incidentReportRoleMapperList":$scope.selectedRoleListFinal,
								     "incidentReportTemplateMapperList":$scope.selectedTemplateList1,
																	     
								 }
//								if ($scope.popUpFlag) {
//									$scope.showModal = !$scope.showModal;
//
//								} else {
								console.log("save data",JSON.stringify(data));
								var URI = BASE_URL + ROOT_URL + "/api/unit/saveIncidentReportMaster";
								GenericService.serviceAction("POST", URI, data).then(function(response) 
								{	
									if (response.data.status == 'success')
									{
										
										growl.success(response.data.message,
												{
													title : 'Success!'
												});
										$scope.$broadcast('show-errors-reset');
										$scope.initCommon();
										$scope.selectedTemplateList1=[];
										$scope.selectedTemplateList=[];
										$scope.selectedRoleList=[];
										$scope.tempSelectedTemplateList =[];
										$scope.storeList = [];
										$scope.tempSelectStore = [];
										$scope.initMasterList($scope.organisationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
										$rootScope.stopLoader();
									} else if(response.data.status == 'error')
									{
										$rootScope.stopLoader();
										growl.error(response.data.message,
												{
													title : 'Error!'
												});
									}
									
								});
//								}
							} catch (e) {
								console.log("Exception",e.message);
							}
							
//						}
					}
					
					//end save store mapper
			//code for update
			$scope.update = function() 
					{
						$scope.$broadcast('show-errors-check-validity');
//						if ($scope.commonform.$valid)
//						{
							try {
								$rootScope.startLoader();
								var date = new Date();
								$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
								if($scope.common.incidentReportCode ==""){
									growl.error("Please Enter Code",
											{
												title : 'Warning!'
											});
									$rootScope.stopLoader();
									return false;
								}
								if($scope.common.incidentReportDesc ==""){
									growl.error("Please Enter Description",
											{
												title : 'Warning!'
											});
									$rootScope.stopLoader();
									return false;
								}
								if($scope.selectedRoleListFinal.length == undefined|| $scope.selectedRoleListFinal==null || $scope.selectedRoleListFinal== ""){
									growl.error("Please Select Role",
											{
												title : 'Warning!'
											});
									$rootScope.stopLoader();
									return false;
								}
								
								if($scope.selectedTemplateList1.length == 0){
									growl.error("Please Select Template",
											{
												title : 'Warning!'
											});
									$rootScope.stopLoader();
									return false;
								}
								var data =
								{
										 "incidentReportId":$scope.incidentReportId,
										 "incidentReportCode":$scope.common.incidentReportCode,
									     "incidentReportDesc":$scope.common.incidentReportDesc,
									     "organizationId":$scope.organisationId,
									     "unitId":$scope.unitId,
									     "createdBy":$scope.createdBy,
										 "createdDate":$scope.dt,
									     "status":'A',
										 "updatedBy":$scope.userId,
										 "updatedDate":$scope.dt,
									     "incidentReportRoleMapperList":$scope.selectedRoleListFinal,
									     "incidentReportTemplateMapperList":$scope.selectedTemplateList1,
																		     
									 								     
								 }
								
								
							
								var URI = BASE_URL + ROOT_URL +"/api/unit/updateIncidentReportMaster";
							    GenericService.serviceAction("POST", URI, data).then(function(response) 
								{
									if (response.data.status == 'success')
									{
										$rootScope.stopLoader();
										growl.success(response.data.message,
												{
													title : 'Success!'
												});
										$scope.$broadcast('show-errors-reset');
										$scope.initCommon();
										$scope.selectedTemplateList1=[];
										$scope.selectedTemplateList=[];
										$scope.selectedRoleList=[];
										$scope.storeList = [];
										$scope.tempSelectedTemplateList =[];
										$scope.tempSelectStore = [];
										$scope.initMasterList($scope.organisationId, $scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
									} else if(response.data.status == 'error')
									{
										$rootScope.stopLoader();
										growl.error(response.data.message,
												{
													title : 'Error!'
												});
									}
								});
							} catch (e) 
							{
								console.log("Exception",e.message);
							}
							
//						}
					}
			//end update
			
			//code for select and add store
				$scope.tempSelectedTemplateList = [];
					$scope.addTempStore = function(templateId,title){
						
						var flag = false;
						angular.forEach($scope.tempSelectedTemplateList,function(value,index){
							if(value.templateId == templateId){
								$scope.tempSelectedTemplateList.splice(index,1);
								$scope.templateList[value.templateId] = false;
								flag = true;
							}
						})
						if(!flag){
							var temp = {
									     templateId:templateId,
									     title:title,
									     };
							$scope.tempSelectedTemplateList.push(temp);
						}
					}
					
					$scope.selectTemplate = function()
					{
						$scope.selectedTemplateList1=[];
						$scope.selectedTemplateList1 = angular.copy($scope.tempSelectedTemplateList);
					}
					
					
                    $scope.templateConfigObject = [];
                    $scope.getTemplateTypes = function(){
                        var obj = {};
                        obj.unitId = $scope.unitId;
                        var URI = BASE_URL + ROOT_URL + "/api/global/templateConfig";
                        BillingGenericService.serviceAction(METHOD_POST,URI,obj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
                            if (response.data.status == "success"){
                                 $scope.templateConfigObject = response.data.object;
                            }
                            else if(response.data.status == "error")
                            {
                                 $rootScope.stopLoader();    
                            }
                        });
                    }
                    $scope.getTemplateTypes();
                    
					$scope.addTemplate = function(template) {
						$scope.saveTrasactionFlag = false;
						$scope.updateTrasactionFlag = false;
						$scope.value ="";
						$scope.templateFormatId =template.templateFormatId;
						if(template.templateFormatId==1)
							{
							document.getElementById("htmlContent").innerHTML = JSON.parse(template.templateObject).html;
							}
						else if(template.templateFormatId==2)
							{
							 $scope.value = template.templateObject;
							}
                        else if(template.templateFormatId==3){

                            var landingUrl =  $scope.templateConfigObject.templateForm + template.formTemplateId +"&formType=digital";
                            window.open( landingUrl,  '_blank'  );
                       
                       }
						$('#TemplateInfoModal').modal('hide');
					}

					$scope.globalSearch = function() {
//					$scope.searchKeyword
    					if($scope.searchKeyword){
                            if($scope.searchKeyword.length >= 3){
                                $scope.masterList($scope.organisationId, 0, $scope.noOfRecordsPerPage,$scope.unitId);
                            }
                            else{
                                 $scope.commonList = $scope.onLoadCommonList;
                                 $scope.commonListCount = $scope.onLoadCommonListCount;
                                 $scope.setPage(1,false);
                            }
                        }
                        else{
                            $scope.searchKeyword = "";
                             $scope.masterList($scope.organisationId, 0, $scope.noOfRecordsPerPage,$scope.unitId);
                        }
					}
			
}
function config($stateProvider) {
	$stateProvider.state('incidentMaster', {
		url: '/incidentMaster',
		templateUrl: 'views/incidentReporting/incident-master.html',
		controller: "incidentMasterController", 
		title: 'Incident Master',
	})
}

angular
.module("myApp")
.config(config)
.controller("incidentMasterController", incidentMasterController);
})();