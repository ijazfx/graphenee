/*******************************************************************************
 * Copyright (c) 2016, 2018 Farrukh Ijaz
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package io.graphenee.vaadin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.vaadin.server.Resource;
import com.vaadin.ui.MenuBar.Command;

public class TRSimpleMenuItem implements TRMenuItem {

	private String caption;
	private Resource icon;
	private Command command;
	private String viewName;
	private String badge;
	private String badgeId;
	private TRMenuItem parent;
	private List<TRMenuItem> children = new ArrayList<>();

	private TRSimpleMenuItem(String viewName, String caption, Resource icon, Command command, String badgeId, String badge) {
		this.viewName = viewName;
		this.caption = caption;
		this.icon = icon;
		this.command = command;
		this.badgeId = badgeId;
		this.badge = badge;
	}

	@Override
	public String badgeId() {
		return badgeId;
	}

	@Override
	public String badge() {
		return badge;
	}

	@Override
	public String caption() {
		return caption;
	}

	@Override
	public Resource icon() {
		return icon;
	}

	@Override
	public Command command() {
		return command;
	}

	@Override
	public String viewName() {
		return viewName;
	}

	@Override
	public Collection<TRMenuItem> getChildren() {
		return children;
	}

	public void addChild(TRMenuItem child) {
		if (!getChildren().contains(child)) {
			getChildren().add(child);
			if (child instanceof TRSimpleMenuItem) {
				((TRSimpleMenuItem) child).parent = this;
			}
		}
	}

	@Override
	public TRMenuItem getParent() {
		return parent;
	}

	@Override
	public boolean hasChildren() {
		return !getChildren().isEmpty();
	}

	public static TRSimpleMenuItem createMenuItem(String caption, Resource icon) {
		TRSimpleMenuItem menuItem = new TRSimpleMenuItem(null, caption, icon, null, null, null);
		return menuItem;
	}

	public static TRSimpleMenuItem createMenuItem(String caption, Resource icon, Command command) {
		TRSimpleMenuItem menuItem = new TRSimpleMenuItem(null, caption, icon, command, null, null);
		return menuItem;
	}

	public static TRSimpleMenuItem createMenuItemForView(String view, String caption, Resource icon) {
		TRSimpleMenuItem menuItem = new TRSimpleMenuItem(view, caption, icon, null, null, null);
		return menuItem;
	}

	public TRSimpleMenuItem withBadge(String badge) {
		this.badge = badge;
		return this;
	}

	public TRSimpleMenuItem clearBadge() {
		this.badge = null;
		return this;
	}

	public TRSimpleMenuItem withBadgeId(String badgeId) {
		this.badgeId = badgeId;
		return this;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((caption == null) ? 0 : caption.hashCode());
		result = prime * result + ((parent == null) ? 0 : parent.hashCode());
		result = prime * result + ((viewName == null) ? 0 : viewName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TRSimpleMenuItem other = (TRSimpleMenuItem) obj;
		if (caption == null) {
			if (other.caption != null)
				return false;
		} else if (!caption.equals(other.caption))
			return false;
		if (parent == null) {
			if (other.parent != null)
				return false;
		} else if (!parent.equals(other.parent))
			return false;
		if (viewName == null) {
			if (other.viewName != null)
				return false;
		} else if (!viewName.equals(other.viewName))
			return false;
		return true;
	}

}
