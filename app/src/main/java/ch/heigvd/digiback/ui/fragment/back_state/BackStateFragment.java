package ch.heigvd.digiback.ui.fragment.back_state;

import android.content.Intent;
import android.graphics.Color;
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

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.util.Calendar;
import java.util.Date;

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
    private MutableLiveData<Stat> stats = new MutableLiveData<>();
    private Button evaluateMobility;

    private BackStateViewModel backStateViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        backStateViewModel =
                ViewModelProviders.of(this).get(BackStateViewModel.class);
        View root = inflater.inflate(R.layout.fragment_back_state, container, false);

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

        graph = root.findViewById(R.id.graph);
        setGraph(stats.getValue());
        //stats.observe(getViewLifecycleOwner(), this::setGraph);

        evaluateMobility = root.findViewById(R.id.evaluate_mobility);
        evaluateMobility.setOnClickListener(view -> {
            Intent i = new Intent(getContext(), MobilityActivity.class);
            this.getActivity().startActivity(i);
        });

        return root;
    }

    private void setGraph(Stat stat) {
        // TODO get info from backend

        Log.d(TAG, "Set graph");






        // generate Dates
        final Calendar calendar = Calendar.getInstance();

        final Date d4 = calendar.getTime();
        calendar.add(Calendar.DATE, -1);
        final Date d3 = calendar.getTime();
        calendar.add(Calendar.DATE, -1);
        final Date d2 = calendar.getTime();
        calendar.add(Calendar.DATE, -1);
        final Date d1 = calendar.getTime();
        calendar.add(Calendar.DATE, -1);
        final Date d0 = calendar.getTime();

        // Graph
        final DataPoint[] points = new DataPoint[] {
                new DataPoint(d0, 1),
                new DataPoint(d1, 3),
                new DataPoint(d2, 12),
                new DataPoint(d3, 16),
                new DataPoint(d4, 16)
        };

        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(points);

        // styling
        series.setValueDependentColor(data -> Color.rgb(83, 169, 98));
        series.setSpacing(10);

        graph.addSeries(series);

        // set date label formatter
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));
        graph.getGridLabelRenderer().setNumHorizontalLabels(points.length);

        // set manual x bounds to have nice steps
        graph.getViewport().setMinX(points[0].getX());
        graph.getViewport().setMaxX(points[points.length - 1].getX());
        graph.getViewport().setXAxisBoundsManual(true);

        // set manual y bounds to have nice steps
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(32);
        graph.getViewport().setYAxisBoundsManual(true);

        // as we use dates as labels, the human rounding to nice readable numbers
        // is not necessary
        graph.getGridLabelRenderer().setHumanRounding(false);
    }
}
