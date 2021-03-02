package com.github.baunz.kafka.connect.storage;

import org.apache.kafka.connect.converters.IntegerConverter;

public class IntegerAsStructConverter extends StructConverter {

    public IntegerAsStructConverter() {
        super(new IntegerConverter());
    }
}
