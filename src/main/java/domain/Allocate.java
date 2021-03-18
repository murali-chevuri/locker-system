package domain;

public class Allocate {
  
  Locker locker;
  
  String otp;

  public Locker getLocker() {
    return locker;
  }

  public void setLocker(Locker locker) {
    this.locker = locker;
  }

  public String getOtp() {
    return otp;
  }

  public void setOtp(String otp) {
    this.otp = otp;
  }

  public Allocate(Locker locker, String otp) {
    super();
    this.locker = locker;
    this.otp = otp;
  }
  
  
}
