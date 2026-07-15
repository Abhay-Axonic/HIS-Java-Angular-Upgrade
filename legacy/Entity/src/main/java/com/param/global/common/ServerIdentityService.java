package com.param.global.common;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.UUID;

@Service
public class ServerIdentityService {

    private String serverId;


    @PostConstruct
    public void init() {

        serverId = UUID.randomUUID().toString();

    }

    public String getServerId() {
        return serverId;
    }
}
