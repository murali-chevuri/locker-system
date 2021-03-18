package mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import entities.LocationEntity;

@Repository
public interface LocationRepository {
  
  List<LocationEntity> findAllAvailableLocations(String address);
  
  LocationEntity findLocationEntity(String locationId);

}
