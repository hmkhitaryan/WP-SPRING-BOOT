package com.egs.account.model.chat;

import com.egs.account.model.User;
import org.apache.commons.lang.builder.EqualsBuilder;

import javax.persistence.*;

@Entity
@Table(name = "friendship")
public class Friendship {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "INITIATOR_USER_ID")
    private User initiator;

    @ManyToOne(optional = false)
    @JoinColumn(name = "RECEIVER_USER_ID")
    private User receiver;

    private boolean isAccepted;

    public Friendship(User initiator, User receiver) {
        this.initiator = initiator;
        this.receiver = receiver;
    }

    public Friendship() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getInitiator() {
        return initiator;
    }

    public void setInitiator(User initiator) {
        this.initiator = initiator;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public void setAccepted(boolean accepted) {
        isAccepted = accepted;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof Friendship)) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        final Friendship other = (Friendship) obj;
        final EqualsBuilder builder = new EqualsBuilder();
        builder.append(this.id, other.id);
        return builder.isEquals();
    }

    @Override
    public String toString() {
        return "Friendship [id=" + id + ", initiator=" + getInitiator().getUsername() + ", receiver="
                + getReceiver().getUsername() + ", isAccepted=" + isAccepted + "]";
    }
}
