// Generated by view binder compiler. Do not edit!
package com.example.newpomodorotimer.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.viewbinding.ViewBinding;
import com.example.newpomodorotimer.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class TimerItemBinding implements ViewBinding {
  @NonNull
  private final CardView rootView;

  @NonNull
  public final ImageView blinkingIndicator;

  @NonNull
  public final ImageButton deleteButton;

  @NonNull
  public final ImageButton startPauseButton;

  @NonNull
  public final TextView stopwatchTimer;

  private TimerItemBinding(@NonNull CardView rootView, @NonNull ImageView blinkingIndicator,
      @NonNull ImageButton deleteButton, @NonNull ImageButton startPauseButton,
      @NonNull TextView stopwatchTimer) {
    this.rootView = rootView;
    this.blinkingIndicator = blinkingIndicator;
    this.deleteButton = deleteButton;
    this.startPauseButton = startPauseButton;
    this.stopwatchTimer = stopwatchTimer;
  }

  @Override
  @NonNull
  public CardView getRoot() {
    return rootView;
  }

  @NonNull
  public static TimerItemBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static TimerItemBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.timer_item, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static TimerItemBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.blinking_indicator;
      ImageView blinkingIndicator = rootView.findViewById(id);
      if (blinkingIndicator == null) {
        break missingId;
      }

      id = R.id.delete_button;
      ImageButton deleteButton = rootView.findViewById(id);
      if (deleteButton == null) {
        break missingId;
      }

      id = R.id.start_pause_button;
      ImageButton startPauseButton = rootView.findViewById(id);
      if (startPauseButton == null) {
        break missingId;
      }

      id = R.id.stopwatch_timer;
      TextView stopwatchTimer = rootView.findViewById(id);
      if (stopwatchTimer == null) {
        break missingId;
      }

      return new TimerItemBinding((CardView) rootView, blinkingIndicator, deleteButton,
          startPauseButton, stopwatchTimer);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}