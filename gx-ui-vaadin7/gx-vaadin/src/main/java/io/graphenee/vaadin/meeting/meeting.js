var configuration = {
    'iceServers': [
        {
            'urls': 'stun:stun.l.google.com:19302'
        }
    ]
}

var offerOptions = {
	offerToReceiveAudio: 1,
	offerToReceiveVideo: 1
};

var localStream = null;
var ws = null;
var peers = new Map();
var videos = new Map();

// initialize websockets...
function initializeWebSocket(wsurl, userId, videoId) {
	var pc = createPeer(userId, videoId);
	if(ws)
		return;
	ws = new WebSocket(wsurl);
	// when a new message will arrive...
	ws.onmessage = function (m) {
	    var message = JSON.parse(m.data);
	    if (message.event == "candidate") {
	        handleCandidate(userId, videoId, message.data, message.userId, message.videoId);
	    } else if (message.event == 'request-offer') {
	    	handleRequestOffer(userId, videoId, message.userId, message.videoId);
	    } else if (message.event == "offer") {
	        handleOffer(userId, videoId, message.data, message.userId, message.videoId);
	    } else if (message.event == "answer") {
	        handleAnswer(userId, videoId, message.data, message.userId, message.videoId);
	    } else {
	        console.log("Invalid Message", message);
	    }
	};
	// when socket will be closed...
	ws.onclose = function(e) {
	    console.log('Socket is closed. Reconnect will be attempted in 5 second.', e.reason);
	    setTimeout(function() {
	      ws = null;
	      initializeWebSocket(wsurl, userId, videoId);
	    }, 5000);
	};
	// when an error will occur...
	ws.onerror = function(err) {
	    console.error('Socket encountered error: ', err.message);
	    // ws.close();
	};
}

// called when websocket will receive candidate
function handleCandidate(localUserId, localVideoId, candidate, userId, videoId) {
	var pc = createPeer(localUserId, localVideoId);
    pc.addIceCandidate(new RTCIceCandidate(candidate)).then(() => console.log(userId + ' candidate')).catch(err => console.log(err));
}

// called when websocket will receive request-offer by any of the connecting
// peer, typically from a joining peer.
function handleRequestOffer(localUserId, localVideoId, userId, videoId) {
	var pc = createPeer(localUserId, localVideoId);
	pc.createOffer(offerOptions).then(offer => {
        pc.setLocalDescription(offer);
        ws.send(JSON.stringify({
            event: "offer",
            userId: localUserId,
            videoId: localVideoId,
            data: offer
        }));
    }).catch(error => {
        console.log("Error", error);
    });
}

// called when websocket will receive offer from any of the connected peer,
// typically from the host peer.
function handleOffer(localUserId, localVideoId, offer, userId, videoId) {
	var pc = createPeer(localUserId, localVideoId);
	var session = new RTCSessionDescription(offer);
    pc.setRemoteDescription(session);
    pc.createAnswer().then(answer => {
        pc.setLocalDescription(answer);
        ws.send(JSON.stringify({
            event: "answer",
            userId: localUserId,
            videoId: localVideoId,
            data: answer
        }));
    }).catch(error => {
        console.log("Error", error);
    });
}

// called when websocket will receive answer from any of the connected peer whom
// offer was sent, typically from a joining peer.
function handleAnswer(localUserId, localVideoId, answer, userId, videoId) {
    var pc = createPeer(localUserId, localVideoId);
    pc.setRemoteDescription(new RTCSessionDescription(answer));
}

// creates a peer connection if does not already exist. Each peer on the meeting
// must have a connection for other peers.
function createPeer(userId, videoId) {
	var pc = peers.get(userId);
	if(pc == null) {
		pc = initializePeerConnection(userId, videoId);
		peers.set(userId, pc);
		videos.set(pc, videoId);
	}
	return pc;
}

// called when join button is clicked on the meeting component.
function requestOffer(userId, videoId) {
	ws.send(JSON.stringify({
        event: "request-offer",
        userId: userId,
        videoId: videoId,
    }));
}

// creates a new peer connection for userId and videoId
function initializePeerConnection(userId, videoId) {
    var pc = new RTCPeerConnection(configuration);
    var isNegotiating = false;
    // is called when new candidate is discovered
    pc.onicecandidate = function (event) {
    	if(event.candidate) {
	        ws.send(JSON.stringify({
	            event: "candidate",
	            userId: userId,
	            videoId: videoId,
	            data: event.candidate
	        }));
    	}
    };
    // called when allow button is clicked on any of the media stream.
    pc.onnegotiationneeded = function (event) {
    	if(isNegotiating)
    		return;
    	isNegotiating = true;
        pc.createOffer(offerOptions).then(offer => {
            pc.setLocalDescription(offer);
            ws.send(JSON.stringify({
                event: "offer",
                userId: userId,
                videoId: videoId,
                data: offer
            }));
        }).catch(error => {
            console.log("Error", error);
        });
    };
    // Workaround for Chrome: skip nested negotiations
    pc.onsignalingstatechange = (e) => { 
    	isNegotiating = (pc.signalingState != "stable");
    }
    // called when a stream is added on the remote peer whose ice candidate is
	// known.
    pc.onaddstream = function (event) {
        var remoteVideo = videos.get(pc);
        remoteVideo.srcObject = event.stream;
    }
    return pc;
}

// called when camera button is clicked on the meeting component.
function startCamera(userId, videoId) {
	var pc = createPeer(userId, videoId);
	var constraints = {
		video : true,
		audio : true
	}
	navigator.getUserMedia(constraints, function(stream) {
		localStream = stream;
		var myVideo = document.getElementById(videoId);
		myVideo.muted = true;
		myVideo.srcObject = localStream;
		pc.addStream(localStream);
	}, function(error) {
		console.log("Error", error);
	});
}

// called when screen button is clicked on the meeting component.
async function startScreen(userId, videoId) {
	var pc = createPeer(userId, videoId);
	var stream = await navigator.mediaDevices.getDisplayMedia();
	localStream = stream;
	var myVideo = document.getElementById(videoId);
	myVideo.muted = true;
	myVideo.srcObject = localStream;
	pc.addStream(localStream);
}