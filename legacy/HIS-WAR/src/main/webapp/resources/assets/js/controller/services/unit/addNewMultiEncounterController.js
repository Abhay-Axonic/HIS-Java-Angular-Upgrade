

angular.module("myApp").controller("addNewMultiEncounterController", [ '$scope','$rootScope','BillingGenericService','$cookies','$state','growl','servicesFactory','GenericService','itemFactory','$stateParams',
  function($scope,$rootScope,BillingGenericService,$cookies,$state,growl,servicesFactory,GenericService,itemFactory,$stateParams){
	try{
		$rootScope.loginpage = true;
		
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
		$scope.defaultSelfTariffId = cookieObjectAppConfig.defaultSelfTariffId;
		$scope.userId = cookieObject.userId;
	    $scope.consultationSpecialityId = cookieObjectAppConfig.consultationSpecialityId;
		
		$scope.init = function(){
			$scope.isMarkupDown='';
			$scope.roundOffPositNeg = '';
			$scope.markupDownInPercentage = '';
			$scope.searchServiceList = [];
			$scope.specialityList = [];
			$scope.subSpecialityList = [];
			$scope.searchServObj = {};
			servicesFactory.initServiceSearch($scope);
			$scope.selectedServiceList = [];
			$scope.initPackageObj();
			$scope.serviceWiseSummary = 0;
			$scope.grandTotal = 0;
			$scope.packageService = "";
			$scope.itemObj = {};
			$scope.packageObjResponse = {};
			itemFactory.initItemFactory($scope);
            $scope.parentPackageId=null;
			//selected List
			$scope.selectedPatientTypeList=[];
			$scope.selectedVisitTypeList=[];
			$scope.selectedPaymentEntitlementList=[];
			$scope.selectedBedBillingCategoryList=[];
		}
		
		$scope.opdBillingCatId = cookieObjectAppConfig.opdBillingCatId;
		$scope.initPackageObj = function(){
			$scope.packageObj = {packageTypeId:"5",sexId:"",minAge:"",maxAge:"",tariffId:"",serviceId:""}
		}
		$scope.initImportPackageObj = function(){
			$scope.importPkgObj = {
							packageTypeId:"5",
							packageCode:"",
							packageName:""	,
							orgId:$scope.orgId,
							unitId:$scope.unitId
						}
		}
		$scope.initImportPackageObj();
		$scope.init();
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
//										$scope.bedBillingCategoryListReference = [];
//										$scope.bedBillingCategoryListReference = response.data.listObject;
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
		
		// for get active billing category list
		
		$scope.activeBillingBedCategory=function(){				
			
			var URI = BASE_URL + ROOT_URL+ GETACTIVEBILLINGBEDCATEGORYLIST+"/orgId/"+$scope.orgId+"/unitId/"+$scope.unitId;
			GenericService.serviceAction("GET", URI, "").then(
							function(response) {
								$scope.billingCategoryReference = [];
								if (response.data.status == "success") {
									$scope.billingCategoryReference=response.data.listObject;									
									console.log('billingCategory',$scope.billingCategoryReference);
								}
							});
		}
		$scope.activeBillingBedCategory();
		// for getting active visit type
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
		//service factory methods  data-target="#getServicesModal_AllIncPackage"
		$scope.searchServices = function(searchObj){servicesFactory.searchServices(searchObj,$scope)}
		$scope.getSubSpecialityBySpecialityId = function(specialityId){servicesFactory.getSubSpecialityBySpecialityId(specialityId,$scope)}
		$scope.selectAllServices = function(flag){servicesFactory.selectAllServices(flag,$scope)}
		$scope.setOrgUnit = function(orgId,unitId,scope){servicesFactory.setOrgUnit(orgId,unitId,$scope)}
		
		//item factory methods
		$scope.getProductCatByAssestCat = function(assetCategoryId,scope){itemFactory.getProductCatByAssestCat(assetCategoryId,$scope)}
		$scope.itemSearchForPackage = function(itemObj,scope){itemFactory.itemSearchForPackage(itemObj,$scope)}
		$scope.selectAllItems = function(flag,scope){itemFactory.selectAllItems(flag,$scope)}
		
		$scope.getPackageTypeList = function(orgId,unitId) {
				var data = "";
				var URI = BASE_URL + ROOT_URL + "/Unit/Service/EHCpackage/getPackgeType/"+orgId+"/"+unitId;
				BillingGenericService.serviceAction("GET", URI, data).then(function(response){
					$scope.packageTypeList = response.data.listObject;
				});
		}

		$scope.getPaymentEntitlementList = function() {
			var URI = BASE_URL + ROOT_URL + "/global/paymententitlement/"+$scope.orgId;
			BillingGenericService.serviceAction("GET", URI, {}).then(function(response){
				$scope.paymentEntitlementList = response.data.listObject;
			});
		}
		
		$scope.getPatientTypeList = function() {
			var URI = BASE_URL + ROOT_URL + "/global/patientcategory/"+$scope.orgId;
			BillingGenericService.serviceAction("GET", URI, null).then(function(response){
				$scope.patientTypeList= response.data.listObject;
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
		  
			$scope.getTariffList = function(orgId,unitId) {
				var URI = BASE_URL + ROOT_URL + "/Unit/Service/EHCpackage/getTariff/"+orgId+"/"+unitId;
				BillingGenericService.serviceAction("GET", URI, null).then(function(response){
					$scope.tariffList= response.data.listObject;
				});
			}
		
		$scope.getPackageTypeList($scope.orgId,$scope.unitId);
		$scope.getTariffList($scope.orgId,$scope.unitId);
		$scope.getPatientTypeList();
		$scope.getPaymentEntitlementList();
	    
		/** Service Autocomplete List * */
		$scope.getPackageServiceList = function(searchKeyword)
		{
				var data = "";
				var URI = BASE_URL + ROOT_URL + "/Unit/Service/EHCpackage/autoCompleteServices/"+$scope.orgId+"/"+$scope.unitId+"/"+searchKeyword;
				return BillingGenericService.serviceAction("GET", URI,data).then(function(response) {
					return response.data.listObject;
				});
		}	
	    
		$scope.selectPackageService = function($item, $model,$label)
		{
	      $scope.packageObj.serviceId = $item.id;
        }
		
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
	     
		
	}catch(e){
		console.log(e.message);
	}
	
	
	$scope.getServices=function(){
		  // validation
		
		$scope.searchServiceList = [];
		$scope.searchServObj = {};
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
		$('#getServicesModal_AllIncPackage').modal('show');
	}
	
	$scope.calculatePackageAmounts = function(serviceId,index)
	{
		angular.forEach($scope.packageObjResponse.paymentEntitlement,function(paymentEnt,indexPaymentEnt)
			{
			angular.forEach(paymentEnt.patientCategoryList, function(patientCat,indexPatientCat){
				angular.forEach(patientCat.billingBedCategoryList,function(billingBedCat,indexBillingBedCat)
						{								
					     var total = 0;
					      packagePriceTemp=billingBedCat.mPackageMasterDto.packagePrice ;
					     billingBedCat.mPackageMasterDto.packagePrice = $rootScope.parseFloatNum(0);
						  angular.forEach(billingBedCat.mPackageMasterDto.listTPackageServicesDetailsDto,function(serviceDtls,indexServiceDtls)
								  {
							         serviceDtls.numberToBeUse = (parseInt(serviceDtls.numberToBeUse)<0 || parseInt(serviceDtls.numberToBeUse)==0) ?1 : serviceDtls.numberToBeUse;
							         serviceDtls.servicePrice=serviceDtls.servicePrice==''? $rootScope.parseFloatNum(0) :$rootScope.parseFloatNum(serviceDtls.servicePrice);
							         serviceDtls.total = $rootScope.parseFloatNum(serviceDtls.numberToBeUse * serviceDtls.servicePrice);
//							         serviceDtls.servicePrice = serviceDtls.numberToBeUse * serviceDtls.servicePrice;
							         total = parseFloat(total) + (isNaN(serviceDtls.total) ? 0 : parseFloat(serviceDtls.total));
						          });
						  
						   var itemWiseTotal = 0;
						  angular.forEach(billingBedCat.mPackageMasterDto.listTPackageItemDetailsDto,function(itemDtls,indexItemeDtls)
								  {
//							  		 itemDtls.servicePrice=itemDtls.servicePrice==''? $rootScope.parseFloatNum(0) :$rootScope.parseFloatNum(itemDtls.servicePrice);
							  		 itemDtls.servicePrice=isNaN(itemDtls.servicePrice) || itemDtls.servicePrice == "" || typeof(itemDtls.servicePrice) == "undefined" ? parseFloat(0) : itemDtls.servicePrice;
							  		 itemWiseTotal = parseFloat(itemWiseTotal) + (isNaN(itemDtls.servicePrice) ? 0 : parseFloat(itemDtls.servicePrice));
						          });
						  
						    billingBedCat.mPackageMasterDto.serviceWiseSummary = $rootScope.parseFloatNum(total);
						    billingBedCat.mPackageMasterDto.itemWiseSummary = $rootScope.parseFloatNum(itemWiseTotal);
							billingBedCat.mPackageMasterDto.grandTotal = $rootScope.parseFloatNum(total);
							billingBedCat.mPackageMasterDto.isMarkupDown = $scope.isMarkupDown;
//							billingBedCat.mPackageMasterDto.packagePrice = $rootScope.parseFloatNum(total);
							var finalTotal=parseFloat(total)+parseFloat(itemWiseTotal);
							
							if(serviceId!=undefined && index!=undefined){
								billingBedCat.mPackageMasterDto.packagePrice = $rootScope.parseFloatNum(finalTotal);
							}else{
								billingBedCat.mPackageMasterDto.packagePrice= parseFloat(packagePriceTemp) > parseFloat(finalTotal) ?  $rootScope.parseFloatNum(packagePriceTemp) :  $rootScope.parseFloatNum(finalTotal);
							}
//							billingBedCat.mPackageMasterDto.packagePrice= billingBedCat.mPackageMasterDto.packagePriceTemp > total ?  $rootScope.parseFloatNum(billingBedCat.mPackageMasterDto.packagePriceTemp) :  $rootScope.parseFloatNum(total);
							billingBedCat.mPackageMasterDto.packageCost = finalTotal;
							
							billingBedCat.mPackageMasterDto.markupDownInPercentage  = $scope.markupDownInPercentage;
							billingBedCat.mPackageMasterDto.validityPeriodIndays= (billingBedCat.mPackageMasterDto.validityPeriodIndays==null || billingBedCat.mPackageMasterDto.validityPeriodIndays==undefined || billingBedCat.mPackageMasterDto.validityPeriodIndays=='') ? 1 : billingBedCat.mPackageMasterDto.validityPeriodIndays;
							billingBedCat.mPackageMasterDto.validityPeriodIndays = (parseInt(billingBedCat.mPackageMasterDto.validityPeriodIndays)<0 || parseInt(billingBedCat.mPackageMasterDto.validityPeriodIndays)==0) ? 1 : billingBedCat.mPackageMasterDto.validityPeriodIndays ;
							billingBedCat.mPackageMasterDto.noOfEncounters = (billingBedCat.mPackageMasterDto.noOfEncounters==null || billingBedCat.mPackageMasterDto.noOfEncounters==undefined || billingBedCat.mPackageMasterDto.noOfEncounters=='') ? 1 : billingBedCat.mPackageMasterDto.noOfEncounters;
							billingBedCat.mPackageMasterDto.noOfEncounters = (parseInt(billingBedCat.mPackageMasterDto.noOfEncounters)<0 || parseInt(billingBedCat.mPackageMasterDto.noOfEncounters)==0) ? 1 : billingBedCat.mPackageMasterDto.noOfEncounters ;
							//package cost
							switch($scope.isMarkupDown!=undefined ? $scope.isMarkupDown.toString() : "0"){
							case "1" :
								var amount = (billingBedCat.mPackageMasterDto.grandTotal/100) * (isNaN(billingBedCat.mPackageMasterDto.markupDownInPercentage) ? 0 : parseFloat(billingBedCat.mPackageMasterDto.markupDownInPercentage));
								billingBedCat.mPackageMasterDto.packageCost = $rootScope.parseFloatNum(parseFloat(billingBedCat.mPackageMasterDto.grandTotal) + parseFloat(amount));
								break;
							case "2" :
								var amount = (billingBedCat.mPackageMasterDto.grandTotal/100) * (isNaN(billingBedCat.mPackageMasterDto.markupDownInPercentage) ? 0 : parseFloat(billingBedCat.mPackageMasterDto.markupDownInPercentage));
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
							}
				      });	
			});
		});
	}
	
	// getting final service list
	var tempPackgeObj = {};
	$scope.addServices = function(){
		$scope.selectedServiceList = [];
		$scope.selectedServiceIds = [];
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
		$('#getServicesModal_AllIncPackage').modal('hide');
		$rootScope.startLoader();
		$scope.addNewServiceList = [];
		arraList=cartesian($scope.selectedPaymentEntitlementList,$scope.selectedPatientTypeList,$scope.selectedBedBillingCategoryList);
		
		for(i=0;i<arraList.length;i++){
			
			var tempObj={						
					paymentEntitlementId:"",paymentEntitlement:"",
					visitTypeId:"",visitTypeName:"",
					patientTypeId:"",patientType:"",
					billingBedCategoryId:"",billingBedCategory:"",
					packagetypeId:5,orgId:$scope.orgId,unitId:$scope.unitId,
				};
			
			var tempArr=arraList[i];
			
			$scope.paymentEntitlementId =tempArr[0].id;
			$scope.patientTypeId =tempArr[1].id;
//			$scope.visitTypeId =tempArr[2].id;
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
				newTempObj.sexId =$scope.packageObj.sexId;
				console.log(JSON.stringify(newTempObj))
			 $scope.addNewServiceList.push(newTempObj);
		  } //IF
		} //FOr
		console.log("final list",$scope.addNewServiceList);
		
		
		var data = {
				serviceIds:$scope.selectedServiceIds,
				packageTypeId:$scope.packageObj.packageTypeId,
				serviceForPackageReqList:$scope.addNewServiceList,
				sexId :$scope.packageObj.sexId
		};
		
		var URI = BASE_URL + ROOT_URL + "/api/packages/package/service/search";
		BillingGenericService.serviceAction("POST", URI, data).then(function(response)
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
										angular.forEach(patientCat.billingBedCategoryList,function(billingBedCat,indexBillingBedCat){								
											billingBedCat.mPackageMasterDto.noOfEncounters=1;
											billingBedCat.mPackageMasterDto.validityPeriodIndays=1;
												 var tempserviceList = tempPackgeObj.paymentEntitlement[indexPaymentEnt].patientCategoryList[indexPatientCat].billingBedCategoryList[indexBillingBedCat].mPackageMasterDto.listTPackageServicesDetailsDto
												 if(tempserviceList!=undefined && tempserviceList!=null){
												  angular.forEach(billingBedCat.mPackageMasterDto.listTPackageServicesDetailsDto,function(serviceDtls,indexServiceDtls)
														  {
															     serviceDtls.numberToBeUse =1;	
														         serviceDtls.total = serviceDtls.numberToBeUse * serviceDtls.servicePrice;
														         if(billingBedCat.mPackageMasterDto.listTPackageServicesDetailsDto==null)
														        	 billingBedCat.mPackageMasterDto.listTPackageServicesDetailsDto =[];
														       
												          });
											         if(!angular.equals({}, $scope.packageObjResponse))
												      {	 
												         tempPackgeObj.paymentEntitlement[indexPaymentEnt].patientCategoryList[indexPatientCat].billingBedCategoryList[indexBillingBedCat].mPackageMasterDto.listTPackageServicesDetailsDto=
												         tempPackgeObj.paymentEntitlement[indexPaymentEnt].patientCategoryList[indexPatientCat].billingBedCategoryList[indexBillingBedCat].mPackageMasterDto.listTPackageServicesDetailsDto.concat(billingBedCat.mPackageMasterDto.listTPackageServicesDetailsDto);
												      } 
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
											
										});//billingBedCategory			
										
									});//patientCategory
									
								});// payment entitlement end
							  $scope.packageObjResponse=tempPackgeObj;
							  console.log("pck obj", $scope.packageObjResponse);
							}
						$scope.calculatePackageAmounts(undefined,undefined);
						$scope.crateClone();
						$rootScope.stopLoader();
						 //enabling save button start
//					  	$("#savePackageId").removeClass("disabled");
//						$("#savePackageId").addClass("enabled");
						//enabling save button end
		});
	
		
	}
	
	$scope.removeSelectedService = function(index){
		$scope.$scope.selectedServiceIds.splice(index,1);
		$scope.selectedServiceIds.splice(index,1);
	
	}
	//get Items
		$scope.getItems=function(){
			  // validation
			$scope.searchItemList = [];
			$scope.searchServObj = {};
			
			if($scope.packageObj.serviceId=="" || $scope.packageObj.serviceId==undefined){
				growl.warning("Please select package service!",{title : 'Warning!'});
				return false;
			}
			if($scope.selectedPaymentEntitlementList==undefined || $scope.selectedPaymentEntitlementList.length==0){
				growl.warning("Please Select payment entitlement!",{title : 'Warning!'});
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
		
			/*$("#divEnabled").removeClass("enabled");
			$("#divEnabled").addClass("disabled");

			$("#divEnabled2").removeClass("enabled");
			$("#divEnabled2").addClass("disabled");*/
			$('#addNewItemMultiPackage').modal('show');
			
		}
		
		$scope.addItemWiseExclusion = function(){
			$scope.itemWiseExclusionList = [];
			$scope.selecteditemIds = [];
			angular.forEach($scope.searchItemList,function(value,key){
				if(value.isSelected){
					/*value.qty = 0;
					value.total = 0;*/
					$scope.selecteditemIds.push(value.itemId);
					$scope.itemWiseExclusionList.push(value);
				}
			});
			if($scope.itemWiseExclusionList==undefined || $scope.itemWiseExclusionList.length==0){
				growl.warning("Please Select items!",{title : 'Warning!'});
				return false;
			}
			$('#addNewItemMultiPackage').modal('hide');
			$rootScope.startLoader();
			$scope.addNewItemList = [];
			arraList=cartesian($scope.selectedPaymentEntitlementList,$scope.selectedPatientTypeList,$scope.selectedBedBillingCategoryList);
			
			for(i=0;i<arraList.length;i++){
				
				var tempObj={						
						paymentEntitlementId:"",paymentEntitlement:"",
						visitTypeId:"",visitTypeName:"",
						patientTypeId:"",patientType:"",
						billingBedCategoryId:"",billingBedCategory:"",
						packagetypeId:5,orgId:$scope.orgId,unitId:$scope.unitId,
					};
				
				var tempArr=arraList[i];
				
				$scope.paymentEntitlementId =tempArr[0].id;
				$scope.patientTypeId =tempArr[1].id;
//				$scope.visitTypeId =tempArr[2].id;
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
					
					console.log(JSON.stringify(newTempObj))
				 $scope.addNewItemList.push(newTempObj);
			  } //IF
			} //FOr
			console.log("final item list",$scope.addNewItemList);
			
			
			var data = {
					itemIds:$scope.selecteditemIds,
					packageTypeId:$scope.packageObj.packageTypeId,
					serviceForPackageReqList:$scope.addNewItemList
			};
			
			var URI = BASE_URL + ROOT_URL + "/api/packages/package/service/search";
			BillingGenericService.serviceAction("POST", URI, data).then(function(response)
					{
				       
							$scope.tempPackageObjResponse= response.data.object;
							console.log("item lsit",$scope.tempPackageObjResponse);
							if(angular.equals({}, tempPackgeObj))
								{
								   tempPackgeObj= angular.copy($scope.tempPackageObjResponse);
								}
							if(!angular.equals({}, tempPackgeObj) && !angular.equals({}, $scope.tempPackageObjResponse))
								{
								  angular.forEach($scope.tempPackageObjResponse.paymentEntitlement,function(paymentEnt,indexPaymentEnt){
										angular.forEach(paymentEnt.patientCategoryList, function(patientCat,indexPatientCat){
											angular.forEach(patientCat.billingBedCategoryList,function(billingBedCat,indexBillingBedCat){								
												
												 var tempItemList = tempPackgeObj.paymentEntitlement[indexPaymentEnt].patientCategoryList[indexPatientCat].billingBedCategoryList[indexBillingBedCat].mPackageMasterDto.listTPackageItemDetailsDto;
												 if(tempItemList!=undefined && tempItemList!=null)
												 {
													 angular.forEach(billingBedCat.mPackageMasterDto.listTPackageItemDetailsDto,function(itemDtls,indexItemDtlsDtls)
															  {
																  var found = tempItemList.find(function(item) { 
																	  return item.itemId == itemDtls.itemId; 
																	});
														        if(found==undefined || found.length==0)
														        	{
															        	itemDtls.numberToBeUse = 1;
														        	  tempPackgeObj.paymentEntitlement[indexPaymentEnt].patientCategoryList[indexPatientCat].billingBedCategoryList[indexBillingBedCat].mPackageMasterDto.listTPackageItemDetailsDto.push(itemDtls);
														        	}
													          });
												 }else{
													 var itemList=[];
													 		angular.forEach(billingBedCat.mPackageMasterDto.listTPackageItemDetailsDto,function(itemDtls,indexItemDtlsDtls)
															  {		
													 			itemDtls.numberToBeUse = 1;
													 			itemList.push(itemDtls);														        	  
													          });
													 		tempPackgeObj.paymentEntitlement[indexPaymentEnt].patientCategoryList[indexPatientCat].billingBedCategoryList[indexBillingBedCat].mPackageMasterDto.listTPackageItemDetailsDto=itemList;
												 }
												 
											});//billingBedCategory			
											
										});//patientCategory
										
									});// payment entitlement end
								  $scope.packageObjResponse=tempPackgeObj;
								  console.log("pck obj", $scope.packageObjResponse);
								}
							$scope.calculatePackageAmounts(undefined,undefined);
							$scope.crateClone();
							$rootScope.stopLoader();
							 //enabling save button start
						  	/*$("#savePackageId").removeClass("disabled");
							$("#savePackageId").addClass("enabled");*/
							//enabling save button end
			});
		}
		
		$scope.removeItemWiseExclusion = function(index){
			$scope.itemWiseExclusionList.splice(index,1);
		}
		
		
		
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

	$scope.savePackage = function(fetch){

	  if($scope.packageObj.serviceId=="" || $scope.packageObj.serviceId==undefined){
    				growl.warning("Please select package service!",{title : 'Warning!'});
    				return false;
    			}
    			if($scope.selectedPaymentEntitlementList==undefined || $scope.selectedPaymentEntitlementList.length==0){
    				growl.warning("Please Select payment entitlement!",{title : 'Warning!'});
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
	if(angular.equals({}, $scope.packageObjResponse) ||
        ($scope.packageObjResponse.paymentEntitlement[0].patientCategoryList[0].billingBedCategoryList[0].mPackageMasterDto.listTPackageServicesDetailsDto==null
      && $scope.packageObjResponse.paymentEntitlement[0].patientCategoryList[0].billingBedCategoryList[0].mPackageMasterDto.listTPackageItemDetailsDto==null ))
        			{
        			    growl.warning("Please add data to package!",{title : 'Warning!'});
                        return false;
        			}

	 var flagFrValidityDays=false;
	 var flagFrPackagePrice=false;
	 var flagFrNoOfEncounters=false;
	 var flagFrpackageCost =false;
	 var flagFritemCost =false;


		angular.forEach($scope.packageObjResponse.paymentEntitlement,function(paymentEnt,indexPaymentEnt){
			
			angular.forEach(paymentEnt.patientCategoryList, function(patientCat,indexPatientCat){

				angular.forEach(patientCat.billingBedCategoryList,function(billingBedCat,indexBillingBedCat){
					
					 var packageCost =billingBedCat.mPackageMasterDto.packageCost;
					 var packagePrice =billingBedCat.mPackageMasterDto.packagePrice;
					 var validityPeriodIndays =billingBedCat.mPackageMasterDto.validityPeriodIndays;
					 var noOfEncounters =billingBedCat.mPackageMasterDto.noOfEncounters;
					 
						if(billingBedCat.mPackageMasterDto.listTPackageItemDetailsDto!=null && billingBedCat.mPackageMasterDto.listTPackageItemDetailsDto!=undefined)
						{
							 angular.forEach(billingBedCat.mPackageMasterDto.listTPackageItemDetailsDto,function(itemDtls,indexItemDtls){
									if(itemDtls.servicePrice==0 || itemDtls.servicePrice=='' || itemDtls.servicePrice==undefined || itemDtls.servicePrice==null){
										flagFritemCost =true;
									}
										
							});
						}
					
					 if(isNaN(Number(validityPeriodIndays))|| Number(validityPeriodIndays)==0){
						 flagFrValidityDays=true;
					 }
					 if(isNaN(Number(packagePrice))|| Number(packagePrice)==0){
						 flagFrPackagePrice=true;
					 }
					 if(isNaN(Number(noOfEncounters))|| Number(noOfEncounters)==0){
						 flagFrNoOfEncounters=true;	
					 }						
					 if(parseFloat(packageCost)>parseFloat(packagePrice)){
						  flagFrpackageCost=true;	
					}	
					
				});
			});
		});
		if(flagFritemCost){
			 growl.warning("Item wise amount should not be zero!",{title : 'Warning!'});
			 $scope.setFirstTabLevel(2);
			return false;
		}
		if(flagFrValidityDays){
			growl.warning("Package validity should not be zero!",{title : 'Warning!'});
			return false;
		}
		if(flagFrNoOfEncounters){
			growl.warning("No of encounter should not be zero!",{title : 'Warning!'});
			return false;
		}
		if(flagFrPackagePrice){
			growl.warning("Package price should not be zero!",{title : 'Warning!'});
			return false;
		}
	
		if(flagFrpackageCost){
			 growl.warning("Package price should not be less than package cost.",{title : 'Warning!'});
			return false;
		}
		$scope.packageObj.orgId = $scope.orgId;
		$scope.packageObj.unitId = $scope.unitId;
		$scope.packageObj.createdBy = $scope.userId;
		$scope.packageObj.updatedBy = $scope.userId;
		$scope.packageObj.packageTypeId = 5;
		$scope.packageObj.serviceId=$scope.packageObj.serviceId;
		$scope.packageObj.tariffId=$scope.packageObj.tariffId;
		$scope.packageObj.parentPackageId=$scope.parentPackageId;
        $scope.packageObj.packageName = $scope.packageService;
		$scope.packageObj.packageMasterDtoList=[];
		console.log("$scope.packageObj",$scope.packageObj);
		
		//pakages list
		if($scope.packageObj.sexId==="" || $scope.packageObj.sexId===null || $scope.packageObj.sexId===undefined)
		{
		  growl.warning("Please select gender.",{title : 'Warning!'});
		  return false;
		}

		angular.forEach($scope.packageObjResponse.paymentEntitlement,function(paymentEnt,indexPaymentEnt){
			
			angular.forEach(paymentEnt.patientCategoryList, function(patientCat,indexPatientCat){

				angular.forEach(patientCat.billingBedCategoryList,function(billingBedCat,indexBillingBedCat){
					$scope.tempListTPackageServicesDetailsDto=[];
					$scope.tempListTPackageItemsDetailsDto=[];
					$scope.packageMasterDtoList=[];
 								var tempPackageObj={};
 							tempPackageObj.packageTypeId=5;
							tempPackageObj.visitTypeId =billingBedCat.mPackageMasterDto.visitTypeId;
							tempPackageObj.patientTypeId=billingBedCat.mPackageMasterDto.patientTypeId;
							tempPackageObj.billingBedCategoryId=billingBedCat.mPackageMasterDto.billingBedCategoryId;
							tempPackageObj.paymentEntitlementTypeId=billingBedCat.mPackageMasterDto.paymentEntitlementTypeId;
							tempPackageObj.sexId=$scope.packageObj.sexId;
							tempPackageObj.packagePrice=billingBedCat.mPackageMasterDto.packagePrice;
							tempPackageObj.packageCost=billingBedCat.mPackageMasterDto.packageCost;
							tempPackageObj.orgId = $scope.orgId;
							tempPackageObj.unitId = $scope.unitId;
							tempPackageObj.tariffId=$scope.packageObj.tariffId;
							tempPackageObj.validityPeriodIndays=billingBedCat.mPackageMasterDto.validityPeriodIndays;
							tempPackageObj.noOfEncounters=billingBedCat.mPackageMasterDto.noOfEncounters;
							tempPackageObj.markupDownInPercentage=$scope.markupDownInPercentage;
							tempPackageObj.isMarkupDown=$scope.isMarkupDown;
							tempPackageObj.manualRoundoffAmount=billingBedCat.mPackageMasterDto.manualRoundoffAmount;
							
							if(billingBedCat.mPackageMasterDto.listTPackageServicesDetailsDto!=null && billingBedCat.mPackageMasterDto.listTPackageServicesDetailsDto!=undefined)
							{
								 angular.forEach(billingBedCat.mPackageMasterDto.listTPackageServicesDetailsDto,function(serviceDtls,indexServiceDtls){
										/*if(serviceDtls.isSelected!=undefined && serviceDtls.isSelected==true)	
											{*/
											
											var apportionedPrice = (parseFloat(serviceDtls.servicePrice) / 100) * parseFloat($scope.markupDownInPercentage);
											switch($scope.isMarkupDown){
											case "1":
												apportionedPrice = parseFloat(serviceDtls.servicePrice) + parseFloat(apportionedPrice);
												break;
											case "2":
												apportionedPrice = parseFloat(serviceDtls.servicePrice) - parseFloat(apportionedPrice);
												break;
											}
											  var newServiceObj={
																	apportionedPrice:apportionedPrice,
																	servicePrice : parseFloat(serviceDtls.servicePrice),
																	serviceId:serviceDtls.serviceId,												
																	specialityId:serviceDtls.specialityId,
																	subSpecialityId:serviceDtls.subSpecialityId,
																	isSelected:serviceDtls.isSelected,
																	consultationSpecialityId:serviceDtls.consultationSpecialityId,
																	isServiceItem:1,
																	numberToBeUse : serviceDtls.numberToBeUse,
																	isCap:1
															 }		
											   $scope.tempListTPackageServicesDetailsDto.push(newServiceObj);
//											}
								});
							}
							tempPackageObj.listTPackageServicesDetailsDto=$scope.tempListTPackageServicesDetailsDto;	
							
							if(billingBedCat.mPackageMasterDto.listTPackageItemDetailsDto!=null && billingBedCat.mPackageMasterDto.listTPackageItemDetailsDto!=undefined)
							{
								 angular.forEach(billingBedCat.mPackageMasterDto.listTPackageItemDetailsDto,function(itemDtls,indexItemDtls){
										var newItemObj={
															itemId:itemDtls.itemId,												
															numberToBeUse : itemDtls.numberToBeUse,
															servicePrice : itemDtls.servicePrice,
															isServiceItem:2,
														}		
											   $scope.tempListTPackageItemsDetailsDto.push(newItemObj);
											
								});
							}
							tempPackageObj.listTPackageItemDetailsDto=$scope.tempListTPackageItemsDetailsDto;
					  				
					$scope.packageObj.packageMasterDtoList.push(tempPackageObj);
					
				});//billingBedCategory
			});//patientCategory
		});// payment entitlement end
		console.log("$scope.packageObj",$scope.packageObj);
		if(fetch){
              $scope.editPackage($scope.packageObj);
            }
        else{
		$rootScope.startLoader();
		var URI = BASE_URL + ROOT_URL + "/api/packages/multiencounter";
		BillingGenericService.serviceAction(METHOD_POST,URI, $scope.packageObj,NOTIFICATION_MSG_STATUS_TRUE).then(function(response) {
			if (response.data.status == "success"){
				$scope.$broadcast('show-errors-reset');
			    $scope.packageObjResponse={};
			    tempPackgeObj={};
				$scope.init();
//				$("#divEnabled").removeClass("disabled");
//				$("#divEnabled").addClass("enabled");
//				$("#divEnabled2").removeClass("disabled");
//				$("#divEnabled2").addClass("enabled");
				 //enabling save button start
			  	/*$("#savePackageId").removeClass("disabled");
				$("#savePackageId").addClass("enabled");*/
				//enabling save button end
				$state.go('multicounterPackage');
				$rootScope.stopLoader();
			}
		});
	  }
	}
	
	// get package list
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
	
	//get selected package
	$scope.getPackageDetails=function(obj){
		$scope.packDetailsObj=obj;
	}
	
	// get selected packages services
	$scope.getSelectedPackageServices=function(importFrom){
		
		if(angular.equals({}, $scope.packDetailsObj) || $scope.packDetailsObj==undefined){
				growl.warning("Please Select package !",{title : 'Warning!'});
				return false;
				
		}
		$rootScope.startLoader();
		var paraentPckId=$scope.packDetailsObj.parentPackageId
		var data={};
		data[paraentPckId]=$scope.packDetailsObj.packageTypeId
		var visitTypeArray=[];
		console.log("request",data);
		var URI = BASE_URL + ROOT_URL + "/api/packages/getPackagesByPackageId";
		BillingGenericService.serviceAction(METHOD_POST,URI, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
			if (response.data.status == "success")
			{
			   $scope.selectedPaymentEntitlementList=[];
			   $scope.selectedPatientTypeList=[];
			   $scope.selectedBedBillingCategoryList=[];
			   $scope.selectedVisitTypeList=[];
			   $scope.packageObjResponse= response.data.object;
			   console.log("packageObjResponse",$scope.packageObjResponse);
			   $scope.packageObj.sexId = $scope.packageObjResponse.parentPackageMasterDto.sexId;
			   $scope.packageObj.tariffId  = $scope.packageObjResponse.parentPackageMasterDto.packageMasterDtoList[0].tariffId;
			   if(!importFrom){
                $scope.packageService = $scope.packageObjResponse.parentPackageMasterDto.packageName;
                $scope.packageObj.serviceId = $scope.packageObjResponse.parentPackageMasterDto.serviceId;
               }
			   tempPackgeObj= angular.copy($scope.packageObjResponse);
			   angular.forEach($scope.packageObjResponse.paymentEntitlement,function(paymentEnt,indexPaymentEnt){
					
					angular.forEach(paymentEnt.patientCategoryList, function(patientCat,indexPatientCat){

						angular.forEach(patientCat.billingBedCategoryList,function(billingBedCat,indexBillingBedCat){		
							//$scope.packageObj.tariffId = billingBedCat.mPackageMasterDto.tariffId;
							
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
			   
			  /* var uniqueVisitType=new Set(visitTypeArray);
			   $scope.selectedVisitTypeList = Array.from(uniqueVisitType);*/
			   console.log("$scope.selectedVisitTypeList",$scope.selectedVisitTypeList);
			   $scope.selectedVisitTypeList = $scope.packageObjResponse.selectedVisitTypeList;
               $scope.selectedPatientTypeList =  $scope.packageObjResponse.selectedPatientTypeList;
               $scope.selectedBedBillingCategoryList = $scope.packageObjResponse.selectedBedBillingCategoryList;
               $scope.selectedPaymentEntitlementList =  $scope.packageObjResponse.selectedPaymentEntitlementList;
               $scope.bedBillingCategoryList = $scope.packageObjResponse.selectedBedBillingCategoryList;
               $scope.fetchValidate = false;
			   $scope.calculatePackageAmounts(undefined,undefined);
			   $scope.crateClone();
			   $rootScope.stopLoader();
				 //enabling save button start
			  /*	$("#savePackageId").removeClass("disabled");
				$("#savePackageId").addClass("enabled");*/
				//enabling save button end
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
		/*$("#divEnabled").removeClass("disabled");
		$("#divEnabled").addClass("enabled");
		$("#divEnabled2").removeClass("disabled");
		$("#divEnabled2").addClass("enabled");*/
		 //enabling save button start
	  	/*$("#savePackageId").removeClass("enabled");
		$("#savePackageId").addClass("disabled");*/
		//enabling save button end
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
//	First Level
	$scope.firstTabLevel = 1 ;
	
	$scope.setFirstTabLevel= function(newTab)
	{
		$scope.firstTabLevel = newTab;
	};
	
	$scope.isSetFirstTabLevel = function(tabNum)
	{
		return $scope.firstTabLevel === tabNum;
	};
	
	//removing details
	$scope.removeService = function(serviceId,index)
	{
		angular.forEach($scope.packageObjResponse.paymentEntitlement,function(paymentEnt,indexPaymentEnt)
			{
			angular.forEach(paymentEnt.patientCategoryList, function(patientCat,indexPatientCat){
				angular.forEach(patientCat.billingBedCategoryList,function(billingBedCat,indexBillingBedCat)
						{								
						  angular.forEach(billingBedCat.mPackageMasterDto.listTPackageServicesDetailsDto,function(serviceDtls,indexServiceDtls)
								  {
							      if(serviceDtls.serviceId==serviceId && indexServiceDtls==index)
							    	 {
							    	   billingBedCat.mPackageMasterDto.listTPackageServicesDetailsDto.splice(indexServiceDtls, 1);  
							    	 }
						          });
				      });	
			   });
		 });
		tempPackgeObj= angular.copy($scope.packageObjResponse);
		$scope.calculatePackageAmounts(serviceId,index);
	}
	$scope.removeItems = function(itemId)
	{
		angular.forEach($scope.packageObjResponse.paymentEntitlement,function(paymentEnt,indexPaymentEnt)
			{
			angular.forEach(paymentEnt.patientCategoryList, function(patientCat,indexPatientCat){
				angular.forEach(patientCat.billingBedCategoryList,function(billingBedCat,indexBillingBedCat)  
						{								
						  angular.forEach(billingBedCat.mPackageMasterDto.listTPackageItemDetailsDto,function(itemDtls,indexitemDtls)
								  {
							      if(itemDtls.itemId==itemId)
							    	 {
							    	   billingBedCat.mPackageMasterDto.listTPackageItemDetailsDto.splice(indexitemDtls, 1);  
							    	 }
						          });
				      });	
			   });
		 });
		tempPackgeObj= angular.copy($scope.packageObjResponse);
		$scope.calculatePackageAmounts(undefined,itemId);
	}
	
	//get consultation dept by service id  conServiceId
	$scope.getDepartmentBySpeacialityId=function(serviceId,index)
	{
		
		$scope.specialityId="";
		console.log("ff"+$scope.packageObjResponse.paymentEntitlement[0].patientCategoryList[0].billingBedCategoryList[0].mPackageMasterDto.listTPackageServicesDetailsDto[index].specialityId);
		$scope.consultationServices=[];
			$rootScope.startLoaderPopup();
			var URI = BASE_URL + ROOT_URL + "/billing/service/getConsultationDeptByServiceId/"+0+"/"+$scope.unitId;
			return BillingGenericService.serviceAction(METHOD_GET,URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
				if(response.data.status == "success"){
					$scope.consultationServices = response.data.listObject;
					$scope.serviceId=serviceId;
					$scope.index=index;
					angular.forEach($scope.packageObjResponse.paymentEntitlement,function(paymentEnt,indexPaymentEnt)
							{
								angular.forEach(paymentEnt.patientCategoryList, function(patientCat,indexPatientCat){
									angular.forEach(patientCat.billingBedCategoryList,function(billingBedCat,indexBillingBedCat)
											{								
										  angular.forEach(billingBedCat.mPackageMasterDto.listTPackageServicesDetailsDto,function(serviceDtls,indexServiceDtls)
												  {
											           if(serviceDtls.serviceId==serviceId && indexServiceDtls==index)
											        	   {
											        	        if(serviceDtls.consultationSpecialityId != null)
											        	   		$scope.specialityId  = serviceDtls.consultationSpecialityId.toString();
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
	
	$scope.getSelectedConsultSpeciality=function(specialityId){
		if(specialityId!=undefined && specialityId!="")
		{
			angular.forEach($scope.packageObjResponse.paymentEntitlement,function(paymentEnt,indexPaymentEnt)
					{
					angular.forEach(paymentEnt.patientCategoryList, function(patientCat,indexPatientCat){
						angular.forEach(patientCat.billingBedCategoryList,function(billingBedCat,indexBillingBedCat)
								{								
							  angular.forEach(billingBedCat.mPackageMasterDto.listTPackageServicesDetailsDto,function(serviceDtls,indexServiceDtls)
									  {
								           if(serviceDtls.serviceId==$scope.serviceId && indexServiceDtls==$scope.index)
								        	   {
								        	   		serviceDtls.consultationSpecialityId=specialityId
								        	   }
							          });
						      });	
					  });
				});
		}
	 }
	// check packages price
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

	//	------------------edit package-------------------
             if($stateParams.newMulticounterPackageObj!=undefined && $stateParams.newMulticounterPackageObj!=null && $stateParams.newMulticounterPackageObj.parentPackageId>0){
              		            $scope.packDetailsObj = {};
                                $scope.packDetailsObj.parentPackageId = $stateParams.newMulticounterPackageObj.parentPackageId;
                                $scope.parentPackageId = $stateParams.newMulticounterPackageObj.parentPackageId;
                                $scope.packDetailsObj.packageTypeId = 5;
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
                         					packagetypeId:5,orgId:$scope.orgId,unitId:$scope.unitId,
                         				};

                         			var tempArr=arraList[i];

                         			$scope.paymentEntitlementId =tempArr[0].id;
                         			$scope.patientTypeId =tempArr[1].id;
                         //			$scope.visitTypeId =tempArr[2].id;
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

                         				console.log(JSON.stringify(newTempObj))
                         			 $scope.addNewServiceList.push(newTempObj);
                         		  } //IF
                         		} //FOr
                        packageObjectFetch.serviceForPackageReqList=$scope.addNewServiceList;
              			var URI = BASE_URL + ROOT_URL + "/api/packages/editPackage";
              			BillingGenericService.serviceAction(METHOD_POST,URI, packageObjectFetch,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
              				if (response.data.status == "success")
              				{
              				   /* $("#savePackageId").removeClass("disabled");
                                $("#savePackageId").addClass("enabled");*/
              					$scope.selectedPaymentEntitlementList=[];
              					$scope.selectedPatientTypeList=[];
              				    $scope.packageObjResponse=  response.data.object==null?{}:response.data.object;
              				    $scope.packageObj.sexId   = $scope.packageObjResponse.parentPackageMasterDto.sexId;
              				    $scope.packageObj.minAge  = $scope.packageObjResponse.parentPackageMasterDto.minAge;
              				    $scope.packageObj.maxAge  = $scope.packageObjResponse.parentPackageMasterDto.maxAge;
              				    $scope.packageObj.packageTypeId  = $scope.packageObjResponse.parentPackageMasterDto.packageTypeId;
              				    $scope.packageObj.tariffId  = $scope.packageObjResponse.parentPackageMasterDto.packageMasterDtoList[0].tariffId;

              				     tempPackgeObj= angular.copy($scope.packageObjResponse);
                                		    angular.forEach($scope.packageObjResponse.paymentEntitlement,function(paymentEnt,indexPaymentEnt){

                                           					angular.forEach(paymentEnt.patientCategoryList, function(patientCat,indexPatientCat){

                                           						angular.forEach(patientCat.billingBedCategoryList,function(billingBedCat,indexBillingBedCat){
                                           							//$scope.packageObj.tariffId = billingBedCat.mPackageMasterDto.tariffId;

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

                                           			  /* var uniqueVisitType=new Set(visitTypeArray);
                                           			   $scope.selectedVisitTypeList = Array.from(uniqueVisitType);*/
                                           			   console.log("$scope.selectedVisitTypeList",$scope.selectedVisitTypeList);
                                           			   $scope.selectedVisitTypeList = $scope.packageObjResponse.selectedVisitTypeList;
                                                       $scope.selectedPatientTypeList =  $scope.packageObjResponse.selectedPatientTypeList;
                                                       $scope.selectedBedBillingCategoryList = $scope.packageObjResponse.selectedBedBillingCategoryList;
                                                       $scope.selectedPaymentEntitlementList =  $scope.packageObjResponse.selectedPaymentEntitlementList;
                                                       $scope.bedBillingCategoryList = $scope.packageObjResponse.selectedBedBillingCategoryList;
                                                       $scope.fetchValidate = false;
                                           			   $scope.calculatePackageAmounts(undefined,undefined);
                                           			   $scope.crateClone();
                                           			   $rootScope.stopLoader();
                                           				 //enabling save button start
                                           			  /*	$("#savePackageId").removeClass("disabled");
                                           				$("#savePackageId").addClass("enabled");*/
                                           				//enabling save button end
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
                         if(!angular.equals({}, $scope.packageObjResponse) && $scope.packageObjResponse.paymentEntitlement[0].patientCategoryList[0].billingBedCategoryList[0].mPackageMasterDto.listTPackageServicesDetailsDto
                                 !=null && $scope.packageObjResponse.paymentEntitlement[0].patientCategoryList[0].billingBedCategoryList[0].mPackageMasterDto.listTPackageServicesDetailsDto.length>0)
                                 {
                                    validate = true;
                                 }
                         if(!angular.equals({}, $scope.packageObjResponse) && $scope.packageObjResponse.paymentEntitlement[0].patientCategoryList[0].billingBedCategoryList[0].mPackageMasterDto.listTPackageItemDetailsDto
                                 !=null && $scope.packageObjResponse.paymentEntitlement[0].patientCategoryList[0].billingBedCategoryList[0].mPackageMasterDto.listTPackageItemDetailsDto.length>0)
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
                    $scope.crateClone = function()
                    {
                          $scope.sexIdClone   =  $scope.packageObj.sexId;
                          $scope.tariffIdClone  =  $scope.packageObj.tariffId;
                          $scope.selectedVisitTypeListClone =  angular.copy($scope.selectedVisitTypeList);
                          $scope.selectedPatientTypeListClone =  angular.copy($scope.selectedPatientTypeList);
                          $scope.selectedBedBillingCategoryListClone = angular.copy($scope.selectedBedBillingCategoryList);
                          $scope.selectedPaymentEntitlementListClone =  angular.copy($scope.selectedPaymentEntitlementList);
                    }
}]);