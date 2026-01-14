package com.jpmc.midascore.component;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.repository.TransactionRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class DatabaseConduit {
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    public DatabaseConduit(UserRepository userRepository, TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    public void save(UserRecord userRecord) {
        userRepository.save(userRecord);
    }

    public UserRecord findUserById(long id) {
        return userRepository.findById(id);
    }

    public float queryUserBalance(long userId) {
        UserRecord user = userRepository.findById(userId);
        return user == null ? 0f : user.getBalance();
    }

    public Iterable<UserRecord> findAllUsers() {
        return userRepository.findAll();
    }

    public void saveTransaction(TransactionRecord transactionRecord) {
        transactionRepository.save(transactionRecord);
    }

}
