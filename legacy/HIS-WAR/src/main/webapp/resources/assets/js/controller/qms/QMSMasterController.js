/**
 * 
 */

angular.module('myApp')
		.controller("QMSMasterController",
		['$scope','$rootScope','$state','$cookies','GenericService','PagerService','promiseFactory','$sessionStorage','growl','$cookieStore','$window','$timeout',
		function($scope,$rootScope,$state, $cookies, GenericService,PagerService,promiseFactory,$sessionStorage,growl,$cookieStore,$window,$timeout){
				
	
			/**Variables for pagination */
			$scope.offset = 0;
			$scope.noOfRecordsPerPage = 10;
			$scope.commonList = [];
			$scope.totalNoOfRecords;
			$scope.searchKeyword;
			$scope.commonListCount;
			$scope.initQMSMaster = function() {
				
				var cookieObject = $cookies.getObject('cookieObject');
				if(cookieObject == undefined){
					$state.go('login');
					return;
				}
				console.log("cookies Object",cookieObject);
				$scope.orgUnitId = cookieObject.unitId;
				$scope.orgId = cookieObject.organizationId;
				$scope.userId = cookieObject.userId;
				$scope.hospitalName = cookieObject.unitNme;
				var labConfig  =    $scope.checkLabConfig();
				$scope.prnUrl = labConfig.prnUrl;
				var cookieObject,cookieObjectAppConfig;
				[cookieObject,cookieObjectAppConfig] = $scope.checkCookies();
				var cookieObjectAppConfigSecond = JSON.parse(localStorage.getItem('cookieObjectAppConfigSecond'));
				if (cookieObjectAppConfigSecond != undefined) {
				$scope.qmsPrnPrint = cookieObjectAppConfigSecond.qmsPrnPrint;
				}
				/** Variables for pagination */
				$scope.offset = 0;
				$scope.noOfRecordsPerPage = 10;
				$scope.totalNoOfRecords;
				$scope.searchKeyword;
				$scope.commonListCount;

				$rootScope.loginpage = false;
				$scope.saveBtnFlag = true;
				$scope.updateBtnFlag = false;

				// for popup
				$scope.showModal = false;
				$scope.buttonClicked = "";
				$scope.popUpFlag = true;
				// end for popup
				$scope.kioskName="";
				$scope.tokenNumber="";
				$scope.common = {
						qmsKioskMasterId:"",
						qmsDeptName:"",
						createdBy:$scope.userId,
						updatedBy:$scope.userId,
						organisationId:$scope.orgId,
						unitId:$scope.orgUnitId					
					};
				
			}
			$scope.initQMSMaster();
			
			//setting kioskId in cookies start 
			$scope.SetCookies = function () {
                $cookies.put("kioskIdForTokenGen", $scope.common.qmsKioskMasterId);
            };
            $scope.getCookie = function(key) {
                return $cookieStore.get(key);
            };
            $scope.cookieskioskId=$scope.getCookie("kioskIdForTokenGen");
            console.log("cookies Id",$scope.cookieskioskId);
          //setting kioskId in cookies end 
            
			$scope.getQMSMasterList = function() {				
				var data = {
							organisationId:$scope.orgId,
							unitId:$scope.orgUnitId						
						};
				
				var URI = BASE_URL + ROOT_URL + "/api/pharmacy/getActiveQMSMasterList";
				GenericService.serviceAction("POST", URI, data).then(function(response) {
//					angular.element('#qmsKioskDetailsModal').modal('show');
					if($scope.cookieskioskId==undefined){
						angular.element('#qmsKioskDetailsModal').modal('show');
					}else{
						$scope.common.qmsKioskMasterId=$scope.cookieskioskId;
						$scope.confirmQMS();
					}
					$scope.activeQMSList = response.data.listObject;
				});
			}
			$scope.getQMSMasterList();
			
			$scope.qmsPrnPrintFormat = "";
			$scope.getQMSPrnFormat = function() {				
				var data = {
							unitId:$scope.orgUnitId						
						};
				
				var URI = BASE_URL + ROOT_URL + "/api/unit/applicationConfiguration/getQMSPrnFormat";
				GenericService.serviceAction("POST", URI, data).then(function(response) {
					
					$scope.qmsPrnPrintFormat = response.data.listObject[0].qmsPrnPrintFormat;
				});
			}
			$scope.getQMSPrnFormat();
			
			$scope.selectQMSKiosk=function(kioskId){
				//setting cookies values
				$scope.SetCookies();
				console.log("kioskId",kioskId);
			}
			
			$scope.confirmQMS=function(){
					console.log("kiosk Id",$scope.common.qmsKioskMasterId);
					if($scope.common.qmsKioskMasterId!="" ||$scope.common.qmsKioskMasterId!=undefined){			
					var data = {
							organisationId:$scope.orgId,
							unitId:$scope.orgUnitId	,
							qmsKioskMasterId:$scope.common.qmsKioskMasterId
							};
			
						var URI = BASE_URL + ROOT_URL + "/api/pharmacy/getActiveDeptListByKioskId";
						GenericService.serviceAction("POST", URI, data).then(function(response) {
							$rootScope.stopLoader();
							$scope.activeQMSDeptList = response.data.listObject;
							angular.forEach($scope.activeQMSDeptList,function(value,index)
				                    {
										if(value.qmsDeptName == "Unregistered"){
											value.discripton = "For Walk-in Patients";
										}
										if(value.qmsDeptName == "Registered"){
											value.discripton = "For Already Registered Patients";
										}
				                    });
							$scope.kioskName=response.data.listObject[0].qmsDesc;
						});
				
					}
			}
			$scope.getDeptDetails=function(obj){
				$scope.deptDetails=obj;
				$scope.saveToken();
			}
			
			$scope.saveToken=function(){
				$scope.tokenNumber="wait...";
				console.log("generate token number",$scope.deptDetails);
				
				var data = {						
						qmsKioskMasterId:$scope.deptDetails.qmsKioskMasterId,
						startLetter:$scope.deptDetails.startLetter,
						startNumber:$scope.deptDetails.startNumber,
						qmsDeptMasterId:$scope.deptDetails.qmsDeptMasterId,
						createdBy:$scope.userId,
						updatedBy:$scope.userId,
						organisationId:$scope.orgId,
						unitId:$scope.orgUnitId	,
					};
				 $rootScope.startLoader();
				 $("#tokenPrintId").removeClass("enabled");
				 $("#tokenPrintId").addClass("disabled");
					var URI = BASE_URL + ROOT_URL + "/api/pharmacy/saveToken";
					GenericService.serviceAction("POST", URI, data).then(function(response) {
						$rootScope.stopLoader();
						$scope.tokenNumber = response.data.object;
						$("#tokenPrintId").removeClass("disabled");
						$("#tokenPrintId").addClass("enabled");
						$rootScope.stopLoader();
						 $timeout(function () {
							
							 if($scope.prnUrl != undefined && $scope.prnUrl != null && $scope.prnUrl != "" && $scope.qmsPrnPrint == "Y" && $scope.qmsPrnPrintFormat != undefined  && $scope.qmsPrnPrintFormat != null  && $scope.qmsPrnPrintFormat != "" ){
								 angular.element('#tokenDisplayModalPRN').modal('show'); 
								 $scope.printPRNPrint($scope.qmsPrnPrintFormat);
							 }else{
								 $scope.printTokenNumberReport();
							 }
						
						}, 2000);
					});
				
			}

                    $scope.firstImg;
                    $scope.imageBackground = function(){
                                    var URI = BASE_URL + ROOT_URL + "/api/unit/applicationConfiguration/screen-images/"+$scope.orgId+"/1"
                                        GenericService.serviceAction("GET", URI).then(function(response) {
                                        	if(response.data.object != null){
                                        $scope.firstImg=response.data.object.imageURL;
                                        	}
                                        });
                                    }
                     $scope.imageBackground();

                                $scope.getDeptDetails=function(obj){
                                    $scope.deptDetails=obj;
                                    $scope.saveToken();
                                }
			
			$scope.printPRNPrint= function(prnObject){						
				
				$timeout(function () {
	                	angular.element('#tokenDisplayModalPRN').modal('hide'); 
					}, 4000);
				
				var prnObjectToken = prnObject.toString().replace("@tokenNumber",$scope.tokenNumber);
				
				var prnObjectDate = prnObjectToken.toString().replace("@date",moment(new Date()).format('YYYY-MM-DD'));
				
				var prnObjectFinal = prnObjectDate.toString().replace("@time",moment(new Date()).format('HH:mm'));
				
                var url = $scope.prnUrl;

                var xhr = new XMLHttpRequest();
                xhr.open("POST", url);

                xhr.setRequestHeader("Content-Type", "text/plain");

                xhr.onreadystatechange = function () {
                   if (xhr.readyState === 4) {
                      console.log(xhr.status);
                      console.log(xhr.responseText);
                   }};

                var data = prnObjectFinal;
                xhr.send(data);
                
			}
			
			
			$scope.printTokenNumberReport=function(){
			
				console.log("print token number");
//				$('#qmsTokenPrint').modal('show');
				$scope.currentDate = new Date();
				$scope.currentDate = moment($scope.currentDate).format('DD-MM-YYYY');
				$scope.currentTime = moment(new Date()).format('HH:mm:ss');
				var printContents = $window.document.getElementById('qmsTokenPrintingId').innerHTML;
		        var popupWin = $window.open('', '_blank', 'width=400,height=400');
		        popupWin.document.open();
		        popupWin.document.write('<html><head><link rel="stylesheet" type="text/css" href="style.css" /></head><body onload="window.print()">' + printContents + '</body></html>');
		        popupWin.document.close();
			}
			
			
			
}]);