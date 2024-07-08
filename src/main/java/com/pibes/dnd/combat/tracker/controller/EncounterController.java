package com.pibes.dnd.combat.tracker.controller;

import com.pibes.dnd.combat.tracker.Character;
import com.pibes.dnd.combat.tracker.CharacterProperties;
import com.pibes.dnd.combat.tracker.Combatant;
import com.pibes.dnd.combat.tracker.Monster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class EncounterController {

    @Autowired
    private CharacterProperties characterProperties;

    private List<Character> characters = new ArrayList<>();
    private List<Monster> monsters = new ArrayList<>();
    private List<Combatant> combatants = new ArrayList<>();

    @GetMapping("/dnd")
    public String index(Model model) {
        if (characters.isEmpty()) {
            characters = loadCharacters();
            combatants.addAll(characters);
        }

        model.addAttribute("characters", characters);
        model.addAttribute("monsters", monsters);

        combatants.sort((a, b) -> Integer.compare(b.getInitiative(), a.getInitiative()));
        model.addAttribute("combatants", combatants);

        return "index";
    }

    private List<Character> loadCharacters() {
        List<Character> loadedChars = new ArrayList<>();
        characterProperties.getCharacters().forEach((key, charConfig) -> {
            Character character = new Character(charConfig.getName(), charConfig.getAc(), charConfig.getInitiative(), charConfig.getHealth());
            loadedChars.add(character);
        });
        return loadedChars;
    }

    @PostMapping("/addCharacter")
    public String addCharacter(@RequestParam String name, @RequestParam int ac, @RequestParam int initiative, @RequestParam int health) {
        Character character = new Character(name, ac, initiative, health);
        characters.add(character);
        combatants.add(character);
        return "redirect:/dnd";
    }

    @PostMapping("/addMonster")
    public String addMonster(@RequestParam String name, @RequestParam int ac, @RequestParam int initiative, @RequestParam int health) {
        Monster monster = new Monster(name, ac, initiative, health);
        monsters.add(monster);
        combatants.add(monster);
        return "redirect:/dnd";
    }

    @PostMapping("/addToCombat")
    @ResponseBody
    public ResponseEntity<?> addToCombat(@RequestParam String id, @RequestParam String type) {
        try {
            int combatantId = Integer.parseInt(id);
            Combatant combatant = null;

            if ("character".equals(type)) {
                combatant = characters.stream()
                        .filter(c -> c.getId() == combatantId)
                        .findFirst()
                        .orElse(null);
            } else if ("monster".equals(type)) {
                combatant = monsters.stream()
                        .filter(m -> m.getId() == combatantId)
                        .findFirst()
                        .orElse(null);
            }

            if (combatant != null && !combatants.contains(combatant)) {
                combatants.add(combatant);
                combatants.sort((a, b) -> Integer.compare(b.getInitiative(), a.getInitiative()));

                Map<String, Object> response = new HashMap<>();
                response.put("message", "Combatant added to combat successfully.");
                response.put("combatants", combatants);
                return ResponseEntity.ok(response);
            } else if (combatant != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Combatant already in combat.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Combatant not found.");
            }
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid parameters.");
        }
    }

    @PostMapping("/changeInitiative")
    @ResponseBody
    public ResponseEntity<?> changeInitiative(@RequestParam String combatantId, @RequestParam String amount) {
        try {
            int amt = Integer.parseInt(amount);
            int combatantID = Integer.parseInt(combatantId);
            Combatant combatant = findCombatantById(combatantID);
            if (combatant != null) {
                combatant.setInitiative(amt);
                combatants.replaceAll(actualCombatant -> StringUtils.equals(actualCombatant.getName(), (combatant.getName())) ? combatant : actualCombatant);
                combatants.sort((a, b) -> Integer.compare(b.getInitiative(), a.getInitiative()));
                Map<String, Object> response = new HashMap<>();
                response.put("newInitiative", combatant.getInitiative());
                response.put("message", "Combatant initiative changed successfully.");
                response.put("combatants", combatants); // Add this line to include updated combatants
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Combatant not found.");
            }
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid parameters.");
        }
    }


    @PostMapping("/healCombatant")
    @ResponseBody
    public ResponseEntity<?> healCombatant(@RequestParam String combatantId, @RequestParam String amount) {
        try {
            int amt = Integer.parseInt(amount);
            int combatantID = Integer.parseInt(combatantId);
            Combatant combatant = findCombatantById(combatantID);
            if (combatant != null) {
                combatant.setTemporalHealth(combatant.getTemporalHealth() + amt);
                Map<String, Object> response = new HashMap<>();
                response.put("newTemporalHealth", combatant.getTemporalHealth());
                response.put("message", "Combatant healed successfully.");
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Combatant not found.");
            }
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid parameters.");
        }
    }

    @PostMapping("/dmgCombatant")
    @ResponseBody
    public ResponseEntity<?> dmgCombatant(@RequestParam String combatantId, @RequestParam String amount) {
        try {
            int amt = Integer.parseInt(amount);
            int combatantID = Integer.parseInt(combatantId);
            Combatant combatant = findCombatantById(combatantID);
            if (combatant != null) {
                combatant.setTemporalHealth(combatant.getTemporalHealth() - amt);
                Map<String, Object> response = new HashMap<>();
                response.put("newTemporalHealth", combatant.getTemporalHealth());
                response.put("message", "Combatant damaged successfully.");
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Combatant not found.");
            }
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid parameters.");
        }
    }

    @PostMapping("/deadMan")
    @ResponseBody
    public ResponseEntity<?> deadMan(@RequestParam String combatantId) {
        try {
            int combatantID = Integer.parseInt(combatantId);
            Combatant combatant = findCombatantById(combatantID);
            if (combatant != null) {
                combatants.remove(combatant);
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Combatant initiative changed successfully.");
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Combatant not found.");
            }
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid parameters.");
        }
    }

    private Combatant findCombatantById(int combatantId) {
        return combatants.stream()
                .filter(combatant -> combatant.getId() == combatantId)
                .findFirst()
                .orElse(null);
    }
}
