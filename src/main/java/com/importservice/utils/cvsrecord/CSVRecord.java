package com.importservice.utils.cvsrecord;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.LinkedHashMap;
import java.util.Map;

@ToString
@EqualsAndHashCode
public class CSVRecord {

    private final Map<String, String> values;

    public CSVRecord() {
        this.values = new LinkedHashMap<>();
    }

    public void put(String key, String value) {
        this.values.put(key, value);
    }

    public String getValue(String key) {
        return this.values.get(key);
    }

    public boolean containsKey(String key) {
        return this.values.containsKey(key);
    }

    public Map<String, String> getAll() {
        return new LinkedHashMap<>(this.values);
    }

    public boolean isMapped(String key) {
        return key != null && !key.trim().isEmpty() && this.values.containsKey(key);
    }

}
