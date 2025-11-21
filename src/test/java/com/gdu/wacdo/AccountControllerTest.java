package com.gdu.wacdo;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.gdu.wacdo.controllers.AccountController;
import com.gdu.wacdo.models.AppUser;
import com.gdu.wacdo.repositories.AppUserRepository;

class AccountControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AppUserRepository userRepo;

    @InjectMocks
    private AccountController accountController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();
    }

    @Test
    void testRegisterGet() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("account/register"))
                .andExpect(model().attributeExists("success"));
    }

    @Test
    void testLoginGet() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("account/login"));
    }
    
    @Test
    void testRegisterPostSuccess() throws Exception {
        when(userRepo.findByEmail("test@test.com")).thenReturn(null);

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("firstName", "John")
                        .param("lastName", "Doe")
                        .param("email", "test@test.com")
                        .param("password", "123456")
                        .param("confirmPassword", "123456")
                )
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("success", true));

        verify(userRepo).save(any(AppUser.class));
    }


}
