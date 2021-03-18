package mapper;

import org.springframework.stereotype.Repository;

import entities.TransactionTable;

@Repository
public interface TransactionRepository {

  void save(TransactionTable transactionTable);
  
  TransactionTable findByOtpAndLocker(String otp, String lockerId);
}
