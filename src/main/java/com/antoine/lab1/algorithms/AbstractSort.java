package com.antoine.lab1.algorithms;

import com.antoine.lab1.interfaces.Sorts;
import javafx.application.Platform;

public abstract class AbstractSort implements Sorts {
    private long comparisons = 0;
    private long interchanges = 0;
    private Runnable uiUpdater = null;
    private long delayMs = 0;

    public void setupVisualization(long delayMs, Runnable uiUpdater) {
        this.delayMs = delayMs;
        this.uiUpdater = uiUpdater;
    }

    public void incrementComparisons() {
        comparisons++;
    }

    public void incrementInterchanges() {
        interchanges++;

        if (uiUpdater != null) {
            Platform.runLater(uiUpdater);
            try {
                Thread.sleep(delayMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public long getComparisons(){
        return comparisons;
    }
    public long getInterchanges(){
        return interchanges;
    }
    public void resetMetrics() {
        comparisons = 0;
        interchanges = 0;
    }
}