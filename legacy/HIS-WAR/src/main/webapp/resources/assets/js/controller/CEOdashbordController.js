'use strict';
/**
 * @Author Sneha Dalave
 * @Author Chetna Dhokne 
 * @name myApp.controller:CEOdashbordController
 * @description #CEOdashbordController Controller of the myApp
 */
angular
    .module('myApp')
    .controller(
        'CEOdashbordController',
        ['$scope', '$rootScope', '$state', '$http', '$cookies', 'GenericService', '$sessionStorage', '$localStorage', '$location',
            function ($scope, $rootScope, $state, $http, $cookies, GenericService, $sessionStorage, permissions, $localStorage ,$location) {
        	
        	var cookieObject,cookieObjectAppConfig;
			[cookieObject,cookieObjectAppConfig] = $scope.checkCookies();
			$rootScope.isKioskForMenu = cookieObjectAppConfig.isKiosk;
			$rootScope.isNursingAssessment = cookieObjectAppConfig.isNursingAssessment;
			$rootScope.loginpage = true;
			$scope.updateScope = false;
			$scope.popUpFlag = true;
			
			var cookieObject = $cookies.getObject('cookieObject');
			if (cookieObject == undefined) {$state.go('login');
			return;
			}
			$scope.unitId = cookieObject.unitId;
			$scope.organizationId = cookieObject.organizationId;
			$scope.userId = cookieObject.userId;
			$scope.createdBy = cookieObject.userId;
			$scope.updatedBy = cookieObject.userId;
			console.log("$scope.organizationId",$scope.organizationId);
			console.log("$scope.unitId ",$scope.unitId );
			
			if(!$rootScope.storeReady()){ // store-accessor campaign: dashboard family reads the store differently — access-check only, its OWN popup (storePopupAcesss); the transactional getStoreObj()/storePopup would be wrong here
				$rootScope.storePopupAcesss();
			}
			
			var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
			var labConfig = JSON.parse(localStorage.getItem('labConfig'));
			 if(cookieObjectAppConfig == undefined){
			  $state.go('login');
			  return;
			 }
			 else
			  {
			   //console.log('cookieObjectAppConfig',cookieObjectAppConfig);
			   
			  }
			 
		$scope.radiologySpecialityId = cookieObjectAppConfig.radiologySpecialityId;
		$scope.hemodialysisSpecialityId = cookieObjectAppConfig.hemodialysisSpecialityId;
		if (labConfig != undefined) {
		$scope.labSpecialityId = labConfig.labSpecialityId;
		}
		$scope.nurseRole = cookieObjectAppConfig.nurseRole;	 
		$scope.doctorRole = cookieObjectAppConfig.doctorRole;	 
		$scope.roleName = cookieObjectAppConfig.roleName;
			
			/**================================COUNTER CODE START======================================*/
			$scope.isCounterApplicable = JSON.parse(sessionStorage.getItem('isCounterApplicable'));
			$scope.isUserCounterApplicable = JSON.parse(sessionStorage.getItem('isUserCounterApplicable'));
			if($scope.isCounterApplicable=='Y' && $scope.isUserCounterApplicable=='Y')
				{
				     var flag = $scope.getCounterDetails();
				     if(flag)
				    	 {
				    	 $rootScope.counterId = $rootScope.counterMstObj.counterId!=""?$rootScope.counterMstObj.counterId:"";
				    	 	$rootScope.counterDesc = $rootScope.counterMstObj.counterDesc!=""?$rootScope.counterMstObj.counterDesc:"";
				    	 }
				     else
				     {
				    	 $scope.counterList = JSON.parse(localStorage.getItem('counterList'));
				     }
				}else{
					$rootScope.counterId = 0;
				}
			$scope.confirmCounter = function()
			 {
				/*$scope.counterMstObj = $cookies.getObject('counterMstObj');
				$scope.counterId = $scope.counterMstObj.counterId!=""?$scope.counterMstObj.counterId:"";*/
				$scope.counterMstObj = JSON.parse(sessionStorage.getItem('counterMstObj'));
				if($scope.counterMstObj == undefined || $scope.counterMstObj == null || $scope.counterMstObj.counterId == undefined){
					growl.warning('Select Counter');
					return false;
				}else if($scope.counterMstObj.counterId > 0){
					$scope.counterId = $scope.counterMstObj.counterId!=""?$scope.counterMstObj.counterId:"";
					angular.element('#counterDetailsModal').modal('hide');
				}
			 }
			/**================================COUNTER CODE END======================================*/
			
			var nursingStationsWardsList = JSON.parse(sessionStorage.getItem('nursingStationsWardsList'));
			if(nursingStationsWardsList == undefined)
			{
				//start for nursing
				var URI = BASE_URL+ROOT_URL+"/api/global/getDetailedNursingStationList/"+$scope.userId+"/"+$scope.organizationId+"/"+$scope.unitId;
				  GenericService.serviceAction("GET",URI,{}).then(function(response){
					  if (response.data.status == "success") {
						  $scope.departmentNursingStationMapperDtoslist = [];
						  
							$scope.departmentNursingStationMapperDtoslist = response.data.listObject;
							//alert($scope.roleName);
							if($scope.nurseRole == 1){
							if($scope.departmentNursingStationMapperDtoslist.length > 0)
								{
									if($scope.departmentNursingStationMapperDtoslist.length > 1){
										angular.element('#nursingStationModal').modal('show');
										//$('#nursingStationModal').modal({backdrop: 'static', keyboard: false});
									}
									else
										{
										var URI = BASE_URL+ROOT_URL+"/api/global/getDeptListByNursingStationId/"+$scope.departmentNursingStationMapperDtoslist[0].nursingStationId+"/"+$scope.organizationId+"/"+$scope.unitId;
										  GenericService.serviceAction("GET",URI,{}).then(function(response){
											  if (response.data.status == "success") {
												  $scope.departmentNursingStationListC = {};
												 // console.log("fff",response.data.object);
												  $scope.departmentNursingStationListC.departmentNursingStationlist = response.data.listObject;
												  
												  sessionStorage.setItem('specialtyNursingStationCookies', JSON.stringify($scope.departmentNursingStationListC));
												  sessionStorage.setItem('specialtyNursingStationCookiesNur', JSON.stringify($scope.departmentNursingStationListC));
												  sessionStorage.setItem('nursingStationsWardsList', JSON.stringify(response.data.object));
												  sessionStorage.setItem('nursingStationId', JSON.stringify($scope.departmentNursingStationMapperDtoslist[0].nursingStationId));
												  //$cookies.putObject('nursingStationDesc',$scope.departmentNursingStationMapperDtoslist.nursingStationDesc);
												  sessionStorage.setItem('nursingStationDesc', JSON.stringify($scope.departmentNursingStationMapperDtoslist[0].nursingStationDesc));
												  $scope.wardListForGetBedList = response.data.object;
												  
											  }
										 });
										}
								}
					  		}
						}
				 });
				//end for nursing
			}
			else
			{
				$scope.wardListForGetBedList = nursingStationsWardsList;
			}
			
			
			
			$scope.departmentNursing = {};
			$scope.selectsetNursingStation = function()
			{
				if($scope.departmentNursing == undefined || $scope.departmentNursing.length == 0){
					growl.warning('Please select nursing station..!!');
					return false;
				}else{
				$scope.wardListForGetBedList=[];
				var data = JSON.parse($scope.departmentNursing);
				//console.log("$scope.departmentNursing",$scope.departmentNursing);
				//console.log("$scope.departmentNursing ID",data.nursingStationId);
				var URI = BASE_URL+ROOT_URL+"/api/global/getDeptListByNursingStationId/"+data.nursingStationId+"/"+$scope.organizationId+"/"+$scope.unitId;
				  GenericService.serviceAction("GET",URI,{}).then(function(response){
					  if (response.data.status == "success") {
						  $scope.departmentNursingStationListC = {};
						  //console.log('nursingStationsWardsList',response.data.object);
						    $scope.departmentNursingStationListC.departmentNursingStationlist = response.data.listObject;
							sessionStorage.setItem('specialtyNursingStationCookies', JSON.stringify($scope.departmentNursingStationListC));
							sessionStorage.setItem('specialtyNursingStationCookiesNur', JSON.stringify($scope.departmentNursingStationListC));
							sessionStorage.setItem('nursingStationsWardsList', JSON.stringify(response.data.object));
							sessionStorage.setItem('nursingStationDesc', JSON.stringify(data.nursingStationDesc));
							sessionStorage.setItem('nursingStationId', JSON.stringify(data.nursingStationId));
							$scope.wardListForGetBedList = response.data.object;
							angular.element('#nursingStationModal').modal('hide');
						}
				 });
			}
			}
			
            $scope.mainCeo=false;
            $scope.main=false;
			
			$scope.click=function(info){
				$scope.mainCeo=true;
				if(info==1){
					$scope.opd=true;
				}
				else if(info==2){
					$scope.ipd=true;
				}
				else if(info==3){
					$scope.dayCare=true;
				}
				else if(info==4){
					$scope.ehr=true;
				}
				else if(info==5){
					$scope.nursing=true;
				}
				else if(info==6){
					$scope.lis=true;
				}
				else if(info==7){
					$scope.radiology=true;
				}
				else if(info==8){
					$scope.bloodBank=true;
				}
				else if(info==9){
					$scope.dietKitchen=true;
				}
				else if(info==10){
					$scope.houseKeeping=true;
				}
				else if(info==11){
					$scope.linenLaundry=true;
				}
				else if(info==12){
					$scope.mortuary=true;
				}
				else if(info==13){
					$scope.storeInventory=true;
				}
				else if(info==14){
					$scope.pharmacy=true;
				}
				else if(info==15){
					$scope.assets=true;
				}
				else if(info==16){
					$scope.equipments=true;
				}
				else if(info==17){
					$scope.backOffice=true;
				}
				else if(info==18){
					$scope.emergencyRoom=true;
				}
				else if(info==19){
					$scope.accessControl=true;
				}
				else if(info==20){
					$scope.BI=true;
				}
				else if(info==21){
					$scope.mis=true;
				}
				else if(info==22){
					$scope.IVF=true;
				}
				else if(info==23){
					$scope.teleConsulting=true;
				}
				else if(info==24){
					$scope.QMS=true;
				}
				else if(info==25){
					$scope.physiotherapy=true;
				}
				else if(info==26){
					$scope.CSSD=true;
				}
				else if(info==27){
					$scope.OTIMS=true;
				}
				else if(info==28){
					$scope.surgerySafety=true;
				}
				else if(info==29){
					$scope.procurement=true;
				}
				else if(info==30){
					$scope.global=true;
				}
				else if(info==31){
					$scope.unit=true;
				}
				
			}
			$scope.backToThePage=function(){
				$scope.main=false;
					$scope.opd=false;
					$scope.ipd=false;
					$scope.dayCare=false;
					$scope.ehr=false;
					$scope.nursing=false;
					$scope.lis=false;
					$scope.radiology=false;
					$scope.bloodBank=false;
					$scope.dietKitchen=false;
					$scope.houseKeeping=false;
					$scope.linenLaundry=false;
					$scope.mortuary=false;
					$scope.storeInventory=false;
					$scope.pharmacy=false;
					$scope.assets=false;
					$scope.equipments=false;
					$scope.backOffice=false;
					$scope.emergencyRoom=false;
					$scope.accessControl=false;
					$scope.BI=false;
					$scope.mis=false;
					$scope.IVF=false;
					$scope.teleConsulting=false;
					$scope.QMS=false;
					$scope.physiotherapy=false;
					$scope.CSSD=false;
					$scope.OTIMS=false;
					$scope.surgerySafety=false;
					$scope.procurement=false;
					$scope.global=false;
					$scope.unit=false;
					
			}
			 
			 if($rootScope.isKioskForMenu == undefined || $rootScope.isKioskForMenu == null)
				{
				  $rootScope.isKioskForMenu = "N";
				}
			else
				{
				  $rootScope.isKioskForMenu = $rootScope.isKioskForMenu;
				}
			 
                $rootScope.$on("checkAccess", function($event,accessId){
                    $scope.checkAccess(accessId);
                 });

                var permissions = JSON.parse(localStorage.getItem('permissions'));

                $scope.checkAccess = function (id) {
                    if (permissions == undefined) {
                        return false;
                    }
                    var arr = permissions.modules.filter(function (item) {
                        return item.id == id && item.moduleType == 1;
                    });
                    return arr.length > 0 ? true : false;

                }
                
                

                $scope.getOtimsUrl = function () {
                    var url = OTIMS_URL + sessionStorage.token;
                    return url;
                }

                $scope.getBiUrl = function () {
                    var url = BI_URL + sessionStorage.token;
                    return url;
                }
                
                $scope.checkCssdStore = function(){
                	if(JSON.parse(sessionStorage.getItem('selectedCssdStore')) == undefined){
                		$rootScope.cssdStorePopup();
                		angular.element('#cssdStoreSelectionPopup').modal('show');
                	}
                }
                
                $rootScope.$on("checkAccess", function($event,accessId){
                    $scope.checkAccess(accessId);
                 });

                var permissions = JSON.parse(localStorage.getItem('permissions'));
                $scope.checkAccess = function (id) {
                    if (permissions == undefined) {
                        return false;
                    }
                    var arr = permissions.modules.filter(function (item) {
                        return item.id == id && item.moduleType == 1;
                    });
                    return arr.length > 0 ? true : false;
                }
                
                
                $scope.click= function(info)
            	{
            		$scope.data=info;
					$state.go('dashboardInfo',
						{
						params : $scope.data,
						});
            	}
                
                /**********************Grapg Code*******************/
    
            
                
                $scope.getListFinanaceCreditCountByVisitType = function() {

					var URI = BASE_URL + ROOT_URL+ "/api/global/getFinanceDetailsCount";
					var data = {
                            organizationId : $scope.organizationId,
                            unitId : $scope.unitId
                       }
                              
					GenericService.serviceAction("POST", URI, data).then(function(response) {
								if (response.data.status == "success") {
									$rootScope.stopLoader();
									$scope.getadmissionDischargeDeathCount = [];
									$scope.finalFinanceCount = [];
									$scope.dataForStackFinanace=[];
									$scope.labelsForStackFinanace=[];
									$scope.listFinanaceCreditCountByVisitType = response.data.listObject;
									console.log("listFinanaceCreditCountByVisitType",$scope.listFinanaceCreditCountByVisitType);
									angular.forEach($scope.listFinanaceCreditCountByVisitType, function(value,key) {
										if(value.financeCountArr != null){
										var newArr={};
										var labeltext={};
										newArr.visitTypeId = value.visitTypeId;
										newArr.visitType = value.visitType;
										labeltext.text = value.visitType;
//										newArr.dcArry = value.intcountArr;
										newArr.dcArry = value.financeCountArr;
										$scope.finalFinanceCount.push(newArr);
									    $scope.dataForStackFinanace.push(newArr);
									    $scope.labelsForStackFinanace.push(labeltext);
//									    $scope.labelsForStackFinanace=[{text:'OP'},{text:'IP'},{text:'ER'},{text:'DC'}];
										}
										
									});
									
									console.log("finalFinanceCount",$scope.finalFinanceCount);
									console.log("dataForStackFinanace",$scope.dataForStackFinanace);
									  var numberWithCommas = function(x) {
									      return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
									    };
									  
									    
//									    $scope.labelsForStack=[{text:'OP'}];
										  var dates = ["Discount","Credit Notes","Refund","Insurance","Company"];
										   $scope.colorArray = [];
									 	   /*var letters = "0123456789ABCDEF"; 
										     for(var j=0;j<16;j++){
											var color = '#'; 
											for (var i = 0; i < 6; i++) {
											color += letters[(Math.floor(Math.random() * 16))];
										    
										    } 
											$scope.colorArray.push(color);
										    }*/
										   var letters =['#22EBC8','#00D1DB','#00B3E2','#0093D6','#5372B9','#6F518F']; //"0F"; 
										     for(var j=0;j<16;j++){
											var color = ''; 
											for (var i = 0; i < 6; i++) {
											color = letters[i];//letters[(Math.floor(Math.random() * 16))];
											$scope.colorArray.push(color);
//										    alert(color);
										    } 
										    }

				                          
				                          $scope.dataForDatasetFinanace=[];
				                      	angular.forEach($scope.labelsForStackFinanace, function(value,index) {
				                      		var newArr={};
				                      		newArr.label=value.text;
				                      		newArr.backgroundColor =$scope.colorArray[index];
				                      		newArr.hoverBackgroundColor=$scope.colorArray[index];
				                      		newArr.hoverBorderWidth= 0
				                      		newArr.borderWidth= 0
				                      		 for(var i=0;i<$scope.dataForStackFinanace.length;i++){
				                      			 if(value.text==$scope.dataForStackFinanace[i].visitType){
				                      				newArr.data= $scope.dataForStackFinanace[i].dcArry;
				                      			 }
				                      			 else if(value.text==$scope.dataForStackFinanace[i].visitType){
				                      				newArr.data= $scope.dataForStackFinanace[i].dcArry;
				                      			 }
				                      			else if(value.text==$scope.dataForStackFinanace[i].visitType){
				                      				newArr.data= $scope.dataForStackFinanace[i].dcArry;
				                      			 }
				                      			else if(value.text==$scope.dataForStackFinanace[i].visitType){
				                      				newArr.data= $scope.dataForStackFinanace[i].dcArry;
				                      			 }
				                      			
				                      		 }
				                             $scope.dataForDatasetFinanace.push(newArr)
				                      	});
				                        console.log("$scope.dataForDataset",$scope.dataForDatasetFinanace);
										  var bar_ctx = document.getElementById('bar2');
										  var bar_chart = new Chart(bar_ctx, {
										      type: 'bar',
										      data: {
										          labels: dates,
										          datasets:$scope.dataForDatasetFinanace
										        
										      },
										      options: {
										       		animation: {
										          	duration: 10,
										          },
										          tooltips: {
										  					mode: 'label',
										            callbacks: {
										            label: function(tooltipItem, data) { 
										            	return data.datasets[tooltipItem.datasetIndex].label + ": " + numberWithCommas(tooltipItem.yLabel);
										            }
										            }
										           },
										          scales: {
										            xAxes: [{ 
										            	stacked: true, 
										              gridLines: { display: false },
										              }],
										            yAxes: [{ 
										            	stacked: true, 
										              ticks: {
										          			callback: function(value) { return numberWithCommas(value); },
										       				}, 
										              }],
										          }, // scales
										          legend: {display: true},
										          title: {
										                display: true,
										                text: 'Finance'
										            }
										      } // options
										     }
										  );
								}
							});
				}
				$scope.getListFinanaceCreditCountByVisitType();
				    
				
				$scope.getListOfOperationCountByVisitType = function() {

					var URI = BASE_URL + ROOT_URL+ "/api/global/getOperationCountList";
					var data = {
                            organizationId : $scope.organizationId,
                            unitId : $scope.unitId
                       }
                              
					GenericService.serviceAction("POST", URI, data).then(function(response) {
								if (response.data.status == "success") {
									$rootScope.stopLoader();
									$scope.getListOfOperationCount = [];
									$scope.finalListOfOperationCount = [];
									$scope.dataForStack=[];
									$scope.labelsForStack=[];
									$scope.getListOfOperationCount = response.data.listObject;
									console.log("getListOfOperationCount",$scope.getListOfOperationCount);
									angular.forEach($scope.getListOfOperationCount, function(value,key) {
										if(value.intcountArr != null){
										var newArr={};
										var labeltext={};
										newArr.visitTypeId = value.visitTypeId;
										newArr.visitType = value.visitType;
										labeltext.text = value.visitType;
										newArr.dcArry = value.intcountArr;
										$scope.finalListOfOperationCount.push(newArr);
									    $scope.dataForStack.push(newArr);
									    $scope.labelsForStack.push(labeltext);
										}
										
									});
									
									console.log("finalListOfOperationCount",$scope.finalListOfOperationCount);
									console.log("dataForStack1111",$scope.dataForStack);
									  var numberWithCommas = function(x) {
									      return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
									    };
									  
									    
//									    $scope.labelsForStack=[{text:'OP'}];
										  var dates = ["Lab", "Radiology"];
										   $scope.colorArray = [];
									 	   var letters =['#006BAF','#4A6BB8','#7568BC','#9A64B9','#BB5FB0','#D65AA2']; //"0F"; 
										     for(var j=0;j<16;j++){
											var color = ''; 
											for (var i = 0; i < 6; i++) {
											color = letters[i];//letters[(Math.floor(Math.random() * 16))];
											$scope.colorArray.push(color);
//										    alert(color);
										    } 
											
										    }

				                          
				                          $scope.dataForDataset=[];
				                      	angular.forEach($scope.labelsForStack, function(value,index) {
				                      		var newArr={};
				                      		newArr.label=value.text;
				                      		newArr.backgroundColor =$scope.colorArray[index];
				                      		newArr.hoverBackgroundColor=$scope.colorArray[index];
				                      		newArr.hoverBorderWidth= 0;
				                      		newArr.borderWidth= 0
				                      		 for(var i=0;i<$scope.dataForStack.length;i++){
				                      			 if(value.text==$scope.dataForStack[i].visitType){
				                      				newArr.data= $scope.dataForStack[i].dcArry;
				                      			 }
				                      			 else if(value.text==$scope.dataForStack[i].visitType){
				                      				newArr.data= $scope.dataForStack[i].dcArry;
				                      			 }
				                      			else if(value.text==$scope.dataForStack[i].visitType){
				                      				newArr.data= $scope.dataForStack[i].dcArry;
				                      			 }
				                      			else if(value.text==$scope.dataForStack[i].visitType){
				                      				newArr.data= $scope.dataForStack[i].dcArry;
				                      			 }
				                      			
				                      		 }
				                             $scope.dataForDataset.push(newArr)
				                      	});
				                        console.log("$scope.dataForDataset",$scope.dataForDataset);
										  var bar_ctx = document.getElementById('bar_chart');
										  var bar_chart = new Chart(bar_ctx, {
										      type: 'bar',
										      data: {
										          labels: dates,
										          datasets:$scope.dataForDataset
										        
										      },
										      options: {
										       		animation: {
										          	duration: 10,
										          },
										          tooltips: {
										  					mode: 'label',
										            callbacks: {
										            label: function(tooltipItem, data) { 
										            	return data.datasets[tooltipItem.datasetIndex].label + ": " + numberWithCommas(tooltipItem.yLabel);
										            }
										            }
										           },
										          scales: {
										            xAxes: [{ 
										            	stacked: true, 
										              gridLines: { display: false },
										              }],
										            yAxes: [{ 
										            	stacked: true, 
										              ticks: {
										          			callback: function(value) { return numberWithCommas(value); },
										       				}, 
										              }],
										          }, // scales
										          legend: {display: true},
										          title: {
										                display: true,
										                text: 'Diagnostics'
										            }
										      } // options
										     }
										  );
									    
									    
									
								}
							});
				}
				$scope.getListOfOperationCountByVisitType();
				
				$scope.getListOfAdministrationCountByVisitType = function() {

					var URI = BASE_URL + ROOT_URL+ "/api/global/getAdmissionDischargeDeathCount";
					var data = {
                            organizationId : $scope.organizationId,
                            unitId : $scope.unitId
                       }
                              
					GenericService.serviceAction("POST", URI, data).then(function(response) {
								if (response.data.status == "success") {
									$rootScope.stopLoader();
									$scope.getadmissionDischargeDeathCount = [];
									$scope.finalgetadmissionDischargeDeathCount = [];
									$scope.dataForStack1=[];
									$scope.labelsForStack1=[];
									$scope.getadmissionDischargeDeathCount = response.data.listObject;
									console.log("getadmissionDischargeDeathCount",$scope.getadmissionDischargeDeathCount);
									angular.forEach($scope.getadmissionDischargeDeathCount, function(value,key) {
										if(value.intcountArr != null){
										var newArr={};
										var labeltext={};
										newArr.visitTypeId = value.visitTypeId;
										newArr.visitType = value.visitType;
										labeltext.text = value.visitType;
										newArr.dcArry = value.intcountArr;
										$scope.finalgetadmissionDischargeDeathCount.push(newArr);
									    $scope.dataForStack1.push(newArr);
									    $scope.labelsForStack1.push(labeltext);
//									    $scope.labelsForStack1=[{text:'IP'},{text:'ER'},{text:'DC'}];
										}
										
									});
									
									console.log("finalgetadmissionDischargeDeathCount",$scope.finalgetadmissionDischargeDeathCount);
									console.log("dataForStack2",$scope.dataForStack1);
									  var numberWithCommas = function(x) {
									      return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
									    };
									  
									    
//									    $scope.labelsForStack=[{text:'OP'}];
										  var dates = [  "Admission", "Discharge","Death"];
										   $scope.colorArray = [];
										   var letters =['#22EBC8','#00D1DB','#00B3E2','#0093D6','#5372B9','#6F518F']; //"0F"; 
										     for(var j=0;j<16;j++){
											var color = ''; 
											for (var i = 0; i < 6; i++) {
											color = letters[i];//letters[(Math.floor(Math.random() * 16))];
											$scope.colorArray.push(color);
//										    alert(color);
										    } 
										    }
									 	   /*var letters = "0123456789ABCDEF"; 
										     for(var j=0;j<16;j++){
											var color = '#'; 
											for (var i = 0; i < 6; i++) {
											color += letters[(Math.floor(Math.random() * 16))];
										    
										    } 
											$scope.colorArray.push(color);
										    }*/
										   

				                          
				                          $scope.dataForDataset1=[];
				                      	angular.forEach($scope.labelsForStack1, function(value,index) {
				                      		var newArr={};
				                      		newArr.label=value.text;
				                      		newArr.backgroundColor =$scope.colorArray[index];
				                      		newArr.hoverBackgroundColor=$scope.colorArray[index];
				                      		newArr.hoverBorderWidth= 0
				                      		newArr.borderWidth= 0
				                      		 for(var i=0;i<$scope.dataForStack1.length;i++){
				                      			 if(value.text==$scope.dataForStack1[i].visitType){
				                      				newArr.data= $scope.dataForStack1[i].dcArry;
				                      			 }
				                      			 else if(value.text==$scope.dataForStack1[i].visitType){
				                      				newArr.data= $scope.dataForStack1[i].dcArry;
				                      			 }
				                      			else if(value.text==$scope.dataForStack1[i].visitType){
				                      				newArr.data= $scope.dataForStack1[i].dcArry;
				                      			 }
				                      			else if(value.text==$scope.dataForStack1[i].visitType){
				                      				newArr.data= $scope.dataForStack1[i].dcArry;
				                      			 }
				                      			
				                      		 }
				                             $scope.dataForDataset1.push(newArr)
				                      	});
				                        console.log("$scope.dataForDataset",$scope.dataForDataset);
										  var bar_ctx = document.getElementById('bar1');
										  var bar_chart = new Chart(bar_ctx, {
										      type: 'bar',
										      data: {
										          labels: dates,
										          datasets:$scope.dataForDataset1
										        
										      },
										      options: {
										       		animation: {
										          	duration: 10,
										          },
										          tooltips: {
										  					mode: 'label',
										            callbacks: {
										            label: function(tooltipItem, data) { 
										            	return data.datasets[tooltipItem.datasetIndex].label + ": " + numberWithCommas(tooltipItem.yLabel);
										            }
										            }
										           },
										          scales: {
										            xAxes: [{ 
										            	stacked: true, 
										              gridLines: { display: false },
										              }],
										            yAxes: [{ 
										            	stacked: true, 
										              ticks: {
										          			callback: function(value) { return numberWithCommas(value); },
										       				}, 
										              }],
										          }, // scales
										          legend: {display: true},
										          title: {
										                display: true,
										                text: 'Patient Count'
										            }
										      } // options
										     }
										  );
									    
								}
							});
				}
				$scope.getListOfAdministrationCountByVisitType();
				
				 $scope.getListOfPatientCountByVisitType = function() {
						var URI = BASE_URL + ROOT_URL+ "/api/global/getOPVisitsCount";
					  	var data = {
	                            organizationId : $scope.organizationId,
	                            unitId : $scope.unitId
	                       }
						GenericService.serviceAction("POST", URI, data).then(function(response) {
									if (response.data.status == "success") {
										$rootScope.stopLoader();
										$scope.getListPatientCountByVisitType = [];
										$scope.sampleListForCount=[];
										
										$scope.getListPatientCountByVisitType = response.data.listObject;
										console.log("getListPatientCountByVisitType",$scope.getListPatientCountByVisitType);
										$scope.demoList1=[];
										$scope.data5=[];
										$scope.countl=[];
										$scope.series5=[];
										$scope.testSeries=[];
										var ccount = 1;
										angular.forEach($scope.getListPatientCountByVisitType, function(value,index) {
										var newarray={};
										if(value.userDefinedVisitTypeId ==1){
											newarray.userDefinedVisitType="Primary";
										}
									   if(value.userDefinedVisitTypeId==2){
											newarray.userDefinedVisitType="Follow Up";
										}
										 if(value.userDefinedVisitType==3){
											newarray.userDefinedVisitTypeId="Secondary";
										}
										
										$scope.testSeries.push(newarray);
										var newArr = {};
									    angular.forEach(value.billingCollectionList, function(value1,key1) {
												
												newArr.totatPatCount=value1.totatPatCount;
												
												$scope.countl.push(newArr.totatPatCount);
											});
									    
									    newArr.demoList1 = [];
									    $scope.collectionList1=[];
									    var coyyt = $scope.countl[ccount - 1];
											newArr.demoList1.push(coyyt);
											$scope.collectionList1.push(newArr);
											var patientCount=$scope.collectionList1;
						       				for (var i = 0; i < patientCount.length; i++) 
						       				{
												$scope.data5.push(patientCount[i].totatPatCount);
										     }
						       				if(ccount == $scope.getListPatientCountByVisitType.length)
						       					{
						       				 $scope.labels5 = ["Primary","Follow Up","Secondary"];
						    	       		 $scope.series5 =  ["Primary","Follow Up","Secondary"];
						    				 $scope.data5= $scope.data5;
						    				 var  colorArray = [];
										 	   /*var letters = "0123456789ABCDEF"; 
											     for(var j=0;j<16;j++){
												var color = '#'; 
												for (var i = 0; i < 6; i++) {
												color += letters[(Math.floor(Math.random() * 16))];
											    
											      } 
												colorArray.push(color);
											    }*/
						    				 var letters =['#22EBC8','#00D1DB','#00B3E2','#0093D6','#5372B9','#6F518F']; //"0F"; 
										     for(var j=0;j<16;j++){
											var color = ''; 
											for (var i = 0; i < 6; i++) {
											color = letters[i];//letters[(Math.floor(Math.random() * 16))];
											colorArray.push(color);
//										    alert(color);
										    } 
										    }
											     $scope.colors5= colorArray;
						       				 $scope.options5 = {
						    				            legend: {
						    				                display: true,
						    				                labels: {
						    				                    fontColor: 'rgb(255, 99, 132)'
						    				                }
						    				            },
						    				            
						    				            animationEnabled: false,
						    				            title: {
						    				                display: true,
						    				                text: 'Visits' 
						    				            } 
						    				 
						    				     }
						       					
						       					}
						       				ccount++;
										});
										
					       				console.log("kkkkkkkk",$scope.data5);
					       				console.log("tttttttttttttttt",$scope.testSeries);
					       				console.log("gststs",$scope.series5);
									}
								});
				  }
				 $scope.getListOfPatientCountByVisitType();
				 
				 
				 
				 //Pie chart OP
				 
				 $scope.onClickFinanceOP = function () 
	                {
	    	        	
	    	        	if($scope.listForOP.length > 0){
	    	        		$scope.paymentModeList=[];
	    	        		$scope.amountList=[];
	    	        		$scope.data1=[];
	    	        		$scope.labels1=[];
	    	        		angular.forEach($scope.listForOP, function(value,key1) {
	    	        			var newArr={};
	    	        			$scope.paymentModeList.push(value.paymentMode);
	    	        			$scope.amountList.push(parseInt(value.amount));
	    	        		});
	    	        		console.log("$scope.paymentModeList",$scope.paymentModeList);
	    	        		console.log("$scope.amountList",$scope.amountList);
	    	        		 var paymentMode=$scope.paymentModeList;
	    	       				for (var i = 0; i < paymentMode.length; i++) {
	    							$scope.labels1.push(paymentMode[i]);
	    						}
	    	       			var amountMode=$scope.amountList;
	    	       				for (var i = 0; i < amountMode.length; i++) {
	    	       					$scope.data1.push(amountMode[i]);
	    						}
	    	       				
	    	       		 $scope.labels1 = $scope.labels1;
	    	       		 $scope.series1 =  $scope.labels1;
	    				 $scope.data1= $scope.data1;
	    				
	    				 
	    				 
	    				 console.log("$scope.labels1",JSON.stringify($scope.labels1));
	    				var  colorArray = [];
					 	   /*var letters = "0123456789ABCDEF"; 
						     for(var j=0;j<16;j++){
							var color = '#'; 
							for (var i = 0; i < 6; i++) {
							color += letters[(Math.floor(Math.random() * 16))];
						    
						      } 
							colorArray.push(color);
						    }*/
	    				var letters =['#22EBC8','#00D1DB','#00B3E2','#0093D6','#5372B9','#6F518F']; //"0F"; 
					     for(var j=0;j<16;j++){
						var color = ''; 
						for (var i = 0; i < 6; i++) {
						color = letters[i];//letters[(Math.floor(Math.random() * 16))];
						colorArray.push(color);
//					    alert(color);
					    } 
					    }
						     $scope.colors1= colorArray;
	       					
	       				    $scope.options1 = {
	    				            legend: {
	    				                display: true,
	    				                labels: {
	    				                    fontColor: 'rgb(255, 99, 132)'
	    				                }
	    				            },
	    				            
	    				            animationEnabled: false,
	    				            title: {
	    				                display: true,
	    				                text: 'Finance For OP' 
	    				            } 
	    				 
	    				     }
	    	        	}
	                }  
				 
				
				 
				 
				 //PIE IP
				 
				 $scope.onClickFinanceIP = function () {
			        	
		        	if($scope.listForIP.length > 0)
		        	{
			        		$scope.paymentModeList2=[];
			        		$scope.amountList2=[];
			        		$scope.data2=[];
			        		$scope.labels2=[];
			        		angular.forEach($scope.listForIP, function(value,key1) {
			        			var newArr={};
			        			$scope.paymentModeList2.push(value.paymentMode);
			        			$scope.amountList2.push(parseInt(value.amount));
			        		});
			        		 var paymentMode2=$scope.paymentModeList2;
			       				for (var i = 0; i < paymentMode2.length; i++) {
									$scope.labels2.push(paymentMode2[i]);
								}
			       			var amountMode2=$scope.amountList2;
			       				for (var i = 0; i < amountMode2.length; i++) {
									$scope.data2.push(amountMode2[i]);
								}
			       				
			       			 $scope.labels2 = $scope.labels2;
		    	       		 $scope.series2 =  $scope.labels2;
		    				 $scope.data2= $scope.data2;
			       		var  colorArray = [];
					 	   /*var letters = "0123456789ABCDEF"; 
						     for(var j=0;j<16;j++){
							var color = '#'; 
							for (var i = 0; i < 6; i++) {
							color += letters[(Math.floor(Math.random() * 16))];
						      } 
							    colorArray.push(color);
						    }*/
				       		var letters =['#22EBC8','#00D1DB','#00B3E2','#0093D6','#5372B9','#6F518F']; //"0F"; 
						     for(var j=0;j<16;j++){
							var color = ''; 
							for (var i = 0; i < 6; i++) {
							color = letters[i];//letters[(Math.floor(Math.random() * 16))];
							colorArray.push(color);
	//					    alert(color);
						    } 
						    }
						     $scope.colors2= colorArray;
						     $scope.options2 = {
		    				            legend: {
		    				                display: true,
		    				                labels: {
		    				                    fontColor: 'rgb(255, 99, 132)'
		    				                }
		    				            },
		    				            
		    				            animationEnabled: false,
		    				            title: {
		    				                display: true,
		    				                text: 'Finance For IP' 
		    				            } 
						     }
						     
		        	 }
						 
			    };
			    
			    //PIE ER
		        $scope.onClickFinanceER = function () {
		        	
	        	if($scope.listForER.length > 0){
		        		$scope.paymentModeList4=[];
		        		$scope.amountList4=[];
		        		$scope.data4=[];
		        		$scope.labels4=[];
		        		angular.forEach($scope.listForER, function(value,key1) {
		        			var newArr={};
		        			$scope.paymentModeList4.push(value.paymentMode);
		        			$scope.amountList4.push(parseInt(value.amount));
		        		});
		        		 var paymentMode4=$scope.paymentModeList4;
		       				for (var i = 0; i < paymentMode4.length; i++) {
								$scope.labels4.push(paymentMode4[i]);
							}
		       			var amountMode4=$scope.amountList4;
		       				for (var i = 0; i < amountMode4.length; i++) {
								$scope.data4.push(amountMode4[i]);
							}
		       			 $scope.labels4 = $scope.labels4
	    	       		 $scope.series4 =  $scope.labels4;
	    				 $scope.data4= $scope.data4;
		       				var  colorArray = [];
						 	   /*var letters = "0123456789ABCDEF"; 
							     for(var j=0;j<16;j++){
								var color = '#'; 
								for (var i = 0; i < 6; i++) {
								color += letters[(Math.floor(Math.random() * 16))];
							      } 
								    colorArray.push(color);
							    }*/
		       				var letters =['#22EBC8','#00D1DB','#00B3E2','#0093D6','#5372B9','#6F518F']; //"0F"; 
						     for(var j=0;j<16;j++){
							var color = ''; 
							for (var i = 0; i < 6; i++) {
							color = letters[i];//letters[(Math.floor(Math.random() * 16))];
							colorArray.push(color);
	//					    alert(color);
						    } 
						    }
							     $scope.colors4= colorArray;
							     $scope.options4 = {
			    				            legend: {
			    				                display: true,
			    				                labels: {
			    				                    fontColor: 'rgb(255, 99, 132)'
			    				                }
			    				            },
			    				            
			    				            animationEnabled: false,
			    				            title: {
			    				                display: true,
			    				                text: 'Finance For ER' 
			    				            } 
							     }
		    				
  			        	   }
					    };
					    //PIE DC
				        $scope.onClickFinanceDC = function () {
			        	if($scope.listForDC.length > 0){
				        		$scope.paymentModeList3=[];
				        		$scope.amountList3=[];
				        		$scope.data3=[];
				        		$scope.labels3=[];
				        		angular.forEach($scope.listForDC, function(value,key1) {
				        			var newArr={};
				        			$scope.paymentModeList3.push(value.paymentMode);
				        			$scope.amountList3.push(parseInt(value.amount));
				        		});
				        		 var paymentMode3=$scope.paymentModeList3;
				       				for (var i = 0; i < paymentMode3.length; i++) {
										$scope.labels3.push(paymentMode3[i]);
									}
				       			var amountMode3=$scope.amountList3;
				       				for (var i = 0; i < amountMode3.length; i++) {
										$scope.data3.push(amountMode3[i]);
									}
				       				
				       				$scope.labels3 = $scope.labels3
				    	       		 $scope.series3 =  $scope.labels3;
				    				 $scope.data3= $scope.data3;
				    				 
				       		  		var  colorArray = [];
								 	   /*var letters = "0123456789ABCDEF"; 
									     for(var j=0;j<16;j++){
										var color = '#'; 
										for (var i = 0; i < 6; i++) {
										color += letters[(Math.floor(Math.random() * 16))];
									      } 
										    colorArray.push(color);
									    }*/
						       		  	var letters =['#22EBC8','#00D1DB','#00B3E2','#0093D6','#5372B9','#6F518F']; //"0F"; 
									     for(var j=0;j<16;j++){
										var color = ''; 
										for (var i = 0; i < 6; i++) {
										color = letters[i];//letters[(Math.floor(Math.random() * 16))];
										colorArray.push(color);
				//					    alert(color);
									    } 
									    }
									     $scope.colors3= colorArray;
									     $scope.options3 = {
					    				            legend: {
					    				                display: true,
					    				                labels: {
					    				                    fontColor: 'rgb(255, 99, 132)'
					    				                }
					    				            },
					    				            
					    				            animationEnabled: false,
					    				            title: {
					    				                display: true,
					    				                text: 'Finance For DC' 
					    				            } 
									     }
				    				
				       		
     					        	}
							    }; 
				 
				  $scope.getFinanceListByVisitType=function(){
	                	var data = {
	                            organizationId : $scope.organizationId,
	                            unitId : $scope.unitId
	                       }
	                	 var URI = BASE_URL + ROOT_URL
	     				+ "/api/global/getBillingListByVisitType";
	     	           	GenericService.serviceAction("Post", URI, data)
	     				.then(function(response) {
	     							$scope.financeListByVisitType = [];
	     							if (response.data.status == "success") {
	     								$scope.financeListByVisitType = response.data.listObject;
	     								console.log("$scope.financeListByVisitType",$scope.financeListByVisitType);
	     								angular.forEach($scope.financeListByVisitType, function(value,key) {
   										if(value.visitTypeId==1 ){
   											$scope.listForOP = [];
   											angular.forEach(value.billingCollectionList, function(value1,key1) {
   												var newArr = {};
   												newArr.paymentMode=value1.paymentMode;
   												newArr.amount=value1.amount;
   												$scope.listForOP.push(newArr);
   											});
   											
   											$scope.onClickFinanceOP();
   											console.log("$scope.listForOP",$scope.listForOP);
   										}
   										if(value.visitTypeId==2 ){
   											 $scope.listForIP = [];
   											angular.forEach(value.billingCollectionList, function(value1,key1) {
   												var newArr = {};
   												newArr.paymentMode=value1.paymentMode;
   												newArr.amount=value1.amount;
   												$scope.listForIP.push(newArr);
   												
   											});
   											$scope.onClickFinanceIP();
   											console.log("$scope.listForIP",$scope.listForIP);
   										}
   										if(value.visitTypeId==3){
   											  $scope.listForDC = [];
   											angular.forEach(value.billingCollectionList, function(value1,key1) {
   												var newArr = {};
   												newArr.paymentMode=value1.paymentMode;
   												newArr.amount=value1.amount;
   												$scope.listForDC.push(newArr);
   											
   											});
   											$scope.onClickFinanceDC();
   											console.log("$scope.listForDC",$scope.listForDC);
   										}
   										if(value.visitTypeId==4){
   											  $scope.listForER = [];
   											angular.forEach(value.billingCollectionList, function(value1,key1) {
   												var newArr = {};
   												newArr.paymentMode=value1.paymentMode;
   												newArr.amount=value1.amount;
   												$scope.listForER.push(newArr);
   											
   											});
   											$scope.onClickFinanceER();
   											console.log("$scope.listForER",$scope.listForER);
   										}
   									});
	     							}
	     						});
	                }
	                $scope.getFinanceListByVisitType();
				 
				 $scope.dummyList = [];
	                $scope.label=[];
	                $scope.getBedOccupancyList=function(){
	                	var data = {
	                            organizationId : $scope.organizationId,
	                            unitId : $scope.unitId
	                       }
	                	 var URI = BASE_URL + ROOT_URL
	     				+ "/api/global/getDashboardBedOccupancyList";
	     	           	GenericService
	     				.serviceAction("Post", URI, data)
	     				.then(
	     						function(response) {
	     							$scope.bedOccupancyList = [];
	     							if (response.data.status == "success") {
	     								$scope.bedOccupancyList = response.data.listObject;
	     								console.log("$scope.bedOccupancyList",$scope.bedOccupancyList);
	     								 $scope.items=[];
	     								angular.forEach($scope.bedOccupancyList, function(value,key) {
	     					       		if(value.bedCategoryId!=undefined || value.bedCategoryId!=null){
	     					       		 if(value.bedVacantCount!=null){
	     					       		 var newArr = {};
	     					       		newArr.value = value.bedVacantCount 
	     					       		newArr.text = value.bedCategory;
	     					       		newArr.status = "bedVacant";
	     					       		newArr.statusId = 1;
	     					       		 $scope.items.push(newArr);
	     					       		}
	     					       		if(value.houskeepingCount!=null){
	     					       		 var newArr = {};
	     					       		newArr.value = value.houskeepingCount ;
	     					       		newArr.text = value.bedCategory;
	     					       		newArr.status = "houskeeping";
	     					       		newArr.statusId = 2;
	     					       		$scope.items.push(newArr);
	     					       		}
	     					       		if(value.occupiedCount!=null){
	     					       		 var newArr = {};
	     					       		newArr.value = value.occupiedCount ;
	     					       		newArr.text = value.bedCategory;
	     					       		newArr.status = "occupied";
	     					       		newArr.statusId = 3;
	     					       			$scope.items.push(newArr);
	     					       		}
	     					         	var lbls = value.bedCategory;
						       		    $scope.label.push(lbls);
	     					       		}
	     				       			});
//	     								console.log("$scope.items",$scope.items);
	     								    $scope.statusList=[];
	     					  	            $scope.statusList.push({ value:1,text: "bedVacant"});
	     					  	            $scope.statusList.push({ value:2,text: "houskeeping"});
	     					  	             $scope.statusList.push({ value:3,text: "occupied"});
	     					  	             
	     					  	          $scope.itemOccuepied = [];  
	     					  	          $scope.itemHousekeeping = [];  
	     					  	          $scope.itemvacent = [];  
	     					  	          
	     								if($scope.items.length>0){
	     									angular.forEach($scope.statusList, function(value1,key) {
	     										 var newArr1 = {};
	  											//newArr1.text = value1.text;	
	       	     					       		newArr1.value = value1.value;	
	       	     					       	$scope.itemFinal = [];
	     									angular.forEach($scope.items, function(value,key) {
	     										if(value1.text ==value.status ){
	     										if(value.status=="occupied"){
	          	     					       		$scope.itemOccuepied.push(value.value);
	     										}
	     										if(value.status=="houskeeping"){
	    											 var newArr = {};
	         	     					       		$scope.itemHousekeeping.push(value.value);
	    										}
	     										if(value.status=="bedVacant"){
	    											 var newArr = {};
	    									
	         	     					       		$scope.itemvacent.push(value.value);
	    										 }
	     										}
	     									});
	     								
	     									});
	     									
	     									$scope.dummyList.push($scope.itemOccuepied);
	     									$scope.dummyList.push($scope.itemHousekeeping);
	     									$scope.dummyList.push($scope.itemvacent);
	     									$scope.onClick();
//	     									console.log("$scope.dummyList",$scope.dummyList);
	     								}
	     							}
	     						});
	                	
	                }
	                $scope.getBedOccupancyList();
	                
	    
						    
	                
	                $scope.itmLst = [];
			       	 /*$scope.onClick = function () {
			       	         $scope.labels =['SUPDLX','DLX','GEN','SEDLX','PR','TB','ER','ER','Deluxe','SUITE'
			       	        	 ,'DYCR','DYCR','DYCR','DYCR','DYCR','DYCR','DYCR','DYCR','DYCR',
			       	        	 'DYCR','DYCR'] ;
			       		     $scope.labels = $scope.label;
							 $scope.colors= ['#006BAF', '#4A6BB8', '#7568BC', '#9A64B9'];
							 $scope.series =  [ "vaccant","House Keeping",  "Occupied"];
							 $scope.data =  $scope.dummyList ;
							 console.log("DataBedAqupancyyyy",$scope.dummyList );
							 console.log("Datalabelssss",$scope.labels);
							 
							 var ctx = document.getElementById("bar").getContext('2d');
							 var myChart = new Chart(ctx, {
//							     type: 'bar',
							     type: 'scrollColumn2d',
						         renderAt: 'chart-container',
						         width: '700',
						         height: '400',
						         dataFormat: 'json',
						         dataSource: {
						           "chart": {
						             "theme": "fusion",
						             "caption": "Sales Trends",
						             "subcaption": "2016 - 2017",
						             "xaxisname": "Month",
						             "yaxisname": "Revenue",
						             "showvalues": "1",
						             "numberprefix": "$",
						     				"numVisiblePlot": "12",
						             "scrollheight": "10",
						             "flatScrollBars": "1",
						             "scrollShowButtons": "0",
						             "scrollColor": "#cccccc",
						             "showHoverEffect": "1"
						           }
						         },
							     data: {
							         labels: $scope.labels,
							         datasets: [{
							             label: '# of Votes',
							             data:$scope.data,
							             borderWidth: 1
							         }]
							     },
							     options: {
							    	  scales: {
							    	      yAxes: [
							    	        {
							    	          id: 'y-axis-1',
							    	          type: 'linear',
							    	          display: true,
							    	          position: 'left'
							    	        },
							    	        {
							    	          id: 'y-axis-2',
							    	          type: 'linear',
							    	          display: true,
							    	          position: 'right'
							    	        }
							    	      ]
							    	    }
							     }
							 });
							 
							 $scope.datasetOverride = [{
							        fill: true,
							        backgroundColor: [
							          "#006BAF",
							          "#006BAF",
							          "#006BAF",
							          "#006BAF",
							          "#006BAF",
							          "#006BAF",
							          "#006BAF",
							          "#006BAF",
							          "#006BAF",
							          "#006BAF",
							          "#006BAF",
							          "#006BAF",
							          "#006BAF",
							          "#006BAF",
							          "#006BAF",
							          "#006BAF",
							        ]
							      }, {
							        fill: true,
							        backgroundColor: [
							          "#4A6BB8",
							          "#4A6BB8",
							          "#4A6BB8",
							          "#4A6BB8",
							          "#4A6BB8",
							          "#4A6BB8",
							          "#4A6BB8",
							          "#4A6BB8",
							          "#4A6BB8",
							          "#4A6BB8",
							          "#4A6BB8",
							          "#4A6BB8",
							          "#4A6BB8",
							          "#4A6BB8",
							          "#4A6BB8",
							          "#4A6BB8"
							        ]
							      }, {
							        fill: true,
							        backgroundColor: [
							          "#7568BC",
							          "#7568BC",
							          "#7568BC",
							          "#7568BC",
							          "#7568BC",
							          "#7568BC",
							          "#7568BC",
							          "#7568BC",
							          "#7568BC",
							          "#7568BC",
							          "#7568BC",
							          "#7568BC",
							          "#7568BC",
							          "#7568BC",
							          "#7568BC",
							          "#7568BC"
							        ]
							      }, {
							        fill: true,
							        backgroundColor: [
							          "#9A64B9",
							          "#9A64B9",
							          "#9A64B9",
							          "#9A64B9",
							          "#9A64B9",
							          "#9A64B9",
							          "#9A64B9",
							          "#9A64B9",
							          "#9A64B9",
							          "#9A64B9",
							          "#9A64B9",
							          "#9A64B9",
							          "#9A64B9",
							          "#9A64B9",
							          "#9A64B9",
							          "#9A64B9"
							        ]
							      },
							    ];
							    $scope.options = {
							            legend: {
							                display: true,
							                labels: {
							                    fontColor: 'rgb(4, 0, 0)'
							                }
							            },
							            title: {
							                display: true,
							                text: 'Bed Occupancy'
							            }
							    };
							 
							    };*/
		           
						    
						    
							    $scope.labels = $scope.label;
							    $scope.colors= ['#006BAF', '#4A6BB8', '#7568BC', '#9A64B9'];
							    $scope.series = [ "vaccant","House Keeping",  "Occupied"];
							    $scope.data = $scope.dummyList;
							    label: '# of Votes',
							    $scope.onClick = function(points, evt) {
							      console.log(points, evt);
							    };
							    $scope.datasetOverride = [{
							        fill: true,
							        backgroundColor: [
							          "#006BAF",
							          "#006BAF",
							          "#006BAF",
							          "#006BAF",
							          "#006BAF",
							          "#006BAF",
							          "#006BAF",
							          "#006BAF",
							          "#006BAF",
							          "#006BAF",
							          "#006BAF",
							          "#006BAF",
							          "#006BAF",
							          "#006BAF",
							          "#006BAF",
							          "#006BAF",
							        ]
							      }, {
							        fill: true,
							        backgroundColor: [
							          "#4A6BB8",
							          "#4A6BB8",
							          "#4A6BB8",
							          "#4A6BB8",
							          "#4A6BB8",
							          "#4A6BB8",
							          "#4A6BB8",
							          "#4A6BB8",
							          "#4A6BB8",
							          "#4A6BB8",
							          "#4A6BB8",
							          "#4A6BB8",
							          "#4A6BB8",
							          "#4A6BB8",
							          "#4A6BB8",
							          "#4A6BB8"
							        ]
							      }, {
							        fill: true,
							        backgroundColor: [
							          "#7568BC",
							          "#7568BC",
							          "#7568BC",
							          "#7568BC",
							          "#7568BC",
							          "#7568BC",
							          "#7568BC",
							          "#7568BC",
							          "#7568BC",
							          "#7568BC",
							          "#7568BC",
							          "#7568BC",
							          "#7568BC",
							          "#7568BC",
							          "#7568BC",
							          "#7568BC"
							        ]
							      }, {
							        fill: true,
							        backgroundColor: [
							          "#9A64B9",
							          "#9A64B9",
							          "#9A64B9",
							          "#9A64B9",
							          "#9A64B9",
							          "#9A64B9",
							          "#9A64B9",
							          "#9A64B9",
							          "#9A64B9",
							          "#9A64B9",
							          "#9A64B9",
							          "#9A64B9",
							          "#9A64B9",
							          "#9A64B9",
							          "#9A64B9",
							          "#9A64B9"
							        ]
							      },
							    ];
							    $scope.options = {
							      scales: {
							        yAxes: [{
							          id: 'y-axis-1',
							          type: 'linear',
							          display: true,
							          position: 'left'
							        }]
							      },
								            legend: {
								                display: true,
								                labels: {
								                    fontColor: 'rgb(4, 0, 0)'
								                }
								            },
								            title: {
								                display: true,
								                text: 'Bed Occupancy'
								            },
							      responsive: false,
							      animation:{
							          onComplete : function(){
							                  var sourceCanvas = this.chart.ctx.canvas;
//							                  var copyWidth = this.chart.controller.chartArea.left - 5;
							                  // the +5 is so that the bottommost y axis label is not clipped off
							                  // we could factor this in using measureText if we wanted to be generic
//							                  var copyHeight = this.chart.controller.chartArea.bottom + 5; // 282 //this.scale.endPoint + 5;
							                  
							                  var targetCtx = document.getElementById("myChartAxis").getContext("2d");
//							                  targetCtx.canvas.width = copyWidth;
//							                  targetCtx.drawImage(sourceCanvas, 0, 0, copyWidth, copyHeight, 0, 0, copyWidth, copyHeight);
							  		
							          }
							      }
							    };
						    
				           
										    
										    
	            
	                
	               
	    				    
			
            }]);