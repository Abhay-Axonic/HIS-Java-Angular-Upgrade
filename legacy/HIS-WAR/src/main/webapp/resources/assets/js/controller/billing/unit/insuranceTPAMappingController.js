/**
 * @author 
 * @date 19-06-18
 * 	Controller to handle department Master(Unit Level)
 */
angular.module("myApp").controller("insuranceTPAMappingController", [ '$scope','$rootScope','BillingGenericService','GenericService','$sessionStorage','PagerService','promiseFactory','$cookies','$state','promiseFactoryWithObject','growl',
	function($scope,$rootScope,BillingGenericService,GenericService,$sessionStorage,PagerService,promiseFactory,$cookies,$state,promiseFactoryWithObject,growl){
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
	$scope.TPAMasterList = [];
	$scope.chekedListItem = [];
	$scope.selectedListItem = [];
	$scope.tempSelectItem = [];
	$scope.specialityMst = [];
	$scope.saveBtnFlag = true;
	/**Variables for pagination */
	$scope.offset = 0;
	$scope.noOfRecordsPerPage = 10;
	$scope.insuranceList = [];
	$scope.insuranceNotMappedList = [];
	$scope.totalNoOfRecords;
	$scope.searchKeyword;
	$scope.commonListCount;
	$scope.pager = {};
	$scope.page;
	$scope.searchKeyword = "";
	
	$scope.initMaster = function(){
		$scope.masterObj={
				companyId  : "",
				status          : "A",
				createdDate     :  "",
				createdBy       : $scope.userId,
				updatedDate     :  "",
				updatedBy       : $scope.userId,
				unitId          : $scope.unitId,
				organizationId   : $scope.organizationId,
				insuranceTPAMapperDtoList : []
		}
		
	}
	$scope.initMaster();
	
	//------------ Get Speciality Master List ------------//
	$scope.getActiveTPAList = function(){
		try{
			var URI = BASE_URL + ROOT_URL + "/api/global/getActiveTPA";
			var obj = {
					    organizationId : $scope.organizationId,
						unitId          : $scope.unitId
				    }
			BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
				console.log(response);
				if(response.data.status == "success")
					$scope.TPAMasterList = response.data.listObject;
				console.log("$scope.TPAMasterList",$scope.TPAMasterList)
			});
		}catch (e) {
			console.log("Exception",e.message);
		}
	}
	$scope.getActiveTPAList();
	$scope.getCompanyId=function(companyId){
		try{
			var URI = BASE_URL + ROOT_URL + "/api/billing/getInsuranceListNotMappedToMapper";
			var obj = {
						companyId			:companyId,
						organizationId 		: $scope.organizationId,
						unitId          	: $scope.unitId
				    }
			BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
				console.log(response);
				if(response.data.status == "success")
					$scope.insuranceNotMappedList = response.data.listObject;
				console.log("$scope.insuranceNotMappedList",$scope.insuranceNotMappedList)
			});
		}catch (e) {
			console.log("Exception",e.message);
		}
	}
	
	
	
	function uncheckListItem(){
		angular.forEach($scope.insuranceNotMappedList,function(value,index){
			$scope.specialityMst[value.insuranceId] = false;
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
	$scope.addTempItem = function(insuranceId, insuranceDesc){
		var flag = false;
		angular.forEach($scope.tempSelectItem,function(value,index){
			if(value.insuranceId == insuranceId){
				$scope.tempSelectItem.splice(index,1);
				$scope.specialityMst[value.insuranceId] = false;
				flag = true;
			}
		})
		if(!flag){
			var temp = {insuranceId:insuranceId,insuranceDesc:insuranceDesc};
			$scope.tempSelectItem.push(temp);
		}
	}
	
	//---------------- Create Billing Group Master ------------//
	$scope.save =  function(){
//		$scope.$broadcast('show-errors-check-validity');
//		if ($scope.billingGroupMasterForm.$valid){
		if($scope.masterObj.companyId == undefined || $scope.masterObj.companyId == null || $scope.masterObj.companyId == ""){
			growl.warning("Please Select TPA",
					{
						title : 'warning!'
					});
			return false;
		}
		
		if($scope.selectedListItem == null || $scope.selectedListItem.length == 0){
			growl.warning("Please Add Mapped Insurance",
					{
						title : 'warning!'
					});
			return false;
		}
		
		$scope.currentDate = new Date();
		$scope.masterObj.createdDate = moment($scope.currentDate).format('DD-MM-YYYY HH:mm:ss');
		$scope.masterObj.updatedDate = moment($scope.currentDate).format('DD-MM-YYYY HH:mm:ss');
			$rootScope.startLoader();
			var URI = BASE_URL + ROOT_URL + "/api/billing/saveInsuranceTPAMapper";
			$scope.masterObj.insuranceTPAMapperDtoList = angular.copy($scope.selectedListItem);
			console.log("save ",$scope.masterObj); 
			BillingGenericService.serviceAction(METHOD_POST,URI, $scope.masterObj, NOTIFICATION_MSG_STATUS_TRUE).then(function(response){
				console.log(response);
				if(response.data.status == "success"){					
					console.log("company id",$scope.masterObj.companyId);
					$scope.getCompanyId($scope.masterObj.companyId);
					$scope.selectedListItem = [];
					$scope.tempSelectItem = [];
					uncheckListItem();
					$scope.initMaster();
					$scope.initInsuranceTPAMasterList($scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
				}
			});
//		}
	}
	
	//-------------- Change Billing Group Master Status ---------------//
	$scope.updateStatus = function(id, status){
		$rootScope.startLoader();
		var URI = BASE_URL + ROOT_URL + "/api/billing/updateInsuranceTPAMapperStatus";
		$scope.currentDate = new Date();
		var obj = {
				    insuranceTpaMapperId  	:id,
					status         			:status,
					organizationId  		:$scope.organizationId,
					unitId         			:$scope.unitId,
					updatedDate 			: moment($scope.currentDate).format('DD-MM-YYYY HH:mm:ss'),
					updatedBy				 : $scope.userId,
		}
		BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_TRUE).then(function(response){
			console.log(response);
			if(response.data.status == "success"){
				$scope.initMaster();
				$scope.initInsuranceTPAMasterList($scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
			}
		});
		
	}
	
	//-------------- Get Billing Group Master By Id --------------//
	$scope.showUpdateBtn = function(id){
		$rootScope.startLoader();
		$scope.selectedListItem=[];
		$scope.tempSelectItem=[];
		uncheckListItem();
		var URI = BASE_URL + ROOT_URL +GETUNITSPECIALITYBYID;
		var obj ={
				 organizationId :$scope.organizationId,
				 unitId :$scope.unitId,
				 unitSpecialityMapperId :id
			}
		BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
			if(response.data.status == "success"){
				
				$scope.billingGroupMasterObj = response.data.listObject[0];
				angular.forEach(response.data.listObject, function(value, key) {
					if(value.unitSpecialityMapperId != null){
						 var obj = {specialityId:value.specialityId, specialityName:value.specialityName};
						 $scope.selectedListItem.push(obj);
						 $scope.tempSelectItem.push(obj);
						 $scope.specialityMst[value.specialityId] = true;
					}
					});
				$scope.saveBtnFlag = false;
			}
		});
	}
	
	//-------------- Update Billing Group Master ---------------//
	$scope.updateDeptAtUnitLevel = function(){
//		$scope.$broadcast('show-errors-check-validity');
//		if ($scope.billingGroupMasterForm.$valid){
			$rootScope.startLoader();
			var URI = BASE_URL + ROOT_URL + BILLING + UPDATE_BILLING_GROUP_MASTER;
			$scope.billingGroupMasterObj.listSpecialityMasterDto = angular.copy($scope.selectedListItem);
			BillingGenericService.serviceAction(METHOD_PUT,URI, $scope.billingGroupMasterObj, NOTIFICATION_MSG_STATUS_TRUE).then(function(response){
				console.log(response);
				if(response.data.status == "success"){
					$scope.initBillingGroupMaster();
					$scope.selectedListItem = [];
					$scope.tempSelectItem = [];
					uncheckListItem();
					$scope.initBillingGroupMasterList($scope.offset, $scope.noOfRecordsPerPage);
				}
			});
//		}
	}
	
	
	//------------ Get Speciality by Group ----------------//
	$scope.getSpecialityByGroup = function(billingGroupId){
		$rootScope.startLoader();
		$scope.specilityListByGroup = [];
		var URI = BASE_URL + ROOT_URL + BILLING + GET_BILLING_GROUP_MASTER_ID;
		var obj ={
			 	 orgnisationId :$scope.organizationId,
				 unitId :$scope.unitId,
				 billingGroupId :billingGroupId
			}
		BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
			if(response.data.status == "success"){
				$scope.specilityListByGroup = response.data.listObject;
			}
		});
	}
	//======================= Service Master Pagination (Start)=====================//
	$scope.setNoOfRecords = function() {
		$scope.initInsuranceTPAMasterList($scope.organizationId, $scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
	};
	
	$scope.initInsuranceTPAMasterList = function(offset, noOfRecordsPerPage){
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
		var URI_TO_GET_BILLING_GROUP_LIST = BASE_URL + ROOT_URL + "/api/billing/getInsuranceTPAMapperList";
		var URI_TO_GET_BILLING_GROUP_COUNT = BASE_URL + ROOT_URL +"/api/billing/getInsuranceTPAMapperListCount";
		promiseFactoryWithObject.setPromisesWithObject(URI_TO_GET_BILLING_GROUP_LIST, URI_TO_GET_BILLING_GROUP_COUNT, METHOD_POST, METHOD_POST, obj1, obj2).then(function(response) 
		{
			$rootScope.stopLoader();
			$scope.insuranceList = response[0].listObject;
			console.log(" insurance List",$scope.insuranceList);
			$scope.commonListCount = response[1].object;
			$scope.setPage(1, false);
		});
	}
	
	
	$scope.getInsuranceTPAMasterList = function(offset, noOfRecordsPerPage){
		try{
			$rootScope.startLoader();
			var obj = {
					organizationId :$scope.organizationId,
					 unitId        :$scope.unitId,
					 offset        :offset != null ? offset : 0,
					 noOfRecordsPerPage :noOfRecordsPerPage != null ? noOfRecordsPerPage : 10
				};
			var URI = BASE_URL + ROOT_URL +"/api/billing/getInsuranceTPAMapperList";
			console.log("URI", URI);
			BillingGenericService.serviceAction(METHOD_POST, URI, obj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				$rootScope.stopLoader();
				$scope.insuranceList = response.data.listObject;
				console.log(" insurance List",$scope.insuranceList);
				console.log($scope.insuranceList);
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
			$scope.getInsuranceTPAMasterList($scope.pager.startIndex, $scope.pager.pageSize,$scope.unitId);
		}
	}
	$scope.initInsuranceTPAMasterList($scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
	//=================== Service Master Pagination (End)====================//
}]);