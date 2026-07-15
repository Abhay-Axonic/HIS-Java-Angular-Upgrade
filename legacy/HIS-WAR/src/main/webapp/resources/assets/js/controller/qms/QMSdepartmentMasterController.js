/**
 * 
 */

angular.module('myApp')
		.controller("QMSdepartmentMasterController",
		['$scope','$rootScope','$state','$cookies','GenericService','PagerService','promiseFactory','$sessionStorage','growl',
		function($scope,$rootScope,$state, $cookies, GenericService,PagerService,promiseFactory,$sessionStorage,growl){
				
	
			/**Variables for pagination */
			$scope.offset = 0;
			$scope.noOfRecordsPerPage = 10;
			$scope.commonList = [];
			$scope.totalNoOfRecords;
			$scope.searchKeyword;
			$scope.commonListCount;
			
			$scope.initQMSMaster = function() {
				
				var cookieObject = $cookies.getObject('cookieObject');
				if(cookieObject == undefined){
					$state.go('login');
					return;
				}
				
				$scope.orgUnitId = cookieObject.unitId;
				$scope.orgId = cookieObject.organizationId;
				$scope.userId = cookieObject.userId;

				/** Variables for pagination */
				$scope.offset = 0;
				$scope.noOfRecordsPerPage = 10;
				$scope.totalNoOfRecords;
				$scope.searchKeyword;
				$scope.commonListCount;

				$rootScope.loginpage = true;
				$scope.saveBtnFlag = true;
				$scope.updateBtnFlag = false;

				// for popup
				$scope.showModal = false;
				$scope.buttonClicked = "";
				$scope.popUpFlag = true;
				// end for popup
				$scope.common = {
						qmsDeptMasterId:"",
						qmsKioskMasterId:"",
						qmsDeptName:"",
						startLetter:"",
						startNumber:"",
						isReception:"",
						isVoice:"",
						qmsDesc:"",
						createdBy:$scope.userId,
						updatedBy:$scope.userId,
						organisationId:$scope.orgId,
						unitId:$scope.orgUnitId					
					};
				
			}
			$scope.initQMSMaster();
			$scope.getQMSMasterList = function() {				
				var data = {
							organisationId:$scope.orgId,
							unitId:$scope.orgUnitId						
						};
				
				var URI = BASE_URL + ROOT_URL + "/api/pharmacy/getActiveQMSMasterList";
				GenericService.serviceAction("POST", URI, data).then(function(response) {
					$rootScope.stopLoader();
					$scope.activeQMSList = response.data.listObject;
				});
			}
			$scope.getQMSMasterList();		
			/**List And Pagination Start**/
			$scope.setNoOfRecords = function() {
				$scope.initQMSDeptMasterList($scope.orgId,$scope.orgUnitId	,$scope.offset, $scope.noOfRecordsPerPage);
			};
			
			$scope.initQMSDeptMasterList = function(orgId,unitId,offset, noOfRecordsPerPage)
			{
				$rootScope.startLoader();
				
				offset = offset != null ? offset : 0;
				noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
				
				var data1 = {
						organisationId:orgId,
						unitId:unitId,
						offset:offset,
						noOfRecordsPerPage:noOfRecordsPerPage
						};
				
				var data2 = {
						organisationId:orgId,
						unitId:unitId,
						};
				
				var URI1 = BASE_URL + ROOT_URL +"/api/pharmacy/getQMSDeptMasterList"; 
				var URI2 = BASE_URL + ROOT_URL + "/api/pharmacy/getQMSDeptMasterCount";
					
				
				GenericService.serviceAction("POST",URI1,data1).then(function(response){
					if(response.data.status == "success"){
					$rootScope.stopLoader();
					console.log("response1",response);
					$scope.commonList = response.data.listObject;					
					/*	angular.forEach($scope.commonList,function(value,index) {
							if(value.isReception=="A"){
//								$scope.isActive=true;
								$("#isReceptionId").removeClass("enabled");
								$("#isReceptionId").addClass("disabled");
								
							}
						});*/
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
			
			
			$scope.qmsMasterList = function(orgId,unitId,offset, noOfRecordsPerPage) {
				$rootScope.startLoader();
				console.log("offset", offset);
				offset = offset != null ? offset : 0;
				noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
				
				var data = {
						organisationId:orgId,
						unitId:unitId,
						offset:offset,
						noOfRecordsPerPage:noOfRecordsPerPage
						};
				
				var URI = BASE_URL + ROOT_URL + "/api/pharmacy/getQMSDeptMasterList";
				console.log("URI", URI);
				GenericService.serviceAction("POST", URI, data).then(function(response) {
					$rootScope.stopLoader();
					$scope.commonList = response.data.listObject;
				});
			}

			$scope.pager = {};
			$scope.page;

			$scope.setPage = function(page, flag) {
				if (page < 1 || page > $scope.pager.totalPages)
				{
					return;
				}
				$scope.pager = PagerService.GetPager($scope.commonListCount, page, $scope.noOfRecordsPerPage);
				if (flag)
				{
					$scope.qmsMasterList($scope.orgId ,$scope.orgUnitId,$scope.pager.startIndex, $scope.pager.pageSize);
				}
			}

			$scope.initQMSDeptMasterList($scope.orgId,$scope.orgUnitId,$scope.offset, $scope.noOfRecordsPerPage);
			
			/**List And Pagination End**/
			
			$scope.getKiosk=function(kioskId){
				var data = {
						organisationId:$scope.orgId,
						unitId:$scope.orgUnitId	,
						qmsKioskMasterId:kioskId
					};
				$("#isReceptionId").removeClass("enabled");
				$("#isReceptionId").addClass("disabled");
			var URI = BASE_URL + ROOT_URL + "/api/pharmacy/getReceptionListByKioskId";
			GenericService.serviceAction("POST", URI, data).then(function(response) {
				$rootScope.stopLoader();
				$scope.QMSIsReceptionList = response.data.listObject;
					if($scope.QMSIsReceptionList!=null || $scope.QMSIsReceptionList!=undefined){
							if($scope.QMSIsReceptionList.length>0){
								$("#isReceptionId").removeClass("enabled");
								$("#isReceptionId").addClass("disabled");
							}else{
								$("#isReceptionId").addClass("enabled");
								$("#isReceptionId").removeClass("disabled");
							}
					}
			});
				
			}
			$scope.PopupOkBtn = function() {
				$scope.popUpFlag = false;
				$scope.save();
			}
			$scope.save = function(){
				try{
					
					
					$scope.$broadcast('show-errors-check-validity');
					var URI = BASE_URL + ROOT_URL + "/api/pharmacy/saveQMSDeptMaster";
					console.log("OBJ",$scope.common);					
						if ($scope.qmsDeptForm.$valid) {

							if ($scope.popUpFlag) {
								$scope.showModal = !$scope.showModal;

							} else {
								$rootScope.startLoader();
								GenericService.serviceAction("POST",URI,$scope.common).then(function(response){
									
									if (response.data.status == 'success')
									{
										$rootScope.stopLoader();
										growl.success(response.data.message,
												{
													title : 'Success!'
												});
										$scope.$broadcast('show-errors-reset');
										$scope.initQMSDeptMasterList($scope.orgId,$scope.orgUnitId,$scope.offset, $scope.noOfRecordsPerPage);
									} else if(response.data.status == 'error')
									{
										$rootScope.stopLoader();
										growl.error(response.data.message,
												{
													title : 'Error!'
												});
									}
									$scope.initQMSMaster();
								});
							}
						
					}
							
				}catch(e){
					console.log("Exception-",e);
				}
			
			}
			
			$scope.showUpdateBtn = function(id,kioskId){
				try{
					$rootScope.startLoader();
					$scope.saveBtnFlag = false;
					$scope.updateBtnFlag = true;
					$scope.common={
							qmsDeptMasterId:id,
							organisationId:$scope.orgId,
							unitId:$scope.orgUnitId		
					}
					/*if(isReception=="A"){
						$("#isReceptionId").addClass("enabled");
						$("#isReceptionId").removeClass("disabled");
						
					}*/
					
					var URI = BASE_URL + ROOT_URL + "/api/pharmacy/getQMSDeptMasterById";
					GenericService.serviceAction("POST",URI,$scope.common).then(function(response){
						console.log("Edit",response);
						
						if(response.data.status == "success"){
							$rootScope.stopLoader();
							$scope.common = response.data.listObject[0];
							$scope.getKiosk(kioskId);
							
						}else if(response.data.status == 'error'){							
							$rootScope.stopLoader();
							growl.error(response.data.message,
									{
										title : 'Error!'
									});
						}
					});
					
				}catch(e){
					console.log("Exception-",e);
				}
			}
			
			
			$scope.update = function(){
				try{
					
													
					var URI = BASE_URL + ROOT_URL + "/api/pharmacy/updateQMSDeptMaster";
					console.log("Update-URI-",URI);
					console.log("OBJ",$scope.common);
					$scope.common.organisationId=$scope.orgId;
					$scope.common.unitId=$scope.orgUnitId;	
					if ($scope.qmsDeptForm.$valid) {
						$rootScope.startLoader();	
					GenericService.serviceAction("POST",URI,$scope.common).then(function(response){
						
						if (response.data.status == 'success')
						{
							$rootScope.stopLoader();
							growl.success(response.data.message,
									{
										title : 'Success!'
									});
							$scope.$broadcast('show-errors-reset');
							$scope.initQMSDeptMasterList($scope.orgId,$scope.orgUnitId,$scope.offset, $scope.noOfRecordsPerPage);
						} else if(response.data.status == 'error')						{
							
								$("#isReceptionId").removeClass("enabled");
								$("#isReceptionId").addClass("disabled");
								
							
							$rootScope.stopLoader();
							growl.error(response.data.message,
									{
										title : 'Error!'
									});
						}
						$scope.initQMSMaster();
					});
				}
					
				}catch(e){
					console.log("Exception-",e);
				}
			}
			
			$scope.updateStatus = function(id,status){
				try{
					var date = new Date();
					$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
					
					var obj = {
							qmsDeptMasterId:id,
							status:status,
							updatedBy:$scope.updatedBy
					}
					var URI = BASE_URL + ROOT_URL + "/api/pharmacy/updateQMSDeptMasterStatus";
					
					GenericService.serviceAction("POST",URI,obj).then(function(response){
						if (response.data.status == 'success')
						{
							$rootScope.stopLoader();
							growl.success(response.data.message,
									{
										title : 'Success!'
									});
							$scope.$broadcast('show-errors-reset');
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
}]);