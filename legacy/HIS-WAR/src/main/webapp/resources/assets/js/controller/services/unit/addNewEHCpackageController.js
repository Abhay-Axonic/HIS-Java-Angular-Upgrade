angular.module("myApp").controller("addNewEHCpackageController", [ '$scope','$rootScope','BillingGenericService','$cookies','$state','growl','servicesFactory','GenericService','$stateParams',
	function($scope,$rootScope,BillingGenericService,$cookies,$state,growl,servicesFactory,GenericService,$stateParams){
	try{
		$rootScope.loginpage = true;
		this.angular = angular;
		var cookieObject = $cookies.getObject('cookieObject');
		if (cookieObject == undefined) {
			$state.go('login');
			return;
		}
		$scope.unitId = cookieObject.unitId;
		$scope.orgId = cookieObject.organizationId;
		$scope.userId = cookieObject.userId;
	
		
		var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
	    if (cookieObjectAppConfig == undefined) {
	        $state.go('login');
	        return;
	    } else {
	        console.log('cookieObjectAppConfig', cookieObjectAppConfig);
	    }
	    $scope.consultationSpecialityId = cookieObjectAppConfig.consultationSpecialityId;
	    $scope.minAge = cookieObjectAppConfig.minAge;
		$scope.maxAge = cookieObjectAppConfig.maxAge;
	    
	    $scope.opdBillingCatId = cookieObjectAppConfig.opdBillingCatId;
		$scope.init = function(){
			$scope.specialityId='';
			$scope.isMarkupDown=1;
			$scope.roundOffPositNeg = 0;
			$scope.markupDownInPercentage = 0;
			$scope.searchServiceList = [];
			$scope.specialityList = [];
			$scope.subSpecialityList = [];
			$scope.searchServObj = {};
			$scope.packageObj = {visitTypeId:"1", billingBedCategoryId: $scope.opdBillingCatId}
			servicesFactory.initServiceSearch($scope);
			$scope.selectedServiceList = [];
			$scope.initPackageObj();
			$scope.serviceWiseSummary = 0;
			$scope.grandTotal = 0;
			$scope.packageService = "";
			$scope.parentPackageId = null;
			$scope.selectedPaymentEntitlementList=[];
			$scope.selectedPatientTypeList=[];
			$scope.selectedBedBillingCategoryList=[];
			$scope.selectedVisitTypeList=[];
			$scope.packageObjResponse ={};
			
//			$("#savePackageId").removeClass("enabled");
//			$("#savePackageId").addClass("disabled");



		}
		$scope.initImportPackageObj = function(){
			$scope.importPkgObj = {
							packageTypeId:"1",
							packageCode:"",
							packageName:""	,
							orgId:$scope.orgId,
							unitId:$scope.unitId
						  }
		}
		$scope.initImportPackageObj();

		//service factory methods 
		$scope.searchServices = function(searchObj){servicesFactory.searchServices(searchObj,$scope)}
		$scope.getSubSpecialityBySpecialityId = function(specialityId){servicesFactory.getSubSpecialityBySpecialityId(specialityId,$scope)}
		$scope.selectAllServices = function(flag){servicesFactory.selectAllServices(flag,$scope)}
		$scope.setOrgUnit = function(orgId,unitId,scope){servicesFactory.setOrgUnit(orgId,unitId,$scope)}
		
		$scope.initPackageObj = function(){
			$scope.packageObj = {packageTypeId:"1",sexId:"",minAge:"",maxAge:"",tariffId:"",serviceId:""}
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
				var visitType = $.grep($scope.bedBillingCategoryListReference, function (billBed) {
		  	           return billBed.billingBedCategoryId == id;
		  	       });
				if(visitType!=undefined && visitType!=null && visitType.length>0)
				 {
					 $scope.visitTypeId=visitType[0].visitTypeId;
				 }
					 return $scope.visitTypeId; 
		   }
	     $scope.visitTypeName = function(id){
			   var visitType = $.grep($scope.visitTypeList, function (visit) {
	  	           return visit.id == id;
	  	       });
			   if(visitType!=undefined && visitType!=null && visitType.length>0)
				 {
					 $scope.visitTypeName1=visitType[0].label;
				 }
			  return $scope.visitTypeName1;
			  
		   }
		$scope.init();
		
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
		
		$scope.getActiveGenderList = function()
		{
			var data = "";
			var URI = BASE_URL + ROOT_URL + GETACTIVEGENDERLIST;
			BillingGenericService.serviceAction("GET", URI, data).then(function(response){
				$scope.genderList= response.data.listObject;
			});
	    }
		//checking max and min age
		$scope.myFunct = function(age,flag) {
		
			if(age >=$scope.minAge && age <= $scope.maxAge){
				
			}else{
				if(flag=='M')
					$scope.packageObj.minAge = "";
				else
					$scope.packageObj.maxAge = "";
				
				growl.warning("Age should be in between  "+$scope.minAge+" to "+$scope.maxAge+" years",{
					title : "warning"
					});
			}
			  
		}
//		started on 19-11-2020
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
									else if(response.data.status == "error"){
									}
								});
						},
					onSelectionChanged: function() {
                                                       $scope.validateFetch(false);
                                                 }
			}

