package uco_448237.movio.pv526.fi.muni.cz.ukol3.activity;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.Bundle;

import uco_448237.movio.pv526.fi.muni.cz.ukol3.R;

/**
 * Created by BlackMail on 2.1.2016.
 */
public class DownloadProgressDialog extends DialogFragment {

    public static final String TAG = "download_progress_dialog";

    private ProgressDialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (dialog == null) {
            dialog = new ProgressDialog(getActivity(), ProgressDialog.STYLE_SPINNER);
            dialog.setTitle(getString(R.string.downloading_data));
            dialog.setMessage(getString(R.string.action_settings));
            dialog.setCancelable(true);
            dialog.setIndeterminate(true);
        }
        return dialog;
    }

    @Override
    public void onDestroyView() {

        // I love this awesome workaround !!!!!!!!!!!!
        Dialog dialog = getDialog();
        if ((dialog != null) && getRetainInstance()) {
            dialog.setDismissMessage(null);
        }

        super.onDestroyView();
    }
}