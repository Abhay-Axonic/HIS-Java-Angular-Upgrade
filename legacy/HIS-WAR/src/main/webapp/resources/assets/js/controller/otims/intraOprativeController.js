angular.module('myApp').controller(
				'intraOprativeController',
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
						'GenericServiceFileUpload',
						function($scope, $rootScope, $state, $http,
								GenericService,$sessionStorage,$cookies, growl, $localStorage,BillingGenericService,$timeout,$stateParams,GenericServiceFileUpload) {			
				
							var cookieObject = $cookies.getObject('cookieObject');
							if(cookieObject == undefined){
								$state.go('login');
								return;
							}
							$scope.offset = 0;
							$scope.noOfRecordsPerPage = 10;
							$scope.commonList = [];
							$scope.usersList=[];
							$scope.totalNoOfRecords;
							$scope.searchKeyword;
							$scope.commonListCount;
							$scope.procedureOrderId = "";
							$scope.unitId = cookieObject.unitId;
							$scope.organizationId = cookieObject.organizationId;
							$scope.organisationId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							$scope.intraWhoChecklistStatus = 2; //for pending
							$scope.snapCount = 1;
							$scope.canvasList = [];
							$scope.imageList = [];
							$scope.green = "#b4da80";
							$scope.cream = "#fffdd0";
							 var newData = {};
					            newData.id = "snapshot1";
					            $scope.canvasList.push(newData);
							$scope.intraObj = $stateParams.intraCommonObj;
							$scope.intraDetailsObj = $stateParams.intraDetailsObj;
							$scope.resultCommonObj = $stateParams.resultCommonObj;
							
							if($scope.intraObj.patientName == undefined){
								$state.go('OTIMSdashboard');
								return false;
							}
							
							$scope.patientDetails = {};
							$scope.patientDetails.patientName = $scope.intraObj.patientName;
							$scope.patientDetails.uhIdNumber = $scope.intraObj.uhidNumber;
							$scope.patientDetails.wardNameCover = $scope.intraObj.wardName;
							$scope.patientDetails.genderCode = $scope.intraObj.genderCode;
							$scope.patientDetails.age = $scope.intraObj.age;
							$scope.patientDetails.ageStr = $scope.intraObj.age;
							$scope.patientDetails.birthDate = $scope.intraObj.birthDate;
							$scope.patientDetails.admissionId = $scope.intraObj.admissionId;
							$scope.patientDetails.patientId = $scope.intraObj.patientId;
							$scope.patientDetails.visitTypeId = $scope.intraObj.visitTypeId;
							$scope.patientDetails.isTreatingDoctor = 'Y';
							$scope.patientDetails.genderId = $scope.intraObj.genderId;
							if($scope.intraObj.doa != null && $scope.intraObj.doa !=""){
							$scope.patientDetails.dateOfAdmission = $scope.intraObj.doa;
							}
							
							if($scope.intraObj.pdd != null && $scope.intraObj.pdd !=""){
							$scope.patientDetails.patientDischargeDate = $scope.intraObj.pdd;
							}
							
							
							$scope.patientDetails.los = $scope.intraObj.los
							$scope.patientDetails.bedNumber = $scope.intraObj.bedNumber;
							$scope.patientDetails.doctorFullName = $scope.intraObj.doctorName;
							
							
							
							
							console.log("$scope.intraObj",$scope.intraObj);
//							$scope.initClockPicker = function() {
//
//                                $('.clockpicker').clockpicker();
//                            }

							if($scope.intraObj.visitTypeId!=undefined && $scope.intraObj.visitTypeId!=1){
								var dateDoa = moment($scope.intraObj.doa, 'DD/MM/YYYY');
								var datePdd = moment($scope.intraObj.pdd, 'DD/MM/YYYY');
								$scope.intraObj.los = $rootScope.dateDiffrence(dateDoa,datePdd);
							} 
							
							console.log("$scope.intraDetailsObj",$scope.intraDetailsObj);
							
							
							//get intraoperative checklist by proReqId
						    //timesheet
						    $scope.timesheet = {
						    		wheelInTime : moment(moment($scope.intraDetailsObj.otCompletionTimeSheetDto.wheelInTime,'DD-MM-YYYY HH:mm:ss').toDate()).format('HH:mm'),
						    		tableOnTime : "",
						    		anesthesiaTime : "",
						    		procedureStartTime : "",
						    		procedureEndTime : "",
						    		tableOutTime : "",
						    		wheelOutTime : "",
						    		scheduleDischarge : "",
						    		actualDischarge : ""
						    }
							$scope.intraOperativeCheckList = [];
						    $scope.otCompletionTimeSheetDto ={};
							$scope.getIntraOperativeChecklist = function(){
								$scope.startLoader();
								 var data = {
										 proReqId : $scope.intraObj.proReqId,
								 }
								var URI = BASE_URL + ROOT_URL + "/api/otims/IntraOperativeDetails/getIntraOperativeByProReqId";
								GenericService.serviceAction(METHOD_POST, URI, data)
									.then(function(response) {
										if (response.data.status == "success") {
											$scope.intraOperativeCheckList = response.data.listObject;
											$scope.procedureOrderId  = response.data.object.procedureOrederId;
											if(response.data.object.otCompletionTimeSheetDto!=null){
												$scope.otCompletionTimeSheetDto = response.data.object.otCompletionTimeSheetDto;
												$scope.timesheet.wheelInTime = moment(moment($scope.otCompletionTimeSheetDto.wheelInTime,'DD-MM-YYYY HH:mm:ss').toDate()).format('HH:mm');
												$scope.timesheet.tableOnTime = moment(moment($scope.otCompletionTimeSheetDto.tableOnTime,'DD-MM-YYYY HH:mm:ss').toDate()).format('HH:mm')
												$scope.timesheet.anesthesiaTime = moment(moment($scope.otCompletionTimeSheetDto.anesthesiaTime,'DD-MM-YYYY HH:mm:ss').toDate()).format('HH:mm')
												$scope.timesheet.procedureStartTime = moment(moment($scope.otCompletionTimeSheetDto.procedureStartTime,'DD-MM-YYYY HH:mm:ss').toDate()).format('HH:mm')
												$scope.timesheet.procedureEndTime = moment(moment($scope.otCompletionTimeSheetDto.procedureEndTime,'DD-MM-YYYY HH:mm:ss').toDate()).format('HH:mm')
												$scope.timesheet.tableOutTime = moment(moment($scope.otCompletionTimeSheetDto.tableOutTime,'DD-MM-YYYY HH:mm:ss').toDate()).format('HH:mm')
												$scope.timesheet.wheelOutTime = moment(moment($scope.otCompletionTimeSheetDto.wheelOutTime,'DD-MM-YYYY HH:mm:ss').toDate()).format('HH:mm')
												$scope.timesheet.scheduleDischarge = moment(moment($scope.otCompletionTimeSheetDto.scheduleDischarge,'DD-MM-YYYY HH:mm:ss').toDate()).format('HH:mm')
												$scope.timesheet.actualDischarge = moment(moment($scope.otCompletionTimeSheetDto.actualDischarge,'DD-MM-YYYY HH:mm:ss').toDate()).format('HH:mm')
											}
											 $scope.intraDetailsObj.intraOperativeId = response.data.object.intraOperativeId != undefined ?  response.data.object.intraOperativeId : null ;
											 $scope.intraWhoChecklistStatus = response.data.object.whoChecklistStatus;
											 if($scope.intraOperativeCheckList!=null && $scope.intraOperativeCheckList!=undefined 
													&& $scope.intraOperativeCheckList.length>0){
														angular.forEach($scope.intraOperativeCheckList,function(value,index){
															if(value.whoStatusId == undefined || value.whoStatusId == null){
																value.whoStatusId = 2 ; //default pending
																$scope.intraWhoChecklistStatus = 2; //for pending
															}
														});
													}
										}else {
											var URI = BASE_URL + ROOT_URL + "/api/otims/dashboard/getPreOparetiveDeta";
											GenericService.serviceAction(METHOD_POST, URI, data)
												.then(function(response) {
													if (response.data.status == "success") {
														$scope.intraOperativeCheckList = response.data.listObject;
														if($scope.intraOperativeCheckList!=null && $scope.intraOperativeCheckList!=undefined 
														&& $scope.intraOperativeCheckList.length>0){
															angular.forEach($scope.intraOperativeCheckList,function(value,index){
																if(value.whoStatusId == null){
																	value.whoStatusId = 2 ; //default pending
																}
															});
														}
													}
													$scope.stopLoader();
												});
										}
										$scope.stopLoader();
									});
							}
							
							if($scope.intraObj.proReqId!=undefined && $scope.intraObj.proReqId!=null){
								$scope.getIntraOperativeChecklist();
							}
							
							
							//get intraoperative checklist by proReqId
							$scope.doctorList = [];
							$scope.getDoctorList = function(){
								$scope.startLoader();
								 var data = {
										 organizationId : $scope.organizationId,
										 unitId : $scope.unitId
								 }
								var URI = BASE_URL + ROOT_URL + "/api/otims/PreOperativeDetails/getActiveDoctorList";
								GenericService.serviceAction(METHOD_POST, URI, data)
									.then(function(response) {
										if (response.data.status == "success") {
											$scope.doctorList = response.data.listObject;
										}
										$scope.stopLoader();
									});
							}
							$scope.getDoctorList();
							
							
							
							//get aneasthesiaList
							$scope.aneasthesiaList = [];
							$scope.getaAneasthesiaList = function(){
								$scope.startLoader();
								 var data = {
										 organizationId : $scope.organizationId,
										 unitId : $scope.unitId
								 }
								var URI = BASE_URL + ROOT_URL + "/api/otims/PreOperativeDetails/getAnaesthesiaDoctorList";
								GenericService.serviceAction(METHOD_POST, URI, data)
									.then(function(response) {
										if (response.data.status == "success") {
											$scope.aneasthesiaList = response.data.listObject;
										}
										$scope.stopLoader();
									});
							}
							$scope.getaAneasthesiaList();
							
							
							//set Who checklist status
							$scope.checkWhoStatus = function(){
								var completeStatusCount = 0;
								var pendingStatusCount = 0;
								if($scope.intraOperativeCheckList != undefined && $scope.intraOperativeCheckList != null && $scope.intraOperativeCheckList.length != 0){
									angular.forEach($scope.intraOperativeCheckList,function(value,index){
										if(value.whoStatusId == 1){
											completeStatusCount = completeStatusCount +1;
										}else if(value.whoStatusId == 2){
											pendingStatusCount = pendingStatusCount + 1;
										}
										
									});
									
									if(completeStatusCount == $scope.intraOperativeCheckList.length){
										$scope.intraWhoChecklistStatus = 1;
									}else if(pendingStatusCount>0){
										$scope.intraWhoChecklistStatus = 2;
									}else{
										$scope.intraWhoChecklistStatus = 3;
									}
								}else{
									$scope.intraWhoChecklistStatus = 4;
								}
							}
							
							
							$scope.setColor = function(flag){
								if (flag == 1) {
									if($scope.intraDetailsObj.whoChecklistStatus == 2){
										return {
											"background-color" : "red"
										};
									}else if($scope.intraDetailsObj.whoChecklistStatus == 1){
										return {
											"background-color" : $scope.green
										};
									}else if($scope.intraDetailsObj.whoChecklistStatus == 3){
										return {
											"background-color" : "yellow"
										};
									}else{
										return {
											"background-color" : "#f0793a"
										};
									}
								}else if(flag == 2){
									if($scope.intraDetailsObj.patientConsentStatus == 2){
										return {
											"background-color" : "red"
										};
									}else if($scope.intraDetailsObj.patientConsentStatus == 1){
										return {
											"background-color" : $scope.green
										};
									}else if($scope.intraDetailsObj.patientConsentStatus == 3){
										return {
											"background-color" : "yellow"
										};
									}else{
										return {
											"background-color" : "#f0793a"
										};
									}
								}else if(flag == 8){
									
									 if($scope.timesheet.wheelInTime == undefined || $scope.timesheet.wheelInTime == "" ){
										return {
											"background-color" : "red"
										};
									}
									 else if($scope.timesheet.tableOnTime == undefined || $scope.timesheet.tableOnTime == "" ){
											return {
												"background-color" : "red"
											};
										}
									 else if( $scope.timesheet.anesthesiaTime == undefined || $scope.timesheet.anesthesiaTime == "" ){
											return {
												"background-color" : "red"
											};
										}
									 else if($scope.timesheet.procedureStartTime == undefined || $scope.timesheet.procedureStartTime == "" ){
											return {
												"background-color" : "red"
											};
										}
									 else if( $scope.timesheet.procedureEndTime == undefined || $scope.timesheet.procedureEndTime == ""){
											return {
												"background-color" : "red"
											};
										}
									 else if($scope.timesheet.tableOutTime == undefined || $scope.timesheet.tableOutTime == ""){
											return {
												"background-color" : "red"
											};
										}
									 else if($scope.timesheet.wheelOutTime == undefined  || $scope.timesheet.wheelOutTime == ""){
											return {
												"background-color" : "red"
											};
										}
									else if($scope.intraDetailsObj.patientIdentificationStatus == 2){
										return {
											"background-color" : "red"
										};
									}else if($scope.intraDetailsObj.patientIdentificationStatus == 1){
										return {
											"background-color" : $scope.green
										};
									}else if($scope.intraDetailsObj.patientIdentificationStatus == 3){
										return {
											"background-color" : "yellow"
										};
									}else{
										return {
											"background-color" : "#f0793a"
										};
									}
								}else if(flag == 3){
									
									
									 if($scope.intraDetailsObj.patientIdentificationStatus == 2){
										return {
											"background-color" : "red"
										};
									}else if($scope.intraDetailsObj.patientIdentificationStatus == 1){
										return {
											"background-color" : $scope.green
										};
									}else if($scope.intraDetailsObj.patientIdentificationStatus == 3){
										return {
											"background-color" : "yellow"
										};
									}else{
										return {
											"background-color" : "#f0793a"
										};
									}
								}
								else if(flag == 4){
									if($scope.intraOperativeCheckList == undefined || $scope.intraOperativeCheckList == null || $scope.intraOperativeCheckList.length == 0){
										return {
											"background-color" : $scope.cream
										};
									}else if($scope.intraWhoChecklistStatus == 1){
										return {
											"background-color" : $scope.green
										};
									}else if($scope.intraWhoChecklistStatus == 2){
										return {
											"background-color" : "red"
										};
									}else if($scope.intraWhoChecklistStatus == 3){
										return {
											"background-color" : "yellow"
										};
									}else if($scope.intraWhoChecklistStatus == 4){
										return {
											"background-color" : $scope.cream
										};
									}else{
										return {
											"background-color" : "#f0793a"
										};
									}
								}else if(flag == 5){
									if($scope.intraDetailsObj.bloodStatus == 1){
										return {
											"background-color" : $scope.green
										};
									}else if($scope.intraDetailsObj.bloodStatus != 1){
										return {
											"background-color" : $scope.cream
										};
									}else{
										return {
											"background-color" : "#f0793a"
										};
									}
								}
								
								}
							
							//Equipments
							
							$scope.equipmentTypeObj = {
									id:"",
									category:""
							}
							
							$scope.equipmentNameObj = {
									id:"",
									name:""
							}
							
							$scope.extraEquipmentList = [];
							
							$scope.equipmentTypeList = [];
							$scope.getEquipmentTypeList = function () {
								var URI = BASE_URL + ROOT_URL + "/api/global/assets/categories/2/"+$scope.unitId+"/"+$scope.organizationId; //2 for Fixed asset type
								BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
									$scope.equipmentTypeList = response.data;
								});
							   }
							
							$scope.getEquipmentTypeList();
							
							$scope.equipmentNameList = [];
							$scope.getEquipmentNamesByEquipmentId = function (equipmentTypeId) {
								$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL + "/api/unit/fixed/asset/getAssetNameListByAssetCategoryId/"+equipmentTypeId; 
								BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
									$scope.equipmentNameList = response.data;
									$rootScope.stopLoader();	
								});
							   }
							
							
							$scope.addEquipmentDetails = function(){
								try{
									$rootScope.startLoader();
									if($scope.equipmentTypeObj.id==""){
										$rootScope.stopLoader();
										growl.warning("Please select Equipment Type",
												{
													title : 'warning!'
												});
									}else if($scope.equipmentNameObj.id==""){
										$rootScope.stopLoader();
										growl.warning("Please Select Equipment Name",
												{
													title : 'warning!'
												});
									}else{
									var equpStatusFlag = false;
									var newArr = {};
									newArr.procedureOrderEquipmentId = null;
									newArr.equipmentTypeId = $scope.equipmentTypeObj.id;
									newArr.equipmentTypeName = $scope.equipmentTypeObj.category;
									newArr.itemId = $scope.equipmentNameObj.id;
									newArr.itemName =$scope.equipmentNameObj.name;
									newArr.organizationId=$scope.organizationId;
									newArr.unitId=$scope.unitId;
									newArr.createdBy=$scope.userId;
									newArr.createdDate=moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
									newArr.updatedBy=$scope.userId;
									newArr.updatedDate=moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
									newArr.qty = 1;
									newArr.status="A",
									$scope.extraEquipmentList.push(newArr);
								
									
									$scope.equipmentTypeObj.id = "";
									$scope.equipmentTypeObj.category = "";
									$scope.equipmentNameObj.id = "";
									$scope.equipmentNameObj.name = "";
									$rootScope.stopLoader();
									}
								}catch(e){
									$rootScope.stopLoader();
									console.log("Exception",e);
								}
							}
							
							$scope.removeEquipment = function(index){
										$scope.extraEquipmentList.splice(index,1);
							}
							
							
							///webcam
							//$scope.videoSource = $sce.trustAsResourceUrl("//www.youtube.com/embed/9B7te184ZpQ?rel=0");
							
							 var _video = null;

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
							        _video.width = 117;
							        _video.height = 100;
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
							      
							        /*	
							            var patCanvas = document.querySelector('#snapshot'+type);
							            if (!patCanvas) return;
							            //alert(_video.width);
							            patCanvas.width = _video.width;
							            patCanvas.height = _video.height;
							            var ctxPat = patCanvas.getContext('2d');

							            var idata = getVideoData($scope.patOpts.x, $scope.patOpts.y, $scope.patOpts.w, $scope.patOpts.h);
							            ctxPat.putImageData(idata, 0, 0);
							            sendSnapshotToServer(patCanvas.toDataURL());
							            patData = idata;
							            $scope.snapCount = $scope.snapCount + 1;
							            var newData = {};
							            newData.id = "snapshot"+$scope.snapCount;
							            $scope.canvasList.push(newData);
							            */

							        	
							    		var canvas = document.getElementById('snapshot'+type);
										var video = document.getElementById('cameraPreview');
										canvas.width = video.videoWidth;
										canvas.height = video.videoHeight;
										canvas.getContext('2d').drawImage(video, 0, 0, video.videoWidth, video.videoHeight); // for drawing the video element on the canvas
										 
								            var ctxPat = canvas.getContext('2d');

								            var idata = getVideoData(0,0,video.videoWidth, video.videoHeight);
								            ctxPat.putImageData(idata, 0, 0);
								            sendSnapshotToServer(canvas.toDataURL());
								            patData = idata;
								            $scope.snapCount = $scope.snapCount + 1;
								            var newData = {};
								            newData.id = "snapshot"+$scope.snapCount
								            $scope.canvasList.push(newData);
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
							        var video = document.getElementById('cameraPreview');
							        hiddenCanvas.width = video.width;
							        hiddenCanvas.height = video.height;
							        var ctx = hiddenCanvas.getContext('2d');
							        ctx.drawImage(video, 0, 0, video.width, video.height);
							        return ctx.getImageData(x, y, w, h);
							    };

							    /**
							     * This function could be used to send the image data
							     * to a backend server that expects base64 encoded images.
							     *
							     * In this example, we simply store it in the scope for display.
							    
							     */
							    $scope.convertBase64ToImage = function (dataurl, filename) {
								    var arr = dataurl.split(','), mime = arr[0].match(/:(.*?);/)[1],
								        bstr = atob(arr[1]), n = bstr.length, u8arr = new Uint8Array(n);
								    while(n--){
								        u8arr[n] = bstr.charCodeAt(n);
								    }
								    return new File([u8arr], filename, {type:mime});
								}
							    
							    var sendSnapshotToServer = function sendSnapshotToServer(imgBase64) {
							        $scope.snapshotData = imgBase64;
							        var newData = {};
							        newData.image = $scope.convertBase64ToImage($scope.snapshotData,moment().toDate().getTime()+".jpeg");
							        $scope.imageList.push(newData);
							    };
							    
							    
							
							
							
							 var ROI_X = 150;
							 var ROI_Y = 100;
							 var ROI_WIDTH = 350;
							 var ROI_HEIGHT = 250;
							    
							 var FPS = 25;
							    
							 var cameraStream = null;
							 var processingStream = null;
							 var mediaRecorder = null;
							 var mediaChunks = null;
							 var processingPreviewIntervalId = null;

							    $scope.processFrame = function() {
							        var cameraPreview = document.getElementById("cameraPreview");
							    }
							    
							    $scope.blobToFile = function(theBlob, fileName){
							        //A Blob() is almost a File() - it's just missing the two properties below which we will add
							        theBlob.lastModifiedDate = new Date();
							        theBlob.name = fileName;
							        return theBlob;
							    }
							    $scope.newVideoForUpload = {};
							    var generateRecordingPreview = function() {
							        var mediaBlob = new Blob(mediaChunks, { type: "video/webm" });
							        var mediaBlobUrl = URL.createObjectURL(mediaBlob);
							    /*    
							        let recordingPreview = document.getElementById("recordingPreview");
							        recordingPreview.src = mediaBlobUrl;
*/
							        var downloadButton = document.getElementById("downloadButton");
							        downloadButton.href = mediaBlobUrl;
							        downloadButton.download = "RecordedVideo.webm";
							        
							        $scope.newVideoForUpload = {};
							        $scope.newVideoForUpload.video = $scope.blobToFile(mediaBlob,moment().toDate().getTime()+".webm");
							    }
							    
							        
							    $scope.startCapture = function() {
							        var constraints = { video: true, audio: true };
							        navigator.mediaDevices.getUserMedia(constraints).then((stream) => {
							            cameraStream = stream;
							            
							            var cameraPreview = document.getElementById("cameraPreview");
							            cameraPreview.srcObject = stream;
							            var options = {
							            		audioBitsPerSecond: 128000,
							            		videoBitsPerSecond: 2500000,
							            		mimeType: 'video/webm;codecs=vp9'
							            		}
							            
							            mediaRecorder = new MediaRecorder(stream,options);
							            
							            
							           // mediaRecorder = new MediaRecorder(stream,{mimeType: 'video/webm;codecs=vp9', bitsPerSecond: 100000}); old
							            mediaChunks = []
							            
							            mediaRecorder.ondataavailable = function(event) {
							                mediaChunks.push(event.data);
							                if(mediaRecorder.state == "inactive") {
							                    generateRecordingPreview();
							                }
							            };
							            
							            mediaRecorder.start();
							            
							            
							            processingPreviewIntervalId = setInterval($scope.processFrame(), 1000 / FPS);
							        })
							        .catch((err) => {
							            alert("No media device found!");
							            console.log(err);
							        });
							    };
							    
							    $scope.stopCapture = function() {
							        if(cameraStream != null) {
							            cameraStream.getTracks().forEach(function(track) {
							                track.stop();
							            });
							        }
							        
							        if(processingStream != null) {
							            processingStream.getTracks().forEach(function(track) {
							                track.stop();
							            });
							        }
							        
							        if(mediaRecorder != null) {
							            if(mediaRecorder.state == "recording") {
							                mediaRecorder.stop();
							            }
							        }
							        
							        if(processingPreviewIntervalId != null) {
							            clearInterval(processingPreviewIntervalId);
							            processingPreviewIntervalId = null;
							        }
							    };
								
								
									/*$scope.capture = function () {
										var canvas = document.getElementById('canvas');
										var video = document.getElementById('cameraPreview');
										canvas.width = video.videoWidth;
										canvas.height = video.videoHeight;
										canvas.getContext('2d').drawImage(video, 0, 0, video.videoWidth, video.videoHeight); // for drawing the video element on the canvas
										 
										*//** Code to merge image **//*
										*//** For instance, if I want to merge a play image on center of existing image **//*
										var playImage = new Image();
										playImage.src = 'path to image asset';
										playImage.onload = () => {
										var startX = (video.videoWidth / 2) - (playImage.width / 2);
										var startY = (video.videoHeight / 2) - (playImage.height / 2);
										canvas.getContext('2d').drawImage(playImage, startX, startY, playImage.width, playImage.height);
										canvas.toBlob() = (blob) => {
												var img  = window.URL.createObjectUrl(blob);
											};
										};
										}
							
							*/
							
							
							
							    // Notify Doctor 
							    $scope.notifyToDoctor = function(info){
									$scope.startLoader();
									 var data = {
											 doctorName : info.doctorName,
											 email : "nitin@clinivantage.com",//info.email,
											 mobileNumber : info.mobileNumber,
											 preferedDateTime : $scope.intraObj.preferedDateTime,
											 procedureName : $scope.intraObj.procedureName,
											 theatreName : $scope.intraObj.theatreName
											 
									 }
									var URI = BASE_URL + ROOT_URL + "/api/otims/PreOperativeDetails/sendNotificationToDoctor";
									GenericService.serviceAction(METHOD_POST, URI, data)
										.then(function(response) {
											$scope.stopLoader();
											if (response.data.status == "success") {
											}
											
										});
								}
								
							    //image upload
							    $scope.uploadImage = function(Obj)
							    {
							    	var fd = new FormData();
							    	fd.append('createdBy',Obj.createdBy );
							    	fd.append('unitId', Obj.unitId);
							    	fd.append('organizationId', Obj.organizationId);
							    	fd.append('intraOperativeId',Obj.intraOperativeId);
							    	fd.append('file',Obj.file);
							    	fd.append('createdDate', moment(new Date()).format('DD-MM-YYYY HH:mm:ss'));
							    	var URI = BASE_URL + ROOT_URL + "/api/otims/IntraOperativeDetails/saveIntraImage";
							    	return GenericServiceFileUpload.serviceAction("POST",URI,fd,{ 'Content-Type': undefined}).then(function(response)
							    			{
							    		return response;
							    			});
							    }
						    	 
							    $scope.uploadVideo = function(Obj)
							    {
							    	var fd = new FormData();
							    	fd.append('createdBy',Obj.createdBy );
							    	fd.append('unitId', Obj.unitId);
							    	fd.append('organizationId', Obj.organizationId);
							    	fd.append('intraOperativeId',Obj.intraOperativeId);
							    	fd.append('file',Obj.videoFile);
							    	fd.append('createdDate', moment(new Date()).format('DD-MM-YYYY HH:mm:ss'));
							    	var URI = BASE_URL + ROOT_URL + "/api/otims/IntraOperativeDetails/saveIntraVideo";
							    	return GenericServiceFileUpload.serviceAction("POST",URI,fd,{ 'Content-Type': undefined}).then(function(response)
							    			{
							    		return response;
							    			});
							    }
							    
							    //save Intraoperative 
							    $scope.detailsObj = {};
							    $scope.saveIntraOperativeDetails = function(){
							    	$scope.startLoader();
							    	if($scope.intraWhoChecklistStatus == 2){
										growl.error("Please Complete/Override All Who Checklist Status",
												{
													title : 'error'
												});
										$scope.stopLoader();
										return false;
									}
							    	 $scope.detailsObj = {};
							    	  if($scope.timesheet.wheelInTime == "" || $scope.timesheet.wheelInTime == undefined ){
						            	  $rootScope.stopLoader();
						            	  growl.warning("Please Enter WheelInTime ", {
					                      title: "Warning"
						            	  });
					                  return false
						             }
						             
						             if($scope.timesheet.tableOnTime == "" || $scope.timesheet.tableOnTime == undefined){
						            	  $rootScope.stopLoader();
						            	  growl.warning("Please Enter tableOnTime ", {
					                     title: "Warning"
						            	  });
					                 return false
						             }
						             
						             if($scope.timesheet.anesthesiaTime == "" || $scope.timesheet.anesthesiaTime == undefined){
						            	  $rootScope.stopLoader();
						            	  growl.warning("Please Enter anesthesiaTime ", {
					                     title: "Warning"
						            	  });
					                 return false
						             }
						             
						             if($scope.timesheet.procedureStartTime == "" || $scope.timesheet.procedureStartTime == undefined){
						            	  $rootScope.stopLoader();
						            	  growl.warning("Please Enter procedureStartTime ", {
					                     title: "Warning"
						            	  });
					                 return false
						             }
						             
						             if($scope.timesheet.procedureEndTime == "" || $scope.timesheet.procedureEndTime == undefined ){
						            	  $rootScope.stopLoader();
						            	  growl.warning("Please Enter procedureEndTime ", {
					                     title: "Warning"
						            	  });
					                 return false
						             }
						             
						             if($scope.timesheet.tableOutTime == "" || $scope.timesheet.tableOutTime == undefined){
						            	  $rootScope.stopLoader();
						            	  growl.warning("Please Enter tableOutTime ", {
					                     title: "Warning"
						            	  });
					                 return false
						             }
						             
						             if($scope.timesheet.wheelOutTime == "" || $scope.timesheet.wheelOutTime == undefined){
						            	  $rootScope.stopLoader();
						            	  growl.warning("Please Enter wheelOutTime ", {
					                     title: "Warning"
						            	  });
					                 return false
						             }
						             
						           /*  if($scope.timesheet.scheduleDischarge == "" || $scope.timesheet.scheduleDischarge == undefined){
						            	  $rootScope.stopLoader();
						            	  growl.warning("Please Enter scheduleDischarge ", {
					                    title: "Warning"
						            	  });
					                return false
						             }*/
						             
//						             if($scope.timesheet.actualDischarge == "" || $scope.timesheet.actualDischarge == undefined){
//						            	  $rootScope.stopLoader();
//						            	  growl.warning("Please Enter actualDischarge ", {
//					                    title: "Warning"
//						            	  });
//					                return false
//						             }
							    	 
							    	 
							    	//timesheet obj
									var timeSheetDto = {
										otCompletionTimeSheetId : $scope.intraDetailsObj.otCompletionTimeSheetDto.otCompletionTimeSheetId,	
										orgId : $scope.organizationId,
										unitId : $scope.unitId,
										proReqId : $scope.intraDetailsObj.otCompletionTimeSheetDto.proReqId,
										status: 'A',
										createdBy : $scope.userId,
										updatedBy : $scope.userId,
										updatedDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
										createdDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
									//	wheelInTime : $scope.intraDetailsObj.otCompletionTimeSheetDto.wheelInTime,
										procedureStartTime : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
										wheelInTime : "",
										tableOnTime : "",
										anesthesiaTime : "",
							    		procedureStartTime : "",
							    		procedureEndTime : "",
							    		tableOutTime : "",
							    		wheelOutTime : "",
							    		scheduleDischarge : "",
							    		actualDischarge : ""
									};
									
									 timeSheetDto.wheelInTime = moment(moment(moment($scope.intraDetailsObj.otCompletionTimeSheetDto.wheelInTime,'DD-MM-YYYY HH:mm:ss').toDate())).format('DD-MM-YYYY').concat(" ",$scope.timesheet.wheelInTime.concat(":00"));
							    	 timeSheetDto.tableOnTime = moment(new Date()).format('DD-MM-YYYY').concat(" ",$scope.timesheet.tableOnTime.concat(":00"));
							    	 timeSheetDto.anesthesiaTime = moment(new Date()).format('DD-MM-YYYY').concat(" ",$scope.timesheet.anesthesiaTime.concat(":00"));
							    	 timeSheetDto.procedureStartTime = moment(new Date()).format('DD-MM-YYYY').concat(" ",$scope.timesheet.procedureStartTime.concat(":00"));
							    	 timeSheetDto.procedureEndTime = moment(new Date()).format('DD-MM-YYYY').concat(" ",$scope.timesheet.procedureEndTime.concat(":00"));
							    	 timeSheetDto.tableOutTime = moment(new Date()).format('DD-MM-YYYY').concat(" ",$scope.timesheet.tableOutTime.concat(":00"));
							    	 timeSheetDto.wheelOutTime = moment(new Date()).format('DD-MM-YYYY').concat(" ",$scope.timesheet.wheelOutTime.concat(":00"));
							    	 timeSheetDto.scheduleDischarge = moment(new Date()).format('DD-MM-YYYY').concat(" ",$scope.timesheet.scheduleDischarge.concat(":00"));
//							    	 timeSheetDto.actualDischarge = moment(new Date()).format('DD-MM-YYYY').concat(" ",$scope.timesheet.actualDischarge.concat(":00"));
								
									//who checklist
									$scope.whoChecklistDtoList = [];
									if($scope.intraOperativeCheckList != undefined && $scope.intraOperativeCheckList != null && $scope.intraOperativeCheckList.length != 0){
										angular.forEach($scope.intraOperativeCheckList,function(value,index){
										
											var newArr = {
													whoChecklistId : (value.whoChecklistId!=undefined && value.whoChecklistId!=null )? value.whoChecklistId : null,
													preOperativeId : null,
													checklistMasterId: value.checklistMasterId,
													remark: value.remark ,
													whoStatusId : value.whoStatusId,
													status: 'A',
													intraOperative: (value.intraOperative!=undefined && value.intraOperative!=null )? value.intraOperative : null,
													organizationId:$scope.organizationId,
													unitId : $scope.unitId,
													createdBy : $scope.userId,
													updatedBy : $scope.userId,
													updatedDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
													createdDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss')
											};
											$scope.whoChecklistDtoList.push(newArr);
										});
									}
									$scope.equipmentList = [];
									if($scope.extraEquipmentList!=null && $scope.extraEquipmentList.length>0){
										angular.forEach($scope.extraEquipmentList,function(value,index){
											var newArr = {};
											newArr.procedureOrderEquipmentId =	value.procedureOrderEquipmentId!=null ?value.procedureOrderEquipmentId : null,
											newArr.qty = value.qty,
											newArr.itemId = value.itemId,
											newArr.status = "A",
											newArr.organizationId = $scope.organizationId,
											newArr.unitId = $scope.unitId,
											newArr.createdBy = $scope.userId,
											newArr.updatedBy = $scope.userId,
											newArr.updatedDate = moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
											newArr.createdDate = moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
											$scope.equipmentList.push(newArr);
										});
									}
									
									 var data = {
											 intraOperativeId : $scope.intraDetailsObj.intraOperativeId!=null ?$scope.intraDetailsObj.intraOperativeId : null,
											 proReqId	: $scope.intraObj.proReqId,
											 videoUrl : "",
											 procedureOrderId : $scope.procedureOrderId,
											 whoChecklistStatus : $scope.intraWhoChecklistStatus,
											 status : "A",
											 organizationId:$scope.organizationId,
											 unitId : $scope.unitId,
											 createdBy : $scope.userId,
											 updatedBy : $scope.userId,
											 updatedDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
											 createdDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
											 whoChecklistDtoList : $scope.whoChecklistDtoList ,
											 otCompletionTimeSheetDto : timeSheetDto ,
											 procedureOrderEquipmentList : $scope.equipmentList ,
									 };
									 console.log("save Data",data);
									var URI = BASE_URL + ROOT_URL + "/api/otims/IntraOperativeDetails/saveIntraOperativeDetails";
									GenericService.serviceAction(METHOD_POST, URI, data)
										.then(function(response) {
											$scope.stopLoader();
											if (response.data.status == "success") {
												if($scope.imageList!=undefined && $scope.imageList.length>0){
													angular.forEach($scope.imageList,function(value,index){
														var obj = {};
														obj.intraOperativeId = response.data.object.intraOperativeId;
														obj.createdBy  = $scope.userId;
														obj.unitId = $scope.unitId;
														obj.organizationId = $scope.organizationId;
														obj.file = value.image;
														$scope.uploadImage(obj);
													});
												}
												
												if($scope.newVideoForUpload.video!=undefined && $scope.newVideoForUpload.video!=null){
													var obj = {};
													obj.intraOperativeId = response.data.object.intraOperativeId;
													obj.createdBy  = $scope.userId;
													obj.unitId = $scope.unitId;
													obj.organizationId = $scope.organizationId;
													obj.videoFile = $scope.newVideoForUpload.video;
													$scope.uploadVideo(obj);
												}
												$scope.detailsObj = data;
												$timeout(function () {
													$scope.goToPost();
													}, 4000);
											}
											
										});
							    }
							    
							    $scope.goToPost = function(){
										 $state.go('post',
													{
											         intraCommonObj : $stateParams.intraCommonObj,
											         intraDetailsObj : $stateParams.intraDetailsObj,
												     resultCommonObj : $scope.resultCommonObj ,
											 		 postCommonObj: $scope.intraObj,
											 		 postDetailsObj : $scope.detailsObj
													});
							    }
							    
							   
							    
							    
							    
							    
							    
							    
							    // post 
							    $scope.encounterDetailsContractList = [];
								   $scope.finalDocUploadList=[];
								   $scope.allDocumentFinalList=[];
							    
							    
							    

								$scope.initCommon = function()
											{
												$scope.saveBtnFlag = true; 
												$scope.updateBtnFlag = false;
												$scope.incidentTypeId = 1;
												$scope.incidentReportingDate=new Date();
												$scope.incidentReportingDateTime='',
												$scope.specialityId='';
												$scope.incidentCategoryId='';
												$scope.incidentReportId='';
												$scope.reportingTemplateId='';
												$scope.incidentRemark='';
												$scope.roleId='';
												$scope.incidentUserId=$scope.userId;
												$scope.patientId=$scope.intraObj.patientId,
												$scope.incidentReportingMapperId = null;
												$scope.patientSearchObj = {
														"uhIdNumber"      : "",
														"patientName"     : "",
														"genderId"        : "",
														"mobileNo"        : "",
														"birthDate"       : "",
														"identificationNo":"",
														"patientType"	  :"1",
														"unitId"          :$scope.unitId,
														"organizationId"  :$scope.organizationId
												}
												
												$scope.getActiveIncidentReport = function(id){
														
														var URI = BASE_URL + ROOT_URL + "/api/unit/getIncidentReportMasterList";
														offset =  0;
														noOfRecordsPerPage = 10;
														var data1 = {
												  			      organizationId:$scope.organisationId,
												  			      unitId:$scope.unitId,
												  			      offset:offset,
												  			      noOfRecordsPerPage:noOfRecordsPerPage
												  		};
														GenericService.serviceAction("POST",URI,data1).then(function(response){
															if(response.data.status == "success"){
															$rootScope.stopLoader();					
															$scope.getActiveIncidentReportList = [];
															angular.forEach(response.data.listObject , function(value,key){
																if(value.status == 'A'){
																	$scope.getActiveIncidentReportList.push(value);
																}
															});
															}
														});
														}
												$scope.getActiveIncidentReport();
												
												
											}
								$scope.initCommon();
								
								$scope.obj2 = {};
							
								/**********from date******************/
								
								$scope.openDatePickerDate = function() {
									$scope.datepickerDate.opened = true;
								};

								$scope.datepickerDate = {
									opened : false
								};
								
								$scope.dateOptionDate = {
										formatYear : 'yyyy',
										showWeeks : false,
										maxDate : new Date(),
									};
								
								
								// function for initializa clockpicker
