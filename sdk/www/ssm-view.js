function ssmToVis(ssm) {
	var nodes = [];
	var edges = [];
	
	var nbNodes = 0;
	ssm.transitions.map(function(trans) {
		nbNodes = Math.max(nbNodes,Math.max(trans.to+1,trans.from+1));
		edges.push({
			from: trans.from,
			to: trans.to,
			label: trans.role + ": " + trans.action,
			arrows:'to'
		});
	});
	
	for (var i=0; i<nbNodes; i++)
		nodes.push({id:i, label: i});
	
	var visData = {
		nodes: new vis.DataSet(nodes),
		edges: new vis.DataSet(edges),
		ssm: ssm,
		nbNodes: nbNodes
	};

	visData.nodes.on('*', function (event, properties, senderId) {
		console.log('---- nodes event:', event, 'properties:', properties, 'senderId:', senderId);
		if (event == 'add') {
			visData.nodes.update({id:properties.items[0], label:visData.nbNodes++});
		}
		visData.nodes.forEach(function(item){console.log(item.toSource())});
	});
	visData.edges.on('*', function (event, properties, senderId) {
		console.log('---- edges event:', event, 'properties:', properties, 'senderId:', senderId);
		if (event == 'add') {
			newEdge = visData.edges.get(properties.items[0]);
			fromLabel = visData.nodes.get(newEdge.from).label;
			toLabel = visData.nodes.get(newEdge.to).label;
			visData.edges.update({id:properties.items[0], arrows:"to"});
			visData.ssm.transitions.push({from: fromLabel, to: toLabel, role: "", action: ""});
		}
		visData.edges.forEach(function(item){console.log(item.toSource())});
		console.log(JSON.stringify(visData.ssm,null,2));
	});

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
		},
		"manipulation": {
			"enabled": true
		}
	};
	var data = ssmToVis(ssm);
	var network = new vis.Network(canvas, data, options);
	return network;
}
