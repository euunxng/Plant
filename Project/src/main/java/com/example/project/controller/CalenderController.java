package com.example.project.controller;

import com.example.project.domain.Calender;
import com.example.project.dto.*;
import com.example.project.service.CalenderService;
import com.example.project.service.MemberService;
import lombok.RequiredArgsConstructor;
import java.time.format.DateTimeFormatter;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CalenderController {

    private final CalenderService calenderService;
    private final MemberService memberService;

    @PostMapping("/addSchedule")
    public Calender addCalenderEvent(@RequestParam("groupId") Long groupId,
                                     @RequestParam("cName") String cName,
                                     @RequestParam("cDate") LocalDate cDate,
                                     @RequestParam("time") String time,
                                     @RequestParam("place") String place) {
        return calenderService.addCalenderEvent(groupId, cName, cDate, time, place);
    }


    @GetMapping("/getSchedule/{groupId}")
    public List<CalenderDto> getCalendersByGroupId(@PathVariable("groupId") Long groupId) {
        return calenderService.getCalendersByGroupId(groupId);
    }

    @GetMapping("/getSchedule/{groupId}/{cDate}")
    public List<cViewDto> getCalendersByGroupIdAndCDate(@PathVariable("groupId") Long groupId,
                                                        @PathVariable("cDate") LocalDate cDate) {
        return calenderService.getCalendersByGroupIdAndCDate(groupId, cDate);
    }

    @DeleteMapping("/delete")
    public void deleteCalenderEvent(@RequestParam("groupId") Long groupId,
                                    @RequestParam("cDate") LocalDate cDate) {
        calenderService.deleteCalenderEvent(groupId, cDate);
    }

    @PutMapping("/updateSchedule")
    public cUpdateDto updateCalenderEvent(@RequestParam("groupId") Long groupId,
                                          @RequestParam("cDate") LocalDate cDate,
                                          @RequestBody cUpdateDto updatedCalender) {
        return calenderService.updateCalenderEvent(groupId, cDate, updatedCalender);
    }

    @GetMapping("/getCompleteByGroupIdAndDate")
    public cCompleteDto getCompleteByGroupIdAndDate(@RequestParam("groupId") Long groupId,
                                                    @RequestParam("cDate") LocalDate cDate) {
        return calenderService.getCompleteByGroupIdAndDate(groupId, cDate);
    }

    @PutMapping("/updateCompleteByGroupIdAndDate")
    public cCompleteDto updateCompleteByGroupIdAndDate(@RequestParam("groupId") Long groupId,
                                                       @RequestParam("cDate") LocalDate cDate) {
        return calenderService.updateCompleteByGroupIdAndDate(groupId, cDate);
    }

    @GetMapping("/getUserFacePaths/{groupId}")
    public List<UserDto> getUserFacePathsByGroupId(@PathVariable("groupId") Long groupId) {
        return memberService.getUserFacePathsByGroupId(groupId);
    }
}
