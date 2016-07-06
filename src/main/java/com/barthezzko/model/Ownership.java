package com.barthezzko.model;

public class Ownership {
	
	private String sessionOwnerId;
	private String sessionOwnerPage;
	private long lastUpdated;
	
	public String getSessionOwnerId() {
		return sessionOwnerId;
	}
	public void setSessionOwnerId(String sessionOwnerId) {
		this.sessionOwnerId = sessionOwnerId;
	}
	public String getSessionOwnerPage() {
		return sessionOwnerPage;
	}
	public void setSessionOwnerPage(String sessionOwnerPage) {
		this.sessionOwnerPage = sessionOwnerPage;
	}
	public long getLastUpdated() {
		return lastUpdated;
	}
	public void setLastUpdated(long lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	
	

}
