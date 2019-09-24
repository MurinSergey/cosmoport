package com.space.service;

import com.space.controller.ShipOrder;
import com.space.model.Ship;
import com.space.model.ShipType;

import java.util.List;

public interface ShipService {

    List<Ship> getShip(String name,
                       String planet,
                       ShipType shipType,
                       Long after,
                       Long before,
                       Boolean isUsed,
                       Double minSpeed,
                       Double maxSpeed,
                       Integer minCrewSize,
                       Integer maxCrewSize,
                       Double minRating,
                       Double maxRating,
                       ShipOrder shipOrder,
                       Integer pageNumber,
                       Integer pageSize);

    List<Ship> getShip(String name,
                       String planet,
                       ShipType shipType,
                       Long after,
                       Long before,
                       Boolean isUsed,
                       Double minSpeed,
                       Double maxSpeed,
                       Integer minCrewSize,
                       Integer maxCrewSize,
                       Double minRating,
                       Double maxRating);

    Ship findShipById(Long id);

    Ship createShip(Ship newShip);

    Ship updateShipById(Long id, Ship updateShip);

    void deleteShipById(Long id);


}
