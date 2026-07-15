angular.module('myApp').controller(
				'postOprativeCareController',
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
						'$stateParams',
						'CONSTANTS',
						'GenericServiceParamHeader',
						'$sce',
						'$window',
						'$interval',
						function($scope, $rootScope, $state, $http,
								GenericService,$sessionStorage,$cookies, growl, $localStorage,BillingGenericService,$timeout,$stateParams,CONSTANTS,GenericServiceParamHeader, $sce, $window, $interval) {
				
							var cookieObject = $cookies.getObject('cookieObject');
							if(cookieObject == undefined){
								$state.go('login');
								return;
							}
							//alert("hiiiii");
							$scope.userId = cookieObject.userId;
							$scope.unitId = cookieObject.unitId;
							$scope.organizationId = cookieObject.organizationId;
							$scope.postCareCommonObj = $stateParams.postCareCommonObj;
							$scope.postCareDetailsObj  = $stateParams.postCareDetailsObj;
							if($scope.postCareCommonObj.patientId == undefined){
								$state.go('OTIMSdashboard');
								return false;
							}
							$scope.intraCommonObj = $stateParams.intraCommonObj;
							$scope.intraDetailsObj  = $stateParams.intraDetailsObj;
							$scope.resultCommonObj  = $stateParams.resultCommonObj;
							$scope.postCommonObj = $stateParams.postCommonObj;
							$scope.postDetailsObj  = $stateParams.postDetailsObj;
							$scope.ckObj={};
							console.log("$scope.postCareCommonObj",$scope.postCareCommonObj);
							console.log("$scope.postCareDetailsObj",$scope.postCareDetailsObj);
							
							//MedicationOrdering
							$scope.initMedicationObj = function() {
								$scope.searchItemFre = "";
								$scope.searchItem = "";
								$scope.registrationNumber = "";
								$scope.saveBtnFlag = true;
								$scope.updateScope = true;
								$scope.updateBtnFlag = false;
								$scope.roa_options=[];
								$scope.medicationObj = {
									itemId : "",
									searchItem : "",
									formulationTypeId : '',
									favorite_status : false,
									medicationStartDate : new Date(),
									roaId : '',
									dose : '',
									doseUnitId : '',
									durationCount : '',
									durationUnit : 'Day',
									instructionId : '',
									medicationEndDate : '',
									frequencyId : '',
									quantity : '',
									uomTypeId : '',
									conversions : '',
									strength : '',
									priorityId : '2',
									priorityCode : 'NR',
									isGeneric:'N',
									isInstruction:'N',
									nonFormularyAdvice:'N',
									searchItemFre :"",
									isStat : false,
									favorite_status : false,
									isDischarge : false,
									nonFormularyAdvice:'N',
									prescribedDosageUnitList:[]

								};
							}

							$scope.initMedicationObj();
							$scope.updateFlag = false;
							$scope.updateFlagValidation = false;
							$scope.saveFlag = true; 
							
							$scope.clrNonForData=function(info){
								if(info == 'N'){
									$scope.initMedicationObj();
								}
								else{
									$scope.searchItemFre = "";
									$scope.searchItem = "";
									$scope.registrationNumber = "";
									$scope.medicationObj = {
										itemId : "",
										formulationTypeId : '',
										favorite_status : false,
										medicationStartDate:new Date(),
										roaId : '',
										dose : '',
										doseUnitId : '',
										durationCount : '',
										durationUnit : 'Day',
										instructionId : '',
										medicationEndDate : '',
										frequencyId : '',
										quantity : '',
										uomTypeId : '',
										conversions : '',
										strength : '',
										priorityId : '2',
										priorityCode : 'NR',
										isGeneric:'N',
										isInstruction:'N',
										nonFormularyAdvice:'Y',
										searchItemFre :"",
										isStat : false,
										isDischarge : false,
										prescribedDosageUnitList:[]
									};
								}
							}
							$scope.medicationDetails = [];
							
							$scope.addEmptyRow = function() {
								 $scope.medicationExist =false;
								 var isDischarge=false;
								 if($scope.medicationDetails.length>0 && $scope.medicationDetails[-1].isDischarge!=undefined){
									 if($scope.medicationDetails[-1].isDischarge==true){
										 isDischarge=true;
									 }
								 }
								 
								 if($scope.medicationDetails.length>0){
								 if($scope.medicationDetails[$scope.medicationDetails.length-1].isDischarge==false && isDischarge==true){
									 growl.warning(
												'You can add only normal medication',
												{
													title : 'Warning!'
												});
									return false;
								 }
								 if($scope.medicationDetails[$scope.medicationDetails.length-1].isDischarge==true && isDischarge==false){
									 growl.warning(
												'You can add only discharge medication',
												{
													title : 'Warning!'
												});
									return false;
								 }
								 
								 }
								 
								if ( ($scope.medicationObj.searchItem != undefined || $scope.medicationObj.medicationName != ""  &&  $scope.medicationObj.searchItem != "")
										&& $scope.medicationObj.medicationEndDate != undefined
										&& $scope.medicationObj.medicationEndDate != ''
										&& $scope.medicationObj.durationCount != undefined
										&& $scope.medicationObj.durationCount != ''
										&& $scope.medicationObj.doseUnitId != undefined
										&& $scope.medicationObj.doseUnitId != ''
										&& $scope.medicationObj.quantity != 0
										&& $scope.medicationObj.roaId != undefined
										&& $scope.medicationObj.roaId != ''
										&& $scope.medicationObj.quantity != undefined
										&& $scope.medicationObj.searchItemFre != ''
										&& $scope.medicationObj.formulationCounterIndex != ''
										) {
									
									   if($scope.medicationObj.searchItem != undefined &&  $scope.medicationObj.searchItem != ""){
										   var Arr = $scope.medicationObj.searchItem.split("|");
										   $scope.medicationObj.itemName = Arr[0];
											var roaDesc = $
													.grep(
															$scope.roa_options,
															function(disCat) {
																return disCat.roa_id == $scope.medicationObj.roaId;
															})[0].roa_details;

											$scope.medicationObj.roaDesc = roaDesc;
											
									   }else{
										   
										   var Arr = $scope.medicationObj.medicationName.split("|");
										   $scope.medicationObj.searchItem = Arr[0];
										   $scope.medicationObj.itemName = $scope.medicationObj.medicationName;
										   $scope.medicationObj.medicationName = $scope.medicationObj.medicationName;

											var roaDesc = $
													.grep(
															$scope.roa_options1,
															function(disCat) {
																return disCat.roa_id == $scope.medicationObj.roaId;
															})[0].roa_details;

											$scope.medicationObj.roaDesc = roaDesc;
									   }
								var strengthDesc = $ .grep( $scope.doseList, function(disCat) {
													return disCat.id == $scope.medicationObj.doseUnitId;
												})[0].unit;
								$scope.medicationObj.strengthDesc = strengthDesc;
								$scope.medicationObj.frequencyDesc = $scope.medicationObj.searchItemFre;
								$scope.medicationObj.nonFormularyAdvice = $scope.medicationObj.nonFormularyAdvice;
								$scope.medicationObj.medicationStartDate = moment($scope.medicationObj.medicationStartDate).format('YYYY-MM-DD');
								$scope.medicationObj.favorite_status = $scope.medicationObj.favorite_status;
								if($scope.medicationObj.doctorFavoriteId != undefined){
								$scope.medicationObj.doctorFavoriteId=$scope.medicationObj.doctorFavoriteId
								}
								if($scope.medicationObj.isInstruction == 'Y') {
									$scope.medicationObj.instructionId = null;
									$scope.medicationObj.instructionDesc = null;
								}
								
								if ($scope.medicationObj.instructionId != null && $scope.medicationObj.instructionId!=""
									&& $scope.medicationObj.instructionId!=undefined) {
									var instructionDesc = $.grep($scope.medicationInstructions,
													function(disCat) {
														return disCat.instructionId == $scope.medicationObj.instructionId;
													})[0].instructionDesc;

									$scope.medicationObj.instructionDesc = instructionDesc;
								} else if($scope.medicationObj.isInstruction == 'Y') {
									$scope.medicationObj.instructionDesc = $scope.medicationObj.isInstructionText;
								}else {
									$scope.medicationObj.instructionDesc = "No instruction";
								}
								
								if ($scope.medicationObj.priorityId != null && $scope.medicationObj.priorityId!="" && $scope.medicationObj.priorityId!=undefined) {
								var priorityCode = $.grep( $scope.priorityList, function(disCat) {
													return disCat.priorityId == $scope.medicationObj.priorityId;
												})[0].priorityCode;

								$scope.medicationObj.priorityCode = priorityCode;
								} else {
									$scope.medicationObj.priorityCode = " ";
									$scope.medicationObj.priorityId =2;
								}
								
								$scope.medicationObj.frequencyId = $scope.medicationObj.frequencyId ;
								$scope.medicationObj.noOfAdministrationTime =$scope.freQua ;
								$scope.medicationObj.durationCount =$scope.medicationObj.durationCount ;
								
								
								 if($scope.medicationDetails.length>0){
	                                    $scope.medicationObj.medicationStartDate = moment($scope.medicationObj.medicationStartDate).format('YYYY-MM-DD');
	                                    angular.forEach($scope.medicationDetails,function(value,index){
	                                        
	                                        if ( $scope.medicationObj.searchItem == value.itemName
	                                                && $scope.medicationObj.medicationStartDate == value.medicationStartDate
	                                                && $scope.medicationObj.durationCount == value.durationCount
	                                                && $scope.medicationObj.searchItemFre == value.frequencyDesc
	                                                && $scope.medicationObj.doseUnitId == value.doseUnitId
	                                                && $scope.medicationObj.roaId == value.roaId
	                                                && $scope.medicationObj.quantity == value.quantity) {
	                                            
	                                            $scope.medicationExist =true;
	                                        }
	                                    });
	                                }
	                                
	                               if($scope.detailedMedications.length>0){
	                                    $scope.medicationObj.medicationStartDate = moment($scope.medicationObj.medicationStartDate).format('YYYY-MM-DD');
	                                    angular.forEach($scope.detailedMedications,function(value,index){
	                                        
	                                        var sDT =  moment(value.startDate,"DD-MM-YYYY").toDate();
	                                        var eDT = moment(value.endDate,"DD-MM-YYYY").toDate();
	                                        var startDT =  moment(sDT).format('YYYY-MM-DD');
	                                        var endDT = moment(eDT).format('YYYY-MM-DD');
	                                        
	                                        if ( $scope.medicationObj.searchItem == value.itemName
	                                                && $scope.medicationObj.medicationStartDate ==  startDT
	                                                && $scope.medicationObj.searchItemFre == value.frequencyDesc
	                                                && $scope.medicationObj.dose== value.doseDesc
	                                                && $scope.medicationObj.doseUnitId == value.doseUnitId
	                                                && $scope.medicationObj.quantity == value.orderQty
	                                                && $scope.medicationObj.roaId == value.roaId) {
	                                            
	                                            $scope.medicationExist =true;
	                                        }
	                                    });
	                                    
	                                }
	                                
	                                
	                                if($scope.medicationExist){
	                                    growl.warning(
	                                            "Medication Already Exist",
	                                            {
	                                                title : 'Warning!'
	                                            });
	                                    $scope.medicationObj.medicationStartDate = new Date();
	                            $rootScope.stopLoader();
	                            return false;
	                            }
							
								$scope.medicationDetails.push($scope.medicationObj);
								
								if(!$scope.medicationExist){
									$scope.initMedicationObj();
									}
								}else{
									growl.warning(
											"Please enter all mandatory fields.",
											{
												title : 'Warning!'
											});
								}
							   
							}
							
							$scope.itemList = [];
							$scope.searchMedicationItems = function(searchKeyword) {
								if(searchKeyword.length >= 3){
									$scope.itemList = [];
									var unitId = $scope.unitId
									var orgId = $scope.organizationId
									var Arr = searchKeyword.split("|");
									searchKeyword = Arr[0];
									var regex=/[^a-z]/gi;
									searchKeyword=searchKeyword.replace(regex," ");
									
									var URI = BASE_URL + ROOT_URL + GETMEDICATIONDETAILS + "?unitId="
											+ unitId + "&orgId=" + orgId + "&searchDrug=" + searchKeyword
									return BillingGenericService
											.serviceAction(METHOD_GET, URI, "",
													NOTIFICATION_MSG_STATUS_FALSE).then(
													function(response) {
														if (response.data.status == "success") {
															$scope.itemList = response.data.listObject;
														}
														return $scope.itemList;
													});
								}
							}
							
					     	$scope.calculationByDoseCalculation=function(item){
                        		var currentMedication = item;
								var indexOfCurrentMedication = $scope.medicationDetails.indexOf(currentMedication)

                        		$scope.medicationObj.doseCalculationApplicableData=JSON.parse(item.doseCalculationApplicableData);
						        angular.forEach($scope.medicationObj.doseCalculationApplicableData, function(val, key){
						        	$scope.medicationObj.quantityForDoseCal=val.quantityForDoseCal;
						        	$scope.medicationObj.doseForDoseCal=val.doseForDoseCal;
						        	$scope.medicationObj.strengthForDoseCal=val.strengthForDoseCal;
						        	$scope.medicationObj.searchItemFre=val.searchItemFre;
						        	$scope.medicationObj.noOfAdministrationTimeForDoseCal=val.noOfAdministrationTimeForDoseCal;
						        	$scope.medicationObj.durationForDoseCal=val.durationForDoseCal;
						        	$scope.medicationObj.durationCountForDoseCal=val.durationCountForDoseCal;
						        	$scope.freQua =$scope.medicationObj.noOfAdministrationTimeForDoseCal;
									$scope.medicationObj.frequencyId = val.frequencyForDoseCal;
									$scope.frequency_id=val.frequencyForDoseCal;
									$scope.frequency=$scope.medicationObj.searchItemFre;
									$scope.medicationObj.durationCount=$scope.medicationObj.durationCountForDoseCal;
									$scope.medicationObj.duration=$scope.medicationObj.durationForDoseCal;
									$scope.medicationObj.itemStrength=$scope.medicationObj.strengthForDoseCal !=null ? $scope.medicationObj.strengthForDoseCal:(item.strength+" "+(item.unitDesc!=null ? item.unitDesc : " ") );
									
						        });	
						        $scope.idx=indexOfCurrentMedication;
								
								$scope.onDurationCountChange(item);
								$scope.medicationObj.prescribeDose= $scope.dosePerServing;
						      
                        	}
					     	
							$scope.checkgenericObj = {};
							$scope.itemStrength=""; 
							$scope.prescribedDosageUnitList=[];
							$scope.addMedicationItemList = function($item,	$model, $label, idx) {
								console.log($item);
								$scope.itemStrength="";
								$rootScope.startLoader();
								var arr = $item.medicationName.split("|");
								var dggd =arr[1].split(":");
								$scope.genericQuantityCheck = true;
								if(dggd[1] == 0){
									growl.warning("Stock not available for the selected Brand" );
									if(dggd[1] == 0)
										{
										$scope.genericQuantityCheck = true;
										}
								}
								$scope.medicationObj.itemStrength=$item.strength+" "+($item.unitDesc!=null ? $item.unitDesc : " ") ;
								$scope.medicationObj.doseUnitId=$item.strengthUnitId;
								$scope.medicationObj.dose=$item.strength;
								$scope.medicationObj.isDoseCalculationApplicable = $item.isDoseCalculationApplicable;
								$scope.medicationObj.doseCalculationApplicableData = $item.doseCalculationApplicableData;
								$scope.medicationObj.itemId = $item.drugId;
								$scope.medicationObj.genericName = "";
								$scope.medicationObj.genericId = $item.genericId;
								$scope.medicationObj.searchGeneric= $item.genericName;
								$scope.medicationObj.prescribedDosageUnitList=[];
								$scope.medicationObj.formulationCounterIndex="";
								$scope.medicationObj.rate=$item.rate;
								$scope.medicationObj.cimsCode=$item.cimsCode;
								$scope.medicationObj.cimsType=$item.cimsType;
								$scope.cimsCode=$item.cimsCode;
								$scope.cimsType=$item.cimsType;
								
                               $scope.favAvailableflag = false;
								
								if($scope.favouriteMedications.length > 0){
									var found = $scope.favouriteMedications.find(ar=>ar.itemId == $scope.medicationObj.itemId);
									   if(found==undefined)
										   {
										      console.log("testOrderSet" ,$scope.medicationDetails);
										   }
									   else{
										   $scope.favAvailableflag = true;
									   }
								}

								var formulationTypeId = $item.formulationTypeId;
								var URI = BASE_URL
										+ ROOT_URL
										+ "/api/opd/getRoaList"
										+ "?unitId="
										+ $scope.unitId
										+ "&orgId="
										+ $scope.organizationId
										+ "&formulationTypeId="
										+ formulationTypeId
										+ "&genericId="
										+ $item.genericId
										+ "&patientId="
										+ $scope.postCareCommonObj.patientId;

								GenericServiceParamHeader
										.serviceAction(METHOD_GET, URI)
										.then(
												function(response) {
													if (response.data.status == "success") {
														$rootScope.stopLoader();
														var responseList = response.data.listObject;
														$scope.medicaRoaList = responseList;
														if (response.data.object == 0) {
															if (response.data.listObject.length !== 0) {

																$scope.roa_options = responseList;
															}
															$scope.medicationObj.genericName = $item.genericName;
															$scope.medicationObj.formulationTypeId = $item.formulationTypeId;
															$scope.medicationObj.strength = $item.strength;
														} else {
															$scope.checkgenericObj = {
																genericName : $item.genericName,
																formulationTypeId : $item.formulationTypeId,
																strength : $item.strength,
															}
															angular.element( '#confirmAddItemPopup').modal('show');
														}
													}
													// get formuation dosage
													// list
													var dataobj = {
															organizationId : $scope.organizationId,
															unitId : $scope.unitId,
															formulationTypeId:$item.formulationTypeId
													};
													if($scope.medicationObj.isDoseCalculationApplicable == true){
														$scope.medicationObj.prescribeDose=($scope.medicationObj.prescribeDose !=undefined && $scope.medicationObj.prescribeDose!= null && $scope.medicationObj.prescribeDose != "") ? $scope.medicationObj.prescribeDose: "";

													}else{
														$scope.medicationObj.prescribeDose="";
													}													
													var URI = BASE_URL + ROOT_URL+ "/api/global/getFormulationTypeToDosageMapperListByFormulationTypeId";
													GenericService.serviceAction("POST", URI, dataobj).then(function(response) {
																$rootScope.stopLoader();
																$scope.medicationObj.prescribedDosageUnitList=[];
																$scope.counterFormulation=1;
																
																angular.forEach(response.data.listObject,function(value,index){
																	var unitObj={};
																	unitObj.formulationDosageFromUnit=value.fromConversionUnit;
																	unitObj.formulationDosageFromUnitId=value.fromConversionFactorUnit;
																	unitObj.formulationDosageToUnit=value.toConversionUnit;
																	unitObj.formulationDosageToUnitId=value.toConversionFactorUnit;
																	unitObj.counterIndex=$scope.counterFormulation++;
																	unitObj.formulationDosageFromConversionValue=value.fromConversionFactor;
																	unitObj.formulationDosageToConversionValue=value.toConversionFactor;
																	unitObj.formulationFactorId=value.factorId;
																	unitObj.formulationDosage=value.dosageForm;
																	unitObj.formulationDosageMappId=value.formulationDosageMappId;
																	$scope.medicationObj.prescribedDosageUnitList.push(unitObj);
																});
																
																angular.forEach(response.data.listObject,function(value,index){
																	var unitObj={};
																	if(value.factorId==1){
																		unitObj.formulationDosageFromUnit=value.toConversionUnit;
																		unitObj.formulationDosageFromUnitId=value.toConversionFactorUnit;
																		unitObj.formulationDosageToUnit=value.fromConversionUnit;
																		unitObj.formulationDosageToUnitId=value.fromConversionFactorUnit;
																		unitObj.counterIndex=$scope.counterFormulation++;
																		unitObj.formulationDosageFromConversionValue=value.toConversionFactor;
																		unitObj.formulationDosageToConversionValue=value.fromConversionFactor;
																		unitObj.formulationFactorId=value.factorId;
																		unitObj.formulationDosage=value.dosageForm;
																		unitObj.formulationDosageMappId=value.formulationDosageMappId;
																		$scope.medicationObj.prescribedDosageUnitList.push(unitObj);
																	}
																});
																var list=[];
																if($scope.medicationObj.prescribedDosageUnitList.length>0 && $scope.medicationObj.prescribedDosageUnitList[0].formulationFactorId==1){
																	 list=$.grep($scope.medicationObj.prescribedDosageUnitList, function (obj) {
																        return $scope.medicationObj.doseUnitId === obj.formulationDosageFromUnitId;
																    });
																	if(list.length>0){	
																		$scope.medicationObj.formulationCounterIndex=list[0].counterIndex;
																		$scope.medicationObj.prescriptionDoseUnit=list[0].formulationDosageFromUnit;
																	}
																}
																
															/*
															 * if($scope.prescribedDosageUnitList.length>0){
															 * $scope.medicationObj.prescribedDoseUnitId=$scope.medicationObj.doseUnitId; }
															 */
													});
													
												});
								
								
								if($scope.favAvailableflag == true){
									var idxFav = $scope.favouriteMedications.findIndex(item => item.itemId == $scope.medicationObj.itemId);
									
									$scope.tempDurationCount=$scope.medicationObj.durationCount;
									$scope.medicationObj = {};
									$scope.medicationObj.rate=$item.rate;
									$scope.medicationObj.itemId = $scope.favouriteMedications[idxFav].itemId;
									$scope.medicationObj.doctorFavoriteId=$scope.favouriteMedications[idxFav].doctorFavoriteId;
									$scope.searchItem = $scope.favouriteMedications[idxFav].itemName;
									$scope.medicationObj.genericName = $scope.favouriteMedications[idxFav].genericName;
									$scope.medicationObj.searchGeneric = $scope.favouriteMedications[idxFav].genericName;
									$scope.medicationObj.genericId = $scope.favouriteMedications[idxFav].genericId;
									$scope.medicationObj.formulationTypeId = $scope.favouriteMedications[idxFav].formulationTypeId;
									$scope.medicationObj.isGeneric ='N';
									$scope.medicationObj.nonFormularyAdvice ='N';
									$scope.medicationObj.isInstruction='N';
									$scope.medicationObj.priorityCode='NR';
									$scope.medicationObj.searchItem =$scope.favouriteMedications[idxFav].itemName;;
									$scope.medicationObj.searchItemFre = $scope.favouriteMedications[idxFav].frequencyDesc;
									$scope.medicationObj.frequencyId = $scope.favouriteMedications[idxFav].frequencyId;
									$scope.medicationObj.prescribedDosageUnitList=[];
									$scope.frequency_id=$scope.favouriteMedications[idxFav].frequencyId;
									$scope.frequency=$scope.medicationObj.searchItemFre;
									$scope.noOfAdministrationTime=$scope.favouriteMedications[idxFav].noOfAdministrationTime;
									$scope.item={
											frequency_id:$scope.frequency_id,
											frequency:$scope.medicationObj.searchItemFre,
											noOfAdministrationTime:$scope.noOfAdministrationTime
									}
									
									$scope.model=$scope.favouriteMedications[idxFav].frequencyDesc;
									$scope.label=$scope.favouriteMedications[idxFav].frequencyDesc;
									$scope.medicationObj.favorite_status =false;
									$scope.addFrequencyItemList($scope.item, $scope.model, $scope.label);
									var formulationTypeId = $scope.favouriteMedications[idxFav].formulationTypeId;
									var URI = BASE_URL
											+ ROOT_URL
											+ "/api/opd/getRoaList"
											+ "?unitId="
											+ $scope.unitId
											+ "&orgId="
											+ $scope.organizationId
											+ "&formulationTypeId="
											+ formulationTypeId
											+ "&genericId="
											+ $scope.favouriteMedications[idxFav].genericId
											+ "&patientId="
											+ $scope.postCareCommonObj.patientId;

									GenericServiceParamHeader
											.serviceAction(METHOD_GET, URI)
											.then(
													function(response) {
														if (response.data.status == "success") {
															if (response.data.listObject.length !== 0) {
																var responseList = response.data.listObject;
																$scope.roa_options = responseList;
															}
														}
													});

									 if($scope.favouriteMedications[idxFav].roaId != null){
										 $scope.medicationObj.roaId =( $scope.favouriteMedications[idxFav].roaId)
											.toString();
	                                 }
									$scope.medicationObj.dose = $scope.favouriteMedications[idxFav].dose;
									$scope.medicationObj.doseUnitId = $scope.favouriteMedications[idxFav].doseUnitId!=null?($scope.favouriteMedications[idxFav].doseUnitId).toString():0;
									$scope.medicationObj.itemStrength=$scope.favouriteMedications[idxFav].dose+" "+$scope.favouriteMedications[idxFav].doseDesc;
									$scope.medicationObj.unit = $scope.favouriteMedications[idxFav].doseDesc;
									
									
									// get formuation dosage list
									var dataobj = {
											organizationId : $scope.organizationId,
											unitId : $scope.unitId,
											formulationTypeId:$scope.favouriteMedications[idxFav].formulationTypeId
									};
									if($scope.medicationObj.isDoseCalculationApplicable == true){
										$scope.medicationObj.prescribeDose=($scope.medicationObj.prescribeDose !=undefined && $scope.medicationObj.prescribeDose!= null && $scope.medicationObj.prescribeDose != "") ? $scope.medicationObj.prescribeDose: "";

									}else{
										$scope.medicationObj.prescribeDose="";
									}
									var URI = BASE_URL + ROOT_URL+ "/api/global/getFormulationTypeToDosageMapperListByFormulationTypeId";
									GenericService.serviceAction("POST", URI, dataobj).then(function(response) {
												$rootScope.stopLoader();
												$scope.medicationObj.prescribedDosageUnitList=[];
												$scope.counterFormulation=1;
												
												angular.forEach(response.data.listObject,function(value,index){
													var unitObj={};
													unitObj.formulationDosageFromUnit=value.fromConversionUnit;
													unitObj.formulationDosageFromUnitId=value.fromConversionFactorUnit;
													unitObj.formulationDosageToUnit=value.toConversionUnit;
													unitObj.formulationDosageToUnitId=value.toConversionFactorUnit;
													unitObj.counterIndex=$scope.counterFormulation++;
													unitObj.formulationDosageFromConversionValue=value.fromConversionFactor;
													unitObj.formulationDosageToConversionValue=value.toConversionFactor;
													unitObj.formulationFactorId=value.factorId;
													unitObj.formulationDosage=value.dosageForm;
													unitObj.formulationDosageMappId=value.formulationDosageMappId;
													$scope.medicationObj.prescribedDosageUnitList.push(unitObj);
												});
												
												angular.forEach(response.data.listObject,function(value,index){
													var unitObj={};
													if(value.factorId==1){
														unitObj.formulationDosageFromUnit=value.toConversionUnit;
														unitObj.formulationDosageFromUnitId=value.toConversionFactorUnit;
														unitObj.formulationDosageToUnit=value.fromConversionUnit;
														unitObj.formulationDosageToUnitId=value.fromConversionFactorUnit;
														unitObj.counterIndex=$scope.counterFormulation++;
														unitObj.formulationDosageFromConversionValue=value.toConversionFactor;
														unitObj.formulationDosageToConversionValue=value.fromConversionFactor;
														unitObj.formulationFactorId=value.factorId;
														unitObj.formulationDosage=value.dosageForm;
														unitObj.formulationDosageMappId=value.formulationDosageMappId;
														$scope.medicationObj.prescribedDosageUnitList.push(unitObj);
													}
												});
												var list=[];
												if($scope.medicationObj.prescribedDosageUnitList.length>0 ){
													 list=$.grep($scope.medicationObj.prescribedDosageUnitList, function (obj) {
														 return $scope.favouriteMedications[idxFav].formulationDosageMappId === obj.formulationDosageMappId;
												    });
													if(list.length>0){	
														$scope.medicationObj.formulationCounterIndex=list[0].counterIndex;
													}
												}
									});
									
									$scope.medicationObj.durationCount = $scope.tempDurationCount;
									$scope.medicationObj.durationUnit = 'Day';
									$scope.medicationObj.isGeneric ='N';
									$scope.medicationObj.nonFormularyAdvice ='N';
									$scope.medicationObj.medicationStartDate = new Date();
									$scope.medicationObj.medicationEndDate = '';
									$scope.medicationObj.instructionId = $scope.favouriteMedications[idxFav].instructionId;
									$scope.medicationObj.priorityId ='2';
									$scope.medicationObj.rate=$item.rate;
									console.log('svdfvdfv', $scope.medicationObj);
									
									if($scope.patientDetails.dischargeStatusId!=null &&
											($scope.patientDetails.dischargeStatusId==1 || $scope.patientDetails.dischargeStatusId==2
													|| $scope.patientDetails.dischargeStatusId == 3)){
										var isDischargeMed = $scope.medicationDetails.find(ar=>ar.isDischarge);
										if(isDischargeMed!=undefined){
											$scope.medicationObj.isDischarge = "discharge";
										}
									}
									
									
								}
								if (!!$scope.medicationObj.itemId) {


                                       	   var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.ITEM_MASTER.DETAILS_API  +$scope.organizationId + '/' + $scope.medicationObj.itemId;
                                             var data = {};
                                              GenericService.serviceAction("GET", URI, data).then(function (response) {
                                                    if(response.data.unitOfMeasurementDetailsResponse!=null)
                                                    {
                                                    	response.data.unitOfMeasurementDetailsResponse.forEach(function(item){
                                                    	$scope.medicationObj.uom = item.uomList;
                                          			   $scope.medicationObj.uom= [{
                                                          uomDetails: "Select UOM",
                                                      }];

                                                      	$scope.medicationObj.uom =item.uomList;
                                                          $scope.medicationObj.selectedUom = $scope.uomUnits.find(function(units){
                                                            return $scope.medicationObj["uom"][2].uomUnitId == units.id;
                                                          });

                                            	      });
                                                    	
                                               
                                                    	}
                                              });

                              }

								$timeout(function() {
									 document.getElementById("medicationFreq435").focus();
								}, 1000);
								
								if($scope.medicationObj.isDoseCalculationApplicable == true){
									$scope.medicationObj.prescribeDose= $scope.dosePerServing;
									$scope.calculationByDoseCalculation($scope.medicationObj);
								}
							}
							
							$scope.searchGenericItems = function(searchKeyword){
					        	$scope.genericList=[];
					        	var unitId = $scope.unitId
								var orgId = $scope.organizationId
								 var data={};
					        	var Arr = searchKeyword.split("|");
					        	searchKeyword = Arr[0];
								var URI = BASE_URL + ROOT_URL + "/api/opd/getGenericListForGenericSearch" + "?unitId=" + 
									unitId + "&orgId=" + orgId + "&searchDrug=" + searchKeyword
					    		return BillingGenericService.serviceAction(METHOD_GET,URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
					    			if(response.data.status == "success"){
					    				$scope.genericList = response.data.listObject;
					    			}
					    			return $scope.genericList;
					    		});
					    	}
							
							$scope.addMedicationGenericList = function($item,
									$model, $label, idx) {
								console.log($item);

								$rootScope.startLoader();

								$scope.medicationObj.itemId = $item.drugId;
								$scope.medicationObj.genericName = "";
								$scope.medicationObj.genericId = $item.genericId;
								$scope.medicationObj.searchItem = $item.medicationName;
								$scope.medicationObj.searchGeneric= $item.genericName
								$scope.medicationObj.genericName= $item.genericName

								var formulationTypeId = $item.formulationTypeId;
								var URI = BASE_URL
										+ ROOT_URL
										+ "/api/opd/getRoaList"
										+ "?unitId="
										+ $scope.unitId
										+ "&orgId="
										+ $scope.organizationId
										+ "&formulationTypeId="
										+ formulationTypeId
										+ "&genericId="
										+ $item.genericId
										+ "&patientId="
										+ $scope.postCareCommonObj.patientId;

								GenericServiceParamHeader
										.serviceAction(METHOD_GET, URI)
										.then(
												function(response) {
													if (response.data.status == "success") {
														$rootScope.stopLoader();
														var responseList = response.data.listObject;
														$scope.medicaRoaList = responseList;
														if (response.data.object == 0) {
															if (response.data.listObject.length !== 0) {
																$scope.roa_options = responseList;
															}
															$scope.medicationObj.genericName = $item.genericName;
															$scope.medicationObj.formulationTypeId = $item.formulationTypeId;
															$scope.medicationObj.strength = $item.strength;
														} else {
															$scope.checkgenericObj = {
																genericName : $item.genericName,
																formulationTypeId : $item.formulationTypeId,
																strength : $item.strength,
															}
															angular.element('#confirmAddItemPopup').modal('show');
														}
													}
												});

								var itemId = $item.drugId;
								var URI = BASE_URL + ROOT_URL
										+ "/api/opd/getUOMList" + "?unitId="
										+ $scope.unitId + "&orgId="
										+ $scope.organizationId + "&itemId="
										+ itemId;

								GenericServiceParamHeader
										.serviceAction(METHOD_GET, URI)
										.then(
												function(response) {
													if (response.data.status == "success") {
														if (response.data.listObject.length !== 0) {
															$scope.medicationObj.uomTypeId = response.data.listObject[0].uomTypeId;
															$scope.medicationObj.conversions = response.data.listObject[0].conversions;
														}
													}
												});

								$scope.showMedicationOptions = false;
								
								$scope.search = "";
								
								//get formuation dosage list
								var dataobj = {
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
										formulationTypeId:formulationTypeId
								};
								$scope.medicationObj.doseUnitId=$item.strengthUnitId;
								$scope.medicationObj.dose=$item.strength;
								
								$scope.medicationObj.prescribeDose="";
								$scope.medicationObj.itemStrength=$item.strength+" "+$item.unitDesc; 
								var URI = BASE_URL + ROOT_URL+ "/api/global/getFormulationTypeToDosageMapperListByFormulationTypeId";
								GenericService.serviceAction("POST", URI, dataobj).then(function(response) {
											$rootScope.stopLoader();
											$scope.medicationObj.prescribedDosageUnitList=[];
											$scope.counterFormulation=1;
											$scope.medicationObj.formulationCounterIndex="";
											angular.forEach(response.data.listObject,function(value,index){
												var unitObj={};
												unitObj.formulationDosageFromUnit=value.fromConversionUnit;
												unitObj.formulationDosageFromUnitId=value.fromConversionFactorUnit;
												unitObj.formulationDosageToUnit=value.toConversionUnit;
												unitObj.formulationDosageToUnitId=value.toConversionFactorUnit;
												unitObj.counterIndex=$scope.counterFormulation++;
												unitObj.formulationDosageFromConversionValue=value.fromConversionFactor;
												unitObj.formulationDosageToConversionValue=value.toConversionFactor;
												unitObj.formulationFactorId=value.factorId;
												unitObj.formulationDosage=value.dosageForm;
												unitObj.formulationDosageMappId=value.formulationDosageMappId;
												$scope.medicationObj.prescribedDosageUnitList.push(unitObj);
											});
											
											angular.forEach(response.data.listObject,function(value,index){
												var unitObj={};
												if(value.factorId==1){
													unitObj.formulationDosageFromUnit=value.toConversionUnit;
													unitObj.formulationDosageFromUnitId=value.toConversionFactorUnit;
													unitObj.formulationDosageToUnit=value.fromConversionUnit;
													unitObj.formulationDosageToUnitId=value.fromConversionFactorUnit;
													unitObj.counterIndex=$scope.counterFormulation++;
													unitObj.formulationDosageFromConversionValue=value.toConversionFactor;
													unitObj.formulationDosageToConversionValue=value.fromConversionFactor;
													unitObj.formulationFactorId=value.factorId;
													unitObj.formulationDosage=value.dosageForm;
													unitObj.formulationDosageMappId=value.formulationDosageMappId;
													$scope.medicationObj.prescribedDosageUnitList.push(unitObj);
												}
											});
											var list=[];
											if($scope.medicationObj.prescribedDosageUnitList.length>0 && $scope.medicationObj.prescribedDosageUnitList[0].formulationFactorId==1){
												 list=$.grep($scope.medicationObj.prescribedDosageUnitList, function (obj) {
											        return $scope.medicationObj.doseUnitId === obj.formulationDosageFromUnitId;
											    });
												if(list.length>0){	
													$scope.medicationObj.formulationCounterIndex=list[0].counterIndex;
												}
											}
								});
							}
							   // get ROA for non formulary start
							$scope.roa_options1 = [];
						    $scope.getROAForNonFormulary=function(){
						    	$scope.medicationObj.prescribedDosageUnitList=[];
						    	var URI = BASE_URL + ROOT_URL + "/api/opd/getRoaListNonFormulary" + "?unitId=" + 
								$scope.unitId + "&orgId=" + $scope.organizationId +"&patientId=" + $scope.postCareCommonObj.patientId;
							
								GenericServiceParamHeader.serviceAction(METHOD_GET, URI).then(function(response) {
								if(response.data.status == "success"){
										$scope.roa_options1 = response.data.listObject;
										console.log("non formulary ROA="+$scope.roa_options);
											
								}
							});
						    }
							$scope.continueToAllergy = function() {
								$scope.medicationDetails[$scope.checkgenericObj.medicationIndx].genericName = $scope.checkgenericObj.genericName;
								$scope.medicationDetails[$scope.checkgenericObj.medicationIndx].formulationTypeId = $scope.checkgenericObj.formulationTypeId;
								$scope.medicationDetails[$scope.checkgenericObj.medicationIndx].strength = $scope.checkgenericObj.strength;
								$scope.medicationDetails[$scope.checkgenericObj.medicationIndx].favorite_status = false;
								$scope.roa_options = $scope.medicaRoaList;
							}
						    
						    // get doseList
							$scope.doseList = [];
						    $scope.getDoseList = function(){
						    	var URI = BASE_URL + ROOT_URL + "/api/globals/strength/units/"+$scope.organizationId;
						GenericService.serviceAction("GET", URI, {})
								.then(function(response) {
									$scope.doseList = [];
									$scope.doseList = response.data;

								});
						    }
						    
						    $scope.getDoseList();
						    
						    $scope.calculateFre = function(idx) {
								
								var list=$.grep($scope.medicationObj.prescribedDosageUnitList, function (obj) {
							        return $scope.medicationObj.formulationCounterIndex === obj.counterIndex;
							    });
								var totalDuration="";
								if($scope.medicationObj.durationUnit=="Day"){
									totalDuration=parseInt($scope.medicationObj.durationCount)*1;
								}else if($scope.medicationObj.durationUnit=="Week"){
									totalDuration=parseInt($scope.medicationObj.durationCount)*7;
								}else if($scope.medicationObj.durationUnit=="Month"){
									totalDuration=parseInt($scope.medicationObj.durationCount)*30;
								}else{
									totalDuration=parseInt($scope.medicationObj.durationCount)*30*12;
								}
								if(list.length>0){	
									var presDoseObj=list[0];
									if(list.length>0  && presDoseObj.formulationFactorId==2){
										$scope.medicationObj.prescriptionDoseUnit=presDoseObj.formulationDosage;
									}else{
										$scope.medicationObj.prescriptionDoseUnit=presDoseObj.formulationDosageFromUnit;
									}
									$scope.medicationObj.prescribedDoseUnitId=presDoseObj.formulationDosageFromUnitId;
								}
								if(list.length>0  && $scope.medicationObj.prescribeDose!="" && $scope.medicationObj.prescribeDose!=undefined && $scope.medicationObj.prescribeDose!=null 
										&& $scope.medicationObj.formulationCounterIndex!="" && $scope.medicationObj.formulationCounterIndex!=undefined && $scope.medicationObj.formulationCounterIndex!=null){
									//for conversion factor
										if($scope.medicationObj.prescribedDoseUnitId==$scope.medicationObj.doseUnitId && $scope.medicationObj.dose!=0){
											$scope.medicationObj.quantity =Math.ceil(($scope.freQua* totalDuration*$scope.medicationObj.prescribeDose)/$scope.medicationObj.dose);
										}else{
											if(list.length>0 && $scope.medicationObj.dose!=0){	
											if(presDoseObj.formulationDosageToUnitId==$scope.medicationObj.doseUnitId){
												var convValue=Math.round(($scope.medicationObj.prescribeDose*presDoseObj.formulationDosageToConversionValue)/presDoseObj.formulationDosageFromConversionValue);
												 $scope.medicationObj.quantity = Math.ceil(($scope.freQua* totalDuration*convValue)/$scope.medicationObj.dose);
											}else{
											  $scope.medicationObj.quantity = Math.ceil(($scope.freQua* totalDuration*$scope.medicationObj.prescribeDose)/$scope.medicationObj.dose);
											}
										  }else{
											  $scope.medicationObj.quantity = Math.ceil(($scope.freQua
														* totalDuration)); 
										  }
											
										}
								}else{
									//using frequency factor 
									if(list.length>0  && presDoseObj.formulationFactorId==2){
										var splitString=(presDoseObj.formulationDosage).split(" ");
										var convValue=0;
										for(i=0;i<splitString.length;i++){
											var b=parseFloat(splitString[i]);
											if(Number.isNaN(b)==false){
												convValue=convValue+(math.number(math.fraction(splitString[i])));
											}
										}
										 $scope.medicationObj.quantity = Math.ceil($scope.freQua
													* totalDuration*(convValue==0?1:convValue)); 
									}else{
										$scope.medicationObj.quantity = Math.ceil(($scope.freQua * totalDuration));
									}
								}
							
							}
						    
							$scope.searchMedicationFreItems = function(searchKeyword) {
								$scope.itemList = [];
								var unitId = $scope.unitId
								var orgId = $scope.organizationId

								var URI = BASE_URL + ROOT_URL
										+ "/api/opd/getFrequencyList"
										+ "?unitId=" + unitId + "&orgId="
										+ orgId + "&searchPar=" + searchKeyword
								return BillingGenericService
										.serviceAction(METHOD_GET, URI, "",
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													if (response.data.status == "success") {
														$scope.itemList = response.data.listObject;
													}
													if($scope.itemList.length > 0)
														{
														return $scope.itemList;
														}
													else{
														$scope.medicationObj.searchItemFre='';
														$scope.medicationObj.frequencyId='';
													}
													
												});
							}
						    
						    $scope.addFrequencyItemList = function($item, $model, $label, idx) {
								$scope.storeItemOrder=$item;
								$scope.medicationObj.searchItemFre=$item.frequency;
								$scope.freQua = 0;
								$scope.medicationObj.frequencyId = $item.frequency_id;
								$scope.freQua = $item.noOfAdministrationTime;
								if($scope.medicationObj.durationCount == ""){
								$scope.medicationObj.durationCount = '';
								}
								var dur = 0;
								if ($scope.medicationObj.durationCount == '') {
									dur = 0;
								} else {
									 if($scope.medicationObj.durationUnit == "Day")
					    				{
				    					 dur = $scope.medicationObj.durationCount;
					    				 }
				    				 else if($scope.medicationObj.durationUnit == "Week")
						    			{
						    			  var  caldur = $scope.medicationObj.durationCount;
						    			    dur = caldur * 7;
						    			}
					    			else if($scope.medicationObj.durationUnit == "Month")
						    			{
						    			  var  caldur = $scope.medicationObj.durationCount;
						    			    dur = caldur * 30;
						    			}
							    		else if($scope.medicationObj.durationUnit == "Year")
						    			{
							    				
							    			var  caldur = $scope.medicationObj.durationCount;
						    				dur = caldur * 365;
						    		    }
								}
								$scope.medicationObj.quantity = $item.noOfAdministrationTime * dur;
								$scope.calculateFre(idx);
								
							}
						    
						    $scope.onDurationCountChange = function(medicationObj) {
								$scope.calculateEndDate(medicationObj)
							}
						    
						    $scope.calculateEndDate = function(medicationObj) {
								var currentMedication = medicationObj
								var medicationStartDate = currentMedication.medicationStartDate
								var indexOfCurrentMedication = $scope.medicationDetails.indexOf(currentMedication)
								var durationCount = parseInt(currentMedication["durationCount"])
								var durationUnit = currentMedication["durationUnit"]
								
							    	 if(durationUnit == "Day") {
										var durationCount = parseInt(currentMedication["durationCount"]);
										}
									    else if(durationUnit == "Week") {
											var durationCount = parseInt(currentMedication["durationCount"])
											var durationCount = durationCount* 7;
										}
										else if(durationUnit == "Month") {
											var durationCount = parseInt(currentMedication["durationCount"])
											var durationCount = durationCount* 30;
										}
										else if(durationUnit == "Year") {
											var durationCount = parseInt(currentMedication["durationCount"])
											var months = durationCount * 12
											var durationCount = months* 30;
										}
								
								if (durationCount && durationUnit && medicationStartDate) {
									if(durationUnit == "Day") {
										var endDate = moment(medicationStartDate).add(durationCount-1, 'days').format('YYYY-MM-DD');
									}
									else if(durationUnit == "Week") {
										var days = durationCount * 7
										var endDate = moment(medicationStartDate).add(durationCount, 'days').format('YYYY-MM-DD');
									}
									else if(durationUnit == "Month") {
										var days = durationCount * 30
										var endDate = moment(medicationStartDate).add(durationCount, 'days').format('YYYY-MM-DD');
									}
									else if(durationUnit == "Year") {
										var days = durationCount * 365;
										var endDate = moment(medicationStartDate).add(durationCount, 'days').format('YYYY-MM-DD');
									}
									currentMedication["medicationEndDate"] = endDate;
									currentMedication["medicationEndDateFormatted"] = moment(endDate).toDate();
									currentMedication["startDate"] = medicationStartDate;
									$scope.medicationDetails[indexOfCurrentMedication] = currentMedication
									$scope.model=currentMedication.searchItemFre;
									$scope.label=currentMedication.searchItemFre;
									$scope.idx=indexOfCurrentMedication;
									$scope.calculateFre($scope.idx);
								}else{
									$scope.medicationObj.quantity ="";
								}
							}
						    
							$scope.onDurationUnitChange = function(medicationObj) {
								$scope.calculateEndDate(medicationObj)
							}
							
							$scope.onStartDateChange = function(medicationObj) {
								$scope.calculateEndDate(medicationObj)
							}
							
							// for Medication Advice
                        	$scope.openDatePickerMedicationAdvice = function() {
								$scope.datepickerMedicationAdvice.opened = true;
							};

							$scope.datepickerMedicationAdvice = {
								opened : false
							};
                            $scope.dateOptionsMedicationAdvice = {
                            		formatYear : 'yyyy',
    								showWeeks : false,
    								minDate:new Date(), //max 
    							};
                            
                            $scope.openDatePickerMedicationAdviceEndDate = function() {
								$scope.datepickerMedicationAdviceEndDate.opened = true;
							};

							$scope.datepickerMedicationAdviceEndDate = {
								opened : false
							};
                        
                            $scope.dateOptionsMedicationAdviceEndDate = {
                            		formatYear : 'yyyy',
    								showWeeks : false,
    								minDate:new Date(), //max 
    							};
                            
                            // for patient discharge or not
                          $scope.disOrNotCount = {};
                          $scope.checkPatientDischargeOrNot = function(){
							var disObj = {
								unitId : $scope.unitId,
								organizationId : $scope.organizationId,
								admissionId : $scope.postCareCommonObj.admissionId
							}
							var URI = BASE_URL + ROOT_URL + "/adt/getPatientDischargeOrNot";
							BillingGenericService
									.serviceAction(METHOD_POST, URI,
											disObj,
											NOTIFICATION_MSG_STATUS_FALSE)
									.then(
											function(response) {
												if (response.data.status == "success")
													$scope.disOrNotCount = response.data.object;
											});
                          }
                          if($scope.postCareCommonObj.admissionId!=null){
                        	  $scope.checkPatientDischargeOrNot();
                          }	  
                          
                          $scope.priorityList = [];
                          $scope.getPriorityList=function(){
								var URI = BASE_URL + ROOT_URL+ "/adt/getActivePriorityList";
								GenericService.serviceAction("GET", URI, {})
								.then(function(response) {
									$scope.priorityList = [];
									$scope.priorityList = response.data.listObject;
									console.log("$scope.priorityList",$scope.priorityList);
								});
							}
							$scope.getPriorityList();
							
							
							$scope.updateMedication = function() {
								$scope.medicationDetails[$scope.medicationIndex] = $scope.medicationObj;
								$scope.medicationDetails[$scope.medicationIndex].medicationStartDate = moment($scope.medicationObj.medicationStartDate).format('YYYY-MM-DD');
								$scope.medicationDetails[$scope.medicationIndex].medicationEndDate = moment($scope.medicationObj.medicationEndDate).format('YYYY-MM-DD');
								if($scope.medicationDetails[$scope.medicationIndex].prescriptionId!=null && $scope.medicationDetails[$scope.medicationIndex].prescriptionId>0)
									{
									var prescriptionId = $scope.medicationDetails[$scope.medicationIndex].prescriptionId;
									 var found = $scope.medicationDetails.find(ar=>ar.prescriptionId==prescriptionId);
									   if(found==undefined)
										   {
										     $scope.addEmptyRow();
										     $scope.saveBtnFlag = false;
											 $scope.updateBtnFlag = true;
											 $scope.updateFlag = false;
											 $scope.updateFlagValidation = false;
											 $scope.saveFlag = true;
										   }
									   else{
										   $scope.medicationDetails.splice($scope.medicationIndex, 1);
										   $scope.addEmptyRow();
										    $scope.saveBtnFlag =false ;
											 $scope.updateBtnFlag = true;
											 $scope.updateFlag = false;
											 $scope.updateFlagValidation = false;
											 $scope.saveFlag = true;
									   }
									}
								else{
									$scope.updateFlag = false;
									$scope.updateFlagValidation = false;
									$scope.saveFlag = true;
									$scope.saveBtnFlag = true;
									$scope.updateBtnFlag = false;
									$scope.medicationDetails.splice($scope.medicationIndex, 1);
									 $scope.addEmptyRow();
								}
							}
							$scope.favouriteMedications = [];
							$scope.getFavoritesMedication = function() {
								var data = {
									doctorId :$scope.postCareCommonObj.doctorId,
									organizationId : $scope.organizationId,
									unitId : $scope.unitId
								};
								var URI = BASE_URL + ROOT_URL + GETFAVOURITEMEDICATION;

								GenericServiceParamHeader
										.serviceAction(METHOD_POST, URI, data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													if (response.data.status == "success") {
														$scope.favouriteMedications = response.data.listObject;
													}
												});
							}
							
							$scope.updateMedicationFavouriteStatus = function(medication) {
								$rootScope.startLoader();
								var data = {
									itemId : medication.itemId,
									doctorId : $scope.postCareCommonObj.doctorId,
									createdBy : $scope.userId,
									updatedBy : $scope.userId,
									createdDate : moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
									updatedDate : moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
								};

								if(medication.favorite_status == false)
		        				{
			        				 data.roaId = medication.roaId;
			        				 data.dose = medication.dose;
			        				 data.doseUnitId = medication.doseUnitId;
			        				 data.frequencyId = medication.frequencyId;
			        				 data.doctorFavoriteId=medication.doctorFavoriteId;
			        				 data.status = 'A';
		        				}
		        			else
		        				{
		        				data.doctorFavoriteId=medication.doctorFavoriteId;
		        				data.status = 'I';
		        				}
								var URI = BASE_URL + ROOT_URL + SAVEDOCTORFAVORITS;

								GenericServiceParamHeader
										.serviceAction(METHOD_POST, URI, data,
												NOTIFICATION_MSG_STATUS_TRUE)
										.then(
												function(response) {
													if (response.data.status == "success") {
														$scope.getFavoritesMedication();
														  var medicationObj =  medication;
														  medicationObj["favorite_status"] =  !medicationObj["favorite_status"];
														  var indexOfMedication =  $scope.medicationDetails.indexOf(medication);
														  $scope.medicationDetails[indexOfMedication] = medicationObj;
														  $rootScope.stopLoader();
													}
												});
							}
							
							$scope.editMedication = function(index) {
								$scope.updateFlag = true;
								$scope.updateFlagValidation = true;
								$scope.saveFlag = false;
								$scope.item={};
								$scope.model={};
								$scope.label={};
								$scope.medicationObj = angular.copy($scope.medicationDetails[index]);
								$scope.medicationObj.medicationStartDate=moment($scope.medicationDetails[index].medicationStartDate,'YYYY-MM-DD').toDate();
								$scope.searchItem = $scope.medicationDetails[index].itemName;
								$scope.medicationObj.searchItem = $scope.medicationDetails[index].itemName;
								$scope.medicationObj.medicationName = $scope.medicationDetails[index].itemName;
								$scope.medicationObj.searchGeneric = $scope.medicationDetails[index].genericName;
								$scope.medicationObj.isGeneric ='N';
								$scope.medicationObj.nonFormularyAdvice ='N';
								$scope.medicationObj.prescribedDosageUnitList =[];
								$scope.medicationObj.searchItemFre = $scope.medicationDetails[index].frequencyDesc;
								$scope.medicationIndex = index;
								$scope.medicationDetails[index].genericId=$scope.medicationObj.genericId;
								$scope.medicationObj.itemStrength=$scope.medicationDetails[index].dose+" "+$scope.medicationDetails[index].strengthDesc;
								
								$scope.frequency_id=$scope.medicationDetails[index].frequencyId;
								$scope.frequencyDesc=$scope.medicationDetails[index].frequencyDesc;
								$scope.noOfAdministrationTime=$scope.medicationDetails[index].noOfAdministrationTime;
								$scope.item={
										frequency_id:$scope.frequency_id,
										frequency:$scope.frequencyDesc,
										noOfAdministrationTime:$scope.noOfAdministrationTime
								}
								$scope.model=$scope.medicationDetails[index].frequencyDesc;
								$scope.label=$scope.medicationDetails[index].frequencyDesc;
								$scope.addFrequencyItemList($scope.item, $scope.model, $scope.label,index);
								var URI = BASE_URL
										+ ROOT_URL
										+ "/api/opd/getRoaList"
										+ "?unitId="
										+ $scope.unitId
										+ "&orgId="
										+ $scope.organizationId
										+ "&formulationTypeId="
										+ $scope.medicationDetails[index].formulationTypeId
										+ "&genericId="
										+ $scope.medicationDetails[index].genericId
										+ "&patientId="
										+ $scope.postCareCommonObj.patientId;

								GenericServiceParamHeader
										.serviceAction(METHOD_GET, URI)
										.then(function(response) {
													if (response.data.status == "success") {
														$scope.roa_options = response.data.listObject;
                                                //prescribed dosage unit list
					  										   $scope.medicationObj.prescribeDose="";
					  										var dataobj = {
																	organizationId : $scope.organizationId,
																	unitId : $scope.unitId,
																	formulationTypeId:$scope.medicationDetails[index].formulationTypeId
															};
															var URI = BASE_URL + ROOT_URL+ "/api/global/getFormulationTypeToDosageMapperListByFormulationTypeId";
															GenericService.serviceAction("POST", URI, dataobj).then(function(response) {
																$rootScope.stopLoader();
																$scope.medicationObj.prescribedDosageUnitList=[];
																$scope.counterFormulation=1;
																$scope.medicationObj.formulationCounterIndex="";
																angular.forEach(response.data.listObject,function(value,index){
																	var unitObj={};
																	unitObj.formulationDosageFromUnit=value.fromConversionUnit;
																	unitObj.formulationDosageFromUnitId=value.fromConversionFactorUnit;
																	unitObj.formulationDosageToUnit=value.toConversionUnit;
																	unitObj.formulationDosageToUnitId=value.toConversionFactorUnit;
																	unitObj.counterIndex=$scope.counterFormulation++;
																	unitObj.formulationDosageFromConversionValue=value.fromConversionFactor;
																	unitObj.formulationDosageToConversionValue=value.toConversionFactor;
																	unitObj.formulationFactorId=value.factorId;
																	unitObj.formulationDosage=value.dosageForm;
																	unitObj.formulationDosageMappId=value.formulationDosageMappId;
																	$scope.medicationObj.prescribedDosageUnitList.push(unitObj);
																});
																
																angular.forEach(response.data.listObject,function(value,index){
																	var unitObj={};
																	if(value.factorId==1){
																		unitObj.formulationDosageFromUnit=value.toConversionUnit;
																		unitObj.formulationDosageFromUnitId=value.toConversionFactorUnit;
																		unitObj.formulationDosageToUnit=value.fromConversionUnit;
																		unitObj.formulationDosageToUnitId=value.fromConversionFactorUnit;
																		unitObj.counterIndex=$scope.counterFormulation++;
																		unitObj.formulationDosageFromConversionValue=value.toConversionFactor;
																		unitObj.formulationDosageToConversionValue=value.fromConversionFactor;
																		unitObj.formulationFactorId=value.factorId;
																		unitObj.formulationDosage=value.dosageForm;
																		unitObj.formulationDosageMappId=value.formulationDosageMappId;
																		$scope.medicationObj.prescribedDosageUnitList.push(unitObj);
																	}
																});
																var list=[];
																if($scope.medicationObj.prescribedDosageUnitList.length>0 && $scope.medicationObj.prescribedDosageUnitList[0].formulationFactorId==1){
																	 list=$.grep($scope.medicationObj.prescribedDosageUnitList, function (obj) {
																        return $scope.medicationDetails[index].doseUnitId === obj.formulationDosageFromUnitId;
																    });
																	if(list.length>0){	
																		$scope.medicationObj.formulationCounterIndex=list[0].counterIndex;
																	}
																}
																
																if($scope.medicationObj.prescribedDosageUnitList.length>0 && $scope.medicationObj.prescribedDosageUnitList[0].formulationFactorId==2){
																	 list=$.grep($scope.medicationObj.prescribedDosageUnitList, function (obj) {
																        return $scope.medicationDetails[index].dosageForm === obj.formulationDosage;
																    });
																	if(list.length>0){	
																		$scope.medicationObj.formulationCounterIndex=list[0].counterIndex;
																	}
																}
													});		
														
													}
												});

							}
							
							$scope.removeMedication = function(medication) {
								if(medication.checkFlagValidation != true){
								if ($scope.medicationDetails.length > 0) {
									var indexOfMedication = $scope.medicationDetails .indexOf(medication);
									$scope.medicationDetails.splice(indexOfMedication, 1);
								}
								}else{
									 $scope.medication= medication;
									 angular.element('#medicationDiagnosis').modal('show');
								}
							}
							
							$scope.removeMedicationOrdderset = function(){
								$scope.medication.checkFlagValidation=false;
								$scope.removeMedication($scope.medication);
							}
							
							$scope.activeMedications = [];
							$scope.getAllActiveMedication = function() {
								$scope.activeMedications = [];
								var patientId = $scope.postCareCommonObj.patientId;
								var dateObj = moment(new Date()).format('YYYY-MM-DD');

								var URI = BASE_URL + ROOT_URL
										+ GETACTIVEMEDICATION + "?patientId="
										+ patientId + "&date=" + dateObj+ "&unitId="+ $scope.unitId
								GenericServiceParamHeader
										.serviceAction(METHOD_GET, URI)
										.then(function(response) {
													if (response.data.status == "success") {
														$scope.activeMedications = response.data.listObject;
													
														angular.forEach($scope.activeMedications,function(value,key){
								    						value.checkTrue='N';
								    					});
													}
												});
							}
							
							//List Of Continued Medication of medication History
							$scope.continuedMedicationHistoryList = [];
							$scope.getContinuedMedicationHistoryList = function() {
								$scope.continuedMedicationHistoryList = [];
								var data = {
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									patientId : $scope.postCareCommonObj.patientId,
								};
								var URI = BASE_URL + ROOT_URL
										+ "/api/opd/getContinuedMedicationList";
								GenericServiceParamHeader
										.serviceAction(METHOD_POST, URI, data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													if (response.data.status == "success") {
														$scope.continuedMedicationHistoryList = response.data.listObject;
														angular.forEach($scope.continuedMedicationHistoryList,function(value,key){
								    						value.checkTrue='N';
								    					});
													}
												});
							}
							$scope.getContinuedMedicationHistoryList();
							
							
							// Detailed Medication
							$scope.detailedMedications = [];
							$scope.getDetailedMedication = function() {
								$scope.detailedMedications = [];
								 var headerObj = {
											unitId : $scope.unitId,
											organizationId : $scope.organizationId
										};
									
									var paramObjForPer = {
											patientId : $scope.postCareCommonObj.patientId,
											doctorId : $scope.postCareCommonObj.doctorId
										};

									if($scope.postCareCommonObj.admissionId!=null){
										paramObjForPer.admissionId = $scope.postCareCommonObj.admissionId;
									}else{
										paramObjForPer.encounterId = $scope.postCareCommonObj.encounterId;
									}
								var URI = BASE_URL + ROOT_URL + "/api/opd/getMedicationDetailsByDoctorId";
								GenericServiceParamHeader
										.serviceAction(METHOD_GET, URI, "",
												NOTIFICATION_MSG_STATUS_FALSE,paramObjForPer,headerObj)
										.then(
												function(response) {
													if (response.data.status == "success") {
														if(response.data.listObject!=null && response.data.listObject.length>0){
															angular.forEach(response.data.listObject,function(value,index){
																if(value.isOtims!=null && value.isOtims){
																	$scope.detailedMedications.push(value);
																}
															});
														}
														//$scope.detailedMedications = response.data.listObject;
													}
												});
							}
							$scope.getDetailedMedication();
							
							
							
							
							$scope.deleteMedicationPendingStatus = function(){
								$rootScope.startLoader();
								try{
									var date = new Date();
									$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
									var data={
										    updatedBy:$scope.userId,
											updatedDate:$scope.dt,
											prescriptionId:$scope.prescriptionId,
											organizationId:$scope.organizationId,
											justificationStop:$scope.justificationDeleteMedi,
											prescriptionDetailsid:$scope.prescriptionDetailsid1,
									};
									
									var URI = BASE_URL + ROOT_URL + "/api/opd/saveJustificationForMedicationDelete";
									GenericService.serviceAction("POST",URI,data).then(function(response){
										if (response.data.status == 'success')
										{
											$rootScope.stopLoader();
											growl.success("Deleted Successfully..!!",
													{
														title : 'Success!'
													});
											$scope.getAllActiveMedication();
											$scope.getDetailedMedication();
											$scope.justificationDeleteMedi="";
										} else if(response.data.status == 'error')
										{
											$rootScope.stopLoader();
											growl.warning(response.data.message,
													{
														title : 'Warning!'
													});
											$scope.getAllActiveMedication();
											$scope.getDetailedMedication();
											$scope.justificationDeleteMedi="";
										}
									});
								}catch(e){
									console.log("Exception",e);
								}
							}
						
							// Activity Popup 
