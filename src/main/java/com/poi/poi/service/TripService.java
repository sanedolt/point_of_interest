package com.poi.poi.service;

import com.poi.poi.exception.PoiAlreadyExistInTripException;
import com.poi.poi.exception.PoiNotExistException;
import com.poi.poi.exception.TripNotExistException;
import com.poi.poi.model.Poi;
import com.poi.poi.model.Trip;
import com.poi.poi.model.User;
import com.poi.poi.repository.PoiRepository;
import com.poi.poi.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class TripService {
    private TripRepository tripRepository;
    private PoiRepository poiRepository;

    @Autowired
    public TripService(TripRepository tripRepository, PoiRepository poiRepository){
        this.tripRepository = tripRepository;
        this.poiRepository = poiRepository;
    }

    public void addTrip(Trip trip){
        tripRepository.save(trip);
    }

    public void saveTrip(Long tripId, String plan, String username) throws TripNotExistException {
        Trip trip = tripRepository.findByTripIdAndUser(tripId, new User.Builder().setUsername(username).build());
        if (trip == null) {
            throw new TripNotExistException("Trip doesn't exist");
        }
        trip.setPlan(plan);
        tripRepository.save(trip);
    }

    public List<Trip> getTripByUser(String username){
        return tripRepository.findByUser(new User.Builder().setUsername(username).build());
//        List<Trip> ans = tripRepository.findByUser(new User.Builder().setUsername(username).build());
//        List<Trip> res = new ArrayList<>();
//        for (Trip t : ans) {
//            if (t.getTripId()>115) {res.add(t);} // previous tests have issues and cannot be deleted due to dependency
//        }
//        return res;
    }

    public List<Trip> getUpcomingTripByUser(String username){
        List<Trip> ans = tripRepository.findByUser(new User.Builder().setUsername(username).build());
        List<Trip> res = new ArrayList<>();

        for (Trip t : ans) {
            if (!t.getCheckoutDate().isBefore(LocalDate.now())) {
                res.add(t);
            }
        }
        return res;
    }

    public List<Trip> getPastTripByUser(String username){
        List<Trip> ans = tripRepository.findByUser(new User.Builder().setUsername(username).build());
        List<Trip> res = new ArrayList<>();

        for (Trip t : ans) {
            if (t.getCheckoutDate().isBefore(LocalDate.now())) {
                res.add(t);
            }
        }
        Collections.sort(res,new Comparator<Trip>(){
           @Override
           public int compare (Trip a, Trip b) {
               LocalDate ea = a.getCheckoutDate();
               LocalDate eb = b.getCheckoutDate();
               LocalDate sa = a.getCheckinDate();
               LocalDate sb = b.getCheckinDate();
               if (ea.equals(eb)) {
                   return sb.compareTo(sa);
               } else {
                   return eb.compareTo(ea);
               }
           }
        });
        return res;
    }

    public Long getNewestTripIDByUser(String username){
        List<Trip> trips = getTripByUser(username);
        String max = "00000000";
        Long curID = 0L;
        for (Trip trip : trips) {
            String cur = trip.getName();
            if (Character.isDigit(cur.charAt(0)) && cur.compareTo(max)>0) {
                max=cur;
                curID=trip.getTripId();
            }
        }
        return curID;
    }

    private void addItem(StringBuilder ans, Poi cur, int day, double beg, boolean start) {
        if (start) {
            if (day > 1) {
                ans.append(",");
            }
            ans.append("D");
            ans.append(day);
            ans.append("#");
        }
        ans.append((int)beg);
        ans.append(":");
        int min = (int)((beg-(int)beg)*60);
        ans.append(min==0?"00":"30");
        ans.append("-");
        double end=cur.getTimeTaken()+beg;
        ans.append((int)end);
        ans.append(":");
        min = (int)((end-(int)end)*60);
        ans.append(min==0?"00":"30");
        ans.append(":");
        ans.append(cur.getId());
        ans.append("#");
    }
    public void createPlan(Trip trip,List<Poi> choices){
        Collections.sort(choices,(a,b)->Double.compare(b.getTimeTaken(),a.getTimeTaken()));
        StringBuilder ans = new StringBuilder();
        LocalDate date1 = trip.getCheckinDate();
        LocalDate date2 = trip.getCheckoutDate();
        int days = 1;
        while (date1.isBefore(date2)) {
            days++;
            date1=date1.plusDays(1);
        }
        int size=choices.size();
        int dif = size-days, day=1;
        if (dif<=0 || choices.get(size-1).getTimeTaken()>=5.0) {
            for (int idx=0;idx<size;idx++) {
                addItem(ans,choices.get(idx),day++,10.0,true);
            }
        } else {
            int idx=0;
            boolean[] visited = new boolean[size];
            for (; idx < size; idx++) {
                Poi cur = choices.get(idx);
                if (cur.getTimeTaken() < 5.0) {break;}
                addItem(ans, cur, day++, 10.0,true);
                visited[idx] = true;
            }
            // now it's possible to merge
            if (choices.get(Math.max(size-dif*2,0)).getTimeTaken()>=5) {
                // eager merge
                for (; idx < size; idx++) {
                    if (visited[idx]) {continue;}
                    Poi cur = choices.get(idx);
                    double maxLeft=8.0;
                    List<Long> using = new ArrayList<>();
                    List<Double> timing = new ArrayList<>();
                    using.add((long) idx);
                    timing.add(10.0);
                    maxLeft-=cur.getTimeTaken();
                    while (maxLeft>=1) {
                        maxLeft -= 1;
                        // find the largest time that can fit into maxLeft
                        // due to dedup, not that straightforward for bindary search
                        int next = idx + 1;
                        while (next < size && choices.get(next).getTimeTaken() > maxLeft) {
                            next++;
                        }
                        while (next < size && visited[next]) {
                            next++;
                        }
                        if (next == size) {
                            break; // while
                        } else {
                            using.add((long) next);
                            timing.add(timing.get(timing.size()-1)+choices.get(timing.size()-1).getTimeTaken()+1);
                            visited[next] = true;
                            maxLeft -= choices.get(next).getTimeTaken();
                        }
                    }
                    for (int i=0;i<using.size();i++) {
                        long u = using.get(i);
                        int ii = (int) u;
                        addItem(ans, choices.get(ii), day, timing.get(i),i==0);
                        visited[ii] = true;
                    }
                    if (++day>days) {break;}
                }
            } else { // lazy merge
                for (; idx<size-dif*2; idx++) {
                    addItem(ans,choices.get(idx),day++,10.0,true);
                }
                int right=size-1;
                while (idx<right) {
                    addItem(ans,choices.get(idx),day,10.0,true);
                    addItem(ans,choices.get(right),day,10+1+choices.get(idx).getTimeTaken(),false);
                    idx++;right--;day++;
                }
            }
        }
        System.out.println(ans);
        trip.setPlan(ans.toString());
        tripRepository.save(trip);
    }

    public List<List<Poi>> getPlanFromTrip(Long tripId, String username) throws TripNotExistException{
        Trip trip = tripRepository.findByTripIdAndUser(tripId, new User.Builder().setUsername(username).build());
        if(trip == null){
            throw new TripNotExistException("Trip doesn't exist");
        }
        List<List<Poi>> ans = new ArrayList<>();
        List<Poi> choices = new ArrayList<>(trip.getPoiSet());
        if (choices.size()==0) {trip.setPlan("");return ans;}
        String input=trip.getPlan();
        if (input.equals("")) {
            createPlan(trip, choices);
            input=trip.getPlan();
        }
        while (true) {
            int numOfPois = 0;
            String[] day = input.split(",");
            for (int i = 0; i < day.length; i++) {
                String[] part = day[i].split("#");
                ans.add(new ArrayList<>());
                for (int j = 1; j < part.length; j++) {
                    long idx = 0L;
                    for (int k = 12; k < part[j].length(); k++) { // skip time, only need poi_id
                        idx = idx * 10 + (part[j].charAt(k) - '0');
                    }
                    for (int l = 0; l < choices.size(); l++) {
                        if (choices.get(l).getId().equals(idx)) {
                            ans.get(i).add(choices.get(l));
                            break;
                        }
                    }
                    numOfPois++;
                }
            }
            if (numOfPois==choices.size()) {break;}
            createPlan(trip,choices);
        }
        return ans;
    }

    public Optional<Trip> getTripById(Long id){
        return tripRepository.findById(id);
    }

    public void deleteTrip(Long tripId, String username) throws TripNotExistException {
        Trip trip = tripRepository.findByTripIdAndUser(tripId, new User.Builder().setUsername(username).build());
        if (trip == null) {
            throw new TripNotExistException("Trip doesn't exist");
        }
        for(Poi poi: trip.getPoiSet()){
            poi.getTrips().remove(trip);
        }
        trip.getPoiSet().clear();
        tripRepository.deleteById(tripId);
    }

    public void addPoiToTrip(Long poiId, Long tripId, String username) throws TripNotExistException, PoiNotExistException, PoiAlreadyExistInTripException {
        Trip trip = tripRepository.findByTripIdAndUser(tripId, new User.Builder().setUsername(username).build());
        if (trip == null) {
            throw new TripNotExistException("Trip doesn't exist");
        }
        Optional<Poi> poi = poiRepository.findById(poiId);
        if (!poi.isPresent()) {
            throw new PoiNotExistException("Point of interest doesn't exist");
        }
//        boolean test = trip.addPoiToTrip(poi.get());
        Boolean test = trip.getPoiSet().add(poi.get());
        if (test == false) {
            throw new PoiAlreadyExistInTripException("The selected point of interest is already in current trip.");
        }
        tripRepository.save(trip);
    }

    public List<Poi> getAllPoiFromTrip(Long tripId, String username) throws TripNotExistException{
        Trip trip = tripRepository.findByTripIdAndUser(tripId, new User.Builder().setUsername(username).build());
        if(trip == null){
            throw new TripNotExistException("Trip doesn't exist");
        }
        List<Poi> l = new ArrayList<>(trip.getPoiSet());
        return l;
    }

    public void deletePoiFromTrip(Long poiId, Long tripId, String username) throws TripNotExistException, PoiNotExistException{
        Trip trip = tripRepository.findByTripIdAndUser(tripId, new User.Builder().setUsername(username).build());
        if(trip == null){
            throw new TripNotExistException("Trip doesn't exist");
        }
        Optional<Poi> poi = poiRepository.findById(poiId);
        if(!poi.isPresent()){
            throw new PoiNotExistException("Point of interest doesn't exist in our system");
        }
        Set<Poi> set = trip.getPoiSet();
        if(!set.contains(poi.get())){
            throw new PoiNotExistException("Current trip does not contain this Point of Interest");
        }
        set.remove(poi.get());
        tripRepository.save(trip);
    }
}