package com.abizerk.talker.model;

import org.springframework.data.annotation.Id;

public class Link {
	
	@Id
	private String id;
	
	private String linkDescription;
	
	private Fact parentFact;
	
	private Fact childFact;
	
	public Link(Fact parentFact, Fact childFact) {
		this.parentFact = parentFact;
		this.childFact = childFact;
	}

	public String getLinkDescription() {
		return linkDescription;
	}

	public void setLinkDescription(String linkDescription) {
		this.linkDescription = linkDescription;
	}

	public Fact getStartFactNode() {
		return parentFact;
	}

	public void setStartFactNode(Fact startFactNode) {
		this.parentFact = startFactNode;
	}

	public Fact getEndFactNode() {
		return childFact;
	}

	public void setEndFactNode(Fact endFactNode) {
		this.childFact = endFactNode;
	}
	
	public String toString() {
		return parentFact + " > " + childFact;
	}
	
}
