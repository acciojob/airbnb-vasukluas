package com.driver.controllers;

import com.driver.model.Booking;
import com.driver.model.Facility;
import com.driver.model.Hotel;
import com.driver.model.User;
import org.springframework.stereotype.Repository;

<<<<<<< HEAD
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public class HotelManagementRepository {
    private  Map<String,Hotel>hotelDb=new HashMap<>();
    private Map<Integer, User>userDb=new HashMap<>();

    private Map<String,Booking>bookingDb=new HashMap<>();

    private Map<Integer,Integer>countOfBoookings=new HashMap<>();

    public String addHotel(Hotel hotel) {
    if(hotel.getHotelName()==null || hotel==null){
        return "FAILURE";
    }
     if(hotelDb.containsKey(hotel.getHotelName())){
         return "FAILURE";
    }

         hotelDb.put(hotel.getHotelName(),hotel);
     return "SUCCESS";
    }

    public int addUser(User user) {
        userDb.put(user.getaadharCardNo(),user);
        return user.getaadharCardNo();
    }

    public String getHotelWithMostFacilities() {
        int facilities=0;
        String HotelName="";
        for(Hotel hotel: hotelDb.values()){
            if(hotel.getFacilities().size() > facilities){
                facilities=hotel.getFacilities().size();
                HotelName=hotel.getHotelName();
            }
            else if(hotel.getFacilities().size()==facilities){
                if(hotel.getHotelName().compareTo(HotelName)<0){
                    HotelName=hotel.getHotelName();
                }
            }
        }
        return HotelName;
    }

    public int bookARoom(Booking booking) {
        String key= UUID.randomUUID().toString();
        booking.setBookingId(key);
        String hotalName= booking.getHotelName();
        Hotel hotel=hotelDb.get(hotalName);
        int avialbleRooms=hotel.getAvailableRooms();
        if(avialbleRooms < booking.getNoOfRooms()){
            return -1;
        }
        int amountToBePaid= hotel.getPricePerNight()*booking.getNoOfRooms();
        booking.setAmountToBePaid(amountToBePaid);

        hotel.setAvailableRooms(hotel.getAvailableRooms()-booking.getNoOfRooms());
        bookingDb.put(key,booking);
        hotelDb.put(hotalName,hotel);
        int aadhaarCard=booking.getBookingAadharCard();
        if(countOfBoookings.containsKey(aadhaarCard)){
            int totalbookings=countOfBoookings.get(aadhaarCard);
            countOfBoookings.put(aadhaarCard,totalbookings+1);
        }
        else {
            countOfBoookings.put(aadhaarCard,1);
        }
        return amountToBePaid;
    }

    public int getBookings(Integer aadharCard) {
        int totalbookings=countOfBoookings.get(aadharCard);
      return totalbookings;
    }

    public Hotel updateFacilities(List<Facility> newFacilities, String hotelName) {
        List<Facility>oldfacilities=hotelDb.get(hotelName).getFacilities();
         for(Facility facilities:newFacilities){
             if(oldfacilities.contains(facilities)){
                 continue;
             }
             else{
                 oldfacilities.add(facilities);
             }
         }
         Hotel hotel=hotelDb.get(hotelName);
         hotel.setFacilities(oldfacilities);
         hotelDb.put(hotelName,hotel);
         return hotel;
=======
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
>>>>>>> 943fc80f53413d15fac77352ccfcc742a56819c3
    }
}
