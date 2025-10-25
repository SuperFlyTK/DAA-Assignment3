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

    public void setMstEdges(List<Edge> mstEdges) { this.mstEdges = mstEdges; }
    public void setTotalCost(int totalCost) { this.totalCost = totalCost; }
    public void setOperationsCount(int operationsCount) { this.operationsCount = operationsCount; }
    public void setExecutionTime(long executionTime) { this.executionTime = executionTime; }
}