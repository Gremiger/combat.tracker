package com.pibes.dnd.combat.tracker.controller;

import com.pibes.dnd.combat.tracker.service.CombatService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/combat")
public class CombatController {

    private final CombatService combatService;

    public CombatController(CombatService combatService) {
        this.combatService = combatService;
    }

    @GetMapping("/started")
    public boolean isCombatStarted() {
        return combatService.isCombatStarted();
    }

    @PostMapping("/start")
    public int startCombat() {
        combatService.startCombat();
        return combatService.startTurn();
    }

    @PostMapping("/stop")
    public int stopCombat() {
        combatService.stopCombat();
        return 0;
    }

    @PostMapping("/next")
    public int nextTurn() {
        return combatService.nextTurn();
    }
}
