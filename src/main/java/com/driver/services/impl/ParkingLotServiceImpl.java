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
        Optional<ParkingLot> parkingLotOpt = parkingLotRepository1.findById(parkingLotId);
        ParkingLot parkingLotObj = parkingLotOpt.get();

        Spot spotEntityObj = new Spot();
        if(numberOfWheels <= 2){
            spotEntityObj.setSpotType(SpotType.TWO_WHEELER);
        }else if(numberOfWheels <= 4){
            spotEntityObj.setSpotType(SpotType.FOUR_WHEELER);
        }else{
            spotEntityObj.setSpotType(SpotType.OTHERS);
        }
        spotEntityObj.setPricePerHour(pricePerHour);
        spotEntityObj.setParkingLot(parkingLotObj);
        spotEntityObj.setOccupied(Boolean.FALSE);
        List<Spot> spotList = parkingLotObj.getSpotList();
        spotList.add(spotEntityObj);
        parkingLotObj.setSpotList(spotList);
        parkingLotRepository1.save(parkingLotObj);
        return spotEntityObj;
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
