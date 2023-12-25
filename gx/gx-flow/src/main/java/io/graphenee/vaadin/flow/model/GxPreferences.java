package io.graphenee.vaadin.flow.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GxPreferences implements Serializable {

	private static final long serialVersionUID = 1L;

	List<GridPreferences> grids = new ArrayList<>();

	public GridPreferences get(String gridName) {
		Optional<GridPreferences> grid = grids.stream().filter(c -> c.getGridName().equals(gridName)).findFirst();
		if (grid.isPresent())
			return grid.get();
		return null;
	}

	void removeGrid(String gridName) {
		grids.removeIf(c -> c.getGridName().equals(gridName));
	}

	public GridPreferences addGrid(String gridName) {
		Optional<GridPreferences> grid = grids.stream().filter(c -> c.getGridName().equals(gridName)).findFirst();
		if (grid.isPresent())
			return grid.get();
		GridPreferences p = GridPreferences.newInstance(gridName);
		grids.add(p);
		return p;
	}

	public static GxPreferences fromJson(String json) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(json.toString(), GxPreferences.class);
		} catch (JsonProcessingException e) {
			return new GxPreferences();
		}
	}

	public String toJson() {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			return "{}";
		}
	}

}
