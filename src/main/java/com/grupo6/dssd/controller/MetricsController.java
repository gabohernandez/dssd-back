package com.grupo6.dssd.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
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

	@GetMapping("/metrics")
	public ResponseEntity<Map<String, Object>> getMetrics() {

		return ResponseEntity.ok(metricsService.getMetrics());
	}


}
