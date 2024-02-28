package com.pibes.dnd.combat.tracker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EncounterController {

    @GetMapping("/dnd")
    public String index(Model model) {
        // Add sample characters and monsters to the model
        model.addAttribute("characters", "Character 1, Character 2");
        model.addAttribute("monsters", "Monster 1, Monster 2");
        return "index";
    }
}

