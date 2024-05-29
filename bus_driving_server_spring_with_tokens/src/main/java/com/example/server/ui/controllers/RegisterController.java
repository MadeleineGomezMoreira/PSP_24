package com.example.server.ui.controllers;

import com.example.server.common.Constants;
import com.example.server.domain.usecases.credentials.RegisterUseCase;
import com.example.server.ui.model.RegisterDTO;
import com.example.server.ui.model.mappers.DataMappers;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RegisterController {

    private final RegisterUseCase registerUseCase;
    private final DataMappers dataMappers;

    @PostMapping(Constants.REGISTER_PATH)
    public ResponseEntity<String> register(@RequestBody RegisterDTO account) {
        registerUseCase.register(dataMappers.mapRegisterDTOToBusDriverCredentialEntity(account));

        return ResponseEntity.ok(Constants.REGISTRATION_WAS_SUCCESSFUL);
    }

}
