package com.egs.account.model.chat;

import com.egs.account.model.User;
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

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "INITIATOR_USER_ID")
    private User initiator;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "RECEIVER_USER_ID")
    private User receiver;

    private String comment;

    private boolean seen;

    public Notification(User initiator, User receiver, boolean seen) {
        this.initiator = initiator;
        this.receiver = receiver;
        this.seen = seen;
    }

    public Notification(User receiver, String comment, boolean seen) {
        this.receiver = receiver;
        this.comment = comment;
        this.seen = seen;
    }

    public Notification(User initiator, User receiver, String comment, boolean seen) {
        this.initiator = initiator;
        this.receiver = receiver;
        this.comment = comment;
        this.seen = seen;
    }

    public Notification(User initiator, User receiver, String comment) {
        this.initiator = initiator;
        this.receiver = receiver;
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

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
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

    public User getInitiator() {
        return initiator;
    }

    public void setInitiator(User initiator) {
        this.initiator = initiator;
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
        return "Notification [id=" + id + ", comment=" + comment + ", initiator name="
                + getInitiator().getUsername() + ", receiver name="
                + getReceiver().getUsername() + ", seen=" + seen + "]";
    }
}
