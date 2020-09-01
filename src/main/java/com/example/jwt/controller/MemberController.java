package com.example.jwt.controller;

import com.example.jwt.config.JwtTokenProvider;
import com.example.jwt.entity.Member;
import com.example.jwt.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    // 회원가입
    @PostMapping("/join")
    public Member join(@RequestBody Map<String, String> member) {
        return memberRepository.save(Member.builder()
                .email(member.get("email"))
                .password(passwordEncoder.encode(member.get("password")))
                .roles(Collections.singletonList("ROLE_USER")) // 최초 가입시 USER 로 설정
                .build());
    }

    // 로그인
    @PostMapping("/login")
    public String login(@RequestBody Map<String, String> user) {
        Member member = memberRepository.findByEmail(user.get("email")).orElseThrow(() -> new UsernameNotFoundException("가입되지 않은 E-MAIL 입니다."));
        if (!passwordEncoder.matches(user.get("password"), member.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }
        return jwtTokenProvider.createToken(member.getUsername(), member.getRoles());

    }

    @GetMapping("/checkJWT")
    public String list(){
        //권한체크
        Authentication user = SecurityContextHolder.getContext().getAuthentication();

        Member member = (Member) user.getPrincipal();
        return user.getAuthorities().toString()+" / "+member.getEmail()+" / "+member.getPassword();
    }

    @GetMapping("/members")
    public List<Member> listAllMember(){
        return memberRepository.findAll();
    }

    @GetMapping("/member/{id}")
    public Member listOneMember(@PathVariable Long id){
        return memberRepository.findById(id).orElseThrow(()->new UsernameNotFoundException("id를 찾지 못했다."));
    }

    @GetMapping("/admin")
    public List<Member> adminCheck(){
        return memberRepository.findAll();
    }
}