/*************************************** ACTIVITY START ****************************************************************/									
							$scope.advicedInstruction='';
							$scope.instructions = [];
							$scope.getInstructionsBySplecilityId = function() {
								var commonObj = {
											"organizationId": $scope.organizationId,
											"specialityId": $scope.postCareCommonObj.specialityId,
											"unitId": $scope.unitId
									}
									var URI = BASE_URL + ROOT_URL + "/api/common/getSpecialityByDept";

									BillingGenericService.serviceAction(METHOD_POST, URI,commonObj,
													NOTIFICATION_MSG_STATUS_FALSE).then(
													function(response) {
														if (response.data.status == "success")
															$scope.instructions = response.data.listObject;
													});

							}
							$scope.getInstructionsBySplecilityId();
							
							$scope.savedInstructions = [];
							$scope.getInstructions = function() {
								$scope.savedInstructions = [];
								var data = {};
								if($scope.postCareCommonObj.admissionId!=null){
									data.encounterId = $scope.postCareCommonObj.admissionId;
								}else{
									data.encounterId - $scope.postCareCommonObj.encounterId;
								}
								var URI = BASE_URL + ROOT_URL + GETINSTRUCTIONS + "/"+$scope.postCareCommonObj.visitTypeId;

								GenericServiceParamHeader
										.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE)
										.then(function(response) {
													if (response.data.status == "success") {
														if(response.data.listObject!=null && response.data.listObject.length>0){
														angular.forEach(response.data.listObject,function(value,key){
															if(value.isOtims!=null && value.isOtims 
																	&& value.activityOrIntrcs!=null && value.activityOrIntrcs == 0){
																value.editAdvice=false;
																$scope.savedInstructions.push(value);
															}
														});
														}
													}
												})
							}
							$scope.getInstructions();
							
							$scope.getMasterInstructions = function() { 
								  $scope.instructionSelectedList = {
											items: []
										  };
								  $scope.items=[];
								  $scope.checkListDuplicateInstruction1=[];
									$scope.checkListDuplicateInstruction1=$scope.checkListDuplicateInstruction1.concat($scope.savedInstructions);
									$scope.checkListDuplicateInstruction1=$scope.checkListDuplicateInstruction1.concat($scope.addedInstructions);
									
									angular.forEach($scope.instructions ,function(value,key){
										$scope.flag=false;
										angular.forEach($scope.checkListDuplicateInstruction1,function(value1,key){
										if(value.instructionId == value1.instructionDetailsId){
											 $scope.flag=true;
										}
										});
										if($scope.flag){
											$scope.items.push(value)
										}
									});
									
									 $scope.instructionSelectedList.items = $scope.items;
									  console.log("data selected",$scope.instructionSelectedList);
							  }
							
							
							$scope.addedInstructions = [];
							$scope.instructionSelectedList = {
									items : []
								};
							$scope.addSelectedInstructions = function() {
								$scope.itemInstruction=[];
								$scope.checkListDuplicateInstruction=[];
								$scope.checkListDuplicateInstruction=$scope.checkListDuplicateInstruction.concat($scope.savedInstructions);
								$scope.checkListDuplicateInstruction=$scope.checkListDuplicateInstruction.concat($scope.addedInstructions);
								
								angular.forEach($scope.instructionSelectedList.items,function(value,key){
									$scope.insCheckFlag=false
									angular.forEach($scope.checkListDuplicateInstruction,function(value1,key){
										if(value1.instructionDetailsId == value.instructionId){
											$scope.insCheckFlag=true;
										}
									});
									if(!$scope.insCheckFlag){
										$scope.itemInstruction.push(value);
									}
								});
								$scope.instructionSelectedList.items=$scope.itemInstruction;
								
								
								var insCheckList = $scope.instructionSelectedList.items;
								for (var i = 0; i < insCheckList.length; i++) {
									var selectedInstructionObj = {};
									selectedInstructionObj["unitId"] = $scope.unitId;
									selectedInstructionObj["organizationId"] = $scope.organizationId;
									selectedInstructionObj["notes"] = '';
									selectedInstructionObj["patientId"] = $scope.postCareCommonObj.patientId;
									selectedInstructionObj["admissionId"] = $scope.postCareCommonObj.admissionId;
									selectedInstructionObj["createdDate"] = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
									selectedInstructionObj["updatedDate"] = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
									selectedInstructionObj["createdBy"] = $scope.userId;
									selectedInstructionObj["updatedBy"] = $scope.userId;
									selectedInstructionObj["instructionDesc"] = insCheckList[i].instructionDesc;
									selectedInstructionObj["instructionDetailsId"] = insCheckList[i].instructionId;
									selectedInstructionObj["status"] = 'A';
									selectedInstructionObj["activityOrIntrcs"] = 0;

									$scope.addedInstructions.push(selectedInstructionObj);
								}

								$scope.instructionSelectedList = {
									items : []
								};

							}
							
							$scope.removeAddedInstruction = function(index) {
								$scope.addedInstructions.splice(index, 1);
							}
							
							$scope.getInstructionId=function(id){
								$scope.instructionIdf=id;
							}
							
							$scope.cancelInstruct=function(){
								$scope.instructionIdf="";
							}
							
						/*	$scope.editInstructionNotes = function(instruction,idx) {
								$scope.saveBtnInstruction=false;
								$scope.updateBtnInstruction=true;
								$scope.InstructionEditIndex=idx;
								angular.forEach($scope.savedInstructions,function(value,index){
									if(idx == index){
										value.editAdvice=true;
									}
								});
								$scope.instructionSelectedList = {
										items: []
									  };
							}*/
							
							$scope.updateInstructionStatus = function() {
								$rootScope.startLoader();
								var date = new Date();
								$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
								data={
										instructionId:$scope.instructionIdf,
										status:'D',
										updatedBy:$scope.userId,
										updatedDate:$scope.dt
								};
								
								var URI = BASE_URL + ROOT_URL + "/api/opd/updateCoversheetInstructionStatus";
								GenericServiceParamHeader.serviceAction(METHOD_POST, URI,
										data).then(function(response) {
												if(response.data.status == "success"){
													$rootScope.stopLoader();
													growl.success('Removed Successfully',
															{
																title : 'Success!'
															});
													$scope.getInstructions();
													$scope.addedInstructions = [];
													$rootScope.stopLoader();
												}
								});
								
							}
