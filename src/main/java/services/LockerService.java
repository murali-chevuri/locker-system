package services;

import java.util.List;

import domain.Allocate;
import domain.Location;
import domain.Locker;
import exception.InvalidException;

public interface LockerService {
  
  List<Location> availableLocations(String address);

  Allocate allocate(String locationId);

  void unlock(Locker locker, String otp) throws InvalidException;

}
