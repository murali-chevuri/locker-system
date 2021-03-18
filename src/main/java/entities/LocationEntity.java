package entities;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class LocationEntity {

  private String locationId;
  
  private List<LockerEntity> lockers;
  
}
