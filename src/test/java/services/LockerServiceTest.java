package services;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import domain.Allocate;
import domain.Location;
import domain.Locker;
import entities.LocationEntity;
import entities.LockerEntity;
import entities.TransactionTable;
import exception.InvalidException;
import mapper.LocationRepository;
import mapper.TransactionRepository;
import services.impl.LockerServiceImpl;

public class LockerServiceTest {

  @Mock
  private LocationRepository    locationRepository;

  @Mock
  private TransactionRepository transactionRepository;

  private LockerService         lockerService;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    lockerService = new LockerServiceImpl(locationRepository, transactionRepository);
  }

  @Test
  public void testAvailableLocations() {
    String address = "testAddress";
    LockerEntity lockerEntity = LockerEntity.builder().lockerId("lockerId").build();
    LocationEntity locationEntity =
        LocationEntity.builder().locationId("testLocationId").lockers(Arrays.asList(lockerEntity)).build();
    when(locationRepository.findAllAvailableLocations(address)).thenReturn(Arrays.asList(locationEntity));

    List<Location> locations = lockerService.availableLocations(address);

    verify(locationRepository, times(1)).findAllAvailableLocations(address);
    assertNotNull(locations);
    assertEquals(1, locations.size());
    assertEquals(locationEntity.getLocationId(), locations.get(0).getLocationId());
    assertNotNull(locations.get(0).getLockers());
    assertEquals(1, locations.get(0).getLockers().size());
    Locker locker = locations.get(0).getLockers().get(0);
    assertEquals(lockerEntity.getLockerId(), locker.getLockerId());

  }

  @Test
  public void testAvailableLocationsWithEmptyLockers() {
    String address = "testAddress";
    LocationEntity locationEntity =
        LocationEntity.builder().locationId("testLocationId").lockers(Collections.emptyList()).build();
    when(locationRepository.findAllAvailableLocations(address)).thenReturn(Arrays.asList(locationEntity));

    List<Location> locations = lockerService.availableLocations(address);

    verify(locationRepository, times(1)).findAllAvailableLocations(address);
    assertNotNull(locations);
    assertEquals(1, locations.size());
    assertEquals(locationEntity.getLocationId(), locations.get(0).getLocationId());
    assertNotNull(locations.get(0).getLockers());
    assertEquals(0, locations.get(0).getLockers().size());

  }

  @Test
  public void testAvailableLocationsWithEmptyLocations() {
    String address = "testAddress";
    when(locationRepository.findAllAvailableLocations(address)).thenReturn(Collections.emptyList());

    List<Location> locations = lockerService.availableLocations(address);

    verify(locationRepository, times(1)).findAllAvailableLocations(address);
    assertNotNull(locations);
    assertEquals(0, locations.size());
  }

  @Test
  public void testAllocate() {
    String locationId = "testLocationId";
    LockerEntity lockerEntity = LockerEntity.builder().lockerId("lockerId").build();
    LocationEntity locationEntity =
        LocationEntity.builder().locationId(locationId).lockers(Arrays.asList(lockerEntity)).build();
    when(locationRepository.findLocationEntity(locationId)).thenReturn(locationEntity);

    Allocate allocate = lockerService.allocate(locationId);

    verify(locationRepository, times(1)).findLocationEntity(locationId);
    verify(transactionRepository, times(1)).save(Mockito.any());
    assertNotNull(allocate);
    assertNotNull(allocate.getOtp());
    assertNotNull(allocate.getLocker());
    assertEquals(lockerEntity.getLockerId(), allocate.getLocker().getLockerId());
  }

  @Test
  public void testUnlock() throws InvalidException {
    String otp = "testOTP";
    String lockerId = "lockerId";
    TransactionTable transactionTable =
        TransactionTable.builder().lockerEntity(new LockerEntity(lockerId)).otp(otp).build();
    when(transactionRepository.findByOtpAndLocker(otp, lockerId)).thenReturn(transactionTable);

    lockerService.unlock(new Locker(lockerId), otp);

    verify(transactionRepository, times(1)).findByOtpAndLocker(otp, lockerId);
  }

  @Test
  public void testUnlockForInvalidException() throws InvalidException {
    String otp = "testOTP";
    String lockerId = "lockerId";
    when(transactionRepository.findByOtpAndLocker(otp, lockerId)).thenReturn(null);

    Exception exception = assertThrows(InvalidException.class, () -> {
      lockerService.unlock(new Locker(lockerId), otp);
    });
    String expectedMessage = "Invalid Request";
    String actualMessage = exception.getMessage();

    assertEquals(expectedMessage, actualMessage);
    verify(transactionRepository, times(1)).findByOtpAndLocker(otp, lockerId);
  }

}
