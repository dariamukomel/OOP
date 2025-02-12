package ru.nsu.lavitskaya.primenums;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;

public class PrimeChart {
    public static void main(String[] args) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(28499, "Execution Time", "Sequential");
        dataset.addValue(20168, "Execution Time", "2 Threads");
        dataset.addValue(15140, "Execution Time", "3 Threads");
        dataset.addValue(12370, "Execution Time", "4 Threads");
        dataset.addValue(10955, "Execution Time", "5 Threads");
        dataset.addValue(9710, "Execution Time", "6 Threads");
        dataset.addValue(9218, "Execution Time", "7 Threads");
        dataset.addValue(8658, "Execution Time", "8 Threads");
        dataset.addValue(8871, "Execution Time", "Parallel Stream");

        JFreeChart chart = ChartFactory.createBarChart(
                "Performance Comparison",
                "Execution Mode",
                "Time (ms)",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false
        );

        JFrame frame = new JFrame("Execution Time Chart");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new ChartPanel(chart));
        frame.pack();
        frame.setVisible(true);
    }
}