/*************************************** ACTIVITY END ****************************************************************/		
							
/*************************************** SPECIAL INSTRUCTION START ****************************************************************/	
							$scope.advicedSplInstruction='';
							 //get instruction for pop up 
                            $scope.medicationInstructions = [];
                            $scope.getMedicationInstruction = function(){
                            	var commonObjInst = {
    									isMedication : 'Y',
    									organizationId : $scope.organizationId
    								}
    								var URI = BASE_URL + ROOT_URL + GETMEDICATIONINSTRUCTIONS;

    								BillingGenericService
    										.serviceAction(METHOD_POST, URI,
    												commonObjInst,
    												NOTIFICATION_MSG_STATUS_FALSE)
    										.then(
    												function(response) {
    													if (response.data.status == "success")
    														$scope.medicationInstructions = response.data.listObject;
    												});
                            }
                            $scope.getMedicationInstruction();
							
							$scope.instructionSelectedSplList = {
									items : []
								};
							
							$scope.savedSplInstructions = [];
							$scope.getSplInstructions = function() {
								$scope.savedSplInstructions = [];
								var data = {};
								if($scope.postCareCommonObj.admissionId!=null){
									data.encounterId = $scope.postCareCommonObj.admissionId;
								}else{
									data.encounterId - $scope.postCareCommonObj.encounterId;
								}
								var URI = BASE_URL + ROOT_URL + GETINSTRUCTIONS + "/" + $scope.postCareCommonObj.visitTypeId;

								GenericServiceParamHeader
										.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE)
										.then(function(response) {
													if (response.data.status == "success") {
														if(response.data.listObject!=null && response.data.listObject.length>0){
														angular.forEach(response.data.listObject,function(value,key){
															if(value.isOtims!=null && value.isOtims 
																	&& value.activityOrIntrcs!=null && value.activityOrIntrcs == 1){
																value.editAdvice=false;
																$scope.savedSplInstructions.push(value);
															}
														});
														}
													}
												})
							}
							$scope.getSplInstructions();
							
							$scope.getMasterSplInstructions = function() { 
								  $scope.instructionSelectedSplList = {
											items: []
										  };
								  $scope.splItems=[];
								  $scope.checkListDuplicateSplInstruction1=[];
									$scope.checkListDuplicateSplInstruction1=$scope.checkListDuplicateSplInstruction1.concat($scope.savedSplInstructions);
									$scope.checkListDuplicateSplInstruction1=$scope.checkListDuplicateSplInstruction1.concat($scope.addedSplInstructions);
									
									angular.forEach($scope.medicationInstructions ,function(value,key){
										$scope.splFlag=false;
										angular.forEach($scope.checkListDuplicateSplInstruction1,function(value1,key){
										if(value.instructionId == value1.instructionDetailsId){
											 $scope.splFlag=true;
										}
										});
										if($scope.splFlag){
											$scope.splItems.push(value)
										}
									});
									
									 $scope.instructionSelectedSplList.items = $scope.splItems;
									  console.log("data selected",$scope.instructionSelectedSplList);
							  }
							
							
							$scope.addedSplInstructions = [];
							$scope.instructionSelectedSplList = {
									items : []
								};
							$scope.addSelectedSplInstructions = function() {
								$scope.itemSplInstruction=[];
								$scope.checkListDuplicateSplInstruction=[];
								$scope.checkListDuplicateSplInstruction=$scope.checkListDuplicateSplInstruction.concat($scope.savedSplInstructions);
								$scope.checkListDuplicateSplInstruction=$scope.checkListDuplicateSplInstruction.concat($scope.addedSplInstructions);
								
								angular.forEach($scope.instructionSelectedSplList.items,function(value,key){
									$scope.insSplCheckFlag=false
									angular.forEach($scope.checkListDuplicateSplInstruction,function(value1,key){
										if(value1.instructionDetailsId == value.instructionId){
											$scope.insSplCheckFlag=true;
										}
									});
									if(!$scope.insSplCheckFlag){
										$scope.itemSplInstruction.push(value);
									}
								});
								$scope.instructionSelectedSplList.items=$scope.itemSplInstruction;
								
								
								var insCheckList = $scope.instructionSelectedSplList.items;
								for (var i = 0; i < insCheckList.length; i++) {
									var selectedInstructionObj = {};
									selectedInstructionObj["unitId"] = $scope.unitId;
									selectedInstructionObj["organizationId"] = $scope.organizationId;
									selectedInstructionObj["notes"] = '';
									selectedInstructionObj["patientId"] = $scope.postCareCommonObj.patientId;
									selectedInstructionObj["admissionId"] = $scope.postCareCommonObj.admissionId;
									selectedInstructionObj["createdDate"] = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
									selectedInstructionObj["updatedDate"] = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
									selectedInstructionObj["createdBy"] = $scope.userId;
									selectedInstructionObj["updatedBy"] = $scope.userId;
									selectedInstructionObj["instructionDesc"] = insCheckList[i].instructionDesc;
									selectedInstructionObj["instructionDetailsId"] = insCheckList[i].instructionId;
									selectedInstructionObj["status"] = 'A';
									selectedInstructionObj["activityOrIntrcs"] = 1;

									$scope.addedSplInstructions.push(selectedInstructionObj);
								}

								$scope.instructionSelectedSplList = {
									items : []
								};

							}
							
							$scope.removeAddedSplInstruction = function(index) {
								$scope.addedSplInstructions.splice(index, 1);
							}
							
							$scope.getSplInstructionId=function(id){
								$scope.instructionSplIdf=id;
							}
							
							$scope.cancelSplInstruct=function(){
								$scope.instructionSplIdf="";
							}
							
							/*$scope.editSplInstructionNotes = function(instruction,idx) {
								$scope.saveBtnSplInstruction=false;
								$scope.updateBtnSplInstruction=true;
								$scope.InstructionSplEditIndex=idx;
								angular.forEach($scope.savedSplInstructions,function(value,index){
									if(idx == index){
										value.editAdvice=true;
									}
								});
								$scope.instructionSelectedSplList = {
										items: []
									  };
							}*/
							
							$scope.updateSplInstructionStatus = function() {
								$rootScope.startLoader();
								var date = new Date();
								$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
								data={
										instructionId:$scope.instructionSplIdf,
										status:'D',
										updatedBy:$scope.userId,
										updatedDate:$scope.dt
								};
								
								var URI = BASE_URL + ROOT_URL + "/api/opd/updateCoversheetInstructionStatus";
								GenericServiceParamHeader.serviceAction(METHOD_POST, URI,
										data).then(function(response) {
												if(response.data.status == "success"){
													$rootScope.stopLoader();
													growl.success('Removed Successfully',
															{
																title : 'Success!'
															});
													$scope.getSplInstructions();
													$scope.addedSplInstructions = [];
													$rootScope.stopLoader();
												}
								});
								
							}
							
							
							
