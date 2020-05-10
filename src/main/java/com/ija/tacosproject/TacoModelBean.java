package com.ija.tacosproject;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.*;

/*
 * This  class was written for Challenge 1 and represents the db.json records
 * This represents the fact that 1 or multi records are found under the "tacos" property
 * and the recordss are represented by a List.
 */
public class TacoModelBean {
	
	@JsonProperty("tacos")
	private List<TacoJsonRecord> tacoRecords;
	
	// constructors
	public TacoModelBean() {}
	
	public TacoModelBean (List<TacoJsonRecord> tacoRecord) {
		this.tacoRecords = tacoRecord;
	}

	//getters and setters
	public List<TacoJsonRecord> getTacoRecords() {
		return tacoRecords;
	}

	public void setTacoRecords(List<TacoJsonRecord> tacoRecords) {
		this.tacoRecords = tacoRecords;
	}

}
