package com.pibes.dnd.combat.tracker.controller;

import com.pibes.dnd.combat.tracker.Character;
import com.pibes.dnd.combat.tracker.Combatant;
import com.pibes.dnd.combat.tracker.Monster;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public class EncounterController {

    @Value("${character.test1}")
    private String test1;

    @Value("${character.test2}")
    private String test2;

    private List<Character> characters = new ArrayList<>();
    private List<Monster> monsters = new ArrayList<>();

    private List<Combatant> combatants = new ArrayList<>();

    @GetMapping("/dnd")
    public String index(Model model) {
        // Add sample characters and monsters to the model
        characters = loadCharacteres();
        model.addAttribute("characters", characters);
        model.addAttribute("monsters", monsters);
        combatants = new ArrayList<>(characters);
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

    private List<Character> loadCharacteres() {
        JSONObject char1 = new JSONObject(test1);
        Character charTest1 = new Character(char1.getString("name"), char1.getInt("ac"),char1.getInt("initiative"), char1.getInt("health"));
        JSONObject char2 = new JSONObject(test2);
        Character charTest2 = new Character(char2.getString("name"), char2.getInt("ac"),char2.getInt("initiative"), char2.getInt("health"));
        ArrayList<Character> loadedChars = new ArrayList<>();
        loadedChars.add(charTest1);
        loadedChars.add(charTest2);
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
        // Redirect back to the /dnd page
        return "redirect:/dnd";
    }

    /**
     * Handles the POST request to change the initiative of a combatant on the server-side.
     * @param {string} combatantId - The ID of the combatant whose initiative to change.
     * @param {string} amount - The amount by which to change the combatant's initiative.
     * @returns {Promise<Response>} A promise containing the server response.
     */
    @PostMapping("/changeInitiative")
    @ResponseBody
    public ResponseEntity<?> changeInitiative(@RequestParam String combatantId, @RequestParam String amount) {
        try {
            // Find the combatant by ID and update its initiative
            int amt = Integer.parseInt(amount);
            int combatantID = Integer.parseInt(combatantId);
            Combatant combatant = findCombatantById(combatantID);
            if (combatant != null) {
                combatant.setInitiative(amt);
                combatants.replaceAll(actualCombatant -> StringUtils.equals(actualCombatant.getName(), (combatant.getName())) ? combatant : actualCombatant);
                combatants.sort((a, b) -> {
                    int initiativeA = a.getInitiative();
                    int initiativeB = b.getInitiative();
                    return initiativeB - initiativeA;
                });
                // Create a Map to hold both the new temporal health and a success message
                Map<String, Object> response = new HashMap<>();
                response.put("newInitiative", combatant.getInitiative());
                response.put("message", "Combatant initiative changed successfully.");

                // Return the response Map
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
            // Find the combatant by ID and update its temporal health
            int amt = Integer.parseInt(amount);
            int combatantID = Integer.parseInt(combatantId);
            Combatant combatant = findCombatantById(combatantID);
            if (combatant != null) {
                combatant.setTemporalHealth(combatant.getTemporalHealth() + amt);
                // Create a Map to hold both the new temporal health and a success message
                Map<String, Object> response = new HashMap<>();
                response.put("newTemporalHealth", combatant.getTemporalHealth());
                response.put("message", "Combatant healed successfully.");

                // Return the response Map
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
        try{
            // Find the combatant by ID and update its temporal health
            int amt = Integer.parseInt(amount);
            int combatantID = Integer.parseInt(combatantId);
            Combatant combatant = findCombatantById(combatantID);
            if (combatant != null) {
                combatant.setTemporalHealth(combatant.getTemporalHealth() - amt);
                // Create a Map to hold both the new temporal health and a success message
                Map<String, Object> response = new HashMap<>();
                response.put("newTemporalHealth", combatant.getTemporalHealth());
                response.put("message", "Combatant damaged successfully.");

                // Return the response Map
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
            // Find the combatant by ID and update its initiative
            int combatantID = Integer.parseInt(combatantId);
            Combatant combatant = findCombatantById(combatantID);
            if (combatant != null) {
                combatants.remove(combatant);
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Combatant initiative changed successfully.");
                // Return the response Map
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Combatant not found.");
            }
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid parameters.");
        }
    }

    // Helper method to find a combatant by ID
    private Combatant findCombatantById(int combatantId) {
        return combatants.stream()
                .filter(combatant -> combatant.getId() == combatantId)
                .findFirst()
                .orElse(null);
    }
}

