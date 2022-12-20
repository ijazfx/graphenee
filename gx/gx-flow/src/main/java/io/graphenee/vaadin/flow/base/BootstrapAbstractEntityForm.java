package io.graphenee.vaadin.flow.base;

import lombok.Setter;

@Setter
public abstract class BootstrapAbstractEntityForm<T> extends GxAbstractEntityForm<T> {
	private static final long serialVersionUID = 1L;

	public static enum Orientation {
		HORIZONTAL,
		VERTICAL
	}

	public static enum Size {
		XS_PHONE,
		SM_TABLET,
		MD_LAPTOP,
		lG_DESKTOP,
		FULLSCREEN
	}

	private Orientation orientation = Orientation.HORIZONTAL;
	private Size size = Size.MD_LAPTOP;

	public BootstrapAbstractEntityForm(Class<T> entityClass) {
		super(entityClass);
	}

	@Override
	protected String dialogWidth() {
		switch (orientation) {
		case VERTICAL:
			switch (size) {
			case XS_PHONE:
				return "768px";
			case SM_TABLET:
				return "992px";
			case MD_LAPTOP:
				return "1200px";
			case lG_DESKTOP:
				return "90%";
			case FULLSCREEN:
				return "100%";
			}
		break;
		case HORIZONTAL:
			switch (size) {
			case XS_PHONE:
				return "1024px";
			case SM_TABLET:
				return "1320px";
			case MD_LAPTOP:
				return "1596px";
			case lG_DESKTOP:
				return "90%";
			case FULLSCREEN:
				return "100%";
			}
		}
		return super.dialogWidth();
	}

	@Override
	protected String dialogHeight() {
		switch (orientation) {
		case HORIZONTAL:
			switch (size) {
			case XS_PHONE:
				return "768px";
			case SM_TABLET:
				return "992px";
			case MD_LAPTOP:
				return "1200px";
			case lG_DESKTOP:
				return "90%";
			case FULLSCREEN:
				return "100%";
			}
		break;
		case VERTICAL:
			switch (size) {
			case XS_PHONE:
				return "1024px";
			case SM_TABLET:
				return "1320px";
			case MD_LAPTOP:
				return "1596px";
			case lG_DESKTOP:
				return "90%";
			case FULLSCREEN:
				return "100%";
			}
		}
		return super.dialogHeight();
	}

}
