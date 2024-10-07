import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fastcgi.FCGIInterface;
import org.json.JSONObject;

public class App {

    private static final String RESPONSE_TEMPLATE = """
            Content-Type: application/json
            Content-Length: %d

            %s""";

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

                // Валидация и логика обработки
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
                sendJson(String.format("{\"error\": \"%s\"}", e));
            }
        }
    }

    /**
     * Метод для чтения тела запроса FastCGI.
     */
    private static String readRequestBody() throws IOException {
        FCGIInterface.request.inStream.fill();
        int contentLength = FCGIInterface.request.inStream.available();
        if (contentLength <= 0) {
            return "";
        }
        byte[] requestBodyRaw = new byte[contentLength];
        int readBytes = FCGIInterface.request.inStream.read(requestBodyRaw, 0, contentLength);
        return new String(requestBodyRaw, 0, readBytes, StandardCharsets.UTF_8);
    }

    /**
     * Метод для отправки JSON-ответа клиенту.
     */
    private static void sendJson(String jsonDump) {
        System.out.printf((RESPONSE_TEMPLATE) + "%n", jsonDump.getBytes(StandardCharsets.UTF_8).length, jsonDump);
    }
}