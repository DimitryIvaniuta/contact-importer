package com.importservice.model;


import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A flexible representation of a CSV record, preserving insertion order
 * and allowing dynamic access to values by column header.
 */
public class CsvRecord {

    private final Map<String, String> values;

    public CsvRecord() {
        this.values = new LinkedHashMap<>();
    }

    public CsvRecord(Map<String, String> values) {
        this.values = new LinkedHashMap<>(values);
    }

    public void put(String key, String value) {
        this.values.put(key, value);
    }

    public String get(String key) {
        return this.values.getOrDefault(key, null);
    }

    public boolean containsKey(String key) {
        return this.values.containsKey(key);
    }

    public boolean isMapped(String key) {
        return key != null && !key.trim().isEmpty() && this.values.containsKey(key);
    }

    public Map<String, String> getAll() {
        return new LinkedHashMap<>(this.values);
    }

    @Override
    public String toString() {
        return "CsvRecord" + values;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CsvRecord csvRecord = (CsvRecord) o;
        return Objects.equals(values, csvRecord.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(values);
    }

}
