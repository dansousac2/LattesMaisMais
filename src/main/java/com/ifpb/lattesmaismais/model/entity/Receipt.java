package com.ifpb.lattesmaismais.model.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import com.ifpb.lattesmaismais.model.enums.ReceiptStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Table(name = "RECEIPTS")
@Entity
public class Receipt implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RECEIPT_ID", nullable = false)
    private Integer id;

    @Column(name = "RECEIPT_NAME")
    private String name;

    @Column(name = "RECEIPT_EXTENSION")
    private String extension;
    
    @Column(name = "RECEIPT_URL")
    private String url;
    
    @Column(name = "RECEIPT_COMENTARY")
    private String commentary;
    
    @Column(name = "RECEIPT_HERITAGE")
    private String heritage;
    
    @Column(name = "RECEIPT_STATUS")
    private ReceiptStatus status;
    
    @OneToMany(cascade = CascadeType.REMOVE)
    @JoinTable(
			name = "RECEIPT_VALIDATORCOMMENT",
			joinColumns = @JoinColumn(name = "RECEIPT_ID"),
			inverseJoinColumns = @JoinColumn(name = "COMMENT_ID")
	)
    private List<ValidatorCommentary> validatorCommentary;
    
    // usado em algumas possíveis situações para comparar com arquivo no frontend
    @Column(name = "RECEIPT_MODIFICATION_REF")
    private String lastModified;

    public Receipt() {
    	this.status = ReceiptStatus.WAITING_VALIDATION;
    }

	public List<ValidatorCommentary> getValidatorCommentary() {
		return validatorCommentary;
	}

	public void setValidatorCommentary(List<ValidatorCommentary> validatorCommentary) {
		this.validatorCommentary = validatorCommentary;
	}

	public String getLastModified() {
		return lastModified;
	}

	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public ReceiptStatus getStatus() {
		return status;
	}

	public void setStatus(ReceiptStatus status) {
		this.status = status;
	}

	public String getHeritage() {
		return heritage;
	}

	public void setHeritage(String heritage) {
		this.heritage = heritage;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCommentary() {
		return commentary;
	}

	public void setCommentary(String commentary) {
		this.commentary = commentary;
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

	@Override
	public int hashCode() {
		return Objects.hash(extension, id, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Receipt other = (Receipt) obj;
		return Objects.equals(extension, other.extension) && Objects.equals(id, other.id)
				&& Objects.equals(name, other.name);
	}

}
