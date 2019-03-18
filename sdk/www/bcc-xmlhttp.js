/*
 * example XMLHttpRequest client for the bcc-rest server
 */

function bccHostCmd(uri, cmd, fcn, args, onOk, onError) {
	
	var cbctx = {
		onOk: onOk,
		onError: onError
	};

	var xmlhttp = new XMLHttpRequest();
	xmlhttp.onreadystatechange = function() {
		if (this.readyState != 4)
			return;
		if (this.status != 200) {
			if (cbctx.onError)
				cbctx.onError(this.responseText);
		}
		else {
			if (cbctx.onOk)
				cbctx.onOk(this.responseText);
		}
	};

	var query = "cmd=" + encodeURIComponent(cmd) + "&fcn=" + encodeURIComponent(fcn);
	args.map(function(arg) {query += "&args=" + encodeURIComponent(arg);});

	xmlhttp.open("GET", uri + "?" + query, true);
	xmlhttp.send();
	return cbctx;
}
