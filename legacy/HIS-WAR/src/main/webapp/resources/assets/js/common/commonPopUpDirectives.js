angular.module('myApp').directive('modal',[  
    '$parse', '$compile', function ($parse, $compile) {
  return {
    template: '<div id="confirmLogout" class="modal fade ng-scope in" role="dialog his-style" style="background:rgba(0, 0, 0, 0.33);" data-keyboard="false" data-backdrop="static">' +
      '<div class="modal-dialog">' +
      '<div class="modal-content">' +
      '<div class="modal-header">' +
      '<div class="modal-title patient-details-row">' +
      'Confirm' +
      '<button type="button" class="close" data-dismiss="modal" ng-click="cancel();stopLoader();">&times;</button>' +
      '</div>' +
      '</div>' +
      '<div class="modal-body">' +
      '<div class="margin-left10"><span>Are you sure you want to Save?</span></div>' +
      '</div>' +
      '<div class="modal-footer" style="border:none;">' +

      '<div class="row">'+
			'<div class="col-lg-1 col-md-2 col-sm-2  col-md-2 col-sm-2 padding-left-right-set4 text-width-auto float-right" >'+
				'<button class="btn btn-primary btn-save-box-shadow  width100" title="Cancel" ng-click="stopLoader();"  data-dismiss="modal" id="cancel">'+
				'<i class="fa fa-times" aria-hidden="true"></i> </button>'+
			'</div>'+
			'<div class="col-lg-1 col-md-2 col-sm-2  col-md-2 col-sm-2 padding-left-right-set4 text-width-auto float-right">'+
				'<button class="btn btn-primary btn-save-box-shadow  width100" title="Save"  one-click-only="PopupOkBtn();" oco-id="clk" id="ok">'+
				'<i class="fa fa-floppy-o" aria-hidden="true"></i> </button>'+
			'</div>'+
		'</div>'+

      '</div>' +
      '</div>' +
      '</div>' +
      '</div>',
    restrict: 'E',
    transclude: true,
    replace: true,
    scope: true,
    link: function postLink(scope, element, attrs) {
      scope.$watch(attrs.visible, function (value) {
        if (value == true){
        	  var fn = $parse(attrs['oneClickOnly']);
          scope.oneClickOnly["clk"].fn(false);
          $("#clk").prop('disabled', false); 
          $(element).modal('show');
        }else{
          $(element).modal('hide');
     	  scope.$parent["popUpFlag"]  = true;
          scope.$parent["showModal"] = false;	
        }
     
      });

      $(element).on('shown.bs.modal', function () {
        scope.$apply(function () {
          scope.$parent[attrs.visible] = true;
        });
      });

      $(element).on('hidden.bs.modal', function () {
        scope.$apply(function () {
          scope.$parent[attrs.visible] = false;
        });
      });
    }
  };
}]);

angular.module('myApp').directive('oneClickOnly', [  
    '$parse', '$compile', function($parse, $compile) {
      return {
        restrict: 'A',
        scope: false, // get the parent scope directly
        compile: function(element, attr) {
          var fn = $parse(attr['oneClickOnly']);
          return {
            pre: function(scope, element, attr) {
              scope.oneClickOnly = scope.oneClickOnly || {}
              scope.oneClickOnly[attr.ocoId] = scope.oneClickOnly[attr.ocoId] || {}
              scope.oneClickOnly[attr.ocoId].fn = function(value) {
                scope.oneClickOnly[attr.ocoId].val = value
               if(value) {                
                  element.text(attr.ocoTextWait)
                  element.attr('disabled', true)
                  scope.$parent["popUpFlag"]  = true;
                  scope.$parent["showModal"] = false;	
                 
                } else {
                  element.text(attr.ocoText)
                  element.attr('disabled', false)
              
                }
              }
              var clickingCallback = function(e) {
                if(scope.oneClickOnly 
                  && scope.oneClickOnly[attr.ocoId]
                  && scope.oneClickOnly[attr.ocoId].val) {
                	 scope.oneClickOnly['clk'].fn(true);
                  return
                } else{        
                 scope.oneClickOnly[attr.ocoId].fn(true)
                } 
                fn(scope, { $event: e });
              };
            
              element.bind("click", clickingCallback)
            }
          }
        }
      }
    }
  ])