//		ended on 19-11-2020
		$scope.getPackageTypeList($scope.orgId,$scope.unitId);
		$scope.getTariffList($scope.orgId,$scope.unitId);
		$scope.getActiveGenderList();
		$scope.getPaymentEntitlementList();
		$scope.getPatientTypeList();
	    
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
		
		$scope.getServices=function(){
			  // validation
			$scope.searchServiceList = [];
			$scope.searchServObj = {};
			if($scope.packageObj.serviceId=="" || $scope.packageObj.serviceId==undefined){
				growl.warning("Please select package service!",{title : 'Warning!'});
				return false;
			}
			if($scope.packageObj.sexId===undefined || $scope.packageObj.sexId===""){
				growl.warning("Please select gender!",{title : 'Warning!'});
				return false;
			}
			if($scope.packageObj.minAge===undefined || $scope.packageObj.minAge===""){
				growl.warning("Please enter min age!",{title : 'Warning!'});
				return false;
			}
			if($scope.packageObj.maxAge===undefined || $scope.packageObj.maxAge===""){
				growl.warning("Please  enter max age!",{title : 'Warning!'});
				return false;
			}			
			if($scope.packageObj.tariffId=="" || $scope.packageObj.tariffId==undefined){
				growl.warning("Please select tariff!",{title : 'Warning!'});
				return false;
			}
			if($scope.selectedPaymentEntitlementList==undefined || $scope.selectedPaymentEntitlementList.length==0){
				growl.warning("Please select payment entitlement!",{title : 'Warning!'});
				return false;
			}
			
			if($scope.selectedPatientTypeList==undefined || $scope.selectedPatientTypeList.length==0){
				growl.warning("Please select patient type!",{title : 'Warning!'});
				return false;
			}
			
			if($scope.selectedVisitTypeList==undefined || $scope.selectedVisitTypeList.length==0){
				growl.warning("Please select visit type!",{title : 'Warning!'});
				return false;
			}
			if($scope.selectedBedBillingCategoryList==undefined || $scope.selectedBedBillingCategoryList.length==0){
				growl.warning("Please select patient billing bed category!",{title : 'Warning!'});
				return false;
			}
			
//			$("#divEnabled").removeClass("enabled");
//			$("#divEnabled").addClass("disabled");
//			$("#divEnabled2").removeClass("enabled");
//			$("#divEnabled2").addClass("disabled");
			$('#getServicesModal_EHCpackage').modal('show');
		}
		
		$scope.importFromPackage=function(){
			if($scope.packageObj.serviceId=="" || $scope.packageObj.serviceId==undefined){
				growl.warning("Please select package service!",{title : 'Warning!'});
				return false;
			}
			$('#importFromPackageModal').modal('show');
//			$("#divEnabled").removeClass("enabled");
//			$("#divEnabled").addClass("disabled");
//			$("#divEnabled2").removeClass("enabled");
//			$("#divEnabled2").addClass("disabled");
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
			$("#divEnabled2").addClass("enabled");
			
			 //enabling save button start
		  	$("#savePackageId").removeClass("enabled");
			$("#savePackageId").addClass("disabled");*/
			//enabling save button end
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
//			arraList=cartesian($scope.selectedPaymentEntitlementList,$scope.selectedPatientTypeList);
			arraList=cartesian($scope.selectedPaymentEntitlementList,$scope.selectedPatientTypeList,$scope.selectedBedBillingCategoryList);
			
			for(i=0;i<arraList.length;i++){
				
				/*var tempObj={						
						paymentEntitlementId:"",paymentEntitlement:"",
						visitTypeId:"1",visitTypeName:"OP",
						patientTypeId:"",patientType:"",
						billingBedCategoryId:$scope.opdBillingCatId,billingBedCategory:"OPD",
						packagetypeId:1,orgId:$scope.orgId,unitId:$scope.unitId,
					};*/
				var tempObj={						
						paymentEntitlementId:"",paymentEntitlement:"",
						visitTypeId:"",visitTypeName:"",
						patientTypeId:"",patientType:"",
						billingBedCategoryId:$scope.opdBillingCatId,billingBedCategory:"",
						packagetypeId:1,orgId:$scope.orgId,unitId:$scope.unitId,
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
					
					console.log(JSON.stringify(newTempObj))
				    $scope.addNewServiceList.push(newTempObj);
				}//IF
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
							$scope.tempPackageObjResponse= response.data.object==null?{}:response.data.object;
							if(angular.equals({}, tempPackgeObj))
								{
								   tempPackgeObj= angular.copy($scope.tempPackageObjResponse);
								}
							if(!angular.equals({}, tempPackgeObj) && !angular.equals({}, $scope.tempPackageObjResponse))
								{
								  angular.forEach($scope.tempPackageObjResponse.paymentEntitlement,function(paymentEnt,indexPaymentEnt){
										angular.forEach(paymentEnt.patientCategoryList, function(patientCat,indexPatientCat){
											angular.forEach(patientCat.billingBedCategoryList,function(billingBedCat,indexBillingBedCat){								
																				
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
												}
											});		
										});
									});
								  $scope.packageObjResponse=tempPackgeObj;
								}
							$scope.calculatePackageAmounts(undefined,undefined);
							$scope.crateClone();
							$rootScope.stopLoader();
			});
			
