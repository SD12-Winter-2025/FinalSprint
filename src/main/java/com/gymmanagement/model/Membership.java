package com.gymmanagement.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Represents a gym membership with details such as type, pricing, validity period, and payment status.
 * Tracks user associations and membership-related information for billing and scheduling purposes.
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

    /**
     * Default constructor for creating an empty membership instance.
     */
    public Membership() {}

    /**
     * Retrieves the unique identifier for the membership.
     * 
     * @return Membership ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Updates the unique identifier for the membership.
     * 
     * @param id New membership ID.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Retrieves the user ID associated with the membership.
     * 
     * @return Associated user ID.
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Updates the user ID associated with the membership.
     * 
     * @param userId New associated user ID.
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Retrieves the type of the membership (e.g., "Premium", "Basic").
     * 
     * @return Membership type.
     */
    public String getType() {
        return type;
    }

    /**
     * Updates the type of the membership.
     * 
     * @param type New membership type (e.g., "Premium", "Basic").
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Retrieves the description of the membership benefits.
     * 
     * @return Description of membership benefits.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Updates the description of the membership benefits.
     * 
     * @param description New description of membership benefits.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Retrieves the start date of the membership.
     * 
     * @return Start date of the membership.
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * Updates the start date of the membership.
     * 
     * @param startDate New start date for the membership.
     */
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    /**
     * Retrieves the end date of the membership.
     * 
     * @return End date of the membership.
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * Updates the end date of the membership.
     * 
     * @param endDate New end date for the membership.
     */
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    /**
     * Retrieves the price of the membership.
     * 
     * @return Membership price.
     */
    public double getPrice() {
        return price;
    }

    /**
     * Updates the price of the membership.
     * 
     * @param price New price for the membership.
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Retrieves the payment status of the membership.
     * 
     * @return Payment status (e.g., "PAID", "PENDING").
     */
    public String getPaymentStatus() {
        return paymentStatus;
    }

    /**
     * Updates the payment status of the membership.
     * 
     * @param paymentStatus New payment status for the membership.
     */
    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    /**
     * Provides a formatted table header for membership listings.
     * 
     * @return String representation of table header.
     */
    public static String getTableHeader() {
        return String.format(
            "\n+------+--------+------------+---------+-----------+--------------------+%n" +
            "| %-4s | %-6s | %-10s | %-7s | %-9s | %-18s |%n" +
            "+------+--------+------------+---------+-----------+--------------------+",
            "ID", "UserID", "Type", "Price", "Status", "Dates");
    }

    /**
     * Provides a formatted table footer for membership listings.
     * 
     * @return String representation of table footer.
     */
    public static String getTableFooter() {
        return "+------+--------+------------+---------+-----------+--------------------+\n";
    }

    /**
     * Formats membership details into a table row for console display.
     * 
     * @return String representation of membership details in table row format.
     */
    public String toTableRow() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateRange = (startDate != null && endDate != null)
            ? startDate.format(formatter) + " to " + endDate.format(formatter)
            : "N/A";

        return String.format(
            "| %-4d | %-6d | %-10s | $%-6.2f | %-9s | %-18s |",
            id, userId, type, price, paymentStatus, dateRange);
    }
}
