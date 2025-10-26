    package com.mst.io;

    import java.util.List;

    public class OutputData {
        private List<GraphResult> results;

        public OutputData(List<GraphResult> results) {
            this.results = results;
        }

        public List<GraphResult> getResults() { return results; }
        public void setResults(List<GraphResult> results) { this.results = results; }
    }