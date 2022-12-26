package com.ifpb.lattesmaismais.model.entity;

import jakarta.persistence.*;

import java.util.Objects;

@Table(name = "ENTRY_RECEIPT")
@Entity
public class Receipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RECEIPT_ID", nullable = false)
    private Integer id;

    @Column(name = "RECEIPT_NAME", nullable = false)
    private String name;

    @Column(name = "RECEIPT_EXTENSION", nullable = false)
    private String extension;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private User owner;

    public Receipt() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Receipt receipt = (Receipt) o;

        if (!Objects.equals(id, receipt.id)) return false;
        if (!Objects.equals(name, receipt.name)) return false;
        if (!Objects.equals(extension, receipt.extension)) return false;
        return Objects.equals(owner, receipt.owner);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
