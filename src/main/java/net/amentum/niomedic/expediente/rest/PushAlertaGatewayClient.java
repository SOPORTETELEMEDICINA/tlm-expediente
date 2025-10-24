package net.amentum.niomedic.expediente.rest.client;

import net.amentum.niomedic.expediente.views.AlertaNotificacionView;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

// Si usas Eureka: la URL suele ser el nombre lógico; si no, cámbialo a url=${gateway.base-url}
@FeignClient(value = "http://nio-gateway")
public interface PushAlertaGatewayClient {
    @PostMapping("/push/alertas/notificaciones")
    void push(@RequestBody AlertaNotificacionView view);
}
