package ch.heigvd.digiback.ui.fragment.etat;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.util.Calendar;
import java.util.Date;

import ch.heigvd.digiback.R;

/**
 * Source pour le graph : https://github.com/jjoe64/GraphView/wiki/Bar-Graph
 */

public class EtatFragment extends Fragment {

    private static final String TAG = "EtatFragment";
    private GraphView graph;
    private Button changeDate;
    private Button addPain;

    private EtatViewModel etatViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        etatViewModel =
                ViewModelProviders.of(this).get(EtatViewModel.class);
        View root = inflater.inflate(R.layout.fragment_etat, container, false);

        graph = root.findViewById(R.id.graph);
        setGraph();

        changeDate = root.findViewById(R.id.change_date);
        changeDate.setOnClickListener(view -> {
            // TODO
        });

        addPain = root.findViewById(R.id.add_pain);
        addPain.setOnClickListener(view -> {
            PopupMenu popup = new PopupMenu(getContext(), view);
            popup.setOnMenuItemClickListener(item -> {
                Log.d(TAG, "Selected pain : " + item.getTitle());
                switch (item.getItemId()) {
                    case R.id.pain_0:
                        // TODO
                        return true;
                    case R.id.pain_1:
                        // TODO
                        return true;
                    case R.id.pain_2:
                        // TODO
                        return true;
                    case R.id.pain_3:
                        // TODO
                        return true;
                    case R.id.pain_4:
                        // TODO
                        return true;
                    case R.id.pain_5:
                        // TODO
                        return true;
                    case R.id.pain_6:
                        // TODO
                        return true;
                    case R.id.pain_7:
                        // TODO
                        return true;
                    case R.id.pain_8:
                        // TODO
                        return true;
                    case R.id.pain_9:
                        // TODO
                        return true;
                    case R.id.pain_10:
                        // TODO
                        return true;
                    default:
                        return false;
                }
            });
            MenuInflater inflater1 = popup.getMenuInflater();
            inflater1.inflate(R.menu.add_pain, popup.getMenu());
            popup.show();
        });

        return root;
    }

    private void setGraph() {
        // TODO make it dynamic

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
