package com.abizerk.talker.model;

import java.util.List;

import org.springframework.data.annotation.Id;

public class Fact {
	@Id
    private String id;
	
	private String factValue;
	
	private List<Link> links;

    public Fact() {}

    public Fact(String factValue) {
        this.factValue = factValue.trim();
    }

    @Override
    public String toString() {
        return String.format("Fact[id=%s, factValue='%s']",id, factValue);
    }

	public String getFactValue() {
		return factValue.trim();
	}

	public void setFactValue(String factValue) {
		this.factValue = factValue.trim();
	}
	
}
