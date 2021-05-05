/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.baunz.kafka.connect.storage;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigDef.Importance;
import org.apache.kafka.common.config.ConfigDef.Type;
import org.apache.kafka.common.config.ConfigDef.Width;
import org.apache.kafka.connect.storage.ConverterConfig;

import java.util.Map;

/**
 * Configuration options for {@link StructConverter} instances.
 */
public class StructConverterConfig extends ConverterConfig {

    public static final String FIELD_NAME_CONFIG = "fieldname";
    public static final String FIELD_NAME_DEFAULT = "value";
    private static final String FIELD_NAME_DOC = "The name of the field that contains the string value in the resulting struct.";
    private static final String FIELD_NAME_DISPLAY = "Field name to use for the struct";

    public static ConfigDef configDef() {
        return ConverterConfig.newConfigDef().define(FIELD_NAME_CONFIG, Type.STRING, FIELD_NAME_DEFAULT, Importance.HIGH, FIELD_NAME_DOC, null, 0, Width.MEDIUM, FIELD_NAME_DISPLAY);
    }

    public StructConverterConfig(Map<String, ?> props) {
        super(configDef(), props);
    }

    String fieldName() {
        return getString(FIELD_NAME_CONFIG);
    }
}
