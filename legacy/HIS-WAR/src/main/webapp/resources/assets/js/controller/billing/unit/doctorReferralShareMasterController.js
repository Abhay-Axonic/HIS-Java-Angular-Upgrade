angular.module("myApp").controller("doctorReferralShareMasterController", [ '$scope','$rootScope','BillingGenericService','$cookies','$state','growl','servicesFactory','PagerService',
	function($scope,$rootScope,BillingGenericService,$cookies,$state,growl,servicesFactory,PagerService){
	try{
		$rootScope.loginpage = true;
		
		var cookieObject = $cookies.getObject('cookieObject');
		if (cookieObject == undefined) {
			$state.go('login');
			return;
		}
		/** Variables for pagination */
		$scope.offset = 0;
		$scope.noOfRecordsPerPage = 10;
		$scope.totalNoOfRecords;
		$scope.searchKeyword;
		$scope.commonListCount;
		
		$scope.unitId = cookieObject.unitId;
		$scope.orgId = cookieObject.organizationId;
		$scope.userId = cookieObject.userId;
		
		$scope.getReferralList = function(){
			var URI = BASE_URL + ROOT_URL + GETACTIVEREFERRALTYPELIST;
			var obj={organizationId : $scope.orgId};
				BillingGenericService.serviceAction(METHOD_POST, URI, obj).then(function(response) {
						$scope.referralTypeList = [];
						if (response.data.status == "success") {
							$scope.referralTypeList = response.data.listObject;
						}
				});
			};
		$scope.getReferralList();
		
		
		  $scope.getReferralMasterList = function(){
				var URI = BASE_URL + ROOT_URL + "/adt/getReferralListByReferralTypeId";
				var obj=
				      { 
						 organizationId:$scope.orgId,
						 referralTypeId:$scope.referralTypeId
				      };
					BillingGenericService.serviceAction(METHOD_POST, URI, obj).then(function(response) {
							$scope.referralMasterList = [];
							if (response.data.status == "success") {
								$scope.referralMasterList = response.data.listObject;
							}
					});
				};
			
			 /*$scope.visitTypeName = function(id){
					for(var i = 0; i < $scope.visityTypeList.length ; i++){
						if($scope.visityTypeList[i].id == id)
						return $scope.visityTypeList[i].name;
					}
			   }*/
			 
			 $scope.getReferralDocName = function(id){
					for(var i = 0; i < $scope.referralMasterList.length ; i++){
						if($scope.referralMasterList[i].referralId == id)
						return $scope.referralMasterList[i].desc;
					}
			   }
			
			 $scope.getDoctorReferralDetails = function(orgId,unitId, offset,noOfRecordsPerPage){
				 
				    offset = offset != null ? offset : 0;
					noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage: 10;
				 
					var URI = BASE_URL + ROOT_URL + "/unit/DoctorShare/getIndividualDoctorReferralDetailsByDepartmentId";
					var obj=
					      { 
							orgId:$scope.orgId,
							unitId:$scope.unitId,
							referralId:$scope.referralDocId
					      };
					$rootScope.startLoader();
						BillingGenericService.serviceAction(METHOD_POST, URI, obj).then(function(response) {
								$scope.referralMasterDetailsList = [];
								if (response.data.status == "success") {
									$scope.referralMasterDetailsList = response.data.listObject;
									$scope.commonListCount= response.data.object;
//									$scope.setPage(1,false);
								}
								$rootScope.stopLoader();
						});
						
					};
					
					$scope.getInitDoctorReferralDetails = function(orgId,unitId, offset,noOfRecordsPerPage){
						 
					    offset = offset != null ? offset : 0;
						noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage: 10;
					 
						var URI = BASE_URL + ROOT_URL + "/unit/DoctorShare/getIndividualDoctorReferralDetailsByDepartmentId";
						var obj=
						      { 
								 orgId:$scope.orgId,
								 unitId:$scope.unitId,
								 referralId:$scope.referralDocId,
								 noOfRecordsPerPage : noOfRecordsPerPage,
								 offset : offset,
						      };
						$rootScope.startLoader();
							BillingGenericService.serviceAction(METHOD_POST, URI, obj).then(function(response) {
									$scope.referralMasterDetailsList = [];
									if (response.data.status == "success") {
										$scope.referralMasterDetailsList = response.data.listObject;
										$scope.commonListCount= response.data.object;
										$scope.setPage(1,false);
//										console.log("Dept List:",$scope.doctorShareSpecialityList);
									}
									$rootScope.stopLoader();
							});
							
						};
					$scope.getInitDoctorReferralDetails($scope.organizationId,$scope.unitId, $scope.offset,$scope.noOfRecordsPerPage);
					
					
					$scope.getDoctorReferralDetailsSearch = function(SearchText){
						 
						if(SearchText!=''){
							
						
						var URI = BASE_URL + ROOT_URL + "/unit/DoctorShare/getIndividualDoctorReferralDetailsByDepartmentIdSearch";
						var obj=
						      { 
								 orgId:$scope.orgId,
								 unitId:$scope.unitId,
								 referralId:$scope.referralDocId,
								 noOfRecordsPerPage : $scope.noOfRecordsPerPage,
								 offset :  $scope.offset,
								 specialitySerarchName:SearchText,
						      };
						$rootScope.startLoader();
							BillingGenericService.
							serviceAction(METHOD_POST, URI, obj).then(function(response) {
									$scope.referralMasterDetailsList = [];
									if (response.data.status == "success") {
										$scope.referralMasterDetailsList = response.data.listObject;
										if( response.data.listObject.length>0){
											$scope.commonListCount= response.data.listObject.length;
											$scope.setPage(1,false);
										}
										
//										console.log("Dept List:",$scope.doctorShareSpecialityList);
									}
									$rootScope.stopLoader();
							});
						}else{
							$scope.getInitDoctorReferralDetails($scope.organizationId,$scope.unitId, $scope.offset,
									$scope.noOfRecordsPerPage);
							
						}
						
						
						};
					
					
					
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
							$scope.getDoctorReferralDetails($scope.organizationId,$scope.unitId, $scope.pager.startIndex, $scope.pager.pageSize);
						}
					}
					$scope.setNoOfRecords = function() {
						$scope.getInitDoctorReferralDetails($scope.organizationId,$scope.unitId, $scope.offset,
								$scope.noOfRecordsPerPage);
					};
					
			 /*$scope.getReferralDoctorDetails=function(referralId){
				 if(referralId!=0){
					 
				 }
				
			 }*/
			 //get configuration details by referral doc id
			 $scope.getRferralId=function(info){
				
					 $state.go('detailsDoctorReferralShareMaster',
								{
						 			doctorReferralObj : info,
								});
					 /*
					 var URI = BASE_URL + ROOT_URL + "/unit/DoctorShare/getDoctorReferralShareDetailsByRefDocId";
						var obj=
						      { 
								orgId:$scope.orgId,
								unitId:$scope.unitId,
								docReferralId:referralId
						      };
							BillingGenericService.serviceAction(METHOD_POST, URI, obj).then(function(response) {
									$scope.referralConfigurationDetailsList = [];
									if (response.data.status == "success") {
										$scope.referralConfigurationDetailsList = response.data.listObject;
									}
							});
					 */
							
			 }
			 
			 $scope.updateStatus = function(id, type) {
				 
				 
				 console.log("status changing",type,id);
				 

					$scope.currentDate = new Date();
					$scope.currentDate = moment($scope.currentDate)
							.format('DD-MM-YYYY HH:mm:ss');

					type = (type == "A") ? "A" : "I";

					var data = {
						referralId : id,
						status : type,
						updatedBy : $scope.userId,
						updatedDate : $scope.currentDate
					};
					console.log(data);
					$rootScope.startLoader();
					var URI = BASE_URL + ROOT_URL+"/unit/DoctorShare/updateStatusByReferralId" ;
					
					BillingGenericService.serviceAction("POST", URI, data)
							.then(
									function(response) {

										if (response.data.status == "success") {
											growl.success(
															'Status Updated Successfully...!!',
															{
																title : 'Success!'
															});
											$rootScope.startLoader();
											$scope.getDoctorReferralDetails( $scope.organizationId,$scope.unitId, $scope.offset, $scope.noOfRecordsPerPage);
										} else {
											growl.warning( response.data.message,
													{
														title : 'Warning!'
													});
											$scope.getDoctorReferralDetails( $scope.organizationId,$scope.unitId, $scope.offset, $scope.noOfRecordsPerPage);
										}
										$rootScope.stopLoader();
									});
				 
							 }
			 
		
		  
	}catch(e){
		console.log(e.message);
	}

	
	
}]);

