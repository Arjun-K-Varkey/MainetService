package com.abm.mainet.cfc.techprocess.dto;

import java.io.Serializable;

public class IdentifierRequest implements Serializable {

	private String identifier;

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	@Override
	public String toString() {
		return "IdentifierRequest [identifier=" + identifier + "]";
	}
}
