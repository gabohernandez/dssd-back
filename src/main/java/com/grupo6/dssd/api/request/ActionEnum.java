package com.grupo6.dssd.api.request;

/**
 * @author nahuel.barrena on 7/12/20
 */
public enum ActionEnum {

	CONTINUE(1, "Continuar"),
	RESTART_PROTOCOL(2, "Repetir"),
	RESTART_ALL(3, "Reiniciar"),
	CANCEL_PROJECT(4, "Cancelar");

	private final int id;
	private final String description ;

	ActionEnum(int id, String description) {
		this.id = id;
		this.description = description;
	}

	public static ActionEnum fromId(int id) {
		switch (id) {
		case 1: return ActionEnum.CONTINUE;
		case 2: return ActionEnum.RESTART_PROTOCOL;
		case 3: return ActionEnum.RESTART_ALL;
		case 4: return ActionEnum.CANCEL_PROJECT;
		default: return CANCEL_PROJECT;

		}
	}

	public int getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}
}
