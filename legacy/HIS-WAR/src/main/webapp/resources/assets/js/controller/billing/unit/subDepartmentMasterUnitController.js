/**
 * @author
 * @date 19-06-2018
 * 		Controller to map unit sub department(Unit Level)
 */
angular.module("myApp").controller("subDepartmentMasterUnitController", [ '$scope','$rootScope','growl','BillingGenericService','GenericService','$sessionStorage','PagerService','promiseFactory','$cookies','$state','promiseFactoryWithObject',
	function($scope,$rootScope,growl,BillingGenericService,GenericService,$sessionStorage,PagerService,promiseFactory,$cookies,$state,promiseFactoryWithObject){
	var cookieObject = $cookies.getObject('cookieObject');
	if(cookieObject == undefined){
		$state.go('login');
		return;
	}
	$rootScope.loginpage = true;
	$scope.unitId = cookieObject.unitId;
	$scope.userId=cookieObject.userId;
	$scope.organizationId = cookieObject.organizationId;
	$scope.unitSpecialityMasterList = [];
	$scope.chekedSpecialityList = [];
	$scope.selectedSpecialityList = [];
	$scope.tempSelectSpecialty = [];
	$scope.specialityMst = [];
	$scope.saveBtnFlag = true;
	$scope.searchKeyword="";
	/**Variables for pagination */
	$scope.offset = 0;
	$scope.noOfRecordsPerPage = 10;
	$scope.unitSubSpelMasterList = [];
	$scope.totalNoOfRecords;
	$scope.searchKeyword;
	$scope.commonListCount;
	$scope.pager = {};
	$scope.page;
	
	$scope.initBillingGroupMaster = function(){
		$scope.unitSpecialityMasterObj={
				specialityId  : "",
				subSpecialityId: "",
				unitSubSpecialityId: "",
				specialityName: "",
				subSpecialityName:"",
				status          : "A",
				createdDate     : "",
				createdBy       : $scope.userId,
				updatedDate     : "",
				updatedBy       : $scope.userId,
				unitId          : $scope.unitId,
				organizationId   : $scope.organizationId,
				unitSubSpecialityMapperDtosList : []
		}
		
	}
	$scope.initBillingGroupMaster();
	
	//------------ Get Speciality Master List ------------//
	$scope.getSpecialityMasterList = function(){
		try{
			var URI = BASE_URL + ROOT_URL + GETACTIVEUNITSPECIALITYLIST;
			var obj = {
						organizationId : $scope.organizationId,
						unitId         :$scope.unitId
				    }
			BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
				console.log(response);
				if(response.data.status == "success")
					$scope.unitSpecialityMasterList = response.data.listObject;
					console.log("unitSpecialityMasterList",$scope.unitSpecialityMasterList);
			});
		}catch (e) {
			console.log("Exception",e.message);
		}
	}
	$scope.getSpecialityMasterList();
	$scope.getSpecilityId=function(id){
		try{
			$scope.subSpecialityMasterList = [];
			$scope.selectedSpecialityList = [];
			var URI = BASE_URL + ROOT_URL + GETSUBSPECIALITYNOTINUNIT+"/orgId/"+$scope.organizationId+"/unitId/"+$scope.unitId+"/specialityId/"+id;
			var obj = {
						
				    }
			BillingGenericService.serviceAction(METHOD_GET,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
				console.log(response);
				if(response.data.status == "success")
					$scope.subSpecialityMasterList = response.data.listObject;
				    $scope.selectedSpecialityList = [];
					console.log("subSpecialityMasterList",$scope.subSpecialityMasterList);
			});
		}catch (e) {
			console.log("Exception",e.message);
		}
	}
	/*$scope.getSpecilityId = function(specialityId){
		if(specialityId != null){
		var URI = BASE_URL + ROOT_URL + "/adt/subspeciality/"+specialityId;
		BillingGenericService.serviceAction(METHOD_GET,URI, null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
			$scope.subSpecialityMasterList= [];
			if (response.data.status == "success")
				$scope.subSpecialityMasterList = response.data.listObject;
			console.log("subSpecialityMasterList",$scope.subSpecialityMasterList);
		});
		}
	}*/
	
	function uncheckSpecialityList(){
		angular.forEach($scope.subSpecialityMasterList,function(value,index){
			$scope.specialityMst[value.subSpecialityMasterId] = false;
		})
	}
	$scope.checkToSelectedSpeciality = function(obj){
		$scope.chekedSpecialityList.push(obj);
	}
	
	$scope.selectSpeciality = function(){
		$scope.selectedSpecialityList = angular.copy($scope.tempSelectSpecialty);
		console.log("selectedSpecialityList",$scope.selectedSpecialityList);
	}
	
	//--------------- Select Specuality --------------//
	$scope.addTempSpecialty = function(subSpecialityMasterId, subSpecialityName){
		var flag = false;
		angular.forEach($scope.tempSelectSpecialty,function(value,index){
			if(value.subSpecialityMasterId == subSpecialityMasterId){
				$scope.tempSelectSpecialty.splice(index,1);
				$scope.specialityMst[value.subSpecialityMasterId] = false;
				flag = true;
			}
		})
		if(!flag){
			var temp = {subSpecialityMasterId:subSpecialityMasterId,subSpecialityName:subSpecialityName};
			$scope.tempSelectSpecialty.push(temp);
		}
	}
	
	//---------------- Save sub speciality at unit level ------------//
	$scope.saveSubSpeciality =  function(){
//		$scope.$broadcast('show-errors-check-validity');
//		if ($scope.billingGroupMasterForm.$valid){
		$scope.currentDate = new Date();
		$scope.unitSpecialityMasterObj.createdDate = moment($scope.currentDate)
				.format('DD-MM-YYYY HH:mm:ss');
		$scope.unitSpecialityMasterObj.updatedDate = moment($scope.currentDate)
			.format('DD-MM-YYYY HH:mm:ss');
			
			var URI = BASE_URL + ROOT_URL + SAVEUNITSUBSPECIALTIY;
			$scope.unitSpecialityMasterObj.unitSubSpecialityMapperDtosList = angular.copy($scope.selectedSpecialityList);
			console.log("saveSubSpeciality",$scope.unitSpecialityMasterObj); 
//			return false;
			if($scope.unitSpecialityMasterObj.specialityId==undefined || $scope.unitSpecialityMasterObj.specialityId == null || $scope.unitSpecialityMasterObj.specialityId==0)
			{
				growl.warning("Please select speciality...!!",{
					title : 'Warning!'
				});
				return;
			}			
			else if($scope.selectedSpecialityList.length ==0 )
				{
					growl.warning("Please select at lease one sub-speciality...!!",{
						title : 'Warning!'
					});
					return;
				}
			else{
				$rootScope.startLoader();
				BillingGenericService.serviceAction(METHOD_POST,URI, $scope.unitSpecialityMasterObj, NOTIFICATION_MSG_STATUS_TRUE).then(function(response){
					console.log(response);
					if(response.data.status == "success"){
						console.log("sp id",$scope.unitSpecialityMasterObj.specialityId);
						$scope.getSpecilityId($scope.unitSpecialityMasterObj.specialityId);
						$scope.selectedSpecialityList = [];
						$scope.tempSelectSpecialty = [];
						uncheckSpecialityList();
						$scope.initBillingGroupMasterList($scope.offset, $scope.noOfRecordsPerPage);
					}
					
				});
			}
//		}
	}
	
	//-------------- Change unit level subspeciality  Status ---------------//
	$scope.updateStatus = function(id, status){
		$rootScope.startLoader();
		$scope.currentDate = new Date();
		var URI = BASE_URL + ROOT_URL + UPDATESTATUSFORUNITSUBSPECIALITY;
		var obj = {
				   unitSubSpecialityId :id,
					status         :status,
					organizationId  :$scope.organizationId,
					unitId         :$scope.unitId,
					updatedBy       : $scope.userId,
					updatedDate : moment($scope.currentDate).format('DD-MM-YYYY HH:mm:ss')
		}
		BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_TRUE).then(function(response){
			console.log(response);
			if(response.data.status == "success"){
				$scope.initBillingGroupMaster();
			}
		});
	}
	
	//-------------- Get Billing Group Master By Id --------------//
	$scope.getBillingGroupMasterById = function(billingGroupId){
		$rootScope.startLoader();
		$scope.selectedSpecialityList=[];
		$scope.tempSelectSpecialty=[];
		uncheckSpecialityList();
		var URI = BASE_URL + ROOT_URL + BILLING + GET_BILLING_GROUP_MASTER_ID;
		var obj ={
			 	 orgnisationId :$scope.organizationId,
				 unitId :$scope.unitId,
				 billingGroupId :billingGroupId
			}
		BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
			if(response.data.status == "success"){
				
				$scope.billingGroupMasterObj = response.data.listObject[0];
				angular.forEach(response.data.listObject, function(value, key) {
					if(value.specialityId != null){
						 var obj = {specialityId:value.specialityId, specialityName:value.specialityName};
						 $scope.selectedSpecialityList.push(obj);
						 $scope.tempSelectSpecialty.push(obj);
						 $scope.specialityMst[value.specialityId] = true;
					}
					});
				$scope.saveBtnFlag = false;
			}
		});
	}
	
	//-------------- Update Billing Group Master ---------------//
	$scope.updateBillingGroupMaster = function(){
		$scope.$broadcast('show-errors-check-validity');
		if ($scope.billingGroupMasterForm.$valid){
			$rootScope.startLoader();
			var URI = BASE_URL + ROOT_URL + BILLING + UPDATE_BILLING_GROUP_MASTER;
			$scope.billingGroupMasterObj.listSpecialityMasterDto = angular.copy($scope.selectedSpecialityList);
			BillingGenericService.serviceAction(METHOD_PUT,URI, $scope.billingGroupMasterObj, NOTIFICATION_MSG_STATUS_TRUE).then(function(response){
				console.log(response);
				if(response.data.status == "success"){
					$scope.initBillingGroupMaster();
					$scope.selectedSpecialityList = [];
					$scope.tempSelectSpecialty = [];
					uncheckSpecialityList();
					$scope.initBillingGroupMasterList($scope.offset, $scope.noOfRecordsPerPage);
				}
			});
		}
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
		$scope.initBillingGroupMasterList($scope.offset, $scope.noOfRecordsPerPage);
	};
	
	$scope.initBillingGroupMasterList = function(offset, noOfRecordsPerPage){
		
		$rootScope.startLoader();
		$scope.unitSubSpelMasterList = [];
		$scope.commonListCount=0;
		
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
	
		var GETUNITSUBSPECIALITYLIST = BASE_URL + ROOT_URL + GETUNITSUBSPECIALITYMASTERLIST;
		var GETUNITSUBSPECIALITYCOUNT = BASE_URL + ROOT_URL + GETUNITSUBSPECIALITYMASTERCOUNT;
		
		promiseFactoryWithObject.setPromisesWithObject(GETUNITSUBSPECIALITYLIST, GETUNITSUBSPECIALITYCOUNT, METHOD_POST, METHOD_POST, obj1, obj2).then(function(response) 
		{
			$rootScope.stopLoader();
			$scope.unitSubSpelMasterList = response[0].listObject;
//			console.log("unitSubSpelMasterList",$scope.unitSubSpelMasterList);
			$scope.commonListCount = response[1].object;
			$scope.setPage(1, false);
		});
	}
	
	
	$scope.getBillingGroupMasterList = function(offset, noOfRecordsPerPage){
		
		try{
			$rootScope.startLoader();
			var obj = {
					organizationId :$scope.organizationId,
					 unitId        :$scope.unitId,
					 offset        :offset != null ? offset : 0,
					 noOfRecordsPerPage :noOfRecordsPerPage != null ? noOfRecordsPerPage : 10
				};
			var URI = BASE_URL + ROOT_URL + GETUNITSUBSPECIALITYMASTERLIST;;
			console.log("URI", URI);
			BillingGenericService.serviceAction(METHOD_POST, URI, obj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				$rootScope.stopLoader();
				$scope.unitSubSpelMasterList = response.data.listObject;
				console.log("unitSubSpelMasterList",$scope.unitSubSpelMasterList);
				
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
		$scope.pager = PagerService.GetPager($scope.commonListCount, page, $scope.noOfRecordsPerPage);
		if (flag)
		{
			$scope.getBillingGroupMasterList($scope.pager.startIndex, $scope.pager.pageSize);
		}
	}
	$scope.initBillingGroupMasterList($scope.offset, $scope.noOfRecordsPerPage);
	//=================== Service Master Pagination (End)====================//
	
	$scope.searchSubDepartmentMasterUnit = function(searchString){
		$rootScope.startLoader();
		$scope.unitSubSpelMasterList=[];
		$scope.loadingMsg="Loading...";
		if(searchString!=''){
			var data = {
				organizationId : $scope.organizationId,
				specialityName : searchString,
				 unitId        :$scope.unitId
			};
		var URI = BASE_URL + ROOT_URL + "/adt/unitSubDepartmentMasterUniversalSearch"
				
		console.log("URI", URI);
		GenericService.serviceAction(METHOD_POST, URI, data).then(function(response) {
							$rootScope.stopLoader();
							$scope.unitSubSpelMasterList = response.data.listObject;
							if($scope.unitSubSpelMasterList.length==0)
								$scope.loadingMsg="No record found...";
			});
		}else{
			$scope.initBillingGroupMasterList($scope.offset,$scope.noOfRecordsPerPage);
		}
		$rootScope.stopLoader();
	}
}]);