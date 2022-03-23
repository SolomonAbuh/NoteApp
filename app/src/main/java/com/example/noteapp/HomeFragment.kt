package com.example.noteapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.noteapp.adapter.NotesAdapter
import com.example.noteapp.database.NoteDatabase
import com.example.noteapp.databinding.FragmentHomeBinding
import kotlinx.coroutines.launch

class HomeFragment : Basefragment() {

    private lateinit var fragBinding: FragmentHomeBinding
    private var adapter: NotesAdapter = NotesAdapter()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragBinding = FragmentHomeBinding.inflate(layoutInflater)


        fragBinding.noteRecyclerView.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        launch {
            context?.let {
                val notes = NoteDatabase.getDatabase(it).noteDao().getAllNote()
                adapter.setData(notes)
                fragBinding.noteRecyclerView.adapter = adapter
            }
        }

        fragBinding.createNoteBtn.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToCreateNoteFragment(0)
            findNavController().navigate(action)
        }

        adapter.setOnClickListener(onCLicked)




        return fragBinding.root
    }


    private val onCLicked = object : NotesAdapter.OnItemClickListener {
        override fun onClicked(notesId: Int) {
            val action = HomeFragmentDirections.actionHomeFragmentToCreateNoteFragment(notesId)
            findNavController().navigate(action)
        }
    }


}