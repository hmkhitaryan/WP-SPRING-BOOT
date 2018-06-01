package com.egs.account.model.chat;

import com.egs.account.model.User;

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

    public Friendship(User initiator, User receiver) {
        this.initiator = initiator;
        this.receiver = receiver;
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
}
