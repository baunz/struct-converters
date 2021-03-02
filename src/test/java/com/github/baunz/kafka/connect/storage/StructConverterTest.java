package com.github.baunz.kafka.connect.storage;

import org.apache.kafka.connect.data.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Collections;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
class StructConverterTest {

    private static final String TOPIC = "some-topic";
    private static final String CUSTOM_FIELD_NAME = "our-field";
    private static final String CUSTOM_FIELD_VALUE = "i am the value";

    private final StringAsStructConverter converter = new StringAsStructConverter();

    @BeforeAll
    void beforeAll() {
        converter.configure(Collections.singletonMap("converter.fieldname", CUSTOM_FIELD_NAME), true);
    }

    private Struct assertSchemaAndStruct(SchemaAndValue schemaAndValue) {

        assertThat(schemaAndValue)
                .isNotNull();
        Schema schema = schemaAndValue.schema();
        assertThat(schema).isNotNull();
        assertThat(schema.type()).isEqualTo(Schema.Type.STRUCT);
        assertThat(schema.fields()).hasSize(1);
        Field field = schema.field(CUSTOM_FIELD_NAME);
        assertThat(field).isNotNull();
        assertThat(field.schema().type()).isEqualTo(Schema.Type.STRING);

        Struct struct = (Struct) schemaAndValue.value();
        assertThat(struct).isNotNull();
        return struct;
    }

    @Test
    void mustConvertConnectDataStructToByteArray() {
        Schema schema = new SchemaBuilder(Schema.Type.STRUCT).field(CUSTOM_FIELD_NAME, Schema.OPTIONAL_STRING_SCHEMA).build();
        byte[] bytes = converter.fromConnectData(TOPIC, schema, new Struct(schema).put(CUSTOM_FIELD_NAME, CUSTOM_FIELD_VALUE));
        assertThat(bytes).isNotNull().asString(UTF_8).isEqualTo(CUSTOM_FIELD_VALUE);
    }

    @Test
    void mustConvertValueToStruct() {
        SchemaAndValue schemaAndValue = converter.toConnectData(TOPIC, CUSTOM_FIELD_VALUE.getBytes(UTF_8));
        Struct struct = assertSchemaAndStruct(schemaAndValue);
        assertThat(struct.getString(CUSTOM_FIELD_NAME)).isEqualTo(CUSTOM_FIELD_VALUE);
    }

    @Test
    void mustHandleNullValue() {
        SchemaAndValue schemaAndValue = converter.toConnectData(TOPIC, null);
        Struct struct = assertSchemaAndStruct(schemaAndValue);
        assertThat(struct.getString(CUSTOM_FIELD_NAME)).isNull();
    }
}