var JSE = new JSEncrypt({default_key_size: 2048});

function ssmRegister(user, admin, adminKey) {
	JSE.setPrivateKey(adminKey);
	var userStr = JSON.stringify(user);
	var signStr = JSE.sign(userStr, CryptoJS.SHA256, "sha256");
	var hostCmd = {
		cmd: "invoke",
		fcn: "register",
		args: [userStr, admin, signStr]
	}

	return hostCmd;
}

function ssmCreate(ssm, admin, adminKey) {
	var hostCmd = {
		cmd: "invoke",
		fcn: "create",
		args: [ssm, admin]
	}

	return hostCmd;
}

function ssmStart(session, admin, adminKey) {
	var hostCmd = {
		cmd: "invoke",
		fcn: "start",
		args: [session, admin]
	}

	return hostCmd;
}

function ssmPerform(action, context, user, userKey) {
	var hostCmd = {
		cmd: "invoke",
		fcn: "perform",
		args: [action, context, user]
	}

	return hostCmd;
}

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

