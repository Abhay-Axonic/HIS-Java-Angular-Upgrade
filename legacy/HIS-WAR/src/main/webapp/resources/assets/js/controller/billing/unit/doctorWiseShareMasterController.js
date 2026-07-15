angular.module("myApp").controller("doctorWiseShareMasterController", [ '$scope','$rootScope','BillingGenericService','$cookies','$state','growl','servicesFactory','PagerService',
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
		
		$scope.doctorWiseShareSearchObj={
				specialityId:null,
				doctorId:null
				
		};
		
		/** Variables for pagination */
		$scope.offset = 0;
		$scope.noOfRecordsPerPage = 10;
		$scope.totalNoOfRecords;
		$scope.searchKeyword;
		$scope.commonListCount;
		
		//get speciality list for autocomplete
		  $scope.getSpecialityList = function(){
			  var URI = BASE_URL + ROOT_URL + "/adt/getActiveSpecialityList";
			  var param = {organizationId:$scope.orgId,unitId:$scope.unitId};
			    BillingGenericService.serviceAction("POST", URI, param).then(function(response){
			    	if(response.data.status == "success")
			    		$scope.specialityList = response.data.listObject;
			    	 console.log("Doctor Share specialityList",JSON.stringify($scope.specialityList));
			    });
		  }
		  $scope.getSpecialityList();
		  
			//========================= get Doctor By Speciality=====================
			$scope.getDoctorBySpeciality = function(id) {
				$scope.doctorWiseShareSearchObj.doctorId = '';
				if(id > 0){
				var data = {
					specialityId : id,
					unitId : $scope.unitId,
					organizationId : $scope.organizationId
				};
				var URI = BASE_URL + ROOT_URL
						+ GETDOCTORBYSPECIALITYID;
				
				BillingGenericService
						.serviceAction(METHOD_POST, URI, data,
								NOTIFICATION_MSG_STATUS_FALSE)
						.then(
								function(response) {
									console.log(response);
									$scope.doctorList = [];
									// $rootScope.startLoader();
									if (response.data.status == "success")
										$scope.doctorList = response.data.listObject;
								});
			}
				
			}
		
		$scope.doctorShareSpecialityList=[];
		$scope.getInitDoctorShareDepartmentMasterList=function(orgId,unitId, offset,noOfRecordsPerPage){
			try 
			{
				/*if($scope.doctorWiseShareSearchObj.specialityId == '' || $scope.doctorWiseShareSearchObj.specialityId == null || $scope.doctorWiseShareSearchObj.specialityId == undefined)
					{
					  growl.warning("Please select specialty.",{
							title : WARNING_MSG
						});
						return;
					
					}else if($scope.doctorWiseShareSearchObj.doctorId == '' || $scope.doctorWiseShareSearchObj.doctorId == null || $scope.doctorWiseShareSearchObj.doctorId == undefined)
						{
						  growl.warning("Please select doctor.",{
								title : WARNING_MSG
							});
							return;
						
						}else{*/
				offset = offset != null ? offset : 0;
				noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage: 10;
				
				 data={
					orgId:$scope.orgId,
					unitId:$scope.unitId,
					 noOfRecordsPerPage : noOfRecordsPerPage,
					 offset : offset,
					specialityId: $scope.doctorWiseShareSearchObj.specialityId!=null || $scope.doctorWiseShareSearchObj.specialityId!='' || $scope.doctorWiseShareSearchObj.specialityId!=undefined? $scope.doctorWiseShareSearchObj.specialityId:null,
					doctorId:$scope.doctorWiseShareSearchObj.doctorId!=null  || $scope.doctorWiseShareSearchObj.doctorId!='' || $scope.doctorWiseShareSearchObj.doctorId!=undefined? $scope.doctorWiseShareSearchObj.doctorId:null
				 
				 };
				 var URI = BASE_URL + ROOT_URL + "/unit/DoctorShare/getDoctorShareDepartmentMasterList";
				  console.log("URI", URI);
				  BillingGenericService.serviceAction("POST", URI, data,NOTIFICATION_MSG_STATUS_FALSE)
				.then(
						function(response) {
							$scope.doctorShareSpecialityList=response.data.listObject;
							$scope.commonListCount= response.data.object;
							$scope.setPage(1,false);
							console.log("Dept List:",$scope.doctorShareSpecialityList);
						});
			/*}*/
				  
			} catch (e) {
				console.log(e.message)
			}
		}
		
		$scope.getInitDoctorShareDepartmentMasterList($scope.organizationId,$scope.unitId, $scope.offset,$scope.noOfRecordsPerPage);
		
		
		
		$scope.getDoctorShareDepartmentMasterList=function(orgId,unitId, offset,noOfRecordsPerPage){
			try 
			{
				/*if($scope.doctorWiseShareSearchObj.specialityId == '' || $scope.doctorWiseShareSearchObj.specialityId == null || $scope.doctorWiseShareSearchObj.specialityId == undefined)
					{
					  growl.warning("Please select specialty.",{
							title : WARNING_MSG
						});
						return;
					
					}else if($scope.doctorWiseShareSearchObj.doctorId == '' || $scope.doctorWiseShareSearchObj.doctorId == null || $scope.doctorWiseShareSearchObj.doctorId == undefined)
						{
						  growl.warning("Please select doctor.",{
								title : WARNING_MSG
							});
							return;
						
						}else{*/
				offset = offset != null ? offset : 0;
				noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage: 10;
				
				 data={
					orgId:$scope.orgId,
					unitId:$scope.unitId,
					 noOfRecordsPerPage : noOfRecordsPerPage,
					 offset : offset,
					specialityId: $scope.doctorWiseShareSearchObj.specialityId!=null || $scope.doctorWiseShareSearchObj.specialityId!='' || $scope.doctorWiseShareSearchObj.specialityId!=undefined? $scope.doctorWiseShareSearchObj.specialityId:null,
					doctorId:$scope.doctorWiseShareSearchObj.doctorId!=null  || $scope.doctorWiseShareSearchObj.doctorId!='' || $scope.doctorWiseShareSearchObj.doctorId!=undefined? $scope.doctorWiseShareSearchObj.doctorId:null
				 
				 };
				 var URI = BASE_URL + ROOT_URL + "/unit/DoctorShare/getDoctorShareDepartmentMasterList";
				  console.log("URI", URI);
				  BillingGenericService.serviceAction("POST", URI, data,NOTIFICATION_MSG_STATUS_FALSE)
				.then(
						function(response) {
							$scope.doctorShareSpecialityList=response.data.listObject;
							$scope.commonListCount= response.data.object;
//							$scope.setPage(1,false);
							console.log("Dept List:",$scope.doctorShareSpecialityList);
						});
			/*}*/
				  
			} catch (e) {
				console.log(e.message)
			}
		}
		
		
		$scope.getDoctorShareDepartmentMasterListSearch=function(searchText){
			try 
			{
				/*if($scope.doctorWiseShareSearchObj.specialityId == '' || $scope.doctorWiseShareSearchObj.specialityId == null || $scope.doctorWiseShareSearchObj.specialityId == undefined)
					{
					  growl.warning("Please select specialty.",{
							title : WARNING_MSG
						});
						return;
					
					}else if($scope.doctorWiseShareSearchObj.doctorId == '' || $scope.doctorWiseShareSearchObj.doctorId == null || $scope.doctorWiseShareSearchObj.doctorId == undefined)
						{
						  growl.warning("Please select doctor.",{
								title : WARNING_MSG
							});
							return;
						
						}else{*/
				
			if(searchText!=''){
				
				
				 data={
					orgId:$scope.orgId,
					unitId:$scope.unitId,
					 offset : $scope.offset,
					 noOfRecordsPerPage:$scope.noOfRecordsPerPage,
					 specialitySerarchName:searchText,
					specialityId: $scope.doctorWiseShareSearchObj.specialityId!=null || $scope.doctorWiseShareSearchObj.specialityId!='' || $scope.doctorWiseShareSearchObj.specialityId!=undefined? $scope.doctorWiseShareSearchObj.specialityId:null,
					doctorId:$scope.doctorWiseShareSearchObj.doctorId!=null  || $scope.doctorWiseShareSearchObj.doctorId!='' || $scope.doctorWiseShareSearchObj.doctorId!=undefined? $scope.doctorWiseShareSearchObj.doctorId:null
				 
				 };
				 var URI = BASE_URL + ROOT_URL + "/unit/DoctorShare/getDoctorShareDepartmentMasterListSearch";
				  console.log("URI", URI);
				  BillingGenericService.serviceAction("POST", URI, data,NOTIFICATION_MSG_STATUS_FALSE)
				.then(
						function(response) {
							$scope.doctorShareSpecialityList=response.data.listObject;
							if(response.data.listObject.length>0){
								$scope.commonListCount= response.data.listObject.length;
								$scope.setPage(1,false);
							}
							
							console.log("Dept List:",$scope.doctorShareSpecialityList);
						});
				  
			}else{
				$scope.getInitDoctorShareDepartmentMasterList($scope.organizationId,$scope.unitId, $scope.offset,
						$scope.noOfRecordsPerPage);
			}
			
			/*}*/
				  
			} catch (e) {
				console.log(e.message)
			}
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
				$scope.getDoctorShareDepartmentMasterList($scope.organizationId,$scope.unitId, $scope.pager.startIndex, $scope.pager.pageSize);
			}
		}
		$scope.setNoOfRecords = function() {
			$scope.getInitDoctorShareDepartmentMasterList($scope.organizationId,$scope.unitId, $scope.offset,
					$scope.noOfRecordsPerPage);
		};
		
		
		$scope.getDoctorShareDetailsById = function(info){
			console.log("info",info);
			$state.go('detailsDoctorWiseShare',
					{
				doctorShareObj: info
					});
		}
		    
	}]);