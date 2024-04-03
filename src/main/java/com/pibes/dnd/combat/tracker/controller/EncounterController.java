package com.pibes.dnd.combat.tracker.controller;

import com.pibes.dnd.combat.tracker.Character;
import com.pibes.dnd.combat.tracker.Combatant;
import com.pibes.dnd.combat.tracker.Monster;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.parser.Entity;
import java.util.ArrayList;
import java.util.List;

@Controller
public class EncounterController {

    private List<Character> characters = new ArrayList<>();
    private List<Monster> monsters = new ArrayList<>();

    @GetMapping("/dnd")
    public String index(Model model) {
        // Add sample characters and monsters to the model
        model.addAttribute("characters", characters);
        model.addAttribute("monsters", monsters);
        List<Combatant> combatants = new ArrayList<>();
        combatants.addAll(characters);
        combatants.addAll(monsters);

        // Sort entities by initiative in descending order
        combatants.sort((a, b) -> {
            int initiativeA = a.getInitiative();
            int initiativeB = b.getInitiative();
            return initiativeB - initiativeA;
        });

        // Add sorted entities to the model
        model.addAttribute("combatants", combatants);

        return "index";
    }

    @PostMapping("/addCharacter")
    public String addCharacter(@RequestParam String name, @RequestParam int ac, @RequestParam int initiative, @RequestParam int health) {
        Character character = new Character(name, ac, initiative, health);
        characters.add(character);
        return "redirect:/dnd";
    }

    @PostMapping("/addMonster")
    public String addMonster(@RequestParam String name, @RequestParam int ac, @RequestParam int initiative, @RequestParam int health) {
        Monster monster = new Monster(name, ac, initiative, health);
        monsters.add(monster);
        // Redirect back to the /dnd page
        return "redirect:/dnd";
    }
}

