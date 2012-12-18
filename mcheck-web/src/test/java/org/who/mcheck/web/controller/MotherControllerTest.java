package org.who.mcheck.web.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.who.mcheck.core.domain.Mother;
import org.who.mcheck.core.service.MotherService;

import java.util.List;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.server.setup.MockMvcBuilders.standaloneSetup;

public class MotherControllerTest {

    @Mock
    private MotherService motherService;
    private MotherController controller;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        controller = new MotherController(motherService);
    }

    @Test
    public void shouldFetchAllMothersAndReturnCorrespondingModelAndView() throws Exception {
        List<Mother> allMothers = asList(new Mother("id", "Anamika", "1234567890", "no", "2013-01-01"));
        when(motherService.fetchAll()).thenReturn(allMothers);

        standaloneSetup(controller).build().perform(get("/mother/all"))
                .andExpect(model().attribute("mothers", allMothers))
                .andExpect(view().name("mother-list"));
    }
}
