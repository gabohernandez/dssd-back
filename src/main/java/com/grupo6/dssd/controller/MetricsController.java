package com.grupo6.dssd.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import com.grupo6.dssd.exception.ProjectNotFoundException;
import com.grupo6.dssd.model.Protocol;
import com.grupo6.dssd.service.MetricsService;

/**
 * @author nahuel.barrena on 2/12/20
 */
@RestController
public class MetricsController {


	private final MetricsService metricsService;

	public MetricsController(MetricsService metricsService) {
		this.metricsService = metricsService;
	}

	/**
	 * 1. Protocolos aprobados / protocolos totales
	 * 2. Protocolos fallidos / protocolos totales
	 * 3. Protocolos re intentados / protocolos fallidos
	 * 4. Cantidad de proyectos finalizados correctamente
	 * 5. Cantidad de proyectos finalizados con errores
	 * 6. Usuario con más protocolos asignados
	 * 7. Tiempo entre asignación y ejecución del protocolo
	 * 8. Tiempo de ejecución de los protocolos
	 */

	@GetMapping("/metrics")
	public ResponseEntity<Map<String, Object>> getMetrics() {

		return ResponseEntity.ok(new HashMap<>());
	}


}
