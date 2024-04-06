package com.marcos.processor.dto.input;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GenerateComparisonConnectorInputDTO {
	
	private String referenceConnector;
	private String reviewConnector;
}
