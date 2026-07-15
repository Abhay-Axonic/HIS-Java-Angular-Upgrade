angular.module("myApp").controller("globalDoctorShareMasterController", [ '$scope','$rootScope','BillingGenericService','$cookies','$state','growl','servicesFactory','PagerService',
	function($scope,$rootScope,BillingGenericService,$cookies,$state,growl,servicesFactory,PagerService){
		$rootScope.loginpage = true;
		
		var cookieObject = $cookies.getObject('cookieObject');
		if (cookieObject == undefined) {
			$state.go('login');
			return;
		}
		$scope.unitId = cookieObject.unitId;
		$scope.orgId= cookieObject.organizationId;
		$scope.organizationId =cookieObject.organizationId;
		$scope.userId = cookieObject.userId;
		
		$scope.globalShareSearchObj={
				specialityId:null
				
		};
		
		
		
		/** Variables for pagination */
		$scope.offset = 0;
		$scope.noOfRecordsPerPage = 10;
		$scope.totalNoOfRecords;
		$scope.searchKeyword;
		$scope.commonListCount;
		
		//get Doctor Grade List
		/*$scope.getDoctorGradeList = function() {
			var URI = BASE_URL + ROOT_URL + "/unit/DoctorShare/getDoctorGradeMaster/"+$scope.orgId+"/"+$scope.unitId;
			BillingGenericService.serviceAction("GET", URI, null).then(function(response){
				$scope.doctorGradeList= response.data.listObject;
				 console.log("Global doctorGradeList",$scope.doctorGradeList);
			});
	   }*/
		//$scope.getDoctorGradeList();
		$scope.globalSpecialityList=[];
		//get speciality list for autocomplete
		  $scope.getSpecialityList = function(){
			  var URI = BASE_URL + ROOT_URL + "/adt/getActiveSpecialityList";
			  var param = {organizationId:$scope.orgId,unitId:$scope.unitId};
			    BillingGenericService.serviceAction("POST", URI, param).then(function(response){
			    	if(response.data.status == "success")
			    		$scope.globalSpecialityList = response.data.listObject;
			    	 console.log("Global specialityList",$scope.specialityList);
			    });
		  }
		  $scope.getSpecialityList();
		
		$scope.globalShareSpecialityList=[];
		
		$scope.getinitGlobalDoctorShareDepartmentMasterList=function(orgId,unitId, offset,
				noOfRecordsPerPage){ 
			try 
			{
				offset = offset != null ? offset : 0;
				noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
						: 10;
				 data={
						 orgId:$scope.orgId,
						 unitId:$scope.unitId,
						 noOfRecordsPerPage : noOfRecordsPerPage,
						 offset : offset,
						 departmentId:$scope.globalShareSearchObj.specialityId!=null?$scope.globalShareSearchObj.specialityId:null
				 }
					 
				 var URI = BASE_URL + ROOT_URL + "/unit/DoctorShare/getGlobalDoctorShareDepartmentMasterList";
				  console.log("URI", URI);
				  BillingGenericService.serviceAction("POST", URI, data,NOTIFICATION_MSG_STATUS_FALSE)
				.then(
						function(response) {
							$scope.globalShareSpecialityList=[];
							$scope.globalShareSpecialityList=response.data.listObject;
							console.log("Dept List:",$scope.globalShareSpecialityList);
							$scope.commonListCount= response.data.object;
							$scope.setPage(1,false);
						});
				  
			} catch (e) {
				console.log(e.message)
			}
		}
		
		$scope.getGlobalDoctorShareDepartmentMasterList=function(orgId,unitId, offset,
				noOfRecordsPerPage){ 
			try 
			{
				offset = offset != null ? offset : 0;
				noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
						: 10;
				 data={
						 orgId:$scope.orgId,
						 unitId:$scope.unitId,
						 noOfRecordsPerPage : noOfRecordsPerPage,
						 offset : offset,
						 departmentId:$scope.globalShareSearchObj.specialityId!=null?$scope.globalShareSearchObj.specialityId:null
				 }
					 
				 var URI = BASE_URL + ROOT_URL + "/unit/DoctorShare/getGlobalDoctorShareDepartmentMasterList";
				  console.log("URI", URI);
				  BillingGenericService.serviceAction("POST", URI, data,NOTIFICATION_MSG_STATUS_FALSE)
				.then(
						function(response) {
							$scope.globalShareSpecialityList=[];
							$scope.globalShareSpecialityList=response.data.listObject;
							console.log("Dept List:",$scope.globalShareSpecialityList);
							$scope.commonListCount= response.data.object;
//							$scope.setPage(1,false);
						});
				  
			} catch (e) {
				console.log(e.message)
			}
		}
		
		$scope.getinitGlobalDoctorShareDepartmentMasterList($scope.organizationId,$scope.unitId, $scope.offset,
				$scope.noOfRecordsPerPage);
		
		$scope.getDoctorShareDetails = function(info){
			$state.go('detailsGlobalDoctorShare',
					{
					doctorShareObjResult: info
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
					$scope.noOfRecordsPerPage);
			if (flag) {
				$scope.getGlobalDoctorShareDepartmentMasterList($scope.organizationId,$scope.unitId, $scope.pager.startIndex, $scope.pager.pageSize);
			}
		}
		$scope.setNoOfRecords = function() {
			$scope.getinitGlobalDoctorShareDepartmentMasterList($scope.organizationId,$scope.unitId, $scope.offset,
					$scope.noOfRecordsPerPage);
		};
		   
		$scope.getGlobalDoctorShareDepartmentMasterListSearch=function(searchText){ 
			try 
			{
			  if(searchText!=''){
				
				
				 data={
						 orgId:$scope.orgId,
						 unitId:$scope.unitId,
						 specialitySerarchName:searchText,
						 offset : $scope.offset,
						 noOfRecordsPerPage:$scope.noOfRecordsPerPage,
						 departmentId:$scope.globalShareSearchObj.specialityId!=null?$scope.globalShareSearchObj.specialityId:null
				 }
					 
				 var URI = BASE_URL + ROOT_URL + "/unit/DoctorShare/getGlobalDoctorShareDepartmentMasterListSearch";
				  console.log("URI", URI);
				  BillingGenericService.serviceAction("POST", URI, data,NOTIFICATION_MSG_STATUS_FALSE)
				.then(
						function(response) {
							$scope.globalShareSpecialityList=[];
							$scope.globalShareSpecialityList=response.data.listObject;
							console.log("Dept List:",$scope.globalShareSpecialityList);
							if(response.data.listObject.length>0){
								$scope.commonListCount=response.data.listObject.length;
								$scope.setPage(1,false);	
							}
							
						});
			}else{
				$scope.getinitGlobalDoctorShareDepartmentMasterList($scope.organizationId,$scope.unitId, $scope.offset,
						$scope.noOfRecordsPerPage);
			}
			} catch (e) {
				console.log(e.message)
			}
		}
		
		
		
	}]);