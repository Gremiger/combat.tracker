package com.pibes.dnd.combat.tracker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class EncounterController {

    private List<String> characters = new ArrayList<>();
    private List<String> monsters = new ArrayList<>();

    @GetMapping("/dnd")
    public String index(Model model) {
        // Add sample characters and monsters to the model
        model.addAttribute("characters", characters);
        model.addAttribute("monsters", monsters);
        return "index";
    }

    @PostMapping("/addCharacter")
    public String addCharacter() {
        characters.add("New Character");
        return "redirect:/dnd";
    }

    @PostMapping("/addMonster")
    public String addMonster(@RequestParam("name") String name) {
        monsters.add(name);
        // Redirect back to the /dnd page
        return "redirect:/dnd";
    }
}

