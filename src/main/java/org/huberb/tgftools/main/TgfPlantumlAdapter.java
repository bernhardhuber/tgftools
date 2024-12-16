/*
 * Copyright 2024 pi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.huberb.tgftools.main;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import net.sourceforge.plantuml.ErrorUml;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.Option;
import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.eggs.QuoteUtils;
import net.sourceforge.plantuml.error.PSystemError;
import net.sourceforge.plantuml.error.PSystemErrorUtils;
import net.sourceforge.plantuml.security.SFile;
import net.sourceforge.plantuml.text.StringLocated;
import net.sourceforge.plantuml.utils.LineLocationImpl;
import net.sourceforge.plantuml.version.Version;

import static java.nio.charset.StandardCharsets.UTF_8;
import static net.sourceforge.plantuml.ErrorUmlType.SYNTAX_ERROR;

/**
 *
 * @author pi
 */
public class TgfPlantumlAdapter {

    static class Response {

        final List<Entry<String, String>> headerList;
        byte[] content;

        public Response() {
            this.content = null;
            this.headerList = new ArrayList<>();
        }

        public List<Entry<String, String>> getHeaderList() {
            return headerList;
        }

        public byte[] getContent() {
            return content;
        }

        public boolean isError() {
            boolean anError = false;
            anError = anError || this.content == null;
            anError = anError || this.content.length <= 10;
            anError = anError || (headerList.stream()
                    .anyMatch(e -> e.getKey().contains("X-PlantUML-Diagram-Error")));
            return anError;
        }

        private void addHeader(String k, String v, boolean ignore) {
            if (!ignore) {
                addHeader(k, v);
            }
        }

        private void addHeader(String k, String v) {
            Entry<String, String> e = new HashMap.SimpleEntry<>(k, v);
            this.headerList.add(e);
        }

        private void storeContent(byte[] theContent) {
            this.content = theContent;
        }

    }

    /**
     * Generate PNG from plantuml source.
     *
     * @param plantuml
     * @return
     * @throws Exception
     */
    public Response process(String plantuml) throws Exception {
        String renderRequestgetSource = plantuml;
        String[] options = new String[0];
        final Option option = new Option(options);

        final String source = renderRequestgetSource.startsWith("@start") ? renderRequestgetSource
                : "@startuml\n" + renderRequestgetSource + "\n@enduml";

        //
        final SFile newCurrentDir = option.getFileDir() == null ? null : new SFile(option.getFileDir());
        final SourceStringReader ssr = new SourceStringReader(
                option.getDefaultDefines(),
                source, UTF_8,
                option.getConfig(),
                newCurrentDir);
        try (final ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            final Diagram system;
            final ImageData imageData;

            if (ssr.getBlocks().isEmpty()) {
                system = PSystemErrorUtils.buildV2(null,
                        new ErrorUml(SYNTAX_ERROR, "No valid @start/@end found, please check the version", 0, new LineLocationImpl("", null)), null,
                        Collections.<StringLocated>emptyList());
                imageData = ssr.noValidStartFound(baos, option.getFileFormatOption());
            } else {
                system = ssr.getBlocks().get(0).getDiagram();
                imageData = system.exportDiagram(baos, 0, option.getFileFormatOption());
            }

            Response response = new Response();
            sendDiagram(response, system, option.getFileFormatOption(), "200", imageData, baos.toByteArray());
            return response;
        }
    }

    private void sendDiagram(Response response, final Diagram system,
            final FileFormatOption fileFormatOption,
            final String returnCode,
            final ImageData imageData,
            final byte[] fileData) throws IOException {

        response.addHeader("status", "HTTP/1.1 " + returnCode);
        response.addHeader("Cache-Control", "no-cache", true);
        response.addHeader("Server", "PlantUML PicoWebServer " + Version.versionString());
        response.addHeader("Date:", "" + new Date(), true);
        response.addHeader("Access-Control-Allow-Origin", "*", true);
        response.addHeader("Content-type", "" + fileFormatOption.getFileFormat().getMimeType());
        response.addHeader("Content-length", "" + fileData.length);
        response.addHeader("X-PlantUML-Diagram-Width", "" + imageData.getWidth());
        response.addHeader("X-PlantUML-Diagram-Height", "" + imageData.getHeight());
        response.addHeader("X-PlantUML-Diagram-Description", "" + system.getDescription().getDescription());
        if (system instanceof PSystemError) {
            final PSystemError error = (PSystemError) system;
            for (ErrorUml err : error.getErrorsUml()) {
                response.addHeader("X-PlantUML-Diagram-Error", "" + err.getError());
                response.addHeader("X-PlantUML-Diagram-Error-Line", "" + (1 + err.getLineLocation().getPosition()));
            }
        }
        if (system.getTitleDisplay() != null && system.getTitleDisplay().size() == 1) {
            final String encode = URLEncoder.encode(system.getTitleDisplay().asList().get(0).toString(), "UTF-8");
            if (encode.length() < 256) {
                response.addHeader("X-PlantUML-Diagram-Title", "" + encode);
            }
        }

        response.addHeader("X-Patreon", "Support us on https://plantuml.com/patreon", true);
        response.addHeader("X-Donate", "https://plantuml.com/paypal", true);
        response.addHeader("X-Quote", "" + StringUtils.rot(QuoteUtils.getSomeQuote()));
        response.storeContent(fileData);
    }

}
