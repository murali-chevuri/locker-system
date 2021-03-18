package services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import domain.Allocate;
import domain.Location;
import domain.Locker;
import entities.LocationEntity;
import entities.LockerEntity;
import entities.TransactionTable;
import exception.InvalidException;
import lombok.RequiredArgsConstructor;
import mapper.LocationRepository;
import mapper.TransactionRepository;
import services.LockerService;

@Service
@RequiredArgsConstructor
public class LockerServiceImpl implements LockerService {

  private final LocationRepository    locationRepository;

  private final TransactionRepository transactionRepository;

  @Override
  public List<Location> availableLocations(String address) {
    return locationRepository.findAllAvailableLocations(address)
        .stream()
        .map(l -> new Location(l.getLocationId(),
            l.getLockers().stream().map(lo -> new Locker(lo.getLockerId())).collect(Collectors.toList())))
        .collect(Collectors.toList());
  }

  @Override
  public Allocate allocate(String locationId) {
    LocationEntity locationEntity = locationRepository.findLocationEntity(locationId);
    LockerEntity locker = locationEntity.getLockers().get(0);
    String otp = generateOtp();
    transactionRepository.save(new TransactionTable(otp, locker));
    return new Allocate(new Locker(locker.getLockerId()), otp);
  }

  // Generates Randon OTP
  private String generateOtp() {
    return "TestOTP";
  }

  @Override
  public void unlock(Locker locker, String otp) throws InvalidException {
    TransactionTable transactionTable = transactionRepository.findByOtpAndLocker(otp, locker.getLockerId());
    if (transactionTable == null) {
      throw new InvalidException("Invalid Request");
    }
  }

}
