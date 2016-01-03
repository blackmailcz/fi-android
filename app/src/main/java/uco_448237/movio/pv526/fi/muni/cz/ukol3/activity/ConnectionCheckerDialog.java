package uco_448237.movio.pv526.fi.muni.cz.ukol3.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import uco_448237.movio.pv526.fi.muni.cz.ukol3.R;

/**
 * Created by BlackMail on 2.1.2016.
 */
public class ConnectionCheckerDialog extends DialogFragment {

    public static final String TAG = "connection_checker_dialog";

    public interface ConnectionCheckerDialogListener {

        void onConnectionCheckerDialogYes();
        void onConnectionCheckerDialogNo();

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (!(activity instanceof ConnectionCheckerDialogListener)) {
            throw new ClassCastException(activity.toString() + " must implement YesNoListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.warning)
                .setMessage(R.string.internet_na_try_again)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((ConnectionCheckerDialogListener) getActivity()).onConnectionCheckerDialogYes();
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((ConnectionCheckerDialogListener) getActivity()).onConnectionCheckerDialogNo();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

}
