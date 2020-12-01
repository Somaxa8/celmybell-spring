package com.somacode.celmybell.service;

import com.somacode.celmybell.config.exception.NotFoundException;
import com.somacode.celmybell.entity.Config;
import com.somacode.celmybell.repository.ConfigRepository;
import com.somacode.celmybell.service.tool.PatchTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ConfigService {

    @Autowired ConfigRepository configRepository;
    @Autowired PatchTool patchTool;


    public void init() {
        if (configRepository.count() <= 0) {
            Config config = new Config();
            config.setTag("social_media");
            config.setKey("facebook");
            config.setUrl("http://www.facebook.com/celmybell");
            config.setMeta("mdi-facebook");
            create(config);
            config = new Config();
            config.setTag("social_media");
            config.setKey("telegram");
            config.setUrl("https://t.me/Celmy");
            config.setMeta("mdi-telegram");
            create(config);
            config = new Config();
            config.setTag("social_media");
            config.setKey("twitter");
            config.setUrl("http://twitter.com/celmybell");
            config.setMeta("mdi-twitter");
            create(config);
            config = new Config();
            config.setTag("social_media");
            config.setKey("instagram");
            config.setUrl("https://www.instagram.com/celmybell/");
            config.setMeta("mdi-instagram");
            create(config);
            config = new Config();
            config.setTag("strings");
            config.setKey("footer");
            config.setValue("lorem ipsum");
            create(config);
            config = new Config();
            config.setTag("strings");
            config.setKey("about_me");
            config.setValue("lorem ipsum");
            create(config);
        }
    }

    public Config create(Config config) {
        config.setId(null);
        return configRepository.save(config);
    }

    public Config update(Long id, Config request) {
        if (!configRepository.existsById(id)) {
            throw new NotFoundException();
        }
        Config config = configRepository.getOne(id);

        request.setId(null);
        patchTool.patch(request, config);

        return configRepository.save(config);
    }

    public List<Config> findConfigByTag(String tag) {
        return configRepository.findByTag(tag);
    }

    public Config findConfigByKey(String key) {
        return configRepository.findByKey(key);
    }

    public List<Config> findAll() {
        return configRepository.findAll();
    }

    public void delete(Long id) {
        if (!configRepository.existsById(id)) {
            throw new NotFoundException();
        }
        configRepository.deleteById(id);
    }
}
