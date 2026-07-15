/**
 * Santosh
 * 04-06-2018
 */

angular.module("myApp").controller("addNewExclusiveController", [ '$scope','$rootScope','BillingGenericService','$cookies','$state','growl','servicesFactory','GenericService','itemFactory','$stateParams',
  function($scope,$rootScope,BillingGenericService,$cookies,$state,growl,servicesFactory,GenericService,itemFactory,$stateParams){
	try{
		$rootScope.loginpage = true;
		$scope.angular = angular;
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
		
		$scope.initSubSpeciality = function(){
			 $scope.subSpeciality = {subSpecialityName:"",subSpecialityId:""};
		}
		$scope.initSpeciality = function(){
			 $scope.speciality = {specialityName:"",specialityId:""};
		}
		
		$scope.init = function(){
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
			itemFactory.initItemFactory($scope);
			servicesFactory.initSpecailityGroupSearch($scope);
             $scope.parentPackageId=null;
			$scope.billingCategoryReference =[];
			//selected List
			$scope.selectedPatientTypeList=[];
			$scope.selectedVisitTypeList=[];
			$scope.packageObjResponse ={};
			$scope.selectedPaymentEntitlementList=[];
			$scope.selectedBedBillingCategoryList=[];
			$scope.initSubSpeciality();
			$scope.initSpeciality();
		}
		
		
		
		$scope.opdBillingCatId = cookieObjectAppConfig.opdBillingCatId;
		$scope.initPackageObj = function(){
			$scope.packageObj = {packageTypeId:"4",sexId:"",minAge:"",maxAge:"",tariffId:$scope.defaultSelfTariffId,serviceId:""}
		}
		$scope.initImportPackageObj = function(){
			$scope.importPkgObj = {
							packageTypeId:"4",
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
							organizationId:$scope.organizationId,
							visitTypeListIds : visitTypeId
					}
					
					$scope.selectedBillingCategory = [];
					var URI = BASE_URL+ ROOT_URL+ "/adt/getBillingBedCategoryByVistTypeIdMultiple";
				GenericService.serviceAction("POST", URI, data)
						.then(function(response) {
									if (response.data.status == "success") {
										/*$scope.bedBillingCategoryListReference = [];
										$scope.bedBillingCategoryListReference = response.data.listObject;*/
									/*	$scope.bedBillingCategoryList = [];
										$scope.billingCategoryReference = response.data.listObject;
										angular.forEach(response.data.listObject,
														function(value,index) {
															var data = {};
															data.id = value.billingBedCategoryId;
															data.label = value.billingBedDesc;
															$scope.bedBillingCategoryList.push(data);
														});*/
										
										$scope.billingCategoryReference = [];
										$scope.bedBillingCategoryList = [];
										$scope.billingCategoryReference = response.data.listObject;
										angular.forEach(response.data.listObject,
														function(value,index) {
															var data = {};
															data.id = value.billingBedCategoryId;
															data.label = value.billingBedDesc;
															var found = $scope.bedBillingCategoryList.find(ar=>ar.id==value.billingBedCategoryId);
															   if(found==undefined)
																   {
																      $scope.bedBillingCategoryList.push(data);
																   }
														});
									}
								});
						},
					onSelectionChanged: function() {
                                                   $scope.validateFetch(false);
                                             }
			}
		
		
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
				$scope.paymentEntitlementList= response.data.listObject;
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
		
		$scope.getPackageTypeList($scope.orgId,$scope.unitId);
//		$scope.getTariffList($scope.orgId,$scope.unitId);
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
		// select speciality 
		 $scope.selectSpeciality = function($item, $model,$label)
		  {
		       $scope.speciality.specialityId = $item.specialityId;
		       if($item.specialityId != ""){
		    	   $scope.initSubSpeciality();
		    	   $scope.subSpecialityList = [];
		       var URI = BASE_URL + ROOT_URL + "/adt/subspeciality/orgId/"+$scope.orgId+"/unitId/"+$scope.unitId+"/specialityId/"+$item.specialityId; 
				BillingGenericService.serviceAction(METHOD_GET,URI, null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					if (response.data.status == "success")
						$scope.subSpecialityList = response.data.listObject;
				});
		       }
		  }
		// select sub speciality 
		  $scope.selectSubSpeciality = function($item, $model,$label){
			  $scope.subSpeciality.subSpecialityId = $item.subSpecialityMasterId;
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
				var bedBillingCategoryName = $.grep($scope.bedBillingCategoryListReference, function (billBed) {
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
	
	
	 $scope.calculatePackageAmounts = function()
		{
			angular.forEach($scope.packageObjResponse.paymentEntitlement,function(paymentEnt,indexPaymentEnt)
				{
				angular.forEach(paymentEnt.patientCategoryList, function(patientCat,indexPatientCat){
					angular.forEach(patientCat.billingBedCategoryList,function(billingBedCat,indexBillingBedCat)
							{								
						     var total = 0;
						     billingBedCat.mPackageMasterDto.packagePrice = $rootScope.parseFloatNum(0);
							 angular.forEach(billingBedCat.mPackageMasterDto.listTPackageIncExcServiceDetailsDto,function(serviceDtls,indexServiceDtls)
									  {
								 			if(serviceDtls.serviceId!=undefined && serviceDtls.serviceId!=null)
								 			    serviceDtls.isGroupwiseExlusion='1';
							          });
							 /*
							    billingBedCat.mPackageMasterDto.serviceWiseSummary = $rootScope.parseFloatNum(total);
							    billingBedCat.mPackageMasterDto.packageCost = $rootScope.parseFloatNum(total);
								billingBedCat.mPackageMasterDto.packagePrice = $rootScope.parseFloatNum(total);	*/
								billingBedCat.mPackageMasterDto.validityPeriodIndays = (parseInt(billingBedCat.mPackageMasterDto.validityPeriodIndays)<0 || parseInt(billingBedCat.mPackageMasterDto.validityPeriodIndays)==0) ? 1 : billingBedCat.mPackageMasterDto.validityPeriodIndays ;
							
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
					packagetypeId:4,orgId:$scope.orgId,unitId:$scope.unitId,
				};
			
			var tempArr=arraList[i];
			
			$scope.paymentEntitlementId =tempArr[0].id;
			$scope.patientTypeId =tempArr[1].id;
//			$scope.visitTypeId =tempArr[2].id;
			$scope.bedBillingCategoryId =tempArr[2].id;
			//get visit type id by billing bed category id
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
				
				newTempObj.tariffId=$scope.defaultSelfTariffId;
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
																			
												 var tempserviceList = tempPackgeObj.paymentEntitlement[indexPaymentEnt].patientCategoryList[indexPatientCat].billingBedCategoryList[indexBillingBedCat].mPackageMasterDto.listTPackageIncExcServiceDetailsDto
												 if(tempserviceList!=undefined && tempserviceList!=null){
												  angular.forEach(billingBedCat.mPackageMasterDto.listTPackageIncExcServiceDetailsDto,function(serviceDtls,indexServiceDtls)
														  {
													  		  serviceDtls.isGroupwiseExlusion='1';
															  var found = tempserviceList.find(function(service) { 
																  return service.serviceId == serviceDtls.serviceId; 
																});
													        if(found==undefined || found.length==0)
													        	{
													        	  tempPackgeObj.paymentEntitlement[indexPaymentEnt].patientCategoryList[indexPatientCat].billingBedCategoryList[indexBillingBedCat].mPackageMasterDto.listTPackageIncExcServiceDetailsDto.push(serviceDtls);
													        	}
												          });
											}else{
												var serviceList=[];
												angular.forEach(billingBedCat.mPackageMasterDto.listTPackageIncExcServiceDetailsDto,function(serviceDtls,indexServiceDtls)
												{
													serviceDtls.isGroupwiseExlusion='1';				
													serviceList.push(serviceDtls);
													        	
												  });
												 tempPackgeObj.paymentEntitlement[indexPaymentEnt].patientCategoryList[indexPatientCat].billingBedCategoryList[indexBillingBedCat].mPackageMasterDto.listTPackageIncExcServiceDetailsDto=serviceList;
											}
											
										});//billingBedCategory			
										
									});//patientCategory
									
								});// payment entitlement end
							  
							  $scope.packageObjResponse=tempPackgeObj;
							  console.log("pck obj", $scope.packageObjResponse);
							  //enabling save button start
//							  	$("#savePackageId").removeClass("disabled");
//								$("#savePackageId").addClass("enabled");
								//enabling save button end

							}
						$scope.calculatePackageAmounts();
                        $scope.crateClone();
						$rootScope.stopLoader();
		});
	}
	
	$scope.removeSelectedService = function(index){
		$scope.$scope.selectedServiceIds.splice(index,1);
		$scope.selectedServiceIds.splice(index,1);
	
	}
	//get Items
		$scope.getItems=function(){
			
			$scope.searchItemList = [];
			$scope.searchServObj = {};
			
			  // validation
			if($scope.packageObj.serviceId=="" || $scope.packageObj.serviceId==undefined){
				growl.warning("Please select package service!",{title : 'Warning!'});
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
		
//			$("#divEnabled").removeClass("enabled");
//			$("#divEnabled").addClass("disabled");
//
//			$("#divEnabled2").removeClass("enabled");
//			$("#divEnabled2").addClass("disabled");
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
						packagetypeId:4,orgId:$scope.orgId,unitId:$scope.unitId,
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
					
					newTempObj.tariffId=$scope.defaultSelfTariffId;
					
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
												 var tempItemList = tempPackgeObj.paymentEntitlement[indexPaymentEnt].patientCategoryList[indexPatientCat].billingBedCategoryList[indexBillingBedCat].mPackageMasterDto.listTPackageIncExcItemDetailsDto;
												 if(tempItemList!=undefined && tempItemList!=null)
												 {
													 angular.forEach(billingBedCat.mPackageMasterDto.listTPackageIncExcItemDetailsDto,function(itemDtls,indexItemDtlsDtls)
															  {
																  var found = tempItemList.find(function(item) {
																	  return item.itemId == itemDtls.serviceId;
																	});
														        if(found==undefined || found.length==0)
														        	{
														        	  tempPackgeObj.paymentEntitlement[indexPaymentEnt].patientCategoryList[indexPatientCat].billingBedCategoryList[indexBillingBedCat].mPackageMasterDto.listTPackageIncExcItemDetailsDto.push(itemDtls);
														        	}
													          });
												 }else{
													 var itemList=[];
													 		angular.forEach(billingBedCat.mPackageMasterDto.listTPackageIncExcItemDetailsDto,function(itemDtls,indexItemDtlsDtls)
															  {
													 			itemList.push(itemDtls);
													          });
													 		tempPackgeObj.paymentEntitlement[indexPaymentEnt].patientCategoryList[indexPatientCat].billingBedCategoryList[indexBillingBedCat].mPackageMasterDto.listTPackageIncExcItemDetailsDto=itemList;
												 }
												 
											});//billingBedCategory			
											
										});//patientCategory
										
									});// payment entitlement end
								  $scope.packageObjResponse=tempPackgeObj;
								  console.log("pck obj", $scope.packageObjResponse);
								  //enabling save button start
