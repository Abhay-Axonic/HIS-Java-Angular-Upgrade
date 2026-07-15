angular
		.module('myApp')
		.controller(
				'billOfMaterialController',
				[
						'$scope',
						'$rootScope',
						'$state',
						'$http',
						'GenericService',
						'$sessionStorage',
						'$cookies',
						'growl',
						'$localStorage',
						'BillingGenericService',
						'$timeout',
						function($scope, $rootScope, $state, $http,
								GenericService,$sessionStorage,$cookies, growl, $localStorage,BillingGenericService,$timeout) {
							
							var cookieObject = $cookies.getObject('cookieObject');
							if(cookieObject == undefined){
								$state.go('login');
								return;
							}
							
							var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
							if (cookieObjectAppConfig == undefined) {
								$state.go('login');
								return;
							}
							
							$scope.tariffId = cookieObjectAppConfig.defaultSelfTariffId;
							$scope.gradeWiseSeviceApplicable=cookieObjectAppConfig.gradeWiseSeviceApplicable;

                            var cookieObjectAppConfigSecond = JSON.parse(localStorage.getItem('cookieObjectAppConfigSecond'));
                            if (cookieObjectAppConfigSecond != undefined) {
	                            $scope.maxInputAmount = cookieObjectAppConfigSecond.maxInputAmount;
                            }

							$scope.saveBtnFlag = true;
							$rootScope.loginpage = true;
							$scope.unitId = cookieObject.unitId;
							$scope.organizationId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							$scope.listBedCategory = [];
							 $scope.list = [];
							 $scope.selectedBillingBedCatList = [];
							 $scope.selectedBillOfMaterialList = [];
							 $scope.billingBedCatDetailsList = [];
							 $scope.isBillOfMaterialDetailsList = [];
							 
							 $scope.getBedCategoryDetails = function(visitTypeId){
							 console.log('visitTypeId ', visitTypeId);
								 $scope.billingBedCatDetailsList = [];	
								 $rootScope.startLoader();
//									var URI = BASE_URL + ROOT_URL+ "/adt/getBillingBedCategoryByVistTypeId/orgId/"+$scope.organizationId+"/unitId/"+$scope.unitId;
									var URI = BASE_URL+ ROOT_URL+ "/adt/getBillingBedCategoryByVistTypeId/"+visitTypeId+"/"+$scope.organizationId+"/"+$scope.unitId;

									var param = {
										
									}
									BillingGenericService.serviceAction(METHOD_GET, URI, param, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
										$rootScope.stopLoader();
										if(response.data.status == 'success'){
											if(response.data.listObject.length > 0 ){
												angular.forEach(response.data.listObject,function(value,index){
													var newArr = {};
													newArr.id = value.billingBedCategoryId;
													newArr.label = value.billingBedDesc;
													
													$scope.billingBedCatDetailsList.push(newArr);
												});
											}
										}
									});
								}
//							 $scope.getBedCategoryDetails();
							 
							$scope.getBillOfMaterialServicesDetails = function(){
									$scope.isBillOfMaterialDetailsList = [];
									$rootScope.startLoader();
									var URI = BASE_URL + ROOT_URL+ "/api/procedure/BillOfMaterial/getBillOfMaterialServices"
									var param = {
											unitId : $scope.unitId,
											organizationId : $scope.organizationId
									}
									BillingGenericService.serviceAction(METHOD_POST, URI, param, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
										$rootScope.stopLoader();
										if(response.data.status == 'success'){
											if(response.data.listObject.length>0){
												angular.forEach(response.data.listObject,function(value,index){
													var newArr = {};
													newArr.id = value.serviceId;
													newArr.label = value.serviceName;
													
													$scope.isBillOfMaterialDetailsList.push(newArr);
												});
											}
											}
									});
							}
							
							$scope.getBillOfMaterialServicesDetails();
							
							$scope.getBedCategory = function(){
								$scope.listBedCategory = [];
								$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL+ "/adt/getActiveBillingBedCategoryList/orgId/"+$scope.organizationId+"/unitId/"+$scope.unitId;
								var param = {
									
								}
								BillingGenericService.serviceAction(METHOD_GET, URI, param, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
									$rootScope.stopLoader();
									if(response.data.status == 'success'){
										if(response.data.listObject.length >0 && $scope.selectedBillingBedCatList.length>0){
											angular.forEach($scope.selectedBillingBedCatList,function(value,index){
												angular.forEach(response.data.listObject,function(value1,index1){
													if(value.id==value1.billingBedCategoryId){
														$scope.listBedCategory.push(value1);
													}
												});
											});
										}
											//$scope.listBedCategory = response.data.listObject;
//										if($scope.commonObj.surgeryGradeId != null){
											$scope.getBillOfMaterialList( $scope.commonObj);
//										}
//										$scope.getBillOfMaterialList( $scope.commonObj.serviceId);
										console.log("listBedCategory",$scope.listBedCategory);
									}
								});
							}
							//$scope.getBedCategory();
							$scope.serviceAmtList = [];
							$scope.listBillOfMaterialServices = [];
							$scope.getBillOfMaterialServices = function(){
								$scope.listBillOfMaterialServices = [];
								$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL+ "/api/procedure/BillOfMaterial/getBillOfMaterialServices"
								var param = {
										unitId : $scope.unitId,
										organizationId : $scope.organizationId
								}
								BillingGenericService.serviceAction(METHOD_POST, URI, param, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
									$rootScope.stopLoader();
									if(response.data.status == 'success'){
										if(response.data.listObject.length > 0 && $scope.selectedBillOfMaterialList.length>0){
												angular.forEach($scope.selectedBillOfMaterialList,function(value,index){
													angular.forEach(response.data.listObject,function(value1,index1){
														if(value.id==value1.serviceId){
															$scope.listBillOfMaterialServices.push(value1);
														}
													});
												});
										}
											//$scope.listBillOfMaterialServices = response.data.listObject;
										 $scope.getBedCategory();
											
										console.log("listBillOfMaterialServices",$scope.listBillOfMaterialServices);
										
										}
								});
							}
							//$scope.getBillOfMaterialServices();
							
							$scope.listprocedure = [];
							$scope.searchprocedure = function(searchString){
								$scope.listprocedure = [];
								$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL+ "/api/procedure/BillOfMaterial/getIsProcedure"
								var param = {
										unitId : $scope.unitId,
										organizationId : $scope.organizationId,
										procedure:searchString
								}
								return BillingGenericService.serviceAction(METHOD_POST, URI, param, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
									$rootScope.stopLoader();
									if(response.data.status == 'success'){
										if(response.data.listObject.length != 0 )
											return $scope.listprocedure = response.data.listObject;
										console.log("listprocedure",$scope.listprocedure);
									}
								});
							}

							$scope.commonObj = {
									serviceId:"",
									surgeryGradeId:"",
									procedureName:"",
									gradeSearch:false,
									procSearch:true,
									tariffId : null,
									visitTypeId : null ,
									paymentEntitlementId : null ,
									patientCategoryId : null,
							};
							
							$scope.procedure = {};
							$scope.selectProcedureName = function($item, $model, $label)
						       {
						    	   $scope.rate = 0.00; 
						    	   $scope.commonObj.serviceId =  $item.serviceId;
						    	   $scope.commonObj.procedureName = $item.serviceName;
						    	
						  
						       }
							
							
							$scope.activeTarrifList = function(){
								var data = {
										organizationId : $scope.organizationId,
										unitId : $scope.unitId
									}
								var URI = BASE_URL + ROOT_URL
								+ "/api/unit/getActiveTariffMasterList";
						GenericService.serviceAction("POST", URI, data)
								.then(
										function(response) {
											$scope.activeTariffList = [];

											if (response.data.status == "success") {
												$scope.activeTariffList=response.data.listObject;												
												console.log('activeTariffList',$scope.activeTariffList);
											}
										});
							}
							$scope.activeTarrifList();
							
							$scope.visitTypeList = function(){
								var data = {};
								var URI = BASE_URL + ROOT_URL
								+ "/api/global/visitTypeMaster/unit/"+$scope.unitId+"/org/"+$scope.organizationId;
						GenericService.serviceAction("GET", URI, data)
								.then(
										function(response) {
											$scope.activeVisitTypeList = [];

											if (response.data.status == "success") {
												$scope.activeVisitTypeList=response.data.listObject;												
												console.log('activeTariffList',$scope.activeTariffList);
											}
										});
							}
							$scope.visitTypeList();
							
							$scope.patientCategory = function(){
								var data = {};
								var URI = BASE_URL + ROOT_URL
								+ GETACTIVEPATIENTCATEGORY;
						GenericService
								.serviceAction("GET", URI, data)
								.then(
										function(response) {
											$scope.patientCategoryList = [];

											if (response.data.status == "success") {
												$scope.patientCategoryList = response.data.listObject;

											}
										});
							}
							$scope.patientCategory();
							
							$scope.activePaymentEntitlement=function(){
								var data = {
										organizationId : $scope.organizationId,
										unitId : $scope.unitId
									}
								var URI = BASE_URL + ROOT_URL
								+ GETACTIVEPAYMENTENTITLEMENT;
						GenericService.serviceAction("GET", URI, data)
								.then(
										function(response) {
											$scope.paymentEntitlementList = [];

											if (response.data.status == "success") {
												$scope.paymentEntitlementList=response.data.listObject;												
												console.log('paymentEntitlementList',$scope.paymentEntitlementList);
											}
										});
							}
							$scope.activePaymentEntitlement();
							
							$scope.displayData = function(){
								$rootScope.startLoader();
								 if( ($scope.commonObj.serviceId!=null ||  $scope.commonObj.serviceId!=undefined) || 
										 ($scope.commonObj.surgeryGradeId!=null ||  $scope.commonObj.surgeryGradeId!=undefined)){
									
									 if($scope.selectedBillingBedCatList.length==0){
										 $rootScope.stopLoader();
											growl.warning("Please Select Billing Bed Category",
													{
														title : 'warning'
													});
											return false;
									 }
									
									 if($scope.selectedBillOfMaterialList.length==0){
										 $rootScope.stopLoader();
											growl.warning("Please Select Is Bill Of Material Services",
													{
														title : 'warning'
													});
											return false;
									 }
									 
									 	$scope.getBillOfMaterialServices();
									 console.log("$scope.selectedBillingBedCatList",$scope.selectedBillingBedCatList);
									 console.log("$scope.selectedBillOfMaterialList",$scope.selectedBillOfMaterialList);
										
								 }else{
									 $rootScope.stopLoader();
										growl.warning("Please Select Procedure",
												{
													title : 'warning'
												});
										return false;
								 }
								
								 $timeout(function () {
									 $rootScope.startLoader();
									// $scope.serviceAmtList = [];
									 if( $scope.serviceAmtList.length == 0){
										 if($scope.listBedCategory.length>0 && $scope.listBillOfMaterialServices.length>0){
												angular.forEach($scope.listBillOfMaterialServices,function(value,index){
													
													angular.forEach($scope.listBedCategory,function(value1,index1){
														var newArr = {};
														newArr.billOfMaterialId = null;
														newArr.billingBedCatId = value1.billingBedCategoryId;
														newArr.procedureId = 0;
														newArr.serviceId = value.serviceId;
														newArr.amount = "";
														$scope.serviceAmtList.push(newArr);
													});
												});
												console.log("$scope.serviceAmtList",$scope.serviceAmtList);
												$rootScope.stopLoader();
											}
									 }
									 $rootScope.stopLoader();
									 }, 2000);
							} 
							
							
							$scope.getServiceAmt = function(billingBedCatId,serviceId,amount,index){
								
								$scope.serviceAmtList[index].billingBedCatId = billingBedCatId;
								$scope.serviceAmtList[index].procedureId = $scope.commonObj.serviceId;
								$scope.serviceAmtList[index].serviceId = serviceId;
								$scope.serviceAmtList[index].amount = amount;
								console.log("$scope.serviceAmtList",$scope.serviceAmtList);
							}
							
							$scope.getBillOfMaterialList = function(procedure){
								 $scope.list = [];
								$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL+ "/api/procedure/BillOfMaterial/getBillOfMaterialListByProcedureId"
					/*		var param = {
										unitId : $scope.unitId,
										organizationId : $scope.organizationId,
										isGrade : procedure.gradeSearch
//										procedureId : procedureId
								};*/
								
								var param = {
										organizationId: $scope.organizationId,
										unitId: $scope.unitId,
										tariffId: $scope.commonObj.tariffId,
										paymentEntitlementId: $scope.commonObj.paymentEntitlementId,
										visitTypeId: $scope.commonObj.visitTypeId,
										patientCategoryId: $scope.commonObj.patientCategoryId,
										isGrade : procedure.gradeSearch
								};
								
								if(procedure.serviceId != null){
									param.procedureId = procedure.serviceId;
								}
								if(procedure.surgeryGradeId != null){
									param.surgeryGradeId = procedure.surgeryGradeId;

								}
								BillingGenericService.serviceAction(METHOD_POST, URI, param, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
									$rootScope.stopLoader();
									if(response.data.status == 'success'){
										if(response.data.listObject.length != 0 )
											 $scope.list = response.data.listObject;
										console.log("savedBillOfMaterialList", $scope.list);
										
										$scope.serviceAmtList = [];
										angular.forEach($scope.list,function(value,index) {
											if(value.tariffId != null){
												$scope.commonObj.tariffId = value.tariffId.toString();
											}else{
//												$scope.commonObj.tariffId = '';
											}
											if(value.visitTypeId != null){
												$scope.commonObj.visitTypeId = value.visitTypeId.toString();
											}else{
//												$scope.commonObj.visitTypeId = '';
											}
											if(value.paymentEntitlementId != null){
												$scope.commonObj.paymentEntitlementId = value.paymentEntitlementId.toString();
											}else{
//												$scope.commonObj.paymentEntitlementId = '';
											}
											if(value.patientCategoryId != null){
												$scope.commonObj.patientCategoryId = value.patientCategoryId.toString();
											}else{
//												$scope.commonObj.patientCategoryId ='';
											}
										});
										
										var flag=true;
										angular.forEach($scope.listBillOfMaterialServices,function(value,index) {//doner
											
												angular.forEach($scope.listBedCategory,function(value1,index1) {//reciept
													
														angular.forEach($scope.list,function(value2,index2) {//database saved list
														
															if(value.serviceId == value2.serviceId && value1.billingBedCategoryId == value2.billingBedCatId){
																var newArr = {};
																newArr.billOfMaterialId = value2.billOfMaterialId;
																newArr.billingBedCatId = value2.billingBedCatId;
																newArr.procedureId = value2.procedureId;
																newArr.serviceId = value2.serviceId;
																newArr.amount = value2.amount;
																$scope.serviceAmtList.push(newArr);
																
																flag=false;
															}
														});
												if(flag){
													var newArr = {};
													newArr.billOfMaterialId = null;
													newArr.billingBedCatId = value1.billingBedCategoryId;
													newArr.procedureId = 0;
													newArr.surgeryGradeId = procedure.surgeryGradeId;
													newArr.serviceId = value.serviceId;
													newArr.amount = "";
													$scope.serviceAmtList.push(newArr);
												}
												else{
													flag=true;
												}
												
										});
										});
										console.log("$scope.serviceAmtList",$scope.serviceAmtList);
										}
								});
							}
							
							$scope.finalList = [];
							$scope.saveBillOfMaterial = function(){
								$rootScope.startLoader();
								if(($scope.procedure.serviceName == undefined || $scope.procedure.serviceName == null || $scope.procedure.serviceName =="")
										&& ($scope.commonObj.surgeryGradeId == undefined || $scope.commonObj.surgeryGradeId == null || $scope.commonObj.surgeryGradeId =="")){
									$scope.stopLoader();
									growl.warning("Please Select Procedure",
											{
												title : 'Warning'
											});
									return false;
								}
								
								if($scope.selectedBillingBedCatList == undefined || $scope.selectedBillingBedCatList == null || $scope.selectedBillingBedCatList.length == 0){
									$scope.stopLoader();
									growl.warning("Please Select Billing Bed Category",
											{
												title : 'Warning'
											});
									return false;
								}
								
								if($scope.selectedBillOfMaterialList == undefined || $scope.selectedBillOfMaterialList == null || $scope.selectedBillOfMaterialList.length == 0 ){
									$scope.stopLoader();
									growl.warning("Please Select Is Bill Of Material Service ",
											{
												title : 'Warning'
											});
									return false;
								}
								
								if($scope.serviceAmtList == undefined || $scope.serviceAmtList == null || $scope.serviceAmtList.length == 0 ){
									$scope.stopLoader();
									growl.warning("Please Click on Display button to Enter Rate of Selected Services ",
											{
												title : 'Warning'
											});
									return false;
								}
								$scope.finalList = [];
								if($scope.serviceAmtList.length>0){
								angular.forEach($scope.serviceAmtList,function(value,index){
									if(value.procedureId != undefined && value.procedureId!=0){

										if(value.billOfMaterialId !=null){
											var newArr = {};
										newArr.billOfMaterialId = value.billOfMaterialId;
										newArr.billingBedCatId = value.billingBedCatId;
										newArr.procedureId = value.procedureId;
										newArr.serviceId = value.serviceId;
										newArr.amount = value.amount;
										newArr.organizationId = $scope.organizationId,
										newArr.unitId = $scope.unitId,
										newArr.createdBy = $scope.userId,
										newArr.updatedBy = $scope.userId,
										newArr.status = 'A',
										newArr.tariffId = $scope.commonObj.tariffId ,
										newArr.visitTypeId = $scope.commonObj.visitTypeId ,
										newArr.paymentEntitlementId = $scope.commonObj.paymentEntitlementId ,
										newArr.patientCategoryId = $scope.commonObj.patientCategoryId ,
										newArr.createdDate = moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
										newArr.updatedDate = moment(new Date()).format('DD-MM-YYYY HH:mm:ss')
										$scope.finalList.push(newArr);
										}

										if(value.amount != "" && value.billOfMaterialId ==null){
											var newArr = {};
										newArr.billOfMaterialId = value.billOfMaterialId;
										newArr.billingBedCatId = value.billingBedCatId;
										newArr.procedureId = value.procedureId;
										newArr.serviceId = value.serviceId;
										newArr.amount = value.amount;
										newArr.organizationId = $scope.organizationId,
										newArr.unitId = $scope.unitId,
										newArr.createdBy = $scope.userId,
										newArr.updatedBy = $scope.userId,
										newArr.status = 'A',
										newArr.tariffId = $scope.commonObj.tariffId ,
										newArr.visitTypeId = $scope.commonObj.visitTypeId ,
										newArr.paymentEntitlementId = $scope.commonObj.paymentEntitlementId ,
										newArr.patientCategoryId = $scope.commonObj.patientCategoryId ,
										newArr.createdDate = moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
										newArr.updatedDate = moment(new Date()).format('DD-MM-YYYY HH:mm:ss')
										$scope.finalList.push(newArr);
										}
										
									}else{


										if(value.amount != "" && (value.billOfMaterialId != null || value.billOfMaterialId == null)){
											var newArr = {};
										newArr.billOfMaterialId = value.billOfMaterialId;
										newArr.billingBedCatId = value.billingBedCatId;
										newArr.procedureId = value.procedureId;
										newArr.surgeryGradeId =$scope.commonObj.surgeryGradeId;
										newArr.isGrade = $scope.commonObj.gradeSearch != undefined ? $scope.commonObj.gradeSearch :false;
										newArr.serviceId = value.serviceId;
										newArr.amount = value.amount;
										newArr.organizationId = $scope.organizationId,
										newArr.unitId = $scope.unitId,
										newArr.createdBy = $scope.userId,
										newArr.updatedBy = $scope.userId,
										newArr.status = 'A',
										newArr.tariffId = $scope.commonObj.tariffId ,
										newArr.visitTypeId = $scope.commonObj.visitTypeId ,
										newArr.paymentEntitlementId = $scope.commonObj.paymentEntitlementId ,
										newArr.patientCategoryId = $scope.commonObj.patientCategoryId ,
										newArr.createdDate = moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
										newArr.updatedDate = moment(new Date()).format('DD-MM-YYYY HH:mm:ss')
										$scope.finalList.push(newArr);
										}
										
									
									}
								});
								}
								$scope.finalBedCatList = [];
								if($scope.listBedCategory.length>0){
									$scope.totalAmt = 0;
									
									var flag = false;
									angular.forEach($scope.listBedCategory,function(value1,index1){
										$scope.totalAmt = 0;
										var newArr1 = {};
												angular.forEach($scope.finalList,function(value,index){
									
													if(value1.billingBedCategoryId == value.billingBedCatId){
														if(value.billOfMaterialId !=null && value.amount == ""){
                                                             value.amount = 0;
                                                              $scope.totalAmt = $scope.totalAmt +parseInt(value.amount);
														      newArr1.billingBedCatId = value.billingBedCatId;
														      flag = true;
														      
														}else{
															    $scope.totalAmt = $scope.totalAmt + parseInt(value.amount);
														        newArr1.billingBedCatId = value.billingBedCatId;
														flag = true;
														}
														
														}
										
										
										
									}); var am = $scope.totalAmt;
									    newArr1.totalAmt = am;

									if(flag == true){

										$scope.finalBedCatList.push(newArr1);
										

									}
										flag = false;
								});
								}
								console.log($scope.finalBedCatList);
								var param = {
										billOfMaterialList : $scope.finalList,
										totalAmount : $scope.finalBedCatList,
								};
								
								var URI = BASE_URL + ROOT_URL+ "/api/procedure/BillOfMaterial/saveBillOfMaterial";
								
								GenericService.serviceAction(METHOD_POST, URI, param).then(
										function(response) {

											if (response.data.status == 'success')
											{
												$rootScope.stopLoader();
												
												$scope.commonObj={};
												$scope.serviceAmtList=[];
												$scope.procedure={};
												$scope.selectedBillOfMaterialList = [];
												$scope.selectedBillingBedCatList = [];
												$scope.listBedCategory = [];
												$scope.listBillOfMaterialServices = [];
												$scope.commonObj.gradeSearch =false;
												$scope.commonObj.procSearch=true;
												growl.success(response.data.message,
														{
															title : 'Success!'
														});
												$scope.$broadcast('show-errors-reset');
											} else if(response.data.status == 'error')
											{
												$rootScope.stopLoader();
												growl.error(response.data.message,
														{
															title : 'Error!'
														});
											}
										});
							}
							
							//added by chetana for Grade Search
							$scope.getGradeList = function() {
								var data = {
										orgnisationId : $scope.organizationId,
//										unitId :$scope.unitId,
										};
								
								var URI = BASE_URL + ROOT_URL + "/api/global/getActiveSurgeryGradeList";
								console.log("URI", URI);
								$scope.gradeLis=[];
								GenericService.serviceAction("GET", URI).then(function(response) {
									$scope.gradeList = response.data.listObject;
									console.log("gradeList",$scope.gradeList);
								});
							}
							$scope.getGradeList();
						
							$scope.checkedFlag=function(flag, index){
								if(index == 2){
									$scope.commonObj.surgeryGradeId="";
									$scope.commonObj.gradeSearch=false;
									$scope.commonObj.procSearch=true;

								}
								if(index == 1){
									$scope.commonObj.gradeSearch=true;
									$scope.commonObj.procSearch=false;

								}
							}

                $scope.checkMaxInoutAmount = function(index){
                    var amount = $scope.serviceAmtList[index].amount;
                    if(amount !== undefined && amount !== null && amount !== ''){
                        if($scope.maxInputAmount !== undefined && $scope.maxInputAmount !== null && $scope.maxInputAmount !== ''){
                            if(amount > $scope.maxInputAmount){
                                $scope.serviceAmtList[index].amount = '0';
                                growl.warning
                                ("You can enter max amount up to " + $scope.maxInputAmount + "...!",
                                    {title : "WARNING!"}
                                );
                            }
                        }
                    }
                }
			}]);
							