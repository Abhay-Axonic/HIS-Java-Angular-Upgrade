

angular.module("myApp").controller("addNewPackageWithCAPDetailsController", [ '$scope','$rootScope','BillingGenericService','$cookies','$state','growl','servicesFactory','GenericService','itemFactory','$stateParams',
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
		
		
		$scope.getBedCategoryList = function(){
			  var URI = BASE_URL + ROOT_URL + "/adt/getBedCategoryListByOrgUnit";
			  var param = {organizationId:$scope.orgId,unitId:$scope.unitId};
			    BillingGenericService.serviceAction("POST", URI, param).then(function(response){
			    	if(response.data.status == "success")
			    		$scope.bedCategoryList = response.data.listObject;
			    });
		  }
		
		$scope.init = function(){
			this.isMarkupDown='';
			this.roundOffPositNeg = '';
			
			$scope.markupDownInPercentage = '';
			$scope.searchServiceList = [];
			$scope.specialityList = [];
			$scope.subSpecialityList = [];
			$scope.specialityGroupList=[];
			$scope.searchServObj = {};
			servicesFactory.initServiceSearch($scope);
			servicesFactory.initSpecailityGroupSearch($scope);
			
			$scope.selectedServiceList = [];
			$scope.bedCategoryList = [];
			$scope.bedCategory = {bedCategoryId:""};
			$scope.initPackageObj();
			$scope.serviceWiseSummary = 0;
			$scope.grandTotal = 0;
			$scope.packageService = "";
			$scope.itemObj = {};
			$scope.parentPackageId=null;
			itemFactory.initItemFactory($scope);
			//selected List
			$scope.selectedPatientTypeList=[];
			$scope.selectedVisitTypeList=[];
			$scope.selectedPaymentEntitlementList=[];
			$scope.selectedBedBillingCategoryList=[];
			$scope.packageObjResponse ={};
			$scope.getBedCategoryList();
			$scope.initSubSpeciality();
			$scope.initSpeciality();
		}
		
		$scope.opdBillingCatId = cookieObjectAppConfig.opdBillingCatId;
		$scope.initPackageObj = function(){
			$scope.packageObj = {packageTypeId:"3",sexId:"",minAge:"",maxAge:"",tariffId:"",serviceId:""}
		}
		$scope.initImportPackageObj = function(){
			$scope.importPkgObj = {
							packageTypeId:"3",
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
										$scope.bedBillingCategoryListReference = [];
										$scope.bedBillingCategoryListReference = response.data.listObject;
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
		
		$scope.getPaymentEntitlementList = function() 
		{
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
		  
			$scope.getTariffList = function(orgId,unitId) 
			{
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
		
		$scope.searchServiceList = [];
		$scope.searchServObj = {};
		  // validation
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
		$scope.specialityList=[];
		servicesFactory.initServiceSearch($scope);
		$('#packageWithCapServicesModal').modal('show');
	}
	
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
						     billingBedCat.mPackageMasterDto.packagePrice = $rootScope.parseFloatNum(0);
							  angular.forEach(billingBedCat.mPackageMasterDto.listTPackageServicesDetailsDto,function(serviceDtls,indexServiceDtls)
									  {
//								         serviceDtls.total = (serviceDtls.numberToBeUse==null || serviceDtls.numberToBeUse==undefined ) ? 1 :  serviceDtls.numberToBeUse * serviceDtls.servicePrice;
								  		 serviceDtls.numberToBeUse = (serviceDtls.numberToBeUse==null || serviceDtls.numberToBeUse==undefined ) ? 1 : serviceDtls.numberToBeUse ;
								         serviceDtls.servicePrice =  serviceDtls.servicePrice!=null && serviceDtls.servicePrice!=undefined ? serviceDtls.servicePrice : 0;
								         serviceDtls.apportionedPrice =serviceDtls.servicePrice ;
								         serviceDtls.total =$rootScope.parseFloatNum(serviceDtls.numberToBeUse * serviceDtls.servicePrice);
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
										var consumablesDtls = isNaN(value.servicePrice) || value.servicePrice == "" || typeof(value.servicePrice) == "undefined" ? parseFloat(0) : value.servicePrice;
										temp = parseFloat(temp) + parseFloat(consumablesDtls);
									}
									
								});
								billingBedCat.mPackageMasterDto.itemWiseSummary = $rootScope.parseFloatNum(temp);
								
								billingBedCat.mPackageMasterDto.packageCost = $rootScope.parseFloatNum(parseFloat(billingBedCat.mPackageMasterDto.serviceWiseSummary) + parseFloat(billingBedCat.mPackageMasterDto.groupWiseSummary) + parseFloat(billingBedCat.mPackageMasterDto.consumWiseSummary)+parseFloat(billingBedCat.mPackageMasterDto.itemWiseSummary));
								billingBedCat.mPackageMasterDto.packagePrice = billingBedCat.mPackageMasterDto.packageCost;
								
								//package cost
								switch($scope.isMarkupDown!=undefined ? $scope.isMarkupDown.toString() : "0"){
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
								}
					      });	
				});
			});
	}
	
	
	// getting final service list
	var tempPackgeObj = {};
	
	
	$scope.addServices = function(isServiceItem,isInclusiveService)
	{
		
		
		if(isServiceItem ==1 && isInclusiveService){
			$scope.alreadyExistsServices = false;
			$scope.alreadyExistsServicesExclu = false;
			angular.forEach($scope.searchServiceList, function(a) {
				if(a.isSelected){
				var found = $scope.packageObjResponse.paymentEntitlement[0].patientCategoryList[0].billingBedCategoryList[0].mPackageMasterDto.listTPackageServicesDetailsDto.find(ar=>ar.serviceMasterId == a.serviceMasterId);
				var foundExclu = undefined;
				if($scope.packageObjResponse.paymentEntitlement[0].patientCategoryList[0].billingBedCategoryList[0].mPackageMasterDto.listGroupWiseIncExc != null){
					 foundExclu = $scope.packageObjResponse.paymentEntitlement[0].patientCategoryList[0].billingBedCategoryList[0].mPackageMasterDto.listGroupWiseIncExc.find(ar=>ar.serviceId == a.serviceMasterId);
				}
				
				
				if(found != undefined){
						
						$scope.alreadyExistsServices = true;
					    $scope.serviceNamemd =a.serviceDescription;
				}
				if(foundExclu != undefined){
						
						$scope.alreadyExistsServicesExclu = true;
					    $scope.serviceNamemd =a.serviceDescription;
				}
				
				}
			});
			if($scope.alreadyExistsServicesExclu == true){
				growl.warning("Selected "+$scope.serviceNamemd +" is already Saved in Service exclusion Cap..!");
				return false;
			}
			
			$scope.checkDepartmentSame = false;
			$scope.checkSubDepartmentSame = true;
			angular.forEach($scope.searchServiceList, function(a) {
				if(a.isSelected){
				
				var found = $scope.packageObjResponse.paymentEntitlement[0].patientCategoryList[0].billingBedCategoryList[0].mPackageMasterDto.listTPackageGroupWiseCapDetailsDto.find(ar=>ar.departmentId == a.specialityId && ar.subDepartmentId =='' );
				if(found == undefined){
					    $scope.serviceNamemd =a.serviceDescription;
				}else{
		    		$scope.checkSubDepartmentSame = false;
		    	}
				
				var foundSub = $scope.packageObjResponse.paymentEntitlement[0].patientCategoryList[0].billingBedCategoryList[0].mPackageMasterDto.listTPackageGroupWiseCapDetailsDto.find(ar=>ar.departmentId == a.specialityId && ar.subDepartmentId ==a.subSpecialityMasterId );
				if(foundSub == undefined){
				    $scope.serviceNamemd =a.serviceDescription;
		    	}else{
		    		$scope.checkSubDepartmentSame = false;
		    	}
		
				
				}
			});
			if($scope.alreadyExistsServices == true){
				growl.warning("Selected "+$scope.serviceNamemd +" is already Saved in Service Wise Cap..!");
				return false;
			}
			if($scope.checkSubDepartmentSame == true){
				growl.warning("Selected "+$scope.serviceNamemd +" is having different Department other that groupwise department!");
				return false;
			}
			
			
			
		}
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
		$('#packageWithCapServicesModal').modal('hide');
		$rootScope.startLoader();
		$scope.addNewServiceList = [];
		arraList=cartesian($scope.selectedPaymentEntitlementList,$scope.selectedPatientTypeList,$scope.selectedBedBillingCategoryList);
		
		for(i=0;i<arraList.length;i++){
			
			var tempObj={						
					paymentEntitlementId:"",paymentEntitlement:"",
					visitTypeId:"",visitTypeName:"",
					patientTypeId:"",patientType:"",
					billingBedCategoryId:"",billingBedCategory:"",
					packagetypeId:3,orgId:$scope.orgId,unitId:$scope.unitId,
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
				newTempObj.isServiceItem=isServiceItem;
				newTempObj.isInclusiveService=isInclusiveService;
				newTempObj.sexId =$scope.packageObj.sexId;
			    $scope.addNewServiceList.push(newTempObj);
		  } //IF
		} //FOr
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

										angular.forEach(patientCat.billingBedCategoryList,function(billingBedCat,indexBillingBedCat)
										 {								
											//1.for normal services
											   var tempserviceList = tempPackgeObj.paymentEntitlement[indexPaymentEnt].patientCategoryList[indexPatientCat].billingBedCategoryList[indexBillingBedCat].mPackageMasterDto.listTPackageServicesDetailsDto;
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
											   
											//2. for inclusion exlusion
											     var tempserviceListIEX = tempPackgeObj.paymentEntitlement[indexPaymentEnt].patientCategoryList[indexPatientCat].billingBedCategoryList[indexBillingBedCat].mPackageMasterDto.listGroupWiseIncExc
												 if(tempserviceListIEX!=undefined && tempserviceListIEX!=null){
												  angular.forEach(billingBedCat.mPackageMasterDto.listGroupWiseIncExc,function(serviceDtls,indexServiceDtls)
														  {
															  var found = tempserviceListIEX.find(function(service) { 
																  return service.serviceId == serviceDtls.serviceId; 
																});
													        if(found==undefined || found.length==0)
													        	{
													        	  tempPackgeObj.paymentEntitlement[indexPaymentEnt].patientCategoryList[indexPatientCat].billingBedCategoryList[indexBillingBedCat].mPackageMasterDto.listGroupWiseIncExc.push(serviceDtls);
													        	}
												          });
											}else{
												var serviceListIEX=[];
												 angular.forEach(billingBedCat.mPackageMasterDto.listGroupWiseIncExc,function(serviceDtls,indexServiceDtls)
												 {
													serviceListIEX.push(serviceDtls);
												 });
												 tempPackgeObj.paymentEntitlement[indexPaymentEnt].patientCategoryList[indexPatientCat].billingBedCategoryList[indexBillingBedCat].mPackageMasterDto.listGroupWiseIncExc=serviceListIEX;
											} 
											   // checking group detials and bed category added or not
											     if(!angular.equals({}, $scope.packageObjResponse) ){
											    
											    	// for group details
												     if($scope.packageObjResponse.paymentEntitlement[indexPaymentEnt].patientCategoryList[indexPatientCat].billingBedCategoryList[indexBillingBedCat].mPackageMasterDto.listTPackageGroupWiseCapDetailsDto==null)
												    	 $scope.packageObjResponse.paymentEntitlement[indexPaymentEnt].patientCategoryList[indexPatientCat].billingBedCategoryList[indexBillingBedCat].mPackageMasterDto.listTPackageGroupWiseCapDetailsDto=[];
												     else
												    	tempPackgeObj.paymentEntitlement[indexPaymentEnt].patientCategoryList[indexPatientCat].billingBedCategoryList[indexBillingBedCat].mPackageMasterDto.listTPackageGroupWiseCapDetailsDto=$scope.packageObjResponse.paymentEntitlement[indexPaymentEnt].patientCategoryList[indexPatientCat].billingBedCategoryList[indexBillingBedCat].mPackageMasterDto.listTPackageGroupWiseCapDetailsDto;
													 
												     // for bed category
												     if($scope.packageObjResponse.paymentEntitlement[indexPaymentEnt].patientCategoryList[indexPatientCat].billingBedCategoryList[indexBillingBedCat].mPackageMasterDto.listTPackageBedCategoryDetailDto==null)
												    	 $scope.packageObjResponse.paymentEntitlement[indexPaymentEnt].patientCategoryList[indexPatientCat].billingBedCategoryList[indexBillingBedCat].mPackageMasterDto.listTPackageBedCategoryDetailDto=[];
												     else
												    	tempPackgeObj.paymentEntitlement[indexPaymentEnt].patientCategoryList[indexPatientCat].billingBedCategoryList[indexBillingBedCat].mPackageMasterDto.listTPackageBedCategoryDetailDto=$scope.packageObjResponse.paymentEntitlement[indexPaymentEnt].patientCategoryList[indexPatientCat].billingBedCategoryList[indexBillingBedCat].mPackageMasterDto.listTPackageBedCategoryDetailDto;
													 
											     }
											     $scope.removegroupWiseCapping();
										});	
									});
								});
							  $scope.packageObjResponse=tempPackgeObj;
							  this.packageObjResponse=tempPackgeObj;
							}
						$scope.calculateSummaryAmt();
						$scope.crateClone();
						$rootScope.stopLoader();
		 });
	}
	
	$scope.removeSelectedService = function(index){
		$scope.$scope.selectedServiceIds.splice(index,1);
		$scope.selectedServiceIds.splice(index,1);
	
	}
	
	
	$scope.addItemWiseInclusionExclusion = function(isServiceItem,isInclusiveService){
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
					packagetypeId:3,orgId:$scope.orgId,unitId:$scope.unitId,
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
			    $scope.addNewItemList.push(newTempObj);
		  } //IF
		} //FOr
		var data = {
				itemIds:$scope.selecteditemIds,
				packageTypeId:$scope.packageObj.packageTypeId,
				serviceForPackageReqList:$scope.addNewItemList,
				isServiceItem:isServiceItem,
				isInclusiveService:isInclusiveService,
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
										});
									});
								});
							  $scope.packageObjResponse=tempPackgeObj;
							}
						$scope.calculateSummaryAmt();
						$scope.crateClone();
						$rootScope.stopLoader();
		});
	}
	
	$scope.removeItemWiseExclusion = function(index){
		$scope.itemWiseExclusionList.splice(index,1);
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
			if($scope.packageObj.tariffId==undefined || $scope.packageObj.tariffId==''){
				growl.warning("Please Select tariff!",{title : 'Warning!'});
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
			$('#addConsumableWiseItemModal').modal('show');
			
		}

		$scope.addConsumableItems = function(isServiceItem,isInclusiveService){
			$scope.itemList = [];
			$scope.selecteditemIds = [];
			angular.forEach($scope.searchItemList,function(value,key){
				if(value.isSelected){
					/*value.qty = 0;
					value.total = 0;*/
					$scope.selecteditemIds.push(value.itemId);
					$scope.itemList.push(value);
				}
			});
			if($scope.itemList==undefined || $scope.itemList.length==0){
				growl.warning("Please Select items!",{title : 'Warning!'});
				return false;
			}
			$('#addConsumableWiseItemModal').modal('hide');
			$rootScope.startLoader();
			$scope.addNewItemList = [];
			arraList=cartesian($scope.selectedPaymentEntitlementList,$scope.selectedPatientTypeList,$scope.selectedBedBillingCategoryList);
			
			for(i=0;i<arraList.length;i++){
				
				var tempObj={						
						paymentEntitlementId:"",paymentEntitlement:"",
						visitTypeId:"",visitTypeName:"",
						patientTypeId:"",patientType:"",
						billingBedCategoryId:"",billingBedCategory:"",
						packagetypeId:3,orgId:$scope.orgId,unitId:$scope.unitId,
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
					newTempObj.isServiceItem=isServiceItem;
					newTempObj.isInclusiveService=isInclusiveService;
					
					console.log(JSON.stringify(newTempObj))
				 $scope.addNewItemList.push(newTempObj);
			  } //IF
			} //FOr
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
												//1.for normal services
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
												 
													//2. for inclusion exlusion
											     var tempItemListIEX = tempPackgeObj.paymentEntitlement[indexPaymentEnt].patientCategoryList[indexPatientCat].billingBedCategoryList[indexBillingBedCat].mPackageMasterDto.listConsumableWiseIncExc;
												 if(tempItemListIEX!=undefined && tempItemListIEX!=null){
												  angular.forEach(billingBedCat.mPackageMasterDto.listConsumableWiseIncExc,function(itemDtls,indexItemDtlsDtls)
														  {
															  var found = tempItemListIEX.find(function(item) { 
																  return item.itemId == itemDtls.itemId; 
																});
													        if(found==undefined || found.length==0)
													        	{
													        	  tempPackgeObj.paymentEntitlement[indexPaymentEnt].patientCategoryList[indexPatientCat].billingBedCategoryList[indexBillingBedCat].mPackageMasterDto.listConsumableWiseIncExc.push(itemDtls);
													        	}
												          });
											}else{
												var itemListIEX=[];
												 angular.forEach(billingBedCat.mPackageMasterDto.listConsumableWiseIncExc,function(itemDtls,indexItemDtlsDtls)
												 {
													 itemListIEX.push(itemDtls);
												 });
												 tempPackgeObj.paymentEntitlement[indexPaymentEnt].patientCategoryList[indexPatientCat].billingBedCategoryList[indexBillingBedCat].mPackageMasterDto.listConsumableWiseIncExc=itemListIEX;
											} 
												 
												 // checking group detials and bed category added or not
											     if(!angular.equals({}, $scope.packageObjResponse) ){
											    
											    	// for group details
												     if($scope.packageObjResponse.paymentEntitlement[indexPaymentEnt].patientCategoryList[indexPatientCat].billingBedCategoryList[indexBillingBedCat].mPackageMasterDto.listTPackageGroupWiseCapDetailsDto==null)
												    	 $scope.packageObjResponse.paymentEntitlement[indexPaymentEnt].patientCategoryList[indexPatientCat].billingBedCategoryList[indexBillingBedCat].mPackageMasterDto.listTPackageGroupWiseCapDetailsDto=[];
												     else
												    	tempPackgeObj.paymentEntitlement[indexPaymentEnt].patientCategoryList[indexPatientCat].billingBedCategoryList[indexBillingBedCat].mPackageMasterDto.listTPackageGroupWiseCapDetailsDto=$scope.packageObjResponse.paymentEntitlement[indexPaymentEnt].patientCategoryList[indexPatientCat].billingBedCategoryList[indexBillingBedCat].mPackageMasterDto.listTPackageGroupWiseCapDetailsDto;
													 
												     // for bed category
												     if($scope.packageObjResponse.paymentEntitlement[indexPaymentEnt].patientCategoryList[indexPatientCat].billingBedCategoryList[indexBillingBedCat].mPackageMasterDto.listTPackageBedCategoryDetailDto==null)
												    	 $scope.packageObjResponse.paymentEntitlement[indexPaymentEnt].patientCategoryList[indexPatientCat].billingBedCategoryList[indexBillingBedCat].mPackageMasterDto.listTPackageBedCategoryDetailDto=[];
												     else
												    	tempPackgeObj.paymentEntitlement[indexPaymentEnt].patientCategoryList[indexPatientCat].billingBedCategoryList[indexBillingBedCat].mPackageMasterDto.listTPackageBedCategoryDetailDto=$scope.packageObjResponse.paymentEntitlement[indexPaymentEnt].patientCategoryList[indexPatientCat].billingBedCategoryList[indexBillingBedCat].mPackageMasterDto.listTPackageBedCategoryDetailDto;
													 
											     }
												 
											});//billingBedCategory			
											
										});//patientCategory
										
									});// payment entitlement end
								  $scope.packageObjResponse=tempPackgeObj;
								  console.log("pck obj", $scope.packageObjResponse);
								}
							$scope.calculateSummaryAmt();
							$scope.crateClone();
							$rootScope.stopLoader();
			});
		}
		
		$scope.removeItemWiseExclusion = function(index){
			$scope.itemList.splice(index,1);
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
		
		
		$scope.packageObj.orgId = $scope.orgId;
		$scope.packageObj.unitId = $scope.unitId;
		$scope.packageObj.createdBy = $scope.userId;
		$scope.packageObj.updatedBy = $scope.userId;
		$scope.packageObj.packageTypeId = 3;
		$scope.packageObj.serviceId=$scope.packageObj.serviceId;
		$scope.packageObj.tariffId=$scope.packageObj.tariffId;
		$scope.packageObj.parentPackageId=$scope.parentPackageId;
        $scope.packageObj.packageName = $scope.packageService;
		$scope.packageObj.packageMasterDtoList=[];
		
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
        		if($scope.packageObj.tariffId==undefined || $scope.packageObj.tariffId==''){
        			growl.warning("Please Select tariff!",{title : 'Warning!'});
        			return false;
        		}
        		if($scope.selectedPaymentEntitlementList==undefined || $scope.selectedPaymentEntitlementList.length==0){
        			growl.warning("Please Select payment entitlement!",{title : 'Warning!'});
        			return false;
        		}

             if( $scope.packageObj.sexId==="" || $scope.packageObj.sexId===null || $scope.packageObj.sexId===undefined)
         		{
         		  growl.warning("Please select gender.",{title : 'Warning!'});
         		  $scope.saveFlag = false;
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
         &&  $scope.packageObjResponse.paymentEntitlement[0].patientCategoryList[0].billingBedCategoryList[0].mPackageMasterDto.listTPackageGroupWiseCapDetailsDto==null
       	 &&  $scope.packageObjResponse.paymentEntitlement[0].patientCategoryList[0].billingBedCategoryList[0].mPackageMasterDto.listGroupWiseIncExc==null
       	 &&  $scope.packageObjResponse.paymentEntitlement[0].patientCategoryList[0].billingBedCategoryList[0].mPackageMasterDto.listTPackageBedCategoryDetailDto==null
         &&  $scope.packageObjResponse.paymentEntitlement[0].patientCategoryList[0].billingBedCategoryList[0].mPackageMasterDto.listTPackageItemDetailsDto==null
         &&  $scope.packageObjResponse.paymentEntitlement[0].patientCategoryList[0].billingBedCategoryList[0].mPackageMasterDto.listTPackageConsumableWiseCapDetailsDto==null
         &&  $scope.packageObjResponse.paymentEntitlement[0].patientCategoryList[0].billingBedCategoryList[0].mPackageMasterDto.listConsumableWiseIncExc==null))
            {
               growl.warning("Please add data to package!",{title : 'Warning!'});
               			  return false;
            }

		
		$scope.saveFlag = true;
		angular.forEach($scope.packageObjResponse.paymentEntitlement,function(paymentEnt,indexPaymentEnt){
			
			angular.forEach(paymentEnt.patientCategoryList, function(patientCat,indexPatientCat){

				angular.forEach(patientCat.billingBedCategoryList,function(billingBedCat,indexBillingBedCat){
					
					
					if(isNaN(Number(billingBedCat.mPackageMasterDto.packagePrice))||Number(billingBedCat.mPackageMasterDto.packagePrice)==0)
					{
					 growl.warning("Package price should not be zero!",{title : 'Warning!'});
					 $scope.saveFlag = false;
					 return false;
					}
					
					$scope.tempListTPackageServicesDetailsDto=[];//normal services
					$scope.tempListTPackageItemsDetailsDto=[]; //normal items
					$scope.templistTPackageGroupWiseCapDetailsDto=[]; //for group wise cap
					$scope.templistGroupWiseIncExc=[]; //for group wise inc exc
					$scope.templistTPackageBedCategoryDetailDto=[];//for bed category 
					$scope.templistTPackageCategoryWiseConsumableDetailsDto=[];//category wise consumables
					$scope.templistTPackageConsumableWiseCapDetailsDto=[];// category wise cap
					$scope.templistConsumableWiseIncExc=[]; // category wise cap inclusion-exclusion
					$scope.packageMasterDtoList=[];
 								var tempPackageObj={};
 							tempPackageObj.packageTypeId	=3;
							tempPackageObj.visitTypeId          =billingBedCat.mPackageMasterDto.visitTypeId;
							tempPackageObj.patientTypeId           =billingBedCat.mPackageMasterDto.patientTypeId;
							tempPackageObj.billingBedCategoryId   =billingBedCat.mPackageMasterDto.billingBedCategoryId;
							tempPackageObj.paymentEntitlementTypeId=billingBedCat.mPackageMasterDto.paymentEntitlementTypeId;
							tempPackageObj.sexId=$scope.packageObj.sexId;
							tempPackageObj.packagePrice=billingBedCat.mPackageMasterDto.packagePrice;
							tempPackageObj.packageCost=billingBedCat.mPackageMasterDto.packageCost;
							tempPackageObj.orgId = $scope.orgId;
							tempPackageObj.unitId = $scope.unitId;
							tempPackageObj.tariffId=$scope.packageObj.tariffId;
							tempPackageObj.validityPeriodIndays=billingBedCat.mPackageMasterDto.validityPeriodIndays;
							tempPackageObj.markupDownInPercentage=$scope.markupDownInPercentage;
							tempPackageObj.isMarkupDown= $scope.isMarkupDown 
							tempPackageObj.manualRoundoffAmount=billingBedCat.mPackageMasterDto.manualRoundoffAmount;
							tempPackageObj.roundOffPositNeg =  $scope.roundOffPositNeg;
							
							//for normal services tap 1
							if(billingBedCat.mPackageMasterDto.listTPackageServicesDetailsDto!=null && billingBedCat.mPackageMasterDto.listTPackageServicesDetailsDto!=undefined)
							{
								 angular.forEach(billingBedCat.mPackageMasterDto.listTPackageServicesDetailsDto,function(serviceDtls,indexServiceDtls){
										/*if(serviceDtls.isSelected!=undefined && serviceDtls.isSelected==true)	
											{*/
											
											var apportionedPrice = (parseFloat(serviceDtls.servicePrice) / 100) * parseFloat($scope.markupDownInPercentage);
											switch($scope.isMarkupDown!=undefined ? $scope.isMarkupDown.toString() : "0"){
											case "1":
												apportionedPrice = parseFloat(serviceDtls.servicePrice) + parseFloat(apportionedPrice);
												break;
											case "2":
												apportionedPrice = parseFloat(serviceDtls.servicePrice) - parseFloat(apportionedPrice);
												break;
											}
											  var newServiceObj={
																	apportionedPrice:parseFloat(serviceDtls.servicePrice),
																	servicePrice : parseFloat(serviceDtls.servicePrice),
																	serviceId:serviceDtls.serviceId,												
																	specialityId:serviceDtls.specialityId,
																	subSpecialityId:serviceDtls.subSpecialityId,
																	isSelected:serviceDtls.isSelected,
																	isCap:serviceDtls.isCap,
																	isServiceItem:1,
																	numberToBeUse : serviceDtls.numberToBeUse,
																	
															 }		
											   $scope.tempListTPackageServicesDetailsDto.push(newServiceObj);
//											}
								});
							}
							tempPackageObj.listTPackageServicesDetailsDto=$scope.tempListTPackageServicesDetailsDto;	
							//for tap 2 package cap details for group wise cap
							
							if(billingBedCat.mPackageMasterDto.listTPackageGroupWiseCapDetailsDto!=null && billingBedCat.mPackageMasterDto.listTPackageGroupWiseCapDetailsDto!=undefined)
							{
								 angular.forEach(billingBedCat.mPackageMasterDto.listTPackageGroupWiseCapDetailsDto,function(grpDtls,indexGrpDtls){
									var newGrpObj={
													    departmentId : 	grpDtls.departmentId,
														subDepartmentId : grpDtls.subDepartmentId,
														departmentCapAmount : grpDtls.departmentCapAmount,
														subDepartmentCapAmount : grpDtls.subDepartmentCapAmount,
														isServiceItem : 1,
														createdBy:$scope.userId,
                                                        groupName:grpDtls.groupName,
                                                        subGroupName:grpDtls.subGroupName
												}		
											   $scope.templistTPackageGroupWiseCapDetailsDto.push(newGrpObj);											
								     });
							}						
							tempPackageObj.listTPackageGroupWiseCapDetailsDto=$scope.templistTPackageGroupWiseCapDetailsDto;
							//for tap 2 package cap details for inclusion and exclusion services
							
							if(billingBedCat.mPackageMasterDto.listGroupWiseIncExc!=null && billingBedCat.mPackageMasterDto.listGroupWiseIncExc!=undefined)
							{
								 angular.forEach(billingBedCat.mPackageMasterDto.listGroupWiseIncExc,function(incServiceDtls,indexIncServiceDtls){
									 var temp = {
													serviceId : incServiceDtls.serviceId,
													isInclusiveService : incServiceDtls.isInclusiveService,
													isServiceItem : 1,
													createdBy:$scope.userId
												};	
											   $scope.templistGroupWiseIncExc.push(temp);											
								     });
							}						
							tempPackageObj.listGroupWiseIncExc=$scope.templistGroupWiseIncExc;
							
							//for tap 3 Bed Category wise
							
							if(billingBedCat.mPackageMasterDto.listTPackageBedCategoryDetailDto!=null && billingBedCat.mPackageMasterDto.listTPackageBedCategoryDetailDto!=undefined)
							{
								 angular.forEach(billingBedCat.mPackageMasterDto.listTPackageBedCategoryDetailDto,function(bedDtls,indexBedDtls){
									 var temp = {
													bedCategoryId : bedDtls.bedCategoryId,
													applicableDays : bedDtls.applicableDays,
													createdBy:$scope.userId,
													bedCategoryDesc:bedDtls.bedCategoryDesc
												};
									    $scope.templistTPackageBedCategoryDetailDto.push(temp);											
								     });
							}						
							tempPackageObj.listTPackageBedCategoryDetailDto=$scope.templistTPackageBedCategoryDetailDto;
							
							// for normal item tap no 4
							if(billingBedCat.mPackageMasterDto.listTPackageItemDetailsDto!=null && billingBedCat.mPackageMasterDto.listTPackageItemDetailsDto!=undefined)
							{
								 angular.forEach(billingBedCat.mPackageMasterDto.listTPackageItemDetailsDto,function(itemDtls,indexItemDtls){
									 if(parseFloat(itemDtls.servicePrice)==0 || itemDtls.servicePrice=='' || itemDtls.servicePrice==undefined || itemDtls.servicePrice==null){
										 growl.warning("Item wise amount should not be zero!",{title : 'Warning!'});
										 $scope.saveFlag = false;
										 $scope.setFirstTabLevel(4);
										 return false;
									 }
									var newItemObj={
													itemId:itemDtls.itemId,												
													numberToBeUse : itemDtls.numberToBeUse,
													servicePrice : parseFloat(itemDtls.servicePrice),
													isServiceItem:2,
											 }		
									$scope.tempListTPackageItemsDetailsDto.push(newItemObj);
											
								});
							}
							tempPackageObj.listTPackageItemDetailsDto=$scope.tempListTPackageItemsDetailsDto;
							
							// for category wise consumables tap no 5
							if(billingBedCat.mPackageMasterDto.listTPackageCategoryWiseConsumableDetailsDto!=null && billingBedCat.mPackageMasterDto.listTPackageCategoryWiseConsumableDetailsDto!=undefined)
							{
								 angular.forEach(billingBedCat.mPackageMasterDto.listTPackageCategoryWiseConsumableDetailsDto,function(itemDtls,indexItemDtls){
									 var temp = {
												numberToBeUse : itemDtls.numberToBeUse,
												createdBy:$scope.userId,
//												isServiceItem : 2,
												categoryId:itemDtls.productCategoryId,
												itemId : itemDtls.itemId,
										      }	
									$scope.templistTPackageCategoryWiseConsumableDetailsDto.push(temp);
											
								});
							}
							tempPackageObj.listTPackageCategoryWiseConsumableDetailsDto=$scope.templistTPackageCategoryWiseConsumableDetailsDto;
							
							// for category wise cap ,tap no 6
							if(billingBedCat.mPackageMasterDto.listTPackageConsumableWiseCapDetailsDto!=null && billingBedCat.mPackageMasterDto.listTPackageConsumableWiseCapDetailsDto!=undefined)
							{
								 angular.forEach(billingBedCat.mPackageMasterDto.listTPackageConsumableWiseCapDetailsDto,function(itemDtls,indexItemDtls){
									 var temp = {
													createdBy:$scope.userId,
													productCateroyId : (itemDtls.productCategoryId!=null?itemDtls.productCategoryId:itemDtls.productCateroyId),
													productCateroyCapAmount : itemDtls.productCateroyCapAmount,
													quantity : itemDtls.quantity ,
													isServiceItem : 2,
													productCategory:itemDtls.productCategory
										       }
									$scope.templistTPackageConsumableWiseCapDetailsDto.push(temp);
											
								});
							}
							tempPackageObj.listTPackageConsumableWiseCapDetailsDto=$scope.templistTPackageConsumableWiseCapDetailsDto;
							
							// for category wise cap inclusion-exclusion ,tap no 6
							if(billingBedCat.mPackageMasterDto.listConsumableWiseIncExc!=null && billingBedCat.mPackageMasterDto.listConsumableWiseIncExc!=undefined)
							{
								 angular.forEach(billingBedCat.mPackageMasterDto.listConsumableWiseIncExc,function(itemDtls,indexItemDtls){
									 var temp = {
												createdBy:$scope.userId,
												isInclusiveService : itemDtls.isInclusiveService,
												isServiceItem : 2,
												itemId : itemDtls.itemId
										}
									$scope.templistConsumableWiseIncExc.push(temp);
											
								});
							}
							tempPackageObj.listConsumableWiseIncExc=$scope.templistConsumableWiseIncExc;
					  				
					$scope.packageObj.packageMasterDtoList.push(tempPackageObj);
					
				});//billingBedCategory			
				
				
				
			});//patientCategory
			
		});// payment entitlement end
		if(fetch){
                 $scope.editPackage($scope.packageObj);
                }
        else if($scope.saveFlag && !fetch)
			{
			$rootScope.startLoader();
			var URI = BASE_URL + ROOT_URL + "/api/packages/packagewithcap";
			BillingGenericService.serviceAction(METHOD_POST,URI, $scope.packageObj,NOTIFICATION_MSG_STATUS_TRUE).then(function(response) {
				if (response.data.status == "success")
					{
						$scope.$broadcast('show-errors-reset');
					    $scope.packageObjResponse={};
					    tempPackgeObj={};
						$scope.init();
//						$("#divEnabled").removeClass("disabled");
//						$("#divEnabled").addClass("enabled");
//						$("#divEnabled2").removeClass("disabled");
//						$("#divEnabled2").addClass("enabled");
						$rootScope.stopLoader();
						$state.go('packageWithCAP');
					}
				else if(response.data.status == "error")
					{
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
				tempPackgeObj = {};
				$scope.packageObjResponse={};
				//$scope.initPackageObj();
				
				$scope.selectedPaymentEntitlementList=[];
				$scope.selectedPatientTypeList=[];
				$scope.selectedBedBillingCategoryList=[];
				$scope.selectedVisitTypeList=[];
			    $scope.packageObjResponse= response.data.object;
			    console.log("packageObjResponse",$scope.packageObjResponse);
			    $scope.packageObj.sexId = $scope.packageObjResponse.parentPackageMasterDto.sexId;
			    if(!importFrom){
			    $scope.packageService = $scope.packageObjResponse.parentPackageMasterDto.packageName;
                $scope.packageObj.serviceId = $scope.packageObjResponse.parentPackageMasterDto.serviceId;
                }
			    tempPackgeObj= angular.copy($scope.packageObjResponse);
			    angular.forEach($scope.packageObjResponse.paymentEntitlement,function(paymentEnt,indexPaymentEnt){
					
					angular.forEach(paymentEnt.patientCategoryList, function(patientCat,indexPatientCat){

						angular.forEach(patientCat.billingBedCategoryList,function(billingBedCat,indexBillingBedCat){		
							$scope.packageObj.tariffId = billingBedCat.mPackageMasterDto.tariffId;
							
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
			   $scope.calculateSummaryAmt();
			   $scope.crateClone();
				$rootScope.stopLoader();
			}	
			else if (response.data.status == "error"){
				$rootScope.stopLoader();
			}
			$scope.packDetailsObj={};
		});
		
	}
	
	
	//cap change logic
	/*$scope.removegroupWiseCapping = function(deptId,productCategoryId)
	{
		  angular.forEach($scope.packageObjResponse.paymentEntitlement,function(paymentEnt,indexPaymentEnt)
				{
				angular.forEach(paymentEnt.patientCategoryList, function(patientCat,indexPatientCat){
					angular.forEach(patientCat.billingBedCategoryList,function(billingBedCat,indexBillingBedCat)
							{	
						     if(deptId>0)
						    	 {
						    	     if(billingBedCat.mPackageMasterDto.listTPackageServicesDetailsDto!=null && billingBedCat.mPackageMasterDto.listTPackageServicesDetailsDto.length>0)
						    	    	{
						    	         angular.forEach(billingBedCat.mPackageMasterDto.listTPackageServicesDetailsDto,function(serviceDtls,indexServiceDtls)
										    {
											  if(serviceDtls.specialityId==deptId)
											  {
//												  serviceDtls.isCap=1;
												  serviceDtls.isCap=0;
											  }
										    });
						    	    	 }
						    	 }
						     else if(productCategoryId>0)
						    	 {
						    	  if(billingBedCat.mPackageMasterDto.listTPackageServicesDetailsDto!=null && billingBedCat.mPackageMasterDto.listTPackageServicesDetailsDto.length>0)
					    	    	 {
						    		  angular.forEach(billingBedCat.mPackageMasterDto.listTPackageServicesDetailsDto,function(serviceDtls,indexServiceDtls)
											    {
							    	        	  if(service.productCategoryId==productCategoryId);
												  {
													  serviceDtls.isCap=0;
												  }
											    });
					    	    	 }
						    	 }
				      });
		        });
          });
	}*/
	$scope.removegroupWiseCapping = function()
	{
		  angular.forEach($scope.packageObjResponse.paymentEntitlement,function(paymentEnt,indexPaymentEnt)
				{
				angular.forEach(paymentEnt.patientCategoryList, function(patientCat,indexPatientCat){
					angular.forEach(patientCat.billingBedCategoryList,function(billingBedCat,indexBillingBedCat)
							{		
						// checking group list has data or not
							if(billingBedCat.mPackageMasterDto.listTPackageGroupWiseCapDetailsDto!=null && billingBedCat.mPackageMasterDto.listTPackageGroupWiseCapDetailsDto.length>0){
								angular.forEach(billingBedCat.mPackageMasterDto.listTPackageGroupWiseCapDetailsDto,function(grpDtls,indexGrpDtls)
										{										     
											// checking service list has data or not
											if(billingBedCat.mPackageMasterDto.listTPackageServicesDetailsDto!=null && billingBedCat.mPackageMasterDto.listTPackageServicesDetailsDto.length>0){
													angular.forEach(billingBedCat.mPackageMasterDto.listTPackageServicesDetailsDto,function(serviceDtls,indexServiceDtls)
														    {
															  if((grpDtls.subDepartmentId=='' || grpDtls.subDepartmentId==undefined) && grpDtls.departmentId==serviceDtls.specialityId)
															  {
																  serviceDtls.isCap=0;
															  }else if(serviceDtls.subSpecialityId==grpDtls.subDepartmentId){
																  serviceDtls.isCap=0;
															  }
												});
											}
									
							      });
							}
				      });
		        });
          });
	}
	
	
	/*$scope.addGroupwiseCapping = function(deptId,productCategoryId)
	{
		 angular.forEach($scope.packageObjResponse.paymentEntitlement,function(paymentEnt,indexPaymentEnt)
					{
					angular.forEach(paymentEnt.patientCategoryList, function(patientCat,indexPatientCat){
						angular.forEach(patientCat.billingBedCategoryList,function(billingBedCat,indexBillingBedCat)
								{	
							
							     if(deptId>0)
							    	 {
							    	     if(billingBedCat.mPackageMasterDto.listTPackageServicesDetailsDto!=null && billingBedCat.mPackageMasterDto.listTPackageServicesDetailsDto.length>0)
							    	    	{
							    	         angular.forEach(billingBedCat.mPackageMasterDto.listTPackageServicesDetailsDto,function(serviceDtls,indexServiceDtls)
											    {
							    	        	 if(serviceDtls.specialityId==deptId)
												  {
//													  serviceDtls.isCap=0;
							    	        		  serviceDtls.isCap=1;
												  }
											    });
							    	    	 }
							   	  
							    	 }
							     else if(productCategoryId>0)
							    	 {
							    	  if(billingBedCat.mPackageMasterDto.listTPackageServicesDetailsDto!=null && billingBedCat.mPackageMasterDto.listTPackageServicesDetailsDto.length>0)
						    	    	 {
							    		  angular.forEach(billingBedCat.mPackageMasterDto.listTPackageServicesDetailsDto,function(serviceDtls,indexServiceDtls)
												    {
									    			  if(service.productCategoryId==productCategoryId);
													  {
														  serviceDtls.isCap=1;
													  }
												    });
						    	    	 }
							    	 }
					      });
			        });
	          });
	}*/
	$scope.addGroupwiseCapping = function()
	{
		 angular.forEach($scope.packageObjResponse.paymentEntitlement,function(paymentEnt,indexPaymentEnt)
					{
					angular.forEach(paymentEnt.patientCategoryList, function(patientCat,indexPatientCat){
						angular.forEach(patientCat.billingBedCategoryList,function(billingBedCat,indexBillingBedCat)
								{		
							// checking group list has data or not
								if(billingBedCat.mPackageMasterDto.listTPackageGroupWiseCapDetailsDto!=null && billingBedCat.mPackageMasterDto.listTPackageGroupWiseCapDetailsDto.length>0){
									angular.forEach(billingBedCat.mPackageMasterDto.listTPackageGroupWiseCapDetailsDto,function(grpDtls,indexGrpDtls)
											{										     
												// checking service list has data or not
												if(billingBedCat.mPackageMasterDto.listTPackageServicesDetailsDto!=null && billingBedCat.mPackageMasterDto.listTPackageServicesDetailsDto.length>0){
														angular.forEach(billingBedCat.mPackageMasterDto.listTPackageServicesDetailsDto,function(serviceDtls,indexServiceDtls)
															    {
																  if((grpDtls.subDepartmentId=='' || grpDtls.subDepartmentId==undefined) && grpDtls.departmentId==serviceDtls.specialityId)
																  {
																	  serviceDtls.isCap=1;
																  }else if(serviceDtls.subSpecialityId==grpDtls.subDepartmentId){
																	  serviceDtls.isCap=1;
																  }
													});
												}
										
								      });
								}else{
									if(billingBedCat.mPackageMasterDto.listTPackageServicesDetailsDto!=null && billingBedCat.mPackageMasterDto.listTPackageServicesDetailsDto.length>0){
										angular.forEach(billingBedCat.mPackageMasterDto.listTPackageServicesDetailsDto,function(serviceDtls,indexServiceDtls)
											    {
											     serviceDtls.isCap=1;
									});
								  }
								}
					      });
			        });
	          });
	}
	
	
	
	//==================PACKAGE WITH CAP GROUP DETAILS START==============================//
	
	$scope.addGroupDetails = function()
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
		if($scope.speciality.specialityId == '' || typeof($scope.speciality.specialityId) == "undefined"){
			growl.error("Please select a group",{
				title : ERROR_MSG
			});
			return;
		}
		
//		$("#divEnabled").removeClass("enabled");
//		$("#divEnabled").addClass("disabled");
//
//		$("#divEnabled2").removeClass("enabled");
//		$("#divEnabled2").addClass("disabled");
		
		 if(angular.equals({}, $scope.packageObjResponse) || $scope.packageObjResponse==null)
		 {
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
							packagetypeId:3,orgId:$scope.orgId,unitId:$scope.unitId,
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
												angular.forEach(patientCat.billingBedCategoryList,function(billingBedCat,indexBillingBedCat){								
													var departmentCapAmount = '';
													var subDepartmentCapAmount = '';
													if($scope.subSpeciality.subSpecialityId != '' && typeof($scope.subSpeciality.subSpecialityId) != "undefined")
														subDepartmentCapAmount = $rootScope.parseFloatNum(0);
													else
														departmentCapAmount = $rootScope.parseFloatNum(0);
													
													var temp = {departmentId:$scope.speciality.specialityId, subDepartmentId:$scope.subSpeciality.subSpecialityId,departmentCapAmount:departmentCapAmount,subDepartmentCapAmount:subDepartmentCapAmount,
															  groupName:$scope.speciality.specialityName,subGroupName:$scope.subSpeciality.subSpecialityName};
													
													if(billingBedCat.mPackageMasterDto.listTPackageGroupWiseCapDetailsDto==null)
														billingBedCat.mPackageMasterDto.listTPackageGroupWiseCapDetailsDto = [];
													
												/*	if(temp.subDepartmentId>0)
														{
														  var result = billingBedCat.mPackageMasterDto.listTPackageGroupWiseCapDetailsDto.find( (service) => (service.departmentId==temp.departmentId && service.subDepartmentId==temp.subDepartmentId));
														  if(result==undefined)
															  {
															    billingBedCat.mPackageMasterDto.listTPackageGroupWiseCapDetailsDto.push(temp);
															  }
														}
													else if(temp.subDepartmentId==null||temp.subDepartmentId=="")
														{
														  var result = billingBedCat.mPackageMasterDto.listTPackageGroupWiseCapDetailsDto.find( (service) => (service.departmentId==temp.departmentId && service.subDepartmentId==''));
														  if(result==undefined)
														  {
														    billingBedCat.mPackageMasterDto.listTPackageGroupWiseCapDetailsDto.push(temp);
														  }
														}*/
													if(temp.subDepartmentId>0)
													{
													//for checking sub-sepcaility exist or not
													  var subSpecialityChk = billingBedCat.mPackageMasterDto.listTPackageGroupWiseCapDetailsDto.find( (service) => (service.departmentId==temp.departmentId && service.subDepartmentId==temp.subDepartmentId));
													//for checking sepcaility exist or not
													  var specialityChk = billingBedCat.mPackageMasterDto.listTPackageGroupWiseCapDetailsDto.find( (service) => (service.departmentId==temp.departmentId && service.subDepartmentId==''));
														 if(specialityChk!=undefined){
															 growl.warning("Speciality or sub sepeciality already added!",{title : 'Warning!'});
																return false;
														 }else{															 
															 if(subSpecialityChk==undefined)
															  {
															    billingBedCat.mPackageMasterDto.listTPackageGroupWiseCapDetailsDto.push(temp);
															  }
														 }
													 
													}
												else if(temp.subDepartmentId==null||temp.subDepartmentId=="")
													{
													//for checking sub-sepcaility exist or not
													  var subSpecialityChk = billingBedCat.mPackageMasterDto.listTPackageGroupWiseCapDetailsDto.find( (service) => (service.departmentId==temp.departmentId));
													//for checking sepcaility exist or not
													  var specialityChk = billingBedCat.mPackageMasterDto.listTPackageGroupWiseCapDetailsDto.find( (service) => (service.departmentId==temp.departmentId && service.subDepartmentId==''));
													  if(subSpecialityChk!=undefined){
														  growl.warning("Speciality or sub sepeciality already added!",{title : 'Warning!'});
															return false;
													  }else{
														  if(specialityChk==undefined)
														  {
														    billingBedCat.mPackageMasterDto.listTPackageGroupWiseCapDetailsDto.push(temp);
														  } 
													  }
													  
													}
//													$scope.removegroupWiseCapping(temp.departmentId,0);
													$scope.removegroupWiseCapping();
											});
									  });
				                 });
									 	$scope.calculateSummaryAmt();
									 	$scope.crateClone();
										$rootScope.stopLoader();
										$scope.initSubSpeciality();
										$scope.initSpeciality();
					           }
					           else  if(response.data.status=="error")
					        	   {
					        	     $rootScope.stopLoader();
					        	   }
						});
		  }
		 else
		 {
			 angular.forEach($scope.packageObjResponse.paymentEntitlement,function(paymentEnt,indexPaymentEnt){
					angular.forEach(paymentEnt.patientCategoryList, function(patientCat,indexPatientCat){
						angular.forEach(patientCat.billingBedCategoryList,function(billingBedCat,indexBillingBedCat){		
							var departmentCapAmount = '';
							var subDepartmentCapAmount = '';
							if($scope.subSpeciality.subSpecialityId != '' && typeof($scope.subSpeciality.subSpecialityId) != "undefined")
								subDepartmentCapAmount = $rootScope.parseFloatNum(0);
							else
								departmentCapAmount = $rootScope.parseFloatNum(0);
							
							var temp = {departmentId:$scope.speciality.specialityId, subDepartmentId:$scope.subSpeciality.subSpecialityId,departmentCapAmount:departmentCapAmount,subDepartmentCapAmount:subDepartmentCapAmount,
									groupName:$scope.speciality.specialityName,subGroupName:$scope.subSpeciality.subSpecialityName};
							
							if(billingBedCat.mPackageMasterDto.listTPackageGroupWiseCapDetailsDto==null)
								billingBedCat.mPackageMasterDto.listTPackageGroupWiseCapDetailsDto = [];
							
							/*if(temp.subDepartmentId>0)
							{
							  var result = billingBedCat.mPackageMasterDto.listTPackageGroupWiseCapDetailsDto.find( (service) => (service.departmentId==temp.departmentId && service.subDepartmentId==temp.subDepartmentId));
							  if(result==undefined)
								  {
								    billingBedCat.mPackageMasterDto.listTPackageGroupWiseCapDetailsDto.push(temp);
								  }
							}
						 else if(temp.subDepartmentId==null||temp.subDepartmentId=="")
							{
							  var result = billingBedCat.mPackageMasterDto.listTPackageGroupWiseCapDetailsDto.find( (service) => (service.departmentId==temp.departmentId && service.subDepartmentId==''));
							  if(result==undefined)
							  {
							    billingBedCat.mPackageMasterDto.listTPackageGroupWiseCapDetailsDto.push(temp);
							  }
							}*/
							if(temp.subDepartmentId>0)
							{
							//for checking sub-sepcaility exist or not
							  var subSpecialityChk = billingBedCat.mPackageMasterDto.listTPackageGroupWiseCapDetailsDto.find( (service) => (service.departmentId==temp.departmentId && service.subDepartmentId==temp.subDepartmentId));
							//for checking sepcaility exist or not
							  var specialityChk = billingBedCat.mPackageMasterDto.listTPackageGroupWiseCapDetailsDto.find( (service) => (service.departmentId==temp.departmentId && service.subDepartmentId==''));
								 if(specialityChk!=undefined){
									 growl.warning("Speciality or sub sepeciality already added!",{title : 'Warning!'});
										return false;
								 }else{															 
									 if(subSpecialityChk==undefined)
									  {
									    billingBedCat.mPackageMasterDto.listTPackageGroupWiseCapDetailsDto.push(temp);
									  }
								 }
							 
							}
						else if(temp.subDepartmentId==null||temp.subDepartmentId=="")
							{
							//for checking sub-sepcaility exist or not
							  var subSpecialityChk = billingBedCat.mPackageMasterDto.listTPackageGroupWiseCapDetailsDto.find( (service) => (service.departmentId==temp.departmentId));
							//for checking sepcaility exist or not
							  var specialityChk = billingBedCat.mPackageMasterDto.listTPackageGroupWiseCapDetailsDto.find( (service) => (service.departmentId==temp.departmentId && service.subDepartmentId==''));
							  if(subSpecialityChk!=undefined){
								  growl.warning("Speciality or sub sepeciality already added!",{title : 'Warning!'});
									return false;
							  }else{
								  if(specialityChk==undefined)
								  {
								    billingBedCat.mPackageMasterDto.listTPackageGroupWiseCapDetailsDto.push(temp);
								  } 
							  }
							  
							}
//							$scope.removegroupWiseCapping(temp.departmentId,0);
							$scope.removegroupWiseCapping();
						});	
					});
				});
			 $scope.calculateSummaryAmt();
			$rootScope.stopLoader();
			$scope.initSubSpeciality();
			$scope.initSpeciality();
		 }
	}
	//==================PACKAGE WITH CAP GROUP DETAILS END==============================//
	
	//==============BED CATEGORY START====================================//
	
	$scope.bedCatName = function(id){
		angular.forEach($scope.bedCategoryList,function(value,index){
			if(value.bedCategoryId == id){
				$scope.bedCategoryName = value.bedCategoryDesc;
			}
		});
	}
	
	$scope.addBedCategory = function()
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
		
		if($scope.bedCategory.bedCategoryId != ""){
			$scope.bedCatName($scope.bedCategory.bedCategoryId);
			if(angular.equals({}, $scope.packageObjResponse) || $scope.packageObjResponse==null)
			 {
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
								packagetypeId:3,orgId:$scope.orgId,unitId:$scope.unitId,
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
														var obj = {bedCategoryId:$scope.bedCategory.bedCategoryId,applicableDays:0,bedCategoryDesc:$scope.bedCategoryName};
														if(billingBedCat.mPackageMasterDto.listTPackageBedCategoryDetailDto==null)
															billingBedCat.mPackageMasterDto.listTPackageBedCategoryDetailDto = [];
														 billingBedCat.mPackageMasterDto.listTPackageBedCategoryDetailDto.push(obj);
												});
										  });
					                 });
										 	$scope.calculateSummaryAmt();
											$rootScope.stopLoader();
						           }
						           else  if(response.data.status=="error")
						        	   {
						        	     $rootScope.stopLoader();
						        	   }
							});
			  }
			 else
			 {
				 angular.forEach($scope.packageObjResponse.paymentEntitlement,function(paymentEnt,indexPaymentEnt){
						angular.forEach(paymentEnt.patientCategoryList, function(patientCat,indexPatientCat){
							angular.forEach(patientCat.billingBedCategoryList,function(billingBedCat,indexBillingBedCat){		
								var obj = {bedCategoryId:$scope.bedCategory.bedCategoryId,applicableDays:0,bedCategoryDesc:$scope.bedCategoryName};
								if(billingBedCat.mPackageMasterDto.listTPackageBedCategoryDetailDto==null)
									billingBedCat.mPackageMasterDto.listTPackageBedCategoryDetailDto = [];
								 billingBedCat.mPackageMasterDto.listTPackageBedCategoryDetailDto.push(obj);
							});	
						});
					});
				 $scope.calculateSummaryAmt();
				 $scope.crateClone();
				$rootScope.stopLoader();
			 }
		}else{
			growl.error("Please select bed category",{
				title : ERROR_MSG
			});
		}
	}
	
	//==============BED CATEGORY END====================================//
	
	
