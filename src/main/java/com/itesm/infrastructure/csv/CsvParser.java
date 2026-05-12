package com.itesm.infrastructure.csv;

import com.itesm.application.dto.MedicineRowDto;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CsvParser {

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
                    throw new RuntimeException("Fila " + rowNumber + " incompleta: se esperaban 5 columnas");
                }

                rows.add(new MedicineRowDto(
                        line[0].trim(), // nombre_generico
                        line[1].trim(), // forma_dosis
                        line[2].trim(), // potencia
                        line[3].trim(), // presentacion
                        Integer.parseInt(line[4].trim()) // stock
                ));
            }

        } catch (CsvValidationException | IOException e) {
            throw new RuntimeException("Error al leer el archivo CSV: " + e.getMessage());
        }

        return rows;
    }
}