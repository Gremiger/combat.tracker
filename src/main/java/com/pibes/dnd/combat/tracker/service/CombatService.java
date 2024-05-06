package com.pibes.dnd.combat.tracker.service;

import org.springframework.stereotype.Service;

@Service
public class CombatService {

    private boolean isCombatStarted = false;
    private int currentTurn = 0;

    public void startCombat() {
        isCombatStarted = true;
    }

    public boolean isCombatStarted() {
        return isCombatStarted;
    }

    public int startTurn() {
        if (!isCombatStarted) {
            throw new IllegalStateException("Combat has not started yet.");
        }
        // Lógica para iniciar el turno (puedes agregar más lógica aquí según sea necesario)
        currentTurn = 0;
        return currentTurn;
    }

    public int getCurrentTurn() {
        return currentTurn;
    }

    public int nextTurn() {
        if (!isCombatStarted) {
            throw new IllegalStateException("Combat has not started yet.");
        }
        // Lógica para avanzar al siguiente turno (puedes agregar más lógica aquí según sea necesario)
        currentTurn++;
        return currentTurn;
    }

    public void stopCombat() {
        isCombatStarted = false;
        currentTurn = 0;
    }
}

