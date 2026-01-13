package com.jpmc.midascore.entity;

import jakarta.persistence.*;

@Entity
public class TransactionRecord {

    @Id
    @GeneratedValue()
    private long id;

    @Column(nullable = false)
    private long senderId;

    @Column(nullable = false)
    private long recipientId;

    @Column(nullable = false)
    private float amount;

    @Column(nullable = false)
    private float incentive;

    protected TransactionRecord() {
    }

    public TransactionRecord(long senderId, long recipientId, float amount, float incentive) {
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.amount = amount;
        this.incentive = incentive;
    }

    public long getId() {
        return id;
    }

    public long getSenderId() {
        return senderId;
    }

    public long getRecipientId() {
        return recipientId;
    }

    public float getAmount() {
        return amount;
    }

    public float getIncentive() {
        return incentive;
    }

    @Override
    public String toString() {
        return String.format("TransactionRecord[id=%d, senderId=%d, recipientId=%d, amount=%f, incentive=%f]",
            id, senderId, recipientId, amount, incentive);
    }
}
