package com.param.global.dto;

import java.util.List;
import java.util.Objects;

public class MultiSelectObj {
		private Integer id;
		private String label;

	public MultiSelectObj() {
	}

	public MultiSelectObj(Integer id) {
		this.id = id;
	}

	public MultiSelectObj(Integer id, String label) {
		this.id = id;
		this.label = label;
	}

	public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public String getLabel() {
			return label;
		}

		public void setLabel(String label) {
			this.label = label;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			MultiSelectObj that = (MultiSelectObj) o;
			return id.equals(that.id);
		}

		@Override
		public int hashCode() {
			return Objects.hash(id);
		}

}
