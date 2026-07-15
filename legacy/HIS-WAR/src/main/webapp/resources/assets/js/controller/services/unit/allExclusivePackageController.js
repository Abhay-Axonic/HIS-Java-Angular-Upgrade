angular.module("myApp").controller("allExclusivePackageController", [ '$scope','$rootScope','BillingGenericService','$cookies','$state','growl','PagerService',
	function($scope,$rootScope,BillingGenericService,$cookies,$state,growl,PagerService){
	try{
		$rootScope.loginpage = true;
		
		$scope.getPackageTypeList = function(orgId,unitId) {
		    var data = "";
		    var URI = BASE_URL + ROOT_URL + "/Unit/Service/EHCpackage/getPackgeType/"+orgId+"/"+unitId;
		    BillingGenericService.serviceAction("GET", URI, data).then(function(response){
		     $scope.packageTypeList = response.data.listObject;
		    });
		  }
		
		
		  $scope.init = function(){
			  var cookieObject = $cookies.getObject('cookieObject');
				if (cookieObject == undefined) {
					$state.go('login');
					return;
				}
				$scope.unitId = cookieObject.unitId;
				$scope.orgId = cookieObject.organizationId;
				$scope.userId = cookieObject.userId;
				
			  $scope.searchPackageObj = {orgId:$scope.orgId,unitId:$scope.unitId,packageCode:"",packageName:"",packageTypeId:""};
			  $scope.searchPackageList = [];
			  $scope.getPackageTypeList($scope.orgId,$scope.unitId);
//			  $scope.getSearchServicesList($scope.searchPackageObj);
			  /** Variables for pagination */
				$scope.offset = 0;
				$scope.noOfRecordsPerPage = 10;
				$scope.searchKeyword=null;
		  }
		  $scope.init();
		  $scope.getSearchServicesList = function(searchPackageObj) {
			  $rootScope.startLoader();
			  var param = {orgId:$scope.orgId,unitId:$scope.unitId,packageCode:searchPackageObj.packageCode,packageName:searchPackageObj.packageName,packageTypeId:4,searchKeyword: ($scope.searchKeyword != null) ? $scope.searchKeyword : null};
			  	var URI = BASE_URL + ROOT_URL + "/api/packages/filter";
			    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					if (response.data.status == "success"){
						 $scope.searchPackageList = response.data.listObject;
					}
					$rootScope.stopLoader();
				});
			  }
		// For Paginations Start
			$scope.setNoOfRecords = function() {
				$scope.initPackageMasterList($scope.orgId, $scope.unitId,$scope.offset,$scope.noOfRecordsPerPage);
			};
			$scope.initPackageMasterList = function(orgId,unitId,offset, noOfRecordsPerPage) {
				  $rootScope.startLoader();				
				var param = {
								  orgId:$scope.orgId,
								  unitId:$scope.unitId,
								  packageCode:'',
								  packageName:'',
								  packageTypeId:4,
								  offset : offset != null ? offset : 0,
								  noOfRecordsPerPage : noOfRecordsPerPage != null ? noOfRecordsPerPage : 10,
								  searchKeyword: ($scope.searchKeyword != null) ? $scope.searchKeyword : null
						  };
			    var URI = BASE_URL + ROOT_URL + "/api/packages/filter";
			    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					if (response.data.status == "success"){
						 $scope.searchPackageList = response.data.listObject;
						 $scope.commonListCount = response.data.object;
						 $scope.setPage(1,false);
					}
					$rootScope.stopLoader();
				});			
			
			}

			$scope.searchInclusivePackage = function(searchKeyword){

                $scope.loadingMsg="Loading...";
                if(searchKeyword.length>2){

                    $scope.searchPackageList=[];
                    $rootScope.startLoader();
                    var param = {
                          orgId:$scope.orgId,
                          unitId:$scope.unitId,
                          packageCode:'',
                          packageName:'',
                          packageTypeId:4,
                          offset : $scope.offset != null ? $scope.offset : 0,
                          noOfRecordsPerPage : $scope.noOfRecordsPerPage != null ? $scope.noOfRecordsPerPage : 10,
                          searchKeyword: searchKeyword
                    };

                    var URI = BASE_URL + ROOT_URL + "/api/packages/filter";

                    console.log("URI", URI);

                    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
                        if (response.data.status == "success"){
                             $scope.searchPackageList = response.data.listObject;
                             $scope.commonListCount = response.data.object;
                             $scope.setPage(1,false);
                        }
                        $rootScope.stopLoader();
                    });
                }
                if(searchKeyword == undefined || searchKeyword == null || searchKeyword ==""){
                      $scope.initPackageMasterList($scope.orgId, $scope.unitId,$scope.offset,$scope.noOfRecordsPerPage);
                }
            }

			$scope.getPackageMasterList = function(orgId,unitId,offset, noOfRecordsPerPage) {
				$rootScope.startLoader();
				var param = {
						  orgId:$scope.orgId,
						  unitId:$scope.unitId,
						  packageCode:'',
						  packageName:'',
						  packageTypeId:4,
						  offset : offset != null ? offset : 0,
						  noOfRecordsPerPage : noOfRecordsPerPage != null ? noOfRecordsPerPage : 10,
						  searchKeyword: ($scope.searchKeyword != null) ? $scope.searchKeyword : null
				  };
				    var URI = BASE_URL + ROOT_URL + "/api/packages/filter";
				    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
						if (response.data.status == "success"){
							 $scope.searchPackageList = response.data.listObject;
						}
						$rootScope.stopLoader();
					});			

			}
			$scope.pager = {};
			$scope.page;

			$scope.setPage = function(page, flag) {
			    if($scope.pager.totalPages==0){
                    $scope.pager.totalPages=undefined;
                }
				if (page < 1 || page > $scope.pager.totalPages) {
					return;
				}
				$scope.pager = PagerService.GetPager($scope.commonListCount, page,$scope.noOfRecordsPerPage);
				if (flag) {
					$scope.getPackageMasterList($scope.orgId, $scope.unitId,$scope.pager.startIndex,$scope.pager.pageSize);
				}
			}

			$scope.initPackageMasterList($scope.orgId, $scope.unitId,$scope.offset, $scope.noOfRecordsPerPage);
			
			// For Paginations END
		  // calculate packages price
		  /*$scope.calculatePackageAmounts = function()
			{
				angular.forEach($scope.packageObjResponse.paymentEntitlement,function(paymentEnt,indexPaymentEnt)
					{
					angular.forEach(paymentEnt.patientCategoryList, function(patientCat,indexPatientCat){
						angular.forEach(patientCat.billingBedCategoryList,function(billingBedCat,indexBillingBedCat)
								{								
							     var total = 0;
								  angular.forEach(billingBedCat.mPackageMasterDto.listTPackageIncExcServiceDetailsDto,function(serviceDtls,indexServiceDtls)
										  {
									         serviceDtls.total =  isNaN(serviceDtls.servicePrice) ? 0 : parseFloat(serviceDtls.servicePrice);
									         total = parseFloat(total) + (isNaN(serviceDtls.total) ? 0 : parseFloat(serviceDtls.total));
								          });
								  
//								    billingBedCat.mPackageMasterDto.serviceWiseSummary = $rootScope.parseFloatNum(total);
//								    billingBedCat.mPackageMasterDto.packageCost = $rootScope.parseFloatNum(total);
//									billingBedCat.mPackageMasterDto.packagePrice = $rootScope.parseFloatNum(total);
//									billingBedCat.mPackageMasterDto.validityPeriodIndays = billingBedCat.mPackageMasterDto.validityPeriodIndays;
								
						      });	
					});
				});
			}*/
		  $scope.getPackagesDetailsForView=function(obj){
			  $rootScope.startLoader();
			  $rootScope.startLoader();
				var paraentPckId=obj.parentPackageId
				var data={};
				data[paraentPckId]=4
				var visitTypeArray=[];
				console.log("request",data);
				$scope.selectedPaymentEntitlementList=[];
				$scope.selectedPatientTypeList=[];
				$scope.selectedBedBillingCategoryList=[];
				$scope.selectedVisitTypeList=[];
				$scope.serviceList=[];
				$scope.itemList=[];
				var URI = BASE_URL + ROOT_URL + "/api/packages/getPackagesByPackageId";
				BillingGenericService.serviceAction(METHOD_POST,URI, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					if (response.data.status == "success")
					{
						$rootScope.startLoaderPopup();
						$('#viewPackageModal').modal('show');
					   $scope.packageObjResponse= response.data.object;
					   console.log("packageObjResponse",$scope.packageObjResponse);
					   angular.forEach($scope.packageObjResponse.paymentEntitlement,function(paymentEnt,indexPaymentEnt){
							
							angular.forEach(paymentEnt.patientCategoryList, function(patientCat,indexPatientCat){

								angular.forEach(patientCat.billingBedCategoryList,function(billingBedCat,indexBillingBedCat){
									visitTypeArray.push(billingBedCat.mPackageMasterDto.visitTypeId);
									
									 var arr = $scope.selectedBedBillingCategoryList.filter(function(bilingBedCat){
					   	            	   return (bilingBedCat.id == billingBedCat.billingBedCategoryId);
					   				 	});
										if(arr.length <= 0){
											var data={
														id:billingBedCat.billingBedCategoryId,
														label:billingBedCat.billingBedCategory
											     }
											$scope.selectedBedBillingCategoryList.push(data);
					   					 }
																		
								});//billingBedCategory			
								
								
								 var arr = $scope.selectedPatientTypeList.filter(function(patType){
				   	            	   return (patType.id == patientCat.patientTypeId);
				   				 	});
									if(arr.length <= 0){
										var data={
													id:patientCat.patientTypeId,
													label:patientCat.patientType
										     }
										$scope.selectedPatientTypeList.push(data);
				   					 }							
								
							});//patientCategory

							var paymentEntData={
									id:paymentEnt.paymentEntitlementId,
									label:paymentEnt.paymentEntitlement
							}
							$scope.selectedPaymentEntitlementList.push(paymentEntData);
							
							$rootScope.stopLoaderPopup();
						});// payment entitlement end
					   
					   var uniqueVisitType=new Set(visitTypeArray);
					   $scope.selectedVisitTypeList = Array.from(uniqueVisitType);
					   console.log("$scope.selectedVisitTypeList",$scope.selectedVisitTypeList);
//					   $scope.calculatePackageAmounts();
						$rootScope.stopLoader();
					}	
					else if (response.data.status == "error"){
						$rootScope.stopLoader();
					}
			  });
		  }
		  
		  $scope.getActiveGenderList = function()
			{
				var data = "";
				var URI = BASE_URL + ROOT_URL + GETACTIVEGENDERLIST;
				BillingGenericService.serviceAction("GET", URI, data).then(function(response){
					$scope.genderList= response.data.listObject;
				});
		    }
		  $scope.getActiveGenderList();
		  
		  $scope.getGenderNameById=function(genderId){
			  if(genderId!=undefined && genderId!=null && genderId!='' && genderId>0){
	    			var genderName = $.grep($scope.genderList, function (gen) {
			  	           return gen.id == genderId;
			  	       })[0].desc;
					  return genderName;
	    	 }else if(genderId==0){
	    		 return 'All';
	    	 }
		  }
		 
		  $scope.updateStatus=function(parentPkgId,status){
				var data = "";
				status=status=='A' ? true : false;
				$rootScope.startLoader();
				var URI = BASE_URL + ROOT_URL + "/api/packages/inActivePackage/parentPkgId/"+parentPkgId+"/PackageTypeId/4/activeInactive/"+status;
				BillingGenericService.serviceAction("GET", URI, data).then(function(response){
					if(response.data.status == "success")
					{
						growl.success("Package inactivated.",{title : 'Success!'});
						$rootScope.stopLoader();
					}else{
						$rootScope.stopLoader();
					}
					$scope.initPackageMasterList($scope.orgId, $scope.unitId,$scope.offset, $scope.noOfRecordsPerPage);
				});
			}
		  
	}catch(e){
		console.log(e.message);
	}
	
		$scope.editPackage = function(obj){
    	$state.go("addNewExclusive",{
        					 newAllExclusiveObj :obj
        					});
    	}

}]);