/*************************************** SPECIAL INSTRUCTION END ****************************************************************/	

/*************************************** Followup Notes START ****************************************************************/								
							
							$scope.selectNote = function(){
								$scope.followUpNoteList =[];
								$scope.followUpNoteList.push($scope.ckObj);
							}
							//get FollowUp Notes List by ProReqId
							$scope.followUpNoteList = [];
							$scope.followUpNoteListNew = [];
							$scope.templatedropdown = false;
							$scope.dietDetails = [];
							$scope.postOperativeCareId =  null ;
							$scope.getFollowUpNotes = function(){
								$scope.followUpNoteList = [];
								$scope.dietDetails = [];
								$scope.startLoader();
								var URI = BASE_URL + ROOT_URL + "/api/otims/PostOperativeCareDetails/getNotesListByProReqId/" + $scope.organizationId + "/" + $scope.unitId + "/" + $scope.postCareCommonObj.proReqId ;
								GenericService.serviceAction(METHOD_GET, URI,"").then(function(response) {
										if (response.data.status == "success") {
											if(response.data.object!=null){
											if(response.data.object.oTCompletionNoteDtoList!=null && response.data.object.oTCompletionNoteDtoList.length>0){
												angular.forEach(response.data.object.oTCompletionNoteDtoList,function(value,index){
													if(value.noteTypeId == 1){
													$scope.templatedropdown  = true;
													value.unitId = $scope.unitId;
													value.organizationId  = $scope.organizationId;
													value.status = 'A';
													value.createdDate = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
													value.updatedDate = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
													value.updatedBy = $scope.userId;
													value.createdBy = $scope.userId;
													value.proReqId =  $scope.postCareCommonObj.proReqId;
													$scope.followUpNoteListNew.push(value);
													$scope.followUpNoteList.push(value);
													$scope.ckObj=value;
													}
												});
												
												$scope.postOperativeCareId = response.data.object.postOperativeCareId ; 
												$scope.dietDetails = response.data.object.otMealList ; 
											}
											}else if(response.data.listObject!=null && response.data.listObject.length>0){
												$scope.postOperativeCareId =  null ;
												angular.forEach(response.data.listObject,function(value,index){
													if(index == 0){
													value.unitId = $scope.unitId;
													value.organizationId  = $scope.organizationId;
													value.status = 'A';
													value.createdDate = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
													value.updatedDate = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
													value.updatedBy = $scope.userId;
													value.createdBy = $scope.userId;
													value.proReqId =  $scope.postCareCommonObj.proReqId;
													$scope.ckObj=value;
													$scope.followUpNoteList.push(value);
													}
													$scope.followUpNoteListNew.push(value);
													
												});
											}
										}
								});
							}	
							$scope.getFollowUpNotes();
							
/*************************************** Followup Notes END ****************************************************************/					
							
/*************************************** DIET START ****************************************************************/
							//get Meal List 
							$scope.mealList= [];
							$scope.getMealList = function(){
								$scope.mealList= [];
								$rootScope.startLoader();
		
								var URI = BASE_URL + ROOT_URL + "/api/kitchen/master/mealMaster/getMealMasterDropDown?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId  ;
								GenericServiceParamHeader.serviceAction(METHOD_GET, URI,"").then(function(response) {
												if(response.data.status == "success"){
													$rootScope.stopLoader();
													$scope.mealList= response.data.listObject;
												}
												$rootScope.stopLoader();
								});
							}
							$scope.getMealList();
							
							//get Dish List 
							$scope.dishList= [];
							$scope.getDishList = function(){
								$scope.dishList= [];
								$rootScope.startLoader();
						
								var URI = BASE_URL + ROOT_URL + "/api/kitchen/master/dishMaster/getDishMasterDropDown?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId  ;
								GenericServiceParamHeader.serviceAction(METHOD_GET, URI,"").then(function(response) {
												if(response.data.status == "success"){
													$rootScope.stopLoader();
													if(response.data.listObject!=null && response.data.listObject.length>0){
														angular.forEach(response.data.listObject,function(value,index){
															var newObj = {};
															newObj.id = value.dishId;
															newObj.label = value.dishDescription;
															$scope.dishList.push(newObj);
														});
													}
												}
												$rootScope.stopLoader();
								});
							}
							$scope.getDishList();
							
							$scope.mealObj={
									mealId:"",
									mealName:""
							};
							$scope.selectedDishList=[];
							$scope.selectedDishes = [];
							$scope.clicked = function() {
								$scope.selectedDishes = [];
									if ($scope.selectedDishList.length > 0) {
										angular.forEach($scope.selectedDishList,function(value, index) {
											angular.forEach($scope.dishList,function(val,idx){
												if(value.id == val.id){
													$scope.selectedDishes.push(val);
												}
											});
										});
								}
							}
							
							$scope.dietDetails = [];
							$scope.addDietDetails = function(){
								$rootScope.startLoader();
								if($scope.mealObj.mealId == "" || $scope.mealObj.mealId ==null){
									growl.warning("Please Select Meal",
											{
												title : 'Warning!'
											});
									$rootScope.stopLoader();
									return false;
								}
								
								if($scope.selectedDishList.length ==0){
									growl.warning("Please Select Dish",
											{
												title : 'Warning!'
											});
									$rootScope.stopLoader();
									return false;
								}
								
								var isMealExist = false;
							
								if($scope.dietDetails.length>0){ //grd
									angular.forEach($scope.dietDetails,function(value,index){
										if(value.mealId == $scope.mealObj.mealId ){
											isMealExist = true;
										}
									});
									
										
										if(isMealExist){
											angular.forEach($scope.dietDetails,function(value,index){ 
												
											if(value.mealId == $scope.mealObj.mealId ){
												
												value.otDishList = []; // replace with new dishes
												if($scope.selectedDishList!=null && $scope.selectedDishList.length>0){
													angular.forEach($scope.selectedDishList,function(val,idx){
														var diet = {};
														diet.otDishId = val.otDishId;
														diet.otMealId = $scope.mealObj.mealId;
														diet.dishId  = val.id;
														diet.status = 'A';
														diet.organizationId  = $scope.organizationId;
														diet.unitId  = $scope.unitId;
														diet.createdDate = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
														diet.updatedDate = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
														diet.updatedBy = $scope.userId;
														diet.createdBy = $scope.userId;
														value.otDishList.push(diet);
													});
												}
												var selectedDishes = "";
												if( $scope.selectedDishes!=null &&  $scope.selectedDishes.length>0){
													angular.forEach( $scope.selectedDishes,function(v,i){
														if(value.status=='A'){
														if(selectedDishes==""){
															selectedDishes = v.label;
														}else{
															selectedDishes = selectedDishes +" , " +v.label;
														}
														}
													});
												}
												value.selectedDishes = selectedDishes;
											}
											
										});
										}else{
											
											var newArr = {};
											newArr.otMealId = null;
											newArr.mealId = $scope.mealObj.mealId;
											newArr.mealName = $scope.mealObj.mealName;
											newArr.unitId = $scope.unitId;
											newArr.organizationId  = $scope.organizationId;
											newArr.status = 'A';
											newArr.createdDate = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
											newArr.updatedDate = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
											newArr.updatedBy = $scope.userId;
											newArr.createdBy = $scope.userId;
											newArr.otDishList = [];
											if($scope.selectedDishList!=null && $scope.selectedDishList.length>0){
												angular.forEach($scope.selectedDishList,function(val,idx){
													var diet = {};
													diet.otDishId = null;
													diet.otMealId = $scope.mealObj.mealId;
													diet.dishId  = val.id;
													diet.organizationId  = $scope.organizationId;
													diet.unitId  = $scope.unitId;
													diet.status = 'A';
													diet.createdDate = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
													diet.updatedDate = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
													diet.updatedBy = $scope.userId;
													diet.createdBy = $scope.userId;
													newArr.otDishList.push(diet);
												});
											}
											var selectedDishes = "";
											if( $scope.selectedDishes!=null &&  $scope.selectedDishes.length>0){
												angular.forEach( $scope.selectedDishes,function(value,index){
													if(selectedDishes==""){
														selectedDishes = value.label;
													}else{
														selectedDishes = selectedDishes +" , " +value.label;
													}
												});
											}
											newArr.selectedDishes = selectedDishes;
											$scope.dietDetails.push(newArr);
										}
									
								}else{
									
									var newArr = {};
									newArr.otMealId = null;
									newArr.mealId = $scope.mealObj.mealId;
									newArr.mealName = $scope.mealObj.mealName;
									newArr.unitId = $scope.unitId;
									newArr.organizationId  = $scope.organizationId;
									newArr.status = 'A';
									newArr.createdDate = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
									newArr.updatedDate = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
									newArr.updatedBy = $scope.userId;
									newArr.createdBy = $scope.userId;
									newArr.otDishList = [];
									if($scope.selectedDishList!=null && $scope.selectedDishList.length>0){
										angular.forEach($scope.selectedDishList,function(val,idx){
											var diet = {};
											diet.otDishId = null;
											diet.otMealId = $scope.mealObj.mealId;
											diet.dishId  = val.id;
											diet.organizationId  = $scope.organizationId;
											diet.unitId  = $scope.unitId;
											diet.status = 'A';
											diet.createdDate = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
											diet.updatedDate = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
											diet.updatedBy = $scope.userId;
											diet.createdBy = $scope.userId;
											newArr.otDishList.push(diet);
										});
									}
									var selectedDishes = "";
									if( $scope.selectedDishes!=null &&  $scope.selectedDishes.length>0){
										angular.forEach( $scope.selectedDishes,function(value,index){
											if(selectedDishes==""){
												selectedDishes = value.label;
											}else{
												selectedDishes = selectedDishes +" , " +value.label;
											}
										});
									}
									newArr.selectedDishes = selectedDishes;
									$scope.dietDetails.push(newArr);
								}
								
								$scope.mealObj={};
								selectedDishes = "";
								$scope.selectedDishList = [];
								$rootScope.stopLoader();
							}
							
							$scope.removeDiet = function(info,index){
								if(info.otMealId!=null){
									info.status='I';
								}else{
									$scope.dietDetails.splice(index,1);
								}
							}
							
							$scope.editDiet = function(info,index){
								$scope.mealObj.mealId = info.mealId;
								$scope.mealObj.mealName = info.mealName;
								var list = [];
								if(info.otDishList.length>0){
									angular.forEach(info.otDishList,function(v1,i1){
                                        var arr = {};
                                        arr.id = v1.dishId;
                                        list.push(arr);
									});
								}
								$scope.selectedDishList = list;
							}
							
