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
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class HotelManagementRepository {
      static Map<String,Hotel>hotelDb=new HashMap<>();
      Map<UUID, User> userDb=new HashMap<UUID, User>();

      Map<UUID, Booking> bookingDb=new HashMap<UUID, Booking>();

      Map<Integer,Integer>countOfBookings=new HashMap<>();
    final AtomicInteger aadharCardCounter = new AtomicInteger(1);

    public String addHotel(Hotel hotel) {
    if(hotel.getHotelName() == null ){
        return "FAILURE";
    }
     else if(hotelDb.containsKey(hotel.getHotelName())){
         return "FAILURE";
    }
     else{
        hotelDb.put(hotel.getHotelName(),hotel);
        return "SUCCESS";
    }

    }

    public UUID addUser(User user) {
        int adhaarCardNo=aadharCardCounter.getAndIncrement();
        user.setaadharCardNo(adhaarCardNo);
        return user.getaadharCardNo();
    }

    public  String getHotelWithMostFacilities() {
        int facilities=0;
        String HotelName="";
        for(Hotel hotel: hotelDb.values()){
            if(hotel.getFacilities()!=null &&   hotel.getFacilities().size() > facilities){
                facilities=hotel.getFacilities().size();
//                HotelName=hotel.getHotelName();
            }
            else if(hotel.getFacilities()!=null && hotel.getFacilities().size()==facilities){
                if(hotel.getHotelName().compareTo(HotelName)<0){
                    HotelName=hotel.getHotelName();
                }
            }
        }
        return HotelName;
    }

    public int bookARoom(Booking booking) {
        String key= UUID.randomUUID().toString();
        booking.setBookingId(UUID.fromString(key));
        String hotelName= booking.getHotelName();
        Hotel hotel=hotelDb.get(hotelName);
        int avialbleRooms=hotel.getAvailableRooms();
        if(avialbleRooms < booking.getNoOfRooms()){
            return -1;
        }
        int amountToBePaid= hotel.getPricePerNight()*booking.getNoOfRooms();
        booking.setAmountToBePaid(amountToBePaid);

        hotel.setAvailableRooms(hotel.getAvailableRooms()-booking.getNoOfRooms());
        bookingDb.put(UUID.fromString(key),booking);
        hotelDb.put(hotelName,hotel);
        int aadhaarCard=booking.getBookingAadharCard();
        if(countOfBookings.containsKey(aadhaarCard)){
            int totalbookings=countOfBookings.get(aadhaarCard);
            countOfBookings.put(aadhaarCard,totalbookings+1);
        }
        else {
            countOfBookings.put(aadhaarCard,1);
        }
        return amountToBePaid;
    }

    public int getBookings(Integer aadharCard) {
        return countOfBookings.get(aadharCard);
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
