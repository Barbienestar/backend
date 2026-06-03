package com.itesm.infrastructure.csv;

import com.itesm.application.dto.MedicineRowDto;
import com.itesm.domain.exceptions.CsvParsingException;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CsvParser {
    private CsvParser() {
        throw new IllegalStateException("Utility class");
    }

    public static List<MedicineRowDto> parse(InputStream inputStream) {
        List<MedicineRowDto> rows = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new InputStreamReader(inputStream))) {
            // Saltar la primera línea que es el encabezado
            reader.readNext();

            String[] line;
            int rowNumber = 1;

            while ((line = reader.readNext()) != null) {
                rowNumber++;

                if (line.length < 5) {
                    throw new CsvParsingException("Fila " + rowNumber + " incompleta: se esperaban 5 columnas");
                }

                rows.add(parseRow(line, rowNumber));
            }

        } catch (CsvValidationException | IOException e) {
            throw new CsvParsingException("Error al leer el archivo CSV: " + e.getMessage());
        }

        return rows;
    }

    private static MedicineRowDto parseRow(String[] line, int rowNumber) {
        try {
            return new MedicineRowDto(
                    line[0].trim(),
                    line[1].trim(),
                    nullIfEmpty(line[2]),
                    nullIfEmpty(line[3]),
                    Integer.parseInt(line[4].trim()));
        } catch (NumberFormatException e) {
            throw new CsvParsingException(
                    "Fila " + rowNumber + ": el campo 'stock' no es un número válido: '" + line[4].trim() + "'");
        }
    }

    private static String nullIfEmpty(String value) {
        return (value == null || value.trim().isEmpty()) ? null : value.trim();
    }
}
