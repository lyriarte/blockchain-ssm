function ssmQuery(fcn, id) {
	var hostCmd = {
		cmd: "query",
		fcn: fcn,
		args: [id]
	}

	return hostCmd;
}
