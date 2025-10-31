package net.amentum.niomedic.expediente.rest.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import net.amentum.niomedic.expediente.views.AlertaNotificacionView;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

@Component
@Slf4j
public class PushAlertaGatewayClient {

    @Value("${push.alerta.gateway.url:https://cct.telemedicina.lat:9211}")
    private String gatewayUrl;

    @Value("${url}")
    private String authUrl;

    private final ObjectMapper objectMapper;
    private String token = "";
    private Date fechaExpiraToken;

    // ✅ Constructor con configuración de fechas ISO compatible con Java 8
    public PushAlertaGatewayClient() {
        ObjectMapper om = new ObjectMapper();

        // No escribir fechas como timestamps numéricos
        om.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        // Formato ISO básico (aceptado por Gateway)
        om.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"));

        this.objectMapper = om;
    }

    private Map<String, Object> obtenerToken() {
        try {
            log.info("obtenerToken() - Solicitando token de acceso a: {}", authUrl);
            String params = "client_id=auth.testing&client_secret=auth.testing&username=sysAdmin&password=5ae23bbbb73b35ef9f4a624e656b8240641dc48e005b55482def92901253389f&grant_type=password";

            URL url = new URL(authUrl + "auth/oauth/token");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            try (OutputStream os = conn.getOutputStream()) {
                os.write(params.getBytes());
                os.flush();
            }

            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                log.error("Error obteniendo token: {} - {}", conn.getResponseCode(), conn.getResponseMessage());
                return null;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String currentLine;
            while ((currentLine = br.readLine()) != null) {
                response.append(currentLine);
            }
            br.close();

            Map<String, Object> jsonResponse = objectMapper.readValue(response.toString(), Map.class);
            Integer expiraEn = (Integer) jsonResponse.get("expires_in");

            final Long oneSecondInMillis = 1000L;
            Calendar date = Calendar.getInstance();
            long t = date.getTimeInMillis();
            fechaExpiraToken = new Date(t + (expiraEn * oneSecondInMillis));

            token = "Bearer " + (String) jsonResponse.get("access_token"); // 🔠 en mayúscula
            log.info("obtenerToken() - Token generado exitosamente");

            return jsonResponse;

        } catch (Exception e) {
            log.error("Error al obtener token", e);
            return null;
        }
    }

    private boolean tokenActivo() {
        if (fechaExpiraToken == null) return false;
        Date fechaActual = new Date();
        return fechaActual.getTime() < fechaExpiraToken.getTime();
    }

    public void push(AlertaNotificacionView view) {
        try {
            if (view == null) {
                log.warn("Intento de enviar alerta nula");
                return;
            }

            if (token.isEmpty() || !tokenActivo()) {
                obtenerToken();
            }

            String url = gatewayUrl + "/push/alertas/notificaciones";
            log.info("Enviando alerta a: {}", url);

            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", token);
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            // ✅ Ahora el ObjectMapper generará fechaCreacion como "2025-10-30T23:10:04"
            String jsonBody = objectMapper.writeValueAsString(view);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(jsonBody.getBytes());
                os.flush();
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                log.info("Alerta enviada exitosamente: {} - ResponseCode: {}", view.getId(), responseCode);
            } else if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                log.warn("Token expirado, reintentando con nuevo token...");
                obtenerToken();
                push(view);
            } else {
                log.warn("Respuesta del gateway: {} - {}", responseCode, conn.getResponseMessage());
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream()))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        response.append(line);
                    }
                    log.error("Error del gateway: {}", response.toString());
                }
            }

        } catch (Exception ex) {
            log.error("Error al enviar alerta al gateway", ex);
        }
    }
}
