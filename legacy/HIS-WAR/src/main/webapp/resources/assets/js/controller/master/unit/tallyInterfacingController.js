(function () {
  "use strict";

  function tallyInterfacingController($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService,
								BillingGenericService, growl, $state,
								$stateParams,PagerService,GenericServiceParamHeader) {

							/* init() function for form object create. */
							$scope.init = function() {

								var cookieObject = $cookies.getObject('cookieObject');
						if (cookieObject == undefined) {
							$state.go('login');
							return;
						}
						$scope.unitId = cookieObject.unitId;
						$scope.organizationId = cookieObject.organizationId;
						$scope.userId = cookieObject.userId;
						$scope.doctorId = cookieObject.doctorId;

						$rootScope.loginpage = true;
								$scope.dateOptions;
								$scope.dateOptions = {
										formatYear : 'yyyy',
										showWeeks : false,
									};


								$scope.openDatePickerFromDate = function() {
									$scope.datepickerFromDate.opened = true;
							};

							$scope.datepickerFromDate = {
									opened : false
							};




							$scope.datepickerFromDate = new Date();

							}

							$scope.init();


							$scope.getUnitByOrganization = function()
							{
								var cookieObject = $cookies.getObject('cookieObject');
								var URI = BASE_URL + ROOT_URL+ "/unit/org/"+cookieObject.organizationId;
								GenericService.serviceAction("GET", URI, {}).then(function(response)
									{
											if (response.data.status == "success") {

												$scope.unitMasterList = response.data.listObject;
									}else if (response.data.status == "error")
									{

									}
								});
							}

							$scope.getUnitByOrganization();
							$scope.selectedUnitId="";

							 /******* START TAB FOR ALL PAGES IN TREE LEVEL *******/
							/*First Level*/
							$scope.firstTabLevel = 1;

							$scope.setFirstTabLevel = function(newTab) {

								$scope.firstTabLevel = newTab;
								$scope.slotIds = [];
							};

							$scope.isSetFirstTabLevel = function(tabNum) {
								return $scope.firstTabLevel === tabNum;
							};
							/*Second Level*/
							$scope.secondTabLevel = 1 ;

							$scope.setSecondTabLevel = function(newTab)
							{
								$scope.secondTabLevel = newTab;

										$scope.example14model = [];
										$scope.example14modelDoctor =[];
										$scope.doctorSlotList =[];
										$scope.slotIds = [];
										if(newTab == 1){
											$scope.isBlockedUnblocked = 2;
											$scope.isBlocked = 1;
										}
										else
											{
											$scope.isBlockedUnblocked = 1;
											$scope.isBlocked = 2;
											}

							};

							$scope.isSetSecondTabLevel = function(tabNum)
							{
								return $scope.secondTabLevel === tabNum;
							};

							$scope.getAllTallyData = function(){

								var data = {

											unitId :$scope.selectedUnitId,
											searchDate : moment($scope.datepickerFromDate).format('YYYY-MM-DD')
										};
								if(data.unitId == null || data.unitId==""){
									growl.error("Please Select Unit",
											{
												title: 'Warning!'
											});
								}
								else
								{
									$scope.getAllLedgerNamesFromHis(data);
									$scope.getIPLedger(data);
									$scope.getOPLedger(data);
									$scope.getInventoryLedger(data);
									$scope.getDoctorShareLedger(data);
								}
							};

/*---------------------------------------------- Ip Data ---------------------------------------------------------------------*/


							$scope.getIPLedger = function(data)
							{	$rootScope.startLoader();
								$scope.tallyDataList = [];

								var data = data;

								var URI = BASE_URL + ROOT_URL + "/api/global/getIpLedger";
								GenericService.serviceAction("POST", URI, data).then(function (response) {

										var responses = response;

									if (response.data.status == 'success') {
										$scope.tallyDataList = response.data.listObject;

										if(($scope.tallyDataList == null) || ($scope.tallyDataList.length == 0))
											{
												growl.error("Data not found for IP",
														{
															title:'Warning!'
														})
											}
										$rootScope.stopLoader();
										console.log("Tally Data",$scope.tallyDataList);
									}
									else if (response.data.status == 'error') {
											growl.error(response.data.message,
												{
													title: 'Error!'
												});
											$rootScope.stopLoader();
										}
								});

							};


	/*---------------------------------------------- Op Data ---------------------------------------------------------------------*/

							$scope.getOPLedger = function(data)
							{
								$rootScope.startLoader();
								$scope.opTallyDataList = [];
								var data = data;
								var URI = BASE_URL + ROOT_URL + "/api/global/getOpLedger";
								GenericService.serviceAction("POST", URI, data).then(function (response) {


									var responses = response;
									if (response.data.status == 'success') {
										$scope.opTallyDataList = response.data.listObject;
										if(($scope.opTallyDataList == null) || ($scope.opTallyDataList.length == 0))
										{
											growl.error("Data not found for OP",
													{
														title:'Warning!'
													});
										}
										$rootScope.stopLoader();
										console.log("Tally Data",$scope.opTallyDataList);
									}
									else if (response.data.status == 'error') {
											growl.error(response.data.message,
												{
													title: 'Error!'
												});
											$rootScope.stopLoader();
										}
								});

							};



/*---------------------------------------------- Inventory Data ---------------------------------------------------------------------*/

							$scope.getInventoryLedger = function(data)
							{
								$rootScope.startLoader();
								$scope.invTallyDataList = [];
								var data = data;
								var URI = BASE_URL + ROOT_URL + "/api/global/getInventoryLedger";
								GenericService.serviceAction("POST", URI, data).then(function (response) {


									var responses = response;
									if (response.data.status == 'success') {
										$scope.invTallyDataList = response.data.listObject;
										if(($scope.invTallyDataList == null) || ($scope.invTallyDataList.length == 0))
										{
											growl.error("Data not found for Inventory",
													{
														title:'Warning!'
													});
										}
										$rootScope.stopLoader();
										console.log("Tally Data",$scope.invTallyDataList);
									}
									else if (response.data.status == 'error') {
											growl.error(response.data.message,
												{
													title: 'Error!'
												});
											$rootScope.stopLoader();
										}
								});

							};


/*---------------------------------------------- Doctor-Share Data ---------------------------------------------------------------------*/

							$scope.getDoctorShareLedger = function(data)
							{
								$rootScope.startLoader();
								$scope.docShareTallyDataList = [];

								var data = data;
								var URI = BASE_URL + ROOT_URL + "/api/global/getDoctorShareLedger";
								GenericService.serviceAction("POST", URI, data).then(function (response) {
									if (response.data.status == 'success') {

										$scope.docShareTallyDataList = response.data.listObject;
										if(($scope.docShareTallyDataList == null) || ($scope.docShareTallyDataList.length == 0))
										{
											growl.error("Data not found for Doctor Share",
													{
														title:'Warning!'
													});
										}
										$rootScope.stopLoader();
										console.log("Tally Data",$scope.docShareTallyDataList);
									}
									else if (response.data.status == 'error') {
											growl.error(response.data.message,
												{
													title: 'Error!'
												});
											$rootScope.stopLoader();
										}
								});

							};

/*---------------------------------------------- Getting All Ledger Names From HIS ---------------------------------------------------------------------*/
							$scope.getAllLedgerNamesFromHis = function(data){

								$rootScope.startLoader();
								var data = data;
								$scope.ledgerNamesList = [];
                                var URI = BASE_URL + ROOT_URL + "/api/global/getAllLedgersNameFromHis";
                                GenericService.serviceAction("POST", URI, data).then(function (response){

                                    if (response.data.status == 'success') {
                                        $scope.ledgerNamesList = response.data.listObject;
                                        $rootScope.stopLoader();
                                        console.log("Ledger Names",$scope.ledgerNamesList);
                                    }
                                    else if(response.data.status == 'error')
                                    {
                                        growl.error(response.data.message,
                                        {
                                            title: 'Error!'
                                        });
                                        $rootScope.stopLoader();
                                    }

                                });
							};

							$scope.getAllTallyData();



/*---------------------------------------------- Sending data to Tally ---------------------------------------------------------------------*/

							$scope.transferTallyToXml = function(){
								$scope.getTallyIntegrationData=[];


								if($scope.isSetSecondTabLevel(1))
								{
									$scope.getTallyIntegrationData = $scope.opTallyDataList;
									$scope.transferTallyData();
								}

								else if($scope.isSetSecondTabLevel(2))
								{

									$scope.getTallyIntegrationData = $scope.tallyDataList;
									$scope.transferTallyData();

								}

								else if($scope.isSetSecondTabLevel(3))
								{
									$scope.getTallyIntegrationData = $scope.invTallyDataList;
									$scope.transferTallyData();
								}

								else if($scope.isSetSecondTabLevel(4))
								{
									$scope.getTallyIntegrationData = $scope.docShareTallyDataList;
									$scope.transferTallyData();
								}

							};


							$scope.transferTallyData = function(){

								if(($scope.getTallyIntegrationData)==null||($scope.getTallyIntegrationData)==[])
                                {
									growl.error("Data not Found",
                                    {
                                        title: 'Warning!'
                                    });
                                }
								else{
									$rootScope.startLoader();
                                    var URI = BASE_URL + ROOT_URL + "/api/global/sendTallyData";
                                    GenericService.serviceAction("POST", URI, $scope.getTallyIntegrationData).then(function (response) {

                                        if (response.data.status == 'success') {
                                            var xml = response.data.object;
                                            //$scope.sendStarter();
                                            $scope.sendToHost(xml);

                                        }
                                        else if(response.data.status == 'error')
                                        {
                                            growl.error(response.data.message,
                                            {
                                                title: 'Error!'
                                            });
                                        }
                                        $rootScope.stopLoader();
                                    });
								}
							};


							$scope.sendToHost = function(xml)
							{
							    if($scope.isTallyLocalHost !== undefined && $scope.isTallyLocalHost !== null && $scope.isTallyLocalHost == 'Y'){
                                    var url = $scope.tallyIp + ':' + $scope.tallyPortNo;
                                    var data = xml;
//                                    var config ={
//                                      "Access-Control-Allow-Origin":'*',
//                                      "Access-Control-Allow-Methods":"HEAD, GET, POST, PUT, PATCH, OPTIONS ,DELETE",
//                                      "Access-Control-Allow-Headers":'Access-Control-*, Origin, X-Requested-With, Content-Type, Accept,Origin, X-Auth-Token',
//                                      'Allow' :'GET, POST, PUT, DELETE, OPTIONS, HEAD',
//                                      'accept' :'application/xml',
//                                      'content-type' :'application/xml',
//                                      'scheme' :'http',
//                                    };

                                    var config = {
                                        headers: {
                                            'Accept': 'application/xml',
                                            'Content-Type': 'application/xml'
//                                            'scheme' :'http'
                                        }
                                    };

                                    $http.post(url, data, config).then(function (response){
                                        if(response.status==200){
                                            var tallyResponse = response.data !== undefined && response.data !== null ? response.data : '';
                                            console.log(tallyResponse);
                                            growl.success("Data Sent to Tally!!",
                                            {
                                                title:'Success!'
                                            });
                                        }
                                        else if (response.status=='error'){
                                            growl.error("Getting Error While Sending Request To Tally!",
                                            {
                                                title: 'Error!'
                                            });
                                        }
                                    },
                                    function (err) {
                                         growl.error("Tally Software is not Running!",
                                         {
                                            title:'Error!'
                                         });
                                    });
							    }
							    else{
                                    console.log(xml);
                                    var data = {
                                        tallyIp : $scope.tallyIp,
                                        tallyPortNo : $scope.tallyPortNo,
                                        requestXmlString : xml
                                    };
                                    var URI = BASE_URL + ROOT_URL + "/api/global/sendRequestToTally";
                                    GenericService.serviceAction("POST", URI, data).then(function (response) {
                                         if(parseInt(response.data.code) == 200){
                                            var tallyResponse = response.data.object;
                                            growl.success("Data Sent to Tally!!",
                                            {
                                                title:'Success!'
                                            });
                                            console.log(tallyResponse);
                                        }
                                         else if(parseInt(response.data.code) == 500)
                                        {
    //                                        growl.error("Getting Error while sending Data to Tally!",
                                            growl.error(response.data.message,
                                            {
                                                title: 'Error!'
                                            });
                                        }
                                         else if(parseInt(response.data.code) == 404){
                                             growl.error("Tally Software is not Running!",
                                             {
                                                title:'Error!'
                                             });
                                        }
                                    });
							    }
                            }

/*---------------------------------------------- Exporting .xml file of Tally data to Local Storage ---------------------------------------------------------------------*/

							$scope.exportTallyToXml = function(){
								$scope.getTallyIntegrationData=[];


								if($scope.isSetSecondTabLevel(1))
								{
									$scope.getTallyIntegrationData = $scope.opTallyDataList;
									$scope.exportTallyData();
								}

								else if($scope.isSetSecondTabLevel(2))
								{

									$scope.getTallyIntegrationData = $scope.tallyDataList;
									$scope.exportTallyData();

								}

								else if($scope.isSetSecondTabLevel(3))
								{
									$scope.getTallyIntegrationData = $scope.invTallyDataList;
									$scope.exportTallyData();
								}

								else if($scope.isSetSecondTabLevel(4))
								{
									$scope.getTallyIntegrationData = $scope.docShareTallyDataList;
									$scope.exportTallyData();
								}

							};


							$scope.download = function(xml) {

								var searchDate = moment($scope.datepickerFromDate).format('YYYY-MM-DD')

								const file = createBlob(xml)
							    var name = 'Tally - ' + searchDate + '.xml';
							    saveAs(file, name);
							  };


							  function createBlob(data) {
								  return new Blob([data], { type: "application/xml" });
								}

								function saveAs(content, fileName) {
								  const a = document.createElement("a");
								  const isBlob = content.toString().indexOf("Blob") > -1;
								  let url = content;
								  if (isBlob) {
								    url = window.URL.createObjectURL(content);
								  }
								  a.href = url;
								  a.download = fileName;
								  a.click();
								  if (isBlob) {
								    window.URL.revokeObjectURL(url);
								  }
								}

							$scope.exportTallyData = function(){

								if(($scope.getTallyIntegrationData)==null||($scope.getTallyIntegrationData)==[])
								{
								growl.error("Data not Found",
										{
											title: 'Warning!'
										});
								}
								else{
									$rootScope.startLoader();

								var URI = BASE_URL + ROOT_URL + "/api/global/exportTallyData";
								GenericService.serviceAction("POST", URI, $scope.getTallyIntegrationData).then(function (response) {



									if (response.data.status == 'success') {
										var xml = response.data.object;
										//$scope.sendStarter();
										$scope.download(xml);

										growl.success("Data Exported Successfully!",
											{
												title: 'Success!'
											});
									}
									else if (response.data.status == 'error') {
											growl.error(response.data.message,
												{
													title: 'Error!'
												});
										}
									$rootScope.stopLoader();
								});
								}
							};


/*---------------------------------------------- Verifying All Ledger Names From HIS and Tally ---------------------------------------------------------------------*/

							$scope.sendLedgerNameRequest = function(){

								$rootScope.startLoader();

								var data = {
										unitId :$scope.selectedUnitId,
										searchDate : moment($scope.datepickerFromDate).format('YYYY-MM-DD')
									};
								if(data.unitId == null || data.unitId==""){
									growl.error("Please Select Unit",
											{
												title: 'Warning!'
											});
									$rootScope.stopLoader();
								}
								else{
                                    $rootScope.startLoader();
                                    var data = {
                                            unitId :$scope.selectedUnitId,
                                            searchDate : moment($scope.datepickerFromDate).format('YYYY-MM-DD')
                                    };

                                    var URI = BASE_URL + ROOT_URL + "/api/global/sendLedgerNameRequest";
                                    GenericService.serviceAction("POST", URI, data).then(function(response) {
                                        if (response.data.status === 'success') {
                                            var ledgerNameXml = response.data.object;
                                            $scope.sendRequstToTally(ledgerNameXml);
                                        } else if (response.data.status === 'error') {
                                            growl.error(response.data.message, { title: 'Error!' });
                                        }
                                        $rootScope.stopLoader();
                                    })
                                    .catch(function(error) {
                                        console.error("Error in request chain:", error);
                                        $rootScope.stopLoader();
                                    });
							    }
							};


							$scope.sendRequstToTally = function(ledgerNameXml)
							{
							    if($scope.isTallyLocalHost !== undefined && $scope.isTallyLocalHost !== null && $scope.isTallyLocalHost == 'Y'){
                                    var url = $scope.tallyIp + ':' + $scope.tallyPortNo;
                                    var data = ledgerNameXml;
//                                    var config ={
//                                      "Access-Control-Allow-Origin":'*',
//                                      "Access-Control-Allow-Methods":"HEAD, GET, POST, PUT, PATCH, OPTIONS ,DELETE",
//                                      "Access-Control-Allow-Headers":'Access-Control-*, Origin, X-Requested-With, Content-Type, Accept,Origin, X-Auth-Token',
//                                      'Allow' :'GET, POST, PUT, DELETE, OPTIONS, HEAD',
//                                      'accept' :'application/xml',
//                                      'content-type' :'application/xml',
//                                      'scheme' :'http',
//                                    };

                                    var config = {
                                        headers: {
                                            'Accept': 'application/xml',
                                            'Content-Type': 'application/xml'
//                                            'scheme' :'http'
                                        }
                                    };

                                    $http.post(url, data, config).then(function (response){
                                        if(response.status==200){
                                            var tallyResponse = response.data !== undefined && response.data !== null ? response.data : '';
                                            console.log(tallyResponse);
                                            growl.success("Request Sent to Tally!!",
                                            {
                                                title:'Success!'
                                            });
                                             $scope.verifyLedgerNames(tallyResponse);
                                        }
                                        else if (response.status=='error'){
                                            growl.error("Getting Error While Sending Request To Tally!",
                                            {
                                                title: 'Error!'
                                            });
                                        }
                                    },
                                    function (err) {
                                         growl.error("Tally Software is not Running!",
                                         {
                                            title:'Error!'
                                         });
                                    });
							    }
							    else{
                                    console.log(ledgerNameXml);

                                    var data = {
                                        tallyIp : $scope.tallyIp,
                                        tallyPortNo : $scope.tallyPortNo,
                                        requestXmlString : ledgerNameXml
                                    };

                                    var URI = BASE_URL + ROOT_URL + "/api/global/sendRequestToTally";
                                    GenericService.serviceAction("POST", URI, data).then(function (response) {
                                         if(parseInt(response.data.code) == 200){
                                            var ledgerNameFromTally = response.data.object;
                                            $scope.verifyLedgerNames(ledgerNameFromTally);
                                            growl.success("Request Sent to Tally!!",
                                            {
                                                title:'Success!'
                                            });
                                            console.log(tallyResponse);
                                        }
                                         else if(parseInt(response.data.code) == 500)
                                        {
                                            growl.error(response.data.message,
                                            {
                                                title: 'Error!'
                                            });
                                        }
                                         else if(parseInt(response.data.code) == 404){
                                             growl.error("Tally Software is not Running!",
                                             {
                                                title:'Error!'
                                             });
                                        }
                                    });
                                }
                            }



							$scope.verifyLedgerNames = function(ledgerNameFromTally){

								$rootScope.startLoader();

									$scope.ledgerNamesFromTallyList =[];

										$scope.createThisLedger = '';
								var data = {};
										data.ledgerNamesFromTally = ledgerNameFromTally;
										data.ledgerNamesFromHis  = $scope.ledgerNamesList;

								var URI = BASE_URL + ROOT_URL + "/api/global/verifyLedgerNames";
								GenericService.serviceAction("POST", URI, data).then(function (response) {

									if (response.data.status == 'success') {
										$scope.ledgerNamesFromTallyList = response.data.listObject;

										$scope.transferTallyToXml();

									}
									else if(response.data.message == 'Ledger Not Found In Tally')
									{
										$scope.ledgerNamesFromTallyList =response.data.listObject;
										$scope.createThisLedger = response.data.object;
										angular.element('#confirmLedgerCreate').modal('show');
									}
									console.log('===========IN VERIFY LEDGER NAMES ERROR======');
									$rootScope.stopLoader();
								});
							};


/*---------------------------------------------- Creating New Ledger in Tally Application ---------------------------------------------------------------------*/


							$scope.createLedgerInTally = function(){

								$rootScope.startLoader();

								var data = {
										newLedger : $scope.createThisLedger,
										underGroupName : $scope.selectedUnderGroup
								};
								var URI = BASE_URL + ROOT_URL + "/api/global/sendCreateLedgerRequest";
								GenericService.serviceAction("POST", URI, data).then(function (response) {



									if (response.data.status == 'success') {
										var ledgerNameXml = response.data.object;
										//$scope.sendStarter();
										$scope.sendCreateLedgerRequest(ledgerNameXml);

									}
									else if(response.data.status == 'error')
									{
											growl.error(response.data.message,
												{
													title: 'Error!'
												});
										}
									$rootScope.stopLoader();
								});
							}


							$scope.sendCreateLedgerRequest = function(ledgerNameXml)
							{
							    if($scope.isTallyLocalHost !== undefined && $scope.isTallyLocalHost !== null && $scope.isTallyLocalHost == 'Y'){
                                    var url = $scope.tallyIp + ':' + $scope.tallyPortNo;
                                    var data = ledgerNameXml;
//                                    var config ={
//                                      "Access-Control-Allow-Origin":'*',
//                                      "Access-Control-Allow-Methods":"HEAD, GET, POST, PUT, PATCH, OPTIONS ,DELETE",
//                                      "Access-Control-Allow-Headers":'Access-Control-*, Origin, X-Requested-With, Content-Type, Accept,Origin, X-Auth-Token',
//                                      'Allow' :'GET, POST, PUT, DELETE, OPTIONS, HEAD',
//                                      'accept' :'application/xml',
//                                      'content-type' :'application/xml',
//                                      'scheme' :'http',
//                                    };

                                    var config = {
                                        headers: {
                                            'Accept': 'application/xml',
                                            'Content-Type': 'application/xml'
//                                            'scheme' :'http'
                                        }
                                    };

                                    $http.post(url, data, config).then(function (response){
                                        if(response.status==200){
                                            var tallyResponse = response.data !== undefined && response.data !== null ? response.data : '';
                                            console.log(tallyResponse);
                                            growl.success("Ledger Created Successfully!!",
                                            {
                                                title:'Success!'
                                            });
                                             $scope.sendRefreshLedgerNameRequest();
                                        }
                                        else if (response.status=='error'){
                                            growl.error("Getting Error While Sending Request To Tally!",
                                            {
                                                title: 'Error!'
                                            });
                                        }
                                    },
                                    function (err) {
                                         growl.error("Tally Software is not Running!",
                                         {
                                            title:'Error!'
                                         });
                                    });
							    }
							    else{
                                    console.log(ledgerNameXml);
                                    var data = {
                                        tallyIp : $scope.tallyIp,
                                        tallyPortNo : $scope.tallyPortNo,
                                        requestXmlString : ledgerNameXml
                                    };

                                    var URI = BASE_URL + ROOT_URL + "/api/global/sendRequestToTally";
                                    GenericService.serviceAction("POST", URI, data).then(function (response) {
                                         if(parseInt(response.data.code) == 200){
                                            growl.success("Ledger Created Successfully!!",
                                            {
                                                title:'Success!'
                                            });
                                            $scope.sendRefreshLedgerNameRequest();
                                         }
                                         else if(parseInt(response.data.code) == 500)
                                        {
    //                                        growl.error("Getting Error while sending Data to Tally!",
                                            growl.error(response.data.message,
                                            {
                                                title: 'Error!'
                                            });
                                        }
                                         else if(parseInt(response.data.code) == 404){
                                             growl.error("Tally Software is not Running!",
                                             {
                                                title:'Error!'
                                             });
                                        }
                                    });
                                }
                            }

/*---------------------------------------------- Refreshing Tally Ledger Names List  ---------------------------------------------------------------------*/

							$scope.sendRefreshLedgerNameRequest = function(){

								var data = {

										searchDate : moment($scope.datepickerFromDate).format('YYYY-MM-DD')
									};

								var URI = BASE_URL + ROOT_URL + "/api/global/sendLedgerNameRequest";
								GenericService.serviceAction("POST", URI, data).then(function (response) {
									if (response.data.status == 'success') {

										var ledgerNameXml = response.data.object;
										$scope.sendRefreshTallyLedgerNameList(ledgerNameXml);


									}
									else if(response.data.status == 'error')
									{
											growl.error(response.data.message,
												{
													title: 'Error!'
												});
										}

								});

							};

							$scope.sendRefreshTallyLedgerNameList = function(ledgerNameXml)
							{
                                if($scope.isTallyLocalHost !== undefined && $scope.isTallyLocalHost !== null && $scope.isTallyLocalHost == 'Y'){
                                    var url = $scope.tallyIp + ':' + $scope.tallyPortNo;
                                    var data = ledgerNameXml;
//                                    var config ={
//                                      "Access-Control-Allow-Origin":'*',
//                                      "Access-Control-Allow-Methods":"HEAD, GET, POST, PUT, PATCH, OPTIONS ,DELETE",
//                                      "Access-Control-Allow-Headers":'Access-Control-*, Origin, X-Requested-With, Content-Type, Accept,Origin, X-Auth-Token',
//                                      'Allow' :'GET, POST, PUT, DELETE, OPTIONS, HEAD',
//                                      'accept' :'application/xml',
//                                      'content-type' :'application/xml',
//                                      'scheme' :'http',
//                                    };

                                    var config = {
                                        headers: {
                                            'Accept': 'application/xml',
                                            'Content-Type': 'application/xml'
//                                            'scheme' :'http'
                                        }
                                    };

                                    $http.post(url, data, config).then(function (response){
                                        if(response.status==200){
                                            var tallyResponse = response.data !== undefined && response.data !== null ? response.data : '';
                                            console.log(tallyResponse);
                                             $scope.getRefreshTallyLedgerNameList(tallyResponse);
                                        }
                                        else if (response.status=='error'){
                                            growl.error("Getting Error While Sending Request To Tally!",
                                            {
                                                title: 'Error!'
                                            });
                                        }
                                    },
                                    function (err) {
                                         growl.error("Tally Software is not Running!",
                                         {
                                            title:'Error!'
                                         });
                                    });
							    }
							    else{
                                    var data = {
                                        tallyIp : $scope.tallyIp,
                                        tallyPortNo : $scope.tallyPortNo,
                                        requestXmlString : ledgerNameXml
                                    };

                                    var URI = BASE_URL + ROOT_URL + "/api/global/sendRequestToTally";
                                    GenericService.serviceAction("POST", URI, data).then(function (response) {
                                         if (parseInt(response.data.code) == 200) {
                                            var ledgerNameFromTally = response.data.object;
                                            $scope.getRefreshTallyLedgerNameList(ledgerNameFromTally);
                                        }
                                         else if(parseInt(response.data.code) == 500)
                                        {
        //                                        growl.error("Getting Error While Sending Request To Tally!",
                                            growl.error(response.data.message,
                                            {
                                                title: 'Error!'
                                            });
                                        }
                                        else if(parseInt(response.data.code) == 404){
                                             growl.error("Tally Software is not Running!",
                                             {
                                                title:'Error!'
                                             });
                                        }
                                    });
                                }
                            }

							$scope.getRefreshTallyLedgerNameList = function(ledgerNameFromTally){

								$rootScope.startLoader();

									$scope.ledgerNamesFromTallyList =[];


								var data = {};
										data.ledgerNamesFromTally = ledgerNameFromTally;


								var URI = BASE_URL + ROOT_URL + "/api/global/refreshTallyLedgerNameList";
								GenericService.serviceAction("POST", URI, data).then(function (response) {

									if (response.data.status == 'success') {

										$scope.ledgerNamesFromTallyList =response.data.listObject;
										growl.success("Ledger List Refresh Succeed!!",
												{
													title:'Success!'
												});

//

										$rootScope.stopLoader();
									}
									else if(response.data.status == 'error')
									{
											growl.error(response.data.message,
												{
													title: 'Error!'
												});
											$rootScope.stopLoader();
									}

								});
							}

                                $scope.getTallyInterfaceCommunicationKeys = function(){
                                    if($scope.selectedUnitId !== undefined && $scope.selectedUnitId !== null && $scope.selectedUnitId !== ''){
                                        var data = {
                                            unitId : parseInt($scope.selectedUnitId),
                                            organizationId : $scope.organizationId
                                        };

                                        var URI = BASE_URL + ROOT_URL + "/api/global/getTallyInterfaceCommunicationKeys";
                                        GenericService.serviceAction("POST", URI, data).then(function (response) {
                                            if (response.data.status == 'success') {
                                                if(response.data.object.isTallyLocalHost !== undefined && response.data.object.isTallyLocalHost !== null && response.data.object.isTallyLocalHost === "N"){
                                                    $scope.tallyIp = response.data.object.tallyIp !== undefined || response.data.object.tallyIp !== null ? response.data.object.tallyIp : "http://127.0.0.1";
                                                    $scope.tallyPortNo = response.data.object.tallyPortNo !== undefined || response.data.object.tallyPortNo !== null ? response.data.object.tallyPortNo : "9000";
                                                    $scope.isTallyLocalHost = response.data.object.isTallyLocalHost !== undefined || response.data.object.isTallyLocalHost !== null ? response.data.object.isTallyLocalHost : "N";
                                                }
                                                else {
                                                    $scope.tallyIp = "http://127.0.0.1";
                                                    $scope.tallyPortNo = "9000";
                                                    $scope.isTallyLocalHost = "Y";
                                                }
                                            }
                                            else if(response.data.status == 'error')
                                            {
                                                growl.error(response.data.message,
                                                {
                                                    title: 'Error!'
                                                });
                                            }
                                        });
                                    }
                                };

                                $scope.getTallyInterfaceCommunicationKeys();


							$scope.getAllLedgerNamesFromHisForTallyTxn = function(){

								$rootScope.startLoader();
								var data = {
										unitId :$scope.selectedUnitId,
										searchDate : moment($scope.datepickerFromDate).format('YYYY-MM-DD')
                                };
								$scope.ledgerNamesList = [];
                                var URI = BASE_URL + ROOT_URL + "/api/global/getAllLedgersNameFromHis";
                                GenericService.serviceAction("POST", URI, data).then(function (response){

                                    if (response.data.status == 'success') {
                                        $scope.ledgerNamesList = response.data.listObject;
                                        $rootScope.stopLoader();
                                        $scope.sendLedgerNameRequest();
                                        console.log("Ledger Names",$scope.ledgerNamesList);
                                    }
                                    else if(response.data.status == 'error')
                                    {
                                        growl.error(response.data.message,
                                        {
                                            title: 'Error!'
                                        });
                                        $rootScope.stopLoader();
                                    }

                                });
							};

							$scope.underGroupList = [];
							$scope.underGroupList.push("Bank Accounts");
							$scope.underGroupList.push("Bank OCC A/c");
							$scope.underGroupList.push("Bank OD A/c");
							$scope.underGroupList.push("Branch / Divisions");
							$scope.underGroupList.push("Capital Account");
							$scope.underGroupList.push("Cash-in-hand");
							$scope.underGroupList.push("Current Assets");
							$scope.underGroupList.push("Current Liabilities");
							$scope.underGroupList.push("Deposits (Asset)");
							$scope.underGroupList.push("Direct Expenses");
							$scope.underGroupList.push("Direct Incomes");
							$scope.underGroupList.push("Duties & Taxes");
							$scope.underGroupList.push("Expenses (Direct)");
							$scope.underGroupList.push("Expenses (Indirect)");
							$scope.underGroupList.push("Fixed Assets");
							$scope.underGroupList.push("Income (Direct)");
							$scope.underGroupList.push("Income (Indirect)");
							$scope.underGroupList.push("Indirect Expenses");
							$scope.underGroupList.push("Indirect Incomes");
							$scope.underGroupList.push("Investments");
							$scope.underGroupList.push("Loan & Advances (Asset)");
							$scope.underGroupList.push("Loans (Liability)");
							$scope.underGroupList.push("Misc. Expenses (ASSET)");
							$scope.underGroupList.push("Provisions");
							$scope.underGroupList.push("Purchase Accounts");
							$scope.underGroupList.push("Reserves & Surplus");
							$scope.underGroupList.push("Retained Earnings");
							$scope.underGroupList.push("Sales Accounts");
							$scope.underGroupList.push("Secured Loans");
							$scope.underGroupList.push("Sundry Creditors");
							$scope.underGroupList.push("Sundry Debtors");
							$scope.underGroupList.push("Suspense A/c");
							$scope.underGroupList.push("Unsecured Loans");


						/******* END TAB FOR ALL PAGES IN TREE LEVEL *******/

						}

  function config($stateProvider) {
    $stateProvider.state("tallyInterfacing", {
    	url: '/tallyInterfacing',
    	templateUrl: 'views/master/UnitMaster/tally-interfacing.html',
    	controller: "tallyInterfacingController",
    	title: 'Tally Interfacing',

    });
  }

  angular
    .module("myApp")
    .config(config)
    .controller("tallyInterfacingController", tallyInterfacingController);
})();


