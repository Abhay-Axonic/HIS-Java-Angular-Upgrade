/* eslint-disable */
/**
 * Global voice-to-text mic for OPD / IPD coversheets.
 *
 * - Renders a yellow IPD-style microphone button on the right of any
 *   <textarea> (and selected free-text <input type="text"> fields)
 *   inside the OPD and IPD coversheet templates.
 * - Reuses the browser SpeechRecognition API (en-IN) used elsewhere
 *   in the app so we don't introduce a second recogniser.
 * - Writes the recognised transcript back via the field's existing
 *   ngModel controller, so all bindings keep working.
 *
 * Provides:
 *   1. <input voice-mic> / <textarea voice-mic> attribute directive
 *   2. Auto-scan that decorates every textarea and matching free-text
 *      input in the active coversheet view, no markup changes required.
 */
(function () {
	'use strict';

	angular.module('myApp')
		.factory('voiceMicSpeech', voiceMicSpeechFactory)
		.directive('voiceMic', voiceMicDirective)
		.run(voiceMicAutoScan);

	// ─── Shared SpeechRecognition wrapper ────────────────────────────
	voiceMicSpeechFactory.$inject = ['$rootScope'];
	function voiceMicSpeechFactory($rootScope) {
		var SR = window.SpeechRecognition || window.webkitSpeechRecognition;
		var recognition = null;
		var activeId = null;
		var activeWriter = null;
		var firstChunk = true;
		var listeners = {};
		var isListening = false;
		var startTimer = null;
		var startAttempts = 0;

		function clearStartTimer() {
			if (startTimer) {
				window.clearTimeout(startTimer);
				startTimer = null;
			}
		}

		function startActiveRecognition(delay) {
			var rec = ensure();
			if (!rec || !activeId || isListening || startTimer) return;

			if (delay) {
				startTimer = window.setTimeout(function () {
					startTimer = null;
					startActiveRecognition(0);
				}, delay);
				return;
			}

			try {
				rec.start();
			} catch (e) {
				if (activeId && startAttempts < 6) {
					startAttempts++;
					startActiveRecognition(250);
					return;
				}
				stop();
			}
		}

		function ensure() {
			if (!SR) return null;
			if (recognition) return recognition;
			recognition = new SR();
			recognition.lang = 'en-IN';
			recognition.continuous = true;
			recognition.interimResults = false;

			recognition.onstart = function () {
				isListening = true;
				startAttempts = 0;
				if (activeId) notify(activeId, true);
			};

			recognition.onresult = function (event) {
				var transcript = '';
				for (var i = event.resultIndex; i < event.results.length; i++) {
					if (event.results[i].isFinal) {
						transcript += event.results[i][0].transcript;
					}
				}
				if (!transcript) return;
				if (firstChunk) {
					transcript = transcript.charAt(0).toUpperCase() + transcript.slice(1);
					firstChunk = false;
				}
				if (activeWriter) {
					try {
						activeWriter(transcript + ' ');
					} catch (e) { /* swallow */ }
				}
			};

			recognition.onend = function () {
				isListening = false;
				if (activeId && listeners[activeId]) {
					startActiveRecognition(250);
				}
			};

			recognition.onerror = function (e) {
				if (window.console && console.warn) {
					console.warn('voice-mic speech error', e && e.error);
				}
				// 'aborted' is expected when another mic session takes over.
				if (e && e.error === 'aborted') {
					stop();
					return;
				}
				stop();
			};

			return recognition;
		}

		function notify(id, recording) {
			var fn = listeners[id];
			if (fn) fn(recording);
		}

		function stop() {
			var prev = activeId;
			clearStartTimer();
			activeId = null;
			activeWriter = null;
			firstChunk = true;
			isListening = false;
			startAttempts = 0;
			if (recognition) {
				try { recognition.stop(); } catch (e) {}
			}
			if (prev) notify(prev, false);
		}

		return {
			isSupported: function () { return !!SR; },
			register: function (id, onStateChange) {
				listeners[id] = onStateChange;
			},
			unregister: function (id) {
				if (activeId === id) stop();
				delete listeners[id];
			},
			toggle: function (id, writer) {
				if (!ensure()) {
					alert('Speech recognition is not supported in this browser.');
					return;
				}
				if (activeId === id) {
					stop();
					return;
				}
				if (activeId) {
					stop();
				}
				$rootScope.$broadcast('coversheetSpeech:stop', 'voice-mic');
				activeId = id;
				activeWriter = writer;
				firstChunk = true;
				startAttempts = 0;
				startActiveRecognition(0);
			},
			forceStop: function () {
				stop();
			},
			isActive: function (id) { return activeId === id; }
		};
	}

	// ─── Per-element directive ───────────────────────────────────────
	voiceMicDirective.$inject = ['$timeout', 'voiceMicSpeech'];
	function voiceMicDirective($timeout, voiceMicSpeech) {
		var seq = 0;
		return {
			restrict: 'A',
			require: '?ngModel',
			link: function (scope, element, attrs, ngModel) {
				if (!ngModel) return; // need a model to write to
				if (!inCoversheetRoot(element[0])) return;
				if (element[0].dataset.voiceMicAttached === '1') return;
				element[0].dataset.voiceMicAttached = '1';

				var id = 'vmic_' + (++seq);
				var btn = document.createElement('span');
				btn.className = 'voice-mic-btn';
				btn.setAttribute('role', 'button');
				btn.setAttribute('tabindex', '0');
				btn.setAttribute('title', 'Start dictation');
				btn.setAttribute('aria-label', 'Toggle voice dictation');

				wrapElement(element[0], btn);

				btn.addEventListener('click', function (ev) {
					ev.preventDefault();
					ev.stopPropagation();
					focusField(element[0]);
					voiceMicSpeech.toggle(id, function (chunk) {
						scope.$applyAsync(function () {
							var current = ngModel.$viewValue || element[0].value || '';
							ngModel.$setViewValue((current + chunk));
							ngModel.$render();
						});
					});
				});

				btn.addEventListener('keydown', function (ev) {
					if (ev.key === 'Enter' || ev.key === ' ') {
						ev.preventDefault();
						btn.click();
					}
				});

				voiceMicSpeech.register(id, function (recording) {
					if (recording) {
						btn.classList.add('recording');
						btn.setAttribute('title', 'Stop dictation');
					} else {
						btn.classList.remove('recording');
						btn.setAttribute('title', 'Start dictation');
					}
				});

				scope.$on('$destroy', function () {
					voiceMicSpeech.unregister(id);
				});
			}
		};
	}

	function wrapElement(el, btn) {
		// Build: <span class="voice-mic-wrap">[el][btn]</span>
		if (el.parentNode && el.parentNode.classList && el.parentNode.classList.contains('voice-mic-wrap')) {
			el.parentNode.appendChild(btn);
			return;
		}
		var wrap = document.createElement('span');
		wrap.className = 'voice-mic-wrap';
		el.parentNode.insertBefore(wrap, el);
		wrap.appendChild(el);
		wrap.appendChild(btn);
	}

		function focusField(el) {
			try {
				el.focus({ preventScroll: true });
			} catch (e) {
				try { el.focus(); } catch (ignore) {}
			}
		}

	function inCoversheetRoot(el) {
		return !!(el && el.closest && el.closest('.opd-ipd-cover-sheet, .ipd-cover-sheet'));
	}

	// ─── Auto-scan for coversheet pages ──────────────────────────────
	voiceMicAutoScan.$inject = ['$rootScope', '$timeout', '$state', 'voiceMicSpeech'];
	function voiceMicAutoScan($rootScope, $timeout, $state, voiceMicSpeech) {
		var seq = 0;
		// Only attempt on OPD and IPD coversheet states.
		var COVERSHEET_STATES = {
			'OPDCoverSheet': true,
			'IPDCoverSheet': true
		};

		// Only auto-attach to free-text inputs whose ng-model name suggests
		// free-form prose. Numbers, codes, dates, typeaheads are skipped.
		var FREE_TEXT_RX = /(remark|notes?|description|advice|comment|diagnosis|reason|chief|complaint|observation|history)/i;
		var NEVER_MIC_RX = /(patient\s*name|patientname|patientsearch|patientsearchobj|uhid|mobile\s*no|mobileno|identification|birth\s*date|birthdate)/i;

		function isManualMicNode(node) {
			return !!(node && node.matches && node.matches('.opd-voice-control, .voice-mic-btn, .ipd-dictation-voice-slot, .opd-editor-voice-slot, .ipd-editor-voice-slot, i.fa-microphone[ng-click], i.fa-stop[ng-click]'));
		}

		function hasManualMic(el) {
			var parent = el.parentNode;
			if (!parent) return false;
			var isManualWrapper = parent.classList && (
				parent.classList.contains('opd-dictation-field') ||
				parent.classList.contains('ipd-dictation-field') ||
				parent.classList.contains('opd-editor-dictation-row') ||
				parent.classList.contains('ipd-editor-dictation-row')
			);
			for (var child = parent.firstElementChild; child; child = child.nextElementSibling) {
				if (child === el) continue;
				if (isManualMicNode(child)) return true;
				if (isManualWrapper && child.querySelector && child.querySelector('.opd-voice-control, .voice-mic-btn, i.fa-microphone[ng-click], i.fa-stop[ng-click]')) return true;
			}
			return false;
		}

		function fieldHint(el) {
			var hint = [
				el.getAttribute('ng-model') || '',
				el.getAttribute('name') || '',
				el.getAttribute('id') || '',
				el.getAttribute('placeholder') || '',
				el.getAttribute('title') || '',
				el.getAttribute('aria-label') || ''
			].join(' ');
			var node = el.parentNode;
			var depth = 0;
			while (node && depth < 3) {
				if (node.querySelector) {
					var label = node.querySelector('label');
					if (label) hint += ' ' + label.textContent;
				}
				node = node.parentNode;
				depth++;
			}
			return hint;
		}

		function shouldAttachInput(el) {
			if (!el || !el.tagName) return false;
			if (!inCoversheetRoot(el)) return false;
			if (el.disabled || el.readOnly) return false;
			if (el.dataset.voiceMicAttached === '1') return false;
			if (hasManualMic(el)) return false;
			if (el.hasAttribute('ck-editor')) return false;
			if (NEVER_MIC_RX.test(fieldHint(el))) return false;
			if (el.tagName === 'TEXTAREA') return true;
			if (el.tagName !== 'INPUT') return false;
			var type = (el.getAttribute('type') || 'text').toLowerCase();
			if (type !== 'text') return false;
			if (el.hasAttribute('uib-typeahead')) return false;
			if (el.hasAttribute('numbers-only')) return false;
			return FREE_TEXT_RX.test(fieldHint(el));
		}

		function attach(el) {
			if (el.dataset.voiceMicAttached === '1') return;
			var $el = angular.element(el);
			var ngModel = $el.controller('ngModel');
			if (!ngModel) return;
			var scope = $el.scope();
			if (!scope) return;
			el.dataset.voiceMicAttached = '1';

			var id = 'vmic_auto_' + (++seq);
			var btn = document.createElement('span');
			btn.className = 'voice-mic-btn';
			btn.setAttribute('role', 'button');
			btn.setAttribute('tabindex', '0');
			btn.setAttribute('title', 'Start dictation');
			btn.setAttribute('aria-label', 'Toggle voice dictation');
			wrapElement(el, btn);

			btn.addEventListener('click', function (ev) {
				ev.preventDefault();
				ev.stopPropagation();
				focusField(el);
				voiceMicSpeech.toggle(id, function (chunk) {
					scope.$applyAsync(function () {
						var current = ngModel.$viewValue || el.value || '';
						ngModel.$setViewValue(current + chunk);
						ngModel.$render();
					});
				});
			});

			btn.addEventListener('keydown', function (ev) {
				if (ev.key === 'Enter' || ev.key === ' ') {
					ev.preventDefault();
					btn.click();
				}
			});

			voiceMicSpeech.register(id, function (recording) {
				if (recording) {
					btn.classList.add('recording');
					btn.setAttribute('title', 'Stop dictation');
				} else {
					btn.classList.remove('recording');
					btn.setAttribute('title', 'Start dictation');
				}
			});

			scope.$on('$destroy', function () {
				voiceMicSpeech.unregister(id);
			});
		}

		function scan(rootEl) {
			if (!rootEl || !voiceMicSpeech.isSupported()) return;
			var nodes = rootEl.querySelectorAll('textarea, input[type="text"], input:not([type])');
			angular.forEach(nodes, function (el) {
				if (!shouldAttachInput(el)) return;
				try { attach(el); } catch (e) { /* ignore */ }
			});
		}

		function activeCoversheetRoot() {
			var currentName = $state && $state.current && $state.current.name;
			if (!COVERSHEET_STATES[currentName]) return null;
			return document.querySelector('.opd-ipd-cover-sheet, .ipd-cover-sheet');
		}

		function cleanupOutsideCoversheets() {
			var wraps = document.querySelectorAll('.voice-mic-wrap');
			angular.forEach(wraps, function (wrap) {
				var field = wrap.querySelector('textarea, input');
				if (inCoversheetRoot(wrap) && field && !NEVER_MIC_RX.test(fieldHint(field))) return;
				if (field) {
					delete field.dataset.voiceMicAttached;
					wrap.parentNode.insertBefore(field, wrap);
				}
				wrap.parentNode.removeChild(wrap);
			});

			var buttons = document.querySelectorAll('.voice-mic-btn');
			angular.forEach(buttons, function (button) {
				if (!inCoversheetRoot(button) && button.parentNode) button.parentNode.removeChild(button);
			});
		}

		function scheduleScan() {
			$timeout(function () {
				cleanupOutsideCoversheets();
				var root = activeCoversheetRoot();
				if (!root) return;
				scan(root);
			}, 250);
		}

		function scheduleCleanup() {
			$timeout(cleanupOutsideCoversheets, 250);
		}

		$rootScope.$on('$stateChangeSuccess', function (ev, toState) {
			if (toState && COVERSHEET_STATES[toState.name]) {
				scheduleScan();
				// Cover sub-tabs that lazy-render on click
				$timeout(scheduleScan, 800);
				$timeout(scheduleScan, 2000);
			} else {
				voiceMicSpeech.forceStop();
				scheduleCleanup();
			}
		});

		$rootScope.$on('coversheetSpeech:stop', function (ev, source) {
			if (source !== 'voice-mic') {
				voiceMicSpeech.forceStop();
			}
		});

		document.addEventListener('visibilitychange', function () {
			if (document.hidden) {
				voiceMicSpeech.forceStop();
			}
		});

		// Re-scan whenever Angular finishes a digest after a tab click
		// (cheap because shouldAttachInput short-circuits on the dataset flag)
		$rootScope.$on('$viewContentLoaded', scheduleScan);

		// Manual hook: call window.__rescanVoiceMic() from any controller after
		// loading dynamic markup if needed.
		window.__rescanVoiceMic = scheduleScan;
	}
})();
