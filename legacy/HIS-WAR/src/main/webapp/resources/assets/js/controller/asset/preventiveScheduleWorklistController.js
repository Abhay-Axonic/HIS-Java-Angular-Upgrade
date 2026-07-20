/**
 * @author  Sufiyan
 */

angular.module('myApp').controller(
		'preventiveScheduleWorklistController',
		['$scope','$rootScope','$state','$http','GenericService','$sessionStorage','$cookies',
		'growl','$localStorage','$stateParams','CONSTANTS','BillingGenericService','PaginationService',
		function($scope, $rootScope, $state, $http,	GenericService,$sessionStorage,$cookies,
				growl, $localStorage,$stateParams,CONSTANTS,BillingGenericService,PaginationService) {
			
			$scope.init = function(){
				var cookieObject = $cookies.getObject('cookieObject');
				if(cookieObject == undefined){
					$state.go('login');
					return;
				}
				var cookieObjectStore = JSON.parse(sessionStorage.getItem('selectedTopStore'));
				console.log("preventiveScheduleWorklistController ctrl");
				if(cookieObjectStore==undefined){
					$rootScope.storePopup();
					angular.element('#pharmacyPopupBeforLeftexpand').modal('show');
				} else {
					$scope.storeId = cookieObjectStore.storeId;
				}
				$scope.showAdd = true;
				$scope.showView = true;
				
				var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
				if(cookieObjectAppConfig == undefined){
					$state.go('applicationConfiguration');
					return;
				}
				$scope.assetMaintenanceSpecialityId = cookieObjectAppConfig.assetMaintenanceSpecialityId;
				
				$rootScope.startLoader();

			  	$scope.unitId = cookieObject.unitId;
				$scope.organizationId = cookieObject.organizationId;
				$scope.userId = cookieObject.userId;
				
				$scope.scheduleWorklistObj = {
						"fromDate":new Date(),
						"toDate":new Date(),
						"storeId":0,
						"pmsType":"",
						"processStatus":"",
						"assignTo":0,
						"unitId":$scope.unitId,
						"organizationId":$scope.organizationId
				}
//				$scope.dateOptions = {  'year-format': "'yyyy'", 'starting-day': 1};
				$scope.toDate= moment().toDate();
				$scope.fromDate= moment().toDate();
				$scope.dateOptions =
				{
					formatYear : 'yyyy',
					showWeeks : false,
					maxDate:new Date(),
					minDate : moment(new Date()).subtract(180, 'days').toDate()
				};
			    $scope.toDateOptions =
				{
					formatYear : 'yyyy',
					showWeeks : false,
					minDate:new Date()
				};
				$scope.openDatePickerF = function() {
					$scope.datepickerF.opened = true;	
					};
				$scope.openDatePickerT = function() {
					$scope.datepickerT.toOpened = true;	
					};
				
				$scope.datepickerF = {	
						opened : false
						};
				$scope.datepickerT = {	
						toOpened : false
						};
				
				$scope.usersList = [];
				$scope.storeList = [];
				$scope.scheduleAssetList = [];
				$scope.AssetDetailsByScheduleCriteria = [];
				 $scope.pagination = {
		                    currentPage: 1,
		                    totalPages: 0,
		                    pageSize: '10',
		                    search: '',
		                    searchData: PaginationService.searchData,
		                    setTotalPages: PaginationService.setTotalPages,
		                    getPagesList: PaginationService.getPagesList,
		                    setPage: PaginationService.setPage,
		                    setFirstPage: PaginationService.setFirstPage,
		                    setPreviousPage: PaginationService.setPreviousPage,
		                    setNextPage: PaginationService.setNextPage,
		                    setLastPage: PaginationService.setLastPage,
		                    sortBy: PaginationService.sortBy
		                  }
				
				$rootScope.startLoader();
				try{
					var data = {
						organizationId:$scope.organizationId,
						unitId:$scope.unitId,
						specialityId : $scope.assetMaintenanceSpecialityId 
					};
					console.log("specialityId	" ,$scope.assetMaintenanceSpecialityId );
					var URI = BASE_URL + ROOT_URL+ "/api/global/getEmployeeListBySpecialityAndSubSpeciality";
					console.log("ÜRI ",URI);
					console.log("data ",data);
					if($scope.assetMaintenanceSpecialityId!=null){
						GenericService.serviceAction("POST", URI, data)
							.then(function(response) {
								$scope.usersList = [];
								if (response.data.status == "success") {
									console.log("List ",response.data.listObject);
									$rootScope.stopLoader();
									$scope.usersList = response.data.listObject;
								}
							});
						}
					
					 var URI =  BASE_URL+ROOT_URL+"/api/global/getDetailedStoreList/"+$scope.userId+"/"+$scope.organizationId+"/"+$scope.unitId;
			            GenericService.serviceAction("GET", URI, {})
			            	.then(function (response) {
			                    $scope.storeList = response.data.listObject;
			                    $rootScope.stopLoader();
								console.log("$scope.storeList",$scope.storeList);
			                });	
			            
					}catch(e){
						console.log("Exception-",e);
					}
				$rootScope.stopLoader();
			}
			$scope.init();
			
			$scope.getAssetDetailsByCriteria = function () {
				$scope.AssetDetailsByScheduleCriteria = [];
	            var URI = BASE_URL+ROOT_URL+"/api/assetManagement/maintenance/getAssetDetailsByCriteria";
	            $rootScope.startLoader();
	            GenericService.serviceAction("POST", URI, $scope.scheduleWorklistObj)
	            	.then(function (responsse) {
	            		if(responsse.data.listObject != null){
	            			var fDate = moment($scope.scheduleWorklistObj.fromDate).format('DD-MM-YYYY');
	        				var tDate = moment($scope.scheduleWorklistObj.toDate).format('DD-MM-YYYY');
	        				var store = $scope.scheduleWorklistObj.storeId;
	        				var pmstype= $scope.scheduleWorklistObj.pmsType;
	        				var proStatus= $scope.scheduleWorklistObj.processStatus;
	        				var empId = $scope.scheduleWorklistObj.assignTo;
	        				console.log("^^^"+fDate+"^^^"+tDate+"^^^"+store+"^^^"+pmstype+"^^^"+proStatus+"^^"+empId);
	        				
	        				$scope.pagination.data = [];
	        				
	            			for(let i=0;i<responsse.data.listObject.length;i++){
		                		var newDate = moment(responsse.data.listObject[i].nextDate).format('DD-MM-YYYY');
		                		var pmsType =  responsse.data.listObject[i].pmsType;
		                		var storeId = responsse.data.listObject[i].storeId;
		                		var processStatus = responsse.data.listObject[i].processStatus;
		                		var assignTo = responsse.data.listObject[i].assignTo;
		                		var object = {};
		                		if(fDate >= newDate || tDate <= newDate || store === storeId || pmstype === pmsType || proStatus === processStatus || empId==assignTo ){
		                			object = responsse.data.listObject[i];
		                			object["assignee"] = $scope.getAssignNameById(object.assignTo);
		                			$scope.AssetDetailsByScheduleCriteria.push(object);
		                			object = {};
		                		}
		                	}
	            			
	            			if($scope.AssetDetailsByScheduleCriteria.length > 0){            
	                            PaginationService.searchData($scope.AssetDetailsByScheduleCriteria , $scope.pagination);
	                          }
	            			
	            			console.log("$scope.AssetDetailsByScheduleCriteria   ",$scope.AssetDetailsByScheduleCriteria );
	            		}else{
	            			growl.error('No List to Display...!!',	{title : 'Error!'});
	            		}
	                    $rootScope.stopLoader();
	                });
	        }
			$scope.getAssetDetailsByCriteria();
			$scope.redirectToDetailScheduleWorklist = function (object) {
				if(object!=null){
					console.log("setDataToView () Data == ",object);
					$state.go('detailScheduleWorklist',{assetDataObject: object});
				}
			}
			
			$scope.getAssignNameById = function (id) {
				console.log("IDEE ",id);
				var assignee = "";
				if(id!=null && id!=0){
					angular.forEach($scope.usersList,function(value,key) {
						if(value.userId == id){assignee = value.name ;}
					});
				}return assignee;
			}
			
			
			
		
							
}]);
