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

package org.sonar.server.startup;

import org.junit.Test;
import org.sonar.api.utils.ValidationMessages;
import org.sonar.core.technicaldebt.DebtCharacteristicsSynchronizer;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class RegisterDebtCharacteristicModelTest {

  @Test
  public void create_model() throws Exception {
    DebtCharacteristicsSynchronizer synchronizer = mock(DebtCharacteristicsSynchronizer.class);
    RegisterDebtCharacteristicModel sqaleDefinition = new RegisterDebtCharacteristicModel(synchronizer);

    sqaleDefinition.start();

    verify(synchronizer, times(1)).synchronize(any(ValidationMessages.class));
  }
}
