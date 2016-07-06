package com.barthezzko.model;

public class SearchCriterion {

	private String From;
	private String InboundDate;
	private String To;
	private String OutboundDate;
	private long MinADT;
	private long MinCHD;
	private long MinINFT;
	private long SelectedADT;
	private long SelectedCHD;
	private long SelectedINFT;
	private long MaxPax;
	private String TripType;
	private String LinkBooking;
	private String MinDepartureDate;
	private String MaxDepartureDate;
	private String MinArrivalDate;
	private String MaxArrivalDate;
	private String Culture;
	private String CurrencyCode;
	private String Success;
	private String AnyFieldWithData;
	public String getFrom() {
		return From;
	}
	public void setFrom(String from) {
		From = from;
	}
	public String getInboundDate() {
		return InboundDate;
	}
	public void setInboundDate(String inboundDate) {
		InboundDate = inboundDate;
	}
	public String getTo() {
		return To;
	}
	public void setTo(String to) {
		To = to;
	}
	public String getOutboundDate() {
		return OutboundDate;
	}
	public void setOutboundDate(String outboundDate) {
		OutboundDate = outboundDate;
	}
	public long getMinADT() {
		return MinADT;
	}
	public void setMinADT(long minADT) {
		MinADT = minADT;
	}
	public long getMinCHD() {
		return MinCHD;
	}
	public void setMinCHD(long minCHD) {
		MinCHD = minCHD;
	}
	public long getMinINFT() {
		return MinINFT;
	}
	public void setMinINFT(long minINFT) {
		MinINFT = minINFT;
	}
	public long getSelectedADT() {
		return SelectedADT;
	}
	public void setSelectedADT(long selectedADT) {
		SelectedADT = selectedADT;
	}
	public long getSelectedCHD() {
		return SelectedCHD;
	}
	public void setSelectedCHD(long selectedCHD) {
		SelectedCHD = selectedCHD;
	}
	public long getSelectedINFT() {
		return SelectedINFT;
	}
	public void setSelectedINFT(long selectedINFT) {
		SelectedINFT = selectedINFT;
	}
	public long getMaxPax() {
		return MaxPax;
	}
	public void setMaxPax(long maxPax) {
		MaxPax = maxPax;
	}
	public String getTripType() {
		return TripType;
	}
	public void setTripType(String tripType) {
		TripType = tripType;
	}
	public String getLinkBooking() {
		return LinkBooking;
	}
	public void setLinkBooking(String linkBooking) {
		LinkBooking = linkBooking;
	}
	public String getMinDepartureDate() {
		return MinDepartureDate;
	}
	public void setMinDepartureDate(String minDepartureDate) {
		MinDepartureDate = minDepartureDate;
	}
	public String getMaxDepartureDate() {
		return MaxDepartureDate;
	}
	public void setMaxDepartureDate(String maxDepartureDate) {
		MaxDepartureDate = maxDepartureDate;
	}
	public String getMinArrivalDate() {
		return MinArrivalDate;
	}
	public void setMinArrivalDate(String minArrivalDate) {
		MinArrivalDate = minArrivalDate;
	}
	public String getMaxArrivalDate() {
		return MaxArrivalDate;
	}
	public void setMaxArrivalDate(String maxArrivalDate) {
		MaxArrivalDate = maxArrivalDate;
	}
	public String getCulture() {
		return Culture;
	}
	public void setCulture(String culture) {
		Culture = culture;
	}
	public String getCurrencyCode() {
		return CurrencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		CurrencyCode = currencyCode;
	}
	public String getSuccess() {
		return Success;
	}
	public void setSuccess(String success) {
		Success = success;
	}
	public String getAnyFieldWithData() {
		return AnyFieldWithData;
	}
	public void setAnyFieldWithData(String anyFieldWithData) {
		AnyFieldWithData = anyFieldWithData;
	}
	public String toJson() {
		return "{From" + From + ", InboundDate:" + InboundDate
				+ ", To:" + To + ", OutboundDate:" + OutboundDate + ", MinADT:"
				+ MinADT + ", MinCHD:" + MinCHD + ", MinINFT:" + MinINFT
				+ ", SelectedADT:" + SelectedADT + ", SelectedCHD:"
				+ SelectedCHD + ", SelectedINFT:" + SelectedINFT + ", MaxPax:"
				+ MaxPax + ", TripType:" + TripType + ", LinkBooking:"
				+ LinkBooking + ", MinDepartureDate:" + MinDepartureDate
				+ ", MaxDepartureDate:" + MaxDepartureDate
				+ ", MinArrivalDate:" + MinArrivalDate + ", MaxArrivalDate:"
				+ MaxArrivalDate + ", Culture:" + Culture + ", CurrencyCode:"
				+ CurrencyCode + ", Success:" + Success + ", AnyFieldWithData:"
				+ AnyFieldWithData + "}";
	}
	
	
}
