package ru.kpfu.itis.meow.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kpfu.itis.meow.model.params.CatDetectionParams;
import ru.kpfu.itis.meow.service.CatService;
import ru.kpfu.itis.meow.service.DeviceService;

import javax.validation.Valid;

@RestController
@RequestMapping("/meow")
public class MeowController {

    private final DeviceService deviceService;
    private final CatService catService;

    @Autowired
    public MeowController(DeviceService deviceService, CatService catService) {
        this.deviceService = deviceService;
        this.catService = catService;
    }

    @PostMapping("/detect")
    public ResponseEntity detectCatInBox(@RequestBody @Valid final CatDetectionParams params) {
        if (!deviceService.isDeviceNameValid(params.getDeviceName())) {
            return ResponseEntity.badRequest().body("Validation failed");
        }
        catService.detect(params);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public String getHelloMeow() {
        return "Meow, fren!";
    }
}
