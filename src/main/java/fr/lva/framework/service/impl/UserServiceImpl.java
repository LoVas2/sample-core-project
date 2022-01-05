package fr.lva.framework.service.impl;

import fr.lva.framework.annotation.ClearCache;
import fr.lva.framework.pojo.User;
import fr.lva.framework.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Override
    @ClearCache("#{args[0].id}")
    public User sampleMethod(User user) {
        return null;
    }

}
