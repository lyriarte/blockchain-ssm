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

	var query = "cmd=" + cmd + "&fcn=" + fcn;
	args.map(function(arg) {query += "&args=" + arg;});
	query = query.replace(/\+/g,'%2B')

	xmlhttp.open("GET", uri + "?" + query, true);
	xmlhttp.send();
	return cbctx;
}
