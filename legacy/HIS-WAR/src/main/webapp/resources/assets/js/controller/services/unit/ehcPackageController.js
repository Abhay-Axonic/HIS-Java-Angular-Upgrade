angular.module("myApp").controller("ehcPackageController", [ '$scope','$rootScope','BillingGenericService','$cookies','$state','growl','PagerService',
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
				

			var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
			    if (cookieObjectAppConfig == undefined) {
			        $state.go('login');
			        return;
			    } else {
			        console.log('cookieObjectAppConfig', cookieObjectAppConfig);
			    }
			  $scope.consultationSpecialityId = cookieObjectAppConfig.consultationSpecialityId;
			  
			  $scope.unitId = cookieObject.unitId;
			  $scope.orgId = cookieObject.organizationId;
			  $scope.userId = cookieObject.userId;
			  $scope.searchPackageObj = {orgId:$scope.orgId,unitId:$scope.unitId,packageCode:"",packageName:"",packageTypeId:"1"};
			  $scope.getPackageTypeList($scope.orgId,$scope.unitId);
			  $scope.searchPackageList = [];
//			  $scope.getSearchServicesList($scope.searchPackageObj);
				/** Variables for pagination */
				$scope.offset = 0;
				$scope.noOfRecordsPerPage = 10;
				$scope.searchKeyword=null;
		  }
		  $scope.init();
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
								  packageTypeId:1,
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

			$scope.searchEHCPackage = function(searchKeyword){

                $scope.loadingMsg="Loading...";
                if(searchKeyword.length>2){

                    $scope.searchPackageList=[];
                    $rootScope.startLoader();
                    var param = {
                          orgId:$scope.orgId,
                          unitId:$scope.unitId,
                          packageCode:'',
                          packageName:'',
                          packageTypeId:1,
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
						  packageTypeId:1,
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
			
		  $scope.getSearchServicesList = function(searchPackageObj) {
			  $rootScope.startLoader();
			  var param = {orgId:$scope.orgId,unitId:$scope.unitId,packageCode:searchPackageObj.packageCode,packageName:searchPackageObj.packageName,packageTypeId:1};
			    var URI = BASE_URL + ROOT_URL + "/api/packages/filter";
			     BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					if (response.data.status == "success"){
						 $scope.searchPackageList =response.data.listObject;
					}
					$rootScope.stopLoader();
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
		  $scope.calculatePackageAmounts = function()
			{
				angular.forEach($scope.packageObjResponse.paymentEntitlement,function(paymentEnt,indexPaymentEnt)
					{
					angular.forEach(paymentEnt.patientCategoryList, function(patientCat,indexPatientCat){
						angular.forEach(patientCat.billingBedCategoryList,function(billingBedCat,indexBillingBedCat)
								{								
							      var total = 0;
//							      billingBedCat.mPackageMasterDto.packagePrice = $rootScope.parseFloatNum(0);
								  angular.forEach(billingBedCat.mPackageMasterDto.listTPackageServicesDetailsDto,function(serviceDtls,indexServiceDtls)
										  {
									         serviceDtls.numberToBeUse = (parseInt(serviceDtls.numberToBeUse)<0||parseInt(serviceDtls.numberToBeUse)==0)?1:serviceDtls.numberToBeUse;
									         serviceDtls.servicePrice =serviceDtls.servicePrice==null || serviceDtls.servicePrice==undefined ? 0 : serviceDtls.servicePrice;
									         serviceDtls.total = serviceDtls.numberToBeUse * serviceDtls.servicePrice;
									         serviceDtls.servicePrice = $rootScope.parseFloatNum(serviceDtls.servicePrice);
									         total = parseFloat(total) + (isNaN(serviceDtls.total) ? 0 : parseFloat(serviceDtls.total));
								          });
								  
//								    billingBedCat.mPackageMasterDto.serviceWiseSummary = $rootScope.parseFloatNum(total);
//									billingBedCat.mPackageMasterDto.grandTotal = $rootScope.parseFloatNum(total);
//									billingBedCat.mPackageMasterDto.isMarkupDown = $scope.isMarkupDown;
//									billingBedCat.mPackageMasterDto.packagePrice = $rootScope.parseFloatNum(total);
									billingBedCat.mPackageMasterDto.packageCost = $rootScope.parseFloatNum(total);
//									billingBedCat.mPackageMasterDto.markupDownInPercentage  = $scope.markupDownInPercentage;
//									billingBedCat.mPackageMasterDto.roundOffPositNeg  = $scope.roundOffPositNeg;
									
									
						      });	
					  });
				});
			}
		  $scope.getPackagesDetailsForView=function(obj)
		  {
			  if(obj.status == 'I'){
					growl.warning("Please active the package to view!",{title : 'Warning!'});
				}
			  else
			  {
			    $rootScope.startLoader();
				var paraentPckId=obj.parentPackageId
				var data={};
				data[paraentPckId]=1;
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
									var billingBedCategoryData={
											id:billingBedCat.billingBedCategoryId,
											label:billingBedCat.billingBedCategory
									}
									  var total = 0;
									   angular.forEach(billingBedCat.mPackageMasterDto.listTPackageServicesDetailsDto,function(serviceDtls,indexServiceDtls)
											  {
										         serviceDtls.total = serviceDtls.numberToBeUse * serviceDtls.servicePrice;
										         total = parseFloat(total) + (isNaN(serviceDtls.total) ? 0 : parseFloat(serviceDtls.total));
									          });
									  
									  
									  $scope.roundOffPositNeg =  billingBedCat.mPackageMasterDto.roundOffPositNeg;
									  $scope.markupDownInPercentage  =  billingBedCat.mPackageMasterDto.markupDownInPercentage;
									  $scope.isMarkupDown  =  billingBedCat.mPackageMasterDto.isMarkupDown;
									  $scope.roundOffPositNeg =  billingBedCat.mPackageMasterDto.roundOffPositNeg;
									  billingBedCat.mPackageMasterDto.serviceWiseSummary = $rootScope.parseFloatNum(total);
									  
									   billingBedCat.mPackageMasterDto.serviceWiseSummary = $rootScope.parseFloatNum(total);
									   billingBedCat.mPackageMasterDto.grandTotal = $rootScope.parseFloatNum(total)
									   
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
					   $scope.calculatePackageAmounts();
						$rootScope.stopLoader();
					}	
					else if (response.data.status == "error"){
						$rootScope.stopLoader();
					}
			  });
			  }
		  }
		  
		//get consultation dept by service id   conServiceId
			$scope.getDepartmentBySpeacialityId=function(serviceId,index)
			{
				$scope.specialityId="";
				$scope.consultationServices=[];
					$rootScope.startLoaderPopup();
					var URI = BASE_URL + ROOT_URL + "/billing/service/getConsultationDeptByServiceId/"+0+"/"+$scope.unitId;
					return BillingGenericService.serviceAction(METHOD_GET,URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
						if(response.data.status == "success")
						{
							$scope.consultationServices = response.data.listObject;
							angular.forEach($scope.packageObjResponse.paymentEntitlement,function(paymentEnt,indexPaymentEnt)
								{
									angular.forEach(paymentEnt.patientCategoryList, function(patientCat,indexPatientCat){
										angular.forEach(patientCat.billingBedCategoryList,function(billingBedCat,indexBillingBedCat)
												{								
											  angular.forEach(billingBedCat.mPackageMasterDto.listTPackageServicesDetailsDto,function(serviceDtls,indexServiceDtls)
													  {
												           if(serviceDtls.serviceId==serviceId && indexServiceDtls==index)
												        	   {
												        	   		$scope.specialityId  = serviceDtls.consultationSpecialityId;
												        	   }
											          });
										      });	
									  });
								  });
							$("#departmentModal").modal('show');
							$rootScope.stopLoaderPopup();
						}else{
							$rootScope.stopLoaderPopup();
						}
					});
			}
			
			$scope.getPackageDetails=function(obj){
				console.log(obj);
				 $state.go("addNewEHCpackage",{
					 newEHCpackageObj :obj
					});
			}
		  
		  $scope.init();
	}catch(e){
		console.log(e.message);
	}
	
	
	$scope.updateStatus=function(parentPkgId,status){
		var data = "";
		status=status=='A' ? true : false;
		$rootScope.startLoader();
		var URI = BASE_URL + ROOT_URL + "/api/packages/inActivePackage/parentPkgId/"+parentPkgId+"/PackageTypeId/1/activeInactive/"+status;
		BillingGenericService.serviceAction("GET", URI, data).then(function(response){
			if(response.data.status == "success")
			{
				growl.success("Status updated scuccessfully..!!",{title : 'Success!'});
				$rootScope.stopLoader();
			}else{
				$rootScope.stopLoader();
			}
			$scope.initPackageMasterList($scope.orgId, $scope.unitId,$scope.offset, $scope.noOfRecordsPerPage);
		});
	}

	$scope.editPackage = function(obj){
		if(obj.status == 'I'){
			growl.warning("Please active the package to edit!",{title : 'Warning!'});
		}else{
			$state.go("addNewEHCpackage",{
				 newEHCpackageObj :obj
				});
		}

	}
	
	
}]);