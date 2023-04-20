package com.ifpb.lattesmaismais.model.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Table(name = "RECEIPTS")
@Entity
public class Receipt implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RECEIPT_ID", nullable = false)
    private Integer id;

    @Column(name = "RECEIPT_NAME", nullable = false)
    private String name;

    @Column(name = "RECEIPT_EXTENSION", nullable = false)
    private String extension;
    
    @Column(name = "RECEIPT_COMENTARY")
    private String commentary;

    public Receipt() {

    }

    public Receipt(Integer id, String name, String extension, String commentary) {
		super();
		this.id = id;
		this.name = name;
		this.extension = extension;
		this.commentary = commentary;
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
