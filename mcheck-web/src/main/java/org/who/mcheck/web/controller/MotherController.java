package org.who.mcheck.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.who.mcheck.core.domain.Mother;
import org.who.mcheck.core.service.MotherService;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class MotherController {
    private final MotherService motherService;

    @Autowired
    public MotherController(MotherService motherService) {
        this.motherService = motherService;
    }

    @RequestMapping(method = GET)
    public ModelAndView fetchAll() {
        List<Mother> mothers = motherService.fetchAll();
        return new ModelAndView("mother-list", "mothers", mothers);
    }

}
