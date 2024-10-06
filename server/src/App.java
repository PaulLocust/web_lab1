import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fastcgi.FCGIInterface;
import org.json.JSONObject;

public class App {

    private static final String RESPONSE_TEMPLATE = "Content-Type: application/json\n" +
            "Content-Length: %d\n\n%s";

    public static void main(String[] args) {
        while (new FCGIInterface().FCGIaccept() >= 0) {
            try {
                long startTime = System.nanoTime();

                // Чтение тела POST-запроса
                String requestBody = readRequestBody();

                // Парсинг JSON из тела запроса
                JSONObject json = new JSONObject(requestBody);
                float x = json.getFloat("x");
                float y = json.getFloat("y");
                int r = json.getInt("r");

                // Валидация и логика обработки Validator.validateX(x) && Validator.validateY(y) && Validator.validateR(r)
                if (Validator.validateX(x) && Validator.validateY(y) && Validator.validateR(r)) {
                    long endTime = System.nanoTime();
                    long executeTime = (endTime - startTime);

                    LocalDateTime date = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                    sendJson(String.format(
                            "{\"result\": %b, \"current_time\": \"%s\", \"exec_time\": \"%s\"}",
                            Checker.hit(x, y, r), date.format(formatter), String.format("%.3f",(double) executeTime/1000000)
                    ));
                } else {
                    sendJson("{\"error\": \"invalid data\"}");
                }
            } catch (NumberFormatException e) {
                sendJson("{\"error\": \"wrong query param type\"}");
            } catch (NullPointerException e) {
                sendJson("{\"error\": \"missed necessary query param\"}");
            } catch (Exception e) {
                sendJson(String.format("{\"error\": \"%s\"}", e.toString()));
            }
        }
    }

    /**
     * Метод для чтения тела запроса FastCGI.
     * Заполняет поток и читает данные, основываясь на Content-Length.
     */
    private static String readRequestBody() throws IOException {
        // Заполняем поток данными
        FCGIInterface.request.inStream.fill();

        // Получаем длину содержимого из доступных данных
        int contentLength = FCGIInterface.request.inStream.available();

        // Если контент отсутствует
        if (contentLength <= 0) {
            return "";
        }

        // Создаём буфер для данных
        byte[] requestBodyRaw = new byte[contentLength];

        // Читаем данные в буфер
        int readBytes = FCGIInterface.request.inStream.read(requestBodyRaw, 0, contentLength);

        // Преобразуем байты в строку
        return new String(requestBodyRaw, 0, readBytes, StandardCharsets.UTF_8);
    }

    /**
     * Метод для отправки JSON-ответа клиенту.
     */
    private static void sendJson(String jsonDump) {
        System.out.println(String.format(RESPONSE_TEMPLATE, jsonDump.getBytes(StandardCharsets.UTF_8).length, jsonDump));
    }
}