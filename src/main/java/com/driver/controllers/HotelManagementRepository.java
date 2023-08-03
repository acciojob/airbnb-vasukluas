package com.driver.controllers;

import com.driver.model.Booking;
import com.driver.model.Facility;
import com.driver.model.Hotel;
import com.driver.model.User;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public class HotelManagementRepository {
      static Map<String,Hotel>hotelDb=new HashMap<>();
      Map<Integer, User>userDb=new HashMap<>();

      Map<String,Booking>bookingDb=new HashMap<>();

      Map<Integer,Integer>countOfBoookings=new HashMap<>();

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

    public  String getHotelWithMostFacilities() {
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
    }
}
