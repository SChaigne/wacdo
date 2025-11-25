package com.gdu.wacdo.controllers;

import com.gdu.wacdo.models.AppUser;
import com.gdu.wacdo.repositories.AppUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountControllerTest.class)
@AutoConfigureMockMvc(addFilters = false)
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

    @Test
    void testRegisterPostEmailAlreadyUsed() throws Exception {
        AppUser existing = new AppUser();
        existing.setEmail("test@test.com");
        when(userRepo.findByEmail("test@test.com")).thenReturn(existing);

        mockMvc.perform(post("/register")
                        .flashAttr("_csrf", new Object())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("firstName", "John")
                        .param("lastName", "Doe")
                        .param("email", "test@test.com")
                        .param("password", "123456")
                        .param("confirmPassword", "123456")
                )
                .andExpect(status().isOk())
                .andExpect(view().name("account/register"))
                .andExpect(model().attributeDoesNotExist("success"));
    }

    @Test
    void testRegisterPostPasswordMismatch() throws Exception {
        when(userRepo.findByEmail("john@mail.com")).thenReturn(null);

        mockMvc.perform(post("/register")
                        .flashAttr("_csrf", new Object())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("firstName", "John")
                        .param("lastName", "Doe")
                        .param("email", "john@mail.com")
                        .param("password", "123456")
                        .param("confirmPassword", "654321")
                )
                .andExpect(status().isOk())
                .andExpect(view().name("account/register"))
                .andExpect(model().attributeDoesNotExist("success"));
    }
}
