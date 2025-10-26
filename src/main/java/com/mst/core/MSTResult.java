package com.mst.core;

import java.util.List;

public class MSTResult {
    private List<Edge> mstEdges;
    private int totalCost;
    private int operationsCount;
    private long executionTime;

    public MSTResult(List<Edge> mstEdges, int totalCost, int operationsCount, long executionTime) {
        this.mstEdges = mstEdges;
        this.totalCost = totalCost;
        this.operationsCount = operationsCount;
        this.executionTime = executionTime;
    }

    public List<Edge> getMstEdges() { return mstEdges; }
    public int getTotalCost() { return totalCost; }
    public int getOperationsCount() { return operationsCount; }
    public long getExecutionTime() { return executionTime; }

    // Добавим полезные методы
    public double getExecutionTimeMs() {
        return executionTime / 1_000_000.0;
    }

    public boolean isValidMST(int vertexCount) {
        return mstEdges.size() == vertexCount - 1 && totalCost >= 0;
    }
}