package com.security.everywhere.service;

import com.security.everywhere.model.SecurityMember;
import com.security.everywhere.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String nickName) throws UsernameNotFoundException {
        return Optional.ofNullable(memberRepository.findByNickName(nickName))
                .filter(m -> m != null)
                .map(m -> new SecurityMember(m)).get();
    }
}
