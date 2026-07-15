/**
 * @author 
 * @date 19-06-18
 * 	Controller to handle department Master(Unit Level)
 */
angular.module("myApp").controller("holidayCalendarMasterUnitController", [ '$scope','$rootScope','BillingGenericService','GenericService','$sessionStorage','PagerService','promiseFactory','$cookies','$state','promiseFactoryWithObject',
	function($scope,$rootScope,BillingGenericService,GenericService,$sessionStorage,PagerService,promiseFactory,$cookies,$state,promiseFactoryWithObject){
	var cookieObject = $cookies.getObject('cookieObject');
	if(cookieObject == undefined){
		$state.go('login');
		return;
	}
	$rootScope.loginpage = true;
	var cookieObject = $cookies.getObject('cookieObject');
	if(cookieObject == undefined){
	$state.go('login');
	return;
	}

	$scope.unitId = cookieObject.unitId;
	$scope.organizationId = cookieObject.organizationId;
	$scope.userId = cookieObject.userId;
	$scope.calenderList = [];
	$scope.chekedListItem = [];
	$scope.selectedListItem = [];
	$scope.tempSelectItem = [];
	$scope.specialityMst = [];
	$scope.saveBtnFlag = true;
	/**Variables for pagination */
	$scope.offset = 0;
	$scope.noOfRecordsPerPage = 10;
	$scope.unitCalenderList = [];
	$scope.totalNoOfRecords;
	$scope.searchKeyword;
	$scope.commonListCount;
	$scope.pager = {};
	$scope.page;
	
	$scope.initMaster = function(){
		$scope.masterObj={
				status          : "A",
				createdDate     :  "",
				createdBy       : $scope.userId,
				updatedDate     :  "",
				updatedBy       : $scope.userId,
				unitId          : $scope.unitId,
				organizationId   : $scope.organizationId,
		}
		
	}
	$scope.initMaster();
	
	//------------ Get calender Master List ------------//
	$scope.getActiveHolidayCalenderList = function(){
		try{
			var URI = BASE_URL + ROOT_URL + GETHOLIDAYCALENDERLISTNOTMAPPEDTOUNIT;
			var obj = {
						organizationId : $scope.organizationId,
						unitId			:$scope.unitId
				    }
			BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
				console.log(response);
				if(response.data.status == "success")
					$scope.calenderList = response.data.listObject;
				console.log("calenderList",$scope.calenderList);
			});
		}catch (e) {
			console.log("Exception",e.message);
		}
	}
	$scope.getActiveHolidayCalenderList();
	
	
	
	function uncheckListItem(){
		angular.forEach($scope.calenderList,function(value,index){
			$scope.specialityMst[value.holidayCalenderId] = false;
		})
	}
	$scope.checkToSelectedListItem = function(obj){
		$scope.chekedListItem.push(obj);
	}
	
	$scope.selectItem = function(){
		$scope.selectedListItem = angular.copy($scope.tempSelectItem);
		console.log("$scope.selectedListItem",$scope.selectedListItem);
	}
	
	//--------------- Select Specuality --------------//
	$scope.addTempItem = function(holidayCalenderId, holidayCalenderDesc){
		var flag = false;
		angular.forEach($scope.tempSelectItem,function(value,index){
			if(value.holidayId == holidayCalenderId){
				$scope.tempSelectItem.splice(index,1);
				$scope.specialityMst[value.holidayCalenderId] = false;
				flag = true;
			}
		})
		if(!flag){
			var temp = {holidayId:holidayCalenderId,holidayCalenderDesc:holidayCalenderDesc};
			$scope.tempSelectItem.push(temp);
		}
	}
	
	//---------------- SAVE ------------//
	$scope.save =  function(){
		$scope.currentDate = new Date();
		$scope.masterObj.createdDate = moment($scope.currentDate).format('DD-MM-YYYY HH:mm:ss');
		$scope.masterObj.updatedDate = moment($scope.currentDate).format('DD-MM-YYYY HH:mm:ss');
			$rootScope.startLoader();
			var URI = BASE_URL + ROOT_URL + SAVEUNITHOLIDAYCALENER;
			$scope.masterObj.unitHolidayCalenderMapperDtosList = angular.copy($scope.selectedListItem);
			console.log("save ",$scope.masterObj);  
			BillingGenericService.serviceAction(METHOD_POST,URI, $scope.masterObj, NOTIFICATION_MSG_STATUS_TRUE).then(function(response){
				console.log(response);
				if(response.data.status == "success"){					
					$scope.getActiveHolidayCalenderList();
					$scope.selectedListItem = [];
					$scope.tempSelectItem = [];
					uncheckListItem();
					$scope.initMasterList($scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
				}
			});
//		}
	}
	
	//-------------- Change Master Status ---------------//
	$scope.updateStatus = function(id, status){
		$rootScope.startLoader();
		var URI = BASE_URL + ROOT_URL + UPDATESTATUSFORUNITHOLIDAYCALENER;
		$scope.currentDate = new Date();
		var obj = {
					unitHolidayCalenderId  	:id,
					status         			:status,
					updatedDate 			: moment($scope.currentDate).format('DD-MM-YYYY HH:mm:ss'),
					updatedBy				: $scope.userId,
		}
		BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_TRUE).then(function(response){
			console.log(response);
			if(response.data.status == "success"){
				$scope.initMaster();
				$scope.initMasterList($scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
			}
		});
		
	}
	
		
	//===================== Master Pagination (Start)=====================//
	$scope.setNoOfRecords = function() {
		$scope.initMasterList($scope.organizationId, $scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
	};
	
	$scope.initMasterList = function(offset, noOfRecordsPerPage){
		$rootScope.startLoader();
		var obj1 = {
				organizationId :$scope.organizationId,
				unitId        :$scope.unitId,
				offset        :offset != null ? offset : 0,
				noOfRecordsPerPage :noOfRecordsPerPage != null ? noOfRecordsPerPage : 10
			}
		
		var obj2 = {
				organizationId :$scope.organizationId,
				 unitId        :$scope.unitId
			}
		var URI_TO_GET_BILLING_GROUP_LIST = BASE_URL + ROOT_URL + GETUNITHOLIDAYCALENERLIST;
		var URI_TO_GET_BILLING_GROUP_COUNT = BASE_URL + ROOT_URL + GETUNITHOLIDAYCALENERCOUNT;
		promiseFactoryWithObject.setPromisesWithObject(URI_TO_GET_BILLING_GROUP_LIST, URI_TO_GET_BILLING_GROUP_COUNT, METHOD_POST, METHOD_POST, obj1, obj2).then(function(response) 
		{
			$rootScope.stopLoader();
			$scope.unitCalenderList = response[0].listObject;
			console.log(" unit calender List",$scope.unitCalenderList);
			$scope.commonListCount = response[1].object;
			$scope.setPage(1, false);
		});
	}
	
	
	$scope.getList = function(offset, noOfRecordsPerPage){
		try{
			$rootScope.startLoader();
			var obj = {
					organizationId :$scope.organizationId,
					 unitId        :$scope.unitId,
					 offset        :offset != null ? offset : 0,
					 noOfRecordsPerPage :noOfRecordsPerPage != null ? noOfRecordsPerPage : 10
				};
			var URI = BASE_URL + ROOT_URL +GETUNITHOLIDAYCALENERLIST;
			BillingGenericService.serviceAction(METHOD_POST, URI, obj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				$rootScope.stopLoader();
				$scope.unitCalenderList = response.data.listObject;
				console.log(" unitCalenderList ",$scope.unitCalenderList);
			});
		}catch (e) {
			console.log(e.message);
		}
	}
	
	$scope.setPage = function(page, flag) {
		if (page < 1 || page > $scope.pager.totalPages)
		{
			return;
		}
		$scope.pager = PagerService.GetPager($scope.commonListCount, page, $scope.noOfRecordsPerPage,$scope.unitId);
		if (flag)
		{
			$scope.getList($scope.pager.startIndex, $scope.pager.pageSize,$scope.unitId);
		}
	}
	$scope.initMasterList($scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
	//===================  Master Pagination (End)====================//
}]);