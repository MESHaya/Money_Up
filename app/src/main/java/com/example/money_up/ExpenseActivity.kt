package com.example.money_up

import android.app.ActivityOptions
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.core.CameraSelector
import java.io.File
import android.Manifest
import android.widget.ArrayAdapter
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import data.CategoryTable.OfflineCategoryRepository
import data.ExpenseTable.Expense
import data.ExpenseTable.OfflineExpenseRepository
import data.MoneyUpDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

class ExpenseActivity : AppCompatActivity() {

    private var imageUri: Uri? = null
    private lateinit var imageCapture: ImageCapture
    private lateinit var previewView: PreviewView

    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            imageUri = uri
            findViewById<ImageView>(R.id.photo_preview).setImageURI(uri)
        }
    }

    //this repo handles the communication with the database
    private lateinit var repository: OfflineExpenseRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_expense)

        //set up the spinner with category labels
        val categoryLabels = resources.getStringArray(R.array.category_labels)
        val iconAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categoryLabels)
        iconAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        //get reference to the spinner
        val categorySpinner = findViewById<Spinner>(R.id.spinner_category)
        categorySpinner.adapter = iconAdapter

        //set up CameraX
        previewView = findViewById(R.id.preview_view)   //retrieves previewView
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS) //if permission not granted/requests necessary permission from user
        }

        //get instance of the dao from db
        val expenseDao = MoneyUpDatabase.getDatabase(applicationContext).expenseDao()
        //pass dao into repo
        repository = OfflineExpenseRepository(expenseDao)

        //get references to UI elements
        val backButton = findViewById<ImageButton>(R.id.back_button)
        val photoPreview = findViewById<ImageView>(R.id.photo_preview)
        val uploadButton = findViewById<Button>(R.id.upload_photo_button)
        val takePhotoButton = findViewById<Button>(R.id.take_photo_button)
        val saveButton = findViewById<Button>(R.id.btn_save_expense)
        val dateInput = findViewById<EditText>(R.id.et_date)
        val startTimeInput = findViewById<EditText>(R.id.et_start_time)
        val endTimeInput = findViewById<EditText>(R.id.et_end_time)
        val amountInput = findViewById<EditText>(R.id.et_amount)
        val descriptionInput = findViewById<EditText>(R.id.et_description)

        //back button functionality
        backButton.setOnClickListener {
            finish() //close the current activity and go back to the previous one
        }

        //upload photo button functionality
        uploadButton.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        //take photo button functionality - triggers method
        takePhotoButton.setOnClickListener {
            takePhoto()
        }

        //date picker dialog - triggers method
        dateInput.setOnClickListener {
            showDatePickerDialog(dateInput)
        }

        //start time picker dialog - triggers method
        startTimeInput.setOnClickListener {
            showTimePickerDialog(startTimeInput)
        }

        //end time picker dialog - triggers method
        endTimeInput.setOnClickListener {
            showTimePickerDialog(endTimeInput)
        }

        //save expense button functionality
        saveButton.setOnClickListener {
            //retrieve the input values
            val date = dateInput.text.toString().trim()
            val startTime = startTimeInput.text.toString().trim()
            val endTime = endTimeInput.text.toString().trim()
            val amount = amountInput.text.toString().trim()
            val description = descriptionInput.text.toString().trim()
            val categoryIndex = categorySpinner.selectedItemPosition // (assuming categories are indexed)

            //error handling - check if all fields are filled
            if (date.isEmpty() || startTime.isEmpty() || endTime.isEmpty() || amount.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            //error handling - ensure correct amount type
            val amountValue = amount.toDoubleOrNull()
            if (amountValue == null) {
                Toast.makeText(this, "Please enter a valid amount e.g. 123.45", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            //create a new category object - add to database
            val expense = Expense(
                user_id = 1, // Replace with the actual user ID
                category_id = categoryIndex + 1, // Adjust based on your category IDs
                expenseName = description, // Description is used as the expense name
                amount = amount.toDoubleOrNull() ?: 0.0, // Convert amount to Double with error handling
                date = date,
                time = startTime, // You can choose to store either startTime or endTime
                description = description,
                photo = imageUri?.toString() ?: "" // Convert Uri to String
            )

            //insert new user into db via the repo
            CoroutineScope(Dispatchers.IO).launch {

                repository.insertExpense(expense)

                runOnUiThread {
                    Toast.makeText(
                        this@ExpenseActivity,
                        "Expense Successfully Saved!",
                        Toast.LENGTH_SHORT
                    )   //user feedback
                        .show()
                    finish()
                }
            }
        }
    }

        //startCamera method
        private fun startCamera() {
            val cameraProviderFuture = ProcessCameraProvider.getInstance(this)  //gets instance of ProcessCameraProvider/managing camera lifecycle

            //when camera is ready
            cameraProviderFuture.addListener({
                val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
                //displays camera feed
                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)  //camera feed will be displayed in this view
                }

                //create an object to capture images
                imageCapture = ImageCapture.Builder().build()

                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)

            }, ContextCompat.getMainExecutor(this))
        }

        //takePhoto method
        private fun takePhoto() {
            val photoFile = File(externalMediaDirs.first(), "${System.currentTimeMillis()}.jpg")    //creates new file for image directory
            val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

            //method captures the photo
            imageCapture.takePicture(outputOptions, ContextCompat.getMainExecutor(this), object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    //stores the URI
                    imageUri = savedUri
                    //set image allowing user to see image
                    findViewById<ImageView>(R.id.photo_preview).setImageURI(savedUri)
                }

                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(this@ExpenseActivity, "Error capturing photo: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }

        //check permissions granted
        private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
        }

        //define static members
        companion object {
            private const val REQUEST_CODE_PERMISSIONS = 10 //used as a request code when asking for permissions
            private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)  //array holds the permissions required by the app
        }

        //showDatePickerDialog method
        private fun showDatePickerDialog(dateInput: EditText) {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                //set up date format (e.g., "YYYY-MM-DD")
                val formattedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
                dateInput.setText(formattedDate) //set the selected date in the EditText to ensure correct data from user
            }, year, month, day)

            datePickerDialog.show() //shows the DatePickerDialog to user
        }

        //showTimePickerDialog method
        private fun showTimePickerDialog(timeInput: EditText) {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
                //set up time format (e.g., "HH:MM")
                val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                timeInput.setText(formattedTime) //set the selected time in the EditText to ensure correct data from user
            }, hour, minute, true)

            timePickerDialog.show() //shows the TimePickerDialog to user
        }

    }



