package com.driver.controllers;

import com.driver.model.Booking;
import com.driver.model.Facility;
import com.driver.model.Hotel;
import com.driver.model.User;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class HotelManagementRepository {
    static Map<String,Hotel> hotelMap=new HashMap<>();
    Map<Integer,User> userMap=new HashMap<>();
    Map<String,Booking> bookingMap=new HashMap<>();
    final AtomicInteger aadharCardCounter = new AtomicInteger(1);


    public static String getHotelWithMostFacilities() {
        String HotelWithMostFacilities="";
        int maxFacilities=0;
        for(Hotel hotel:hotelMap.values()){
            List<Facility> facilities=hotel.getFacilities();
            if(facilities !=null && facilities.size()>maxFacilities){
                maxFacilities=facilities.size();
            } else if (facilities != null && facilities.size()==maxFacilities) {
                if(hotel.getHotelName().compareTo(HotelWithMostFacilities)<0){
                    HotelWithMostFacilities=hotel.getHotelName();
                }
            }
        }
        return HotelWithMostFacilities;
    }

    public String addHotel(Hotel hotel) {
        if(hotel.getHotelName()==null ){
            return "FAILURE";
        } else if (hotelMap.containsKey(hotel.getHotelName())) {
            return "FAILURE";
        }
        else{
            hotelMap.put(hotel.getHotelName(),hotel);
            return "SUCCESS";
        }
    }

    public Integer addUser(User user) {
        int adhaarCardNo=aadharCardCounter.getAndIncrement();
        user.setaadharCardNo(adhaarCardNo);
        userMap.put(adhaarCardNo,user);
        return adhaarCardNo;
    }

    public int bookARoom(Booking booking) {
    String BookingId= UUID.randomUUID().toString();
    booking.setBookingId(BookingId);
    String HotelName= booking.getHotelName();
    Hotel hotel=hotelMap.get(HotelName);
    int availableRooms= hotel.getAvailableRooms();
    if(booking.getNoOfRooms()>availableRooms){
        return -1;
    }
    int totalAmount=booking.getNoOfRooms()*hotel.getPricePerNight();
    booking.setAmountToBePaid(totalAmount);
    hotel.setAvailableRooms(availableRooms-booking.getNoOfRooms());
    bookingMap.put(BookingId,booking);
    return totalAmount;
    }

    public String getBookings(Integer aadharCard) {
       for(Booking booking:bookingMap.values()){
           if(booking.getBookingAadharCard()==aadharCard){
               return booking.getBookingPersonName();
           }
       }
       return null;
    }

    public Hotel updateFacilities(List<Facility> newFacilities, String hotelName) {
        Hotel hotel=hotelMap.get(hotelName);
        if(hotel==null){
            return null;
        }
     if(hotel.getFacilities()==null){
         hotel.setFacilities(new ArrayList<>());
     }
     for(Facility facility:newFacilities){
         if(!hotel.getFacilities().contains(facility)){
             hotel.setFacilities(Collections.singletonList(facility));
         }
     }
     hotelMap.put(hotelName,hotel);
    return (Hotel) hotel.getFacilities();
    }
}
