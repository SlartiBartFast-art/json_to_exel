package org.converter.servise;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.*;
import org.converter.model.Depo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class ConvertToExel {

    //    private static final String FILE_NAME = "F:/ConvertExel/MySecondExcel.xlsx";
    private String filePath = null;

    private Map<Integer, String> stringMap = new HashMap<>();

    private Map<Integer, Depo> depoMap = new HashMap();

    public ConvertToExel(Map<Integer, Depo> depoMap, String filePath) {
        this.depoMap = depoMap;
        this.filePath = filePath;
        initMap();
    }

    private void initMap() {
        this.stringMap.put(1, "id");
        this.stringMap.put(2, "date_time");
        this.stringMap.put(3, "type");
        this.stringMap.put(4, "sender_id");
        this.stringMap.put(5, "sender name");
        this.stringMap.put(6, "recipient_id");
        this.stringMap.put(7, "recipient name");
        this.stringMap.put(8, "amount");
        this.stringMap.put(9, "fee");
        this.stringMap.put(10, "currency_label");
    }
//public void convertToEl(Map<Integer, Depo> map) {
    public void convertToEl() {
        XSSFWorkbook workbook = new XSSFWorkbook(); // создание книги
        XSSFSheet sheet = workbook.createSheet("Datatypes in Java"); // создание страницы


        int rowNum = 0; //строка
        int colNum1 = 0;
        System.out.println("Creating excel");
        Row row1 = sheet.createRow(rowNum++); // строка
        CellStyle style=null;

        XSSFFont defaultFont= workbook.createFont();
        defaultFont.setFontHeightInPoints((short)10);
        defaultFont.setFontName("Arial");
        defaultFont.setColor(IndexedColors.BLACK.getIndex());
        defaultFont.setBold(false);
        defaultFont.setItalic(false);

        XSSFFont font= workbook.createFont();
        font.setFontHeightInPoints((short)10);
        font.setFontName("Arial");
        font.setColor(IndexedColors.WHITE.getIndex());
        font.setBold(true);
        font.setItalic(false);

        style=row1.getRowStyle();
//        style.setFillBackgroundColor(IndexedColors.DARK_BLUE.getIndex());
//        style.setFillPattern(CellStyle.class. .SOLID_FOREGROUND);
//        style.setAlignment(CellStyle.ALIGN_CENTER);
//        style.setFont(font);
        for (int i = 1; i < stringMap.size() + 1; i++) {
            Cell cell = row1.createCell(i - 1); // ячейка
            cell.setCellValue((String) stringMap.get(i));
        }
        for (var datatype : depoMap.values()) {
            Row row = sheet.createRow(rowNum++); // строка
            int colNum = 0;

            Cell cell = row.createCell(colNum++); // ячейка
            cell.setCellValue((Integer) datatype.getId());
            Cell cell1 = row.createCell(colNum++); // ячейка
            sheet.autoSizeColumn(1);
//            cell1.setCellValue((String)  datatype.getDate() + " " + datatype.getTime());
            cell1.setCellValue((String)  timeRevers(datatype.getDate(), datatype.getTime()));
            Cell cellT = row.createCell(colNum++); // ячейка
            cellT.setCellValue((String) datatype.getType());
            Cell cell3 = row.createCell(colNum++); // ячейка
            cell3.setCellValue((Integer) datatype.getSender().getId());
            Cell cell4 = row.createCell(colNum++); // ячейка
            cell4.setCellValue((String) datatype.getSender().getName());
            Cell cell5 = row.createCell(colNum++); // ячейка
            cell5.setCellValue((Integer) datatype.getRecipient().getId());
            Cell cell6 = row.createCell(colNum++); // ячейка
            cell6.setCellValue((String) datatype.getRecipient().getName());
            Cell cell7 = row.createCell(colNum++); // ячейка
            cell7.setCellValue((Double) datatype.getAmount());
            Cell cell8 = row.createCell(colNum++); // ячейка
            cell8.setCellValue((Double) datatype.getFee());
            Cell cell9 = row.createCell(colNum++); // ячейка
            cell9.setCellValue((String) datatype.getCurrency().getLabel());
        }
        String path = filePath + convertT() + ".xlsx";
        System.out.println("Path file -> " + path);
        try (FileOutputStream outputStream = new FileOutputStream(path)) {
            workbook.write(outputStream);
            workbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Done");
    }

    private String convertT() {
        LocalDateTime localDateTime = LocalDateTime.now();
        String rsl = null;
        var t = localDateTime.toString().split("T");
        var y = t[1].split(":");
        rsl = t[0] + "_" + y[0] + "-" + y[1];
        return rsl;
    }

    private String timeRevers(String one, String two) {
        String rsl = null;
        var t = one.split("-");
        var y = t[0].substring(2);
        rsl = t[2] + "-" + t[1] + "-" + y + " " + two;
        System.out.println("timeRevers() -> :" + rsl);
        return rsl;
    }

    public static void main(String[] args) {
        String string = "F:/ConvertExel/";
        File file = new File("F:\\ConvertExel\\exampleTZ.json");
        ConvertJson convertJson = new ConvertJson();
        convertJson.convertJs(file);
        ConvertToExel convertToExel = new ConvertToExel(convertJson.getDepoMap(), string);
        convertToExel.convertToEl();
    }
}
