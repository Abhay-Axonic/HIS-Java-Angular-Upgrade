angular
		.module('myApp')
		.controller(
				'donorScreeningController',
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
						'PagerService',
						function($scope, $rootScope, $state, $http,
								GenericService,$sessionStorage,$cookies, growl, $localStorage , PagerService) {
							
							$scope.saveBtnFlag = true;
							var cookieObject = $cookies.getObject('cookieObject');
							if(cookieObject == undefined){
								$state.go('login');
								return;
							}
							$rootScope.loginpage = true;
							$scope.unitId = cookieObject.unitId;
							$scope.organizationId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							$scope.parameter='';
							$scope.deferredTypeId='';
							$scope.selectedGenderList = [];
							$scope.offset = 0;
							$scope.noOfRecordsPerPage = 10;
							
							/**List And Pagination Start**/
							$scope.setNoOfRecords = function() {
								$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
							};
							$scope.getDonorScreeningList = [];
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
								
								var URI1 = BASE_URL + ROOT_URL + "/global/bloodBank/getAllDonorScreeningListPaginationList"; 
								var URI2 = BASE_URL + ROOT_URL + "/global/bloodBank/getAllDonorScreeningListCount";
									
								
								GenericService.serviceAction("POST",URI1,data1).then(function(response){
									if(response.data.status == "success"){
									$rootScope.stopLoader();					
									$scope.getDonorScreeningList = response.data.listObject;
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
								
								var URI = BASE_URL + ROOT_URL +  "/global/bloodBank/getAllDonorScreeningListPaginationList";
								console.log("URI", URI);
								GenericService.serviceAction("POST", URI,data).then(function(response) {
									$rootScope.stopLoader();
									$scope.getDonorScreeningList = response.data.listObject;
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
							
							
							$scope.getGenderList = function(){
							var data = {};
							var URI = BASE_URL + ROOT_URL+ GETACTIVEGENDERLIST;
							GenericService.serviceAction(METHOD_GET, URI, data,NOTIFICATION_MSG_STATUS_FALSE)
							.then(function(response) {
								$scope.getGenderList = [];
										if (response.data.status == "success")
											angular .forEach(response.data.listObject, function( value, index) {
												var newAr = {};
												newAr.id = value.id;
												newAr.label = value.desc;
												$scope.getGenderList.push(newAr);
												
											});
										console.log("$scope.genderList",$scope.getGenderList);
									});
							
							}
							$scope.getGenderList();
							
							
							$scope.getDefferedTypeList = [];
							$scope.getDefferedType = function(){
								var data = {};
								var URI = BASE_URL + ROOT_URL+ "/global/bloodBank/getDefferedTypeMaster";
								GenericService.serviceAction(METHOD_GET, URI, data,NOTIFICATION_MSG_STATUS_FALSE)
								.then(function(response) {
											if (response.data.status == "success")
												$scope.getDefferedTypeList = response.data.listObject;
												
											console.log("$scope.genderList",$scope.getDefferedTypeList );
										});
								
								}
							
							$scope.getDefferedType();
							
						/*	$scope.getDonorScreeningList = [];
							$scope.getDonorScreening = function(){
								
								var data = {	
										organizationId:$scope.organizationId,
										unitId:	$scope.unitId
									};
								$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL+ "/global/bloodBank/getAllDonorScreeningList";
								GenericService.serviceAction("POST", URI, data,NOTIFICATION_MSG_STATUS_FALSE)
								.then(function(response) {
									
											if (response.data.status == "success")
											$scope.getDonorScreeningList = response.data.listObject;
											$rootScope.stopLoader();
											console.log("$scope.genderList",$scope.getDonorScreeningList);
										});
								
								}
								$scope.getDonorScreening();
								*/
								
							
							$scope.savedonorScreening=function(){
								
								if($scope.parameter == undefined || $scope.parameter == ""){
									growl.error("Please Enter Parameter ",
											{
												title : 'error'
											});
									return false;
								}
								
								if($scope.deferredTypeId == undefined || $scope.deferredTypeId == ""){
									growl.error("Please Select Deferral Type ",
											{
												title : 'error'
											});
									return false;
								}
								
								if($scope.selectedGenderList == undefined || $scope.selectedGenderList.length == 0){
									growl.error("Please Select Gender ",
											{
												title : 'error'
											});
									return false;
								}
								
								$scope.obj = {
										
										organizationId:$scope.organizationId,
										unitId:	$scope.unitId,
										parameter:$scope.parameter,
										deferredTypeId:$scope.deferredTypeId,
										createdBy:$scope.userId,
										updatedBy:$scope.userId,
										createdDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
										updatedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
										status:"A",
										genderList:$scope.selectedGenderList
								};
							
								console.log("$scope.$scope.common ",$scope.obj);
								
							
								var URI = BASE_URL + ROOT_URL + "/global/bloodBank/saveDonorScreenigMaster";
							
								GenericService.serviceAction("POST",URI,$scope.obj).then(function(response){
									
									if (response.data.status == 'success')
									{
										$rootScope.stopLoader();
										$scope.getDonorScreening();
										growl.success(response.data.message,
												{
													title : 'Success!'
												});
										$scope.parameter='';
										$scope.deferredTypeId='';
										$scope.selectedGenderList=[];
									//	$state.go('donorScreeningWorklist');
									} else if(response.data.status == 'error')
									{
										$rootScope.stopLoader();
										growl.error(response.data.message,
												{
													title : 'Error!'
												});
									}
								});
								
								
							}
							
								$scope.updateStatus = function(Id,status){
									try{
										var date = new Date();
										$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
										var data={
												updatedBy:$scope.userId,
												updatedDate:$scope.dt,
												status:status,
												id:Id
										};
										$rootScope.startLoader();
										var URI = BASE_URL + ROOT_URL + "/global/bloodBank/updateDonorScreeningMasterStatus";
										console.log("update status",data);
										GenericService.serviceAction("POST",URI,data).then(function(response){
											if (response.data.status == 'success')
											{
												$rootScope.stopLoader();
												$scope.getDonorScreening();
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
								
								$scope.Id='';
								$scope.showUpdateBtn = function(info){
									try{
										
										$scope.parameter=info.parameter;
										$scope.deferredTypeId=info.deferredTypeId;
										$scope.Id=info.id
										var newAr = {};
										newAr.id = info.genderId;
										newAr.label = info.gender;
										$scope.selectedGenderList.push(newAr);
										$scope.getDefferedTypeList.push({ deferredTypeId: info.deferredTypeId, defferedTypeName: info.defferedTypeName });
		 								$scope.saveBtnFlag = false;
										$scope.updateBtnFlag = true;
									
									}catch(e){
										console.log("Exception-",e);
									}
								}
								
								
								
								$scope.updateDonorScreening = function(){
									try{
									
										if($scope.parameter == undefined || $scope.parameter == ""){
											growl.error("Please Enter Parameter ",
													{
														title : 'error'
													});
											return false;
										}
										
										if($scope.deferredTypeId == undefined || $scope.deferredTypeId == ""){
											growl.error("Please Select Deferral Type ",
													{
														title : 'error'
													});
											return false;
										}
										
										if($scope.selectedGenderList == undefined || $scope.selectedGenderList.length == 0){
											growl.error("Please Select Gender ",
													{
														title : 'error'
													});
											return false;
										}
										
										$scope.data = {
												id:$scope.Id,
												organizationId:$scope.organizationId,
												unitId:	$scope.unitId,
												parameter:$scope.parameter,
												deferredTypeId:$scope.deferredTypeId,
												createdBy:$scope.userId,
												updatedBy:$scope.userId,
												createdDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
												updatedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
												status:"A",
												genderList:$scope.selectedGenderList
										};
										  	$rootScope.startLoader();
											var URI = BASE_URL + ROOT_URL +"/global/bloodBank/updateDonorScreeningMaster";
										GenericService
											.serviceAction(METHOD_POST, URI, $scope.data,
													NOTIFICATION_MSG_STATUS_FALSE)
											.then(
													function(response) {
														console.log(response);
														
														if (response.data.status == "success"){
															$rootScope.stopLoader();
															$scope.getDonorScreening();
															$scope.parameter='';
															$scope.deferredTypeId='';
															$scope.Id='';
															$scope.selectedGenderList=[];
														growl.success(response.data.message,
																{
																	title : 'Success!'
																});
														}
														else{
															$rootScope.stopLoader();
															growl.success(response.data.message,
																	{
																		title : 'error!'
																	});
														}
													});
											
										
									}catch(e){
										console.log("Exception-",e);
									}
								}
								
			}]);
							