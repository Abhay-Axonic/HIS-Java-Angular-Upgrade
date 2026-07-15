angular.module('myApp').controller(
		'viewAssetSchedulerController',
		['$scope','$rootScope','$state','$http','GenericService','$sessionStorage','$cookies',
		'growl','$localStorage','$stateParams','CONSTANTS','BillingGenericService',
		function($scope, $rootScope, $state, $http,	GenericService,$sessionStorage,$cookies,
				growl, $localStorage,$stateParams,CONSTANTS,BillingGenericService) {
			
			$scope.assetInfo = $stateParams.assetDataObj;
			console.log("Param Data ",$scope.assetInfo);
			
			$scope.init = function(){
				var cookieObject = $cookies.getObject('cookieObject');
				if(cookieObject == undefined){
					$state.go('login');
					return;
				}
				var cookieObjectStore = JSON.parse(sessionStorage.getItem('selectedTopStore'));
				if(cookieObjectStore==undefined){
					$rootScope.storePopup();
					angular.element('#pharmacyPopupBeforLeftexpand').modal('show');
				}
				$scope.storeId = cookieObjectStore.storeId;
				$scope.showAdd = true;
				$scope.showView = true;
				
				$rootScope.startLoader();

			  	$scope.unitId = cookieObject.unitId;
				$scope.organizationId = cookieObject.organizationId;
				$scope.userId = cookieObject.userId;
				
				$scope.assetScheduleList = [];
				$scope.assetScheduleCheckListList = [];
				
				$scope.monthObj = [{key:1 ,value:"January" },
					{key:2 ,value:"February" },
					{key:3 ,value:"March" },
					{key:4 ,value:"April" },
					{key:5 ,value:"May" },
					{key:6 ,value:"June" },
					{key:7 ,value:"July" },
					{key:8 ,value:"August" },
					{key:9 ,value:"September" },
					{key:10 ,value:"October" },
					{key:11 ,value:"November" },
					{key:12 ,value:"December" }];
				
				var obj = {
						"itemNameId":$scope.assetInfo.itemNameId,
						"serialNo":$scope.assetInfo.serialNo,
						"unitId":$scope.unitId ,
						"organizationId":$scope.organizationId
				        };
				console.log("Object ",obj);
				$rootScope.startLoader();
				var URI = BASE_URL + ROOT_URL + "/api/assetManagement/maintenance/fetchAssetSchedule";
				GenericService.serviceAction("POST", URI, obj).then(
					function(response) {
						console.log("createAssetScheduleList ", response.data.listObject);
						$rootScope.stopLoader();
						$scope.assetScheduleList = [];
						$scope.assetScheduleList = response.data.listObject;
						$scope.assetScheduleCheckList = [];
					});
				
			}
			$scope.init();
		
			
							
		
		$scope.fetchAssetSchedule = function(){
			console.log("fetchAssetSchedule ()");
			var obj = {	
					"itemNameId":$scope.assetInfo.itemNameId,
					"serialNo":$scope.assetInfo.serialNo,
					"unitId":$scope.unitId ,
					"organizationId":$scope.organizationId
			}
			console.log("object ",obj);
			$rootScope.startLoader();
			var URI = BASE_URL + ROOT_URL + "/api/assetManagement/maintenance/fetchAssetSchedule";
			GenericService.serviceAction("POST", URI, obj).then(
				function(response) {
					console.log("createAssetScheduleList ",response.data.listObject);
					$rootScope.stopLoader();
					$scope.createAssetScheduleList = [];
					$scope.createAssetScheduleList = response.data.listObject;
				});
			
		}
		
		$scope.getCheckListByAssetScheduleId= function(assetScheduleId){
			console.log("getCheckListByAssetScheduleId ()");
			if(assetScheduleId != 0){
				var obj = {	assetScheduleId:assetScheduleId,
							unitId:$scope.unitId ,
							organizationId:$scope.organizationId };
				console.log("object  ",obj);
				$rootScope.startLoader();
				var URI = BASE_URL + ROOT_URL + "/api/assetManagement/maintenance/fetchCheckListByAssetScheduleId";
				GenericService.serviceAction("POST", URI, obj).then(
					function(response) {
						$rootScope.stopLoader();
						$scope.assetScheduleCheckList = [];
						$scope.assetScheduleCheckList = response.data.listObject;
					});
			}
		}
	
		$scope.getMonth= function(d){
			var date = new Date(d);
			var monthIndex = (date.getMonth()+1);
			var month = "";
			 angular.forEach($scope.monthObj, function(object, key) {
				 if(object.key == monthIndex){ month=object.value;}
			 }); return month;console.log("Month == ",month);
		}
		
		
							
}]);
