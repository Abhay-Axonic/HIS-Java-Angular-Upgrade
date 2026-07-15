'use strict';
/**
 * @Author Sneha Dalave
 * @Author Chetna Dhokne 
 * @name myApp.controller:dashboardInfoMasterController
 * @description #dashboardInfoMasterController Controller of the myApp
 */
angular
    .module('myApp')
    .controller(
        'dashboardInfoMasterController',
        ['$scope', '$rootScope', '$state', '$http', '$cookies', 'GenericService', '$sessionStorage','$stateParams','$localStorage', '$location',
            function ($scope, $rootScope, $state, $http, $cookies, GenericService, $sessionStorage, $stateParams , permissions, $localStorage ,$location) {
        	
        		
        	var cookieObject,cookieObjectAppConfig;
			[cookieObject,cookieObjectAppConfig] = $scope.checkCookies();
			$rootScope.isKioskForMenu = cookieObjectAppConfig.isKiosk;
			$rootScope.isNursingAssessment = cookieObjectAppConfig.isNursingAssessment;
			$rootScope.loginpage = true;
			$scope.updateScope = false;
			$scope.popUpFlag = true;
			
            $scope.main=false;
			
			$scope.roleName = cookieObjectAppConfig.roleName;
			$scope.nurseRole = cookieObjectAppConfig.nurseRole;	 
			$scope.doctorRole = cookieObjectAppConfig.doctorRole;
			
			$rootScope.isKioskForMenu = cookieObjectAppConfig.isKiosk;
			$rootScope.isAppointmentRequiredForMenu = cookieObjectAppConfig.isAppointmentRequired;
			$rootScope.isNursingAssessment = cookieObjectAppConfig.isNursingAssessment;
			$rootScope.isPACSRequired = cookieObjectAppConfig.isPACSRequired;
			$rootScope.refreshPageTime = cookieObjectAppConfig.refreshPageTime;
			$rootScope.isSuperUser = cookieObject.isSuperUser !=null ? cookieObject.isSuperUser : 'N' ;
			$rootScope.skipReadyForBilling = cookieObjectAppConfig.skipReadyForBilling !=null ? cookieObjectAppConfig.skipReadyForBilling : 'N' ;
			
		
			$scope.click=function(info){
				$scope.main=true;
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
				else if(info==35){
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
				}else if(info==32){
					$scope.billing=true;
				}
				else if(info==34){
					$scope.physiotherapy=true;
				}
				else if(info==36){
					$scope.QMS=true;
				}
				
			}
			
			
			if(!angular.equals({},$stateParams.params))
			{
			$scope.main=true;
			$scope.click($stateParams.params);
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
					$scope.billing=false;
				    $scope.procedure=false;
				    $scope.physiotherapy=false;
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
                
                /**********************Grapg Code*******************/
             
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
            		    	 $rootScope.counterList = JSON.parse(localStorage.getItem('counterList'));
            		     }
            		}else{
            			$rootScope.counterId = 0;
            		}
            	/**================================COUNTER CODE END======================================*/
                
            	if($scope.nurseRole==1)
            	{
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
        			}else{
        				$scope.wardListForGetBedList = nursingStationsWardsList;
        			}
            	}
            	
            	
            	
            	
            	
                /*
                $scope.dummyList = [];
	            
	            $scope.dummyList.push({ value:1,text: "suit", items: [
          	  	  { text :"Occupied",value1:1},
          		  { text:"vaccant",value1:2},
          		  { text:"House Keeping",value1:3},
          		  { text:"Under Maintainance",value1:4}
	            	   ]});
	            $scope.dummyList.push({ value:2,text: "Deluxe", items: [
	            	{ text :"Occupied",value1:5},
	          		  { text:"vaccant",value1:6},
	          		  { text:"House Keeping",value1:7},
	          		  { text:"Under Maintainance",value1:8}
           		]});
	            $scope.dummyList.push({ value:3,text: "Twin Share", items: [
	            	{ text :"Occupied",value1:9},
	          		  { text:"vaccant",value1:10},
	          		  { text:"House Keeping",value1:11},
	          		  { text:"Under Maintainance",value1:12}
            		  ]});
	            $scope.dummyList.push({value:4,text: "General", items: [
	            	{ text :"Occupied",value1:13},
	          		  { text:"vaccant",value1:14},
	          		  { text:"House Keeping",value1:15},
	          		  { text:"Under Maintainance",value1:16}
         		 ]});
	            
	            console.log("$scope.dummyList",$scope.dummyList);*/
	            
            /*  $scope.dummyList = [];
                
                $scope.dummyList.push({ value:1,text: "Occupied", items: [
            	  	  { text :"suit",value1:1},
            		  { text:"Deluxe",value1:2},
            		  { text:"Twin Share",value1:3},
            		  { text:"General",value1:4}
  	            	   ]});
  	            $scope.dummyList.push({ value:2,text: "vaccant", items: [
  	            	 { text :"suit",value1:5},
           		  { text:"Deluxe",value1:6},
           		  { text:"Twin Share",value1:7},
           		  { text:"General",value1:8}
             		]});
  	            $scope.dummyList.push({ value:3,text: "House Keeping", items: [
  	            	 { text :"suit",value1:9},
           		  { text:"Deluxe",value1:10},
           		  { text:"Twin Share",value1:11},
           		  { text:"General",value1:12}
              		  ]});
  	            $scope.dummyList.push({value:4,text: "Under Maintainance", items: [
  	            	 { text :"suit",value1:13},
           		  { text:"Deluxe",value1:14},
           		  { text:"Twin Share",value1:15},
           		  { text:"General",value1:16}
           		 ]});
	            console.log("$scope.dummyList",$scope.dummyList); */
	            
	            
	            /*$scope.itmLst = [];
		       	 $scope.onClick = function () {
			       		$scope.data = [];
			       		$scope.sampleList=[];
			       		$scope.label=[];
			       		$scope.series=[];
			       		$scope.test=[];
			       		angular.forEach($scope.dummyList, function(value,key) {
			       			$scope.itmLst = [];
			       			$scope.ser=[];
			       			$scope.datalistForArray=[];
			       			var Arr = {};
			       			Arr.label = value.text;
			       			$scope.label.push(value.text);
			       			
			       			angular.forEach(value.items, function(value2,key) {
				       			$scope.itmLst.push(value2.value1);
//				       			$scope.ser.push(value2.text);
				       			
				       		
			       			});
			       			var newArr = {};
		       				var series={};
		       				newArr.id = value.value;
		       				newArr.lst = $scope.itmLst;
		       				$scope.arrayList=$scope.itmLst;
		       				newArr.ser=$scope.ser;
		       				$scope.sampleList.push(newArr);
		       				$scope.datalistForArray.push($scope.arrayList);
		       				$scope.data[i]=$scope.datalistForArray;
		       				var datalistForArray = $scope.sampleList;
		       				
							for (var i = 0; i <  $scope.sampleList.length; i++) {
								$scope.data[i]= $scope.sampleList[i].lst;
								
							}
							
						
			       	 });
			       		console.log("$scope.label",$scope.label);
			       		console.log("$scope.sampleList",$scope.sampleList);
		       			console.log("$scope.data",$scope.data);
						
			       		$scope.labels = $scope.label;
						 $scope.colors= ['#ED402A', '#F0AB05', '#A0B421', '#00A39F'];
						 $scope.series =  ["Occupied", "vaccant", "House Keeping"];
						 $scope.data =  $scope.data ;
						 var ctx = document.getElementById("bar").getContext('2d');
						 var myChart = new Chart(ctx, {
						     type: 'bar',
						    
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
						          "#ED402A",
						          "#ED402A",
						          "#ED402A",
						          "#ED402A"
						        ]
						      }, {
						        fill: true,
						        backgroundColor: [
						          "#F0AB05",
						          "#F0AB05",
						          "#F0AB05",
						          "#F0AB05"
						        ]
						      }, {
						        fill: true,
						        backgroundColor: [
						          "#A0B421",
						          "#A0B421",
						          "#A0B421",
						          "#A0B421"
						        ]
						      }, {
						        fill: true,
						        backgroundColor: [
						          "#00A39F",
						          "#00A39F",
						          "#00A39F",
						          "#00A39F"
						        ]
						      },
						    ];
						    $scope.options = {
						            legend: {
						                display: true,
						                labels: {
						                    fontColor: 'rgb(255, 99, 132)'
						                }
						            },
						            title: {
						                display: true,
						                text: 'Bed Occupancy'
						            }
						    };
						 
						    };*/
	            
	            
	            
	            
	            /*****working code**************/
/*	       	 $scope.onClick = function () {
	       		$scope.finalArr = [];
	       		$scope.label=[];
	       		angular.forEach($scope.dummyList, function(value,key) {
	       			var Arr = {};
	       			Arr.text = value.text;
	       			angular.forEach($scope.dummyList, function(value,key) {
	       				var newArr = {};
	       				$scope.finalArr.push(newArr);
	       			});
	       			
	       			$scope.label.push(Arr);
				
	       	 });
				
	       		$scope.labels = ['suit', 'Deluxe', 'Twin Share', 'General'];
				 $scope.colors= ['#ED402A', '#F0AB05', '#A0B421', '#00A39F'];
				 $scope.series = ['Occupied', 'vaccant','House Keeping','Under Maintainance'];
				 $scope.data = [
					    [10,12,12,10],
					    [10,10,10,11],
					    [12,10,10,12],
				        [10,5,10,10]
					  ];
				 var ctx = document.getElementById("bar").getContext('2d');
				 var myChart = new Chart(ctx, {
				     type: 'bar',
				    
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
				             yAxes: [{
				                 ticks: {
				                     beginAtZero:true
				                 }
				             }]
				         }
				     }
				 });
				 
				 $scope.datasetOverride = [{
				        fill: true,
				        backgroundColor: [
				          "#ED402A",
				          "#ED402A",
				          "#ED402A",
				          "#ED402A"
				        ]
				      }, {
				        fill: true,
				        backgroundColor: [
				          "#F0AB05",
				          "#F0AB05",
				          "#F0AB05",
				          "#F0AB05"
				        ]
				      }, {
				        fill: true,
				        backgroundColor: [
				          "#A0B421",
				          "#A0B421",
				          "#A0B421",
				          "#A0B421"
				        ]
				      }, {
				        fill: true,
				        backgroundColor: [
				          "#00A39F",
				          "#00A39F",
				          "#00A39F",
				          "#00A39F"
				        ]
				      },
				    ];
				    $scope.options = {
				            legend: {
				                display: true,
				                labels: {
				                    fontColor: 'rgb(255, 99, 132)'
				                }
				            },
				            title: {
				                display: true,
				                text: 'Bed Occupancy'
				            }
				    };
				 
				    };*/
	         
		      
            }]);