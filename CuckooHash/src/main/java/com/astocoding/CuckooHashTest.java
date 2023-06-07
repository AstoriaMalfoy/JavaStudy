package com.astocoding;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.checkerframework.checker.units.qual.C;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by IntelliJ LITAO.
 *
 * @author litao
 * @since 2023/3/31 17:11
 */
public class CuckooHashTest {

    private static final Integer TOTAL_COUNT = 100000;
    private static final Integer STEP = 100;


    public static void main(String[] args) {
        List<Context> contextList = new ArrayList<>();

        contextList.add(Context.ofFunction("deepLimit-2-10", CuckooHashTest::testTwoBucket, 10));
        contextList.add(Context.ofFunction("deepLimit-2-20", CuckooHashTest::testTwoBucket, 30));
        contextList.add(Context.ofFunction("deepLimit-2-30", CuckooHashTest::testTwoBucket, 50));
        contextList.add(Context.ofFunction("deepLimit-2-40", CuckooHashTest::testTwoBucket, 60));

        contextList.add(Context.ofFunction("deepLimit-1-10", CuckooHashTest::testOneBucket, 10));
        contextList.add(Context.ofFunction("deepLimit-1-20", CuckooHashTest::testOneBucket, 30));
        contextList.add(Context.ofFunction("deepLimit-1-30", CuckooHashTest::testOneBucket, 50));
        contextList.add(Context.ofFunction("deepLimit-1-40", CuckooHashTest::testOneBucket, 60));


        SwingUtilities.invokeLater(() -> {
            UIDraw ex = new UIDraw(contextList);
            ex.setVisible(true);
        });
    }


    @Data
    public static class Context {
        private String title;
        private Function<Object, List<Point>> dataGetter;
        private Supplier<List<Point>> dataSupplier;
        private Object param;

        public static Context ofFunction(String title, Function<Object, List<Point>> dataGetter, Object param) {
            Context context = new Context();
            context.title = title;
            context.dataGetter = dataGetter;
            context.param = param;
            return context;
        }

        public static Context ofSupplier(String title, Supplier<List<Point>> dataSupplier) {
            Context context = new Context();
            context.title = title;
            context.dataSupplier = dataSupplier;
            return context;
        }
    }


    public static class UIDraw extends JFrame {


        private static final List<Color> colors = Arrays.asList(Color.MAGENTA, Color.CYAN, Color.green, Color.yellow, Color.red, Color.PINK, Color.ORANGE, Color.LIGHT_GRAY, Color.DARK_GRAY, Color.BLUE, Color.BLACK);

        public UIDraw(List<Context> contextList) {
            initUI(contextList);
        }


        private void initUI(List<Context> contextList) {

            XYDataset dataset = createDataset(contextList);
            JFreeChart chart = createChart(dataset);
            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            chartPanel.setBackground(Color.white);
            add(chartPanel);

            pack();
            setTitle("Line chart");
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }

        private JFreeChart createChart(XYDataset dataset) {

            JFreeChart chart = ChartFactory.createXYLineChart(
                    "CuckooHashSpace",
                    "ElemCount",
                    "SpaceCost",
                    dataset,
                    PlotOrientation.VERTICAL,
                    true,
                    true,
                    false
            );

            XYPlot plot = chart.getXYPlot();

            XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();

            int datasetCount = plot.getDatasetCount();
            for (int i = 0; i < datasetCount; i++) {
                renderer.setSeriesPaint(i, colors.get(i));
                renderer.setSeriesStroke(i, new BasicStroke(1.0f));
            }


            plot.setRenderer(renderer);
            plot.setBackgroundPaint(Color.white);

            plot.setRangeGridlinesVisible(true);
            plot.setRangeGridlinePaint(Color.BLACK);

            plot.setDomainGridlinesVisible(true);
            plot.setDomainGridlinePaint(Color.BLACK);

            chart.getLegend().setFrame(BlockBorder.NONE);

            chart.setTitle(new TextTitle("GRAPH",
                            new Font("Serif", java.awt.Font.BOLD, 18)
                    )
            );

            return chart;

        }

        private XYDataset createDataset(List<Context> contextList) {
            XYSeriesCollection dataset = new XYSeriesCollection();
            for (Context context : contextList) {
                XYSeries series = new XYSeries(context.getTitle());
                if (Objects.nonNull(context.getDataGetter())) {
                    context.getDataGetter().apply(context.getParam()).forEach(point -> series.add(point.getX(), point.getY()));
                }
                if (Objects.nonNull(context.dataSupplier)) {
                    context.getDataSupplier().get().forEach(point -> series.add(point.getX(), point.getY()));
                }

                dataset.addSeries(series);
            }
            return dataset;
        }

    }


    public static List<Point> testTwoBucket(Object deepLimit) {
        CuckooHashFactory.CuckooHashTemplate<Point> cuckooHashTemplate = CuckooHashFactory.createCuckooHash(CuckooHashFactory.BUCKET_FORMAT.TWO_BUCKET, 16, (Integer) deepLimit);
        List<Point> result = new ArrayList<>();
        for (int i = 1; i < TOTAL_COUNT + 1; i++) {
            assert cuckooHashTemplate != null;
            cuckooHashTemplate.add(Point.of(i, i));
            if (i % STEP == 0) {
                double totalSpace = cuckooHashTemplate.getStatus().getBucketSize() * 2;
                result.add(Point.of(i, i / totalSpace));
            }
        }
        return result;
    }


    public static List<Point> testOneBucket(Object deepLimit) {
        CuckooHashFactory.CuckooHashTemplate<Point> cuckooHashTemplate = CuckooHashFactory.createCuckooHash(CuckooHashFactory.BUCKET_FORMAT.ONE_BUCKET, 16, (Integer) deepLimit);
        List<Point> result = new ArrayList<>();
        for (int i = 1; i < TOTAL_COUNT + 1; i++) {
            assert cuckooHashTemplate != null;
            cuckooHashTemplate.add(Point.of(i, i));
            if (i % STEP == 0) {
                double totalSpace = cuckooHashTemplate.getStatus().getBucketSize();
                result.add(Point.of(i, i / totalSpace));
            }
        }
        return result;
    }


    @Data
    @AllArgsConstructor
    private static class Point {
        double x;
        double y;

        public static Point of(double x, double y) {
            return new Point(x, y);
        }
    }

}
