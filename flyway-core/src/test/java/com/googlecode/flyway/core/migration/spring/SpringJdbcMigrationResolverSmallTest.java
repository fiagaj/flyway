/**
 * Copyright (C) 2010-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.googlecode.flyway.core.migration.spring;

import com.googlecode.flyway.core.api.MigrationInfo;
import com.googlecode.flyway.core.migration.ExecutableMigration;
import com.googlecode.flyway.core.migration.spring.dummy.V2__InterfaceBasedMigration;
import com.googlecode.flyway.core.migration.spring.dummy.Version3dot5;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test for SpringJdbcMigrationResolver.
 */
public class SpringJdbcMigrationResolverSmallTest {
    @Test
    public void resolveMigrations() {
        SpringJdbcMigrationResolver springJdbcMigrationResolver =
                new SpringJdbcMigrationResolver("com/googlecode/flyway/core/migration/spring/dummy");
        Collection<ExecutableMigration> migrations = springJdbcMigrationResolver.resolveMigrations();

        assertEquals(2, migrations.size());

        List<ExecutableMigration> migrationList = new ArrayList<ExecutableMigration>(migrations);
        Collections.sort(migrationList);

        assertEquals("2", migrationList.get(0).getInfo().getVersion().toString());
        assertEquals("3.5", migrationList.get(1).getInfo().getVersion().toString());

        assertEquals("InterfaceBasedMigration", migrationList.get(0).getInfo().getDescription());
        assertEquals("Three Dot Five", migrationList.get(1).getInfo().getDescription());

        assertNull(migrationList.get(0).getInfo().getChecksum());
        assertEquals(35, migrationList.get(1).getInfo().getChecksum().intValue());
    }

    @Test
    public void conventionOverConfiguration() {
        SpringJdbcMigrationResolver springJdbcMigrationResolver = new SpringJdbcMigrationResolver(null);
        MigrationInfo migrationInfo = springJdbcMigrationResolver.extractMigrationInfo(new V2__InterfaceBasedMigration());
        assertEquals("2", migrationInfo.getVersion().toString());
        assertEquals("InterfaceBasedMigration", migrationInfo.getDescription());
        assertNull(migrationInfo.getChecksum());
    }

    @Test
    public void explicitInfo() {
        SpringJdbcMigrationResolver springJdbcMigrationResolver = new SpringJdbcMigrationResolver(null);
        MigrationInfo migrationInfo = springJdbcMigrationResolver.extractMigrationInfo(new Version3dot5());
        assertEquals("3.5", migrationInfo.getVersion().toString());
        assertEquals("Three Dot Five", migrationInfo.getDescription());
        assertEquals(35, migrationInfo.getChecksum().intValue());
    }
}
