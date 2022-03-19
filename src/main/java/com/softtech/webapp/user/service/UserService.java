package com.softtech.webapp.user.service;

import com.softtech.webapp.general.enums.ErrorMessage;
import com.softtech.webapp.general.exceptions.BusinessException;
import com.softtech.webapp.general.exceptions.ItemNotFoundException;
import com.softtech.webapp.user.dto.UserDeleteDto;
import com.softtech.webapp.user.dto.UserGetDto;
import com.softtech.webapp.user.dto.UserPatchDto;
import com.softtech.webapp.user.dto.UserPostDto;
import com.softtech.webapp.user.entity.User;
import com.softtech.webapp.user.enums.UserErrorMessage;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserEntityService userEntityService;
    private final ModelMapper mapper;
    private final PasswordEncoder passwordEncoder;

    public List<UserGetDto> findAll() {
        List<User> userList = userEntityService.findAll();
        List<UserGetDto> userGetDtoList = userList.stream().map(user -> mapper.map(user, UserGetDto.class)).collect(Collectors.toList());
        return userGetDtoList;
    }

    public UserGetDto findById(Long id) {
        User user = userEntityService.getByIdWithControl(id);
        return mapper.map(user, UserGetDto.class);
    }

    public UserGetDto findByUsername(String username) {
        User user = userEntityService.findByUsername(username);
        validateUser(user);
        return mapper.map(user, UserGetDto.class);
    }

    public UserGetDto save(UserPostDto userPostDto) {
        isUsernameUnique(userPostDto.getUsername().toUpperCase());

        User user = mapper.map(userPostDto, User.class);
        user.setUsernameUpper(userPostDto.getUsername().toUpperCase());
        String password = passwordEncoder.encode(user.getPassword());
        user.setPassword(password);

        user = userEntityService.save(user, false);

        return mapper.map(user, UserGetDto.class);
    }

    public void delete(UserDeleteDto userDeleteDto, Long id) {
        User user = userEntityService.findByUsername(userDeleteDto.getUsername());
        validateUser(user);
        if (!Objects.equals(user.getId(), id)) {
            throw new BusinessException(UserErrorMessage.ID_USERNAME_NOT_MATCH);
        }

        userEntityService.delete(user);
    }

    public UserGetDto update(UserPatchDto userPatchDto, Long id) {
        User user = userEntityService.getByIdWithControl(id);

        mapper.getConfiguration().setSkipNullEnabled(true);
        mapper.map(userPatchDto, user);

        if(userPatchDto.getUsername() != null) {
            user.setUsernameUpper(user.getUsername().toUpperCase());
            isUsernameUnique(user.getUsernameUpper());
        }

        user = userEntityService.save(user, true);
        return mapper.map(user, UserGetDto.class);
    }

    private void validateUser(User user) {
        if(user == null)
            throw new ItemNotFoundException(ErrorMessage.ITEM_NOT_FOUND);
    }

    private void isUserExist(Long id) {
        boolean isExist = userEntityService.existsById(id);
        if (!isExist){
            throw new ItemNotFoundException(ErrorMessage.ITEM_NOT_FOUND);
        }
    }

    private void isUsernameUnique(String username) {
        User user = userEntityService.findByUsernameUpper(username);
        if(user != null)
            throw new BusinessException(UserErrorMessage.USERNAME_ALREADY_TAKEN);
    }
}
