/**
 * Golden-trace injectable tracer.
 *
 * This file is never loaded by the app itself and never touches any file
 * under legacy/. It is injected purely at the browser-automation layer
 * (Playwright's `addInitScript`, see capture.js) before any page script
 * runs, so it is present and armed before AngularJS or app.js execute --
 * "application code touched: none" holds regardless of what this captures.
 *
 * What it does: AngularJS 1.x's own bootstrap does
 * `angular = window.angular || (window.angular = {})` very early -- an
 * *empty* object at that point, populated with `.module` etc. a few
 * hundred lines later via `extend(angular, {module: ..., ...})`, which
 * mutates that same object in place rather than reassigning `window.angular`
 * again. So this hooks two levels: a `window.angular` setter to catch the
 * initial (still-empty) object, then a `.module` property interceptor on
 * that same object to catch the moment the real module-loader function
 * lands on it. The moment the app calls `angular.module('myApp', [...])`
 * (the one-time *definition* call, with a dependency array, as opposed to
 * the many later *retrieval* calls of the same name without one) through
 * that intercepted `.module`, this appends a .config()/.run() block that
 * installs:
 *
 *   - a $controller decorator: logs every controller actually instantiated,
 *     tagged with $state.current.name at that moment. This is the specific,
 *     named Phase 1 priority -- proving MasterController's internal
 *     branching is keyed on state name/URL, empirically, without reading
 *     its source.
 *   - an $http interceptor: logs method+url of every API call, tagged with
 *     the current state.
 *   - an $rootScope decorator: logs every $broadcast/$on registration.
 *   - $stateChangeStart/Success/Error listeners: logs state transitions,
 *     including failures (e.g. a resolve rejecting, or a genuinely missing
 *     controller throwing) -- this is real runtime evidence static analysis
 *     can never produce.
 *   - exposes window.__goldenTraceState ($state) and
 *     window.__goldenTraceRootScope ($rootScope) so capture.js can drive
 *     navigation directly via $state.go(...) rather than guessing the
 *     app's URL/hash-prefix scheme, and take a best-effort snapshot of
 *     custom $rootScope keys after a transition settles.
 *
 * Known limitation, stated plainly: the $rootScope signal here is a
 * post-transition snapshot of custom (non-$-prefixed, non-function) keys,
 * not a full write-by-write trace -- diffing on every possible assignment
 * generically is not reliable to do from outside the app. Good enough to
 * see "this state left behind X keys on $rootScope", not to reconstruct
 * exactly which line wrote them.
 */
(function () {
  window.__goldenTrace = {
    controllerInstantiations: [],
    httpCalls: [],
    broadcasts: [],
    listens: [],
    stateChanges: [],
    consoleErrors: [],
  };
  window.__goldenTraceCurrentState = null;
  window.__goldenTraceInstalled = false;

  window.addEventListener('error', function (e) {
    window.__goldenTrace.consoleErrors.push({
      message: e.message,
      source: e.filename + ':' + e.lineno,
      ts: Date.now(),
    });
  });

  var realAngular;
  Object.defineProperty(window, 'angular', {
    configurable: true,
    get: function () {
      return realAngular;
    },
    set: function (value) {
      realAngular = value;
      if (value && typeof value === 'object') {
        armModuleInterceptor(value);
      }
    },
  });

  function armModuleInterceptor(ng) {
    var realModuleFn;
    var wrappedModuleFn = function (name, deps) {
      var mod = realModuleFn.apply(ng, arguments);
      if (name === 'myApp' && Array.isArray(deps) && !window.__goldenTraceInstalled) {
        window.__goldenTraceInstalled = true;
        installDecorators(mod);
      }
      return mod;
    };
    Object.defineProperty(ng, 'module', {
      configurable: true,
      get: function () {
        return realModuleFn ? wrappedModuleFn : undefined;
      },
      set: function (fn) {
        realModuleFn = fn;
      },
    });
  }

  function installDecorators(mod) {
    mod.config([
      '$provide',
      '$httpProvider',
      function ($provide, $httpProvider) {
        $provide.decorator('$controller', [
          '$delegate',
          function ($delegate) {
            return function (ctrl, locals) {
              var name = typeof ctrl === 'string' ? ctrl : '(inline function, not a registered name)';
              window.__goldenTrace.controllerInstantiations.push({
                controller: name,
                stateName: window.__goldenTraceCurrentState,
                url: window.location.hash,
                ts: Date.now(),
              });
              return $delegate(ctrl, locals);
            };
          },
        ]);

        $provide.decorator('$rootScope', [
          '$delegate',
          function ($delegate) {
            var origBroadcast = $delegate.$broadcast;
            $delegate.$broadcast = function (name) {
              window.__goldenTrace.broadcasts.push({
                event: name,
                stateName: window.__goldenTraceCurrentState,
                ts: Date.now(),
              });
              return origBroadcast.apply(this, arguments);
            };
            var origOn = $delegate.$on;
            $delegate.$on = function (name) {
              window.__goldenTrace.listens.push({
                event: name,
                stateName: window.__goldenTraceCurrentState,
                ts: Date.now(),
              });
              return origOn.apply(this, arguments);
            };
            return $delegate;
          },
        ]);

        $httpProvider.interceptors.push(function () {
          return {
            request: function (config) {
              window.__goldenTrace.httpCalls.push({
                method: config.method,
                url: config.url,
                stateName: window.__goldenTraceCurrentState,
                ts: Date.now(),
              });
              return config;
            },
          };
        });
      },
    ]);

    mod.run([
      '$rootScope',
      '$state',
      function ($rootScope, $state) {
        window.__goldenTraceRootScope = $rootScope;
        window.__goldenTraceState = $state;

        $rootScope.$on('$stateChangeStart', function (event, toState) {
          window.__goldenTraceCurrentState = toState.name;
          window.__goldenTrace.stateChanges.push({
            event: 'start',
            state: toState.name,
            url: toState.url,
            ts: Date.now(),
          });
        });
        $rootScope.$on('$stateChangeSuccess', function (event, toState) {
          window.__goldenTrace.stateChanges.push({
            event: 'success',
            state: toState.name,
            ts: Date.now(),
          });
        });
        $rootScope.$on('$stateChangeError', function (event, toState, toParams, fromState, fromParams, error) {
          window.__goldenTrace.stateChanges.push({
            event: 'error',
            state: toState && toState.name,
            error: error ? String(error.message || error) : null,
            ts: Date.now(),
          });
        });
        $rootScope.$on('$stateNotFound', function (event, unfoundState) {
          window.__goldenTrace.stateChanges.push({
            event: 'notFound',
            state: unfoundState && unfoundState.to,
            ts: Date.now(),
          });
        });
      },
    ]);
  }
})();
