package ch.heigvd.digiback.ui.fragment.back_state;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProviders;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.MarkerType;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;
import com.jjoe64.graphview.GraphView;

import java.util.ArrayList;
import java.util.List;

import ch.heigvd.digiback.R;
import ch.heigvd.digiback.business.api.TaskRunner;
import ch.heigvd.digiback.business.api.stat.GetStat;
import ch.heigvd.digiback.business.api.stat.iOnStatFetched;
import ch.heigvd.digiback.business.model.Stat;
import ch.heigvd.digiback.ui.activity.mobility.MobilityActivity;

/**
 * Source pour le graph : https://github.com/jjoe64/GraphView/wiki/Bar-Graph
 */

public class BackStateFragment extends Fragment {

    private static final String TAG = "BackStateFragment";
    private GraphView graph;

    private AnyChartView anyChartView;
    private MutableLiveData<Stat> stats = new MutableLiveData<>();
    private Button evaluateMobility;

    private BackStateViewModel backStateViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        backStateViewModel =
                ViewModelProviders.of(this).get(BackStateViewModel.class);
        View root = inflater.inflate(R.layout.fragment_back_state, container, false);

        anyChartView = root.findViewById(R.id.any_chart_view);
        anyChartView.setProgressBar(root.findViewById(R.id.progress_bar));

        TaskRunner taskRunner = new TaskRunner();
        taskRunner.executeAsync(new GetStat(new iOnStatFetched() {
            @Override
            public void showProgressBar() {

            }

            @Override
            public void hideProgressBar() {

            }

            @Override
            public void setDataInPageWithResult(Stat stat) {
                stats.postValue(stat);
            }
        }));

        stats.observe(getViewLifecycleOwner(), stat -> setChart());
        //graph = root.findViewById(R.id.graph);
        //setGraph(stats.getValue());
        //stats.observe(getViewLifecycleOwner(), this::setGraph);

        evaluateMobility = root.findViewById(R.id.evaluate_mobility);
        evaluateMobility.setOnClickListener(view -> {
            Intent i = new Intent(getContext(), MobilityActivity.class);
            this.getActivity().startActivity(i);
        });

        return root;
    }

    private void setChart() {
        Cartesian cartesian = AnyChart.line();

        cartesian.animation(true);

        cartesian.padding(10d, 20d, 5d, 20d);

        cartesian.crosshair().enabled(true);
        cartesian.crosshair()
                .yLabel(true)
                // TODO ystroke
                .yStroke((Stroke) null, null, null, (String) null, (String) null);

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);

        cartesian.title("Angle evolutions");

        cartesian.yAxis(0).title("Angles");
        cartesian.xAxis(0).labels().padding(5d, 5d, 5d, 5d);

        List<DataEntry> seriesData = new ArrayList<>();
        Log.d(TAG, "Observer is called");
        Log.d(TAG, "" + stats.getValue().getHighestAngle());
        for (int i = 0; i < Math.max(stats.getValue().getAngleEvolution().size(), stats.getValue().getPainEvolution().size()); ++i) {
            float angle = 0;
            int pain = 0;
            try {
                angle = stats.getValue().getAngleEvolution().get(i);
            } catch (Exception e) {
                Log.d(TAG, "No more angles");
            }
            try {
                pain = stats.getValue().getPainEvolution().get(i);
            } catch (Exception e) {
                Log.d(TAG, "No more pain");
            }
            seriesData.add(new CustomDataEntry("" + (i + 1), i, angle, pain));
            Log.d(TAG, "" + (i + 1) + ", " + i + ", " + angle + ", " + pain);
        }

        Set set = Set.instantiate();
        set.data(seriesData);
        Mapping series1Mapping = set.mapAs("{ x: 'x', value: 'value' }");
        Mapping series2Mapping = set.mapAs("{ x: 'x', value: 'angleEvolution' }");
        Mapping series3Mapping = set.mapAs("{ x: 'x', value: 'painEvolution' }");

        Line series1 = cartesian.line(series1Mapping);
        series1.name("Numbers");
        series1.hovered().markers().enabled(true);
        series1.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series1.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        Line series2 = cartesian.line(series2Mapping);
        series2.name("Angle Evolution");
        series2.hovered().markers().enabled(true);
        series2.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series2.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        Line series3 = cartesian.line(series3Mapping);
        series3.name("Pain evolution");
        series3.hovered().markers().enabled(true);
        series3.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series3.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        cartesian.legend().enabled(true);
        cartesian.legend().fontSize(13d);
        cartesian.legend().padding(0d, 0d, 10d, 0d);

        anyChartView.setChart(cartesian);
/*
        seriesData.add(new CustomDataEntry("1986", 3.6, 2.3, 2.8));
        seriesData.add(new CustomDataEntry("1987", 7.1, 4.0, 4.1));
        seriesData.add(new CustomDataEntry("1988", 8.5, 6.2, 5.1));
        seriesData.add(new CustomDataEntry("1989", 9.2, 11.8, 6.5));
        seriesData.add(new CustomDataEntry("1990", 10.1, 13.0, 12.5));
        seriesData.add(new CustomDataEntry("1991", 11.6, 13.9, 18.0));
        seriesData.add(new CustomDataEntry("1992", 16.4, 18.0, 21.0));
        seriesData.add(new CustomDataEntry("1993", 18.0, 23.3, 20.3));
        seriesData.add(new CustomDataEntry("1994", 13.2, 24.7, 19.2));
        seriesData.add(new CustomDataEntry("1995", 12.0, 18.0, 14.4));
        seriesData.add(new CustomDataEntry("1996", 3.2, 15.1, 9.2));
        seriesData.add(new CustomDataEntry("1997", 4.1, 11.3, 5.9));
        seriesData.add(new CustomDataEntry("1998", 6.3, 14.2, 5.2));
        seriesData.add(new CustomDataEntry("1999", 9.4, 13.7, 4.7));
        seriesData.add(new CustomDataEntry("2000", 11.5, 9.9, 4.2));
        seriesData.add(new CustomDataEntry("2001", 13.5, 12.1, 1.2));
        seriesData.add(new CustomDataEntry("2002", 14.8, 13.5, 5.4));
        seriesData.add(new CustomDataEntry("2003", 16.6, 15.1, 6.3));
        seriesData.add(new CustomDataEntry("2004", 18.1, 17.9, 8.9));
        seriesData.add(new CustomDataEntry("2005", 17.0, 18.9, 10.1));
        seriesData.add(new CustomDataEntry("2006", 16.6, 20.3, 11.5));
        seriesData.add(new CustomDataEntry("2007", 14.1, 20.7, 12.2));
        seriesData.add(new CustomDataEntry("2008", 15.7, 21.6, 10));
        seriesData.add(new CustomDataEntry("2009", 12.0, 22.5, 8.9));
*/

    }

    private class CustomDataEntry extends ValueDataEntry {
        CustomDataEntry(String x, Number value, Number angleEvolution, Number painEvolution) {
            super(x, value);
            setValue("angleEvolution", angleEvolution);
            setValue("painEvolution", painEvolution);
        }
    }
}
