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
let peers = new Map();
let videos = new Map();
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
        if (message.event == "joining") {
            handleJoining(message.userId);
        } else if (message.event == "leaving") {
            handleLeaving(message.userId);
        } else if (message.event == "candidate") {
            handleCandidate(message.data, message.userId);
        } else if (message.event == "offer") {
            handleOffer(message.data, message.userId);
        } else if (message.event == "answer") {
            handleAnswer(message.data, message.userId);
        } else {
            console.log("Invalid Message", message);
        }
    };
    // when socket will be closed...
    ws.onclose = function (e) {
        console.log('Socket is closed. Reconnect will be attempted in 5 second.', e.reason);
        setTimeout(function () {
            ws = null;
            initializeWebSocket(wsurl, userId);
        }, 5000);
    };
    // when an error will occur...
    ws.onerror = function (err) {
        console.error('Socket encountered error: ', err.message);
        // ws.close();
    };
}

function handleJoining(userId) {
    let pc = createPeer(userId);
    console.log(userId, "joined");
    createOffer(userId);
    try {
        document.getElementById(createVideoId(userId) + "_container").style.display = 'inline-block';
    } catch (err) {

    }
}

function handleLeaving(userId) {
    let pc = peers.get(userId);
    videos.delete(pc);
    peers.delete(userId);
    console.log(userId, "left");
    pc.close();
    try {
        document.getElementById(createVideoId(userId) + "_container").style.display = 'none';
    } catch (err) {

    }
}

// called when websocket will receive candidate
async function handleCandidate(candidate, userId) {
    let pc = createPeer(userId);
    try {
        await pc.addIceCandidate(new RTCIceCandidate(candidate));
    } catch (error) {
        console.log(error);
    }
}

// called when join button is clicked on the meeting component.
async function createOffer(userId) {
    pc = createPeer(userId);
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

// called when websocket will receive offer from any of the connected peer,
// typically from the host peer.
async function handleOffer(offer, userId) {
    try {
        document.getElementById(createVideoId(userId)).srcObject = null;
    } catch (error) {
        console.log(error);
    }
    let pc = createPeer(userId);
    let session = new RTCSessionDescription(offer);
    await pc.setRemoteDescription(session);
    try {
        let answer = await pc.createAnswer();
        await pc.setLocalDescription(answer);
        ws.send(JSON.stringify({
            event: "answer",
            data: answer
        }));
    } catch (error) {
        console.log("Error", error);
    }
}

// called when websocket will receive answer from any of the connected peer whom
// offer was sent, typically from a joining peer.
async function handleAnswer(answer, userId) {
    pc = createPeer(userId);
    try {
        await pc.setRemoteDescription(new RTCSessionDescription(answer));
    } catch (error) {
        console.log(error);
    }
}

// creates a peer connection if does not already exist. Each peer on the meeting
// must have a connection for other peers.
function createPeer(userId) {
    let pc = peers.get(userId);
    if (pc != null) {
        console.log("Using peer for: " + userId)
        return pc;
    }
    console.log("Creating peer for: " + userId)
    pc = initializePeerConnection(userId);
    peers.set(userId, pc);
    videos.set(pc, createVideoId(userId));
    return pc;
}

// creates a new peer connection for userId and videoId
function initializePeerConnection(userId) {
    let pc = new RTCPeerConnection(configuration);
    let isNegotiating = false;
    // is called when new candidate is discovered
    pc.onicecandidate = function (event) {
        if (event.candidate) {
            ws.send(JSON.stringify({
                event: "candidate",
                data: event.candidate
            }));
        }
    };
    // called when allow button is clicked on any of the media stream.
    pc.onnegotiationneeded = function (event) {
        if (isNegotiating)
            return;
        isNegotiating = true;
        createOffer(userId);
    };
    // Workaround for Chrome: skip nested negotiations
    pc.onsignalingstatechange = (e) => {
        isNegotiating = (pc.signalingState != "stable");
    }
    // called when a stream is added on the remote peer whose ice candidate is
    // known.
    pc.onaddstream = function (event) {
        if (event.stream) {
            let remoteVideo = document.getElementById(videos.get(pc));
            remoteVideo.srcObject = event.stream;
        }
    }

    pc.ontrack = function (event) {
        if (event.track) {
            let remoteVideo = document.getElementById(videos.get(pc));
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
                peers.forEach(pc => pc.addTrack(track, stream));
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
                peers.forEach(pc => pc.addTrack(track, stream));
            });
        }).catch(reason => {
            console.log(reason);
        });
    } catch (error) {
        console.log(error);
    }
}

// mute all attendees...
function muteAllAttendees() {
    videos.forEach(v => {
        document.getElementById(v).muted = true;
    });
}

// unmute all attendees...
function unmuteAllAttendees() {
    videos.forEach(v => {
        document.getElementById(v).muted = false;
    });
}

// mute single attendee...
function muteAttendee(userId) {
    document.getElementById(createVideoId(userId)).muted = true;
}

//unmute single attendee...
function unmuteAttendee(userId) {
    document.getElementById(createVideoId(userId)).muted = false;
}

function createVideoId(userId) {
    return 'vid_' + userId.replace('-', '_').replace('.', '_');
}
