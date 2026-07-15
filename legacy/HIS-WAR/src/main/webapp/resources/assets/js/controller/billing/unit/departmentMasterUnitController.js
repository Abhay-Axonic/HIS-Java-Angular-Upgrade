/**
 * @author 
 * @date 19-06-18
 * 	Controller to handle department Master(Unit Level)
 */
angular.module("myApp").controller("departmentMasterUnitController", [ '$scope','$rootScope','BillingGenericService','GenericService','$sessionStorage','PagerService','promiseFactory','growl','$cookies','$state','promiseFactoryWithObject',
	function($scope,$rootScope,BillingGenericService,GenericService,$sessionStorage,PagerService,promiseFactory,growl,$cookies,$state,promiseFactoryWithObject){
	var cookieObject = $cookies.getObject('cookieObject');
	if(cookieObject == undefined){
		$state.go('login');
		return;
	}
	$rootScope.loginpage = true;
	$scope.unitId = cookieObject.unitId;
	$scope.userId = cookieObject.userId;
	$scope.organizationId = cookieObject.organizationId;
	$scope.specialityMasterList = [];
	$scope.chekedSpecialityList = [];
	$scope.selectedSpecialityList = [];
	$scope.tempSelectSpecialty = [];
	$scope.specialityMst = [];
	$scope.saveBtnFlag = true;
	$scope.searchKeyword="";
	/**Variables for pagination */
	$scope.offset = 0;
	$scope.noOfRecordsPerPage = 10;
	$scope.unitDeptMasterList = [];
	$scope.totalNoOfRecords;
	$scope.searchKeyword;
	$scope.commonListCount;
	$scope.pager = {};
	$scope.page;
	
	$scope.initBillingGroupMaster = function(){
		$scope.billingGroupMasterObj={
				unitSpecialityMapperId  : "",
				specialityId    : "",
				status          : "A",
				createdDate     :  "",
				createdBy       : $scope.userId,
				updatedDate     :  "",
				updatedBy       : $scope.userId,
				unitId          : $scope.unitId,
				organizationId   : $scope.organizationId,
				unitSpecialtyMapperDtosList : []
		}
		
	}
	$scope.initBillingGroupMaster();
	
	//------------ Get Speciality Master List ------------//
	$scope.getSpecialityMasterList = function(){
		try{
			var URI = BASE_URL + ROOT_URL + GETORGACTIVESPECIALITYLIST;
			var obj = {
						organizationId : $scope.organizationId,
						unitId          : $scope.unitId
				    }
			BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
				console.log(response);
				if(response.data.status == "success")
					$scope.specialityMasterList = response.data.listObject;
			});
		}catch (e) {
			console.log("Exception",e.message);
		}
	}
	$scope.getSpecialityMasterList();
	
	function uncheckSpecialityList(){
		angular.forEach($scope.specialityMasterList,function(value,index){
			$scope.specialityMst[value.specialityId] = false;
		})
	}
	$scope.checkToSelectedSpeciality = function(obj){
		$scope.chekedSpecialityList.push(obj);
	}
	
	$scope.selectSpeciality = function(){
		$scope.selectedSpecialityList = angular.copy($scope.tempSelectSpecialty);
	}
	
	//--------------- Select Specuality --------------//
	$scope.addTempSpecialty = function(specialityId, specialtyName){
		var flag = false;
		angular.forEach($scope.tempSelectSpecialty,function(value,index){
			if(value.specialityId == specialityId){
				$scope.tempSelectSpecialty.splice(index,1);
				$scope.specialityMst[value.specialityId] = false;
				flag = true;
			}
		})
		if(!flag){
			var temp = {specialityId:specialityId,specialityName:specialtyName};
			$scope.tempSelectSpecialty.push(temp);
		}
	}
	
	//---------------- Create Billing Group Master ------------//
	$scope.saveDeptAtUnitLevel =  function(){
		if($scope.selectedSpecialityList.length == 0){
			growl.warning("Please assign at least one department..!!",{
				title : WARNING_MSG
				});
			return false;
		}else{
//		$scope.$broadcast('show-errors-check-validity');
//		if ($scope.billingGroupMasterForm.$valid){
		$scope.currentDate = new Date();
		$scope.billingGroupMasterObj.createdDate = moment($scope.currentDate)
				.format('DD-MM-YYYY HH:mm:ss');
		$scope.billingGroupMasterObj.updatedDate = moment($scope.currentDate)
			.format('DD-MM-YYYY HH:mm:ss');
			$rootScope.startLoader();
			var URI = BASE_URL + ROOT_URL + SAVEUNITSPECIALTIY;
			$scope.billingGroupMasterObj.unitSpecialtyMapperDtosList = angular.copy($scope.selectedSpecialityList);
			console.log("save ",$scope.billingGroupMasterObj);
			BillingGenericService.serviceAction(METHOD_POST,URI, $scope.billingGroupMasterObj, NOTIFICATION_MSG_STATUS_TRUE).then(function(response){
				console.log(response);
				if(response.data.status == "success"){
					$scope.initBillingGroupMaster();
					$scope.selectedSpecialityList = [];
					$scope.tempSelectSpecialty = [];
					uncheckSpecialityList();
					$scope.initBillingGroupMasterList($scope.offset, $scope.noOfRecordsPerPage);
					$scope.getSpecialityMasterList();
				}
			});
//		}
	}
	}
	
	//-------------- Change Billing Group Master Status ---------------//
	$scope.updateStatus = function(id, status){
		$rootScope.startLoader();
		var URI = BASE_URL + ROOT_URL + UPDATESTATUSFORUNITSPECIALITYLIST;
		$scope.currentDate = new Date();
		var obj = {
				unitSpecialityMapperId :id,
					status         :status,
					organizationId  :$scope.organizationId,
					unitId         :$scope.unitId,
					updatedDate : moment($scope.currentDate).format('DD-MM-YYYY HH:mm:ss'),
					updatedBy : $scope.userId,
		}
		BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_TRUE).then(function(response){
			console.log(response);
			if(response.data.status == "success"){
				$scope.initBillingGroupMaster();
			}
		});
	}
	
	$scope.getFlagByMapperId = function(item){
		angular.element('#detailsMapperModal').modal('show');
		$scope.isSurgicalCode = (item.isSurgicalCode == "" || item.isSurgicalCode == null ? 'N':item.isSurgicalCode);
		$scope.isClinicalSpeciality = (item.isClinicalSpeciality == "" || item.isClinicalSpeciality == null ? 'N':item.isClinicalSpeciality);
		$scope.isRenderingSpeciality = (item.isRenderingSpeciality == "" || item.isRenderingSpeciality == null ? 'N':item.isRenderingSpeciality);
		$scope.isKioskDept = (item.isKioskDept == "" || item.isKioskDept == null ? 'N':item.isKioskDept);
		$scope.isNursingAssessmentRequired =  (item.isNursingAssessmentRequired == "" || item.isNursingAssessmentRequired == null ? 'N':item.isNursingAssessmentRequired);
		$scope.deptWiseAppointment =(item.deptWiseAppointment == "" || item.deptWiseAppointment == null ? 'N':item.deptWiseAppointment);
		$scope.allowedOverbookingSlots = item.allowedOverbookingSlots;
		$scope.pddDays = item.pddDays;
		$scope.unitSpecialityMapperId = item.unitSpecialityMapperId;
	}
	$scope.updateDetails = function(){
		
		var data = {
			isSurgicalCode : $scope.isSurgicalCode,
			isRenderingSpeciality : $scope.isRenderingSpeciality,
			isClinicalSpeciality : $scope.isClinicalSpeciality,
			deptWiseAppointment : $scope.deptWiseAppointment,
			isKioskDept : $scope.isKioskDept,
			isNursingAssessmentRequired :  $scope.isNursingAssessmentRequired,
			allowedOverbookingSlots : $scope.allowedOverbookingSlots,
			unitSpecialityMapperId : $scope.unitSpecialityMapperId,
			pddDays : $scope.pddDays,
			unitId:$scope.unitId
		}
		
		if($scope.allowedOverbookingSlot  == "" || $scope.allowedOverbookingSlots == null || $scope.allowedOverbookingSlots == undefined){
			data.allowedOverbookingSlots =  0;
		}
		
			console.log(data);
			var URI = BASE_URL + ROOT_URL
					+"/adt/updateUnitSpecialtyMapperDetails";
			GenericService
					.serviceAction("POST", URI, data)
					.then(
							function(response) {

								if (response.data.status == "success") {
									growl
											.success(
													'Updated successfully...!!',
													{
														title : 'Success!'
													});
									
									angular.element('#detailsMapperModal').modal('hide');
									$scope.isSurgicalCode = "N";
									$scope.isClinicalSpeciality = "N";
									$scope.isRenderingSpeciality = "N";
									$scope.isKioskDept = "N";
									$scope.isNursingAssessmentRequired = "N";
									$scope.deptWiseAppointment ="N";
									$scope.allowedOverbookingSlots = "";
									$scope.pddDays ="";
									
									
									$scope.initBillingGroupMaster();
									$scope.selectedSpecialityList = [];
									$scope.tempSelectSpecialty = [];
									uncheckSpecialityList();
									$scope.initBillingGroupMasterList($scope.offset, $scope.noOfRecordsPerPage);
									$scope.getSpecialityMasterList();

								} else {
									growl
											.error(
													'Somethung went wrong.!!',
													{
														title : 'Error!'
													});
								}
								
							});
			
		
	}
	
	//-------------- Get Billing Group Master By Id --------------//
	$scope.showUpdateBtn = function(id){
		$rootScope.startLoader();
		$scope.selectedSpecialityList=[];
		$scope.tempSelectSpecialty=[];
		uncheckSpecialityList();
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
	$scope.updateDeptAtUnitLevel = function(){
//		$scope.$broadcast('show-errors-check-validity');
//		if ($scope.billingGroupMasterForm.$valid){
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
		$scope.initBillingGroupMasterList($scope.offset, $scope.noOfRecordsPerPage);
	};
	
	$scope.initBillingGroupMasterList = function(offset, noOfRecordsPerPage){
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
		var URI_TO_GET_BILLING_GROUP_LIST = BASE_URL + ROOT_URL + GETUNITSPECIALITYLIST;
		var URI_TO_GET_BILLING_GROUP_COUNT = BASE_URL + ROOT_URL +GETUNITSPECIALITYCOUNT;
		promiseFactoryWithObject.setPromisesWithObject(URI_TO_GET_BILLING_GROUP_LIST, URI_TO_GET_BILLING_GROUP_COUNT, METHOD_POST, METHOD_POST, obj1, obj2).then(function(response) 
		{
			$rootScope.stopLoader();
			$scope.unitDeptMasterList = response[0].listObject;
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
			var URI = BASE_URL + ROOT_URL +GETUNITSPECIALITYLIST;
			console.log("URI", URI);
			BillingGenericService.serviceAction(METHOD_POST, URI, obj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				$rootScope.stopLoader();
				$scope.unitDeptMasterList = response.data.listObject;
				console.log("List",$scope.unitDeptMasterList);
				console.log($scope.unitDeptMasterList);
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
	
	$scope.searchDepartmentMasterUnit = function(searchString){
		$rootScope.startLoader();
		$scope.unitDeptMasterList=[];
		$scope.loadingMsg="Loading...";
		if(searchString!=''){
			var data = {
				organizationId : $scope.organizationId,
				specialityName : searchString,
				 unitId        :$scope.unitId
			};
		var URI = BASE_URL + ROOT_URL + "/adt/unitDepartmentMasterUniversalSearch"
				
		console.log("URI", URI);
		GenericService.serviceAction(METHOD_POST, URI, data).then(function(response) {
							$rootScope.stopLoader();
							$scope.unitDeptMasterList = response.data.listObject;
							if($scope.unitDeptMasterList.length==0)
								$scope.loadingMsg="No record found...";
			});
		}else{
			$scope.initBillingGroupMasterList($scope.offset,$scope.noOfRecordsPerPage);
		}
		$rootScope.stopLoader();
	}
}]);