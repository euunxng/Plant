package com.example.project.service;

import com.example.project.domain.Calender;
import com.example.project.domain.CalenderId;
import com.example.project.dto.cCompleteDto;
import com.example.project.dto.cUpdateDto;
import com.example.project.dto.cViewDto;
import com.example.project.repository.CalenderRepository;
import com.example.project.repository.GroupsRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CalenderService {

    private final CalenderRepository calenderRepository;
    private final GroupsRepository groupsRepository;

    public Calender addCalenderEvent(Long groupId, String cName, LocalDate cDate, String time, String place) {
        CalenderId calenderId = new CalenderId(groupId, cDate);
        if (calenderRepository.existsById(calenderId)) {
            throw new IllegalArgumentException("중복된 groupId와 cDate: " + groupId + ", " + cDate);
        }

        Calender calender = Calender.builder()
                .groupId(groupId)
                .cName(cName)
                .cDate(cDate)
                .time(time)
                .place(place)
                .complete(false)
                .build();
        return calenderRepository.save(calender);
    }
    

    public List<cViewDto> getCalendersByGroupIdAndCDate(Long groupId, LocalDate cDate) {
        if (!groupsRepository.existsById(groupId)) {
            throw new IllegalArgumentException("존재하지 않는 그룹 ID: " + groupId);
        }

        List<Calender> calenders = calenderRepository.findByGroupId(groupId)
                .stream()
                .filter(calender -> calender.getCDate().equals(cDate))
                .toList();

        if (calenders.isEmpty()) {
            throw new IllegalArgumentException("해당 그룹에 일정이 없습니다: " + groupId + " on date " + cDate);
        }

        return calenders.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public void deleteCalenderEvent(Long groupId, LocalDate cDate) {
        CalenderId calenderId = new CalenderId(groupId, cDate);
        if (!calenderRepository.existsById(calenderId)) {
            throw new IllegalArgumentException("해당 그룹 ID와 날짜에 해당하는 일정이 없습니다: " + groupId + ", " + cDate);
        }
        calenderRepository.deleteById(calenderId);
    }

    private cViewDto convertToDto(Calender calender) {
        return cViewDto.builder()
                .cDate(calender.getCDate())
                .cName(calender.getCName())
                .time(calender.getTime())
                .place(calender.getPlace())
                .complete(calender.isComplete())
                .build();
    }

    @Transactional
    public cUpdateDto updateCalenderEvent(Long groupId, LocalDate cDate, cUpdateDto updatedCalender) {
        CalenderId calenderId = new CalenderId(groupId, cDate);
        Optional<Calender> existingCalenderOptional = calenderRepository.findById(calenderId);

        if (existingCalenderOptional.isEmpty()) {
            throw new IllegalArgumentException("해당 그룹 ID와 날짜에 해당하는 일정이 없습니다: " + groupId + ", " + cDate);
        }

        Calender existingCalender = existingCalenderOptional.get();
        existingCalender.setCName(updatedCalender.getCName());
        existingCalender.setTime(updatedCalender.getTime());
        existingCalender.setPlace(updatedCalender.getPlace());

        calenderRepository.save(existingCalender);

        return updatedCalender;
    }

    public cCompleteDto getCompleteByGroupIdAndDate(Long groupId, LocalDate cDate) {
        CalenderId calenderId = new CalenderId(groupId, cDate);
        Optional<Calender> calenderOptional = calenderRepository.findById(calenderId);

        if (calenderOptional.isEmpty()) {
            throw new IllegalArgumentException("해당 그룹 ID와 날짜에 해당하는 일정이 없습니다: " + groupId + ", " + cDate);
        }

        Calender calender = calenderOptional.get();
        return new cCompleteDto(calender.isComplete());
    }

    @Transactional
    public cCompleteDto updateCompleteByGroupIdAndDate(Long groupId, LocalDate cDate) {
        CalenderId calenderId = new CalenderId(groupId, cDate);
        Optional<Calender> calenderOptional = calenderRepository.findById(calenderId);

        if (calenderOptional.isEmpty()) {
            throw new IllegalArgumentException("해당 그룹 ID와 날짜에 해당하는 일정이 없습니다: " + groupId + ", " + cDate);
        }

        Calender calender = calenderOptional.get();
        calender.setComplete(true); // complete 값을 true로 설정

        calenderRepository.save(calender);

        return new cCompleteDto(true); // 업데이트된 complete 값을 반환
    }


}
