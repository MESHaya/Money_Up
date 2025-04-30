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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.core.CameraSelector
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import com.google.android.material.bottomnavigation.BottomNavigationView
import data.CategoryTable.Category
import data.CategoryTable.OfflineCategoryRepository
import data.ExpenseTable.Expense
import data.ExpenseTable.OfflineExpenseRepository
import data.MoneyUpDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import android.Manifest
import android.widget.ArrayAdapter
import java.io.File
import kotlinx.coroutines.flow.first



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

    // Initialize repository for expenses and categories
    private lateinit var expenseRepository: OfflineExpenseRepository
    private lateinit var categoryRepository: OfflineCategoryRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense)

        // Set up CameraX
        previewView = findViewById(R.id.preview_view)
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        // Get instances of DAOs from the database
        val expenseDao = MoneyUpDatabase.getDatabase(applicationContext).expenseDao()
        val categoryDao = MoneyUpDatabase.getDatabase(applicationContext).categoryDao()

        // Pass DAOs into repositories
        expenseRepository = OfflineExpenseRepository(expenseDao)
        categoryRepository = OfflineCategoryRepository(categoryDao)

        // Get reference to the Spinner
        val categorySpinner = findViewById<Spinner>(R.id.spinner_category)

        CoroutineScope(Dispatchers.IO).launch {
            val categories = categoryRepository.getAllCategoriesStream().first()
            val categoryNames = categories.map { it.category_name }

            runOnUiThread {
                val adapter = ArrayAdapter(
                    this@ExpenseActivity,
                    android.R.layout.simple_spinner_item,
                    categoryNames
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                categorySpinner.adapter = adapter
            }
        }


        // Get references to UI elements
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

        // Back button functionality
        backButton.setOnClickListener {
            finish()
        }

        // Upload photo button functionality
        uploadButton.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        // Take photo button functionality
        takePhotoButton.setOnClickListener {
            takePhoto()
        }

        // Date picker dialog functionality
        dateInput.setOnClickListener {
            showDatePickerDialog(dateInput)
        }

        // Start time picker dialog functionality
        startTimeInput.setOnClickListener {
            showTimePickerDialog(startTimeInput)
        }

        // End time picker dialog functionality
        endTimeInput.setOnClickListener {
            showTimePickerDialog(endTimeInput)
        }

        // Save expense button functionality
        saveButton.setOnClickListener {
            // Retrieve the input values
            val date = dateInput.text.toString().trim()
            val startTime = startTimeInput.text.toString().trim()
            val endTime = endTimeInput.text.toString().trim()
            val amount = amountInput.text.toString().trim()
            val description = descriptionInput.text.toString().trim()
            val categoryIndex = categorySpinner.selectedItemPosition

            // Error handling: Check if all fields are filled
            if (date.isEmpty() || startTime.isEmpty() || endTime.isEmpty() || amount.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Error handling: Ensure correct amount type
            val amountValue = amount.toDoubleOrNull()
            if (amountValue == null) {
                Toast.makeText(this, "Please enter a valid amount e.g. 123.45", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Create a new Expense object and save it to the database
            val expense = Expense(
                user_id = 1, // Replace with the actual user ID
                category_id = categoryIndex + 1, // Adjust based on your category IDs
                expenseName = description,
                amount = amount.toDoubleOrNull() ?: 0.0,
                date = date,
                startTime = startTime,
                endTime = endTime,
                description = description,
                photo = imageUri?.toString() ?: ""
            )

            // Insert the expense into the database
            CoroutineScope(Dispatchers.IO).launch {
                expenseRepository.insertExpense(expense)
                runOnUiThread {
                    Toast.makeText(this@ExpenseActivity, "Expense Successfully Saved!", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }

    // Start CameraX
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            imageCapture = ImageCapture.Builder().build()
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)

        }, ContextCompat.getMainExecutor(this))
    }

    // Capture photo
    private fun takePhoto() {
        val photoFile = File(externalMediaDirs.first(), "${System.currentTimeMillis()}.jpg")
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    imageUri = savedUri
                    findViewById<ImageView>(R.id.photo_preview).setImageURI(savedUri)
                }

                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(this@ExpenseActivity, "Error capturing photo: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    // Check permissions
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }

    // Date picker dialog
    private fun showDatePickerDialog(dateInput: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val formattedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
            dateInput.setText(formattedDate)
        }, year, month, day)

        datePickerDialog.show()
    }

    // Time picker dialog
    private fun showTimePickerDialog(timeInput: EditText) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
            val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
            timeInput.setText(formattedTime)
        }, hour, minute, true)

        timePickerDialog.show()
    }
}
