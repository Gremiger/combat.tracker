package com.pibes.dnd.combat.tracker.controller;

import com.pibes.dnd.combat.tracker.Character;
import com.pibes.dnd.combat.tracker.Combatant;
import com.pibes.dnd.combat.tracker.Monster;
import com.pibes.dnd.combat.tracker.service.CombatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class EncounterController {

    private List<Character> characters = new ArrayList<>();
    private List<Monster> monsters = new ArrayList<>();
    private List<Combatant> combatants = new ArrayList<>();
    private final CombatService combatService;

    public EncounterController(CombatService combatService) {
        this.combatService = combatService;
    }

    @GetMapping("/dnd")
    public String index(Model model) {
        // Add sample characters and monsters to the model
        model.addAttribute("characters", characters);
        model.addAttribute("monsters", monsters);

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
        combatants.add(character);
        return "redirect:/dnd";
    }

    @PostMapping("/addMonster")
    public String addMonster(@RequestParam String name, @RequestParam int ac, @RequestParam int initiative, @RequestParam int health) {
        // TODO Receive the 'current' combat-turn as a request parameter, to return to the same turn after adding a monster
        // What is better, to have the current combat turn, and calculate if it's before or after the monster's turn, or to have the combatant ID and "find" his turn?
        Monster monster = new Monster(name, ac, initiative, health);
        monsters.add(monster);
        combatants.add(monster);
        // If combat is already started, preserve the current turn and redirect back to the /dnd page
        if (combatService.isCombatStarted()) {
            return "redirect:/dnd?currentTurn=" + combatService.getCurrentTurn() + "&combatStarted=true";
        } else {
            // Redirect back to the /dnd page
            return "redirect:/dnd";
        }
    }

    @PostMapping("/healCombatant")
    public ResponseEntity<Map<String, Object>> healCombatant(@RequestParam String combatantId, @RequestParam String amount) {
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
                return errorResponse("Combatant not found.", HttpStatus.NOT_FOUND);
            }
        } catch (NumberFormatException e) {
            return errorResponse("Invalid parameters.", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/dmgCombatant")
    public ResponseEntity<Map<String, Object>> dmgCombatant(@RequestParam String combatantId, @RequestParam String amount) {
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
                return errorResponse("Combatant not found.", HttpStatus.NOT_FOUND);
            }
        } catch (NumberFormatException e) {
            return errorResponse("Invalid parameters.", HttpStatus.BAD_REQUEST);
        }
    }

    private static ResponseEntity<Map<String, Object>> errorResponse(String value, HttpStatus notFound) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", value);
        return ResponseEntity.status(notFound).body(errorResponse);
    }

    // Helper method to find a combatant by ID
    private Combatant findCombatantById(int combatantId) {
        return combatants.stream()
                .filter(combatant -> combatant.getId() == combatantId)
                .findFirst()
                .orElse(null);
    }
}

