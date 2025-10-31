package net.amentum.niomedic.expediente.rest;

import net.amentum.niomedic.expediente.service.AlertaNotificacionService;
import net.amentum.niomedic.expediente.views.AlertaNotificacionCreateView;
import net.amentum.niomedic.expediente.views.AlertaNotificacionView;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alertas/notificaciones")
public class AlertaNotificacionRest {

    private final AlertaNotificacionService service;

    public AlertaNotificacionRest(AlertaNotificacionService service) {
        this.service = service;
    }

    @PostMapping
    public AlertaNotificacionView create(@RequestBody AlertaNotificacionCreateView view) {
        return service.createAndReturn(view);
    }

    @PutMapping("/{id}/visto")
    public void markAsSeen(@PathVariable("id") Long id) {
        service.markAsSeen(id);
    }

    @GetMapping("/activas/{idMedico}")
    public List<AlertaNotificacionView> listActivas(@PathVariable("idMedico") String idMedico) {
        return service.listActivas(idMedico);
    }

    @GetMapping("/activas/{idMedico}/count")
    public long countActivas(@PathVariable("idMedico") String idMedico) {
        return service.countActivas(idMedico);
    }
}