angular.module('myApp').directive('fileModel', ['$parse', function ($parse) {
	    return {
	        restrict: 'A',
	        link: function(scope, element, attrs) {
	            var model = $parse(attrs.fileModel);
	            var modelSetter = model.assign;
	            
	            element.bind('change', function(){
	                scope.$apply(function(){
	                    modelSetter(scope, element[0].files[0]);
	                });
	            });
	        }
	    };
	}]);

    angular.module('myApp').directive('validfile', function validFile() {
        var validFormats = ['jpg', 'txt', 'pdf', 'docx', 'doc', 'jpeg', 'png', 'gif', 'csv'];
        var maxSize = 5 * 1024 * 1024; // 5MB
        return {
            link: function(scope, elem, attrs, ctrl) {
                elem.on('change', function() {
                    angular.forEach(elem[0].files, function(file){
                        scope.$apply(function() {
                            if (file) {
                                var value = elem.val(),
                                    ext = value.substring(value.lastIndexOf('.') + 1).toLowerCase();

                                if (validFormats.indexOf(ext) === -1) {
                                    elem.val('');
                                    scope.fileError = "Invalid file format. Allowed file formats are : "  + validFormats.join(', ')+'.';
                                    return;
                                }
                                else if (file.size > maxSize) {
                                    elem.val('');
                                    scope.fileError = "File is too large. Upload a file less than 5MB.";
                                    return;
                                }
                                else{
                                    scope.fileError = '';
                                }
                            }
                        });
                    })
                });
            }
        };
    });

        angular.module('myApp').directive('validfile', function validFile25Mb() {
            var validFormats = ['jpg', 'txt', 'pdf', 'docx', 'doc', 'jpeg', 'png', 'gif', 'csv'];
            var maxSize = 25 * 1024 * 1024; // 25MB
            return {
                link: function(scope, elem, attrs, ctrl) {
                    elem.on('change', function() {
                        angular.forEach(elem[0].files, function(file){
                            scope.$apply(function() {
                                if (file) {
                                    var value = elem.val(),
                                        ext = value.substring(value.lastIndexOf('.') + 1).toLowerCase();

                                    if (validFormats.indexOf(ext) === -1) {
                                        elem.val('');
                                        scope.fileError = "Invalid file format. Allowed file formats are : "  + validFormats.join(', ')+'.';
                                        return;
                                    }
                                    else if (file.size > maxSize) {
                                        elem.val('');
                                        scope.fileError = "File is too large. Upload a file less than 5MB.";
                                        return;
                                    }
                                    else{
                                        scope.fileError = '';
                                    }
                                }
                            });
                        })
                    });
                }
            };
        });

/*angular.module('myApp').directive('fileModelUp', ['$parse', function ($parse) {
    return {
        restrict: 'A',
        link: function(scope, element, attrs) {
            var model = $parse(attrs.fileModel);
            var modelSetter = model.assign;
            
            element.bind('change', function(){
                scope.$apply(function(){
                    modelSetter(scope, element[0].files);
                });
            });
        }
    };
}]);*/
angular.module('myApp').directive("fileinput", [function() {
    return {
      scope: {
        fileinput: "=",
        filepreview: "="
      },
      link: function(scope, element, attributes) {
        element.bind("change", function(changeEvent) {
          scope.fileinput = changeEvent.target.files[0];
          var reader = new FileReader();
          reader.onload = function(loadEvent) {
            scope.$apply(function() {
              scope.filepreview = loadEvent.target.result;
            });
          }
          if(scope.fileinput !== undefined){
            reader.readAsDataURL(scope.fileinput);
          }
        });
      }
    }
  }])