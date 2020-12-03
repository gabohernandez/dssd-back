package com.grupo6.dssd.service;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.springframework.stereotype.Service;
import com.grupo6.dssd.model.Project;
import com.grupo6.dssd.model.Protocol;
import com.grupo6.dssd.model.User;

/**
 * @author nahuel.barrena on 2/12/20
 */
@Service
public class MetricsService {

	private final ProtocolService protocolService;

	public MetricsService(ProtocolService protocolService) {
		this.protocolService = protocolService;
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
	public Map<String, Object> getMetrics() {
		Map<String, Object> metrics = new HashMap<>();
		List<Protocol> protocols = protocolService.findAllProtocols();
		List<Project> projects = protocolService.findAllProjects();
		metrics.put("aprobados_totales", this.getAprobadosTotales(protocols));
		metrics.put("fallidos_totales", this.getFallidosTotales(protocols));
		// TODO ver como contar cada reintento
		//metrics.put("reIntentados_fallidos", "");
		metrics.put("proyectos_finalizados_ok", this.getFinalizadosOk(projects));
		metrics.put("proyectos_finalizados_error", this.getFinalizadosError(projects));
		metrics.put("usuario_mas_protocolos_asignados", this.getMostBusyUser());
		// TODO VER LO DEL TIEMPO DE ASIGNACION vs EJECUCION
		//metrics.put("tiempo_entre_asignacion_y_ejecucion", "");

		metrics.put("tiempo_ejecucion_por_protocolo", this.getTiempoEjecucionPorProtocolo(protocols));
		// TODO
		metrics.put("tiempo_promedio_ejecucion_protocolos", this.getTiempoPromedioEjecucionProtocolos(protocols));
		return metrics;
	}

	private double getTiempoPromedioEjecucionProtocolos(List<Protocol> protocols) {
		OptionalDouble averageMillis = protocols.stream()
				.mapToLong(x -> Duration.between(x.getStartTime(), x.getEndTime()).toMillis()).average();
		return ((averageMillis.orElse(0) / 1000) % 60);

	}

	private Map<String, String> getTiempoEjecucionPorProtocolo(List<Protocol> protocols) {
		Map<String, String> map = new HashMap<>();
		protocols.forEach(p -> {
					Duration protocolDuration = Duration.between(p.getStartTime(), p.getEndTime());
					map.put(p.getName(), DurationFormatUtils.formatDuration(protocolDuration.toMillis(), "H:mm:ss", true));
				}
		);
		return map;
	}

	private User getMostBusyUser() {
		return protocolService.getMostBusyUser();
	}

	private long getFinalizadosError(List<Project> projects) {
		return projects.stream()
				.filter(project -> project.getStatus().equalsIgnoreCase("FAILED"))
				.count();

	}

	private long getFinalizadosOk(List<Project> projects) {
		return projects.stream()
				.filter(project -> project.getStatus().equalsIgnoreCase("FINISHED"))
				.count();

	}

	private String getFallidosTotales(List<Protocol> protocols) {
		try {
			return String.valueOf(protocols.stream().filter(p -> !p.isApproved()).count() / protocols.size());
		} catch (Exception e) {
			return "";
		}
	}

	private String getAprobadosTotales(List<Protocol> protocols) {
		try {
			return String.valueOf(protocols.stream().filter(Protocol::isApproved).count() / protocols.size());
		} catch (Exception e) {
			return "";
		}
	}
}
