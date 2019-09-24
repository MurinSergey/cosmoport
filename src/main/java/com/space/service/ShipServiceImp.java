package com.space.service;

import com.space.controller.ShipOrder;
import com.space.exception.BadRequestException;
import com.space.exception.EntityNotFoundException;
import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
@Transactional
public class ShipServiceImp implements ShipService {
    private final ShipRepository shipRepository;

    private static final int MAX_LENGTH = 50;
    private static final int MIN_YEAR = 2800;
    private static final int CURRENT_YEAR = 3019;
    private static final Double MIN_SPEED = 0.01;
    private static final Double MAX_SPEED = 0.99;
    private static final Integer MIN_CREW_SIZE = 1;
    private static final Integer MAX_CREW_SIZE = 9999;

    @Autowired
    public ShipServiceImp(final ShipRepository shipRepository)
    {
        this.shipRepository = shipRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Ship> getShip(String name,
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
                              Integer pageSize)
    {
        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by(shipOrder.getFieldName()));
        if (isUsed == null)
        {
            return convertToList(shipRepository.getShip(
                    name,
                    planet,
                    shipType == null ? "" : shipType.name(),
                    getOnlyYear(after),
                    getOnlyYear(before),
                    minSpeed,
                    maxSpeed,
                    minCrewSize,
                    maxCrewSize,
                    minRating,
                    maxRating,
                    paging));
        }
        return convertToList(shipRepository.getShipUsed(
                name,
                planet,
                shipType == null ? "" : shipType.name(),
                getOnlyYear(after),
                getOnlyYear(before),
                isUsed,
                minSpeed,
                maxSpeed,
                minCrewSize,
                maxCrewSize,
                minRating,
                maxRating,
                paging));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Ship> getShip(String name,
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
                              Double maxRating)
    {
        if (isUsed == null)
        {
            return convertToList(shipRepository.getShip(
                    name,
                    planet,
                    shipType == null ? "" : shipType.name(),
                    getOnlyYear(after),
                    getOnlyYear(before),
                    minSpeed,
                    maxSpeed,
                    minCrewSize,
                    maxCrewSize,
                    minRating,
                    maxRating));
        }
        return convertToList(shipRepository.getShipUsed(
                name,
                planet,
                shipType == null ? "" : shipType.name(),
                getOnlyYear(after),
                getOnlyYear(before),
                isUsed,
                minSpeed,
                maxSpeed,
                minCrewSize,
                maxCrewSize,
                minRating,
                maxRating));

    }

    @Override
    @Transactional(readOnly = true)
    public Ship findShipById(Long id) {
        Optional<Ship> ship = shipRepository.findById(id);
        if (!ship.isPresent())
        {
            throw new EntityNotFoundException("Запись с таким ID не найдена");
        }
        return ship.get();
    }

    @Override
    @Transactional
    public Ship createShip(Ship newShip) {
        if (newShip == null)
        {
            throw new BadRequestException("Тело запроса равно null");
        }
        if (newShip.getUsed() == null) {
            newShip.setUsed(false);
        }
        if (newShip.getName() == null || !checkName(newShip.getName()) ||
                newShip.getPlanet() == null || !checkPlanet(newShip.getPlanet()) ||
                    newShip.getShipType() == null ||
                        newShip.getProdDate() == null || !checkDate(newShip.getProdDate()) ||
                            newShip.getSpeed() == null || !checkSpeed(newShip.getSpeed()) ||
                                newShip.getCrewSize() == null || !checkCrewSize(newShip.getCrewSize()))
        {
            throw new BadRequestException("Есть пустые поля");
        }

        newShip.setSpeed(round(newShip.getSpeed()));
        newShip.setRating(calcRating(newShip));

        return shipRepository.save(newShip);
    }

    @Override
    @Transactional
    public Ship updateShipById(Long id, Ship updateShip) {
        Ship ship = findShipById(id);

        if (updateShip == null)
        {
            throw new BadRequestException("Тело запроса равно null");
        }

        String name = updateShip.getName();
        String planet = updateShip.getPlanet();
        ShipType shipType = updateShip.getShipType();
        Date prodDate = updateShip.getProdDate();
        Boolean isUsed = updateShip.getUsed();
        Double speed = updateShip.getSpeed();
        Integer crewSize = updateShip.getCrewSize();

        //Если имя не null и имя соответсвует, то меняем
        if (name != null && checkName(name))
        {
            ship.setName(name);
        }
        //Если планета не null и планета соответсвует, то меняем
        if (planet != null && checkPlanet(planet))
        {
            ship.setPlanet(planet);
        }
        //Если тип коробля не null, то меняем
        if (shipType != null)
        {
            ship.setShipType(shipType);
        }
        //Если время не null и соответсвует, то меняем
        if (prodDate != null && checkDate(prodDate))
        {
            ship.setProdDate(prodDate);
        }
        //Если isUsed не null
        if (isUsed != null)
        {
            ship.setUsed(isUsed);
        }
        //Проверяем скорость
        if (speed != null && checkSpeed(speed))
        {
            ship.setSpeed(speed);
        }
        //Проверяем размер трюма
        if (crewSize != null && checkCrewSize(crewSize))
        {
            ship.setCrewSize(crewSize);
        }

        Double rating = calcRating(ship);
        ship.setRating(rating);

        return shipRepository.save(ship);
    }

    @Override
    @Transactional
    public void deleteShipById(Long id) {
        findShipById(id);
        shipRepository.deleteById(id);
    }

    //Конвертируем Iterable в List
    private List<Ship> convertToList(Iterable<Ship> ships)
    {
        List<Ship> allShips = new ArrayList<>();
        for (Ship ship : ships) {
            allShips.add(ship);
        }
        return allShips;
    }

    //Проверка имени и планеты
    private boolean checkName(String name)
    {
        if (name.length() > MAX_LENGTH || name.equals(""))
        {
            throw new BadRequestException(String.format("Название корабля более %d символов", MAX_LENGTH));
        }
        return true;
    }

    //Проверка имени и планеты
    private boolean checkPlanet(String planet)
    {
        if (planet.length() > MAX_LENGTH || planet.equals(""))
        {
            throw new BadRequestException(String.format("Название планеты более %d символов", MAX_LENGTH));
        }
        return true;
    }

    //Проверка даты
    private boolean checkDate(Date date)
    {
        Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(date.getTime());
        int year = calendar.get(Calendar.YEAR);
        if (year >= MIN_YEAR && year <= CURRENT_YEAR)
        {
            return true;
        }
        throw new BadRequestException(String.format("Дата должна быть от %d до %d", MIN_YEAR, CURRENT_YEAR));
    }

    //Проверка скорости
    private boolean checkSpeed(Double speed)
    {
        if (speed >= MIN_SPEED && speed <= MAX_SPEED)
        {
            return true;
        }
        throw new BadRequestException(String.format("Скорость должна быть от %f до %f", MIN_SPEED, MAX_SPEED));
    }

    //Проверяем размер трюма
    private boolean checkCrewSize(Integer crewSize)
    {
        if (crewSize >= MIN_CREW_SIZE && crewSize <= MAX_CREW_SIZE)
        {
            return true;
        }
        throw new BadRequestException(String.format("Размер трюма должен быть от %d до %d", MIN_CREW_SIZE, MAX_CREW_SIZE));
    }

    //Расчет рейтинга коробля
    private Double calcRating(Ship ship)
    {
        //коэффициент, который равен 1 для нового корабля и 0,5 для использованного
        double k = ship.getUsed() ? 0.5 : 1.0;
        //скорость корабля;
        double v = ship.getSpeed();
        int y0 = CURRENT_YEAR;
        int y1 = getYear(ship.getProdDate().getTime());
        double r = (80.0 * v * k) / (y0 - y1 + 1.0);
        return round(r);
    }

    //Округляем
    private Double round(double targetNumber)
    {
        BigDecimal scaleNumber = new BigDecimal(targetNumber);
        scaleNumber = scaleNumber.setScale(2, RoundingMode.HALF_UP);
        return scaleNumber.doubleValue();
    }

    //Возвращает только год, сбрасывая остальные значения даты
    private Date getOnlyYear(Long millis)
    {
        Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(millis);
        int year = calendar.get(Calendar.YEAR);
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        return calendar.getTime();
    }

    //Возвращает только год
    private Integer getYear(Long millis)
    {
        Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(millis);
        return calendar.get(Calendar.YEAR);
    }
}
