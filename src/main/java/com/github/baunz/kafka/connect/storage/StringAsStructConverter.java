package com.github.baunz.kafka.connect.storage;

import org.apache.kafka.connect.storage.StringConverter;

public class StringAsStructConverter extends StructConverter {
    public StringAsStructConverter() {
        super(new StringConverter());
    }
}
