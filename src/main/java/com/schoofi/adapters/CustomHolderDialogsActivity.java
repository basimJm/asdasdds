package com.schoofi.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.schoofi.activitiess.R;
import com.schoofi.utils.CustomDialogViewHolder;
import com.schoofi.utils.DemoDialogsActivity;
import com.schoofi.utils.Dialog;
import com.schoofi.utils.DialogsFixtures;
import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;


public class CustomHolderDialogsActivity extends DemoDialogsActivity {

    public static void open(Context context) {
        context.startActivity(new Intent(context, CustomHolderDialogsActivity.class));
    }

    private DialogsList dialogsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_holder_dialogs);

        dialogsList = (DialogsList) findViewById(R.id.dialogsList);
        initAdapter();
    }

    @Override
    public void onDialogClick(Dialog dialog) {
        CustomHolderMessagesActivity.open(this);
    }

    private void initAdapter() {
        super.dialogsAdapter = new DialogsListAdapter<>(
                R.layout.item_custom_dialog_view_holder,
                CustomDialogViewHolder.class,
                super.imageLoader);

        super.dialogsAdapter.setItems(DialogsFixtures.getDialogs());

        super.dialogsAdapter.setOnDialogClickListener(this);
        super.dialogsAdapter.setOnDialogLongClickListener(this);

        dialogsList.setAdapter(super.dialogsAdapter);
    }
}