/*************************************** DIET END ****************************************************************/
							$scope.diablebuttonMedication = false;
							// save Post Operative Care Details
							$scope.savePostOperativeCareDetails = function(){
								
								
								$rootScope.startLoader();
								$scope.diablebuttonMedication = true;
								
								if($scope.followUpNoteList == null || $scope.followUpNoteList.length == 0){
									growl.warning(
											"Please  Add Anesthetia  Note ",
											{
												title : 'Warning!'
											});
									$scope.diablebuttonMedication = false;
									$rootScope.stopLoader();
									return false;
								}
								
								if($scope.infusionObj.solutionItemId !=""){
									$scope.saveInfusionDetails();
								}
								
								if($scope.medicationDetails.length > 0){
									var medicationDetails = $scope.medicationDetails;
									
									var medicationDetailList = [];
									if( $scope.medicationDetails[0].priorityId != "" && $scope.medicationDetails[0].priorityId != null && $scope.medicationDetails[0].priorityId != undefined){
										$scope.pri=$scope.medicationDetails[0].priorityId;
									}
								
								var prescriptionObj = {
										"organizationId" : $scope.organizationId,
										"unitId" : $scope.unitId,
										"visitTypeId" : $scope.postCareCommonObj.visitTypeId,
										"employeeId" : $scope.employeeId,
										"doctorId" : $scope.postCareCommonObj.doctorId,
										"prescriptionDate" :  moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
										"authorisedBy" : $scope.postCareCommonObj.doctorId,
										"authorisedDate" : moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
										"addedBy" : $scope.userId,
										"addedDate" : moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
										"patientId" : $scope.postCareCommonObj.patientId,
										"priorityId" : $scope.pri,
										"isDischarge" : false,
										"deptId" : $scope.postCareCommonObj.specialityId
									};
									if($scope.postCareCommonObj.admissionId!=null){
										prescriptionObj.admissionId = $scope.postCareCommonObj.admissionId;
									}else{
										prescriptionObj.encounterId =$scope.postCareCommonObj.encounterId;
									}
									
									var flag = true;
									angular.forEach( medicationDetails, function(value, key) {
										var instructionId = value.instructionId;
										if (value.medicationStartDate != '' && value.medicationEndDate != '' && value.durationCount != ''
											&& value.durationUnit != '' && value.quantity != '') {
											var medicationDetailsObj = {};
											medicationDetailsObj["itemId"] = value.itemId;
											medicationDetailsObj["storeId"] = $scope.storeId;
											medicationDetailsObj["uomUnitId"] = value.uomTypeId;
											medicationDetailsObj["isMedicationStop"] = 'N';
											medicationDetailsObj["orderQty"] = value.quantity;
											medicationDetailsObj["strength"] = value.strength;
											medicationDetailsObj["prescriptionDate"] = moment(new Date()).format('YYYY-MM-DD HH:MM:SS');
											medicationDetailsObj["authorisedBy"] = $scope.postCareCommonObj.doctorId;
											medicationDetailsObj["authorisedDate"] = moment(new Date()).format('YYYY-MM-DD HH:MM:SS');
											medicationDetailsObj["dose"] = value.dose;
											medicationDetailsObj["itemName"] = value.medicationName;
											medicationDetailsObj["genericName"] = value.genericName;
											medicationDetailsObj["doseUnitId"] = value.doseUnitId;
											medicationDetailsObj["frequencyId"] = value.frequencyId;
											medicationDetailsObj["routeId"] = value.roaId;
											medicationDetailsObj["isNonFormulary"] = value.nonFormularyAdvice;
											medicationDetailsObj["startDate"] = value.medicationStartDate+ " 00:00:00";
											medicationDetailsObj["stopDate"] = value.medicationEndDate+ " 00:00:00";
											medicationDetailsObj["statflag"] = value.isStat!=null?value.isStat:false;
											medicationDetailsObj.isDischarge = value.isDischarge;
											if(value.prescribedDosageUnitList != undefined){
												if(value.prescribedDosageUnitList.length>0 && value.formulationCounterIndex!="" && value.formulationCounterIndex!=null
														&& value.formulationCounterIndex!=undefined){
															var list=$.grep(value.prescribedDosageUnitList, function (obj) {
															    return value.formulationCounterIndex === obj.counterIndex;
															});
															if(list.length>0){
																if(list[0].formulationFactorId==1){
																	medicationDetailsObj.prescribedDose=value.prescribeDose;
																	medicationDetailsObj.prescribedDoseUnitId=value.prescribedDoseUnitId;
																	medicationDetailsObj.factorId=list[0].formulationFactorId;
																	medicationDetailsObj.formulationDosageMappId=list[0].formulationDosageMappId;
																}else{
																	medicationDetailsObj.factorId=list[0].formulationFactorId;
																	medicationDetailsObj.formulationDosageMappId=list[0].formulationDosageMappId;
																}
															}
																
												}
											}
											if(value.instructionId != '' && value.instructionId != undefined  && value.instructionId != null ){
												medicationDetailsObj["instructionId"] = value.instructionId;
											}else{
												medicationDetailsObj["instructionDesc"] = value.instructionDesc;
											}
											medicationDetailList.push(medicationDetailsObj);
										} else {
											flag = false;
										}
														
									});
									if (medicationDetailList.length > 0) {
										prescriptionObj.prescriptionDetailsDtosList = medicationDetailList;
									}
								
								}
								
								// Activity instruction Save
								if ($scope.addedInstructions.length > 0 || ($scope.advicedInstruction!=null && $scope.advicedInstruction!="")) { 
								
								if($scope.advicedInstruction != null && $scope.advicedInstruction !=""){
									var newInstruction={};
									newInstruction.instructionDesc=$scope.advicedInstruction;
									newInstruction.advicedInstruction=$scope.advicedInstruction;
									newInstruction.unitId= $scope.unitId;
									newInstruction.organizationId=$scope.organizationId;
									newInstruction.patientId= $scope.postCareCommonObj.patientId;
									newInstruction.createdDate=moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
									newInstruction.updatedDate= moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
									newInstruction.createdBy= $scope.userId;
									newInstruction.updatedBy= $scope.userId;
									newInstruction.status='A';
									newInstruction.activityOrIntrcs = 0 ;
									if($scope.postCareCommonObj.admissionId!=null){
										newInstruction.admissionId= $scope.postCareCommonObj.admissionId;
									}else{
										newInstruction.encounterId= $scope.postCareCommonObj.encounterId;
									}
									$scope.addedInstructions.push(newInstruction);
								}
								
							}
								
								//Special Instruction Save
								if ($scope.addedSplInstructions.length > 0 || ($scope.advicedSplInstruction!=null && $scope.advicedSplInstruction!="")) { 
									
									if($scope.advicedSplInstruction != null && $scope.advicedSplInstruction !=""){
										var newInstruction={};
										newInstruction.instructionDesc=$scope.advicedSplInstruction;
										newInstruction.advicedInstruction=$scope.advicedSplInstruction;
										newInstruction.unitId= $scope.unitId;
										newInstruction.organizationId=$scope.organizationId;
										newInstruction.patientId= $scope.postCareCommonObj.patientId;
										newInstruction.createdDate=moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
										newInstruction.updatedDate= moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
										newInstruction.createdBy= $scope.userId;
										newInstruction.updatedBy= $scope.userId;
										newInstruction.status='A';
										newInstruction.activityOrIntrcs = 1 ;
										if($scope.postCareCommonObj.admissionId!=null){
											newInstruction.admissionId= $scope.postCareCommonObj.admissionId;
										}else{
											newInstruction.encounterId= $scope.postCareCommonObj.encounterId;
										}
										$scope.addedSplInstructions.push(newInstruction);
									}
									
									$scope.addedInstructions = $scope.addedInstructions.concat($scope.addedSplInstructions);
								
								}
								var activityInstructionObj = {}; 
								if($scope.addedInstructions.length>0){
								 activityInstructionObj = {
										unitId: $scope.unitId,
										organizationId:$scope.organizationId,
										patientId: $scope.postCareCommonObj.patientId,
										createdDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
										updatedDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
										createdBy: $scope.userId,
										updatedBy: $scope.userId,
										status:'A',
									coversheetInstructionDtoslist : $scope.addedInstructions
								};
								if($scope.postCareCommonObj.admissionId!=null){
									activityInstructionObj.admissionId= $scope.postCareCommonObj.admissionId;
								}else{
									activityInstructionObj.encounterId= $scope.postCareCommonObj.encounterId;
								}
								}else{
									activityInstructionObj = null;
								}
								
								var otMealList = [];
								if($scope.dietDetails.length>0){
									otMealList = otMealList.concat($scope.dietDetails);
								}else{
									otMealList = null;
								}
								var data = {
									prescriptionDto :prescriptionObj,
									oTCompletionNoteDtoList : $scope.followUpNoteList,
									postOperativeCareId : $scope.postOperativeCareId !=null ? $scope.postOperativeCareId : null,
									proReqId : $scope.postCareCommonObj.proReqId,
									status : 'A',
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									createdBy : $scope.userId,
									createdDate : moment( new Date()).format('DD-MM-YYYY HH:mm:ss'),
									updatedBy : $scope.userId,
									updatedDate : moment( new Date()).format('DD-MM-YYYY HH:mm:ss'),
									coversheetInstructionDto :activityInstructionObj,
									otMealList : otMealList
								};
								
								console.log("data",data);
								$rootScope.stopLoader();
								var URI = BASE_URL + ROOT_URL + "/api/otims/PostOperativeCareDetails/savePostOperativeCare";

								GenericServiceParamHeader.serviceAction(METHOD_POST, URI,data).then(
										function(response) {
										if (response.data.status == "success") {	
										//medication clear data
											
											growl.success(response.data.message,
													{
													title : 'Success!'
													});
											
											$scope.updateFlagValidation = false;
											$scope.medicationDetails = [];
											$scope.searchItem="";
											$scope.searchItemFre="";
											$scope.medicationIndex = undefined;
											$scope.diablebuttonMedication = false;
											$scope.medicationObj = {
												itemId : "",
												searchItem:"",
												searchItemFre:"",
												formulationTypeId : '',
												favorite_status : false,
												medicationStartDate : new Date(),
												roaId : '',
												dose : '',
												doseUnitId : '',
												durationCount : "",
												durationUnit : 'Day',
												instructionId : '',
												medicationEndDate : '',
												frequencyId : '',
												quantity : '',
												uomTypeId : '',
												conversions : '',
												strength : '',
												priorityId : '2',
												priorityCode : 'NR',
												isGeneric:'N',
												isInstruction:'N',
												nonFormularyAdvice:'N',
												searchItemFre : "",
												isStat : false,
												isDischarge : false

											};
											$scope.initMedicationObj();
											$scope.selectedOrderSet=[];
											$scope.getAllActiveMedication();
											$scope.getDetailedMedication();
											$scope.medicationObj.itemStrength="";
											$scope.medicationObj.prescribedDosageUnitList=[];
											
											//Activity Instruction Clear data
											$scope.getInstructions();
											$scope.advicedInstruction='';
											$scope.addedInstructions = [];
											
											//Special Instruction Clear data
											$scope.getSplInstructions();
											$scope.advicedSplInstruction='';
											$scope.addedSplInstructions = [];
											
											$scope.getFollowUpNotes();
											$state.go('OTIMSdashboard');
											$rootScope.stopLoader();
											return false;
											
										}else{
												$rootScope.stopLoader();
										}
								});
								
							}
							
							$scope.unitDetails = "";
							$scope.getUnitById = function(){
								$rootScope.startLoader();
								$scope.unitObj = {};
					        	
								var URI = BASE_URL + ROOT_URL + "/unit/" + $scope.unitId ;
								 BillingGenericService
										.serviceAction(METHOD_GET, URI, "",
												NOTIFICATION_MSG_STATUS_FALSE).then(
												function(response) {
													if (response.data.status == "success") {
														if(response.data.object.unitContact!=null && response.data.object.unitContact!=""){
															$scope.unitDetails = response.data.object.unitContact ;
														}
														if(response.data.object.unitEmailId!=null && response.data.object.unitEmailId!=""){
															if($scope.unitDetails!=""){
															$scope.unitDetails = $scope.unitDetails + " / " + response.data.object.unitEmailId;
															}else{
																$scope.unitDetails =  response.data.object.unitEmailId;
															}
														}
													}
													$rootScope.stopLoader();
												});
							}
							$scope.getUnitById();
							
							
							$scope.setTemplateName=function(){
								if( $scope.medicationDetails != null &&  $scope.medicationDetails.length > 0){
									$("#coversheetTemplateModel").show();
								}else{
									growl.warning("Please add medicine.");
								}
								
							}
							$scope.saveCovAsTemplate=function(){
								count=0;
								if($scope.medicationDetails != null && $scope.medicationDetails.length > 0){
									angular.forEach($scope.medicationDetails, function(value, key){
										if( (value.itemName == undefined || value.itemName == null  ||  value.itemName == "")
												|| (value.medicationEndDate == undefined || value.medicationEndDate == null || value.medicationEndDate == "")
												|| (value.durationCount == undefined || value.durationCount == null || value.durationCount == "")
												|| ((value.quantity == undefined || value.quantity == null || value.quantity == "") || (value.orderQty == undefined || value.orderQty == null || value.orderQty == ""))
												|| (value.frequencyDesc == undefined || value.frequencyDesc == null || value.frequencyDesc == "")
												|| (value.frequencyId == undefined || value.frequencyId == null || value.frequencyId == "")
												)
										{
											count++;
											
											}
										});
								}


								var medicationDetailsList = [];
								
								angular.forEach( $scope.medicationDetails, function(val, key){
									medicationDetailsList.push(val.selectedUom);
									 delete val.selectedUom;
								});
								
								var data = {

									      "covTemplateId": ($scope.covTemplateId != undefined  && $scope.covTemplateId != null && $scope.covTemplateId != "") ? $scope.covTemplateId : "",

									      "createdBy": $scope.userId,
									      "visitTypeId": 2,
									      "createdDate": moment(new Date).format('DD-MM-YYYY HH:mm:ss'),

									      "doctorId": $scope.patientDetails.doctorId,

									      "encounterId": $scope.patientDetails.admissionId,

									      "organizationId": $scope.organizationId,

									      "patientId": $scope.patientDetails.patientId,

									      "status": "A",

									      "templateName": $scope.covTemplateName,

									      "unitId": $scope.unitId,

									      "updatedBy": $scope.userId,

									      "updatedDate": moment(new Date).format('DD-MM-YYYY HH:mm:ss'),

									      "typeId": 3,// for laborder

									      "listPrescriptionData": $scope.medicationDetails

									    }
								if(count == 0){
									$rootScope.startLoaderPopup();
									var URI = BASE_URL + ROOT_URL + "/api/opd/saveTemplateListDataForNewEMR";
									GenericService.serviceAction("POST", URI, data).then(function(response) 
									{	
										if (response.data.status == 'success')
										{
											
											growl.success(response.data.message,
													{
														title : 'Success!'
													});
											$scope.$broadcast('show-errors-reset');
											$scope.covTemplateName="";
											$scope.covTemplateId="";
											$scope.selectedCovTemplateId="";
											$scope.initMedicationObj();
											// $scope.totalSumOfRate=0;
											$scope.selectedOrderSet=[];
											$scope.medicationObj.itemStrength="";
											$scope.medicationObj.prescribedDosageUnitList=[];
											$("#coversheetTemplateModel").hide();
											$scope.getMedicationTemplate();
											$rootScope.stopLoaderPopup();
										} else if(response.data.status == 'error')
										{
											
											growl.error(response.data.message,
													{
														title : 'Error!'
													});
											$rootScope.stopLoaderPopup();
										}
										
										angular.forEach( $scope.medicationDetails, function(val, key){
											  val.selectedUom = medicationDetailsList[key];
										});
										
									});
								}else{
									
									angular.forEach( $scope.medicationDetails, function(val, key){
										  val.selectedUom = medicationDetailsList[key];
									});
									
									growl.warning("Please update the medication.");
									return false;
									$rootScope.stopLoaderPopup();
								}
							
							}
							$scope.getMedicationTemplate = function(){
								$scope.savedTemplateList=[];
								$scope.presTemplateList=[];
								var headerObj={};
								var data = {
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
										typeId: 3,
										doctorId:$scope.patientDetails.doctorId
										
								}
								var URI = BASE_URL + ROOT_URL + "/api/opd/getCoversheetAsTemaplateList";
								GenericService.serviceAction("POST", URI, data)
								.then(function(response) {
											console.log(response);
											if (response.data.status == "success")
												$scope.savedTemplateList = response.data.listObject;
										});

							}
							$scope.getMedicationTemplate();
							$scope.covTemplateName="";
							$scope.selectedCovTemplateId="";
							$scope.getPrescriptionBycovTemplateId=function(covTemplateId)
								{
								$rootScope.startLoader();
								var data = {
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
										covTemplateId:covTemplateId
										
								}
								
								angular.forEach($scope.savedTemplateList, function(value,key){
									if(covTemplateId == value.covTemplateId){
										$scope.covTemplateName=value.templateName;
										$scope.selectedCovTemplateId=value.covTemplateId;
									}
								})
								
								var URI = BASE_URL + ROOT_URL + "/api/opd/getTemplateDataByCovTempId";
								GenericService.serviceAction("POST", URI, data)
								.then(function(response) {
											console.log(response);
											if (response.data.status == "success")
											$scope.presTemplateList=response.data.listObject[0].listPrescriptionData;
											$scope.addedMedList();
					                       /*
											 * $scope.medicationObj.medicationStartDate =
											 * (moment($scope.medicationObj.medicationStartDate1,
											 * "YYYY-MM-DD").toDate()).getTime();
											 */
											$rootScope.stopLoader();
										});
								
								
								 
							}
							$scope.addedMedList=function(){
								$scope.withoutTemplateMedications = [];
								angular.forEach($scope.medicationDetails, function(val ,key){
									if(val.isCovtemplate == undefined || val.isCovtemplate == null){
										$scope.withoutTemplateMedications.push(val);
									}
									
								});	
								
								$scope.medicationDetails = [];
								$scope.medicationDetails = $scope.withoutTemplateMedications;
								
								
								$scope.medicationDetailsNew=[];
								if($scope.medicationDetailsNew != null && $scope.medicationDetailsNew.length > 0){
									angular.forEach($scope.medicationDetails, function(value, key){

								if( (value.itemName == undefined || value.itemName == null  ||  value.itemName == "")
										|| (value.medicationEndDate == undefined || value.medicationEndDate == null || value.medicationEndDate == "")
										|| (value.durationCount == undefined || value.durationCount == null || value.durationCount == "")
										|| ((value.quantity == undefined || value.quantity == null || value.quantity == "") || (value.orderQty == undefined || value.orderQty == null || value.orderQty == ""))
										|| (value.frequencyDesc == undefined || value.frequencyDesc == null || value.frequencyDesc == "")
										|| (value.frequencyId == undefined || value.frequencyId == null || value.frequencyId == "")
										)
									{
									growl.warning("Please update the medication.");
									return false;
									$rootScope.stopLoaderPopup();
									}
								});
								}
								  
								  angular.forEach($scope.presTemplateList, function(value, key){	
										if(value.itemName != null ){
											$scope.medicationObjNew = {};
											$scope.medicationObjNew.itemId = value.itemId;
											$scope.medicationObjNew.doctorFavoriteId=value.doctorFavoriteId;
											$scope.medicationObjNew.quantity = value.quantity;
											$scope.medicationObjNew.orderQty = value.quantity;
											$scope.medicationObjNew.durationCount = value.durationCount;
					// $scope.searchItem = value.itemName;
											$scope.medicationObjNew.frequencyId = value.frequencyId;
											$scope.medicationObjNew.frequencyDesc = value.frequencyDesc;
											$scope.model=value.frequencyDesc;
											$scope.label=value.frequencyDesc;
											$scope.medicationObjNew.visitTypeId = 2;
											$scope.medicationObjNew.instructionDesc = (value.instructionDesc != undefined && value.instructionDesc != null && value.instructionDesc != "") ? value.instructionDesc :  value.instructionText;						$scope.medicationObjNew.instructionId = value.instructionId;
											$scope.medicationObjNew.durationCount = value.durationCount;
											$scope.medicationObjNew.durationUnit = value.durationUnit;
					// $scope.medicationObjNew.medicationStartDate = new Date();
					// $scope.medicationObjNew.prescribeDose =value.dosageForm;
											$scope.medicationObjNew.prescribeDose =value.prescribeDose;
											$scope.medicationObjNew.prescriptionDoseUnit =value.prescribeDoseUnit;
											$scope.medicationObjNew.itemName = value.itemName;
											$scope.medicationObjNew.roaDesc = value.roaDesc;
											$scope.medicationObjNew.genericName = value.genericName;
					// $scope.medicationObjNew.searchGeneric = value.genericName;
											$scope.medicationObjNew.genericId = value.genericId;
											$scope.medicationObjNew.formulationTypeId = value.formulationTypeId;
											$scope.medicationObjNew.isGeneric ='N';
					                        $scope.medicationObjNew.nonFormularyAdvice = value.isNonFormulary!= undefined ? value.isNonFormulary :'N';
											$scope.medicationObjNew.isInstruction='N';
											$scope.medicationObjNew.priorityCode=value.priorityCode;
											$scope.medicationObjNew.priorityId =value.priorityId;

					// $scope.medicationObjNew.searchItem =value.itemName;;
					// $scope.medicationObjNew.searchItemFre = value.frequencyDesc;
											$scope.medicationObjNew.frequencyId = value.frequencyId;
											$scope.medicationObjNew.rate = value.rate;
											$scope.medicationObjNew.totalRate= parseInt($scope.medicationObjNew.rate * (($scope.medicationObjNew.quantity!=undefined && $scope.medicationObjNew.quantity!="") ? $scope.medicationObjNew.quantity :0));
											$scope.frequency_id=value.frequencyId;
											$scope.frequency=$scope.medicationObjNew.searchItemFre;
											 $scope.medicationObjNew.medicationType =1;
											$scope.medicationObjNew.noOfAdministrationTime=value.noOfAdministrationTime;
											$scope.medicationObjNew.formulationDosageMappId=value.formulationDosageMappId;
											$scope.medicationObjNew.prescribedDosageUnitList=[];

											$scope.noOfAdministrationTime=value.noOfAdministrationTime;
											$scope.item={
													frequency_id:$scope.frequency_id,
													frequency:$scope.medicationObjNew.searchItemFre,
													noOfAdministrationTime:$scope.noOfAdministrationTime
											}
											
											$scope.model=value.frequencyDesc;
											$scope.label=value.frequencyDesc;
											if ($scope.medicationObjNew.instructionId != null && $scope.medicationObjNew.instructionId!=""
												&& $scope.medicationObjNew.instructionId!=undefined) {
												var instructionDesc = $.grep($scope.medicationInstructions,
																function(disCat) {
																	return disCat.instructionId == $scope.medicationObjNew.instructionId;
																})[0].instructionDesc;
												$scope.medicationObjNew.instructionDesc = instructionDesc;
											} else if(value.instructionText != undefined && value.instructionText != null && value.instructionText != "") {
												$scope.medicationObjNew.instructionDesc = value.instructionText;
												$scope.medicationObjNew.instructionText = value.instructionText;
												$scope.medicationObjNew.instructionTextFlag=true;
											}else {
												$scope.medicationObjNew.instructionDesc = "No instruction";
											}
											$scope.medicationObjNew.isStat=value.isStat;
												if($scope.medicationObjNew.isStat==true){
													$scope.medicationObjNew.isStat="stat";
												}else{
													$scope.medicationObjNew.isStat==false;
												}
											
											 if ($scope.medicationObjNew.isStat =="stat") {
													
													$scope.medicationObjNew.isStat = true;
													} else {
														$scope.medicationObjNew.isStat = false;
													}

											$scope.flagActiMed = true;
											$scope.medicationObj.prescribedDosageUnitList=[];
											$scope.addFrequencyItemList($scope.item, $scope.model, $scope.label);
											if((value.formulationTypeId != undefined && value.formulationTypeId != null && value.formulationTypeId!= "")
													&& (value.genericId != undefined && value.genericId != null && value.genericId!= "")){

											var formulationTypeId = value.formulationTypeId;
											var URI = BASE_URL
													+ ROOT_URL
													+ "/api/opd/getRoaList"
													+ "?unitId="
													+ $scope.unitId
													+ "&orgId="
													+ $scope.organizationId
													+ "&formulationTypeId="
													+ formulationTypeId
													+ "&genericId="
													+ value.genericId
													+ "&patientId="
													+ $scope.patientDetails.patientId;

											GenericServiceParamHeader
													.serviceAction(METHOD_GET, URI)
													.then(
															function(response) {
																if (response.data.status == "success") {
																	if (response.data.listObject.length !== 0) {
																		var responseList = response.data.listObject;
																		$scope.roa_options = responseList;
																	}
																}
															});
											}
											 if(value.roaId != null){
												 $scope.medicationObjNew.roaId =(value.roaId)
													.toString();
							                 }
											 $scope.medicationObjNew.dose = value.dose;
												$scope.medicationObjNew.strengthDesc=value.strengthDesc;
							                $scope.medicationObjNew.isDischarge = value.isDischarge;
											$scope.medicationObjNew.doseUnitId = value.doseUnitId!=null ? value.doseUnitId.toString():0;
											$scope.medicationObjNew.itemStrength=(value.dose != null ?value.dose :"")+" "+(value.doseDesc != null ? value.doseDesc :"");
											$scope.medicationObjNew.unit = value.doseDesc;
											$scope.medicationObjNew.prescriptionDoseUnit =value.prescribeDoseUnit;
											var sDT =  moment((new Date()),"DD-MM-YYYY").toDate();
							                var eDT = moment(value.endDate,"DD-MM-YYYY").toDate();
							                var startDT =  moment(sDT).format('YYYY-MM-DD');
							                var endDT = moment(eDT).format('YYYY-MM-DD');
							                $scope.medicationObjNew.medicationStartDate =  (moment(startDT, 'YYYY-MM-DD').toDate()).getTime();
							                $scope.medicationObjNew.startDateNew = startDT;
							                $scope.medicationObjNew.endDateNew = endDT;
							                $scope.medicationObjNew.endDate = endDT;
							                $scope.medicationObjNew.medicationStartDate1 = startDT;
							                if($scope.medicationObjNew.durationUnit == "Day") {
												var durationCount = parseInt($scope.medicationObjNew.durationCount);
												}
											    else if($scope.medicationObjNew.durationUnit == "Week") {
													var durationCount = parseInt($scope.medicationObjNew.durationCount)
													$scope.medicationObjNew.durationCount = durationCount* 7;
												}
												else if($scope.medicationObjNew.durationUnit == "Month") {
													var durationCount = parseInt($scope.medicationObjNew.durationCount)
													$scope.medicationObjNew.durationCount = durationCount* 30;
												}
												else if($scope.medicationObjNew.durationUnit == "Year") {
													var durationCount = parseInt($scope.medicationObjNew.durationCount)
													var months = durationCount * 12
													$scope.medicationObjNew.durationCount = months* 30;
												}
										
										if ($scope.medicationObjNew.durationCount && $scope.medicationObjNew.durationUnit && $scope.medicationObjNew.medicationStartDate) {
											if($scope.medicationObjNew.durationUnit == "Day") {
												var endDate = moment($scope.medicationObjNew.medicationStartDate).add($scope.medicationObjNew.durationCount-1, 'days').format('YYYY-MM-DD');
											}
											else if($scope.medicationObjNew.durationUnit == "Week") {
												var days = $scope.medicationObjNew.durationCount * 7
												var endDate = moment($scope.medicationObjNew.medicationStartDate).add($scope.medicationObjNew.durationCount, 'days').format('YYYY-MM-DD');
											}
											else if($scope.medicationObjNew.durationUnit == "Month") {
												var days = $scope.medicationObjNew.durationCount * 30
												var endDate = moment($scope.medicationObjNew.medicationStartDate).add($scope.medicationObjNew.durationCount, 'days').format('YYYY-MM-DD');
											}
											else if($scope.medicationObjNew.durationUnit == "Year") {
												var days = $scope.medicationObjNew.durationCount * 365;
												var endDate = moment($scope.medicationObjNew.medicationStartDate).add($scope.medicationObjNew.durationCount, 'days').format('YYYY-MM-DD');
											}
											$scope.medicationObjNew.medicationEndDate = endDate;
											$scope.medicationObjNew.medicationEndDate1 = endDate;
										}
										// added by chetana
										 if($scope.medicationObjNew.isDischarge == true){
												$scope.medicationObjNew.medicationType = 2;
												$scope.medType=$scope.medicationObjNew.medicationType;// discharge
											}else{
												$scope.medicationObjNew.medicationType = 1;// Administered
												$scope.medType=$scope.medicationObjNew.medicationType;
											}
						                   if ($scope.medicationObjNew.isDischarge ==true) {
													
					// $scope.medicationObjNew.isDischarge = "discharge";
													$scope.medicationObjNew.isDischarge = true;
													 $scope.flagDisch=true;
													} else {
														$scope.medicationObjNew.isDischarge = false;
													}
											 if ($scope.medicationObjNew.isStat ==true) {
													
					// $scope.medicationObjNew.isStat = "stat";
													$scope.medicationObjNew.statFlag = true;
													$scope.flagStat=true;
													} else {
														$scope.medicationObjNew.isStat = false;
													}
								                $scope.medicationObjNew.medicationStartDate = moment($scope.medicationObjNew.medicationStartDate).format('YYYY-MM-DD');
										if($scope.medicationDetailsNew.length>0){
					// $scope.medicationObjNew.medicationStartDate =
					// moment($scope.medicationObjNew.medicationStartDate).format('YYYY-MM-DD');
							                angular.forEach($scope.medicationDetailsNew,function(value,index){
							                    
							                    if ( $scope.medicationObjNew.searchItem == value.itemName
							                            && $scope.medicationObjNew.medicationStartDate == value.medicationStartDate
					// && $scope.medicationObjNew.medicationEndDate == value.medicationEndDate
							                            && $scope.medicationObjNew.durationCount == value.durationCount
							                            && $scope.medicationObjNew.searchItemFre == value.frequencyDesc
							                            && $scope.medicationObjNew.doseUnitId == value.doseUnitId
							                            && $scope.medicationObjNew.roaId == value.roaId
							                            && $scope.medicationObjNew.quantity == value.quantity) {
							                        
							                        $scope.medicationExist =true;
							                    }
							                });
							            }
										
										   if($scope.detailedMedications.length>0){
					// $scope.medicationObjNew.medicationStartDate =
					// moment($scope.medicationObjNew.medicationStartDate).format('YYYY-MM-DD');
							                   angular.forEach($scope.detailedMedications,function(value,index){
							                       
							                       var sDT =  moment(value.startDate,"DD-MM-YYYY").toDate();
							                       var eDT = moment(value.endDate,"DD-MM-YYYY").toDate();
							                       var startDT =  moment(sDT).format('YYYY-MM-DD');
							                       var endDT = moment(eDT).format('YYYY-MM-DD');
							                       
							                       if ( $scope.medicationObjNew.searchItem == value.itemName
							                               && $scope.medicationObjNew.medicationStartDate ==  startDT
					// && $scope.medicationObjNew.medicationEndDate == endDT
							                               && $scope.medicationObjNew.searchItemFre == value.frequencyDesc
							                               && $scope.medicationObjNew.dose== value.doseDesc
							                               && $scope.medicationObjNew.doseUnitId == value.doseUnitId
							                               && $scope.medicationObjNew.quantity == value.orderQty
							                               && $scope.medicationObjNew.roaId == value.roaId
							                               && $scope.medicationObjNew.isStat == value.statflag) {
							                           
							                           $scope.medicationExist =true;
							                       }
							                   });
							                   
							               }
										   
										   if($scope.medicationExist){
							                   growl.warning(
							                           "Medication Already Exist",
							                           {
							                               title : 'Warning!'
							                           });
							                   $scope.medicationObjNew.medicationStartDate = new Date();
							                   
							           $rootScope.stopLoader();
							           return false;
							           }
										   
											$scope.medicationDetailsNew.push($scope.medicationObjNew);
												 
												 $scope.totalSumOfRate = 0;
													angular.forEach($scope.medicationDetailsNew , function(value1,key1){
														$scope.totalSumOfRate+=value1.totalRate;
												   	});
												 console.log("Listtttt",$scope.medicationDetailsNew);
												 
												 // get formuation dosage list
											$scope.medicationObjNew.priorityId = '2';
//											
											}
										$scope.medicationObjNew.strengthDesc = value.strengthDesc;
										$scope.medicationObjNew.dose = value.strength;
										$scope.medicationObjNew.roaId = value.roaId;
										$scope.medicationObjNew.quantity = value.quantity;
										$scope.medicationObjNew.strength = value.strength;
										$scope.medicationObjNew.isCovtemplate = true;
										$scope.medicationObjNew.rate = $scope.medicationObjNew.rate;
										 if($scope.medicationObjNew.rate  == undefined){
					                       $scope.medicationObjNew.rate = 0;
					                   }
										$scope.medicationObjNew.totalRate=0;
										 $scope.medicationObjNew.totalRate=$scope.medicationObjNew.rate * $scope.medicationObjNew.quantity;
										
										$scope.medicationDetails.push($scope.medicationObjNew);
										})
										$scope.medicationDetailsNewList=[];
								  var i=0;
								  $scope.medTemp = [];
								  angular.forEach($scope.medicationDetails , function(val, key){
									  if(i==0)
										  {
										  $scope.medTemp.push(val);
										  }
									  else{
										  var check =false;
										  angular.forEach($scope.medTemp , function(val1, key1){
											  if(val.itemId == val1.itemId && val.itemName == val1.itemName){
												  check =true;
											  }
										  });
										  if(check== false)
											  {
											  $scope.medTemp.push(val);
											  }
									  }
									  i++;
								  });
								  $scope.medicationDetails=[];
								  $scope.medicationDetails=$scope.medTemp;
								  $scope.totalSumOfRate = 0;
									angular.forEach($scope.medicationDetails , function(value1,key1){
										$scope.totalSumOfRate+=value1.totalRate;

								   	});
									
									angular.forEach($scope.medicationDetails,function(value){
					                    if(value.itemId != null){
					                        	   var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.ITEM_MASTER.DETAILS_API  +$scope.organizationId + '/' + value.itemId;
					                              var data = {};
					                               GenericService.serviceAction("GET", URI, data).then(function (response) {
					                                     if(response.data.unitOfMeasurementDetailsResponse!=null)
					                                     {
					                                     	response.data.unitOfMeasurementDetailsResponse.forEach(function(item){

					                                    	   value.uom =item.uomList;
					                                    	   value.selectedUom = $scope.uomUnits.find(function(units){
					                                             return value["uom"][2].uomUnitId == units.id;
					                                           });

					                             	      });
					                                     	
					                                
					                                     	}
					                               });
					                    }
					             });
							}
							
							$scope.setTemplateName=function(){
								if( $scope.medicationDetails != null &&  $scope.medicationDetails.length > 0){
									$("#coversheetTemplateModel").show();
								}else{
									growl.warning("Please add medicine.");
								}
								
							}
							$scope.saveCovAsTemplate=function(){
								count=0;
								if($scope.medicationDetails != null && $scope.medicationDetails.length > 0){
									angular.forEach($scope.medicationDetails, function(value, key){
										if( (value.itemName == undefined || value.itemName == null  ||  value.itemName == "")
												|| (value.medicationEndDate == undefined || value.medicationEndDate == null || value.medicationEndDate == "")
												|| (value.durationCount == undefined || value.durationCount == null || value.durationCount == "")
												|| ((value.quantity == undefined || value.quantity == null || value.quantity == "") || (value.orderQty == undefined || value.orderQty == null || value.orderQty == ""))
												|| (value.frequencyDesc == undefined || value.frequencyDesc == null || value.frequencyDesc == "")
												|| (value.frequencyId == undefined || value.frequencyId == null || value.frequencyId == "")
												)
										{
											count++;
											
											}
										});
								}


								var medicationDetailsList = [];
								
								angular.forEach( $scope.medicationDetails, function(val, key){
									medicationDetailsList.push(val.selectedUom);
									 delete val.selectedUom;
								});
								
								var data = {

									      "covTemplateId": ($scope.covTemplateId != undefined  && $scope.covTemplateId != null && $scope.covTemplateId != "") ? $scope.covTemplateId : "",

									      "createdBy": $scope.userId,
									      "visitTypeId": 2,
									      "createdDate": moment(new Date).format('DD-MM-YYYY HH:mm:ss'),

									      "doctorId": $scope.postCommonObj.doctorId,

									      "encounterId": $scope.postCommonObj.admissionId != null ? $scope.postCommonObj.admissionId : $scope.postCommonObj.encounterId,

									      "organizationId": $scope.organizationId,

									      "patientId": $scope.postCommonObj.patientId,

									      "status": "A",

									      "templateName": $scope.covTemplateName,

									      "unitId": $scope.unitId,

									      "updatedBy": $scope.userId,

									      "updatedDate": moment(new Date).format('DD-MM-YYYY HH:mm:ss'),

									      "typeId": 3,// for laborder

									      "listPrescriptionData": $scope.medicationDetails

									    }
								if(count == 0){
									$rootScope.startLoaderPopup();
									var URI = BASE_URL + ROOT_URL + "/api/opd/saveTemplateListDataForNewEMR";
									GenericService.serviceAction("POST", URI, data).then(function(response) 
									{	
										if (response.data.status == 'success')
										{
											
											growl.success(response.data.message,
													{
														title : 'Success!'
													});
											$scope.$broadcast('show-errors-reset');
											$scope.covTemplateName="";
											$scope.covTemplateId="";
											$scope.selectedCovTemplateId="";
											$scope.initMedicationObj();
											// $scope.totalSumOfRate=0;
											$scope.selectedOrderSet=[];
											$scope.medicationObj.itemStrength="";
											$scope.medicationObj.prescribedDosageUnitList=[];
											$("#coversheetTemplateModel").hide();
											$scope.getMedicationTemplate();
											$rootScope.stopLoaderPopup();
										} else if(response.data.status == 'error')
										{
											
											growl.error(response.data.message,
													{
														title : 'Error!'
													});
											$rootScope.stopLoaderPopup();
										}
										
										angular.forEach( $scope.medicationDetails, function(val, key){
											  val.selectedUom = medicationDetailsList[key];
										});
										
									});
								}else{
									
									angular.forEach( $scope.medicationDetails, function(val, key){
										  val.selectedUom = medicationDetailsList[key];
									});
									
									growl.warning("Please update the medication.");
									return false;
									$rootScope.stopLoaderPopup();
								}
								
								
							}
							
							
							$scope.closeCoveTempModal = function(){
								$("#coversheetTemplateModel").hide();
							}
							
							
							
							$scope.closeCoveTempModal = function(){
								$("#coversheetTemplateModel").hide();
							}
							
							
				            $scope.validateCIMMSAllergy = function(){
								$scope.allergyData =[];
								$scope.medicationData =[];
								$scope.diagnosisData =[];
								
								angular.forEach($scope.allergyCurrentList,function(value){
									if(value.cimsCode != null)
									$scope.allergyData.push("<"+ value.cimsType +" reference='"+"{"+ value.cimsCode +"}"+"'/>");
								});
								
								angular.forEach($scope.allergyOldList,function(value){
									if(value.cimsCode != null)
									$scope.allergyData.push("<"+ value.cimsType +" reference='"+"{"+ value.cimsCode +"}"+"'/>");
								});
								
									angular.forEach($scope.allergenList,function(value){
										if($scope.patientAllergyObject.allergyId == value.allergyId){
										if(value.cimsCode != null)
										$scope.allergyData.push("<"+ value.cimsType +" reference='"+"{"+ value.cimsCode +"}"+"'/>");
										}
									});
									
									angular.forEach($scope.medicationDetails ,function(value){
										if(value.cimsCode != null)
										$scope.medicationData.push("<"+ value.cimsType +" reference='"+"{"+ value.cimsCode +"}"+"'/>");
									});
									$scope.todaysdate =  moment(new Date()).format('DD/MM/YYYY');
									angular.forEach($scope.detailedMedications ,function(value){
// if($scope.todaysdate >= value.startDate && $scope.todaysdate <= value.endDate
// ){
										if(value.cimsCode != null){
										$scope.medicationData.push("<"+ value.cimsType +" reference='"+"{"+ value.cimsCode +"}"+"'/>");
										}
// }
									});
									
									angular.forEach($scope.dataListDiagnosis ,function(value){
										if(value.code != null)
										$scope.diagnosisData.push("<HealthIssueCode code='"+value.code +"'  codeType='ICD 10'/>");
									});
									
									angular.forEach($scope.patientDiagnosisList ,function(value){
										if(value.code != null)
										$scope.diagnosisData.push("<HealthIssueCode code='"+value.code +"'  codeType='ICD 10'/>");
									});
									
									console.log("allergyData",$scope.allergyData);
									var data={
											organizationId : $scope.organizationId,
											unitId : $scope.unitId,
											reqXml: "<Request><Interaction>"+
											"<Prescribing>"+$scope.medicationData+"</Prescribing>"+
											"<Allergies>"+$scope.allergyData+"</Allergies>"+
											"<HealthIssueCodes>"+$scope.diagnosisData+"</HealthIssueCodes>"+
											"<References/></Interaction></Request>"
										}
									console.log("data",data);
									var URI = BASE_URL + ROOT_URL + "/api/opd/saveCIMMS";
									
									GenericService.serviceAction("POST",URI,data).then(function(response){
										
										if (response.data.status == 'success')
										{
											$scope.cimsResponseObject = response.data.object;
											$scope.Result = response.data.object;
						    				   var printWindow = window.open('', '', 'height=500,width=700');  
				                              printWindow.document.write($scope.Result);  
				                              printWindow.document.close();  
										} else if(response.data.status == 'error')
										{
											
											$scope.Result = response.data.message;
						    				   var printWindow = window.open('', '', 'height=500,width=700');  
				                              printWindow.document.write($scope.Result);  
				                              printWindow.document.close();  
				                              
										}
									});
									$rootScope.stopLoader();
							}
							
							$scope.validateCIMMS = function(){
								
								$scope.allergyData =[];
								$scope.medicationData =[];
								$scope.diagnosisData =[];
								
									angular.forEach($scope.allergyCurrentList,function(value){
										if(value.cimsCode != null)
										$scope.allergyData.push("<"+ value.cimsType +" reference='"+"{"+ value.cimsCode +"}"+"'/>");
									});
									
									angular.forEach($scope.allergyOldList,function(value){
										if(value.cimsCode != null)
										$scope.allergyData.push("<"+ value.cimsType +" reference='"+"{"+ value.cimsCode +"}"+"'/>");
									});
									
									angular.forEach($scope.medicationDetails ,function(value){
										if(value.cimsCode != null)
										$scope.medicationData.push("<"+ value.cimsType +" reference='"+"{"+ value.cimsCode +"}"+"'/>");
									});
									$scope.todaysdate =  moment(new Date()).format('DD/MM/YYYY');
									angular.forEach($scope.detailedMedications ,function(value){
// if($scope.todaysdate >= value.startDate && $scope.todaysdate <= value.endDate
// ){
										if(value.cimsCode != null){
										$scope.medicationData.push("<"+ value.cimsType +" reference='"+"{"+ value.cimsCode +"}"+"'/>");
										}
// }
									});
									
									angular.forEach($scope.patientDiagnosisList ,function(value){
										if(value.code != null)
										$scope.diagnosisData.push("<HealthIssueCode code='"+value.code +"'  codeType='ICD 10'/>");
									});
									
									console.log("allergyData",$scope.allergyData);
									var data={	
											organizationId : $scope.organizationId,
											unitId : $scope.unitId,
											reqXml: "<Request><Interaction>"+
											"<Prescribing>"+$scope.medicationData+"</Prescribing>"+
										// "<Allergies>"+$scope.allergyData+"<SubstanceClass
										// reference='{8B9ECB52-CDC9-4460-AB40-6E3C7DF38B1E}'/><SubstanceClass
										// reference='{9E775009-231D-728D-E034-080020E1DD8C}'/></Allergies>"+
											"<Allergies>"+$scope.allergyData+"</Allergies>"+
											"<HealthIssueCodes>"+$scope.diagnosisData+"</HealthIssueCodes>"+
											"<References/></Interaction></Request>"
										}
									
								/*	
									 * var data = { reqXml: "<Request><Interaction><Prescribing><GGPI
									 * reference='{488F9F61-5D37-4989-925E-1742FFFDAA9E}'/><GGPI
									 * reference='{B3E6B75E-9519-6AE7-E034-080020E1DD8C}'/><GGPI
									 * reference='{BF33752F-6062-0589-E034-0003BA299378}'/><GGPI
									 * reference='{B3E696CF-71F8-6518-E034-080020E1DD8C}'/></Prescribing><Allergies><Molecule
									 * reference='{5143B2B1-DBED-45A2-8308-61636C3A2F0B}'/><Molecule
									 * reference='{7142A19F-EDE2-4A7C-A30B-06AB60B6B71F}'/><Molecule
									 * reference='{74DCDDAF-9349-430F-A2F2-B7F2BC5CD8AA}'/><SubstanceClass
									 * reference='{8B9ECB52-CDC9-4460-AB40-6E3C7DF38B1E}'/><SubstanceClass
									 * reference='{9E775009-231D-728D-E034-080020E1DD8C}'/></Allergies><HealthIssueCodes><HealthIssueCode
									 * code='J45' codeType='ICD10'/><HealthIssueCode
									 * code='N17' codeType='ICD10'/><HealthIssueCode
									 * code='I49.5' codeType='ICD10'/></HealthIssueCodes><References/></Interaction></Request>"
									 * //reqXml: "<Request><Interaction><Prescribing><GGPI
									 * reference='{488F9F61-5D37-4989-925E-1742FFFDAA9E}'/><GGPI
									 * reference='{B3E6B75E-9519-6AE7-E034-080020E1DD8C}'/><GGPI
									 * reference='{BF33752F-6062-0589-E034-0003BA299378}'/><GGPI
									 * reference='{B3E696CF-71F8-6518-E034-080020E1DD8C}'/></Prescribing><Allergies><Molecule
									 * reference='{5143B2B1-DBED-45A2-8308-61636C3A2F0B}'/><Molecule
									 * reference='{7142A19F-EDE2-4A7C-A30B-06AB60B6B71F}'/><Molecule
									 * reference='{74DCDDAF-9349-430F-A2F2-B7F2BC5CD8AA}'/><SubstanceClass
									 * reference='{8B9ECB52-CDC9-4460-AB40-6E3C7DF38B1E}'/><SubstanceClass
									 * reference='{9E775009-231D-728D-E034-080020E1DD8C}'/></Allergies><HealthIssueCodes><HealthIssueCode
									 * code='J45' codeType='ICD10'/><HealthIssueCode
									 * code='N17' codeType='ICD10'/><HealthIssueCode
									 * code='I49.5' codeType='ICD10'/></HealthIssueCodes><References/></Interaction></Request>" }
									 */
									console.log("data",data);
// return false;
									var URI = BASE_URL + ROOT_URL + "/api/opd/saveCIMMS";
									
									GenericService.serviceAction("POST",URI,data).then(function(response){
										
										if (response.data.status == 'success')
										{
											$scope.cimsResponseObject = response.data.object;
											$scope.Result = response.data.object;
						    				   var printWindow = window.open('', '', 'height=500,width=700');  
				                              printWindow.document.write($scope.Result);  
				                              printWindow.document.close();  
										} else if(response.data.status == 'error')
										{
											
											$scope.Result = response.data.message;
						    				   var printWindow = window.open('', '', 'height=500,width=700');  
				                              printWindow.document.write($scope.Result);  
				                              printWindow.document.close();  
				                              
										}
									});
									$rootScope.stopLoader();
							}
							
							$scope.checkInformationRelatedToDrug = function(){
								
								if($scope.cimsOption =='Y'){
								    $scope.medicationData =[];
								
								    $scope.medicationData.push("<"+ $scope.cimsType +" reference='"+"{"+ $scope.cimsCode +"}"+"'/>");
									var data={	
											organizationId : $scope.organizationId,
											unitId : $scope.unitId,
											reqXml: "<Request>"+
											"<Content>"+$scope.medicationData+"</Content>"+
											"</Request>"
										}
									console.log("data",data);
// return false;
									var URI = BASE_URL + ROOT_URL + "/api/opd/saveCIMMS";
									
									GenericService.serviceAction("POST",URI,data).then(function(response){
										
										if (response.data.status == 'success')
										{
											$scope.cimsResponseObject = response.data.object;
											$scope.Result = response.data.object;
						    				   var printWindow = window.open('', '', 'height=500,width=700');  
				                              printWindow.document.write($scope.Result);  
				                              printWindow.document.close();  
										} else if(response.data.status == 'error')
										{
											
											$scope.Result = response.data.message;
						    				   var printWindow = window.open('', '', 'height=500,width=700');  
				                              printWindow.document.write($scope.Result);  
				                              printWindow.document.close();  
				                              
										}
									});
									$rootScope.stopLoader();
								}else{
									
									if($scope.cimsCode != undefined && $scope.cimsCode != null && $scope.cimsCode != ''){
										var New_EMR = "https://pharman.his.clinivantage.co.in/pharmandoctor?drugId=" +parseInt($scope.cimsCode);
										window.open(New_EMR,'popUpWindow','height=700,width=1000');
									}
									
								}
							}
							
							$scope.checkInformationRelatedToDrugItem = function(info){
								if($scope.cimsOption =='Y'){
							    $scope.medicationData =[];
							
							    $scope.medicationData.push("<"+ info.cimsType +" reference='"+"{"+ info.cimsCode +"}"+"'/>");
								var data={
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
										reqXml: "<Request>"+
										"<Content>"+$scope.medicationData+"</Content>"+
										"</Request>"
									}
								console.log("data",data);
								var URI = BASE_URL + ROOT_URL + "/api/opd/saveCIMMS";
								
								GenericService.serviceAction("POST",URI,data).then(function(response){
									
									if (response.data.status == 'success')
									{
										$scope.cimsResponseObject = response.data.object;
										$scope.Result = response.data.object;
					    				   var printWindow = window.open('', '', 'height=500,width=700');  
			                              printWindow.document.write($scope.Result);  
			                              printWindow.document.close();  
									} else if(response.data.status == 'error')
									{
										
										$scope.Result = response.data.message;
					    				   var printWindow = window.open('', '', 'height=500,width=700');  
			                              printWindow.document.write($scope.Result);  
			                              printWindow.document.close();  
			                              
									}
								});
								$rootScope.stopLoader();
								}else{
									if(info.cimsCode != undefined && info.cimsCode != null && info.cimsCode != ''){
										var New_EMR = "https://pharman.his.clinivantage.co.in/pharmandoctor?drugId=" +parseInt(info.cimsCode);
										window.open(New_EMR,'popUpWindow','height=700,width=1000');
										}
									}
						}
								
								/** **** Infusion Start Date*** */
								$scope.dateOptionsinfusionStartDate = {
									formatYear : 'yyyy',
									showWeeks : false,
									minDate : new Date(),
								};

								$scope.infusionStartOpen = {
									opened : false
								};

								$scope.openinfusionStartDate = function() {
									$scope.infusionStartOpen.opened = true;
								};

								/** ****Infusion End Date*** */
								$scope.dateOptionsinfusionEndDate = {
									formatYear : 'yyyy',
									showWeeks : false,
									minDate : new Date(),
								};

								$scope.infusionEndOpen = {
									opened : false
								};

								$scope.openinfusionEndDate = function() {
									$scope.infusionEndOpen.opened = true;
								};

								$scope.setNormalPriorityForInfusion = function(tabValue) {
									$scope.infusionObj.priority = tabValue;
								}
							   	$scope.addEmptyRowInfusion= function() {
									console.log("infusionDetails",$scope.infusionAdditiveDetails);
									$scope.additiveInfusion = {
											additiveName:"",
											additiveItemId:"",
					 						additivegenericName:"",
					 						additivegenericId:"",
					 						additiveVolume: 0 ,
					 						additiveUnit:"",
					 						additiveroaId:"",
					 						removeInfusionHistory:true
										};
									if($scope.infusionAdditiveDetails.length > 0 ){
										if($scope.infusionAdditiveDetails[0].additiveName != "" &&
										   $scope.infusionAdditiveDetails[0].additiveItemId != "" &&	
										   $scope.infusionAdditiveDetails[0].additivegenericId != "" &&
										   $scope.infusionAdditiveDetails[0].additiveUnit != "" &&
										   $scope.infusionAdditiveDetails[0].additiveName != "" ){
									         $scope.infusionAdditiveDetails.push($scope.additiveInfusion);
										}else{
											growl.warning(
				        	 						'Please enter additive details!!',
				        	 						{
				        	 							title : 'Warning!'
				        	 						});
										}
									}
									console.log($scope.infusionAdditiveDetails);
								}
								/// infusion code
								$scope.searchMedicationName = function(solutionName) {
									$scope.nameList = [];
									var data = {
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
										name : solutionName
									}

									var URI = BASE_URL
											+ ROOT_URL
											+ "/api/opd/getInfusionSolutionItemList";
									return BillingGenericService
											.serviceAction(METHOD_POST, URI, data,
													NOTIFICATION_MSG_STATUS_FALSE)
											.then(
													function(response) {
														if (response.data.status == "success") {
															$scope.nameList = response.data.listObject;
															console
																	.log(
																			"$scope.nameList ",
																			$scope.nameList);
														}
														return $scope.nameList;
													});
								}
								
								
								$scope.initBloodComponent = function() {

									$scope.infusionStartDate = undefined;
									$scope.infusionEndDate = undefined;

								}

								$scope.additivelist = [];

								$scope.getUOMActiveList = function() {

									var URI = BASE_URL + ROOT_URL
											+ "/api/globals/strength/units/"+$scope.organizationId;
									var data = {};
									GenericService
											.serviceAction("GET", URI, data)
											.then(
													function(response) {

														$rootScope.stopLoader();
														$scope.getUOMActiveList = [];
														$scope.getUOMActiveList = response.data;
														console
																.log(
																		"getUOMActiveList",
																		$scope.getUOMActiveList);

													});
								}
								$scope.getUOMActiveList();

								$scope.addGenericName = function($item, $model,
										$label, idx, itemMaster) {
									console.log($item);

									$rootScope.startLoader();

									$scope.itemId = $item.solutionItemId;
									$scope.infusionObj.solutionItemId = $item.solutionItemId;
									$scope.infusionObj.solutiongenericId = $item.solutiongenericId;
									$scope.solutiongenericName = "";

									var formulationTypeId = $item.formulationTypeId;
									var URI = BASE_URL
											+ ROOT_URL
											+ "/api/opd/getRoaList"
											+ "?unitId="
											+ $scope.unitId
											+ "&orgId="
											+ $scope.organizationId
											+ "&formulationTypeId="
											+ formulationTypeId
											+ "&genericId="
											+ $item.solutiongenericId
											+ "&patientId="
											+ $scope.postCareCommonObj.patientId;

									GenericServiceParamHeader
											.serviceAction(METHOD_GET, URI)
											.then(
													function(response) {
														if (response.data.status == "success") {
															$rootScope.stopLoader();
															var responseList = response.data.listObject;
															$scope.medicaRoaList = responseList;
															if (response.data.object == 0) {
																if (response.data.listObject.length !== 0) {

																	$scope.roa_options = responseList;
																	console
																			.log(
																					"ROA",
																					$scope.roa_options)
																}
																$scope.solutiongenericName = $item.solutiongenericName;
																$scope.formulationTypeId = $item.formulationTypeId;
																// $scope.strength =
																// $item.strength;
															} else {
																$scope.checkgenericObj = {
																	solutiongenericName : $item.solutiongenericName,
																	formulationTypeId : $item.formulationTypeId,
																	strength : $item.strength,
																}
																angular
																		.element(
																				'#confirmAddItemPopup')
																		.modal(
																				'show');
															}
														}
													});

								}
								//** ****************** code for additive************ *//*

								$scope.searchMedicationadditiveName = function(
										additiveName) {

									$scope.nameList = [];
									var data = {

										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
										name : additiveName

									}

									var URI = BASE_URL
											+ ROOT_URL
											+ "/api/opd/getInfusionAdditiveItemList";
									return BillingGenericService
											.serviceAction(METHOD_POST, URI, data,
													NOTIFICATION_MSG_STATUS_FALSE)
											.then(
													function(response) {
														if (response.data.status == "success") {
															$scope.nameList = response.data.listObject;
															console
																	.log(
																			"$scope.nameList ",
																			$scope.nameList);
														}
														return $scope.nameList;
													});
								}

								$scope.addGenericadditiveName = function($item,
										$model, $label, idx) {
									$rootScope.startLoader();
									$scope.itemId = $item.additiveItemId;
									$scope.additiveInfusion.additivegenericName = "";

									$scope.additiveInfusion.additiveItemId = $item.additiveItemId;
									$scope.additiveInfusion.additivegenericId = $item.additivegenericId;

									var formulationTypeId = $item.formulationTypeId;
									var URI = BASE_URL
											+ ROOT_URL
											+ "/api/opd/getRoaList"
											+ "?unitId="
											+ $scope.unitId
											+ "&orgId="
											+ $scope.organizationId
											+ "&formulationTypeId="
											+ formulationTypeId
											+ "&genericId="
											+ $item.additivegenericId
											+ "&patientId="
											+ $scope.postCareCommonObj.patientId;

									GenericServiceParamHeader
											.serviceAction(METHOD_GET, URI)
											.then(
													function(response) {
														if (response.data.status == "success") {
															$rootScope.stopLoader();
															var responseList = response.data.listObject;
															$scope.medicaRoaList = responseList;
															if (response.data.object == 0) {
																if (response.data.listObject.length !== 0) {

																	$scope.roa_additive = responseList;
																	console
																			.log(
																					"ROA",
																					$scope.roa_options)
																}
																$scope.additiveInfusion.additivegenericName = $item.additivegenericName;
																$scope.formulationTypeId = $item.formulationTypeId;
															} else {
																$scope.checkgenericObj = {
																	additivegenericName : $item.additivegenericName,
																	formulationTypeId : $item.formulationTypeId,
																	strength : $item.strength,
																}
																angular
																		.element(
																				'#confirmAddItemPopup')
																		.modal(
																				'show');
															}
														}
													});

								}

								$scope.getListOfInfusion = function() {

									var URI3 = BASE_URL + ROOT_URL
											+ "/api/opd/getListOfInfusion";
									var data = {

										"organizationId" : $scope.organizationId,
										"unitId" : $scope.unitId,
										"patientId" : $scope.postCareCommonObj.patientId
									};
									GenericService.serviceAction("POST", URI3, data)
											.then( function(response) {
														$rootScope.stopLoader();
														$scope.getInfusionList = [];
														$scope.getInfusionList = response.data.listObject;
														angular.forEach($scope.getInfusionList,function(value,index)
																{
															var startDate1 =moment(value.treatDate,'DD/MM/YYYY').format('DD-MMM-YYYY');
																var endDate1 =moment(value.createdDate,'DD/MM/YYYY').format('DD-MMM-YYYY');
																var updatedDate1 =moment(value.createdDate,'DD/MM/YYYY').format('DD-MMM-YYYY');
																if(startDate1=='Invalid date')
																	{
																	startDate1='';
																	}
																else
																	{
																	startDate1=startDate1;
																	}
																if(endDate1=='Invalid date')
																{
																	endDate1='';
																}
															else
																{
																endDate1=endDate1;
																}
																if(updatedDate1=='Invalid date')
																{
																	updatedDate1='';
																}
															else
																{
																updatedDate1=updatedDate1;
																}
																value["startDate1"]=startDate1;
																value["endDate1"]=endDate1;
																value["updatedDate1"]=updatedDate1;
																});
														console.log("infusion list",$scope.getInfusionList);

													});
								}
								$scope.getListOfInfusion();

								$scope.getadditivelist = function(
										infusionSolutionId) {

									var data = {

										"organizationId" : $scope.organizationId,
										"unitId" : $scope.unitId,
										"infusionSolutionId" : infusionSolutionId
									};
									var URI3 = BASE_URL + ROOT_URL
											+ "/api/opd/getAdditiveListById";

									GenericService
											.serviceAction("POST", URI3, data)
											.then(
													function(response) {

														$rootScope.stopLoader();
														$scope.getsaveadditivelist = [];
														$scope.getsaveadditivelist = response.data.listObject;
													});

								}
								// $scope.getadditivelist();
								$scope.searchMedicationFreItemsInfusion = function(
										searchKeyword) {
									$scope.itemList = [];
									var unitId = $scope.unitId
									var orgId = $scope.organizationId

									var URI = BASE_URL + ROOT_URL
											+ "/api/opd/getFrequencyList"
											+ "?unitId=" + unitId + "&orgId="
											+ orgId + "&searchPar=" + searchKeyword
									return BillingGenericService.serviceAction(METHOD_GET, URI, "", NOTIFICATION_MSG_STATUS_FALSE)
											.then( function(response) {
														if (response.data.status == "success") {
															$scope.itemList = response.data.listObject;
															console.log( "$scope.itemList", $scope.itemList)
														}
														return $scope.itemList;
													});
								}

								$scope.infusionObj = {
									solutionName : "",
									solutionItemId : "",
									solutiongenericName : "",
									solutiongenericId : "",
									solutionVolume : 0,
									solutionUnit : "",
									solutionroaId : "",
									additiveName : "",
									additiveItemId : "",
									additivegenericName : "",
									additivegenericId : "",
									additiveVolume : "",
									additiveUnit : "",
									additiveroaId : "",
									priority : "0",
									infusionStartDate : "",
									infusionEndDate : "",
									infusionFrequencyId : "",
									totalVolume : 0,
									duration : 0,
									infusionRate : 0,
									comments : "",
									instructionId : "",
									infusionAdditiveDtosList : ""

								};
								
								$scope.eraseAdditive = function(index){
									if(index == 0){
									$scope.initadditiveInfusion();
									$scope.calculateTotalVolume();
									}else{
										$scope.additiveInfusion = {
												additiveName : "",
												additiveItemId : "",
												additivegenericName : "",
												additivegenericId : "",
												additiveVolume : 0,
												additiveUnit : "",
												additiveroaId : "",
											};
									$scope.infusionAdditiveDetails[index] = [ $scope.additiveInfusion ];
									$scope.calculateTotalVolume();
									}
								}
								$scope.eraserInfusion = function(){
									$scope.initadditiveInfusion();
									$scope.infusionObj = {
											solutionName : "",
											solutionItemId : "",
											solutiongenericName : "",
											solutiongenericId : "",
											solutionVolume : 0,
											solutionUnit : "",
											solutionroaId : "",
											additiveName : "",
											additiveItemId : "",
											additivegenericName : "",
											additivegenericId : "",
											additiveVolume : "",
											additiveUnit : "",
											additiveroaId : "",
											priority : "0",
											infusionStartDate : new Date(),
											infusionEndDate : "",
											infusionFrequencyId : "",
											totalVolume : 0,
											duration : 0,
											infusionRate : 0,
											comments : "",
											instructionId : "",
											infusionAdditiveDtosList : ""

										};
									$scope.solutiongenericName="";
									$scope.infusionFrequencyId = "";
								}
								$scope.infusionObj.infusionStartDate = new Date();

								$scope.addFrequencyHistoryItemListInfusion = function(
										$item, $model, $label, idx, infusionObj) {
									$scope.frequencyId = $item.frequency_id;
									$scope.freQua = $item.noOfAdministrationTime;
									$scope.frequency = $item.frequency;
								}
								$scope.infusionAdditiveDetails = [];

								$scope.initadditiveInfusion = function() {

									$scope.additiveInfusion = {
										additiveName : "",
										additiveItemId : "",
										additivegenericName : "",
										additivegenericId : "",
										additiveVolume : 0,
										additiveUnit : "",
										additiveroaId : "",
									};
									$scope.infusionAdditiveDetails = [ $scope.additiveInfusion ];
								}
								$scope.initadditiveInfusion();
								$scope.Infusion = function() {
									console.log("infusionDetails",
											$scope.infusionAdditiveDetails);

									$scope.additiveInfusion = {
										additiveName : "",
										additiveItemId : "",
										additivegenericName : "",
										additivegenericId : "",
										additiveVolume : 0,
										additiveUnit : "",
										additiveroaId : "",
										removeInfusionHistory:false
									};
									$scope.infusionAdditiveDetails
											.push($scope.additiveInfusion);
									console.log($scope.infusionAdditiveDetails);
								}

								
								$scope.removeEmptyRowHistory = function(index) {
									if($scope.infusionAdditiveDetails.length == 1){
										angular.forEach($scope.infusionAdditiveDetails,function(value,key){
											value.removeInfusionHistory = false
										});
									}
									if(index != 0){
									$scope.infusionAdditiveDetails.splice(index, 1);
									$scope.calculateTotalVolume();
									}
								}

								//** *********calculation for total volume********** *//*

								$scope.infusionAdditiveList = [];
								$scope.resultadditiveVolume = 0;
								$scope.calculateTotalVolume = function() {
									$scope.infusionObj.totalVolume = 0;
									$scope.resultadditiveVolume = 0;

									if($scope.infusionAdditiveDetails[0].additiveVolume != 0 && $scope.infusionAdditiveDetails[0].additiveVolume != undefined){
									 angular.forEach($scope.infusionAdditiveDetails, function(value, key) {
										 var additiveVolume=value.additiveVolume;
										 $scope.resultadditiveVolume = $scope.resultadditiveVolume
											+ parseInt(additiveVolume);
									$scope.infusionObj.totalVolume = $scope.infusionObj.solutionVolume
											+ $scope.resultadditiveVolume;
									 });
										
								
									}else {
										$scope.infusionObj.totalVolume = $scope.infusionObj.solutionVolume;
									}

								}

								//** *********calculation for Rate********** *//*
								$scope.calculateRate = function(infusionObj) {

									$scope.infusionObj.infusionRate = ($scope.infusionObj.totalVolume/ $scope.infusionObj.duration).toFixed(3);

								}

								$scope.montantAnnuel = function() {
									if (isNaN(inputMontantAnnuel)) {
										inputMontantAnnuel = "";
									} else {
										inputMontantAnnuel = inputMontantGlobalAffaire
												/ inputDureeAnnee;
									}
								};
		
								
								$scope.saveInfusionDetails = function() {
									$rootScope.startLoader();
								
									
									if($scope.infusionObj.solutionItemId ==""){
										
										growl.warning(" Please enter the infusion details. ", {
											title : 'Warning!'
										});
										$rootScope.stopLoader();
										return false;
									}else if($scope.infusionObj.solutionVolume == "" ){
									
										growl.warning("Please select solution volume", {
											title : 'Warning!'
										});
										$rootScope.stopLoader();
										return false;
									}else if($scope.infusionObj.solutionUnit == ""){
										
										growl.warning("Please select strength", {
											title : 'Warning!'
										});
										$rootScope.stopLoader();
										return false;
									}else if($scope.infusionObj.solutionroaId ==""){
										
										growl.warning("Please select route of administration", {
											title : 'Warning!'
										});
										$rootScope.stopLoader();
										return false;
									}
								else if($scope.frequencyId == undefined){
										
										growl.warning("Please select frequency", {
											title : 'Warning!'
										});
										$rootScope.stopLoader();
										return false;
									}else if($scope.infusionObj.infusionEndDate ==""){
											
											growl.warning("Please select end date ", {
												title : 'Warning!'
											});
											$rootScope.stopLoader();
										return false;
									}else if($scope.infusionObj.duration ==""){
										
										growl.warning("Please select duration", {
											title : 'Warning!'
										});
										$rootScope.stopLoader();
										return false;
									
									}
	// if(infusionAdditive[0].additiveItemId!=null){
									else if($scope.infusionObj.infusionEndDate ==""){
										
										growl.warning("Please select end date ", {
											title : 'Warning!'
										});
										$rootScope.stopLoader();
									return false;}
		
									else{
										$scope.saveBtnFlag = true;
										$scope.updateBtnFlag = false;
										var infusionAdditive = $scope.infusionAdditiveDetails;
										var infusionAdditiveList = [];

										// calculate infusion quantity;
										$scope.noOfAdmin=null;
										
										angular.forEach($scope.itemList, function(value, key){
											if($scope.frequencyId == value.frequency_id){
												$scope.noOfAdmin=value.noOfAdministrationTime;
											}
										});
										

										var first = $scope.infusionObj.infusionStartDate;
											var second =$scope.infusionObj.infusionEndDate; 
											var dateFirst = moment(first).format("DD-MM-YYYY");
											var dateSecond =moment(second).format("DD-MM-YYYY"); 
											$scope.test = $rootScope.calcDiff(first,second);
											var dateDiff=0;
	// if($scope.test != 1){
	// dateDiff=$scope.test + 1;
	// }else{
	// dateDiff=$scope.test;
	// }
											if(dateFirst != dateSecond){
												dateDiff=$scope.test + 1;
											}else{
												dateDiff=$scope.test;
											}
											
											$scope.infusionQty=$scope.noOfAdmin * dateDiff;	
										var data = {
											"itemId" : $scope.infusionObj.solutionItemId,
											"genericId" : $scope.infusionObj.solutiongenericId,
											"solutionVolume" : $scope.infusionObj.solutionVolume,
											"strengthUnitId" : $scope.infusionObj.solutionUnit,
											"roaId" : $scope.infusionObj.solutionroaId,
											"frequencyId" : $scope.frequencyId,
											"priority" : $scope.infusionObj.priority,
											"startDate" : moment($scope.infusionObj.infusionStartDate).format('YYYY-MM-DD HH:mm:ss'),
											"endDate" : moment($scope.infusionObj.infusionEndDate).format('YYYY-MM-DD HH:mm:ss'),
											"totalVolume" : $scope.infusionObj.totalVolume,
											"duration" : $scope.infusionObj.duration,
											"infusionRate" : $scope.infusionObj.infusionRate,
											"comments" : $scope.infusionObj.comments,
											"instructionId" : $scope.infusionObj.instructionId,
											"organizationId" : $scope.organizationId,
											"unitId" : $scope.unitId,
											"createdBy" : $scope.userId,
											"updatedBy" : $scope.userId,
											"status" : "A",
											"createdDate" : moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
											"updatedDate" : moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
											"patientId" : $scope.postCareCommonObj.patientId,
											"admissionId" : $scope.postCareCommonObj.admissionId,
											"doctorId" : $scope.postCareCommonObj.doctorId,
											"visitTypeId" : $scope.postCareCommonObj.visitTypeId,
											 "deptId" :$scope.postCareCommonObj.specialityId,
											 "infusionQuantity" : $scope.infusionQty,
										};
										console.log("resultdata", data);
										for (var i = 0; i < infusionAdditive.length; i++) {
											

											if (infusionAdditive[i].additiveItemId > 0) {
												 if (infusionAdditive[i].additiveItemId!=null && infusionAdditive[i].additiveVolume==""){
														growl.warning("Please select additive volume", {
															title : 'Warning!'
														});
														$rootScope.stopLoader();
														return false;
													}
													else if (infusionAdditive[i].additiveVolume!=null && infusionAdditive[i].additiveUnit==""){
														growl.warning("Please select additive strength",{
															title : 'Warning!'
														});
														$rootScope.stopLoader();
														return false;
													}else{
												var additiveInfusionDetailsObj = {};
												additiveInfusionDetailsObj["itemId"] = infusionAdditive[i].additiveItemId;
												additiveInfusionDetailsObj["genericId"] = infusionAdditive[i].additivegenericId;
												additiveInfusionDetailsObj["volume"] = infusionAdditive[i].additiveVolume;
												additiveInfusionDetailsObj["additiveName"] = infusionAdditive[i].additiveName;
												additiveInfusionDetailsObj["strengthUnitId"] = infusionAdditive[i].additiveUnit;
												additiveInfusionDetailsObj["roaId"] = infusionAdditive[i].additiveroaId;
												additiveInfusionDetailsObj["organizationId"] = $scope.organizationId;
												additiveInfusionDetailsObj["unitId"] = $scope.unitId;
												additiveInfusionDetailsObj["updatedBy"] = $scope.userId;
												additiveInfusionDetailsObj["status"] = "A";
												additiveInfusionDetailsObj["createdDate"] = moment(
														new Date()).format(
														'YYYY-MM-DD HH:mm:ss');
												additiveInfusionDetailsObj["updatedDate"] = moment(
														new Date()).format(
														'YYYY-MM-DD HH:mm:ss');
												infusionAdditiveList
														.push(additiveInfusionDetailsObj);
													}

											}
										}

										data.infusionAdditiveDtosList = infusionAdditiveList;
										console.log("result", data);
										
									var URI = BASE_URL + ROOT_URL
											+ "/api/pharmacy/ip/pharmacology/validation/saveInfusionPresWithouthPharmaValidation";

									GenericServiceParamHeader
											.serviceAction(METHOD_POST, URI, data,
													NOTIFICATION_MSG_STATUS_TRUE)
											.then(
													function(response) {
														if (response.data.status == "success") {
															$scope.infusionObj.solutionName = "";
															$scope.solutiongenericName = "";
															$scope.infusionObj.solutionVolume = 0;
															$scope.infusionObj.solutionUnit = "";
															$scope.infusionObj.solutionroaId = "";
															$scope.additiveInfusion.additiveName = "";
															$scope.additiveInfusion.additivegenericName = "";
															$scope.additiveInfusion.additiveVolume = "";
															$scope.additiveInfusion.additiveUnit = "";
															$scope.additiveInfusion.additiveroaId = "";
															$scope.infusionFrequencyId = "";
															$scope.infusionObj.infusionEndDate = "";
															$scope.infusionObj.totalVolume = 0;
															$scope.infusionObj.duration = 0;
															$scope.infusionObj.infusionRate = 0;
															$scope.infusionObj.comments = "";
															$scope.infusionObj.instructionId = "";
															$scope.getListOfInfusion();
															$scope.infusionAdditiveDetails = [];
															$scope.additiveInfusion = {
																	additiveName : "",
																	additiveItemId : "",
																	additivegenericName : "",
																	additivegenericId : "",
																	additiveVolume : 0,
																	additiveUnit : "",
																	additiveroaId : "",
																};
															
															$scope.infusionAdditiveDetails.push($scope.additiveInfusion);
															$scope.getDetailedMedication();
															$rootScope.stopLoader();
														}
														$rootScope.stopLoader();
													});
	// $rootScope.stopLoader();
									
									}
								}
								
								 $scope.stopInfusionData = function(index,flag,info) {
									      $scope.isStartFlag=flag,
									      $scope.patientId=$scope.postCareCommonObj.patientId,
									      $scope.infusionSolutionId1=info.infusionSolutionId,
									      $scope.prescriptionDetailList=[],
									      $scope.prescriptionDetailList=info.prescriptionDetailsDto
								 }
								 $scope.stopInfusion = function(justificationStopInfusion) {
									 $rootScope.startLoader();
									      var data = {
													"isStartFlag":$scope.isStartFlag,
													"patientId":$scope.postCareCommonObj.patientId,
													"infusionSolutionId":  $scope.infusionSolutionId1,
													"justificationStopInfusion":justificationStopInfusion,
											 }
									    if(justificationStopInfusion != "" && justificationStopInfusion != null && justificationStopInfusion != undefined){
										var URI = BASE_URL + ROOT_URL+ "/api/opd/updateInfusionStopFlag";
										
										GenericService.serviceAction("POST", URI, data).then(function(response) {
											if (response.data.status == "success") {
												growl.success(response.data.message,
														{
															title : 'Success!'
														});
												$scope.getListOfInfusion();
												$rootScope.stopLoader();
												if($scope.prescriptionDetailList.length >0){

													angular.forEach($scope.prescriptionDetailList,function(value,key){
														
														var date = new Date();
														$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
														var data={
															    updatedBy:$scope.userId,
																updatedDate:$scope.dt,
																isMedicationStop:'Y',
																justificationStop:justificationStopInfusion,
																prescriptionDetailsid:value.prescriptionDetailsid,
														};
														
														
													     $timeout( function(){
														if(justificationStopInfusion != null && justificationStopInfusion !="" && justificationStopInfusion != undefined){
														var URI = BASE_URL + ROOT_URL + "/api/opd/saveJustificationForMedicationStop";
														GenericService.serviceAction("POST",URI,data).then(function(response){
															if (response.data.status == 'success')
															{
																
																growl.success(response.data.message,
																		{
																			title : 'Success!'
																		});
																$scope.getAllActiveMedication();
																$scope.getDetailedMedication();
																$scope.getContinuedMedicationHistoryList();
																$scope.getDetailedMedicationHistoryList();
																$scope.getMedicationListForMedicalHistory();
															}
														});
														}
														
													},5000);
													});
												
												}
												
										  	}
									  });
								   }
									else{
										 growl.warning(" Please enter reason. ",
													{
														title : 'warning!'
													});
										 $rootScope.stopLoader();
									}
							        }
								 
									var commonObjInst = {
											isMedication : 'Y',
											organizationId : $scope.organizationId
										}

										var URI = BASE_URL + ROOT_URL
												+ GETMEDICATIONINSTRUCTIONS;

										BillingGenericService
												.serviceAction(METHOD_POST, URI,
														commonObjInst,
														NOTIFICATION_MSG_STATUS_FALSE)
												.then(
														function(response) {
															console.log(response);
															// $rootScope.startLoader();
															if (response.data.status == "success")
																$scope.medicationInstructions = response.data.listObject;
														});
										
										   $scope.activeMedications = [];
											$scope.getAllActiveMedication = function() {
												$scope.activeMedications = [];
												var patientId = $scope.postCareCommonObj.patientId;
												var dateObj = moment(new Date()).format(
														'YYYY-MM-DD');

												var URI = BASE_URL + ROOT_URL
														+ GETACTIVEMEDICATION + "?patientId="
														+ patientId + "&date=" + dateObj + "&unitId="+ $scope.unitId
												GenericServiceParamHeader
														.serviceAction(METHOD_GET, URI)
														.then(
																function(response) {
																	if (response.data.status == "success") {
																		$scope.activeMedications = response.data.listObject;
																	
																		angular.forEach($scope.activeMedications,function(value,key){
												    						value.checkTrue='N';
												    						if(value.strengthDesc == null ){
																				value.strengthDesc = "";
																			}
																			if(value.doseDesc == null){
																				value.doseDesc = "";
																			}
												    					});
																		console.log("activeMedication",$scope.activeMedications);
																	}
																});
											}
											$scope.getContinuedMedicationHistoryList = function() {
												$scope.continuedMedicationHistoryList = [];
												var data = {
													organizationId : $scope.organizationId,
													unitId : $scope.unitId,
													patientId :$scope.postCareCommonObj.patientId,

												};
												var URI = BASE_URL + ROOT_URL
														+ "/api/opd/getContinuedMedicationList";
												GenericServiceParamHeader
														.serviceAction(METHOD_POST, URI, data,
																NOTIFICATION_MSG_STATUS_FALSE)
														.then(
																function(response) {
																	if (response.data.status == "success") {
																		$scope.continuedMedicationHistoryList = response.data.listObject;
																		angular.forEach($scope.continuedMedicationHistoryList,function(value,key){
												    						value.checkTrue='N';
												    					});
																	}
																});
											}
										      $scope.GetUOMUnitList = function () {
										          var URI = CONSTANTS.GLOBAL.UOM_UNITS + "/"+ $scope.organizationId;

										          $rootScope.startLoader();
										          GenericService.serviceAction("GET", URI, {}).then(
										            function (response) {
										              $scope.uomUnits = response.data;
										            },
										            function (err) {
										              $rootScope.stopLoader();
										            });
										      }
										      $scope.GetUOMUnitList();
											
											 $scope.saveBtnFlag = true;
												$scope.updateBtnFlag = false;
												$scope.updateScope = false;
												$scope.popUpFlag = true;
												
										  	  $scope.showUpdateBtn = function(info,idx){
								      				try{
								      					
								      					
								      					$rootScope.startLoader();
								      					
								      				// for once the medication saved as normal
													// then user can not be changed it as
													// discharge medication
								      					if(info.medicationType != undefined && info.medicationType != null 
								      							&& info.medicationType != "" && info.medicationType == 1){
								      						$scope.disOrNotCount = 0;
								      					}
								      					
								      					if(info.isNonFormulary == 'N'){
								      						var URI = BASE_URL + ROOT_URL + "/api/opd/getOPDMedicationListById/" +info.prescriptionId +"/"+ info.prescriptionDetailsid +"/"+$scope.postCareCommonObj.patientId+"/"+info.itemId +"/"+ $scope.unitId+"/"+ $scope.organizationId ;
								      					}else{
								      						var URI = BASE_URL + ROOT_URL + "/api/opd/getOPDMedicationListByIdForNonFormulary/" +info.prescriptionId +"/"+ info.prescriptionDetailsid +"/"+$scope.postCareCommonObj.patientId+"/"+ $scope.unitId+"/"+ $scope.organizationId ;
								      					}
								      					console.log("EDIT",URI);
								      					GenericService.serviceAction("GET",URI).then(function(response){
								      						console.log("Edit",response);
								      						
								      						if(response.data.status == "success"){
								      							$scope.updateFlag = true;
										      					$scope.updateFlagValidation = true;
																$scope.saveFlag = false;
																$scope.saveBtnFlag = false;
																$scope.updateScope = true;
																$scope.updateBtnFlag = true;
								      							$rootScope.stopLoader();
								      							$scope.list = [];
								      						    $scope.list=response.data.listObject
								      						    $scope.list = response.data.listObject;
								      							console.log("showlist",$scope.list );
								      							
								      							$scope.medicationObj.prescriptionId = info.prescriptionId;
								      							$scope.medicationObj.prescriptionNo = info.prescriptionNo;
								      							$scope.medicationObj.frequencyDesc=response.data.listObject[0].frequencyDesc;
								      							$scope.medicationObj.itemId=response.data.listObject[0].itemId;
								      							$scope.medicationObj.searchItem=response.data.listObject[0].searchItem;
								      							$scope.medicationObj.medicationName = response.data.listObject[0].searchItem;
								      							$scope.medicationObj.genericId=response.data.listObject[0].genericId;
								      							$scope.medicationObj.doseUnitId=response.data.listObject[0].doseUnitId;
								      							$scope.medicationObj.dose=((response.data.listObject[0].dose != undefined && response.data.listObject[0].dose != null && response.data.listObject[0].dose != "") ||  response.data.listObject[0].dose == 0) ? response.data.listObject[0].dose :response.data.listObject[0].prescribeDose;
								      							$scope.medicationObj.itemStrength=response.data.listObject[0].dose+" "+(response.data.listObject[0].strengthDesc!=null ?response.data.listObject[0].strengthDesc : "");
								      							$scope.medicationObj.roaId=response.data.listObject[0].roa_id;
								      							$scope.medicationObj.durationUnit='Day';
								      							$scope.medicationObj.frequencyId=response.data.listObject[0].frequencyId;
								      							$scope.medicationObj.searchItemFre=response.data.listObject[0].searchItemFre;
								      							$scope.medicationObj.genericName=response.data.listObject[0].genericName;
								      							$scope.medicationObj.searchGeneric=response.data.listObject[0].genericName;
								      							$scope.medicationObj.genericId=response.data.listObject[0].genericId;
								      							$scope.medicationObj.rate=response.data.listObject[0].rate;
								      							$scope.medicationObj.prescribeDose=response.data.listObject[0].prescribeDose;
								      							if(response.data.listObject[0].isDoseCalculationApplicable != undefined 
								                                        && response.data.listObject[0].isDoseCalculationApplicable != null 
								                                        && response.data.listObject[0].isDoseCalculationApplicable != false){
								      							$scope.medicationObj.isDoseCalculationApplicable=response.data.listObject[0].isDoseCalculationApplicable;
								      							
								      							$scope.medicationObj.doseCalculationApplicableData = response.data.listObject[0].doseCalculationApplicableData;
																$scope.medicationObj.doseCalculationApplicableData=JSON.parse($scope.medicationObj.doseCalculationApplicableData);
														        angular.forEach($scope.medicationObj.doseCalculationApplicableData, function(val, key){
														        	$scope.medicationObj.quantityForDoseCal=val.quantityForDoseCal;
														        	$scope.medicationObj.doseForDoseCal=val.doseForDoseCal;
														        	$scope.medicationObj.strengthForDoseCal=val.strengthForDoseCal;
														        	$scope.medicationObj.searchItemFre=val.searchItemFre;
														        	$scope.medicationObj.noOfAdministrationTimeForDoseCal=val.noOfAdministrationTimeForDoseCal;
														        	$scope.medicationObj.durationForDoseCal=val.durationForDoseCal;
														        	$scope.medicationObj.durationCountForDoseCal=val.durationCountForDoseCal;
														        	$scope.freQua =$scope.medicationObj.noOfAdministrationTimeForDoseCal;
																	$scope.medicationObj.frequencyId = val.frequencyForDoseCal;
																	$scope.frequency_id=val.frequencyForDoseCal;
																	$scope.frequency=$scope.medicationObj.searchItemFre;
																	$scope.medicationObj.durationCount=$scope.medicationObj.durationCountForDoseCal;
																	$scope.medicationObj.duration=$scope.medicationObj.durationForDoseCal;
																	$scope.medicationObj.itemStrength=$scope.medicationObj.strengthForDoseCal !=null ? $scope.medicationObj.strengthForDoseCal:(item.strength+" "+(item.unitDesc!=null ? item.unitDesc : " ") );
																	
														        });	
								      							}
								      							$scope.medicationObj.isInstruction='N';
								      							if(response.data.listObject[0].instructionDesc != null){
								      								$scope.medicationObj.instructionDesc=response.data.listObject[0].instructionDesc;
								      								$scope.medicationObj.isInstruction='N';
								      								$scope.medicationObj.instructionId=response.data.listObject[0].instructionId;
								      							}else{
								      								$scope.medicationObj.isInstructionText=response.data.listObject[0].instructionText;
								      								$scope.medicationObj.isInstruction='Y';
								      								$scope.medicationObj.instructionId='';
								      							}
								      							
								      							if($scope.medicationObj.instructionId == ''&& $scope.medicationObj.isInstructionText == null){
								      								$scope.medicationObj.isInstruction='N';
								      							}
								      							
								      							
								      							$scope.medicationObj.noOfAdministrationTime=response.data.listObject[0].noOfAdministrationTime;
								      							$scope.medicationObj.unit=response.data.listObject[0].strengthDesc;
								      							$scope.medicationObj.priorityId=response.data.listObject[0].priorityId;
								      							$scope.medicationObj.isStat=response.data.listObject[0].isStat;
								      							if($scope.medicationObj.isStat==true){
								      								$scope.medicationObj.isStat="stat";
								      							}else{
								      								$scope.medicationObj.isStat==false;
								      							}
								      							$scope.medicationObj.isDischarge=response.data.listObject[0].isDischargeMedication;
								      							if($scope.medicationObj.isDischarge==true){
								      								$scope.medicationObj.isDischarge="discharge"
								      							}else{
								      								$scope.medicationObj.isDischarge==false;
								      							}
								      							$scope.medicationObj.medicationStartDate=moment(response.data.listObject[0].medicationStartDate,"DD-MM-YYYY").toDate();
								      							$scope.medicationObj.medicationEndDate=moment(response.data.listObject[0].medicationEndDate,"DD-MM-YYYY").toDate();
								      							$scope.freQua =response.data.listObject[0].noOfAdministrationTime;
								      							$scope.medicationObj.formulationTypeId=response.data.listObject[0].formulationTypeId;
								      							$scope.medicationIndex=0;
								      							$scope.medicationObj.isGeneric='N';
								      							if(info.isNonFormulary == 'N'){
								      							    $scope.medicationObj.nonFormularyAdvice='N';
								      							}else{
								      								$scope.medicationObj.nonFormularyAdvice = 'Y';
								      								$scope.medicationObj.searchItem = null;
								      							}
								      							$scope.frequency_id=$scope.medicationObj.frequencyId;
																$scope.noOfAdministrationTime=$scope.medicationObj.noOfAdministrationTime;
																$scope.medicationObj.prescribedDosageUnitList=[];
																$scope.item={
																		frequency_id:$scope.frequency_id,
																		frequency:response.data.listObject[0].searchItemFre,
																		noOfAdministrationTime:$scope.noOfAdministrationTime
																}
																
																$scope.model=response.data.listObject[0].searchItemFre;
																$scope.label=response.data.listObject[0].searchItemFre;
																$scope.addFrequencyItemList($scope.item, $scope.model, $scope.label);
																if(info.isNonFormulary == 'N'){
								      							var formulationTypeId = response.data.listObject[0].formulationTypeId;
								    							var genericId  =response.data.listObject[0].genericId;
									  							var URI = BASE_URL + ROOT_URL + "/api/opd/getRoaList" + "?unitId=" + 
									  							$scope.unitId + "&orgId=" + $scope.organizationId + "&formulationTypeId=" + formulationTypeId+ "&genericId=" + genericId+ "&patientId=" +$scope.postCareCommonObj.patientId;
									  						
									  							$scope.prescriptionDtlObj=response.data.listObject[0];
									  							GenericServiceParamHeader.serviceAction(METHOD_GET, URI).then(function(response) {
									  							if(response.data.status == "success"){
									  								$rootScope.stopLoader();
									  								var responseList = response.data.listObject;
								  								$scope.medicaRoaList = responseList;
								  								if(response.data.object == 0)
								  									{
								  										if(response.data.listObject.length !== 0) {
								  											
								  										   $scope.roa_options = responseList;
								  										   console.log("$scope.roa_options",$scope.roa_options);
								  										}
								  										$scope.showMedicationOptions = true
								  										
								  										if($scope.prescriptionDtlObj.prescribeDose!=null && $scope.prescriptionDtlObj.factorId!=null && $scope.prescriptionDtlObj.factorId==1){
								  										   $scope.medicationObj.prescribeDose=$scope.prescriptionDtlObj.prescribeDose;
								  										}else if($scope.prescriptionDtlObj.prescribeDose !=undefined && $scope.prescriptionDtlObj.prescribeDose != null && $scope.prescriptionDtlObj.prescribeDose != ""){
								  											$scope.medicationObj.prescribeDose=$scope.prescriptionDtlObj.prescribeDose;
								  										}
								  										
								  										var dataobj = {
																				organizationId : $scope.organizationId,
																				unitId : $scope.unitId,
																				formulationTypeId:$scope.prescriptionDtlObj.formulationTypeId
																		};
																		var URI = BASE_URL + ROOT_URL+ "/api/global/getFormulationTypeToDosageMapperListByFormulationTypeId";
																		GenericService.serviceAction("POST", URI, dataobj).then(function(response) {
																					$rootScope.stopLoader();
																					$scope.medicationObj.prescribedDosageUnitList=[];
																					$scope.counterFormulation=1;
																					$scope.medicationObj.formulationCounterIndex="";
																					angular.forEach(response.data.listObject,function(value,index){
																						var unitObj={};
																						unitObj.formulationDosageFromUnit=value.fromConversionUnit;
																						unitObj.formulationDosageFromUnitId=value.fromConversionFactorUnit;
																						unitObj.formulationDosageToUnit=value.toConversionUnit;
																						unitObj.formulationDosageToUnitId=value.toConversionFactorUnit;
																						unitObj.counterIndex=$scope.counterFormulation++;
																						unitObj.formulationDosageFromConversionValue=value.fromConversionFactor;
																						unitObj.formulationDosageToConversionValue=value.toConversionFactor;
																						unitObj.formulationFactorId=value.factorId;
																						unitObj.formulationDosage=value.dosageForm;
																						unitObj.formulationDosageMappId=value.formulationDosageMappId;
																						$scope.medicationObj.prescribedDosageUnitList.push(unitObj);
																					});
																					
																					angular.forEach(response.data.listObject,function(value,index){
																						var unitObj={};
																						if(value.factorId==1){
																							unitObj.formulationDosageFromUnit=value.toConversionUnit;
																							unitObj.formulationDosageFromUnitId=value.toConversionFactorUnit;
																							unitObj.formulationDosageToUnit=value.fromConversionUnit;
																							unitObj.formulationDosageToUnitId=value.fromConversionFactorUnit;
																							unitObj.counterIndex=$scope.counterFormulation++;
																							unitObj.formulationDosageFromConversionValue=value.toConversionFactor;
																							unitObj.formulationDosageToConversionValue=value.fromConversionFactor;
																							unitObj.formulationFactorId=value.factorId;
																							unitObj.formulationDosage=value.dosageForm;
																							unitObj.formulationDosageMappId=value.formulationDosageMappId;
																							$scope.medicationObj.prescribedDosageUnitList.push(unitObj);
																						}
																					});
																					var list=[];
																					if($scope.medicationObj.prescribedDosageUnitList.length>0 && $scope.medicationObj.prescribedDosageUnitList[0].formulationFactorId==1){
																						 list=$.grep($scope.medicationObj.prescribedDosageUnitList, function (obj) {
																							 if(obj.formulationDosageFromUnit != undefined && obj.formulationDosageFromUnit != null && obj.formulationDosageFromUnit != ""){
																								 return  info.prescribeDoseUnit === obj.formulationDosageFromUnit;
																							 }else{
																								 return  info.dosageForm === obj.formulationDosage;
																							 }
																					    });
																						if(list.length>0){	
																							$scope.medicationObj.formulationCounterIndex=list[0].counterIndex;
																						}
																					}
																					
																					if($scope.medicationObj.prescribedDosageUnitList.length>0 && $scope.medicationObj.prescribedDosageUnitList[0].formulationFactorId==2){
																						 list=$.grep($scope.medicationObj.prescribedDosageUnitList, function (obj) {
																					        return $scope.prescriptionDtlObj.dosageForm === obj.formulationDosage;
																					    });
																						if(list.length>0){	
																							$scope.medicationObj.formulationCounterIndex=list[0].counterIndex;
																						}
																					}
																		});
								  									}
									  							}
									  							});
																}else{
																	$scope.medicationObj.prescribedDosageUnitList=[];
															    	var URI = BASE_URL + ROOT_URL + "/api/opd/getRoaListNonFormulary" + "?unitId=" + 
																	$scope.unitId + "&orgId=" + $scope.organizationId +"&patientId=" +  $scope.postCareCommonObj.patientId;
																
																	GenericServiceParamHeader.serviceAction(METHOD_GET, URI).then(function(response) {
																	if(response.data.status == "success"){
																			$scope.roa_options1 = response.data.listObject;
																			$scope.medicationObj.roaId=$scope.list[0].roa_id;
																	}
																});
																	
																}
								      						}else if(response.data.status == 'error'){
								      							$rootScope.stopLoader();
								      							growl.warning(response.data.message,
								      									{
								      										title : 'Warning!'
								      									});
								      						}
								      					});
								      					
								      				}catch(e){
								      					console.log("Exception-",e);
								      				}
								      			}
						            	
										  	$scope.updateFlag = false;
											$scope.saveFlag = true;
											
											// cancel Medication
											$scope.stopMedication=function(item){
				// $scope.prescriptionId=item.prescriptionId;
												$scope.prescriptionDetailsid=item.prescriptionDetailsid;
												
											}
											$scope.cancelMedication = function(justificationStop){
												$scope.justificationStop=justificationStop;
												$rootScope.startLoader();
												try{
				// $rootScope.startLoader();
													var date = new Date();
													$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
													var data={
														    updatedBy:$scope.userId,
															updatedDate:$scope.dt,
															isMedicationStop:'Y',
															justificationStop:$scope.justificationStop,
															/* prescriptionId:$scope.prescriptionId, */
															prescriptionDetailsid:$scope.prescriptionDetailsid,
															
													};
													
													if(justificationStop != null && justificationStop !="" && justificationStop != undefined){
													var URI = BASE_URL + ROOT_URL + "/api/opd/saveJustificationForMedicationStop";
													GenericService.serviceAction("POST",URI,data).then(function(response){
														if (response.data.status == 'success')
														{
															
															growl.success(response.data.message,
																	{
																		title : 'Success!'
																	});
															$rootScope.stopLoader();
															$scope.getAllActiveMedication();
															$scope.getDetailedMedication();
															$scope.getContinuedMedicationHistoryList();
															$scope.getDetailedMedicationHistoryList();
															$scope.getMedicationListForMedicalHistory();
				// $scope.$broadcast('show-errors-reset');
															$scope.justificationStop="";
														} else if(response.data.status == 'error')
														{
															$rootScope.stopLoader();
															growl.warning(response.data.message,
																	{
																		title : 'Warning!'
																	});
														}
													});
													}else{
														growl.warning(" Please enter reason to discontinue medication. ",
																{
																	title : 'warning!'
																});
															$rootScope.stopLoader();
													}
												}catch(e){
													console.log("Exception",e);
												}
											}
											
											
											// end cancel
											// delete medication
											$scope.deleteFunction = function(info){
												$scope.prescriptionId=info.prescriptionId;
												$scope.prescriptionDetailsid1=info.prescriptionDetailsid;
											}
											$scope.deleteMedicationPendingStatus = function(){
												$rootScope.startLoader();
												try{
													var date = new Date();
													$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
													var data={
														    updatedBy:$scope.userId,
															updatedDate:$scope.dt,
															prescriptionId:$scope.prescriptionId,
															organizationId:$scope.organizationId,
															justificationStop:$scope.justificationDeleteMedi,
															prescriptionDetailsid:$scope.prescriptionDetailsid1,
															
													};
													
													var URI = BASE_URL + ROOT_URL + "/api/opd/saveJustificationForMedicationDelete";
													GenericService.serviceAction("POST",URI,data).then(function(response){
														if (response.data.status == 'success')
														{
															$rootScope.stopLoader();
															growl.success("Deleted Successfully..!!",
																	{
																		title : 'Success!'
																	});
															$scope.getAllActiveMedication();
															$scope.getDetailedMedication();
															$scope.getMedicationListForMedicalHistory();
															$scope.justificationDeleteMedi="";
														} else if(response.data.status == 'error')
														{
															$rootScope.stopLoader();
															growl.warning(response.data.message,
																	{
																		title : 'Warning!'
																	});
															$scope.getAllActiveMedication();
															$scope.getDetailedMedication();
															$scope.getMedicationListForMedicalHistory();
															$scope.justificationDeleteMedi="";
														}
													});
				
												}catch(e){
													console.log("Exception",e);
												}
											}
											
											
											$scope.updateMedicationList = function() {
							          			$scope.saveBtnFlag = false;
						      					$scope.updateScope = true;
						      					$scope.updateBtnFlag = true;
						      					
												
												console.log("$scope.medicationDetails",
														$scope.medicationDetails);
												
				                                if($scope.detailedMedications.length>0){
				                                    $scope.medicationObj.medicationStartDate = moment($scope.medicationObj.medicationStartDate).format('YYYY-MM-DD');
				                                    angular.forEach($scope.detailedMedications,function(value,index){
				                                        
				                                        var sDT =  moment(value.startDate,"DD-MM-YYYY").toDate();
				                                        var eDT = moment(value.endDate,"DD-MM-YYYY").toDate();
				                                        var startDT =  moment(sDT).format('YYYY-MM-DD');
				                                        var endDT = moment(eDT).format('YYYY-MM-DD');
				                                        
				                                        if ( $scope.searchItem == value.itemName
				                                                && $scope.medicationObj.medicationStartDate ==  startDT
				// && $scope.medicationObj.medicationEndDate == endDT
				                                                && $scope.searchItemFre == value.frequencyDesc
				                                                && $scope.medicationObj.doseUnitId == value.doseUnitId
				                                                && $scope.medicationObj.quantity == value.orderQty
				                                                && $scope.medicationObj.roaId == value.roaId
				                                                && $scope.medicationObj.isStat == value.statflag) {
				                                            
				                                            $scope.medicationExist =true;
				                                        }
				                                    });
				                                    
				                                }
				                                
				                                
				                                if($scope.medicationExist){
				                                    growl.warning(
				                                            "Medication Already Exist",
				                                            {
				                                                title : 'Warning!'
				                                            });
				                            $rootScope.stopLoader();
				                            return false;
				                            }
				// alert($scope.list[0].prescriptionId);
												$rootScope.startLoader();
												var medicationDetails = $scope.medicationDetails;

												var medicationDetailList = [];
												if($scope.medicationDetails.length > 0){
												if( $scope.medicationDetails[0].priorityId != "" && $scope.medicationDetails[0].priorityId != null && $scope.medicationDetails[0].priorityId != undefined){
													$scope.pri=$scope.medicationDetails[0].priorityId;
												}
												}

												var data = {
														"prescriptionId":$scope.list[0].prescriptionId,
														"prescriptionNo":$scope.list[0].prescriptionNo,
														"prescriptionDetailsid":$scope.list[0].prescriptionDetailsid,
														"prescriptionFrequencyId":$scope.list[0].prescriptionFrequencyId,
													"organizationId" : $scope.organizationId,
													"unitId" : $scope.unitId,
													"visitTypeId" : $scope.postCareCommonObj.visitTypeId,
													"doctorId" : $scope.postCareCommonObj.doctorId,
													"prescriptionDate" : moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
													"authorisedBy" : $scope.postCareCommonObj.doctorId,
													"authorisedDate" :moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
													"addedBy" : $scope.userId,
													"addedDate" :moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
													"admissionId" : $scope.postCareCommonObj.admissionId,
													"patientId" : $scope.postCareCommonObj.patientId,
													"priorityId" : $scope.pri,
												  "deptId" : $scope.postCareCommonObj.specialityId,
													"isDischarge":false,
													"isPharmacologicalValidationSkipIp": true,
													 "medicationType":$scope.medType
												};

												// var medicationStartDate =
												// moment($scope.medicationObj.medicationStartDate).format('YYYY-MM-DD');

												var flag = true;
												if(medicationDetails.length > 0){
												angular.forEach(medicationDetails,function(value, key) {

																	var instructionId = value.instructionId;
																	if (value.medicationStartDate != ''
																			&& value.medicationEndDate != ''
																			&& value.durationCount != ''
																			&& value.durationUnit != ''
				// && value.instructionId != ''
																			&& value.quantity != '') {

																		var medicationDetailsObj = {};
																		medicationDetailsObj["itemId"] = value.itemId;
																		medicationDetailsObj["prescriptionId"] = $scope.list[0].prescriptionId;
																		medicationDetailsObj["prescriptionDetailsid"] = $scope.list[0].prescriptionDetailsid;
																		medicationDetailsObj["prescriptionFrequencyId"] = $scope.list[0].prescriptionFrequencyId;
																		medicationDetailsObj["storeId"] = 1;
																		medicationDetailsObj["uomUnitId"] = value.uomTypeId;
																		medicationDetailsObj["orderQty"] = value.quantity;
																		medicationDetailsObj["strength"] = value.dose;
																		medicationDetailsObj["prescriptionDate"] = moment(new Date()).format('YYYY-MM-DD HH:MM:SS');
																		medicationDetailsObj["authorisedBy"] = $scope.postCareCommonObj.doctorId;
																		medicationDetailsObj["authorisedDate"] = moment(new Date()).format('YYYY-MM-DD HH:MM:SS');
																		medicationDetailsObj["dose"] = value.dose;
																		medicationDetailsObj["doseUnitId"] = value.doseUnitId;
																		medicationDetailsObj["frequencyId"] = value.frequencyId;
																		medicationDetailsObj["itemName"] =value.medicationName != undefined ? value.medicationName :value.itemName;
																		medicationDetailsObj["genericName"] = value.genericName;
																		medicationDetailsObj["routeId"] = value.roaId;
																		medicationDetailsObj["duration"] = value.durationCount;
								                                        medicationDetailsObj["durationUnit"] = value.durationUnit;
																		medicationDetailsObj["startDate"] = value.medicationStartDate+ " 00:00:00";
																		medicationDetailsObj["stopDate"] = value.medicationEndDate+ " 00:00:00";
																		medicationDetailsObj["priorityId"] = value.priorityId;
																		   if(value.instructionId != '' && value.instructionId != undefined  && value.instructionId != null ){
																				medicationDetailsObj["instructionId"] = value.instructionId;
																				}else{
																				 medicationDetailsObj["instructionDesc"] = value.instructionDesc;
																				}
																		   
																		medicationDetailsObj["statflag"] = value.isStat;
																		medicationDetailsObj["isNonFormulary"] = value.nonFormularyAdvice;
																		// medicationDetailsObj["isDischarge"]
																		// = value.isDischarge;
																		if(value.isDischarge == true)
																			{
																			medicationDetailsObj["isDischarge"] = true;
																			}
																		
																		if(value.prescribedDosageUnitList.length>0 && value.formulationCounterIndex!="" && value.formulationCounterIndex!=null
																				&& value.formulationCounterIndex!=undefined){
																			 var list=$.grep(value.prescribedDosageUnitList, function (obj) {
																		        return value.formulationCounterIndex === obj.counterIndex;
																		    });
																			 if(list.length>0){
																				 if(list[0].formulationFactorId==1){
																					 medicationDetailsObj.prescribedDose=value.prescribeDose;
																					 medicationDetailsObj.prescribedDoseUnitId=value.prescribedDoseUnitId;
																					 medicationDetailsObj.factorId=list[0].formulationFactorId;
																					 medicationDetailsObj.formulationDosageMappId=list[0].formulationDosageMappId;
																				 }else{
																					 medicationDetailsObj.prescribedDose=value.prescribeDose;
																					 medicationDetailsObj.prescribedDoseUnitId=value.prescribedDoseUnitId;
																					 medicationDetailsObj.factorId=list[0].formulationFactorId;
																					 medicationDetailsObj.formulationDosageMappId=list[0].formulationDosageMappId;
																				 }
																			 }
																		}else{
																			
																			medicationDetailsObj.prescribedDose=value.prescribeDose != undefined ? value.prescribeDose :null;
																			medicationDetailsObj.factorId=2;
																		}
																		
																		medicationDetailList.push(medicationDetailsObj);
																	} else {
																		flag = false;
																	}

																});
												}else{
													flag = false;
												}
												if (flag) {
													data.prescriptionDetailsDtosList = medicationDetailList;
													console.log("medicationDetailList", data);
													// return false;
													var URI = BASE_URL + ROOT_URL
															+ "/api/opd/medicationUpdate";

													GenericServiceParamHeader
															.serviceAction(METHOD_POST, URI,
																	data,
																	NOTIFICATION_MSG_STATUS_TRUE)
															.then(
																	function(response) {
																		if (response.data.status == "success") {
																			$scope.updateFlag = false;
																			$scope.updateFlagValidation = false;
																			$scope.saveFlag = true;
																			
																			$scope.medicationDetails = [];

																			$scope.medicationObj = {
																				itemId : "",
																				searchItemFre:"",
																				searchItem:"",
																				formulationTypeId : '',
																				favorite_status : false,
																				medicationStartDate:new Date(),
																				roaId : '',
																				dose : '',
																				doseUnitId : '',
																				durationCount : "",
																				durationUnit : 'Day',
																				instructionId : '',
																				medicationEndDate : '',
																				frequencyId : '',
																				quantity : '',
																				uomTypeId : '',
																				conversions : '',
																				strength : '',
																				priorityId : '',
																				isStat : false,
																				isDischarge:false,
																				priorityCode: 'NR',
																				searchItemFre :"",
																				isGeneric:'N',
																				isInstruction:'N',
																				nonFormularyAdvice:'N'

																			};
																			$scope.initMedicationObj();
																			$scope.getAllActiveMedication();
																			 $scope.getDetailedMedication();
																			 $scope.totalSumOfRate=0;
																			 $scope.searchItem = "";
																			 $scope.searchItemFre = "";
																			 $scope.medicationObj.itemStrength="";
																			 $scope.medicationObj.prescribedDosageUnitList=[];
																			 $scope.saveBtnFlag = true;
																			 $scope.updateBtnFlag = false;
																			 $scope.covTemplateId="";
																				$scope.covTemplateName="";
																			 if($scope.patientDetails.dischargeStatusId==1 || $scope.patientDetails.dischargeStatusId==2
																						|| $scope.patientDetails.dischargeStatusId == 3){
																				 $scope.disOrNotCount=1;
																			 }
																			 
																			$rootScope.stopLoader();
																		} else {
																			$rootScope
																					.stopLoader();
																		}
																	});
												} else {
													growl.warning(
															     "Please add Medication and then save !!",
																	{
																		title : 'Warning!'
																	});
													$scope.medicationDetailList = [];
													$rootScope.stopLoader();
													return false;
												}

											}
											
											$scope.backScreen = function(){
												 $state.go('post',
															{
													 		 intraCommonObj : $scope.intraCommonObj,
													 		 intraDetailsObj : $scope.intraDetailsObj,
													 	 	 resultCommonObj :$scope.resultCommonObj ,
													 		 postCommonObj: $scope.postCommonObj,
													 		 postDetailsObj : $scope.postDetailsObj
															});
									    }

			$scope.status = "Idle";
			$scope.isPaused = false;
			$scope.canPause = false;
			$scope.canStop = false;
			$scope.timerInterval;
			$scope.audioPreviewUrl = null;
			$scope.audioBlob = null;
			$scope.totalSeconds = 0;
			$scope.recordingTime = "00:00";


			$scope.getAiTranscriptionCreds = function(){
				var data ={};
				data.unitId = $scope.unitId;
				data.organizationId = $scope.organizationId;

				var URI = BASE_URL + ROOT_URL + "/api/otims/PreOperativeDetails/getAiTranscriptionCreds";
				GenericService.serviceAction(METHOD_POST, URI, data)
					.then(function(response) {
						if (response.data.status == "success") {
							if(response.data.object !== undefined && response.data.object != null){
								$scope.aiTranscriptionApiKey  = response.data.object.aiTranscriptionApiKey;
								$scope.aiTranscriptionUrl = response.data.object.aiTranscriptionUrl;
								$scope.userPrompt = response.data.object.userPrompt;
							}
						}
						$scope.stopLoader();
					});
			}

			$scope.getAiTranscriptionCreds();

			$scope.formatTime = function (sec) {
				var min = Math.floor(sec / 60).toString().padStart(2, '0');
				var secStr = (sec % 60).toString().padStart(2, '0');
				return `${min}:${secStr}`;
			}

			$scope.startTimer =function() {
				$scope.timerInterval = $interval(() => {
					$scope.totalSeconds++;
					$scope.recordingTime = $scope.formatTime($scope.totalSeconds);
				}, 1000);
			}

			$scope.pauseTimer = function() {
				if ($scope.timerInterval) {
					$interval.cancel($scope.timerInterval);
					$scope.timerInterval = null;
				}
			}

			$scope.resetTimer = function() {
				$scope.pauseTimer();
				$scope.totalSeconds = 0;
				$scope.recordingTime = "00:00";
			}

			$scope.startRecording = function() {
				navigator.mediaDevices.getUserMedia({ audio: true })
					.then(function(stream) {
						$scope.mediaRecorder = new MediaRecorder(stream);
						var audioChunks = [];

						$scope.mediaRecorder.ondataavailable = function(e) {
							audioChunks.push(e.data);
						};

						$scope.mediaRecorder.onstop = function() {
							$scope.pauseTimer();
							$scope.isRecording = false;
							$scope.isPaused = false;
							var blob = new Blob(audioChunks, { type: $scope.mediaRecorder.mimeType });

							$scope.audioPreviewUrl = $scope.audioPreviewUrl = $sce.trustAsResourceUrl(URL.createObjectURL(blob));
							$scope.audioBlob = blob;
							$scope.$apply();
						};

						$scope.mediaRecorder.start();
						$scope.status = "Recording...";
						$scope.isRecording = true;
						$scope.isPaused = false;
						$scope.resetTimer();
						$scope.startTimer();
						$scope.$apply();
					})
					.catch(function(err) {
						console.error(err);
						$scope.status = "Microphone access denied.";
					});
			}

			$scope.pauseRecording = function() {
				if (!$scope.mediaRecorder) return;

				if ($scope.mediaRecorder.state === 'recording') {
					$scope.mediaRecorder.pause();
					$scope.pauseTimer();
					$scope.isPaused = true;
					$scope.status = "Paused";
				}
			}

			$scope.stopRecording = function() {
				if ($scope.mediaRecorder && ($scope.mediaRecorder.state === 'recording' || $scope.mediaRecorder.state === 'paused')) {
					$scope.mediaRecorder.stop();
					$scope.resetTimer();
					$scope.status = "Processing...";
					$scope.canPause = false;
					$scope.canStop = false;
				}
			}

			$scope.resumeRecording = function() {
				if ($scope.mediaRecorder && $scope.mediaRecorder.state === 'paused') {
					$scope.mediaRecorder.resume();
					$scope.startTimer();
					$scope.status = "Recording...";
					$scope.isPaused = false;
				}
			};

			$scope.deleteRecording = function() {
				$scope.stopRecording();
				if ($scope.audioPreviewUrl) {
					URL.revokeObjectURL($scope.audioPreviewUrl);
				}

				$scope.audioPreviewUrl = null;
				$scope.audioBlob = null;
				$scope.status = "Recording deleted.";
				$scope.recordingTime = "00:00";
				$scope.totalSeconds = 0;
				$scope.isRecording = false;
				$scope.isPaused = false;
			};

			$scope.openRecordingPopup = function() {
				var popup = angular.element('#recordingPopup');
				popup.modal('show');
			}

			$scope.blobToBase64 = function (blob, callback) {
				var reader = new FileReader();
				reader.onloadend = function () {
					// Remove data URI prefix
					var base64data = reader.result.split(',')[1];
					callback(base64data);
				};
				reader.readAsDataURL(blob);
			};

			$scope.sendToVoice = function () {
				if($scope.aiTranscriptionUrl !== undefined && $scope.aiTranscriptionUrl !== null && $scope.aiTranscriptionApiKey!== undefined && $scope.aiTranscriptionApiKey !== null) {

					if (!$scope.audioBlob) {
						console.error("Audio not recorded");
						return;
					}

					$scope.blobToBase64($scope.audioBlob, function (base64Audio) {

						var payload = {
							audio_base64: base64Audio,
							html_template: $scope.ckObj.template,
							templateId: $scope.ckObj.noteMasterId,
							audio_format: "webm"
						};
						$rootScope.startLoader();
						$scope.startLoader();

						$http({
							method: 'POST',
							url: $scope.aiTranscriptionUrl,
							headers: {
								'Content-Type': 'application/json',
								'X-API-Key': $scope.aiTranscriptionApiKey
							},
							data: payload
						}).then(function success(response) {

							console.log("Voice API response:", response.data.id);

							if (response.data.id && response.data.transcript) {
								$timeout(function () {
									angular.forEach($scope.followUpNoteListNew, function (info) {
										if(info.noteMasterId === $scope.ckObj.noteMasterId){
											info.template =
												response.data.html_template_filled +
												'<hr/>' +
												'<p>' + response.data.transcript.replace(/\n/g, '<br/>') + '</p>';
										}
									});
									$scope.deleteRecording();
									var popup = angular.element('#recordingPopup');
									popup.modal('hide');
								});
								$rootScope.stopLoader();
								$scope.stopLoader();
							}
							$rootScope.stopLoader();
							$scope.stopLoader();

						}, function error(err) {
							$rootScope.stopLoader();
							$scope.stopLoader();
							console.error("Voice API error:", err);
							$scope.status = "Upload failed.";
						});

					});
				}
				else{
					growl.warning("Please configure Ai Voice Credentials!!",{
						title : WARNING_MSG
					});
				}
			};

		}]);
		