angular.module("myApp").controller("addEitherOrPackageController", [ '$scope','$rootScope','BillingGenericService','$cookies','$state','growl','servicesFactory','GenericService','$stateParams',
	function($scope,$rootScope,BillingGenericService,$cookies,$state,growl,servicesFactory,GenericService,$stateParams){
	try{
		$rootScope.loginpage = true;
		$scope.angular = angular;
		 function cartesian() {
			    var r = [], arg = arguments, max = arg.length-1;
			    function helper(arr, i) {
			        for (var j=0, l=arg[i].length; j<l; j++) {
			            var a = arr.slice(0); // clone arr
			            a.push(arg[i][j]);
			            if (i==max)
			                r.push(a);
			            else
			                helper(a, i+1);
			        }
			    }
			    helper([], 0);
			    return r;
			}
		
		
		$scope.initPackageObj = function(){
			$scope.packageObj = {packageTypeId:"2",sexId:"",minAge:"",maxAge:"",tariffId:"",serviceId:""}
		}
		
		$scope.initPackageGroupDtls = function(){
			$scope.packageGroup = {packageGroupName:"",maxPrice:$rootScope.parseFloatNum(0),minPrice:$rootScope.parseFloatNum(0),avgPrice:$rootScope.parseFloatNum(0),groupPrice:$rootScope.parseFloatNum(0),listTPackageServicesDetailsDto:[]};
			$scope.groupServices = [];
		}
		
		$scope.initViewPackageObj = function(){
			$scope.viewPackageGroupServices = [];
			$scope.viewPackageObj = {maxPrice:$rootScope.parseFloatNum(0),minPrice:$rootScope.parseFloatNum(0),avgPrice:$rootScope.parseFloatNum(0),groupPrice:$rootScope.parseFloatNum(0)};
		}
		
		$scope.initSummary = function(){
			$scope.summary = {packageGroupAmt:$rootScope.parseFloatNum(0),serviceGroupAmt:$rootScope.parseFloatNum(0),groupTotal:$rootScope.parseFloatNum(0)};
		}
		
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
		
		$scope.opdBillingCatId = cookieObjectAppConfig.opdBillingCatId;
		
		
		$scope.initImportPackageObj = function(){
			$scope.importPkgObj = {
							packageTypeId:"2",
							packageCode:"",
							packageName:""	,
							orgId:$scope.orgId,
							unitId:$scope.unitId
						}
		}
		$scope.initImportPackageObj();
		//get selected package
		$scope.getPackageDetails=function(obj){
			$scope.packDetailsObj=obj;
		}
		
		$scope.init = function(){
			$scope.searchServiceList = [];
			$scope.initPackageObj();
			servicesFactory.initServiceSearch($scope);
			$scope.selectedServiceList = [];
			$scope.specialityList = [];
			$scope.subSpecialityList = [];
			$scope.searchServObj = {};
			$scope.groupServices = [];
			$scope.initPackageGroupDtls();
			$scope.listTPackageEitherorGroupDetailsDto = [];
			$scope.initViewPackageObj();
			$scope.listTPackageServicesDetailsDto = [];
			$scope.groupServAmounts = [];
			$scope.initSummary();
			$scope.packageService = "";
			$scope.selectedPatientTypeList=[];
			$scope.selectedVisitTypeList=[];
			$scope.selectedPaymentEntitlementList=[];
			$scope.selectedBedBillingCategoryList=[];
			$scope.billingCategoryReference = [];
			$scope.packageObjResponse ={};
			$scope.parentPackageId=null;
			
		}
		$scope.init();
		servicesFactory.setOrgUnit($scope.orgId,$scope.unitId,$scope);
		
		//service factory methods 
		$scope.searchServices = function(searchObj){servicesFactory.searchServices(searchObj,$scope)}
		$scope.getSubSpecialityBySpecialityId = function(specialityId){servicesFactory.getSubSpecialityBySpecialityId(specialityId,$scope)}
		$scope.selectAllServices = function(flag){servicesFactory.selectAllServices(flag,$scope)}
		$scope.setOrgUnit = function(orgId,unitId,scope){servicesFactory.setOrgUnit(orgId,unitId,$scope)}
		
		$scope.getPackageTypeList = function(orgId,unitId) {
			var data = "";
			var URI = BASE_URL + ROOT_URL + "/Unit/Service/EHCpackage/getPackgeType/"+orgId+"/"+unitId;
			BillingGenericService.serviceAction("GET", URI, data).then(function(response){
				$scope.packageTypeList = response.data.listObject;
			});
	  }
	
	$scope.getTariffList = function(orgId,unitId) {
			var data = "";
			var URI = BASE_URL + ROOT_URL + "/Unit/Service/EHCpackage/getTariff/"+orgId+"/"+unitId;
			BillingGenericService.serviceAction("GET", URI, data).then(function(response){
				$scope.tariffList= response.data.listObject;
			});
	}
	
	$scope.getPaymentEntitlementList = function() {
		var URI = BASE_URL + ROOT_URL + "/global/paymententitlement/"+$scope.orgId;
		BillingGenericService.serviceAction("GET", URI, {}).then(function(response){
			$scope.paymentEntitlementList= response.data.listObject;
		});
	}
	
	$scope.getPatientTypeList = function() {
		var URI = BASE_URL + ROOT_URL + "/global/patientcategory/"+$scope.orgId;
		BillingGenericService.serviceAction("GET", URI, null).then(function(response){
			$scope.patientTypeList= response.data.listObject;
		});
	}
	
	// for get active billing category list
	$scope.activeBillingBedCategory=function(){				
		var URI = BASE_URL + ROOT_URL+ GETACTIVEBILLINGBEDCATEGORYLIST+"/orgId/"+$scope.orgId+"/unitId/"+$scope.unitId;
		GenericService.serviceAction("GET", URI, "").then(
						function(response) {
							$scope.billingCategoryReference = [];
							if (response.data.status == "success") {
								$scope.billingCategoryReference=response.data.listObject;									
							}
						});
	}
	  $scope.activeBillingBedCategory();
	
	$scope.activeVisitType=function(){							
		$scope.visitTypeList = [
			{
				id:3,
				label:"Day Care"
			},
			{
				id:4,
				label:"ER"
			},
			{
				id:2,
				label:"IP"
			},
			{
				id:1,
				label:"OP"
			}
		];		
		
	}
	$scope.activeVisitType();
	
	$scope.getBillingBedCategory=
	{
			onClose  : function () {
				console.log($scope.selectedVisitTypeList);
				var visitTypeId = [];
				angular.forEach($scope.selectedVisitTypeList,
						function(value,index) 
						{
							visitTypeId.push(value.id);
						});
				
				var data = {
						unitId: $scope.unitId,
						organizationId:$scope.orgId,
						visitTypeListIds : visitTypeId
				}
				
				$scope.selectedBillingCategory = [];
				var URI = BASE_URL+ ROOT_URL+ "/adt/getBillingBedCategoryByVistTypeIdMultiple";
			GenericService.serviceAction("POST", URI, data)
					.then(function(response) {
								if (response.data.status == "success") {
//									$scope.bedBillingCategoryListReference = [];
//									$scope.bedBillingCategoryListReference = response.data.listObject;
									$scope.bedBillingCategoryList = [];
									angular.forEach(response.data.listObject,
													function(value,index) {
														var data = {};
														data.id = value.billingBedCategoryId;
														data.label = value.billingBedDesc;
														$scope.bedBillingCategoryList.push(data);
													});
								}
							});
					},
				onSelectionChanged: function() {
                         $scope.validateFetch(false);
                      }
		}
	$scope.getBillingBedCategoryForAllVisitType=function()
	{
		      var visitTypeId = [];
				angular.forEach($scope.visitTypeList,function(value,index) 
						{
							visitTypeId.push(value.id);
						});					
				var data = {
						unitId: $scope.unitId,
						organizationId:$scope.orgId,
						visitTypeListIds : visitTypeId
				      }					
				var URI = BASE_URL+ ROOT_URL+ "/adt/getBillingBedCategoryByVistTypeIdMultiple";
			   GenericService.serviceAction("POST", URI, data).then(function(response) {
					if (response.data.status == "success") {
									$scope.bedBillingCategoryListReference = [];
									$scope.bedBillingCategoryListReference = response.data.listObject;
									
								}
							});
		}
	$scope.getBillingBedCategoryForAllVisitType();
	
	 $scope.getActiveGenderList = function()
		{
			var data = "";
			var URI = BASE_URL + ROOT_URL + GETACTIVEGENDERLIST;
			BillingGenericService.serviceAction("GET", URI, data).then(function(response){
				$scope.genderList= response.data.listObject;
			});
	    }
	  $scope.getActiveGenderList();
	
	$scope.getPackageTypeList($scope.orgId,$scope.unitId);
	$scope.getTariffList($scope.orgId,$scope.unitId);
	$scope.getPaymentEntitlementList();
	$scope.getPatientTypeList();
	$scope.getPackageServiceList = function(searchKeyword){
			var URI = BASE_URL + ROOT_URL + "/Unit/Service/EHCpackage/autoCompleteServices/"+$scope.orgId+"/"+$scope.unitId+"/"+searchKeyword;
			return BillingGenericService.serviceAction("GET", URI,null).then(function(response) {
				return response.data.listObject;
			});
	    }	
    
		$scope.selectPackageService = function($item, $model,$label)
		{
	      $scope.packageObj.serviceId = $item.id;
	    }
		
	  	$scope.firstTabLevel = 1 ;
		
		$scope.setFirstTabLevel= function(newTab)
		{
			$scope.firstTabLevel = newTab;
		};
		$scope.isSetFirstTabLevel = function(tabNum)
		{
			return $scope.firstTabLevel === tabNum;
		};
		
		// getting name from ids
	     $scope.visitTypeName = function(id){
			   var visitTypeName = $.grep($scope.visitTypeList, function (visit) {
	  	           return visit.id == id;
	  	       })[0].label;
			  return visitTypeName;
		   }
	     $scope.paymentEntitlementName = function(id)
		  {
			  var paymentEntitlementName = $.grep($scope.paymentEntitlementList, function (pay) {
	  	           return pay.id == id;
	  	       })[0].label;
			  return paymentEntitlementName;
		  }
	     $scope.patientTypeName = function(id){
	    	 var patientTypeName = $.grep($scope.patientTypeList, function (patientType) {
	  	           return patientType.id == id;
	  	       })[0].label;
			  return patientTypeName;
		   }
	     $scope.bedBillingCategoryName = function(id){				
				var bedBillingCategoryName = $.grep($scope.billingCategoryReference, function (billBed) {
		  	           return billBed.billingBedCategoryId == id;
		  	       })[0].billingBedDesc;
				  return bedBillingCategoryName;
		   }
	     $scope.genderName = function(id){				
				var genderName = $.grep($scope.genderList, function (gen) {
		  	           return gen.sexId == id;
		  	       })[0].label;
				  return genderName;
		   }	
		
	     $scope.getVisitTypeByBillingBedCategoryId = function(id){				
				var visitTypeId = $.grep($scope.bedBillingCategoryListReference, function (billBed) {
		  	           return billBed.billingBedCategoryId == id;
		  	       })[0].visitTypeId;
				  return visitTypeId;
		   }
	     
	// getting final service list
	var tempPackgeObj = {};
	 	
	//group services
	$scope.groupServices = [];
	$scope.addGroupServices = function(){
		
//		$('#addDetailsModal').modal('show');
		
		switch($scope.firstTabLevel){
		case 1:
			
		    $scope.selectedServiceList = [];
			$scope.selectedServiceIds = [];
			$rootScope.startLoader();
			$scope.addNewServiceList = [];
			angular.forEach($scope.searchServiceList,function(value,key){
				if(value.isSelected){
					value.qty = 0;
					value.total = 0;
					$scope.selectedServiceIds.push(value.serviceMasterId);
					$scope.selectedServiceList.push(value);
				
				}
			});
			if($scope.selectedServiceList==undefined || $scope.selectedServiceList.length==0){
				growl.warning("Please Select services!",{title : 'Warning!'});
				return false;
			}
			arraList=cartesian($scope.selectedPaymentEntitlementList,$scope.selectedPatientTypeList,$scope.selectedBedBillingCategoryList);
			for(i=0;i<arraList.length;i++){
				var tempObj={						
						paymentEntitlementId:"",paymentEntitlement:"",
						visitTypeId:"",visitTypeName:"",
						patientTypeId:"",patientType:"",
						billingBedCategoryId:"",billingBedCategory:"",
						packagetypeId:2,orgId:$scope.orgId,unitId:$scope.unitId,
					};
				
				var tempArr=arraList[i];
				$scope.paymentEntitlementId =tempArr[0].id;
				$scope.patientTypeId =tempArr[1].id;
//				$scope.visitTypeId =tempArr[2].id==undefined?tempArr[2]:tempArr[2].id;
				$scope.bedBillingCategoryId =tempArr[2].id;
				$scope.visitTypeId = $scope.getVisitTypeByBillingBedCategoryId($scope.bedBillingCategoryId);
				if(($scope.visitTypeId == 1 && $scope.bedBillingCategoryId == $scope.opdBillingCatId) || ($scope.visitTypeId != 1 && $scope.opdBillingCatId !=$scope.bedBillingCategoryId))
					{
						var newTempObj = angular.copy(tempObj);					
						newTempObj.visitTypeId=$scope.visitTypeId;
						newTempObj.visitTypeName=$scope.visitTypeName($scope.visitTypeId);
						newTempObj.paymentEntitlementId=$scope.paymentEntitlementId;
						newTempObj.paymentEntitlement=$scope.paymentEntitlementName($scope.paymentEntitlementId);
						newTempObj.patientTypeId=$scope.patientTypeId;
						newTempObj.patientType=$scope.patientTypeName($scope.patientTypeId);
						newTempObj.billingBedCategoryId=$scope.bedBillingCategoryId;
						newTempObj.billingBedCategory=$scope.bedBillingCategoryName($scope.bedBillingCategoryId);
						newTempObj.tariffId=$scope.packageObj.tariffId;
					    $scope.addNewServiceList.push(newTempObj);
			       }
			}
			var data = {
					serviceIds:$scope.selectedServiceIds,
					packageTypeId:$scope.packageObj.packageTypeId,
					serviceForPackageReqList:$scope.addNewServiceList
			};
			
			var URI = BASE_URL + ROOT_URL + "/api/packages/package/service/search";
			BillingGenericService.serviceAction("POST", URI, data).then(function(response)
					{
				           if(response.data.status=="success")
				        	   {
				        	    var flag=false;
				        	    $scope.tempPackageObjResponse= response.data.object==null?{}:response.data.object;
								if(angular.equals({}, tempPackgeObj))
									{
									   tempPackgeObj= angular.copy($scope.tempPackageObjResponse);
									   flag=true;
									}
								if(!angular.equals({}, tempPackgeObj) && !angular.equals({}, $scope.tempPackageObjResponse))
									{
											angular.forEach($scope.tempPackageObjResponse.paymentEntitlement[0].patientCategoryList[0].billingBedCategoryList[0].mPackageMasterDto.listTPackageServicesDetailsDto,function(service,index)
											{			
												var found = $scope.groupServices.find(function(serviceDtls) { 
													  return serviceDtls.serviceId == service.serviceId; 
													});
													if(found==undefined || found.length==0){
														$scope.groupServices.push(service);
														$scope.groupServAmounts.push(service.servicePrice);
													}
												
			                                 });
									}
									if(flag){
										tempPackgeObj.paymentEntitlement[0].patientCategoryList[0].billingBedCategoryList[0].mPackageMasterDto.listTPackageServicesDetailsDto=[];
									}
									
									$rootScope.stopLoader();
								    $scope.calculateGroupAmounts();
								    $scope.crateClone();
								    //enabling save button start
//								  	$("#savePackageId").removeClass("disabled");
//									$("#savePackageId").addClass("enabled");
									//enabling save button end
				               }
				           else  if(response.data.status=="error")
				        	   {
				        	     $rootScope.stopLoader();
				        	   }
					});
		break;
		case 2:
				    $scope.selectedServiceList = [];
					$scope.selectedServiceIds = [];
					$rootScope.startLoader();
					$scope.addNewServiceList = [];
					angular.forEach($scope.searchServiceList,function(value,key){
						if(value.isSelected){
							value.qty = 0;
							value.total = 0;
							$scope.selectedServiceIds.push(value.serviceMasterId);
							$scope.selectedServiceList.push(value);
						}
					});
					if($scope.selectedServiceList==undefined || $scope.selectedServiceList.length==0){
						growl.warning("Please Select services!",{title : 'Warning!'});
						return false;
					}
					arraList=cartesian($scope.selectedPaymentEntitlementList,$scope.selectedPatientTypeList,$scope.selectedBedBillingCategoryList);
					for(i=0;i<arraList.length;i++){
						var tempObj={						
								paymentEntitlementId:"",paymentEntitlement:"",
								visitTypeId:"",visitTypeName:"",
								patientTypeId:"",patientType:"",
								billingBedCategoryId:"",billingBedCategory:"",
								packagetypeId:2,orgId:$scope.orgId,unitId:$scope.unitId,
							};
						
						var tempArr=arraList[i];
						$scope.paymentEntitlementId =tempArr[0].id;
						$scope.patientTypeId =tempArr[1].id;
//						$scope.visitTypeId =tempArr[2].id==undefined?tempArr[2]:tempArr[2].id;
						$scope.bedBillingCategoryId =tempArr[2].id;
						$scope.visitTypeId = $scope.getVisitTypeByBillingBedCategoryId($scope.bedBillingCategoryId);
						
						if(($scope.visitTypeId == 1 && $scope.bedBillingCategoryId == $scope.opdBillingCatId) || ($scope.visitTypeId != 1 && $scope.opdBillingCatId !=$scope.bedBillingCategoryId))
							{
								var newTempObj = angular.copy(tempObj);					
								newTempObj.visitTypeId=$scope.visitTypeId;
								newTempObj.visitTypeName=$scope.visitTypeName($scope.visitTypeId);
								newTempObj.paymentEntitlementId=$scope.paymentEntitlementId;
								newTempObj.paymentEntitlement=$scope.paymentEntitlementName($scope.paymentEntitlementId);
								newTempObj.patientTypeId=$scope.patientTypeId;
								newTempObj.patientType=$scope.patientTypeName($scope.patientTypeId);
								newTempObj.billingBedCategoryId=$scope.bedBillingCategoryId;
								newTempObj.billingBedCategory=$scope.bedBillingCategoryName($scope.bedBillingCategoryId);
								newTempObj.tariffId=$scope.packageObj.tariffId;
							    $scope.addNewServiceList.push(newTempObj);
					       }
					}
					var data = {
							serviceIds:$scope.selectedServiceIds,
							packageTypeId:$scope.packageObj.packageTypeId,
							serviceForPackageReqList:$scope.addNewServiceList
					};
					
					var URI = BASE_URL + ROOT_URL + "/api/packages/package/service/search";
					BillingGenericService.serviceAction("POST", URI, data).then(function(response)
							{
						           if(response.data.status=="success")
						        	   {
						        	    $scope.tempPackageObjResponse= response.data.object;
										if(angular.equals({}, tempPackgeObj))
											{
											   tempPackgeObj= angular.copy($scope.tempPackageObjResponse);
											}
										if(!angular.equals({}, tempPackgeObj) && !angular.equals({}, $scope.tempPackageObjResponse))
											{
										    angular.forEach($scope.tempPackageObjResponse.paymentEntitlement,function(paymentEnt,indexPaymentEnt){
												angular.forEach(paymentEnt.patientCategoryList, function(patientCat,indexPatientCat){
													angular.forEach(patientCat.billingBedCategoryList,function(billingBedCat,indexBillingBedCat)
													{								

														//1.for normal services
														   var tempserviceList = tempPackgeObj.paymentEntitlement[indexPaymentEnt].patientCategoryList[indexPatientCat].billingBedCategoryList[indexBillingBedCat].mPackageMasterDto.listTPackageServicesDetailsDto
															 if(tempserviceList!=undefined && tempserviceList!=null){

															  angular.forEach(billingBedCat.mPackageMasterDto.listTPackageServicesDetailsDto,function(serviceDtls,indexServiceDtls)
																	  {
																		  var found = tempserviceList.find(function(service) { 
																			  return service.serviceId == serviceDtls.serviceId; 
																			});
																		    serviceDtls.numberToBeUse = 1;
																			serviceDtls.total = serviceDtls.numberToBeUse * serviceDtls.servicePrice;
																        if(found==undefined || found.length==0)
																        	{
																	        	serviceDtls.numberToBeUse = 1;
																				serviceDtls.total = serviceDtls.numberToBeUse * serviceDtls.servicePrice;
																        		tempPackgeObj.paymentEntitlement[indexPaymentEnt].patientCategoryList[indexPatientCat].billingBedCategoryList[indexBillingBedCat].mPackageMasterDto.listTPackageServicesDetailsDto.push(serviceDtls);
																        	}
															          });
														}else{
															var serviceList=[];
															angular.forEach(billingBedCat.mPackageMasterDto.listTPackageServicesDetailsDto,function(serviceDtls,indexServiceDtls)
															{
																serviceDtls.numberToBeUse = 1;
																serviceDtls.total = serviceDtls.numberToBeUse * serviceDtls.servicePrice;			
																serviceList.push(serviceDtls);
																        	
															  });
															 tempPackgeObj.paymentEntitlement[indexPaymentEnt].patientCategoryList[indexPatientCat].billingBedCategoryList[indexBillingBedCat].mPackageMasterDto.listTPackageServicesDetailsDto=serviceList;
														 }
														   
														   
														   
														   //Group
														   if(billingBedCat.mPackageMasterDto.listTPackageEitherorGroupDetailsDto==null){
															   billingBedCat.mPackageMasterDto.listTPackageEitherorGroupDetailsDto = [];
														   }
														   
														   if(!angular.equals({}, tempPackgeObj) && !angular.equals({}, $scope.packageObjResponse))
															{															   
															   tempPackgeObj.paymentEntitlement[indexPaymentEnt].patientCategoryList[indexPatientCat].billingBedCategoryList[indexBillingBedCat].mPackageMasterDto.listTPackageEitherorGroupDetailsDto =  $scope.packageObjResponse.paymentEntitlement[indexPaymentEnt].patientCategoryList[indexPatientCat].billingBedCategoryList[indexBillingBedCat].mPackageMasterDto.listTPackageEitherorGroupDetailsDto;
															}
												  });
										       });
					                        });
										    
										      $scope.packageObjResponse =  tempPackgeObj;
											  $rootScope.stopLoader();
											  $scope.calculateSummaryAmounts();
											  $scope.crateClone();
											  //enabling save button start
//											  	$("#savePackageId").removeClass("disabled");
//												$("#savePackageId").addClass("enabled");
												//enabling save button end
									     }
						            }
						           else  if(response.data.status=="error")
						        	   {
						        	     $rootScope.stopLoader();
						        	   }
							});
					break;
			  }
	}
	
	$scope.removeGroupServices = function(index){
		$scope.groupServices.splice(index,1);
		$scope.groupServAmounts.splice(index,1);
		$scope.calculateGroupAmounts();
	}
	
	$scope.calculateGroupAmounts = function(){
		var amount = parseFloat(0);
		angular.forEach($scope.groupServices,function(value,key){
			amount = amount + parseFloat(value.servicePrice);
		});
		amount = $rootScope.parseFloatNum(amount);
		var avgPrice = amount/$scope.groupServices.length;
		$scope.packageGroup.maxPrice = $rootScope.parseFloatNum($scope.groupServAmounts.max());
		$scope.packageGroup.minPrice = $rootScope.parseFloatNum($scope.groupServAmounts.min());
		$scope.packageGroup.avgPrice = $rootScope.parseFloatNum(avgPrice);
		$scope.packageGroup.groupPrice = $rootScope.parseFloatNum(0);
		
	}
	
	
	$scope.showPackageGroup = function()
	{
		if($scope.packageObj.serviceId=="" || $scope.packageObj.serviceId==undefined){
			growl.warning("Please select package service!",{title : 'Warning!'});
			return false;
		}
		if($scope.packageObj.tariffId=="" || $scope.packageObj.tariffId==undefined){
			growl.warning("Please select tariff!",{title : 'Warning!'});
			return false;
		}
		if($scope.selectedPaymentEntitlementList==undefined || $scope.selectedPaymentEntitlementList.length==0){
			growl.warning("Please Select payment entitlement!",{title : 'Warning!'});
			return false;
		}
		if($scope.packageObj.sexId==undefined){
			growl.warning("Please Select gender!",{title : 'Warning!'});
			return false;
		}
		if($scope.selectedPatientTypeList==undefined || $scope.selectedPatientTypeList.length==0){
			growl.warning("Please Select patient type!",{title : 'Warning!'});
			return false;
		}
		if($scope.selectedVisitTypeList==undefined || $scope.selectedVisitTypeList.length==0){
			growl.warning("Please Select visit type!",{title : 'Warning!'});
			return false;
		}
		if($scope.selectedBedBillingCategoryList==undefined || $scope.selectedBedBillingCategoryList.length==0){
			growl.warning("Please Select patient billing bed category!",{title : 'Warning!'});
			return false;
		}
//		$("#divEnabled").removeClass("enabled");
//		$("#divEnabled").addClass("disabled");
//		$("#divEnabled2").removeClass("enabled");
//		$("#divEnabled2").addClass("disabled");
		$("#addDetailsModal").modal('show');
	}
	
	$scope.getGrpServices=function(){
		$scope.searchServiceList=[];
	}
	$scope.getServices=function(){
		if($scope.packageObj.serviceId=="" || $scope.packageObj.serviceId==undefined){
			growl.warning("Please select package service!",{title : 'Warning!'});
			return false;
		}
		if($scope.packageObj.tariffId=="" || $scope.packageObj.tariffId==undefined){
			growl.warning("Please select tariff!",{title : 'Warning!'});
			return false;
		}
		if($scope.selectedPaymentEntitlementList==undefined || $scope.selectedPaymentEntitlementList.length==0){
			growl.warning("Please Select payment entitlement!",{title : 'Warning!'});
			return false;
		}
		if($scope.packageObj.sexId==undefined){
			growl.warning("Please Select gender!",{title : 'Warning!'});
			return false;
		}
		if($scope.selectedPatientTypeList==undefined || $scope.selectedPatientTypeList.length==0){
			growl.warning("Please Select patient type!",{title : 'Warning!'});
			return false;
		}
		if($scope.selectedVisitTypeList==undefined || $scope.selectedVisitTypeList.length==0){
			growl.warning("Please Select visit type!",{title : 'Warning!'});
			return false;
		}
		if($scope.selectedBedBillingCategoryList==undefined || $scope.selectedBedBillingCategoryList.length==0){
			growl.warning("Please Select patient billing bed category!",{title : 'Warning!'});
			return false;
		}
		
//		$("#divEnabled").removeClass("enabled");
//		$("#divEnabled").addClass("disabled");
//		$("#divEnabled2").removeClass("enabled");
//		$("#divEnabled2").addClass("disabled");
		$('#getServicesModal-Eitheror').modal('show');
		$scope.searchServiceList=[];
		
	}
	
	$scope.addPackageGroup = function(packageGroup)
	{
		
		if($scope.groupServices.length == 0)
		{
			growl.error("Please add atleast one service",
			{
				title : ERROR_MSG
			});
			return;
		}
		
		$('#addDetailsModal').modal('hide');
		
			if(angular.equals({}, tempPackgeObj))
				{
				  $scope.packageObjRespons= angular.copy($scope.tempPackageObjResponse);
				}
			if(angular.equals({}, $scope.packageObjResponse) || $scope.packageObjResponse==null)
			 {
				    $scope.selectedServiceList = [];
					$scope.selectedServiceIds = [];
					$rootScope.startLoader();
					$scope.addNewServiceList = [];
					for(i=0;i<arraList.length;i++){
						var tempObj={						
								paymentEntitlementId:"",paymentEntitlement:"",
								visitTypeId:"",visitTypeName:"",
								patientTypeId:"",patientType:"",
								billingBedCategoryId:"",billingBedCategory:"",
								packagetypeId:2,orgId:$scope.orgId,unitId:$scope.unitId,
							};
						
						var tempArr=arraList[i];
						$scope.paymentEntitlementId =tempArr[0].id;
						$scope.patientTypeId =tempArr[1].id;
//						$scope.visitTypeId =tempArr[2].id==undefined?tempArr[2]:tempArr[2].id;
						$scope.bedBillingCategoryId =tempArr[2].id;
						$scope.visitTypeId = $scope.getVisitTypeByBillingBedCategoryId($scope.bedBillingCategoryId);
						
						if(($scope.visitTypeId == 1 && $scope.bedBillingCategoryId == $scope.opdBillingCatId) || ($scope.visitTypeId != 1 && $scope.opdBillingCatId !=$scope.bedBillingCategoryId))
							{
								var newTempObj = angular.copy(tempObj);					
								newTempObj.visitTypeId=$scope.visitTypeId;
								newTempObj.visitTypeName=$scope.visitTypeName($scope.visitTypeId);
								newTempObj.paymentEntitlementId=$scope.paymentEntitlementId;
								newTempObj.paymentEntitlement=$scope.paymentEntitlementName($scope.paymentEntitlementId);
								newTempObj.patientTypeId=$scope.patientTypeId;
								newTempObj.patientType=$scope.patientTypeName($scope.patientTypeId);
								newTempObj.billingBedCategoryId=$scope.bedBillingCategoryId;
								newTempObj.billingBedCategory=$scope.bedBillingCategoryName($scope.bedBillingCategoryId);
								newTempObj.tariffId=$scope.packageObj.tariffId;
							    $scope.addNewServiceList.push(newTempObj);
					       }
					}
					var data = {
							serviceIds:[],
							packageTypeId:$scope.packageObj.packageTypeId,
							serviceForPackageReqList:$scope.addNewServiceList
					};
					
					var URI = BASE_URL + ROOT_URL + "/api/packages/package/service/search";
					BillingGenericService.serviceAction("POST", URI, data).then(function(response)
							{
						           if(response.data.status=="success")
						        	   {
										$scope.packageObjResponse= response.data.object;
										  angular.forEach($scope.packageObjResponse.paymentEntitlement,function(paymentEnt,indexPaymentEnt){
												angular.forEach(paymentEnt.patientCategoryList, function(patientCat,indexPatientCat){
													angular.forEach(patientCat.billingBedCategoryList,function(billingBedCat,indexBillingBedCat)
													{		
														var packageGroupObj = angular.copy(packageGroup);
														packageGroupObj.listTPackageServicesDetailsDto = $scope.groupServices;
														if(billingBedCat.mPackageMasterDto.listTPackageEitherorGroupDetailsDto==null)
															billingBedCat.mPackageMasterDto.listTPackageEitherorGroupDetailsDto = [];
														 billingBedCat.mPackageMasterDto.listTPackageEitherorGroupDetailsDto.push(packageGroupObj);
												  });
										    });
					                     });
										   $scope.calculateSummaryAmounts();
											$rootScope.stopLoader();
											$scope.initPackageGroupDtls();
						           }
						           else  if(response.data.status=="error")
						        	   {
						        	     $rootScope.stopLoader();
						        	   }
							});
			  }
			else{
				angular.forEach($scope.packageObjResponse.paymentEntitlement,function(paymentEnt,indexPaymentEnt){
					angular.forEach(paymentEnt.patientCategoryList, function(patientCat,indexPatientCat){
						angular.forEach(patientCat.billingBedCategoryList,function(billingBedCat,indexBillingBedCat)
						{								
							var packageGroupObj = angular.copy(packageGroup);
							packageGroupObj.listTPackageServicesDetailsDto = $scope.groupServices;
							if(billingBedCat.mPackageMasterDto.listTPackageEitherorGroupDetailsDto==null)
								billingBedCat.mPackageMasterDto.listTPackageEitherorGroupDetailsDto = [];
							 billingBedCat.mPackageMasterDto.listTPackageEitherorGroupDetailsDto.push(packageGroupObj);
					   });
			       });
				});
				$scope.initPackageGroupDtls();
			}
		$scope.calculateSummaryAmounts();
	}
	
	$scope.viewPackageGroup = function(packageGroup){
		$scope.viewPackageGroupServices = packageGroup.listTPackageServicesDetailsDto;
		$scope.viewPackageObj = packageGroup;
	}
	
	$scope.removePackageGroup = function(index){
		$scope.listTPackageEitherorGroupDetailsDto.splice(index,1);
		$scope.calculateSummaryAmounts();
	}
	
	//service wise details
	$scope.removeService = function(index){
		$scope.listTPackageServicesDetailsDto.splice(index,1);
		$scope.calculateSummaryAmounts();
	}
	
	$scope.calculateSummaryAmounts = function()
	{
		
		 angular.forEach($scope.packageObjResponse.paymentEntitlement,function(paymentEnt,indexPaymentEnt)
				{
				angular.forEach(paymentEnt.patientCategoryList, function(patientCat,indexPatientCat){
					angular.forEach(patientCat.billingBedCategoryList,function(billingBedCat,indexBillingBedCat)
							{								
						     var temp = 0;
						      billingBedCat.mPackageMasterDto.packagePrice = $rootScope.parseFloatNum(0);
							  angular.forEach(billingBedCat.mPackageMasterDto.listTPackageServicesDetailsDto,function(serviceDtls,indexServiceDtls)
									  {
								         serviceDtls.numberToBeUse= (serviceDtls.numberToBeUse==null)?1:serviceDtls.numberToBeUse;
								         serviceDtls.servicePrice = serviceDtls.servicePrice!=null && serviceDtls.servicePrice!=undefined ? serviceDtls.servicePrice : 0;
								         serviceDtls.total = $rootScope.parseFloatNum(serviceDtls.numberToBeUse * serviceDtls.servicePrice);
								         temp = temp + parseFloat(serviceDtls.total);
							          });
							    billingBedCat.mPackageMasterDto.serviceGroupAmt = $rootScope.parseFloatNum(temp);
								temp = parseFloat(0);
								angular.forEach(billingBedCat.mPackageMasterDto.listTPackageEitherorGroupDetailsDto, function(value,index)
								{
									value.numberServiceUsable=value.numberServiceUsable!="" && value.numberServiceUsable!=undefined && value.numberServiceUsable!=null ? value.numberServiceUsable : 1;
									value.groupPrice= value.groupPrice !=null && value.groupPrice!=undefined ? $rootScope.parseFloatNum(value.groupPrice) :$rootScope.parseFloatNum(0)  ;	
									temp = temp + parseFloat(value.groupPrice);
								});
								billingBedCat.mPackageMasterDto.packageGroupAmt =  $rootScope.parseFloatNum(temp);
								billingBedCat.mPackageMasterDto.packageCost = $rootScope.parseFloatNum(parseFloat(billingBedCat.mPackageMasterDto.serviceGroupAmt) + parseFloat(billingBedCat.mPackageMasterDto.packageGroupAmt));
								billingBedCat.mPackageMasterDto.packagePrice = $rootScope.parseFloatNum(parseFloat(billingBedCat.mPackageMasterDto.serviceGroupAmt) + parseFloat(billingBedCat.mPackageMasterDto.packageGroupAmt));
								billingBedCat.mPackageMasterDto.validityPeriodIndays= (billingBedCat.mPackageMasterDto.validityPeriodIndays==null || billingBedCat.mPackageMasterDto.validityPeriodIndays==undefined || billingBedCat.mPackageMasterDto.validityPeriodIndays=='') ? 1 : billingBedCat.mPackageMasterDto.validityPeriodIndays;
								billingBedCat.mPackageMasterDto.validityPeriodIndays = (parseInt(billingBedCat.mPackageMasterDto.validityPeriodIndays)<0 || parseInt(billingBedCat.mPackageMasterDto.validityPeriodIndays)==0) ? 1 : billingBedCat.mPackageMasterDto.validityPeriodIndays ;
							
					      });	
				});
			});
	}
	
	
	
	$scope.saveEitherOrPackage = function(fetch)
	{
		$scope.packageObj.orgId = $scope.orgId;
		$scope.packageObj.unitId = $scope.unitId;
		$scope.packageObj.createdBy = $scope.userId;
		$scope.packageObj.updatedBy = $scope.userId;
		$scope.packageObj.packageTypeId = 2;
		$scope.packageObj.serviceId=$scope.packageObj.serviceId;
//		$scope.packageObj.tariffId=$scope.packageObj.tariffId;
        $scope.packageObj.parentPackageId=$scope.parentPackageId;
        $scope.packageObj.packageName = $scope.packageService;
		$scope.packageObj.packageMasterDtoList=[];
		$scope.saveFlag = true;
		
		if( $scope.packageObj.sexId==="" || $scope.packageObj.sexId===null || $scope.packageObj.sexId===undefined)
		{
		  growl.warning("Please select gender.",{title : 'Warning!'});
		  $scope.saveFlag = false;
		  return false;
		}
	      if($scope.packageObj.serviceId=="" || $scope.packageObj.serviceId==undefined){
    			growl.warning("Please select package service!",{title : 'Warning!'});
    			return false;
    		}
    		if($scope.packageObj.tariffId=="" || $scope.packageObj.tariffId==undefined){
    			growl.warning("Please select tariff!",{title : 'Warning!'});
    			return false;
    		}
    		if($scope.selectedPaymentEntitlementList==undefined || $scope.selectedPaymentEntitlementList.length==0){
    			growl.warning("Please Select payment entitlement!",{title : 'Warning!'});
    			return false;
    		}
    		if($scope.selectedPatientTypeList==undefined || $scope.selectedPatientTypeList.length==0){
    			growl.warning("Please Select patient type!",{title : 'Warning!'});
    			return false;
    		}
    		if($scope.selectedVisitTypeList==undefined || $scope.selectedVisitTypeList.length==0){
    			growl.warning("Please Select visit type!",{title : 'Warning!'});
    			return false;
    		}
    		if($scope.selectedBedBillingCategoryList==undefined || $scope.selectedBedBillingCategoryList.length==0){
    			growl.warning("Please Select patient billing bed category!",{title : 'Warning!'});
    			return false;
    		}
    if(angular.equals({}, $scope.packageObjResponse) ||
    ($scope.packageObjResponse.paymentEntitlement[0].patientCategoryList[0].billingBedCategoryList[0].mPackageMasterDto.listTPackageServicesDetailsDto==null
     && $scope.packageObjResponse.paymentEntitlement[0].patientCategoryList[0].billingBedCategoryList[0].mPackageMasterDto.listTPackageEitherorGroupDetailsDto==null ))
    			{
    			    growl.warning("Please add data to package!",{title : 'Warning!'});
                    return false;
    			}
		
		angular.forEach($scope.packageObjResponse.paymentEntitlement,function(paymentEnt,indexPaymentEnt)
		 {
			angular.forEach(paymentEnt.patientCategoryList, function(patientCat,indexPatientCat){
				angular.forEach(patientCat.billingBedCategoryList,function(billingBedCat,indexBillingBedCat){
					        $scope.packageMasterDtoList=[];
							var tempPackageObj={};
							tempPackageObj.packageTypeId = 2;
							tempPackageObj.visitTypeId = billingBedCat.mPackageMasterDto.visitTypeId;
							tempPackageObj.patientTypeId  =billingBedCat.mPackageMasterDto.patientTypeId;
							tempPackageObj.billingBedCategoryId   =billingBedCat.mPackageMasterDto.billingBedCategoryId;
							tempPackageObj.paymentEntitlementTypeId=billingBedCat.mPackageMasterDto.paymentEntitlementTypeId;
							tempPackageObj.sexId = $scope.packageObj.sexId;
							tempPackageObj.packagePrice = billingBedCat.mPackageMasterDto.packagePrice;
							tempPackageObj.packageCost=billingBedCat.mPackageMasterDto.packageCost;
							tempPackageObj.orgId = $scope.orgId;
							tempPackageObj.unitId = $scope.unitId;
							tempPackageObj.tariffId=$scope.packageObj.tariffId;
							tempPackageObj.markupDownInPercentage =  billingBedCat.mPackageMasterDto.markupDownInPercentage;
							tempPackageObj.manualRoundoffAmount = billingBedCat.mPackageMasterDto.manualRoundoffAmount;
							tempPackageObj.isMarkupDown =  billingBedCat.mPackageMasterDto.isMarkupDown;
							tempPackageObj.roundOffPositNeg =  billingBedCat.mPackageMasterDto.roundOffPositNeg;
							tempPackageObj.validityPeriodIndays =  billingBedCat.mPackageMasterDto.validityPeriodIndays;
							
							if(isNaN(Number(billingBedCat.mPackageMasterDto.validityPeriodIndays))||Number(billingBedCat.mPackageMasterDto.validityPeriodIndays)==0)
							{
							  growl.warning("Package validity should not be zero!",{title : 'Warning!'});
							  $scope.saveFlag = false;
							  return false;
							}
							if(isNaN(Number(billingBedCat.mPackageMasterDto.packagePrice))||Number(billingBedCat.mPackageMasterDto.packagePrice)==0)
								{
								 growl.warning("Package price should not be zero!",{title : 'Warning!'});
								 $scope.saveFlag = false;
								 return false;
								}
							if(Number(billingBedCat.mPackageMasterDto.packageCost)>Number(billingBedCat.mPackageMasterDto.packagePrice))
							{
								 growl.warning("Package price should not be less than package cost.",{title : 'Warning!'});
								 $scope.saveFlag = false;
								 return false;
							}
							if(billingBedCat.mPackageMasterDto.listTPackageServicesDetailsDto!=null && billingBedCat.mPackageMasterDto.listTPackageServicesDetailsDto!=undefined)
							{
								 angular.forEach(billingBedCat.mPackageMasterDto.listTPackageServicesDetailsDto,function(serviceDtls,indexServiceDtls)
										 {
									    var apportionedPrice = (parseFloat(serviceDtls.servicePrice) / 100) * parseFloat(billingBedCat.mPackageMasterDto.markupDownInPercentage);
										switch($scope.isMarkupDown)
										{
											case "1":
												serviceDtls.apportionedPrice = parseFloat(serviceDtls.servicePrice) + parseFloat(apportionedPrice);
												break;
											case "2":
												serviceDtls.apportionedPrice = parseFloat(serviceDtls.servicePrice) - parseFloat(apportionedPrice);
												break;
										}
								});
							}
							if(billingBedCat.mPackageMasterDto.listTPackageEitherorGroupDetailsDto!=null && billingBedCat.mPackageMasterDto.listTPackageEitherorGroupDetailsDto!=undefined)
							{
								 angular.forEach(billingBedCat.mPackageMasterDto.listTPackageEitherorGroupDetailsDto,function(groupDtls,indexServiceDtls)
										 {
									       angular.forEach(groupDtls.listTPackageServicesDetailsDto,function(serviceDtls,indexServiceDtls)
											  {
										       serviceDtls.apportionedPrice = (parseFloat(serviceDtls.servicePrice));
									         });
								        });
							}	
						   tempPackageObj.listTPackageServicesDetailsDto=billingBedCat.mPackageMasterDto.listTPackageServicesDetailsDto;
						   tempPackageObj.listTPackageEitherorGroupDetailsDto=billingBedCat.mPackageMasterDto.listTPackageEitherorGroupDetailsDto;
					       $scope.packageObj.packageMasterDtoList.push(tempPackageObj);
					       
				});		
			});
		});
		if(fetch){
                     $scope.editPackage($scope.packageObj);
        		}
		if($scope.saveFlag && !fetch){
			$rootScope.startLoader();
			var URI = BASE_URL + ROOT_URL + "/api/packages/eitheror";
			BillingGenericService.serviceAction(METHOD_POST,URI,$scope.packageObj ,NOTIFICATION_MSG_STATUS_TRUE).then(function(response) 
					{
						if (response.data.status == "success"){
							 $scope.init();
						    $rootScope.stopLoader();
						    //enabling save button start
						  	/*$("#savePackageId").removeClass("enabled");
							$("#savePackageId").addClass("disabled");*/
							//enabling save button end
							$state.go('eitherOrPackage');
							
						}
						else if(response.data.status == "error")
							{
							   $rootScope.stopLoader();
							   //enabling save button start
							  	/*$("#savePackageId").removeClass("disabled");
								$("#savePackageId").addClass("enabled");*/
								//enabling save button end
							}
			  });
		}
	 	
	  }
	
	$scope.searchPackage=function(){
		$rootScope.startLoader();
		var URI = BASE_URL + ROOT_URL + "/api/packages/searchPackageCommonFilter";
		BillingGenericService.serviceAction(METHOD_POST,URI, $scope.importPkgObj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
			if (response.data.status == "success")
			{
			   $scope.searchList=[];
			   $scope.searchList= response.data.listObject;
			   console.log("searchList",$scope.searchList);
				$rootScope.stopLoader();
			}	
			else if (response.data.status == "error"){
				$rootScope.stopLoader();
			}
		});
	}
	  
	$scope.getSelectedPackageServices=function(importFrom){
	if(angular.equals({}, $scope.packDetailsObj) || $scope.packDetailsObj==undefined){
			growl.warning("Please Select package !",{title : 'Warning!'});
			return false;
	}
	$('#importFromPackageModal').modal('hide');
	$rootScope.startLoader();
	var paraentPckId=$scope.packDetailsObj.parentPackageId
	var data={};
	data[paraentPckId]=$scope.packDetailsObj.packageTypeId
	var visitTypeArray=[];
	var URI = BASE_URL + ROOT_URL + "/api/packages/getPackagesByPackageId";
	BillingGenericService.serviceAction(METHOD_POST,URI, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
		if (response.data.status == "success")
		{
			$scope.selectedPaymentEntitlementList=[];
			$scope.selectedPatientTypeList=[];
		    $scope.packageObjResponse=  response.data.object==null?{}:response.data.object;
		    $scope.packageObj.sexId   = $scope.packageObjResponse.parentPackageMasterDto.sexId;
		    $scope.packageObj.tariffId  = $scope.packageObjResponse.parentPackageMasterDto.packageMasterDtoList[0].tariffId;
	         if(!importFrom){
		     $scope.packageService = $scope.packageObjResponse.parentPackageMasterDto.packageName;
             $scope.packageObj.serviceId = $scope.packageObjResponse.parentPackageMasterDto.serviceId;
		    }
		    tempPackgeObj= angular.copy($scope.packageObjResponse);
		    angular.forEach($scope.packageObjResponse.paymentEntitlement,function(paymentEnt,indexPaymentEnt){
				angular.forEach(paymentEnt.patientCategoryList, function(patientCat,indexPatientCat){
					angular.forEach(patientCat.billingBedCategoryList,function(billingBedCat,indexBillingBedCat)
				     {		
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
						  
						  visitTypeArray.push(billingBedCat.mPackageMasterDto.visitTypeId);
						  
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
					var found= $scope.selectedPatientTypeList.find(item=>item.id==patientCat.patientTypeId);
					if(found==undefined)
					$scope.selectedPatientTypeList.push(patientCategoryData);
				});
				var paymentEntData={
						id:paymentEnt.paymentEntitlementId,
						label:paymentEnt.paymentEntitlement
				}
				$scope.selectedPaymentEntitlementList.push(paymentEntData);
			});
		   var uniqueVisitType=new Set(visitTypeArray);
		   $scope.selectedVisitTypeList = Array.from(uniqueVisitType);
		   $scope.selectedVisitTypeList = $scope.packageObjResponse.selectedVisitTypeList;
           $scope.selectedPatientTypeList =  $scope.packageObjResponse.selectedPatientTypeList;
           $scope.selectedBedBillingCategoryList = $scope.packageObjResponse.selectedBedBillingCategoryList;
           $scope.selectedPaymentEntitlementList =  $scope.packageObjResponse.selectedPaymentEntitlementList;
           $scope.bedBillingCategoryList = $scope.packageObjResponse.selectedBedBillingCategoryList;
           $scope.fetchValidate = false;
		   $rootScope.stopLoader();
		   $scope.calculateSummaryAmounts();
		   $scope.crateClone();
		}	
		else if (response.data.status == "error"){
			$rootScope.stopLoader();
		}
		$scope.packDetailsObj={};
	});
  }
	

	$scope.clear=function(){
		$scope.init();
		tempPackgeObj = {};
		$scope.packageObjResponse={};
		$scope.initPackageObj();
		$scope.fetchValidate = false;
		$scope.selectedPaymentEntitlementListClone=undefined;
        $scope.selectedBedBillingCategoryListClone=undefined;
        $scope.selectedPatientTypeListClone=undefined;
        $scope.selectedVisitTypeListClone=undefined;
        $scope.tariffIdClone=undefined;
        $scope.sexIdClone=undefined;
//		$("#divEnabled").removeClass("disabled");
//		$("#divEnabled").addClass("enabled");
//		$("#divEnabled2").removeClass("disabled");
//		$("#divEnabled2").addClass("enabled");
		
		// disabling save button start
	  /*	$("#savePackageId").removeClass("enabled");
		$("#savePackageId").addClass("disabled");*/
		// disabling save button end
	}
	//validation for import from packages
	$scope.importFromPackage=function(){
		
		if($scope.packageObj.serviceId=="" || $scope.packageObj.serviceId==undefined){
			growl.warning("Please select package service!",{title : 'Warning!'});
			return false;
		}
		$('#importFromPackageModal').modal('show');
		/*$("#divEnabled").removeClass("enabled");
		$("#divEnabled").addClass("disabled");
		$("#divEnabled2").removeClass("enabled");
		$("#divEnabled2").addClass("disabled");*/
		
	}
	
	$scope.removeService = function(serviceId)
	{
		angular.forEach($scope.packageObjResponse.paymentEntitlement,function(paymentEnt,indexPaymentEnt)
			{
			angular.forEach(paymentEnt.patientCategoryList, function(patientCat,indexPatientCat){
				angular.forEach(patientCat.billingBedCategoryList,function(billingBedCat,indexBillingBedCat)
						{								
						  angular.forEach(billingBedCat.mPackageMasterDto.listTPackageServicesDetailsDto,function(serviceDtls,indexServiceDtls)
								  {
							      if(serviceDtls.serviceId==serviceId)
							    	 {
							    	   billingBedCat.mPackageMasterDto.listTPackageServicesDetailsDto.splice(indexServiceDtls, 1);  
							    	 }
						          });
				      });	
			   });
		 });
		tempPackgeObj= angular.copy($scope.packageObjResponse);
		$scope.calculateSummaryAmounts();
	}
	
	$scope.removeEitherOrGroup = function(grpIndex)
	{
		angular.forEach($scope.packageObjResponse.paymentEntitlement,function(paymentEnt,indexPaymentEnt)
			{
			angular.forEach(paymentEnt.patientCategoryList, function(patientCat,indexPatientCat){
				angular.forEach(patientCat.billingBedCategoryList,function(billingBedCat,indexBillingBedCat)
						{								
						  angular.forEach(billingBedCat.mPackageMasterDto.listTPackageEitherorGroupDetailsDto,function(grpDtls,index)
								  {
							      if(index==grpIndex)
							    	 {
							    	   billingBedCat.mPackageMasterDto.listTPackageEitherorGroupDetailsDto.splice(index, 1);  
							    	 }
						          });
				      });	
			   });
		 });
		tempPackgeObj= angular.copy($scope.packageObjResponse);
		$scope.calculateGroupAmounts();
		$scope.calculateSummaryAmounts();
	}

	$scope.checkPackagePrice = function(packageCost,packagePrice,pEntIndex,peIndex,bBedIndex)
	{
		if(Number(packageCost)>0 && Number(packagePrice)>0)
			{ 
			  if(Number(packageCost)> Number(packagePrice))
				  {
				    growl.warning("Package price should not be less than package cost.",{title : 'Warning!'});
				    $scope.packageObjResponse.paymentEntitlement[pEntIndex].patientCategoryList[peIndex].billingBedCategoryList[bBedIndex].mPackageMasterDto.packagePrice =packageCost; 
					return packageCost; 
				  }
			 
			}
	}
	
	  }catch(e){
		console.log(e.message);
	 }
	  
	  //	------------------edit package-------------------
      		if($stateParams.newEitherOrPackageObj!=undefined && $stateParams.newEitherOrPackageObj!=null && $stateParams.newEitherOrPackageObj.parentPackageId>0){
      		            $scope.packDetailsObj = {};
                        $scope.packDetailsObj.parentPackageId = $stateParams.newEitherOrPackageObj.parentPackageId;
                        $scope.parentPackageId = $stateParams.newEitherOrPackageObj.parentPackageId;
                        $scope.packDetailsObj.packageTypeId = 2;
                        $scope.getSelectedPackageServices(false);
          			}
          	$scope.editPackage = function(packageObjectFetch){
      			$rootScope.startLoader();
      			var visitTypeArray=[];
      			$scope.addNewServiceList = [];
      			packageObjectFetch.paymentEntitlement =$scope.paymentEntitlementList;
                  packageObjectFetch.patientCategoryList = $scope.patientTypeList;
                  packageObjectFetch.billingBedCategoryList =$scope.bedBillingCategoryListReference;
                  arraList=cartesian($scope.selectedPaymentEntitlementList,$scope.selectedPatientTypeList,$scope.selectedBedBillingCategoryList);
                  			for(i=0;i<arraList.length;i++){
                  				var tempObj={
                  						paymentEntitlementId:"",paymentEntitlement:"",
                  						visitTypeId:"",visitTypeName:"",
                  						patientTypeId:"",patientType:"",
                  						billingBedCategoryId:"",billingBedCategory:"",
                  						packagetypeId:2,orgId:$scope.orgId,unitId:$scope.unitId,
                  					};

                  				var tempArr=arraList[i];
                  				$scope.paymentEntitlementId =tempArr[0].id;
                  				$scope.patientTypeId =tempArr[1].id;
                  //				$scope.visitTypeId =tempArr[2].id==undefined?tempArr[2]:tempArr[2].id;
                  				$scope.bedBillingCategoryId =tempArr[2].id;
                  				$scope.visitTypeId = $scope.getVisitTypeByBillingBedCategoryId($scope.bedBillingCategoryId);
                  				if(($scope.visitTypeId == 1 && $scope.bedBillingCategoryId == $scope.opdBillingCatId) || ($scope.visitTypeId != 1 && $scope.opdBillingCatId !=$scope.bedBillingCategoryId))
                  					{
                  						var newTempObj = angular.copy(tempObj);
                  						newTempObj.visitTypeId=$scope.visitTypeId;
                  						newTempObj.visitTypeName=$scope.visitTypeName($scope.visitTypeId);
                  						newTempObj.paymentEntitlementId=$scope.paymentEntitlementId;
                  						newTempObj.paymentEntitlement=$scope.paymentEntitlementName($scope.paymentEntitlementId);
                  						newTempObj.patientTypeId=$scope.patientTypeId;
                  						newTempObj.patientType=$scope.patientTypeName($scope.patientTypeId);
                  						newTempObj.billingBedCategoryId=$scope.bedBillingCategoryId;
                  						newTempObj.billingBedCategory=$scope.bedBillingCategoryName($scope.bedBillingCategoryId);
                  						newTempObj.tariffId=$scope.packageObj.tariffId;
                  					    $scope.addNewServiceList.push(newTempObj);
                  			       }
                  			}
                packageObjectFetch.serviceForPackageReqList=$scope.addNewServiceList;
      			var URI = BASE_URL + ROOT_URL + "/api/packages/editPackage";
      			BillingGenericService.serviceAction(METHOD_POST,URI, packageObjectFetch,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
      				if (response.data.status == "success")
      				{
//      				    $("#savePackageId").removeClass("disabled");
//                        $("#savePackageId").addClass("enabled");
      					$scope.selectedPaymentEntitlementList=[];
      					$scope.selectedPatientTypeList=[];
      				    $scope.packageObjResponse=  response.data.object==null?{}:response.data.object;
      				    $scope.packageObj.sexId   = $scope.packageObjResponse.parentPackageMasterDto.sexId;
      				    $scope.packageObj.minAge  = $scope.packageObjResponse.parentPackageMasterDto.minAge;
      				    $scope.packageObj.maxAge  = $scope.packageObjResponse.parentPackageMasterDto.maxAge;
      				    $scope.packageObj.packageTypeId  = $scope.packageObjResponse.parentPackageMasterDto.packageTypeId;
      				    $scope.packageObj.tariffId  = $scope.packageObjResponse.parentPackageMasterDto.packageMasterDtoList[0].tariffId;
                        $scope.packageObj.packageTypeId = "2";
      				     tempPackgeObj= angular.copy($scope.packageObjResponse);
                        		    angular.forEach($scope.packageObjResponse.paymentEntitlement,function(paymentEnt,indexPaymentEnt){
                        				angular.forEach(paymentEnt.patientCategoryList, function(patientCat,indexPatientCat){
                        					angular.forEach(patientCat.billingBedCategoryList,function(billingBedCat,indexBillingBedCat)
                        				     {
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

                        						  visitTypeArray.push(billingBedCat.mPackageMasterDto.visitTypeId);

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
                        					var found= $scope.selectedPatientTypeList.find(item=>item.id==patientCat.patientTypeId);
                        					if(found==undefined)
                        					$scope.selectedPatientTypeList.push(patientCategoryData);
                        				});
                        				var paymentEntData={
                        						id:paymentEnt.paymentEntitlementId,
                        						label:paymentEnt.paymentEntitlement
                        				}
                        				$scope.selectedPaymentEntitlementList.push(paymentEntData);
                        			});
                        		   var uniqueVisitType=new Set(visitTypeArray);
                        		   $scope.selectedVisitTypeList = Array.from(uniqueVisitType);
                        		   $scope.selectedVisitTypeList = $scope.packageObjResponse.selectedVisitTypeList;
                                   $scope.selectedPatientTypeList =  $scope.packageObjResponse.selectedPatientTypeList;
                                   $scope.selectedBedBillingCategoryList = $scope.packageObjResponse.selectedBedBillingCategoryList;
                                   $scope.selectedPaymentEntitlementList =  $scope.packageObjResponse.selectedPaymentEntitlementList;
                                   $scope.bedBillingCategoryList = $scope.packageObjResponse.selectedBedBillingCategoryList;
                                   $scope.fetchValidate = false;
                        		   $rootScope.stopLoader();
                        		   $scope.calculateSummaryAmounts();
                        		   $scope.crateClone();
      				}
      				else if (response.data.status == "error"){
      					$rootScope.stopLoader();
      				}
      				$scope.packDetailsObj={};
      			});
          	}
       $scope.validateFetch = function(tarrifChange)
           {
              $scope.fetchValidate = true;
               var validate = true;
               if(!angular.equals({}, $scope.packageObjResponse) && $scope.packageObjResponse.paymentEntitlement[0].patientCategoryList[0].billingBedCategoryList[0].mPackageMasterDto.listTPackageEitherorGroupDetailsDto
                       !=null && $scope.packageObjResponse.paymentEntitlement[0].patientCategoryList[0].billingBedCategoryList[0].mPackageMasterDto.listTPackageEitherorGroupDetailsDto.length>0)
                       {
                          validate = true;
                       }
               if(!angular.equals({}, $scope.packageObjResponse) && $scope.packageObjResponse.paymentEntitlement[0].patientCategoryList[0].billingBedCategoryList[0].mPackageMasterDto.listTPackageServicesDetailsDto
                                      !=null && $scope.packageObjResponse.paymentEntitlement[0].patientCategoryList[0].billingBedCategoryList[0].mPackageMasterDto.listTPackageServicesDetailsDto.length>0)
                       {
                          validate = true;
                       }
               if (($scope.packageObj.sexId == $scope.sexIdClone)
                                 &&($scope.packageObj.tariffId == $scope.tariffIdClone)
                                         &&(Array.isArray($scope.selectedVisitTypeListClone) && Array.isArray($scope.selectedVisitTypeList) &&
                                                        $scope.selectedVisitTypeListClone.length === $scope.selectedVisitTypeList.length &&
                                                        $scope.selectedVisitTypeListClone.every((val, index) => val.id == $scope.selectedVisitTypeList[index].id))
                                         &&(Array.isArray($scope.selectedPatientTypeListClone) && Array.isArray($scope.selectedPatientTypeList) &&
                                                        $scope.selectedPatientTypeListClone.length === $scope.selectedPatientTypeList.length &&
                                                        $scope.selectedPatientTypeListClone.every((val, index) => val.id == $scope.selectedPatientTypeList[index].id))
                                         &&(Array.isArray($scope.selectedBedBillingCategoryListClone) && Array.isArray($scope.selectedBedBillingCategoryList) &&
                                                        $scope.selectedBedBillingCategoryListClone.length === $scope.selectedBedBillingCategoryList.length &&
                                                        $scope.selectedBedBillingCategoryListClone.every((val, index) => val.id == $scope.selectedBedBillingCategoryList[index].id))
                                         &&(Array.isArray($scope.selectedPaymentEntitlementListClone) && Array.isArray($scope.selectedPaymentEntitlementList) &&
                                                        $scope.selectedPaymentEntitlementListClone.length === $scope.selectedPaymentEntitlementList.length &&
                                                        $scope.selectedPaymentEntitlementListClone.every((val, index) => val.id == $scope.selectedPaymentEntitlementList[index].id))
                                        ) {
                                          validate = false;
                                        }
                    if($scope.selectedPaymentEntitlementListClone==undefined
                              && $scope.selectedBedBillingCategoryListClone==undefined
                              && $scope.selectedPatientTypeListClone==undefined
                              && $scope.selectedVisitTypeListClone==undefined
                              && $scope.tariffIdClone==undefined
                              && $scope.sexIdClone==undefined
                              && $scope.minAgeClone==undefined
                              && $scope.maxAgeClone==undefined
                              ){
                              validate = false;
                           }
               $scope.fetchValidate = validate;
           }

       $scope.fetchEvent = {
        onSelectionChanged: function() {
                                      $scope.validateFetch(false);
                                }
       };

          //validation
          $scope.crateClone = function(packageObjResponse)
          {
               $scope.sexIdClone   =  $scope.packageObj.sexId;
               $scope.tariffIdClone  =  $scope.packageObj.tariffId;
               $scope.selectedVisitTypeListClone =  angular.copy($scope.selectedVisitTypeList);
               $scope.selectedPatientTypeListClone =  angular.copy($scope.selectedPatientTypeList);
               $scope.selectedBedBillingCategoryListClone = angular.copy($scope.selectedBedBillingCategoryList);
               $scope.selectedPaymentEntitlementListClone =  angular.copy($scope.selectedPaymentEntitlementList);
          }
	  
}]);	