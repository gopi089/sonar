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
package org.sonar.wsclient.rule.internal;

import org.json.simple.JSONValue;
import org.sonar.wsclient.internal.HttpRequestFactory;
import org.sonar.wsclient.rule.RuleTagClient;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Do not instantiate this class, but use {@link org.sonar.wsclient.SonarClient#ruleTagClient()}.
 */
public class DefaultRuleTagClient implements RuleTagClient {

  private static final String ROOT_URL = "/api/rule_tags";
  private static final String LIST_URL = ROOT_URL + "/list";
  private static final String CREATE_URL = ROOT_URL + "/create";

  private final HttpRequestFactory requestFactory;

  public DefaultRuleTagClient(HttpRequestFactory requestFactory) {
    this.requestFactory = requestFactory;
  }

  @Override
  @SuppressWarnings("unchecked")
  public Collection<String> list() {
    String json = requestFactory.get(LIST_URL, Collections.<String, Object> emptyMap());
    return (Collection<String>) JSONValue.parse(json);
  }

  @Override
  public void create(String tag) {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("tag", tag);
    requestFactory.post(CREATE_URL, params);
  }
}
