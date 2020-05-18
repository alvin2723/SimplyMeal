package id.ac.umn.simplymeal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import java.lang.annotation.ElementType;
import java.util.Calendar;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutUs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Element addElement = new Element();
        addElement.setTitle("Develop By: \n    Adi Wirya \n    Alvin Julian \n    Vanessa " +
                "Ardelia \n " +
                "   Ventryshia Andiyani");

        View aboutpage= new AboutPage(this)
                .isRTL(false)
                .setImage(R.drawable.logo)
                .setDescription("Simply Meal is ready to provide your daily needs for cooking by " +
                        "delivering food ingredients that have been measured according to the " +
                        "recipe to destination location.")
                .addItem(new Element().setTitle("Version 0.1"))
                .addGroup("Connect with Us")
                .addEmail("simplymeal@gmail.com")
                .addWebsite("https://www.google.com")
                .addInstagram("simply.meal")
                .addItem(createCopyright())
                .create();

        setContentView(aboutpage);
    }

    private Element createCopyright() {
        final Element copyright = new Element();
        final String copyrightString = String.format("Copyright %d by Simply Meal",
                Calendar.getInstance().get(Calendar.YEAR));
        copyright.setTitle(copyrightString);
        copyright.setIcon(R.mipmap.ic_launcher);
        copyright.setGravity(Gravity.CENTER);
        copyright.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Toast.makeText(AboutUs.this, copyrightString, Toast.LENGTH_SHORT).show();
            }
        });
        return copyright;
    }
}
