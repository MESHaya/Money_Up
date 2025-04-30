package com.example.money_up

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.widget.Button
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import data.MoneyUpDatabase
import data.CategoryTable.OfflineCategoryRepository
import data.CategoryTable.Category
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import data.UserTable.OfflineUsersRepository
import kotlinx.coroutines.launch
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.graphics.Color
import android.widget.ImageButton
import android.widget.ImageView


class CategoryActivity : AppCompatActivity() {

    //this repo handles the communication with the database
    private lateinit var repository: OfflineCategoryRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_category)

        val backButton = findViewById<ImageButton>(R.id.back_button)

        //back button functionality
        backButton.setOnClickListener {
            finish() //close the current activity and go back to the previous one
        }


        //get instance of the dao from db
        val categoryDao = MoneyUpDatabase.getDatabase(applicationContext).categoryDao()
        //pass dao into repo
        repository = OfflineCategoryRepository(categoryDao)

        //get references to UI elements
        val nameInput = findViewById<EditText>(R.id.et_category_name)
        val descriptionInput = findViewById<EditText>(R.id.et_category_description)
        val colorSpinner = findViewById<Spinner>(R.id.spinner_color)
        val iconSpinner = findViewById<Spinner>(R.id.spinner_icon)
        val addButton = findViewById<Button>(R.id.btn_add_category)

        //set up the color spinner with a simple ArrayAdapter
        val colours = resources.getStringArray(R.array.category_colors)  //spinner for category colours
        val colorAdapter = object : ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, colours) {
            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent) as TextView
                val colorName = getItem(position)

                //set the background color based on the color name
                view.setBackgroundColor(getColorFromName(colorName))
                view.setTextColor(Color.BLACK)
                return view
            }
        }

        //category spinner functionality
        //set the adapter to the spinner
        colorSpinner.adapter = colorAdapter

        //set up the icon spinner with category labels
        val categoryLabels = resources.getStringArray(R.array.category_labels)
        val iconAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categoryLabels)
        iconAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        iconSpinner.adapter = iconAdapter

        // handle the functionality of the add category button
        addButton.setOnClickListener {
            val categoryName = nameInput.text.toString().trim()
            val categoryDescription = descriptionInput.text.toString().trim()
            val categoryColor = colorSpinner.selectedItem.toString()
            val categoryIcon = iconSpinner.selectedItem.toString()

            // error handling - check if all fields are filled
            if (categoryName.isEmpty() || categoryDescription.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            //create a new category object - add to database
                val category = Category(
                    user_id = 1,
                    category_name = categoryName,
                    category_description = categoryDescription,
                    category_color = categoryColor,
                    category_icon = categoryIcon    //label selected by user = string
                )

                //insert new user into db via the repo
                CoroutineScope(Dispatchers.IO).launch {

                    repository.insertCategory(category)

                    runOnUiThread {
                        Toast.makeText(this@CategoryActivity, "Category Successfully Added!", Toast.LENGTH_SHORT)   //user feedback
                            .show()
                        finish()
                    }
                }

        }

    }
    //function to get color from the name in spinner
    private fun getColorFromName(colorName: String?): Int {
        return when (colorName) {
            "Red" -> Color.parseColor("#FF7F7F")
            "Blue" -> Color.parseColor("#ADD8E6")
            "Green" -> Color.parseColor("#90EE90")
            "Yellow" -> Color.parseColor("#FFFFE0")
            "Orange" -> Color.parseColor("#FFB347")
            "Purple" -> Color.parseColor("#DDA0DD")
            "Teal" -> Color.parseColor("#20B2AA")
            else -> Color.TRANSPARENT //default color
        }
    }

}