//==================PACKAGE WITH CATEGORY WISE CONSUMABLE PRODUCT START==============================//
	
	$scope.addCategoryWiseConsumableProduct = function(obj)
	{
		
		  // validation
		if($scope.packageObj.serviceId=="" || $scope.packageObj.serviceId==undefined){
			growl.warning("Please select package service!",{title : 'Warning!'});
			return false;
		}
		if($scope.packageObj.tariffId==undefined || $scope.packageObj.tariffId==''){
			growl.warning("Please Select tariff!",{title : 'Warning!'});
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
		
		if(obj==undefined ||(obj.assetCategoryId == '' || obj.assetCategoryId ==undefined )||( obj.productCategoryId=='' ||  obj.productCategoryId==undefined)){
			growl.warning("Please select asset and product category",{
				title : "warning !"
			});
			return;
		}

//		$("#divEnabled").removeClass("enabled");
//		$("#divEnabled").addClass("disabled");
//
//		$("#divEnabled2").removeClass("enabled");
//		$("#divEnabled2").addClass("disabled");
		 if(angular.equals({}, $scope.packageObjResponse) || $scope.packageObjResponse==null)
		 {
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
							packagetypeId:3,orgId:$scope.orgId,unitId:$scope.unitId,
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
												angular.forEach(patientCat.billingBedCategoryList,function(billingBedCat,indexBillingBedCat){	
													angular.forEach($scope.productCategoryList,function(value,index){
														if(value.id == obj.productCategoryId)
															productCategory = value.category;
													});
													var temp = {productCategoryId:obj.productCategoryId, productCategory:productCategory, numberToBeUse:0};
													
													if(billingBedCat.mPackageMasterDto.listTPackageCategoryWiseConsumableDetailsDto==null)
														billingBedCat.mPackageMasterDto.listTPackageCategoryWiseConsumableDetailsDto = [];
													
													var result = billingBedCat.mPackageMasterDto.listTPackageCategoryWiseConsumableDetailsDto.find( (item) => (item.productCategoryId==temp.productCategoryId));
													  if(result==undefined)
													  {
													    billingBedCat.mPackageMasterDto.listTPackageCategoryWiseConsumableDetailsDto.push(temp);
													  }
											});
									   });
				                  });
									 	$scope.calculateSummaryAmt();
										$rootScope.stopLoader();
										
					           }
					           else  if(response.data.status=="error")
					        	   {
					        	     $rootScope.stopLoader();
					        	   }
						});
		  }
		 else
		 {
			 angular.forEach($scope.packageObjResponse.paymentEntitlement,function(paymentEnt,indexPaymentEnt){
					angular.forEach(paymentEnt.patientCategoryList, function(patientCat,indexPatientCat){
						angular.forEach(patientCat.billingBedCategoryList,function(billingBedCat,indexBillingBedCat){
							
							angular.forEach($scope.productCategoryList,function(value,index){
								if(value.id == obj.productCategoryId)
									productCategory = value.category;
							});
							var temp = {productCategoryId:obj.productCategoryId, productCategory:productCategory, numberToBeUse:0};
							if(billingBedCat.mPackageMasterDto.listTPackageCategoryWiseConsumableDetailsDto==null)
								billingBedCat.mPackageMasterDto.listTPackageCategoryWiseConsumableDetailsDto = [];
							
							var result = billingBedCat.mPackageMasterDto.listTPackageCategoryWiseConsumableDetailsDto.find( (item) => (item.productCategoryId==temp.productCategoryId));
							  if(result==undefined)
							  {
							    billingBedCat.mPackageMasterDto.listTPackageCategoryWiseConsumableDetailsDto.push(temp);
							  }
						});	
					});
				});
			$rootScope.stopLoader();
		 }
	}
	//==================PACKAGE WITH  CATEGORY WISE CONSUMABLE PRODUCT END==============================//
	
