package com.example.server.ui.controllers;

import com.example.server.common.Constants;
import com.example.server.domain.usecases.credentials.ActivateAccountUseCase;
import com.example.server.domain.usecases.email.SendActivationEmailUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ActivateController {

    private final ActivateAccountUseCase activateAccount;
    private final SendActivationEmailUseCase sendActivationEmailUseCase;

    @GetMapping(Constants.ACTIVATE_PATH)
    public ResponseEntity<String> activateAccount(@RequestParam String email, @RequestParam String code) {
        if (activateAccount.activateAccount(email, code)) {
            //notifies the user that the account was activated
            return ResponseEntity.ok(Constants.ACTIVATION_WAS_SUCCESSFUL);
        } else {
            String htmlContent = "<html><body><h1>Activation Link Expired</h1>"
                    + "<p>The activation link has expired. Click the button below to resend the activation link.</p>"
                    + "<input id='resendButton' type='button' value='Resend Activation Link' onclick='resendActivationLink(this)'/>"
                    + "<script>"
                    + "function resendActivationLink(button) {"
                    + "    button.disabled = true;"
                    + "    var email = '" + email + "';"
                    + "    var xhr = new XMLHttpRequest();"
                    + "    xhr.open('PUT', '/Bus_driving_server_spring-1.0-SNAPSHOT/api/activate/resend-code?email=' + encodeURIComponent(email));"
                    + "    xhr.onload = function() {"
                    + "        if (xhr.status === 200) {"
                    + "            alert('Activation link resent successfully.');"
                    + "        } else {"
                    + "            alert('Failed to resend activation link. Please try again.');"
                    + "            button.disabled = false;"
                    + "        }"
                    + "    };"
                    + "    xhr.send();"
                    + "}"
                    + "</script></body></html>";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                    .contentType(MediaType.TEXT_HTML)
                    .body(htmlContent);
        }
    }

    @PutMapping(Constants.RESEND_CODE_PATH)
    public ResponseEntity<String> resendActivationCode(@RequestParam String email) {
        sendActivationEmailUseCase.sendEmail(email);
        return ResponseEntity.ok(Constants.REGISTRATION_WAS_SUCCESSFUL);
    }

}
