angular
		.module('myApp')
		.controller(
				'checklistMasterController',
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
                        'PagerService',
						function($scope, $rootScope, $state, $http,
								GenericService,$sessionStorage,$cookies, growl, $localStorage,BillingGenericService,PagerService) {
							
							var cookieObject = $cookies.getObject('cookieObject');
							if(cookieObject == undefined){
								$state.go('login');
								return;
							}
							$scope.saveBtnFlag = true;
							$rootScope.loginpage = true;
							$scope.unitId = cookieObject.unitId;
							$scope.organizationId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							
                            /** Variables for pagination */
                            $scope.offset = 0;
                            $scope.noOfRecordsPerPage = 10;
                            $scope.commonList = [];
                            $scope.totalNoOfRecords;
                            $scope.searchKeyword;
                            $scope.commonListCount;

                            
							$scope.init = function(){
								
								$scope.checklistObj ={
										"checklistMasterId":null,
										"code":"",
										"description":"",
										"roleId":"",
										"stageId":"",
										"isWho":"",
										"checklistMasterDetails":[],
										"createdBy":$scope.userId,
										"createdDate":moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
										"status":"A",
										"updatedBy":$scope.userId,
										"updatedDate":moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
										"unitId":$scope.unitId ,
										"organizationId":$scope.organizationId
								}
								
								$scope.checklistDetailsObj ={
										"checklistMasterDetailsId":null,
										"checklistItemMasterId":"",
										"description":"",
										"createdBy":$scope.userId,
										"createdDate":moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
										"status":"A",
										"updatedBy":$scope.userId,
										"updatedDate":moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
										"unitId":$scope.unitId ,
										"organizationId":$scope.organizationId
								}
								
							}
			
							$scope.init();
							$scope.otimsRoleList = [];
							$scope.otimsRole = function(organisationId,offset, noOfRecordsPerPage,unitId) {
								$rootScope.startLoader();
								var data = {
										organisationId:$scope.organizationId,
										unitId:$scope.unitId
										};
								
								var URI = BASE_URL + ROOT_URL + "/api/global//getActiveOTRoleList";
								GenericService.serviceAction("POST", URI, data).then(function(response) {
									$rootScope.stopLoader();
									$scope.otimsRoleList = response.data.listObject;
									console.log("otimsRoleList",$scope.otimsRoleList);
								});
							}
							
							$scope.otimsRole();
							$scope.checklistList = [];
                            $scope.getChecklistMasterList = function(organizationId,offset, noOfRecordsPerPage, unitId){
                                $rootScope.startLoader();
                                offset = offset != null ? offset : 0;
                                noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage:10;
                            	
                            	var data = {
										unitId:unitId,
										organizationId:organizationId,
										offset : offset,
										noOfRecordsPerPage : noOfRecordsPerPage
										};								
								var URI = BASE_URL + ROOT_URL+ "/global/otims/getAllChecklistMasterList";
								BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE)
								.then(function(response) {
											$scope.checklistList = [];
											if (response.data.status == "success"){
												$rootScope.stopLoader();
													$scope.checklistList = response.data.listObject;
                                                    $scope.commonListCount = response.data.object;
                                                    $scope.setPage(1,false);

											}
										});
						}
						$scope.getChecklistMasterList($scope.organizationId,$scope.offset,$scope.noOfRecordsPerPage,$scope.unitId);
							
							
							 $scope.getCheckItemListByName = function(name){
									var URI = BASE_URL + ROOT_URL + "/global/otims/searchChecklistItemName";
									var data={
											description : name,
											organizationId : $scope.organizationId,
											unitId : $scope.unitId
									};
									
								return GenericService.serviceAction(METHOD_POST,URI,data).then(function(response){
										if (response.data.status == "success")
											return response.data.listObject;
										});
							   }
							
							
							
							
							$scope.selectCheckItem = function(info){
								
								$scope.checklistDetailsObj.checklistItemMasterId = info.checklistItemMasterId;
								$scope.checklistDetailsObj.description = info.description;
								
							}
							$scope.checkListItemNameList = [];
							$scope.addChecklistItemName  =function(){
								
								if($scope.checklistDetailsObj.description != ""){
									
									var newdata ={};
									
									newdata.checklistItemMasterId = $scope.checklistDetailsObj.checklistItemMasterId;
									newdata.description = $scope.checklistDetailsObj.description;
									newdata.checklistMasterDetailsId = $scope.checklistDetailsObj.checklistMasterDetailsId;
									newdata.createdBy=$scope.userId;
									newdata.createdDate=moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
									newdata.status="A";
									newdata.updatedBy=$scope.userId;
									newdata.updatedDate=moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
									newdata.unitId=$scope.unitId ;
									newdata.organizationId=$scope.organizationId;
									$scope.checkListItemNameList.push(newdata);
								}
								$scope.checklistDetailsObj.description ="";
								$scope.checklistDetailsObj.checklistItemMasterId = "";
							}
							
							$scope.remove = function(info1){
								
								$scope.checkListItemNameList.splice(info1,1);
								
							}
							
							$scope.saveCheckListMaster = function(){
								$rootScope.startLoader();
								if($scope.checklistObj.code==""){
									$rootScope.stopLoader();
									growl.warning("Please select code",
											{
												title : 'warning!'
											});
								}else if($scope.checklistObj.description==""){
									$rootScope.stopLoader();
									growl.warning("Please select description",
											{
												title : 'warning!'
											});
								}else if($scope.checklistObj.roleId==""){
									$rootScope.stopLoader();
									growl.warning("Please select Role",
											{
												title : 'warning!'
											});
								}else if($scope.checkListItemNameList.length==0){
									$rootScope.stopLoader();
									growl.warning("Please select Checklist name",
											{
												title : 'warning!'
											});
								}else{
								
								$scope.checklistObj.checklistMasterDetails = $scope.checkListItemNameList;
								var URI = BASE_URL + ROOT_URL +"/global/otims/saveChecklistMaster";
								console.log("$scope.checklistObj", 	$scope.checklistObj);
								GenericService.serviceAction("POST", URI,$scope.checklistObj).then(function(response){
									if (response.data.status == 'success')
									{	$rootScope.stopLoader();
										$scope.init();
										$scope.getChecklistMasterList($scope.organizationId,$scope.offset,$scope.noOfRecordsPerPage,$scope.unitId);
										$scope.checkListItemNameList =[];
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
							}
							
							$scope.updateStatus = function(checklistMasterId,status){
								try{
									var date = new Date();
									$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
									var data={
											updatedBy:$scope.userId,
											updatedDate:$scope.dt,
											status:status,
											checklistMasterId :checklistMasterId,
									};
									$rootScope.startLoader();
									var URI = BASE_URL + ROOT_URL + "/global/otims/updateChecklistMasterStatus";
									console.log("update status",data);
									GenericService.serviceAction("POST",URI,data).then(function(response){
										if (response.data.status == 'success')
										{
											$scope.getChecklistMasterList($scope.organizationId,$scope.offset,$scope.noOfRecordsPerPage,$scope.unitId);
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
							
						
							$scope.checklistNameList = [];
							$scope.getChecklistNameListById = function(info,label){
								$rootScope.startLoader();
								var data = {
										unitId:$scope.unitId,
										organizationId:$scope.organizationId,
										checklistMasterId:info.checklistMasterId
										};								
								var URI = BASE_URL + ROOT_URL+ "/global/otims//getChecklistMasterDetailsByMasterId";
								BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE)
								.then(function(response) {
											$scope.checklistNameList = [];
											if (response.data.status == "success"){
												$rootScope.stopLoader();
													$scope.checklistNameList = response.data.listObject;
													if(label==2){
														$scope.checkListItemNameList  = response.data.listObject;
													}
													console.log("$scope.checklistNameList", 	$scope.checklistNameList);
											}
										});
						}
								
							$scope.update = function(info,label){
							
								if(info!=null){
									
									$scope.getChecklistNameListById(info,label);
									
									$scope.checklistObj.checklistMasterId =info.checklistMasterId
									
									$scope.checklistObj.code = info.code;
									
									$scope.checklistObj.description = info.description;
									
									$scope.checklistObj.status = info.status;
									
									$scope.checklistObj.roleId = info.roleId;
									
									$scope.checklistObj.isWho = info.isWho;
									$scope.checklistObj.stageId = info.stageId;
							}
							
						}	
							
			          $scope.masterList = function(organizationId,offset, noOfRecordsPerPage, unitId) {
			        	offset = offset != null ? offset : 0;
	                    noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage: 10;

	                    $rootScope.startLoader();
	                    offset = offset != null ? offset : 0;
	                    noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage:10;
	                    
	                    var data = {
	                            unitId:unitId,
	                            organizationId:organizationId,
	                            offset : offset,
	                            noOfRecordsPerPage : noOfRecordsPerPage,
	                            
	                            };                                
						var URI = BASE_URL + ROOT_URL+ "/global/otims/getAllChecklistMasterList";
						BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE)
						.then(function(response) {
									$scope.checklistList = [];
									if (response.data.status == "success"){
										$rootScope.stopLoader();
											$scope.checklistList = response.data.listObject;
									}
								});
	                }
			                
			        $scope.setNoOfRecords = function() {
                       $scope.getChecklistMasterList($scope.organizationId,$scope.offset,$scope.noOfRecordsPerPage,$scope.unitId);
                    };
       
	                $scope.pager = {};
	                $scope.page;

	                $scope.setPage = function(page, flag) {
	                    if (page < 1 || page > $scope.pager.totalPages) {
	                        return;
	                    }
	                    $scope.pager = PagerService.GetPager($scope.commonListCount, page,$scope.noOfRecordsPerPage,$scope.unitId);
	                    if (flag) {
	                            $scope.masterList($scope.organizationId,$scope.pager.startIndex,$scope.pager.pageSize,$scope.unitId);
	                    }
	                }

			}]);
							