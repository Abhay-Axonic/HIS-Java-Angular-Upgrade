angular
    .module('myApp')
    .controller(
        "accessControlController",
        ['$scope', '$rootScope', '$state', '$cookies', 'GenericService', '$sessionStorage', 'growl',
            '$stateParams', '$http', 'CONSTANTS', 'ivhTreeviewOptions', 'ivhTreeviewMgr', 'ivhTreeviewBfs','BillingGenericService',
            function($scope, $rootScope, $state, $cookies, GenericService, $sessionStorage, growl,
                $stateParams, $http,CONSTANTS, ivhTreeviewOptions, ivhTreeviewMgr, ivhTreeviewBfs,BillingGenericService) {
                $scope.title = "Access Control";
                $rootScope.loginpage = true;

                $scope.type = 'role';
               // alert(CONSTANTS.GLOBAL.USER_STORE_API);

                $scope.accessFormObj = {
                    roleId: '',
                    privilegeId: '',
                    moduleType: '',
                    modules: []
                }

                var data;

                $scope.ui = {
                    roles: [],
                    modalTitle: '',
                    showModule: false,
                    showSubModule: false,
                    showLink: false,
                    showRoleForm: true,
                    edit: '',
                    deleteId: ''

                }


                $scope.savePermissions = function(moduleType) {

                    $scope.accessFormObj.modules = [];

                    $scope.accessFormObj.moduleType = moduleType;
                    $scope.accessFormObj.privilegeId = $scope.hisPrivilegeId;

                  ivhTreeviewBfs($scope.hisBags, function(node) {
                    	
                        if (node.selected) {
                            $scope.accessFormObj.modules.push({
                                id: node.moduleId,
                                name: node.moduleName,
                                actionLink: node.actionLink,
                                parentId: node.parentId,
                                moduleType: 1
                            })
                        }
                    })
                    
                    /*var nd1=false;
                    var nd2=false;
                    var nd3=false;
                    var nd4=false;
                    ivhTreeviewBfs($scope.hisBags, function(node1) 
                    		{
                        if (node1.children > 0) {
                            ivhTreeviewBfs(node1.children, function(node2) {
                                if (node2.children > 0) {
                                    ivhTreeviewBfs(node2.children, function(node3) {

                                        if (node3.children > 0) {

                                            nd3 = false;
                                            ivhTreeviewBfs(node3.children, function(node4) {
                                                if (node4.selected) {
                                                    nd3 = true;
                                                    $scope.accessFormObj.modules.push({
                                                        id: node4.moduleId,
                                                        name: node4.moduleName,
                                                        parentId: node4.parentId,
                                                        moduleType: 1
                                                    })
                                                }
                                            })
                                            if (nd3) {
                                                nd3 = false;
                                                nd2 = true;
                                                $scope.accessFormObj.modules.push({
                                                    id: node3.moduleId,
                                                    name: node3.moduleName,
                                                    parentId: node3.parentId,
                                                    moduleType: 1
                                                })
                                            }
                                        }
                                    })
                                    if (nd2) {
                                        nd2 = false;
                                        nd1 = true;
                                        $scope.accessFormObj.modules.push({
                                            id: node3.moduleId,
                                            name: node3.moduleName,
                                            parentId: node3.parentId,
                                            moduleType: 1
                                        })
                                    }
                                }
                            })
                            if (nd1) {
                                nd1 = false;
                                $scope.accessFormObj.modules.push({
                                    id: node3.moduleId,
                                    name: node3.moduleName,
                                    parentId: node3.parentId,
                                    moduleType: 1
                                })
                            }
                        }
                    })*/

                    ivhTreeviewBfs($scope.biBags, function(node) {
                        if (node.selected) {
                            $scope.accessFormObj.modules.push({
                                id: node.moduleId,
                                name: node.moduleName,
                                actionLink: node.actionLink,
                                parentId: node.parentId,
                                moduleType: 2
                            })
                        }
                    })

                    $scope.accessFormObj.organisationId = parseInt(sessionStorage.getItem("organizationId"));
                    $scope.accessFormObj.unitId = parseInt(sessionStorage.getItem("unitId"));

                    if ($scope.ui.edit) {
                        var URI = CONSTANTS.GLOBAL.PRIVILEGE_API + '/role';
                        $scope.accessFormObj.moduleType = moduleType;
                        $rootScope.startLoader();
                        BillingGenericService.serviceAction('put',URI, $scope.accessFormObj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
                                $scope.getPermissions();
                                $scope.permissions = [];
                                $scope.accessFormObj = {
                                    modules: [],
                                    roleId: '',
                                }
                                growl.success("Saved Successfully");
                                $rootScope.stopLoader();
                                $scope.getPermissions();

                            }).catch(function(err) {
                                growl.error("Failed to save");
                                $scope.getPermissions();
                            })
                    } else {
                        var URI = CONSTANTS.GLOBAL.PRIVILEGE_API;
                        $scope.accessFormObj.privilegeId = "";
                        $rootScope.startLoader();
                        BillingGenericService.serviceAction(METHOD_POST,URI, $scope.accessFormObj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
                                $scope.permissions = [];
                                $scope.getPermissions();
                                $scope.accessFormObj = {
                                    modules: [],
                                    roleId: '',
                                }

                                if(response.data.error){
                                 growl.error(response.data.error);
                                }else{
                                  growl.success("Saved Successfully");
                                }
                                //growl.success("Saved Successfully");
                                $rootScope.stopLoader();
                                $scope.getPermissions();
                            })
                            .catch(function(err) {
                                growl.error("Failed to save");
                                $scope.getPermissions();
                            })
                    }
                    //deselectNodes();
                    collapseAll();

                }

                $scope.deletePermissions = function() {
                	$rootScope.startLoader();
                    var URI = CONSTANTS.GLOBAL.PRIVILEGE_API + '/role/' + parseInt(sessionStorage.getItem("organizationId")) + "/" + parseInt(sessionStorage.getItem("unitId")) + "/" + $scope.ui.deleteId;
                    BillingGenericService.serviceAction('delete',URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
                        	$rootScope.stopLoader();
                            $scope.getPermissions();
                        })
                }	

                $scope.getPermissions = function() {
                    var URI = CONSTANTS.GLOBAL.PRIVILEGE_API + '/' + parseInt(sessionStorage.getItem("organizationId")) + "/" + parseInt(sessionStorage.getItem("unitId"));
                    console.log("URI", URI);
                    $rootScope.startLoader();
                    BillingGenericService.serviceAction(METHOD_GET,URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
                            $scope.permissionList = response.data.privileges;
                            $rootScope.stopLoader();
                        })
                }


                $scope.initModal = function(permissionItem, type) {
                	
                	var element = angular.element('#instructionDetails');
               
                    deselectNodes();
                    collapseAll();
                    $scope.ui.modalTitle = type + ' Permissions';
                    $scope.accessFormObj.modules = [];
                    if (type === 'Add' || type === 'Edit') {
                        $scope.ui.showRoleForm = true;
                        $scope.permissions = [];
                        if (type === 'Edit') {

                            $scope.biPrivilegeId = 0;
                            $scope.hisPrivilegeId = 0;
                            $scope.ui.edit = true;

                            $rootScope.startLoader();
                            var URI = BASE_URL + ROOT_URL + "/privilege/role/" + parseInt(sessionStorage.getItem("organizationId")) + "/" + parseInt(sessionStorage.getItem("unitId")) + "/" + permissionItem.roleId;
                            BillingGenericService.serviceAction(METHOD_GET,URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
                                	var dataList = response.data.modules.length > 0 ? response.data.modules[0].modules: null;
                            		
                            		if(dataList!=null){
                            			 $scope.accessFormObj.roleId = response.data.modules[0].roleId;
                            			angular.forEach(dataList, function(value, index){
                            				if(value.moduleType==1){
    	                             			$scope.hisPrivilegeId = response.data.modules[0].privilegeId;
    	                         				selectNodes(value);
                            				}
                            			    else if(value.moduleType==2){
    	                    				   $scope.biPrivilegeId = response.data.modules[0].privilegeId;
    	                    				   selectNodes(value);
                        				   }
                            			})
                            			
                            		}
                            		
                            		$rootScope.stopLoader();
                            		element.modal('show');
                                    
                                })
                        } else {
                            $scope.ui.edit = false;
                            $scope.accessFormObj.roleId = "";
                            $scope.accessFormObj.moduleType = "";
                            $scope.accessFormObj.modules = [];
                            element.modal('show');

                        }
                    } else if (type === 'Delete') {
                        $scope.ui.deleteId = permissionItem.roleId;
                    }

                }

                $scope.roleChanged = function() {
                    var arr = $scope.permissionList.filter(function(item) {
                        return item.roleId == $scope.accessFormObj.roleId;
                    })
                    if (arr.length <= 0) {
                        $scope.ui.duplicateRole = false
                    } else {
                        if (arr.moduleType == $scope.accessFormObj.moduleType) {
                            $scope.ui.duplicateRole = true
                            growl.error("Permissions already assigned to this role.");
                        } else {
                            $scope.ui.duplicateRole = false
                        }

                    }
                }


                function getRoles() {
                    var URI = CONSTANTS.GLOBAL.ROLE_API + '/' + parseInt(sessionStorage.getItem("organizationId")) + "/" + parseInt(sessionStorage.getItem("unitId"));
                   /* $http.get(URI)
                        .then(function(response) {
                            $scope.ui.roles = response.data.roles;
                        })*/

                        BillingGenericService.serviceAction(METHOD_GET,URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
                        	 $scope.ui.roles = response.data.roles;
                        	 $scope.permissionList=$scope.ui.roles;
                        	 console.log("permissionList",$scope.permissionList);
                        });

                }
                function getModules() {
                    var URI = CONSTANTS.GLOBAL.MODULE_API+ '/' + parseInt(sessionStorage.getItem("organizationId"));
                    BillingGenericService.serviceAction(METHOD_GET,URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
                            data = response.data.listObject;
                            var modules = data.filter(function(item) {
                                return item.parentId == 0;
                            });
                            initTree(modules);
                            $scope.hisBags = modules;
                        })
                }

                $scope.getBiModules = function() {
                    var URI = BASE_URL + ROOT_URL + "/module/biModules";
                    
                    BillingGenericService.serviceAction(METHOD_GET,URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
                            data = response.data.listObject;
                            var modules = data.filter(function(item) {
                                return item.parentId == 0;
                            });
                            initTree(modules);
                            $scope.biBags = modules;
                        })
                }
                $scope.firstTabLevel = 1;
                $rootScope.setFirstTabLevel = function(newTab) {
                    $scope.firstTabLevel = newTab;
                };
                $scope.isSetFirstTabLevel = function(tabNum) {
                    return $scope.firstTabLevel === tabNum;
                };


                function initTree(modules) {
                    for (var i = 0; i < modules.length; i++) {
                        var subModules = data.filter(function(item) {
                            return item.parentId == modules[i].moduleId;
                        });
                        modules[i].children = subModules;

                        initTree(modules[i].children);
                    }
                }



                function selectNodes(moduleObj) {
                    // Selecting by node id

                    if (moduleObj.moduleType == 1) {
                       
                            ivhTreeviewMgr.select($scope.hisBags, moduleObj.id);
                        
                    } else if (moduleObj.moduleType == 2) {
                        
                            ivhTreeviewMgr.select($scope.biBags, moduleObj.id);
                        
                    }

                };

                function deselectNodes() {
                    ivhTreeviewMgr.deselectAll($scope.hisBags);
                    ivhTreeviewMgr.deselectAll($scope.biBags);
                }

                function collapseAll() {
                    ivhTreeviewMgr.collapseRecursive($scope.hisBags, $scope.hisBags);
                    ivhTreeviewMgr.collapseRecursive($scope.biBags, $scope.biBags);
                };

                $scope.getRoleDesc = function(roleId) {
                    for (var i = 0; i < $scope.ui.roles.length; i++) {
                        if (roleId === $scope.ui.roles[i].roleId)
                            return $scope.ui.roles[i].roleDesc;
                    }
                }

                getRoles();
                getModules();
                $scope.getBiModules();
                $scope.getPermissions();

            }
        ]);