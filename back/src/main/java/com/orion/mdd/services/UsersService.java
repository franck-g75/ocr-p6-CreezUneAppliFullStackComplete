package com.orion.mdd.services;

import org.springframework.stereotype.Service;

import com.orion.mdd.models.Users;
import com.orion.mdd.repository.UsersRepository;

@Service
public class UsersService {

    private final UsersRepository usersRepository;

    public UsersService(UsersRepository usersRepository){
        this.usersRepository = usersRepository;
    }

    public Users findByEmail(String email){
        return usersRepository.findByEmail(email);
    }
    
    public Users findByUsername(String username){
        return usersRepository.findByUsername(username);
    }
}
