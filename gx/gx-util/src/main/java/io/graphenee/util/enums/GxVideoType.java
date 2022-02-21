/*******************************************************************************
 * Copyright (c) 2016, 2018 Farrukh Ijaz
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package io.graphenee.util.enums;

public enum GxVideoType {
    WEBM_VIDEO("video/webm", "webm"),
    MP4("video/mp4", "mp4"),
    M4V("video/m4v", "m4v"),
    MPEG("video/mpeg", "mpeg"),
    OGG_VIDEO("video/ogg", "ogg");

    private String mimeType;

    private String extension;

    private GxVideoType(String mimeType, String extension) {
        this.mimeType = mimeType;
        this.extension = extension;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public static GxVideoType findByMimeType(String mimeType) {
        if (mimeType == GxVideoType.WEBM_VIDEO.mimeType)
            return GxVideoType.WEBM_VIDEO;
        if (mimeType == GxVideoType.MP4.mimeType)
            return GxVideoType.MP4;
        if (mimeType == GxVideoType.MPEG.mimeType)
            return GxVideoType.MPEG;
        if (mimeType == GxVideoType.M4V.mimeType)
            return GxVideoType.M4V;
        if (mimeType == GxVideoType.OGG_VIDEO.mimeType)
            return GxVideoType.OGG_VIDEO;

        return null;
    }

}