//			$("#divEnabled").removeClass("enabled");
//			$("#divEnabled").addClass("disabled");
//			$("#divEnabled2").removeClass("enabled");
//			$("#divEnabled2").addClass("disabled");
			
//			$("#savePackageId").removeClass("disabled");
//			$("#savePackageId").addClass("enabled");
		}
		
		
		//get selected package
		$scope.getPackageDetails=function(obj){
			$scope.packDetailsObj=obj;
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
		
		
		$scope.removeSelectedService = function(index){
			$scope.selectedServiceList.splice(index,1);
			$scope.calculatePackageAmounts();
		}
		
		
		//1.service
		//2.Item
		
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
							  if(billingBedCat.mPackageMasterDto.listTPackageServicesDetailsDto!=undefined && billingBedCat.mPackageMasterDto.listTPackageServicesDetailsDto!=null && billingBedCat.mPackageMasterDto.listTPackageServicesDetailsDto.length==0){
//								    $("#savePackageId").removeClass("enabled");
//									$("#savePackageId").addClass("disabled");
							  }else{
//								  $("#savePackageId").removeClass("disabled");
//									$("#savePackageId").addClass("enabled");
							  }
					      });	
				   });
			 });
			tempPackgeObj= angular.copy($scope.packageObjResponse);
			$scope.calculatePackageAmounts(serviceId,index);
		}
		
		$scope.calculatePackageAmounts = function(serviceId,index)
		{
			angular.forEach($scope.packageObjResponse.paymentEntitlement,function(paymentEnt,indexPaymentEnt)
				{
				angular.forEach(paymentEnt.patientCategoryList, function(patientCat,indexPatientCat){
					angular.forEach(patientCat.billingBedCategoryList,function(billingBedCat,indexBillingBedCat)
							{								
						      var total = 0;
						     var  packagePriceTemp = billingBedCat.mPackageMasterDto.packagePrice;
						      billingBedCat.mPackageMasterDto.packagePrice = $rootScope.parseFloatNum(0);
							  angular.forEach(billingBedCat.mPackageMasterDto.listTPackageServicesDetailsDto,function(serviceDtls,indexServiceDtls)
									  {
								         serviceDtls.numberToBeUse = (parseInt(serviceDtls.numberToBeUse)<0||parseInt(serviceDtls.numberToBeUse)==0)?1:serviceDtls.numberToBeUse;
								         serviceDtls.servicePrice= serviceDtls.servicePrice!=null && serviceDtls.servicePrice!=undefined ? $rootScope.parseFloatNum(serviceDtls.servicePrice) : $rootScope.parseFloatNum(0);
								         serviceDtls.total = serviceDtls.numberToBeUse * serviceDtls.servicePrice;
								         total = parseFloat(total) + (isNaN(serviceDtls.total) ? 0 : parseFloat(serviceDtls.total));
							          });
							  
							    billingBedCat.mPackageMasterDto.serviceWiseSummary = $rootScope.parseFloatNum(total);
								billingBedCat.mPackageMasterDto.grandTotal = $rootScope.parseFloatNum(total);
								billingBedCat.mPackageMasterDto.isMarkupDown = $scope.isMarkupDown;
								total= $rootScope.parseFloatNum(total);
								packagePriceTemp= $rootScope.parseFloatNum(packagePriceTemp);
								console.log("total",total+",price="+packagePriceTemp);
								if(serviceId!=undefined && index!=undefined){
									billingBedCat.mPackageMasterDto.packagePrice = $rootScope.parseFloatNum(total);
								}else{
									billingBedCat.mPackageMasterDto.packagePrice= parseFloat(packagePriceTemp) > parseFloat(total) ?  $rootScope.parseFloatNum(packagePriceTemp) :  $rootScope.parseFloatNum(total);
								}
								
//								billingBedCat.mPackageMasterDto.packagePrice = $rootScope.parseFloatNum(total);
								billingBedCat.mPackageMasterDto.packageCost = $rootScope.parseFloatNum(total);
								billingBedCat.mPackageMasterDto.markupDownInPercentage  = $scope.markupDownInPercentage;
								billingBedCat.mPackageMasterDto.roundOffPositNeg  = $scope.roundOffPositNeg;
								
								//package cost
								switch($scope.isMarkupDown!=''?$scope.isMarkupDown.toString():"0"){
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
								switch($scope.roundOffPositNeg!=''?$scope.roundOffPositNeg.toString():"0"){
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
		
	}catch(e){
		console.log(e.message);
	}
	
	$scope.saveEhcPackage = function(fetch)
	{
		$scope.$broadcast('show-errors-check-validity');
		if ($scope.ehcPackageForm.$valid)
		{
		if($scope.packageObj.serviceId=="" || $scope.packageObj.serviceId==undefined){
				growl.warning("Please select package service!",{title : 'Warning!'});
				return false;
		}
		if($scope.selectedPaymentEntitlementList==undefined || $scope.selectedPaymentEntitlementList.length==0){
				growl.warning("Please Select payment entitlement!",{title : 'Warning!'});
				return false;
		}
		if($scope.packageObj.sexId===undefined || $scope.packageObj.sexId===""){
				growl.warning("Please Select gender!",{title : 'Warning!'});
				return false;
			}
		if($scope.selectedPatientTypeList==undefined || $scope.selectedPatientTypeList.length==0){
				growl.warning("Please Select patient type!",{title : 'Warning!'});
				return false;
			}
		if(angular.equals({}, $scope.packageObjResponse) || $scope.packageObjResponse.paymentEntitlement[0].patientCategoryList[0].billingBedCategoryList[0].mPackageMasterDto.listTPackageServicesDetailsDto==null)
			{
			  growl.warning("Please add services to package!",{title : 'Warning!'});
			  return false;
			}

		$scope.packageObj.orgId = $scope.orgId;
		$scope.packageObj.unitId = $scope.unitId;
		$scope.packageObj.createdBy = $scope.userId;
		$scope.packageObj.updatedBy = $scope.userId;
		$scope.packageObj.packageTypeId = 1;
		$scope.packageObj.serviceId=$scope.packageObj.serviceId;
		$scope.packageObj.tariffId=fetch?$scope.packageObj.tariffId:$scope.defaultSelfTariffId;
		$scope.packageObj.parentPackageId=$scope.parentPackageId;
		$scope.packageObj.packageMasterDtoList=[];
		
	    $scope.saveFlag=true;
		
		angular.forEach($scope.packageObjResponse.paymentEntitlement,function(paymentEnt,indexPaymentEnt){
			angular.forEach(paymentEnt.patientCategoryList, function(patientCat,indexPatientCat){
				angular.forEach(patientCat.billingBedCategoryList,function(billingBedCat,indexBillingBedCat){
					        $scope.packageMasterDtoList=[];
 							var tempPackageObj={};
 							tempPackageObj.packageTypeId = 1;
							tempPackageObj.visitTypeId = billingBedCat.mPackageMasterDto.visitTypeId;
							tempPackageObj.patientTypeId  =billingBedCat.mPackageMasterDto.patientTypeId;
							tempPackageObj.billingBedCategoryId   =billingBedCat.mPackageMasterDto.billingBedCategoryId;
							tempPackageObj.paymentEntitlementTypeId=billingBedCat.mPackageMasterDto.paymentEntitlementTypeId;
							tempPackageObj.sexId=$scope.packageObj.sexId;
							tempPackageObj.packagePrice=billingBedCat.mPackageMasterDto.packagePrice;
							
							tempPackageObj.packageCost=billingBedCat.mPackageMasterDto.packageCost;
							tempPackageObj.orgId = $scope.orgId;
							tempPackageObj.unitId = $scope.unitId;
							tempPackageObj.tariffId=$scope.packageObj.tariffId;
							tempPackageObj.markupDownInPercentage =  billingBedCat.mPackageMasterDto.markupDownInPercentage;
							tempPackageObj.manualRoundoffAmount = billingBedCat.mPackageMasterDto.manualRoundoffAmount;
							tempPackageObj.isMarkupDown =  billingBedCat.mPackageMasterDto.isMarkupDown;
							tempPackageObj.roundOffPositNeg =  billingBedCat.mPackageMasterDto.roundOffPositNeg;
							
						/*	if(isNaN(Number(billingBedCat.mPackageMasterDto.packageCost))||Number(billingBedCat.mPackageMasterDto.packageCost)==0)
							{
							  growl.warning("Package price should not be zero!",{title : 'Warning!'});
							  $scope.saveFlag = false;
							  return false;
							}*/
							if(isNaN(Number(billingBedCat.mPackageMasterDto.packagePrice))||Number(billingBedCat.mPackageMasterDto.packagePrice)==0)
								{
								 growl.warning("Package price should not be zero!",{title : 'Warning!'});
								 $scope.saveFlag = false;
								 return false;
								}
							if(Number(billingBedCat.mPackageMasterDto.packagePrice)<Number(billingBedCat.mPackageMasterDto.packageCost))
								{
								 billingBedCat.mPackageMasterDto.packagePrice = billingBedCat.mPackageMasterDto.packageCost;
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
						  tempPackageObj.listTPackageServicesDetailsDto=billingBedCat.mPackageMasterDto.listTPackageServicesDetailsDto;	
					      $scope.packageObj.packageMasterDtoList.push(tempPackageObj);
				});		
			});
		});

		if(fetch){
             $scope.editPackage($scope.packageObj);
		}
		else if($scope.saveFlag && !fetch)
			{
			/*console.log("save ehc obj", $scope.packageObj);
			return false;*/
			$rootScope.startLoader();
			var URI = BASE_URL + ROOT_URL + "/api/packages/ehc";
			BillingGenericService.serviceAction(METHOD_POST,URI, $scope.packageObj,NOTIFICATION_MSG_STATUS_TRUE).then(function(response) {
				if (response.data.status == "success")
				{
					$scope.$broadcast('show-errors-reset');
					$scope.init();
					$rootScope.stopLoader();
					$state.go('EHCpackage');
				}	
				else if (response.data.status == "error"){
					$rootScope.stopLoader();
				}
			 });
			}
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
	  
		$scope.selectAllPackage = function() {
			$scope.packageIds=[];
			if($scope.chkAll){
				var idex = 0;
				angular.forEach($scope.searchList,
						function(value,index) {
						$scope.packageIds.push(value.packageMasterId);
						$scope.searchList[idex].flag=true;
		    			idex ++;
						});
				
			}else{
				var idex = 0;
        		angular.forEach($scope.searchList,function(value,key) 
				{
		        			$scope.searchList[idex].flag=false;
		        			idex ++;
				});
			}
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
				    $scope.packageObj.minAge  = $scope.packageObjResponse.parentPackageMasterDto.minAge;
				    $scope.packageObj.maxAge  = $scope.packageObjResponse.parentPackageMasterDto.maxAge;
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
				   var uniqueVisitTypeIds=new Set(visitTypeArray);
				   var uniqueVisitType =[];
				   uniqueVisitTypeIds.forEach(value => {uniqueVisitType.push({id:value});});
				   $scope.selectedVisitTypeList = Array.from(uniqueVisitType);
				   $scope.selectedVisitTypeList = $scope.packageObjResponse.selectedVisitTypeList;
                   $scope.selectedPatientTypeList =  $scope.packageObjResponse.selectedPatientTypeList;
                   $scope.selectedBedBillingCategoryList = $scope.packageObjResponse.selectedBedBillingCategoryList;
                   $scope.selectedPaymentEntitlementList =  $scope.packageObjResponse.selectedPaymentEntitlementList;
                   $scope.bedBillingCategoryList = $scope.packageObjResponse.selectedBedBillingCategoryList;
                   $scope.fetchValidate = false;
				   $rootScope.stopLoader();
				   $scope.calculatePackageAmounts(undefined ,undefined);
                   $scope.crateClone();
//				   $("#savePackageId").removeClass("disabled");
//					$("#savePackageId").addClass("enabled");

				}
				else if (response.data.status == "error"){
					$rootScope.stopLoader();
				}
				$scope.packDetailsObj={};
			});
		}


		
	//get consultation dept by service id   conServiceId
	$scope.getDepartmentBySpeacialityId=function(serviceId,index)
	{
		$scope.specialityId="";
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
										        	       if(serviceDtls.consultationSpecialityId>0)
										        	    	   {
										        	    	     $scope.specialityId = serviceDtls.consultationSpecialityId;
										        	    	   }
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

//	------------------edit package-------------------
		if($stateParams.newEHCpackageObj!=undefined && $stateParams.newEHCpackageObj!=null && $stateParams.newEHCpackageObj.parentPackageId>0){
		          $scope.packDetailsObj = {};
                  $scope.packDetailsObj.parentPackageId = $stateParams.newEHCpackageObj.parentPackageId;
                  $scope.parentPackageId = $stateParams.newEHCpackageObj.parentPackageId;
                  $scope.packDetailsObj.packageTypeId = 1;
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
            						billingBedCategoryId:$scope.opdBillingCatId,billingBedCategory:"",
            						packagetypeId:1,orgId:$scope.orgId,unitId:$scope.unitId,
            					};

            				var tempArr=arraList[i];
            				$scope.paymentEntitlementId =tempArr[0].id;
            				$scope.patientTypeId =tempArr[1].id;
            				$scope.bedBillingCategoryId =tempArr[2].id;

            				$scope.visitTypeId = $scope.getVisitTypeByBillingBedCategoryId($scope.bedBillingCategoryId);

            				if(($scope.visitTypeId!=undefined && $scope.visitTypeId!=null && $scope.visitTypeId == 1 && $scope.bedBillingCategoryId == $scope.opdBillingCatId) || ($scope.visitTypeId!=undefined && $scope.visitTypeId!=null && $scope.visitTypeId != 1 && $scope.opdBillingCatId !=$scope.bedBillingCategoryId))
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
            				}//IF
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
				    $scope.packageObj.minAge  = $scope.packageObjResponse.parentPackageMasterDto.minAge;
				    $scope.packageObj.maxAge  = $scope.packageObjResponse.parentPackageMasterDto.maxAge;
				    $scope.packageObj.tariffId  =  $scope.packageObjResponse.parentPackageMasterDto.packageMasterDtoList[0].tariffId;
                    $scope.packageObj.packageTypeId = "1";
				    tempPackgeObj= angular.copy($scope.packageObjResponse);
				    angular.forEach($scope.packageObjResponse.paymentEntitlement,function(paymentEnt,indexPaymentEnt){
						angular.forEach(paymentEnt.patientCategoryList, function(patientCat,indexPatientCat){
							angular.forEach(patientCat.billingBedCategoryList,function(billingBedCat,indexBillingBedCat)
						     {
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
				    $scope.calculatePackageAmounts(undefined ,undefined);
                    $scope.crateClone();
//				    $("#savePackageId").removeClass("disabled");
//					$("#savePackageId").addClass("enabled");
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
           $scope.minAgeClone  =  $scope.packageObj.minAge;
           $scope.maxAgeClone  =  $scope.packageObj.maxAge;
           $scope.tariffIdClone  =  $scope.packageObj.tariffId;
           $scope.selectedVisitTypeListClone =  angular.copy($scope.selectedVisitTypeList);
           $scope.selectedPatientTypeListClone =  angular.copy($scope.selectedPatientTypeList);
           $scope.selectedBedBillingCategoryListClone = angular.copy($scope.selectedBedBillingCategoryList);
           $scope.selectedPaymentEntitlementListClone =  angular.copy($scope.selectedPaymentEntitlementList);
   }
	
}]);