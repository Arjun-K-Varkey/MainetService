package com.abm.mainet.cfc.challan.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TransactionData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 166666636666666666L;
	@JsonProperty("Tag")
	private String tag;
	@JsonProperty("Value")
	private String value;
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return "TransactionData [tag=" + tag + ", value=" + value + "]";
	}
}
