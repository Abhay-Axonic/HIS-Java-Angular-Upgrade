/**
 * formFSelfIdController
 */

angular.module('myApp')
		.controller("formFSelfIdController",
		['$scope','$rootScope','$state','$cookies','GenericService','PagerService','promiseFactory','$sessionStorage','growl','BillingGenericService','GenericServiceParamHeader','$timeout','$stateParams',
		function($scope,$rootScope,$state, $cookies, GenericService,PagerService,promiseFactory,$sessionStorage,growl,BillingGenericService,GenericServiceParamHeader,$timeout,$stateParams){
			$rootScope.loginpage = true;
			$scope.saveBtnFlag = true;
			$scope.updateBtnFlag = false;
			$scope.updateScope = false;
			$scope.popUpFlag = true;
	
			/**Variables for pagination */
			$scope.offset = 0;
			$scope.noOfRecordsPerPage = 10;
			$scope.commonList = [];
			$scope.usersList=[];
			$scope.totalNoOfRecords;
			$scope.searchKeyword;
			$scope.commonListCount;
			
			var cookieObject = $cookies.getObject('cookieObject');
			if(cookieObject == undefined){
				$state.go('login');
				return;
			}
			
			
			
			$scope.unitId = cookieObject.unitId;
			$scope.organisationId = cookieObject.organizationId;
			$scope.userId = cookieObject.userId;
			$scope.createdBy =cookieObject.userId;
			$scope.updatedBy =cookieObject.userId;
			
			
			$scope.UserNursingStationMppr=[];
			$scope.nursingStationList = [];
			$scope.selectUserNursingStationMapperList=[];
			
			
			if (angular.equals({},
					$stateParams.itemObj)) {
				return;
			} else {
				
				$scope.selectedForm = $stateParams.itemObj;
				
				 $scope.templateObj = { 
						 "consentTransactionId":"",
							"consentMasterId":"",
							"admissionId":null,
							"encounterId":"",
							"templateDesc":"",
							"status":"A",
							"createdBy":$scope.userId,
							"createdDate":"",
							"updatedBy":$scope.userId,
							"updatedDate":"",
							"unitId":$scope.unitId,
							"organizationId":$scope.organizationId,
							"consentTypeId":"1000",
							"appointmentId" : $scope.selectedForm.appointmentId,
							"encounterId" : $scope.selectedForm.encounterId,
		    				"admissionId" : $scope.selectedForm.admissionId
						}
				 
				 $scope.resetValue = $scope.selectedForm.templateOriginal;
				 
				 $timeout( function(){
					 $scope.value = $stateParams.itemObj.templateDesc;
		          },2000);
				 
				
			}
			
			
			
			$scope.initMaster = function(){
								 
				//$scope.value = '#frontImage<br><br>मैं <b data-id="pname">NISHU W/O NARENDER S/O SHRI BHAGWAN</b> प्रमाणित करती हूँ की ये id मेरी है और मैं इसी पते पर रहती हूँ  | मैंने अल्ट्रसाउंड के लिए केवल&nbsp; &nbsp; &nbsp;रुपये दिए है । इनके अलावा मेरे से कोई पैसे नहीं लिए गये है ।<br><br>#backImage<br><br><br><br><br><br><div><br><br></div><br><br><br>#kinfront<br><br><br><br>ये ID मेरे&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;की है, ओर मैं फ़िलहाल इसी पते पर रह रहीं हूँ 1&nbsp;&nbsp;मैंने&nbsp;अल्ट्रसाउंड के लिए केवल&nbsp; &nbsp; &nbsp;रुपये दिए है । इनके अलावा मेरे से कोई पैसे नहीं लिए गये है ।<br><div><br>#kinback<br>                   ';
				  var _video = null,
			        patData = null;

			    $scope.patOpts = {x: 0, y: 0, w: 25, h: 25};

			    // Setup a channel to receive a video property
			    // with a reference to the video element
			    // See the HTML binding in main.html
			    $scope.channel = {};

			    $scope.webcamError = false;
			    $scope.onError = function (err) {
			        $scope.$apply(
			            function() {
			                $scope.webcamError = err;
			            }
			        );
			    };

			    $scope.onSuccess = function () {
			        // The video element contains the captured camera data
			        _video = $scope.channel.video;
			        _video.width = 180;
			        _video.height = 120;
			        $scope.$apply(function() {
			        	//alert(_video.width);
			            $scope.patOpts.w = _video.width;
			            $scope.patOpts.h = _video.height;
			            //$scope.showDemos = true;
			        });
			    };

			    $scope.onStream = function (stream) {
			        // You could do something manually with the stream.
			    };

				$scope.makeSnapshot = function(type) {
			        if (_video) {
			        	
			            var patCanvas = document.querySelector('#snapshot'+type);
			            if (!patCanvas) return;
			            //alert(_video.width);
			            patCanvas.width = _video.width;
			            patCanvas.height = _video.height;
			            var ctxPat = patCanvas.getContext('2d');

			            var idata = getVideoData($scope.patOpts.x, $scope.patOpts.y, $scope.patOpts.w, $scope.patOpts.h);
			            ctxPat.putImageData(idata, 0, 0);

			            sendSnapshotToServer(patCanvas.toDataURL(),type);

			            patData = idata;
			        }
			    };
			    
			    $scope.makeSnapshotBack = function(type) {
			        if (_video) {
			        	
			            var patCanvas = document.querySelector('#snapshot'+type);
			            if (!patCanvas) return;
			            //alert(_video.width);
			            patCanvas.width = _video.width;
			            patCanvas.height = _video.height;
			            var ctxPat = patCanvas.getContext('2d');

			            var idata = getVideoData($scope.patOpts.x, $scope.patOpts.y, $scope.patOpts.w, $scope.patOpts.h);
			            ctxPat.putImageData(idata, 0, 0);

			            sendSnapshotToServer(patCanvas.toDataURL(),type);

			            patData = idata;
			        }
			    };
			    
			    /**
			     * Redirect the browser to the URL given.
			     * Used to download the image by passing a dataURL string
			     */
			    $scope.downloadSnapshot = function downloadSnapshot(dataURL) {
			        window.location.href = dataURL;
			    };
			    
			    var getVideoData = function getVideoData(x, y, w, h) {
			        var hiddenCanvas = document.createElement('canvas');
			        hiddenCanvas.width = _video.width;
			        hiddenCanvas.height = _video.height;
			        var ctx = hiddenCanvas.getContext('2d');
			        ctx.drawImage(_video, 0, 0, _video.width, _video.height);
			        return ctx.getImageData(x, y, w, h);
			    };

			    /**
			     * This function could be used to send the image data
			     * to a backend server that expects base64 encoded images.
			     *
			     * In this example, we simply store it in the scope for display.
			     */
			    var sendSnapshotToServer = function sendSnapshotToServer(imgBase64,type) {
			    	console.log("imgBase640"+imgBase64);
			        $scope.snapshotData = imgBase64;
			        
			        if(type==1){
			        	var img = '<img src="'+imgBase64+'" />';
			        	$scope.value= $scope.value.replace('#frontImage',img);
			        }
			        	if(type==2){
				        	var img = '<img src="'+imgBase64+'" />';
				        	$scope.value= $scope.value.replace('#backImage',img);
				        }
			        	if(type==3){
				        	var img = '<img src="'+imgBase64+'" />';
				        	$scope.value= $scope.value.replace('#kinfront',img);
				        }
			        	if(type==4){
				        	var img = '<img src="'+imgBase64+'" />';
				        	$scope.value= $scope.value.replace('#kinback',img);
				        }
			    };
				
				//for end camera
			}
			$scope.initMaster();
			
			$scope.resetContent = function(){
				
				var item = {};
				item.patientId = $scope.selectedForm.patientId;
				item.templateDesc = $scope.resetValue;
	        	$rootScope.startLoader();
        	    var URI = BASE_URL + ROOT_URL + "/api/opd/appendCensentData";
        	    GenericService.serviceAction(METHOD_POST,URI,item).then(function(response) {
	    			if (response.data.status == "success")
	    			{
	    				
	    				$rootScope.stopLoader();
	    				$scope.value  = response.data.object.templateDesc;
					}
	    			else if(response.data.status == "error")
    				{
	    				$rootScope.stopLoader();
    				}
					
	    		});
        	    
			}
			
			/*$scope.printTemplate = function()
			{
				var divContents = document.getElementById("page-content-wrapper").innerHTML;  
	            var printWindow = window.open('', '', 'height=500,width=700');  
	            printWindow.document.write('<html><head><title>FORM F</title>');  
	            printWindow.document.write('</head><body >');  
	            printWindow.document.write(divContents);  
	            printWindow.document.write('</body></html>');  
	            printWindow.document.close();  
	            printWindow.print();
				
			}*/
			
			
			$scope.printTemplate = function()
			{
				console.log("hh",$scope.value);
				 
					var templateSaveList = [];
						            var obj = angular.copy($scope.templateObj);
						            obj.consentTransactionId = $scope.selectedForm.consentTransactionId;
						            obj.consentMasterId =$scope.selectedForm.consentMasterId; 
						            obj.templateDesc =$scope.value//$scope.selectedForm.templateDesc;
						            templateSaveList.push(obj);
			        	
			         $rootScope.startLoader();
		        	    var URI = BASE_URL + ROOT_URL + "/adt/patientConsent";
		        	    GenericService.serviceAction(METHOD_POST,URI,templateSaveList).then(function(response) {
			    			if (response.data.status == "success")
			    			{
								$rootScope.stopLoader();
									
								
								    $scope.value= $scope.value.replace('#frontImage','');
						        	$scope.value= $scope.value.replace('#backImage','');
						        	$scope.value= $scope.value.replace('#kinfront','');
						        	$scope.value= $scope.value.replace('#kinback','');
						        	
						        	
									    var divContents = $scope.value;  
							            var printWindow = window.open('', '', 'height=500,width=700');  
							            printWindow.document.write('<html><head><title>FORM F</title>');  
							            printWindow.document.write('</head><body >');  
							            printWindow.document.write(divContents);  
							            printWindow.document.write('</body></html>');  
							            printWindow.document.close();  
							            printWindow.print();  
							            
									
									 /*var data =  
									  {
								            "appName": "HIS",
								            "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
								            "reportDesignFilePath": "getConsent.rpt",
								            "reportFormat": "PDF",
								            "exportFileName": "consentRpt",
								            "reportParams": [
								              {
									             "paramName": "admission_id",
									             "paramType": "Integer",
									             "paramValue": $scope.itemSelecteds.admissionId
								              },
								              {
										             "paramName": "consent_master_id",
										             "paramType": "Integer",
										             "paramValue": value.consentMasterId
									           },
									           {
										             "paramName": "consent_type_id",
										             "paramType": "Integer",
										             "paramValue": 1//admission consent
									           }
								            ]
								         };
									 reportService.generate(REPORT_API_URI1,data);*/
									
							}
			    			else if(response.data.status == "error")
			    				{
			    				$rootScope.stopLoader();
			    				}
			    		});
					
					 
				
				
			}
		
			
			
			
}]);