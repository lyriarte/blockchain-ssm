function ssmToVis(ssm) {
	var visData = {
		nodes: [],
		edges: []
	};
	
	var nbNodes = 0;
	ssm.transitions.map(function(trans) {
		nbNodes = Math.max(nbNodes,Math.max(trans.to+1,trans.from+1));
		visData.edges.push({
			from: trans.from,
			to: trans.to,
			label: trans.role + ": " + trans.action,
			arrows:'to'
		});
	});
	
	for (var i=0; i<nbNodes; i++)
		visData.nodes.push({id:i, label: i});
	
	return visData;
}

function ssmView(ssm, canvas) {
	var options = {
		"physics": {
			"enabled": true,
			"solver": "repulsion",
			"repulsion": {
				"damping": 0.9,
				"springConstant": 0.1,
				"springLength": 300,
				"nodeDistance": 100
			}
		}
	};
	var data = ssmToVis(ssm);
	var network = new vis.Network(canvas, data, options);
}
