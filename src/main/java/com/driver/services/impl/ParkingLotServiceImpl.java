package com.driver.services.impl;

import com.driver.model.ParkingLot;
import com.driver.model.Spot;
import com.driver.model.SpotType;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.SpotRepository;
import com.driver.services.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ParkingLotServiceImpl implements ParkingLotService {
    @Autowired
    ParkingLotRepository parkingLotRepository1;
    @Autowired
    SpotRepository spotRepository1;

    @Override
    public ParkingLot addParkingLot(String name, String address) {
        ParkingLot parkingLot = new ParkingLot();
        parkingLot.setName(name);
        parkingLot.setAddress(address);
        parkingLot = parkingLotRepository1.save(parkingLot);
        return parkingLot;
    }

    @Override
    public Spot addSpot(int parkingLotId, Integer numberOfWheels, Integer pricePerHour) {
        // Check if parameters are null
        if (numberOfWheels == null || pricePerHour == null) {
            throw new IllegalArgumentException("numberOfWheels and pricePerHour cannot be null");
        }

        // Initialize spot object
        Spot spot = new Spot();
        spot.setPricePerHour(pricePerHour);
        spot.setOccupied(Boolean.FALSE);

        // Determine spot type based on the number of wheels
        if (numberOfWheels <= 2) {
            spot.setSpotType(SpotType.TWO_WHEELER);
        } else if (numberOfWheels <= 4) {
            spot.setSpotType(SpotType.FOUR_WHEELER);
        } else {
            spot.setSpotType(SpotType.OTHERS);
        }

        // Retrieve parking lot from repository
        Optional<ParkingLot> parkingLotOptional = parkingLotRepository1.findById(parkingLotId);
        if (!parkingLotOptional.isPresent()) {
            throw new IllegalArgumentException("Parking lot with ID " + parkingLotId + " not found");
        }
        ParkingLot parkingLot = parkingLotOptional.get();

        // Add spot to parking lot
        parkingLot.getSpotList().add(spot);
        spot.setParkingLot(parkingLot);

        // Save changes
        parkingLotRepository1.save(parkingLot);

        return spot;
    }

    @Override
    public void deleteSpot(int spotId) {
        if (spotRepository1.existsById(spotId) == false) return;
        spotRepository1.deleteById(spotId);
    }

    @Override
    public Spot updateSpot(int parkingLotId, int spotId, int pricePerHour) {
        ParkingLot parkingLot = parkingLotRepository1.findById(parkingLotId).get();
        Spot spot = null;
        for (Spot spot1 : parkingLot.getSpotList()) {
            if (spot1.getId() == spotId) {
                spot1.setPricePerHour(pricePerHour);
                spot = spot1;
                spotRepository1.save(spot1);
                break;
            }
        }
        return spot;
    }

    @Override
    public void deleteParkingLot(int parkingLotId) {
        parkingLotRepository1.deleteById(parkingLotId);
    }
}
