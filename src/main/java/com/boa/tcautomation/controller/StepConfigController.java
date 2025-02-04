package com.boa.tcautomation.controller;

import com.boa.tcautomation.model.StepConfig;
import com.boa.tcautomation.repository.StepConfigRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class StepConfigController {

    @Autowired
    private StepConfigRepository stepConfigRepository;

    private static final Logger logger = LoggerFactory.getLogger(StepConfigController.class);

    @GetMapping("/stepconfig/add")
    public String showAddStepConfigForm(Model model) {
        model.addAttribute("stepConfig", new StepConfig());
        return "stepconfig/add";
    }

    @PostMapping("/stepconfig/save")
    public String saveStepConfig(@ModelAttribute StepConfig stepConfig) {
        stepConfigRepository.save(stepConfig);
        return "redirect:/stepconfig/list";
    }

    @GetMapping("/stepconfig/list")
    public String listStepConfigs(Model model) {
        model.addAttribute("stepConfigs", stepConfigRepository.findAll());
        return "stepconfig/list";
    }

    @GetMapping("/stepconfig/edit/{stepName}")
    public String showEditStepConfigForm(@PathVariable String stepName, Model model) {
        StepConfig stepConfig = stepConfigRepository.findById(stepName).orElseThrow(() -> new IllegalArgumentException("Invalid step name: " + stepName));
        model.addAttribute("stepConfig", stepConfig);
        return "stepconfig/edit";
    }

    @PostMapping("/stepconfig/update")
    public String updateStepConfig(@ModelAttribute StepConfig stepConfig) {
        stepConfigRepository.save(stepConfig);
        return "redirect:/stepconfig/list";
    }

    @GetMapping("/stepconfigs")
    public String listAllStepConfigs(Model model) {
        logger.info("Listing all step configs");
        model.addAttribute("stepConfigs", stepConfigRepository.findAll());
        logger.info("Returning stepconfig/list {}",stepConfigRepository.findAll());
        return "stepconfig/list";
    }
}
