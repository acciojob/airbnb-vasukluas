//package com.driver.controllers;
//
//import com.driver.model.Booking;
//import com.driver.model.Facility;
//import com.driver.model.Hotel;
//import com.driver.model.User;
//import org.springframework.stereotype.Repository;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.UUID;
//import java.util.concurrent.atomic.AtomicInteger;
//
//@Repository
//public class HotelManagementRepository {
//    static Map<String, Hotel> hotelDb = new HashMap<>();
//    Map<Integer, User> userDb = new HashMap<>();
//
//    Map<String, Booking> bookingDb = new HashMap<>();
//
//    Map<Integer, Integer> countOfBookings = new HashMap<>();
////    final AtomicInteger aadharCardCounter = new AtomicInteger(1);
//
//    public String addHotel(Hotel hotel) {
//        if (hotel==null || hotel.getHotelName() == null) {
//            return "FAILURE";
//        } else if (hotelDb.containsKey(hotel.getHotelName())) {
//            return "FAILURE";
//        } else {
//            hotelDb.put(hotel.getHotelName(), hotel);
//            return "SUCCESS";
//        }
//
//    }
//
//    public int addUser(User user) {
////        int adhaarCardNo=aadharCardCounter.getAndIncrement();
////        user.setaadharCardNo(adhaarCardNo);
//        userDb.put(user.getaadharCardNo(), user);
//        return user.getaadharCardNo();
//    }
//
//    public String getHotelWithMostFacilities() {
//        int facilities = 0;
//        String HotelName = "";
//
//        for (Hotel hotel : hotelDb.values()) {
//
////            if (hotel.getFacilities() == null) continue;
//
//            if (hotel.getFacilities().size() > facilities) {
//                facilities = hotel.getFacilities().size();
//                HotelName=hotel.getHotelName();
//            } else if (hotel.getFacilities().size() == facilities) {
//                if (hotel.getHotelName().compareTo(HotelName) < 0) {
//                    HotelName = hotel.getHotelName();
//                }
//            }
//        }
//        return HotelName;
//    }
//
//    public int bookARoom(Booking booking) {
//        String key = UUID.randomUUID().toString();
//        booking.setBookingId(key);
//        String hotelName = booking.getHotelName();
//        Hotel hotel = hotelDb.get(hotelName);
//        int avialbleRooms = hotel.getAvailableRooms();
//        if (avialbleRooms < booking.getNoOfRooms()) {
//            return -1;
//        }
//        int amountToBePaid = hotel.getPricePerNight() * booking.getNoOfRooms();
//        booking.setAmountToBePaid(amountToBePaid);
//
//        hotel.setAvailableRooms(hotel.getAvailableRooms() - booking.getNoOfRooms());
//        bookingDb.put(key, booking);
//        hotelDb.put(hotelName, hotel);
//        int aadhaarCard = booking.getBookingAadharCard();
//        if (countOfBookings.containsKey(aadhaarCard)) {
//            int totalbookings = countOfBookings.get(aadhaarCard);
//            countOfBookings.put(aadhaarCard, totalbookings + 1);
//        } else {
//            countOfBookings.put(aadhaarCard, 1);
//        }
//        return amountToBePaid;
//    }
//
//    public int getBookings(Integer aadharCard) {
//        return countOfBookings.get(aadharCard);
//    }
//
//    public Hotel updateFacilities(List<Facility> newFacilities, String hotelName) {
//        List<Facility> oldfacilities = hotelDb.get(hotelName).getFacilities();
//        for (Facility facilities : newFacilities) {
//            if (oldfacilities.contains(facilities)) {
//                continue;
//            } else {
//                oldfacilities.add(facilities);
//            }
//        }
//        Hotel hotel = hotelDb.get(hotelName);
//        hotel.setFacilities(oldfacilities);
//        hotelDb.put(hotelName, hotel);
//        return hotel;
//    }
//}
package com.driver.controllers;

import com.driver.model.Booking;
import com.driver.model.Facility;
import com.driver.model.Hotel;
import com.driver.model.User;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class HotelManagementRepository {
    Map<String,Hotel> hotelMap = new HashMap<>();
    Map<Integer,User> userMap = new HashMap<>();
    Map<String,Booking> bookingMap= new HashMap<>();
    Map<Integer,Integer> countMap = new HashMap<>();

    public String addHotel(Hotel hotel) {
        if(hotel==null || hotel.getHotelName()==null || hotelMap.containsKey(hotel.getHotelName()))return "FAILURE";
        hotelMap.put(hotel.getHotelName(),hotel);
        return "SUCCESS";
    }

    public Integer addUser(User user) {
        userMap.put(user.getaadharCardNo(),user);
        return user.getaadharCardNo();
    }

    public String getHotelWithMostFacilities() {
        int facility=0;
        String hotelName="";
        for(Hotel hotel : hotelMap.values()){
            if(hotel.getFacilities().size()>facility){
                facility=hotel.getFacilities().size();
                hotelName=hotel.getHotelName();
            }
            else if(hotel.getFacilities().size()==facility){
                if(hotel.getHotelName().compareTo(hotelName)<0){
                    hotelName=hotel.getHotelName();
                }
            }
        }
        return hotelName;
    }

    public int bookARoom(Booking booking) {
        String bookingID= UUID.randomUUID().toString();
        booking.setBookingId(bookingID);
        String hotelName =booking.getHotelName();
        Hotel hotel = hotelMap.get(hotelName);
        if(booking.getNoOfRooms()>hotel.getAvailableRooms())return -1;

        int amount = booking.getNoOfRooms()*hotel.getPricePerNight();
        booking.setAmountToBePaid(amount);

        hotel.setAvailableRooms(hotel.getAvailableRooms()-booking.getNoOfRooms());
        bookingMap.put(bookingID,booking);
        hotelMap.put(hotelName,hotel);

        int aadhar = booking.getBookingAadharCard();
        Integer bookings = countMap.get(aadhar);
        countMap.put(aadhar,(bookings==null)?1:bookings+1);
        return amount;

    }

    public int getBookings(Integer aadharCard) {
        return countMap.get(aadharCard);
    }

    public Hotel updateFacilities(List<Facility> newFacilities, String hotelName) {
        List<Facility> old = hotelMap.get(hotelName).getFacilities();
        for(Facility facility:newFacilities){
            if(old.contains(facility))continue;
            else old.add(facility);
        }
        Hotel hotel= hotelMap.get(hotelName);
        hotel.setFacilities(old);
        hotelMap.put(hotelName,hotel);
        return hotel;
    }
}