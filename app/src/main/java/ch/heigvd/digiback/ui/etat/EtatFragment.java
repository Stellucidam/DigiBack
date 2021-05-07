package ch.heigvd.digiback.ui.etat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.view.View.OnTouchListener;
import android.view.MotionEvent;

import ch.heigvd.digiback.MainActivity;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.ValueDependentColor;

import android.graphics.Color;

import java.util.Calendar;
import java.util.Date;

import ch.heigvd.digiback.R;

/**
 * Source pour le graph : https://github.com/jjoe64/GraphView/wiki/Bar-Graph
 */

public class EtatFragment extends Fragment {

    private EtatViewModel etatViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        etatViewModel =
                ViewModelProviders.of(this).get(EtatViewModel.class);
        View root = inflater.inflate(R.layout.fragment_etat, container, false);

        Button changeDate = (Button) root.findViewById(R.id.change_date);
        Button addPain = (Button) root.findViewById(R.id.add_pain);

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
        final GraphView graph = (GraphView) root.findViewById(R.id.graph);
        final DataPoint[] points = new DataPoint[] {
                new DataPoint(d0, 1),
                new DataPoint(d1, 3),
                new DataPoint(d2, 12),
                new DataPoint(d3, 16),
                new DataPoint(d4, 16)
        };

        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(points);

        // styling
        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb(83, 169, 98);
            }
        });
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

        return root;
    }

}
