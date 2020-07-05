var audioPlayer;

window.gxPlyrInit = function(id) {
	audioPlayer = new Plyr(document.getElementById(id));
}

window.gxPlyrSetAudioUrl = function(id, url) {
	document.getElementById(id).src = url;
}