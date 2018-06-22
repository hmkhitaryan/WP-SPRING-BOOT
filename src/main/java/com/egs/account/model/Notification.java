package com.egs.account.model;

import org.apache.commons.lang.builder.EqualsBuilder;

import javax.persistence.*;

/**
 * @author Hayk_Mkhitaryan
 */
@Entity
@Table(name = "notification")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    private String comment;

    private boolean seen;

    public Notification(User user, String comment, boolean seen) {
        this.user = user;
        this.comment = comment;
        this.seen = seen;
    }

    public Notification(User initiator, String comment) {
        this.user = initiator;
        this.comment = comment;
    }

    public Notification() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((comment == null) ? 0 : comment.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof Notification)) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        final Notification other = (Notification) obj;
        final EqualsBuilder builder = new EqualsBuilder();
        builder.append(this.id, other.id);
        return builder.isEquals();
    }

    @Override
    public String toString() {
        return "Notification [id=" + id + ", comment=" + comment + ", user name="
                + getUser().getUsername() + ", seen=" + seen + "]";
    }
}
