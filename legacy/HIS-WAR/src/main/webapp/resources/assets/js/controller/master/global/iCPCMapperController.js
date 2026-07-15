
/**
 * 
 */

(function () {
  "use strict";
function iCPCMapperController($scope,$rootScope,$state, $cookies, GenericService,PagerService,promiseFactory,$sessionStorage,growl,BillingGenericService){
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
						icpcSystemMapperId : '',
						icpcSystemId:'',
						genderId:'',
						icpcSystemDesc:''
				};
				$scope.genderList=[];
				$scope.selectedGenderList =[];
				var data = {};
				var commonObj = {
					unitId : $scope.unitId,
					organizationId : $scope.organizationId
				}

				//var URI = BASE_URL + ROOT_URL+ "/api/opd/getActiveGenderListForICPCMapper";
				var URI = BASE_URL + ROOT_URL
				+ GETACTIVEGENDERLIST;
	
		BillingGenericService
				.serviceAction(METHOD_GET, URI,
						commonObj,data,
						NOTIFICATION_MSG_STATUS_FALSE)
				.then(
						function(response) {
							console.log(response); 
							// $rootScope.startLoader();
							if (response.data.status == "success")
								///$scope.genderList = response.data.listObject;
							console.log("GEN",$scope.genderList);
							angular.forEach(response.data.listObject,
									function(value,index) {
										var data = {};
										data.id = value.id;
										data.label = value.desc;
										$scope.genderList.push(data);
									});
						});
//		
		
				console.log("Common-init",$scope.common);
			}
			$scope.initMaster();
			
			//start for ICPC System list
			$scope.icpcSystemList = [];
			$scope.getIcpcSystemList = function(){
			var URI = BASE_URL + ROOT_URL + "/api/opd/getICPCSYSTEMList";
		
			GenericService.serviceAction(METHOD_GET, URI).then(function(response) {
				if(response.data.status == "success"){
					$scope.icpcSystemList = response.data.listObject;
					console.log("$scope.icpcSystemList",$scope.icpcSystemList);
				}
			});
			//end for icpc system list
			}
		$scope.getIcpcSystemList();
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
//							unitId:unitId
							};
				
				var data2 = {
						      organizationId:organizationId
//						      unitId:unitId
							};
							var URI1 = BASE_URL + ROOT_URL + "/api/opd/getICPCMapperList"; 
							var URI2 = BASE_URL + ROOT_URL + "/api/opd/getICPCMapperCount";
					
				
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
				
				var URI = BASE_URL + ROOT_URL + "/api/opd/getICPCMapperList";
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
				
