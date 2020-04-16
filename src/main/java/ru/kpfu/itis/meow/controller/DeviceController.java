package ru.kpfu.itis.meow.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.kpfu.itis.meow.model.entity.DeviceSound;
import ru.kpfu.itis.meow.model.params.Token;
import ru.kpfu.itis.meow.service.DeviceService;

import javax.validation.Valid;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/device")
public class DeviceController {

    private DeviceService deviceService;

    @Autowired
    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @PostMapping("/sound/change")
    public DeviceSound changeDeviceSoundStatus(@Valid @RequestBody Token token) {
        return deviceService.changeSoundStatus(token);
    }

    @PostMapping("/sound/status")
    public DeviceSound getDeviceSoundStatus(@Valid @RequestBody Token token) {
        return deviceService.getSoundStatus(token);
    }
}
