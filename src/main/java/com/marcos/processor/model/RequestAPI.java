package com.marcos.processor.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Objects;

@Data
@AllArgsConstructor
public class RequestAPI {
	private String httpMethod;
	private String endpoint;
	private String description;


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		RequestAPI that = (RequestAPI) o;
		return Objects.equals(httpMethod, that.httpMethod) && Objects.equals(endpoint, that.endpoint);
	}

	@Override
	public int hashCode() {
		return Objects.hash(httpMethod, endpoint);
	}
}
