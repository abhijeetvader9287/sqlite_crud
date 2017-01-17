package com.example.cristiano.sqlitecrud;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonCreateLocation = (Button) findViewById(R.id.buttonCreateStudent);

        // refresh list records
        countRecords();
        readRecords();

        if (buttonCreateLocation != null) {
            buttonCreateLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Context context = view.getContext();

                    // Inflate the student_input_form.xml
                    // This will make UI elements or widgets accessible using code.
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final View formElementsView;
                    formElementsView = inflater.inflate(R.layout.student_input_form, null, false);

                    // List down form widgets inside student_input_form.xml as “final” variables.
                    // This is because we will use them inside an AlertDialog.
                    final EditText editTextStudentFirstname = (EditText) formElementsView.findViewById(R.id.editTextStudentFirstname);
                    final EditText editTextStudentEmail = (EditText) formElementsView.findViewById(R.id.editTextStudentEmail);

                    //Create an AlertDialog with the inflated student_input_form.xml
                    new AlertDialog.Builder(context).setView(formElementsView).setTitle("Create Student").setPositiveButton("Add",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            String studentFirstname = editTextStudentFirstname.getText().toString();
                            String studentEmail = editTextStudentEmail.getText().toString();

                            ObjectStudent objectStudent = new ObjectStudent();
                            objectStudent.firstname= studentFirstname;
                            objectStudent.email= studentEmail;

                            boolean createSuccessful = new TableControllerStudent(context).create(objectStudent);

                            if(createSuccessful){
                                Toast.makeText(context, "Student information was saved.", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(context, "Unable to save student information.", Toast.LENGTH_SHORT).show();
                            }
                            countRecords();
                            readRecords();

                            dialog.dismiss();

                        }

                    }).show();
                }
            });
        }

    }

    public void readRecords() {

        LinearLayout linearLayoutRecords = (LinearLayout) findViewById(R.id.linearLayoutRecords);
        if (linearLayoutRecords != null) {
            linearLayoutRecords.removeAllViews();
        }


        List<ObjectStudent> students = new TableControllerStudent(this).read();

        if (students.size() > 0) {
            for (ObjectStudent obj : students) {

                int id = obj.id;
                String studentFirstname = obj.firstname;
                String studentEmail = obj.email;

                String textViewContents = studentFirstname + " - " + studentEmail;

                TextView textViewStudentItem = new TextView(this);
                textViewStudentItem.setPadding(0, 10, 0, 10);
                textViewStudentItem.setText(textViewContents);
                textViewStudentItem.setTag(Integer.toString(id));
                textViewStudentItem.setOnLongClickListener(new OnLongClickListenerStudentRecord());

                if (linearLayoutRecords != null) {
                    linearLayoutRecords.addView(textViewStudentItem);
                }
            }
        }
        else {

            TextView locationItem = new TextView(this);
            locationItem.setPadding(8, 8, 8, 8);
            locationItem.setText("No records yet.");

            if (linearLayoutRecords != null) {
                linearLayoutRecords.addView(locationItem);
            }
        }

    }

    public void countRecords() {
        int recordCount = new TableControllerStudent(this).count();
        TextView textViewRecordCount = (TextView) findViewById(R.id.textViewRecordCount);
        if (textViewRecordCount != null) {
            textViewRecordCount.setText(recordCount + " records found.");
        }
    }

}
