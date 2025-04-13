package com.importservice.utils.cvsrecord;

import java.util.Iterator;
import java.util.List;

import com.importservice.model.CsvRecord;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

import java.io.Reader;
import java.util.NoSuchElementException;

/**
 * A streaming reader for CSV data that converts each record into a CsvRecord instance.
 * Designed for high-performance and low-memory usage using uniVocity.
 */

public class CsvRecordReader implements Iterator<CsvRecord> {

    private final CsvParser parser;
    private final List<String> headers;
    private final Iterator<String[]> rowIterator;

    public CsvRecordReader(Reader reader) {
        CsvParserSettings settings = new CsvParserSettings();
        settings.setHeaderExtractionEnabled(true);
        settings.setMaxCharsPerColumn(10000);
        settings.setInputBufferSize(4096);
        settings.setLineSeparatorDetectionEnabled(true);

        this.parser = new CsvParser(settings);
        this.parser.beginParsing(reader);
        this.headers = List.of(parser.getContext().headers());

        this.rowIterator = new Iterator<>() {
            String[] nextRow = parser.parseNext();

            @Override
            public boolean hasNext() {
                return nextRow != null;
            }

            @Override
            public String[] next() {
                if (nextRow == null) throw new NoSuchElementException();
                String[] current = nextRow;
                nextRow = parser.parseNext();
                return current;
            }
        };
    }

    @Override
    public boolean hasNext() {
        return rowIterator.hasNext();
    }

    @Override
    public CsvRecord next() {
        String[] row = rowIterator.next();
        CsvRecord cvsRecord = new CsvRecord();
        for (int i = 0; i < headers.size(); i++) {
            String header = headers.get(i);
            String value = (i < row.length) ? row[i] : null;
            cvsRecord.put(header, value);
        }
        return cvsRecord;
    }

    public void close() {
        parser.stopParsing();
    }
}
