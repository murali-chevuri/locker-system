package entities;

import lombok.Builder;

@Builder
public class TransactionTable {

  private String otp;
  private LockerEntity lockerEntity;
  public String getOtp() {
    return otp;
  }
  public void setOtp(String otp) {
    this.otp = otp;
  }
  public LockerEntity getLockerEntity() {
    return lockerEntity;
  }
  public void setLockerEntity(LockerEntity lockerEntity) {
    this.lockerEntity = lockerEntity;
  }
  public TransactionTable(String otp, LockerEntity lockerEntity) {
    super();
    this.otp = otp;
    this.lockerEntity = lockerEntity;
  }

  
  
  
}
