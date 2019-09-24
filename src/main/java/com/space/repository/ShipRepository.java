package com.space.repository;

import com.space.model.Ship;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;

public interface ShipRepository extends PagingAndSortingRepository<Ship, Long> {
    @Query("SELECT t FROM Ship t WHERE " +
            "(LOWER(t.name) LIKE LOWER(CONCAT('%', :name, '%')) AND " +
            "LOWER(t.planet) LIKE LOWER(CONCAT('%', :planet, '%')) AND " +
            "LOWER(t.shipType) LIKE LOWER(CONCAT('%', :shipType)) AND " +
            "(t.prodDate BETWEEN :after AND :before) AND " +
            "(t.speed BETWEEN :minSpeed AND :maxSpeed) AND " +
            "(t.crewSize BETWEEN :minCrewSize AND :maxCrewSize) AND " +
            "(t.rating BETWEEN :minRating AND :maxRating))")
    Page<Ship> getShip(@Param("name") String name,
                       @Param("planet") String planet,
                       @Param("shipType") String shipType,
                       @Param("after") Date after,
                       @Param("before") Date before,
                       @Param("minSpeed") Double minSpeed,
                       @Param("maxSpeed") Double maxSpeed,
                       @Param("minCrewSize") Integer minCrewSize,
                       @Param("maxCrewSize") Integer maxCrewSize,
                       @Param("minRating") Double minRating,
                       @Param("maxRating") Double maxRating,
                       Pageable pageable);

    @Query("SELECT t FROM Ship t WHERE " +
            "(LOWER(t.name) LIKE LOWER(CONCAT('%', :name, '%')) AND " +
            "LOWER(t.planet) LIKE LOWER(CONCAT('%', :planet, '%')) AND " +
            "LOWER(t.shipType) LIKE LOWER(CONCAT('%', :shipType)) AND " +
            "(t.prodDate BETWEEN :after AND :before) AND " +
            "(t.speed BETWEEN :minSpeed AND :maxSpeed) AND " +
            "(t.crewSize BETWEEN :minCrewSize AND :maxCrewSize) AND " +
            "(t.rating BETWEEN :minRating AND :maxRating))")
    Iterable<Ship> getShip(@Param("name") String name,
                           @Param("planet") String planet,
                           @Param("shipType") String shipType,
                           @Param("after") Date after,
                           @Param("before") Date before,
                           @Param("minSpeed") Double minSpeed,
                           @Param("maxSpeed") Double maxSpeed,
                           @Param("minCrewSize") Integer minCrewSize,
                           @Param("maxCrewSize") Integer maxCrewSize,
                           @Param("minRating") Double minRating,
                           @Param("maxRating") Double maxRating);

    @Query("SELECT t FROM Ship t WHERE " +
            "(LOWER(t.name) LIKE LOWER(CONCAT('%', :name, '%')) AND " +
            "LOWER(t.planet) LIKE LOWER(CONCAT('%', :planet, '%')) AND " +
            "LOWER(t.shipType) LIKE LOWER(CONCAT('%', :shipType)) AND " +
            "(t.prodDate BETWEEN :after AND :before) AND " +
            "(t.speed BETWEEN :minSpeed AND :maxSpeed) AND " +
            "(t.crewSize BETWEEN :minCrewSize AND :maxCrewSize) AND " +
            "(t.rating BETWEEN :minRating AND :maxRating) AND " +
            "t.isUsed = :isUsed)")
    Page<Ship> getShipUsed(
            @Param("name") String name,
            @Param("planet") String planet,
            @Param("shipType") String shipType,
            @Param("after") Date after,
            @Param("before") Date before,
            @Param("isUsed") Boolean isUsed,
            @Param("minSpeed") Double minSpeed,
            @Param("maxSpeed") Double maxSpeed,
            @Param("minCrewSize") Integer minCrewSize,
            @Param("maxCrewSize") Integer maxCrewSize,
            @Param("minRating") Double minRating,
            @Param("maxRating") Double maxRating,
            Pageable pageable);

    @Query("SELECT t FROM Ship t WHERE " +
            "(LOWER(t.name) LIKE LOWER(CONCAT('%', :name, '%')) AND " +
            "LOWER(t.planet) LIKE LOWER(CONCAT('%', :planet, '%')) AND " +
            "LOWER(t.shipType) LIKE LOWER(CONCAT('%', :shipType)) AND " +
            "(t.prodDate BETWEEN :after AND :before) AND " +
            "(t.speed BETWEEN :minSpeed AND :maxSpeed) AND " +
            "(t.crewSize BETWEEN :minCrewSize AND :maxCrewSize) AND " +
            "(t.rating BETWEEN :minRating AND :maxRating) AND " +
            "t.isUsed = :isUsed)")
    Iterable<Ship> getShipUsed(
            @Param("name") String name,
            @Param("planet") String planet,
            @Param("shipType") String shipType,
            @Param("after") Date after,
            @Param("before") Date before,
            @Param("isUsed") Boolean isUsed,
            @Param("minSpeed") Double minSpeed,
            @Param("maxSpeed") Double maxSpeed,
            @Param("minCrewSize") Integer minCrewSize,
            @Param("maxCrewSize") Integer maxCrewSize,
            @Param("minRating") Double minRating,
            @Param("maxRating") Double maxRating);
}