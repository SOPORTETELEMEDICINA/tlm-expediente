package net.amentum.niomedic.expediente.rest.client;

import net.amentum.niomedic.expediente.views.AlertaNotificacionView;

public interface PushAlertaGatewayClient {
    void push(AlertaNotificacionView view);
}
