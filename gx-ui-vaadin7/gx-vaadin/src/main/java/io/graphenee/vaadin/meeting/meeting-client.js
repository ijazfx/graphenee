var configuration = {
    'iceServers': [
        {
            'urls': 'stun:stun.l.google.com:19302'
        }
    ]
}

const offerOptions = {
	offerToReceiveAudio: 1,
	offerToReceiveVideo: 1
};

var localStream = null;
var ws = null;
var localUserId = null;
var pc = null;

// initialize websockets...
function initializeWebSocket(wsurl, userId) {
	localUserId = userId;
	if(ws)
		return;
	ws = new WebSocket(wsurl);
	// when a new message will arrive...
	ws.onmessage = function (m) {
	    var message = JSON.parse(m.data);
	    if (message.event == "candidate") {
	        handleCandidate(message.data);
	    } else if (message.event == "request-offer") {
	        createOffer();
	    } else if (message.event == "answer") {
	        handleAnswer(message.data);
	    } else {
	        console.log("Invalid Message", message);
	    }
	};
	// when socket will be closed...
	ws.onclose = function(e) {
	    console.log('Socket is closed. Reconnect will be attempted in 5 second.', e.reason);
	    setTimeout(function() {
	      ws = null;
	      initializeWebSocket(wsurl);
	    }, 5000);
	};
	// when an error will occur...
	ws.onerror = function(err) {
	    console.error('Socket encountered error: ', err.message);
	    // ws.close();
	};
}

// called when websocket will receive candidate
async function handleCandidate(candidate) {
	pc = createPeer();
	try {
		await pc.addIceCandidate(new RTCIceCandidate(candidate));
	} catch(error) {
		console.log(error);
	}
}

// called when websocket will receive answer from any of the connected peer whom
// offer was sent, typically from a joining peer.
async function handleAnswer(answer) {
	pc = createPeer();
    try {
    	await pc.setRemoteDescription(new RTCSessionDescription(answer));    	
    } catch(error) {
    	console.log(error);
    }
}

// creates a peer connection if does not already exist. Each peer on the meeting
// must have a connection for other peers.
function createPeer() {
	if(pc == null) {
		pc = initializePeerConnection();
	}
	return pc;
}

// called when join button is clicked on the meeting component.
async function createOffer() {
	pc = createPeer();
	try {
		var offer = await pc.createOffer(offerOptions);
	    await pc.setLocalDescription(offer);
        ws.send(JSON.stringify({
            event: "offer",
            userId: localUserId,
            data: offer
        }));
    } catch(error){
        console.log("Error", error);
    }
}

// creates a new peer connection for userId and videoId
function initializePeerConnection() {
    pc = new RTCPeerConnection(configuration);
    var isNegotiating = false;
    // is called when new candidate is discovered
    pc.onicecandidate = function (event) {
    	if(event.candidate) {
	        ws.send(JSON.stringify({
	            event: "candidate",
	            userId: localUserId,
	            data: event.candidate
	        }));
    	}
    };
    // called when allow button is clicked on any of the media stream.
    pc.onnegotiationneeded = function (event) {
    	if(isNegotiating)
    		return;
    	isNegotiating = true;
        createOffer();
    };
    // Workaround for Chrome: skip nested negotiations
    pc.onsignalingstatechange = (e) => { 
    	isNegotiating = (pc.signalingState != "stable");
    }
    // called when a stream is added on the remote peer whose ice candidate is
	// known.
    pc.onaddstream = function (event) {
        var remoteVideo = document.getElementById('remoteVideo');
        remoteVideo.srcObject = event.stream;
    }
    
    pc.ontrack = function(event) {
    	if(event.track) {
    		var remoteVideo = document.getElementById('remoteVideo');
    		var remoteStream = remoteVideo.srcObject;
    		if(remoteStream == null) {
    	    	remoteStream = new MediaStream();
        	    remoteVideo.srcObject = remoteStream;
    	    }
    		remoteStream.addTrack(event.track, remoteStream);
    	}
    }
    return pc;
}

// called when camera button is clicked on the meeting component.
async function startCamera() {
	pc = createPeer();
	var constraints = {
		video : true,
		audio : true
	}
	try {
		var stream = await navigator.mediaDevices.getUserMedia(constraints);
	 	localStream = stream;
		var myVideo = document.getElementById('localVideo');
		myVideo.muted = true;
		myVideo.srcObject = localStream;
		createOffer();
		localStream.getTracks().forEach(track => {
		    pc.addTrack(track, localStream);
		});
	} catch(error) {
		console.log("Error", error);
	}
}

// called when screen button is clicked on the meeting component.
async function startScreen() {
	pc = createPeer();
	try {
		var stream = await navigator.mediaDevices.getDisplayMedia();
		localStream = stream;
		var myVideo = document.getElementById('localVideo');
		myVideo.muted = true;
		myVideo.srcObject = localStream;
		createOffer();
		localStream.getTracks().forEach(track => {
		    pc.addTrack(track, localStream);
		});
	} catch(error) {
		console.log(error);
	}
}