angular.module('myApp')
		.controller("weekendMasterController",
		['$scope','$rootScope','$state','$cookies','GenericService','PagerService','promiseFactory','$sessionStorage','growl','BillingGenericService',
		function($scope,$rootScope,$state, $cookies, GenericService,PagerService,promiseFactory,$sessionStorage,growl,BillingGenericService){
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
			
			
			var cookieObject = $cookies.getObject('cookieObject');
			if(cookieObject == undefined){
			$state.go('login');
			return;
			}

			$scope.unitId = cookieObject.unitId;
			$scope.organizationId = cookieObject.organizationId;
			$scope.userId = cookieObject.userId;
			$scope.createdBy =cookieObject.userId;
			$scope.updatedBy =cookieObject.userId;
			
			
			$scope.dateOptions = {
					formatYear : 'yyyy',
					showWeeks : false
				};
				$scope.openDatePicker = function() {
					// alert("in");
					$scope.datepicker.opened = true;
				};

				$scope.datepicker = {
					opened : false
				};

				$scope.openDatePickerForNewDOA = function() {

					$scope.datepickerForNewDOA.opened = true;
				};

				$scope.datepickerForNewDOA = {
					opened : false
				};
				
			
			$scope.initMaster = function(){
			
				$scope.saveBtnFlag = true;
				$scope.updateBtnFlag = false;
				$scope.common = {
						weekendId:"",
						day:"",
						weekendDate:"",
						dayId:"",
						isHalfDay:"N",
						fromTime:"",
						toTime:""
								
				};
				console.log("Common-init",$scope.common);
				$scope.dayName="";
			}
			$scope.initMaster();
			
			

			$scope.test = function() {
				
				var day = $scope.datepicker.getDay();
				if(day == 0)
					{
						day = 7;
					}
				$scope.common.dayId = day;
				var data = {
					dayId : day
				}
				var URI = BASE_URL + ROOT_URL + GETDAYOBJ;
				GenericService
						.serviceAction("POST", URI, data)
						.then(
								function(response) {

									if (response.data.status == "success") {
										$scope.dayName = response.data.listObject[0].day;

									}
								});

			}
		
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
								unitId:unitId
							};
				
				var data2 = {
						      organizationId:organizationId,
						      unitId:unitId
							};
				
				var URI1 = BASE_URL + ROOT_URL +"/api/global/unit/getListOfWeekendMaster"; 
				var URI2 = BASE_URL + ROOT_URL + "/api/global/unit/getWeekendMasterCount";
					
				
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
						unitId:unitId
						};
				
				var URI = BASE_URL + ROOT_URL + "/api/global/unit/getListOfWeekendMaster";
				console.log("URI", URI);
				GenericService.serviceAction("POST", URI, data).then(function(response) {
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
			
			$scope.save = function(){
				
				try{
					
//					$rootScope.startLoader();
					var date = new Date();
					$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
					$scope.$broadcast('show-errors-check-validity');					
					/*$rootScope.startLoader();*/
					var data1={
							updatedBy:$scope.updatedBy,
							updatedDate:$scope.dt,
							status:'A',
							organizationId:$scope.organizationId,
							unitId:$scope.unitId,
							createdDate:$scope.dt,
							createdBy:$scope.createdBy,
							weekendDate:moment($scope.datepicker).format('DD-MM-YYYY HH:mm:ss'),
							isHalfDay:$scope.common.isHalfDay,
							dayId:$scope.common.dayId,
							fromTime:$scope.common.fromTime,
							toTime:$scope.common.toTime
													
					}
					if ($scope.weekendMasterForm.$valid) {
					if ($scope.popUpFlag) {
						$scope.showModal = !$scope.showModal;

					} else {
					var URI = BASE_URL + ROOT_URL + "/api/global/unit/saveWeekendMaster";
					console.log("Save-URI-",URI);
					console.log(" save OBJ",data1);   //return false;
					
					GenericService.serviceAction("POST",URI,data1).then(function(response){
						
						if (response.data.status == 'success')
						{
							$rootScope.stopLoader();
							growl.success(response.data.message,
									{
										title : 'Success!'
									});
//							return false;
							$scope.$broadcast('show-errors-reset');
							$scope.initMaster();
							$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
//							$scope.dayName="";
//							$scope.datepicker="";
							$scope.datepicker.opened = false;
						} else if(response.data.status == 'error')
						{
							$rootScope.stopLoader();
							growl.error(response.data.message,
									{
										title : 'Error!'
									});
						}
						$rootScope.stopLoader();

					});
				}
					}
				}catch(e){
					console.log("Exception-",e);
				}
			}
		
			$scope.showUpdateBtn = function(id){
				try{
					$rootScope.startLoader();
					$scope.saveBtnFlag = false;
					$scope.updateScope = true;
					$scope.updateBtnFlag = true;
					$rootScope.startLoader();
					var URI = BASE_URL + ROOT_URL +"/api/global/unit/getWeekendMasterById/"+id+"/"+$scope.organizationId+"/"+$scope.unitId;
					console.log("EDIT",URI);
					GenericService.serviceAction("GET",URI).then(
							function(response){
						console.log("Edit",response);
						
						if(response.data.status == "success"){
							$rootScope.stopLoader();
							$scope.common = response.data.listObject[0];
							$scope.datepicker = new Date(response.data.listObject[0].weekendDate);
							$scope.common.toTime=response.data.listObject[0].toTimeString;
							$scope.common.fromTime=response.data.listObject[0].fromTimeString;
							console.log("edit id ",response.data.listObject[0]);
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
				
					var date = new Date();
					$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');	
					$scope.$broadcast('show-errors-check-validity');
					$rootScope.startLoader();
					var data1={	
							weekendId:$scope.common.weekendId,
							updatedBy:$scope.updatedBy,
							updatedDate:$scope.dt,
							organizationId:$scope.organizationId,
							weekendDate:moment($scope.datepicker).format('DD-MM-YYYY HH:mm:ss'),
							unitId:$scope.unitId,
							isHalfDay:$scope.common.isHalfDay,
							dayId:$scope.common.dayId,
							fromTime:$scope.common.fromTime,
							toTime:$scope.common.toTime
						
						}
									
					var URI = BASE_URL + ROOT_URL + "/api/global/unit/updateWeekendMaster";
					console.log("Update-URI-",URI);
					console.log("Update OBJ",data1);
					$rootScope.startLoader(); 
					if ($scope.weekendMasterForm.$valid) {
					GenericService.serviceAction("POST",URI,data1).then(function(response){
						
						if (response.data.status == 'success')
						{
							$rootScope.stopLoader();
							growl.success(response.data.message,
									{
										title : 'Success!'
									});
							$scope.$broadcast('show-errors-reset');
							$scope.initMaster();
							$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
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
				}catch(e){
					console.log("Exception-",e);
				}
			}
			
			$scope.updateStatus = function(Id,status){
				try{
					var date = new Date();
					$scope.dt = moment(date).format('DD-M-YYYY HH:mm:ss');
					
					
					$rootScope.startLoader();
					var data={
							updatedBy:$scope.updatedBy,
							updatedDate:$scope.dt,
							status:status,
							weekendId:Id
					}
					var URI = BASE_URL + ROOT_URL + "/api/global/unit/updateWeekendMasterStatus";
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
}]);
