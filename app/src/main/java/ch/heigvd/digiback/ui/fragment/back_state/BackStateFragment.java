package ch.heigvd.digiback.ui.fragment.back_state;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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

import java.util.ArrayList;
import java.util.List;

import ch.heigvd.digiback.R;
import ch.heigvd.digiback.business.api.TaskRunner;
import ch.heigvd.digiback.business.api.stat.GetStat;
import ch.heigvd.digiback.business.api.stat.iOnStatFetched;
import ch.heigvd.digiback.business.model.Stat;
import ch.heigvd.digiback.ui.activity.mobility.MobilityActivity;


public class BackStateFragment extends Fragment {
    private static final String TAG = "BackStateFragment";

        private AnyChartView angleEvolutionChart;
    private MutableLiveData<Stat> stats = new MutableLiveData<>();
    private Button evaluateMobility;

    private BackStateViewModel backStateViewModel;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        backStateViewModel =
                ViewModelProviders.of(this).get(BackStateViewModel.class);
        View root = inflater.inflate(R.layout.fragment_back_state, container, false);

        angleEvolutionChart = root.findViewById(R.id.angle_evolution);
        angleEvolutionChart.setProgressBar(root.findViewById(R.id.progress_bar));

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

        evaluateMobility = root.findViewById(R.id.evaluate_mobility);
        evaluateMobility.setOnClickListener(view -> {
            Intent i = new Intent(getContext(), MobilityActivity.class);
            this.getActivity().startActivity(i);
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
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
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setChart() {
        Cartesian cartesian = AnyChart.line();

        cartesian.animation(true);
        cartesian.padding(10d, 20d, 5d, 20d);

        cartesian.crosshair().enabled(true);
        cartesian.crosshair()
                .yLabel(true)
                .yStroke((Stroke) null, null, null, (String) null, (String) null);

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);

        cartesian.title(getString(R.string.evolutions));
        cartesian.yAxis(0).title(getString(R.string.values));
        cartesian.xAxis(0).labels().padding(5d, 5d, 5d, 5d);

        List<DataEntry> angleSeries = new ArrayList<>();
        stats.getValue().getAngleEvolution().forEach((date, angle) -> {
            Log.d(TAG, date.toString() + " " + angle);
            angleSeries.add(new CustomDataEntry(
                    date.toString(),
                    angle,
                    stats.getValue().getPainEvolution().getOrDefault(date, 0)));
        });

        Set anglesSet = Set.instantiate();
        anglesSet.data(angleSeries);
        Mapping serie1Mapping = anglesSet.mapAs("{ x: 'x', value: 'value' }");
        Mapping serie2Mapping = anglesSet.mapAs("{ x: 'x', value: 'value2' }");

        Line angleLine = cartesian.line(serie1Mapping);
        angleLine.name(getString(R.string.angles));
        angleLine.hovered().markers().enabled(true);
        angleLine.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        angleLine.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);
        Line painLine = cartesian.line(serie2Mapping);
        painLine.name("Pain Levels");
        painLine.hovered().markers().enabled(true);
        painLine.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        painLine.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        cartesian.legend().enabled(true);
        cartesian.legend().fontFamily("Montserrat");
        cartesian.legend().fontSize(14d);
        cartesian.legend().padding(0d, 0d, 10d, 0d);

        angleEvolutionChart.setChart(cartesian);
    }
    private static class CustomDataEntry extends ValueDataEntry {
        CustomDataEntry(String x, Number value, Number value2) {
            super(x, value);
            setValue("value2", value2);
        }
    }
}
