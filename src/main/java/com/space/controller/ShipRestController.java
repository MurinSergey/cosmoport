package com.space.controller;

import com.space.exception.BadRequestException;
import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/ships")
public class ShipRestController {

    private final String MIN_SPEED = "0";
    private final String MAX_SPEED = "1";
    private final String MIN_CREW_SIZE = "0";
    private final String MAX_CREW_SIZE = "10000";
    private final String MIN_RATING = "0";
    private final String MAX_RATING = "10000";
    private final String MIN_AFTER_DATE = "26192210400000";     //Минимальный год в милисекундах (2800)
    private final String MAX_BEFORE_DATE = "33134709600000";    //Максимальный год в милисекундах (3020) - нужно для прохождения тестов

    private final ShipService shipService;

    @Autowired
    public ShipRestController(final ShipService shipService)
    {
        this.shipService = shipService;
    }

    @GetMapping
    public List<Ship> getShips(@RequestParam(value = "name", defaultValue = "", required = false) String name,
                               @RequestParam(value = "planet", defaultValue = "", required = false) String planet,
                               @RequestParam(value = "shipType", defaultValue = "", required = false) ShipType shipType,
                               @RequestParam(value = "after", defaultValue = MIN_AFTER_DATE, required = false) Long after,
                               @RequestParam(value = "before", defaultValue = MAX_BEFORE_DATE, required = false) Long before,
                               @RequestParam(value = "isUsed", defaultValue = "", required = false) Boolean isUsed,
                               @RequestParam(value = "minSpeed", defaultValue = MIN_SPEED, required = false) Double minSpeed,
                               @RequestParam(value = "maxSpeed", defaultValue = MAX_SPEED, required = false) Double maxSpeed,
                               @RequestParam(value = "minCrewSize", defaultValue = MIN_CREW_SIZE, required = false) Integer minCrewSize,
                               @RequestParam(value = "maxCrewSize", defaultValue = MAX_CREW_SIZE, required = false) Integer maxCrewSize,
                               @RequestParam(value = "minRating", defaultValue = MIN_RATING, required = false) Double minRating,
                               @RequestParam(value = "maxRating", defaultValue = MAX_RATING, required = false) Double maxRating,
                               @RequestParam(value = "order", defaultValue = "ID") ShipOrder shipOrder,
                               @RequestParam(value = "pageNumber", defaultValue = "0") Integer pageNumber,
                               @RequestParam(value = "pageSize", defaultValue = "3") Integer pageSize) {
        return shipService.getShip( name,
                                    planet,
                                    shipType,
                                    after,
                                    before,
                                    isUsed,
                                    minSpeed,
                                    maxSpeed,
                                    minCrewSize,
                                    maxCrewSize,
                                    minRating,
                                    maxRating,
                                    shipOrder,
                                    pageNumber,
                                    pageSize);
    }

    @GetMapping("/count")
    public Integer getCount(@RequestParam(value = "name", defaultValue = "", required = false) String name,
                            @RequestParam(value = "planet", defaultValue = "", required = false) String planet,
                            @RequestParam(value = "shipType", defaultValue = "", required = false) ShipType shipType,
                            @RequestParam(value = "after", defaultValue = MIN_AFTER_DATE, required = false) Long after,
                            @RequestParam(value = "before", defaultValue = MAX_BEFORE_DATE, required = false) Long before,
                            @RequestParam(value = "isUsed", defaultValue = "", required = false) Boolean isUsed,
                            @RequestParam(value = "minSpeed", defaultValue = MIN_SPEED, required = false) Double minSpeed,
                            @RequestParam(value = "maxSpeed", defaultValue = MAX_SPEED, required = false) Double maxSpeed,
                            @RequestParam(value = "minCrewSize", defaultValue = MIN_CREW_SIZE, required = false) Integer minCrewSize,
                            @RequestParam(value = "maxCrewSize", defaultValue = MAX_CREW_SIZE, required = false) Integer maxCrewSize,
                            @RequestParam(value = "minRating", defaultValue = MIN_RATING, required = false) Double minRating,
                            @RequestParam(value = "maxRating", defaultValue = MAX_RATING, required = false) Double maxRating) {
        return shipService.getShip( name,
                                    planet,
                                    shipType,
                                    after,
                                    before,
                                    isUsed,
                                    minSpeed,
                                    maxSpeed,
                                    minCrewSize,
                                    maxCrewSize,
                                    minRating,
                                    maxRating).size();
    }

    @GetMapping("/{id}")
    public @ResponseBody Ship getShipById(@PathVariable String id)
    {
        return shipService.findShipById(checkID(id));
    }

    @PostMapping
    public @ResponseBody Ship createShip(@RequestBody Ship ship)
    {
        return shipService.createShip(ship);
    }

    @PostMapping("/{id}")
    public @ResponseBody Ship updateShip (@PathVariable String id, @RequestBody Ship updateShip)
    {
        Long localID = checkID(id);
        updateShip.setId(localID);
        return shipService.updateShipById(localID, updateShip);
    }

    @DeleteMapping("/{id}")
    public @ResponseBody ResponseEntity<?> deleteShip(@PathVariable String id)
    {
        shipService.deleteShipById(checkID(id));
        return new ResponseEntity(HttpStatus.OK);
    }

    //Проверяем id
    private Long checkID(String id)
    {
        try{
            Long tryID = Long.parseLong(id);
            if (tryID <= 0)
            {
                throw new BadRequestException("Поле ID меньше или равно 0");
            }
            return tryID;
        }
        catch (Exception e)
        {
            throw new BadRequestException("Поле ID должно быть целым числом");
        }
    }
}
