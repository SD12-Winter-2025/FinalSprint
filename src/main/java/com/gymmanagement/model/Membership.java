package com.gymmanagement.model;

import java.time.LocalDate;

/**
 * Represents a user's gym membership with pricing and validity period.
 */
public class Membership {
    private int id;
    private int userId;
    private String type;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private double price;
    private String paymentStatus;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    
    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    @Override
    public String toString() {
        return String.format(
            "Membership[id=%d, userId=%d, type='%s', price=%.2f, status='%s', dates=%s to %s]",
            id, userId, type, price, paymentStatus, startDate, endDate
        );
    }
}