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
package org.sonar.core.component.db;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.resources.Qualifiers;
import org.sonar.core.component.ComponentQuery;
import org.sonar.core.persistence.AbstractDaoTestCase;

import static org.fest.assertions.Assertions.assertThat;

public class ComponentDaoTest extends AbstractDaoTestCase {

  private ComponentDao dao;

  @Before
  public void createDao() throws Exception {
    dao = new ComponentDao(getMyBatis());
  }

  @Test
  public void should_select_component() {
    setupData("selectComponent");
    assertThat(dao.selectComponent(ComponentQuery.create())).hasSize(3);
    assertThat(dao.selectComponent(ComponentQuery.create().addIds(1L))).hasSize(1);
    assertThat(dao.selectComponent(ComponentQuery.create().addQualifiers(Qualifiers.PROJECT))).hasSize(2);
    assertThat(dao.selectComponent(ComponentQuery.create().addQualifiers(Qualifiers.PROJECT, Qualifiers.VIEW))).hasSize(3);
    assertThat(dao.selectComponent(ComponentQuery.create().addIds(1L).addQualifiers(Qualifiers.PROJECT))).hasSize(1);
    assertThat(dao.selectComponent(ComponentQuery.create().addIds(1L).addQualifiers(Qualifiers.VIEW))).hasSize(0);
  }
}
