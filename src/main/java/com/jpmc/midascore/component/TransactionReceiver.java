package com.jpmc.midascore.component;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TransactionReceiver {
    static final Logger logger = LoggerFactory.getLogger(TransactionReceiver.class);
    
    private final DatabaseConduit databaseConduit;

    public TransactionReceiver(DatabaseConduit databaseConduit) {
        this.databaseConduit = databaseConduit;
    }

    @KafkaListener(topics = "${general.kafka-topic}", groupId = "midas")
    public void receive(Transaction transaction) {
        logger.info("Received transaction: {}", transaction);
        
        // Validate transaction
        if (isValid(transaction)) {
            // Update sender's balance
            UserRecord sender = databaseConduit.findUserById(transaction.getSenderId());
            sender.setBalance(sender.getBalance() - transaction.getAmount());
            databaseConduit.save(sender);
            
            // Update recipient's balance
            UserRecord recipient = databaseConduit.findUserById(transaction.getRecipientId());
            recipient.setBalance(recipient.getBalance() + transaction.getAmount());
            databaseConduit.save(recipient);
            
            // Save transaction record
            TransactionRecord transactionRecord = new TransactionRecord(
                transaction.getSenderId(),
                transaction.getRecipientId(),
                transaction.getAmount()
            );
            databaseConduit.saveTransaction(transactionRecord);
            
            logger.info("Transaction processed successfully");
        } else {
            logger.warn("Invalid transaction rejected: {}", transaction);
        }
    }

    private boolean isValid(Transaction transaction) {
        // Check if sender exists and is valid
        UserRecord sender = databaseConduit.findUserById(transaction.getSenderId());
        if (sender == null) {
            logger.warn("Invalid senderId: {}", transaction.getSenderId());
            return false;
        }

        // Check if recipient exists and is valid
        UserRecord recipient = databaseConduit.findUserById(transaction.getRecipientId());
        if (recipient == null) {
            logger.warn("Invalid recipientId: {}", transaction.getRecipientId());
            return false;
        }

        // Check if sender has sufficient balance
        if (sender.getBalance() < transaction.getAmount()) {
            logger.warn("Insufficient balance for senderId {}: has {}, needs {}",
                transaction.getSenderId(), sender.getBalance(), transaction.getAmount());
            return false;
        }

        return true;
    }
}
