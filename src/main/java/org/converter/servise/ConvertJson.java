package org.converter.servise;

import org.codehaus.jackson.map.ObjectMapper;
import org.converter.model.Deposit;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.util.*;

/**
 * Класс конвертирует JSON файл в локальное хранилище объектов POJO
 */
public class ConvertJson {

    private Map<Integer, Deposit> depoMap = new HashMap();

    public Map<Integer, Deposit> getDepoMap() {
        return depoMap;
    }

    public void setDepoMap(Map<Integer, Deposit> depoMap) {
        this.depoMap = depoMap;
    }

    public void convertJs(File filePath) {
        File file = filePath;
        String string = null;
        try (FileReader reader = new FileReader(file)) {
            JSONParser jsonParser = new JSONParser();
            JSONArray jsonObject = null;
            try {
                jsonObject = (JSONArray) jsonParser.parse(reader);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            } catch (ParseException parseException) {
                parseException.printStackTrace();
            }
            var toJSONString = jsonObject.toJSONString();
            var arrJ = toJSONString.split("\\[");

            var arrH = arrJ[1].split("\\]");

            String[] arrG = arrH[0].split("},\\{");

            ObjectMapper mapper = new ObjectMapper();

//JSON from String to Object
            String string1 = null;
            for (int i = 0; i < arrG.length; i++) {
                if (i == 0) {
                    string1 = arrG[i] + "}";
                }
                if (i == arrG.length - 1) {
                    string1 = "{" + arrG[i];
                }
                if (i != 0 && i != arrG.length - 1) {
                    string1 = "{" + arrG[i] + "}";
                }

                Deposit deposit1 = null;
                try {
                    deposit1 = mapper.readValue(string1, Deposit.class);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                depoMap.put(deposit1.getId(), deposit1);
                string1 = null;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
