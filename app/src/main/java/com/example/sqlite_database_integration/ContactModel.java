package com.example.sqlite_database_integration;

import java.io.Serializable;

/**
 * Model class for Contact data
 */
public class ContactModel implements Serializable {
    private int id;
    private String name;
    private String phone_no;
    private String email;
    private String dateCreated;

    // Default constructor
    public ContactModel() {
    }

    // Constructor with parameters
    public ContactModel(int id, String name, String phone_no) {
        this.id = id;
        this.name = name;
        this.phone_no = phone_no;
    }

    // Constructor without id (for new contacts)
    public ContactModel(String name, String phone_no) {
        this.name = name;
        this.phone_no = phone_no;
    }

    // Full constructor
    public ContactModel(int id, String name, String phone_no, String email, String dateCreated) {
        this.id = id;
        this.name = name;
        this.phone_no = phone_no;
        this.email = email;
        this.dateCreated = dateCreated;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNo() {
        return phone_no;
    }

    public void setPhoneNo(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    /**
     * Check if this is a new contact (not yet saved to DB)
     * @return true if this is a new contact, false otherwise
     */
    public boolean isNewContact() {
        return id <= 0;
    }

    /**
     * Validates that the contact has required fields
     * @return true if valid, false otherwise
     */
    public boolean isValid() {
        return name != null && !name.trim().isEmpty() &&
                phone_no != null && !phone_no.trim().isEmpty();
    }

    /**
     * Creates a copy of this contact
     * @return A new ContactModel with the same data
     */
    public ContactModel copy() {
        ContactModel copy = new ContactModel();
        copy.id = this.id;
        copy.name = this.name;
        copy.phone_no = this.phone_no;
        copy.email = this.email;
        copy.dateCreated = this.dateCreated;
        return copy;
    }

    @Override
    public String toString() {
        return "ContactModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone_no='" + phone_no + '\'' +
                ", email='" + (email != null ? email : "") + '\'' +
                ", dateCreated='" + (dateCreated != null ? dateCreated : "") + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        ContactModel that = (ContactModel) obj;

        if (id != that.id) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return phone_no != null ? phone_no.equals(that.phone_no) : that.phone_no == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (phone_no != null ? phone_no.hashCode() : 0);
        return result;
    }
}