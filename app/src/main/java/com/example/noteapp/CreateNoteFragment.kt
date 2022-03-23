package com.example.noteapp

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.noteapp.database.NoteDatabase
import com.example.noteapp.databinding.FragmentCreateNoteBinding
import com.example.noteapp.entities.Notes
import kotlinx.coroutines.launch
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class CreateNoteFragment : Basefragment(), View.OnClickListener {

    lateinit var fragBind: FragmentCreateNoteBinding
    private lateinit var selectedColor: String
    private lateinit var colorChar: String
    private lateinit var currentDate: String
    private var selectedImagePath = ""
    private var noteId = 0
    private val args: CreateNoteFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        noteId = args.noteId

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragBind = FragmentCreateNoteBinding.inflate(layoutInflater)

        fragBind.addImageBtn.setOnClickListener {
            pickImageFromGallery()
        }

        fragBind.noteColorBlue.setOnClickListener(this)
        fragBind.noteColorWhite.setOnClickListener(this)
        fragBind.noteColorOrange.setOnClickListener(this)
        fragBind.noteColorGreen.setOnClickListener(this)
        fragBind.noteColorPink.setOnClickListener(this)

        return fragBind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // get the date you saved the notes
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        currentDate = sdf.format(Date())
        fragBind.dateTime.text = currentDate
        selectedColor = resources.getColor(R.color.colorPrimary).toString()
        colorChar = "DEFAULT"


        if (noteId != 0) {
            launch {
                context?.let {
                    val notes = NoteDatabase.getDatabase(it).noteDao().getSpecificNote(noteId)
                    fragBind.noteTagColor.setBackgroundColor(resources.getColor(R.color.ColorOrangeNote))
                    fragBind.noteTitle.setText(notes.title)
                    fragBind.noteSubTitle.setText(notes.subTitle)
                    fragBind.noteDescription.setText(notes.noteText)

                    if (notes.imgPath != "") {
                        selectedImagePath = notes.imgPath!!
                        fragBind.noteImage.setImageBitmap(BitmapFactory.decodeFile(notes.imgPath))
                        fragBind.noteImage.visibility = View.VISIBLE
                    } else {
                        fragBind.noteImage.visibility = View.GONE
                    }
                }
            }
        } else if (noteId == 0) {
            launch {
                context?.let {
                    fragBind.noteTagColor.setBackgroundColor(resources.getColor(R.color.ColorOrangeNote))
                    fragBind.noteTitle.setText("")
                    fragBind.noteSubTitle.setText("")
                    fragBind.noteDescription.setText("")
                }
            }

        }


        fragBind.doneBtn.setOnClickListener {
            saveNote()
            requestPermission()
        }
        fragBind.backBtn.setOnClickListener {
            findNavController().navigate(R.id.action_createNoteFragment_to_homeFragment)
        }
    }

    private fun saveNote() {
        if (fragBind.noteTitle.text.isNullOrEmpty()) {
            Toast.makeText(context, "Title can't be empty", Toast.LENGTH_SHORT).show()
        } else {

            findNavController().navigate(R.id.action_createNoteFragment_to_homeFragment)


            launch {
                val notes = Notes()
                notes.title = fragBind.noteTitle.text.toString()
                notes.subTitle = fragBind.noteSubTitle.text.toString()
                notes.noteText = fragBind.noteDescription.text.toString()
                notes.color = selectedColor
                notes.colorChar = colorChar
                notes.dateTime = currentDate
                notes.imgPath = selectedImagePath
                context?.let {
                    NoteDatabase.getDatabase(it).noteDao().insertNotes(notes)
                    fragBind.noteTitle.setText("")
                    fragBind.noteSubTitle.setText("")
                    fragBind.noteDescription.setText("")
                    fragBind.noteImage.visibility = View.GONE
                }


            }
        }


    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.note_color_blue -> {
                fragBind.noteColorBlueTick.visibility = View.VISIBLE
                fragBind.noteColorOrangeTick.visibility = View.GONE
                fragBind.noteColorWhiteTick.visibility = View.GONE
                fragBind.noteColorGreenTick.visibility = View.GONE
                fragBind.noteColorPinkTick.visibility = View.GONE

                fragBind.noteTagColor.setBackgroundColor(resources.getColor(R.color.ColorBlueNote))
                selectedColor = resources.getColor(R.color.ColorBlueNote).toString()
                colorChar = "BLUE"
            }
            R.id.note_color_white -> {
                fragBind.noteColorBlueTick.visibility = View.GONE
                fragBind.noteColorOrangeTick.visibility = View.GONE
                fragBind.noteColorWhiteTick.visibility = View.VISIBLE
                fragBind.noteColorGreenTick.visibility = View.GONE
                fragBind.noteColorPinkTick.visibility = View.GONE

                fragBind.noteTagColor.setBackgroundColor(resources.getColor(R.color.ColorWhiteNote))
                selectedColor = resources.getColor(R.color.ColorWhiteNote).toString()
                colorChar = "WHITE"
            }
            R.id.note_color_orange -> {
                fragBind.noteColorBlueTick.visibility = View.GONE
                fragBind.noteColorOrangeTick.visibility = View.VISIBLE
                fragBind.noteColorWhiteTick.visibility = View.GONE
                fragBind.noteColorGreenTick.visibility = View.GONE
                fragBind.noteColorPinkTick.visibility = View.GONE


                fragBind.noteTagColor.setBackgroundColor(resources.getColor(R.color.ColorOrangeNote))
                selectedColor = resources.getColor(R.color.ColorOrangeNote).toString()
                colorChar = "ORANGE"
            }
            R.id.note_color_green -> {
                fragBind.noteColorBlueTick.visibility = View.GONE
                fragBind.noteColorOrangeTick.visibility = View.GONE
                fragBind.noteColorWhiteTick.visibility = View.GONE
                fragBind.noteColorGreenTick.visibility = View.VISIBLE
                fragBind.noteColorPinkTick.visibility = View.GONE


                fragBind.noteTagColor.setBackgroundColor(resources.getColor(R.color.ColorGreenNote))
                selectedColor = resources.getColor(R.color.ColorGreenNote).toString()
                colorChar = "GREEN"
            }
            R.id.note_color_pink -> {
                fragBind.noteColorBlueTick.visibility = View.GONE
                fragBind.noteColorOrangeTick.visibility = View.GONE
                fragBind.noteColorWhiteTick.visibility = View.GONE
                fragBind.noteColorGreenTick.visibility = View.GONE
                fragBind.noteColorPinkTick.visibility = View.VISIBLE

                fragBind.noteTagColor.setBackgroundColor(resources.getColor(R.color.ColorPurpleNote))
                selectedColor = resources.getColor(R.color.ColorPurpleNote).toString()
                colorChar = "PINK"
            }
        }
    }

    private fun hasCameraPermmision() = ActivityCompat
        .checkSelfPermission(
            requireActivity(),
            android.Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

    private fun hasReadStoragePermmision() = ActivityCompat
        .checkSelfPermission(
            requireActivity(),
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

    private fun requestPermission() {
        var permissionsToRequest = mutableListOf<String>()
        if (!hasCameraPermmision()) permissionsToRequest.add(Manifest.permission.CAMERA)
        if (!hasReadStoragePermmision()) permissionsToRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                permissionsToRequest.toTypedArray(),
                0
            )
        }

    }

    private fun pickImageFromGallery() {
        if (hasReadStoragePermmision()) {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, GALLERY)
        } else {
            requestPermission()
        }
    }


    private fun getPathFromUri(contentUri: Uri): String? {
        var filePath: String? = null
        val cursor = requireActivity().contentResolver.query(contentUri, null, null, null, null)
        if (cursor == null) {
            filePath = contentUri.path
        } else {
            cursor.moveToFirst()
            var index = cursor.getColumnIndex("_data")
            filePath = cursor.getString(index)
            cursor.close()
        }
        return filePath
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY && resultCode == RESULT_OK) {
            if (data != null) {
                val selectedImage = data.data
                if (selectedImage != null) {
                    try {
                        val inputStream =
                            requireActivity().contentResolver.openInputStream(selectedImage)
                        val bitmap = BitmapFactory.decodeStream(inputStream)
                        fragBind.noteImage.setImageBitmap(bitmap)
                        fragBind.noteImage.visibility = View.VISIBLE
                        selectedImagePath = getPathFromUri(selectedImage)!!

                    } catch (e: Exception) {
                        Toast.makeText(requireActivity(), "${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }


    companion object {
        const val GALLERY = 2
    }
}