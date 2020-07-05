function setClipboardText(text){
	
    var id = "temp-text-area";
    var textarea = document.createElement("textarea");
    textarea.id = "temp-text-area";
    document.querySelector("body").appendChild(textarea);
    
    var existsTextarea = document.getElementById(id);
    existsTextarea.value = text;
    existsTextarea.select();
    existsTextarea.setSelectionRange(0, 99999);
    document.execCommand('copy');
}