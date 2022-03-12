package com.example.noteapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.noteapp.adapter.NotesAdapter
import com.example.noteapp.database.NoteDatabase
import com.example.noteapp.databinding.FragmentHomeBinding
import com.example.noteapp.entities.Notes
import kotlinx.coroutines.launch

class HomeFragment :Basefragment() {

    private lateinit var fragBinding:FragmentHomeBinding
    private var adapter : NotesAdapter = NotesAdapter()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragBinding = FragmentHomeBinding.inflate(layoutInflater)


        fragBinding.noteRecyclerView.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)

        launch {
            context?.let {
                val notes = NoteDatabase.getDatabase(it).noteDao().getAllNote()
                adapter.setData(notes)
                fragBinding.noteRecyclerView.adapter = adapter
            }
        }




        fragBinding.createNoteBtn.setOnClickListener {
//            findNavController().navigate(action)
            val action = HomeFragmentDirections.actionHomeFragmentToCreateNoteFragment(0)
            findNavController().navigate(action)
        }

        adapter.setOnClickListener(onCLicked)




        return fragBinding.root
    }


    private val onCLicked = object :NotesAdapter.OnItemClickListener{
        override fun onClicked(notesId: Int) {
            //findNavController().navigate(R.id.action_homeFragment_to_createNoteFragment)
            val action = HomeFragmentDirections.actionHomeFragmentToCreateNoteFragment(notesId)
            findNavController().navigate(action)
        }
    }



}