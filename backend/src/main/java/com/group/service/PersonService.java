package com.group.service;

import com.group.dto.UserRegistrationDTO;
import com.group.entity.PersonEntity;
import com.group.repository.PersonRepository;
import com.group.security.CustomUserDetails;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PersonService implements UserDetailsService {

    private final PersonRepository personRepository;

    RoleService roleService;


    @Autowired
    public PersonService(PersonRepository personRepository,
                         RoleService roleService
    ) {
        this.personRepository = personRepository;
        this.roleService = roleService;
    }

    @Override
    @Transactional
    public CustomUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        PersonEntity person = personRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(
                String.format("User %s not found!", email)
        ));
        return new CustomUserDetails(
                person.getName(),
                person.getPassword(),
                person.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList()),
                person.getEmail(),
                person.getUserPic())
        ;
    }

    public PersonEntity getAuthPersonEntity() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated()) {
            String email = authentication.getName();
            return personRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(
                    String.format("User %s not found!", email)));
        } else {
            throw new BadCredentialsException("This user is not Authenticated");
        }
    }


    public boolean isNewPerson(String email) {
        return personRepository.findByEmail(email).isPresent();
    }

    public void saveNewPerson(UserRegistrationDTO dto) {
        personRepository.save(new PersonEntity(dto.userName(),
                dto.password(),
                dto.email(),
                Set.of(roleService.getUserRole()), dto.userPic()));
    }
}
