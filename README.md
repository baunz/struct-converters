# struct-converters

Convert schemaless, primitive kafka messages into ConnectData

## Why?

Most sink connectors in kafka-connect require a schema to insert the data. When dealing with topics that contain plain JSON records it's not possible to insert them them without an inline schema, as explained [here](https://rmoff.net/2020/01/22/kafka-connect-and-schemas/)

Sometimes it is just not possible to change the JSON records to include the schema and we are okay with having the json "as-is" in the sink system. This still allows a "schema-on-read"-strategy, supported by RDBMS like MySql and Postgres with their JSON data types.
 
 ## Installation
 
 Copy a release jar from this website into your [connect plugin path](https://docs.confluent.io/home/connect/userguide.html#installing-kconnect-plugins)
 
 ## Configuration
 
Given this example json
 
 ```
record-key: 456
record-value: {
   "id" : "11eb50e4-e3b5-f40f-b709-36bc5ee27958",
   "shopId" : 2001,
   "origin" : "space",
   "type" : "like",
   "createDate" : "2021-01-12T13:34:16.653Z",
   "payload" : {
     "profileId" : "11eb50e4-e3b5-f40f-b709-36bc5ee27958",
    }
}
 ```

and the following table created in the sink db

```
create table `super_event` (
  `ID` VARCHAR(255) NOT NULL ,
  `VALUE` JSON ,
   PRIMARY KEY (ID)
) ENGINE = InnoDB
```

The following connector configuration 
 
 ```
connector.class= io.confluent.connect.jdbc.JdbcSinkConnector
topics=super_event
key.converter=org.apache.kafka.connect.storage.StringConverter
value.converter=org.baunz.kafka.connect.StringAsStructConverter
connection.url=jdbc=mysql=//awesome-db/
insert.mode= UPSERT,
pk.mode=record_key
pk.fields=id
```

creates a connector that fills the target table 
 
 ## Further 