//				if ($scope.systemMasterForm.$valid) {
					
					if ($scope.popUpFlag) {
						$scope.showModal = !$scope.showModal;

					} else {
						
						if($scope.common.icpcSystemId == undefined || $scope.common.icpcSystemId == null || $scope.common.icpcSystemId ==""){
							growl.error("Please Select ICPC System",
									{
										title : 'error!'
									});
							return false;
						}
						if($scope.selectedGenderList == undefined || $scope.selectedGenderList == null || $scope.selectedGenderList.length ==0){
							growl.error("Please Select Gender",
									{
										title : 'error!'
									});
							return false;
						}
						var date = new Date();
						$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');	
						var data={
								icpcSystemId:$scope.common.icpcSystemId,
								updatedBy:$scope.updatedBy,
								updatedDate:$scope.dt,
								createdDate:$scope.dt,
								createdBy:$scope.createdBy,
								organizationId:$scope.organizationId,
								unitId:$scope.unitId,
								status:'A',
								genderList:$scope.selectedGenderList
						}
						
							     
						$rootScope.startLoader();
						var URI = BASE_URL + ROOT_URL + "/api/opd/saveICPCMapper";
						
						GenericService.serviceAction("POST", URI,data)
						.then(
								function(response) {
									console.log(response);
									// $rootScope.startLoader();
									
									if (response.data.status == 'success')
									{
										growl.success(response.data.message,
												{
													title : 'Success!'
												});
										
										$scope.$broadcast('show-errors-reset');
										$scope.initMaster();
										$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
										$scope.common.genderIdList=[];
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
//				}

			}
			$scope.clicked = function(gender) {
				
				if (gender == 'selectedGenderList') {
					
					$scope.selectedList =[];
					if ($scope.selectedGenderList.length > 0) {
						var selectedGenders = [];
						angular.forEach($scope.selectedGenderList,
								function(value, index) {
							selectedGenders.push(value.id);
								});
						var data = {
								
							genderList : selectedGenders
						}
					}
				}
			}
			

			$scope.oldDig = "";
			$scope.showUpdateBtn = function(info){
				$scope.selectedGenderList=[];
				try{
					$scope.saveBtnFlag = false;
					$scope.updateScope = true;
					$scope.updateBtnFlag = true;
					$rootScope.startLoader();
					var data={
							organizationId:$scope.organizationId,
							icpcSystemMapperId:info.icpcSystemMapperId
					}
				
					var URI = BASE_URL + ROOT_URL + "/api/opd/getICPCMapperById";
					
					return GenericService.serviceAction("POST",URI,data).then(function(response){
							if (response.data.status == "success"){
								$scope.common.icpcSystemId=response.data.listObject[0].icpcSystemId;
								$scope.common.icpcSystemDesc=response.data.listObject[0].icpcSystemDesc;
								$scope.common.icpcSystemMapperId=response.data.listObject[0].icpcSystemMapperId;
								$scope.selectedListOfGender=response.data.listObject;
								
								angular.forEach($scope.selectedListOfGender, function( value, index) {
									angular.forEach(value.genderList, function( value1, index1) {
										var newAr = {};
										newAr.id = value1.genderId;
		//								newAr.label = value.genderDesc;
										$scope.selectedGenderList.push(newAr);
										console.log("result",$scope.selectedGenderList)
									});
								});
							}
							$scope.oldDig = $scope.common.diagnosisId;
							$rootScope.stopLoader();
						});
					
				}catch(e){
					console.log("Exception-",e);
				}
			}
			
			$scope.update = function() {
				
//				if ($scope.systemMasterForm.$valid) {
					
//					if ($scope.popUpFlag) {
//						$scope.showModal = !$scope.showModal;
//
//					} else {
				if($scope.common.icpcSystemId == undefined || $scope.common.icpcSystemId == null || $scope.common.icpcSystemId ==""){
					growl.error("Please Select ICPC System",
							{
								title : 'error!'
							});
					return false;
				}
				if($scope.selectedGenderList == undefined || $scope.selectedGenderList == null || $scope.selectedGenderList.length ==0){
					growl.error("Please Select Gender",
							{
								title : 'error!'
							});
					return false;
				}
						var date = new Date();
						$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');	
						var data={
								icpcSystemMapperId:$scope.common.icpcSystemMapperId,
								icpcSystemId:$scope.common.icpcSystemId,
								updatedBy:$scope.updatedBy,
								updatedDate:$scope.dt,
								organizationId:$scope.organizationId,
								unitId:$scope.unitId,
								status:'A',
								genderList:$scope.selectedGenderList
						}
						
							     
						$rootScope.startLoader();
						var URI = BASE_URL + ROOT_URL + "/api/opd/updateICPCMapper";
						
						GenericService.serviceAction("POST", URI,data)
						.then(
								function(response) {
									console.log(response);
									// $rootScope.startLoader();
									if (response.data.status == "success")
										{
										growl.success("Updated Successfully",{
												title : 'Success!'
													
										});
										$rootScope.stopLoader();
										$scope.initMaster();
										$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
										$scope.common.genderIdList=[];
										
										}												
								});											
//					}
//				}

			}

			
			$scope.getGenderListByICPCSystemId= function(info) {
				
				var data = {
						icpcSystemMapperId : info.icpcSystemMapperId,
						organizationId:$scope.organizationId
				};
				var URI = BASE_URL + ROOT_URL
						+ "/api/opd/getGenderListByICPCSystemId";
				
				return GenericService
						.serviceAction("POST", URI, data)
						.then(
								function(response) {
									console.log(response);
									// $rootScope.startLoader();
									if (response.data.status == "success")
										$scope.listOfGenderByDiagnosisId=response.data.listObject;
										return response.data.listObject;
								});

			}
			

			$scope.selectDignosisIdForDescription = function(info, model,
					label,ind) {
				console.log(info);
				$scope.common.diagnosisId = info.diagnosisId;
			}
			
			$scope.updateStatus = function(id,status){
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
							icpcSystemMapperId:id
					}
					var URI = BASE_URL + ROOT_URL + "/api/opd/updateICPCMapperStatus";
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
    $stateProvider.state('ICPCMapper', { 
		 url: '/ICPCMapper',
		 templateUrl: 'views/master/GlobalMaster/ICPC-mapper.html',
		 controller: "iCPCMapperController",
		 title: 'ICPC Mapper',
	})
  }

  angular
    .module("myApp")
    .config(config)
    .controller("iCPCMapperController", iCPCMapperController);
})();