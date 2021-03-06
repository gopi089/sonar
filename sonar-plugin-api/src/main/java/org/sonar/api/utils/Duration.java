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

package org.sonar.api.utils;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @since 4.3
 */
public class Duration implements Serializable {

  public static final String DAY = "d";
  public static final String HOUR = "h";
  public static final String MINUTE = "min";

  private static final short MINUTES_IN_ONE_HOUR = 60;

  private final long durationInMinutes;

  private Duration(long durationInMinutes) {
    this.durationInMinutes = durationInMinutes;
  }

  private Duration(int days, int hours, int minutes, int hoursInDay) {
    this(((long) days * hoursInDay * MINUTES_IN_ONE_HOUR) + (hours * MINUTES_IN_ONE_HOUR) + minutes);
  }

  public static Duration create(long durationInMinutes) {
    return new Duration(durationInMinutes);
  }

  public static Duration decode(String text, int hoursInDay) {
    return new Duration(extractValue(text, DAY), extractValue(text, HOUR), extractValue(text, MINUTE), hoursInDay);
  }

  private static int extractValue(String text, String unit) {
    try {
      Pattern pattern = Pattern.compile("(\\d*?)\\D*" + unit);
      Matcher matcher = pattern.matcher(text);
      if (matcher.find()) {
        String daysString = matcher.group(1);
        return Integer.parseInt(daysString);
      }
      return 0;
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException(String.format("Duration '%s' is invalid, it should use the following sample format : 2d 10h 15min", text), e);
    }
  }

  public String encode(int hoursInDay) {
    int days = ((Double) ((double) durationInMinutes / hoursInDay / MINUTES_IN_ONE_HOUR)).intValue();
    Long remainingDuration = durationInMinutes - (days * hoursInDay * MINUTES_IN_ONE_HOUR);
    int hours = ((Double) (remainingDuration.doubleValue() / MINUTES_IN_ONE_HOUR)).intValue();
    remainingDuration = remainingDuration - (hours * MINUTES_IN_ONE_HOUR);
    int minutes = remainingDuration.intValue();

    StringBuilder stringBuilder = new StringBuilder();
    if (days > 0) {
      stringBuilder.append(days);
      stringBuilder.append(DAY);
    }
    if (hours > 0) {
      stringBuilder.append(hours);
      stringBuilder.append(HOUR);
    }
    if (minutes > 0) {
      stringBuilder.append(minutes);
      stringBuilder.append(MINUTE);
    }
    return stringBuilder.toString();
  }

  public long toMinutes() {
    return durationInMinutes;
  }

  public boolean isGreaterThan(Duration other){
    return toMinutes() > other.toMinutes();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Duration that = (Duration) o;
    if (durationInMinutes != that.durationInMinutes) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return (int) (durationInMinutes ^ (durationInMinutes >>> 32));
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
  }
}
