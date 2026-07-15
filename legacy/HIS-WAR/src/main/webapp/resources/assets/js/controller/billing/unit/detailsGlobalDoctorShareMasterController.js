angular.module("myApp").controller("detailsGlobalDoctorShareMasterController", [ '$scope','$rootScope','BillingGenericService','$cookies','$state','growl','servicesFactory','$stateParams',
	function($scope,$rootScope,BillingGenericService,$cookies,$state,growl,servicesFactory,$stateParams){
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
		
		$scope.common = $stateParams.doctorShareObjResult;
		
		console.log("DoctorGlobalShareObj:",$scope.common);
		
		$scope.doctorGlobalShareGroupList=[];
		$scope.doctorGlobalShareServiceList=[];
		$scope.getGlobalDoctorShareDepartmentMasterList=function(){
			try 
			{
				$rootScope.startLoader();
				
				 data={
						 gradeId:$scope.common.gradeId,
						 departmentId:$scope.common.specialityId,
						 orgId:$scope.orgId,
					     unitId:$scope.unitId
				 };
				 
				 if(data.departmentId!=undefined){
					 
				 var URI = BASE_URL + ROOT_URL + "/unit/DoctorShare/getGlobalDoctorShareDetailsByDepartmentId";
				  console.log("URI", URI);
				  BillingGenericService.serviceAction("POST", URI, data,NOTIFICATION_MSG_STATUS_FALSE)
				.then(
						function(response) {
							$scope.doctorGlobalShareGroupList=response.data.object.listTGlobalDocShareGroupWiseDto;
							$scope.doctorGlobalShareServiceList=response.data.object.listTGlobalDocShareServiceWiseDto;
							console.log("Group List:",$scope.doctorGlobalShareGroupList);
							console.log("service List:",$scope.doctorGlobalShareServiceList);
							$rootScope.stopLoader();
						});
				 }
				  
			} catch (e) {
				console.log(e.message)
			}
		}
		$scope.getGlobalDoctorShareDepartmentMasterList();

	}]);