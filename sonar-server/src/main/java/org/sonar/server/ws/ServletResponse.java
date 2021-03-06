/*
 * SonarQube, open source software quality management tool.
 * Copyright (C) 2008-2013 SonarSource
 * mailto:contact AT sonarsource DOT com
 *
 * SonarQube is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * SonarQube is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.server.ws;

import org.sonar.api.server.ws.Response;
import org.sonar.api.utils.text.JsonWriter;
import org.sonar.api.utils.text.XmlWriter;
import org.sonar.server.plugins.MimeTypes;

import javax.annotation.CheckForNull;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class ServletResponse implements Response {

  public static class ServletStream implements Stream {
    private String mediaType;
    private int httpStatus = 200;
    private final ByteArrayOutputStream output = new ByteArrayOutputStream();

    @CheckForNull
    public String mediaType() {
      return mediaType;
    }

    public int httpStatus() {
      return httpStatus;
    }

    @Override
    public ServletStream setMediaType(String s) {
      this.mediaType = s;
      return this;
    }

    @Override
    public ServletStream setStatus(int httpStatus) {
      this.httpStatus = httpStatus;
      return this;
    }

    @Override
    public OutputStream output() {
      return output;
    }

    public String outputAsString() {
      return output.toString();
    }

    public ServletStream reset() {
      output.reset();
      return this;
    }
  }

  private final ServletStream stream = new ServletStream();

  @Override
  public JsonWriter newJsonWriter() {
    stream.setMediaType(MimeTypes.JSON);
    return JsonWriter.of(new OutputStreamWriter(stream.output()));
  }

  @Override
  public XmlWriter newXmlWriter() {
    stream.setMediaType(MimeTypes.XML);
    return XmlWriter.of(new OutputStreamWriter(stream.output()));
  }

  @Override
  public ServletStream stream() {
    return stream;
  }

  @Override
  public Response noContent() {
    stream.setStatus(204);
    return this;
  }
}
