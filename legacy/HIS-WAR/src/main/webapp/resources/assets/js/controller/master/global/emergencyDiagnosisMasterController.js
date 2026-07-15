
/**
 * sneha dalave
 */


(function () {
	  "use strict";

		function emergencyDiagnosisMasterController($scope,$rootScope,$state, $cookies, GenericService,PagerService,promiseFactory,$sessionStorage,growl,BillingGenericService){
			$rootScope.loginpage = true;
			$scope.saveBtnFlag = true;
			$scope.updateBtnFlag = false;
			$scope.updateScope = false;
			$scope.popUpFlag = true;
	
			/**Variables for pagination */
			$scope.offset = 0;
			$scope.noOfRecordsPerPage = 10;
			$scope.commonList = [];
			$scope.totalNoOfRecords;
			$scope.searchKeyword;
			$scope.commonListCount;
			$scope.selectedGenderList=[];

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
			
			
			$scope.initMaster = function(){
				$scope.saveBtnFlag = true;
				$scope.updateBtnFlag = false;
				$scope.common = 
				{
						diagnosisMapperId : '',
						diagnosisId:'',
						updatedBy:$scope.userId,
						createdBy:$scope.userId,
						diagnosisCode:'',
						diagnosisName:'',
				};
				var data = {};
				var commonObj = {
					unitId : $scope.unitId,
					organizationId : $scope.organizationId
				}
			}
			$scope.initMaster();
			
			
			/**Crud Operations Start**/
		
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
							};
				
				var data2 = {
						      organizationId:organizationId
							};
				var URI1 = BASE_URL + ROOT_URL + "/api/opd/getListOfEmergencyDiagnosisMaster"; 
				var URI2 = BASE_URL + ROOT_URL + "/api/opd/getEmergencyDiagnosisMasterCount";
					
				
				GenericService.serviceAction("POST",URI1,data1).then(function(response){
					if(response.data.status == "success"){
					$rootScope.stopLoader();					
					$scope.commonList = response.data.listObject;
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
//						unitId:unitId
						};
				
				var URI = BASE_URL + ROOT_URL + "/api/opd/getListOfEmergencyDiagnosisMaster";
				console.log("URI", URI);
				GenericService.serviceAction("POST", URI,data).then(function(response) {
					$rootScope.stopLoader();
					$scope.commonList = response.data.listObject;
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
			
			// function for popup ok button call
			$scope.PopupOkBtn = function() {
				$scope.popUpFlag = false;
				$scope.save();
			}
			
			// Function for save System Type
			$scope.save = function() {
				
				if($scope.common.diagnosisId != ""){
					if ($scope.popUpFlag) {
						$scope.showModal = !$scope.showModal;

					} else {
						
						
							var date = new Date();
							$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');	
							var data={
									diagnosisId:$scope.common.diagnosisId,
									updatedBy:$scope.updatedBy,
									updatedDate:$scope.dt,
									createdDate:$scope.dt,
									createdBy:$scope.createdBy,
									organizationId:$scope.organizationId,
									unitId:$scope.unitId,
									status:'A',
							}
							
								     
							$rootScope.startLoader();
							var URI = BASE_URL + ROOT_URL + "/api/opd/saveEmergencyDiagnosisMaster";
							
							GenericService.serviceAction("POST", URI,data)
							.then(
									function(response) {
										console.log(response);
										
										if (response.data.status == 'success')
										{
											growl.success(response.data.message,
													{
														title : 'Success!'
													});
											
											$scope.$broadcast('show-errors-reset');
											$scope.initMaster();
											$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
											$scope.common.diagnosisName="";
										} else if(response.data.status == 'error')
										{
											growl.error(response.data.message,
													{
														title : 'Error!'
													});
										}
									});
									$rootScope.stopLoader();
																		
						}
				}else{
					growl.warning("Please select diagnosis",
							{
						       title : 'warning!'
							});
				}
			}
			$scope.searchGroupListDesc= function(keyword) {
				
				var data = {
						diagnosisName : keyword,
				};
				var URI = BASE_URL + ROOT_URL
						+ "/api/opd/searchDiagnosisByCodeOrDescription";
				
				return GenericService
						.serviceAction("POST", URI, data)
						.then(
								function(response) {
									console.log(response);
									if (response.data.status == "success")
										return response.data.listObject;
								});

			}
			

			$scope.showUpdateBtn = function(info){
				$scope.selectedGenderList=[];
				try{
					$scope.saveBtnFlag = false;
					$scope.updateScope = true;
					$scope.updateBtnFlag = true;
					$rootScope.startLoader();
					var data={
							organizationId:$scope.organizationId,
							emergencyDiagnosisMapperId:info.emergencyDiagnosisMapperId
					}
					var URI = BASE_URL + ROOT_URL + "/api/opd/getEmergencyDiagnosisMasterById";
					
					return GenericService.serviceAction("POST",URI,data).then(function(response){
							if (response.data.status == "success"){
								$scope.common.diagnosisId=response.data.listObject[0].diagnosisId;
								$scope.common.diagnosisName=response.data.listObject[0].diagnosisName +"|"+ response.data.listObject[0].code;
								$scope.emergencyDiagnosisMapperId = info.emergencyDiagnosisMapperId;
							}
							$rootScope.stopLoader();
						});
					
				}catch(e){
					console.log("Exception-",e);
				}
			}
			
			$scope.update = function() {
				
						var date = new Date();
						$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');	
						var data={
								emergencyDiagnosisMapperId:$scope.emergencyDiagnosisMapperId,
								diagnosisId:$scope.common.diagnosisId,
								updatedBy:$scope.updatedBy,
								updatedDate:$scope.dt,
								organizationId:$scope.organizationId,
								unitId:$scope.unitId,
								status:'A',
						}
						
							     
						$rootScope.startLoader();
						var URI = BASE_URL + ROOT_URL + "/api/opd/updateEmergencyDiagnosisMaster";
						
						GenericService.serviceAction("POST", URI,data)
						.then(
								function(response) {
									console.log(response);
									if (response.data.status == "success")
										{
										growl.success("Updated Successfully",{
												title : 'Success!'
													
										});
										$rootScope.stopLoader();
										$scope.initMaster();
										$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
										$scope.common.diagnosisName="";
										$scope.emergencyDiagnosisMapperId="";
									    $scope.common.diagnosisId="";
										}	 else if(response.data.status == 'error')
										{
											growl.error(response.data.message,
													{
														title : 'Error!'
													});
											$rootScope.stopLoader();
										}											
								});											
//					}
//				}

			}

			$scope.searchGroupListCode= function(keyword) {
				
				var data = {
						diagnosisCode : keyword
				};
				var URI = BASE_URL + ROOT_URL
						+ "/api/opd/searchDiagnosisByCodeOrDescription";
				
				return GenericService
						.serviceAction("POST", URI, data)
						.then(
								function(response) {
									console.log(response);
									// $rootScope.startLoader();
									if (response.data.status == "success")
										return response.data.listObject;
								});

			}
			
			

			$scope.selectDignosisIdForDescription = function(info, model,
					label,ind) {
				console.log(info);
				$scope.common.diagnosisId = info.diagnosisId;
			}
			
			$scope.updateStatus = function(Id,status){
				try{
					var date = new Date();
					$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
					$rootScope.startLoader();
					var data={
							updatedBy:$scope.updatedBy,
							updatedDate:$scope.dt,
							organizationId:$scope.organizationId,
							unitId:$scope.unitId,
							status:status,
							emergencyDiagnosisMapperId:Id
					}
					var URI = BASE_URL + ROOT_URL + "/api/opd/updateEmergencyDiagnosisMasterStatus";
					console.log("update status",data);
					GenericService.serviceAction("POST",URI,data).then(function(response){
						if (response.data.status == 'success')
						{
							$rootScope.stopLoader();
							growl.success(response.data.message,
									{
										title : 'Success!'
									});
//							$scope.$broadcast('show-errors-reset');
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
			
			

			/**Crud Operations End**/
}
	function config($stateProvider) {
						    $stateProvider.state("emergencyDiagnosisMaster",
						    		{
						   	 url: '/emergencyDiagnosisMaster',
							 templateUrl: 'views/master/GlobalMaster/emergency-diagnosis-master.html',
							 controller: "emergencyDiagnosisMasterController",
							 title: 'Emergency Diagnosis Mapper',
                             });	
							  }
							
							  angular
							    .module("myApp")
							    .config(config)
							    .controller("emergencyDiagnosisMasterController", emergencyDiagnosisMasterController);
							})();

	
	