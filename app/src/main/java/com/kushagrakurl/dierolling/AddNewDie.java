package com.kushagrakurl.dierolling;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddNewDie#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddNewDie extends Fragment implements View.OnClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    Button add;
    View view;
    EditText noOfSides;
    private UpdateDieArray diearray;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddNewDie() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddNewDie.
     */
    // TODO: Rename and change types and number of parameters
    public static AddNewDie newInstance(String param1, String param2) {
        AddNewDie fragment = new AddNewDie();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        diearray = (UpdateDieArray) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //initializing different components
        view = inflater.inflate(R.layout.fragment_add_new_die, container, false);
        add= view.findViewById(R.id.addbutton);
        noOfSides = view.findViewById(R.id.newdie);

        add.setOnClickListener(this);

        // Inflate the layout for this fragment
        return view;
    }

    //update die type list on save button click on add fragment
    @Override
    public void onClick(View view) {
        diearray.updateDieList(noOfSides.getText().toString());
        FragmentManager myfragmentManager = getActivity().getSupportFragmentManager();
        while (myfragmentManager.getBackStackEntryCount() > 0) {
            myfragmentManager.popBackStackImmediate();
        }
    }

    public interface UpdateDieArray {
        void updateDieList(String newDieSides);
    }
}