
package com.isima.projet.calendrier.controller;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.isima.projet.calendrier.domain.Event;
import com.isima.projet.calendrier.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.Month;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class MainController {
    @Autowired
   EventRepository er;

    @RequestMapping("/api")
    @ResponseBody
    String home() {
        return "Welcome!";
    }



    @GetMapping("/api/events/{from}/{to}")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    Iterable<Event> events(@PathVariable("from") @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime from, @PathVariable("to") @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime to) {
        return er.findBetween(from, to);
    }
    @PostMapping("/api/events/create")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Transactional
    Event createEvent() {


        Event e = new Event();
        e.setStart(LocalDateTime.of(2022,
                Month.APRIL, 12, 19, 30, 40));
        e.setEnd(LocalDateTime.of(2022,
                Month.APRIL, 12, 20, 30, 40));
        e.setText("test");


        er.save(e);

        return e;
    }

   /* @PostMapping("/api/events/move")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Transactional
    Event moveEvent( EventMoveParams params) {

        Event e = er.findById(params.id).get();


        e.setStart(params.start);
        e.setEnd(params.end);


        er.save(e);

        return e;
    }

    @PostMapping("/api/events/delete")
    @Transactional
    void deleteEvent(@RequestBody EventDeleteParams params) {
        er.deleteById(params.id);
    }
*/
    public static class EventCreateParams {
        public LocalDateTime start=LocalDateTime.of(2022,
                Month.APRIL, 29, 19, 30, 40);
        public LocalDateTime end=LocalDateTime.of(2022,
                Month.APRIL, 29, 20, 30, 40);
        public String text="rappel";

    }

    public static class EventMoveParams {
        public Long id;
        public LocalDateTime start;
        public LocalDateTime end;

    }

    public static class EventDeleteParams {
        public Long id;
    }

}
