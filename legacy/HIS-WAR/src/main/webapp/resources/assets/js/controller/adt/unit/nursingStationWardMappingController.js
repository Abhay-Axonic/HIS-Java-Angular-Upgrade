/**
 * @author
 * @date 26-06-2018
 * 		Controller to map warad to nursing station (Unit Level)
 */
angular.module("myApp").controller("nursingStationWardMappingController", [ '$scope','$rootScope','BillingGenericService','GenericService','$sessionStorage','PagerService','promiseFactory','$cookies','$state','promiseFactoryWithObject','GenericServiceParamHeader','growl',
	function($scope,$rootScope,BillingGenericService,GenericService,$sessionStorage,PagerService,promiseFactory,$cookies,$state,promiseFactoryWithObject,GenericServiceParamHeader,growl){
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
	/**Variables for pagination */
	$scope.offset = 0;
	$scope.noOfRecordsPerPage = 10;
	$scope.totalNoOfRecords;
	$scope.commonListCount;
	$scope.pager = {};
	$scope.page;
	$scope.wardList = [];
	$scope.nursingStationList=[];
	$scope.nursingMapperObj={
			nursingStationId:"",
			createdBy:$scope.userId,
			updatedBy:$scope.userId,
			unitId:$scope.unitId,
			organizationId:$scope.organizationId
	}
	$scope.saveBtnFlag = true;
	$scope.updateBtnFlag = false;

	// for popup
	$scope.showModal = false;
	$scope.buttonClicked = "";
	$scope.popUpFlag = true;
	// end for popup
	$scope.initBillingGroupMaster = function(){


			
			//For Get Nursingstation List
		var data = {}
			var headerObj = {
								organization_id:$scope.organizationId,
								unit_id : $scope.unitId
							}
			var paramObj = {};
			var URI = BASE_URL + ROOT_URL
					+ GETLISTOFNURSINGSTATION;
			GenericServiceParamHeader
					.serviceAction(METHOD_GET, URI,
							data,
							NOTIFICATION_MSG_STATUS_FALSE,paramObj,headerObj)
					.then(
							function(response) {
								$scope.nursingStationList=[];
								// $rootScope.startLoader();
								if (response.data.status == "success")
									$scope.nursingStationList = response.data.listObject;
								console.log("nursingStationList",$scope.nursingStationList);
							});


				}
	
	$scope.getNursingStationId=function(nursingId){
		// For Get ward  List 
		var URI = BASE_URL + ROOT_URL
		+ GETWARDLISTNOTMAPPEDTONURSINGSTATION;
		var data = {
				organizationId:$scope.organizationId,
				unitId:$scope.unitId,
				nursingStationId:nursingId
		}
	GenericService.serviceAction("POST", URI, data).then(
				function(response) {
					$scope.wardList = [];		
					if (response.data.status == "success") {
						$scope.wardList = response.data.listObject;	
						console.log("wardList",$scope.wardList);
					}
				});
	}
	$scope.initBillingGroupMaster();
			
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
	$scope.addTempSpecialty = function(wardId, wardName){
		var flag = false;
		angular.forEach($scope.tempSelectSpecialty,function(value,index){
			if(value.wardId == wardId){
				$scope.tempSelectSpecialty.splice(index,1);
				$scope.specialityMst[value.wardId] = false;
				flag = true;
			}
		})
		if(!flag){
			var temp = {wardId:wardId,wardName:wardName};
			$scope.tempSelectSpecialty.push(temp);
		}
	}
	
	//SELECT ward from nursing stationId start
	$scope.geNursingStationMasterId=function(nursingStationId){
		$scope.wardInfoList = [];	
		var URI = BASE_URL + ROOT_URL
		+ "/api/adt/unit/getWardListByNursingStationId/"+nursingStationId+"/orgId/"+$scope.organizationId+"/unitId/"+$scope.unitId;
		var data = {
				
		}
	GenericService.serviceAction("GET", URI, data).then(
				function(response) {
						
					if (response.data.status == "success") {
						$scope.wardInfoList = response.data.listObject;	
						console.log("wardInfoList",$scope.wardInfoList);
					}
				});
	}
	
	//SELECT ward from nursing stationId End 
	
	// For Paginations Start
	$scope.setNoOfRecords = function() {
		$scope.initNursingMasterMappingList(
				$scope.organizationId, $scope.offset,
				$scope.noOfRecordsPerPage,$scope.unitId);
	};
	$scope.initNursingMasterMappingList = function(orgId,
			offset, noOfRecordsPerPage,unitId) {
		var data = "";
		offset = offset != null ? offset : 0;
		noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
				: 10;
		var URI = BASE_URL + ROOT_URL
				+ GETNURSINGSTATIONWARDMAPPERLIST;
		var URI1 = BASE_URL + ROOT_URL
				+ GETNURSINGSTATIONWARDMAPPERCOUNT;

		var data = {
			organizationId : orgId,
			unitId:unitId,
			offset : offset,
			recordPerPage : noOfRecordsPerPage
		}
		console.log("data", data);

		var data1 = {
			unitId:unitId,
			organizationId : $scope.organizationId
		}

		// For Get mapper list Using Offset
		GenericService
				.serviceAction("POST", URI, data)
				.then(
						function(response) {
							$scope.nursingMapperMasterList = [];

							if (response.data.status == "success") {
								$scope.nursingMapperMasterList = response.data.listObject;
								console.log("nursingMapperMasterList", $scope.nursingMapperMasterList);
								$scope.commonListCount = response.data.object;
								$scope.setPage(1,false);
							}
						});

		// For Count nursing mapper list 
	/*	GenericService
				.serviceAction("POST", URI1, data1)
				.then(
						function(response) {
							if (response.data.status == "success") {
								console
										.log(response.data);
								$scope.commonListCount = response.data.object;
								$scope
										.setPage(1,
												false);

							}
						});*/
	}

	$scope.getNursingMapperList = function(orgId,
			offset, noOfRecordsPerPage,unitId) {
		console.log("offset", offset);
		var data = "";
		offset = offset != null ? offset : 0;
		noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
				: 10;
		var URI = BASE_URL + ROOT_URL
				+ GETNURSINGSTATIONWARDMAPPERLIST;
		var data = {
			organizationId : $scope.organizationId,
			unitId:$scope.unitId,
			offset : offset,
			recordPerPage : noOfRecordsPerPage
		}

		console.log("URI", URI);
		// For Get nursing mapper List Using Offset
		GenericService
				.serviceAction("POST", URI, data)
				.then(
						function(response) {
							$scope.nursingMapperMasterList = [];

							if (response.data.status == "success") {
								$scope.nursingMapperMasterList = response.data.listObject;														
							}
						});
	}

	$scope.pager = {};
	$scope.page;

	$scope.setPage = function(page, flag) {
		if (page < 1 || page > $scope.pager.totalPages) {
			return;
		}
		$scope.pager = PagerService.GetPager(
				$scope.commonListCount, page,
				$scope.noOfRecordsPerPage,$scope.unitId);
		if (flag) {
			$scope.getNursingMapperList(
					$scope.organizationId,
					$scope.pager.startIndex,
					$scope.pager.pageSize,$scope.unitId);
		}
	}

	$scope.initNursingMasterMappingList($scope.organizationId,
			$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
	
	// function for popup ok button call
	$scope.PopupOkBtn = function() {
		$scope.popUpFlag = false;
		$scope.saveNursingMapper();
	}

	// For Paginations End
	$scope.saveNursingMapper =  function(){
		if($scope.popUpFlag==false)
		{
		  $rootScope.startLoader();
		}
		$scope.currentDate = new Date();
		$scope.nursingMapperObj.createdDate = moment($scope.currentDate)
				.format('DD-MM-YYYY HH:mm:ss');
		
		$scope.nursingMapperObj.updatedDate = moment($scope.currentDate)
			.format('DD-MM-YYYY HH:mm:ss');
		

		$scope.$broadcast('show-errors-check-validity');
		
		$scope.nursingMapperObj.status='A';
			
			var URI = BASE_URL + ROOT_URL + SAVENURSINGSTATIONWARDMAPPER;
			$scope.nursingMapperObj.nursingWardMapperList = angular.copy($scope.selectedSpecialityList);
			console.log("nursingMapperObj",$scope.nursingMapperObj); 
			
			if ($scope.nursingStationWardMappingForm.$valid){
				if ($scope.popUpFlag) {
					$scope.showModal = !$scope.showModal;
	//				return false;
				} else {
			BillingGenericService.serviceAction(METHOD_POST,URI, $scope.nursingMapperObj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
				console.log(response);
				if(response.data.status == "success"){
				
					if(response.data.code!=null){
						growl.success(response.data.message,{
							title : "SUCCESS"
						});
					}
					$scope.$broadcast('show-errors-reset');
					$scope.getNursingStationId($scope.nursingMapperObj.nursingStationId);
					$scope.nursingMapperObj.nursingStationId="";
					$scope.initBillingGroupMaster();
					$scope.selectedSpecialityList = [];
					$scope.tempSelectSpecialty = [];
					$scope.specialityMst=[];
					$scope.wardList=[];
					uncheckSpecialityList();
					
					$scope.initNursingMasterMappingList($scope.organizationId,
							$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
				}else if(response.data.status == "error"){
					growl.error(response.data.message,
							{
								title : 'Error!'
							});
				}
				
			});
			}
				
		}
	}
	
	// Function For update room status
	$scope.updateStatus = function(id, type,nursingWardId) {
		$scope.currentDate = new Date();
		$scope.currentDate = moment($scope.currentDate)
				.format('DD-MM-YYYY HH:mm:ss');

		type = (type == "A") ? "A" : "I";

		var data = {
			nursingStationId : id,
			nursingWardId : nursingWardId,
			status : type,
			updatedBy : $scope.userId,
			updatedDate : $scope.currentDate,
			organizationId : $scope.organizationId,
			unitId:$scope.unitId
		}
		var URI = BASE_URL + ROOT_URL
				+ UPDATENURSINGSTATIONWARDMAPPERSTATUS;
		GenericService
				.serviceAction("PUT", URI, data)
				.then(
						function(response) {

							if (response.data.status == "success") {
								growl
										.success(
												response.data.message,
												{
													title : 'Success!'
												});
								$scope.initBillingGroupMaster();
							} else {
								growl
										.error(
												response.data.message,
												{
													title : 'Error!'
												});
							}
						});
	}
	$scope.isDeleted='A';
	$scope.showUpdateBtn = function(nursingStationId,status)
	{
		try {
			$rootScope.startLoader();
			$scope.saveBtnFlag = false;
			$scope.updateBtnFlag = true;
			var URI = BASE_URL + ROOT_URL + "/api/adt/unit/getNursingStationWardMapperById/id/"+nursingStationId+"/orgId/"+$scope.organizationId+"/unitId/"+$scope.unitId;
			console.log("EDIT",URI);
			GenericService.serviceAction("GET", URI, "").then(function(response) 
			{
				console.log("data",response.data);
				
				if (response.data.status == 'success')
				{
					$rootScope.stopLoader();
//					$scope.common = response.data.listObject[0];
//					$scope.nursingMapperObj.nursingStationId = response.data.listObject[0].nursingStationId.toString();
					$scope.isDeleted=status;
					if(response.data.listObject.length==0 ){
						$scope.nursingMapperObj.nursingStationId = nursingStationId.toString();
						$scope.getNursingStationId(nursingStationId);
					}else{
							$scope.nursingMapperObj.nursingStationId = response.data.listObject[0].nursingStationId.toString();
							$scope.getNursingStationId(response.data.listObject[0].nursingStationId);
					}
					$scope.selectedSpecialityList = [];
					//$scope.getNursingStationId(response.data.listObject[0].nursingStationId);
					angular.forEach($scope.specialityMst,function(value,index){
						$scope.specialityMst[index] = false;
					var i = $scope.tempSelectSpecialty.indexOf(value);
					$scope.tempSelectSpecialty.splice(i,1);
					})
					
					angular.forEach(response.data.listObject,function(value,index){
						var temp = {wardId:value.wardId,wardName:value.wardName};
						$scope.tempSelectSpecialty.push(temp);
						$scope.specialityMst[value.wardId] = true;
						$scope.selectedSpecialityList.push(temp);
					});
					
//					console.log("$scope.common",$scope.common);
				} else if(response.data.status == 'error')
				{
					$rootScope.stopLoader();
					alert("Error In Fetching Data");
				}
			});
		} catch (e) {
			console.log("Exception",e.message);
		}
		
	}
	$scope.updateNursingMapper =  function(){
		/*if($scope.popUpFlag==false)
		{
		  $rootScope.startLoader();
		}*/
		$scope.saveBtnFlag = true;
		$scope.updateBtnFlag = false;
		$scope.currentDate = new Date();
		$scope.nursingMapperObj.createdDate = moment($scope.currentDate)
				.format('DD-MM-YYYY HH:mm:ss');
		
		$scope.nursingMapperObj.updatedDate = moment($scope.currentDate)
			.format('DD-MM-YYYY HH:mm:ss');
		

		$scope.$broadcast('show-errors-check-validity');
		
		$scope.nursingMapperObj.status='A';
		$scope.nursingMapperObj.isDeleted=$scope.isDeleted;
			var URI = BASE_URL + ROOT_URL + "/api/adt/unit/updateNursingStationWardMapper";
			$scope.nursingMapperObj.nursingWardMapperList = angular.copy($scope.selectedSpecialityList);
			console.log("nursingMapperObj",$scope.nursingMapperObj); 
			
			if ($scope.nursingStationWardMappingForm.$valid){
			/*	if ($scope.popUpFlag) {
					$scope.showModal = !$scope.showModal;
	//				return false;
				} else */
				{
			BillingGenericService.serviceAction(METHOD_POST,URI, $scope.nursingMapperObj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
				console.log(response);
				if(response.data.status == "success"){
					
					if(response.data.code==200){
						growl.success("Updated successfully...!!",{
								title : "SUCCESS"
						});
					}
					
//					$scope.$broadcast('show-errors-reset');
					$scope.nursingMapperObj.nursingStationId="";
//					$scope.getNursingStationId($scope.nursingMapperObj.nursingStationId);
					$scope.initBillingGroupMaster();
					$scope.selectedSpecialityList = [];
					$scope.tempSelectSpecialty = [];
					$scope.specialityMst=[];
					$scope.wardList=[];
					uncheckSpecialityList();
					$scope.initNursingMasterMappingList($scope.organizationId,
							$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
					
					$scope.isDeleted='A';
				}else if(response.data.code==409){
					growl.warning("Already Exist...!!",
							{
								title : 'warnings!'
							});
				}else if(response.data.status == "error"){
					growl.error("Something went wrong...!!",
							{
								title : 'Error!'
							});
				}
				
			});
			}
				
		}
	}
	
}]);