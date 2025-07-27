package com.example.ocbccollecting.view;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.view.menu.MenuBuilder;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public class SystemManagementLayout extends LinearLayout {
    public static final int container = 0x66000001;
    public static final int hide = 0x66000002;
    public static final int show = 0x66000003;

    private final TextView hideTextView;
    private final Button showButton;
    //日志显示
    private final TextView contentText;

    public SystemManagementLayout(Context context) {
        super(context);
        setLayoutParams(new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        setOrientation(VERTICAL);
        setBackgroundColor(Color.WHITE);

        // Main container
        LinearLayout container = new LinearLayout(context);
        container.setId(SystemManagementLayout.container);
        container.setLayoutParams(new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(400)));
        container.setOrientation(VERTICAL);

        // Top bar layout
        LinearLayout topBar = new LinearLayout(context);
        topBar.setLayoutParams(new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                dpToPx(60)));
        topBar.setGravity(Gravity.CENTER_VERTICAL | Gravity.START);
        topBar.setOrientation(HORIZONTAL);
        topBar.setPadding(dpToPx(10), dpToPx(10), dpToPx(10), dpToPx(10));

        // System management text
        TextView systemText = new TextView(context);
        systemText.setText("系统管理");
        systemText.setTextColor(Color.BLACK);
        systemText.setTextSize(21);
        topBar.addView(systemText);

        // Right side layout
        LinearLayout rightLayout = new LinearLayout(context);
        rightLayout.setLayoutParams(new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        rightLayout.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);

        // Hide text
        hideTextView = new TextView(context);
        hideTextView.setId(SystemManagementLayout.hide);
        hideTextView.setLayoutParams(new LayoutParams(
                dpToPx(60), dpToPx(60)));
        hideTextView.setGravity(Gravity.CENTER);
        hideTextView.setText("隐藏");
        hideTextView.setTextColor(Color.BLACK);
        rightLayout.addView(hideTextView);

        // More text
        TextView moreText = new TextView(context);
        moreText.setLayoutParams(new LayoutParams(
                dpToPx(60), dpToPx(60)));
        moreText.setGravity(Gravity.CENTER);
        moreText.setText("更多");
        moreText.setTextColor(Color.BLACK);
        rightLayout.addView(moreText);

        topBar.addView(rightLayout);
        container.addView(topBar);

        // Content area
        LinearLayout contentArea = new LinearLayout(context);
        contentArea.setLayoutParams(new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        contentArea.setBackgroundColor(Color.BLACK);
        contentArea.setOrientation(VERTICAL);

        // Scroll view
        ScrollView scrollView = new ScrollView(context);
        scrollView.setLayoutParams(new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        // Content text
        contentText = new TextView(context);
        contentText.setLayoutParams(new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        contentText.setPadding(dpToPx(10), dpToPx(10), dpToPx(10), dpToPx(10));
        contentText.setText("Your app name here"); // Replace with actual string resource
        contentText.setTextColor(Color.parseColor("#4CAF50"));
        contentText.setTextSize(11);

        scrollView.addView(contentText);
        contentArea.addView(scrollView);
        container.addView(contentArea);

        addView(container);

        // Show button
        showButton = new Button(context);
        showButton.setId(SystemManagementLayout.show);
        showButton.setLayoutParams(new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        showButton.setText("显示");
        showButton.setVisibility(GONE);
        addView(showButton);

        hideTextView.setOnClickListener(view -> {
            container.setVisibility(GONE);
            showButton.setVisibility(VISIBLE);
        });

        showButton.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showButton.setVisibility(GONE);
                container.setVisibility(VISIBLE);
                return false;
            }
        });

        moreText.setOnClickListener(this::showConfigurationPopup);
    }

    // In your SystemManagementLayout.java or similar file
    public void showConfigurationPopup(View anchorView) {
        // Create a ListView for the menu items
        ListView listView = new ListView(getContext());

        // Define your menu items dynamically
        List<String> menuItems = new ArrayList<>();
        menuItems.add("配置");

        // Create adapter for the list
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_list_item_1,
                menuItems);
        listView.setAdapter(adapter);

        // Create PopupWindow
        PopupWindow popupWindow = new PopupWindow(
                listView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        // Set click listener for menu items
        listView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedItem = menuItems.get(position);
            // Handle menu item selection
            handleMenuItemSelection(selectedItem);
            popupWindow.dismiss();
        });

        // Show the popup anchored to the view
        popupWindow.showAsDropDown(anchorView);
    }

    private void handleMenuItemSelection(String item) {
        switch (item) {
            case "配置":
                // Handle config 1
                break;
        }
    }


    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

}
