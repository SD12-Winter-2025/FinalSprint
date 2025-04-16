package com.gymmanagement.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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

    /**
     * Generates the table header for displaying memberships.
     */
    public static String getTableHeader() {
        return String.format(
            "\n+------+--------+------------+---------+-----------+--------------------+%n" +
            "| %-4s | %-6s | %-10s | %-7s | %-9s | %-18s |%n" +
            "+------+--------+------------+---------+-----------+--------------------+",
            "ID", "UserID", "Type", "Price", "Status", "Dates"
        );
    }

    /**
     * Generates the table footer for displaying memberships.
     */
    public static String getTableFooter() {
        return "+------+--------+------------+---------+-----------+--------------------+\n";
    }

    /**
     * Formats membership data into a table row.
     */
    public String toTableRow() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateRange = (startDate != null && endDate != null)
            ? startDate.format(formatter) + " to " + endDate.format(formatter)
            : "N/A";

        return String.format(
            "| %-4d | %-6d | %-10s | $%-6.2f | %-9s | %-18s |",
            id, userId, type, price, paymentStatus, dateRange
        );
    }
}
