angular.module("myApp").controller("eitherOrPackageController", [ '$scope','$rootScope','BillingGenericService','$cookies','$state','growl','PagerService',
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
				
			  $scope.getPackageTypeList($scope.orgId,$scope.unitId);
			  $scope.searchPackageObj = {orgId:$scope.orgId,unitId:$scope.unitId,packageCode:"",packageName:"",packageTypeId:2};
			  $scope.searchPackageList = [];
			  $scope.offset = 0;
				$scope.noOfRecordsPerPage = 10;
//			  $scope.getSearchServicesList($scope.searchPackageObj);
		  }
		  $scope.init();
		  $scope.getSearchServicesList = function(searchPackageObj) {
			  $rootScope.startLoader();
			  var param = {orgId:$scope.orgId,unitId:$scope.unitId,packageCode:searchPackageObj.packageCode,packageName:searchPackageObj.packageName,packageTypeId:2};
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
								  packageTypeId:2,
								  offset : offset != null ? offset : 0,
								  noOfRecordsPerPage : noOfRecordsPerPage != null ? noOfRecordsPerPage : 10
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
			$scope.getPackageMasterList = function(orgId,unitId,offset, noOfRecordsPerPage) {
				$rootScope.startLoader();
				var param = {
						  orgId:$scope.orgId,
						  unitId:$scope.unitId,
						  packageCode:'',
						  packageName:'',
						  packageTypeId:2,
						  offset : offset != null ? offset : 0,
						  noOfRecordsPerPage : noOfRecordsPerPage != null ? noOfRecordsPerPage : 10
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
		  $scope.calculateSummaryAmounts = function()
			{
				 angular.forEach($scope.packageObjResponse.paymentEntitlement,function(paymentEnt,indexPaymentEnt)
						{
						angular.forEach(paymentEnt.patientCategoryList, function(patientCat,indexPatientCat){
							angular.forEach(patientCat.billingBedCategoryList,function(billingBedCat,indexBillingBedCat)
									{								
								     var temp = 0;
//								      billingBedCat.mPackageMasterDto.packagePrice = $rootScope.parseFloatNum(0);
									  angular.forEach(billingBedCat.mPackageMasterDto.listTPackageServicesDetailsDto,function(serviceDtls,indexServiceDtls)
											  {
										         serviceDtls.numberToBeUse= (serviceDtls.numberToBeUse==null)?1:serviceDtls.numberToBeUse;
										         serviceDtls.total = serviceDtls.numberToBeUse * serviceDtls.servicePrice;
										         temp = temp + parseFloat(serviceDtls.total);
									          });
									    billingBedCat.mPackageMasterDto.serviceGroupAmt = $rootScope.parseFloatNum(temp);
										temp = parseFloat(0);
										angular.forEach(billingBedCat.mPackageMasterDto.listTPackageEitherorGroupDetailsDto, function(value,index)
										{
											value.numberServiceUsable=value.numberServiceUsable!="" && value.numberServiceUsable!=undefined && value.numberServiceUsable!=null ? value.numberServiceUsable : 1;
												temp = temp + parseFloat(value.groupPrice);
										});
										billingBedCat.mPackageMasterDto.packageGroupAmt =  $rootScope.parseFloatNum(temp);
										billingBedCat.mPackageMasterDto.packageCost = $rootScope.parseFloatNum(parseFloat(billingBedCat.mPackageMasterDto.serviceGroupAmt) + parseFloat(billingBedCat.mPackageMasterDto.packageGroupAmt));
//										billingBedCat.mPackageMasterDto.packagePrice = $rootScope.parseFloatNum(parseFloat(billingBedCat.mPackageMasterDto.serviceGroupAmt) + parseFloat(billingBedCat.mPackageMasterDto.packageGroupAmt));
										billingBedCat.mPackageMasterDto.validityPeriodIndays= (billingBedCat.mPackageMasterDto.validityPeriodIndays==null || billingBedCat.mPackageMasterDto.validityPeriodIndays==undefined || billingBedCat.mPackageMasterDto.validityPeriodIndays=='') ? 1 : billingBedCat.mPackageMasterDto.validityPeriodIndays;
										billingBedCat.mPackageMasterDto.validityPeriodIndays = (parseInt(billingBedCat.mPackageMasterDto.validityPeriodIndays)<0 || parseInt(billingBedCat.mPackageMasterDto.validityPeriodIndays)==0) ? 1 : billingBedCat.mPackageMasterDto.validityPeriodIndays ;
									
							      });	
						});
					});
			}
		  $scope.viewPackageGroup = function(packageGroup){
				$scope.viewPackageGroupServices = packageGroup.listTPackageServicesDetailsDto;
				$scope.viewPackageObj = packageGroup;
			}
		  
		  $scope.getPackagesDetailsForView=function(obj){
			    $rootScope.startLoader();
			     $rootScope.startLoader();
				var paraentPckId=obj.parentPackageId
				var data={};
				data[paraentPckId]=2;
				var visitTypeArray=[];
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
									
									  var total = 0;
									  angular.forEach(billingBedCat.mPackageMasterDto.listTPackageServicesDetailsDto,function(serviceDtls,indexServiceDtls)
											  {
										         serviceDtls.total = serviceDtls.numberToBeUse * serviceDtls.servicePrice;
										         total = parseFloat(total) + (isNaN(serviceDtls.total) ? 0 : parseFloat(serviceDtls.total));
									          });
									  billingBedCat.mPackageMasterDto.serviceGroupAmt = $rootScope.parseFloatNum(total);
									  
									  var total = 0;
									  angular.forEach(billingBedCat.mPackageMasterDto.listTPackageEitherorGroupDetailsDto,function(grpDtls,indexServiceDtls)
											  {
										         total = parseFloat(total) + (isNaN(grpDtls.groupPrice) ? 0 : parseFloat(grpDtls.groupPrice));
									          });
									  
									  billingBedCat.mPackageMasterDto.packageGroupAmt = $rootScope.parseFloatNum(total);
									
									  billingBedCat.mPackageMasterDto.grandTotal = parseFloat($rootScope.parseFloatNum(billingBedCat.mPackageMasterDto.packageGroupAmt))+parseFloat((billingBedCat.mPackageMasterDto.serviceGroupAmt));
									  $scope.roundOffPositNeg =  billingBedCat.mPackageMasterDto.roundOffPositNeg;
									  $scope.markupDownInPercentage  =  billingBedCat.mPackageMasterDto.markupDownInPercentage;
									  $scope.isMarkupDown  =  billingBedCat.mPackageMasterDto.isMarkupDown;
									  $scope.roundOffPositNeg =  billingBedCat.mPackageMasterDto.roundOffPositNeg;
									  
									  var billingBedCategoryData={
												id:billingBedCat.billingBedCategoryId,
												label:billingBedCat.billingBedCategory
										}
								      var found= $scope.selectedBedBillingCategoryList.find(item=>item.id==billingBedCat.billingBedCategoryId);	   
									  if(found==undefined)
									  $scope.selectedBedBillingCategoryList.push(billingBedCategoryData);									
								});		
								var patientCategoryData={
										id:patientCat.patientTypeId,
										label:patientCat.patientType
								}
								
								$scope.selectedPatientTypeList.push(patientCategoryData);
							});
							var paymentEntData={
									id:paymentEnt.paymentEntitlementId,
									label:paymentEnt.paymentEntitlement
							}
							
							$scope.selectedPaymentEntitlementList.push(paymentEntData);
							$rootScope.stopLoaderPopup();
						});
					   var uniqueVisitType=new Set(visitTypeArray);
					   $scope.selectedVisitTypeList = Array.from(uniqueVisitType);
						$rootScope.stopLoader();
						
						  $scope.calculateSummaryAmounts();
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
				var URI = BASE_URL + ROOT_URL + "/api/packages/inActivePackage/parentPkgId/"+parentPkgId+"/PackageTypeId/2/activeInactive/"+status;
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
    	$state.go("addNewEitherOrPackage",{
        					 newEitherOrPackageObj :obj
        					});
    	}
	
	
	
	
}]);