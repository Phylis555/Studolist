//package com.example.studolist;
//
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.CalendarView;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.RadioButton;
//import android.widget.RadioGroup;
//
//import androidx.annotation.NonNull;
//import androidx.constraintlayout.widget.Group;
//
//import com.example.studolist.Models.Task;
//import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
//import com.google.android.material.chip.Chip;
//
//import java.util.Calendar;
//
//public class BottomSheetFragment extends BottomSheetDialogFragment {
//
//    private EditText enterTodo;
//    private ImageButton calendarBtn;
//    private ImageButton priorityBtn;
//    private RadioGroup priorityRadioGroup;
//    private RadioButton selectedRadioBtn;
//    private int selectedBtnId;
//    private ImageButton saveBtn;
//    private CalendarView calendarView;
//    private Group calendarGroup;
//
//
//
//
//
//
//    @Override
//    public View onCreateView(
//            LayoutInflater inflater, ViewGroup container,
//            Bundle savedInstanceState
//    ) {
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.bottom_sheet, container, false);
//        calendarGroup = view.findViewById(R.id.calendar_group);
//        calendarView = view.findViewById(R.id.calendar_view);
//        calendarBtn = view.findViewById(R.id.today_calendar_button);
//        enterTodo = view.findViewById(R.id.enter_todo_et);
//        saveBtn = view.findViewById(R.id.save_todo_button);
//        priorityBtn = view.findViewById(R.id.priority_todo_button);
//        priorityRadioGroup = view.findViewById(R.id.radioGroup_priority);
//
//
//        Chip todayChip = view.findViewById(R.id.today_chip);
//       // todayChip.setOnClickListener(this);
//        Chip tomorrowChip = view.findViewById(R.id.tomorrow_chip);
//        //tomorrowChip.setOnClickListener(this);
//        Chip nextWeekChip = view.findViewById(R.id.next_week_chip);
//       // nextWeekChip.setOnClickListener(this);
//
//
//        return view;
//    }
//
//    private void saveTask() {
//        String task = enterTodo.getText().toString();
//
//    }
//
//    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        calendarBtn.setOnClickListener(view12 -> {
//            calendarGroup.setVisibility(calendarGroup.getVisibility() == View.GONE ?
//                    View.VISIBLE : View.GONE);
//           // Utils.hideSoftKeyboard(view12);
//
//        });
//        calendarView.setOnDateChangeListener((calendarView, year, month, dayOfMoth) -> {
//            calendar.clear();
//            calendar.set(year, month, dayOfMoth);
//            dueDate = calendar.getTime();
//
//        });
//        priorityButton.setOnClickListener(view13 -> {
//            Utils.hideSoftKeyboard(view13);
//            priorityRadioGroup.setVisibility(
//                    priorityRadioGroup.getVisibility() == View.GONE ? View.VISIBLE : View.GONE
//            );
//            priorityRadioGroup.setOnCheckedChangeListener((radioGroup, checkedId) -> {
//                if (priorityRadioGroup.getVisibility() == View.VISIBLE) {
//                    selectedButtonId = checkedId;
//                    selectedRadioButton = view.findViewById(selectedButtonId);
//                    if (selectedRadioButton.getId() == R.id.radioButton_high) {
//                        priority = Priority.HIGH;
//                    } else if (selectedRadioButton.getId() == R.id.radioButton_med) {
//                        priority = Priority.MEDIUM;
//                    }else if (selectedRadioButton.getId() == R.id.radioButton_low) {
//                        priority = Priority.LOW;
//                    }else {
//                        priority = Priority.LOW;
//                    }
//                }else{
//                    priority = Priority.LOW;
//                }
//            });
//        });
//
//    }
//}
