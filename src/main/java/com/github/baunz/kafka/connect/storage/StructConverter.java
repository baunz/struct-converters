package com.github.baunz.kafka.connect.storage;

import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaAndValue;
import org.apache.kafka.connect.data.SchemaBuilder;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.errors.DataException;
import org.apache.kafka.connect.storage.Converter;
import org.apache.kafka.connect.storage.ConverterType;
import org.apache.kafka.connect.storage.StringConverterConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * Convert primitive values from and to ConnectData with a struct schema (as opposed to the matching primitive type)
 */
public class StructConverter implements Converter {

    private final Converter converter;
    private String fieldName;
    private Schema schema;

    public StructConverter(Converter converter) {
        this.converter = converter;
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        Map<String, Object> configWithType = new HashMap<>(configs);
        configWithType.put(StringConverterConfig.TYPE_CONFIG, isKey ? ConverterType.KEY.getName() : ConverterType.VALUE.getName());
        this.fieldName = new StructConverterConfig(configWithType).fieldName();
        this.schema = new SchemaBuilder(Schema.Type.STRUCT).field(fieldName, Schema.OPTIONAL_STRING_SCHEMA).build();
        converter.configure(configs, isKey);
    }

    @Override
    public byte[] fromConnectData(String topic, Schema schema, Object value) {
        if (schema == null || schema.type() != Schema.Type.STRUCT) {
            throw new DataException("Expecting a struct record with single field named" + fieldName);
        }
        Struct struct = (Struct) value;
        if (!struct.schema().equals(schema))
            throw new DataException("Mismatching schema.");
        return converter.fromConnectData(topic, schema, struct.get(fieldName));
    }

    @Override
    public SchemaAndValue toConnectData(String topic, byte[] value) {
        Struct record = new Struct(schema);
        record.put(fieldName, converter.toConnectData(topic, value).value());
        return new SchemaAndValue(schema, record);
    }
}
