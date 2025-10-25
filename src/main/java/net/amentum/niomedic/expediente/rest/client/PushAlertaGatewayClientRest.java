package net.amentum.niomedic.expediente.rest.client;

import net.amentum.niomedic.expediente.views.AlertaNotificacionView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PushAlertaGatewayClientRest implements PushAlertaGatewayClient {

    private static final Logger log = LoggerFactory.getLogger(PushAlertaGatewayClientRest.class);

    private final RestTemplate restTemplate;
    private final String baseUrl; // p.ej. http://nio-gateway:8080

    public PushAlertaGatewayClientRest(RestTemplate restTemplate,
                                       @Value("http://nio-gateway:9211") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length()-1) : baseUrl;
    }

    @Override
    public void push(AlertaNotificacionView view) {
        try {
            String url = baseUrl + "/push/alertas/notificaciones";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<AlertaNotificacionView> req = new HttpEntity<>(view, headers);
            restTemplate.postForEntity(url, req, Void.class);
        } catch (Exception ex) {
            log.warn("No se pudo enviar la notificación al gateway: {}", ex.toString());
        }
    }
}
