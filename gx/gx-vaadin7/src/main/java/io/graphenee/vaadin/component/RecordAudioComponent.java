package io.graphenee.vaadin.component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

import com.vaadin.annotations.JavaScript;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.JavaScriptFunction;
import com.vaadin.ui.themes.ValoTheme;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import elemental.json.JsonArray;
import io.graphenee.util.enums.GxAudioType;
import io.graphenee.util.exception.GxMediaConversionException;
import io.graphenee.util.media.GxFfmpegMediaConverterImpl;
import io.graphenee.util.media.GxMediaConverter;

@SuppressWarnings("serial")
@JavaScript({ "record-audio.js" })
public class RecordAudioComponent extends MHorizontalLayout {

    private static final Logger L = LoggerFactory.getLogger(RecordAudioComponent.class);

    private File audioFile;
    volatile FileOutputStream audioFileOutputStream;
    private RecordAudioDelegate delegate;

    public RecordAudioComponent() {
        this(FontAwesome.MICROPHONE, FontAwesome.STOP);
    }

    public RecordAudioComponent(Resource recordIcon, Resource stopIcon) {
        MButton startRecordingButton = new MButton("Record").withIcon(recordIcon).withStyleName(ValoTheme.BUTTON_SMALL);
        MButton stopRecordingButton = new MButton("Stop").withIcon(stopIcon).withStyleName(ValoTheme.BUTTON_SMALL).withVisible(false);

        startRecordingButton.addClickListener(cl -> {
            try {
                if (audioFile != null && audioFile.exists())
                    audioFile.delete();
                audioFileOutputStream = null;
                audioFile = File.createTempFile("recording", "webm");
            } catch (IOException e) {
                e.printStackTrace();
            }
            // record audio
            com.vaadin.ui.JavaScript.eval("gxStartRecording()");
            startRecordingButton.setVisible(false);
            stopRecordingButton.setVisible(true);
        });

        stopRecordingButton.addClickListener(cl -> {
            // stop recording
            com.vaadin.ui.JavaScript.eval("gxStopRecording()");
            stopRecordingButton.setVisible(false);
            startRecordingButton.setVisible(true);
        });

        CssLayout buttonLayout = new CssLayout();
        buttonLayout.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);

        buttonLayout.addComponents(startRecordingButton, stopRecordingButton);

        addComponents(buttonLayout);

        // To be called when recording is complete.
        com.vaadin.ui.JavaScript.getCurrent().addFunction("io.graphenee.vaadin.component.record_audio.recordingStopped", new JavaScriptFunction() {

            @Override
            public void call(JsonArray arguments) {
                if (audioFileOutputStream != null) {
                    try {
                        audioFileOutputStream.close();
                    } catch (IOException e) {
                        L.warn(e.getMessage());
                    }
                }
                if (delegate != null && audioFile != null && audioFile.exists()) {
                    String filename = audioFile.getName() + ".webm";
                    File sourceFile = new File(audioFile.getParentFile(), filename);
                    audioFile.renameTo(sourceFile);

                    GxMediaConverter mediaConverter = new GxFfmpegMediaConverterImpl();
                    try {
                        File targetFile = new File(audioFile.getParentFile(), UUID.randomUUID().toString() + ".mp3");
                        mediaConverter.convertAudioMedia(sourceFile.getAbsolutePath(), targetFile.getAbsolutePath(), GxAudioType.MP3);
                        delegate.onAudioAvailable(targetFile);
                    } catch (GxMediaConversionException e) {
                        L.warn("File: " + sourceFile.getName(), e);
                        File targetFile = new File(sourceFile.getParentFile(), UUID.randomUUID().toString() + ".webm");
                        sourceFile.renameTo(targetFile);
                        delegate.onAudioAvailable(targetFile);
                    }
                }
            }

        });

        /*
         * To be used as this is efficient to transfer smaller chucks of data while
         * recording in progress. However, we may need to close the stream when
         * recording is stopped which can be notified using recordingStopped method.
         */
        com.vaadin.ui.JavaScript.getCurrent().addFunction("io.graphenee.vaadin.component.record_audio.uploadChunk", new JavaScriptFunction() {

            @Override
            public void call(JsonArray arguments) {
                if (audioFileOutputStream == null) {
                    synchronized (RecordAudioComponent.this) {
                        if (audioFileOutputStream == null) {
                            try {
                                audioFileOutputStream = new FileOutputStream(audioFile);
                            } catch (FileNotFoundException e) {
                                L.warn(e.getMessage());
                            }
                        }
                    }
                }
                String content = arguments.getString(0);
                String[] parts = content.split("base64,");
                if (audioFileOutputStream != null) {
                    try {
                        byte[] decoded = Base64.getDecoder().decode(parts[1].getBytes());
                        audioFileOutputStream.write(decoded);
                    } catch (Exception e) {
                        L.warn(e.getMessage());
                    }
                }
            }
        });
    }

    public RecordAudioDelegate getDelegate() {
        return delegate;
    }

    public void setDelegate(RecordAudioDelegate delegate) {
        this.delegate = delegate;
    }

    public static interface RecordAudioDelegate {
        void onAudioAvailable(File audioFile);
    }

}
