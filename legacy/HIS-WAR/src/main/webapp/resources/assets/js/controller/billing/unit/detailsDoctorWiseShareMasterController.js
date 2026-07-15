angular.module("myApp").controller("detailsDoctorWiseShareMasterController", [ '$scope','$rootScope','BillingGenericService','$cookies','$state','growl','servicesFactory','$stateParams',
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
		
		$scope.dateOptions = {
				formatYear : 'yyyy',
				showWeeks : false
		};
		
		$scope.openDatePickerPatiSearch = function() {
			$scope.datepicker1.opened = true;
		};

		$scope.datepicker1 = {
				opened : false
		};
		//$scope.common = null;
		
		if($stateParams.doctorShareObj !== null || $stateParams.doctorShareObj != undefined)
			{
			$scope.common = $stateParams.doctorShareObj;
			console.log("DoctorGlobalShareObj:",$scope.common);
			
			}
		
		
		
		$scope.doctorShareGroupList=[];
		$scope.doctorShareServiceList=[];
		$scope.getDoctorShareDetailList=function(){
			try 
			{
				$rootScope.startLoader();
				 var data={
						 doctorId:$scope.common.doctorId,
						 specialityId:$scope.common.specialityId,
						 orgId:$scope.orgId,
					     unitId:$scope.unitId
				 };
				 if(data.specialityId!=undefined && data.doctorId!=undefined){
				 var URI = BASE_URL + ROOT_URL + "/unit/DoctorShare/getDoctorShareDetailsByDepartmentIdAndDoctorId";
				  BillingGenericService.serviceAction("POST", URI, data,NOTIFICATION_MSG_STATUS_FALSE)
				.then(
						function(response) {
							//var list=response.data.object.listTIndividualDocShareGroupWiseDto;
							//if(list.length>0 && list!=undefined &&  list!=null)
							$scope.doctorShareGroupList=response.data.object.listTIndividualDocShareGroupWiseDto;
							//else	
							$scope.doctorShareServiceList=response.data.object.listTIndividualDocShareServiceWiseDto;

							//console.log("Group List:",$scope.doctorShareGroupList);
							//console.log("service List:",$scope.doctorShareServiceList);
							$rootScope.stopLoader();
						});
				 }
				  
			} catch (e) {
				console.log(e.message)
			}
		}
		$scope.getDoctorShareDetailList();

	}]);