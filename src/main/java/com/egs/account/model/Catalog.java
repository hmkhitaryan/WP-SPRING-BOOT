package com.egs.account.model;

import org.apache.commons.lang.builder.EqualsBuilder;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "catalog")
public class Catalog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String link;
    private String fullFileName;
    private String comment;
    private String type;
    private Date insertDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "USER_ID")
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLink() { return link; }

    public void setLink(String link) { this.link = link; }

    public String getFullFileName() {
        return fullFileName;
    }

    public void setFullFileName(String fullFileName) {
        this.fullFileName = fullFileName;
    }

    public String getComment() { return comment; }

    public void setComment(String comment) { this.comment = comment; }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getInsertDate() { return insertDate; }

    public void setInsertDate(Date insertDate) { this.insertDate = insertDate; }

    public User getUser() { return user;}

    public void setUser(User user) { this.user = user; }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((link == null) ? 0 : link.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof Catalog)) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        final Catalog other = (Catalog) obj;
        final EqualsBuilder builder = new EqualsBuilder();
        builder.append(this.id, other.id);
        return builder.isEquals();
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", comment=" + comment + ", type="
                + type + ", insertDate=" + insertDate + "]";
    }
}
