package com.mightymerce.core.domain;

import java.math.BigDecimal;

public class FlatSocialOrder {

	private Long article;
	private String transactionId;    
	private String paymentStatus;
	private String email; 			
	private  String payerId 		;	// ' Unique PayPal customer account identification number.
	private String payerStatus	;	// ' Status of payer. Character length and limitations: 10 single-byte alphabetic characters.
	private String firstName	;	// ' Payer's first name.
	private String lastName		;	// ' Payer's last name.
	private String shipToName	;	// ' Person's name associated with this address.
	private String shipToStreet	;	// ' First street address.
	private String shipToCity	;	// ' Name of city.
	private String shipToState	;	// ' State or province
	private String shipToCntryCode;	// ' Country code. 
	private String shipToZip	;	// ' U.S. Zip code or other country-specific postal code.
	private String addressStatus;	// ' Status of street address on file with PayPal 
	private BigDecimal totalAmt ;	// ' Total Amount to be paid by buyer
	private String currencyCode ;    // 'Currency being used 

	public Long getArticle() {
		return article;
	}

	public void setArticle(Long article) {
		this.article = article;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPayerId() {
		return payerId;
	}

	public void setPayerId(String payerId) {
		this.payerId = payerId;
	}

	public String getPayerStatus() {
		return payerStatus;
	}

	public void setPayerStatus(String payerStatus) {
		this.payerStatus = payerStatus;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getShipToName() {
		return shipToName;
	}

	public void setShipToName(String shipToName) {
		this.shipToName = shipToName;
	}

	public String getShipToStreet() {
		return shipToStreet;
	}

	public void setShipToStreet(String shipToStreet) {
		this.shipToStreet = shipToStreet;
	}

	public String getShipToCity() {
		return shipToCity;
	}

	public void setShipToCity(String shipToCity) {
		this.shipToCity = shipToCity;
	}

	public String getShipToState() {
		return shipToState;
	}

	public void setShipToState(String shipToState) {
		this.shipToState = shipToState;
	}

	public String getShipToCntryCode() {
		return shipToCntryCode;
	}

	public void setShipToCntryCode(String shipToCntryCode) {
		this.shipToCntryCode = shipToCntryCode;
	}

	public String getShipToZip() {
		return shipToZip;
	}

	public void setShipToZip(String shipToZip) {
		this.shipToZip = shipToZip;
	}

	public String getAddressStatus() {
		return addressStatus;
	}

	public void setAddressStatus(String addressStatus) {
		this.addressStatus = addressStatus;
	}

	public BigDecimal getTotalAmt() {
		return totalAmt;
	}

	public void setTotalAmt(BigDecimal totalAmt) {
		this.totalAmt = totalAmt;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

}
