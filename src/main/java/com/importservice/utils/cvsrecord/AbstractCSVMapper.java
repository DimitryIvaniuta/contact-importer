package com.importservice.utils.cvsrecord;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.Map;

@Slf4j
public abstract class AbstractCSVMapper<T> {

    /**
     * Maps a CSV record to the target entity.
     *
     * @param csvRecord the CSV record
     * @return the mapped entity instance
     */
    public T map(CSVRecord csvRecord) {
        T instance = createInstance();
        Map<String, String> headerToFieldMap = getHeaderToFieldMap();

        for (Map.Entry<String, String> entry : headerToFieldMap.entrySet()) {
            String csvHeader = entry.getKey();
            String methodName = entry.getValue();

            if (csvRecord.isMapped(csvHeader)) {
                String value = csvRecord.getValue(csvHeader);
                try {
                    Method method = instance.getClass().getMethod(methodName, String.class);
                    method.invoke(instance, value);
                } catch (Exception e) {
                    log.info("Failed to map header '{}' with method '{}': {}\n",
                            csvHeader, methodName, e.getMessage());
                }
            }
        }
        return instance;
    }

    /**
     * Creates an instance of the target type.
     */
    protected abstract T createInstance();

    /**
     * Provides the mapping between CSV headers and setter method names.
     */
    protected abstract Map<String, String> getHeaderToFieldMap();
}
