package com.fofa.fofa_full_search.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

public class XlsxExporter {
    public static <T> void exportTableToXlsx(File file, TableView<T> table) throws IOException {
        ObservableList<T> items = table.getItems();
        if (items.isEmpty()) {
            return;
        }

        // 创建 xlsx 文件
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Sheet1");

            // 写入表头
            Row headerRow = sheet.createRow(0);
            List<String> headers = table.getColumns().stream()
                    .map(column -> column.getText())
                    .collect(Collectors.toList());
            for (int i = 0; i < headers.size(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers.get(i));
            }

            // 写入数据
            for (int rowNumber = 1; rowNumber <= items.size(); rowNumber++) {
                Row row = sheet.createRow(rowNumber);
                T item = items.get(rowNumber - 1);
                for (int colNumber = 0; colNumber < table.getColumns().size(); colNumber++) {
                    Cell cell = row.createCell(colNumber);
                    cell.setCellValue(table.getColumns().get(colNumber).getCellData(item).toString());
                }
            }

            // 将数据写入文件
            try (FileOutputStream outputStream = new FileOutputStream(file)) {
                workbook.write(outputStream);
            }
        }
    }
}

