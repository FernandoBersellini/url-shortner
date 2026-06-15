package com.senhorcafe.urlshortner.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignInRequest(

   @NotBlank(message = "A valid email is required")
   @Email
   String email,

   @NotBlank(message = "A password must be informed")
   @Size(min = 8)
   String password
) {}
