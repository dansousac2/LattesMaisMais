package com.ifpb.lattesmaismais.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

/**
 * Classe usada apenas para envio de informações ao Backend.
 * No retorno ao Frontend usa-se a própria entidade.
 * @author Danilo
 *
 */
public class ValidatorCommentaryDto {

	@Positive(message = "Id de validador deve ser positivo")
	private Integer validatorId;
	
	@NotBlank(message = "Nome de validador não pode ser nulo")
	private String validatorName;
	
	@NotBlank(message = "Comentário não pode ser nulo")
	private String commentary;
	
	public Integer getValidatorId() {
		return validatorId;
	}

	public void setValidatorId(Integer validatorId) {
		this.validatorId = validatorId;
	}

	public String getValidatorName() {
		return validatorName;
	}

	public void setValidatorName(String validatorName) {
		this.validatorName = validatorName;
	}

	public String getCommentary() {
		return commentary;
	}

	public void setCommentary(String commentary) {
		this.commentary = commentary;
	}

}
