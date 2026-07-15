angular.module("myApp").controller("packageWithCAPController", [ '$scope','$rootScope','BillingGenericService','$cookies','$state','growl','PagerService',
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
				if(cookieObjectAppConfig == undefined){
					$state.go('login');
					return;
				}
				$scope.unitId = cookieObject.unitId;
				$scope.orgId = cookieObject.organizationId;
				$scope.userId = cookieObject.userId;
				$scope.getPackageTypeList($scope.orgId,$scope.unitId);
				$scope.searchPackageObj = {orgId:$scope.orgId,unitId:$scope.unitId,packageCode:"",packageName:"",packageTypeId:3};
			  $scope.searchPackageList = [];
			  $scope.opdBillingCatId = cookieObjectAppConfig.opdBillingCatId;
//			  $scope.getSearchServicesList($scope.searchPackageObj);
			  /** Variables for pagination */
				$scope.offset = 0;
				$scope.noOfRecordsPerPage = 10;
				$scope.selectedBedBillingCategoryList=[];
		  }
		  $scope.init();
		  $scope.getSearchServicesList = function(searchPackageObj) {
			  $rootScope.startLoader();
			  var param = {orgId:$scope.orgId,unitId:$scope.unitId,packageCode:searchPackageObj.packageCode,packageName:searchPackageObj.packageName,packageTypeId:3};
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
								  packageTypeId:3,
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
			$scope.getPackageMasterList = function(orgId,unitId,offset, noOfRecordsPerPage) {
				$rootScope.startLoader();
				var param = {
						  orgId:$scope.orgId,
						  unitId:$scope.unitId,
						  packageCode:'',
						  packageName:'',
						  packageTypeId:3,
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
		  
		//----------- Calculate Summary Amounts -----------------
			$scope.calculateSummaryAmt = function()
			{		
				
				 $scope.isMarkupDown = this.isMarkupDown;
				 $scope.roundOffPositNeg = this.roundOffPositNeg;
				 $scope.markupDownInPercentage=this.markupDownInPercentage;
				angular.forEach($scope.packageObjResponse.paymentEntitlement,function(paymentEnt,indexPaymentEnt)
						{
						angular.forEach(paymentEnt.patientCategoryList, function(patientCat,indexPatientCat){
							angular.forEach(patientCat.billingBedCategoryList,function(billingBedCat,indexBillingBedCat)
									{								
								     var temp = 0;
//								     billingBedCat.mPackageMasterDto.packagePrice = $rootScope.parseFloatNum(0);
									  angular.forEach(billingBedCat.mPackageMasterDto.listTPackageServicesDetailsDto,function(serviceDtls,indexServiceDtls)
											  {
										         serviceDtls.total = (serviceDtls.numberToBeUse==null || serviceDtls.numberToBeUse==undefined ) ? 1 :  serviceDtls.numberToBeUse * serviceDtls.servicePrice;
										         temp = parseFloat(temp) + (isNaN(serviceDtls.total) ? 0 : parseFloat(serviceDtls.total));
									          });
									  billingBedCat.mPackageMasterDto.serviceWiseSummary = $rootScope.parseFloatNum(temp);
									    
									    
									  //calculate group wise cap amount
										temp = parseFloat(0);
										angular.forEach(billingBedCat.mPackageMasterDto.listTPackageGroupWiseCapDetailsDto, function(value,index){
											var subDepartmentCapAmount = isNaN(value.subDepartmentCapAmount) || value.subDepartmentCapAmount == "" || value.subDepartmentCapAmount ==null || typeof(value.subDepartmentCapAmount) == "undefined" ? parseFloat(0) : value.subDepartmentCapAmount;
											var departmentCapAmount = isNaN(value.departmentCapAmount) || value.departmentCapAmount == "" || value.departmentCapAmount == null || typeof(value.departmentCapAmount) == "undefined" ? parseFloat(0) : value.departmentCapAmount;
											temp = parseFloat(temp) + parseFloat(subDepartmentCapAmount) + parseFloat(departmentCapAmount);
										});
										billingBedCat.mPackageMasterDto.groupWiseSummary =  $rootScope.parseFloatNum(temp);
										
										//calculate consumable wise cap amount
										temp = parseFloat(0); 
										angular.forEach(billingBedCat.mPackageMasterDto.listTPackageConsumableWiseCapDetailsDto, function(value,index){
											var productCateroyCapAmount = isNaN(value.productCateroyCapAmount) || value.productCateroyCapAmount == "" || typeof(value.productCateroyCapAmount) == "undefined" ? parseFloat(0) : value.productCateroyCapAmount;
											temp = parseFloat(temp) + parseFloat(productCateroyCapAmount);
										});
										billingBedCat.mPackageMasterDto.bedCatSummary =0;
										billingBedCat.mPackageMasterDto.consumWiseSummary = $rootScope.parseFloatNum(temp);
										
										//calculate item wise cap amount
										temp = parseFloat(0); 
										angular.forEach(billingBedCat.mPackageMasterDto.listTPackageItemDetailsDto, function(value,index){
											if(value.isServiceItem==2){
												var consumablesDtls = isNaN(value.servicePrice) || value.servicePrice == "" || typeof(value.servicePrice) == "undefined" ? parseFloat(1) : value.servicePrice;
												temp = parseFloat(temp) + parseFloat(consumablesDtls);
											}
											
										});
										billingBedCat.mPackageMasterDto.itemWiseSummary = $rootScope.parseFloatNum(temp);
										
										billingBedCat.mPackageMasterDto.grandTotal = $rootScope.parseFloatNum(parseFloat(billingBedCat.mPackageMasterDto.serviceWiseSummary) + parseFloat(billingBedCat.mPackageMasterDto.groupWiseSummary) + parseFloat(billingBedCat.mPackageMasterDto.consumWiseSummary)+parseFloat(billingBedCat.mPackageMasterDto.itemWiseSummary));
										
										//package cost
										/*switch($scope.isMarkupDown!=undefined ? $scope.isMarkupDown.toString() : "0"){
										case "1" :
											var amount = (billingBedCat.mPackageMasterDto.grandTotal/100) * (isNaN($scope.markupDownInPercentage) ? 0 : parseFloat($scope.markupDownInPercentage));
											billingBedCat.mPackageMasterDto.packageCost = $rootScope.parseFloatNum(parseFloat(billingBedCat.mPackageMasterDto.grandTotal) + parseFloat(amount));
											break;
										case "2" :
											var amount = (billingBedCat.mPackageMasterDto.grandTotal/100) * (isNaN($scope.markupDownInPercentage) ? 0 : parseFloat($scope.markupDownInPercentage));
											billingBedCat.mPackageMasterDto.packageCost = $rootScope.parseFloatNum(billingBedCat.mPackageMasterDto.grandTotal - amount);
											break;	
										}
										
										//package price
										switch($scope.roundOffPositNeg != undefined ? $scope.roundOffPositNeg.toString() : "0"){
										case "1":
											billingBedCat.mPackageMasterDto.packagePrice = $rootScope.parseFloatNum(parseFloat(billingBedCat.mPackageMasterDto.packageCost) + (isNaN(billingBedCat.mPackageMasterDto.manualRoundoffAmount) ? 0 : parseFloat(billingBedCat.mPackageMasterDto.manualRoundoffAmount)));
											break;
										case "2":
											billingBedCat.mPackageMasterDto.packagePrice = $rootScope.parseFloatNum(parseFloat(billingBedCat.mPackageMasterDto.packageCost) - (isNaN(billingBedCat.mPackageMasterDto.manualRoundoffAmount) ? 0 : parseFloat(billingBedCat.mPackageMasterDto.manualRoundoffAmount)));
											break;	
										}*/
							      });	
						});
					});
			}
			// for view packages
		  $scope.getPackagesDetailsForView=function(obj)
		  {
			  $rootScope.startLoader();
				var paraentPckId=obj.parentPackageId
				var data={};
				data[paraentPckId]=3;
				var visitTypeArray=[];
				console.log("request",data);
				var URI = BASE_URL + ROOT_URL + "/api/packages/getPackagesByPackageId";
				BillingGenericService.serviceAction(METHOD_POST,URI, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					if (response.data.status == "success")
					{
						tempPackgeObj = {};
						$scope.packageObjResponse={};
						
						
						$scope.selectedPaymentEntitlementList=[];
						$scope.selectedPatientTypeList=[];
						$scope.selectedBedBillingCategoryList=[];
						$scope.selectedVisitTypeList=[];
						$('#viewPackageModal').modal('show');
					   $scope.packageObjResponse= response.data.object;
					   console.log("packageObjResponse",$scope.packageObjResponse);
//					   $scope.packageObj.sexId = $scope.packageObjResponse.parentPackageMasterDto.sexId;
					   tempPackgeObj= angular.copy($scope.packageObjResponse);
					   angular.forEach($scope.packageObjResponse.paymentEntitlement,function(paymentEnt,indexPaymentEnt){
							
							angular.forEach(paymentEnt.patientCategoryList, function(patientCat,indexPatientCat){

								angular.forEach(patientCat.billingBedCategoryList,function(billingBedCat,indexBillingBedCat){		
//									$scope.packageObj.tariffId = billingBedCat.mPackageMasterDto.tariffId;
									
									  visitTypeArray.push(billingBedCat.mPackageMasterDto.visitTypeId);
									  
										var arr = $scope.selectedVisitTypeList.filter(function(vi){
					   	            	   return (vi.id == billingBedCat.mPackageMasterDto.visitTypeId);
					   				 	});
										if(arr.length <= 0){
											var data={
														id:billingBedCat.mPackageMasterDto.visitTypeId,
														label:$rootScope.getVisitTypeById(billingBedCat.mPackageMasterDto.visitTypeId)
											     }
											$scope.selectedVisitTypeList.push(data);
					   					 }
									
									  $scope.roundOffPositNeg =  billingBedCat.mPackageMasterDto.roundOffPositNeg;
									  $scope.markupDownInPercentage  =  billingBedCat.mPackageMasterDto.markupDownInPercentage;
									  $scope.isMarkupDown  =  billingBedCat.mPackageMasterDto.isMarkupDown;
									  $scope.roundOffPositNeg =  billingBedCat.mPackageMasterDto.roundOffPositNeg;
									  
									var billingBedCategoryData={
											id:billingBedCat.billingBedCategoryId,
											label:billingBedCat.billingBedCategory
									}
									$scope.selectedBedBillingCategoryList.push(billingBedCategoryData);
									
								});//billingBedCategory			
								
								var patientCategoryData={
										id:patientCat.patientTypeId,
										label:patientCat.patientType
								}
								$scope.selectedPatientTypeList.push(patientCategoryData);
								
							});//patientCategory

							var paymentEntData={
									id:paymentEnt.paymentEntitlementId,
									label:paymentEnt.paymentEntitlement
							}
							$scope.selectedPaymentEntitlementList.push(paymentEntData);
							
							
						});// payment entitlement end					   
				
					   console.log("$scope.selectedVisitTypeList",$scope.selectedVisitTypeList);
					   $scope.calculateSummaryAmt();
						$rootScope.stopLoader();
					}	
					else if (response.data.status == "error"){
						$rootScope.stopLoader();
					}
					$scope.packDetailsObj={};
				});
		  }
		  
		  $scope.updateStatus=function(parentPkgId,status){
				var data = "";
				status=status=='A' ? true : false;
				$rootScope.startLoader();
				var URI = BASE_URL + ROOT_URL + "/api/packages/inActivePackage/parentPkgId/"+parentPkgId+"/PackageTypeId/3/activeInactive/"+status;
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
    	$state.go("addNewPackageWithCAPDetails",{
        					 newPackageWithCAPObj :obj
        					});
    	}
	
	
	
	
}]);