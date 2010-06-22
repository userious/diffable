/**
 * Copyright 2010 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
function DJSBootstrap(identifier, code) {
	this.code_ = code;
	this.identifier_ = identifier;
}

DJSBootstrap.prototype.bootstrap = function(bootstrap_version,
		                                    diff_url) {
	if (window['deltajs'][this.identifier_]['cv'] == bootstrap_version) {
		this.applyAndExecute();
	} else {
		var me = this;
		window['deltajs'][this.identifier_]['load'] = function() {
			me.applyAndExecute.apply(me, arguments);
		};
		var diffScript = document.createElement('script');
		diffScript.src = diff_url + this.identifier_ + "_" + 
		                 bootstrap_version + "_" +
		                 window['deltajs'][this.identifier_]['cv'] + ".diff";
		document.getElementsByTagName("head")[0].appendChild(diffScript);
	}
}

DJSBootstrap.prototype.globalEval_ = function(source) {
	if (window.execScript) {
		window.execScript(source);
	} else {
		var fn = function() { window.eval.call(window, source); };
		fn();
	}
}

DJSBootstrap.prototype.applyAndExecute = function(opt_delta) {
	var output = this.code_;
	if (opt_delta) {
		output = DJSBootstrap.apply_(this.code_, opt_delta);
	}
	this.globalEval_(output);
}

DJSBootstrap.apply_ = function(dict, diff) {
	var output = [];
	for (var i = 0; i < diff.length; i++) {
		if (typeof diff[i] == 'number') {
			output.push(
				dict.substring(diff[i], diff[i] + diff[i + 1]));
			++i;
		} else if (typeof diff[i] == 'string') {
			output.push(diff[i]);
		}
	}
	return output.join('');
}

var djs = new DJSBootstrap('{{DJS_RESOURCE_IDENTIFIER}}', {{DJS_CODE}});
djs.bootstrap('{{DJS_BOOTSTRAP_VERSION}}', '{{DJS_DIFF_URL}}')