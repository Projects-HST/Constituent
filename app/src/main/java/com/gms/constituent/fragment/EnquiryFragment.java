package com.gms.constituent.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gms.constituent.R;
import com.gms.constituent.activity.GrievanceDetailActivity;
import com.gms.constituent.activity.NewsDetailActivity;
import com.gms.constituent.adapter.GrievanceListAdapter;
import com.gms.constituent.bean.support.Grievance;
import com.gms.constituent.bean.support.GrievanceList;
import com.gms.constituent.helper.AlertDialogHelper;
import com.gms.constituent.helper.ProgressDialogHelper;
import com.gms.constituent.interfaces.DialogClickListener;
import com.gms.constituent.servicehelpers.ServiceHelper;
import com.gms.constituent.serviceinterfaces.IServiceListener;
import com.gms.constituent.utils.GMSConstants;
import com.gms.constituent.utils.PreferenceStorage;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class EnquiryFragment extends Fragment implements IServiceListener, DialogClickListener, View.OnClickListener, AdapterView.OnItemClickListener {

    private static final String TAG = NewsFragment.class.getName();
    private View rootView;
    private ServiceHelper serviceHelper;
    private ProgressDialogHelper progressDialogHelper;
    private ListView listView;
    private int ab = 0;
    private ArrayList<Grievance> grievances = new ArrayList<>();
    private GrievanceListAdapter grievanceListAdapter;
    private TextView selectedConstituency, noGrievance;

    public static PetitionFragment newInstance(int position) {
        PetitionFragment frag = new PetitionFragment();
        Bundle b = new Bundle();
        b.putInt("position", position);
        frag.setArguments(b);
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_petition, container, false);

        listView = (ListView) rootView.findViewById(R.id.list_petition);
        listView.setOnItemClickListener(this);

        noGrievance = (TextView) rootView.findViewById(R.id.no_grievance);

        selectedConstituency = (TextView) rootView.findViewById(R.id.text_constituency);
        selectedConstituency.setText(PreferenceStorage.getConstituencyName(getActivity()));

        serviceHelper = new ServiceHelper(getActivity());
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(getActivity());

        getGrievances();
        return rootView;
    }

    private void getGrievances() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(GMSConstants.KEY_USER_ID, PreferenceStorage.getUserId(getActivity()));
            jsonObject.put(GMSConstants.KEY_GRIEVANCE_TYPE, "E");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = PreferenceStorage.getClientUrl(getActivity()) + GMSConstants.GET_GRIEVANCES;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }

    @Override
    public void onClick(View v) {

    }


    @Override
    public void onAlertPositiveClicked(int tag) {

    }

    @Override
    public void onAlertNegativeClicked(int tag) {

    }

    private boolean validateResponse(JSONObject response) {
        boolean signInSuccess = false;
        if ((response != null)) {
            try {
                String status = response.getString("status");
                String msg = response.getString(GMSConstants.PARAM_MESSAGE);
                Log.d(TAG, "status val" + status + "msg" + msg);

                if ((status != null)) {
                    if (status.equalsIgnoreCase("success")) {
                        signInSuccess = true;
                    } else {
                        signInSuccess = false;
                        Log.d(TAG, "Show error dialog");
                        noGrievance.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.GONE);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return signInSuccess;
    }

    @Override
    public void onResponse(JSONObject response) {
        progressDialogHelper.hideProgressDialog();
        if (validateResponse(response)) {
            Gson gson = new Gson();
            GrievanceList grievanceList = gson.fromJson(response.toString(), GrievanceList.class);
            if (grievanceList.getGrievanceArrayList() != null && grievanceList.getGrievanceArrayList().size() > 0) {
//                    this.ongoingServiceArrayList.addAll(ongoingServiceList.getserviceArrayList());
                updateListAdapter(grievanceList.getGrievanceArrayList());
            } else {
                if (grievances != null) {
                    grievances.clear();
                    updateListAdapter(grievanceList.getGrievanceArrayList());
                }
            }
        }
    }

    protected void updateListAdapter(ArrayList<Grievance> grievanceArrayList) {
        grievances.clear();
        grievances.addAll(grievanceArrayList);
        if (grievanceListAdapter == null) {
            grievanceListAdapter = new GrievanceListAdapter(getActivity(), grievances);
            listView.setAdapter(grievanceListAdapter);
        } else {
            grievanceListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onError(String error) {
        progressDialogHelper.hideProgressDialog();
        AlertDialogHelper.showSimpleAlertDialog(getActivity(), error);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "onEvent list item clicked" + position);
        Grievance grievance = null;
        if ((grievanceListAdapter != null) && (grievanceListAdapter.ismSearching())) {
            Log.d(TAG, "while searching");
            int actualindex = grievanceListAdapter.getActualEventPos(position);
            Log.d(TAG, "actual index" + actualindex);
            grievance = grievances.get(actualindex);
        } else {
            grievance = grievances.get(position);
        }
//        PreferenceStorage.saveUserId(this, news.getid());
        Intent intent = new Intent(getActivity(), GrievanceDetailActivity.class);
        intent.putExtra("serviceObj", grievance);
        startActivity(intent);
    }
}