package com.paondev.lib.tapakasih.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.paondev.lib.tapakasih.manager.SessionManager;

/**
 * Dialog for user to input their session ID
 */
public class SessionDialog {
    private final Context context;
    private final SessionManager sessionManager;
    
    public SessionDialog(Context context, SessionManager sessionManager) {
        this.context = context;
        this.sessionManager = sessionManager;
    }
    
    /**
     * Show session ID input dialog
     */
    public void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Masukkan Session ID");
        builder.setMessage("Silakan masukkan Session ID yang Anda dapatkan dari website kami untuk melanjutkan.");
        
        // Create input field
        final EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint("Session ID Anda");
        
        // Add some padding
        int padding = (int) (48 * context.getResources().getDisplayMetrics().density);
        input.setPadding(padding, padding / 2, padding, padding / 2);
        
        // Create container for input
        LinearLayout container = new LinearLayout(context);
        container.setOrientation(LinearLayout.VERTICAL);
        container.setPadding(padding, padding, padding, padding);
        container.addView(input);
        
        builder.setView(container);
        
        // Set positive button
        builder.setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String sessionId = input.getText().toString().trim();
                if (sessionId.isEmpty()) {
                    Toast.makeText(context, "Session ID tidak boleh kosong", Toast.LENGTH_SHORT).show();
                    show(); // Show dialog again
                } else {
                    sessionManager.saveSessionId(sessionId);
                    Toast.makeText(context, "Session ID berhasil disimpan", Toast.LENGTH_SHORT).show();
                }
            }
        });
        
        // Set negative button
        builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        
        // Prevent dialog from being dismissed by clicking outside
        builder.setCancelable(false);
        
        // Create and show dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}