//								  	$("#savePackageId").removeClass("disabled");
//									$("#savePackageId").addClass("enabled");
									//enabling save button end
								}
							$scope.calculatePackageAmounts();
							$scope.crateClone();
							$rootScope.stopLoader();
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
		 var flagFrValidityDays=false;
		 var flagFrPackagePrice=false;


		 if($scope.packageObj.serviceId=="" || $scope.packageObj.serviceId==undefined){
        				growl.warning("Please select package service!",{title : 'Warning!'});
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

      if(angular.equals({}, $scope.packageObjResponse) ||(
      $scope.packageObjResponse.paymentEntitlement[0].patientCategoryList[0].billingBedCategoryList[0].mPackageMasterDto.listTPackageIncExcServiceDetailsDto==null
	  && $scope.packageObjResponse.paymentEntitlement[0].patientCategoryList[0].billingBedCategoryList[0].mPackageMasterDto.listTPackageIncExcItemDetailsDto==null))
			{
			  growl.warning("Please add data to package!",{title : 'Warning!'});
			  return false;
			}

if(angular.equals({}, $scope.packageObjResponse) ||(
      $scope.packageObjResponse.paymentEntitlement[0].patientCategoryList[0].billingBedCategoryList[0].mPackageMasterDto.listTPackageIncExcServiceDetailsDto==null
	  && $scope.packageObjResponse.paymentEntitlement[0].patientCategoryList[0].billingBedCategoryList[0].mPackageMasterDto.listTPackageIncExcItemDetailsDto==null
	   ))
			{
			  growl.warning("Please add data to package!",{title : 'Warning!'});
			  return false;
			}

     angular.forEach($scope.packageObjResponse.paymentEntitlement,function(paymentEnt,indexPaymentEnt){
			
			angular.forEach(paymentEnt.patientCategoryList, function(patientCat,indexPatientCat){

				angular.forEach(patientCat.billingBedCategoryList,function(billingBedCat,indexBillingBedCat){
					
					 var packagePrice =billingBedCat.mPackageMasterDto.packagePrice;
					 var validityPeriodIndays =billingBedCat.mPackageMasterDto.validityPeriodIndays;
					 
					 if(isNaN(Number(validityPeriodIndays))|| Number(validityPeriodIndays)==0){
						 flagFrValidityDays=true;
					 }
					 if(isNaN(Number(packagePrice))|| Number(packagePrice)==0){
						 flagFrPackagePrice=true;
					 }										
					 	
				});
			});
		});
     
 
     
		if(flagFrValidityDays && !fetch){
			growl.warning("Package validity should not be zero!",{title : 'Warning!'});
			return false;
		}
		if(flagFrPackagePrice && !fetch){
			growl.warning("Package price should not be zero!",{title : 'Warning!'});
			return false;
		}
		/*if(flagFrpackageCost){
			growl.warning("Package price should not be less than package cost.",{title : 'Warning!'});
			return false;
		}*/
		
		$scope.packageObj.orgId = $scope.orgId;
		$scope.packageObj.unitId = $scope.unitId;
		$scope.packageObj.createdBy = $scope.userId;
		$scope.packageObj.updatedBy = $scope.userId;
		$scope.packageObj.packageTypeId = 4;
		$scope.packageObj.serviceId=$scope.packageObj.serviceId;
		$scope.packageObj.tariffId=$scope.defaultSelfTariffId;
		$scope.packageObj.parentPackageId=$scope.parentPackageId;
		$scope.packageObj.packageMasterDtoList=[];
		$scope.packageObj.packageName =$scope.packageService;
		console.log("$scope.packageObj",$scope.packageObj);

		//pakages list
		
		if( $scope.packageObj.sexId==="" || $scope.packageObj.sexId===null || $scope.packageObj.sexId===undefined)
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
 							tempPackageObj.packageTypeId	=4;
							tempPackageObj.visitTypeId          =billingBedCat.mPackageMasterDto.visitTypeId;
							tempPackageObj.patientTypeId           =billingBedCat.mPackageMasterDto.patientTypeId;
							tempPackageObj.billingBedCategoryId   =billingBedCat.mPackageMasterDto.billingBedCategoryId;
							tempPackageObj.paymentEntitlementTypeId=billingBedCat.mPackageMasterDto.paymentEntitlementTypeId;
							tempPackageObj.sexId=$scope.packageObj.sexId;
							tempPackageObj.packagePrice=billingBedCat.mPackageMasterDto.packagePrice;
							tempPackageObj.validityPeriodIndays=billingBedCat.mPackageMasterDto.validityPeriodIndays;
							tempPackageObj.orgId = $scope.orgId;
							tempPackageObj.unitId = $scope.unitId;
							tempPackageObj.tariffId=$scope.packageObj.tariffId;
							if(billingBedCat.mPackageMasterDto.listTPackageIncExcServiceDetailsDto!=null && billingBedCat.mPackageMasterDto.listTPackageIncExcServiceDetailsDto!=undefined)
							{
								 angular.forEach(billingBedCat.mPackageMasterDto.listTPackageIncExcServiceDetailsDto,function(serviceDtls,indexServiceDtls){
//										if(serviceDtls.isSelected!=undefined && serviceDtls.isSelected==true)	
											{
											  var newServiceObj={
																	apportionedPrice:serviceDtls.apportionedPrice!=undefined && serviceDtls.apportionedPrice!=null ? serviceDtls.apportionedPrice : 0,
																	servicePrice:serviceDtls.servicePrice !=undefined && serviceDtls.servicePrice!=null  ? serviceDtls.servicePrice : 0 ,
																	serviceId:serviceDtls.serviceId ,												
																	specialityId:serviceDtls.specialityId,
																	subSpecialityId:serviceDtls.subSpecialityId,
																	isExcluded : serviceDtls.isSelected!=undefined && serviceDtls.isSelected==true ? 0 : 1,
																	isServiceItem:1,
																	isInclusiveService:2,
																	isGroupwiseExlusion:serviceDtls.isGroupwiseExlusion,
																	specialityName:serviceDtls.specialityName,
																	subSpecialityName:serviceDtls.subSpecialityName
															 }		
											   $scope.tempListTPackageServicesDetailsDto.push(newServiceObj);
											}
								});
							}
							tempPackageObj.listTPackageIncExcServiceDetailsDto=$scope.tempListTPackageServicesDetailsDto;	
							
							if(billingBedCat.mPackageMasterDto.listTPackageIncExcItemDetailsDto!=null && billingBedCat.mPackageMasterDto.listTPackageIncExcItemDetailsDto!=undefined)
							{
								 angular.forEach(billingBedCat.mPackageMasterDto.listTPackageIncExcItemDetailsDto,function(itemDtls,indexItemDtls){
//										if(itemDtls.isSelected!=undefined && itemDtls.isSelected==true)	
											{
											  var newItemObj={
																	itemId:itemDtls.itemId,												
																	isSelected:itemDtls.isSelected,
																	isServiceItem:2,
																	isInclusiveService:2,
																	isExcluded : itemDtls.isSelected!=undefined && itemDtls.isSelected==true ? 0 : 1,
															 }		
											   $scope.tempListTPackageItemsDetailsDto.push(newItemObj);
											}
								});
							}
							tempPackageObj.listTPackageIncExcItemDetailsDto=$scope.tempListTPackageItemsDetailsDto;
					  				
					$scope.packageObj.packageMasterDtoList.push(tempPackageObj);
					
				});//billingBedCategory			
				
				
				
			});//patientCategory

			
		});// payment entitlement end
		if(fetch){
                     $scope.editPackage($scope.packageObj);
        		}
        else{
		console.log("$scope.packageObj",$scope.packageObj);
		$rootScope.startLoader();
		var URI = BASE_URL + ROOT_URL + "/api/packages/allInclusive";
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
//			  	$("#savePackageId").removeClass("enabled");
//				$("#savePackageId").addClass("disabled");
				//enabling save button end
				$state.go('allExclusivePackage');
				$rootScope.stopLoader();
			}else{
				 //enabling save button start
//			  	$("#savePackageId").removeClass("enabled");
//				$("#savePackageId").addClass("disabled");
				//enabling save button end
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
			    $scope.packageObjResponse= response.data.object==null?{}:response.data.object;
			   console.log("packageObjResponse",$scope.packageObjResponse);
			   $scope.packageObj.sexId = $scope.packageObjResponse.parentPackageMasterDto.sexId;
//			   tempPackgeObj= angular.copy($scope.packageObjResponse);
               if(!importFrom){
			     $scope.packageService = $scope.packageObjResponse.parentPackageMasterDto.packageName;
                 $scope.packageObj.serviceId = $scope.packageObjResponse.parentPackageMasterDto.serviceId;
               }
			   angular.forEach($scope.packageObjResponse.paymentEntitlement,function(paymentEnt,indexPaymentEnt){
					
					angular.forEach(paymentEnt.patientCategoryList, function(patientCat,indexPatientCat){

						angular.forEach(patientCat.billingBedCategoryList,function(billingBedCat,indexBillingBedCat){		
							
							visitTypeArray.push(billingBedCat.mPackageMasterDto.visitTypeId);
							
							var billingBedCategoryData={
									id:billingBedCat.billingBedCategoryId,
									label:billingBedCat.billingBedCategory
							}
							$scope.selectedBedBillingCategoryList.push(billingBedCategoryData);
							
							// for adding groupwise exclusion start 
							var i = 0;
							angular.forEach(billingBedCat.mPackageMasterDto.listTPackageIncExcServiceDetailsDto,function(serviceDtls,indexServiceDtls)
									  {
								  		  if(serviceDtls.isGroupwiseExlusion!=undefined && serviceDtls.isGroupwiseExlusion!=null && serviceDtls.isGroupwiseExlusion==2){
								  			billingBedCat.mPackageMasterDto.listTPackageIncExcServiceDetailsDto[i].groupName = serviceDtls.specialityName;
								  			billingBedCat.mPackageMasterDto.listTPackageIncExcServiceDetailsDto[i].subGroupName = serviceDtls.subSpecialityName;
								  			
								  		  }
								  		 billingBedCat.mPackageMasterDto.listTPackageIncExcServiceDetailsDto[i].isSelected=true;
								  		  i++;
							          });
							
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
			   tempPackgeObj= angular.copy($scope.packageObjResponse);
			   var uniqueVisitType=new Set(visitTypeArray);
			   $scope.selectedVisitTypeList = Array.from(uniqueVisitType);
			   $scope.selectedVisitTypeList = $scope.packageObjResponse.selectedVisitTypeList;
               $scope.selectedPatientTypeList =  $scope.packageObjResponse.selectedPatientTypeList;
               $scope.selectedBedBillingCategoryList = $scope.packageObjResponse.selectedBedBillingCategoryList;
               $scope.selectedPaymentEntitlementList =  $scope.packageObjResponse.selectedPaymentEntitlementList;
               $scope.bedBillingCategoryList = $scope.packageObjResponse.selectedBedBillingCategoryList;
               $scope.fetchValidate = false;
			   console.log("$scope.selectedVisitTypeList",$scope.selectedVisitTypeList);
			   $scope.crateClone();

			   //$scope.calculatePackageAmounts();
				$rootScope.stopLoader();
				 //enabling save button start
//			  	$("#savePackageId").removeClass("disabled");
//				$("#savePackageId").addClass("enabled");
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
        $scope.sexIdClone=undefined;
	/*
		$("#divEnabled").removeClass("disabled");
		$("#divEnabled").addClass("enabled");
		$("#divEnabled2").removeClass("disabled");
		$("#divEnabled2").addClass("enabled");*/
		
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
//		$("#divEnabled").removeClass("enabled");
//		$("#divEnabled").addClass("disabled");
//		$("#divEnabled2").removeClass("enabled");
//		$("#divEnabled2").addClass("disabled");
		
	}	
	
	// remove data from list
	$scope.removeItems = function(itemId)
	{
		angular.forEach($scope.packageObjResponse.paymentEntitlement,function(paymentEnt,indexPaymentEnt)
			{
			angular.forEach(paymentEnt.patientCategoryList, function(patientCat,indexPatientCat){
				angular.forEach(patientCat.billingBedCategoryList,function(billingBedCat,indexBillingBedCat)
						{								
						  angular.forEach(billingBedCat.mPackageMasterDto.listTPackageIncExcItemDetailsDto,function(itemDtls,indexitemDtls)
								  {
								      if(itemDtls.itemId==itemId)
								    	 {
								    	   billingBedCat.mPackageMasterDto.listTPackageIncExcItemDetailsDto.splice(indexitemDtls, 1);  
								    	 }
						          });
				      });	
			   });
		 });
		tempPackgeObj= angular.copy($scope.packageObjResponse);
		$scope.calculatePackageAmounts();
	}
	
	$scope.removeService = function(serviceId)
	{
		angular.forEach($scope.packageObjResponse.paymentEntitlement,function(paymentEnt,indexPaymentEnt)
			{
			angular.forEach(paymentEnt.patientCategoryList, function(patientCat,indexPatientCat){
				angular.forEach(patientCat.billingBedCategoryList,function(billingBedCat,indexBillingBedCat)
						{								
						  angular.forEach(billingBedCat.mPackageMasterDto.listTPackageIncExcServiceDetailsDto,function(serviceDtls,indexServiceDtls)
								  {
							      if(serviceDtls.serviceId==serviceId)
							    	 {
							    	   billingBedCat.mPackageMasterDto.listTPackageIncExcServiceDetailsDto.splice(indexServiceDtls, 1);  
							    	 }
						          });
				      });	
			   });
		 });
		tempPackgeObj= angular.copy($scope.packageObjResponse);
		$scope.calculatePackageAmounts();
	}
	$scope.removeGroups = function(index)
	{
		angular.forEach($scope.packageObjResponse.paymentEntitlement,function(paymentEnt,indexPaymentEnt)
			{
			angular.forEach(paymentEnt.patientCategoryList, function(patientCat,indexPatientCat){
				angular.forEach(patientCat.billingBedCategoryList,function(billingBedCat,indexBillingBedCat)
						{								
						  angular.forEach(billingBedCat.mPackageMasterDto.listTPackageIncExcServiceDetailsDto,function(serviceDtls,indexServiceDtls)
								  {
							      if(serviceDtls.isGroupwiseExlusion==2 && indexServiceDtls==index)
							    	 {
							    	   billingBedCat.mPackageMasterDto.listTPackageIncExcServiceDetailsDto.splice(indexServiceDtls, 1);  
							    	 }
						          });
				      });	
			   });
		 });
		tempPackgeObj= angular.copy($scope.packageObjResponse);
		$scope.calculatePackageAmounts();
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
	
	// add Group wise exclusion  start
	
	$scope.addGroupDetails = function()
	{
		
		$scope.searchServiceList = [];
		$scope.searchServObj = {};
		if($scope.packageObj.serviceId=="" || $scope.packageObj.serviceId==undefined){
			growl.warning("Please select package service!",{title : 'Warning!'});
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
		
		if($scope.speciality.specialityName==undefined || $scope.speciality.specialityName==''){
			growl.warning("Please select group!",{title : 'Warning!'});
			return false;
		}
		
		/*$("#divEnabled").removeClass("enabled");
		$("#divEnabled").addClass("disabled");
		$("#divEnabled2").removeClass("enabled");
		$("#divEnabled2").addClass("disabled");*/
		console.log("SPE",$scope.speciality);
		console.log("SUp SPE",$scope.subSpeciality );
		/* if(angular.equals({}, $scope.packageObjResponse) || $scope.packageObjResponse==null)
		 {*/
			    $scope.selectedServiceList = [];
				$scope.selectedServiceIds = [];
				$rootScope.startLoader();
				$scope.addNewServiceList = [];
				arraList=cartesian($scope.selectedPaymentEntitlementList,$scope.selectedPatientTypeList,$scope.selectedBedBillingCategoryList);
				for(i=0;i<arraList.length;i++){
					var tempObj={						
							paymentEntitlementId:"",paymentEntitlement:"",
							visitTypeId:"",visitTypeName:"",
							patientTypeId:"",patientType:"",
							billingBedCategoryId:"",billingBedCategory:"",
							packagetypeId:4,orgId:$scope.orgId,unitId:$scope.unitId,
						};
					
					var tempArr=arraList[i];
					$scope.paymentEntitlementId =tempArr[0].id;
					$scope.patientTypeId =tempArr[1].id;
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
						    $scope.addNewServiceList.push(newTempObj);
				       }
				}
				var data = {
						serviceIds:[],
						packageTypeId:$scope.packageObj.packageTypeId,
						serviceForPackageReqList:$scope.addNewServiceList,
						sexId :$scope.packageObj.sexId
				};
				
				var URI = BASE_URL + ROOT_URL + "/api/packages/package/service/search";
				BillingGenericService.serviceAction("POST", URI, data).then(function(response)
						{
					           if(response.data.status=="success")
					        	   {
//									$scope.packageObjResponse= response.data.object;
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
																	
													var departmentCapAmount = '';
													var subDepartmentCapAmount = '';
													if($scope.subSpeciality.subSpecialityId != '' && typeof($scope.subSpeciality.subSpecialityId) != "undefined")
														subDepartmentCapAmount = $rootScope.parseFloatNum(0);
													else
														departmentCapAmount = $rootScope.parseFloatNum(0);
													
													var temp = {specialityId:$scope.speciality.specialityId, 
															   subSpecialityId:$scope.subSpeciality.subSpecialityId,
															   departmentCapAmount:departmentCapAmount,
															   subDepartmentCapAmount:subDepartmentCapAmount,
															  groupName:$scope.speciality.specialityName,
															  subGroupName:$scope.subSpeciality.subSpecialityName,
															  isGroupwiseExlusion:'2',
															  serviceId:undefined
															};
														
														 var tempserviceList = tempPackgeObj.paymentEntitlement[indexPaymentEnt].patientCategoryList[indexPatientCat].billingBedCategoryList[indexBillingBedCat].mPackageMasterDto.listTPackageIncExcServiceDetailsDto;
														 if(tempserviceList!=undefined && tempserviceList!=null && tempserviceList.length>0){
														  /*angular.forEach(billingBedCat.mPackageMasterDto.listTPackageIncExcServiceDetailsDto,function(serviceDtls,indexServiceDtls)
																  {*/
																	  if(temp.subSpecialityId>0)
																		{
																		//for checking sub-sepcaility exist or not
																		  var subSpecialityChk = tempPackgeObj.paymentEntitlement[indexPaymentEnt].patientCategoryList[indexPatientCat].billingBedCategoryList[indexBillingBedCat].mPackageMasterDto.listTPackageIncExcServiceDetailsDto.find( (service) => (service.specialityId==temp.specialityId && service.subSpecialityId==temp.subSpecialityId && service.isGroupwiseExlusion=='2'));
																		//for checking sepcaility exist or not
																		  var specialityChk = tempPackgeObj.paymentEntitlement[indexPaymentEnt].patientCategoryList[indexPatientCat].billingBedCategoryList[indexBillingBedCat].mPackageMasterDto.listTPackageIncExcServiceDetailsDto.find( (service) => (service.specialityId==temp.specialityId && service.subSpecialityId=='' && service.isGroupwiseExlusion=='2'));
																			 if(specialityChk!=undefined){
																				 growl.warning("Speciality or sub sepeciality already added!",{title : 'Warning!'});
																					return false;
																			 }else{															 
																				 if(subSpecialityChk==undefined)
																				  {
																					 tempPackgeObj.paymentEntitlement[indexPaymentEnt].patientCategoryList[indexPatientCat].billingBedCategoryList[indexBillingBedCat].mPackageMasterDto.listTPackageIncExcServiceDetailsDto.push(temp);
																				  }
																			 }
																 
																}
															else if(temp.subDepartmentId==null||temp.subDepartmentId=="")
																{
																//for checking sub-sepcaility exist or not
																  var subSpecialityChk = tempPackgeObj.paymentEntitlement[indexPaymentEnt].patientCategoryList[indexPatientCat].billingBedCategoryList[indexBillingBedCat].mPackageMasterDto.listTPackageIncExcServiceDetailsDto.find( (service) => (service.specialityId==temp.specialityId && service.isGroupwiseExlusion=='2'));
																//for checking sepcaility exist or not
																  var specialityChk = tempPackgeObj.paymentEntitlement[indexPaymentEnt].patientCategoryList[indexPatientCat].billingBedCategoryList[indexBillingBedCat].mPackageMasterDto.listTPackageIncExcServiceDetailsDto.find( (service) => (service.specialityId==temp.specialityId && service.subSpecialityId=='' && service.isGroupwiseExlusion=='2'));
																  if(subSpecialityChk!=undefined){
																	  growl.warning("Speciality or sub sepeciality already added!",{title : 'Warning!'});
																		return false;
																  }else{
																	  if(specialityChk==undefined)
																	  {
																		  tempPackgeObj.paymentEntitlement[indexPaymentEnt].patientCategoryList[indexPatientCat].billingBedCategoryList[indexBillingBedCat].mPackageMasterDto.listTPackageIncExcServiceDetailsDto.push(temp);
																	  } 
																  }
																  
																}
														          /*});*/
													}else{
														var serviceList=[];
														if(temp.subSpecialityId>0)
														{
															 serviceList.push(temp);
															
														 
														}
													else if(temp.subSpecialityId==null||temp.subSpecialityId=="")
														{
														 serviceList.push(temp);														
														  
														}
														 tempPackgeObj.paymentEntitlement[indexPaymentEnt].patientCategoryList[indexPatientCat].billingBedCategoryList[indexBillingBedCat].mPackageMasterDto.listTPackageIncExcServiceDetailsDto=serviceList;
													}
													
												});//billingBedCategory			
												
											});//patientCategory
											
										});// payment entitlement end
									
										$scope.packageObjResponse=tempPackgeObj;
								}
									$scope.calculatePackageAmounts();
									$scope.crateClone();
									console.log("pck obj", $scope.packageObjResponse);
										$rootScope.stopLoader();
										$scope.initSubSpeciality();
										$scope.initSpeciality();
										 //enabling save button start
//									  	$("#savePackageId").removeClass("disabled");
//										$("#savePackageId").addClass("enabled");
										//enabling save button end
					           }
					           else  if(response.data.status=="error")
					        	   {
					        	     $rootScope.stopLoader();
					        	   }
						});
		 
	}
	// add Group wise exclusion  end
