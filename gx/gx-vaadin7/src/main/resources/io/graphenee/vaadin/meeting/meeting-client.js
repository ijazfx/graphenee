let configuration = {
    "iceServers": [
        {
            "url": "stun:stun.l.google.com:19302",
            "urls": [
                "stun:stun.l.google.com:19302",
                "stun:global.stun.twilio.com:3478?transport=udp"
            ]
        }
    ]
}

let offerOptions = {
    offerToReceiveAudio: 1,
    offerToReceiveVideo: 1
};

let ws = null;
let pc = null;
let localUserId = null;

// initialize websockets...
function initializeWebSocket(wsurl, userId) {
    localUserId = userId;
    if (ws)
        return;
    ws = new WebSocket(wsurl);
    // when a new message will arrive...
    ws.onmessage = function (m) {
        let message = JSON.parse(m.data);
        if (message.event == "candidate") {
            handleCandidate(message.data);
        } else if (message.event == "request-offer") {
            createOffer();
        } else if (message.event == "offer") {
            handleOffer(message.data);
        } else if (message.event == "answer") {
            handleAnswer(message.data);
        } else {
            console.log("Invalid Message", message);
        }
    };
    // when socket will be closed...
    ws.onclose = function (e) {
        console.log('Socket is closed. Reconnect will be attempted in 5 second.', e.reason);
        setTimeout(function () {
            ws = null;
            initializeWebSocket(wsurl);
        }, 5000);
    };
    // when an error will occur...
    ws.onerror = function (err) {
        console.error('Socket encountered error: ', err.message);
        // ws.close();
    };
}

// called when websocket will receive candidate
async function handleCandidate(candidate) {
    pc = createPeer();
    try {
        await pc.addIceCandidate(new RTCIceCandidate(candidate));
    } catch (error) {
        console.log(error);
    }
}

// called when websocket will receive offer from any of the connected peer,
// typically from the host peer.
async function handleOffer(offer) {
    try {
        document.getElementById('remoteVideo').srcObject = null;
    } catch (error) {
        console.log(error);
    }
    let pc = createPeer();
    let session = new RTCSessionDescription(offer);
    await pc.setRemoteDescription(session);
    try {
        let answer = await pc.createAnswer();
        await pc.setLocalDescription(answer);
        ws.send(JSON.stringify({
            event: "answer",
            userId: localUserId,
            data: answer
        }));
    } catch (error) {
        console.log("Error", error);
    }
}

// called when websocket will receive answer from any of the connected peer whom
// offer was sent, typically from a joining peer.
async function handleAnswer(answer) {
    pc = createPeer();
    try {
        await pc.setRemoteDescription(new RTCSessionDescription(answer));
    } catch (error) {
        console.log(error);
    }
}

// creates a peer connection if does not already exist. Each peer on the meeting
// must have a connection for other peers.
function createPeer() {
    if (pc != null) {
        console.log("Using existing peer");
        return pc;
    }
    console.log("Creating new peer");
    pc = initializePeerConnection();
    return pc;
}

// called when join button is clicked on the meeting component.
async function createOffer() {
    pc = createPeer();
    try {
        let offer = await pc.createOffer(offerOptions);
        await pc.setLocalDescription(offer);
        ws.send(JSON.stringify({
            event: "offer",
            userId: localUserId,
            data: offer
        }));
    } catch (error) {
        console.log("Error", error);
    }
}

function joinMeeting() {
    pc = createPeer();
    try {
        ws.send(JSON.stringify({
            event: "joining",
            userId: localUserId,
        }));
    } catch (error) {
        console.log("Error", error);
    }
}

function leaveMeeting() {
    pc = createPeer();
    try {
        ws.send(JSON.stringify({
            event: "leaving",
            userId: localUserId,
        }));
        pc.close();
        pc = null;
    } catch (error) {
        console.log("Error", error);
    }
}

// creates a new peer connection for userId and videoId
function initializePeerConnection() {
    pc = new RTCPeerConnection(configuration);
    let isNegotiating = false;
    // is called when new candidate is discovered
    pc.onicecandidate = function (event) {
        if (event.candidate) {
            ws.send(JSON.stringify({
                event: "candidate",
                userId: localUserId,
                data: event.candidate
            }));
        }
    };
    // called when allow button is clicked on any of the media stream.
    pc.onnegotiationneeded = function (event) {
        if (isNegotiating)
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
        let remoteVideo = document.getElementById('remoteVideo');
        remoteVideo.srcObject = event.stream;
    }

    pc.ontrack = function (event) {
        if (event.track) {
            let remoteVideo = document.getElementById('remoteVideo');
            let remoteStream = remoteVideo.srcObject;
            if (remoteStream == null) {
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
    let constraints = {
        video: true,
        audio: true
    }
    try {
        navigator.mediaDevices.getUserMedia(constraints).then(stream => {
            let myVideo = document.getElementById('localVideo');
            if (myVideo.srcObject != null) {
                myVideo.srcObject.getTracks().forEach(track => track.stop());
            }
            myVideo.muted = true;
            myVideo.srcObject = stream;
            stream.getTracks().forEach(track => {
                pc.addTrack(track, stream);
            });
        }).catch(reason => {
            console.log(reason);
        });
    } catch (error) {
        console.log(error);
    }
}

// called when screen button is clicked on the meeting component.
async function startScreen() {
    try {
        navigator.mediaDevices.getDisplayMedia().then(stream => {
            let myVideo = document.getElementById('localVideo');
            if (myVideo.srcObject != null) {
                myVideo.srcObject.getTracks().forEach(track => track.stop());
            }
            myVideo.muted = true;
            myVideo.srcObject = stream;
            stream.getTracks().forEach(track => {
                pc.addTrack(track, stream);
            });
        }).catch(reason => {
            console.log(reason);
        });
    } catch (error) {
        console.log(error);
    }
}
