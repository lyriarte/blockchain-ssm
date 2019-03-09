function ssmQuery(fcn, id) {
	var hostCmd = {
		cmd: "query",
		fcn: fcn,
		args: [id]
	}

	return hostCmd;
}

function flattenPublicKey(pub) {
	var pubLst = pub.split("\n");
	var res = "";
	pubLst.map(function(str) {
		if (str != "" && str.indexOf("PUBLIC KEY") == -1)
			res += str;
	});
	return res;
}