//	First Level
	$scope.firstTabLevel = 3 ;
	
	$scope.setFirstTabLevel= function(newTab)
	{
		$scope.firstTabLevel = newTab;
	};
	
	$scope.isSetFirstTabLevel = function(tabNum)
	{
		return $scope.firstTabLevel === tabNum;
	};
	
if($stateParams.newAllExclusiveObj!=undefined && $stateParams.newAllExclusiveObj!=null && $stateParams.newAllExclusiveObj.parentPackageId>0){
                              $scope.packDetailsObj = {};
                              $scope.packDetailsObj.parentPackageId = $stateParams.newAllExclusiveObj.parentPackageId;
                              $scope.parentPackageId = $stateParams.newAllExclusiveObj.parentPackageId;
                              $scope.packDetailsObj.packageTypeId = 4;
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
                            					packagetypeId:4,orgId:$scope.orgId,unitId:$scope.unitId,
                            				};

                            			var tempArr=arraList[i];

                            			$scope.paymentEntitlementId =tempArr[0].id;
                            			$scope.patientTypeId =tempArr[1].id;
                            //			$scope.visitTypeId =tempArr[2].id;
                            			$scope.bedBillingCategoryId =tempArr[2].id;
                            			//get visit type id by billing bed category id
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

                            				newTempObj.tariffId=$scope.defaultSelfTariffId;

                            				console.log(JSON.stringify(newTempObj))
                            			 $scope.addNewServiceList.push(newTempObj);
                            		  } //IF
                            		} //FOr
                    packageObjectFetch.serviceForPackageReqList=$scope.addNewServiceList;
        			var URI = BASE_URL + ROOT_URL + "/api/packages/editPackage";
        			BillingGenericService.serviceAction(METHOD_POST,URI, packageObjectFetch,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
        				if (response.data.status == "success")
        				{
        					$scope.selectedPaymentEntitlementList=[];
                            				$scope.selectedPatientTypeList=[];
                            				$scope.selectedBedBillingCategoryList=[];
                            				$scope.selectedVisitTypeList=[];
                            			    $scope.packageObjResponse= response.data.object==null?{}:response.data.object;
                            			   console.log("packageObjResponse",$scope.packageObjResponse);
                            			   $scope.packageObj.sexId = $scope.packageObjResponse.parentPackageMasterDto.sexId;
                            			  // tempPackgeObj= angular.copy($scope.packageObjResponse);
                            			   $scope.packageService = $scope.packageObjResponse.parentPackageMasterDto.packageName;
                                           $scope.packageObj.serviceId = $scope.packageObjResponse.parentPackageMasterDto.serviceId;

                            			   angular.forEach($scope.packageObjResponse.paymentEntitlement,function(paymentEnt,indexPaymentEnt){

                            					angular.forEach(paymentEnt.patientCategoryList, function(patientCat,indexPatientCat){

                            						angular.forEach(patientCat.billingBedCategoryList,function(billingBedCat,indexBillingBedCat){

                            							visitTypeArray.push(billingBedCat.mPackageMasterDto.visitTypeId);

                            							var billingBedCategoryData={
                            									id:billingBedCat.billingBedCategoryId,
                            									label:billingBedCat.billingBedCategory
                            							}
                            							$scope.selectedBedBillingCategoryList.push(billingBedCategoryData);

                            							// for adding groupwise exclusion start
                            							var i = 0;
                            							angular.forEach(billingBedCat.mPackageMasterDto.listTPackageIncExcServiceDetailsDto,function(serviceDtls,indexServiceDtls)
                            									  {
                            								  		  if(serviceDtls.isGroupwiseExlusion!=undefined && serviceDtls.isGroupwiseExlusion!=null && serviceDtls.isGroupwiseExlusion==2){
                            								  			billingBedCat.mPackageMasterDto.listTPackageIncExcServiceDetailsDto[i].groupName = serviceDtls.specialityName;
                            								  			billingBedCat.mPackageMasterDto.listTPackageIncExcServiceDetailsDto[i].subGroupName = serviceDtls.subSpecialityName;

                            								  		  }
                            								  		 billingBedCat.mPackageMasterDto.listTPackageIncExcServiceDetailsDto[i].isSelected=true;
                            								  		  i++;
                            							          });

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
                            			   tempPackgeObj= angular.copy($scope.packageObjResponse);
                            			   var uniqueVisitType=new Set(visitTypeArray);
                            			   $scope.selectedVisitTypeList = Array.from(uniqueVisitType);
                            			   $scope.selectedVisitTypeList = $scope.packageObjResponse.selectedVisitTypeList;
                                           $scope.selectedPatientTypeList =  $scope.packageObjResponse.selectedPatientTypeList;
                                           $scope.selectedBedBillingCategoryList = $scope.packageObjResponse.selectedBedBillingCategoryList;
                                           $scope.selectedPaymentEntitlementList =  $scope.packageObjResponse.selectedPaymentEntitlementList;
                                           $scope.bedBillingCategoryList = $scope.packageObjResponse.selectedBedBillingCategoryList;
                                           $scope.fetchValidate = false;
                            			   console.log("$scope.selectedVisitTypeList",$scope.selectedVisitTypeList);
                            			   $scope.calculatePackageAmounts();
                            			    $scope.crateClone();
                            				$rootScope.stopLoader();
                            				 //enabling save button start
//                            			  	$("#savePackageId").removeClass("disabled");
//                            				$("#savePackageId").addClass("enabled");
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
        if(!angular.equals({}, $scope.packageObjResponse) && $scope.packageObjResponse.paymentEntitlement[0].patientCategoryList[0].billingBedCategoryList[0].mPackageMasterDto.listTPackageIncExcServiceDetailsDto
                !=null && $scope.packageObjResponse.paymentEntitlement[0].patientCategoryList[0].billingBedCategoryList[0].mPackageMasterDto.listTPackageIncExcServiceDetailsDto.length>0)
                {
                   validate = true;
                }
          if(!angular.equals({}, $scope.packageObjResponse) && $scope.packageObjResponse.paymentEntitlement[0].patientCategoryList[0].billingBedCategoryList[0].mPackageMasterDto.listTPackageIncExcItemDetailsDto
                          !=null && $scope.packageObjResponse.paymentEntitlement[0].patientCategoryList[0].billingBedCategoryList[0].mPackageMasterDto.listTPackageIncExcItemDetailsDto.length>0)
               {
                             validate = true;
               }
        if (($scope.packageObj.sexId == $scope.sexIdClone)
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
                && $scope.sexIdClone==undefined
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
        $scope.selectedVisitTypeListClone =  angular.copy($scope.selectedVisitTypeList);
        $scope.selectedPatientTypeListClone =  angular.copy($scope.selectedPatientTypeList);
        $scope.selectedBedBillingCategoryListClone = angular.copy($scope.selectedBedBillingCategoryList);
        $scope.selectedPaymentEntitlementListClone =  angular.copy($scope.selectedPaymentEntitlementList);
   }
	
}]);