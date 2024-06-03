package com.example.project.service;

import com.example.project.domain.Member;
import com.example.project.domain.User;
import com.example.project.repository.MemberRepository;
import com.example.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WithdrawService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Transactional
    public boolean deleteUser(String userID) {
        User user = userRepository.findByUserID(userID);
        if (user != null) {
            // 사용자와 관련된 Member 엔티티를 찾아서 삭제합니다.
            List<Member> members = memberRepository.findByUser(user);
            memberRepository.deleteAll(members);

            // 사용자를 삭제합니다.
            userRepository.delete(user);
            return true;
        }
        return false;
    }
}