//==================PACKAGE WITH CONSUMABLE WISE AMOUNT START==============================//
	
	
	$scope.addConsumableWiseProduct = function(obj)
	{
		
		  // validation
		if($scope.packageObj.serviceId=="" || $scope.packageObj.serviceId==undefined){
			growl.warning("Please select package service!",{title : 'Warning!'});
			return false;
		}
		if($scope.packageObj.tariffId==undefined || $scope.packageObj.tariffId==''){
			growl.warning("Please Select tariff!",{title : 'Warning!'});
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
		
		if(obj==undefined ||(obj.assetCategoryId == '' || obj.assetCategoryId ==undefined )||( obj.productCategoryId=='' ||  obj.productCategoryId==undefined)){
			growl.warning("Please select asset and product category",{
				title : "warning !"
			});
			return;
		}

//		$("#divEnabled").removeClass("enabled");
//		$("#divEnabled").addClass("disabled");
//
//		$("#divEnabled2").removeClass("enabled");
//		$("#divEnabled2").addClass("disabled");
		
		if(obj==undefined ||(obj.assetCategoryId == '' || obj.assetCategoryId ==undefined )||( obj.productCategoryId=='' ||  obj.productCategoryId==undefined)){
			growl.warning("Please select asset and product category",{
				title : "warning !"
			});
			return;
		}
		 if(angular.equals({}, $scope.packageObjResponse) || $scope.packageObjResponse==null)
		 {
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
							packagetypeId:3,orgId:$scope.orgId,unitId:$scope.unitId,
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
												angular.forEach(patientCat.billingBedCategoryList,function(billingBedCat,indexBillingBedCat){	
													angular.forEach($scope.productCategoryList,function(value,index){
														if(value.id == obj.productCategoryId)
															productCategory = value.category;
													});
													var temp = {productCategoryId:obj.productCategoryId, productCategory:productCategory, productCateroyCapAmount:0};
													
													if(billingBedCat.mPackageMasterDto.listTPackageConsumableWiseCapDetailsDto==null)
														billingBedCat.mPackageMasterDto.listTPackageConsumableWiseCapDetailsDto = [];
													billingBedCat.mPackageMasterDto.listTPackageConsumableWiseCapDetailsDto.push(temp);
													
													var result = billingBedCat.mPackageMasterDto.listTPackageConsumableWiseCapDetailsDto.find( (item) => (item.productCategoryId==temp.productCategoryId));
													  if(result==undefined)
													  {
													    billingBedCat.mPackageMasterDto.listTPackageConsumableWiseCapDetailsDto.push(temp);
													  }
//													  $scope.removegroupWiseCapping(0,temp.productCategoryId);
													  $scope.removegroupWiseCapping();
											});
									   });
				                  });
									 	$scope.calculateSummaryAmt();
									 	$scope.crateClone();
										$rootScope.stopLoader();
										
					           }
					           else  if(response.data.status=="error")
					        	   {
					        	     $rootScope.stopLoader();
					        	   }
						});
		  }
		 else
		 {
			 angular.forEach($scope.packageObjResponse.paymentEntitlement,function(paymentEnt,indexPaymentEnt){
					angular.forEach(paymentEnt.patientCategoryList, function(patientCat,indexPatientCat){
						angular.forEach(patientCat.billingBedCategoryList,function(billingBedCat,indexBillingBedCat){
							
							angular.forEach($scope.productCategoryList,function(value,index){
								if(value.id == obj.productCategoryId)
									productCategory = value.category;
							});
							var temp = {productCategoryId:obj.productCategoryId, productCategory:productCategory, productCateroyCapAmount:0};
							if(billingBedCat.mPackageMasterDto.listTPackageConsumableWiseCapDetailsDto==null)
								billingBedCat.mPackageMasterDto.listTPackageConsumableWiseCapDetailsDto = [];

							var result = billingBedCat.mPackageMasterDto.listTPackageConsumableWiseCapDetailsDto.find( (item) => (item.productCategoryId==temp.productCategoryId));
							  if(result==undefined)
							  {
							    billingBedCat.mPackageMasterDto.listTPackageConsumableWiseCapDetailsDto.push(temp);
							  }
							
						});	
					});
				});
			 $scope.calculateSummaryAmt();
			$rootScope.stopLoader();
		 }
	};
	//==================PACKAGE WITH   CONSUMABLE WISE AMOUNT  END==============================//
	
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
	}
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
	
	
	
	
	$scope.getEitherOrService = function()
	{
		$scope.searchServiceList = [];
		$scope.searchServObj = {};
		
		if($scope.packageObj.serviceId=="" || $scope.packageObj.serviceId==undefined){
			growl.warning("Please select package service!",{title : 'Warning!'});
			return false;
		}
		if($scope.packageObj.tariffId==undefined || $scope.packageObj.tariffId==''){
			growl.warning("Please Select tariff!",{title : 'Warning!'});
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
		
		 
		// checking the group added or not start
		var showFlag=true;
		$scope.grpList=[];
		
		if(angular.equals({}, $scope.packageObjResponse) ){
			  showFlag=false;
				growl.warning("Please add group or sub-group!",{title : 'Warning!'});
				return false;
		 }
		 angular.forEach($scope.packageObjResponse.paymentEntitlement,function(paymentEnt,indexPaymentEnt)
				{
				angular.forEach(paymentEnt.patientCategoryList, function(patientCat,indexPatientCat){
					angular.forEach(patientCat.billingBedCategoryList,function(billingBedCat,indexBillingBedCat)
							{	
								if(billingBedCat.mPackageMasterDto.listTPackageGroupWiseCapDetailsDto!=null && billingBedCat.mPackageMasterDto.listTPackageGroupWiseCapDetailsDto!=undefined && billingBedCat.mPackageMasterDto.listTPackageGroupWiseCapDetailsDto.length>0){
									angular.forEach(billingBedCat.mPackageMasterDto.listTPackageGroupWiseCapDetailsDto ,function(grpDtls,indexGrpDtls)
											  {
													  var found = $scope.grpList.find(function(grp) { 
														  return grp.departmentId == grpDtls.departmentId; 
														});
											           if(found==undefined || found.length==0)
											        	{
											        	   var data={
											        			   departmentId:grpDtls.departmentId,
											        			   specialityName:grpDtls.groupName
											        	   }
											        	   $scope.grpList.push(data);
											        	   
											        	}
									          });
								}else{
									showFlag=false;
									growl.warning("Please add group or sub-group!",{title : 'Warning!'});
									return false;
								}
							  
					      });	
				   });
			 });
			// checking the group added or not end
		if(showFlag){
//			$scope.specialityListTemp = angular.copy($scope.specialityList);
			$scope.specialityList=[];
			angular.forEach($scope.grpList ,function(grpDtls,indexGrpDtls)
					  {				
							angular.forEach($scope.specialityGroupList ,function(speDtls,indexSpeDtls)
									  {
											  if(grpDtls.departmentId==speDtls.specialityId){
												  $scope.specialityList.push(speDtls);
											  }
								           
							          });
			          });
			$('#packageWithCapServicesModal').modal('show');	
			
			/*$("#divEnabled").removeClass("enabled");
			$("#divEnabled").addClass("disabled");
			
			$("#divEnabled2").removeClass("enabled");
			$("#divEnabled2").addClass("disabled");*/
		}
		
	}
	
	/* first level */
	$scope.firstTabLevel = 1 ;
	$scope.setFirstTabLevel= function(newTab)
	{
		$scope.firstTabLevel = newTab;
	};
	$scope.isSetFirstTabLevel = function(tabNum)
	{
		return $scope.firstTabLevel === tabNum;
	};
	
	/*Second Level*/
	$scope.secondTabLevel = 1 ;

	$scope.setSecondTabLevel = function(newTab)
	{
		$scope.secondTabLevel = newTab;
	};

	$scope.isSetSecondTabLevel = function(tabNum)
	{
		return $scope.secondTabLevel === tabNum;
	};
	
	
	
	//Code for remove itesm from list
	
	
	//1.service
	//2.Item
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
		  $scope.calculateSummaryAmt();
	}
	
	 


	//tab 2.1
	$scope.removeGroups = function(departmentId)
	{
		angular.forEach($scope.packageObjResponse.paymentEntitlement,function(paymentEnt,indexPaymentEnt)
			{
			angular.forEach(paymentEnt.patientCategoryList, function(patientCat,indexPatientCat){
				angular.forEach(patientCat.billingBedCategoryList,function(billingBedCat,indexBillingBedCat)
						{		
							// for removing group details
						  angular.forEach(billingBedCat.mPackageMasterDto.listTPackageGroupWiseCapDetailsDto,function(groupDtls,indexgroupDtls)
								  {
							      if(groupDtls.departmentId==departmentId)
							    	 {
							    	   billingBedCat.mPackageMasterDto.listTPackageGroupWiseCapDetailsDto.splice(indexgroupDtls, 1);  
							    	 }
						          });
						  
						  //for removing exclusion
						  if(billingBedCat.mPackageMasterDto.listGroupWiseIncExc!=null && billingBedCat.mPackageMasterDto.listGroupWiseIncExc.length>0){
							  angular.forEach(billingBedCat.mPackageMasterDto.listGroupWiseIncExc,function(exDtls,indexExDtls)
									  {
								      if(exDtls.specialityId==departmentId)
								    	 {
								    	   billingBedCat.mPackageMasterDto.listGroupWiseIncExc.splice(indexExDtls, 1);  
								    	 }
							          });
						  }
						   
				       });	
			   });
		 });
//		 $scope.addGroupwiseCapping(departmentId,0);
		$scope.addGroupwiseCapping();
		tempPackgeObj= angular.copy($scope.packageObjResponse);
		 $scope.calculateSummaryAmt();
	}
	
	
	//tab 2.1//2.2
	$scope.removeServiceInclusionExlusion = function(serviceId,isInclusiveService)
	{
		angular.forEach($scope.packageObjResponse.paymentEntitlement,function(paymentEnt,indexPaymentEnt)
			{
			angular.forEach(paymentEnt.patientCategoryList, function(patientCat,indexPatientCat){
				angular.forEach(patientCat.billingBedCategoryList,function(billingBedCat,indexBillingBedCat)
						{								
						  angular.forEach(billingBedCat.mPackageMasterDto.listGroupWiseIncExc,function(inExDtls,indexinEx)
								  {
							      if(inExDtls.serviceId==serviceId && inExDtls.isInclusiveService==isInclusiveService)
							    	 {
							    	   billingBedCat.mPackageMasterDto.listGroupWiseIncExc.splice(indexinEx, 1);  
							    	 }
						          });
				      });	
			   });
		 });
		tempPackgeObj= angular.copy($scope.packageObjResponse);
		$scope.calculateSummaryAmt();
	}
	
	//tab 2.1//2.2
	$scope.removeBedCategory = function(bedCategoryId)
	{
		angular.forEach($scope.packageObjResponse.paymentEntitlement,function(paymentEnt,indexPaymentEnt)
			{
			angular.forEach(paymentEnt.patientCategoryList, function(patientCat,indexPatientCat){
				angular.forEach(patientCat.billingBedCategoryList,function(billingBedCat,indexBillingBedCat)
						{								
						  angular.forEach(billingBedCat.mPackageMasterDto.listTPackageBedCategoryDetailDto,function(bedCategory,bedCategoryIndex)
								  {
							      if(bedCategory.bedCategoryId==bedCategoryId)
							    	 {
							    	   billingBedCat.mPackageMasterDto.listTPackageBedCategoryDetailDto.splice(bedCategoryIndex, 1);  
							    	 }
						          });
				      });	
			   });
		 });
		tempPackgeObj= angular.copy($scope.packageObjResponse);
		$scope.calculateSummaryAmt();
	};
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
		$scope.calculateSummaryAmt();
	}
	
	$scope.removeCategoryWiseConsumables = function(categoryId)
	{
		angular.forEach($scope.packageObjResponse.paymentEntitlement,function(paymentEnt,indexPaymentEnt)
			{
			angular.forEach(paymentEnt.patientCategoryList, function(patientCat,indexPatientCat){
				angular.forEach(patientCat.billingBedCategoryList,function(billingBedCat,indexBillingBedCat)
						{								
						  angular.forEach(billingBedCat.mPackageMasterDto.listTPackageCategoryWiseConsumableDetailsDto,function(categoryConsumable,index)
								  {
							      if(categoryConsumable.categoryId==categoryId)
							    	 {
							    	   billingBedCat.mPackageMasterDto.listTPackageCategoryWiseConsumableDetailsDto.splice(index, 1);  
							    	 }
						          });
				      });	
			   });
		 });
		$scope.calculateSummaryAmt();
	};
	
	$scope.consumableWiseAmount = function(categoryId)
	{
		angular.forEach($scope.packageObjResponse.paymentEntitlement,function(paymentEnt,indexPaymentEnt)
			{
			angular.forEach(paymentEnt.patientCategoryList, function(patientCat,indexPatientCat){
				angular.forEach(patientCat.billingBedCategoryList,function(billingBedCat,indexBillingBedCat)
						{								
						  angular.forEach(billingBedCat.mPackageMasterDto.listTPackageConsumableWiseCapDetailsDto,function(consumable,index)
								  {
							      if(consumable.categoryId==categoryId)
							    	 {
							    	   billingBedCat.mPackageMasterDto.listTPackageConsumableWiseCapDetailsDto.splice(index, 1);  
							    	 }
						          });
				      });	
			   });
		 });
//		$scope.addGroupwiseCapping(0,categoryId);
		$scope.addGroupwiseCapping();
		tempPackgeObj= angular.copy($scope.packageObjResponse);
		$scope.calculateSummaryAmt();
	};
	
	//tab 6.1//6.2
	$scope.removeConsumableIncExclusion = function(itemId,isInclusiveService)
	{
		angular.forEach($scope.packageObjResponse.paymentEntitlement,function(paymentEnt,indexPaymentEnt)
			{
			angular.forEach(paymentEnt.patientCategoryList, function(patientCat,indexPatientCat){
				angular.forEach(patientCat.billingBedCategoryList,function(billingBedCat,indexBillingBedCat)
						{								
						  angular.forEach(billingBedCat.mPackageMasterDto.listConsumableWiseIncExc,function(inExDtls,index)
								  {
							      if(inExDtls.itemId==itemId && inExDtls.isInclusiveService==isInclusiveService)
							    	 {
							    	   billingBedCat.mPackageMasterDto.listConsumableWiseIncExc.splice(index, 1);  
							    	 }
						          });
				      });	
			   });
		 });
		tempPackgeObj= angular.copy($scope.packageObjResponse);
		$scope.calculateSummaryAmt();
	}
	
	$scope.checkPackagePrice = function(packageCost,packagePrice,pEntIndex,peIndex,bBedIndex)
	{
		/*if(Number(packageCost)>0 && Number(packagePrice)>0)
			{ 
			  if(Number(packageCost)> Number(packagePrice))
				  {
				    growl.warning("Package price should not be less than package cost.",{title : 'Warning!'});
				    $scope.packageObjResponse.paymentEntitlement[pEntIndex].patientCategoryList[peIndex].billingBedCategoryList[bBedIndex].mPackageMasterDto.packagePrice =packageCost; 
					return packageCost; 
				  }
			 
			} ------Removed by kaustubh for athayu, approved by vishal  */
		
		return packageCost; 
		
	}

	//	------------------edit package-------------------
    		if($stateParams.newPackageWithCAPObj!=undefined && $stateParams.newPackageWithCAPObj!=null && $stateParams.newPackageWithCAPObj.parentPackageId>0){
    		          $scope.packDetailsObj = {};
                      $scope.packDetailsObj.parentPackageId = $stateParams.newPackageWithCAPObj.parentPackageId;
                      $scope.parentPackageId = $stateParams.newPackageWithCAPObj.parentPackageId;
                      $scope.packDetailsObj.packageTypeId = 3;
                      $scope.getSelectedPackageServices(false);
        			}
        	$scope.editPackage = function(packageObjectFetch){
    			$rootScope.startLoader();
    			var visitTypeArray=[];
    			$scope.addNewServiceList = [];
    			packageObjectFetch.paymentEntitlement =$scope.paymentEntitlementList;
                packageObjectFetch.patientCategoryList = $scope.patientTypeList;
                packageObjectFetch.billingBedCategoryList =$scope.billingCategoryReference;
                arraList=cartesian($scope.selectedPaymentEntitlementList,$scope.selectedPatientTypeList,$scope.selectedBedBillingCategoryList);

                		for(i=0;i<arraList.length;i++){

                			var tempObj={
                					paymentEntitlementId:"",paymentEntitlement:"",
                					visitTypeId:"",visitTypeName:"",
                					patientTypeId:"",patientType:"",
                					billingBedCategoryId:"",billingBedCategory:"",
                					packagetypeId:3,orgId:$scope.orgId,unitId:$scope.unitId,
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
                				newTempObj.isServiceItem= 1;//isServiceItem;
                				newTempObj.isInclusiveService= 1; //isInclusiveServcie;
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
    				    $scope.packageObjResponse=  response.data.object==null?{}:response.data.object;
    				    $scope.packageObj.sexId   = $scope.packageObjResponse.parentPackageMasterDto.sexId;
    				    $scope.packageObj.tariffId  = $scope.packageObjResponse.parentPackageMasterDto.packageMasterDtoList[0].tariffId;
    				    $scope.packageService = $scope.packageObjResponse.parentPackageMasterDto.packageName;
                        $scope.packageObj.serviceId = $scope.packageObjResponse.parentPackageMasterDto.serviceId;
    				    tempPackgeObj= angular.copy($scope.packageObjResponse);
                        			    angular.forEach($scope.packageObjResponse.paymentEntitlement,function(paymentEnt,indexPaymentEnt){

                        					angular.forEach(paymentEnt.patientCategoryList, function(patientCat,indexPatientCat){

                        						angular.forEach(patientCat.billingBedCategoryList,function(billingBedCat,indexBillingBedCat){
                        							$scope.packageObj.tariffId = billingBedCat.mPackageMasterDto.tariffId;

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
                        			   $scope.calculateSummaryAmt();
                        			   $scope.crateClone();
                        			   $rootScope.stopLoader();
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
                  if(!angular.equals({}, $scope.packageObjResponse) && $scope.packageObjResponse.paymentEntitlement[0].patientCategoryList[0].billingBedCategoryList[0].mPackageMasterDto.listTPackageGroupWiseCapDetailsDto
                              !=null && $scope.packageObjResponse.paymentEntitlement[0].patientCategoryList[0].billingBedCategoryList[0].mPackageMasterDto.listTPackageGroupWiseCapDetailsDto.length>0)
                              {
                                 validate = true;
                              }
                   if(!angular.equals({}, $scope.packageObjResponse) && $scope.packageObjResponse.paymentEntitlement[0].patientCategoryList[0].billingBedCategoryList[0].mPackageMasterDto.listGroupWiseIncExc
                                                 !=null && $scope.packageObjResponse.paymentEntitlement[0].patientCategoryList[0].billingBedCategoryList[0].mPackageMasterDto.listGroupWiseIncExc.length>0)
                             {
                                validate = true;
                             }
                   if(!angular.equals({}, $scope.packageObjResponse) && $scope.packageObjResponse.paymentEntitlement[0].patientCategoryList[0].billingBedCategoryList[0].mPackageMasterDto.listTPackageBedCategoryDetailDto
                                                 !=null && $scope.packageObjResponse.paymentEntitlement[0].patientCategoryList[0].billingBedCategoryList[0].mPackageMasterDto.listTPackageBedCategoryDetailDto.length>0)
                             {
                                validate = true;
                             }
                   if(!angular.equals({}, $scope.packageObjResponse) && $scope.packageObjResponse.paymentEntitlement[0].patientCategoryList[0].billingBedCategoryList[0].mPackageMasterDto.listTPackageItemDetailsDto
                                                 !=null && $scope.packageObjResponse.paymentEntitlement[0].patientCategoryList[0].billingBedCategoryList[0].mPackageMasterDto.listTPackageItemDetailsDto.length>0)
                              {
                                                  validate = true;
                              }
                   if(!angular.equals({}, $scope.packageObjResponse) && $scope.packageObjResponse.paymentEntitlement[0].patientCategoryList[0].billingBedCategoryList[0].mPackageMasterDto.listTPackageConsumableWiseCapDetailsDto
                                         !=null && $scope.packageObjResponse.paymentEntitlement[0].patientCategoryList[0].billingBedCategoryList[0].mPackageMasterDto.listTPackageConsumableWiseCapDetailsDto.length>0)
                               {
                                                  validate = true;
                               }
                   if(!angular.equals({}, $scope.packageObjResponse) && $scope.packageObjResponse.paymentEntitlement[0].patientCategoryList[0].billingBedCategoryList[0].mPackageMasterDto.listConsumableWiseIncExc
                                   !=null && $scope.packageObjResponse.paymentEntitlement[0].patientCategoryList[0].billingBedCategoryList[0].mPackageMasterDto.listConsumableWiseIncExc.length>0)
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

