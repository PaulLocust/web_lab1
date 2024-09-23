import check.Checker;
import com.fastcgi.FCGIInterface;
import org.json.JSONObject;
import validation.Validate;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FastCGIServer {
    public static void main(String[] args) {
        FCGIInterface fcgiInterface = new FCGIInterface();
        Checker checker = new Checker();
        Validate validate = new Validate();
        while (fcgiInterface.FCGIaccept() >= 0) {
            try {
                long startTime = System.nanoTime();
                String content = """
                {
                    "result": "%s",
                    "workTime": "%s",
                    "time": "%s"
                }
                """;

                // Читаем и парсим тело запроса как JSON
                float[] floats = readRequestBody();
                float x = floats[0];
                float y = floats[1];
                int r = (int) floats[2];

                boolean result;
                if (validate.check(x, y, r)) {
                    result = checker.hit(x, y, r);
                } else {
                    result = false;
                }

                long endTime = System.nanoTime();
                long executeTime = (endTime - startTime);
                content = content.formatted(result, String.format("%.3f",(double) executeTime/1000000), LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")), x, y, r);

                String response = """
                    HTTP/2 200 OK
                    Content-Type: application/json
                    Content-Length: %d
                    
                    %s
                    
                    """.formatted(content.getBytes(StandardCharsets.UTF_8).length, content);

                // Отправляем ответ
                System.out.println(response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static float[] readRequestBody() {
        try {
            // Чтение тела запроса
            FCGIInterface.request.inStream.fill();
            var contentLength = FCGIInterface.request.inStream.available();
            var buffer = ByteBuffer.allocate(contentLength);
            var readBytes = FCGIInterface.request.inStream.read(buffer.array(), 0, contentLength);
            var requestBodyRaw = new byte[readBytes];
            buffer.get(requestBodyRaw);
            buffer.clear();

            // Преобразуем тело запроса в строку
            String requestString = new String(requestBodyRaw, StandardCharsets.UTF_8);

            // Парсим строку как JSON
            JSONObject json = new JSONObject(requestString);

            // Извлекаем значения x, y, r из JSON
            float x = json.getFloat("x");
            float y = json.getFloat("y");
            float r = json.getFloat("r");

            return new float[]{x, y, r};
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}