//								$scope.initClockPicker = function() {
//
//									$('.clockpicker').clockpicker();
//								}

							$scope.timesheet.wheelInTime = $scope.timesheet.wheelInTime || '';
                            $scope.timesheet.tableOnTime = $scope.timesheet.tableOnTime || '';
                            $scope.timesheet.anesthesiaTime = $scope.timesheet.anesthesiaTime || '';
                            $scope.timesheet.procedureStartTime = $scope.timesheet.procedureStartTime || '';
                            $scope.timesheet.procedureEndTime = $scope.timesheet.procedureEndTime || '';
                            $scope.timesheet.tableOutTime = $scope.timesheet.tableOutTime || '';
                            $scope.timesheet.wheelOutTime = $scope.timesheet.wheelOutTime || '';
                            var clockInitialized = false;

                            $scope.initClockPicker = function () {
                                setTimeout(function () {
                                    var $input = $('.clockpicker input');

                                    if ($input.data('clockpicker')) return;

                                    $input.clockpicker({
                                        autoclose: true,   // commit happens here
                                        appendTo: 'body'
                                    });

                                    // Update Angular when value changes
                                    $input.on('change', function () {
                                        var value = this.value;
                                        var clockName = this.name;
                                        $scope.$applyAsync(function () {
                                            if(clockName!== undefined && clockName !== null && clockName === 'wheelInTime'){
                                                $scope.timesheet.wheelInTime = value;
                                            }
                                            else if(clockName!== undefined && clockName !== null && clockName === 'tableOnTime'){
                                                $scope.timesheet.tableOnTime = value;
                                            }
                                            else if(clockName!== undefined && clockName !== null && clockName === 'anesthesiaTime'){
                                                $scope.timesheet.anesthesiaTime = value;
                                            }
                                            else if(clockName!== undefined && clockName !== null && clockName === 'procedureStartTime'){
                                                $scope.timesheet.procedureStartTime = value;
                                            }
                                            else if(clockName!== undefined && clockName !== null && clockName === 'procedureEndTime'){
                                                $scope.timesheet.procedureEndTime = value;
                                            }
                                            else if(clockName!== undefined && clockName !== null && clockName === 'tableOutTime'){
                                                $scope.timesheet.tableOutTime = value;
                                            }
                                            else if(clockName!== undefined && clockName !== null && clockName === 'wheelOutTime'){
                                                $scope.timesheet.wheelOutTime = value;
                                            }
                                        });
                                    });

                                }, 0);
                            };

                            $scope.initClockPicker();
									
								$scope.specialityMasterList=[];
								//------------ Get Speciality Master List ------------//
								$scope.getSpecialityMasterList = function(){
									try{
										var URI = BASE_URL + ROOT_URL + GETACTIVESPECIALITY;
										var obj = {
													organizationId : $scope.organizationId,
													unitId         :$scope.unitId
											    }
										BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
											console.log(response);
											if(response.data.status == "success"){
												$scope.specialityMasterList = response.data.listObject;
											}
										});
									}catch (e) {
										console.log("Exception",e.message);
									}
								}
								$scope.getSpecialityMasterList();
								
							
								
								
								//------------ Get 	Active Category Maste List ------------//
								$scope.getActiveCategoryMaster = function(){
									try{
										var URI = BASE_URL + ROOT_URL + "/api/global/unit/getActiveIncidentCategoryMaster/" +$scope.organizationId + "/"+ $scope.unitId;
										var obj = {};
										BillingGenericService.serviceAction(METHOD_GET,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
											console.log(response);
											if(response.data.status == "success"){
												$scope.incidentCategoryMasterList = [];
												$scope.incidentCategoryMasterList = response.data.listObject;
											}
										});
									}catch (e) {
										console.log("Exception",e.message);
									}
								}
								$scope.getActiveCategoryMaster();
								
									$scope.getUserList = function(id){
										
										var URI = BASE_URL + ROOT_URL + "/api/global/getActiveUserRoleMasterList";
										var data = {
												organisationId:$scope.organizationId,
											      unitId:$scope.unitId
										}
										GenericService.serviceAction("POST",URI,data).then(function(response){
											if(response.data.status == "success"){
											$rootScope.stopLoader();					
											$scope.getActiveUserList = [];
											angular.forEach(response.data.listObject,
															function(value,index) {
																var data = {};
																data.id = value.roleId;
																data.label = value.roleName;
																$scope.getActiveUserList.push(data);
															});
											console.log("getActiveRoleList",$scope.getActiveRoleList);
											}
										});
										}

								
									
								
								$scope.getRoleMappedToIncidentReport = function(info){
									$scope.getActiveRoleList = [];
									
									$scope.incidentReportId = info.incidentReportId;
									//get role mapped
									angular.forEach(info.incidentReportRoleMapperList,function(value,index){
										var neeArr = {};
										neeArr.id = value.roleId;
										neeArr.label = value.roleName;
										$scope.getActiveRoleList.push(neeArr);
									
									});
									//get template mapped
									$scope.incidentReportTemplateMapperList=[];
									$scope.incidentReportTemplateMapperList=angular.copy(info.incidentReportTemplateMapperList);
								}
								
								//Role wise user
								$scope.getRolewiseUsers = function(id){
									$scope.getRoleWiseUsersList = [];
									$scope.roleList = [];
									$scope.roleList.push(id);
									var URI = BASE_URL + ROOT_URL + "/api/unit/getRoleWiseUserList";
									offset =  0;
									noOfRecordsPerPage = 10;
									var data1 = {
							  			      organizationId:$scope.organisationId,
							  			      unitId:$scope.unitId,
							  			      roleIdList:$scope.roleList
							  			      
							  		};
									GenericService.serviceAction("POST",URI,data1).then(function(response){
										if(response.data.status == "success"){
										$rootScope.stopLoader();
										$scope.getRoleWiseUsersList=response.data.listObject;
										console.log("$scope.getRoleWiseUsersList", $scope.getRoleWiseUsersList);
										}
									});
									}
								
								
								$scope.templateFormatId=2;
								$scope.value ="";
								$scope.addTemplate = function(reportingTemplateId) {
									if(reportingTemplateId){
										$scope.reportingTemplateId = reportingTemplateId;
										angular.forEach($scope.incidentReportTemplateMapperList , function(value,key){
											if(value.templateId == reportingTemplateId){
												$scope.template = {};
												$scope.template = value;
											}
										});
										$scope.value ="";
										$scope.templateFormatId =$scope.template.templateFormatId;
										if($scope.template.templateFormatId==1)
											{
											
										     	$scope.obj = {};
												$scope.template.templateObject = JSON.parse($scope.template.templateObject);
												$scope.obj = $scope.template.templateObject.obj;
												var replacedHtml = $scope.template.templateObject.html.replace(/ng-model="/g,'ng-model="obj.');
												document.getElementById("htmlContent").innerHTML = replacedHtml;
												
												
//											document.getElementById("htmlContent").innerHTML = JSON.parse($scope.template.templateObject).html;
											}
										else if($scope.template.templateFormatId==2)
											{
											 $scope.value = $scope.template.templateObject;
											}
									}
								}
								$scope.filepreview = "";
								$scope.fileObj={
										csvfile:'',
								}
							    
							    
								//docuument Upload code
								$scope.uploadEncounterDoc = function(Obj)
								{
									 var fd = new FormData();
								        fd.append('encounterId', 0);
								        fd.append('visitTypeId', 1);
								        fd.append('admissionId', 0);
								        fd.append('patientId',$scope.patientId);
								        fd.append('documentTypeId',1);
								        fd.append('createdBy',$scope.userId);
								        fd.append('updatedBy',$scope.userId);
								        fd.append('unitId', $scope.unitId);
								        fd.append('organisationId', $scope.organisationId);
								        fd.append('file',$scope.csvfile);
								        fd.append('expenseId',0);
								        fd.append('grnId',0);
										fd.append('poId',0);
								        fd.append('incidentReportingMapperId',Obj.incidentReportingMapperId);
								        fd.append('companyExpenseId',0);
								        fd.append('createdDate',null);
										fd.append('fileEditedName',null);
										fd.append('opbId',0);
										fd.append('vendorId',0);
								        var URI1 = BASE_URL + ROOT_URL + UPLOAD_ENCOUNTER_DOCUMENT;
								        return GenericServiceFileUpload.serviceAction("POST",URI1,fd,{ 'Content-Type': undefined}).then(function(response) 
										{	
											return response;
										});
								}
								
								$scope.saveIncidentReporting = function(){
										try {
											$rootScope.startLoader();
											var date = new Date();
											$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
											
											
											if ($scope.incidentReportingDate == undefined) {
												growl.error("Please Select Date ", {
													title : 'Warning!'
												});
												$rootScope.stopLoader();
												return false;
											}
											if ($scope.incidentReportingDateTime == undefined
													|| $scope.incidentReportingDateTime == "") {
												growl.error("Please Enter Time", {
													title : 'Warning!'
												});
												$rootScope.stopLoader();
												return false;
											}
											if ($scope.roleId == undefined
													|| $scope.roleId == "") {
												growl.error("Please Select Role", {
													title : 'Warning!'
												});
												$rootScope.stopLoader();
												return false;
											}

											if ($scope.incidentUserId == undefined
													|| $scope.incidentUserId == "") {
												growl.error("Please Select User", {
													title : 'Warning!'
												});
												$rootScope.stopLoader();
												return false;
											}
											if ($scope.reportingTemplateId == undefined
													|| $scope.reportingTemplateId == "") {
												growl.error("Please Select Template", {
													title : 'Warning!'
												});
												$rootScope.stopLoader();
												return false;
											}
											
											if($scope.templateFormatId == 1){
											  $scope.dataObj=document.getElementById("htmlContent").innerHTML;
												console.log("sdfsdfdsd",$scope.dataObj);
											}else{
												 $scope.dataObj=$scope.value;
													console.log("sdfsdfdsd",$scope.dataObj);
											}
											
											
											var data =
											{
												 "incidentReportingMapperId":$scope.incidentReportingMapperId,
												 "incidentReportingDate": moment($scope.incidentReportingDate).format('DD-MM-YYYY HH:mm:ss'),
											     "incidentReportingTime":$scope.incidentReportingDateTime,
											     "specialityId":$scope.specialityId,
											     "incidentTypeId":$scope.incidentTypeId,
											     "incidentCategoryId":$scope.incidentCategoryId,
											     "incidentReportId":$scope.incidentReportId,
											     "reportingTemplateId":$scope.reportingTemplateId,
											     "reportingTemplateObj":$scope.dataObj,
											     "incidentRemark":$scope.incidentRemark,
											     "roleId":$scope.roleId,
											     "userId":$scope.incidentUserId,
											     "patientId":$scope.patientId,
											     "organizationId":$scope.organisationId,
											     "unitId":$scope.unitId,
											     "status":'A',
												 "createdBy":$scope.userId,
												 "createdDate":$scope.dt,
												 "updatedBy":$scope.userId,
												 "updatedDate":$scope.dt,
											 }
											console.log("save data",JSON.stringify(data));
											var URI = BASE_URL + ROOT_URL + "/api/unit/saveIncidentReportingMapper";
											GenericService.serviceAction("POST", URI, data).then(function(response) 
											{	
												if (response.data.status == 'success')
												{
													
													growl.success(response.data.message,
															{
																title : 'Success!'
															});
													$scope.$broadcast('show-errors-reset');
													
													
													var uploadData = {};
													uploadData.incidentReportingMapperId = response.data.object;
													var promise = $scope.uploadEncounterDoc(uploadData);
													promise.then(function(response) 
															{
															  if(response.data.status == "success")
																  {
																    $scope.count++;
																    if($scope.finalDocUploadList.length==$scope.count)
																	 {
																	  
																	   $rootScope.stopLoader();
																	   $scope.encounterDetailsContractList = [];
																	   $scope.finalDocUploadList=[];
																	   $scope.allDocumentFinalList=[];
																	 }
																    
															      }
															  else 
																{																						
																	 $rootScope.stopLoader();
																}
														    });
													
													$scope.saveBtnFlag = true; 
													$scope.updateBtnFlag = false;
													$scope.incidentTypeId = 1;
													$scope.incidentReportingDate=new Date();
													$scope.incidentReportingDateTime='',
													$scope.specialityId='';
													$scope.incidentCategoryId='';
													$scope.incidentReportId='';
													$scope.reportingTemplateId='';
													$scope.incidentRemark='';
													$scope.roleId='';
													$scope.incidentUserId='';
													$scope.patientId=null,
													$scope.incidentReportingMapperId = null;
													$scope.obj2='';
													$scope.value='';
													
													$rootScope.stopLoader();
													
												} else if(response.data.status == 'error')
												{
													$rootScope.stopLoader();
													growl.error(response.data.message,
															{
																title : 'Error!'
															});
												}
												
											});
										} catch (e) {
											console.log("Exception",e.message);
										}
								}   
							   
								$scope.backScreen = function(info){
									
									$state.go('preOperative',
											{
												resultCommonObj: $scope.resultCommonObj
											});
								}
				}]);
		