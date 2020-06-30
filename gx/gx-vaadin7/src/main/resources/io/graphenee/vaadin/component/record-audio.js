var gxMediaRecorder;
var gxAudioElement;
var gxAudioData;

function gxStartRecording() {
	navigator.mediaDevices.getUserMedia({ audio: true })
	  .then(stream => {
		gxMediaRecorder = new MediaRecorder(stream);
		gxMediaRecorder.start(3000);

		const chunkReader = new FileReader();
		chunkReader.onloadend = function() {
	        const base64data = chunkReader.result;                
	        io.graphenee.vaadin.component.record_audio.uploadChunk(base64data);
	    }
		
		const fileReader = new FileReader();
		fileReader.onloadend = function() {
	        const base64data = fileReader.result;                
	        io.graphenee.vaadin.component.record_audio.uploadFile(base64data);
	    }
		
	    const audioChunks = [];
	    gxMediaRecorder.addEventListener("dataavailable", event => {
	      audioChunks.push(event.data);
	      chunkReader.readAsDataURL(event.data);
	    });

	    gxMediaRecorder.addEventListener("stop", () => {
	      gxAudioData = new Blob(audioChunks);
	      gxAudioElement = new Audio(URL.createObjectURL(gxAudioData));
	      fileReader.readAsDataURL(gxAudioData);
	    });
	});
}

function gxStopRecording() {
	if(gxMediaRecorder !== null) {
		gxMediaRecorder.stop();
	}
}

function gxPlayAudio() {
	if(gxAudioElement !== null) {
		gxAudioElement.play();
	}
}

function gxStopAudio() {
	if(gxAudioElement !== null) {
		gxAudioElement.stop();
	}
}

function gxDeleteAudio() {
	if(gxAudioElement !== null) {
		gxAudioElement.remove();
		gxAudioElement = null;
	}
}